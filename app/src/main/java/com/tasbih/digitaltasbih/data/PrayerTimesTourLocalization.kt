package com.tasbih.digitaltasbih.data

data class PrayerTimesTourStrings(
    val step: String,
    val next: String,
    val done: String,
    val skip: String,

    // Step 1: Next Prayer Hero Card
    val heroTitle: String,
    val heroDesc: String,

    // Step 2: 5 Prayer Timings Cards
    val timingsTitle: String,
    val timingsDesc: String,

    // Step 3: Local Masjid Jamat Time
    val jamatTitle: String,
    val jamatDesc: String,

    // Step 4: Today's Salah Tracker
    val trackerTitle: String,
    val trackerDesc: String,

    // Step 5: Monthly Timetable & Audio Settings
    val settingsTitle: String,
    val settingsDesc: String
)

object PrayerTimesTourLocalization {

    private val en = PrayerTimesTourStrings(
        step = "Step {curr} of {total}",
        next = "Next ➔",
        done = "Got it! ✓",
        skip = "Skip",
        heroTitle = "🕌 Next Prayer & Live Countdown",
        heroDesc = "Check the upcoming prayer with accurate real-time countdown. Tap the speaker icon to toggle Adhan alert for this prayer.",
        timingsTitle = "⏰ 5 Daily Prayer Timings",
        timingsDesc = "Accurate daily times for Fajr, Dhuhr, Asr, Maghrib, and Isha. Individual speaker icons let you customize Adhan alert per prayer.",
        jamatTitle = "🕌 Local Masjid Jamat Time",
        jamatDesc = "Tap the pencil icon next to 'Jamat' on any prayer card to set your local mosque congregation time or your personal prayer time.",
        trackerTitle = "✅ Today's Salah Tracker",
        trackerDesc = "Tick off prayers you offer today (Fajr, Dhuhr, Asr, Maghrib, Isha) to maintain daily Salah consistency and track streaks.",
        settingsTitle = "⚙️ Adhan Sounds & Timetable",
        settingsDesc = "Tap '🔔' to pick your favorite Adhan voice, '⚙️' to change calculation methods, and scroll down to view the full monthly timetable."
    )

    private val hi = PrayerTimesTourStrings(
        step = "कदम {curr} / {total}",
        next = "आगे बढ़ें ➔",
        done = "समझ गया! ✓",
        skip = "छोड़ें",
        heroTitle = "🕌 अगली नमाज़ और लाइव काउंटडाउन",
        heroDesc = "आने वाली नमाज़ और सटीक लाइव उल्टी गिनती देखें। इस नमाज़ के लिए अज़ान अलर्ट चालू या बंद करने हेतु स्पीकर आइकन दबाएं।",
        timingsTitle = "⏰ दैनिक 5 नमाज़ों के समय",
        timingsDesc = "फ़ज्र, ज़ुहर, अस्र, मग़रिब और इशा के सटीक समय। हर नमाज़ के अलग स्पीकर बटन से आप अपनी पसंद अनुसार अलर्ट सेट कर सकते हैं।",
        jamatTitle = "🕌 स्थानीय मस्जिद का जमात समय",
        jamatDesc = "किसी भी नमाज़ के 'जमात' के पास पेंसिल आइकन दबाकर अपनी स्थानीय मस्जिद की जमात का समय या अपना नमाज़ समय सेट करें।",
        trackerTitle = "✅ आज की नमाज़ ट्रैकर",
        trackerDesc = "दिनभर में पढ़ी गई नमाज़ों (फ़ज्र, ज़ुहर, अस्र, मग़रिब, इशा) पर सही का निशान लगाएं और अपनी नमाज़ों की पाबंदी बनाए रखें।",
        settingsTitle = "⚙️ अज़ान आवाज़ और टाइमटेबल",
        settingsDesc = "अपनी पसंदीदा अज़ान आवाज़ चुनने के लिए '🔔' दबाएं, हिसाब पद्धति बदलने के लिए '⚙️' दबाएं, और पूरा मासिक टाइमटेबल नीचे देखें।"
    )

    private val ur = PrayerTimesTourStrings(
        step = "مرحلہ {curr} از {total}",
        next = "اگلا ➔",
        done = "سمجھ گیا! ✓",
        skip = "چھوڑیں",
        heroTitle = "🕌 اگلی نماز اور لائیو الٹی گنتی",
        heroDesc = "اگلی نماز اور اس کے وقت کی لائیو الٹی گنتی دیکھیں۔ اس نماز کے لیے اذان کا الرٹ آن یا آف کرنے کے لیے اسپیکر پر ٹیپ کریں۔",
        timingsTitle = "⏰ روزانہ کی 5 نمازوں کے اوقات",
        timingsDesc = "فجر، ظہر، عصر، مغرب اور عشاء کے صحیح اوقات۔ ہر نماز کے الگ اسپیکر آئیکن سے آپ انفرادی اذان الرٹ کا انتخاب کر سکتے ہیں۔",
        jamatTitle = "🕌 مقامی مسجد کی جماعت کا وقت",
        jamatDesc = "کسی بھی نماز کے 'جماعت' کے ساتھ پنسل آئیکن پر ٹیپ کر کے اپنی مقامی مسجد کی جماعت کا وقت یا اپنا ذاتی وقت سیٹ کریں۔",
        trackerTitle = "✅ آج کا نماز ٹریکر",
        trackerDesc = "دن بھر میں پڑھی گئی نمازوں کو ٹک کریں اور روزانہ نمازوں کی پابندی اور تسلسل کو برقرار رکھیں۔",
        settingsTitle = "⚙️ اذان کی آوازیں اور ٹائم ٹیبل",
        settingsDesc = "پسندیدہ اذان چننے کے لیے '🔔' دبائیں، حساب کے طریقے بدلنے کے لیے '⚙️' دبائیں اور نیچے ماہانہ ٹائم ٹیبل دیکھیں۔"
    )

    private val ar = PrayerTimesTourStrings(
        step = "الخطوة {curr} من {total}",
        next = "التالي ➔",
        done = "فهمت! ✓",
        skip = "تخطي",
        heroTitle = "🕌 الصلاة القادمة والعد التنازلي",
        heroDesc = "تابع الصلاة القادمة مع عد تنازلي مباشر ودقيق. انقر على أيقونة مكبر الصوت لتفعيل أو كتم صوت الأذان لهذه الصلاة.",
        timingsTitle = "⏰ مواقيت الصلوات الخمس",
        timingsDesc = "مواقيت دقيقة لكل من الفجر والظهر والعصر والمغرب والعشاء مع إمكانية تخصيص تنبيه الأذان لكل صلاة بشكل مستقل.",
        jamatTitle = "🕌 وقت صلاة الجماعة في المسجد",
        jamatDesc = "اضغط على أيقونة القلم بجانب 'الجماعة' لضبط موعد صلاة الجماعة في مسجدك المحلي أو وقت صلاتك الخاص.",
        trackerTitle = "✅ متابعة صلوات اليوم",
        trackerDesc = "قم بوضع علامة صح على الصلوات التي أديتها طوال اليوم للحفاظ على أداء الصلوات في وقتها وبناء استمرارية مباركة.",
        settingsTitle = "⚙️ أصوات الأذان وجدول الصلوات",
        settingsDesc = "انقر على '🔔' لاختيار صوت الأذان المفضل، و'⚙️' لتعديل طرق الحساب الفقهية، وتمرير لأسفل لعرض التقويم الشهري الكامل."
    )

    private val bn = PrayerTimesTourStrings(
        step = "ধাপ {curr} / {total}",
        next = "পরবর্তী ➔",
        done = "বুঝেছি! ✓",
        skip = "এড়িয়ে যান",
        heroTitle = "🕌 পরবর্তী নামাজ ও লাইভ কাউন্টডাউন",
        heroDesc = "পরবর্তী নামাজের সময় এবং লাইভ কাউন্টডাউন দেখুন। এই নামাজের জন্য আজান সতর্কতা চালু বা বন্ধ করতে স্পিকার আইকনে ট্যাপ করুন।",
        timingsTitle = "⏰ ৫ ওয়াক্ত নামাজের সময়সূচি",
        timingsDesc = "ফজর, জোহর, আসর, মাগরিব এবং এশার সঠিক সময়। প্রতিটি নামাজের আলাদা স্পিকার বাটনে আজান কাস্টমাইজ করুন।",
        jamatTitle = "🕌 স্থানীয় মসজিদের জামাতের সময়",
        jamatDesc = "যেকোনো নামাজের 'জামত' এর পাশের পেন্সিল আইকন চাপুন এবং আপনার মসজিদের জামাতের সময় বা ব্যক্তিগত সময় সেট করুন।",
        trackerTitle = "✅ আজকের সালাত ট্র্যাকার",
        trackerDesc = "সারাদিনের আদায় করা নামাজগুলোতে টিক দিন এবং নিয়মিত ৫ ওয়াক্ত নামাজ আদায়ের অভ্যাস গড়ে তুলুন।",
        settingsTitle = "⚙️ আজান সুর ও মাসিক সময়সূচি",
        settingsDesc = "পছন্দের আজানের সুরের জন্য '🔔' চাপুন, গণনার পদ্ধতি পরিবর্তনের জন্য '⚙️' চাপুন এবং নিচে সম্পূর্ণ মাসিক সময়সূচি দেখুন।"
    )

    private val gu = PrayerTimesTourStrings(
        step = "પગલું {curr} / {total}",
        next = "આગળ ➔",
        done = "સમજાઈ ગયું! ✓",
        skip = "છોડો",
        heroTitle = "🕌 આગામી નમાઝ અને લાઈવ કાઉન્ટડાઉન",
        heroDesc = "આગામી નમાઝ અને સચોટ લાઈવ કાઉન્ટડાઉન જુઓ. અઝાન એલર્ટ સેટ કરવા માટે સ્પીકર આઈકન પર ટેપ કરો.",
        timingsTitle = "⏰ દૈનિક ૫ નમાઝના ચોક્કસ સમય",
        timingsDesc = "ફજર, ઝુહર, અસ્ર, મગરીબ અને ઈશાના સચોટ સમય. દરેક નમાઝ માટે અલગ સ્પીકર આઈકનથી અઝાન નોટિફિકેશન કસ્ટમાઈઝ કરો.",
        jamatTitle = "🕌 સ્થાનિક મસ્જિદનો જમાઅત સમય",
        jamatDesc = "કોઈપણ નમાઝના 'જમાઅત' પાસે પેન્સિલ આઈકન પર ક્લિક કરીને તમારી મસ્જિદનો જમાઅત સમય સેટ કરો.",
        trackerTitle = "✅ આજનો નમાઝ ટ્રેકર",
        trackerDesc = "આજના દિવસે પઢેલી નમાઝો પર ખરાનું નિશાન કરો અને નિયમિત નમાઝનું સાતત્ય જાળવી રાખો.",
        settingsTitle = "⚙️ અઝાન અવાજ અને સમયપત્રક",
        settingsDesc = "મનપસંદ અઝાન પસંદ કરવા '🔔' દબાવો, ગણતરી પદ્ધતિ બદલવા '⚙️' દબાવો અને નીચે આખા મહિનાનું ટાઈમટેબલ જુઓ."
    )

    private val ta = PrayerTimesTourStrings(
        step = "படி {curr} / {total}",
        next = "அடுத்து ➔",
        done = "புரிந்தது! ✓",
        skip = "தவிர்",
        heroTitle = "🕌 அடுத்த தொழுகை & நேரடி கவுண்டவுன்",
        heroDesc = "அடுத்த தொழுகை மற்றும் துல்லியமான கவுண்டவுனைக் காண்க. அதான் அறிவிப்பை ஆன்/ஆஃப் செய்ய ஸ்பீக்கர் ஐகானைத் தட்டவும்.",
        timingsTitle = "⏰ 5 நேர தொழுகை நேரங்கள்",
        timingsDesc = "ஃபஜ்ர், ளுஹ்ர், அஸ்ர், மஃக்ரிப் மற்றும் இஷா தொழுகைகளின் துல்லியமான நேரங்கள்.",
        jamatTitle = "🕌 உள்ளூர் பள்ளிவாசல் ஜமாஅத் நேரம்",
        jamatDesc = "உங்கள் பள்ளிவாசலின் ஜமாஅத் நேரத்தை அமைக்க 'ஜமாஅத்' அருகிலுள்ள பென்சில் ஐகானைத் தட்டவும்.",
        trackerTitle = "✅ இன்றைய தொழுகை டிராக்கர்",
        trackerDesc = "இன்று நீங்கள் தொழுத தொழுகைகளைக் குறித்துக் கொண்டு தொழுகைத் தொடர்ச்சியைப் பேணுங்கள்.",
        settingsTitle = "⚙️ அதான் ஒலிகள் & மாதாந்திர அட்டவணை",
        settingsDesc = "அதான் ஒலிகளை மாற்ற '🔔' அழுத்தவும், அமைப்புகளை மாற்ற '⚙️' அழுத்தவும்."
    )

    private val te = PrayerTimesTourStrings(
        step = "దశ {curr} / {total}",
        next = "తరువాత ➔",
        done = "అర్థమైంది! ✓",
        skip = "దాటవేయి",
        heroTitle = "🕌 తదుపరి నమాజ్ & లైవ్ కౌంట్‌డౌన్",
        heroDesc = "తదుపరి నమాజ్ మరియు ఖచ్చితమైన లైవ్ కౌంట్‌డౌన్‌ను చూడండి. అజాన్ అలర్ట్ ఆన్/ఆఫ్ చేయడానికి స్పీకర్ చిహ్నాన్ని నొక్కండి.",
        timingsTitle = "⏰ రోజువారీ 5 నమాజ్ సమయాలు",
        timingsDesc = "ఫజ్ర్, జుహ్ర్, అసర్, మగ్రిబ్ మరియు ఇషా నమాజుల ఖచ్చితమైన సమయాలు.",
        jamatTitle = "🕌 స్థానిక మసీదు జమాత్ సమయం",
        jamatDesc = "మీ స్థానిక మసీదు జమాత్ సమయాన్ని సెట్ చేయడానికి పెన్సిల్ చిహ్నాన్ని నొక్కండి.",
        trackerTitle = "✅ నేటి నమాజ్ ట్రాకర్",
        trackerDesc = "ఈరోజు మీరు చదివిన నమాజులను టిక్ చేసి నమాజ్ అలవాటును కొనసాగించండి.",
        settingsTitle = "⚙️ అజాన్ శబ్దాలు & నెలవారీ సమయ పట్టిక",
        settingsDesc = "అజాన్ స్వరాన్ని మార్చడానికి '🔔', సెట్టింగ్‌ల కోసం '⚙️' నొక్కండి."
    )

    private val ml = PrayerTimesTourStrings(
        step = "ഘട്ടം {curr} / {total}",
        next = "അടുത്തത് ➔",
        done = "മനസ്സിലായി! ✓",
        skip = "ഒഴിവാക്കുക",
        heroTitle = "🕌 അടുത്ത നമസ്കാരവും കൗണ്ട്ഡൗണും",
        heroDesc = "അടുത്ത നമസ്കാര സമയവും ലൈവ് കൗണ്ട്ഡൗണും കാണുക. ബാങ്ക് അലേർട്ടിനായി സ്പീക്കർ ഐക്കൺ ടാപ്പ് ചെയ്യുക.",
        timingsTitle = "⏰ 5 നേരത്തെ നമസ്കാര സമയങ്ങൾ",
        timingsDesc = "സുബ്ഹ്, ദുഹ്ര്, അസർ, മഗ്‌രിബ്, ഇശാ നമസ്കാരങ്ങളുടെ കൃത്യമായ സമയങ്ങൾ.",
        jamatTitle = "🕌 പ്രാദേശിക പള്ളിയിലെ ജമാഅത്ത് സമയം",
        jamatDesc = "നിങ്ങളുടെ മഹല്ല് പള്ളിയിലെ ജമാഅത്ത് സമയം ക്രമീകരിക്കാൻ പെൻസിൽ ഐക്കൺ ടാപ്പ് ചെയ്യുക.",
        trackerTitle = "✅ ഇന്നത്തെ നമസ്കാര ട്രാക്കർ",
        trackerDesc = "നിർവ്വഹിച്ച നമസ്കാരങ്ങൾ അടയാളപ്പെടുത്തി നമസ്കാര നിഷ്ഠ നിലനിർത്തുക.",
        settingsTitle = "⚙️ ബാങ്ക് ശബ്ദങ്ങളും മാസ കലണ്ടറും",
        settingsDesc = "ബാങ്ക് ശബ്ദം മാറ്റാൻ '🔔' ഐക്കണും ക്രമീകരണങ്ങൾക്കായി '⚙️' ഐക്കണും ഉപയോഗിക്കുക."
    )

    private val mr = PrayerTimesTourStrings(
        step = "पायरी {curr} / {total}",
        next = "पुढे ➔",
        done = "समजले! ✓",
        skip = "वगळा",
        heroTitle = "🕌 पुढची नमाज आणि थेट काउंटडाउन",
        heroDesc = "पुढची नमाज आणि अचूक वेळ पहा. या नमाजसाठी अजान अलर्ट चालू/बंद करण्यासाठी स्पीकरवर टॅप करा.",
        timingsTitle = "⏰ दैनंदिन ५ नमाज वेळा",
        timingsDesc = "फज्र, जोहर, असर, मगरिब आणि इशा या सर्व नमाजांच्या अचूक वेळा.",
        jamatTitle = "🕌 स्थानिक मशिदीची जमात वेळ",
        jamatDesc = "स्थानिक मशिदीची जमात वेळ सेट करण्यासाठी पेन्सिल आयकॉनवर क्लिक करा.",
        trackerTitle = "✅ आजचा नमाज ट्रॅकर",
        trackerDesc = "पढलेल्या नमाजांवर बरोबरची खूण करा आणि नियमितता टिकवून ठेवा.",
        settingsTitle = "⚙️ अजान आवाज आणि वेळापत्रक",
        settingsDesc = "अजान आवाज बदलण्यासाठी '🔔' आणि सेटिंग्जसाठी '⚙️' दाबा."
    )

    private val pa = PrayerTimesTourStrings(
        step = "ਕਦਮ {curr} / {total}",
        next = "ਅੱਗੇ ➔",
        done = "ਸਮਝ ਗਿਆ! ✓",
        skip = "ਛੱਡੋ",
        heroTitle = "🕌 ਅਗਲੀ ਨਮਾਜ਼ ਅਤੇ ਲਾਈਵ ਕਾਊਂਟਡਾਊਨ",
        heroDesc = "ਅਗਲੀ ਨਮਾਜ਼ ਅਤੇ ਸਹੀ ਕਾਊਂਟਡਾਊਨ ਦੇਖੋ। ਅਜ਼ਾਨ ਅਲਰਟ ਲਈ ਸਪੀਕਰ ਆਈਕਨ 'ਤੇ ਟੈਪ ਕਰੋ।",
        timingsTitle = "⏰ ਰੋਜ਼ਾਨਾ 5 ਨਮਾਜ਼ਾਂ ਦੇ ਸਮੇਂ",
        timingsDesc = "ਫ਼ਜਰ, ਜ਼ੁਹਰ, ਅਸਰ, ਮਗ਼ਰਿਬ ਅਤੇ ਇਸ਼ਾ ਦੇ ਸਹੀ ਸਮੇਂ।",
        jamatTitle = "🕌 ਮਸਜਿਦ ਦੀ ਜਮਾਤ ਦਾ ਸਮਾਂ",
        jamatDesc = "ਜਮਾਤ ਦਾ ਸਮਾਂ ਸੈੱਟ ਕਰਨ ਲਈ ਪੈਨਸਿਲ ਆਈਕਨ 'ਤੇ ਕਲਿੱਕ ਕਰੋ।",
        trackerTitle = "✅ ਅੱਜ ਦਾ ਨਮਾਜ਼ ਟਰੈਕਰ",
        trackerDesc = "ਪੜ੍ਹੀਆਂ ਗਈਆਂ ਨਮਾਜ਼ਾਂ ਨੂੰ ਟਿੱਕ ਕਰੋ ਅਤੇ ਰੋਜ਼ਾਨਾ ਪਾਬੰਦੀ ਬਣਾਈ ਰੱਖੋ।",
        settingsTitle = "⚙️ ਅਜ਼ਾਨ ਆਵਾਜ਼ਾਂ ਅਤੇ ਟਾਈਮਟੇਬਲ",
        settingsDesc = "ਆਵਾਜ਼ ਲਈ '🔔' ਅਤੇ ਸੈਟਿੰਗਜ਼ ਲਈ '⚙️' ਦਬਾਓ।"
    )

    private val kn = PrayerTimesTourStrings(
        step = "ಹಂತ {curr} / {total}",
        next = "ಮುಂದೆ ➔",
        done = "ತಿಳಿಯಿತು! ✓",
        skip = "ಬಿಟ್ಟುಬಿಡಿ",
        heroTitle = "🕌 ಮುಂದಿನ ನಮಾಜ್ & ಲೈವ್ ಕೌಂಟ್‌ಡೌನ್",
        heroDesc = "ಮುಂದಿನ ನಮಾಜ್ ಮತ್ತು ನಿಖರವಾದ ಕೌಂಟ್‌ಡೌನ್ ವೀಕ್ಷಿಸಿ. ಅಜಾನ್ ಎಚ್ಚರಿಕೆಗಾಗಿ ಸ್ಪೀಕರ್ ಐಕಾನ್ ಟ್ಯಾಪ್ ಮಾಡಿ.",
        timingsTitle = "⏰ ದೈನಂದಿನ 5 ನಮಾಜ್ ಸಮಯಗಳು",
        timingsDesc = "ಫಜ್ರ್, ಝುಹ್ರ್, ಅಸ್ರ್, ಮಗ್ರಿಬ್ ಮತ್ತು ಇಶಾ ನಮಾಜ್ ಸಮಯಗಳು.",
        jamatTitle = "🕌 ಸ್ಥಳೀಯ ಮಸೀದಿಯ ಜಮಾತ್ ಸಮಯ",
        jamatDesc = "ಮಸೀದಿಯ ಜಮಾತ್ ಸಮಯ ಹೊಂದಿಸಲು ಪೆನ್ಸಿಲ್ ಐಕಾನ್ ಮೇಲೆ ಟ್ಯಾಪ್ ಮಾಡಿ.",
        trackerTitle = "✅ ಇಂದಿನ ನಮಾಜ್ ಟ್ರ್ಯಾಕರ್",
        trackerDesc = "ನೀವು ನಿರ್ವಹಿಸಿದ ನಮಾಜ್‌ಗಳನ್ನು ಗುರುತಿಸಿ ನಮಾಜಿನ ನಿಷ್ಠೆ ಕಾಪಾಡಿಕೊಳ್ಳಿ.",
        settingsTitle = "⚙️ ಅಜಾನ್ ಶಬ್ದಗಳು & ವೇಳಾಪಟ್ಟಿ",
        settingsDesc = "ಅಜಾನ್ ಶಬ್ದ ಬದಲಿಸಲು '🔔' ಮತ್ತು ಸೆಟ್ಟಿಂಗ್ಸ್‌ಗೆ '⚙️' ಒತ್ತಿರಿ."
    )

    private val or = PrayerTimesTourStrings(
        step = "ପଦକ୍ଷେପ {curr} / {total}",
        next = "ପରବର୍ତ୍ତୀ ➔",
        done = "ବୁଝିଗଲି! ✓",
        skip = "ଛାଡ଼ନ୍ତୁ",
        heroTitle = "🕌 ପରବର୍ତ୍ତୀ ନମାଜ୍ ଏବଂ କାଉଣ୍ଟଡାଉନ୍",
        heroDesc = "ପରବର୍ତ୍ତୀ ନମାଜ୍ ସମୟ ଦେଖନ୍ତୁ ଏବଂ ଅଜାନ ଆଲର୍ଟ ସେଟ୍ କରନ୍ତୁ।",
        timingsTitle = "⏰ ଦୈନିକ ୫ ୱକ୍ତ ନମାଜ୍ ସମୟ",
        timingsDesc = "ଫଜର, ଜୋହର, ଅସର, ମଗରିବ ଏବଂ ଇଶା ନମାଜ୍ର ସଠିକ୍ ସମୟ।",
        jamatTitle = "🕌 ମସଜିଦ୍ ଜମାତ ସମୟ",
        jamatDesc = "ଜମାତ ସମୟ ସେଟ୍ କରିବା ପାଇଁ ପେନସିଲ୍ ଆଇକନ୍ ଦବାନ୍ତୁ।",
        trackerTitle = "✅ ଆଜିର ନମାଜ୍ ଟ୍ରାକର୍",
        trackerDesc = "ପଢ଼ିଥିବା ନମାଜ୍କୁ ଟିକ୍ କରି ଟ୍ରାକ୍ କରନ୍ତୁ।",
        settingsTitle = "⚙️ ଅଜାନ ଧ୍ୱନି ଏବଂ ସେଟିଂସ୍",
        settingsDesc = "ଅଜାନ ଧ୍ୱନି ପାଇଁ '🔔' ଏବଂ ସେଟିଂସ୍ ପାଇଁ '⚙️' ଦବାନ୍ତୁ।"
    )

    private val `as` = PrayerTimesTourStrings(
        step = "পদক্ষেপ {curr} / {total}",
        next = "পৰৱৰ্তী ➔",
        done = "বুজি পালোঁ! ✓",
        skip = "এৰাই চলক",
        heroTitle = "🕌 পৰৱৰ্তী নামাজ আৰু লাইভ কাউণ্টডাউন",
        heroDesc = "পৰৱৰ্তী নামাজৰ সময় আৰু লাইভ কাউণ্টডাউন চাওক। আজান এলাৰ্টৰ বাবে স্পীকাৰত টিপক।",
        timingsTitle = "⏰ দৈনিক ৫ ৱাক্ত নামাজৰ সময়সূচী",
        timingsDesc = "ফজৰ, জোহৰ, আচৰ, মাগৰিব আৰু এশা নামাজৰ সঠিক সময়।",
        jamatTitle = "🕌 জামাতৰ সময়",
        jamatDesc = "মছজিদৰ জামাতৰ সময় সংহতি কৰিবলৈ পেঞ্চিল আইকনত টিপক।",
        trackerTitle = "✅ আজিৰ চালাত ট্ৰেকাৰ",
        trackerDesc = "আদায় কৰা নামাজসমূহ টিক চিন দি নিয়মীয়া কৰক।",
        settingsTitle = "⚙️ আজান ধ্বনি আৰু সংহতি",
        settingsDesc = "আজানৰ বাবে '🔔' আৰু সংহতিৰ বাবে '⚙️' ব্যৱহাৰ কৰক।"
    )

    private val ms = PrayerTimesTourStrings(
        step = "Langkah {curr} / {total}",
        next = "Seterusnya ➔",
        done = "Faham! ✓",
        skip = "Langkau",
        heroTitle = "🕌 Solat Seterusnya & Kiraan Masa",
        heroDesc = "Lihat waktu solat seterusnya dengan kiraan masa langsung. Ketik ikon pembesar suara untuk menetapkan pemberitahuan Azan.",
        timingsTitle = "⏰ Waktu 5 Solat Fardhu",
        timingsDesc = "Waktu tepat bagi Subuh, Zohor, Asar, Maghrib dan Isyak mengikut lokasi semasa anda.",
        jamatTitle = "🕌 Waktu Solat Berjemaah",
        jamatDesc = "Ketik ikon pensel di sebelah 'Jemaah' untuk menetapkan waktu jemaah masjid kawasan anda.",
        trackerTitle = "✅ Penjejak Solat Harian",
        trackerDesc = "Tandakan solat yang telah anda tunaikan hari ini bagi membina konsistensi ibadah.",
        settingsTitle = "⚙️ Pilihan Suara Azan & Tetapan",
        settingsDesc = "Ketik '🔔' untuk memilih suara muazzin kegemaran, dan '⚙️' untuk kaedah kiraan solat."
    )

    private val id = PrayerTimesTourStrings(
        step = "Langkah {curr} dari {total}",
        next = "Lanjut ➔",
        done = "Paham! ✓",
        skip = "Lewati",
        heroTitle = "🕌 Salat Berikutnya & Hitung Mundur",
        heroDesc = "Pantau jadwal salat berikutnya dengan hitung mundur langsung. Ketuk ikon speaker untuk mengaktifkan notifikasi Adzan.",
        timingsTitle = "⏰ Jadwal 5 Waktu Salat",
        timingsDesc = "Waktu akurat Subuh, Dzuhur, Ashar, Maghrib, dan Isya sesuai lokasi GPS Anda.",
        jamatTitle = "🕌 Waktu Salat Jamaah Masjid",
        jamatDesc = "Ketuk ikon pensil di samping 'Jamaah' untuk mengatur waktu sholat jamaah di masjid terdekat.",
        trackerTitle = "✅ Pelacak Salat Harian",
        trackerDesc = "Centang salat yang telah Anda kerjakan hari ini untuk menjaga istiqomah ibadah.",
        settingsTitle = "⚙️ Pilihan Suara Adzan & Pengaturan",
        settingsDesc = "Ketuk '🔔' untuk memilih suara muadzin dan '⚙️' untuk metode perhitungan hisab."
    )

    private val tr = PrayerTimesTourStrings(
        step = "Adım {curr} / {total}",
        next = "İleri ➔",
        done = "Anladım! ✓",
        skip = "Geç",
        heroTitle = "🕌 Sonraki Vakit & Canlı Geri Sayım",
        heroDesc = "Yaklaşan namaz vaktini ve canlı geri sayımı takip edin. Ezan bildirimi için hoparlör simgesine dokunun.",
        timingsTitle = "⏰ 5 Vakit Namaz Saatleri",
        timingsDesc = "İmsak, Öğle, İkindi, Akşam ve Yatsı için bulunduğunuz konuma göre tam ve doğru vakitler.",
        jamatTitle = "🕌 Cami Cemaat Vakti",
        jamatDesc = "Mahallenizdeki caminin cemaatle kılınış vaktini ayarlamak için kalem simgesine dokunun.",
        trackerTitle = "✅ Günlük Namaz Takipçisi",
        trackerDesc = "Kıldığınız namazları işaretleyin ve düzenli namaz kılma alışkanlığınızı takip edin.",
        settingsTitle = "⚙️ Ezan Sesleri & Ayarlar",
        settingsDesc = "Ezan sesini seçmek için '🔔', hesaplama yöntemleri için '⚙️' simgesine dokunun."
    )

    private val fa = PrayerTimesTourStrings(
        step = "مرحله {curr} از {total}",
        next = "بعدی ➔",
        done = "متوجه شدم! ✓",
        skip = "رد کردن",
        heroTitle = "🕌 نماز بعدی و شمارش معکوس زنده",
        heroDesc = "مشاهده نماز پیش رو و شمارش معکوس دقیق. برای زنگ اذان، روی بلندگو ضربه بزنید.",
        timingsTitle = "⏰ اوقات شرعی نمازهای پنجگانه",
        timingsDesc = "اوقات دقیق صبح، ظهر، عصر، مغرب و عشاء مطابق موقعیت مکانی شما.",
        jamatTitle = "🕌 زمان نماز جماعت مسجد",
        jamatDesc = "با زدن روی علامت مداد کنار 'جماعت'، زمان جماعت مسجد محله خود را تنظیم کنید.",
        trackerTitle = "✅ پیگیری روزانه نمازها",
        trackerDesc = "نمازهای خوانده شده امروز را علامت بزنید تا پایداری در نماز را حفظ کنید.",
        settingsTitle = "⚙️ نوای اذان و تنظیمات",
        settingsDesc = "برای انتخاب صدای اذان روی '🔔' و برای تنظیمات روی '⚙️' ضربه بزنید."
    )

    private val ha = PrayerTimesTourStrings(
        step = "Mataki {curr} na {total}",
        next = "Gaba ➔",
        done = "Na gane! ✓",
        skip = "Wuce",
        heroTitle = "🕌 Sallar Gaba & Ƙidaya Kai Tsaye",
        heroDesc = "Duba lokacin sallah mai zuwa da ƙidayar lokaci. Taɓa hoton lasifika don kunna kiran sallah.",
        timingsTitle = "⏰ Lokutan Salloli 5 na Rana",
        timingsDesc = "Cikakkun lokutan Asubahi, Azahar, La'asar, Magriba da Isha'i.",
        jamatTitle = "🕌 Lokacin Jam'i a Masallaci",
        jamatDesc = "Taɓa alamar fensir don saita lokacin jam'i a masallacinku.",
        trackerTitle = "✅ Bibiyar Sallolin Yau",
        trackerDesc = "Yi alama ga sallolin da ka gabatar yau don kiyaye ibada.",
        settingsTitle = "⚙️ Sautukan Kiran Sallah & Tsare-tsare",
        settingsDesc = "Taɓa '🔔' don zaɓar sautin kiran sallah, '⚙️' don tsare-tsare."
    )

    private val sw = PrayerTimesTourStrings(
        step = "Hatua {curr} ya {total}",
        next = "Mbele ➔",
        done = "Nimeelewa! ✓",
        skip = "Ruka",
        heroTitle = "🕌 Sala Inayofuata & Hesabu Moja kwa Moja",
        heroDesc = "Angalia sala inayofuata na muda uliobaki. Bonyeza kipaza sauti kuweka arifa ya Adhana.",
        timingsTitle = "⏰ Nyakati za Sala 5 za Kila Siku",
        timingsDesc = "Muda sahihi wa Alfajiri, Adhuhuri, Alasiri, Maghribi na Isha kulingana na eneo lako.",
        jamatTitle = "🕌 Muda wa Sala ya Jamaa Msikitini",
        jamatDesc = "Bonyeza alama ya penseli kurekebisha muda wa jamaa wa msikiti wako.",
        trackerTitle = "✅ Mfuatiliaji wa Sala za Leo",
        trackerDesc = "Weka alama kwenye sala ulizoswali leo ili kuimarisha msimamo wako wa ibada.",
        settingsTitle = "⚙️ Sauti za Adhana & Mipangilio",
        settingsDesc = "Bonyeza '🔔' kuchagua sauti ya muadhini na '⚙️' kwa njia za mahesabu."
    )

    private val so = PrayerTimesTourStrings(
        step = "Talaabada {curr} ee {total}",
        next = "Xiga ➔",
        done = "Waan fahmay! ✓",
        skip = "Ka gudub",
        heroTitle = "🕌 Salaadda Xigta & Tirinta Tooska ah",
        heroDesc = "Ogow salaadda xigta iyo inta ka dhiman. Taabo sameecadda si aad u furto aadaanka.",
        timingsTitle = "⏰ Waqtiyada 5-ta Salaadood",
        timingsDesc = "Waqtiyo sax ah oo loogu talagalay Subax, Duhur, Casar, Maqrib iyo Cishe.",
        jamatTitle = "🕌 Waqtiga Jamaca Masaajidka",
        jamatDesc = "Taabo qalinkaa si aad u geliso waqtiga jamaacada masaajidkaaga.",
        trackerTitle = "✅ Dabagalka Salaadaha Maanta",
        trackerDesc = "Calaamadee salaadaha aad tukatay maanta si aad u joogteyso salaadda.",
        settingsTitle = "⚙️ Codadka Aadaanka & Dejinta",
        settingsDesc = "Taabo '🔔' si aad cod aadaan u doorato, '⚙️' si aad hababka u beddesho."
    )

    private val fr = PrayerTimesTourStrings(
        step = "Étape {curr} sur {total}",
        next = "Suivant ➔",
        done = "Compris! ✓",
        skip = "Passer",
        heroTitle = "🕌 Prochaine Prière & Compte à Rebours",
        heroDesc = "Consultez la prochaine prière et le compte à rebours précis. Appuyez sur le haut-parleur pour activer l'alerte Adhan.",
        timingsTitle = "⏰ Horaires des 5 Prières Quotidiennes",
        timingsDesc = "Heures précises pour Fajr, Dhuhr, Asr, Maghrib et Isha basées sur votre localisation.",
        jamatTitle = "🕌 Heure de la Prière en Groupe (Jamat)",
        jamatDesc = "Appuyez sur l'icône crayon à côté de 'Jamat' pour régler l'heure de la congrégation de votre mosquée.",
        trackerTitle = "✅ Suivi des Prières d'Aujourd'hui",
        trackerDesc = "Cochez les prières accomplies dans la journée pour maintenir votre régularité et votre assiduité.",
        settingsTitle = "⚙️ Voix d'Adhan & Paramètres",
        settingsDesc = "Appuyez sur '🔔' pour choisir la voix de l'Adhan et sur '⚙️' pour régler les méthodes de calcul."
    )

    private val de = PrayerTimesTourStrings(
        step = "Schritt {curr} von {total}",
        next = "Weiter ➔",
        done = "Verstanden! ✓",
        skip = "Überspringen",
        heroTitle = "🕌 Nächstes Gebet & Live-Countdown",
        heroDesc = "Sehen Sie das nächste Gebet und den sekundengenauen Countdown. Tippen Sie auf das Lautsprechersymbol für den Adhan-Alarm.",
        timingsTitle = "⏰ Die 5 täglichen Gebetszeiten",
        timingsDesc = "Genaue Zeiten für Fadschr, Zuhr, Asr, Maghrib und Ischa basierend auf Ihrem Standort.",
        jamatTitle = "🕌 Jamat-Zeit der örtlichen Moschee",
        jamatDesc = "Tippen Sie auf das Stiftsymbol neben 'Jamat', um die Gemeindezeit Ihrer Moschee einzustellen.",
        trackerTitle = "✅ Heutiger Gebetstracker",
        trackerDesc = "Haken Sie verrichtete Gebete ab, um Ihre tägliche Beständigkeit im Gebet aufrechtzuerhalten.",
        settingsTitle = "⚙️ Adhan-Stimmen & Einstellungen",
        settingsDesc = "Tippen Sie auf '🔔', um Ihre Lieblings-Adhan-Stimme auszuwählen, und auf '⚙️' für die Berechnungsmethode."
    )

    private val ru = PrayerTimesTourStrings(
        step = "Шаг {curr} из {total}",
        next = "Далее ➔",
        done = "Понятно! ✓",
        skip = "Пропустить",
        heroTitle = "🕌 Следующий намаз и обратный отсчёт",
        heroDesc = "Смотрите время до следующего намаза в реальном времени. Нажмите на значок динамика для включения азана.",
        timingsTitle = "⏰ Точное расписание 5 намазов",
        timingsDesc = "Точное время для Фаджр, Зухр, Аср, Магриб и Иша в соответствии с вашим местоположением.",
        jamatTitle = "🕌 Время джамаата в мечети",
        jamatDesc = "Нажмите на карандаш рядом с 'Джамаат', чтобы установить время коллективного намаза в вашей мечети.",
        trackerTitle = "✅ Трекер намазов на сегодня",
        trackerDesc = "Отмечайте прочитанные сегодня намазы, чтобы сохранять постоянство в поклонении.",
        settingsTitle = "⚙️ Выбор азана и настройки",
        settingsDesc = "Нажмите '🔔' для выбора голоса азана, '⚙️' для настройки методов расчёта времени."
    )

    private val zh = PrayerTimesTourStrings(
        step = "第 {curr} 步 / 共 {total} 步",
        next = "下一步 ➔",
        done = "明白! ✓",
        skip = "跳过",
        heroTitle = "🕌 下次礼拜与实时倒计时",
        heroDesc = "查看下一次礼拜时间与实时倒计时。点击扬声器图标可为此番拜功开启或关闭宣礼宣呼唤。",
        timingsTitle = "⏰ 每日五番拜精确时刻",
        timingsDesc = "晨礼、晌礼、晡礼、昏礼和宵礼的精确时刻，支持为每番拜独立定制提醒方式。",
        jamatTitle = "🕌 当地清真寺集体拜时间",
        jamatDesc = "点击“集体拜”旁边的铅笔图标，可输入设置您所在清真寺的集体拜时间或个人习惯时间。",
        trackerTitle = "✅ 今日礼拜跟进打卡",
        trackerDesc = "完成礼拜后在此勾选标记，帮助您保持每日五番拜功的按时与自律。",
        settingsTitle = "⚙️ 宣礼声音与拜度设置",
        settingsDesc = "点击“🔔”挑选喜爱的宣礼宣呼声，点击“⚙️”可选择法学计算标准和查看月历时间表。"
    )

    fun get(lang: String): PrayerTimesTourStrings {
        val clean = lang.lowercase().split("-", "_")[0]
        return when (clean) {
            "en" -> en
            "hi" -> hi
            "ur" -> ur
            "ar" -> ar
            "bn" -> bn
            "gu" -> gu
            "ta" -> ta
            "te" -> te
            "ml" -> ml
            "mr" -> mr
            "pa" -> pa
            "kn" -> kn
            "or" -> or
            "as" -> `as`
            "ms" -> ms
            "id" -> id
            "tr" -> tr
            "fa" -> fa
            "ha" -> ha
            "sw" -> sw
            "so" -> so
            "fr" -> fr
            "de" -> de
            "ru" -> ru
            "zh" -> zh
            else -> en
        }
    }
}
