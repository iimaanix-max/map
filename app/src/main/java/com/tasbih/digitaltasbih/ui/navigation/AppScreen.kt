package com.tasbih.digitaltasbih.ui.navigation

sealed class AppScreen {
    object Home : AppScreen()
    object QuranMain : AppScreen()
    data class QuranReader(val type: String, val number: Int) : AppScreen()
    object QuranSettings : AppScreen()
    object ChooseReciter : AppScreen()
    object QuranAudio : AppScreen()
    object AdhanAudio : AppScreen()
    object PrayerTimes : AppScreen()
    object SalahTracker : AppScreen()
    object MorningAdhkar : AppScreen()
    object EveningAdhkar : AppScreen()
    object DhikrTracker : AppScreen()
    data class Tasbih(val initialTab: Int = 0) : AppScreen()
    data class DaroodSharif(val initialDaroodId: String? = null) : AppScreen()
    object Qibla : AppScreen()
    object Hadith : AppScreen()
    object Calendar : AppScreen()
    object Settings : AppScreen()

    // ── Zakat Calculator Dedicated Screen ──
    object ZakatCalculator : AppScreen()

    // ── Shahada (The 1st Pillar of Islam) Dedicated Screen ──
    object Shahada : AppScreen()

    // ⭐⭐ Salah (The 2nd Pillar of Islam) Dedicated Screen ⭐⭐
    object SalahPillar : AppScreen()

    // 🌙 Sawm / Fasting (The 4th Pillar of Islam) Dedicated Screen
    object Sawm : AppScreen()

    // 🕋 Hajj (The 5th Pillar of Islam) Dedicated Screen
    object Hajj : AppScreen()

    // ── Live Dhikr / Group Campaigns ──
    data class LiveDhikr(val autoJoinCode: String? = null) : AppScreen()

    // ── Jummah Mubarak Screen ──
    object Jummah : AppScreen()

    // ── Dua Section Screens ──
    object DuaHome : AppScreen()
    data class DuaCategory(val categoryId: String, val categoryName: String = "", val icon: String = "") : AppScreen()
    data class DuaDetail(val duaId: String, val categoryName: String = "") : AppScreen()
    object DuaAllFeelings : AppScreen()
    object DuaAllCategories : AppScreen()
    object DuaMyDua : AppScreen()
    object DuaMyLists : AppScreen()

    // ── Ramadan Hub Screen ──
    object Ramadan : AppScreen()

    // ── 99 Names of Allah Dedicated Screen ──
    object NamesOfAllah : AppScreen()

    // ── Quran Tracker Dedicated Screen ──
    object QuranTracker : AppScreen()

    // ── Quran Read Time & Analytics Dedicated Screen ──
    object QuranReadTime : AppScreen()

    // ── Find Nearby Mosques & Live Route Navigator ──
    object FindMosque : AppScreen()
}
