package com.tasbih.digitaltasbih.data

data class HadithStrings(
    val title: String = "हदीस शरीफ़",
    val back: String = "वापस",
    val searchCollections: String = "हदीस किताबें खोजें...",
    val collectionsTab: String = "Collections",
    val bookmarksTab: String = "Bookmarks",
    val searchBooks: String = "किताबें खोजें...",
    val jumpPlaceholder: String = "हदीस नंबर लिखें...",
    val jumpButton: String = "जाएं →",
    val searchChapters: String = "अध्याय खोजें...",
    val searchLoadedHadiths: String = "लोड हुई हदीसों में खोजें...",
    val readMore: String = "Read More",
    val loadMore: String = "और हदीस दिखाएं",
    val noHadiths: String = "कोई हदीस नहीं मिली",
    val noBookmarks: String = "अभी तक कोई बुकमार्क नहीं है",
    val bookWord: String = "Books",
    val hadithWord: String = "Hadith",
    val inBookRef: String = "In-book reference : Book %s, Hadith %s",
    val sunnahLink: String = "Sunnah.com ↗",
    val settingsTitle: String = "हदीस सेटिंग्स",
    val fontSize: String = "फ़ॉन्ट साइज़",
    val language: String = "भाषा"
)

object HadithLocalization {
    private val hindi = HadithStrings(
        title = "हदीस शरीफ़",
        back = "वापस",
        searchCollections = "हदीस किताबें खोजें...",
        collectionsTab = "Collections",
        bookmarksTab = "Bookmarks",
        searchBooks = "किताबें खोजें...",
        jumpPlaceholder = "हदीस नंबर लिखें...",
        jumpButton = "जाएं →",
        searchChapters = "अध्याय खोजें...",
        searchLoadedHadiths = "लोड हुई हदीसों में खोजें...",
        readMore = "Read More",
        loadMore = "और हदीस दिखाएं",
        noHadiths = "कोई हदीस नहीं मिली",
        noBookmarks = "अभी तक कोई बुकमार्क नहीं है",
        bookWord = "किताबें",
        hadithWord = "हदीस",
        inBookRef = "In-book reference : Book %s, Hadith %s",
        sunnahLink = "Sunnah.com ↗",
        settingsTitle = "हदीस सेटिंग्स",
        fontSize = "फ़ॉन्ट साइज़",
        language = "भाषा"
    )

    private val english = HadithStrings(
        title = "Hadith Sharif",
        back = "Back",
        searchCollections = "Search Hadith books...",
        collectionsTab = "Collections",
        bookmarksTab = "Bookmarks",
        searchBooks = "Search books...",
        jumpPlaceholder = "Hadith number...",
        jumpButton = "Go →",
        searchChapters = "Search chapters...",
        searchLoadedHadiths = "Search loaded hadiths...",
        readMore = "Read More",
        loadMore = "Load More Hadiths",
        noHadiths = "No Hadiths Found",
        noBookmarks = "No bookmarks yet",
        bookWord = "Books",
        hadithWord = "Hadith",
        inBookRef = "In-book reference : Book %s, Hadith %s",
        sunnahLink = "Sunnah.com ↗",
        settingsTitle = "Hadith Settings",
        fontSize = "Font Size",
        language = "Language"
    )

    private val urdu = HadithStrings(
        title = "حدیث شریف",
        back = "واپس",
        searchCollections = "حدیث کی کتابیں تلاش کریں...",
        collectionsTab = "Collections",
        bookmarksTab = "Bookmarks",
        searchBooks = "کتابیں تلاش کریں...",
        jumpPlaceholder = "حدیث نمبر درج کریں...",
        jumpButton = "← جائیں",
        searchChapters = "ابواب تلاش کریں...",
        searchLoadedHadiths = "لوڈ شدہ احادیث میں تلاش کریں...",
        readMore = "مزید پڑھیں",
        loadMore = "مزید احادیث دکھائیں",
        noHadiths = "کوئی حدیث نہیں ملی",
        noBookmarks = "ابھی تک کوئی بُک مارک نہیں ہے",
        bookWord = "کتب",
        hadithWord = "احادیث",
        inBookRef = "In-book reference : Book %s, Hadith %s",
        sunnahLink = "Sunnah.com ↗",
        settingsTitle = "حدیث کی ترتیبات",
        fontSize = "فونٹ سائز",
        language = "زبان"
    )

    private val arabic = HadithStrings(
        title = "الحديث الشريف",
        back = "رجوع",
        searchCollections = "البحث في كتب الحديث...",
        collectionsTab = "المجموعات",
        bookmarksTab = "الإشارات المرجعية",
        searchBooks = "البحث في الكتب...",
        jumpPlaceholder = "رقم الحديث...",
        jumpButton = "← اذهب",
        searchChapters = "البحث في الأبواب...",
        searchLoadedHadiths = "البحث في الأحاديث المحملة...",
        readMore = "اقرأ المزيد",
        loadMore = "تحميل المزيد",
        noHadiths = "لم يتم العثور على أحاديث",
        noBookmarks = "لا توجد إشارات مرجعية بعد",
        bookWord = "كتب",
        hadithWord = "حديث",
        inBookRef = "المرجع في الكتاب : كتاب %s, حديث %s",
        sunnahLink = "Sunnah.com ↗",
        settingsTitle = "إعدادات الحديث",
        fontSize = "حجم الخط",
        language = "اللغة"
    )

    private val bengali = HadithStrings(
        title = "হাদিস শরীফ",
        back = "ফিরে যান",
        searchCollections = "হাদিস গ্রন্থ অনুসন্ধান করুন...",
        collectionsTab = "Collections",
        bookmarksTab = "Bookmarks",
        searchBooks = "গ্রন্থ অনুসন্ধান করুন...",
        jumpPlaceholder = "হাদিস নম্বর লিখুন...",
        jumpButton = "যান →",
        searchChapters = "অধ্যায় অনুসন্ধান করুন...",
        searchLoadedHadiths = "লোড হওয়া হাদিস খুঁজুন...",
        readMore = "আরও পড়ুন",
        loadMore = "আরও হাদিস দেখুন",
        noHadiths = "কোন হাদিস পাওয়া যায়নি",
        noBookmarks = "এখনো কোন বুকমার্ক নেই",
        bookWord = "গ্রন্থ",
        hadithWord = "হাদিস",
        inBookRef = "In-book reference : Book %s, Hadith %s",
        sunnahLink = "Sunnah.com ↗",
        settingsTitle = "হাদিস সেটিংস",
        fontSize = "ফন্ট সাইজ",
        language = "ভাষা"
    )

    fun get(lang: String): HadithStrings = when (lang) {
        "en" -> english
        "ur" -> urdu
        "ar" -> arabic
        "bn" -> bengali
        else -> hindi
    }
}
