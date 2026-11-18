package com.tasbih.digitaltasbih.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin

/**
 * Festive Floral & Fairy Lights Overlay for Featured 99 Names of Allah Card.
 * Exact match to user reference image:
 * 1. Royal Rose Bouquet / Garland crowning ABOVE "99 Names of Allah" without obscuring text.
 * 2. Twin Ornate Hanging Golden Lanterns flanking the Arabic calligraphy banner with warm glow.
 * 3. Animated Golden Fairy Lights Garland tracing the perimeter with soft twinkling bulbs and stars.
 * 4. Illuminated fairy lights woven into the rose bouquet garland.
 */
@Composable
fun AllahNameFestiveDecorations(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = false
) {
    // Smooth infinite transition for twinkling fairy lights
    val infiniteTransition = rememberInfiniteTransition(label = "allahNameTwinkle")
    val twinklePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "twinklePhase"
    )

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        // -------------------------------------------------------------
        // 1. ANIMATED FAIRY LIGHTS PERIMETER & GARLAND (CANVAS)
        // -------------------------------------------------------------
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            if (w <= 0f || h <= 0f) return@Canvas

            val cornerR = 20.dp.toPx()
            val wireColor = Color(0xFFD4AF37)
            val wireStroke = Stroke(width = 1.3.dp.toPx(), cap = StrokeCap.Round)

            // --- Top Perimeter Garland Wire with Gentle Swags ---
            val topSwags = 4
            val topSwagWidth = (w - 2f * cornerR) / topSwags
            val topWirePath = Path().apply {
                moveTo(cornerR, 2.dp.toPx())
                for (i in 0 until topSwags) {
                    val startX = cornerR + i * topSwagWidth
                    val endX = startX + topSwagWidth
                    val midX = (startX + endX) / 2f
                    quadraticTo(midX, 8.dp.toPx(), endX, 2.dp.toPx())
                }
            }
            drawPath(topWirePath, color = wireColor, style = wireStroke)

            // --- Left Edge Vertical Wire ---
            val leftWirePath = Path().apply {
                moveTo(2.dp.toPx(), cornerR)
                val leftSwags = 5
                val leftSwagH = (h - 2f * cornerR) / leftSwags
                for (i in 0 until leftSwags) {
                    val startY = cornerR + i * leftSwagH
                    val endY = startY + leftSwagH
                    val midY = (startY + endY) / 2f
                    quadraticTo(7.dp.toPx(), midY, 2.dp.toPx(), endY)
                }
            }
            drawPath(leftWirePath, color = wireColor, style = wireStroke)

            // --- Right Edge Vertical Wire ---
            val rightWirePath = Path().apply {
                moveTo(w - 2.dp.toPx(), cornerR)
                val rightSwags = 5
                val rightSwagH = (h - 2f * cornerR) / rightSwags
                for (i in 0 until rightSwags) {
                    val startY = cornerR + i * rightSwagH
                    val endY = startY + rightSwagH
                    val midY = (startY + endY) / 2f
                    quadraticTo(w - 7.dp.toPx(), midY, w - 2.dp.toPx(), endY)
                }
            }
            drawPath(rightWirePath, color = wireColor, style = wireStroke)

            // --- Bottom Perimeter Wire with Gentle Swags ---
            val bottomSwags = 4
            val bottomSwagWidth = (w - 2f * cornerR) / bottomSwags
            val bottomWirePath = Path().apply {
                moveTo(cornerR, h - 2.dp.toPx())
                for (i in 0 until bottomSwags) {
                    val startX = cornerR + i * bottomSwagWidth
                    val endX = startX + bottomSwagWidth
                    val midX = (startX + endX) / 2f
                    quadraticTo(midX, h + 6.dp.toPx(), endX, h - 2.dp.toPx())
                }
            }
            drawPath(bottomWirePath, color = wireColor, style = wireStroke)

            // Helper to draw a radiant warm golden bulb
            fun drawFairyBulb(cx: Float, cy: Float, index: Int, isAccent: Boolean = false) {
                val sineVal = sin(twinklePhase + index * 0.75f)
                val intensity = 0.55f + 0.45f * ((sineVal + 1f) / 2f)

                val glowRadius = (if (isAccent) 13.dp else 9.dp).toPx() * (0.85f + 0.25f * intensity)
                val coreRadius = (if (isAccent) 3.2.dp else 2.2.dp).toPx()

                // Ambient Radial Glow
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFFE898).copy(alpha = 0.65f * intensity),
                            Color(0xFFFFB800).copy(alpha = 0.28f * intensity),
                            Color(0x00FFD700)
                        ),
                        center = Offset(cx, cy),
                        radius = glowRadius
                    ),
                    radius = glowRadius,
                    center = Offset(cx, cy)
                )

                // Brilliant warm core
                drawCircle(
                    color = Color(0xFFFFF9E6).copy(alpha = 0.95f),
                    radius = coreRadius,
                    center = Offset(cx, cy)
                )

                // Occasional ✦ sparkle cross star on accent bulbs
                if (isAccent && intensity > 0.72f) {
                    val starSize = 4.5.dp.toPx() * intensity
                    val starColor = Color(0xFFFFFDF5).copy(alpha = (intensity - 0.72f) * 3.5f)
                    drawLine(
                        color = starColor,
                        start = Offset(cx - starSize, cy),
                        end = Offset(cx + starSize, cy),
                        strokeWidth = 1.0.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = starColor,
                        start = Offset(cx, cy - starSize),
                        end = Offset(cx, cy + starSize),
                        strokeWidth = 1.0.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }

            // Top bulbs along swags
            for (i in 0..topSwags) {
                val bx = cornerR + i * topSwagWidth
                drawFairyBulb(bx, 2.dp.toPx(), index = i, isAccent = (i % 2 == 0))
                if (i < topSwags) {
                    val midX = bx + topSwagWidth / 2f
                    drawFairyBulb(midX, 7.5.dp.toPx(), index = i + 10, isAccent = true)
                }
            }

            // Subtle golden fairy wire woven through the top rose bouquet
            val roseWirePath = Path().apply {
                moveTo(w * 0.06f, -42.dp.toPx())
                quadraticTo(w * 0.28f, -98.dp.toPx(), w * 0.50f, -102.dp.toPx())
                quadraticTo(w * 0.72f, -98.dp.toPx(), w * 0.94f, -42.dp.toPx())
            }
            drawPath(roseWirePath, color = wireColor.copy(alpha = 0.70f), style = wireStroke)

            // Rose bouquet accent twinkling lights
            val roseBulbPoints = listOf(
                Offset(w * 0.12f, -55.dp.toPx()),
                Offset(w * 0.24f, -82.dp.toPx()),
                Offset(w * 0.38f, -96.dp.toPx()),
                Offset(w * 0.50f, -104.dp.toPx()),
                Offset(w * 0.62f, -96.dp.toPx()),
                Offset(w * 0.76f, -82.dp.toPx()),
                Offset(w * 0.88f, -55.dp.toPx())
            )
            roseBulbPoints.forEachIndexed { idx, pt ->
                drawFairyBulb(pt.x, pt.y, index = idx + 60, isAccent = true)
            }

            // Left vertical bulbs
            val leftCount = 6
            val leftStep = (h - 2f * cornerR) / leftCount
            for (i in 1..leftCount) {
                val by = cornerR + i * leftStep
                val isPeak = (i % 2 == 1)
                val bx = if (isPeak) 6.dp.toPx() else 2.5.dp.toPx()
                drawFairyBulb(bx, by, index = i + 20, isAccent = isPeak)
            }

            // Right vertical bulbs
            val rightCount = 6
            val rightStep = (h - 2f * cornerR) / rightCount
            for (i in 1..rightCount) {
                val by = cornerR + i * rightStep
                val isPeak = (i % 2 == 1)
                val bx = if (isPeak) w - 6.dp.toPx() else w - 2.5.dp.toPx()
                drawFairyBulb(bx, by, index = i + 30, isAccent = isPeak)
            }

            // Bottom bulbs along swags
            for (i in 0..bottomSwags) {
                val bx = cornerR + i * bottomSwagWidth
                drawFairyBulb(bx, h - 2.dp.toPx(), index = i + 40, isAccent = (i % 2 == 1))
                if (i < bottomSwags) {
                    val midX = bx + bottomSwagWidth / 2f
                    drawFairyBulb(midX, h + 5.5.dp.toPx(), index = i + 50, isAccent = true)
                }
            }
        }

        // -------------------------------------------------------------
        // 2. HANGING GOLDEN LANTERNS (Left & Right of Arabic Banner)
        // Suspended with chains along the outer green banner edges
        // -------------------------------------------------------------
        // Left Lantern
        Image(
            painter = rememberAssetPainter("media/hanging_gold_lantern.webp"),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 10.dp, y = 46.dp)
                .size(width = 24.dp, height = 82.dp)
        )

        // Right Lantern
        Image(
            painter = rememberAssetPainter("media/hanging_gold_lantern.webp"),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-10).dp, y = 46.dp)
                .size(width = 24.dp, height = 82.dp)
        )

        // -------------------------------------------------------------
        // 3. ROYAL ROSE GULDASTA TIED WITH GOLDEN BOW ABOVE "99 Names of Allah"
        // Floats gracefully in the dedicated space ABOVE the card.
        // Golden ribbon tails drape down into the center blank space between
        // the left title "99 Names of Allah" and the right "View all →" button.
        // -------------------------------------------------------------
        Image(
            painter = rememberAssetPainter("media/rose_guldasta_tied_banner.webp"),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-125).dp)
                .fillMaxWidth(0.96f)
                .aspectRatio(1297f / 515f)
        )
    }
}
