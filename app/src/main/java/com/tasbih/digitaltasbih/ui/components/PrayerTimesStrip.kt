package com.tasbih.digitaltasbih.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.data.AppStrings
import com.tasbih.digitaltasbih.data.PrayerDayData
import com.tasbih.digitaltasbih.data.PrayerRepository
import java.util.Locale

data class PrayerStripItemData(
    val id: String,
    val name: String,
    val time: String,
    val isPast: Boolean,
    val isActive: Boolean
)

@Composable
fun PrayerTimesStrip(
    strings: AppStrings,
    todayPrayerData: PrayerDayData? = null,
    currentPrayerId: String? = null,
    isDarkTheme: Boolean = false,
    isRamadanActive: Boolean = false,
    onOpenPrayerScreen: () -> Unit
) {
    val timings = todayPrayerData?.timings ?: emptyMap()
    val activeId = (currentPrayerId ?: "dhuhr").lowercase()

    fun parseMins(timeStr: String): Int {
        val clean = timeStr.trim().split(" ")[0]
        val parts = clean.split(":")
        if (parts.size < 2) return 0
        return (parts[0].trim().toIntOrNull() ?: 0) * 60 + (parts[1].trim().toIntOrNull() ?: 0)
    }

    val cal = java.util.Calendar.getInstance()
    val nowMins = cal.get(java.util.Calendar.HOUR_OF_DAY) * 60 + cal.get(java.util.Calendar.MINUTE)
    val fajrMins = timings["Fajr"]?.let { parseMins(it) } ?: 289
    val isAfterMidnightBeforeFajr = nowMins < fajrMins

    fun checkIsPast(id: String, pMins: Int): Boolean {
        if (isAfterMidnightBeforeFajr) return false
        val isAct = id.equals(activeId, ignoreCase = true)
        return nowMins >= pMins && !isAct
    }

    val items = listOf(
        PrayerStripItemData(
            id = "fajr",
            name = strings.fajr,
            time = timings["Fajr"]?.let { PrayerRepository.to12HourFormat(it) } ?: "4:47 am",
            isPast = checkIsPast("fajr", timings["Fajr"]?.let { parseMins(it) } ?: 289),
            isActive = activeId == "fajr"
        ),
        PrayerStripItemData(
            id = "dhuhr",
            name = strings.dhuhr,
            time = timings["Dhuhr"]?.let { PrayerRepository.to12HourFormat(it) } ?: "11:52 am",
            isPast = checkIsPast("dhuhr", timings["Dhuhr"]?.let { parseMins(it) } ?: 712),
            isActive = activeId == "dhuhr"
        ),
        PrayerStripItemData(
            id = "asr",
            name = strings.asr,
            time = timings["Asr"]?.let { PrayerRepository.to12HourFormat(it) } ?: "4:05 pm",
            isPast = checkIsPast("asr", timings["Asr"]?.let { parseMins(it) } ?: 965),
            isActive = activeId == "asr"
        ),
        PrayerStripItemData(
            id = "maghrib",
            name = strings.maghrib,
            time = timings["Maghrib"]?.let { PrayerRepository.to12HourFormat(it) } ?: "5:42 pm",
            isPast = checkIsPast("maghrib", timings["Maghrib"]?.let { parseMins(it) } ?: 1062),
            isActive = activeId == "maghrib"
        ),
        PrayerStripItemData(
            id = "isha",
            name = strings.isha,
            time = timings["Isha"]?.let { PrayerRepository.to12HourFormat(it) } ?: "6:56 pm",
            isPast = checkIsPast("isha", timings["Isha"]?.let { parseMins(it) } ?: 1136),
            isActive = activeId == "isha"
        )
    )

    // Unified Strip Container (Dark mode: deep forest green matching Image 2; Light mode: #FBF8F2)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(if (isDarkTheme) Color(0xFF0E2218) else Color(0xFFFBF8F2))
            .border(1.5.dp, if (isDarkTheme) Color(0xFF1C3A2C) else Color(0xFFECE4D4), RoundedCornerShape(22.dp))
            .padding(horizontal = 4.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { prayer ->
                if (prayer.isActive) {
                    ActivePrayerCapsule(
                        prayer = prayer,
                        isRamadanActive = isRamadanActive,
                        onClick = onOpenPrayerScreen
                    )
                } else {
                    CleanPrayerColumn(
                        prayer = prayer,
                        isDarkTheme = isDarkTheme,
                        onClick = onOpenPrayerScreen
                    )
                }
            }

            // 6th Card: All Prayers (सभी नमाज़ें →)
            AllPrayersCard(
                label = strings.allPrayers,
                onClick = onOpenPrayerScreen
            )
        }
    }
}

/**
 * Clean vertical prayer column (Non-active items):
 * - Dark mode badge: #152A20 with subtle green border #224533 (NO bright white shining!)
 * - Light mode badge: #FFFFFF with warm border #EBDCC5
 * - Prayer name: soft silver-sage #BAC7BE (dark) / #5C5240 (light)
 * - Prayer time: bold white Color.White in uppercase AM/PM (dark) / #1F1A14 (light)
 * - Indicator line: bright glowing emerald #00E676 for passed prayers (Image 2)
 */
@Composable
fun RowScope.CleanPrayerColumn(
    prayer: PrayerStripItemData,
    isDarkTheme: Boolean = false,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .height(94.dp)
            .clickable(onClick = onClick)
            .padding(vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Circular Icon Badge (Matches Image 2)
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(
                    if (isDarkTheme) Color(0xFF152A20) else Color.White
                )
                .border(
                    width = 1.dp,
                    color = if (isDarkTheme) Color(0xFF224533) else Color(0xFFEBDCC5),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            PrayerVectorIcon(
                prayerId = prayer.id,
                isDarkTheme = isDarkTheme,
                size = 20.dp
            )
        }

        // Prayer Name
        Text(
            text = prayer.name,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isDarkTheme) Color(0xFFBAC7BE) else Color(0xFF5C5240),
            textAlign = TextAlign.Center
        )

        // Prayer Time (Uppercase AM/PM, Bold White in Dark Mode, matching Image 2!)
        Text(
            text = prayer.time.uppercase(Locale.US),
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDarkTheme) Color.White else Color(0xFF1F1A14),
            textAlign = TextAlign.Center
        )

        // Bottom Indicator Bar:
        // Passed -> Radiant emerald green #00E676 from Image 2
        // Upcoming -> Subtle dark slot in dark mode / warm tan in light mode
        Box(
            modifier = Modifier
                .width(26.dp)
                .height(2.8.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(
                    if (prayer.isPast) {
                        if (isDarkTheme) Color(0xFF00E676) else Color(0xFF0E5D47)
                    } else {
                        if (isDarkTheme) Color(0xFF1B382A) else Color(0xFFE2D6C0)
                    }
                )
        )
    }
}

/**
 * Active prayer capsule (e.g. Isha, Dhuhr):
 * Deep emerald green rounded capsule (#0E5D47), gold border (#D4AF37),
 * golden vector icon, bold white name and time, gold gradient bar,
 * and bright glowing gold dot at the bottom!
 */
@Composable
fun RowScope.ActivePrayerCapsule(
    prayer: PrayerStripItemData,
    isRamadanActive: Boolean = false,
    onClick: () -> Unit
) {
    val capsuleGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0E5D47),
            Color(0xFF084333)
        )
    )

    val capsuleBorder = if (isRamadanActive) {
        Brush.linearGradient(
            colors = listOf(
                Color(0xFFFFD54F),
                Color(0xFFFFF8E1),
                Color(0xFFD4AF37)
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                Color(0xFFD4AF37),
                Color(0xFFD4AF37)
            )
        )
    }

    Box(
        modifier = Modifier
            .weight(1.15f)
            .height(98.dp)
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(capsuleGradient)
            .border(1.5.dp, capsuleBorder, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 2.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isRamadanActive) {
            Text(
                text = "✦",
                fontSize = 8.sp,
                color = Color(0xFFFFD54F),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-4).dp, y = 2.dp)
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.height(84.dp)
        ) {
            // Elegant Vector Icon for Active Capsule
            PrayerVectorIcon(
                prayerId = prayer.id,
                isDarkTheme = true,
                size = 24.dp
            )

            Text(
                text = prayer.name,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Text(
                text = prayer.time.uppercase(Locale.US),
                fontSize = 10.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            // Bottom Gold Line
            Box(
                modifier = Modifier
                    .width(32.dp)
                    .height(2.5.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFFF6D365))
            )
        }

        // Active Bright Glowing Light Dot (At the very bottom center matching Image 1!)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 1.dp)
                .size(6.dp)
                .shadow(elevation = 8.dp, shape = CircleShape, spotColor = Color(0xFFFFD700))
                .clip(CircleShape)
                .background(Color.White)
                .border(0.8.dp, Color(0xFFF6D365), CircleShape)
        )
    }
}

/**
 * All Prayers Card ("सभी नमाज़ें →"):
 * Compact dark green card with gold border and mosque dome icon
 */
@Composable
fun RowScope.AllPrayersCard(
    label: String,
    onClick: () -> Unit
) {
    val cardGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0E5D47),
            Color(0xFF084333)
        )
    )

    Box(
        modifier = Modifier
            .weight(1.2f)
            .height(94.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(cardGradient)
            .border(1.5.dp, Color(0xFFD4AF37), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Circular badge with mosque dome
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color(0x33000000))
                    .border(1.dp, Color(0x66D4AF37), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                PrayerVectorIcon(
                    prayerId = "all",
                    isDarkTheme = true,
                    size = 20.dp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = label.replace(" →", "").replace(" ←", ""),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 12.sp
            )

            Spacer(modifier = Modifier.height(1.dp))

            Text(
                text = "→",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF6D365)
            )
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  CUSTOM VECTOR PRAYER ICONS (EXACT MATCH FOR IMAGE 2)
// ══════════════════════════════════════════════════════════════

@Composable
fun PrayerVectorIcon(
    prayerId: String,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = true,
    size: Dp = 20.dp
) {
    val mod = modifier.size(size)
    when (prayerId.lowercase(Locale.US).trim()) {
        "fajr" -> FajrSunriseIcon(modifier = mod)
        "dhuhr" -> DhuhrSunIcon(modifier = mod)
        "asr" -> AsrSunCloudIcon(modifier = mod)
        "maghrib" -> MaghribMosqueIcon(modifier = mod)
        "isha" -> IshaMoonIcon(modifier = mod)
        "all", "mosque" -> MosqueDomeIcon(modifier = mod)
        else -> DhuhrSunIcon(modifier = mod)
    }
}

/**
 * 1. Fajr: Stylized golden sunrise over horizon with 5 radiating rays (Image 2)
 */
@Composable
fun FajrSunriseIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFFBBF24)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val strokeW = 1.6.dp.toPx()
        val horizonY = h * 0.65f
        val sunRadius = w * 0.24f

        // Horizon main line
        drawLine(
            color = color,
            start = Offset(w * 0.12f, horizonY),
            end = Offset(w * 0.88f, horizonY),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )

        // Reflection secondary line
        drawLine(
            color = color.copy(alpha = 0.85f),
            start = Offset(w * 0.28f, horizonY + 3.2.dp.toPx()),
            end = Offset(w * 0.72f, horizonY + 3.2.dp.toPx()),
            strokeWidth = strokeW * 0.85f,
            cap = StrokeCap.Round
        )

        // Semicircle sun (rising above horizon)
        val arcRect = Rect(
            left = w / 2f - sunRadius,
            top = horizonY - sunRadius,
            right = w / 2f + sunRadius,
            bottom = horizonY + sunRadius
        )
        drawArc(
            color = color,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = true,
            topLeft = arcRect.topLeft,
            size = arcRect.size
        )

        // 5 sun rays radiating up & out
        val rayInner = sunRadius + 2.dp.toPx()
        val rayOuter = rayInner + 3.4.dp.toPx()
        val rayAngles = floatArrayOf(150f, 120f, 90f, 60f, 30f)
        for (deg in rayAngles) {
            val rad = Math.toRadians(deg.toDouble())
            val x1 = (w / 2f + rayInner * kotlin.math.cos(rad)).toFloat()
            val y1 = (horizonY - rayInner * kotlin.math.sin(rad)).toFloat()
            val x2 = (w / 2f + rayOuter * kotlin.math.cos(rad)).toFloat()
            val y2 = (horizonY - rayOuter * kotlin.math.sin(rad)).toFloat()
            drawLine(
                color = color,
                start = Offset(x1, y1),
                end = Offset(x2, y2),
                strokeWidth = strokeW,
                cap = StrokeCap.Round
            )
        }
    }
}

/**
 * 2. Dhuhr: Midday golden sun with 8 radiating beams (Image 2)
 */
@Composable
fun DhuhrSunIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFFBBF24)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f
        val strokeW = 1.6.dp.toPx()
        val sunRadius = w * 0.22f

        // Center filled circle
        drawCircle(
            color = color,
            radius = sunRadius,
            center = Offset(cx, cy)
        )

        // 8 radiating rays
        val rayInner = sunRadius + 2.dp.toPx()
        val rayOuter = rayInner + 3.2.dp.toPx()
        for (i in 0 until 8) {
            val rad = Math.toRadians(i * 45.0)
            val x1 = (cx + rayInner * kotlin.math.cos(rad)).toFloat()
            val y1 = (cy + rayInner * kotlin.math.sin(rad)).toFloat()
            val x2 = (cx + rayOuter * kotlin.math.cos(rad)).toFloat()
            val y2 = (cy + rayOuter * kotlin.math.sin(rad)).toFloat()
            drawLine(
                color = color,
                start = Offset(x1, y1),
                end = Offset(x2, y2),
                strokeWidth = strokeW,
                cap = StrokeCap.Round
            )
        }
    }
}

/**
 * 3. Asr: Afternoon golden sun with soft white cloud in foreground (Image 2)
 */
@Composable
fun AsrSunCloudIcon(
    modifier: Modifier = Modifier,
    sunColor: Color = Color(0xFFFBBF24),
    cloudColor: Color = Color(0xFFF1F5F9)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val strokeW = 1.5.dp.toPx()

        // Sun shifted top-left
        val sunCx = w * 0.42f
        val sunCy = h * 0.38f
        val sunRadius = w * 0.20f

        // 5 rays on top and left (avoiding the bottom-right where cloud sits)
        val rayInner = sunRadius + 2.dp.toPx()
        val rayOuter = rayInner + 3.dp.toPx()
        val rayAngles = floatArrayOf(80f, 125f, 170f, 215f, 260f)
        for (deg in rayAngles) {
            val rad = Math.toRadians(deg.toDouble())
            val x1 = (sunCx + rayInner * kotlin.math.cos(rad)).toFloat()
            val y1 = (sunCy - rayInner * kotlin.math.sin(rad)).toFloat()
            val x2 = (sunCx + rayOuter * kotlin.math.cos(rad)).toFloat()
            val y2 = (sunCy - rayOuter * kotlin.math.sin(rad)).toFloat()
            drawLine(
                color = sunColor,
                start = Offset(x1, y1),
                end = Offset(x2, y2),
                strokeWidth = strokeW,
                cap = StrokeCap.Round
            )
        }

        // Sun disk
        drawCircle(
            color = sunColor,
            radius = sunRadius,
            center = Offset(sunCx, sunCy)
        )

        // Cloud in bottom right
        drawCircle(
            color = cloudColor,
            radius = w * 0.18f,
            center = Offset(w * 0.62f, h * 0.60f)
        )
        drawCircle(
            color = cloudColor,
            radius = w * 0.13f,
            center = Offset(w * 0.44f, h * 0.68f)
        )
        drawCircle(
            color = cloudColor,
            radius = w * 0.12f,
            center = Offset(w * 0.77f, h * 0.67f)
        )
        val cloudBasePath = Path().apply {
            moveTo(w * 0.38f, h * 0.72f)
            lineTo(w * 0.82f, h * 0.72f)
            lineTo(w * 0.82f, h * 0.64f)
            lineTo(w * 0.38f, h * 0.64f)
            close()
        }
        drawPath(cloudBasePath, cloudColor)
    }
}

/**
 * 4. Maghrib: Sunset circular medallion with mosque minarets & dome silhouette (Image 2)
 */
@Composable
fun MaghribMosqueIcon(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f
        val radius = w * 0.42f

        // Clip to medallion circle
        val circlePath = Path().apply {
            addOval(Rect(cx - radius, cy - radius, cx + radius, cy + radius))
        }

        clipPath(circlePath) {
            // Sunset gradient
            val sunsetBrush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFFED7AA), // warm peach/gold at top
                    Color(0xFFFB923C), // vivid sunset orange
                    Color(0xFFEA580C)  // deep dusk orange at bottom
                ),
                startY = cy - radius,
                endY = cy + radius
            )
            drawCircle(
                brush = sunsetBrush,
                radius = radius,
                center = Offset(cx, cy)
            )

            // Mosque silhouette in deep emerald
            val mosqueColor = Color(0xFF0C2B1D)
            val baseY = cy + radius * 0.75f

            val mosquePath = Path().apply {
                moveTo(cx - radius * 0.9f, baseY)

                // Left minaret
                lineTo(cx - radius * 0.62f, baseY)
                lineTo(cx - radius * 0.62f, cy - radius * 0.30f)
                lineTo(cx - radius * 0.66f, cy - radius * 0.30f)
                lineTo(cx - radius * 0.66f, cy - radius * 0.38f)
                lineTo(cx - radius * 0.58f, cy - radius * 0.38f)
                lineTo(cx - radius * 0.54f, cy - radius * 0.65f)
                lineTo(cx - radius * 0.50f, cy - radius * 0.38f)
                lineTo(cx - radius * 0.44f, cy - radius * 0.38f)
                lineTo(cx - radius * 0.44f, cy - radius * 0.30f)
                lineTo(cx - radius * 0.48f, cy - radius * 0.30f)
                lineTo(cx - radius * 0.48f, cy + radius * 0.05f)

                // Center main dome
                lineTo(cx - radius * 0.32f, cy + radius * 0.05f)
                cubicTo(
                    cx - radius * 0.32f, cy - radius * 0.40f,
                    cx, cy - radius * 0.65f,
                    cx, cy - radius * 0.65f
                )
                // Dome spire
                lineTo(cx, cy - radius * 0.82f)
                lineTo(cx, cy - radius * 0.65f)
                cubicTo(
                    cx, cy - radius * 0.65f,
                    cx + radius * 0.32f, cy - radius * 0.40f,
                    cx + radius * 0.32f, cy + radius * 0.05f
                )

                // Right minaret
                lineTo(cx + radius * 0.48f, cy + radius * 0.05f)
                lineTo(cx + radius * 0.48f, cy - radius * 0.30f)
                lineTo(cx + radius * 0.44f, cy - radius * 0.30f)
                lineTo(cx + radius * 0.44f, cy - radius * 0.38f)
                lineTo(cx + radius * 0.50f, cy - radius * 0.38f)
                lineTo(cx + radius * 0.54f, cy - radius * 0.65f)
                lineTo(cx + radius * 0.58f, cy - radius * 0.38f)
                lineTo(cx + radius * 0.66f, cy - radius * 0.38f)
                lineTo(cx + radius * 0.66f, cy - radius * 0.30f)
                lineTo(cx + radius * 0.62f, cy - radius * 0.30f)
                lineTo(cx + radius * 0.62f, baseY)

                // Close along circle bottom
                lineTo(cx + radius * 0.9f, baseY)
                lineTo(cx + radius * 0.9f, cy + radius)
                lineTo(cx - radius * 0.9f, cy + radius)
                close()
            }
            drawPath(mosquePath, mosqueColor)
        }
    }
}

/**
 * 5. Isha: Golden crescent moon
 */
@Composable
fun IshaMoonIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFFBBF24)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w * 0.48f
        val cy = h * 0.50f
        val r = w * 0.34f

        val outer = Path().apply {
            addOval(Rect(cx - r, cy - r, cx + r, cy + r))
        }
        val inner = Path().apply {
            val cutR = r * 0.92f
            val cutCx = cx + r * 0.42f
            val cutCy = cy - r * 0.26f
            addOval(Rect(cutCx - cutR, cutCy - cutR, cutCx + cutR, cutCy + cutR))
        }
        val crescent = Path().apply {
            op(outer, inner, PathOperation.Difference)
        }
        drawPath(crescent, color)
    }
}

/**
 * 6. All Prayers: Golden Mosque Dome & Minarets
 */
@Composable
fun MosqueDomeIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFF6D365)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h * 0.52f
        val radius = w * 0.42f

        val mosquePath = Path().apply {
            val baseY = cy + radius * 0.70f
            moveTo(cx - radius * 0.85f, baseY)
            // Left minaret
            lineTo(cx - radius * 0.58f, baseY)
            lineTo(cx - radius * 0.58f, cy - radius * 0.30f)
            lineTo(cx - radius * 0.50f, cy - radius * 0.65f)
            lineTo(cx - radius * 0.42f, cy - radius * 0.30f)
            lineTo(cx - radius * 0.42f, cy)
            // Center dome
            lineTo(cx - radius * 0.28f, cy)
            cubicTo(
                cx - radius * 0.28f, cy - radius * 0.40f,
                cx, cy - radius * 0.65f,
                cx, cy - radius * 0.65f
            )
            // Spire
            lineTo(cx, cy - radius * 0.82f)
            lineTo(cx, cy - radius * 0.65f)
            cubicTo(
                cx, cy - radius * 0.65f,
                cx + radius * 0.28f, cy - radius * 0.40f,
                cx + radius * 0.28f, cy
            )
            // Right minaret
            lineTo(cx + radius * 0.42f, cy)
            lineTo(cx + radius * 0.42f, cy - radius * 0.30f)
            lineTo(cx + radius * 0.50f, cy - radius * 0.65f)
            lineTo(cx + radius * 0.58f, cy - radius * 0.30f)
            lineTo(cx + radius * 0.58f, baseY)
            lineTo(cx + radius * 0.85f, baseY)
            lineTo(cx + radius * 0.85f, baseY + 2.dp.toPx())
            lineTo(cx - radius * 0.85f, baseY + 2.dp.toPx())
            close()
        }
        drawPath(mosquePath, color)
    }
}
