package com.tasbih.digitaltasbih.data.hajj

data class HajjDua(
    val id: String,
    val titleHindi: String,
    val titleUrdu: String,
    val titleEnglish: String,
    val arabicText: String,
    val transliteration: String,
    val meaningHindi: String,
    val meaningUrdu: String,
    val meaningEnglish: String,
    val contextHindi: String,
    val contextEnglish: String,
    val hadithSource: String,
    val hadithSourceEnglish: String = hadithSource
)

data class HajjDayStage(
    val dayNumber: Int,
    val dateHijri: String,
    val titleHindi: String,
    val titleUrdu: String,
    val titleEnglish: String,
    val locationHindi: String,
    val locationEnglish: String,
    val summaryHindi: String,
    val summaryEnglish: String,
    val keyRitualsHindi: List<String>,
    val keyRitualsEnglish: List<String>,
    val quranHadithReference: String,
    val quranHadithReferenceEnglish: String = quranHadithReference
)

data class HajjPillarRequirement(
    val id: String,
    val isRukn: Boolean, // true = Rukn (Core Pillar), false = Wajib (Obligatory)
    val titleHindi: String,
    val titleUrdu: String,
    val titleEnglish: String,
    val arabicTitle: String,
    val descriptionHindi: String,
    val descriptionEnglish: String,
    val consequenceHindi: String,
    val consequenceEnglish: String,
    val reference: String,
    val referenceEnglish: String = reference
)

data class HajjHadithVirtue(
    val arabicText: String,
    val source: String,
    val sourceEnglish: String = source,
    val topicHindi: String,
    val topicEnglish: String,
    val translationHindi: String,
    val translationUrdu: String,
    val translationEnglish: String
)

object HajjData {

    val DUAS = listOf(
        HajjDua(
            id = "talbiyah",
            titleHindi = "मशहूर अज़ीम तल्बिया (तौहीद का नारा)",
            titleUrdu = "عظیم تلبیہ (توحید کا پرشکوہ نعرہ)",
            titleEnglish = "The Great Talbiyah (Proclamation of Tawheed)",
            arabicText = "لَبَّيْكَ اللّٰهُمَّ لَبَّيْكَ، لَبَّيْكَ لَا شَرِيكَ لَكَ لَبَّيْكَ، إِنَّ الْحَمْدَ وَالنِّعْمَةَ لَكَ وَالْمُلْكَ، لَا شَرِيكَ لَكَ",
            transliteration = "Labbayka Allahumma Labbayk, Labbayka Laa Shareeka Laka Labbayk, Innal-Hamda Wan-Ni'mata Laka Wal-Mulk, Laa Shareeka Lak.",
            meaningHindi = "मैं हाज़िर हूँ, ऐ अल्लाह! मैं हाज़िर हूँ। तेरा कोई शरीक नहीं, मैं हाज़िर हूँ। बेशक तमाम तारीफ़ें, नेमतें और बादशाही तेरी ही हैं, तेरा कोई शरीक नहीं।",
            meaningUrdu = "میں حاضر ہوں، اے اللہ! میں حاضر ہوں۔ تیرا کوئی شریک نہیں، میں حاضر ہوں۔ بیشک تمام تعریفیں، نعمتیں اور بادشاہی تیری ہی ہے، تیرا کوئی شریک نہیں۔",
            meaningEnglish = "Here I am, O Allah, here I am. Here I am, You have no partner, here I am. Verily, all praise, grace, and sovereignty belong to You. You have no partner.",
            contextHindi = "एहराम बांधने से लेकर 10 ज़िलहिज्जा को रमी (कंकरी मारने) तक कसरत से बुलंद आवाज़ में पढ़ें।",
            contextEnglish = "Recited repeatedly and aloud from donning Ihram until stoning Jamarat al-Aqaba on 10th Dhul-Hijjah.",
            hadithSource = "सहीह बुख़ारी (1549), सहीह मुस्लिम (1184)",
            hadithSourceEnglish = "Sahih al-Bukhari (1549), Sahih Muslim (1184)"
        ),
        HajjDua(
            id = "arafah_dua",
            titleHindi = "मैदान-ए-अराफ़ात की सबसे अफ़ज़ल दुआ",
            titleUrdu = "میدانِ عرفات کی سب سے افضل دعا",
            titleEnglish = "The Greatest Supplication on the Day of Arafah",
            arabicText = "لَا إِلٰهَ إِلَّا اللهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ",
            transliteration = "Laa ilaaha illallahu wahdahoo laa shareeka lah, lahul-mulku wa lahul-hamdu wa huwa 'ala kulli shay'in qadeer.",
            meaningHindi = "अल्लाह के सिवा कोई माबूद नहीं, वह यकता है, उसका कोई शरीक नहीं। उसी की बादशाही है और उसी के लिए सारी तारीफ़ें हैं, और वह हर चीज़ पर क़ादिर है।",
            meaningUrdu = "اللہ کے سوا کوئی معبود نہیں، وہ اکیلا ہے، اس کا کوئی شریک نہیں۔ اسی کی بادشاہت ہے اور اسی کے لیے تمام تعریفیں ہیں، اور وہ ہر چیز پر قادر ہے۔",
            meaningEnglish = "There is no deity except Allah alone, with no partner. His is the sovereignty, to Him belongs all praise, and He has power over all things.",
            contextHindi = "रसूलुल्लाह ﷺ ने फ़रमाया: सबसे बेहतरीन दुआ अराफ़ा के दिन की दुआ है।",
            contextEnglish = "The Prophet ﷺ said: 'The best supplication is the supplication on the Day of Arafah.'",
            hadithSource = "जामिअ अत-तिर्मिज़ी (3585) — सहीह सनद",
            hadithSourceEnglish = "Jami` at-Tirmidhi (3585) — Sahih"
        ),
        HajjDua(
            id = "tawaf_dua",
            titleHindi = "तवाफ़ के दौरान (रुकन-ए-यामानी से हजर-ए-असवद के बीच)",
            titleUrdu = "طواف کے دوران (رکنِ یمانی سے حجرِ اسود تک)",
            titleEnglish = "Dua between Rukn Yamani and the Black Stone during Tawaf",
            arabicText = "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ",
            transliteration = "Rabbana aatina fid-dunya hasanatan wa fil-aakhirati hasanatan wa qinaa 'adhaaban-naar.",
            meaningHindi = "ऐ हमारे रब! हमें दुनिया में भी भलाई दे और आख़िरत में भी भलाई अता फ़रमा, और हमें दोज़ख़ के अज़ाब से बचा।",
            meaningUrdu = "اے ہمارے پروردگار! ہمیں دنیا میں بھی بھلائی عطا فرما اور آخرت میں بھی بھلائی عطا فرما، اور ہمیں جہنم کے عذاب سے بچا۔",
            meaningEnglish = "Our Lord, give us in this world that which is good and in the Hereafter that which is good, and protect us from the punishment of the Fire.",
            contextHindi = "काबा शरीफ़ के हर चक्कर में रुकन-ए-यामानी और हजर-ए-असवद के दरम्यान पढ़ी जाती है।",
            contextEnglish = "Recited on every circuit of Tawaf between Rukn al-Yamani and the Black Stone (Hajar al-Aswad).",
            hadithSource = "सूरह अल-बक़रह (2:201), सुनन अबू दाऊद (1892)",
            hadithSourceEnglish = "Surah Al-Baqarah (2:201), Sunan Abi Dawud (1892)"
        ),
        HajjDua(
            id = "safa_marwa",
            titleHindi = "सफ़ा व मरवा की सई का आग़ाज़",
            titleUrdu = "صفا و مروہ کی سعی کا آغاز",
            titleEnglish = "Commencing Sa'i at Mount Safa and Marwa",
            arabicText = "إِنَّ الصَّفَا وَالْمَرْوَةَ مِنْ شَعَائِرِ اللَّهِ ۖ فَمَنْ حَجَّ الْبَيْتَ أَوِ اعْتَمَرَ فَلَا جُنَاحَ عَلَيْهِ أَنْ يَطَّوَّفَ بِهِمَا ۚ نَبْدَأُ بِمَا بَدَأَ اللَّهُ بِهِ",
            transliteration = "Innas-Safaa wal-Marwata min sha'aa'irillaah... Nabda'u bimaa bada'allaahu bih.",
            meaningHindi = "बेशक सफ़ा और मरवा अल्लाह की निशानियों में से हैं... हम उसी से आग़ाज़ करते हैं जिससे अल्लाह ने आग़ाज़ किया।",
            meaningUrdu = "بیشک صفا اور مروہ اللہ کی نشانیوں میں سے ہیں... ہم اسی سے ابتدا کرتے ہیں جس سے اللہ نے ابتدا فرمائی۔",
            meaningEnglish = "Indeed, as-Safa and al-Marwah are among the symbols of Allah... We begin with that which Allah began with.",
            contextHindi = "सफ़ा पहाड़ी पर चढ़ते वक़्त क़िबला रुख़ होकर तकबीर व तहलील के साथ पढ़ें।",
            contextEnglish = "Recited when mounting Mount Safa before beginning the seven circuits of Sa'i.",
            hadithSource = "सहीह मुस्लिम (1218)",
            hadithSourceEnglish = "Sahih Muslim (1218)"
        )
    )

    val DAYS = listOf(
        HajjDayStage(
            dayNumber = 1,
            dateHijri = "8 ज़िलहिज्जा (يوم التروية)",
            titleHindi = "पहला दिन: यौम अत-तरविया (मीना रवानगी)",
            titleUrdu = "پہلا دن: یوم الترویہ (منیٰ روانگی)",
            titleEnglish = "Day 1: Yawm at-Tarwiyah (Departure to Mina)",
            locationHindi = "मक्का मुकर्रमा → मीना",
            locationEnglish = "Makkah → Mina tent city",
            summaryHindi = "हाजी अपनी क़यामगाह से एहराम की नीयत करते हैं और ज़ुहर से पहले मीना तशरीफ़ ले जाते हैं।",
            summaryEnglish = "Pilgrims enter the state of Ihram from Makkah, recite Talbiyah, and proceed to the tent city of Mina before Dhuhr.",
            keyRitualsHindi = listOf(
                "ग़ुस्ल करके एहराम पहनना और 'लब्बैक अल्लाहुम्मा हज' की नीयत करना।",
                "मीना में ज़ुहर, अस्र, मग़रिब, इशा और 9 ज़िलहिज्जा की फ़ज्र अपने वक़्त पर क़स्र (2-2 रकअत) पढ़ना।",
                "पूरी रात मीना के ख़ेमों में ज़िक्र, तिलावते क़ुरआन और इबादत में गुज़ारना।"
            ),
            keyRitualsEnglish = listOf(
                "Perform Ghusl, put on Ihram garments, and make intention for Hajj with Talbiyah.",
                "Pray Dhuhr, Asr, Maghrib, Isha, and Fajr of 9th shortened (Qasr) at their designated times.",
                "Spend the entire night in Mina in dhikr, Quran recitation, and rest."
            ),
            quranHadithReference = "सहीह बुख़ारी (1653), सहीह मुस्लिम (1218)",
            quranHadithReferenceEnglish = "Sahih al-Bukhari (1653), Sahih Muslim (1218)"
        ),
        HajjDayStage(
            dayNumber = 2,
            dateHijri = "9 ज़िलहिज्जा (يوم عرفة)",
            titleHindi = "दूसरा दिन: यौम-ए-अराफ़ा (हज का सबसे बड़ा रुक्न)",
            titleUrdu = "دوسرا دن: یومِ عرفہ (حج کا سب سے بڑا رکن)",
            titleEnglish = "Day 2: Day of Arafah (The Pinnacle of Hajj)",
            locationHindi = "मीना → मैदान-ए-अराफ़ात → मुज़दलिफ़ा",
            locationEnglish = "Mina → Plain of Arafat → Muzdalifah",
            summaryHindi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'अल-हज्जु अराफ़ा' (हज ही अराफ़ात में ठहरना है)। यहाँ रो-रोकर मग़फ़िरत मांगना हज का असल मक़सद है।",
            summaryEnglish = "The Prophet ﷺ declared: 'Al-Hajju Arafah' (Hajj is Arafah). The most sacred day of forgiveness, tears, and standing before Allah.",
            keyRitualsHindi = listOf(
                "सूरज निकलने के बाद मीना से अराफ़ात की तरफ़ रवाना होना।",
                "मस्जिद-ए-नमिरा में ख़ुत्बा सुनना और ज़ुहर व अस्र जमा तक़दीम (एक साथ) अदा करना।",
                "ज़वाल के बाद से सूरज डूबने तक वुक़ूफ़-ए-अराफ़ा (क़िबला रुख़ खड़े होकर गिड़गिड़ाकर दुआएं मांगना)।",
                "ग़ुरूब-ए-आफ़ताब के बाद बग़ैर मग़रिब पढ़े मुज़दलिफ़ा रवाना होना।",
                "मुज़दलिफ़ा पहुँचकर मग़रिब और इशा एक साथ पढ़ना और खुले आसमान तले रात गुज़ारना तथा कंकरियां चुनना।"
            ),
            keyRitualsEnglish = listOf(
                "Proceed from Mina to the Plains of Arafat after sunrise.",
                "Listen to the Khutbah at Masjid Namirah; combine and shorten Dhuhr and Asr.",
                "Stand in deep supplication (Wuqoof) facing Qibla until the sun sets.",
                "Depart for Muzdalifah peacefully after sunset without praying Maghrib at Arafat.",
                "Combine Maghrib & Isha at Muzdalifah, sleep under open sky, and collect pebbles."
            ),
            quranHadithReference = "सूरह अल-बक़रह (2:198), जामिअ अत-तिर्मिज़ी (889)",
            quranHadithReferenceEnglish = "Surah Al-Baqarah (2:198), Jami` at-Tirmidhi (889)"
        ),
        HajjDayStage(
            dayNumber = 3,
            dateHijri = "10 ज़िलहिज्जा (يوم النحر / عيد الأضحى)",
            titleHindi = "तीसरा दिन: यौम अन-नह्र (क़ुरबानी व तवाफ़े इफ़ाज़ा)",
            titleUrdu = "تیسرا دن: یوم النحر (قربانی و طوافِ افاضہ)",
            titleEnglish = "Day 3: Yawm an-Nahr (Eid al-Adha & 4 Major Acts)",
            locationHindi = "मुज़दलिफ़ा → मीना (जम्रतुल अक़बा) → मक्का मुकर्रमा",
            locationEnglish = "Muzdalifah → Mina Jamarat → Makkah Haram",
            summaryHindi = "हज का सबसे व्यस्त और मुक़द्दस दिन जिसमें 4 बड़े आमाल अंजाम दिए जाते हैं।",
            summaryEnglish = "The most significant and eventful day of Hajj, encompassing four major mandatory rites.",
            keyRitualsHindi = listOf(
                "फ़ज्र के बाद मुज़दलिफ़ा में मशअरुल हराम के पास वुक़ूफ़ करना और सूरज निकलने से पहले मीना लौटना।",
                "जम्रतुल अक़बा (बड़ा शैतान) को 7 कंकरियां मारना (हर कंकरी पर अल्लाहु अकबर कहना)। तल्बिया बंद करना।",
                "हज की क़ुरबानी (हदी) ज़बह करना।",
                "हलक़ (सर मुंडवाना) या तक़सीर (बाल छोटे कराना)—इसके बाद पहला तहल्लुल (एहराम की पाबंदियां खुलना)।",
                "मक्का जाकर 'तवाफ़-ए-इफ़ाज़ा' (हज का फ़र्ज़ तवाफ़) और सफ़ा-मरवा की सई करना। दूसरा (मुकम्मल) तहल्लुल।"
            ),
            keyRitualsEnglish = listOf(
                "Pray Fajr at Muzdalifah, supplicate near Mash'ar al-Haram, depart for Mina before sunrise.",
                "Stone Jamarat al-Aqaba (large pillar) with 7 pebbles, saying Takbir with each throw; cease Talbiyah.",
                "Slaughter the sacrificial animal (Hadi).",
                "Shave (Halq) or trim (Taqseer) hair — attains the first partial Tahallul.",
                "Proceed to Haram for Tawaf al-Ifadah (obligatory Hajj Tawaf) and Sa'i — attains complete Tahallul."
            ),
            quranHadithReference = "सूरह अल-हज (22:29), सहीह बुख़ारी (1732)",
            quranHadithReferenceEnglish = "Surah Al-Hajj (22:29), Sahih al-Bukhari (1732)"
        ),
        HajjDayStage(
            dayNumber = 4,
            dateHijri = "11 ज़िलहिज्जा (أيام التشريق 1)",
            titleHindi = "चौथा दिन: अय्याम अत-तशरीक़ (तीनों जम्रात पर रमी)",
            titleUrdu = "چوتھا دن: ایام التشریق اول (تینوں جمرات پر رامی)",
            titleEnglish = "Day 4: Tashreeq 1st Day (Stoning the 3 Jamarat)",
            locationHindi = "मीना के ख़ेमे",
            locationEnglish = "Mina Tent Encampment",
            summaryHindi = "मीना में क़याम और ज़वाल के बाद तीनों जम्रात को तरतीब से कंकरियां मारना और दुआएं करना।",
            summaryEnglish = "Remaining in Mina for dhikr, brotherhood, and stoning all three pillars in sequence after zenith.",
            keyRitualsHindi = listOf(
                "मीना में 5 वक़्त की नमाज़ें अदा करना और अल्लाह की बड़ाई बयान करना।",
                "ज़वाल (ज़ुहर का वक़्त शुरू होने) के बाद तीनों जम्रात को 7-7 (कुल 21) कंकरियां मारना:",
                "1. जम्रह ऊला (छोटा) पर 7 कंकरियां और क़िबला रुख़ होकर लंबी दुआ।",
                "2. जम्रह वुस्ता (दरमियाना) पर 7 कंकरियां और लंबी दुआ।",
                "3. जम्रह अक़बा (बड़ा) पर 7 कंकरियां (यहाँ ठहरकर दुआ नहीं की जाती)।"
            ),
            keyRitualsEnglish = listOf(
                "Offer the five daily prayers in Mina tents and engage in continuous Takbeerat.",
                "After Dhuhr zenith, stone all 3 pillars with 7 pebbles each (21 total) in strict sequence:",
                "1. Jamrat al-Sughra (small) followed by long dua facing Qibla.",
                "2. Jamrat al-Wusta (middle) followed by long dua facing Qibla.",
                "3. Jamrat al-Aqaba (large) without stopping for dua afterward."
            ),
            quranHadithReference = "सूरह अल-बक़रह (2:203), सुनन अबू दाऊद (1968)",
            quranHadithReferenceEnglish = "Surah Al-Baqarah (2:203), Sunan Abi Dawud (1968)"
        ),
        HajjDayStage(
            dayNumber = 5,
            dateHijri = "12 ज़िलहिज्जा (أيام التشريق 2)",
            titleHindi = "पांचवां दिन: दूसरा तशरीक़ व जल्दी रवानगी की रुख़सत",
            titleUrdu = "پانچواں دن: ایام التشریق دوم و رخصتِ تعجّل",
            titleEnglish = "Day 5: Tashreeq 2nd Day (Stoning & Option to Depart)",
            locationHindi = "मीना → मक्का मुकर्रमा",
            locationEnglish = "Mina → Makkah",
            summaryHindi = "ज़वाल के बाद तीनों जम्रात को फिर 21 कंकरियां मारना। जो चाहें सूरज डूबने से पहले मक्का रवाना हो सकते हैं।",
            summaryEnglish = "Repeat stoning of all 3 Jamarat after Dhuhr. Pilgrims have the option to depart Mina before sunset.",
            keyRitualsHindi = listOf(
                "ज़वाल के बाद तीनों जम्रात को तरतीब से 7-7 कंकरियां मारना।",
                "अल्लाह तआला ने इजाज़त दी है कि जो 12 ज़िलहिज्जा को सूरज डूबने से पहले मीना छोड़ना चाहे वह जा सकता है (सूरह बक़रह 203)।",
                "अगर सूरज डूबने तक मीना में रह गए तो 13वीं ज़िलहिज्जा की रात भी मीना में गुज़ारना लाज़िम हो जाएगा।"
            ),
            keyRitualsEnglish = listOf(
                "Stone all three Jamarat with 7 pebbles each after Dhuhr in the same sequence.",
                "Pilgrims may choose to depart Mina before sunset (Ta'ajjul), as sanctioned in Surah Al-Baqarah 203.",
                "If sunset overtakes the pilgrim in Mina, staying for the 13th night becomes obligatory."
            ),
            quranHadithReference = "सूरह अल-बक़रह (2:203), सहीह मुस्लिम (1297)",
            quranHadithReferenceEnglish = "Surah Al-Baqarah (2:203), Sahih Muslim (1297)"
        ),
        HajjDayStage(
            dayNumber = 6,
            dateHijri = "13 ज़िलहिज्जा (الوداع - طواف الوداع)",
            titleHindi = "छठा दिन: आख़िरी रमी और तवाफ़-ए-विदा (अल-विदा)",
            titleUrdu = "چھٹا دن: آخری رامی اور طوافِ وداع",
            titleEnglish = "Day 6: Final Stoning & Tawaf al-Wada' (Farewell)",
            locationHindi = "मीना → मस्जिद-ए-हराम (काबा शरीफ़)",
            locationEnglish = "Mina → Holy Ka'bah at Masjid al-Haram",
            summaryHindi = "मीना से आख़िरी रवानगी के बाद अपने वतन लौटने से पहले काबा शरीफ़ का तवाफ़-ए-विदा अदा करना।",
            summaryEnglish = "The final concluding rite: circling the Holy Kaaba in farewell before returning to home countries.",
            keyRitualsHindi = listOf(
                "13 ज़िलहिज्जा को ज़वाल के बाद आख़िरी बार तीनों जम्रात को 7-7 कंकरियां मारना।",
                "मीना को अलविदा कहकर मक्का मुकर्रमा तशरीफ़ लाना।",
                "सफ़र पर निकलने से पहले 'तवाफ़-ए-विदा' करना (यह आफ़ाक़ी हाजियों पर वाजिब है)।",
                "जमजम का पानी पीना, मुल्तज़म पर सीना लगाकर मग़फ़िरत और ईमान की सलामती की दुआएं करना।"
            ),
            keyRitualsEnglish = listOf(
                "Perform final stoning of 3 Jamarat with 7 pebbles each after zenith.",
                "Depart Mina and proceed to Masjid al-Haram in Makkah.",
                "Perform Tawaf al-Wada' (Farewell Circumambulation) as the final act before leaving Makkah.",
                "Drink Zamzam water and make heartfelt prayers for acceptance and a blessed life."
            ),
            quranHadithReference = "सहीह बुख़ारी (1755), सहीह मुस्लिम (1328)",
            quranHadithReferenceEnglish = "Sahih al-Bukhari (1755), Sahih Muslim (1328)"
        )
    )

    val PILLARS_AND_WAJIBAT = listOf(
        // 4 Arkaan (Core Pillars)
        HajjPillarRequirement(
            id = "rukn_1",
            isRukn = true,
            titleHindi = "1. एहराम (नीयत व लब्बैक)",
            titleUrdu = "۱. احرام (نیت و تلبیہ)",
            titleEnglish = "1. Al-Ihram (Sacred State & Intention)",
            arabicTitle = "الإِحْرَام",
            descriptionHindi = "हज की नीयत दिल से करना और तल्बिया पुकारते हुए एहराम की मुक़द्दस हालत में दाख़िल होना।",
            descriptionEnglish = "Entering the sacred sanctified state with heartfelt intention (Niyyah) and verbal Talbiyah.",
            consequenceHindi = "इसके बग़ैर हज का आग़ाज़ ही नहीं होता। कोई दम या फ़िद्या इसकी भरपाई नहीं कर सकता।",
            consequenceEnglish = "Without Ihram, Hajj does not begin; no sacrifice or compensation can replace it.",
            reference = "सहीह बुख़ारी (1)",
            referenceEnglish = "Sahih al-Bukhari (1)"
        ),
        HajjPillarRequirement(
            id = "rukn_2",
            isRukn = true,
            titleHindi = "2. वुक़ूफ़-ए-अराफ़ात (अराफ़ात में ठहरना)",
            titleUrdu = "۲. وقوفِ عرفات (عرفات میں قیام)",
            titleEnglish = "2. Wuqoof at Arafat (Standing at Arafat)",
            arabicTitle = "الْوُقُوفُ بِعَرَفَة",
            descriptionHindi = "9 ज़िलहिज्जा के ज़वाल से लेकर 10 ज़िलहिज्जा की सुबह सादिक़ तक किसी भी लम्हे अराफ़ात की हुदूद में हाज़िरी देना।",
            descriptionEnglish = "Being physically present within the boundaries of Arafat anytime between noon of 9th and dawn of 10th Dhul-Hijjah.",
            consequenceHindi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'हज अराफ़ा ही है'। अगर किसी का अराफ़ात छूट जाए तो उसका हज बातिल हो गया।",
            consequenceEnglish = "The Prophet ﷺ said: 'Hajj is Arafah'. Missing Arafat completely invalidates the Hajj.",
            reference = "जामिअ अत-तिर्मिज़ी (889)",
            referenceEnglish = "Jami` at-Tirmidhi (889)"
        ),
        HajjPillarRequirement(
            id = "rukn_3",
            isRukn = true,
            titleHindi = "3. तवाफ़-ए-ज़ियारत / इफ़ाज़ा",
            titleUrdu = "۳. طوافِ زیارت / افاضہ",
            titleEnglish = "3. Tawaf al-Ifadah (Obligatory Hajj Tawaf)",
            arabicTitle = "طَوَافُ الإِفَاضَة",
            descriptionHindi = "10 ज़िलहिज्जा या अय्याम-ए-नह्र में बैतुल्लाह शरीफ़ के 7 फेरे (तवाफ़) मुकम्मल करना।",
            descriptionEnglish = "Performing the 7 circuits around the Holy Ka'bah on or after the 10th of Dhul-Hijjah.",
            consequenceHindi = "यह हज का फ़र्ज़ तवाफ़ है। जब तक यह अदा न हो, हज मुकम्मल नहीं होता और मुकम्मल तहल्लुल हासिल नहीं होता।",
            consequenceEnglish = "Core pillar ordained directly by Quran (Surah Hajj 22:29); Hajj remains unfulfilled until done.",
            reference = "सूरह अल-हज (22:29)",
            referenceEnglish = "Surah Al-Hajj (22:29)"
        ),
        HajjPillarRequirement(
            id = "rukn_4",
            isRukn = true,
            titleHindi = "4. सफ़ा व मरवा की सई",
            titleUrdu = "۴. صفا و مروہ کی سعی",
            titleEnglish = "4. Sa'i between Safa and Marwah",
            arabicTitle = "السَّعْيُ بَيْنَ الصَّفَا وَالْمَرْوَة",
            descriptionHindi = "हज़रत हाजरा अलैहिस्सलाम की सुन्नत में सफ़ा और मरवा की दो पहाड़ियों के दरमियान 7 चक्कर लगाना।",
            descriptionEnglish = "Walking and hastening 7 times between the hills of Safa and Marwah following Sayyidah Hajar (AS).",
            consequenceHindi = "जमहूर उलमा के नज़दीक यह हज का रुक्न है, जिसके बग़ैर हज मुकम्मल नहीं होता।",
            consequenceEnglish = "Prophet ﷺ instructed: 'Perform Sa'i, for verily Allah has decreed Sa'i upon you' (Ahmad 27380).",
            reference = "मुसनद अहमद (27380)",
            referenceEnglish = "Musnad Ahmad (27380)"
        ),

        // 7 Wajibat (Obligatory Acts)
        HajjPillarRequirement(
            id = "wajib_1",
            isRukn = false,
            titleHindi = "1. मीक़ात से एहराम बांधना",
            titleUrdu = "۱. میقات سے احرام باندھنا",
            titleEnglish = "1. Donning Ihram from designated Miqat",
            arabicTitle = "الإِحْرَامُ مِنَ الْمِيقَات",
            descriptionHindi = "मुअय्यन मीक़ात (जैसे ज़ुल हुलैफ़ा, यलमलम, जुहफ़ा व़ग़ैरह) से या उससे पहले एहराम में दाख़िल होना।",
            descriptionEnglish = "Crossing the Miqat boundary only while already in the state of Ihram.",
            consequenceHindi = "बग़ैर एहराम मीक़ात पार करने पर वापस मीक़ात लौटना ज़रूरी है, वरना दम (क़ुरबानी) लाज़िम आता है।",
            consequenceEnglish = "Crossing without Ihram requires returning to Miqat or sacrificing a sheep (Damm).",
            reference = "सहीह बुख़ारी (1524)",
            referenceEnglish = "Sahih al-Bukhari (1524)"
        ),
        HajjPillarRequirement(
            id = "wajib_2",
            isRukn = false,
            titleHindi = "2. अराफ़ात में ग़ुरूब-ए-आफ़ताब तक ठहरना",
            titleUrdu = "۲. عرفات میں غروبِ آفتاب تک قیام",
            titleEnglish = "2. Remaining at Arafat until Sunset",
            arabicTitle = "الْبَقَاءُ بِعَرَفَةَ إِلَى الْغُرُوب",
            descriptionHindi = "दिन में अराफ़ात पहुँचने वाले हाजी के लिए सूरज डूबने तक अराफ़ात में ठहरना वाजिब है।",
            descriptionEnglish = "Remaining in the Plains of Arafat until the sun completely sets below the horizon on 9th Dhul-Hijjah.",
            consequenceHindi = "सूरज डूबने से पहले निकलने पर दम लाज़िम होता है।",
            consequenceEnglish = "Departing prematurely before sunset incurs an obligatory compensatory sacrifice.",
            reference = "सहीह मुस्लिम (1218)",
            referenceEnglish = "Sahih Muslim (1218)"
        ),
        HajjPillarRequirement(
            id = "wajib_3",
            isRukn = false,
            titleHindi = "3. मुज़दलिफ़ा में रात गुज़ारना",
            titleUrdu = "۳. مزدلفہ میں رات گزارنا",
            titleEnglish = "3. Spending the Night in Muzdalifah",
            arabicTitle = "الْمَبِيتُ بِمُزْدَلِفَة",
            descriptionHindi = "9 ज़िलहिज्जा की रात अराफ़ात से निकलकर मुज़दलिफ़ा में आधी रात या सुबह सादिक़ तक ठहरना।",
            descriptionEnglish = "Staying the night at Muzdalifah after descending from Arafat until after midnight or dawn.",
            consequenceHindi = "कमज़ोरों व बीमारों को आधी रात के बाद मीना जाने की रुख़सत है, दूसरों के लिए छोड़ना दम वाजिब करता है।",
            consequenceEnglish = "Exemption granted to the elderly and weak; otherwise omission requires a sacrifice.",
            reference = "सूरह अल-बक़रह (2:198)",
            referenceEnglish = "Surah Al-Baqarah (2:198)"
        ),
        HajjPillarRequirement(
            id = "wajib_4",
            isRukn = false,
            titleHindi = "4. तशरीक़ की रातें मीना में गुज़ारना",
            titleUrdu = "۴. تشریق کی راتیں منیٰ میں گزارنا",
            titleEnglish = "4. Spending Tashreeq Nights in Mina",
            arabicTitle = "الْمَبِيتُ بِمِنًى لَيَالِي التَّشْرِيق",
            descriptionHindi = "11 और 12 ज़िलहिज्जा की रातें मीना के ख़ेमों में गुज़ारना।",
            descriptionEnglish = "Residing overnight in the tent city of Mina on the 11th and 12th nights of Dhul-Hijjah.",
            consequenceHindi = "बग़ैर शरई उज़्र के रात मीना से बाहर गुज़ारने पर फ़िद्या/दम लाज़िम आता है।",
            consequenceEnglish = "Omitting without a recognized excuse requires a penalty in Islamic jurisprudence.",
            reference = "सहीह बुख़ारी (1745)",
            referenceEnglish = "Sahih al-Bukhari (1745)"
        ),
        HajjPillarRequirement(
            id = "wajib_5",
            isRukn = false,
            titleHindi = "5. तरतीब से जम्रात पर रमी (कंकरियां मारना)",
            titleUrdu = "۵. ترتیب سے جمرات پر رامی (کنکریاں مارنا)",
            titleEnglish = "5. Stoning Jamarat in Sequential Order",
            arabicTitle = "رَمْيُ الْجِمَارِ بِالتَّرْتِيب",
            descriptionHindi = "10 तारीख़ को जम्रह अक़बा और 11, 12, 13 को छोटे से बड़े की तरतीब से कंकरियां मारना।",
            descriptionEnglish = "Stoning Jamarat al-Aqaba on 10th and all 3 pillars sequentially (small, medium, large) on Tashreeq days.",
            consequenceHindi = "रमी छोड़ने पर दम वाजिब होता है।",
            consequenceEnglish = "Failure to stone the pillars incurs a penalty sacrifice.",
            reference = "सहीह मुस्लिम (1299)",
            referenceEnglish = "Sahih Muslim (1299)"
        ),
        HajjPillarRequirement(
            id = "wajib_6",
            isRukn = false,
            titleHindi = "6. हलक़ या तक़सीर (बाल मुंडवाना या कटवाना)",
            titleUrdu = "۶. حلق یا تقصیر (بال منڈوانا یا کٹوانا)",
            titleEnglish = "6. Halq (Shaving) or Taqseer (Trimming)",
            arabicTitle = "الْحَلْقُ أَوِ التَّقْصِير",
            descriptionHindi = "मर्दों के लिए सर मुंडवाना (अफ़ज़ल) या चारों तरफ़ से बाल बराबर तराशना, और औरतों के लिए उंगली के पोर बराबर बाल काटना।",
            descriptionEnglish = "Shaving head cleanly for men (most rewarded) or trimming hair equally; trimming one fingertip length for women.",
            consequenceHindi = "रसूलुल्लाह ﷺ ने सर मुंडवाने वालों के लिए 3 बार मग़फ़िरत की दुआ फ़रमाई।",
            consequenceEnglish = "The Prophet ﷺ supplicated thrice: 'O Allah, forgive those who shave their heads' (Bukhari 1727).",
            reference = "सहीह बुख़ारी (1727)",
            referenceEnglish = "Sahih al-Bukhari (1727)"
        ),
        HajjPillarRequirement(
            id = "wajib_7",
            isRukn = false,
            titleHindi = "7. तवाफ़-ए-विदा (अलविदा तवाफ़)",
            titleUrdu = "۷. طوافِ وداع (رخصت طواف)",
            titleEnglish = "7. Tawaf al-Wada' (Farewell Circumambulation)",
            arabicTitle = "طَوَافُ الْوَدَاع",
            descriptionHindi = "हुदूद-ए-हरम से बाहर रहने वाले हाजियों के लिए मक्का छोड़ने से पहले आख़िरी तवाफ़।",
            descriptionEnglish = "Final circuit of the Holy Ka'bah before embarking on the journey back home.",
            consequenceHindi = "हैज़ व निफ़ास वाली औरतों के अलावा जो छोड़ दे उस पर दम लाज़िम होता है।",
            consequenceEnglish = "Obligatory for all non-resident pilgrims; menstruating women are exempt without penalty.",
            reference = "सहीह बुख़ारी (1755)",
            referenceEnglish = "Sahih al-Bukhari (1755)"
        )
    )

    val VIRTUES = listOf(
        HajjHadithVirtue(
            arabicText = "الْحَجُّ الْمَبْرُورُ لَيْسَ لَهُ جَزَاءٌ إِلَّا الْجَنَّةُ",
            source = "सहीह बुख़ारी (1773), सहीह मुस्लिम (1349)",
            sourceEnglish = "Sahih al-Bukhari (1773), Sahih Muslim (1349)",
            topicHindi = "मक़बूल हज का बदला सिर्फ़ जन्नत है",
            topicEnglish = "An Accepted Hajj Has No Reward Except Jannah",
            translationHindi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'उमरा दूसरे उमरे तक के गुनाहों का कफ़्फ़ारा है, और हज-ए-मबरूर (मक़बूल हज) का बदला सिर्फ़ और सिर्फ़ जन्नत ही है।'",
            translationUrdu = "رسول اللہ ﷺ نے فرمایا: 'عمرہ دوسرے عمرے تک کے گناہوں کا کفارہ ہے، اور حجِ مبرور (مقبول حج) کا بدلہ صرف اور صرف جنت ہی ہے۔'",
            translationEnglish = "The Messenger of Allah ﷺ said: 'Umrah to Umrah is an expiation for what is between them, and an accepted Hajj (Hajj Mabrur) has no reward except Paradise.'"
        ),
        HajjHadithVirtue(
            arabicText = "مَنْ حَجَّ لِلَّهِ فَلَمْ يَرْفُثْ وَلَمْ يَفْسُقْ رَجَعَ كَيَوْمِ وَلَدَتْهُ أُمُّهُ",
            source = "सहीह बुख़ारी (1521), सहीह मुस्लिम (1350)",
            sourceEnglish = "Sahih al-Bukhari (1521), Sahih Muslim (1350)",
            topicHindi = "गुनाहों से नवजात बच्चे की तरह पाक-साफ़ होना",
            topicEnglish = "Purified of All Sins Like a Newborn Baby",
            translationHindi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'जिसने अल्लाह के लिए हज किया, न बेहूदा बात की और न कोई गुनाह किया, वह गुनाहों से ऐसा पाक होकर लौटता है जैसे उस दिन था जब उसकी माँ ने उसे जना था।'",
            translationUrdu = "رسول اللہ ﷺ نے فرمایا: 'جس نے اللہ کے لیے حج کیا، نہ بےہودہ بات کی اور نہ کوئی گناہ کیا، وہ گناہوں سے ایسا پاک ہو کر لوٹتا ہے جیسے اس دن تھا جب اس کی ماں نے اسے جنا تھا۔'",
            translationEnglish = "The Messenger of Allah ﷺ said: 'Whoever performs Hajj for Allah's sake, avoiding all obscenity and wickedness, returns as sinless as the day his mother gave birth to him.'"
        ),
        HajjHadithVirtue(
            arabicText = "تَابِعُوا بَيْنَ الْحَجِّ وَالْعُمْرَةِ فَإِنَّهُمَا يَنْفِيَانِ الْفَقْرَ وَالذُّنُوبَ كَمَا يَنْفِي الْكِيرُ خَبَثَ الْحَدِيدِ وَالذَّهَبِ وَالْفِضَّةِ",
            source = "जामिअ अत-तिर्मिज़ी (810), सुनन अन-नसाई (2631)",
            sourceEnglish = "Jami` at-Tirmidhi (810), Sunan an-Nasa'i (2631)",
            topicHindi = "हज व उमरा ग़रीबी और गुनाहों को मिटा देते हैं",
            topicEnglish = "Hajj & Umrah Eliminate Poverty and Evils",
            translationHindi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'हज और उमरा एक के बाद दूसरा करते रहा करो, क्योंकि ये दोनों फ़क़्र व तंगदस्ती और गुनाहों को इस तरह दूर कर देते हैं जैसे भट्टी लोहे, सोने और चांदी के मैल को दूर कर देती है।'",
            translationUrdu = "رسول اللہ ﷺ نے فرمایا: 'حج اور عمرہ پے در پے کرتے رہا کرو، کیونکہ یہ دونوں فقر اور گناہوں کو اس طرح دور کر دیتے ہیں جیسے بھٹی لوہے، سونے اور چاندی کے میل کو دور کر دیتی ہے۔'",
            translationEnglish = "The Messenger of Allah ﷺ said: 'Alternate between Hajj and Umrah, for they remove poverty and sins just as the furnace removes the dross of iron, gold, and silver.'"
        ),
        HajjHadithVirtue(
            arabicText = "مَا مِنْ يَوْمٍ أَكْثَرَ مِنْ أَنْ يُعْتِقَ اللهُ فِيهِ عَبْدًا مِنَ النَّارِ مِنْ يَوْمِ عَرَفَةَ",
            source = "सहीह मुस्लिम (1348)",
            sourceEnglish = "Sahih Muslim (1348)",
            topicHindi = "यौम-ए-अराफ़ा पर लाखों बंदों की दोज़ख़ से रिहाई",
            topicEnglish = "Vast Forgiveness on the Day of Arafah",
            translationHindi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'अल्लाह तआला अराफ़ा के दिन से ज़्यादा किसी और दिन अपने बंदों को जहन्नम की आग से आज़ाद नहीं फ़रमाता। अल्लाह करीब होता है और फ़रिश्तों के सामने उन पर फ़ख़्र फ़रमाता है।'",
            translationUrdu = "رسول اللہ ﷺ نے فرمایا: 'اللہ تعالیٰ عرفہ کے دن سے زیادہ کسی اور دن اپنے بندوں کو جہنم کی آگ سے آزاد نہیں فرماتا۔ اللہ قریب ہوتا ہے اور فرشتوں کے سامنے ان پر فخر فرماتا ہے۔'",
            translationEnglish = "The Messenger of Allah ﷺ said: 'There is no day on which Allah frees more slaves from the Fire than the Day of Arafah. He draws near and boasts of them before the angels.'"
        )
    )
}

