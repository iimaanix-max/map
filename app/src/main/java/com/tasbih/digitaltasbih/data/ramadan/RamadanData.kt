package com.tasbih.digitaltasbih.data.ramadan

data class RamadanDua(
    val title: String,
    val arabic: String,
    val transliteration: String,
    val translationEn: String,
    val translationHi: String,
    val translationUr: String,
    val reference: String
)

data class RamadanDayPlan(
    val dayNumber: Int,
    val ashraNumber: Int, // 1: Rehmat (1-10), 2: Maghfirat (11-20), 3: Najat (21-30)
    val duaTitle: String,
    val arabicDua: String,
    val transliteration: String,
    val translationEn: String,
    val translationHi: String,
    val translationUr: String,
    val sunnahDeedEn: String,
    val sunnahDeedHi: String,
    val sunnahDeedUr: String,
    val quranGemAyah: String,
    val quranGemRef: String
)

data class RamadanFiqhItem(
    val questionEn: String,
    val questionHi: String,
    val questionUr: String,
    val rulingType: String, // "Allowed", "Invalidates Fast", "Makruh / Caution"
    val answerEn: String,
    val answerHi: String,
    val answerUr: String,
    val reference: String
)

data class CityLeaderboardItem(
    val rank: Int,
    val userName: String,
    val cityName: String,
    val dhikrCount: Long,
    val isCurrentUser: Boolean = false
)

object RamadanData {

    // ── 3 Ashra Duas ──
    val ashra1Dua = RamadanDua(
        title = "1st Ashra of Mercy (Days 1 - 10)",
        arabic = "رَبِّ اغْفِرْ وَارْحَمْ وَأَنْتَ خَيْرُ الرَّاحِمِينَ",
        transliteration = "Rabbi-ghfir warham wa Anta Khayrur-Raahimeen",
        translationEn = "O my Lord! Forgive and have mercy, for You are the Best of those who show mercy.",
        translationHi = "ऐ मेरे रब! मुझे बख्श दे और रहम फ़रमा, तू सब रहम करने वालों से बेहतर है।",
        translationUr = "اے میرے رب! بخش دے اور رحم فرما، تو سب رحم کرنے والوں سے بہتر ہے۔",
        reference = "Surah Al-Mu'minun (23:118)"
    )

    val ashra2Dua = RamadanDua(
        title = "2nd Ashra of Forgiveness (Days 11 - 20)",
        arabic = "أَسْتَغْفِرُ اللَّهَ رَبِّي مِنْ كُلِّ ذَنْبٍ وَأَتُوبُ إِلَيْهِ",
        transliteration = "Astaghfirullaha Rabbi min kulli zambinw-wa atoobu ilayh",
        translationEn = "I seek forgiveness from Allah, my Lord, from every sin and I turn to Him in repentance.",
        translationHi = "मैं अल्लाह से अपने तमाम गुनाहों की माफ़ी मांगता हूँ जो मेरा रब है, और उसी की तरफ रुजू करता हूँ।",
        translationUr = "میں اللہ سے اپنے تمام گناہوں کی معافی مانگتا ہوں جو میرا رب ہے، اور اسی کی طرف رجوع کرتا ہوں۔",
        reference = "Authentic Sunnah Istighfar"
    )

    val ashra3Dua = RamadanDua(
        title = "3rd Ashra & Laylatul Qadr (Days 21 - 30)",
        arabic = "اللَّهُمَّ إِنَّكَ عَفُوٌّ تُحِبُّ الْعَفْوَ فَاعْفُ عَنِّي",
        transliteration = "Allahumma innaka 'Afuwwun tuhibbul-'afwa fa'fu 'anni",
        translationEn = "O Allah, You are Most Forgiving, and You love forgiveness, so forgive me.",
        translationHi = "ऐ अल्लाह! तू बहुत माफ़ करने वाला है और माफ़ी को पसंद फ़रमाता है, पस मुझे माफ़ फ़रमा दे।",
        translationUr = "اے اللہ! تو بہت معاف کرنے والا ہے اور معافی کو پسند فرماتا ہے، پس مجھے معاف فرما دے۔",
        reference = "Jami` at-Tirmidhi 3513 (Authentic)"
    )

    // ── Sehri & Iftar Duas ──
    val sehriDua = RamadanDua(
        title = "Dua for Starting the Fast (Suhoor / Sehri)",
        arabic = "وَبِصَوْمِ غَدٍ نَوَيْتُ مِنْ شَهْرِ رَمَضَانَ",
        transliteration = "Wa bi-sawmi ghadin nawaytu min shahri Ramadan",
        translationEn = "I intend to keep the fast tomorrow for the month of Ramadan.",
        translationHi = "और मैंने रमज़ान के महीने के कल के रोज़े की नीयत की।",
        translationUr = "اور میں نے ماہِ رمضان کے کل کے روزے کی نیت کی۔",
        reference = "Traditional Intention (Niyyah)"
    )

    val iftarSunnahDua = RamadanDua(
        title = "Authentic Sunnah Dua for Breaking Fast (Iftar)",
        arabic = "ذَهَبَ الظَّمَأُ وَابْتَلَّتِ الْعُرُوقُ وَثَبَتَ الْأَجْرُ إِنْ شَاءَ اللَّهُ",
        transliteration = "Dhahaba az-zama'u wabtallatil-'urooqu wa thabatal-ajru in sha' Allah",
        translationEn = "The thirst is gone, the veins are moistened, and the reward is confirmed, if Allah wills.",
        translationHi = "प्यास बुझ गई, नसें तर हो गईं और अल्लाहतआला ने चाहा तो सवाब साबित हो गया।",
        translationUr = "پیاس بجھ گئی، رگیں تر ہو گئیں اور اللہ نے چاہا تو ثواب پکا ہو گیا۔",
        reference = "Sunan Abi Dawood 2357 (Hasan / Sound)"
    )

    val iftarPopularDua = RamadanDua(
        title = "Popular Dua for Iftar",
        arabic = "اللَّهُمَّ إِنِّي لَكَ صُمْتُ وَبِكَ آمَنْتُ وَعَلَيْكَ تَوَكَّلْتُ وَعَلَى رِزْقِكَ أَفْطَرْتُ",
        transliteration = "Allahumma inni laka sumtu wa bika aamantu wa 'alayka tawakkaltu wa 'ala rizqika aftartu",
        translationEn = "O Allah! I fasted for You and I believe in You and I put my trust in You and I break my fast with Your sustenance.",
        translationHi = "ऐ अल्लाह! मैंने तेरे ही लिए रोज़ा रखा, और तुझ पर ईमान लाया, और तेरे दिए हुए रिज़्क़ से इफ़्तार किया।",
        translationUr = "اے اللہ! میں نے تیرے ہی لیے روزہ رکھا، اور تجھ پر ایمان لایا، اور تیرے دیے ہوئے رزق سے افطار کیا۔",
        reference = "Sunan Abi Dawood 2358"
    )

    // ── 30-Day Ramadan Journey ──
    val days30List: List<RamadanDayPlan> = listOf(
        RamadanDayPlan(
            dayNumber = 1,
            ashraNumber = 1,
            duaTitle = "Day 1: Welcoming Ramadan & Seeking Mercy",
            arabicDua = "اللَّهُمَّ سَلِّمْنِي لِرَمَضَانَ وَسَلِّمْ رَمَضَانَ لِي وَتَسَلَّمْهُ مِنِّي مُتَقَبَّلاً",
            transliteration = "Allahumma sallimni li-Ramadana wa sallim Ramadana li wa tasallamhu minni mutaqabbala",
            translationEn = "O Allah, preserve me for Ramadan, safeguard Ramadan for me, and accept it from me.",
            translationHi = "ऐ अल्लाह! मुझे रमज़ान के लिए सलामत रख, रमज़ान को मेरे लिए सलामत रख, और इसे मेरी तरफ से क़ुबूल फ़रमा।",
            translationUr = "اے اللہ! مجھے رمضان کے لیے سلامت رکھ، رمضان کو میرے لیے سلامت رکھ، اور اسے میری طرف سے قبول فرما۔",
            sunnahDeedEn = "Make sincere Tawbah (repentance) and set your Ramadan spiritual goals.",
            sunnahDeedHi = "सच्चे दिल से तौबा करें और अपने रमज़ान के नेक मक़ासिद तय करें।",
            sunnahDeedUr = "سچے دل سے توبہ کریں اور اپنے رمضان کے نیک مقاصد طے کریں۔",
            quranGemAyah = "شَهْرُ رَمَضَانَ الَّذِي أُنزِلَ فِيهِ الْقُرْآنُ هُدًى لِّلنَّاسِ",
            quranGemRef = "Surah Al-Baqarah (2:185)"
        ),
        RamadanDayPlan(
            dayNumber = 2,
            ashraNumber = 1,
            duaTitle = "Day 2: Strengthening Closeness to Allah",
            arabicDua = "اللَّهُمَّ قَرِّبْنِي فِيهِ إِلَى مَرْضَاتِكَ وَجَنِّبْنِي فِيهِ مِنْ سَخَطِكَ وَنَقِمَاتِكَ",
            transliteration = "Allahumma qarribni feehi ila mardaatika wa jannibni feehi min sakhatika wa naqimaatik",
            translationEn = "O Allah, bring me closer to Your pleasure and distance me from Your anger and wrath.",
            translationHi = "ऐ अल्लाह! मुझे अपनी रज़ा के क़रीब कर दे और अपने ग़ज़ब और नाराज़गी से दूर रख।",
            translationUr = "اے اللہ! مجھے اپنی رضا کے قریب کر دے اور اپنے غضب اور ناراضگی سے دور رکھ۔",
            sunnahDeedEn = "Feed a fasting person with even a single date or sip of water for immense reward.",
            sunnahDeedHi = "किसी रोज़ेदार को सिर्फ एक खजूर या पानी का घूंट पिलाकर अज़ीम सवाब पाएं।",
            sunnahDeedUr = "کسی روزہ دار کو صرف ایک کھجور یا پانی کا گھونٹ پلا کر عظیم ثواب پائیں۔",
            quranGemAyah = "وَإِذَا سَأَلَكَ عِبَادِي عَنِّي فَإِنِّي قَرِيبٌ ۖ أُجِيبُ دَعْوَةَ الدَّاعِ إِذَا دَعَانِ",
            quranGemRef = "Surah Al-Baqarah (2:186)"
        ),
        RamadanDayPlan(
            dayNumber = 3,
            ashraNumber = 1,
            duaTitle = "Day 3: Seeking Wisdom & Right Guidance",
            arabicDua = "اللَّهُمَّ ارْزُقْنِي فِيهِ الذِّهْنَ وَالتَّنْبِيهَ وَبَاعِدْنِي فِيهِ مِنَ السَّفَاهَةِ وَالتَّمْوِيهِ",
            transliteration = "Allahummar-zuqni feehi-z-zihna wat-tanbeeh, wa baa'idni feehi minas-safahaati wat-tamweeh",
            translationEn = "O Allah, grant me wisdom and awareness, and distance me from foolishness and falsehood.",
            translationHi = "ऐ अल्लाह! मुझे समझदारी और बेदारी अता फ़रमा और बेवकूफ़ी और ग़फ़लत से बचा।",
            translationUr = "اے اللہ! مجھے سمجھداری اور بیداری عطا فرما اور بیوقوفی اور غفلت سے بچا۔",
            sunnahDeedEn = "Protect your tongue from backbiting, arguing, and unnecessary talk while fasting.",
            sunnahDeedHi = "रोज़े के दौरान अपनी ज़बान को ग़ीबत, बहस और फ़ज़ूल बातों से महफ़ूज़ रखें।",
            sunnahDeedUr = "روزے کے دوران اپنی زبان کو غیبت، بحث اور فضول باتوں سے محفوظ رکھیں۔",
            quranGemAyah = "يَا أَيُّهَا الَّذِينَ آمَنُوا كُتِبَ عَلَيْكُمُ الصِّيَامُ كَمَا كُتِبَ عَلَى الَّذِينَ مِن قَبْلِكُمْ لَعَلَّكُمْ تَتَّقُونَ",
            quranGemRef = "Surah Al-Baqarah (2:183)"
        ),
        RamadanDayPlan(
            dayNumber = 4,
            ashraNumber = 1,
            duaTitle = "Day 4: Sweetness of Dhikr",
            arabicDua = "اللَّهُمَّ قَوِّنِي فِيهِ عَلَى إِقَامَةِ أَمْرِكَ وَأَذِقْنِي فِيهِ حَلاوَةَ ذِكْرِكَ",
            transliteration = "Allahumma qawwini feehi 'ala iqaamati amrik, wa aziqni feehi halaawata zikrik",
            translationEn = "O Allah, grant me strength to uphold Your commands, and let me taste the sweetness of Your remembrance.",
            translationHi = "ऐ अल्लाह! मुझे अपने हुक्म पर क़ायम रहने की ताक़त दे और अपने ज़िक्र की मिठास चखा।",
            translationUr = "اے اللہ! مجھے اپنے حکم پر قائم رہنے کی طاقت دے اور اپنے ذکر کی مٹھاس چکھا۔",
            sunnahDeedEn = "Perform Salah with complete Khushu (presence of mind) without rushing.",
            sunnahDeedHi = "नमाज़ को पूरे ख़ुशू-ओ-ख़ुज़ू के साथ अदा करें, बिल्कुल जल्दबाज़ी न करें।",
            sunnahDeedUr = "نماز کو پورے خشوع و خضوع کے ساتھ ادا کریں، بالکل جلد بازی نہ کریں۔",
            quranGemAyah = "أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ",
            quranGemRef = "Surah Ar-Ra'd (13:28)"
        ),
        RamadanDayPlan(
            dayNumber = 5,
            ashraNumber = 1,
            duaTitle = "Day 5: Count Me Among the Righteous",
            arabicDua = "اللَّهُمَّ اجْعَلْنِي فِيهِ مِنَ الْمُسْتَغْفِرِينَ وَاجْعَلْنِي فِيهِ مِنْ عِبَادِكَ الصَّالِحِينَ",
            transliteration = "Allahummaj-'alnee feehi minal mustaghfireen, waj-'alnee feehi min 'ibaadikas-saaliheen",
            translationEn = "O Allah, make me in this month among those who constantly seek forgiveness and among Your righteous servants.",
            translationHi = "ऐ अल्लाह! मुझे इस महीने में कसरत से इस्तिग़फ़ार करने वालों और अपने नेक बन्दों में शामिल फ़रमा।",
            translationUr = "اے اللہ! مجھے اس مہینے میں کثرت سے استغفار کرنے والوں اور اپنے نیک بندوں میں شامل فرما۔",
            sunnahDeedEn = "Give secret charity (Sadaqah) today, even if it's small, so the left hand knows not what the right gives.",
            sunnahDeedHi = "आज पोशीदा सदक़ा दें, ख़्वाह थोड़ा ही क्यों न हो, ताकि अल्लाह राज़ी हो।",
            sunnahDeedUr = "آج پوشیدہ صدقہ دیں، خواہ تھوڑا ہی کیوں نہ ہو، تاکہ اللہ راضی ہو۔",
            quranGemAyah = "إِنَّ رَحْمَتَ اللَّهِ قَرِيبٌ مِّنَ الْمُحْسِنِينَ",
            quranGemRef = "Surah Al-A'raf (7:56)"
        ),
        RamadanDayPlan(
            dayNumber = 6,
            ashraNumber = 1,
            duaTitle = "Day 6: Seeking Protection from Transgression",
            arabicDua = "اللَّهُمَّ لا تَخْذُلْنِي فِيهِ لِتَعَرُّضِ مَعْصِيَتِكَ وَلا تَضْرِبْنِي بِسِيَاطِ نَقِمَتِكَ",
            transliteration = "Allahumma la takhzulni feehi li-ta'arrudi ma'siyatik, wa la tadribni bi-siyaati naqimatik",
            translationEn = "O Allah, do not humiliate me through disobedience to You, and protect me from the whip of Your retribution.",
            translationHi = "ऐ अल्लाह! मुझे अपनी नाफ़रमानी के ज़रिए ज़लील न फ़रमा और अपनी सज़ा के कोड़ों से महफ़ूज़ रख।",
            translationUr = "اے اللہ! مجھے اپنی نافرمانی کے ذریعے ذلیل نہ فرما اور اپنی سزا کے کوڑوں سے محفوظ رکھ۔",
            sunnahDeedEn = "Visit or check upon elderly relatives or neighbors with a warm greeting.",
            sunnahDeedHi = "अपने बुज़ुर्ग रिश्तेदारों या पड़ोसियों की ख़ैरियत पूछें और उनका हालचाल लें।",
            sunnahDeedUr = "اپنے بزرگ رشتہ داروں یا پڑوسیوں کی خیریت پوچھیں اور ان کا حال چال لیں۔",
            quranGemAyah = "وَسَارِعُوا إِلَىٰ مَغْفِرَةٍ مِّن رَّبِّكُمْ وَجَنَّةٍ عَرْضُهَا السَّمَاوَاتُ وَالْأَرْضُ",
            quranGemRef = "Surah Ali 'Imran (3:133)"
        ),
        RamadanDayPlan(
            dayNumber = 7,
            ashraNumber = 1,
            duaTitle = "Day 7: Endurance in Fasting & Night Prayer",
            arabicDua = "اللَّهُمَّ أَعِنِّي فِيهِ عَلَى صِيَامِهِ وَقِيَامِهِ وَجَنِّبْنِي فِيهِ مِنْ هَفَوَاتِهِ وَآثَامِهِ",
            transliteration = "Allahumma a'inni feehi 'ala siyaamihi wa qiyaamih, wa jannibni feehi min hafawaatihi wa aasaamih",
            translationEn = "O Allah, help me fast its days and stand in prayer during its nights, and protect me from errors and sins.",
            translationHi = "ऐ अल्लाह! इस महीने के रोज़े रखने और क़याम करने में मेरी मदद फ़रमा और कोताहियों व गुनाहों से बचा।",
            translationUr = "اے اللہ! اس مہینے کے روزے رکھنے اور قیام کرنے میں میری مدد فرما اور کوتاہیوں و گناہوں سے بچا۔",
            sunnahDeedEn = "Pray Tahajjud prayer before Sehri — even 2 rakats carry immense light.",
            sunnahDeedHi = "सेहरी से पहले कम से कम 2 रकअत तहज्जुद ज़रूर पढ़ें।",
            sunnahDeedUr = "سحری سے پہلے کم از کم 2 رکعت تہجد ضرور پڑھیں۔",
            quranGemAyah = "وَمِنَ اللَّيْلِ فَتَهَجَّدْ بِهِ نَافِلَةً لَّكَ عَسَىٰ أَن يَبْعَثَكَ رَبُّكَ مَقَامًا مَّحْمُودًا",
            quranGemRef = "Surah Al-Isra (17:79)"
        ),
        RamadanDayPlan(
            dayNumber = 8,
            ashraNumber = 1,
            duaTitle = "Day 8: Compassion Towards Orphans & Weak",
            arabicDua = "اللَّهُمَّ ارْزُقْنِي فِيهِ رَحْمَةَ الأَيْتَامِ وَإِطْعَامَ الطَّعَامِ وَإِفْشَاءَ السَّلامِ",
            transliteration = "Allahummar-zuqni feehi rahmatal aytaam, wa it'aamat-ta'aam, wa ifshaa-as-salaam",
            translationEn = "O Allah, grant me compassion towards orphans, the feeding of food, and the spreading of peace (Salam).",
            translationHi = "ऐ अल्लाह! मुझे यतीमों पर रहम करने, ग़रीबों को खाना खिलाने और सलाम फैलाने की तौफ़ीक़ अता फ़रमा।",
            translationUr = "اے اللہ! مجھے یتیموں پر رحم کرنے، غریبوں کو کھانا کھلانے اور سلام پھیلانے کی توفیق عطا فرما۔",
            sunnahDeedEn = "Smile at your family members and greet everyone with 'Assalamu Alaikum' warmly.",
            sunnahDeedHi = "अपने घरवालों से मुस्कुराकर बात करें और हर एक को मोहब्बत से सलाम करें।",
            sunnahDeedUr = "اپنے گھر والوں سے مسکرا کر بات کریں اور ہر ایک کو محبت سے سلام کریں۔",
            quranGemAyah = "وَيُطْعِمُونَ الطَّعَامَ عَلَىٰ حُبِّهِ مِسْكِينًا وَيَتِيمًا وَأَسِيرًا",
            quranGemRef = "Surah Al-Insan (76:8)"
        ),
        RamadanDayPlan(
            dayNumber = 9,
            ashraNumber = 1,
            duaTitle = "Day 9: Vast Ocean of Mercy",
            arabicDua = "اللَّهُمَّ اجْعَلْ لِي فِيهِ نَصِيباً مِنْ رَحْمَتِكَ الْوَاسِعَةِ وَاهْدِنِي فِيهِ لِبَرَاهِينِكَ السَّاطِعَةِ",
            transliteration = "Allahummaj-'al lee feehi naseeban min rahmatikal-waasi'ah, wahdinee feehi li-baraaheenikas-saati'ah",
            translationEn = "O Allah, bestow upon me a portion of Your vast mercy, and guide me towards Your shining proofs.",
            translationHi = "ऐ अल्लाह! अपनी बेपनाह रहमत में से मेरा हिस्सा मुक़र्रर फ़रमा और अपने रौशन दलाइल की तरफ हिदायत दे।",
            translationUr = "اے اللہ! اپنی بے پناہ رحمت میں سے میرا حصہ مقرر فرما اور اپنے روشن دلائل کی طرف ہدایت دے۔",
            sunnahDeedEn = "Read at least 1 Juz or 4 pages after every prayer today.",
            sunnahDeedHi = "आज हर नमाज़ के बाद कम से कम 4 सफ़हात (पेज) क़ुरआन ज़रूर पढ़ें।",
            sunnahDeedUr = "آج ہر نماز کے بعد کم از کم 4 صفحات قرآن ضرور پڑھیں۔",
            quranGemAyah = "وَرَحْمَتِي وَسِعَتْ كُلَّ شَيْءٍ",
            quranGemRef = "Surah Al-A'raf (7:156)"
        ),
        RamadanDayPlan(
            dayNumber = 10,
            ashraNumber = 1,
            duaTitle = "Day 10: Complete Trust in Allah (Tawakkul)",
            arabicDua = "اللَّهُمَّ اجْعَلْنِي فِيهِ مِنَ الْمُتَوَكِّلِينَ عَلَيْكَ وَاجْعَلْنِي فِيهِ مِنَ الْفَائِزِينَ لَدَيْكَ",
            transliteration = "Allahummaj-'alnee feehi minal-mutawakkileena 'alayk, waj-'alnee feehi minal-faa'izeena ladayk",
            translationEn = "O Allah, place me among those who rely wholly upon You, and make me among the triumphant in Your sight.",
            translationHi = "ऐ अल्लाह! मुझे उन लोगों में शामिल फ़रमा जो तुझ पर भरोसा करते हैं और तेरे नज़दीक कामयाब हैं।",
            translationUr = "اے اللہ! مجھے ان لوگوں میں شامل فرما جو تجھ پر بھروسہ کرتے ہیں اور تیرے نزدیک کامیاب ہیں۔",
            sunnahDeedEn = "End of 1st Ashra of Mercy: Thank Allah for the blessings of the first 10 days.",
            sunnahDeedHi = "रहमत के पहले अशरे के ख़ात्मे पर अल्लाह की नेमतों का दिल से शुक्र अदा करें।",
            sunnahDeedUr = "رحمت کے پہلے عشرے کے خاتمے پر اللہ کی نعمتوں کا دل سے شکر ادا کریں۔",
            quranGemAyah = "وَمَن يَتَوَكَّلْ عَلَى اللَّهِ فَهُوَ حَسْبُهُ",
            quranGemRef = "Surah At-Talaq (65:3)"
        ),
        // Days 11-20 (Ashra-e-Maghfirat)
        RamadanDayPlan(
            dayNumber = 11,
            ashraNumber = 2,
            duaTitle = "Day 11: Beginning of Forgiveness Ashra",
            arabicDua = "اللَّهُمَّ حَبِّبْ إِلَيَّ فِيهِ الإِحْسَانَ وَكَرِّهْ إِلَيَّ فِيهِ الْفُسُوقَ وَالْعِصْيَانَ",
            transliteration = "Allahumma habbib ilayya feehil-ihsaan, wa karrih ilayya feehil-fusooqa wal-'isyaan",
            translationEn = "O Allah, make good deeds beloved to me, and make sin and disobedience hateful to me.",
            translationHi = "ऐ अल्लाह! नेकी को मेरे दिल में महबूब बना दे और गुनाह व नाफ़रमानी से नफ़रत पैदा फ़रमा दे।",
            translationUr = "اے اللہ! نیکی کو میرے دل میں محبوب بنا دے اور گناہ و نافرمانی سے نفرت پیدا فرما دے۔",
            sunnahDeedEn = "Recite Astaghfirullah at least 300 times with genuine sorrow over past missteps.",
            sunnahDeedHi = "माज़ी के गुनाहों पर शर्मसार होकर कम से कम 300 बार अस्तग़फ़िरुल्लाह पढ़ें।",
            sunnahDeedUr = "ماضی کے گناہوں پر شرمسار ہو کر کم از کم 300 بار استغفار کریں۔",
            quranGemAyah = "قُلْ يَا عِبَادِيَ الَّذِينَ أَسْرَفُوا عَلَىٰ أَنفُسِهِمْ لَا تَقْنَطُوا مِن رَّحْمَةِ اللَّهِ",
            quranGemRef = "Surah Az-Zumar (39:53)"
        ),
        RamadanDayPlan(
            dayNumber = 12,
            ashraNumber = 2,
            duaTitle = "Day 12: Beauty of Modesty & Chastity",
            arabicDua = "اللَّهُمَّ زَيِّنِّي فِيهِ بِالسِّتْرِ وَالْعَفَافِ وَاسْتُرْنِي فِيهِ بِلِبَاسِ الْقُنُوعِ وَالْكَفَافِ",
            transliteration = "Allahumma zayyinnee feehi bis-sitri wal-'afaaf, was-turnee feehi bi-libaasil-qunoo'i wal-kafaaf",
            translationEn = "O Allah, adorn me with modesty and chastity, and clothe me in contentment and sufficiency.",
            translationHi = "ऐ अल्लाह! मुझे पर्दे और पाकदामनी से ज़ीनत दे और क़नाअत व किफ़ायत का लिबास पहना।",
            translationUr = "اے اللہ! مجھے پردے اور پاکدامنی سے زینت دے اور قناعت و کفایت کا لباس پہنا۔",
            sunnahDeedEn = "Forgive anyone who has ever hurt you, and seek forgiveness from someone you may have wronged.",
            sunnahDeedHi = "जिसने आपका दिल दुखाया हो उसे माफ़ कर दें और जिसकी दिलआज़ारी की हो उससे माफ़ी मांग लें।",
            sunnahDeedUr = "جس نے آپ کا دل دکھایا ہو اسے معاف کر دیں اور جس کی دل آزاری کی ہو اس سے معافی مانگ لیں۔",
            quranGemAyah = "وَلْيَعْفُوا وَلْيَصْفَحُوا ۗ أَلَا تُحِبُّونَ أَن يَغْفِرَ اللَّهُ لَكُمْ",
            quranGemRef = "Surah An-Nur (24:22)"
        ),
        RamadanDayPlan(
            dayNumber = 13,
            ashraNumber = 2,
            duaTitle = "Day 13: Purification of the Heart",
            arabicDua = "اللَّهُمَّ طَهِّرْنِي فِيهِ مِنَ الدَّنَسِ وَالأَقْذَارِ وَصَبِّرْنِي فِيهِ عَلَى كَائِنَاتِ الأَقْدَارِ",
            transliteration = "Allahumma tahhirnee feehi minad-danasi wal-aqzaar, wa sabbirnee feehi 'ala kaa'inaatil-aqdaar",
            translationEn = "O Allah, cleanse me from filth and impurities, and grant me patience over decreed trials.",
            translationHi = "ऐ अल्लाह! मुझे हर तरह की आलूदगी से पाक फ़रमा और तक़दीर के फ़ैसलों पर सब्र अता कर।",
            translationUr = "اے اللہ! مجھے ہر طرح کی آلودگی سے پاک فرما اور تقدیر کے فیصلوں پر صبر عطا کر۔",
            sunnahDeedEn = "Make special Dua for the oppressed and struggling brothers and sisters across the Ummah.",
            sunnahDeedHi = "दुनिया भर के परेशान-हाल और मज़लूम मुसलमानों के लिए ख़ास दुआ करें।",
            sunnahDeedUr = "دنیا بھر کے پریشان حال اور مظلوم مسلمانوں کے لیے خاص دعا کریں۔",
            quranGemAyah = "يَوْمَ لَا يَنفَعُ مَالٌ وَلَا بَنُونَ إِلَّا مَنْ أَتَى اللَّهَ بِقَلْبٍ سَلِيمٍ",
            quranGemRef = "Surah Ash-Shu'ara (26:88-89)"
        ),
        RamadanDayPlan(
            dayNumber = 14,
            ashraNumber = 2,
            duaTitle = "Day 14: Overlooking Mistakes",
            arabicDua = "اللَّهُمَّ لا تُؤَاخِذْنِي فِيهِ بِالْعَثَرَاتِ وَأَقِلْنِي فِيهِ مِنَ الْخَطَايَا وَالْهَفَوَاتِ",
            transliteration = "Allahumma la tu'aakhiznee feehi bil-'atharaat, wa aqilnee feehi minal-khataaya wal-hafawaat",
            translationEn = "O Allah, do not hold me accountable for my slips, and pardon my faults and errors.",
            translationHi = "ऐ अल्लाह! मुझे मेरी भूल-चूक पर न पकड़ और मेरी ख़ताओं व लग़्ज़िशों से दरगुज़र फ़रमा।",
            translationUr = "اے اللہ! مجھے میری بھول چوک پر نہ پکڑ اور میری خطاؤں و لغزشوں سے درگزر فرما۔",
            sunnahDeedEn = "Offer extra Nawafil (voluntary prayers) during the quiet daytime or after Taraweeh.",
            sunnahDeedHi = "तन्हाई के वक़्त नफ़्ल नमाज़ें पढ़कर अल्लाह से अपने ताल्लुक़ को गहरा करें।",
            sunnahDeedUr = "تنہائی کے وقت نفل نمازیں پڑھ کر اللہ سے اپنے تعلق کو گہرا کریں۔",
            quranGemAyah = "رَبَّنَا لَا تُؤَاخِذْنَا إِن نَّسِينَا أَوْ أَخْطَأْنَا",
            quranGemRef = "Surah Al-Baqarah (2:286)"
        ),
        RamadanDayPlan(
            dayNumber = 15,
            ashraNumber = 2,
            duaTitle = "Day 15: Mid-Ramadan Reflection & Obedience",
            arabicDua = "اللَّهُمَّ ارْزُقْنِي فِيهِ طَاعَةَ الْخَاشِعِينَ وَاشْرَحْ فِيهِ صَدْرِي بِإِنَابَةِ الْمُخْبِتِينَ",
            transliteration = "Allahummar-zuqnee feehi taa'atal-khaashi'een, wash-rah feehi sadree bi-inaabatil-mukhbiteen",
            translationEn = "O Allah, grant me the humble obedience of the reverent, and expand my heart with the return of the submissive.",
            translationHi = "ऐ अल्लाह! मुझे आजिज़ों जैसी फ़रमाबरदारी अता कर और सच्चे बन्दों जैसी तौबा से मेरा सीना खोल दे।",
            translationUr = "اے اللہ! مجھے عاجزوں جیسی فرماں برداری عطا کر اور سچے بندوں جیسی توبہ سے میرا سینہ کھول دے۔",
            sunnahDeedEn = "Midpoint check: Calculate how many Quran Juz you have completed; catch up if needed.",
            sunnahDeedHi = "आधा रमज़ान मुकम्मल! अपना क़ुरआन का हिसाब लगाएं और अगर पीछे हों तो रफ़्तार बढ़ाएं।",
            sunnahDeedUr = "آدھا رمضان مکمل! اپنا قرآن کا حساب لگائیں اور اگر پیچھے ہوں تو رفتار بڑھائیں۔",
            quranGemAyah = "أَلَمْ يَأْنِ لِلَّذِينَ آمَنُوا أَن تَخْشَعَ قُلُوبُهُمْ لِذِكْرِ اللَّهِ",
            quranGemRef = "Surah Al-Hadid (57:16)"
        ),
        RamadanDayPlan(
            dayNumber = 16,
            ashraNumber = 2,
            duaTitle = "Day 16: Harmony with the Good Companions",
            arabicDua = "اللَّهُمَّ وَفِّقْنِي فِيهِ لِمُوَافَقَةِ الأَبْرَارِ وَجَنِّبْنِي فِيهِ مُرَافَقَةَ الأَشْرَارِ",
            transliteration = "Allahumma waffiqnee feehi li-muwaafaqatil-abraar, wa jannibnee feehi muraafaqatal-ashraar",
            translationEn = "O Allah, guide me to walk in step with the righteous and protect me from the company of the wicked.",
            translationHi = "ऐ अल्लाह! मुझे नेकी के रास्ते पर चलने वालों का साथ अता कर और बुरों की सोहबत से महफ़ूज़ रख।",
            translationUr = "اے اللہ! مجھے نیکی کے راستے پر چلنے والوں کا ساتھ عطا کر اور بروں کی صحبت سے محفوظ رکھ۔",
            sunnahDeedEn = "Join a gathering of knowledge, Hadith circle, or Islamic lecture today.",
            sunnahDeedHi = "आज किसी दीनी महफ़िल, हदीस के हलक़े या इस्लामी दरस में शिरकत करें।",
            sunnahDeedUr = "آج کسی دینی محفل، حدیث کے حلقے یا اسلامی درس میں شرکت کریں۔",
            quranGemAyah = "يَا أَيُّهَا الَّذِينَ آمَنُوا اتَّقُوا اللَّهَ وَكُونُوا مَعَ الصَّادِقِينَ",
            quranGemRef = "Surah At-Tawbah (9:119)"
        ),
        RamadanDayPlan(
            dayNumber = 17,
            ashraNumber = 2,
            duaTitle = "Day 17: Guidance Towards Virtuous Aspirations",
            arabicDua = "اللَّهُمَّ اهْدِنِي فِيهِ لِصَالِحِ الأَعْمَالِ وَاقْضِ لِي فِيهِ الْحَوَائِجَ وَالآَمَالَ",
            transliteration = "Allahummah-dinee feehi li-saalihil-a'maal, waqdi lee feehil-hawaa'ija wal-aamaal",
            translationEn = "O Allah, guide me to righteous deeds, and fulfill for me my needs and aspirations.",
            translationHi = "ऐ अल्लाह! मुझे नेक आमाल की हिदायत दे और मेरी जायज़ हाजात व तमन्नाओं को पूरा फ़रमा।",
            translationUr = "اے اللہ! مجھے نیک اعمال کی ہدایت دے اور میری جائز حاجات و تمناؤں کو پورا فرما۔",
            sunnahDeedEn = "Remember the Battle of Badr (17th Ramadan) — make Dua for strength and steadfastness in faith.",
            sunnahDeedHi = "17 रमज़ान यवमे बद्र की याद: दीन पर साबित क़दमी और ईमान की हिफ़ाज़त की दुआ करें।",
            sunnahDeedUr = "17 رمضان یومِ بدر کی یاد: دین پر ثابت قدمی اور ایمان کی حفاظت کی دعا کریں۔",
            quranGemAyah = "وَمَا النَّصْرُ إِلَّا مِنْ عِندِ اللَّهِ الْعَزِيزِ الْحَكِيمِ",
            quranGemRef = "Surah Ali 'Imran (3:126)"
        ),
        RamadanDayPlan(
            dayNumber = 18,
            ashraNumber = 2,
            duaTitle = "Day 18: Blessings of Suhoor Hours",
            arabicDua = "اللَّهُمَّ نَبِّهْنِي فِيهِ لِبَرَكَاتِ أَسْحَارِهِ وَنَوِّرْ فِيهِ قَلْبِي بِضِيَاءِ أَنْوَارِهِ",
            transliteration = "Allahumma nabbihnee feehi li-barakaati as-haarih, wa nawwir feehi qalbee bi-diyaa'i anwaarih",
            translationEn = "O Allah, awaken me to the blessings of its early dawns, and illuminate my heart with its radiant light.",
            translationHi = "ऐ अल्लाह! मुझे सेहरी के औक़ात की बरकतों से बेदार कर और मेरे दिल को अपने नूर से रौशन फ़रमा।",
            translationUr = "اے اللہ! مجھے سحری کے اوقات کی برکتوں سے بیدار کر اور میرے دل کو اپنے نور سے روشن فرما۔",
            sunnahDeedEn = "Eat Suhoor even if it's just a sip of water or dates, as the Prophet (ﷺ) said there is Barakah in it.",
            sunnahDeedHi = "सेहरी ज़रूर खाएं चाहे एक खजूर या पानी ही हो, क्योंकि सेहरी में बरकत है।",
            sunnahDeedUr = "سحری ضرور کھائیں چاہے ایک کھجور یا پانی ہی ہو، کیونکہ سحری میں برکت ہے۔",
            quranGemAyah = "وَالْمُسْتَغْفِرِينَ بِالْأَسْحَارِ",
            quranGemRef = "Surah Ali 'Imran (3:17)"
        ),
        RamadanDayPlan(
            dayNumber = 19,
            ashraNumber = 2,
            duaTitle = "Day 19: Preparation for the Glorious Finale",
            arabicDua = "اللَّهُمَّ وَفِّرْ فِيهِ حَظِّي مِنْ بَرَكَاتِهِ وَسَهِّلْ سَبِيلِي إِلَى خَيْرَاتِهِ",
            transliteration = "Allahumma waffir feehi hazzee min barakaatih, wa sahhil sabeeli ila khayraatih",
            translationEn = "O Allah, maximize my share of its blessings, and ease my path to its boundless goodness.",
            translationHi = "ऐ अल्लाह! इस महीने की बरकतों से मेरा हिस्सा बढ़ा दे और नेकियों की तरफ मेरे रास्ते को आसान कर।",
            translationUr = "اے اللہ! اس مہینے کی برکتوں سے میرا حصہ بڑھا دے اور نیکیوں کی طرف میرے راستے کو آسان کر۔",
            sunnahDeedEn = "Plan your worship strategy for the upcoming Last 10 Nights (Laylatul Qadr).",
            sunnahDeedHi = "आख़िरी 10 रातों (शब-ए-क़द्र की तलाश) के लिए अपनी इबादत का मुकम्मल प्लान बनाएं।",
            sunnahDeedUr = "آخری 10 راتوں (شبِ قدر کی تلاش) کے لیے اپنی عبادت کا مکمل پلان بنائیں۔",
            quranGemAyah = "فَاسْتَبِقُوا الْخَيْرَاتِ",
            quranGemRef = "Surah Al-Baqarah (2:148)"
        ),
        RamadanDayPlan(
            dayNumber = 20,
            ashraNumber = 2,
            duaTitle = "Day 20: Opening Gates of Paradise",
            arabicDua = "اللَّهُمَّ افْتَحْ لِي فِيهِ أَبْوَابَ الْجِنَانِ وَأَغْلِقْ عَنِّي فِيهِ أَبْوَابَ النِّيرَانِ",
            transliteration = "Allahummaf-tah lee feehi abwaabal-jinaan, wa aghliq 'annee feehi abwaaban-neeraan",
            translationEn = "O Allah, open wide for me the gates of Paradise and shut tightly against me the gates of the Fire.",
            translationHi = "ऐ अल्लाह! मेरे लिए जन्नत के दरवाज़े खोल दे और जहन्नम के दरवाज़े मुझ पर बन्द फ़रमा दे।",
            translationUr = "اے اللہ! میرے لیے جنت کے دروازے کھول دے اور جہنم کے دروازے مجھ پر بند فرما دے۔",
            sunnahDeedEn = "End of 2nd Ashra of Forgiveness: Renew your vows of repentance before the final 10 days begin.",
            sunnahDeedHi = "मग़फ़िरत के दूसरे अशरे का ख़ात्मे: आख़िरी अशरे में दाख़िल होने से पहले सच्ची तौबा करें।",
            sunnahDeedUr = "مغفرت کے دوسرے عشرے کا خاتمہ: آخری عشرے میں داخل ہونے سے پہلے سچی توبہ کریں۔",
            quranGemAyah = "جَنَّاتُ عَدْنٍ مُّفَتَّحَةً لَّهُمُ الْأَبْوَابُ",
            quranGemRef = "Surah Sad (38:50)"
        ),
        // Days 21-30 (Ashra-e-Najat & Laylatul Qadr)
        RamadanDayPlan(
            dayNumber = 21,
            ashraNumber = 3,
            duaTitle = "Day 21: First Odd Night (21st Night of Power)",
            arabicDua = "اللَّهُمَّ إِنَّكَ عَفُوٌّ تُحِبُّ الْعَفْوَ فَاعْفُ عَنِّي",
            transliteration = "Allahumma innaka 'Afuwwun tuhibbul-'afwa fa'fu 'anni",
            translationEn = "O Allah, You are Most Forgiving and You love forgiveness, so forgive me.",
            translationHi = "ऐ अल्लाह! तू बहुत माफ़ करने वाला है और माफ़ी को पसंद फ़रमाता है, पस मुझे माफ़ फ़रमा दे।",
            translationUr = "اے اللہ! تو بہت معاف کرنے والا ہے اور معافی کو پسند فرماتا ہے، پس مجھے معاف فرما دے۔",
            sunnahDeedEn = "Seek Laylatul Qadr on this 21st odd night; perform extra Tahajjud and recite Quran till dawn.",
            sunnahDeedHi = "21वीं ताक रात: शब-ए-क़द्र की तलाश में कसरत से नवाफ़िल, ज़िक्र और रो-रोकर दुआ करें।",
            sunnahDeedUr = "21ویں طاق رات: شبِ قدر کی تلاش میں کثرت سے نوافل، ذکر اور رو رو کر دعا کریں۔",
            quranGemAyah = "إِنَّا أَنزَلْنَاهُ فِي لَيْلَةِ الْقَدْرِ",
            quranGemRef = "Surah Al-Qadr (97:1)"
        ),
        RamadanDayPlan(
            dayNumber = 22,
            ashraNumber = 3,
            duaTitle = "Day 22: Freedom from the Fire",
            arabicDua = "اللَّهُمَّ أَعْتِقْ رِقَابَنَا وَرِقَابَ آبَائِنَا وَأُمَّهَاتِنَا مِنَ النَّارِ",
            transliteration = "Allahumma a'tiq riqaabanaa wa riqaaba aabaa'inaa wa ummahaatinaa minan-naar",
            translationEn = "O Allah, emancipate our necks and the necks of our fathers and mothers from the Hellfire.",
            translationHi = "ऐ अल्लाह! हमारी गर्दनों को और हमारे वालिदैन की गर्दनों को जहन्नम की आग से आज़ाद फ़रमा दे।",
            translationUr = "اے اللہ! ہماری گردنوں کو اور ہمارے والدین کی گردنوں کو جہنم کی آگ سے آزاد فرما دے۔",
            sunnahDeedEn = "Start Itikaf (spiritual retreat in the mosque) or create an Itikaf sanctuary at home for women.",
            sunnahDeedHi = "ऐतेकाफ़ की सुन्नत ज़िंदा करें या घर में ख़ास इबादतगाह मुक़र्रर करें।",
            sunnahDeedUr = "اعتکاف کی سنت زندہ کریں یا گھر میں خاص عبادت گاہ مقرر کریں۔",
            quranGemAyah = "فَمَن زُحْزِحَ عَنِ النَّارِ وَأُدْخِلَ الْجَنَّةَ فَقَدْ فَازَ",
            quranGemRef = "Surah Ali 'Imran (3:185)"
        ),
        RamadanDayPlan(
            dayNumber = 23,
            ashraNumber = 3,
            duaTitle = "Day 23: Second Odd Night (23rd Night of Power)",
            arabicDua = "اللَّهُمَّ اغْسِلْنِي فِيهِ مِنَ الذُّنُوبِ وَطَهِّرْنِي فِيهِ مِنَ الْعُيُوبِ",
            transliteration = "Allahummagh-silnee feehi minaz-zunoob, wa tahhirnee feehi minal-'uyoob",
            translationEn = "O Allah, wash away my sins and cleanse me from every flaw and spiritual defect.",
            translationHi = "ऐ अल्लाह! मुझे गुनाहों से धो दे और हर ऐब व ख़ामी से पाक साफ़ फ़रमा दे।",
            translationUr = "اے اللہ! مجھے گناہوں سے دھو دے اور ہر عیب و خامی سے پاک صاف فرما دے۔",
            sunnahDeedEn = "23rd Odd Night: Engage in intense supplication; give charity for the sake of Laylatul Qadr.",
            sunnahDeedHi = "23वीं ताक रात: कसरत से सदक़ा दें और शब-ए-क़द्र की बरकत हासिल करने के लिए बेदार रहें।",
            sunnahDeedUr = "23ویں طاق رات: کثرت سے صدقہ دیں اور شبِ قدر کی برکت حاصل کرنے کے لیے بیدار رہیں۔",
            quranGemAyah = "لَيْلَةُ الْقَدْرِ خَيْرٌ مِّنْ أَلْفِ شَهْرٍ",
            quranGemRef = "Surah Al-Qadr (97:3)"
        ),
        RamadanDayPlan(
            dayNumber = 24,
            ashraNumber = 3,
            duaTitle = "Day 24: Seeking Everything Pleasing to Allah",
            arabicDua = "اللَّهُمَّ إِنِّي أَسْأَلُكَ فِيهِ مَا يُرْضِيكَ وَأَعُوذُ بِكَ مِمَّا يُؤْذِيكَ",
            transliteration = "Allahumma innee as'aluka feehi maa yurzeek, wa a'oozu bika mimmaa yu'zeek",
            translationEn = "O Allah, I ask You for all that pleases You, and I take refuge in You from that which displeases You.",
            translationHi = "ऐ अल्लाह! मैं तुझसे हर वह चीज़ मांगता हूँ जो तुझे राज़ी करे और तेरी नाफ़रमानी से तेरी पनाह चाहता हूँ।",
            translationUr = "اے اللہ! میں تجھ سے ہر وہ چیز مانگتا ہوں جو تجھے راضی کرے اور تیری نافرمانی سے تیری پناہ چاہتا ہوں۔",
            sunnahDeedEn = "Calculate your Zakat and Sadaqatul Fitr early so it reaches the needy before Eid.",
            sunnahDeedHi = "अपनी ज़कात और सदक़ा-ए-फ़ित्र का हिसाब अभी से लगाएं ताकि ईद से पहले मुस्तहिक़ तक पहुंच जाए।",
            sunnahDeedUr = "اپنی زکوٰۃ اور صدقۂ فطر کا حساب ابھی سے لگائیں تاکہ عید سے پہلے مستحق تک پہنچ جائے۔",
            quranGemAyah = "وَأَقِيمُوا الصَّلَاةَ وَآتُوا الزَّكَاةَ ۚ وَمَا تُقَدِّمُوا لِأَنفُسِكُم مِّنْ خَيْرٍ تَجِدُوهُ عِندَ اللَّهِ",
            quranGemRef = "Surah Al-Baqarah (2:110)"
        ),
        RamadanDayPlan(
            dayNumber = 25,
            ashraNumber = 3,
            duaTitle = "Day 25: Third Odd Night (25th Night of Power)",
            arabicDua = "اللَّهُمَّ اجْعَلْنِي فِيهِ مُحِبّاً لِأَوْلِيَائِكَ وَمُعَادِياً لِأَعْدَائِكَ مُسْتَنّاً بِسُنَّةِ خَاتَمِ أَنْبِيَائِكَ",
            transliteration = "Allahummaj-'alnee feehi muhibban li-awliyaa'ik, wa mu'aadiyan li-a'daa'ik, mustannan bi-sunnati khaatami ambiyaa'ik",
            translationEn = "O Allah, make me love Your close friends, abhor Your enemies, and follow the Sunnah of the Seal of the Prophets.",
            translationHi = "ऐ अल्लाह! मुझे अपने नेक बन्दों से मोहब्बत करने वाला और अपने नबी (ﷺ) की सुन्नत का पैरव बना।",
            translationUr = "اے اللہ! مجھے اپنے نیک بندوں سے محبت کرنے والا اور اپنے نبی (ﷺ) کی سنت کا پیرو بنا۔",
            sunnahDeedEn = "25th Odd Night: Recite Surah Al-Ikhlas, Durood, and engage in heartfelt dua for yourself and parents.",
            sunnahDeedHi = "25वीं ताक रात: सूरह इख़्लास, दुरूद शरीफ़ और अपने वालिदैन की मग़फ़िरत के लिए ख़ास दुआएं करें।",
            sunnahDeedUr = "25ویں طاق رات: سورۂ اخلاص، درود شریف اور اپنے والدین کی مغفرت کے لیے خاص دعائیں کریں۔",
            quranGemAyah = "تَنَزَّلُ الْمَلَائِكَةُ وَالرُّوحُ فِيهَا بِإِذْنِ رَبِّهِم مِّن كُلِّ أَمْرٍ",
            quranGemRef = "Surah Al-Qadr (97:4)"
        ),
        RamadanDayPlan(
            dayNumber = 26,
            ashraNumber = 3,
            duaTitle = "Day 26: Appreciated Endeavors",
            arabicDua = "اللَّهُمَّ اجْعَلْ سَعْيِي فِيهِ مَشْكُوراً وَذَنْبِي فِيهِ مَغْفُوراً وَعَمَلِي فِيهِ مَقْبُولاً",
            transliteration = "Allahummaj-'al sa'yee feehi mashkooraa, wa zambee feehi maghfooraa, wa 'amalee feehi maqboolaa",
            translationEn = "O Allah, make my striving appreciated, my sins forgiven, and my good deeds accepted.",
            translationHi = "ऐ अल्लाह! मेरी कोशिशों को क़बूल फ़रमा, मेरे गुनाहों को बख्श दे और मेरे आमाल को बारगाह में मक़बूल कर।",
            translationUr = "اے اللہ! میری کوششوں کو قبول فرما، میرے گناہوں کو بخش دے اور میرے اعمال کو بارگاہ میں مقبول کر۔",
            sunnahDeedEn = "Ensure you have not broken any ties of kinship; reconcile with any estranged family member.",
            sunnahDeedHi = "सुलह-रहमी: अगर किसी अज़ीज़ से बोलचाल बन्द हो तो आज आगे बढ़कर सुलह कर लें।",
            sunnahDeedUr = "صلح رحمی: اگر کسی عزیز سے بول چال بند ہو تو آج آگے بڑھ کر صلح کر لیں۔",
            quranGemAyah = "إِنَّ هَٰذَا كَانَ لَكُمْ جَزَاءً وَكَانَ سَعْيُكُم مَّشْكُورًا",
            quranGemRef = "Surah Al-Insan (76:22)"
        ),
        RamadanDayPlan(
            dayNumber = 27,
            ashraNumber = 3,
            duaTitle = "Day 27: 27th Night of Ramadan (Peak Night of Laylatul Qadr)",
            arabicDua = "اللَّهُمَّ إِنَّكَ عَفُوٌّ كَرِيمٌ تُحِبُّ الْعَفْوَ فَاعْفُ عَنِّي",
            transliteration = "Allahumma innaka 'Afuwwun Kareemun tuhibbul-'afwa fa'fu 'anni",
            translationEn = "O Allah, You are Most Forgiving and Generous, You love to forgive, so forgive me.",
            translationHi = "ऐ अल्लाह! तू बेहद माफ़ करने वाला, करम फ़रमाने वाला है, माफ़ी को पसंद करता है, पस मुझे माफ़ फ़रमा दे।",
            translationUr = "اے اللہ! تو بے حد معاف کرنے والا، کرم فرمانے والا ہے، معافی کو پسند کرتا ہے، پس مجھے معاف فرما دے۔",
            sunnahDeedEn = "27th Night: Historically emphasized night for Laylatul Qadr. Pray with utmost devotion, shed tears, and seek deliverance.",
            sunnahDeedHi = "27वीं शब: शब-ए-क़द्र की सबसे ख़ास रात! रो-रोकर दुआएं करें, गिड़गिड़ाएं और जहन्नम से रिहाई मांगें।",
            sunnahDeedUr = "27ویں شب: شبِ قدر کی سب سے خاص رات! رو رو کر دعائیں کریں، گڑگڑائیں اور جہنم سے رہائی مانگیں۔",
            quranGemAyah = "سَلَامٌ هِيَ حَتَّىٰ مَطْلَعِ الْفَجْرِ",
            quranGemRef = "Surah Al-Qadr (97:5)"
        ),
        RamadanDayPlan(
            dayNumber = 28,
            ashraNumber = 3,
            duaTitle = "Day 28: Abundant Blessings of Nawafil",
            arabicDua = "اللَّهُمَّ وَفِّرْ حَظِّي فِيهِ مِنَ النَّوَافِلِ وَأَكْرِمْنِي فِيهِ بِإِحْضَارِ الْمَسَائِلِ",
            transliteration = "Allahumma waffir hazzee feehi minan-nawaafil, wa akrimnee feehi bi-ihdaaril-masaa'il",
            translationEn = "O Allah, grant me an abundant share of voluntary prayers, and honor me with answered supplications.",
            translationHi = "ऐ अल्लाह! नवाफ़िल से मेरा हिस्सा भरपूर कर और मेरी दुआओं को क़बूलियत से नवाज़।",
            translationUr = "اے اللہ! نوافل سے میرا حصہ بھرپور کر اور میری دعاؤں کو قبولیت سے نواز۔",
            sunnahDeedEn = "Pay your Sadaqatul Fitr (Fitrana) today so that poor families can prepare for Eid happily.",
            sunnahDeedHi = "आज ही अपना सदक़ा-ए-फ़ित्र अदा कर दें ताकि ग़रीब लोग भी ईद की ख़ुशियां मना सकें।",
            sunnahDeedUr = "آج ہی اپنا صدقۂ فطر ادا کر دیں تاکہ غریب لوگ بھی عید کی خوشیاں منا سکیں۔",
            quranGemAyah = "فَاسْتَجَبْنَا لَهُ وَنَجَّيْنَاهُ مِنَ الْغَمِّ ۚ وَكَذَٰلِكَ نُنجِي الْمُؤْمِنِينَ",
            quranGemRef = "Surah Al-Anbiya (21:88)"
        ),
        RamadanDayPlan(
            dayNumber = 29,
            ashraNumber = 3,
            duaTitle = "Day 29: Final Odd Night (29th Night of Power)",
            arabicDua = "اللَّهُمَّ غَشِّنِي فِيهِ بِالرَّحْمَةِ وَارْزُقْنِي فِيهِ التَّوْفِيقَ وَالْعِصْمَةَ",
            transliteration = "Allahumma ghash-shinee feehi bir-rahmah, war-zuqnee feahit-tawfeeqa wal-'ismah",
            translationEn = "O Allah, envelop me with Your mercy, and grant me success and protection from sin.",
            translationHi = "ऐ अल्लाह! मुझे अपनी रहमत की चादर में ढांप ले और मुझे तौफ़ीक़ व गुनाहों से हिफ़ाज़त अता फ़रमा।",
            translationUr = "اے اللہ! مجھے اپنی رحمت کی چادر میں ڈھانپ لے اور مجھے توفیق و گناہوں سے حفاظت عطا فرما۔",
            sunnahDeedEn = "29th Odd Night: Do not become lax before the moon is sighted; keep worshipping till the very end.",
            sunnahDeedHi = "29वीं ताक रात: सुस्ती न आने दें, चांद देखने तक अपनी इबादत को पूरे जोश से जारी रखें।",
            sunnahDeedUr = "29ویں طاق رات: سستی نہ آنے دیں، چاند دیکھنے تک اپنی عبادت کو پورے جوش سے جاری رکھیں۔",
            quranGemAyah = "وَاعْبُدْ رَبَّكَ حَتَّىٰ يَأْتِيَكَ الْيَقِينُ",
            quranGemRef = "Surah Al-Hijr (15:99)"
        ),
        RamadanDayPlan(
            dayNumber = 30,
            ashraNumber = 3,
            duaTitle = "Day 30: Acceptance & Gratitude",
            arabicDua = "اللَّهُمَّ اجْعَلْ صِيَامِي فِيهِ بِالشُّكْرِ وَالْقَبُولِ عَلَى مَا تَرْضَاهُ وَيَرْضَاهُ الرَّسُولُ",
            transliteration = "Allahummaj-'al siyaamee feehi bish-shukri wal-qabooli 'ala maa tardaahu wa yardaahur-Rasool",
            translationEn = "O Allah, accept our fasting with gratitude and favor, in accordance with what pleases You and Your Messenger.",
            translationHi = "ऐ अल्लाह! हमारे रोज़ों को शुक्र और क़बूलियत के साथ क़बूल फ़रमा, जिससे तू और तेरा रसूल राज़ी हों।",
            translationUr = "اے اللہ! ہمارے روزوں کو شکر اور قبولیت کے ساتھ قبول فرما، جس سے تو اور تیرا رسول راضی ہوں۔",
            sunnahDeedEn = "Complete your Quran recitation, give Takbeerat of Eid, and make firm intention to carry these good habits all year round.",
            sunnahDeedHi = "क़ुरआन मुकम्मल करें, तकबीरात पढ़ें और पूरे साल इन नेकियों पर क़ायम रहने का अहद करें।",
            sunnahDeedUr = "قرآن مکمل کریں، تکبیرات پڑھیں اور پورے سال ان نیکیوں پر قائم رہنے کا عہد کریں۔",
            quranGemAyah = "وَلِتُكْمِلُوا الْعِدَّةَ وَلِتُكَبِّرُوا اللَّهَ عَلَىٰ مَا هَدَاكُمْ وَلَعَلَّكُمْ تَشْكُرُونَ",
            quranGemRef = "Surah Al-Baqarah (2:185)"
        )
    )

    // ── Ramadan Rules & Fiqh Doubts (Common Questions) ──
    val fiqhQuestions: List<RamadanFiqhItem> = listOf(
        RamadanFiqhItem(
            questionEn = "Does using an Asthma Inhaler break the fast?",
            questionHi = "क्या अस्थमा इनहेलर (Inhaler) इस्तेमाल करने से रोज़ा टूटता है?",
            questionUr = "کیا دمہ کا انہیلر (Inhaler) استعمال کرنے سے روزہ ٹوٹتا ہے؟",
            rulingType = "Permissible (Majority / Contemporary Councils)",
            answerEn = "According to the Islamic Fiqh Academy and majority contemporary scholars (e.g. Ibn Baz, Ibn Uthaymeen, European Fatwa Council), medicinal gas inhalers do NOT break the fast because it is compressed gas directed to the lungs for breathing, not nourishment or food entering the stomach.",
            answerHi = "इस्लामिक फ़िक़्ह एकेडमी और जदीद उलमा-ए-किराम के मुताबिक़ इनहेलर से रोज़ा नहीं टूटता, क्योंकि यह फेफड़ों में सांस की नली खोलने के लिए गैस पहुंचाता है, पेट में गिज़ा या तक़वियत का काम नहीं करता।",
            answerUr = "اسلامک فقہ اکیڈمی اور معاصر علماء کے مطابق انہیلر سے روزہ نہیں ٹوٹتا کیونکہ یہ پھیپھڑوں میں گیس پہنچاتا ہے، معدے میں غذا نہیں بنتا۔",
            reference = "Islamic Fiqh Academy (Jeddah, Res. 93)"
        ),
        RamadanFiqhItem(
            questionEn = "Do Eye Drops, Ear Drops, or Contact Lens break the fast?",
            questionHi = "क्या आंख या कान में ड्रॉप्स डालने से रोज़ा टूटता है?",
            questionUr = "کیا آنکھ یا کان میں قطرے ڈالنے سے روزہ ٹوٹتا ہے؟",
            rulingType = "Permissible (Does not break fast)",
            answerEn = "Eye drops, ear drops (with intact eardrum), and contact lenses do not break the fast even if a faint taste is felt in the throat, as the eye is not an open passage to the digestive stomach.",
            answerHi = "आंख या कान में दवाई/ड्रॉप्स डालने से रोज़ा नहीं टूटता, ख़्वाह हलक़ में हल्का सा ज़ायका ही क्यों न महसूस हो।",
            answerUr = "آنکھ یا کان میں قطرے ڈالنے سے روزہ نہیں ٹوٹتا، چاہے حلق میں ہلکا ذائقہ ہی کیوں نہ آئے۔",
            reference = "Fatawa al-Lajnah ad-Da'imah"
        ),
        RamadanFiqhItem(
            questionEn = "Does having a Blood Test or Injections invalidate the fast?",
            questionHi = "क्या ब्लड टेस्ट या इंजेक्शन लगवाने से रोज़ा टूटता है?",
            questionUr = "کیا بلڈ ٹیسٹ یا انجکشن لگوانے سے روزہ ٹوٹتا ہے؟",
            rulingType = "Permissible (Non-nutritive injections)",
            answerEn = "Routine blood testing and non-nutritive medical injections (intramuscular or intravenous painkillers, vaccines, antibiotics, insulin) do NOT break the fast. However, IV glucose drips or nutritional feeds that act as a substitute for food DO invalidate the fast.",
            answerHi = "ब्लड टेस्ट और दर्दनिवारक/एंटीबायोटिक/इंसुलिन इंजेक्शन से रोज़ा नहीं टूटता। लेकिन ग्लूकोज़ या ताक़त की ड्रिप जो खाने का मुतबादिल हो, उससे रोज़ा टूट जाता है।",
            answerUr = "بلڈ ٹیسٹ اور درد کش یا انسولین انجکشن سے روزہ نہیں ٹوٹتا۔ البتہ گلوکوز کی ڈرپ سے روزہ ٹوٹ جاتا ہے۔",
            reference = "Fiqh Academy Resolution 93(1/10)"
        ),
        RamadanFiqhItem(
            questionEn = "Does using Toothpaste or Miswak break the fast?",
            questionHi = "क्या टूथपेस्ट या मिस्वाक करने से रोज़ा टूटता है?",
            questionUr = "کیا ٹوتھ پیسٹ یا مسواک کرنے سے روزہ ٹوٹتا ہے؟",
            rulingType = "Miswak Sunnah; Toothpaste Permissible with care",
            answerEn = "Using Miswak is a beloved Sunnah at all times of the day. Using toothpaste is permissible provided none of the paste or foam is swallowed into the throat. It is safer to brush thoroughly before Sehri and after Iftar.",
            answerHi = "मिस्वाक करना दिन भर सुन्नत है। टूथपेस्ट से रोज़ा नहीं टूटता बशर्ते झाग या पेस्ट हलक़ के नीचे न उतरे। एहतियात यह है कि सेहरी से पहले या इफ़्तार के बाद करें।",
            answerUr = "مسواک کرنا مستحب و سنت ہے۔ ٹوتھ پیسٹ استعمال کرنا جائز ہے بشرطیکہ حلق سے نیچے نہ اترے۔",
            reference = "Sahih al-Bukhari & Fatwa Ibn Baz"
        ),
        RamadanFiqhItem(
            questionEn = "What happens if someone eats or drinks out of forgetfulness?",
            questionHi = "अगर भूलकर कुछ खा या पी लिया तो क्या रोज़ा टूटेगा?",
            questionUr = "اگر بھول کر کچھ کھا یا پی لیا تو کیا روزہ ٹوٹ جائے گا؟",
            rulingType = "Fast is 100% Valid (Allah has fed you)",
            answerEn = "The Prophet (ﷺ) said: 'Whoever forgets while he is fasting and eats or drinks, let him complete his fast, for it was Allah who fed him and gave him to drink.' No Qadha or Kaffara is required.",
            answerHi = "नबी करीम (ﷺ) ने फ़रमाया: जो भूलकर खा या पी ले, वह अपना रोज़ा पूरा करे, क्योंकि अल्लाह ने उसे खिलाया और पिलाया है। रोज़ा बिल्कुल सही है, क़ज़ा वाजिब नहीं।",
            answerUr = "رسول اللہ (ﷺ) نے فرمایا: جو بھول کر کھا پی لے وہ روزہ پورا کرے کیونکہ اللہ نے اسے کھلایا اور پلایا۔ روزہ بالکل درست ہے۔",
            reference = "Sahih al-Bukhari 1933, Muslim 1155"
        ),
        RamadanFiqhItem(
            questionEn = "Does swallowing saliva break the fast?",
            questionHi = "क्या अपना थूक (Saliva) निगलने से रोज़ा टूटता है?",
            questionUr = "کیا اپنا تھوک نگلنے سے روزہ ٹوٹتا ہے؟",
            rulingType = "Permissible (Consensus of Scholars)",
            answerEn = "Swallowing one's own natural saliva does not break the fast by unanimous consensus of scholars, as it is natural and unavoidable.",
            answerHi = "मुंह का सामान्य थूक निगलने से रोज़ा बिल्कुल नहीं टूटता, इस पर तमाम उम्मत का इज्माअ है।",
            answerUr = "اپنا تھوک نگلنے سے روزہ بالکل نہیں ٹوٹتا، اس پر تمام ائمہ کا اجماع ہے۔",
            reference = "Al-Mughni by Ibn Qudamah"
        )
    )

    // ── City Dhikr Leaderboard Simulation Data ──
    val featuredCities = listOf(
        "Rajnandgaon",
        "Raipur",
        "Bhilai",
        "Bilaspur",
        "Nagpur",
        "Hyderabad",
        "Delhi",
        "Mumbai",
        "Lucknow",
        "Bangalore",
        "Srinagar",
        "Kolkata"
    )

    fun getCityLeaderboard(cityName: String): List<CityLeaderboardItem> {
        return listOf(
            CityLeaderboardItem(1, "Sufiyan_786", cityName, 18450),
            CityLeaderboardItem(2, "Banda-e-Khuda", cityName, 15200),
            CityLeaderboardItem(3, "Talib-e-Ilm", cityName, 12890),
            CityLeaderboardItem(4, "Khadim-ul-Quran", cityName, 11450),
            CityLeaderboardItem(5, "Aap (You)", cityName, 4250, isCurrentUser = true),
            CityLeaderboardItem(6, "Noor_e_Madina", cityName, 3890),
            CityLeaderboardItem(7, "Abdullah", cityName, 2750),
            CityLeaderboardItem(8, "Musafir_786", cityName, 2100)
        )
    }
}
