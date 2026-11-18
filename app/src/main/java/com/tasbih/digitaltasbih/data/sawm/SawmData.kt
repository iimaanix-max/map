package com.tasbih.digitaltasbih.data.sawm

data class SawmDua(
    val id: String,
    val titleHindi: String,
    val titleUrdu: String,
    val titleEnglish: String,
    val arabicText: String,
    val transliteration: String,
    val meaningHindi: String,
    val meaningUrdu: String,
    val meaningEnglish: String,
    val timingHindi: String,
    val timingEnglish: String,
    val hadithSource: String,
    val hadithSourceEnglish: String = hadithSource
)

data class SawmRule(
    val id: String,
    val category: String, // "BREAKS", "DOES_NOT_BREAK", "EXEMPTION"
    val titleHindi: String,
    val titleUrdu: String,
    val titleEnglish: String,
    val detailHindi: String,
    val detailEnglish: String,
    val proof: String,
    val proofEnglish: String = proof
)

data class VoluntaryFast(
    val id: String,
    val titleHindi: String,
    val titleUrdu: String,
    val titleEnglish: String,
    val arabicTitle: String,
    val timingHindi: String,
    val timingEnglish: String,
    val rewardHindi: String,
    val rewardEnglish: String,
    val hadithSource: String,
    val hadithSourceEnglish: String = hadithSource
)

data class SawmHadithVirtue(
    val arabicText: String,
    val source: String,
    val sourceEnglish: String = source,
    val topicHindi: String,
    val topicEnglish: String,
    val translationHindi: String,
    val translationUrdu: String,
    val translationEnglish: String
)

object SawmData {

    val DUAS = listOf(
        SawmDua(
            id = "suhoor",
            titleHindi = "सहरी (रोज़ा रखने) की नीयत",
            titleUrdu = "سحری (روزہ رکھنے) کی نیت",
            titleEnglish = "Suhoor (Intention for Fasting)",
            arabicText = "وَبِصَوْمِ غَدٍ نَّوَيْتُ مِنْ شَهْرِ رَمَضَانَ",
            transliteration = "Wa bi-sawmi ghadin nawaytu min shahri Ramadan.",
            meaningHindi = "और मैंने रमज़ान के इस महीने के कल के रोज़े की नीयत की।",
            meaningUrdu = "اور میں نے ماہِ رمضان کے کل کے روزے کی نیت کی۔",
            meaningEnglish = "And I intend to keep the fast tomorrow in the month of Ramadan.",
            timingHindi = "सुबह सादिक़ से पहले सहरी खाने के बाद दिल से नीयत करें।",
            timingEnglish = "Intended before true dawn (Fajr) after eating Suhoor.",
            hadithSource = "मस्नून नीयत (सहीह बुख़ारी 1)",
            hadithSourceEnglish = "Sunnah Intention (Sahih al-Bukhari 1)"
        ),
        SawmDua(
            id = "iftar_sunnah",
            titleHindi = "इफ़्तार की सुन्नत दुआ (रसूलुल्लाह ﷺ की दुआ)",
            titleUrdu = "افطار کی مسنون دعا (رسول اللہ ﷺ کی دعا)",
            titleEnglish = "Iftar Sunnah Dua (Prophet's ﷺ Supplication)",
            arabicText = "ذَهَبَ الظَّمَأُ وَابْتَلَّتِ الْعُرُوقُ وَثَبَتَ الأَجْرُ إِنْ شَاءَ اللهُ",
            transliteration = "Dhahabadh-dhama'u wabtallatil-'urooqu wa thabatal-ajru in sha'Allah.",
            meaningHindi = "प्यास बुझ गई, रगें तर हो गईं और अल्लाह ने चाहा तो अज्र व सवाब साबित हो गया।",
            meaningUrdu = "پیاس بجھ گئی، رگیں تر ہو گئیں اور انشاءاللہ ثواب پکا ہو گیا۔",
            meaningEnglish = "The thirst is gone, the veins are moistened, and the reward is confirmed, if Allah wills.",
            timingHindi = "रोज़ा खोलने (पानी या खजूर चखने) के फ़ौरन बाद यह दुआ पढ़ें।",
            timingEnglish = "Recited immediately upon breaking the fast.",
            hadithSource = "सुनन अबू दाऊद (हदीस 2357) — सनद हसन",
            hadithSourceEnglish = "Sunan Abi Dawud (2357) — Hasan"
        ),
        SawmDua(
            id = "iftar_common",
            titleHindi = "इफ़्तार से पहले की मशहूर दुआ",
            titleUrdu = "افطار کی مشہور دعا",
            titleEnglish = "Dua before Breaking the Fast",
            arabicText = "اللّٰهُمَّ إِنِّي لَكَ صُمْتُ وَعَلَىٰ رِزْقِكَ أَفْطَرْتُ",
            transliteration = "Allahumma innee laka sumtu wa 'ala rizqika aftartu.",
            meaningHindi = "ऐ अल्लाह! मैंने तेरे ही लिए रोज़ा रखा और तेरे ही दिए हुए रिज़्क़ पर इफ़्तार किया।",
            meaningUrdu = "اے اللہ! میں نے تیرے ہی لیے روزہ رکھا اور تیرے ہی دیے ہوئے رزق پر افطار کیا۔",
            meaningEnglish = "O Allah, for You I have fasted, and with Your provision I have broken my fast.",
            timingHindi = "इफ़्तार के वक़्त दस्तरख़्वान पर बैठते समय पढ़ें।",
            timingEnglish = "Commonly recited upon sitting down for Iftar.",
            hadithSource = "सुनन अबू दाऊद (हदीस 2358)",
            hadithSourceEnglish = "Sunan Abi Dawud (2358)"
        )
    )

    val RULES = listOf(
        SawmRule(
            id = "r1",
            category = "DOES_NOT_BREAK",
            titleHindi = "भूलकर खा-पी लेना",
            titleUrdu = "بھول کر کھا پی لینا",
            titleEnglish = "Eating or Drinking Unintentionally",
            detailHindi = "अगर कोई रोज़ेदार भूलकर कुछ खा या पी ले, तो रोज़ा नहीं टूटता। याद आते ही फ़ौरन रुक जाए। अल्लाह ने उसे खिलाया और पिलाया है।",
            detailEnglish = "If someone forgets and eats or drinks, the fast remains valid. Immediately cease upon remembering.",
            proof = "सहीह बुख़ारी (हदीस 1933) व सहीह मुस्लिम (1155)",
            proofEnglish = "Sahih al-Bukhari (1933) & Sahih Muslim (1155)"
        ),
        SawmRule(
            id = "r2",
            category = "DOES_NOT_BREAK",
            titleHindi = "इत्र, सुरमा व तेल लगाना",
            titleUrdu = "عطر، سرمہ اور تیل لگانا",
            titleEnglish = "Perfume, Kohl & Hair Oil",
            detailHindi = "खुशबू सूंघना, इत्र लगाना, आँखों में सुरमा या ड्रॉप डालना और सर या बदन में तेल लगाने से रोज़ा नहीं टूटता।",
            detailEnglish = "Applying fragrance, eye drops, kohl, or oil does not invalidate the fast.",
            proof = "सहीह बुख़ारी, किताबुस-सौम",
            proofEnglish = "Sahih al-Bukhari, Book of Fasting"
        ),
        SawmRule(
            id = "r3",
            category = "DOES_NOT_BREAK",
            titleHindi = "मिस्वाक करना या कुल्ली करना",
            titleUrdu = "مسواک کرنا اور کلی کرنا",
            titleEnglish = "Using Siwak / Miswak & Rinsing",
            detailHindi = "वज़ू में कुल्ली करना और मिस्वाक करना सुन्नत है। बस पानी हलक़ के नीचे न उतरे।",
            detailEnglish = "Rinsing the mouth and using Miswak is permitted as long as water is not swallowed.",
            proof = "सुनन अबू दाऊद 2364",
            proofEnglish = "Sunan Abi Dawud (2364)"
        ),
        SawmRule(
            id = "r4",
            category = "DOES_NOT_BREAK",
            titleHindi = "इंजेक्शन या ख़ून टेस्ट",
            titleUrdu = "انجکشن یا خون کا ٹیسٹ",
            titleEnglish = "Injections & Blood Tests",
            detailHindi = "इलाज के लिए इंट्रा-मस्कुलर या नस का इंजेक्शन लेना और ख़ून की जांच कराना रोज़ा नहीं तोड़ता (जब तक वह ग्लूकोज़ या गिज़ा की ड्रिप न हो)।",
            detailEnglish = "Medical non-nutritional injections and blood sampling do not break the fast.",
            proof = "इस्लामिक फ़िक़्ह अकादमी का मुत्तफ़िक़ा फ़तवा",
            proofEnglish = "Islamic Fiqh Academy Consensus"
        ),
        SawmRule(
            id = "r5",
            category = "BREAKS",
            titleHindi = "जानबूझकर खाना या पीना",
            titleUrdu = "جان بوجھ کر کھانا یا پینا",
            titleEnglish = "Deliberate Eating or Drinking",
            detailHindi = "रोज़ा याद होने के बावजूद जानबूझकर कोई भी गिज़ा, पानी या दवा हलक़ से नीचे उतारने से रोज़ा टूट जाता है और क़ज़ा व कफ़्फ़ारा दोनों लाज़िम होते हैं।",
            detailEnglish = "Intentionally consuming food, drink, or oral medication completely invalidates the fast.",
            proof = "सूरह अल-बक़रह 2:187",
            proofEnglish = "Surah Al-Baqarah 2:187"
        ),
        SawmRule(
            id = "r6",
            category = "BREAKS",
            titleHindi = "जानबूझकर क़य (उल्टी) करना",
            titleUrdu = "جان بوجھ کر قے (الٹی) کرنا",
            titleEnglish = "Deliberately Inducing Vomiting",
            detailHindi = "अगर खुद-ब-खुद उल्टी आ जाए तो रोज़ा नहीं टूटता, लेकिन अगर जानबूझकर मुंह भरकर उल्टी की जाए तो रोज़ा टूट जाता है।",
            detailEnglish = "Spontaneous vomiting does not break fast, but deliberately inducing vomit invalidates it.",
            proof = "जामे तिर्मिज़ी 720 (सहीह)",
            proofEnglish = "Jami` at-Tirmidhi (720) — Sahih"
        ),
        SawmRule(
            id = "r7",
            category = "EXEMPTION",
            titleHindi = "सफ़र (Traveling)",
            titleUrdu = "سفر کی رخصت",
            titleEnglish = "Traveler Exemption",
            detailHindi = "शरई मुसाफ़िर (48 मील या अधिक सफ़र पर निकलने वाले) के लिए रोज़ा मुल्तवी करने की इजाज़त है, बाद में उसकी क़ज़ा रखनी होगी।",
            detailEnglish = "Travelers on a valid journey may defer their fast and make it up later.",
            proof = "सूरह अल-बक़रह 2:185",
            proofEnglish = "Surah Al-Baqarah 2:185"
        ),
        SawmRule(
            id = "r8",
            category = "EXEMPTION",
            titleHindi = "बीमारी व ज़ईफ़ उम्र (Illness & Old Age)",
            titleUrdu = "مرض اور ضعیف العمری",
            titleEnglish = "Illness & Elderly (Fidyah)",
            detailHindi = "ऐसी बीमारी जिसमें रोज़ा रखने से जान या सेहत को ख़तरा हो, छूट हासिल है। मुस्तक़िल बुज़ुर्ग जो रोज़ा रखने की ताक़त नहीं रखते, वे हर रोज़े के बदले फ़िदया अदा करें।",
            detailEnglish = "Those with severe illness may postpone; the chronically frail elderly give Fidyah instead.",
            proof = "सूरह अल-बक़रह 2:184",
            proofEnglish = "Surah Al-Baqarah 2:184"
        )
    )

    val VOLUNTARY_FASTS = listOf(
        VoluntaryFast(
            id = "shawwal",
            titleHindi = "शव्वाल के 6 रोज़े",
            titleUrdu = "شوال کے ۶ روزے",
            titleEnglish = "6 Days of Shawwal",
            arabicTitle = "سِتٌّ مِنْ شَوَّالٍ",
            timingHindi = "ईद-उल-फ़ित्र के बाद पूरे शव्वाल के महीने में कभी भी",
            timingEnglish = "Any 6 days during Shawwal after Eid al-Fitr",
            rewardHindi = "रमज़ान के बाद शव्वाल के 6 रोज़े रखने वाले को पूरे साल (ज़माने भर) रोज़ा रखने का सवाब मिलता है।",
            rewardEnglish = "Fasting Ramadan followed by six days of Shawwal is equivalent to fasting the entire year.",
            hadithSource = "सहीह मुस्लिम (हदीस 1164)",
            hadithSourceEnglish = "Sahih Muslim (1164)"
        ),
        VoluntaryFast(
            id = "arafah",
            titleHindi = "यौमे अरफ़ा (9 ज़ुल-हज्ज) का रोज़ा",
            titleUrdu = "یومِ عرفہ (۹ ذوالحجہ) کا روزہ",
            titleEnglish = "Day of Arafah (9th Dhul-Hijjah)",
            arabicTitle = "صَوْمُ يَوْمِ عَرَفَةَ",
            timingHindi = "9 ज़ुल-हज्ज (ग़ैर-हाजियों के लिए)",
            timingEnglish = "9th of Dhul Hijjah (for non-pilgrims)",
            rewardHindi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'मुझे अल्लाह की ज़ात से उम्मीद है कि यह गुज़रे हुए एक साल और आने वाले एक साल के गुनाहों का कफ़्फ़ारा बन जाता है।'",
            rewardEnglish = "Expiates the sins of the previous year and the coming year.",
            hadithSource = "सहीह मुस्लिम (हदीस 1162)",
            hadithSourceEnglish = "Sahih Muslim (1162)"
        ),
        VoluntaryFast(
            id = "ashura",
            titleHindi = "आशूरा (10 मुहर्रम) का रोज़ा",
            titleUrdu = "عاشوراء (۱۰ محرم) کا روزہ",
            titleEnglish = "Day of Ashura (10th Muharram)",
            arabicTitle = "صَوْمُ يَوْمِ عَاشُورَاءَ",
            timingHindi = "9 और 10 मुहर्रम (या 10 और 11 मुहर्रम)",
            timingEnglish = "9th & 10th (or 10th & 11th) of Muharram",
            rewardHindi = "आशूरा के रोज़े से गुज़रे हुए पूरे एक साल के गुनाह मुआफ़ हो जाते हैं। यहूदियों की मुख़ालफ़त के लिए 9 या 11 को भी साथ मिलाना सुन्नत है।",
            rewardEnglish = "Expiates the sins of the previous year.",
            hadithSource = "सहीह मुस्लिम (हदीस 1162)",
            hadithSourceEnglish = "Sahih Muslim (1162)"
        ),
        VoluntaryFast(
            id = "ayyam_beed",
            titleHindi = "अय्याम-ए-बीज़ (चांद की 13, 14, 15)",
            titleUrdu = "ایامِ بیض (قمری تاریخ ۱۳، ۱۴، ۱۵)",
            titleEnglish = "The White Days (13, 14, 15)",
            arabicTitle = "أَيَّامُ الْبِيضِ",
            timingHindi = "हर इस्लामी महीने की 13, 14 और 15 तारीख़",
            timingEnglish = "13th, 14th, and 15th of every lunar month",
            rewardHindi = "हर महीने 3 दिन रोज़ा रखना हमेशा रोज़ा रखने (सौम-ए-दहर) के बराबर है।",
            rewardEnglish = "Fasting three days every month is like fasting for a lifetime.",
            hadithSource = "सहीह बुख़ारी (हदीस 1981)",
            hadithSourceEnglish = "Sahih al-Bukhari (1981)"
        ),
        VoluntaryFast(
            id = "mon_thu",
            titleHindi = "पीर और जुमेरात (सोमवार व गुरुवार)",
            titleUrdu = "پیر اور جمعرات کے روزے",
            titleEnglish = "Mondays & Thursdays",
            arabicTitle = "صِيَامُ الاِثْنَيْنِ وَالْخَمِيسِ",
            timingHindi = "हर हफ़्ते के पीर और जुमेरात का दिन",
            timingEnglish = "Weekly on Mondays and Thursdays",
            rewardHindi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'इन दोनों दिनों में अल्लाह के सामने बंदों के आमाल पेश किए जाते हैं, और मैं चाहता हूँ कि मेरे आमाल रोज़े की हालत में पेश हों।'",
            rewardEnglish = "Deeds are presented to Allah on these two days, and the Prophet ﷺ loved fasting while deeds are presented.",
            hadithSource = "जामे तिर्मिज़ी (हदीस 747) — सहीह",
            hadithSourceEnglish = "Jami` at-Tirmidhi (747) — Sahih"
        )
    )

    val VIRTUES = listOf(
        SawmHadithVirtue(
            arabicText = "كُلُّ عَمَلِ ابْنِ آدَمَ يُضَاعَفُ الْحَسَنَةُ عَشْرُ أَمْثَالِهَا إِلَىٰ سَبْعِمِائَةِ ضِعْفٍ، قَالَ اللهُ عَزَّ وَجَلَّ: إِلَّا الصَّوْمَ، فَإِنَّهُ لِي وَأَنَا أَجْزِي بِهِ",
            source = "सहीह बुख़ारी (हदीस 1894, 1904) व सहीह मुस्लिम (1151)",
            sourceEnglish = "Sahih al-Bukhari (1894, 1904) & Sahih Muslim (1151)",
            topicHindi = "रोज़ा अल्लाह के लिए है और वही इसका बदला देगा",
            topicEnglish = "Fasting is for Allah and He Directly Rewards It",
            translationHindi = "अल्लाह तआला ने (हदीस-ए-क़ुदसी में) फ़रमाया: 'इंसान के हर अमल का बदला 10 से 700 गुना तक बढ़ाया जाता है सिवाय रोज़े के, क्योंकि वह ख़ास मेरे लिए है और मैं खुद ही उसका बदला दूँगा।' (बुख़ारी 1894)",
            translationUrdu = "اللہ تعالیٰ نے فرمایا: 'انسان کا ہر عمل ۱۰ سے ۷۰۰ گنا تک بڑھایا جاتا ہے سوائے روزے کے، کیونکہ وہ خالص میرے لیے ہے اور میں ہی اس کی جزا دوں گا۔' (بخاری)",
            translationEnglish = "Allah said in Hadith Qudsi: 'Every deed of the son of Adam is multiplied from 10 to 700 times, except fasting, for it is for Me and I will reward it.'"
        ),
        SawmHadithVirtue(
            arabicText = "الصِّيَامُ جُنَّةٌ، فَلَا يَرْفُثْ وَلَا يَجْهَلْ، وَإِنِ امْرُؤٌ قَاتَلَهُ أَوْ شَاتَمَهُ فَلْيَقُلْ: إِنِّي صَائِمٌ",
            source = "सहीह बुख़ारी (हदीस 1894) व सहीह मुस्लिम (1151)",
            sourceEnglish = "Sahih al-Bukhari (1894) & Sahih Muslim (1151)",
            topicHindi = "रोज़ा ढाल (Shield) है",
            topicEnglish = "Fasting is a Protective Shield",
            translationHindi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'रोज़ा एक ढाल है। पस रोज़ेदार न बेहूदा बातें करे और न जिहालत का काम। अगर कोई उससे लड़े या गाली दे, तो वह कह दे: मैं रोज़ेदार हूँ।' (बुख़ारी 1894)",
            translationUrdu = "رسول اللہ ﷺ نے فرمایا: 'روزہ ڈھال ہے۔ لہٰذا روزہ دار نہ بیہودہ بات کرے اور نہ جہالت۔ اگر کوئی اس سے لڑے یا گالی دے تو کہہ دے: میں روزے دار ہوں۔'",
            translationEnglish = "The Prophet ﷺ said: 'Fasting is a shield. So the fasting person should avoid obscenity and foolishness. If anyone fights or insults him, let him say: I am fasting.'"
        ),
        SawmHadithVirtue(
            arabicText = "إِنَّ فِي الْجَنَّةِ بَابًا يُقَالُ لَهُ الرَّيَّانُ، يَدْخُلُ مِنْهُ الصَّائِمُونَ يَوْمَ الْقِيَامَةِ، لَا يَدْخُلُ مِنْهُ أَحَدٌ غَيْرُهُمْ",
            source = "सहीह बुख़ारी (हदीस 1896) व सहीह मुस्लिम (1152)",
            sourceEnglish = "Sahih al-Bukhari (1896) & Sahih Muslim (1152)",
            topicHindi = "जन्नत का बाब-उर-रैय्यान दरवाज़ा",
            topicEnglish = "The Gate of Ar-Rayyan in Paradise",
            translationHindi = "'जन्नत में एक ख़ास दरवाज़ा है जिसे 'अर-रैय्यान' कहा जाता है। क़ियामत के दिन उससे सिर्फ़ रोज़ेदार ही दाख़िल होंगे। जब वे दाख़िल हो जाएँगे तो वह दरवाज़ा बंद कर दिया जाएगा।' (बुख़ारी 1896)",
            translationUrdu = "'جنت میں ایک خاص دروازہ ہے جسے الریان کہا جاتا ہے۔ قیامت کے دن اس سے صرف روزے دار داخل ہوں گے، ان کے علاوہ کوئی داخل نہیں ہوگا۔ جب وہ داخل ہو جائیں گے تو دروازہ بند کر دیا جائے گا۔'",
            translationEnglish = "'In Paradise there is a gate called Ar-Rayyan, through which only those who fasted will enter on the Day of Resurrection, and none other will enter with them.'"
        ),
        SawmHadithVirtue(
            arabicText = "لِلصَّائِمِ فَرْحَتَانِ يَفْرَحُهُمَا: إِذَا أَفْطَرَ فَرِحَ، وَإِذَا لَقِيَ رَبَّهُ فَرِحَ بِصَوْمِهِ",
            source = "सहीह बुख़ारी (हदीस 1904) व सहीह मुस्लिम (1151)",
            sourceEnglish = "Sahih al-Bukhari (1904) & Sahih Muslim (1151)",
            topicHindi = "रोज़ेदार के लिए दो खुशियाँ",
            topicEnglish = "Two Joys for the Fasting Person",
            translationHindi = "'रोज़ेदार के लिए दो बड़ी खुशियाँ हैं: एक खुशी जब वह इफ़्तार करता है, और दूसरी अज़ीम खुशी जब वह अपने रब से मुलाक़ात करेगा।' (बुख़ारी 1904)",
            translationUrdu = "'روزے دار کے لیے دو خوشیاں ہیں: ایک خوشی جب وہ افطار کرتا ہے، اور دوسری خوشی جب وہ اپنے رب سے ملاقات کرے گا۔'",
            translationEnglish = "'The fasting person has two moments of joy: one when he breaks his fast, and one when he meets his Lord and rejoices over his fasting.'"
        )
    )
}
