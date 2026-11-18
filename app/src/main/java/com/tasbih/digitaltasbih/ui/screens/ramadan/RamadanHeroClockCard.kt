package com.tasbih.digitaltasbih.ui.screens.ramadan

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.alpha
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.R
import com.tasbih.digitaltasbih.data.PrayerDayData
import com.tasbih.digitaltasbih.ui.components.rememberAssetPainter
import java.util.Calendar

import com.tasbih.digitaltasbih.data.ramadan.RamadanLocalization

/**
 * Pixel-perfect implementation using the user-provided PNG template (R.drawable.ramadan_hero_clock_template):
 * - Ornate Islamic Mihrab Arch at top with localized Ashra and Day.
 * - Localized "TIME LEFT TO IFTAR" (or Sehri / Suhoor) countdown divider label.
 * - 3D Emerald Countdown Boxes for HOURS, MINUTES, and SECONDS (Numbers inside, units outside below).
 * - Sehri and Iftar cards with localized titles, live prayer times and clean AM/PM.
 * - Bottom Pill with localized "View Sehri & Iftar Duas →".
 */
@Composable
fun RamadanHeroClockCard(
    todayData: PrayerDayData?,
    currentTimeMillis: Long,
    isDarkTheme: Boolean = true,
    language: String = "en",
    currentDay: Int = 1,
    onOpenDua: (Int) -> Unit // 0: Sehri, 1: Iftar, 2: 3 Ashras
) {
    val strings = RamadanLocalization.get(language)
    val fajrTime = todayData?.timings?.get("Fajr") ?: "04:52"
    val maghribTime = todayData?.timings?.get("Maghrib") ?: "17:33"

    // Parse time strings in HH:mm
    fun parseMins(timeStr: String): Int {
        val parts = timeStr.split(":")
        if (parts.size >= 2) {
            val h = parts[0].trim().toIntOrNull() ?: 0
            val m = parts[1].trim().toIntOrNull() ?: 0
            return h * 60 + m
        }
        return 0
    }

    val cal = Calendar.getInstance().apply { timeInMillis = currentTimeMillis }
    val nowMins = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
    val fajrMins = parseMins(fajrTime)
    val maghribMins = parseMins(maghribTime)

    // Convert 24-hr time to 12-hr with AM/PM
    fun format12Hour(time24: String, defaultAmPm: String): Pair<String, String> {
        val clean = time24.trim()
        val parts = clean.split(":")
        if (parts.size >= 2) {
            val h = parts[0].trim().toIntOrNull() ?: return Pair(clean, defaultAmPm)
            val m = parts[1].trim().take(2).toIntOrNull() ?: 0
            val isPm = h >= 12
            val h12 = when {
                h == 0 -> 12
                h > 12 -> h - 12
                else -> h
            }
            val amPm = if (isPm) strings.pm else strings.am
            return Pair("%02d:%02d".format(h12, m), amPm)
        }
        return Pair(clean, defaultAmPm)
    }

    val (fajrTime12, fajrAmPm) = format12Hour(fajrTime, strings.am)
    val (maghribTime12, maghribAmPm) = format12Hour(maghribTime, strings.pm)

    val isFastingHours = nowMins in fajrMins until maghribMins
    val isAfterIftar = nowMins >= maghribMins

    val countdownLabel = if (isFastingHours) {
        strings.timeLeftIftar
    } else if (isAfterIftar) {
        strings.timeLeftSuhoor
    } else {
        strings.timeLeftSehri
    }

    val remainingMins = if (isFastingHours) {
        (maghribMins - nowMins).coerceAtLeast(0)
    } else if (isAfterIftar) {
        ((24 * 60 - nowMins) + fajrMins).coerceAtLeast(0)
    } else {
        (fajrMins - nowMins).coerceAtLeast(0)
    }

    val hours = remainingMins / 60
    val mins = remainingMins % 60
    val sec = 59 - cal.get(Calendar.SECOND)

    val hoursStr = "%02d".format(hours)
    val minsStr = "%02d".format(mins)
    val secStr = "%02d".format(sec)

    val ashraTitle = when {
        currentDay <= 10 -> strings.ashra1Title
        currentDay <= 20 -> strings.ashra2Title
        else -> strings.ashra3Title
    }
    val dayText = "${strings.dayPrefix} $currentDay"

    // Dynamic Blinking Lights & Floral Animations
    val infiniteTransition = rememberInfiniteTransition(label = "heroClockAnim")
    val lanternGlow by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "lanternGlow"
    )
    val flowerBlink1 by infiniteTransition.animateFloat(
        initialValue = 0.20f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flowerBlink1"
    )
    val flowerBlink2 by infiniteTransition.animateFloat(
        initialValue = 0.90f,
        targetValue = 0.20f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flowerBlink2"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 28.dp)
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1024f / 859f)
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(26.dp),
                    ambientColor = Color(0x33000000),
                    spotColor = Color(0x40D4AF37)
                )
                .clip(RoundedCornerShape(26.dp))
        ) {
            val totalW = maxWidth
            val totalH = maxHeight

        // 1. HIGH-RES BACKGROUND TEMPLATE GRAPHIC (Day: Cream & Gold, Night: Deep Midnight Obsidian & Emerald)
        Image(
            painter = painterResource(
                id = if (isDarkTheme) R.drawable.ramadan_hero_clock_template_night else R.drawable.ramadan_hero_clock_template
            ),
            contentDescription = "Ramadan Hero Card",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        // 1B. DYNAMIC BLINKING LANTERN GLOW (Warm golden flame aura over both hanging lanterns)
        // Left Lantern Flame
        Box(
            modifier = Modifier
                .size(totalW * 0.12f)
                .offset(x = totalW * 0.038f, y = totalH * 0.170f)
                .background(
                    Brush.radialGradient(
                        listOf(
                            Color(0xFFFFEE99).copy(alpha = lanternGlow * 0.85f),
                            Color(0xFFFFB800).copy(alpha = lanternGlow * 0.40f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        // Right Lantern Flame
        Box(
            modifier = Modifier
                .size(totalW * 0.12f)
                .offset(x = totalW * 0.842f, y = totalH * 0.170f)
                .background(
                    Brush.radialGradient(
                        listOf(
                            Color(0xFFFFEE99).copy(alpha = lanternGlow * 0.85f),
                            Color(0xFFFFB800).copy(alpha = lanternGlow * 0.40f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        // Top Arch Hanging Fairy Light Beads
        listOf(0.24f, 0.32f, 0.40f, 0.50f, 0.60f, 0.68f, 0.76f).forEachIndexed { i, frac ->
            val bAlpha = if (i % 2 == 0) flowerBlink1 else flowerBlink2
            Box(
                modifier = Modifier
                    .size(totalW * 0.016f)
                    .offset(
                        x = totalW * frac,
                        y = totalH * (0.052f + if (frac == 0.50f) 0.005f else 0.012f)
                    )
                    .alpha(bAlpha)
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0xFFFFF7C2), Color(0xFFFFB800), Color.Transparent)
                        ),
                        shape = CircleShape
                    )
            )
        }

        // Left Diamond Divider Sparkle
        Box(
            modifier = Modifier
                .offset(x = totalW * 0.368f, y = totalH * 0.435f)
                .alpha(flowerBlink2)
        ) {
            Text(
                text = "✦",
                fontSize = 10.sp,
                color = Color(0xFFFDE08A)
            )
        }

        // Right Diamond Divider Sparkle
        Box(
            modifier = Modifier
                .offset(x = totalW * 0.602f, y = totalH * 0.435f)
                .alpha(flowerBlink1)
        ) {
            Text(
                text = "✦",
                fontSize = 10.sp,
                color = Color(0xFFFDE08A)
            )
        }

        // 2. TOP MIHRAB ARCH BADGE (Ashra & Day - luxury emerald badge with gold border)
        Box(
            modifier = Modifier
                .width(totalW * 0.64f)
                .offset(x = totalW * 0.18f, y = totalH * 0.108f)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xF00A3828), Color(0xF8051E15))
                    )
                )
                .border(1.dp, Color(0x80D4AF37), RoundedCornerShape(16.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onOpenDua(2) }
                .padding(horizontal = 10.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "۞",
                        color = Color(0xFFD4AF37),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = ashraTitle,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        color = Color(0xFFFAF7EE),
                        letterSpacing = 0.5.sp,
                        maxLines = 1
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = dayText,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFDE8A8),
                    letterSpacing = 0.5.sp
                )
            }
        }

        // 3. "── TIME LEFT TO IFTAR ──" LABEL (Above countdown boxes, cleanly framed by gold lines with stars)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(totalH * 0.06f)
                .offset(y = totalH * 0.298f),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "✦",
                    fontSize = 9.sp,
                    color = if (isDarkTheme) Color(0xFFFDE8A8) else Color(0xFFB38928)
                )
                Text(
                    text = countdownLabel,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    letterSpacing = if (language == "en") 2.sp else 1.sp,
                    color = if (isDarkTheme) Color(0xFFFDE8A8) else Color(0xFF143024)
                )
                Text(
                    text = "✦",
                    fontSize = 9.sp,
                    color = if (isDarkTheme) Color(0xFFFDE8A8) else Color(0xFFB38928)
                )
            }
        }

        // 4. 3D COUNTDOWN TILES (Numbers INSIDE emerald boxes)
        val tileY = totalH * 0.364f
        val tileH = totalH * 0.156f

        // Tile 1: HOURS (Inside emerald tile)
        Box(
            modifier = Modifier
                .width(totalW * 0.146f)
                .height(tileH)
                .offset(x = totalW * 0.205f, y = tileY),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = hoursStr,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        // Tile 2: MIN (Inside emerald tile)
        Box(
            modifier = Modifier
                .width(totalW * 0.173f)
                .height(tileH)
                .offset(x = totalW * 0.413f, y = tileY),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = minsStr,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        // Tile 3: SEC (Inside emerald tile)
        Box(
            modifier = Modifier
                .width(totalW * 0.150f)
                .height(tileH)
                .offset(x = totalW * 0.645f, y = tileY),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = secStr,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        // 4B. UNITS OUTSIDE THE BOXES (In cream space below the emerald tiles)
        val unitY = totalH * 0.523f

        // Unit 1: HR / घंटे
        Box(
            modifier = Modifier
                .width(totalW * 0.146f)
                .offset(x = totalW * 0.205f, y = unitY),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = strings.hr,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = if (language == "en") 1.sp else 0.sp,
                color = if (isDarkTheme) Color(0xFFE8C878) else Color(0xFF143024),
                textAlign = TextAlign.Center,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                maxLines = 1
            )
        }

        // Unit 2: MIN / मिनट
        Box(
            modifier = Modifier
                .width(totalW * 0.173f)
                .offset(x = totalW * 0.413f, y = unitY),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = strings.min,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = if (language == "en") 1.sp else 0.sp,
                color = if (isDarkTheme) Color(0xFFE8C878) else Color(0xFF143024),
                textAlign = TextAlign.Center,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                maxLines = 1
            )
        }

        // Unit 3: SEC / सेकंड
        Box(
            modifier = Modifier
                .width(totalW * 0.150f)
                .offset(x = totalW * 0.645f, y = unitY),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = strings.sec,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = if (language == "en") 1.sp else 0.sp,
                color = if (isDarkTheme) Color(0xFFE8C878) else Color(0xFF143024),
                textAlign = TextAlign.Center,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                maxLines = 1
            )
        }

        // 5. SEHRI CARD CONTENT (Centered inside cream/dark slot: X 200-479, Y 520-639)
        Box(
            modifier = Modifier
                .width(totalW * 0.270f)
                .height(totalH * 0.138f)
                .offset(x = totalW * 0.200f, y = totalH * 0.605f),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = strings.sehri,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isDarkTheme) Color(0xFFFDE8A8) else Color(0xFF1B3B2B),
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = fajrTime12,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Serif,
                        color = if (isDarkTheme) Color.White else Color(0xFF0D281E)
                    )
                    Text(
                        text = fajrAmPm,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        color = if (isDarkTheme) Color(0xFFFDE8A8) else Color(0xFF0D281E),
                        modifier = Modifier.padding(bottom = 1.5.dp)
                    )
                }
            }
        }

        // Full clickable overlay for Sehri Card
        Box(
            modifier = Modifier
                .width(totalW * 0.44f)
                .height(totalH * 0.19f)
                .offset(x = totalW * 0.05f, y = totalH * 0.59f)
                .clip(RoundedCornerShape(16.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(color = Color(0x33D4AF37))
                ) { onOpenDua(0) }
        )

        // 6. IFTAR CARD CONTENT (Centered inside cream/dark slot: X 650-949, Y 520-639)
        Box(
            modifier = Modifier
                .width(totalW * 0.270f)
                .height(totalH * 0.138f)
                .offset(x = totalW * 0.655f, y = totalH * 0.605f),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = strings.iftar,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isDarkTheme) Color(0xFFFDE8A8) else Color(0xFF1B3B2B),
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = maghribTime12,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Serif,
                        color = if (isDarkTheme) Color.White else Color(0xFF0D281E)
                    )
                    Text(
                        text = maghribAmPm,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        color = if (isDarkTheme) Color(0xFFFDE8A8) else Color(0xFF0D281E),
                        modifier = Modifier.padding(bottom = 1.5.dp)
                    )
                }
            }
        }

        // Full clickable overlay for Iftar Card
        Box(
            modifier = Modifier
                .width(totalW * 0.44f)
                .height(totalH * 0.19f)
                .offset(x = totalW * 0.51f, y = totalH * 0.59f)
                .clip(RoundedCornerShape(16.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(color = Color(0x33D4AF37))
                ) { onOpenDua(1) }
        )

        // 7. BOTTOM ACTION BUTTON: "View Sehri & Iftar Duas →"
        Box(
            modifier = Modifier
                .width(totalW * 0.72f)
                .height(totalH * 0.080f)
                .offset(x = totalW * 0.14f, y = totalH * 0.835f)
                .clip(RoundedCornerShape(50))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(color = Color(0xFFD4AF37))
                ) { onOpenDua(if (isFastingHours) 1 else 0) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = strings.viewDuasBtn,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = Color(0xFFFAF7EE),
                letterSpacing = 0.5.sp
            )
        }
    }

    // 8. ROYAL ROSE GULDASTA CROWNING TOP ARCH (Compact Edition matching 99 Names)
    Box(
        modifier = Modifier
            .align(Alignment.TopCenter)
            .offset(y = (-20).dp)
            .fillMaxWidth(0.33f)
            .aspectRatio(1297f / 515f)
    ) {
        Image(
            painter = rememberAssetPainter("media/rose_guldasta_tied_banner.webp"),
            contentDescription = "Ramadan Rose Guldasta",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxSize()
        )

            }
}
}
