package com.tasbih.digitaltasbih.data.ramadan

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object RamadanRepository {
    private const val PREFS_NAME = "dt_ramadan_hub_prefs"

    // Observable Compose tick trigger
    val updateTrigger = mutableStateOf(0L)

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    private fun getTodayKey(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    }

    // ── Selected City ──
    fun getSelectedCity(context: Context): String {
        return getPrefs(context).getString("key_ramadan_city", "Rajnandgaon") ?: "Rajnandgaon"
    }

    fun setSelectedCity(context: Context, city: String) {
        getPrefs(context).edit().putString("key_ramadan_city", city).apply()
        updateTrigger.value = System.currentTimeMillis()
    }

    // ── Current Ramadan Day (1..30) ──
    fun getCurrentRamadanDay(context: Context): Int {
        return getPrefs(context).getInt("key_current_ramadan_day", 1).coerceIn(1, 30)
    }

    fun setCurrentRamadanDay(context: Context, day: Int) {
        getPrefs(context).edit().putInt("key_current_ramadan_day", day.coerceIn(1, 30)).apply()
        updateTrigger.value = System.currentTimeMillis()
    }

    // ── Today's Deeds Checklist ──
    fun isDeedDone(context: Context, deedKey: String, dateKey: String = getTodayKey()): Boolean {
        return getPrefs(context).getBoolean("deed_${dateKey}_$deedKey", false)
    }

    fun setDeedDone(context: Context, deedKey: String, done: Boolean, dateKey: String = getTodayKey()) {
        getPrefs(context).edit().putBoolean("deed_${dateKey}_$deedKey", done).apply()
        updateTrigger.value = System.currentTimeMillis()
    }

    fun getTodayCompletedDeedsCount(context: Context): Int {
        val allDeeds = listOf("roza", "fajr", "dhuhr", "asr", "maghrib", "isha", "taraweeh", "tahajjud", "quran", "sadaqah", "dhikr")
        val today = getTodayKey()
        return allDeeds.count { isDeedDone(context, it, today) }
    }

    // ── Water Intake Tracker (Glasses 0..10) ──
    fun getWaterGlassesToday(context: Context): Int {
        return getPrefs(context).getInt("water_${getTodayKey()}", 0)
    }

    fun setWaterGlassesToday(context: Context, count: Int) {
        val clamped = count.coerceIn(0, 15)
        getPrefs(context).edit().putInt("water_${getTodayKey()}", clamped).apply()
        updateTrigger.value = System.currentTimeMillis()
    }

    // ── Quran Khatam Tracker ──
    fun getQuranTargetKhatams(context: Context): Int {
        return getPrefs(context).getInt("quran_target_khatams", 1)
    }

    fun setQuranTargetKhatams(context: Context, count: Int) {
        getPrefs(context).edit().putInt("quran_target_khatams", count.coerceIn(1, 5)).apply()
        updateTrigger.value = System.currentTimeMillis()
    }

    fun getCompletedJuzCount(context: Context): Int {
        return getPrefs(context).getInt("quran_completed_juz", 0)
    }

    fun setCompletedJuzCount(context: Context, count: Int) {
        getPrefs(context).edit().putInt("quran_completed_juz", count.coerceIn(0, 30)).apply()
        updateTrigger.value = System.currentTimeMillis()
    }

    // ── Ramadan Dhikr Counter (User & Community Contribution) ──
    fun getUserRamadanDhikrTotal(context: Context): Long {
        return getPrefs(context).getLong("user_ramadan_dhikr_total", 4250L)
    }

    fun incrementUserRamadanDhikr(context: Context, delta: Long = 1L) {
        val current = getUserRamadanDhikrTotal(context)
        getPrefs(context).edit().putLong("user_ramadan_dhikr_total", current + delta).apply()
        updateTrigger.value = System.currentTimeMillis()
    }

    // ── Zakat & Fitrana Saved Preferences ──
    fun getSavedCashAmount(context: Context): Double = getPrefs(context).getFloat("zakat_cash", 0f).toDouble()
    fun setSavedCashAmount(context: Context, amount: Double) {
        getPrefs(context).edit().putFloat("zakat_cash", amount.toFloat()).apply()
    }

    fun getSavedGoldGrams(context: Context): Double = getPrefs(context).getFloat("zakat_gold", 0f).toDouble()
    fun setSavedGoldGrams(context: Context, grams: Double) {
        getPrefs(context).edit().putFloat("zakat_gold", grams.toFloat()).apply()
    }

    fun getSavedSilverGrams(context: Context): Double = getPrefs(context).getFloat("zakat_silver", 0f).toDouble()
    fun setSavedSilverGrams(context: Context, grams: Double) {
        getPrefs(context).edit().putFloat("zakat_silver", grams.toFloat()).apply()
    }

    fun getSavedDebts(context: Context): Double = getPrefs(context).getFloat("zakat_debts", 0f).toDouble()
    fun setSavedDebts(context: Context, debts: Double) {
        getPrefs(context).edit().putFloat("zakat_debts", debts.toFloat()).apply()
    }

    fun getFitranaFamilyCount(context: Context): Int = getPrefs(context).getInt("fitrana_family_count", 4)
    fun setFitranaFamilyCount(context: Context, count: Int) {
        getPrefs(context).edit().putInt("fitrana_family_count", count.coerceAtLeast(1)).apply()
    }

    fun getFitranaRatePerPerson(context: Context): Double = getPrefs(context).getFloat("fitrana_rate", 100f).toDouble()
    fun setFitranaRatePerPerson(context: Context, rate: Double) {
        getPrefs(context).edit().putFloat("fitrana_rate", rate.toFloat()).apply()
    }
}
