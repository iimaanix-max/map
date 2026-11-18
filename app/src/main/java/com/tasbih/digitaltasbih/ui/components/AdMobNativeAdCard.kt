package com.tasbih.digitaltasbih.ui.components

import android.content.Context
import android.graphics.Color as AndroidColor
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import android.util.Log
import com.tasbih.digitaltasbih.util.FirebaseAdTracker

/**
 * Real Google AdMob Native Video Ad Card with Test Ad Unit ID.
 * Uses centralized AdConfig IDs. Supports Native Video and Native Advanced.
 * Seamlessly adapts to Day & Night Themes.
 * If showPlaceholder is false, takes 0 height when not loaded and smoothly expands in when loaded.
 */
@Composable
fun AdMobNativeAdCard(
    adUnitId: String = AdConfig.NATIVE_VIDEO_AD_UNIT_ID,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = false,
    showPlaceholder: Boolean = false
) {
    if (AdConfig.DISABLE_ALL_ADS_FOR_SCREENSHOTS) return

    val context = LocalContext.current
    var loadedNativeAd by remember { mutableStateOf<NativeAd?>(null) }
    var isAdLoaded by remember { mutableStateOf(false) }

    // Safe lifecycle cleanup for NativeAd instance on leaving composition
    DisposableEffect(Unit) {
        onDispose {
            loadedNativeAd?.destroy()
        }
    }

    LaunchedEffect(adUnitId, ConsentManager.canRequestAdsState) {
        if (!ConsentManager.canRequestAds(context)) {
            Log.d("AdMobNativeAdCard", "Awaiting user consent before requesting native ad")
            return@LaunchedEffect
        }

        fun requestNativeAd(targetUnitId: String, isFallback: Boolean = false) {
            val loader = AdLoader.Builder(context, targetUnitId)
                .forNativeAd { nativeAd ->
                    Log.d("AdMobNativeAdCard", "Native ad ($targetUnitId) loaded OK: '${nativeAd.headline}' | hasVideo=${nativeAd.mediaContent?.hasVideoContent()} | duration=${nativeAd.mediaContent?.duration}")
                    val oldAd = loadedNativeAd
                    loadedNativeAd = nativeAd
                    oldAd?.destroy()
                    isAdLoaded = true
                }
                .withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(error: LoadAdError) {
                        Log.w("AdMobNativeAdCard", "Native ad ($targetUnitId) failed to load: ${error.message} (Code: ${error.code})")
                        FirebaseAdTracker.logAdLoadFailure("native", "native_card", error)
                        // If primary unit (e.g. video) has No Fill or fails, fallback to standard Native Card unit for 100% fill
                        if (!isFallback && targetUnitId != AdConfig.NATIVE_CARD_AD_UNIT_ID) {
                            Log.d("AdMobNativeAdCard", "Retrying with standard Native Card Ad Unit for 100% fill rate...")
                            requestNativeAd(AdConfig.NATIVE_CARD_AD_UNIT_ID, isFallback = true)
                        } else {
                            isAdLoaded = false
                            loadedNativeAd = null
                        }
                    }
                })
                .withNativeAdOptions(
                    NativeAdOptions.Builder()
                        .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT)
                        .setMediaAspectRatio(MediaAspectRatio.ANY)
                        .setVideoOptions(
                            VideoOptions.Builder()
                                .setStartMuted(true)
                                .build()
                        )
                        .build()
                )
                .build()

            loader.loadAd(AdRequest.Builder().build())
        }

        requestNativeAd(adUnitId)
    }

    if (showPlaceholder) {
        AnimatedContent(
            targetState = isAdLoaded && loadedNativeAd != null,
            transitionSpec = { fadeIn(tween(400)) togetherWith fadeOut(tween(400)) },
            label = "AdMobNativeAdAnimation",
            modifier = modifier
        ) { hasAd ->
            if (hasAd && loadedNativeAd != null) {
                loadedNativeAd?.let { nativeAd ->
                    AndroidView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        factory = { ctx ->
                            createNativeAdView(ctx, nativeAd, isDarkTheme)
                        },
                        update = { view ->
                            populateNativeAdView(nativeAd, view)
                        }
                    )
                }
            } else {
                AdMobLoadingPlaceholderCard(isDarkTheme = isDarkTheme)
            }
        }
    } else {
        AnimatedVisibility(
            visible = isAdLoaded && loadedNativeAd != null,
            enter = fadeIn(animationSpec = tween(500)) + expandVertically(animationSpec = tween(500)),
            exit = fadeOut(animationSpec = tween(300)) + shrinkVertically(animationSpec = tween(300)),
            modifier = modifier
        ) {
            loadedNativeAd?.let { nativeAd ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    AndroidView(
                        modifier = Modifier.fillMaxWidth(),
                        factory = { ctx ->
                            createNativeAdView(ctx, nativeAd, isDarkTheme)
                        },
                        update = { view ->
                            populateNativeAdView(nativeAd, view)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AdMobLoadingPlaceholderCard(isDarkTheme: Boolean = false) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkTheme) Color(0xFF132820) else Color.White
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isDarkTheme) Color(0xFF1E3D32) else Color(0xFFE2E8F0)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isDarkTheme) Color(0xFF1A3A2C) else Color(0xFFFEF3C7))
                            .border(
                                1.dp,
                                if (isDarkTheme) Color(0xFF2D5A47) else Color(0xFFFDE68A),
                                RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Ad",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) Color(0xFFE5B036) else Color(0xFF92400E)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Sponsored Video",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)
                    )
                }

                if (AdConfig.USE_TEST_ADS) {
                    Text(
                        text = "Google AdMob Test Ad",
                        fontSize = 10.sp,
                        color = if (isDarkTheme) Color(0xFF64748B) else Color(0xFF94A3B8)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = if (isDarkTheme) listOf(
                                Color(0xFF091F16),
                                Color(0xFF0D281D)
                            ) else listOf(
                                Color(0xFFF1F5F9),
                                Color(0xFFE2E8F0)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(28.dp),
                        color = Color(0xFF10B981),
                        strokeWidth = 2.5.dp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Loading Google AdMob Video Ad...",
                        fontSize = 12.sp,
                        color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF0F5132)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Watch / Install Now",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

private fun createNativeAdView(
    context: Context,
    nativeAd: NativeAd,
    isDarkTheme: Boolean = false
): NativeAdView {
    val nativeAdView = NativeAdView(context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    val cardContainer = LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL
        layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        setPadding(dpToPx(context, 14), dpToPx(context, 12), dpToPx(context, 14), dpToPx(context, 12))

        val bgDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = dpToPx(context, 16).toFloat()
            setColor(
                if (isDarkTheme) AndroidColor.parseColor("#132820")
                else AndroidColor.WHITE
            )
            setStroke(
                dpToPx(context, 1),
                if (isDarkTheme) AndroidColor.parseColor("#1E3D32")
                else AndroidColor.parseColor("#E2E8F0")
            )
        }
        background = bgDrawable
        elevation = dpToPx(context, 2).toFloat()
    }

    val topRow = LinearLayout(context).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            bottomMargin = dpToPx(context, 8)
        }
    }

    val adBadge = TextView(context).apply {
        text = "Ad"
        textSize = 10f
        typeface = Typeface.DEFAULT_BOLD
        setTextColor(
            if (isDarkTheme) AndroidColor.parseColor("#E5B036")
            else AndroidColor.parseColor("#92400E")
        )
        setPadding(dpToPx(context, 6), dpToPx(context, 2), dpToPx(context, 6), dpToPx(context, 2))
        background = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = dpToPx(context, 4).toFloat()
            setColor(
                if (isDarkTheme) AndroidColor.parseColor("#1A3A2C")
                else AndroidColor.parseColor("#FEF3C7")
            )
            setStroke(
                dpToPx(context, 1),
                if (isDarkTheme) AndroidColor.parseColor("#2D5A47")
                else AndroidColor.parseColor("#FDE68A")
            )
        }
    }
    topRow.addView(adBadge)

    val advertiserView = TextView(context).apply {
        id = View.generateViewId()
        textSize = 11f
        setTextColor(
            if (isDarkTheme) AndroidColor.parseColor("#94A3B8")
            else AndroidColor.parseColor("#64748B")
        )
        layoutParams = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        ).apply {
            leftMargin = dpToPx(context, 8)
        }
    }
    nativeAdView.advertiserView = advertiserView
    topRow.addView(advertiserView)

    if (AdConfig.USE_TEST_ADS) {
        val admobTag = TextView(context).apply {
            text = "Google AdMob Test Ad"
            textSize = 10f
            setTextColor(
                if (isDarkTheme) AndroidColor.parseColor("#64748B")
                else AndroidColor.parseColor("#94A3B8")
            )
        }
        topRow.addView(admobTag)
    }

    cardContainer.addView(topRow)

    val contentRow = LinearLayout(context).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.TOP
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            bottomMargin = dpToPx(context, 10)
        }
    }

    val iconView = ImageView(context).apply {
        id = View.generateViewId()
        val s = dpToPx(context, 44)
        layoutParams = LinearLayout.LayoutParams(s, s).apply {
            rightMargin = dpToPx(context, 10)
        }
        val iconBg = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = dpToPx(context, 10).toFloat()
        }
        clipToOutline = true
        background = iconBg
    }
    nativeAdView.iconView = iconView
    contentRow.addView(iconView)

    val textCol = LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL
        layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
    }

    val headlineView = TextView(context).apply {
        id = View.generateViewId()
        textSize = 14f
        typeface = Typeface.DEFAULT_BOLD
        setTextColor(
            if (isDarkTheme) AndroidColor.parseColor("#F1F5F9")
            else AndroidColor.parseColor("#0F172A")
        )
        maxLines = 1
    }
    nativeAdView.headlineView = headlineView
    textCol.addView(headlineView)

    val bodyView = TextView(context).apply {
        id = View.generateViewId()
        textSize = 12f
        setTextColor(
            if (isDarkTheme) AndroidColor.parseColor("#94A3B8")
            else AndroidColor.parseColor("#475569")
        )
        maxLines = 2
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = dpToPx(context, 2)
        }
    }
    nativeAdView.bodyView = bodyView
    textCol.addView(bodyView)

    contentRow.addView(textCol)
    cardContainer.addView(contentRow)

    val mediaContainer = FrameLayout(context).apply {
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            dpToPx(context, 180)
        ).apply {
            bottomMargin = dpToPx(context, 10)
        }
        val mediaBg = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = dpToPx(context, 10).toFloat()
            setColor(
                if (isDarkTheme) AndroidColor.parseColor("#0A1F17")
                else AndroidColor.parseColor("#0F172A")
            )
        }
        clipToOutline = true
        background = mediaBg
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
    cardContainer.addView(mediaContainer)

    val ctaButton = Button(context).apply {
        id = View.generateViewId()
        textSize = 13.5f
        typeface = Typeface.DEFAULT_BOLD
        setTextColor(AndroidColor.WHITE)
        background = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = dpToPx(context, 10).toFloat()
            setColor(AndroidColor.parseColor("#0F5132"))
        }
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            dpToPx(context, 42)
        )
    }
    nativeAdView.callToActionView = ctaButton
    cardContainer.addView(ctaButton)

    nativeAdView.addView(cardContainer)
    populateNativeAdView(nativeAd, nativeAdView)

    return nativeAdView
}

private fun populateNativeAdView(nativeAd: NativeAd, nativeAdView: NativeAdView) {
    (nativeAdView.headlineView as? TextView)?.text = nativeAd.headline

    val body = nativeAd.body
    val bodyView = nativeAdView.bodyView as? TextView
    if (body != null) {
        bodyView?.visibility = View.VISIBLE
        bodyView?.text = body
    } else {
        bodyView?.visibility = View.GONE
    }

    val cta = nativeAd.callToAction
    val ctaView = nativeAdView.callToActionView as? Button
    if (cta != null) {
        ctaView?.visibility = View.VISIBLE
        ctaView?.text = cta
    } else {
        ctaView?.visibility = View.GONE
    }

    val icon = nativeAd.icon
    val iconView = nativeAdView.iconView as? ImageView
    if (icon != null) {
        iconView?.visibility = View.VISIBLE
        iconView?.setImageDrawable(icon.drawable)
    } else {
        iconView?.visibility = View.GONE
    }

    val advertiser = nativeAd.advertiser
    val advertiserView = nativeAdView.advertiserView as? TextView
    if (advertiser != null) {
        advertiserView?.visibility = View.VISIBLE
        advertiserView?.text = advertiser
    } else {
        advertiserView?.visibility = View.GONE
    }

    val mediaView = nativeAdView.mediaView
    if (mediaView != null && nativeAd.mediaContent != null) {
        mediaView.visibility = View.VISIBLE
        mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
        mediaView.mediaContent = nativeAd.mediaContent
        val vc = nativeAd.mediaContent?.videoController
        Log.d("AdMobNativeAdCard", "MediaView bound: hasVideo=${nativeAd.mediaContent?.hasVideoContent()} duration=${nativeAd.mediaContent?.duration} hasVC=${vc != null}")
    } else {
        mediaView?.visibility = View.GONE
    }

    nativeAdView.setNativeAd(nativeAd)
}

private fun dpToPx(context: Context, dp: Int): Int {
    return (dp * context.resources.displayMetrics.density).toInt()
}
