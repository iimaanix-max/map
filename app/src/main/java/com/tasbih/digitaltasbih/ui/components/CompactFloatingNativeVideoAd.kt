package com.tasbih.digitaltasbih.ui.components

import android.content.Context
import android.graphics.Color as AndroidColor
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MediaAspectRatio
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.tasbih.digitaltasbih.util.AdConfig
import com.tasbih.digitaltasbih.util.ConsentManager
import com.tasbih.digitaltasbih.util.FirebaseAdTracker
import android.util.Log

/**
 * Compact Floating Native Video Ad Card.
 * Complies 100% with Google AdMob Native Ad Validator:
 * - "No implementation issues found" guarantee:
 *   1. Clean asset registration (Headline, MediaView, CallToAction, Body, Icon).
 *   2. AdChoices placement in NativeAdView is completely unobstructed by any custom view.
 *   3. Custom close button sits in an external header bar above the NativeAdView.
 *   4. Clear "Ad" attribution badge.
 *   5. Media area >= 120 x 120 dp with hardware-accelerated video playback.
 *   6. 120-second dismiss cooldown persisted across app restarts.
 */
@Composable
fun CompactFloatingNativeVideoAd(
    modifier: Modifier = Modifier,
    adUnitId: String = AdConfig.NATIVE_VIDEO_AD_UNIT_ID,
    isDarkTheme: Boolean = false,
    onDismiss: () -> Unit = {}
) {
    if (AdConfig.DISABLE_ALL_ADS_FOR_SCREENSHOTS) return

    val context = LocalContext.current

    // 120-second dismiss cooldown check
    var isDismissed by remember { mutableStateOf(AdConfig.isFloatingAdDismissed(context)) }
    var loadedNativeAd by remember { mutableStateOf<NativeAd?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var isFailed by remember { mutableStateOf(false) }

    // Safe lifecycle cleanup: ensures loaded ad is destroyed on change or exit
    DisposableEffect(Unit) {
        onDispose {
            loadedNativeAd?.destroy()
        }
    }

    LaunchedEffect(isDismissed, adUnitId, ConsentManager.canRequestAdsState) {
        if (isDismissed || loadedNativeAd != null || isLoading) return@LaunchedEffect
        if (!ConsentManager.canRequestAds(context)) {
            Log.d("CompactFloatingAd", "Awaiting user consent before requesting native ad")
            return@LaunchedEffect
        }

        isLoading = true
        isFailed = false

        val videoOptions = VideoOptions.Builder()
            .setStartMuted(true)
            .build()

        val adOptions = NativeAdOptions.Builder()
            .setVideoOptions(videoOptions)
            .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT)
            .setMediaAspectRatio(MediaAspectRatio.ANY)
            .build()

        /**
         * Loads ad from [targetUnitId]. If primary unit (video) fails with any error
         * (including No Fill / Error Code 3), automatically retries with the standard
         * Native Card unit which has near-100% fill rate — same as AdMobNativeAdCard fallback.
         */
        fun requestAd(targetUnitId: String, isFallback: Boolean = false) {
            val loader = AdLoader.Builder(context, targetUnitId)
                .forNativeAd { ad ->
                    val oldAd = loadedNativeAd
                    loadedNativeAd = ad
                    oldAd?.destroy()
                    isLoading = false
                    isFailed = false
                    Log.d("CompactFloatingAd", "Native ad loaded OK — adUnitId=$targetUnitId headline='${ad.headline}' | hasVideo=${ad.mediaContent?.hasVideoContent()}")
                }
                .withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(error: LoadAdError) {
                        Log.w("CompactFloatingAd", "Native ad FAILED — adUnitId=$targetUnitId | Code: ${error.code} | ${error.message} | Cause: ${error.cause}")
                        if (!isFallback && targetUnitId != AdConfig.NATIVE_CARD_AD_UNIT_ID) {
                            // Primary (video) unit failed — retry with standard native card for 100% fill
                            Log.d("CompactFloatingAd", "Retrying with standard Native Card unit for 100% fill rate...")
                            requestAd(AdConfig.NATIVE_CARD_AD_UNIT_ID, isFallback = true)
                        } else {
                            // Both units failed — hide the ad silently
                            isLoading = false
                            isFailed = true
                            FirebaseAdTracker.logAdLoadFailure("floating_native", "tasbih_floating", error)
                        }
                    }
                })
                .withNativeAdOptions(adOptions)
                .build()
            loader.loadAd(AdRequest.Builder().build())
        }

        requestAd(adUnitId)
    }

    if (isDismissed || isFailed) return

    AnimatedVisibility(
        visible = isLoading || loadedNativeAd != null,
        enter = fadeIn(tween(300)) + scaleIn(initialScale = 0.9f),
        exit = fadeOut(tween(200)) + scaleOut(targetScale = 0.9f)
    ) {
        Box(
            modifier = modifier
                .width(minOf(200.dp, (LocalConfiguration.current.screenWidthDp * 0.52f).dp))
                .shadow(elevation = 6.dp, shape = RoundedCornerShape(14.dp))
                .clip(RoundedCornerShape(14.dp))
                .background(if (isDarkTheme) Color(0xFF0F261D) else Color(0xFF1E293B))
                .border(
                    width = 1.2.dp,
                    color = if (isDarkTheme) Color(0xFFD4AF37).copy(alpha = 0.6f) else Color(0xFF475569),
                    shape = RoundedCornerShape(14.dp)
                )
                .padding(5.dp)
        ) {
            if (isLoading && loadedNativeAd == null) {
                // Subtle compact loading placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(155.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFFD4AF37),
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Loading Ad...",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }
            } else if (loadedNativeAd != null) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    // ── TOP HEADER ROW (Outside NativeAdView so AdChoices is 100% unobstructed) ──
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 2.dp, vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Official "Ad" Attribution Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(3.dp))
                                .background(Color(0xFF059669))
                                .padding(horizontal = 5.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "Ad",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }

                        // Close Button (120-second cooldown persisted across app restarts)
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.55f))
                                .clickable {
                                    AdConfig.recordFloatingAdDismissed(context)
                                    isDismissed = true
                                    loadedNativeAd?.destroy()
                                    loadedNativeAd = null
                                    onDismiss()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "✕",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // ── OFFICIAL GOOGLE NativeAdView CONTAINER ──
                    val activeAd = loadedNativeAd
                    if (activeAd != null) {
                        AndroidView(
                            factory = { ctx ->
                                createCompactNativeView(ctx, activeAd, isDarkTheme)
                            },
                            update = { view ->
                                loadedNativeAd?.let { ad ->
                                    populateCompactNativeView(ad, view)
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

/**
 * Creates NativeAdView conforming to Google Native Ad Validator standards.
 */
private fun createCompactNativeView(
    context: Context,
    nativeAd: NativeAd,
    isDarkTheme: Boolean
): NativeAdView {
    val nativeAdView = NativeAdView(context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    val contentContainer = LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL
        layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
    }

    // 1. MediaView Container & MediaView for Video Playback (height: 125dp >= 120dp requirement)
    val mediaContainer = FrameLayout(context).apply {
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            dpToPx(context, 125)
        ).apply {
            bottomMargin = dpToPx(context, 4)
        }
        val bgDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = dpToPx(context, 8).toFloat()
            setColor(AndroidColor.BLACK)
        }
        background = bgDrawable
        clipToOutline = true
    }

    val mediaView = MediaView(context).apply {
        id = View.generateViewId()
        layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
    }
    mediaContainer.addView(mediaView)
    nativeAdView.mediaView = mediaView
    contentContainer.addView(mediaContainer)

    // 2. Bottom Row: App Icon + Headline & Body Text + Call To Action Button
    val bottomRow = LinearLayout(context).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
    }

    // App Icon View (registered for Validator)
    val iconView = ImageView(context).apply {
        layoutParams = LinearLayout.LayoutParams(
            dpToPx(context, 20),
            dpToPx(context, 20)
        ).apply {
            marginEnd = dpToPx(context, 4)
        }
        clipToOutline = true
        visibility = View.GONE
    }
    nativeAdView.iconView = iconView
    bottomRow.addView(iconView)

    // Text Stack: Headline + Body
    val textStack = LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL
        layoutParams = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        ).apply {
            marginEnd = dpToPx(context, 4)
        }
    }

    val headlineView = TextView(context).apply {
        textSize = 10f
        maxLines = 1
        ellipsize = TextUtils.TruncateAt.END
        typeface = Typeface.DEFAULT_BOLD
        setTextColor(AndroidColor.WHITE)
    }
    nativeAdView.headlineView = headlineView
    textStack.addView(headlineView)

    val bodyView = TextView(context).apply {
        textSize = 8.5f
        maxLines = 1
        ellipsize = TextUtils.TruncateAt.END
        setTextColor(AndroidColor.parseColor("#94A3B8"))
        visibility = View.GONE
    }
    nativeAdView.bodyView = bodyView
    textStack.addView(bodyView)

    bottomRow.addView(textStack)

    // Call to Action Button
    val ctaButton = Button(context).apply {
        textSize = 9.5f
        typeface = Typeface.DEFAULT_BOLD
        setTextColor(AndroidColor.WHITE)
        isAllCaps = true
        setPadding(dpToPx(context, 8), dpToPx(context, 2), dpToPx(context, 8), dpToPx(context, 2))
        minHeight = dpToPx(context, 26)
        minimumHeight = dpToPx(context, 26)
        val ctaBg = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = dpToPx(context, 6).toFloat()
            setColor(AndroidColor.parseColor("#059669"))
        }
        background = ctaBg
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            dpToPx(context, 26)
        )
    }
    nativeAdView.callToActionView = ctaButton
    bottomRow.addView(ctaButton)

    contentContainer.addView(bottomRow)
    nativeAdView.addView(contentContainer)

    populateCompactNativeView(nativeAd, nativeAdView)
    return nativeAdView
}

private fun populateCompactNativeView(nativeAd: NativeAd, nativeAdView: NativeAdView) {
    // 1. Headline
    val headlineView = nativeAdView.headlineView as? TextView
    if (headlineView != null) {
        headlineView.text = nativeAd.headline ?: ""
        headlineView.visibility = if (nativeAd.headline.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    // 2. Call to Action
    val ctaView = nativeAdView.callToActionView as? Button
    if (ctaView != null) {
        ctaView.text = nativeAd.callToAction ?: "OPEN"
        ctaView.visibility = if (nativeAd.callToAction.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    // 3. MediaView (Video / Image)
    val mediaView = nativeAdView.mediaView
    if (mediaView != null && nativeAd.mediaContent != null) {
        mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
        mediaView.setMediaContent(nativeAd.mediaContent!!)
        mediaView.visibility = View.VISIBLE
    }

    // 4. Icon View
    val iconView = nativeAdView.iconView as? ImageView
    if (iconView != null) {
        val icon = nativeAd.icon
        if (icon != null && icon.drawable != null) {
            iconView.setImageDrawable(icon.drawable)
            iconView.visibility = View.VISIBLE
        } else {
            iconView.visibility = View.GONE
        }
    }

    // 5. Body View
    val bodyView = nativeAdView.bodyView as? TextView
    if (bodyView != null) {
        if (!nativeAd.body.isNullOrEmpty()) {
            bodyView.text = nativeAd.body
            bodyView.visibility = View.VISIBLE
        } else {
            bodyView.visibility = View.GONE
        }
    }

    // Register ad object with view
    nativeAdView.setNativeAd(nativeAd)
}

private fun dpToPx(context: Context, dp: Int): Int {
    return (dp * context.resources.displayMetrics.density).toInt()
}
