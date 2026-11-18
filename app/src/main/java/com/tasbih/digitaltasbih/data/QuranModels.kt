package com.tasbih.digitaltasbih.data

enum class QuranReadingMode(val label: String, val desc: String) {
    LIST("List", "Ayat by Ayat Cards"),
    TRANSLATION("Translation", "Flowing Text Only"),
    MUSHAF("Real Mushaf Page", "15-Line Book Page")
}

enum class QuranAudioPlayMode(val label: String, val shortLabel: String) {
    ARABIC_ONLY("Arabic Only", "Arabic"),
    TRANSLATION_ONLY("Tarjuma Only", "Tarjuma"),
    ARABIC_AND_TRANSLATION("Arabic + Tarjuma", "Arabic + Tarjuma")
}

data class AyahWordItem(
    val id: Int,
    val position: Int,
    val text: String,
    val audioUrl: String? = null,
    val translation: String = "",
    val transliteration: String = "",
    val charTypeName: String = "word"
)

data class AyahItem(
    val globalNumber: Int,
    val numberInSurah: Int,
    val surahNumber: Int,
    val arabicText: String,
    val transliteration: String = "",
    val translation: String = "",
    val isFavorite: Boolean = false,
    val tafsirText: String? = null,
    val isTafsirOpen: Boolean = false,
    val isTafsirLoading: Boolean = false,
    val words: List<AyahWordItem> = emptyList()
)

data class MushafWordItem(
    val id: Int,
    val text: String,
    val lineNumber: Int,
    val charTypeName: String, // "word" or "end"
    val verseNumber: Int = 0,
    val surahNumber: Int = 0,
    val audioUrl: String? = null,
    val translation: String = "",
    val transliteration: String = ""
)

data class MushafLineItem(
    val lineNumber: Int,
    val words: List<MushafWordItem>
)

data class MushafPageData(
    val pageNumber: Int,
    val juzNumber: Int,
    val hizbNumber: Int,
    val rukuNumber: Int,
    val surahNumber: Int,
    val surahArabicName: String,
    val surahEnglishName: String,
    val surahType: String,
    val totalAyahs: Int,
    val lines: List<MushafLineItem>,
    val translations: List<AyahItem>
)

data class QuranSettingsState(
    val readingMode: QuranReadingMode = QuranReadingMode.LIST,
    val translationLanguage: String = "hi", // "hi", "en", "ur", "ar", "bn", etc.
    val translationId: String = "122", // Maulana Azizul Haque (Hindi: 122), Abdul Haleem (En: 85)
    val translatorName: String = "Maulana Azizul Haque al-Umari",
    val showTranslation: Boolean = true,
    val showTransliteration: Boolean = true,
    val reciterId: String = "ar.alafasy",
    val reciterName: String = "Alafasy",
    val autoplayNext: Boolean = false,
    val autoScroll: Boolean = true,
    val tafsirId: String = "926",
    val tafsirName: String = "Arabic Jalalayn Tafseer",
    val repeatMode: String = "Repeat Off",
    val arabicFontSizeSp: Float = 24f,
    val isNightMode: Boolean = false,
    val audioPlayMode: QuranAudioPlayMode = QuranAudioPlayMode.ARABIC_ONLY,
    val translationVoiceLang: String = "ur", // "ur" (Shamshad Ali Khan) or "en" (Ibrahim Walk)
    val mushafThemeId: String = "madani_cream"
)

data class QuranRecentItem(
    val type: String, // "surah" or "juz"
    val number: Int,
    val title: String,
    val date: String
)
