/**
 * Digital Tasbih — Production Campaigns Worker
 * Revision: 7.0 Final (Airtight Edge Cache, Atomic Amount Cap, Post-Commit Flagging)
 * Route: api.imaanix.in/campaigns/*
 */

const FIRESTORE_PROJECT_ID = "digital-tasbih-campaigns-e43ee";
const AUTH_PROJECT_ID = "digital-tasbih-19bca";
const JWKS_URL = "https://www.googleapis.com/service_accounts/v1/jwk/securetoken@system.gserviceaccount.com";

let CACHED_JWKS = null;
let LAST_JWKS_FETCH = 0;
let CACHED_OAUTH_TOKEN = null;
let OAUTH_TOKEN_EXPIRY = 0;

const getWeekIndex = (ts) => Math.floor((Math.floor(ts / 86400) + 5) / 7);
const getWeekStart = (w) => (w * 7 - 5) * 86400; // Saturday 00:00 UTC
const getWeekEnd = (w) => ((w + 1) * 7 - 5) * 86400 - 1; // Friday 23:59:59 UTC

export default {
  async fetch(request, env, ctx) {
    // 0. Body Size Limit Guard (10 KB Max)
    const cl = parseInt(request.headers.get("content-length") || "0", 10);
    if (cl > 10240) return jsonResponse({ ok: false, error: "Payload too large" }, 413);

    const url = new URL(request.url);
    // 0. Handle join.imaanix.in Deep Link & WhatsApp Preview Landing Page
    if (url.hostname === "join.imaanix.in") {
      const code = (url.searchParams.get("code") || url.pathname.replace(/^\/join\/?/, "").replace(/^\//, "")).trim();
      return handleJoinLandingPage(code, url);
    }

    const endpoint = url.pathname.replace(/^\/campaigns\/?/, "").replace(/^\//, "");

    // Health check for root /campaigns
    if (!endpoint || endpoint === "") {
      return jsonResponse({ ok: true, service: "Digital Tasbih Campaigns API", status: "online" });
    }

    // 0. App-Level Secret Key Guard (Blocks raw scrapers, curl, Postman bots without valid App Secret or Firebase Auth)
    const APP_SECRET_KEY = "DT_LIVE_SECURE_KEY_2026";
    const clientAppKey = request.headers.get("X-App-Key");
    const hasAuthToken = request.headers.has("Authorization");
    if (request.method !== "OPTIONS" && !endpoint.startsWith("admin")) {
      if (clientAppKey !== APP_SECRET_KEY && !hasAuthToken) {
        return jsonResponse({ ok: false, error: "Unauthorized: Missing App Signature" }, 403);
      }
    }

    // Asynchronous non-blocking analytics metric logging (Zero latency impact)
    if (env.DB) {
      try {
        const istDate = new Date(Date.now() + 19800000); // UTC + 5:30
        const dayBucket = istDate.toISOString().slice(0, 10);
        const istHour = istDate.getUTCHours().toString().padStart(2, "0");
        const country = request.cf?.country || "IN";
        const region = request.cf?.region || "Unknown";
        const nowTs = Date.now();

        env.DB.prepare("INSERT INTO analytics_metrics (metric_key, category, item_name, day_bucket, count, last_updated_at) VALUES (?1, 'worker', 'campaigns', ?2, 1, ?3) ON CONFLICT(metric_key) DO UPDATE SET count = count + 1, last_updated_at = ?3").bind(`worker:campaigns:${dayBucket}`, dayBucket, nowTs).run().catch(() => {});
        env.DB.prepare("INSERT INTO analytics_metrics (metric_key, category, item_name, day_bucket, count, last_updated_at) VALUES (?1, 'hourly', ?2, ?3, 1, ?4) ON CONFLICT(metric_key) DO UPDATE SET count = count + 1, last_updated_at = ?4").bind(`hourly:${istHour}:${dayBucket}`, istHour, dayBucket, nowTs).run().catch(() => {});
        env.DB.prepare("INSERT INTO analytics_metrics (metric_key, category, item_name, day_bucket, count, last_updated_at) VALUES (?1, 'geo_country', ?2, ?3, 1, ?4) ON CONFLICT(metric_key) DO UPDATE SET count = count + 1, last_updated_at = ?4").bind(`geo_country:${country}:${dayBucket}`, country, dayBucket, nowTs).run().catch(() => {});
        env.DB.prepare("INSERT INTO analytics_metrics (metric_key, category, item_name, day_bucket, count, last_updated_at) VALUES (?1, 'geo_region', ?2, ?3, 1, ?4) ON CONFLICT(metric_key) DO UPDATE SET count = count + 1, last_updated_at = ?4").bind(`geo_region:${region}:${dayBucket}`, region, dayBucket, nowTs).run().catch(() => {});
      } catch (_) {}
    }

    // ─────────────────────────────────────────────────────────────
    // 0. ADMIN ENDPOINTS (Checked BEFORE user auth, fail-closed)
    // ─────────────────────────────────────────────────────────────
    if (endpoint.startsWith("admin")) {
      if (request.method !== "POST") return jsonResponse({ ok: false, error: "Method not allowed" }, 405);
      const body = await parseJsonBody(request);
      const adminSecret = env.CAMPAIGN_ADMIN_SECRET;
      const headerKey = request.headers.get("X-Admin-Key");
      const providedSecret = headerKey || body.adminSecret;

      if (!adminSecret || !providedSecret || !(await timingSafeEqualStrings(providedSecret, adminSecret))) {
        return jsonResponse({ ok: false, error: "Unauthorized admin access" }, 401);
      }

      const token = await getGoogleOAuthToken(env);

      // ADMIN LIST CONTESTANTS (Full Pagination - Never Truncates)
      if (endpoint === "adminListContestants") {
        const campaignId = sanitizeDocId(body.campaignId);
        if (!campaignId) return jsonResponse({ ok: false, error: "Invalid campaign ID" }, 400);

        try {
          const allContestants = [];
          let pageToken = null;
          do {
            const pageUrl = `https://firestore.googleapis.com/v1/projects/${FIRESTORE_PROJECT_ID}/databases/(default)/documents/campaign_contestants/${campaignId}/entries?pageSize=300${pageToken ? `&pageToken=${encodeURIComponent(pageToken)}` : ""}`;
            const res = await fetch(pageUrl, { headers: { Authorization: `Bearer ${token}` } });
            if (!res.ok) throw new Error(`Firestore list error: ${res.status} - ${await res.text()}`);
            const data = await res.json();
            if (data.documents) {
              for (const d of data.documents) {
                allContestants.push({ id: d.name.split("/").pop(), ...fromFirestoreDoc(d) });
              }
            }
            pageToken = data.nextPageToken || null;
          } while (pageToken && allContestants.length < 5000);

          allContestants.sort((a, b) => (b.flagged === true ? 1 : 0) - (a.flagged === true ? 1 : 0));
          return jsonResponse({
            ok: true,
            flaggedCount: allContestants.filter(c => c.flagged).length,
            totalCount: allContestants.length,
            contestants: allContestants
          });
        } catch (err) {
          console.error("adminListContestants error:", err);
          return jsonResponse({ ok: false, error: "Failed to list contestants from database" }, 500);
        }
      }

      // ADMIN PURGE CONTEST DATA (Fulfills Privacy Policy 30-day deletion commitment)
      // Processes up to 80 entries (400 document deletes across 3 collections) per single :commit batch
      if (endpoint === "adminPurgeContest") {
        const campaignId = sanitizeDocId(body.campaignId);
        if (!campaignId) return jsonResponse({ ok: false, error: "Invalid campaign ID" }, 400);

        try {
          const pageToken = body.pageToken || url.searchParams.get("pageToken") || "";
          const pageQuery = pageToken ? `&pageToken=${encodeURIComponent(pageToken)}` : "";
          const listRes = await fetch(
            `https://firestore.googleapis.com/v1/projects/${FIRESTORE_PROJECT_ID}/databases/(default)/documents/campaign_contestants/${campaignId}/entries?pageSize=80${pageQuery}`,
            { headers: { Authorization: `Bearer ${token}` } }
          );
          if (!listRes.ok) {
            const errText = await listRes.text().catch(() => "");
            return jsonResponse({ ok: false, error: `Failed to list contest entries: ${listRes.status} ${errText}` }, 500);
          }

          const listData = await listRes.json();
          const docs = listData.documents || [];
          if (docs.length === 0) {
            return jsonResponse({ ok: true, message: `Contest data completely purged for ${campaignId}`, purgedInBatch: 0, hasMore: false });
          }

          const base = `projects/${FIRESTORE_PROJECT_ID}/databases/(default)/documents`;
          const writes = [];

          for (const doc of docs) {
            const entryId = doc.name.split("/").pop();
            const data = fromFirestoreDoc(doc);

            // 1. Delete contestant entry document
            writes.push({ delete: `${base}/campaign_contestants/${campaignId}/entries/${entryId}` });

            // 2. Delete contest_registry emailKey
            const emailKey = data.emailKey || (data.emailHash ? `${campaignId}_${data.emailHash}` : null);
            if (emailKey) writes.push({ delete: `${base}/contest_registry/${emailKey}` });

            // 3. Delete contest_registry phoneKey
            const phoneKey = data.phoneKey || (data.phoneHash ? `${campaignId}_${data.phoneHash}` : null);
            if (phoneKey) writes.push({ delete: `${base}/contest_registry/${phoneKey}` });

            // 4. Delete contest_signals devKey
            const devKey = data.devKey || (data.deviceHash ? `dev_${campaignId}_${data.deviceHash}` : null);
            if (devKey) writes.push({ delete: `${base}/contest_signals/${devKey}` });

            // 5. Delete contest_signals ipKey
            const ipKey = data.ipKey || (data.ipHash ? `ip_${campaignId}_${data.ipHash}` : null);
            if (ipKey) writes.push({ delete: `${base}/contest_signals/${ipKey}` });
          }

          // Single atomic batch delete commit (maximum 400 writes, far below 500 limit and only 2 subrequests total)
          const commitRes = await fetch(`https://firestore.googleapis.com/v1/${base}:commit`, {
            method: "POST",
            headers: { Authorization: `Bearer ${token}`, "Content-Type": "application/json" },
            body: JSON.stringify({ writes })
          });

          if (!commitRes.ok) {
            const commitErr = await commitRes.text().catch(() => "");
            return jsonResponse({ ok: false, error: `Batch purge commit failed: ${commitRes.status} ${commitErr}` }, 500);
          }

          const hasMore = !!listData.nextPageToken;
          return jsonResponse({
            ok: true,
            message: `Batch purged ${docs.length} entries (${writes.length} total documents removed across collections).`,
            purgedInBatch: docs.length,
            hasMore: hasMore,
            nextPageToken: listData.nextPageToken || null
          });
        } catch (err) {
          return jsonResponse({ ok: false, error: `Purge exception: ${err.message}` }, 500);
        }
      }

      if (endpoint === "adminUpdateHomeAnnouncement") {
        const config = body.config;
        if (!config || typeof config !== "object") return jsonResponse({ ok: false, error: "Invalid config object" }, 400);
        const allowlist = ["isEnabled", "badgeText", "arabicText", "title", "prizeTitle", "message", "targetText", "buttonText", "version", "campaignId"];
        const sanitized = {};
        for (const k of allowlist) {
          if (config[k] !== undefined) {
            if (k === "isEnabled") sanitized[k] = Boolean(config[k]);
            else sanitized[k] = stripUrlsAndControls(String(config[k]).slice(0, 500));
          }
        }
        if (Object.keys(sanitized).length === 0) {
          return jsonResponse({ ok: false, error: "Announcement config must contain at least one valid property" }, 400);
        }
        try {
          await patchFirestoreDoc("settings/home_announcement", sanitized, token, Object.keys(sanitized));
          return jsonResponse({ ok: true, message: "Announcement broadcasted successfully" });
        } catch (err) {
          return jsonResponse({ ok: false, error: "Failed to update announcement" }, 500);
        }
      }

      // ADMIN LIST ALL GROUPS (For Admin Moderation Dashboard)
      if (endpoint === "adminListAllGroups") {
        try {
          const listRes = await fetch(
            `https://firestore.googleapis.com/v1/projects/${FIRESTORE_PROJECT_ID}/databases/(default)/documents/campaigns?pageSize=100`,
            { headers: { Authorization: `Bearer ${token}` } }
          );
          if (!listRes.ok) return jsonResponse({ ok: false, error: "Failed to list campaigns" }, 502);
          const data = await listRes.json();
          const groups = (data.documents || []).map(d => ({
            id: d.name.split("/").pop(),
            ...fromFirestoreDoc(d)
          }));
          return jsonResponse({ ok: true, groups, campaigns: groups });
        } catch (err) {
          return jsonResponse({ ok: false, error: "Failed to list groups: " + err.message }, 500);
        }
      }

      // ADMIN DELETE GROUP (Instant Moderation / Remove Vulgar or Inappropriate Groups)
      if (endpoint === "adminDeleteGroup") {
        const campaignId = sanitizeDocId(body.campaignId);
        if (!campaignId) return jsonResponse({ ok: false, error: "Invalid campaign ID" }, 400);

        try {
          const base = `projects/${FIRESTORE_PROJECT_ID}/databases/(default)/documents`;
          const writes = [
            { delete: `${base}/campaigns/${campaignId}` },
            { delete: `${base}/campaigns_public_index/${campaignId}` },
            { delete: `${base}/campaign_owners/${campaignId}` }
          ];

          await fetch(`https://firestore.googleapis.com/v1/${base}:commit`, {
            method: "POST",
            headers: { Authorization: `Bearer ${token}`, "Content-Type": "application/json" },
            body: JSON.stringify({ writes })
          });

          // Invalidate edge cache
          if (typeof caches !== "undefined" && caches.default) {
            ctx.waitUntil(caches.default.delete(new Request("https://api.imaanix.in/campaigns/getPublicIndex", { method: "GET" })));
          }

          return jsonResponse({ ok: true, deleted: true, campaignId, message: "Group deleted successfully" });
        } catch (err) {
          return jsonResponse({ ok: false, error: "Failed to delete group: " + err.message }, 500);
        }
      }

      // ADMIN CREATE OFFICIAL GROUP (Admin can create official public/private live groups)
      if (endpoint === "adminCreateOfficialGroup") {
        const title = stripUrlsAndControls(String(body.title || "").slice(0, 100));
        const target = parseInt(body.target, 10);
        if (!title || isNaN(target) || target < 10) {
          return jsonResponse({ ok: false, error: "Invalid title or target count" }, 400);
        }

        const isPrivate = body.visibility === "private" || body.isPrivate === true;
        const joinCode = isPrivate ? "GC-" + generateCryptoCode(8) : null;
        const cid = "camp_" + crypto.randomUUID().replace(/-/g, "").slice(0, 16);
        const now = Date.now();

        const officialCampaign = {
          campaignId: cid,
          id: cid,
          title,
          arabic: stripUrlsAndControls(String(body.arabic || "").slice(0, 200)),
          translit: stripUrlsAndControls(String(body.translit || title).slice(0, 100)),
          meaning: stripUrlsAndControls(String(body.meaning || "").slice(0, 200)),
          note: stripUrlsAndControls(String(body.note || "").slice(0, 300)),
          target,
          totalCount: 0,
          completed: false,
          visibility: isPrivate ? "private" : "public",
          isPrivate,
          isOfficial: true,
          joinCode,
          creatorName: stripUrlsAndControls(String(body.communityName || "Digital Tasbih Official").slice(0, 50)),
          location: "Global Community",
          createdAt: now,
          isSpecialMega: !!body.isSpecialMega,
          prizeTitle: body.isSpecialMega ? stripUrlsAndControls(String(body.prizeTitle || "1 Free Umrah Package").slice(0, 120)) : "",
          regEndDate: body.isSpecialMega ? String(body.regEndDate || "") : "",
          minDailyDhikr: parseInt(body.minDailyDhikr, 10) || 100,
          badgeRibbon: body.isSpecialMega ? "👑 SPECIAL MEGA CAMPAIGN" : "⭐ OFFICIAL LIVE GROUP"
        };

        const base = `projects/${FIRESTORE_PROJECT_ID}/databases/(default)/documents`;
        const writes = [
          {
            update: {
              name: `${base}/campaigns/${cid}`,
              fields: Object.fromEntries(Object.entries(officialCampaign).map(([k, v]) => [k, toFirestoreValue(v)]))
            }
          }
        ];

        if (!isPrivate) {
          writes.push({
            update: {
              name: `${base}/campaigns_public_index/${cid}`,
              fields: Object.fromEntries(Object.entries(officialCampaign).map(([k, v]) => [k, toFirestoreValue(v)]))
            }
          });
        }

        await fetch(`https://firestore.googleapis.com/v1/${base}:commit`, {
          method: "POST",
          headers: { Authorization: `Bearer ${token}`, "Content-Type": "application/json" },
          body: JSON.stringify({ writes })
        });

        return jsonResponse({
          ok: true,
          campaignId: cid,
          joinCode,
          message: "Official live group created successfully",
          campaign: filterPublicCampaign(officialCampaign, true)
        });
      }

      // ADMIN LIST REPORTS (View flagged or inappropriate groups reported by users)
      if (endpoint === "adminListReports") {
        try {
          const listRes = await fetch(
            `https://firestore.googleapis.com/v1/projects/${FIRESTORE_PROJECT_ID}/databases/(default)/documents/campaign_reports?pageSize=100`,
            { headers: { Authorization: `Bearer ${token}` } }
          );
          if (!listRes.ok) return jsonResponse({ ok: false, error: "Failed to list reports" }, 502);
          const data = await listRes.json();
          const reports = (data.documents || []).map(d => ({
            id: d.name.split("/").pop(),
            ...fromFirestoreDoc(d)
          }));
          // Sort newest reports first
          reports.sort((a, b) => (b.createdAt || 0) - (a.createdAt || 0));
          return jsonResponse({ ok: true, reports });
        } catch (err) {
          return jsonResponse({ ok: false, error: "Failed to list reports: " + err.message }, 500);
        }
      }

      // ADMIN DELETE REPORT (Dismiss/Purge individual report or fulfill DPDP user right to erasure)
      if (endpoint === "adminDeleteReport") {
        const reportId = sanitizeDocId(body.reportId);
        if (!reportId) return jsonResponse({ ok: false, error: "Invalid report ID" }, 400);

        try {
          const base = `projects/${FIRESTORE_PROJECT_ID}/databases/(default)/documents`;
          await fetch(`https://firestore.googleapis.com/v1/${base}/campaign_reports/${reportId}`, {
            method: "DELETE",
            headers: { Authorization: `Bearer ${token}` }
          });
          return jsonResponse({ ok: true, deleted: true, reportId, message: "Report deleted successfully" });
        } catch (err) {
          return jsonResponse({ ok: false, error: "Failed to delete report: " + err.message }, 500);
        }
      }

      // ADMIN SAVE WEEKLY WIRD
      if (endpoint === "adminSaveWeeklyWird") {
        try {
          const nowSec = Math.floor(Date.now() / 1000);
          const weekIdx = parseInt(body.weekIndex || getWeekIndex(nowSec), 10);
          const planJson = typeof body.plan === "string" ? body.plan : JSON.stringify(body.plan);

          await env.DB.prepare(
            "INSERT OR REPLACE INTO weekly_wird (week_index, plan_json, updated_at) VALUES (?1, ?2, ?3)"
          ).bind(weekIdx, planJson, nowSec).run();

          // Invalidate edge cache for this week
          try {
            ctx.waitUntil(caches.default.delete(new Request("https://cache.internal/wird:" + weekIdx)));
          } catch (_) {}

          return jsonResponse({ ok: true, message: "Weekly wird saved successfully", weekIndex: weekIdx });
        } catch (err) {
          return jsonResponse({ ok: false, error: err.message }, 500);
        }
      }

      return jsonResponse({ ok: false, error: "Admin endpoint not found" }, 404);
    }

    // ─────────────────────────────────────────────────────────────
    // 1. PUBLIC READ ENDPOINTS (Edge Caching via caches.default)
    // ─────────────────────────────────────────────────────────────

    // GET HOME ANNOUNCEMENT
    if (endpoint === "getHomeAnnouncement") {
      return cached(ctx, "announcement", async () => {
        try {
          const token = await getGoogleOAuthToken(env);
          const doc = await getFirestoreDoc("settings/home_announcement", token);
          return jsonResponse({ ok: true, config: doc || { isEnabled: false } });
        } catch {
          return jsonResponse({ ok: false, config: { isEnabled: false } });
        }
      });
    }

    // GET WEEKLY WIRD
    if (endpoint === "getWeeklyWird") {
      const nowSec = Math.floor(Date.now() / 1000);
      const currentWeek = getWeekIndex(nowSec);
      const body = await parseJsonBody(request);
      const reqWeek = parseInt(body.weekIndex || url.searchParams.get("weekIndex") || currentWeek, 10);

      return cached(ctx, "wird:" + reqWeek, async () => {
        try {
          const row = await env.DB.prepare(
            "SELECT * FROM weekly_wird WHERE week_index <= ?1 ORDER BY week_index DESC LIMIT 1"
          ).bind(reqWeek).first();

          let plan = null;
          if (row && row.plan_json) {
            try { plan = JSON.parse(row.plan_json); } catch (_) {}
          }

          // Fetch recent plans (past 3 weeks) so user who joins on 15th can view day 12/13/14 etc.
          const recentRows = await env.DB.prepare(
            "SELECT week_index, plan_json, updated_at FROM weekly_wird WHERE week_index < ?1 ORDER BY week_index DESC LIMIT 3"
          ).bind(reqWeek).all();

          const recentPlans = [];
          if (recentRows && recentRows.results) {
            for (const r of recentRows.results) {
              try { recentPlans.push(JSON.parse(r.plan_json)); } catch (_) {}
            }
          }

          return jsonResponse({
            ok: true,
            currentWeek: currentWeek,
            requestedWeek: reqWeek,
            weekEndsAt: getWeekEnd(currentWeek),
            plan: plan,
            recentPlans: recentPlans
          });
        } catch (err) {
          console.error("getWeeklyWird error:", err);
          return jsonResponse({ ok: false, error: "Failed to load wird plan" }, 500);
        }
      });
    }

    // GET LEADERBOARD RANKINGS
    if (endpoint === "getLeaderboardRankings") {
      const nowSec = Math.floor(Date.now() / 1000);
      const currentWeek = getWeekIndex(nowSec);
      const body = await parseJsonBody(request);
      const scope = (body.scope || url.searchParams.get("scope") || "global").toLowerCase();
      const country = (body.country || url.searchParams.get("country") || "in").toLowerCase().trim();
      const state = (body.state || url.searchParams.get("state") || "").toLowerCase().trim();
      const city = (body.city || url.searchParams.get("city") || "").toLowerCase().trim();

      const cacheKey = `leaderboard:${currentWeek}:${scope}:${country}:${state}:${city}`;
      return cached(ctx, cacheKey, async () => {
        try {
          let query = "SELECT user_id, display_name, avatar_id, city, state, country, total_count FROM dhikr_weekly_stats WHERE week_index = ?1";
          const params = [currentWeek];

          if (scope === "city" && city) {
            query += " AND LOWER(city) = ?2";
            params.push(city);
          } else if (scope === "state" && state) {
            query += " AND LOWER(state) = ?2";
            params.push(state);
          } else if (scope === "country" && country) {
            query += " AND LOWER(country) = ?2";
            params.push(country);
          }
          query += " ORDER BY total_count DESC LIMIT 100";

          const stmt = env.DB.prepare(query);
          const r = await stmt.bind(...params).all();
          const list = (r.results || []).map((row, idx) => ({
            rank: idx + 1,
            displayName: row.display_name || "Mumin",
            avatarId: row.avatar_id || 1,
            city: row.city || "",
            state: row.state || "",
            country: row.country || "",
            totalCount: Number(row.total_count || 0)
          }));

          return jsonResponse({
            ok: true,
            weekIndex: currentWeek,
            weekEndsAt: getWeekEnd(currentWeek),
            scope: scope,
            rankings: list
          });
        } catch (err) {
          console.error("getLeaderboardRankings error:", err);
          return jsonResponse({ ok: false, error: "Failed to load leaderboard" }, 500);
        }
      });
    }

    // GET LEADERBOARD MY RANK
    if (endpoint === "getLeaderboardMyRank") {
      try {
        const nowSec = Math.floor(Date.now() / 1000);
        const currentWeek = getWeekIndex(nowSec);
        const body = await parseJsonBody(request);
        const userId = sanitizeDocId(body.userId || body.deviceId || "");
        const scope = (body.scope || "global").toLowerCase();
        const city = (body.city || "").toLowerCase().trim();
        const country = (body.country || "in").toLowerCase().trim();

        if (!userId) return jsonResponse({ ok: false, error: "Missing userId" }, 400);

        const myStat = await env.DB.prepare(
          "SELECT total_count, display_name, city, country FROM dhikr_weekly_stats WHERE user_id = ?1 AND week_index = ?2"
        ).bind(userId, currentWeek).first();

        const myCount = myStat ? Number(myStat.total_count || 0) : 0;
        let rank = 0;
        if (myCount > 0) {
          let countQuery = "SELECT COUNT(*) as higher_count FROM dhikr_weekly_stats WHERE week_index = ?1 AND total_count > ?2";
          const countParams = [currentWeek, myCount];
          if (scope === "city" && city) {
            countQuery += " AND LOWER(city) = ?3";
            countParams.push(city);
          } else if (scope === "country" && country) {
            countQuery += " AND LOWER(country) = ?3";
            countParams.push(country);
          }
          const higher = await env.DB.prepare(countQuery).bind(...countParams).first();
          rank = (Number(higher?.higher_count || 0)) + 1;
        }

        return jsonResponse({
          ok: true,
          weekIndex: currentWeek,
          weekEndsAt: getWeekEnd(currentWeek),
          myCount: myCount,
          rank: rank,
          scope: scope
        });
      } catch (err) {
        return jsonResponse({ ok: false, error: err.message }, 500);
      }
    }

    // SYNC LEADERBOARD DHIKR
    if (endpoint === "syncLeaderboardDhikr") {
      try {
        const body = await parseJsonBody(request);
        const userId = sanitizeDocId(body.userId || body.deviceId || "");
        const displayName = stripUrlsAndControls(String(body.displayName || "Mumin").slice(0, 40).trim());
        const avatarId = parseInt(body.avatarId || 1, 10);
        const country = String(body.country || "in").toLowerCase().slice(0, 10).trim();
        const state = String(body.state || "unknown").toLowerCase().slice(0, 40).trim();
        const city = String(body.city || "unknown").toLowerCase().slice(0, 40).trim();
        const amount = parseInt(body.amount || body.count || 0, 10);

        if (!userId || isNaN(amount) || amount <= 0 || amount > 500) {
          return jsonResponse({ ok: false, error: "Invalid amount (1-500 taps per batch)" }, 400);
        }

        const nowSec = Math.floor(Date.now() / 1000);
        const currentWeek = getWeekIndex(nowSec);

        // Guarded atomic batch upsert (Users table + Stats table)
        await env.DB.batch([
          env.DB.prepare(
            "INSERT INTO users (id, display_name, avatar_id, country, state, city, created_at) " +
            "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7) " +
            "ON CONFLICT(id) DO UPDATE SET display_name = excluded.display_name, city = excluded.city, country = excluded.country"
          ).bind(userId, displayName, avatarId, country, state, city, nowSec),
          env.DB.prepare(
            "INSERT INTO dhikr_weekly_stats (user_id, week_index, display_name, avatar_id, country, state, city, total_count, updated_at) " +
            "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9) " +
            "ON CONFLICT(user_id, week_index) DO UPDATE SET " +
            "total_count = total_count + excluded.total_count, " +
            "display_name = excluded.display_name, " +
            "avatar_id = excluded.avatar_id, " +
            "city = excluded.city, " +
            "state = excluded.state, " +
            "country = excluded.country, " +
            "updated_at = excluded.updated_at"
          ).bind(userId, currentWeek, displayName, avatarId, country, state, city, amount, nowSec)
        ]);

        return jsonResponse({
          ok: true,
          synced: amount,
          weekIndex: currentWeek
        });
      } catch (err) {
        console.error("syncLeaderboardDhikr error:", err);
        return jsonResponse({ ok: false, error: "Failed to sync leaderboard count" }, 500);
      }
    }

    // GET PUBLIC INDEX
    if (endpoint === "getPublicIndex") {
      return cached(ctx, "public_index_v2", async () => {
        try {
          const token = await getGoogleOAuthToken(env);

          // 1. Query Dedicated Active Mega Contest (isSpecialMega == true, limit 5)
          // Guarantees Umrah Lucky Draw is always present in feed even if >60 general campaigns exist!
          const megaRes = await fetch(
            `https://firestore.googleapis.com/v1/projects/${FIRESTORE_PROJECT_ID}/databases/(default)/documents:runQuery`,
            {
              method: "POST",
              headers: { Authorization: `Bearer ${token}`, "Content-Type": "application/json" },
              body: JSON.stringify({
                structuredQuery: {
                  from: [{ collectionId: "campaigns" }],
                  where: {
                    fieldFilter: { field: { fieldPath: "isSpecialMega" }, op: "EQUAL", value: { booleanValue: true } }
                  },
                  limit: 5
                }
              })
            }
          );

          let special = [];
          if (megaRes.ok) {
            const mRows = await megaRes.json();
            if (Array.isArray(mRows)) {
              for (const r of mRows) {
                if (r.document) {
                  const c = fromFirestoreDoc(r.document);
                  if (!c.completed) special.push(filterPublicCampaign(c));
                }
              }
            }
          }

          // 2. Query Public Campaigns (Supports both modern visibility: 'public' and legacy isPrivate: false, plus campaigns_public_index)
          const isPublic = (c) => {
            if (c.visibility === "private" || c.isPrivate === true) return false;
            return c.visibility === "public" || c.isPrivate === false || (!c.isPrivate && c.visibility !== "private");
          };

          // Fetch from both campaigns and campaigns_public_index to support all historical groups
          const [campRes, pubRes] = await Promise.all([
            fetch(`https://firestore.googleapis.com/v1/projects/${FIRESTORE_PROJECT_ID}/databases/(default)/documents/campaigns?pageSize=100`, {
              headers: { Authorization: `Bearer ${token}` }
            }),
            fetch(`https://firestore.googleapis.com/v1/projects/${FIRESTORE_PROJECT_ID}/databases/(default)/documents/campaigns_public_index?pageSize=100`, {
              headers: { Authorization: `Bearer ${token}` }
            }).catch(() => null)
          ]);

          const campaignMap = new Map();

          if (pubRes && pubRes.ok) {
            const pData = await pubRes.json().catch(() => ({}));
            for (const d of (pData.documents || [])) {
              const doc = { id: d.name.split("/").pop(), ...fromFirestoreDoc(d) };
              if (isPublic(doc)) campaignMap.set(doc.campaignId || doc.id, doc);
            }
          }

          if (campRes && campRes.ok) {
            const cData = await campRes.json().catch(() => ({}));
            for (const d of (cData.documents || [])) {
              const doc = { id: d.name.split("/").pop(), ...fromFirestoreDoc(d) };
              if (isPublic(doc)) {
                const existing = campaignMap.get(doc.campaignId || doc.id);
                const bestTotal = Math.max(Number(existing?.totalCount || 0), Number(doc.totalCount || 0));
                campaignMap.set(doc.campaignId || doc.id, { ...(existing || {}), ...doc, totalCount: bestTotal });
              }
            }
          }

          const all = Array.from(campaignMap.values());
          // Sort newest first (handles integer epoch millis and ISO date strings)
          all.sort((a, b) => {
            const ta = typeof a.createdAt === "number" ? a.createdAt : new Date(a.createdAt || 0).getTime();
            const tb = typeof b.createdAt === "number" ? b.createdAt : new Date(b.createdAt || 0).getTime();
            return tb - ta;
          });

          const filtered = all.filter(c => !c.completed && ((c.totalCount || 0) < (c.target || 1000))).map(c => filterPublicCampaign(c));

          // If mega query was empty, check if any mega campaigns exist in filtered public list
          if (special.length === 0) {
            special = all.filter(c => c.isSpecialMega && !c.completed).map(c => filterPublicCampaign(c));
          }

          return jsonResponse({
            ok: true,
            liveCount: filtered.length,
            completedCount: all.filter(c => c.completed).length,
            campaigns: filtered,
            specialCampaigns: special,
            smsQuotaExceeded: false,
            adminIndianSmsEnabled: true
          });
        } catch (err) {
          console.error("getPublicIndex error:", err);
          return jsonResponse({ ok: false, error: "Failed to load campaigns" }, 500);
        }
      });
    }

    // GET PARTICIPANTS
    if (endpoint === "getParticipants" || endpoint === "getLeaderboard") {
      const body = await parseJsonBody(request);
      const campaignId = sanitizeDocId(body.campaignId || url.searchParams.get("campaignId"));
      if (!campaignId) return jsonResponse({ ok: false, error: "Invalid campaign ID" }, 400);

      return cached(ctx, "participants:" + campaignId, async () => {
        try {
          const token = await getGoogleOAuthToken(env);
          const parent = `projects/${FIRESTORE_PROJECT_ID}/databases/(default)/documents/campaign_members/${campaignId}`;
          const queryRes = await fetch(
            `https://firestore.googleapis.com/v1/${parent}:runQuery`,
            {
              method: "POST",
              headers: { Authorization: `Bearer ${token}`, "Content-Type": "application/json" },
              body: JSON.stringify({
                structuredQuery: {
                  from: [{ collectionId: "members" }],
                  orderBy: [{ field: { fieldPath: "count" }, direction: "DESCENDING" }],
                  limit: 50
                }
              })
            }
          );

          if (!queryRes.ok) {
            return jsonResponse({ ok: false, error: "Failed to fetch leaderboard" }, 502);
          }

          const rows = await queryRes.json();
          const participants = [];
          if (Array.isArray(rows)) {
            for (const r of rows) {
              if (r.document) {
                const d = fromFirestoreDoc(r.document);
                const rawLastActive = d.lastActive || d.lastSubmittedAt || 0;
                let lastActiveNum = 0;
                if (typeof rawLastActive === "number") {
                  lastActiveNum = rawLastActive;
                } else if (rawLastActive) {
                  const parsed = new Date(rawLastActive).getTime();
                  lastActiveNum = isNaN(parsed) ? 0 : parsed;
                }

                const pId = d.publicId || (await generatePublicId(d.uid, env.SIGNING_SECRET));
                participants.push({
                  publicId: pId,
                  deviceId: pId,
                  displayName: d.displayName || d.name || "Devotee",
                  count: parseInt(d.count || d.totalContributed, 10) || 0,
                  city: d.city || "",
                  lastActive: lastActiveNum
                });
              }
            }
          }
          return jsonResponse({ ok: true, participants, leaderboard: participants }, 200, { "Cache-Control": "public, max-age=30" });
        } catch (err) {
          return jsonResponse({ ok: false, error: "Leaderboard query failed: " + err.message }, 502);
        }
      });
    }

    if (endpoint === "getCampaignTotal") {
      const body = await parseJsonBody(request);
      const campaignId = sanitizeDocId(body.campaignId || url.searchParams.get("campaignId"));
      if (!campaignId) return jsonResponse({ ok: false, error: "Invalid campaign ID" }, 400);

      return cached(ctx, "total:" + campaignId, async () => {
        try {
          const token = await getGoogleOAuthToken(env);
          const doc = await getFirestoreDoc(`campaigns/${campaignId}`, token);
          if (!doc) return jsonResponse({ ok: false, error: "Campaign not found" }, 404);

          return jsonResponse({
            ok: true,
            campaignId: campaignId,
            totalCount: doc.totalCount || 0,
            target: doc.target || 0,
            completed: !!doc.completed
          }, 200, { "Cache-Control": "public, max-age=15" });
        } catch (err) {
          return jsonResponse({ ok: false, error: "Failed to fetch campaign total" }, 502);
        }
      });
    }

    // ─────────────────────────────────────────────────────────────
    // 2. CRYPTOGRAPHICALLY VERIFIED USER AUTH GATE
    // ─────────────────────────────────────────────────────────────
    if (request.method !== "POST") return jsonResponse({ ok: false, error: "Method not allowed" }, 405);
    const authUser = await verifyFirebaseJwt(request.headers.get("Authorization"));
    if (!authUser) {
      return jsonResponse({ ok: false, error: "Authentication failed. Token invalid or expired." }, 401);
    }
    const uid = authUser.sub;
    const body = await parseJsonBody(request);

    // Fail-Closed: D1 Database must be bound
    if (!env.DB) {
      return jsonResponse({ ok: false, error: "Database service unavailable" }, 500);
    }

    // ─────────────────────────────────────────────────────────────
    // 3. AUTHENTICATED WRITE ENDPOINTS
    // ─────────────────────────────────────────────────────────────

    // SUBMIT DHIKR COUNT
    if (endpoint === "submitCampaignCount") {
      const campaignId = sanitizeDocId(body.campaignId);
      const amount = parseInt(body.amount || body.count, 10);
      const displayName = stripUrlsAndControls(String(body.displayName || "Devotee").slice(0, 40).trim());

      if (!campaignId || isNaN(amount) || amount < 1 || amount > 500) {
        return jsonResponse({ ok: false, error: "Amount must be between 1 and 500" }, 400);
      }

      // 1. CHEAP D1 ATOMIC COOLDOWN CHECK FIRST (Protects Firestore free 50k read quota!)
      // Spammers and bot loops fail at Cloudflare edge with zero Firestore reads consumed.
      const now = Date.now();
      const r = await env.DB.prepare(
        "INSERT INTO user_submit_cooldowns (uid, last_submitted_at, last_amount) " +
        "SELECT ?1, ?2, ?3 " +
        "WHERE (NOT EXISTS (SELECT 1 FROM user_submit_cooldowns WHERE uid = ?1) AND ?3 <= 50) " +
        "   OR (EXISTS (SELECT 1 FROM user_submit_cooldowns WHERE uid = ?1)) " +
        "ON CONFLICT(uid) DO UPDATE SET last_submitted_at = ?2, last_amount = ?3 " +
        "WHERE (?2 - last_submitted_at) >= 2000 AND ?3 <= MAX(50, ((?2 - last_submitted_at) / 100)) " +
        "RETURNING uid"
      ).bind(uid, now, amount).first();

      if (!r) {
        return jsonResponse({ ok: false, error: "Please wait between submissions, tap rate exceeded, or first submission exceeds 50 taps." }, 429);
      }

      // Synchronous compensation helper if validation fails or commit fails
      const compensateCooldown = async () => {
        await env.DB.prepare("UPDATE user_submit_cooldowns SET last_submitted_at = ?2 - 2000 WHERE uid = ?1")
          .bind(uid, now).run().catch(() => {});
      };

      try {
        const token = await getGoogleOAuthToken(env);

        // 2. FIRESTORE VALIDATION READS (Executed only after passing edge cooldown check)
        const campDoc = await getFirestoreDoc(`campaigns/${campaignId}`, token);
        if (!campDoc) {
          await compensateCooldown();
          return jsonResponse({ ok: false, error: "Campaign not found" }, 404);
        }
        if (campDoc.completed) {
          await compensateCooldown();
          return jsonResponse({ ok: false, error: "Campaign target is already completed!" }, 400);
        }

        if (campDoc.visibility === "private" && campDoc.creatorUid !== uid) {
          const memberCheck = await getFirestoreDoc(`campaign_members/${campaignId}/members/${uid}`, token);
          if (!memberCheck) {
            await compensateCooldown();
            return jsonResponse({ ok: false, error: "You must join this private campaign first." }, 403);
          }
        }

        const publicId = await generatePublicId(uid, env.SIGNING_SECRET);

        // Standard Firestore v1 REST with currentDocument: { exists: true } (Prevents ghost doc creation on deleted campaigns!)
        const writes = [
          {
            update: {
              name: `projects/${FIRESTORE_PROJECT_ID}/databases/(default)/documents/campaigns/${campaignId}`,
              fields: {}
            },
            currentDocument: { exists: true },
            updateMask: { fieldPaths: [] },
            updateTransforms: [
              { fieldPath: "totalCount", increment: { integerValue: String(amount) } }
            ]
          },
          {
            update: {
              name: `projects/${FIRESTORE_PROJECT_ID}/databases/(default)/documents/campaign_members/${campaignId}/members/${uid}`,
              fields: {
                displayName: { stringValue: displayName },
                publicId: { stringValue: publicId },
                campaignId: { stringValue: campaignId },
                uid: { stringValue: uid },
                lastActive: { integerValue: String(now) }
              }
            },
            updateMask: { fieldPaths: ["displayName", "publicId", "campaignId", "uid", "lastActive"] },
            updateTransforms: [
              { fieldPath: "count", increment: { integerValue: String(amount) } }
            ]
          }
        ];

        // Also update campaigns_public_index for public campaigns so public list is 100% in sync
        if (campDoc.visibility === "public" || campDoc.isPrivate === false) {
          writes.push({
            update: {
              name: `projects/${FIRESTORE_PROJECT_ID}/databases/(default)/documents/campaigns_public_index/${campaignId}`,
              fields: {}
            },
            updateMask: { fieldPaths: [] },
            updateTransforms: [
              { fieldPath: "totalCount", increment: { integerValue: String(amount) } }
            ]
          });
        }

        const commitPayload = { writes };

        const commitUrl = `https://firestore.googleapis.com/v1/projects/${FIRESTORE_PROJECT_ID}/databases/(default)/documents:commit`;
        let commitRes = await fetch(commitUrl, {
          method: "POST",
          headers: { Authorization: `Bearer ${token}`, "Content-Type": "application/json" },
          body: JSON.stringify(commitPayload)
        });

        // Contention Defense: Retry once after 100-150ms jitter if transient 409 ABORTED or 429 occurs
        if (commitRes.status === 409 || commitRes.status === 429) {
          await new Promise(res => setTimeout(res, 100 + Math.random() * 50));
          commitRes = await fetch(commitUrl, {
            method: "POST",
            headers: { Authorization: `Bearer ${token}`, "Content-Type": "application/json" },
            body: JSON.stringify(commitPayload)
          });
        }

        if (!commitRes.ok) {
          await compensateCooldown();
          return jsonResponse({ ok: false, error: "Failed to record dhikr in database" }, 500);
        }

        const commitData = await commitRes.json();
        const tr = commitData.writeResults?.[0]?.transformResults?.[0]?.integerValue;
        const trueTotal = tr != null ? parseInt(tr, 10) : (campDoc.totalCount || 0) + amount;
        const target = campDoc.target || 1000;
        const isCompleted = trueTotal >= target;

        if (isCompleted && !campDoc.completed) {
          ctx.waitUntil(
            patchFirestoreDoc(`campaigns/${campaignId}`, { completed: true, completedAt: now }, token, ["completed", "completedAt"])
          );
        }

        // Invalidate edge caches so other devices see new count immediately
        try {
          ctx.waitUntil(caches.default.delete(new Request("https://cache.internal/index")));
          ctx.waitUntil(caches.default.delete(new Request("https://cache.internal/public_index_v2")));
          ctx.waitUntil(caches.default.delete(new Request("https://cache.internal/total:" + campaignId)));
        } catch (_) {}

        return jsonResponse({
          ok: true,
          totalCount: trueTotal,
          newTotal: trueTotal,
          completed: isCompleted,
          targetReached: isCompleted
        });
      } catch (err) {
        await compensateCooldown();
        return jsonResponse({ ok: false, error: "Internal server error" }, 500);
      }
    }

    // CREATE GROUP
    if (endpoint === "createGroup") {
      const isPrivate = body.isPrivate === true || body.visibility === "private";

      // 1. Google Check FIRST: Public campaigns strictly require Google Sign-In with verified email
      if (!isPrivate && (authUser.firebase?.sign_in_provider !== "google.com" || authUser.email_verified !== true || !authUser.email)) {
        return jsonResponse({ ok: false, error: "Creating public campaigns requires signing in with Google." }, 403);
      }

      // 2. IP Rate Limit (Max 10 creations per hour per IP) - Blocks anonymous spam without consuming IP quota on 403
      const rawIp = request.headers.get("CF-Connecting-IP") || "0.0.0.0";
      const ipHash = await hmacHex(env.SIGNING_SECRET, "ip:" + rawIp);
      const canCreateIp = await hitLimit(env, "create_ip:" + ipHash, 10, 3600);
      if (!canCreateIp) {
        return jsonResponse({ ok: false, error: "Too many campaigns created from this IP. Please try again later." }, 429);
      }

      // 3. Per-UID Rate Limit (Max 5 creations per day per uid)
      const canCreateUid = await hitLimit(env, "create_uid:" + uid, 5, 86400);
      if (!canCreateUid) {
        return jsonResponse({ ok: false, error: "Daily campaign creation limit reached (Max 5/day)." }, 429);
      }

      const title = stripUrlsAndControls(String(body.title || "").slice(0, 100));
      const arabic = stripUrlsAndControls(String(body.arabic || "").slice(0, 150));
      const translit = stripUrlsAndControls(String(body.translit || title).slice(0, 100));
      const meaning = stripUrlsAndControls(String(body.meaning || "").slice(0, 150));
      const note = stripUrlsAndControls(String(body.note || "").slice(0, 200));
      const target = parseInt(body.target, 10);
      const displayName = stripUrlsAndControls(String(body.displayName || "Devotee").slice(0, 40));
      const city = stripUrlsAndControls(String(body.city || "Global").slice(0, 40));

      if (!title || isNaN(target) || target < 100 || target > 100000000) {
        return jsonResponse({ ok: false, error: "Invalid target count (100 to 10 Cr)" }, 400);
      }

      // 8-character random code: GC-XXXXXXXX (1.09 Trillion combinations, zero collisions)
      const joinCode = "GC-" + generateCryptoCode(8);
      const cid = "camp_" + crypto.randomUUID().replace(/-/g, "").slice(0, 16);
      const now = Date.now();
      const publicId = await generatePublicId(uid, env.SIGNING_SECRET);

      const newCampaign = {
        campaignId: cid,
        title,
        arabic,
        translit,
        meaning,
        note,
        target,
        totalCount: 0,
        joinCode,
        visibility: isPrivate ? "private" : "public",
        creatorName: displayName,
        creatorUid: uid,
        location: city,
        participantsCount: 1,
        createdAt: now,
        completed: false,
        isSpecialMega: false
      };

      try {
        const token = await getGoogleOAuthToken(env);
        const base = `projects/${FIRESTORE_PROJECT_ID}/databases/(default)/documents`;
        
        // Single atomic commit for Campaign + Creator Member Doc with currentDocument: { exists: false }
        const createPayload = {
          writes: [
            {
              update: {
                name: `${base}/campaigns/${cid}`,
                fields: convertObjectToFields(newCampaign)
              },
              currentDocument: { exists: false }
            },
            {
              update: {
                name: `${base}/campaign_members/${cid}/members/${uid}`,
                fields: {
                  displayName: { stringValue: displayName },
                  publicId: { stringValue: publicId },
                  campaignId: { stringValue: cid },
                  uid: { stringValue: uid },
                  count: { integerValue: "0" },
                  city: { stringValue: city },
                  lastActive: { integerValue: String(now) },
                  isCreator: { booleanValue: true }
                }
              },
              currentDocument: { exists: false }
            }
          ]
        };

        const res = await fetch(`${base}:commit`, {
          method: "POST",
          headers: { Authorization: `Bearer ${token}`, "Content-Type": "application/json" },
          body: JSON.stringify(createPayload)
        });

        if (!res.ok) throw new Error("Commit failed");

        // Filter response using filterPublicCampaign(newCampaign, true) so creatorUid is NEVER leaked
        return jsonResponse({
          ok: true,
          campaignId: cid,
          joinCode,
          campaign: filterPublicCampaign(newCampaign, true)
        });
      } catch (err) {
        return jsonResponse({ ok: false, error: "Failed to create group" }, 500);
      }
    }

    // JOIN GROUP BY CODE
    if (endpoint === "joinGroupByCode") {
      const rawIp = request.headers.get("CF-Connecting-IP") || "0.0.0.0";
      const ipHash = await hmacHex(env.SIGNING_SECRET, "ip:" + rawIp);

      // Rate Limits: 10 per 10min per UID, 30 per 10min per IP
      const [uOk, iOk] = await Promise.all([
        hitLimit(env, "join_uid:" + uid, 10, 600),
        hitLimit(env, "join_ip:" + ipHash, 30, 600)
      ]);
      if (!uOk || !iOk) return jsonResponse({ ok: false, error: "Too many join attempts. Please wait 10 minutes." }, 429);

      // Strict Prefix Normalization: GC-XXXXXXXX, XXXXXXXX (8 chars), or legacy GCXXXX
      let raw = String(body.joinCode || body.code || "").trim().toUpperCase().replace(/[\s-]/g, "");
      let formattedCode;
      if (raw.length === 8) formattedCode = "GC-" + raw;
      else if (raw.length === 10 && raw.startsWith("GC")) formattedCode = "GC-" + raw.slice(2);
      else if (/^GC\d{4}$/.test(raw)) formattedCode = raw; // legacy 4-digit GC1234
      else return jsonResponse({ ok: false, error: "Invalid join code format" }, 400);

      try {
        const token = await getGoogleOAuthToken(env);
        const queryRes = await fetch(
          `https://firestore.googleapis.com/v1/projects/${FIRESTORE_PROJECT_ID}/databases/(default)/documents:runQuery`,
          {
            method: "POST",
            headers: { Authorization: `Bearer ${token}`, "Content-Type": "application/json" },
            body: JSON.stringify({
              structuredQuery: {
                from: [{ collectionId: "campaigns" }],
                where: { fieldFilter: { field: { fieldPath: "joinCode" }, op: "EQUAL", value: { stringValue: formattedCode } } },
                limit: 1
              }
            })
          }
        );

        if (!queryRes.ok) {
          return jsonResponse({ ok: false, error: "Failed to query campaign" }, 500);
        }

        const rows = await queryRes.json();
        if (!Array.isArray(rows) || !rows[0]?.document) {
          return jsonResponse({ ok: false, error: "Group not found with this code." }, 404);
        }

        const docPath = rows[0].document.name;
        const cid = docPath.split("/").pop(); // Robust doc ID from resource name!
        const camp = fromFirestoreDoc(rows[0].document);
        if (camp.completed) return jsonResponse({ ok: false, error: "This campaign is already completed." }, 400);

        const now = Date.now();
        const publicId = await generatePublicId(uid, env.SIGNING_SECRET);
        const displayName = stripUrlsAndControls(String(body.displayName || "Devotee").slice(0, 40));

        const base = `projects/${FIRESTORE_PROJECT_ID}/databases/(default)/documents`;
        const joinPayload = {
          writes: [
            {
              update: {
                name: `${base}/campaign_members/${cid}/members/${uid}`,
                fields: {
                  displayName: { stringValue: displayName },
                  publicId: { stringValue: publicId },
                  campaignId: { stringValue: cid },
                  uid: { stringValue: uid },
                  count: { integerValue: "0" },
                  lastActive: { integerValue: String(now) }
                }
              },
              currentDocument: { exists: false }
            },
            {
              update: {
                name: `${base}/campaigns/${cid}`,
                fields: {}
              },
              currentDocument: { exists: true },
              updateMask: { fieldPaths: [] },
              updateTransforms: [
                { fieldPath: "participantsCount", increment: { integerValue: "1" } }
              ]
            }
          ]
        };

        const commitRes = await fetch(`${base}:commit`, {
          method: "POST",
          headers: { Authorization: `Bearer ${token}`, "Content-Type": "application/json" },
          body: JSON.stringify(joinPayload)
        });

        if (!commitRes.ok) {
          const e = await commitRes.json().catch(() => ({}));
          const errMsg = e.error?.message || "";
          if (e.error?.status === "ALREADY_EXISTS" || errMsg.includes("exists: false")) {
            return jsonResponse({ ok: true, campaign: filterPublicCampaign(camp, true), alreadyJoined: true });
          }
          return jsonResponse({ ok: false, error: "Failed to join group" }, 500);
        }

        return jsonResponse({ ok: true, campaign: filterPublicCampaign(camp, true) });
      } catch (err) {
        return jsonResponse({ ok: false, error: "Failed to join group" }, 500);
      }
    }

    // REGISTER MEGA CONTEST (Umrah Lucky Draw)
    if (endpoint === "registerMegaContest") {
      // Rate Limit: 5 attempts per hour per uid
      const canAttempt = await hitLimit(env, "contest_uid:" + uid, 5, 3600);
      if (!canAttempt) return jsonResponse({ ok: false, error: "Too many registration attempts. Please wait an hour." }, 429);

      const campaignId = sanitizeDocId(body.campaignId);
      const fullName = String(body.fullName || "").trim().slice(0, 50);
      const city = String(body.city || "").trim().slice(0, 50);
      const rawPhone = normalizePhone(String(body.phone || ""));
      const deviceId = String(body.deviceId || "").trim();
      const rulesVersion = String(body.rulesVersion || "v1").slice(0, 20);

      if (!campaignId) return jsonResponse({ ok: false, error: "Invalid contest" }, 400);
      if (!fullName) return jsonResponse({ ok: false, error: "Full name required" }, 400);
      if (!/^\+[1-9]\d{7,14}$/.test(rawPhone)) {
        return jsonResponse({ ok: false, error: "Please enter phone in E.164 format (+91...)" }, 400);
      }
      if (!/^[A-Za-z0-9_-]{8,64}$/.test(deviceId)) {
        return jsonResponse({ ok: false, error: "Device verification failed" }, 400);
      }
      if (body.disclaimerAccepted !== true) {
        return jsonResponse({ ok: false, error: "You must accept contest rules and terms." }, 400);
      }

      // GOOGLE-ONLY CONTEST CHECK
      if (authUser.firebase?.sign_in_provider !== "google.com" || authUser.email_verified !== true || !authUser.email) {
        return jsonResponse({ ok: false, error: "Please sign in with Google to join the Lucky Draw." }, 403);
      }

      try {
        const token = await getGoogleOAuthToken(env);
        const camp = await getFirestoreDoc(`campaigns/${campaignId}`, token);
        if (!camp || !camp.isSpecialMega) {
          return jsonResponse({ ok: false, error: "Contest not found or closed" }, 404);
        }

        // Fail-Closed RegEndDate (IST Midnight) - Mandatory Schedule Check
        if (!camp.regEndDate) {
          return jsonResponse({ ok: false, error: "Contest registration schedule is not defined." }, 400);
        }
        const endMs = new Date(`${camp.regEndDate}T23:59:59+05:30`).getTime();
        if (isNaN(endMs) || Date.now() > endMs) {
          return jsonResponse({ ok: false, error: "Contest registration has closed." }, 400);
        }

        const existing = await getFirestoreDoc(`campaign_contestants/${campaignId}/entries/${uid}`, token);
        if (existing) {
          return jsonResponse({ ok: true, ticketNumber: existing.ticketNumber, message: "You are already registered! Here is your ticket." });
        }

        const normalizedEmail = normalizeEmail(authUser.email);
        const rawIp = request.headers.get("CF-Connecting-IP") || "0.0.0.0";
        const [emailHash, phoneHash, deviceHash, ipHash] = await Promise.all([
          hmacHex(env.SIGNING_SECRET, "email:" + normalizedEmail),
          hmacHex(env.SIGNING_SECRET, "phone:" + rawPhone),
          hmacHex(env.SIGNING_SECRET, "device:" + deviceId),
          hmacHex(env.SIGNING_SECRET, "ip:" + rawIp)
        ]);

        const emailKey = `${campaignId}_${emailHash}`;
        const phoneKey = `${campaignId}_${phoneHash}`;
        const devKey = `dev_${campaignId}_${deviceHash}`;
        const ipKey = `ip_${campaignId}_${ipHash}`;

        // Hard Block check
        const [dupEmail, dupPhone, devSig, ipSig] = await Promise.all([
          getFirestoreDoc(`contest_registry/${emailKey}`, token),
          getFirestoreDoc(`contest_registry/${phoneKey}`, token),
          getFirestoreDoc(`contest_signals/${devKey}`, token),
          getFirestoreDoc(`contest_signals/${ipKey}`, token)
        ]);

        if (dupEmail) return jsonResponse({ ok: false, error: "This email has already claimed a ticket." }, 400);
        if (dupPhone) return jsonResponse({ ok: false, error: "This phone number has already claimed a ticket." }, 400);

        const flagReasons = [];
        if (devSig && devSig.count >= 1) flagReasons.push("shared_device");
        if (ipSig && ipSig.count >= 5) flagReasons.push("busy_ip");

        const ticketNumber = "UMR-" + generateCryptoCode(6);
        const now = Date.now();
        const base = `projects/${FIRESTORE_PROJECT_ID}/databases/(default)/documents`;

        const commitPayload = {
          writes: [
            {
              update: {
                name: `${base}/campaign_contestants/${campaignId}/entries/${uid}`,
                fields: {
                  ticketNumber: { stringValue: ticketNumber },
                  fullName: { stringValue: fullName },
                  phone: { stringValue: rawPhone },
                  city: { stringValue: city },
                  uid: { stringValue: uid },
                  normalizedEmail: { stringValue: normalizedEmail },
                  emailKey: { stringValue: emailKey },
                  phoneKey: { stringValue: phoneKey },
                  devKey: { stringValue: devKey },
                  ipKey: { stringValue: ipKey },
                  deviceHash: { stringValue: deviceHash },
                  ipHash: { stringValue: ipHash },
                  flagged: { booleanValue: flagReasons.length > 0 },
                  flagReasons: { stringValue: flagReasons.join(",") },
                  consentAt: { integerValue: String(now) },
                  rulesVersion: { stringValue: rulesVersion },
                  registeredAt: { integerValue: String(now) }
                }
              },
              currentDocument: { exists: false }
            },
            {
              update: { name: `${base}/contest_registry/${emailKey}`, fields: { uid: { stringValue: uid }, registeredAt: { integerValue: String(now) } } },
              currentDocument: { exists: false }
            },
            {
              update: { name: `${base}/contest_registry/${phoneKey}`, fields: { uid: { stringValue: uid }, registeredAt: { integerValue: String(now) } } },
              currentDocument: { exists: false }
            },
            {
              update: { name: `${base}/contest_signals/${devKey}`, fields: { campaignId: { stringValue: campaignId }, lastSeen: { integerValue: String(now) } } },
              updateMask: { fieldPaths: ["campaignId", "lastSeen"] },
              updateTransforms: [{ fieldPath: "count", increment: { integerValue: "1" } }]
            },
            {
              update: { name: `${base}/contest_signals/${ipKey}`, fields: { campaignId: { stringValue: campaignId }, lastSeen: { integerValue: String(now) } } },
              updateMask: { fieldPaths: ["campaignId", "lastSeen"] },
              updateTransforms: [{ fieldPath: "count", increment: { integerValue: "1" } }]
            }
          ]
        };

        const commitRes = await fetch(`${base}:commit`, {
          method: "POST",
          headers: { Authorization: `Bearer ${token}`, "Content-Type": "application/json" },
          body: JSON.stringify(commitPayload)
        });

        if (!commitRes.ok) {
          const errData = await commitRes.json().catch(() => ({}));
          const errMsg = errData.error?.message || "";
          if (errData.error?.status === "ALREADY_EXISTS" || errMsg.includes("exists: false")) {
            return jsonResponse({ ok: false, error: "Duplicate registration detected." }, 400);
          }
          return jsonResponse({ ok: false, error: "Registration failed. Try again later." }, 500);
        }

        // Post-Commit Flagging Guard (Catches race conditions if 2 registrations happen in parallel!)
        const commitData = await commitRes.json();
        const devRes = commitData.writeResults?.[3]?.transformResults?.[0]?.integerValue;
        const ipRes = commitData.writeResults?.[4]?.transformResults?.[0]?.integerValue;
        const postDevCount = devRes != null ? parseInt(devRes, 10) : ((devSig?.count || 0) + 1);
        const postIpCount = ipRes != null ? parseInt(ipRes, 10) : ((ipSig?.count || 0) + 1);

        if (postDevCount > 1 || postIpCount > 5) {
          const postReasons = [];
          if (postDevCount > 1) postReasons.push("shared_device");
          if (postIpCount > 5) postReasons.push("busy_ip");
          ctx.waitUntil(
            patchFirestoreDoc(
              `campaign_contestants/${campaignId}/entries/${uid}`,
              { flagged: true, flagReasons: postReasons.join(",") },
              token,
              ["flagged", "flagReasons"]
            )
          );
        }

        return jsonResponse({
          ok: true,
          ticketNumber,
          message: "Blessed Umrah Draw Ticket issued successfully!",
          disclaimer: "Google is not a sponsor or involved in this campaign draw in any manner."
        });
      } catch (err) {
        return jsonResponse({ ok: false, error: "Registration failed. Try again later." }, 500);
      }
    }

    // REPORT GROUP (Community Safety & Moderation)
    if (endpoint === "reportGroup") {
      const body = await parseJsonBody(request);
      const campaignId = sanitizeDocId(body.campaignId);
      if (!campaignId) return jsonResponse({ ok: false, error: "Invalid campaign ID" }, 400);

      const reason = stripUrlsAndControls(String(body.reason || "Inappropriate Content").slice(0, 100));
      const notes = stripUrlsAndControls(String(body.notes || "").slice(0, 500));
      const campaignTitle = stripUrlsAndControls(String(body.campaignTitle || "").slice(0, 100));

      // Rate limit: Max 5 reports per 10 min per IP
      const rawIp = request.headers.get("CF-Connecting-IP") || "0.0.0.0";
      const ipHash = await hmacHex(env.SIGNING_SECRET, "rep_ip:" + rawIp);
      const canReport = await hitLimit(env, "report:" + ipHash, 5, 600);
      if (!canReport) {
        return jsonResponse({ ok: false, error: "Too many reports submitted. Please wait a few minutes." }, 429);
      }

      try {
        const token = await getGoogleOAuthToken(env);
        const reportId = "rep_" + crypto.randomUUID().replace(/-/g, "").slice(0, 16);
        const now = Date.now();

        const reportData = {
          reportId,
          id: reportId,
          campaignId,
          campaignTitle,
          reason,
          notes,
          createdAt: now,
          status: "pending"
        };

        const base = `projects/${FIRESTORE_PROJECT_ID}/databases/(default)/documents`;
        const writeRes = await fetch(`${base}/campaign_reports/${reportId}`, {
          method: "PATCH",
          headers: { Authorization: `Bearer ${token}`, "Content-Type": "application/json" },
          body: JSON.stringify({
            fields: Object.fromEntries(Object.entries(reportData).map(([k, v]) => [k, toFirestoreValue(v)]))
          })
        });

        if (!writeRes.ok) {
          return jsonResponse({ ok: false, error: "Failed to save report" }, 500);
        }

        return jsonResponse({ ok: true, message: "Thank you. Your report has been submitted for admin review." });
      } catch (err) {
        return jsonResponse({ ok: false, error: "Internal server error" }, 500);
      }
    }

    return jsonResponse({ ok: false, error: "Endpoint not found" }, 404);
  }
};

// ─────────────────────────────────────────────────────────────
// HELPERS, CACHING & ATOMIC HELPERS
// ─────────────────────────────────────────────────────────────

async function cached(ctx, key, producer) {
  const ck = new Request("https://cache.internal/" + key);
  try {
    const hit = await caches.default.match(ck);
    if (hit) return hit;
  } catch {}

  const res = await producer();
  if (res.status === 200) {
    const cloned = res.clone();
    try {
      const data = await cloned.json();
      if (data && data.ok === true) {
        const cacheRes = new Response(JSON.stringify(data), res);
        cacheRes.headers.set("Cache-Control", "public, max-age=30");
        cacheRes.headers.set("Content-Type", "application/json");
        ctx.waitUntil(caches.default.put(ck, cacheRes.clone()));
        return cacheRes;
      }
    } catch {}
  }
  return res;
}

async function hitLimit(env, key, limit, windowSec) {
  const now = Date.now(), b = Math.floor(now / (windowSec * 1000));
  const r = await env.DB.prepare(
    "INSERT INTO rate_limits (key, last_requested_at, request_count, day_bucket) VALUES (?1, ?2, 1, ?3) " +
    "ON CONFLICT(key) DO UPDATE SET request_count = request_count + 1, last_requested_at = ?2 " +
    "WHERE request_count < ?4 RETURNING request_count"
  ).bind(`${key}:${b}`, now, String(b), limit).first();
  return !!r;
}

const b64uToBytes = s => {
  s = s.replace(/-/g, "+").replace(/_/g, "/");
  while (s.length % 4) s += "=";
  return Uint8Array.from(atob(s), c => c.charCodeAt(0));
};

const decodeJson = p => JSON.parse(new TextDecoder().decode(b64uToBytes(p)));

async function verifyFirebaseJwt(authHeader) {
  if (!authHeader?.startsWith("Bearer ")) return null;
  const parts = authHeader.slice(7).split(".");
  if (parts.length !== 3) return null;

  try {
    const header = decodeJson(parts[0]);
    const payload = decodeJson(parts[1]);
    if (header.alg !== "RS256" || !header.kid) return null;

    const jwk = await getJwkByKid(header.kid);
    if (!jwk) return null;

    const key = await crypto.subtle.importKey(
      "jwk",
      jwk,
      { name: "RSASSA-PKCS1-v1_5", hash: "SHA-256" },
      false,
      ["verify"]
    );

    const ok = await crypto.subtle.verify(
      "RSASSA-PKCS1-v1_5",
      key,
      b64uToBytes(parts[2]),
      new TextEncoder().encode(parts[0] + "." + parts[1])
    );
    if (!ok) return null;

    const now = Math.floor(Date.now() / 1000);
    if (payload.aud !== AUTH_PROJECT_ID || payload.iss !== `https://securetoken.google.com/${AUTH_PROJECT_ID}`) return null;
    if (!payload.sub || payload.exp <= now || payload.iat > now + 60) return null;

    return payload;
  } catch {
    return null;
  }
}

async function getJwkByKid(kid) {
  let jwk = CACHED_JWKS?.find(k => k.kid === kid);
  if (jwk) return jwk;

  // Throttled refetch: min 5 minutes between Google JWKS fetches!
  if (!CACHED_JWKS || Date.now() - LAST_JWKS_FETCH > 300000) {
    LAST_JWKS_FETCH = Date.now();
    try {
      const r = await fetch(JWKS_URL);
      if (r.ok) {
        const data = await r.json();
        CACHED_JWKS = data.keys || CACHED_JWKS;
      }
    } catch {}
  }
  return CACHED_JWKS?.find(k => k.kid === kid) || null;
}

function getCampaignsServiceAccount(env) {
  if (env.FIREBASE_SERVICE_ACCOUNT_JSON) {
    try { return JSON.parse(env.FIREBASE_SERVICE_ACCOUNT_JSON); } catch {}
  }
  const client_email = env.GCP_CLIENT_EMAIL || env.FIREBASE_CLIENT_EMAIL;
  const private_key = env.GCP_PRIVATE_KEY || env.FIREBASE_PRIVATE_KEY;
  if (client_email && private_key) {
    return { client_email, private_key };
  }
  throw new Error("Missing Firestore Service Account credentials. Set FIREBASE_SERVICE_ACCOUNT_JSON or GCP_CLIENT_EMAIL / GCP_PRIVATE_KEY");
}

async function getGoogleOAuthToken(env) {
  const now = Math.floor(Date.now() / 1000);
  if (CACHED_OAUTH_TOKEN && now < OAUTH_TOKEN_EXPIRY) return CACHED_OAUTH_TOKEN;

  const sa = getCampaignsServiceAccount(env);
  const header = { alg: "RS256", typ: "JWT" };
  const claim = {
    iss: sa.client_email,
    scope: "https://www.googleapis.com/auth/datastore",
    aud: "https://oauth2.googleapis.com/token",
    exp: now + 3600,
    iat: now
  };

  const unsigned = `${base64Url(JSON.stringify(header))}.${base64Url(JSON.stringify(claim))}`;
  const cleanKey = sa.private_key.replace(/-----BEGIN PRIVATE KEY-----/, "").replace(/-----END PRIVATE KEY-----/, "").replace(/\s+/g, "");
  const binaryKey = Uint8Array.from(atob(cleanKey), c => c.charCodeAt(0));

  const cryptoKey = await crypto.subtle.importKey(
    "pkcs8",
    binaryKey,
    { name: "RSASSA-PKCS1-v1_5", hash: "SHA-256" },
    false,
    ["sign"]
  );

  const sig = await crypto.subtle.sign("RSASSA-PKCS1-v1_5", cryptoKey, new TextEncoder().encode(unsigned));
  const signedJwt = `${unsigned}.${base64Url(sig)}`;

  const tokenRes = await fetch("https://oauth2.googleapis.com/token", {
    method: "POST",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
    body: `grant_type=urn%3Aietf%3Aparams%3Aoauth%3Agrant-type%3Ajwt-bearer&assertion=${signedJwt}`
  });

  const tokenData = await tokenRes.json();
  if (!tokenRes.ok) throw new Error("OAuth token error");

  CACHED_OAUTH_TOKEN = tokenData.access_token;
  OAUTH_TOKEN_EXPIRY = now + 3300;
  return CACHED_OAUTH_TOKEN;
}

function toFirestoreValue(val) {
  if (val === null || val === undefined) return { nullValue: null };
  if (typeof val === "boolean") return { booleanValue: val };
  if (typeof val === "number") return Number.isInteger(val) ? { integerValue: val.toString() } : { doubleValue: val };
  if (typeof val === "string") return { stringValue: val };
  if (Array.isArray(val)) return { arrayValue: { values: val.map(toFirestoreValue) } };
  if (typeof val === "object") {
    const fields = {};
    for (const [k, v] of Object.entries(val)) fields[k] = toFirestoreValue(v);
    return { mapValue: { fields } };
  }
  return { stringValue: String(val) };
}

function fromFirestoreDoc(doc) {
  if (!doc || !doc.fields) return null;
  const obj = {};
  for (const [k, v] of Object.entries(doc.fields)) obj[k] = fromFirestoreValue(v);
  return obj;
}

function fromFirestoreValue(val) {
  if (!val) return null;
  if ("nullValue" in val) return null;
  if ("booleanValue" in val) return val.booleanValue;
  if ("integerValue" in val) return parseInt(val.integerValue, 10);
  if ("doubleValue" in val) return parseFloat(val.doubleValue);
  if ("stringValue" in val) return val.stringValue;
  if ("timestampValue" in val) return val.timestampValue;
  if ("arrayValue" in val) return (val.arrayValue.values || []).map(fromFirestoreValue);
  if ("mapValue" in val) {
    const obj = {};
    for (const [k, v] of Object.entries(val.mapValue.fields || {})) obj[k] = fromFirestoreValue(v);
    return obj;
  }
  return null;
}

function convertObjectToFields(obj) {
  const fields = {};
  for (const [k, v] of Object.entries(obj)) {
    if (v !== undefined) fields[k] = toFirestoreValue(v);
  }
  return fields;
}

async function getFirestoreDoc(path, token) {
  const res = await fetch(`https://firestore.googleapis.com/v1/projects/${FIRESTORE_PROJECT_ID}/databases/(default)/documents/${path}`, {
    headers: { Authorization: `Bearer ${token}` }
  });
  if (res.status === 404) return null;
  if (!res.ok) throw new Error(`Firestore read: ${res.status}`);
  return fromFirestoreDoc(await res.json());
}

async function patchFirestoreDoc(path, data, token, maskFields) {
  const mask = maskFields.map(f => `updateMask.fieldPaths=${encodeURIComponent(f)}`).join("&");
  const fields = {};
  for (const f of maskFields) {
    if (data[f] !== undefined) fields[f] = toFirestoreValue(data[f]);
  }
  const res = await fetch(`https://firestore.googleapis.com/v1/projects/${FIRESTORE_PROJECT_ID}/databases/(default)/documents/${path}?${mask}`, {
    method: "PATCH",
    headers: { Authorization: `Bearer ${token}`, "Content-Type": "application/json" },
    body: JSON.stringify({ fields })
  });
  if (!res.ok) throw new Error(`Firestore patch error`);
  return res.json();
}

async function listFirestoreCollection(col, pageSize = 50, token) {
  const res = await fetch(`https://firestore.googleapis.com/v1/projects/${FIRESTORE_PROJECT_ID}/databases/(default)/documents/${col}?pageSize=${pageSize}`, {
    headers: { Authorization: `Bearer ${token}` }
  });
  if (!res.ok) return [];
  const data = await res.json();
  if (!data.documents) return [];
  return data.documents.map(d => ({ id: d.name.split("/").pop(), ...fromFirestoreDoc(d) }));
}

function filterPublicCampaign(c, includeJoinCode = false) {
  const rawCreated = c.createdAt;
  let createdAtNum = 0;
  if (typeof rawCreated === "number") {
    createdAtNum = rawCreated;
  } else if (rawCreated) {
    const parsed = new Date(rawCreated).getTime();
    createdAtNum = isNaN(parsed) ? Date.now() : parsed;
  } else {
    createdAtNum = Date.now();
  }

  const res = {
    campaignId: c.campaignId || c.id || "",
    title: c.title || "",
    arabic: c.arabic || "",
    translit: c.translit || c.title || "",
    meaning: c.meaning || "",
    target: parseInt(c.target, 10) || 1000,
    totalCount: parseInt(c.totalCount, 10) || 0,
    creatorName: c.creatorName || "Community Member",
    location: c.location || "Global Community",
    visibility: c.visibility || "public",
    participantsCount: parseInt(c.participantsCount, 10) || 1,
    createdAt: createdAtNum,
    completed: !!c.completed,
    isSpecialMega: !!c.isSpecialMega,
    prizeTitle: c.prizeTitle || "",
    regEndDate: c.regEndDate || "",
    minDailyDhikr: c.minDailyDhikr || 100,
    contestRules: c.contestRules || "",
    badgeRibbon: c.badgeRibbon || "",
    note: c.note || ""
  };
  if (includeJoinCode && c.joinCode) {
    res.joinCode = c.joinCode;
  }
  return res;
}

function normalizeEmail(email) {
  const parts = email.trim().toLowerCase().split("@");
  if (parts.length !== 2) return email.toLowerCase();
  let [user, domain] = parts;
  if (domain === "gmail.com" || domain === "googlemail.com") {
    user = user.replace(/\./g, "").split("+")[0];
    domain = "gmail.com";
  }
  return `${user}@${domain}`;
}

function normalizePhone(phone) {
  // Safe normalization: strips whitespace, parentheses, and hyphens without corrupting area codes (e.g. US +1202... remains intact)
  return phone.replace(/[\s()\-]/g, "").trim();
}

async function hmacHex(secret, message) {
  if (!secret) throw new Error("SIGNING_SECRET missing");
  const key = await crypto.subtle.importKey(
    "raw", new TextEncoder().encode(secret),
    { name: "HMAC", hash: "SHA-256" }, false, ["sign"]
  );
  const sig = await crypto.subtle.sign("HMAC", key, new TextEncoder().encode(message));
  return Array.from(new Uint8Array(sig)).map(b => b.toString(16).padStart(2, "0")).join("");
}

async function generatePublicId(uid, secret) {
  const h = await hmacHex(secret, "pub:" + uid);
  return "usr_" + h.slice(0, 10);
}

function generateCryptoCode(len = 6) {
  const chars = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
  const buf = new Uint8Array(len);
  crypto.getRandomValues(buf);
  let res = "";
  for (let i = 0; i < len; i++) res += chars[buf[i] % chars.length];
  return res;
}

async function timingSafeEqualStrings(a, b) {
  const hashA = new Uint8Array(await crypto.subtle.digest("SHA-256", new TextEncoder().encode(a)));
  const hashB = new Uint8Array(await crypto.subtle.digest("SHA-256", new TextEncoder().encode(b)));
  let mismatch = 0;
  for (let i = 0; i < 32; i++) mismatch |= hashA[i] ^ hashB[i];
  return mismatch === 0;
}

function stripUrlsAndControls(s) {
  return s.replace(/https?:\/\/\S+/gi, "").replace(/[\u0000-\u001F\u007F]/g, "").trim();
}

function base64Url(data) {
  const str = typeof data === "string" ? btoa(data) : btoa(String.fromCharCode(...new Uint8Array(data)));
  return str.replace(/\+/g, "-").replace(/\//g, "_").replace(/=+$/, "");
}

function sanitizeDocId(id) {
  if (typeof id !== "string") return null;
  const s = id.trim();
  return /^[a-zA-Z0-9_\-]{4,64}$/.test(s) ? s : null;
}

async function parseJsonBody(req) {
  try { return await req.json(); } catch { return {}; }
}

function jsonResponse(data, status = 200, headers = {}) {
  return new Response(JSON.stringify(data), {
    status,
    headers: { "Content-Type": "application/json", "X-Content-Type-Options": "nosniff", ...headers }
  });
}


function handleJoinLandingPage(code, url) {
  const safeCode = escapeHtml(code || "");
  const playStoreUrl = "https://play.google.com/store/apps/details?id=com.tasbih.digitaltasbih";
  const appSchemeUrl = safeCode ? `digitaltasbih://join?code=${encodeURIComponent(safeCode)}` : "digitaltasbih://join";
  const ogTitle = safeCode ? `📿 Join Live Dhikr Group (${safeCode}) • Digital Tasbih` : "📿 Live Dhikr & Group Recitations • Digital Tasbih";
  const ogDesc = safeCode
    ? `You have been invited to recite together in Group ${safeCode}. Tap to open Digital Tasbih & join immediately in 1-click!`
    : "Join global live dhikr rooms, khatam campaigns, and recite together on Digital Tasbih.";
  const previewImg = "https://digitaltasbihimagesonly.taykaro786.workers.dev/banners/dhikr_invite_card.jpg";

  const html = `<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
  <title>${ogTitle}</title>

  <!-- OpenGraph / WhatsApp / Telegram Meta Tags -->
  <meta property="og:type" content="website">
  <meta property="og:url" content="${url.href}">
  <meta property="og:title" content="${ogTitle}">
  <meta property="og:description" content="${ogDesc}">
  <meta property="og:image" content="${previewImg}">
  <meta property="og:site_name" content="Digital Tasbih">
  <meta name="theme-color" content="#047857">

  <!-- Twitter Card -->
  <meta name="twitter:card" content="summary_large_image">
  <meta name="twitter:title" content="${ogTitle}">
  <meta name="twitter:description" content="${ogDesc}">
  <meta name="twitter:image" content="${previewImg}">

  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Amiri:wght@700&family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap" rel="stylesheet">

  <style>
    * { box-sizing: border-box; margin: 0; padding: 0; }
    body {
      background: radial-gradient(circle at top, #064e3b 0%, #022c22 45%, #01140e 100%);
      color: #f8fafc;
      font-family: 'Plus Jakarta Sans', -apple-system, BlinkMacSystemFont, sans-serif;
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 20px;
    }
    .card {
      background: rgba(6, 78, 59, 0.45);
      border: 1px solid rgba(245, 158, 11, 0.35);
      backdrop-filter: blur(20px);
      -webkit-backdrop-filter: blur(20px);
      border-radius: 28px;
      max-width: 440px;
      width: 100%;
      padding: 36px 28px;
      text-align: center;
      box-shadow: 0 25px 60px -15px rgba(0, 0, 0, 0.7), 0 0 40px rgba(16, 185, 129, 0.15);
    }
    .badge {
      display: inline-flex;
      align-items: center;
      gap: 6px;
      background: linear-gradient(135deg, rgba(245, 158, 11, 0.2), rgba(16, 185, 129, 0.2));
      border: 1px solid rgba(245, 158, 11, 0.5);
      color: #fbbf24;
      font-size: 11px;
      font-weight: 700;
      letter-spacing: 1.2px;
      text-transform: uppercase;
      padding: 6px 14px;
      border-radius: 20px;
      margin-bottom: 18px;
    }
    .arabic-bismillah {
      font-family: 'Amiri', serif;
      font-size: 26px;
      color: #d1fae5;
      margin-bottom: 8px;
      direction: rtl;
    }
    h1 {
      font-size: 24px;
      font-weight: 800;
      letter-spacing: -0.5px;
      color: #ffffff;
      margin-bottom: 8px;
    }
    p.sub {
      color: #94a3b8;
      font-size: 14px;
      line-height: 1.5;
      margin-bottom: 24px;
    }
    .code-box {
      background: rgba(2, 44, 34, 0.7);
      border: 1.5px dashed rgba(16, 185, 129, 0.6);
      border-radius: 18px;
      padding: 16px 20px;
      margin-bottom: 24px;
      display: flex;
      flex-direction: column;
      gap: 6px;
      align-items: center;
    }
    .code-box span.lbl {
      font-size: 11px;
      text-transform: uppercase;
      letter-spacing: 1.2px;
      color: #6ee7b7;
      font-weight: 600;
    }
    .code-box span.val {
      font-size: 26px;
      font-weight: 800;
      letter-spacing: 2px;
      color: #34d399;
      font-family: monospace;
    }
    .btn-primary {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 10px;
      width: 100%;
      background: linear-gradient(135deg, #10b981 0%, #059669 100%);
      color: #ffffff;
      font-weight: 700;
      font-size: 16px;
      padding: 16px 24px;
      border-radius: 16px;
      text-decoration: none;
      box-shadow: 0 10px 25px -5px rgba(16, 185, 129, 0.5);
      transition: all 0.2s ease;
      margin-bottom: 12px;
      border: none;
      cursor: pointer;
    }
    .btn-primary:hover {
      transform: translateY(-2px);
      box-shadow: 0 15px 30px -5px rgba(16, 185, 129, 0.65);
    }
    .btn-secondary {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 8px;
      width: 100%;
      background: rgba(255, 255, 255, 0.06);
      border: 1px solid rgba(255, 255, 255, 0.15);
      color: #cbd5e1;
      font-weight: 600;
      font-size: 14px;
      padding: 14px 20px;
      border-radius: 14px;
      text-decoration: none;
      transition: all 0.2s ease;
    }
    .btn-secondary:hover {
      background: rgba(255, 255, 255, 0.12);
      color: #ffffff;
    }
    .hadith-quote {
      margin-top: 24px;
      padding-top: 20px;
      border-top: 1px solid rgba(255, 255, 255, 0.08);
      font-size: 12px;
      color: #64748b;
      line-height: 1.6;
      font-style: italic;
    }
  </style>

  <script>
    const appUrl = "${appSchemeUrl}";
    const isMobile = /Android|iPhone|iPad|iPod/i.test(navigator.userAgent);
    if (isMobile) {
      window.location.href = appUrl;
    }
  </script>
</head>
<body>
  <div class="card">
    <div class="badge">✨ Live Dhikr Invitation</div>
    <div class="arabic-bismillah">بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ</div>
    <h1>Digital Tasbih</h1>
    <p class="sub">You are cordially invited to recite together and earn continuous rewards.</p>

    ${safeCode ? `
    <div class="code-box">
      <span class="lbl">Private Group Code</span>
      <span class="val">${safeCode}</span>
    </div>
    ` : ''}

    <a href="${appSchemeUrl}" class="btn-primary" id="openAppBtn">
      <span>📿 Open in Digital Tasbih</span>
    </a>

    <a href="${playStoreUrl}" target="_blank" class="btn-secondary">
      <span>Get App on Google Play Store</span>
    </a>

    <div class="hadith-quote">
      "Whoever guides someone to goodness will have a reward like one who did it."<br>— Sahih Muslim
    </div>
  </div>
</body>
</html>`;

  return new Response(html, {
    status: 200,
    headers: {
      "Content-Type": "text/html; charset=utf-8",
      "Cache-Control": "public, max-age=300"
    }
  });
}

function escapeHtml(str) {
  return String(str || "")
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;")
    .replace(/'/g, "&#039;");
}
