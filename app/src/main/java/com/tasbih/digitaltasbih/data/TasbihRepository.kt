package com.tasbih.digitaltasbih.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * TasbihRepository - Master data & persistence engine for Tasbih Counter.
 * Handles core dhikrs, custom user dhikrs, dynamic Firebase Admin extra dhikrs,
 * builtin & remote bead marble skins, Darood Sharif collection, 99 Names of Allah,
 * history records, streak milestones, and live synchronization with Dhikr Tracker.
 */
object TasbihRepository {
    private const val PREFS_NAME = "tasbih_counter_prefs"
    private const val FIREBASE_RTDB_URL = "https://digital-tasbih-19bca-default-rtdb.asia-southeast1.firebasedatabase.app"
    private var prefs: SharedPreferences? = null

    fun getTodayKey(): String {
        val cal = Calendar.getInstance()
        return String.format(
            Locale.US,
            "%04d-%02d-%02d",
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) + 1,
            cal.get(Calendar.DAY_OF_MONTH)
        )
    }
    private val scope = CoroutineScope(Dispatchers.IO)
    private val inMemorySessionCounts = java.util.concurrent.ConcurrentHashMap<String, Int>()

    val DEFAULT_DHIKR_LIST = listOf(
        TasbihDhikrItem(
            id = "subhan",
            arabic = "سُبْحَانَ اللّٰہ",
            localizedNames = mapOf("hi" to "सुब्हान अल्लाह", "en" to "SubhanAllah", "ur" to "سبحان اللہ", "ar" to "سبحان الله", "bn" to "সুবহানআল্লাহ"),
            localizedMeanings = mapOf("hi" to "अल्लाह पाक है", "en" to "Glory be to Allah", "ur" to "اللہ پاک ہے", "ar" to "سبحانه وتعالى", "bn" to "আল্লাহ পবিত্র"),
            icon = "✨",
            defaultTarget = 33,
            order = 1
        ),
        TasbihDhikrItem(
            id = "alhamd",
            arabic = "اَلْحَمْدُ لِلّٰہ",
            localizedNames = mapOf("hi" to "अलहम्दुलिल्लाह", "en" to "Alhamdulillah", "ur" to "الحمداللہ", "ar" to "الحمد لله", "bn" to "আলহামদুলিল্লাহ"),
            localizedMeanings = mapOf("hi" to "सब तारीफ अल्लाह को", "en" to "All praise is for Allah", "ur" to "تمام تعریف اللہ کو", "ar" to "كل الحمد لله", "bn" to "সব প্রশংসা আল্লাহর"),
            icon = "🌼",
            defaultTarget = 33,
            order = 2
        ),
        TasbihDhikrItem(
            id = "akbar",
            arabic = "اَللّٰہُ اَکْبَر",
            localizedNames = mapOf("hi" to "अल्लाहु अकबर", "en" to "Allahu Akbar", "ur" to "اللہ اکبر", "ar" to "الله أكبر", "bn" to "আল্লাহু আকবার"),
            localizedMeanings = mapOf("hi" to "अल्लाह सबसे बड़ा है", "en" to "Allah is the Greatest", "ur" to "اللہ سب سے بڑا", "ar" to "الله أعظم من كل شيء", "bn" to "আল্লাহ সর্বমহান"),
            icon = "🌙",
            defaultTarget = 33,
            order = 3
        ),
        TasbihDhikrItem(
            id = "lailaha",
            arabic = "لَا إِلٰهَ إِلَّا اللّٰهُ مُحَمَّدٌ رَسُوْلُ اللّٰهِ",
            localizedNames = mapOf("hi" to "ला इलाहा इल्लल्लाह मुहम्मदुर रसूलुल्लाह", "en" to "La ilaha illallah muhammadur rasulullah", "ur" to "لا الہ الا اللہ محمد رسول اللہ", "ar" to "لا إله إلا الله محمد رسول الله", "bn" to "লা ইলাহা ইল্লাল্লাহু মুহাম্মাদুর রাসুলুল্লাহ"),
            localizedMeanings = mapOf("hi" to "अल्लाह के सिवा कोई माबूद नहीं, मुहम्मद ﷺ अल्लाह के रसूल हैं", "en" to "There is no god but Allah, Muhammad is the Messenger of Allah", "ur" to "اللہ کے سوا کوئی معبود نہیں، محمد ﷺ اللہ کے رسول ہیں", "ar" to "لا معبود حق إلا الله، محمد رسول الله", "bn" to "আল্লাহ ছাড়া কোন উপাস্য নেই, মুহাম্মদ আল্লাহর রাসুল"),
            icon = "☀️",
            defaultTarget = 100,
            order = 4
        ),
        TasbihDhikrItem(
            id = "istgh",
            arabic = "اَسْتَغْفِرُ اللّٰہ",
            localizedNames = mapOf("hi" to "अस्तग़फिरुल्लाह", "en" to "Astaghfirullah", "ur" to "استغفراللہ", "ar" to "أستغفر الله", "bn" to "আস্তাগফিরুল্লাহ"),
            localizedMeanings = mapOf("hi" to "अल्लाह से माफी माँगता हूँ", "en" to "I seek forgiveness from Allah", "ur" to "اللہ سے معافی مانگتا ہوں", "ar" to "أطلب المغفرة من الله", "bn" to "ক্ষমা প্রার্থনা করি"),
            icon = "🤲",
            defaultTarget = 100,
            order = 5
        ),
        TasbihDhikrItem(
            id = "durood",
            arabic = "صَلَّى اللّٰہُ عَلَيْہِ وَسَلَّمَ",
            localizedNames = mapOf("hi" to "दरूद शरीफ", "en" to "Durood Shareef", "ur" to "درود شریف", "ar" to "الصلاة على النبي", "bn" to "দরূদ শরীফ"),
            localizedMeanings = mapOf("hi" to "नबी ﷺ पर रहमत", "en" to "Blessings on Prophet ﷺ", "ur" to "نبی ﷺ پر رحمت", "ar" to "صلى الله عليه وسلم", "bn" to "নবী ﷺ এর উপর রহমত"),
            icon = "💚",
            defaultTarget = 100,
            order = 6
        ),
        TasbihDhikrItem(
            id = "hawla",
            arabic = "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللّٰہِ",
            localizedNames = mapOf("hi" to "ला हौल वला कूव्वता", "en" to "La Hawla Wala Quwwata", "ur" to "لا حول ولا قوۃ", "ar" to "لا حول ولا قوة إلا بالله", "bn" to "লা হাওলা ওয়ালা কুওয়াতা"),
            localizedMeanings = mapOf("hi" to "ताकत सिर्फ अल्लाह से है", "en" to "No power except with Allah", "ur" to "طاقت صرف اللہ سے", "ar" to "لا قدرة إلا بالله", "bn" to "আল্লাহ ছাড়া কোন শক্তি নেই"),
            icon = "🛡️",
            defaultTarget = 99,
            order = 7
        ),
        TasbihDhikrItem(
            id = "hasby",
            arabic = "حَسْبِيَ اللّٰہُ لَا إِلٰهَ إِلَّا هُوَ",
            localizedNames = mapOf("hi" to "हस्बियल्लाह", "en" to "Hasbiyallah", "ur" to "حسبی اللہ", "ar" to "حسبي الله لا إله إلا هو", "bn" to "হাসবিয়াল্লাহ"),
            localizedMeanings = mapOf("hi" to "मेरे लिए अल्लाह काफी है", "en" to "Allah is sufficient for me", "ur" to "اللہ میرے لیے کافی", "ar" to "الله يكفيني", "bn" to "আল্লাহই আমার জন্য যথেষ্ট"),
            icon = "⭐",
            defaultTarget = 7,
            order = 8
        ),
        TasbihDhikrItem(
            id = "tahmid",
            arabic = "سُبْحَانَ اللّٰہِ وَبِحَمْدِهٖ",
            localizedNames = mapOf("hi" to "सुब्हानल्लाहि वबिहम्दिही", "en" to "SubhanAllahi wa Bihamdihi", "ur" to "سبحان اللہ و بحمدہ", "ar" to "سبحان الله وبحمده", "bn" to "সুবহানাল্লাহি ওয়া বিহামদিহি"),
            localizedMeanings = mapOf("hi" to "अल्लाह पाक और उसी की तारीफ", "en" to "Glory and Praise be to Allah", "ur" to "اللہ پاک اور اسی کی تعریف", "ar" to "سبحانه وبحمده", "bn" to "আল্লাহর পবিত্রতা ও প্রশংসা"),
            icon = "🌸",
            defaultTarget = 100,
            order = 9
        ),
        TasbihDhikrItem(
            id = "azeem",
            arabic = "سُبْحَانَ اللّٰہِ الْعَظِيْم",
            localizedNames = mapOf("hi" to "सुब्हानल्लाहिल अज़ीम", "en" to "SubhanAllahil Azim", "ur" to "سبحان اللہ العظیم", "ar" to "سبحان الله العظيم", "bn" to "সুবহানাল্লাহিল আজিম"),
            localizedMeanings = mapOf("hi" to "अज़ीम अल्लाह की पवित्रता", "en" to "Glory to Allah the Magnificent", "ur" to "عظیم اللہ کی پاکیزگی", "ar" to "سبحان العظيم", "bn" to "মহান আল্লাহর পবিত্রতা"),
            icon = "🌠",
            defaultTarget = 100,
            order = 10
        ),
        TasbihDhikrItem(
            id = "kalimat",
            arabic = "لَا إِلٰهَ إِلَّا اللّٰہُ وَحْدَهٗ لَا شَرِيْكَ لَهٗ",
            localizedNames = mapOf("hi" to "वहदहू ला शरीका लहू", "en" to "Wahdahu La Sharika Lah", "ur" to "وحدہ لا شریک", "ar" to "وحده لا شريك له", "bn" to "ওয়াহদাহু লা শরীকা লাহু"),
            localizedMeanings = mapOf("hi" to "वह अकेला, कोई शरीक नहीं", "en" to "He alone, no partner for Him", "ur" to "وہ اکیلا، کوئی شریک نہیں", "ar" to "لا شريك له", "bn" to "তিনি একা, কোন শরিক নেই"),
            icon = "☝️",
            defaultTarget = 100,
            order = 11
        ),
        TasbihDhikrItem(
            id = "salawat",
            arabic = "اَللّٰہُمَّ صَلِّ عَلٰى مُحَمَّدٍ",
            localizedNames = mapOf("hi" to "अल्लाहुम्मा सल्लि", "en" to "Allahumma Salli ala Muhammad", "ur" to "اللہم صل علی محمد", "ar" to "اللهم صل على محمد", "bn" to "আল্লাহুম্মা সাল্লি আলা মুহাম্মদ"),
            localizedMeanings = mapOf("hi" to "नबी ﷺ पर अल्लाह की रहमत", "en" to "O Allah! Bless Muhammad ﷺ", "ur" to "اے اللہ محمد ﷺ پر رحمت", "ar" to "صل وبارك على نبينا محمد", "bn" to "হে আল্লাহ মুহাম্মদ ﷺ এ রহমত দাও"),
            icon = "💛",
            defaultTarget = 100,
            order = 12
        ),
        TasbihDhikrItem(
            id = "tawbah",
            arabic = "رَبِّ اغْفِرْ لِي وَتُبْ عَلَيَّ",
            localizedNames = mapOf("hi" to "रब्बिग्फिर ली", "en" to "Rabbighfir li wa tub alayya", "ur" to "رب مجھے معاف کر", "ar" to "رب اغفر لي وتب علي", "bn" to "রাব্বিগফির লি ওয়া তুব আলাইয়া"),
            localizedMeanings = mapOf("hi" to "ऐ रब माफ कर और तौबा कबूल कर", "en" to "Lord, forgive me and accept repentance", "ur" to "اے رب معاف اور توبہ قبول کر", "ar" to "رب اغفر لي", "bn" to "হে রব ক্ষমা করো ও তওবা কবুল করো"),
            icon = "🤲",
            defaultTarget = 100,
            order = 13
        ),
        TasbihDhikrItem(
            id = "qadr",
            arabic = "اَللّٰہُمَّ اِنَّكَ عَفُوٌّ تُحِبُّ الْعَفْوَ فَاعْفُ عَنِّيْ",
            localizedNames = mapOf("hi" to "दुआ-ए-क़द्र", "en" to "Dua of Laylatul Qadr", "ur" to "دعائے قدر", "ar" to "دعاء ليلة القدر", "bn" to "কদরের দুআ"),
            localizedMeanings = mapOf("hi" to "ऐ अल्लाह तू माफ करने वाला है", "en" to "O Allah, You are Forgiving", "ur" to "اے اللہ تو معاف کرنے والا", "ar" to "اللهم إنك تحب العفو", "bn" to "হে আল্লাহ তুমি ক্ষমাকারী"),
            icon = "🌙",
            defaultTarget = 33,
            order = 14
        ),
        TasbihDhikrItem(
            id = "yunus",
            arabic = "لَا إِلٰهَ إِلَّا أَنْتَ سُبْحَانَكَ إِنِّيْ كُنْتُ مِنَ الظَّالِمِيْن",
            localizedNames = mapOf("hi" to "दुआ-ए-यूनुस", "en" to "Dua of Yunus (A.S.)", "ur" to "دعائے یونس", "ar" to "دعاء يونس", "bn" to "ইউনুসের দুআ"),
            localizedMeanings = mapOf("hi" to "ऐ अल्लाह! मैंने ज़ुल्म किया", "en" to "O Allah, I was among the wrongdoers", "ur" to "اے اللہ! میں نے ظلم کیا", "ar" to "سبحانك إني كنت من الظالمين", "bn" to "হে আল্লাহ আমি গুনাহগার"),
            icon = "🐋",
            defaultTarget = 40,
            order = 15
        ),
        TasbihDhikrItem(
            id = "tawakkul",
            arabic = "تَوَكَّلْتُ عَلَى اللّٰہِ",
            localizedNames = mapOf("hi" to "तवक्कलतु अलल्लाह", "en" to "Tawakkaltu Alallah", "ur" to "توکلت علی اللہ", "ar" to "توكلت على الله", "bn" to "তাওয়াক্কালতু আলাল্লাহ"),
            localizedMeanings = mapOf("hi" to "मैंने अल्लाह पर भरोसा किया", "en" to "I put my trust in Allah", "ur" to "اللہ پر بھروسہ کیا", "ar" to "اعتمدت على الله", "bn" to "আল্লাহর উপর ভরসা করলাম"),
            icon = "🏔️",
            defaultTarget = 7,
            order = 16
        ),
        TasbihDhikrItem(
            id = "shukr",
            arabic = "اَلْحَمْدُ لِلّٰہِ عَلٰى كُلِّ حَال",
            localizedNames = mapOf("hi" to "हर हाल में शुक्र", "en" to "Alhamdulillah Ala Kulli Hal", "ur" to "ہر حال میں الحمد", "ar" to "الحمد لله على كل حال", "bn" to "সর্বাবস্থায় আলহামদুলিল্লাহ"),
            localizedMeanings = mapOf("hi" to "हर हाल में अल्लाह की तारीफ", "en" to "Praise Allah in every condition", "ur" to "ہر حال میں اللہ کی تعریف", "ar" to "الحمد لله في كل حال", "bn" to "সকল অবস্থায় আল্লাহর প্রশংসা"),
            icon = "🙏",
            defaultTarget = 33,
            order = 17
        ),
        TasbihDhikrItem(
            id = "fatiha",
            arabic = "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الْآخِرَةِ حَسَنَةً",
            localizedNames = mapOf("hi" to "रब्बना आतिना", "en" to "Rabbana Atina fid Dunya", "ur" to "ربنا آتنا", "ar" to "ربنا آتنا في الدنيا حسنة", "bn" to "রাব্বানা আতিনা"),
            localizedMeanings = mapOf("hi" to "दुनिया और आखिरत में भलाई दे", "en" to "Give us good in both worlds", "ur" to "دنیا اور آخرت میں بھلائی", "ar" to "ربنا هب لنا خيري الدارين", "bn" to "দুনিয়া ও আখিরাতে ভালো দাও"),
            icon = "🌍",
            defaultTarget = 3,
            order = 18
        ),
        TasbihDhikrItem(
            id = "quds",
            arabic = "سُبُّوْحٌ قُدُّوْسٌ رَبُّنَا وَرَبُّ الْمَلَائِكَةِ",
            localizedNames = mapOf("hi" to "सुब्बूहुन कुद्दूसुन", "en" to "Subbuhun Quddusun", "ur" to "سبوح قدوس", "ar" to "سبوح قدوس", "bn" to "সুব্বুহুন কুদ্দুসুন"),
            localizedMeanings = mapOf("hi" to "पाक है हमारा रब और फरिश्तों का रब", "en" to "Most Holy, Lord of Angels", "ur" to "پاک ہے ہمارا رب اور فرشتوں کا رب", "ar" to "ربنا القدوس رب الملائكة", "bn" to "পবিত্র আমাদের রব ও ফেরেশতাদের রব"),
            icon = "👼",
            defaultTarget = 33,
            order = 19
        ),
        TasbihDhikrItem(
            id = "basmala",
            arabic = "بِسْمِ اللّٰہِ الرَّحْمٰنِ الرَّحِيْم",
            localizedNames = mapOf("hi" to "बिस्मिल्लाह", "en" to "Bismillah", "ur" to "بسم اللہ", "ar" to "بسم الله الرحمن الرحيم", "bn" to "বিসমিল্লাহ"),
            localizedMeanings = mapOf("hi" to "अल्लाह के नाम से शुरू", "en" to "In the name of Allah", "ur" to "اللہ کے نام سے", "ar" to "باسم الله الرحمن الرحيم", "bn" to "আল্লাহর নামে শুরু"),
            icon = "📖",
            defaultTarget = 99,
            order = 20
        )
    )
    // ══════════════════════════════════════════════════════════════
    //  2. 13 BUILTIN BEAD MARBLE MATERIALS
    // ══════════════════════════════════════════════════════════════
    val BUILTIN_BEAD_MARBLES = listOf(
        BeadMarbleSkin("wood", "Wood", "wood", "#8a5a34", "#eccfa3", "#3a2412", "free"),
        BeadMarbleSkin("onyx", "Black Onyx", "stone", "#222222", "#8a8a8a", "#000000", "free"),
        BeadMarbleSkin("jade", "Jade", "stone", "#2f9e6e", "#bdf7dd", "#0c3b27", "free"),
        BeadMarbleSkin("amethyst", "Amethyst", "stone", "#8e3fd0", "#e9d2ff", "#391a54", "free"),
        BeadMarbleSkin("marble", "Marble", "stone", "#d6d2c6", "#ffffff", "#8a8578", "free"),
        BeadMarbleSkin("emerald", "Emerald", "stone", "#1f9d5c", "#aeffd2", "#0a3d24", "free"),
        BeadMarbleSkin("crystal", "Crystal", "glass", "#5fb3e0", "#ffffff", "#1d4f6b", "free"),
        BeadMarbleSkin("pearl", "Pearl", "stone", "#ede6d3", "#ffffff", "#b8ac8e", "free"),
        BeadMarbleSkin("ruby", "Ruby", "stone", "#c81d3f", "#ffbdca", "#4a0a18", "free"),
        BeadMarbleSkin("sapphire", "Sapphire", "stone", "#1d4fc8", "#bcd7ff", "#0a1f52", "free"),
        BeadMarbleSkin("gold", "Gold", "metal", "#d4a017", "#fff3c4", "#5c4408", "free"),
        BeadMarbleSkin("silver", "Silver", "metal", "#b8b8b8", "#ffffff", "#5a5a5a", "free"),
        BeadMarbleSkin("turquoise", "Turquoise", "stone", "#1fa89d", "#bdfff2", "#0a423d", "free"),
        // Floral Glass Collection (Matching Image 2)
        BeadMarbleSkin("floral_01", "Floral Glass 1", "glass", "#8a5a34", "#eccfa3", "#3a2412", "premium", "https://digital-tasbih.abdulquadirqureshi.workers.dev/images/marbles/floral_01-1788155917991.jpg"),
        BeadMarbleSkin("floral_02", "Floral Glass 2", "glass", "#8a5a34", "#eccfa3", "#3a2412", "premium", "https://digital-tasbih.abdulquadirqureshi.workers.dev/images/marbles/floral_02-1788192807699.jpg"),
        BeadMarbleSkin("floral_03", "Floral Glass 3", "glass", "#8a5a34", "#eccfa3", "#3a2412", "premium", "https://digital-tasbih.abdulquadirqureshi.workers.dev/images/marbles/floral_03-1788252071374.jpg"),
        BeadMarbleSkin("floral_04", "Floral Glass 4", "glass", "#8a5a34", "#eccfa3", "#3a2412", "premium", "https://digital-tasbih.abdulquadirqureshi.workers.dev/images/marbles/floral_04-1788252083849.jpg"),
        BeadMarbleSkin("floral_05", "Floral Glass 5", "glass", "#8a5a34", "#eccfa3", "#3a2412", "premium", "https://digital-tasbih.abdulquadirqureshi.workers.dev/images/marbles/floral_05-1788252255454.jpg"),
        BeadMarbleSkin("floral_06", "Floral Glass 6", "glass", "#8a5a34", "#eccfa3", "#3a2412", "premium", "https://digital-tasbih.abdulquadirqureshi.workers.dev/images/marbles/floral_06-1788252270024.jpg"),
        BeadMarbleSkin("floral_07", "Floral Glass 7", "glass", "#8a5a34", "#eccfa3", "#3a2412", "premium", "https://digital-tasbih.abdulquadirqureshi.workers.dev/images/marbles/floral_07-1788257355947.jpg"),
        BeadMarbleSkin("floral_08", "Floral Glass 8", "glass", "#8a5a34", "#eccfa3", "#3a2412", "premium", "https://digital-tasbih.abdulquadirqureshi.workers.dev/images/marbles/floral_08-1788257378568.jpg"),
        BeadMarbleSkin("floral_09", "Floral Glass 9", "glass", "#8a5a34", "#eccfa3", "#3a2412", "premium", "https://digital-tasbih.abdulquadirqureshi.workers.dev/images/marbles/floral_09-1788258962685.jpg"),
        BeadMarbleSkin("floral_10", "Floral Glass 10", "glass", "#8a5a34", "#eccfa3", "#3a2412", "premium", "https://digital-tasbih.abdulquadirqureshi.workers.dev/images/marbles/floral_10-1788258972181.jpg"),
        BeadMarbleSkin("floral_11", "Floral Glass 11", "glass", "#8a5a34", "#eccfa3", "#3a2412", "premium", "https://digital-tasbih.abdulquadirqureshi.workers.dev/images/marbles/floral_11-1788258982631.jpg"),
        BeadMarbleSkin("floral_12", "Floral Glass 12", "glass", "#8a5a34", "#eccfa3", "#3a2412", "premium", "https://digital-tasbih.abdulquadirqureshi.workers.dev/images/marbles/floral_12-1788258994297.jpg")
    )

    // ══════════════════════════════════════════════════════════════
    //  3. DAROOD SHARIF COLLECTION
    // ══════════════════════════════════════════════════════════════
    val DAROOD_LIST = listOf(
        DaroodGuideItem(
            id = "d_ibrahimi",
            title = mapOf(
                "en" to "Durood-e-Ibrahimi",
                "hi" to "दरूद-ए-इब्राहिमी",
                "ur" to "درود ابراہیمی",
                "ar" to "الصلاة الإبراهيمية",
                "bn" to "দরূদে ইবরাহিমি"
            ),
            arabic = "اللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ وَعَلَى آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلَى إِبْرَاهِيمَ وَعَلَى آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ ، اللَّهُمَّ بَارِكْ عَلَى مُحَمَّدٍ وَعَلَى آلِ مُحَمَّدٍ كَمَا بَارَكْتَ عَلَى إِبْرَاهِيمَ وَعَلَى آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ",
            transliteration = mapOf(
                "en" to "Allahumma salli ala Muhammadin wa ala aali Muhammadin kama sallaita ala Ibrahima wa ala aali Ibrahima innaka Hamidum Majid. Allahumma barik ala Muhammadin wa ala aali Muhammadin kama barakta ala Ibrahima wa ala aali Ibrahima innaka Hamidum Majid.",
                "hi" to "अल्लाहुम्मा सल्लि अला मुहम्मदिंव-व अला आलि मुहम्मदिन कमा सल्लैता अला इब्राहीमा व अला आलि इब्राहीमा इन्नका हमीदुम-मजीद। अल्लाहुम्मा बारिक अला मुहम्मदिंव-व अला आलि मुहम्मदिन कमा बारक्ता अला इब्राहीमा व अला आलि इब्राहीमा इन्नका हमीदुम-मजीद।",
                "ur" to "اللّٰهُمَّ صَلِّ عَلٰى مُحَمَّدٍ وَّعَلٰى آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلٰى إِبْرَاهِيْمَ وَعَلٰى آلِ إِبْرَاهِيْمَ إِنَّكَ حَمِيْدٌ مَّجِيْدٌ، اللّٰهُمَّ بَارِكْ عَلٰى مُحَمَّدٍ وَّعَلٰى آلِ مُحَمَّدٍ كَمَا بَارَكْتَ عَلٰى إِبْرَاهِيْمَ وَعَلٰى آلِ إِبْرَاهِيْمَ إِنَّكَ حَمِيْدٌ مَّجِيْدٌ"
            ),
            translation = mapOf(
                "en" to "O Allah, send blessings upon Muhammad and the family of Muhammad, as You sent blessings upon Ibrahim and the family of Ibrahim. Indeed, You are Praiseworthy and Glorious. O Allah, bless Muhammad and the family of Muhammad, as You blessed Ibrahim and the family of Ibrahim. Indeed, You are Praiseworthy and Glorious.",
                "hi" to "ऐ अल्लाह! हज़रत मुहम्मद ﷺ और उनकी आल पर रहमत नाज़िल फ़रमा जैसा कि तूने हज़रत इब्राहीम (अ.स.) और उनकी आल पर रहमत नाज़िल फ़रमाई, बेशक तू क़ाबिले तारीफ़ और बुज़ुर्ग है। ऐ अल्लाह! बरकत नाज़िल फ़रमा हज़रत मुहम्मद ﷺ और उनकी आल पर जैसा कि तूने बरकत नाज़िल फ़रमाई हज़रत इब्राहीम (अ.स.) और उनकी आल पर, बेशक तू क़ाबिले तारीफ़ और बुज़ुर्ग है।",
                "ur" to "اے اللہ! رحمت نازل فرما محمد پر اور آلِ محمد پر جیسا کہ تو نے رحمت نازل فرمائی آلِ ابراہیم پر، بیشک تو تعریف کیا گیا اور بزرگی والا ہے۔ اے اللہ! برکت نازل فرما محمد پر اور آلِ محمد پر جیسا کہ تو نے برکت نازل فرمائی آلِ ابراہیم پر، بیشک تو تعریف کیا گیا اور بزرگی والا ہے۔",
                "ar" to "اللهم صل على محمد وعلى آل محمد كما صليت على آل إبراهيم إنك حميد مجيد، اللهم بارك على محمد وعلى آل محمد كما باركت على آل إبراهيم إنك حميد مجيد.",
                "bn" to "হে আল্লাহ! মুহাম্মদ ও তাঁর পরিবারের প্রতি রহমত বর্ষণ করুন, যেমন আপনি ইবরাহিম ও তাঁর পরিবারের প্রতি রহমত বর্ষণ করেছিলেন। নিশ্চয়ই আপনি প্রশংসিত ও মহিমান্বিত।"
            ),
            benefits = mapOf(
                "en" to "Whoever sends blessings upon the Prophet ﷺ with Durood-e-Ibrahimi attains divine mercy and the Prophet's intercession on the Day of Judgment.",
                "hi" to "नमाज़ के क़ाअदा में पढ़ा जाने वाला सबसे मुकम्मल दरूद — शफ़ाअत का ज़रिया और अनगिनत रहमतों का सबब।",
                "ur" to "نماز کے قعدہ میں پڑھا جانے والا سب سے افضل درود — شفاعت نبوی ﷺ کا سب سے بڑا ذریعہ۔"
            ),
            hadithRef = "Sahih al-Bukhari 3370 & Sahih Muslim 405",
            defaultTarget = 33,
            hadithArabic = "قِيلَ: يَا رَسُولَ اللَّهِ، أَمَّا السَّلَامُ عَلَيْكَ فَقَدْ عَرَفْنَاهُ، فَكَيْفَ الصَّلَاةُ عَلَيْكَ؟ قَالَ: «قُولُوا: اللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ، وَعَلَى آلِ مُحَمَّدٍ، كَمَا صَلَّيْتَ عَلَى آلِ إِبْرَاهِيمَ، إِنَّكَ حَمِيدٌ مَجِيدٌ، اللَّهُمَّ بَارِكْ عَلَى مُحَمَّدٍ، وَعَلَى آلِ مُحَمَّدٍ، كَمَا بَارَكْتَ عَلَى آلِ إِبْرَاهِيمَ، إِنَّكَ حَمِيدٌ مَجِيدٌ»",
            hadithTranslations = mapOf(
                "en" to "The companions asked: 'O Messenger of Allah! We already know how to greet you with peace, but how should we send blessings upon you?' He said: 'Say: O Allah, send blessings upon Muhammad and the family of Muhammad, as You sent blessings upon the family of Ibrahim. Indeed, You are Praiseworthy and Glorious. O Allah, bless Muhammad and the family of Muhammad, as You blessed the family of Ibrahim. Indeed, You are Praiseworthy and Glorious.'",
                "hi" to "सहाबा किराम ने अर्ज़ किया: 'या रसूलल्लाह! आप पर सलाम भेजना तो हम जान चुके, लेकिन आप पर दरूद कैसे भेजें?' आप ﷺ ने फ़रमाया: 'कहो: ऐ अल्लाह! रहमत नाज़िल फ़रमा मुहम्मद ﷺ पर और उनकी आल पर, जैसा कि तूने रहमत नाज़िल फ़रमाई इब्राहीम (अ.स.) की आल पर, बेशक तू क़ाबिले तारीफ़ और बुज़ुर्ग है। ऐ अल्लाह! बरकत नाज़िल फ़रमा मुहम्मद ﷺ पर और उनकी आल पर, जैसा कि तूने बरकत नाज़िल फ़रमाई इब्राहीम (अ.स.) की आल पर, बेशक तू क़ाबिले तारीफ़ और बुज़ुर्ग है।' (सहीह बुख़ारी: 3370)",
                "ur" to "صحابہ نے عرض کیا: یا رسول اللہ! آپ پر سلام کیسے بھیجا جائے یہ تو ہم نے جان لیا، لیکن آپ پر درود کس طرح بھیجیں؟ آپ ﷺ نے فرمایا: کہو: اے اللہ! رحمت نازل فرما محمد پر اور آلِ محمد پر جیسا کہ تو نے رحمت نازل فرمائی آلِ ابراہیم پر، بیشک تو تعریف کیا گیا اور بزرگی والا ہے۔ اے اللہ! برکت نازل فرما محمد پر اور آلِ محمد پر جیسا کہ تو نے برکت نازل فرمائی آلِ ابراہیم پر، بیشک تو تعریف کیا گیا اور بزرگی والا ہے۔ (صحیح بخاری: 3370)",
                "ar" to "قيل: يا رسول الله، أما السلام عليك فقد عرفناه، فكيف الصلاة عليك؟ قال: قولوا: اللهم صل على محمد وعلى آل محمد كما صليت على آل إبراهيم إنك حميد مجيد، اللهم بارك على محمد وعلى آل محمد كما باركت على آل إبراهيم إنك حميد مجيد.",
                "bn" to "সাহাবীগণ আরজ করলেন: হে আল্লাহর রাসূল! আপনার প্রতি সালাম কিভাবে জানাতে হয় তা তো আমরা জেনেছি, কিন্তু আপনার প্রতি দরূদ কিভাবে পেশ করব? তিনি বললেন: তোমরা বলো: হে আল্লাহ! মুহাম্মদ ও তাঁর পরিবারের প্রতি রহমত বর্ষণ করুন, যেমন আপনি ইবরাহিম ও তাঁর পরিবারের প্রতি রহমত বর্ষণ করেছিলেন..."
            ),
            hadithNarrator = mapOf(
                "en" to "Ka'b bin 'Ujrah (RA)",
                "hi" to "हज़रत का'ब बिन उजरा (र.अ.)",
                "ur" to "حضرت کعب بن عجرہ رضی اللہ عنہ",
                "ar" to "كعب بن عجرة رضي الله عنه",
                "bn" to "কাব বিন উজরাহ (রা.)"
            ),
            hadithBook = "Sahih al-Bukhari 3370",
            hadithGrade = "Sahih",
            hadithUrl = "https://sunnah.com/bukhari:3370"
        ),
        DaroodGuideItem(
            id = "d_salaam",
            title = mapOf(
                "en" to "Durood-e-Salaam (Shortest Salawat)",
                "hi" to "दरूद-ए-सलाम",
                "ur" to "درود سلام",
                "ar" to "الصلاة والسلام المختصرة",
                "bn" to "দরূদে সালাম"
            ),
            arabic = "صَلَّى اللّٰہُ عَلَيْهِ وَسَلَّمَ",
            transliteration = mapOf(
                "en" to "Sallallahu Alayhi Wasallam",
                "hi" to "सल्लल्लाहु अलैहि वसल्लम",
                "ur" to "صلی اللہ علیہ وسلم"
            ),
            translation = mapOf(
                "en" to "May the peace and blessings of Allah be upon him.",
                "hi" to "अल्लाह की रहमत और सलामती हो उन पर।",
                "ur" to "اللہ تعالیٰ کی رحمتیں اور سلامتی ہو ان پر۔",
                "ar" to "صلى الله عليه وسلم وبارك عليه.",
                "bn" to "তাঁর প্রতি আল্লাহর শান্তি ও রহমত বর্ষিত হোক।"
            ),
            benefits = mapOf(
                "en" to "The easiest and most beloved salawat to recite continuously throughout the day.",
                "hi" to "हर समय पढ़ा जाने वाला आसान दरूद — ज़बान पर हल्का और मीज़ान में भारी।",
                "ur" to "ہر لمحہ پڑھا جانے والا مختصر ترین اور فضیلت والا درود شریف۔"
            ),
            hadithRef = "Sunan an-Nasa'i 1296 & Sahih Muslim 408",
            defaultTarget = 100,
            hadithArabic = "قَالَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ: «مَنْ صَلَّى عَلَيَّ صَلَاةً وَاحِدَةً صَلَّى اللَّهُ عَلَيْهِ عَشْرَ صَلَوَاتٍ، وَحُطَّتْ عَنْهُ عَشْرُ خَطِيئَاتٍ، وَرُفِعَتْ لَهُ عَشْرُ دَرَجَاتٍ»",
            hadithTranslations = mapOf(
                "en" to "The Messenger of Allah ﷺ said: 'Whoever sends blessings upon me once, Allah will send blessings upon him ten times, erase ten sins from him, and raise him ten degrees in status.'",
                "hi" to "रसूलुल्लाह ﷺ ने फ़रमाया: 'जो शख्स मुझ पर एक बार दरूद भेजता है, अल्लाह तआला उस पर दस रहमतें नाज़िल फ़रमाता है, उसके दस गुनाह माफ़ फ़रमाता है और उसके दस दर्जात बुलंद फ़रमाता है।' (सुनन अन-नसाई: 1296, सहीह मुस्लिम: 408)",
                "ur" to "رسول اللہ ﷺ نے فرمایا: جس شخص نے مجھ پر ایک مرتبہ درود بھیجا، اللہ تعالیٰ اس پر دس رحمتیں نازل فرماتا ہے، اس کی دس خطائیں معاف فرماتا ہے اور اس کے دس درجات بلند فرماتا ہے۔ (سنن نسائی: 1296، صحیح مسلم: 408)",
                "ar" to "قال رسول الله صلى الله عليه وسلم: من صلى علي صلاة واحدة صلى الله عليه عشر صلوات، وحطت عنه عشر خطيئات، ورفعت له عشر درجات.",
                "bn" to "রাসূলুল্লাহ ﷺ বলেছেন: যে ব্যক্তি আমার প্রতি একবার দরূদ পাঠ করে, আল্লাহ তার প্রতি দশটি রহমত বর্ষণ করেন, তার দশটি পাপ ক্ষমা করেন এবং তার মর্যাদা দশ গুণ বৃদ্ধি করেন।"
            ),
            hadithNarrator = mapOf(
                "en" to "Abu Talhah al-Ansari (RA) & Abu Hurairah (RA)",
                "hi" to "हज़रत अबू तलहा अल-अंसारी व हज़रत अबू हुरैरा (र.अ.)",
                "ur" to "حضرت ابو طلحہ انصاری و حضرت ابو ہریرہ رضی اللہ عنہ",
                "ar" to "أبو طلحة الأنصاري وأبو هريرة رضي الله عنهما",
                "bn" to "আবু তালহা আনসারি ও আবু হুরায়রা (রা.)"
            ),
            hadithBook = "Sunan an-Nasa'i 1296",
            hadithGrade = "Sahih",
            hadithUrl = "https://sunnah.com/nasai:1296"
        ),
        DaroodGuideItem(
            id = "d_salli",
            title = mapOf(
                "en" to "Allahumma Salli Ala Muhammad",
                "hi" to "अल्लाहुम्मा सल्लि अला मुहम्मद",
                "ur" to "اللہم صل علی محمد",
                "ar" to "اللهم صل على محمد وعلى آل محمد وسلم",
                "bn" to "আল্লাহুম্মা সাল্লি আলা মুহাম্মদ"
            ),
            arabic = "اَللّٰهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ وَسَلِّمْ",
            transliteration = mapOf(
                "en" to "Allahumma salli ala Muhammadin wa ala aali Muhammadin wa sallim",
                "hi" to "अल्लाहुम्मा सल्लि अला मुहम्मदिंव-व अला आलि मुहम्मदिंव-व सल्लिम",
                "ur" to "اللہم صل علی محمد وعلی آل محمد وسلم"
            ),
            translation = mapOf(
                "en" to "O Allah, send blessings and peace upon Muhammad and upon the family of Muhammad.",
                "hi" to "ऐ अल्लाह! मुहम्मद ﷺ और मुहम्मद ﷺ की आल पर रहमत और सलाम नाज़िल फ़रमा।",
                "ur" to "اے اللہ! محمد ﷺ پر اور ان کی آل پر رحمتیں اور سلامتی نازل فرما۔",
                "ar" to "اللهم صل وسلم وبارك على نبينا محمد وعلى آله وصحبه أجمعين.",
                "bn" to "হে আল্লাহ! মুহাম্মদ ও তাঁর পরিবারের প্রতি রহমত ও শান্তি বর্ষণ করুন।"
            ),
            benefits = mapOf(
                "en" to "Brings 10 blessings, erases 10 sins, and raises 10 ranks for each recitation.",
                "hi" to "हर एक बार पढ़ने पर 10 रहमतें, 10 गुनाहों की माफ़ी और 10 दर्जात की बुलंदी।",
                "ur" to "ہر بار پڑھنے پر دس رحمتوں کا نزول اور درجات کی بلندی۔"
            ),
            hadithRef = "Jami' at-Tirmidhi 484 & Sunan Abi Dawud 981",
            defaultTarget = 100,
            hadithArabic = "قَالَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ: «أَوْلَى النَّاسِ بِي يَوْمَ الْقِيَامَةِ أَكْثَرُهُمْ عَلَيَّ صَلَاةً»",
            hadithTranslations = mapOf(
                "en" to "The Messenger of Allah ﷺ said: 'The people who are closest and most deserving of me on the Day of Resurrection are those who send the most blessings upon me.'",
                "hi" to "रसूलुल्लाह ﷺ ने फ़रमाया: 'क़यामत के दिन लोगों में सबसे ज़्यादा मेरे क़रीब और मेरी शफ़ाअत का हक़दार वह शख्स होगा जो मुझ पर सबसे ज़्यादा दरूद भेजता है।' (जामेअ तिर्मिज़ी: 484)",
                "ur" to "رسول اللہ ﷺ نے فرمایا: قیامت کے دن لوگوں میں سب سے زیادہ میرے قریب وہ شخص ہوگا جو مجھ پر سب سے زیادہ درود بھیجتا ہے۔ (جامع ترمذی: 484)",
                "ar" to "قال رسول الله صلى الله عليه وسلم: أولى الناس بي يوم القيامة أكثرهم علي صلاة.",
                "bn" to "রাসূলুল্লাহ ﷺ বলেছেন: কিয়ামতের দিন মানুষের মধ্যে সবচেয়ে বেশি আমার নিকটবর্তী হবে সে, যে আমার প্রতি সবচেয়ে বেশি দরূদ পাঠ করে।"
            ),
            hadithNarrator = mapOf(
                "en" to "Abdullah bin Mas'ud (RA)",
                "hi" to "हज़रत अब्दुल्लाह बिन मसऊद (र.अ.)",
                "ur" to "حضرت عبد اللہ بن مسعود رضی اللہ عنہ",
                "ar" to "عبد الله بن مسعود رضي الله عنه",
                "bn" to "আব্দুল্লাহ ইবনে মাসউদ (রা.)"
            ),
            hadithBook = "Jami' at-Tirmidhi 484",
            hadithGrade = "Hasan Sahih",
            hadithUrl = "https://sunnah.com/tirmidhi:484"
        ),
        DaroodGuideItem(
            id = "d_tunajjina",
            title = mapOf(
                "en" to "Durood-e-Tunajjina (Salawat al-Munjiyyah)",
                "hi" to "दरूद-ए-तुनज्जिना",
                "ur" to "درود تنجینا",
                "ar" to "صلاة التنجينا المنجية",
                "bn" to "দরূদে তুনজিয়্যা"
            ),
            arabic = "اللَّهُمَّ صَلِّ عَلَى سَيِّدِنَا مُحَمَّدٍ صَلَاةً تُنْجِينَا بِهَا مِنْ جَمِيعِ الْأَهْوَالِ وَالْآفَاتِ ، وَتَقْضِي لَنَا بِهَا جَمِيعَ الْحَاجَاتِ ، وَتُطَهِّرُنَا بِهَا مِنْ جَمِيعِ السَّيِّئَاتِ ، وَتَرْفَعُنَا بِهَا عِنْدَكَ أَعْلَى الدَّرَجَاتِ ، وَتُبَلِّغُنَا بِهَا أَقْصَى الْغَايَاتِ مِنْ جَمِيعِ الْخَيْرَاتِ فِي الْحَيَاةِ وَبَعْدَ الْمَمَاتِ",
            transliteration = mapOf(
                "en" to "Allahumma salli ala Sayyidina Muhammadin salatan tunjina biha min jami'il ahwali wal-afat, wa taqdi lana biha jami'al hajat, wa tutahhiruna biha min jami'is-sayyi'at, wa tarfa'una biha 'indaka a'lad-darajat, wa tuballighuna biha aqsal-ghayat min jami'il-khayrati fil-hayati wa ba'dal-mamat.",
                "hi" to "अल्लाहुम्मा सल्लि अला सय्यिदिना मुहम्मदिंन सलातन तुनज्जीना बिहा मिन जमीइल अहवालि वल-आफ़ात, व तक़्दी लना बिहा जमीअल हाजात, व तुतह्हिरुना बिहा मिन जमीइस-सय्यिआत, व तरफ़उना बिहा इंदका अअलद-दरजात, व तुबल्लिग़ुना बिहा अक़्सल-ग़ायात मिन जमीइल खैरात फ़िल-हयाति व बादाल-ममात।"
            ),
            translation = mapOf(
                "en" to "O Allah, send blessings upon our Master Muhammad, by which You save us from all terrors and calamities, fulfill for us all needs, purify us from all evils, raise us to the highest ranks, and grant us the ultimate of all good in life and after death.",
                "hi" to "ऐ अल्लाह! हमारे सरदार हज़रत मुहम्मद ﷺ पर ऐसी रहमत नाज़िल फ़रमा जिसके ज़रिए तू हमें हर आफ़त और मुसीबत से नजात अता फ़रमाए, हमारी तमाम हाजतें पूरी फ़रमाए, हमें तमाम बुराइयों से पाक कर दे, अपने पास आला दर्जात अता फ़रमाए और ज़िंदगी व मौत के बाद की तमाम भलाइयों के आख़िरी मुक़ाम तक पहुंचाए।",
                "ur" to "اے اللہ! ہمارے سردار حضرت محمد ﷺ پر ایسی رحمت نازل فرما جس کے ذریعے تو ہمیں تمام خوف اور مصیبتوں سے نجات عطا فرمائے، ہماری تمام حاجات پوری فرمائے، ہمیں تمام برائیوں سے پاک کر دے، اپنے پاس اعلیٰ درجات عطا فرمائے اور زندگی میں اور موت کے بعد تمام بھلائیوں کے آخری مقام تک پہنچائے۔",
                "ar" to "اللهم صل على سيدنا محمد صلاة تنجينا بها من جميع الأهوال والآفات، وتقضي لنا بها جميع الحاجات، وتطهرنا بها من جميع السيئات، وترفعنا بها عندك أعلى الدرجات، وتبلغنا بها أقصى الغايات من جميع الخيرات في الحياة وبعد الممات.",
                "bn" to "হে আল্লাহ! আমাদের নেতা মুহাম্মদ ﷺ-এর প্রতি এমন রহমত বর্ষণ করুন যার মাধ্যমে আপনি আমাদেরকে সমস্ত বিপদ ও ভীতি থেকে মুক্তি দান করবেন, আমাদের সকল প্রয়োজন পূরণ করবেন, আমাদেরকে সমস্ত পাপ থেকে পবিত্র করবেন..."
            ),
            benefits = mapOf(
                "en" to "Authentic traditional Salawat of deliverance from hardships and granting of prayers.",
                "hi" to "मुश्किलों और परेशानियों से नजात पाने के लिए मशहूर और मुजर्रब दरूद।",
                "ur" to "مشکلات اور پریشانیوں سے نجات پانے کے لیے بے حد مجرب اور مشہور درود۔"
            ),
            hadithRef = "Authentic Traditional Salawat of Deliverance (Fajr al-Munir)",
            defaultTarget = 33,
            hadithArabic = "اللَّهُمَّ صَلِّ عَلَى سَيِّدِنَا مُحَمَّدٍ صَلَاةً تُنْجِينَا بِهَا مِنْ جَمِيعِ الْأَهْوَالِ وَالْآفَاتِ ، وَتَقْضِي لَنَا بِهَا جَمِيعَ الْحَاجَاتِ ، وَتُطَهِّرُنَا بِهَا مِنْ جَمِيعِ السَّيِّئَاتِ ، وَتَرْفَعُنَا بِهَا عِنْدَكَ أَعْلَى الدَّرَجَاتِ ، وَتُبَلِّغُنَا بِهَا أَقْصَى الْغَايَاتِ مِنْ جَمِيعِ الْخَيْرَاتِ فِي الْحَيَاةِ وَبَعْدَ الْمَمَاتِ",
            hadithTranslations = mapOf(
                "en" to "O Allah, send blessings upon our Master Muhammad, by which You deliver us from all fears and calamities, fulfill all our needs, purify us from all sins, raise us to the highest ranks in Your presence, and bring us to the ultimate good in this life and after death.",
                "hi" to "ऐ अल्लाह! हमारे सरदार हज़रत मुहम्मद ﷺ पर ऐसी रहमत नाज़िल फ़रमा जिसके ज़रिए तू हमें हर आफ़त और मुसीबत से नजात अता फ़रमाए, हमारी तमाम हाजतें पूरी फ़रमाए, हमें तमाम बुराइयों से पाक कर दे, अपने पास आला दर्जात अता फ़रमाए और ज़िंदगी व मौत के बाद की तमाम भलाइयों के आख़िरी मुक़ाम तक पहुंचाए।",
                "ur" to "اے اللہ! ہمارے سردار حضرت محمد ﷺ پر ایسی رحمت نازل فرما جس کے ذریعے تو ہمیں تمام خوف اور مصیبتوں سے نجات عطا فرمائے، ہماری تمام حاجات پوری فرمائے، ہمیں تمام برائیوں سے پاک کر دے، اپنے پاس اعلیٰ درجات عطا فرمائے اور زندگی میں اور موت کے بعد تمام بھلائیوں کے آخری مقام تک پہنچائے۔",
                "ar" to "اللهم صل على سيدنا محمد صلاة تنجينا بها من جميع الأهوال والآفات، وتقضي لنا بها جميع الحاجات، وتطهرنا بها من جميع السيئات، وترفعنا بها عندك أعلى الدرجات، وتبلغنا بها أقصى الغايات من جميع الخيرات في الحياة وبعد الممات.",
                "bn" to "হে আল্লাহ! আমাদের নেতা মুহাম্মদ ﷺ-এর প্রতি এমন রহমত বর্ষণ করুন যার মাধ্যমে আপনি আমাদেরকে সমস্ত বিপদ ও ভীতি থেকে মুক্তি দান করবেন, আমাদের সকল প্রয়োজন পূরণ করবেন, আমাদেরকে সমস্ত পাপ থেকে পবিত্র করবেন..."
            ),
            hadithNarrator = mapOf(
                "en" to "Shaykh Musa ad-Darir (RA) & Islamic Tradition",
                "hi" to "शैख़ मूसा अद्-दरीर (र.अ.) व बुज़ुर्गान-ए-दीन",
                "ur" to "شیخ موسیٰ الضریر رحمہ اللہ و علمائے امت",
                "ar" to "الشيخ موسى الضریر وعلماء الأمة",
                "bn" to "শায়খ মুসা আদ-দারীর (রহ.)"
            ),
            hadithBook = "Al-Fajr al-Munir & Classical Traditions",
            hadithGrade = "Maqbul / Mashhur",
            hadithUrl = "https://sunnah.com/search?q=salawat"
        )
    )

    // ══════════════════════════════════════════════════════════════
    //  4. 99 NAMES OF ALLAH (Full Authentic Catalog)
    // ══════════════════════════════════════════════════════════════
    val ASMA_UL_HUSNA: List<AsmaNameItem> = listOf(
        AsmaNameItem(1, "اَللّٰہ", "Allah", mapOf("hi" to "वह जो केवल पूजा के योग्य है", "en" to "The One worthy of all worship", "ur" to "وہ جو صرف عبادت کے لائق ہے", "ar" to "الذي يستحق العبادة وحده", "bn" to "তিনি যিনি একমাত্র উপাসনার যোগ্য")),
        AsmaNameItem(2, "اَلرَّحْمٰن", "Ar-Rahman", mapOf("hi" to "अत्यंत दयालु", "en" to "The Most Gracious", "ur" to "انتہائی مہربان", "ar" to "بالغ الرحمة", "bn" to "অত্যন্ত দয়ালু")),
        AsmaNameItem(3, "اَلرَّحِيْم", "Ar-Raheem", mapOf("hi" to "विशेष रूप से दयालु", "en" to "The Most Merciful", "ur" to "خصوصی رحمت والا", "ar" to "الرحيم بالمؤمنين", "bn" to "বিশেষভাবে দয়ালু")),
        AsmaNameItem(4, "اَلْمَلِك", "Al-Malik", mapOf("hi" to "राजाओं का राजा", "en" to "The Absolute Sovereign", "ur" to "بادشاہوں کا بادشاہ", "ar" to "ملك الملوك", "bn" to "রাজাদের রাজা")),
        AsmaNameItem(5, "اَلْقُدُّوْس", "Al-Quddus", mapOf("hi" to "परम पवित्र", "en" to "The Most Holy", "ur" to "بالکل پاک", "ar" to "المنزه عن كل نقص", "bn" to "সর্বপবিত্র")),
        AsmaNameItem(6, "اَلسَّلَام", "As-Salam", mapOf("hi" to "शांति देने वाला", "en" to "The Source of Peace", "ur" to "سلامتی دینے والا", "ar" to "مصدر السلام والأمان", "bn" to "শান্তির উৎস")),
        AsmaNameItem(7, "اَلْمُؤْمِن", "Al-Mumin", mapOf("hi" to "विश्वास दिलाने वाला", "en" to "The Grantor of Security", "ur" to "امن دینے والا", "ar" to "المؤمِّن بالأمان", "bn" to "নিরাপত্তাদাতা")),
        AsmaNameItem(8, "اَلْمُهَيْمِن", "Al-Muhaymin", mapOf("hi" to "सब पर निगरान", "en" to "The Guardian, Overseer", "ur" to "نگران، حفاظت کرنے والا", "ar" to "المهيمن على كل شيء", "bn" to "সর্বোপরি তত্ত্বাবধায়ক")),
        AsmaNameItem(9, "اَلْعَزِيْز", "Al-Aziz", mapOf("hi" to "सर्वशक्तिमान, अजेय", "en" to "The Almighty, Invincible", "ur" to "سب سے زبردست", "ar" to "الغالب الذي لا يُقهر", "bn" to "সর্বশক্তিমান অপরাজেয়")),
        AsmaNameItem(10, "اَلْجَبَّار", "Al-Jabbar", mapOf("hi" to "ज़बरदस्त, सब पर प्रभावी", "en" to "The Compeller", "ur" to "زبردستی قہر والا", "ar" to "القاهر فوق عباده", "bn" to "প্রচণ্ড শক্তিশালী")),
        AsmaNameItem(11, "اَلْمُتَكَبِّر", "Al-Mutakabbir", mapOf("hi" to "सर्वोच्च महिमा वाला", "en" to "The Supreme in Greatness", "ur" to "بڑائی والا", "ar" to "المتعالي بكبريائه", "bn" to "সর্বোচ্চ গৌরবান্বিত")),
        AsmaNameItem(12, "اَلْخَالِق", "Al-Khaliq", mapOf("hi" to "सृष्टिकर्ता", "en" to "The Creator", "ur" to "خالق، پیدا کرنے والا", "ar" to "خالق كل شيء", "bn" to "সৃষ্টিকর্তা")),
        AsmaNameItem(13, "اَلْبَارِئ", "Al-Bari", mapOf("hi" to "हर चीज़ का निर्माता", "en" to "The Originator", "ur" to "ہر چیز بنانے والا", "ar" to "المبدع الصانع", "bn" to "সব কিছুর উদ্ভাবক")),
        AsmaNameItem(14, "اَلْمُصَوِّر", "Al-Musawwir", mapOf("hi" to "रूप देने वाला", "en" to "The Shaper of Beauty", "ur" to "صورت بنانے والا", "ar" to "المصوِّر للخلق", "bn" to "আকৃতিদাতা")),
        AsmaNameItem(15, "اَلْغَفَّار", "Al-Ghaffar", mapOf("hi" to "बहुत माफ करने वाला", "en" to "The Repeatedly Forgiving", "ur" to "بہت بخشنے والا", "ar" to "كثير المغفرة", "bn" to "বারবার ক্ষমাকারী")),
        AsmaNameItem(16, "اَلْقَهَّار", "Al-Qahhar", mapOf("hi" to "सबको दबाने वाला", "en" to "The Subduer", "ur" to "غالب، سب کو دبانے والا", "ar" to "القاهر لكل شيء", "bn" to "সব কিছু দমনকারী")),
        AsmaNameItem(17, "اَلْوَهَّاب", "Al-Wahhab", mapOf("hi" to "बहुत देने वाला", "en" to "The Bestower of Gifts", "ur" to "بہت عطا کرنے والا", "ar" to "كثير العطاء", "bn" to "অনেক দানকারী")),
        AsmaNameItem(18, "اَلرَّزَّاق", "Ar-Razzaq", mapOf("hi" to "रोज़ी देने वाला", "en" to "The Provider", "ur" to "رزق دینے والا", "ar" to "رازق الخلق", "bn" to "রিযিকদাতা")),
        AsmaNameItem(19, "اَلْفَتَّاح", "Al-Fattah", mapOf("hi" to "हर राह खोलने वाला", "en" to "The Opener, Judge", "ur" to "ہر مشکل کھولنے والا", "ar" to "فاتح الأبواب", "bn" to "সব পথ উন্মোচনকারী")),
        AsmaNameItem(20, "اَلْعَلِيْم", "Al-Alim", mapOf("hi" to "सर्वज्ञ", "en" to "The All-Knowing", "ur" to "سب کچھ جاننے والا", "ar" to "العالم بكل شيء", "bn" to "সর্বজ্ঞ")),
        AsmaNameItem(21, "اَلْقَابِض", "Al-Qabid", mapOf("hi" to "सकुचाने वाला", "en" to "The Withholder", "ur" to "روکنے والا", "ar" to "القابض للأرزاق", "bn" to "সংকোচনকারী")),
        AsmaNameItem(22, "اَلْبَاسِط", "Al-Basit", mapOf("hi" to "फैलाने वाला", "en" to "The Extender", "ur" to "کشادگی دینے والا", "ar" to "الباسط للرزق", "bn" to "বিস্তারকারী")),
        AsmaNameItem(23, "اَلْخَافِض", "Al-Khafid", mapOf("hi" to "नीचा करने वाला", "en" to "The Abaser", "ur" to "پست کرنے والا", "ar" to "الخافض للمتكبرين", "bn" to "নিম্নকারী")),
        AsmaNameItem(24, "اَلرَّافِع", "Ar-Rafi", mapOf("hi" to "ऊँचा करने वाला", "en" to "The Exalter", "ur" to "بلند کرنے والا", "ar" to "الرافع للمؤمنين", "bn" to "উন্নয়নকারী")),
        AsmaNameItem(25, "اَلْمُعِزّ", "Al-Muizz", mapOf("hi" to "इज़्ज़त देने वाला", "en" to "The Bestower of Honour", "ur" to "عزت دینے والا", "ar" to "المُعزّ لمن يشاء", "bn" to "সম্মানদাতা")),
        AsmaNameItem(26, "اَلْمُذِلّ", "Al-Mudhill", mapOf("hi" to "ज़लील करने वाला", "en" to "The Humiliator", "ur" to "ذلیل کرنے والا", "ar" to "المُذلّ للعاصين", "bn" to "অপমানকারী")),
        AsmaNameItem(27, "اَلسَّمِيْع", "As-Sami", mapOf("hi" to "सब कुछ सुनने वाला", "en" to "The All-Hearing", "ur" to "سب کچھ سننے والا", "ar" to "السميع لكل شيء", "bn" to "সর্বশ্রোতা")),
        AsmaNameItem(28, "اَلْبَصِيْر", "Al-Basir", mapOf("hi" to "सब कुछ देखने वाला", "en" to "The All-Seeing", "ur" to "سب کچھ دیکھنے والا", "ar" to "البصير بكل شيء", "bn" to "সর্বদ্রষ্টা")),
        AsmaNameItem(29, "اَلْحَكَم", "Al-Hakam", mapOf("hi" to "न्यायाधीश", "en" to "The Impartial Judge", "ur" to "فیصلہ کرنے والا", "ar" to "الحاكم بالعدل", "bn" to "ন্যায়বিচারক")),
        AsmaNameItem(30, "اَلْعَدْل", "Al-Adl", mapOf("hi" to "न्यायकारी", "en" to "The Utterly Just", "ur" to "عادل", "ar" to "العادل في أحكامه", "bn" to "ন্যায়পরায়ণ")),
        AsmaNameItem(31, "اَللَّطِيْف", "Al-Latif", mapOf("hi" to "सूक्ष्म, कृपालु", "en" to "The Subtle, Kind", "ur" to "لطیف، باریک بین", "ar" to "اللطيف بعباده", "bn" to "সূক্ষ্ম দয়ালু")),
        AsmaNameItem(32, "اَلْخَبِيْر", "Al-Khabir", mapOf("hi" to "सब से बाखबर", "en" to "The All-Aware", "ur" to "باخبر، آگاہ", "ar" to "الخبير بكل شيء", "bn" to "সর্বসচেতন")),
        AsmaNameItem(33, "اَلْحَلِيْم", "Al-Halim", mapOf("hi" to "अत्यंत सहनशील", "en" to "The Most Forbearing", "ur" to "بردبار، حلیم", "ar" to "الحليم المتأني", "bn" to "অত্যন্ত সহনশীল")),
        AsmaNameItem(34, "اَلْعَظِيْم", "Al-Azim", mapOf("hi" to "सबसे महान", "en" to "The Most Great", "ur" to "بہت بڑا", "ar" to "العظيم في ذاته", "bn" to "সর্বমহান")),
        AsmaNameItem(35, "اَلْغَفُوْر", "Al-Ghafur", mapOf("hi" to "माफ करने वाला", "en" to "The Forgiving", "ur" to "معاف کرنے والا", "ar" to "الغافر للذنوب", "bn" to "ক্ষমাশীল")),
        AsmaNameItem(36, "اَلشَّكُوْر", "Ash-Shakur", mapOf("hi" to "शुक्रगुज़ार", "en" to "The Appreciative", "ur" to "شکر قبول کرنے والا", "ar" to "الشاكر لعباده", "bn" to "কৃতজ্ঞতা স্বীকারকারী")),
        AsmaNameItem(37, "اَلْعَلِيّ", "Al-Ali", mapOf("hi" to "सर्वोच्च", "en" to "The Most High", "ur" to "بلند، اعلیٰ", "ar" to "المتعالي فوق كل شيء", "bn" to "সর্বোচ্চ")),
        AsmaNameItem(38, "اَلْكَبِيْر", "Al-Kabir", mapOf("hi" to "बड़ाई वाला", "en" to "The Most Great", "ur" to "بڑائی والا", "ar" to "الكبير في عظمته", "bn" to "মহত্ত্বশালী")),
        AsmaNameItem(39, "اَلْحَفِيْظ", "Al-Hafiz", mapOf("hi" to "हिफाज़त करने वाला", "en" to "The Preserver", "ur" to "حفاظت کرنے والا", "ar" to "الحافظ للخلق", "bn" to "সংরক্ষণকারী")),
        AsmaNameItem(40, "اَلْمُقِيْت", "Al-Muqit", mapOf("hi" to "खाना देने वाला", "en" to "The Sustainer", "ur" to "خوراک دینے والا", "ar" to "المُغذِّي للخلق", "bn" to "পালনকর্তা")),
        AsmaNameItem(41, "اَلْحَسِيْب", "Al-Hasib", mapOf("hi" to "हिसाब लेने वाला", "en" to "The Reckoner", "ur" to "حساب لینے والا", "ar" to "المحاسب للعباد", "bn" to "হিসাবগ্রহণকারী")),
        AsmaNameItem(42, "اَلْجَلِيْل", "Al-Jalil", mapOf("hi" to "जलाल वाला", "en" to "The Majestic", "ur" to "جلال والا", "ar" to "الجليل في عظمته", "bn" to "মহিমান্বিত")),
        AsmaNameItem(43, "اَلْكَرِيْم", "Al-Karim", mapOf("hi" to "बहुत करम वाला", "en" to "The Most Generous", "ur" to "بہت کرم", "ar" to "الكريم الجواد", "bn" to "অত্যন্ত উদার")),
        AsmaNameItem(44, "اَلرَّقِيْب", "Ar-Raqib", mapOf("hi" to "निगरानी करने वाला", "en" to "The Watchful", "ur" to "نگران", "ar" to "الرقيب على أعمال العباد", "bn" to "নজরদারকারী")),
        AsmaNameItem(45, "اَلْمُجِيْب", "Al-Mujib", mapOf("hi" to "दुआ क़बूल करने वाला", "en" to "The Responsive", "ur" to "دعا قبول کرنے والا", "ar" to "المجيب للدعاء", "bn" to "দুআ কবুলকারী")),
        AsmaNameItem(46, "اَلْوَاسِع", "Al-Wasi", mapOf("hi" to "सब को समेटने वाला", "en" to "The All-Encompassing", "ur" to "وسعت والا", "ar" to "الواسع رحمته وعلمه", "bn" to "সর্বব্যাপী")),
        AsmaNameItem(47, "اَلْحَكِيْم", "Al-Hakim", mapOf("hi" to "परम बुद्धिमान", "en" to "The All-Wise", "ur" to "حکیم، دانا", "ar" to "الحكيم في أفعاله", "bn" to "পরম জ্ঞানী")),
        AsmaNameItem(48, "اَلْوَدُوْد", "Al-Wadud", mapOf("hi" to "मुहब्बत करने वाला", "en" to "The Loving", "ur" to "محبت کرنے والا", "ar" to "المحبّ لعباده المؤمنين", "bn" to "প্রেমময়")),
        AsmaNameItem(49, "اَلْمَجِيْد", "Al-Majid", mapOf("hi" to "शान वाला", "en" to "The Glorious", "ur" to "مجید، شان والا", "ar" to "المجيد في ذاته", "bn" to "মহাগৌরবান্বিত")),
        AsmaNameItem(50, "اَلْبَاعِث", "Al-Baaith", mapOf("hi" to "उठाने वाला", "en" to "The Resurrector", "ur" to "اٹھانے والا", "ar" to "الباعث للأموات", "bn" to "পুনরুত্থানকারী")),
        AsmaNameItem(51, "اَلشَّهِيْد", "Ash-Shahid", mapOf("hi" to "हर जगह मौजूद गवाह", "en" to "The Witness", "ur" to "گواہ", "ar" to "الشاهد على كل شيء", "bn" to "সর্বত্র সাক্ষী")),
        AsmaNameItem(52, "اَلْحَقّ", "Al-Haqq", mapOf("hi" to "परम सत्य", "en" to "The Absolute Truth", "ur" to "سچا، حق", "ar" to "الحق في ذاته وصفاته", "bn" to "পরম সত্য")),
        AsmaNameItem(53, "اَلْوَكِيْل", "Al-Wakil", mapOf("hi" to "भरोसेमंद वकील", "en" to "The Trustee", "ur" to "وکیل، بھروسے کا", "ar" to "الوكيل عن عباده", "bn" to "বিশ্বস্ত অভিভাবক")),
        AsmaNameItem(54, "اَلْقَوِيّ", "Al-Qawi", mapOf("hi" to "ताकतवर", "en" to "The All-Strong", "ur" to "طاقتور", "ar" to "القوي الشديد", "bn" to "অত্যন্ত শক্তিশালী")),
        AsmaNameItem(55, "اَلْمَتِيْن", "Al-Matin", mapOf("hi" to "दृढ़, मज़बूत", "en" to "The Firm", "ur" to "مضبوط", "ar" to "المتين القوي", "bn" to "দৃঢ় মজবুত")),
        AsmaNameItem(56, "اَلْوَلِيّ", "Al-Wali", mapOf("hi" to "दोस्त, संरक्षक", "en" to "The Protecting Friend", "ur" to "دوست، محافظ", "ar" to "وليّ المؤمنين", "bn" to "বন্ধু রক্ষাকর্তা")),
        AsmaNameItem(57, "اَلْحَمِيْد", "Al-Hamid", mapOf("hi" to "हर तारीफ के लायक", "en" to "The Praiseworthy", "ur" to "تعریف کے لائق", "ar" to "المستحق للحمد", "bn" to "প্রশংসাযোগ্য")),
        AsmaNameItem(58, "اَلْمُحْصِيْ", "Al-Muhsi", mapOf("hi" to "गिनने वाला", "en" to "The Accounter", "ur" to "گنتی کرنے والا", "ar" to "المحصي لكل شيء", "bn" to "গণনাকারী")),
        AsmaNameItem(59, "اَلْمُبْدِئ", "Al-Mubdi", mapOf("hi" to "शुरुआत करने वाला", "en" to "The Originator", "ur" to "ابتدا کرنے والا", "ar" to "المبدئ للخلق", "bn" to "সূচনাকারী")),
        AsmaNameItem(60, "اَلْمُعِيْد", "Al-Muid", mapOf("hi" to "दोबारा उठाने वाला", "en" to "The Restorer", "ur" to "دوبارہ اٹھانے والا", "ar" to "المعيد للخلق", "bn" to "পুনরুদ্ধারকারী")),
        AsmaNameItem(61, "اَلْمُحْيِيْ", "Al-Muhyi", mapOf("hi" to "ज़िंदगी देने वाला", "en" to "The Giver of Life", "ur" to "زندگی دینے والا", "ar" to "المُحيي للأموات", "bn" to "জীবনদাতা")),
        AsmaNameItem(62, "اَلْمُمِيْت", "Al-Mumit", mapOf("hi" to "मौत देने वाला", "en" to "The Taker of Life", "ur" to "موت دینے والا", "ar" to "المُميت للأحياء", "bn" to "মৃত্যুদাতা")),
        AsmaNameItem(63, "اَلْحَيّ", "Al-Hayy", mapOf("hi" to "हमेशा ज़िंदा", "en" to "The Eternally Living", "ur" to "ہمیشہ زندہ", "ar" to "الحيّ الدائم", "bn" to "চিরজীবী")),
        AsmaNameItem(64, "اَلْقَيُّوْم", "Al-Qayyum", mapOf("hi" to "हमेशा कायम रहने वाला", "en" to "The Self-Sustaining", "ur" to "قائم رہنے والا", "ar" to "القائم بنفسه", "bn" to "স্বয়ংসম্পূর্ণ")),
        AsmaNameItem(65, "اَلْوَاجِد", "Al-Wajid", mapOf("hi" to "जो चाहे पाने वाला", "en" to "The Finder", "ur" to "جو چاہے پانے والا", "ar" to "الواجد لما يشاء", "bn" to "যা চান তা পানকারী")),
        AsmaNameItem(66, "اَلْمَاجِد", "Al-Majid", mapOf("hi" to "जलाल और करम वाला", "en" to "The Noble", "ur" to "جلال اور کرم والا", "ar" to "الماجد الكريم", "bn" to "মহৎ মহিমান্বিত")),
        AsmaNameItem(67, "اَلْوَاحِد", "Al-Wahid", mapOf("hi" to "एकमात्र", "en" to "The One", "ur" to "اکیلا، واحد", "ar" to "الواحد الأحد", "bn" to "একমাত্র")),
        AsmaNameItem(68, "اَلصَّمَد", "As-Samad", mapOf("hi" to "सबका सहारा", "en" to "The Eternal Refuge", "ur" to "سب کا سہارا", "ar" to "الصمد الذي يلجأ إليه", "bn" to "সবার আশ্রয়")),
        AsmaNameItem(69, "اَلْقَادِر", "Al-Qadir", mapOf("hi" to "हर चीज़ पर क़ादिर", "en" to "The All-Powerful", "ur" to "قدرت والا", "ar" to "القادر على كل شيء", "bn" to "সর্বক্ষমতাবান")),
        AsmaNameItem(70, "اَلْمُقْتَدِر", "Al-Muqtadir", mapOf("hi" to "बड़ी ताकत वाला", "en" to "The Powerful", "ur" to "بڑی قوت والا", "ar" to "المقتدر القوي", "bn" to "মহাশক্তিশালী")),
        AsmaNameItem(71, "اَلْمُقَدِّم", "Al-Muqaddim", mapOf("hi" to "आगे करने वाला", "en" to "The Expediter", "ur" to "آگے کرنے والا", "ar" to "المقدِّم لما يشاء", "bn" to "অগ্রগামী করণকারী")),
        AsmaNameItem(72, "اَلْمُؤَخِّر", "Al-Muakhkhir", mapOf("hi" to "पीछे करने वाला", "en" to "The Delayer", "ur" to "پیچھے کرنے والا", "ar" to "المؤخِّر لما يشاء", "bn" to "পিছিয়ে দেওয়াকারী")),
        AsmaNameItem(73, "اَلْاَوَّل", "Al-Awwal", mapOf("hi" to "सबसे पहला", "en" to "The First", "ur" to "سب سے پہلا", "ar" to "الأول قبل كل شيء", "bn" to "সর্বপ্রথম")),
        AsmaNameItem(74, "اَلْاٰخِر", "Al-Akhir", mapOf("hi" to "सबसे आखिरी", "en" to "The Last", "ur" to "سب سے آخری", "ar" to "الآخر بعد كل شيء", "bn" to "সর্বশেষ")),
        AsmaNameItem(75, "اَلظَّاهِر", "Az-Zahir", mapOf("hi" to "ज़ाहिर, प्रत्यक्ष", "en" to "The Manifest", "ur" to "ظاہر", "ar" to "الظاهر بآياته", "bn" to "প্রকাশমান")),
        AsmaNameItem(76, "اَلْبَاطِن", "Al-Batin", mapOf("hi" to "छुपा हुआ, सूक्ष्म", "en" to "The Hidden", "ur" to "باطن، پوشیدہ", "ar" to "الباطن المستتر", "bn" to "গুপ্ত অদৃশ্য")),
        AsmaNameItem(77, "اَلْوَالِيْ", "Al-Wali", mapOf("hi" to "शासक, मालिक", "en" to "The Sole Governor", "ur" to "حاکم، مالک", "ar" to "الوالي المدبِّر", "bn" to "একমাত্র শাসক")),
        AsmaNameItem(78, "اَلْمُتَعَالِيْ", "Al-Mutaali", mapOf("hi" to "सबसे ऊँचा", "en" to "The Self Exalted", "ur" to "سب سے اونچا", "ar" to "المتعالي فوق كل شيء", "bn" to "সর্বোচ্চে আসীন")),
        AsmaNameItem(79, "اَلْبَرّ", "Al-Barr", mapOf("hi" to "नेकी करने वाला", "en" to "The Source of All Goodness", "ur" to "نیکی کرنے والا", "ar" to "البرّ المُحسن", "bn" to "নেকির উৎস")),
        AsmaNameItem(80, "اَلتَّوَّاب", "At-Tawwab", mapOf("hi" to "बार बार तौबा क़बूल करने वाला", "en" to "The Acceptor of Repentance", "ur" to "توبہ قبول کرنے والا", "ar" to "التواب على عباده", "bn" to "বারবার তওবা কবুলকারী")),
        AsmaNameItem(81, "اَلْمُنْتَقِم", "Al-Muntaqim", mapOf("hi" to "बदला लेने वाला", "en" to "The Avenger", "ur" to "انتقام لینے والا", "ar" to "المنتقم من الظالمين", "bn" to "প্রতিশোধগ্রহণকারী")),
        AsmaNameItem(82, "اَلْعَفُوّ", "Al-Afuw", mapOf("hi" to "माफ करने वाला", "en" to "The Pardoner", "ur" to "معاف کرنے والا", "ar" to "العفوّ عن الذنوب", "bn" to "পাপ মার্জনাকারী")),
        AsmaNameItem(83, "اَلرَّؤُوْف", "Ar-Rauf", mapOf("hi" to "बहुत नरम दिल", "en" to "The Most Kind", "ur" to "بہت شفیق", "ar" to "الرؤوف بعباده", "bn" to "অত্যন্ত স্নেহশীল")),
        AsmaNameItem(84, "مَالِكُ الْمُلْك", "Malik ul Mulk", mapOf("hi" to "बादशाहत का मालिक", "en" to "Owner of all Sovereignty", "ur" to "سلطنت کا مالک", "ar" to "مالك الملك وحده", "bn" to "সার্বভৌমত্বের মালিক")),
        AsmaNameItem(85, "ذُو الْجَلَالِ وَالْاِكْرَام", "Dhul Jalali wal Ikram", mapOf("hi" to "जलाल और करम वाला", "en" to "Lord of Majesty and Bounty", "ur" to "جلال اور اکرام والا", "ar" to "ذو الجلال والإكرام", "bn" to "মহিমা ও সম্মানের অধিকারী")),
        AsmaNameItem(86, "اَلْمُقْسِط", "Al-Muqsit", mapOf("hi" to "न्यायकारी", "en" to "The Equitable", "ur" to "انصاف کرنے والا", "ar" to "المقسط العادل", "bn" to "ন্যায়পরায়ণ")),
        AsmaNameItem(87, "اَلْجَامِع", "Al-Jami", mapOf("hi" to "सबको जमा करने वाला", "en" to "The Gatherer", "ur" to "جمع کرنے والا", "ar" to "الجامع للخلق", "bn" to "সমবেতকারী")),
        AsmaNameItem(88, "اَلْغَنِيّ", "Al-Ghani", mapOf("hi" to "बेनियाज़, बेपरवाह", "en" to "The Self-Sufficient", "ur" to "بے نیاز", "ar" to "الغني عن كل شيء", "bn" to "অমুখাপেক্ষী")),
        AsmaNameItem(89, "اَلْمُغْنِيْ", "Al-Mughni", mapOf("hi" to "अमीर बनाने वाला", "en" to "The Enricher", "ur" to "مالدار بنانے والا", "ar" to "المُغني لمن يشاء", "bn" to "ধনী করণকারী")),
        AsmaNameItem(90, "اَلْمَانِع", "Al-Mani", mapOf("hi" to "रोकने वाला", "en" to "The Preventer", "ur" to "روکنے والا", "ar" to "المانع لما يشاء", "bn" to "বাধাদানকারী")),
        AsmaNameItem(91, "اَلضَّارّ", "Ad-Darr", mapOf("hi" to "नुकसान देने वाला", "en" to "The Distresser", "ur" to "نقصان پہنچانے والا", "ar" to "الضار لمن يشاء", "bn" to "ক্ষতিকারক")),
        AsmaNameItem(92, "اَلنَّافِع", "An-Nafi", mapOf("hi" to "फायदा देने वाला", "en" to "The Benefiter", "ur" to "نفع دینے والا", "ar" to "النافع لمن يشاء", "bn" to "উপকারকারী")),
        AsmaNameItem(93, "اَلنُّوْر", "An-Nur", mapOf("hi" to "नूर, रोशनी देने वाला", "en" to "The Light", "ur" to "نور، روشنی", "ar" to "نور السماوات والأرض", "bn" to "আলো")),
        AsmaNameItem(94, "اَلْهَادِيْ", "Al-Hadi", mapOf("hi" to "राह दिखाने वाला", "en" to "The Guide", "ur" to "ہدایت دینے والا", "ar" to "الهادي إلى الصراط المستقيم", "bn" to "পথপ্রদর্শক")),
        AsmaNameItem(95, "اَلْبَدِيْع", "Al-Badi", mapOf("hi" to "अनोखा रचने वाला", "en" to "The Originator", "ur" to "انوکھا بنانے والا", "ar" to "البديع المبتكر", "bn" to "অনন্য স্রষ্টা")),
        AsmaNameItem(96, "اَلْبَاقِيْ", "Al-Baqi", mapOf("hi" to "हमेशा रहने वाला", "en" to "The Everlasting", "ur" to "ہمیشہ رہنے والا", "ar" to "الباقي الدائم", "bn" to "চিরস্থায়ী")),
        AsmaNameItem(97, "اَلْوَارِث", "Al-Waris", mapOf("hi" to "सब का वारिस", "en" to "The Inheritor", "ur" to "وارث", "ar" to "الوارث لكل شيء", "bn" to "উত্তরাধিকারী")),
        AsmaNameItem(98, "اَلرَّشِيْد", "Ar-Rashid", mapOf("hi" to "सही राह बताने वाला", "en" to "The Guide to the Right Path", "ur" to "سیدھی راہ دکھانے والا", "ar" to "الرشيد المُهدي", "bn" to "সঠিক পথের দিকনির্দেশক")),
        AsmaNameItem(99, "اَلصَّبُوْر", "As-Sabur", mapOf("hi" to "बहुत सब्र वाला", "en" to "The Patient", "ur" to "بہت صبر والا", "ar" to "الصبور الحليم", "bn" to "অত্যন্ত ধৈর্যশীল")),
    )

        // 12 AUTHENTIC GUIDES FROM ORIGINAL APP
    val DEFAULT_GUIDE_LIST: List<DhikrGuideDetail> = listOf(
        DhikrGuideDetail(
            id = "g1",
            arabic = "سُبْحَانَ اللّٰہِ وَبِحَمْدِهٖ سُبْحَانَ اللّٰہِ الْعَظِيْم",
            title = mapOf("hi" to "सुब्हानल्लाहि वबिहम्दिही, सुब्हानल्लाहिल अज़ीम", "en" to "SubhanAllahi wa Bihamdihi, SubhanAllahil Azim", "ur" to "سبحان اللہ و بحمدہ، سبحان اللہ العظیم", "ar" to "سبحان الله وبحمده، سبحان الله العظيم", "bn" to "সুবহানাল্লাহি ওয়া বিহামদিহি, সুবহানাল্লাহিল আজিম"),
            meaning = mapOf("hi" to "अल्लाह पाक है अपनी तारीफ के साथ, वह सबसे अज़ीम (महान) है", "en" to "Glory and Praise be to Allah — He is the Most Magnificent", "ur" to "اللہ پاک ہے اپنی تعریف کے ساتھ، وہ عظیم ہے", "ar" to "سبحانه وبحمده، وهو العظيم", "bn" to "আল্লাহ পবিত্র ও প্রশংসিত, তিনি মহান"),
            targetStr = "100×",
            howToRecite = mapOf("en" to "Recite 100 times in the morning and evening, or complete 100 times anytime during the day.", "hi" to "सुबह और शाम 100 बार पढ़ें, या दिन में किसी भी समय 100 बार पूरा करें।", "ur" to "صبح و شام 100 مرتبہ پڑھیں، یا دن میں کسی بھی وقت 100 بار مکمل کریں۔", "ar" to "يُقرأ مائة مرة صباحاً ومساءً أو في أي وقت من اليوم.", "bn" to "সকাল ও সন্ধ্যায় ১০০ বার পড়ুন, অথবা দিনের যেকোনো সময় ১০০ বার পূর্ণ করুন।"),
            hadithRef = "Sahih Muslim 2691",
            virtues = mapOf("hi" to "100 गुनाह माफ होते हैं, 100 नेकियाँ आमलनामे में लिखी जाती हैं, और यह रहमान को सबसे प्यारी है।", "en" to "100 sins are forgiven, 100 good deeds written in the record, and it is most beloved to the Most Merciful.", "ur" to "100 گناہ معاف ہوتے ہیں، 100 نیکیاں اعمال نامے میں درج ہوتی ہیں، اور رحمٰن کو سب سے محبوب ہے۔", "ar" to "تُمحى مائة خطيئة وتُكتب مائة حسنة في الصحيفة، وهي أحب الكلام إلى الرحمن.", "bn" to "১০০ গুনাহ মাফ হয়, আমলনামায় ১০০ নেকি লেখা হয় এবং এটি রহমানের কাছে সর্বাধিক প্রিয়।"),
            hadithArabic = "كَلِمَتَانِ حَبِيبَتَانِ إِلَى الرَّحْمَنِ، خَفِيفَتَانِ عَلَى اللِّسَانِ، ثَقِيلَتَانِ فِي الْمِيزَانِ: سُبْحَانَ اللَّهِ وَبِحَمْدِهِ، سُبْحَانَ اللَّهِ الْعَظِيمِ",
            hadithTranslations = mapOf("hi" to "रसूलुल्लाह (ﷺ) ने फ़रमाया: 'दो कलिमे ऐसे हैं जो रहमान (अल्लाह) को बहुत प्यारे हैं, ज़बान पर बहुत हल्के हैं, और मीज़ान (तराज़ू) में बहुत भारी हैं: सुब्हानल्लाहि वबिहम्दिही, सुब्हानल्लाहिल अज़ीम।'", "en" to "The Prophet (ﷺ) said: 'There are two expressions that are dear to the Most Gracious, light on the tongue, and heavy on the Balance: SubhanAllahi wa bihamdihi, SubhanAllahil Azim.'", "ur" to "رسول اللہ صلی اللہ علیہ وسلم نے فرمایا: 'دو کلمے ایسے ہیں جو رحمٰن کو بہت محبوب ہیں، زبان پر بہت ہلکے ہیں اور میزان میں بہت بھاری ہیں: سبحان اللہ وبحمدہ، سبحان اللہ العظیم۔'"),
            hadithNarrator = "Abu Huraira (R.A.)",
            hadithBook = "Sahih al-Bukhari 6682 & Sahih Muslim 2691",
            hadithGrade = "Sahih (Muttafaq 'Alayh)",
            hadithUrl = "https://sunnah.com/bukhari:6682",
            order = 1
        ),
        DhikrGuideDetail(
            id = "g2",
            arabic = "سُبْحَانَ اللّٰہِ الْعَظِيْم",
            title = mapOf("hi" to "सुब्हानल्लाहिल अज़ीम", "en" to "SubhanAllahil Azim", "ur" to "سبحان اللہ العظیم", "ar" to "سبحان الله العظيم", "bn" to "সুবহানাল্লাহিল আজিম"),
            meaning = mapOf("hi" to "अज़ीम (महान व सर्वशक्तिमान) अल्लाह की पवित्रता बयान करना", "en" to "Declaring the glory of Allah the Most Magnificent", "ur" to "عظیم اللہ کی پاکیزگی بیان کرنا", "ar" to "تنزيه الله العظيم عن كل نقص", "bn" to "মহান আল্লাহর পবিত্রতা ঘোষণা করা"),
            targetStr = "∞",
            howToRecite = mapOf("en" to "Recite frequently throughout the day while walking, sitting, or working.", "hi" to "दिन-रात में कसरत से चलते-फिरते, उठते-बैठते और काम करते हुए पढ़ें।", "ur" to "دن رات میں کثرت سے چلتے پھرتے، اٹھتے بیٹھتے پڑھیں۔", "ar" to "يُكثر منه في سائر الأوقات قائماً وقاعداً وعلى جنبه.", "bn" to "সারাদিন হাঁটাচলা, বসা ও কাজের সময় বেশি বেশি পাঠ করুন।"),
            hadithRef = "Sahih Bukhari 6682",
            virtues = mapOf("hi" to "ज़बान पर हल्की, मीज़ान में बहुत भारी, और रहमान को अत्यंत पसंदीदा कलिमा।", "en" to "Light on the tongue, heavy on the Scale of Deeds, most beloved to the Most Merciful.", "ur" to "زبان پر ہلکی، میزان میں بہت بھاری، رحمٰن کو انتہائی پسندیدہ کلمہ۔", "ar" to "خفيفة على اللسان، ثقيلة في الميزان، حبيبة إلى الرحمن.", "bn" to "মুখে হালকা, আমলের পাল্লায় অনেক ভারী, রহমানের কাছে সবচেয়ে প্রিয়।"),
            hadithArabic = "كَلِمَتَانِ خَفِيفَتَانِ عَلَى اللِّسَانِ، ثَقِيلَتَانِ فِي الْمِيزَانِ، حَبِيبَتَانِ إِلَى الرَّحْمَنِ: سُبْحَانَ اللَّهِ الْعَظِيمِ، سُبْحَانَ اللَّهِ وَبِحَمْدِهِ",
            hadithTranslations = mapOf("hi" to "रसूलुल्लाह (ﷺ) ने फ़रमाया: 'दो कलिमे रहमान को बहुत प्यारे हैं, ज़बान पर हल्के और तराज़ू में भारी: सुब्हानल्लाहिल अज़ीम, सुब्हानल्लाहि वबिहम्दिही।'", "en" to "The Messenger of Allah (ﷺ) said: 'Two words are beloved to the Most Merciful, light on the tongue and heavy in the Balance: SubhanAllahil Azim, SubhanAllahi wa bihamdihi.'", "ur" to "رسول اللہ صلی اللہ علیہ وسلم نے فرمایا: 'دو کلمے رحمٰن کو بہت محبوب ہیں، زبان پر ہلکے اور ترازو میں بھاری: سبحان اللہ العظیم، سبحان اللہ وبحمدہ۔'"),
            hadithNarrator = "Abu Huraira (R.A.)",
            hadithBook = "Sahih al-Bukhari 6682",
            hadithGrade = "Sahih",
            hadithUrl = "https://sunnah.com/bukhari:6682",
            order = 2
        ),
        DhikrGuideDetail(
            id = "g3",
            arabic = "لَا إِلٰهَ إِلَّا اللّٰہُ وَحْدَهٗ لَا شَرِيْكَ لَهٗ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلٰى كُلِّ شَيْءٍ قَدِيْر",
            title = mapOf("hi" to "ला इलाहा इल्लल्लाहु वहदहू ला शरीका लहू, लहुल मुल्कु वलहुल हम्दु, वहुवा अला कुल्लि शैइन क़दीर", "en" to "La ilaha illAllahu wahdahu la sharika lahu, lahul mulku walahul hamdu, wahuwa ala kulli shain Qadir", "ur" to "لا الہ الا اللہ وحدہ لا شریک لہ، لہ الملک ولہ الحمد، وہو علی کل شیء قدیر", "ar" to "لا إله إلا الله وحده لا شريك له، له الملك وله الحمد، وهو على كل شيء قدير", "bn" to "লা ইলাহা ইল্লাল্লাহু ওয়াহদাহু লা শারীকা লাহু, লাহুল মুলকু ওয়ালাহুল হামদু, ওয়াহুয়া আলা কুল্লি শাইন কাদির"),
            meaning = mapOf("hi" to "अल्लाह के सिवा कोई माबूद नहीं, वह अकेला है, उसका कोई शरीक नहीं, उसी की बादशाहत और तारीफ है, वह हर चीज़ पर क़ादिर है", "en" to "None worthy of worship except Allah alone, no partner — His is the dominion and praise, and He has power over all things", "ur" to "اللہ کے سوا کوئی معبود نہیں، وہ اکیلا ہے بغیر شریک، اسی کی بادشاہت اور تعریف ہے، وہ ہر چیز پر قادر ہے", "ar" to "لا معبود بحق إلا الله وحده لا شريك له، له السلطان وله الحمد، وهو على كل شيء قدير", "bn" to "আল্লাহ ছাড়া কোনো সত্য উপাস্য নেই, তিনি একা শরিকবিহীন, রাজত্ব ও প্রশংসা তাঁরই, তিনি সর্বশক্তিমান"),
            targetStr = "100×",
            howToRecite = mapOf("en" to "Best recited 100 times after Fajr prayer, or anytime throughout the day.", "hi" to "फज्र की नमाज़ के बाद सुबह 100 बार पढ़ना सबसे अफ़ज़ल है, या दिन में कभी भी 100 बार पूरा करें।", "ur" to "فجر کی نماز کے بعد صبح 100 مرتبہ پڑھنا افضل ہے، یا دن میں کسی بھی وقت پورا کریں۔", "ar" to "يستحب قراءته مائة مرة بعد صلاة الفجر أو في سائر النهار.", "bn" to "ফজরের নামাজের পর সকালে ১০০ বার পড়া সর্বোত্তম, অথবা সারাদিনে যেকোনো সময় ১০০ বার পূর্ণ করুন।"),
            hadithRef = "Sahih Bukhari 3293",
            virtues = mapOf("hi" to "10 गुलाम आज़ाद करने का सवाब, 100 नेकियां दर्ज, 100 गुनाह मिटाए जाते हैं, और शाम तक शैतान से हिफ़ाज़त रहती है।", "en" to "Reward equivalent to freeing 10 slaves, 100 good deeds recorded, 100 sins erased, and shielded from Shaytan all day.", "ur" to "10 غلام آزاد کرنے کا ثواب، 100 نیکیاں درج، 100 گناہ معاف، اور شام تک شیطان سے مکمل پناہ۔", "ar" to "تعدل عتق عشر رقاب، وتكتب له مائة حسنة، وتمحى عنه مائة سيئة، وكانت له حرزاً من الشيطان يومه ذلك حتى يمسي.", "bn" to "১০টি দাস মুক্ত করার সমতুল্য সওয়াব, ১০০ নেকি লেখা হয়, ১০০ পাপ মোচন হয় এবং সারা দিন শয়তান থেকে নিরাপদ থাকা যায়।"),
            hadithArabic = "مَنْ قَالَ: لاَ إِلَهَ إِلاَّ اللَّهُ وَحْدَهُ لاَ شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ، وَهْوَ عَلَى كُلِّ شَىْءٍ قَدِيرٌ. فِي يَوْمٍ مِائَةَ مَرَّةٍ، كَانَتْ لَهُ عَدْلَ عَشْرِ رِقَابٍ، وَكُتِبَتْ لَهُ مِائَةُ حَسَنَةٍ، وَمُحِيَتْ عَنْهُ مِائَةُ سَيِّئَةٍ، وَكَانَتْ لَهُ حِرْزًا مِنَ الشَّيْطَانِ يَوْمَهُ ذَلِكَ حَتَّى يُمْسِيَ",
            hadithTranslations = mapOf("hi" to "रसूलुल्लाह (ﷺ) ने फ़रमाया: 'जो शख्स दिन में 100 बार यह कहे, उसे 10 गुलाम आज़ाद करने का सवाब मिलेगा, उसके खाते में 100 नेकियां लिखी जाएंगी, 100 गुनाह मिटा दिए जाएंगे, और उस दिन शाम तक वह शैतान से महफ़ूज़ रहेगा।'", "en" to "Allah's Messenger (ﷺ) said: 'Whoever says: La ilaha illAllahu wahdahu la sharika lahu... one hundred times a day, will get the reward of releasing ten slaves, and one hundred good deeds will be written in his account, and one hundred bad deeds will be erased, and it will be a shield for him from Satan on that day until evening.'", "ur" to "رسول اللہ صلی اللہ علیہ وسلم نے فرمایا: 'جس نے دن میں سو مرتبہ یہ کہا، اسے دس غلام آزاد کرنے کا ثواب ملے گا، اس کے لیے سو نیکیاں لکھی جائیں گی، سو گناہ مٹا دیے جائیں گے، اور اس دن شام تک وہ شیطان سے محفوظ رہے گا۔'"),
            hadithNarrator = "Abu Huraira (R.A.)",
            hadithBook = "Sahih al-Bukhari 3293 & Sahih Muslim 2691",
            hadithGrade = "Sahih (Muttafaq 'Alayh)",
            hadithUrl = "https://sunnah.com/bukhari:3293",
            order = 3
        ),
        DhikrGuideDetail(
            id = "g4",
            arabic = "سُبْحَانَ اللّٰہِ وَبِحَمْدِهٖ",
            title = mapOf("hi" to "सुब्हानल्लाहि वबिहम्दिही", "en" to "SubhanAllahi wa Bihamdihi", "ur" to "سبحان اللہ و بحمدہ", "ar" to "سبحان الله وبحمده", "bn" to "সুবহানাল্লাহি ওয়া বিহামদিহি"),
            meaning = mapOf("hi" to "अल्लाह पाक है और तमाम तारीफें उसी के लिए हैं", "en" to "Glory be to Allah and Praise be to Him", "ur" to "اللہ پاک ہے اور تمام تعریفیں اسی کے لیے ہیں", "ar" to "تنزيهاً لله وحمداً له", "bn" to "আল্লাহ পবিত্র এবং সমস্ত প্রশংসা তাঁরই"),
            targetStr = "100×",
            howToRecite = mapOf("en" to "Recite 100 times in the morning after Fajr and 100 times in the evening after Asr/Maghrib.", "hi" to "सुबह फज्र के बाद 100 बार और शाम असर/मगरिब के बाद 100 बार मुक़र्रर करें।", "ur" to "صبح فجر کے بعد 100 بار اور شام عصر/مغرب کے بعد 100 بار پڑھیں۔", "ar" to "مائة مرة صباحاً بعد الفجر ومائة مرة مساءً بعد العصر.", "bn" to "সকালে ফজরের পর ১০০ বার এবং সন্ধ্যায় আসর বা মাগরিবের পর ১০০ বার পড়ুন।"),
            hadithRef = "Sahih Muslim 2692",
            virtues = mapOf("hi" to "क़यामत के दिन इससे अफ़ज़ल अमल किसी का नहीं होगा, सिवाय उसके जिसने इससे भी ज़्यादा पढ़ा हो। सारे गुनाह समंदर के झाग बराबर हों तो भी माफ़ हो जाते हैं।", "en" to "No one will bring anything better on the Day of Resurrection except one who said more. All sins are forgiven even if as much as sea foam.", "ur" to "قیامت کے دن اس سے افضل عمل کسی کا نہ ہوگا، سوائے اس کے جس نے اس سے زیادہ پڑھا ہو۔ گناہ سمندر کی جھاگ برابر بھی ہوں تو معاف ہو جاتے ہیں۔", "ar" to "لم يأت أحد يوم القيامة بأفضل مما جاء به إلا أحد قال مثل ما قال أو زاد عليه، وغفرت خطاياه وإن كانت مثل زبد البحر.", "bn" to "কিয়ামতের দিন তার চেয়ে উত্তম আমল নিয়ে কেউ আসতে পারবে না, সমুদ্রের ফেনা সমতুল্য পাপ থাকলেও তা ক্ষমা করে দেওয়া হয়।"),
            hadithArabic = "مَنْ قَالَ حِينَ يُصْبِحُ وَحِينَ يُمْسِي: سُبْحَانَ اللَّهِ وَبِحَمْدِهِ، مِائَةَ مَرَّةٍ، لَمْ يَأْتِ أَحَدٌ يَوْمَ الْقِيَامَةِ بِأَفْضَلَ مِمَّا جَاءَ بِهِ، إِلَّا أَحَدٌ قَالَ مِثْلَ مَا قَالَ أَوْ زَادَ عَلَيْهِ",
            hadithTranslations = mapOf("hi" to "रसूलुल्लाह (ﷺ) ने फ़रमाया: 'जो शख्स सुबह और शाम 100 बार सुब्हानल्लाहि वबिहम्दिही कहे, क़यामत के दिन उससे अफ़ज़ल अमल लेकर कोई नहीं आएगा, सिवाय उसके जिसने इतना या इससे ज़्यादा पढ़ा हो।'", "en" to "The Messenger of Allah (ﷺ) said: 'He who recites in the morning and in the evening: SubhanAllahi wa bihamdihi one hundred times, none will bring on the Day of Resurrection anything better than that except someone who said the same or added more.'", "ur" to "رسول اللہ صلی اللہ علیہ وسلم نے فرمایا: 'جو شخص صبح اور شام سو مرتبہ سبحان اللہ وبحمدہ کہے، قیامت کے دن اس سے افضل عمل لے کر کوئی نہ آئے گا، سوائے اس کے جس نے اتنا ہی یا اس سے زیادہ کہا ہو۔'"),
            hadithNarrator = "Abu Huraira (R.A.)",
            hadithBook = "Sahih Muslim 2692",
            hadithGrade = "Sahih",
            hadithUrl = "https://sunnah.com/muslim:2692",
            order = 4
        ),
        DhikrGuideDetail(
            id = "g5",
            arabic = "تَسْبِيْحُ فَاطِمَةَ: سُبْحَانَ اللّٰہِ (۳۳) + اَلْحَمْدُ لِلّٰہِ (۳۳) + اَللّٰہُ أَكْبَرُ (۳۴)",
            title = mapOf("hi" to "तसबीह-ए-फ़ातिमा — सुब्हानल्लाह 33 + अलहम्दुलिल्लाह 33 + अल्लाहु अकबर 34", "en" to "Tasbih-e-Fatimah — SubhanAllah 33 + Alhamdulillah 33 + Allahu Akbar 34", "ur" to "تسبیح فاطمہ — سبحان اللہ 33 + الحمد للہ 33 + اللہ اکبر 34", "ar" to "تسبيح فاطمة — سبحان الله 33 + الحمد لله 33 + الله أكبر 34", "bn" to "তাসবিহে ফাতিমা — সুবহানাল্লাহ ৩৩ + আলহামদুলিল্লাহ ৩৩ + আল্লাহু আকবার ৩৪"),
            meaning = mapOf("hi" to "अल्लाह पाक है (33 बार), सब तारीफ अल्लाह के लिए है (33 बार), अल्लाह सबसे बड़ा है (34 बार) — कुल 100", "en" to "Glory to Allah (33x), Praise be to Allah (33x), Allah is the Greatest (34x) — Total 100", "ur" to "اللہ پاک ہے (33 بار)، سب تعریف اللہ کے لیے ہے (33 بار)، اللہ سب سے بڑا ہے (34 بار) — کل 100", "ar" to "تنزيه الله وتحميده وتكبيره — المجموع 100", "bn" to "আল্লাহর পবিত্রতা (৩৩ বার), আল্লাহর প্রশংসা (৩৩ বার), আল্লাহ সর্বশ্রেষ্ঠ (৩৪ বার) — মোট ১০০"),
            targetStr = "33+33+34",
            howToRecite = mapOf("en" to "Recite after every obligatory Salah, or at night upon lying in bed before sleeping.", "hi" to "हर फर्ज़ नमाज़ के बाद या रात को सोने से पहले बिस्तर पर लेटकर पढ़ें।", "ur" to "ہر فرض نماز کے بعد یا رات کو بستر پر لیٹ کر سونے سے پہلے پڑھیں۔", "ar" to "دبر كل صلاة مكتوبة، وعند النوم إذا أخذت مضجعك.", "bn" to "প্রত্যেক ফরজ নামাজের পর অথবা রাতে বিছানায় ঘুমানোর আগে পড়ুন।"),
            hadithRef = "Sahih Bukhari 3113",
            virtues = mapOf("hi" to "थकान और कमज़ोरी दूर होती है, रूहानी और जिस्मानी ताक़त मिलती है। नबी ﷺ ने हज़रत फ़ातिमा (रज़ि.) को ख़ादिम से बेहतर यह अता फ़रमाया।", "en" to "Relieves physical and mental exhaustion; provides extraordinary divine energy and blessing. Gifted by Prophet ﷺ to Fatimah (R.A.) instead of a servant.", "ur" to "تھکن دور ہوتی ہے، روحانی اور جسمانی طاقت عطا ہوتی ہے۔ نبی کریم ﷺ نے حضرت فاطمہ رضی اللہ عنہا کو خادم سے بہتر یہ تحفہ عطا فرمایا۔", "ar" to "خير من خادم، يذهب التعب ويكسب القوة والبركة في الجسد والعمل.", "bn" to "ক্লান্তি দূর হয়, আত্মিক ও শারীরিক শক্তি বৃদ্ধি পায়। খাদেমের চেয়েও উত্তম আমল হিসেবে নবীজি ﷺ হযরত ফাতিমা (রা.)-কে শিক্ষা দিয়েছিলেন।"),
            hadithArabic = "أَلاَ أَدُلُّكُمَا عَلَى مَا هُوَ خَيْرٌ لَكُمَا مِنْ خَادِمٍ؟ إِذَا أَوَيْتُمَا إِلَى فِرَاشِكُمَا فَسَبِّحَا ثَلاَثًا وَثَلاَثِينَ، وَاحْمَدَا ثَلاَثًا وَثَلاَثِينَ، وَكَبِّرَا أَرْبَعًا وَثَلاَثِينَ، فَإِنَّهُ خَيْرٌ لَكُمَا مِنْ خَادِمٍ",
            hadithTranslations = mapOf("hi" to "रसूलुल्लाह (ﷺ) ने हज़रत अली और हज़रत फ़ातिमा (रज़ि.) से फ़रमाया: 'क्या मैं तुम्हें ऐसी चीज़ न बताऊं जो ख़ादिम (नौकर) से भी बेहतर हो? जब तुम बिस्तर पर जाओ तो 33 बार सुब्हानल्लाह, 33 बार अलहम्दुलिल्लाह, और 34 बार अल्लाहु अकबर कहो; यह तुम्हारे लिए ख़ादिम से बेहतर है।'", "en" to "The Prophet (ﷺ) said to Ali and Fatimah (R.A.): 'Shall I not guide you to what is better for you than a servant? When you go to your bed, say: SubhanAllah 33 times, Alhamdulillah 33 times, and Allahu Akbar 34 times; that is better for you than a servant.'", "ur" to "رسول اللہ صلی اللہ علیہ وسلم نے فرمایا: 'کیا میں تمہیں ایسی چیز نہ بتاؤں جو خادم سے بہتر ہو؟ جب سونے کے لیے بستر پر جاؤ تو 33 بار سبحان اللہ، 33 بار الحمد للہ اور 34 بار اللہ اکبر کہو، یہ تمہارے لیے خادم سے بہتر ہے۔'"),
            hadithNarrator = "Ali ibn Abi Talib (R.A.)",
            hadithBook = "Sahih al-Bukhari 3113 & Sahih Muslim 2727",
            hadithGrade = "Sahih (Muttafaq 'Alayh)",
            hadithUrl = "https://sunnah.com/bukhari:3113",
            order = 5
        ),
        DhikrGuideDetail(
            id = "g6",
            arabic = "اَللّٰهُمَّ صَلِّ عَلٰى مُحَمَّدٍ وَّعَلٰى آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلٰى إِبْرَاهِيمَ وَعَلٰى آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ",
            title = mapOf("hi" to "अल्लाहुम्मा सल्लि अला मुहम्मदिवं व अला आलि मुहम्मद, कमा सल्लैता अला इब्राहीमा व अला आलि इब्राहीमा इन्नका हमीदुम मजीद", "en" to "Allahumma salli ala Muhammadin wa ala ali Muhammad, kama sallayta ala Ibrahima wa ala ali Ibrahima innaka Hamidum Majid", "ur" to "اللہم صل علی محمد وعلی آل محمد کما صلیت علی ابراہیم وعلی آل ابراہیم انک حمید مجید", "ar" to "اللهم صل على محمد وعلى آل محمد كما صليت على إبراهيم وعلى آل إبراهيم إنك حميد مجيد", "bn" to "আল্লাহুম্মা সাল্লি আলা মুহাম্মাদিওঁ ওয়া আলা আলি মুহাম্মাদ, কামা সাল্লাইতা আলা ইবরাহীমা ওয়া আলা আলি ইবরাহীমা ইন্নাকা হামীদুম মাজীদ"),
            meaning = mapOf("hi" to "ऐ अल्लाह! रहमत नाज़िल फ़रमा हज़रत मुहम्मद ﷺ पर और उनकी आल पर, जैसे तूने रहमत नाज़िल फ़रमाई हज़रत इब्राहीम (अ.) पर और उनकी आल पर, बेशक तू काबिले-तारीफ़ और बुज़ुर्ग है।", "en" to "O Allah, send blessings upon Muhammad and the family of Muhammad, as You sent blessings upon Ibrahim and the family of Ibrahim. Indeed, You are Praiseworthy, Full of Glory.", "ur" to "اے اللہ! رحمت نازل فرما حضرت محمد ﷺ پر اور ان کی آل پر، جیسا کہ تو نے رحمت نازل فرمائی حضرت ابراہیم علیہ السلام اور ان کی آل پر، بیشک تو تعریف کے لائق اور بزرگی والا ہے۔", "ar" to "طلب الرحمة والبركة من الله لنبيه محمد وآله كما بارك لإبراهيم وآله", "bn" to "হে আল্লাহ! মুহাম্মদ ﷺ এবং তাঁর পরিবারের উপর রহমত বর্ষণ করুন, যেমন আপনি ইবরাহীম (আ.) ও তাঁর পরিবারের উপর রহমত বর্ষণ করেছিলেন।"),
            targetStr = "10×",
            howToRecite = mapOf("en" to "Recite 10 times morning and evening, abundantly on Fridays, and at the start and end of every Dua.", "hi" to "सुबह और शाम 10 बार, ख़ासकर जुमा के दिन कसरत से, और हर दुआ के शुरू व आखिर में पढ़ें।", "ur" to "صبح و شام 10 بار، جمعہ کے دن کثرت سے، اور ہر دعا کے اول و آخر میں پڑھیں۔", "ar" to "عشر مرات صباحاً ومساءً، والإكثار منه يوم الجمعة وعند الدعاء.", "bn" to "সকাল ও সন্ধ্যায় ১০ বার, বিশেষ করে জুমার দিনে বেশি বেশি এবং প্রতিটি দোয়ার শুরুতে ও শেষে পড়ুন।"),
            hadithRef = "Sahih Muslim 408",
            virtues = mapOf("hi" to "1 बार दरूद पढ़ने पर अल्लाह 10 रहमतें नाज़िल फ़रमाता है, 10 गुनाह माफ़ करता है, और 10 दर्जात बुलंद फ़रमाता है।", "en" to "Whoever sends blessings upon the Prophet ﷺ once, Allah sends 10 blessings upon him, erases 10 sins, and elevates his status by 10 degrees.", "ur" to "ایک بار درود پڑھنے پر اللہ تعالیٰ 10 رحمتیں نازل فرماتا ہے، 10 گناہ معاف کرتا ہے، اور 10 درجات بلند فرماتا ہے۔", "ar" to "من صلى عليّ واحدة صلى الله عليه بها عشراً، وحطت عنه عشر خطيئات، ورفعت له عشر درجات.", "bn" to "যে ব্যক্তি একবার দরূদ পড়ে, আল্লাহ তার উপর ১০টি রহমত বর্ষণ করেন, ১০টি পাপ মোচন করেন এবং তার মর্যাদা ১০ গুণ বৃদ্ধি করেন।"),
            hadithArabic = "مَنْ صَلَّى عَلَىَّ وَاحِدَةً صَلَّى اللَّهُ عَلَيْهِ عَشْرًا",
            hadithTranslations = mapOf("hi" to "रसूलुल्लाह (ﷺ) ने फ़रमाया: 'जो शख्स मुझ पर एक बार दरूद भेजता है, अल्लाह उस पर दस रहमतें नाज़िल फ़रमाता है।'", "en" to "The Messenger of Allah (ﷺ) said: 'Whoever sends blessings upon me once, Allah will send blessings upon him ten times.'", "ur" to "رسول اللہ صلی اللہ علیہ وسلم نے فرمایا: 'جس نے مجھ پر ایک بار درود بھیجا، اللہ تعالیٰ اس پر دس رحمتیں نازل فرمائے گا۔'"),
            hadithNarrator = "Abu Huraira (R.A.)",
            hadithBook = "Sahih Muslim 408 & Sunan an-Nasa'i 1297",
            hadithGrade = "Sahih",
            hadithUrl = "https://sunnah.com/muslim:408",
            order = 6
        ),
        DhikrGuideDetail(
            id = "g7",
            arabic = "لَا إِلٰهَ إِلَّا أَنْتَ سُبْحَانَكَ إِنِّي كُنْتُ مِنَ الظَّالِمِينَ",
            title = mapOf("hi" to "ला इलाहा इल्ला अन्ता सुब्हानका इन्नी कुन्तु मिनज़-ज़ालिमीन", "en" to "La ilaha illa Anta subhanaka inni kuntu minaz-zalimin", "ur" to "لا الہ الا انت سبحانک انی کنت من الظالمین", "ar" to "لا إله إلا أنت سبحانك إني كنت من الظالمين", "bn" to "লা ইলাহা ইল্লা আনতা সুবহানাকা ইন্নি কুনতু মিনায-যালিমীন"),
            meaning = mapOf("hi" to "तेरे सिवा कोई सच्चा माबूद नहीं, तू पाक है, बेशक मैं ही ज़ालिमों (क़ुसूरवारों) में से था", "en" to "There is no deity except You; exalted are You. Indeed, I have been of the wrongdoers", "ur" to "تیرے سوا کوئی معبود نہیں، تو پاک ہے، بیشک میں ہی قصورواروں میں سے تھا", "ar" to "اعتراف بالتوحيد وتنزيه الله والاعتراف بالذنب", "bn" to "আপনি ছাড়া কোনো উপাস্য নেই, আপনি পরম পবিত্র, নিশ্চয়ই আমি অন্যায়কারীদের অন্তর্ভুক্ত ছিলাম"),
            targetStr = "40×",
            howToRecite = mapOf("en" to "Recite 40 times during difficulty, in Sujood, or repeat continuously when feeling overwhelmed.", "hi" to "मुसीबत या परेशानी के वक्त 40 बार पढ़ें, सजदे में पढ़ें, या कसरत से विर्द करें।", "ur" to "مصیبت یا پریشانی کے وقت 40 بار پڑھیں، سجدے میں پڑھیں، یا کثرت سے ورد کریں۔", "ar" to "يُقرأ أربعين مرة عند الشدائد والكرب، أو في السجود.", "bn" to "বিপদ বা সংকটের সময় ৪০ বার পড়ুন, সিজদায় পড়ুন অথবা বেশি বেশি জিকির করুন।"),
            hadithRef = "Jami` at-Tirmidhi 3505",
            virtues = mapOf("hi" to "मुसीबत, ग़म, क़र्ज़, परेशानी या बीमारी से नजात। हज़रत यूनुस (अ.) को मछली के पेट से नजात मिली। जो मुसलमान इस दुआ से मांगे, अल्लाह उसकी दुआ क़ुबूल फ़रमाता है।", "en" to "Immediate relief from depression, grief, debt, distress, and incurable situations. Allah rescued Yunus (A.S.) from the whale's belly through this Dua.", "ur" to "مصیبت، پریشانی، غم، قرض اور بیماری سے نجات۔ حضرت یونس علیہ السلام کو مچھلی کے پیٹ سے رہائی ملی۔ جو مسلمان اس کے ذریعے مانگے دعا قبول ہوتی ہے۔", "ar" to "دعوة ذي النون ما دعا بها رجل مسلم في شيء قط إلا استجاب الله له، تفرج الكروب وتكشف الغموم.", "bn" to "বিপদ, দুশ্চিন্তা ও রোগমুক্তি। ইউনুস (আ.) মাছের পেট থেকে এই দোয়ার বরকতে মুক্তি পেয়েছিলেন। যে মুসলিম এই দোয়া পড়ে, আল্লাহ তার দোয়া কবুল করেন।"),
            hadithArabic = "دَعْوَةُ ذِي النُّونِ إِذْ دَعَا وَهُوَ فِي بَطْنِ الْحُوتِ: لاَ إِلَهَ إِلاَّ أَنْتَ سُبْحَانَكَ إِنِّي كُنْتُ مِنَ الظَّالِمِينَ. فَإِنَّهُ لَمْ يَدْعُ بِهَا رَجُلٌ مُسْلِمٌ فِي شَيْءٍ قَطُّ إِلاَّ اسْتَجَابَ اللَّهُ لَهُ",
            hadithTranslations = mapOf("hi" to "रसूलुल्लाह (ﷺ) ने फ़रमाया: 'मछली वाले हज़रत यूनुस (अ.) की दुआ जब वह मछली के पेट में थे: ला इलाहा इल्ला अन्ता सुब्हानका इन्नी कुन्तु मिनज़-ज़ालिमीन थी। कोई भी मुसलमान जब किसी परेशानी में इसके ज़रिए दुआ मांगता है, अल्लाह उसकी दुआ ज़रूर क़ुबूल फ़रमाता है।'", "en" to "The Messenger of Allah (ﷺ) said: 'The supplication of Dhun-Nun (Yunus) when he called upon Allah inside the belly of the fish was: La ilaha illa Anta subhanaka inni kuntu minaz-zalimin. Indeed, no Muslim supplication with it for anything, ever, except that Allah responds to him.'", "ur" to "رسول اللہ صلی اللہ علیہ وسلم نے فرمایا: 'حضرت یونس علیہ السلام کی مچھلی کے پیٹ میں دعا: لا الہ الا انت سبحانک انی کنت من الظالمین تھی۔ جو بھی مسلمان کسی چیز کے لیے یہ دعا کرے گا تو اللہ اس کی دعا ضرور قبول فرمائے گا۔'"),
            hadithNarrator = "Sa'd ibn Abi Waqqas (R.A.)",
            hadithBook = "Jami` at-Tirmidhi 3505",
            hadithGrade = "Sahih",
            hadithUrl = "https://sunnah.com/tirmidhi:3505",
            order = 7
        ),
        DhikrGuideDetail(
            id = "g8",
            arabic = "حَسْبُنَا اللّٰہُ وَنِعْمَ الْوَكِيْلُ",
            title = mapOf("hi" to "हस्बुनल्लाहु व नि'मल वकील", "en" to "HasbunAllahu wa ni'mal wakeel", "ur" to "حسبنا اللہ و نعم الوکیل", "ar" to "حسبنا الله ونعم الوكيل", "bn" to "হাসবুনাল্লাহু ওয়া নি'মাল ওয়াকীল"),
            meaning = mapOf("hi" to "हमारे लिए अल्लाह काफी है और वह बेहतरीन कारसाज़ (मददगार) है", "en" to "Sufficient for us is Allah, and He is the best Disposer of affairs", "ur" to "ہمارے لیے اللہ کافی ہے اور وہ بہترین کارساز ہے", "ar" to "الله كافينا وهو نعم المولى ونعم النصير", "bn" to "আমাদের জন্য আল্লাহই যথেষ্ট এবং তিনি কতই না উত্তম কর্মবিধায়ক"),
            targetStr = "7× / 100×",
            howToRecite = mapOf("en" to "Recite 7 times in the morning and evening, or 100 times when feeling helpless or anxious.", "hi" to "सुबह और शाम 7 बार पढ़ें, या जब भी डर, बेबसी या तनाव महसूस हो तो 100 बार पढ़ें।", "ur" to "صبح و شام 7 بار پڑھیں، یا جب بھی خوف، بے بسی یا پریشانی ہو تو 100 بار پڑھیں۔", "ar" to "سبع مرات صباحاً ومساءً أو مائة مرة عند الخوف والشدائد.", "bn" to "সকাল ও সন্ধ্যায় ৭ বার পড়ুন, অথবা ভয়, অসহায়ত্ব বা উদ্বেগের সময় ১০০ বার পড়ুন।"),
            hadithRef = "Sahih Bukhari 4563",
            virtues = mapOf("hi" to "दुश्मन के डर, बेबसी, चिंता और मुक़द्दमेबाज़ी से हिफ़ाज़त। हज़रत इब्राहीम (अ.) को जब आग में डाला गया तो उन्होंने यही पढ़ा और आग ठंडी व सलामती बन गई।", "en" to "Ultimate protection against enemies, helplessness, dread, and difficult odds. Ibrahim (A.S.) said it when thrown into the blazing fire, and it turned cool and peaceful.", "ur" to "دشمن کے خوف، بے بسی اور خطرات سے حفاظت۔ حضرت ابراہیم علیہ السلام کو آگ میں ڈالا گیا تو یہی پڑھا اور آگ ٹھنڈی سلامتی والی بن گئی۔", "ar" to "أمان من الخوف، قالها إبراهيم عليه السلام حين ألقي في النار فكانت برداً وسلاماً، وقالها النبي ﷺ وأصحابه بعد أحد.", "bn" to "শত্রুর ভয়, অসহায়ত্ব ও বিপদ থেকে সুরক্ষা। ইবরাহীম (আ.)-কে যখন আগুনে নিক্ষেপ করা হয় তখন তিনি এটি পড়েছিলেন এবং আগুন শীতল ও শান্তিদায়ক হয়েছিল।"),
            hadithArabic = "حَسْبُنَا اللَّهُ وَنِعْمَ الْوَكِيلُ قَالَهَا إِبْرَاهِيمُ عَلَيْهِ السَّلاَمُ حِينَ أُلْقِيَ فِي النَّارِ، وَقَالَهَا مُحَمَّدٌ صلى الله عليه وسلم حِينَ قَالُوا: إِنَّ النَّاسَ قَدْ جَمَعُوا لَكُمْ فَاخْشَوْهُمْ فَزَادَهُمْ إِيمَانًا وَقَالُوا حَسْبُنَا اللَّهُ وَنِعْمَ الْوَكِيلُ",
            hadithTranslations = mapOf("hi" to "हज़रत इब्ने अब्बास (रज़ि.) ने फ़रमाया: 'हस्बुनल्लाहु व नि'मल वकील हज़रत इब्राहीम (अ.) ने उस वक्त फ़रमाया जब उन्हें आग में डाला गया, और मुहम्मद ﷺ ने उस वक्त फ़रमाया जब लोगों ने कहा: लश्कर जमा हो चुका है, उनसे डरो; तो उनका ईमान और बढ़ गया और उन्होंने कहा: हमारे लिए अल्लाह काफी है और वह बेहतरीन कारसाज़ है।'", "en" to "Ibn Abbas (R.A.) said: 'HasbunAllahu wa ni'mal wakeel was said by Ibrahim when he was thrown into the fire; and it was said by Muhammad (ﷺ) when the people said: Verily, the people have gathered against you, therefore, fear them. But it increased their faith and they said: Allah is Sufficient for us, and He is the Best Disposer of affairs.'", "ur" to "حضرت ابن عباس رضی اللہ عنہما نے فرمایا: 'حسبنا اللہ ونعم الوکیل حضرت ابراہیم علیہ السلام نے فرمایا جب انہیں آگ میں ڈالا گیا، اور حضرت محمد صلی اللہ علیہ وسلم نے فرمایا جب لوگوں نے کہا کہ کافروں کا لشکر جمع ہو گیا ہے، تو ان کا ایمان اور بڑھ گیا اور انہوں نے کہا: ہمارے لیے اللہ کافی ہے اور وہ بہترین کارساز ہے۔'"),
            hadithNarrator = "Ibn Abbas (R.A.)",
            hadithBook = "Sahih al-Bukhari 4563",
            hadithGrade = "Sahih",
            hadithUrl = "https://sunnah.com/bukhari:4563",
            order = 8
        ),
        DhikrGuideDetail(
            id = "g9",
            arabic = "اَللّٰهُمَّ أَنْتَ رَبِّي لَا إِلٰهَ إِلَّا أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلٰى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوْذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهٗ لَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ",
            title = mapOf("hi" to "अल्लाहुम्मा अन्ता रब्बी ला इलाहा इल्ला अन्ता, खलक़्तनी व अना अब्दुका, व अना अला अहदिका व वा'दिका मसततअ'तु, अऊज़ु बिका मिन शर्रि मा सनअ'तु, अबूउ लका बि नि'मतिका अलैया, व अबूउ बिज़म्बी फ़ग़फ़िर ली फ़इन्नहू ला यग़फ़िरुज़-ज़ुनूबा इल्ला अन्ता", "en" to "Allahumma Anta Rabbi la ilaha illa Anta, khalaqtani wa ana abduka, wa ana ala ahdika wa wa'dika mastata'tu, a'udhu bika min sharri ma sana'tu, abu'u laka bini'matika alayya, wa abu'u bidhanbi faghfir li fainnahu la yaghfirudh-dhunuba illa Anta", "ur" to "اللہم انت ربی لا الہ الا انت، خلقتنی وانا عبدک، وانا علی عھدک ووعدک ما استطعت، اعوذ بک من شر ما صنعت، ابوء لک بنعمتک علی، وابوء بذنبی فاغفر لی فانہ لا یغفر الذنوب الا انت", "ar" to "سيد الاستغفار — توبة كاملة وإقرار بالعبودية والذنب", "bn" to "আল্লাহুম্মা আনতা রব্বী লা ইলাহা ইল্লা আনতা, খালাকতানী ওয়া আনা আবদুকা, ওয়া আনা আলা আহদিকা ওয়া ওয়াদিকা মাস্তাতাতু..."),
            meaning = mapOf("hi" to "ऐ अल्लाह! तू मेरा रब है, तेरे सिवा कोई माबूद नहीं, तूने मुझे पैदा किया और मैं तेरा बंदा हूँ, और मैं अपनी ताक़त के मुताबिक़ तेरे अहद और वादे पर क़ायम हूँ। मैं अपने आमाल के शर से तेरी पनाह चाहता हूँ, मुझ पर तेरी जो नेमतें हैं उनका एतराफ़ करता हूँ और अपने गुनाहों का इक़रार करता हूँ, मुझे माफ़ फ़रमा दे क्योंकि तेरे सिवा कोई गुनाहों को माफ़ नहीं कर सकता।", "en" to "O Allah, You are my Lord; none has the right to be worshipped but You. You created me and I am Your servant, and I remain upon Your covenant and promise as much as I am able. I seek refuge in You from the evil of what I have done. I acknowledge before You Your favor upon me, and I acknowledge my sin, so forgive me, for indeed none forgives sins except You.", "ur" to "اے اللہ! تو میرا رب ہے، تیرے سوا کوئی معبود نہیں، تو نے مجھے پیدا کیا اور میں تیرا بندہ ہوں، اور میں اپنی طاقت کے مطابق تیرے عہد اور وعدے پر قائم ہوں۔ میں اپنے اعمال کے شر سے تیری پناہ مانگتا ہوں، اپنے اوپر تیری نعمتوں کا اقرار کرتا ہوں اور اپنے گناہوں کا اعتراف کرتا ہوں، پس مجھے معاف فرما دے کیونکہ تیرے سوا کوئی گناہوں کو معاف نہیں کر سکتا۔", "ar" to "سيد الاستغفار وأعظم صيغ التوبة والمغفرة", "bn" to "হে আল্লাহ! আপনি আমার রব, আপনি ছাড়া কোনো সত্য উপাস্য নেই। আপনি আমাকে সৃষ্টি করেছেন এবং আমি আপনার বান্দা..."),
            targetStr = "1×",
            howToRecite = mapOf("en" to "Recite 1 time every morning after Fajr and 1 time every evening after Maghrib with complete sincerity.", "hi" to "सुबह फज्र के बाद 1 बार और शाम मगरिब के बाद 1 बार दिल के पूरे यक़ीन और तवज्जोह से पढ़ें।", "ur" to "صبح فجر کے بعد 1 بار اور شام مغرب کے بعد 1 بار پورے دل کے یقین سے پڑھیں۔", "ar" to "مرة واحدة صباحاً بعد الفجر ومرة مساءً بعد المغرب بيقين تام.", "bn" to "সকালে ফজরের পর ১ বার এবং সন্ধ্যায় মাগরিবের পর ১ বার পূর্ণ বিশ্বাসের সাথে পড়ুন।"),
            hadithRef = "Sahih Bukhari 6306",
            virtues = mapOf("hi" to "तौबा का सरदार (सैयदुल इस्तिग़फ़ार)। जो शख्स दिन में यक़ीन से पढ़े और शाम से पहले इंतक़ाल हो जाए, या रात को पढ़े और सुबह से पहले इंतक़ाल हो जाए तो वह जन्नती है।", "en" to "The Master Supplication for Forgiveness. Whoever says it during the day with firm faith and dies before evening, or says it at night and dies before morning, will be among the people of Jannah.", "ur" to "سید الاستغفار۔ جو شخص دن میں یقین کے ساتھ پڑھے اور شام سے پہلے وفات پا جائے یا رات کو پڑھے اور صبح سے پہلے وفات پا جائے تو وہ جنتی ہے۔", "ar" to "من قالها موقناً بها حين يمسي فمات من ليلته دخل الجنة، ومن قالها موقناً بها حين يصبح فمات من يومه دخل الجنة.", "bn" to "সাইয়্যিদুল ইস্তিগফার (ক্ষমা প্রার্থনার শ্রেষ্ঠ দোয়া)। যে ব্যক্তি দিনে দৃঢ় বিশ্বাসের সাথে পড়ে সন্ধ্যায় মারা যায় বা রাতে পড়ে সকালে মারা যায়, সে জান্নাতি হবে।"),
            hadithArabic = "سَيِّدُ الاِسْتِغْفَارِ أَنْ تَقُولَ: اللَّهُمَّ أَنْتَ رَبِّي لاَ إِلَهَ إِلاَّ أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَىَّ وَأَبُوءُ لَكَ بِذَنْبِي، فَاغْفِرْ لِي، فَإِنَّهُ لاَ يَغْفِرُ الذُّنُوبَ إِلاَّ أَنْتَ. مَنْ قَالَهَا مِنَ النَّهَارِ مُوقِنًا بِهَا، فَمَاتَ مِنْ يَوْمِهِ قَبْلَ أَنْ يُمْسِيَ، فَهُوَ مِنْ أَهْلِ الْجَنَّةِ",
            hadithTranslations = mapOf("hi" to "रसूलुल्लाह (ﷺ) ने फ़रमाया: 'सैयदुल इस्तिग़फ़ार यह है कि बंदा कहे: अल्लाहुम्मा अन्ता रब्बी... जो शख्स दिन के वक्त पूरे यक़ीन से यह कहे और शाम होने से पहले फ़ौत हो जाए, वह अहले-जन्नत में से है; और जो रात को पूरे यक़ीन से कहे और सुबह से पहले फ़ौत हो जाए, वह अहले-जन्नत में से है।'", "en" to "The Prophet (ﷺ) said: 'The most superior way of asking for forgiveness is to say: Allahumma Anta Rabbi la ilaha illa Anta... If somebody recites it during the day with firm faith in it, and dies on the same day before evening, he will be from the people of Paradise; and if somebody recites it at night with firm faith and dies before morning, he will be from the people of Paradise.'", "ur" to "رسول اللہ صلی اللہ علیہ وسلم نے فرمایا: 'سید الاستغفار یہ ہے کہ کہے: اللہم انت ربی... جس نے دن کے وقت کامل یقین کے ساتھ یہ پڑھا اور شام سے پہلے وفات پا گیا تو وہ اہل جنت میں سے ہے، اور جس نے رات کو پڑھا اور صبح سے پہلے وفات پا گیا تو وہ اہل جنت میں سے ہے۔'"),
            hadithNarrator = "Shaddad ibn Aws (R.A.)",
            hadithBook = "Sahih al-Bukhari 6306",
            hadithGrade = "Sahih",
            hadithUrl = "https://sunnah.com/bukhari:6306",
            order = 9
        ),
        DhikrGuideDetail(
            id = "g10",
            arabic = "آيَةُ الْكُرْسِيِّ: اَللّٰهُ لَا إِلٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَّلَا نَوْمٌ ۚ لَهٗ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ...",
            title = mapOf("hi" to "आयतुल कुर्सी — अल्लाहु ला इलाहा इल्ला हुवल हय्युल क़य्यूम...", "en" to "Ayat al-Kursi — Allahu la ilaha illa Huwal Hayyul Qayyum...", "ur" to "آیت الکرسی — اللہ لا الہ الا ہو الحی القیوم...", "ar" to "آية الكرسي دبر كل صلاة مكتوبة", "bn" to "আয়াতুল কুরসি — আল্লাহু লা ইলাহা ইল্লা হুয়াল হাইয়্যুল কাইয়্যুম..."),
            meaning = mapOf("hi" to "अल्लाह, जिसके सिवा कोई माबूद नहीं, वह ज़िंदा है और सब को संभालने वाला है। न उसे ऊंघ आती है न नींद...", "en" to "Allah! There is no deity except Him, the Ever-Living, the Sustainer of all existence. Neither drowsiness overtakes Him nor sleep...", "ur" to "اللہ، جس کے سوا کوئی معبود نہیں، وہ زندہ ہے اور سب کا سنبھالنے والا ہے۔ نہ اسے اونگھ آتی ہے نہ نیند...", "ar" to "أعظم آية في كتاب الله تعالى", "bn" to "আল্লাহ, তিনি ছাড়া কোনো উপাস্য নেই, তিনি চিরঞ্জীব, সর্বসত্তার ধারক..."),
            targetStr = "1×",
            howToRecite = mapOf("en" to "Recite 1 time immediately after concluding each of the five obligatory (Fard) prayers, and 1 time before sleeping.", "hi" to "पाँचों वक़्त की फर्ज़ नमाज़ के सलाम फेरने के तुरंत बाद 1 बार, और रात को सोने से पहले 1 बार पढ़ें।", "ur" to "پانچوں وقت کی فرض نماز کے سلام کے فوراً بعد 1 بار، اور رات کو سوتے وقت 1 بار پڑھیں۔", "ar" to "مرة واحدة دبر كل صلاة مكتوبة، ومرة عند النوم.", "bn" to "পাঁচ ওয়াক্ত ফরজ সালাতের সালাম ফেরানোর পর ১ বার এবং রাতে ঘুমানোর আগে ১ বার পড়ুন।"),
            hadithRef = "Sunan an-Nasa'i 9848",
            virtues = mapOf("hi" to "जो शख्स हर फर्ज़ नमाज़ के बाद आयतुल कुर्सी पढ़े, उसे जन्नत में दाखिल होने से सिवाय मौत के कोई चीज़ नहीं रोक सकती। घर में पढ़ने से शैतान दाखिल नहीं होता।", "en" to "Whoever recites Ayat al-Kursi after every obligatory prayer, nothing prevents him from entering Paradise except death. Recited before sleep shields from Shaytan.", "ur" to "جس نے ہر فرض نماز کے بعد آیت الکرسی پڑھی، اسے جنت میں داخل ہونے سے سوائے موت کے کوئی چیز نہیں روک سکتی۔ رات کو پڑھنے سے صبح تک حفاظت رہتی ہے۔", "ar" to "من قرأ آية الكرسي دبر كل صلاة مكتوبة لم يمنعه من دخول الجنة إلا أن يموت، وحفظ من الشيطان حتى يصبح.", "bn" to "যে ব্যক্তি প্রত্যেক ফরজ সালাতের পর আয়াতুল কুরসি পাঠ করবে, তার জান্নাতে প্রবেশ করতে মৃত্যু ছাড়া আর কোনো বাধা থাকবে না।"),
            hadithArabic = "مَنْ قَرَأَ آيَةَ الْكُرْسِيِّ دُبُرَ كُلِّ صَلاَةٍ مَكْتُوبَةٍ لَمْ يَمْنَعْهُ مِنْ دُخُولِ الْجَنَّةِ إِلاَّ أَنْ يَمُوتَ",
            hadithTranslations = mapOf("hi" to "रसूलुल्लाह (ﷺ) ने फ़रमाया: 'जो शख्स हर फर्ज़ नमाज़ के बाद आयतुल कुर्सी पढ़ेगा, उसे जन्नत में जाने से सिर्फ मौत रोक रही है (यानी मौत के तुरंत बाद जन्नती होगा)।'", "en" to "The Prophet (ﷺ) said: 'Whoever recites Ayat al-Kursi after every obligatory prayer, nothing stands between him and his entering Paradise except death.'", "ur" to "رسول اللہ صلی اللہ علیہ وسلم نے فرمایا: 'جس شخص نے ہر فرض نماز کے بعد آیت الکرسی پڑھی، اسے جنت میں داخل ہونے سے موت کے سوا کوئی چیز نہیں روکتی۔'"),
            hadithNarrator = "Abu Umamah (R.A.)",
            hadithBook = "Sunan an-Nasa'i (as-Sunan al-Kubra 9848) & Tabarani",
            hadithGrade = "Sahih (Albani)",
            hadithUrl = "https://sunnah.com/nasai",
            order = 10
        ),
        DhikrGuideDetail(
            id = "g11",
            arabic = "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللّٰہِ الْعَلِيِّ الْعَظِيْمِ",
            title = mapOf("hi" to "ला हवला वला कुव्वता इल्ला बिल्लाहिल अलिय्यिल अज़ीम", "en" to "La hawla wa la quwwata illa billahil Aliyyil Azim", "ur" to "لا حول ولا قوة الا باللہ العلی العظیم", "ar" to "لا حول ولا قوة إلا بالله العلي العظيم", "bn" to "লা হাওলা ওয়ালা কুওয়াতা ইল্লা বিল্লাহিল আলিয়্যিল আজিম"),
            meaning = mapOf("hi" to "बुराई से बचने की ताक़त और नेकी करने की क़ुव्वत सिर्फ अल्लाह की मदद से है, जो बुलंद और अज़ीम है", "en" to "There is no might nor power except with Allah, the Most High, the Most Supreme", "ur" to "گناہ سے بچنے اور نیکی کرنے کی طاقت صرف اللہ کی مدد سے ہے، جو بلند اور بڑا ہے", "ar" to "لا تحول من معصية ولا قوة على طاعة إلا بتوفيق الله وعونه", "bn" to "আল্লাহর সাহায্য ছাড়া পাপ থেকে বাঁচার কোনো উপায় এবং নেক কাজ করার কোনো শক্তি নেই"),
            targetStr = "∞ / 100×",
            howToRecite = mapOf("en" to "Recite frequently whenever feeling helpless, weak, anxious, or facing a tough task.", "hi" to "जब भी लाचारी, कमज़ोरी, परेशानी या मुश्किल काम सामने हो, कसरत से इसका विर्द करें।", "ur" to "جب بھی لاچاری، کمزوری، وسوسہ یا مشکل کام درپیش ہو، کثرت سے ورد کریں۔", "ar" to "يُكثر منه عند العجز والضعف والشدائد ودفع الوساوس.", "bn" to "অসহায়ত্ব, দুর্বলতা, দুশ্চিন্তা বা কঠিন কাজের মুখোমুখি হলে বেশি বেশি পড়ুন।"),
            hadithRef = "Sahih Bukhari 6384",
            virtues = mapOf("hi" to "जन्नत के अर्श के नीचे से अनमोल ख़ज़ाना है। 99 बीमारियों और परेशानियों की दवा है, जिसमें सबसे हल्की बीमारी ग़म और डिप्रेशन है।", "en" to "A treasure from beneath the Throne of Allah in Paradise. A remedy for 99 ailments, the least of which is anxiety and sorrow.", "ur" to "جنت کے خزانوں میں سے ایک انمول خزانہ ہے۔ 99 بیماریوں کا علاج ہے جن میں سب سے ادنیٰ بیماری غم اور پریشانی ہے۔", "ar" to "كنز من كنوز الجنة تحت العرش، ودواء لتسعة وتسعين داءً أيسرها الهم.", "bn" to "জান্নাতের আরশের নিচে সঞ্চিত অমূল্য রত্নভাণ্ডার। ৯৯টি রোগের ওষুধ, যার মধ্যে সবচেয়ে সাধারণ রোগ হলো দুশ্চিন্তা ও বিষণ্ণতা।"),
            hadithArabic = "يَا عَبْدَ اللَّهِ بْنَ قَيْسٍ، أَلاَ أَدُلُّكَ عَلَى كَنْزٍ مِنْ كُنُوزِ الْجَنَّةِ؟ فَقُلْتُ: بَلَى يَا رَسُولَ اللَّهِ. قَالَ: قُلْ: لاَ حَوْلَ وَلاَ قُوَّةَ إِلاَّ بِاللَّهِ",
            hadithTranslations = mapOf("hi" to "रसूलुल्लाह (ﷺ) ने हज़रत अबू मूसा (रज़ि.) से फ़रमाया: 'ऐ अब्दुल्लाह बिन क़ैस! क्या मैं तुम्हें जन्नत के ख़ज़ानों में से एक ख़ज़ाना न बताऊं?' मैंने अर्ज़ किया: ज़रूर या रसूलुल्लाह! फ़रमाया: 'कहो: ला हवला वला कुव्वता इल्ला बिल्लाह।'", "en" to "The Prophet (ﷺ) said to Abu Musa: 'O Abdullah bin Qais, shall I not inform you of a treasure from the treasures of Paradise?' I said: 'Yes, O Messenger of Allah.' He said: 'Say: La hawla wa la quwwata illa billah.'", "ur" to "رسول اللہ صلی اللہ علیہ وسلم نے حضرت ابو موسیٰ اشعری رضی اللہ عنہ سے فرمایا: 'اے عبداللہ بن قیس! کیا میں تمہیں جنت کے خزانوں میں سے ایک خزانہ نہ بتاؤں؟' میں نے عرض کیا: ضرور یا رسول اللہ! فرمایا: 'کہو: لا حول ولا قوة الا باللہ۔'"),
            hadithNarrator = "Abu Musa al-Ash'ari (R.A.)",
            hadithBook = "Sahih al-Bukhari 6384 & Sahih Muslim 2704",
            hadithGrade = "Sahih (Muttafaq 'Alayh)",
            hadithUrl = "https://sunnah.com/bukhari:6384",
            order = 11
        ),
        DhikrGuideDetail(
            id = "g12",
            arabic = "أَسْتَغْفِرُ اللّٰہَ وَأَتُوبُ إِلَيْهِ",
            title = mapOf("hi" to "अस्तग़फ़िरुल्लाहा व अतूबु इलैह", "en" to "Astaghfirullaha wa atubu ilayh", "ur" to "استغفر اللہ و اتوب الیہ", "ar" to "أستغفر الله وأتوب إليه", "bn" to "আস্তাগফিরুল্লাহ ওয়া আতুবু ইলাইহ"),
            meaning = mapOf("hi" to "मैं अल्लाह से बख़्शिश (माफ़ी) तलब करता हूँ और उसी के हुज़ूर तौबा करता हूँ", "en" to "I seek the forgiveness of Allah and I turn to Him in repentance", "ur" to "میں اللہ سے بخشش طلب کرتا ہوں اور اسی کے حضور توبہ کرتا ہوں", "ar" to "طلب المغفرة والرجوع إلى الله تعالى بالتوبة الصادقة", "bn" to "আমি আল্লাহর কাছে ক্ষমা চাই এবং তাঁরই দিকে তওবা করে ফিরে আসি"),
            targetStr = "70× / 100×",
            howToRecite = mapOf("en" to "Recite 100 times daily as part of morning and evening routines or during any free moment.", "hi" to "रोज़ाना कम से कम 70 से 100 बार पढ़ें, जब भी खाली वक्त मिले दिल से तौबा करें।", "ur" to "روزانہ کم از کم 70 سے 100 بار پڑھیں، ہر نماز کے بعد اور فراغت کے لمحات میں ورد کریں۔", "ar" to "مائة مرة يومياً تأسياً بالنبي صلى الله عليه وسلم.", "bn" to "প্রতিদিন অন্তত ৭০ থেকে ১০০ বার পড়ুন, অবসর সময়ে খাঁটি অন্তরে তওবা করুন।"),
            hadithRef = "Sahih Bukhari 6307",
            virtues = mapOf("hi" to "रिज़्क़ में कसरत और बरकत, ग़मों और तंगहाली से नजात, गुनाहों की बख़्शिश, और रूहानी सुकून। नबी ﷺ रोज़ाना 100 बार इस्तिग़फ़ार फ़रमाते थे।", "en" to "Blessing and abundance in sustenance, liberation from sorrow, anxiety, and hardship. Prophet ﷺ sought forgiveness over 70 to 100 times daily.", "ur" to "رزق میں برکت اور فراخی، ہر غم اور تنگی سے نجات، گناہوں کی بخشش۔ نبی کریم ﷺ روزانہ 100 بار استغفار فرماتے تھے۔", "ar" to "سعة الرزق، وتفريج الهموم، وفتح الأبواب المغلقة، ومغفرة الذنوب والخطايا.", "bn" to "রিজিকে বরকত, সকল চিন্তা ও সংকীর্ণতা থেকে মুক্তি, গুনাহ মাফ। নবীজি ﷺ প্রতিদিন ৭০ থেকে ১০০ বারেরও বেশি তওবা-ইস্তিগফার করতেন।"),
            hadithArabic = "وَاللَّهِ إِنِّي لأَسْتَغْفِرُ اللَّهَ وَأَتُوبُ إِلَيْهِ فِي الْيَوْمِ أَكْثَرَ مِنْ سَبْعِينَ مَرَّةً",
            hadithTranslations = mapOf("hi" to "रसूलुल्लाह (ﷺ) ने फ़रमाया: 'अल्लाह की क़सम! मैं दिन में सत्तर (70) से भी ज़्यादा बार अल्लाह से इस्तिग़फ़ार (माफ़ी) तलब करता हूँ और उसकी तरफ़ रुजू (तौबा) करता हूँ।'", "en" to "The Messenger of Allah (ﷺ) said: 'By Allah! I ask for forgiveness from Allah and turn to Him in repentance more than seventy times a day.'", "ur" to "رسول اللہ صلی اللہ علیہ وسلم نے فرمایا: 'اللہ کی قسم! میں دن میں ستر مرتبہ سے زیادہ اللہ سے بخشش مانگتا ہوں اور اس کے حضور توبہ کرتا ہوں۔'"),
            hadithNarrator = "Abu Huraira (R.A.)",
            hadithBook = "Sahih al-Bukhari 6307 & Sahih Muslim 2702",
            hadithGrade = "Sahih",
            hadithUrl = "https://sunnah.com/bukhari:6307",
            order = 12
        )
    )

    fun init(context: Context) {
        if (prefs == null) {
            prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            inMemorySessionCounts.clear()
            seedInitialSampleData()
            syncFirebaseContent()
        }
        com.tasbih.digitaltasbih.util.TasbihAudioEngine.init(context)
    }

    private fun seedInitialSampleData() {
        val p = prefs ?: return
        if (!p.contains("tasbih_seeded_v2")) {
            val todayKey = getTodayKey()
            p.edit()
                .putInt("count_today_${todayKey}_subhan", 0)
                .putInt("count_alltime_subhan", 0)
                .putInt("count_alltime_alhamd", 0)
                .putInt("count_alltime_akbar", 0)
                .putInt("count_alltime_lailaha", 0)
                .putInt("grand_total", 0)
                .putInt("streak_days", 0)
                .putInt("shields_count", 1)
                .putString("active_dhikr_id", "subhan")
                .putString("active_bead_skin", "marble")
                .putInt("target_subhan", 33)
                .putInt("target_alhamd", 33)
                .putInt("target_akbar", 33)
                .putBoolean("tasbih_seeded_v1", true)
                .putBoolean("tasbih_seeded_v2", true)
                .apply()
    
        if (!p.contains("tasbih_seeded_v3")) {
            p.edit()
                .putInt("count_alltime_name_1", 27)
                .putInt("count_alltime_name_2", 4)
                .putBoolean("tasbih_seeded_v3", true)
                .apply()
        }    }
    }

    /**
     * Asynchronously fetches dynamic Dhikrs and Bead Marbles from Firebase Realtime Database
     * with full offline SharedPreferences caching.
     */
        fun syncFirebaseContent() {
        scope.launch {
            // 1. App Dhikr (/app_dhikr.json)
            try {
                val appDhikrUrl = "$FIREBASE_RTDB_URL/app_dhikr.json"
                val res = httpGet(appDhikrUrl)
                if (!res.isNullOrEmpty() && res != "null") {
                    prefs?.edit()?.putString("cached_firebase_app_dhikr", res)?.apply()
                }
            } catch (_: Exception) {}

            // 2. Extra Dhikrs (/extra_dhikr.json)
            try {
                val extraDhikrUrl = "$FIREBASE_RTDB_URL/extra_dhikr.json"
                val res = httpGet(extraDhikrUrl)
                if (!res.isNullOrEmpty() && res != "null") {
                    prefs?.edit()?.putString("cached_firebase_extra_dhikr", res)?.apply()
                }
            } catch (_: Exception) {}

            // 3. 99 Names of Allah (/app_asma.json)
            try {
                val asmaUrl = "$FIREBASE_RTDB_URL/app_asma.json"
                val res = httpGet(asmaUrl)
                if (!res.isNullOrEmpty() && res != "null") {
                    prefs?.edit()?.putString("cached_firebase_app_asma", res)?.apply()
                }
            } catch (_: Exception) {}

            // 4. Islamic Dhikr Guide (/guide.json)
            try {
                val guideUrl = "$FIREBASE_RTDB_URL/guide.json"
                val res = httpGet(guideUrl)
                if (!res.isNullOrEmpty() && res != "null") {
                    prefs?.edit()?.putString("cached_firebase_guide", res)?.apply()
                }
            } catch (_: Exception) {}

            // 5. Authentic Darood Sharif (/app_darood.json)
            try {
                val daroodUrl = "$FIREBASE_RTDB_URL/app_darood.json"
                val res = httpGet(daroodUrl)
                if (!res.isNullOrEmpty() && res != "null") {
                    prefs?.edit()?.putString("cached_firebase_app_darood", res)?.apply()
                }
            } catch (_: Exception) {}

            // 6. Bead Marbles (/bead_marbles.json)
            try {
                val beadUrl = "$FIREBASE_RTDB_URL/bead_marbles.json"
                val res = httpGet(beadUrl)
                if (!res.isNullOrEmpty() && res != "null") {
                    prefs?.edit()?.putString("cached_firebase_bead_marbles", res)?.apply()
                }
            } catch (_: Exception) {}
        }
    }

    private fun httpGet(urlStr: String): String? {
        var conn: HttpURLConnection? = null
        return try {
            val url = URL(urlStr)
            conn = url.openConnection() as HttpURLConnection
            conn.connectTimeout = 6000
            conn.readTimeout = 6000
            conn.requestMethod = "GET"
            if (conn.responseCode == 200) {
                val reader = BufferedReader(InputStreamReader(conn.inputStream))
                val sb = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    sb.append(line)
                }
                reader.close()
                sb.toString()
            } else null
        } catch (_: Exception) {
            null
        } finally {
            conn?.disconnect()
        }
    }

        // ══════════════════════════════════════════════════════════════
    //  5. DYNAMIC DHIKR, GUIDE, 99 NAMES & DAROOD MANAGEMENT
    // ══════════════════════════════════════════════════════════════
    fun getAllDhikrs(): List<TasbihDhikrItem> {
        val list = mutableListOf<TasbihDhikrItem>()
        val p = prefs

        // 1. User's local custom dhikrs are ALWAYS kept at the top
        val customJson = p?.getString("user_custom_dhikrs", "[]") ?: "[]"
        try {
            val jsonArray = org.json.JSONArray(customJson)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val id = obj.optString("id", "c_$i")
                val name = obj.optString("name", "Custom Dhikr")
                val ar = obj.optString("ar", name)
                list.add(
                    TasbihDhikrItem(
                        id = id,
                        arabic = ar,
                        localizedNames = mapOf("en" to name, "hi" to name, "ur" to name, "ar" to ar, "bn" to name),
                        localizedMeanings = mapOf("en" to "Custom Dhikr", "hi" to "कस्टम ज़िक्र"),
                        icon = "📿",
                        defaultTarget = obj.optInt("tgt", 33),
                        isCustom = true
                    )
                )
            }
        } catch (_: Exception) {}

        // 2. All 20 Authentic Core Dhikrs (Always pure & complete without truncation)
        val coreMap = LinkedHashMap<String, TasbihDhikrItem>()
        for (item in DEFAULT_DHIKR_LIST) {
            coreMap[item.id] = item
        }

        // Merge target counts or order from Firebase cache while preserving 100% authentic full texts
        val appDhikrJson = p?.getString("cached_firebase_app_dhikr", null)
        if (!appDhikrJson.isNullOrEmpty()) {
            try {
                val obj = JSONObject(appDhikrJson)
                val keys = obj.keys()
                while (keys.hasNext()) {
                    val k = keys.next()
                    val item = obj.optJSONObject(k)
                    if (item != null && item.optBoolean("active", true)) {
                        val dId = item.optString("id", k)
                        // Ignore redundant separate First Kalima key since lailaha is already the full Kalima
                        if (dId == "la ilaha illallah muhammadur rasulullah") continue

                        val existing = coreMap[dId]
                        if (existing != null) {
                            val newTgt = item.optInt("tgt", existing.defaultTarget)
                            val newOrder = item.optInt("order", existing.order)
                            coreMap[dId] = existing.copy(defaultTarget = newTgt, order = newOrder)
                        } else {
                            val ar = item.optString("ar", "")
                            val loObj = item.optJSONObject("lo")
                            val meObj = item.optJSONObject("me")
                            val loMap = mutableMapOf<String, String>()
                            val meMap = mutableMapOf<String, String>()
                            if (loObj != null) {
                                val loKeys = loObj.keys()
                                while (loKeys.hasNext()) { val lk = loKeys.next(); loMap[lk] = loObj.optString(lk) }
                            }
                            if (meObj != null) {
                                val meKeys = meObj.keys()
                                while (meKeys.hasNext()) { val mk = meKeys.next(); meMap[mk] = meObj.optString(mk) }
                            }
                            coreMap[dId] = TasbihDhikrItem(
                                id = dId,
                                arabic = ar,
                                localizedNames = loMap.ifEmpty { mapOf("en" to dId) },
                                localizedMeanings = meMap,
                                icon = item.optString("icon", "✨"),
                                defaultTarget = item.optInt("tgt", 33),
                                order = item.optInt("order", 99)
                            )
                        }
                    }
                }
            } catch (_: Exception) {}
        }

        val sortedCore = coreMap.values.sortedBy { it.order }
        list.addAll(sortedCore)

        // 3. Remote /extra_dhikr from Firebase
        val extraJson = p?.getString("cached_firebase_extra_dhikr", null)
        if (!extraJson.isNullOrEmpty()) {
            try {
                val obj = JSONObject(extraJson)
                val keys = obj.keys()
                val extraList = mutableListOf<TasbihDhikrItem>()
                while (keys.hasNext()) {
                    val k = keys.next()
                    val item = obj.optJSONObject(k)
                    if (item != null && item.optBoolean("active", true)) {
                        val dId = item.optString("id", k)
                        if (list.none { it.id == dId }) {
                            val ar = item.optString("ar", "")
                            val loObj = item.optJSONObject("lo")
                            val loMap = mutableMapOf<String, String>()
                            if (loObj != null) {
                                val lKeys = loObj.keys()
                                while (lKeys.hasNext()) { val lk = lKeys.next(); loMap[lk] = loObj.optString(lk) }
                            }
                            val enName = loMap["en"] ?: item.optString("name", dId)
                            extraList.add(
                                TasbihDhikrItem(
                                    id = dId,
                                    arabic = ar,
                                    localizedNames = loMap.ifEmpty { mapOf("en" to enName) },
                                    localizedMeanings = mapOf("en" to item.optString("meaning", ""), "hi" to item.optString("meaning_hi", "")),
                                    icon = item.optString("icon", "✨"),
                                    defaultTarget = item.optInt("tgt", 100),
                                    order = item.optInt("order", 99)
                                )
                            )
                        }
                    }
                }
                extraList.sortBy { it.order }
                list.addAll(extraList)
            } catch (_: Exception) {}
        }

                val activeId = p?.getString("active_dhikr_id", "") ?: ""
        if (activeId.startsWith("name_")) {
            val num = activeId.removePrefix("name_").toIntOrNull()
            if (num != null && list.none { it.id == activeId }) {
                getAsmaDhikrItem(num)?.let { list.add(0, it) }
            }
        }

        val customItems = list.filter { it.isCustom }
        val nonCustomItems = list.filter { !it.isCustom }.sortedBy { it.order }
        return customItems + nonCustomItems
    }

    fun getAsmaUlHusna(): List<AsmaNameItem> {
        val p = prefs
        val cacheStr = p?.getString("cached_firebase_app_asma", null)
        if (!cacheStr.isNullOrEmpty()) {
            try {
                val obj = JSONObject(cacheStr)
                val keys = obj.keys()
                val list = mutableListOf<AsmaNameItem>()
                while (keys.hasNext()) {
                    val k = keys.next()
                    val item = obj.optJSONObject(k)
                    if (item != null) {
                        val num = item.optInt("n", k.toIntOrNull() ?: 0)
                        val ar = item.optString("ar", "")
                        val tr = item.optString("tr", "")
                        val meObj = item.optJSONObject("me")
                        val meMap = mutableMapOf<String, String>()
                        if (meObj != null) {
                            val mKeys = meObj.keys()
                            while (mKeys.hasNext()) { val mk = mKeys.next(); meMap[mk] = meObj.optString(mk) }
                        }
                        if (num in 1..99 && ar.isNotBlank()) {
                            list.add(AsmaNameItem(num, ar, tr, meMap))
                        }
                    }
                }
                if (list.size >= 90) {
                    return list.sortedBy { it.number }
                }
            } catch (_: Exception) {}
        }
        return ASMA_UL_HUSNA
    }

    fun getAsmaDhikrItem(num: Int): TasbihDhikrItem? {
        val asma = getAsmaUlHusna().find { it.number == num } ?: return null
        val loMap = mutableMapOf<String, String>()
        loMap["en"] = asma.transliteration
        loMap["hi"] = asma.transliteration
        loMap["ur"] = asma.transliteration
        loMap["ar"] = asma.arabic
        loMap["bn"] = asma.transliteration
        return TasbihDhikrItem(
            id = "name_${asma.number}",
            arabic = asma.arabic,
            localizedNames = loMap,
            localizedMeanings = asma.meanings,
            icon = "✨",
            defaultTarget = 99,
            order = 500 + asma.number,
            isCustom = false
        )
    }

    fun getGuideList(lang: String): List<DhikrGuideDetail> {
        val p = prefs
        val cacheStr = p?.getString("cached_firebase_guide", null)
        if (!cacheStr.isNullOrEmpty()) {
            try {
                val obj = JSONObject(cacheStr)
                val keys = obj.keys()
                val list = mutableListOf<DhikrGuideDetail>()
                while (keys.hasNext()) {
                    val k = keys.next()
                    val item = obj.optJSONObject(k)
                    if (item != null) {
                        val id = item.optString("id", k)
                        val ar = item.optString("ar", "")
                        val cnt = item.optString("cnt", "100×")
                        val ref = item.optString("ref", item.optString("hadith_book", ""))
                        var book = item.optString("hadith_book", ref)
                        var grade = item.optString("hadith_grade", "Sahih")
                        var url = item.optString("hadith_url", "")
                        var narr = item.optString("hadith_narrator", "")
                        var hadithAr = item.optString("hadith_ar", "")
                        val order = item.optInt("order", 99)

                        val loMap = mutableMapOf<String, String>()
                        val meMap = mutableMapOf<String, String>()
                        val benMap = mutableMapOf<String, String>()
                        val howMap = mutableMapOf<String, String>()
                        val htrMap = mutableMapOf<String, String>()

                        item.optJSONObject("lo")?.let { o -> val ks = o.keys(); while (ks.hasNext()) { val x = ks.next(); loMap[x] = o.optString(x) } }
                        item.optJSONObject("me")?.let { o -> val ks = o.keys(); while (ks.hasNext()) { val x = ks.next(); meMap[x] = o.optString(x) } }
                        item.optJSONObject("ben")?.let { o -> val ks = o.keys(); while (ks.hasNext()) { val x = ks.next(); benMap[x] = o.optString(x) } }
                        item.optJSONObject("how")?.let { o -> val ks = o.keys(); while (ks.hasNext()) { val x = ks.next(); howMap[x] = o.optString(x) } }

                        htrMap["en"] = item.optString("hadith_en", "")
                        htrMap["hi"] = item.optString("hadith_hi", "")
                        htrMap["ur"] = item.optString("hadith_ur", "")
                        htrMap["bn"] = item.optString("hadith_bn", "")

                        // Enrich from DEFAULT_GUIDE_LIST if cached data has missing fields
                        val fallback = DEFAULT_GUIDE_LIST.find {
                            it.id.equals(id, ignoreCase = true) ||
                            it.arabic.replace(" ", "") == ar.replace(" ", "")
                        }
                        if (fallback != null) {
                            if (howMap.isEmpty()) howMap.putAll(fallback.howToRecite)
                            if (loMap.isEmpty()) loMap.putAll(fallback.title)
                            if (meMap.isEmpty()) meMap.putAll(fallback.meaning)
                            if (benMap.isEmpty()) benMap.putAll(fallback.virtues)
                            if (hadithAr.isBlank()) hadithAr = fallback.hadithArabic
                            if (narr.isBlank()) narr = fallback.hadithNarrator
                            if (url.isBlank()) url = fallback.hadithUrl
                            if (book.isBlank() || book == ref) book = fallback.hadithBook
                            if (grade.isBlank() || grade == "Sahih") grade = fallback.hadithGrade
                            if (htrMap["en"].isNullOrBlank()) fallback.hadithTranslations["en"]?.let { htrMap["en"] = it }
                            if (htrMap["hi"].isNullOrBlank()) fallback.hadithTranslations["hi"]?.let { htrMap["hi"] = it }
                            if (htrMap["ur"].isNullOrBlank()) fallback.hadithTranslations["ur"]?.let { htrMap["ur"] = it }
                            if (htrMap["bn"].isNullOrBlank()) fallback.hadithTranslations["bn"]?.let { htrMap["bn"] = it }
                        }

                        if (ar.isNotBlank()) {
                            list.add(
                                DhikrGuideDetail(
                                    id = id,
                                    arabic = ar,
                                    title = loMap,
                                    meaning = meMap,
                                    targetStr = cnt,
                                    howToRecite = howMap,
                                    hadithRef = ref,
                                    virtues = benMap,
                                    hadithArabic = hadithAr,
                                    hadithTranslations = htrMap,
                                    hadithNarrator = narr,
                                    hadithBook = book,
                                    hadithGrade = grade,
                                    hadithUrl = url,
                                    order = order
                                )
                            )
                        }
                    }
                }
                if (list.isNotEmpty()) {
                    return list.sortedBy { it.order }
                }
            } catch (_: Exception) {}
        }
        return DEFAULT_GUIDE_LIST
    }

    fun getDaroodList(): List<DaroodGuideItem> {
        val p = prefs
        val cacheStr = p?.getString("cached_firebase_app_darood", null)
        if (!cacheStr.isNullOrEmpty()) {
            try {
                val obj = JSONObject(cacheStr)
                val keys = obj.keys()
                val list = mutableListOf<DaroodGuideItem>()
                while (keys.hasNext()) {
                    val k = keys.next()
                    val item = obj.optJSONObject(k)
                    if (item != null) {
                        val id = item.optString("id", k)
                        val ar = item.optString("ar", "")
                        val titleMap = mutableMapOf<String, String>()
                        val loMap = mutableMapOf<String, String>()
                        val meMap = mutableMapOf<String, String>()
                        val benMap = mutableMapOf<String, String>()

                        item.optJSONObject("title")?.let { o -> val ks = o.keys(); while (ks.hasNext()) { val x = ks.next(); titleMap[x] = o.optString(x) } }
                        item.optJSONObject("lo")?.let { o -> val ks = o.keys(); while (ks.hasNext()) { val x = ks.next(); loMap[x] = o.optString(x) } }
                        item.optJSONObject("me")?.let { o -> val ks = o.keys(); while (ks.hasNext()) { val x = ks.next(); meMap[x] = o.optString(x) } }
                        item.optJSONObject("ben")?.let { o -> val ks = o.keys(); while (ks.hasNext()) { val x = ks.next(); benMap[x] = o.optString(x) } }

                        if (ar.isNotBlank()) {
                            val fallback = DAROOD_LIST.find { it.id == id }
                            list.add(
                                DaroodGuideItem(
                                    id = id,
                                    title = titleMap.ifEmpty { fallback?.title ?: mapOf("en" to id) },
                                    arabic = ar,
                                    transliteration = loMap.ifEmpty { fallback?.transliteration ?: emptyMap() },
                                    translation = meMap.ifEmpty { fallback?.translation ?: emptyMap() },
                                    benefits = benMap.ifEmpty { fallback?.benefits ?: emptyMap() },
                                    hadithRef = item.optString("hadith_ref", item.optString("source", fallback?.hadithRef ?: "")),
                                    defaultTarget = item.optInt("tgt", fallback?.defaultTarget ?: 33),
                                    hadithArabic = item.optString("hadith_arabic", fallback?.hadithArabic ?: ""),
                                    hadithTranslations = fallback?.hadithTranslations ?: meMap,
                                    hadithNarrator = fallback?.hadithNarrator ?: emptyMap(),
                                    hadithBook = item.optString("hadith_book", fallback?.hadithBook ?: ""),
                                    hadithGrade = item.optString("hadith_grade", fallback?.hadithGrade ?: "Sahih"),
                                    hadithUrl = item.optString("hadith_url", fallback?.hadithUrl ?: "")
                                )
                            )
                        }
                    }
                }
                if (list.isNotEmpty()) {
                    return list
                }
            } catch (_: Exception) {}
        }
        return DAROOD_LIST
    }

    fun addCustomDhikr(name: String, arabic: String, target: Int = 33): TasbihDhikrItem {
        val p = prefs
        val id = "custom_" + System.currentTimeMillis()
        val customJson = p?.getString("user_custom_dhikrs", "[]") ?: "[]"
        try {
            val jsonArray = org.json.JSONArray(customJson)
            val newObj = JSONObject().apply {
                put("id", id)
                put("name", name)
                put("ar", if (arabic.isNotBlank()) arabic else name)
                put("tgt", target)
            }
            jsonArray.put(newObj)
            p?.edit()?.putString("user_custom_dhikrs", jsonArray.toString())?.apply()
        } catch (_: Exception) {}

        return TasbihDhikrItem(
            id = id,
            arabic = if (arabic.isNotBlank()) arabic else name,
            localizedNames = mapOf("en" to name, "hi" to name, "ur" to name, "ar" to arabic),
            localizedMeanings = mapOf("en" to "Custom Dhikr", "hi" to "कस्टम ज़िक्र"),
            icon = "📿",
            defaultTarget = target,
            isCustom = true
        )
    }

    // ══════════════════════════════════════════════════════════════
    //  6. BEAD MARBLE SKINS & UNLOCKS
    // ══════════════════════════════════════════════════════════════
    fun getAllBeadSkins(): List<BeadMarbleSkin> {
        val list = mutableListOf<BeadMarbleSkin>()
        list.addAll(BUILTIN_BEAD_MARBLES)

        // Remote marbles from Firebase cache
        val cacheStr = prefs?.getString("cached_firebase_bead_marbles", null)
        if (!cacheStr.isNullOrEmpty()) {
            try {
                val obj = JSONObject(cacheStr)
                val keys = obj.keys()
                val remoteList = mutableListOf<Pair<Int, BeadMarbleSkin>>()
                while (keys.hasNext()) {
                    val k = keys.next()
                    val m = obj.optJSONObject(k)
                    if (m != null && m.optBoolean("active", true)) {
                        val mId = m.optString("id", k)
                        val order = m.optInt("order", 999)
                        remoteList.add(
                            order to BeadMarbleSkin(
                                id = mId,
                                name = m.optString("name", "Marble"),
                                family = m.optString("family", "stone"),
                                baseColor = m.optString("base", "#8a5a34"),
                                highlightColor = m.optString("highlight", "#eccfa3"),
                                shadowColor = m.optString("shadow", "#3a2412"),
                                tier = if (m.optString("tier") == "premium") "premium" else "free",
                                image = m.optString("image", "")
                            )
                        )
                    }
                }
                remoteList.sortBy { it.first }
                remoteList.forEach { (_, skin) ->
                    if (list.none { it.id == skin.id }) {
                        list.add(skin)
                    }
                }
            } catch (_: Exception) {}
        } else {
            scope.launch {
                try {
                    val beadUrl = "$FIREBASE_RTDB_URL/bead_marbles.json"
                    val res = httpGet(beadUrl)
                    if (!res.isNullOrEmpty() && res != "null") {
                        prefs?.edit()?.putString("cached_firebase_bead_marbles", res)?.apply()
                    }
                } catch (_: Exception) {}
            }
        }

        return list
    }

    fun isMarbleUnlocked(id: String): Boolean {
        val skin = getAllBeadSkins().find { it.id == id } ?: return true
        if (skin.tier != "premium") return true

        val p = prefs ?: return false
        val exp = p.getLong("unlock_marble_$id", 0L)
        return exp > System.currentTimeMillis()
    }

    fun unlockMarble24Hours(id: String) {
        val p = prefs ?: return
        val exp = System.currentTimeMillis() + (24 * 60 * 60 * 1000L)
        p.edit().putLong("unlock_marble_$id", exp).apply()
    }

    fun getActiveBeadSkin(): BeadMarbleSkin {
        val p = prefs
        val skinId = p?.getString("active_bead_skin", "marble") ?: "marble"
        return getAllBeadSkins().find { it.id == skinId } ?: BUILTIN_BEAD_MARBLES[4]
    }

    fun setActiveBeadSkin(id: String) {
        prefs?.edit()?.putString("active_bead_skin", id)?.apply()
    }

    // ══════════════════════════════════════════════════════════════
    //  7. ACTIVE COUNTING STATE & DHIKR TRACKER AUTO-SYNC
    // ══════════════════════════════════════════════════════════════
    fun getActiveDhikrId(): String {
        return prefs?.getString("active_dhikr_id", "subhan") ?: "subhan"
    }

    fun setActiveDhikrId(id: String) {
        prefs?.edit()?.putString("active_dhikr_id", id)?.apply()
    }

    fun getSessionCount(dhikrId: String): Int {
        return inMemorySessionCounts[dhikrId] ?: 0
    }

    fun getTodayCount(dhikrId: String): Int {
        val todayKey = getTodayKey()
        return prefs?.getInt("count_today_${todayKey}_$dhikrId", 0) ?: 0
    }

    fun getAllTimeCount(dhikrId: String): Int {
        val p = prefs ?: return if (dhikrId == "name_1") 27 else if (dhikrId == "name_2") 4 else 0
        if (dhikrId == "name_1" && !p.contains("count_alltime_name_1")) return 27
        if (dhikrId == "name_2" && !p.contains("count_alltime_name_2")) return 4
        return p.getInt("count_alltime_$dhikrId", 0)
    }

    fun getTarget(dhikrId: String, defaultTgt: Int = 33): Int {
        return prefs?.getInt("target_$dhikrId", defaultTgt) ?: defaultTgt
    }

    fun setTarget(dhikrId: String, target: Int) {
        prefs?.edit()?.putInt("target_$dhikrId", target)?.apply()
    }

    /**
     * Records a count tap:
     * 1. Increments in-memory session count (always 0 on app launch).
     * 2. Increments today count, all-time count, and grand total in persistent SharedPreferences.
     * 3. Auto-syncs seamlessly into DhikrRepository so Dhikr Tracker is updated live!
     */
    fun recordCount(dhikrId: String, increment: Int = 1) {
        val curSession = inMemorySessionCounts[dhikrId] ?: 0
        inMemorySessionCounts[dhikrId] = curSession + increment

        val p = prefs ?: return
        val todayKey = getTodayKey()

        val curToday = p.getInt("count_today_${todayKey}_$dhikrId", 0)
        val curAllTime = if (p.contains("count_alltime_$dhikrId")) {
            p.getInt("count_alltime_$dhikrId", 0)
        } else {
            getAllTimeCount(dhikrId)
        }
        val grandTotal = p.getInt("grand_total", 0)

        p.edit()
            .putInt("count_today_${todayKey}_$dhikrId", curToday + increment)
            .putInt("count_alltime_$dhikrId", curAllTime + increment)
            .putInt("grand_total", grandTotal + increment)
            .putBoolean("history_user_cleared", false)
            .apply()

        // 🔗 Live auto-sync with Dhikr Tracker!
        DhikrRepository.recordTasbihCount(dhikrId, increment)
    }

    fun undoCount(dhikrId: String) {
        val curSession = inMemorySessionCounts[dhikrId] ?: 0
        if (curSession <= 0) return
        inMemorySessionCounts[dhikrId] = (curSession - 1).coerceAtLeast(0)

        val p = prefs ?: return
        val todayKey = getTodayKey()
        val curToday = p.getInt("count_today_${todayKey}_$dhikrId", 0)
        val curAllTime = p.getInt("count_alltime_$dhikrId", 0)
        val grandTotal = p.getInt("grand_total", 0)

        p.edit()
            .putInt("count_today_${todayKey}_$dhikrId", (curToday - 1).coerceAtLeast(0))
            .putInt("count_alltime_$dhikrId", (curAllTime - 1).coerceAtLeast(0))
            .putInt("grand_total", (grandTotal - 1).coerceAtLeast(0))
            .apply()
    }

    fun resetSession(dhikrId: String) {
        inMemorySessionCounts[dhikrId] = 0
    }

    fun resetTodayCount(dhikrId: String) {
        val todayKey = getTodayKey()
        prefs?.edit()?.putInt("count_today_${todayKey}_$dhikrId", 0)?.apply()
    }

    fun isHistoryCleared(): Boolean {
        return prefs?.getBoolean("history_user_cleared", false) ?: false
    }

    fun saveSessionToHistory(dhikrItem: TasbihDhikrItem, count: Int) {
        if (count <= 0) return
        inMemorySessionCounts[dhikrItem.id] = 0
        val p = prefs ?: return
        val histJson = p.getString("dhikr_history_list", "[]") ?: "[]"
        try {
            val arr = org.json.JSONArray(histJson)
            val now = System.currentTimeMillis()
            val dateStr = SimpleDateFormat("d MMM yyyy, h:mm a", Locale.ENGLISH).format(Date(now))

            val obj = JSONObject().apply {
                put("id", "h_" + now)
                put("dhikrId", dhikrItem.id)
                put("dhikrName", dhikrItem.getName("en"))
                put("arabic", dhikrItem.arabic)
                put("count", count)
                put("timestamp", now)
                put("dateDisplay", dateStr)
                put("icon", dhikrItem.icon)
            }
            arr.put(obj)
            p.edit()
                .putString("dhikr_history_list", arr.toString())
                .putBoolean("history_user_cleared", false)
                .apply()
        } catch (_: Exception) {}
    }

    fun getHistoryList(): List<DhikrHistoryItem> {
        val list = mutableListOf<DhikrHistoryItem>()
        val p = prefs ?: return emptyList()
        if (p.getBoolean("history_user_cleared", false)) {
            return emptyList()
        }
        val histJson = p.getString("dhikr_history_list", null)
        if (histJson.isNullOrEmpty() || histJson == "[]") {
            return emptyList()
        }

        try {
            val arr = org.json.JSONArray(histJson)
            for (i in (arr.length() - 1) downTo 0) {
                val obj = arr.getJSONObject(i)
                val idStr = obj.optString("id")
                if (idStr.startsWith("def_")) continue
                val countVal = obj.optInt("count", 0)
                if (countVal > 0) {
                    list.add(
                        DhikrHistoryItem(
                            id = idStr,
                            dhikrId = obj.optString("dhikrId"),
                            dhikrName = obj.optString("dhikrName"),
                            arabic = obj.optString("arabic"),
                            count = countVal,
                            timestamp = obj.optLong("timestamp"),
                            dateDisplay = obj.optString("dateDisplay"),
                            icon = obj.optString("icon", "✨")
                        )
                    )
                }
            }
        } catch (_: Exception) {}

        return list
    }

    fun clearHistory() {
        val p = prefs ?: return
        val editor = p.edit()
            .putString("dhikr_history_list", "[]")
            .putInt("grand_total", 0)
            .putBoolean("history_user_cleared", true)
        for (dhikr in getAllDhikrs()) {
            editor.putInt("count_alltime_${dhikr.id}", 0)
        }
        editor.apply()
        DhikrRepository.clearData()
    }

    fun getGrandTotal(): Int {
        val p = prefs ?: return 0
        val isCleared = p.getBoolean("history_user_cleared", false)
        if (isCleared) return 0
        return p.getInt("grand_total", 0)
    }

    fun isTasbihTourDone(): Boolean {
        return prefs?.getBoolean("tasbih_tour_done_v1", false) ?: false
    }

    fun setTasbihTourDone(done: Boolean) {
        prefs?.edit()?.putBoolean("tasbih_tour_done_v1", done)?.apply()
    }

    fun getStreakDays(): Int {
        return DhikrRepository.getSummaryForDate(Calendar.getInstance()).streakDays
    }

    fun getLongestStreak(): Int {
        return prefs?.getInt("longest_streak", 2) ?: 2
    }

    fun getDailyFeaturedAsmaItem(): AsmaNameItem {
        val list = getAsmaUlHusna()
        if (list.isEmpty()) {
            return ASMA_UL_HUSNA[35]
        }
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val daySeed = year * 1000 + month * 35 + day
        val index = (daySeed % 99)
        return list.getOrNull(index) ?: list.first()
    }

    fun getDailyFeaturedAllahName(lang: String = "en"): AllahNameData {
        val asma = getDailyFeaturedAsmaItem()
        val m = asma.meanings[lang]
            ?: asma.meanings["en"]
            ?: asma.meanings["hi"]
            ?: asma.meanings.values.firstOrNull()
            ?: asma.transliteration
        return AllahNameData(
            number = asma.number,
            arabic = asma.arabic,
            transliteration = asma.transliteration,
            meaning = m
        )
    }
}
