package com.tasbih.digitaltasbih.data

data class AppStrings(
    val appSubtitle: String,
    val greeting: String,
    val now: String,
    val next: String,
    val dateLabel: String,
    val countdown: String,
    // Prayers
    val fajr: String,
    val sunrise: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val isha: String,
    val allPrayers: String,
    // Feature Tiles
    val quran: String,
    val prayerTimes: String,
    val qibla: String,
    val tasbih: String,
    val liveDhikr: String,
    val salahTracker: String,
    val islamicCalendar: String,
    val adhan: String,
    val duaTile: String,
    val names99: String,
    val hadith: String,
    val quranAudio: String,
    val findMosque: String,
    val liveKaaba: String,
    val dhikrTracker: String,
    val morningAdhkar: String,
    val eveningAdhkar: String,
    val jummahMubarak: String = "Jummah Mubarak",
    val ramadanMubarak: String = "Ramadan Mubarak 🌙",
    val more: String = "More",
    val zakat: String = "Zakat",
    val shahada: String = "Shahada",
    val salah: String = "Salah",
    val sawm: String = "Sawm",
    val hajj: String = "Hajj",
    // Bottom Tabs
    val home: String,
    val calendar: String,
    val dua: String
)

data class LangCategory(
    val title: String,
    val languages: List<LanguageItem>
)

object LocalizationManager {

    val allLanguageGroups = listOf(
        LangCategory(
            title = "🇮🇳 South Asian",
            languages = listOf(
                LanguageItem("hi", "हिन्दी"),
                LanguageItem("ur", "اردو"),
                LanguageItem("hinglish", "Hinglish (Roman)"),
                LanguageItem("bn", "বাংলা"),
                LanguageItem("pa", "ਪੰਜਾਬੀ"),
                LanguageItem("gu", "ગુજરાતી"),
                LanguageItem("mr", "मराठी"),
                LanguageItem("ta", "தமிழ்"),
                LanguageItem("te", "తెలుగు"),
                LanguageItem("ml", "മലയാളം"),
                LanguageItem("kn", "ಕನ್ನಡ")
            )
        ),
        LangCategory(
            title = "🌍 Middle East & Central Asia",
            languages = listOf(
                LanguageItem("ar", "العربية"),
                LanguageItem("fa", "فارسی"),
                LanguageItem("tr", "Türkçe")
            )
        ),
        LangCategory(
            title = "🌏 Southeast Asia",
            languages = listOf(
                LanguageItem("id", "Bahasa Indonesia"),
                LanguageItem("ms", "Bahasa Melayu")
            )
        ),
        LangCategory(
            title = "🌐 International",
            languages = listOf(
                LanguageItem("en", "English"),
                LanguageItem("fr", "Français"),
                LanguageItem("de", "Deutsch"),
                LanguageItem("ru", "Русский"),
                LanguageItem("es", "Español")
            )
        )
    )

    private val translations = mapOf(
        "en" to AppStrings(
            appSubtitle = "Your Companion in Worship",
            greeting = "Assalamu Alaikum 👋",
            now = "NOW",
            next = "NEXT PRAYER",
            dateLabel = "15 Oct 2026 · 2 Jumada al-Awwal 1448",
            countdown = "2:37:12",
            fajr = "Fajr",
            sunrise = "Sunrise",
            dhuhr = "Dhuhr",
            asr = "Asr",
            maghrib = "Maghrib",
            isha = "Isha",
            allPrayers = "All Prayers →",
            quran = "Noble Quran",
            prayerTimes = "Prayer Times",
            qibla = "Qibla Direction",
            tasbih = "Tasbih",
            liveDhikr = "Live Dhikr",
            salahTracker = "Salah Tracker",
            islamicCalendar = "Islamic Calendar",
            adhan = "Adhan",
            duaTile = "Dua",
            names99 = "99 Names",
            hadith = "Hadith",
            quranAudio = "Quran Audio",
            findMosque = "Find Mosque",
            liveKaaba = "Live Kaaba",
            dhikrTracker = "Dhikr Tracker",
            morningAdhkar = "Morning Adhkar",
            eveningAdhkar = "Evening Adhkar",
            jummahMubarak = "Jummah Mubarak",
            home = "Home",
            calendar = "Calendar",
            dua = "Dua"
        ),
        "hinglish" to AppStrings(
            appSubtitle = "Ibadat Me Aapka Saathi",
            greeting = "Assalamu Alaikum 👋",
            now = "ABHI",
            next = "AGLI NAMAZ",
            dateLabel = "15 Oct 2026 · 2 Jumada al-Awwal 1448",
            countdown = "2:37:12",
            fajr = "Fajr",
            sunrise = "Tulu-e-Aftab",
            dhuhr = "Zohar",
            asr = "Asr",
            maghrib = "Maghrib",
            isha = "Isha",
            allPrayers = "Sabhi Namazein →",
            quran = "Quran Shareef",
            prayerTimes = "Namaz Ke Auqaat",
            qibla = "Qibla Rukh",
            tasbih = "Tasbih",
            liveDhikr = "Live Dhikr",
            salahTracker = "Salah Tracker",
            islamicCalendar = "Islamic Calendar",
            adhan = "Adhan",
            duaTile = "Dua",
            names99 = "99 Asma-ul-Husna",
            hadith = "Hadith",
            quranAudio = "Quran Audio",
            findMosque = "Masjid Khojein",
            liveKaaba = "Live Kaaba",
            dhikrTracker = "Dhikr Tracker",
            morningAdhkar = "Subah Ke Azkar",
            eveningAdhkar = "Shaam Ke Azkar",
            jummahMubarak = "Jummah Mubarak",
            home = "Home",
            calendar = "Calendar",
            dua = "Dua"
        ),
        "hi" to AppStrings(
            appSubtitle = "डिजिटल तस्बीह",
            greeting = "अस्सलाम वालेकुम 👋",
            now = "अभी",
            next = "अगली नमाज़",
            dateLabel = "15 अक्टूबर 2026 · 2 जमादिउल अव्वल 1448",
            countdown = "2:37:12",
            fajr = "फ़ज्र",
            sunrise = "सूर्योदय",
            dhuhr = "ज़ुहर",
            asr = "असर",
            maghrib = "मग़रिब",
            isha = "इशा",
            allPrayers = "सभी नमाज़ें →",
            quran = "क़ुरान शरीफ़",
            prayerTimes = "नमाज़ समय",
            qibla = "क़िबला दिशा",
            tasbih = "तस्बीह",
            liveDhikr = "Live Dhikr",
            salahTracker = "सलाह ट्रैकर",
            islamicCalendar = "इस्लामिक कैलेंडर",
            adhan = "अज़ान",
            duaTile = "दुआ",
            names99 = "अल्लाह के 99 नाम",
            hadith = "हदीस",
            quranAudio = "क़ुरान ऑडियो",
            findMosque = "मस्जिद खोजें",
            liveKaaba = "लाइव काबा",
            dhikrTracker = "ज़िक्र ट्रैकर",
            morningAdhkar = "सुबह के अज़कार",
            eveningAdhkar = "शाम के अज़कार",
            jummahMubarak = "जुम्मा मुबारक",
            ramadanMubarak = "रमज़ान मुबारक 🌙",
            home = "होम",
            calendar = "कैलेंडर",
            dua = "दुआ"
        ),
        "ur" to AppStrings(
            appSubtitle = "ڈیجیٹل تسبیح",
            greeting = "السلام علیکم 👋",
            now = "ابھی",
            next = "اگلی نماز",
            dateLabel = "15 اکتوبر 2026 · 2 جمادی الاول 1448",
            countdown = "2:37:12",
            fajr = "فجر",
            sunrise = "طلوعِ آفتاب",
            dhuhr = "ظہر",
            asr = "عصر",
            maghrib = "مغرب",
            isha = "عشاء",
            allPrayers = "تمام نمازیں ←",
            quran = "قرآن مجید",
            prayerTimes = "نماز اوقات",
            qibla = "قبلہ رخ",
            tasbih = "تسبیح",
            liveDhikr = "Live Dhikr",
            salahTracker = "نماز ٹریکر",
            islamicCalendar = "اسلامی کیلنڈر",
            adhan = "اذان",
            duaTile = "دعا",
            names99 = "اللہ کے 99 نام",
            hadith = "حدیث",
            quranAudio = "قرآن آڈیو",
            findMosque = "مسجد تلاش کریں",
            liveKaaba = "لائیو کعبہ",
            dhikrTracker = "ذکر ٹریکر",
            morningAdhkar = "صبح کے اذکار",
            eveningAdhkar = "شام کے اذکار",
            jummahMubarak = "جمعہ مبارک",
            ramadanMubarak = "رمضان مبارک 🌙",
            home = "ہوم",
            calendar = "کیلنڈر",
            dua = "دعا"
        ),
        "ar" to AppStrings(
            appSubtitle = "رفيقك في العبادة",
            greeting = "السلام عليكم 👋",
            now = "الآن",
            next = "الصلاة القادمة",
            dateLabel = "15 أكتوبر 2026 · 2 جمادى الأولى 1448",
            countdown = "2:37:12",
            fajr = "الفجر",
            sunrise = "الشروق",
            dhuhr = "الظهر",
            asr = "العصر",
            maghrib = "المغرب",
            isha = "العشاء",
            allPrayers = "جميع الصلوات ←",
            quran = "القرآن الكريم",
            prayerTimes = "مواقيت الصلاة",
            qibla = "اتجاه القبلة",
            tasbih = "تسبيح",
            liveDhikr = "Live Dhikr",
            salahTracker = "متتبع الصلاة",
            islamicCalendar = "التقويم الهجري",
            adhan = "الأذان",
            duaTile = "الدعاء",
            names99 = "أسماء الله الحسنى",
            hadith = "الأحاديث",
            quranAudio = "القرآن الصوتي",
            findMosque = "البحث عن مساجد",
            liveKaaba = "مباشر الكعبة",
            dhikrTracker = "متتبع الأذكار",
            morningAdhkar = "أذكار الصباح",
            eveningAdhkar = "أذكار المساء",
            jummahMubarak = "جمعة مباركة",
            ramadanMubarak = "رمضان مبارك 🌙",
            home = "الرئيسية",
            calendar = "التقويم",
            dua = "الدعاء"
        ),
        "bn" to AppStrings(
            appSubtitle = "ডিজিটাল তাসবীহ",
            greeting = "আসসালামু আলাইকুম 👋",
            now = "এখন",
            next = "পরবর্তী নামাজ",
            dateLabel = "১৫ অক্টোবর ২০২৬ · ২ জমাদিউল আউয়াল ১৪৪৮",
            countdown = "2:37:12",
            fajr = "ফজর",
            sunrise = "সূর্যোদয়",
            dhuhr = "যোহর",
            asr = "আসর",
            maghrib = "মাগরিব",
            isha = "ইশা",
            allPrayers = "সকল নামাজ →",
            quran = "কোরআন শরীফ",
            prayerTimes = "নামাজের সময়",
            qibla = "কিবলা দিক",
            tasbih = "তাসবীহ",
            liveDhikr = "Live Dhikr",
            salahTracker = "নামাজ ট্র্যাকার",
            islamicCalendar = "ইসলামিক ক্যালেন্ডার",
            adhan = "আযান",
            duaTile = "দো‘আ",
            names99 = "আল্লাহর ৯৯ নাম",
            hadith = "হাদিস",
            quranAudio = "কোরআন অডিও",
            findMosque = "মসজিদ খুঁজুন",
            liveKaaba = "লাইভ কাবা",
            dhikrTracker = "যিকির ট্র্যাকার",
            morningAdhkar = "সকালের দো‘আ",
            eveningAdhkar = "সন্ধ্যার দো‘আ",
            jummahMubarak = "জুম্মা মোবারক",
            ramadanMubarak = "রমজান মোবারক 🌙",
            home = "হোম",
            calendar = "ক্যালেন্ডার",
            dua = "দো‘আ"
        )
    )

    fun getString(langCode: String): AppStrings {
        val base = translations[langCode] ?: translations["en"]!!
        val localizedGreeting = when (langCode) {
            "hi" -> "अस्सलाम वालेकुम 👋"
            "ur" -> "السلام علیکم 👋"
            "ar" -> "السلام عليكم 👋"
            "bn" -> "আসসালামু আলাইকুম 👋"
            "pa" -> "ਅੱਸਲਾਮੁ ਅਲੈਕੁਮ 👋"
            "gu" -> "અસ્સલામુ અલૈકુમ 👋"
            "mr" -> "अस्सलाम वालेकुम 👋"
            "ta" -> "அஸ்ஸலாமு அலைக்கும் 👋"
            "te" -> "అస్సలాము అలైకుమ్ 👋"
            "ml" -> "അസ്സലാമു അലൈക്കും 👋"
            "kn" -> "ಅಸ್ಸಲಾಮು ಅಲೈಕುಮ್ 👋"
            "fa" -> "سلام علیکم 👋"
            "tr" -> "Selamün Aleyküm 👋"
            "id", "ms" -> "Assalamu'alaikum 👋"
            "ru" -> "Ассаляму Алейкум 👋"
            else -> base.greeting
        }
        val localizedRamadan = when (langCode) {
            "hi" -> "रमज़ान मुबारक 🌙"
            "ur" -> "رمضان مبارک 🌙"
            "ar" -> "رمضان مبارك 🌙"
            "bn" -> "রমজান মোবারক 🌙"
            "pa" -> "ਰਮਜ਼ਾਨ ਮੁਬਾਰਕ 🌙"
            "gu" -> "રમઝાન મુબારક 🌙"
            "mr" -> "रमझान मुबारक 🌙"
            "ta" -> "ரமலான் முபாரக் 🌙"
            "te" -> "రంజాన్ ముబారక్ 🌙"
            "ml" -> "റമദാൻ മുബാറക് 🌙"
            "kn" -> "ರಂಜಾನ್ ಮುಬಾರಕ್ 🌙"
            "fa" -> "رمضان مبارک 🌙"
            "tr" -> "Ramazan Mübarek 🌙"
            "id" -> "Ramadhan Mubarak 🌙"
            "ms" -> "Ramadan Mubarak 🌙"
            "fr" -> "Ramadan Moubarak 🌙"
            "ru" -> "Рамадан Мубарак 🌙"
            "es" -> "Ramadán Mubarak 🌙"
            else -> base.ramadanMubarak
        }
        val localizedMore = when (langCode) {
            "hi" -> "और"
            "ur" -> "مزید"
            "ar" -> "المزيد"
            "bn" -> "আরও"
            "fa" -> "بیشتر"
            "tr" -> "Daha Fazla"
            "id", "ms" -> "Lainnya"
            "fr" -> "Plus"
            "es" -> "Más"
            "de" -> "Mehr"
            "ru" -> "Ещё"
            else -> "More"
        }
        val localizedZakat = when (langCode) {
            "hi" -> "ज़कात"
            "ur" -> "زکوٰۃ"
            "ar" -> "الزكاة"
            "bn" -> "যাকাত"
            "fa" -> "زکات"
            "tr" -> "Zekât"
            "id", "ms" -> "Zakat"
            "ru" -> "Закят"
            else -> "Zakat"
        }
        val localizedShahada = when (langCode) {
            "hi" -> "शहादा"
            "ur" -> "شہادت"
            "ar" -> "الشهادة"
            "bn" -> "শাহাদাহ"
            "fa" -> "شهادت"
            "tr" -> "Şehadet"
            "id", "ms" -> "Syahadat"
            "ru" -> "Шахада"
            "fr" -> "Chahada"
            "es" -> "Shahada"
            "de" -> "Schahada"
            else -> "Shahada"
        }
        val localizedSalah = when (langCode) {
            "hi" -> "सलाह"
            "ur" -> "صلوٰۃ"
            "ar" -> "الصلاة"
            "bn" -> "সালাত"
            "fa" -> "نماز"
            "tr" -> "Namaz"
            "id", "ms" -> "Shalat"
            "ru" -> "Намаз"
            "fr" -> "Salât"
            "es" -> "Salah"
            "de" -> "Gebet"
            else -> "Salah"
        }
        val localizedSawm = when (langCode) {
            "hi" -> "रोज़ा"
            "ur" -> "روزہ"
            "ar" -> "الصوم"
            "bn" -> "রোজা"
            "fa" -> "روزه"
            "tr" -> "Oruç"
            "id", "ms" -> "Puasa"
            "ru" -> "Пост"
            "fr" -> "Jeûne"
            "es" -> "Ayuno"
            "de" -> "Fasten"
            else -> "Sawm"
        }
        val localizedHajj = when (langCode) {
            "hi" -> "हज"
            "ur" -> "حج"
            "ar" -> "الحج"
            "bn" -> "হজ"
            "fa" -> "حج"
            "tr" -> "Hac"
            "id", "ms" -> "Haji"
            "ru" -> "Хадж"
            "fr" -> "Hajj"
            "es" -> "Hajj"
            "de" -> "Haddsch"
            else -> "Hajj"
        }
        return base.copy(
            greeting = localizedGreeting,
            ramadanMubarak = localizedRamadan,
            more = localizedMore,
            zakat = localizedZakat,
            shahada = localizedShahada,
            salah = localizedSalah,
            sawm = localizedSawm,
            hajj = localizedHajj
        )
    }
}
