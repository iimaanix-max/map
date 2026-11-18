package com.tasbih.digitaltasbih.data.jummah

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import com.tasbih.digitaltasbih.data.PrayerRepository
import org.json.JSONObject
import java.util.Calendar
import java.util.Date

object JummahRepository {

    private const val PREFS_NAME = "digital_tasbih_jummah"
    private const val KEY_FORCE_SHOW = "dt_jummah_force_show"

    // Observable tick state for Compose recomposition
    val updateTrigger = mutableStateOf(0L)

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Computes today's Maghrib time in minutes from midnight (0..1439).
     * Uses PrayerRepository's offline calculation with user's settings/location.
     * Fallback to 18:00 (1080 minutes) if unavailable.
     */
    fun getMaghribMinutes(context: Context?, date: Date = Calendar.getInstance().time): Int {
        return try {
            if (context != null) {
                PrayerRepository.init(context)
            }
            val settings = PrayerRepository.loadSettings()
            val dayData = PrayerRepository.computeOfflineDay(date, settings)
            val maghribStr = dayData.timings["Maghrib"] ?: "18:00"
            val parts = maghribStr.split(":")
            if (parts.size >= 2) {
                val h = parts[0].trim().toIntOrNull() ?: 18
                val m = parts[1].trim().toIntOrNull() ?: 0
                h * 60 + m
            } else {
                18 * 60
            }
        } catch (_: Exception) {
            18 * 60
        }
    }

    /**
     * Date key for persisting Jummah checklist and Durood count.
     * In Islamic timing, Thursday evening after Maghrib is already Friday (Laylatul Jummah).
     * Therefore, if it is Thursday after Maghrib, we advance calendar by 1 day to Friday's date
     * so that checklist and counter progress started Thursday evening seamlessly carries over
     * into Friday daytime without resetting.
     */
    fun getDateKey(context: Context? = null): String {
        val cal = Calendar.getInstance()
        val day = cal.get(Calendar.DAY_OF_WEEK)
        val nowMins = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
        val maghribMins = getMaghribMinutes(context, cal.time)

        if (day == Calendar.THURSDAY && nowMins >= maghribMins) {
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }

        val y = cal.get(Calendar.YEAR)
        val m = String.format(java.util.Locale.US, "%02d", cal.get(Calendar.MONTH) + 1)
        val d = String.format(java.util.Locale.US, "%02d", cal.get(Calendar.DAY_OF_MONTH))
        return "$y-$m-$d"
    }

    /**
     * Active according to Islamic timing:
     * - Starts on Thursday at Maghrib (Laylatul Jummah / Islamic Friday begins)
     * - Continues all through Thursday night and Friday daytime
     * - Ends on Friday at Maghrib (Maghrib concludes Islamic Friday)
     * - Hidden at all other times (Friday post-Maghrib, Sat, Sun, Mon, Tue, Wed, Thursday pre-Maghrib)
     */
    fun isJummahActiveNow(context: Context? = null): Boolean {
        val cal = Calendar.getInstance()
        val day = cal.get(Calendar.DAY_OF_WEEK)
        val nowMins = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
        val maghribMins = getMaghribMinutes(context, cal.time)

        return when (day) {
            Calendar.THURSDAY -> nowMins >= maghribMins
            Calendar.FRIDAY -> nowMins < maghribMins
            else -> false
        }
    }

    /**
     * Determines whether the Jummah Mubarak tile is visible in the feature grid.
     * Follows Islamic schedule (Thursday Maghrib to Friday Maghrib),
     * or if debug force-show is explicitly enabled in preferences.
     */
    fun isJummahTileVisible(context: Context): Boolean {
        if (isJummahActiveNow(context)) return true
        val prefs = getPrefs(context)
        return prefs.getBoolean(KEY_FORCE_SHOW, false)
    }

    fun setForceShow(context: Context, force: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_FORCE_SHOW, force).apply()
        updateTrigger.value = System.currentTimeMillis()
    }

    /**
     * Shows pulsing red notification dot until user marks at least one Sunnah today
     */
    fun isJummahSunnahUnread(context: Context): Boolean {
        if (!isJummahTileVisible(context)) return false
        val progress = getProgress(context)
        return progress.values.none { it }
    }

    fun getProgress(context: Context): Map<String, Boolean> {
        val key = "dt_jummah_prog_" + getDateKey(context)
        val jsonStr = getPrefs(context).getString(key, "{}") ?: "{}"
        val map = mutableMapOf<String, Boolean>()
        try {
            val json = JSONObject(jsonStr)
            val keys = json.keys()
            while (keys.hasNext()) {
                val k = keys.next()
                map[k] = json.optBoolean(k, false)
            }
        } catch (_: Exception) {}
        return map
    }

    fun toggleItem(context: Context, itemId: String): Boolean {
        val prefs = getPrefs(context)
        val key = "dt_jummah_prog_" + getDateKey(context)
        val map = getProgress(context).toMutableMap()
        val newState = !(map[itemId] ?: false)
        map[itemId] = newState

        val json = JSONObject()
        map.forEach { (k, v) -> json.put(k, v) }
        prefs.edit().putString(key, json.toString()).apply()
        updateTrigger.value = System.currentTimeMillis()
        return newState
    }

    fun isItemCompleted(context: Context, itemId: String): Boolean {
        return getProgress(context)[itemId] == true
    }

    fun resetProgress(context: Context) {
        val prefs = getPrefs(context)
        val key = "dt_jummah_prog_" + getDateKey(context)
        prefs.edit().remove(key).apply()
        updateTrigger.value = System.currentTimeMillis()
    }

    // ── Durood Tap Counter ──

    fun getDuroodCount(context: Context): Int {
        val key = "dt_jummah_durood_" + getDateKey(context)
        return getPrefs(context).getInt(key, 0)
    }

    fun incrementDurood(context: Context): Int {
        val prefs = getPrefs(context)
        val key = "dt_jummah_durood_" + getDateKey(context)
        val current = getDuroodCount(context)
        val next = current + 1
        prefs.edit().putInt(key, next).apply()

        // Auto mark durood Sunnah #10 completed if not already marked
        if (!isItemCompleted(context, "durood")) {
            val progKey = "dt_jummah_prog_" + getDateKey(context)
            val map = getProgress(context).toMutableMap()
            map["durood"] = true
            val json = JSONObject()
            map.forEach { (k, v) -> json.put(k, v) }
            prefs.edit().putString(progKey, json.toString()).apply()
        }

        updateTrigger.value = System.currentTimeMillis()
        return next
    }

    fun resetDurood(context: Context) {
        val prefs = getPrefs(context)
        val key = "dt_jummah_durood_" + getDateKey(context)
        prefs.edit().putInt(key, 0).apply()
        updateTrigger.value = System.currentTimeMillis()
    }
}
