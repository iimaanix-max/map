# 🔄 Email OTP Automatic Failover (Resend + Brevo) - ACTIVE IN PRODUCTION 🟢

> **Status:** **LIVE & ACTIVE IN PRODUCTION**  
> **Target Service:** Cloudflare Auth Worker (`api.imaanix.in/auth/*` / `digital-tasbih-auth`)  
> **Primary Provider:** Resend.com (100 emails/day free = 3,000/mo)  
> **Automatic Failover Provider:** Brevo (formerly Sendinblue) (300 emails/day free = 9,000/mo)  
> **Combined Daily Capacity:** **400 Free OTP Emails / Day** (= **12,000 Free OTPs / Month**)  
> **Total Monthly Cost:** **₹0 / Purely Free Tier**  
> **Last Deployed Version ID:** `2ba78bc6-eace-4631-a02f-19089b288e59`

---

## 1. System Overview & Auto-Failover Logic

Jab bhi Android app se user OTP request karta hai (`POST /auth/send-email-otp`), Cloudflare Worker automatic multi-tier delivery execute karega:

```
[Android App: User requests OTP]
              │
              ▼
    Cloudflare Auth Worker
              │
    ┌─────────┴─────────┐
    ▼                   │
[Tier 1: Resend.com]    │
- API: api.resend.com   │
- Limit: 100/day free   │
    │                   │
    ├─► Status 200 OK ──┼──────────────────────┐
    │   (Success)       │                      │
    ▼                   ▼                      ▼
  Status 429/402/Fail  [Tier 2: Brevo Standby]  [Success Response]
  (Quota exhausted)    - API: api.brevo.com    - OTP delivered
                        - Limit: 300/day free   - In-app toast: "Code Sent"
                        │
                        ├─► Status 200 OK ─────┘
                        │   (Auto-recovered)
                        ▼
                       Both Failed
                       - D1 rollback
                       - 502 Service Unavailable
```

### Key Advantages:
1. **Zero User Intervention (Auto-Switch):**
   - Agar aap online nahi hain, so rahe hain ya travel kar rahe hain, system khud Resend ka quota khatam hone par **0.2 second me Brevo par switch** kar deta hai.
2. **Auto-Recovery on Next Day:**
   - Agle din jab Resend ka 100/day quota reset hota hai, Worker wapas pehle Resend try karta hai. Manual reset ki zaroorat nahi.
3. **No Downtime / No App Update Needed:**
   - Android client app ko pata bhi nahi chalta ki email kis engine se nikli hai. Dono cases me branded HTML email (`Digital Tasbih <noreply@imaanix.co.in>`) pahunchti hai.

---

## 2. Technical API Specifications

### Provider 1: Resend.com (Primary)
- **Endpoint:** `https://api.resend.com/emails`
- **Method:** `POST`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer ${env.RESEND_API_KEY}",
    "Content-Type": "application/json"
  }
  ```
- **Body:**
  ```json
  {
    "from": "Digital Tasbih <noreply@imaanix.co.in>",
    "to": ["user@example.com"],
    "subject": "Your Verification Code: 123456",
    "html": "..."
  }
  ```

### Provider 2: Brevo.com (Standby Failover)
- **Endpoint:** `https://api.brevo.com/v3/smtp/email`
- **Method:** `POST`
- **Headers:**
  ```json
  {
    "api-key": "${env.BREVO_API_KEY}",
    "Content-Type": "application/json",
    "Accept": "application/json"
  }
  ```
- **Body:**
  ```json
  {
    "sender": { "name": "Digital Tasbih", "email": "noreply@imaanix.co.in" },
    "to": [{ "email": "user@example.com" }],
    "subject": "Your Verification Code: 123456",
    "htmlContent": "..."
  }
  ```

---

## 3. Staged Standby Files

| File | Purpose | Status |
|------|---------|--------|
| `scratch/auth-worker-standby-brevo.js` | Complete Worker with Resend + Brevo Auto-Failover | Ready for Deploy |
| `scratch/auth-worker-final.js` | Current active single-provider worker | Active |
| `SETUP_GUIDES/EMAIL_FAILOVER_BREVO_STANDBY.md` | This architecture & operational doc | Saved in Memory |

---

## 4. Activation Steps (When Ready to Enable)

Jab bhi aap isko live karna chahein, sirf ye 2 steps karne honge:

### Step 1: Brevo Free API Key Create Karein
1. [Brevo.com](https://brevo.com) par free account banayein (koi credit card nahi lagta).
2. Settings ➔ **SMTP & API** ➔ **Generate a new API key**.
3. Key copy karein (`xkeysib-...`).

### Step 2: Cloudflare Secret Set Karein & Deploy
Terminal me run karein:
```bash
npx wrangler secret put BREVO_API_KEY
# Paste: xkeysib-...
```
Aur `scratch/auth-worker-standby-brevo.js` ko deploy kar dein:
```bash
npx wrangler deploy
```

System automatically dono providers ke sath 400 OTPs/day free handle karna shuru kar dega!

