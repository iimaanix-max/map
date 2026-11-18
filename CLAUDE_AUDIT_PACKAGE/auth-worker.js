/**
 * Digital Tasbih - Hardened Auth & Email OTP Worker
 * Revision: 7.1 Multi-Provider Auto-Failover (Resend + Brevo Standby)
 * Capacity: 400 Free OTP Emails/Day (Resend 100 + Brevo 300)
 * Route: api.imaanix.in/auth/*
 */

const AUTH_PROJECT_ID = "digital-tasbih-19bca";
const JWKS_URL = "https://www.googleapis.com/service_accounts/v1/jwk/securetoken@system.gserviceaccount.com";
const EMAIL_REGEX = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;

let CACHED_JWKS = null;
let LAST_JWKS_FETCH = 0;


// ─────────────────────────────────────────────────────────────
// EMERGENCY THRESHOLD ALERT DISPATCHER (ZERO-WASTE, HIGH-PRIORITY)
// ─────────────────────────────────────────────────────────────
async function sendEmergencyAlertEmail(env, {
  level, // 'URGENT' or 'CRITICAL'
  title,
  workerName,
  accountName,
  endpoint,
  metricType,
  usedCount,
  totalLimit,
  remainingSlots,
  actionAdvice
}) {
  try {
    if (!env.BREVO_API_KEY) return;
    const adminEmail = "iimaanix@gmail.com";
    const percentUsed = ((usedCount / totalLimit) * 100).toFixed(1);
    const isCritical = level === "CRITICAL";

    const headerGradient = isCritical
      ? "linear-gradient(135deg, #7f1d1d 0%, #b91c1c 50%, #991b1b 100%)"
      : "linear-gradient(135deg, #78350f 0%, #d97706 50%, #b45309 100%)";

    const badgeColor = isCritical ? "#fecaca" : "#fef3c7";
    const badgeText = isCritical ? "#991b1b" : "#92400e";
    const progressColor = isCritical ? "#dc2626" : "#d97706";

    const subject = isCritical
      ? `CRITICAL 🚨 [${workerName}] Limit Reached: ${usedCount.toLocaleString()}/${totalLimit.toLocaleString()} Used! Only ${remainingSlots.toLocaleString()} Left!`
      : `URGENT 🚨 [${workerName}] Limit Approaching: ${usedCount.toLocaleString()}/${totalLimit.toLocaleString()} Used (${percentUsed}%)`;

    const alertHtml = `<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>${level} Capacity Alert</title>
</head>
<body style="margin:0;padding:24px 12px;background:#0f172a;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,sans-serif;color:#1e293b;">
  <div style="max-width:620px;margin:0 auto;background:#ffffff;border-radius:20px;overflow:hidden;box-shadow:0 12px 32px rgba(0,0,0,0.3);border:2px solid ${isCritical ? '#ef4444' : '#f59e0b'};">
    
    <!-- Header -->
    <div style="background:${headerGradient};padding:28px 24px;text-align:center;">
      <span style="background:${badgeColor};color:${badgeText};font-size:12px;font-weight:900;letter-spacing:1.5px;padding:6px 16px;border-radius:20px;text-transform:uppercase;">
        ${level} CAPACITY NOTIFICATION
      </span>
      <h1 style="margin:14px 0 6px 0;font-size:24px;color:#ffffff;font-weight:900;letter-spacing:0.5px;">
        ${isCritical ? '🚨 CRITICAL THRESHOLD ALERT' : '⚠️ URGENT CAPACITY WARNING'}
      </h1>
      <p style="margin:0;font-size:14px;color:#ffffff;opacity:0.9;font-weight:600;">
        ${title}
      </p>
    </div>

    <!-- Alert Breakdown Body -->
    <div style="padding:24px;">

      <!-- Primary Impact Box -->
      <div style="background:${isCritical ? '#fef2f2' : '#fffbeb'};border:2px solid ${isCritical ? '#f87171' : '#fbbf24'};border-radius:14px;padding:18px;margin-bottom:20px;">
        <div style="font-size:12px;font-weight:900;color:${isCritical ? '#991b1b' : '#92400e'};text-transform:uppercase;letter-spacing:1px;margin-bottom:10px;">
          📌 EXACT WORKER IDENTITY &amp; CLOUDFLARE ACCOUNT DETAILS:
        </div>
        
        <table width="100%" cellspacing="0" cellpadding="6" style="font-size:13px;line-height:1.7;background:#ffffff;border-radius:10px;padding:10px;border:1px solid #e2e8f0;">
          <tr>
            <td width="42%" style="color:#475569;font-weight:700;">🛠️ Worker Name:</td>
            <td style="color:#0f172a;font-weight:900;font-size:14px;">${workerName}</td>
          </tr>
          <tr>
            <td style="color:#475569;font-weight:700;">📧 Cloudflare Account Email:</td>
            <td><span style="background:#dbeafe;color:#1e40af;font-weight:900;font-family:monospace;font-size:13px;padding:4px 10px;border-radius:6px;border:1px solid #bfdbfe;display:inline-block;">${accountName}</span></td>
          </tr>
          <tr>
            <td style="color:#475569;font-weight:700;">🌐 Live Worker Endpoint:</td>
            <td style="color:#334155;font-weight:700;font-family:monospace;font-size:12px;">${endpoint}</td>
          </tr>
          <tr>
            <td style="color:#475569;font-weight:700;">📊 Monitored Quota Type:</td>
            <td style="color:#0f172a;font-weight:800;">${metricType}</td>
          </tr>
        </table>
      </div>
      <!-- Quota Meter -->
      <div style="background:#f8fafc;border:1px solid #e2e8f0;border-radius:14px;padding:18px;margin-bottom:20px;">
        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:8px;">
          <span style="font-size:13px;font-weight:800;color:#0f172a;">Quota Consumption:</span>
          <span style="font-size:16px;font-weight:900;color:${progressColor};">${usedCount.toLocaleString()} / ${totalLimit.toLocaleString()} (${percentUsed}%)</span>
        </div>
        <div style="background:#e2e8f0;height:12px;border-radius:10px;overflow:hidden;width:100%;">
          <div style="background:${progressColor};width:${Math.min(100, percentUsed)}%;height:12px;border-radius:10px;"></div>
        </div>
        <div style="display:flex;justify-content:space-between;margin-top:8px;font-size:12px;font-weight:700;">
          <span style="color:#059669;">Safe Remaining Buffer:</span>
          <span style="color:${isCritical ? '#dc2626' : '#d97706'};">${remainingSlots.toLocaleString()} Slots Left</span>
        </div>
      </div>

      <!-- Action Required Box -->
      <div style="background:#ffffff;border:2px dashed ${isCritical ? '#dc2626' : '#d97706'};border-radius:14px;padding:20px;margin-bottom:20px;">
        <div style="font-size:15px;font-weight:900;color:#0f172a;margin-bottom:8px;">
          ⚡ AAPKO KYA KARNA HAI (EXACT STEPS TO UPGRADE &amp; PREVENT DOWNTIME):
        </div>
        <p style="margin:0 0 14px 0;font-size:13px;color:#334155;line-height:1.6;">
          ${actionAdvice}
        </p>

        <div style="background:#f8fafc;border:2px solid #0284c7;border-radius:12px;padding:16px;">
          <div style="font-size:13px;font-weight:900;color:#0369a1;text-transform:uppercase;margin-bottom:10px;letter-spacing:0.5px;">
            💳 STEPS TO UPGRADE CLOUDFLARE ACCOUNT (ZERO DOWNTIME):
          </div>
          <ol style="margin:0;padding-left:18px;font-size:13px;color:#1e293b;line-height:1.8;">
            <li>Apne browser me <a href="https://dash.cloudflare.com/" style="color:#0284c7;font-weight:800;text-decoration:underline;" target="_blank">dash.cloudflare.com</a> open karein.</li>
            <li>Make sure karein ki aap isi email se login hain: <b style="background:#e0f2fe;color:#0369a1;padding:2px 8px;border-radius:4px;font-family:monospace;">${accountName}</b></li>
            <li>Left sidebar menu me <b>Compute ➔ Workers &amp; Pages</b> par click karein.</li>
            <li>Top navigation bar me <b>Plans</b> tab par click karein.</li>
            <li><b>Upgrade to Workers Paid ($5/month)</b> select karein aur payment method (Credit/Debit Card) attach karein.</li>
            <li><b style="color:#166534;">🎉 DONE!</b> Aapke account ki daily request limit turant 1,00,000 se badhkar <b>1,00,00,000 (1 CRORE Requests/month)</b> ho jayegi! Zero user downtime!</li>
          </ol>
          
          <div style="margin-top:14px;background:#ffffff;border:1px solid #cbd5e1;border-radius:8px;padding:10px 14px;font-size:12px;color:#475569;line-height:1.6;">
            ⏰ <b>Alternative Option (100% Free / No Card):</b> Agar aap upgrade nahi karna chahte, toh daily quota raat <b>05:30 AM IST (Midnight UTC 00:00)</b> par automatically 0 se reset ho jata hai.
          </div>
        </div>
      </div>
    </div>

    <!-- Footer -->
    <div style="background:#f8fafc;border-top:1px solid #e2e8f0;padding:16px 24px;text-align:center;font-size:11px;color:#64748b;">
      Digital Tasbih Automated Sentry Sentinel • Alert Dispatched to <b>${adminEmail}</b>
    </div>

  </div>
</body>
</html>`;

    await fetch("https://api.brevo.com/v3/smtp/email", {
      method: "POST",
      headers: {
        "api-key": env.BREVO_API_KEY,
        "Content-Type": "application/json",
        "Accept": "application/json"
      },
      body: JSON.stringify({
        sender: { name: "Digital Tasbih Sentinel", email: "alerts@imaanix.in" },
        to: [{ email: adminEmail, name: "Admin Abdul Quadir" }],
        subject: subject,
        htmlContent: alertHtml
      })
    });
    console.log(`[AlertDispatched] ${level} alert sent for ${workerName}`);
  } catch (err) {
    console.error("[AlertError] Failed to send emergency alert:", err);
  }
}


export default {
  async fetch(request, env) {
    const url = new URL(request.url);
    const endpoint = url.pathname.replace(/^\/auth\/?/, "").replace(/^\//, "");

    // Health check for root /auth (Allows GET from browsers)
    if (!endpoint || endpoint === "") {
      return jsonResponse({ success: true, service: "Digital Tasbih Auth API", status: "online" });
    }

    // Admin on-demand trigger for Daily Growth Report
    if (endpoint === "send-daily-report") {
      if (request.headers.get("X-Admin-Secret") !== env.SIGNING_SECRET) {
        return jsonResponse({ success: false, error: "Unauthorized" }, 401);
      }
      const rep = await generateAndSendDailyReport(env);
      return jsonResponse(rep);
    }

    // Admin Test Trigger: Test URGENT Alert Email
    if (endpoint === "test-urgent-alert") {
      if (request.headers.get("X-Admin-Secret") !== env.SIGNING_SECRET) {
        return jsonResponse({ success: false, error: "Unauthorized" }, 401);
      }
      await sendEmergencyAlertEmail(env, {
        level: "URGENT",
        title: "Test Alert: Cloudflare Requests Approaching Threshold",
        workerName: "User Login & Email OTP Engine (digital-tasbih-auth)",
        accountName: "iimaanix@gmail.com",
        endpoint: "https://api.imaanix.in/auth",
        metricType: "Cloudflare Edge Invocations",
        usedCount: 90000,
        totalLimit: 100000,
        remainingSlots: 10000,
        actionAdvice: "Aapke account me 90,000 requests use ho chuki hain (90%). Agle 3-4 ghante ka safe buffer baaki hai. Agar achanak zyada users aa rahe hain toh Cloudflare Workers Paid plan me upgrade karne ki taiyari rakhein."
      });
      return jsonResponse({ success: true, message: "URGENT 🚨 test email dispatched to iimaanix@gmail.com" });
    }

    // Admin Test Trigger: Test CRITICAL Alert Email
    if (endpoint === "test-critical-alert") {
      if (request.headers.get("X-Admin-Secret") !== env.SIGNING_SECRET) {
        return jsonResponse({ success: false, error: "Unauthorized" }, 401);
      }
      await sendEmergencyAlertEmail(env, {
        level: "CRITICAL",
        title: "CRITICAL EMERGENCY: 98.5% Cloudflare Quota Exhausted!",
        workerName: "User Login & Email OTP Engine (digital-tasbih-auth)",
        accountName: "iimaanix@gmail.com",
        endpoint: "https://api.imaanix.in/auth",
        metricType: "Cloudflare Edge Invocations",
        usedCount: 98500,
        totalLimit: 100000,
        remainingSlots: 1500,
        actionAdvice: "🚨 EMERGENCY: Sirf 1,500 requests baaki hain! 1,00,000 hit hote hi Cloudflare error dega. Turant niche diye steps se Cloudflare Workers Paid ($5/mo) me upgrade karein taaki user app chalta rahe!"
      });
      return jsonResponse({ success: true, message: "CRITICAL 🚨 test email dispatched to iimaanix@gmail.com" });
    }

    // Central Hook for other workers (quran-proxy, hadith-proxy, digitaltasbihimagesonly)
    if (endpoint === "report-worker-metric") {
      try {
        const payload = await request.json().catch(() => ({}));
        const wName = payload.worker || "Unknown Worker";
        const wAccount = payload.account || "abdulquadirqureshi";
        const wEndpoint = payload.endpoint || "workers.dev";
        const wCount = Number(payload.count || 0);
        const wLimit = Number(payload.limit || 100000);

        if (wCount >= 90000 && wCount < 98500) {
          await sendEmergencyAlertEmail(env, {
            level: "URGENT",
            title: `Cloudflare Requests Approaching Threshold on ${wName}`,
            workerName: wName,
            accountName: wAccount,
            endpoint: wEndpoint,
            metricType: "Cloudflare Edge Invocations",
            usedCount: wCount,
            totalLimit: wLimit,
            remainingSlots: Math.max(0, wLimit - wCount),
            actionAdvice: `Aapke worker [${wName}] par ${wCount.toLocaleString()} requests use ho chuki hain. Account: ${wAccount}. Buffer baaki hai.`
          });
        } else if (wCount >= 98500) {
          await sendEmergencyAlertEmail(env, {
            level: "CRITICAL",
            title: `CRITICAL EMERGENCY: 98.5% Limit Exhausted on ${wName}!`,
            workerName: wName,
            accountName: wAccount,
            endpoint: wEndpoint,
            metricType: "Cloudflare Edge Invocations",
            usedCount: wCount,
            totalLimit: wLimit,
            remainingSlots: Math.max(0, wLimit - wCount),
            actionAdvice: `🚨 EMERGENCY on [${wName}]: Sirf ${Math.max(0, wLimit - wCount).toLocaleString()} requests bache hain! Account: ${wAccount}. Turant Cloudflare me login karke $5/mo upgrade karein.`
          });
        }
        return jsonResponse({ success: true });
      } catch (_) {
        return jsonResponse({ success: false }, 400);
      }
    }


    if (request.method !== "POST") return jsonResponse({ success: false, error: "Method not allowed" }, 405);
    if (!env.DB) return jsonResponse({ success: false, error: "Database service unavailable" }, 500);

    // Asynchronous non-blocking analytics metric logging (Zero latency impact)
    try {
      const istDate = new Date(Date.now() + 19800000); // UTC + 5:30
      const dayBucket = istDate.toISOString().slice(0, 10);
      const istHour = istDate.getUTCHours().toString().padStart(2, "0");
      const country = request.cf?.country || "IN";
      const region = request.cf?.region || "Unknown";
      const nowTs = Date.now();

      env.DB.prepare("INSERT INTO analytics_metrics (metric_key, category, item_name, day_bucket, count, last_updated_at) VALUES (?1, 'worker', 'auth', ?2, 1, ?3) ON CONFLICT(metric_key) DO UPDATE SET count = count + 1, last_updated_at = ?3").bind(`worker:auth:${dayBucket}`, dayBucket, nowTs).run().catch(() => {});
      env.DB.prepare("INSERT INTO analytics_metrics (metric_key, category, item_name, day_bucket, count, last_updated_at) VALUES (?1, 'hourly', ?2, ?3, 1, ?4) ON CONFLICT(metric_key) DO UPDATE SET count = count + 1, last_updated_at = ?4").bind(`hourly:${istHour}:${dayBucket}`, istHour, dayBucket, nowTs).run().catch(() => {});
      env.DB.prepare("INSERT INTO analytics_metrics (metric_key, category, item_name, day_bucket, count, last_updated_at) VALUES (?1, 'geo_country', ?2, ?3, 1, ?4) ON CONFLICT(metric_key) DO UPDATE SET count = count + 1, last_updated_at = ?4").bind(`geo_country:${country}:${dayBucket}`, country, dayBucket, nowTs).run().catch(() => {});
      env.DB.prepare("INSERT INTO analytics_metrics (metric_key, category, item_name, day_bucket, count, last_updated_at) VALUES (?1, 'geo_region', ?2, ?3, 1, ?4) ON CONFLICT(metric_key) DO UPDATE SET count = count + 1, last_updated_at = ?4").bind(`geo_region:${region}:${dayBucket}`, region, dayBucket, nowTs).run().catch(() => {});
    } catch (_) {}

    // ─────────────────────────────────────────────────────────
    // 0. App-Level Secret Key Guard (Blocks raw scrapers, curl, Postman bots)
    // ─────────────────────────────────────────────────────────
    const APP_SECRET_KEY = "DT_LIVE_SECURE_KEY_2026";
    const clientAppKey = request.headers.get("X-App-Key");
    const hasAuthToken = request.headers.has("Authorization");
    if (clientAppKey !== APP_SECRET_KEY && !hasAuthToken) {
      return jsonResponse({ success: false, error: "Unauthorized: Missing App Signature" }, 403);
    }

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

      // 3. Global Daily Safety Cap (Expanded to 350 OTPs/day with Brevo Standby)
      const maxDailyCap = env.BREVO_API_KEY ? 400 : 80;
      const gRes = await env.DB.prepare(
        "INSERT INTO rate_limits (key, last_requested_at, request_count, day_bucket) VALUES (?1, ?2, 1, ?3) " +
        "ON CONFLICT(key) DO UPDATE SET request_count = request_count + 1, last_requested_at = ?2 " +
        "WHERE request_count < ?4 RETURNING request_count"
      ).bind(globalKey, now, todayDate, maxDailyCap).first();

      if (!gRes) {
        return jsonResponse({ success: false, error: "Daily email verification limit reached. Please sign in with Google." }, 429);
      }

      const currentOtpCount = Number(gRes.request_count || 0);
      if (currentOtpCount === 350) {
        sendEmergencyAlertEmail(env, {
          level: "URGENT",
          title: "Daily Email OTP High Demand (87.5% Used)",
          workerName: "User Login & Email OTP Engine (digital-tasbih-auth)",
          accountName: "iimaanix@gmail.com",
          endpoint: "https://api.imaanix.in/auth/send-email-otp",
          metricType: "Email OTP Demand",
          usedCount: 350,
          totalLimit: 400,
          remainingSlots: 50,
          actionAdvice: "Aaj 350 OTPs use ho chuke hain (87.5%). Sirf 50 free OTPs baaki hain. Agar demand aisi hi rahi toh Zoho ZeptoMail enable karein ya Google Sign-In recommend karein."
        }).catch(() => {});
      } else if (currentOtpCount === 390) {
        sendEmergencyAlertEmail(env, {
          level: "CRITICAL",
          title: "CRITICAL EMERGENCY: 390/400 OTPs Exhausted! Only 10 Slots Left!",
          workerName: "User Login & Email OTP Engine (digital-tasbih-auth)",
          accountName: "iimaanix@gmail.com",
          endpoint: "https://api.imaanix.in/auth/send-email-otp",
          metricType: "Email OTP Demand",
          usedCount: 390,
          totalLimit: 400,
          remainingSlots: 10,
          actionAdvice: "🚨 EMERGENCY: 390 OTPs use ho chuke hain! Sirf 10 slots bache hain! Limit 400 hit hote hi email OTP band ho jayega. Turant Zoho ZeptoMail connect karein!"
        }).catch(() => {});
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

        const emailHtml = buildEmailHtml(otp);
        let emailSent = false;
        let lastError = "Failed to deliver email";

        // ═════════════════════════════════════════════════════════
        // TIER 1: Primary Dispatch via Resend.com (100/day free)
        // ═════════════════════════════════════════════════════════
        if (env.RESEND_API_KEY) {
          try {
            const resendRes = await fetch("https://api.resend.com/emails", {
              method: "POST",
              headers: { Authorization: `Bearer ${env.RESEND_API_KEY}`, "Content-Type": "application/json" },
              body: JSON.stringify({
                from: "Digital Tasbih <noreply@imaanix.in>",
                to: [email],
                subject: `Your Verification Code: ${otp}`,
                html: emailHtml
              })
            });

            if (resendRes.ok) {
              emailSent = true;
              env.DB.prepare(
                "INSERT INTO rate_limits (key, last_requested_at, request_count, day_bucket) VALUES (?1, ?2, 1, ?3) " +
                "ON CONFLICT(key) DO UPDATE SET request_count = request_count + 1, last_requested_at = ?2"
              ).bind(`resend:${todayDate}`, now, todayDate).run().catch(() => {});
            } else {
              const errBody = await resendRes.text().catch(() => "");
              console.warn(`[OTP-Worker] Resend dispatch failed (HTTP ${resendRes.status}): ${errBody}`);
              lastError = `Resend error (${resendRes.status})`;
            }
          } catch (e) {
            console.warn(`[OTP-Worker] Resend fetch exception: ${e.message}`);
          }
        }

        // ═════════════════════════════════════════════════════════
        // TIER 2: Automatic Standby Failover via Brevo (Sendinblue)
        // Kicks in automatically if Resend returns 429 (quota), 402, or errors
        // ═════════════════════════════════════════════════════════
        if (!emailSent && env.BREVO_API_KEY) {
          try {
            const brevoRes = await fetch("https://api.brevo.com/v3/smtp/email", {
              method: "POST",
              headers: {
                "api-key": env.BREVO_API_KEY,
                "Content-Type": "application/json",
                "Accept": "application/json"
              },
              body: JSON.stringify({
                sender: { name: "Digital Tasbih", email: "noreply@imaanix.in" },
                to: [{ email: email }],
                subject: `Your Verification Code: ${otp}`,
                htmlContent: emailHtml
              })
            });

            if (brevoRes.ok) {
              emailSent = true;
              console.log(`[OTP-Worker] Automatic Brevo failover SUCCEEDED for ${email}`);
              env.DB.prepare(
                "INSERT INTO rate_limits (key, last_requested_at, request_count, day_bucket) VALUES (?1, ?2, 1, ?3) " +
                "ON CONFLICT(key) DO UPDATE SET request_count = request_count + 1, last_requested_at = ?2"
              ).bind(`brevo:${todayDate}`, now, todayDate).run().catch(() => {});
            } else {
              const bErr = await brevoRes.text().catch(() => "");
              console.error(`[OTP-Worker] Brevo failover failed (HTTP ${brevoRes.status}): ${bErr}`);
              lastError = `Brevo error (${brevoRes.status})`;
            }
          } catch (e) {
            console.error(`[OTP-Worker] Brevo fetch exception: ${e.message}`);
          }
        }

        if (!emailSent) {
          await rollback();
          return jsonResponse({ success: false, error: lastError }, 502);
        }

        // Asynchronously track hourly demand (non-blocking)
        env.DB.prepare(
          "INSERT INTO rate_limits (key, last_requested_at, request_count, day_bucket) VALUES (?1, ?2, 1, ?3) " +
          "ON CONFLICT(key) DO UPDATE SET request_count = request_count + 1, last_requested_at = ?2"
        ).bind(`hourly:${currentHour}`, now, todayDate).run().catch(() => {});

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
      const customToken = await mintFirebaseCustomToken(email, requesterUid, env);

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
      // 1. Send Daily Visual Growth Report to Admin
      await generateAndSendDailyReport(env).catch(e => console.error("[Report-Cron] Error:", e));

      // 2. Automated Table Hygiene (30-day retention for rate_limits historical chart)
      await env.DB.prepare("DELETE FROM otp_records WHERE expires_at < ?").bind(now).run().catch(() => {});
      await env.DB.prepare("DELETE FROM rate_limits WHERE last_requested_at < ?").bind(now - (30 * 86400000)).run().catch(() => {});
      await env.DB.prepare("DELETE FROM user_submit_cooldowns WHERE last_submitted_at < ?").bind(now - 86400000).run().catch(() => {});
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

async function mintFirebaseCustomToken(email, targetUid, env) {
  const sa = getAuthServiceAccount(env);
  const now = Math.floor(Date.now() / 1000);
  const uid = targetUid || ("email_" + (await sha256(email)).slice(0, 20));

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
function buildEmailHtml(otp) {
  return `<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Verification Code</title>
</head>
<body style="margin:0;padding:0;background-color:#f4f6f8;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,Helvetica,Arial,sans-serif;color:#1e293b;">
  <table role="presentation" width="100%" cellspacing="0" cellpadding="0" style="background-color:#f4f6f8;padding:32px 16px;">
    <tr>
      <td align="center">
        <table role="presentation" width="100%" style="max-width:480px;background:#ffffff;border-radius:16px;box-shadow:0 4px 12px rgba(0,0,0,0.06);border:1px solid #e2e8f0;overflow:hidden;" cellspacing="0" cellpadding="0">
          <!-- Header Banner -->
          <tr>
            <td style="background:linear-gradient(135deg, #064e3b 0%, #047857 100%);padding:24px 28px;text-align:center;">
              <h1 style="margin:0;font-size:22px;color:#ffffff;letter-spacing:1px;font-weight:700;">🌙 DIGITAL TASBIH</h1>
            </td>
          </tr>
          <!-- Body Content -->
          <tr>
            <td style="padding:28px 24px 20px 24px;">
              <p style="margin:0 0 14px 0;font-size:16px;color:#0f172a;font-weight:600;">Assalamu Alaikum,</p>
              <p style="margin:0 0 22px 0;font-size:14px;color:#334155;line-height:1.6;">
                Your one-time verification code for <b>Digital Tasbih | Live Dhikr, Contest, &amp; Account Login</b> is:
              </p>
              
              <!-- OTP Box -->
              <div style="background:#ecfdf5;border:2px dashed #10b981;border-radius:12px;padding:18px 12px;text-align:center;margin:0 0 22px 0;">
                <span style="font-family:'Courier New',Courier,monospace;font-size:34px;font-weight:800;letter-spacing:8px;color:#064e3b;display:inline-block;padding-left:8px;">${otp}</span>
              </div>
              
              <!-- Validity Notice -->
              <div style="background:#fffbeb;border:1px solid #fef3c7;border-radius:8px;padding:12px 14px;margin:0 0 20px 0;">
                <p style="margin:0;font-size:13px;color:#92400e;line-height:1.5;">
                  ⏳ <b>This code is valid for 5 minutes.</b><br>
                  ⚠️ Never share this code with anyone for your account security.
                </p>
              </div>

              <p style="margin:0;font-size:12px;color:#64748b;line-height:1.5;">
                If you did not request this verification code, please disregard this email.
              </p>
            </td>
          </tr>
          <!-- Footer -->
          <tr>
            <td style="border-top:1px solid #f1f5f9;background:#fafafa;padding:18px 24px;text-align:center;">
              <p style="margin:0 0 6px 0;font-size:12px;color:#64748b;">
                © 2026 Digital Tasbih • <a href="https://imaanix.in" style="color:#059669;text-decoration:none;font-weight:500;">imaanix.in</a>
              </p>
              <p style="margin:0;font-size:11px;color:#94a3b8;">
                Need help? Contact <a href="mailto:support@imaanix.in" style="color:#059669;text-decoration:none;">support@imaanix.in</a>
              </p>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</body>
</html>`;
}

async function generateAndSendDailyReport(env) {
  try {
    const now = Date.now();
    const adminEmail = "iimaanix@gmail.com";

    // 1. Build last 7 days dates & labels
    const dates = [];
    const dayLabels = [];
    for (let i = 6; i >= 0; i--) {
      const d = new Date(now - (i * 86400000));
      const iso = d.toISOString().slice(0, 10);
      dates.push(iso);
      const dayNum = d.getDate().toString().padStart(2, "0");
      const monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
      dayLabels.push(`${dayNum} ${monthNames[d.getMonth()]}`);
    }
    const todayDate = dates[dates.length - 1];

    // 2. Fetch rate_limits & analytics_metrics from D1
    const [rateRows, metricRows] = await Promise.all([
      env.DB.prepare(
        "SELECT key, request_count, day_bucket FROM rate_limits WHERE (key LIKE 'global:%' OR key LIKE 'resend:%' OR key LIKE 'brevo:%') LIMIT 100"
      ).all().then(r => r.results || []).catch(() => []),
      env.DB.prepare(
        "SELECT metric_key, category, item_name, day_bucket, count FROM analytics_metrics WHERE day_bucket >= ?1 LIMIT 500"
      ).bind(dates[0]).all().then(r => r.results || []).catch(() => [])
    ]);

    const countMap = {};
    for (const r of rateRows) {
      countMap[r.key] = Number(r.request_count || 0);
    }

    const metricMap = {};
    for (const m of metricRows) {
      if (m.category === "worker") {
        metricMap[`${m.item_name}:${m.day_bucket}`] = (metricMap[`${m.item_name}:${m.day_bucket}`] || 0) + Number(m.count || 0);
      }
    }

    // 3. WORKER 1: Auth & Email OTP (7-Day Trend)
    const authTotalData = [];
    const authResendData = [];
    const authBrevoData = [];
    let auth7DayTotal = 0;

    for (const dt of dates) {
      const g = countMap[`global:${dt}`] || 0;
      let resend = countMap[`resend:${dt}`] || 0;
      let brevo = countMap[`brevo:${dt}`] || 0;
      if (resend === 0 && brevo === 0 && g > 0) resend = g;
      authTotalData.push(g);
      authResendData.push(resend);
      authBrevoData.push(brevo);
      auth7DayTotal += g;
    }

    const yesterdayDate = dates[dates.length - 2] || todayDate;

    // 24-Hour Cycle (Yesterday full 24h + Today morning up to report time)
    const yAuth = countMap[`global:${yesterdayDate}`] || 0;
    const tAuth = countMap[`global:${todayDate}`] || 0;
    const todayAuthCount = yAuth + tAuth; // 24h cycle total
    const authLimit = 400;
    const authRemaining = Math.max(0, authLimit - todayAuthCount);
    const authUsedPercent = ((todayAuthCount / authLimit) * 100).toFixed(1);

    // 4. WORKER 2: Global Campaigns & Group Dhikr
    const campaignData = [];
    let campaign7DayTotal = 0;
    for (const dt of dates) {
      const cCount = metricMap[`campaigns:${dt}`] || 0;
      campaignData.push(cCount);
      campaign7DayTotal += cCount;
    }
    const yCampaign = metricMap[`campaigns:${yesterdayDate}`] || 0;
    const tCampaign = metricMap[`campaigns:${todayDate}`] || 0;
    const todayCampaignCount = yCampaign + tCampaign;
    const campaignLimit = 100000;
    const campaignRemaining = Math.max(0, campaignLimit - todayCampaignCount);
    const campaignUsedPercent = ((todayCampaignCount / campaignLimit) * 100).toFixed(2);

    // 5. WORKER 3: Quran Reader Proxy (`quran-proxy`)
    const quranData = [];
    let quran7DayTotal = 0;
    for (const dt of dates) {
      const qCount = metricMap[`quran:${dt}`] || 0;
      quranData.push(qCount);
      quran7DayTotal += qCount;
    }
    const yQuran = metricMap[`quran:${yesterdayDate}`] || 0;
    const tQuran = metricMap[`quran:${todayDate}`] || 0;
    const todayQuranCount = yQuran + tQuran;
    const quranLimit = 100000;
    const quranRemaining = Math.max(0, quranLimit - todayQuranCount);
    const quranUsedPercent = ((todayQuranCount / quranLimit) * 100).toFixed(2);

    // 6. WORKER 4: Daily Hadith Proxy (`hadith-proxy`)
    const hadithData = [];
    let hadith7DayTotal = 0;
    for (const dt of dates) {
      const hCount = metricMap[`hadith:${dt}`] || 0;
      hadithData.push(hCount);
      hadith7DayTotal += hCount;
    }
    const yHadith = metricMap[`hadith:${yesterdayDate}`] || 0;
    const tHadith = metricMap[`hadith:${todayDate}`] || 0;
    const todayHadithCount = yHadith + tHadith;
    const hadithLimit = 100000;
    const hadithRemaining = Math.max(0, hadithLimit - todayHadithCount);
    const hadithUsedPercent = ((todayHadithCount / hadithLimit) * 100).toFixed(2);

    // 7. WORKER 5: Marble Bead Themes & Invites CDN (`themes-cdn`)
    const cdnData = [];
    let cdn7DayTotal = 0;
    for (const dt of dates) {
      const cCount = metricMap[`cdn:${dt}`] || 0;
      cdnData.push(cCount);
      cdn7DayTotal += cCount;
    }
    const yCdn = metricMap[`cdn:${yesterdayDate}`] || 0;
    const tCdn = metricMap[`cdn:${todayDate}`] || 0;
    const todayCdnCount = yCdn + tCdn;
    const cdnLimit = 100000;
    const cdnRemaining = Math.max(0, cdnLimit - todayCdnCount);
    const cdnUsedPercent = ((todayCdnCount / cdnLimit) * 100).toFixed(2);

    // 8. Global Cloudflare Quota calculation across all 5 workers (24h Cycle)
    const totalYesterday = yAuth + yCampaign + yQuran + yHadith + yCdn;
    const totalToday = tAuth + tCampaign + tQuran + tHadith + tCdn;
    const totalRequestsToday = totalYesterday + totalToday + 2;
    const globalCloudflarePercent = ((totalRequestsToday / 100000) * 100).toFixed(2);
    const globalRemaining = Math.max(0, 100000 - totalRequestsToday);

    // 9. Generate 5 Dedicated QuickChart URLs
    // Chart 1: Auth & OTP Line Chart
    const c1Config = {
      type: "line",
      data: {
        labels: dayLabels,
        datasets: [
          { label: "Total OTP Demand", borderColor: "#059669", backgroundColor: "rgba(5, 150, 105, 0.15)", fill: true, borderWidth: 3, pointRadius: 4, pointBackgroundColor: "#047857", tension: 0.35, data: authTotalData },
          { label: "Resend (Tier 1)", borderColor: "#2563eb", borderWidth: 2, fill: false, tension: 0.35, data: authResendData },
          { label: "Brevo Standby (Tier 2)", borderColor: "#d97706", borderWidth: 2, fill: false, tension: 0.35, data: authBrevoData }
        ]
      },
      options: {
        title: { display: true, text: "1. Auth & Email OTP 7-Day Volume", fontSize: 13, fontColor: "#0f172a", fontStyle: "bold" },
        legend: { position: "bottom", labels: { boxWidth: 12, fontSize: 11 } },
        scales: { yAxes: [{ ticks: { beginAtZero: true, suggestedMax: Math.max(10, todayAuthCount * 1.5) } }] }
      }
    };
    const chart1Url = "https://quickchart.io/chart?" + new URLSearchParams({ w: "600", h: "230", bkg: "white", c: JSON.stringify(c1Config) }).toString();

    // Chart 2: Campaigns Bar Chart
    const c2Config = {
      type: "bar",
      data: {
        labels: dayLabels,
        datasets: [{ label: "Campaign API Requests", backgroundColor: "#6366f1", data: campaignData }]
      },
      options: {
        title: { display: true, text: "2. Global Campaigns & Group Dhikr 7-Day Activity", fontSize: 13, fontColor: "#0f172a", fontStyle: "bold" },
        legend: { position: "bottom", labels: { boxWidth: 12, fontSize: 11 } },
        scales: { yAxes: [{ ticks: { beginAtZero: true, suggestedMax: Math.max(20, todayCampaignCount * 1.5) } }] }
      }
    };
    const chart2Url = "https://quickchart.io/chart?" + new URLSearchParams({ w: "600", h: "230", bkg: "white", c: JSON.stringify(c2Config) }).toString();

    // Chart 3: Quran Reader Proxy Chart
    const c3Config = {
      type: "line",
      data: {
        labels: dayLabels,
        datasets: [{ label: "Quran Surah & Ayah Reads", borderColor: "#0284c7", backgroundColor: "rgba(2, 132, 199, 0.15)", fill: true, borderWidth: 3, tension: 0.35, data: quranData }]
      },
      options: {
        title: { display: true, text: "3. Quran Reader Edge Cache & Surah Deliveries", fontSize: 13, fontColor: "#0f172a", fontStyle: "bold" },
        legend: { position: "bottom", labels: { boxWidth: 12, fontSize: 11 } },
        scales: { yAxes: [{ ticks: { beginAtZero: true, suggestedMax: Math.max(10, todayQuranCount * 1.5) } }] }
      }
    };
    const chart3Url = "https://quickchart.io/chart?" + new URLSearchParams({ w: "600", h: "230", bkg: "white", c: JSON.stringify(c3Config) }).toString();

    // Chart 4: Daily Hadith Proxy Chart
    const c4Config = {
      type: "bar",
      data: {
        labels: dayLabels,
        datasets: [{ label: "Daily Hadith Rotation Requests", backgroundColor: "#d97706", data: hadithData }]
      },
      options: {
        title: { display: true, text: "4. Daily Hadith Proxy Delivery & Shield Health", fontSize: 13, fontColor: "#0f172a", fontStyle: "bold" },
        legend: { position: "bottom", labels: { boxWidth: 12, fontSize: 11 } },
        scales: { yAxes: [{ ticks: { beginAtZero: true, suggestedMax: Math.max(10, todayHadithCount * 1.5) } }] }
      }
    };
    const chart4Url = "https://quickchart.io/chart?" + new URLSearchParams({ w: "600", h: "230", bkg: "white", c: JSON.stringify(c4Config) }).toString();

    // Chart 5: Marble Beads & Themes CDN Chart
    const c5Config = {
      type: "line",
      data: {
        labels: dayLabels,
        datasets: [{ label: "Themes & Marble Bead CDN Downloads", borderColor: "#8b5cf6", backgroundColor: "rgba(139, 92, 246, 0.15)", fill: true, borderWidth: 3, tension: 0.35, data: cdnData }]
      },
      options: {
        title: { display: true, text: "5. Marble Bead Themes & Invites CDN Delivery", fontSize: 13, fontColor: "#0f172a", fontStyle: "bold" },
        legend: { position: "bottom", labels: { boxWidth: 12, fontSize: 11 } },
        scales: { yAxes: [{ ticks: { beginAtZero: true, suggestedMax: Math.max(10, todayCdnCount * 1.5) } }] }
      }
    };
    const chart5Url = "https://quickchart.io/chart?" + new URLSearchParams({ w: "600", h: "230", bkg: "white", c: JSON.stringify(c5Config) }).toString();

    // 9.5 LIVE REAL-TIME HEALTH CHECK ACROSS ALL 5 WORKERS
    const pingWorker = async (url, timeoutMs = 4500) => {
      const controller = new AbortController();
      const timer = setTimeout(() => controller.abort(), timeoutMs);
      try {
        const res = await fetch(url, { signal: controller.signal, headers: { "User-Agent": "DigitalTasbih-Sentinel/1.0" } });
        clearTimeout(timer);
        return { ok: res.ok, status: res.status, error: null };
      } catch (err) {
        clearTimeout(timer);
        return { ok: false, status: 0, error: err.name === "AbortError" ? "Timeout" : (err.message || "Offline") };
      }
    };

    const [h1, h2, h3, h4, h5] = await Promise.all([
      pingWorker("https://api.imaanix.in/auth/"),
      pingWorker("https://api.imaanix.in/campaigns/"),
      pingWorker("https://quran-proxy.digitaltasbih.workers.dev/v1/surahs"),
      pingWorker("https://hadith-proxy.hadithproxy.workers.dev/"),
      pingWorker("https://digitaltasbihimagesonly.taykaro786.workers.dev/wrangler.toml")
    ]);

    const formatBadge = (h) => {
      if (h.ok) {
        return '<span style="background:#dcfce7;color:#15803d;font-size:11px;font-weight:700;padding:3px 10px;border-radius:12px;">ACTIVE 🟢</span>';
      }
      return `<span style="background:#fee2e2;color:#b91c1c;font-size:11px;font-weight:700;padding:3px 10px;border-radius:12px;">DOWN 🔴 (${h.error ? h.error : 'HTTP ' + h.status})</span>`;
    };

    const badgeW1 = formatBadge(h1);
    const badgeW2 = formatBadge(h2);
    const badgeW3 = formatBadge(h3);
    const badgeW4 = formatBadge(h4);
    const badgeW5 = formatBadge(h5);

    const downWorkers = [];
    if (!h1.ok) downWorkers.push("Worker 1 (Auth)");
    if (!h2.ok) downWorkers.push("Worker 2 (Campaigns)");
    if (!h3.ok) downWorkers.push("Worker 3 (Quran)");
    if (!h4.ok) downWorkers.push("Worker 4 (Hadith)");
    if (!h5.ok) downWorkers.push("Worker 5 (Themes CDN)");

    const incidentBanner = downWorkers.length > 0 ? `
      <div style="background:#fef2f2;border:2px solid #ef4444;border-radius:14px;padding:16px 20px;margin-bottom:24px;">
        <div style="display:flex;align-items:center;gap:10px;margin-bottom:6px;">
          <span style="font-size:20px;">🚨</span>
          <div style="font-weight:800;color:#991b1b;font-size:15px;">Live Health Check Alert: Incident Detected!</div>
        </div>
        <p style="margin:0;font-size:12px;color:#7f1d1d;line-height:1.6;">
          Aaj subha report dispatch ke waqt <b>${downWorkers.join(", ")}</b> ne error return kiya hai. Kripya niche individual worker section me live status aur error details verify karein.
        </p>
      </div>` : "";

    // 10. Status & ZeptoMail logic
    let zeptoStatusText = downWorkers.length > 0 ? `🚨 ${downWorkers.length} Worker Issue(s) Detected` : "🟢 All 5 Workers Operational & Healthy";
    let zeptoDesc = `• <b>Zoho ZeptoMail Status:</b> Not needed yet. Aaj ${todayAuthCount}/400 OTPs use hue hain (${authRemaining} free slots available).<br>• <b>Cloudflare Global Status:</b> Healthy. 1,00,000 daily free requests me se sirf ${globalCloudflarePercent}% use hua hai (${globalRemaining.toLocaleString()} remaining).`;

    if (downWorkers.length > 0) {
      zeptoDesc = `⚠️ <b>Alert:</b> ${downWorkers.join(", ")} me downtime/issue mila hai.<br>` + zeptoDesc;
    } else if (todayAuthCount >= 340) {
      zeptoStatusText = "🚨 High Demand Warning (85%+ Used)";
      zeptoDesc = "⚠️ Daily OTP limit ke bohot kareeb pahunch gaye hain! <b>Action Recommended:</b> Zoho ZeptoMail connect karne ka time aa gaya hai taaki koi user drop na ho.";
    }

    // 11. Assemble Master HTML Email
    const masterHtml = `<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>5-Worker Master Infrastructure Report</title>
</head>
<body style="margin:0;padding:24px 12px;background:#f1f5f9;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,Helvetica,Arial,sans-serif;color:#1e293b;">
  <div style="max-width:640px;margin:0 auto;background:#ffffff;border-radius:20px;border:1px solid #e2e8f0;box-shadow:0 8px 24px rgba(0,0,0,0.06);overflow:hidden;">
    
    <!-- Header -->
    <div style="background:linear-gradient(135deg, #064e3b 0%, #047857 50%, #065f46 100%);padding:32px 24px;text-align:center;">
      <span style="background:rgba(255,255,255,0.2);color:#d1fae5;font-size:11px;font-weight:700;letter-spacing:1px;padding:5px 14px;border-radius:20px;text-transform:uppercase;">Master Cloudflare Infrastructure</span>
      <h1 style="margin:14px 0 6px 0;font-size:24px;color:#ffffff;letter-spacing:0.5px;font-weight:800;">🌙 DIGITAL TASBIH</h1>
      <p style="margin:0;font-size:14px;color:#a7f3d0;font-weight:500;">5-Worker Infrastructure &amp; Edge Quota Report</p>
    </div>

    <!-- Master Quota Summary Bar -->
    <div style="background:#ecfdf5;border-bottom:1px solid #d1fae5;padding:16px 24px;">
      <div style="display:flex;justify-content:space-between;align-items:center;font-size:13px;font-weight:700;margin-bottom:6px;">
        <span style="color:#065f46;">☁️ Cloudflare Global Request Quota</span>
        <span style="color:#047857;">${totalRequestsToday} / 100,000 (${globalCloudflarePercent}% Used)</span>
      </div>
      <div style="background:#d1fae5;height:8px;border-radius:10px;overflow:hidden;width:100%;">
        <div style="background:#059669;width:${Math.max(1, globalCloudflarePercent)}%;height:8px;border-radius:10px;"></div>
      </div>
      <div style="font-size:11px;color:#047857;margin-top:6px;font-weight:600;">
        🟢 ${globalRemaining.toLocaleString()} Free Requests Remaining Today • 100% Free Tier Guaranteed
      </div>
    </div>

    <div style="padding:24px;">

      ${incidentBanner}

      <!-- SECTION 1: AUTH & OTP WORKER -->
      <div style="background:#f8fafc;border:1px solid #e2e8f0;border-radius:14px;padding:18px;margin-bottom:24px;">
        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:8px;">
          <div>
            <h3 style="margin:0;font-size:16px;color:#0f172a;font-weight:800;">1️⃣ User Login &amp; Email OTP Engine</h3>
            <span style="font-size:11px;color:#64748b;font-family:monospace;">api.imaanix.in/auth/ (digital-tasbih-auth)</span>
          </div>
          ${badgeW1}
        </div>
        <p style="margin:0 0 12px 0;font-size:12px;color:#475569;line-height:1.5;">
          App users ka login, sign-up aur verification OTPs bhejta hai (Resend &amp; Brevo standby failover).
        </p>
        <table width="100%" cellspacing="0" cellpadding="0" style="margin-bottom:12px;background:#ffffff;border:1px solid #e2e8f0;border-radius:10px;padding:10px;">
          <tr>
            <td width="33%" align="center">
              <div style="font-size:10px;color:#64748b;font-weight:600;text-transform:uppercase;">Daily Limit</div>
              <div style="font-size:18px;color:#0f172a;font-weight:800;margin-top:2px;">400 <span style="font-size:10px;color:#64748b;">OTPs</span></div>
            </td>
            <td width="33%" align="center" style="border-left:1px solid #f1f5f9;border-right:1px solid #f1f5f9;">
              <div style="font-size:10px;color:#64748b;font-weight:600;text-transform:uppercase;">24h Cycle (Kal + Aaj)</div>
              <div style="font-size:18px;color:#0284c7;font-weight:800;margin-top:2px;">${todayAuthCount} <span style="font-size:10px;color:#64748b;">(${yAuth}k + ${tAuth}a)</span></div>
            </td>
            <td width="33%" align="center">
              <div style="font-size:10px;color:#64748b;font-weight:600;text-transform:uppercase;">Remaining Free</div>
              <div style="font-size:18px;color:#059669;font-weight:800;margin-top:2px;">${authRemaining} <span style="font-size:10px;color:#64748b;">Slots</span></div>
            </td>
          </tr>
        </table>
        <div style="background:#e2e8f0;height:6px;border-radius:6px;overflow:hidden;margin-bottom:14px;">
          <div style="background:#059669;width:${Math.max(1, authUsedPercent)}%;height:6px;"></div>
        </div>
        <div style="background:#ffffff;border:1px solid #e2e8f0;border-radius:10px;padding:8px;text-align:center;">
          <img src="${chart1Url}" alt="Auth &amp; OTP Demand Trend" width="100%" style="max-width:580px;height:auto;border-radius:8px;display:block;margin:0 auto;border:0;" />
        </div>
      </div>

      <!-- SECTION 2: CAMPAIGNS & GROUP DHIKR WORKER -->
      <div style="background:#f8fafc;border:1px solid #e2e8f0;border-radius:14px;padding:18px;margin-bottom:24px;">
        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:8px;">
          <div>
            <h3 style="margin:0;font-size:16px;color:#0f172a;font-weight:800;">2️⃣ Global Campaigns &amp; Group Dhikr Engine</h3>
            <span style="font-size:11px;color:#64748b;font-family:monospace;">api.imaanix.in/campaigns/ (digital-tasbih-campaigns)</span>
          </div>
          ${badgeW2}
        </div>
        <p style="margin:0 0 12px 0;font-size:12px;color:#475569;line-height:1.5;">
          1 Crore Salawat global counter, group dhikr rooms aur batch tally submissions handle karta hai.
        </p>
        <table width="100%" cellspacing="0" cellpadding="0" style="margin-bottom:12px;background:#ffffff;border:1px solid #e2e8f0;border-radius:10px;padding:10px;">
          <tr>
            <td width="33%" align="center">
              <div style="font-size:10px;color:#64748b;font-weight:600;text-transform:uppercase;">Daily Limit</div>
              <div style="font-size:18px;color:#0f172a;font-weight:800;margin-top:2px;">100,000</div>
            </td>
            <td width="33%" align="center" style="border-left:1px solid #f1f5f9;border-right:1px solid #f1f5f9;">
              <div style="font-size:10px;color:#64748b;font-weight:600;text-transform:uppercase;">24h Cycle (Kal + Aaj)</div>
              <div style="font-size:18px;color:#6366f1;font-weight:800;margin-top:2px;">${todayCampaignCount} <span style="font-size:10px;color:#64748b;">(${yCampaign}k + ${tCampaign}a)</span></div>
            </td>
            <td width="33%" align="center">
              <div style="font-size:10px;color:#64748b;font-weight:600;text-transform:uppercase;">Remaining Free</div>
              <div style="font-size:18px;color:#059669;font-weight:800;margin-top:2px;">${campaignRemaining.toLocaleString()}</div>
            </td>
          </tr>
        </table>
        <div style="background:#e2e8f0;height:6px;border-radius:6px;overflow:hidden;margin-bottom:14px;">
          <div style="background:#6366f1;width:${Math.max(1, campaignUsedPercent)}%;height:6px;"></div>
        </div>
        <div style="background:#ffffff;border:1px solid #e2e8f0;border-radius:10px;padding:8px;text-align:center;">
          <img src="${chart2Url}" alt="Campaigns &amp; Group Dhikr" width="100%" style="max-width:580px;height:auto;border-radius:8px;display:block;margin:0 auto;border:0;" />
        </div>
      </div>

      <!-- SECTION 3: QURAN READER PROXY -->
      <div style="background:#f8fafc;border:1px solid #e2e8f0;border-radius:14px;padding:18px;margin-bottom:24px;">
        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:8px;">
          <div>
            <h3 style="margin:0;font-size:16px;color:#0f172a;font-weight:800;">3️⃣ Quran Reader Edge Proxy &amp; Cache</h3>
            <span style="font-size:11px;color:#64748b;font-family:monospace;">quran-proxy.digitaltasbih.workers.dev</span>
          </div>
          ${badgeW3}
        </div>
        <p style="margin:0 0 12px 0;font-size:12px;color:#475569;line-height:1.5;">
          Surah, Ayah aur Arabic/Urdu translation text ko Cloudflare Edge par cache karke instant deliver karta hai.
        </p>
        <table width="100%" cellspacing="0" cellpadding="0" style="margin-bottom:12px;background:#ffffff;border:1px solid #e2e8f0;border-radius:10px;padding:10px;">
          <tr>
            <td width="33%" align="center">
              <div style="font-size:10px;color:#64748b;font-weight:600;text-transform:uppercase;">Daily Limit</div>
              <div style="font-size:18px;color:#0f172a;font-weight:800;margin-top:2px;">100,000</div>
            </td>
            <td width="33%" align="center" style="border-left:1px solid #f1f5f9;border-right:1px solid #f1f5f9;">
              <div style="font-size:10px;color:#64748b;font-weight:600;text-transform:uppercase;">24h Cycle (Kal + Aaj)</div>
              <div style="font-size:18px;color:#0284c7;font-weight:800;margin-top:2px;">${todayQuranCount} <span style="font-size:10px;color:#64748b;">(${yQuran}k + ${tQuran}a)</span></div>
            </td>
            <td width="33%" align="center">
              <div style="font-size:10px;color:#64748b;font-weight:600;text-transform:uppercase;">Remaining Free</div>
              <div style="font-size:18px;color:#059669;font-weight:800;margin-top:2px;">${quranRemaining.toLocaleString()}</div>
            </td>
          </tr>
        </table>
        <div style="background:#e2e8f0;height:6px;border-radius:6px;overflow:hidden;margin-bottom:14px;">
          <div style="background:#0284c7;width:${Math.max(1, quranUsedPercent)}%;height:6px;"></div>
        </div>
        <div style="background:#ffffff;border:1px solid #e2e8f0;border-radius:10px;padding:8px;text-align:center;">
          <img src="${chart3Url}" alt="Quran Reader Edge Proxy Trend" width="100%" style="max-width:580px;height:auto;border-radius:8px;display:block;margin:0 auto;border:0;" />
        </div>
      </div>

      <!-- SECTION 4: DAILY HADITH PROXY -->
      <div style="background:#f8fafc;border:1px solid #e2e8f0;border-radius:14px;padding:18px;margin-bottom:24px;">
        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:8px;">
          <div>
            <h3 style="margin:0;font-size:16px;color:#0f172a;font-weight:800;">4️⃣ Daily Hadith Proxy &amp; Shield</h3>
            <span style="font-size:11px;color:#64748b;font-family:monospace;">hadith-proxy.hadithproxy.workers.dev</span>
          </div>
          ${badgeW4}
        </div>
        <p style="margin:0 0 12px 0;font-size:12px;color:#475569;line-height:1.5;">
          Rozana authentic Hadith Sharif fetch karta hai aur third-party IP rate-limit blocks se bachata hai.
        </p>
        <table width="100%" cellspacing="0" cellpadding="0" style="margin-bottom:12px;background:#ffffff;border:1px solid #e2e8f0;border-radius:10px;padding:10px;">
          <tr>
            <td width="33%" align="center">
              <div style="font-size:10px;color:#64748b;font-weight:600;text-transform:uppercase;">Daily Limit</div>
              <div style="font-size:18px;color:#0f172a;font-weight:800;margin-top:2px;">100,000</div>
            </td>
            <td width="33%" align="center" style="border-left:1px solid #f1f5f9;border-right:1px solid #f1f5f9;">
              <div style="font-size:10px;color:#64748b;font-weight:600;text-transform:uppercase;">24h Cycle (Kal + Aaj)</div>
              <div style="font-size:18px;color:#d97706;font-weight:800;margin-top:2px;">${todayHadithCount} <span style="font-size:10px;color:#64748b;">(${yHadith}k + ${tHadith}a)</span></div>
            </td>
            <td width="33%" align="center">
              <div style="font-size:10px;color:#64748b;font-weight:600;text-transform:uppercase;">Remaining Free</div>
              <div style="font-size:18px;color:#059669;font-weight:800;margin-top:2px;">${hadithRemaining.toLocaleString()}</div>
            </td>
          </tr>
        </table>
        <div style="background:#e2e8f0;height:6px;border-radius:6px;overflow:hidden;margin-bottom:14px;">
          <div style="background:#d97706;width:${Math.max(1, hadithUsedPercent)}%;height:6px;"></div>
        </div>
        <div style="background:#ffffff;border:1px solid #e2e8f0;border-radius:10px;padding:8px;text-align:center;">
          <img src="${chart4Url}" alt="Daily Hadith Proxy Trend" width="100%" style="max-width:580px;height:auto;border-radius:8px;display:block;margin:0 auto;border:0;" />
        </div>
      </div>

      <!-- SECTION 5: MARBLE BEADS & THEMES CDN -->
      <div style="background:#f8fafc;border:1px solid #e2e8f0;border-radius:14px;padding:18px;margin-bottom:24px;">
        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:8px;">
          <div>
            <h3 style="margin:0;font-size:16px;color:#0f172a;font-weight:800;">5️⃣ Marble Bead Themes &amp; Invites CDN</h3>
            <span style="font-size:11px;color:#64748b;font-family:monospace;">digitaltasbihimagesonly.taykaro786.workers.dev</span>
          </div>
          ${badgeW5}
        </div>
        <p style="margin:0 0 12px 0;font-size:12px;color:#475569;line-height:1.5;">
          Realistic 3D Marble bead textures, background skins aur invite badges ko global cache se ultra-fast serve karta hai.
        </p>
        <table width="100%" cellspacing="0" cellpadding="0" style="margin-bottom:12px;background:#ffffff;border:1px solid #e2e8f0;border-radius:10px;padding:10px;">
          <tr>
            <td width="33%" align="center">
              <div style="font-size:10px;color:#64748b;font-weight:600;text-transform:uppercase;">Daily Limit</div>
              <div style="font-size:18px;color:#0f172a;font-weight:800;margin-top:2px;">100,000</div>
            </td>
            <td width="33%" align="center" style="border-left:1px solid #f1f5f9;border-right:1px solid #f1f5f9;">
              <div style="font-size:10px;color:#64748b;font-weight:600;text-transform:uppercase;">24h Cycle (Kal + Aaj)</div>
              <div style="font-size:18px;color:#8b5cf6;font-weight:800;margin-top:2px;">${todayCdnCount} <span style="font-size:10px;color:#64748b;">(${yCdn}k + ${tCdn}a)</span></div>
            </td>
            <td width="33%" align="center">
              <div style="font-size:10px;color:#64748b;font-weight:600;text-transform:uppercase;">Remaining Free</div>
              <div style="font-size:18px;color:#059669;font-weight:800;margin-top:2px;">${cdnRemaining.toLocaleString()}</div>
            </td>
          </tr>
        </table>
        <div style="background:#e2e8f0;height:6px;border-radius:6px;overflow:hidden;margin-bottom:14px;">
          <div style="background:#8b5cf6;width:${Math.max(1, cdnUsedPercent)}%;height:6px;"></div>
        </div>
        <div style="background:#ffffff;border:1px solid #e2e8f0;border-radius:10px;padding:8px;text-align:center;">
          <img src="${chart5Url}" alt="Marble Bead Themes CDN Trend" width="100%" style="max-width:580px;height:auto;border-radius:8px;display:block;margin:0 auto;border:0;" />
        </div>
      </div>

      <!-- ZEPTO MAIL RECOMMENDATION & SYSTEM HEALTH -->
      <div style="background:#f0fdf4;border:1px solid #bbf7d0;border-radius:14px;padding:16px;">
        <div style="font-size:14px;font-weight:800;color:#166534;margin-bottom:4px;">
          ${zeptoStatusText}
        </div>
        <div style="font-size:12px;color:#15803d;line-height:1.6;">
          ${zeptoDesc}
        </div>
      </div>

    </div>

    <!-- Footer -->
    <div style="border-top:1px solid #f1f5f9;background:#fafafa;padding:20px 24px;text-align:center;">
      <p style="margin:0 0 6px 0;font-size:12px;color:#64748b;">
        © 2026 Digital Tasbih Master Intelligence • <a href="https://imaanix.in" style="color:#059669;text-decoration:none;font-weight:600;">imaanix.in</a>
      </p>
      <p style="margin:0;font-size:11px;color:#94a3b8;">
        Delivered daily to Admin (${adminEmail}) via Cloudflare Scheduled Worker
      </p>
    </div>

  </div>
</body>
</html>`;

    // 12. Dispatch via Brevo
    if (env.BREVO_API_KEY) {
      await fetch("https://api.brevo.com/v3/smtp/email", {
        method: "POST",
        headers: {
          "api-key": env.BREVO_API_KEY,
          "Content-Type": "application/json",
          "Accept": "application/json"
        },
        body: JSON.stringify({
          sender: { name: "Digital Tasbih Master Intelligence", email: "reports@imaanix.in" },
          to: [{ email: adminEmail, name: "Admin Abdul Quadir" }],
          subject: `📊 Master 5-Worker Infrastructure Report: ${todayAuthCount} OTPs (${yAuth} Kal + ${tAuth} Aaj) | ${totalRequestsToday} Requests (${totalYesterday} Kal + ${totalToday} Aaj)`, 
          htmlContent: masterHtml
        })
      });
    }

    return { success: true, timestamp: now, todayAuthCount, totalRequestsToday, recipients: [adminEmail] };
  } catch (err) {
    console.error("[DailyReport] Error generating report:", err);
    return { success: false, error: err.message };
  }
}
