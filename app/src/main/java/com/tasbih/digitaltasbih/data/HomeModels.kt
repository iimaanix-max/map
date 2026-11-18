package com.tasbih.digitaltasbih.data

data class HomePrayerItemSample(
    val id: String,
    val name: String,
    val time: String,
    val isPast: Boolean = false,
    val isActive: Boolean = false
)

data class FeatureTile(
    val id: String,
    val title: String,
    val assetPath: String,
    val isFeatured: Boolean = false,
    val hasBadge: Boolean = false,
    val isPulse: Boolean = false
)

data class AllahNameData(
    val number: Int,
    val arabic: String,
    val transliteration: String,
    val meaning: String
)

data class IslamicEventData(
    val id: String,
    val title: String,
    val hijriDate: String,
    val gregorianDate: String,
    val daysRemaining: Int
)

data class DailyDuaData(
    val id: String = "dua_v_077",
    val arabic: String,
    val transliteration: String,
    val translation: String,
    val reference: String,
    val hadithBook: String = "Sahih al-Bukhari 1377",
    val hadithGrade: String = "✓ Sahih al-Bukhari",
    val narrator: String = "Abu Hurairah (RA)",
    val sunnahUrl: String = "https://sunnah.com/bukhari:1377"
)

data class LanguageItem(
    val code: String,
    val label: String
)
