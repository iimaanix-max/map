# Project Rules & Strict Constraints

## ⛔ CRITICAL: TASBIH SECTION IS LOCKED & FROZEN
**STRICT USER DIRECTIVE (Permanent Rule):**
The **Tasbih** module and everything inside it is completely finalized, verified, and locked.

**ABSOLUTELY NO AI ASSISTANT OR DEVELOPER MAY MODIFY, REFACTOR, REORGANIZE, CLEAN UP, OR TOUCH ANY PART OF THE TASBIH SECTION UNLESS THE USER EXPLICITLY AND UNAMBIGUOUSLY REQUESTS A DIRECT CHANGE TO TASBIH IN CHAT.**

This lock strictly covers:
1. `app/src/main/java/com/tasbih/digitaltasbih/ui/screens/tasbih/TasbihScreen.kt` (Entire file).
2. All Counter Logic: Session count, Today's total, All-time total, and Dhikr lifetime badges.
3. Reset Logic: Reset must ONLY reset current session count to 0, NEVER touching dhikr badges or lifetime stats.
4. Beads Counter (`RealisticBeadsCanvas`):
   - Bézier curve math ($P_0, P_1, P_2$).
   - 11-slot configuration with 7 visible slots: 3 on left (Slots 0, 1, 2) and 4 on right (Slots 7, 8, 9, 10).
   - Easing and animation timing (`naturalTasbihSlide` over 440ms).
   - Slide transition from right-to-left across the center thread gap.
   - Zero touch ripple / no black highlight box on tap.
   - Subtle radial gradient glow on active bead (no hard golden outline rings, no flat solid discs).
5. Digital Dial Counter: Touch interaction, dial progress ring, tap to count.
6. Dhikr selection, targets, themes/skins (Colours & Marbles), sound, haptics.
7. Floating switch buttons (⭕ / dial toggle), Undo, Reset, Save layout.

**Under no circumstances should any automated refactoring, code cleanup, lint fix, or other feature work touch or modify anything in the Tasbih section.**

---

## 🏗️ Cloudflare Infrastructure & Multi-Account Directory
- **Documentation Root:** `INFRASTRUCTURE_DOCS/MASTER_CLOUDFLARE_WORKERS_ARCHITECTURE.md` and `D:\Henriey's Native app\INFRASTRUCTURE_DOCS\`.
- **4 Cloudflare Accounts:**
  1. `iimaanix@gmail.com` (`ca1f76e46a9cacdcd797a4afc2912328`, `api.imaanix.in`, `join.imaanix.in`): `digital-tasbih-auth`, `digital-tasbih-campaigns`.
  2. `abdulquadirqureshi3@gmail.com` (`35e47141a892f9020da19643ef4e4d8b`, `digitaltasbih.workers.dev`): `quran-proxy` with atomic D1 (`quran-proxy-db`).
  3. `abdulquadirqureshi@gmail.com` (`77122f9454e0ef05b9b4d2bc9f55aeac`, `hadithproxy.workers.dev`): `hadith-proxy` with atomic D1 (`hadith-proxy-db`).
  4. `taykaro786` (`digitaltasbihimagesonly.taykaro786.workers.dev`): Static assets worker (unlimited free).
- **Sender Addresses:**
  - `alerts@imaanix.in` (Name: Digital Tasbih Sentinel) - 90k/98.5k quota alerts & OTP thresholds (350/390).
  - `reports@imaanix.in` (Name: Digital Tasbih Master Intelligence) - Daily reports.
  - `noreply@imaanix.in` (Name: Digital Tasbih) - User login OTPs.
- **Deep Link Base:** `https://join.imaanix.in`
- **Brevo SMTP Relay:** `smtp-relay.brevo.com:587`, User: `c19c06001@smtp-brevo.com` for `support@`, `business@`, `help@`, `contact@imaanix.in`.

---

## 📖 Quran Sharif: Standalone Translation Audio & Floating Hover Mini-Player
1. **Translation Click Directive:**
   - When the user taps on any Translation text (Hindi, Urdu, English) in Quran Reader (`QuranReaderScreen.kt` - both List View and 15-Line Mushaf Page View):
     - **ONLY the translation audio plays** (Arabic recitation must not play).
     - The active Ayah's translation card/text is highlighted with an emerald border + pulsing speaker icon.
2. **Approved Authentic Studio Voices:**
   - **Hindi & Urdu:** **Shamshad Ali Khan** (`https://everyayah.com/data/translations/urdu_shamshad_ali_khan_46kbps/{sPad}{aPad}.mp3`) — universally accepted classical Hindustani studio voice.
   - **English:** **Ibrahim Walk** (`https://everyayah.com/data/English/Sahih_Intnl_Ibrahim_Walk_192kbps/{sPad}{aPad}.mp3`) — standard Saheeh International voice.
3. **Floating Hover Mini-Player (`QuranTranslationFloatingBar.kt`):**
   - Automatically slides up from bottom upon tapping any translation text.
   - Uses `Modifier.navigationBarsPadding()` so it dynamically floats above **3-Button Navigation** (`◀ ● ■`, 48-56dp) or **Gesture Navigation** (16-24dp) on all Android devices without overlap.
   - Reader `LazyColumn` has dynamic bottom padding (`110.dp`) when active so the last verse scrolls completely clear of the floating bar.
   - Controls: Play/Pause toggle, Stop button (`⏹`), Close/Dismiss button (`✕`), Ayah & Language label, Reciter studio credit, audio progress track.



