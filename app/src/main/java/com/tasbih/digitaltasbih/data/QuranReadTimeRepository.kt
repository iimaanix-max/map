package com.tasbih.digitaltasbih.data

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Visual intensity tiers for the Quran Reading Activity Heatmap.
 * Matches user's exact specification:
 * - High (>= 20m): Rich Deep Green
 * - Moderate (10-19m): Medium Green
 * - Light (5-9m): Light Green
 * - Minimal (1-4m): Yellow / Amber
 * - Missed (0m): Red
 * - Future: Empty neutral dark
 */
enum class HeatmapIntensity(
    val label: String,
    val color: Color,
    val borderColor: Color
) {
    HIGH("High (20m+)", Color(0xFF15803D), Color(0xFF22C55E)),
    MODERATE("Moderate (10-19m)", Color(0xFF22C55E), Color(0xFF4ADE80)),
    LIGHT("Light (5-9m)", Color(0xFF86EFAC), Color(0xFFBBF7D0)),
    MINIMAL("Minimal (1-4m)", Color(0xFFFACC15), Color(0xFFFEF08A)),
    MISSED("Missed (0m)", Color(0xFFDC2626), Color(0xFFF87171)),
    NOT_STARTED("No reading", Color(0xFFF1F5F9), Color(0xFFE2E8F0)),
    FUTURE("Upcoming", Color(0xFFF1F5F9), Color(0xFFE2E8F0))
}

data class MonthlyWeekProgress(
    val weekNumber: Int,
    val weekLabel: String,
    val dateRangeLabel: String,
    val totalMinutes: Int,
    val activeDays: Int,
    val isCurrentWeek: Boolean
)

data class MonthlyDayCell(
    val dayNumber: Int,
    val dateStr: String,
    val dayOfWeek: Int,
    val minutes: Int,
    val isToday: Boolean,
    val isFuture: Boolean,
    val isBeforeInstall: Boolean
)

data class MonthlyTrackerData(
    val monthTitle: String,
    val monthKey: String,
    val totalMinutes: Int,
    val activeDays: Int,
    val totalDaysInMonth: Int,
    val daysElapsed: Int,
    val dailyGoalMinutes: Int,
    val monthTargetMinutes: Int,
    val completionPercentage: Int,
    val weeks: List<MonthlyWeekProgress>,
    val days: List<MonthlyDayCell>,
    val startDayOfWeekOffset: Int
)

data class DayReadingRecord(
    val dateStr: String,
    val dayOfWeekName: String,
    val dayOfMonth: Int,
    val monthName: String,
    val minutes: Int,
    val isToday: Boolean
)

data class WeeklyDayProgress(
    val dayLabel: String,
    val dateStr: String,
    val minutes: Int,
    val isToday: Boolean
)

data class HeatmapDay(
    val dateStr: String,
    val dayOfMonth: Int,
    val dayOfWeek: Int, // 1 = Sunday, 7 = Saturday
    val monthIndex: Int,
    val monthName: String,
    val minutes: Int,
    val isToday: Boolean,
    val isFuture: Boolean,
    val intensity: HeatmapIntensity
)

data class MonthBreakdown(
    val monthKey: String, // e.g. "2026-10"
    val monthTitle: String, // e.g. "October 2026"
    val totalMinutes: Int,
    val activeDays: Int,
    val days: List<DayReadingRecord>
)

data class QuranReadStats(
    val todayMinutes: Int,
    val goalMinutes: Int,
    val currentStreak: Int,
    val bestStreak: Int,
    val totalReadMinutes: Int,
    val totalActiveDays: Int
)

object QuranReadTimeRepository {
    private const val PREFS_NAME = "quran_read_time_prefs"
    private const val KEY_DAILY_GOAL = "key_quran_daily_goal_minutes"
    private const val KEY_CLEANED_DUMMY_V2 = "key_cleaned_dummy_v2"
    private const val KEY_FIRST_INSTALL_DATE = "key_quran_first_install_date"
    private const val KEY_ACCUMULATED_SECONDS = "key_accumulated_seconds_today"
    private const val KEY_ACCUMULATED_DATE = "key_accumulated_date"

    val updateTrigger = mutableStateOf(0L)

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val dayNameFormat = SimpleDateFormat("EEE", Locale.US)
    private val monthNameFormat = SimpleDateFormat("MMM", Locale.US)
    private val monthYearFormat = SimpleDateFormat("MMMM yyyy", Locale.US)

    fun getTodayDateStr(): String {
        return dateFormat.format(Date())
    }

    /**
     * Initializes repository. Cleans any previously seeded dummy data so
     * tracking starts fresh and accurate from actual user reading.
     */
    fun init(context: Context) {
        val prefs = getPrefs(context)
        if (!prefs.getBoolean(KEY_CLEANED_DUMMY_V2, false)) {
            prefs.edit().clear().putBoolean(KEY_CLEANED_DUMMY_V2, true).apply()
            updateTrigger.value = System.currentTimeMillis()
        }
    }

    fun clearAllData(context: Context) {
        getPrefs(context).edit().clear().putBoolean(KEY_CLEANED_DUMMY_V2, true).apply()
        updateTrigger.value = System.currentTimeMillis()
    }

    fun getInstallDateStr(context: Context): String {
        val prefs = getPrefs(context)
        val saved = prefs.getString(KEY_FIRST_INSTALL_DATE, null)
        if (!saved.isNullOrEmpty()) return saved

        val installDate = try {
            val installTime = context.packageManager.getPackageInfo(context.packageName, 0).firstInstallTime
            dateFormat.format(Date(installTime))
        } catch (e: Exception) {
            getTodayDateStr()
        }
        prefs.edit().putString(KEY_FIRST_INSTALL_DATE, installDate).apply()
        return installDate
    }

    fun getDailyGoalMinutes(context: Context): Int {
        return getPrefs(context).getInt(KEY_DAILY_GOAL, 20)
    }

    fun setDailyGoalMinutes(context: Context, goal: Int) {
        val clamped = goal.coerceIn(5, 180)
        getPrefs(context).edit().putInt(KEY_DAILY_GOAL, clamped).apply()
        updateTrigger.value = System.currentTimeMillis()
    }

    fun getMinutesForDate(context: Context, dateStr: String): Int {
        return getPrefs(context).getInt("read_mins_$dateStr", 0)
    }

    fun getTodayMinutes(context: Context): Int {
        return getMinutesForDate(context, getTodayDateStr())
    }

    /**
     * Adds minutes to a given date (defaults to today).
     */
    fun addReadingMinutes(context: Context, minutes: Int, dateStr: String = getTodayDateStr()) {
        if (minutes <= 0) return
        val current = getMinutesForDate(context, dateStr)
        val newMinutes = current + minutes
        getPrefs(context).edit().putInt("read_mins_$dateStr", newMinutes).apply()
        updateTrigger.value = System.currentTimeMillis()
    }

    /**
     * Sets exact minutes for a given date.
     */
    fun setReadingMinutes(context: Context, minutes: Int, dateStr: String = getTodayDateStr()) {
        val clamped = minutes.coerceAtLeast(0)
        getPrefs(context).edit().putInt("read_mins_$dateStr", clamped).apply()
        updateTrigger.value = System.currentTimeMillis()
    }

    /**
     * Live active reading session tracker:
     * Called by QuranReaderScreen every few seconds.
     * Accumulates seconds; each 60 seconds converts to +1 minute.
     */
    fun logSessionSeconds(context: Context, seconds: Int) {
        if (seconds <= 0) return
        val today = getTodayDateStr()
        val prefs = getPrefs(context)
        val lastDate = prefs.getString(KEY_ACCUMULATED_DATE, today) ?: today
        var accumulated = if (lastDate == today) prefs.getInt(KEY_ACCUMULATED_SECONDS, 0) else 0

        accumulated += seconds
        val fullMinutes = accumulated / 60
        val remainingSeconds = accumulated % 60

        prefs.edit()
            .putString(KEY_ACCUMULATED_DATE, today)
            .putInt(KEY_ACCUMULATED_SECONDS, remainingSeconds)
            .apply()

        if (fullMinutes > 0) {
            addReadingMinutes(context, fullMinutes, today)
        }
    }

    /**
     * Returns full Quran stats (Today, Goal, Streaks, Total mins, Total days).
     */
    fun getStats(context: Context): QuranReadStats {
        val today = getTodayDateStr()
        val todayMins = getMinutesForDate(context, today)
        val goalMins = getDailyGoalMinutes(context)

        var totalMins = 0
        var totalDays = 0

        // Scan all days in the past 365 days
        val cal = Calendar.getInstance()
        var currentStreak = 0
        var streakBroken = false
        var bestStreak = 0
        var runningStreak = 0

        // For streak calculation, start from today
        val todayRead = todayMins > 0
        if (todayRead) {
            currentStreak++
        }

        // Check backwards up to 365 days
        for (i in 1..365) {
            val pastCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -i) }
            val dateStr = dateFormat.format(pastCal.time)
            val mins = getMinutesForDate(context, dateStr)
            if (mins > 0) {
                totalMins += mins
                totalDays++
                if (!streakBroken) {
                    currentStreak++
                }
            } else {
                streakBroken = true
            }
        }
        if (todayRead) {
            totalMins += todayMins
            totalDays++
        }

        // Calculate best streak historically across the past 180 days
        for (i in 180 downTo 0) {
            val pastCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -i) }
            val dateStr = dateFormat.format(pastCal.time)
            val mins = getMinutesForDate(context, dateStr)
            if (mins > 0) {
                runningStreak++
                if (runningStreak > bestStreak) bestStreak = runningStreak
            } else {
                runningStreak = 0
            }
        }
        bestStreak = maxOf(bestStreak, currentStreak)

        return QuranReadStats(
            todayMinutes = todayMins,
            goalMinutes = goalMins,
            currentStreak = currentStreak,
            bestStreak = bestStreak,
            totalReadMinutes = totalMins,
            totalActiveDays = totalDays
        )
    }

    /**
     * Weekly progress bar data (8 days ending today: Wed, Thu, Fri, Sat, Sun, Mon, Tue, Today).
     */
    fun getWeeklyProgress(context: Context): List<WeeklyDayProgress> {
        val list = mutableListOf<WeeklyDayProgress>()
        for (i in 7 downTo 0) {
            val cal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -i) }
            val dateStr = dateFormat.format(cal.time)
            val minutes = getMinutesForDate(context, dateStr)
            val isToday = (i == 0)
            val dayLabel = if (isToday) "Today" else dayNameFormat.format(cal.time)
            list.add(WeeklyDayProgress(dayLabel, dateStr, minutes, isToday))
        }
        return list
    }

    /**
     * Generates the multi-month heatmap grid (e.g. Aug, Sep, Oct 2026).
     * Grouped by weeks (columns), with 7 days per column (Sunday to Saturday).
     */
    fun getReadingActivityHeatmap(context: Context, year: Int = Calendar.getInstance().get(Calendar.YEAR)): List<List<HeatmapDay>> {
        val todayCal = Calendar.getInstance()
        val todayStr = dateFormat.format(todayCal.time)
        val installDateStr = getInstallDateStr(context)

        // Show rolling ~12 to 14 weeks up to current week
        val startCal = Calendar.getInstance().apply {
            // Go back 13 weeks to Sunday
            add(Calendar.WEEK_OF_YEAR, -13)
            set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val weeks = mutableListOf<List<HeatmapDay>>()
        val walkCal = startCal.clone() as Calendar

        while (true) {
            val weekDays = mutableListOf<HeatmapDay>()
            for (d in 0 until 7) {
                val dateStr = dateFormat.format(walkCal.time)
                val dayOfMonth = walkCal.get(Calendar.DAY_OF_MONTH)
                val dayOfWeek = walkCal.get(Calendar.DAY_OF_WEEK)
                val monthIndex = walkCal.get(Calendar.MONTH)
                val monthName = monthNameFormat.format(walkCal.time)
                val minutes = getMinutesForDate(context, dateStr)

                val isToday = (dateStr == todayStr)
                val isFuture = walkCal.after(todayCal) && !isToday

                val isBeforeInstall = (dateStr < installDateStr)

                val intensity = when {
                    isFuture -> HeatmapIntensity.FUTURE
                    minutes >= 20 -> HeatmapIntensity.HIGH
                    minutes >= 10 -> HeatmapIntensity.MODERATE
                    minutes >= 5 -> HeatmapIntensity.LIGHT
                    minutes >= 1 -> HeatmapIntensity.MINIMAL
                    isToday -> HeatmapIntensity.NOT_STARTED // Today is not missed yet
                    isBeforeInstall -> HeatmapIntensity.NOT_STARTED // Before install date, never marked as missed
                    else -> HeatmapIntensity.MISSED // 0m on actual tracked day after app installation
                }

                weekDays.add(
                    HeatmapDay(
                        dateStr = dateStr,
                        dayOfMonth = dayOfMonth,
                        dayOfWeek = dayOfWeek,
                        monthIndex = monthIndex,
                        monthName = monthName,
                        minutes = minutes,
                        isToday = isToday,
                        isFuture = isFuture,
                        intensity = intensity
                    )
                )
                walkCal.add(Calendar.DAY_OF_MONTH, 1)
            }
            weeks.add(weekDays)

            // Stop if we have completed the current week
            if (walkCal.after(todayCal) && walkCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                break
            }
            if (weeks.size >= 16) break
        }

        return weeks
    }

    /**
     * Monthly breakdown list (e.g. October 2026, September 2026).
     */
    fun getMonthlyBreakdown(context: Context): List<MonthBreakdown> {
        val todayCal = Calendar.getInstance()
        val todayStr = dateFormat.format(todayCal.time)
        val months = mutableListOf<MonthBreakdown>()

        // Look at current month and previous 2 months
        for (m in 0..2) {
            val cal = Calendar.getInstance().apply {
                add(Calendar.MONTH, -m)
            }
            val monthTitle = monthYearFormat.format(cal.time)
            val monthKey = SimpleDateFormat("yyyy-MM", Locale.US).format(cal.time)

            val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
            val maxDayToScan = if (m == 0) todayCal.get(Calendar.DAY_OF_MONTH) else daysInMonth

            val dayList = mutableListOf<DayReadingRecord>()
            var monthMinutes = 0
            var activeDays = 0

            for (d in maxDayToScan downTo 1) {
                val dayCal = (cal.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, d) }
                val dateStr = dateFormat.format(dayCal.time)
                val mins = getMinutesForDate(context, dateStr)
                val isToday = (dateStr == todayStr)

                // Include day in list if it has minutes recorded or is recent/today
                if (mins > 0 || isToday) {
                    if (mins > 0) {
                        monthMinutes += mins
                        activeDays++
                    }
                    dayList.add(
                        DayReadingRecord(
                            dateStr = dateStr,
                            dayOfWeekName = dayNameFormat.format(dayCal.time),
                            dayOfMonth = d,
                            monthName = monthNameFormat.format(dayCal.time),
                            minutes = mins,
                            isToday = isToday
                        )
                    )
                }
            }

            if (dayList.isNotEmpty() && (monthMinutes > 0 || m == 0)) {
                months.add(
                    MonthBreakdown(
                        monthKey = monthKey,
                        monthTitle = monthTitle,
                        totalMinutes = monthMinutes,
                        activeDays = activeDays,
                        days = dayList
                    )
                )
            }
        }

        return months
    }

    /**
     * Generates rich Monthly Tracker data for the current or selected month.
     * Includes month overview stats, week-by-week bar breakdown, and 31-day calendar cells.
     */
    fun getMonthlyTrackerData(context: Context, monthOffset: Int = 0): MonthlyTrackerData {
        val todayCal = Calendar.getInstance()
        val todayStr = dateFormat.format(todayCal.time)
        val installDateStr = getInstallDateStr(context)
        val goalMinutes = getDailyGoalMinutes(context)

        val cal = Calendar.getInstance().apply {
            add(Calendar.MONTH, -monthOffset)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val monthTitle = monthYearFormat.format(cal.time)
        val monthKey = SimpleDateFormat("yyyy-MM", Locale.US).format(cal.time)
        val totalDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) // 1 = Sunday, 7 = Saturday
        val startDayOfWeekOffset = (firstDayOfWeek - Calendar.SUNDAY).coerceIn(0, 6)

        var totalMinutes = 0
        var activeDays = 0
        var daysElapsed = 0

        val dayCells = mutableListOf<MonthlyDayCell>()

        for (d in 1..totalDaysInMonth) {
            val dayCal = (cal.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, d) }
            val dateStr = dateFormat.format(dayCal.time)
            val isToday = (dateStr == todayStr)
            val isFuture = dayCal.after(todayCal) && !isToday
            val isBeforeInstall = (dateStr < installDateStr)
            val mins = getMinutesForDate(context, dateStr)

            if (!isFuture) {
                daysElapsed++
                if (mins > 0) {
                    totalMinutes += mins
                    activeDays++
                }
            }

            dayCells.add(
                MonthlyDayCell(
                    dayNumber = d,
                    dateStr = dateStr,
                    dayOfWeek = dayCal.get(Calendar.DAY_OF_WEEK),
                    minutes = mins,
                    isToday = isToday,
                    isFuture = isFuture,
                    isBeforeInstall = isBeforeInstall
                )
            )
        }

        val monthTargetMinutes = maxOf(daysElapsed, 1) * goalMinutes
        val completionPercentage = if (monthTargetMinutes > 0) {
            ((totalMinutes.toFloat() / monthTargetMinutes.toFloat()) * 100).toInt().coerceIn(0, 100)
        } else 0

        val weekRanges = listOf(
            Pair(1, 7),
            Pair(8, 14),
            Pair(15, 21),
            Pair(22, 28),
            Pair(29, totalDaysInMonth)
        )

        val todayDayOfMonth = if (monthOffset == 0) todayCal.get(Calendar.DAY_OF_MONTH) else -1
        val shortMonthName = monthNameFormat.format(cal.time)

        val weeks = weekRanges.filter { it.first <= totalDaysInMonth }.mapIndexed { index, (startDay, endDay) ->
            val actualEnd = minOf(endDay, totalDaysInMonth)
            val weekDays = dayCells.filter { it.dayNumber in startDay..actualEnd }
            val weekMins = weekDays.sumOf { it.minutes }
            val weekActive = weekDays.count { it.minutes > 0 }
            val isCurrentWeek = (todayDayOfMonth in startDay..actualEnd)

            MonthlyWeekProgress(
                weekNumber = index + 1,
                weekLabel = "W${index + 1}",
                dateRangeLabel = "$startDay-$actualEnd $shortMonthName",
                totalMinutes = weekMins,
                activeDays = weekActive,
                isCurrentWeek = isCurrentWeek
            )
        }

        return MonthlyTrackerData(
            monthTitle = monthTitle,
            monthKey = monthKey,
            totalMinutes = totalMinutes,
            activeDays = activeDays,
            totalDaysInMonth = totalDaysInMonth,
            daysElapsed = daysElapsed,
            dailyGoalMinutes = goalMinutes,
            monthTargetMinutes = monthTargetMinutes,
            completionPercentage = completionPercentage,
            weeks = weeks,
            days = dayCells,
            startDayOfWeekOffset = startDayOfWeekOffset
        )
    }
}

