package com.tasbih.digitaltasbih.data

data class CalendarTourStrings(
    val step: String,
    val title: String,
    val desc: String,
    val done: String,
    val skip: String
)

object CalendarTourLocalization {
    private val data = mapOf(
        "en" to CalendarTourStrings(
            step = "Guide • 1 / 1",
            title = "🌙 Islamic Events & Dates",
            desc = "Here you can view authentic Islamic events, sacred festivals, historical dates, and special occasions for the selected day.",
            done = "Got it! ✓",
            skip = "Skip"
        ),
        "hi" to CalendarTourStrings(
            step = "गाइड • 1 / 1",
            title = "🌙 इस्लामी इवेंट्स और तारीख़ें",
            desc = "यहाँ आप चुने हुए दिन के इस्लामी इवेंट्स, मुक़द्दस त्यौहार, ऐतिहासिक तारीख़ें और ख़ास दिन आसानी से देख सकते हैं।",
            done = "समझ गया ✓",
            skip = "छोड़ें"
        ),
        "ur" to CalendarTourStrings(
            step = "رہنما • 1 / 1",
            title = "🌙 اسلامی ایونٹس اور اہم تاریخیں",
            desc = "یہاں آپ منتخب کردہ تاریخ کے اسلامی ایونٹس، مقدس تہوار، تاریخی تاریخیں اور خصوصی ایام دیکھ سکتے ہیں۔",
            done = "سمجھ گیا ✓",
            skip = "چھوڑیں"
        ),
        "ar" to CalendarTourStrings(
            step = "دليل • 1 / 1",
            title = "🌙 المناسبات والأحداث الإسلامية",
            desc = "هنا يمكنك الاطلاع على المناسبات الإسلامية والأعياد الدينية والتواريخ التاريخية المباركة لليوم المحدد.",
            done = "فهمت ✓",
            skip = "تخطي"
        ),
        "bn" to CalendarTourStrings(
            step = "গাইড • ১ / ১",
            title = "🌙 ইসলামিক দিবস ও ঘটনাবলি",
            desc = "এখানে আপনি নির্বাচিত দিনের ইসলামিক ঘটনাবলি, পবিত্র উৎসব, ঐতিহাসিক তারিখ এবং বিশেষ দিনগুলো দেখতে পারেন।",
            done = "বুঝেছি ✓",
            skip = "এড়িয়ে যান"
        ),
        "gu" to CalendarTourStrings(
            step = "માર્ગદર્શિકા • 1 / 1",
            title = "🌙 ઇસ્લામિક ઇવેન્ટ્સ અને તારીખો",
            desc = "અહીં તમે પસંદ કરેલા દિવસની ઇસ્લામિક ઇવેન્ટ્સ, પવિત્ર તહેવારો, ઐતિહાસિક તારીખો અને વિશેષ દિવસો જોઈ શકો છો.",
            done = "સમજાઈ ગયું ✓",
            skip = "છોડો"
        ),
        "ta" to CalendarTourStrings(
            step = "வழிகாட்டி • 1 / 1",
            title = "🌙 இஸ்லாமிய நிகழ்வுகள் & தேதிகள்",
            desc = "தேர்ந்தெடுக்கப்பட்ட நாளுக்கான இஸ்லாமிய நிகழ்வுகள், புனித பண்டிகைகள், வரலாற்று தேதிகள் மற்றும் முக்கிய தினங்களை இங்கே காணலாம்.",
            done = "புரிந்தது ✓",
            skip = "தவிர்"
        ),
        "te" to CalendarTourStrings(
            step = "గైడ్ • 1 / 1",
            title = "🌙 ఇస్లామిక్ ఈవెంట్లు & తేదీలు",
            desc = "ఎంచుకున్న రోజు కోసం ఇస్లామిక్ ఈవెంట్లు, పవిత్ర పండుగలు, చారిత్రక తేదీలు మరియు ప్రత్యేక సందర్భాలను ఇక్కడ చూడవచ్చు.",
            done = "అర్థమైంది ✓",
            skip = "దాటవేయి"
        ),
        "ml" to CalendarTourStrings(
            step = "വഴികാട്ടി • 1 / 1",
            title = "🌙 ഇസ്ലാമിക വിശേഷ ദിവസങ്ങൾ",
            desc = "തിരഞ്ഞെടുത്ത ദിവസത്തെ ഇസ്ലാമിക ദിനങ്ങൾ, വിശുദ്ധ ഉത്സവങ്ങൾ, ചരിത്ര പ്രാധാന്യമുള്ള തീയതികൾ എന്നിവ ഇവിടെ കാണാം.",
            done = "മനസ്സിലായി ✓",
            skip = "ഒഴിവാക്കുക"
        ),
        "mr" to CalendarTourStrings(
            step = "मार्गदर्शक • 1 / 1",
            title = "🌙 इस्लामिक इव्हेंट्स आणि तारखा",
            desc = "येथे तुम्ही निवडलेल्या दिवसाचे इस्लामिक इव्हेंट्स, पवित्र सण, ऐतिहासिक तारखा आणि विशेष प्रसंग पाहू शकता.",
            done = "समजले ✓",
            skip = "वगळा"
        ),
        "pa" to CalendarTourStrings(
            step = "ਗਾਈਡ • 1 / 1",
            title = "🌙 ਇਸਲਾਮੀ ਸਮਾਗਮ ਅਤੇ ਤਾਰੀਖਾਂ",
            desc = "ਇੱਥੇ ਤੁਸੀਂ ਚੁਣੇ ਹੋਏ ਦਿਨ ਦੇ ਇਸਲਾਮੀ ਸਮਾਗਮ, ਪਵਿੱਤਰ ਤਿਉਹਾਰ, ਇਤਿਹਾਸਕ ਤਾਰੀਖਾਂ ਅਤੇ ਵਿਸ਼ੇਸ਼ ਦਿਨ ਦੇਖ ਸਕਦੇ ਹੋ।",
            done = "ਸਮਝ ਗਿਆ ✓",
            skip = "ਛੱਡੋ"
        ),
        "kn" to CalendarTourStrings(
            step = "ಮಾರ್ಗದರ್ಶಿ • 1 / 1",
            title = "🌙 ಇಸ್ಲಾಮಿಕ್ ದಿನಗಳು ಮತ್ತು ಘಟನೆಗಳು",
            desc = "ಆಯ್ಕೆಮಾಡಿದ ದಿನದ ಇಸ್ಲಾಮಿಕ್ ಘಟನೆಗಳು, ಪವಿತ್ರ ಹಬ್ಬಗಳು, ಐತಿಹಾಸಿಕ ದಿನಾಂಕಗಳು ಮತ್ತು ವಿಶೇಷ ಸಂದರ್ಭಗಳನ್ನು ಇಲ್ಲಿ ವೀಕ್ಷಿಸಬಹುದು.",
            done = "ಅರ್ಥವಾಯಿತು ✓",
            skip = "ಬಿಟ್ಟುಬಿಡಿ"
        ),
        "or" to CalendarTourStrings(
            step = "ଗାଇଡ୍ • ୧ / ୧",
            title = "🌙 ଇସଲାମିକ ଇଭେଣ୍ଟ ଏବଂ ତାରିଖ",
            desc = "ଏଠାରେ ଆପଣ ନିର୍ବାଚିତ ଦିନର ଇସଲାମିକ ଇଭେଣ୍ଟ, ପବିତ୍ର ପର୍ବ, ଐତିହାସିକ ତାରିଖ ଏବଂ ବିଶେଷ ଦିନ ଦେଖିପାରିବେ।",
            done = "ବୁଝିଗଲି ✓",
            skip = "ଛାଡ଼ନ୍ତୁ"
        ),
        "as" to CalendarTourStrings(
            step = "নিৰ্দেশিকা • ১ / ১",
            title = "🌙 ইছলামিক দিৱস আৰু ঘটনাসমূহ",
            desc = "ইয়াত আপুনি নিৰ্বাচিত দিনৰ ইছলামিক ঘটনাৱলী, পৱিত্ৰ উৎসৱ, ঐতিহাসিক তাৰিখ আৰু বিশেষ দিনসমূহ চাব পাৰে।",
            done = "বুজি পালোঁ ✓",
            skip = "এৰক"
        ),
        "ms" to CalendarTourStrings(
            step = "Panduan • 1 / 1",
            title = "🌙 Peristiwa & Tarikh Islam",
            desc = "Di sini anda boleh melihat peristiwa Islam, perayaan suci, tarikh bersejarah, dan hari-hari kebesaran bagi hari yang dipilih.",
            done = "Faham ✓",
            skip = "Langkau"
        ),
        "id" to CalendarTourStrings(
            step = "Panduan • 1 / 1",
            title = "🌙 Peristiwa & Tanggal Islam",
            desc = "Di sini Anda dapat melihat peristiwa penting Islam, hari raya suci, tanggal bersejarah, dan peringatan khusus pada tanggal yang dipilih.",
            done = "Paham ✓",
            skip = "Lewati"
        ),
        "tr" to CalendarTourStrings(
            step = "Rehber • 1 / 1",
            title = "🌙 İslami Günler & Olaylar",
            desc = "Seçilen güne ait İslami olayları, mübarek gün ve geceleri, tarihi tarihleri ve özel günleri buradan görebilirsiniz.",
            done = "Anladım ✓",
            skip = "Geç"
        ),
        "fa" to CalendarTourStrings(
            step = "راهنما • ۱ / ۱",
            title = "🌙 مناسبت‌ها و رویدادهای اسلامی",
            desc = "در اینجا می‌توانید مناسبت‌های اسلامی، اعیاد مذهبی، تاریخ‌های تاریخی و ایام خاص روز انتخاب شده را مشاهده کنید.",
            done = "متوجه شدم ✓",
            skip = "رد کردن"
        ),
        "ha" to CalendarTourStrings(
            step = "Jagora • 1 / 1",
            title = "🌙 Muhimman Ranaku a Musulunci",
            desc = "A nan zaka iya duba abubuwan da suka faru a tarihin Musulunci, bukukuwan ibada, da ranaku masu alfarma na ranar da ka zaba.",
            done = "Na gane ✓",
            skip = "Wuce"
        ),
        "sw" to CalendarTourStrings(
            step = "Mwongozo • 1 / 1",
            title = "🌙 Matukio na Tarehe za Kiislamu",
            desc = "Hapa unaweza kuona matukio ya Kiislamu, sikukuu takatifu, tarehe za kihistoria, na siku maalum za siku uliyochagua.",
            done = "Nimeelewa ✓",
            skip = "Ruka"
        ),
        "so" to CalendarTourStrings(
            step = "Hagaha • 1 / 1",
            title = "🌙 Munaasabadaha & Taariikhaha Islaamka",
            desc = "Halkan waxaad ku arki kartaa munaasabadaha Islaamka, ciidaha barakaysan, taariikhaha muhiimka ah, iyo maalmaha gaarka ah ee maalinta la doortay.",
            done = "Waan fahmay ✓",
            skip = "Ka gudub"
        ),
        "fr" to CalendarTourStrings(
            step = "Guide • 1 / 1",
            title = "🌙 Événements & Dates Islamiques",
            desc = "Consultez ici les événements islamiques authentiques, les fêtes sacrées, les repères historiques et les jours bénis pour la date sélectionnée.",
            done = "Compris ✓",
            skip = "Passer"
        ),
        "de" to CalendarTourStrings(
            step = "Leitfaden • 1 / 1",
            title = "🌙 Islamische Ereignisse & Termine",
            desc = "Hier können Sie authentische islamische Ereignisse, gesegnete Feste, historische Meilensteine und besondere Anlässe für den ausgewählten Tag einsehen.",
            done = "Verstanden ✓",
            skip = "Überspringen"
        ),
        "ru" to CalendarTourStrings(
            step = "Гид • 1 / 1",
            title = "🌙 Исламские события и даты",
            desc = "Здесь вы можете узнать о достоверных исламских событиях, священных праздниках, исторических датах и памятных днях для выбранной даты.",
            done = "Понятно ✓",
            skip = "Пропустить"
        ),
        "zh" to CalendarTourStrings(
            step = "指南 • 1 / 1",
            title = "🌙 伊斯兰重大节日与历史事件",
            desc = "在此处您可以查看所选日期的正统伊斯兰节日、圣洁节庆、历史重大事件及特殊纪念日。",
            done = "知道了 ✓",
            skip = "跳过"
        ),
    )

    fun get(lang: String): CalendarTourStrings {
        val code = lang.lowercase().split("-")[0]
        return data[code] ?: data["en"]!!
    }
}
