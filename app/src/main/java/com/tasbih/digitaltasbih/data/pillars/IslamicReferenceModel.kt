package com.tasbih.digitaltasbih.data.pillars

data class IslamicReferenceItem(
    val id: String,
    val bookOrSurah: String,           // e.g. "Sahih al-Bukhari 128" or "Surah Al-Bayyinah (98:5)"
    val grade: String = "Sahih",      // e.g. "Sahih", "Hasan", "Quranic Ayah"
    val narrator: String = "",        // e.g. "Mu'adh ibn Jabal (RA)"
    val arabicText: String = "",      // Complete Arabic text of the Hadith or Ayah
    val translationEn: String = "",   // Full English translation
    val translationHi: String = "",   // Full Hindi translation
    val translationUr: String = "",   // Full Urdu translation
    val translationAr: String = "",   // Arabic note/tafsir
    val webUrl: String = "https://sunnah.com", // Direct link to Sunnah.com or Quran.com
    val benefitOrContext: String = "" // Additional guidance
) {
    fun getTranslation(lang: String): String {
        val code = lang.lowercase().trim()
        return when {
            code.startsWith("hi") || code == "hinglish" -> translationHi.ifEmpty { translationEn }
            code.startsWith("ur") -> translationUr.ifEmpty { translationEn }
            code.startsWith("ar") -> translationAr.ifEmpty { translationEn }
            else -> translationEn.ifEmpty { translationHi }
        }
    }
}

object PillarsReferenceRepository {

    private val references = mapOf(
        // ═══════════════════════════════════════════════════════════
        // PILLAR 1: SHAHADAH REFERENCES
        // ═══════════════════════════════════════════════════════════
        "quran_47_19" to IslamicReferenceItem(
            id = "quran_47_19",
            bookOrSurah = "Surah Muhammad (47:19)",
            grade = "Quranic Ayah",
            arabicText = "فَاعْلَمْ أَنَّهُ لَا إِلَٰهَ إِلَّا اللَّهُ وَاسْتَغْفِرْ لِذَنبِكَ وَلِلْمُؤْمِنِينَ وَالْمُؤْمِنَاتِ ۗ وَاللَّهُ يَعْلَمُ مُتَقَلَّبَكُمْ وَمَثْوَاكُمْ",
            translationEn = "So know, [O Muhammad], that there is no deity except Allah and ask forgiveness for your sin and for the believing men and believing women. And Allah knows your movement and your resting place.",
            translationHi = "तो जान लो कि अल्लाह के सिवा कोई सच्चा माबूद (उपास्य) नहीं, और अपने गुनाहों की माफ़ी माँगो और मोमिन मर्दों और मोमिन औरतों के लिए भी।",
            translationUr = "پس جان لیجیے کہ اللہ کے سوا کوئی معبودِ برحق نہیں اور اپنے گناہوں کی بخشش مانگیے اور مومن مردوں اور مومن عورتوں کے لیے بھی۔",
            translationAr = "فاعلم أنه لا معبود بحق إلا الله، واستغفر لذنبك وللمؤمنين والمؤمنات.",
            webUrl = "https://quran.com/47/19"
        ),
                "quran_33_40" to IslamicReferenceItem(
            id = "quran_33_40",
            bookOrSurah = "Surah Al-Ahzab (33:40)",
            grade = "Quranic Ayah",
            narrator = "Allah (SWT)",
            arabicText = "مَّا كَانَ مُحَمَّدٌ أَبَا أَحَدٍ مِّن رِّجَالِكُمْ وَلَٰكِن رَّسُولَ اللَّهِ وَخَاتَمَ النَّبِيِّينَ ۗ وَكَانَ اللَّهُ بِكُلِّ شَيْءٍ عَلِيمًا",
            translationEn = "Muhammad is not the father of [any] one of your men, but [he is] the Messenger of Allah and the last of the prophets. And ever is Allah, of all things, Knowing.",
            translationHi = "मुहम्मद तुम्हारे मर्दों में से किसी के बाप नहीं हैं, बल्कि वे अल्लाह के रसूल और नबियों के ख़ातम (अंतिम नबी) हैं। और अल्लाह हर चीज़ का जानने वाला है।",
            translationUr = "محمد تمہارے مردوں میں سے کسی کے باپ نہیں ہیں لیکن وہ اللہ کے رسول اور خاتم النبیین ہیں۔ اور اللہ ہر چیز کا خوب جانने वाला ہے۔",
            translationAr = "مَّا كَانَ مُحَمَّدٌ أَبَا أَحَدٍ مِّن رِّجَالِكُمْ وَلَٰكِن رَّسُولَ اللَّهِ وَخَاتَمَ النَّبِيِّينَ وَكَانَ اللَّهُ بِكُلِّ شَيْءٍ عَلِيمًا.",
            webUrl = "https://quran.com/33/40"
        ),
        "quran_48_29" to IslamicReferenceItem(
            id = "quran_48_29",
            bookOrSurah = "Surah Al-Fath (48:29)",
            grade = "Quranic Ayah",
            arabicText = "مُّحَمَّدٌ رَّسُولُ اللَّهِ ۚ وَالَّذِينَ مَعَهُ أَشِدَّاءُ عَلَى الْكُفَّارِ رُحَمَاءُ بَيْنَهُمْ",
            translationEn = "Muhammad is the Messenger of Allah; and those with him are forceful against the disbelievers, merciful among themselves.",
            translationHi = "मुहम्मद ﷺ अल्लाह के रसूल हैं; और जो लोग उनके साथ हैं, वे काफ़िरों पर सख्त और आपस में बेहद रहमदिल हैं।",
            translationUr = "محمد اللہ کے رسول ہیں اور جو لوگ ان کے ساتھ ہیں وہ کافروں پر سخت اور آپس میں نہایت رحم دل ہیں۔",
            translationAr = "محمد رسول الله والذين معه أشداء على الكفار رحماء بينهم.",
            webUrl = "https://quran.com/48/29"
        ),
        "bukhari_8" to IslamicReferenceItem(
            id = "bukhari_8",
            bookOrSurah = "Sahih al-Bukhari (8)",
            grade = "Sahih (Muttafaq 'Alayh)",
            narrator = "Ibn Umar (RA)",
            arabicText = "بُنِيَ الإِسْلاَمُ عَلَى خَمْسٍ: شَهَادَةِ أَنْ لاَ إِلَهَ إِلاَّ اللَّهُ وَأَنَّ مُحَمَّدًا رَسُولُ اللَّهِ، وَإِقَامِ الصَّلاَةِ، وَإِيتَاءِ الزَّكَاةِ، وَالحَجِّ، وَصَوْمِ رَمَضَانَ",
            translationEn = "Islam is based on five (principles): To testify that none has the right to be worshipped but Allah and Muhammad is Allah's Messenger; to establish the prayers; to pay Zakat; to perform Hajj; and to observe fast during the month of Ramadan.",
            translationHi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'इस्लाम की बुनियाद 5 चीज़ों पर रखी गई है: इस बात की गवाही देना कि अल्लाह के सिवा कोई माबूद नहीं और मुहम्मद ﷺ अल्लाह के रसूल हैं, नमाज़ क़ायम करना, ज़कात देना, हज करना और रमज़ान के रोज़े रखना।'",
            translationUr = "رسول اللہ ﷺ نے فرمایا: 'اسلام کی بنیاد پانچ چیزوں پر ہے: اس بات کی گواہی دینا کہ اللہ کے سوا کوئی معبود نہیں اور محمد ﷺ اللہ کے رسول ہیں، نماز قائم کرنا، زکوٰۃ دینا، حج کرنا اور رمضان کے روزے رکھنا۔'",
            translationAr = "بني الإسلام على خمس: شهادة أن لا إله إلا الله وأن محمداً رسول الله، وإقام الصلاة، وإيتاء الزكاة، والحج، وصوم رمضان.",
            webUrl = "https://sunnah.com/bukhari:8"
        ),
        "muslim_234" to IslamicReferenceItem(
            id = "muslim_234",
            bookOrSurah = "Sahih Muslim (234)",
            grade = "Sahih",
            narrator = "Umar ibn al-Khattab (RA)",
            arabicText = "مَا مِنْكُمْ مِنْ أَحَدٍ يَتَوَضَّأُ فَيُبْلِغُ أَوْ فَيُسْبِغُ الْوَضُوءَ ثُمَّ يَقُولُ: أَشْهَدُ أَنْ لَا إِلَهَ إِلَّا اللهُ وَأَنَّ مُحَمَّدًا عَبْدُ اللهِ وَرَسُولُهُ إِلَّا فُتِحَتْ لَهُ أَبْوَابُ الْجَنَّةِ الثَّمَانِيَةُ يَدْخُلُ مِنْ أَيِّهَا شَاءَ",
            translationEn = "If anyone amongst you performs the ablution, and completes it well and then says: 'I testify that there is no god but Allah and that Muhammad is the servant of Allah and His Messenger,' the eight gates of Paradise would be opened for him and he may enter by whichever of them he wishes.",
            translationHi = "जो शख्स कामिल वज़ू करे और फिर कलिमा-ए-शहादत पढ़े, उसके लिए जन्नत के आठों दरवाज़े खोल दिए जाते हैं, जिस दरवाज़े से चाहे दाख़िल हो जाए।",
            translationUr = "جو شخص اچھی طرح وضو کرے اور پھر کلمہ شہادت پڑھے تو اس کے لیے جنت کے آٹھوں دروازے کھول دیے جاتے ہیں، جس سے چاہے داخل ہو جائے۔",
            translationAr = "ما منكم من أحد يتوضأ فيسبغ الوضوء ثم يقول: أشهد أن لا إله إلا الله وأن محمداً عبد الله ورسوله، إلا فتحت له أبواب الجنة الثمانية يدخل من أيها شاء.",
            webUrl = "https://sunnah.com/muslim:234"
        ),
        "bukhari_3293" to IslamicReferenceItem(
            id = "bukhari_3293",
            bookOrSurah = "Sahih al-Bukhari (3293)",
            grade = "Sahih",
            narrator = "Abu Hurairah (RA)",
            arabicText = "مَنْ قَالَ: لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ المُلْكُ وَلَهُ الحَمْدُ، وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ، فِي يَوْمٍ مِائَةَ مَرَّةٍ، كَانَتْ لَهُ عَدْلَ عَشْرِ رِقَابٍ، وَكُتِبَتْ لَهُ مِائَةُ حَسَنَةٍ، وَمُحِيَتْ عَنْهُ مِائَةُ سَيِّئَةٍ",
            translationEn = "Whoever says: 'La ilaha illallahu wahdahu la sharika lahu, lahul-mulku wa lahul-hamdu, wa huwa 'ala kulli shay'in qadir' one hundred times a day will have a reward equivalent to freeing ten slaves, one hundred good deeds will be recorded to him, and a hundred sins will be blotted out from him.",
            translationHi = "जो शख्स दिन में 100 बार यह कलिमा-ए-तौहीद पढ़ेगा, उसे 10 ग़ुलाम आज़ाद करने के बराबर सवाब मिलेगा, 100 नेकियां लिखी जाएँगी और 100 गुनाह मिटा दिए जाएँगे।",
            translationUr = "جو شخص دن میں ۱۰۰ مرتبہ یہ کلمہ کہے گا، اسے ۱۰ غلام آزاد کرنے کا ثواب ملے گا، ۱۰۰ نیکیاں لکھی جائیں گی اور ۱۰۰ گناہ مٹا دیے جائیں گے۔",
            translationAr = "من قال: لا إله إلا الله وحده لا شريك له، له الملك وله الحمد وهو على كل شيء قدير في يوم مائة مرة كانت له عدل عشر رقاب.",
            webUrl = "https://sunnah.com/bukhari:3293"
        ),
        "muslim_2691" to IslamicReferenceItem(
            id = "muslim_2691",
            bookOrSurah = "Sahih Muslim (2691)",
            grade = "Sahih",
            narrator = "Samurah ibn Jundub (RA)",
            arabicText = "أَحَبُّ الْكَلاَمِ إِلَى اللَّهِ أَرْبَعٌ: سُبْحَانَ اللَّهِ، وَالْحَمْدُ لِلَّهِ، وَلاَ إِلَهَ إِلاَّ اللَّهُ، وَاللَّهُ أَكْبَرُ",
            translationEn = "The words dearest to Allah are four: SubhanAllah, al-Hamdulillah, La ilaha illallah, and Allahu Akbar.",
            translationHi = "अल्लाह को सबसे प्यारे 4 कलिमात हैं: सुब्हानअल्लाह, अलहम्दुलिल्लाह, लाइलाहा इल्लल्लाह, और अल्लाहु अकबर।",
            translationUr = "اللہ کے نزدیک سب سے پسندیدہ چار کلمات ہیں: سبحان اللہ، الحمد للہ، لا الہ الا اللہ، اور اللہ اکبر۔",
            translationAr = "أحب الكلام إلى الله أربع: سبحان الله، والحمد لله، ولا إله إلا الله، والله أكبر.",
            webUrl = "https://sunnah.com/muslim:2691"
        ),
        "abudawud_1517" to IslamicReferenceItem(
            id = "abudawud_1517",
            bookOrSurah = "Sunan Abi Dawud (1517)",
            grade = "Sahih",
            narrator = "Zayd ibn Harithah (RA)",
            arabicText = "مَنْ قَالَ: أَسْتَغْفِرُ اللَّهَ الَّذِي لَا إِلَهَ إِلَّا هُوَ الْحَيَّ الْقَيُّومَ وَأَتُوبُ إِلَيْهِ، غُفِرَ لَهُ وَإِنْ كَانَ فَرَّ مِنَ الزَّحْفِ",
            translationEn = "Whoever says: 'I seek forgiveness from Allah, other than Whom there is no god, the Ever-Living, the Self-Subsisting, and I turn to Him in repentance,' he will be forgiven, even if he had fled from battle.",
            translationHi = "जो शख्स यह इस्तिग़फ़ार पढ़ेगा उसके गुनाह मुआफ़ कर दिए जाएँगे, ख़्वाह वह मैदान-ए-जंग से पीठ फेरकर ही क्यों न भागा हो।",
            translationUr = "جو شخص یہ استغفار پڑھے گا اس کے گناہ بخش دیے جائیں گے اگرچہ وہ میدانِ جنگ سے بھاگا ہو۔",
            translationAr = "من قال: أستغفر الله الذي لا إله إلا هو الحي القيوم وأتوب إليه، غفر له وإن كان فر من الزحف.",
            webUrl = "https://sunnah.com/abudawud:1517"
        ),
        "adab_716" to IslamicReferenceItem(
            id = "adab_716",
            bookOrSurah = "Al-Adab al-Mufrad (716)",
            grade = "Sahih (Imam Bukhari & Ahmad)",
            narrator = "Abu Bakr as-Siddiq (RA)",
            arabicText = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ أَنْ أُشْرِكَ بِكَ وَأَنَا أَعْلَمُ، وَأَسْتَغْفِرُكَ لِمَا لَا أَعْلَمُ",
            translationEn = "O Allah, I seek refuge in You from knowingly associating partners with You, and I seek Your forgiveness for that which I do not know.",
            translationHi = "ऐ अल्लाह! मैं तेरी पनाह माँगता हूँ इस बात से कि जानते-बूझते किसी चीज़ को तेरा साझीदार ठहराऊँ, और मैं तुझसे उन गुनाहों की माफ़ी चाहता हूँ जिन्हें मैं नहीं जानता।",
            translationUr = "اے اللہ! میں تیری پناہ مانگتا ہوں اس بات سے کہ جانتے بوجھتے کسی کو تیرا شریک ٹھہراؤں، اور بخشش مانگتا ہوں اس کی جسے میں نہیں جانتا۔",
            translationAr = "اللهم إني أعوذ بك أن أشرك بك وأنا أعلم، وأستغفرك لما لا أعلم.",
            webUrl = "https://sunnah.com/adab:716"
        ),
        "bukhari_128" to IslamicReferenceItem(
            id = "bukhari_128",
            bookOrSurah = "Sahih al-Bukhari (128)",
            grade = "Sahih",
            narrator = "Mu'adh ibn Jabal (RA)",
            arabicText = "مَا مِنْ أَحَدٍ يَشْهَدُ أَنْ لاَ إِلَهَ إِلاَّ اللَّهُ وَأَنَّ مُحَمَّدًا رَسُولُ اللَّهِ صِدْقًا مِنْ قَلْبِهِ، إِلاَّ حَرَّمَهُ اللَّهُ عَلَى النَّارِ",
            translationEn = "The Prophet ﷺ said: 'There is none who testifies that none has the right to be worshipped but Allah and that Muhammad is His Messenger sincerely from his heart, but Allah will forbid him for Hellfire.'",
            translationHi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'जो शख्स भी सच्चे दिल से इस बात की गवाही दे कि अल्लाह के सिवा कोई माबूद नहीं और मुहम्मद ﷺ उसके रसूल हैं, अल्लाह तआला उस पर जहन्नम की आग हराम कर देता है।'",
            translationUr = "رسول اللہ ﷺ نے فرمایا: 'جو شخص سچے دل سے گواہی دے کہ اللہ کے سوا کوئی معبود نہیں اور محمد ﷺ اس کے رسول ہیں، اللہ اس پر جہنم کی آگ حرام کر دیتا ہے۔'",
            translationAr = "ما من أحد يشهد أن لا إله إلا الله وأن محمداً رسول الله صدقاً من قلبه، إلا حرمه الله على النار.",
            webUrl = "https://sunnah.com/bukhari:128"
        ),
        "quran_98_5" to IslamicReferenceItem(
            id = "quran_98_5",
            bookOrSurah = "Surah Al-Bayyinah (98:5)",
            grade = "Quranic Ayah",
            arabicText = "وَمَا أُمِرُوا إِلَّا لِيَعْبُدُوا اللَّهَ مُخْلِصِينَ لَهُ الدِّينَ حُنَفَاءَ وَيُقِيمُوا الصَّلَاةَ وَيُؤْتُوا الزَّكَاةَ ۚ وَذَٰلِكَ دِينُ الْقَيِّمَةِ",
            translationEn = "And they were not commanded except to worship Allah, [being] sincere to Him in religion, inclining to truth, and to establish prayer and to give Zakat. And that is the correct religion.",
            translationHi = "और उन्हें इसके सिवा कोई हुक्म नहीं दिया गया था कि वे ख़ालिस अल्लाह ही की इबादत करें, अपने दीन को उसी के लिए ख़ालिस रखते हुए, और नमाज़ क़ायम करें और ज़कात अदा करें।",
            translationUr = "اور انہیں صرف یہی حکم دیا گیا تھا کہ وہ یکسو ہو کر خالص اللہ کی عبادت کریں اور نماز قائم کریں اور زکوٰۃ دیں۔",
            translationAr = "وما أمروا إلا ليعبدوا الله مخلصين له الدين حنفاء ويقيموا الصلاة ويؤتوا الزكاة وذلك دين القيمة.",
            webUrl = "https://quran.com/98/5"
        ),
        "muslim_26" to IslamicReferenceItem(
            id = "muslim_26",
            bookOrSurah = "Sahih Muslim (26)",
            grade = "Sahih",
            narrator = "Uthman ibn Affan (RA)",
            arabicText = "مَنْ مَاتَ وَهُوَ يَعْلَمُ أَنَّهُ لَا إِلَهَ إِلَّا اللهُ دَخَلَ الْجَنَّةَ",
            translationEn = "The Messenger of Allah ﷺ said: 'He who died knowing that there is no god but Allah entered Paradise.'",
            translationHi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'जो शख्स इस हाल में मरा कि वह जानता था कि अल्लाह के सिवा कोई सच्चा माबूद नहीं, वह जन्नत में दाख़िल होगा।' (इल्म की शर्त)",
            translationUr = "رسول اللہ ﷺ نے فرمایا: 'جو شخص اس حال میں مرا کہ وہ جانتا تھا کہ اللہ کے سوا کوئی معبود نہیں وہ جنت میں داخل ہوا۔'",
            translationAr = "من مات وهو يعلم أنه لا إله إلا الله دخل الجنة.",
            webUrl = "https://sunnah.com/muslim:26"
        ),
        "bukhari_99" to IslamicReferenceItem(
            id = "bukhari_99",
            bookOrSurah = "Sahih al-Bukhari (99)",
            grade = "Sahih",
            narrator = "Abu Hurairah (RA)",
            arabicText = "أَسْعَدُ النَّاسِ بِشَفَاعَتِي يَوْمَ القِيَامَةِ، مَنْ قَالَ لاَ إِلَهَ إِلاَّ اللَّهُ، خَالِصًا مِنْ قَلْبِهِ أَوْ نَفْسِهِ",
            translationEn = "The Prophet ﷺ said: 'The most fortunate of people with my intercession on the Day of Resurrection will be he who said: There is no deity but Allah sincerely from his heart.'",
            translationHi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'क़ियामत के दिन मेरी शफ़ाअत का सबसे ज़्यादा हक़दार वह ख़ुशनसीब होगा जिसने ख़ालिस अपने दिल से 'ला इलाहा इल्लल्लाह' कहा हो।' (इख़लास की शर्त)",
            translationUr = "رسول اللہ ﷺ نے فرمایا: 'قیامت کے دن میری شفاعت کا سب سے زیادہ حقدار وہ ہوگا جس نے سچے دل سے اخلاص کے ساتھ لا الہ الا اللہ کہا ہو۔'",
            translationAr = "أسعد الناس بشفاعتي يوم القيامة من قال لا إله إلا الله خالصاً من قلبه أو نفسه.",
            webUrl = "https://sunnah.com/bukhari:99"
        ),
        "abudawud_3116" to IslamicReferenceItem(
            id = "abudawud_3116",
            bookOrSurah = "Sunan Abi Dawud (3116)",
            grade = "Sahih",
            narrator = "Mu'adh ibn Jabal (RA)",
            arabicText = "مَنْ كَانَ آخِرُ كَلَامِهِ لَا إِلَهَ إِلَّا اللَّهُ دَخَلَ الْجَنَّةَ",
            translationEn = "The Messenger of Allah ﷺ said: 'He whose last words are: There is no god but Allah, will enter Paradise.'",
            translationHi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'जिसकी ज़बान से निकला आख़िरी कलिमा 'ला इलाहा इल्लल्लाह' होगा, वह सीधा जन्नत में दाख़िल होगा।'",
            translationUr = "رسول اللہ ﷺ نے فرمایا: 'جس کا آخری کلام لا الہ الا اللہ ہوگا وہ جنت میں داخل ہوگا۔'",
            translationAr = "من كان آخر كلامه لا إله إلا الله دخل الجنة.",
            webUrl = "https://sunnah.com/abudawud:3116"
        ),
        "muslim_32" to IslamicReferenceItem(
            id = "muslim_32",
            bookOrSurah = "Sahih Muslim (32)",
            grade = "Sahih",
            narrator = "Ubada ibn as-Samit (RA)",
            arabicText = "مَنْ شَهِدَ أَنْ لَا إِلَهَ إِلَّا اللهُ، وَأَنَّ مُحَمَّدًا رَسُولُ اللهِ، حَرَّمَ اللهُ عَلَيْهِ النَّارَ",
            translationEn = "The Messenger of Allah ﷺ said: 'Whoever testifies that there is no god but Allah and that Muhammad is the Messenger of Allah, Allah forbids the Fire for him.'",
            translationHi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'जिसने यह गवाही दी कि अल्लाह के सिवा कोई माबूद नहीं और मुहम्मद ﷺ अल्लाह के रसूल हैं, अल्लाह उस पर आग हराम कर देता है।'",
            translationUr = "جس نے گواہی دی کہ اللہ کے سوا کوئی معبود نہیں اور محمد ﷺ اللہ کے رسول ہیں اللہ نے اس پر آگ حرام فرما دی۔",
            translationAr = "من شهد أن لا إله إلا الله وأن محمداً رسول الله حرم الله عليه النار.",
            webUrl = "https://sunnah.com/muslim:32"
        ),

        // ═══════════════════════════════════════════════════════════
        // PILLAR 2: SALAH / NAMAZ REFERENCES
        // ═══════════════════════════════════════════════════════════
        "quran_2_43" to IslamicReferenceItem(
            id = "quran_2_43",
            bookOrSurah = "Surah Al-Baqarah (2:43)",
            grade = "Quranic Ayah",
            arabicText = "وَأَقِيمُوا الصَّلَاةَ وَآتُوا الزَّكَاةَ وَارْكَعُوا مَعَ الرَّاكِعِينَ",
            translationEn = "And establish prayer and give Zakat and bow with those who bow [in worship and obedience].",
            translationHi = "और नमाज़ क़ायम करो और ज़कात दो और रुकू करने वालों के साथ रुकू करो।",
            translationUr = "اور نماز قائم کرو اور زکوٰۃ ادا کرو اور رکوع کرنے والوں کے ساتھ رکوع کرو۔",
            translationAr = "وأقيموا الصلاة وآتوا الزكاة واركعوا مع الراكعين.",
            webUrl = "https://quran.com/2/43"
        ),
        "quran_29_45" to IslamicReferenceItem(
            id = "quran_29_45",
            bookOrSurah = "Surah Al-Ankabut (29:45)",
            grade = "Quranic Ayah",
            arabicText = "اتْلُ مَا أُوحِيَ إِلَيْكَ مِنَ الْكِتَابِ وَأَقِمِ الصَّلَاةَ ۖ إِنَّ الصَّلَاةَ تَنْهَىٰ عَنِ الْفَحْشَاءِ وَالْمُنكَرِ ۗ وَلَذِكْرُ اللَّهِ أَكْبَرُ",
            translationEn = "Recite, [O Muhammad], what has been revealed to you of the Book and establish prayer. Indeed, prayer prohibits immorality and wrongdoing, and the remembrance of Allah is greater.",
            translationHi = "बेशक नमाज़ बेहयाई और बुराई के कामों से रोकती है, और यक़ीनन अल्लाह का ज़िक्र सबसे बड़ी चीज़ है।",
            translationUr = "بیشک نماز بے حیائی اور برائی سے روکتی ہے اور یقیناً اللہ کا ذکر سب سے بڑا ہے۔",
            translationAr = "اتل ما أوحي إليك من الكتاب وأقم الصلاة إن الصلاة تنهى عن الفحشاء والمنكر.",
            webUrl = "https://quran.com/29/45"
        ),
        "bukhari_349" to IslamicReferenceItem(
            id = "bukhari_349",
            bookOrSurah = "Sahih al-Bukhari (349)",
            grade = "Sahih (Muttafaq 'Alayh)",
            narrator = "Anas ibn Malik (RA)",
            arabicText = "فَرَضَ اللَّهُ عَلَى أُمَّتِي لَيْلَةَ الإِسْرَاءِ خَمْسِينَ صَلاَةً، فَلَمْ أَزَلْ أُرَاجِعُهُ وَيَضَعُ حَتَّى قَالَ: هِيَ خَمْسٌ وَهِيَ خَمْسُونَ، لاَ يُبَدَّلُ القَوْلُ لَدَيَّ",
            translationEn = "On the night of Isra, Allah enjoined fifty prayers on my followers. I kept appealing to my Lord until He said: 'They are five (prayers in practice) and fifty (in reward), for My Word does not change.'",
            translationHi = "शब-ए-मेअराज में अल्लाह तआला ने 50 नमाज़ें फ़र्ज़ फ़रमाईं, फिर घटाते-घटाते 5 नमाज़ें मुक़र्रर हुईं और अल्लाह ने फ़रमाया: 'अमल में 5 हैं, लेकिन सवाब 50 नमाज़ों का ही मिलेगा।'",
            translationUr = "شبِ معراج میں اللہ تعالیٰ نے ۵۰ نمازیں فرض کیں، پھر تخفیف ہو کر ۵ رہ گئیں اور فرمایا: عمل میں ۵ ہیں اور ثواب ۵۰ کا ہے۔",
            translationAr = "فرضت علي الصلوات خمسين، ثم جعلت خمساً، ونودي: إني قد أمضيت فريضتي وخففت عن عبادي وأجزي بالحسنة عشراً.",
            webUrl = "https://sunnah.com/bukhari:349"
        ),
        "bukhari_757" to IslamicReferenceItem(
            id = "bukhari_757",
            bookOrSurah = "Sahih al-Bukhari (757)",
            grade = "Sahih (Hadith al-Musi'u Salatahu)",
            narrator = "Abu Hurairah (RA)",
            arabicText = "إِذَا قُمْتَ إِلَى الصَّلاَةِ فَكَبِّرْ، ثُمَّ اقْرَأْ مَا تَيَسَّرَ مَعَكَ مِنَ القُرْآنِ، ثُمَّ ارْكَعْ حَتَّى تَطْمَئِنَّ رَاكِعًا، ثُمَّ ارْفَعْ حَتَّى تَعْتَدِلَ قَائِمًا، ثُمَّ اسْجُدْ حَتَّى تَطْمَئِنَّ سَاجِدًا",
            translationEn = "The Prophet ﷺ said to the man who prayed improperly: 'When you stand for prayer, say Takbir, then recite what you can of the Quran, then bow until you are calm in bowing, then rise until you stand erect, then prostrate until you are calm in prostration...'",
            translationHi = "रसूलुल्लाह ﷺ ने सही तरीक़ा सिखाते हुए फ़रमाया: 'जब नमाज़ के लिए खड़े हो तो तकबीर कहो, फिर क़ुरआन की जो आयतें आसान हों पढ़ो, फिर इत्मीनान से रुकू करो, फिर सीधे खड़े हो जाओ, फिर इत्मीनान से सज्दा करो...'",
            translationUr = "رسول اللہ ﷺ نے فرمایا: جب تم نماز کے لیے کھڑے ہو تو تکبیر کہو، پھر جو قرآن سے میسر ہو پڑھو، پھر اطمینان سے رکوع کرو، پھر سیدھے کھڑے ہو جاؤ، پھر اطمینان سے سجدہ کرو۔",
            translationAr = "إذا قمت إلى الصلاة فكبر، ثم اقرأ ما تيسر معك من القرآن، ثم اركع حتى تطمئن راكعاً...",
            webUrl = "https://sunnah.com/bukhari:757"
        ),
        "bukhari_528" to IslamicReferenceItem(
            id = "bukhari_528",
            bookOrSurah = "Sahih al-Bukhari (528)",
            grade = "Sahih",
            narrator = "Abu Hurairah (RA)",
            arabicText = "أَرَأَيْتُمْ لَوْ أَنَّ نَهَرًا بِبَابِ أَحَدِكُمْ يَغْتَسِلُ مِنْهُ كُلَّ يَوْمٍ خَمْسَ مَرَّاتٍ، هَلْ يَبْقَى مِنْ دَرَنِهِ شَيْءٌ؟ قَالُوا: لاَ. قَالَ: فَذَلِكَ مَثَلُ الصَّلَوَاتِ الخَمْسِ، يَمْحُو اللَّهُ بِهِنَّ الخَطَايَا",
            translationEn = "The Prophet ﷺ said: 'Consider if there were a river at your door and you washed in it five times a day, would any dirt remain?' They replied, 'No.' He said, 'That is the example of the five daily prayers: Allah wipes away sins with them.'",
            translationHi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'बताओ अगर तुम में से किसी के दरवाज़े पर नहर बहती हो और वह रोज़ाना उसमें 5 बार नहाए तो क्या उसके बदन पर कोई मैल बचेगा? सहाबा ने कहा: नहीं। फ़रमाया: यही मिसाल 5 नमाज़ों की है, अल्लाह उनके ज़रिए गुनाहों को धो देता है।'",
            translationUr = "رسول اللہ ﷺ نے فرمایا: 'اگر تم میں سے کسی کے دروازے پر نہر ہو جس میں وہ روزانہ پانچ بار نہائے تو کیا کوئی میل باقی رہے گا؟ صحابہ نے کہا نہیں، فرمایا یہی پانچ نمازوں کی مثال ہے اللہ ان سے گناہ مٹا دیتا ہے۔'",
            translationAr = "أرأيتم لو أن نهراً بباب أحدكم يغتسل منه كل يوم خمس مرات، هل يبقى من درنه شيء؟ قالوا: لا. قال: فذلك مثل الصلوات الخمس يمحو الله بهن الخطايا.",
            webUrl = "https://sunnah.com/bukhari:528"
        ),
        "muslim_223" to IslamicReferenceItem(
            id = "muslim_223",
            bookOrSurah = "Sahih Muslim (223)",
            grade = "Sahih",
            narrator = "Abu Malik al-Ash'ari (RA)",
            arabicText = "الطُّهُورُ شَطْرُ الإِيمَانِ، وَالصَّلاَةُ نُورٌ، وَالصَّدَقَةُ بُرْهَانٌ، وَالصَّبْرُ ضِيَاءٌ، وَالْقُرْآنُ حُجَّةٌ لَكَ أَوْ عَلَيْكَ",
            translationEn = "The Messenger of Allah ﷺ said: 'Cleanliness is half of faith, and prayer is light, and charity is proof, and patience is illumination, and the Quran is an argument for you or against you.'",
            translationHi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'पाकी आधा ईमान है, और नमाज़ नूर है, और सदक़ा दलील है, और सब्र रोशनी है, और क़ुरआन तुम्हारे हक़ में हुज्जत है या तुम्हारे ख़िलाफ़।'",
            translationUr = "رسول اللہ ﷺ نے فرمایا: 'پاکیزگی نصف ایمان ہے، نماز نور ہے، صدقہ برہان ہے، اور صبر روشنی ہے۔'",
            translationAr = "الطهور شطر الإيمان، والصلاة نور، والصدقة برهان، والصبر ضياء، والقرآن حجة لك أو عليك.",
            webUrl = "https://sunnah.com/muslim:223"
        ),
        "tirmidhi_4" to IslamicReferenceItem(
            id = "tirmidhi_4",
            bookOrSurah = "Jami` at-Tirmidhi (4)",
            grade = "Sahih",
            narrator = "Jabir ibn Abdullah (RA)",
            arabicText = "مِفْتَاحُ الْجَنَّةِ الصَّلَاةُ، وَمِفْتَاحُ الصَّلَاةِ الْوُضُوءُ",
            translationEn = "The Prophet ﷺ said: 'The key to Paradise is prayer, and the key to prayer is purification (Wudu).'",
            translationHi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'जन्नत की चाबी नमाज़ है, और नमाज़ की चाबी वुज़ू (पाकीज़ा होना) है।'",
            translationUr = "رسول اللہ ﷺ نے فرمایا: 'جنت کی کنجی نماز ہے، اور نماز کی کنجی وضو ہے۔'",
            translationAr = "مفتاح الجنة الصلاة، ومفتاح الصلاة الوضوء.",
            webUrl = "https://sunnah.com/tirmidhi:4"
        ),
        "baihaqi_2550" to IslamicReferenceItem(
            id = "baihaqi_2550",
            bookOrSurah = "Shu'ab al-Iman (Al-Bayhaqi 2550)",
            grade = "Hasan (Al-Bayhaqi)",
            narrator = "Umar ibn al-Khattab / Ali (RA)",
            arabicText = "الصَّلَاةُ عِمَادُ الدِّينِ، فَمَنْ أَقَامَهَا فَقَدْ أَقَامَ الدِّينَ، وَمَنْ هَدَمَهَا فَقَدْ هَدَمَ الدِّينَ",
            translationEn = "The Prophet ﷺ said: 'Salah (prayer) is the pillar of religion; whoever establishes it has established religion, and whoever demolishes it has demolished religion.'",
            translationHi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'नमाज़ दीन का सुतून (स्तंभ) है; जिसने इसे क़ायम किया उसने दीन को क़ायम किया, और जिसने इसे छोड़ दिया उसने दीन की इमारत को ढा दिया।'",
            translationUr = "رسول اللہ ﷺ نے فرمایا: 'نماز دین کا ستون ہے؛ جس نے اسے قائم کیا اس نے دین کو قائم کیا، اور جس نے اسے گرا دیا اس نے دین کو گرا دیا۔'",
            translationAr = "الصلاة عماد الدين، فمن أقامها فقد أقام الدين، ومن هدمها فقد هدم الدين.",
            webUrl = "https://sunnah.com/search?q=prayer+pillar+of+religion"
        ),
        "quran_4_103" to IslamicReferenceItem(
            id = "quran_4_103",
            bookOrSurah = "Surah An-Nisa (4:103)",
            grade = "Quranic Ayah",
            arabicText = "إِنَّ الصَّلَاةَ كَانَتْ عَلَى الْمُؤْمِنِينَ كِتَابًا مَّوْقُوتًا",
            translationEn = "Indeed, prayer has been decreed upon the believers a decree of specified times.",
            translationHi = "बेशक नमाज़ मोमिनों पर मुक़र्ररा वक़्तों में फ़र्ज़ की गई है।",
            translationUr = "بیشک نماز مومنوں پر مقررہ اوقات میں فرض کی گئی ہے۔",
            translationAr = "إن الصلاة كانت على المؤمنين كتاباً موقوتاً.",
            webUrl = "https://quran.com/4/103"
        ),

        // ═══════════════════════════════════════════════════════════
        // PILLAR 3: ZAKAT REFERENCES
        // ═══════════════════════════════════════════════════════════
        "quran_9_60" to IslamicReferenceItem(
            id = "quran_9_60",
            bookOrSurah = "Surah At-Tawbah (9:60)",
            grade = "Quranic Ayah",
            arabicText = "إِنَّمَا الصَّدَقَاتُ لِلْفُقَرَاءِ وَالْمَسَاكِينِ وَالْعَامِلِينَ عَلَيْهَا وَالْمُؤَلَّفَةِ قُلُوبُهُمْ وَفِي الرِّقَابِ وَالْغَارِمِينَ وَفِي سَبِيلِ اللَّهِ وَابْنِ السَّبِيلِ ۖ فَرِيضَةً مِّنَ اللَّهِ ۗ وَاللَّهُ عَلِيمٌ حَكِيمٌ",
            translationEn = "Zakah expenditures are only for the poor and for the needy and for those employed to collect [zakah] and for bringing hearts together [for Islam] and for freeing captives [or slaves] and for those in debt and for the cause of Allah and for the [stranded] traveler - an obligation [imposed] by Allah. And Allah is Knowing and Wise.",
            translationHi = "ज़कात तो सिर्फ़ फ़क़ीरों, मिस्कीनों, ज़कात वसूल करने वाले कारिंदों, और जिनके दिलों को परचाना मक़सूद हो, और ग़ुलामों को आज़ाद कराने में, और क़र्ज़दारों के क़र्ज़ चुकाने में, और अल्लाह की राह में, और मुसाफ़िरों के लिए है। यह अल्लाह का मुक़र्रर किया हुआ फ़र्ज़ है।",
            translationUr = "زکوٰۃ تو بس فقراء، مساکین، عاملینِ زکوٰۃ، تالیفِ قلب کے لیے، گردنیں چھڑانے میں، مقروضوں کے لیے، اللہ کی راہ میں اور مسافروں کے لیے فرض ہے۔",
            translationAr = "إنما الصدقات للفقراء والمساكين والعاملين عليها والمؤلفة قلوبهم وفي الرقاب والغارمين وفي سبيل الله وابن السبيل فريضة من الله.",
            webUrl = "https://quran.com/9/60"
        ),
        "abudawud_1573" to IslamicReferenceItem(
            id = "abudawud_1573",
            bookOrSurah = "Sunan Abi Dawud (1573)",
            grade = "Sahih (Ali ibn Abi Talib RA)",
            narrator = "Ali ibn Abi Talib (RA)",
            arabicText = "فَإِذَا كَانَتْ لَكَ مِائَتَا دِرْهَمٍ وَحَالَ عَلَيْهَا الْحَوْلُ فَفِيهَا خَمْسَةُ دَرَاهِمَ، وَلَيْسَ عَلَيْكَ شَيْءٌ فِي الذَّهَبِ حَتَّى يَكُونَ لَكَ عِشْرُونَ دِينَارًا، فَإِذَا كَانَتْ لَكَ عِشْرُونَ دِينَارًا وَحَالَ عَلَيْهَا الْحَوْلُ فَفِيهَا نِصْفُ دِينَارٍ",
            translationEn = "The Prophet ﷺ said: 'When you possess two hundred dirhams (595g silver) and one year passes on them, five dirhams are due (2.5%). You owe nothing on gold until you possess twenty dinars (85g gold); when you possess twenty dinars and a year passes, half a dinar is due.'",
            translationHi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'जब तुम्हारे पास 200 दिरहम (595 ग्राम चाँदी) हों और उस पर एक साल गुज़र जाए तो उसमें 5 दिरहम (2.5%) ज़कात है, और सोने में जब तक 20 दीनार (85 ग्राम सोना) न हों कुछ वाजिब नहीं, जब 20 दीनार हों और साल गुज़र जाए तो आधा दीनार (2.5%) ज़कात है।'",
            translationUr = "رسول اللہ ﷺ نے فرمایا: 'جب تمہارے پاس ۲۰۰ درہم چاندی ہو اور سال گزر جائے تو اس میں ۵ درہم زکوٰۃ ہے، اور سونے میں ۲۰ دینار ہونے پر نصف دینار زکوٰۃ ہے۔'",
            translationAr = "فإذا كانت لك مائتا درهم وحال عليها الحول ففيها خمسة دراهم، وليس عليك شيء في الذهب حتى يكون لك عشرون ديناراً ففيها نصف دينار.",
            webUrl = "https://sunnah.com/abudawud:1573"
        ),
        "tabarani_zakat" to IslamicReferenceItem(
            id = "tabarani_zakat",
            bookOrSurah = "Al-Mu'jam al-Awsat (Tabarani)",
            grade = "Sahih",
            narrator = "Jabir ibn Abdullah (RA)",
            arabicText = "مَنْ أَدَّى زَكَاةَ مَالِهِ فَقَدْ ذَهَبَ عَنْهُ شَرُّهُ",
            translationEn = "The Prophet ﷺ said: 'Whoever pays the Zakat on his wealth, its evil has been removed from him.'",
            translationHi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'जो शख्स अपने माल की ज़कात अदा कर देता है, अल्लाह तआला उसके माल से बुराई और नुकसान को दूर कर देता है।'",
            translationUr = "رسول اللہ ﷺ نے فرمایا: 'جس نے اپنے مال کی زکوٰۃ ادا کر دی، یقیناً اس سے اس کا شر اور برائی دور کر دی گئی۔'",
            translationAr = "من أدى زكاة ماله فقد ذهب عنه شره.",
            webUrl = "https://sunnah.com"
        ),

        // ═══════════════════════════════════════════════════════════
        // PILLAR 4: SAWM / FASTING REFERENCES
        // ═══════════════════════════════════════════════════════════
        "quran_2_183" to IslamicReferenceItem(
            id = "quran_2_183",
            bookOrSurah = "Surah Al-Baqarah (2:183)",
            grade = "Quranic Ayah",
            arabicText = "يَا أَيُّهَا الَّذِينَ آمَنُوا كُتِبَ عَلَيْكُمُ الصِّيَامُ كَمَا كُتِبَ عَلَى الَّذِينَ مِن قَبْلِكُمْ لَعَلَّكُمْ تَتَّقُونَ",
            translationEn = "O you who have believed, decreed upon you is fasting as it was decreed upon those before you that you may become righteous (attain Taqwa).",
            translationHi = "ऐ ईमान वालो! तुम पर रोज़े फ़र्ज़ किए गए जिस तरह तुमसे पहले के लोगों पर फ़र्ज़ किए गए थे, ताकि तुम्हारे अंदर तक़्वा (परहेज़गारी) पैदा हो।",
            translationUr = "اے ایمان والو! تم پر روزے فرض کیے گئے جیسے تم سے اگلوں پر فرض ہوئے تھے تاکہ تم پرہیزگار بن جاؤ۔",
            translationAr = "يا أيها الذين آمنوا كتب عليكم الصيام كما كتب على الذين من قبلكم لعلكم تتقون.",
            webUrl = "https://quran.com/2/183"
        ),
        "abudawud_2357" to IslamicReferenceItem(
            id = "abudawud_2357",
            bookOrSurah = "Sunan Abi Dawud (2357)",
            grade = "Hasan (Ibn Umar RA)",
            narrator = "Abdullah ibn Umar (RA)",
            arabicText = "كَانَ رَسُولُ اللَّهِ صَلَّى اللهُ عَلَيْهِ وَسَلَّمَ إِذَا أَفْطَرَ قَالَ: ذَهَبَ الظَّمَأُ، وَابْتَلَّتِ الْعُرُوقُ، وَثَبَتَ الأَجْرُ إِنْ شَاءَ اللَّهُ",
            translationEn = "At the time of breaking the fast, the Messenger of Allah ﷺ said: 'The thirst has gone, the veins are moistened, and the reward is confirmed, if Allah wills.'",
            translationHi = "रसूलुल्लाह ﷺ जब इफ़्तार फ़रमाते तो यह दुआ पढ़ते: 'प्यास बुझ गई, रगें तर हो गईं और अल्लाह ने चाहा तो अज्र व सवाब साबित हो गया।'",
            translationUr = "رسول اللہ ﷺ افطار کے وقت فرماتے تھے: 'پیاس بجھ گئی، رگیں تر ہو گئیں اور ان شاء اللہ ثواب پکا ہو گیا۔'",
            translationAr = "كان رسول الله ﷺ إذا أفطر قال: ذهب الظمأ، وابتلت العروق، وثبت الأجر إن شاء الله.",
            webUrl = "https://sunnah.com/abudawud:2357"
        ),
        "bukhari_1933" to IslamicReferenceItem(
            id = "bukhari_1933",
            bookOrSurah = "Sahih al-Bukhari (1933)",
            grade = "Sahih (Muttafaq 'Alayh)",
            narrator = "Abu Hurairah (RA)",
            arabicText = "إِذَا نَسِيَ فَأَكَلَ وَشَرِبَ، فَلْيُتِمَّ صَوْمَهُ، فَإِنَّمَا أَطْعَمَهُ اللَّهُ وَسَقَاهُ",
            translationEn = "The Prophet ﷺ said: 'If anyone forgets that he is fasting and eats or drinks, he should complete his fast, for Allah has fed him and given him drink.'",
            translationHi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'अगर कोई रोज़ेदार भूलकर कुछ खा या पी ले तो अपना रोज़ा पूरा करे, क्योंकि अल्लाह ने उसे खिलाया और पिलाया है (रोज़ा नहीं टूटा)।'",
            translationUr = "رسول اللہ ﷺ نے فرمایا: 'جب کوئی بھول کر کھا پی لے تو اپنا روزہ پورا کرے، کیونکہ اسے اللہ نے کھلایا اور پلایا ہے۔'",
            translationAr = "إذا نسي فأكل وشرب فليتم صومه، فإنما أطعمه الله وسقاه.",
            webUrl = "https://sunnah.com/bukhari:1933"
        ),
        "tirmidhi_720" to IslamicReferenceItem(
            id = "tirmidhi_720",
            bookOrSurah = "Jami` at-Tirmidhi (720)",
            grade = "Sahih",
            narrator = "Abu Hurairah (RA)",
            arabicText = "مَنْ ذَرَعَهُ قَيْءٌ وَهُوَ صَائِمٌ فَلَيْسَ عَلَيْهِ قَضَاءٌ، وَإِنِ اسْتَقَاءَ عَمْدًا فَلْيَقْضِ",
            translationEn = "The Prophet ﷺ said: 'Whoever is overcome by vomiting while he is fasting does not have to make up for it, but whoever deliberately induces vomiting must make up for the fast.'",
            translationHi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'जिस पर क़य (उल्टी) ग़ालिब आ जाए और वह रोज़े से हो तो उस पर कोई क़ज़ा नहीं, लेकिन जो जानबूझकर उल्टी करे वह क़ज़ा रखे।' (रोज़ा टूट जाता है)",
            translationUr = "رسول اللہ ﷺ نے فرمایا: 'جسے از خود قے آ جائے اس پر قضا نہیں، اور جو جان بوجھ کر قے کرے وہ قضا کرے۔'",
            translationAr = "من ذرعه قيء وهو صائم فليس عليه قضاء، وإن استقاء عمداً فليقض.",
            webUrl = "https://sunnah.com/tirmidhi:720"
        ),
        "muslim_1164" to IslamicReferenceItem(
            id = "muslim_1164",
            bookOrSurah = "Sahih Muslim (1164)",
            grade = "Sahih",
            narrator = "Abu Ayyub al-Ansari (RA)",
            arabicText = "مَنْ صَامَ رَمَضَانَ ثُمَّ أَتْبَعَهُ سِتًّا مِنْ شَوَّالٍ، كَانَ كَصِيَامِ الدَّهْرِ",
            translationEn = "The Messenger of Allah ﷺ said: 'Whoever fasts Ramadan, then follows it with six days of Shawwal, it is like fasting for a lifetime.'",
            translationHi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'जिसने रमज़ान के रोज़े रखे और फिर उसके बाद शव्वाल के 6 रोज़े रखे, तो यह पूरे ज़माने (साल भर) रोज़ा रखने के बराबर है।'",
            translationUr = "رسول اللہ ﷺ نے فرمایا: 'جس نے رمضان کے روزے رکھے پھر شوال کے چھ روزے رکھے، تو وہ زمانے بھر کے روزوں کی طرح ہے۔'",
            translationAr = "من صام رمضان ثم أتبعه ستاً من شوال، كان كصيام الدهر.",
            webUrl = "https://sunnah.com/muslim:1164"
        ),
        "muslim_1162" to IslamicReferenceItem(
            id = "muslim_1162",
            bookOrSurah = "Sahih Muslim (1162)",
            grade = "Sahih",
            narrator = "Abu Qatadah (RA)",
            arabicText = "صِيَامُ يَوْمِ عَرَفَةَ، أَحْتَسِبُ عَلَى اللهِ أَنْ يُكَفِّرَ السَّنَةَ الَّتِي قَبْلَهُ، وَالسَّنَةَ الَّتِي بَعْدَهُ، وَصِيَامُ يَوْمِ عَاشُورَاءَ، أَحْتَسِبُ عَلَى اللهِ أَنْ يُكَفِّرَ السَّنَةَ الَّتِي قَبْلَهُ",
            translationEn = "Fasting on the Day of Arafah expiates the sins of the preceding year and the coming year, and fasting on the Day of Ashura expiates the sins of the preceding year.",
            translationHi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'मुझे अल्लाह से उम्मीद है कि अरफ़ा के दिन (9 ज़ुल-हज्ज) का रोज़ा गुज़रे हुए एक साल और आने वाले एक साल के गुनाहों का कफ़्फ़ारा बन जाता है, और आशूरा का रोज़ा गुज़रे एक साल के गुनाह मुआफ़ करा देता है।'",
            translationUr = "رسول اللہ ﷺ نے فرمایا: 'عرفہ کا روزہ ایک سال پہلے اور ایک سال بعد کے گناہ مٹاتا ہے اور عاشوراء کا روزہ ایک سال پہلے کے گناہ معاف کراتا ہے۔'",
            translationAr = "صيام يوم عرفة أحتسب على الله أن يكفر السنة التي قبله والسنة التي بعده، وصيام يوم عاشوراء يكفر السنة التي قبله.",
            webUrl = "https://sunnah.com/muslim:1162"
        ),
        "bukhari_1894" to IslamicReferenceItem(
            id = "bukhari_1894",
            bookOrSurah = "Sahih al-Bukhari (1894)",
            grade = "Sahih (Hadith Qudsi)",
            narrator = "Abu Hurairah (RA)",
            arabicText = "الصِّيَامُ جُنَّةٌ، فَلاَ يَرْفُثْ وَلاَ يَجْهَلْ، وَإِنِ امْرُؤٌ قَاتَلَهُ أَوْ شَاتَمَهُ فَلْيَقُلْ: إِنِّي صَائِمٌ مَرَّتَيْنِ. وَالَّذِي نَفْسِي بِيَدِهِ لَخُلُوفُ فَمِ الصَّائِمِ أَطْيَبُ عِنْدَ اللَّهِ تَعَالَى مِنْ رِيحِ المِسْكِ",
            translationEn = "The Prophet ﷺ said: 'Fasting is a shield. When anyone of you is fasting on a day, he should neither indulge in obscene language nor raise his voice. If someone fights him or reviles him, he should say twice: I am fasting.'",
            translationHi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'रोज़ा एक ढाल है, लिहाज़ा रोज़ेदार न बेहूदा बात करे और न जिहालत। अगर कोई उससे लड़े या गाली दे, तो वह कह दे: मैं रोज़ेदार हूँ। उस ज़ात की क़सम जिसके हाथ में मेरी जान है, रोज़ेदार के मुंह की बू अल्लाह के नज़दीक मुश्क की खुशबू से भी ज़्यादा पाकीज़ा है।'",
            translationUr = "رسول اللہ ﷺ نے فرمایا: 'روزہ ڈھال ہے، پس روزہ دار نہ فحش بات کرے اور نہ جہالت۔ اگر کوئی جھگڑے تو کہہ دے میں روزے دار ہوں۔'",
            translationAr = "الصيام جنة، فلا يرفث ولا يجهل، وإن امرؤ قاتله أو شاتمه فليقل: إني صائم مرتين.",
            webUrl = "https://sunnah.com/bukhari:1894"
        ),

        // ═══════════════════════════════════════════════════════════
        // PILLAR 5: HAJJ REFERENCES
        // ═══════════════════════════════════════════════════════════
        "quran_3_97" to IslamicReferenceItem(
            id = "quran_3_97",
            bookOrSurah = "Surah Ali 'Imran (3:97)",
            grade = "Quranic Ayah",
            arabicText = "وَلِلَّهِ عَلَى النَّاسِ حِجُّ الْبَيْتِ مَنِ اسْتَطَاعَ إِلَيْهِ سَبِيلًا ۚ وَمَن كَفَرَ فَإِنَّ اللَّهَ غَنِيٌّ عَنِ الْعَالَمِينَ",
            translationEn = "And [due] to Allah from the people is a pilgrimage to the House - for whoever is able to find thereto a way. But whoever disbelieves - then indeed, Allah is free from need of the worlds.",
            translationHi = "और लोगों पर अल्लाह का हक़ है कि जो उस घर (काबा) तक पहुँचने की इस्तेताअत रखता हो वह उसका हज करे। और जो कोई इन्कार करे तो अल्लाह तमाम जहानों से बेनियाज़ है।",
            translationUr = "اور اللہ کے لیے لوگوں پر اس گھر کا حج فرض ہے جو اس تک جانے کی طاقت رکھے، اور جو کفر کرے تو اللہ جہانوں سے بے نیاز ہے۔",
            translationAr = "ولله على الناس حج البيت من استطاع إليه سبيلا ومن كفر فإن الله غني عن العالمين.",
            webUrl = "https://quran.com/3/97"
        ),
        "bukhari_1549" to IslamicReferenceItem(
            id = "bukhari_1549",
            bookOrSurah = "Sahih al-Bukhari (1549)",
            grade = "Sahih",
            narrator = "Abdullah ibn Umar (RA)",
            arabicText = "لَبَّيْكَ اللَّهُمَّ لَبَّيْكَ، لَبَّيْكَ لاَ شَرِيكَ لَكَ لَبَّيْكَ، إِنَّ الحَمْدَ وَالنِّعْمَةَ لَكَ وَالمُلْكَ، لاَ شَرِيكَ لَكَ",
            translationEn = "Here I am, O Allah, here I am. Here I am, You have no partner, here I am. Verily all praise, grace, and sovereignty belong to You. You have no partner.",
            translationHi = "मैं हाज़िर हूँ, ऐ अल्लाह! मैं हाज़िर हूँ। तेरा कोई शरीक नहीं, मैं हाज़िर हूँ। बेशक तमाम तारीफ़ें, नेमतें और बादशाही तेरी ही हैं, तेरा कोई शरीक नहीं।",
            translationUr = "میں حاضر ہوں، اے اللہ! میں حاضر ہوں۔ تیرا کوئی شریک نہیں، میں حاضر ہوں۔ بیشک تمام تعریفیں، نعمتیں اور بادشاہی تیری ہی ہے، تیرا کوئی شریک نہیں۔",
            translationAr = "لبيك اللهم لبيك، لبيك لا شريك لك لبيك، إن الحمد والنعمة لك والملك، لا شريك لك.",
            webUrl = "https://sunnah.com/bukhari:1549"
        ),
        "tirmidhi_889" to IslamicReferenceItem(
            id = "tirmidhi_889",
            bookOrSurah = "Jami` at-Tirmidhi (889)",
            grade = "Sahih",
            narrator = "Abdur-Rahman ibn Ya'mar (RA)",
            arabicText = "الْحَجُّ عَرَفَةُ، فَمَنْ أَدْرَكَ لَيْلَةَ عَرَفَةَ قَبْلَ طُلُوعِ الْفَجْرِ مِنْ لَيْلَةِ جَمْعٍ فَقَدْ تَمَّ حَجُّهُ",
            translationEn = "The Messenger of Allah ﷺ ordered an announcer to proclaim: 'Hajj is Arafah! Whoever arrives before dawn on the night of Jam' (Muzdalifah), his Hajj is complete.'",
            translationHi = "रसूलुल्लाह ﷺ ने मुनादी कराने का हुक्म दिया: 'हज अराफ़ा ही है! जो 10 ज़ुल-हज्ज की फ़ज्र से पहले अराफ़ात पहुँच गया, उसका हज मुकम्मल हो गया।' (हज का सबसे बड़ा रुक्न)",
            translationUr = "رسول اللہ ﷺ نے فرمایا: 'حج عرفہ ہی ہے، جو فجر کے طلوع ہونے سے پہلے عرفات پہنچ گیا اس کا حج مکمل ہو گیا۔'",
            translationAr = "الحج عرفة، فمن أدرك ليلة عرفة قبل طلوع الفجر من ليلة جمع فقد تم حجه.",
            webUrl = "https://sunnah.com/tirmidhi:889"
        ),
        "bukhari_1773" to IslamicReferenceItem(
            id = "bukhari_1773",
            bookOrSurah = "Sahih al-Bukhari (1773)",
            grade = "Sahih (Muttafaq 'Alayh)",
            narrator = "Abu Hurairah (RA)",
            arabicText = "العُمْرَةُ إِلَى العُمْرَةِ كَفَّارَةٌ لِمَا بَيْنَهُمَا، وَالحَجُّ المَبْرُورُ لَيْسَ لَهُ جَزَاءٌ إِلَّا الجَنَّةُ",
            translationEn = "The Messenger of Allah ﷺ said: 'Umrah to Umrah is an expiation for what is between them, and an accepted Hajj (Hajj Mabrur) has no reward except Paradise.'",
            translationHi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'उमरा दूसरे उमरे तक के गुनाहों का कफ़्फ़ारा है, और हज-ए-मबरूर (मक़बूल हज) का बदला सिर्फ़ और सिर्फ़ जन्नत ही है।'",
            translationUr = "رسول اللہ ﷺ نے فرمایا: 'عمرہ دوسرے عمرے تک کے گناہوں کا کفارہ ہے اور حج مبرور کا بدلہ سوائے جنت کے کچھ نہیں۔'",
            translationAr = "العمرة إلى العمرة كفارة لما بينهما، والحج المبرور ليس له جزاء إلا الجنة.",
            webUrl = "https://sunnah.com/bukhari:1773"
        ),
        "bukhari_1521" to IslamicReferenceItem(
            id = "bukhari_1521",
            bookOrSurah = "Sahih al-Bukhari (1521)",
            grade = "Sahih",
            narrator = "Abu Hurairah (RA)",
            arabicText = "مَنْ حَجَّ لِلَّهِ فَلَمْ يَرْفُثْ، وَلَمْ يَفْسُقْ، رَجَعَ كَيَوْمِ وَلَدَتْهُ أُمُّهُ",
            translationEn = "The Prophet ﷺ said: 'Whoever performs Hajj for Allah's sake and does not have sexual relations with his wife, and does not do evil or sins, then he will return (as pure and free from sin) as on the day on which his mother gave birth to him.'",
            translationHi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'जिसने अल्लाह के लिए हज किया, न बेहूदा बात की और न कोई गुनाह किया, वह गुनाहों से ऐसा पाक-साफ़ होकर लौटता है जैसे उस दिन था जब उसकी माँ ने उसे जना था।'",
            translationUr = "رسول اللہ ﷺ نے فرمایا: 'جس نے اللہ کے لیے حج کیا اور نہ کوئی فحش بات کی نہ گناہ کیا، وہ گناہوں سے ایسا پاک ہو کر لوٹتا ہے جیسے اس دن تھا جب اس کی ماں نے اسے جنا تھا۔'",
            translationAr = "من حج لله فلم يرفث ولم يفسق رجع كيوم ولدته أمه.",
            webUrl = "https://sunnah.com/bukhari:1521"
        ),
        "muslim_1218" to IslamicReferenceItem(
            id = "muslim_1218",
            bookOrSurah = "Sahih Muslim (1218)",
            grade = "Sahih",
            narrator = "Jabir ibn Abdullah (RA)",
            arabicText = "لِتَأْخُذُوا مَنَاسِكَكُمْ فَإِنِّي لَا أَدْرِي لَعَلِّي لَا أَحُجُّ بَعْدَ حَجَّتِي هَذِهِ",
            translationEn = "The Messenger of Allah ﷺ said: 'Learn your rituals from me, for I do not know whether I will perform Hajj after this Hajj of mine.'",
            translationHi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'मुझसे अपने हज के तरीक़े (मनासिक) सीख लो, शायद मैं अपने इस हज के बाद दूसरा हज न कर सकूँ।'",
            translationUr = "رسول اللہ ﷺ نے فرمایا: 'مجھ سے اپنے حج کے مناسک سیکھ لو، کیونکہ مجھے معلوم نہیں کہ شاید میں اس حج کے بعد دوسرا حج نہ کر سکوں۔'",
            translationAr = "لتأخذوا مناسككم فإني لا أدري لعلي لا أحج بعد حجتي هذه.",
            webUrl = "https://sunnah.com/muslim:1218"
        ),
        "quran_2_165" to IslamicReferenceItem(
            id = "quran_2_165",
            bookOrSurah = "Surah Al-Baqarah (2:165)",
            grade = "Quranic Ayah",
            arabicText = "وَمِنَ النَّاسِ مَن يَتَّخِذُ مِن دُونِ اللَّهِ أَندَادًا يُحِبُّونَهُمْ كَحُبِّ اللَّهِ ۖ وَالَّذِينَ آمَنُوا أَشَدُّ حُبًّا لِّلَّهِ",
            translationEn = "And [yet], among the people are those who take other than Allah as equals [to Him]. They love them as they [should] love Allah. But those who believe are stronger in love for Allah.",
            translationHi = "और लोगों में से कुछ ऐसे हैं जो अल्लाह के सिवा दूसरों को उसका समकक्ष ठहराते हैं और उनसे अल्लाह जैसा प्रेम करते हैं, जबकि ईमान वाले सबसे बढ़कर अल्लाह से प्रेम करते हैं।",
            translationUr = "اور لوگوں میں سے بعض ایسے ہیں جو اللہ کے سوا دوسروں کو اس کا شریک بناتے ہیں اور ان سے ایسی محبت کرتے ہیں جیسی اللہ سے کرنی چاہیے، جبکہ ایمان والے سب سے بڑھ کر اللہ سے محبت کرتے ہیں۔",
            translationAr = "وَمِنَ النَّاسِ مَن يَتَّخِذُ مِن دُونِ اللَّهِ أَندَادًا يُحِبُّونَهُمْ كَحُبِّ اللَّهِ وَالَّذِينَ آمَنُوا أَشَدُّ حُبًّا لِّلَّهِ.",
            webUrl = "https://quran.com/2/165"
        ),
        "quran_31_22" to IslamicReferenceItem(
            id = "quran_31_22",
            bookOrSurah = "Surah Luqman (31:22)",
            grade = "Quranic Ayah",
            arabicText = "وَمَن يُسْلِمْ وَجْهَهُ إِلَى اللَّهِ وَهُوَ مُحْسِنٌ فَقَدِ اسْتَمْسَكَ بِالْعُرْوَةِ الْوُثْقَىٰ",
            translationEn = "And whoever submits his face to Allah while he is a doer of good - then he has grasped the most trustworthy handhold.",
            translationHi = "और जो कोई अपना मुख अल्लाह की ओर समर्पित कर दे और वह नेकी करने वाला हो, तो उसने सबसे मज़बूत सहारा थाम लिया।",
            translationUr = "اور جو شخص نیکی کے ساتھ اپنا چہرہ اللہ کے آگے جھکا دے، اس نے سب سے مضبوط سہارا تھام لیا۔",
            translationAr = "ومن يسلم وجهه إلى الله وهو محسن فقد استمسك بالعروة الوثقى.",
            webUrl = "https://quran.com/31/22"
        ),
        "quran_37_35" to IslamicReferenceItem(
            id = "quran_37_35",
            bookOrSurah = "Surah As-Saffat (37:35)",
            grade = "Quranic Ayah",
            arabicText = "إِنَّهُمْ كَانُوا إِذَا قِيلَ لَهُمْ لَا إِلَٰهَ إِلَّا اللَّهُ يَسْتَكْبِرُونَ",
            translationEn = "Indeed they, when it was said to them, 'There is no deity except Allah,' were arrogant.",
            translationHi = "निस्संदेह, जब उनसे कहा जाता था कि 'अल्लाह के सिवा कोई पूज्य नहीं', तो वे घमंड करते थे।",
            translationUr = "بے شک جب ان سے کہا جاتا تھا کہ ”اللہ کے سوا کوئی معبود نہیں“ تو وہ تکبر کیا کرتے تھے۔",
            translationAr = "إنهم كانوا إذا قيل لهم لا إله إلا الله يستكبرون.",
            webUrl = "https://quran.com/37/35"
        ),
        "quran_5_6" to IslamicReferenceItem(
            id = "quran_5_6",
            bookOrSurah = "Surah Al-Ma'idah (5:6)",
            grade = "Quranic Ayah",
            arabicText = "يَا أَيُّهَا الَّذِينَ آمَنُوا إِذَا قُمْتُمْ إِلَى الصَّلَاةِ فَاغْسِلُوا وُجُوهَكُمْ وَأَيْدِيَكُمْ إِلَى الْمَرَافِقِ وَامْسَحُوا بِرُءُوسِكُمْ وَأَرْجُلَكُمْ إِلَى الْكَعْبَيْنِ",
            translationEn = "O you who have believed, when you rise to [perform] prayer, wash your faces and your forearms to the elbows and wipe over your heads and wash your feet to the ankles.",
            translationHi = "ऐ ईमान वालो! जब तुम नमाज़ के लिए खड़े हो, तो अपने चेहरे और अपनी कोहनियों तक हाथ धो लो, और अपने सिरों पर मसह करो और टखनों तक पैर धो लो।",
            translationUr = "اے ایمان والو! جب تم نماز کے لیے کھڑے ہو تو اپنے چہرے اور کہنیوں تک ہاتھ دھو لو، اور اپنے سروں کا مسح کرو اور ٹخنوں تک پاؤں دھو لو।",
            translationAr = "يا أيها الذين آمنوا إذا قمتم إلى الصلاة فاغسلوا وجوهكم وأيديكم إلى المرافق وامسحوا برءوسكم وأرجلكم إلى الكعبين.",
            webUrl = "https://quran.com/5/6"
        ),
        "quran_2_144" to IslamicReferenceItem(
            id = "quran_2_144",
            bookOrSurah = "Surah Al-Baqarah (2:144)",
            grade = "Quranic Ayah",
            arabicText = "فَوَلِّ وَجْهَكَ شَطْرَ الْمَسْجِدِ الْحَرَامِ ۚ وَحَيْثُ مَا كُنتُمْ فَوَلُّوا وُجُوهَكُمْ شَطْرَهُ",
            translationEn = "So turn your face toward al-Masjid al-Haram. And wherever you [believers] are, turn your faces toward it [in prayer].",
            translationHi = "तो अपना मुख मस्जिदे हराम (काबा) की ओर फेर लो; और तुम जहाँ कहीं भी हो, अपने चेहरे उसी की ओर फेरो।",
            translationUr = "پس اپنا رخ مسجد حرام کی طرف پھیر لو، اور تم جہاں کہیں بھی ہو اپنے چہرے اسی کی طرف پھیرو।",
            translationAr = "فول وجهك شطر المسجد الحرام وحيث ما كنتم فولوا وجوهكم شطره.",
            webUrl = "https://quran.com/2/144"
        ),
        "bukhari_1" to IslamicReferenceItem(
            id = "bukhari_1",
            bookOrSurah = "Sahih al-Bukhari (1)",
            grade = "Sahih (Muttafaq 'Alayh)",
            narrator = "Umar ibn al-Khattab (RA)",
            arabicText = "إِنَّمَا الأَعْمَالُ بِالنِّيَّاتِ، وَإِنَّمَا لِكُلِّ امْرِئٍ مَا نَوَى",
            translationEn = "Actions are but by intention and every person will have but that which he intended.",
            translationHi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'कर्मों का दारोमदार नीयत पर है, और हर इंसान के लिए वही है जिसकी उसने नीयत की।'",
            translationUr = "رسول اللہ ﷺ نے فرمایا: 'اعمال کا دارومدار نیتوں پر ہے، اور ہر شخص کے لیے وہی ہے جس کی اس نے نیت کی۔'",
            translationAr = "إنما الأعمال بالنيات وإنما لكل امرئ ما نوى.",
            webUrl = "https://sunnah.com/bukhari:1"
        ),
        "bukhari_756" to IslamicReferenceItem(
            id = "bukhari_756",
            bookOrSurah = "Sahih al-Bukhari (756)",
            grade = "Sahih",
            narrator = "Ubadah ibn as-Samit (RA)",
            arabicText = "لاَ صَلاَةَ لِمَنْ لَمْ يَقْرَأْ بِفَاتِحَةِ الْكِتَابِ",
            translationEn = "There is no prayer for the one who does not recite the Opening of the Book (Surah Al-Fatihah).",
            translationHi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'उस व्यक्ति की कोई नमाज़ नहीं जो सूरह अल-फ़ातिहा न पढ़े।'",
            translationUr = "رسول اللہ ﷺ نے فرمایا: 'اس شخص کی نماز نہیں ہوتی جس نے سورۃ الفاتحہ نہ پڑھی۔'",
            translationAr = "لا صلاة لمن لم يقرأ بفاتحة الكتاب.",
            webUrl = "https://sunnah.com/bukhari:756"
        ),
        "bukhari_743" to IslamicReferenceItem(
            id = "bukhari_743",
            bookOrSurah = "Sahih al-Bukhari (743)",
            grade = "Sahih",
            narrator = "Abdullah ibn Umar (RA)",
            arabicText = "اللَّهُمَّ بَاعِدْ بَيْنِي وَبَيْنَ خَطَايَايَ كَمَا بَاعَدْتَ بَيْنَ الْمَشْرِقِ وَالْمَغْرِبِ",
            translationEn = "O Allah, distance between me and my sins just as You have distanced between the East and the West.",
            translationHi = "रसूलुल्लाह ﷺ तकबीर और क़िराअत के बीच में यह दुआ पढ़ते थे: ऐ अल्लाह! मेरे और मेरे गुनाहों के दरमियान उतनी दूरी पैदा कर दे जितनी पूरब और पश्चिम के दरमियान है।",
            translationUr = "رسول اللہ ﷺ تکبیر اور قرات کے درمیان دعا فرماتے: اے اللہ! میرے اور میرے گناہوں کے درمیان اتنی دوری پیدا فرما دے جتنی مشرق اور مغرب کے درمیان ہے۔",
            translationAr = "اللهم باعد بيني وبين خطاياي كما باعدت بين المشرق والمغرب.",
            webUrl = "https://sunnah.com/bukhari:743"
        ),
        "muslim_772" to IslamicReferenceItem(
            id = "muslim_772",
            bookOrSurah = "Sahih Muslim (772)",
            grade = "Sahih",
            narrator = "Aisha (RA)",
            arabicText = "سُبْحَانَ رَبِّيَ الْعَظِيمِ ، سُبْحَانَ رَبِّيَ الأَعْلَى",
            translationEn = "Glory be to my Lord, the Most Great (in Ruku); Glory be to my Lord, the Most High (in Sujood).",
            translationHi = "रसूलुल्लाह ﷺ रुकू में 'सुब्हाना रब्बियल अज़ीम' और सजदे में 'सुब्हाना रब्बियल आला' फ़रमाते थे।",
            translationUr = "رسول اللہ ﷺ رکوع میں 'سبحان ربی العظیم' اور سجدے میں 'سبحان ربی الاعلیٰ' فرماتے تھے۔",
            translationAr = "سبحان ربي العظيم ، سبحان ربي الأعلى.",
            webUrl = "https://sunnah.com/muslim:772"
        ),
        "bukhari_795" to IslamicReferenceItem(
            id = "bukhari_795",
            bookOrSurah = "Sahih al-Bukhari (795)",
            grade = "Sahih",
            narrator = "Rifa'ah ibn Rafi' (RA)",
            arabicText = "سَمِعَ اللَّهُ لِمَنْ حَمِدَهُ ، رَبَّنَا وَلَكَ الْحَمْدُ",
            translationEn = "Allah hears whoever praises Him. Our Lord, and to You belongs all praise.",
            translationHi = "रसूलुल्लाह ﷺ जब रुकू से सिर उठाते तो कहते: 'समिअल्लाहु लिमन हमिदह' और सीधे खड़े होकर 'रब्बना व लकल हम्द' कहते।",
            translationUr = "رسول اللہ ﷺ رکوع سے سر اٹھاتے تو فرماتے: 'سمع اللہ لمن حمدہ' اور کھڑے ہو کر فرماتے: 'ربنا ولک الحمد'۔",
            translationAr = "سمع الله لمن حمده ، ربنا ولك الحمد.",
            webUrl = "https://sunnah.com/bukhari:795"
        ),
        "bukhari_828" to IslamicReferenceItem(
            id = "bukhari_828",
            bookOrSurah = "Sahih al-Bukhari (828)",
            grade = "Sahih",
            narrator = "Ibn Abbas (RA)",
            arabicText = "أُمِرْتُ أَنْ أَسْجُدَ عَلَى سَبْعَةِ أَعْظُمٍ: عَلَى الْجَبْهَةِ، وَالْيَدَيْنِ، وَالرُّكْبَتَيْنِ، وَأَطْرَافِ الْقَدَمَيْنِ",
            translationEn = "I have been commanded to prostrate on seven bones: on the forehead, the two hands, the two knees, and the ends of the two feet.",
            translationHi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'मुझे हुक्म दिया गया है कि मैं सात अंगों पर सजदा करूँ: पेशानी, दोनों हाथ, दोनों घुटने और दोनों पाँवों की उँगलियाँ।'",
            translationUr = "رسول اللہ ﷺ نے فرمایا: 'مجھے حکم دیا گیا ہے کہ میں سات ہڈیوں پر سجدہ کروں: پیشانی، دونوں ہاتھ، دونوں گھٹنے اور دونوں پاؤں کی انگلیاں۔'",
            translationAr = "أمرت أن أسجد على سبعة أعظم: على الجبهة واليدين والركبتين وأطراف القدمين.",
            webUrl = "https://sunnah.com/bukhari:828"
        ),
        "bukhari_831" to IslamicReferenceItem(
            id = "bukhari_831",
            bookOrSurah = "Sahih al-Bukhari (831) / Muslim 402",
            grade = "Sahih (Muttafaq 'Alayh)",
            narrator = "Abdullah ibn Mas'ud (RA)",
            arabicText = "التَّحِيَّاتُ لِلَّهِ وَالصَّلَوَاتُ وَالطَّيِّبَاتُ، السَّلاَمُ عَلَيْكَ أَيُّهَا النَّبِيُّ وَرَحْمَةُ اللَّهِ وَبَرَكَاتُهُ",
            translationEn = "All compliments, prayers and pure words are due to Allah. Peace be upon you, O Prophet, and the mercy of Allah and His blessings.",
            translationHi = "रसूलुल्लाह ﷺ ने हमें तशह्हुद सिखाया: अत-तहिय्यातु लिल्लाहि वस-सलवातु वत-तय्यिबातु, अस-सलामु अलयका अय्युहन-नबिय्यु व रहमतुल्लाहि व बरकातुह...",
            translationUr = "رسول اللہ ﷺ نے ہمیں تشہد سکھایا: التحیات للہ والصلوات والطیبات، السلام علیک ایھا النبی ورحمۃ اللہ وبرکاتہ...",
            translationAr = "التحيات لله والصلوات والطيبات السلام عليك أيها النبي ورحمة الله وبركاته.",
            webUrl = "https://sunnah.com/bukhari:831"
        ),
        "bukhari_3370" to IslamicReferenceItem(
            id = "bukhari_3370",
            bookOrSurah = "Sahih al-Bukhari (3370)",
            grade = "Sahih (Muttafaq 'Alayh)",
            narrator = "Ka'b ibn Ujrah (RA)",
            arabicText = "اللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ وَعَلَى آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلَى إِبْرَاهِيمَ وَعَلَى آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ",
            translationEn = "O Allah, send blessings upon Muhammad and upon the family of Muhammad, as You sent blessings upon Ibrahim and upon the family of Ibrahim. Indeed, You are Praiseworthy and Glorious.",
            translationHi = "ऐ अल्लाह! हज़रत मुहम्मद ﷺ और उनकी आल पर रहमत नाज़िल फ़रमा, जिस तरह तूने इब्राहीम (अ.) और उनकी आल पर रहमत नाज़िल फ़रमाई...",
            translationUr = "اے اللہ! محمد ﷺ اور ان کی آل پر رحمت نازل فرما جس طرح تو نے ابراہیمؑ اور ان کی آل پر رحمت نازل فرمائی...",
            translationAr = "اللهم صل على محمد وعلى آل محمد كما صليت على إبراهيم وعلى آل إبراهيم إنك حميد مجيد.",
            webUrl = "https://sunnah.com/bukhari:3370"
        ),
        "muslim_582" to IslamicReferenceItem(
            id = "muslim_582",
            bookOrSurah = "Sahih Muslim (582) / Bukhari 833",
            grade = "Sahih",
            narrator = "Amir ibn Sa'd (RA)",
            arabicText = "السَّلاَمُ عَلَيْكُمْ وَرَحْمَةُ اللَّهِ ، السَّلاَمُ عَلَيْكُمْ وَرَحْمَةُ اللَّهِ",
            translationEn = "Peace be upon you and the mercy of Allah; Peace be upon you and the mercy of Allah.",
            translationHi = "रसूलुल्लाह ﷺ नमाज़ पूरी करने पर दायें और बायें सलाम फेरते थे: 'अस्सलाम अलैकुम व रहमतुल्लाह'।",
            translationUr = "رسول اللہ ﷺ نماز کے اختتام پر دائیں اور بائیں سلام پھیرتے: 'السلام علیکم ورحمۃ اللہ'۔",
            translationAr = "السلام عليكم ورحمة الله ، السلام عليكم ورحمة الله.",
            webUrl = "https://sunnah.com/muslim:582"
        ),
        "abudawud_425" to IslamicReferenceItem(
            id = "abudawud_425",
            bookOrSurah = "Sunan Abi Dawud (425) / Tirmidhi 413",
            grade = "Sahih",
            narrator = "Abu Hurairah (RA)",
            arabicText = "وَقْتُ الظُّهْرِ إِذَا زَالَتِ الشَّمْسُ... وَوَقْتُ الْعَصْرِ مَا لَمْ تَصْفَرَّ الشَّمْسُ",
            translationEn = "The time for Dhuhr is when the sun passes its meridian... and the time for Asr is as long as the sun has not turned pale.",
            translationHi = "रसूलुल्लाह ﷺ ने पाँचों वक़्त की नमाज़ों के मुक़र्ररा औक़ात की तफ़सील बयान फ़रमाई।",
            translationUr = "رسول اللہ ﷺ نے پانچوں نمازوں کے اوقات مقررہ کی تفصیلی رہنمائی فرمائی।",
            translationAr = "وقت الظهر إذا زالت الشمس ووقت العصر ما لم تصفر الشمس.",
            webUrl = "https://sunnah.com/abudawud:425"
        ),
        "muslim_657" to IslamicReferenceItem(
            id = "muslim_657",
            bookOrSurah = "Sahih Muslim (657) / Bukhari 645",
            grade = "Sahih (Muttafaq 'Alayh)",
            narrator = "Abdullah ibn Umar (RA)",
            arabicText = "صَلاَةُ الْجَمَاعَةِ تَفْضُلُ صَلاَةَ الْفَذِّ بِسَبْعٍ وَعِشْرِينَ دَرَجَةً",
            translationEn = "Prayer in congregation is twenty-seven degrees more virtuous than prayer performed alone.",
            translationHi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'जमाअत के साथ नमाज़ पढ़ना अकेले नमाज़ पढ़ने से सत्ताईस (27) दर्जे ज़्यादा अफ़ज़ल है।'",
            translationUr = "رسول اللہ ﷺ نے فرمایا: 'باجماعت نماز تنہا نماز سے ستائیس (27) درجے فضیلت رکھتی ہے۔'",
            translationAr = "صلاة الجماعة تفضل صلاة الفذ بسبع وعشرين درجة.",
            webUrl = "https://sunnah.com/muslim:657"
        ),
        "abudawud_2358" to IslamicReferenceItem(
            id = "abudawud_2358",
            bookOrSurah = "Sunan Abi Dawud (2358)",
            grade = "Hasan",
            narrator = "Mu'adh ibn Zuhrah (RA)",
            arabicText = "ذَهَبَ الظَّمَأُ وَابْتَلَّتِ الْعُرُوقُ وَثَبَتَ الأَجْرُ إِنْ شَاءَ اللَّهُ",
            translationEn = "The thirst has gone, the veins are moistened, and the reward is confirmed, if Allah wills.",
            translationHi = "इफ़्तार के वक़्त की मसनून दुआ: प्यास बुझ गई, नसें तर-ओ-ताज़ा हो गईं और अल्लाह ने चाहा तो सवाब साबित हो गया।",
            translationUr = "افطار کی مسنون دعا: پیاس چلی گئی، رگیں تر ہو گئیں اور ان شاء اللہ اجر ثابت ہو گیا۔",
            translationAr = "ذهب الظمأ وابتلت العروق وثبت الأجر إن شاء الله.",
            webUrl = "https://sunnah.com/abudawud:2358"
        ),
        "bukhari_1896" to IslamicReferenceItem(
            id = "bukhari_1896",
            bookOrSurah = "Sahih al-Bukhari (1896)",
            grade = "Sahih (Muttafaq 'Alayh)",
            narrator = "Sahl ibn Sa'd (RA)",
            arabicText = "إِنَّ فِي الْجَنَّةِ بَابًا يُقَالُ لَهُ الرَّيَّانُ يَدْخُلُ مِنْهُ الصَّائِمُونَ يَوْمَ الْقِيَامَةِ",
            translationEn = "Indeed, in Paradise there is a gate called Ar-Rayyan through which only those who observed fasting will enter on the Day of Resurrection.",
            translationHi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'जन्नत में एक दरवाज़ा है जिसे 'अर-रय्यान' कहा जाता है, क़ियामत के दिन उससे सिर्फ़ रोज़ेदार ही दाख़िल होंगे।'",
            translationUr = "رسول اللہ ﷺ نے فرمایا: 'جنت میں ایک دروازہ ہے جسے 'الریان' کہا جاتا ہے جس سے روز قیامت صرف روزے دار داخل ہوں گے۔'",
            translationAr = "إن في الجنة بابا يقال له الريان يدخل منه الصائمون يوم القيامة.",
            webUrl = "https://sunnah.com/bukhari:1896"
        ),
        "bukhari_1981" to IslamicReferenceItem(
            id = "bukhari_1981",
            bookOrSurah = "Sahih al-Bukhari (1981)",
            grade = "Sahih",
            narrator = "Abu Hurairah (RA)",
            arabicText = "أَوْصَانِي خَلِيلِي بِثَلاَثٍ: صِيَامِ ثَلاَثَةِ أَيَّامٍ مِنْ كُلِّ شَهْرٍ، وَرَكْعَتَىِ الضُّحَى، وَأَنْ أُوتِرَ قَبْلَ أَنْ أَنَامَ",
            translationEn = "My beloved friend advised me to do three things: to fast three days of every month, to pray the two Rak`ahs of Duha, and to pray Witr before sleeping.",
            translationHi = "हज़रत अबू हुरैरा (रज़ि.) कहते हैं: मेरे महबूब रसूल ﷺ ने मुझे तीन चीज़ों की वसीयत फ़रमाई: हर महीने तीन दिन (अय्याम-ए-बीज़) के रोज़े रखना, चाश्त की दो रकअत पढ़ना, और सोने से पहले वित्र पढ़ना।",
            translationUr = "حضرت ابو ہریرہؓ فرماتے ہیں: میرے پیارے رسول ﷺ نے مجھے تین چیزوں کی وصیت فرمائی: ہر مہینے تین دن روزہ رکھنا، چاشت کی دو رکعتیں اور سونے سے پہلے وتر پڑھنا।",
            translationAr = "أوصاني خليلي بثلاث صيام ثلاثة أيام من كل شهر وركعتي الضحى وأن أوتر قبل أن أنام.",
            webUrl = "https://sunnah.com/bukhari:1981"
        ),
        "tirmidhi_747" to IslamicReferenceItem(
            id = "tirmidhi_747",
            bookOrSurah = "Jami` at-Tirmidhi (747)",
            grade = "Sahih",
            narrator = "Abu Hurairah (RA)",
            arabicText = "تُعْرَضُ الأَعْمَالُ يَوْمَ الاِثْنَيْنِ وَالْخَمِيسِ فَأُحِبُّ أَنْ يُعْرَضَ عَمَلِي وَأَنَا صَائِمٌ",
            translationEn = "Deeds are presented on Monday and Thursday, and I love that my deeds be presented while I am fasting.",
            translationHi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'पीर और जुमेरात के दिन बन्दों के आमाल पेश किए जाते हैं, इसलिए मैं पसंद करता हूँ कि मेरा अमल इस हाल में पेश हो कि मैं रोज़े से हूँ।'",
            translationUr = "رسول اللہ ﷺ نے فرمایا: 'پیر اور جمعرات کو اعمال پیش کیے جاتے हैं, پس مجھے پسند ہے کہ میرا عمل اس حالت میں پیش ہو کہ میں روزے دار ہوں۔'",
            translationAr = "تعرض الأعمال يوم الاثنين والخميس فأحب أن يعرض عملي وأنا صائم.",
            webUrl = "https://sunnah.com/tirmidhi:747"
        ),
        "bukhari_1744" to IslamicReferenceItem(
            id = "bukhari_1744",
            bookOrSurah = "Sahih al-Bukhari (1744)",
            grade = "Sahih",
            narrator = "Ibn Umar (RA)",
            arabicText = "الْحَجُّ الْمَبْرُورُ لَيْسَ لَهُ جَزَاءٌ إِلاَّ الْجَنَّةُ",
            translationEn = "An accepted Hajj (Hajj Mabrur) has no reward other than Paradise.",
            translationHi = "रसूलुल्लाह ﷺ ने फ़रमाया: 'हज्जे मबरूर (मक़बूल हज्ज) का बदला सिर्फ़ और सिर्फ़ जन्नत है।'",
            translationUr = "رسول اللہ ﷺ ने فرمایا: 'حج مبرور کا بدلہ سوائے جنت کے کچھ نہیں۔'",
            translationAr = "الحج المبرور ليس له جزاء إلا الجنة.",
            webUrl = "https://sunnah.com/bukhari:1744"
        ),
                "muslim_1350" to IslamicReferenceItem(
            id = "muslim_1350",
            bookOrSurah = "Sahih Muslim (1350)",
            grade = "Sahih",
            narrator = "Ibn Abbas (RA)",
            arabicText = "أُمِرَ النَّاسُ أَنْ يَكُونَ آخِرُ عَهْدِهِمْ بِالْبَيْتِ، إِلاَّ أَنَّهُ خُفِّفَ عَنِ الْمَرْأَةِ الْحَائِضِ",
            translationEn = "The people were ordered that their last engagement should be with the House (Ka'bah), but an exception was made for menstruating women.",
            translationHi = "लोगों को हुक्म दिया गया कि उनका आखिरी अहद बैतुल्लाह (तवाफ़-ए-विदा) होना चाहिए, सिवाय इसके कि माहवारी वाली औरतों के लिए इसमें आसानी दी गई।",
            translationUr = "لوگوں کو حکم دیا گیا کہ ان کا آخری عہد بیت اللہ (طواف وداع) ہونا چاہیے لیکن حیض والی عورتوں کو اس سے رخصت دی گئی۔",
            translationAr = "أمر الناس أن يكون آخر عهدهم بالبيت إلا أنه خفف عن الحائض.",
            webUrl = "https://sunnah.com/muslim:1350"
        )
    )

    fun getReference(id: String): IslamicReferenceItem? {
        return references[id]
    }

    /**
     * Resolves an IslamicReferenceItem by key or fuzzy text (e.g. "Bukhari 128", "Surah Al-Bayyinah 98:5").
     * If no exact key matches, it extracts numbers and generates a direct Sunnah.com/Quran.com link!
     */
    fun findReference(query: String, defaultTitle: String = ""): IslamicReferenceItem {
        val q = query.lowercase().trim()
        val direct = references[q]
        if (direct != null) return direct

        // Search in keys or bookOrSurah
        for ((_, item) in references) {
            val titleLow = item.bookOrSurah.lowercase()
            if (q.contains(item.id.replace("_", " ")) || titleLow.contains(q) || q.contains(titleLow)) {
                return item
            }
        }
        // Fuzzy match common names
        return when {
            q.contains("bayyinah") || q.contains("98:5") || q.contains("98") -> references["quran_98_5"]!!
            q.contains("47:19") || q.contains("muhammad") -> references["quran_47_19"]!!
            q.contains("33:40") || q.contains("ahzab") -> references["quran_33_40"]!!
            q.contains("48:29") || q.contains("fath") -> references["quran_48_29"]!!
            q.contains("2:165") -> references["quran_2_165"]!!
            q.contains("31:22") || q.contains("luqman") -> references["quran_31_22"]!!
            q.contains("37:35") || q.contains("saffat") -> references["quran_37_35"]!!
            q.contains("5:6") || q.contains("maidah") -> references["quran_5_6"]!!
            q.contains("2:144") -> references["quran_2_144"]!!
            q.contains("128") && q.contains("bukhari") -> references["bukhari_128"]!!
            q.contains("234") && q.contains("muslim") -> references["muslim_234"]!!
            q.contains("3293") || q.contains("6403") -> references["bukhari_3293"]!!
            q.contains("2691") || q.contains("tamjeed") -> references["muslim_2691"]!!
            q.contains("1517") || q.contains("astaghfar") -> references["abudawud_1517"]!!
            q.contains("716") || q.contains("adab") -> references["adab_716"]!!
            q.contains("26") && q.contains("muslim") -> references["muslim_26"]!!
            q.contains("99") && q.contains("bukhari") -> references["bukhari_99"]!!
            q.contains("3116") -> references["abudawud_3116"]!!
            q.contains("32") && q.contains("muslim") -> references["muslim_32"]!!

            q.contains("2:43") || q.contains("2: 43") -> references["quran_2_43"]!!
            q.contains("29:45") -> references["quran_29_45"]!!
            q.contains("4:103") || q.contains("103") -> references["quran_4_103"]!!
            q.contains("2550") || q.contains("baihaqi") || q.contains("bayhaqi") -> references["baihaqi_2550"]!!
            q.contains("349") -> references["bukhari_349"]!!
            q.contains("757") -> references["bukhari_757"]!!
            q.contains("756") || q.contains("fatihah") -> references["bukhari_756"]!!
            q.contains("743") -> references["bukhari_743"]!!
            q.contains("772") -> references["muslim_772"]!!
            q.contains("795") -> references["bukhari_795"]!!
            q.contains("828") -> references["bukhari_828"]!!
            q.contains("831") || q.contains("402") || q.contains("tashahhud") -> references["bukhari_831"]!!
            q.contains("3370") || q.contains("durood") || q.contains("ibrahim") -> references["bukhari_3370"]!!
            q.contains("582") || q.contains("833") || q.contains("tasleem") -> references["muslim_582"]!!
            q.contains("425") -> references["abudawud_425"]!!
            q.contains("657") -> references["muslim_657"]!!
            q.contains("528") -> references["bukhari_528"]!!
            q.contains("223") && q.contains("muslim") -> references["muslim_223"]!!
            q.contains("tirmidhi 4") || q.contains("ترمذي 4") -> references["tirmidhi_4"]!!

            q.contains("9:60") -> references["quran_9_60"]!!
            q.contains("1573") -> references["abudawud_1573"]!!
            q.contains("tabarani") || q.contains("طبراني") -> references["tabarani_zakat"]!!

            q.contains("2:183") -> references["quran_2_183"]!!
            q.contains("2358") -> references["abudawud_2358"]!!
            q.contains("2357") -> references["abudawud_2357"]!!
            q.contains("1933") -> references["bukhari_1933"]!!
            q.contains("1896") || q.contains("rayyan") -> references["bukhari_1896"]!!
            q.contains("1981") -> references["bukhari_1981"]!!
            q.contains("747") -> references["tirmidhi_747"]!!
            q.contains("720") -> references["tirmidhi_720"]!!
            q.contains("1164") -> references["muslim_1164"]!!
            q.contains("1162") -> references["muslim_1162"]!!
            q.contains("1894") || q.contains("1904") -> references["bukhari_1894"]!!

            q.contains("3:97") -> references["quran_3_97"]!!
            q.contains("1549") || q.contains("1184") -> references["bukhari_1549"]!!
            q.contains("889") -> references["tirmidhi_889"]!!
            q.contains("1773") -> references["bukhari_1773"]!!
            q.contains("1744") -> references["bukhari_1744"]!!
            q.contains("1521") -> references["bukhari_1521"]!!
            q.contains("1350") -> references["muslim_1350"]!!
            q.contains("1218") -> references["muslim_1218"]!!
            q.contains("bukhari 1") || (q.contains("niyyah") && q.contains("bukhari")) -> references["bukhari_1"]!!
            q.contains("8") && q.contains("bukhari") -> references["bukhari_8"]!!

            // Fallback: smart dynamic generation with exact URL extraction
            else -> {
                val cleanTitle = defaultTitle.ifEmpty { query }
                
                // 1. Check if it is a Quran verse e.g. "2:183" or "(47:19)"
                val ayahMatch = Regex("(\\d{1,3})\\s*[:?]\\s*(\\d{1,3})").find(q)
                if (ayahMatch != null) {
                    val surah = ayahMatch.groupValues[1]
                    val ayah = ayahMatch.groupValues[2]
                    return IslamicReferenceItem(
                        id = "dyn_quran_${surah}_$ayah",
                        bookOrSurah = cleanTitle,
                        grade = "Quranic Ayah",
                        arabicText = "بِسْمِ اللَّهِ الرَّحْمٰنِ الرَّحِيمِ",
                        translationEn = "Noble Ayah from Surah $surah, Verse $ayah. Tap below to read full text and authentic Tafsir on Quran.com.",
                        translationHi = "कुरआन मजीद की मुबारका आयत (सूरह $surah : आयत $ayah)। पूरी आयत व तफ़सीर के लिए नीचे क्लिक करें।",
                        translationUr = "قرآن مجید کی مبارک آیت (سورۃ $surah : آیت $ayah)۔ مکمل تلاوت اور تفسیر کے لیے نیچے بٹن دبائیں۔",
                        webUrl = "https://quran.com/$surah/$ayah"
                    )
                }

                // 2. Check canonical Hadith collections
                val numMatch = Regex("\\d+").find(q)
                val number = numMatch?.value ?: ""
                val targetUrl = when {
                    q.contains("bukhari") && number.isNotEmpty() -> "https://sunnah.com/bukhari:$number"
                    q.contains("muslim") && number.isNotEmpty() -> "https://sunnah.com/muslim:$number"
                    (q.contains("abu dawud") || q.contains("dawud")) && number.isNotEmpty() -> "https://sunnah.com/abudawud:$number"
                    q.contains("tirmidhi") && number.isNotEmpty() -> "https://sunnah.com/tirmidhi:$number"
                    q.contains("nasai") && number.isNotEmpty() -> "https://sunnah.com/nasai:$number"
                    q.contains("ibn majah") && number.isNotEmpty() -> "https://sunnah.com/ibnmajah:$number"
                    else -> {
                        val searchParam = java.net.URLEncoder.encode(cleanTitle, "UTF-8")
                        "https://sunnah.com/search?q=$searchParam"
                    }
                }
                IslamicReferenceItem(
                    id = "dyn_hadith_${System.currentTimeMillis()}",
                    bookOrSurah = cleanTitle,
                    grade = "Authentic Hadith",
                    arabicText = "قَالَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ",
                    translationEn = "Noble narration from canonical Sunnah. Tap below to read the complete authenticated Hadith on Sunnah.com.",
                    translationHi = "रसूलुल्लाह ﷺ की मुबारका हदीस। पूरी हदीस व प्रामाणिक सनद देखने के लिए नीचे दिए गए बटन पर क्लिक करें।",
                    translationUr = "رسول اللہ ﷺ کی مبارک حدیث۔ مکمل حدیث اور سند دیکھنے کے لیے نیچے دیے گئے بٹن پر کلک کریں۔",
                    webUrl = targetUrl
                )
            }
        }
    }
}