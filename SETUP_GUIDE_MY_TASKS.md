# 📋 Setup Guide: Aapke End Ke Kaam (Checklist)

Yeh document aapke liye step-by-step checklist hai jo aapko apne **Firebase Console** aur **Cloudflare Worker Dashboard** me set karni hai.

---

## 📌 Index (Kya-Kya Karna Hai):
1. [Part 1: Firebase Google Sign-In Setup](#part-1-firebase-google-sign-in-setup-free-50k-usersmonth)
2. [Part 2: Cloudflare Worker Email OTP Setup](#part-2-cloudflare-worker-email-otp-setup-zero-cost)
3. [Part 3: Security & Bot / Abuse Protection](#part-3-security--bot--abuse-protection)
4. [Part 4: UI Button Controls (Apple & Phone Hide Karna)](#part-4-ui-button-controls-apple--phone-hide-karna)

---

## Part 1: Firebase Google Sign-In Setup (Free 50k Users/Month)

Google Sign-In Firebase par **har mahine 50,000 active users tak 100% Free** hai. Isme user ko koi OTP ka intezar nahi karna padta, 1-tap me verified email mil jata hai.

### Step 1: Google Provider On Karein
1. **[Firebase Console](https://console.firebase.google.com/)** open karein aur apna project select karein.
2. Left side menu me **Build** ➔ **Authentication** par click karein.
3. Top tabs me se **Sign-in method** par click karein.
4. List me **Google** provider par click karein.
5. **Enable** switch ko ON karein.
6. **Project support email** dropdown me se apna email select karein.
7. **Save** par click karein.

---

### Step 2: SHA-1 & SHA-256 Fingerprints Add Karein
> [!IMPORTANT]
> Google Sign-In tabhi kaam karta hai jab Firebase me aapke app ka SHA-1 fingerprint registered ho. Humne aapke debug keystore ka exact fingerprint pehle hi nikaal liya hai:

- **Aapka Debug SHA-1:**
  ```text
  98:87:FF:78:78:A6:88:E7:AA:84:87:92:24:3E:2D:F2:B4:46:AE:9A
  ```
- **Aapka Debug SHA-256:**
  ```text
  84:09:1C:3D:E1:D9:8C:28:A3:3A:77:97:4E:5A:6C:07:43:3F:9A:0A:FF:EC:51:4D:37:24:7D:16:BC:64:39:54
  ```

#### Paste Karne Ka Tarika:
1. Firebase Console me top-left me **Project Overview** ke paas Gear icon ⚙️ (**Project settings**) par click karein.
2. **General** tab me bilkul niche scroll karein jahan **"Your apps"** section hoga.
3. Apna Android package select karein (`com.tasbih.digitaltasbih`).
4. **Add fingerprint** button par click karein.
5. Upar diya gaya **SHA-1** paste karein aur Save karein.
6. Dubara **Add fingerprint** par click karein aur **SHA-256** bhi paste karke Save kar dein.
7. *(Release build ke liye)*: Jab aap Play Store ke liye signed APK/AAB banayenge, toh apne release keystore ka SHA-1 bhi yahi add kar dena.

---

### Step 3: Naya `google-services.json` Download Karein
1. SHA-1 save karne ke baad usi page par **Download google-services.json** ka button dikhega.
2. Use download karein aur apne project ke is folder me purani file ko replace kar dein:
   ```text
   The Native/app/google-services.json
   ```

---

## Part 2: Cloudflare Worker Email OTP Setup (Zero Cost)

Agar user Google Sign-In ke bajay apna Email manually enter kare, toh use 6-digit OTP bhejne ke liye hum Cloudflare Worker + Free Email API use karenge.

### Kaunsi Service Best & Free Hai?
- **Resend.com**: Har mahine **3,000 free emails**. Setup sirf 2 minute ka hai.
- **Brevo.com (Sendinblue)**: Har din **300 free emails** (= 9,000 free emails/month).

---

### Step 1: Free Resend API Key Lein
1. [Resend.com](https://resend.com) par free account banayein.
2. Dashboard me **API Keys** par click karein aur **Create API Key** karein.
3. Key copy kar lein (format: `re_xxxxxxxxxxxxxxxxx`).

---

### Step 2: Cloudflare Worker Me KV Storage Banayein (OTP Save Karne Ke Liye)
1. [Cloudflare Dashboard](https://dash.cloudflare.com/) open karein.
2. Left menu me **Workers & Pages** ➔ **KV** par jayein.
3. **Create a namespace** button click karein aur naam dein: `OTP_KV`.
4. Ab apne Worker par jayein ➔ **Settings** ➔ **Bindings** ➔ **Add**:
   - Type: **KV Namespace**
   - Variable name: `OTP_KV`
   - KV namespace: `OTP_KV` select karein aur Save karein.

---

### Step 3: Worker Environment Variable Set Karein
1. Worker ➔ **Settings** ➔ **Variables** ➔ **Add variable**.
2. Name: `RESEND_API_KEY`
3. Value: Apni Resend wali key (`re_xxxx...`) paste karein aur **Encrypt** tick karke Save karein.

---

### Step 4: Cloudflare Worker Code (Copy & Paste Ready)
Apne Worker script me yeh code paste kar dein:

```javascript
export default {
  async fetch(request, env) {
    const url = new URL(request.url);

    // CORS Headers for Mobile App
    const corsHeaders = {
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "POST, OPTIONS",
      "Access-Control-Allow-Headers": "Content-Type",
    };

    if (request.method === "OPTIONS") {
      return new Response(null, { headers: corsHeaders });
    }

    // ── 1. SEND EMAIL OTP ──
    if (url.pathname === "/send-email-otp" && request.method === "POST") {
      try {
        const body = await request.json();
        const email = body.email ? body.email.trim().toLowerCase() : "";

        if (!email || !email.includes("@")) {
          return new Response(JSON.stringify({ error: "Invalid email" }), {
            status: 400,
            headers: { ...corsHeaders, "Content-Type": "application/json" }
          });
        }

        // 6-digit random secure OTP
        const otp = Math.floor(100000 + Math.random() * 900000).toString();

        // Cloudflare KV me save karein (5 minute validity = 300 seconds)
        await env.OTP_KV.put(email, otp, { expirationTtl: 300 });

        // Resend API ke zariye email dispatch karein
        const emailResponse = await fetch("https://api.resend.com/emails", {
          method: "POST",
          headers: {
            "Authorization": `Bearer ${env.RESEND_API_KEY}`,
            "Content-Type": "application/json"
          },
          body: JSON.stringify({
            from: "Digital Tasbih <onboarding@resend.dev>",
            to: email,
            subject: "Your Digital Tasbih Verification Code",
            html: `
              <div style="font-family: Arial, sans-serif; padding: 20px; color: #1E293B;">
                <h2 style="color: #047857;">Digital Tasbih</h2>
                <p>Assalamu Alaikum,</p>
                <p>Your 6-digit verification code for Group Campaigns is:</p>
                <h1 style="background: #ECFDF5; color: #047857; padding: 12px 20px; display: inline-block; border-radius: 8px; letter-spacing: 4px;">${otp}</h1>
                <p>This code is valid for 5 minutes. Please do not share it with anyone.</p>
              </div>
            `
          })
        });

        if (!emailResponse.ok) {
          const errText = await emailResponse.text();
          return new Response(JSON.stringify({ error: "Failed to send email", details: errText }), {
            status: 500,
            headers: { ...corsHeaders, "Content-Type": "application/json" }
          });
        }

        return new Response(JSON.stringify({ success: true, message: "OTP sent successfully" }), {
          status: 200,
          headers: { ...corsHeaders, "Content-Type": "application/json" }
        });

      } catch (err) {
        return new Response(JSON.stringify({ error: err.message }), {
          status: 500,
          headers: { ...corsHeaders, "Content-Type": "application/json" }
        });
      }
    }

    // ── 2. VERIFY EMAIL OTP ──
    if (url.pathname === "/verify-email-otp" && request.method === "POST") {
      try {
        const body = await request.json();
        const email = body.email ? body.email.trim().toLowerCase() : "";
        const otp = body.otp ? body.otp.trim() : "";

        const savedOtp = await env.OTP_KV.get(email);

        if (savedOtp && savedOtp === otp) {
          // Verify hote hi OTP delete karein taaki dubara reuse na ho sake
          await env.OTP_KV.delete(email);
          return new Response(JSON.stringify({ verified: true, message: "Email verified successfully" }), {
            status: 200,
            headers: { ...corsHeaders, "Content-Type": "application/json" }
          });
        } else {
          return new Response(JSON.stringify({ verified: false, error: "Invalid or expired OTP" }), {
            status: 400,
            headers: { ...corsHeaders, "Content-Type": "application/json" }
          });
        }
      } catch (err) {
        return new Response(JSON.stringify({ error: err.message }), {
          status: 500,
          headers: { ...corsHeaders, "Content-Type": "application/json" }
        });
      }
    }

    return new Response("Not Found", { status: 404 });
  }
};
```

---

## Part 3: Security & Bot / Abuse Protection

### 1. SMS Region Policy (Bots Se Bachne Ke Liye)
Agar future me aap phone number auth wapas on karte hain, toh international hacker bots aapka bill lakho me na pahuncha sakein:
1. Firebase Console ➔ **Authentication** ➔ **Settings** tab.
2. **SMS Region Policy** par click karein.
3. Select karein: **Allow only specific regions**.
4. Country list me sirf **India (+91)** ko select karein aur Save kar dein.
5. *Result*: Koi bhi foreign number par SMS nahi jayega aur toll-fraud se 100% protection milegi.

### 2. Firebase App Check / Play Integrity (Scripts & Scrapers Block)
1. Firebase Console ➔ Left menu me **App Check** par click karein.
2. **Apps** tab me `com.tasbih.digitaltasbih` par click karein.
3. Provider select karein: **Play Integrity**.
4. Apna SHA-256 daalein aur register karein.
5. *Result*: Sirf Play Store se install hua authentic app hi Firebase services ko call kar sakega. Koi bot ya emulator requests trigger nahi kar payega.

---

## Part 4: UI Button Controls (Apple & Phone Hide Karna)

Abhi testing ke liye dono buttons visible hain. Jab aap unhe hide karna chahein:
- File: `app/src/main/java/com/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrDialogs.kt`
- Line ~1719 par:
  ```kotlin
  val showAppleOption = false  // Set false to hide Apple
  val showPhoneOption = false  // Set false to hide Phone
  ```
- Ya fir aap chat me bol dijiyega: *"Apple aur phone hide kardo"* — mai turant change karke build kar dunga!

---

## ✅ Quick Verification Checklist
- [ ] Firebase me Google Sign-In enable kiya
- [ ] Debug SHA-1 & SHA-256 Firebase me add kiya
- [ ] Naya `google-services.json` app folder me replace kiya
- [ ] Resend.com account banaya aur API key li
- [ ] Cloudflare me `OTP_KV` aur `RESEND_API_KEY` set karke Worker code deploy kiya
- [ ] Firebase me SMS Region Policy sirf India (+91) par restrict ki

