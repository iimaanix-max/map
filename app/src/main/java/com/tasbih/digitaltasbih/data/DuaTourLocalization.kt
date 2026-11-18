package com.tasbih.digitaltasbih.data

data class DuaTourStrings(
    val step: String,
    val quickTitle: String,
    val quickDesc: String,
    val feelTitle: String,
    val feelDesc: String,
    val catsTitle: String,
    val catsDesc: String,
    val myduaTitle: String,
    val myduaDesc: String,
    val mylistsTitle: String,
    val mylistsDesc: String,
    val saveTitle: String,
    val saveDesc: String,
    val next: String,
    val done: String,
    val skip: String
)

object DuaTourLocalization {
    private val en = DuaTourStrings(
        step = "Step {curr} of {total}",
        quickTitle = "⚡ Quick Access",
        quickDesc = "Quickly access daily essential Duas for morning, evening, after prayers, sleeping, and travel with a single tap.",
        feelTitle = "🤲 How are you feeling?",
        feelDesc = "Find heartfelt Duas tailored for your current emotions and life situations like anxiety, sadness, gratitude, or seeking patience.",
        catsTitle = "📚 Browse Categories",
        catsDesc = "Explore comprehensive Islamic Dua categories including Quranic Duas, Prophets prayers, healing, forgiveness, and daily life.",
        myduaTitle = "📝 My Dua",
        myduaDesc = "Write, save, and keep your own personal heartfelt Duas in your own words in this private space.",
        mylistsTitle = "📂 My Lists",
        mylistsDesc = "Create custom Dua collections and organize your favorite bookmarked Duas into personalized lists.",
        saveTitle = "🔖 Save to My Lists",
        saveDesc = "Tap here to bookmark and save this Dua to your custom collections for quick access anytime.",
        next = "Next ➔",
        done = "Got it! ✓",
        skip = "Skip"
    )

    private val hi = DuaTourStrings(
        step = "कदम {curr} / {total}",
        quickTitle = "⚡ त्वरित पहुंच (Quick Access)",
        quickDesc = "यहाँ से आप दैनिक जीवन की ज़रूरी दुआएं (जैसे सुबह/शाम, नमाज़ के बाद, सोने और सफ़र की दुआएं) तुरंत एक क्लिक में पढ़ सकते हैं।",
        feelTitle = "🤲 आप कैसा महसूस कर रहे हैं?",
        feelDesc = "अपने मौजूदा जज़्बात और हालात (जैसे परेशानी, ग़म, शुक्रगुज़ारी या सब्र) के हिसाब से ख़ास मसनून दुआएं तलाशें।",
        catsTitle = "📚 दुआओं की श्रेणियाँ (Categories)",
        catsDesc = "क़ुरआनी दुआएं, पैग़म्बरों की दुआएं, शिफ़ा, हिफ़ाज़त और रोज़मर्रा की ज़िंदगी की सभी दुआएं अलग-अलग श्रेणियों में पढ़ें।",
        myduaTitle = "📝 मेरी दुआ (My Dua)",
        myduaDesc = "यहाँ आप अपने ख़ुद के अल्फ़ाज़ में अपनी दिली और निजी दुआएं लिखकर हमेशा के लिए सहेज (Save) कर रख सकते हैं।",
        mylistsTitle = "📂 मेरी लिस्ट (My Lists)",
        mylistsDesc = "अपनी पसंदीदा और ज़रूरी दुआओं के अलग-अलग कस्टम फोल्डर और लिस्ट बनाएं ताकि उन्हें आसानी से पढ़ सकें।",
        saveTitle = "🔖 लिस्ट में सेव करें",
        saveDesc = "इस बटन पर क्लिक करके आप इस दुआ को अपनी पसंदीदा लिस्ट (My Lists) में सेव कर सकते हैं ताकि कभी भी आसानी से पढ़ सकें।",
        next = "आगे बढ़ें ➔",
        done = "समझ गया ✓",
        skip = "छोड़ें"
    )

    private val ur = DuaTourStrings(
        step = "مرحلہ {curr} از {total}",
        quickTitle = "⚡ فوری رسائی (Quick Access)",
        quickDesc = "یہاں سے آپ روزمرہ زندگی کی اہم دعائیں (جیسے صبح و شام، نماز کے بعد، سونے اور سفر کی دعائیں) ایک کلک میں پڑھ سکتے ہیں۔",
        feelTitle = "🤲 آپ کیسا محسوس کر رہے ہیں؟",
        feelDesc = "اپنے موجودہ جذبات اور حالات (جیسے پریشانی، غم، شکر گزاری یا صبر) کے مطابق مسنون دعائیں تلاش کریں۔",
        catsTitle = "📚 دعاؤں کی اقسام (Categories)",
        catsDesc = "قرآنی دعائیں، انبیاء کی دعائیں، شفاء، حفاظت اور روزمرہ زندگی کی دعائیں موضوع کے لحاظ سے پڑھیں۔",
        myduaTitle = "📝 میری دعا (My Dua)",
        myduaDesc = "یہاں آپ اپنے الفاظ میں اپنی دلی اور ذاتی دعائیں لکھ کر ہمیشہ کے لیے محفوظ (Save) کر سکتے ہیں۔",
        mylistsTitle = "📂 میری لسٹ (My Lists)",
        mylistsDesc = "اپنی پسندیدہ دعاؤں کے الگ الگ کسٹم فولڈرز اور فہرستیں بنائیں تاکہ انہیں باآسانی پڑھ سکیں۔",
        saveTitle = "🔖 لسٹ میں محفوظ کریں",
        saveDesc = "اس بٹن پر کلک کر کے آپ اس دعا کو اپنی پسندیدہ فہرست (My Lists) میں محفوظ کر سکتے ہیں تاکہ کبھی بھی آسانی سے پڑھ سکیں۔",
        next = "اگلا ➔",
        done = "سمجھ گیا ✓",
        skip = "چھوڑیں"
    )

    private val ar = DuaTourStrings(
        step = "الخطوة {curr} من {total}",
        quickTitle = "⚡ الوصول السريع",
        quickDesc = "وصول فوري للأدعية اليومية الأساسية كأذكار الصباح والمساء، أدعية ما بعد الصلاة، النوم، والسفر بنقرة واحدة.",
        feelTitle = "🤲 كيف تشعر الآن؟",
        feelDesc = "أدعية نبوية ومأثورة مخصصة لمشاعرك وحالتك النفسية مثل القلق، الحزن، الشكر، وطلب الصبر.",
        catsTitle = "📚 تصفح الأقسام والتصنيفات",
        catsDesc = "تصفح الأدعية القرآنية، أدعية الأنبياء، الرقية والشفاء، الاستغفار ومختلف مجالات الحياة.",
        myduaTitle = "📝 دعائي الخاص (My Dua)",
        myduaDesc = "اكتب واحفظ أدعيتك الشخصية والخاصة بكلماتك ورجائك في هذه المساحة المحفوظة.",
        mylistsTitle = "📂 قوائمي (My Lists)",
        mylistsDesc = "أنشئ مجموعات وقوائم مخصصة لتنظيم أدعيتك المفضلة والرجوع إليها بكل يسر.",
        saveTitle = "🔖 حفظ في القوائم",
        saveDesc = "اضغط هنا لحفظ هذا الدعاء في قوائمك ومجموعاتك المفضلة لسهولة قراءته في أي وقت.",
        next = "التالي ➔",
        done = "فهمت ✓",
        skip = "تخطي"
    )

    private val bn = DuaTourStrings(
        step = "ধাপ {curr} / {total}",
        quickTitle = "⚡ দ্রুত প্রবেশ (Quick Access)",
        quickDesc = "দৈনন্দিন জীবনের জরুরি দোয়া (যেমন সকাল-সন্ধ্যা, নামাজের পরের, ঘুমানোর ও সফরের দোয়া) এক ক্লিকে সহজে পড়ুন।",
        feelTitle = "🤲 আপনি কেমন অনুভব করছেন?",
        feelDesc = "আপনার মানসিক অবস্থা ও অনুভূতির (যেমন দুশ্চিন্তা, দুঃখ, কৃতজ্ঞতা বা ধৈর্য) সাথে সামঞ্জস্যপূর্ণ বিশেষ দোয়া খুঁজুন।",
        catsTitle = "📚 দোয়ার বিভাগসমূহ (Categories)",
        catsDesc = "কুরআনের দোয়া, নবীদের দোয়া, রোগমুক্তি, নিরাপত্তা এবং জীবনের প্রতিটি ক্ষেত্রের দোয়া ক্যাটাগরি অনুযায়ী পড়ুন।",
        myduaTitle = "📝 আমার দোয়া (My Dua)",
        myduaDesc = "এখানে আপনি নিজের ভাষায় আপনার মনের একান্ত ও ব্যক্তিগত দোয়াগুলো লিখে সংরক্ষণ করে রাখতে পারেন।",
        mylistsTitle = "📂 আমার তালিকা (My Lists)",
        mylistsDesc = "পছন্দের দোয়াগুলোর কাস্টম তালিকা ও কালেকশন তৈরি করুন যাতে প্রয়োজনের সময় সহজে খুঁজে পান।",
        saveTitle = "🔖 তালিকায় সংরক্ষণ করুন",
        saveDesc = "এই বাটনে ক্লিক করে দোয়াটি আপনার পছন্দের তালিকায় সংরক্ষণ করুন যাতে যেকোনো সময় সহজে পড়তে পারেন।",
        next = "পরবর্তী ➔",
        done = "বুঝেছি ✓",
        skip = "এড়িয়ে যান"
    )

    private val gu = DuaTourStrings(
        step = "પગલું {curr} / {total}",
        quickTitle = "⚡ ઝડપી ઍક્સેસ (Quick Access)",
        quickDesc = "રોજિંદા જીવનની મહત્વપૂર્ણ દુઆઓ (જેમ કે સવાર-સાંજ, નમાઝ પછીની, સૂવાની અને મુસાફરીની) સરળતાથી વાંચો.",
        feelTitle = "🤲 તમે કેવું અનુભવી રહ્યા છો?",
        feelDesc = "તમારી ભાવનાઓ અને પરિસ્થિતિઓ (જેમ કે ચિંતા, દુઃખ, કૃતજ્ઞતા કે ધીરજ) અનુસાર મસ્નૂન દુઆઓ શોધો.",
        catsTitle = "📚 દુઆઓની શ્રેણીઓ (Categories)",
        catsDesc = "કુરાની દુઆઓ, પયગંબરોની દુઆઓ, શિફા, સલામતી અને રોજિંદા જીવનની તમામ દુઆઓ વર્ગીકૃત રીતે વાંચો.",
        myduaTitle = "📝 મારી દુઆ (My Dua)",
        myduaDesc = "અહીં તમે તમારા પોતાના શબ્દોમાં તમારી અંગત દુઆઓ લખીને હંમેશા માટે સાચવી (Save) શકો છો.",
        mylistsTitle = "📂 મારી યાદી (My Lists)",
        mylistsDesc = "તમારી મનપસંદ દુઆઓના કસ્ટમ ફોલ્ડર્સ અને લિસ્ટ બનાવો જેથી તેમને સરળતાથી વાંચી શકાય.",
        saveTitle = "🔖 યાદીમાં સાચવો",
        saveDesc = "આ બટન પર ક્લિક કરીને આ દુઆને તમારી મનપસંદ યાદીમાં સાચવો જેથી ગમે ત્યારે સરળતાથી વાંચી શકો.",
        next = "આગળ ➔",
        done = "સમજાઈ ગયું ✓",
        skip = "છોડો"
    )

    private val ta = DuaTourStrings(
        step = "படி {curr} / {total}",
        quickTitle = "⚡ விரைவு அணுகல் (Quick Access)",
        quickDesc = "காலை-மாலை, தொழுகைக்குப் பின், உறங்கும் போது மற்றும் பயணத்திற்கான அத்தியாவசிய துஆக்களை ஒரே தொடுதலில் படியுங்கள்.",
        feelTitle = "🤲 நீங்கள் எப்படி உணர்கிறீர்கள்?",
        feelDesc = "உங்கள் மனநிலை மற்றும் உணர்வுகளுக்கு (கவலை, சோகம், நன்றி, பொறுமை) ஏற்ற சிறப்பு துஆக்களைக் கண்டறியுங்கள்.",
        catsTitle = "📚 துஆ பிரிவுகள் (Categories)",
        catsDesc = "குர்ஆன் துஆக்கள், நபிமார்களின் துஆக்கள், நிவாரணம், பாதுகாப்பு மற்றும் அன்றாட வாழ்க்கை துஆக்களைப் படியுங்கள்.",
        myduaTitle = "📝 என் துஆ (My Dua)",
        myduaDesc = "உங்கள் சொந்த வார்த்தைகளில் உங்கள் தனிப்பட்ட துஆக்களை எழுதி நிரந்தரமாகச் சேமித்துக் கொள்ளுங்கள்.",
        mylistsTitle = "📂 என் பட்டியல்கள் (My Lists)",
        mylistsDesc = "விருப்பமான துஆக்களைக் கொண்டு தனிப்பயன் பட்டியல்கள் மற்றும் கோப்புகளை உருவாக்கி எளிதாக அணுகுங்கள்.",
        saveTitle = "🔖 பட்டியலில் சேமிக்க",
        saveDesc = "இங்கு கிளிக் செய்து இந்த துஆவை உங்கள் விருப்பப் பட்டியலில் சேமித்து எப்போது வேண்டுமானாலும் படியுங்கள்.",
        next = "அடுத்து ➔",
        done = "புரிந்தது ✓",
        skip = "தவிர்"
    )

    private val te = DuaTourStrings(
        step = "దశ {curr} / {total}",
        quickTitle = "⚡ త్వరిత ప్రాప్యత (Quick Access)",
        quickDesc = "ఉదయం, సాయంత్రం, నమాజ్ తర్వాత, నిద్ర మరియు ప్రయాణానికి సంబంధించిన రోజువారీ ముఖ్యమైన దువాలను చదవండి.",
        feelTitle = "🤲 మీరు ఎలా భావిస్తున్నారు?",
        feelDesc = "మీ భావోద్వేగాలు మరియు పరిస్థితులకు (ఆందోళన, బాధ, కృతజ్ఞత, సహనం) తగిన మస్నూన్ దువాలను కనుగొనండి.",
        catsTitle = "📚 దువా విభాగాలు (Categories)",
        catsDesc = "ఖురాన్ దువాలు, ప్రవక్తల ప్రార్థనలు, ఆరోగ్యం, రక్షణ మరియు రోజువారీ జీవిత దువాలను వర్గాల వారీగా అన్వేషించండి.",
        myduaTitle = "📝 నా దువా (My Dua)",
        myduaDesc = "ఇక్కడ మీరు మీ స్వంత మాటలలో మీ వ్యక్తిగత ప్రార్థనలను వ్రాసి భద్రపరుచుకోవచ్చు.",
        mylistsTitle = "📂 నా జాబితాలు (My Lists)",
        mylistsDesc = "మీకు ఇష్టమైన దువాలతో అనుకూల జాబితాలను సృష్టించండి మరియు సులభంగా చదవండి.",
        saveTitle = "🔖 జాబితాలో భద్రపరచండి",
        saveDesc = "ఈ బటన్‌ను నొక్కడం ద్వారా ఈ దువాను మీ ఇష్టమైన జాబితాలో భద్రపరుచుకోవచ్చు.",
        next = "తరువాత ➔",
        done = "అర్థమైంది ✓",
        skip = "దాటవేయి"
    )

    private val ml = DuaTourStrings(
        step = "ഘട്ടം {curr} / {total}",
        quickTitle = "⚡ പെട്ടെന്നുള്ള പ്രവേശനം (Quick Access)",
        quickDesc = "പ്രഭാത-പ്രദോഷ, നിസ്കാര ശേഷമുള്ള, ഉറങ്ങുന്ന നേരത്തെ, യാത്രാ പ്രാർത്ഥനകൾ ഒരൊറ്റ ക്ലിക്കിൽ വായിക്കുക.",
        feelTitle = "🤲 നിങ്ങൾക്ക് ഇപ്പോൾ എന്തുതോന്നുന്നു?",
        feelDesc = "നിങ്ങളുടെ മാനസികാവസ്ഥയ്ക്കും വികാരങ്ങൾക്കും (ഉത്കണ്ഠ, ദുഃഖം, നന്ദി, ക്ഷമ) ഇണങ്ങുന്ന മസ്നൂൻ പ്രാർത്ഥനകൾ കണ്ടെത്തുക.",
        catsTitle = "📚 പ്രാർത്ഥനാ വിഭാഗങ്ങൾ (Categories)",
        catsDesc = "ഖുർആനിക പ്രാർത്ഥനകൾ, പ്രവാചക പ്രാർത്ഥനകൾ, രോഗശമനം, സംരക്ഷണം തുടങ്ങിയ വിഭാഗങ്ങൾ വായിക്കുക.",
        myduaTitle = "📝 എൻ്റെ ദുആ (My Dua)",
        myduaDesc = "നിങ്ങളുടെ സ്വന്തം വാക്കുകളിൽ നിങ്ങളുടെ വ്യക്തിപരമായ പ്രാർത്ഥനകൾ എഴുതി സൂക്ഷിക്കുക.",
        mylistsTitle = "📂 എൻ്റെ ലിസ്റ്റുകൾ (My Lists)",
        mylistsDesc = "പ്രിയപ്പെട്ട ദുആകൾ ഉൾപ്പെടുത്തി സ്വന്തം ഫോൾഡറുകളും ലിസ്റ്റുകളും തയ്യാറാക്കുക.",
        saveTitle = "🔖 ലിസ്റ്റിൽ സൂക്ഷിക്കുക",
        saveDesc = "ഈ ദുആ നിങ്ങളുടെ പ്രിയപ്പെട്ട ലിസ്റ്റിലേക്ക് സേവ് ചെയ്യാൻ ഇവിടെ ക്ലിക്ക് ചെയ്യുക.",
        next = "അടുത്തത് ➔",
        done = "മനസ്സിലായി ✓",
        skip = "ഒഴിവാക്കുക"
    )

    private val mr = DuaTourStrings(
        step = "टप्पा {curr} / {total}",
        quickTitle = "⚡ जलद प्रवेश (Quick Access)",
        quickDesc = "सकाळ-संध्याकाळ, नमाजनंतर, झोपताना व प्रवासादरम्यानच्या आवश्यक दैनंदिन दुआ एका क्लिकवर वाचा.",
        feelTitle = "🤲 तुम्हाला कसे वाटत आहे?",
        feelDesc = "तुमच्या भावना व सद्यस्थितीनुसार (उदा. चिंता, दुःख, कृतज्ञता किंवा संयम) खास मसनून दुआ शोधा.",
        catsTitle = "📚 दुआंचे वर्गीकरण (Categories)",
        catsDesc = "कुराणातील दुआ, पैगंबरांच्या दुआ, आजारपण, संरक्षण आणि दैनंदिन जीवनातील दुआ वेगवेगळ्या श्रेणींमध्ये वाचा.",
        myduaTitle = "📝 माझी दुआ (My Dua)",
        myduaDesc = "येथे तुम्ही तुमच्या स्वतःच्या शब्दांत वैयक्तिक प्रार्थना लिहून कायमस्वरूपी सेव्ह करू शकता.",
        mylistsTitle = "📂 माझी यादी (My Lists)",
        mylistsDesc = "आवडत्या दुआंचे कस्टमाइझ फोल्डर्स आणि याद्या तयार करा जेणेकरून त्या सहज वाचता येतील.",
        saveTitle = "🔖 यादीमध्ये सेव्ह करा",
        saveDesc = "ही दुआ तुमच्या आवडीच्या यादीत सेव्ह करण्यासाठी येथे क्लिक करा जेणेकरून ती कधीही वाचता येईल.",
        next = "पुढे ➔",
        done = "समजले ✓",
        skip = "वगळा"
    )

    private val pa = DuaTourStrings(
        step = "ਕਦਮ {curr} / {total}",
        quickTitle = "⚡ ਤੁਰੰਤ ਪਹੁੰਚ (Quick Access)",
        quickDesc = "ਰੋਜ਼ਾਨਾ ਜੀਵਨ ਦੀਆਂ ਜ਼ਰੂਰੀ ਦੁਆਵਾਂ (ਜਿਵੇਂ ਸਵੇਰ-ਸ਼ਾਮ, ਨਮਾਜ਼ ਤੋਂ ਬਾਅਦ, ਸੌਣ ਅਤੇ ਸਫ਼ਰ ਦੀਆਂ) ਇਕ ਕਲਿੱਕ ਤੇ ਪੜ੍ਹੋ।",
        feelTitle = "🤲 ਤੁਸੀਂ ਕਿਵੇਂ ਮਹਿਸੂਸ ਕਰ ਰਹੇ ਹੋ?",
        feelDesc = "ਆਪਣੇ ਜਜ਼ਬਾਤਾਂ ਅਤੇ ਹਾਲਾਤਾਂ (ਜਿਵੇਂ ਚਿੰਤਾ, ਉਦਾਸੀ, ਸ਼ੁਕਰਗੁਜ਼ਾਰੀ ਜਾਂ ਸਬਰ) ਮੁਤਾਬਕ ਮਸਨੂਨ ਦੁਆਵਾਂ ਲੱਭੋ।",
        catsTitle = "📚 ਦੁਆਵਾਂ ਦੀਆਂ ਸ਼੍ਰੇਣੀਆਂ (Categories)",
        catsDesc = "ਕੁਰਆਨੀ ਦੁਆਵਾਂ, ਨਬੀਆਂ ਦੀਆਂ ਦੁਆਵਾਂ, ਸ਼ਿਫਾ, ਹਿਫ਼ਾਜ਼ਤ ਅਤੇ ਰੋਜ਼ਾਨਾ ਜੀਵਨ ਦੀਆਂ ਦੁਆਵਾਂ ਸ਼੍ਰੇਣੀ ਅਨੁਸਾਰ ਪੜ੍ਹੋ।",
        myduaTitle = "📝 ਮੇਰੀ ਦੁਆ (My Dua)",
        myduaDesc = "ਇੱਥੇ ਤੁਸੀਂ ਆਪਣੇ ਸ਼ਬਦਾਂ ਵਿੱਚ ਆਪਣੀਆਂ ਨਿੱਜੀ ਦੁਆਵਾਂ ਲਿਖ ਕੇ ਸੁਰੱਖਿਅਤ (Save) ਕਰ ਸਕਦੇ ਹੋ।",
        mylistsTitle = "📂 ਮੇਰੀ ਸੂਚੀ (My Lists)",
        mylistsDesc = "ਆਪਣੀਆਂ ਮਨਪਸੰਦ ਦੁਆਵਾਂ ਦੀਆਂ ਵੱਖ-ਵੱਖ ਸੂਚੀਆਂ ਬਣਾਓ ਤਾਂ ਜੋ ਉਹਨਾਂ ਨੂੰ ਆਸਾਨੀ ਨਾਲ ਪੜ੍ਹਿਆ ਜਾ ਸਕੇ।",
        saveTitle = "🔖 ਸੂਚੀ ਵਿੱਚ ਸੇਵ ਕਰੋ",
        saveDesc = "ਇਸ ਬਟਨ ਤੇ ਕਲਿੱਕ ਕਰਕੇ ਇਸ ਦੁਆ ਨੂੰ ਆਪਣੀ ਮਨਪਸੰਦ ਸੂਚੀ ਵਿੱਚ ਸੇਵ ਕਰੋ।",
        next = "ਅੱਗੇ ➔",
        done = "ਸਮਝ ਗਿਆ ✓",
        skip = "ਛੱਡੋ"
    )

    private val kn = DuaTourStrings(
        step = "ಹಂತ {curr} / {total}",
        quickTitle = "⚡ ತ್ವರಿತ ಪ್ರವೇಶ (Quick Access)",
        quickDesc = "ಬೆಳಿಗ್ಗೆ, ಸಂಜೆ, ನಮಾಜ್ ನಂತರ, ನಿದ್ರೆ ಮತ್ತು ಪ್ರಯಾಣದ ದೈನಂದಿನ ಪ್ರಮುಖ ದುಆಗಳನ್ನು ಒಂದೇ ಕ್ಲಿಕ್‌ನಲ್ಲಿ ಓದಿ.",
        feelTitle = "🤲 ನೀವು ಹೇಗೆ ಭಾವಿಸುತ್ತಿದ್ದೀರಿ?",
        feelDesc = "ನಿಮ್ಮ ಭಾವನೆಗಳು ಮತ್ತು ಸಂದರ್ಭಗಳಿಗೆ (ಆತಂಕ, ದುಃಖ, ಕೃತಜ್ಞತೆ, ತಾಳ್ಮೆ) ಅನುಗುಣವಾದ ಮಸ್ನೂನ್ ದುಆಗಳನ್ನು ಹುಡುಕಿ.",
        catsTitle = "📚 ದುಆ ವರ್ಗಗಳು (Categories)",
        catsDesc = "ಕುರಾನ್ ದುಆಗಳು, ಪ್ರವಾದಿಗಳ ಪ್ರಾರ್ಥನೆಗಳು, ರಕ್ಷಣೆ ಮತ್ತು ದೈನಂದಿನ ಜೀವನದ ದುಆಗಳನ್ನು ವರ್ಗಗಳ ಪ್ರಕಾರ ಓದಿ.",
        myduaTitle = "📝 ನನ್ನ ದುಆ (My Dua)",
        myduaDesc = "ಇಲ್ಲಿ ನೀವು ನಿಮ್ಮ ಸ್ವಂತ ಮಾತುಗಳಲ್ಲಿ ನಿಮ್ಮ ವೈಯಕ್ತಿಕ ಪ್ರಾರ್ಥನೆಗಳನ್ನು ಬರೆದು ಉಳಿಸಬಹುದು.",
        mylistsTitle = "📂 ನನ್ನ ಪಟ್ಟಿಗಳು (My Lists)",
        mylistsDesc = "ನಿಮ್ಮ ನೆಚ್ಚಿನ ದುಆಗಳ ಕಸ್ಟಮ್ ಪಟ್ಟಿಗಳನ್ನು ರಚಿಸಿ ಮತ್ತು ಸುಲಭವಾಗಿ ಪ್ರವೇಶಿಸಿ.",
        saveTitle = "🔖 ಪಟ್ಟಿಗೆ ಉಳಿಸಿ",
        saveDesc = "ಈ ದುಆವನ್ನು ನಿಮ್ಮ ನೆಚ್ಚಿನ ಪಟ್ಟಿಗೆ ಉಳಿಸಲು ಇಲ್ಲಿ ಕ್ಲಿಕ್ ಮಾಡಿ.",
        next = "ಮುಂದೆ ➔",
        done = "ಅರ್ಥವಾಯಿತು ✓",
        skip = "ಬಿಟ್ಟುಬಿಡಿ"
    )

    private val or = DuaTourStrings(
        step = "ପଦକ୍ଷେପ {curr} / {total}",
        quickTitle = "⚡ ଶୀଘ୍ର ପ୍ରବେଶ (Quick Access)",
        quickDesc = "ସକାଳ, ସନ୍ଧ୍ୟା, ନମାଜ ପରେ, ଶୋଇବା ଏବଂ ଯାତ୍ରା ସମୟର ଆବଶ୍ୟକୀୟ ଦୁଆଗୁଡ଼ିକୁ ସହଜରେ ପଢ଼ନ୍ତୁ।",
        feelTitle = "🤲 ଆପଣ କିପରି ଅନୁଭବ କରୁଛନ୍ତି?",
        feelDesc = "ନିଜ ଭାବନା ଏବଂ ପରିସ୍ଥିତି ଅନୁସାରେ ଉପଯୁକ୍ତ ମସନୁନ ଦୁଆ ସନ୍ଧାନ କରନ୍ତୁ।",
        catsTitle = "📚 ଦୁଆ ବର୍ଗଗୁଡ଼ିକ (Categories)",
        catsDesc = "କୁରଆନିକ ଦୁଆ, ନବୀମାନଙ୍କ ପ୍ରାର୍ଥନା ଏବଂ ଦୈନନ୍ଦିନ ଜୀବନର ଦୁଆଗୁଡ଼ିକୁ ଶ୍ରେଣୀ ଅନୁଯାୟୀ ପଢ଼ନ୍ତୁ।",
        myduaTitle = "📝 ମୋର ଦୁଆ (My Dua)",
        myduaDesc = "ଏଠାରେ ଆପଣ ନିଜ ଭାଷାରେ ବ୍ୟକ୍ତିଗତ ଦୁଆ ଲେଖି ସୁରକ୍ଷିତ ରଖିପାରିବେ।",
        mylistsTitle = "📂 ମୋର ତାଲିକା (My Lists)",
        mylistsDesc = "ନିଜ ପସନ୍ଦର ଦୁଆଗୁଡ଼ିକର ତାଲିକା ପ୍ରସ୍ତୁତ କରି ସହଜରେ ପଢ଼ନ୍ତୁ।",
        saveTitle = "🔖 ତାଲିକାରେ ସେଭ୍ କରନ୍ତୁ",
        saveDesc = "ଏହି ବଟନ ଉପରେ କ୍ଲିକ୍ କରି ଦୁଆଟିକୁ ଆପଣଙ୍କ ପସନ୍ଦ ତାଲିକାରେ ସେଭ୍ କରନ୍ତୁ।",
        next = "ପରବର୍ତ୍ତୀ ➔",
        done = "ବୁଝିଗଲି ✓",
        skip = "ଛାଡ଼ନ୍ତୁ"
    )

    private val `as` = DuaTourStrings(
        step = "স্তৰ {curr} / {total}",
        quickTitle = "⚡ দ্ৰুত প্ৰৱেশ (Quick Access)",
        quickDesc = "ৰাতিপুৱা-গধূলি, নামাজৰ পিছত, শোৱা আৰু ভ্ৰমণৰ প্ৰয়োজনীয় দোয়াসমূহ সহজে পঢ়ক।",
        feelTitle = "🤲 আপুনি কেনে অনুভৱ কৰিছে?",
        feelDesc = "আপোনাৰ মানসিক অৱস্থা আৰু অনুভৱ অনুসৰি প্ৰয়োজনীয় মাছনুন দোয়াসমূহ বিচাৰি লওক।",
        catsTitle = "📚 দোৱাৰ শ্ৰেণীসমূহ (Categories)",
        catsDesc = "কোৰআনৰ দোয়া, নবীসকলৰ প্ৰাৰ্থনা আৰু দৈনন্দিন জীৱনৰ সকলো দোয়া বিভিন্ন শ্ৰেণীত পঢ়ক।",
        myduaTitle = "📝 মোৰ দোয়া (My Dua)",
        myduaDesc = "ইয়াত আপুনি আপোনাৰ নিজৰ ভাষাত ব্যক্তিগত দোয়াসমূহ লিখি সংৰক্ষণ কৰিব পাৰে।",
        mylistsTitle = "📂 মোৰ তালিকা (My Lists)",
        mylistsDesc = "পছন্দৰ দোয়াসমূহৰ সুকীয়া তালিকা তৈয়াৰ কৰক যাতে সহজে বিচাৰি পায়।",
        saveTitle = "🔖 তালিকাত সংৰক্ষণ কৰক",
        saveDesc = "এই বুটামত ক্লিক কৰি দোয়াটি আপোনাৰ পছন্দৰ তালিকাত সংৰক্ষণ কৰক।",
        next = "পৰৱৰ্তী ➔",
        done = "বুজি পালোঁ ✓",
        skip = "এৰক"
    )

    private val ms = DuaTourStrings(
        step = "Langkah {curr} / {total}",
        quickTitle = "⚡ Akses Pantas (Quick Access)",
        quickDesc = "Akses segera doa harian penting untuk waktu pagi, petang, selepas solat, tidur, dan musafir.",
        feelTitle = "🤲 Bagaimana Perasaan Anda?",
        feelDesc = "Cari doa masnun yang sesuai dengan emosi dan situasi semasa anda seperti kegelisahan, kesedihan, atau kesyukuran.",
        catsTitle = "📚 Kategori Doa (Categories)",
        catsDesc = "Terokai kategori doa lengkap termasuk doa Al-Quran, doa para Nabi, kesembuhan, dan kehidupan harian.",
        myduaTitle = "📝 Doa Saya (My Dua)",
        myduaDesc = "Tulis dan simpan doa peribadi anda yang tulus dengan kata-kata sendiri di ruang khas ini.",
        mylistsTitle = "📂 Senarai Saya (My Lists)",
        mylistsDesc = "Cipta koleksi tersuai dan susun doa kegemaran anda ke dalam senarai peribadi.",
        saveTitle = "🔖 Simpan ke Senarai",
        saveDesc = "Ketik di sini untuk menanda buku dan menyimpan doa ini ke dalam senarai kegemaran anda.",
        next = "Seterusnya ➔",
        done = "Faham ✓",
        skip = "Langkau"
    )

    private val id = DuaTourStrings(
        step = "Langkah {curr} dari {total}",
        quickTitle = "⚡ Akses Cepat (Quick Access)",
        quickDesc = "Akses cepat kumpulan doa harian penting seperti pagi-petang, setelah sholat, sebelum tidur, dan bepergian.",
        feelTitle = "🤲 Apa yang Anda Rasakan?",
        feelDesc = "Temukan doa mustajab yang sesuai dengan suasana hati dan kondisi Anda (gelisah, sedih, bersyukur, atau butuh kesabaran).",
        catsTitle = "📚 Kategori Doa (Categories)",
        catsDesc = "Jelajahi doa Al-Quran, doa para Nabi, kesembuhan, perlindungan, dan doa aktivitas sehari-hari.",
        myduaTitle = "📝 Doa Saya (My Dua)",
        myduaDesc = "Tulis dan simpan doa pribadi dari lubuk hati terdalam dengan kata-kata Anda sendiri.",
        mylistsTitle = "📂 Daftar Saya (My Lists)",
        mylistsDesc = "Buat daftar koleksi doa khusus dan kelompokkan doa-doa favorit Anda dengan rapi.",
        saveTitle = "🔖 Simpan ke Daftar",
        saveDesc = "Ketuk di sini untuk menyimpan doa ini ke dalam daftar favorit Anda agar mudah dibaca kapan saja.",
        next = "Lanjut ➔",
        done = "Paham ✓",
        skip = "Lewati"
    )

    private val tr = DuaTourStrings(
        step = "Adım {curr} / {total}",
        quickTitle = "⚡ Hızlı Erişim (Quick Access)",
        quickDesc = "Sabah-akşam, namaz sonrası, uyku ve yolculuk gibi günlük hayati dualara tek dokunuşla ulaşın.",
        feelTitle = "🤲 Nasıl Hissediyorsunuz?",
        feelDesc = "Endişe, üzüntü, şükür veya sabır gibi ruh halinize ve duygularınıza uygun duaları bulun.",
        catsTitle = "📚 Dua Kategorileri (Categories)",
        catsDesc = "Kuran duaları, Peygamber duaları, şifa, korunma ve günlük yaşam dualarını kategorilerine göre inceleyin.",
        myduaTitle = "📝 Özel Duam (My Dua)",
        myduaDesc = "Kendi cümlelerinizle içten dualarınızı yazıp bu özel alanda kalıcı olarak saklayın.",
        mylistsTitle = "📂 Listelerim (My Lists)",
        mylistsDesc = "Favori dualarınızı özel listeler halinde gruplayın ve istediğiniz zaman kolayca okuyun.",
        saveTitle = "🔖 Listeye Kaydet",
        saveDesc = "Bu duayı favori listelerinize kaydetmek için buraya dokunun.",
        next = "İleri ➔",
        done = "Anladım ✓",
        skip = "Geç"
    )

    private val fa = DuaTourStrings(
        step = "مرحله {curr} از {total}",
        quickTitle = "⚡ دسترسی سریع (Quick Access)",
        quickDesc = "دعاهای مهم روزمره مانند دعاهای صبح و شام، بعد از نماز، خواب و سفر را به آسانی بخوانید.",
        feelTitle = "🤲 چه احساسی دارید؟",
        feelDesc = "دعاهای متناسب با حالات روحی خود (مانند اضطراب، اندوه، شکرگزاری یا طلب صبر) را پیدا کنید.",
        catsTitle = "📚 دسته‌بندی دعاها (Categories)",
        catsDesc = "دعاهای قرآنی، دعای پیامبران، طلب شفا، حفاظت و دعاهای زندگی روزمره را دسته‌بندی شده بخوانید.",
        myduaTitle = "📝 دعای من (My Dua)",
        myduaDesc = "دعاهای شخصی و از صمیم قلب خود را با کلمات خودتان بنویسید و ذخیره کنید.",
        mylistsTitle = "📂 فهرست‌های من (My Lists)",
        mylistsDesc = "مجموعه‌ها و فهرست‌های دلخواه خود را بسازید تا به دعاهای منتخب دسترسی آسان داشته باشید.",
        saveTitle = "🔖 ذخیره در فهرست",
        saveDesc = "برای ذخیره این دعا در فهرست‌های منتخب خود روی این دکمه کلیک کنید.",
        next = "بعدی ➔",
        done = "متوجه شدم ✓",
        skip = "رد کردن"
    )

    private val ha = DuaTourStrings(
        step = "Mataki {curr} na {total}",
        quickTitle = "⚡ Hanyar Sauri (Quick Access)",
        quickDesc = "Samun addu'o'in yau da kullum cikin sauki kamar na safe da yamma, bayan sallah, barci, da tafiya.",
        feelTitle = "🤲 Yaya Kake Ji A Yanzu?",
        feelDesc = "Nemo addu'o'i da suka dace da halin da kake ciki kamar damuwa, bakin ciki, godiya, ko neman hakuri.",
        catsTitle = "📚 Rabe-raben Addu'o'i (Categories)",
        catsDesc = "Duba addu'o'in Alkur'ani, addu'o'in Annabawa, na neman waraka, da na rayuwar yau da kullum.",
        myduaTitle = "📝 Addu'ata (My Dua)",
        myduaDesc = "Rubuta addu'o'inka na kanka da kalmominka ka ajiye su anan.",
        mylistsTitle = "📂 Jerin Addu'o'ina (My Lists)",
        mylistsDesc = "Tsara jerin addu'o'in da kake so domin karanta su cikin sauki a kowane lokaci.",
        saveTitle = "🔖 Ajiye a Jeri",
        saveDesc = "Taba nan domin ajiye wannan addu'a a cikin jerin addu'o'inka da kake so.",
        next = "Gaba ➔",
        done = "Na gane ✓",
        skip = "Wuce"
    )

    private val sw = DuaTourStrings(
        step = "Hatua {curr} ya {total}",
        quickTitle = "⚡ Ufikiaji wa Haraka (Quick Access)",
        quickDesc = "Pata dua muhimu za kila siku kama za asubuhi, jioni, baada ya swala, kulala na kusafiri kwa mguso mmoja.",
        feelTitle = "🤲 Unajisikiaje Sasa?",
        feelDesc = "Pata dua kulingana na hisia na hali yako kama vile wasiwasi, huzuni, shukrani au kutafuta subira.",
        catsTitle = "📚 Makundi ya Dua (Categories)",
        catsDesc = "Vinjari dua za Qur'ani, dua za Mitume, uponyaji, ulinzi na maisha ya kila siku.",
        myduaTitle = "📝 Dua Yangu (My Dua)",
        myduaDesc = "Andika na uhifadhi dua zako binafsi kwa maneno yako mwenyewe.",
        mylistsTitle = "📂 Orodha Zangu (My Lists)",
        mylistsDesc = "Tengeneza orodha za dua unazozipenda ili uzisome kwa urahisi wakati wowote.",
        saveTitle = "🔖 Hifadhi kwenye Orodha",
        saveDesc = "Gonga hapa ili kuhifadhi dua hii kwenye orodha zako unazozipenda.",
        next = "Ifuatayo ➔",
        done = "Nimeelewa ✓",
        skip = "Ruka"
    )

    private val so = DuaTourStrings(
        step = "Tallaabada {curr} ee {total}",
        quickTitle = "⚡ Helid Degdeg ah (Quick Access)",
        quickDesc = "Ka hel ducooyinka maalinlaha ah ee subaxa, galabta, salaadda kadib, hurdada iyo safarka.",
        feelTitle = "🤲 Sideed Dareemaysaa?",
        feelDesc = "Hel ducooyin ku habboon dareenkaaga sida welwelka, murugada, mahadnaqa ama samirka.",
        catsTitle = "📚 Qaybaha Ducooyinka (Categories)",
        catsDesc = "Baadh ducooyinka Qur'aanka, ducooyinkii Nabiyada, caafimaadka, badbaadada iyo nolosha maalinlaha ah.",
        myduaTitle = "📝 Ducadayda (My Dua)",
        myduaDesc = "Halkan ku qoro oo ku keydso ducooyinkaaga gaarka ah erayadaada.",
        mylistsTitle = "📂 Liisaskayda (My Lists)",
        mylistsDesc = "Sameyso liisas gaar ah oo ducooyinka aad jeceshahay si aad mar kasta u akhrisato.",
        saveTitle = "🔖 Ku keydso Liiska",
        saveDesc = "Taabo halkan si aad ducadan ugu keydsato liisaskaaga si aad markasta u hesho.",
        next = "Ku xiga ➔",
        done = "Waan fahmay ✓",
        skip = "Ka gudub"
    )

    private val fr = DuaTourStrings(
        step = "Étape {curr} sur {total}",
        quickTitle = "⚡ Accès Rapide (Quick Access)",
        quickDesc = "Accédez rapidement aux invocations quotidiennes pour le matin, le soir, après la prière, le coucher et le voyage.",
        feelTitle = "🤲 Que Ressentez-vous ?",
        feelDesc = "Trouvez des invocations adaptées à vos émotions actuelles (anxiété, tristesse, gratitude ou patience).",
        catsTitle = "📚 Catégories d'Invocations (Categories)",
        catsDesc = "Explorez les invocations coraniques, prophétiques, de guérison, de protection et de la vie quotidienne.",
        myduaTitle = "📝 Mes Invocations (My Dua)",
        myduaDesc = "Rédigez et conservez vos prières personnelles avec vos propres mots en toute intimité.",
        mylistsTitle = "📂 Mes Listes (My Lists)",
        mylistsDesc = "Créez vos propres listes personnalisées pour regrouper et retrouver vos invocations préférées.",
        saveTitle = "🔖 Enregistrer dans la Liste",
        saveDesc = "Appuyez ici pour enregistrer cette invocation dans vos listes favorites et la relire facilement.",
        next = "Suivant ➔",
        done = "Compris ✓",
        skip = "Passer"
    )

    private val de = DuaTourStrings(
        step = "Schritt {curr} von {total}",
        quickTitle = "⚡ Schnellzugriff (Quick Access)",
        quickDesc = "Schneller Zugriff auf wichtige tägliche Bittgebete für Morgen, Abend, nach dem Gebet, Schlaf und Reisen.",
        feelTitle = "🤲 Wie fühlen Sie sich?",
        feelDesc = "Finden Sie Bittgebete passend zu Ihrer aktuellen Gefühlslage wie Sorgen, Trauer, Dankbarkeit oder Geduld.",
        catsTitle = "📚 Dua-Kategorien (Categories)",
        catsDesc = "Entdecken Sie Koran-Bittgebete, Prophetengebete, Heilung, Schutz und Bittgebete für den Alltag.",
        myduaTitle = "📝 Meine Duas (My Dua)",
        myduaDesc = "Schreiben und speichern Sie Ihre persönlichen Bittgebete in Ihren eigenen Worten.",
        mylistsTitle = "📂 Meine Listen (My Lists)",
        mylistsDesc = "Erstellen Sie individuelle Sammlungen und organisieren Sie Ihre Lieblings-Duas übersichtlich.",
        saveTitle = "🔖 In Liste speichern",
        saveDesc = "Tippen Sie hier, um dieses Bittgebet in Ihren Favoritenlisten zu speichern.",
        next = "Weiter ➔",
        done = "Verstanden ✓",
        skip = "Überspringen"
    )

    private val ru = DuaTourStrings(
        step = "Шаг {curr} из {total}",
        quickTitle = "⚡ Быстрый доступ (Quick Access)",
        quickDesc = "Быстрый доступ к важным ежедневным дуа: утренние и вечерние, после намаза, перед сном и в пути.",
        feelTitle = "🤲 Что вы чувствуете?",
        feelDesc = "Найдите дуа, соответствующие вашим чувствам и жизненным ситуациям: тревога, печаль, благодарность или терпение.",
        catsTitle = "📚 Категории дуа (Categories)",
        catsDesc = "Читайте коранические дуа, молитвы пророков, дуа для исцеления, защиты и повседневной жизни.",
        myduaTitle = "📝 Мои Дуа (My Dua)",
        myduaDesc = "Записывайте и сохраняйте свои личные искренние молитвы своими словами.",
        mylistsTitle = "📂 Мои Списки (My Lists)",
        mylistsDesc = "Создавайте свои коллекции и списки избранных дуа для быстрого повторения в любое время.",
        saveTitle = "🔖 Сохранить в список",
        saveDesc = "Нажмите здесь, чтобы сохранить это дуа в свои персональные списки.",
        next = "Далее ➔",
        done = "Понятно ✓",
        skip = "Пропустить"
    )

    private val zh = DuaTourStrings(
        step = "第 {curr} 步 / 共 {total} 步",
        quickTitle = "⚡ 快捷访问 (Quick Access)",
        quickDesc = "一键快速查阅早晚赞念、拜后祈祷、就寝及旅行等每日重要杜阿。",
        feelTitle = "🤲 您此刻的心境如何？",
        feelDesc = "根据您当前的心境与情境（如焦虑、悲伤、感恩或寻求坚忍）查找相应的圣行祈愿词。",
        catsTitle = "📚 杜阿分类浏览 (Categories)",
        catsDesc = "全面浏览《古兰经》杜阿、众先知祈愿、康复疗愈、庇护及日常生活祈祷词。",
        myduaTitle = "📝 我的杜阿 (My Dua)",
        myduaDesc = "用您自己的语言在此写下由衷的心声与个人祈求，随时珍藏复诵。",
        mylistsTitle = "📂 我的收藏集 (My Lists)",
        mylistsDesc = "建立个人专属祈愿分类，轻松整理并快速复诵喜爱的杜阿。",
        saveTitle = "🔖 收藏至我的列表",
        saveDesc = "点击此处将此杜阿收藏至您的自定义列表中，方便随时重温。",
        next = "下一步 ➔",
        done = "知道了 ✓",
        skip = "跳过"
    )

    fun get(lang: String): DuaTourStrings {
        val clean = lang.lowercase().trim().split("-")[0]
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
