package com.tasbih.digitaltasbih.data

data class SalahTourStrings(
    val step: String,
    val todayTitle: String,
    val todayDesc: String,
    val chartTitle: String,
    val chartDesc: String,
    val next: String,
    val done: String,
    val skip: String
)

object SalahTourLocalization {
    private val data = mapOf(
        "en" to SalahTourStrings(
            step = "Step {curr} of {total}",
            todayTitle = "🕌 Daily Salah Tracker",
            todayDesc = "Mark your 5 daily prayers (Fajr, Dhuhr, Asr, Maghrib, Isha) here and stay consistent.",
            chartTitle = "📊 Monthly Progress Chart",
            chartDesc = "Here you can track your monthly Salah performance, consistency stats, and complete prayer progress chart.",
            next = "Next ➔",
            done = "Got it! ✓",
            skip = "Skip"
        ),
        "hi" to SalahTourStrings(
            step = "कदम {curr} / {total}",
            todayTitle = "🕌 नमाज़ ट्रैकर (Daily Salah)",
            todayDesc = "अपनी 5 वक़्त की नमाज़ यहाँ मार्क करें (फ़ज्र, ज़ुहर, अस्र, मग़रिब, इशा) और पाबंदी बनाए रखें।",
            chartTitle = "📊 मासिक प्रोग्रेस चार्ट",
            chartDesc = "यहाँ आप अपने पूरे महीने की नमाज़ों का प्रोग्रेस चार्ट, निरंतरता (Consistency) और प्रदर्शन ट्रैक कर सकते हैं।",
            next = "आगे बढ़ें ➔",
            done = "समझ गया ✓",
            skip = "छोड़ें"
        ),
        "ur" to SalahTourStrings(
            step = "مرحلہ {curr} از {total}",
            todayTitle = "🕌 نماز ٹریکر (Daily Salah)",
            todayDesc = "اپنی 5 وقت کی نماز یہاں نشان زد (مارک) کریں اور پابندی برقرار رکھیں۔",
            chartTitle = "📊 ماہانہ پروگریس چارٹ",
            chartDesc = "یہاں آپ پورے مہینے کی نمازوں کی کارکردگی، مستقل مزاجی اور مکمل پروگریس چارٹ ٹریک کر سکتے ہیں۔",
            next = "اگلا ➔",
            done = "سمجھ گیا ✓",
            skip = "چھوڑیں"
        ),
        "ar" to SalahTourStrings(
            step = "الخطوة {curr} من {total}",
            todayTitle = "🕌 صلوات اليوم",
            todayDesc = "هنا يمكنك متابعة الصلوات الخمس اليومية وتحديد الصلوات التي تم أداؤها بكل سهولة.",
            chartTitle = "📊 مخطط الإنجاز الشهري",
            chartDesc = "هنا يمكنك متابعة إحصائيات أدائك للصلوات طوال الشهر ونسبة المحافظة عليها ومخطط التقدم اليومي.",
            next = "التالي ➔",
            done = "فهمت ✓",
            skip = "تخطي"
        ),
        "bn" to SalahTourStrings(
            step = "ধাপ {curr} / {total}",
            todayTitle = "🕌 আজকের নামাজ",
            todayDesc = "এখানে আপনি আপনার ৫ ওয়াক্ত নামাজ দেখতে পারবেন এবং সারা দিনে আদায় করা নামাজে সহজেই টিক দিতে পারবেন।",
            chartTitle = "📊 মাসিক অগ্রগতি চার্ট",
            chartDesc = "এখানে আপনি পুরো মাসের নামাজের ধারাবাহিকতা, পারফরম্যান্স এবং সম্পূর্ণ প্রগ্রেস চার্ট ট্র্যাক করতে পারবেন।",
            next = "পরবর্তী ➔",
            done = "বুঝেছি ✓",
            skip = "এড়িয়ে যান"
        ),
        "gu" to SalahTourStrings(
            step = "પગલું {curr} / {total}",
            todayTitle = "🕌 આજની નમાઝ",
            todayDesc = "અહીં તમે તમારી દૈનિક 5 નમાઝ જોઈ શકો છો અને દિવસ દરમિયાન પઢેલી નમાઝને સરળતાથી ટિક કરી શકો છો.",
            chartTitle = "📊 માસિક પ્રગતિ ચાર્ટ",
            chartDesc = "અહીં તમે આખા મહિનાની નમાઝનું પ્રદર્શન, નિયમિતતા અને સંપૂર્ણ પ્રગતિ ચાર્ટ ટ્રેક કરી શકો છો.",
            next = "આગળ ➔",
            done = "સમજાઈ ગયું ✓",
            skip = "છોડો"
        ),
        "ta" to SalahTourStrings(
            step = "படி {curr} / {total}",
            todayTitle = "🕌 இன்றைய தொழுகைகள்",
            todayDesc = "இங்கே நீங்கள் உங்கள் தினசரி 5 தொழுகைகளைக் காணலாம் மற்றும் தொழுத தொழுகைகளை எளிதாகக் குறிக்கலாம்.",
            chartTitle = "📊 மாதாந்திர முன்னேற்ற வரைபடம்",
            chartDesc = "இங்கே உங்கள் முழு மாத தொழுகை செயல்திறன், தொடர்ச்சி மற்றும் முன்னேற்ற வரைபடத்தைக் கண்காணிக்கலாம்.",
            next = "அடுத்து ➔",
            done = "புரிந்தது ✓",
            skip = "தவிர்"
        ),
        "te" to SalahTourStrings(
            step = "దశ {curr} / {total}",
            todayTitle = "🕌 నేటి నమాజులు",
            todayDesc = "ఇక్కడ మీరు రోజువారీ 5 నమాజులను చూడవచ్చు మరియు రోజంతా చదివిన నమాజులను సులభంగా టిక్ చేయవచ్చు.",
            chartTitle = "📊 నెలవారీ పురోగతి చార్ట్",
            chartDesc = "ఇక్కడ మీరు నెల మొత్తం నమాజ్ పనితీరు, స్థిరత్వం మరియు పూర్తి పురోగతి చార్ట్‌ను ట్రాక్ చేయవచ్చు.",
            next = "తరువాత ➔",
            done = "అర్థమైంది ✓",
            skip = "దాటవేయి"
        ),
        "ml" to SalahTourStrings(
            step = "ഘട്ടം {curr} / {total}",
            todayTitle = "🕌 ഇന്നത്തെ നമസ്കാരങ്ങൾ",
            todayDesc = "ഇവിടെ നിങ്ങൾക്ക് ദൈനംദിന 5 നമസ്കാരങ്ങൾ കാണാനും നിർവ്വഹിച്ച നമസ്കാരങ്ങൾ എളുപ്പത്തിൽ അടയാളപ്പെടുത്താനും കഴിയും.",
            chartTitle = "📊 പ്രതിമാസ പുരോഗതി ചാർട്ട്",
            chartDesc = "ഇവിടെ മുഴുവൻ മാസത്തെയും നമസ്കാര സ്ഥിരതയും പുരോഗതി ചാർട്ടും നിങ്ങൾക്ക് നിരീക്ഷിക്കാം.",
            next = "അടുത്തത് ➔",
            done = "മനസ്സിലായി ✓",
            skip = "ഒഴിവാക്കുക"
        ),
        "mr" to SalahTourStrings(
            step = "टप्पा {curr} / {total}",
            todayTitle = "🕌 आजच्या नमाज",
            todayDesc = "येथे तुम्ही तुमच्या दैनंदिन ५ नमाज पाहू शकता आणि दिवसभरात अदा केलेल्या नमाजांवर सहज टिक करू शकता.",
            chartTitle = "📊 मासिक प्रगती आलेख",
            chartDesc = "येथे तुम्ही पूर्ण महिन्याच्या नमाजची कामगिरी, नियमितता आणि प्रगती आलेख ट्रॅक करू शकता.",
            next = "पुढे ➔",
            done = "समजले ✓",
            skip = "वगळा"
        ),
        "pa" to SalahTourStrings(
            step = "ਕਦਮ {curr} / {total}",
            todayTitle = "🕌 ਅੱਜ ਦੀਆਂ ਨਮਾਜ਼ਾਂ",
            todayDesc = "ਇੱਥੇ ਤੁਸੀਂ ਆਪਣੀਆਂ ਰੋਜ਼ਾਨਾ 5 ਨਮਾਜ਼ਾਂ ਦੇਖ ਸਕਦੇ ਹੋ ਅਤੇ ਅਦਾ ਕੀਤੀਆਂ ਨਮਾਜ਼ਾਂ ਨੂੰ ਆਸਾਨੀ ਨਾਲ ਟਿਕ ਕਰ ਸਕਦੇ ਹੋ।",
            chartTitle = "📊 ਮਹੀਨਾਵਾਰ ਪ੍ਰਗਤੀ ਚਾਰਟ",
            chartDesc = "ਇੱਥੇ ਤੁਸੀਂ ਪੂਰੇ ਮਹੀਨੇ ਦੀਆਂ ਨਮਾਜ਼ਾਂ ਦਾ ਪ੍ਰਦਰਸ਼ਨ, ਨਿਰੰਤਰਤਾ ਅਤੇ ਪ੍ਰਗਤੀ ਚਾਰਟ ਟਰੈਕ ਕਰ ਸਕਦੇ ਹੋ।",
            next = "ਅੱਗੇ ➔",
            done = "ਸਮਝ ਗਿਆ ✓",
            skip = "ਛੱਡੋ"
        ),
        "kn" to SalahTourStrings(
            step = "ಹಂತ {curr} / {total}",
            todayTitle = "🕌 ಇಂದಿನ ನಮಾಜುಗಳು",
            todayDesc = "ಇಲ್ಲಿ ನೀವು ದೈನಂದಿನ 5 ನಮಾಜುಗಳನ್ನು ವೀಕ್ಷಿಸಬಹುದು ಮತ್ತು ನಿರ್ವಹಿಸಿದ ನಮಾಜುಗಳನ್ನು ಸುಲಭವಾಗಿ ಗುರುತಿಸಬಹುದು.",
            chartTitle = "📊 ಮಾಸಿಕ ಪ್ರಗತಿ ಚಾರ್ಟ್",
            chartDesc = "ಇಲ್ಲಿ ನೀವು ಇಡೀ ತಿಂಗಳ ನಮಾಜ್ ಕಾರ್ಯಕ್ಷಮತೆ, ಸ್ಥಿರತೆ ಮತ್ತು ಪ್ರಗತಿ ಚಾರ್ಟ್ ಅನ್ನು ಟ್ರ್ಯಾಕ್ ಮಾಡಬಹುದು.",
            next = "ಮುಂದೆ ➔",
            done = "ಅರ್ಥವಾಯಿತು ✓",
            skip = "ಬಿಟ್ಟುಬಿಡಿ"
        ),
        "or" to SalahTourStrings(
            step = "ପଦକ୍ଷେପ {curr} / {total}",
            todayTitle = "🕌 ଆଜିର ନମାଜ",
            todayDesc = "ଏଠାରେ ଆପଣ ନିଜର ଦୈନିକ ୫ ନମାଜ ଦେଖିପାରିବେ ଏବଂ ଅଦା କରିଥିବା ନମାଜକୁ ସହଜରେ ଟିକ୍ କରିପାରିବେ।",
            chartTitle = "📊 ମାସିକ ପ୍ରଗତି ଚାର୍ଟ",
            chartDesc = "ଏଠାରେ ଆପଣ ସାରା ମାସର ନମାଜ ପ୍ରଦର୍ଶନ, ନିରନ୍ତରତା ଏବଂ ପ୍ରଗତି ଚାର୍ଟ ଟ୍ରାକ୍ କରିପାରିବେ।",
            next = "ପରବର୍ତ୍ତୀ ➔",
            done = "ବୁଝିଗଲି ✓",
            skip = "ଛାଡ଼ନ୍ତୁ"
        ),
        "as" to SalahTourStrings(
            step = "স্তৰ {curr} / {total}",
            todayTitle = "🕌 আজিৰ নামাজ",
            todayDesc = "ইয়াত আপুনি দৈনিক ৫ ৱাক্ত নামাজ চাব পাৰে আৰু আদায় কৰা নামাজত সহজে টিক দিব পাৰে।",
            chartTitle = "📊 মাহেকীয়া অগ্ৰগতি চাৰ্ট",
            chartDesc = "ইয়াত আপুনি সমগ্ৰ মাহৰ নামাজৰ স্থিৰতা, প্ৰদৰ্শন আৰু অগ্ৰগতি চাৰ্ট নিৰীক্ষণ কৰিব পাৰে।",
            next = "পৰৱৰ্তী ➔",
            done = "বুজি পালোঁ ✓",
            skip = "এৰক"
        ),
        "ms" to SalahTourStrings(
            step = "Langkah {curr} / {total}",
            todayTitle = "🕌 Solat Hari Ini",
            todayDesc = "Di sini anda boleh melihat waktu 5 solat fardu harian dan menanda solat yang telah ditunaikan dengan mudah.",
            chartTitle = "📊 Carta Kemajuan Bulanan",
            chartDesc = "Di sini anda boleh menjejaki prestasi solat bulanan, tahap istiqamah dan carta kemajuan harian.",
            next = "Seterusnya ➔",
            done = "Faham ✓",
            skip = "Langkau"
        ),
        "id" to SalahTourStrings(
            step = "Langkah {curr} dari {total}",
            todayTitle = "🕌 Salat Hari Ini",
            todayDesc = "Di sini Anda dapat melihat waktu 5 salat harian dan mencentang salat yang telah dikerjakan dengan mudah.",
            chartTitle = "📊 Grafik Kemajuan Bulanan",
            chartDesc = "Di sini Anda dapat memantau performa salat bulanan, tingkat konsistensi, dan grafik kemajuan salat.",
            next = "Lanjut ➔",
            done = "Paham ✓",
            skip = "Lewati"
        ),
        "tr" to SalahTourStrings(
            step = "Adım {curr} / {total}",
            todayTitle = "🕌 Bugünkü Namazlar",
            todayDesc = "Buradan günlük 5 vakit namazınızı görebilir ve kıldığınız namazları kolayca işaretleyebilirsiniz.",
            chartTitle = "📊 Aylık İlerleme Grafiği",
            chartDesc = "Buradan tüm ay boyunca namaz performansınızı, istikrarınızı ve ilerleme grafiğinizi takip edebilirsiniz.",
            next = "İleri ➔",
            done = "Anladım ✓",
            skip = "Geç"
        ),
        "fa" to SalahTourStrings(
            step = "مرحله {curr} از {total}",
            todayTitle = "🕌 نمازهای امروز",
            todayDesc = "در اینجا می‌توانید ۵ وعده نماز روزانه خود را مشاهده کرده و نمازهای خوانده شده را علامت بزنید.",
            chartTitle = "📊 نمودار پیشرفت ماهانه",
            chartDesc = "در اینجا می‌توانید عملکرد ماهانه، ثبات در اقامه نماز و نمودار پیشرفت کامل خود را مشاهده کنید.",
            next = "بعدی ➔",
            done = "متوجه شدم ✓",
            skip = "رد کردن"
        ),
        "ha" to SalahTourStrings(
            step = "Mataki {curr} na {total}",
            todayTitle = "🕌 Sallolin Yau",
            todayDesc = "A nan zaka iya duba sallolinka 5 na yau da kullum da kuma shigar da alamar wadanda ka yi cikin sauki.",
            chartTitle = "📊 Jadawalin Ci Gaban Wata",
            chartDesc = "A nan zaka iya bibiyar kokarinka na sallah a cikin wata da kuma cikakken jadawalin ci gaba.",
            next = "Gaba ➔",
            done = "Na gane ✓",
            skip = "Wuce"
        ),
        "sw" to SalahTourStrings(
            step = "Hatua {curr} ya {total}",
            todayTitle = "🕌 Sala za Leo",
            todayDesc = "Hapa unaweza kuona nyakati za sala 5 za kila siku na kutia alama sala ulizoswali kwa urahisi.",
            chartTitle = "📊 Chati ya Maendeleo ya Mwezi",
            chartDesc = "Hapa unaweza kufuatilia uthabiti wa sala zako za mwezi mzima na chati kamili ya maendeleo.",
            next = "Ifuatayo ➔",
            done = "Nimeelewa ✓",
            skip = "Ruka"
        ),
        "so" to SalahTourStrings(
            step = "Tallaabada {curr} ee {total}",
            todayTitle = "🕌 Salaadaha Maanta",
            todayDesc = "Halkan waxaad ku arki kartaa 5-ta salaadood ee maanta oo aad si fudud u calaamadin kartaa kuwa aad tukatay.",
            chartTitle = "📊 Shaxda Horumarka Bishii",
            chartDesc = "Halkan waxaad kula socon kartaa waxqabadkaaga salaadda ee bisha oo dhan iyo shaxda horumarka.",
            next = "Ku xiga ➔",
            done = "Waan fahmay ✓",
            skip = "Ka gudub"
        ),
        "fr" to SalahTourStrings(
            step = "Étape {curr} sur {total}",
            todayTitle = "🕌 Prières d'Aujourd'hui",
            todayDesc = "Ici, vous pouvez voir vos 5 prières quotidiennes et cocher facilement celles que vous avez accomplies.",
            chartTitle = "📊 Graphique de Progression Mensuelle",
            chartDesc = "Ici, vous pouvez suivre votre assiduité sur le mois, votre taux de réussite et votre graphique complet.",
            next = "Suivant ➔",
            done = "Compris ✓",
            skip = "Passer"
        ),
        "de" to SalahTourStrings(
            step = "Schritt {curr} von {total}",
            todayTitle = "🕌 Heutige Gebete",
            todayDesc = "Hier sehen Sie Ihre 5 täglichen Gebete und können verrichtete Gebete ganz einfach abhaken.",
            chartTitle = "📊 Monatlicher Verlaufsbericht",
            chartDesc = "Hier können Sie Ihre monatliche Gebetsleistung, Beständigkeit und das vollständige Verlaufsdiagramm einsehen.",
            next = "Weiter ➔",
            done = "Verstanden ✓",
            skip = "Überspringen"
        ),
        "ru" to SalahTourStrings(
            step = "Шаг {curr} из {total}",
            todayTitle = "🕌 Намазы на сегодня",
            todayDesc = "Здесь вы можете видеть расписание 5 ежедневных намазов и легко отмечать прочитанные молитвы.",
            chartTitle = "📊 Ежемесячный график прогресса",
            chartDesc = "Здесь вы можете отслеживать статистику намазов за весь месяц, постоянство и график выполнения.",
            next = "Далее ➔",
            done = "Понятно ✓",
            skip = "Пропустить"
        ),
        "zh" to SalahTourStrings(
            step = "第 {curr} 步 / 共 {total} 步",
            todayTitle = "🕌 今日礼拜",
            todayDesc = "在此处您可以查看每日五番拜功，并轻松打卡记录您全天完成的礼拜。",
            chartTitle = "📊 每月进度统计图",
            chartDesc = "在此处您可以追踪整月的礼拜坚持率、完成度统计以及详细的柱状进度图表。",
            next = "下一步 ➔",
            done = "知道了 ✓",
            skip = "跳过"
        ),
    )

    fun get(lang: String): SalahTourStrings {
        val code = lang.lowercase().split("-")[0]
        return data[code] ?: data["en"]!!
    }
}
