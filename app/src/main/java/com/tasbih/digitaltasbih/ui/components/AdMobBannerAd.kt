package com.tasbih.digitaltasbih.ui.components

import android.content.Context
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.tasbih.digitaltasbih.util.AdConfig
import com.tasbih.digitaltasbih.util.ConsentManager
import com.tasbih.digitaltasbih.util.FirebaseAdTracker

/**
 * Official Google AdMob Anchored Adaptive Banner Ad.
 * Follows Google's official best practice guidance for adaptive banner ads:
 * - Strictly waits for user consent ([ConsentManager.canRequestAds]) before initializing or loading.
 * - Dynamically calculates ad size based on current device width & orientation via
 *   [AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize].
 * - Prevents layout clipping, black borders, and layout shifts across all screen widths & tablets.
 * - Stays 0dp height (completely invisible) while loading, awaiting consent, or if loading fails.
 * - Smoothly animates into the exact adaptive banner height when filled.
 */
@Composable
fun AdMobBannerAd(
    modifier: Modifier = Modifier,
    adUnitId: String = AdConfig.BANNER_AD_UNIT_ID,
    isDarkTheme: Boolean = false,
    onAdLoadedStateChanged: (Boolean) -> Unit = {}
) {
    if (AdConfig.DISABLE_ALL_ADS_FOR_SCREENSHOTS) return

    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp

    // Calculate official Google Anchored Adaptive Banner Size
    val adaptiveAdSize = remember(screenWidthDp, context) {
        val safeWidth = if (screenWidthDp > 0) screenWidthDp else 320
        AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, safeWidth)
    }

    // Dynamic height in dp
    val bannerHeightDp = remember(adaptiveAdSize, context) {
        val heightPx = adaptiveAdSize.getHeightInPixels(context)
        val density = context.resources.displayMetrics.density
        if (density > 0f) heightPx / density else 50f
    }

    var isAdLoaded by remember { mutableStateOf(false) }

    // Consent Gate: Strictly verify consent before creating or loading AdView
    val canRequestAds = ConsentManager.canRequestAdsState || ConsentManager.canRequestAds(context)

    val adView = remember(adUnitId, adaptiveAdSize, canRequestAds) {
        if (!canRequestAds) {
            Log.d("AdMobBannerAd", "Awaiting user consent before loading banner (canRequestAds=false)")
            null
        } else {
            AdView(context).apply {
                setAdSize(adaptiveAdSize)
                this.adUnitId = adUnitId
                adListener = object : AdListener() {
                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        Log.d("AdMobBannerAd", "Adaptive Banner Ad loaded: ${adaptiveAdSize.width}x${adaptiveAdSize.height} (${bannerHeightDp}dp)")
                        isAdLoaded = true
                        onAdLoadedStateChanged(true)
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        super.onAdFailedToLoad(error)
                        Log.w("AdMobBannerAd", "Adaptive Banner Ad failed to load: ${error.message} (Code: ${error.code})")
                        FirebaseAdTracker.logAdLoadFailure("banner", "adaptive_banner", error)
                        isAdLoaded = false
                        onAdLoadedStateChanged(false)
                    }
                }
                loadAd(AdRequest.Builder().build())
            }
        }
    }

    DisposableEffect(adView) {
        onDispose {
            adView?.destroy()
        }
    }

    // Container: 0dp when loading or failed; smoothly expands to exact adaptive banner height when loaded
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(if (isAdLoaded) (bannerHeightDp + 6).dp else 0.dp)
            .animateContentSize(tween(300))
            .alpha(if (isAdLoaded) 1f else 0f),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 2.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isDarkTheme) Color(0xFF0F261D) else Color(0xFFF1F5F9))
                    .border(
                        0.8.dp,
                        if (isDarkTheme) Color(0xFF1E3D32) else Color(0xFFCBD5E1),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(2.dp),
                contentAlignment = Alignment.Center
            ) {
                if (adView != null) {
                    AndroidView(
                        factory = { adView },
                        modifier = Modifier.wrapContentSize()
                    )
                }
            }
        }
    }
}
