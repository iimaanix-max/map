package com.tasbih.digitaltasbih.data.shahada

data class KalimaModel(
    val id: Int,
    val arabicTitle: String,
    val titleHindi: String,
    val titleUrdu: String,
    val titleEnglish: String,
    val subtitle: String,
    val subtitleEnglish: String,
    val arabicText: String,
    val transliteration: String,
    val translationHindi: String,
    val translationUrdu: String,
    val translationEnglish: String,
    val virtueHindi: String,
    val virtueUrdu: String,
    val virtueEnglish: String,
    val hadithReference: String,
    val audioUrl: String? = null
) {
    // Backward compatibility property
    val localizedTitle: String get() = titleHindi
}

data class ShahadaCondition(
    val number: Int,
    val arabicName: String,
    val titleHindi: String,
    val titleUrdu: String,
    val titleEnglish: String,
    val descHindi: String,
    val descUrdu: String,
    val descEnglish: String,
    val quranOrHadithRefHindi: String,
    val quranOrHadithRefEnglish: String,
    val quranOrHadithRefUrdu: String
) {
    val quranOrHadithRef: String get() = quranOrHadithRefHindi
}

data class HadithVirtue(
    val arabicQuote: String,
    val sourceHindi: String,
    val sourceUrdu: String,
    val sourceEnglish: String,
    val translationHindi: String,
    val translationUrdu: String,
    val translationEnglish: String
) {
    val source: String get() = sourceHindi
}

object ShahadaData {

    val CONDITIONS = listOf(
        ShahadaCondition(
            number = 1,
            arabicName = "العِلْم",
            titleHindi = "इल्म (सही ज्ञान)",
            titleUrdu = "علم (صحیح معرفت)",
            titleEnglish = "Knowledge (Al-'Ilm)",
            descHindi = "कलिमे के अर्थ और नफ़ी (अस्वीकार) व इसबात (स्वीकार) का सही इल्म होना, ताकि इंसान अज्ञानता व शिर्क से बचे।",
            descUrdu = "کلمہ کے معنی، نفی اور اثبات کا صحیح علم ہونا تاکہ انسان جہالت اور شرک سے محفوظ رہے۔",
            descEnglish = "Understanding the true meaning of negation (negating all false gods) and affirmation (affirming Allah alone), which dispels ignorance.",
            quranOrHadithRefHindi = "सूरह मुहम्मद 47:19 — فَاعْلَمْ أَنَّهُ لَا إِلَٰهَ إِلَّا اللَّهُ (जान लो कि अल्लाह के सिवा कोई सच्चा माबूद नहीं)",
            quranOrHadithRefEnglish = "Surah Muhammad 47:19 — 'So know that there is no deity worthy of worship except Allah'",
            quranOrHadithRefUrdu = "سورۃ محمد ۴۷:۱۹ — فَاعْلَمْ أَنَّهُ لَا إِلَٰهَ إِلَّا اللَّهُ (جان لیں کہ اللہ کے سوا کوئی سچا معبود نہیں)"
        ),
        ShahadaCondition(
            number = 2,
            arabicName = "اليَقِين",
            titleHindi = "यक़ीन (अटूट विश्वास)",
            titleUrdu = "یقین (کامل بھروسہ)",
            titleEnglish = "Certainty (Al-Yaqeen)",
            descHindi = "दिल में बिना किसी संदेह, शक या हिचकिचाहट के मुकम्मल और पुख्ता यक़ीन होना कि अल्लाह ही सच्चा रब है।",
            descUrdu = "دل میں بغیر کسی شک و شبہ کے کامل یقین ہونا کہ صرف اللہ ہی سچا معبود ہے۔",
            descEnglish = "Complete certainty in the heart without a shadow of doubt, hesitation, or skepticism.",
            quranOrHadithRefHindi = "सूरह अल-हुजुरात 49:15 — (सच्चे मोमिन वही हैं जो अल्लाह और उसके रसूल पर ईमान लाए फिर शक नहीं किया)",
            quranOrHadithRefEnglish = "Surah Al-Hujurat 49:15 — 'The believers are only those who believe in Allah and His Messenger and then do not doubt'",
            quranOrHadithRefUrdu = "سورۃ الحجرات ۴۹:۱۵ — (سچے مومن وہی ہیں جو اللہ اور اس کے رسول پر ایمان لائے پھر شک نہیں کیا)"
        ),
        ShahadaCondition(
            number = 3,
            arabicName = "الإِخْلَاص",
            titleHindi = "इख़्लास (सच्ची निष्ठा)",
            titleUrdu = "اخلاص (خالص نیت)",
            titleEnglish = "Sincerity (Al-Ikhlas)",
            descHindi = "हर अमल और इबादत केवल और केवल अल्लाह की रज़ा के लिए करना, रियाकारी (दिखावे) और शिर्क से पाक।",
            descUrdu = "ہر عمل اور عبادت کو دکھاوے اور شرک سے پاک کر کے صرف اللہ کی رضا کے لیے بجا لانا۔",
            descEnglish = "Purifying one's worship and intention solely for the pleasure of Allah, completely free of hypocrisy and Shirk.",
            quranOrHadithRefHindi = "सूरह अल-बय्यिना 98:5 — وَمَا أُمِرُوا إِلَّا لِيَعْبُدُوا اللَّهَ مُخْلِصِينَ لَهُ الدِّينَ",
            quranOrHadithRefEnglish = "Surah Al-Bayyinah 98:5 — 'And they were not commanded except to worship Allah, [being] sincere to Him in religion'",
            quranOrHadithRefUrdu = "سورۃ البینہ ۹۸:۵ — وَمَا أُمِرُوا إِلَّا لِيَعْبُدُوا اللَّهَ مُخْلِصِينَ لَهُ الدِّينَ"
        ),
        ShahadaCondition(
            number = 4,
            arabicName = "الصِّدْق",
            titleHindi = "सिद्क़ (सच्चाई व निष्कपटता)",
            titleUrdu = "صدق (سچائی)",
            titleEnglish = "Truthfulness (As-Sidq)",
            descHindi = "ज़बान और दिल में पूरी समानता होना। मुनाफ़िक़ों की तरह केवल ज़बानी इक़रार नहीं बल्कि दिल से सच मानना।",
            descUrdu = "زبان اور دل میں مکمل مطابقت ہونا، منافقین کی طرح محض زبانی اقرار نہیں بلکہ باطنی سچائی۔",
            descEnglish = "Harmony between the tongue and the heart, truthful declaration in direct contrast to hypocrisy (Nifaq).",
            quranOrHadithRefHindi = "सहीह बुख़ारी 128 — (जो शख्स दिल से सच्चे यक़ीन के साथ गवाही दे, अल्लाह उस पर दोज़ख़ की आग हराम कर देता है)",
            quranOrHadithRefEnglish = "Sahih al-Bukhari 128 — 'Whoever testifies sincerely from the heart, Allah forbids him to the Fire'",
            quranOrHadithRefUrdu = "صحیح بخاری ۱۲۸ — (جو شخص دل کی سچائی سے گواہی دے اللہ اس پر جہنم کی آگ حرام فرما دیتا ہے)"
        ),
        ShahadaCondition(
            number = 5,
            arabicName = "المَحَبَّة",
            titleHindi = "महब्बत (परम प्रेम)",
            titleUrdu = "محبت (عشقِ الٰہی)",
            titleEnglish = "Love (Al-Mahabbah)",
            descHindi = "इस कलिमे, अल्लाह तआला, रसूलुल्लाह ﷺ और उनके सच्चे बंदों से दुनिया की हर चीज़ से बढ़कर महब्बत करना।",
            descUrdu = "اللہ، اس کے رسول ﷺ، اس کلمے اور تمام اہل ایمان سے دنیا کی ہر چیز سے بڑھ کر محبت کرنا۔",
            descEnglish = "Loving Allah, His Messenger ﷺ, this noble testimony, and the righteous believers above all worldly attachments.",
            quranOrHadithRefHindi = "सूरह अल-बक़रह 2:165 — وَالَّذِينَ آمَنُوا أَشَدُّ حُبًّا لِلَّهِ (और जो ईमान वाले हैं वे अल्लाह से सबसे तीव्र प्रेम रखते हैं)",
            quranOrHadithRefEnglish = "Surah Al-Baqarah 2:165 — 'And those who believe are stronger in love for Allah'",
            quranOrHadithRefUrdu = "سورۃ البقرۃ ۲:۱۶۵ — وَالَّذِينَ آمَنُوا أَشَدُّ حُبًّا لِلَّهِ (اور جو ایمان والے ہیں وہ اللہ سے سب سے زیادہ محبت کرتے ہیں)"
        ),
        ShahadaCondition(
            number = 6,
            arabicName = "الانْقِيَاد",
            titleHindi = "इन्क़ियाद (आज्ञाकारिता व अमल)",
            titleUrdu = "انقیاد (فرمانبرداری)",
            titleEnglish = "Submission & Compliance (Al-Inqiyad)",
            descHindi = "अल्लाह के अहकामात और नबी ﷺ की सुन्नत पर ख़ुशी व निष्ठा से अमल करना और उनके फ़ैसलों के आगे झुकना।",
            descUrdu = "اللہ کے احکامات اور نبی ﷺ کی سنت کے آگے دل و جان سے سر تسلیم خم کرنا اور عمل کرنا۔",
            descEnglish = "Practical physical and spiritual submission to Allah's commandments and the Sunnah of His Prophet ﷺ.",
            quranOrHadithRefHindi = "सूरह लुक़मान 31:22 — وَمَنْ يُسْلِمْ وَجْهَهُ إِلَى اللَّهِ وَهُوَ مُحْسِنٌ (और जो अपना रुख़ अल्लाह की तरफ़ झुका दे)",
            quranOrHadithRefEnglish = "Surah Luqman 31:22 — 'And whoever submits his face to Allah while he is a doer of good'",
            quranOrHadithRefUrdu = "سورۃ لقمان ۳۱:۲۲ — وَمَنْ يُسْلِمْ وَجْهَهُ إِلَى اللَّهِ وَهُوَ مُحْسِنٌ"
        ),
        ShahadaCondition(
            number = 7,
            arabicName = "القَبُول",
            titleHindi = "क़बूल (पूर्ण स्वीकृति)",
            titleUrdu = "قبول (کامل تسلیم)",
            titleEnglish = "Acceptance (Al-Qabool)",
            descHindi = "दीन-ए-इस्लाम के हर हुक्म को दिलोजान से स्वीकार करना और तकब्बुर (घमंड) या हठधर्मिता से बचकर तस्लीम करना।",
            descUrdu = "دین اسلام کے تمام احکام کو صدقِ دل سے قبول کرنا اور تکبر و سرکشی سے بچنا۔",
            descEnglish = "Wholehearted acceptance of the message of Tawheed, steering clear of stubbornness, arrogance, and rejection.",
            quranOrHadithRefHindi = "सूरह अस-साफ़्फ़ात 37:35 — (जब उनसे कहा जाता था कि अल्लाह के सिवा कोई माबूद नहीं, तो वे तकब्बुर करते थे)",
            quranOrHadithRefEnglish = "Surah As-Saffat 37:35 — 'Indeed, when it was said to them, 'There is no deity except Allah,' they were arrogant'",
            quranOrHadithRefUrdu = "سورۃ الصافات ۳۷:۳۵ — (جب ان سے کہا جاتا تھا کہ اللہ کے سوا کوئی معبود نہیں تو وہ تکبر کرتے تھے)"
        )
    )

    val VIRTUES = listOf(
        HadithVirtue(
            arabicQuote = "بُنِيَ الْإِسْلَامُ عَلَى خَمْسٍ: شَهَادَةِ أَنْ لَا إِلَهَ إِلَّا اللَّهُ وَأَنَّ مُحَمَّدًا رَسُولُ اللَّهِ...",
            sourceHindi = "सहीह बुख़ारी 8, सहीह मुस्लिम 16 (हज़रत इब्ने उमर रज़ि.)",
            sourceUrdu = "صحیح بخاری ۸، صحیح مسلم ۱۶ (حضرت عبداللہ بن عمرؓ)",
            sourceEnglish = "Sahih al-Bukhari 8, Sahih Muslim 16 (Narrated by Ibn Umar RA)",
            translationHindi = "इस्लाम की बुनियाद 5 सुतूनों पर रखी गई है: सबसे पहली और बुनियादी चीज़ इस बात की गवाही देना है कि अल्लाह के सिवा कोई सच्चा माबूद नहीं और मुहम्मद ﷺ अल्लाह के रसूल हैं...",
            translationUrdu = "اسلام کی بنیاد پانچ ستونوں پر رکھی گئی ہے: اس بات کی گواہی دینا کہ اللہ کے سوا کوئی معبود نہیں اور محمد ﷺ اللہ کے رسول ہیں...",
            translationEnglish = "Islam is built upon five pillars: Testifying that none has the right to be worshipped but Allah and that Muhammad is the Messenger of Allah, establishing prayer, giving Zakat, performing Hajj, and fasting Ramadan."
        ),
        HadithVirtue(
            arabicQuote = "مَنْ مَاتَ وَهُوَ يَعْلَمُ أَنَّهُ لَا إِلَهَ إِلَّا اللَّهُ، دَخَلَ الْجَنَّةَ",
            sourceHindi = "सहीह मुस्लिम 26 (हज़रत उस्मान बिन अफ़्फ़ान रज़ि.)",
            sourceUrdu = "صحیح مسلم ۲۶ (حضرت عثمان بن عفانؓ)",
            sourceEnglish = "Sahih Muslim 26 (Narrated by Uthman ibn Affan RA)",
            translationHindi = "जिस शख्स का इंतिक़ाल इस हाल में हुआ कि वह यह पक्का इल्म व यक़ीन रखता था कि अल्लाह के सिवा कोई सच्चा माबूद नहीं, वह जन्नत में दाख़िल होगा।",
            translationUrdu = "جس کی وفات اس حال میں ہوئی کہ وہ یقین کے ساتھ جانتا تھا کہ اللہ کے سوا کوئی معبود نہیں، وہ جنت میں داخل ہوگا۔",
            translationEnglish = "He who died knowing full well that there is no god worthy of worship except Allah will enter Paradise."
        ),
        HadithVirtue(
            arabicQuote = "أَفْضَلُ الذِّكْرِ لَا إِلَهَ إِلَّا اللَّهُ، وَأَفْضَلُ الدُّعَاءِ الْحَمْدُ لِلَّهِ",
            sourceHindi = "जामिअ अत-तिर्मिज़ी 3383 (हज़रत जाबिर रज़ि. — हदीस हसन)",
            sourceUrdu = "جامع ترمذی ۳۳۸۳ (حضرت جابرؓ — حسن)",
            sourceEnglish = "Jami` at-Tirmidhi 3383, Sunan Ibn Majah 3800 (Hasan, Narrated by Jabir RA)",
            translationHindi = "सबसे अफ़ज़ल और बेहतरीन ज़िक्र 'ला इलाहा इल्लल्लाह' है, और सबसे बेहतरीन दुआ 'अलहम्दुलिल्लाह' है।",
            translationUrdu = "سب سے افضل ذکر 'لا الہ الا اللہ' ہے، اور سب سے افضل دعا 'الحمد للہ' ہے۔",
            translationEnglish = "The best remembrance (Dhikr) is 'La ilaha ill-Allah', and the best supplication (Dua) is 'Alhamdulillah'."
        ),
        HadithVirtue(
            arabicQuote = "مَنْ كَانَ آخِرُ كَلَامِهِ لَا إِلَهَ إِلَّا اللَّهُ دَخَلَ الْجَنَّةَ",
            sourceHindi = "सुनन अबी दाऊद 3116, सहीह अल-जामिअ 6479 (हज़रत मुआज़ रज़ि.)",
            sourceUrdu = "سنن ابو داؤد ۳۱۱۶، صحیح الجامع ۶۴۷۹ (حضرت معاذؓ)",
            sourceEnglish = "Sunan Abi Dawud 3116, Sahih al-Jami` 6479 (Sahih, Narrated by Mu'adh RA)",
            translationHindi = "जिस इंसान का आख़िरी कलाम (दुनिया से रुख़सत होते वक़्त) 'ला इलाहा इल्लल्लाह' होगा, वह सीधा जन्नत में दाख़िल होगा।",
            translationUrdu = "جس کا آخری کلام (دنیا سے رخصت ہوتے وقت) 'لا الہ الا اللہ' ہوگا، وہ جنت میں داخل ہوگا۔",
            translationEnglish = "He whose last words in this worldly life are 'La ilaha ill-Allah' will enter Paradise."
        )
    )

    val KALIMAS = listOf(
        KalimaModel(
            id = 1,
            arabicTitle = "الكَلِمَةُ الطَّيِّبَةُ",
            titleHindi = "पहला कलिमा: तय्यिब (पवित्रता)",
            titleUrdu = "پہلا کلمہ: طیب (پاکیزگی)",
            titleEnglish = "1st Kalima: Tayyibah (Purity)",
            subtitle = "Kalima Tayyibah — Declaration of Purity",
            subtitleEnglish = "Declaration of Faith and Purity",
            arabicText = "لَا إِلٰهَ إِلَّا اللهُ مُحَمَّدٌ رَسُولُ اللهِ",
            transliteration = "Laa ilaaha ill-Allahu Muhammadur Rasoolullah",
            translationHindi = "अल्लाह के सिवा कोई सच्चा माबूद (उपास्य) नहीं, और हज़रत मुहम्मद ﷺ अल्लाह के सच्चे रसूल हैं।",
            translationUrdu = "اللہ کے سوا کوئی معبود نہیں، محمد ﷺ اللہ کے رسول ہیں۔",
            translationEnglish = "There is no deity worthy of worship except Allah, and Muhammad ﷺ is the Messenger of Allah.",
            virtueHindi = "यह इस्लाम का बुनियादी कलिमा है, इसे पढ़ने से इंसान कुफ़्र और शिर्क से निकलकर इस्लाम के दायरे में दाख़िल होता है।",
            virtueUrdu = "یہ اسلام کا بنیادی کلمہ ہے، اس کے اقرار سے انسان کفر و شرک سے نکل کر اسلام میں داخل ہوتا ہے۔",
            virtueEnglish = "The foundational pillar of Islamic faith. Declaring it brings one into the fold of Islam and cleanses the soul.",
            hadithReference = "Surah Muhammad 47:19, Surah Al-Fath 48:29, Sahih al-Bukhari 8, Sahih Muslim 16"
        ),
        KalimaModel(
            id = 2,
            arabicTitle = "كَلِمَةُ الشَّهَادَةِ",
            titleHindi = "दूसरा कलिमा: शहादत (गवाही)",
            titleUrdu = "دوسرا کلمہ: شہادت (گواہی)",
            titleEnglish = "2nd Kalima: Shahadah (Testimony)",
            subtitle = "Kalima Shahadat — Declaration of Testimony",
            subtitleEnglish = "Declaration of Sacred Testimony",
            arabicText = "أَشْهَدُ أَنْ لَا إِلٰهَ إِلَّا اللهُ وَحْدَهُ لَا شَرِيكَ لَهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ",
            transliteration = "Ash-hadu an laa ilaaha ill-Allahu wahdahu laa shareeka lahu wa ash-hadu anna Muhammadan 'abduhu wa Rasooluh",
            translationHindi = "मैं गवाही देता हूँ कि अल्लाह के सिवा कोई सच्चा माबूद नहीं, वह अकेला है, उसका कोई शरीक (साझीदार) नहीं, और मैं गवाही देता हूँ कि हज़रत मुहम्मद ﷺ उसके बंदे और रसूल हैं।",
            translationUrdu = "میں گواہی دیتا ہوں کہ اللہ کے سوا کوئی معبود نہیں، وہ یکتا ہے، اس کا کوئی شریک نہیں، اور میں گواہی دیتا ہوں کہ محمد ﷺ اس کے بندے اور رسول ہیں۔",
            translationEnglish = "I testify that there is no deity worthy of worship except Allah, alone without partner, and I testify that Muhammad ﷺ is His servant and Messenger.",
            virtueHindi = "इस्लाम का पहला अरकान (स्तंभ)। वुज़ू के बाद इस कलिमे को पढ़ने वाले के लिए जन्नत के आठों दरवाज़े खोल दिए जाते हैं (सहीह मुस्लिम 234)।",
            virtueUrdu = "اسلام کا پہلا رکن۔ وضو کے بعد اسے پڑھنے والے کے لیے جنت کے آٹھوں دروازے کھول دیے جاتے ہیں (صحیح مسلم ۲۳۴)۔",
            virtueEnglish = "The core first pillar of Islam. Reciting it after ablution (Wudu) opens all eight gates of Paradise (Sahih Muslim 234).",
            hadithReference = "Sahih Muslim 234, Sahih al-Bukhari 831, Jami` at-Tirmidhi 55"
        ),
        KalimaModel(
            id = 3,
            arabicTitle = "كَلِمَةُ التَّمْجِيدِ",
            titleHindi = "तीसरा कलिमा: तमजीद (महानता)",
            titleUrdu = "تیسرا کلمہ: تمجید (بزرگی)",
            titleEnglish = "3rd Kalima: Tamjeed (Glorification)",
            subtitle = "Kalima Tamjeed — Glorification of Allah",
            subtitleEnglish = "Exaltation and Praise of the Almighty",
            arabicText = "سُبْحَانَ اللهِ وَالْحَمْدُ لِلَّهِ وَلَا إِلٰهَ إِلَّا اللهُ وَاللهُ أَكْبَرُ وَلَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللهِ الْعَلِيِّ الْعَظِيمِ",
            transliteration = "Subhaan-Allahi wal-hamdu lillaahi wa laa ilaaha ill-Allahu wallaahu Akbar, wa laa hawla wa laa quwwata illaa billaahil-'Aliyyil-'Azeem",
            translationHindi = "अल्लाह हर ऐब से पाक है, और तमाम तारीफ़ें अल्लाह ही के लिए हैं, और अल्लाह के सिवा कोई माबूद नहीं, और अल्लाह सबसे बड़ा है। और गुनाहों से बचने और नेकी करने की ताक़त सिर्फ़ आलीशान और अज़ीम अल्लाह की मदद से है।",
            translationUrdu = "اللہ پاک ہے اور تمام تعریفیں اللہ ہی کے لیے ہیں اور اللہ کے سوا کوئی معبود نہیں اور اللہ سب سے بڑا ہے، اور گناہوں سے بچنے کی طاقت اور نیکی کرنے کی قوت صرف بزرگ اور عظمت والے اللہ کی طرف سے ہے۔",
            translationEnglish = "Glory be to Allah, and praise be to Allah, and there is no deity except Allah, and Allah is the Greatest. And there is no might nor power except with Allah, the Most High, the Supreme.",
            virtueHindi = "रसूलुल्लाह ﷺ ने फ़रमाया: यह कलाम मुझे उस पूरी दुनिया और जो कुछ इसमें है, उससे ज़्यादा महबूब है (सहीह मुस्लिम 2137)।",
            virtueUrdu = "رسول اللہ ﷺ نے فرمایا: یہ کلمات مجھے دنیا اور جو کچھ اس میں ہے اس سے زیادہ محبوب ہیں (صحیح مسلم ۲۱۳۷)۔",
            virtueEnglish = "The Prophet ﷺ said these words are more beloved to him than everything over which the sun rises (Sahih Muslim 2137).",
            hadithReference = "Sahih Muslim 2137, Sunan an-Nasa'i 10684, Sahih Ibn Hibban 840, Musnad Ahmad 513"
        ),
        KalimaModel(
            id = 4,
            arabicTitle = "كَلِمَةُ التَّوْحِيدِ",
            titleHindi = "चौथा कलिमा: तौहीद (एकत्व)",
            titleUrdu = "چوتھا کلمہ: توحید (یکتائی)",
            titleEnglish = "4th Kalima: Tawheed (Oneness)",
            subtitle = "Kalima Tawheed — Declaration of Absolute Oneness",
            subtitleEnglish = "Proclamation of Absolute Divine Monotheism",
            arabicText = "لَا إِلٰهَ إِلَّا اللهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ يُحْيِي وَيُمِيتُ وَهُوَ حَيٌّ لَا يَمُوتُ أَبَدًا أَبَدًا، ذُو الْجَلَالِ وَالْإِكْرَامِ، بِيَدِهِ الْخَيْرُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ",
            transliteration = "Laa ilaaha ill-Allahu wahdahu laa shareeka lah, lahul-mulku wa lahul-hamdu yuhyee wa yumeetu wa Huwa hayyun laa yamootu abadan abadaa, Zul-Jalaali wal-Ikraam, biyadihil-khayr, wa Huwa 'alaa kulli shay'in Qadeer",
            translationHindi = "अल्लाह के सिवा कोई सच्चा माबूद नहीं, वह अकेला है, उसका कोई शरीक नहीं। उसी की बादशाही है और उसी के लिए सारी प्रशंसा है। वही ज़िंदा करता है और वही मौत देता है, और वह हमेशा ज़िंदा रहने वाला है जिसे कभी मौत नहीं आनी। वह बड़ी शान व करम वाला है, उसी के हाथ में सारी भलाई है, और वह हर चीज़ पर पूरी ताक़त रखने वाला है।",
            translationUrdu = "اللہ کے سوا کوئی معبود نہیں، وہ اکیلا ہے، اس کا کوئی شریک نہیں، اسی کے لیے بادشاہت ہے اور اسی کے لیے حمد ہے، وہی زندہ کرتا ہے اور وہی مارتا ہے اور وہ ہمیشہ زندہ رہنے والا ہے جسے کبھی موت نہیں، عظمت اور بزرگی والا ہے، اسی کے ہاتھ میں تمام بھلائی ہے اور وہ ہر چیز پر قادر ہے۔",
            translationEnglish = "There is no deity except Allah, alone without partner. His is the sovereignty and His is the praise. He gives life and causes death, and He is Ever-Living who never dies. Possessor of Majesty and Honor, in His hand is all goodness, and He is over all things Omnipotent.",
            virtueHindi = "दिन में 100 बार पढ़ने पर 100 नेकियां मिलती हैं, 100 गुनाह माफ़ होते हैं और शैतान से मुकम्मल हिफ़ाज़त मिलती है (सहीह बुख़ारी 3293, 6403)।",
            virtueUrdu = "دن میں ۱۰۰ بار پڑھنے پر ۱۰۰ نیکیاں ملتی ہیں، ۱۰۰ گناہ معاف ہوتے ہیں اور شیطان سے حفاظت رہتی ہے (صحیح بخاری ۳۲۹۳)۔",
            virtueEnglish = "Reciting 100 times daily earns the reward of freeing 10 slaves, grants 100 good deeds, erases 100 sins, and protects from Shaytan (Sahih Bukhari 3293).",
            hadithReference = "Sahih al-Bukhari 3293, 6403, Sahih Muslim 2691, Jami` at-Tirmidhi 3428, Ibn Majah 2235"
        ),
        KalimaModel(
            id = 5,
            arabicTitle = "كَلِمَةُ الِاسْتِغْفَارِ",
            titleHindi = "पांचवां कलिमा: इस्तिग़फ़ार (क्षमा याचना)",
            titleUrdu = "پانچواں کلمہ: استغفار (طلبِ مغفرت)",
            titleEnglish = "5th Kalima: Astaghfar (Forgiveness)",
            subtitle = "Kalima Astaghfar — Seeking Forgiveness",
            subtitleEnglish = "Repentance and Seeking Forgiveness",
            arabicText = "أَسْتَغْفِرُ اللهَ رَبِّي مِنْ كُلِّ ذَنْبٍ أَذْنَبْتُهُ عَمَدًا أَوْ خَطَأً سِرًّا أَوْ عَلَانِيَةً وَأَتُوبُ إِلَيْهِ مِنَ الذَّنْبِ الَّذِي أَعْلَمُ وَمِنَ الذَّنْبِ الَّذِي لَا أَعْلَمُ إِنَّكَ أَنْتَ عَلَّامُ الْغُيُوبِ وَسَتَّارُ الْعُيُوبِ وَغَفَّارُ الذُّنُوبِ وَلَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللهِ الْعَلِيِّ الْعَظِيمِ",
            transliteration = "Astaghfirullaaha Rabbee min kulli zambin aznabtuhu 'amadan aw khata-an sirran aw 'alaaniyyatan wa atoobu ilayhi minaz-zambil-lazee a'lamu wa minaz-zambil-lazee laa a'lam, innaka Anta 'Allaamul-Ghuyoobi wa Sattaarul-'Uyoobi wa Ghaffaaruz-Zunoobi wa laa hawla wa laa quwwata illaa billaahil-'Aliyyil-'Azeem",
            translationHindi = "मैं अपने रब अल्लाह से अपने हर गुनाह की माफ़ी मांगता हूँ, जो मैंने जानबूझकर किया हो या भूलकर, छुपकर किया हो या सबके सामने। और मैं उसकी बारगाह में उन गुनाहों से भी तौबा करता हूँ जिन्हें मैं जानता हूँ, और उन गुनाहों से भी जिन्हें मैं नहीं जानता। बेशक तू ग़ैब का जानने वाला, ऐबों को छुपाने वाला और गुनाहों को बख्शने वाला है।",
            translationUrdu = "میں اللہ سے اپنے تمام گناہوں کی بخشش مانگتا ہوں جو میں نے جان بوجھ کر کیے یا بھولے سے، چھپ کر کیے یا اعلانیہ، اور میں اس سے توبہ کرتا ہوں ان گناہوں سے جنہیں میں جانتا ہوں اور جنہیں نہیں جانتا، بے شک تو غیب جاننے والا، عیب چھپانے والا اور گناہ معاف فرمانے والا ہے۔",
            translationEnglish = "I seek forgiveness from Allah, my Lord, for every sin I committed intentionally or mistakenly, secretly or openly, and I turn to Him in repentance from the sins that I know and from the sins that I do not know. Indeed, You are the Knower of the unseen, the Concealer of faults, and the Forgiver of sins, and there is no might nor power except with Allah, the Most High, the Supreme.",
            virtueHindi = "इस्तिग़फ़ार से रिज़्क़ में बरकत होती है, दिल का ज़ंग धुलता है और हर परेशानी व ग़म से निकलने का रास्ता खुलता है (सहीह मुस्लिम 771, अबू दाऊद 1517)।",
            virtueUrdu = "استغفار سے رزق میں برکت ہوتی ہے، دل کا زنگ دھلتا ہے اور ہر تنگی سے نجات کا راستہ نکلتا ہے (صحیح مسلم ۷۷۱)۔",
            virtueEnglish = "Consistent repentance removes spiritual distress, cleanses the heart, and opens doors of divine provision and relief (Sahih Muslim 771, Abu Dawud 1517).",
            hadithReference = "Sahih Muslim 771, Musnad Ahmad 19614, Sunan Abi Dawud 1517, Sahih al-Jami` 3731"
        ),
        KalimaModel(
            id = 6,
            arabicTitle = "كَلِمَةُ رَدِّ الْكُفْرِ",
            titleHindi = "छठा कलिमा: रद्द-ए-कुफ़्र (ईमान की हिफ़ाज़त)",
            titleUrdu = "چھٹا کلمہ: ردِ کفر (حفاظتِ ایمان)",
            titleEnglish = "6th Kalima: Radde Kufr (Rejection of Disbelief)",
            subtitle = "Kalima Radde Kufr — Protection from Disbelief & Polytheism",
            subtitleEnglish = "Protection from Disbelief, Polytheism, and Sins",
            arabicText = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنْ أَنْ أُشْرِكَ بِكَ شَيْئًا وَأَنَا أَعْلَمُ بِهِ وَأَسْتَغْفِرُكَ لِمَا لَا أَعْلَمُ بِهِ تُبْتُ عَنْهُ وَتَبَرَّأْتُ مِنَ الْكُفْرِ وَالشِّرْكِ وَالْكِذْبِ وَالْغِيبَةِ وَالْبِدْعَةِ وَالنَّمِيمَةِ وَالْفَوَاحِشِ وَالْبُهْتَانِ وَالْمَعَاصِي كُلِّهَا وَأَسْلَمْتُ وَأَقُولُ لَا إِلٰهَ إِلَّا اللهُ مُحَمَّدٌ رَسُولُ اللهِ",
            transliteration = "Allaahumma innee a'oozu bika min an ushrika bika shay-an wa ana a'lamu bih, wa astaghfiruka limaa laa a'lamu bih, tubtu 'anhu wa tabarra-tu minal-kufri wash-shirki wal-kizbi wal-gheebati wal-bid'ati wan-nameemati wal-fawaahishi wal-buhtaani wal-ma'aasee kullihaa, wa aslamtu wa aqoolu Laa ilaaha ill-Allahu Muhammadur Rasoolullah",
            translationHindi = "ऐ अल्लाह! मैं तेरी पनाह चाहता हूँ इस बात से कि मैं जानबूझकर किसी चीज़ को तेरा शरीक ठहराऊँ, और मैं तुझसे उन गुनाहों की माफ़ी चाहता हूँ जिन्हें मैं नहीं जानता। मैंने इससे तौबा की और मैं बेज़ार हुआ कुफ़्र से, शिर्क से, झूठ से, ग़ीबत (चुगली) से, बिदअत से, बदचलनी से, तोहमत लगाने से और हर गुनाह से। और मैं ईमान लाया और कहता हूँ कि अल्लाह के सिवा कोई माबूद नहीं और मुहम्मद ﷺ अल्लाह के रसूल हैं।",
            translationUrdu = "اے اللہ! میں تیری پناہ مانگتا ہوں اس بات سے کہ میں جان بوجھ کر کسی کو تیرا شریک بناؤں، اور معافی چاہتا ہوں اس سے جو میں نہیں جانتا، میں نے اس سے توبہ کی اور بیزار ہوا کفر سے، شرک سے، جھوٹ سے، غیبت سے، بدعت سے، بےحیائی سے اور تمام گناہوں سے، اور میں اسلام لایا اور اقرار کرتا ہوں کہ اللہ کے سوا کوئی معبود نہیں اور محمد ﷺ اس کے رسول ہیں۔",
            translationEnglish = "O Allah, I seek refuge in You from knowingly associating any partner with You, and I ask Your forgiveness for that which I do not know. I repent from it and disassociate myself from disbelief, polytheism, falsehood, backbiting, innovation, talebearing, obscenities, slander, and all sins. I have submitted to Islam and I declare: There is no deity except Allah, and Muhammad ﷺ is the Messenger of Allah.",
            virtueHindi = "यह कलिमा ईमान को शैतान के हमलों, शिर्क-ए-ख़फ़ी (छिपे हुए शिर्क) और दिल की बुराइयों से महफ़ूज़ रखने का सबसे मज़बूत क़िला है (अल-अदब अल-मुफ़्रद 716)।",
            virtueUrdu = "یہ کلمہ ایمان کو پوشیدہ شرک، نفاق اور شیطانی وسوسوں سے محفوظ رکھنے کا مضبوط ترین قلعہ ہے (الادب المفرد ۷۱۶)۔",
            virtueEnglish = "The Prophet ﷺ taught this supplication to protect believers from subtle and hidden shirk and preserve pure faith (Al-Adab Al-Mufrad 716, Sahih al-Jami` 3731).",
            hadithReference = "Al-Adab Al-Mufrad (Al-Bukhari) 716, Musnad Ahmad 19614, Sahih al-Jami` 3731 (Sahih)"
        )
    )
}
