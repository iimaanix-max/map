package com.tasbih.digitaltasbih.util

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.LoadAdError
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics

/**
 * Centralized Firebase Crashlytics & Analytics Tracker for Digital Tasbih.
 *
 * Captures ad load & show failures, linking them with deviceId, ad placement,
 * and error codes without incurring any database costs (100% Free Firebase tier).
 */
object FirebaseAdTracker {

    private const val TAG = "FirebaseAdTracker"

    private var firebaseAnalytics: FirebaseAnalytics? = null
    private var firebaseCrashlytics: FirebaseCrashlytics? = null
    private var cachedDeviceId: String = ""

    fun init(context: Context, deviceId: String) {
        cachedDeviceId = deviceId
        try {
            firebaseAnalytics = FirebaseAnalytics.getInstance(context)
            firebaseCrashlytics = FirebaseCrashlytics.getInstance().apply {
                setCrashlyticsCollectionEnabled(true)
                setUserId(deviceId)
                setCustomKey("device_id", deviceId)
            }
            firebaseAnalytics?.setUserId(deviceId)
            Log.d(TAG, "Firebase Analytics & Crashlytics initialized for device: $deviceId")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize Firebase trackers: ${e.message}")
        }
    }

    /**
     * Log Ad load failures (e.g. Code 3 = No Fill, Code 2 = Network Error)
     */
    fun logAdLoadFailure(
        adFormat: String, // "rewarded", "interstitial", "native"
        placement: String,
        error: LoadAdError
    ) {
        val code = error.code
        val message = error.message.ifBlank { "Unknown" }
        val domain = error.domain.ifBlank { "Unknown" }

        try {
            // 1. Firebase Analytics: Shows failure events, graphs, and country breakdown in console
            firebaseAnalytics?.let { analytics ->
                val bundle = Bundle().apply {
                    putString("ad_format", adFormat)
                    putString("placement", placement)
                    putInt("error_code", code)
                    putString("error_message", message)
                    putString("error_domain", domain)
                    putString("device_id", cachedDeviceId)
                }
                analytics.logEvent("ad_load_failed", bundle)
            }

            // 2. Firebase Crashlytics: Breadcrumb log & diagnostics keys
            firebaseCrashlytics?.let { crashlytics ->
                crashlytics.setCustomKey("last_failed_ad_format", adFormat)
                crashlytics.setCustomKey("last_failed_ad_placement", placement)
                crashlytics.setCustomKey("last_failed_ad_code", code)
                crashlytics.setCustomKey("last_failed_ad_msg", message)
                crashlytics.log("Ad load failed: format=$adFormat placement=$placement code=$code msg=$message")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error logging ad failure to Firebase: ${e.message}")
        }

        Log.w(TAG, "Logged ad failure: format=$adFormat placement=$placement code=$code msg=$message")
    }

    /**
     * Log Ad full-screen display failures
     */
    fun logAdShowFailure(
        adFormat: String,
        placement: String,
        adError: AdError
    ) {
        val code = adError.code
        val message = adError.message.ifBlank { "Unknown" }

        try {
            firebaseAnalytics?.let { analytics ->
                val bundle = Bundle().apply {
                    putString("ad_format", adFormat)
                    putString("placement", placement)
                    putInt("error_code", code)
                    putString("error_message", message)
                    putString("device_id", cachedDeviceId)
                }
                analytics.logEvent("ad_show_failed", bundle)
            }

            firebaseCrashlytics?.let { crashlytics ->
                crashlytics.setCustomKey("last_show_failed_format", adFormat)
                crashlytics.setCustomKey("last_show_failed_placement", placement)
                crashlytics.setCustomKey("last_show_failed_code", code)
                crashlytics.log("Ad show failed: format=$adFormat placement=$placement code=$code msg=$message")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error logging ad show failure to Firebase: ${e.message}")
        }

        Log.w(TAG, "Logged ad show failure: format=$adFormat placement=$placement code=$code msg=$message")
    }
}
