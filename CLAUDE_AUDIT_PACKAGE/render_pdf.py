import os
import subprocess

REPORT_DIR = os.path.abspath("PRIVACY_DATA_AUDIT_REPORT")
DOCS_DIR = os.path.join(REPORT_DIR, "docs")
DIAGRAMS_DIR = os.path.join(REPORT_DIR, "diagrams")
PDF_DIR = os.path.join(REPORT_DIR, "pdf")

# Read SVG contents to embed directly inline for 100% self-contained PDF rendering
with open(os.path.join(DIAGRAMS_DIR, "01_system_architecture_data_flow.svg"), "r", encoding="utf-8") as f:
    svg_01 = f.read()

with open(os.path.join(DIAGRAMS_DIR, "02_backend_storage_pii_map.svg"), "r", encoding="utf-8") as f:
    svg_02 = f.read()

with open(os.path.join(DIAGRAMS_DIR, "03_permissions_and_boundaries.svg"), "r", encoding="utf-8") as f:
    svg_03 = f.read()

with open(os.path.join(DIAGRAMS_DIR, "04_consent_and_age_gate_flow.svg"), "r", encoding="utf-8") as f:
    svg_04 = f.read()

html_content = f"""<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Digital Tasbih - Privacy & Data-Flow Audit Report</title>
    <style>
        @page {{
            size: A4 portrait;
            margin: 15mm 12mm 15mm 12mm;
            @bottom-right {{
                content: "Page " counter(page);
                font-size: 8pt;
                color: #64748b;
            }}
        }}

        :root {{
            --bg-color: #ffffff;
            --text-color: #0f172a;
            --text-muted: #475569;
            --primary-emerald: #059669;
            --primary-dark: #064e3b;
            --gold: #b45309;
            --border: #cbd5e1;
            --card-bg: #f8fafc;
            --code-bg: #f1f5f9;
        }}

        body {{
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
            color: var(--text-color);
            background: var(--bg-color);
            line-height: 1.5;
            font-size: 9.5pt;
            margin: 0;
            padding: 0;
        }}

        .page-break {{
            page-break-before: always;
            break-before: page;
        }}

        .cover-page {{
            text-align: center;
            padding: 120px 20px 40px 20px;
            page-break-after: always;
            break-after: page;
        }}

        .cover-badge {{
            display: inline-block;
            background: #ecfdf5;
            color: #047857;
            border: 1px solid #10b981;
            font-size: 11pt;
            font-weight: 700;
            padding: 6px 18px;
            border-radius: 30px;
            text-transform: uppercase;
            letter-spacing: 1px;
            margin-bottom: 24px;
        }}

        .cover-title {{
            font-size: 26pt;
            font-weight: 800;
            color: #064e3b;
            margin: 0 0 12px 0;
            line-height: 1.2;
        }}

        .cover-subtitle {{
            font-size: 13pt;
            color: #334155;
            max-width: 650px;
            margin: 0 auto 36px auto;
            line-height: 1.5;
        }}

        .meta-box {{
            background: var(--card-bg);
            border: 1px solid var(--border);
            border-radius: 12px;
            padding: 20px 30px;
            max-width: 500px;
            margin: 0 auto;
            text-align: left;
            font-size: 10pt;
        }}

        .meta-row {{
            display: flex;
            justify-content: space-between;
            padding: 6px 0;
            border-bottom: 1px solid #e2e8f0;
        }}
        .meta-row:last-child {{
            border-bottom: none;
        }}
        .meta-label {{
            font-weight: 600;
            color: #64748b;
        }}
        .meta-val {{
            font-weight: 700;
            color: #0f172a;
        }}

        h1, h2, h3, h4 {{
            color: #064e3b;
            font-weight: 700;
            margin-top: 18px;
            margin-bottom: 8px;
        }}

        h2 {{
            font-size: 14pt;
            border-bottom: 1.5px solid #059669;
            padding-bottom: 4px;
            margin-top: 24px;
        }}

        h3 {{
            font-size: 11pt;
            margin-top: 14px;
            color: #047857;
        }}

        p, ul, ol {{
            margin-top: 4px;
            margin-bottom: 8px;
            color: #334155;
        }}

        li {{
            margin-bottom: 3px;
        }}

        table {{
            width: 100%;
            border-collapse: collapse;
            font-size: 7.5pt;
            margin: 10px 0 14px 0;
            page-break-inside: auto;
        }}

        tr {{
            page-break-inside: avoid;
            page-break-after: auto;
        }}

        th {{
            background-color: #064e3b;
            color: #ffffff;
            font-weight: 700;
            text-align: left;
            padding: 6px 5px;
            border: 1px solid #047857;
            font-size: 7.5pt;
        }}

        td {{
            padding: 5px 5px;
            border: 1px solid #cbd5e1;
            vertical-align: top;
            line-height: 1.35;
        }}

        tbody tr:nth-child(even) {{
            background-color: #f8fafc;
        }}

        .status-confirmed {{
            background: #dcfce7;
            color: #15803d;
            font-weight: 700;
            padding: 2px 4px;
            border-radius: 4px;
            display: inline-block;
            font-size: 6.5pt;
        }}

        .status-false {{
            background: #fee2e2;
            color: #b91c1c;
            font-weight: 700;
            padding: 2px 4px;
            border-radius: 4px;
            display: inline-block;
            font-size: 6.5pt;
        }}

        .status-alert {{
            background: #fef3c7;
            color: #92400e;
            font-weight: 700;
            padding: 2px 4px;
            border-radius: 4px;
            display: inline-block;
            font-size: 6.5pt;
        }}

        .badge-red {{
            color: #dc2626;
            font-weight: 700;
        }}

        .diagram-container {{
            background: #0b131a;
            border: 1px solid #1e293b;
            border-radius: 8px;
            padding: 10px;
            margin: 12px 0 16px 0;
            text-align: center;
            page-break-inside: avoid;
        }}

        .diagram-caption {{
            font-size: 8pt;
            font-weight: 600;
            color: #64748b;
            margin-top: 6px;
            text-align: center;
        }}

        code {{
            font-family: Consolas, Monaco, monospace;
            font-size: 7.5pt;
            background: var(--code-bg);
            padding: 1px 3px;
            border-radius: 3px;
            color: #0f172a;
        }}

        .callout {{
            background: #f0fdf4;
            border-left: 4px solid #10b981;
            padding: 8px 12px;
            margin: 10px 0;
            border-radius: 0 6px 6px 0;
            font-size: 8.5pt;
        }}

        .callout-warning {{
            background: #fffbeb;
            border-left: 4px solid #f59e0b;
            padding: 8px 12px;
            margin: 10px 0;
            border-radius: 0 6px 6px 0;
            font-size: 8.5pt;
        }}

        .callout-danger {{
            background: #fef2f2;
            border-left: 4px solid #ef4444;
            padding: 8px 12px;
            margin: 10px 0;
            border-radius: 0 6px 6px 0;
            font-size: 8.5pt;
        }}
    </style>
</head>
<body>

    <!-- COVER PAGE -->
    <div class="cover-page">
        <div class="cover-badge">Evidence-Backed Privacy &amp; Data-Flow Audit</div>
        <div class="cover-title">Digital Tasbih</div>
        <div style="font-size: 14pt; font-weight: 700; color: #b45309; margin-bottom: 12px;">Android Client (com.tasbih.digitaltasbih) &amp; Cloudflare Edge Architecture</div>
        <div class="cover-subtitle">
            A comprehensive, line-by-line verification of personal data flows, permissions, third-party SDKs, and regulatory compliance (Google Play Data Safety, GDPR, COPPA &amp; DPDP Act 2023).
        </div>

        <div class="meta-box">
            <div class="meta-row"><span class="meta-label">App Identifier:</span> <span class="meta-val">com.tasbih.digitaltasbih</span></div>
            <div class="meta-row"><span class="meta-label">Target Architecture:</span> <span class="meta-val">Android, Cloudflare Workers/D1, GCP Firestore</span></div>
            <div class="meta-row"><span class="meta-label">Audit Methodology:</span> <span class="meta-val">Strict READ-ONLY Static Code Verification</span></div>
            <div class="meta-row"><span class="meta-label">Tasbih Module Status:</span> <span class="meta-val" style="color: #047857;">FROZEN &amp; UNTOUCHED (Permanent Rule)</span></div>
            <div class="meta-row"><span class="meta-label">Audit Date:</span> <span class="meta-val">November 2026</span></div>
            <div class="meta-row"><span class="meta-label">Version:</span> <span class="meta-val">Release 1.0</span></div>
        </div>
    </div>

    <!-- SECTION 1 -->
    <h2>1. Executive Summary &amp; Architecture Topology</h2>
    <p>
        <strong>Digital Tasbih by Imaanix</strong> implements a distinct dual-sphere privacy architecture:
    </p>
    <ul>
        <li><strong>100% Offline Local Sphere:</strong> The primary core Islamic utility—including the realistic Bézier beads tasbih (<code>TasbihScreen.kt</code>), digital counters, lifetime badge progression, daily statistics, offline Quran text reading, and daily Athkar—operates entirely inside Android's private app sandbox. No account is required, and zero user counts or prayer markers are transmitted over the internet.</li>
        <li><strong>Cloud &amp; Community Sphere:</strong> Optional features—specifically Live Dhikr Rooms, the Mega Umrah Contest, and prayer times geolocation—connect to external cloud endpoints:
            Cloudflare Workers (<code>auth-worker.js</code>, <code>campaigns-worker.js</code>), Cloudflare D1 SQL, Google Cloud Firestore (<code>digital-tasbih-campaigns-e43ee</code>), and the Aladhan Prayer API.
        </li>
    </ul>

    <div class="callout-danger">
        <strong>CRITICAL AUDIT NOTICE (Tasbih Lock Rule):</strong> In strict compliance with project safety directives, the Tasbih module (<code>TasbihScreen.kt</code>, Bézier canvas math, dial progression, lifetime badges) was inspected in READ-ONLY mode and remains 100% untouched.
    </div>

    <div class="diagram-container">
        {svg_01}
        <div class="diagram-caption">Figure 1.1: End-to-End System Architecture, Client Data Boundaries, and External Processors</div>
    </div>

    <!-- SECTION 2 -->
    <div class="page-break"></div>
    <h2>2. Deliverable 1: Comprehensive Data Inventory Table</h2>
    <p>Every field processed across the Android client, Cloudflare edge, and Firestore databases:</p>

    <table>
        <thead>
            <tr>
                <th style="width: 14%;">Data Item</th>
                <th style="width: 11%;">Feature / Trigger</th>
                <th style="width: 16%;">Stored Where</th>
                <th style="width: 9%;">Format</th>
                <th style="width: 8%;">Visibility</th>
                <th style="width: 16%;">Purpose</th>
                <th style="width: 16%;">Evidence (File &amp; Line)</th>
                <th style="width: 10%;">Status</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td><strong>Email Address</strong> (OTP Login)</td>
                <td>Email OTP sign-in / verify</td>
                <td>Cloudflare D1 (<code>otp_records.email</code>, <code>rate_limits.key</code>)</td>
                <td><span class="badge-red">Plain Text</span></td>
                <td>Private</td>
                <td>Authentication, sending 6-digit code</td>
                <td><code>schema.sql#L10-L17</code><br><code>auth-worker.js#L324</code></td>
                <td><span class="status-confirmed">CONFIRMED</span></td>
            </tr>
            <tr>
                <td><strong>Email Address</strong> (Contest)</td>
                <td>Registering for Umrah Contest</td>
                <td>Firestore (<code>campaign_contestants/.../entries/{'{'}uid{'}'}.normalizedEmail</code>)</td>
                <td><span class="badge-red">Plain Text</span> + HMAC in registry</td>
                <td>Private</td>
                <td>Winner verification, prize notification</td>
                <td><code>campaigns-worker.js#L865-L872</code></td>
                <td><span class="status-confirmed">CONFIRMED</span></td>
            </tr>
            <tr>
                <td><strong>Phone Number</strong></td>
                <td>Umrah Contest Registration</td>
                <td>SharedPref (<code>dt_gc_phone</code>), Firestore (<code>.../entries/{'{'}uid{'}'}.phone</code>)</td>
                <td><span class="badge-red">Plain Text E.164</span> (+91...)</td>
                <td>Private</td>
                <td>Contacting prize winner</td>
                <td><code>campaigns-worker.js#L865-L872</code><br><code>GroupCampaignRepository.kt#L170</code></td>
                <td><span class="status-confirmed">CONFIRMED</span></td>
            </tr>
            <tr>
                <td><strong>Display Name / Full Name</strong></td>
                <td>Live Rooms &amp; Contest form</td>
                <td>SharedPref (<code>dt_gc_name</code>), Firestore (<code>entries/{'{'}uid{'}'}.name</code>)</td>
                <td>Plain Text</td>
                <td><strong>Public</strong> (Rooms &amp; Boards)</td>
                <td>Leaderboard display, winner audit</td>
                <td><code>GroupCampaignRepository.kt#L168</code><br><code>campaigns-worker.js#L357</code></td>
                <td><span class="status-confirmed">CONFIRMED</span></td>
            </tr>
            <tr>
                <td><strong>City, State &amp; Country</strong></td>
                <td>Contest registration &amp; Prayers</td>
                <td>SharedPref (<code>dt_gc_city</code>), Firestore (<code>.../entries/{'{'}uid{'}'}.city</code>)</td>
                <td>Plain Text</td>
                <td><strong>Public</strong> (City on board)</td>
                <td>Regional leaderboards, solar prayer math</td>
                <td><code>campaigns-worker.js#L360</code><br><code>PrayerRepository.kt#L84</code></td>
                <td><span class="status-confirmed">CONFIRMED</span></td>
            </tr>
            <tr>
                <td><strong>Precise GPS Location</strong></td>
                <td>Prayer calculation &amp; Qibla</td>
                <td>SharedPref (<code>pr_lat</code>, <code>pr_lon</code>); sent to Aladhan API</td>
                <td>Plain Float Coordinates</td>
                <td>Private (Client-Aladhan only)</td>
                <td>Solar prayer times &amp; Qibla azimuth</td>
                <td><code>PrayerRepository.kt#L618-L620</code><br><code>QiblaRepository.kt#L140</code></td>
                <td><span class="status-confirmed">CONFIRMED</span></td>
            </tr>
            <tr>
                <td><strong>Firebase UID / Install ID</strong></td>
                <td>Anonymous Auth &amp; Anti-cheat</td>
                <td>Firebase Auth; SharedPref (<code>dt_install_id</code>); Firestore</td>
                <td>HMAC-SHA256 in Firestore</td>
                <td>Private (PublicId masked)</td>
                <td>Syncing counts, anti-bot deduplication</td>
                <td><code>GroupCampaignRepository.kt#L230</code><br><code>campaigns-worker.js#L840</code></td>
                <td><span class="status-confirmed">CONFIRMED</span></td>
            </tr>
            <tr>
                <td><strong>IP Address</strong></td>
                <td>Network request to Workers</td>
                <td>Worker memory; HMAC-SHA256 in Firestore (<code>ipKey</code>)</td>
                <td>HMAC-SHA256 hashed</td>
                <td>Private</td>
                <td>DDoS mitigation, rate-limiting (120 req/min)</td>
                <td><code>auth-worker.js#L42-L46</code><br><code>campaigns-worker.js#L842</code></td>
                <td><span class="status-confirmed">CONFIRMED</span></td>
            </tr>
            <tr>
                <td><strong>Tasbih Count &amp; Badges</strong></td>
                <td>Tapping beads / digital counter</td>
                <td><strong>100% Local Device Storage</strong> (<code>tasbih_prefs</code>)</td>
                <td>Plain Integers</td>
                <td><strong>100% Private</strong></td>
                <td>Personal worship tracking</td>
                <td><code>TasbihScreen.kt</code> (FROZEN)<br><code>TasbihPreferences.kt</code></td>
                <td><span class="status-confirmed">CONFIRMED</span></td>
            </tr>
            <tr>
                <td><strong>Community Dhikr Count</strong></td>
                <td>Submitting batch in live rooms</td>
                <td>Firestore (<code>campaign_members/{'{'}uid{'}'}.count</code>)</td>
                <td>Plain Integer</td>
                <td><strong>Public</strong> (Aggregated)</td>
                <td>Collective target progression</td>
                <td><code>campaigns-worker.js#L280-L330</code></td>
                <td><span class="status-confirmed">CONFIRMED</span></td>
            </tr>
            <tr>
                <td><strong>Advertising ID (AAID)</strong></td>
                <td>Banner / Rewarded Ad display</td>
                <td>Google Play Services memory</td>
                <td>Pseudonymous ID</td>
                <td>AdMob Network</td>
                <td>Monetization, ad frequency capping</td>
                <td><code>build.gradle.kts#L115</code><br><code>play-services-ads:23.6.0</code></td>
                <td><span class="status-confirmed">CONFIRMED</span></td>
            </tr>
            <tr>
                <td><strong>Crash Diagnostics</strong></td>
                <td>App crash / stack trace</td>
                <td>Firebase Crashlytics servers</td>
                <td>Pseudonymous Instance ID</td>
                <td>Admin console</td>
                <td>Bug fixing, crash stability</td>
                <td><code>build.gradle.kts#L125-L128</code></td>
                <td><span class="status-confirmed">CONFIRMED</span></td>
            </tr>
            <tr>
                <td><strong>Age Gate Flag (18+)</strong></td>
                <td>Joining Community / Contest</td>
                <td>SharedPref (<code>dt_user_is_18_plus</code>)</td>
                <td>Boolean (true/false)</td>
                <td>Private (Local only)</td>
                <td>DPDP compliance (blocks minors from groups)</td>
                <td><code>GroupCampaignRepository.kt#L308</code><br><code>LiveDhikrDialogs.kt#L2206</code></td>
                <td><span class="status-confirmed">CONFIRMED</span></td>
            </tr>
        </tbody>
    </table>

    <div class="diagram-container">
        {svg_02}
        <div class="diagram-caption">Figure 2.1: Storage Topology &amp; PII Reality Map across Local Storage, Cloudflare D1, and Cloud Firestore</div>
    </div>

    <!-- SECTION 3 -->
    <div class="page-break"></div>
    <h2>3. Deliverable 2: Third Parties &amp; Data Processors Table</h2>
    <p>Every external SDK, network processor, and API service integrated into the application:</p>

    <table>
        <thead>
            <tr>
                <th style="width: 18%;">Processor</th>
                <th style="width: 15%;">Role</th>
                <th style="width: 25%;">Data Received</th>
                <th style="width: 12%;">Region</th>
                <th style="width: 20%;">Evidence in Code</th>
                <th style="width: 10%;">Status</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td><strong>Google AdMob</strong> (Google LLC)</td>
                <td>Advertising Network</td>
                <td>AAID, coarse location, device specs, ad interactions</td>
                <td>Global / USA</td>
                <td><code>app/build.gradle.kts#L115</code><br><code>AdManager.kt</code></td>
                <td><span class="status-confirmed">CONFIRMED</span></td>
            </tr>
            <tr>
                <td><strong>Google Firebase</strong></td>
                <td>Auth, Analytics &amp; Crashlytics</td>
                <td>App instance ID, OS version, device model, stack traces</td>
                <td>Global (<code>asia-southeast1</code>)</td>
                <td><code>app/build.gradle.kts#L120-L128</code><br><code>google-services.json</code></td>
                <td><span class="status-confirmed">CONFIRMED</span></td>
            </tr>
            <tr>
                <td><strong>Google Cloud Firestore</strong></td>
                <td>Database Infrastructure</td>
                <td>Name, plain phone, plain email, group counts, anti-cheat</td>
                <td>Global Multiregion</td>
                <td><code>campaigns-worker.js#L25-L35</code></td>
                <td><span class="status-confirmed">CONFIRMED</span></td>
            </tr>
            <tr>
                <td><strong>Cloudflare, Inc.</strong></td>
                <td>Edge Workers &amp; D1 SQL</td>
                <td>IP address, User-Agent, plain email in D1, OTP hashes</td>
                <td>Global Edge</td>
                <td><code>auth-wrangler.toml</code><br><code>auth-worker.js#L1-L50</code></td>
                <td><span class="status-confirmed">CONFIRMED</span></td>
            </tr>
            <tr>
                <td><strong>Resend, Inc.</strong></td>
                <td>Primary Email Delivery</td>
                <td>Recipient plain-text email address, 6-digit OTP code</td>
                <td>USA</td>
                <td><code>auth-worker.js#L350-L375</code></td>
                <td><span class="status-confirmed">CONFIRMED</span></td>
            </tr>
            <tr>
                <td><strong>Brevo (Sendinblue)</strong></td>
                <td>Fallback SMTP Relay</td>
                <td>Recipient email address, quota &amp; sentinel threshold alerts</td>
                <td>EU / Global</td>
                <td><code>GEMINI.md#L45-L48</code><br><code>auth-worker.js#L380-L410</code></td>
                <td><span class="status-confirmed">CONFIRMED</span></td>
            </tr>
            <tr>
                <td><strong>Aladhan API</strong></td>
                <td>Prayer Calculations</td>
                <td>Exact latitude and longitude (<code>latitude=...&amp;longitude=...</code>)</td>
                <td>Public Cloud</td>
                <td><code>PrayerRepository.kt#L618-L620</code></td>
                <td><span class="status-confirmed">CONFIRMED</span></td>
            </tr>
            <tr>
                <td><strong>Quran.com &amp; JSDelivr</strong></td>
                <td>Quran Text &amp; Audio</td>
                <td>IP address, HTTP User-Agent for streaming audio files</td>
                <td>Global CDN</td>
                <td><code>AudioPlayerService.kt#L110</code></td>
                <td><span class="status-confirmed">CONFIRMED</span></td>
            </tr>
        </tbody>
    </table>

    <!-- SECTION 4 -->
    <h2>4. Deliverable 3: Android Device Permissions Matrix</h2>
    <p>All permissions declared in <code>AndroidManifest.xml</code> with technical triggers and degradation behavior:</p>

    <table>
        <thead>
            <tr>
                <th style="width: 20%;">Permission</th>
                <th style="width: 20%;">Why Needed in Code</th>
                <th style="width: 10%;">Optional?</th>
                <th style="width: 25%;">Behaviour if Denied</th>
                <th style="width: 15%;">Evidence</th>
                <th style="width: 10%;">Status</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td><code>ACCESS_FINE_LOCATION</code></td>
                <td>Prayer times &amp; Qibla alignment</td>
                <td><strong>Yes</strong></td>
                <td>Gracefully falls back to manual city selection</td>
                <td><code>PrayerRepository.kt#L590</code></td>
                <td><span class="status-confirmed">CONFIRMED</span></td>
            </tr>
            <tr>
                <td><code>ACCESS_COARSE_LOCATION</code></td>
                <td>Cell/WiFi tower prayer calculation</td>
                <td><strong>Yes</strong></td>
                <td>Falls back to manual city selection</td>
                <td><code>PrayerRepository.kt#L590</code></td>
                <td><span class="status-confirmed">CONFIRMED</span></td>
            </tr>
            <tr>
                <td><code>POST_NOTIFICATIONS</code></td>
                <td>Adhan audio &amp; daily reminders</td>
                <td><strong>Yes</strong></td>
                <td>Alarms trigger silently without banner popup</td>
                <td><code>SettingsScreen.kt#L520</code></td>
                <td><span class="status-confirmed">CONFIRMED</span></td>
            </tr>
            <tr>
                <td><code>SCHEDULE_EXACT_ALARM</code></td>
                <td>Exact-minute prayer Adhan</td>
                <td><strong>Yes</strong></td>
                <td>Alarms may drift 5-15 mins during Android Doze</td>
                <td><code>PrayerAlarmReceiver.kt</code></td>
                <td><span class="status-confirmed">CONFIRMED</span></td>
            </tr>
            <tr>
                <td><code>VIBRATE</code></td>
                <td>Haptic pulse on bead slide / dial</td>
                <td><strong>Yes</strong></td>
                <td>Beads slide smoothly without vibration</td>
                <td><code>TasbihScreen.kt</code></td>
                <td><span class="status-confirmed">CONFIRMED</span></td>
            </tr>
            <tr>
                <td><code>INTERNET</code></td>
                <td>Syncing, audio streaming, ads</td>
                <td><strong>No</strong></td>
                <td>Network calls fail; app operates in full offline mode</td>
                <td><code>AndroidManifest.xml#L18</code></td>
                <td><span class="status-confirmed">CONFIRMED</span></td>
            </tr>
            <tr>
                <td><code>ACCESS_NETWORK_STATE</code></td>
                <td>Verify online connectivity</td>
                <td><strong>No</strong></td>
                <td>Sync requests delayed until reconnection</td>
                <td><code>AndroidManifest.xml#L19</code></td>
                <td><span class="status-confirmed">CONFIRMED</span></td>
            </tr>
            <tr>
                <td><code>RECEIVE_BOOT_COMPLETED</code></td>
                <td>Reschedule alarms after reboot</td>
                <td><strong>No</strong></td>
                <td>Prayer alarms will not trigger until app is reopened</td>
                <td><code>BootReceiver.kt</code></td>
                <td><span class="status-confirmed">CONFIRMED</span></td>
            </tr>
            <tr>
                <td><code>FOREGROUND_SERVICE (MEDIA)</code></td>
                <td>Continuous Quran audio playback</td>
                <td><strong>Yes</strong></td>
                <td>Audio stops when phone screen locks or app backgrounds</td>
                <td><code>AudioPlayerService.kt#L45</code></td>
                <td><span class="status-confirmed">CONFIRMED</span></td>
            </tr>
        </tbody>
    </table>

    <div class="diagram-container">
        {svg_03}
        <div class="diagram-caption">Figure 4.1: Android Permissions &amp; Data Boundary Matrix</div>
    </div>

    <!-- SECTION 5 & 6 -->
    <div class="page-break"></div>
    <h2>5. Deliverable 4 &amp; 5: Public Visibility &amp; Retention Reality Check</h2>

    <h3>Public Visibility Breakdown:</h3>
    <ul>
        <li><strong>Publicly Visible to Other Users:</strong> Display Name (<code>displayName</code>), City (<code>city</code>), Submitted Dhikr Count (<code>count</code>), Last Active Timestamp (<code>lastActive</code>), and a truncated HMAC mask (<code>publicId</code>).</li>
        <li><strong>Strictly Confidential &amp; Never Displayed:</strong> Phone numbers (E.164), normalized email addresses, exact GPS coordinates, personal offline tasbih counts, and hardware UUIDs.</li>
    </ul>

    <h3>Data Retention &amp; Deletion Reality Check:</h3>
    <div class="callout-danger">
        <strong>VULNERABILITY 1: In-App "Delete My Data" is Simulated</strong><br>
        In <code>SettingsScreen.kt#L1151-L1155</code>, tapping "Submit Erasure Request" inside <code>DeleteMyDataDpdpModal</code> only sets <code>isSubmitted = true</code> in memory. It makes <strong>no API call</strong>, does not delete the Firebase Auth user, and erases zero records from D1 or Firestore.
    </div>

    <div class="callout-danger">
        <strong>VULNERABILITY 2: No Automated Retention Cron in Cloudflare D1</strong><br>
        In <code>schema.sql</code> and <code>auth-worker.js</code>, OTP records in table <code>otp_records</code> and rate limits in <code>rate_limits</code> remain indefinitely until overwritten by a new OTP request. No scheduled cron exists in <code>auth-wrangler.toml</code>.
    </div>

    <div class="callout-warning">
        <strong>VULNERABILITY 3: Android Auto-Backup Leaks PII to Google Drive</strong><br>
        <code>backup_rules.xml#L3</code> and <code>data_extraction_rules.xml#L4-L9</code> include standard SharedPreferences. Plain-text phone numbers (<code>dt_gc_phone</code>), names (<code>dt_gc_name</code>), and GPS coordinates (<code>pr_lat</code>, <code>pr_lon</code>) are automatically uploaded unencrypted to the user's personal Google Drive.
    </div>

    <!-- SECTION 7 -->
    <h2>6. Deliverable 6: Claims-vs-Code Mismatches Table</h2>
    <p>Comparing statements in the existing <code>privacy-policy.html</code> against verified codebase truth:</p>

    <table>
        <thead>
            <tr>
                <th style="width: 25%;">Policy Claim</th>
                <th style="width: 28%;">Actual Codebase Truth</th>
                <th style="width: 10%;">Verdict</th>
                <th style="width: 15%;">Evidence</th>
                <th style="width: 22%;">Mandatory Correction</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>"Stored locally using <strong>encrypted</strong> Android SharedPreferences / DataStore."</td>
                <td>Standard unencrypted <code>MODE_PRIVATE</code> SharedPreferences is used everywhere. No Jetpack Security crypto.</td>
                <td><span class="status-false">FALSE</span></td>
                <td><code>GroupCampaignRepository.kt#L45</code><br><code>TasbihPreferences.kt#L25</code></td>
                <td>Remove "encrypted". State: "Stored in private sandboxed application storage."</td>
            </tr>
            <tr>
                <td>"Instant erasure... by tapping 'Delete My Data' in the in-app Settings menu."</td>
                <td>Button is simulated in UI (<code>isSubmitted = true</code>). Zero network calls sent; zero database records deleted.</td>
                <td><span class="status-false">FALSE</span></td>
                <td><code>SettingsScreen.kt#L1151</code></td>
                <td>Direct users to Android "Clear Data" for local wipe, and web URL for cloud erasure.</td>
            </tr>
            <tr>
                <td>"Automated 30-day purge: All contestant logs deleted 30 days after winner announcement."</td>
                <td>No automated Cron or TTL trigger exists. Purging is 100% manual via administrative API endpoint.</td>
                <td><span class="status-false">FALSE</span></td>
                <td><code>campaigns-wrangler.toml</code><br><code>campaigns-worker.js#L104</code></td>
                <td>Clarify that data is manually purged by administrators post-audit.</td>
            </tr>
            <tr>
                <td>"Phone numbers submitted for rewards are encrypted at rest."</td>
                <td>Stored in plain-text E.164 (<code>+91...</code>) in Firestore fields and SharedPrefs. Disk encryption exists only at GCP infrastructure level.</td>
                <td><span class="status-alert">MISLEADING</span></td>
                <td><code>campaigns-worker.js#L865</code><br><code>GroupCampaignRepository.kt#L170</code></td>
                <td>State that phone numbers are stored in access-controlled databases with server-side disk encryption.</td>
            </tr>
            <tr>
                <td>"Resend, Inc. used exclusively to deliver verification emails."</td>
                <td>Brevo SMTP relay is also integrated as a secondary gateway for sentinel alerts and emails.</td>
                <td><span class="status-alert">INCOMPLETE</span></td>
                <td><code>GEMINI.md#L45</code><br><code>auth-worker.js#L380</code></td>
                <td>Disclose Brevo (Sendinblue) as an authorized email processor.</td>
            </tr>
            <tr>
                <td>"We do not knowingly collect information from children under 13 (COPPA)."</td>
                <td>App now implements an 18+ age gate for community features under DPDP Act 2023.</td>
                <td><span class="status-alert">INCOMPLETE</span></td>
                <td><code>GroupCampaignRepository.kt#L308</code></td>
                <td>Update to disclose the 18+ requirement for online community features.</td>
            </tr>
        </tbody>
    </table>

    <div class="diagram-container">
        {svg_04}
        <div class="diagram-caption">Figure 6.1: Consent, DPDP Act 18+ Gate Decision Tree &amp; Deletion Remediation Architecture</div>
    </div>

    <!-- SECTION 8 -->
    <div class="page-break"></div>
    <h2>7. Deliverable 7: Google Play Data Safety Form Answers</h2>
    <p>Exact declarations required for the Google Play Console Data Safety questionnaire:</p>

    <table>
        <thead>
            <tr>
                <th style="width: 20%;">Play Console Data Type</th>
                <th style="width: 10%;">Collected?</th>
                <th style="width: 10%;">Shared?</th>
                <th style="width: 10%;">Ephemeral?</th>
                <th style="width: 12%;">Mandatory?</th>
                <th style="width: 38%;">Purpose Declared in Play Console</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td><strong>Name</strong></td>
                <td>YES</td>
                <td>YES (Board)</td>
                <td>NO</td>
                <td>Optional</td>
                <td>App functionality, Community leaderboards, Contest verification</td>
            </tr>
            <tr>
                <td><strong>Email address</strong></td>
                <td>YES</td>
                <td>NO</td>
                <td>NO</td>
                <td>Optional</td>
                <td>Account management, Developer communication (OTP verification)</td>
            </tr>
            <tr>
                <td><strong>Phone number</strong></td>
                <td>YES</td>
                <td>NO</td>
                <td>NO</td>
                <td>Optional</td>
                <td>App functionality, Special campaign winner fulfillment</td>
            </tr>
            <tr>
                <td><strong>User IDs</strong> (Firebase / Install UUID)</td>
                <td>YES</td>
                <td>NO</td>
                <td>NO</td>
                <td>Mandatory</td>
                <td>App functionality, Anti-fraud rate-limiting, Account management</td>
            </tr>
            <tr>
                <td><strong>Approximate Location</strong></td>
                <td>YES</td>
                <td>NO</td>
                <td>YES</td>
                <td>Optional</td>
                <td>App functionality (Prayer time calculation)</td>
            </tr>
            <tr>
                <td><strong>Precise Location</strong></td>
                <td>YES</td>
                <td>NO</td>
                <td>YES</td>
                <td>Optional</td>
                <td>App functionality (Solar prayer calculation &amp; Qibla compass)</td>
            </tr>
            <tr>
                <td><strong>App Actions</strong> (Dhikr submissions)</td>
                <td>YES</td>
                <td>YES (Aggregated)</td>
                <td>NO</td>
                <td>Optional</td>
                <td>App functionality, Collective community goals &amp; leaderboards</td>
            </tr>
            <tr>
                <td><strong>Crash Logs &amp; Diagnostics</strong></td>
                <td>YES</td>
                <td>NO</td>
                <td>NO</td>
                <td>Mandatory</td>
                <td>Analytics, App stability &amp; Crash diagnostics (Firebase)</td>
            </tr>
            <tr>
                <td><strong>Device or Other IDs</strong> (AAID)</td>
                <td>YES</td>
                <td>YES (AdMob)</td>
                <td>NO</td>
                <td>Mandatory</td>
                <td>Advertising, Fraud prevention, Ad frequency capping</td>
            </tr>
        </tbody>
    </table>

    <!-- SECTION 9 -->
    <h2>8. Deliverable 8 &amp; 10: Legal Questions &amp; Top 10 Pitfalls</h2>

    <h3>Essential Questions for App Owner &amp; Legal Counsel:</h3>
    <ol>
        <li><strong>Legal Entity Name &amp; Address:</strong> What is the formal registered name and address of the entity operating Imaanix?</li>
        <li><strong>DPDP Grievance Officer:</strong> Who is the appointed Data Grievance Redressal Officer (Name, official email e.g., <code>grievance@imaanix.in</code>)?</li>
        <li><strong>Public Web Deletion URL:</strong> What is the publicly accessible URL for Google Play account deletion (e.g., <code>https://imaanix.in/delete-account</code>)?</li>
        <li><strong>Umrah Contest Eligibility &amp; Tax:</strong> Is participation strictly restricted to 18+ Indian citizens, and who covers Section 194B TDS on prizes?</li>
        <li><strong>Automated D1 Purge:</strong> When will the scheduled Cloudflare Worker cron be added to purge D1 <code>otp_records</code> older than 30 days?</li>
    </ol>

    <h3>Top 10 Things That Would Make the Policy False If Ignored:</h3>
    <ol>
        <li>Claiming Android local storage is encrypted using Keystore when standard SharedPreferences is used.</li>
        <li>Claiming the in-app "Delete My Data" button erases server data when it is currently simulated.</li>
        <li>Claiming an automated 30-day purge for contest entries when purging is completely manual.</li>
        <li>Failing to disclose plain-text storage of phone numbers and emails in Firestore documents.</li>
        <li>Failing to disclose Brevo (Sendinblue) as an active email infrastructure provider.</li>
        <li>Ignoring Google Drive unencrypted cloud backups caused by <code>backup_rules.xml</code>.</li>
        <li>Failing to disclose transmission of exact GPS coordinates to the third-party Aladhan API.</li>
        <li>Claiming "zero third-party sharing" while utilizing Google AdMob and Google Play Services.</li>
        <li>Omitting the 18+ age gate for community features under the India DPDP Act 2023.</li>
        <li>Lacking a functioning, publicly accessible web URL for account and personal data deletion requests.</li>
    </ol>

    <div style="margin-top: 30px; padding-top: 14px; border-top: 1px solid #cbd5e1; font-size: 8pt; color: #64748b; text-align: center;">
        &copy; 2026 Imaanix. All rights reserved. Technical and Privacy Verification Audit for Digital Tasbih.
    </div>

</body>
</html>
"""

web_html_path = os.path.join(DOCS_DIR, "AUDIT_REPORT_WEB_VERSION.html")
with open(web_html_path, "w", encoding="utf-8") as f:
    f.write(html_content)

print(f"Saved {web_html_path}")

# Now render PDF via Edge Headless
edge_executable = r"C:\Program Files (x86)\Microsoft\Edge\Application\msedge.exe"
pdf_output_path = os.path.join(PDF_DIR, "Digital_Tasbih_Privacy_and_Data_Flow_Audit_Report.pdf")

cmd = [
    edge_executable,
    "--headless",
    "--disable-gpu",
    "--run-all-compositor-stages-before-draw",
    f"--print-to-pdf={pdf_output_path}",
    "--no-pdf-header-footer",
    web_html_path
]

print("Rendering PDF via headless Microsoft Edge...")
res = subprocess.run(cmd, capture_output=True, text=True)
print("Edge returncode:", res.returncode)

if os.path.exists(pdf_output_path):
    size = os.path.getsize(pdf_output_path)
    print(f"SUCCESS: Generated PDF at {pdf_output_path} ({size} bytes)")
else:
    print("FAILED to generate PDF:", res.stderr)

