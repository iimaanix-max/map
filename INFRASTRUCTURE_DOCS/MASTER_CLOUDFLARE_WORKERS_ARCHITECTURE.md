# 🏗️ Master Cloudflare Workers & Infrastructure Architecture
**Project:** Digital Tasbih Ecosystem  
**Last Updated:** 11 November 2026  
**Confidentiality:** Internal Architecture & Credential Reference  

---

## 1. Multi-Account Cloudflare Topography

Digital Tasbih ka infrastructure total **4 independent Cloudflare accounts** me split hai taaki free tier limits (100,000 req/day per account) isolate rahein aur ek feature doosre ko block na kare:

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                       DIGITAL TASBIH CLOUD ARCHITECTURE                     │
└─────────────────────────────────────────────────────────────────────────────┘
                                       │
        ┌──────────────────────────────┼──────────────────────────────┐
        ▼                              ▼                              ▼
【 ACCOUNT 1: iimaanix 】      【 ACCOUNT 2: abdulquadir3 】  【 ACCOUNT 3: abdulquadir 】
 Email: iimaanix@gmail.com      Email: abdulquadirqureshi3     Email: abdulquadirqureshi
 ID: ca1f76e46a9cacdcd797a4..   ID: 35e47141a892f9020da1964..  ID: 77122f9454e0ef05b9b4..
 Domain: api.imaanix.in         Subdomain: digitaltasbih.      Subdomain: hadithproxy.
         join.imaanix.in                   workers.dev                    workers.dev
        │                              │                              │
        ├─ digital-tasbih-auth         └─ quran-proxy                 └─ hadith-proxy
        ├─ digital-tasbih-campaigns
        │
        └──────────────────────────────┬──────────────────────────────┘
                                       │
                                       ▼
                        【 ACCOUNT 4: taykaro786 】
                         Subdomain: taykaro786.workers.dev
                         Worker: digitaltasbihimagesonly
                         (Cloudflare Static Assets CDN — Unlimited Free)
```

---

## 2. Worker Inventory & Account Mapping

| # | Worker Name | Cloudflare Account | Account ID | Production URL / Route | Primary Responsibility |
|---|---|---|---|---|---|
| **1** | `digital-tasbih-auth` | `iimaanix@gmail.com` | `ca1f76e46a9cacdcd797a4afc2912328` | `https://api.imaanix.in/auth/*` | User Auth, Email OTP (Resend + Brevo), HMAC signing, Quota & Emergency Alerts |
| **2** | `digital-tasbih-campaigns` | `iimaanix@gmail.com` | `ca1f76e46a9cacdcd797a4afc2912328` | `https://api.imaanix.in/campaigns/*`<br>`https://join.imaanix.in/*` | Group Dhikr, 1 Crore Salawat Campaigns, Live sync, 1-Click Deep Link landing page |
| **3** | `quran-proxy` | `abdulquadirqureshi3@gmail.com` | `35e47141a892f9020da19643ef4e4d8b` | `https://quran-proxy.digitaltasbih.workers.dev` | Quran Surahs Arabic text, translations delivery, D1 atomic daily counting |
| **4** | `hadith-proxy` | `abdulquadirqureshi@gmail.com` | `77122f9454e0ef05b9b4d2bc9f55aeac` | `https://hadith-proxy.hadithproxy.workers.dev` | Daily Hadith rotation, collections cache, rate-limit bypass shield, D1 counting |
| **5** | `digitaltasbihimagesonly` | `taykaro786` | Linked to GitHub Repo | `https://digitaltasbihimagesonly.taykaro786.workers.dev` | HD Islamic Posters, Share banners, App assets CDN (Cloudflare Static Assets — 100% Free) |

---

## 3. Deep Dive: Worker Subsystems

### Subsystem A: User Auth & OTP Engine (`digital-tasbih-auth`)
* **Core Function:** Real-time email verification for sign-in without passwords.
* **Dual Delivery Pipeline:**
  1. **Primary Provider:** Resend API (`api.resend.com/emails`).
  2. **Automatic Fallback:** Brevo API (`api.brevo.com/v3/smtp/email`).
  Agar Resend par koi bhi network drop ya error aati hai, worker instantly 0.5s me Brevo se OTP fire kar deta hai. User ko pata bhi nahi chalta.
* **Security & Token Generation:**
  - OTP 6-digits cryptographically random number (`crypto.getRandomValues`).
  - Stored in D1 database `tasbih-auth-db` with SHA-256 hash.
  - Expiry: 10 minutes.
  - Rate Limit: Max 3 OTP requests per hour per email.
* **Threshold Alert System:**
  - Brevo Free Tier limit: 300-400 emails/day.
  - 350 OTPs consumed: URGENT email trigger to `iimaanix@gmail.com` from `alerts@imaanix.in`.
  - 390 OTPs consumed: CRITICAL emergency alert trigger.

---

### Subsystem B: D1 Atomic Counting & Zero Cost Tracking
Workers ke 100k daily requests ko track karne ke liye third-party analytics ya external API call use nahi hoti:
* **The Problem It Solved:** Agar worker count track karne ke liye koi external API call kare, toh har user request par 2 request count hone lagengi (100k ki jagah 50k me limit khatam)!
* **The Solution (Atomic Local D1):**
  - D1 database usi Cloudflare data center me run karta hai jahan worker execute hota hai.
  - Query:
    ```sql
    INSERT INTO daily_stats (day, count, alert_90k, alert_98k)
    VALUES (?1, 1, 0, 0)
    ON CONFLICT(day) DO UPDATE SET count = count + 1
    RETURNING count, alert_90k, alert_98k;
    ```
  - **Non-blocking Execution:** `ctx.waitUntil(trackRequestAsync(env))` use hota hai. Client ko Surah ya Hadith turant mil jati hai (0ms delay), aur background me D1 increment ho jata hai.
  - **Single-Fire Alert Guard (Zero Race Condition):**
    ```sql
    UPDATE daily_stats SET alert_90k = 1 WHERE day = ?1 AND alert_90k = 0 RETURNING day;
    ```
    Ye SQL ensure karta hai ki high concurrency me bhi 90k alert sirf **1 baar** hi email dispatch karega, multiple mails kabhi nahi aayenge.

---

### Subsystem C: Group Dhikr & Campaigns Engine (`digital-tasbih-campaigns`)
* **Visibility Modes:**
  1. **Public:** Sabhi app users ke "Live Dhikr" list me live dikhta hai. Join karne ke liye kisi password ki zarurat nahi.
  2. **Private:** List se completely invisible rehta hai. Sirf `GC-XXXXXXXX` code se join ho sakta hai.
* **1-Click Deep Link Handler (`join.imaanix.in`):**
  - Custom domain `join.imaanix.in` Cloudflare Workers Domains API se bind hai.
  - User URL open karta hai: `https://join.imaanix.in/?code=GC-XXXX`
  - Cloudflare Worker check karta hai:
    - **Crawler User-Agent (WhatsApp, Telegram):** OpenGraph tags return karta hai jisse WhatsApp me image banner + title + description render hota hai.
    - **Mobile Browser:** JS script execute karke `digitaltasbih://join?code=GC-XXXX` open karta hai jo Android App ko seedha room me bhejta hai.
    - **Desktop / No-App Browser:** High quality glassmorphic HTML landing page show karta hai with group code and Google Play Store link.

---

### Subsystem D: Static Assets CDN (`digitaltasbihimagesonly`)
* **Account:** `taykaro786`
* **Technology:** Cloudflare Workers Static Assets linked to GitHub `taykaro786-hue/digital-tasbih-images`.
* **Billing Note:** Cloudflare official dashboard note: *"Requests for this kind of Worker are served at no charge."*
* **Usage:** App ke saare high-resolution Islamic posters, Azan sounds, Tasbih marble textures, aur invite card preview images serve karta hai with 100% unlimited zero-cost bandwidth.

---

## 4. Brevo SMTP Relay & Identities Setup

* **SMTP Server:** `smtp-relay.brevo.com`
* **Port:** `587` (TLS)
* **SMTP User:** `c19c06001@smtp-brevo.com`
* **SMTP Key:** `xsmtpsib-5a66d3a7c4a0fff24e36b37bf747ca3f2daae83266826541231e1b3d9424e709-eIkxxjCTQpoVMBhR`
* **Authorized Gmail Sender Aliases:**
  - `support@imaanix.in`
  - `business@imaanix.in`
  - `help@imaanix.in`
  - `contact@imaanix.in`
* **Security Verification:** Domain `imaanix.in` par SPF (`v=spf1 include:spf.brevo.com ~all`), DKIM (`mail._domainkey.imaanix.in`), aur DMARC active hain, jisse emails inbox me land hote hain (spam folder me nahi jate).

---

## 5. Security & Deployment Secrets Reference

```json
{
  "SIGNING_SECRET": "5d17ec40bd15cc303a861958aead52948063163b903eac88b1f341d9eb17f484",
  "CAMPAIGN_ADMIN_SECRET": "dt_admin_92b003f70cfb6c724d012194beaa0f2356f759b6",
  "D1_DATABASE_ID": "05f1880c-10e7-411d-8c16-69e8bef04428",
  "BREVO_SMTP_USER": "c19c06001@smtp-brevo.com",
  "BREVO_SMTP_KEY": "xsmtpsib-5a66d3a7c4a0fff24e36b37bf747ca3f2daae83266826541231e1b3d9424e709-eIkxxjCTQpoVMBhR"
}
```

---

## 6. Golden Rule (Tasbih Freeze Directive)
As defined in `GEMINI.md`:
* `TasbihScreen.kt`, `RealisticBeadsCanvas` (Bézier curves $P_0, P_1, P_2$, 11-slot layout, 440ms sliding animations), Digital Dial Counter, Daily Dhikr Badges, aur Session reset logic are **100% FROZEN & UNTOUCHABLE**.
* Future modifications to Workers, Auth, Campaigns, or Deep Links must NEVER touch the Tasbih module.
