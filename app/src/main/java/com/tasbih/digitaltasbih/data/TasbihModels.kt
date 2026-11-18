package com.tasbih.digitaltasbih.data

/**
 * Data models for Tasbih Counter, Marbles, Darood Sharif, 99 Names, and Streaks
 */
data class TasbihDhikrItem(
    val id: String,
    val arabic: String,
    val localizedNames: Map<String, String>,
    val localizedMeanings: Map<String, String>,
    val icon: String = "✨",
    val defaultTarget: Int = 33,
    val isCustom: Boolean = false,
    val order: Int = 99
) {
    fun getName(lang: String): String {
        return localizedNames[lang] ?: localizedNames["en"] ?: id
    }

    fun getMeaning(lang: String): String {
        return localizedMeanings[lang] ?: localizedMeanings["en"] ?: ""
    }
}

data class BeadMarbleSkin(
    val id: String,
    val name: String,
    val family: String, // "wood", "stone", "metal", "glass"
    val baseColor: String,
    val highlightColor: String,
    val shadowColor: String,
    val tier: String = "free", // "free", "premium"
    val image: String = ""
)

data class DaroodGuideItem(
    val id: String,
    val title: Map<String, String>,
    val arabic: String,
    val transliteration: Map<String, String>,
    val translation: Map<String, String>,
    val benefits: Map<String, String>,
    val hadithRef: String,
    val defaultTarget: Int = 33,
    val hadithArabic: String = "",
    val hadithTranslations: Map<String, String> = emptyMap(),
    val hadithNarrator: Map<String, String> = emptyMap(),
    val hadithBook: String = "",
    val hadithGrade: String = "Sahih",
    val hadithUrl: String = ""
) {
    fun getTitle(lang: String): String = title[lang] ?: title["en"] ?: ""
    fun getTranslit(lang: String): String = transliteration[lang] ?: transliteration["en"] ?: ""
    fun getTranslation(lang: String): String = translation[lang] ?: translation["en"] ?: ""
    fun getBenefit(lang: String): String = benefits[lang] ?: benefits["en"] ?: ""
    fun getNarrator(lang: String): String = hadithNarrator[lang] ?: hadithNarrator["en"] ?: hadithNarrator["hi"] ?: hadithNarrator["ur"] ?: ""

    fun toGuideDetail(lang: String = "en"): DhikrGuideDetail {
        return DhikrGuideDetail(
            id = id,
            arabic = arabic,
            title = title,
            meaning = translation,
            targetStr = defaultTarget.toString(),
            howToRecite = emptyMap(),
            hadithRef = hadithRef,
            virtues = benefits,
            transliteration = transliteration,
            hadithArabic = hadithArabic.ifBlank { arabic },
            hadithTranslations = if (hadithTranslations.isNotEmpty()) hadithTranslations else translation,
            hadithNarrator = getNarrator(lang),
            hadithBook = hadithBook.ifBlank { hadithRef },
            hadithGrade = hadithGrade,
            hadithUrl = hadithUrl.ifBlank { "https://sunnah.com/search?q=" + java.net.URLEncoder.encode(arabic, "UTF-8") }
        )
    }
}

data class AsmaNameItem(
    val number: Int,
    val arabic: String,
    val transliteration: String,
    val meanings: Map<String, String>
) {
    fun getMeaning(lang: String): String = meanings[lang] ?: meanings["en"] ?: ""
}

data class NamesBenefitInfo(
    val title: String,
    val items: List<String>,
    val reference: String
)

val NAMES_BENEFIT = mapOf(
    "en" to NamesBenefitInfo(
        title = "📿 Benefits of Reciting the 99 Names",
        items = listOf(
            "Whoever memorizes all 99 Names will enter Paradise",
            "Their recitation brings peace and tranquility to the heart",
            "Each Name opens a unique door of supplication to Allah",
            "Established in the Quran and authentic Hadith"
        ),
        reference = "Sahih Bukhari 6410 — Sahih Muslim 2677"
    ),
    "hi" to NamesBenefitInfo(
        title = "📿 99 नाम पढ़ने के फ़ायदे",
        items = listOf(
            "जो 99 नाम याद करे वह जन्नत में दाखिल होगा",
            "इन नामों का ज़िक्र दिल को सुकून देता है",
            "हर नाम एक अलग दुआ का दरवाज़ा खोलता है",
            "यह क़ुरआन व हदीस से साबित है"
        ),
        reference = "Sahih Bukhari 6410 — Sahih Muslim 2677"
    ),
    "ur" to NamesBenefitInfo(
        title = "📿 99 نام پڑھنے کے فوائد",
        items = listOf(
            "جو 99 نام یاد کرے وہ جنت میں داخل ہوگا",
            "ان ناموں کا ذکر دل کو سکون دیتا ہے",
            "ہر نام دعا کا ایک خاص دروازہ کھولتا ہے",
            "قرآن و حدیث سے ثابت ہے"
        ),
        reference = "صحیح بخاری 6410 — صحیح مسلم 2677"
    ),
    "ar" to NamesBenefitInfo(
        title = "📿 فضل قراءة الأسماء الحسنى",
        items = listOf(
            "من أحصاها دخل الجنة",
            "تورث طمأنينة القلب وسكينته",
            "كل اسم باب من أبواب الدعاء",
            "ثابتة في القرآن والسنة الصحيحة"
        ),
        reference = "صحيح البخاري 6410 — صحيح مسلم 2677"
    ),
    "bn" to NamesBenefitInfo(
        title = "📿 ৯৯ নাম পাঠের ফজিলত",
        items = listOf(
            "যে ৯৯ নাম মুখস্থ করবে সে জান্নাতে প্রবেশ করবে",
            "এই নাম পাঠে হৃদয়ে প্রশান্তি আসে",
            "প্রতিটি নাম দুআর এক বিশেষ দ্বার খুলে দেয়",
            "কুরআন ও সহীহ হাদিসে প্রমাণিত"
        ),
        reference = "সহীহ বুখারী 6410 — সহীহ মুসলিম 2677"
    )
)

data class DhikrHistoryItem(
    val id: String,
    val dhikrId: String,
    val dhikrName: String,
    val arabic: String,
    val count: Int,
    val timestamp: Long,
    val dateDisplay: String,
    val icon: String = "✨"
)

data class DhikrGuideDetail(
    val id: String,
    val arabic: String,
    val title: Map<String, String>,
    val meaning: Map<String, String>,
    val targetStr: String,
    val howToRecite: Map<String, String>,
    val hadithRef: String,
    val virtues: Map<String, String>,
    val transliteration: Map<String, String> = emptyMap(),
    val hadithArabic: String = "",
    val hadithTranslations: Map<String, String> = emptyMap(),
    val hadithNarrator: String = "",
    val hadithBook: String = "",
    val hadithGrade: String = "Sahih",
    val hadithUrl: String = "",
    val order: Int = 99
) {
    val narrator: String get() = hadithNarrator
    fun getTitle(lang: String): String = title[lang] ?: title["hi"] ?: title["en"] ?: title["ur"] ?: ""
    fun getMeaning(lang: String): String = meaning[lang] ?: meaning["hi"] ?: meaning["en"] ?: meaning["ur"] ?: ""
    fun getTranslit(lang: String): String = transliteration[lang] ?: transliteration["en"] ?: transliteration["hi"] ?: transliteration["ur"] ?: ""
    fun getHowTo(lang: String): String = howToRecite[lang] ?: howToRecite["hi"] ?: howToRecite["en"] ?: howToRecite["ur"] ?: ""
    fun getVirtues(lang: String): String = virtues[lang] ?: virtues["hi"] ?: virtues["en"] ?: virtues["ur"] ?: ""
    fun getHadithTranslation(lang: String): String = hadithTranslations[lang] ?: hadithTranslations["hi"] ?: hadithTranslations["en"] ?: hadithTranslations["ur"] ?: getVirtues(lang)
}

data class MedallionAchievement(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val currentProgress: Int,
    val maxProgress: Int,
    val category: String, // "streaks", "volume", "habits"
    val isUnlocked: Boolean
)
