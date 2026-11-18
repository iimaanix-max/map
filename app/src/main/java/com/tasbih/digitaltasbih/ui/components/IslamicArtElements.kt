package com.tasbih.digitaltasbih.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

/**
 * Shared Islamic Luxury Art Elements & Custom Shapes
 * Designed to match the exact ultra-premium 3D Islamic luxury aesthetic
 * from the user's reference designs (Images 2 & 3).
 */

// Luxury Gold Palette
val RoyalGoldLight = Color(0xFFFFF2B2)
val RoyalGoldMid = Color(0xFFE5C07B)
val RoyalGoldBase = Color(0xFFD4AF37)
val RoyalGoldDark = Color(0xFFAA7E1E)
val RoyalGoldDeep = Color(0xFF7A5813)

val RoyalEmeraldDark = Color(0xFF032219)
val RoyalEmeraldMid = Color(0xFF063B2C)
val RoyalEmeraldBase = Color(0xFF0A4D3A)
val RoyalEmeraldCardBg = Color(0xFFFAF7F0)

val LuxuryGoldBorderBrush = Brush.linearGradient(
    colors = listOf(
        Color(0xFFFFF4B8),
        Color(0xFFD4AF37),
        Color(0xFFA8791C),
        Color(0xFFE5C07B),
        Color(0xFFFFF4B8)
    )
)

val LuxuryEmeraldBackgroundBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF012318),
        Color(0xFF033F2E),
        Color(0xFF011C13)
    )
)

/**
 * Islamic Ogee / Pointed Arch Shape (Mihrab)
 * Rounded bottom corners, vertical sides, curving smoothly into a pointed Moorish peak at the top.
 */
class IslamicMihrabShape(
    private val archHeightRatio: Float = 0.38f,
    private val cornerRadius: Dp = 8.dp
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val w = size.width
        val h = size.height
        val archH = h * archHeightRatio
        val r = with(density) { cornerRadius.toPx() }
        val path = Path().apply {
            moveTo(0f, h - r)
            quadraticTo(0f, h, r, h)
            lineTo(w - r, h)
            quadraticTo(w, h, w, h - r)
            lineTo(w, archH)
            // Right curve to pointed arch peak
            cubicTo(
                w, archH * 0.45f,
                w * 0.65f, 0f,
                w * 0.5f, 0f
            )
            // Left curve from pointed arch peak
            cubicTo(
                w * 0.35f, 0f,
                0f, archH * 0.45f,
                0f, archH
            )
            close()
        }
        return Outline.Generic(path)
    }
}

/**
 * Scalloped / Stepped Arched Plaque Shape (Image 2 Right Countdown Badge)
 * Has decorative stepped/scalloped corner cutouts at the top with an arched peak.
 */
class ScallopedPlaqueShape(
    private val cornerStep: Dp = 10.dp
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val w = size.width
        val h = size.height
        val step = with(density) { cornerStep.toPx() }
        val r = step * 0.8f

        val path = Path().apply {
            moveTo(0f, h - r)
            quadraticTo(0f, h, r, h)
            lineTo(w - r, h)
            quadraticTo(w, h, w, h - r)
            // Right side up to stepped corner
            lineTo(w, step * 1.5f)
            // Inward step right
            lineTo(w - step, step * 1.5f)
            lineTo(w - step, step)
            // Curve to top arched center
            quadraticTo(w * 0.5f, 0f, step, step)
            lineTo(step, step * 1.5f)
            lineTo(0f, step * 1.5f)
            close()
        }
        return Outline.Generic(path)
    }
}

/**
 * High-detail Golden Mosque Silhouette Canvas Emblem
 * Features 3 domes, central crescent, flanking minarets with balconies & entrance arch.
 */
@Composable
fun GoldenMosqueEmblem(
    modifier: Modifier = Modifier.size(28.dp),
    tint: Color = RoyalGoldBase
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val baseH = h * 0.28f
        val baseY = h * 0.72f

        // 1. Base building
        drawRect(
            color = tint,
            topLeft = Offset(w * 0.18f, baseY),
            size = Size(w * 0.64f, baseH)
        )

        // 2. Central Arch Entrance cutout (dark)
        drawArc(
            color = Color(0xFF04241A),
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = true,
            topLeft = Offset(w * 0.42f, h * 0.76f),
            size = Size(w * 0.16f, h * 0.24f)
        )

        // 3. Central Grand Dome (Pointed onion arch)
        val domePath = Path().apply {
            val dLeft = w * 0.32f
            val dRight = w * 0.68f
            val dBase = baseY
            val dTop = h * 0.22f

            moveTo(dLeft, dBase)
            cubicTo(
                dLeft - w * 0.04f, dBase - h * 0.22f,
                w * 0.42f, dTop,
                w * 0.5f, dTop
            )
            cubicTo(
                w * 0.58f, dTop,
                dRight + w * 0.04f, dBase - h * 0.22f,
                dRight, dBase
            )
            close()
        }
        drawPath(domePath, color = tint)

        // 4. Central Finial & Crescent
        drawLine(
            color = tint,
            start = Offset(w * 0.5f, h * 0.22f),
            end = Offset(w * 0.5f, h * 0.09f),
            strokeWidth = 1.4f
        )
        drawArc(
            color = tint,
            startAngle = 120f,
            sweepAngle = 260f,
            useCenter = false,
            topLeft = Offset(w * 0.45f, h * 0.04f),
            size = Size(w * 0.1f, h * 0.1f),
            style = Stroke(width = 1.2f)
        )

        // 5. Left Minaret
        val minW = w * 0.075f
        drawRect(
            color = tint,
            topLeft = Offset(w * 0.08f, h * 0.28f),
            size = Size(minW, h * 0.72f)
        )
        // Left Balcony
        drawRect(
            color = tint,
            topLeft = Offset(w * 0.06f, h * 0.42f),
            size = Size(minW + w * 0.04f, h * 0.04f)
        )
        // Left Minaret Dome
        val leftDome = Path().apply {
            moveTo(w * 0.08f, h * 0.28f)
            lineTo(w * 0.1175f, h * 0.16f)
            lineTo(w * 0.155f, h * 0.28f)
            close()
        }
        drawPath(leftDome, color = tint)

        // 6. Right Minaret
        drawRect(
            color = tint,
            topLeft = Offset(w * 0.845f, h * 0.28f),
            size = Size(minW, h * 0.72f)
        )
        // Right Balcony
        drawRect(
            color = tint,
            topLeft = Offset(w * 0.825f, h * 0.42f),
            size = Size(minW + w * 0.04f, h * 0.04f)
        )
        // Right Minaret Dome
        val rightDome = Path().apply {
            moveTo(w * 0.845f, h * 0.28f)
            lineTo(w * 0.8825f, h * 0.16f)
            lineTo(w * 0.92f, h * 0.28f)
            close()
        }
        drawPath(rightDome, color = tint)
    }
}

/**
 * Luxury Gold Vector Calendar Icon (Replacing the cheap OS phone emoji)
 * Exactly matches the gold spiral-bound Islamic calendar in Image 2.
 */
@Composable
fun GoldenCalendarVector(
    modifier: Modifier = Modifier.size(16.dp),
    tint: Color = RoyalGoldLight
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Outer calendar box with rounded corners
        val r = 2.dp.toPx()
        val boxTop = h * 0.2f
        val boxH = h * 0.8f

        // Body border
        drawRoundRect(
            color = tint,
            topLeft = Offset(0f, boxTop),
            size = Size(w, boxH),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(r, r),
            style = Stroke(width = 1.3.dp.toPx())
        )

        // Top Header Bar
        drawRoundRect(
            color = tint,
            topLeft = Offset(0f, boxTop),
            size = Size(w, h * 0.26f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(r, r)
        )

        // 2 Spiral Ring Binders
        val ring1X = w * 0.28f
        val ring2X = w * 0.72f
        drawRoundRect(
            color = tint,
            topLeft = Offset(ring1X - 1.2.dp.toPx(), 0f),
            size = Size(2.4.dp.toPx(), h * 0.32f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(1.dp.toPx(), 1.dp.toPx())
        )
        drawRoundRect(
            color = tint,
            topLeft = Offset(ring2X - 1.2.dp.toPx(), 0f),
            size = Size(2.4.dp.toPx(), h * 0.32f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(1.dp.toPx(), 1.dp.toPx())
        )

        // 4 Calendar Date Grid Dots
        val dotR = 1.1.dp.toPx()
        val y1 = h * 0.60f
        val y2 = h * 0.80f
        drawCircle(color = tint, radius = dotR, center = Offset(w * 0.30f, y1))
        drawCircle(color = tint, radius = dotR, center = Offset(w * 0.50f, y1))
        drawCircle(color = tint, radius = dotR, center = Offset(w * 0.70f, y1))
        drawCircle(color = tint, radius = dotR, center = Offset(w * 0.40f, y2))
        drawCircle(color = tint, radius = dotR, center = Offset(w * 0.60f, y2))
    }
}

/**
 * Sleek Emerald Vector Location Pin (Replacing the phone 📍 emoji)
 */
@Composable
fun EmeraldLocationPin(
    modifier: Modifier = Modifier.size(13.dp),
    pinColor: Color = Color(0xFF0F7655)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val pinPath = Path().apply {
            moveTo(w * 0.5f, h)
            cubicTo(
                w * 0.15f, h * 0.62f,
                0f, h * 0.45f,
                0f, w * 0.5f
            )
            arcTo(
                rect = Rect(0f, 0f, w, w),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 180f,
                forceMoveTo = false
            )
            cubicTo(
                w, h * 0.45f,
                w * 0.85f, h * 0.62f,
                w * 0.5f, h
            )
            close()
        }
        drawPath(pinPath, color = pinColor)
        // Center white circle
        drawCircle(
            color = Color.White,
            radius = w * 0.20f,
            center = Offset(w * 0.5f, w * 0.5f)
        )
    }
}

/**
 * Hanging Golden Fanoos Lantern (Used in header backgrounds of Image 2 & 3)
 */
fun DrawScope.drawHangingFanoosLantern(
    anchorX: Float,
    chainLength: Float = 28f,
    lanternSize: Float = 34f
) {
    // 1. Chain from top edge
    drawLine(
        color = RoyalGoldBase,
        start = Offset(anchorX, 0f),
        end = Offset(anchorX, chainLength),
        strokeWidth = 1.4f
    )

    val topY = chainLength

    // 2. Radial warm glow bloom
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xEEFFE082),
                Color(0x88FFA000),
                Color(0x22FF6F00),
                Color.Transparent
            ),
            center = Offset(anchorX, topY + lanternSize * 0.55f),
            radius = lanternSize * 1.6f
        )
    )

    // 3. Ornate Cap with Crescent
    val capPath = Path().apply {
        moveTo(anchorX, topY)
        lineTo(anchorX + lanternSize * 0.28f, topY + lanternSize * 0.22f)
        lineTo(anchorX - lanternSize * 0.28f, topY + lanternSize * 0.22f)
        close()
    }
    drawPath(capPath, color = RoyalGoldLight)

    // 4. Glass body (hexagonal faceted lantern)
    val bodyTop = topY + lanternSize * 0.22f
    val bodyH = lanternSize * 0.55f
    val bodyPath = Path().apply {
        moveTo(anchorX - lanternSize * 0.28f, bodyTop)
        lineTo(anchorX + lanternSize * 0.28f, bodyTop)
        lineTo(anchorX + lanternSize * 0.20f, bodyTop + bodyH)
        lineTo(anchorX - lanternSize * 0.20f, bodyTop + bodyH)
        close()
    }
    // Glowing candle inside glass
    drawPath(
        bodyPath,
        brush = Brush.verticalGradient(
            listOf(Color(0xFFFFF9C4), Color(0xFFFFD54F), Color(0xFFFFB300))
        )
    )
    // Facet lines
    drawLine(
        color = RoyalGoldDark,
        start = Offset(anchorX, bodyTop),
        end = Offset(anchorX, bodyTop + bodyH),
        strokeWidth = 1f
    )
    drawPath(bodyPath, color = RoyalGoldBase, style = Stroke(width = 1.2f))

    // 5. Bottom base & dangling finial
    val baseY = bodyTop + bodyH
    val basePath = Path().apply {
        moveTo(anchorX - lanternSize * 0.22f, baseY)
        lineTo(anchorX + lanternSize * 0.22f, baseY)
        lineTo(anchorX, baseY + lanternSize * 0.20f)
        close()
    }
    drawPath(basePath, color = RoyalGoldBase)
    drawCircle(
        color = RoyalGoldLight,
        radius = 2f,
        center = Offset(anchorX, baseY + lanternSize * 0.24f)
    )
}

/**
 * 3D Embossed Ornate Islamic Corner Arabesque (Used in all 4 corners of cards)
 */
fun DrawScope.drawIslamicCornerArabesque(
    isTop: Boolean,
    isLeft: Boolean,
    sizePx: Float = 54f
) {
    val startX = if (isLeft) 0f else size.width
    val startY = if (isTop) 0f else size.height
    val dirX = if (isLeft) 1f else -1f
    val dirY = if (isTop) 1f else -1f

    val goldBrush = Brush.linearGradient(
        colors = listOf(RoyalGoldLight, RoyalGoldBase, RoyalGoldDark),
        start = Offset(startX, startY),
        end = Offset(startX + dirX * sizePx, startY + dirY * sizePx)
    )

    // Outer scalloped flourish curve
    val flourishPath = Path().apply {
        moveTo(startX, startY + dirY * sizePx)
        cubicTo(
            startX + dirX * sizePx * 0.25f, startY + dirY * sizePx * 0.75f,
            startX + dirX * sizePx * 0.65f, startY + dirY * sizePx * 0.65f,
            startX + dirX * sizePx * 0.85f, startY + dirY * sizePx * 0.35f
        )
        cubicTo(
            startX + dirX * sizePx * 0.95f, startY + dirY * sizePx * 0.15f,
            startX + dirX * sizePx * 0.90f, startY,
            startX + dirX * sizePx, startY
        )
    }
    drawPath(
        flourishPath,
        brush = goldBrush,
        style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
    )

    // Inner delicate gold accent loop
    val innerPath = Path().apply {
        moveTo(startX + dirX * 6.dp.toPx(), startY + dirY * (sizePx * 0.6f))
        quadraticTo(
            startX + dirX * (sizePx * 0.45f), startY + dirY * (sizePx * 0.45f),
            startX + dirX * (sizePx * 0.6f), startY + dirY * 6.dp.toPx()
        )
    }
    drawPath(
        innerPath,
        color = RoyalGoldLight,
        style = Stroke(width = 1.dp.toPx(), cap = StrokeCap.Round)
    )
}

/**
 * Background Mosque Skyline Silhouette (Domes, Minarets & Stars in header)
 */
fun DrawScope.drawMosqueSkylineWatermark(
    headerHeight: Float = 110.dp.toPx()
) {
    val skylineColor = Color(0x3510B981)
    val starColor = Color(0x66E5C07B)

    val w = size.width
    val baseY = headerHeight * 0.85f

    // Minaret 1
    val minW = 7.dp.toPx()
    val min1X = w * 0.54f
    drawRect(
        color = skylineColor,
        topLeft = Offset(min1X, headerHeight * 0.12f),
        size = Size(minW, baseY - headerHeight * 0.12f)
    )
    // Minaret 1 top spire
    val m1Path = Path().apply {
        moveTo(min1X, headerHeight * 0.12f)
        lineTo(min1X + minW * 0.5f, headerHeight * 0.04f)
        lineTo(min1X + minW, headerHeight * 0.12f)
        close()
    }
    drawPath(m1Path, color = skylineColor)

    // Dome 1 (Small dome)
    val d1W = 38.dp.toPx()
    val d1H = 32.dp.toPx()
    drawArc(
        color = skylineColor,
        startAngle = 180f,
        sweepAngle = 180f,
        useCenter = true,
        topLeft = Offset(w * 0.58f, baseY - d1H),
        size = Size(d1W, d1H * 2)
    )

    // Dome 2 (Large central dome)
    val d2W = 56.dp.toPx()
    val d2H = 46.dp.toPx()
    drawArc(
        color = skylineColor,
        startAngle = 180f,
        sweepAngle = 180f,
        useCenter = true,
        topLeft = Offset(w * 0.68f, baseY - d2H),
        size = Size(d2W, d2H * 2)
    )

    // Minaret 2
    val min2X = w * 0.84f
    drawRect(
        color = skylineColor,
        topLeft = Offset(min2X, headerHeight * 0.08f),
        size = Size(minW, baseY - headerHeight * 0.08f)
    )
    val m2Path = Path().apply {
        moveTo(min2X, headerHeight * 0.08f)
        lineTo(min2X + minW * 0.5f, 0f)
        lineTo(min2X + minW, headerHeight * 0.08f)
        close()
    }
    drawPath(m2Path, color = skylineColor)

    // Twinkling stars in sky
    drawCircle(starColor, 2.dp.toPx(), Offset(w * 0.50f, headerHeight * 0.22f))
    drawCircle(starColor, 2.5.dp.toPx(), Offset(w * 0.65f, headerHeight * 0.10f))
    drawCircle(starColor, 1.8.dp.toPx(), Offset(w * 0.78f, headerHeight * 0.18f))
    drawCircle(starColor, 2.2.dp.toPx(), Offset(w * 0.90f, headerHeight * 0.28f))
}

/**
 * 8-Point Islamic Star Rosette (Vector)
 */
@Composable
fun EightPointIslamicStar(
    modifier: Modifier = Modifier.size(16.dp),
    color: Color = RoyalGoldBase
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f
        val r = w / 2f

        // Draw two overlapping rotated squares for 8-point star
        val path = Path().apply {
            for (i in 0 until 16) {
                val angle = i * Math.PI / 8.0 - Math.PI / 2.0
                val radius = if (i % 2 == 0) r else r * 0.54f
                val x = cx + (radius * cos(angle)).toFloat()
                val y = cy + (radius * sin(angle)).toFloat()
                if (i == 0) moveTo(x, y) else lineTo(x, y)
            }
            close()
        }
        drawPath(path, color = color)
        // Center jewel dot
        drawCircle(color = RoyalGoldLight, radius = r * 0.22f, center = Offset(cx, cy))
    }
}

/**
 * Pure Gold Outline Mosque Line-Art Emblem (Exact Image 2)
 * Clean, modern, luxury outline: Central pointed dome with crescent, arched doorway, and 2 minarets.
 */
@Composable
fun MosqueLineArtEmblem(
    modifier: Modifier = Modifier.size(24.dp),
    tint: Color = Color(0xFFDFC68D)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val stroke = 1.35.dp.toPx()

        // 1. Floor line
        val baseY = h * 0.88f
        drawLine(
            color = tint,
            start = Offset(w * 0.10f, baseY),
            end = Offset(w * 0.90f, baseY),
            strokeWidth = stroke
        )

        // 2. Central Arch Doorway (gold outline)
        val doorW = w * 0.22f
        val doorH = h * 0.32f
        val doorPath = Path().apply {
            moveTo(w * 0.5f - doorW * 0.5f, baseY)
            lineTo(w * 0.5f - doorW * 0.5f, baseY - doorH * 0.6f)
            cubicTo(
                w * 0.5f - doorW * 0.5f, baseY - doorH,
                w * 0.5f + doorW * 0.5f, baseY - doorH,
                w * 0.5f + doorW * 0.5f, baseY - doorH * 0.6f
            )
            lineTo(w * 0.5f + doorW * 0.5f, baseY)
        }
        drawPath(doorPath, color = tint, style = Stroke(width = stroke))

        // 3. Central Main Dome (Islamic pointed arch dome)
        val domePath = Path().apply {
            val dL = w * 0.26f
            val dR = w * 0.74f
            val dBase = baseY - doorH * 0.52f
            val dPeakY = h * 0.22f
            moveTo(dL, dBase)
            cubicTo(
                dL - w * 0.04f, dBase - h * 0.26f,
                w * 0.42f, dPeakY,
                w * 0.5f, dPeakY
            )
            cubicTo(
                w * 0.58f, dPeakY,
                dR + w * 0.04f, dBase - h * 0.26f,
                dR, dBase
            )
        }
        drawPath(domePath, color = tint, style = Stroke(width = stroke))

        // Central Finial & Crescent
        drawLine(
            color = tint,
            start = Offset(w * 0.5f, h * 0.22f),
            end = Offset(w * 0.5f, h * 0.12f),
            strokeWidth = stroke
        )
        drawArc(
            color = tint,
            startAngle = 100f,
            sweepAngle = 260f,
            useCenter = false,
            topLeft = Offset(w * 0.44f, h * 0.04f),
            size = Size(w * 0.12f, h * 0.12f),
            style = Stroke(width = stroke)
        )

        // 4. Left Minaret
        val minLeftX = w * 0.16f
        val minRightX = w * 0.84f
        val minTopY = h * 0.26f
        drawLine(
            color = tint,
            start = Offset(minLeftX, baseY),
            end = Offset(minLeftX, minTopY),
            strokeWidth = stroke
        )
        // Left minaret top / dome
        val lDome = Path().apply {
            moveTo(minLeftX - w * 0.04f, minTopY)
            lineTo(minLeftX, minTopY - h * 0.12f)
            lineTo(minLeftX + w * 0.04f, minTopY)
        }
        drawPath(lDome, color = tint, style = Stroke(width = stroke))

        // 5. Right Minaret
        drawLine(
            color = tint,
            start = Offset(minRightX, baseY),
            end = Offset(minRightX, minTopY),
            strokeWidth = stroke
        )
        // Right minaret top / dome
        val rDome = Path().apply {
            moveTo(minRightX - w * 0.04f, minTopY)
            lineTo(minRightX, minTopY - h * 0.12f)
            lineTo(minRightX + w * 0.04f, minTopY)
        }
        drawPath(rDome, color = tint, style = Stroke(width = stroke))
    }
}

/**
 * Mini Red-Header Vector Calendar Icon with "17" (Exact Image 2)
 */
@Composable
fun MiniRedCalendarIcon(
    modifier: Modifier = Modifier.size(16.dp)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val r = 2.dp.toPx()

        // 1. Calendar Body (White with rounded corners)
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(0f, h * 0.12f),
            size = Size(w, h * 0.88f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(r, r)
        )

        // 2. Red Top Header Bar
        val headerH = h * 0.32f
        val redBarPath = Path().apply {
            moveTo(0f, h * 0.12f + r)
            quadraticTo(0f, h * 0.12f, r, h * 0.12f)
            lineTo(w - r, h * 0.12f)
            quadraticTo(w, h * 0.12f, w, h * 0.12f + r)
            lineTo(w, h * 0.12f + headerH)
            lineTo(0f, h * 0.12f + headerH)
            close()
        }
        drawPath(redBarPath, color = Color(0xFFE53935))

        // 3. Two spiral binder rings
        val ringW = 1.6.dp.toPx()
        val ringH = 3.2.dp.toPx()
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(w * 0.28f - ringW / 2, 0f),
            size = Size(ringW, ringH),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(0.8.dp.toPx(), 0.8.dp.toPx())
        )
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(w * 0.72f - ringW / 2, 0f),
            size = Size(ringW, ringH),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(0.8.dp.toPx(), 0.8.dp.toPx())
        )

        // 4. "17" date lines inside white body
        val linePaintColor = Color(0xFF424242)
        val stroke = 1.1.dp.toPx()
        // Digit 1
        drawLine(
            color = linePaintColor,
            start = Offset(w * 0.38f, h * 0.54f),
            end = Offset(w * 0.38f, h * 0.86f),
            strokeWidth = stroke
        )
        // Digit 7
        drawLine(
            color = linePaintColor,
            start = Offset(w * 0.52f, h * 0.54f),
            end = Offset(w * 0.70f, h * 0.54f),
            strokeWidth = stroke
        )
        drawLine(
            color = linePaintColor,
            start = Offset(w * 0.70f, h * 0.54f),
            end = Offset(w * 0.56f, h * 0.86f),
            strokeWidth = stroke
        )
    }
}

/**
 * Sleek Red Vector Location Pin (Exact Image 2)
 */
@Composable
fun RedLocationPin(
    modifier: Modifier = Modifier.size(13.dp)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val pinColor = Color(0xFFE53935)
        val pinPath = Path().apply {
            moveTo(w * 0.5f, h)
            cubicTo(
                w * 0.15f, h * 0.65f,
                0f, h * 0.42f,
                0f, h * 0.32f
            )
            cubicTo(
                0f, h * 0.12f,
                w * 0.22f, 0f,
                w * 0.5f, 0f
            )
            cubicTo(
                w * 0.78f, 0f,
                w, h * 0.12f,
                w, h * 0.32f
            )
            cubicTo(
                w, h * 0.42f,
                w * 0.85f, h * 0.65f,
                w * 0.5f, h
            )
            close()
        }
        drawPath(pinPath, color = pinColor)

        // Center white circle
        drawCircle(
            color = Color.White,
            radius = w * 0.18f,
            center = Offset(w * 0.5f, h * 0.32f)
        )
    }
}

/**
 * Event Card Center Divider: Hairline with Gold Diamond (Exact Image 2)
 */
@Composable
fun EventCardDivider(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(0.8.dp)
                .background(Color(0xFFE0DACB))
        )
        Spacer(modifier = Modifier.width(6.dp))
        // Small gold diamond
        Canvas(modifier = Modifier.size(6.dp)) {
            val s = size.width
            val path = Path().apply {
                moveTo(s * 0.5f, 0f)
                lineTo(s, s * 0.5f)
                lineTo(s * 0.5f, s)
                lineTo(0f, s * 0.5f)
                close()
            }
            drawPath(path, color = Color(0xFFC5A059), style = Stroke(width = 1.dp.toPx()))
        }
        Spacer(modifier = Modifier.width(6.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(0.8.dp)
                .background(Color(0xFFE0DACB))
        )
    }
}

/**
 * Subtle Islamic Arch Watermark for outer dark emerald container background
 */
fun DrawScope.drawSubtleIslamicArchWatermark() {
    val archColor = Color(0x15C5A059) // Soft faint gold
    val w = size.width
    val h = size.height
    val strokeWidth = 1.2.dp.toPx()

    // Soft arch curve 1 across header
    val path1 = Path().apply {
        moveTo(w * 0.10f, h * 0.18f)
        cubicTo(
            w * 0.30f, 0f,
            w * 0.70f, 0f,
            w * 0.90f, h * 0.18f
        )
    }
    drawPath(path1, color = archColor, style = Stroke(width = strokeWidth))

    // Soft arch curve 2 lower
    val path2 = Path().apply {
        moveTo(w * 0.05f, h * 0.35f)
        cubicTo(
            w * 0.30f, h * 0.10f,
            w * 0.70f, h * 0.10f,
            w * 0.95f, h * 0.35f
        )
    }
    drawPath(path2, color = archColor, style = Stroke(width = strokeWidth))
}

/**
 * Full Luxury Container Atmosphere:
 * - Mosque skyline watermark across header
 * - Hanging golden Fanoos lantern with warm amber glowing bloom from top right
 * - Four 3D embossed gold corner arabesques
 */
fun DrawScope.drawLuxuryContainerAtmosphere(
    headerHeight: Float = 110.dp.toPx()
) {
    // 1. Mosque skyline watermark across top
    drawMosqueSkylineWatermark(headerHeight = headerHeight)

    // 2. Hanging Golden Fanoos lantern at top right with warm glow bloom
    drawHangingFanoosLantern(
        anchorX = size.width - 34.dp.toPx(),
        chainLength = 12.dp.toPx(),
        lanternSize = 34.dp.toPx()
    )

    // 3. Four corner embossed gold arabesques
    val arabesqueSize = 36.dp.toPx()
    drawIslamicCornerArabesque(isTop = true, isLeft = true, sizePx = arabesqueSize)
    drawIslamicCornerArabesque(isTop = true, isLeft = false, sizePx = arabesqueSize)
    drawIslamicCornerArabesque(isTop = false, isLeft = true, sizePx = arabesqueSize)
    drawIslamicCornerArabesque(isTop = false, isLeft = false, sizePx = arabesqueSize)
}

/**
 * Faint Golden Mandala Floral Watermark in card corners
 */
fun DrawScope.drawMandalaWatermark(
    centerX: Float,
    centerY: Float,
    radius: Float = 48.dp.toPx(),
    color: Color = Color(0x18C5A059)
) {
    val rings = 3
    for (r in 1..rings) {
        drawCircle(
            color = color,
            radius = radius * (r / rings.toFloat()),
            center = Offset(centerX, centerY),
            style = Stroke(width = 1f)
        )
    }
    val petals = 8
    for (i in 0 until petals) {
        val angle = i * (2.0 * Math.PI / petals)
        val px = centerX + (radius * 0.45f * cos(angle)).toFloat()
        val py = centerY + (radius * 0.45f * sin(angle)).toFloat()
        drawCircle(
            color = color,
            radius = radius * 0.3f,
            center = Offset(px, py),
            style = Stroke(width = 0.9f)
        )
    }
}

/**
 * Manuscript Corner Tab (Emerald & Gold corner accents for Islamic Quranic bookbinding look)
 */
fun DrawScope.drawManuscriptCornerTab(
    isTop: Boolean,
    isLeft: Boolean,
    sizePx: Float = 28.dp.toPx()
) {
    val startX = if (isLeft) 0f else size.width
    val startY = if (isTop) 0f else size.height
    val dirX = if (isLeft) 1f else -1f
    val dirY = if (isTop) 1f else -1f

    val tabPath = Path().apply {
        moveTo(startX, startY)
        lineTo(startX + dirX * sizePx, startY)
        lineTo(startX + dirX * sizePx * 0.65f, startY + dirY * sizePx * 0.65f)
        lineTo(startX, startY + dirY * sizePx)
        close()
    }
    drawPath(tabPath, color = Color(0xFF033827))

    // Gold trim line
    drawLine(
        brush = LuxuryGoldBorderBrush,
        start = Offset(startX + dirX * sizePx, startY),
        end = Offset(startX, startY + dirY * sizePx),
        strokeWidth = 1.3f
    )
}

/**
 * Luxury Golden Praying Hands Line-Art Vector (Image 2 Header Badge)
 * Two hands raised in supplication (Dua) with delicate gold line work.
 */
@Composable
fun GoldenPrayingHandsIcon(
    modifier: Modifier = Modifier.size(26.dp),
    tint: Color = Color(0xFFC5A059)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val stroke = 1.35.dp.toPx()
        val thinStroke = 1.0.dp.toPx()

        // ── LEFT HAND (Cupped in prayer) ──
        // Outer palm & pinky finger
        val leftOuter = Path().apply {
            moveTo(w * 0.40f, h * 0.90f) // wrist center
            lineTo(w * 0.22f, h * 0.88f) // wrist left
            cubicTo(
                w * 0.14f, h * 0.68f,
                w * 0.14f, h * 0.45f,
                w * 0.22f, h * 0.28f  // pinky tip
            )
            // Finger curve at tip
            arcTo(
                rect = Rect(w * 0.20f, h * 0.22f, w * 0.28f, h * 0.32f),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 180f,
                forceMoveTo = false
            )
            // Line down between pinky and ring
            lineTo(w * 0.28f, h * 0.48f)
        }
        drawPath(leftOuter, color = tint, style = Stroke(width = stroke, cap = StrokeCap.Round, join = StrokeJoin.Round))

        // Left Ring Finger
        val leftRing = Path().apply {
            moveTo(w * 0.28f, h * 0.46f)
            lineTo(w * 0.28f, h * 0.24f)
            arcTo(
                rect = Rect(w * 0.26f, h * 0.16f, w * 0.34f, h * 0.26f),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 180f,
                forceMoveTo = false
            )
            lineTo(w * 0.34f, h * 0.50f)
        }
        drawPath(leftRing, color = tint, style = Stroke(width = stroke, cap = StrokeCap.Round))

        // Left Middle Finger (tallest)
        val leftMiddle = Path().apply {
            moveTo(w * 0.34f, h * 0.48f)
            lineTo(w * 0.35f, h * 0.18f)
            arcTo(
                rect = Rect(w * 0.32f, h * 0.10f, w * 0.41f, h * 0.20f),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 180f,
                forceMoveTo = false
            )
            lineTo(w * 0.41f, h * 0.52f)
        }
        drawPath(leftMiddle, color = tint, style = Stroke(width = stroke, cap = StrokeCap.Round))

        // Left Index Finger
        val leftIndex = Path().apply {
            moveTo(w * 0.41f, h * 0.50f)
            lineTo(w * 0.41f, h * 0.22f)
            arcTo(
                rect = Rect(w * 0.39f, h * 0.14f, w * 0.47f, h * 0.24f),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 180f,
                forceMoveTo = false
            )
            lineTo(w * 0.46f, h * 0.58f)
        }
        drawPath(leftIndex, color = tint, style = Stroke(width = stroke, cap = StrokeCap.Round))

        // Left Thumb (curving gracefully outward/inward)
        val leftThumb = Path().apply {
            moveTo(w * 0.46f, h * 0.58f)
            cubicTo(
                w * 0.49f, h * 0.46f,
                w * 0.46f, h * 0.38f,
                w * 0.43f, h * 0.38f
            )
            cubicTo(
                w * 0.40f, h * 0.38f,
                w * 0.41f, h * 0.46f,
                w * 0.40f, h * 0.64f
            )
            lineTo(w * 0.40f, h * 0.90f)
        }
        drawPath(leftThumb, color = tint, style = Stroke(width = stroke, cap = StrokeCap.Round))

        // ── RIGHT HAND (Mirrored cupped in prayer) ──
        // Outer palm & pinky finger
        val rightOuter = Path().apply {
            moveTo(w * 0.60f, h * 0.90f) // wrist center
            lineTo(w * 0.78f, h * 0.88f) // wrist right
            cubicTo(
                w * 0.86f, h * 0.68f,
                w * 0.86f, h * 0.45f,
                w * 0.78f, h * 0.28f  // pinky tip
            )
            arcTo(
                rect = Rect(w * 0.72f, h * 0.22f, w * 0.80f, h * 0.32f),
                startAngleDegrees = 0f,
                sweepAngleDegrees = -180f,
                forceMoveTo = false
            )
            lineTo(w * 0.72f, h * 0.48f)
        }
        drawPath(rightOuter, color = tint, style = Stroke(width = stroke, cap = StrokeCap.Round, join = StrokeJoin.Round))

        // Right Ring Finger
        val rightRing = Path().apply {
            moveTo(w * 0.72f, h * 0.46f)
            lineTo(w * 0.72f, h * 0.24f)
            arcTo(
                rect = Rect(w * 0.66f, h * 0.16f, w * 0.74f, h * 0.26f),
                startAngleDegrees = 0f,
                sweepAngleDegrees = -180f,
                forceMoveTo = false
            )
            lineTo(w * 0.66f, h * 0.50f)
        }
        drawPath(rightRing, color = tint, style = Stroke(width = stroke, cap = StrokeCap.Round))

        // Right Middle Finger (tallest)
        val rightMiddle = Path().apply {
            moveTo(w * 0.66f, h * 0.48f)
            lineTo(w * 0.65f, h * 0.18f)
            arcTo(
                rect = Rect(w * 0.59f, h * 0.10f, w * 0.68f, h * 0.20f),
                startAngleDegrees = 0f,
                sweepAngleDegrees = -180f,
                forceMoveTo = false
            )
            lineTo(w * 0.59f, h * 0.52f)
        }
        drawPath(rightMiddle, color = tint, style = Stroke(width = stroke, cap = StrokeCap.Round))

        // Right Index Finger
        val rightIndex = Path().apply {
            moveTo(w * 0.59f, h * 0.50f)
            lineTo(w * 0.59f, h * 0.22f)
            arcTo(
                rect = Rect(w * 0.53f, h * 0.14f, w * 0.61f, h * 0.24f),
                startAngleDegrees = 0f,
                sweepAngleDegrees = -180f,
                forceMoveTo = false
            )
            lineTo(w * 0.54f, h * 0.58f)
        }
        drawPath(rightIndex, color = tint, style = Stroke(width = stroke, cap = StrokeCap.Round))

        // Right Thumb
        val rightThumb = Path().apply {
            moveTo(w * 0.54f, h * 0.58f)
            cubicTo(
                w * 0.51f, h * 0.46f,
                w * 0.54f, h * 0.38f,
                w * 0.57f, h * 0.38f
            )
            cubicTo(
                w * 0.60f, h * 0.38f,
                w * 0.59f, h * 0.46f,
                w * 0.60f, h * 0.64f
            )
            lineTo(w * 0.60f, h * 0.90f)
        }
        drawPath(rightThumb, color = tint, style = Stroke(width = stroke, cap = StrokeCap.Round))

        // Bottom wrist bar lines
        drawLine(
            color = tint,
            start = Offset(w * 0.22f, h * 0.90f),
            end = Offset(w * 0.40f, h * 0.92f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
        drawLine(
            color = tint,
            start = Offset(w * 0.78f, h * 0.90f),
            end = Offset(w * 0.60f, h * 0.92f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
    }
}

/**
 * Elegant Hairline Divider with 8-Point Islamic Star Rosette in center: ── ۞ ──
 */
@Composable
fun RosetteDivider(
    modifier: Modifier = Modifier,
    starSize: Dp = 10.dp,
    lineColor: Color = Color(0xFFC5A059),
    starColor: Color = Color(0xFFC5A059)
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(0.9.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(Color.Transparent, lineColor.copy(alpha = 0.85f))
                    )
                )
        )
        Spacer(modifier = Modifier.width(6.dp))
        EightPointIslamicStar(modifier = Modifier.size(starSize), color = starColor)
        Spacer(modifier = Modifier.width(6.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(0.9.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(lineColor.copy(alpha = 0.85f), Color.Transparent)
                    )
                )
        )
    }
}

