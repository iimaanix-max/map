package com.tasbih.digitaltasbih.data.jummah

data class HadithSourceLink(
    val label: String,
    val url: String
)

data class JummahHadithDetail(
    val ref: String,
    val grade: String,
    val links: List<HadithSourceLink>,
    val ar: String,
    val en: String,
    val hi: String = "",
    val ur: String = ""
)

data class JummahSunnahItem(
    val id: String,
    val sequence: String,
    val icon: String,
    val hadiths: Map<String, String>,
    val titles: Map<String, String>,
    val descs: Map<String, String>
)

data class JummahUiPack(
    val badge: String,
    val title: String,
    val hadithHero: String,
    val progressLabel: String,
    val duroodCounterTitle: String,
    val duroodTapBtn: String,
    val duroodReset: String,
    val readKahfBtn: String,
    val mubarakTitle: String,
    val mubarakDesc: String,
    val resetBtn: String
)

object JummahData {

    val items: List<JummahSunnahItem> = listOf(
        JummahSunnahItem(
            id = "miswak",
            sequence = "01",
            icon = "🪥",
            hadiths = mapOf("en" to "Sahih Muslim 846", "hi" to "सहीह मुस्लिम 846", "ur" to "صحیح مسلم 846", "ar" to "صحيح مسلم ٨٤٦", "bn" to "সহীহ মুসলিম ৮৪৬", "ta" to "ஸஹீஹ் முஸ்லிம் 846", "te" to "సహీహ్ ముస్లిం 846", "ml" to "സ്വഹീഹ് മുസ്ലിം 846", "gu" to "સહીહ મુસ્લિમ 846", "mr" to "सहीह मुस्लिम 846", "pa" to "ਸਹੀਹ ਮੁਸਲਿਮ 846", "kn" to "ಸಹೀಹ್ ಮುಸ್ಲಿಮ್ 846", "or" to "ସହିହ ମୁସଲିମ 846", "as" to "ছহিহ মুছলিম ৮৪৬", "id" to "Shahih Muslim 846", "ms" to "Shahih Muslim 846", "tr" to "Sahih-i Müslim 846", "fa" to "صحیح مسلم ۸۴۶", "ha" to "Sahih Muslim 846", "sw" to "Sahih Muslim 846", "so" to "Saxiiq Muslim 846", "fr" to "Sahih Muslim 846", "de" to "Sahih Muslim 846", "ru" to "Сахих Муслим 846", "zh" to "《穆斯林圣训实录》846"),
            titles = mapOf("en" to "Using Miswak & Oral Cleaning", "hi" to "मिस्वाक व दांत साफ़ करना", "ur" to "مسواک کرنا و دانت صاف کرنا", "ar" to "السواك وطهارة الفم", "bn" to "মিসওয়াক করা ও মুখ পরিষ্কার", "ta" to "மிஸ்வாக் செய்தல்", "te" to "మిస్వాక్ చేయడం", "ml" to "മിസ്‌വാക്ക് ചെയ്യുക", "gu" to "મિસ્વાક કરવું", "mr" to "मिस्वाक करणे", "pa" to "ਮਿਸਵਾਕ ਕਰਨਾ", "kn" to "ಮಿಸ್ವಾಕ್ ಮಾಡುವುದು", "or" to "ମିସଓ୍ବାକ କରିବା", "as" to "মিছৱাক কৰা", "id" to "Bersiwak & Membersihkan Gigi", "ms" to "Bersiwak & Membersihkan Gigi", "tr" to "Misvak Kullanmak ve Ağız Temizliği", "fa" to "مسواک زدن و پاکیزگی دهان", "ha" to "Yin Asawaki", "sw" to "Kupiga Mswaki", "so" to "Cadayanashada (Miswaak)", "fr" to "Utiliser le Siwak et nettoyer les dents", "de" to "Verwendung des Siwak & Mundhygiene", "ru" to "Использование сивака и чистка зубов", "zh" to "使用密斯瓦克刷牙洁齿"),
            descs = mapOf("en" to "Cleaning teeth and purifying the mouth before the Friday prayer.", "hi" to "नमाज़-ए-जुमा से पहले मुंह को पाक और ताज़ा रखना।", "ur" to "نمازِ جمعہ سے پہلے منہ کو پاک اور تروتازہ رکھنا۔", "ar" to "تطهير الفم بالسواك قبل صلاة الجمعة.", "bn" to "জুমার নামাযের পূর্বে মিসওয়াক করে মুখ পরিষ্কার রাখা।")
        ),
        JummahSunnahItem(
            id = "ghusl",
            sequence = "02",
            icon = "🚿",
            hadiths = mapOf("en" to "Sahih al-Bukhari 877", "hi" to "सहीह अल-बुख़ारी 877", "ur" to "صحیح بخاری 877", "ar" to "صحيح البخاري ٨٧٧", "bn" to "সহীহ বুখারী ৮৭৭", "ta" to "ஸஹீஹ் புகாரி 877", "te" to "సహీహ్ అల్-బుఖారీ 877", "ml" to "സ്വഹീഹ് അൽ-ബുഖാരി 877", "gu" to "સહીહ અલ-બુખારી 877", "mr" to "सहीह अल-बुखारी 877", "pa" to "ਸਹੀਹ ਅਲ-ਬੁਖ਼ਾਰੀ 877", "kn" to "ಸಹೀಹ್ ಅಲ್-ಬುಖಾರಿ 877", "or" to "ସହିହ ଅଲ-ବୁଖାରୀ 877", "as" to "ছহিহ বুখাৰী ৮৭৭", "id" to "Shahih al-Bukhari 877", "ms" to "Shahih al-Bukhari 877", "tr" to "Sahih-i Buhari 877", "fa" to "صحیح بخاری ۸۷۷", "ha" to "Sahih al-Bukhari 877", "sw" to "Sahih al-Bukhari 877", "so" to "Saxiiq al-Bukhaari 877", "fr" to "Sahih al-Bukhari 877", "de" to "Sahih al-Bukhari 877", "ru" to "Сахих аль-Бухари 877", "zh" to "《布哈里圣训实录》877"),
            titles = mapOf("en" to "Taking a Bath (Ghusl)", "hi" to "ग़ुस्ल करना (जुमे का स्नान)", "ur" to "غسل کرنا", "ar" to "الاغتسال يوم الجمعة", "bn" to "গোসল করা", "ta" to "குளித்தல் (குஸ்ல்)", "te" to "స్నానం చేయడం (గుస్ల్)", "ml" to "കുളിക്കുക (ഗുസ്ൽ)", "gu" to "ગુસ્લ કરવું (સ્નાન)", "mr" to "गुस्ल करणे (स्नान)", "pa" to "ਗੁਸਲ ਕਰਨਾ (ਇਸ਼ਨਾਨ)", "kn" to "ಸ್ನಾನ ಮಾಡುವುದು (ಗುಸ್ಲ್)", "or" to "ଗୋସଲ କରିବା", "as" to "গোসল কৰা", "id" to "Mandi Hari Jumat (Ghusl)", "ms" to "Mandi Sunat Jumaat (Ghusl)", "tr" to "Cuma Guslü Almak", "fa" to "غسل روز جمعه", "ha" to "Wankan Ranar Jumaa", "sw" to "Kuoga Siku ya Ijumaa", "so" to "Maydhashada Jimcaha", "fr" to "Prendre le bain rituel (Ghusl)", "de" to "Das Freitagsbad (Ghusl) vollziehen", "ru" to "Совершение полного омовения (Гусль)", "zh" to "主麻日沐浴（大净）"),
            descs = mapOf("en" to "Taking a bath on Friday is an emphasized Sunnah for purification.", "hi" to "जुमे के दिन ग़ुस्ल करना सुन्नते मौकदा है।", "ur" to "جمعہ کے دن غسل کرنا سنتِ مؤکدہ ہے۔", "ar" to "الغسل يوم الجمعة سنة مؤكدة للطهارة.", "bn" to "জুমার দিন গোসল করা সুন্নাতে মুয়াক্কাদাহ।")
        ),
        JummahSunnahItem(
            id = "nails",
            sequence = "03",
            icon = "✂️",
            hadiths = mapOf("en" to "Sunan al-Bayhaqi", "hi" to "सुनन अल-बैहक़ी", "ur" to "سنن البیہقی", "ar" to "سنن البيهقي", "bn" to "সুনানুল বায়হাকী", "ta" to "ஸுனன் பைஹகீ", "te" to "సునన్ అల్-బైహఖీ", "ml" to "സുനൻ അൽ-ബൈഹഖി", "gu" to "સુનન અલ-બયહકી", "mr" to "सुनन अल-बैहकी", "pa" to "ਸੁਨਨ ਅਲ-ਬੈਹਕੀ", "kn" to "ಸುನನ್ ಅಲ್-ಬೈಹಖಿ", "or" to "ସୁନନ ଅଲ-ବାଇହାକୀ", "as" to "সুনান বাইহাকী", "id" to "Sunan al-Baihaqi", "ms" to "Sunan al-Baihaqi", "tr" to "Sünen-i Beyhaki", "fa" to "سنن بیهقی", "ha" to "Sunan al-Bayhaqi", "sw" to "Sunan al-Bayhaqi", "so" to "Sunan al-Bayhaqi", "fr" to "Sunan al-Bayhaqi", "de" to "Sunan al-Bayhaqi", "ru" to "Сунан аль-Байхаки", "zh" to "《白哈奇圣训集》"),
            titles = mapOf("en" to "Clipping Nails & Grooming", "hi" to "नाख़ून तराशना व ज़ीनत", "ur" to "नाخن تراشنا و صفائی", "ar" to "تقليم الأظافر والنظافة", "bn" to "নখ কাটা ও পরিচ্ছন্নতা", "ta" to "நகங்களை வெட்டுதல்", "te" to "గోళ్లు కత్తిరించడం", "ml" to "നഖം മുറിക്കുക", "gu" to "નખ કાપવા અને સફાઈ", "mr" to "नखे कापणे व स्वच्छता", "pa" to "ਨਹੁੰ ਕੱਟਣਾ ਤੇ ਸਫਾਈ", "kn" to "ಉಗುರು ಕತ್ತರಿಸುವುದು", "or" to "ନଖ କାଟିବା", "as" to "নখ কটা আৰু পৰিষ্কাৰ কৰা", "id" to "Memotong Kuku & Merapikan Diri", "ms" to "Memotong Kuku & Kekemasan Diri", "tr" to "Tırnak Kesmek ve Bakım Yapmak", "fa" to "کوتاه کردن ناخن‌ها و پیراستگی", "ha" to "Yanke Fikoci", "sw" to "Kukata Kucha", "so" to "Ciddiyaha oo la Jaro", "fr" to "Se couper les ongles et se soigner", "de" to "Nägel schneiden & Körperpflege", "ru" to "Стрижка ногтей и личная гигиена", "zh" to "修剪指甲与修饰个人仪表"),
            descs = mapOf("en" to "Personal hygiene, clipping nails, and neat Sunnah grooming.", "hi" to "सुन्नत के मुताबिक़ नाख़ून तराशना और ज़ाती सफ़ाई।", "ur" to "سنت کے مطابق ناخن کاٹنا اور ذاتی صفائی۔", "ar" to "قص الأظافر والتطهر والاعتناء بالنظافة الشخصية.", "bn" to "সুন্নাত মোতাবেক নখ কাটা ও ব্যক্তিগত পরিচ্ছন্নতা অর্জন।")
        ),
        JummahSunnahItem(
            id = "clothes",
            sequence = "04",
            icon = "👔",
            hadiths = mapOf("en" to "Sunan Abi Dawood 343", "hi" to "सुनन अबू दाऊद 343", "ur" to "سنن ابوداؤد 343", "ar" to "سنن أبي داود ٣٤٣", "bn" to "সুনান আবু দাউদ ৩৪৩", "ta" to "ஸுனன் அபூ தாவூத் 343", "te" to "సునన్ అబూ దావూద్ 343", "ml" to "സുനൻ അബൂ ദാവൂദ് 343", "gu" to "સુનન અબુ દાઉદ 343", "mr" to "सुनन अबू दाऊद 343", "pa" to "ਸੁਨਨ ਅਬੂ ਦਾਊਦ 343", "kn" to "ಸುನನ್ ಅಬೂ ದಾವೂದ್ 343", "or" to "ସୁନନ ଅବୁ ଦାଉଦ 343", "as" to "সুনান আবু দাউদ ৩৪৩", "id" to "Sunan Abu Dawud 343", "ms" to "Sunan Abu Dawud 343", "tr" to "Sünen-i Ebu Davud 343", "fa" to "سنن ابوداود ۳۴۳", "ha" to "Sunan Abi Dawud 343", "sw" to "Sunan Abi Dawud 343", "so" to "Sunan Abii Daawood 343", "fr" to "Sunan Abi Dawood 343", "de" to "Sunan Abi Dawud 343", "ru" to "Сунан Аби Давуд 343", "zh" to "《艾布·达吾德圣训集》343"),
            titles = mapOf("en" to "Wearing Clean & Best Clothes", "hi" to "साफ़-सुथरे / बेहतरीन कपड़े पहनना", "ur" to "صاف اور بہترین لباس پہننا", "ar" to "لبس أحسن الثياب وأطهرها", "bn" to "উত্তম ও পরিষ্কার পোশাক পরিধান", "ta" to "சிறந்த ஆடைகளை அணிதல்", "te" to "మంచి బట్టలు ధరించడం", "ml" to "മികച്ച വസ്ത്രം ധരിക്കുക", "gu" to "શ્રેષ્ઠ અને સ્વચ્છ કપડાં પહેરવા", "mr" to "स्वच्छ व उत्तम कपडे घालणे", "pa" to "ਸਾਫ ਸੁਥਰੇ ਕੱਪੜੇ ਪਹਿਨਣਾ", "kn" to "ಉತ್ತಮ ಬಟ್ಟೆ ಧರಿಸುವುದು", "or" to "ସଫା ଓ ଉତ୍ତମ ବସ୍ତ୍ର ପିନ୍ଧିବା", "as" to "উত্তম পোছাক পৰিধান কৰা", "id" to "Memakai Pakaian Terbaik & Bersih", "ms" to "Memakai Pakaian Terbaik & Bersih", "tr" to "En Güzel ve Temiz Elbiseleri Giymek", "fa" to "پوشیدن پاکیزه‌ترین و بهترین لباس", "ha" to "Sanya Kaya Masu Kyau da Tsabta", "sw" to "Kuvaa Nguo Safi na Nzuri", "so" to "Xidhashada Dharka Ugu Wanaagsan", "fr" to "Porter ses plus beaux habits propres", "de" to "Die beste und sauberste Kleidung tragen", "ru" to "Надевание лучшей и чистой одежды", "zh" to "穿着整洁优美的衣裳"),
            descs = mapOf("en" to "Dressing in one’s finest clean and dignified attire.", "hi" to "जुमे के मुक़द्दस दिन के लिए सबसे अच्छे और साफ़ कपड़े पहनना।", "ur" to "جمعہ کے مقدس دن کے لیے بہترین اور پاک لباس زیب تن کرنا۔", "ar" to "التجمل بأحسن الثياب وأطهرها ليوم الجمعة.", "bn" to "জুমার বরকতময় দিনের জন্য সবচেয়ে উত্তম ও পরিচ্ছন্ন পোশাক পরা।")
        ),
        JummahSunnahItem(
            id = "attar",
            sequence = "05",
            icon = "🌸",
            hadiths = mapOf("en" to "Sahih al-Bukhari 880", "hi" to "सहीह अल-बुख़ारी 880", "ur" to "صحیح بخاری 880", "ar" to "صحيح البخاري ٨٨٠", "bn" to "সহীহ বুখারী ৮৮০", "ta" to "ஸஹீஹ் புகாரி 880", "te" to "సహీహ్ అల్-బుఖారీ 880", "ml" to "സ്വഹീഹ് അൽ-ബുഖാരി 880", "gu" to "સહીહ અલ-બુખારી 880", "mr" to "सहीह अल-बुखारी 880", "pa" to "ਸਹੀਹ ਅਲ-ਬੁਖ਼ਾਰੀ 880", "kn" to "ಸಹೀಹ್ ಅಲ್-ಬುಖಾರಿ 880", "or" to "ସହିହ ଅଲ-ବୁଖାରୀ 880", "as" to "ছহিহ বুখাৰী ৮৮০", "id" to "Shahih al-Bukhari 880", "ms" to "Shahih al-Bukhari 880", "tr" to "Sahih-i Buhari 880", "fa" to "صحیح بخاری ۸۸۰", "ha" to "Sahih al-Bukhari 880", "sw" to "Sahih al-Bukhari 880", "so" to "Saxiiq al-Bukhaari 880", "fr" to "Sahih al-Bukhari 880", "de" to "Sahih al-Bukhari 880", "ru" to "Сахих аль-Бухари 880", "zh" to "《布哈里圣训实录》880"),
            titles = mapOf("en" to "Applying Perfume (Attar) & Oil", "hi" to "इत्र / ख़ुशबू व तेल लगाना", "ur" to "عطر / خوشبو لگانا", "ar" to "التطيب بالمسك والعطر", "bn" to "আতর ও সুগন্ধি ব্যবহার করা", "ta" to "அத்தர் / நறுமணம் பூசுதல்", "te" to "అత్తరు పూసుకోవడం", "ml" to "സുഗന്ധം ഉപയോഗിക്കുക", "gu" to "અત્તર / ખુશબૂ લગાવવી", "mr" to "અત્તર / सुगंध लावणे", "pa" to "ਇਤਰ / ਖੁਸ਼ਬੂ ਲਗਾਉਣਾ", "kn" to "ಅತ್ತರ್ / ಸುಗಂಧ ಲೇಪಿಸುವುದು", "or" to "ଅତର / ସୁଗନ୍ଧ ଲଗାଇବା", "as" to "সুগন্ধি ব্যৱহাৰ কৰা", "id" to "Memakai Minyak Wangi (Parfum)", "ms" to "Memakai Wangian (Minyak Wangi)", "tr" to "Güzel Koku ve Yağ Sürünmek", "fa" to "استعمال عطر و بوی خوش", "ha" to "Shiryawa da Turare", "sw" to "Kupaka Manukato", "so" to "Catariska iyo Udgoonka", "fr" to "Se parfumer (Attar) et se huiler", "de" to "Parfüm (Attar) auftragen & Ölen", "ru" to "Использование благовоний (миск) и масел", "zh" to "使用香水和香油"),
            descs = mapOf("en" to "Applying fine non-alcoholic fragrance for the gathering of believers.", "hi" to "जुमे की नमाज़ के लिए बेहतरीन पाकीज़ा ख़ुशबू लगाना।", "ur" to "نمازیوں کے اجتماع کے لیے عمدہ اور پاکیزہ خوشبو لگانا۔", "ar" to "استعمال الطيب ودهن الشعر لحضور الجمعة.", "bn" to "জুমার জামাতের উদ্দেশ্যে উত্তম সুগন্ধি ব্যবহার করা।")
        ),
        JummahSunnahItem(
            id = "early_mosque",
            sequence = "06",
            icon = "🕌",
            hadiths = mapOf("en" to "Sahih al-Bukhari 881", "hi" to "सहीह अल-बुख़ारी 881", "ur" to "صحیح بخاری 881", "ar" to "صحيح البخاري ٨٨١", "bn" to "সহীহ বুখারী ৮৮১", "ta" to "ஸஹீஹ் புகாரி 881", "te" to "సహీహ్ అల్-బుఖారీ 881", "ml" to "സ്വഹീഹ് അൽ-ബുഖാരി 881", "gu" to "સહીહ અલ-બુખારી 881", "mr" to "सहीह अल-बुखारी 881", "pa" to "ਸਹੀਹ ਅਲ-ਬੁਖ਼ਾਰੀ 881", "kn" to "ಸಹೀಹ್ ಅಲ್-ಬುಖಾರಿ 881", "or" to "ସହିହ ଅଲ-ବୁଖାରୀ 881", "as" to "ছহিহ বুখাৰী ৮৮১", "id" to "Shahih al-Bukhari 881", "ms" to "Shahih al-Bukhari 881", "tr" to "Sahih-i Buhari 881", "fa" to "صحیح بخاری ۸۸۱", "ha" to "Sahih al-Bukhari 881", "sw" to "Sahih al-Bukhari 881", "so" to "Saxiiq al-Bukhaari 881", "fr" to "Sahih al-Bukhari 881", "de" to "Sahih al-Bukhari 881", "ru" to "Сахих аль-Бухари 881", "zh" to "《布哈里圣训实录》881"),
            titles = mapOf("en" to "Going Early to the Mosque", "hi" to "मस्जिद जल्दी पहुंचना", "ur" to "مسجد جلدی جانا", "ar" to "التبكير إلى المسجد", "bn" to "তাড়াতাড়ি মসজিদে যাওয়া", "ta" to "பள்ளிவாசலுக்கு முன்கூட்டியே செல்லுதல்", "te" to "మసీదుకు త్వరగా వెళ్లడం", "ml" to "നേരത്തെ പള്ളിയിലെത്തുക", "gu" to "મસ્જિદ વહેલા પહોંચવું", "mr" to "मशीदीत लवकर जाणे", "pa" to "ਮਸਜਿਦ ਜਲਦੀ ਜਾਣਾ", "kn" to "ಬೇಗನೆ ಮಸೀದಿಗೆ ಹೋಗುವುದು", "or" to "ମସଜିଦକୁ ଶୀଘ୍ର ଯିବା", "as" to "মছজিদলৈ সোনকালে যোৱা", "id" to "Berangkat Awal ke Masjid", "ms" to "Pergi Awal ke Masjid", "tr" to "Camiye Erken Gitmek", "fa" to "زود رفتن به مسجد", "ha" to "Tafiya Masallaci da Wuri", "sw" to "Kwenda Mapema Msikitini", "so" to "Hore u Tagidda Masaajidka", "fr" to "Se rendre tôt à la mosquée", "de" to "Frühzeitig zur Moschee gehen", "ru" to "Ранний приход в мечеть на пятничную молитву", "zh" to "提早前往清真寺"),
            descs = mapOf("en" to "Arriving early earns a massive reward equivalent to sacrificing a camel.", "hi" to "अव्वल वक़्त मस्जिद जाने पर ऊंट की क़ुर्बानी जैसा अज़ीम सवाब।", "ur" to "پہلے وقت مسجد جانے پر اونٹ کی قربانی جیسا ثواب۔", "ar" to "فضل عظيم كالذي يقرّب بدنة في سبيل الله.", "bn" to "প্রথম ওয়াক্তে মসজিদে যাওয়ায় উট কুরবানীর সমতুল্য অসীম সওয়াব।")
        ),
        JummahSunnahItem(
            id = "walk_mosque",
            sequence = "07",
            icon = "🚶",
            hadiths = mapOf("en" to "Jami at-Tirmidhi 496", "hi" to "जामेअ तिर्मिज़ी 496", "ur" to "جامع ترمذی 496", "ar" to "جامع الترمذي ٤٩٦", "bn" to "জামে তিরমিযী ৪৯৬", "ta" to "ஜாமிஉத் திர்மிதி 496", "te" to "జామి అత్-తిర్మిది 496", "ml" to "ജാമിഅ് തിർമിദി 496", "gu" to "જામેઅ તિર્મિઝી 496", "mr" to "जामेअ तिर्मिझी 496", "pa" to "ਜਾਮੇਅ ਤਿਰਮਿਜ਼ੀ 496", "kn" to "ಜಾಮಿಅ್ ಅತ್-ತಿರ್ಮಿದಿ 496", "or" to "ଜାମେ ଅତ-ତିର୍ମିଜି 496", "as" to "জামে তিৰমিজি ৪৯৬", "id" to "Jami at-Tirmidzi 496", "ms" to "Jami at-Tirmidhi 496", "tr" to "Camiu't-Tirmizi 496", "fa" to "جامع ترمذی ۴۹۶", "ha" to "Jami at-Tirmidhi 496", "sw" to "Jami at-Tirmidhi 496", "so" to "Jaamic at-Tirmidhi 496", "fr" to "Jami at-Tirmidhi 496", "de" to "Dschami at-Tirmidhi 496", "ru" to "Джами ат-Тирмизи 496", "zh" to "《提尔密济圣训集》496"),
            titles = mapOf("en" to "Walking on Foot to Mosque", "hi" to "पैदल मस्जिद जाना (अगर मुमकिन हो)", "ur" to "پیدل مسجد جانا", "ar" to "المشي إلى المسجد على الأقدام", "bn" to "হেঁটে মসজিদে যাওয়া", "ta" to "மசூதிக்கு நடந்து செல்லுதல்", "te" to "నడిచి మసీదుకు వెళ్లడం", "ml" to "നടന്നു പള്ളിയിലേക്ക് പോവുക", "gu" to "પગપાળા મસ્જિદ જવું", "mr" to "मशिदीत पायी चालत जाणे", "pa" to "ਪੈਦਲ ਮਸਜਿਦ ਜਾਣਾ", "kn" to "ನಡೆದುಕೊಂಡು ಮಸೀದಿಗೆ ಹೋಗುವುದು", "or" to "ଚାଲି ଚାଲି ମସଜିଦ ଯିବା", "as" to "খোজকাঢ়ি মছজিদলৈ যোৱা", "id" to "Berjalan Kaki Menuju Masjid", "ms" to "Berjalan Kaki ke Masjid", "tr" to "Camiye Yürüyerek Gitmek", "fa" to "پیاده رفتن به مسجد", "ha" to "Tafiya a Kafa Zuwa Masallaci", "sw" to "Kutembea kwa Miguu Kwenda Msikitini", "so" to "U Socodka Masaajidka Cagaha", "fr" to "Aller à la mosquée à pied", "de" to "Zu Fuß zur Moschee gehen", "ru" to "Пеший путь в мечеть", "zh" to "步行前往清真寺"),
            descs = mapOf("en" to "For every step, the reward of one year of fasting and standing in prayer.", "hi" to "हर क़दम पर एक साल के रोज़े और नमाज़-ए-तहज्जुद का सवाब।", "ur" to "ہر قدم پر ایک سال کے روزوں اور نمازوں کا ثواب۔", "ar" to "بكل خطوة يخطوها أجر صيام سنة وقيامها.", "bn" to "প্রতি পদক্ষেপে এক বছরের রোজা ও তাহাজ্জুদের সওয়াব।")
        ),
        JummahSunnahItem(
            id = "khutbah",
            sequence = "08",
            icon = "🤫",
            hadiths = mapOf("en" to "Sahih al-Bukhari 934", "hi" to "सहीह अल-बुख़ारी 934", "ur" to "صحیح بخاری 934", "ar" to "صحيح البخاري ٩٣٤", "bn" to "সহীহ বুখারী ৯৩৪", "ta" to "ஸஹீஹ் புகாரி 934", "te" to "సహీహ్ అల్-బుఖారీ 934", "ml" to "സ്വഹീഹ് അൽ-ബുഖാരി 934", "gu" to "સહીહ અલ-બુખારી 934", "mr" to "सहीह अल-बुखारी 934", "pa" to "ਸਹੀਹ ਅਲ-ਬੁਖ਼ਾਰੀ 934", "kn" to "ಸಹೀಹ್ ಅಲ್-ಬುಖಾರಿ 934", "or" to "ସହିହ ଅଲ-ବୁଖାରୀ 934", "as" to "ছহিহ বুখাৰী ৯৩৪", "id" to "Shahih al-Bukhari 934", "ms" to "Shahih al-Bukhari 934", "tr" to "Sahih-i Buhari 934", "fa" to "صحیح بخاری ۹۳۴", "ha" to "Sahih al-Bukhari 934", "sw" to "Sahih al-Bukhari 934", "so" to "Saxiiq al-Bukhaari 934", "fr" to "Sahih al-Bukhari 934", "de" to "Sahih al-Bukhari 934", "ru" to "Сахих аль-Бухари 934", "zh" to "《布哈里圣训实录》934"),
            titles = mapOf("en" to "Listening Attentively to Khutbah", "hi" to "ख़ामोशी से ख़ुत्बा सुनना", "ur" to "خاموشی سے خطبہ سننا", "ar" to "الإنصات التام لخطبة الجمعة", "bn" to "মনোযোগ সহকারে খুতবা শোনা", "ta" to "குத்பாவை மௌனமாக கேட்டல்", "te" to "ఖుత్బా శ్రద్ధగా వినడం", "ml" to "ഖുതുബ ശ്രദ്ധയോടെ കേൾക്കുക", "gu" to "શાંતિથી ખુત્બો સાંભળવો", "mr" to "शांतपणे खुतबा ऐकणे", "pa" to "ਧਿਆਨ ਨਾਲ ਖੁਤਬਾ ਸੁਣਨਾ", "kn" to "ಖುತ್ಬಾವನ್ನು ಗಮನವಿಟ್ಟು ಕೇಳುವುದು", "or" to "ଧ୍ୟାନ ଦେଇ ଖୁତବା ଶୁଣିବା", "as" to "মনোযোগেৰে খুতবা শুনা", "id" to "Mendengarkan Khutbah dengan Khusyuk", "ms" to "Mendengar Khutbah dengan Khusyuk", "tr" to "Hutbeyi Sessizce ve Dikkatle Dinlemek", "fa" to "گوش فرا دادن به خطبه با سکوت کامل", "ha" to "Sauraron Huduwa cikin Natsuwa", "sw" to "Kusikiliza Khutba kwa Makini", "so" to "Dhegeysiga Khudbada oo la Aamuso", "fr" to "Écouter attentivement le sermon (Khoutba)", "de" to "Aufmerksam & schweigend der Chutba zuhören", "ru" to "Внимательное и безмолвное слушание проповеди (хутбы)", "zh" to "静听主麻演讲（呼图白）"),
            descs = mapOf("en" to "Remaining completely silent and focused while the Imam delivers the sermon.", "hi" to "ख़ुत्बे के दौरान मुकम्मल ख़ामोशी और तवज्जोह से बयान सुनना।", "ur" to "خطبے کے دوران مکمل خاموشی اور توجہ سے بیان سننا۔", "ar" to "السكوت التام والإنصات أثناء إلقاء الإمام للخطبة.", "bn" to "ইমামের খুতবা প্রদানের সময় সম্পূর্ণ নীরব থেকে মনোযোগ দিয়ে শোনা।")
        ),
        JummahSunnahItem(
            id = "surah_kahf",
            sequence = "09",
            icon = "📖",
            hadiths = mapOf("en" to "Sahih al-Jami 6470 | Hakim 3392", "hi" to "सहीह अल-जामेअ 6470 | हाकिम 3392", "ur" to "صحیح الجامع 6470 | حاکم 3392", "ar" to "صحيح الجامع ٦٤٧٠ | الحاكم ٣٣٩٢", "bn" to "সহীহুল জামে ৬৪৭০ | হাকিম ৩৩৯২", "ta" to "ஸஹீஹுல் ஜாமி 6470 | ஹாகிம் 3392", "te" to "సహీహ్ అల్-జామీ 6470 | హాకిమ్ 3392", "ml" to "സ്വഹീഹുൽ ജാമി 6470 | ഹാക്കിം 3392", "gu" to "સહીહ અલ-જામેઅ 6470 | હાકિમ 3392", "mr" to "सहीह अल-जामे 6470 | हाकिम 3392", "pa" to "ਸਹੀਹ ਅਲ-ਜਾਮੇ 6470 | ਹਾਕਿਮ 3392", "kn" to "ಸಹೀಹ್ ಅಲ್-ಜಾಮಿ 6470 | ಹಾಕಿಮ್ 3392", "or" to "ସହିହ ଅଲ-ଜାମି 6470 | ହାକିମ 3392", "as" to "ছহিহুল জামে ৬৪৭০ | হাকিম ৩৩৯২", "id" to "Shahih al-Jami 6470 | Hakim 3392", "ms" to "Shahih al-Jami 6470 | Hakim 3392", "tr" to "Sahihü'l-Cami 6470 | Hakim 3392", "fa" to "صحیح الجامع ۶۴۷۰ | حاکم ۳۳۹۲", "ha" to "Sahih al-Jami 6470 | Hakim 3392", "sw" to "Sahih al-Jami 6470 | Hakim 3392", "so" to "Saxiiq al-Jaamic 6470 | Xaakim 3392", "fr" to "Sahih al-Jami 6470 | Hakim 3392", "de" to "Sahih al-Dschami 6470 | Hakim 3392", "ru" to "Сахих аль-Джами 6470 | Хаким 3392", "zh" to "《圣训全集精选》6470 | 哈基姆3392"),
            titles = mapOf("en" to "Reciting Surah Al-Kahf", "hi" to "सूरह अल-कहफ़ की तिलावत", "ur" to "سورۃ الکہف کی تلاوت", "ar" to "قراءة سورة الكهف", "bn" to "সূরা আল-কাহাফ তিলাওয়াত", "ta" to "ஸூரத்துல் கஹ்ஃப் ஓதுதல்", "te" to "సూరా అల్-కహఫ్ పఠించడం", "ml" to "സൂറത്തുൽ കഹ്ഫ് പാരായണം", "gu" to "સૂરહ અલ-કહફની તિલાવત", "mr" to "सूरह अल-कहफ पठण करणे", "pa" to "ਸੂਰਹ ਅਲ-ਕਹਫ ਦੀ ਤਿਲਾਵਤ", "kn" to "ಸೂರಾ ಅಲ್-ಕಹ್ಫ್ ಪಠಣ", "or" to "ସୁରା ଅଲ-କହଫ ପଢିବା", "as" to "সূৰা আল-কাহাফ তিলাৱত", "id" to "Membaca Surah Al-Kahfi", "ms" to "Membaca Surah Al-Kahfi", "tr" to "Kehf Suresini Okumak", "fa" to "تلاوت سوره مبارکه کهف", "ha" to "Karatun Suratul Kahfi", "sw" to "Kusoma Surah Al-Kahf", "so" to "Akhrinta Suuradda Al-Kahf", "fr" to "Réciter la sourate Al-Kahf", "de" to "Die Sure Al-Kahf rezitieren", "ru" to "Чтение суры «Аль-Кахф» (Пещера)", "zh" to "诵读《山洞章》（古兰经第18章）"),
            descs = mapOf("en" to "A divine light (Noor) illuminates the believer until the next Friday.", "hi" to "दोनों जुमों के दरमियान नूर चमकता रहेगा और गुनाह माफ़ होंगे।", "ur" to "دونوں جمعوں کے درمیان نور روشن رہے گا اور گناہ معاف ہوں گے۔", "ar" to "أضاء له من النور ما بين الجمعتين وعُصم من الدجال.", "bn" to "দুই জুমার মধ্যবর্তী সময় আল্লাহর পক্ষ থেকে নূরে উদ্ভাসিত থাকবে।")
        ),
        JummahSunnahItem(
            id = "durood",
            sequence = "10",
            icon = "💚",
            hadiths = mapOf("en" to "Sunan Abi Dawood 1047", "hi" to "सुनन अबू दाऊद 1047", "ur" to "سنن ابوداؤد 1047", "ar" to "سنن أبي داود ١٠٤٧", "bn" to "সুনান আবু দাউদ ১০৪৭", "ta" to "ஸுனன் அபூ தாவூத் 1047", "te" to "సునన్ అబూ దావూద్ 1047", "ml" to "സുനൻ അബൂ ദാവൂദ് 1047", "gu" to "સુનન અબુ દાઉદ 1047", "mr" to "सुनन अबू दाऊद 1047", "pa" to "ਸੁਨਨ ਅਬੂ ਦਾਊਦ 1047", "kn" to "ಸುನನ್ ಅಬೂ ದಾವೂದ್ 1047", "or" to "ସୁନନ ଅବୁ ଦାଉଦ 1047", "as" to "সুনান আবু দাউদ ১০৪৭", "id" to "Sunan Abu Dawud 1047", "ms" to "Sunan Abu Dawud 1047", "tr" to "Sünen-i Ebu Davud 1047", "fa" to "سنن ابوداود ۱۰۴۷", "ha" to "Sunan Abi Dawud 1047", "sw" to "Sunan Abi Dawud 1047", "so" to "Sunan Abii Daawood 1047", "fr" to "Sunan Abi Dawood 1047", "de" to "Sunan Abi Dawud 1047", "ru" to "Сунан Аби Давуд 1047", "zh" to "《艾布·达吾德圣训集》1047"),
            titles = mapOf("en" to "Abundant Salawat upon Prophet ﷺ", "hi" to "हुज़ूर ﷺ पर कसरत से दुरूद भेजना", "ur" to "حضور ﷺ پر کثرت سے درود بھیجنا", "ar" to "الإكثار من الصلاة والسلام على النبي ﷺ", "bn" to "নবীজী ﷺ-এর উপর অধিক দরূদ পাঠ", "ta" to "நபி ﷺ மீது அதிக ஸலவாத் கூறுதல்", "te" to "ప్రవక్త ﷺ పై దరూద్ పంపడం", "ml" to "നബി ﷺ മേൽ സ്വലാത്ത് അധികരിപ്പിക്കുക", "gu" to "હુઝૂર ﷺ પર વધુમાં વધુ દુરૂદ મોકલવા", "mr" to "प्रेषित ﷺ यांच्यावर विपुल दुरुद पाठवणे", "pa" to "ਹਜ਼ੂਰ ﷺ ਉੱਤੇ ਵੱਧ ਤੋਂ ਵੱਧ ਦਰੂਦ ਭੇਜਣਾ", "kn" to "ಪ್ರವಾದಿ ﷺ ಅವರ ಮೇಲೆ ಹೆಚ್ಚಿನ ಸಲವಾತ್ ಸಲ್ಲಿಸುವುದು", "or" to "ନବୀ ﷺ ଙ୍କ ଉପରେ ଦୁରୁଦ ପଠାଇବା", "as" to "নবী ﷺ-ৰ ওপৰত অধিক দৰূদ পঢ়া", "id" to "Memperbanyak Shalawat atas Nabi ﷺ", "ms" to "Memperbanyakkan Selawat ke atas Nabi ﷺ", "tr" to "Peygamber Efendimize ﷺ Çokça Salavat Getirmek", "fa" to "فرستادن صلوات فراوان بر پیامبر اکرم ﷺ", "ha" to "Yawaita Salati ga Manzon Allah ﷺ", "sw" to "Kukithirisha Kumswalia Mtume ﷺ", "so" to "Saliga Nabiga ﷺ oo la Badiyo", "fr" to "Multiplier les prières sur le Prophète ﷺ", "de" to "Reichlich Segenswünsche (Salawat) auf den Propheten ﷺ senden", "ru" to "Многократное благословение Пророка ﷺ (Салават)", "zh" to "多多为先知穆罕默德 ﷺ 祈求祝福（赞圣词）"),
            descs = mapOf("en" to "Sending blessings upon Prophet Muhammad ﷺ on Friday is presented directly to him.", "hi" to "जुमे के दिन कसरत से दुरूद शरीफ़ का विर्द करना सरकार ﷺ तक पहुंचाया जाता है।", "ur" to "جمعہ کے دن کثرت سے درود شریف پڑھنا حضور ﷺ کی بارگاہ میں پیش ہوتا ہے۔", "ar" to "«أكثروا عليّ من الصلاة فيه فإن صلاتكم معروضة عليّ».", "bn" to "জুমার দিনে নবীজী ﷺ-এর ওপর পঠিত দরূদ সরাসরি তাঁর সমীপে পেশ করা হয়।")
        ),
        JummahSunnahItem(
            id = "dua_hour",
            sequence = "11",
            icon = "🤲",
            hadiths = mapOf("en" to "Sahih al-Bukhari 935", "hi" to "सहीह अल-बुख़ारी 935", "ur" to "صحیح بخاری 935", "ar" to "صحيح البخاري ٩٣٥", "bn" to "সহীহ বুখারী ৯৩৫", "ta" to "ஸஹீஹ் புகாரி 935", "te" to "సహీహ్ అల్-బుఖారీ 935", "ml" to "സ്വഹീഹ് അൽ-ബുഖാരി 935", "gu" to "સહીહ અલ-બુખારી 935", "mr" to "सहीह अल-बुखारी 935", "pa" to "ਸਹੀਹ ਅਲ-ਬੁਖ਼ਾਰੀ 935", "kn" to "ಸಹೀಹ್ ಅಲ್-ಬುಖಾರಿ 935", "or" to "ସହିହ ଅଲ-ବୁଖାରୀ 935", "as" to "ছহিহ বুখাৰী ৯৩৫", "id" to "Shahih al-Bukhari 935", "ms" to "Shahih al-Bukhari 935", "tr" to "Sahih-i Buhari 935", "fa" to "صحیح بخاری ۹۳۵", "ha" to "Sahih al-Bukhari 935", "sw" to "Sahih al-Bukhari 935", "so" to "Saxiiq al-Bukhaari 935", "fr" to "Sahih al-Bukhari 935", "de" to "Sahih al-Bukhari 935", "ru" to "Сахих аль-Бухари 935", "zh" to "《布哈里圣训实录》935"),
            titles = mapOf("en" to "Supplicating in the Hour of Acceptance", "hi" to "दुआ-ए-इजाबत में दुआ (असर बाद)", "ur" to "ساعتِ اجابت میں دعا (عصر کے بعد)", "ar" to "تحري ساعة الإجابة (آخر ساعة بعد العصر)", "bn" to "দোয়া কবুলের বিশেষ মুহূর্তে প্রার্থনা", "ta" to "துஆ ஏற்றுக்கொள்ளப்படும் நேரத்தில் வேண்டுதல்", "te" to "దువా అంగీకరించబడే సమయంలో ప్రార్థన", "ml" to "ദുആ സ്വീകരിക്കപ്പെടുന്ന സമയത്ത് പ്രാർത്ഥിക്കുക", "gu" to "દુઆ કબૂલ થવાના ખાસ વક્તમાં દુઆ", "mr" to "दुआ स्वीकारल्या जाणाऱ्या वेळेत प्रार्थना", "pa" to "ਦੁਆ ਕਬੂਲ ਹੋਣ ਵਾਲੇ ਸਮੇਂ ਦੁਆ ਮੰਗਣਾ", "kn" to "ದುವಾ ಸ್ವೀಕರಿಸಲ್ಪಡುವ ಸಮಯದಲ್ಲಿ ಪ್ರಾರ್ಥನೆ", "or" to "ଦୁଆ କବୁଲ ହେବା ସମୟରେ ପ୍ରାର୍ଥନା", "as" to "দোৱা কবুলৰ বিশেষ মুহূৰ্তত প্ৰাৰ্থনা", "id" to "Berdoa di Waktu Mustajab (Bada Ashar)", "ms" to "Berdoa pada Waktu Mustajab (Selepas Asar)", "tr" to "İcabet Saatinde (İkindi Sonrası) Dua Etmek", "fa" to "دعا در ساعت اجابت (پس از عصر تا مغرب)", "ha" to "Yin Addua a Lokacin Karba", "sw" to "Kuomba Dua Wakati wa Kukubaliwa", "so" to "Ducaysiga Waqtiga Ajiibista", "fr" to "Invoquer pendant l'Heure d'Exaucement", "de" to "Bittgebete in der Stunde der Erhörung sprechen", "ru" to "Мольба (дуа) в час принятия мольбы (между Асром и Магрибом)", "zh" to "在提求必应的尊贵时刻（晡礼至昏礼之间）祈祷"),
            descs = mapOf("en" to "A blessed hour on Friday (between Asr and Maghrib) where no sincere Dua is rejected.", "hi" to "असर से मग़रिब के बीच एक घड़ी ऐसी है जिसमें मांगी गई कोई भी जायज़ दुआ रद्द नहीं होती।", "ur" to "عصر سے مغرب کے درمیان ایک ایسی گھڑی ہے جس میں بندہ जो مانگے قبول ہوتا ہے۔", "ar" to "ساعة لا يوافقها عبد مسلم يسأل الله تعالى شيئاً إلا أعطاه إياه.", "bn" to "আসর থেকে মাগরিবের মধ্যবর্তী এমন এক বরকতময় মুহূর্ত যখন কোনো নেক দোয়া প্রত্যাখ্যাত হয় না।")
        ),
        JummahSunnahItem(
            id = "charity",
            sequence = "12",
            icon = "🪙",
            hadiths = mapOf("en" to "Musannaf 'Abd al-Razzaq 5558", "hi" to "मुसन्नफ़ अब्दुर रज़्ज़ाक़ 5558", "ur" to "مصنف عبدالرزاق 5558", "ar" to "مصنف عبد الرزاق ٥٥٥٨", "bn" to "মুসান্নাফ আব্দুর রাজ্জাক ৫৫৫৮", "ta" to "முஸன்னஃப் அப்துர் ரஸ்ஸாக் 5558", "te" to "ముసన్నఫ్ అబ్దుర్ రజ్జాఖ్ 5558", "ml" to "മുസന്നഫ് അബ്ദുർ റസ്സാഖ് 5558", "gu" to "મુસન્નફ અબ્દુર રઝાક 5558", "mr" to "मुसन्नफ अब्दुर रझ्झाक 5558", "pa" to "ਮੁਸੰਨਫ਼ ਅਬਦੁਰ ਰਜ਼ਾਕ 5558", "kn" to "ಮುಸನ್ನಫ್ ಅಬ್ದುರ್ ರಝಾಕ್ 5558", "or" to "ମୁସନ୍ନଫ ଅବ୍ଦୁର ରଜ୍ଜାକ 5558", "as" to "মুছান্নাফ আব্দুর ৰাজ্জাক ৫৫৫৮", "id" to "Mushannaf Abdur Razzaq 5558", "ms" to "Mushannaf Abdur Razzaq 5558", "tr" to "Musannef Abdürrezzak 5558", "fa" to "مصنف عبدالرزاق ۵۵۵۸", "ha" to "Musannaf Abdur Razzaq 5558", "sw" to "Musannaf Abdur Razzaq 5558", "so" to "Musannaf Cabdir-Razzaaq 5558", "fr" to "Musannaf 'Abd al-Razzaq 5558", "de" to "Musannaf Abdur Razzaq 5558", "ru" to "Мусаннаф Абд ар-Раззак 5558", "zh" to "《阿卜杜·拉扎格圣训集》5558"),
            titles = mapOf("en" to "Giving Charity (Sadaqah)", "hi" to "सदक़ा व ख़ैरात देना", "ur" to "صدقہ و خیرات کرنا", "ar" to "الصدقة يوم الجمعة", "bn" to "দান-সদকা করা", "ta" to "தான தர்மம் (ஸதகா) செய்தல்", "te" to "దానం (సదకా) చేయడం", "ml" to "ദാനം (സ്വദഖ) ചെയ്യുക", "gu" to "સદકા અને દાન કરવું", "mr" to "दान (सदका) करणे", "pa" to "ਦਾਨ (ਸਦਕਾ) ਕਰਨਾ", "kn" to "ದಾನ (ಸದಕಾ) ಮಾಡುವುದು", "or" to "ଦାନ (ସଦକା) କରିବା", "as" to "দান-ছদকা কৰা", "id" to "Bersedekah di Hari Jumat", "ms" to "Bersedekah pada Hari Jumaat", "tr" to "Cuma Günü Sadaka Vermek", "fa" to "صدقه دادن در روز جمعه", "ha" to "Yin Sadaka a Ranar Jumaa", "sw" to "Kutoa Sadaka Siku ya Ijumaa", "so" to "Sadaqo Bixinta Jimcaha", "fr" to "Faire l'aumône (Sadaqah) le vendredi", "de" to "Almosen (Sadaqah) am Freitag geben", "ru" to "Раздача милостыни (Садака) в пятницу", "zh" to "主麻日施舍（撒德盖）"),
            descs = mapOf("en" to "Giving charity on Friday is specially blessed, multiplying rewards abundantly.", "hi" to "जुमे के दिन सदक़ा व ख़ैरात करने का सवाब बाक़ी दिनों से कई गुना बढ़कर मिलता है।", "ur" to "جمعہ کے دن صدقہ و خیرات کرنا دیگر تمام دنوں کے مقابلے میں زیادہ فضیلت اور اجر رکھتا ہے۔", "ar" to "الصدقة في يوم الجمعة لها فضل عظيم وأجر مضاعف كما بيّن أهل العلم.", "bn" to "জুমার দিনে দান-সদকা করার মাধ্যমে অশেষ নেকি ও বরকত অর্জিত হয়।")
        ),
    )

    val hadithDetails: Map<String, JummahHadithDetail> = mapOf(
        "miswak" to JummahHadithDetail(
            ref = "Sahih Muslim 846 | Sunan an-Nasa'i 1378",
            grade = "Sahih (Authentic)",
            links = listOf(HadithSourceLink("Sahih Muslim 846 (Sunnah.com)", "https://sunnah.com/muslim:846"), HadithSourceLink("Sunan an-Nasa'i 1378 (Sunnah.com)", "https://sunnah.com/nasai:1378")),
            ar = "عَنْ أَبِي سَعِيدٍ الْخُدْرِيِّ رَضِيَ اللَّهُ عَنْهُ، أَنَّ رَسُولَ اللَّهِ ﷺ قَالَ: «غُسْلُ يَوْمِ الْجُمُعَةِ عَلَى كُلِّ مُحْتَلِمٍ، وَسِوَاكٌ، وَيَمَسُّ مِنَ الطِّيبِ مَا قَدَرَ عَلَيْهِ»",
            en = "Narrated by Abu Sa'id Al-Khudri (RA): The Messenger of Allah ﷺ said: \"Taking a bath on Friday is essential for every adult, as is using the tooth-stick (Miswak) and applying perfume if available.\"",
            hi = "हज़रत अबू सईद ख़ुदरी (रज़ि.) से रिवायत है कि रसूलुल्लाह ﷺ ने फ़रमाया: \"जुमे के दिन ग़ुस्ल करना हर बालिग़ पर सुन्नत-ए-मौक़दा है, और मिस्वाक करना और जितनी मुमकिन हो ख़ुशबू लगाना।\"",
            ur = "حضرت ابو سعید خدری رضی اللہ عنہ سے روایت ہے کہ رسول اللہ ﷺ نے فرمایا: \"جمعہ کے دن غسل کرنا ہر بالغ پر، اور مسواک کرنا اور جتنی خوشبو میسر ہو لگانا مسنون ہے۔\""
        ),
        "ghusl" to JummahHadithDetail(
            ref = "Sahih al-Bukhari 877 | Sahih Muslim 844",
            grade = "Sahih (Authentic)",
            links = listOf(HadithSourceLink("Sahih al-Bukhari 877 (Sunnah.com)", "https://sunnah.com/bukhari:877"), HadithSourceLink("Sahih Muslim 844 (Sunnah.com)", "https://sunnah.com/muslim:844a")),
            ar = "عَنِ ابْنِ عُمَرَ رَضِيَ اللَّهُ عَنْهُمَا، أَنَّ رَسُولَ اللَّهِ ﷺ قَالَ: «مَنْ جَاءَ مِنْكُمُ الْجُمُعَةَ فَلْيَغْتَسِلْ»",
            en = "Narrated by Ibn Umar (RA): The Messenger of Allah ﷺ said: \"Anyone of you attending the Friday (Jumu'ah) prayer should take a bath (Ghusl).\"",
            hi = "हज़रत अब्दुल्लाह बिन उमर (रज़ि.) से रिवायत है कि रसूलुल्लाह ﷺ ने फ़रमाया: \"तुम में से जो कोई जुमे की नमाज़ के लिए आए, उसे चाहिए कि वह ग़ुस्ल (स्नान) करे।\"",
            ur = "حضرت عبداللہ بن عمر رضی اللہ عنہما سے روایت ہے کہ رسول اللہ ﷺ نے فرمایا: \"تم میں سے جو شخص جمعہ کی نماز کے لیے آئے، اسے چاہیے کہ وہ غسل کرے۔\""
        ),
        "nails" to JummahHadithDetail(
            ref = "Sunan al-Bayhaqi (Shu'ab al-Iman 2769) | Sahih al-Bukhari 5891",
            grade = "Sahih (Fitrah Sunnah)",
            links = listOf(HadithSourceLink("Sahih al-Bukhari 5891 (Sunnah.com)", "https://sunnah.com/bukhari:5891"), HadithSourceLink("Sahih Muslim 257 (Sunnah.com)", "https://sunnah.com/muslim:257b")),
            ar = "عَنْ أَبِي هُرَيْرَةَ رَضِيَ اللَّهُ عَنْهُ، أَنَّ رَسُولَ اللَّهِ ﷺ قَالَ: «الْفِطْرَةُ خَمْسٌ: الْخِتَانُ، وَالِاسْتِحْدَادُ، وَقَصُّ الشَّارِبِ، وَتَقْلِيمُ الْأَظْفَارِ، وَنَتْفُ الْآبَاطِ»",
            en = "Narrated by Abu Hurairah (RA): The Messenger of Allah ﷺ said: \"Five are the acts of natural disposition (Fitrah): circumcision, shaving the pubes, trimming the moustache, clipping the nails, and plucking the armpit hairs.\"",
            hi = "हज़रत अबू हुरैरा (रज़ि.) से रिवायत है कि रसूलुल्लाह ﷺ ने फ़रमाया: \"पाँच चीज़ें फ़ितरत (सुन्नत) में से हैं: ख़तना करना, ज़ेरे नाफ़ के बाल काटना, मूंछें छोटी करना, नाख़ून तराशना और बग़ल के बाल साफ़ करना।\"",
            ur = "حضرت ابوہریرہ رضی اللہ عنہ سے روایت ہے کہ رسول اللہ ﷺ نے فرمایا: \"پانچ چیزیں فطرت (سنت) میں سے ہیں: ختنہ کرنا، زیرِ ناف کے بال کاٹنا، مونچھیں چھوٹی کرنا، ناخن تراشنا اور بغل کے بال صاف کرنا۔\""
        ),
        "clothes" to JummahHadithDetail(
            ref = "Sunan Abi Dawood 343 | Sunan Ibn Majah 1095",
            grade = "Sahih (Authentic)",
            links = listOf(HadithSourceLink("Sunan Abi Dawood 343 (Sunnah.com)", "https://sunnah.com/abudawud:343"), HadithSourceLink("Sunan Ibn Majah 1095 (Sunnah.com)", "https://sunnah.com/ibnmajah:1095")),
            ar = "عَنْ عَائِشَةَ رَضِيَ اللَّهُ عَنْهَا، أَنَّ رَسُولَ اللَّهِ ﷺ قَالَ: «مَا عَلَى أَحَدِكُمْ إِنْ وَجَدَ سَعَةً أَنْ يَتَّخِذَ ثَوْبَيْنِ لِيَوْمِ الْجُمُعَةِ سِوَى ثَوْبَيْ مِهْنَتِهِ»",
            en = "Narrated by Aisha (RA): The Messenger of Allah ﷺ said: \"There is no harm if anyone of you, if he can afford it, keeps two garments for Friday other than his daily working clothes.\"",
            hi = "हज़रत आयशा (रज़ि.) से रिवायत है कि रसूलुल्लाह ﷺ ने फ़रमाया: \"अगर किसी के पास गुंजाइश हो तो इसमें कोई हर्ज़ नहीं कि वह जुमे के दिन के लिए अपने रोज़मर्रा के कामकाजी कपड़ों के अलावा दो कपड़े अलग रखे।\"",
            ur = "ام المومنین حضرت عائشہ رضی اللہ عنہا سے روایت ہے کہ رسول اللہ ﷺ نے فرمایا: \"اگر کسی کے پاس گنجائش ہو تو اس میں کوئی حرج نہیں کہ وہ روزمرہ کے کام کاج کے کپڑوں کے علاوہ جمعہ کے لیے دو خاص کپڑے رکھے۔\""
        ),
        "attar" to JummahHadithDetail(
            ref = "Sahih al-Bukhari 880",
            grade = "Sahih (Authentic)",
            links = listOf(HadithSourceLink("Sahih al-Bukhari 880 (Sunnah.com)", "https://sunnah.com/bukhari:880")),
            ar = "عَنْ سَلْمَانَ الْفَارِسِيِّ رَضِيَ اللَّهُ عَنْهُ قَالَ: قَالَ النَّبِيُّ ﷺ: «لاَ يَغْتَسِلُ رَجُلٌ يَوْمَ الجُمُعَةِ، وَيَتَطَهَّرُ مَا اسْتَطَاعَ مِنْ طُهْرٍ، وَيَدَّهِنُ مِنْ دُهْنِهِ، أَوْ يَمَسُّ مِنْ طِيبِ بَيْتِهِ، ثُمَّ يَخْرُجُ فَلاَ يُفَرِّقُ بَيْنَ اثْنَيْنِ، ثُمَّ يُصَلِّي مَا كُتِبَ لَهُ، ثُمَّ يُنْصِتُ إِذَا تَكَلَّمَ الإِمَامُ، إِلَّا غُفِرَ لَهُ مَا بَيْنَهُ وَبَيْنَ الجُمُعَةِ الأُخْرَى»",
            en = "Narrated by Salman Al-Farsi (RA): The Prophet ﷺ said: \"Whoever takes a bath on Friday, purifies himself as much as possible, puts on his oil or applies perfume from his house, then proceeds to the mosque... his sins between that Friday and the next will be forgiven.\"",
            hi = "हज़रत सलमान फ़ारसी (रज़ि.) से रिवायत है कि नबी करीम ﷺ ने फ़रमाया: \"जो शख़्स जुमे के दिन ग़ुस्ल करे, जितनी हो सके पाकीज़गी हासिल करे, तेल या घर में रखी ख़ुशबू लगाए, फिर मस्जिद जाए... तो उसके इस जुमे से अगले जुमे तक के गुनाह बख़्श दिए जाते हैं।\"",
            ur = "حضرت سلمان فارسی رضی اللہ عنہ سے روایت ہے کہ نبی کریم ﷺ نے فرمایا: \"جو شخص جمعہ کے دن غسل کرے، جتنی ہو سکے صفائی کرے، تیل یا خوشبو لگائے، پھر مسجد جائے... تو اس کے اس جمعہ سے دوسرے جمعہ تک کے گناہ معاف کر دیے جاتے ہیں۔\""
        ),
        "early_mosque" to JummahHadithDetail(
            ref = "Sahih al-Bukhari 881 | Sahih Muslim 850",
            grade = "Sahih (Authentic)",
            links = listOf(HadithSourceLink("Sahih al-Bukhari 881 (Sunnah.com)", "https://sunnah.com/bukhari:881"), HadithSourceLink("Sahih Muslim 850 (Sunnah.com)", "https://sunnah.com/muslim:850")),
            ar = "عَنْ أَبِي هُرَيْرَةَ رَضِيَ اللَّهُ عَنْهُ، أَنَّ رَسُولَ اللَّهِ ﷺ قَالَ: «مَنِ اغْتَسَلَ يَوْمَ الجُمُعَةِ غُسْلَ الجَنَابَةِ ثُمَّ رَاحَ، فَكَأَنَّمَا قَرَّبَ بَدَنَةً، وَمَنْ رَاحَ فِي السَّاعَةِ الثَّانِيَةِ، فَكَأَنَّمَا قَرَّبَ بَقَرَةً، وَمَنْ رَاحَ فِي السَّاعَةِ الثَّالِثَةِ، فَكَأَنَّمَا قَرَّبَ كَبْشًا أَقْرَنَ، وَمَنْ رَاحَ فِي السَّاعَةِ الرَّابِعَةِ، فَكَأَنَّمَا قَرَّبَ دَجَاجَةً، وَمَنْ رَاحَ فِي السَّاعَةِ الخَامِسَةِ، فَكَأَنَّمَا قَرَّبَ بَيْضَةً...»",
            en = "Narrated by Abu Hurairah (RA): The Messenger of Allah ﷺ said: \"Whoever takes a bath on Friday and goes to the mosque in the first hour, it is as if he sacrificed a camel; whoever goes in the second hour, it is as if he sacrificed a cow; whoever goes in the third hour, it is as if he sacrificed a horned ram...\"",
            hi = "हज़रत अबू हुरैरा (रज़ि.) से रिवायत है कि रसूलुल्लाह ﷺ ने फ़रमाया: \"जो शख़्स जुमे के दिन ग़ुस्ल करके पहली घड़ी में मस्जिद जाता है, गोया उसने ऊंट की क़ुर्बानी दी; जो दूसरी घड़ी में जाए गोया उसने गाय की क़ुर्बानी दी; जो तीसरी घड़ी में जाए गोया उसने मेंढे की क़ुर्बानी दी...\"",
            ur = "حضرت ابوہریرہ رضی اللہ عنہ سے روایت ہے کہ رسول اللہ ﷺ نے فرمایا: \"جو شخص جمعہ کے دن پہلی گھڑی میں مسجد جائے گویا اس نے اونٹ کی قربانی دی، جو دوسری گھڑی میں جائے گویا اس نے گائے کی قربانی دی، اور جو تیسری گھڑی میں جائے گویا اس نے مینڈھے کی قربانی دی...\""
        ),
        "walk_mosque" to JummahHadithDetail(
            ref = "Jami at-Tirmidhi 496 | Sunan Abi Dawood 345",
            grade = "Sahih (Authentic)",
            links = listOf(HadithSourceLink("Jami at-Tirmidhi 496 (Sunnah.com)", "https://sunnah.com/tirmidhi:496"), HadithSourceLink("Sunan Abi Dawood 345 (Sunnah.com)", "https://sunnah.com/abudawud:345")),
            ar = "عَنْ أَوْسِ بْنِ أَوْسٍ الثَّقَفِيِّ رَضِيَ اللَّهُ عَنْهُ قَالَ: قَالَ رَسُولُ اللَّهِ ﷺ: «مَنْ غَسَّلَ يَوْمَ الجُمُعَةِ وَاغْتَسَلَ، ثُمَّ بَكَّرَ وَابْتَكَرَ، وَمَشَى وَلَمْ يَرْكَبْ، وَدَنَا مِنَ الإِمَامِ فَاسْتَمَعَ وَلَمْ يَلْغُ، كَانَ لَهُ بِكُلِّ خُطْوَةٍ عَمَلُ سَنَةٍ أَجْرُ صِيَامِهَا وَقِيَامِهَا»",
            en = "Narrated by Aws ibn Aws (RA): The Prophet ﷺ said: \"Whoever takes a bath on Friday, goes early on foot without riding, sits close to the Imam, and listens attentively... for every step he takes, he will have the reward of a whole year of fasting and standing in night prayer.\"",
            hi = "हज़रत औस बिन औस (रज़ि.) से रिवायत है कि रसूलुल्लाह ﷺ ने फ़रमाया: \"जो शख़्स जुमे के दिन ग़ुस्ल करे, जल्दी मस्जिद जाए, पैदल चले और सवारी न करे, इमाम के क़रीब होकर ख़ामोशी से सुने... तो उसके हर क़दम पर एक साल के रोज़ों और एक साल की रातों की नमाज़ (तहज्जुद) का सवाब मिलता है।\"",
            ur = "حضرت اوس بن اوس رضی اللہ عنہ سے روایت ہے کہ رسول اللہ ﷺ نے فرمایا: \"جو شخص جمعہ کے دن غسل کرے، جلدی مسجد جائے، پیدل جائے اور سوار نہ ہو، امام کے قریب بیٹھ کر خاموشی سے سنے... تو اس کے ہر قدم پر پورے ایک سال کے روزوں اور ایک سال کی راتوں کی نماز کا ثواب ملتا ہے۔\""
        ),
        "khutbah" to JummahHadithDetail(
            ref = "Sahih al-Bukhari 934 | Sahih Muslim 851",
            grade = "Sahih (Authentic)",
            links = listOf(HadithSourceLink("Sahih al-Bukhari 934 (Sunnah.com)", "https://sunnah.com/bukhari:934"), HadithSourceLink("Sahih Muslim 851 (Sunnah.com)", "https://sunnah.com/muslim:851")),
            ar = "عَنْ أَبِي هُرَيْرَةَ رَضِيَ اللَّهُ عَنْهُ، أَنَّ رَسُولَ اللَّهِ ﷺ قَالَ: «إِذَا قُلْتَ لِصَاحِبِكَ يَوْمَ الجُمُعَةِ: أَنْصِتْ، وَالإِمَامُ يَخْطُبُ، فَقَدْ لَغَوْتَ»",
            en = "Narrated by Abu Hurairah (RA): The Messenger of Allah ﷺ said: \"If you say to your companion, 'Listen quietly,' on Friday while the Imam is delivering the sermon (Khutbah), you have engaged in idle talk (and nullified your reward).\"",
            hi = "हज़रत अबू हुरैरा (रज़ि.) से रिवायत है कि रसूलुल्लाह ﷺ ने फ़रमाया: \"अगर तुमने जुमे के दिन ख़ुत्बे के दौरान अपने साथी से इतना भी कहा कि 'ख़ामोश रहो', तो तुमने भी बेकार बात की (और सवाब गँवा दिया)।\"",
            ur = "حضرت ابوہریرہ رضی اللہ عنہ سے روایت ہے کہ رسول اللہ ﷺ نے فرمایا: \"جمعہ کے دن خطبے کے دوران اگر تم نے اپنے ساتھی سے اتنا بھی کہا کہ 'خاموش رہو'، تو تم نے لغو (بے کار) کام کیا۔\""
        ),
        "surah_kahf" to JummahHadithDetail(
            ref = "Sahih al-Jami' 6470 | Al-Mustadrak 3392 (2/399) | Sunan al-Kubra 6063",
            grade = "Sahih (Authentic — Shaykh al-Albani & Imam al-Hakim)",
            links = listOf(HadithSourceLink("1. Sahih al-Jami' 6470 (Shaykh al-Albani / Dorar)", "https://dorar.net/hadith/sharh/146476"), HadithSourceLink("2. Al-Mustadrak 'ala al-Sahihayn 3392 (Imam al-Hakim / Dorar)", "https://dorar.net/h/jyf7OzOs?osoul=1"), HadithSourceLink("3. Sunan al-Kubra 6063 (Imam al-Bayhaqi / Dorar)", "https://dorar.net/h/jyf7OzOs?osoul=1")),
            ar = "عَنْ أَبِي سَعِيدٍ الْخُدْرِيِّ رَضِيَ اللَّهُ عَنْهُ، أَنَّ النَّبِيَّ ﷺ قَالَ: «مَنْ قَرَأَ سُورَةَ الْكَهْفِ فِي يَوْمِ الْجُمُعَةِ أَضَاءَ لَهُ مِنَ النُّورِ مَا بَيْنَ الْجُمُعَتَيْنِ» [صحيح الجامع الصغير للألباني ٦٤٧٠، المستدرك للحاكم ٣٣٩٢، السنن الكبرى للبيهقي ٦٠٦٣]",
            en = "Narrated by Abu Sa'id Al-Khudri (RA): The Prophet ﷺ said: \"Whoever recites Surah Al-Kahf on Friday, a light (Noor) will illuminate for him from one Friday to the next.\" [Sahih al-Jami' (Shaykh al-Albani 6470), Al-Mustadrak 'ala al-Sahihayn (Imam al-Hakim 3392), Sunan al-Kubra (Imam al-Bayhaqi 6063)]",
            hi = "हज़रत अबू सईद ख़ुदरी (रज़ि.) से रिवायत है कि नबी करीम ﷺ ने फ़रमाया: \"जो शख़्स जुमे के दिन सूरह अल-कहफ़ की तिलावत करेगा, उसके लिए दोनों जुमों के दरमियान नूर चमकता रहेगा।\" [सहीह अल-जामेअ (अल्बानी 6470), अल-मुस्तदरक (हाकिम 3392), सुनन अल-कुबरा (बैहक़ी 6063)]",
            ur = "حضرت ابو سعید خدری رضی اللہ عنہ سے روایت ہے کہ نبی کریم ﷺ نے فرمایا: \"جو شخص جمعہ کے دن سورۃ الکہف کی تلاوت کرے گا، اس کے لیے دونوں جمعوں کے درمیان نور روشن رہے گا۔\" [صحیح الجامع الصغیر (البانی 6470)، المستدرک للحاکم (3392)، السنن الکبریٰ للبیہقی (6063)]"
        ),
        "durood" to JummahHadithDetail(
            ref = "Sunan Abi Dawood 1047 | Sunan an-Nasa'i 1374",
            grade = "Sahih (Authentic)",
            links = listOf(HadithSourceLink("Sunan Abi Dawood 1047 (Sunnah.com)", "https://sunnah.com/abudawud:1047"), HadithSourceLink("Sunan an-Nasa'i 1374 (Sunnah.com)", "https://sunnah.com/nasai:1374")),
            ar = "عَنْ أَوْسِ بْنِ أَوْسٍ رَضِيَ اللَّهُ عَنْهُ قَالَ: قَالَ رَسُولُ اللَّهِ ﷺ: «إِنَّ مِنْ أَفْضَلِ أَيَّامِكُمْ يَوْمَ الْجُمُعَةِ، فِيهِ خُلِقَ آدَمُ، وَفِيهِ قُبِضَ... فَأَكْثِرُوا عَلَيَّ مِنَ الصَّلَاةِ فِيهِ، فَإِنَّ صَلَاتَكُمْ مَعْرُوضَةٌ عَلَيَّ»",
            en = "Narrated by Aws ibn Aws (RA): The Messenger of Allah ﷺ said: \"Among the most excellent of your days is Friday... therefore, send abundant blessings (Salawat/Durood) upon me on this day, for your blessings are presented to me directly.\"",
            hi = "हज़रत औस बिन औस (रज़ि.) से रिवायत है कि रसूलुल्लाह ﷺ ने फ़रमाया: \"बेशक तुम्हारे दिनों में सबसे अफ़ज़ल दिन जुमे का दिन है... इसलिए इस दिन मुझ पर कसरत से दुरूद भेजा करो, क्योंकि तुम्हारा दुरूद मुझ पर पेश किया जाता है।\"",
            ur = "حضرت اوس بن اوس رضی اللہ عنہ سے روایت ہے کہ رسول اللہ ﷺ نے فرمایا: \"تمہارے تمام دنوں میں سب سے افضل دن جمعہ کا دن ہے... لہٰذا اس دن مجھ پر کثرت سے درود بھیجا کرو، کیونکہ تمہارا درود مجھ پر پیش کیا جاتا ہے۔\""
        ),
        "dua_hour" to JummahHadithDetail(
            ref = "Sahih al-Bukhari 935 | Sunan Abi Dawood 1048",
            grade = "Sahih (Authentic)",
            links = listOf(HadithSourceLink("Sahih al-Bukhari 935 (Sunnah.com)", "https://sunnah.com/bukhari:935"), HadithSourceLink("Sunan Abi Dawood 1048 (Sunnah.com)", "https://sunnah.com/abudawud:1048")),
            ar = "عَنْ أَبِي هُرَيْرَةَ رَضِيَ اللَّهُ عَنْهُ، أَنَّ رَسُولَ اللَّهِ ﷺ ذَكَرَ يَوْمَ الْجُمُعَةِ فَقَالَ: «فِيهِ سَاعَةٌ لاَ يُوَافِقُهَا عَبْدٌ مُسْلِمٌ، وَهُوَ قَائِمٌ يُصَلِّي، يَسْأَلُ اللَّهَ تَعَالَى شَيْئًا، إِلَّا أَعْطَاهُ إِيَّاهُ... فَالْتَمِسُوهَا آخِرَ سَاعَةٍ بَعْدَ الْعَصْرِ»",
            en = "Narrated by Abu Hurairah (RA): The Messenger of Allah ﷺ mentioned Friday and said: \"There is an hour on Friday when no Muslim servant stands in prayer asking Allah for anything good but He gives it to him... Seek it in the last hour after Asr prayer.\"",
            hi = "हज़रत अबू हुरैरा (रज़ि.) से रिवायत है कि रसूलुल्लाह ﷺ ने जुमे के दिन का ज़िक्र करते हुए फ़रमाया: \"जुमे में एक ऐसी घड़ी है जिसमें कोई मुसलमान बन्दा नमाज़ में अल्लाह से जो भी भलाई मांगता है, अल्लाह उसे अता फ़रमाता है... इसे असर के बाद की आख़िरी घड़ी में तलाश करो।\"",
            ur = "حضرت ابوہریرہ رضی اللہ عنہ سے روایت ہے کہ رسول اللہ ﷺ نے جمعہ کے دن کا ذکر کرتے ہوئے فرمایا: \"جمعہ کے دن ایک گھڑی ایسی ہے کہ کوئی مسلمان بندہ اس وقت اللہ سے جو بھی دعا مانگتا ہے, اللہ اسے ضرور عطا فرماتا ہے... اسے عصر کے بعد آخری گھڑی میں تلاش کرو۔\""
        ),
        "charity" to JummahHadithDetail(
            ref = "Musannaf 'Abd al-Razzaq 5558 — Sahih Isnad",
            grade = "Sahih Isnad (Authentic Chain)",
            links = listOf(HadithSourceLink("Musannaf 'Abd al-Razzaq 5558 (Islamweb.net)", "https://www.islamweb.net/ar/library/content/73/5302/%D8%A8%D8%A7%D8%A8-%D8%B9%D8%B8%D9%85-%D9%8A%D9%88%D9%85-%D8%A7%D9%84%D8%AC%D9%85%D8%B9%D8%A9")),
            ar = "عَنْ كَعْبِ الْأَحْبَارِ رَضِيَ اللَّهُ عَنْهُ قَالَ: «الصَّدَقَةُ يَوْمَ الْجُمُعَةِ أَعْظَمُ مِنْهَا فِي سَائِرِ الْأَيَّامِ» [مصنف عبد الرزاق ٥٥٥٨، إسناده صحيح]",
            en = "Narrated by Ka'b al-Ahbar (RA): \"Giving charity (Sadaqah) on Friday is greater in reward than on any other day of the week.\" [Musannaf 'Abd al-Razzaq 5558 — Sahih Isnad]",
            hi = "हज़रत का'ब अल-अहबार (रज़ि.) फ़रमाते हैं: \"जुमे के दिन सदक़ा व ख़ैरात करने का सवाब बाक़ी तमाम दिनों के मुक़ाबले में सबसे अफ़ज़ल और अज़ीम है।\" [मुसन्नफ़ अब्दुर रज़्ज़ाक़ 5558 — सनद सहीह]",
            ur = "حضرت کعب الاحبار رضی اللہ عنہ فرماتے ہیں: \"جمعہ کے دن صدقہ و خیرات کرنا ہفتے کے باقی تمام دنوں کے مقابلے میں سب سے زیادہ اجر و فضیلت رکھتا ہے۔\" [مصنف عبدالرزاق 5558 — سند صحیح]"
        ),
    )

    val uiPacks: Map<String, JummahUiPack> = mapOf(
        "hi" to JummahUiPack(
            badge = "मुबारक जुमा",
            title = "जुम्मा मुबारक",
            hadithHero = "\"सूरज निकलने वाले दिनों में सबसे बेहतरीन दिन जुमे का दिन है।\" — सहीह मुस्लिम (854)",
            progressLabel = "सुन्नतें मुकम्मल",
            duroodCounterTitle = "दरूद शरीफ़ विर्द काउंटर",
            duroodTapBtn = "दुरूद पढ़ें (+1)",
            duroodReset = "रीसेट",
            readKahfBtn = "📖 सूरह अल-कहफ़ खोलें",
            mubarakTitle = "माशाअल्लाह! जुमे की सुन्नतें पूरी हुईं",
            mubarakDesc = "अल्लाह तआला आपकी इबादत और सुन्नतों को क़बूल फ़रमाए।",
            resetBtn = "चेकलिस्ट रीसेट करें"
        ),
        "en" to JummahUiPack(
            badge = "BLESSED FRIDAY",
            title = "Jummah Mubarak",
            hadithHero = "\"The best day on which the sun rises is Friday.\" — Sahih Muslim (854)",
            progressLabel = "Sunnahs Completed",
            duroodCounterTitle = "Salawat / Durood Tap Counter",
            duroodTapBtn = "Tap for Salawat (+1)",
            duroodReset = "Reset",
            readKahfBtn = "📖 Open Surah Al-Kahf",
            mubarakTitle = "MashaAllah! Friday Sunnahs Completed",
            mubarakDesc = "May Allah accept all your Friday deeds, prayers, and blessings.",
            resetBtn = "Reset Checklist"
        ),
        "ur" to JummahUiPack(
            badge = "مبارک جمعہ",
            title = "جمعہ مبارک",
            hadithHero = "\"تمام دنوں میں سب سے بہترین دن جس پر سورج طلوع ہوتا ہے، جمعہ کا دن ہے۔\" — صحیح مسلم (854)",
            progressLabel = "سنتیں مکمل",
            duroodCounterTitle = "درود شریف کاؤنٹر",
            duroodTapBtn = "درود شریف (+1)",
            duroodReset = "ری سیٹ",
            readKahfBtn = "📖 سورۃ الکہف کھولیں",
            mubarakTitle = "ماشاءاللہ! جمعہ کی تمام سنتیں مکمل ہوئیں",
            mubarakDesc = "اللہ تعالی آپ کی عبادات اور دعاؤں کو قبول فرمائے۔",
            resetBtn = "چیک لسٹ ری سیٹ کریں"
        ),
        "ar" to JummahUiPack(
            badge = "يوم الجمعة المبارك",
            title = "جمعة مباركة",
            hadithHero = "«خَيْرُ يَوْمٍ طَلَعَتْ عَلَيْهِ الشَّمْسُ يَوْمُ الْجُمُعَةِ» — صحيح مسلم (854)",
            progressLabel = "السنن المكتملة",
            duroodCounterTitle = "عداد الصلاة على النبي ﷺ",
            duroodTapBtn = "اضغط للصلاة (+1)",
            duroodReset = "إعادة ضبط",
            readKahfBtn = "📖 قراءة سورة الكهف",
            mubarakTitle = "ما شاء الله! اكتملت سنن الجمعة",
            mubarakDesc = "تقبل الله منا ومنكم صالح الأعمال والطاعات.",
            resetBtn = "إعادة ضبط القائمة"
        ),
        "bn" to JummahUiPack(
            badge = "বরকতময় শুক্রবার",
            title = "জুম্মা মোবারক",
            hadithHero = "\"সূর্য উদিত হওয়া দিনগুলির মধ্যে সর্বোত্তম দিন হলো শুক্রবার।\" — সহীহ মুসলিম (৮৫৪)",
            progressLabel = "সুন্নাত সম্পন্ন",
            duroodCounterTitle = "দরূদ শরীফ কাউন্টার",
            duroodTapBtn = "দরূদ পাঠ করুন (+১)",
            duroodReset = "রিসেট",
            readKahfBtn = "📖 সূরা আল-কাহাফ খুলুন",
            mubarakTitle = "মাশাআল্লাহ! জুমার সুন্নাতসমূহ সম্পন্ন হয়েছে",
            mubarakDesc = "আল্লাহ তায়ালা আপনার সকল ইবাদত ও দোয়া কবুল করুন।",
            resetBtn = "চেকলিস্ট রিসেট করুন"
        ),
    )

    fun getUiPack(lang: String): JummahUiPack {
        return uiPacks[lang] ?: uiPacks["en"] ?: JummahUiPack(
            badge = "BLESSED FRIDAY",
            title = "Jummah Mubarak",
            hadithHero = "\"The best day on which the sun rises is Friday.\" — Sahih Muslim (854)",
            progressLabel = "Sunnahs Completed",
            duroodCounterTitle = "Salawat / Durood Tap Counter",
            duroodTapBtn = "Tap for Salawat (+1)",
            duroodReset = "Reset",
            readKahfBtn = "📖 Open Surah Al-Kahf",
            mubarakTitle = "MashaAllah! Friday Sunnahs Completed",
            mubarakDesc = "May Allah accept all your Friday deeds, prayers, and blessings.",
            resetBtn = "Reset Checklist"
        )
    }

    fun getItemTitle(item: JummahSunnahItem, lang: String): String {
        return item.titles[lang] ?: item.titles["en"] ?: item.titles["hi"] ?: item.id
    }

    fun getItemDesc(item: JummahSunnahItem, lang: String): String {
        return item.descs[lang] ?: item.descs["en"] ?: item.descs["hi"] ?: ""
    }

    fun getItemHadithRef(item: JummahSunnahItem, lang: String): String {
        return item.hadiths[lang] ?: item.hadiths["en"] ?: item.hadiths["hi"] ?: ""
    }
}
