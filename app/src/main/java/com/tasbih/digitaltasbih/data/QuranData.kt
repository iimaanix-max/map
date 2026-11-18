package com.tasbih.digitaltasbih.data

data class SurahMeta(
    val number: Int,
    val arabicName: String,
    val englishName: String,
    val englishMeaning: String,
    val numberOfAyahs: Int,
    val revelationType: String, // "Meccan" or "Medinan"
    val hindiName: String,
    val hindiMeaning: String,
    val urduName: String
) {
    fun getLocalizedName(lang: String): String = when (lang) {
        "en" -> englishName
        "ur" -> if (urduName.isNotBlank()) urduName else englishName
        "ar" -> arabicName
        else -> hindiName
    }

    fun getLocalizedMeaning(lang: String): String = when (lang) {
        "en" -> englishMeaning
        "ur" -> hindiMeaning
        "ar" -> arabicName
        else -> hindiMeaning
    }

    fun getLocalizedType(lang: String): String = when (lang) {
        "en" -> revelationType
        "ur" -> if (revelationType == "Meccan") "مکی" else "مدنی"
        "ar" -> if (revelationType == "Meccan") "مكية" else "مدنية"
        else -> if (revelationType == "Meccan") "मक्की" else "मदनी"
    }

    fun getLocalizedAyahsLabel(lang: String): String = when (lang) {
        "en" -> "$numberOfAyahs Ayahs"
        "ur" -> "$numberOfAyahs آیات"
        "ar" -> "$numberOfAyahs آيات"
        else -> "$numberOfAyahs आयतें"
    }
}

fun toArabicDigits(num: Int): String {
    val arabicIndicDigits = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
    return num.toString().map { if (it in '0'..'9') arabicIndicDigits[it - '0'] else it }.joinToString("")
}

data class JuzSurahRange(
    val surahNumber: Int,
    val surahHindiName: String,
    val ayahRange: String
)

data class JuzMeta(
    val juzNumber: Int,
    val arabicName: String,
    val ranges: List<JuzSurahRange>
)

data class QuranReciterItem(
    val identifier: String,
    val englishName: String,
    val letter: String,
    val flag: String = "",
    val country: String = "",
    val hindiName: String = "",
    val urduName: String = "",
    val arabicName: String = "",
    val bengaliName: String = "",
    val hindiCountry: String = "",
    val urduCountry: String = "",
    val arabicCountry: String = "",
    val bengaliCountry: String = ""
) {
    fun getLocalizedName(lang: String): String = when (lang) {
        "hi" -> hindiName.ifBlank { englishName }
        "ur" -> urduName.ifBlank { arabicName.ifBlank { englishName } }
        "ar" -> arabicName.ifBlank { englishName }
        "bn" -> bengaliName.ifBlank { englishName }
        else -> englishName
    }

    fun getLocalizedCountry(lang: String): String = when (lang) {
        "hi" -> hindiCountry.ifBlank { country }
        "ur" -> urduCountry.ifBlank { country }
        "ar" -> arabicCountry.ifBlank { country }
        "bn" -> bengaliCountry.ifBlank { country }
        else -> country
    }

    fun getLocalizedLetter(lang: String): String = when (lang) {
        "hi" -> when {
            identifier.contains("abdullah") || identifier.contains("sudais") || identifier.contains("samad") || identifier.contains("shaatree") || identifier.contains("ajamy") || identifier.contains("alafasy") -> "अ"
            identifier.contains("hanirifai") || identifier.contains("husary") || identifier.contains("hudhaify") -> "ह"
            identifier.contains("akhdar") -> "इ"
            identifier.contains("muaiqly") || identifier.contains("minshawi") || identifier.contains("ayyoub") || identifier.contains("jibreel") -> "म"
            identifier.contains("shuraym") -> "स"
            else -> letter
        }
        "ur", "ar" -> when {
            identifier.contains("abdullah") || identifier.contains("sudais") || identifier.contains("samad") || identifier.contains("alafasy") -> "ع"
            identifier.contains("shaatree") || identifier.contains("ajamy") -> "أ"
            identifier.contains("hanirifai") || identifier.contains("husary") || identifier.contains("hudhaify") -> "ح"
            identifier.contains("akhdar") -> "إ"
            identifier.contains("muaiqly") || identifier.contains("minshawi") || identifier.contains("ayyoub") || identifier.contains("jibreel") -> "م"
            identifier.contains("shuraym") -> "س"
            else -> letter
        }
        "bn" -> when {
            identifier.contains("abdullah") || identifier.contains("sudais") || identifier.contains("samad") || identifier.contains("shaatree") || identifier.contains("ajamy") || identifier.contains("alafasy") -> "আ"
            identifier.contains("hanirifai") || identifier.contains("husary") || identifier.contains("hudhaify") -> "হ"
            identifier.contains("akhdar") -> "ই"
            identifier.contains("muaiqly") || identifier.contains("minshawi") || identifier.contains("ayyoub") || identifier.contains("jibreel") -> "ম"
            identifier.contains("shuraym") -> "স"
            else -> letter
        }
        else -> letter
    }
}

object QuranData {
    val surahs: List<SurahMeta> = listOf(
        SurahMeta(1, "الفاتحة", "Al-Fatihah", "The Opener", 7, "Meccan", "अल-फ़ातिहा", "प्रारंभ / शुरुआत", "الفاتحہ"),
        SurahMeta(2, "البقرة", "Al-Baqarah", "The Cow", 286, "Medinan", "अल-बक़रह", "गाय", "البقرہ"),
        SurahMeta(3, "آل عمران", "Ali 'Imran", "Family of Imran", 200, "Medinan", "आल-इमरान", "इमरान का परिवार", "آل عمران"),
        SurahMeta(4, "النساء", "An-Nisa", "The Women", 176, "Medinan", "अन-निसा", "महिलाएं / औरतें", "النساء"),
        SurahMeta(5, "المائدة", "Al-Ma'idah", "The Table Spread", 120, "Medinan", "अल-माइदा", "दस्तरख़्वान / भोजन की थाल", "المائدہ"),
        SurahMeta(6, "الأنعام", "Al-An'am", "The Cattle", 165, "Meccan", "अल-अनआम", "मवेशी / चौपाए", "الانعام"),
        SurahMeta(7, "الأعراف", "Al-A'raf", "The Heights", 206, "Meccan", "अल-आराफ़", "ऊंचाइयां", "الاعراف"),
        SurahMeta(8, "الأنفال", "Al-Anfal", "The Spoils of War", 75, "Medinan", "अल-अनफ़ाल", "ग़नीमत का माल", "الانفال"),
        SurahMeta(9, "التوبة", "At-Tawbah", "The Repentance", 129, "Medinan", "अत-तौबह", "पश्चाताप / तौबा", "التوبہ"),
        SurahMeta(10, "يونس", "Yunus", "Jonah", 109, "Meccan", "यूनुस", "पैगंबर यूनुस", "یونس"),
        SurahMeta(11, "هود", "Hud", "Hud", 123, "Meccan", "हूद", "पैगंबर हूद", "ہود"),
        SurahMeta(12, "يوسف", "Yusuf", "Joseph", 111, "Meccan", "यूसुफ़", "पैगंबर यूसुफ़", "یوسف"),
        SurahMeta(13, "الرعد", "Ar-Ra'd", "The Thunder", 43, "Medinan", "अर-रअद", "बादलों की गरज", "الرعد"),
        SurahMeta(14, "ابراهيم", "Ibrahim", "Abraham", 52, "Meccan", "इब्राहीम", "पैगंबर इब्राहीम", "ابراہیم"),
        SurahMeta(15, "الحجر", "Al-Hijr", "The Rocky Tract", 99, "Meccan", "अल-हिज्र", "पथरीली घाटी", "الحجر"),
        SurahMeta(16, "النحل", "An-Nahl", "The Bee", 128, "Meccan", "अन-नह्ल", "मधुमक्खी", "النحل"),
        SurahMeta(17, "الإسراء", "Al-Isra", "The Night Journey", 111, "Meccan", "अल-इसरा", "रात का सफ़र", "الاسراء"),
        SurahMeta(18, "الكهف", "Al-Kahf", "The Cave", 110, "Meccan", "अल-कहफ़", "गुफ़ा", "الکہف"),
        SurahMeta(19, "مريم", "Maryam", "Mary", 98, "Meccan", "मरयम", "हज़रत मरयम", "مریم"),
        SurahMeta(20, "طه", "Taha", "Ta-Ha", 135, "Meccan", "ता-हा", "ता-हा", "طٰہٰ"),
        SurahMeta(21, "الأنبياء", "Al-Anbya", "The Prophets", 112, "Meccan", "अल-अंबिया", "पैगंबरगण", "الانبیاء"),
        SurahMeta(22, "الحج", "Al-Hajj", "The Pilgrimage", 78, "Medinan", "अल-हज्ज", "हज यात्रा", "الحج"),
        SurahMeta(23, "المؤمنون", "Al-Mu'minun", "The Believers", 118, "Meccan", "अल-मुमिनून", "ईमान वाले / मोमिन", "المؤمنون"),
        SurahMeta(24, "النور", "An-Nur", "The Light", 64, "Medinan", "अन-नूर", "ज्योति / प्रकाश", "النور"),
        SurahMeta(25, "الفرقان", "Al-Furqan", "The Criterion", 77, "Meccan", "अल-फ़ुरक़ान", "सत्य और असत्य का फ़र्क़", "الفرقان"),
        SurahMeta(26, "الشعراء", "Ash-Shu'ara", "The Poets", 227, "Meccan", "अश-शुआरा", "कविगण / शायर", "الشعراء"),
        SurahMeta(27, "النمل", "An-Naml", "The Ant", 93, "Meccan", "अन-नम्ल", "चींटी", "النمل"),
        SurahMeta(28, "القصص", "Al-Qasas", "The Stories", 88, "Meccan", "अल-क़सस", "क़िस्से / वृत्तांत", "القصص"),
        SurahMeta(29, "العنكبوت", "Al-'Ankabut", "The Spider", 69, "Meccan", "अल-अनकबूत", "मकड़ी", "العنکبوت"),
        SurahMeta(30, "الروم", "Ar-Rum", "The Romans", 60, "Meccan", "अर-रूम", "रोमवासी", "الروم"),
        SurahMeta(31, "لقمان", "Luqman", "Luqman", 34, "Meccan", "लुक़मान", "हकीम लुक़मान", "لقمان"),
        SurahMeta(32, "السجدة", "As-Sajdah", "The Prostration", 30, "Meccan", "अस-सजदा", "सजदा", "السجدہ"),
        SurahMeta(33, "الأحزاب", "Al-Ahzab", "The Combined Forces", 73, "Medinan", "अल-अहज़ाब", "संयुक्त दल / गिरोह", "الاحزاب"),
        SurahMeta(34, "سبإ", "Saba", "Sheba", 54, "Meccan", "सबा", "सबा देश", "سبا"),
        SurahMeta(35, "فاطر", "Fatir", "Originator", 45, "Meccan", "फ़ातिर", "सृष्टिकर्ता / पैदा करने वाला", "فاطر"),
        SurahMeta(36, "يس", "Ya-Sin", "Ya Sin", 83, "Meccan", "या-सीन", "या-सीन", "یٰسٓ"),
        SurahMeta(37, "الصافات", "As-Saffat", "Those who set the Ranks", 182, "Meccan", "अस-साफ़्फ़ात", "पंक्तिबद्ध खड़े होने वाले", "الصافات"),
        SurahMeta(38, "ص", "Sad", "The Letter \"Saad\"", 88, "Meccan", "साद", "साद", "ص"),
        SurahMeta(39, "الزمر", "Az-Zumar", "The Troops", 75, "Meccan", "अज़-ज़ुमर", "जत्थे / गिरोह", "الزمر"),
        SurahMeta(40, "غافر", "Ghafir", "The Forgiver", 85, "Meccan", "ग़ाफ़िर", "क्षमा करने वाला / बख़्शने वाला", "غافر"),
        SurahMeta(41, "فصلت", "Fussilat", "Explained in Detail", 54, "Meccan", "फ़ुस्सिलत", "विस्तारपूर्वक समझाई गई", "فصلت"),
        SurahMeta(42, "الشورى", "Ash-Shuraa", "The Consultation", 53, "Meccan", "अश-शूरा", "आपसी परामर्श / मशविरा", "الشوریٰ"),
        SurahMeta(43, "الزخرف", "Az-Zukhruf", "The Ornaments of Gold", 89, "Meccan", "अज़-ज़ुख़रुफ़", "सोने के गहने", "الزخرف"),
        SurahMeta(44, "الدخان", "Ad-Dukhan", "The Smoke", 59, "Meccan", "अद-दुख़ान", "धुआं", "الدخان"),
        SurahMeta(45, "الجاثية", "Al-Jathiyah", "The Crouching", 37, "Meccan", "अल-जासिया", "घुटनों के बल झुके हुए", "الجاثیہ"),
        SurahMeta(46, "الأحقاف", "Al-Ahqaf", "The Wind-Curved Sandhills", 35, "Meccan", "अल-अहक़ाफ़", "रेत के टीले", "الاحقاف"),
        SurahMeta(47, "محمد", "Muhammad", "Muhammad", 38, "Medinan", "मुहम्मद", "पैगंबर मुहम्मद ﷺ", "محمد"),
        SurahMeta(48, "الفتح", "Al-Fath", "The Victory", 29, "Medinan", "अल-फ़तह", "शानदार विजय / जीत", "الفتح"),
        SurahMeta(49, "الحجرات", "Al-Hujurat", "The Rooms", 18, "Medinan", "अल-हुजुरात", "कमरे / हुजरे", "الحجرات"),
        SurahMeta(50, "ق", "Qaf", "The Letter \"Qaf\"", 45, "Meccan", "क़ाफ़", "क़ाफ़", "ق"),
        SurahMeta(51, "الذاريات", "Adh-Dhariyat", "The Winnowing Winds", 60, "Meccan", "अज़-ज़ारियात", "बिखेरने वाली हवाएं", "الذاریات"),
        SurahMeta(52, "الطور", "At-Tur", "The Mount", 49, "Meccan", "अत-तूर", "तूर पर्वत", "الطور"),
        SurahMeta(53, "النجم", "An-Najm", "The Star", 62, "Meccan", "अन-नज्म", "तारा", "النجم"),
        SurahMeta(54, "القمر", "Al-Qamar", "The Moon", 55, "Meccan", "अल-क़मर", "चाँद", "القمر"),
        SurahMeta(55, "الرحمن", "Ar-Rahman", "The Beneficent", 78, "Medinan", "अर-रहमान", "अत्यंत दयालु", "الرحمن"),
        SurahMeta(56, "الواقعة", "Al-Waqi'ah", "The Inevitable", 96, "Meccan", "अल-वाक़िआ", "होने वाली घटना / क़ियामत", "الواقعہ"),
        SurahMeta(57, "الحديد", "Al-Hadid", "The Iron", 29, "Medinan", "अल-हदीद", "लोहा", "الحدید"),
        SurahMeta(58, "المجادلة", "Al-Mujadila", "The Pleading Woman", 22, "Medinan", "अल-मुजादिला", "बहस करने वाली महिला", "المجادلہ"),
        SurahMeta(59, "الحشر", "Al-Hashr", "The Exile", 24, "Medinan", "अल-हश्र", "एकत्रिकरण / निर्वासन", "الحشر"),
        SurahMeta(60, "الممتحنة", "Al-Mumtahanah", "She that is to be examined", 13, "Medinan", "अल-मुमतहना", "परखी जाने वाली स्त्री", "الممتحنہ"),
        SurahMeta(61, "الصف", "As-Saf", "The Ranks", 14, "Medinan", "अस-सफ़", "पंक्ति / कतार", "الصف"),
        SurahMeta(62, "الجمعة", "Al-Jumu'ah", "The Congregation, Friday", 11, "Medinan", "अल-जुमुआ", "जुमा का दिन", "الجمعہ"),
        SurahMeta(63, "المنافقون", "Al-Munafiqun", "The Hypocrites", 11, "Medinan", "अल-मुनाफ़िक़ून", "कपटाचारी / मुनाफ़िक़", "المنافقون"),
        SurahMeta(64, "التغابن", "At-Taghabun", "The Mutual Disillusion", 18, "Medinan", "अत-तग़ाबुन", "हार-जीत का दिन", "التغابن"),
        SurahMeta(65, "الطلاق", "At-Talaq", "The Divorce", 12, "Medinan", "अत-तलाक़", "तलाक़", "الطلاق"),
        SurahMeta(66, "التحريم", "At-Tahrim", "The Prohibition", 12, "Medinan", "अत-तहरीम", "वर्जित करना / हराम ठहराना", "التحریم"),
        SurahMeta(67, "الملك", "Al-Mulk", "The Sovereignty", 30, "Meccan", "अल-मुल्क", "प्रभुसत्ता / बादशाही", "الملک"),
        SurahMeta(68, "القلم", "Al-Qalam", "The Pen", 52, "Meccan", "अल-क़लम", "क़लम / लेखनी", "القلم"),
        SurahMeta(69, "الحاقة", "Al-Haqqah", "The Reality", 52, "Meccan", "अल-हाक़्क़ा", "सत्य घटना / क़ियामत", "الحاقہ"),
        SurahMeta(70, "المعارج", "Al-Ma'arij", "The Ascending Stairways", 44, "Meccan", "अल-मआरिज", "ऊंचाइयों के सोपान", "المعارج"),
        SurahMeta(71, "نوح", "Nuh", "Noah", 28, "Meccan", "नूह", "पैगंबर नूह", "نوح"),
        SurahMeta(72, "الجن", "Al-Jinn", "The Jinn", 28, "Meccan", "अल-जिन्न", "जिन्नात", "الجن"),
        SurahMeta(73, "المزمل", "Al-Muzzammil", "The Enshrouded One", 20, "Meccan", "अल-मुज़्ज़म्मिल", "कंबल ओढ़ने वाले", "المزمل"),
        SurahMeta(74, "المدثر", "Al-Muddaththir", "The Cloaked One", 56, "Meccan", "अल-मुद्दस्सिर", "चादर ओढ़ने वाले", "المدثر"),
        SurahMeta(75, "القيامة", "Al-Qiyamah", "The Resurrection", 40, "Meccan", "अल-क़ियामा", "क़ियामत / पुनरुत्थान", "القیامہ"),
        SurahMeta(76, "الانسان", "Al-Insan", "The Man", 31, "Medinan", "अल-इंसान", "इंसान / मनुष्य", "الانسان"),
        SurahMeta(77, "المرسلات", "Al-Mursalat", "The Emissaries", 50, "Meccan", "अल-मुरसलात", "भेजी जाने वाली हवाएं", "المرسلات"),
        SurahMeta(78, "النبإ", "An-Naba", "The Tidings", 40, "Meccan", "अन-नबा", "बड़ी ख़बर", "النباء"),
        SurahMeta(79, "النازعات", "An-Nazi'at", "Those who drag forth", 46, "Meccan", "अन-नाज़िआत", "खींच निकालने वाले फ़रिश्ते", "النازعات"),
        SurahMeta(80, "عبس", "'Abasa", "He Frowned", 42, "Meccan", "अबासा", "माथे पर बल डाला", "عبس"),
        SurahMeta(81, "التكوير", "At-Takwir", "The Overthrowing", 29, "Meccan", "अत-तकवीर", "लपेट दिया जाना", "التکویر"),
        SurahMeta(82, "الإنفطار", "Al-Infitar", "The Cleaving", 19, "Meccan", "अल-इन्फ़ितार", "आकाश का फटना", "الانفطار"),
        SurahMeta(83, "المطففين", "Al-Mutaffifin", "The Defrauding", 36, "Meccan", "अल-मुतफ़्फ़िफ़ीन", "नाप-तौल में कमी करने वाले", "المطففین"),
        SurahMeta(84, "الإنشقاق", "Al-Inshiqaq", "The Sundering", 25, "Meccan", "अल-इन्शिक़ाक़", "टुकड़े-टुकड़े होना", "الانشقاق"),
        SurahMeta(85, "البروج", "Al-Buruj", "The Mansions of the Stars", 22, "Meccan", "अल-बुरूज", "आकाश के बुर्ज", "البروج"),
        SurahMeta(86, "الطارق", "At-Tariq", "The Nightcommer", 17, "Meccan", "अत-तारिक़", "रात को चमकने वाला तारा", "الطارق"),
        SurahMeta(87, "الأعلى", "Al-A'la", "The Most High", 19, "Meccan", "अल-आला", "सर्वोच्च / सबसे महान", "الاعلیٰ"),
        SurahMeta(88, "الغاشية", "Al-Ghashiyah", "The Overwhelming", 26, "Meccan", "अल-ग़ाशिया", "छा जाने वाली विपत्ति", "الغاشیہ"),
        SurahMeta(89, "الفجر", "Al-Fajr", "The Dawn", 30, "Meccan", "अल-फ़ज्र", "प्रभात / भोर", "الفجر"),
        SurahMeta(90, "البلد", "Al-Balad", "The City", 20, "Meccan", "अल-बलद", "पवित्र शहर मक्का", "البلد"),
        SurahMeta(91, "الشمس", "Ash-Shams", "The Sun", 15, "Meccan", "अश-शम्स", "सूर्य / सूरज", "الشمس"),
        SurahMeta(92, "الليل", "Al-Layl", "The Night", 21, "Meccan", "अल-लैल", "रात", "اللیل"),
        SurahMeta(93, "الضحى", "Ad-Duhaa", "The Morning Hours", 11, "Meccan", "अद-दुहा", "चढ़ता दिन / चाश्त", "الضحیٰ"),
        SurahMeta(94, "الشرح", "Ash-Sharh", "The Relief", 8, "Meccan", "अश-शरह", "हृदय का विस्तार / सुकून", "الشرح"),
        SurahMeta(95, "التين", "At-Tin", "The Fig", 8, "Meccan", "अत-तीन", "अंजीर", "التین"),
        SurahMeta(96, "العلق", "Al-'Alaq", "The Clot", 19, "Meccan", "अल-अलक़", "जमा हुआ ख़ून", "العلق"),
        SurahMeta(97, "القدر", "Al-Qadr", "The Power", 5, "Meccan", "अल-क़द्र", "महिमा / शबे क़द्र", "القدر"),
        SurahMeta(98, "البينة", "Al-Bayyinah", "The Clear Proof", 8, "Medinan", "अल-बय्यिनाह", "स्पष्ट प्रमाण", "البینہ"),
        SurahMeta(99, "الزلزلة", "Az-Zalzalah", "The Earthquake", 8, "Medinan", "अज़-ज़लज़ला", "भूकंप / ज़लज़ला", "الزلزلہ"),
        SurahMeta(100, "العاديات", "Al-'Adiyat", "The Courser", 11, "Meccan", "अल-आदियात", "दौड़ने वाले घोड़े", "العادیات"),
        SurahMeta(101, "القارعة", "Al-Qari'ah", "The Calamity", 11, "Meccan", "अल-क़ारिआ", "खटखटाने वाली विपत्ति", "القارعہ"),
        SurahMeta(102, "التكاثر", "At-Takathur", "The Rivalry in world increase", 8, "Meccan", "अत-तकासुर", "दौलत की होड़", "التکاثر"),
        SurahMeta(103, "العصر", "Al-'Asr", "The Declining Day", 3, "Meccan", "अल-असर", "ज़माना / समय", "العصر"),
        SurahMeta(104, "الهمزة", "Al-Humazah", "The Traducer", 9, "Meccan", "अल-हुमज़ह", "दोष निकालने वाला / चुग़लख़ोर", "الہمزہ"),
        SurahMeta(105, "الفيل", "Al-Fil", "The Elephant", 5, "Meccan", "अल-फ़ील", "हाथी", "الفیل"),
        SurahMeta(106, "قريش", "Quraysh", "Quraysh", 4, "Meccan", "क़ुरैश", "क़ुरैश क़बीला", "قریش"),
        SurahMeta(107, "الماعون", "Al-Ma'un", "The Small kindnesses", 7, "Meccan", "अल-माऊन", "दैनिक उपयोग की वस्तुएं", "الماعون"),
        SurahMeta(108, "الكوثر", "Al-Kawthar", "The Abundance", 3, "Meccan", "अल-कौसर", "नेमतों की प्रचुरता / हौज़े कौसर", "الکوثر"),
        SurahMeta(109, "الكافرون", "Al-Kafirun", "The Disbelievers", 6, "Meccan", "अल-काफ़िरून", "इनकार करने वाले / काफ़िर", "الکافرون"),
        SurahMeta(110, "النصر", "An-Nasr", "The Divine Support", 3, "Medinan", "अन-नस्र", "अल्लाह की मदद व विजय", "النصر"),
        SurahMeta(111, "المسد", "Al-Masad", "The Palm Fiber", 5, "Meccan", "अल-मसद", "खजूर की रस्सी", "المسد"),
        SurahMeta(112, "الإخلاص", "Al-Ikhlas", "The Sincerity", 4, "Meccan", "अल-इख़्लास", "विशुद्ध निष्ठा / तौहीद", "الاخلاص"),
        SurahMeta(113, "الفلق", "Al-Falaq", "The Daybreak", 5, "Meccan", "अल-फ़लक़", "प्रभात / सुबह की किरण", "الفلق"),
        SurahMeta(114, "الناس", "An-Nas", "Mankind", 6, "Meccan", "अन-नास", "मानवजाति / लोग", "الناس"),
    )

    val juzList: List<JuzMeta> = listOf(
        JuzMeta(
            juzNumber = 1,
            arabicName = "الم",
            ranges = listOf(
                JuzSurahRange(1, "अल-फ़ातिहा", "1:1 – 1:7"),
                JuzSurahRange(2, "अल-बक़रह", "2:1 – 2:141"),
            )
        ),
        JuzMeta(
            juzNumber = 2,
            arabicName = "سيقول",
            ranges = listOf(
                JuzSurahRange(2, "अल-बक़रह", "2:142 – 2:252"),
            )
        ),
        JuzMeta(
            juzNumber = 3,
            arabicName = "تلك الرسل",
            ranges = listOf(
                JuzSurahRange(2, "अल-बक़रह", "2:253 – 2:286"),
                JuzSurahRange(3, "आल-इमरान", "3:1 – 3:92"),
            )
        ),
        JuzMeta(
            juzNumber = 4,
            arabicName = "لن تنالوا",
            ranges = listOf(
                JuzSurahRange(3, "आल-इमरान", "3:93 – 3:200"),
                JuzSurahRange(4, "अन-निसा", "4:1 – 4:23"),
            )
        ),
        JuzMeta(
            juzNumber = 5,
            arabicName = "والمحصنات",
            ranges = listOf(
                JuzSurahRange(4, "अन-निसा", "4:24 – 4:147"),
            )
        ),
        JuzMeta(
            juzNumber = 6,
            arabicName = "لا يحب الله",
            ranges = listOf(
                JuzSurahRange(4, "अन-निसा", "4:148 – 4:176"),
                JuzSurahRange(5, "अल-माइदा", "5:1 – 5:81"),
            )
        ),
        JuzMeta(
            juzNumber = 7,
            arabicName = "وإذا سمعوا",
            ranges = listOf(
                JuzSurahRange(5, "अल-माइदा", "5:82 – 5:120"),
                JuzSurahRange(6, "अल-अनआम", "6:1 – 6:110"),
            )
        ),
        JuzMeta(
            juzNumber = 8,
            arabicName = "ولو أننا",
            ranges = listOf(
                JuzSurahRange(6, "अल-अनआम", "6:111 – 6:165"),
                JuzSurahRange(7, "अल-आराफ़", "7:1 – 7:87"),
            )
        ),
        JuzMeta(
            juzNumber = 9,
            arabicName = "قال الملأ",
            ranges = listOf(
                JuzSurahRange(7, "अल-आराफ़", "7:88 – 7:206"),
                JuzSurahRange(8, "अल-अनफ़ाल", "8:1 – 8:40"),
            )
        ),
        JuzMeta(
            juzNumber = 10,
            arabicName = "واعلموا",
            ranges = listOf(
                JuzSurahRange(8, "अल-अनफ़ाल", "8:41 – 8:75"),
                JuzSurahRange(9, "अत-तौबह", "9:1 – 9:92"),
            )
        ),
        JuzMeta(
            juzNumber = 11,
            arabicName = "يعتذرون",
            ranges = listOf(
                JuzSurahRange(9, "अत-तौबह", "9:93 – 9:129"),
                JuzSurahRange(10, "यूनुस", "10:1 – 10:109"),
                JuzSurahRange(11, "हूद", "11:1 – 11:5"),
            )
        ),
        JuzMeta(
            juzNumber = 12,
            arabicName = "وما من دابة",
            ranges = listOf(
                JuzSurahRange(11, "हूद", "11:6 – 11:123"),
                JuzSurahRange(12, "यूसुफ़", "12:1 – 12:52"),
            )
        ),
        JuzMeta(
            juzNumber = 13,
            arabicName = "وما أبرئ",
            ranges = listOf(
                JuzSurahRange(12, "यूसुफ़", "12:53 – 12:111"),
                JuzSurahRange(13, "अर-रअद", "13:1 – 13:43"),
                JuzSurahRange(14, "इब्राहीम", "14:1 – 14:52"),
            )
        ),
        JuzMeta(
            juzNumber = 14,
            arabicName = "ربما",
            ranges = listOf(
                JuzSurahRange(15, "अल-हिज्र", "15:1 – 15:99"),
                JuzSurahRange(16, "अन-नह्ल", "16:1 – 16:128"),
            )
        ),
        JuzMeta(
            juzNumber = 15,
            arabicName = "سبحان الذي",
            ranges = listOf(
                JuzSurahRange(17, "अल-इसरा", "17:1 – 17:111"),
                JuzSurahRange(18, "अल-कहफ़", "18:1 – 18:74"),
            )
        ),
        JuzMeta(
            juzNumber = 16,
            arabicName = "قال ألم",
            ranges = listOf(
                JuzSurahRange(18, "अल-कहफ़", "18:75 – 18:110"),
                JuzSurahRange(19, "मरयम", "19:1 – 19:98"),
                JuzSurahRange(20, "ता-हा", "20:1 – 20:135"),
            )
        ),
        JuzMeta(
            juzNumber = 17,
            arabicName = "اقترب للناس",
            ranges = listOf(
                JuzSurahRange(21, "अल-अंबिया", "21:1 – 21:112"),
                JuzSurahRange(22, "अल-हज्ज", "22:1 – 22:78"),
            )
        ),
        JuzMeta(
            juzNumber = 18,
            arabicName = "قد أفلح",
            ranges = listOf(
                JuzSurahRange(23, "अल-मुमिनून", "23:1 – 23:118"),
                JuzSurahRange(24, "अन-नूर", "24:1 – 24:64"),
                JuzSurahRange(25, "अल-फ़ुरक़ान", "25:1 – 25:20"),
            )
        ),
        JuzMeta(
            juzNumber = 19,
            arabicName = "وقال الذين",
            ranges = listOf(
                JuzSurahRange(25, "अल-फ़ुरक़ान", "25:21 – 25:77"),
                JuzSurahRange(26, "अश-शुआरा", "26:1 – 26:227"),
                JuzSurahRange(27, "अन-नम्ल", "27:1 – 27:55"),
            )
        ),
        JuzMeta(
            juzNumber = 20,
            arabicName = "أمن خلق",
            ranges = listOf(
                JuzSurahRange(27, "अन-नम्ल", "27:56 – 27:93"),
                JuzSurahRange(28, "अल-क़सस", "28:1 – 28:88"),
                JuzSurahRange(29, "अल-अनकबूत", "29:1 – 29:45"),
            )
        ),
        JuzMeta(
            juzNumber = 21,
            arabicName = "اتل ما أوحي",
            ranges = listOf(
                JuzSurahRange(29, "अल-अनकबूत", "29:46 – 29:69"),
                JuzSurahRange(30, "अर-रूम", "30:1 – 30:60"),
                JuzSurahRange(31, "लुक़मान", "31:1 – 31:34"),
                JuzSurahRange(32, "अस-सजदा", "32:1 – 32:30"),
                JuzSurahRange(33, "अल-अहज़ाब", "33:1 – 33:30"),
            )
        ),
        JuzMeta(
            juzNumber = 22,
            arabicName = "ومن يقنت",
            ranges = listOf(
                JuzSurahRange(33, "अल-अहज़ाब", "33:31 – 33:73"),
                JuzSurahRange(34, "सबा", "34:1 – 34:54"),
                JuzSurahRange(35, "फ़ातिर", "35:1 – 35:45"),
                JuzSurahRange(36, "या-सीन", "36:1 – 36:27"),
            )
        ),
        JuzMeta(
            juzNumber = 23,
            arabicName = "وما أنزلنا",
            ranges = listOf(
                JuzSurahRange(36, "या-सीन", "36:28 – 36:83"),
                JuzSurahRange(37, "अस-साफ़्फ़ात", "37:1 – 37:182"),
                JuzSurahRange(38, "साद", "38:1 – 38:88"),
                JuzSurahRange(39, "अज़-ज़ुमर", "39:1 – 39:31"),
            )
        ),
        JuzMeta(
            juzNumber = 24,
            arabicName = "فمن أظلم",
            ranges = listOf(
                JuzSurahRange(39, "अज़-ज़ुमर", "39:32 – 39:75"),
                JuzSurahRange(40, "ग़ाफ़िर", "40:1 – 40:85"),
                JuzSurahRange(41, "फ़ुस्सिलत", "41:1 – 41:46"),
            )
        ),
        JuzMeta(
            juzNumber = 25,
            arabicName = "إليه يرد",
            ranges = listOf(
                JuzSurahRange(41, "फ़ुस्सिलत", "41:47 – 41:54"),
                JuzSurahRange(42, "अश-शूरा", "42:1 – 42:53"),
                JuzSurahRange(43, "अज़-ज़ुख़रुफ़", "43:1 – 43:89"),
                JuzSurahRange(44, "अद-दुख़ान", "44:1 – 44:59"),
                JuzSurahRange(45, "अल-जासिया", "45:1 – 45:37"),
            )
        ),
        JuzMeta(
            juzNumber = 26,
            arabicName = "حـم",
            ranges = listOf(
                JuzSurahRange(46, "अल-अहक़ाफ़", "46:1 – 46:35"),
                JuzSurahRange(47, "मुहम्मद", "47:1 – 47:38"),
                JuzSurahRange(48, "अल-फ़तह", "48:1 – 48:29"),
                JuzSurahRange(49, "अल-हुजुरात", "49:1 – 49:18"),
                JuzSurahRange(50, "क़ाफ़", "50:1 – 50:45"),
                JuzSurahRange(51, "अज़-ज़ारियात", "51:1 – 51:30"),
            )
        ),
        JuzMeta(
            juzNumber = 27,
            arabicName = "قال فما خطبكم",
            ranges = listOf(
                JuzSurahRange(51, "अज़-ज़ारियात", "51:31 – 51:60"),
                JuzSurahRange(52, "अत-तूर", "52:1 – 52:49"),
                JuzSurahRange(53, "अन-नज्म", "53:1 – 53:62"),
                JuzSurahRange(54, "अल-क़मर", "54:1 – 54:55"),
                JuzSurahRange(55, "अर-रहमान", "55:1 – 55:78"),
                JuzSurahRange(56, "अल-वाक़िआ", "56:1 – 56:96"),
                JuzSurahRange(57, "अल-हदीद", "57:1 – 57:29"),
            )
        ),
        JuzMeta(
            juzNumber = 28,
            arabicName = "قد سمع الله",
            ranges = listOf(
                JuzSurahRange(58, "अल-मुजादिला", "58:1 – 58:22"),
                JuzSurahRange(59, "अल-हश्र", "59:1 – 59:24"),
                JuzSurahRange(60, "अल-मुमतहना", "60:1 – 60:13"),
                JuzSurahRange(61, "अस-सफ़", "61:1 – 61:14"),
                JuzSurahRange(62, "अल-जुमुआ", "62:1 – 62:11"),
                JuzSurahRange(63, "अल-मुनाफ़िक़ून", "63:1 – 63:11"),
                JuzSurahRange(64, "अत-तग़ाबुन", "64:1 – 64:18"),
                JuzSurahRange(65, "अत-तलाक़", "65:1 – 65:12"),
                JuzSurahRange(66, "अत-तहरीम", "66:1 – 66:12"),
            )
        ),
        JuzMeta(
            juzNumber = 29,
            arabicName = "تبارك الذي",
            ranges = listOf(
                JuzSurahRange(67, "अल-मुल्क", "67:1 – 67:30"),
                JuzSurahRange(68, "अल-क़लम", "68:1 – 68:52"),
                JuzSurahRange(69, "अल-हाक़्क़ा", "69:1 – 69:52"),
                JuzSurahRange(70, "अल-मआरिज", "70:1 – 70:44"),
                JuzSurahRange(71, "नूह", "71:1 – 71:28"),
                JuzSurahRange(72, "अल-जिन्न", "72:1 – 72:28"),
                JuzSurahRange(73, "अल-मुज़्ज़म्मिल", "73:1 – 73:20"),
                JuzSurahRange(74, "अल-मुद्दस्सिर", "74:1 – 74:56"),
                JuzSurahRange(75, "अल-क़ियामा", "75:1 – 75:40"),
                JuzSurahRange(76, "अल-इंसान", "76:1 – 76:31"),
                JuzSurahRange(77, "अल-मुरसलात", "77:1 – 77:50"),
            )
        ),
        JuzMeta(
            juzNumber = 30,
            arabicName = "عم يتساءلون",
            ranges = listOf(
                JuzSurahRange(78, "अन-नबा", "78:1 – 78:40"),
                JuzSurahRange(79, "अन-नाज़िआत", "79:1 – 79:46"),
                JuzSurahRange(80, "अबासा", "80:1 – 80:42"),
                JuzSurahRange(81, "अत-तकवीर", "81:1 – 81:29"),
                JuzSurahRange(82, "अल-इन्फ़ितार", "82:1 – 82:19"),
                JuzSurahRange(83, "अल-मुतफ़्फ़िफ़ीन", "83:1 – 83:36"),
                JuzSurahRange(84, "अल-इन्शिक़ाक़", "84:1 – 84:25"),
                JuzSurahRange(85, "अल-बुरूज", "85:1 – 85:22"),
                JuzSurahRange(86, "अत-तारिक़", "86:1 – 86:17"),
                JuzSurahRange(87, "अल-आला", "87:1 – 87:19"),
                JuzSurahRange(88, "अल-ग़ाशिया", "88:1 – 88:26"),
                JuzSurahRange(89, "अल-फ़ज्र", "89:1 – 89:30"),
                JuzSurahRange(90, "अल-बलद", "90:1 – 90:20"),
                JuzSurahRange(91, "अश-शम्स", "91:1 – 91:15"),
                JuzSurahRange(92, "अल-लैल", "92:1 – 92:21"),
                JuzSurahRange(93, "अद-दुहा", "93:1 – 93:11"),
                JuzSurahRange(94, "अश-शरह", "94:1 – 94:8"),
                JuzSurahRange(95, "अत-तीन", "95:1 – 95:8"),
                JuzSurahRange(96, "अल-अलक़", "96:1 – 96:19"),
                JuzSurahRange(97, "अल-क़द्र", "97:1 – 97:5"),
                JuzSurahRange(98, "अल-बय्यिनाह", "98:1 – 98:8"),
                JuzSurahRange(99, "अज़-ज़लज़ला", "99:1 – 99:8"),
                JuzSurahRange(100, "अल-आदियात", "100:1 – 100:11"),
                JuzSurahRange(101, "अल-क़ारिआ", "101:1 – 101:11"),
                JuzSurahRange(102, "अत-तकासुर", "102:1 – 102:8"),
                JuzSurahRange(103, "अल-असर", "103:1 – 103:3"),
                JuzSurahRange(104, "अल-हुमज़ह", "104:1 – 104:9"),
                JuzSurahRange(105, "अल-फ़ील", "105:1 – 105:5"),
                JuzSurahRange(106, "क़ुरैश", "106:1 – 106:4"),
                JuzSurahRange(107, "अल-माऊन", "107:1 – 107:7"),
                JuzSurahRange(108, "अल-कौसर", "108:1 – 108:3"),
                JuzSurahRange(109, "अल-काफ़िरून", "109:1 – 109:6"),
                JuzSurahRange(110, "अन-नस्र", "110:1 – 110:3"),
                JuzSurahRange(111, "अल-मसद", "111:1 – 111:5"),
                JuzSurahRange(112, "अल-इख़्लास", "112:1 – 112:4"),
                JuzSurahRange(113, "अल-फ़लक़", "113:1 – 113:5"),
                JuzSurahRange(114, "अन-नास", "114:1 – 114:6"),
            )
        ),
    )

    val reciters: List<QuranReciterItem> = listOf(
        QuranReciterItem(
            identifier = "ar.abdullahbasfar",
            englishName = "Abdullah Basfar",
            letter = "A",
            flag = "🇸🇦",
            country = "Saudi Arabia",
            hindiName = "अब्दुल्लाह बसफ़र",
            urduName = "عبد اللہ بصفر",
            arabicName = "عبد الله بصفر",
            bengaliName = "আব্দুল্লাহ বাছফার",
            hindiCountry = "सऊदी अरब",
            urduCountry = "سعودی عرب",
            arabicCountry = "المملكة العربية السعودية",
            bengaliCountry = "সৌদি আরব"
        ),
        QuranReciterItem(
            identifier = "ar.abdurrahmaansudais",
            englishName = "Abdurrahmaan As-Sudais",
            letter = "A",
            flag = "🇸🇦",
            country = "Saudi Arabia",
            hindiName = "अब्दुर्रहमान अस-सुदैस",
            urduName = "عبد الرحمن السدیس",
            arabicName = "عبد الرحمن السديس",
            bengaliName = "আব্দুর রহমান আস-সুদাইস",
            hindiCountry = "सऊदी अरब",
            urduCountry = "سعودی عرب",
            arabicCountry = "المملكة العربية السعودية",
            bengaliCountry = "সৌদি আরব"
        ),
        QuranReciterItem(
            identifier = "ar.abdulsamad",
            englishName = "Abdul Samad",
            letter = "A",
            flag = "🇪🇬",
            country = "Egypt",
            hindiName = "अब्दुल बासित अब्दुल समद",
            urduName = "عبد الباسط عبد الصمد",
            arabicName = "عبد الباسط عبد الصمد",
            bengaliName = "আব্দুল বাসেত আব্দুল সামাদ",
            hindiCountry = "मिस्र",
            urduCountry = "مصر",
            arabicCountry = "جمهورية مصر العربية",
            bengaliCountry = "মিশর"
        ),
        QuranReciterItem(
            identifier = "ar.shaatree",
            englishName = "Abu Bakr Ash-Shaatree",
            letter = "A",
            flag = "🇸🇦",
            country = "Saudi Arabia",
            hindiName = "अबू बक्र अश-शात्री",
            urduName = "ابو بکر الشاطری",
            arabicName = "أبو بكر الشاطري",
            bengaliName = "আবু বকর আশ-শাতরী",
            hindiCountry = "सऊदी अरब",
            urduCountry = "سعودی عرب",
            arabicCountry = "المملكة العربية السعودية",
            bengaliCountry = "সৌদি আরব"
        ),
        QuranReciterItem(
            identifier = "ar.ahmedajamy",
            englishName = "Ahmed ibn Ali al-Ajamy",
            letter = "A",
            flag = "🇸🇦",
            country = "Saudi Arabia",
            hindiName = "अहमद बिन अली अल-अजमी",
            urduName = "احمد بن علی العجمی",
            arabicName = "أحمد بن علي العجمي",
            bengaliName = "আহমেদ বিন আলী আল-আজমী",
            hindiCountry = "सऊदी अरब",
            urduCountry = "سعودی عرب",
            arabicCountry = "المملكة العربية السعودية",
            bengaliCountry = "সৌদি আরব"
        ),
        QuranReciterItem(
            identifier = "ar.alafasy",
            englishName = "Alafasy",
            letter = "A",
            flag = "🇰🇼",
            country = "Kuwait",
            hindiName = "मिशारी राशिद अल-अफ़ासी",
            urduName = "مشاری راشد العفاسی",
            arabicName = "مشاري راشد العفاسي",
            bengaliName = "মিশারী রাশিদ আল-আফাসী",
            hindiCountry = "कुवैत",
            urduCountry = "کویت",
            arabicCountry = "دولة الكويت",
            bengaliCountry = "কুয়েত"
        ),
        QuranReciterItem(
            identifier = "ar.hanirifai",
            englishName = "Hani Rifai",
            letter = "H",
            flag = "🇸🇦",
            country = "Saudi Arabia",
            hindiName = "हानी अर-रिफ़ाई",
            urduName = "ہانی الرفاعی",
            arabicName = "هاني الرفاعي",
            bengaliName = "হানি আর-রিফাই",
            hindiCountry = "सऊदी अरब",
            urduCountry = "سعودی عرب",
            arabicCountry = "المملكة العربية السعودية",
            bengaliCountry = "সৌদি আরব"
        ),
        QuranReciterItem(
            identifier = "ar.husary",
            englishName = "Husary",
            letter = "H",
            flag = "🇪🇬",
            country = "Egypt",
            hindiName = "महमूद ख़लील अल-हुसरी",
            urduName = "محمود خلیل الحصری",
            arabicName = "محمود خليل الحصري",
            bengaliName = "মাহমুদ খলিল আল-হুসারী",
            hindiCountry = "मिस्र",
            urduCountry = "مصر",
            arabicCountry = "جمهورية مصر العربية",
            bengaliCountry = "মিশর"
        ),
        QuranReciterItem(
            identifier = "ar.husarymujawwad",
            englishName = "Husary (Mujawwad)",
            letter = "H",
            flag = "🇪🇬",
            country = "Egypt",
            hindiName = "अल-हुसरी (मुजव्वाद)",
            urduName = "الحصری (مجود)",
            arabicName = "الحصري (مجود)",
            bengaliName = "আল-হুসারী (মুজাওয়াদ)",
            hindiCountry = "मिस्र",
            urduCountry = "مصر",
            arabicCountry = "جمهورية مصر العربية",
            bengaliCountry = "মিশর"
        ),
        QuranReciterItem(
            identifier = "ar.hudhaify",
            englishName = "Hudhaify",
            letter = "H",
            flag = "🇸🇦",
            country = "Saudi Arabia",
            hindiName = "अली अल-हुदैफ़ी",
            urduName = "علی الحذیفی",
            arabicName = "علي الحذيفي",
            bengaliName = "আলী আল-হুজাইফী",
            hindiCountry = "सऊदी अरब",
            urduCountry = "سعودی عرب",
            arabicCountry = "المملكة العربية السعودية",
            bengaliCountry = "সৌদি আরব"
        ),
        QuranReciterItem(
            identifier = "ar.ibrahimakhdar",
            englishName = "Ibrahim Akhdar",
            letter = "I",
            flag = "🇸🇦",
            country = "Saudi Arabia",
            hindiName = "इब्राहिम अल-अख़्दर",
            urduName = "ابراہیم الاخضر",
            arabicName = "إبراهيم الأخضر",
            bengaliName = "ইব্রাহিম আল-আখদার",
            hindiCountry = "सऊदी अरब",
            urduCountry = "سعودی عرب",
            arabicCountry = "المملكة العربية السعودية",
            bengaliCountry = "সৌদি আরব"
        ),
        QuranReciterItem(
            identifier = "ar.mahermuaiqly",
            englishName = "Maher Al Muaiqly",
            letter = "M",
            flag = "🇸🇦",
            country = "Saudi Arabia",
            hindiName = "माहिर अल-मुआइक़ली",
            urduName = "ماہر المعیقلی",
            arabicName = "ماهر المعيقلي",
            bengaliName = "মাহের আল-মুয়াইকলী",
            hindiCountry = "सऊदी अरब",
            urduCountry = "سعودی عرب",
            arabicCountry = "المملكة العربية السعودية",
            bengaliCountry = "সৌদি আরব"
        ),
        QuranReciterItem(
            identifier = "ar.minshawi",
            englishName = "Minshawi",
            letter = "M",
            flag = "🇪🇬",
            country = "Egypt",
            hindiName = "मुहम्मद सिद्दीक़ अल-मिंशावी",
            urduName = "محمد صدیق المنشاوی",
            arabicName = "محمد صديق المنشاوي",
            bengaliName = "মুহাম্মদ সিদ্দিক আল-মিনশাবী",
            hindiCountry = "मिस्र",
            urduCountry = "مصر",
            arabicCountry = "جمهورية مصر العربية",
            bengaliCountry = "মিশর"
        ),
        QuranReciterItem(
            identifier = "ar.muhammadayyoub",
            englishName = "Muhammad Ayyoub",
            letter = "M",
            flag = "🇸🇦",
            country = "Saudi Arabia",
            hindiName = "मुहम्मद अय्यूब",
            urduName = "محمد ایوب",
            arabicName = "محمد أيوب",
            bengaliName = "মুহাম্মদ আইয়ুব",
            hindiCountry = "सऊदी अरब",
            urduCountry = "سعودی عرب",
            arabicCountry = "المملكة العربية السعودية",
            bengaliCountry = "সৌদি আরব"
        ),
        QuranReciterItem(
            identifier = "ar.muhammadjibreel",
            englishName = "Muhammad Jibreel",
            letter = "M",
            flag = "🇪🇬",
            country = "Egypt",
            hindiName = "मुहम्मद जिब्रील",
            urduName = "محمد جبریل",
            arabicName = "محمد جبريل",
            bengaliName = "মুহাম্মদ জিবরীল",
            hindiCountry = "मिस्र",
            urduCountry = "مصر",
            arabicCountry = "جمهورية مصر العربية",
            bengaliCountry = "মিশর"
        ),
        QuranReciterItem(
            identifier = "ar.saoodshuraym",
            englishName = "Saood bin Ibraaheem Ash-Shuraym",
            letter = "S",
            flag = "🇸🇦",
            country = "Saudi Arabia",
            hindiName = "सऊद बिन इब्राहिम अश-शुरैम",
            urduName = "سعود بن ابراہیم الشریم",
            arabicName = "سعود الشريم",
            bengaliName = "সাউদ আশ-শুরাইম",
            hindiCountry = "सऊदी अरब",
            urduCountry = "سعودی عرب",
            arabicCountry = "المملكة العربية السعودية",
            bengaliCountry = "সৌদি আরব"
        )
    )

    private val surahStartPages = intArrayOf(
        0,
        1, 2, 50, 77, 106, 128, 151, 177, 187, 208,
        221, 235, 249, 255, 262, 267, 282, 293, 305, 312,
        322, 332, 342, 350, 359, 367, 377, 385, 396, 404,
        411, 415, 418, 428, 434, 440, 446, 453, 458, 467,
        477, 483, 489, 496, 499, 502, 507, 511, 515, 518,
        520, 523, 526, 528, 531, 534, 537, 542, 545, 549,
        551, 553, 554, 556, 558, 560, 562, 564, 566, 568,
        570, 572, 574, 575, 577, 578, 580, 582, 583, 585,
        586, 587, 587, 589, 590, 591, 591, 592, 593, 594,
        595, 595, 596, 596, 597, 597, 598, 598, 599, 599,
        600, 600, 601, 601, 601, 602, 602, 602, 603, 603,
        603, 604, 604, 604
    )

    fun getSurahStartPage(surahNum: Int): Int {
        return if (surahNum in 1..114) surahStartPages[surahNum] else 1
    }
}
