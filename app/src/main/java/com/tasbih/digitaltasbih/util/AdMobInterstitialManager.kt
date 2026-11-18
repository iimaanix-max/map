package com.tasbih.digitaltasbih.util

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

/**
 * Google AdMob Interstitial Ad Manager.
 * Handles background pre-loading, fail-safe display, and seamless fallback.
 * If an ad is ready, it displays smoothly on placement events (e.g. salah_back).
 * If not ready or failed, it triggers background loading and lets the user proceed without delay.
 */
object AdMobInterstitialManager {
    private const val TAG = "AdMobInterstitial"
    // Google official test Interstitial Ad Unit ID
    private val ADMOB_INTERSTITIAL_UNIT_ID: String
        get() = AdConfig.INTERSTITIAL_AD_UNIT_ID

    private var interstitialAd: InterstitialAd? = null
    private var isLoading = false
    private var isShowing = false

    fun loadAd(context: Context) {
        if (!ConsentManager.canRequestAds(context)) {
            Log.d(TAG, "Postponing interstitial load: Waiting for user consent")
            ConsentManager.addConsentListener {
                loadAd(context)
            }
            return
        }

        if (interstitialAd != null || isLoading) return
        isLoading = true

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context.applicationContext,
            ADMOB_INTERSTITIAL_UNIT_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    isLoading = false
                    Log.d(TAG, "Interstitial ad loaded successfully")
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                    isLoading = false
                    Log.w(TAG, "Interstitial ad FAILED — Code: ${error.code} | ${error.message} | Cause: ${error.cause}")
                    FirebaseAdTracker.logAdLoadFailure("interstitial", "interstitial_unit", error)
                }
            }
        )
    }

    fun showAd(activity: Activity, placement: String = "salah_back", onDismissed: () -> Unit = {}) {
        if (AdConfig.DISABLE_ALL_ADS_FOR_SCREENSHOTS) {
            onDismissed()
            return
        }
        val ad = interstitialAd
        if (ad != null && !isShowing) {
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdShowedFullScreenContent() {
                    isShowing = true
                    Log.d(TAG, "Interstitial ad showed for placement: $placement")
                }

                override fun onAdDismissedFullScreenContent() {
                    isShowing = false
                    interstitialAd = null
                    loadAd(activity) // Pre-load next
                    activity.runOnUiThread {
                        onDismissed()
                    }
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    isShowing = false
                    interstitialAd = null
                    FirebaseAdTracker.logAdShowFailure("interstitial", placement, adError)
                    loadAd(activity)
                    activity.runOnUiThread {
                        onDismissed()
                    }
                }
            }
            ad.show(activity)
        } else {
            // Ad not ready: trigger background loading and proceed immediately
            loadAd(activity)
            activity.runOnUiThread {
                onDismissed()
            }
        }
    }
}
