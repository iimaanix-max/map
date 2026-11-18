# 🌟 THE NATIVE: MASTER ARCHITECTURE & AI PROJECT GUIDE
**Application Name:** Digital Tasbih & Noble Quran Native ("The Native")  
**Package Identifier:** `com.tasbih.digitaltasbih`  
**Platform:** Android Native (100% Jetpack Compose & Kotlin)  
**Primary Language:** Kotlin (with Gradle Kotlin DSL `.gradle.kts`)  
**Target Android Version:** Android 14+ (API 34+), Min SDK: Android 7.0 (API 24)  
**Main Architect & Owner:** Henriey  
**Backup Location:** `D:\Henriey's Native app`  

---

## ⛔ CRITICAL PERMANENT DIRECTIVE: TASBIH SECTION IS LOCKED & FROZEN
> [!CAUTION]
> **STRICT USER DIRECTIVE (Permanent Rule for ALL AI Assistants & Developers):**  
> The **Tasbih** module and everything inside it is completely finalized, verified, and locked.  
> **ABSOLUTELY NO AI ASSISTANT OR DEVELOPER MAY MODIFY, REFACTOR, REORGANIZE, CLEAN UP, OR TOUCH ANY PART OF THE TASBIH SECTION UNLESS THE USER EXPLICITLY AND UNAMBIGUOUSLY REQUESTS A DIRECT CHANGE TO TASBIH IN CHAT.**

### Specific Files & Subsystems Protected Under This Freeze:
1. `app/src/main/java/com/tasbih/digitaltasbih/ui/screens/tasbih/TasbihScreen.kt` (Entire file).
2. **All Counter Logic:** Current session count, Today's total, All-time total, and Dhikr lifetime badges.
3. **Reset Logic:** Reset must ONLY reset current session count to 0, NEVER touching dhikr badges or lifetime stats.
4. **Realistic Beads Counter Canvas (`RealisticBeadsCanvas`):**
   - Exact Bézier curve trajectory mathematics ($P_0, P_1, P_2$).
   - 11-slot configuration with 7 visible slots: 3 on left (Slots 0, 1, 2) and 4 on right (Slots 7, 8, 9, 10).
   - Easing and animation timing (`naturalTasbihSlide` over 440ms).
   - Slide transition physics from right-to-left across the center thread gap.
   - Zero touch ripple / no black highlight box on tap.
   - Subtle radial gradient glow on active bead (no hard golden outline rings, no flat solid discs).
5. **Digital Dial Counter:** Touch interaction, dial progress ring, tap to count.
6. **Dhikr Selection & Management:** Targets, themes/skins (Colours & Marbles), sound, haptics.
7. **Floating Switch Controls:** Toggle between Beads (⭕) and Dial modes, Undo, Reset, Save layout.

---

## 📖 PROJECT OVERVIEW & VALUE PROPOSITION
**The Native** is a production-grade, all-in-one Islamic lifestyle, worship, and study application built natively for Android using modern Jetpack Compose. It brings together world-class UI design, fluid 60fps animations, rich offline datasets, dual-engine Quran audio, authentic daily hadiths, accurate prayer times, and live global dhikr campaigns.

### Key Pillars:
1. **Realistic 3D-Feel Tasbih & Digital Counter:** Tactile, authentic physical bead physics and minimalist digital dial.
2. **Noble Quran Reader (List View & Real 15-Line Mushaf):**
   - Complete Surah and Juz navigation.
   - **Word-Tap Recitation & Meaning:** Tapping any word recites that exact word and displays its translation popup.
   - **Tajweed & Waqf Pause Coloring:** Stop (Waqf) signs and pronunciation (Tajweed) rules rendered in distinct contextual colors.
   - **Interactive Tajweed/Waqf Guide Modal:** Explains every symbol in 4 languages (English, Hindi, Urdu, Arabic).
   - **12 Beautiful Mushaf Themes:** Classic Cream, Madinah Green, Royal Emerald, Night Sky, Rose Sepia, Dark Charcoal, Parchment, etc.
   - **115 Tanzil Translation Database:** Multi-lingual offline and on-demand translation support.
   - **Standalone Translation Audio & Floating Hover Mini-Player:** Tapping any translation text plays ONLY that Ayah's studio voice (Shamshad Ali Khan for Hindi/Urdu, Ibrahim Walk for English) with an emerald highlight and an adaptive floating bottom mini-player (Play/Pause, Stop, Close, progress bar) that handles both 3-button navigation and gesture navigation without overlap.
3. **Prayer Times & Adhan Engine:** Highly accurate astronomical solar calculation with custom juristic methods, Adhan audio playback, and notification scheduling.
4. **Smooth Qibla Compass:** Sensor fusion (accelerometer + magnetometer) with magnetic declination correction and 40+ custom compass rose skins.
5. **Hisnul Muslim Duas & Adhkar:** 38 categorized Dua topics, Morning & Evening Adhkar with counter, transliteration, and audio.
6. **Hijri Calendar & Ramadan Mode:** Umm al-Qura lunar date sync, fasting Sehri/Iftar countdowns, and White Days (Ayyam al-Beed).
7. **366 Authentic Daily Hadiths:** Leap-year daily hadith engine with Bukhari/Muslim citations.
8. **Live Global Dhikr Campaigns:** Cloudflare Worker & D1/KV real-time backend for community dhikr tracking.

---

## 🏛 ARCHITECTURE & TECH STACK

```
com.tasbih.digitaltasbih/
├── data/
│   ├── model/               # Data classes (Quran, Duas, Prayer, Hadith, Themes)
│   └── repository/          # Data repositories & offline caches
├── service/                 # Audio players, notification receivers, workers
├── ui/
│   ├── components/          # Reusable Compose widgets (Banners, Cards, Dialogs)
│   ├── navigation/          # Compose Navigation routes & bottom bar
│   ├── screens/             # Feature screens
│   │   ├── adhkar/          # Morning/Evening Adhkar & daily supplications
│   │   ├── calendar/        # Hijri lunar calendar & Ramadan fasting tracker
│   │   ├── compass/         # Qibla compass & sensor visualizer
│   │   ├── duas/            # 38 Dua categories from Hisnul Muslim
│   │   ├── hadith/          # 366 Authentic Daily Hadith viewer
│   │   ├── prayer/          # Prayer times schedule & adhan audio settings
│   │   ├── quran/           # Quran reader (List & Mushaf, Audio, Themes, Waqf)
│   │   └── tasbih/          # ⛔ LOCKED: Beads canvas & digital counter
│   └── theme/               # Islamic color schemes, typography, typography shapes
└── util/                    # Waqf/Tajweed coloring, astronomical math, haptics
```

### Technology Highlights:
- **UI Framework:** 100% Jetpack Compose with Material 3.
- **State Management:** Android ViewModel + Kotlin StateFlow / SharedFlow.
- **Audio Engines:**
  - `QuranAudioPlayer.kt`: Ayah-by-ayah and word-by-word instant playback using Android MediaPlayer / ExoPlayer.
  - `QuranSurahAudioEngine.kt`: Continuous streaming surah player supporting world-renowned reciters (Mishary Alafasy, Abdul Basit, Minshawi, etc.).
- **Local Storage & Offline Bundles:** SharedPreferences, Room/JSON asset files in `app/src/main/assets/`.
- **Sensors:** Android `SensorManager` with Low-Pass filter and geomagnetic field vector rotation.
- **Monetization:** Google Mobile Ads (AdMob) SDK with Native Advanced Ads, Interstitials, Rewarded Ads, and Google UMP Consent.

---

## 📂 COMPLETE BACKUP DIRECTORY GUIDE (`D:\Henriey's Native app`)

| Directory / File | Description |
|---|---|
| `app/` | **Full Android Application Source Code** (`src/main/java`, `res/`, `assets/`, `AndroidManifest.xml`). |
| `gradle/` & `gradlew` | Gradle wrapper (v8.x) for deterministic, reproducible builds without IDE dependencies. |
| `build.gradle.kts` & `settings.gradle.kts` | Root project Gradle build scripts using Kotlin DSL. |
| `apk/app-debug.apk` | Ready-to-install, fully verified debug APK compiled from the current source. |
| `Tanzil Translations (115 Languages)/` | Complete archive of 115 official Tanzil Quran translation text files. |
| `extra_data/` | Raw JSON datasets: `366_authentic_daily_hadiths.json` and 38 category `Duas json file/`. |
| `CLAUDE_AUDIT_PACKAGE/` | Cloudflare Workers backend code, schema, and live Dhikr API endpoints. |
| `group-campaigns-backend/` | Alternative backend worker scripts and deployment guides for live group campaigns. |
| `compass_designs/` | 44 custom high-resolution compass needle and dial artwork files. |
| `notification_sounds/` | Audio assets for Adhan and prayer notifications. |
| `share_posters/` | High-resolution shareable posters for morning/evening adhkar in EN, HI, UR, AR. |
| `extra_project_resources/` | Dhikr Tree 24-stage growth spritesheet sequence and additional Adhan recordings. |
| `admin.html` | Interactive web dashboard for managing live global dhikr campaigns. |
| `privacy-policy.html` | Official privacy policy page ready for Google Play Store compliance. |
| `Open_In_Android_Studio.bat` | One-click script to launch the project directly into Android Studio. |
| `GEMINI.md` | Strict rules file containing the **Tasbih Freeze Directive**. |
| `MASTER_AI_PROJECT_GUIDE.md` | This document: Comprehensive architecture, feature explanation, and AI guidelines. |

---

## 🛠 HOW TO BUILD & RUN

### 1. In Android Studio:
1. Double-click `Open_In_Android_Studio.bat` or open `D:\Henriey's Native app` in Android Studio (Ladybug or newer).
2. Wait for Gradle Sync to finish.
3. Select an emulator (e.g., Pixel 9 Pro with API 34+) or a physical device.
4. Press **Shift + F10** or click **Run 'app'**.

### 2. From Command Line (Terminal / PowerShell):
```powershell
cd "D:\Henriey's Native app"
.\gradlew.bat assembleDebug
```
The output APK will be generated at:
`app/build/outputs/apk/debug/app-debug.apk`

---

## ⚠️ STRICT RULES FOR FUTURE DEVELOPERS & AI ASSISTANTS

1. **NEVER TOUCH TASBIH:** Do not refactor, rename, format, or modify `TasbihScreen.kt` or any beads canvas math.
2. **PRESERVE WORD-TAP IN QURAN:** In Quran Reader (both List View and Mushaf View), tapping an individual word MUST trigger audio pronunciation and open the translation/meaning card. Never disable this interaction.
3. **PRESERVE WAQF & TAJWEED COLORING:** Waqf stop signs and Tajweed letter coloring must continue using `TajweedWaqfHelper.kt`. The guide modal must stay fully localized.
4. **NO BREAKING COMPONENT CHANGES:** When enhancing any screen (Duas, Prayer, Calendar, Hadith), ensure backwards compatibility with saved SharedPreferences and local states.
5. **VERIFY BUILDS:** Always test compilation using `./gradlew assembleDebug` before committing changes.
