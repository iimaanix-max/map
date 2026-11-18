import os

DIAGRAMS_DIR = os.path.abspath("PRIVACY_DATA_AUDIT_REPORT/diagrams")

# 1. Architecture & Data Flow SVG
svg_architecture = """<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1200 780" width="100%" height="100%" style="background:#0b131a; font-family:'Segoe UI',system-ui,-apple-system,sans-serif;">
  <defs>
    <linearGradient id="gradEmerald" x1="0%" y1="0%" x2="100%" y2="100%">
      <stop offset="0%" stop-color="#10b981" stop-opacity="0.25"/>
      <stop offset="100%" stop-color="#047857" stop-opacity="0.1"/>
    </linearGradient>
    <linearGradient id="gradGold" x1="0%" y1="0%" x2="100%" y2="100%">
      <stop offset="0%" stop-color="#f59e0b" stop-opacity="0.25"/>
      <stop offset="100%" stop-color="#b45309" stop-opacity="0.1"/>
    </linearGradient>
    <linearGradient id="gradBlue" x1="0%" y1="0%" x2="100%" y2="100%">
      <stop offset="0%" stop-color="#3b82f6" stop-opacity="0.25"/>
      <stop offset="100%" stop-color="#1d4ed8" stop-opacity="0.1"/>
    </linearGradient>
    <linearGradient id="gradPurple" x1="0%" y1="0%" x2="100%" y2="100%">
      <stop offset="0%" stop-color="#8b5cf6" stop-opacity="0.25"/>
      <stop offset="100%" stop-color="#6d28d9" stop-opacity="0.1"/>
    </linearGradient>
    <filter id="shadow" x="-5%" y="-5%" width="110%" height="110%">
      <feDropShadow dx="0" dy="4" stdDeviation="6" flood-color="#000" flood-opacity="0.4"/>
    </filter>
    <marker id="arrowGreen" viewBox="0 0 10 10" refX="6" refY="5" markerWidth="6" markerHeight="6" orient="auto-start-reverse">
      <path d="M 0 1 L 8 5 L 0 9 z" fill="#10b981"/>
    </marker>
    <marker id="arrowGold" viewBox="0 0 10 10" refX="6" refY="5" markerWidth="6" markerHeight="6" orient="auto-start-reverse">
      <path d="M 0 1 L 8 5 L 0 9 z" fill="#f59e0b"/>
    </marker>
    <marker id="arrowRed" viewBox="0 0 10 10" refX="6" refY="5" markerWidth="6" markerHeight="6" orient="auto-start-reverse">
      <path d="M 0 1 L 8 5 L 0 9 z" fill="#ef4444"/>
    </marker>
    <marker id="arrowBlue" viewBox="0 0 10 10" refX="6" refY="5" markerWidth="6" markerHeight="6" orient="auto-start-reverse">
      <path d="M 0 1 L 8 5 L 0 9 z" fill="#3b82f6"/>
    </marker>
  </defs>

  <!-- Title & Subtitle -->
  <text x="600" y="45" text-anchor="middle" fill="#f8fafc" font-size="24" font-weight="700" letter-spacing="0.5">Digital Tasbih (com.tasbih.digitaltasbih) - System Architecture &amp; Data Flows</text>
  <text x="600" y="70" text-anchor="middle" fill="#94a3b8" font-size="13">High-Level Data Topology: Local Sandboxed Storage, External 3rd Party APIs, and Cloud Infrastructure</text>

  <!-- Box 1: Android Client Container -->
  <rect x="50" y="100" width="480" height="640" rx="16" fill="#0f172a" stroke="#1e293b" stroke-width="2" filter="url(#shadow)"/>
  <rect x="50" y="100" width="480" height="42" rx="16" fill="url(#gradEmerald)"/>
  <text x="75" y="128" fill="#10b981" font-size="16" font-weight="700">📱 Android Client Device</text>
  <text x="380" y="127" fill="#64748b" font-size="11">Sandboxed Environment</text>

  <!-- Section A: 100% Offline / Local Features -->
  <rect x="70" y="160" width="440" height="150" rx="12" fill="#132328" stroke="#10b981" stroke-width="1.5" stroke-dasharray="4,4"/>
  <text x="85" y="185" fill="#34d399" font-size="13" font-weight="700">🔒 100% OFFLINE LOCAL SPHERE (Zero Server Contact)</text>
  <text x="85" y="210" fill="#cbd5e1" font-size="12">• Tasbih Beads &amp; Digital Counter (TasbihScreen.kt - FROZEN)</text>
  <text x="85" y="230" fill="#cbd5e1" font-size="12">• Lifetime Dhikr Badges &amp; Daily Statistics (TasbihPreferences.kt)</text>
  <text x="85" y="250" fill="#cbd5e1" font-size="12">• Quran Reading Bookmarks, Offline Surahs, Personal Dua Lists</text>
  <text x="85" y="275" fill="#f87171" font-size="11" font-weight="600">⚠️ Storage: Standard Unencrypted SharedPreferences (tasbih_prefs)</text>
  <text x="85" y="295" fill="#94a3b8" font-size="10">Auto-Backup Leaks: backup_rules.xml includes all SharedPrefs to Google Drive</text>

  <!-- Section B: Online Features -->
  <rect x="70" y="325" width="440" height="235" rx="12" fill="#1e1e38" stroke="#6366f1" stroke-width="1.5"/>
  <text x="85" y="350" fill="#a5b4fc" font-size="13" font-weight="700">🌐 ONLINE &amp; COMMUNITY MODULES</text>
  <text x="85" y="375" fill="#cbd5e1" font-size="12">• Group Dhikr Rooms (Anonymous Auth / Google Sign-In)</text>
  <text x="85" y="395" fill="#cbd5e1" font-size="12">• Global Umrah Contest Registration (Name, Phone, Email, City)</text>
  <text x="85" y="415" fill="#cbd5e1" font-size="12">• Prayer Times &amp; Qibla (GPS Coordinates to Aladhan API)</text>
  <text x="85" y="435" fill="#cbd5e1" font-size="12">• Audio Recitation Streaming (Quran.com &amp; JSDelivr CDN)</text>
  <text x="85" y="460" fill="#fbbf24" font-size="11" font-weight="600">⚠️ Local Cache: dt_gc_phone, dt_gc_name, dt_gc_city, pr_lat, pr_lon</text>
  <text x="85" y="480" fill="#cbd5e1" font-size="11">• 18+ Age Self-Declaration Gate: dt_user_is_18_plus</text>
  <text x="85" y="505" fill="#f87171" font-size="11" font-weight="600">❌ In-App Deletion: Simulated boolean flag (No API call)</text>
  <text x="85" y="525" fill="#94a3b8" font-size="10">SettingsScreen.kt: DeleteMyDataDpdpModal sets isSubmitted = true</text>

  <!-- Section C: Ad & Telemetry Client -->
  <rect x="70" y="575" width="440" height="145" rx="12" fill="#1c2438" stroke="#3b82f6" stroke-width="1.5"/>
  <text x="85" y="600" fill="#93c5fd" font-size="13" font-weight="700">📊 SDK &amp; TELEMETRY CLIENTS</text>
  <text x="85" y="625" fill="#cbd5e1" font-size="12">• Google AdMob SDK 23.6.0 (AAID, Coarse Location, Ad events)</text>
  <text x="85" y="645" fill="#cbd5e1" font-size="12">• Firebase Analytics &amp; Crashlytics (Instance ID, Stack traces)</text>
  <text x="85" y="665" fill="#cbd5e1" font-size="12">• Google UMP Consent Dialog (EEA/UK compliance modal)</text>
  <text x="85" y="695" fill="#38bdf8" font-size="11">Data is dispatched directly to Google servers via Google Play Services</text>

  <!-- External 3rd Party APIs (Top Right) -->
  <rect x="670" y="100" width="480" height="175" rx="16" fill="#0f172a" stroke="#1e293b" stroke-width="2" filter="url(#shadow)"/>
  <rect x="670" y="100" width="480" height="38" rx="16" fill="url(#gradBlue)"/>
  <text x="695" y="126" fill="#60a5fa" font-size="15" font-weight="700">🌍 Direct 3rd-Party Service APIs</text>

  <rect x="690" y="150" width="210" height="55" rx="8" fill="#1e293b"/>
  <text x="705" y="172" fill="#f8fafc" font-size="12" font-weight="600">Aladhan API</text>
  <text x="705" y="192" fill="#94a3b8" font-size="10">Sends: pr_lat, pr_lon (Exact GPS)</text>

  <rect x="920" y="150" width="210" height="55" rx="8" fill="#1e293b"/>
  <text x="935" y="172" fill="#f8fafc" font-size="12" font-weight="600">Quran.com &amp; JSDelivr</text>
  <text x="935" y="192" fill="#94a3b8" font-size="10">Sends: IP, Stream Quran audio</text>

  <rect x="690" y="215" width="210" height="50" rx="8" fill="#1e293b"/>
  <text x="705" y="235" fill="#f8fafc" font-size="12" font-weight="600">Google AdMob</text>
  <text x="705" y="253" fill="#94a3b8" font-size="10">Sends: AAID, Ad clicks, Telemetry</text>

  <rect x="920" y="215" width="210" height="50" rx="8" fill="#1e293b"/>
  <text x="935" y="235" fill="#f8fafc" font-size="12" font-weight="600">Firebase Analytics</text>
  <text x="935" y="253" fill="#94a3b8" font-size="10">Sends: App Instance ID, Crashes</text>

  <!-- Cloudflare & Backend Infrastructure (Bottom Right) -->
  <rect x="670" y="300" width="480" height="440" rx="16" fill="#0f172a" stroke="#1e293b" stroke-width="2" filter="url(#shadow)"/>
  <rect x="670" y="300" width="480" height="38" rx="16" fill="url(#gradGold)"/>
  <text x="695" y="326" fill="#fbbf24" font-size="15" font-weight="700">☁️ Imaanix Cloud Backend (Cloudflare &amp; GCP)</text>

  <!-- auth-worker -->
  <rect x="690" y="350" width="215" height="110" rx="10" fill="#1c1917" stroke="#f59e0b" stroke-width="1.5"/>
  <text x="705" y="372" fill="#fbbf24" font-size="12" font-weight="700">auth-worker.js (Cloudflare)</text>
  <text x="705" y="392" fill="#e2e8f0" font-size="11">• /api/auth/request-otp</text>
  <text x="705" y="410" fill="#e2e8f0" font-size="11">• /api/auth/verify-otp</text>
  <text x="705" y="430" fill="#f87171" font-size="10">Reads CF-Connecting-IP (Memory)</text>
  <text x="705" y="448" fill="#94a3b8" font-size="10">Rate Limit: 5/hr, 120 req/min</text>

  <!-- campaigns-worker -->
  <rect x="925" y="350" width="210" height="110" rx="10" fill="#1c1917" stroke="#f59e0b" stroke-width="1.5"/>
  <text x="940" y="372" fill="#fbbf24" font-size="12" font-weight="700">campaigns-worker.js</text>
  <text x="940" y="392" fill="#e2e8f0" font-size="11">• /api/campaigns/register</text>
  <text x="940" y="410" fill="#e2e8f0" font-size="11">• /api/campaigns/submit-batch</text>
  <text x="940" y="430" fill="#e2e8f0" font-size="11">• /api/campaigns/leaderboard</text>
  <text x="940" y="448" fill="#f87171" font-size="10">Purge is MANUAL (No auto-cron)</text>

  <!-- Cloudflare D1 -->
  <rect x="690" y="480" width="215" height="115" rx="10" fill="#1e293b" stroke="#38bdf8" stroke-width="1.5"/>
  <text x="705" y="502" fill="#38bdf8" font-size="12" font-weight="700">Cloudflare D1 (SQL)</text>
  <text x="705" y="522" fill="#cbd5e1" font-size="11">otp_records: <tspan fill="#f87171">Plain Email</tspan>, OTP Hash</text>
  <text x="705" y="540" fill="#cbd5e1" font-size="11">rate_limits: <tspan fill="#f87171">email:user@domain</tspan></text>
  <text x="705" y="558" fill="#cbd5e1" font-size="11">user_submit_cooldowns: uid</text>
  <text x="705" y="576" fill="#cbd5e1" font-size="11">analytics_metrics: counters</text>

  <!-- Google Cloud Firestore -->
  <rect x="925" y="480" width="210" height="115" rx="10" fill="#1e293b" stroke="#a855f7" stroke-width="1.5"/>
  <text x="940" y="502" fill="#c084fc" font-size="12" font-weight="700">Cloud Firestore</text>
  <text x="940" y="522" fill="#cbd5e1" font-size="10">entries/{uid}: <tspan fill="#f87171">Plain Phone E.164</tspan></text>
  <text x="940" y="538" fill="#cbd5e1" font-size="10">entries/{uid}: <tspan fill="#f87171">Plain Email, Name</tspan></text>
  <text x="940" y="554" fill="#cbd5e1" font-size="10">contest_registry: HMAC Hashes</text>
  <text x="940" y="570" fill="#cbd5e1" font-size="10">campaign_members: Counts</text>

  <!-- Email Relays -->
  <rect x="690" y="615" width="445" height="105" rx="10" fill="#172554" stroke="#60a5fa" stroke-width="1.5"/>
  <text x="705" y="638" fill="#93c5fd" font-size="12" font-weight="700">✉️ Transactional Email Dispatch Gateways</text>
  <text x="705" y="660" fill="#cbd5e1" font-size="11">1. <tspan font-weight="600" fill="#f8fafc">Resend API (Primary):</tspan> Sends single-use 6-digit OTP code to user's plain email address.</text>
  <text x="705" y="682" fill="#cbd5e1" font-size="11">2. <tspan font-weight="600" fill="#f8fafc">Brevo SMTP Relay (Fallback/Alerts):</tspan> alerts@imaanix.in quota &amp; threshold alerts.</text>
  <text x="705" y="704" fill="#94a3b8" font-size="10">Both receive user plain-text email address via TLS encrypted HTTPS/SMTP connections.</text>

  <!-- Connecting Lines / Arrows -->
  <!-- App -> Aladhan -->
  <path d="M 510 420 Q 600 350 690 180" fill="none" stroke="#3b82f6" stroke-width="2" marker-end="url(#arrowBlue)"/>
  <!-- App -> AdMob & Firebase -->
  <path d="M 510 630 Q 580 500 690 240" fill="none" stroke="#3b82f6" stroke-width="1.5" stroke-dasharray="4,4" marker-end="url(#arrowBlue)"/>
  <!-- App -> auth-worker -->
  <path d="M 510 380 L 685 390" fill="none" stroke="#f59e0b" stroke-width="2" marker-end="url(#arrowGold)"/>
  <!-- App -> campaigns-worker -->
  <path d="M 510 405 Q 700 420 920 405" fill="none" stroke="#f59e0b" stroke-width="2" marker-end="url(#arrowGold)"/>
  <!-- auth-worker -> D1 -->
  <path d="M 797 460 L 797 475" fill="none" stroke="#38bdf8" stroke-width="2" marker-end="url(#arrowBlue)"/>
  <!-- auth-worker -> Email -->
  <path d="M 750 460 Q 660 550 720 610" fill="none" stroke="#60a5fa" stroke-width="2" marker-end="url(#arrowBlue)"/>
  <!-- campaigns-worker -> Firestore -->
  <path d="M 1030 460 L 1030 475" fill="none" stroke="#a855f7" stroke-width="2" marker-end="url(#arrowBlue)"/>

</svg>
"""

with open(os.path.join(DIAGRAMS_DIR, "01_system_architecture_data_flow.svg"), "w", encoding="utf-8") as f:
    f.write(svg_architecture)

print("Saved 01_system_architecture_data_flow.svg")

# 2. Backend Storage PII Map SVG
svg_pii_map = """<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1200 780" width="100%" height="100%" style="background:#0b131a; font-family:'Segoe UI',system-ui,-apple-system,sans-serif;">
  <defs>
    <filter id="shadow2" x="-5%" y="-5%" width="110%" height="110%">
      <feDropShadow dx="0" dy="4" stdDeviation="6" flood-color="#000" flood-opacity="0.4"/>
    </filter>
  </defs>

  <text x="600" y="45" text-anchor="middle" fill="#f8fafc" font-size="24" font-weight="700">Digital Tasbih - PII &amp; Sensitive Data Storage Reality Map</text>
  <text x="600" y="70" text-anchor="middle" fill="#94a3b8" font-size="13">Code-Verified Inventory: Plain-Text Identifiers vs Cryptographic Hashes vs Sandboxed Local Data</text>

  <!-- Legend -->
  <rect x="250" y="90" width="140" height="30" rx="6" fill="#450a0a" stroke="#ef4444" stroke-width="1.5"/>
  <circle cx="265" cy="105" r="6" fill="#ef4444"/>
  <text x="280" y="110" fill="#fca5a5" font-size="11" font-weight="700">PLAIN-TEXT PII</text>

  <rect x="420" y="90" width="170" height="30" rx="6" fill="#1e1b4b" stroke="#818cf8" stroke-width="1.5"/>
  <circle cx="435" cy="105" r="6" fill="#818cf8"/>
  <text x="450" y="110" fill="#c7d2fe" font-size="11" font-weight="700">HMAC-SHA256 HASH</text>

  <rect x="620" y="90" width="170" height="30" rx="6" fill="#064e3b" stroke="#10b981" stroke-width="1.5"/>
  <circle cx="635" cy="105" r="6" fill="#10b981"/>
  <text x="650" y="110" fill="#6ee7b7" font-size="11" font-weight="700">LOCAL DEVICE ONLY</text>

  <rect x="820" y="90" width="140" height="30" rx="6" fill="#1e293b" stroke="#94a3b8" stroke-width="1.5"/>
  <circle cx="835" cy="105" r="6" fill="#94a3b8"/>
  <text x="850" y="110" fill="#cbd5e1" font-size="11" font-weight="700">PUBLIC DISPLAY</text>

  <!-- Column 1: Local Device Storage -->
  <rect x="50" y="140" width="340" height="600" rx="14" fill="#0f172a" stroke="#1e293b" stroke-width="2" filter="url(#shadow2)"/>
  <rect x="50" y="140" width="340" height="40" rx="14" fill="#132e24"/>
  <text x="70" y="166" fill="#34d399" font-size="15" font-weight="700">📱 Local Android Storage</text>
  <text x="70" y="200" fill="#fca5a5" font-size="12" font-weight="700">File: SharedPref ("dt_gc_phone")</text>
  <text x="70" y="218" fill="#cbd5e1" font-size="11">• Phone Number: Plain text E.164 (+91...)</text>
  <text x="70" y="234" fill="#cbd5e1" font-size="11">• Display Name: Plain text ("dt_gc_name")</text>
  <text x="70" y="250" fill="#cbd5e1" font-size="11">• City / State: Plain text ("dt_gc_city")</text>
  <text x="70" y="266" fill="#ef4444" font-size="10" font-weight="600">❌ UNENCRYPTED: Stored via standard getSharedPreferences()</text>
  <text x="70" y="282" fill="#ef4444" font-size="10" font-weight="600">⚠️ AUTO-BACKUP: Uploaded to Google Drive unencrypted</text>

  <line x1="60" y1="300" x2="380" y2="300" stroke="#1e293b" stroke-width="1.5"/>

  <text x="70" y="325" fill="#6ee7b7" font-size="12" font-weight="700">File: "tasbih_prefs" (100% Local)</text>
  <text x="70" y="343" fill="#cbd5e1" font-size="11">• Session Count, All-Time Count</text>
  <text x="70" y="359" fill="#cbd5e1" font-size="11">• Lifetime Dhikr Badges &amp; Goals</text>
  <text x="70" y="375" fill="#cbd5e1" font-size="11">• Bead Marble &amp; Dial Color Themes</text>
  <text x="70" y="393" fill="#10b981" font-size="11" font-weight="600">✅ NEVER TRANSMITTED: Stays on phone</text>

  <line x1="60" y1="410" x2="380" y2="410" stroke="#1e293b" stroke-width="1.5"/>

  <text x="70" y="435" fill="#fca5a5" font-size="12" font-weight="700">File: "pr_prefs" (Prayer Location)</text>
  <text x="70" y="453" fill="#cbd5e1" font-size="11">• Exact Latitude: "pr_lat" (Double)</text>
  <text x="70" y="469" fill="#cbd5e1" font-size="11">• Exact Longitude: "pr_lon" (Double)</text>
  <text x="70" y="487" fill="#f59e0b" font-size="11" font-weight="600">⚠️ Sent to Aladhan API in cleartext parameters</text>

  <line x1="60" y1="505" x2="380" y2="505" stroke="#1e293b" stroke-width="1.5"/>

  <text x="70" y="530" fill="#c7d2fe" font-size="12" font-weight="700">File: "dt_app_prefs" (Device Identity)</text>
  <text x="70" y="548" fill="#cbd5e1" font-size="11">• Install ID: UUID.randomUUID()</text>
  <text x="70" y="564" fill="#cbd5e1" font-size="11">• 18+ Age Gate Flag: Boolean</text>
  <text x="70" y="580" fill="#cbd5e1" font-size="11">• Ad Consent Choice: Boolean</text>

  <!-- Column 2: Cloudflare D1 SQL -->
  <rect x="430" y="140" width="340" height="600" rx="14" fill="#0f172a" stroke="#1e293b" stroke-width="2" filter="url(#shadow2)"/>
  <rect x="430" y="140" width="340" height="40" rx="14" fill="#0c4a6e"/>
  <text x="450" y="166" fill="#38bdf8" font-size="15" font-weight="700">☁️ Cloudflare D1 SQL Database</text>
  <text x="450" y="200" fill="#fca5a5" font-size="12" font-weight="700">Table: otp_records</text>
  <text x="450" y="218" fill="#fca5a5" font-size="11">• email TEXT PRIMARY KEY <tspan fill="#ef4444">(PLAIN TEXT!)</tspan></text>
  <text x="450" y="234" fill="#c7d2fe" font-size="11">• otp_hash TEXT (SHA-256)</text>
  <text x="450" y="250" fill="#cbd5e1" font-size="11">• attempts INTEGER (Max 5)</text>
  <text x="450" y="266" fill="#cbd5e1" font-size="11">• expires_at INTEGER (10 min TTL in code)</text>
  <text x="450" y="284" fill="#ef4444" font-size="10" font-weight="600">❌ NO AUTO-PURGE: Stays in D1 until row overwrite</text>

  <line x1="440" y1="300" x2="760" y2="300" stroke="#1e293b" stroke-width="1.5"/>

  <text x="450" y="325" fill="#fca5a5" font-size="12" font-weight="700">Table: rate_limits</text>
  <text x="450" y="343" fill="#fca5a5" font-size="11">• key TEXT PRIMARY KEY <tspan fill="#ef4444">(email:plain_user_email)</tspan></text>
  <text x="450" y="359" fill="#c7d2fe" font-size="11">• key: ip:HMAC_SHA256 (IP is hashed)</text>
  <text x="450" y="375" fill="#cbd5e1" font-size="11">• count INTEGER, reset_at INTEGER</text>

  <line x1="440" y1="410" x2="760" y2="410" stroke="#1e293b" stroke-width="1.5"/>

  <text x="450" y="435" fill="#c7d2fe" font-size="12" font-weight="700">Table: user_submit_cooldowns</text>
  <text x="450" y="453" fill="#cbd5e1" font-size="11">• uid TEXT PRIMARY KEY</text>
  <text x="450" y="469" fill="#cbd5e1" font-size="11">• last_submitted_at INTEGER</text>
  <text x="450" y="487" fill="#94a3b8" font-size="10">Cooldown: 60 seconds per count submission</text>

  <line x1="440" y1="505" x2="760" y2="505" stroke="#1e293b" stroke-width="1.5"/>

  <text x="450" y="530" fill="#cbd5e1" font-size="12" font-weight="700">Table: analytics_metrics</text>
  <text x="450" y="548" fill="#cbd5e1" font-size="11">• metric_name TEXT, date TEXT</text>
  <text x="450" y="564" fill="#cbd5e1" font-size="11">• count INTEGER</text>
  <text x="450" y="582" fill="#10b981" font-size="11">Aggregated counts only; zero user PII</text>

  <!-- Column 3: Cloud Firestore -->
  <rect x="810" y="140" width="340" height="600" rx="14" fill="#0f172a" stroke="#1e293b" stroke-width="2" filter="url(#shadow2)"/>
  <rect x="810" y="140" width="340" height="40" rx="14" fill="#581c87"/>
  <text x="830" y="166" fill="#c084fc" font-size="15" font-weight="700">🔥 Cloud Firestore Database</text>
  <text x="830" y="200" fill="#fca5a5" font-size="12" font-weight="700">Doc: campaign_contestants/{id}/entries/{uid}</text>
  <text x="830" y="218" fill="#fca5a5" font-size="11">• phone: <tspan fill="#ef4444">Plain Text E.164 (+91...)</tspan></text>
  <text x="830" y="234" fill="#fca5a5" font-size="11">• normalizedEmail: <tspan fill="#ef4444">Plain Text Email</tspan></text>
  <text x="830" y="250" fill="#cbd5e1" font-size="11">• name: Plain Text Full Name</text>
  <text x="830" y="266" fill="#cbd5e1" font-size="11">• city, state, country: Plain Text</text>
  <text x="830" y="284" fill="#ef4444" font-size="10" font-weight="600">❌ NO AUTOMATED PURGE: Stays until admin Purge API</text>

  <line x1="820" y1="300" x2="1140" y2="300" stroke="#1e293b" stroke-width="1.5"/>

  <text x="830" y="325" fill="#c7d2fe" font-size="12" font-weight="700">Collection: contest_registry/{campaignId}</text>
  <text x="830" y="343" fill="#c7d2fe" font-size="11">• phoneKey: HMAC_SHA256(phone, SECRET)</text>
  <text x="830" y="359" fill="#c7d2fe" font-size="11">• emailKey: HMAC_SHA256(email, SECRET)</text>
  <text x="830" y="375" fill="#c7d2fe" font-size="11">• devKey: HMAC_SHA256(deviceId, SECRET)</text>
  <text x="830" y="393" fill="#10b981" font-size="11">Used for 1-entry-per-person deduplication</text>

  <line x1="820" y1="410" x2="1140" y2="410" stroke="#1e293b" stroke-width="1.5"/>

  <text x="830" y="435" fill="#cbd5e1" font-size="12" font-weight="700">Public Leaderboard Output</text>
  <text x="830" y="453" fill="#cbd5e1" font-size="11">• displayName (User name or 'Anonymous')</text>
  <text x="830" y="469" fill="#cbd5e1" font-size="11">• city (e.g. 'Mumbai', 'London')</text>
  <text x="830" y="485" fill="#cbd5e1" font-size="11">• count (Submitted Dhikr integer)</text>
  <text x="830" y="501" fill="#c7d2fe" font-size="11">• publicId: HMAC(uid)[0..8] (Masked)</text>
  <text x="830" y="520" fill="#10b981" font-size="11" font-weight="600">✅ Phone and Email are NEVER in Leaderboard</text>
</svg>
"""

with open(os.path.join(DIAGRAMS_DIR, "02_backend_storage_pii_map.svg"), "w", encoding="utf-8") as f:
    f.write(svg_pii_map)

print("Saved 02_backend_storage_pii_map.svg")

# 3. Permissions & Boundaries SVG
svg_permissions = """<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1200 750" width="100%" height="100%" style="background:#0b131a; font-family:'Segoe UI',system-ui,-apple-system,sans-serif;">
  <text x="600" y="45" text-anchor="middle" fill="#f8fafc" font-size="24" font-weight="700">Digital Tasbih - Android Permissions &amp; Data Boundary Matrix</text>
  <text x="600" y="70" text-anchor="middle" fill="#94a3b8" font-size="13">Manifest Analysis: Hardware Triggers, Optionality, Data Leak Risks, and Fallback Degradation</text>

  <!-- Table Header -->
  <rect x="50" y="100" width="1100" height="40" rx="8" fill="#1e293b"/>
  <text x="70" y="125" fill="#f8fafc" font-size="12" font-weight="700">ANDROID PERMISSION</text>
  <text x="310" y="125" fill="#f8fafc" font-size="12" font-weight="700">TRIGGER IN CODE</text>
  <text x="500" y="125" fill="#f8fafc" font-size="12" font-weight="700">OPTIONAL?</text>
  <text x="610" y="125" fill="#f8fafc" font-size="12" font-weight="700">BEHAVIOUR IF DENIED</text>
  <text x="880" y="125" fill="#f8fafc" font-size="12" font-weight="700">PRIVACY / EXPOSURE RISK</text>

  <!-- Row 1: ACCESS_FINE_LOCATION -->
  <rect x="50" y="150" width="1100" height="60" rx="6" fill="#0f172a" stroke="#1e293b"/>
  <text x="70" y="175" fill="#38bdf8" font-size="12" font-weight="700">ACCESS_FINE_LOCATION</text>
  <text x="70" y="195" fill="#94a3b8" font-size="10">android.permission.ACCESS_FINE_LOCATION</text>
  <text x="310" y="175" fill="#cbd5e1" font-size="12">Prayer times &amp; Qibla compass</text>
  <text x="310" y="195" fill="#94a3b8" font-size="10">PrayerRepository.kt#L590</text>
  <rect x="500" y="165" width="60" height="24" rx="4" fill="#064e3b"/>
  <text x="512" y="181" fill="#34d399" font-size="11" font-weight="700">YES</text>
  <text x="610" y="175" fill="#cbd5e1" font-size="12">Falls back to manual city selection</text>
  <text x="610" y="195" fill="#94a3b8" font-size="10">User chooses city from local list</text>
  <text x="880" y="175" fill="#f59e0b" font-size="11" font-weight="600">HIGH: Exact coordinates sent to</text>
  <text x="880" y="195" fill="#f59e0b" font-size="10">Aladhan API (api.aladhan.com)</text>

  <!-- Row 2: ACCESS_COARSE_LOCATION -->
  <rect x="50" y="220" width="1100" height="60" rx="6" fill="#0f172a" stroke="#1e293b"/>
  <text x="70" y="245" fill="#38bdf8" font-size="12" font-weight="700">ACCESS_COARSE_LOCATION</text>
  <text x="70" y="265" fill="#94a3b8" font-size="10">android.permission.ACCESS_COARSE_LOCATION</text>
  <text x="310" y="245" fill="#cbd5e1" font-size="12">Cell / WiFi approximate prayer times</text>
  <text x="310" y="265" fill="#94a3b8" font-size="10">Requested alongside fine location</text>
  <rect x="500" y="235" width="60" height="24" rx="4" fill="#064e3b"/>
  <text x="512" y="251" fill="#34d399" font-size="11" font-weight="700">YES</text>
  <text x="610" y="245" fill="#cbd5e1" font-size="12">Manual city selection dropdown</text>
  <text x="610" y="265" fill="#94a3b8" font-size="10">No GPS lock needed</text>
  <text x="880" y="245" fill="#cbd5e1" font-size="11">MEDIUM: City-level accuracy</text>
  <text x="880" y="265" fill="#94a3b8" font-size="10">AdMob may use for geo-targeting</text>

  <!-- Row 3: POST_NOTIFICATIONS -->
  <rect x="50" y="290" width="1100" height="60" rx="6" fill="#0f172a" stroke="#1e293b"/>
  <text x="70" y="315" fill="#38bdf8" font-size="12" font-weight="700">POST_NOTIFICATIONS</text>
  <text x="70" y="335" fill="#94a3b8" font-size="10">android.permission.POST_NOTIFICATIONS</text>
  <text x="310" y="315" fill="#cbd5e1" font-size="12">Adhan prayer alarms &amp; Daily reminders</text>
  <text x="310" y="335" fill="#94a3b8" font-size="10">Prompted on Android 13+ (API 33)</text>
  <rect x="500" y="305" width="60" height="24" rx="4" fill="#064e3b"/>
  <text x="512" y="321" fill="#34d399" font-size="11" font-weight="700">YES</text>
  <text x="610" y="315" fill="#cbd5e1" font-size="12">Alarms trigger silently without banner</text>
  <text x="610" y="335" fill="#94a3b8" font-size="10">User misses prayer times</text>
  <text x="880" y="315" fill="#10b981" font-size="11" font-weight="600">ZERO: Local system notifications</text>
  <text x="880" y="335" fill="#94a3b8" font-size="10">No remote push servers (No FCM)</text>

  <!-- Row 4: SCHEDULE_EXACT_ALARM -->
  <rect x="50" y="360" width="1100" height="60" rx="6" fill="#0f172a" stroke="#1e293b"/>
  <text x="70" y="385" fill="#38bdf8" font-size="12" font-weight="700">SCHEDULE_EXACT_ALARM</text>
  <text x="70" y="405" fill="#94a3b8" font-size="10">android.permission.SCHEDULE_EXACT_ALARM</text>
  <text x="310" y="385" fill="#cbd5e1" font-size="12">Exact-minute Adhan playback</text>
  <text x="310" y="405" fill="#94a3b8" font-size="10">PrayerAlarmReceiver.kt</text>
  <rect x="500" y="375" width="60" height="24" rx="4" fill="#064e3b"/>
  <text x="512" y="391" fill="#34d399" font-size="11" font-weight="700">YES</text>
  <text x="610" y="385" fill="#cbd5e1" font-size="12">Adhan may drift by 5-15 mins in Doze</text>
  <text x="610" y="405" fill="#94a3b8" font-size="10">Android OS batches background alarms</text>
  <text x="880" y="385" fill="#10b981" font-size="11" font-weight="600">ZERO: Pure device scheduling</text>
  <text x="880" y="405" fill="#94a3b8" font-size="10">No data transmitted</text>

  <!-- Row 5: VIBRATE -->
  <rect x="50" y="430" width="1100" height="60" rx="6" fill="#0f172a" stroke="#1e293b"/>
  <text x="70" y="455" fill="#38bdf8" font-size="12" font-weight="700">VIBRATE</text>
  <text x="70" y="475" fill="#94a3b8" font-size="10">android.permission.VIBRATE</text>
  <text x="310" y="455" fill="#cbd5e1" font-size="12">Haptic pulse on bead slide / dial click</text>
  <text x="310" y="475" fill="#94a3b8" font-size="10">TasbihScreen.kt (FROZEN)</text>
  <rect x="500" y="445" width="60" height="24" rx="4" fill="#064e3b"/>
  <text x="512" y="461" fill="#34d399" font-size="11" font-weight="700">YES</text>
  <text x="610" y="455" fill="#cbd5e1" font-size="12">Beads slide without vibration</text>
  <text x="610" y="475" fill="#94a3b8" font-size="10">Toggled in Tasbih settings</text>
  <text x="880" y="455" fill="#10b981" font-size="11" font-weight="600">ZERO: Hardware motor only</text>
  <text x="880" y="475" fill="#94a3b8" font-size="10">No data processed</text>

  <!-- Row 6: INTERNET & ACCESS_NETWORK_STATE -->
  <rect x="50" y="500" width="1100" height="60" rx="6" fill="#0f172a" stroke="#1e293b"/>
  <text x="70" y="525" fill="#38bdf8" font-size="12" font-weight="700">INTERNET / NETWORK STATE</text>
  <text x="70" y="545" fill="#94a3b8" font-size="10">android.permission.INTERNET</text>
  <text x="310" y="525" fill="#cbd5e1" font-size="12">Syncing campaigns, OTP, Audio stream, Ads</text>
  <text x="310" y="545" fill="#94a3b8" font-size="10">Standard normal permission</text>
  <rect x="500" y="515" width="60" height="24" rx="4" fill="#450a0a"/>
  <text x="516" y="531" fill="#f87171" font-size="11" font-weight="700">NO</text>
  <text x="610" y="525" fill="#cbd5e1" font-size="12">Operating system grants automatically</text>
  <text x="610" y="545" fill="#94a3b8" font-size="10">Airplane mode runs local Tasbih/Quran</text>
  <text x="880" y="525" fill="#f59e0b" font-size="11" font-weight="600">MEDIUM: IP address exposed to</text>
  <text x="880" y="545" fill="#f59e0b" font-size="10">Cloudflare edge &amp; Google servers</text>

  <!-- Row 7: FOREGROUND_SERVICE_MEDIA_PLAYBACK -->
  <rect x="50" y="570" width="1100" height="60" rx="6" fill="#0f172a" stroke="#1e293b"/>
  <text x="70" y="595" fill="#38bdf8" font-size="12" font-weight="700">FOREGROUND_SERVICE (MEDIA)</text>
  <text x="70" y="615" fill="#94a3b8" font-size="10">android.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK</text>
  <text x="310" y="595" fill="#cbd5e1" font-size="12">Continuous Quran recitation playback</text>
  <text x="310" y="615" fill="#94a3b8" font-size="10">AudioPlayerService.kt</text>
  <rect x="500" y="585" width="60" height="24" rx="4" fill="#064e3b"/>
  <text x="512" y="601" fill="#34d399" font-size="11" font-weight="700">YES</text>
  <text x="610" y="595" fill="#cbd5e1" font-size="12">Audio halts when app goes to background</text>
  <text x="610" y="615" fill="#94a3b8" font-size="10">Screen must stay active to listen</text>
  <text x="880" y="595" fill="#10b981" font-size="11" font-weight="600">ZERO: Media session controller</text>
  <text x="880" y="615" fill="#94a3b8" font-size="10">No personal data collected</text>

  <!-- Bottom Summary Note -->
  <rect x="50" y="650" width="1100" height="70" rx="8" fill="#132328" stroke="#10b981"/>
  <text x="70" y="675" fill="#34d399" font-size="13" font-weight="700">🛡️ Zero Dangerous Hardware Probes Found in Manifest</text>
  <text x="70" y="695" fill="#cbd5e1" font-size="11">Audit Confirms: CAMERA, RECORD_AUDIO, READ_CONTACTS, READ_CALL_LOG, READ_SMS, READ_MEDIA_IMAGES are <tspan fill="#34d399" font-weight="700">COMPLETELY ABSENT</tspan>.</text>
  <text x="70" y="710" fill="#94a3b8" font-size="10">The app cannot access user gallery, address book, microphone, or text messages under any circumstances.</text>
</svg>
"""

with open(os.path.join(DIAGRAMS_DIR, "03_permissions_and_boundaries.svg"), "w", encoding="utf-8") as f:
    f.write(svg_permissions)

print("Saved 03_permissions_and_boundaries.svg")


# 4. Consent & Age Gate Flow SVG
svg_consent_gate = """<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1200 780" width="100%" height="100%" style="background:#0b131a; font-family:'Segoe UI',system-ui,-apple-system,sans-serif;">
  <defs>
    <marker id="arrowG" viewBox="0 0 10 10" refX="6" refY="5" markerWidth="6" markerHeight="6" orient="auto-start-reverse">
      <path d="M 0 1 L 8 5 L 0 9 z" fill="#10b981"/>
    </marker>
    <marker id="arrowR" viewBox="0 0 10 10" refX="6" refY="5" markerWidth="6" markerHeight="6" orient="auto-start-reverse">
      <path d="M 0 1 L 8 5 L 0 9 z" fill="#ef4444"/>
    </marker>
    <marker id="arrowB" viewBox="0 0 10 10" refX="6" refY="5" markerWidth="6" markerHeight="6" orient="auto-start-reverse">
      <path d="M 0 1 L 8 5 L 0 9 z" fill="#3b82f6"/>
    </marker>
  </defs>

  <text x="600" y="45" text-anchor="middle" fill="#f8fafc" font-size="24" font-weight="700">Digital Tasbih - Consent, DPDP Age Gate &amp; Deletion Architecture</text>
  <text x="600" y="70" text-anchor="middle" fill="#94a3b8" font-size="13">Decision Tree: Child Protection (18+ Gate), Google UMP Ad Consent, and Current vs Target Deletion Flow</text>

  <!-- Left Branch: DPDP Act 18+ Age Gate -->
  <rect x="50" y="110" width="530" height="630" rx="14" fill="#0f172a" stroke="#1e293b" stroke-width="2"/>
  <rect x="50" y="110" width="530" height="40" rx="14" fill="#132e24"/>
  <text x="70" y="136" fill="#34d399" font-size="15" font-weight="700">⚖️ DPDP Act 2023 Compliance: Age Gate (18+)</text>

  <!-- Step 1: User enters Community -->
  <rect x="80" y="170" width="470" height="60" rx="8" fill="#1e293b"/>
  <text x="100" y="195" fill="#f8fafc" font-size="12" font-weight="600">User taps "Live Dhikr Rooms" or "Umrah Contest"</text>
  <text x="100" y="215" fill="#94a3b8" font-size="11">GroupCampaignRepository.kt#L308-L325 checks dt_user_is_18_plus</text>

  <!-- Decision Diamond -->
  <polygon points="315,260 415,310 315,360 215,310" fill="#1c2438" stroke="#38bdf8" stroke-width="1.5"/>
  <text x="315" y="305" text-anchor="middle" fill="#f8fafc" font-size="12" font-weight="700">Is User 18+?</text>
  <text x="315" y="322" text-anchor="middle" fill="#94a3b8" font-size="10">(Self-Declaration)</text>

  <!-- YES Path -->
  <path d="M 415 310 L 460 310 L 460 400" fill="none" stroke="#10b981" stroke-width="2" marker-end="url(#arrowG)"/>
  <text x="430" y="300" fill="#34d399" font-size="11" font-weight="700">YES</text>

  <rect x="350" y="400" width="200" height="120" rx="8" fill="#064e3b" stroke="#10b981"/>
  <text x="365" y="425" fill="#6ee7b7" font-size="12" font-weight="700">✅ Full Community Access</text>
  <text x="365" y="445" fill="#cbd5e1" font-size="11">• Join Live Rooms</text>
  <text x="365" y="465" fill="#cbd5e1" font-size="11">• Submit Dhikr counts</text>
  <text x="365" y="485" fill="#cbd5e1" font-size="11">• Participate in Umrah Contest</text>
  <text x="365" y="505" fill="#cbd5e1" font-size="11">• Appears on Leaderboards</text>

  <!-- NO Path -->
  <path d="M 215 310 L 160 310 L 160 400" fill="none" stroke="#ef4444" stroke-width="2" marker-end="url(#arrowR)"/>
  <text x="175" y="300" fill="#f87171" font-size="11" font-weight="700">NO</text>

  <rect x="80" y="400" width="220" height="150" rx="8" fill="#450a0a" stroke="#ef4444"/>
  <text x="95" y="425" fill="#fca5a5" font-size="12" font-weight="700">⛔ Minor Restricted View</text>
  <text x="95" y="445" fill="#cbd5e1" font-size="11">• Community features BLOCKED</text>
  <text x="95" y="465" fill="#cbd5e1" font-size="11">• Contest participation DENIED</text>
  <text x="95" y="485" fill="#cbd5e1" font-size="11">• Zero personal data uploaded</text>
  <text x="95" y="505" fill="#34d399" font-size="11" font-weight="600">✅ Offline Tasbih &amp; Quran OPEN</text>
  <text x="95" y="525" fill="#94a3b8" font-size="10">User can toggle back anytime</text>

  <!-- Age Gate Evaluation Note -->
  <rect x="80" y="575" width="470" height="140" rx="8" fill="#1e293b"/>
  <text x="95" y="600" fill="#fbbf24" font-size="12" font-weight="700">⚠️ Legal Vulnerability &amp; Counsel Guidance</text>
  <text x="95" y="620" fill="#cbd5e1" font-size="11">• Current implementation relies on self-declaration (checkbox/modal).</text>
  <text x="95" y="640" fill="#cbd5e1" font-size="11">• Under DPDP Act Sec 9, Verifiable Parental Consent will be required by</text>
  <text x="95" y="660" fill="#cbd5e1" font-size="11">  May 2027 if minors are allowed in community features.</text>
  <text x="95" y="680" fill="#34d399" font-size="11" font-weight="600">RECOMMENDATION: Keep community features strictly 18+ only.</text>

  <!-- Right Branch: Account Deletion & Ad Consent -->
  <rect x="620" y="110" width="530" height="630" rx="14" fill="#0f172a" stroke="#1e293b" stroke-width="2"/>
  <rect x="620" y="110" width="530" height="40" rx="14" fill="#1e1b4b"/>
  <text x="640" y="136" fill="#818cf8" font-size="15" font-weight="700">🗑️ Account Deletion &amp; Ad Consent Reality</text>

  <!-- In-App Deletion Analysis -->
  <rect x="640" y="170" width="490" height="175" rx="8" fill="#1c1917" stroke="#ef4444" stroke-width="1.5"/>
  <text x="655" y="195" fill="#f87171" font-size="13" font-weight="700">❌ In-App "Delete My Data" (SettingsScreen.kt#L1151)</text>
  <text x="655" y="220" fill="#fca5a5" font-size="11">• Actual Code: // Simulate legal request logging -> isSubmitted = true</text>
  <text x="655" y="240" fill="#cbd5e1" font-size="11">• Network Call: NONE (Zero API requests sent)</text>
  <text x="655" y="260" fill="#cbd5e1" font-size="11">• Cloudflare D1 Records: NOT DELETED (Email stays in otp_records)</text>
  <text x="655" y="280" fill="#cbd5e1" font-size="11">• Firestore Contest Data: NOT DELETED (Phone/Name stays in entries)</text>
  <text x="655" y="305" fill="#f87171" font-size="11" font-weight="700">⚠️ VIOLATION: Violates Google Play Account Deletion Requirement!</text>

  <!-- Compliant Deletion Architecture (What is needed) -->
  <rect x="640" y="365" width="490" height="185" rx="8" fill="#132328" stroke="#10b981" stroke-width="1.5"/>
  <text x="655" y="390" fill="#34d399" font-size="13" font-weight="700">🛡️ Mandatory Remediation for Google Play &amp; DPDP</text>
  <text x="655" y="415" fill="#cbd5e1" font-size="11">1. <tspan font-weight="600" fill="#f8fafc">Public Web Deletion URL:</tspan> Provide https://imaanix.in/delete-account</text>
  <text x="655" y="435" fill="#cbd5e1" font-size="11">   (Play Console mandates web URL where users can request account erasure).</text>
  <text x="655" y="460" fill="#cbd5e1" font-size="11">2. <tspan font-weight="600" fill="#f8fafc">Backend Deletion Endpoint:</tspan> Implement DELETE /api/user/me</text>
  <text x="655" y="480" fill="#cbd5e1" font-size="11">   • Delete Firebase Auth User</text>
  <text x="655" y="500" fill="#cbd5e1" font-size="11">   • Erase Cloudflare D1 otp_records &amp; rate_limits rows</text>
  <text x="655" y="520" fill="#cbd5e1" font-size="11">   • Erase Firestore campaign_contestants entry &amp; phone number</text>

  <!-- AdMob Consent Dialog -->
  <rect x="640" y="570" width="490" height="145" rx="8" fill="#1e293b"/>
  <text x="655" y="595" fill="#60a5fa" font-size="12" font-weight="700">📢 Google AdMob &amp; UMP Consent Choices</text>
  <text x="655" y="618" fill="#cbd5e1" font-size="11">• EU/UK: Google UMP SDK shows interactive consent dialog automatically.</text>
  <text x="655" y="638" fill="#cbd5e1" font-size="11">• India &amp; Rest of World: AdChoicesConsentModal allows switching between</text>
  <text x="655" y="658" fill="#cbd5e1" font-size="11">  Personalized vs Non-Personalized Ads (Stored locally in AdManager.kt).</text>
  <text x="655" y="682" fill="#94a3b8" font-size="10">Note: Even non-personalized ads transmit mobile identifiers for frequency capping.</text>
</svg>
"""

with open(os.path.join(DIAGRAMS_DIR, "04_consent_and_age_gate_flow.svg"), "w", encoding="utf-8") as f:
    f.write(svg_consent_gate)

print("Saved 04_consent_and_age_gate_flow.svg")
