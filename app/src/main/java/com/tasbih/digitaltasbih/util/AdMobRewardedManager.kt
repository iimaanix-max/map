package com.tasbih.digitaltasbih.util

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

/**
 * Google AdMob Rewarded Ad Manager for Digital Tasbih.
 * Manages background preloading, rewarded video playback, and strict reward enforcement.
 *
 * PRODUCTION GUARANTEE:
 * Rewards are ONLY unlocked when a user legitimately finishes watching the rewarded video.
 * No fake/demo dialog granting unearned unlocks without ad view.
 * All requests are strictly guarded by [ConsentManager.canRequestAds].
 */
object AdMobRewardedManager {
    private const val TAG = "AdMobRewarded"
    private val ADMOB_REWARDED_UNIT_ID: String
        get() = AdConfig.REWARDED_AD_UNIT_ID

    private var rewardedAd: RewardedAd? = null
    private var isLoading = false
    private var isShowing = false

    fun loadAd(context: Context) {
        if (!ConsentManager.canRequestAds(context)) {
            Log.d(TAG, "Postponing rewarded ad load: Waiting for user consent (canRequestAds=false)")
            ConsentManager.addConsentListener {
                loadAd(context)
            }
            return
        }

        if (rewardedAd != null || isLoading) return
        isLoading = true

        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            context.applicationContext,
            ADMOB_REWARDED_UNIT_ID,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    isLoading = false
                    Log.d(TAG, "Rewarded ad loaded successfully")
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    rewardedAd = null
                    isLoading = false
                    Log.w(TAG, "Rewarded ad FAILED — Code: ${error.code} | ${error.message} | Cause: ${error.cause}")
                    FirebaseAdTracker.logAdLoadFailure("rewarded", "rewarded_unit", error)
                }
            }
        )
    }

    fun isAdLoaded(): Boolean = rewardedAd != null

    fun showAd(
        activity: Activity,
        itemName: String = "Premium Item",
        onRewarded: () -> Unit
    ) {
        val ad = rewardedAd
        if (ad != null && !isShowing) {
            var rewardEarned = false

            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdShowedFullScreenContent() {
                    isShowing = true
                    Log.d(TAG, "Rewarded ad opened full screen for: $itemName")
                }

                override fun onAdDismissedFullScreenContent() {
                    isShowing = false
                    rewardedAd = null
                    loadAd(activity) // Preload next ad
                    if (rewardEarned) {
                        activity.runOnUiThread {
                            onRewarded()
                        }
                    } else {
                        activity.runOnUiThread {
                            Toast.makeText(
                                activity,
                                "Please watch the full ad to claim your reward.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    isShowing = false
                    rewardedAd = null
                    FirebaseAdTracker.logAdShowFailure("rewarded", itemName, adError)
                    loadAd(activity)
                    activity.runOnUiThread {
                        Toast.makeText(
                            activity,
                            "Ad failed to display. Please try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            ad.show(activity) { rewardItem ->
                rewardEarned = true
                Log.d(TAG, "User earned reward: ${rewardItem.type} (${rewardItem.amount}) for $itemName")
            }
        } else {
            // Ad not ready: trigger background load and notify user without granting free unlock
            loadAd(activity)
            activity.runOnUiThread {
                Toast.makeText(
                    activity,
                    "Video ad is loading. Please check connection and try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
