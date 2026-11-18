package com.tasbih.digitaltasbih.data

import android.content.Context
import java.util.Calendar

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

/**
 * Manages Ramadan seasonal events, automated detection, and visual decoration states.
 * - Detects authentic Hijri Month 9 (Ramadan) via HijriCalendarEngine.
 * - Supports instant Preview Mode so user can inspect and enjoy the festive decoration anytime.
 * - Supports persistent enable/disable preferences.
 * - Exposes live Compose reactive state so home screen updates instantly when settings change.
 */
object RamadanManager {
    private const val PREFS_NAME = "dt_app_prefs"
    private const val KEY_RAMADAN_PREVIEW = "key_ramadan_preview_mode"
    private const val KEY_RAMADAN_DECORATION_ENABLED = "key_ramadan_decoration_enabled"

    // Temporarily paused per user request. All Ramadan hub, decor, and tracker code is 100% saved and preserved.
    // Set to false whenever Ramadan features are resumed!
    var isRamadanTemporarilyPaused: Boolean = true

    private val _isRamadanActiveState = mutableStateOf(false)
    val isRamadanActiveState: State<Boolean> = _isRamadanActiveState

    fun init(context: Context) {
        _isRamadanActiveState.value = shouldShowRamadanDecoration(context)
    }

    /**
     * Preview mode defaults to false so the app remains clean and normal.
     * When Ramadan arrives (month 9), it activates automatically.
     */
    fun isPreviewEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_RAMADAN_PREVIEW, false)
    }

    fun setPreviewEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean(KEY_RAMADAN_PREVIEW, enabled)
            .putBoolean(KEY_RAMADAN_DECORATION_ENABLED, enabled)
            .apply()
        _isRamadanActiveState.value = shouldShowRamadanDecoration(context)
    }

    fun isDecorationEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_RAMADAN_DECORATION_ENABLED, false)
    }

    fun setDecorationEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_RAMADAN_DECORATION_ENABLED, enabled).apply()
        _isRamadanActiveState.value = shouldShowRamadanDecoration(context)
    }

    /**
     * Checks if current Islamic date is within Ramadan (Hijri month 9).
     */
    fun isRamadanMonth(): Boolean {
        return try {
            val now = Calendar.getInstance()
            val hDate = HijriCalendarEngine.getHijriDate(
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH),
                "en"
            )
            hDate.monthNumber == 9
        } catch (_: Exception) {
            false
        }
    }

    /**
     * Master condition to display Ramadan light decorations on Home screen.
     */
    fun shouldShowRamadanDecoration(context: Context): Boolean {
        if (isRamadanTemporarilyPaused) return false
        if (!isDecorationEnabled(context)) return false
        return isRamadanMonth() || isPreviewEnabled(context)
    }
}
