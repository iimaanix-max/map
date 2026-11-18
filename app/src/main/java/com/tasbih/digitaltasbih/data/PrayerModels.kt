package com.tasbih.digitaltasbih.data

data class PrayerCalculationMethod(
    val id: Int,
    val name: String,
    val params: String,
    val description: String = ""
)

val AvailableCalculationMethods = listOf(
    PrayerCalculationMethod(1, "University of Islamic Sciences, Karachi", "Fajr 18° · Isha 18°", "Recommended for India, Pakistan, Bangladesh, South Asia"),
    PrayerCalculationMethod(3, "Muslim World League", "Fajr 18° · Isha 17°", "Recommended for Europe, Far East, parts of USA"),
    PrayerCalculationMethod(2, "Islamic Society of North America", "Fajr 15° · Isha 15°", "Recommended for USA, Canada, North America"),
    PrayerCalculationMethod(4, "Umm Al-Qura University, Makkah", "Fajr 18.5° · Isha 90 min", "Official method of Saudi Arabia, Arabian Peninsula"),
    PrayerCalculationMethod(8, "Gulf Region", "Fajr 19.5° · Isha 90 min", "United Arab Emirates, Oman"),
    PrayerCalculationMethod(11, "Majlis Ugama Islam Singapura", "Fajr 20° · Isha 18°", "Singapore, Malaysia, Indonesia"),
    PrayerCalculationMethod(13, "Diyanet, Turkey", "Fajr 18° · Isha 17°", "Official Turkey Calculation"),
    PrayerCalculationMethod(15, "Moonsighting Committee Worldwide", "General shafaq", "Worldwide moonsighting calculation")
)

data class PrayerTune(
    val fajr: Int = 0,
    val sunrise: Int = 0,
    val dhuhr: Int = 0,
    val asr: Int = 0,
    val maghrib: Int = 0,
    val sunset: Int = 0,
    val isha: Int = 0,
    val imsak: Int = 0
)

data class PrayerSettingsState(
    val autoLocation: Boolean = true,
    val latitude: Double = 21.0970,
    val longitude: Double = 81.0374,
    val locationLabel: String = "Rajnandgaon, India",
    val methodId: Int = 1,
    val school: Int = 2, // 1 = Shafi/Standard, 2 = Hanafi
    val tune: PrayerTune = PrayerTune(),
    val notificationsEnabled: Boolean = true,
    val warningEnabled: Boolean = true,
    val fixedNotificationEnabled: Boolean = true,
    val soundFajr: Boolean = true,
    val soundDhuhr: Boolean = true,
    val soundAsr: Boolean = true,
    val soundMaghrib: Boolean = true,
    val soundIsha: Boolean = true
)

data class PrayerItem(
    val id: String, // "Fajr", "Dhuhr", "Asr", "Maghrib", "Isha"
    val nameEn: String,
    val nameHi: String,
    val nameUr: String,
    val nameAr: String,
    val time: String, // "05:12"
    val displayTime: String, // "5:12 am"
    val jamatTime: String, // "05:30" or "Unset"
    val iconEmoji: String,
    val isCurrent: Boolean = false,
    val isPassed: Boolean = false,
    val isDone: Boolean = false
) {
    fun getLocalizedName(lang: String): String = when (lang) {
        "en" -> nameEn
        "ur" -> nameUr
        "ar" -> nameAr
        else -> nameHi
    }
}

data class PrayerExtraPhase(
    val id: String,
    val nameEn: String,
    val nameHi: String,
    val nameUr: String,
    val nameAr: String,
    val startTime: String,
    val endTime: String,
    val type: String // "forbidden" (Makrooh), "sunnah" (Ishraq/Chasht), "tahajjud"
) {
    fun getLocalizedName(lang: String): String = when (lang) {
        "en" -> nameEn
        "ur" -> nameUr
        "ar" -> nameAr
        else -> nameHi
    }
}

data class PrayerDayData(
    val dateStr: String, // "15-10-2026"
    val dayOfMonth: Int,
    val monthNumber: Int,
    val year: Int,
    val hijriDay: Int,
    val hijriMonthName: String,
    val hijriMonthAr: String,
    val hijriYear: Int,
    val gregorianReadable: String,
    val timings: Map<String, String>,
    val extraPhases: List<PrayerExtraPhase> = emptyList()
)

data class PrayerActiveMeta(
    val currentPrayer: PrayerItem?,
    val nextPrayer: PrayerItem?,
    val nextPrayerRemainingMs: Long,
    val countdownText: String,
    val progressPercent: Float,
    val spiritualQuoteTitle: String,
    val spiritualQuoteBody: String,
    val activeHighlightPrayerId: String? = null
)

data class HomeAnnouncementConfig(
    val isEnabled: Boolean = false,
    val campaignId: String = "",
    val badgeText: String = "✨ SPECIAL EVENT LIVE",
    val arabicText: String = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
    val title: String = "",
    val prizeTitle: String = "",
    val message: String = "",
    val targetText: String = "",
    val buttonText: String = "Alhamdulillah! Shamil Huein ✓",
    val version: Long = 0L
)
