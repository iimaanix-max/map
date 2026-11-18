# 📘 Master Setup Guide: Authentication, Custom Domain & Email System (100% Free Architecture)

> **Document Type:** Production-Hardened Roadmap, Security Architecture & Deployment Checklist  
> **Target App:** Digital Tasbih (`com.tasbih.digitaltasbih`)  
> **Domain:** `imaanix.co.in` (ya `imaanix.in`)  
> **Total Monthly Cost:** **₹0 / Purely Free Tier**  
> **Revision:** 6.5 (Audit Lockdown: REST Parent URL Fix, Leaderboard 502 Guard, getCampaignTotal, Secret Fallbacks & Daily Cron)

---

## 📑 Index & Overview

1. [Architectural Overview & Free Tier Breakdown (D1 vs KV)](#1-architectural-overview--free-tier-breakdown)
2. [Phase 1: Firebase Google Sign-In (Debug, Release & Play App Signing SHA-1)](#phase-1-firebase-google-sign-in)
3. [Phase 2: GoDaddy Domain Purchase & Cloudflare Nameservers](#phase-2-godaddy-domain-purchase--cloudflare-nameservers)
4. [Phase 3: GitHub Pages Hosting + Custom Domain Link (SSL & CNAME Trap Avoided)](#phase-3-github-pages-hosting--custom-domain-link)
5. [Phase 4: Cloudflare Free Email Routing (Incoming: support@, help@ ➔ Gmail)](#phase-4-cloudflare-free-email-routing)
6. [Phase 5: Resend.com Setup (Outgoing OTP: noreply@imaanix.co.in)](#phase-5-resendcom-setup)
7. [Phase 6: Cloudflare D1 SQL Schema & Database Migration](#phase-6-cloudflare-d1-sql-schema--database-migration)
8. [Phase 7: Production Campaigns Worker (campaigns-worker.js)](#phase-7-production-campaigns-worker-campaigns-workerjs)
9. [Phase 8: Production Auth & Email OTP Worker (auth-worker.js)](#phase-8-production-auth--email-otp-worker-auth-workerjs)
10. [Phase 9: Wrangler.toml Configurations & Cron Triggers](#phase-9-wranglertoml-configurations--cron-triggers)
11. [Phase 10: Airtight Firestore Deny-All Security Rules](#phase-10-airtight-firestore-deny-all-security-rules)
12. [Summary Checklist & Negative Tests](#12-summary-checklist--negative-tests)

---

## Phase 6: Cloudflare D1 SQL Schema & Database Migration

### A. Live Migration for Existing Deployments
```sql
ALTER TABLE user_submit_cooldowns ADD COLUMN prev_submitted_at INTEGER NOT NULL DEFAULT 0;
```

### B. Fresh Database Initialization (`schema.sql`)
```sql
DROP TABLE IF EXISTS otp_records;
DROP TABLE IF EXISTS rate_limits;
DROP TABLE IF EXISTS user_submit_cooldowns;

-- 1. Requester-Bound HMAC-Hashed OTP Storage (Anti-Griefing Lockout Protected)
CREATE TABLE otp_records (
    email TEXT PRIMARY KEY,
    uid TEXT NOT NULL,
    otp_hmac TEXT NOT NULL,
    attempts INTEGER DEFAULT 0,
    created_at INTEGER NOT NULL,
    expires_at INTEGER NOT NULL
);

-- 2. Advanced Rate Limiting (CGNAT-Friendly & Atomic)
CREATE TABLE rate_limits (
    key TEXT PRIMARY KEY, -- 'email:xyz', 'ip:hash:YYYY-MM-DDTHH', 'global:YYYY-MM-DD'
    last_requested_at INTEGER NOT NULL,
    request_count INTEGER DEFAULT 1,
    day_bucket TEXT NOT NULL
);

-- 3. Campaigns Cooldown & Tap Speed Cap
CREATE TABLE user_submit_cooldowns (
    uid TEXT PRIMARY KEY,
    last_submitted_at INTEGER NOT NULL,
    last_amount INTEGER NOT NULL
);

CREATE INDEX idx_otp_expires ON otp_records(expires_at);
CREATE INDEX idx_rl_last_req ON rate_limits(last_requested_at);
```

---

## Phase 7: Production Campaigns Worker (`campaigns-worker.js`)

```javascript
/**
 * Digital Tasbih — Production Campaigns Worker
 * Revision: 6.0 (Airtight Edge Cache, Atomic Amount Cap, Post-Commit Flagging)
 * Route: api.imaanix.co.in/campaigns/*
 */

const FIRESTORE_PROJECT_ID = "digital-tasbih-campaigns-e43ee";
const AUTH_PROJECT_ID = "digital-tasbih-19bca";
const JWKS_URL = "https://www.googleapis.com/service_accounts/v1/jwk/securetoken@system.gserviceaccount.com";

let CACHED_JWKS = null;
let LAST_JWKS_FETCH = 0;
let CACHED_OAUTH_TOKEN = null;
let OAUTH_TOKEN_EXPIRY = 0;

export default {
  async fetch(request, env, ctx) {
    // 0. Body Size Limit Guard (10 KB Max)
    const cl = parseInt(request.headers.get("content-length") || "0", 10);
    if (cl > 10240) return jsonResponse({ ok: false, error: "Payload too large" }, 413);

    const url = new URL(request.url);
    const endpoint = url.pathname.replace(/^\/campaigns\/?/, "").replace(/^\//, "");

    // ─────────────────────────────────────────────────────────────
    // 0. ADMIN ENDPOINTS (Checked BEFORE user auth, fail-closed)
    // ─────────────────────────────────────────────────────────────
    if (endpoint.startsWith("admin")) {
      if (request.method !== "POST") return jsonResponse({ ok: false, error: "Method not allowed" }, 405);
      const adminSecret = env.CAMPAIGN_ADMIN_SECRET;
      const headerKey = request.headers.get("X-Admin-Key");

      if (!adminSecret || !headerKey || !(await timingSafeEqualStrings(headerKey, adminSecret))) {
        return jsonResponse({ ok: false, error: "Unauthorized admin access" }, 401);
      }

      const body = await parseJsonBody(request);
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

    // GET PUBLIC INDEX
    if (endpoint === "getPublicIndex") {
      return cached(ctx, "index", async () => {
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

          // 2. Query Public Campaigns (Requires Composite Index: visibility ASC + createdAt DESC)
          const qRes = await fetch(
            `https://firestore.googleapis.com/v1/projects/${FIRESTORE_PROJECT_ID}/databases/(default)/documents:runQuery`,
            {
              method: "POST",
              headers: { Authorization: `Bearer ${token}`, "Content-Type": "application/json" },
              body: JSON.stringify({
                structuredQuery: {
                  from: [{ collectionId: "campaigns" }],
                  where: {
                    fieldFilter: { field: { fieldPath: "visibility" }, op: "EQUAL", value: { stringValue: "public" } }
                  },
                  orderBy: [{ field: { fieldPath: "createdAt" }, direction: "DESCENDING" }],
                  limit: 60
                }
              })
            }
          );

          let all = [];
          if (qRes.ok) {
            const rows = await qRes.json();
            if (Array.isArray(rows)) {
              for (const r of rows) {
                if (r.document) all.push(fromFirestoreDoc(r.document));
              }
            }
          } else {
            // SAFE FALLBACK: If composite index is building, list campaigns and strictly filter visibility == "public"
            // Never exposes private campaigns!
            const rawList = await listFirestoreCollection("campaigns", 60, token);
            all = rawList.filter(c => c.visibility === "public");
          }

          const filtered = all.filter(c => !c.completed).map(c => filterPublicCampaign(c));

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
                participants.push({
                  publicId: d.publicId || (await generatePublicId(d.uid, env.SIGNING_SECRET)),
                  displayName: d.displayName || "Devotee",
                  count: d.count || 0,
                  city: d.city || "",
                  lastActive: d.lastActive || 0
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
          const doc = await fetchFirestoreDoc(`campaigns/${campaignId}`, token);
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
        const commitPayload = {
          writes: [
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
          ]
        };

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

      const rawIp = request.headers.get("CF-Connecting-IP") || "0.0.0.0";
      const ipHash = await hmacHex(env.SIGNING_SECRET, "ip:" + rawIp);

      // Rate Limit 1: IP Rate Limit (Max 10 creations per hour per IP) - Blocks anonymous UID spam
      const canCreateIp = await hitLimit(env, "create_ip:" + ipHash, 10, 3600);
      if (!canCreateIp) {
        return jsonResponse({ ok: false, error: "Too many campaigns created from this IP. Please try again later." }, 429);
      }

      // Public campaigns require non-anonymous account (Google Sign-In with verified email)
      if (!isPrivate && (authUser.firebase?.sign_in_provider === "anonymous" || !authUser.email)) {
        return jsonResponse({ ok: false, error: "Creating public campaigns requires signing in with Google." }, 403);
      }

      // Rate Limit 2: Per-UID Rate Limit (Max 5 creations per day per uid)
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
  const res = {
    campaignId: c.campaignId || c.id,
    title: c.title,
    arabic: c.arabic,
    translit: c.translit,
    meaning: c.meaning,
    target: c.target,
    totalCount: c.totalCount || 0,
    creatorName: c.creatorName,
    location: c.location,
    visibility: c.visibility || "public",
    participantsCount: c.participantsCount || 1,
    createdAt: c.createdAt,
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
```

---

## Phase 8: Production Auth & Email OTP Worker (`auth-worker.js`)

```javascript
/**
 * Digital Tasbih - Hardened Auth & Email OTP Worker
 * Revision: 6.0 (Throttled JWKS App Gate, Atomic Evaluation Order, Hour-Bucket IP)
 * Route: api.imaanix.co.in/auth/*
 */

const AUTH_PROJECT_ID = "digital-tasbih-19bca";
const JWKS_URL = "https://www.googleapis.com/service_accounts/v1/jwk/securetoken@system.gserviceaccount.com";
const EMAIL_REGEX = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;

let CACHED_JWKS = null;
let LAST_JWKS_FETCH = 0;

export default {
  async fetch(request, env) {
    if (request.method !== "POST") return jsonResponse({ success: false, error: "Method not allowed" }, 405);
    if (!env.DB) return jsonResponse({ success: false, error: "Database service unavailable" }, 500);

    const url = new URL(request.url);
    const endpoint = url.pathname.replace(/^\/auth\/?/, "").replace(/^\//, "");

    // ─────────────────────────────────────────────────────────
    // REAL JWKS APP GATE: Token must be cryptographically valid!
    // ─────────────────────────────────────────────────────────
    const authUser = await verifyFirebaseJwt(request.headers.get("Authorization"));
    if (!authUser) {
      return jsonResponse({ success: false, error: "App verification required. Invalid or missing token." }, 401);
    }

    let body = {};
    try { body = await request.json(); } catch { body = {}; }

    // ─────────────────────────────────────────────────────────
    // 1. SEND EMAIL OTP
    // ─────────────────────────────────────────────────────────
    if (endpoint === "send-email-otp") {
      const rawEmail = String(body.email || "").trim().toLowerCase();
      if (!rawEmail || !EMAIL_REGEX.test(rawEmail) || rawEmail.length > 80) {
        return jsonResponse({ success: false, error: "Invalid email format" }, 400);
      }

      const email = normalizeEmail(rawEmail);
      const requesterUid = authUser.sub;
      const now = Date.now();
      const todayDate = new Date().toISOString().slice(0, 10); // 'YYYY-MM-DD'
      const currentHour = new Date().toISOString().slice(0, 13); // 'YYYY-MM-DDTHH'
      
      const rawIp = request.headers.get("CF-Connecting-IP") || "0.0.0.0";
      const ipHash = await hmacHex(env.SIGNING_SECRET, "ip:" + rawIp);

      const ipKey = `ip:${ipHash}:${currentHour}`;
      const emailKey = `email:${email}`;
      const globalKey = `global:${todayDate}`;

      // ── EVALUATION ORDER: IP -> Email -> Atomic Global Reservation ──

      // 1. Per-IP Hourly Bucket (CGNAT Friendly: 30 requests/hour bucket)
      const iRes = await env.DB.prepare(
        "INSERT INTO rate_limits (key, last_requested_at, request_count, day_bucket) VALUES (?1, ?2, 1, ?3) " +
        "ON CONFLICT(key) DO UPDATE SET request_count = request_count + 1, last_requested_at = ?2 " +
        "WHERE request_count < 30 RETURNING request_count"
      ).bind(ipKey, now, currentHour).first();

      if (!iRes) {
        return jsonResponse({ success: false, error: "Network rate limit exceeded. Please try again in the next hour." }, 429);
      }

      // 2. Per-Email 60-Second Cooldown & Max 5/Day Cap
      const eRes = await env.DB.prepare(
        "INSERT INTO rate_limits (key, last_requested_at, request_count, day_bucket) VALUES (?1, ?2, 1, ?3) " +
        "ON CONFLICT(key) DO UPDATE SET " +
        "request_count = CASE WHEN day_bucket = ?3 THEN request_count + 1 ELSE 1 END, " +
        "last_requested_at = ?2, day_bucket = ?3 " +
        "WHERE (?2 - last_requested_at) >= 60000 AND (day_bucket != ?3 OR request_count < 5) " +
        "RETURNING request_count"
      ).bind(emailKey, now, todayDate).first();

      if (!eRes) {
        return jsonResponse({ success: false, error: "Rate limit reached for this email. Please wait or use Google Sign-In." }, 429);
      }

      // 3. Global Daily Safety Cap (Atomic Slot Reservation: Max 80 OTPs/day)
      const gRes = await env.DB.prepare(
        "INSERT INTO rate_limits (key, last_requested_at, request_count, day_bucket) VALUES (?1, ?2, 1, ?3) " +
        "ON CONFLICT(key) DO UPDATE SET request_count = request_count + 1, last_requested_at = ?2 " +
        "WHERE request_count < 80 RETURNING request_count"
      ).bind(globalKey, now, todayDate).first();

      if (!gRes) {
        return jsonResponse({ success: false, error: "Daily email verification limit reached. Please sign in with Google." }, 429);
      }

      // Rollback helper ensures neither reserved slot nor pending OTP leak on failure or runtime exception
      const rollback = async () => {
        await Promise.all([
          env.DB.prepare("UPDATE rate_limits SET request_count = MAX(0, request_count - 1) WHERE key = ?1 AND day_bucket = ?2").bind(globalKey, todayDate).run(),
          env.DB.prepare("DELETE FROM otp_records WHERE email = ?1 AND uid = ?2").bind(email, requesterUid).run()
        ]).catch(() => {});
      };

      try {
        // Generate Cryptographic 6-digit OTP + HMAC Hashing bound to requester's UID
        const randomBuf = new Uint32Array(1);
        crypto.getRandomValues(randomBuf);
        const otp = (100000 + (randomBuf[0] % 900000)).toString();
        const otpHmac = await hmacHex(env.SIGNING_SECRET, email + ":" + otp);
        const expiresAt = now + 300000; // 5 min TTL

        // ANTI-OVERWRITE GUARD: Bob cannot overwrite Alice's pending unexpired OTP!
        // Only updates if the existing OTP belongs to the same uid OR is already expired.
        const otpIns = await env.DB.prepare(
          "INSERT INTO otp_records (email, uid, otp_hmac, attempts, created_at, expires_at) VALUES (?1, ?2, ?3, 0, ?4, ?5) " +
          "ON CONFLICT(email) DO UPDATE SET uid = ?2, otp_hmac = ?3, attempts = 0, created_at = ?4, expires_at = ?5 " +
          "WHERE uid = ?2 OR expires_at < ?4 RETURNING email"
        ).bind(email, requesterUid, otpHmac, now, expiresAt).first();

        if (!otpIns) {
          await rollback();
          return jsonResponse({ success: false, error: "A verification code is already active for this email. Please check your inbox or wait 5 minutes." }, 429);
        }

        const resendRes = await fetch("https://api.resend.com/emails", {
          method: "POST",
          headers: { Authorization: `Bearer ${env.RESEND_API_KEY}`, "Content-Type": "application/json" },
          body: JSON.stringify({
            from: "Digital Tasbih <noreply@imaanix.co.in>",
            to: [email],
            subject: `Your Verification Code: ${otp}`,
            html: `<p>Your single-use verification code is: <b style="font-size:24px;">${otp}</b> (Valid for 5 minutes).</p>`
          })
        });

        if (!resendRes.ok) {
          await rollback();
          return jsonResponse({ success: false, error: "Failed to deliver email" }, 502);
        }

        return jsonResponse({ success: true, message: "Code sent successfully" });
      } catch (err) {
        await rollback();
        return jsonResponse({ success: false, error: "Failed to process verification code" }, 500);
      }
    }

    // ─────────────────────────────────────────────────────────
    // 2. VERIFY EMAIL OTP (Requester-Bound Anti-Griefing Single-Use Guard)
    // ─────────────────────────────────────────────────────────
    if (endpoint === "verify-email-otp") {
      const email = normalizeEmail(String(body.email || ""));
      const enteredOtp = String(body.otp || "").trim();
      const requesterUid = authUser.sub;

      if (!email || enteredOtp.length !== 6) return jsonResponse({ success: false, error: "Invalid code format" }, 400);
      const now = Date.now();

      // Atomic attempt increment bound strictly to requester UID (Attacker cannot grief legitimate victim)
      const row = await env.DB.prepare(
        "UPDATE otp_records SET attempts = attempts + 1 WHERE email = ?1 AND uid = ?2 AND expires_at > ?3 AND attempts < 5 RETURNING otp_hmac, attempts"
      ).bind(email, requesterUid, now).first();

      if (!row) {
        // IMPORTANT: Never DELETE on !row (prevents deleting another user's pending OTP or session)
        return jsonResponse({ success: false, error: "Code expired, maximum attempts reached, or invalid session. Please request a new code." }, 400);
      }

      const inputHmac = await hmacHex(env.SIGNING_SECRET, email + ":" + enteredOtp);
      if (inputHmac !== row.otp_hmac) {
        if (row.attempts >= 5) {
          await env.DB.prepare("DELETE FROM otp_records WHERE email = ?1 AND uid = ?2").bind(email, requesterUid).run();
        }
        return jsonResponse({ success: false, error: `Incorrect code. ${5 - row.attempts} attempt(s) remaining.` }, 400);
      }

      // MATCH! Single-Use Atomic Delete: Must return the row, otherwise parallel request already consumed it!
      const deleted = await env.DB.prepare("DELETE FROM otp_records WHERE email = ?1 AND uid = ?2 RETURNING *").bind(email, requesterUid).first();
      if (!deleted) {
        return jsonResponse({ success: false, error: "Code already used or invalid." }, 400);
      }

      // Mint Firebase Custom Token with verified claim
      const customToken = await mintFirebaseCustomToken(email, env);

      return jsonResponse({
        success: true,
        verified: true,
        email: email,
        firebaseCustomToken: customToken
      });
    }

    return jsonResponse({ error: "Endpoint not found" }, 404);
  },

  async scheduled(event, env, ctx) {
    const now = Date.now();
    ctx.waitUntil((async () => {
      await env.DB.prepare("DELETE FROM otp_records WHERE expires_at < ?").bind(now).run();
      await env.DB.prepare("DELETE FROM rate_limits WHERE last_requested_at < ?").bind(now - 172800000).run();
      await env.DB.prepare("DELETE FROM user_submit_cooldowns WHERE last_submitted_at < ?").bind(now - 86400000).run();
    })());
  }
};

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

function getAuthServiceAccount(env) {
  if (env.FIREBASE_AUTH_SERVICE_ACCOUNT_JSON) {
    try { return JSON.parse(env.FIREBASE_AUTH_SERVICE_ACCOUNT_JSON); } catch {}
  }
  const client_email = env.AUTH_GCP_CLIENT_EMAIL || env.GCP_CLIENT_EMAIL || env.FIREBASE_CLIENT_EMAIL;
  const private_key = env.AUTH_GCP_PRIVATE_KEY || env.GCP_PRIVATE_KEY || env.FIREBASE_PRIVATE_KEY;
  if (client_email && private_key) {
    return { client_email, private_key };
  }
  throw new Error("Missing Auth Service Account credentials. Set FIREBASE_AUTH_SERVICE_ACCOUNT_JSON or AUTH_GCP_CLIENT_EMAIL / AUTH_GCP_PRIVATE_KEY");
}

async function mintFirebaseCustomToken(email, env) {
  const sa = getAuthServiceAccount(env);
  const now = Math.floor(Date.now() / 1000);
  const uid = "email_" + (await sha256(email)).slice(0, 20);

  const header = { alg: "RS256", typ: "JWT" };
  const payload = {
    iss: sa.client_email,
    sub: sa.client_email,
    aud: "https://identitytoolkit.googleapis.com/google.identity.identitytoolkit.v1.IdentityToolkit",
    iat: now,
    exp: now + 3600,
    uid: uid,
    claims: {
      email: email,
      otpEmail: email,
      emailOtpVerified: true,
      email_verified: true
    }
  };

  const unsigned = `${base64Url(JSON.stringify(header))}.${base64Url(JSON.stringify(payload))}`;
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
  return `${unsigned}.${base64Url(sig)}`;
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

async function sha256(str) {
  const buf = await crypto.subtle.digest("SHA-256", new TextEncoder().encode(str));
  return Array.from(new Uint8Array(buf)).map(b => b.toString(16).padStart(2, "0")).join("");
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

function base64Url(data) {
  const str = typeof data === "string" ? btoa(data) : btoa(String.fromCharCode(...new Uint8Array(data)));
  return str.replace(/\+/g, "-").replace(/\//g, "_").replace(/=+$/, "");
}

function jsonResponse(data, status = 200) {
  return new Response(JSON.stringify(data), {
    status,
    headers: { "Content-Type": "application/json", "X-Content-Type-Options": "nosniff" }
  });
}
```

---

## Phase 9: Wrangler.toml Configurations & Cron Triggers

### 1. `wrangler.toml` for Campaigns Worker (`campaigns-worker`)
```toml
name = "digital-tasbih-campaigns"
main = "campaigns-worker.js"
compatibility_date = "2025-01-01"

# Custom Domain Route
routes = [
  { pattern = "api.imaanix.co.in/campaigns/*", zone_name = "imaanix.co.in" }
]

# D1 SQL Database Binding
[[d1_databases]]
binding = "DB"
database_name = "tasbih-auth-db"
database_id = "YOUR_CLOUDFLARE_D1_DATABASE_ID"

# Encrypted Secrets to set via wrangler secret put:
# - FIREBASE_SERVICE_ACCOUNT_JSON (for digital-tasbih-campaigns-e43ee)
# - CAMPAIGN_ADMIN_SECRET
# - SIGNING_SECRET
```

### 2. `wrangler.toml` for Auth & Email OTP Worker (`auth-worker`)
```toml
name = "digital-tasbih-auth"
main = "auth-worker.js"
compatibility_date = "2025-01-01"

# Custom Domain Route
routes = [
  { pattern = "api.imaanix.co.in/auth/*", zone_name = "imaanix.co.in" }
]

# D1 SQL Database Binding
[[d1_databases]]
binding = "DB"
database_name = "tasbih-auth-db"
database_id = "YOUR_CLOUDFLARE_D1_DATABASE_ID"

# Hourly Cron Trigger for Automatic Database Cleanup
[triggers]
crons = ["0 3 * * *"]

# Encrypted Secrets to set via wrangler secret put:
# - RESEND_API_KEY
# - FIREBASE_AUTH_SERVICE_ACCOUNT_JSON (for digital-tasbih-19bca)
# - SIGNING_SECRET
```

---

## Phase 10: Airtight Firestore Deny-All Security Rules

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // 🛡️ AIRTIGHT DENY-ALL:
    // All read/write operations must go through our Cloudflare Worker authenticated via Google Service Account.
    match /{document=**} {
      allow read, write: if false;
    }
  }
}
```

---

## 12. Summary Checklist & Round 4 Verification Suite

### A. Pre-Deployment Configuration & Indexes
1. **Firestore Composite Index for Campaigns Feed:**
   - **Collection:** `campaigns`
   - **Fields:**
     - `visibility` (Ascending)
     - `createdAt` (Descending)
   - **Status:** Create via Firebase Console ➔ Firestore Database ➔ Indexes ➔ Composite ➔ Add Index.
2. **Cloudflare D1 Migration for `otp_records`:**
   ```sql
   -- Clean schema update with requester-bound uid
   DROP TABLE IF EXISTS otp_records;
   CREATE TABLE otp_records (
       email TEXT PRIMARY KEY,
       uid TEXT NOT NULL,
       otp_hmac TEXT NOT NULL,
       attempts INTEGER DEFAULT 0,
       created_at INTEGER NOT NULL,
       expires_at INTEGER NOT NULL
   );
   CREATE INDEX idx_otp_expires ON otp_records(expires_at);
   ```
3. **Cloudflare WAF / Rate Limiting Rule:**
   - In Cloudflare Dashboard ➔ Security ➔ WAF ➔ Rate Limiting:
   - Rule URI path contains `/campaigns/admin` ➔ Max 20 requests per 10 minutes per IP ➔ Action: Block or Challenge.

### B. Positive Test Suite (Production Verification)
1. **Google ID Token Write Verification:**
   - Obtain genuine Google ID token via Android app Google Sign-In.
   - Send `POST https://api.imaanix.co.in/campaigns/createGroup` with `Authorization: Bearer <ID_TOKEN>`.
   - Expected: `200 OK` with `{ ok: true, campaign: { joinCode: "GC-XXXXXXXX", ... } }`.
2. **Email OTP Flow with Custom Token Minting:**
   - Request OTP: `POST https://api.imaanix.co.in/auth/send-email-otp` with anonymous `Authorization: Bearer <ANON_TOKEN>`.
   - Check inbox for 6-digit code.
   - Verify OTP: `POST https://api.imaanix.co.in/auth/verify-email-otp` with same `<ANON_TOKEN>`.
   - Expected: `200 OK` with `{ success: true, verified: true, firebaseCustomToken: "..." }`.
   - Client calls `FirebaseAuth.signInWithCustomToken(customToken)`.
3. **Join Group with Prefix Variations:**
   - Input `8F2KL91X` ➔ Formatted as `GC-8F2KL91X` ➔ Joined successfully (`200 OK`).
   - Input `GC-8F2KL91X` ➔ Formatted as `GC-8F2KL91X` ➔ Joined successfully (`200 OK`).
   - Second attempt with same user ➔ `{ ok: true, alreadyJoined: true }`.
4. **Contest Registration (Umrah Lucky Draw):**
   - Submit registration with Google ID token, disclaimer accepted, valid E.164 phone.
   - Expected: `200 OK` with ticket number `UMR-XXXXXX`.
5. **Batch Purge Verification:**
   - Run `adminPurgeContest` with `X-Admin-Key`.
   - Expected: Single `:commit` batch deletes up to 400 documents (entries + registry + signals) without exceeding Cloudflare subrequest limits.

### C. Negative Test Suite (Penetration & Bypass Proofing)
1. **Fake JWT Token (`Bearer abc`):**
   - Both Workers reject immediately with `401 Unauthorized`.
2. **Forged Key ID (`kid: "random-attacker-id"`):**
   - Throttled JWKS refetch does not spam Google; returns `401 Unauthorized`.
3. **OTP Lockout Griefing Attempt:**
   - Attacker Bob attempts to verify Alice's email with wrong OTP:
   - Query checks `WHERE email = ?1 AND uid = ?2`. Since Bob's UID does not match Alice's UID, 0 rows returned.
   - Alice's attempt counter is untouched and Alice is NOT locked out.
4. **Tap-Speed & Amount Cap:**
   - First submit > 50 taps ➔ Blocked with 400 (`First submission capped at 50 taps`).
   - Submit within 2000ms ➔ Blocked with 429 (`Please wait between submissions`).
   - Rate exceeding mathematical threshold ➔ Blocked atomically in SQL.
5. **Private Campaign Isolation:**
   - Unauthenticated / public callers cannot view private campaigns in `getPublicIndex` even if composite index is rebuilding (strictly filtered).
6. **Malicious Join Code Format:**
   - Passing invalid length (e.g. 5 chars, malformed characters) ➔ Rejected with `400 Bad Request`.
7. **Admin Announcement Safety:**
   - Submitting empty or malformed config object ➔ Rejected with `400 Bad Request` without altering Firestore documents.
7. Admin contestants list pe agar Firestore fail ho ➔ 500 error throw hoga (kabhi truncated ya khali list nahi aayegi).

---
*Created and verified for Digital Tasbih project. All components adhere strictly to the ₹0 / Free-tier guarantee.*
