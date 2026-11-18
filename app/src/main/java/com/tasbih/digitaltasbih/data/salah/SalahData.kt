package com.tasbih.digitaltasbih.data.salah

data class SalahStep(
    val id: Int,
    val stepNameHindi: String,
    val stepNameUrdu: String,
    val stepNameEnglish: String,
    val arabicText: String,
    val transliteration: String,
    val meaningHindi: String,
    val meaningUrdu: String,
    val meaningEnglish: String,
    val instructionHindi: String,
    val instructionEnglish: String,
    val hadithProof: String
)

data class PrayerRakatsInfo(
    val id: String,
    val arabicName: String,
    val nameHindi: String,
    val nameUrdu: String,
    val nameEnglish: String,
    val timeWindowHindi: String,
    val timeWindowEnglish: String,
    val sunnahBefore: Int = 0,
    val fard: Int = 0,
    val sunnahAfter: Int = 0,
    val nafl: Int = 0,
    val witr: Int = 0,
    val totalRakats: Int = 0,
    val detailsHindi: String,
    val detailsEnglish: String
)

data class SalahPillarCondition(
    val number: Int,
    val type: String, // "SHART" (Condition before Salah) or "RUKN" (Pillar within Salah)
    val arabicTitle: String,
    val titleHindi: String,
    val titleUrdu: String,
    val titleEnglish: String,
    val explanationHindi: String,
    val explanationEnglish: String,
    val proofReference: String
)

data class SalahHadithVirtue(
    val arabicText: String,
    val source: String,
    val topicHindi: String,
    val topicEnglish: String,
    val translationHindi: String,
    val translationUrdu: String,
    val translationEnglish: String
)

object SalahData {

    // 12 Step-by-Step Practical Salah Steps
    val STEPS = listOf(
        SalahStep(
            id = 1,
            stepNameHindi = "1. नीयत (Intention)",
            stepNameUrdu = "۱. نیت (ارادہ کرنا)",
            stepNameEnglish = "1. Niyyah (Intention)",
            arabicText = "نَوَيْتُ أَنْ أُصَلِّيَ لِلّٰهِ تَعَالَى",
            transliteration = "Nawaytu an usalliya lillaahi Ta'aala...",
            meaningHindi = "मैंने सिर्फ अल्लाह तआला के लिए इस नमाज़ की नीयत की।",
            meaningUrdu = "میں نے خالص اللہ تعالی کی رضا کے لیے اس نماز کی نیت کی۔",
            meaningEnglish = "I intend to pray for the sake of Allah the Almighty.",
            instructionHindi = "क़िब्ला-रुख़ सीधे खड़े हों। नीयत दिल के पक्के इरादे का नाम है। दिल में यह तय करें कि कौन से वक़्त की और कितनी रकअत नमाज़ पढ़ रहे हैं।",
            instructionEnglish = "Stand upright facing the Qiblah. Niyyah is the firm intention in the heart specifying which prayer and how many rak'ahs you are offering.",
            hadithProof = "सहीह बुख़ारी (हदीस 1) — 'तमाम आमाल का दारोमदार नीयतों पर है।'"
        ),
        SalahStep(
            id = 2,
            stepNameHindi = "2. तकबीर-ए-तहरीमा (Takbeer)",
            stepNameUrdu = "۲. تکبیر تحریمہ",
            stepNameEnglish = "2. Takbeer-e-Tahreema",
            arabicText = "اللهُ أَكْبَرُ",
            transliteration = "Allahu Akbar",
            meaningHindi = "अल्लाह सबसे बड़ा है।",
            meaningUrdu = "اللہ سب سے بڑا ہے۔",
            meaningEnglish = "Allah is the Greatest.",
            instructionHindi = "दोनों हाथों को कानों की लौ (या कंधों) तक उठाएं। हथेलियाँ क़िबला की तरफ़ हों और 'अल्लाहु अकबर' कहते हुए हाथ नाफ़ के नीचे (या सीने पर) बाँध लें।",
            instructionEnglish = "Raise both hands up to the earlobes (or shoulders) with palms facing the Qibla, utter 'Allahu Akbar', and fold hands.",
            hadithProof = "सहीह मुस्लिम 390, अबू दाऊद 618"
        ),
        SalahStep(
            id = 3,
            stepNameHindi = "3. सना (Opening Supplication)",
            stepNameUrdu = "۳. ثناء (حمد و ثنا)",
            stepNameEnglish = "3. Sana (Opening Praise)",
            arabicText = "سُبْحَانَكَ اللّٰهُمَّ وَبِحَمْدِكَ، وَتَبَارَكَ اسْمُكَ، وَتَعَالَىٰ جَدُّكَ، وَلَا إِلٰهَ غَيْرُكَ",
            transliteration = "Subhanak-Allahumma wa bihamdika, wa tabarakasmuka, wa ta'ala jadduka, wa laa ilaaha ghayruk.",
            meaningHindi = "ऐ अल्लाह! तू पाक है, हम तेरी तारीफ़ करते हैं। तेरा नाम बरकत वाला है, तेरी बुज़ुर्गी सबसे आला है, और तेरे सिवा कोई माबूद नहीं।",
            meaningUrdu = "اے اللہ! تو پاک ہے اور تیری ہی تعریف ہے، تیرا نام بابرکت ہے، تیری شان بلند ہے اور تیرے سوا کوئی عبادت کے لائق نہیں۔",
            meaningEnglish = "Glory be to You, O Allah, and all praise is Yours; blessed is Your name, exalted is Your majesty, and there is no deity worthy of worship besides You.",
            instructionHindi = "हाथ बाँधकर नज़र सजदे की जगह रखें और आहिस्ता आवाज़ में सना पढ़ें।",
            instructionEnglish = "With folded hands, keep your gaze fixed on the spot of prostration and recite the Sana softly.",
            hadithProof = "सुनन अबू दाऊद 775, तिर्मिज़ी 242 (सहीह)"
        ),
        SalahStep(
            id = 4,
            stepNameHindi = "4. तअव्वुज़, तस्मिया व सूरह फ़ातिहा",
            stepNameUrdu = "۴. تعوذ، تسمیہ اور سورۃ الفاتحہ",
            stepNameEnglish = "4. Ta'awwudh, Basmalah & Surah Al-Fatiha",
            arabicText = "أَعُوذُ بِاللهِ مِنَ الشَّيْطَانِ الرَّجِيمِ • بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيمِ • الْحَمْدُ لِلّٰهِ رَبِّ الْعَالَمِينَ ۝ الرَّحْمٰنِ الرَّحِيمِ ۝ مَالِكِ يَوْمِ الدِّينِ ۝ إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ ۝ اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ ۝ صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ ۝ آمِين",
            transliteration = "A'udhu billahi minash-shaytanir-rajeem. Bismillahir-Rahmanir-Raheem. Alhamdu lillahi Rabbil-'aalameen. Ar-Rahmanir-Raheem. Maliki Yawmid-Deen. Iyyaka na'budu wa iyyaka nasta'een. Ihdinas-siraatal-mustaqeem. Siraatalladhina an'amta 'alayhim, ghayril-maghdoobi 'alayhim walad-daalleen. Aameen.",
            meaningHindi = "मैं शैतान मर्दूद से अल्लाह की पनाह माँगता हूँ। अल्लाह के नाम से जो निहायत मेहरबान, बेहद रहम वाला है। सब तारीफ़ें अल्लाह ही के लिए हैं जो तमाम जहानों का रब है... ऐ अल्लाह! हमें सीधा रास्ता दिखा... आमीन।",
            meaningUrdu = "میں پناہ مانگتا ہوں اللہ کی شیطان مردود سے۔ اللہ کے نام سے جو نہایت مہربان رحم والا ہے۔ سب تعریف اللہ ہی کے لیے ہے جو سارے جہانوں کا پروردگار ہے... اے اللہ ہمیں سیدھا راستہ دکھا... آمین۔",
            meaningEnglish = "I seek refuge in Allah from Satan, the outcast. In the Name of Allah, the Most Compassionate, Most Merciful. All praise belongs to Allah, Lord of all the worlds... Guide us on the straight path... Aameen.",
            instructionHindi = "सूरह फ़ातिहा हर रकअत का लाज़िमी रुक्न है। इसके बाद क़ुरान की कोई भी सूरत (जैसे सूरह इख़्लास या सूरह कौसर) पढ़ें।",
            instructionEnglish = "Reciting Surah Al-Fatiha is an essential pillar in every Rak'ah. After it, recite another Surah or verses from the Quran.",
            hadithProof = "सहीह बुख़ारी 756 — 'उस शख़्स की नमाज़ नहीं जो सूरह फ़ातिहा न पढ़े।'"
        ),
        SalahStep(
            id = 5,
            stepNameHindi = "5. रुकू (Bowing in Humility)",
            stepNameUrdu = "۵. رکوع (عاجزی کے ساتھ جھکنا)",
            stepNameEnglish = "5. Ruku' (Bowing)",
            arabicText = "سُبْحَانَ رَبِّيَ الْعَظِيمِ",
            transliteration = "Subhana Rabbiyal-'Azeem (x3)",
            meaningHindi = "पाक है मेरा रब जो सबसे बुज़ुर्ग और अज़ीम है। (3 बार)",
            meaningUrdu = "پاک ہے میرا پروردگار جو بڑی عظمت والا ہے۔ (۳ مرتبہ)",
            meaningEnglish = "Glory be to my Lord, the Almighty. (Repeat 3 times)",
            instructionHindi = "'अल्लाहु अकबर' कहते हुए झुकें। दोनों हाथों से घुटनों को पकड़ें। पीठ और सर को एक सीध में सीधा रखें और कम से कम 3 बार तस्बीह पढ़ें।",
            instructionEnglish = "Say 'Allahu Akbar' and bow down. Clasp knees with fingers spread, keep back flat and level with head, and recite the tasbeeh at least 3 times.",
            hadithProof = "सहीह मुस्लिम 772, अबू दाऊद 869"
        ),
        SalahStep(
            id = 6,
            stepNameHindi = "6. क़ौमा (Rising from Ruku')",
            stepNameUrdu = "۶. قومہ (رکوع سے سیدھا کھڑا ہونا)",
            stepNameEnglish = "6. Qauma (Rising Upright)",
            arabicText = "سَمِعَ اللهُ لِمَنْ حَمِدَهُ • رَبَّنَا لَكَ الْحَمْدُ حَمْدًا كَثِيرًا طَيِّبًا مُبَارَكًا فِيهِ",
            transliteration = "Sami'Allahu liman hamidah. Rabbana lakal-hamd, hamdan katheeran tayyiban mubarakan feeh.",
            meaningHindi = "अल्लाह ने उस बंदे की सुन ली जिसने उसकी तारीफ़ की। ऐ हमारे रब! तेरे ही लिए तमाम तारीफ़ें हैं — कसीर, पाकीज़ा और बा-बरकत तारीफ़।",
            meaningUrdu = "سن لی اللہ نے اس کی جس نے اس کی تعریف کی۔ اے ہمارے رب! تیرے ہی لیے تمام تعریفیں ہیں، بہت زیادہ، پاکیزہ اور برکت والی۔",
            meaningEnglish = "Allah hears the one who praises Him. Our Lord, to You belongs all praise, abundant, pure, and full of blessings.",
            instructionHindi = "रुकू से बिल्कुल सीधे खड़े हो जाएं। इत्मीनान से ठहरना (तअदीले अरकान) नमाज़ का वाजिब रुक्न है।",
            instructionEnglish = "Rise up completely straight and motionless. Standing calmly before proceeding to Sajdah is mandatory.",
            hadithProof = "सहीह बुख़ारी 795, 799"
        ),
        SalahStep(
            id = 7,
            stepNameHindi = "7. सजदा (Prostration - Nearest to Allah)",
            stepNameUrdu = "۷. سجدہ (قربِ الٰہی کا مقام)",
            stepNameEnglish = "7. Sujood (Prostration)",
            arabicText = "سُبْحَانَ رَبِّيَ الْأَعْلَىٰ",
            transliteration = "Subhana Rabbiyal-A'laa (x3)",
            meaningHindi = "पाक है मेरा रब जो सबसे आला और बुलंद है। (3 बार)",
            meaningUrdu = "پاک ہے میرا پروردگار جو سب سے بلند و بالا ہے۔ (۳ مرتبہ)",
            meaningEnglish = "Glory be to my Lord, the Most High. (Repeat 3 times)",
            instructionHindi = "'अल्लाहु अकबर' कहते हुए सजदे में जाएँ। 7 अंगों पर सजदा फ़र्ज़ है: पेशानी व नाक, दोनों हथेलियाँ, दोनों घुटने और दोनों पैरों की उँगलियाँ। कम से कम 3 बार तस्बीह पढ़ें।",
            instructionEnglish = "Say 'Allahu Akbar' and prostrate on 7 limbs: forehead with nose, both palms, both knees, and toes curled toward Qibla. Recite 3 times.",
            hadithProof = "सहीह बुख़ारी 812 — 'मुझे 7 हड्डियों पर सजदा करने का हुक्म दिया गया है।' सहीह मुस्लिम 482"
        ),
        SalahStep(
            id = 8,
            stepNameHindi = "8. जलसा (Sitting between Two Sujoods)",
            stepNameUrdu = "۸. جلسہ (دو سجدوں کے درمیان بیٹھنا)",
            stepNameEnglish = "8. Jalsa (Sitting between Sujood)",
            arabicText = "رَبِّ اغْفِرْ لِي، رَبِّ اغْفِرْ لِي، وَارْحَمْنِي، وَاهْدِنِي، وَعَافِنِي، وَارْزُقْنِي",
            transliteration = "Rabbighfir lee, Rabbighfir lee, warhamnee, wahdinee, wa 'aafinee, warzuqnee.",
            meaningHindi = "ऐ मेरे रब! मुझे बख़्श दे, ऐ मेरे रब! मुझे बख़्श दे, मुझ पर रहम फ़रमा, मुझे हिदायत दे, मुझे आफ़ियत बख़्श और मुझे रिज़्क़ अता फ़रमा।",
            meaningUrdu = "اے میرے رب! مجھے بخش دے، اے میرے رب! مجھے بخش دے، مجھ پر رحم فرما، مجھے ہدایت دے، مجھے عافیت دے اور مجھے رزق عطا فرما۔",
            meaningEnglish = "O my Lord! Forgive me, O my Lord! Forgive me, have mercy on me, guide me, grant me health, and provide for me.",
            instructionHindi = "सजदे से सर उठाकर बायाँ पाँव बिछाकर उस पर बैठें और दायाँ पाँव खड़ा रखें। दोनों सजदों के दरमियान सीधा बैठना वाजिब है। फिर दूसरा सजदा करें।",
            instructionEnglish = "Sit calmly on your left foot with the right foot upright. Sitting still between the two prostrations is compulsory, then perform the 2nd Sujood.",
            hadithProof = "सुनन अबू दाऊद 874, सुनन इब्न माजाह 897 (सहीह)"
        ),
        SalahStep(
            id = 9,
            stepNameHindi = "9. तशह्हुद / अत्तहियात (Tashahhud)",
            stepNameUrdu = "۹. تشہد / التحیات",
            stepNameEnglish = "9. Tashahhud (Attahiyyat)",
            arabicText = "التَّحِيَّاتُ لِلّٰهِ وَالصَّلَوَاتُ وَالطَّيِّبَاتُ، السَّلَامُ عَلَيْكَ أَيُّهَا النَّبِيُّ وَرَحْمَةُ اللهِ وَبَرَكَاتُهُ، السَّلَامُ عَلَيْنَا وَعَلَىٰ عِبَادِ اللهِ الصَّالِحِينَ، أَشْهَدُ أَنْ لَا إِلٰهَ إِلَّا اللهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ",
            transliteration = "Attahiyyatu lillahi was-salawatu wat-tayyibat. As-salamu 'alayka ayyuhan-Nabiyyu wa rahmatullahi wa barakatuh. As-salamu 'alayna wa 'ala 'ibadillahis-saliheen. Ash-hadu an laa ilaaha ill-Allahu wa ash-hadu anna Muhammadan 'abduhu wa Rasooluh.",
            meaningHindi = "तमाम ज़बानी, बदनी और माली इबादतें अल्लाह ही के लिए हैं। ऐ नबी! आप पर सलाम हो और अल्लाह की रहमत व बरकतें नाज़िल हों। सलाम हो हम पर और अल्लाह के तमाम नेक बंदों पर। मैं गवाही देता हूँ कि अल्लाह के सिवा कोई माबूद नहीं और हज़रत मुहम्मद ﷺ उसके बंदे और रसूल हैं।",
            meaningUrdu = "تمام زبانی، بدنی اور مالی عبادتیں اللہ ہی کے لیے ہیں۔ اے نبی! آپ پر سلام اور اللہ کی رحمت اور برکتیں ہوں۔ سلام ہو ہم پر اور اللہ کے نیک بندوں پر۔ میں گواہی دیتا ہوں کہ اللہ کے سوا کوئی معبود نہیں اور محمد ﷺ اس کے بندے اور رسول ہیں۔",
            meaningEnglish = "All compliments, prayers and pure words are due to Allah. Peace be upon you, O Prophet, and the mercy of Allah and His blessings. Peace be upon us and upon the righteous servants of Allah. I bear witness that there is no true deity except Allah, and that Muhammad is His servant and Messenger.",
            instructionHindi = "दूसरी व आख़िरी रकअत में क़ादे में बैठें। 'अशहदु अल-ला इलाहा' पर शहादत की उँगली उठाएँ और 'इल्लल्लाह' पर नीचे कर लें।",
            instructionEnglish = "Sit in Qadah. Raise the index finger upon saying 'Ash-hadu an laa ilaaha' testifying Tawheed, then lower it.",
            hadithProof = "सहीह बुख़ारी 831, 1202, सहीह मुस्लिम 402"
        ),
        SalahStep(
            id = 10,
            stepNameHindi = "10. दुरूद-ए-इब्राहीमी (Durood Ibrahim)",
            stepNameUrdu = "۱۰. درودِ ابراہیمی",
            stepNameEnglish = "10. Durood Ibrahim (Salawat)",
            arabicText = "اللّٰهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلَىٰ إِبْرَاهِيمَ وَعَلَىٰ آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ • اللّٰهُمَّ بَارِكْ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ كَمَا بَارَكْتَ عَلَىٰ إِبْرَاهِيمَ وَعَلَىٰ آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ",
            transliteration = "Allahumma salli 'ala Muhammadin wa 'ala aali Muhammadin kama sallayta 'ala Ibrahima wa 'ala aali Ibrahima innaka Hameedum-Majeed. Allahumma barik 'ala Muhammadin wa 'ala aali Muhammadin kama barakta 'ala Ibrahima wa 'ala aali Ibrahima innaka Hameedum-Majeed.",
            meaningHindi = "ऐ अल्लाह! रहमत नाज़िल फ़रमा हज़रत मुहम्मद ﷺ और उनकी आल पर, जैसा कि तूने रहमत नाज़िल फ़रमाई हज़रत इब्राहीम (अलै.) और उनकी आल पर। बेशक तू तारीफ़ के क़ाबिल, बुज़ुर्ग है। ऐ अल्लाह! बरकत नाज़िल फ़रमा हज़रत मुहम्मद ﷺ और उनकी आल पर...",
            meaningUrdu = "اے اللہ! رحمت فرما حضرت محمد ﷺ اور ان کی آل پر جیسا کہ تو نے رحمت فرمائی حضرت ابراہیم علیہ السلام اور ان کی آل پر، بے شک تو تعریف کیا گیا بڑی بزرگی والا ہے۔ اے اللہ! برکت نازل فرما...",
            meaningEnglish = "O Allah, send prayers upon Muhammad and upon the family of Muhammad, as You sent prayers upon Ibrahim and the family of Ibrahim; You are Praiseworthy, Full of Glory...",
            instructionHindi = "आख़िरी रकअत के क़ादे में अत्तहियात के बाद दुरूद-ए-इब्राहीमी पढ़ना सुन्नते मुअक्कदा है।",
            instructionEnglish = "Recite Durood Ibrahim after Tashahhud in the final sitting of every prayer.",
            hadithProof = "सहीह बुख़ारी 3370, सहीह मुस्लिम 406"
        ),
        SalahStep(
            id = 11,
            stepNameHindi = "11. दुआ-ए-मासूरा (Closing Dua)",
            stepNameUrdu = "۱۱. دعائے ماثورہ",
            stepNameEnglish = "11. Dua Masoora (Supplication)",
            arabicText = "اللّٰهُمَّ إِنِّي ظَلَمْتُ نَفْسِي ظُلْمًا كَثِيرًا، وَلَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ، فَاغْفِرْ لِي مَغْفِرَةً مِنْ عِنْدِكَ وَارْحَمْنِي إِنَّكَ أَنْتَ الْغَفُورُ الرَّحِيمُ",
            transliteration = "Allahumma innee zalamtu nafsee zulman katheeran, wa laa yaghfirudh-dhunooba illaa Anta, faghfir lee maghfiratan min 'indika warhamnee innaka Antal-Ghafoorur-Raheem.",
            meaningHindi = "ऐ अल्लाह! बेशक मैंने अपनी जान पर बहुत ज़ुल्म किया है, और तेरे सिवा कोई गुनाहों को मुआफ़ नहीं कर सकता। पस तू अपनी ख़ास बख़्शिश से मुझे बख़्श दे और मुझ पर रहम फ़रमा, बेशक तू बड़ा बख़्शने वाला, निहायत रहम वाला है।",
            meaningUrdu = "اے اللہ! بے شک میں نے اپنی جان پر بہت بڑا ظلم کیا ہے اور تیرے سوا کوئی گناہوں کو نہیں بخش سکتا، پس تو اپنے پاس سے میری مغفرت فرما اور مجھ پر رحم کر، بے شک تو بڑا بخشنے والا نہایت رحم والا ہے۔",
            meaningEnglish = "O Allah, I have wronged myself greatly, and none forgives sins except You, so forgive me with forgiveness from You and have mercy on me. Indeed, You are the Forgiving, the Merciful.",
            instructionHindi = "दुरूद शरीफ़ के बाद यह दुआ या कोई भी क़ुरानी दुआ (जैसे 'रब्बना आतिना फिद-दुनिया...') पढ़ें।",
            instructionEnglish = "After the Salawat, recite this profound Hadith supplication taught by the Prophet ﷺ to Abu Bakr As-Siddiq (R.A).",
            hadithProof = "सहीह बुख़ारी 834, सहीह मुस्लिम 2705"
        ),
        SalahStep(
            id = 12,
            stepNameHindi = "12. सलाम (Tasleem - Completion)",
            stepNameUrdu = "۱۲. سلام (نماز کی تکمیل)",
            stepNameEnglish = "12. Tasleem (Salutation)",
            arabicText = "السَّلَامُ عَلَيْكُمْ وَرَحْمَةُ اللهِ",
            transliteration = "As-salamu 'alaykum wa rahmatullah",
            meaningHindi = "आप पर सलामती हो और अल्लाह की रहमत।",
            meaningUrdu = "تم پر سلامتی ہو اور اللہ کی رحمت۔",
            meaningEnglish = "Peace be upon you and the mercy of Allah.",
            instructionHindi = "पहले दाएँ कंधे की तरफ़ चेहरा फेरते हुए सलाम कहें, फिर बाएँ कंधे की तरफ़ चेहरा फेरते हुए सलाम कहें। इसके साथ नमाज़ मुकम्मल होती है।",
            instructionEnglish = "Turn your face to the right shoulder uttering the Tasleem, then to the left shoulder. This completes the prayer.",
            hadithProof = "सहीह मुस्लिम 582, सुनन अबू दाऊद 996"
        )
    )

    // 5 Daily Obligatory Prayers + Jummah Breakdown
    val RAKATS = listOf(
        PrayerRakatsInfo(
            id = "fajr",
            arabicName = "صَلَاةُ الْفَجْرِ",
            nameHindi = "फज्र (Fajr)",
            nameUrdu = "فجر",
            nameEnglish = "Fajr (Dawn Prayer)",
            timeWindowHindi = "सुबह सादिक़ से सूरज निकलने तक",
            timeWindowEnglish = "From true dawn until just before sunrise",
            sunnahBefore = 2,
            fard = 2,
            sunnahAfter = 0,
            nafl = 0,
            witr = 0,
            totalRakats = 4,
            detailsHindi = "फज्र की 2 रकअत सुन्नत-ए-मौक़दा की फ़ज़ीलत दुनिया और जो कुछ इसमें है उन सब से बेहतर है (सहीह मुस्लिम 725)। इसके बाद 2 रकअत फ़र्ज़ पढ़ी जाती हैं।",
            detailsEnglish = "The 2 Sunnah before Fajr are better than the entire world and everything in it (Sahih Muslim 725), followed by 2 Fard."
        ),
        PrayerRakatsInfo(
            id = "dhuhr",
            arabicName = "صَلَاةُ الظُّهْرِ",
            nameHindi = "ज़ुहर (Dhuhr)",
            nameUrdu = "ظہر",
            nameEnglish = "Dhuhr (Midday Prayer)",
            timeWindowHindi = "सूरज ढलने के बाद से हर चीज़ का साया दुगुना होने तक",
            timeWindowEnglish = "From solar noon until objects shadow equals length",
            sunnahBefore = 4,
            fard = 4,
            sunnahAfter = 2,
            nafl = 2,
            witr = 0,
            totalRakats = 12,
            detailsHindi = "4 सुन्नत-ए-मौक़दा + 4 फ़र्ज़ + 2 सुन्नत-ए-मौक़दा + 2 नफ़्ल। ज़ुहर के वक़्त आसमान के दरवाज़े खोले जाते हैं (तिर्मिज़ी 478)।",
            detailsEnglish = "4 Sunnah Mu'akkadah + 4 Fard + 2 Sunnah Mu'akkadah + 2 Nafl = 12 Rak'ahs total."
        ),
        PrayerRakatsInfo(
            id = "asr",
            arabicName = "صَلَاةُ الْعَصْرِ",
            nameHindi = "अस्र (Asr)",
            nameUrdu = "عصر",
            nameEnglish = "Asr (Late Afternoon)",
            timeWindowHindi = "ज़ुहर का वक़्त ख़त्म होने से सूरज ज़र्द होने तक",
            timeWindowEnglish = "From mid-afternoon until the sun begins to set",
            sunnahBefore = 4,
            fard = 4,
            sunnahAfter = 0,
            nafl = 0,
            witr = 0,
            totalRakats = 8,
            detailsHindi = "4 सुन्नत (ग़ैर-मौक़दा) + 4 फ़र्ज़। अस्र को 'सलातुल वुस्ता' (दरमियानी नमाज़) कहा गया है। इसे छोड़ने पर आमाल बर्बाद हो जाते हैं (बुख़ारी 553)।",
            detailsEnglish = "4 Sunnah (Ghair-Mu'akkadah) + 4 Fard. Mentioned as the 'Middle Prayer' in Surah Al-Baqarah 2:238."
        ),
        PrayerRakatsInfo(
            id = "maghrib",
            arabicName = "صَلَاةُ الْمَغْرِبِ",
            nameHindi = "मग़रिब (Maghrib)",
            nameUrdu = "مغرب",
            nameEnglish = "Maghrib (Sunset Prayer)",
            timeWindowHindi = "सूरज डूबने के तुरंत बाद से शफ़क़ (सुर्ख़ी) ग़ायब होने तक",
            timeWindowEnglish = "Immediately after sunset until the twilight disappears",
            sunnahBefore = 0,
            fard = 3,
            sunnahAfter = 2,
            nafl = 2,
            witr = 0,
            totalRakats = 7,
            detailsHindi = "3 फ़र्ज़ + 2 सुन्नत-ए-मौक़दा + 2 नफ़्ल। मग़रिब की नमाज़ में जल्दी करना मुस्तहब है।",
            detailsEnglish = "3 Fard + 2 Sunnah Mu'akkadah + 2 Nafl = 7 Rak'ahs total."
        ),
        PrayerRakatsInfo(
            id = "isha",
            arabicName = "صَلَاةُ الْعِشَاءِ",
            nameHindi = "इशा (Isha)",
            nameUrdu = "عشاء",
            nameEnglish = "Isha (Night Prayer)",
            timeWindowHindi = "शफ़क़ (सुर्ख़ी/सफ़ेदी) डूबने से आधी रात / सुबह सादिक़ तक",
            timeWindowEnglish = "From complete darkness until true dawn (best before midnight)",
            sunnahBefore = 4,
            fard = 4,
            sunnahAfter = 2,
            nafl = 2,
            witr = 3,
            totalRakats = 17,
            detailsHindi = "4 सुन्नत + 4 फ़र्ज़ + 2 सुन्नत + 2 नफ़्ल + 3 वित्र वाजिब + 2 नफ़्ल। जमाअत से इशा पढ़ने वाले को आधी रात इबादत का सवाब मिलता है (मुस्लिम 656)।",
            detailsEnglish = "4 Sunnah + 4 Fard + 2 Sunnah + 2 Nafl + 3 Witr Wajib + 2 Nafl = 17 Rak'ahs total."
        ),
        PrayerRakatsInfo(
            id = "jummah",
            arabicName = "صَلَاةُ الْجُمُعَةِ",
            nameHindi = "जुमा (Jummah)",
            nameUrdu = "جمعہ",
            nameEnglish = "Jummah (Friday Congregation)",
            timeWindowHindi = "ज़ुहर के वक़्त में जुमा की ख़ास जमाअत",
            timeWindowEnglish = "Offered in congregation during Dhuhr time on Friday",
            sunnahBefore = 4,
            fard = 2,
            sunnahAfter = 4,
            nafl = 2,
            witr = 0,
            totalRakats = 14,
            detailsHindi = "4 सुन्नत + 2 फ़र्ज़ (ख़ुत्बा के साथ) + 4 सुन्नत + 2 सुन्नत + 2 नफ़्ल। जुमा का दिन हफ़्ते का सरदार है और ख़ास इबादत का दिन है।",
            detailsEnglish = "4 Sunnah + 2 Fard (with Khutbah) + 4 Sunnah + 2 Sunnah + 2 Nafl = 14 Rak'ahs total."
        )
    )

    // 9 Shuroot (Prerequisites before prayer) & 14 Arkaan (Essential Pillars within prayer)
    val CONDITIONS_AND_PILLARS = listOf(
        SalahPillarCondition(
            number = 1,
            type = "SHART (शर्त)",
            arabicTitle = "الطَّهَارَةُ مِنَ الْحَدَثِ",
            titleHindi = "1. तहारत (वज़ू व ग़ुस्ल से पाकी)",
            titleUrdu = "۱. طہارت (وضو اور غسل سے پاکیزگی)",
            titleEnglish = "1. Purity from Impurities (Taharah)",
            explanationHindi = "छोटे हदस से वज़ू और बड़े हदस से ग़ुस्ल करके मुकम्मल पाकी हासिल करना। बिना पाकी के नमाज़ क़बूल नहीं होती।",
            explanationEnglish = "Purification from ritual impurity via Wudu or Ghusl.",
            proofReference = "सहीह मुस्लिम 224 — 'पाकी के बग़ैर नमाज़ क़बूल नहीं होती।'"
        ),
        SalahPillarCondition(
            number = 2,
            type = "SHART (शर्त)",
            arabicTitle = "طَهَارَةُ الثَّوْبِ وَالْمَكَانِ",
            titleHindi = "2. बदन, कपड़े व जगह की पाकी",
            titleUrdu = "۲. لباس، بدن اور جگہ کی پاکی",
            titleEnglish = "2. Cleanliness of Body, Clothes & Place",
            explanationHindi = "नमाज़ी का बदन, उसका लिबास और जिस जगह पर नमाज़ पढ़ी जा रही है, वह किसी भी नजासत (गंदगी) से पाक हो।",
            explanationEnglish = "The physical body, clothes, and surface of prayer must be free from filth.",
            proofReference = "सूरह अल-मुद्दस्सिर 74:4 — 'और अपने कपड़ों को पाक रखिए।'"
        ),
        SalahPillarCondition(
            number = 3,
            type = "SHART (शर्त)",
            arabicTitle = "سَتْرُ الْعَوْرَةِ",
            titleHindi = "3. सतर ढांकना (Covering Awrah)",
            titleUrdu = "۳. ستر پوشی",
            titleEnglish = "3. Covering the Awrah",
            explanationHindi = "मर्द के लिए नाफ़ से घुटने तक और औरत के लिए चेहरे, हथेलियों व पैरों के पंजों को छोड़कर पूरा जिस्म छुपाना फ़र्ज़ है।",
            explanationEnglish = "Covering the mandatory private parts of the body.",
            proofReference = "सूरह अल-अराफ़ 7:31 — 'हर नमाज़ के वक़्त अपनी ज़ीनत (लिबास) ले लिया करो।'"
        ),
        SalahPillarCondition(
            number = 4,
            type = "SHART (शर्त)",
            arabicTitle = "اسْتِقْبَالُ الْقِبْلَةِ",
            titleHindi = "4. क़िब्ला-रुख़ होना (Facing Qibla)",
            titleUrdu = "۴. قبلہ رو ہونا",
            titleEnglish = "4. Facing the Sacred Kaaba (Qiblah)",
            explanationHindi = "मक्का मुकर्रमा में ख़ाना-ए-काबा की सिम्त चेहरा करके नमाज़ अदा करना।",
            explanationEnglish = "Facing in the direction of the Holy Ka'bah in Makkah.",
            proofReference = "सूरह अल-बक़रह 2:144 — 'पस अपना चेहरा मस्जिदे हराम की तरफ़ फेर लीजिए।'"
        ),
        SalahPillarCondition(
            number = 5,
            type = "SHART (शर्त)",
            arabicTitle = "دُخُولُ الْوَقْتِ",
            titleHindi = "5. वक़्त का दाख़िल होना",
            titleUrdu = "۵. وقت کا داخل ہونا",
            titleEnglish = "5. Entering of the Prescribed Time",
            explanationHindi = "हर नमाज़ को उसके मुक़र्रर वक़्त के अंदर ही पढ़ना ज़रूरी है। वक़्त से पहले नमाज़ नहीं होती।",
            explanationEnglish = "Praying strictly within the designated time boundaries.",
            proofReference = "सूरह अन-निसा 4:103 — 'बेशक नमाज़ मोमिनों पर मुक़र्ररा वक़्तों पर फ़र्ज़ है।'"
        ),
        SalahPillarCondition(
            number = 6,
            type = "SHART (शर्त)",
            arabicTitle = "النِّيَّةُ",
            titleHindi = "6. नीयत (Intention)",
            titleUrdu = "۶. نیت خالص",
            titleEnglish = "6. Sincere Intention (Niyyah)",
            explanationHindi = "दिल में यह पुख़्ता इरादा करना कि मैं किस वक़्त की और कितनी फ़र्ज़/सुन्नत नमाज़ अल्लाह के लिए पढ़ रहा हूँ।",
            explanationEnglish = "Having conscious intention in the heart for the specific prayer.",
            proofReference = "सहीह बुख़ारी 1 — 'आमाल का दारोमदार नीयत पर है।'"
        ),
        SalahPillarCondition(
            number = 7,
            type = "RUKN (रुक्न)",
            arabicTitle = "تَكْبِيرَةُ الْإِحْرَامِ",
            titleHindi = "7. तकबीर-ए-तहरीमा (First Takbeer)",
            titleUrdu = "۷. تکبیر تحریمہ",
            titleEnglish = "7. Takbeerat al-Ihram",
            explanationHindi = "नमाज़ की शुरुआत में ज़बान से 'अल्लाहु अकबर' कहना जिससे नमाज़ के अलावा तमाम दुनियावी काम हराम हो जाते हैं।",
            explanationEnglish = "Saying 'Allahu Akbar' to enter the state of prayer.",
            proofReference = "सुनन अबू दाऊद 61 — 'नमाज़ की पाबंदी (तहरीम) तकबीर है।'"
        ),
        SalahPillarCondition(
            number = 8,
            type = "RUKN (रुक्न)",
            arabicTitle = "الْقِيَامُ مَعَ الْقُدْرَةِ",
            titleHindi = "8. क़ियाम (ताक़त होने पर सीधा खड़ा होना)",
            titleUrdu = "۸. قیام (سیدھا کھڑا ہونا)",
            titleEnglish = "8. Qiyam (Standing Upright)",
            explanationHindi = "फ़र्ज़ नमाज़ों में अगर जिस्मानी ताक़त हो तो सीधा खड़ा होना फ़र्ज़ है। मजबूरी में बैठकर पढ़ने की इजाज़त है।",
            explanationEnglish = "Standing upright during obligatory prayers if physically able.",
            proofReference = "सूरह अल-बक़रह 2:238 — 'और अल्लाह के सामने बा-अदब सीधे खड़े रहो।'"
        ),
        SalahPillarCondition(
            number = 9,
            type = "RUKN (रुक्न)",
            arabicTitle = "قِرَاءَةُ الْفَاتِحَةِ",
            titleHindi = "9. सूरह फ़ातिहा की तिलावत",
            titleUrdu = "۹. قرأت سورۃ الفاتحہ",
            titleEnglish = "9. Reciting Surah Al-Fatiha",
            explanationHindi = "हर रकअत में सूरह फ़ातिहा पढ़ना नमाज़ का बुनियादी रुक्न है।",
            explanationEnglish = "Reciting the Opening Chapter of the Quran in every Rak'ah.",
            proofReference = "सहीह बुख़ारी 756 — 'उसकी नमाज़ नहीं जिसने सूरह फ़ातिहा नहीं पढ़ी।'"
        ),
        SalahPillarCondition(
            number = 10,
            type = "RUKN (रुक्न)",
            arabicTitle = "الرُّكُوعُ وَالطُّمَأْنِينَةُ",
            titleHindi = "10. रुकू और उसमें इत्मीनान",
            titleUrdu = "۱۰. رکوع اور اس میں اطمینان",
            titleEnglish = "10. Ruku' with Serenity",
            explanationHindi = "अल्लाह की अज़मत के सामने झुकना और कम से कम एक तस्बीह की मिक़दार तक सुकून व इत्मीनान से ठहरना।",
            explanationEnglish = "Bowing with humility and resting at peace in that position.",
            proofReference = "सूरह अल-हज्ज 22:77 — 'ऐ ईमान वालो! रुकू करो और सजदा करो।'"
        ),
        SalahPillarCondition(
            number = 11,
            type = "RUKN (रुक्न)",
            arabicTitle = "السُّجُودُ عَلَى الأَعْضَاءِ السَّبْعَةِ",
            titleHindi = "11. सात अंगों पर सजदा",
            titleUrdu = "۱۱. سات اعضاء پر سجدہ",
            titleEnglish = "11. Sujood on Seven Limbs",
            explanationHindi = "हर रकअत में दो बार सजदा करना। पेशानी, नाक, दोनों हथेलियाँ, दोनों घुटने और पैरों के पंजे ज़मीन पर टिकना ज़रूरी हैं।",
            explanationEnglish = "Prostrating twice per Rak'ah on the 7 designated bodily parts.",
            proofReference = "सहीह बुख़ारी 812"
        ),
        SalahPillarCondition(
            number = 12,
            type = "RUKN (रुक्न)",
            arabicTitle = "الْقَعْدَةُ الأَخِيرَةُ وَالتَّشَهُّدُ",
            titleHindi = "12. आख़िरी क़ादा व तशह्हुद",
            titleUrdu = "۱۲. قعدہ اخیرہ اور تشہد",
            titleEnglish = "12. Final Sitting & Tashahhud",
            explanationHindi = "नमाज़ के आख़िर में अत्तहियात पढ़ने की मिक़दार बैठना और तशह्हुद अदा करना।",
            explanationEnglish = "Sitting for the duration of Tashahhud at the end of prayer.",
            proofReference = "सहीह बुख़ारी 831, सहीह मुस्लिम 402"
        ),
        SalahPillarCondition(
            number = 13,
            type = "RUKN (रुक्न)",
            arabicTitle = "التَّسْلِيمُ",
            titleHindi = "13. तसलीम (सलाम फेरना)",
            titleUrdu = "۱۳. سلام پھیرنا",
            titleEnglish = "13. Tasleem (Ending Salutations)",
            explanationHindi = "नमाज़ से बाहर निकलने के लिए 'अस्सलाम अलैकुम व रहमतुल्लाह' कहना।",
            explanationEnglish = "Uttering the peace salutations to exit prayer.",
            proofReference = "सुनन अबू दाऊद 61 — 'और इसका हलाल करना सलाम फेरना है।'"
        ),
        SalahPillarCondition(
            number = 14,
            type = "RUKN (रुक्न)",
            arabicTitle = "التَّرْتِيبُ وَالتَّعْدِيلُ",
            titleHindi = "14. तरतीब व तअदील (ठहराव)",
            titleUrdu = "۱۴. ترتیب اور اعتدال",
            titleEnglish = "14. Sequence & Proper Posture",
            explanationHindi = "तमाम अरकान को उसी तरतीब से अदा करना जैसा रसूलुल्लाह ﷺ ने सिखाया, और हर रुक्न में इत्मीनान रखना।",
            explanationEnglish = "Performing all steps in correct sequence with stillness.",
            proofReference = "सहीह बुख़ारी 631 — 'नमाज़ वैसे पढ़ो जैसे मुझे पढ़ते देखते हो।'"
        )
    )

    // Authentic Hadith Virtues of Salah
    val VIRTUES = listOf(
        SalahHadithVirtue(
            arabicText = "الصَّلَاةُ عِمَادُ الدِّينِ، فَمَنْ أَقَامَهَا فَقَدْ أَقَامَ الدِّينَ، وَمَنْ هَدَمَهَا فَقَدْ هَدَمَ الدِّينَ",
            source = "शुअबुल ईमान लिल-बैहक़ी (हदीस 2550)",
            topicHindi = "नमाज़ दीन का सुतून (Pillar of Faith) है",
            topicEnglish = "Salah is the Pillar of Religion",
            translationHindi = "'नमाज़ दीन का सुतून (खंभा) है। जिसने इसे क़ायम रखा उसने दीन को क़ायम रखा, और जिसने इसे गिरा दिया उसने दीन को गिरा दिया।'",
            translationUrdu = "'نماز دین کا ستون ہے۔ جس نے اسے قائم کیا اس نے دین کو قائم کیا، اور جس نے اسے گرا دیا اس نے دین کو ڈھا دیا۔'",
            translationEnglish = "'Salah is the pillar of religion; whoever establishes it has established the religion, and whoever abandons it has demolished the religion.'"
        ),
        SalahHadithVirtue(
            arabicText = "أَرَأَيْتُمْ لَوْ أَنَّ نَهْرًا بِبَابِ أَحَدِكُمْ يَغْتَسِلُ مِنْهُ كُلَّ يَوْمٍ خَمْسَ مَرَّاتٍ، هَلْ يَبْقَىٰ مِنْ دَرَنِهِ شَيْءٌ؟ قَالُوا: لَا. قَالَ: فَذَٰلِكَ مَثَلُ الصَّلَوَاتِ الْخَمْسِ، يَمْحُو اللهُ بِهِنَّ الْخَطَايَا",
            source = "सहीह बुख़ारी (हदीस 528) व सहीह मुस्लिम (667)",
            topicHindi = "पाँचों नमाज़ें गुनाहों को धो देती हैं",
            topicEnglish = "The Cleansing River of 5 Prayers",
            translationHindi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'भला बताओ अगर किसी के दरवाज़े पर एक नहर बहती हो जिसमें वह रोज़ाना पाँच बार नहाए, तो क्या उसके बदन पर कुछ मैल बाक़ी रहेगा?' सहाबा ने अर्ज़ किया: 'नहीं।' आप ﷺ ने फ़रमाया: 'यही मिसाल पाँचों नमाज़ों की है, अल्लाह उनके ज़रिए गुनाहों को मिटा देता है।'",
            translationUrdu = "رسول اللہ ﷺ نے فرمایا: 'بھلا بتاؤ اگر تم میں سے کسی کے دروازے پر نہر بہتی ہو اور وہ اس میں روزانہ پانچ بار نہائے تو کیا اس کے جسم پر کوئی میل باقی رہے گا؟' صحابہ نے عرض کیا: 'نہیں۔' فرمایا: 'یہی مثال پانچوں نمازوں کی ہے، اللہ تعالیٰ ان کے ذریعے گناہوں کو مٹا دیتا ہے۔'",
            translationEnglish = "The Prophet ﷺ said: 'Consider if there were a river at your door and you bathed in it five times a day, would any dirt remain?' They said, 'None.' He said: 'That is the parable of the five daily prayers; Allah wipes away sins with them.'"
        ),
        SalahHadithVirtue(
            arabicText = "إِنَّ أَوَّلَ مَا يُحَاسَبُ بِهِ الْعَبْدُ يَوْمَ الْقِيَامَةِ مِنْ عَمَلِهِ صَلَاتُهُ، فَإِنْ صَلُحَتْ فَقَدْ أَفْلَحَ وَأَنْجَحَ، وَإِنْ فَسَدَتْ فَقَدْ خَابَ وَخَسِرَ",
            source = "जामे तिर्मिज़ी (हदीस 413) व सुनन अबू दाऊद (864) — सहीह",
            topicHindi = "क़ियामत के दिन सबसे पहला हिसाब",
            topicEnglish = "The First Deed Accounted on Judgement Day",
            translationHindi = "'क़ियामत के दिन बंदे के आमाल में सबसे पहले नमाज़ का हिसाब लिया जाएगा। अगर नमाज़ ठीक निकली तो वह कामयाब और मुराद को पहुँच गया, और अगर नमाज़ ख़राब निकली तो वह नाकाम और घाटे में रहा।'",
            translationUrdu = "'قیامت کے دن بندے کے اعمال میں سب سے پہلے نماز کا حساب لیا جائے گا۔ اگر نماز درست نکلی تو وہ کامیاب ہوا اور فلاح پا گیا، اور اگر نماز بگڑ گئی تو وہ ناکام اور خسارے میں رہا۔'",
            translationEnglish = "'The first matter that the slave will be brought to account for on the Day of Judgment is the prayer. If it is sound, he has triumphed and succeeded; if it is corrupt, he has failed and lost.'"
        ),
        SalahHadithVirtue(
            arabicText = "بَيْنَ الرَّجُلِ وَبَيْنَ الشِّرْكِ وَالْكُفْرِ تَرْكُ الصَّلَاةِ",
            source = "सहीह मुस्लिम (हदीस 82)",
            topicHindi = "ईमान और कुफ़्र के दरमियान फ़र्क़",
            topicEnglish = "The Distinction between Faith and Disbelief",
            translationHindi = "'बंदे और शिर्क व कुफ़्र के दरमियान फ़र्क़ नमाज़ का छोड़ना है।'",
            translationUrdu = "'آدمی اور شرک و کفر کے درمیان حدِ فاصل نماز چھوڑنا ہے۔'",
            translationEnglish = "'Between a man and polytheism and disbelief is the abandonment of prayer.'"
        ),
        SalahHadithVirtue(
            arabicText = "وَجُعِلَتْ قُرَّةُ عَيْنِي فِي الصَّلَاةِ",
            source = "सुनन अन-नसाई (हदीस 3939) व अहमद — सहीह",
            topicHindi = "नबी-ए-करीम ﷺ की आँखों की ठंडक",
            topicEnglish = "The Coolness of the Prophet's Eyes",
            translationHindi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'और मेरी आँखों की ठंडक नमाज़ में रखी गई है।'",
            translationUrdu = "'اور میری آنکھوں کی ٹھنڈک نماز میں بنائی گئی ہے۔'",
            translationEnglish = "'And the coolness of my eyes has been placed in prayer.'"
        )
    )
}
