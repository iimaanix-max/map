# Executive Summary: Privacy & Data-Flow Audit

**Product:** Digital Tasbih by Imaanix (`com.tasbih.digitaltasbih`)  
**Audience:** Founders, Leadership, Lead Engineers, and Legal Counsel  
**Date:** November 2026  

---

### 1. Key Takeaways
1. **Core Islamic Utility is Truly Account-Free & Local:**
   The offline Tasbih counter, beads simulation (`RealisticBeadsCanvas`), daily athkar, offline Quran reading, and Salah tracking store 100% of counts and badges on the device. No personal data is sent to the cloud for core local usage.
2. **Community & Umrah Contest Collect Personal Contact Info:**
   When users join the Mega Umrah Contest or live dhikr rooms, their full name, plain-text phone number (`+91...`), normalized email, and city are collected in Cloud Firestore (`campaign_contestants/{id}/entries/{uid}`) and Cloudflare D1 SQL (`otp_records.email`).
3. **In-App Account Deletion is Currently Simulated:**
   In `SettingsScreen.kt#L1151`, the "Delete My Data" modal executes `isSubmitted = true` without sending any network request to Cloudflare or deleting records from D1 or Firestore.
4. **Google Play Compliance Priority:**
   Google Play requires:
   - Accurate disclosure of all collected data (Name, Phone, Email, Location, AdMob AAID, Crashlytics).
   - An active **web URL** where users can submit account and data deletion requests (e.g., `https://imaanix.in/delete-account`).
5. **DPDP Act (India) Alignment:**
   The codebase already includes an 18+ age gate self-declaration (`dt_user_is_18_plus`) preventing minors from joining live community rooms and contest draws. This cleanly shields under-18 users from behavioural tracking and advertising data processing until India DPDP enforcement (May 2027).

---

### 2. Immediate Action Checklist

| Priority | Area | Required Action | Status in Code |
| :---: | :--- | :--- | :---: |
| 🔴 **URGENT** | **Account Deletion** | Deploy a public web page (`https://imaanix.in/delete-account`) and add a real deletion endpoint in `campaigns-worker.js` / `auth-worker.js`. | Simulated in app |
| 🔴 **URGENT** | **Privacy Policy Text** | Correct `privacy-policy.html`: remove claims of "encrypted SharedPreferences" and "automated 30-day purge". | Documented |
| 🟡 **HIGH** | **Auto-Backup Rule** | Update `backup_rules.xml` to exclude sensitive SharedPreferences files (`dt_gc_phone`, `dt_gc_name`). | Leaks to G-Drive |
| 🟡 **HIGH** | **Google Play Console** | Fill out the Data Safety form exactly per Deliverable 7 of the audit report. | Ready to submit |
| 🟢 **NORMAL** | **D1 Database Maintenance**| Add a scheduled Cloudflare Worker cron job to purge expired D1 `otp_records`. | Manual overwrite only |

---

### 3. File Directory
- **Full Report (10 Deliverables):** `PRIVACY_DATA_AUDIT_REPORT/docs/DIGITAL_TASBIH_PRIVACY_AUDIT.md`
- **Visual Diagrams:** `PRIVACY_DATA_AUDIT_REPORT/diagrams/`
- **Official PDF File:** `PRIVACY_DATA_AUDIT_REPORT/pdf/Digital_Tasbih_Privacy_and_Data_Flow_Audit_Report.pdf`

