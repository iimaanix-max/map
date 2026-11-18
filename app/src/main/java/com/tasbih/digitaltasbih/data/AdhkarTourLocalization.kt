package com.tasbih.digitaltasbih.data

data class AdhkarTourStrings(
    val step: String,
    val reciteTitle: String,
    val reciteDesc: String,
    val progressTitle: String,
    val progressDesc: String,
    val next: String,
    val done: String,
    val skip: String
)

object AdhkarTourLocalization {
    private val morning = mapOf(
        "en" to AdhkarTourStrings(
            step = "Step {curr} of {total}",
            reciteTitle = "☀️ Morning Adhkar Recitation",
            reciteDesc = "Tap on any Adhkar card to open the interactive counter and recite your morning remembrance with Arabic, translation, and authentic benefits.",
            progressTitle = "📈 Weekly Progress & Streak",
            progressDesc = "Here you can track your weekly Morning Adhkar progress, completion consistency chart, and daily spiritual streak.",
            next = "Next ➔",
            done = "Got it! ✓",
            skip = "Skip"
        ),
        "hi" to AdhkarTourStrings(
            step = "कदम {curr} / {total}",
            reciteTitle = "☀️ सुबह के अज़कार पढ़ें",
            reciteDesc = "किसी भी अज़कार कार्ड पर टैप करके आप काउंटर खोल सकते हैं और अरबी, तर्जुमा व फ़ज़ीलत के साथ अपना सुबह का ज़िक्र पढ़ सकते हैं।",
            progressTitle = "📈 साप्ताहिक प्रोग्रेस और स्ट्रीक",
            progressDesc = "यहाँ आप अपने पूरे हफ़्ते के सुबह के अज़कार की प्रोग्रेस, निरंतरता (Consistency) चार्ट और दैनिक स्ट्रीक ट्रैक कर सकते हैं।",
            next = "आगे बढ़ें ➔",
            done = "समझ गया ✓",
            skip = "छोड़ें"
        ),
        "ur" to AdhkarTourStrings(
            step = "مرحلہ {curr} از {total}",
            reciteTitle = "☀️ صبح کے اذکار پڑھیں",
            reciteDesc = "کسی بھی اذکار کارڈ پر ٹیپ کر کے آپ کاؤنٹر کھول سکتے ہیں اور عربی، ترجمہ اور فضیلت کے ساتھ اپنے صبح کے اذکار پڑھ سکتے ہیں۔",
            progressTitle = "📈 ہفتہ وار پروگریس اور سٹریک",
            progressDesc = "یہاں آپ اپنے پورے ہفتے کے صبح کے اذکار کی پروگریس، مستقل مزاجی چارٹ اور روزانہ کی سٹریک ٹریک کر سکتے ہیں۔",
            next = "اگلا ➔",
            done = "سمجھ گیا ✓",
            skip = "چھوڑیں"
        ),
        "ar" to AdhkarTourStrings(
            step = "الخطوة {curr} من {total}",
            reciteTitle = "☀️ قراءة أذكار الصباح",
            reciteDesc = "اضغط على أي بطاقة ذكر لفتح العداد التفاعلي وقراءة أذكار الصباح بالنص العربي والترجمة والفضائل.",
            progressTitle = "📈 التقدم الأسبوعي والمداومة",
            progressDesc = "هنا يمكنك متابعة إنجازك الأسبوعي لأذكار الصباح ومخطط المداومة وسلسلة الأيام المتتالية.",
            next = "التالي ➔",
            done = "فهمت ✓",
            skip = "تخطي"
        ),
        "bn" to AdhkarTourStrings(
            step = "ধাপ {curr} / {total}",
            reciteTitle = "☀️ সকালের আজকার পাঠ",
            reciteDesc = "যেকোনো আজকার কার্ডে ট্যাপ করে আপনি কাউন্টার খুলতে পারেন এবং আরবি, অর্থ ও ফজিলতসহ সকালের জিকির পাঠ করতে পারেন।",
            progressTitle = "📈 সাপ্তাহিক অগ্রগতি ও ধারাবাহিকতা",
            progressDesc = "এখানে আপনি পুরো সপ্তাহের সকালের আজকারের অগ্রগতি, ধারাবাহিকতা চার্ট এবং দৈনিক স্ট্রিক ট্র্যাক করতে পারেন।",
            next = "পরবর্তী ➔",
            done = "বুঝেছি ✓",
            skip = "এড়িয়ে যান"
        ),
        "gu" to AdhkarTourStrings(
            step = "પગલું {curr} / {total}",
            reciteTitle = "☀️ સવારના અઝકાર પઢો",
            reciteDesc = "કોઈપણ અઝકાર કાર્ડ પર ટેપ કરીને તમે કાઉન્ટર ખોલી શકો છો અને અરબી, અનુવાદ અને ફઝીલત સાથે સવારનું ઝિક્ર પઢી શકો છો.",
            progressTitle = "📈 સાપ્તાહિક પ્રગતિ અને સ્ટ્રીક",
            progressDesc = "અહીં તમે આખા અઠવાડિયાના સવારના અઝકારની પ્રગતિ, સાતત્ય ચાર્ટ અને દૈનિક સ્ટ્રીક ટ્રેક કરી શકો છો.",
            next = "આગળ ➔",
            done = "સમજાઈ ગયું ✓",
            skip = "છોડો"
        ),
        "ta" to AdhkarTourStrings(
            step = "படி {curr} / {total}",
            reciteTitle = "☀️ காலை அத்கார் ஓதுதல்",
            reciteDesc = "எந்தவொரு அத்கார் கார்டையும் தட்டுவதன் மூலம் கவுண்ட்டரைத் திறந்து, அரபி, மொழிபெயர்ப்பு மற்றும் சிறப்புகளுடன் காலை திக்ரை ஓதலாம்.",
            progressTitle = "📈 வாராந்திர முன்னேற்றம் & தொடர்ச்சி",
            progressDesc = "இங்கே உங்கள் முழு வாரத்தின் காலை அத்கார் முன்னேற்றம், தொடர்ச்சி வரைபடம் மற்றும் தினசரி ஸ்ட்ரீக்கைக் கண்காணிக்கலாம்.",
            next = "அடுத்து ➔",
            done = "புரிந்தது ✓",
            skip = "தவிர்"
        ),
        "te" to AdhkarTourStrings(
            step = "దశ {curr} / {total}",
            reciteTitle = "☀️ ఉదయపు అజ్కార్ పఠనం",
            reciteDesc = "ఏదైనా అజ్కార్ కార్డుపై ట్యాప్ చేసి కౌంటర్‌ను తెరిచి అరబిక్, అనువాదం మరియు విశిష్టతలతో మీ ఉదయపు స్మరణను పఠించవచ్చు.",
            progressTitle = "📈 వారపు పురోగతి & స్ట్రీక్",
            progressDesc = "ఇక్కడ మీరు మొత్తం వారం ఉదయపు అజ్కార్ పురోగతి, స్థిరత్వ చార్ట్ మరియు రోజువారీ స్ట్రీక్‌ను ట్రాక్ చేయవచ్చు.",
            next = "తరువాత ➔",
            done = "అర్థమైంది ✓",
            skip = "దాటవేయి"
        ),
        "ml" to AdhkarTourStrings(
            step = "ഘട്ടം {curr} / {total}",
            reciteTitle = "☀️ പ്രഭാത ദിക്റുകൾ പാരായണം",
            reciteDesc = "ഏതെങ്കിലും അദ്കാർ കാർഡിൽ ടാപ്പ് ചെയ്ത് കൗണ്ടർ തുറന്ന് അറബിക്, പരിഭാഷ, മഹത്വം എന്നിവയോടൊപ്പം പ്രഭാത ദിക്റുകൾ ചൊല്ലാം.",
            progressTitle = "📈 പ്രതിവാര പുരോഗതിയും സ്ട്രീക്കും",
            progressDesc = "ഇവിടെ നിങ്ങൾക്ക് ആഴ്ചയിലെ പ്രഭാത ദിക്റുകളുടെ പുരോഗതിയും സ്ഥിരതാ ചാർട്ടും ദിവസേനയുള്ള സ്ട്രീക്കും ട്രാക്ക് ചെയ്യാം.",
            next = "അടുത്തത് ➔",
            done = "മനസ്സിലായി ✓",
            skip = "ഒഴിവാക്കുക"
        ),
        "mr" to AdhkarTourStrings(
            step = "टप्पा {curr} / {total}",
            reciteTitle = "☀️ सकाळचे अझकार पठण",
            reciteDesc = "कोणत्याही अझकार कार्डवर टॅप करून तुम्ही काउंटर उघडू शकता आणि अरबी, भाषांतर व महत्त्वांसह सकाळचे स्मरण करू शकता.",
            progressTitle = "📈 साप्ताहिक प्रगती आणि स्ट्रीक",
            progressDesc = "येथे तुम्ही संपूर्ण आठवड्यातील सकाळच्या अझकारची प्रगती, सातत्य आलेख आणि दैनंदिन स्ट्रीक ट्रॅक करू शकता.",
            next = "पुढे ➔",
            done = "समजले ✓",
            skip = "वगळा"
        ),
        "pa" to AdhkarTourStrings(
            step = "ਕਦਮ {curr} / {total}",
            reciteTitle = "☀️ ਸਵੇਰ ਦੇ ਅਜ਼ਕਾਰ ਪੜ੍ਹੋ",
            reciteDesc = "ਕਿਸੇ ਵੀ ਅਜ਼ਕਾਰ ਕਾਰਡ ਤੇ ਟੈਪ ਕਰਕੇ ਕਾਊਂਟਰ ਖੋਲ੍ਹੋ ਅਤੇ ਅਰਬੀ, ਤਰਜਮੇ ਅਤੇ ਫ਼ਜ਼ੀਲਤਾਂ ਨਾਲ ਸਵੇਰ ਦਾ ਜ਼ਿਕਰ ਕਰੋ।",
            progressTitle = "📈 ਹਫ਼ਤਾਵਾਰੀ ਪ੍ਰਗਤੀ ਅਤੇ ਸਟ੍ਰੀਕ",
            progressDesc = "ਇੱਥੇ ਤੁਸੀਂ ਪੂਰੇ ਹਫ਼ਤੇ ਦੇ ਸਵੇਰ ਦੇ ਅਜ਼ਕਾਰ ਦੀ ਪ੍ਰਗਤੀ, ਨਿਰੰਤਰਤਾ ਚਾਰਟ ਅਤੇ ਰੋਜ਼ਾਨਾ ਸਟ੍ਰੀਕ ਟਰੈਕ ਕਰ ਸਕਦੇ ਹੋ।",
            next = "ਅੱਗੇ ➔",
            done = "ਸਮਝ ਗਿਆ ✓",
            skip = "ਛੱਡੋ"
        ),
        "kn" to AdhkarTourStrings(
            step = "ಹಂತ {curr} / {total}",
            reciteTitle = "☀️ ಮುಂಜಾನೆಯ ಅಜ್ಕಾರ್ ಪಠಣ",
            reciteDesc = "ಯಾವುದೇ ಅಜ್ಕಾರ್ ಕಾರ್ಡ್ ಮೇಲೆ ಟ್ಯಾಪ್ ಮಾಡಿ ಕೌಂಟರ್ ತೆರೆಯಿರಿ ಮತ್ತು ಅರೇಬಿಕ್, ಅನುವಾದ ಹಾಗೂ ಮಹತ್ವದೊಂದಿಗೆ ಮುಂಜಾನೆಯ ಜಿಕ್ರ್ ಪಠಿಸಿ.",
            progressTitle = "📈 ಸಾಪ್ತಾಹಿಕ ಪ್ರಗತಿ ಮತ್ತು ಸ್ಟ್ರೀಕ್",
            progressDesc = "ಇಲ್ಲಿ ನೀವು ಇಡೀ ವಾರದ ಮುಂಜಾನೆಯ ಅಜ್ಕಾರ್ ಪ್ರಗತಿ, ನಿರಂತರತೆಯ ಚಾರ್ಟ್ ಮತ್ತು ದೈನಂದಿನ ಸ್ಟ್ರೀಕ್ ಟ್ರ್ಯಾಕ್ ಮಾಡಬಹುದು.",
            next = "ಮುಂದೆ ➔",
            done = "ಅರ್ಥವಾಯಿತು ✓",
            skip = "ಬಿಟ್ಟುಬಿಡಿ"
        ),
        "or" to AdhkarTourStrings(
            step = "ପଦକ୍ଷେପ {curr} / {total}",
            reciteTitle = "☀️ ସକାଳର ଅଜକାର ପାଠ",
            reciteDesc = "ଯେକୌଣସି ଅଜକାର କାର୍ଡ ଉପରେ ଟ୍ୟାପ୍ କରି କାଉଣ୍ଟର ଖୋଲନ୍ତୁ ଏବଂ ଆରବୀ, ଅନୁବାଦ ଓ ଫଜିଲତ ସହିତ ସକାଳର ଜିକ୍ର ପାଠ କରନ୍ତୁ।",
            progressTitle = "📈 ସାପ୍ତାହିକ ପ୍ରଗତି ଓ ଷ୍ଟ୍ରିକ",
            progressDesc = "ଏଠାରେ ଆପଣ ସାରା ସପ୍ତାହର ସକାଳ ଅଜକାର ପ୍ରଗତି, ନିରନ୍ତରତା ଚାର୍ଟ ଏବଂ ଦୈନିକ ଷ୍ଟ୍ରିକ ଟ୍ରାକ୍ କରିପାରିବେ।",
            next = "ପରବର୍ତ୍ତୀ ➔",
            done = "ବୁଝିଗଲି ✓",
            skip = "ଛାଡ଼ନ୍ତୁ"
        ),
        "as" to AdhkarTourStrings(
            step = "স্তৰ {curr} / {total}",
            reciteTitle = "☀️ ৰাতিপুৱাৰ আজকাৰ পাঠ",
            reciteDesc = "যিকোনো আজকাৰ কাৰ্ডত টেপ কৰি কাউন্টাৰ খোলক আৰু আৰবী, অৰ্থ আৰু ফজিলতৰ সৈতে ৰাতিপুৱাৰ জিকিৰ পাঠ কৰক।",
            progressTitle = "📈 সাপ্তাহিক অগ্ৰগতি আৰু ষ্ট্ৰীক",
            progressDesc = "ইয়াত আপুনি সমগ্ৰ সপ্তাহৰ ৰাতিপুৱাৰ আজকাৰৰ অগ্ৰগতি, ধাৰাবাহিকতা চাৰ্ট আৰু দৈনিক ষ্ট্ৰীক নিৰীক্ষণ কৰিব পাৰে।",
            next = "পৰৱৰ্তী ➔",
            done = "বুজি পালোঁ ✓",
            skip = "এৰক"
        ),
        "ms" to AdhkarTourStrings(
            step = "Langkah {curr} / {total}",
            reciteTitle = "☀️ Bacaan Zikir Pagi",
            reciteDesc = "Ketik mana-mana kad zikir untuk membuka kaunter dan membaca zikir pagi beserta teks Arab, terjemahan, dan kelebihannya.",
            progressTitle = "📈 Kemajuan Mingguan & Istiqamah",
            progressDesc = "Di sini anda boleh menjejaki kemajuan zikir pagi mingguan, carta istiqamah, dan rekod streak harian anda.",
            next = "Seterusnya ➔",
            done = "Faham ✓",
            skip = "Langkau"
        ),
        "id" to AdhkarTourStrings(
            step = "Langkah {curr} dari {total}",
            reciteTitle = "☀️ Bacaan Zikir Pagi",
            reciteDesc = "Ketuk kartu zikir mana saja untuk membuka penghitung interaktif dan membaca zikir pagi lengkap dengan teks Arab, terjemahan, dan keutamaannya.",
            progressTitle = "📈 Kemajuan Mingguan & Streak",
            progressDesc = "Di sini Anda dapat memantau progres zikir pagi mingguan, grafik konsistensi, dan capaian streak harian.",
            next = "Lanjut ➔",
            done = "Paham ✓",
            skip = "Lewati"
        ),
        "tr" to AdhkarTourStrings(
            step = "Adım {curr} / {total}",
            reciteTitle = "☀️ Sabah Zikirleri Okuma",
            reciteDesc = "Sayaç ekranını açmak ve Arapça metin, anlam ve faziletleriyle sabah zikirlerini okumak için herhangi bir zikir kartına dokunun.",
            progressTitle = "📈 Haftalık İlerleme & Seri",
            progressDesc = "Buradan haftalık sabah zikirleri tamamlama durumunuzu, devamlılık grafiğinizi ve günlük serinizi takip edebilirsiniz.",
            next = "İleri ➔",
            done = "Anladım ✓",
            skip = "Geç"
        ),
        "fa" to AdhkarTourStrings(
            step = "مرحله {curr} از {total}",
            reciteTitle = "☀️ تلاوت اذکار صبحگاه",
            reciteDesc = "با ضربه زدن روی هر کارت ذکر، شمارنده را باز کنید و اذکار صبحگاهی خود را به همراه متن عربی، ترجمه و فضایل بخوانید.",
            progressTitle = "📈 پیشرفت هفتگی و تداوم",
            progressDesc = "در اینجا می‌توانید پیشرفت هفتگی اذکار صبح، نمودار تداوم و استمرار روزانه خود را مشاهده و پیگیری کنید.",
            next = "بعدی ➔",
            done = "متوجه شدم ✓",
            skip = "رد کردن"
        ),
        "ha" to AdhkarTourStrings(
            step = "Mataki {curr} na {total}",
            reciteTitle = "☀️ Karanta Azkarin Safe",
            reciteDesc = "Taba kowace katin azkar domin bude ma’aunin kirga da karanta azkarin safe tare da fassarar Larabci da falalolinsu.",
            progressTitle = "📈 Ci Gaban Mako & Dage",
            progressDesc = "A nan zaka iya bibiyar ci gaban azkarin safenka na mako, jadawalin dauriya, da kuma yawan kwanakin da ka kiyaye.",
            next = "Gaba ➔",
            done = "Na gane ✓",
            skip = "Wuce"
        ),
        "sw" to AdhkarTourStrings(
            step = "Hatua {curr} ya {total}",
            reciteTitle = "☀️ Kusoma Adhkar za Asubuhi",
            reciteDesc = "Gonga kadi yoyote ya adhkar ili kufungua kaunta na kusoma dhikr za asubuhi pamoja na Kiarabu, maana, na fadhila zake.",
            progressTitle = "📈 Maendeleo ya Wiki & Mwendelezo",
            progressDesc = "Hapa unaweza kufuatilia maendeleo ya adhkar za asubuhi za wiki, chati ya uthabiti, na mwendelezo wa kila siku.",
            next = "Ifuatayo ➔",
            done = "Nimeelewa ✓",
            skip = "Ruka"
        ),
        "so" to AdhkarTourStrings(
            step = "Tallaabada {curr} ee {total}",
            reciteTitle = "☀️ Akhrinta Adkaarta Subaxda",
            reciteDesc = "Taabo kaar kasta oo adkaar ah si aad u furto tiriyaha oo aad u akhrido adkaarta subaxda oo wadata Carabiga, macnaha, iyo fadliga.",
            progressTitle = "📈 Horumarka Toddobaadka & Joogteynta",
            progressDesc = "Halkan waxaad kula socon kartaa horumarka adkaarta subaxda ee toddobaadka, shaxda joogteynta, iyo xiriirka maalinlaha ah.",
            next = "Ku xiga ➔",
            done = "Waan fahmay ✓",
            skip = "Ka gudub"
        ),
        "fr" to AdhkarTourStrings(
            step = "Étape {curr} sur {total}",
            reciteTitle = "☀️ Récitation des Invocations du Matin",
            reciteDesc = "Appuyez sur une carte pour ouvrir le compteur interactif et réciter vos invocations du matin avec l’arabe, la traduction et les mérites.",
            progressTitle = "📈 Progression Hebdomadaire & Série",
            progressDesc = "Suivez ici votre assiduité hebdomadaire pour les invocations du matin, votre graphique de constance et votre série quotidienne.",
            next = "Suivant ➔",
            done = "Compris ✓",
            skip = "Passer"
        ),
        "de" to AdhkarTourStrings(
            step = "Schritt {curr} von {total}",
            reciteTitle = "☀️ Morgen-Adhkar Rezitation",
            reciteDesc = "Tippen Sie auf eine Adhkar-Karte, um den interaktiven Zähler zu öffnen und Ihre Morgen-Bittgebete mit arabischem Text, Übersetzung und Vorzügen zu rezitieren.",
            progressTitle = "📈 Wöchentlicher Fortschritt & Serie",
            progressDesc = "Hier können Sie Ihren wöchentlichen Fortschritt bei den Morgen-Adhkar, Ihr Beständigkeitsdiagramm und Ihre tägliche Serie verfolgen.",
            next = "Weiter ➔",
            done = "Verstanden ✓",
            skip = "Überspringen"
        ),
        "ru" to AdhkarTourStrings(
            step = "Шаг {curr} из {total}",
            reciteTitle = "☀️ Чтение утренних азкаров",
            reciteDesc = "Нажмите на любую карточку азкара, чтобы открыть интерактивный счетчик и читать утренние поминания с арабским текстом, переводом и пользой.",
            progressTitle = "📈 Еженедельный прогресс и серия",
            progressDesc = "Здесь вы можете отслеживать свой еженедельный прогресс утренних азкаров, график постоянства и серию дней.",
            next = "Далее ➔",
            done = "Понятно ✓",
            skip = "Пропустить"
        ),
        "zh" to AdhkarTourStrings(
            step = "第 {curr} 步 / 共 {total} 步",
            reciteTitle = "☀️ 诵念早晨赞念 (Adhkar)",
            reciteDesc = "点击任意赞念卡片即可打开计数器，查看阿拉伯原文、译文及尊贵功德并开始诵念。",
            progressTitle = "📈 每周进度与坚持记录",
            progressDesc = "在此处您可以追踪整周早晨赞念的完成进度、坚持度统计图表以及每日连续打卡记录。",
            next = "下一步 ➔",
            done = "知道了 ✓",
            skip = "跳过"
        ),
    )

    private val evening = mapOf(
        "en" to AdhkarTourStrings(
            step = "Step {curr} of {total}",
            reciteTitle = "🌙 Evening Adhkar Recitation",
            reciteDesc = "Tap on any Adhkar card to open the interactive counter and recite your evening remembrance with Arabic, translation, and authentic benefits.",
            progressTitle = "📈 Weekly Progress & Streak",
            progressDesc = "Here you can track your weekly Evening Adhkar progress, completion consistency chart, and daily spiritual streak.",
            next = "Next ➔",
            done = "Got it! ✓",
            skip = "Skip"
        ),
        "hi" to AdhkarTourStrings(
            step = "कदम {curr} / {total}",
            reciteTitle = "🌙 शाम के अज़कार पढ़ें",
            reciteDesc = "किसी भी अज़कार कार्ड पर टैप करके आप काउंटर खोल सकते हैं और अरबी, तर्जुमा व फ़ज़ीलत के साथ अपना शाम का ज़िक्र पढ़ सकते हैं।",
            progressTitle = "📈 साप्ताहिक प्रोग्रेस और स्ट्रीक",
            progressDesc = "यहाँ आप अपने पूरे हफ़्ते के शाम के अज़कार की प्रोग्रेस, निरंतरता (Consistency) चार्ट और दैनिक स्ट्रीक ट्रैक कर सकते हैं।",
            next = "आगे बढ़ें ➔",
            done = "समझ गया ✓",
            skip = "छोड़ें"
        ),
        "ur" to AdhkarTourStrings(
            step = "مرحلہ {curr} از {total}",
            reciteTitle = "🌙 شام کے اذکار پڑھیں",
            reciteDesc = "کسی بھی اذکار کارڈ پر ٹیپ کر کے آپ کاؤنٹر کھول سکتے ہیں اور عربی، ترجمہ اور فضیلت کے ساتھ اپنے شام کے اذکار پڑھ سکتے ہیں۔",
            progressTitle = "📈 ہفتہ وار پروگریس اور سٹریک",
            progressDesc = "یہاں آپ اپنے پورے ہفتے کے شام کے اذکار کی پروگریس، مستقل مزاجی چارٹ اور روزانہ کی سٹریک ٹریک کر سکتے ہیں۔",
            next = "اگلا ➔",
            done = "سمجھ گیا ✓",
            skip = "چھوڑیں"
        ),
        "ar" to AdhkarTourStrings(
            step = "الخطوة {curr} من {total}",
            reciteTitle = "🌙 قراءة أذكار المساء",
            reciteDesc = "اضغط على أي بطاقة ذكر لفتح العداد التفاعلي وقراءة أذكار المساء بالنص العربي والترجمة والفضائل.",
            progressTitle = "📈 التقدم الأسبوعي والمداومة",
            progressDesc = "هنا يمكنك متابعة إنجازك الأسبوعي لأذكار المساء ومخطط المداومة وسلسلة الأيام المتتالية.",
            next = "التالي ➔",
            done = "فهمت ✓",
            skip = "تخطي"
        ),
        "bn" to AdhkarTourStrings(
            step = "ধাপ {curr} / {total}",
            reciteTitle = "🌙 সন্ধ্যার আজকার পাঠ",
            reciteDesc = "যেকোনো আজকার কার্ডে ট্যাপ করে আপনি কাউন্টার খুলতে পারেন এবং আরবি, অর্থ ও ফজিলতসহ সন্ধ্যার জিকির পাঠ করতে পারেন।",
            progressTitle = "📈 সাপ্তাহিক অগ্রগতি ও ধারাবাহিকতা",
            progressDesc = "এখানে আপনি পুরো সপ্তাহের সন্ধ্যার আজকারের অগ্রগতি, ধারাবাহিকতা চার্ট এবং দৈনিক স্ট্রিক ট্র্যাক করতে পারেন।",
            next = "পরবর্তী ➔",
            done = "বুঝেছি ✓",
            skip = "এড়িয়ে যান"
        ),
        "gu" to AdhkarTourStrings(
            step = "પગલું {curr} / {total}",
            reciteTitle = "🌙 સાંજના અઝકાર પઢો",
            reciteDesc = "કોઈપણ અઝકાર કાર્ડ પર ટેપ કરીને તમે કાઉન્ટર ખોલી શકો છો અને અરબી, અનુવાદ અને ફઝીલત સાથે સાંજનું ઝિક્ર પઢી શકો છો.",
            progressTitle = "📈 સાપ્તાહિક પ્રગતિ અને સ્ટ્રીક",
            progressDesc = "અહીં તમે આખા અઠવાડિયાના સાંજના અઝકારની પ્રગતિ, સાતત્ય ચાર્ટ અને દૈનિક સ્ટ્રીક ટ્રેક કરી શકો છો.",
            next = "આગળ ➔",
            done = "સમજાઈ ગયું ✓",
            skip = "છોડો"
        ),
        "ta" to AdhkarTourStrings(
            step = "படி {curr} / {total}",
            reciteTitle = "🌙 மாலை அத்கார் ஓதுதல்",
            reciteDesc = "எந்தவொரு அத்கார் கார்டையும் தட்டுவதன் மூலம் கவுண்ட்டரைத் திறந்து, அரபி, மொழிபெயர்ப்பு மற்றும் சிறப்புகளுடன் மாலை திக்ரை ஓதலாம்.",
            progressTitle = "📈 வாராந்திர முன்னேற்றம் & தொடர்ச்சி",
            progressDesc = "இங்கே உங்கள் முழு வாரத்தின் மாலை அத்கார் முன்னேற்றம், தொடர்ச்சி வரைபடம் மற்றும் தினசரி ஸ்ட்ரீக்கைக் கண்காணிக்கலாம்.",
            next = "அடுத்து ➔",
            done = "புரிந்தது ✓",
            skip = "தவிர்"
        ),
        "te" to AdhkarTourStrings(
            step = "దశ {curr} / {total}",
            reciteTitle = "🌙 సాయంత్రపు అజ్కార్ పఠనం",
            reciteDesc = "ఏదైనా అజ్కార్ కార్డుపై ట్యాప్ చేసి కౌంటర్‌ను తెరిచి అరబిక్, అనువాదం మరియు విశిష్టతలతో మీ సాయంత్రపు స్మరణను పఠించవచ్చు.",
            progressTitle = "📈 వారపు పురోగతి & స్ట్రీక్",
            progressDesc = "ఇక్కడ మీరు మొత్తం వారం సాయంత్రపు అజ్కార్ పురోగతి, స్థిరత్వ చార్ట్ మరియు రోజువారీ స్ట్రీక్‌ను ట్రాక్ చేయవచ్చు.",
            next = "తరువాత ➔",
            done = "అర్థమైంది ✓",
            skip = "దాటవేయి"
        ),
        "ml" to AdhkarTourStrings(
            step = "ഘട്ടം {curr} / {total}",
            reciteTitle = "🌙 സായാഹ്ന ദിക്റുകൾ പാരായണം",
            reciteDesc = "ഏതെങ്കിലും അദ്കാർ കാർഡിൽ ടാപ്പ് ചെയ്ത് കൗണ്ടർ തുറന്ന് അറബിക്, പരിഭാഷ, മഹത്വം എന്നിവയോടൊപ്പം സായാഹ്ന ദിക്റുകൾ ചൊല്ലാം.",
            progressTitle = "📈 പ്രതിവാര പുരോഗതിയും സ്ട്രീക്കും",
            progressDesc = "ഇവിടെ നിങ്ങൾക്ക് ആഴ്ചയിലെ സായാഹ്ന ദിക്റുകളുടെ പുരോഗതിയും സ്ഥിരതാ ചാർട്ടും ദിവസേനയുള്ള സ്ട്രീക്കും ട്രാക്ക് ചെയ്യാം.",
            next = "അടുത്തത് ➔",
            done = "മനസ്സിലായി ✓",
            skip = "ഒഴിവാക്കുക"
        ),
        "mr" to AdhkarTourStrings(
            step = "टप्पा {curr} / {total}",
            reciteTitle = "🌙 संध्याकाळचे अझकार पठण",
            reciteDesc = "कोणत्याही अझकार कार्डवर टॅप करून तुम्ही काउंटर उघडू शकता आणि अरबी, भाषांतर व महत्त्वांसह संध्याकाळचे स्मरण करू शकता.",
            progressTitle = "📈 साप्ताहिक प्रगती आणि स्ट्रीक",
            progressDesc = "येथे तुम्ही संपूर्ण आठवड्यातील संध्याकाळच्या अझकारची प्रगती, सातत्य आलेख आणि दैनंदिन स्ट्रीक ट्रॅक करू शकता.",
            next = "पुढे ➔",
            done = "समजले ✓",
            skip = "वगळा"
        ),
        "pa" to AdhkarTourStrings(
            step = "ਕਦਮ {curr} / {total}",
            reciteTitle = "🌙 ਸ਼ਾਮ ਦੇ ਅਜ਼ਕਾਰ ਪੜ੍ਹੋ",
            reciteDesc = "ਕਿਸੇ ਵੀ ਅਜ਼ਕਾਰ ਕਾਰਡ ਤੇ ਟੈਪ ਕਰਕੇ ਕਾਊਂਟਰ ਖੋਲ੍ਹੋ ਅਤੇ ਅਰਬੀ, ਤਰਜਮੇ ਅਤੇ ਫ਼ਜ਼ੀਲਤਾਂ ਨਾਲ ਸ਼ਾਮ ਦਾ ਜ਼ਿਕਰ ਕਰੋ।",
            progressTitle = "📈 ਹਫ਼ਤਾਵਾਰੀ ਪ੍ਰਗਤੀ ਅਤੇ ਸਟ੍ਰੀਕ",
            progressDesc = "ਇੱਥੇ ਤੁਸੀਂ ਪੂਰੇ ਹਫ਼ਤੇ ਦੇ ਸ਼ਾਮ ਦੇ ਅਜ਼ਕਾਰ ਦੀ ਪ੍ਰਗਤੀ, ਨਿਰੰਤਰਤਾ ਚਾਰਟ ਅਤੇ ਰੋਜ਼ਾਨਾ ਸਟ੍ਰੀਕ ਟਰੈਕ ਕਰ ਸਕਦੇ ਹੋ।",
            next = "ਅੱਗੇ ➔",
            done = "ਸਮਝ ਗਿਆ ✓",
            skip = "ਛੱਡੋ"
        ),
        "kn" to AdhkarTourStrings(
            step = "ಹಂತ {curr} / {total}",
            reciteTitle = "🌙 ಸಂಜೆಯ ಅಜ್ಕಾರ್ ಪಠಣ",
            reciteDesc = "ಯಾವುದೇ ಅಜ್ಕಾರ್ ಕಾರ್ಡ್ ಮೇಲೆ ಟ್ಯಾಪ್ ಮಾಡಿ ಕೌಂಟರ್ ತೆರೆಯಿರಿ ಮತ್ತು ಅರೇಬಿಕ್, ಅನುವಾದ ಹಾಗೂ ಮಹತ್ವದೊಂದಿಗೆ ಸಂಜೆಯ ಜಿಕ್ರ್ ಪಠಿಸಿ.",
            progressTitle = "📈 ಸಾಪ್ತಾಹಿಕ ಪ್ರಗತಿ ಮತ್ತು ಸ್ಟ್ರೀಕ್",
            progressDesc = "ಇಲ್ಲಿ ನೀವು ಇಡೀ ವಾರದ ಸಂಜೆಯ ಅಜ್ಕಾರ್ ಪ್ರಗತಿ, ನಿರಂತರತೆಯ ಚಾರ್ಟ್ ಮತ್ತು ದೈನಂದಿನ ಸ್ಟ್ರೀಕ್ ಟ್ರ್ಯಾಕ್ ಮಾಡಬಹುದು.",
            next = "ಮುಂದೆ ➔",
            done = "ಅರ್ಥವಾಯಿತು ✓",
            skip = "ಬಿಟ್ಟುಬಿಡಿ"
        ),
        "or" to AdhkarTourStrings(
            step = "ପଦକ୍ଷେପ {curr} / {total}",
            reciteTitle = "🌙 ସନ୍ଧ୍ୟାର ଅଜକାର ପାଠ",
            reciteDesc = "ଯେକୌଣସି ଅଜକାର କାର୍ଡ ଉପରେ ଟ୍ୟାପ୍ କରି କାଉଣ୍ଟର ଖୋଲନ୍ତୁ ଏବଂ ଆରବୀ, ଅନୁବାଦ ଓ ଫଜିଲତ ସହିତ ସନ୍ଧ୍ୟାର ଜିକ୍ର ପାଠ କରନ୍ତୁ।",
            progressTitle = "📈 ସାପ୍ତାହିକ ପ୍ରଗତି ଓ ଷ୍ଟ୍ରିକ",
            progressDesc = "ଏଠାରେ ଆପଣ ସାରା ସପ୍ତାହର ସନ୍ଧ୍ୟା ଅଜକାର ପ୍ରଗତି, ନିରନ୍ତରତା ଚାର୍ଟ ଏବଂ ଦୈନିକ ଷ୍ଟ୍ରିକ ଟ୍ରାକ୍ କରିପାରିବେ।",
            next = "ପରବର୍ତ୍ତୀ ➔",
            done = "ବୁଝିଗଲି ✓",
            skip = "ଛାଡ଼ନ୍ତୁ"
        ),
        "as" to AdhkarTourStrings(
            step = "স্তৰ {curr} / {total}",
            reciteTitle = "🌙 গধূলিৰ আজকাৰ পাঠ",
            reciteDesc = "যিকোনো আজকাৰ কাৰ্ডত টেপ কৰি কাউন্টাৰ খোলক আৰু আৰবী, অৰ্থ আৰু ফজিলতৰ সৈতে গধূলিৰ জিকিৰ পাঠ কৰক।",
            progressTitle = "📈 সাপ্তাহিক অগ্ৰগতি আৰু ষ্ট্ৰীক",
            progressDesc = "ইয়াত আপুনি সমগ্ৰ সপ্তাহৰ গধূলিৰ আজকাৰৰ অগ্ৰগতি, ধাৰাবাহিকতা চাৰ্ট আৰু দৈনিক ষ্ট্ৰীক নিৰীক্ষণ কৰিব পাৰে।",
            next = "পৰৱৰ্তী ➔",
            done = "বুজি পালোঁ ✓",
            skip = "এৰক"
        ),
        "ms" to AdhkarTourStrings(
            step = "Langkah {curr} / {total}",
            reciteTitle = "🌙 Bacaan Zikir Petang",
            reciteDesc = "Ketik mana-mana kad zikir untuk membuka kaunter dan membaca zikir petang beserta teks Arab, terjemahan, dan kelebihannya.",
            progressTitle = "📈 Kemajuan Mingguan & Istiqamah",
            progressDesc = "Di sini anda boleh menjejaki kemajuan zikir petang mingguan, carta istiqamah, dan rekod streak harian anda.",
            next = "Seterusnya ➔",
            done = "Faham ✓",
            skip = "Langkau"
        ),
        "id" to AdhkarTourStrings(
            step = "Langkah {curr} dari {total}",
            reciteTitle = "🌙 Bacaan Zikir Petang",
            reciteDesc = "Ketuk kartu zikir mana saja untuk membuka penghitung interaktif dan membaca zikir petang lengkap dengan teks Arab, terjemahan, dan keutamaannya.",
            progressTitle = "📈 Kemajuan Mingguan & Streak",
            progressDesc = "Di sini Anda dapat memantau progres zikir petang mingguan, grafik konsistensi, dan capaian streak harian.",
            next = "Lanjut ➔",
            done = "Paham ✓",
            skip = "Lewati"
        ),
        "tr" to AdhkarTourStrings(
            step = "Adım {curr} / {total}",
            reciteTitle = "🌙 Akşam Zikirleri Okuma",
            reciteDesc = "Sayaç ekranını açmak ve Arapça metin, anlam ve faziletleriyle akşam zikirlerini okumak için herhangi bir zikir kartına dokunun.",
            progressTitle = "📈 Haftalık İlerleme & Seri",
            progressDesc = "Buradan haftalık akşam zikirleri tamamlama durumunuzu, devamlılık grafiğinizi ve günlük serinizi takip edebilirsiniz.",
            next = "İleri ➔",
            done = "Anladım ✓",
            skip = "Geç"
        ),
        "fa" to AdhkarTourStrings(
            step = "مرحله {curr} از {total}",
            reciteTitle = "🌙 تلاوت اذکار شامگاه",
            reciteDesc = "با ضربه زدن روی هر کارت ذکر، شمارنده را باز کنید و اذکار شامگاهی خود را به همراه متن عربی، ترجمه و فضایل بخوانید.",
            progressTitle = "📈 پیشرفت هفتگی و تداوم",
            progressDesc = "در اینجا می‌توانید پیشرفت هفتگی اذکار شامگاهی، نمودار تداوم و استمرار روزانه خود را مشاهده و پیگیری کنید.",
            next = "بعدی ➔",
            done = "متوجه شدم ✓",
            skip = "رد کردن"
        ),
        "ha" to AdhkarTourStrings(
            step = "Mataki {curr} na {total}",
            reciteTitle = "🌙 Karanta Azkarin Yamma",
            reciteDesc = "Taba kowace katin azkar domin bude ma’aunin kirga da karanta azkarin yamma tare da fassarar Larabci da falalolinsu.",
            progressTitle = "📈 Ci Gaban Mako & Dage",
            progressDesc = "A nan zaka iya bibiyar ci gaban azkarin yammanka na mako, jadawalin dauriya, da kuma yawan kwanakin da ka kiyaye.",
            next = "Gaba ➔",
            done = "Na gane ✓",
            skip = "Wuce"
        ),
        "sw" to AdhkarTourStrings(
            step = "Hatua {curr} ya {total}",
            reciteTitle = "🌙 Kusoma Adhkar za Jioni",
            reciteDesc = "Gonga kadi yoyote ya adhkar ili kufungua kaunta na kusoma dhikr za jioni pamoja na Kiarabu, maana, na fadhila zake.",
            progressTitle = "📈 Maendeleo ya Wiki & Mwendelezo",
            progressDesc = "Hapa unaweza kufuatilia maendeleo ya adhkar za jioni za wiki, chati ya uthabiti, na mwendelezo wa kila siku.",
            next = "Ifuatayo ➔",
            done = "Nimeelewa ✓",
            skip = "Ruka"
        ),
        "so" to AdhkarTourStrings(
            step = "Tallaabada {curr} ee {total}",
            reciteTitle = "🌙 Akhrinta Adkaarta Galabta",
            reciteDesc = "Taabo kaar kasta oo adkaar ah si aad u furto tiriyaha oo aad u akhrido adkaarta galabta oo wadata Carabiga, macnaha, iyo fadliga.",
            progressTitle = "📈 Horumarka Toddobaadka & Joogteynta",
            progressDesc = "Halkan waxaad kula socon kartaa horumarka adkaarta galabta ee toddobaadka, shaxda joogteynta, iyo xiriirka maalinlaha ah.",
            next = "Ku xiga ➔",
            done = "Waan fahmay ✓",
            skip = "Ka gudub"
        ),
        "fr" to AdhkarTourStrings(
            step = "Étape {curr} sur {total}",
            reciteTitle = "🌙 Récitation des Invocations du Soir",
            reciteDesc = "Appuyez sur une carte pour ouvrir le compteur interactif et réciter vos invocations du soir avec l’arabe, la traduction et les mérites.",
            progressTitle = "📈 Progression Hebdomadaire & Série",
            progressDesc = "Suivez ici votre assiduité hebdomadaire pour les invocations du soir, votre graphique de constance et votre série quotidienne.",
            next = "Suivant ➔",
            done = "Compris ✓",
            skip = "Passer"
        ),
        "de" to AdhkarTourStrings(
            step = "Schritt {curr} von {total}",
            reciteTitle = "🌙 Abend-Adhkar Rezitation",
            reciteDesc = "Tippen Sie auf eine Adhkar-Karte, um den interaktiven Zähler zu öffnen und Ihre Abend-Bittgebete mit arabischem Text, Übersetzung und Vorzügen zu rezitieren.",
            progressTitle = "📈 Wöchentlicher Fortschritt & Serie",
            progressDesc = "Hier können Sie Ihren wöchentlichen Fortschritt bei den Abend-Adhkar, Ihr Beständigkeitsdiagramm und Ihre tägliche Serie verfolgen.",
            next = "Weiter ➔",
            done = "Verstanden ✓",
            skip = "Überspringen"
        ),
        "ru" to AdhkarTourStrings(
            step = "Шаг {curr} из {total}",
            reciteTitle = "🌙 Чтение вечерних азкаров",
            reciteDesc = "Нажмите на любую карточку азкара, чтобы открыть интерактивный счетчик и читать вечерние поминания с арабским текстом, переводом и пользой.",
            progressTitle = "📈 Еженедельный прогресс и серия",
            progressDesc = "Здесь вы можете отслеживать свой еженедельный прогресс вечерних азкаров, график постоянства и серию дней.",
            next = "Далее ➔",
            done = "Понятно ✓",
            skip = "Пропустить"
        ),
        "zh" to AdhkarTourStrings(
            step = "第 {curr} 步 / 共 {total} 步",
            reciteTitle = "🌙 诵念傍晚赞念 (Evening Adhkar)",
            reciteDesc = "点击任意赞念卡片即可打开计数器，查看阿拉伯原文、译文及尊贵功德并开始诵念傍晚赞念。",
            progressTitle = "📈 每周进度与坚持记录",
            progressDesc = "在此处您可以追踪整周傍晚赞念的完成进度、坚持度统计图表以及每日连续打卡记录。",
            next = "下一步 ➔",
            done = "知道了 ✓",
            skip = "跳过"
        ),
    )

    fun getMorning(lang: String): AdhkarTourStrings {
        val code = lang.lowercase().split("-")[0]
        return morning[code] ?: morning["en"]!!
    }

    fun getEvening(lang: String): AdhkarTourStrings {
        val code = lang.lowercase().split("-")[0]
        return evening[code] ?: evening["en"]!!
    }
}
