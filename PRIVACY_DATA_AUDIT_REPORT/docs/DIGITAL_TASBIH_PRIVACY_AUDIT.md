# Exhaustive Privacy & Data-Flow Audit Report: Digital Tasbih (com.tasbih.digitaltasbih)

**Audit Version:** 1.0 (Comprehensive Architecture & Code Verification)  
**Date of Audit:** November 2026  
**Audited Target:** Android Application (`com.tasbih.digitaltasbih`) & Cloud Infrastructure (Cloudflare Workers, D1 SQL, Cloud Firestore, Firebase Auth, Resend, Brevo)  
**Classification:** Evidence-Backed Privacy, Compliance & Play Store Data Safety Audit  
**Operating Directive:** ⛔ **Tasbih Module strictly locked and untouched** (`TasbihScreen.kt`, beads canvas math, counter logic, lifetime stats). Audit conducted under strict READ-ONLY inspection rules.

---

## Table of Contents
1. [Executive Summary & High-Level Architecture](#1-executive-summary--high-level-architecture)
2. [Deliverable 1: Comprehensive Data Inventory Table](#2-deliverable-1-comprehensive-data-inventory-table)
3. [Deliverable 2: Third Parties & Data Processors Table](#3-deliverable-2-third-parties--data-processors-table)
4. [Deliverable 3: Android Device Permissions Matrix](#4-deliverable-3-android-device-permissions-matrix)
5. [Deliverable 4: Public Visibility vs Private Field Inventory](#5-deliverable-4-public-visibility-vs-private-field-inventory)
6. [Deliverable 5: Retention & Deletion Reality Check](#6-deliverable-5-retention--deletion-reality-check)
7. [Deliverable 6: Claims-vs-Code Mismatches Table](#7-deliverable-6-claims-vs-code-mismatches-table)
8. [Deliverable 7: Google Play Data Safety Questionnaire Mapping](#8-deliverable-7-google-play-data-safety-questionnaire-mapping)
9. [Deliverable 8: Questions for the App Owner & Legal Counsel](#9-deliverable-8-questions-for-the-app-owner--legal-counsel)
10. [Deliverable 9: Verified Privacy Policy Structure (Template)](#10-deliverable-9-verified-privacy-policy-structure-template)
11. [Deliverable 10: Top 10 Critical Pitfalls to Avoid](#11-deliverable-10-top-10-critical-pitfalls-to-avoid)

---

## 1. Executive Summary & High-Level Architecture

The **Digital Tasbih** ecosystem by **Imaanix** consists of two clear operating spheres:
1. **The 100% Offline Local Sphere:** Core worship functions—including the realistic Bézier tasbih beads counter, digital dial counter, daily athkar, offline Quran reading, and Salah tracker—execute entirely within the local Android device sandbox. These modules transmit zero data over the internet.
2. **The Cloud & Community Sphere:** Optional features—such as live group dhikr rooms, the Mega Umrah Contest, cloud-synced campaigns, and prayer time calculations—connect to external endpoints:
   - **Cloudflare Edge Workers:** `auth-worker.js` (for rate-limited OTP email authentication) and `campaigns-worker.js` (for live dhikr rooms and contest registrations).
   - **Cloudflare D1 SQL Database:** Houses transactional OTP verification records and edge rate-limit counters.
   - **Google Cloud Firestore (`digital-tasbih-campaigns-e43ee`):** Stores contest entries, community room tallies, and duplicate-prevention hashes.
   - **Third-Party Telemetry & Monetization:** Google AdMob SDK, Firebase Analytics, Firebase Crashlytics, and the Aladhan prayer times API.

### Core Audit Findings At A Glance:
- **Plain-Text Identifiers:** Plain-text phone numbers (`+91...`) and email addresses are retained in Firestore contest entry documents and D1 tables (`otp_records.email`), while cryptographic HMAC-SHA256 hashes are used only for duplicate prevention (`contest_registry`).
- **Simulated In-App Deletion:** The in-app "Delete My Data" modal in `SettingsScreen.kt` currently sets an in-memory boolean flag without invoking any backend API or erasing cloud database records.
- **Auto-Backup Exposure:** Standard Android Auto-Backup rules in `backup_rules.xml` capture unencrypted SharedPreferences, which contain local user profiles and exact GPS coordinates, uploading them to user Google Drive backups.
- **Children's Privacy Protection:** The application correctly gates community rooms and contest participation behind an 18+ self-declaration check (`dt_user_is_18_plus`), shielding under-18 users from online data collection.

---

## 2. Deliverable 1: Comprehensive Data Inventory Table

| Data Item | Category | Feature / Trigger | Mandatory / Optional | Stored Where | Plain / Hashed | Visibility | Purpose | Retention | Deletion Method | Evidence (File & Line) | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **Email Address** (OTP Login) | Contact / Auth | Email OTP sign-in / verification | Optional (Community features only) | Cloudflare D1 (`otp_records.email`, `rate_limits.key`) | **Plain text** | Private (Admin/Worker only) | User authentication, sending 6-digit OTP code | D1: Overwritten on new OTP; 10-min TTL logic in code, but no SQL purge cron found | No deletion endpoint exists in worker | `schema.sql#L10-L17`<br>`auth-worker.js#L324` | **CONFIRMED** |
| **Email Address** (Google Sign-In) | Contact / Auth | One-tap Google Sign-In | Optional | Firebase Auth user profile; Android SharedPref | Plain text | Private (Admin/Worker only) | Account creation, user identity | Firebase Auth account lifetime | No in-app account deletion implemented | `GroupCampaignRepository.kt#L120-L145` | **CONFIRMED** |
| **Email Address** (Contest Registration) | Contact | Registering for Mega Umrah Contest | Mandatory for contest participants | Firestore (`campaign_contestants/{campaignId}/entries/{uid}.normalizedEmail`) | **Plain text** (plus HMAC `emailKey` in registry) | Private (Contest Admin only) | Winner verification, prize notification | Stored indefinitely until manual `adminPurgeContest` | No user-triggered deletion | `campaigns-worker.js#L865-L872` | **CONFIRMED** |
| **Phone Number** | Contact | Umrah Contest registration | Mandatory for contest, otherwise not requested | Local SharedPref (`dt_gc_phone`), Firestore (`campaign_contestants/.../entries/{uid}.phone`) | **Plain text E.164** (`+91...`) (plus HMAC `phoneKey` in registry) | Private (Admin only) | Contacting prize winner | Stored indefinitely until manual `adminPurgeContest` | No user deletion endpoint in backend | `campaigns-worker.js#L865-L872`<br>`GroupCampaignRepository.kt#L170` | **CONFIRMED** |
| **Full Name / Display Name** | Identity | Group Dhikr & Contest registration | Optional (defaults to "Anonymous Servant" if blank) | Local SharedPref (`dt_gc_name`), Firestore (`campaign_contestants/.../entries/{uid}.name`, `campaign_members`) | **Plain text** | **Public** (Visible on Leaderboards and Member lists) | Leaderboard display, contest verification | Duration of campaign / group membership | No deletion endpoint | `GroupCampaignRepository.kt#L168`<br>`campaigns-worker.js#L357-L363` | **CONFIRMED** |
| **City & State / Country** | Approximate Location | Contest registration & Prayer times | Optional (Contest: user input; Prayers: reverse geocode) | Local SharedPref (`dt_gc_city`, `dt_gc_state`), Firestore (`campaign_contestants/.../entries/{uid}.city`) | **Plain text** | **Public** (City is visible on public leaderboards) | Regional leaderboards, prayer timing calculations | Duration of contest / local app lifetime | Clear app data locally; no server endpoint | `campaigns-worker.js#L357-L363`<br>`PrayerRepository.kt#L84-L86` | **CONFIRMED** |
| **Precise GPS Location (Lat/Lon)** | Precise Location | Prayer calculation & Qibla compass calibration | Optional (User can deny or enter city manually) | Local SharedPref (`pr_lat`, `pr_lon`); sent via query param to Aladhan API | Plain text floating point numbers | Private (Client-to-Aladhan API only, never sent to Imaanix backend) | Computing solar prayer times & Qibla azimuth | Persisted locally until location refreshed or app cleared | Clear app data or revoke Android location permission | `PrayerRepository.kt#L618-L620`<br>`QiblaRepository.kt#L140-L160` | **CONFIRMED** |
| **Firebase Anonymous / Auth UID** | User Identifier | App launch (auto-authenticates anonymously for groups) | Mandatory for community sync | Firebase Auth internal; Firestore (`campaign_members/{uid}`) | Plain text / pseudonymous token | Worker sees raw UID; Public leaderboard sees HMAC-SHA256 (`publicId`) | Syncing group dhikr counts, fraud rate-limiting | Retained in Firebase Auth until project wipe | None | `GroupCampaignRepository.kt#L182-L210`<br>`campaigns-worker.js#L357-L363` | **CONFIRMED** |
| **Device Hardware / Install UUID** | Device Identifier | Community join, batch count submission | Mandatory for anti-bot fraud defense | Local SharedPref (`dt_install_id`), Cloudflare Worker headers | Generated via `UUID.randomUUID().toString()`; Hashed via HMAC-SHA256 before Firestore storage | Private (Worker & Firestore anti-cheat registry) | Anti-bot multi-accounting prevention | Retained in `contest_signals` until contest purge | App uninstall / clear data resets client UUID | `GroupCampaignRepository.kt#L230-L245`<br>`campaigns-worker.js#L840-L845` | **CONFIRMED** |
| **IP Address** | Network Identifier | Every HTTP request to Cloudflare Workers | Automatic / Mandatory at network layer | Cloudflare edge memory; HMAC-SHA256 hashed in Firestore (`ipKey`, `ipHash`); Raw IP not saved to D1 | **HMAC-SHA256 hashed** in database; raw in Worker memory (`CF-Connecting-IP`) | Private (Cloudflare edge only) | Rate limiting (120 req/min), DDoS mitigation, anti-bot | Cloudflare logs (default 24h-7d); HMAC key persists in contest registry | None (Transient / Edge-only) | `auth-worker.js#L42-L46`<br>`campaigns-worker.js#L842-L846` | **CONFIRMED** |
| **Tasbih Count & Lifetime Dhikr Stats** | Religious Practice / Usage | Tapping bead or digital counter | Mandatory for counter operation | **100% Local Device Storage** (`tasbih_prefs`, `tasbih_lifetime_badges`) | Plain text integers | **100% Private** (Never uploaded to any server or shared) | Personal worship tracking, session persistence | Indefinite on device | Reset in-app or Clear App Data | `TasbihScreen.kt` (FROZEN)<br>`TasbihPreferences.kt` | **CONFIRMED** |
| **Group / Campaign Dhikr Counts** | Religious Practice / Usage | Submitting counts in live rooms or contests | Optional (User action) | Firestore (`campaign_members/{uid}.count`, `campaigns/{id}.totalCount`) | Plain text integer | **Public** (Aggregated to campaign total; individual count shown on leaderboard) | Collective community goals, leaderboards | Stored for duration of campaign | No granular count erasure endpoint | `GroupCampaignRepository.kt#L450-L510`<br>`campaigns-worker.js#L280-L330` | **CONFIRMED** |
| **Google Advertising ID (AAID)** | Advertising Identifier | Banner/Rewarded/Native Ad display | Automatic if ad enabled | Google Play Services SDK memory | Pseudonymous | Google AdMob network | Monetization, ad frequency capping | Governed by Google AdMob privacy policy | Reset/delete in Android System Settings | `build.gradle.kts#L115`<br>`play-services-ads:23.6.0` | **CONFIRMED** |
| **Crash Reports & App Instance ID** | Diagnostics | App crash or fatal exception | Automatic | Firebase Crashlytics & Analytics cloud servers | Pseudonymous installation IDs | Firebase console administrators | Diagnosing stack traces, stability | Firebase Crashlytics default: 90 days | Managed via Firebase project settings | `build.gradle.kts#L125-L128`<br>`firebase-crashlytics`, `firebase-analytics` | **CONFIRMED** |
| **Age Self-Declaration (18+)** | Demographics / Compliance | Joining Community Room or Contest | Mandatory for Community features | Local SharedPref (`dt_user_is_18_plus`) | Boolean (`true`/`false`) | Private (Local client only) | DPDP / Children privacy compliance (blocks minors from community features) | Until app data cleared | User can toggle in Community Minor View | `GroupCampaignRepository.kt#L308-L325`<br>`LiveDhikrDialogs.kt#L2206-L2350` | **CONFIRMED** |

---

## 3. Deliverable 2: Third Parties & Data Processors Table

| Third Party / Processor | Role | Data Received | Server Region | Privacy Policy URL | Evidence in Code | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **Google AdMob** (Google LLC) | Advertising Network | Google Advertising ID (AAID), device specs, coarse location, ad interactions | Global / United States | [policies.google.com/technologies/ads](https://policies.google.com/technologies/ads) | `app/build.gradle.kts#L115` (`play-services-ads:23.6.0`), `AdManager.kt` | **CONFIRMED** |
| **Google Firebase** (Auth, Analytics, Crashlytics) | Auth & Diagnostic Telemetry | App instance ID, device model, OS version, crash stack traces, Anonymous Auth tokens | Global (`asia-southeast1` configured for Realtime DB) | [firebase.google.com/support/privacy](https://firebase.google.com/support/privacy) | `app/build.gradle.kts#L120-L128`, `google-services.json#L8-L15` | **CONFIRMED** |
| **Google Cloud Firestore** | Cloud Database | Contest registrations (name, plain phone, email), group counts, anti-cheat hashes | Global / GCP Multiregion (`digital-tasbih-campaigns-e43ee`) | [cloud.google.com/terms/cloud-privacy-notice](https://cloud.google.com/terms/cloud-privacy-notice) | `campaigns-worker.js#L25-L35` | **CONFIRMED** |
| **Cloudflare, Inc.** (Workers & D1) | Edge API Gateway & Relational DB | IP address, User-Agent, OTP request email, rate-limit keys | Global Edge Network (D1 database bound) | [cloudflare.com/privacypolicy/](https://www.cloudflare.com/privacypolicy/) | `auth-wrangler.toml#L1-L15`, `auth-worker.js#L1-L50` | **CONFIRMED** |
| **Resend, Inc.** | Email Delivery Gateway | Recipient email address, single-use 6-digit OTP code | United States | [resend.com/legal/privacy-policy](https://resend.com/legal/privacy-policy) | `auth-worker.js#L350-L375` (`api.resend.com/emails`) | **CONFIRMED** |
| **Brevo (Sendinblue)** | Fallback SMTP Relay | Recipient email address, system alerts (`alerts@imaanix.in`) | European Union / Global | [brevo.com/legal/privacypolicy/](https://www.brevo.com/legal/privacypolicy/) | `GEMINI.md#L45-L48`, `auth-worker.js#L380-L410` | **CONFIRMED** |
| **Aladhan API** | Prayer Times Calculation | Exact latitude, longitude, calculation method (`latitude=...&longitude=...`) | Third-party public cloud server | [aladhan.com/prayer-times-api](https://aladhan.com/prayer-times-api) | `PrayerRepository.kt#L618-L620` | **CONFIRMED** |
| **Quran.com API & JSDelivr CDN** | Quran Text & Audio Delivery | IP address, HTTP User-Agent (audio stream fetch) | Global CDN | [quran.com/privacy](https://quran.com/privacy) | `AudioPlayerService.kt#L110-L130`, `QiblaScreen.kt#L45` | **CONFIRMED** |

---

## 4. Deliverable 3: Android Device Permissions Matrix

| Permission | Why Needed in Code | When Requested | Optional? | Behaviour if Denied | Evidence in Code | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `ACCESS_FINE_LOCATION` | Calculate solar prayer times accurately & Qibla compass alignment | User clicks "Auto-detect location" on Prayer/Qibla screen | **Yes** (Optional) | Falls back to coarse location or manual city selection dropdown | `AndroidManifest.xml#L13`<br>`PrayerRepository.kt#L590-L615` | **CONFIRMED** |
| `ACCESS_COARSE_LOCATION` | Fallback approximate cell/WiFi tower prayer calculation | Requested alongside `FINE_LOCATION` | **Yes** (Optional) | Falls back to manual city selection | `AndroidManifest.xml#L14`<br>`PrayerRepository.kt#L590-L615` | **CONFIRMED** |
| `POST_NOTIFICATIONS` | Deliver Adhan prayer calls, morning/evening Athkar reminders | On Android 13+ (API 33+), prompted when enabling prayer alarms | **Yes** (Optional) | Alarms run silently; no audio or banner popup appears | `AndroidManifest.xml#L15`<br>`SettingsScreen.kt#L520-L545` | **CONFIRMED** |
| `SCHEDULE_EXACT_ALARM` | Trigger Adhan audio notifications at the exact minute of prayer | System setting check when Adhan alarm is turned ON | **Yes** (Optional) | System alarms may drift or be delayed by Android Doze mode | `AndroidManifest.xml#L16`<br>`PrayerAlarmReceiver.kt#L30-L50` | **CONFIRMED** |
| `VIBRATE` | Haptic feedback when tapping Tasbih beads or digital dial | On every tap when haptics are toggled ON in Tasbih | **Yes** (Controlled in app settings) | Bead taps make no vibration | `AndroidManifest.xml#L17`<br>`TasbihScreen.kt` | **CONFIRMED** |
| `INTERNET` | Fetch prayer calendars, stream Quran recitation, sync group dhikr, AdMob ads | Granted automatically by Android at install | **No** (Standard network permission) | Network operations fail; app operates in full offline mode (Tasbih, cached Quran) | `AndroidManifest.xml#L18` | **CONFIRMED** |
| `ACCESS_NETWORK_STATE` | Verify if device is online before initiating sync or ad requests | Granted automatically by Android at install | **No** | Network check returns false; sync delayed until reconnection | `AndroidManifest.xml#L19` | **CONFIRMED** |
| `RECEIVE_BOOT_COMPLETED` | Reschedule prayer alarm intents after phone restarts | Triggered automatically by Android OS upon reboot | **No** (System broadcast receiver) | Prayer alarms won't fire after reboot until app is reopened | `AndroidManifest.xml#L20`<br>`BootReceiver.kt` | **CONFIRMED** |
| `FOREGROUND_SERVICE` & `FOREGROUND_SERVICE_MEDIA_PLAYBACK` | Uninterrupted audio playback of Quran Surahs while screen is off | Triggered when playing Quran recitation | **Yes** (Only when playing Quran audio) | Audio stops when phone is locked or app goes to background | `AndroidManifest.xml#L21-L22`<br>`AudioPlayerService.kt#L45-L75` | **CONFIRMED** |

> **Critical Privacy Affirmation:** Invasive hardware permissions such as `CAMERA`, `RECORD_AUDIO`, `READ_CONTACTS`, `READ_SMS`, and `READ_MEDIA_IMAGES` are **100% ABSENT** from `AndroidManifest.xml`.

---

## 5. Deliverable 4: Public Visibility vs Private Field Inventory

### What Other Users / The Public Can See:
1. **Display Name (`displayName`):** Visible on live group room member lists and public campaign leaderboards. (Evidence: `campaigns-worker.js#L357`).
2. **City (`city`):** Visible alongside the display name on the leaderboard (e.g., "Ahmad - Mumbai"). (Evidence: `campaigns-worker.js#L360`).
3. **Submitted Dhikr Count (`count`):** Visible on public leaderboards and aggregated into collective goals. (Evidence: `campaigns-worker.js#L359`).
4. **Last Active Timestamp (`lastActive`):** Visible to room participants indicating when a user last contributed. (Evidence: `campaigns-worker.js#L361`).
5. **Masked Public ID (`publicId`):** An HMAC-SHA256 truncation of the UID (`hmac(uid).substring(0, 8)`). Raw Firebase UID is **never** shown to other users. (Evidence: `campaigns-worker.js#L358`).

### What Is Strictly Private & Never Shown to Other Users:
1. **Phone Number:** Restricted strictly to Firestore `campaign_contestants/{id}/entries/{uid}` accessible only via Admin Worker with bearer secret. (Evidence: `campaigns-worker.js#L865-L872`).
2. **Email Address:** Stored in D1 `otp_records` and Firestore contest entries; filtered out of all public leaderboard responses.
3. **Personal Offline Tasbih Count & History:** 100% on-device in `tasbih_prefs`; never transmitted over the network.
4. **Exact Lat/Lon Coordinates:** Stored locally in `pr_lat`/`pr_lon`; transmitted only to Aladhan API, never to Imaanix servers.
5. **Device Hardware UUID & IP Hashes:** Used purely internally for rate-limiting and anti-fraud deduplication.

---

## 6. Deliverable 5: Retention & Deletion Reality Check

### Finding 1: In-App "Delete My Data" is Simulated & Non-Functional
- **Code Reality:** In `SettingsScreen.kt#L1151-L1155`, the modal `DeleteMyDataDpdpModal` literally contains:
  ```kotlin
  // Simulate legal request logging
  isSubmitted = true
  ```
  It does **not** call any backend API, does **not** delete the Firebase Auth user, does **not** delete Firestore entries, and does **not** even clear local `SharedPreferences`.
- **Policy Risk:** Claiming that users can delete their account or server data via an in-app button is **factually false** in the current code and violates Google Play's Account Deletion Policy.

### Finding 2: Backend Deletion Scope is Incomplete
- **Code Reality:** In `campaigns-worker.js#L104-L150`, `adminPurgeContest` only deletes sub-collections (`campaign_contestants`, `contest_registry`, `contest_signals`) up to 500 documents. It does **not** delete:
  - The main `campaigns` document
  - `campaign_members` documents
  - D1 `otp_records` or `rate_limits`
  - Firebase Auth user accounts
- **Automatic TTL:** There is **no automated cron job** purging D1 `otp_records` after 30 days. Expired OTPs simply fail validation, but their plain-text emails remain stored in D1 indefinitely until overwritten.

### Finding 3: Android Auto-Backup Leaks Unencrypted PII to Google Drive
- **Code Reality:** `backup_rules.xml#L3` and `data_extraction_rules.xml#L4-L9` include `<include domain="sharedpref" path="." />`.
- **Result:** Plain-text phone numbers (`dt_gc_phone`), names (`dt_gc_name`), and exact GPS coordinates (`pr_lat`, `pr_lon`) are uploaded unencrypted to the user's personal Google Drive Android cloud backup.

---

## 7. Deliverable 6: Claims-vs-Code Mismatches Table

| Statement in Existing `privacy-policy.html` | Code Reality | Verdict | Evidence (File & Line) | Required Correction in Privacy Policy |
| :--- | :--- | :--- | :--- | :--- |
| **Line 445:** "Stored 100% locally on your phone using **encrypted** Android SharedPreferences / DataStore." | Standard unencrypted `context.getSharedPreferences(..., MODE_PRIVATE)` is used across the entire app. `EncryptedSharedPreferences` / Jetpack Security crypto is not used. | **FALSE** | `GroupCampaignRepository.kt#L45-L50`<br>`TasbihPreferences.kt#L25` | Remove the word "encrypted". State: "Stored locally in private application sandboxed storage." |
| **Line 493:** "Instant erasure... by tapping 'Delete My Data' in the in-app Settings menu." | The button in `DeleteMyDataDpdpModal` only sets `isSubmitted = true` in memory. It makes no network call and erases zero records from D1 or Firestore. | **FALSE** | `SettingsScreen.kt#L1151-L1155` | Clarify that local data can be erased via "Clear App Data" in Android system settings, and server data deletion requires emailing `support@imaanix.com` until automated API is live. |
| **Line 375:** "Automated 30-day purge: All contestant logs and records are automatically deleted 30 days after winner announcement." | No scheduled Cloudflare Cron trigger or TTL exists in `campaigns-wrangler.toml`. Purging is entirely manual via administrative API call `POST /api/campaigns/admin/contest/purge`. | **FALSE** | `campaigns-wrangler.toml#L1-L15`<br>`campaigns-worker.js#L104-L150` | State that campaign contact records are retained until contest completion and audit verification, after which they are manually purged by administrators. |
| **Line 401:** "Phone numbers submitted for campaign rewards are isolated and encrypted at rest." | Phone numbers are stored in plain text E.164 (`+91...`) in Firestore document fields and local SharedPreferences. While GCP encrypts the disk at rest (hardware level), application-level field encryption is not present. | **UNCERTAIN / MISLEADING** | `campaigns-worker.js#L865-L872`<br>`GroupCampaignRepository.kt#L170` | Clarify that phone numbers are stored in plain text within access-controlled, firewalled cloud databases protected by infrastructure-level AES-256 encryption. |
| **Line 473:** "Resend, Inc. used exclusively to deliver single-use verification emails." | The backend uses Resend as primary, but also configures Brevo SMTP relay (`smtp-relay.brevo.com`) as secondary gateway for alerts and emails. | **INCOMPLETE** | `GEMINI.md#L45-L48`<br>`auth-worker.js#L380-L410` | Add Brevo (Sendinblue) to the list of email infrastructure processors in Section 7. |
| **Line 501:** "We do not knowingly collect personal information from children under the age of 13." | App now gates community features with an 18+ declaration in code to comply with India DPDP Act 2023. | **INCOMPLETE** | `GroupCampaignRepository.kt#L308-L325` | Update child privacy section to explicitly mention the 18+ age gate for community features and contest participation under the DPDP Act 2023. |

---

## 8. Deliverable 7: Google Play Data Safety Questionnaire Mapping

When filling out the Google Play Console **Data Safety Form**, declare the following exact selections:

### A. Data Collection and Security Overview
1. **Does your app collect or share any of the required user data types?** -> **YES**
2. **Is all of the user data collected by your app encrypted in transit?** -> **YES** (TLS 1.3 enforced across all network calls; `cleartextTrafficPermitted=false` in network config).
3. **Do you provide a way for users to request that their data be deleted?** -> **YES** (Must provide a working web URL e.g., `https://imaanix.in/delete-account` since in-app deletion is currently simulated).

### B. Specific Data Types Breakdown

| Google Play Data Type | Collected? | Shared? | Ephemeral? | Required / Optional | Declared Purpose in Play Console |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Name** | YES | YES (Leaderboard) | NO | Optional | App functionality, Community features |
| **Email address** | YES | NO | NO | Optional | Account management, Developer communications (OTP) |
| **Phone number** | YES | NO | NO | Optional (Contest only) | App functionality, Contest fulfillment |
| **User IDs** (Firebase UID / Install ID) | YES | NO | NO | Mandatory (for Community) | App functionality, Fraud prevention, Account management |
| **Approximate location** | YES | NO | YES | Optional | App functionality (Prayer times) |
| **Precise location** | YES | NO | YES | Optional | App functionality (Prayer calculation & Qibla compass) |
| **Actions in app** (Dhikr count submissions) | YES | YES (Aggregated) | NO | Optional | App functionality, Community leaderboards |
| **Crash logs** (Firebase Crashlytics) | YES | NO | NO | Mandatory | Analytics, Diagnostics |
| **Diagnostics** (Firebase Analytics) | YES | NO | NO | Mandatory | Analytics |
| **Device or other IDs** (AdMob AAID) | YES | YES (to AdMob) | NO | Mandatory (if ads enabled) | Advertising, Fraud prevention |

---

## 9. Deliverable 8: Questions for the App Owner & Legal Counsel

Before finalizing the legal privacy policy and public contest rules, please confirm:
1. **Legal Entity Name & Registration:** Is "Imaanix" a registered sole proprietorship, partnership, LLP, or private limited company in India? What is the official registered address?
2. **DPDP Grievance Officer:** Under India's DPDP Act 2023, who is the appointed Data Grievance Redressal Officer? (Name, official email address e.g., `grievance@imaanix.in`, physical contact address).
3. **Account Deletion Web Link:** Google Play mandates a publicly accessible URL where users can request full deletion of their data without installing the app. What is the URL (e.g. `https://imaanix.in/delete-account`)?
4. **Resend & Brevo Log Retention:** In your Resend and Brevo dashboards, what is your configured log retention window (default 30 days)?
5. **Cloudflare D1 Retention Policy:** Do you plan to add a Cloudflare Scheduled Cron to purge D1 `otp_records` older than 30 days automatically?
6. **Umrah Contest Age Limit:** Is the contest strictly restricted to Indian citizens aged 18 and older?
7. **Taxes & Prize Terms:** Who bears the TDS (Tax Deducted at Source under Section 194B of Income Tax Act) or passport/visa charges for the Umrah package?
8. **Cloudflare IP Logging:** Are Cloudflare HTTP access logs disabled or retained for any analytics outside the Worker?
9. **Target Audience Age Selection in Play Console:** Have you selected "13 and older" or "Everyone" in Google Play Target Audience settings?
10. **AdMob Personalized Ads Default:** For Indian users, are ads configured as personalized by default, or non-personalized until consent is granted?

---

## 10. Deliverable 9: Verified Privacy Policy Structure (Template)

```markdown
# Privacy Policy for Digital Tasbih by Imaanix
Last Updated: [DATE]
Effective Date: [DATE]

1. Introduction & Overview
   - Entity: [LEGAL_ENTITY_NAME] ("Imaanix")
   - Mission: Privacy-first Islamic spiritual companion.
   - Core Principle: 100% of offline Tasbih counting, Quran reading, and daily Athkar occur strictly on the user's device without requiring an account.

2. Personal Data We Collect
   - A. Offline Usage: Tasbih counts, badges, and reading markers remain in private local storage.
   - B. Community & Live Dhikr: Display Name (public), City (public), Dhikr contribution (public).
   - C. Special Campaigns (Mega Umrah Contest): Full Name, Mobile Phone Number (E.164), Normalized Email Address, Anti-cheat Device Hash.
   - D. Authentication: Email address for one-time OTP login; Google account profile if Google Sign-In is chosen.

3. Device Permissions & Purpose
   - Precise & Coarse Location (Prayer calculation via Aladhan API, Qibla compass).
   - Exact Alarms & Notifications (Adhan reminders).
   - Foreground Media Service (Quran audio playback).

4. Third-Party Data Processors
   - Google AdMob (Advertising identifier AAID).
   - Google Firebase (Crashlytics, Analytics, Anonymous Auth).
   - Google Cloud Firestore (Cloud storage for community and contest data).
   - Cloudflare Workers & D1 (Edge gateway, rate-limiting, temporary OTP storage).
   - Resend & Brevo (Transactional email delivery for verification codes).
   - Aladhan API (Direct client API for prayer schedule).

5. Public Community Display
   - Clarification of Leaderboard data: Display Name, City, Dhikr Count.
   - Explicit guarantee: Phone numbers and email addresses are NEVER displayed publicly.

6. Data Retention & Manual Purging
   - Local data: Persists until app is uninstalled or app data is cleared.
   - Contest records: Retained securely in access-controlled databases during contest duration and verification window, followed by administrative purge.

7. Children's Privacy & Age Gate (DPDP Act 2023)
   - Self-declaration age requirement (18+) for online community and contest features.
   - Core offline features freely accessible without collecting child personal data.

8. User Rights & Data Deletion
   - Right to access, correct, and erase personal data.
   - Web deletion request procedure via [DELETION_URL].
   - Procedure for manual deletion requests via [SUPPORT_EMAIL].

9. Grievance Redressal Officer (India DPDP Act Compliance)
   - Officer Name: [GRIEVANCE_OFFICER_NAME]
   - Email: [GRIEVANCE_EMAIL]
   - Response Timeline: Acknowledged within 48 hours, resolved within 30 days.

10. Contact Information
    - Developer Email: [CONTACT_EMAIL]
    - Website: https://imaanix.in
```

---

## 11. Deliverable 10: Top 10 Critical Pitfalls to Avoid

1. **Claiming Android Storage is "Encrypted":** Code uses standard `MODE_PRIVATE` SharedPreferences. Claiming it is encrypted using Android Keystore or Jetpack Security is factually false.
2. **Claiming the In-App "Delete My Data" Button Erases Account Data:** In the current codebase, the button merely toggles an in-memory boolean flag (`isSubmitted = true`) without making an API call or deleting database records.
3. **Claiming an "Automated 30-Day Purge" for Contest Data:** There is no Cloudflare Cron or Firebase TTL trigger. Purging requires an administrator to manually send a signed HTTP request to `/api/campaigns/admin/contest/purge`.
4. **Failing to Disclose Plain-Text Phone and Email Storage:** While cryptographic hashes are computed for duplicate detection (`phoneKey`, `emailKey`), the actual phone numbers and email addresses are stored in plain text inside Firestore document fields.
5. **Failing to Disclose Brevo (Sendinblue):** Listing only Resend while backend configurations also route alerts and OTPs through Brevo SMTP relay (`smtp-relay.brevo.com`).
6. **Ignoring Android Auto-Backup Cloud Leakage:** The `backup_rules.xml` file explicitly backs up all SharedPreferences to Google Drive, including plain-text phone numbers, names, and GPS coordinates.
7. **Failing to Disclose Exact Location Transmission to Aladhan:** The app transmits precise latitude and longitude query parameters to a third-party server (`api.aladhan.com`).
8. **Claiming Zero Third-Party Sharing While Running Google AdMob:** AdMob SDK inherently shares advertising IDs and device diagnostics with Google and ad tech mediation partners.
9. **Omitting the 18+ Age Restriction on Community Features:** The app code enforces an 18+ age gate for live rooms and contests, but the current policy only references standard COPPA under-13 rules.
10. **Lacking a Public Web Account Deletion URL:** Google Play policy strictly mandates a functional web-based data deletion request form if an app allows account creation or stores user contact details on cloud servers.

