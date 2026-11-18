package com.tasbih.digitaltasbih.data.ramadan

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class QuranReflectionNote(
    val id: String,
    val title: String,
    val text: String,
    val date: String,
    val pageOrRuku: String
)

data class QuranBookmarkItem(
    val id: String,
    val label: String,
    val juz: Int,
    val page: Int,
    val ruku: Int,
    val date: String
)

object QuranTrackerRepository {
    private const val PREFS_NAME = "quran_tracker_preferences"
    val updateTrigger = mutableStateOf(0L)

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // ── Pages Read ──
    fun getPagesRead(context: Context): Int {
        return getPrefs(context).getInt("pages_read", 226)
    }

    fun setPagesRead(context: Context, pages: Int) {
        val clamped = pages.coerceIn(0, 604)
        getPrefs(context).edit().putInt("pages_read", clamped).apply()
        // Sync completed juz count
        val juzCount = ((clamped - 1).coerceAtLeast(0) / 20)
        RamadanRepository.setCompletedJuzCount(context, juzCount.coerceIn(0, 30))
        updateTrigger.value = System.currentTimeMillis()
    }

    // ── Daily Salah 5 Checklist ──
    fun isSalahRead(context: Context, salahKey: String): Boolean {
        val dateKey = getTodayDateKey()
        return getPrefs(context).getBoolean("salah_read_${dateKey}_$salahKey", false)
    }

    fun toggleSalahRead(context: Context, salahKey: String, pagesDelta: Int = 4): Boolean {
        val dateKey = getTodayDateKey()
        val key = "salah_read_${dateKey}_$salahKey"
        val cur = getPrefs(context).getBoolean(key, false)
        val next = !cur
        getPrefs(context).edit().putBoolean(key, next).apply()
        if (next) {
            setPagesRead(context, getPagesRead(context) + pagesDelta)
            addReadingMinutes(context, 15)
        } else {
            setPagesRead(context, (getPagesRead(context) - pagesDelta).coerceAtLeast(0))
        }
        updateTrigger.value = System.currentTimeMillis()
        return next
    }

    // ── 30 Juz Completed Set ──
    fun isJuzCompleted(context: Context, juzNum: Int): Boolean {
        val raw = getPrefs(context).getString("completed_juz_set", "") ?: ""
        val set = raw.split(",").filter { it.isNotBlank() }.mapNotNull { it.toIntOrNull() }.toSet()
        return set.contains(juzNum)
    }

    fun toggleJuzCompleted(context: Context, juzNum: Int): Boolean {
        val raw = getPrefs(context).getString("completed_juz_set", "") ?: ""
        val set = raw.split(",").filter { it.isNotBlank() }.mapNotNull { it.toIntOrNull() }.toMutableSet()
        val isNowCompleted: Boolean
        if (set.contains(juzNum)) {
            set.remove(juzNum)
            isNowCompleted = false
        } else {
            set.add(juzNum)
            isNowCompleted = true
            // If completed, ensure page count accounts for at least this juz
            val minPages = juzNum * 20
            if (getPagesRead(context) < minPages) {
                setPagesRead(context, minPages)
            }
        }
        getPrefs(context).edit().putString("completed_juz_set", set.joinToString(",")).apply()
        updateTrigger.value = System.currentTimeMillis()
        return isNowCompleted
    }

    fun getCompletedJuzCount(context: Context): Int {
        val raw = getPrefs(context).getString("completed_juz_set", "") ?: ""
        val fromSet = raw.split(",").filter { it.isNotBlank() }.mapNotNull { it.toIntOrNull() }.size
        val fromPages = (getPagesRead(context) / 20).coerceIn(0, 30)
        return maxOf(fromSet, fromPages)
    }

    // ── 114 Surahs Completed Set ──
    fun isSurahCompleted(context: Context, surahNum: Int): Boolean {
        val raw = getPrefs(context).getString("completed_surah_set", "1") ?: "1"
        val set = raw.split(",").filter { it.isNotBlank() }.mapNotNull { it.toIntOrNull() }.toSet()
        return set.contains(surahNum)
    }

    fun toggleSurahCompleted(context: Context, surahNum: Int): Boolean {
        val raw = getPrefs(context).getString("completed_surah_set", "1") ?: "1"
        val set = raw.split(",").filter { it.isNotBlank() }.mapNotNull { it.toIntOrNull() }.toMutableSet()
        val isNowCompleted: Boolean
        if (set.contains(surahNum)) {
            set.remove(surahNum)
            isNowCompleted = false
        } else {
            set.add(surahNum)
            isNowCompleted = true
        }
        getPrefs(context).edit().putString("completed_surah_set", set.joinToString(",")).apply()
        updateTrigger.value = System.currentTimeMillis()
        return isNowCompleted
    }

    // ── Current Position ──
    fun getCurrentJuz(context: Context): Int {
        val pages = getPagesRead(context)
        return (((pages - 1).coerceAtLeast(0) / 20) + 1).coerceIn(1, 30)
    }

    fun getCurrentRuku(context: Context): Int {
        val defaultRuku = (getPagesRead(context) * 558) / 604
        return getPrefs(context).getInt("current_ruku", defaultRuku.coerceIn(1, 558))
    }

    fun setCurrentRuku(context: Context, ruku: Int) {
        val clamped = ruku.coerceIn(1, 558)
        val equivPage = (clamped * 604) / 558
        getPrefs(context).edit().putInt("current_ruku", clamped).apply()
        setPagesRead(context, equivPage)
    }

    fun getCurrentSurah(context: Context): Int {
        return getPrefs(context).getInt("current_surah", 2)
    }

    fun setCurrentSurah(context: Context, surah: Int) {
        getPrefs(context).edit().putInt("current_surah", surah.coerceIn(1, 114)).apply()
        updateTrigger.value = System.currentTimeMillis()
    }

    // ── Reading Time & Streak ──
    fun getReadingMinutes(context: Context): Int {
        return getPrefs(context).getInt("reading_minutes", 1122)
    }

    fun addReadingMinutes(context: Context, mins: Int) {
        val cur = getReadingMinutes(context)
        getPrefs(context).edit().putInt("reading_minutes", cur + mins).apply()
        updateTrigger.value = System.currentTimeMillis()
    }

    fun getStreakDays(context: Context): Int {
        return getPrefs(context).getInt("quran_streak_days", 7)
    }

    // ── Khatam Plan ──
    fun getTargetDays(context: Context): Int {
        return getPrefs(context).getInt("khatam_target_days", 30)
    }

    fun setTargetDays(context: Context, days: Int) {
        getPrefs(context).edit().putInt("khatam_target_days", days.coerceIn(10, 120)).apply()
        updateTrigger.value = System.currentTimeMillis()
    }

    fun getPagesPerSalah(context: Context): Int {
        val days = getTargetDays(context)
        return when (days) {
            15 -> 8
            60 -> 2
            else -> 4
        }
    }

    // ── Reflections / Notes ──
    fun getNotes(context: Context): List<QuranReflectionNote> {
        val raw = getPrefs(context).getString("reflection_notes", null) ?: return defaultNotes()
        return try {
            val arr = JSONArray(raw)
            val list = mutableListOf<QuranReflectionNote>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(
                    QuranReflectionNote(
                        id = obj.getString("id"),
                        title = obj.getString("title"),
                        text = obj.getString("text"),
                        date = obj.getString("date"),
                        pageOrRuku = obj.getString("pageOrRuku")
                    )
                )
            }
            list
        } catch (_: Exception) {
            defaultNotes()
        }
    }

    fun addNote(context: Context, note: QuranReflectionNote) {
        val current = getNotes(context).toMutableList()
        current.add(0, note)
        val arr = JSONArray()
        current.take(50).forEach { n ->
            val obj = JSONObject()
            obj.put("id", n.id)
            obj.put("title", n.title)
            obj.put("text", n.text)
            obj.put("date", n.date)
            obj.put("pageOrRuku", n.pageOrRuku)
            arr.put(obj)
        }
        getPrefs(context).edit().putString("reflection_notes", arr.toString()).apply()
        updateTrigger.value = System.currentTimeMillis()
    }

    fun deleteNote(context: Context, id: String) {
        val current = getNotes(context).filterNot { it.id == id }
        val arr = JSONArray()
        current.forEach { n ->
            val obj = JSONObject()
            obj.put("id", n.id)
            obj.put("title", n.title)
            obj.put("text", n.text)
            obj.put("date", n.date)
            obj.put("pageOrRuku", n.pageOrRuku)
            arr.put(obj)
        }
        getPrefs(context).edit().putString("reflection_notes", arr.toString()).apply()
        updateTrigger.value = System.currentTimeMillis()
    }

    private fun defaultNotes(): List<QuranReflectionNote> {
        return listOf(
            QuranReflectionNote(
                id = "note_1",
                title = "Surah Al-Baqarah (Ayatul Kursi 2:255)",
                text = "Reciting Ayatul Kursi after Fajr brings unshakeable peace and protection throughout the day.",
                date = "Day 1 Ramadan",
                pageOrRuku = "Page 42 • Ruku 34"
            ),
            QuranReflectionNote(
                id = "note_2",
                title = "Patience and Prayer (2:153)",
                text = "'Seek help through patience and prayer; surely Allah is with the patient.' Perfect reminder for fasting hours.",
                date = "Day 2 Ramadan",
                pageOrRuku = "Page 24 • Ruku 18"
            )
        )
    }

    // ── Bookmarks ──
    fun getBookmarks(context: Context): List<QuranBookmarkItem> {
        val raw = getPrefs(context).getString("quran_bookmarks", null) ?: return defaultBookmarks()
        return try {
            val arr = JSONArray(raw)
            val list = mutableListOf<QuranBookmarkItem>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(
                    QuranBookmarkItem(
                        id = obj.getString("id"),
                        label = obj.getString("label"),
                        juz = obj.getInt("juz"),
                        page = obj.getInt("page"),
                        ruku = obj.getInt("ruku"),
                        date = obj.getString("date")
                    )
                )
            }
            list
        } catch (_: Exception) {
            defaultBookmarks()
        }
    }

    fun addBookmark(context: Context, item: QuranBookmarkItem) {
        val current = getBookmarks(context).toMutableList()
        current.removeAll { it.page == item.page }
        current.add(0, item)
        val arr = JSONArray()
        current.take(30).forEach { b ->
            val obj = JSONObject()
            obj.put("id", b.id)
            obj.put("label", b.label)
            obj.put("juz", b.juz)
            obj.put("page", b.page)
            obj.put("ruku", b.ruku)
            obj.put("date", b.date)
            arr.put(obj)
        }
        getPrefs(context).edit().putString("quran_bookmarks", arr.toString()).apply()
        updateTrigger.value = System.currentTimeMillis()
    }

    fun removeBookmark(context: Context, id: String) {
        val current = getBookmarks(context).filterNot { it.id == id }
        val arr = JSONArray()
        current.forEach { b ->
            val obj = JSONObject()
            obj.put("id", b.id)
            obj.put("label", b.label)
            obj.put("juz", b.juz)
            obj.put("page", b.page)
            obj.put("ruku", b.ruku)
            obj.put("date", b.date)
            arr.put(obj)
        }
        getPrefs(context).edit().putString("quran_bookmarks", arr.toString()).apply()
        updateTrigger.value = System.currentTimeMillis()
    }

    private fun defaultBookmarks(): List<QuranBookmarkItem> {
        return listOf(
            QuranBookmarkItem(
                id = "bm_1",
                label = "Surah Al-Baqarah",
                juz = 2,
                page = 34,
                ruku = 28,
                date = "Ramadan 1447"
            ),
            QuranBookmarkItem(
                id = "bm_2",
                label = "Surah Ali 'Imran",
                juz = 3,
                page = 50,
                ruku = 41,
                date = "Ramadan 1447"
            )
        )
    }

    private fun getTodayDateKey(): String {
        val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return sdf.format(Date())
    }

    fun formatDateNow(): String {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return sdf.format(Date())
    }
}
