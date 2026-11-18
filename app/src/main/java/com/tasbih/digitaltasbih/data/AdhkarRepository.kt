package com.tasbih.digitaltasbih.data

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class AdhkarItem(
    val id: String,
    val order: Int,
    val targetCount: Int,
    val titleEn: String,
    val titleHi: String,
    val titleUr: String,
    val titleAr: String,
    val arabicText: String,
    val transliterationEn: String,
    val transliterationHi: String,
    val translationEn: String,
    val translationHi: String,
    val translationUr: String,
    val translationAr: String,
    val referenceEn: String,
    val referenceHi: String,
    val referenceUr: String,
    val referenceAr: String,
    val benefitEn: String,
    val benefitHi: String,
    val benefitUr: String,
    val hadithBook: String = "",
    val hadithGrade: String = "",
    val hadithNarrator: String = "",
    val hadithArabic: String = "",
    val hadithTranslationEn: String = "",
    val hadithTranslationHi: String = "",
    val hadithTranslationUr: String = "",
    val hadithUrl: String = ""
) {
    fun getTitle(lang: String): String {
        return when {
            lang.startsWith("hi") && titleHi.isNotEmpty() -> titleHi
            lang.startsWith("ur") && titleUr.isNotEmpty() -> titleUr
            lang.startsWith("ar") && titleAr.isNotEmpty() -> titleAr
            else -> titleEn.ifEmpty { titleHi }
        }
    }

    fun getTranslation(lang: String): String {
        return when {
            lang.startsWith("hi") && translationHi.isNotEmpty() -> translationHi
            lang.startsWith("ur") && translationUr.isNotEmpty() -> translationUr
            lang.startsWith("ar") && translationAr.isNotEmpty() -> translationAr
            else -> translationEn.ifEmpty { translationHi }
        }
    }

    fun getTransliteration(lang: String): String {
        return when {
            lang.startsWith("hi") && transliterationHi.isNotEmpty() -> transliterationHi
            else -> transliterationEn
        }
    }

    fun getReference(lang: String): String {
        return when {
            lang.startsWith("hi") && referenceHi.isNotEmpty() -> referenceHi
            lang.startsWith("ur") && referenceUr.isNotEmpty() -> referenceUr
            lang.startsWith("ar") && referenceAr.isNotEmpty() -> referenceAr
            else -> referenceEn
        }
    }

    fun getBenefit(lang: String): String {
        return when {
            lang.startsWith("hi") && benefitHi.isNotEmpty() -> benefitHi
            lang.startsWith("ur") && benefitUr.isNotEmpty() -> benefitUr
            else -> benefitEn.ifEmpty { benefitHi }
        }
    }

    fun getHadithTranslation(lang: String): String {
        return when {
            lang.startsWith("hi") && hadithTranslationHi.isNotEmpty() -> hadithTranslationHi
            lang.startsWith("ur") && hadithTranslationUr.isNotEmpty() -> hadithTranslationUr
            else -> hadithTranslationEn.ifEmpty { hadithTranslationHi }
        }
    }

    // Backward-compatible properties for older callers
    val title: String get() = titleEn
    val transliteration: String get() = transliterationEn
    val translation: String get() = translationEn
    val reference: String get() = referenceEn
    val benefit: String get() = benefitEn
    val hadithMatn: String get() = hadithArabic
    val hadithTranslation: String get() = hadithTranslationEn
    val hadithSource: String get() = if (hadithBook.isNotEmpty() && hadithGrade.isNotEmpty()) "$hadithBook • $hadithGrade" else hadithBook
}

data class AdhkarDayStats(
    val dateKey: String,
    val label: String,
    val doneCount: Int,
    val totalCount: Int = 18
)

object AdhkarRepository {
    private const val PREFS_NAME = "digital_tasbih_adhkar_prefs"
    private var prefs: SharedPreferences? = null

    // State trigger for Compose recomposition
    val updateTrigger = mutableStateOf(0L)

    fun notifyUpdate() {
        updateTrigger.value = System.currentTimeMillis()
    }

    fun init(context: Context) {
        if (prefs == null) {
            prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }
    }

    private fun getTodayKey(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return sdf.format(Calendar.getInstance().time)
    }

    fun isTourDone(): Boolean {
        return prefs?.getBoolean("morning_tour_done_v2", false) ?: false
    }

    fun setTourDone(done: Boolean) {
        prefs?.edit()?.putBoolean("morning_tour_done_v2", done)?.apply()
    }

    fun getFontScale(): Float {
        return prefs?.getFloat("adhkar_font_scale", 1.0f) ?: 1.0f
    }

    fun setFontScale(scale: Float) {
        prefs?.edit()?.putFloat("adhkar_font_scale", scale.coerceIn(0.7f, 2.0f))?.apply()
    }

    fun isItemDone(id: String): Boolean {
        val today = getTodayKey()
        return prefs?.getBoolean("done_${today}_${id}", false) ?: false
    }

    fun setItemDone(id: String, done: Boolean) {
        val today = getTodayKey()
        prefs?.edit()?.putBoolean("done_${today}_${id}", done)?.apply()
        val count = getCompletedCount()
        prefs?.edit()?.putInt("total_done_${today}", count)?.apply()
        notifyUpdate()
    }

    fun getItemCount(id: String): Int {
        val today = getTodayKey()
        return prefs?.getInt("count_${today}_${id}", 0) ?: 0
    }

    fun setItemCount(id: String, count: Int) {
        val today = getTodayKey()
        prefs?.edit()?.putInt("count_${today}_${id}", count.coerceAtLeast(0))?.apply()
        notifyUpdate()
    }

    fun getCompletedCount(): Int {
        val list = getMorningAdhkarList()
        return list.count { isItemDone(it.id) }
    }

    fun resetAll() {
        val today = getTodayKey()
        val editor = prefs?.edit() ?: return
        val list = getMorningAdhkarList()
        for (item in list) {
            editor.remove("done_${today}_${item.id}")
            editor.remove("count_${today}_${item.id}")
        }
        editor.apply()
        notifyUpdate()
    }

    fun getStreak(): Int {
        val p = prefs ?: return 0
        var streak = 0
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)

        // Check if today is completed
        val todayStr = sdf.format(cal.time)
        val todayDone = getCompletedCount() == 18
        if (todayDone) {
            streak++
        }

        // Check previous days
        for (i in 1..365) {
            cal.add(Calendar.DAY_OF_YEAR, -1)
            val dStr = sdf.format(cal.time)
            val pastDoneCount = p.getInt("total_done_${dStr}", 0)
            if (pastDoneCount >= 18) {
                streak++
            } else {
                break
            }
        }
        return streak
    }

    fun getRecentDaysHistory(): List<AdhkarDayStats> {
        val list = mutableListOf<AdhkarDayStats>()
        val cal = Calendar.getInstance()
        val sdfKey = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val sdfLabel = SimpleDateFormat("dd MMM", Locale.ENGLISH)

        // Today
        val todayKey = sdfKey.format(cal.time)
        list.add(AdhkarDayStats(todayKey, "Today", getCompletedCount(), 18))

        // Yesterday
        cal.add(Calendar.DAY_OF_YEAR, -1)
        val yKey = sdfKey.format(cal.time)
        val yCount = prefs?.getInt("total_done_${yKey}", 0) ?: 0
        list.add(AdhkarDayStats(yKey, "Yesterday", yCount, 18))

        // Previous 3 days
        for (i in 2..4) {
            cal.add(Calendar.DAY_OF_YEAR, -1)
            val dKey = sdfKey.format(cal.time)
            val label = sdfLabel.format(cal.time)
            val count = prefs?.getInt("total_done_${dKey}", 0) ?: 0
            list.add(AdhkarDayStats(dKey, label, count, 18))
        }

        return list
    }

    fun getWeeklyProgress(): List<Float> {
        val p = prefs
        val cal = Calendar.getInstance()
        val sdfKey = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val todayKey = getTodayKey()
        val todayCompleted = getCompletedCount()

        // Set to Monday of this week
        cal.firstDayOfWeek = Calendar.MONDAY
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

        val result = mutableListOf<Float>()
        for (d in 0..6) {
            val dKey = sdfKey.format(cal.time)
            val count = if (dKey == todayKey) todayCompleted else (p?.getInt("total_done_${dKey}", 0) ?: 0)
            val pct = (count.toFloat() / 18f * 100f).coerceIn(0f, 100f)
            result.add(pct)
            cal.add(Calendar.DAY_OF_WEEK, 1)
        }
        return result
    }


    fun isEveningTourDone(): Boolean {
        return prefs?.getBoolean("evening_tour_done_v2", false) ?: false
    }

    fun setEveningTourDone(done: Boolean) {
        prefs?.edit()?.putBoolean("evening_tour_done_v2", done)?.apply()
    }

    fun isEveningItemDone(id: String): Boolean {
        val today = getTodayKey()
        return prefs?.getBoolean("eve_done_${today}_${id}", false) ?: false
    }

    fun setEveningItemDone(id: String, done: Boolean) {
        val today = getTodayKey()
        prefs?.edit()?.putBoolean("eve_done_${today}_${id}", done)?.apply()
        val count = getEveningCompletedCount()
        prefs?.edit()?.putInt("total_eve_done_${today}", count)?.apply()
        notifyUpdate()
    }

    fun getEveningItemCount(id: String): Int {
        val today = getTodayKey()
        return prefs?.getInt("eve_count_${today}_${id}", 0) ?: 0
    }

    fun setEveningItemCount(id: String, count: Int) {
        val today = getTodayKey()
        prefs?.edit()?.putInt("eve_count_${today}_${id}", count.coerceAtLeast(0))?.apply()
        notifyUpdate()
    }

    fun getEveningCompletedCount(): Int {
        val list = getEveningAdhkarList()
        return list.count { isEveningItemDone(it.id) }
    }

    fun resetAllEvening() {
        val today = getTodayKey()
        val editor = prefs?.edit() ?: return
        val list = getEveningAdhkarList()
        for (item in list) {
            editor.remove("eve_done_${today}_${item.id}")
            editor.remove("eve_count_${today}_${item.id}")
        }
        editor.putInt("total_eve_done_${today}", 0)
        editor.apply()
        notifyUpdate()
    }

    // ══════════════════════════════════════════════════════════════
    //  TIME WINDOW & UNFINISHED BADGE LOGIC (Exact from index.html)
    // ══════════════════════════════════════════════════════════════

    /**
     * Morning Adhkar Time Window:
     * - Primary: Starts at Fajr and continues until Asr (now >= Fajr && now < Asr)
     * - Fallback: 4:00 AM (240m) to 4:45 PM (1005m)
     */
    fun isMorningAdhkarTimeWindow(context: Context? = null): Boolean {
        val cal = Calendar.getInstance()
        val nowMins = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
        try {
            if (context != null) {
                PrayerRepository.init(context)
            }
            val settings = PrayerRepository.loadSettings()
            val dayData = PrayerRepository.computeOfflineDay(cal.time, settings)
            val fajrStr = dayData.timings["Fajr"]
            val asrStr = dayData.timings["Asr"]
            if (!fajrStr.isNullOrEmpty() && !asrStr.isNullOrEmpty()) {
                val fajrParts = fajrStr.split(":")
                val asrParts = asrStr.split(":")
                if (fajrParts.size >= 2 && asrParts.size >= 2) {
                    val fajrMins = (fajrParts[0].trim().toIntOrNull() ?: 5) * 60 + (fajrParts[1].trim().toIntOrNull() ?: 0)
                    val asrMins = (asrParts[0].trim().toIntOrNull() ?: 16) * 60 + (asrParts[1].trim().toIntOrNull() ?: 30)
                    return nowMins in fajrMins until asrMins
                }
            }
        } catch (_: Exception) {}
        return nowMins in 240 until 1005
    }

    /**
     * Evening Adhkar Time Window:
     * - Primary: Starts at Maghrib and continues until 1 hour before next day Fajr
     * - Fallback: 6:30 PM (1110m) until 3:30 AM (210m)
     */
    fun isEveningAdhkarTimeWindow(context: Context? = null): Boolean {
        val cal = Calendar.getInstance()
        val nowMins = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
        try {
            if (context != null) {
                PrayerRepository.init(context)
            }
            val settings = PrayerRepository.loadSettings()
            val dayData = PrayerRepository.computeOfflineDay(cal.time, settings)
            val maghribStr = dayData.timings["Maghrib"]
            val fajrStr = dayData.timings["Fajr"]
            if (!maghribStr.isNullOrEmpty() && !fajrStr.isNullOrEmpty()) {
                val maghribParts = maghribStr.split(":")
                val fajrParts = fajrStr.split(":")
                if (maghribParts.size >= 2 && fajrParts.size >= 2) {
                    val maghribMins = (maghribParts[0].trim().toIntOrNull() ?: 18) * 60 + (maghribParts[1].trim().toIntOrNull() ?: 0)
                    val fajrMins = (fajrParts[0].trim().toIntOrNull() ?: 5) * 60 + (fajrParts[1].trim().toIntOrNull() ?: 0)
                    val fajrCutoffMins = (fajrMins - 60 + 1440) % 1440
                    return (nowMins >= maghribMins || nowMins < fajrCutoffMins)
                }
            }
        } catch (_: Exception) {}
        return nowMins >= 1110 || nowMins < 210
    }

    /**
     * Determines whether the Morning Adhkar notification dot / alert is shown:
     * - Returns true ONLY if within morning time window AND not all 18 adhkar completed today.
     * - Returns false as soon as all 18 are completed, or when time window closes.
     */
    fun isMorningAdhkarUnfinishedNow(context: Context): Boolean {
        init(context)
        if (!isMorningAdhkarTimeWindow(context)) return false
        val morningList = getMorningAdhkarList()
        val total = morningList.size.coerceAtLeast(18)
        val completed = getCompletedCount()
        return completed < total
    }

    /**
     * Determines whether the Evening Adhkar notification dot / alert is shown:
     * - Returns true ONLY if within evening time window AND not all 23 adhkar completed today.
     * - Returns false as soon as all 23 are completed, or when time window closes.
     */
    fun isEveningAdhkarUnfinishedNow(context: Context): Boolean {
        init(context)
        if (!isEveningAdhkarTimeWindow(context)) return false
        val eveningList = getEveningAdhkarList()
        val total = eveningList.size.coerceAtLeast(23)
        val completed = getEveningCompletedCount()
        return completed < total
    }

    fun getEveningStreak(): Int {
        val p = prefs ?: return 0
        var streak = 0
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)

        val todayDone = getEveningCompletedCount() >= 23
        if (todayDone) {
            streak++
        }

        for (i in 1..365) {
            cal.add(Calendar.DAY_OF_YEAR, -1)
            val dStr = sdf.format(cal.time)
            val pastDoneCount = p.getInt("total_eve_done_${dStr}", 0)
            if (pastDoneCount >= 23) {
                streak++
            } else {
                break
            }
        }
        return streak
    }

    fun getEveningRecentDaysHistory(): List<AdhkarDayStats> {
        val list = mutableListOf<AdhkarDayStats>()
        val cal = Calendar.getInstance()
        val sdfKey = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val sdfLabel = SimpleDateFormat("dd MMM", Locale.ENGLISH)

        val todayKey = sdfKey.format(cal.time)
        list.add(AdhkarDayStats(todayKey, "Today", getEveningCompletedCount(), 23))

        cal.add(Calendar.DAY_OF_YEAR, -1)
        val yKey = sdfKey.format(cal.time)
        val yCount = prefs?.getInt("total_eve_done_${yKey}", 0) ?: 0
        list.add(AdhkarDayStats(yKey, "Yesterday", yCount, 23))

        for (i in 2..4) {
            cal.add(Calendar.DAY_OF_YEAR, -1)
            val dKey = sdfKey.format(cal.time)
            val label = sdfLabel.format(cal.time)
            val count = prefs?.getInt("total_eve_done_${dKey}", 0) ?: 0
            list.add(AdhkarDayStats(dKey, label, count, 23))
        }
        return list
    }

    fun getEveningWeeklyProgress(): List<Float> {
        val p = prefs
        val cal = Calendar.getInstance()
        val sdfKey = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val todayKey = getTodayKey()
        val todayCompleted = getEveningCompletedCount()

        cal.firstDayOfWeek = Calendar.MONDAY
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

        val result = mutableListOf<Float>()
        for (d in 0..6) {
            val dKey = sdfKey.format(cal.time)
            val count = if (dKey == todayKey) todayCompleted else (p?.getInt("total_eve_done_${dKey}", 0) ?: 0)
            val pct = (count.toFloat() / 23f * 100f).coerceIn(0f, 100f)
            result.add(pct)
            cal.add(Calendar.DAY_OF_WEEK, 1)
        }
        return result
    }

    fun getEveningAdhkarList(): List<AdhkarItem> = eveningList

    fun getMorningAdhkarList(): List<AdhkarItem> = morningList

    private val morningList = listOf(
        AdhkarItem(
            id = "m_ayatulkursi",
            order = 1,
            targetCount = 1,
            titleEn = "Ayat al-Kursi",
            titleHi = "आयतुल कुर्सी",
            titleUr = "آیۃ الکرسی",
            titleAr = "آية الكرسي",
            arabicText = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَنْ ذَا الَّذِي يَشْفَعُ عِنْدَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ",
            transliterationEn = "Allahu la ilaha illa Huwal-Hayyul-Qayyum. La ta'khudhuhu sinatuw-wa la nawm. Lahu ma fis-samawati wa ma fil-ard. Man dhal-ladhi yashfa'u 'indahu illa bi-idhnih. Ya'lamu ma bayna aydihim wa ma khalfahum, wa la yuhituna bi-shay'im-min 'ilmihi illa bima sha'. Wasi'a kursiyyuhus-samawati wal-ard, wa la ya'uduhu hifzuhuma, wa Huwal-'Aliyyul-'Azim.",
            transliterationHi = "अल्लाहु ला इलाहा इल्ला हुवल हय्युल क़य्यूम, ला तअख़ुज़ुहू सिनतुंव-वला नौम, लहू मा फ़िस-समावाति वमा फ़िल अर्द, मन् ज़ल्लज़ी यश्फ़उ इन्दहू इल्ला बि-इज़्निह, यअलमु मा बैना ऐदीहिम वमा ख़ल्फ़हुम, वला युहीतूना बिशयइम-मिन इल्मिही इल्ला बिमा शाअ, वसिअ कुरसिय्युहुस-समावाति वल-अर्द, वला यऊदुहू हिफ़्ज़ुहुमा, वहुवल-अलिय्युल-अज़ीम।",
            translationEn = "Allah! There is no deity except Him, the Ever-Living, the Sustainer of all existence. Neither drowsiness overtakes Him nor sleep. To Him belongs whatever is in the heavens and whatever is on the earth. Who is it that can intercede with Him except by His permission? He knows what is before them and what will be after them, and they encompass not a thing of His knowledge except for what He wills. His Kursi extends over the heavens and the earth, and their preservation tires Him not. And He is the Most High, the Most Great.",
            translationHi = "अल्लाह! उसके सिवा कोई सच्चा माबूद नहीं, वह हमेशा ज़िंदा रहने वाला और सब को क़ायम रखने वाला है। उसे न तो ऊंघ आती है और न नींद। जो कुछ आसमानों में है और जो कुछ ज़मीन में है, सब उसी का है। कौन है जो उसकी इजाज़त के बिना उसके पास सिफारिश कर सके? वह जानता है जो कुछ उनके आगे है और जो कुछ उनके पीछे है, और वे उसके इल्म में से किसी चीज़ का अहाता नहीं कर सकते सिवाय जितना वह चाहे। उसकी कुर्सी आसमानों और ज़मीन को घेरे हुए है, और इन दोनों की हिफ़ाज़त उसे थकाती नहीं। और वह सबसे बुलंद, सबसे बड़ा है।",
            translationUr = "اللہ! اس کے سوا کوئی معبود نہیں، وہ زندہ ہے، سب کا تھامنے والا ہے۔ نہ اسے اونگھ آتی ہے نہ نیند۔ جو کچھ آسمانوں میں ہے اور جو کچھ زمین میں ہے اسی کا ہے۔ کون ہے جو اس کی اجازت کے بغیر اس کے پاس سفارش کرے؟ وہ جانتا ہے جو کچھ ان کے آگے ہے اور جو کچھ ان کے پیچھے ہے، اور وہ اس کے علم میں سے کسی چیز کا احاطہ نہیں کر سکتے مگر جتنا وہ چاہے۔ اس کی کرسی نے آسمانوں اور زمین کو گھیرا ہوا ہے، اور ان کی حفاظت اسے نہیں تھکاتی۔ اور وہ بلند مرتبہ، بڑی عظمت والا ہے۔",
            translationAr = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ... الآية العظيمة سيدة آي القرآن الكريم",
            referenceEn = "Qur'an 2:255 (Al-Hakim 1/562)",
            referenceHi = "सूरह अल-बक़रा (2:255)",
            referenceUr = "سورۃ البقرۃ (2:255)",
            referenceAr = "سورة البقرة: 255",
            benefitEn = "Whoever recites it in the morning will be protected from jinn and evil until evening.",
            benefitHi = "जो शख्स सुबह इसे पढ़ता है, वह शाम तक जिन्नात और शैतानी शर से अल्लाह की पनाह में रहता है।",
            benefitUr = "جو شخص صبح کے وقت اسے پڑھے گا، وہ شام تک جنات اور شیاطین کے شر سے محفوظ رہے گا۔",
            hadithBook = "Al-Mustadrak 'ala al-Sahihayn 1/562 & Sahih at-Targhib 658",
            hadithGrade = "Sahih / Hasan",
            hadithNarrator = "Ubayy ibn Ka'b (RA)",
            hadithArabic = "مَنْ قَالَهَا حِينَ يُصْبِحُ أُجِيرَ مِنَّا حَتَّى يُمْسِيَ، وَمَنْ قَالَهَا حِينَ يُمْسِي أُجِيرَ مِنَّا حَتَّى يُصْبِحَ",
            hadithTranslationEn = "The Prophet ﷺ said regarding Ayat al-Kursi: 'Whoever recites it when he reaches the morning will be protected from the jinn until he reaches the evening, and whoever recites it when he reaches the evening will be protected from them until he reaches the morning.'",
            hadithTranslationHi = "रसूलुल्लाह ﷺ ने आयतुल कुर्सी के बारे में फरमाया: 'जो शख्स सुबह के वक्त इसे पढ़ेगा, वह शाम तक जिन्नात के शर से बचा रहेगा, और जो शाम को पढ़ेगा वह सुबह तक महफूज़ रहेगा।'",
            hadithTranslationUr = "نبی کریم ﷺ نے آیت الکرسی کے بارے میں فرمایا: 'جو شخص صبح کے وقت اسے پڑھے گا وہ شام تک جنات سے پناہ میں رہے گا، اور جو شخص شام کے وقت اسے پڑھے گا وہ صبح تک ان سے پناہ میں رہے گا۔'",
            hadithUrl = "https://quran.com/2/255"
        ),
        AdhkarItem(
            id = "m_ikhlas",
            order = 2,
            targetCount = 3,
            titleEn = "Surah Al-Ikhlas (3x)",
            titleHi = "सूरह अल-इख़्लास (3 बार)",
            titleUr = "سورۃ الاخلاص (3 مرتبہ)",
            titleAr = "سورة الإخلاص (3 مرات)",
            arabicText = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ ۝ قُلْ هُوَ اللَّهُ أَحَدٌ ۝ اللَّهُ الصَّمَدُ ۝ لَمْ يَلِدْ وَلَمْ يُولَدْ ۝ وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ",
            transliterationEn = "Bismillahir-Rahmanir-Rahim. Qul Huwallahu Ahad. Allahus-Samad. Lam yalid wa lam yulad. Wa lam yakul-lahu kufuwan ahad.",
            transliterationHi = "बिस्मिल्लाहिर-रहमानिर-रहीम। क़ुल हुवल्लाहु अहद। अल्लाहुस-समद। लम यलिद वलम यूलद। वलम यकुल-लहू कुफ़ुवन अहद।",
            translationEn = "In the name of Allah, the Entirely Merciful, the Especially Merciful. Say: He is Allah, [who is] One, Allah, the Eternal Refuge. He neither begets nor is born, Nor is there to Him any equivalent.",
            translationHi = "अल्लाह के नाम से जो बहुत मेहरबान, निहायत रहम फरमाने वाला है। कह दीजिए: वह अल्लाह एक है। अल्लाह बेनियाज़ (सबका सहारा) है। न उसने किसी को जना और न वह जना गया। और न कोई उसके बराबर का है।",
            translationUr = "اللہ کے نام سے جو نہایت مہربان، ہمیشہ رحم فرمانے والا ہے۔ فرما دیجئے: وہ اللہ ایک ہے۔ اللہ بے نیاز ہے۔ نہ اس کی کوئی اولاد ہے اور نہ وہ کسی کی اولاد ہے۔ اور نہ ہی کوئی اس کا ہمسر ہے۔",
            translationAr = "In the name of Allah, the Entirely Merciful, the Especially Merciful. Say: He is Allah, [who is] One, Allah, the Eternal Refuge. He neither begets nor is born, Nor is there to Him any equivalent.",
            referenceEn = "Sunan Abi Dawud 5082, Tirmidhi 3575",
            referenceHi = "सुनन अबी दाऊद 5082, तिर्मिज़ी 3575",
            referenceUr = "سنن ابی داؤد 5082، جامع ترمذی 3575",
            referenceAr = "سنن أبي داود 5082، الترمذي 3575",
            benefitEn = "Reciting Al-Ikhlas, Al-Falaq, and An-Nas 3 times in the morning and evening suffices you against everything.",
            benefitHi = "सुबह और शाम 3-3 बार मुअव्वज़ात (इख़्लास, फ़लक़, नास) पढ़ना हर बला और बुराई से हिफ़ाज़त के लिए काफी है।",
            benefitUr = "صبح اور شام 3-3 مرتبہ اخلاص، فلق اور ناس پڑھنا ہر قسم کی تکلیف اور برائی سے کفایت کرتا ہے۔",
            hadithBook = "Sunan Abi Dawud 5082, Jami` at-Tirmidhi 3575",
            hadithGrade = "Hasan Sahih (Al-Albani)",
            hadithNarrator = "Abdullah ibn Khubayb (RA)",
            hadithArabic = "قُلْ: قُلْ هُوَ اللَّهُ أَحَدٌ، وَالْمُعَوِّذَتَيْنِ، حِينَ تُمْسِي وَحِينَ تُصْبِحُ، ثَلاَثَ مَرَّاتٍ، تَكْفِيكَ مِنْ كُلِّ شَيْءٍ",
            hadithTranslationEn = "The Messenger of Allah ﷺ said: 'Recite: Qul Huwallahu Ahad and the Mu'awwidhatayn (Al-Falaq and An-Nas) three times in the evening and in the morning, and they will suffice you against everything.'",
            hadithTranslationHi = "रसूलुल्लाह ﷺ ने फरमाया: 'सुबह और शाम 3 बार क़ुल हुवल्लाहु अहद और दोनों मुअव्वज़तैन (अल-फ़लक़ और अन-नास) पढ़ लिया करो, यह तुम्हें हर चीज़ (की बुराई) से काफी हो जाएंगी।'",
            hadithTranslationUr = "نبی کریم ﷺ نے فرمایا: 'صبح اور شام تین مرتبہ قل ہو اللہ احد اور معوذتین پڑھ لیا کرو، یہ تمہیں ہر چیز سے کافی ہو جائیں گی۔'",
            hadithUrl = "https://sunnah.com/abudawud:5082"
        ),
        AdhkarItem(
            id = "m_falaq",
            order = 3,
            targetCount = 3,
            titleEn = "Surah Al-Falaq (3x)",
            titleHi = "सूरह अल-फ़लक़ (3 बार)",
            titleUr = "سورۃ الفلق (3 مرتبہ)",
            titleAr = "سورة الفلق (3 مرات)",
            arabicText = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ ۝ قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ ۝ مِن شَرِّ مَا خَلَقَ ۝ وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ ۝ وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ ۝ وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ",
            transliterationEn = "Bismillahir-Rahmanir-Rahim. Qul a'udhu bi-Rabbil-falaq. Min sharri ma khalaq. Wa min sharri ghasiqin idha waqab. Wa min sharrin-naffathati fil-'uqad. Wa min sharri hasidin idha hasad.",
            transliterationHi = "बिस्मिल्लाहिर-रहमानिर-रहीम। क़ुल अऊज़ु बिरब्बिल-फ़लक़। मिन शर्री मा ख़लक़। वमिन शर्री ग़ासिक़िन इज़ा वक़ब। वमिन शर्रिन-नफ़्फ़ासाति फ़िल-'उक़द। वमिन शर्री हासिदिन इज़ा हसद।",
            translationEn = "In the name of Allah, the Entirely Merciful, the Especially Merciful. Say: I seek refuge in the Lord of daybreak from the evil of that which He created, and from the evil of darkness when it settles, and from the evil of the blowers in knots, and from the evil of an envier when he envies.",
            translationHi = "अल्लाह के नाम से जो बहुत मेहरबान, निहायत रहम फरमाने वाला है। कह दीजिए: मैं सुबह के रब की पनाह मांगता हूं, हर उस चीज़ की बुराई से जो उसने पैदा की, और अंधेरी रात की बुराई से जब वह छा जाए, और गांठों पर फूंकने वाली औरतों (जादूगरनियों) की बुराई से, और हसद करने वाले की बुराई से जब वह हसद करे।",
            translationUr = "اللہ کے نام سے جو نہایت مہربان، ہمیشہ رحم فرمانے والا ہے۔ فرما دیجئے: میں صبح کے رب کی پناہ مانگتا ہوں، ہر اس چیز کے شر سے جو اس نے پیدا کی، اور اندھیری رات کے شر سے جب وہ چھا جائے، اور گرہوں میں پھونکنے والیوں کے شر سے، اور حسد کرنے والے کے شر سے جب وہ حسد کرے۔",
            translationAr = "In the name of Allah, the Entirely Merciful, the Especially Merciful. Say: I seek refuge in the Lord of daybreak from the evil of that which He created, and from the evil of darkness when it settles, and from the evil of the blowers in knots, and from the evil of an envier when he envies.",
            referenceEn = "Sunan Abi Dawud 5082, Tirmidhi 3575",
            referenceHi = "सुनन अबी दाऊद 5082, तिर्मिज़ी 3575",
            referenceUr = "سنن ابی داؤد 5082، جامع ترمذی 3575",
            referenceAr = "سنن أبي داود 5082، الترمذي 3575",
            benefitEn = "Shields against black magic, evil eye, jealousy, and dark spiritual harm.",
            benefitHi = "जादू, नज़र-ए-बद, हसद और हर तरह की छुपी हुई बुराई से हिफ़ाज़त की ढाल है।",
            benefitUr = "جادو، نظر بد، حسد اور ہر قسم کے پوشیدہ شر سے حفاظت کی مضبوط ڈھال ہے۔",
            hadithBook = "Sunan Abi Dawud 5082, Jami` at-Tirmidhi 3575",
            hadithGrade = "Hasan Sahih",
            hadithNarrator = "Abdullah ibn Khubayb (RA)",
            hadithArabic = "قُلْ هُوَ اللَّهُ أَحَدٌ وَالْمُعَوِّذَتَيْنِ حِينَ تُمْسِي وَحِينَ تُصْبِحُ ثَلَاثَ مَرَّاتٍ تَكْفِيكَ مِنْ كُلِّ شَيْءٍ",
            hadithTranslationEn = "Reciting the 3 Quls (Ikhlas, Falaq, Nas) 3 times every morning and evening protects from all evil.",
            hadithTranslationHi = "सुबह और शाम 3 बार तीनों क़ुल पढ़ना इंसान को हर परेशानी और शैतानी शर से महफूज़ रखता है।",
            hadithTranslationUr = "رسول اللہ ﷺ نے فرمایا: 'ان سورتوں کو صبح اور شام پڑھ لیا کرو، یہ ہر قسم کے شر اور برائی سے بچاؤ کے لیے کافی ہیں۔'",
            hadithUrl = "https://sunnah.com/abudawud:5082"
        ),
        AdhkarItem(
            id = "m_nas",
            order = 4,
            targetCount = 3,
            titleEn = "Surah An-Nas (3x)",
            titleHi = "सूरह अन-नास (3 बार)",
            titleUr = "سورۃ الناس (3 مرتبہ)",
            titleAr = "سورة الناس (3 مرات)",
            arabicText = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ ۝ قُلْ أَعُوذُ بِرَبِّ النَّاسِ ۝ مَلِكِ النَّاسِ ۝ إِلَٰهِ النَّاسِ ۝ مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ ۝ الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ ۝ مِنَ الْجِنَّةِ وَالنَّاسِ",
            transliterationEn = "Bismillahir-Rahmanir-Rahim. Qul a'udhu bi-Rabbin-nas. Malikin-nas. Ilahin-nas. Min sharril-waswasil-khannas. Alladhi yuwaswisu fi sudurin-nas. Minal-jinnati wan-nas.",
            transliterationHi = "बिस्मिल्लाहिर-रहमानिर-रहीम। क़ुल अऊज़ु बिरब्बिन-नास। मलिकिन-नास। इलाहिन-नास। मिन शर्रिल-वस्वासिल-ख़न्नास। अल्लज़ी युवस्विसु फ़ी सुदूरिन-नास। मिनल-जिन्नति वन-नास।",
            translationEn = "In the name of Allah, the Entirely Merciful, the Especially Merciful. Say: I seek refuge in the Lord of mankind, the Sovereign of mankind, the God of mankind, from the evil of the retreating whisperer who whispers into the breasts of mankind, from among the jinn and mankind.",
            translationHi = "अल्लाह के नाम से जो बहुत मेहरबान, निहायत रहम फरमाने वाला है। कह दीजिए: मैं इंसानों के रब की पनाह मांगता हूं, इंसानों के बादशाह की, इंसानों के सच्चे माबूद की, बार-बार पीछे हटकर वसवसा डालने वाले शैतान की बुराई से, जो इंसानों के सीनों में वसवसे डालता है, चाहे वह जिन्नों में से हो या इंसानों में से।",
            translationUr = "اللہ کے نام سے جو نہایت مہربان، ہمیشہ رحم فرمانے والا ہے۔ فرما دیجئے: میں انسانوں کے رب کی پناہ مانگتا ہوں، انسانوں کے بادشاہ کی، انسانوں کے معبود کی، وسوسہ ڈالنے والے، پیچھے ہٹ جانے والے کے شر سے، جو لوگوں کے سینوں میں وسوسے ڈالتا ہے، خواہ وہ جنات میں سے ہو یا انسانوں میں سے۔",
            translationAr = "In the name of Allah, the Entirely Merciful, the Especially Merciful. Say: I seek refuge in the Lord of mankind, the Sovereign of mankind, the God of mankind, from the evil of the retreating whisperer who whispers into the breasts of mankind, from among the jinn and mankind.",
            referenceEn = "Sunan Abi Dawud 5082, Tirmidhi 3575",
            referenceHi = "सुनन अबी दाऊद 5082, तिर्मिज़ी 3575",
            referenceUr = "سنن ابی داؤد 5082، جامع ترمذی 3575",
            referenceAr = "سنن أبي داود 5082، الترمذي 3575",
            benefitEn = "Protection against the evil whispers of shaytan, doubts, anxiety, and internal spiritual attacks.",
            benefitHi = "शैतानी वसवसों, शक-ओ-शुबहात, घबराहट और बुरे ख्यालात से हिफ़ाज़त का अज़ीम ज़रिया है।",
            benefitUr = "شیطانی وسوسوں، شکوک و شبہات، بے چینی اور برے خیالات سے حفاظت کا عظیم ذریعہ ہے۔",
            hadithBook = "Sunan Abi Dawud 5082, Jami` at-Tirmidhi 3575",
            hadithGrade = "Hasan Sahih",
            hadithNarrator = "Abdullah ibn Khubayb (RA)",
            hadithArabic = "قُلْ هُوَ اللَّهُ أَحَدٌ وَالْمُعَوِّذَتَيْنِ حِينَ تُمْسِي وَحِينَ تُصْبِحُ ثَلَاثَ مَرَّاتٍ تَكْفِيكَ مِنْ كُلِّ شَيْءٍ",
            hadithTranslationEn = "The Prophet ﷺ emphasized repeating these 3 Surahs three times every morning and evening.",
            hadithTranslationHi = "नबी करीम ﷺ ने सुबह और शाम इन तीनों सूरतों को 3 बार पढ़ने की ताकीद फरमाई।",
            hadithTranslationUr = "رسول اللہ ﷺ نے فرمایا: 'لوگوں نے پناہ مانگنے کے لیے ان دونوں سورتوں (معوذتین) جیسی کوئی دوسری چیز نہیں پائی۔'",
            hadithUrl = "https://sunnah.com/abudawud:5082"
        ),
        AdhkarItem(
            id = "m_sayyidul_istighfar",
            order = 5,
            targetCount = 1,
            titleEn = "Sayyid al-Istighfar",
            titleHi = "सय्यिदुल इस्तिग़फ़ार",
            titleUr = "سید الاستغفار",
            titleAr = "سيد الاستغفار",
            arabicText = "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَٰهَ إِلَّا أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَىٰ عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ لَكَ بِذَنْبِي، فَاغْفِرْ لِي، فَإِنَّهُ لَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ",
            transliterationEn = "Allahumma Anta Rabbi la ilaha illa Anta, khalaqtani wa ana 'abduka, wa ana 'ala 'ahdika wa wa'dika mastata'tu, a'udhu bika min sharri ma sana'tu, abu'u laka bi-ni'matika 'alayya, wa abu'u laka bi-dhanbi faghfir li, fa-innahu la yaghfirudh-dhunuba illa Anta.",
            transliterationHi = "अल्लाहुम्मा अंता रब्बी ला इलाहा इल्ला अंत, ख़लक़तनी व अना 'अब्दुक, व अना 'अला 'अहदिका व व'दिका मस्ततअतु, अऊज़ु बिका मिन शर्री मा सनअतु, अबूउ लका बि-नि'मतिका 'अलैय्या, व अबूउ लका बि-ज़म्बी फ़ग़फ़िर ली, फ़-इन्नहू ला यग़फ़िरुज़-ज़ुनूबा इल्ला अंत।",
            translationEn = "O Allah, You are my Lord, none has the right to be worshipped except You. You created me and I am Your servant, and I abide by Your covenant and promise as best I can. I seek refuge in You from the evil of what I have done. I acknowledge Your favors upon me, and I confess my sins to You, so forgive me, for none can forgive sins except You.",
            translationHi = "ऐ अल्लाह! तू ही मेरा रब है, तेरे सिवा कोई सच्चा माबूद नहीं, तूने ही मुझे पैदा किया और मैं तेरा बंदा हूं, और अपनी ताक़त के मुताबिक तेरे अहद और वादे पर क़ायम हूं। मैंने जो कुछ किया उसके शर से तेरी पनाह मांगता हूं। मुझ पर जो तेरी नेमतें हैं उनका एतराफ़ करता हूं और अपने गुनाहों का इक़रार करता हूं, पस मुझे बख़्श दे, क्योंकि तेरे सिवा कोई गुनाहों को बख़्शने वाला नहीं।",
            translationUr = "اے اللہ! تو ہی میرا رب ہے، تیرے سوا کوئی معبود نہیں، تو نے ہی مجھے پیدا کیا اور میں تیرا بندہ ہوں، اور میں اپنی استطاعت کے مطابق تیرے عہد اور وعدے پر قائم ہوں۔ میں اپنے اعمال کے شر سے تیری پناہ مانگتا ہوں۔ مجھ پر تیری جو نعمتیں ہیں ان کا اقرار کرتا ہوں اور اپنے گناہوں کا اعتراف کرتا ہوں، پس مجھے بخش دے، کیونکہ تیرے سوا گناہوں کو کوئی معاف نہیں کر سکتا۔",
            translationAr = "O Allah, You are my Lord, none has the right to be worshipped except You. You created me and I am Your servant, and I abide by Your covenant and promise as best I can. I seek refuge in You from the evil of what I have done. I acknowledge Your favors upon me, and I confess my sins to You, so forgive me, for none can forgive sins except You.",
            referenceEn = "Sahih al-Bukhari 6306",
            referenceHi = "सहीह बुख़ारी 6306",
            referenceUr = "صحیح بخاری 6306",
            referenceAr = "صحيح البخاري: 6306",
            benefitEn = "Whoever recites it during the day with firm faith and dies before evening will be among the people of Paradise.",
            benefitHi = "जो शख्स यक़ीन के साथ सुबह इसे पढ़े और शाम से पहले उसका इंतिक़ाल हो जाए, वह जन्नत वालों में से होगा।",
            benefitUr = "جس نے یقین کے ساتھ صبح کے وقت یہ دعا پڑھی اور شام سے پہلے وفات پا گیا تو وہ اہلِ جنت میں سے ہوگا۔",
            hadithBook = "Sahih al-Bukhari 6306",
            hadithGrade = "Sahih",
            hadithNarrator = "Shaddad ibn Aws (RA)",
            hadithArabic = "سَيِّدُ الاِسْتِغْفَارِ أَنْ تَقُولَ: اللَّهُمَّ أَنْتَ رَبِّي لاَ إِلَهَ إِلاَّ أَنْتَ... وَمَنْ قَالَهَا مِنَ النَّهَارِ مُوقِنًا بِهَا، فَمَاتَ مِنْ يَوْمِهِ قَبْلَ أَنْ يُمْسِيَ، فَهُوَ مِنْ أَهْلِ الْجَنَّةِ",
            hadithTranslationEn = "The Prophet ﷺ said: 'The most superior way of asking for forgiveness from Allah is to say: Allahumma Anta Rabbi... If somebody recites it during the day with firm faith in it, and dies on the same day before evening, he will be from the people of Paradise.'",
            hadithTranslationHi = "नबी करीम ﷺ ने फरमाया: 'तमाम इस्तिग़फ़ारों का सरदार (सय्यिदुल इस्तिग़फ़ार) यह है कि तुम कहो: अल्लाहुम्मा अंता रब्बी... जिसने इसे दिन में कामिल यक़ीन के साथ कहा और उसी दिन शाम से पहले मर गया तो वह जन्नती है।'",
            hadithTranslationUr = "نبی کریم ﷺ نے فرمایا: 'جس نے سید الاستغفار پر یقین رکھتے ہوئے دن میں اسے پڑھا اور اسی دن شام سے پہلے انتقال کر گیا تو وہ جنتی ہے، اور جس نے رات کو پڑھا اور صبح سے پہلے انتقال کر گیا تو وہ جنتی ہے۔'",
            hadithUrl = "https://sunnah.com/bukhari:6306"
        ),
        AdhkarItem(
            id = "m_subhanallah_100",
            order = 6,
            targetCount = 100,
            titleEn = "SubhanAllahi wa bihamdihi (100x)",
            titleHi = "सुब्हानल्लाहि व बिहम्दिही (100 बार)",
            titleUr = "سبحان اللہ وبحمدہ (100 مرتبہ)",
            titleAr = "سبحان الله وبحمده (100 مرة)",
            arabicText = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ",
            transliterationEn = "SubhanAllahi wa bihamdih.",
            transliterationHi = "सुब्हानल्लाहि व बिहम्दिही।",
            translationEn = "Glory is to Allah and all praise is to Him.",
            translationHi = "अल्लाह पाक है अपनी तारीफ़ों के साथ (तमाम पाकीज़गी और तारीफ़ें अल्लाह ही के लिए हैं)।",
            translationUr = "اللہ پاک ہے اپنی تمام تر تعریفوں کے ساتھ۔",
            translationAr = "Glory is to Allah and all praise is to Him.",
            referenceEn = "Sahih Muslim 2692, Sahih al-Bukhari 6405",
            referenceHi = "सहीह मुस्लिम 2692, सहीह बुख़ारी 6405",
            referenceUr = "صحیح مسلم 2692، صحیح بخاری 6405",
            referenceAr = "صحيح مسلم 2692، صحيح البخاري 6405",
            benefitEn = "Whoever says this 100 times a day, all his sins will be forgiven even if they were like the foam of the sea, and no one will bring better deeds on Judgment Day.",
            benefitHi = "जो शख्स दिन में 100 बार इसे पढ़ेगा, उसके तमाम गुनाह माफ़ कर दिए जाएंगे चाहे वे समंदर के झाग के बराबर ही क्यों न हों।",
            benefitUr = "جو شخص دن میں 100 مرتبہ یہ پڑھے گا، اس کے تمام گناہ معاف کر دیے جائیں گے خواہ وہ سمندر کی جھاگ کے برابر ہی کیوں نہ ہوں۔",
            hadithBook = "Sahih Muslim 2692, Sahih al-Bukhari 6405",
            hadithGrade = "Sahih",
            hadithNarrator = "Abu Hurairah (RA)",
            hadithArabic = "مَنْ قَالَ: سُبْحَانَ اللهِ وَبِحَمْدِهِ، فِي يَوْمٍ مِائَةَ مَرَّةٍ، حُطَّتْ خَطَايَاهُ، وَإِنْ كَانَتْ مِثْلَ زَبَدِ الْبَحْرِ",
            hadithTranslationEn = "The Messenger of Allah ﷺ said: 'Whoever says: SubhanAllahi wa bihamdihi 100 times a day, his sins will be forgiven even if they were like the foam of the sea.'",
            hadithTranslationHi = "रसूलुल्लाह ﷺ ने फरमाया: 'जिसने एक दिन में सौ बार सुब्हानल्लाहि व बिहम्दिही कहा, उसके गुनाह मिटा दिए जाते हैं अगरचे वे समंदर की झाग जितने हों।'",
            hadithTranslationUr = "رسول اللہ ﷺ نے فرمایا: 'جس نے دن میں سو مرتبہ سُبْحَانَ اللَّهِ وَبِحَمْدِهِ کہا، اس کے گناہ معاف کر دیے جائیں گے اگرچہ سمندر کی جھاگ کے برابر ہی کیوں نہ ہوں۔'",
            hadithUrl = "https://sunnah.com/muslim:2692"
        ),
        AdhkarItem(
            id = "m_bika_asbahna",
            order = 7,
            targetCount = 1,
            titleEn = "Allahumma bika asbahna",
            titleHi = "अल्लाहुम्मा बिका असबहना",
            titleUr = "اللہم بک اصبحنا",
            titleAr = "اللهم بك أصبحنا",
            arabicText = "اللَّهُمَّ بِكَ أَصْبَحْنَا، وَبِكَ أَمْسَيْنَا، وَبِكَ نَحْيَا، وَبِكَ نَمُوتُ، وَإِلَيْكَ النُّشُورُ",
            transliterationEn = "Allahumma bika asbahna, wa bika amsayna, wa bika nahya, wa bika namutu, wa ilaykan-nushur.",
            transliterationHi = "अल्लाहुम्मा बिका असबहना, व बिका अमसैना, व बिका नहया, व बिका नमूतू, व इलैकन-नुशूर।",
            translationEn = "O Allah, by You we enter the morning, and by You we enter the evening, by You we live, and by You we die, and to You is the final resurrection.",
            translationHi = "ऐ अल्लाह! तेरी ही मदद से हमने सुबह की, और तेरी ही मदद से हमने शाम की, तेरे ही हुक्म से हम जीते हैं, और तेरे ही हुक्म से हम मरते हैं, और तेरी ही तरफ दोबारा जी उठना है।",
            translationUr = "اے اللہ! تیری ہی توفیق سے ہم نے صبح کی، اور تیری ہی توفیق سے ہم نے شام کی، تیرے ہی حکم سے ہم جیتے ہیں اور تیرے ہی حکم سے ہم مرتے ہیں، اور تیری ہی طرف اٹھ کر جانا ہے۔",
            translationAr = "O Allah, by You we enter the morning, and by You we enter the evening, by You we live, and by You we die, and to You is the final resurrection.",
            referenceEn = "Sunan Abi Dawud 5068, Tirmidhi 3391",
            referenceHi = "सुनन अबी दाऊद 5068, तिर्मिज़ी 3391",
            referenceUr = "سنن ابی داؤد 5068، جامع ترمذی 3391",
            referenceAr = "سنن أبي داود 5068، الترمذي 3391",
            benefitEn = "Acknowledging complete reliance on Allah in life, death, morning, evening, and the Hereafter.",
            benefitHi = "ज़िंदगी, मौत, सुबह, शाम और आखिरत में अल्लाह तआला पर मुकम्मल तवक्कुल और ईमान का इज़हार।",
            benefitUr = "زندگی، موت، صبح، شام اور آخرت میں اللہ تعالیٰ پر مکمل توکل اور ایمان کا اظہار۔",
            hadithBook = "Sunan Abi Dawud 5068, Jami` at-Tirmidhi 3391",
            hadithGrade = "Sahih (Al-Albani)",
            hadithNarrator = "Abu Hurairah (RA)",
            hadithArabic = "كَانَ النَّبِيُّ صلى الله عليه وسلم إِذَا أَصْبَحَ قَالَ: اللَّهُمَّ بِكَ أَصْبَحْنَا وَبِكَ أَمْسَيْنَا وَبِكَ نَحْيَا وَبِكَ نَمُوتُ وَإِلَيْكَ النُّشُورُ",
            hadithTranslationEn = "The Prophet ﷺ used to say in the morning: 'Allahumma bika asbahna wa bika amsayna wa bika nahya wa bika namutu wa ilaykan-nushur.'",
            hadithTranslationHi = "नबी करीम ﷺ जब सुबह करते तो यह दुआ फरमाते थे।",
            hadithTranslationUr = "رسول اللہ ﷺ صبح کے وقت یہ دعا پڑھا کرتے تھے اور شام کے وقت 'أمْسَيْنَا وَأمْسَى المُلْكُ لِلَّهِ' کے الفاظ سے دعا فرماتے تھے۔",
            hadithUrl = "https://sunnah.com/abudawud:5068"
        ),
        AdhkarItem(
            id = "m_afwa_afiyah",
            order = 8,
            targetCount = 1,
            titleEn = "Dua for Comprehensive Well-being",
            titleHi = "मुकम्मल आफ़ियत और हिफ़ाज़त की दुआ",
            titleUr = "جامع عافیت اور حفاظت کی دعا",
            titleAr = "دعاء العفو والعافية",
            arabicText = "اللَّهُمَّ إِنِّي أَسْأَلُكَ الْعَافِيَةَ فِي الدُّنْيَا وَالْآخِرَةِ، اللَّهُمَّ إِنِّي أَسْأَلُكَ الْعَفْوَ وَالْعَافِيَةَ فِي دِينِي وَدُنْيَايَ وَأَهْلِي وَمَالِي، اللَّهُمَّ اسْتُرْ عَوْرَاتِي وَآمِنْ رَوْعَاتِي، اللَّهُمَّ احْفَظْنِي مِنْ بَيْنِ يَدَيَّ، وَمِنْ خَلْفِي، وَعَنْ يَمِينِي، وَعَنْ شِمَالِي، وَمِنْ فَوْقِي، وَأَعُوذُ بِعَظَمَتِكَ أَنْ أُغْتَالَ مِنْ تَحْتِي",
            transliterationEn = "Allahumma inni as'alukal-'afiyata fid-dunya wal-akhirah. Allahumma inni as'alukal-'afwa wal-'afiyata fi dini wa dunyaya wa ahli wa mali. Allahummastur 'awrati wa amin raw'ati. Allahummahfazni mim-bayni yadayya, wa min khalfi, wa 'an yamini, wa 'an shimali, wa min fawqi, wa a'udhu bi-'azamatika an ughtala min tahti.",
            transliterationHi = "अल्लाहुम्मा इन्नी असअलोकल-'आफ़ियता फ़िद-दुनिया वल-आख़िरह। अल्लाहुम्मा इन्नी असअलोकल-'अफ़्वा वल-'आफ़ियता फ़ी दीनी व दुनियाया व अहलि व माली। अल्लाहुम्मस-तुर औरतिया व आमिन रव'आति। अल्लाहुमह-फ़ज़नी मिम-बैनि यदय्या, व मिन ख़ल्फ़ी, व 'अन यमीनी, व 'अन शिमाली, व मिन फ़ौक़ी, व अऊज़ु बि-'अज़मतिका अन उग़्ताल मिन तहती।",
            translationEn = "O Allah, I ask You for well-being in this world and the Hereafter. O Allah, I ask You for forgiveness and well-being in my religion, my worldly affairs, my family, and my wealth. O Allah, conceal my faults and keep me safe from my fears. O Allah, protect me from before me, from behind me, on my right, on my left, and from above me, and I seek refuge in Your greatness from being struck down from beneath me.",
            translationHi = "ऐ अल्लाह! मैं तुझसे दुनिया और आख़िरत में आफ़ियत (सलामती) का सवाल करता हूं। ऐ अल्लाह! मैं तुझसे अपने दीन, अपनी दुनिया, अपने अहल-ओ-अयाल और अपने माल में दरगुज़र और आफ़ियत मांगता हूं। ऐ अल्लाह! मेरे ऐबों को छुपा ले और मुझे मेरे खौफ से अमन दे। ऐ अल्लाह! मेरी हिफ़ाज़त फरमा मेरे आगे से, मेरे पीछे से, मेरे दाएं से, मेरे बाएं से और मेरे ऊपर से, और मैं तेरी अज़मत की पनाह मांगता हूं कि नीचे से अचानक हलाक कर दिया जाऊं।",
            translationUr = "اے اللہ! میں تجھ سے دنیا اور آخرت میں عافیت کا سوال کرتا ہوں۔ اے اللہ! میں تجھ سے اپنے دین، اپنی دنیا، اپنے اہل و عیال اور اپنے مال میں معافی اور عافیت مانگتا ہوں۔ اے اللہ! میرے عیبوں پر پردہ ڈال دے اور میرے خوف کو امن میں بدل دے۔ اے اللہ! میری حفاظت فرما میرے سامنے سے، میرے پیچھے سے، میرے دائیں سے، میرے بائیں سے، اور میرے اوپر سے، اور میں تیری عظمت کی پناہ مانگتا ہوں اس بات سے کہ اچانک نیچے سے ہلاک کر دیا جاؤں۔",
            translationAr = "O Allah, I ask You for well-being in this world and the Hereafter. O Allah, I ask You for forgiveness and well-being in my religion, my worldly affairs, my family, and my wealth. O Allah, conceal my faults and keep me safe from my fears. O Allah, protect me from before me, from behind me, on my right, on my left, and from above me, and I seek refuge in Your greatness from being struck down from beneath me.",
            referenceEn = "Sunan Abi Dawud 5074, Ibn Majah 3871",
            referenceHi = "सुनन अबी दाऊद 5074, इब्न माजा 3871",
            referenceUr = "سنن ابی داؤد 5074، ابن ماجہ 3871",
            referenceAr = "سنن أبي داود 5074، ابن ماجه 3871",
            benefitEn = "The Messenger of Allah ﷺ never omitted these supplications in the morning and evening, covering protection from all 6 directions.",
            benefitHi = "रसूलुल्लाह ﷺ ने सुबह और शाम कभी इन कलिमात को नहीं छोड़ा; यह छहों दिशाओं से हिफ़ाज़त की अज़ीम दुआ है।",
            benefitUr = "رسول اللہ ﷺ نے صبح اور شام کبھی ان کلمات کو ترک نہیں فرمایا؛ یہ چھ کے چھ اطراف سے کامل حفاظت کی دعا ہے۔",
            hadithBook = "Sunan Abi Dawud 5074, Sunan Ibn Majah 3871",
            hadithGrade = "Sahih (Al-Albani)",
            hadithNarrator = "Abdullah ibn Umar (RA)",
            hadithArabic = "لَمْ يَكُنْ رَسُولُ اللَّهِ صلى الله عليه وسلم يَدَعُ هَؤُلاَءِ الدَّعَوَاتِ حِينَ يُمْسِي وَحِينَ يُصْبِحُ: اللَّهُمَّ إِنِّي أَسْأَلُكَ الْعَافِيَةَ فِي الدُّنْيَا وَالآخِرَةِ...",
            hadithTranslationEn = "Ibn Umar reported: The Messenger of Allah ﷺ never abandoned these supplications when evening came and when morning came.",
            hadithTranslationHi = "हज़रत इब्ने उमर (रज़ि.) से रिवायत है कि रसूलुल्लाह ﷺ सुबह और शाम कभी इन दुआओं को नहीं छोड़ते थे।",
            hadithTranslationUr = "عبداللہ بن عمر رضی اللہ عنہما نے فرمایا کہ رسول اللہ ﷺ صبح اور شام کے وقت ان کلمات کو کبھی نہیں چھوڑتے تھے جس میں دین، دنیا، اہل و عیال اور جان و مال کی عافیت مانگی جاتی تھی۔",
            hadithUrl = "https://sunnah.com/abudawud:5074"
        ),
        AdhkarItem(
            id = "m_asbahna_mulk",
            order = 9,
            targetCount = 1,
            titleEn = "Asbahna wa Asbahal-Mulku Lillah",
            titleHi = "असबहना व असबहुल-मुल्कु लिल्लाह",
            titleUr = "اصبحنا واصبح الملک للہ",
            titleAr = "أصبحنا وأصبح الملك لله",
            arabicText = "أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لَا إِلَٰهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ، وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ، رَبِّ أَسْأَلُكَ خَيْرَ مَا فِي هَٰذَا الْيَوْمِ وَخَيْرَ مَا بَعْدَهُ، وَأَعُوذُ بِكَ مِنْ شَرِّ مَا فِي هَٰذَا الْيَوْمِ وَشَرِّ مَا بَعْدَهُ، رَبِّ أَعُوذُ بِكَ مِنَ الْكَسَلِ وَسُوءِ الْكِبَرِ، رَبِّ أَعُوذُ بِكَ مِنْ عَذَابٍ فِي النَّارِ وَعَذَابٍ فِي الْقَبْرِ",
            transliterationEn = "Asbahna wa asbahal-mulku lillah, wal-hamdu lillah, la ilaha illAllahu wahdahu la sharika lah, lahul-mulku wa lahul-hamd, wa Huwa 'ala kulli shay'in qadir. Rabbi as'aluka khayra ma fi hadhal-yawmi wa khayra ma ba'dahu, wa a'udhu bika min sharri ma fi hadhal-yawmi wa sharri ma ba'dahu, Rabbi a'udhu bika minal-kasali wa su'il-kibar, Rabbi a'udhu bika min 'adhabin fin-nari wa 'adhabin fil-qabr.",
            transliterationHi = "असबह्ना व असबहुल-मुल्कु लिल्लाह, वल-हम्दु लिल्लाह, ला इलाहा इल्लल्लाहु वहदहू ला शरीका लह, लहुल-मुल्कु व लहुल-हम्द, व हुवा 'अला कुल्लि शैइन क़दीर। रब्बि असअलोका ख़ैरा मा फ़ी हाज़ल-यौमि व ख़ैरा मा ब'दह, व अऊज़ु बिका मिन शर्रि मा फ़ी हाज़ल-यौमि व शर्रि मा ब'दह। रब्बि अऊज़ु बिका मिनल-कसलि व सूइल-किबर, रब्बि अऊज़ु बिका मिन 'अज़ाबिन फ़िन-नारि व 'अज़ाबिन फ़िल-क़ब्र।",
            translationEn = "We have reached the morning and the sovereignty belongs to Allah, and all praise is to Allah. There is no god except Allah alone, without partner; to Him belongs all sovereignty and to Him belongs all praise, and He is over all things competent. My Lord, I ask You for the good of this day and the good of what follows it, and I seek refuge in You from the evil of this day and the evil of what follows it. My Lord, I seek refuge in You from laziness and helpless old age. My Lord, I seek refuge in You from torment in the Fire and torment in the grave.",
            translationHi = "हमने सुबह की और अल्लाह के तमाम मुल्क ने सुबह की, और सब तारीफें अल्लाह के लिए हैं। अल्लाह के सिवा कोई माबूद नहीं, वह अकेला है, उसका कोई शरीक नहीं, उसी की बादशाही है और उसी के लिए तमाम तारीफ़ है, और वह हर चीज़ पर क़ादिर है। ऐ मेरे रब! मैं तुझसे भलाई मांगता हूं जो कुछ इस दिन में है और जो कुछ इसके बाद है, और मैं तेरी पनाह मांगता हूं उस बुराई से जो इस दिन में है और जो इसके बाद है। ऐ मेरे रब! मैं तेरी पनाह मांगता हूं सुस्ती से और बुढ़ापे की खराबी से। ऐ मेरे रब! मैं तेरी पनाह मांगता हूं आग (जहन्नम) के अज़ाब से और क़ब्र के अज़ाब से।",
            translationUr = "ہم نے صبح کی اور اللہ کی تمام سلطنت نے صبح کی، اور تمام تعریفیں اللہ ہی کے لیے ہیں۔ اللہ کے سوا کوئی معبود نہیں، وہ اکیلا ہے، اس کا کوئی شریک نہیں، اسی کی بادشاہی ہے اور اسی کی حمد ہے، اور وہ ہر چیز پر قادر ہے۔ اے میرے رب! میں تجھ سے خیر مانگتا ہوں جو اس دن میں ہے اور جو اس کے بعد ہے، اور میں تیری پناہ مانگتا ہوں اس برائی سے جو اس دن میں ہے اور جو اس کے بعد ہے۔ اے میرے رب! میں تیری پناہ مانگتا ہوں سستی سے اور بڑھاپے کی خرابی سے۔ اے میرے رب! میں تیری پناہ مانگتا ہوں جہنم کے عذاب سے اور قبر کے عذاب سے۔",
            translationAr = "We have reached the morning and the sovereignty belongs to Allah, and all praise is to Allah. There is no god except Allah alone, without partner; to Him belongs all sovereignty and to Him belongs all praise, and He is over all things competent. My Lord, I ask You for the good of this day and the good of what follows it, and I seek refuge in You from the evil of this day and the evil of what follows it. My Lord, I seek refuge in You from laziness and helpless old age. My Lord, I seek refuge in You from torment in the Fire and torment in the grave.",
            referenceEn = "Sahih Muslim 2723",
            referenceHi = "सहीह मुस्लिम 2723",
            referenceUr = "صحیح مسلم 2723",
            referenceAr = "صحيح مسلم: 2723",
            benefitEn = "Comprehensive protection for the entire day from laziness, evil events, Hellfire, and punishment of the grave.",
            benefitHi = "पूरे दिन की भलाई, सुस्ती से नजात, जहन्नम और क़ब्र के अज़ाब से हिफ़ाज़त की मुकम्मल दुआ।",
            benefitUr = "پورے دن کی خیر و برکت، سستی سے نجات، جہنم اور قبر کے عذاب سے حفاظت کی جامع دعا۔",
            hadithBook = "Sahih Muslim 2723",
            hadithGrade = "Sahih",
            hadithNarrator = "Abdullah ibn Mas'ud (RA)",
            hadithArabic = "كَانَ نَبِيُّ اللهِ صَلَّى اللهُ عَلَيْهِ وَسَلَّمَ، إِذَا أَصْبَحَ قَالَ: أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ...",
            hadithTranslationEn = "When morning came, the Prophet of Allah ﷺ used to say: Asbahna wa asbahal-mulku lillah...",
            hadithTranslationHi = "नबी करीम ﷺ जब सुबह करते तो यह दुआ पढ़ते थे।",
            hadithTranslationUr = "رسول اللہ ﷺ صبح کے وقت یہ کلمات پڑھا کرتے تھے جس میں تمام تر بادشاہی اور تعریف صرف اللہ کے لیے تسلیم کی جاتی ہے۔",
            hadithUrl = "https://sunnah.com/muslim:2723"
        ),
        AdhkarItem(
            id = "m_raditu_billah",
            order = 10,
            targetCount = 3,
            titleEn = "Raditu Billahi Rabban (3x)",
            titleHi = "रदीतु बिल्लाहि रब्बन (3 बार)",
            titleUr = "رضیت باللہ ربا (3 مرتبہ)",
            titleAr = "رضيت بالله ربا (3 مرات)",
            arabicText = "رَضِيتُ بِاللَّهِ رَبًّا، وَبِالْإِسْلَامِ دِينًا، وَبِمُحَمَّدٍ ﷺ نَبِيًّا",
            transliterationEn = "Raditu Billahi Rabban, wa bil-Islami dinan, wa bi-Muhammadin sallAllahu 'alayhi wa sallama Nabiyya.",
            transliterationHi = "रदीतु बिल्लाहि रब्बन, व बिल-इस्लामि दीनन, व बि-मुहम्मदिन सल्लल्लाहु अलैहि वसल्लम नबिय्या।",
            translationEn = "I am pleased with Allah as my Lord, with Islam as my religion, and with Muhammad (peace and blessings of Allah be upon him) as my Prophet.",
            translationHi = "मैं अल्लाह के रब होने पर, इस्लाम के दीन होने पर, और हज़रत मुहम्मद ﷺ के नबी होने पर राज़ी और खुश हुआ।",
            translationUr = "میں راضی ہوا اللہ کے رب ہونے پر، اسلام کے دین ہونے پر، اور حضرت محمد ﷺ کے نبی ہونے پر۔",
            translationAr = "I am pleased with Allah as my Lord, with Islam as my religion, and with Muhammad (peace and blessings of Allah be upon him) as my Prophet.",
            referenceEn = "Jami` at-Tirmidhi 3389, Abu Dawud 5072",
            referenceHi = "तिर्मिज़ी 3389, अबी दाऊद 5072",
            referenceUr = "جامع ترمذی 3389، ابی داؤد 5072",
            referenceAr = "الترمذي 3389، أبو داود 5072",
            benefitEn = "Allah takes it upon Himself to please whoever says this 3 times in the morning and evening on the Day of Resurrection.",
            benefitHi = "जो शख्स सुबह और शाम 3 बार यह पढ़े, अल्लाह तआला पर यह हक हो जाता है कि वह क़यामत के दिन उसे राज़ी और खुश फरमाए।",
            benefitUr = "جو شخص صبح اور شام 3 مرتبہ یہ کلمات کہے، اللہ تعالیٰ پر یہ حق ہے کہ قیامت کے دن اسے راضی اور خوش فرمائے۔",
            hadithBook = "Jami` at-Tirmidhi 3389, Sunan Abi Dawud 5072",
            hadithGrade = "Hasan Sahih",
            hadithNarrator = "Thawban (RA)",
            hadithArabic = "مَنْ قَالَ حِينَ يُمْسِي وَحِينَ يُصْبِحُ: رَضِيتُ بِاللَّهِ رَبًّا وَبِالإِسْلاَمِ دِينًا وَبِمُحَمَّدٍ نَبِيًّا، كَانَ حَقًّا عَلَى اللَّهِ أَنْ يُرْضِيَهُ",
            hadithTranslationEn = "The Messenger of Allah ﷺ said: 'Whoever says in the morning and evening: Raditu Billahi Rabban... it is a right upon Allah to please him on the Day of Resurrection.'",
            hadithTranslationHi = "रसूलुल्लाह ﷺ ने फरमाया: 'जो शख्स सुबह और शाम यह कहे: रदीतु बिल्लाहि रब्बन... तो अल्लाह पर लाज़िम हो जाता है कि वह उसे क़यामत के दिन खुश कर दे।'",
            hadithTranslationUr = "رسول اللہ ﷺ نے فرمایا: 'جس مسلمان بندے نے صبح اور شام تین مرتبہ کہا: رضیت باللہ ربا وبالاسلام دینا وبمحمد نبیا، تو اللہ تعالیٰ پر حق ہے کہ قیامت کے دن اسے راضی کرے۔'",
            hadithUrl = "https://sunnah.com/tirmidhi:3389"
        ),
        AdhkarItem(
            id = "m_bismillah_ladhi",
            order = 11,
            targetCount = 3,
            titleEn = "Bismillahil-ladhi la yadurru (3x)",
            titleHi = "बिस्मिल्लाहिल्लज़ी ला यदुर्रु (3 बार)",
            titleUr = "بسم اللہ الذی لا یضر (3 مرتبہ)",
            titleAr = "بسم الله الذي لا يضر (3 مرات)",
            arabicText = "بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الْأَرْضِ وَلَا فِي السَّمَاءِ، وَهُوَ السَّمِيعُ الْعَلِيمُ",
            transliterationEn = "Bismillahil-ladhi la yadurru ma'as-mihi shay'un fil-ardi wa la fis-sama'i wa Huwas-Sami'ul-'Alim.",
            transliterationHi = "बिस्मिल्लाहिल्लज़ी ला यदुर्रु म'अस्मिही शैउन फ़िल-अर्दि वला फ़िस-समाइ व हुवस-समी'उल-'अलीम।",
            translationEn = "In the name of Allah, with whose name nothing can cause harm on earth or in heaven, and He is the All-Hearing, the All-Knowing.",
            translationHi = "अल्लाह के नाम के साथ, जिसके नाम की बरकत से ज़मीन और आसमान की कोई चीज़ नुकसान नहीं पहुंचा सकती, और वही सब कुछ सुनने वाला, सब कुछ जानने वाला है।",
            translationUr = "اللہ کے نام کے ساتھ، جس کے نام کی برکت سے زمین اور آسمان کی کوئی چیز نقصان نہیں پہنچا سکتی، اور وہی خوب سننے والا، سب کچھ جاننے والا ہے۔",
            translationAr = "In the name of Allah, with whose name nothing can cause harm on earth or in heaven, and He is the All-Hearing, the All-Knowing.",
            referenceEn = "Jami` at-Tirmidhi 3388, Abu Dawud 5088",
            referenceHi = "तिर्मिज़ी 3388, अबी दाऊद 5088",
            referenceUr = "جامع ترمذی 3388، ابی داؤد 5088",
            referenceAr = "الترمذي 3388، أبو داود 5088",
            benefitEn = "Reciting this 3 times in the morning shields from all sudden harm, poisons, illnesses, and disasters until evening.",
            benefitHi = "जो शख्स सुबह 3 बार इसे पढ़े, शाम तक उसे कोई अचानक मुसीबत, ज़हर या बीमारी नुकसान नहीं पहुंचा सकती।",
            benefitUr = "جو شخص صبح کے وقت 3 مرتبہ یہ پڑھے گا، شام تک اسے کوئی ناگہانی آفت، زہر یا بیماری نقصان نہیں پہنچا سکے گی۔",
            hadithBook = "Jami` at-Tirmidhi 3388, Sunan Abi Dawud 5088",
            hadithGrade = "Hasan Sahih",
            hadithNarrator = "Uthman ibn Affan (RA)",
            hadithArabic = "مَا مِنْ عَبْدٍ يَقُولُ فِي صَبَاحِ كُلِّ يَوْمٍ وَمَسَاءِ كُلِّ لَيْلَةٍ: بِسْمِ اللَّهِ الَّذِي لاَ يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الأَرْضِ وَلاَ فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ ثَلاَثَ مَرَّاتٍ إِلاَّ لَمْ يَضُرَّهُ شَيْءٌ",
            hadithTranslationEn = "The Messenger of Allah ﷺ said: 'There is no servant who recites three times every morning and evening: Bismillahil-ladhi la yadurru... except that nothing will harm him.'",
            hadithTranslationHi = "रसूलुल्लाह ﷺ ने फरमाया: 'जो बन्दा हर सुबह और हर शाम 3 बार यह दुआ पढ़े, उसे कोई चीज़ नुकसान नहीं पहुंचा सकती।'",
            hadithTranslationUr = "نبی کریم ﷺ نے فرمایا: 'جس بندے نے ہر دن صبح اور ہر رات شام تین مرتبہ یہ دعا پڑھی، اسے کوئی چیز نقصان نہیں پہنچا سکتی۔'",
            hadithUrl = "https://sunnah.com/tirmidhi:3388"
        ),
        AdhkarItem(
            id = "m_subhanallah_adada",
            order = 12,
            targetCount = 3,
            titleEn = "SubhanAllahi wa bihamdihi, 'adada khalqihi (3x)",
            titleHi = "सुब्हानल्लाहि व बिहम्दिही, अददा ख़लक़िही (3 बार)",
            titleUr = "سبحان اللہ وبحمدہ عدد خلقہ (3 مرتبہ)",
            titleAr = "سبحان الله وبحمده عدد خلقه (3 مرات)",
            arabicText = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ، عَدَدَ خَلْقِهِ، وَرِضَا نَفْسِهِ، وَزِنَةَ عَرْشِهِ، وَمِدَادَ كَلِمَاتِهِ",
            transliterationEn = "SubhanAllahi wa bihamdihi, 'adada khalqihi, wa rida nafsihi, wa zinata 'arshihi, wa midada kalimatih.",
            transliterationHi = "सुब्हानल्लाहि व बिहम्दिही, 'अददा ख़लक़िही, व रिदा नफ़्सिही, व ज़ीनता 'अर्शहि, व मिदादा कलिमातिह।",
            translationEn = "Glory is to Allah and praise is to Him, by the number of His creation, by His pleasure, by the weight of His Throne, and by the ink of His words.",
            translationHi = "अल्लाह पाक है और तमाम तारीफ़ें उसी के लिए हैं — उसकी मख़लूक़ात की तादाद के बराबर, उसकी अपनी रज़ा के मुताबिक, उसके अर्श के वज़न के बराबर, और उसके कलिमात की स्याही के बराबर।",
            translationUr = "اللہ پاک ہے اور تمام تعریفیں اسی کے لیے ہیں — اس کی مخلوق کی تعداد کے برابر، اس کی ذات کی رضا کے مطابق، اس کے عرش کے وزن کے برابر، اور اس کے کلمات کی سیاہی کے برابر۔",
            translationAr = "Glory is to Allah and praise is to Him, by the number of His creation, by His pleasure, by the weight of His Throne, and by the ink of His words.",
            referenceEn = "Sahih Muslim 2726",
            referenceHi = "सहीह मुस्लिम 2726",
            referenceUr = "صحیح مسلم 2726",
            referenceAr = "صحيح مسلم: 2726",
            benefitEn = "The Prophet ﷺ told Juwayriyah (RA) that reciting this 3 times outweighs hours of continuous prayer and dhikr.",
            benefitHi = "नबी करीम ﷺ ने उम्मुल मोमिनीन जुवैरिया (रज़ि.) से फरमाया कि ये 3 बार पढ़े गए कलिमात घंटों के ज़िक्र से वज़नदार हैं।",
            benefitUr = "نبی کریم ﷺ نے حضرت جویریہ (رضی اللہ عنہا) کو بتایا کہ یہ 3 مرتبہ کہے گئے کلمات گھنٹوں کے ذکر سے زیادہ وزنی ہیں۔",
            hadithBook = "Sahih Muslim 2726",
            hadithGrade = "Sahih",
            hadithNarrator = "Juwayriyah bint al-Harith (RA)",
            hadithArabic = "لَقَدْ قُلْتُ بَعْدَكِ أَرْبَعَ كَلِمَاتٍ، ثَلاَثَ مَرَّاتٍ، لَوْ وُزِنَتْ بِمَا قُلْتِ مُنْذُ الْيَوْمِ لَوَزَنَتْهُنَّ: سُبْحَانَ اللهِ وَبِحَمْدِهِ، عَدَدَ خَلْقِهِ...",
            hadithTranslationEn = "The Prophet ﷺ said: 'I recited four phrases three times after leaving you; if these were weighed against all that you have recited since morning, they would outweigh them: SubhanAllahi wa bihamdihi...'",
            hadithTranslationHi = "नबी करीम ﷺ ने फरमाया: 'मैंने तुम्हारे बाद 4 कलिमात 3 बार कहे हैं, अगर उनका वज़न उस तमाम ज़िक्र से किया जाए जो तुमने सुबह से अब तक किया है, तो वे भारी रहेंगे।'",
            hadithTranslationUr = "رسول اللہ ﷺ نے جویریہ رضی اللہ عنہا سے فرمایا: 'میں نے تمہارے بعد چار کلمات تین مرتبہ کہے ہیں، اگر ان کا وزن ان تمام اذکار کے ساتھ کیا جائے جو تم نے کہے ہیں تو ان کا وزن زیادہ ہو گا۔'",
            hadithUrl = "https://sunnah.com/muslim:2726"
        ),
        AdhkarItem(
            id = "m_afini_badani",
            order = 13,
            targetCount = 3,
            titleEn = "Allahumma 'afini fi badani (3x)",
            titleHi = "अल्लाहुम्मा आफ़िनी फ़ी बदनी (3 बार)",
            titleUr = "اللہم عافنی فی بدنی (3 مرتبہ)",
            titleAr = "اللهم عافني في بدني (3 مرات)",
            arabicText = "اللَّهُمَّ عَافِنِي فِي بَدَنِي، اللَّهُمَّ عَافِنِي فِي سَمْعِي، اللَّهُمَّ عَافِنِي فِي بَصَرِي، لَا إِلَٰهَ إِلَّا أَنْتَ",
            transliterationEn = "Allahumma 'afini fi badani, Allahumma 'afini fi sam'i, Allahumma 'afini fi basari, la ilaha illa Anta.",
            transliterationHi = "अल्लाहुम्मा 'आफ़िनी फ़ी बदनी, अल्लाहुम्मा 'आफ़िनी फ़ी सम'ई, अल्लाहुम्मा 'आफ़िनी फ़ी बसरी, ला इलाहा इल्ला अंत।",
            translationEn = "O Allah, grant well-being to my body. O Allah, grant well-being to my hearing. O Allah, grant well-being to my sight. There is no deity except You.",
            translationHi = "ऐ अल्लाह! मेरे जिस्म में आफ़ियत (सेहत-ओ-सलामती) अता फरमा। ऐ अल्लाह! मेरे कानों (सुनने की ताक़त) में आफ़ियत दे। ऐ अल्लाह! मेरी आंखों (देखने की ताक़त) में आफ़ियत दे। तेरे सिवा कोई सच्चा माबूद नहीं।",
            translationUr = "اے اللہ! میرے بدن میں عافیت (صحت و سلامتی) عطا فرما۔ اے اللہ! میرے کانوں (سننے کی طاقت) میں عافیت عطا فرما۔ اے اللہ! میری آنکھوں (دیکھنے کی قوت) میں عافیت عطا فرما۔ تیرے سوا کوئی معبود نہیں۔",
            translationAr = "O Allah, grant well-being to my body. O Allah, grant well-being to my hearing. O Allah, grant well-being to my sight. There is no deity except You.",
            referenceEn = "Sunan Abi Dawud 5090",
            referenceHi = "सुनन अबी दाऊद 5090",
            referenceUr = "سنن ابی داؤد 5090",
            referenceAr = "سنن أبي داود: 5090",
            benefitEn = "Preserves physical health, hearing, eyesight, and physical faculties in obedience to Allah.",
            benefitHi = "जिस्मानी तंदुरुस्ती, कानों और आंखों की रोशनी व हिफ़ाज़त के लिए मुजर्रब सुन्नत दुआ।",
            benefitUr = "جسمانی تندرستی، کانوں اور آنکھوں کی قوت و حفاظت کی عظیم نبوی دعا۔",
            hadithBook = "Sunan Abi Dawud 5090",
            hadithGrade = "Hasan (Al-Albani)",
            hadithNarrator = "Abu Bakrah (RA)",
            hadithArabic = "إِنِّي سَمِعْتُ رَسُولَ اللَّهِ صلى الله عليه وسلم يَدْعُو بِهِنَّ فَأَنَا أُحِبُّ أَنْ أَسْتَنَّ بِسُنَّتِهِ",
            hadithTranslationEn = "Abu Bakrah said to his son: 'I heard the Messenger of Allah ﷺ supplicating with these words three times in the morning and three times in the evening, and I love to follow his Sunnah.'",
            hadithTranslationHi = "हज़रत अबू बकरा (रज़ि.) ने फरमाया: 'मैंने रसूलुल्लाह ﷺ को सुबह और शाम 3 बार इन कलिमात के साथ दुआ करते सुना है, इसलिए मैं आपकी सुन्नत पर अमल करना पसंद करता हूं।'",
            hadithTranslationUr = "رسول اللہ ﷺ صبح اور شام تین مرتبہ بدن، کان اور آنکھوں کی عافیت کی یہ دعا مانگا کرتے تھے۔",
            hadithUrl = "https://sunnah.com/abudawud:5090"
        ),
        AdhkarItem(
            id = "m_kufri_faqr",
            order = 14,
            targetCount = 3,
            titleEn = "Allahumma inni a'udhu bika minal-kufri wal-faqr (3x)",
            titleHi = "कुफ्र, फ़क़्र और अज़ाब-ए-क़ब्र से पनाह (3 बार)",
            titleUr = "کفر، فقر اور عذاب قبر سے پناہ (3 مرتبہ)",
            titleAr = "الاستعاذة من الكفر والفقر (3 مرات)",
            arabicText = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْكُفْرِ وَالْفَقْرِ، اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنْ عَذَابِ الْقَبْرِ، لَا إِلَٰهَ إِلَّا أَنْتَ",
            transliterationEn = "Allahumma inni a'udhu bika minal-kufri wal-faqr, Allahumma inni a'udhu bika min 'adhabil-qabr, la ilaha illa Anta.",
            transliterationHi = "अल्लाहुम्मा इन्नी अऊज़ु बिका मिनल-कुफ़्रि वल-फ़क़्र, अल्लाहुम्मा इन्नी अऊज़ु बिका मिन 'अज़ाबिल-क़ब्र, ला इलाहा इल्ला अंत।",
            translationEn = "O Allah, I seek refuge in You from disbelief and poverty. O Allah, I seek refuge in You from the punishment of the grave. There is no deity except You.",
            translationHi = "ऐ अल्लाह! मैं कुफ्र और फ़क़्र (मुफ़लिसी व लाचारी) से तेरी पनाह मांगता हूं। ऐ अल्लाह! मैं क़ब्र के अज़ाब से तेरी पनाह मांगता हूं। तेरे सिवा कोई सच्चा माबूद नहीं।",
            translationUr = "اے اللہ! میں کفر اور فقر (تنگدستی و محتاجی) سے تیری پناہ مانگتا ہوں۔ اے اللہ! میں عذابِ قبر سے تیری پناہ مانگتا ہوں۔ تیرے سوا کوئی معبود نہیں۔",
            translationAr = "O Allah, I seek refuge in You from disbelief and poverty. O Allah, I seek refuge in You from the punishment of the grave. There is no deity except You.",
            referenceEn = "Sunan Abi Dawud 5090",
            referenceHi = "सुनन अबी दाऊद 5090",
            referenceUr = "سنن ابی داؤد 5090",
            referenceAr = "سنن أبي داود: 5090",
            benefitEn = "Guards faith against spiritual deviation, financial bankruptcy/poverty, and the trials of the grave.",
            benefitHi = "ईमान की हिफ़ाज़त, तंगदस्ती से नजात और क़ब्र के अज़ाब से पनाह का मुकम्मल ज़रिया।",
            benefitUr = "ایمان کی حفاظت، مفلسی سے نجات اور عذاب قبر سے پناہ کی بہترین نبوی دعا۔",
            hadithBook = "Sunan Abi Dawud 5090",
            hadithGrade = "Hasan",
            hadithNarrator = "Abu Bakrah (RA)",
            hadithArabic = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْكُفْرِ وَالْفَقْرِ، اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنْ عَذَابِ الْقَبْرِ...",
            hadithTranslationEn = "Abu Bakrah reported that the Prophet ﷺ regularly recited this supplication three times in the morning and evening.",
            hadithTranslationHi = "अबू बकरा (रज़ि.) फरमाते हैं कि रसूलुल्लाह ﷺ सुबह और शाम 3 बार यह दुआ पढ़ा करते थे।",
            hadithTranslationUr = "نبی کریم ﷺ کفر، محتاجی اور عذابِ قبر سے پناہ مانگنے کے لیے یہ مسنون دعا پڑھا کرتے تھے۔",
            hadithUrl = "https://sunnah.com/abudawud:5090"
        ),
        AdhkarItem(
            id = "m_hasbiyallah",
            order = 15,
            targetCount = 7,
            titleEn = "HasbiyAllahu la ilaha illa Huwa (7x)",
            titleHi = "हसबियल्लाहु ला इलाहा इल्ला हुव (7 बार)",
            titleUr = "حسبی اللہ لا الہ الا ہو (7 مرتبہ)",
            titleAr = "حسبي الله لا إله إلا هو (7 مرات)",
            arabicText = "حَسْبِيَ اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ، عَلَيْهِ تَوَكَّلْتُ، وَهُوَ رَبُّ الْعَرْشِ الْعَظِيمِ",
            transliterationEn = "HasbiyAllahu la ilaha illa Huwa, 'alayhi tawakkaltu, wa Huwa Rabbul-'Arshil-'Azim.",
            transliterationHi = "हसबियल्लाहु ला इलाहा इल्ला हुवा, 'अलैहि तवक्कल्तु, व हुवा रब्बुल-'अर्शिल-'अज़ीम।",
            translationEn = "Allah is sufficient for me; there is no deity except Him. Upon Him I have relied, and He is the Lord of the Great Throne.",
            translationHi = "मुझे अल्लाह ही काफी है, उसके सिवा कोई सच्चा माबूद नहीं, उसी पर मैंने भरोसा किया, और वही अज़ीम अर्श का रब है।",
            translationUr = "مجھے اللہ ہی کافی ہے، اس کے سوا کوئی معبود نہیں، اسی پر میں نے بھروسہ کیا، اور وہی عرشِ عظیم کا مالک ہے۔",
            translationAr = "Allah is sufficient for me; there is no deity except Him. Upon Him I have relied, and He is the Lord of the Great Throne.",
            referenceEn = "Sunan Abi Dawud 5081, Qur'an 9:129",
            referenceHi = "सुनन अबी दाऊद 5081, सूरह अत-तौबा (9:129)",
            referenceUr = "سنن ابی داؤد 5081، سورۃ التوبہ (9:129)",
            referenceAr = "سنن أبي داود 5081، سورة التوبة: 129",
            benefitEn = "Whoever recites this 7 times in the morning and evening, Allah will suffice him for all that concerns him of this world and the Hereafter.",
            benefitHi = "जो शख्स सुबह और शाम 7 बार यह कहेगा, अल्लाह तआला उसकी दुनिया और आख़िरत की तमाम फिक्रों और परेशानियों के लिए काफी हो जाएगा।",
            benefitUr = "جو شخص صبح اور شام 7 مرتبہ یہ کہے گا، اللہ تعالیٰ اس کی دنیا اور آخرت کی تمام پریشانیوں اور فکروں کے لیے کافی ہو جائے گا۔",
            hadithBook = "Sunan Abi Dawud 5081",
            hadithGrade = "Sahih Mawquf",
            hadithNarrator = "Abu ad-Darda (RA)",
            hadithArabic = "مَنْ قَالَ إِذَا أَصْبَحَ وَإِذَا أَمْسَى: حَسْبِيَ اللَّهُ لاَ إِلَهَ إِلاَّ هُوَ عَلَيْهِ تَوَكَّلْتُ وَهُوَ رَبُّ الْعَرْشِ الْعَظِيمِ سَبْعَ مَرَّاتٍ، كَفَاهُ اللَّهُ مَا أَهَمَّهُ مِنْ أَمْرِ الدُّنْيَا وَالآخِرَةِ",
            hadithTranslationEn = "Whoever says seven times morning and evening: 'HasbiyAllahu la ilaha illa Huwa...', Allah will suffice him against whatever grieves him in worldly matters and the Hereafter.",
            hadithTranslationHi = "जो शख्स सुबह और शाम 7 मर्तबा यह पढ़ेगा, अल्लाह तआला उसकी दुनिया और आख़िरत के तमाम गमों और चिंताओं के लिए काफी हो जाएगा।",
            hadithTranslationUr = "جو شخص صبح اور شام سات مرتبہ یہ کلمات کہے گا، اللہ تعالیٰ اس کے دنیا اور آخرت کے تمام غموں اور پریشانیوں کے لیے کافی ہو جائے گا۔",
            hadithUrl = "https://sunnah.com/abudawud:5081"
        ),
        AdhkarItem(
            id = "m_fatiras_samawat",
            order = 16,
            targetCount = 1,
            titleEn = "Allahumma Fatiras-Samawati wal-Ard",
            titleHi = "अल्लाहुम्मा फ़ातिरस-समावाति वल-अर्द",
            titleUr = "اللہم فاطر السماوات والارض",
            titleAr = "اللهم فاطر السماوات والأرض",
            arabicText = "اللَّهُمَّ فَاطِرَ السَّمَاوَاتِ وَالْأَرْضِ، عَالِمَ الْغَيْبِ وَالشَّهَادَةِ، لَا إِلَٰهَ إِلَّا أَنْتَ، رَبَّ كُلِّ شَيْءٍ وَمَلِيكَهُ، أَعُوذُ بِكَ مِنْ شَرِّ نَفْسِي، وَمِنْ شَرِّ الشَّيْطَانِ وَشِرْكِهِ، وَأَنْ أَقْتَرِفَ عَلَىٰ نَفْسِي سُوءًا أَوْ أَجُرَّهُ إِلَىٰ مُسْلِمٍ",
            transliterationEn = "Allahumma Fatiras-samawati wal-ardi, 'Alimal-ghaybi wash-shahadah, la ilaha illa Anta, Rabba kulli shay'in wa Malikahu, a'udhu bika min sharri nafsi, wa min sharrish-shaytani wa shirkihi, wa an aqtarifa 'ala nafsi su'an aw ajurrahu ila Muslim.",
            transliterationHi = "अल्लाहुम्मा फ़ातिरस-समावाति वल-अर्द, 'आलिमल-ग़ैबि wash-शहादह, ला इलाहा इल्ला अंत, रब्बा कुल्लि शैइन व मलीकह, अऊज़ु बिका मिन शर्री नफ़्सी, व मिन शर्रिश-शैतानि व शिर्किही, व अन अक़तरिफ़ा 'अला नफ़्सी सूअन औ अजुर्रहू इला मुस्लिम।",
            translationEn = "O Allah, Creator of the heavens and the earth, Knower of the unseen and the seen, there is no deity except You, Lord and Sovereign of all things. I seek refuge in You from the evil of my own self, from the evil of Satan and his polytheism/traps, and from committing any wrong against myself or bringing harm upon any Muslim.",
            translationHi = "ऐ अल्लाह! आसमानों और ज़मीन के पैदा करने वाले, छुपे और खुले को जानने वाले, तेरे सिवा कोई सच्चा माबूद नहीं, तू ही हर चीज़ का रब और उसका मालिक है। मैं तेरी पनाह मांगता हूं अपने नफ़्स की बुराई से, और शैतान की बुराई व उसके शिर्क (जाल) से, और इस बात से कि मैं अपनी जान पर कोई बुराई करूं या किसी मुसलमान की तरफ बुराई खींच लाऊं।",
            translationUr = "اے اللہ! آسمانوں اور زمین کے پیدا فرمانے والے، غیب اور حاضر کے جاننے والے، تیرے سوا کوئی معبود نہیں، تو ہی ہر چیز کا رب اور اس کا مالک ہے۔ میں تیری پناہ مانگتا ہوں اپنے نفس کے شر سے، اور شیطان کے شر اور اس کے شرک (جال) سے، اور اس بات سے کہ میں اپنی جان پر کوئی برائی کروں یا کسی مسلمان کو نقصان پہنچاؤں۔",
            translationAr = "O Allah, Creator of the heavens and the earth, Knower of the unseen and the seen, there is no deity except You, Lord and Sovereign of all things. I seek refuge in You from the evil of my own self, from the evil of Satan and his polytheism/traps, and from committing any wrong against myself or bringing harm upon any Muslim.",
            referenceEn = "Jami` at-Tirmidhi 3529, Abu Dawud 5067",
            referenceHi = "तिर्मिज़ी 3529, अबी दाऊद 5067",
            referenceUr = "جامع ترمذی 3529، ابی داؤد 5067",
            referenceAr = "الترمذي 3529، أبو داود 5067",
            benefitEn = "Taught specifically by the Prophet ﷺ to Abu Bakr as-Siddiq (RA) for morning, evening, and bedtime protection.",
            benefitHi = "रसूलुल्लाह ﷺ ने हज़रत अबू बकर सिद्दीक़ (रज़ि.) को खास तौर पर सुबह, शाम और सोते वक़्त पढ़ने के लिए यह दुआ सिखाई।",
            benefitUr = "رسول اللہ ﷺ نے حضرت ابو بکر صدیق (رضی اللہ عنہ) کو خاص طور پر صبح، شام اور سوتے وقت پڑھنے کے لیے یہ جامع دعا سکھائی۔",
            hadithBook = "Jami` at-Tirmidhi 3529, Sunan Abi Dawud 5067",
            hadithGrade = "Hasan Sahih",
            hadithNarrator = "Abu Bakr as-Siddiq (RA)",
            hadithArabic = "قُلْتُ: يَا رَسُولَ اللَّهِ، مُرْنِي بِكَلِمَاتٍ أَقُولُهُنَّ إِذَا أَصْبَحْتُ وَإِذَا أَمْسَيْتُ، قَالَ: قُلِ: اللَّهُمَّ فَاطِرَ السَّمَاوَاتِ وَالأَرْضِ...",
            hadithTranslationEn = "Abu Bakr said: 'O Messenger of Allah, command me with words to say in the morning and evening.' The Prophet ﷺ taught him this supplication.",
            hadithTranslationHi = "हज़रत अबू बकर (रज़ि.) ने अर्ज़ किया: 'या रसूलुल्लाह! मुझे कोई ऐसी दुआ सिखाएं जो मैं सुबह और शाम पढ़ूं।' नबी ﷺ ने यह दुआ सिखाई।",
            hadithTranslationUr = "رسول اللہ ﷺ نے حضرت ابوبکر صدیق رضی اللہ عنہ کو صبح و شام اور سوتے وقت یہ دعا پڑھنے کی تلقین فرمائی۔",
            hadithUrl = "https://sunnah.com/tirmidhi:3529"
        ),
        AdhkarItem(
            id = "m_ya_hayyu",
            order = 17,
            targetCount = 1,
            titleEn = "Ya Hayyu Ya Qayyum",
            titleHi = "या हय्यु या क़य्यूम",
            titleUr = "یا حی یا قیوم",
            titleAr = "يا حي يا قيوم",
            arabicText = "يَا حَيُّ يَا قَيُّومُ، بِرَحْمَتِكَ أَسْتَغِيثُ، أَصْلِحْ لِي شَأْنِي كُلَّهُ، وَلَا تَكِلْنِي إِلَىٰ نَفْسِي طَرْفَةَ عَيْنٍ",
            transliterationEn = "Ya Hayyu Ya Qayyum, bi-rahmatika astaghith, aslih li sha'ni kullahu, wa la takilni ila nafsi tarfata 'ayn.",
            transliterationHi = "या हय्यु या क़य्यूम, बि-रह्मतिका अस्तग़ीस, अस्लिह ली शअनी कुल्लहू, वला तकिल्नी इला नफ़्सी तर्फ़ता 'अैन।",
            translationEn = "O Ever-Living, O Self-Sustaining, by Your mercy I seek assistance. Rectify for me all of my affairs, and do not leave me in charge of myself even for the blink of an eye.",
            translationHi = "ऐ हमेशा ज़िंदा रहने वाले! ऐ तमाम जहान को क़ायम रखने वाले! तेरी रहमत के वसीले से मैं मदद मांगता हूं, मेरे तमाम मामलात को संवार दे, और मुझे पलक झपकने जितनी देर के लिए भी मेरे नफ़्स के हवाले न कर।",
            translationUr = "اے ہمیشہ زندہ رہنے والے! اے قائم بالذات سب کو سنبھالنے والے! تیری رحمت کے وسیلے سے میں مدد طلب کرتا ہوں، میرے تمام کام سنوار دے، اور مجھے آنکھ جھپکنے کی دیر بھی میرے نفس کے حوالے نہ فرما۔",
            translationAr = "O Ever-Living, O Self-Sustaining, by Your mercy I seek assistance. Rectify for me all of my affairs, and do not leave me in charge of myself even for the blink of an eye.",
            referenceEn = "Sunan an-Nasa'i 10330, Al-Hakim 1/545, Tirmidhi 3524",
            referenceHi = "सुनन अन-नसाई 10330, अल-हाकिम 1/545",
            referenceUr = "سنن نسائی 10330، الحاکم 1/545",
            referenceAr = "النسائي في الكبرى 10330، الحاكم 1/545",
            benefitEn = "The Prophet ﷺ advised his beloved daughter Fatimah (RA) to never leave this supplication every morning and evening.",
            benefitHi = "रसूलुल्लाह ﷺ ने अपनी प्यारी बेटी हज़रत फ़ातिमा (रज़ि.) को नसीहत फरमाई थी कि सुबह और शाम इस दुआ को कभी न छोड़ें।",
            benefitUr = "رسول اللہ ﷺ نے اپنی لختِ جگر حضرت فاطمہ (رضی اللہ عنہا) کو نصیحت فرمائی کہ صبح و شام اس دعا کو کبھی نہ چھوڑیں۔",
            hadithBook = "Sunan an-Nasa'i (Al-Kubra 10330), Al-Mustadrak 'ala al-Sahihayn 1/545",
            hadithGrade = "Hasan (Al-Albani)",
            hadithNarrator = "Anas ibn Malik (RA)",
            hadithArabic = "قَالَ النَّبِيُّ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ لِفَاطِمَةَ: مَا يَمْنَعُكِ أَنْ تَسْمَعِي مَا أُوصِيكِ بِهِ، أَنْ تَقُولِي إِذَا أَصْبَحْتِ وَإِذَا أَمْسَيْتِ: يَا حَيُّ يَا قَيُّومُ بِرَحْمَتِكَ أَسْتَغِيثُ...",
            hadithTranslationEn = "The Prophet ﷺ said to Fatimah (RA): 'What prevents you from listening to what I advise you? That you say in the morning and evening: Ya Hayyu Ya Qayyum...'",
            hadithTranslationHi = "नबी करीम ﷺ ने हज़रत फ़ातिमा (रज़ि.) से फरमाया: 'तुम्हें यह वसीयत सुनने से क्या चीज़ रोकती है कि तुम सुबह और शाम यह कहा करो: या हय्यु या क़य्यूम...'",
            hadithTranslationUr = "رسول اللہ ﷺ نے سیدہ فاطمہ رضی اللہ عنہا کو وصیت فرمائی کہ صبح و شام یہ دعا پڑھا کرو: یا حی یا قیوم برحمتک استغیث...",
            hadithUrl = "https://sunnah.com/tirmidhi:3524"
        ),
        AdhkarItem(
            id = "m_la_ilaha_illa_allah",
            order = 18,
            targetCount = 100,
            titleEn = "La ilaha illAllahu wahdahu la sharika lah (100x / Day)",
            titleHi = "ला इलाहा इल्लल्लाहु वहदहू ला शरीका लह (100 बार / दिन)",
            titleUr = "لا الہ الا اللہ وحدہ لا شریک لہ (100 مرتبہ / روزانہ)",
            titleAr = "لا إله إلا الله وحده لا شريك له (100 مرة)",
            arabicText = "لَا إِلَٰهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ، وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ",
            transliterationEn = "La ilaha illAllahu wahdahu la sharika lah, lahul-mulku wa lahul-hamd, wa Huwa 'ala kulli shay'in qadir.",
            transliterationHi = "ला इलाहा इल्लल्लाहु वहदहू ला शरीका लह, लहुल-मुल्कु व लहुल-हम्द, व हुवा 'अला कुल्लि शैइन क़दीर।",
            translationEn = "There is no deity except Allah alone, without partner; to Him belongs all sovereignty and to Him belongs all praise, and He is over all things competent.",
            translationHi = "अल्लाह के सिवा कोई सच्चा माबूद नहीं, वह अकेला है, उसका कोई शरीक नहीं, उसी की बादशाही है और उसी के लिए तमाम तारीफ़ है, और वह हर चीज़ पर पूरी तरह क़ादिर है।",
            translationUr = "اللہ کے سوا کوئی معبود नहीं, وہ اکیلا ہے، اس کا کوئی شریک نہیں، اسی کی بادشاہی ہے اور اسی کے لیے تمام تعریف ہے، اور وہ ہر چیز پر قادر ہے۔",
            translationAr = "There is no deity except Allah alone, without partner; to Him belongs all sovereignty and to Him belongs all praise, and He is over all things competent.",
            referenceEn = "Sahih al-Bukhari 3293, Sahih Muslim 2691",
            referenceHi = "सहीह बुख़ारी 3293, सहीह मुस्लिम 2691",
            referenceUr = "صحیح بخاری 3293، صحیح مسلم 2691، مسلم 2691",
            referenceAr = "سنن أبي داود 5077، البخاري 3293، مسلم 2691",
            benefitEn = "Equal to freeing a slave from the children of Ismail, 10 good deeds recorded, 10 sins erased, 10 ranks raised, and protected from Satan until evening.",
            benefitHi = "इस कलिमे को सुबह पढ़ने से हज़रत इस्माईल (अलै.) की औलाद से गुलाम आज़ाद करने का सवाब, 10 नेकियों का इंदिराज, 10 गुनाहों की माफ़ी, 10 दर्जे की बुलंदी और शाम तक शैतान से हिफ़ाज़त मिलती है।",
            benefitUr = "صبح کے وقت اسے پڑھنا اولادِ اسماعیل سے ایک غلام آزاد کرنے کے برابر ہے، 10 نیکیاں لکھی جاتی ہیں، 10 گناہ معاف اور 10 درجات بلند ہوتے ہیں، اور شام تک شیطان سے حفاظت رہتی ہے۔",
            hadithBook = "Sunan Abi Dawud 5077, Sahih al-Bukhari 3293, Sahih Muslim 2691",
            hadithGrade = "Sahih",
            hadithNarrator = "Abu Ayyub al-Ansari (RA)",
            hadithArabic = "مَنْ قَالَهَا إِذَا أَصْبَحَ كَانَ لَهُ عَدْلَ رَقَبَةٍ مِنْ وَلَدِ إِسْمَاعِيلَ، وَكُتِبَ لَهُ عَشْرُ حَسَنَاتٍ، وَحُطَّ عَنْهُ عَشْرُ سَيِّئَاتٍ، وَرُفِعَ لَهُ عَشْرُ دَرَجَاتٍ، وَكَانَ فِي حِرْزٍ مِنَ الشَّيْطَانِ حَتَّى يُمْسِيَ",
            hadithTranslationEn = "The Messenger of Allah ﷺ said: 'Whoever says it when he reaches the morning, it will be equivalent to freeing a slave from the descendants of Ismail, ten good deeds will be written for him, ten sins will be remitted from him, he will be raised ten degrees, and he will be in a sanctuary against Satan until evening.'",
            hadithTranslationHi = "रसूलुल्लाह ﷺ ने फरमाया: 'जिसने सुबह के वक्त यह कहा, उसे औलादे इस्माईल में से एक गुलाम आज़ाद करने का सवाब मिलेगा, 10 नेकियां लिखी जाएंगी, 10 गुनाह मिटाए जाएंगे, 10 दर्जे बुलंद होंगे, और शाम तक वह शैतान से महफूज़ रहेगा।'",
            hadithTranslationUr = "رسول اللہ ﷺ نے فرمایا: 'جس نے صبح کے وقت یہ دعا سو مرتبہ پڑھی، اسے دس غلام آزاد کرنے کا ثواب ملے گا، سو نیکیاں لکھی جائیں گی، سو برائیاں مٹا دی جائیں گی اور دن بھر شیطان سے حفاظت رہے گی۔'",
            hadithUrl = "https://sunnah.com/abudawud:5077"
        ),
    )

    private val eveningList = listOf(
        AdhkarItem(
            id = "e_ayatulkursi",
            order = 1,
            targetCount = 1,
            titleEn = "Ayat al-Kursi",
            titleHi = "आयतुल कुर्सी",
            titleUr = "آیۃ الکرسی",
            titleAr = "آية الكرسي",
            arabicText = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَنْ ذَا الَّذِي يَشْفَعُ عِنْدَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ",
            transliterationEn = "Allahu la ilaha illa Huwal-Hayyul-Qayyum. La ta'khudhuhu sinatuw-wa la nawm. Lahu ma fis-samawati wa ma fil-ard. Man dhal-ladhi yashfa'u 'indahu illa bi-idhnih. Ya'lamu ma bayna aydihim wa ma khalfahum, wa la yuhituna bi-shay'im-min 'ilmihi illa bima sha'. Wasi'a kursiyyuhus-samawati wal-ard, wa la ya'uduhu hifzuhuma, wa Huwal-'Aliyyul-'Azim.",
            transliterationHi = "अल्लाहु ला इलाहा इल्ला हुवल हय्युल क़य्यूम, ला तअख़ुज़ुहू सिनतुंव-वला नौम, लहू मा फ़िस-समावाति वमा फ़िल अर्द, मन् ज़ल्लज़ी यश्फ़उ इन्दहू इल्ला बि-इज़्निह, यअलमु मा बैना ऐदीहिम वमा ख़ल्फ़हुम, वला युहीतूना बिशयइम-मिन इल्मिही इल्ला बिमा शाअ, वसिअ कुरसिय्युहुस-समावाति वल-अर्द, वला यऊदुहू हिफ़्ज़ुहुमा, वहुवल-अलिय्युल-अज़ीम।",
            translationEn = "Allah! There is no deity except Him, the Ever-Living, the Sustainer of all existence. Neither drowsiness overtakes Him nor sleep. To Him belongs whatever is in the heavens and whatever is on the earth. Who is it that can intercede with Him except by His permission? He knows what is before them and what will be after them, and they encompass not a thing of His knowledge except for what He wills. His Kursi extends over the heavens and the earth, and their preservation tires Him not. And He is the Most High, the Most Great.",
            translationHi = "अल्लाह! उसके सिवा कोई सच्चा माबूद नहीं, वह हमेशा ज़िंदा रहने वाला और सब को क़ायम रखने वाला है। उसे न तो ऊंघ आती है और न नींद। जो कुछ आसमानों में है और जो कुछ ज़मीन में है, सब उसी का है। कौन है जो उसकी इजाज़त के बिना उसके पास सिफारिश कर सके? वह जानता है जो कुछ उनके आगे है और जो कुछ उनके पीछे है, और वे उसके इल्म में से किसी चीज़ का अहाता नहीं कर सकते सिवाय जितना वह चाहे। उसकी कुर्सी आसमानों और ज़मीन को घेरे हुए है, और इन दोनों की हिफ़ाज़त उसे थकाती नहीं। और वह सबसे बुलंद, सबसे बड़ा है।",
            translationUr = "اللہ! اس کے سوا کوئی معبود نہیں، وہ زندہ ہے، سب کا تھامنے والا ہے۔ نہ اسے اونگھ آتی ہے نہ نیند۔ جو کچھ آسمانوں میں ہے اور جو کچھ زمین میں ہے اسی کا ہے۔ کون ہے جو اس کی اجازت کے بغیر اس کے پاس سفارش کرے؟ وہ جانتا ہے جو کچھ ان کے آگے ہے اور جو کچھ ان کے پیچھے ہے، اور وہ اس کے علم میں سے کسی چیز کا احاطہ نہیں کر سکتے مگر جتنا وہ چاہے۔ اس کی کرسی نے آسمانوں اور زمین کو گھیرا ہوا ہے، اور ان کی حفاظت اسے نہیں تھکاتی۔ اور وہ بلند مرتبہ، بڑی عظمت والا ہے۔",
            translationAr = "Allah! There is no deity except Him, the Ever-Living, the Sustainer of all existence. Neither drowsiness overtakes Him nor sleep. To Him belongs whatever is in the heavens and whatever is on the earth. Who is it that can intercede with Him except by His permission? He knows what is before them and what will be after them, and they encompass not a thing of His knowledge except for what He wills. His Kursi extends over the heavens and the earth, and their preservation tires Him not. And He is the Most High, the Most Great.",
            referenceEn = "Sahih al-Bukhari 2311, Qur'an 2:255",
            referenceHi = "सहीह बुख़ारी 2311, सूरह अल-बक़रा (2:255)",
            referenceUr = "صحیح بخاری 2311، سورۃ البقرۃ (2:255)",
            referenceAr = "صحيح البخاري: 2311",
            benefitEn = "Reciting Ayat al-Kursi in the evening guarantees Allah's guardian angel protection throughout the night until morning.",
            benefitHi = "शाम को आयतुल कुर्सी पढ़ने वाले पर अल्लाह की तरफ से एक मुहाफ़िज़ (फ़रिश्ता) मुक़र्रर रहता है और रात भर शैतान क़रीब नहीं आता।",
            benefitUr = "شام کے وقت آیۃ الکرسی پڑھنے والے پر اللہ کی طرف سے نگہبان مقرر رہتا ہے اور رات بھر شیطان قریب نہیں آتا۔",
            hadithBook = "Sahih al-Bukhari 2311",
            hadithGrade = "Sahih",
            hadithNarrator = "Abu Hurairah (RA)",
            hadithArabic = "إِذَا أَوَيْتَ إِلَى فِرَاشِكَ فَاقْرَأْ آيَةَ الْكُرْسِيِّ... لَنْ يَزَالَ عَلَيْكَ مِنَ اللَّهِ حَافِظٌ، وَلاَ يَقْرَبُكَ شَيْطَانٌ حَتَّى تُصْبِحَ",
            hadithTranslationEn = "The Prophet ﷺ confirmed: 'When you recite Ayat al-Kursi, Allah will appoint a guard for you who will stay with you and no devil will come near you until morning.'",
            hadithTranslationHi = "रसूलुल्लाह ﷺ ने फरमाया: 'जब तुम आयतुल कुर्सी पढ़ोगे तो अल्लाह की तरफ से तुम पर एक मुहाफ़िज़ मुक़र्रर रहेगा और सुबह तक शैतान तुम्हारे पास नहीं फटकेगा।'",
            hadithTranslationUr = "",
            hadithUrl = "https://sunnah.com/bukhari:2311"
        ),
        AdhkarItem(
            id = "e_baqarah_last2",
            order = 2,
            targetCount = 1,
            titleEn = "Surah Al-Baqarah (Last 2 Ayahs: 285-286)",
            titleHi = "सूरह अल-बक़रा (आखिरी 2 आयतें)",
            titleUr = "سورۃ البقرۃ کی آخری دو آیات",
            titleAr = "خواتيم سورة البقرة (285-286)",
            arabicText = "آمَنَ الرَّسُولُ بِمَا أُنْزِلَ إِلَيْهِ مِنْ رَبِّهِ وَالْمُؤْمِنُونَ ۚ كُلٌّ آمَنَ بِاللَّهِ وَمَلَائِكَتِهِ وَكُتُبِهِ وَرُسُلِهِ لَا نُفَرِّقُ بَيْنَ أَحَدٍ مِنْ رُسُلِهِ ۚ وَقَالُوا سَمِعْنَا وَأَطَعْنَا ۖ غُفْرَانَكَ رَبَّنَا وَإِلَيْكَ الْمَصِيرُ ۝ لَا يُكَلِّفُ اللَّهُ نَفْسًا إِلَّا وُسْعَهَا ۚ لَهَا مَا كَسَبَتْ وَعَلَيْهَا مَا اكْتَسَبَتْ ۗ رَبَّنَا لَا تُؤَاخِذْنَا إِنْ نَسِينَا أَوْ أَخْطَأْنَا ۚ رَبَّنَا وَلَا تَحْمِلْ عَلَيْنَا إِصْرًا كَمَا حَمَلْتَهُ عَلَى الَّذِينَ مِنْ قَبْلِنَا ۚ رَبَّنَا وَلَا تُحَمِّلْنَا مَا لَا طَاقَةَ لَنَا بِهِ ۖ وَاعْفُ عَنَّا وَاغْفِرْ لَنَا وَارْحَمْنَا ۚ أَنْتَ مَوْلَانَا فَانْصُرْنَا عَلَى الْقَوْمِ الْكَافِرِينَ",
            transliterationEn = "Amanar-Rasulu bima unzila ilayhi mir-Rabbihi wal-mu'minun. Kullun amana billahi wa mala'ikatihi wa kutubihi wa rusulih, la nufarriqu bayna ahadim-mir-rusulih. Wa qalu sami'na wa ata'na ghufranaka Rabbana wa ilaykal-masir. La yukallifullahu nafsan illa wus'aha, laha ma kasabat wa 'alayha maktasabat. Rabbana la tu'akhidhna in-nasina aw akhta'na, Rabbana wa la tahmil 'alayna isran kama hamaltahu 'alal-ladhina min qablina, Rabbana wa la tuhammilna ma la taqata lana bih, wa'fu 'anna waghfir lana warhamna, Anta Mawlana fansurna 'alal-qawmil-kafirin.",
            transliterationHi = "आमनर-रसूलो बिमा उन्ज़िला इलैहि मिर-रब्बिही वल-मोमिनून, कुल्लुन आमना बिल्लाहि व मलाइकतिही व कुतुबिही व रुसुलिह, ला नुफ़र्रिक़ु बैना अहदिम-मिर-रुसुलिह, व क़ालू समिअना व अतअना ग़ुफ़्रानका रब्बना व इलैकेल-मसीर। ला युकल्लिफ़ुल्लाहु नफ़्सन इल्ला वुसअहा, लहा मा कसबत् व अलैहा मक्तसबत्, रब्बना ला तुआख़िज़ना इन-नसीना औ अख़्तअना, रब्बना व ला तह्मिल अलैना इस्रन कमा हमल्तहू अलल्लज़ीना मिन क़ब्लिना, रब्बना व ला तुहम्मिल्ना मा ला ताक़ता लना बिह, वअफ़ु अन्ना वग़्फ़िर लना वर्हम्ना, अंता मौलाना फ़न्सुरना अलल-क़ौमिल-काफ़िरीन।",
            translationEn = "The Messenger has believed in what was revealed to him from his Lord, and [so have] the believers. All of them have believed in Allah and His angels and His books and His messengers, [saying], \"We make no distinction between any of His messengers.\" And they say, \"We hear and we obey. [We seek] Your forgiveness, our Lord, and to You is the [final] destination.\" Allah does not burden a soul beyond that it can bear. It will have [the consequence of] what [good] it has gained, and it will bear [the consequence of] what [evil] it has earned. Our Lord, do not impose blame upon us if we have forgotten or erred. Our Lord, and lay not upon us a burden like that which You laid upon those before us. Our Lord, and burden us not with that which we have no ability to bear. And pardon us; and forgive us; and have mercy upon us. You are our protector, so give us victory over the disbelieving people.",
            translationHi = "रसूल उस चीज़ पर ईमान लाए जो उनके रब की तरफ से उन पर नाज़िल हुई और मोमिन भी। सब अल्लाह, उसके फ़रिश्तों, उसकी किताबों और उसके रसूलों पर ईमान लाए कि हम उसके रसूलों में से किसी के दरमियान फर्क़ नहीं करते, और उन्होंने कहा: हमने सुना और इताअत की, ऐ हमारे रब! हम तेरी मग़फ़िरत चाहते हैं और तेरी ही तरफ लौटना है। अल्लाह किसी जान पर उसकी ताक़त से ज़्यादा बोझ नहीं डालता। उसने जो नेकी कमाई वह उसी के लिए है और जो बुराई कमाई वह भी उसी पर है। ऐ हमारे रब! अगर हम भूल जाएं या ख़ता करें तो हमारी पकड़ न करना। ऐ हमारे रब! हम पर ऐसा भारी बोझ न डाल जैसा तूने हमसे पहले लोगों पर डाला था। ऐ हमारे रब! हम पर ऐसा बोझ न डाल जिसे उठाने की हममें ताक़त न हो। हमें माफ़ फरमा, हमें बख़्श दे और हम पर रहम कर, तू ही हमारा मौला (मददगार) है, पस काफ़िर क़ौम के मुक़ाबले में हमारी मदद फरमा।",
            translationUr = "رسول اس پر ایمان لائے جو ان کے رب کی طرف سے ان پر نازل ہوا اور مومنین بھی۔ سب اللہ، اس کے فرشتوں، اس کی کتابوں اور اس کے رسولوں پر ایمان لائے کہ ہم اس کے رسولوں کے درمیان کوئی فرق نہیں کرتے، اور انہوں نے کہا: ہم نے سنا اور اطاعت کی، اے ہمارے رب! ہم تیری مغفرت کے طلبگار ہیں اور تیری ہی طرف لوٹنا ہے۔ اللہ کسی جان پر اس کی طاقت سے زیادہ بوجھ نہیں ڈالتا۔ اس نے جو نیکی کمائی وہ اسی کے لیے ہے اور جو گناہ کمایا اس کا وبال بھی اسی پر ہے۔ اے ہمارے رب! اگر ہم بھول جائیں یا خطا کر بیٹھیں تو ہماری پکڑ نہ فرما۔ اے ہمارے رب! ہم پر ایسا بھاری بوجھ نہ ڈال جیسا تو نے ہم سے پہلے لوگوں پر ڈالا تھا۔ اے ہمارے رب! ہم پر ایسا بوجھ نہ ڈال جس کے اٹھانے کی ہم میں طاقت نہ ہو۔ ہمیں معاف فرما، ہمیں بخش دے اور ہم پر رحم فرما، تو ہی ہمارا کارساز ہے، پس کافر قوم پر ہماری مدد فرما۔",
            translationAr = "The Messenger has believed in what was revealed to him from his Lord, and [so have] the believers. All of them have believed in Allah and His angels and His books and His messengers, [saying], \"We make no distinction between any of His messengers.\" And they say, \"We hear and we obey. [We seek] Your forgiveness, our Lord, and to You is the [final] destination.\" Allah does not burden a soul beyond that it can bear. It will have [the consequence of] what [good] it has gained, and it will bear [the consequence of] what [evil] it has earned. Our Lord, do not impose blame upon us if we have forgotten or erred. Our Lord, and lay not upon us a burden like that which You laid upon those before us. Our Lord, and burden us not with that which we have no ability to bear. And pardon us; and forgive us; and have mercy upon us. You are our protector, so give us victory over the disbelieving people.",
            referenceEn = "Sahih al-Bukhari 5009, Sahih Muslim 807",
            referenceHi = "सहीह बुख़ारी 5009, सहीह मुस्लिम 807",
            referenceUr = "صحیح بخاری 5009، صحیح مسلم 807",
            referenceAr = "صحيح البخاري: 5009، صحيح مسلم: 807",
            benefitEn = "Whoever recites the last two verses of Surah Al-Baqarah at night, they will suffice him (protect from all evil and substitute for night prayer).",
            benefitHi = "जो शख्स रात को सूरह बक़रा की आख़िरी दो आयतें पढ़ लेगा, वे उसके लिए (हर बुराई से हिफ़ाज़त और क़ियामुल-लैल के बराबर) काफ़ी हो जाएंगी।",
            benefitUr = "جس نے رات کے وقت سورۃ البقرۃ کی آخری دو آیات پڑھیں، وہ اس کے لیے ہر قسم کے شر سے حفاظت کے لیے کافی ہو جائیں گی۔",
            hadithBook = "Sahih al-Bukhari 5009, Sahih Muslim 807",
            hadithGrade = "Sahih",
            hadithNarrator = "Abu Mas'ud al-Badri (RA)",
            hadithArabic = "مَنْ قَرَأَ بِالآيَتَيْنِ مِنْ آخِرِ سُورَةِ الْبَقَرَةِ فِي لَيْلَةٍ كَفَتَاهُ",
            hadithTranslationEn = "The Prophet ﷺ said: 'Whoever recites the last two verses of Surah Al-Baqarah at night, they will be sufficient for him.'",
            hadithTranslationHi = "नबी करीम ﷺ ने फरमाया: 'जो शख्स रात में सूरह अल-बक़रा की आख़िरी दो आयतें पढ़ेगा, वे उसके लिए काफी हो जाएंगी।'",
            hadithTranslationUr = "",
            hadithUrl = "https://sunnah.com/bukhari:5009"
        ),
        AdhkarItem(
            id = "e_ikhlas",
            order = 3,
            targetCount = 3,
            titleEn = "Surah Al-Ikhlas (3x)",
            titleHi = "सूरह अल-इख़्लास (3 बार)",
            titleUr = "سورۃ الاخلاص (3 مرتبہ)",
            titleAr = "سورة الإخلاص (3 مرات)",
            arabicText = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ ۝ قُلْ هُوَ اللَّهُ أَحَدٌ ۝ اللَّهُ الصَّمَدُ ۝ لَمْ يَلِدْ وَلَمْ يُولَدْ ۝ وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ",
            transliterationEn = "Bismillahir-Rahmanir-Rahim. Qul Huwallahu Ahad. Allahus-Samad. Lam yalid wa lam yulad. Wa lam yakul-lahu kufuwan ahad.",
            transliterationHi = "बिस्मिल्लाहिर-रहमानिर-रहीम। क़ुल हुवल्लाहु अहद। अल्लाहुस-समद। लम यलिद वलम यूलद। वलम यकुल-लहू कुफ़ुवन अहद।",
            translationEn = "In the name of Allah, the Entirely Merciful, the Especially Merciful. Say: He is Allah, [who is] One. Allah, the Eternal Refuge. He neither begets nor is born, nor is there to Him any equivalent.",
            translationHi = "अल्लाह के नाम से जो बहुत मेहरबान, निहायत रहम फरमाने वाला है। कह दीजिए: वह अल्लाह एक है। अल्लाह बेनियाज़ है (सब उसके मोहताज हैं)। न उसने किसी को जना और न वह किसी से जना गया। और न कोई उसके बराबर का है।",
            translationUr = "اللہ کے نام سے جو نہایت مہربان، ہمیشہ رحم فرمانے والا ہے۔ فرما دیجئے: وہ اللہ ایک ہے۔ اللہ بے نیاز ہے۔ نہ اس کی کوئی اولاد ہے اور نہ وہ کسی کی اولاد ہے۔ اور نہ کوئی اس کا ہمسر ہے۔",
            translationAr = "In the name of Allah, the Entirely Merciful, the Especially Merciful. Say: He is Allah, [who is] One. Allah, the Eternal Refuge. He neither begets nor is born, nor is there to Him any equivalent.",
            referenceEn = "Sunan Abi Dawud 5082, Tirmidhi 3575",
            referenceHi = "सुनन अबी दाऊद 5082, तिर्मिज़ी 3575",
            referenceUr = "سنن ابی داؤد 5082، جامع ترمذی 3575",
            referenceAr = "سنن أبي داود: 5082",
            benefitEn = "Reciting Al-Ikhlas 3 times in the evening equals reciting the entire Quran in reward and grants complete protection.",
            benefitHi = "शाम को 3 बार सूरह इख़्लास पढ़ना पूरे क़ुरआन के बराबर सवाब और हर बुराई से हिफ़ाज़त देता है।",
            benefitUr = "شام کو 3 مرتبہ سورۃ الاخلاص پڑھنا پورے قرآن کی تلاوت کے برابر ثواب اور ہر شر سے حفاظت دیتا ہے۔",
            hadithBook = "Sunan Abi Dawud 5082, Jami` at-Tirmidhi 3575",
            hadithGrade = "Hasan Sahih",
            hadithNarrator = "Abdullah ibn Khubayb (RA)",
            hadithArabic = "قُلْ هُوَ اللَّهُ أَحَدٌ وَالْمُعَوِّذَتَيْنِ حِينَ تُمْسِي وَحِينَ تُصْبِحُ ثَلَاثَ مَرَّاتٍ تَكْفِيكَ مِنْ كُلِّ شَيْءٍ",
            hadithTranslationEn = "The Prophet ﷺ said: 'Recite Qul Huwallahu Ahad and the Mu'awwidhatayn three times in the evening and in the morning; they will suffice you against everything.'",
            hadithTranslationHi = "रसूलुल्लाह ﷺ ने फरमाया: 'शाम और सुबह 3 बार क़ुल हुवल्लाहु अहद और दोनों मुअव्वज़तैन पढ़ लिया करो, यह तुम्हें हर चीज़ से काफी होंगी।'",
            hadithTranslationUr = "",
            hadithUrl = "https://sunnah.com/abudawud:5082"
        ),
        AdhkarItem(
            id = "e_falaq",
            order = 4,
            targetCount = 3,
            titleEn = "Surah Al-Falaq (3x)",
            titleHi = "सूरह अल-फ़लक़ (3 बार)",
            titleUr = "سورۃ الفلق (3 مرتبہ)",
            titleAr = "سورة الفلق (3 مرات)",
            arabicText = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ ۝ قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ ۝ مِن شَرِّ مَا خَلَقَ ۝ وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ ۝ وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ ۝ وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ",
            transliterationEn = "Bismillahir-Rahmanir-Rahim. Qul a'udhu bi-Rabbil-falaq. Min sharri ma khalaq. Wa min sharri ghasiqin idha waqab. Wa min sharrin-naffathati fil-'uqad. Wa min sharri hasidin idha hasad.",
            transliterationHi = "बिस्मिल्लाहिर-रहमानिर-रहीम। क़ुल अऊज़ु बिरब्बिल-फ़लक़। मिन शर्रि मा ख़लक़। व मिन शर्रि ग़ासिक़िन इज़ा वक़ब। व मिन शर्रिन-नफ़्फ़ासाति फ़िल-'उक़द। व मिन शर्रि हासिदिन इज़ा हसद।",
            translationEn = "Say: I seek refuge in the Lord of daybreak from the evil of that which He created, and from the evil of darkness when it settles, and from the evil of the blowers in knots, and from the evil of an envier when he envies.",
            translationHi = "अल्लाह के नाम से जो बहुत मेहरबान, निहायत रहम फरमाने वाला है। कह दीजिए: मैं सुबह के रब की पनाह मांगता हूं, हर उस चीज़ की बुराई से जो उसने पैदा की, और अंधेरी रात की बुराई से जब वह छा जाए, और गिरहों में फूंकने वाली जादूगरनियों की बुराई से, और हसद करने वाले की बुराई से जब वह हसद करे।",
            translationUr = "اللہ کے نام سے جو نہایت مہربان، ہمیشہ رحم فرمانے والا ہے۔ فرما دیجئے: میں صبح کے رب کی پناہ مانگتا ہوں، ہر اس چیز کے شر سے جو اس نے پیدا کی، اور اندھیری رات کے شر سے جب وہ چھا جائے، اور گرہوں میں پھونکنے والیوں کے شر سے، اور حسد کرنے والے کے شر سے جب وہ حسد کرے۔",
            translationAr = "Say: I seek refuge in the Lord of daybreak from the evil of that which He created, and from the evil of darkness when it settles, and from the evil of the blowers in knots, and from the evil of an envier when he envies.",
            referenceEn = "Sunan Abi Dawud 5082, Tirmidhi 3575",
            referenceHi = "सुनन अबी दाऊद 5082, तिर्मिज़ी 3575",
            referenceUr = "سنن ابی داؤد 5082، جامع ترمذی 3575",
            referenceAr = "سنن أبي داود: 5082",
            benefitEn = "Divine shield against night evil, black magic, envy, and unseen spiritual harm.",
            benefitHi = "रात के अंधेरे, जादू, हसद और बुरी नज़र से हिफ़ाज़त की रब्बानी ढाल।",
            benefitUr = "رات کی تاریکی، جادو، حسد اور نظر بد سے حفاظت کی مضبوط ڈھال۔",
            hadithBook = "Sunan Abi Dawud 5082",
            hadithGrade = "Hasan Sahih",
            hadithNarrator = "Abdullah ibn Khubayb (RA)",
            hadithArabic = "تَكْفِيكَ مِنْ كُلِّ شَيْءٍ",
            hadithTranslationEn = "Reciting these 3 Surahs three times in the evening suffices against all harm.",
            hadithTranslationHi = "शाम को 3 बार इन सूरतों को पढ़ना हर तरह के शर से हिफ़ाज़त के लिए काफी है।",
            hadithTranslationUr = "",
            hadithUrl = "https://sunnah.com/abudawud:5082"
        ),
        AdhkarItem(
            id = "e_nas",
            order = 5,
            targetCount = 3,
            titleEn = "Surah An-Nas (3x)",
            titleHi = "सूरह अन-नास (3 बार)",
            titleUr = "سورۃ الناس (3 مرتبہ)",
            titleAr = "سورة الناس (3 مرات)",
            arabicText = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ ۝ قُلْ أَعُوذُ بِرَبِّ النَّاسِ ۝ مَلِكِ النَّاسِ ۝ إِلَٰهِ النَّاسِ ۝ مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ ۝ الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ ۝ مِنَ الْجِنَّةِ وَالنَّاسِ",
            transliterationEn = "Bismillahir-Rahmanir-Rahim. Qul a'udhu bi-Rabbin-nas. Malikin-nas. Ilahin-nas. Min sharril-waswasil-khannas. Alladhi yuwaswisu fi sudurin-nas. Minal-jinnati wan-nas.",
            transliterationHi = "बिस्मिल्लाहिर-रहमानिर-रहीम। क़ुल अऊज़ु बिरब्बिन-नास। मलिकिन-नास। इलाहिन-नास। मिन शर्रिल-वस्वासिल-ख़न्नास। अल्लज़ी युवस्विसु फ़ी सुदूरिन-नास। मिनल-जिन्नति वन-नास।",
            translationEn = "In the name of Allah, the Entirely Merciful, the Especially Merciful. Say: I seek refuge in the Lord of mankind, the Sovereign of mankind, the God of mankind, from the evil of the retreating whisperer who whispers into the breasts of mankind, from among the jinn and mankind.",
            translationHi = "अल्लाह के नाम से जो बहुत मेहरबान, निहायत रहम फरमाने वाला है। कह दीजिए: मैं इंसानों के रब की पनाह मांगता हूं, इंसानों के बादशाह की, इंसानों के सच्चे माबूद की, छुपकर वसवसा डालने वाले शैतान की बुराई से, जो इंसानों के सीनों में वसवसा डालता है, जिन्नात में से और इंसानों में से।",
            translationUr = "اللہ کے نام سے جو نہایت مہربان، ہمیشہ رحم فرمانے والا ہے۔ فرما دیجئے: میں انسانوں کے رب کی پناہ مانگتا ہوں، انسانوں کے بادشاہ کی، انسانوں کے معبود برحق کی، بار بار وسوسہ ڈال کر پیچھے ہٹ جانے والے کے شر سے، جو لوگوں کے سینوں میں وسوسے ڈالتا ہے، خواہ وہ جنوں میں سے ہو یا انسانوں میں سے۔",
            translationAr = "In the name of Allah, the Entirely Merciful, the Especially Merciful. Say: I seek refuge in the Lord of mankind, the Sovereign of mankind, the God of mankind, from the evil of the retreating whisperer who whispers into the breasts of mankind, from among the jinn and mankind.",
            referenceEn = "Sunan Abi Dawud 5082, Tirmidhi 3575",
            referenceHi = "सुनन अबी दाऊद 5082, तिर्मिज़ी 3575",
            referenceUr = "سنن ابی داؤد 5082، جامع ترمذی 3575",
            referenceAr = "سنن أبي داود: 5082",
            benefitEn = "Complete protection from whispers of shaytan, nightmare fears, and evil jinn at night.",
            benefitHi = "रात को शैतानी वसवसों, डरावने ख्वाबों और जिन्नों के हमलों से महफूज़ रहने की दुआ।",
            benefitUr = "رات کے وقت شیطانی وسوسوں، ڈراؤنے خوابوں اور جنات کے شر سے حفاظت کا قلعہ۔",
            hadithBook = "Sunan Abi Dawud 5082",
            hadithGrade = "Hasan Sahih",
            hadithNarrator = "Abdullah ibn Khubayb (RA)",
            hadithArabic = "قُلْ هُوَ اللَّهُ أَحَدٌ وَالْمُعَوِّذَتَيْنِ... تَكْفِيكَ مِنْ كُلِّ شَيْءٍ",
            hadithTranslationEn = "Protection from all types of physical and spiritual harm.",
            hadithTranslationHi = "हर तरह की जिस्मानी और रूहानी तकलीफों से मुकम्मल हिफ़ाज़त।",
            hadithTranslationUr = "",
            hadithUrl = "https://sunnah.com/abudawud:5082"
        ),
        AdhkarItem(
            id = "e_amsayna_mulk",
            order = 6,
            targetCount = 1,
            titleEn = "Amsayna wa Amsal-Mulku Lillah",
            titleHi = "अमसैना व अमसल-मुल्कु लिल्लाह",
            titleUr = "امسینا و امسی الملک للہ",
            titleAr = "أمسينا وأمسى الملك لله",
            arabicText = "أَمْسَيْنَا وَأَمْسَى الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لَا إِلَٰهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ، وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ، رَبِّ أَسْأَلُكَ خَيْرَ مَا فِي هَٰذِهِ اللَّيْلَةِ وَخَيْرَ مَا بَعْدَهَا، وَأَعُوذُ بِكَ مِنْ شَرِّ مَا فِي هَٰذِهِ اللَّيْلَةِ وَشَرِّ مَا بَعْدَهَا، رَبِّ أَعُوذُ بِكَ مِنَ الْكَسَلِ وَسُوءِ الْكِبَرِ، رَبِّ أَعُوذُ بِكَ مِنْ عَذَابٍ فِي النَّارِ وَعَذَابٍ فِي الْقَبْرِ",
            transliterationEn = "Amsayna wa amsal-mulku lillah, wal-hamdu lillah, la ilaha illAllahu wahdahu la sharika lah, lahul-mulku wa lahul-hamd, wa Huwa 'ala kulli shay'in qadir. Rabbi as'aluka khayra ma fi hadhihil-laylati wa khayra ma ba'daha, wa a'udhu bika min sharri ma fi hadhihil-laylati wa sharri ma ba'daha, Rabbi a'udhu bika minal-kasali wa su'il-kibar, Rabbi a'udhu bika min 'adhabin fin-nari wa 'adhabin fil-qabr.",
            transliterationHi = "अमसैना व अमसल-मुल्कु लिल्लाह, वल-हम्दु लिल्लाह, ला इलाहा इल्लल्लाहु वहदहू ला शरीका लह, लहुल-मुल्कु व लहुल-हम्द, व हुवा 'अला कुल्लि शैइन क़दीर। रब्बि असअलोका ख़ैरा मा फ़ी हाज़िहिल-लैलति व ख़ैरा मा ब'दहा, व अऊज़ु बिका मिन शर्रि मा फ़ी हाज़िहिल-लैलति व शर्रि मा ब'दहा। रब्बि अऊज़ु बिका मिनल-कसलि व सूइल-किबर, रब्बि अऊज़ु बिका मिन 'अज़ाबिन फ़िन-नारि व 'अज़ाबिन फ़िल-क़ब्र।",
            translationEn = "We have entered the evening and the sovereignty belongs to Allah, and all praise is to Allah. There is no deity except Allah alone, without partner; to Him belongs all sovereignty and praise, and He is over all things competent. My Lord, I ask You for the good of this night and the good of what follows it, and I seek refuge in You from the evil of this night and the evil of what follows it. My Lord, I seek refuge in You from laziness and the helplessness of old age. My Lord, I seek refuge in You from punishment in the Hellfire and punishment in the grave.",
            translationHi = "हमने शाम की और अल्लाह के तमाम मुल्क ने शाम की, और सब तारीफें अल्लाह के लिए हैं। अल्लाह के सिवा कोई सच्चा माबूद नहीं, वह अकेला है, उसका कोई शरीक नहीं, उसी की बादशाही है और उसी के लिए तमाम तारीफ़ है, और वह हर चीज़ पर क़ादिर है। ऐ मेरे रब! मैं तुझसे इस रात की भलाई और इसके बाद की भलाई मांगता हूं, और इस रात की बुराई और इसके बाद की बुराई से तेरी पनाह मांगता हूं। ऐ मेरे रब! मैं सुस्ती और बुढ़ापे की बुराइयों से तेरी पनाह मांगता हूं। ऐ मेरे रब! मैं जहन्नम के अज़ाब से और क़ब्र के अज़ाब से तेरी पनाह मांगता हूं।",
            translationUr = "ہم نے شام کی اور اللہ کی تمام سلطنت نے شام کی، اور تمام تعریفیں اللہ ہی کے لیے ہیں۔ اللہ کے سوا کوئی معبود نہیں، وہ اکیلا ہے، اس کا کوئی شریک نہیں، اسی کی بادشاہی ہے اور اسی کی تعریف، اور وہ ہر چیز پر قادر ہے۔ اے میرے رب! میں تجھ سے اس رات کی خیر اور اس کے بعد کی خیر مانگتا ہوں، اور اس رات کے شر اور اس کے بعد کے شر سے تیری پناہ مانگتا ہوں۔ اے میرے رب! میں سستی اور بڑھاپے کی برائیوں سے تیری پناہ مانگتا ہوں۔ اے میرے رب! میں جہنم کے عذاب سے اور قبر کے عذاب سے تیری پناہ مانگتا ہوں۔",
            translationAr = "We have entered the evening and the sovereignty belongs to Allah, and all praise is to Allah. There is no deity except Allah alone, without partner; to Him belongs all sovereignty and praise, and He is over all things competent. My Lord, I ask You for the good of this night and the good of what follows it, and I seek refuge in You from the evil of this night and the evil of what follows it. My Lord, I seek refuge in You from laziness and the helplessness of old age. My Lord, I seek refuge in You from punishment in the Hellfire and punishment in the grave.",
            referenceEn = "Sahih Muslim 2723",
            referenceHi = "सहीह मुस्लिम 2723",
            referenceUr = "صحیح مسلم 2723",
            referenceAr = "صحيح مسلم: 2723",
            benefitEn = "Protection for the whole night from laziness, evil disasters, Hellfire, and the torment of the grave.",
            benefitHi = "रात भर की अफ़ात, सुस्ती, जहन्नम की आग और क़ब्र के अज़ाब से महफूज़ रहने की दुआ।",
            benefitUr = "رات بھر کی آفات، سستی، جہنم کی آگ اور قبر کے عذاب سے پناہ کی مسنون دعا۔",
            hadithBook = "Sahih Muslim 2723",
            hadithGrade = "Sahih",
            hadithNarrator = "Abdullah ibn Mas'ud (RA)",
            hadithArabic = "كَانَ نَبِيُّ اللهِ صَلَّى اللهُ عَلَيْهِ وَسَلَّمَ، إِذَا أَمْسَى قَالَ: أَمْسَيْنَا وَأَمْسَى الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ...",
            hadithTranslationEn = "When evening came, the Prophet of Allah ﷺ used to say: Amsayna wa amsal-mulku lillah...",
            hadithTranslationHi = "रसूलुल्लाह ﷺ जब शाम करते तो यह दुआ पढ़ा करते थे।",
            hadithTranslationUr = "",
            hadithUrl = "https://sunnah.com/muslim:2723"
        ),
        AdhkarItem(
            id = "e_bika_amsayna",
            order = 7,
            targetCount = 1,
            titleEn = "Allahumma bika amsayna",
            titleHi = "अल्लाहुम्मा बिका अमसैना",
            titleUr = "اللہم بک امسینا",
            titleAr = "اللهم بك أمسينا",
            arabicText = "اللَّهُمَّ بِكَ أَمْسَيْنَا، وَبِكَ أَصْبَحْنَا، وَبِكَ نَحْيَا، وَبِكَ نَمُوتُ، وَإِلَيْكَ الْمَصِيرُ",
            transliterationEn = "Allahumma bika amsayna, wa bika asbahna, wa bika nahya, wa bika namutu, wa ilaykal-masir.",
            transliterationHi = "अल्लाहुम्मा बिका अमसैना, व बिका असबहना, व बिका नहया, व बिका नमूतू, व इलैकल-मसीर।",
            translationEn = "O Allah, by You we enter the evening, and by You we enter the morning, by You we live, and by You we die, and to You is the final destination.",
            translationHi = "ऐ अल्लाह! तेरी ही मदद से हमने शाम की, और तेरी ही मदद से हमने सुबह की, तेरे ही हुक्म से हम जीते हैं, और तेरे ही हुक्म से हम मरते हैं, और तेरी ही तरफ लौट कर जाना है।",
            translationUr = "اے اللہ! تیری ہی توفیق سے ہم نے شام کی اور تیری ہی توفیق سے صبح کی، تیرے ہی حکم سے ہم جیتے ہیں اور تیرے ہی حکم سے مرتے ہیں، اور تیری ہی طرف پلٹنا ہے۔",
            translationAr = "O Allah, by You we enter the evening, and by You we enter the morning, by You we live, and by You we die, and to You is the final destination.",
            referenceEn = "Sunan Abi Dawud 5068, Tirmidhi 3391",
            referenceHi = "सुनन अबी दाऊद 5068, तिर्मिज़ी 3391",
            referenceUr = "سنن ابی داؤد 5068، جامع ترمذی 3391",
            referenceAr = "سنن أبي داود: 5068",
            benefitEn = "Affirming life, death, evening, morning, and the ultimate return to Allah.",
            benefitHi = "शाम और सुबह, ज़िंदगी और मौत में अल्लाह पर मुकम्मल ईमान और उसी की तरफ लौटने का अक़ीदा।",
            benefitUr = "شام اور صبح، زندگی اور موت میں اللہ تعالیٰ پر کامل توکل اور واپسی کا اقرار۔",
            hadithBook = "Sunan Abi Dawud 5068, Jami` at-Tirmidhi 3391",
            hadithGrade = "Sahih (Al-Albani)",
            hadithNarrator = "Abu Hurairah (RA)",
            hadithArabic = "إِذَا أَمْسَى قَالَ: اللَّهُمَّ بِكَ أَمْسَيْنَا وَبِكَ أَصْبَحْنَا وَبِكَ نَحْيَا وَبِكَ نَمُوتُ وَإِلَيْكَ الْمَصِيرُ",
            hadithTranslationEn = "The Prophet ﷺ used to say in the evening: Allahumma bika amsayna...",
            hadithTranslationHi = "नबी करीम ﷺ जब शाम करते तो यह दुआ फरमाते थे।",
            hadithTranslationUr = "",
            hadithUrl = "https://sunnah.com/abudawud:5068"
        ),
        AdhkarItem(
            id = "e_sayyidul_istighfar",
            order = 8,
            targetCount = 1,
            titleEn = "Sayyid al-Istighfar",
            titleHi = "सय्यिदुल इस्तिग़फ़ार",
            titleUr = "سید الاستغفار",
            titleAr = "سيد الاستغفار",
            arabicText = "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَٰهَ إِلَّا أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَىٰ عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ لَكَ بِذَنْبِي، فَاغْفِرْ لِي، فَإِنَّهُ لَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ",
            transliterationEn = "Allahumma Anta Rabbi la ilaha illa Anta, khalaqtani wa ana 'abduka, wa ana 'ala 'ahdika wa wa'dika mastata'tu, a'udhu bika min sharri ma sana'tu, abu'u laka bi-ni'matika 'alayya, wa abu'u laka bi-dhanbi faghfir li, fa-innahu la yaghfirudh-dhunuba illa Anta.",
            transliterationHi = "अल्लाहुम्मा अंता रब्बी ला इलाहा इल्ला अंत, ख़लक़तनी व अना 'अब्दुक, व अना 'अला 'अह़दिका व व'दिका मस्तत'अ्तु, अऊज़ु बिका मिन शर्रि मा सन'अ्तु, अबूउ लका बि-नि'मतिका 'अलैय्या, व अबूउ बि-ज़म्बी फ़ग़्फ़िर ली, फ़इन्नहू ला यग़्फ़िरुज़-ज़ुनूबा इल्ला अंत।",
            translationEn = "O Allah, You are my Lord, none has the right to be worshipped except You. You created me and I am Your slave, and I am faithful to my covenant and my promise to You as much as I can. I seek refuge in You from all the evil I have done. I acknowledge before You all the blessings You have bestowed upon me, and I confess to You all my sins. So forgive me, for indeed none can forgive sins except You.",
            translationHi = "ऐ अल्लाह! तू ही मेरा रब है, तेरे सिवा कोई सच्चा माबूद नहीं, तूने ही मुझे पैदा किया और मैं तेरा बंदा हूं, और मैं अपनी ताक़त के मुताबिक तेरे अहद और वादे पर क़ायम हूं। मैं अपने किए हुए आमाल की बुराई से तेरी पनाह मांगता हूं, मैं अपने ऊपर तेरी नेमतों का इक़रार करता हूं और अपने गुनाहों का एतिराफ़ करता हूं, पस मुझे बख़्श दे, क्योंकि तेरे सिवा कोई गुनाहों को बख़्शने वाला नहीं।",
            translationUr = "اے اللہ! تو ہی میرا رب ہے، تیرے سوا کوئی معبود نہیں، تو نے ہی مجھے پیدا کیا اور میں تیرا بندہ ہوں، اور میں اپنی طاقت کے مطابق تیرے عہد اور وعدے پر قائم ہوں۔ میں اپنے کیے کے شر سے تیری پناہ مانگتا ہوں، میں اپنے اوپر تیری نعمتوں کا اقرار کرتا ہوں اور اپنے گناہوں کا اعتراف کرتا ہوں، پس مجھے بخش دے، کیونکہ تیرے سوا کوئی گناہوں کو معاف نہیں کر سکتا۔",
            translationAr = "O Allah, You are my Lord, none has the right to be worshipped except You. You created me and I am Your slave, and I am faithful to my covenant and my promise to You as much as I can. I seek refuge in You from all the evil I have done. I acknowledge before You all the blessings You have bestowed upon me, and I confess to You all my sins. So forgive me, for indeed none can forgive sins except You.",
            referenceEn = "Sahih al-Bukhari 6306",
            referenceHi = "सहीह बुख़ारी 6306",
            referenceUr = "صحیح بخاری 6306",
            referenceAr = "صحيح البخاري: 6306",
            benefitEn = "Whoever recites it in the evening with firm faith and dies that night enters Paradise directly.",
            benefitHi = "जो शख्स यक़ीन के साथ शाम को इसे पढ़े और उसी रात उसका इंतिक़ाल हो जाए, वह जन्नत में दाखिल होगा।",
            benefitUr = "جس نے یقین کے ساتھ شام کے وقت یہ دعا پڑھی اور اسی رات وفات پا گیا تو وہ اہلِ جنت میں سے ہوگا۔",
            hadithBook = "Sahih al-Bukhari 6306",
            hadithGrade = "Sahih",
            hadithNarrator = "Shaddad ibn Aws (RA)",
            hadithArabic = "وَمَنْ قَالَهَا مِنَ اللَّيْلِ وَهْوَ مُوقِنٌ بِهَا، فَمَاتَ قَبْلَ أَنْ يُصْبِحَ، دَخَلَ الْجَنَّةَ",
            hadithTranslationEn = "The Prophet ﷺ said: 'If somebody recites it during the night with firm faith in it, and dies before morning, he will be from the people of Paradise.'",
            hadithTranslationHi = "नबी ﷺ ने फरमाया: 'जिसने रात में कामिल यक़ीन के साथ इसे पढ़ा और सुबह से पहले मर गया तो वह जन्नत में दाखिल होगा।'",
            hadithTranslationUr = "",
            hadithUrl = "https://sunnah.com/bukhari:6306"
        ),
        AdhkarItem(
            id = "e_ushhiduka",
            order = 9,
            targetCount = 4,
            titleEn = "Allahumma inni amsaytu ush-hiduka (4x)",
            titleHi = "अल्लाहुम्मा इन्नी अमसैतु उश-हिदुक (4 बार)",
            titleUr = "اللہم انی امسیت اشہدک (4 مرتبہ)",
            titleAr = "اللهم إني أمسيت أشهدك (4 مرات)",
            arabicText = "اللَّهُمَّ إِنِّي أَمْسَيْتُ أُشْهِدُكَ، وَأُشْهِدُ حَمَلَةَ عَرْشِكَ، وَمَلَائِكَتَكَ، وَجَمِيعَ خَلْقِكَ، أَنَّكَ أَنْتَ اللَّهُ لَا إِلَٰهَ إِلَّا أَنْتَ، وَحْدَكَ لَا شَرِيكَ لَكَ، وَأَنَّ مُحَمَّدًا عَبْدُكَ وَرَسُولُكَ",
            transliterationEn = "Allahumma inni amsaytu ush-hiduka, wa ush-hidu hamalata 'arshika, wa mala'ikataka, wa jami'a khalqika, annaka AntAllahu la ilaha illa Anta, wahdaka la sharika laka, wa anna Muhammadan 'abduka wa rasuluk.",
            transliterationHi = "अल्लाहुम्मा इन्नी अमसैतु उश-हिदुक, व उश-हिदु हमालता 'अर्शिक, व मलाइकतक, व जमी'अ ख़ल्क़िक, अन्नका अन्तल्लाहु ला इलाहा इल्ला अंत, वहदका ला शरीका लक, व अन्ना मुहम्मदन 'अब्दुका व रसूलुक।",
            translationEn = "O Allah, I have reached the evening and I call You to witness, and I call the bearers of Your Throne, Your angels, and all of Your creation to witness, that You are Allah, there is no deity except You alone, without partner, and that Muhammad is Your servant and Your Messenger.",
            translationHi = "ऐ अल्लाह! मैंने शाम की और मैं तुझे गवाह बनाता हूं, और तेरे अर्श को उठाने वाले फ़रिश्तों को, तेरे तमाम फ़रिश्तों को, और तेरी पूरी मख़लूक़ को गवाह बनाता हूं कि बेशक तू ही अल्लाह है, तेरे सिवा कोई सच्चा माबूद नहीं, तू अकेला है तेरा कोई शरीक नहीं, और बेशक हज़रत मुहम्मद ﷺ तेरे बंदे और तेरे रसूल हैं।",
            translationUr = "اے اللہ! میں نے شام کی اور میں تجھے گواہ بناتا ہوں، اور تیرے عرش کو اٹھانے والے فرشتوں کو، تیرے تمام فرشتوں کو اور تیری تمام مخلوق کو گواہ بناتا ہوں کہ بے شک تو ہی اللہ ہے، تیرے سوا کوئی معبود نہیں، تو اکیلا ہے، اور بے شک حضرت محمد ﷺ تیرے بندے اور رسول ہیں۔",
            translationAr = "O Allah, I have reached the evening and I call You to witness, and I call the bearers of Your Throne, Your angels, and all of Your creation to witness, that You are Allah, there is no deity except You alone, without partner, and that Muhammad is Your servant and Your Messenger.",
            referenceEn = "Sunan Abi Dawud 5069, Al-Adab al-Mufrad 1201",
            referenceHi = "सुनन अबी दाऊद 5069, अल-अदबुल मुफ़्रद 1201",
            referenceUr = "سنن ابی داؤد 5069، الادب المفرد 1201",
            referenceAr = "سنن أبي داود: 5069",
            benefitEn = "Whoever says this 4 times in the evening, Allah frees his entire body from the Hellfire.",
            benefitHi = "जो शख्स शाम को 4 बार यह दुआ पढ़े, अल्लाह तआला उसे पूरी तरह जहन्नम की आग से आज़ाद फरमा देता है।",
            benefitUr = "جو شخص شام کے وقت 4 مرتبہ یہ کلمات کہے، اللہ تعالیٰ اس کے پورے جسم کو جہنم کی آگ سے آزاد فرما دیتا ہے۔",
            hadithBook = "Sunan Abi Dawud 5069",
            hadithGrade = "Hasan",
            hadithNarrator = "Anas ibn Malik (RA)",
            hadithArabic = "مَنْ قَالَ حِينَ يُمْسِي أَوْ يُصْبِحُ: اللَّهُمَّ إِنِّي أَصْبَحْتُ أُشْهِدُكَ... أَعْتَقَ اللَّهُ رُبُعَهُ مِنَ النَّارِ، فَإِنْ قَالَهَا مَرَّتَيْنِ أَعْتَقَ اللَّهُ نِصْفَهُ... فَإِنْ قَالَهَا أَرْبَعًا أَعْتَقَهُ اللَّهُ مِنَ النَّارِ",
            hadithTranslationEn = "The Messenger of Allah ﷺ said: 'Whoever says in the evening or morning: Allahumma inni amsaytu ush-hiduka... four times, Allah will free him completely from the Fire.'",
            hadithTranslationHi = "रसूलुल्लाह ﷺ ने फरमाया: 'जो शख्स शाम को 4 बार यह पढ़ेगा, अल्लाह उसे जहन्नम की आग से पूरी तरह आज़ाद कर देगा।'",
            hadithTranslationUr = "",
            hadithUrl = "https://sunnah.com/abudawud:5069"
        ),
        AdhkarItem(
            id = "e_ma_amsa_bi",
            order = 10,
            targetCount = 1,
            titleEn = "Allahumma ma amsa bi min ni'matin",
            titleHi = "अल्लाहुम्मा मा अमसा बी मिन निअमतिन",
            titleUr = "اللہم ما امسی بی من نعمۃ",
            titleAr = "اللهم ما أمسى بي من نعمة",
            arabicText = "اللَّهُمَّ مَا أَمْسَى بِي مِنْ نِعْمَةٍ، أَوْ بِأَحَدٍ مِنْ خَلْقِكَ، فَمِنْكَ وَحْدَكَ لَا شَرِيكَ لَكَ، فَلَكَ الْحَمْدُ وَلَكَ الشُّكْرُ",
            transliterationEn = "Allahumma ma amsa bi min ni'matin, aw bi-ahadim-min khalqika, faminka wahdaka la sharika laka, falakal-hamdu wa lakash-shukr.",
            transliterationHi = "अल्लाहुम्मा मा अमसा बी मिन नि'मतिन, औ बि-अहदिम-मिन ख़ल्क़िक, फमिन्का वहदका ला शरीका लक, फ़लकल्-हम्दु व लकश-शुक्र।",
            translationEn = "O Allah, whatever blessing I or any of Your creation have reached the evening with, it is from You alone, without partner; so to You belongs all praise and to You belongs all thanks.",
            translationHi = "ऐ अल्लाह! शाम के वक़्त मुझ पर या तेरी मख़लूक़ में से किसी पर भी जो नेमत है, वह सिर्फ और सिर्फ तेरी तरफ से है, तेरा कोई शरीक नहीं, पस तमाम तारीफ़ें तेरे ही लिए हैं और तमाम शुक्र तेरे ही लिए है।",
            translationUr = "اے اللہ! شام کے وقت مجھ پر یا تیری مخلوق میں سے کسی پر بھی جو نعمت ہے، وہ صرف تیری ہی طرف سے ہے، تیرا کوئی شریک نہیں، پس تمام حمد اور تمام شکر تیرے ہی لیے ہے۔",
            translationAr = "O Allah, whatever blessing I or any of Your creation have reached the evening with, it is from You alone, without partner; so to You belongs all praise and to You belongs all thanks.",
            referenceEn = "Sunan Abi Dawud 5073, Ibn Hibban 861",
            referenceHi = "सुनन अबी दाऊद 5073, इब्न हिब्बान 861",
            referenceUr = "سنن ابی داؤد 5073، ابن حبان 861",
            referenceAr = "سنن أبي داود: 5073",
            benefitEn = "Whoever recites this in the evening has fulfilled the entire gratitude (shukr) owed for that night.",
            benefitHi = "जो शख्स शाम को यह दुआ पढ़ लेता है, उसने उस पूरी रात का शुक्र अदा कर दिया।",
            benefitUr = "جس نے شام کے وقت یہ دعا پڑھی، اس نے اس پوری رات کا شکر ادا کر دیا۔",
            hadithBook = "Sunan Abi Dawud 5073",
            hadithGrade = "Hasan",
            hadithNarrator = "Abdullah ibn Ghannam al-Bayadi (RA)",
            hadithArabic = "مَنْ قَالَ حِينَ يُمْسِي: اللَّهُمَّ مَا أَمْسَى بِي مِنْ نِعْمَةٍ... فَقَدْ أَدَّى شُكْرَ لَيْلَتِهِ",
            hadithTranslationEn = "The Messenger of Allah ﷺ said: 'Whoever says when evening comes: Allahumma ma amsa bi min ni'matin... has fulfilled the gratitude due for his night.'",
            hadithTranslationHi = "रसूलुल्लाह ﷺ ने फरमाया: 'जिसने शाम के वक़्त यह कहा, उसने अपनी उस रात का शुक्र अदा कर दिया।'",
            hadithTranslationUr = "",
            hadithUrl = "https://sunnah.com/abudawud:5073"
        ),
        AdhkarItem(
            id = "e_afini_badani_combo",
            order = 11,
            targetCount = 3,
            titleEn = "Allahumma 'afini fi badani & Isti'adha (3x)",
            titleHi = "आफ़ियत और कुफ्र-ओ-फ़क़्र से पनाह (3 बार)",
            titleUr = "عافیت اور کفر و فقر سے پناہ (3 مرتبہ)",
            titleAr = "العافية والاستعاذة من الكفر والفقر (3 مرات)",
            arabicText = "اللَّهُمَّ عَافِنِي فِي بَدَنِي، اللَّهُمَّ عَافِنِي فِي سَمْعِي، اللَّهُمَّ عَافِنِي فِي بَصَرِي، لَا إِلَٰهَ إِلَّا أَنْتَ، اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْكُفْرِ وَالْفَقْرِ، اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنْ عَذَابِ الْقَبْرِ، لَا إِلَٰهَ إِلَّا أَنْتَ",
            transliterationEn = "Allahumma 'afini fi badani, Allahumma 'afini fi sam'i, Allahumma 'afini fi basari, la ilaha illa Anta. Allahumma inni a'udhu bika minal-kufri wal-faqr, Allahumma inni a'udhu bika min 'adhabil-qabr, la ilaha illa Anta.",
            transliterationHi = "अल्लाहुम्मा 'आफ़िनी फ़ी बदनी, अल्लाहुम्मा 'आफ़िनी फ़ी सम'ई, अल्लाहुम्मा 'आफ़िनी फ़ी बसरी, ला इलाहा इल्ला अंत। अल्लाहुम्मा इन्नी अऊज़ु बिका मिनल-कुफ़्रि वल-फ़क़्र, व अऊज़ु बिका मिन 'अज़ाबिल-क़ब्र, ला इलाहा इल्ला अंत।",
            translationEn = "O Allah, grant well-being to my body, my hearing, and my sight. There is no deity except You. O Allah, I seek refuge in You from disbelief, poverty, and the punishment of the grave. There is no deity except You.",
            translationHi = "ऐ अल्लाह! मेरे जिस्म में, मेरे कानों में, और मेरी आंखों में आफ़ियत अता फरमा। तेरे सिवा कोई माबूद नहीं। ऐ अल्लाह! मैं कुफ्र, मुफ़लिसी और क़ब्र के अज़ाब से तेरी पनाह मांगता हूं।",
            translationUr = "اے اللہ! میرے بدن، میرے کانوں اور میری آنکھوں میں عافیت عطا فرما۔ تیرے سوا کوئی معبود نہیں۔ اے اللہ! میں کفر، تنگدستی اور عذاب قبر سے تیری پناہ مانگتا ہوں۔",
            translationAr = "O Allah, grant well-being to my body, my hearing, and my sight. There is no deity except You. O Allah, I seek refuge in You from disbelief, poverty, and the punishment of the grave. There is no deity except You.",
            referenceEn = "Sunan Abi Dawud 5090, Musnad Ahmad",
            referenceHi = "सुनन अबी दाऊद 5090, मुसनद अहमद",
            referenceUr = "سنن ابی داؤد 5090، مسند احمد",
            referenceAr = "سنن أبي داود: 5090",
            benefitEn = "Sunnah of the Prophet ﷺ recited 3 times morning and evening for physical health, faith protection, and grave safety.",
            benefitHi = "जिस्मानी सेहत, आंखों-कानों की हिफ़ाज़त, कंगाली और क़ब्र के अज़ाब से बचने की 3 बार पढ़ी जाने वाली मुजर्रब सुन्नत दुआ।",
            benefitUr = "جسمانی صحت، آنکھوں کانوں کی حفاظت، غربت اور عذاب قبر سے پناہ کی مسنون دعا۔",
            hadithBook = "Sunan Abi Dawud 5090",
            hadithGrade = "Hasan",
            hadithNarrator = "Abu Bakrah (RA)",
            hadithArabic = "إِنِّي سَمِعْتُ رَسُولَ اللَّهِ صلى الله عليه وسلم يَدْعُو بِهِنَّ ثَلاَثًا حِينَ يُصْبِحُ وَثَلاَثًا حِينَ يُمْسِي",
            hadithTranslationEn = "Abu Bakrah reported that the Prophet ﷺ prayed with these words three times in the morning and three times in the evening.",
            hadithTranslationHi = "अबू बकरा (रज़ि.) फरमाते हैं कि रसूलुल्लाह ﷺ सुबह और शाम 3-3 बार इन कलिमात से दुआ करते थे।",
            hadithTranslationUr = "",
            hadithUrl = "https://sunnah.com/abudawud:5090"
        ),
        AdhkarItem(
            id = "e_hasbiyallah",
            order = 12,
            targetCount = 7,
            titleEn = "HasbiyAllahu la ilaha illa Huwa (7x)",
            titleHi = "हसबियल्लाहु ला इलाहा इल्ला हुव (7 बार)",
            titleUr = "حسبی اللہ لا الہ الا ہو (7 مرتبہ)",
            titleAr = "حسبي الله لا إله إلا هو (7 مرات)",
            arabicText = "حَسْبِيَ اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ، عَلَيْهِ تَوَكَّلْتُ، وَهُوَ رَبُّ الْعَرْشِ الْعَظِيمِ",
            transliterationEn = "HasbiyAllahu la ilaha illa Huwa, 'alayhi tawakkaltu, wa Huwa Rabbul-'Arshil-'Azim.",
            transliterationHi = "हसबियल्लाहु ला इलाहा इल्ला हुवा, 'अलैहि तवक्कल्तु, व हुवा रब्बुल-'अर्शिल-'अज़ीम।",
            translationEn = "Allah is sufficient for me; there is no deity except Him. Upon Him I have relied, and He is the Lord of the Great Throne.",
            translationHi = "मुझे अल्लाह ही काफी है, उसके सिवा कोई सच्चा माबूद नहीं, उसी पर मैंने भरोसा किया, और वही अज़ीम अर्श का रब है।",
            translationUr = "مجھے اللہ ہی کافی ہے، اس کے سوا کوئی معبود نہیں، اسی پر میں نے بھروسہ کیا، اور وہی عرشِ عظیم کا مالک ہے۔",
            translationAr = "Allah is sufficient for me; there is no deity except Him. Upon Him I have relied, and He is the Lord of the Great Throne.",
            referenceEn = "Sunan Abi Dawud 5081, Qur'an 9:129",
            referenceHi = "सुनन अबी दाऊद 5081, सूरह अत-तौबा (9:129)",
            referenceUr = "سنن ابی داؤد 5081، سورۃ التوبہ (9:129)",
            referenceAr = "سنن أبي داود: 5081",
            benefitEn = "Whoever says this 7 times in the evening, Allah will suffice him for all worries of this world and the Hereafter.",
            benefitHi = "जो शख्स शाम को 7 बार यह कहेगा, अल्लाह तआला उसकी दुनिया और आख़िरत की तमाम चिंताओं के लिए काफी हो जाएगा।",
            benefitUr = "جو شخص شام کے وقت 7 مرتبہ یہ کہے گا، اللہ تعالیٰ اس کے تمام غموں اور پریشانیوں کے لیے کافی ہو جائے گا۔",
            hadithBook = "Sunan Abi Dawud 5081",
            hadithGrade = "Sahih Mawquf",
            hadithNarrator = "Abu ad-Darda (RA)",
            hadithArabic = "مَنْ قَالَ إِذَا أَصْبَحَ وَإِذَا أَمْسَى: حَسْبِيَ اللَّهُ لاَ إِلَهَ إِلاَّ هُوَ عَلَيْهِ تَوَكَّلْتُ وَهُوَ رَبُّ الْعَرْشِ الْعَظِيمِ سَبْعَ مَرَّاتٍ، كَفَاهُ اللَّهُ مَا أَهَمَّهُ",
            hadithTranslationEn = "Whoever says 7 times in the morning and evening: 'HasbiyAllahu...', Allah will suffice him for all that concerns him.",
            hadithTranslationHi = "जो शख्स सुबह और शाम 7 बार यह पढ़ेगा, अल्लाह तआला उसके तमाम मुआमलात के लिए काफी हो जाएगा।",
            hadithTranslationUr = "",
            hadithUrl = "https://sunnah.com/abudawud:5081"
        ),
        AdhkarItem(
            id = "e_afwa_afiyah",
            order = 13,
            targetCount = 1,
            titleEn = "Dua for Well-being & Protection",
            titleHi = "आफ़ियत और छहों दिशाओं से हिफ़ाज़त की दुआ",
            titleUr = "عافیت اور مکمل حفاظت کی دعا",
            titleAr = "دعاء العفو والعافية في المساء",
            arabicText = "اللَّهُمَّ إِنِّي أَسْأَلُكَ الْعَافِيَةَ فِي الدُّنْيَا وَالْآخِرَةِ، اللَّهُمَّ إِنِّي أَسْأएलُكَ الْعَفْوَ وَالْعَافِيَةَ فِي دِينِي وَدُنْيَايَ وَأَهْلِي وَمَالِي، اللَّهُمَّ اسْتُرْ عَوْرَاتِي وَآمِنْ رَوْعَاتِي، اللَّهُمَّ احْفَظْنِي مِنْ بَيْنِ يَدَيَّ، وَمِنْ خَلْفِي، وَعَنْ يَمِينِي، وَعَنْ شِمَالِي، وَمِنْ فَوْقِي، وَأَعُوذُ بِعَظَمَتِكَ أَنْ أُغْتَالَ مِنْ تَحْتِي",
            transliterationEn = "Allahumma inni as'alukal-'afiyata fid-dunya wal-akhirah. Allahumma inni as'alukal-'afwa wal-'afiyata fi dini wa dunyaya wa ahli wa mali. Allahummastur 'awrati wa amin raw'ati. Allahummahfazni mim-bayni yadayya, wa min khalfi, wa 'an yamini, wa 'an shimali, wa min fawqi, wa a'udhu bi-'azamatika an ughtala min tahti.",
            transliterationHi = "अल्लाहुम्मा इन्नी असअलोकल-'आफ़ियता फ़िद-दुनिया वल-आख़िरह। अल्लाहुम्मा इन्नी असअलोकल-'अफ़्वा वल-'आफ़ियता फ़ी दीनी व दुनियाया व अहलि व माली। अल्लाहुम्मस-तुर औरतिया व आमिन रव'आति। अल्लाहुमह-फ़ज़नी मिम-बैनि यदय्या, व मिन ख़ल्फ़ी, व 'अन यमीनी, व 'अन शिमाली, व मिन फ़ौक़ी, व अऊज़ु बि-'अज़मतिका अन उग़्ताल मिन तहती।",
            translationEn = "O Allah, I ask You for well-being in this world and the Hereafter. O Allah, I ask You for forgiveness and well-being in my religion, my worldly affairs, my family, and my wealth. O Allah, conceal my faults and keep me safe from my fears. O Allah, protect me from before me, behind me, on my right, on my left, and from above me, and I seek refuge in Your greatness from being struck down from beneath me.",
            translationHi = "ऐ अल्लाह! मैं तुझसे दुनिया और आख़िरत में आफ़ियत मांगता हूं। ऐ अल्लाह! मैं तुझसे अपने दीन, अपनी दुनिया, अपने अहल-ओ-अयाल और अपने माल में दरगुज़र और आफ़ियत मांगता हूं। ऐ अल्लाह! मेरे ऐबों को छुपा ले और मेरे खौफ़ को अमन में बदल दे। ऐ अल्लाह! मेरी हिफ़ाज़त फरमा मेरे आगे से, मेरे पीछे से, मेरे दाएं से, मेरे बाएं से, और मेरे ऊपर से; और मैं तेरी अज़मत की पनाह मांगता हूं इस बात से कि मैं अपने नीचे से अचानक हलाक कर दिया जाऊं।",
            translationUr = "اے اللہ! میں تجھ سے دنیا اور آخرت میں عافیت مانگتا ہوں۔ اے اللہ! میں تجھ سے اپنے دین، اپنی دنیا، اپنے اہل و عیال اور مال میں معافی اور عافیت مانگتا ہوں۔ اے اللہ! میرے عیبوں کو چھپا لے اور میرے خوف کو امن میں بدل دے۔ اے اللہ! میری حفاظت فرما میرے آگے سے، میرے پیچھے سے، میرے دائیں سے، میرے بائیں سے، اور میرے اوپر سے؛ اور میں تیری عظمت کی پناہ مانگتا ہوں اس بات سے کہ میں نیچے سے اچانک ہلاک کیا جاؤں۔",
            translationAr = "O Allah, I ask You for well-being in this world and the Hereafter. O Allah, I ask You for forgiveness and well-being in my religion, my worldly affairs, my family, and my wealth. O Allah, conceal my faults and keep me safe from my fears. O Allah, protect me from before me, behind me, on my right, on my left, and from above me, and I seek refuge in Your greatness from being struck down from beneath me.",
            referenceEn = "Sunan Abi Dawud 5074, Ibn Majah 3871",
            referenceHi = "सुनन अबी दाऊद 5074, इब्न माजा 3871",
            referenceUr = "سنن ابی داؤد 5074، ابن ماجہ 3871",
            referenceAr = "سنن أبي داود: 5074",
            benefitEn = "Never left by the Prophet ﷺ, securing divine protection across all 6 physical and spiritual directions.",
            benefitHi = "रसूलुल्लाह ﷺ ने कभी इसे नहीं छोड़ा; छहों दिशाओं से हिफ़ाज़त की अज़ीम रात की ढाल।",
            benefitUr = "رسول اللہ ﷺ نے کبھی اسے ترک نہیں کیا؛ چھ کے چھ اطراف سے کامل حفاظت کی دعا۔",
            hadithBook = "Sunan Abi Dawud 5074",
            hadithGrade = "Sahih",
            hadithNarrator = "Abdullah ibn Umar (RA)",
            hadithArabic = "لَمْ يَكُنْ رَسُولُ اللَّهِ صلى الله عليه وسلم يَدَعُ هَؤُلاَءِ الدَّعَوَاتِ حِينَ يُمْسِي وَحِينَ يُصْبِحُ",
            hadithTranslationEn = "Ibn Umar reported: The Messenger of Allah ﷺ never abandoned these supplications in the morning and evening.",
            hadithTranslationHi = "इब्ने उमर (रज़ि.) फरमाते हैं कि रसूलुल्लाह ﷺ सुबह और शाम कभी इन दुआओं को नहीं छोड़ते थे।",
            hadithTranslationUr = "",
            hadithUrl = "https://sunnah.com/abudawud:5074"
        ),
        AdhkarItem(
            id = "e_aalimal_ghayb",
            order = 14,
            targetCount = 1,
            titleEn = "Allahumma 'Alimal-Ghayb (Fatiras-Samawat)",
            titleHi = "अल्लाहुम्मा आलिमल-ग़ैब (फ़ातिरस-समावात)",
            titleUr = "اللہم عالم الغیب والشہادۃ",
            titleAr = "اللهم عالم الغيب والشهادة",
            arabicText = "اللَّهُمَّ عَالِمَ الْغَيْبِ وَالشَّهَادَةِ، فَاطِرَ السَّمَاوَاتِ وَالْأَرْضِ، رَبَّ كُلِّ شَيْءٍ وَمَلِيكَهُ، أَشْهَدُ أَنْ لَا إِلَٰهَ إِلَّا أَنْتَ، أَعُوذُ بِكَ مِنْ شَرِّ نَفْسِي، وَمِنْ شَرِّ الشَّيْطَانِ وَشِرْكِهِ، وَأَنْ أَقْتَرِفَ عَلَىٰ نَفْسِي سُوءًا أَوْ أَجُرَّهُ إِلَىٰ مُسْلِمٍ",
            transliterationEn = "Allahumma 'Alimal-ghaybi wash-shahadati, Fatiras-samawati wal-ardi, Rabba kulli shay'in wa Malikahu, ash-hadu an la ilaha illa Anta, a'udhu bika min sharri nafsi, wa min sharrish-shaytani wa shirkihi, wa an aqtarifa 'ala nafsi su'an aw ajurrahu ila Muslim.",
            transliterationHi = "अल्लाहुम्मा 'आलिमल-ग़ैबि wash-शहादति, फ़ातिरस-समावाति वल-अर्दि, रब्बा कुल्लि शैइंव-व मलीकह, अशहदु अल-ला इलाहा इल्ला अंत, अऊज़ु बिका मिन शर्रि नफ़्सी, व मिन शर्रिश-शैतानि व शिर्किही, व अन अक़तरिफ़ा 'अला नफ़्सी सूअन औ अजुर्रहू इला मुस्लिम।",
            translationEn = "O Allah, Knower of the unseen and the seen, Creator of the heavens and the earth, Lord and Sovereign of all things. I bear witness that there is no deity except You. I seek refuge in You from the evil of my own self, from the evil of Satan and his traps, and from committing any wrong against myself or bringing harm upon any Muslim.",
            translationHi = "ऐ अल्लाह! छुपे और खुले को जानने वाले, आसमानों और ज़मीन के पैदा करने वाले, हर चीज़ के रब और मालिक! मैं गवाही देता हूं कि तेरे सिवा कोई माबूद नहीं। मैं तेरी पनाह मांगता हूं अपने नफ़्स के शर से, शैतान के शर और उसके जाल से, और इस बात से कि मैं अपने आप पर कोई बुराई करूं या किसी मुसलमान को नुकसान पहुंचाऊं।",
            translationUr = "اے اللہ! غیب اور حاضر کے جاننے والے، آسمانوں اور زمین کے پیدا کرنے والے، ہر چیز کے رب اور مالک! میں گواہی دیتا ہوں کہ تیرے سوا کوئی معبود برحق نہیں۔ میں تیری پناہ مانگتا ہوں اپنے نفس کے شر سے، شیطان کے شر اور اس کے شرک (جال) سے، اور اس بات سے کہ میں اپنی جان پر کوئی برائی کروں یا کسی مسلمان کی طرف برائی کھینچ لاؤں۔",
            translationAr = "O Allah, Knower of the unseen and the seen, Creator of the heavens and the earth, Lord and Sovereign of all things. I bear witness that there is no deity except You. I seek refuge in You from the evil of my own self, from the evil of Satan and his traps, and from committing any wrong against myself or bringing harm upon any Muslim.",
            referenceEn = "Jami` at-Tirmidhi 3529, Abu Dawud 5067",
            referenceHi = "तिर्मिज़ी 3529, अबी दाऊद 5067",
            referenceUr = "جامع ترمذی 3529، ابی داؤد 5067",
            referenceAr = "الترمذي: 3529",
            benefitEn = "Taught by the Prophet ﷺ to Abu Bakr (RA) to recite in the morning, evening, and before sleeping.",
            benefitHi = "नबी करीम ﷺ ने हज़रत अबू बकर (रज़ि.) को खास तौर पर सुबह, शाम और सोते वक़्त पढ़ने की तालीम दी।",
            benefitUr = "رسول اللہ ﷺ نے حضرت ابوبکر صدیق (رضی اللہ عنہ) کو صبح، شام اور سوتے وقت پڑھنے کے لیے سکھائی۔",
            hadithBook = "Jami` at-Tirmidhi 3529",
            hadithGrade = "Hasan Sahih",
            hadithNarrator = "Abu Bakr as-Siddiq (RA)",
            hadithArabic = "قُلِ: اللَّهُمَّ عَالِمَ الْغَيْبِ وَالشَّهَادَةِ... قُلْهَا إِذَا أَصْبَحْتَ وَإِذَا أَمْسَيْتَ وَإِذَا أَخَذْتَ مَضْجَعَكَ",
            hadithTranslationEn = "The Prophet ﷺ said: 'Say this in the morning, in the evening, and when you go to bed.'",
            hadithTranslationHi = "नबी ﷺ ने फरमाया: 'इसे सुबह, शाम और जब अपने बिस्तर पर जाओ तब कहा करो।'",
            hadithTranslationUr = "",
            hadithUrl = "https://sunnah.com/tirmidhi:3529"
        ),
        AdhkarItem(
            id = "e_bismillah_ladhi",
            order = 15,
            targetCount = 3,
            titleEn = "Bismillahil-ladhi la yadurru (3x)",
            titleHi = "बिस्मिल्लाहिल्लज़ी ला यदुर्रु (3 बार)",
            titleUr = "بسم اللہ الذی لا یضر (3 مرتبہ)",
            titleAr = "بسم الله الذي لا يضر (3 مرات)",
            arabicText = "بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الْأَرْضِ وَلَا فِي السَّمَاءِ، وَهُوَ السَّمِيعُ الْعَلِيمُ",
            transliterationEn = "Bismillahil-ladhi la yadurru ma'as-mihi shay'un fil-ardi wa la fis-sama'i wa Huwas-Sami'ul-'Alim.",
            transliterationHi = "बिस्मिल्लाहिल्लज़ी ला यदुर्रु म'अस्मिही शैउन फ़िल-अर्दि वला फ़िस-समाइ व हुवस-समी'उल-'अलीम।",
            translationEn = "In the name of Allah, with whose name nothing can cause harm on earth or in heaven, and He is the All-Hearing, the All-Knowing.",
            translationHi = "अल्लाह के नाम के साथ, जिसके नाम की बरकत से ज़मीन और आसमान की कोई चीज़ नुकसान नहीं पहुंचा सकती, और वही सब कुछ सुनने वाला, सब कुछ जानने वाला है।",
            translationUr = "اللہ کے نام کے ساتھ، جس کے نام کی برکت سے زمین اور آسمان کی کوئی چیز نقصان نہیں پہنچا سکتی، اور وہی خوب سننے والا، سب کچھ جاننے والا ہے۔",
            translationAr = "In the name of Allah, with whose name nothing can cause harm on earth or in heaven, and He is the All-Hearing, the All-Knowing.",
            referenceEn = "Jami` at-Tirmidhi 3388, Abu Dawud 5088",
            referenceHi = "तिर्मिज़ी 3388, अबी दाऊद 5088",
            referenceUr = "جامع ترمذی 3388، ابی داؤد 5088",
            referenceAr = "الترمذي: 3388",
            benefitEn = "Reciting 3 times in the evening shields from sudden harm, poison, snake/scorpion bites, and sickness until morning.",
            benefitHi = "शाम को 3 बार पढ़ने वाले को सुबह तक कोई अचानक आफ़त, ज़हरीला जानवर या बीमारी नुकसान नहीं पहुंचा सकती।",
            benefitUr = "شام کے وقت 3 مرتبہ پڑھنے والے کو صبح تک کوئی ناگہانی مصیبت، زہریلا جانور یا بیماری نقصان نہیں پہنچا سکتی۔",
            hadithBook = "Jami` at-Tirmidhi 3388",
            hadithGrade = "Hasan Sahih",
            hadithNarrator = "Uthman ibn Affan (RA)",
            hadithArabic = "مَا مِنْ عَبْدٍ يَقُولُ فِي مَسَاءِ كُلِّ لَيْلَةٍ: بِسْمِ اللَّهِ الَّذِي لاَ يَضُرُّ مَعَ اسْمِهِ شَيْءٌ... إِلاَّ لَمْ يَضُرَّهُ شَيْءٌ",
            hadithTranslationEn = "The Prophet ﷺ said: 'There is no servant who recites 3 times every evening: Bismillahil-ladhi la yadurru... except that nothing will harm him.'",
            hadithTranslationHi = "रसूलुल्लाह ﷺ ने फरमाया: 'जो बन्दा हर शाम 3 बार यह दुआ पढ़े, उसे कोई चीज़ नुकसान नहीं पहुंचा सकती।'",
            hadithTranslationUr = "",
            hadithUrl = "https://sunnah.com/tirmidhi:3388"
        ),
        AdhkarItem(
            id = "e_raditu_billah",
            order = 16,
            targetCount = 3,
            titleEn = "Raditu Billahi Rabban (3x)",
            titleHi = "रदीतु बिल्लाहि रब्बन (3 बार)",
            titleUr = "رضیت باللہ ربا (3 مرتبہ)",
            titleAr = "رضيت بالله ربا (3 مرات)",
            arabicText = "رَضِيتُ بِاللَّهِ رَبًّا، وَبِالْإِسْلَامِ دِينًا، وَبِمُحَمَّدٍ ﷺ نَبِيًّا",
            transliterationEn = "Raditu Billahi Rabban, wa bil-Islami dinan, wa bi-Muhammadin sallAllahu 'alayhi wa sallama Nabiyya.",
            transliterationHi = "रदीतु बिल्लाहि रब्बन, व बिल-इस्लामि दीनन, व बि-मुहम्मदिन सल्लल्लाहु अलैहि वसल्लम नबिय्या।",
            translationEn = "I am pleased with Allah as my Lord, with Islam as my religion, and with Muhammad (peace and blessings of Allah be upon him) as my Prophet.",
            translationHi = "मैं अल्लाह के रब होने पर, इस्लाम के दीन होने पर, और हज़रत मुहम्मद ﷺ के नबी होने पर राज़ी और खुश हुआ।",
            translationUr = "میں راضی ہوا اللہ کے رب ہونے پر، اسلام کے دین ہونے پر، اور حضرت محمد ﷺ کے نبی ہونے پر۔",
            translationAr = "I am pleased with Allah as my Lord, with Islam as my religion, and with Muhammad (peace and blessings of Allah be upon him) as my Prophet.",
            referenceEn = "Jami` at-Tirmidhi 3389, Abu Dawud 5072",
            referenceHi = "तिर्मिज़ी 3389, अबी दाऊद 5072",
            referenceUr = "جامع ترمذی 3389، ابی داؤد 5072",
            referenceAr = "الترمذي: 3389",
            benefitEn = "Allah takes it upon Himself to please whoever recites this 3 times in the evening on Judgment Day.",
            benefitHi = "जो शख्स शाम को 3 बार यह पढ़े, अल्लाह तआला पर यह हक हो जाता है कि वह क़यामत के दिन उसे राज़ी फरमाए।",
            benefitUr = "جو شخص شام کو 3 مرتبہ یہ کلمات کہے، اللہ تعالیٰ پر لازم ہو جاتا ہے کہ قیامت کے دن اسے راضی اور خوش فرمائے۔",
            hadithBook = "Jami` at-Tirmidhi 3389",
            hadithGrade = "Hasan Sahih",
            hadithNarrator = "Thawban (RA)",
            hadithArabic = "مَنْ قَالَ حِينَ يُمْسِي... رَضِيتُ بِاللَّهِ رَبًّا وَبِالإِسْلاَمِ دِينًا وَبِمُحَمَّدٍ نَبِيًّا، كَانَ حَقًّا عَلَى اللَّهِ أَنْ يُرْضِيَهُ",
            hadithTranslationEn = "The Messenger of Allah ﷺ said: 'Whoever says in the evening: Raditu Billahi Rabban... it is a right upon Allah to please him.'",
            hadithTranslationHi = "रसूलुल्लाह ﷺ ने फरमाया: 'जो शख्स शाम को यह कहे तो अल्लाह पर हक हो जाता है कि वह उसे खुश कर दे।'",
            hadithTranslationUr = "",
            hadithUrl = "https://sunnah.com/tirmidhi:3389"
        ),
        AdhkarItem(
            id = "e_ya_hayyu",
            order = 17,
            targetCount = 1,
            titleEn = "Ya Hayyu Ya Qayyum",
            titleHi = "या हय्यु या क़य्यूम",
            titleUr = "یا حی یا قیوم",
            titleAr = "يا حي يا قيوم",
            arabicText = "يَا حَيُّ يَا قَيُّومُ، بِرَحْمَتِكَ أَسْتَغِيثُ، أَصْلِحْ لِي شَأْنِي كُلَّهُ، وَلَا تَكِلْنِي إِلَىٰ نَفْسِي طَرْفَةَ عَيْنٍ",
            transliterationEn = "Ya Hayyu Ya Qayyum, bi-rahmatika astaghith, aslih li sha'ni kullahu, wa la takilni ila nafsi tarfata 'ayn.",
            transliterationHi = "या हय्यु या क़य्यूम, बि-रह्मतिका अस्तग़ीस, अस्लिह ली शअनी कुल्लहू, वला तकिल्नी इला नफ़्सी तर्फ़ता 'अैन।",
            translationEn = "O Ever-Living, O Self-Sustaining, by Your mercy I seek assistance. Rectify for me all of my affairs, and do not leave me in charge of myself even for the blink of an eye.",
            translationHi = "ऐ हमेशा ज़िंदा रहने वाले! ऐ सब को थामने वाले! तेरी रहमत के वसीले से मैं मदद मांगता हूं, मेरे तमाम मामलात को संवार दे, और मुझे पलक झपकने जितनी देर के लिए भी मेरे नफ़्स के हवाले न कर।",
            translationUr = "اے ہمیشہ زندہ رہنے والے! اے قائم بالذات! تیری رحمت کے وسیلے سے میں مدد مانگتا ہوں، میرے تمام کام درست فرما دے، اور مجھے آنکھ جھپکنے کی دیر بھی میرے نفس کے حوالے نہ فرما۔",
            translationAr = "O Ever-Living, O Self-Sustaining, by Your mercy I seek assistance. Rectify for me all of my affairs, and do not leave me in charge of myself even for the blink of an eye.",
            referenceEn = "Sunan an-Nasa'i 10330, Al-Hakim 1/545",
            referenceHi = "सुनन अन-नसाई 10330, अल-हाकिम 1/545",
            referenceUr = "سنن نسائی 10330، الحاکم 1/545",
            referenceAr = "النسائي: 10330",
            benefitEn = "Prophetic advice to Fatimah (RA) to never leave in the evening for divine guidance in every matter.",
            benefitHi = "रसूलुल्लाह ﷺ ने हज़रत फ़ातिमा (रज़ि.) को ताकीद फरमाई कि शाम को इस दुआ को कभी न छोड़ें।",
            benefitUr = "نبی اکرم ﷺ کی حضرت فاطمہ (رضی اللہ عنہا) کو شام کے وقت کبھی نہ چھوڑنے کی وصیت۔",
            hadithBook = "Sunan an-Nasa'i (Al-Kubra 10330)",
            hadithGrade = "Hasan",
            hadithNarrator = "Anas ibn Malik (RA)",
            hadithArabic = "أَنْ تَقُولِي إِذَا أَصْبَحْتِ وَإِذَا أَمْسَيْتِ: يَا حَيُّ يَا قَيُّومُ بِرَحْمَتِكَ أَسْتَغِيثُ...",
            hadithTranslationEn = "The Prophet ﷺ instructed Fatimah to say this every morning and evening.",
            hadithTranslationHi = "नबी करीम ﷺ ने हज़रत फ़ातिमा को सुबह और शाम यह पढ़ने की हिदायत दी।",
            hadithTranslationUr = "",
            hadithUrl = "https://sunnah.com/tirmidhi:3524"
        ),
        AdhkarItem(
            id = "e_amsayna_rabbil_alamin",
            order = 18,
            targetCount = 1,
            titleEn = "Amsayna wa Amsal-Mulku lillahi Rabbil-Alamin",
            titleHi = "अमसैना व अमसल-मुल्कु लिल्लाहि रब्बिल-आलमीन",
            titleUr = "امسینا و امسی الملک للہ رب العالمین",
            titleAr = "أمسينا وأمسى الملك لله رب العالمين",
            arabicText = "أَمْسَيْنَا وَأَمْسَى الْمُلْكُ لِلَّهِ رَبِّ الْعَالَمِينَ، اللَّهُمَّ إِنِّي أَسْأَلُكَ خَيْرَ هَٰذِهِ اللَّيْلَةِ: فَتْحَهَا، وَنَصْرَهَا، وَنُورَهَا، وَبَرَكَتَهَا، وَهُدَاهَا، وَأَعُوذُ بِكَ مِنْ شَرِّ مَا فِيهَا وَشَرِّ مَا بَعْدَهَا",
            transliterationEn = "Amsayna wa amsal-mulku lillahi Rabbil-'alamin. Allahumma inni as'aluka khayra hadhihil-laylati: fat-haha, wa nasraha, wa nuraha, wa barakataha, wa hudaha, wa a'udhu bika min sharri ma fiha wa sharri ma ba'daha.",
            transliterationHi = "अमसैना व अमसल-मुल्कु लिल्लाहि रब्बिल-'आलमीन। अल्लाहुम्मा इन्नी असअलोका ख़ैरा हाज़िहिल-लैलह: फ़त-हहा, व नस्रहा, व नूरहा, व बरकतहा, व हुदाहा, व अऊज़ु बिका मिन शर्रि मा फ़ीहा व शर्रि मा ब'दहा।",
            translationEn = "We have reached the evening and the sovereignty belongs to Allah, Lord of the worlds. O Allah, I ask You for the good of this night: its victory, its triumph, its light, its blessing, and its guidance; and I seek refuge in You from the evil in it and the evil of what follows it.",
            translationHi = "हमने शाम की और अल्लाह के तमाम मुल्क ने शाम की जो तमाम जहानों का रब है। ऐ अल्लाह! मैं तुझसे इस रात की भलाई का सवाल करता हूं: इसकी कामयाबी, इसकी मदद, इसका नूर, इसकी बरकत और इसकी हिदायत; और मैं तेरी पनाह मांगता हूं उस बुराई से जो इसमें है और उस बुराई से जो इसके बाद है।",
            translationUr = "ہم نے شام کی اور تمام جہانوں کے رب اللہ کے ملک نے شام کی۔ اے اللہ! میں تجھ سے اس رات کی خیر کا سوال کرتا ہوں: اس کی فتح، اس کی نصرت، اس کا نور، اس کی برکت اور اس کی ہدایت؛ اور میں تیری پناہ مانگتا ہوں اس برائی سے جو اس میں ہے اور جو اس کے بعد ہے۔",
            translationAr = "We have reached the evening and the sovereignty belongs to Allah, Lord of the worlds. O Allah, I ask You for the good of this night: its victory, its triumph, its light, its blessing, and its guidance; and I seek refuge in You from the evil in it and the evil of what follows it.",
            referenceEn = "Sunan Abi Dawud 5084, Ibn as-Sunni",
            referenceHi = "सुनन अबी दाऊद 5084, इब्न अस्सुन्नी",
            referenceUr = "سنن ابی داؤد 5084، ابن السنی",
            referenceAr = "سنن أبي داود: 5084",
            benefitEn = "Brings divine victory, spiritual light, immense blessings, and protection throughout the night.",
            benefitHi = "रात में रब्बानी मदद, रूहानी नूर, बरकत और हिदायत हासिल करने की बेहतरीन दुआ।",
            benefitUr = "رات کے وقت غیبی مدد، روحانی نور، برکت اور ہر شر سے امان حاصل کرنے کی دعا۔",
            hadithBook = "Sunan Abi Dawud 5084",
            hadithGrade = "Hasan",
            hadithNarrator = "Abu Malik al-Ash'ari (RA)",
            hadithArabic = "إِذَا أَمْسَى فَلْيَقُلْ: أَمْسَيْنَا وَأَمْسَى الْمُلْكُ لِلَّهِ رَبِّ الْعَالَمِينَ، اللَّهُمَّ إِنِّي أَسْأَلُكَ خَيْرَ هَذِهِ اللَّيْلَةِ...",
            hadithTranslationEn = "The Prophet ﷺ said: 'When you reach the evening, you should say: Amsayna wa amsal-mulku lillahi Rabbil-'alamin...'",
            hadithTranslationHi = "रसूलुल्लाह ﷺ ने फरमाया: 'जब तुम शाम करो तो यह कहा करो: अमसैना व अमसल-मुल्कु लिल्लाहि रब्बिल-आलमीन...'",
            hadithTranslationUr = "",
            hadithUrl = "https://sunnah.com/abudawud:5084"
        ),
        AdhkarItem(
            id = "e_fitratil_islam",
            order = 19,
            targetCount = 1,
            titleEn = "Amsayna 'ala Fitratil-Islam",
            titleHi = "अमसैना अला फ़ितरतिल-इस्लाम",
            titleUr = "امسینا علی فطرۃ الاسلام",
            titleAr = "أمسينا على فطرة الإسلام",
            arabicText = "أَمْسَيْنَا عَلَىٰ فِطْرَةِ الْإِسْلَامِ، وَعَلَىٰ كَلِمَةِ الْإِخْلَاصِ، وَعَلَىٰ دِينِ نَبِيِّنَا مُحَمَّدٍ ﷺ، وَعَلَىٰ مِلَّةِ أَبِينَا إِبْرَاهِيمَ حَنِيفًا مُسْلِمًا وَمَا كَانَ مِنَ الْمُشْرِكِينَ",
            transliterationEn = "Amsayna 'ala fitratil-Islam, wa 'ala kalimatil-ikhlas, wa 'ala dini Nabiyyina Muhammadin sallAllahu 'alayhi wa sallam, wa 'ala millati abina Ibrahima hanifan Muslimaw-wa ma kana minal-mushrikin.",
            transliterationHi = "अमसैना 'अला फ़ितरतिल-इस्लाम, व 'अला कलिमतिल-इख़्लास, व 'अला दीनि नबिय्यिना मुहम्मदिन सल्लल्लाहु अलैहि वसल्लम, व 'अला मिल्लति अबीना इब्राहीमा हनीफ़म-मुस्लिमांव वमा काना मिनल-मुशरिकीन।",
            translationEn = "We have reached the evening upon the natural fitrah of Islam, upon the word of pure sincerity, upon the religion of our Prophet Muhammad (ﷺ), and upon the faith of our father Ibrahim, upright and submitting to Allah, and he was not of the polytheists.",
            translationHi = "हमने इस्लाम की फ़ितरत पर, इख़्लास के कलिमे (तौहीद) पर, अपने नबी हज़रत मुहम्मद ﷺ के दीन पर, और अपने बाप हज़रत इब्राहीम (अलै.) के तरीक़े पर शाम की, जो एक तरफ़ा मुसलमान थे और शिर्क करने वालों में से नहीं थे।",
            translationUr = "ہم نے اسلام کی فطرت پر، اخلاص کے کلمے پر، اپنے نبی حضرت محمد ﷺ کے دین پر، اور اپنے باپ حضرت ابراہیم (علیہ السلام) کی ملت پر شام کی جو یکسو مسلمان تھے اور مشرکین میں سے نہ تھے۔",
            translationAr = "We have reached the evening upon the natural fitrah of Islam, upon the word of pure sincerity, upon the religion of our Prophet Muhammad (ﷺ), and upon the faith of our father Ibrahim, upright and submitting to Allah, and he was not of the polytheists.",
            referenceEn = "Musnad Ahmad 15367, Sunan ad-Darimi 2689",
            referenceHi = "मुसनद अहमद 15367, सुनन अद-दारिमी 2689",
            referenceUr = "مسند احمد 15367، سنن دارمی 2689",
            referenceAr = "مسند أحمد: 15367",
            benefitEn = "Re-establishes firm steadfastness upon Islamic monotheism, the Sunnah, and Millat-e-Ibrahim.",
            benefitHi = "तौहीद, सुन्नत-ए-नबवी और मिल्लत-ए-इब्राहीम पर साबित-क़दमी और ईमान की ताज़गी की दुआ।",
            benefitUr = "توحید، سنت نبوی اور ملت ابراہیمی پر استقامت اور ایمان کی تجدید کی عظیم دعا۔",
            hadithBook = "Musnad Ahmad 15367, Sunan ad-Darimi 2689",
            hadithGrade = "Sahih",
            hadithNarrator = "Abd al-Rahman ibn Abza (RA)",
            hadithArabic = "كَانَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ إِذَا أَمْسَى قَالَ: أَمْسَيْنَا عَلَى فِطْرَةِ الْإِسْلَامِ...",
            hadithTranslationEn = "The Messenger of Allah ﷺ used to say in the evening: Amsayna 'ala fitratil-Islam...",
            hadithTranslationHi = "रसूलुल्लाह ﷺ जब शाम करते तो यह दुआ फरमाते थे।",
            hadithTranslationUr = "",
            hadithUrl = "https://sunnah.com/ahmad:15367"
        ),
        AdhkarItem(
            id = "e_la_ilaha_illa_allah",
            order = 20,
            targetCount = 10,
            titleEn = "La ilaha illAllahu wahdahu la sharika lah (10x)",
            titleHi = "तौहीद का अज़ीम कलिमा (10 बार)",
            titleUr = "کلمہ توحید (10 مرتبہ)",
            titleAr = "التهليل (10 مرات)",
            arabicText = "لَا إِلَٰهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ، وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ",
            transliterationEn = "La ilaha illAllahu wahdahu la sharika lah, lahul-mulku wa lahul-hamd, wa Huwa 'ala kulli shay'in qadir.",
            transliterationHi = "ला इलाहा इल्लल्लाहु वहदहू ला शरीका लह, लहुल-मुल्कु व लहुल-हम्द, व हुवा 'अला कुल्लि शैइन क़दीर।",
            translationEn = "There is no deity except Allah alone, without partner; to Him belongs all sovereignty and to Him belongs all praise, and He is over all things competent.",
            translationHi = "अल्लाह के सिवा कोई सच्चा माबूद नहीं, वह अकेला है, उसका कोई शरीक नहीं, उसी की बादशाही है और उसी के लिए तमाम तारीफ़ है, और वह हर चीज़ पर पूरी तरह क़ादिर है।",
            translationUr = "اللہ کے سوا کوئی معبود نہیں، وہ اکیلا ہے، اس کا کوئی شریک نہیں، اسی کی بادشاہی ہے اور اسی کے لیے تمام تعریف ہے، اور وہ ہر چیز پر قادر ہے۔",
            translationAr = "There is no deity except Allah alone, without partner; to Him belongs all sovereignty and to Him belongs all praise, and He is over all things competent.",
            referenceEn = "Sahih al-Bukhari 6403, Sahih Muslim 2691, Abu Dawud 5077",
            referenceHi = "सहीह बुख़ारी 6403, सहीह मुस्लिम 2691",
            referenceUr = "صحیح بخاری 6403، صحیح مسلم 2691",
            referenceAr = "صحيح البخاري: 6403، صحيح مسلم: 2691",
            benefitEn = "Reciting 10 times in the evening equals freeing 4 slaves, writes 100 good deeds, erases 100 sins, and protects from Satan until morning.",
            benefitHi = "शाम को 10 बार पढ़ने से 4 गुलाम आज़ाद करने का सवाब, 100 नेकियां, 100 गुनाहों की माफ़ी और सुबह तक शैतान से हिफ़ाज़त मिलती है।",
            benefitUr = "شام کو 10 مرتبہ پڑھنے سے 4 غلام آزاد کرنے کا ثواب، 100 نیکیاں، 100 گناہوں کی معافی اور صبح تک شیطان سے پناہ ملتی ہے۔",
            hadithBook = "Sahih Muslim 2691, Sahih al-Bukhari 6403",
            hadithGrade = "Sahih",
            hadithNarrator = "Abu Ayyub al-Ansari / Abu Hurairah (RA)",
            hadithArabic = "مَنْ قَالَ: لَا إِلَهَ إِلَّا اللهُ وَحْدَهُ لَا شَرِيكَ لَهُ... عَشْرَ مِرَارٍ، كَانَ كَمَنْ أَعْتَقَ أَرْبَعَةَ أَنْفُسٍ مِنْ وَلَدِ إِسْمَاعِيلَ",
            hadithTranslationEn = "The Messenger of Allah ﷺ said: 'Whoever says it 10 times, it is as if he freed four slaves from the children of Ismail.'",
            hadithTranslationHi = "रसूलुल्लाह ﷺ ने फरमाया: 'जिसने इसे 10 बार कहा, वह ऐसा है जैसे उसने औलादे इस्माईल से चार जानें आज़ाद की हों।'",
            hadithTranslationUr = "",
            hadithUrl = "https://sunnah.com/muslim:2691"
        ),
        AdhkarItem(
            id = "e_subhanallah_100",
            order = 21,
            targetCount = 100,
            titleEn = "SubhanAllahi wa bihamdihi (100x)",
            titleHi = "सुब्हानल्लाहि व बिहम्दिही (100 बार)",
            titleUr = "سبحان اللہ وبحمدہ (100 مرتبہ)",
            titleAr = "سبحان الله وبحمده (100 مرة)",
            arabicText = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ",
            transliterationEn = "SubhanAllahi wa bihamdih.",
            transliterationHi = "सुब्हानल्लाहि व बिहम्दिही।",
            translationEn = "Glory is to Allah and all praise is to Him.",
            translationHi = "अल्लाह पाक है अपनी तमाम तारीफ़ों के साथ।",
            translationUr = "اللہ پاک ہے اپنی تمام تر تعریفوں کے ساتھ۔",
            translationAr = "Glory is to Allah and all praise is to Him.",
            referenceEn = "Sahih Muslim 2692, Sahih al-Bukhari 6405",
            referenceHi = "सहीह मुस्लिम 2692, सहीह बुख़ारी 6405",
            referenceUr = "صحیح مسلم 2692، صحیح بخاری 6405",
            referenceAr = "صحيح مسلم: 2692",
            benefitEn = "Whoever says this 100 times in the evening, no one will bring a better deed on the Day of Judgment except one who said the same or more.",
            benefitHi = "जो शख्स शाम को 100 बार इसे पढ़ेगा, क़यामत के दिन उससे अफ़ज़ल अमल लेकर कोई नहीं आएगा सिवाय उसके जिसने इतना या इससे ज़्यादा पढ़ा हो।",
            benefitUr = "جو شخص شام کو 100 مرتبہ یہ پڑھے گا، قیامت کے دن اس سے افضل عمل لے کر کوئی نہیں آئے گا سوائے اس کے جس نے اس جتنا یا اس سے زیادہ پڑھا ہو۔",
            hadithBook = "Sahih Muslim 2692",
            hadithGrade = "Sahih",
            hadithNarrator = "Abu Hurairah (RA)",
            hadithArabic = "مَنْ قَالَ حِينَ يُصْبِحُ وَحِينَ يُمْسِي: سُبْحَانَ اللهِ وَبِحَمْدِهِ مِائَةَ مَرَّةٍ، لَمْ يَأْتِ أَحَدٌ يَوْمَ الْقِيَامَةِ بِأَفْضَلَ مِمَّا جَاءَ بِهِ...",
            hadithTranslationEn = "The Messenger of Allah ﷺ said: 'Whoever says in the morning and evening 100 times: SubhanAllahi wa bihamdihi, no one will bring anything better on the Day of Resurrection...'",
            hadithTranslationHi = "रसूलुल्लाह ﷺ ने फरमाया: 'जो शख्स सुबह और शाम 100 बार सुब्हानल्लाहि व बिहम्दिही कहेगा, क़यामत के दिन उससे बेहतर कोई अमल लेकर नहीं आएगा।'",
            hadithTranslationUr = "",
            hadithUrl = "https://sunnah.com/muslim:2692"
        ),
        AdhkarItem(
            id = "e_audhu_bikalimatillah",
            order = 22,
            targetCount = 3,
            titleEn = "A'udhu bi-kalimatil-lahit-tammati (3x)",
            titleHi = "अऊज़ु बि-कलिमातिल्लाहित-ताम्माति (3 बार)",
            titleUr = "اعوذ بکلمات اللہ التامات (3 مرتبہ)",
            titleAr = "أعوذ بكلمات الله التامات (3 مرات)",
            arabicText = "أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّاتِ مِنْ شَرِّ مَا خَلَقَ",
            transliterationEn = "A'udhu bi-kalimatil-lahit-tammati min sharri ma khalaq.",
            transliterationHi = "अऊज़ु बि-कलिमातिल्लाहित-ताम्माति मिन शर्री मा ख़लक़।",
            translationEn = "I seek refuge in the perfect words of Allah from the evil of that which He has created.",
            translationHi = "मैं अल्लाह के मुकम्मल कलिमात की पनाह मांगता हूं हर उस चीज़ की बुराई से जो उसने पैदा की है।",
            translationUr = "میں اللہ کے مکمل کلمات کی پناہ مانگتا ہوں ہر اس چیز کے شر سے جو اس نے پیدا فرمائی۔",
            translationAr = "I seek refuge in the perfect words of Allah from the evil of that which He has created.",
            referenceEn = "Sahih Muslim 2708, Tirmidhi 3604",
            referenceHi = "सहीह मुस्लिम 2708, तिर्मिज़ी 3604",
            referenceUr = "صحیح مسلم 2708، جامع ترمذی 3604",
            referenceAr = "صحيح مسلم: 2708",
            benefitEn = "Whoever recites this 3 times in the evening will not be harmed by poisonous stings (scorpions/snakes) or fever that night.",
            benefitHi = "जो शख्स शाम को 3 बार इसे पढ़े, उस रात उसे कोई बिच्छू, सांप का डंक या ज़हरीला कीड़ा नुकसान नहीं पहुंचा सकता।",
            benefitUr = "جو شخص شام کو 3 مرتبہ یہ پڑھے، اس رات اسے بچھو یا سانپ کا ڈنک یا کوئی زہریلا کیڑا نقصان نہیں پہنچا سکتا۔",
            hadithBook = "Sahih Muslim 2708, Jami` at-Tirmidhi 3604",
            hadithGrade = "Sahih",
            hadithNarrator = "Abu Hurairah (RA)",
            hadithArabic = "مَنْ قَالَ حِينَ يُمْسِي ثَلَاثَ مَرَّاتٍ: أَعُوذُ بِكَلِمَاتِ اللهِ التَّامَّاتِ مِنْ شَرِّ مَا خَلَقَ، لَمْ تَضُرَّهُ حُمَةٌ تِلْكَ اللَّيْلَةَ",
            hadithTranslationEn = "The Messenger of Allah ﷺ said: 'Whoever says three times in the evening: A'udhu bi-kalimatil-lahit-tammati min sharri ma khalaq, no poisonous sting will harm him that night.'",
            hadithTranslationHi = "रसूलुल्लाह ﷺ ने फरमाया: 'जिसने शाम के वक़्त 3 बार यह कहा, उस रात उसे कोई ज़हरीला डंक नुकसान नहीं पहुंचाएगा।'",
            hadithTranslationUr = "",
            hadithUrl = "https://sunnah.com/muslim:2708"
        ),
        AdhkarItem(
            id = "e_durood_sharif",
            order = 23,
            targetCount = 10,
            titleEn = "Durood Sharif (Salawat 10x)",
            titleHi = "दुरूद शरीफ़ (10 बार)",
            titleUr = "درود شریف (10 مرتبہ)",
            titleAr = "الصلاة على النبي ﷺ (10 مرات)",
            arabicText = "اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلَىٰ إِبْرَاهِيمَ وَعَلَىٰ آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ، اللَّهُمَّ بَارِكْ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ كَمَا بَارَكْتَ عَلَىٰ إِبْرَاهِيمَ وَعَلَىٰ آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ",
            transliterationEn = "Allahumma salli 'ala Muhammadin wa 'ala ali Muhammadin kama sallayta 'ala Ibrahima wa 'ala ali Ibrahima innaka Hamidum-Majid. Allahumma barik 'ala Muhammadin wa 'ala ali Muhammadin kama barakta 'ala Ibrahima wa 'ala ali Ibrahima innaka Hamidum-Majid.",
            transliterationHi = "अल्लाहुम्मा सल्लि 'अला मुहम्मदिंव व 'अला आलि मुहम्मद, कमा सल्लैता 'अला इब्राहीमा व 'अला आलि इब्राहीम, इन्नका हमीदुम-मजीद। अल्लाहुम्मा बारिक 'अला मुहम्मदिंव व 'अला आलि मुहम्मद, कमा बारक्ता 'अला इब्राहीमा व 'अला आलि इब्राहीम, इन्नका हमीदुम-मजीद।",
            translationEn = "O Allah, send prayers upon Muhammad and upon the family of Muhammad as You sent prayers upon Ibrahim and upon the family of Ibrahim; indeed, You are Praiseworthy and Glorious. O Allah, send blessings upon Muhammad and upon the family of Muhammad as You sent blessings upon Ibrahim and upon the family of Ibrahim; indeed, You are Praiseworthy and Glorious.",
            translationHi = "ऐ अल्लाह! रहमत नाज़िल फरमा हज़रत मुहम्मद ﷺ पर और आपकी आल पर जैसा कि तूने रहमत नाज़िल फरमाई हज़रत इब्राहीम (अलै.) पर और उनकी आल पर, बेशक तू तारीफ़ के लायक और बुज़ुर्ग है। ऐ अल्लाह! बरकत नाज़िल फरमा हज़रत मुहम्मद ﷺ पर और आपकी आल पर जैसा कि तूने बरकत नाज़िल फरमाई हज़रत इब्राहीम (अलै.) पर और उनकी आल पर, बेशक तू तारीफ़ के लायक और बुज़ुर्ग है।",
            translationUr = "اے اللہ! رحمت نازل فرما حضرت محمد ﷺ پر اور آپ کی آل پر جیسا کہ تو نے رحمت نازل فرمائی حضرت ابراہیم (علیہ السلام) پر اور ان کی آل پر، بے شک تو تعریف کے لائق اور بزرگ ہے۔ اے اللہ! برکت نازل فرما حضرت محمد ﷺ پر اور آپ کی آل پر جیسا کہ تو نے برکت نازل فرمائی حضرت ابراہیم (علیہ السلام) پر اور ان کی آل پر، بے شک تو تعریف کے لائق اور بزرگ ہے۔",
            translationAr = "O Allah, send prayers upon Muhammad and upon the family of Muhammad as You sent prayers upon Ibrahim and upon the family of Ibrahim; indeed, You are Praiseworthy and Glorious. O Allah, send blessings upon Muhammad and upon the family of Muhammad as You sent blessings upon Ibrahim and upon the family of Ibrahim; indeed, You are Praiseworthy and Glorious.",
            referenceEn = "At-Tabarani (Majma' az-Zawa'id 10/120), Sahih at-Targhib 656",
            referenceHi = "अल-मुअजमुल कबीर, सहीह अत-तर्ग़ीब 656",
            referenceUr = "المعجم الکبیر طبرانی، صحیح الترغیب 656",
            referenceAr = "الطبراني في الكبير، صحيح الترغيب: 656",
            benefitEn = "Whoever sends blessings upon the Prophet ﷺ 10 times in the morning and 10 times in the evening will attain his intercession (Shafa'at) on the Day of Resurrection.",
            benefitHi = "जो शख्स सुबह 10 बार और शाम 10 बार नबी करीम ﷺ पर दुरूद भेजेगा, क़यामत के दिन उसे नबी ﷺ की शफ़ाअत हासिल होगी।",
            benefitUr = "جو شخص صبح 10 مرتبہ اور شام 10 مرتبہ نبی کریم ﷺ پر درود بھیجے گا، قیامت کے دن اسے آپ ﷺ کی شفاعت نصیب ہوگی۔",
            hadithBook = "Al-Mu'jam al-Kabir (At-Tabarani), Sahih at-Targhib 656",
            hadithGrade = "Hasan / Jayyid",
            hadithNarrator = "Abu ad-Darda (RA)",
            hadithArabic = "مَنْ صَلَّى عَلَيَّ حِينَ يُصْبِحُ عَشْرًا، وَحِينَ يُمْسِي عَشْرًا، أَدْرَكَتْهُ شَفَاعَتِي يَوْمَ الْقِيَامَةِ",
            hadithTranslationEn = "The Messenger of Allah ﷺ said: 'Whoever sends blessings upon me ten times in the morning and ten times in the evening will attain my intercession on the Day of Resurrection.'",
            hadithTranslationHi = "रसूलुल्लाह ﷺ ने फरमाया: 'जिसने सुबह 10 बार और शाम 10 बार मुझ पर दुरूद भेजा, क़यामत के दिन उसे मेरी शफ़ाअत हासिल होगी।'",
            hadithTranslationUr = "",
            hadithUrl = "https://sunnah.com/tirmidhi:484"
        ),
    )
}
