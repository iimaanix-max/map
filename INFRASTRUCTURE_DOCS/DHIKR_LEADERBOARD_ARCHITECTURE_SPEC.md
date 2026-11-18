# 🏆 Dhikr Leaderboard + Weekly Wird: Final Spec V2.1

> **Status:** Final for Implementation (Supersedes v1.0 and V2 draft).  
> **Hard Rule:** `TasbihScreen.kt` and every Tasbih module file stay untouched (read-only access to counter persistence at most).  
> **Authors & Reviewers:** Claude & Gemini (Antigravity)  

---

## 0. Locked Decisions
1. **Cloudflare Account:** Deploy on the primary Cloudflare account (`iimaanix@gmail.com`).
   * Routes: `api.imaanix.in/api/leaderboard/*` and `api.imaanix.in/api/wird*`.
   * Plan: Workers Paid plan ($5/month — 50M writes, 25B reads/month).
2. **Authentication:** Firebase ID token only (`digital-tasbih-19bca`). No HMAC secret inside the APK.
   * Admin actions are checked by Firebase UID on the server (`env.ADMIN_UID`), never by a key shipped in the app.
3. **Weekly Cadence:** Week = Saturday 00:00 UTC to Friday 23:59:59 UTC (Jummah is the grand finale).
   * No daily period and no all-time table on the server; both stay local on the phone.
4. **Opt-In Only:** Only opted-in (`is_public = 1`) users ever send data to the server. Opt-out means no sync and their server rows are deleted.
5. **Weekly Wird:** Admin publishes a weekly Wird plan (what to recite each day), see section 8.

---

## 1. Week Index (Exact Formula)

```javascript
const weekIndex = (ts) => Math.floor((Math.floor(ts / 86400) + 5) / 7);
const weekStart = (w) => (w * 7 - 5) * 86400; // Saturday 00:00 UTC, epoch seconds
```
* **Time Source:** Always use server time (`Math.floor(Date.now() / 1000)`), never client time.
* **Verified Unit Test Vectors:**
  * `Fri 2026-11-13 23:59:59 UTC` (ts: 1794614399) $\rightarrow$ `weekIndex: 2967`
  * `Sat 2026-11-14 00:00:00 UTC` (ts: 1794614400) $\rightarrow$ `weekIndex: 2968`

---

## 2. D1 Schema

```sql
CREATE TABLE IF NOT EXISTS users (
  id TEXT PRIMARY KEY,                      -- Firebase UID
  display_name TEXT NOT NULL,
  avatar_id INTEGER NOT NULL DEFAULT 1,
  is_public INTEGER NOT NULL DEFAULT 0,
  status TEXT NOT NULL DEFAULT 'active',    -- active | hidden | banned (admin moderation)
  country TEXT NOT NULL DEFAULT 'unknown',  -- lowercase 2-letter code (e.g. 'in')
  state TEXT NOT NULL DEFAULT 'unknown',    -- normalized slug (e.g. 'up')
  city TEXT NOT NULL DEFAULT 'unknown',     -- normalized slug (e.g. 'lucknow')
  last_sequence_no INTEGER NOT NULL DEFAULT 0,
  last_sync_at INTEGER NOT NULL DEFAULT 0,
  last_window_end INTEGER NOT NULL DEFAULT 0,
  day_key INTEGER NOT NULL DEFAULT 0,
  day_count INTEGER NOT NULL DEFAULT 0,
  profile_updated_at INTEGER NOT NULL DEFAULT 0,
  created_at INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS dhikr_weekly_stats (
  user_id TEXT NOT NULL,
  week_index INTEGER NOT NULL,
  display_name TEXT NOT NULL,
  avatar_id INTEGER NOT NULL,
  country TEXT NOT NULL,
  state TEXT NOT NULL,
  city TEXT NOT NULL,
  total_count INTEGER NOT NULL DEFAULT 0,
  updated_at INTEGER NOT NULL,
  PRIMARY KEY (user_id, week_index),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Only two indexes. Country and state scopes use the geo index prefix.
CREATE INDEX IF NOT EXISTS idx_weekly_geo ON dhikr_weekly_stats(week_index, country, state, city, total_count DESC);
CREATE INDEX IF NOT EXISTS idx_weekly_global ON dhikr_weekly_stats(week_index, total_count DESC);

CREATE TABLE IF NOT EXISTS geo_places (
  country TEXT NOT NULL, 
  state TEXT NOT NULL, 
  city TEXT NOT NULL,
  PRIMARY KEY (country, state, city)
);

CREATE TABLE IF NOT EXISTS weekly_wird (
  week_index INTEGER PRIMARY KEY,
  plan_json TEXT NOT NULL,
  updated_at INTEGER NOT NULL
);
```

* **Budget:** One sync must write at most 4 rows (stats row + 2 index entries + users row).
* Log `meta.rows_written` in tests and fail if $> 4$.
* At 5,000 DAU $\times$ 5 syncs = ~3M writes/month (well inside 50M paid plan).

---

## 3. POST /api/leaderboard/sync

* **Headers:** `Authorization: Bearer <Firebase ID token>`. No HMAC.
* **Payload:**
  ```json
  {
    "sequence_no": 143,
    "window_start": 1794650160,
    "window_end": 1794651000,
    "added_count": 500
  }
  ```
* **Constants (env vars):**
  * `MAX_BATCH = 2500`
  * `MAX_RATE = 8` (taps/s)
  * `MIN_INTERVAL = 60` (s)
  * `DAILY_CAP = 30000`
  * `SKEW = 120` (s)
  * `MAX_BACKLOG = 604800` (7 days in seconds)

### Validation Order (Strict):
1. **Verify Token:** RS256 signature against Google's Firebase public keys (cached). `aud` and `iss` must match `digital-tasbih-19bca`. `exp` valid, `sub` non-empty. Fail $\rightarrow$ 401.
2. **Load Users Row:** (1 read). Missing, `is_public = 0`, or `status != 'active'` $\rightarrow$ 403.
3. **Shape:** Integers only, `added_count` between 1 and `MAX_BATCH`. Fail $\rightarrow$ 422.
4. **Replay Check:** `sequence_no <= last_sequence_no` $\rightarrow$ Return `200 { "status": "duplicate" }` with **zero writes**.
5. **Rate Limit:** `now - last_sync_at < MIN_INTERVAL` $\rightarrow$ Return `429` with `Retry-After: <seconds>`.
6. **Time Window Verification:**
   * `window_start <= window_end`
   * `window_end <= now + SKEW`
   * `window_start >= now - MAX_BACKLOG`
   * `window_start >= last_window_end - 5`
   * `added_count <= MAX_RATE * (window_end - window_start + 1)`
   * Fail any $\rightarrow$ 422.
7. **Daily Cap:**
   * `day_key = Math.floor(now / 86400)`
   * If `user.day_key === day_key`, `new_day_count = user.day_count + added_count`; else `new_day_count = added_count`.
   * If `new_day_count > DAILY_CAP` $\rightarrow$ 422 `daily_cap`.
8. **Guarded Atomic Batch Write (`env.DB.batch([A, B])`):**
   ```sql
   -- Statement A: Guarded upsert (denormalizes current name/geo from users)
   INSERT INTO dhikr_weekly_stats (user_id, week_index, display_name, avatar_id, country, state, city, total_count, updated_at)
   SELECT id, ?2, display_name, avatar_id, country, state, city, ?3, ?4 FROM users WHERE id = ?1 AND last_sequence_no < ?5
   ON CONFLICT(user_id, week_index) DO UPDATE SET total_count = total_count + excluded.total_count, updated_at = excluded.updated_at;

   -- Statement B: Advance counters
   UPDATE users SET last_sequence_no = ?2, last_sync_at = ?3, last_window_end = ?4, day_key = ?5, day_count = ?6 WHERE id = ?1 AND last_sequence_no < ?2;
   ```
* **Response:** `{ "status": "ok", "synced": 500, "week_index": 2968 }` (No rank calculation).

---

## 4. Profile, Opt-in & Account Deletion

* **`PUT /api/leaderboard/profile` (auth):**
  * Body: `{ is_public, display_name, avatar_id, place: { country, state, city } }`
  * Rate limit: 1 change per 30s (`profile_updated_at`).
  * First call creates the `users` row. Geo comes from `request.cf` (country, region, city), normalized. Later changes allowed only from existing `geo_places` list.
  * Normalization: lowercase, trim, strip diacritics, collapse spaces, max 40 chars; empty becomes `unknown`.
  * Display name: 2 to 24 chars, strip control characters & URLs. Default: `Mumin #` + 4 digits from UID hash.
  * Updating profile updates `users` and current-week `dhikr_weekly_stats` in the same batch.
  * Opting out (`is_public = 0`) deletes all that user's stats rows.
* **`DELETE /api/leaderboard/account` (auth):**
  * Deletes the `users` row (foreign key cascade deletes stats).
* **`PUT /api/admin/user-status` (admin only):**
  * Sets status to `hidden` or `banned`.

---

## 5. Public and Private Reads

### 5.1 `GET /api/leaderboard/rankings?scope=city|state|country|global&country=&state=&city=` (Public, No Auth)
* Cache Key: `week_index + scope + country + state + city`. Ignore all other query params.
* Validation: `scope` enum, `country` 2-letter, place exists in `geo_places` (on cache miss). Unknown place $\rightarrow$ 404 (negative-cache for 60s).
* Edge Cache: `caches.default.match` / `put` with `ctx.waitUntil`, headers: `Cache-Control: public, max-age=180`.
* Query:
  ```sql
  SELECT display_name, avatar_id, total_count 
  FROM dhikr_weekly_stats 
  WHERE week_index = ? AND <scope_filters> 
  ORDER BY total_count DESC 
  LIMIT 100;
  ```
* Ranks computed in code. User IDs never returned.
* Response: `{ week_index, week_ends_at, generated_at, rankings: [...] }`

### 5.2 `GET /api/leaderboard/my-rank?scope=` (Auth, On Demand)
* Triggered only when the leaderboard screen is actively opened.
* Queries: User's `total_count`, `COUNT(*) WHERE total_count > ?`, and `MIN(total_count) WHERE total_count > ?` (distance to next).
* Cached per user for 120s with Cache API key including UID hash.
* Beyond top 1,000 for country/global: show approximate band (e.g. `Top 20%`).

---

## 6. Cron (Weekly Cleanup)

* Trigger: `5 0 * * 6` (Saturday 00:05 UTC).
* SQL: `DELETE FROM dhikr_weekly_stats WHERE week_index < current - 4;` (keeps 4 past weeks).

---

## 7. Android Client Architecture

* **Database:** Separate Room database file `leaderboard.db` containing `LeaderboardSyncBuffer`, `CachedLeaderboard`, and `CachedWird`.
* **Zero Modification to Tasbih Module:**
  * Investigation confirmed: Tasbih counts are stored in `SharedPreferences("tasbih_counter_prefs", Context.MODE_PRIVATE)` under `grand_total`.
  * The `LeaderboardSyncCoordinator` attaches an `OnSharedPreferenceChangeListener` to `tasbih_counter_prefs` in read-only mode.
  * `TasbihScreen.kt` and `TasbihRepository.kt` remain **100% untouched**.
* **Delta Computation:** Reads positive increments of `grand_total`. Decrements or resets are ignored.
* **Buffer Table:** `LeaderboardSyncBuffer(id, added_count, first_tap_ts, last_tap_ts, sent)`. Splits chunks $> 2500$ into non-overlapping consecutive windows.
* **Coordinator:** Mutex-guarded flusher. 60s cooldown between flushes. `WorkManager` unique work with exponential backoff honoring `Retry-After`.
* **Settings Toggle:** "Show my name on the leaderboard" (default OFF, gated to 18+).

---

## 8. Weekly Wird (Daily Recitation Plan)

* **Plan JSON format:**
  ```json
  {
    "week_index": 2968,
    "days": [
      {
        "date": "2026-11-14",
        "title": "Aaj ka Wird",
        "text_ar": "سُبْحَانَ اللّٰهِ وَبِحَمْدِهِ",
        "text_translit": "SubhanAllahi wa Bihamdihi",
        "meaning": "Glory and praise be to Allah",
        "target_count": 100,
        "note": "Recite 100 times after Fajr"
      }
    ]
  }
  ```
* **`PUT /api/admin/wird` (admin only):** Validates 1–7 days, UID === `env.ADMIN_UID`, upserts to `weekly_wird`.
* **`DELETE /api/admin/wird?week_index=` (admin only)**
* **`GET /api/wird` (public, cached max-age=300):** Returns current and next week's plans.
* **Client Card:** Picks entry matching device local date. Display card + button to open Tasbih screen (no counter pre-selection).
* **Admin UI:** In-app screen visible only when signed-in UID matches `ADMIN_UID`.

---

## 9. Privacy & Logging

* Never log IP addresses or raw `request.cf` object.
* Opt-in only with default name "Mumin #XXXX" (respects riya concern).
* Privacy policy update scheduled for lawyer sign-off.

---

## 10. Acceptance Tests

1. `weekIndex` vectors: Fri 23:59:59 $\rightarrow$ 2967, Sat 00:00:00 $\rightarrow$ 2968.
2. Sync: duplicate sequence returns `duplicate` with 0 writes; parallel requests count once; 429 returns `Retry-After`; `meta.rows_written <= 4`.
3. Cheating: overlapping windows, future windows, and velocity $> 8 \text{ taps/s}$ rejected.
4. Opt-out deletes stats rows and evicts cache.
5. Rankings: random city returns 404; repeated requests within 180s cause 0 D1 reads.
6. Wird: non-admin gets 403; local date matching works across IST and US timezones.
7. Load test: 5,000 simulated users $\times$ 5 syncs verified against write budget.

---

## 11. Build Order

1. **Phase 1:** D1 schema, `weekIndex`, Firebase token verification, unit tests.
2. **Phase 2:** Cloudflare Worker endpoints (`sync`, `profile`, `rankings`, `my-rank`, `places`, `cron`).
3. **Phase 3:** Android sync engine in `leaderboard.db` (no UI).
4. **Phase 4:** Leaderboard UI & opt-in settings.
5. **Phase 5:** Wird endpoints, admin screen, Wird card.
6. **Phase 6:** Privacy policy update, load test, and Claude re-audit.
