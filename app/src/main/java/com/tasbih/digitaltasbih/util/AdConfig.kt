package com.tasbih.digitaltasbih.util

import android.content.Context

/**
 * Centralized AdMob Ad Unit and Configuration Manager.
 * Single Source of Truth for all AdMob IDs across the entire app.
 *
 * HOW TO SWITCH TO PRODUCTION:
 * 1. Change `USE_TEST_ADS = false` below.
 * 2. Replace the `PROD_*` IDs with your official AdMob IDs from the AdMob Console.
 * 3. In `AndroidManifest.xml`, replace the test `APPLICATION_ID` (`ca-app-pub-3940256099942544~3347511713`)
 *    with your official AdMob App ID (`ADMOB_APP_ID`).
 */
object AdConfig {

    // ─────────────────────────────────────────────────────────────────────────────
    // 1. MASTER PRODUCTION / TEST TOGGLE
    // ─────────────────────────────────────────────────────────────────────────────
    /**
     * Set to `false` when preparing a production release build.
     * When `true`, all ad loaders throughout the app strictly use Google's official test ad units.
     */
    const val USE_TEST_ADS = true

    /**
     * Master screenshot / website presentation toggle.
     * When true, completely disables all banners, native cards, floating video ads, and interstitials.
     */
    const val DISABLE_ALL_ADS_FOR_SCREENSHOTS = false

    // ─────────────────────────────────────────────────────────────────────────────
    // 2. OFFICIAL GOOGLE ADMOB TEST IDs (Never click live ads during development)
    // ─────────────────────────────────────────────────────────────────────────────
    const val TEST_ADMOB_APP_ID = "ca-app-pub-3940256099942544~3347511713"
    const val TEST_NATIVE_VIDEO_AD_UNIT_ID = "ca-app-pub-3940256099942544/1044960115"
    const val TEST_NATIVE_CARD_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"
    const val TEST_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
    const val TEST_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
    const val TEST_REWARDED_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"

    // ─────────────────────────────────────────────────────────────────────────────
    // 3. YOUR OFFICIAL PRODUCTION ADMOB IDs (Replace before release)
    // ─────────────────────────────────────────────────────────────────────────────
    const val PROD_ADMOB_APP_ID = "ca-app-pub-3940256099942544~3347511713"
    const val PROD_NATIVE_VIDEO_AD_UNIT_ID = "ca-app-pub-3940256099942544/1044960115"
    const val PROD_NATIVE_CARD_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"
    const val PROD_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
    const val PROD_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
    const val PROD_REWARDED_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"

    // ─────────────────────────────────────────────────────────────────────────────
    // 4. ACTIVE RESOLVERS (Automatically routed based on USE_TEST_ADS)
    // ─────────────────────────────────────────────────────────────────────────────
    val ADMOB_APP_ID: String
        get() = if (USE_TEST_ADS) TEST_ADMOB_APP_ID else PROD_ADMOB_APP_ID

    val NATIVE_VIDEO_AD_UNIT_ID: String
        get() = if (USE_TEST_ADS) TEST_NATIVE_VIDEO_AD_UNIT_ID else PROD_NATIVE_VIDEO_AD_UNIT_ID

    val NATIVE_CARD_AD_UNIT_ID: String
        get() = if (USE_TEST_ADS) TEST_NATIVE_CARD_AD_UNIT_ID else PROD_NATIVE_CARD_AD_UNIT_ID

    val BANNER_AD_UNIT_ID: String
        get() = if (USE_TEST_ADS) TEST_BANNER_AD_UNIT_ID else PROD_BANNER_AD_UNIT_ID

    val INTERSTITIAL_AD_UNIT_ID: String
        get() = if (USE_TEST_ADS) TEST_INTERSTITIAL_AD_UNIT_ID else PROD_INTERSTITIAL_AD_UNIT_ID

    val REWARDED_AD_UNIT_ID: String
        get() = if (USE_TEST_ADS) TEST_REWARDED_AD_UNIT_ID else PROD_REWARDED_AD_UNIT_ID

    // ─────────────────────────────────────────────────────────────────────────────
    // 5. FLOATING AD COOLDOWN (120 seconds persisted in SharedPreferences)
    // ─────────────────────────────────────────────────────────────────────────────
    const val FLOATING_AD_DISMISS_COOLDOWN_MS = 120_000L

    private const val PREFS_NAME = "tasbih_ad_prefs"
    private const val KEY_FLOATING_AD_DISMISSED_AT = "floating_ad_dismissed_at"

    /**
     * Checks whether the floating native video ad is currently in the 120-second cooldown period.
     * Persisted across app restarts.
     */
    fun isFloatingAdDismissed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val dismissedAt = prefs.getLong(KEY_FLOATING_AD_DISMISSED_AT, 0L)
        if (dismissedAt == 0L) return false

        val elapsed = System.currentTimeMillis() - dismissedAt
        return elapsed < FLOATING_AD_DISMISS_COOLDOWN_MS
    }

    /**
     * Records that the user clicked the close button on the floating native video ad.
     */
    fun recordFloatingAdDismissed(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putLong(KEY_FLOATING_AD_DISMISSED_AT, System.currentTimeMillis()).apply()
    }

    /**
     * Resets cooldown (used for testing or developer settings).
     */
    fun resetFloatingAdCooldown(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_FLOATING_AD_DISMISSED_AT).apply()
    }
}
