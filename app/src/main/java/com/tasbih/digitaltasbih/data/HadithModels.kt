package com.tasbih.digitaltasbih.data

data class HadithCollectionItem(
    val name: String,
    val titleEn: String,
    val titleAr: String,
    val shortIntro: String = "",
    val totalHadith: Int = 0,
    val totalBooks: Int = 0,
    val coverAsset: String = "",
    val badgeNumber: String = "",
    val index: Int = 0
)

data class HadithBookItem(
    val bookNumber: String,
    val nameEn: String,
    val nameAr: String = "",
    val hadithStartNumber: Int = 0,
    val hadithEndNumber: Int = 0,
    val numberOfHadith: Int = 0
)

data class HadithChapterItem(
    val bookNumber: String,
    val chapterId: String,
    val chapterNumber: String = "",
    val titleEn: String = "",
    val titleAr: String = "",
    val intro: String? = null
)

data class HadithDetailItem(
    val collection: String,
    val bookNumber: String,
    val chapterId: String,
    val hadithNumber: String,
    val arabicText: String = "",
    val narrator: String = "",
    val bodyText: String = "",
    val grade: String = "",
    val gradedBy: String = "",
    val refUrl: String = "",
    val inBookRef: String = ""
)

data class HadithBookmarkItem(
    val hadithNum: String,
    val refLabel: String,
    val refUrl: String,
    val arText: String = "",
    val narrator: String = "",
    val bodyText: String = "",
    val collection: String = "",
    val bookNumber: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
