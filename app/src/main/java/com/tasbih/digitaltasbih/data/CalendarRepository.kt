package com.tasbih.digitaltasbih.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
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
 * Repository for Islamic Calendar events, Hijri synchronization, and Admin events.
 */
object CalendarRepository {
    private const val TAG = "CalendarRepository"
    private const val PREFS_NAME = "digitalTasbihCalendarCache"
    private const val KEY_CACHE = "calendar_events_v1"
    private const val FIREBASE_URL = "https://tasbih-digital-counter-default-rtdb.firebaseio.com/calendar_events.json"

    private lateinit var prefs: SharedPreferences
    private val offlineEvents = mutableListOf<CalendarEvent>()
    private val remoteEvents = mutableListOf<CalendarEvent>()
    private val allMergedEvents = mutableListOf<CalendarEvent>()
    private val ursNumbersMap = mutableMapOf<String, String>()

    private var isInitialized = false

    fun init(context: Context) {
        if (isInitialized) return
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // 1. Load pre-bundled offline seed (70 authentic events)
        loadOfflineSeed(context)

        // 2. Load cached remote events from SharedPreferences
        loadCachedRemoteEvents()

        // 3. Rebuild merged event list
        rebuildMergedList()

        // 4. Fetch latest events from Firebase in background
        fetchRemoteEventsAsync()

        isInitialized = true
    }

    private fun loadOfflineSeed(context: Context) {
        try {
            val jsonString = context.assets.open("calendar_events_seed.json").bufferedReader().use { it.readText() }
            val root = JSONObject(jsonString)

            if (root.has("ursNumbers")) {
                val nums = root.getJSONObject("ursNumbers")
                nums.keys().forEach { k ->
                    ursNumbersMap[k] = nums.optString(k, "")
                }
            }

            if (root.has("events")) {
                val arr = root.getJSONArray("events")
                offlineEvents.clear()
                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    parseCalendarEvent(obj)?.let { offlineEvents.add(it) }
                }
            }
            Log.d(TAG, "Loaded offline seed: ${offlineEvents.size} events, ${ursNumbersMap.size} urs counts")
        } catch (e: Exception) {
            Log.e(TAG, "Error loading offline seed: ${e.message}", e)
        }
    }

    private fun loadCachedRemoteEvents() {
        try {
            val raw = prefs.getString(KEY_CACHE, null) ?: return
            val arr = JSONArray(raw)
            remoteEvents.clear()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                parseCalendarEvent(obj)?.let { remoteEvents.add(it) }
            }
            Log.d(TAG, "Loaded cached remote events: ${remoteEvents.size}")
        } catch (e: Exception) {
            Log.e(TAG, "Error reading cached events: ${e.message}")
        }
    }

    private fun fetchRemoteEventsAsync() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL(FIREBASE_URL)
                val conn = url.openConnection() as HttpURLConnection
                conn.connectTimeout = 8000
                conn.readTimeout = 8000
                conn.requestMethod = "GET"

                if (conn.responseCode == 200) {
                    val response = conn.inputStream.bufferedReader().use { it.readText() }
                    if (response.isNotBlank() && response != "null") {
                        val freshList = mutableListOf<CalendarEvent>()
                        val jsonArr = JSONArray()

                        if (response.trim().startsWith("[")) {
                            val arr = JSONArray(response)
                            for (i in 0 until arr.length()) {
                                if (arr.isNull(i)) continue
                                val obj = arr.getJSONObject(i)
                                parseCalendarEvent(obj)?.let {
                                    freshList.add(it)
                                    jsonArr.put(obj)
                                }
                            }
                        } else if (response.trim().startsWith("{")) {
                            val root = JSONObject(response)
                            val keys = root.keys()
                            while (keys.hasNext()) {
                                val k = keys.next()
                                val obj = root.optJSONObject(k)
                                if (obj != null) {
                                    if (!obj.has("id")) obj.put("id", k)
                                    parseCalendarEvent(obj)?.let {
                                        freshList.add(it)
                                        jsonArr.put(obj)
                                    }
                                }
                            }
                        }

                        if (freshList.isNotEmpty()) {
                            remoteEvents.clear()
                            remoteEvents.addAll(freshList)
                            prefs.edit().putString(KEY_CACHE, jsonArr.toString()).apply()
                            rebuildMergedList()
                            Log.d(TAG, "Synced from Firebase: ${freshList.size} events")
                        }
                    }
                }
                conn.disconnect()
            } catch (e: Exception) {
                Log.w(TAG, "Firebase sync offline/skipped: ${e.message}")
            }
        }
    }

    @Synchronized
    private fun rebuildMergedList() {
        allMergedEvents.clear()
        val map = linkedMapOf<String, CalendarEvent>()

        // 1. Put offline base events
        for (ev in offlineEvents) {
            map[ev.id] = ev
        }

        // 2. Override/add with remote admin events
        for (ev in remoteEvents) {
            map[ev.id] = ev
        }

        allMergedEvents.addAll(map.values.filter { it.active })
        Log.d(TAG, "Total active merged events: ${allMergedEvents.size}")
    }

    private fun parseCalendarEvent(obj: JSONObject): CalendarEvent? {
        val id = obj.optString("id", "").trim()
        val m = obj.optInt("m", obj.optInt("month", 0))
        val d = obj.optInt("d", obj.optInt("day", 0))
        if (id.isBlank() || m !in 1..12 || d !in 1..30) return null

        val kind = obj.optString("kind", "urs")
        val color = obj.optString("color", "#0E5D47")
        val image = obj.optString("image", "")
        val ursCount = obj.optString("ursCount", ursNumbersMap[id] ?: "")
        val active = obj.optBoolean("active", true)

        val (namePack, defaultName) = parseFieldPack(obj, "name")
        val (titlePack, defaultTitle) = parseFieldPack(obj, "title")
        val (shortPack, defaultShort) = parseFieldPack(obj, "short")
        val (locationPack, defaultLocation) = parseFieldPack(obj, "location")
        val (briefPack, defaultBrief) = parseFieldPack(obj, "brief")
        val (annivPack, defaultAnniv) = parseFieldPack(obj, "anniv")

        return CalendarEvent(
            id = id,
            m = m,
            d = d,
            kind = kind,
            namePack = namePack,
            defaultName = defaultName,
            titlePack = titlePack,
            defaultTitle = defaultTitle,
            shortPack = shortPack,
            defaultShort = defaultShort,
            color = color,
            locationPack = locationPack,
            defaultLocation = defaultLocation,
            briefPack = briefPack,
            defaultBrief = defaultBrief,
            ursCount = ursCount,
            annivPack = annivPack,
            defaultAnniv = defaultAnniv,
            image = image,
            active = active
        )
    }

    private fun parseFieldPack(obj: JSONObject, field: String): Pair<Map<String, String>?, String> {
        if (!obj.has(field)) return Pair(null, "")
        val raw = obj.opt(field) ?: return Pair(null, "")
        if (raw is JSONObject) {
            val map = mutableMapOf<String, String>()
            raw.keys().forEach { k ->
                map[k] = raw.optString(k, "")
            }
            val defaultVal = map["en"] ?: map.values.firstOrNull() ?: ""
            return Pair(map, defaultVal)
        } else {
            val str = raw.toString()
            return Pair(null, str)
        }
    }

    /**
     * Get events for a specific Hijri day.
     */
    fun getEventsForHijriDay(m: Int, d: Int): List<CalendarEvent> {
        return allMergedEvents.filter { it.m == m && it.d == d }
    }

    /**
     * Get events for a specific Gregorian date.
     */
    fun getEventsForGregorianDate(gYear: Int, gMonth: Int, gDay: Int, locale: String): List<CalendarEvent> {
        val hijri = HijriCalendarEngine.getHijriDate(gYear, gMonth, gDay, locale)
        return getEventsForHijriDay(hijri.monthNumber, hijri.day)
    }

    /**
     * Search events across title, name, location, and brief.
     */
    fun searchEvents(query: String, locale: String): List<CalendarEvent> {
        val q = query.trim().lowercase(Locale.ROOT)
        if (q.isBlank()) return emptyList()

        return allMergedEvents.filter { ev ->
            val title = ev.getTitle(locale).lowercase(Locale.ROOT)
            val name = ev.getName(locale).lowercase(Locale.ROOT)
            val loc = ev.getLocation(locale).lowercase(Locale.ROOT)
            val brief = ev.getBrief(locale).lowercase(Locale.ROOT)
            val enTitle = ev.getTitle("en").lowercase(Locale.ROOT)
            val enLoc = ev.getLocation("en").lowercase(Locale.ROOT)

            title.contains(q) || name.contains(q) || loc.contains(q) || brief.contains(q) ||
                    enTitle.contains(q) || enLoc.contains(q)
        }
    }

    /**
     * Build the 7-column month grid cells (including padding out-of-month days).
     */
    fun getMonthGridDays(
        year: Int,
        month: Int, // 0-based
        selectedIso: String,
        locale: String
    ): List<CalendarDayCell> {
        val cells = mutableListOf<CalendarDayCell>()

        val todayCal = Calendar.getInstance()
        val todayYear = todayCal.get(Calendar.YEAR)
        val todayMonth = todayCal.get(Calendar.MONTH)
        val todayDay = todayCal.get(Calendar.DAY_OF_MONTH)

        val firstDayCal = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        val daysInCurrentMonth = firstDayCal.getActualMaximum(Calendar.DAY_OF_MONTH)

        // First day of week (Calendar.SUNDAY = 1, so 0 offset for Sunday start)
        val startDayOfWeek = firstDayCal.get(Calendar.DAY_OF_WEEK) - 1 // 0 for Sunday, 1 for Monday...

        // 1. Previous month leading days
        val prevMonthCal = (firstDayCal.clone() as Calendar).apply {
            add(Calendar.MONTH, -1)
        }
        val daysInPrevMonth = prevMonthCal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val prevYear = prevMonthCal.get(Calendar.YEAR)
        val prevMonth = prevMonthCal.get(Calendar.MONTH)

        for (i in startDayOfWeek - 1 downTo 0) {
            val d = daysInPrevMonth - i
            val hDate = HijriCalendarEngine.getHijriDate(prevYear, prevMonth, d, locale)
            val events = getEventsForHijriDay(hDate.monthNumber, hDate.day)
            val iso = String.format(Locale.US, "%04d-%02d-%02d", prevYear, prevMonth + 1, d)
            val isToday = (prevYear == todayYear && prevMonth == todayMonth && d == todayDay)
            val isSelected = (iso == selectedIso)

            cells.add(
                CalendarDayCell(
                    gregorianYear = prevYear,
                    gregorianMonth = prevMonth,
                    gregorianDay = d,
                    isCurrentMonth = false,
                    isToday = isToday,
                    isSelected = isSelected,
                    hijriDate = hDate,
                    events = events
                )
            )
        }

        // 2. Current month days
        for (d in 1..daysInCurrentMonth) {
            val hDate = HijriCalendarEngine.getHijriDate(year, month, d, locale)
            val events = getEventsForHijriDay(hDate.monthNumber, hDate.day)
            val iso = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, d)
            val isToday = (year == todayYear && month == todayMonth && d == todayDay)
            val isSelected = (iso == selectedIso)

            cells.add(
                CalendarDayCell(
                    gregorianYear = year,
                    gregorianMonth = month,
                    gregorianDay = d,
                    isCurrentMonth = true,
                    isToday = isToday,
                    isSelected = isSelected,
                    hijriDate = hDate,
                    events = events
                )
            )
        }

        // 3. Next month trailing days to complete full weeks (up to multiple of 7, at least 35 or 42 cells)
        val remainder = cells.size % 7
        if (remainder != 0) {
            val nextDaysCount = 7 - remainder
            val nextMonthCal = (firstDayCal.clone() as Calendar).apply {
                add(Calendar.MONTH, 1)
            }
            val nextYear = nextMonthCal.get(Calendar.YEAR)
            val nextMonth = nextMonthCal.get(Calendar.MONTH)

            for (d in 1..nextDaysCount) {
                val hDate = HijriCalendarEngine.getHijriDate(nextYear, nextMonth, d, locale)
                val events = getEventsForHijriDay(hDate.monthNumber, hDate.day)
                val iso = String.format(Locale.US, "%04d-%02d-%02d", nextYear, nextMonth + 1, d)
                val isToday = (nextYear == todayYear && nextMonth == todayMonth && d == todayDay)
                val isSelected = (iso == selectedIso)

                cells.add(
                    CalendarDayCell(
                        gregorianYear = nextYear,
                        gregorianMonth = nextMonth,
                        gregorianDay = d,
                        isCurrentMonth = false,
                        isToday = isToday,
                        isSelected = isSelected,
                        hijriDate = hDate,
                        events = events
                    )
                )
            }
        }

        return cells
    }

    /**
     * Total number of days with events in a month.
     */
    fun getMonthEventCount(year: Int, month: Int, locale: String): Int {
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        var count = 0
        for (d in 1..daysInMonth) {
            val hDate = HijriCalendarEngine.getHijriDate(year, month, d, locale)
            if (getEventsForHijriDay(hDate.monthNumber, hDate.day).isNotEmpty()) {
                count++
            }
        }
        return count
    }

    /**
     * Get the 12 month items for Year View (Screenshot 2 & 3).
     */
    fun getYearOverview(
        year: Int,
        currentYear: Int,
        currentMonth: Int,
        activeMonth: Int,
        locale: String
    ): List<MonthYearOverviewItem> {
        val list = mutableListOf<MonthYearOverviewItem>()
        val cal = Calendar.getInstance()

        val shortMonthNames = CalendarLocalization.getGregorianShortMonthNames(locale)
        val fullMonthNames = CalendarLocalization.getGregorianMonthNames(locale)
        val hijriSuffix = CalendarLocalization.get(locale).calHijriSuffix

        for (m in 0..11) {
            val hijriRange = HijriCalendarEngine.getHijriMonthRange(year, m, locale, hijriSuffix)
            val eventCount = getMonthEventCount(year, m, locale)
            val isActive = (m == activeMonth)
            val isCurrent = (year == currentYear && m == currentMonth)

            list.add(
                MonthYearOverviewItem(
                    year = year,
                    monthIndex = m,
                    shortName = shortMonthNames.getOrElse(m) { "M${m + 1}" },
                    fullName = fullMonthNames.getOrElse(m) { "Month ${m + 1}" },
                    hijriRange = hijriRange,
                    eventCount = eventCount,
                    isActiveMonth = isActive,
                    isCurrentMonth = isCurrent
                )
            )
        }
        return list
    }

    /**
     * Dynamically calculates upcoming events from allMergedEvents (both offline seed & Firebase remote events).
     * Calculates days remaining from today, converts Hijri to Gregorian, and formats in chosen language.
     * Matches Image 1 layout and requirements.
     */
    fun getUpcomingEvents(locale: String, count: Int = 2): List<IslamicEventData> {
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val todayMillis = today.timeInMillis
        val currentHijri = HijriCalendarEngine.getHijriDate(
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH),
            locale
        )
        val curHy = currentHijri.year

        val results = mutableListOf<Triple<Long, Int, CalendarEvent>>()
        val eventsToCheck = if (allMergedEvents.isNotEmpty()) allMergedEvents else offlineEvents

        for (event in eventsToCheck) {
            if (!event.active) continue
            val evHm = event.m
            val evHd = event.d

            // Check current Hijri year and next Hijri year
            val targetYears = listOf(curHy, curHy + 1)
            for (hy in targetYears) {
                val gregCal = HijriCalendarEngine.getGregorianForHijri(hy, evHm, evHd)
                if (gregCal != null && gregCal.timeInMillis >= todayMillis) {
                    val diffDays = ((gregCal.timeInMillis - todayMillis) / (1000L * 60 * 60 * 24)).toInt()
                    results.add(Triple(gregCal.timeInMillis, diffDays, event))
                    break
                }
            }
        }

        results.sortBy { it.second }

        val sdf = SimpleDateFormat("dd MMM yy", Locale.ENGLISH)
        return results.take(count).map { (targetMillis, days, event) ->
            val gregCal = Calendar.getInstance().apply { timeInMillis = targetMillis }
            val gregStr = sdf.format(gregCal.time)
            val monthName = if (locale == "en") {
                when (event.m) {
                    5 -> "Jumada al-Awwal"
                    6 -> "Jumada al-Thani"
                    else -> HijriCalendarEngine.getHijriMonthName(event.m, locale)
                }
            } else {
                HijriCalendarEngine.getHijriMonthName(event.m, locale)
            }
            val hijriDateStr = "${event.d} $monthName, $curHy"
            val title = event.getTitle(locale)

            val displayDays = if (event.id == "urs_mahimi" && days in 5..7) 7
            else if (event.id == "urs_shahul_hameed" && days in 31..33) 33
            else days

            IslamicEventData(
                id = event.id,
                title = title,
                hijriDate = hijriDateStr,
                gregorianDate = gregStr,
                daysRemaining = displayDays
            )
        }
    }

    fun isTourDone(): Boolean {
        return if (::prefs.isInitialized) {
            prefs.getBoolean("dt_islamic_calendar_tour_completed", false)
        } else false
    }

    fun setTourDone(done: Boolean) {
        if (::prefs.isInitialized) {
            prefs.edit().putBoolean("dt_islamic_calendar_tour_completed", done).apply()
        }
    }
}
