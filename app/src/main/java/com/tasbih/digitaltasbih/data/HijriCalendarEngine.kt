package com.tasbih.digitaltasbih.data

import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.floor

/**
 * Authentic Islamic Hijri Calendar conversion engine.
 * Supports:
 * 1. Verified South Asia Crescent Moon table (1447 AH to 1450 AH - 2025 to 2029+)
 * 2. High-precision Algorithmic Kuwaiti/Umm al-Qura calculation for universal lifetime calendar (any year from 1 to 9999 AD/AH)
 * 3. 24-language Hijri Month names matching the original app.
 */
object HijriCalendarEngine {

    data class MonthStartEntry(
        val gYear: Int,
        val gMonth: Int, // 1-12
        val gDay: Int,
        val hy: Int,
        val hm: Int
    ) {
        val timeInMillis: Long by lazy {
            val c = Calendar.getInstance().apply {
                set(Calendar.YEAR, gYear)
                set(Calendar.MONTH, gMonth - 1)
                set(Calendar.DAY_OF_MONTH, gDay)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            c.timeInMillis
        }
    }

    // 48 verified crescent sightings in South Asia (India, Pakistan, Bangladesh)
    // Directly extracted from index.html HIJRI_MONTH_STARTS_SA
    private val HIJRI_MONTH_STARTS_SA = listOf(
        // 1447 AH
        MonthStartEntry(2025, 6, 27, 1447, 1),
        MonthStartEntry(2025, 7, 27, 1447, 2),
        MonthStartEntry(2025, 8, 25, 1447, 3),
        MonthStartEntry(2025, 9, 24, 1447, 4),
        MonthStartEntry(2025, 10, 23, 1447, 5),
        MonthStartEntry(2025, 11, 22, 1447, 6),
        MonthStartEntry(2025, 12, 22, 1447, 7),
        MonthStartEntry(2026, 1, 21, 1447, 8),
        MonthStartEntry(2026, 2, 19, 1447, 9),
        MonthStartEntry(2026, 3, 21, 1447, 10),
        MonthStartEntry(2026, 4, 19, 1447, 11),
        MonthStartEntry(2026, 5, 19, 1447, 12),
        // 1448 AH
        MonthStartEntry(2026, 6, 17, 1448, 1),
        MonthStartEntry(2026, 7, 16, 1448, 2),
        MonthStartEntry(2026, 8, 15, 1448, 3),
        MonthStartEntry(2026, 9, 13, 1448, 4),
        MonthStartEntry(2026, 10, 14, 1448, 5),
        MonthStartEntry(2026, 11, 12, 1448, 6),
        MonthStartEntry(2026, 12, 12, 1448, 7),
        MonthStartEntry(2027, 1, 11, 1448, 8),
        MonthStartEntry(2027, 2, 9, 1448, 9),
        MonthStartEntry(2027, 3, 11, 1448, 10),
        MonthStartEntry(2027, 4, 9, 1448, 11),
        MonthStartEntry(2027, 5, 9, 1448, 12),
        // 1449 AH
        MonthStartEntry(2027, 6, 7, 1449, 1),
        MonthStartEntry(2027, 7, 7, 1449, 2),
        MonthStartEntry(2027, 8, 5, 1449, 3),
        MonthStartEntry(2027, 9, 4, 1449, 4),
        MonthStartEntry(2027, 10, 3, 1449, 5),
        MonthStartEntry(2027, 11, 2, 1449, 6),
        MonthStartEntry(2027, 12, 1, 1449, 7),
        MonthStartEntry(2027, 12, 31, 1449, 8),
        MonthStartEntry(2028, 1, 29, 1449, 9),
        MonthStartEntry(2028, 2, 28, 1449, 10),
        MonthStartEntry(2028, 3, 28, 1449, 11),
        MonthStartEntry(2028, 4, 27, 1449, 12),
        // 1450 AH
        MonthStartEntry(2028, 5, 26, 1450, 1),
        MonthStartEntry(2028, 6, 25, 1450, 2),
        MonthStartEntry(2028, 7, 24, 1450, 3),
        MonthStartEntry(2028, 8, 23, 1450, 4),
        MonthStartEntry(2028, 9, 21, 1450, 5),
        MonthStartEntry(2028, 10, 21, 1450, 6),
        MonthStartEntry(2028, 11, 19, 1450, 7),
        MonthStartEntry(2028, 12, 19, 1450, 8),
        MonthStartEntry(2029, 1, 17, 1450, 9),
        MonthStartEntry(2029, 2, 16, 1450, 10),
        MonthStartEntry(2029, 3, 17, 1450, 11),
        MonthStartEntry(2029, 4, 16, 1450, 12)
    )

    private val HIJRI_MONTH_NAMES = mapOf(
        "en" to listOf(
            "Muharramul Haram", "Safarul Muzaffar", "Rabi ul Awwal", "Rabi ul Aakhir",
            "Jamadi ul Awwal", "Jamadius Aakhir", "Rajabul Murajjab", "Shabanul Muazzam",
            "Ramzanul Mubarak", "Shawwalul Mukarram", "Zilqadah", "Zilhijjah"
        ),
        "hi" to listOf(
            "मोहर्रमुल हराम", "सफरुल मुजफ्फर", "रबीउल अव्वल", "रबीउल आखिर",
            "जमादिउल अव्वल", "जमादिउस आखिर", "रज्जबुल मुरज्जब", "शाबानुल मोजज्जम",
            "रमजानुल मुबारक", "शव्वालुल मुकर्रम", "जिलकादा", "जिलहिज्जा"
        ),
        "ur" to listOf(
            "محرم الحرام", "صفر المظفر", "ربیع الاول", "ربیع الآخر",
            "جمادی الاول", "جمادی الآخر", "رجب المرجب", "شعبان المعظم",
            "رمضان المبارک", "شوال المکرم", "ذوالقعدہ", "ذوالحجہ"
        ),
        "ar" to listOf(
            "محرم الحرام", "صفر المظفر", "ربيع الأول", "ربيع الآخر",
            "جمادى الأولى", "جمادى الآخرة", "رجب المرجب", "شعبان المعظم",
            "رمضان المبارك", "شوال المكرم", "ذو القعدة", "ذو الحجة"
        ),
        "bn" to listOf(
            "মহররমুল হারাম", "সফরুল মুজাফফর", "রবিউল আউয়াল", "রবিউল আখির",
            "জামাদিউল আউয়াল", "জামাদিউস আখির", "রজবুল মুরাজ্জাব", "শাবানুল মুআজ্জাম",
            "রমজানুল মুবারক", "শাওয়ালুল মুকাররম", "জিলকদ", "জিলহজ্জ"
        ),
        "ta" to listOf(
            "முஹர்ரம்", "ஸஃபர்", "ரபீவுல் அவ்வல்", "ரபீவுல் ஆகிர்",
            "ஜமாதில் அவ்வல்", "ஜமாதில் ஆகிர்", "ரஜப்", "ஷஃபான்",
            "ரமழான்", "ஷவ்வால்", "துல்கஃதா", "துல்ஹிஜ்ஜா"
        ),
        "te" to listOf(
            "ముహర్రం", "సఫర్", "రబీ అల్-అవ్వల్", "రబీ అల్-ఆఖిర్",
            "జమాదిల్ అవ్వల్", "జమాదిల్ ఆఖిర్", "రజబ్", "షాబాన్",
            "రమదాన్", "షవ్వాల్", "జుల్ కాదా", "జుల్ హిజ్జా"
        ),
        "ml" to listOf(
            "മുഹർറം", "സഫർ", "റബീഉൽ അവ്വൽ", "റബീഉൽ ആഖിർ",
            "ജമാദുൽ അവ്വൽ", "ജമാദുൽ ആഖിർ", "റജബ്", "ശഅ്ബാൻ",
            "റമദാൻ", "ശവ്വാൽ", "ദുൽ ക്വഅ്ദ", "ദുൽ ഹിജ്ജ"
        ),
        "gu" to listOf(
            "મોહર્રમ", "સફર", "રબી-અલ-અવ્વલ", "રબી-અલ-આખિર",
            "જમાદ-અલ-અવ્વલ", "જમાદ-અલ-આખિર", "રજ્જબ", "શાબાન",
            "રમઝાન", "શવ્વાલ", "ઝુ-અલ-કઅ્દ", "ઝુ-અલ-હિજ્જ"
        ),
        "mr" to listOf(
            "मोहर्रम", "सफर", "रबी अल-अव्वल", "रबी अल-आखिर",
            "जमाद अल-अव्वल", "जमाद अल-आखिर", "रजब", "शाबान",
            "रमजान", "शव्वाल", "झुल-कदह", "झुल-हिज्जह"
        ),
        "tr" to listOf(
            "Muharrem", "Safer", "Rebiülevvel", "Rebiülahir",
            "Cemaziyelevvel", "Cemaziyelahir", "Recep", "Şaban",
            "Ramazan", "Şevval", "Zilkade", "Zilhicce"
        ),
        "id" to listOf(
            "Muharram", "Safar", "Rabiul Awal", "Rabiul Akhir",
            "Jumadil Awal", "Jumadil Akhir", "Rajab", "Syaban",
            "Ramadhan", "Syawal", "Zulkaidah", "Zulhijjah"
        ),
        "ms" to listOf(
            "Muharam", "Safar", "Rabiulawal", "Rabiulakhir",
            "Jamadilawal", "Jamadilakhir", "Rejab", "Syaaban",
            "Ramadan", "Syawal", "Zulkaedah", "Zulhijjah"
        ),
        "fa" to listOf(
            "محرم", "صفر", "ربیع‌الاول", "ربیع‌الثانی",
            "جمادی‌الاول", "جمادی‌الثانی", "رجب", "شعبان",
            "رمضان", "شوال", "ذی‌القعده", "ذی‌الحجه"
        ),
        "fr" to listOf(
            "Mouharram", "Safar", "Rabi al-Awwal", "Rabi ath-Thani",
            "Joumada al-Oula", "Joumada ath-Thania", "Rajab", "Chaabane",
            "Ramadan", "Chawwal", "Dhou al-Qi'da", "Dhou al-Hijja"
        ),
        "de" to listOf(
            "Muharram", "Safar", "Rabi' al-Awwal", "Rabi' ath-Thani",
            "Dschumada al-Ula", "Dschumada ath-Thaniya", "Radschab", "Scha'ban",
            "Ramadan", "Schawwal", "Dhu l-qa'da", "Dhu l-Hiddscha"
        ),
        "ru" to listOf(
            "Мухаррам", "Сафар", "Раби аль-Авваль", "Раби ас-Сани",
            "Джумада аль-Уля", "Джумада ас-Сани", "Раджаб", "Шабан",
            "Рамадан", "Шавваль", "Зу-ль-Каада", "Зу-ль-Хиджа"
        ),
        "es" to listOf(
            "Muharram", "Safar", "Rabi al-Awwal", "Rabi al-Thani",
            "Yumada al-Wula", "Yumada al-Thania", "Rayab", "Shaabán",
            "Ramadán", "Shawwal", "Du al-Qada", "Du al-Hiyya"
        ),
        "zh" to listOf(
            "穆哈兰姆月", "色法尔月", "赖比尔·敖外鲁月", "赖比尔·阿色尼月",
            "主马达·敖外鲁月", "主马达·阿色尼月", "赖哲卜月", "舍尔邦月",
            "莱麦丹月", "闪瓦鲁月", "都尔喀尔德月", "都尔黑哲月"
        ),
        "pa" to listOf(
            "ਮੋਹਰ੍ਰਮ", "ਸਫ਼ਰ", "ਰਬੀ ਉਲ ਅੱਵਲ", "ਰਬੀ ਉਲ ਆਖ਼ਿਰ",
            "ਜਮਾਦੀ ਉਲ ਅੱਵਲ", "ਜਮਾਦੀ ਉਲ ਆਖ਼ਿਰ", "ਰਜਬ", "ਸ਼ਾਬਾਨ",
            "ਰਮਜ਼ਾਨ", "ਸ਼ੱਵਾਲ", "ਜ਼ਿਲਕਾਦਾ", "ਜ਼ਿਲਹਿਜ੍ਹਾ"
        ),
        "kn" to listOf(
            "ಮುಹರ್ರಮ್", "ಸಫರ್", "ರಬೀವುಲ್ ಅವ್ವಲ್", "ರಬೀವುಲ್ ಆಖಿರ್",
            "ಜಮಾದಿಲ್ ಅವ್ವಲ್", "ಜಮಾದಿಲ್ ಆಖಿರ್", "ರಜಬ್", "ಶಾಬಾನ್",
            "ರಮದಾನ್", "ಶವ್ವಾಲ್", "ಜುಲ್-ಖಾದ", "ಜುಲ್-ಹಿಜ್ಜಾ"
        ),
        "or" to listOf(
            "ମୋହରମ", "ସଫର", "ରବି ଉଲ ଅବ୍ବଲ", "ରବି ଉଲ ଆଖିର",
            "ଜମାଦି ଉଲ ଅବ୍ବଲ", "ଜମାଦି ଉଲ ଆଖିର", "ରଜବ", "ଶାବାନ",
            "ରମଜାନ", "ଶବ୍ବାଲ", "ଜିଲକାଦା", "ଜିଲହିଜ୍ଜା"
        ),
        "as" to listOf(
            "মহৰম", "চফৰ", "ৰবিউল আউৱাল", "ৰবিউল আখিৰ",
            "জামাদিউল আউৱাল", "জামাদিউল আখিৰ", "ৰজব", "শ্বাবান",
            "ৰমজান", "শ্বাৱাল", "জিলকদ", "জিলহজ্জ"
        ),
        "sw" to listOf(
            "Mfunguo Nne (Muharram)", "Mfunguo Tano (Safar)", "Mfunguo Sita (Rabiul Awwal)", "Mfunguo Saba (Rabiuth Thani)",
            "Mfunguo Nane (Jumadal Ula)", "Mfunguo Tisa (Jumadath Thani)", "Mfunguo Kumi (Rajab)", "Mfunguo Kumi na Moja (Shaaban)",
            "Ramadhani", "Mfunguo Mosi (Shawwal)", "Mfunguo Pili (Dhul Qadah)", "Mfunguo Tatu (Dhul Hijjah)"
        ),
        "so" to listOf(
            "Muxarram", "Safar", "Rabici al-Awwal", "Rabici al-Aakhir",
            "Jumaada al-Uula", "Jumaada al-Aakhira", "Rajab", "Shacbaan",
            "Ramadaan", "Shawwaal", "Dul-Qacda", "Dul-Xijja"
        ),
        "ha" to listOf(
            "Muharram", "Safar", "Rabi'ul Awwal", "Rabi'us Sani",
            "Jumadal Ula", "Jumadas Sani", "Rajab", "Sha'aban",
            "Ramadan", "Shawwal", "Zul-Qada", "Zul-Hijja"
        )
    )

    fun getHijriMonthName(monthNumber: Int, locale: String): String {
        val safeMonth = ((monthNumber - 1).coerceIn(0, 11))
        val list = HIJRI_MONTH_NAMES[locale] ?: HIJRI_MONTH_NAMES["en"] ?: return "Month $monthNumber"
        return list.getOrElse(safeMonth) { "Month $monthNumber" }
    }

    /**
     * Algorithmic Kuwaiti Astronomical Hijri calculation.
     * Ported directly from original app kuwaitiHijri(date) lines 337900-338070.
     * Provides lifetime conversion for past and future centuries.
     */
    fun kuwaitiHijri(year: Int, month: Int, day: Int): Triple<Int, Int, Int> { // Day, Month(1-12), Year
        var y = year
        var m = month // 1-12
        val d = day

        if (m < 3) {
            y -= 1
            m += 12
        }

        val a = floor(y / 100.0).toInt()
        var b = 2 - a + floor(a / 4.0).toInt()
        if (y < 1583) b = 0
        if (y == 1582) {
            if (m > 10) b = -10
            if (m == 10 && d > 4) b = -10
        }

        val jd = floor(365.25 * (y + 4716)).toLong() + floor(30.6001 * (m + 1)).toLong() + d + b - 1524
        val bjd = jd - 1948440 + 10632
        val n = floor((bjd - 1) / 10631.0).toLong()
        var remBjd = bjd - 10631 * n + 354
        val j = (floor((10985 - remBjd) / 5316.0) * floor((50 * remBjd) / 17719.0) +
                floor(remBjd / 5670.0) * floor((43 * remBjd) / 15238.0)).toLong()
        remBjd = remBjd - (floor((30 - j) / 15.0) * floor((17719 * j) / 50.0)).toLong() -
                (floor(j / 16.0) * floor((15238 * j) / 43.0)).toLong() + 29
        val hm = floor((24 * remBjd) / 709.0).toInt()
        val hd = (remBjd - floor((709 * hm) / 24.0)).toInt()
        val hy = (30 * n + j - 30).toInt()

        val cleanDay = hd.coerceIn(1, 30)
        val cleanMonth = hm.coerceIn(1, 12)
        return Triple(cleanDay, cleanMonth, hy)
    }

    private fun isSouthAsiaLocale(): Boolean {
        return try {
            val tz = TimeZone.getDefault().id.lowercase(Locale.ROOT)
            val country = Locale.getDefault().country.lowercase(Locale.ROOT)
            tz.contains("kolkata") || tz.contains("calcutta") || tz.contains("karachi") ||
                    tz.contains("dhaka") || tz.contains("india") || country == "in" || country == "pk" || country == "bd"
        } catch (_: Exception) {
            true // default to South Asia for primary target userbase
        }
    }

    /**
     * Get exact Hijri date for any Gregorian date.
     */
    fun getHijriDate(gYear: Int, gMonth: Int, gDay: Int, locale: String): HijriDate {
        val targetCal = Calendar.getInstance().apply {
            set(Calendar.YEAR, gYear)
            set(Calendar.MONTH, gMonth) // 0-based
            set(Calendar.DAY_OF_MONTH, gDay)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val targetTime = targetCal.timeInMillis

        // 1. Check verified South Asia moon sighting table if applicable
        if (isSouthAsiaLocale()) {
            val first = HIJRI_MONTH_STARTS_SA.first()
            val last = HIJRI_MONTH_STARTS_SA.last()
            val maxValidTime = last.timeInMillis + 30L * 86400000L

            if (targetTime >= first.timeInMillis && targetTime < maxValidTime) {
                for (i in HIJRI_MONTH_STARTS_SA.indices) {
                    val cur = HIJRI_MONTH_STARTS_SA[i]
                    val next = HIJRI_MONTH_STARTS_SA.getOrNull(i + 1)
                    if (targetTime >= cur.timeInMillis && (next == null || targetTime < next.timeInMillis)) {
                        val calcDay = ((targetTime - cur.timeInMillis) / 86400000L).toInt() + 1
                        if (calcDay in 1..30) {
                            return HijriDate(
                                day = calcDay,
                                monthNumber = cur.hm,
                                year = cur.hy,
                                monthName = getHijriMonthName(cur.hm, locale),
                                isSouthAsiaMoonVerified = true
                            )
                        }
                    }
                }
            }
        }

        // 2. Kuwaiti astronomical calculation (lifetime universal past & future)
        // With regional 1-day South Asia moon-sighting shift when in South Asia
        val evalCal = if (isSouthAsiaLocale()) {
            Calendar.getInstance().apply {
                timeInMillis = targetTime - 86400000L
            }
        } else targetCal

        val (calcDay, calcMonth, calcYear) = kuwaitiHijri(
            evalCal.get(Calendar.YEAR),
            evalCal.get(Calendar.MONTH) + 1,
            evalCal.get(Calendar.DAY_OF_MONTH)
        )

        return HijriDate(
            day = calcDay,
            monthNumber = calcMonth,
            year = calcYear,
            monthName = getHijriMonthName(calcMonth, locale),
            isSouthAsiaMoonVerified = false
        )
    }

    /**
     * Dual Hijri Month Header Label e.g. "Rabi ul Aakhir / Jamadi ul Awwal 1448"
     * Matches Screenshot 1 and index.html monthHijriHeaderLabel(year, month).
     */
    fun getDualHijriMonthHeader(gYear: Int, gMonth: Int, locale: String): String {
        val firstDay = getHijriDate(gYear, gMonth, 1, locale)
        val maxDays = Calendar.getInstance().apply {
            set(Calendar.YEAR, gYear)
            set(Calendar.MONTH, gMonth)
            set(Calendar.DAY_OF_MONTH, 1)
        }.getActualMaximum(Calendar.DAY_OF_MONTH)
        val lastDay = getHijriDate(gYear, gMonth, maxDays, locale)

        return if (firstDay.monthNumber == lastDay.monthNumber && firstDay.year == lastDay.year) {
            "${firstDay.monthName} ${firstDay.year}"
        } else if (firstDay.year == lastDay.year) {
            "${firstDay.monthName} / ${lastDay.monthName} ${firstDay.year}"
        } else {
            "${firstDay.monthName} ${firstDay.year} / ${lastDay.monthName} ${lastDay.year}"
        }
    }

    /**
     * Hijri Month Range string e.g. "Rabi ul Aakhir - Jamadi ul Awwal 1448 AH"
     * Matches Screenshot 2 & 3 and index.html monthHijriRange(year, month).
     */
    fun getHijriMonthRange(gYear: Int, gMonth: Int, locale: String, suffix: String = "AH"): String {
        val firstDay = getHijriDate(gYear, gMonth, 1, locale)
        val maxDays = Calendar.getInstance().apply {
            set(Calendar.YEAR, gYear)
            set(Calendar.MONTH, gMonth)
            set(Calendar.DAY_OF_MONTH, 1)
        }.getActualMaximum(Calendar.DAY_OF_MONTH)
        val lastDay = getHijriDate(gYear, gMonth, maxDays, locale)

        return if (firstDay.monthNumber == lastDay.monthNumber && firstDay.year == lastDay.year) {
            "${firstDay.monthName} ${firstDay.year} $suffix"
        } else if (firstDay.year == lastDay.year) {
            "${firstDay.monthName} - ${lastDay.monthName} ${firstDay.year} $suffix"
        } else {
            "${firstDay.monthName} ${firstDay.year} $suffix - ${lastDay.monthName} ${lastDay.year} $suffix"
        }
    }

    /**
     * Converts a Hijri date (hy, hm, hd) to its corresponding Gregorian Calendar date.
     * Uses verified South Asia moon sightings when available, falling back to astronomical calculation.
     */
    fun getGregorianForHijri(hy: Int, hm: Int, hd: Int): Calendar? {
        val match = HIJRI_MONTH_STARTS_SA.firstOrNull { it.hy == hy && it.hm == hm }
        if (match != null) {
            return Calendar.getInstance().apply {
                set(Calendar.YEAR, match.gYear)
                set(Calendar.MONTH, match.gMonth - 1)
                set(Calendar.DAY_OF_MONTH, match.gDay)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                add(Calendar.DAY_OF_MONTH, hd - 1)
            }
        }
        val estimatedGYear = ((hy - 1) * 0.970224 + 622).toInt()
        val searchCal = Calendar.getInstance().apply {
            set(Calendar.YEAR, estimatedGYear)
            set(Calendar.MONTH, 0)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        for (i in 0 until 400) {
            val h = getHijriDate(searchCal.get(Calendar.YEAR), searchCal.get(Calendar.MONTH), searchCal.get(Calendar.DAY_OF_MONTH), "en")
            if (h.year == hy && h.monthNumber == hm && h.day == hd) {
                return searchCal
            }
            searchCal.add(Calendar.DAY_OF_MONTH, 1)
        }
        return null
    }
}
