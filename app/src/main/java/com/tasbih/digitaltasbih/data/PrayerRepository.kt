package com.tasbih.digitaltasbih.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import com.tasbih.digitaltasbih.receivers.PrayerAlarmReceiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
import kotlin.math.*

object PrayerRepository {
    private const val TAG = "PrayerRepository"
    private const val PREFS_NAME = "dt_prayer_prefs"
    private var prefs: SharedPreferences? = null

    // Cache of month data in memory
    private val monthCache = mutableMapOf<String, List<PrayerDayData>>()
    private var quotesMap: Map<String, Map<String, List<Pair<String, String>>>>? = null
    private var appContext: Context? = null

    fun init(context: Context) {
        appContext = context.applicationContext
        if (prefs == null) {
            prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }
        loadQuotesJson(context.applicationContext)
    }

    private fun loadQuotesJson(context: Context) {
        if (quotesMap != null) return
        try {
            val jsonStr = context.assets.open("prayer_quotes.json").bufferedReader().use { it.readText() }
            val root = JSONObject(jsonStr)
            val result = mutableMapOf<String, Map<String, List<Pair<String, String>>>>()
            val langKeys = root.keys()
            while (langKeys.hasNext()) {
                val lang = langKeys.next()
                val langObj = root.optJSONObject(lang) ?: continue
                val prayerMap = mutableMapOf<String, List<Pair<String, String>>>()
                val prayerKeys = langObj.keys()
                while (prayerKeys.hasNext()) {
                    val prayerKey = prayerKeys.next()
                    val arr = langObj.optJSONArray(prayerKey) ?: continue
                    val qList = mutableListOf<Pair<String, String>>()
                    for (i in 0 until arr.length()) {
                        val item = arr.optJSONObject(i) ?: continue
                        val title = item.optString("title", "")
                        val body = item.optString("body", "")
                        if (title.isNotEmpty()) {
                            qList.add(Pair(title, body))
                        }
                    }
                    prayerMap[prayerKey.lowercase()] = qList
                }
                result[lang.lowercase()] = prayerMap
            }
            quotesMap = result
            Log.d(TAG, "Loaded ${result.size} languages from prayer_quotes.json")
        } catch (e: Exception) {
            Log.e(TAG, "Error loading prayer_quotes.json", e)
        }
    }

    // ══════════════════════════════════════════════════════════════
    //  SETTINGS MANAGEMENT
    // ══════════════════════════════════════════════════════════════
    fun loadSettings(): PrayerSettingsState {
        val p = prefs ?: return PrayerSettingsState()
        return PrayerSettingsState(
            autoLocation = p.getBoolean("pr_auto_loc", true),
            latitude = p.getFloat("pr_lat", 21.0970f).toDouble(),
            longitude = p.getFloat("pr_lon", 81.0374f).toDouble(),
            locationLabel = p.getString("pr_loc_label", "Rajnandgaon, India") ?: "Rajnandgaon, India",
            methodId = p.getInt("pr_method_id", 1),
            school = p.getInt("pr_school", 2),
            tune = PrayerTune(
                fajr = p.getInt("pr_tune_fajr", 0),
                sunrise = p.getInt("pr_tune_sunrise", 0),
                dhuhr = p.getInt("pr_tune_dhuhr", 0),
                asr = p.getInt("pr_tune_asr", 0),
                maghrib = p.getInt("pr_tune_maghrib", 0),
                sunset = p.getInt("pr_tune_sunset", 0),
                isha = p.getInt("pr_tune_isha", 0),
                imsak = p.getInt("pr_tune_imsak", 0)
            ),
            notificationsEnabled = p.getBoolean("pr_notif_enabled", true),
            warningEnabled = p.getBoolean("pr_warn_enabled", true),
            fixedNotificationEnabled = p.getBoolean("pr_fixed_notif_enabled", true),
            soundFajr = p.getBoolean("pr_snd_fajr", true),
            soundDhuhr = p.getBoolean("pr_snd_dhuhr", true),
            soundAsr = p.getBoolean("pr_snd_asr", true),
            soundMaghrib = p.getBoolean("pr_snd_maghrib", true),
            soundIsha = p.getBoolean("pr_snd_isha", true)
        )
    }

    fun saveSettings(s: PrayerSettingsState) {
        prefs?.edit()
            ?.putBoolean("pr_auto_loc", s.autoLocation)
            ?.putFloat("pr_lat", s.latitude.toFloat())
            ?.putFloat("pr_lon", s.longitude.toFloat())
            ?.putString("pr_loc_label", s.locationLabel)
            ?.putInt("pr_method_id", s.methodId)
            ?.putInt("pr_school", s.school)
            ?.putInt("pr_tune_fajr", s.tune.fajr)
            ?.putInt("pr_tune_sunrise", s.tune.sunrise)
            ?.putInt("pr_tune_dhuhr", s.tune.dhuhr)
            ?.putInt("pr_tune_asr", s.tune.asr)
            ?.putInt("pr_tune_maghrib", s.tune.maghrib)
            ?.putInt("pr_tune_sunset", s.tune.sunset)
            ?.putInt("pr_tune_isha", s.tune.isha)
            ?.putInt("pr_tune_imsak", s.tune.imsak)
            ?.putBoolean("pr_notif_enabled", s.notificationsEnabled)
            ?.putBoolean("pr_warn_enabled", s.warningEnabled)
            ?.putBoolean("pr_fixed_notif_enabled", s.fixedNotificationEnabled)
            ?.putBoolean("pr_snd_fajr", s.soundFajr)
            ?.putBoolean("pr_snd_dhuhr", s.soundDhuhr)
            ?.putBoolean("pr_snd_asr", s.soundAsr)
            ?.putBoolean("pr_snd_maghrib", s.soundMaghrib)
            ?.putBoolean("pr_snd_isha", s.soundIsha)
            ?.apply()

        monthCache.clear() // Clear cache so changes reflect
        appContext?.let { scheduleAllPrayerNotifications(it) }
    }

    fun isDarkTheme(): Boolean {
        return prefs?.getBoolean("dt_app_dark_theme", false) ?: false
    }

    fun setDarkTheme(isDark: Boolean) {
        prefs?.edit()?.putBoolean("dt_app_dark_theme", isDark)?.apply()
    }

    fun getLanguage(): String {
        return prefs?.getString("pr_selected_lang", "hi") ?: "hi"
    }

    fun saveLanguage(lang: String) {
        prefs?.edit()?.putString("pr_selected_lang", lang)?.apply()
        appContext?.let { scheduleAllPrayerNotifications(it) }
    }

    fun isPrayerTourDone(): Boolean {
        return prefs?.getBoolean("dt_prayer_tour_completed_v2", false) ?: false
    }

    fun setPrayerTourDone(done: Boolean = true) {
        prefs?.edit()?.putBoolean("dt_prayer_tour_completed_v2", done)?.apply()
    }

    fun resetPrayerTour() {
        setPrayerTourDone(false)
    }

    fun isOnboardingLangSelected(): Boolean {
        return prefs?.getBoolean("dt_onboarding_lang_selected", false) ?: false
    }

    fun setOnboardingLangSelected(selected: Boolean) {
        prefs?.edit()?.putBoolean("dt_onboarding_lang_selected", selected)?.apply()
    }

    fun getExtraLangSlot(): String {
        return prefs?.getString("dt_extra_lang_slot", "bn") ?: "bn"
    }

    fun setExtraLangSlot(lang: String) {
        prefs?.edit()?.putString("dt_extra_lang_slot", lang)?.apply()
    }

    fun isFirstPermissionsRequested(): Boolean {
        return prefs?.getBoolean("dt_first_permissions_requested", false) ?: false
    }

    fun setFirstPermissionsRequested(requested: Boolean = true) {
        prefs?.edit()?.putBoolean("dt_first_permissions_requested", requested)?.apply()
    }

    fun getLocationDeniedLastTime(): Long {
        return prefs?.getLong("dt_loc_denied_last_time", 0L) ?: 0L
    }

    fun setLocationDeniedLastTime(timeMs: Long = System.currentTimeMillis()) {
        prefs?.edit()?.putLong("dt_loc_denied_last_time", timeMs)?.apply()
    }

    fun getNotificationDeniedLastTime(): Long {
        return prefs?.getLong("dt_notif_denied_last_time", 0L) ?: 0L
    }

    fun setNotificationDeniedLastTime(timeMs: Long = System.currentTimeMillis()) {
        prefs?.edit()?.putLong("dt_notif_denied_last_time", timeMs)?.apply()
    }

    fun togglePrayerSound(prayerId: String): Boolean {
        val s = loadSettings()
        val newState = when (prayerId.lowercase()) {
            "fajr" -> s.copy(soundFajr = !s.soundFajr)
            "dhuhr" -> s.copy(soundDhuhr = !s.soundDhuhr)
            "asr" -> s.copy(soundAsr = !s.soundAsr)
            "maghrib" -> s.copy(soundMaghrib = !s.soundMaghrib)
            "isha" -> s.copy(soundIsha = !s.soundIsha)
            else -> s
        }
        saveSettings(newState)
        return when (prayerId.lowercase()) {
            "fajr" -> newState.soundFajr
            "dhuhr" -> newState.soundDhuhr
            "asr" -> newState.soundAsr
            "maghrib" -> newState.soundMaghrib
            "isha" -> newState.soundIsha
            else -> true
        }
    }

    // ══════════════════════════════════════════════════════════════
    //  JAMAT / IQAMAH TIME CUSTOMIZATION
    // ══════════════════════════════════════════════════════════════
    fun getJamatTime(prayerId: String, defaultAzanTime: String = "00:00"): String {
        val p = prefs ?: return defaultAzanTime
        val custom = p.getString("jamat_${prayerId.lowercase()}", null)
        return if (!custom.isNullOrBlank()) custom else defaultAzanTime
    }

    fun isJamatCustomized(prayerId: String): Boolean {
        val p = prefs ?: return false
        val custom = p.getString("jamat_${prayerId.lowercase()}", null)
        return !custom.isNullOrBlank()
    }

    // ══════════════════════════════════════════════════════════════════════════════
    //  JAMAT / IQAMAH TIME CUSTOMIZATION & 30-DAY REVIEW
    // ══════════════════════════════════════════════════════════════════════════════
    private const val KEY_JAMAT_LAST_UPDATED = "jamat_last_updated_time"
    private const val KEY_JAMAT_REVIEW_SNOOZE = "jamat_review_snooze_until"
    private const val THIRTY_DAYS_MS = 30L * 24 * 60 * 60 * 1000L
    private const val FIFTEEN_DAYS_MS = 15L * 24 * 60 * 60 * 1000L

    fun getLastJamatUpdateTime(): Long {
        val p = prefs ?: return System.currentTimeMillis()
        val t = p.getLong(KEY_JAMAT_LAST_UPDATED, 0L)
        if (t == 0L) {
            val now = System.currentTimeMillis()
            p.edit().putLong(KEY_JAMAT_LAST_UPDATED, now).apply()
            return now
        }
        return t
    }

    fun setLastJamatUpdateTime(timeMs: Long) {
        prefs?.edit()?.putLong(KEY_JAMAT_LAST_UPDATED, timeMs)?.apply()
    }

    fun shouldShowJamat30DayReview(): Boolean {
        val p = prefs ?: return false
        val now = System.currentTimeMillis()

        // If snoozed by 'Cancel (Remind in 15 Days)', check if snooze duration is still active
        val snoozeUntil = p.getLong(KEY_JAMAT_REVIEW_SNOOZE, 0L)
        if (snoozeUntil > 0L && now < snoozeUntil) {
            return false
        }

        val lastUpdate = p.getLong(KEY_JAMAT_LAST_UPDATED, 0L)
        if (lastUpdate == 0L) {
            // First time running: initialize baseline to now
            p.edit().putLong(KEY_JAMAT_LAST_UPDATED, now).apply()
            return false
        }

        return (now - lastUpdate) >= THIRTY_DAYS_MS
    }

    fun keepJamatCurrentFor30Days() {
        val p = prefs ?: return
        val now = System.currentTimeMillis()
        p.edit()
            .putLong(KEY_JAMAT_LAST_UPDATED, now)
            .remove(KEY_JAMAT_REVIEW_SNOOZE)
            .apply()
    }

    fun snoozeJamatReviewFor15Days() {
        val p = prefs ?: return
        val now = System.currentTimeMillis()
        p.edit()
            .putLong(KEY_JAMAT_REVIEW_SNOOZE, now + FIFTEEN_DAYS_MS)
            .apply()
    }

    fun markJamatUpdatedNow() {
        val p = prefs ?: return
        val now = System.currentTimeMillis()
        p.edit()
            .putLong(KEY_JAMAT_LAST_UPDATED, now)
            .remove(KEY_JAMAT_REVIEW_SNOOZE)
            .apply()
    }

    fun triggerTestJamat30DayReview() {
        val p = prefs ?: return
        val past31Days = System.currentTimeMillis() - (31L * 24 * 60 * 60 * 1000L)
        p.edit()
            .putLong(KEY_JAMAT_LAST_UPDATED, past31Days)
            .remove(KEY_JAMAT_REVIEW_SNOOZE)
            .apply()
    }

    fun setJamatTime(prayerId: String, timeStr: String) {
        prefs?.edit()
            ?.putString("jamat_${prayerId.lowercase()}", timeStr)
            ?.putLong(KEY_JAMAT_LAST_UPDATED, System.currentTimeMillis())
            ?.remove(KEY_JAMAT_REVIEW_SNOOZE)
            ?.apply()
    }

    fun clearJamatTime(prayerId: String) {
        prefs?.edit()
            ?.remove("jamat_${prayerId.lowercase()}")
            ?.putLong(KEY_JAMAT_LAST_UPDATED, System.currentTimeMillis())
            ?.remove(KEY_JAMAT_REVIEW_SNOOZE)
            ?.apply()
    }

    // ══════════════════════════════════════════════════════════════
    //  SALAH TRACKER / DONE MAP & ANALYTICS
    // ══════════════════════════════════════════════════════════════
    data class SalahPeriodStats(
        val done: Int,
        val total: Int,
        val pct: Int,
        val delta: Int
    )

    fun isPrayerDone(paramA: String, paramB: String): Boolean {
        val p = prefs ?: return false
        val (dateStr, prayerId) = if (paramA.contains("-")) Pair(paramA, paramB) else Pair(paramB, paramA)
        return p.getBoolean("done_${dateStr}_${prayerId.lowercase()}", false)
    }

    fun setPrayerDone(paramA: String, paramB: String, done: Boolean = true) {
        val p = prefs ?: return
        val (dateStr, prayerId) = if (paramA.contains("-")) Pair(paramA, paramB) else Pair(paramB, paramA)
        val key = "done_${dateStr}_${prayerId.lowercase()}"
        p.edit().putBoolean(key, done).apply()
    }

    fun togglePrayerDone(paramA: String, paramB: String): Boolean {
        val p = prefs ?: return false
        val (dateStr, prayerId) = if (paramA.contains("-")) Pair(paramA, paramB) else Pair(paramB, paramA)
        val key = "done_${dateStr}_${prayerId.lowercase()}"
        val current = p.getBoolean(key, false)
        val now = !current
        p.edit().putBoolean(key, now).apply()
        return now
    }

    fun isSalahTourDone(): Boolean {
        return prefs?.getBoolean("salah_tracker_tour_done", false) ?: false
    }

    fun setSalahTourDone(done: Boolean) {
        prefs?.edit()?.putBoolean("salah_tracker_tour_done", done)?.apply()
    }

    fun getSalahGoal(): Int {
        return prefs?.getInt("dt_salah_goal", 80) ?: 80
    }

    fun setSalahGoal(goal: Int) {
        prefs?.edit()?.putInt("dt_salah_goal", goal)?.apply()
    }

    fun isAppRated(): Boolean {
        return prefs?.getBoolean("dt_app_rated", false) ?: false
    }

    fun setAppRated(rated: Boolean = true) {
        prefs?.edit()?.putBoolean("dt_app_rated", rated)?.apply()
    }

    fun getRatePromptReminderTime(): Long {
        return prefs?.getLong("dt_rate_prompt_reminder_time", 0L) ?: 0L
    }

    fun setRatePromptReminderTime(timeMs: Long) {
        prefs?.edit()?.putLong("dt_rate_prompt_reminder_time", timeMs)?.apply()
    }

    fun isFixedCountdownNotifEnabled(): Boolean {
        return prefs?.getBoolean("dt_fixed_countdown_notif", true) ?: true
    }

    fun setFixedCountdownNotifEnabled(enabled: Boolean) {
        prefs?.edit()?.putBoolean("dt_fixed_countdown_notif", enabled)?.apply()
    }

    fun isHomeAnnouncementSeen(): Boolean {
        val config = getHomeAnnouncementConfig()
        if (!config.isEnabled) return true // If turned OFF by admin, consider it seen / do not show
        return getLastSeenAnnouncementVersion() >= config.version
    }

    fun setHomeAnnouncementSeen(seen: Boolean = true) {
        val config = getHomeAnnouncementConfig()
        if (seen) {
            markAnnouncementSeen(config.version)
        } else {
            markAnnouncementSeen(0L)
        }
    }

    fun getHomeAnnouncementConfig(): HomeAnnouncementConfig {
        val p = prefs ?: return HomeAnnouncementConfig()
        return HomeAnnouncementConfig(
            isEnabled = p.getBoolean("dt_home_ann_enabled", false), // Default: OFF until Admin turns it ON!
            campaignId = p.getString("dt_home_ann_campaign_id", "") ?: "",
            badgeText = p.getString("dt_home_ann_badge", "✨ SPECIAL EVENT LIVE") ?: "✨ SPECIAL EVENT LIVE",
            arabicText = p.getString("dt_home_ann_arabic", "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ") ?: "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
            title = p.getString("dt_home_ann_title", "") ?: "",
            prizeTitle = p.getString("dt_home_ann_prize", "") ?: "",
            message = p.getString("dt_home_ann_msg", "") ?: "",
            targetText = p.getString("dt_home_ann_target", "") ?: "",
            buttonText = p.getString("dt_home_ann_btn", "Alhamdulillah! Shamil Huein ✓") ?: "Alhamdulillah! Shamil Huein ✓",
            version = p.getLong("dt_home_ann_version", 0L)
        )
    }

    fun saveHomeAnnouncementConfig(config: HomeAnnouncementConfig) {
        prefs?.edit()?.apply {
            putBoolean("dt_home_ann_enabled", config.isEnabled)
            putString("dt_home_ann_campaign_id", config.campaignId)
            putString("dt_home_ann_badge", config.badgeText)
            putString("dt_home_ann_arabic", config.arabicText)
            putString("dt_home_ann_title", config.title)
            putString("dt_home_ann_prize", config.prizeTitle)
            putString("dt_home_ann_msg", config.message)
            putString("dt_home_ann_target", config.targetText)
            putString("dt_home_ann_btn", config.buttonText)
            putLong("dt_home_ann_version", config.version)
            apply()
        }
    }

    fun getLastSeenAnnouncementVersion(): Long {
        return prefs?.getLong("dt_home_ann_last_seen_version", 0L) ?: 0L
    }

    fun markAnnouncementSeen(version: Long) {
        prefs?.edit()?.putLong("dt_home_ann_last_seen_version", version)?.apply()
    }

    fun salahDayCount(dateStr: String): Int {
        var done = 0
        val prayers = listOf("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha")
        for (pId in prayers) {
            if (isPrayerDone(pId, dateStr)) done++
        }
        return done
    }

    fun salahDayCount(cal: Calendar): Int {
        val dStr = String.format(Locale.US, "%02d-%02d-%04d", cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR))
        return salahDayCount(dStr)
    }

    fun salahStreak(): Int {
        val today = Calendar.getInstance()
        val todayDone = salahDayCount(today)
        val startOffset = if (todayDone >= 5) 0 else 1
        var streak = 0
        val checkCal = Calendar.getInstance()
        for (i in startOffset until 365) {
            checkCal.time = today.time
            checkCal.add(Calendar.DAY_OF_YEAR, -i)
            if (salahDayCount(checkCal) >= 5) {
                streak++
            } else {
                break
            }
        }
        return streak
    }

    fun salahPeriodStats(period: String): SalahPeriodStats {
        val now = Calendar.getInstance()
        var done = 0
        var total = 0
        var prevDone = 0
        var prevTotal = 0

        when (period) {
            "today" -> {
                done = salahDayCount(now)
                total = 5
                val yest = (now.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, -1) }
                prevDone = salahDayCount(yest)
                prevTotal = 5
            }
            "week" -> {
                for (i in 0 until 7) {
                    val d = (now.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, -i) }
                    done += salahDayCount(d)
                }
                total = 35
                for (i in 7 until 14) {
                    val d = (now.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, -i) }
                    prevDone += salahDayCount(d)
                }
                prevTotal = 35
            }
            "month" -> {
                val currentDay = now.get(Calendar.DAY_OF_MONTH)
                for (d in 1..currentDay) {
                    val dt = (now.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, d) }
                    done += salahDayCount(dt)
                }
                total = currentDay * 5

                val prevMonth = (now.clone() as Calendar).apply {
                    add(Calendar.MONTH, -1)
                }
                val prevMonthDays = prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
                val compareDays = minOf(currentDay, prevMonthDays)
                for (d in 1..compareDays) {
                    val dt = (prevMonth.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, d) }
                    prevDone += salahDayCount(dt)
                }
                prevTotal = compareDays * 5
            }
            "year" -> {
                val dayOfYear = now.get(Calendar.DAY_OF_YEAR)
                val startOfYear = (now.clone() as Calendar).apply {
                    set(Calendar.MONTH, 0)
                    set(Calendar.DAY_OF_MONTH, 1)
                }
                for (i in 0 until dayOfYear) {
                    val dt = (startOfYear.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, i) }
                    done += salahDayCount(dt)
                }
                total = dayOfYear * 5

                val startOfPrevYear = (now.clone() as Calendar).apply {
                    add(Calendar.YEAR, -1)
                    set(Calendar.MONTH, 0)
                    set(Calendar.DAY_OF_MONTH, 1)
                }
                for (i in 0 until dayOfYear) {
                    val dt = (startOfPrevYear.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, i) }
                    prevDone += salahDayCount(dt)
                }
                prevTotal = dayOfYear * 5
            }
            else -> { // "all"
                val allKeys = prefs?.all?.keys ?: emptySet()
                val dateKeys = allKeys.filter { it.startsWith("done_") }.mapNotNull {
                    val parts = it.split("_")
                    if (parts.size >= 2) parts[1] else null
                }.toSet()

                val totalDays = maxOf(1, dateKeys.size)
                total = totalDays * 5
                for (dKey in dateKeys) {
                    done += salahDayCount(dKey)
                }
                prevDone = (done * 0.85).toInt()
                prevTotal = total
            }
        }

        val pct = if (total > 0) ((done * 100) / total) else 0
        val prevPct = if (prevTotal > 0) ((prevDone * 100) / prevTotal) else 0
        val delta = if (prevTotal > 0) (pct - prevPct) else 0
        return SalahPeriodStats(done = done, total = total, pct = pct, delta = delta)
    }

    // ══════════════════════════════════════════════════════════════
    //  ALADHAN API & MONTH CALENDAR FETCH
    // ══════════════════════════════════════════════════════════════
    private fun tuneParamString(t: PrayerTune): String {
        // Imsak,Fajr,Sunrise,Dhuhr,Asr,Maghrib,Sunset,Isha,Midnight
        return "${t.imsak},${t.fajr},${t.sunrise},${t.dhuhr},${t.asr},${t.maghrib},${t.sunset},${t.isha},0"
    }

    suspend fun fetchMonthPrayerTimes(year: Int, month: Int): List<PrayerDayData> = withContext(Dispatchers.IO) {
        val s = loadSettings()
        val cacheKey = "month_${year}_${month}_${s.latitude}_${s.longitude}_${s.methodId}_${s.school}"
        monthCache[cacheKey]?.let { return@withContext it }

        // Check local disk cache
        val diskJson = prefs?.getString(cacheKey, null)
        if (diskJson != null) {
            val list = parseAladhanCalendarJson(diskJson, s)
            if (list.isNotEmpty()) {
                monthCache[cacheKey] = list
                return@withContext list
            }
        }

        // Fetch from Aladhan API
        try {
            val tuneStr = tuneParamString(s.tune)
            val urlStr = "https://api.aladhan.com/v1/calendar" +
                    "?latitude=${s.latitude}&longitude=${s.longitude}" +
                    "&method=${s.methodId}&school=${s.school}" +
                    "&month=$month&year=$year&tune=$tuneStr"

            val url = URL(urlStr)
            val conn = url.openConnection() as HttpURLConnection
            conn.connectTimeout = 8000
            conn.readTimeout = 8000
            conn.requestMethod = "GET"

            if (conn.responseCode == 200) {
                val reader = BufferedReader(InputStreamReader(conn.inputStream))
                val sb = java.lang.StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    sb.append(line)
                }
                reader.close()
                val jsonStr = sb.toString()
                val list = parseAladhanCalendarJson(jsonStr, s)
                if (list.isNotEmpty()) {
                    prefs?.edit()?.putString(cacheKey, jsonStr)?.apply()
                    monthCache[cacheKey] = list
                    return@withContext list
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Aladhan API network error, falling back to offline engine: ${e.message}")
        }

        // Fallback to offline astronomical calculation for entire month
        val offlineList = computeOfflineMonth(year, month, s)
        monthCache[cacheKey] = offlineList
        offlineList
    }

    suspend fun fetchTodayPrayerTimes(): PrayerDayData = withContext(Dispatchers.IO) {
        val cal = Calendar.getInstance()
        val y = cal.get(Calendar.YEAR)
        val m = cal.get(Calendar.MONTH) + 1
        val d = cal.get(Calendar.DAY_OF_MONTH)

        val monthData = fetchMonthPrayerTimes(y, m)
        val today = monthData.find { it.dayOfMonth == d }
        today ?: computeOfflineDay(cal.time, loadSettings())
    }

    private fun parseAladhanCalendarJson(jsonStr: String, s: PrayerSettingsState): List<PrayerDayData> {
        val result = mutableListOf<PrayerDayData>()
        try {
            val root = JSONObject(jsonStr)
            val dataArr = root.optJSONArray("data") ?: return emptyList()

            for (i in 0 until dataArr.length()) {
                val dayObj = dataArr.getJSONObject(i)
                val timingsObj = dayObj.optJSONObject("timings") ?: continue
                val dateObj = dayObj.optJSONObject("date") ?: continue
                val gregObj = dateObj.optJSONObject("gregorian") ?: continue
                val hijriObj = dateObj.optJSONObject("hijri") ?: continue

                val gregDate = gregObj.optString("date", "") // "15-10-2026"
                val dayNum = gregObj.optInt("day", i + 1)
                val mNum = gregObj.optJSONObject("month")?.optInt("number", 10) ?: 10
                val yNum = gregObj.optInt("year", 2026)
                val readable = gregObj.optString("readable", "$dayNum Oct $yNum")

                val hDay = hijriObj.optInt("day", 1)
                val hMonthEn = hijriObj.optJSONObject("month")?.optString("en", "Jumada al-Awwal") ?: "Jumada al-Awwal"
                val hMonthAr = hijriObj.optJSONObject("month")?.optString("ar", "جُمَادَىٰ الأُولَىٰ") ?: "جُمَادَىٰ الأُولَىٰ"
                val hYear = hijriObj.optInt("year", 1448)

                val map = mutableMapOf<String, String>()
                val keys = listOf("Fajr", "Sunrise", "Dhuhr", "Asr", "Sunset", "Maghrib", "Isha", "Imsak", "Midnight")
                for (k in keys) {
                    val raw = timingsObj.optString(k, "00:00")
                    map[k] = cleanTimeString(raw)
                }

                val dayData = PrayerDayData(
                    dateStr = gregDate,
                    dayOfMonth = dayNum,
                    monthNumber = mNum,
                    year = yNum,
                    hijriDay = hDay,
                    hijriMonthName = hMonthEn,
                    hijriMonthAr = hMonthAr,
                    hijriYear = hYear,
                    gregorianReadable = readable,
                    timings = map
                )
                val withPhases = dayData.copy(extraPhases = computeExtraPhases(dayData))
                result.add(withPhases)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing Aladhan JSON: ${e.message}")
        }
        return result
    }

    private fun cleanTimeString(raw: String): String {
        // e.g. "05:12 (IST)" -> "05:12"
        val trimmed = raw.trim()
        val spaceIdx = trimmed.indexOf(' ')
        val clean = if (spaceIdx != -1) trimmed.substring(0, spaceIdx) else trimmed
        return if (clean.length >= 5) clean.substring(0, 5) else clean
    }

    // ══════════════════════════════════════════════════════════════
    //  OFFLINE ASTRONOMICAL CALCULATION ENGINE (Exact from index.html)
    // ══════════════════════════════════════════════════════════════
    fun computeOfflineMonth(year: Int, month: Int, s: PrayerSettingsState): List<PrayerDayData> {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month - 1)
        val maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        val list = mutableListOf<PrayerDayData>()
        for (day in 1..maxDays) {
            cal.set(Calendar.DAY_OF_MONTH, day)
            list.add(computeOfflineDay(cal.time, s))
        }
        return list
    }

    fun computeOfflineDay(d: Date, s: PrayerSettingsState): PrayerDayData {
        val cal = Calendar.getInstance().apply { time = d }
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val lat = if (abs(s.latitude) >= 0.1) s.latitude else 28.6139
        val lon = if (abs(s.longitude) >= 0.1) s.longitude else 77.2090

        val rad = Math.PI / 180.0
        val deg = 180.0 / Math.PI

        val N = floor(275.0 * month / 9.0) - floor((month + 9.0) / 12.0) * (1.0 + floor((year - 4.0 * floor(year / 4.0) + 2.0) / 3.0)) + day - 30.0
        val lngHour = lon / 15.0
        val t = N + ((6.0 - lngHour) / 24.0)
        val M = (0.9856 * t) - 3.289
        val L = (M + (1.916 * sin(M * rad)) + (0.020 * sin(2.0 * M * rad)) + 282.634) % 360.0
        var RA = (deg * atan(0.91764 * tan(L * rad))) % 360.0
        if (RA < 0) RA += 360.0
        val Lquadrant = floor(L / 90.0) * 90.0
        val RAquadrant = floor(RA / 90.0) * 90.0
        RA = (RA + (Lquadrant - RAquadrant)) / 15.0

        val sinDec = 0.39782 * sin(L * rad)
        val cosDec = cos(asin(sinDec))
        val tz = cal.timeZone.getOffset(d.time) / (1000.0 * 60.0 * 60.0)

        fun hourAngle(alt: Double, isMorning: Boolean): Double {
            val cosH = (sin(alt * rad) - (sin(lat * rad) * sinDec)) / (cos(lat * rad) * cosDec)
            if (cosH > 1.0) return 0.0
            if (cosH < -1.0) return 12.0
            val H = deg * acos(cosH) / 15.0
            return if (isMorning) (24.0 - H) else H
        }

        val fajrAngle = if (s.methodId == 2) -15.0 else if (s.methodId == 4) -18.5 else -18.0
        val ishaAngle = if (s.methodId == 2) -15.0 else if (s.methodId == 4) -18.5 else -18.0

        val fajrHA = hourAngle(fajrAngle, true)
        val sunriseHA = hourAngle(-0.833, true)
        val sunsetHA = hourAngle(-0.833, false)
        val ishaHA = hourAngle(ishaAngle, false)

        val asrFactor = if (s.school == 1) 1.0 else 2.0
        val noonZenith = abs(lat - (deg * asin(sinDec)))
        val asrAlt = deg * atan(1.0 / (asrFactor + tan(noonZenith * rad)))
        val asrHA = hourAngle(asrAlt, false)

        val b = (360.0 / 365.0) * (N - 81.0) * rad
        val eot = (9.87 * sin(2.0 * b) - 7.53 * cos(b) - 1.5 * sin(b)) / 60.0
        val noon = 12.0 + tz - (lon / 15.0) - eot

        fun toTimeString(hours: Double, tuneMin: Int): String {
            var totalMins = (round((hours % 24.0) * 60.0) + tuneMin).toInt()
            while (totalMins < 0) totalMins += 1440
            totalMins %= 1440
            val h = totalMins / 60
            val m = totalMins % 60
            return String.format(Locale.US, "%02d:%02d", h, m)
        }

        val fajrH = noon - (24.0 - fajrHA)
        val sunrH = noon - (24.0 - sunriseHA)
        val asrH = noon + asrHA
        val maghH = noon + sunsetHA
        val ishaH = if (s.methodId == 4) (maghH + 1.5) else (noon + ishaHA)

        val dateStr = String.format(Locale.US, "%02d-%02d-%04d", day, month, year)
        val timings = mapOf(
            "Fajr" to toTimeString(fajrH, s.tune.fajr),
            "Sunrise" to toTimeString(sunrH, s.tune.sunrise),
            "Dhuhr" to toTimeString(noon, s.tune.dhuhr),
            "Asr" to toTimeString(asrH, s.tune.asr),
            "Sunset" to toTimeString(maghH, s.tune.sunset),
            "Maghrib" to toTimeString(maghH, s.tune.maghrib),
            "Isha" to toTimeString(ishaH, s.tune.isha),
            "Imsak" to toTimeString(fajrH - 0.16, s.tune.imsak)
        )

        // Offline Hijri approximation
        val (hDay, hMonth, hYear) = approximateHijri(cal)

        val dayData = PrayerDayData(
            dateStr = dateStr,
            dayOfMonth = day,
            monthNumber = month,
            year = year,
            hijriDay = hDay,
            hijriMonthName = hMonth,
            hijriMonthAr = "",
            hijriYear = hYear,
            gregorianReadable = SimpleDateFormat("d MMMM yyyy", Locale.US).format(d),
            timings = timings
        )
        return dayData.copy(extraPhases = computeExtraPhases(dayData))
    }

    fun approximateHijri(cal: Calendar, lang: String = "en"): Triple<Int, String, Int> {
        val islamicMonthsEn = listOf(
            "Muharram", "Safar", "Rabi' al-Awwal", "Rabi' al-Thani",
            "Jamadi ul Awwal", "Jamadi ul Thani", "Rajab", "Sha'ban",
            "Ramadan", "Shawwal", "Dhu al-Qi'dah", "Dhu al-Hijjah"
        )
        val islamicMonthsHi = listOf(
            "मुहर्रम", "सफ़र", "रबीउल अव्वल", "रबीउस सानी",
            "जमादिउल अव्वल", "जमादिउस सानी", "रजब", "शाबान",
            "रमज़ान", "शव्वाल", "ज़ुल क़ादा", "ज़ुल हिज्जा"
        )
        val islamicMonthsUr = listOf(
            "محرم", "صفر", "ربیع الاول", "ربیع الثانی",
            "جمادی الاول", "جمادی الثانی", "رجب", "شعبان",
            "رمضان", "شوال", "ذوالقعدہ", "ذوالحجہ"
        )
        val islamicMonths = when (lang.lowercase().split("-")[0]) {
            "hi" -> islamicMonthsHi
            "ur" -> islamicMonthsUr
            else -> islamicMonthsEn
        }
        // Offset reference: 15 Oct 2026 is 2 Jamadi ul Awwal 1448 AH
        val baseMillis = 1792041600000L // 15-10-2026
        val diffDays = ((cal.timeInMillis - baseMillis) / (1000 * 60 * 60 * 24)).toInt()
        var hDay = 2 + diffDays
        var hMonthIdx = 4 // Jamadi ul Awwal (0-indexed)
        var hYear = 1448
        while (hDay > 30) {
            hDay -= 30
            hMonthIdx = (hMonthIdx + 1) % 12
            if (hMonthIdx == 0) hYear++
        }
        while (hDay < 1) {
            hDay += 30
            hMonthIdx = (hMonthIdx - 1 + 12) % 12
            if (hMonthIdx == 11) hYear--
        }
        return Triple(hDay, islamicMonths[hMonthIdx], hYear)
    }

    fun approximateHijri(cal: Calendar): Triple<Int, String, Int> = approximateHijri(cal, "en")

    // ══════════════════════════════════════════════════════════════
    //  EXTRA PHASES (Makrooh, Ishraq, Tahajjud)
    // ══════════════════════════════════════════════════════════════
    fun computeExtraPhases(day: PrayerDayData): List<PrayerExtraPhase> {
        val fajrTime = day.timings["Fajr"] ?: "05:00"
        val sunriseTime = day.timings["Sunrise"] ?: "06:15"
        val dhuhrTime = day.timings["Dhuhr"] ?: "12:00"
        val maghribTime = day.timings["Maghrib"] ?: "17:45"
        val ishaTime = day.timings["Isha"] ?: "19:00"

        // 1. Tulu (Sunrise forbidden window: Sunrise to Sunrise + 20 mins)
        val tuluStart = sunriseTime
        val tuluEnd = addMinutesToTime(sunriseTime, 20)

        // 2. Ishraq & Chasht (Sunrise + 20 mins to Solar Noon - 10 mins)
        val ishraqStart = tuluEnd
        val ishraqEnd = addMinutesToTime(dhuhrTime, -10)

        // 3. Zawal (Solar noon forbidden: Dhuhr - 10 mins to Dhuhr)
        val zawalStart = addMinutesToTime(dhuhrTime, -10)
        val zawalEnd = dhuhrTime

        // 4. Ghurub (Sunset forbidden: Maghrib - 15 mins to Maghrib)
        val ghurubStart = addMinutesToTime(maghribTime, -15)
        val ghurubEnd = maghribTime

        // 5. Tahajjud (Last 1/3 of the night between Maghrib and next Fajr)
        val tahajjudStart = addMinutesToTime(fajrTime, -180) // approx 3h before Fajr
        val tahajjudEnd = fajrTime

        return listOf(
            PrayerExtraPhase("tulu", "Tulu (Sunrise)", "तुलू (सूर्योदय)", "طلوع آفتاب", "الشروق", tuluStart, tuluEnd, "forbidden"),
            PrayerExtraPhase("ishraq", "Ishraq & Chasht", "इशराक़ व चाश्त", "اشراق و چاشت", "الإشراق", ishraqStart, ishraqEnd, "sunnah"),
            PrayerExtraPhase("zawal", "Zawal (Solar Noon)", "ज़वाल (दोपहर)", "زوال آفتاب", "الزوال", zawalStart, zawalEnd, "forbidden"),
            PrayerExtraPhase("ghurub", "Ghurub (Sunset)", "ग़ुरूब (सूर्यास्त)", "غروب آفتاب", "الغروب", ghurubStart, ghurubEnd, "forbidden"),
            PrayerExtraPhase("tahajjud", "Tahajjud", "तहज्जुद", "تہجد", "التهجد", tahajjudStart, tahajjudEnd, "tahajjud")
        )
    }

    private fun addMinutesToTime(time: String, minutes: Int): String {
        val parts = time.split(":")
        if (parts.size < 2) return time
        val h = parts[0].trim().toIntOrNull() ?: 0
        val m = parts[1].trim().toIntOrNull() ?: 0
        var total = h * 60 + m + minutes
        while (total < 0) total += 1440
        total %= 1440
        return String.format(Locale.US, "%02d:%02d", total / 60, total % 60)
    }

    fun to12HourFormat(time24: String): String {
        val parts = time24.split(":")
        if (parts.size < 2) return time24
        val h = parts[0].trim().toIntOrNull() ?: 0
        val m = parts[1].trim().toIntOrNull() ?: 0
        val ampm = if (h >= 12) "pm" else "am"
        val h12 = if (h % 12 == 0) 12 else h % 12
        return String.format(Locale.US, "%d:%02d %s", h12, m, ampm)
    }

    // ══════════════════════════════════════════════════════════════
    //  ACTIVE PRAYER & NEXT COUNTDOWN META
    // ══════════════════════════════════════════════════════════════
    fun getActiveMeta(day: PrayerDayData, lang: String): PrayerActiveMeta {
        val now = Calendar.getInstance()
        val nowMs = now.timeInMillis

        fun parseToCalendar(timeStr: String?, baseCal: Calendar): Calendar? {
            if (timeStr.isNullOrBlank()) return null
            val clean = timeStr.trim().split(" ")[0]
            val parts = clean.split(":")
            if (parts.size < 2) return null
            val h = parts[0].trim().toIntOrNull() ?: return null
            val m = parts[1].trim().toIntOrNull() ?: return null
            val c = baseCal.clone() as Calendar
            c.set(Calendar.HOUR_OF_DAY, h)
            c.set(Calendar.MINUTE, m)
            c.set(Calendar.SECOND, 0)
            c.set(Calendar.MILLISECOND, 0)
            return c
        }

        val fajrCal = parseToCalendar(day.timings["Fajr"] ?: "04:30", now)
        val sunriseCal = parseToCalendar(day.timings["Sunrise"] ?: "05:46", now)
        val dhuhrCal = parseToCalendar(day.timings["Dhuhr"] ?: "12:09", now)
        val asrCal = parseToCalendar(day.timings["Asr"] ?: "16:40", now)
        val maghribCal = parseToCalendar(day.timings["Maghrib"] ?: day.timings["Sunset"] ?: "18:31", now)
        val ishaCal = parseToCalendar(day.timings["Isha"] ?: "19:48", now)

        val fajrMs = fajrCal?.timeInMillis ?: 0L
        val sunriseMs = sunriseCal?.timeInMillis ?: 0L
        val dhuhrMs = dhuhrCal?.timeInMillis ?: 0L
        val asrMs = asrCal?.timeInMillis ?: 0L
        val maghribMs = maghribCal?.timeInMillis ?: 0L
        val ishaMs = ishaCal?.timeInMillis ?: 0L

        val prayerDefs = listOf(
            Triple("Fajr", "🌅", day.timings["Fajr"] ?: "04:30"),
            Triple("Dhuhr", "☀️", day.timings["Dhuhr"] ?: "12:09"),
            Triple("Asr", "⛅", day.timings["Asr"] ?: "16:40"),
            Triple("Maghrib", "🌇", day.timings["Maghrib"] ?: day.timings["Sunset"] ?: "18:31"),
            Triple("Isha", "🌙", day.timings["Isha"] ?: "19:48")
        )

        val prayerItems = prayerDefs.map { (id, emoji, time24) ->
            val pCal = parseToCalendar(time24, now)
            val pMs = pCal?.timeInMillis ?: 0L
            val done = isPrayerDone(day.dateStr, id)
            val jTime = getJamatTime(id, time24)
            PrayerItem(
                id = id,
                nameEn = id,
                nameHi = when (id) {
                    "Fajr" -> "फ़ज्र"
                    "Dhuhr" -> "ज़ुहर"
                    "Asr" -> "असर"
                    "Maghrib" -> "मग़रिब"
                    else -> "इशा"
                },
                nameUr = when (id) {
                    "Fajr" -> "فجر"
                    "Dhuhr" -> "ظہر"
                    "Asr" -> "عصر"
                    "Maghrib" -> "مغرب"
                    else -> "عشاء"
                },
                nameAr = when (id) {
                    "Fajr" -> "الفجر"
                    "Dhuhr" -> "الظهر"
                    "Asr" -> "العصر"
                    "Maghrib" -> "المغرب"
                    else -> "العشاء"
                },
                time = time24,
                displayTime = to12HourFormat(time24),
                jamatTime = to12HourFormat(jTime),
                iconEmoji = emoji,
                isPassed = nowMs >= pMs,
                isDone = done
            )
        }

        val fajrItem = prayerItems.find { it.id == "Fajr" } ?: prayerItems[0]
        val dhuhrItem = prayerItems.find { it.id == "Dhuhr" } ?: prayerItems[1]
        val asrItem = prayerItems.find { it.id == "Asr" } ?: prayerItems[2]
        val maghribItem = prayerItems.find { it.id == "Maghrib" } ?: prayerItems[3]
        val ishaItem = prayerItems.find { it.id == "Isha" } ?: prayerItems[4]

        // ══════════════════════════════════════════════════════════════
        // EXACT 7 INTERVAL RULES FROM ORIGINAL APP (prayerActiveMeta):
        // ══════════════════════════════════════════════════════════════
        var current: PrayerItem? = null
        var next: PrayerItem? = null
        var nextTargetMs = 0L

        if (fajrMs > 0 && sunriseMs > 0 && nowMs >= fajrMs && nowMs < sunriseMs) {
            // 1. FAJR is active (starts at Fajr, ends at Sunrise)
            current = fajrItem
            next = dhuhrItem
            nextTargetMs = dhuhrMs
        } else if (sunriseMs > 0 && dhuhrMs > 0 && nowMs >= sunriseMs && nowMs < dhuhrMs) {
            // 2. Post-Sunrise gap (Fajr ends at Sunrise, Dhuhr hasn't started -> NO current prayer!)
            current = null
            next = dhuhrItem
            nextTargetMs = dhuhrMs
        } else if (dhuhrMs > 0 && asrMs > 0 && nowMs >= dhuhrMs && nowMs < asrMs) {
            // 3. DHUHR is active (starts at Dhuhr, ends at Asr)
            current = dhuhrItem
            next = asrItem
            nextTargetMs = asrMs
        } else if (asrMs > 0 && maghribMs > 0 && nowMs >= asrMs && nowMs < maghribMs) {
            // 4. ASR is active (starts at Asr, ends at Maghrib / Sunset)
            current = asrItem
            next = maghribItem
            nextTargetMs = maghribMs
        } else if (maghribMs > 0 && ishaMs > 0 && nowMs >= maghribMs && nowMs < ishaMs) {
            // 5. MAGHRIB is active (starts at Maghrib, ends at Isha)
            current = maghribItem
            next = ishaItem
            nextTargetMs = ishaMs
        } else if (ishaMs > 0 && nowMs >= ishaMs) {
            // 6. ISHA is active (evening: Isha start to midnight)
            current = ishaItem
            next = fajrItem
            nextTargetMs = fajrMs + 24 * 3600 * 1000L
        } else if (fajrMs > 0 && nowMs < fajrMs) {
            // 7. ISHA is active (morning: midnight to Fajr)
            current = ishaItem
            next = fajrItem
            nextTargetMs = fajrMs
        } else {
            // Fallback for edge cases
            for (item in prayerItems) {
                val pMs = parseToCalendar(item.time, now)?.timeInMillis ?: 0L
                if (nowMs >= pMs) current = item
                if (nowMs < pMs && next == null) {
                    next = item
                    nextTargetMs = pMs
                }
            }
            if (next == null && prayerItems.isNotEmpty()) {
                next = fajrItem
                nextTargetMs = fajrMs + 24 * 3600 * 1000L
            }
        }

        // ══════════════════════════════════════════════════════════════
        // COUNTDOWN TIMER LOGIC (prayerCountdownText)
        // ══════════════════════════════════════════════════════════════
        val remainingMs = maxOf(0L, nextTargetMs - nowMs)
        val totalSeconds = remainingMs / 1000L
        val hours = totalSeconds / 3600L
        val minutes = (totalSeconds % 3600L) / 60L
        val seconds = totalSeconds % 60L

        val countdownText = if (remainingMs <= 0L) {
            when (lang) {
                "en" -> "NOW"
                "ur" -> "ابھی"
                "ar" -> "الآن"
                else -> "अभी"
            }
        } else if (hours > 0) {
            String.format(Locale.US, "%d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format(Locale.US, "%02d:%02d", minutes, seconds)
        }

        // In horizontal strip: highlight Current prayer if ongoing, otherwise highlight Next prayer
        val activeHighlightId = current?.id ?: next?.id

        val mappedList = prayerItems.map { item ->
            val pCal = parseToCalendar(item.time, now)
            val pMs = pCal?.timeInMillis ?: 0L
            val isAct = item.id.equals(activeHighlightId, ignoreCase = true)
            item.copy(
                isCurrent = item.id.equals(current?.id, ignoreCase = true),
                isPassed = pMs < nowMs && !isAct
            )
        }
        val finalCurrent = mappedList.find { it.id.equals(current?.id, ignoreCase = true) }
        val finalNext = mappedList.find { it.id.equals(next?.id, ignoreCase = true) }

        // Progress between current and next
        var progress = 0.5f
        if (current != null && next != null) {
            val cMs = parseToCalendar(current.time, now)?.timeInMillis ?: (nowMs - 3600000L)
            val totalSpan = maxOf(1000L, nextTargetMs - cMs)
            val elapsed = nowMs - cMs
            progress = (elapsed.toFloat() / totalSpan).coerceIn(0f, 1f)
        }

        // Spiritual Quote
        val (quoteTitle, quoteBody) = getSpiritualQuote(finalNext?.id ?: "Dhuhr", lang)

        return PrayerActiveMeta(
            currentPrayer = finalCurrent,
            nextPrayer = finalNext,
            nextPrayerRemainingMs = remainingMs,
            countdownText = countdownText,
            progressPercent = progress,
            spiritualQuoteTitle = quoteTitle,
            spiritualQuoteBody = quoteBody,
            activeHighlightPrayerId = activeHighlightId
        )
    }

    fun getSpiritualQuote(prayerId: String, lang: String, dayOfMonth: Int = 1): Pair<String, String> {
        val cal = Calendar.getInstance().apply { set(Calendar.DAY_OF_MONTH, dayOfMonth) }
        return getSpiritualQuote(prayerId, lang, cal)
    }

    fun getSpiritualQuote(prayerId: String, lang: String, targetCal: Calendar?): Pair<String, String> {
        val cal = targetCal ?: Calendar.getInstance()
        val dayOfYear = cal.get(Calendar.DAY_OF_YEAR)
        val dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
        val quoteIndex = Math.abs((dayOfYear + dayOfMonth) % 10)

        val l = lang.lowercase().split("-")[0]
        val p = prayerId.lowercase()

        val map = quotesMap
        if (map != null) {
            val langDict = map[l] ?: map["en"] ?: map["hi"]
            val quotesList = langDict?.get(p) ?: map["en"]?.get(p) ?: map["hi"]?.get(p)
            if (!quotesList.isNullOrEmpty()) {
                return quotesList[quoteIndex % quotesList.size]
            }
        }

        // Static fallbacks
        val quotesEn = mapOf(
            "fajr" to listOf(
                "🌙 Fajr has arrived." to "Rise for Salah and begin your day with the remembrance of Allah.",
                "🌙 A new day begins." to "Let your first moments be with Allah through Fajr.",
                "🌙 Wake your heart before the world wakes around you." to "It’s time for Fajr.",
                "🌙 The dawn is here." to "Rise for Salah and welcome the blessings of a new day.",
                "🌙 Before the day gets busy," to "take a moment to stand before your Creator."
            ),
            "dhuhr" to listOf(
                "☀️ Dhuhr has arrived." to "Pause your day and reconnect with Allah through Salah.",
                "☀️ In the middle of your busy day," to "make a moment for the One who gave you this day.",
                "☀️ It’s time for Dhuhr." to "Step away from the world and find peace in Salah.",
                "☀️ Pause. Pray. Continue with purpose." to "Dhuhr Salah is here.",
                "☀️ Take a peaceful break." to "Turn your heart toward Allah and pray Dhuhr."
            ),
            "asr" to listOf(
                "🌤️ Asr has arrived." to "Pause your afternoon and turn your heart toward Allah.",
                "🌤️ The day is moving on." to "Take a moment for Asr and remember your Creator.",
                "🌤️ It’s time for Asr." to "Step away from your tasks and make time for Salah.",
                "🌤️ Refresh your heart this afternoon." to "It’s time for Asr Salah.",
                "🌤️ Before the day slips away," to "take a moment to stand before Allah."
            ),
            "maghrib" to listOf(
                "🌅 Maghrib has arrived." to "Welcome the evening with Salah and gratitude to Allah.",
                "🌅 The sun has set." to "Take a moment to pray Maghrib and remember Allah.",
                "🌅 As daylight fades," to "let your heart turn toward Allah. It’s time for Maghrib.",
                "🌅 A beautiful evening begins with Salah." to "Maghrib is here.",
                "🌅 The day is coming to an end." to "Pause and thank Allah through Maghrib Salah."
            ),
            "isha" to listOf(
                "🌌 Isha has arrived." to "End your day with Salah and place your heart at peace.",
                "🌌 The day is coming to an end." to "Take a moment for Isha and remember Allah.",
                "🌌 Before you rest tonight," to "stand before Allah for Isha Salah.",
                "🌌 The night has arrived." to "Find peace in Isha and leave your worries with Allah.",
                "🌌 Leave today in Allah’s hands." to "Pray Isha and rest with a peaceful heart."
            ),
            "tahajjud" to listOf(
                "🌙 The last third of the night is here." to "Rise for Tahajjud and call upon Allah, for He calls you to ask of Him.",
                "🌙 While the world sleeps, a special hour awaits." to "Rise for Tahajjud, make your dua, and seek Allah’s forgiveness.",
                "🌙 When the world is asleep, turn to the One who never sleeps." to "Pray Tahajjud and pour your heart out to Allah."
            )
        )

        val quotesHi = mapOf(
            "fajr" to listOf(
                "🌙 फ़ज्र का वक़्त हो गया है।" to "उठिए, नमाज़ अदा कीजिए और अल्लाह के ज़िक्र के साथ अपने दिन की शुरुआत कीजिए।",
                "🌙 एक नई सुबह का आग़ाज़ हो चुका है।" to "अपने दिन के पहले लम्हे अल्लाह की इबादत के साथ शुरू कीजिए।",
                "🌙 सुबह की रौशनी आ चुकी है।" to "उठिए, फ़ज्र की नमाज़ अदा कीजिए और नए दिन की बरकतों का इस्तक़बाल कीजिए।",
                "🌙 दिन शुरू होने से पहले सुकून का एक पल।" to "उठिए, फ़ज्र की नमाज़ अदा कीजिए और अपने दिल को अल्लाह के ज़िक्र से रोशन कीजिए।"
            ),
            "dhuhr" to listOf(
                "☀️ ज़ुहर का वक़्त हो गया है।" to "अपने दिन को कुछ पल रोकिए और नमाज़ के ज़रिए अल्लाह से अपना रिश्ता मज़बूत कीजिए।",
                "☀️ दिन की व्यस्तता के बीच," to "उस रब के लिए कुछ पल निकालिए जिसने आपको यह दिन अता किया है।",
                "☀️ ज़ुहर की नमाज़ का वक़्त है।" to "दुनिया की भाग-दौड़ से दूर होकर नमाज़ में सुकून तलाश कीजिए।",
                "☀️ नमाज़ के लिए वक़्त निकालिए।" to "ज़ुहर अल्लाह से जुड़ने और दिल को सुकून देने का बेहतरीन मौक़ा है।"
            ),
            "asr" to listOf(
                "🌤️ अस्र का वक़्त हो गया है।" to "दोपहर की व्यस्तता रोकिए और अपने दिल को अल्लाह की ओर मोड़िए।",
                "🌤️ दिन ढल रहा है।" to "कुछ देर रुकिए, अस्र की नमाज़ अदा कीजिए और अपने रब को याद कीजिए।",
                "🌤️ अपनी दोपहर को ज़िक्र का तोहफ़ा दीजिए।" to "अस्र की नमाज़ का वक़्त हो गया है।"
            ),
            "maghrib" to listOf(
                "🌅 मग़रिब का वक़्त हो गया है।" to "नमाज़ और अल्लाह के शुक्र के साथ शाम का इस्तक़बाल कीजिए।",
                "🌅 सूरज डूब चुका है।" to "कुछ पल रुकिए, मग़रिब की नमाज़ अदा कीजिए और अल्लाह को याद कीजिए।",
                "🌅 दिन अपने ख़ात्मे की तरफ़ है।" to "मग़रिब की नमाज़ के ज़रिए अपने रब का शुक्र अदा कीजिए।"
            ),
            "isha" to listOf(
                "🌌 इशा का वक़्त हो गया है।" to "नमाज़ के साथ अपने दिन का ख़ात्मा कीजिए और दिल को सुकून दीजिए।",
                "🌌 आज रात आराम करने से पहले," to "अपने रब के हुज़ूर इशा की नमाज़ के लिए खड़े हो जाइए।",
                "🌌 आज का दिन अल्लाह के हवाले कीजिए।" to "इशा की नमाज़ अदा कीजिए और पुरसुकून दिल के साथ आराम कीजिए।"
            ),
            "tahajjud" to listOf(
                "🌙 रात का आख़िरी एक-तिहाई हिस्सा आ चुका है।" to "तहज्जुद के लिए उठिए, अल्लाह से दुआ कीजिए और मग़फ़िरत तलब कीजिए।",
                "🌙 जब पूरी दुनिया सो रही हो, तब एक ख़ास वक़्त आपका इंतज़ार कर रहा है।" to "तहज्जुद के लिए उठिए, दुआ कीजिए और अल्लाह से मग़फ़िरत माँगिए।"
            )
        )

        val quotesHinglish = mapOf(
            "fajr" to listOf(
                "🌙 Fajr ki namaz ka waqt ho gaya hai." to "Namaz neend se behtar hai. Apne din ka aaghaz Allah ke zikr ke saath karein.",
                "🌙 Naya din, nayi barkat." to "Fajr ki namaz ada karein aur apne din ko roshan karein.",
                "🌙 Subah ki pehli namaz ke zariye" to "Allah se apna rabta mazboot karein."
            ),
            "dhuhr" to listOf(
                "☀️ Zohar ka waqt ho gaya hai." to "Apne din ko kuch pal rokiye aur namaz ke zariye Allah se rabta mazboot karein.",
                "☀️ Din ki masroofiyat ke darmiyan," to "Us Rab ke liye kuch pal nikaliye jisne aapko yeh din ata kiya hai.",
                "☀️ Zohar ki namaz ka waqt hai." to "Duniya ki bhaag-daud se door hokar namaz me sukoon talash kijiye."
            ),
            "asr" to listOf(
                "🌤️ Asr ka waqt ho gaya hai." to "Dopehar ki masroofiyat rokiye aur apne dil ko Allah ki taraf modiye.",
                "🌤️ Din dhal raha hai." to "Kuch der rukiye, Asr ki namaz ada kijiye aur apne Rab ko yaad kijiye."
            ),
            "maghrib" to listOf(
                "🌅 Maghrib ka waqt ho gaya hai." to "Namaz aur Allah ke shukr ke saath shaam ka isteqbal kijiye.",
                "🌅 Suraj doob chuka hai." to "Kuch pal rukiye, Maghrib ki namaz ada kijiye aur Allah ko yaad kijiye."
            ),
            "isha" to listOf(
                "🌌 Isha ka waqt ho gaya hai." to "Namaz ke saath apne din ka ikhtitam kijiye aur dil ko sukoon dijiye.",
                "🌌 Aaj raat aaram karne se pehle," to "Apne Rab ke huzoor Isha ki namaz ke liye khade ho jaiye."
            ),
            "tahajjud" to listOf(
                "🌙 Raat ka aakhiri hissa aa chuka hai." to "Tahajjud ke liye uthiye, Allah se dua kijiye aur maghfirat talab kijiye."
            )
        )

        val langMap = when (l) {
            "hi" -> quotesHi
            "hinglish", "roman" -> quotesHinglish
            else -> quotesEn
        }

        val list = langMap[p] ?: quotesEn[p] ?: listOf("🕌 $prayerId Time" to "It is time for prayer. Stand before Allah with gratitude.")
        return list[quoteIndex % list.size]
    }

    fun getEndingWarningQuote(prayerId: String, lang: String): Pair<String, String> {
        val l = lang.lowercase().split("-")[0]
        val pName = when (prayerId) {
            "Fajr" -> when (l) {
                "hi" -> "फ़ज्र"; "ur" -> "فجر"; "hinglish", "roman" -> "Fajr"; "ar" -> "الفجر"; "bn" -> "ফজর"; "gu" -> "ફજર"; "mr" -> "फजर"; "ta" -> "ஃபஜ்ர்"; "tr" -> "Sabah"; "id", "ms" -> "Subuh"; else -> "Fajr"
            }
            "Dhuhr" -> when (l) {
                "hi" -> "ज़ुहर"; "ur" -> "ظہر"; "hinglish", "roman" -> "Zohar"; "ar" -> "الظهر"; "bn" -> "যোহর"; "gu" -> "ઝુહર"; "mr" -> "झुहर"; "ta" -> "ளுஹர்"; "tr" -> "Öğle"; "id", "ms" -> "Dzuhur"; else -> "Dhuhr"
            }
            "Asr" -> when (l) {
                "hi" -> "अस्र"; "ur" -> "عصر"; "hinglish", "roman" -> "Asr"; "ar" -> "العصر"; "bn" -> "আসর"; "gu" -> "અસ્ર"; "mr" -> "असर"; "ta" -> "அஸர்"; "tr" -> "İkindi"; "id", "ms" -> "Ashar"; else -> "Asr"
            }
            "Maghrib" -> when (l) {
                "hi" -> "मग़रिब"; "ur" -> "مغرب"; "hinglish", "roman" -> "Maghrib"; "ar" -> "المغرب"; "bn" -> "মাগরিব"; "gu" -> "મગરીબ"; "mr" -> "मगरीब"; "ta" -> "மஃக்ரிப்"; "tr" -> "Akşam"; "id", "ms" -> "Maghrib"; else -> "Maghrib"
            }
            "Isha" -> when (l) {
                "hi" -> "इशा"; "ur" -> "عشاء"; "hinglish", "roman" -> "Isha"; "ar" -> "العشاء"; "bn" -> "এশা"; "gu" -> "ઇશા"; "mr" -> "इशा"; "ta" -> "இஷா"; "tr" -> "Yatsı"; "id", "ms" -> "Isya"; else -> "Isha"
            }
            else -> prayerId
        }

        return when (l) {
            "hi" -> Pair(
                "⚠️ $pName का वक़्त ख़त्म होने वाला है!",
                "सिर्फ़ 20 मिनट बाक़ी हैं। मेहरबानी करके वक़्त ख़त्म होने से पहले नमाज़ अदा कर लें।"
            )
            "ur" -> Pair(
                "⚠️ $pName کا وقت ختم ہونے والا ہے!",
                "صرف 20 منٹ باقی ہیں۔ برائے مہربانی وقت ختم ہونے سے پہلے نماز ادا کر لیں۔"
            )
            "hinglish", "roman" -> Pair(
                "⚠️ $pName Ka Waqt Khatam Hone Wala Hai!",
                "Sirf 20 minute baqi hain. Meherbani karke waqt khatam hone se pehle namaz ada kar lein."
            )
            "gu" -> Pair(
                "⚠️ $pName નો સમય સમાપ્ત થવા આવ્યો છે!",
                "માત્ર ૨૦ મિનિટ બાકી છે. કૃપા કરીને સમય પૂરો થાય તે પહેલાં નમાઝ અદા કરી લો."
            )
            "mr" -> Pair(
                "⚠️ $pName ची वेळ संपत आली आहे!",
                "फक्त २० मिनिटे शिल्लक आहेत. कृपया वेळ संपण्यापूर्वी नमाज अदा करा."
            )
            "ta" -> Pair(
                "⚠️ $pName தொழுகை நேரம் முடியப்போகிறது!",
                "இன்னும் 20 நிமிடங்கள் மட்டுமே உள்ளன. நேரம் முடிவதற்குள் உங்கள் தொழுகையை நிறைவேற்றுங்கள்."
            )
            "ar" -> Pair(
                "⚠️ وقت صلاة $pName شارف على الانتهاء!",
                "لم يتبق سوى 20 دقيقة. يرجى أداء الصلاة قبل انتهاء الوقت."
            )
            "bn" -> Pair(
                "⚠️ $pName নামাজের ওয়াক্ত শেষ হতে চলেছে!",
                "মাত্র ২০ মিনিট বাকি আছে। অনুগ্রহ করে সময় শেষ হওয়ার আগেই নামাজ আদায় করে নিন।"
            )
            "tr" -> Pair(
                "⚠️ $pName Vakti Çıkmak Üzere!",
                "Sadece 20 dakika kaldı. Lütfen vaktin çıkmasından önce namazınızı kılınız."
            )
            "id", "ms" -> Pair(
                "⚠️ Waktu Salat $pName Hampir Habis!",
                "Hanya tersisa 20 menit. Segeralah tunaikan salat sebelum waktunya berakhir."
            )
            "fr" -> Pair(
                "⚠️ L'heure de $pName se termine bientôt!",
                "Il ne reste que 20 minutes. Veuillez accomplir votre prière avant la fin de l'heure."
            )
            "de" -> Pair(
                "⚠️ Die $pName-Gebetszeit endet bald!",
                "Nur noch 20 Minuten verbleibend. Bitte verrichten Sie Ihr Gebet vor Ablauf der Zeit."
            )
            "ru" -> Pair(
                "⚠️ Время намаза $pName скоро закончится!",
                "Осталось всего 20 минут. Пожалуйста, совершите намаз до истечения времени."
            )
            else -> Pair(
                "⚠️ $prayerId Salah Ending Soon!",
                "Only 20 minutes remaining. Please perform your $prayerId prayer before the time expires."
            )
        }
    }

    // ══════════════════════════════════════════════════════════════
    //  NOTIFICATION SCHEDULING (Exact Alarms via AlarmManager)
    // ══════════════════════════════════════════════════════════════
    fun scheduleAllPrayerNotifications(context: Context) {
        val s = loadSettings()
        if (!s.notificationsEnabled) {
            cancelAllPrayerNotifications(context)
            return
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val nowMs = System.currentTimeMillis()
        val currentLang = getLanguage()

        // Schedule for today and tomorrow
        for (dayOffset in 0..1) {
            val targetCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, dayOffset) }
            val dayData = computeOfflineDay(targetCal.time, s)

            val prayerList = listOf(
                Pair("Fajr", s.soundFajr),
                Pair("Dhuhr", s.soundDhuhr),
                Pair("Asr", s.soundAsr),
                Pair("Maghrib", s.soundMaghrib),
                Pair("Isha", s.soundIsha)
            )

            for ((idx, p) in prayerList.withIndex()) {
                val prayerId = p.first
                val soundEnabled = p.second
                if (!soundEnabled) continue

                val timeStr = dayData.timings[prayerId] ?: continue
                val parts = timeStr.split(":")
                if (parts.size < 2) continue
                val h = parts[0].trim().toIntOrNull() ?: 0
                val m = parts[1].trim().toIntOrNull() ?: 0

                val prayerCal = Calendar.getInstance().apply {
                    time = targetCal.time
                    set(Calendar.HOUR_OF_DAY, h)
                    set(Calendar.MINUTE, m)
                    set(Calendar.SECOND, 0)
                }

                val triggerMs = prayerCal.timeInMillis
                if (triggerMs > nowMs) {
                    val notifId = (dayData.dayOfMonth * 100) + (idx + 1)
                    val (qTitle, qBody) = getSpiritualQuote(prayerId, currentLang, targetCal)

                    scheduleExactAlarm(
                        context = context,
                        alarmManager = alarmManager,
                        id = notifId,
                        title = qTitle,
                        body = "$qBody • ${to12HourFormat(timeStr)}",
                        timestampMs = triggerMs,
                        isWarning = false,
                        prayerName = prayerId,
                        dateKey = dayData.dateStr
                    )
                }

                // ⚠️ 20-Minute Ending Warning Notification (Red alert in notification drawer)
                if (s.warningEnabled) {
                    val endApi = when (prayerId) {
                        "Fajr" -> dayData.timings["Sunrise"]
                        "Dhuhr" -> dayData.timings["Asr"]
                        "Asr" -> dayData.timings["Maghrib"]
                        "Maghrib" -> dayData.timings["Isha"]
                        "Isha" -> dayData.timings["Midnight"] ?: dayData.timings["Fajr"]
                        else -> null
                    }

                    if (!endApi.isNullOrEmpty()) {
                        val endParts = endApi.split(":")
                        if (endParts.size >= 2) {
                            val endH = endParts[0].trim().toIntOrNull() ?: 0
                            val endM = endParts[1].trim().toIntOrNull() ?: 0
                            val endCal = Calendar.getInstance().apply {
                                time = targetCal.time
                                set(Calendar.HOUR_OF_DAY, endH)
                                set(Calendar.MINUTE, endM)
                                set(Calendar.SECOND, 0)
                                if (prayerId == "Isha" && endH < h) {
                                    add(Calendar.DAY_OF_YEAR, 1)
                                }
                            }
                            val warnMs = endCal.timeInMillis - (20 * 60 * 1000L)
                            if (warnMs > nowMs) {
                                val warnId = 50000 + (dayData.dayOfMonth * 100) + (idx + 1)
                                val (wTitle, wBody) = getEndingWarningQuote(prayerId, currentLang)
                                scheduleExactAlarm(
                                    context = context,
                                    alarmManager = alarmManager,
                                    id = warnId,
                                    title = wTitle,
                                    body = wBody,
                                    timestampMs = warnMs,
                                    isWarning = true,
                                    prayerName = prayerId,
                                    dateKey = dayData.dateStr
                                )
                            }
                        }
                    }
                }
            }
        }

        // Schedule Daily 9:00 PM Dhikr Reminder (with smart skip if already recited)
        scheduleDhikrDailyReminder(context)

        // Schedule Friday Jummah Reminders: 11:00 AM & 3:00 PM
        scheduleJummahReminders(context)
    }

    fun scheduleJummahReminders(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

        // 1. Friday 11:00 AM (Slot 1)
        val cal11 = getNextFridayCalendar(11, 0)
        scheduleSingleJummahAlarm(
            context,
            alarmManager,
            cal11.timeInMillis,
            com.tasbih.digitaltasbih.receivers.JummahReminderReceiver.REQ_CODE_MORNING,
            com.tasbih.digitaltasbih.receivers.JummahReminderReceiver.ACTION_JUMMAH_REMINDER_MORNING
        )

        // 2. Friday 3:00 PM (Slot 2)
        val cal15 = getNextFridayCalendar(15, 0)
        scheduleSingleJummahAlarm(
            context,
            alarmManager,
            cal15.timeInMillis,
            com.tasbih.digitaltasbih.receivers.JummahReminderReceiver.REQ_CODE_AFTERNOON,
            com.tasbih.digitaltasbih.receivers.JummahReminderReceiver.ACTION_JUMMAH_REMINDER_AFTERNOON
        )
        Log.d(TAG, "Scheduled Jummah reminders for 11:00 AM (${cal11.time}) and 3:00 PM (${cal15.time})")
    }

    private fun getNextFridayCalendar(targetHour: Int, targetMinute: Int): Calendar {
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, targetHour)
            set(Calendar.MINUTE, targetMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY || cal.timeInMillis <= System.currentTimeMillis()) {
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }
        return cal
    }

    private fun scheduleSingleJummahAlarm(
        context: Context,
        alarmManager: AlarmManager,
        timeMs: Long,
        requestCode: Int,
        actionStr: String
    ) {
        val intent = Intent(context, com.tasbih.digitaltasbih.receivers.JummahReminderReceiver::class.java).apply {
            action = actionStr
        }
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
        val pi = PendingIntent.getBroadcast(context, requestCode, intent, flags)

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeMs, pi)
                } else {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeMs, pi)
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeMs, pi)
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeMs, pi)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to schedule Jummah alarm ($actionStr): ${e.message}")
        }
    }

    fun scheduleDhikrDailyReminder(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 21) // 9:00 PM
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        val intent = Intent(context, com.tasbih.digitaltasbih.receivers.DhikrReminderReceiver::class.java).apply {
            action = com.tasbih.digitaltasbih.receivers.DhikrReminderReceiver.ACTION_DHIKR_REMINDER
        }
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
        val pi = PendingIntent.getBroadcast(context, com.tasbih.digitaltasbih.receivers.DhikrReminderReceiver.NOTIF_ID, intent, flags)

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pi)
                } else {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pi)
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pi)
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pi)
            }
            Log.d(TAG, "Scheduled daily 9 PM Dhikr reminder for ${cal.time}")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to schedule dhikr reminder: ${e.message}")
        }
    }

    private fun scheduleExactAlarm(
        context: Context,
        alarmManager: AlarmManager,
        id: Int,
        title: String,
        body: String,
        timestampMs: Long,
        isWarning: Boolean,
        prayerName: String,
        dateKey: String
    ) {
        val intent = Intent(context, PrayerAlarmReceiver::class.java).apply {
            action = PrayerAlarmReceiver.ACTION_PRAYER_ALARM
            putExtra("id", id)
            putExtra("title", title)
            putExtra("body", body)
            putExtra("is_warning", isWarning)
            putExtra("prayer_name", prayerName)
            putExtra("date_key", dateKey)
        }
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
        val pendingIntent = PendingIntent.getBroadcast(context, id, intent, flags)

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timestampMs, pendingIntent)
                } else {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timestampMs, pendingIntent)
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timestampMs, pendingIntent)
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, timestampMs, pendingIntent)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to schedule exact alarm: ${e.message}")
        }
    }

    fun cancelAllPrayerNotifications(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        // Cancel dhikr reminder
        val dhikrIntent = Intent(context, com.tasbih.digitaltasbih.receivers.DhikrReminderReceiver::class.java).apply {
            action = com.tasbih.digitaltasbih.receivers.DhikrReminderReceiver.ACTION_DHIKR_REMINDER
        }
        val dhikrPi = PendingIntent.getBroadcast(
            context,
            com.tasbih.digitaltasbih.receivers.DhikrReminderReceiver.NOTIF_ID,
            dhikrIntent,
            PendingIntent.FLAG_NO_CREATE or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
        )
        if (dhikrPi != null) {
            alarmManager.cancel(dhikrPi)
            dhikrPi.cancel()
        }

        // Cancel Jummah reminders
        val jMorningIntent = Intent(context, com.tasbih.digitaltasbih.receivers.JummahReminderReceiver::class.java).apply {
            action = com.tasbih.digitaltasbih.receivers.JummahReminderReceiver.ACTION_JUMMAH_REMINDER_MORNING
        }
        val jMorningPi = PendingIntent.getBroadcast(
            context,
            com.tasbih.digitaltasbih.receivers.JummahReminderReceiver.REQ_CODE_MORNING,
            jMorningIntent,
            PendingIntent.FLAG_NO_CREATE or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
        )
        if (jMorningPi != null) {
            alarmManager.cancel(jMorningPi)
            jMorningPi.cancel()
        }

        val jAfternoonIntent = Intent(context, com.tasbih.digitaltasbih.receivers.JummahReminderReceiver::class.java).apply {
            action = com.tasbih.digitaltasbih.receivers.JummahReminderReceiver.ACTION_JUMMAH_REMINDER_AFTERNOON
        }
        val jAfternoonPi = PendingIntent.getBroadcast(
            context,
            com.tasbih.digitaltasbih.receivers.JummahReminderReceiver.REQ_CODE_AFTERNOON,
            jAfternoonIntent,
            PendingIntent.FLAG_NO_CREATE or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
        )
        if (jAfternoonPi != null) {
            alarmManager.cancel(jAfternoonPi)
            jAfternoonPi.cancel()
        }

        // Cancel today and tomorrow prayer alarms
        for (dayOffset in 0..1) {
            val cal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, dayOffset) }
            val dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
            for (i in 1..5) {
                val notifId = (dayOfMonth * 100) + i
                val warnId = 50000 + (dayOfMonth * 100) + i

                val intent1 = Intent(context, PrayerAlarmReceiver::class.java).apply { action = PrayerAlarmReceiver.ACTION_PRAYER_ALARM }
                val pi1 = PendingIntent.getBroadcast(context, notifId, intent1, PendingIntent.FLAG_NO_CREATE or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0))
                if (pi1 != null) { alarmManager.cancel(pi1); pi1.cancel() }

                val intent2 = Intent(context, PrayerAlarmReceiver::class.java).apply { action = PrayerAlarmReceiver.ACTION_PRAYER_ALARM }
                val pi2 = PendingIntent.getBroadcast(context, warnId, intent2, PendingIntent.FLAG_NO_CREATE or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0))
                if (pi2 != null) { alarmManager.cancel(pi2); pi2.cancel() }
            }
        }
    }

    fun triggerTestNotification(context: Context, prayerId: String, isWarning: Boolean = false) {
        val currentLang = getLanguage()
        val title: String
        val body: String
        if (isWarning) {
            val (wTitle, wBody) = getEndingWarningQuote(prayerId, currentLang)
            title = wTitle
            body = wBody
        } else {
            val (qTitle, qBody) = getSpiritualQuote(prayerId, currentLang, Calendar.getInstance())
            title = qTitle
            body = qBody
        }

        val intent = Intent(context, PrayerAlarmReceiver::class.java).apply {
            action = PrayerAlarmReceiver.ACTION_PRAYER_ALARM
            putExtra("id", if (isWarning) 8888 else 9999)
            putExtra("title", title)
            putExtra("body", body)
            putExtra("is_warning", isWarning)
            putExtra("prayer_name", prayerId)
            putExtra("date_key", "test")
        }
        context.sendBroadcast(intent)
    }

    fun triggerTestDhikrNotification(context: Context) {
        val intent = Intent(context, com.tasbih.digitaltasbih.receivers.DhikrReminderReceiver::class.java).apply {
            action = com.tasbih.digitaltasbih.receivers.DhikrReminderReceiver.ACTION_DHIKR_REMINDER
        }
        context.sendBroadcast(intent)
    }
}
