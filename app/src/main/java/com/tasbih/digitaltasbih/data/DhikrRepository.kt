package com.tasbih.digitaltasbih.data

import android.content.Context
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * DhikrRepository - Master data engine for Dhikr Tracker.
 * 100% dynamically linked to TasbihRepository, reflecting real user recitations only.
 * Reads daily counts, cumulative history records, streaks, dynamic chart periods,
 * most frequent recitations, best prayer times, and live in-memory sessions.
 */
object DhikrRepository {
    private const val PREFS_NAME = "dhikr_tracker_prefs"
    private var prefs: SharedPreferences? = null

    fun init(context: Context) {
        if (prefs == null) {
            prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }
        TasbihRepository.init(context)
    }

    private fun dateKey(cal: Calendar): String {
        return String.format(
            Locale.US,
            "%04d-%02d-%02d",
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) + 1,
            cal.get(Calendar.DAY_OF_MONTH)
        )
    }

    private fun isSameDay(c1: Calendar, c2: Calendar): Boolean {
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
                c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)
    }

    private fun isSameDay(timestamp: Long, cal: Calendar): Boolean {
        val c = Calendar.getInstance().apply { timeInMillis = timestamp }
        return isSameDay(c, cal)
    }

    /**
     * Resolves all tracked dhikrs across core items, custom items, Asma-ul-Husna, and history items.
     */
    fun getAllTrackedDhikrs(lang: String = "en"): List<DhikrItem> {
        val list = mutableListOf<DhikrItem>()
        val seen = HashSet<String>()

        // 1. All active dhikrs from TasbihRepository
        val allTasbihDhikrs = TasbihRepository.getAllDhikrs()
        for (td in allTasbihDhikrs) {
            if (seen.add(td.id)) {
                list.add(
                    DhikrItem(
                        id = td.id,
                        name = td.getName(lang),
                        meaning = td.getMeaning(lang),
                        arabic = td.arabic,
                        icon = td.icon,
                        defaultTarget = td.defaultTarget
                    )
                )
            }
        }

        // 2. Any additional dhikrs present in History
        val history = TasbihRepository.getHistoryList()
        for (h in history) {
            if (seen.add(h.dhikrId)) {
                list.add(
                    DhikrItem(
                        id = h.dhikrId,
                        name = h.dhikrName,
                        meaning = "",
                        arabic = h.arabic,
                        icon = h.icon,
                        defaultTarget = 100
                    )
                )
            }
        }

        return list
    }

    /**
     * Calculates the exact recitation count for a given dhikr on a given calendar day.
     * Matches original index.html dhikrTasbihCountForDate(tasbihId, date).
     */
    fun getDhikrCountForDate(dhikrId: String, cal: Calendar): Int {
        val isToday = isSameDay(cal, Calendar.getInstance())
        if (isToday) {
            // For today, TasbihRepository.getTodayCount holds the grand total of today's recitations
            return TasbihRepository.getTodayCount(dhikrId)
        }

        // For past dates: check history list + dateKey storage
        val histList = TasbihRepository.getHistoryList()
        val histSum = histList.filter { isSameDay(it.timestamp, cal) && it.dhikrId == dhikrId }.sumOf { it.count }

        val p = prefs
        val dKey = dateKey(cal)
        val prefCount = p?.getInt("count_${dKey}_$dhikrId", 0) ?: 0

        return maxOf(histSum, prefCount)
    }

    /**
     * Calculates the total recitations across ALL dhikrs on a given calendar day.
     */
    fun getDayTotal(cal: Calendar): Int {
        val isToday = isSameDay(cal, Calendar.getInstance())
        if (isToday) {
            val allDhikrs = getAllTrackedDhikrs()
            return allDhikrs.sumOf { TasbihRepository.getTodayCount(it.id) }
        }

        val histList = TasbihRepository.getHistoryList()
        val histSum = histList.filter { isSameDay(it.timestamp, cal) }.sumOf { it.count }

        val allDhikrs = getAllTrackedDhikrs()
        val p = prefs
        val dKey = dateKey(cal)
        var prefSum = 0
        if (p != null) {
            for (d in allDhikrs) {
                prefSum += p.getInt("count_${dKey}_${d.id}", 0)
            }
        }

        return maxOf(histSum, prefSum)
    }

    /**
     * Records a count session from the Tasbih counter or direct tracker entry.
     * Updates real-time prayer hour buckets and persistent daily counts.
     */
    fun recordTasbihCount(dhikrId: String, count: Int, timestamp: Long = System.currentTimeMillis()) {
        val p = prefs ?: return
        val cal = Calendar.getInstance().apply { timeInMillis = timestamp }
        val dKey = dateKey(cal)

        val curDateCount = p.getInt("count_${dKey}_$dhikrId", 0)
        val curAllTime = p.getInt("alltime_$dhikrId", 0)

        // Hour bucket computation matching original index.html
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val bucket = when (hour) {
            in 4..6 -> "Fajr"
            in 11..14 -> "Dhuhr"
            in 15..17 -> "Asr"
            in 18..19 -> "Maghrib"
            in 20..22 -> "Isha"
            else -> "Night"
        }
        val curBucketCount = p.getInt("bucket_$bucket", 0)

        p.edit()
            .putInt("count_${dKey}_$dhikrId", curDateCount + count)
            .putInt("alltime_$dhikrId", curAllTime + count)
            .putInt("bucket_$bucket", curBucketCount + count)
            .apply()
    }

    /**
     * Retrieves daily summary metrics and active dhikr records for a given date.
     * Purely dynamic — returns real user recitations only (0 if none).
     */
    fun getSummaryForDate(cal: Calendar, lang: String = "en"): DhikrDaySummary {
        val allItems = getAllTrackedDhikrs(lang)
        val records = mutableListOf<DhikrCountRecord>()
        var total = 0

        for (item in allItems) {
            val count = getDhikrCountForDate(item.id, cal)
            if (count > 0) {
                val target = TasbihRepository.getTarget(item.id, item.defaultTarget)
                records.add(DhikrCountRecord(item, count, target))
                total += count
            }
        }
        records.sortByDescending { it.count }

        // Yesterday comparison
        val yCal = Calendar.getInstance().apply {
            time = cal.time
            add(Calendar.DAY_OF_YEAR, -1)
        }
        val yTotal = getDayTotal(yCal)
        val delta = total - yTotal

        // Streak computation
        val streak = calculateStreak()

        return DhikrDaySummary(
            totalCount = total,
            activeTypesCount = records.size,
            streakDays = streak,
            deltaVsYesterday = delta,
            records = records
        )
    }

    /**
     * Calculates consecutive days streak where dhikr was recited.
     * Matches original index.html dhikrStreak().
     */
    fun calculateStreak(): Int {
        var streak = 0
        val cal = Calendar.getInstance()

        val todayTotal = getDayTotal(cal)
        val startOffset = if (todayTotal > 0) 0 else 1

        val checkCal = Calendar.getInstance()
        if (startOffset > 0) {
            checkCal.add(Calendar.DAY_OF_YEAR, -1)
        }

        for (i in 0..365) {
            val daySum = getDayTotal(checkCal)
            if (daySum > 0) {
                streak++
                checkCal.add(Calendar.DAY_OF_YEAR, -1)
            } else {
                break
            }
        }
        return streak
    }

    /**
     * Weekly progress bar chart data (Mon to Sun of current week).
     * Future days have isFuture = true and count = 0.
     */
    fun getWeeklyChartData(now: Calendar = Calendar.getInstance()): List<ChartBarData> {
        val result = mutableListOf<ChartBarData>()
        val weekDayLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

        // Mon = 0, Sun = 6
        val dow = (now.get(Calendar.DAY_OF_WEEK) + 5) % 7
        val mondayCal = Calendar.getInstance().apply {
            time = now.time
            add(Calendar.DAY_OF_YEAR, -dow)
        }

        for (i in 0..6) {
            val dCal = Calendar.getInstance().apply {
                time = mondayCal.time
                add(Calendar.DAY_OF_YEAR, i)
            }
            val isFuture = dCal.after(now) && !isSameDay(dCal, now)

            val dayTotal = if (isFuture) 0 else getDayTotal(dCal)
            val dLabel = SimpleDateFormat("d MMM", Locale.ENGLISH).format(dCal.time)

            result.add(
                ChartBarData(
                    label = weekDayLabels[i],
                    count = dayTotal,
                    isFuture = isFuture,
                    fullLabel = "${weekDayLabels[i]} ($dLabel)"
                )
            )
        }
        return result
    }

    /**
     * Monthly progress bar chart data (Days 1 to DaysInMonth).
     */
    fun getMonthlyChartData(now: Calendar = Calendar.getInstance()): List<ChartBarData> {
        val result = mutableListOf<ChartBarData>()
        val maxDays = now.getActualMaximum(Calendar.DAY_OF_MONTH)
        val curDay = now.get(Calendar.DAY_OF_MONTH)
        val curYear = now.get(Calendar.YEAR)
        val curMonth = now.get(Calendar.MONTH)
        val monthStr = SimpleDateFormat("MMM yyyy", Locale.ENGLISH).format(now.time)

        for (d in 1..maxDays) {
            val dCal = Calendar.getInstance().apply {
                set(Calendar.YEAR, curYear)
                set(Calendar.MONTH, curMonth)
                set(Calendar.DAY_OF_MONTH, d)
            }
            val isFuture = d > curDay

            val dayTotal = if (isFuture) 0 else getDayTotal(dCal)

            result.add(
                ChartBarData(
                    label = d.toString(),
                    count = dayTotal,
                    isFuture = isFuture,
                    fullLabel = "$d $monthStr"
                )
            )
        }
        return result
    }

    /**
     * Yearly progress bar chart data (Jan to Dec).
     */
    fun getYearlyChartData(now: Calendar = Calendar.getInstance()): List<ChartBarData> {
        val result = mutableListOf<ChartBarData>()
        val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        val curMonth = now.get(Calendar.MONTH)
        val curYear = now.get(Calendar.YEAR)

        for (m in 0..11) {
            val isFuture = m > curMonth
            var monthTotal = 0

            if (!isFuture) {
                val tempCal = Calendar.getInstance().apply {
                    set(Calendar.YEAR, curYear)
                    set(Calendar.MONTH, m)
                    set(Calendar.DAY_OF_MONTH, 1)
                }
                val daysInM = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH)
                val maxDayToCheck = if (m == curMonth) now.get(Calendar.DAY_OF_MONTH) else daysInM

                for (d in 1..maxDayToCheck) {
                    tempCal.set(Calendar.DAY_OF_MONTH, d)
                    monthTotal += getDayTotal(tempCal)
                }
            }

            result.add(
                ChartBarData(
                    label = months[m],
                    count = monthTotal,
                    isFuture = isFuture,
                    fullLabel = "${months[m]} $curYear"
                )
            )
        }
        return result
    }

    /**
     * Top 3 Most Frequent all-time recitations.
     * Matches original index.html dhikrAllTimeCounts().
     * If user hasn't counted anything yet, returns emptyList() cleanly!
     */
    fun getMostFrequentDhikrs(lang: String = "en"): List<FrequentDhikrItem> {
        val allDhikrs = getAllTrackedDhikrs(lang)
        val list = mutableListOf<FrequentDhikrItem>()

        for (d in allDhikrs) {
            val allTime = TasbihRepository.getAllTimeCount(d.id)
            if (allTime > 0) {
                val iconType = when {
                    d.icon.contains("☀️") || d.icon.contains("🌼") -> "sun"
                    d.icon.contains("🌙") -> "moon"
                    else -> "sparkle"
                }
                list.add(FrequentDhikrItem(d.id, d.name, allTime, iconType))
            }
        }

        return list.sortedByDescending { it.count }.take(3)
    }

    /**
     * Best prayer time window for Dhikr recitation.
     * Derived from history session timestamps and live bucket counts.
     * Matches original index.html dhikrBestTimeLabel().
     * If no recitations recorded yet, returns "—" (dash).
     */
    fun getBestTime(): String {
        val buckets = mutableMapOf(
            "Fajr" to 0,
            "Dhuhr" to 0,
            "Asr" to 0,
            "Maghrib" to 0,
            "Isha" to 0,
            "Night" to 0
        )

        // 1. Parse timestamps from Tasbih history list
        val histList = TasbihRepository.getHistoryList()
        for (h in histList) {
            val cal = Calendar.getInstance().apply { timeInMillis = h.timestamp }
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            val b = when (hour) {
                in 4..6 -> "Fajr"
                in 11..14 -> "Dhuhr"
                in 15..17 -> "Asr"
                in 18..19 -> "Maghrib"
                in 20..22 -> "Isha"
                else -> "Night"
            }
            buckets[b] = (buckets[b] ?: 0) + h.count
        }

        // 2. Add real-time live bucket counts from SharedPreferences
        val p = prefs
        if (p != null) {
            for (k in buckets.keys.toList()) {
                val liveCnt = p.getInt("bucket_$k", 0)
                buckets[k] = maxOf(buckets[k] ?: 0, liveCnt)
            }
        }

        var best: String? = null
        var maxCount = 0
        for ((b, cnt) in buckets) {
            if (cnt > maxCount) {
                maxCount = cnt
                best = b
            }
        }

        return best ?: "—"
    }

    /**
     * Total dhikrs recited this week (sum of last 7 days).
     * Returns real user total (0 if no recitations).
     */
    fun getTotalThisWeek(now: Calendar = Calendar.getInstance()): Int {
        var total = 0
        val cal = Calendar.getInstance().apply { time = now.time }
        for (i in 0..6) {
            total += getDayTotal(cal)
            cal.add(Calendar.DAY_OF_YEAR, -1)
        }
        return total
    }

    /**
     * Daily average recitation count.
     * Returns real user average (0 if no recitations).
     */
    fun getDailyAverage(now: Calendar = Calendar.getInstance()): Int {
        val total = getTotalThisWeek(now)
        return Math.round(total / 7f)
    }

    /**
     * Clears local tracker preferences when user clears history.
     */
    fun clearData() {
        prefs?.edit()?.clear()?.apply()
    }
}
