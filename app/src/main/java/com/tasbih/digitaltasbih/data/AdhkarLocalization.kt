package com.tasbih.digitaltasbih.data

data class AdhkarStrings(
    val title: String,
    val completed: String,
    val done: String,
    val protection: String,
    val active: String,
    val forging: String,
    val pending: String,
    val streak: String,
    val days: String,
    val weeklyProgress: String,
    val week: String,
    val month: String,
    val year: String,
    val recentHistory: String,
    val today: String,
    val yesterday: String,
    val resetAll: String,
    val markComplete: String,
    val reset: String,
    val nextAdhkar: String,
    val tapToCount: String,
    val taps: String,
    val viewHadith: String,
    val close: String,
    val backToAdhkar: String,
    val share: String,
    val shareCard: String,
    val hadithModalTitle: String,
    val narratedBy: String,
    val transLabel: String,
    val viewSource: String,
    val resetConfirmTitle: String,
    val resetConfirmDesc: String,
    val cancel: String,
    val tourStep: String,
    val ofWord: String,
    val skip: String,
    val next: String,
    val doneBtn: String,
    val tour1Title: String,
    val tour1Desc: String,
    val tour2Title: String,
    val tour2Desc: String,
    val celebrationBadge: String,
    val celebrationTitle: String,
    val celebrationDesc: String,
    val celebrationQuote: String,
    val celebrationQuoteRef: String
)

object AdhkarLocalization {

    private val en = AdhkarStrings(
        title = "MORNING ADHKAR",
        completed = "Completed",
        done = "Done",
        protection = "Protection",
        active = "Active 🛡️",
        forging = "Forging ⚡",
        pending = "Pending ⚠️",
        streak = "Daily Streak",
        days = "Days",
        weeklyProgress = "Weekly Progress",
        week = "Week",
        month = "Month",
        year = "Year",
        recentHistory = "Recent Days History",
        today = "Today",
        yesterday = "Yesterday",
        resetAll = "Reset All Adhkar",
        markComplete = "Mark Count Complete",
        reset = "Reset",
        nextAdhkar = "Next Adhkar ➔",
        tapToCount = "TAP ANYWHERE TO COUNT",
        taps = "TAPS",
        viewHadith = "View Hadith",
        close = "Close",
        backToAdhkar = "Back to Adhkar",
        share = "Share Achievement",
        shareCard = "Share Protection Card",
        hadithModalTitle = "AUTHENTIC HADITH REFERENCE",
        narratedBy = "Narrated by: ",
        transLabel = "TRANSLATION:",
        viewSource = "View on Sunnah.com / Official Source",
        resetConfirmTitle = "Reset All Morning Adhkar?",
        resetConfirmDesc = "This will reset all your recited counts and completed marks for today's morning adhkar. Are you sure?",
        cancel = "Cancel",
        tourStep = "Step",
        ofWord = "of",
        skip = "Skip",
        next = "Next ➔",
        doneBtn = "Done ✓",
        tour1Title = "Morning Adhkar Recitation",
        tour1Desc = "Tap on any Adhkar card to open the interactive counter and recite your morning remembrance with Arabic, translation, and authentic benefits.",
        tour2Title = "Weekly Progress & Protection",
        tour2Desc = "Track your 7-day consistency and watch your Divine Protection Shield forge as you complete all 18 morning adhkar.",
        celebrationBadge = "🛡️ 100% DIVINE PROTECTION ATTAINED",
        celebrationTitle = "Mubarak! 🎉",
        celebrationDesc = "You have completed today's 18 Morning Adhkar! By Allah's will, you are enveloped in divine protection from all evil, harm, and hardship until evening.",
        celebrationQuote = "\"Whoever recites them when morning comes, they will be protected from all evil until evening.\"",
        celebrationQuoteRef = "— Sahih al-Bukhari & Sunan an-Nasa'i"
    )

    private val hi = en.copy(
        title = "सुबह के अज़कार",
        completed = "पूरा हुआ",
        done = "पूर्ण",
        protection = "सुरक्षा",
        active = "सक्रिय 🛡️",
        forging = "तैयार हो रहा ⚡",
        pending = "लंबित ⚠️",
        streak = "दैनिक स्ट्रीक",
        days = "दिन",
        weeklyProgress = "साप्ताहिक प्रगति",
        week = "सप्ताह",
        month = "महीना",
        year = "वर्ष",
        recentHistory = "पिछले दिनों का इतिहास",
        today = "आज",
        yesterday = "कल",
        resetAll = "सभी अज़कार रीसेट करें",
        markComplete = "गिनती पूरी मार्क करें",
        reset = "रीसेट",
        nextAdhkar = "अगला अज़कार ➔",
        tapToCount = "गिनने के लिए कहीं भी टैप करें",
        taps = "टैप",
        viewHadith = "हदीस देखें",
        close = "बंद करें",
        backToAdhkar = "अज़कार पर वापस जाएं",
        share = "उपलब्धि साझा करें",
        shareCard = "प्रोटेक्शन कार्ड साझा करें",
        hadithModalTitle = "प्रामाणिक हदीस संदर्भ",
        narratedBy = "द्वारा वर्णित: ",
        transLabel = "अनुवाद (TRANSLATION):",
        viewSource = "मूल स्रोत (Sunnah.com) पर देखें",
        resetConfirmTitle = "क्या सभी सुबह के अज़कार रीसेट करें?",
        resetConfirmDesc = "यह आज के सभी पढ़े गए काउंट और पूर्ण निशानों को रीसेट कर देगा। क्या आप सुनिश्चित हैं?",
        cancel = "रद्द करें",
        tourStep = "चरण",
        ofWord = "का",
        skip = "छोड़ें",
        next = "आगे ➔",
        doneBtn = "पूर्ण ✓",
        tour1Title = "सुबह के अज़कार पाठ",
        tour1Desc = "किसी भी अज़कार कार्ड पर टैप करके इंटरैक्टिव काउंटर खोलें और अरबी, अनुवाद और प्रामाणिक लाभों के साथ अपनी सुबह की याद का पाठ करें।",
        tour2Title = "साप्ताहिक प्रगति और सुरक्षा",
        tour2Desc = "अपनी 7-दिवसीय निरंतरता को ट्रैक करें और सभी 18 सुबह के अज़कार पूरे करते ही अपनी सुरक्षा ढाल को तैयार होते देखें।",
        celebrationBadge = "🛡️ 100% दिव्य सुरक्षा प्राप्त",
        celebrationTitle = "मुबारक हो! 🎉",
        celebrationDesc = "आपने आज के 18 सुबह के अज़कार पूरे कर लिए हैं! अल्लाह की इच्छा से, आप शाम तक हर बुराई और नुकसान से दिव्य सुरक्षा में हैं।",
        celebrationQuote = "\"जो कोई सुबह होते ही इन्हें पढ़ेगा, वह शाम तक हर बुराई से सुरक्षित रहेगा।\"",
        celebrationQuoteRef = "— सहीह अल-बुखारी व सुनन अन-नसाई"
    )

    private val ur = en.copy(
        title = "صبح کے اذکار",
        completed = "مکمل",
        done = "مکمل",
        protection = "حفاظت",
        active = "فعال 🛡️",
        forging = "بن رہا ہے ⚡",
        pending = "زیر التواء ⚠️",
        streak = "روزانہ سلسلہ",
        days = "دن",
        weeklyProgress = "ہفتہ وار پیش رفت",
        week = "ہفتہ",
        month = "مہینہ",
        year = "سال",
        recentHistory = "گزشتہ دنوں کی تاریخ",
        today = "آج",
        yesterday = "کل",
        resetAll = "تمام اذکار ری سیٹ کریں",
        markComplete = "گنتی مکمل نشان زد کریں",
        reset = "ری سیٹ",
        nextAdhkar = "اگلا ذکر ➔",
        tapToCount = "شمار کرنے کے لیے کہیں بھی ٹیپ کریں",
        taps = "ٹیپس",
        viewHadith = "حدیث دیکھیں",
        close = "بند کریں",
        backToAdhkar = "اذکار پر واپس جائیں",
        share = "شیئر کریں",
        shareCard = "حفاظتی کارڈ شیئر کریں",
        hadithModalTitle = "مستند حدیث کا حوالہ",
        narratedBy = "روایت ہے: ",
        transLabel = "ترجمہ (TRANSLATION):",
        viewSource = "اصل ماخذ پر دیکھیں",
        resetConfirmTitle = "کیا تمام صبح کے اذکار ری سیٹ کریں؟",
        resetConfirmDesc = "یہ آج کے تمام پڑھے گئے کاؤنٹ اور مکمل نشانات کو ری سیٹ کر دے گا۔ کیا آپ کو یقین ہے؟",
        cancel = "منسوخ",
        tourStep = "مرحلہ",
        ofWord = "میں سے",
        skip = "چھوڑیں",
        next = "اگلا ➔",
        doneBtn = "مکمل ✓",
        tour1Title = "صبح کے اذکار تلاوت",
        tour1Desc = "کسی بھی ذکر کارڈ پر ٹیپ کر کے انٹرایکٹو کاؤنٹر کھولیں اور عربی، ترجمہ اور مستند فضائل کے ساتھ صبح کا ذکر کریں۔",
        tour2Title = "ہفتہ وار پیش رفت اور حفاظت",
        tour2Desc = "اپنی 7 روزہ تسلسل کو ٹریک کریں اور تمام 18 صبح کے اذکار مکمل کرتے ہوئے اپنی حفاظتی ڈھال کو تیار ہوتے دیکھیں۔",
        celebrationBadge = "🛡️ 100% الٰہی حفاظت حاصل",
        celebrationTitle = "مبارک ہو! 🎉",
        celebrationDesc = "آپ نے آج کے تمام 18 صبح کے اذکار مکمل کر لیے ہیں! اللہ کے فضل سے، آپ شام تک ہر برائی اور نقصان سے محفوظ ہیں۔",
        celebrationQuote = "\"جس نے صبح ہوتے ہی یہ کلمات پڑھے، وہ شام تک ہر شر سے محفوظ رہے گا۔\"",
        celebrationQuoteRef = "— صحیح البخاری و سنن النسائی"
    )

    private val ar = en.copy(
        title = "أذكار الصباح",
        completed = "مكتمل",
        done = "منجز",
        protection = "الحماية",
        active = "نشط 🛡️",
        forging = "قيد التكوين ⚡",
        pending = "معلق ⚠️",
        streak = "الأيام المتتالية",
        days = "أيام",
        weeklyProgress = "التقدم الأسبوعي",
        week = "أسبوع",
        month = "شهر",
        year = "سنة",
        recentHistory = "سجل الأيام الأخيرة",
        today = "اليوم",
        yesterday = "أمس",
        resetAll = "إعادة تعيين جميع الأذكار",
        markComplete = "تحديد اكتمال العد",
        reset = "إعادة تعيين",
        nextAdhkar = "الذكر التالي ➔",
        tapToCount = "اضغط في أي مكان للعد",
        taps = "نقرات",
        viewHadith = "عرض الحديث",
        close = "إغلاق",
        backToAdhkar = "العودة إلى الأذكار",
        share = "مشاركة الإنجاز",
        shareCard = "مشاركة بطاقة الحماية",
        hadithModalTitle = "مرجع الحديث الصحيح",
        narratedBy = "رواه: ",
        transLabel = "الترجمة (TRANSLATION):",
        viewSource = "عرض المصدر الرسمي",
        resetConfirmTitle = "هل تريد إعادة تعيين جميع أذكار الصباح؟",
        resetConfirmDesc = "سيؤدي هذا إلى إعادة تعيين جميع أعداد التلاوة وعلامات الاكتمال لأذكار الصباح اليوم. هل أنت متأكد؟",
        cancel = "إلغاء",
        tourStep = "الخطوة",
        ofWord = "من",
        skip = "تخطي",
        next = "التالي ➔",
        doneBtn = "تم ✓",
        tour1Title = "تلاوة أذكار الصباح",
        tour1Desc = "اضغط على أي بطاقة لفتح العداد التفاعلي وقراءة أذكارك مع النص العربي والترجمة والفوائد الصحيحة.",
        tour2Title = "التقدم الأسبوعي والحماية",
        tour2Desc = "تابع التزامك الأسبوعي وشاهد درع الحماية الإلهية يكتمل بإتمام أذكار الصباح الـ 18.",
        celebrationBadge = "🛡️ تم نيل الحماية الإلهية بنسبة 100%",
        celebrationTitle = "مبارك! 🎉",
        celebrationDesc = "لقد أتممت أذكار الصباح الـ 18 لهذا اليوم! بإذن الله، أنت في حصن منيع وحماية تامة حتى المساء.",
        celebrationQuote = "«من قالها حين يصبح أُجير منها حتى يمسي»",
        celebrationQuoteRef = "— صحيح البخاري وسنن النسائي"
    )

    private val bn = en.copy(
        title = "সকালের আজকার",
        completed = "সম্পন্ন",
        done = "সম্পূর্ণ",
        protection = "সুরক্ষা",
        active = "সক্রিয় 🛡️",
        forging = "তৈরি হচ্ছে ⚡",
        pending = "অপেক্ষমান ⚠️",
        streak = "দৈনিক ধারা",
        days = "দিন",
        weeklyProgress = "সাপ্তাহিক অগ্রগতি",
        week = "সপ্তাহ",
        month = "মাস",
        year = "বছর",
        recentHistory = "সাম্প্রতিক দিনের ইতিহাস",
        today = "আজ",
        yesterday = "গতকাল",
        resetAll = "সব আজকার পুনরায় সেট করুন",
        markComplete = "গণনা সম্পন্ন চিহ্নিত করুন",
        reset = "রিসেট",
        nextAdhkar = "পরবর্তী আজকার ➔",
        tapToCount = "গণনা করতে যেকোনো স্থানে ট্যাপ করুন",
        taps = "ট্যাপ",
        viewHadith = "হাদিস দেখুন",
        close = "বন্ধ করুন",
        backToAdhkar = "আজকারে ফিরে যান",
        share = "শেয়ার করুন",
        shareCard = "সুরক্ষা কার্ড শেয়ার করুন",
        hadithModalTitle = "সহিহ হাদিস রেফারেন্স",
        narratedBy = "বর্ণনায়: ",
        transLabel = "অনুবাদ:",
        viewSource = "মূল সূত্র (Sunnah.com) দেখুন",
        resetConfirmTitle = "সব সকালের আজকার রিসেট করবেন?",
        resetConfirmDesc = "এটি আজকের সমস্ত পঠিত সংখ্যা এবং সমাপ্ত চিহ্নগুলি মুছে ফেলবে। আপনি কি নিশ্চিত?",
        cancel = "বাতিল",
        tourStep = "ধাপ",
        ofWord = "এর",
        skip = "এড়িয়ে যান",
        next = "পরবর্তী ➔",
        doneBtn = "সম্পন্ন ✓",
        tour1Title = "সকালের আজকার পাঠ",
        tour1Desc = "ইন্টারেক্টিভ কাউন্টার খুলতে যেকোনো আজকার কার্ডে ট্যাপ করুন এবং অর্থ ও ফজিলতসহ সকালের জিকির পাঠ করুন।",
        tour2Title = "সাপ্তাহিক অগ্রগতি ও সুরক্ষা",
        tour2Desc = "আপনার ৭ দিনের ধারাবাহিকতা ট্র্যাক করুন এবং ১৮টি সকালের আজকার সম্পন্ন করার সাথে সাথে সুরক্ষা ঢাল অর্জন করুন।",
        celebrationBadge = "🛡️ ১০০% ঐশ্বরিক সুরক্ষা অর্জিত",
        celebrationTitle = "মুবারক! 🎉",
        celebrationDesc = "আপনি আজকের ১৮টি সকালের আজকার সম্পন্ন করেছেন! আল্লাহর ইচ্ছায় আপনি সন্ধ্যা পর্যন্ত সমস্ত অনিষ্ট থেকে সুরক্ষিত।",
        celebrationQuote = "\"যে ব্যক্তি সকালে এটি পাঠ করবে, সে সন্ধ্যা পর্যন্ত সমস্ত ক্ষতি থেকে রক্ষা পাবে।\"",
        celebrationQuoteRef = "— সহিহ বুখারি ও সুনান আন-নাসায়ী"
    )

    private val tr = en.copy(
        title = "SABAH ZİKİRLERİ",
        completed = "Tamamlandı",
        done = "Bitti",
        protection = "Koruma",
        active = "Aktif 🛡️",
        forging = "Oluşuyor ⚡",
        pending = "Beklemede ⚠️",
        streak = "Günlük Seri",
        days = "Gün",
        weeklyProgress = "Haftalık İlerleme",
        week = "Hafta",
        month = "Ay",
        year = "Yıl",
        recentHistory = "Son Günlerin Geçmişi",
        today = "Bugün",
        yesterday = "Dün",
        resetAll = "Tüm Zikirleri Sıfırla",
        markComplete = "Tamamlandı Olarak İşaretle",
        reset = "Sıfırla",
        nextAdhkar = "Sonraki Zikir ➔",
        tapToCount = "SAYMAK İÇİN HERHANGİ BİR YERE DOKUNUN",
        taps = "DOKUNMA",
        viewHadith = "Hadisi Gör",
        close = "Kapat",
        backToAdhkar = "Zikirlere Dön",
        share = "Paylaş",
        shareCard = "Koruma Kartını Paylaş",
        hadithModalTitle = "SAHİH HADİS KAYNAĞI",
        narratedBy = "Râvi: ",
        transLabel = "ÇEVİRİ:",
        viewSource = "Sunnah.com'da Resmi Kaynağı Gör",
        resetConfirmTitle = "Tüm Sabah Zikirleri Sıfırlansın mı?",
        resetConfirmDesc = "Bu işlem bugünkü tüm okuma sayılarınızı sıfırlayacaktır. Emin misiniz?",
        cancel = "İptal",
        tourStep = "Adım",
        ofWord = "/",
        skip = "Atla",
        next = "İleri ➔",
        doneBtn = "Bitti ✓",
        tour1Title = "Sabah Zikirleri Okuma",
        tour1Desc = "Etkileşimli sayacı açmak ve sabah zikirlerinizi Arapça metin, çeviri ve faziletleriyle okumak için herhangi bir karta dokunun.",
        tour2Title = "Haftalık İlerleme ve Koruma",
        tour2Desc = "7 günlük istikrarınızı takip edin ve 18 sabah zikrini tamamladıkça manevi kalkanınızı inşa edin.",
        celebrationBadge = "🛡️ %100 MANEVİ KORUMA ELDE EDİLDİ",
        celebrationTitle = "Mübarek Olsun! 🎉",
        celebrationDesc = "Bugünün 18 Sabah Zikrini tamamladınız! Allah'ın izniyle akşama kadar her türlü kötülükten korunmaktasınız.",
        celebrationQuote = "\"Kim sabahleyin bunları söylerse, akşama kadar her türlü kötülükten korunur.\"",
        celebrationQuoteRef = "— Sahih-i Buhârî ve Sünen-i Nesâî"
    )

    private val id = en.copy(
        title = "DZIKIR PAGI",
        completed = "Selesai",
        done = "Selesai",
        protection = "Perlindungan",
        active = "Aktif 🛡️",
        forging = "Ditempa ⚡",
        pending = "Tertunda ⚠️",
        streak = "Rangkaian Hari",
        days = "Hari",
        weeklyProgress = "Kemajuan Mingguan",
        week = "Minggu",
        month = "Bulan",
        year = "Tahun",
        recentHistory = "Riwayat Hari Terakhir",
        today = "Hari Ini",
        yesterday = "Kemarin",
        resetAll = "Atur Ulang Semua Dzikir",
        markComplete = "Tandai Selesai",
        reset = "Atur Ulang",
        nextAdhkar = "Dzikir Berikutnya ➔",
        tapToCount = "KETUK DI MANA SAJA UNTUK MENGHITUNG",
        taps = "KETUKAN",
        viewHadith = "Lihat Hadits",
        close = "Tutup",
        backToAdhkar = "Kembali ke Dzikir",
        share = "Bagikan",
        shareCard = "Bagikan Kartu Perlindungan",
        hadithModalTitle = "REFERENSI HADITS SHAHIH",
        narratedBy = "Diriwayatkan oleh: ",
        transLabel = "TERJEMAHAN:",
        viewSource = "Lihat Sumber Resmi di Sunnah.com",
        resetConfirmTitle = "Atur Ulang Semua Dzikir Pagi?",
        resetConfirmDesc = "Ini akan mengatur ulang semua hitungan dzikir Anda untuk hari ini. Apakah Anda yakin?",
        cancel = "Batal",
        tourStep = "Langkah",
        ofWord = "dari",
        skip = "Lewati",
        next = "Lanjut ➔",
        doneBtn = "Selesai ✓",
        tour1Title = "Membaca Dzikir Pagi",
        tour1Desc = "Ketuk kartu dzikir untuk membuka penghitung interaktif dan membaca dzikir pagi Anda lengkap dengan bahasa Arab, terjemahan, dan keutamaannya.",
        tour2Title = "Kemajuan Mingguan & Perlindungan",
        tour2Desc = "Lacak konsistensi 7 hari Anda dan saksikan perisai perlindungan ilahi Anda terbentuk seiring Anda menyelesaikan 18 dzikir pagi.",
        celebrationBadge = "🛡️ 100% PERLINDUNGAN ILAHI DIPEROLEH",
        celebrationTitle = "Mubarak! 🎉",
        celebrationDesc = "Anda telah menyelesaikan 18 Dzikir Pagi hari ini! Dengan izin Allah, Anda dilindungi dari segala kejahatan hingga petang.",
        celebrationQuote = "\"Barangsiapa mengucapkannya di waktu pagi, ia akan dilindungi dari segala keburukan hingga petang.\"",
        celebrationQuoteRef = "— Shahih al-Bukhari & Sunan an-Nasa'i"
    )

    private val ms = id.copy(
        title = "ZIKIR PAGI",
        resetAll = "Set Semula Semua Zikir",
        markComplete = "Tanda Selesai",
        reset = "Set Semula",
        nextAdhkar = "Zikir Seterusnya ➔",
        backToAdhkar = "Kembali ke Zikir",
        hadithModalTitle = "RUJUKAN HADIS SAHIH"
    )

    private val fa = en.copy(
        title = "اذکار صبحگاه",
        completed = "تکمیل شده",
        done = "انجام شد",
        protection = "حفاظت",
        active = "فعال 🛡️",
        forging = "در حال ساخت ⚡",
        pending = "در انتظار ⚠️",
        streak = "روزهای متوالی",
        days = "روز",
        weeklyProgress = "پیشرفت هفتگی",
        week = "هفته",
        month = "ماه",
        year = "سال",
        recentHistory = "تاریخچه روزهای اخیر",
        today = "امروز",
        yesterday = "دیروز",
        resetAll = "بازنشانی همه اذکار",
        markComplete = "علامت‌گذاری تکمیل",
        reset = "بازنشانی",
        nextAdhkar = "ذکر بعدی ➔",
        tapToCount = "برای شمارش هر جا ضربه بزنید",
        taps = "شمارش",
        viewHadith = "مشاهده حدیث",
        close = "بستن",
        backToAdhkar = "بازگشت به اذکار",
        share = "اشتراک‌گذاری",
        shareCard = "اشتراک‌گذاری کارت حفاظت",
        hadithModalTitle = "مرجع حدیث صحیح",
        narratedBy = "روایت از: ",
        transLabel = "ترجمه:",
        viewSource = "مشاهده منبع رسمی در Sunnah.com"
    )

    private val fr = en.copy(
        title = "ADHKAR DU MATIN",
        completed = "Terminé",
        done = "Fait",
        protection = "Protection",
        active = "Actif 🛡️",
        forging = "En forge ⚡",
        pending = "En attente ⚠️",
        streak = "Série quotidienne",
        days = "Jours",
        weeklyProgress = "Progression hebdomadaire",
        week = "Semaine",
        month = "Mois",
        year = "Année",
        recentHistory = "Historique récent",
        today = "Aujourd'hui",
        yesterday = "Hier",
        resetAll = "Réinitialiser tous les Adhkar",
        markComplete = "Marquer comme terminé",
        reset = "Réinitialiser",
        nextAdhkar = "Adhkar suivant ➔",
        tapToCount = "APPUYEZ N'IMPORTE OÙ POUR COMPTER",
        taps = "TAPS",
        viewHadith = "Voir le Hadith",
        close = "Fermer",
        backToAdhkar = "Retour aux Adhkar",
        share = "Partager",
        shareCard = "Partager la carte de protection",
        hadithModalTitle = "RÉFÉRENCE DE HADITH AUTHENTIQUE",
        narratedBy = "Rapporté par : ",
        transLabel = "TRADUCTION :",
        viewSource = "Voir sur Sunnah.com / Source officielle",
        cancel = "Annuler"
    )

    private val de = en.copy(
        title = "MORGEN-ADHKAR",
        completed = "Abgeschlossen",
        done = "Erledigt",
        protection = "Schutz",
        active = "Aktiv 🛡️",
        forging = "Wird geschmiedet ⚡",
        pending = "Ausstehend ⚠️",
        streak = "Tägliche Serie",
        days = "Tage",
        weeklyProgress = "Wöchentlicher Fortschritt",
        week = "Woche",
        month = "Monat",
        year = "Jahr",
        recentHistory = "Verlauf der letzten Tage",
        today = "Heute",
        yesterday = "Gestern",
        resetAll = "Alle Adhkar zurücksetzen",
        markComplete = "Als abgeschlossen markieren",
        reset = "Zurücksetzen",
        nextAdhkar = "Nächster Adhkar ➔",
        tapToCount = "ÜBERALL TIPPEN ZUM ZÄHLEN",
        taps = "TAPS",
        viewHadith = "Hadith ansehen",
        close = "Schließen",
        backToAdhkar = "Zurück zu Adhkar",
        share = "Teilen",
        shareCard = "Schutzkarte teilen",
        hadithModalTitle = "AUTHENTISCHE HADITH-QUELLE",
        narratedBy = "Überliefert von: ",
        transLabel = "ÜBERSETZUNG:",
        viewSource = "Auf Sunnah.com / Offizielle Quelle ansehen",
        cancel = "Abbrechen"
    )

    private val ru = en.copy(
        title = "УТРЕННИЕ АЗКАРЫ",
        completed = "Выполнено",
        done = "Готово",
        protection = "Защита",
        active = "Активна 🛡️",
        forging = "Формируется ⚡",
        pending = "В ожидании ⚠️",
        streak = "Серия дней",
        days = "Дней",
        weeklyProgress = "Недельный прогресс",
        week = "Неделя",
        month = "Месяц",
        year = "Год",
        recentHistory = "История за последние дни",
        today = "Сегодня",
        yesterday = "Вчера",
        resetAll = "Сбросить все азкары",
        markComplete = "Отметить завершённым",
        reset = "Сброс",
        nextAdhkar = "Следующий азкар ➔",
        tapToCount = "НАЖМИТЕ В ЛЮБОМ МЕСТЕ ДЛЯ СЧЁТА",
        taps = "НАЖАТИЙ",
        viewHadith = "Посмотреть хадис",
        close = "Закрыть",
        backToAdhkar = "Назад к азкарам",
        share = "Поделиться",
        shareCard = "Поделиться карточкой защиты",
        hadithModalTitle = "ДОСТОВЕРНЫЙ ХАДИС",
        narratedBy = "Передал: ",
        transLabel = "ПЕРЕВОД:",
        viewSource = "Смотреть на Sunnah.com",
        cancel = "Отмена"
    )

    private val es = en.copy(
        title = "ADHKAR DE LA MAÑANA",
        completed = "Completado",
        done = "Hecho",
        protection = "Protección",
        active = "Activa 🛡️",
        forging = "En forja ⚡",
        pending = "Pendiente ⚠️",
        streak = "Racha diaria",
        days = "Días",
        weeklyProgress = "Progreso semanal",
        week = "Semana",
        month = "Mes",
        year = "Año",
        recentHistory = "Historial reciente",
        today = "Hoy",
        yesterday = "Ayer",
        resetAll = "Restablecer todos los Adhkar",
        markComplete = "Marcar como completado",
        reset = "Restablecer",
        nextAdhkar = "Siguiente Adhkar ➔",
        tapToCount = "TOCA EN CUALQUIER LUGAR PARA CONTAR",
        taps = "TOQUES",
        viewHadith = "Ver Hadiz",
        close = "Cerrar",
        backToAdhkar = "Volver a los Adhkar",
        share = "Compartir",
        shareCard = "Compartir tarjeta de protección",
        hadithModalTitle = "REFERENCIA DE HADIZ AUTÉNTICO",
        narratedBy = "Narrado por: ",
        transLabel = "TRADUCCIÓN:",
        viewSource = "Ver en Sunnah.com / Fuente oficial",
        cancel = "Cancelar"
    )

    // Regional Indian languages (Punjabi, Gujarati, Marathi, Tamil, Telugu, Malayalam, Kannada)
    private val pa = hi.copy(
        title = "ਸਵੇਰ ਦੇ ਅਜ਼ਕਾਰ",
        completed = "ਪੂਰਾ ਹੋਇਆ",
        done = "ਪੂਰਾ",
        protection = "ਸੁਰੱਖਿਆ",
        active = "ਸਰਗਰਮ 🛡️",
        forging = "ਤਿਆਰ ਹੋ ਰਿਹਾ ⚡",
        pending = "ਬਾਕੀ ⚠️",
        streak = "ਰੋਜ਼ਾਨਾ ਲੜੀ",
        days = "ਦਿਨ",
        weeklyProgress = "ਹਫ਼ਤਾਵਾਰੀ ਤਰੱਕੀ",
        week = "ਹਫ਼ਤਾ",
        month = "ਮਹੀਨਾ",
        year = "ਸਾਲ",
        today = "ਅੱਜ",
        yesterday = "ਕੱਲ੍ਹ",
        resetAll = "ਸਾਰੇ ਅਜ਼ਕਾਰ ਰੀਸੈੱਟ ਕਰੋ",
        markComplete = "ਗਿਣਤੀ ਪੂਰੀ ਮਾਰਕ ਕਰੋ",
        reset = "ਰੀਸੈੱਟ",
        nextAdhkar = "ਅਗਲਾ ਅਜ਼ਕਾਰ ➔",
        tapToCount = "ਗਿਣਨ ਲਈ ਕਿਤੇ ਵੀ ਟੈਪ ਕਰੋ",
        taps = "ਟੈਪ",
        viewHadith = "ਹਦੀਸ ਦੇਖੋ",
        close = "ਬੰਦ ਕਰੋ",
        backToAdhkar = "ਅਜ਼ਕਾਰ 'ਤੇ ਵਾਪਸ ਜਾਓ",
        share = "ਸਾਂਝਾ ਕਰੋ",
        hadithModalTitle = "ਪ੍ਰਮਾਣਿਕ ਹਦੀਸ ਹਵਾਲਾ",
        viewSource = "ਮੂਲ ਸਰੋਤ ਦੇਖੋ"
    )

    private val gu = hi.copy(
        title = "સવારના અઝકાર",
        completed = "પૂર્ણ થયું",
        done = "પૂર્ણ",
        protection = "રક્ષણ",
        active = "સક્રિય 🛡️",
        forging = "તૈયાર થઈ રહ્યું છે ⚡",
        pending = "બાકી ⚠️",
        streak = "દૈનિક શ્રેણી",
        days = "દિવસ",
        weeklyProgress = "સાપ્તાહિક પ્રગતિ",
        week = "અઠવાડિયું",
        month = "મહિનો",
        year = "વર્ષ",
        today = "આજે",
        yesterday = "ગઈકાલે",
        resetAll = "બધા અઝકાર રીસેટ કરો",
        markComplete = "ગણતરી પૂર્ણ માર્ક કરો",
        reset = "રીસેટ",
        nextAdhkar = "આગળનો અઝકાર ➔",
        tapToCount = "ગણવા માટે ગમે ત્યાં ટેપ કરો",
        viewHadith = "હદીસ જુઓ",
        close = "બંધ કરો",
        backToAdhkar = "અઝકાર પર પાછા જાઓ",
        hadithModalTitle = "પ્રમાણભૂત હદીસ સંદર્ભ"
    )

    private val mr = hi.copy(
        title = "सकाळचे अझकार",
        completed = "पूर्ण झाले",
        done = "पूर्ण",
        protection = "संरक्षण",
        active = "सक्रिय 🛡️",
        forging = "तयार होत आहे ⚡",
        pending = "प्रलंबित ⚠️",
        streak = "दैनिक सातत्य",
        days = "दिवस",
        weeklyProgress = "साप्ताहिक प्रगती",
        week = "आठवडा",
        month = "महिना",
        year = "वर्ष",
        today = "आज",
        yesterday = "काल",
        resetAll = "सर्व अझकार रीसेट करा",
        markComplete = "मोजणी पूर्ण चिन्हांकित करा",
        reset = "रीसेट",
        nextAdhkar = "पुढील अझकार ➔",
        tapToCount = "मोजण्यासाठी कुठेही टॅप करा",
        viewHadith = "हदीस पहा",
        close = "बंद करा",
        backToAdhkar = "अझकारवर परत जा",
        hadithModalTitle = "प्रामाणिक हदीस संदर्भ"
    )

    private val ta = en.copy(
        title = "காலை அத்கார்",
        completed = "முடிந்தது",
        done = "முடிந்தது",
        protection = "பாதுகாப்பு",
        active = "செயலில் 🛡️",
        forging = "உருவாகிறது ⚡",
        pending = "நிலுவையில் ⚠️",
        streak = "தினசரி தொடர்",
        days = "நாட்கள்",
        weeklyProgress = "வாராந்திர முன்னேற்றம்",
        week = "வாரம்",
        month = "மாதம்",
        year = "ஆண்டு",
        today = "இன்று",
        yesterday = "நேற்று",
        resetAll = "அனைத்து அத்காரையும் மீட்டமை",
        markComplete = "முழுமையாக குறிக்கவும்",
        reset = "மீட்டமை",
        nextAdhkar = "அடுத்த அத்கார் ➔",
        tapToCount = "எண்ண எங்கு வேண்டுமானாலும் தட்டவும்",
        viewHadith = "ஹதீஸைப் பார்க்கவும்",
        close = "மூடு",
        backToAdhkar = "அத்காருக்கு திரும்பு",
        hadithModalTitle = "நம்பகமான ஹதீஸ் ஆதாரம்"
    )

    private val te = en.copy(
        title = "ఉదయపు అజ్కార్",
        completed = "పూర్తయింది",
        done = "పూర్తయింది",
        protection = "రక్షణ",
        active = "యాక్టివ్ 🛡️",
        forging = "రూపొందుతోంది ⚡",
        pending = "పెండింగ్ ⚠️",
        streak = "రోజువారీ పరంపర",
        days = "రోజులు",
        weeklyProgress = "వారపు పురోగతి",
        week = "వారం",
        month = "నెల",
        year = "సంవత్సరం",
        today = "ఈరోజు",
        yesterday = "నిన్న",
        resetAll = "అన్ని అజ్కార్‌లను రీసెట్ చేయండి",
        markComplete = "పూర్తయినట్లు గుర్తు పెట్టండి",
        reset = "రీసెట్",
        nextAdhkar = "తరువాతి అజ్కార్ ➔",
        tapToCount = "లెక్కించడానికి ఎక్కడైనా నొక్కండి",
        viewHadith = "హదీస్ చూడండి",
        close = "మూసివేయి",
        backToAdhkar = "అజ్కార్‌కు తిరిగి వెళ్లండి",
        hadithModalTitle = "ప్రామాణిక హదీస్ ఆధారం"
    )

    private val ml = en.copy(
        title = "രാവിലത്തെ അദ്കാർ",
        completed = "പൂർത്തിയായി",
        done = "പൂർത്തിയായി",
        protection = "സംരക്ഷണം",
        active = "സജീവം 🛡️",
        forging = "നിർമ്മിക്കപ്പെടുന്നു ⚡",
        pending = "ബാക്കി ⚠️",
        streak = "ദിവസേനയുള്ള തുടർച്ച",
        days = "ദിവസങ്ങൾ",
        weeklyProgress = "പ്രതിവാര പുരോഗതി",
        week = "ആഴ്ച",
        month = "മാസം",
        year = "വർഷം",
        today = "ഇന്ന്",
        yesterday = "ഇന്നലെ",
        resetAll = "എല്ലാ അദ്കാറുകളും പുനഃക്രമീകരിക്കുക",
        markComplete = "പൂർത്തിയായതായി അടയാളപ്പെടുത്തുക",
        reset = "റീസെറ്റ്",
        nextAdhkar = "അടുത്ത അദ്കാർ ➔",
        tapToCount = "എണ്ണാൻ എവിടെയും സ്പർശിക്കുക",
        viewHadith = "ഹദീസ് കാണുക",
        close = "അടയ്ക്കുക",
        backToAdhkar = "അദ്കാറിലേക്ക് മടങ്ങുക",
        hadithModalTitle = "വിശ്വസനീയമായ ഹദീസ് പരാമർശം"
    )

    private val kn = en.copy(
        title = "ಬೆಳಗಿನ ಅಜ್ಕಾರ್",
        completed = "ಪೂರ್ಣಗೊಂಡಿದೆ",
        done = "ಮುಗಿದಿದೆ",
        protection = "ರಕ್ಷಣೆ",
        active = "ಸಕ್ರಿಯ 🛡️",
        forging = "ರೂಪುಗೊಳ್ಳುತ್ತಿದೆ ⚡",
        pending = "ಬಾಕಿ ⚠️",
        streak = "ದೈನಂದಿನ ಸರಣಿ",
        days = "ದಿನಗಳು",
        weeklyProgress = "ವಾರದ ಪ್ರಗತಿ",
        week = "ವಾರ",
        month = "ತಿಂಗಳು",
        year = "ವರ್ಷ",
        today = "ಇಂದು",
        yesterday = "ನಿನ್ನೆ",
        resetAll = "ಎಲ್ಲಾ ಅಜ್ಕಾರ್ ಮರುಹೊಂದಿಸಿ",
        markComplete = "ಪೂರ್ಣಗೊಂಡಿದೆ ಎಂದು ಗುರುತಿಸಿ",
        reset = "ರೀಸೆಟ್",
        nextAdhkar = "ಮುಂದಿನ ಅಜ್ಕಾರ್ ➔",
        tapToCount = "ಎಣಿಸಲು ಎಲ್ಲಿಯಾದರೂ ಸ್ಪರ್ಶಿಸಿ",
        viewHadith = "ಹದೀಸ್ ನೋಡಿ",
        close = "ಮುಚ್ಚಿ",
        backToAdhkar = "ಅಜ್ಕಾರ್‌ಗೆ ಹಿಂತಿರುಗಿ",
        hadithModalTitle = "ಪ್ರಾಮಾಣಿಕ ಹದೀಸ್ ಉಲ್ಲೇಖ"
    )

    fun getStrings(lang: String): AdhkarStrings {
        val code = lang.lowercase().trim()
        return when {
            code.startsWith("hi") -> hi
            code.startsWith("ur") -> ur
            code.startsWith("ar") -> ar
            code.startsWith("bn") -> bn
            code.startsWith("pa") -> pa
            code.startsWith("gu") -> gu
            code.startsWith("mr") -> mr
            code.startsWith("ta") -> ta
            code.startsWith("te") -> te
            code.startsWith("ml") -> ml
            code.startsWith("kn") -> kn
            code.startsWith("tr") -> tr
            code.startsWith("id") -> id
            code.startsWith("ms") -> ms
            code.startsWith("fa") -> fa
            code.startsWith("fr") -> fr
            code.startsWith("de") -> de
            code.startsWith("ru") -> ru
            code.startsWith("es") -> es
            else -> en
        }
    }

    fun getEveningStrings(lang: String): AdhkarStrings {
        val base = getStrings(lang)
        val code = lang.lowercase().trim()
        val eveTitle = when {
            code.startsWith("hi") -> "शाम के अज़कार"
            code.startsWith("ur") -> "شام کے اذکار"
            code.startsWith("ar") -> "أذكار المساء"
            code.startsWith("bn") -> "সন্ধ্যার দো‘আ"
            code.startsWith("tr") -> "Akşam Zikirleri"
            code.startsWith("id") || code.startsWith("ms") -> "Dzikir Petang"
            code.startsWith("fa") -> "اذکار شامگاه"
            code.startsWith("fr") -> "ADHKAR DU SOIR"
            code.startsWith("de") -> "ABEND-ADHKAR"
            code.startsWith("ru") -> "ВЕЧЕРНИЕ АЗКАРЫ"
            code.startsWith("es") -> "ADHKAR DE LA TARDE"
            else -> "EVENING ADHKAR"
        }
        val t1Title = when {
            code.startsWith("hi") -> "🌙 शाम के अज़कार पाठ"
            code.startsWith("ur") -> "🌙 شام کے اذکار تلاوت"
            code.startsWith("ar") -> "🌙 تلاوة أذكار المساء"
            else -> "🌙 Evening Adhkar Recitation"
        }
        val t1Desc = when {
            code.startsWith("hi") -> "किसी भी अज़कार कार्ड पर टैप करके इंटरएक्टिव काउंटर खोलें और अरबी, अनुवाद और फ़ज़ीलत के साथ शाम का ज़िक्र करें।"
            code.startsWith("ur") -> "کسی بھی اذکار کارڈ پر ٹیپ کر کے انٹرایکٹو کاؤنٹر کھولیں اور عربی، ترجمہ اور مستند فوائد کے ساتھ شام کا ذکر کریں۔"
            code.startsWith("ar") -> "اضغط على أي بطاقة لفتح العداد التفاعلي وقراءة أذكار المساء مع النص العربي والترجمة والفوائد الصحيحة."
            else -> "Tap on any Adhkar card to open the interactive counter and recite your evening remembrance with Arabic, translation, and authentic benefits."
        }
        val t2Title = when {
            code.startsWith("hi") -> "📈 साप्ताहिक प्रगति और स्ट्रीक"
            code.startsWith("ur") -> "📈 ہفتہ وار پیش رفت اور تسلسل"
            code.startsWith("ar") -> "📈 التقدم الأسبوعي وسلسلة الأيام"
            else -> "📈 Weekly Progress & Streak"
        }
        val t2Desc = when {
            code.startsWith("hi") -> "यहाँ आप अपनी साप्ताहिक शाम के अज़कार प्रगति, निरंतरता चार्ट और दैनिक रूहानी स्ट्रीक देख सकते हैं।"
            code.startsWith("ur") -> "یہاں آپ اپنی ہفتہ وار شام کے اذکار کی پیش رفت، تسلسل کا چارٹ اور روزانہ کا روحانی تسلسل دیکھ سکتے ہیں۔"
            code.startsWith("ar") -> "هنا يمكنك تتبع تقدم أذكار المساء الأسبوعي، ورسم بياني للانتظام، وسلسلتك اليومية."
            else -> "Here you can track your weekly Evening Adhkar progress, completion consistency chart, and daily spiritual streak."
        }
        val rTitle = when {
            code.startsWith("hi") -> "सभी शाम के अज़कार रीसेट करें?"
            code.startsWith("ur") -> "کیا تمام شام کے اذکار ری سیٹ کریں؟"
            code.startsWith("ar") -> "إعادة تعيين جميع أذكار المساء؟"
            else -> "Reset All Evening Adhkar?"
        }
        val rDesc = when {
            code.startsWith("hi") -> "यह आज के शाम के अज़कार के लिए आपकी पढ़ी गई गिनती और पूर्ण किए गए चिह्नों को रीसेट कर देगा। क्या आप सुनिश्चित हैं?"
            code.startsWith("ur") -> "یہ آج کے شام کے اذکار کی تمام پڑھی گئی گنتی اور مکمل نشانات کو ری سیٹ کر دے گا۔ کیا آپ مطمئن ہیں؟"
            code.startsWith("ar") -> "سيؤدي هذا إلى إعادة تعيين جميع أعداد قراءاتك وعلامات الإكمال لأذكار المساء لهذا اليوم. هل أنت متأكد؟"
            else -> "This will reset all your recited counts and completed marks for today's evening adhkar. Are you sure?"
        }
        val cDesc = when {
            code.startsWith("hi") -> "अल्हम्दुलिल्लाह! आपने आज के सभी 23 शाम के अज़कार पूरे कर लिए हैं। अल्लाह के हुक्म से यह रूहानी क़िला सुबह तक आपकी हर शर, नज़र-ए-बद और मुसीबत से हिफ़ाज़त करेगा।"
            code.startsWith("ur") -> "الحمد للہ! آپ نے آج کے تمام 23 شام کے اذکار مکمل کر لیے ہیں۔ اللہ کے اذن سے یہ روحانی قلعہ صبح تک ہر شر، نظر بد اور مصیبت سے آپ کی حفاظت فرمائے گا۔"
            code.startsWith("ar") -> "الحمد لله! لقد أتممت جميع أذكار المساء الـ 23 اليوم. بإذن الله، هذا الحصن الإلهي يحميك من كل سوء حتى تصبح."
            else -> "Alhamdulillah! You have completed all 23 Evening Adhkar. By Allah's permission, this divine fortress protects you from jinn, evil eye, misfortune, and anxiety until morning."
        }
        val cQuote = when {
            code.startsWith("hi") -> "\"जिसने शाम के वक़्त इन्हें पढ़ा, वह सुबह होने तक हर बुराई से महफ़ूज़ रहेगा।\""
            code.startsWith("ur") -> "\"جس نے شام کے وقت ان کو پڑھا، وہ صبح ہونے تک ہر برائی سے محفوظ رہے گا۔\""
            code.startsWith("ar") -> "\"مَن قالَها حِينَ يُمْسِي أُجِيرَ مِنَ الجِنِّ حَتَّى يُصْبِحَ\""
            else -> "\"Whoever recites them when evening comes, they will be protected from all evil until morning.\""
        }

        return base.copy(
            title = eveTitle,
            tour1Title = t1Title,
            tour1Desc = t1Desc,
            tour2Title = t2Title,
            tour2Desc = t2Desc,
            resetConfirmTitle = rTitle,
            resetConfirmDesc = rDesc,
            celebrationDesc = cDesc,
            celebrationQuote = cQuote
        )
    }
}

