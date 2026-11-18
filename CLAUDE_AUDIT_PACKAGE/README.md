# Digital Tasbih - Cloudflare Backend & Android Audit Package (Rev 7.0 Final)

This folder contains the complete, standalone, production-ready code files for the final deployment pass:

1. **`campaigns-worker.js`**
   - Public Feed with `specialCampaigns`, `liveCount`, and `completedCount` (returns 502 on double query failure to avoid caching errors)
   - Real JWKS user JWT authentication (`crypto.subtle.verify`)
   - Leaderboard & Participants with `parent` strictly in REST URL path, 502 on query failure (never caches errors)
   - `getCampaignTotal` with 15s edge caching and `getFirestoreDoc` helper
   - Atomic tap submission with D1 speed-cap check and exact timestamp compensation
   - `createGroup`:
     - Google Sign-In check executed FIRST (blocks anonymous 403 before consuming IP quota)
     - IP rate limit (10/hour) executed SECOND
     - Per-UID rate limit (5/day) executed THIRD
     - Dual `currentDocument: { exists: false }` preconditions
     - Response stripped of creatorUid via `filterPublicCampaign(newCampaign, true)`
   - `joinGroupByCode` with length-based code parsing, 10-min rate limits, and `exists: true` update precondition
   - `registerMegaContest` with IST midnight cutoff, 5/hour uid rate limit, full HMAC hashing (`email:`, `phone:`, `device:`, `ip:`), and post-commit sybil transform detection
   - `adminPurgeContest` with `pageSize=80`, `pageToken` support, and `nextPageToken` response
   - Service account credentials fallback supporting both `FIREBASE_SERVICE_ACCOUNT_JSON` and `GCP_CLIENT_EMAIL` / `GCP_PRIVATE_KEY`

2. **`auth-worker.js`**
   - Real Google RS256 JWKS signature verification (`crypto.subtle.verify`) with 5-minute throttled key caching
   - Hourly CGNAT IP rate limiting (30/hour bucket) + 60s cooldown per email (5/day) + atomic global daily cap (80/day)
   - Gmail dot & plus-tag normalization (`user.replace(/\./g, "").split("+")[0]`)
   - Anti-overwrite WHERE clause on OTP storage (`WHERE uid = ?2 OR expires_at < ?4`)
   - Single-use atomic deletion on verification (`RETURNING *`)
   - Safe rollback helper with `MAX(0, request_count - 1)`
   - UID preservation: `mintFirebaseCustomToken(email, requesterUid, env)` retains anonymous user UID
   - Scheduled daily cleanup handler at 3 AM UTC for all 3 D1 tables (`otp_records`, `rate_limits`, `user_submit_cooldowns`)
   - Verified sender `noreply@imaanix.in` with 5-minute TTL

3. **`campaigns-wrangler.toml`** & **`auth-wrangler.toml`**
   - Cloudflare Worker configuration routed to `api.imaanix.in/campaigns/*` and `api.imaanix.in/auth/*`
   - Redundant cron trigger removed from campaigns worker (scheduled cleanup is handled cleanly by auth worker)

4. **`schema.sql`**
   - Full D1 SQL schema with clean table definitions ready for immediate execution on fresh D1 databases

5. **`GroupCampaignRepository.kt`**
   - Full Android client repository:
     - Endpoints configured to `https://api.imaanix.in/campaigns/` and `https://api.imaanix.in/auth/`
     - Exact server error messages propagated in `submitCount` (e.g. rate limit cooldown alerts)
     - Clean E.164 phone formatting with user-selected `dialCode`
     - Zero fake ticket fallback in `registerMegaContest` (fails closed if server ticket missing)
     - `sendEmailOtp` and `verifyEmailOtp` hitting Auth Worker and signing in with custom token
     - `linkAnonymousAccountWithCredential(credential)` preserving anonymous user UID on Google Sign-In

6. **`LiveDhikrScreen_submitBatch.kt`**
   - Tap preservation on transient failure: restores unsubmitted taps into `pendingCount` only on network error or 429 rate limit (never loops permanently on 400/403/404)
   - `maxBatchSize` clamped to 50 taps to strictly respect D1 atomic speed limits
