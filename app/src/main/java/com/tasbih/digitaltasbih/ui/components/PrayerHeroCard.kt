package com.tasbih.digitaltasbih.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.data.AppStrings
import com.tasbih.digitaltasbih.data.PrayerDayData
import com.tasbih.digitaltasbih.data.PrayerItem
import com.tasbih.digitaltasbih.data.PrayerRepository
import com.tasbih.digitaltasbih.ui.theme.GoldAccent
import java.util.Calendar
import java.util.Locale

@Composable
fun PrayerHeroCard(
    modifier: Modifier = Modifier,
    strings: AppStrings,
    currentPrayer: PrayerItem? = null,
    nextPrayer: PrayerItem? = null,
    countdownText: String? = null,
    todayPrayerData: PrayerDayData? = null,
    isRamadanActive: Boolean = false,
    onClick: () -> Unit
) {
    val heroGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF1E4A2C),
            Color(0xFF123321),
            Color(0xFF081810)
        )
    )

    fun getPrayerDisplayName(prayerId: String?): String {
        return when (prayerId?.lowercase()) {
            "fajr" -> strings.fajr
            "dhuhr" -> strings.dhuhr
            "asr" -> strings.asr
            "maghrib" -> strings.maghrib
            "isha" -> strings.isha
            else -> strings.fajr
        }
    }

    val currentName = if (currentPrayer != null) getPrayerDisplayName(currentPrayer.id) else ""
    val liveClockTime = remember(countdownText) {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR)
        val h12 = if (hour == 0) 12 else hour
        val minute = cal.get(Calendar.MINUTE)
        val ampm = if (cal.get(Calendar.AM_PM) == Calendar.PM) "pm" else "am"
        String.format(Locale.US, "%d:%02d %s", h12, minute, ampm)
    }

    val headlinePrayer = nextPrayer ?: currentPrayer
    val headlineLabel = if (nextPrayer != null) strings.next else if (currentPrayer != null) strings.now else strings.next
    val headlineName = if (headlinePrayer != null) getPrayerDisplayName(headlinePrayer.id) else strings.dhuhr
    val headlineTime = headlinePrayer?.displayTime ?: "11:51 am"
    val nextName = if (nextPrayer != null) getPrayerDisplayName(nextPrayer.id) else headlineName

    val dateFormatted = remember(todayPrayerData, strings) {
        if (todayPrayerData != null) {
            "${todayPrayerData.gregorianReadable} · ${todayPrayerData.hijriDay} ${todayPrayerData.hijriMonthName} ${todayPrayerData.hijriYear}"
        } else {
            strings.dateLabel
        }
    }
    val activeCountdown = countdownText ?: strings.countdown

    // Prefix next prayer name to the countdown (e.g. Dhuhr in 3:55:23 or Dhuhr in 25:34)
    val countdownWithPrefix = remember(nextName, activeCountdown) {
        if (nextName.isNotBlank()) {
            if (activeCountdown == "NOW" || activeCountdown == "अभी" || activeCountdown == "ابھی" || activeCountdown == "الآن") {
                "$nextName · $activeCountdown"
            } else {
                "$nextName in $activeCountdown"
            }
        } else {
            activeCountdown
        }
    }

    // Infinite transition for live blinking / breathing pulsing green dot
    val infiniteTransition = rememberInfiniteTransition(label = "nowDotPulse")
    val dotAlpha by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 650, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dotAlpha"
    )
    val dotScale by infiniteTransition.animateFloat(
        initialValue = 0.82f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 650, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dotScale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(heroGradient)
            .border(1.2.dp, Color(0x73E8C878), RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        // Mosque Silhouette Art Watermark
        val mosquePainter = rememberAssetPainter("media/ba6c9de700cd4f7f.webp")
        Image(
            painter = mosquePainter,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            alignment = Alignment.BottomEnd,
            alpha = if (isRamadanActive) 0.90f else 0.42f,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(175.dp)
        )

        Column {
            // Greeting & Bell inside circular cream button with gold rim
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isRamadanActive) strings.ramadanMubarak else strings.greeting,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE8C878)
                )

                // Bell button with festive golden glow and micro-star when Ramadan active
                val bellBorder = if (isRamadanActive) {
                    Brush.sweepGradient(
                        listOf(
                            Color(0xFFFFD54F),
                            Color(0xFFFFF8E1),
                            Color(0xFFD4AF37),
                            Color(0xFFFFD54F)
                        )
                    )
                } else {
                    Brush.linearGradient(listOf(GoldAccent, GoldAccent))
                }

                Box(
                    modifier = Modifier.size(36.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isRamadanActive) {
                        // Subtle breathing warm golden halo aura
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .graphicsLayer {
                                    alpha = dotAlpha * 0.45f
                                    scaleX = dotScale
                                    scaleY = dotScale
                                }
                                .clip(CircleShape)
                                .background(Color(0x66FFD54F))
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(if (isRamadanActive) Color(0xFFFFFDF5) else Color(0xFFFFF8EE))
                            .border(1.2.dp, bellBorder, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🔔", fontSize = 15.sp)
                    }

                    if (isRamadanActive) {
                        Text(
                            text = "✦",
                            fontSize = 9.sp,
                            color = Color(0xFFFFD54F),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 1.dp, y = (-2).dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Ongoing Prayer Pill (NOW) - Option 1: Live Digital Clock
            if (currentPrayer != null && currentName.isNotBlank()) {
                val nowCapsuleBorder = if (isRamadanActive) {
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFFFE082),
                            Color(0xFFFFD54F),
                            Color(0xFFFFF8E1),
                            Color(0xFFD4AF37),
                            Color(0xFFFFD54F)
                        )
                    )
                } else {
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFF6D365),
                            Color(0xFFD4AF37),
                            Color(0x80F6D365)
                        )
                    )
                }
                val nowCapsuleBg = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xF20A2D1F),
                        Color(0xDB061F15)
                    )
                )

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(nowCapsuleBg)
                        .border(1.2.dp, nowCapsuleBorder, RoundedCornerShape(16.dp))
                        .padding(horizontal = 13.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Blinking / Pulsing Live Beacon Green Dot
                    Box(
                        modifier = Modifier.size(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Outer glowing pulse halo
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .graphicsLayer {
                                    scaleX = dotScale
                                    scaleY = dotScale
                                    alpha = dotAlpha * 0.5f
                                }
                                .clip(CircleShape)
                                .background(Color(0xFF34D399))
                        )
                        // Inner solid blinking green dot
                        Box(
                            modifier = Modifier
                                .size(7.5.dp)
                                .graphicsLayer {
                                    alpha = dotAlpha
                                }
                                .clip(CircleShape)
                                .background(Color(0xFF10B981))
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = strings.now,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.8.sp,
                        color = Color.White
                    )

                    Text(
                        text = " ✦ ",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF6D365)
                    )

                    Text(
                        text = "$currentName · $liveClockTime",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFF0C2),
                        letterSpacing = 0.2.sp
                    )
                }

                Spacer(modifier = Modifier.height(13.dp))
            }

            // NEXT PRAYER Label
            Text(
                text = headlineLabel,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFE8C878),
                letterSpacing = 0.6.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Name + Time - Centered vertically on the same optical axis for all languages (Hindi, Urdu, English, etc.)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = headlineName,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = headlineTime,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF0D080)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Subtitle with date and prefixed countdown (e.g. 📅 21 Oct 2026 · 10 Jumādá al-Ūlá 1448 · Dhuhr in 3:55:23)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "📅 $dateFormatted · $countdownWithPrefix",
                    fontSize = 10.5.sp,
                    color = Color(0xD8E8DCBE),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

