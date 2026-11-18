package com.tasbih.digitaltasbih.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Minimal & Ultra-Luxury Ramadan Hanging Fairy Lights Garland ("Light ki Ladi")
 * Designed based on the user's festive reference aesthetic, refined into a clean, minimal layout:
 * - 3 graceful golden catenary wire swags draped across the top header.
 * - Perfectly spaced warm-golden fairy light bulbs with incandescent core, golden halo bloom, and breathing twinkle.
 * - Two exquisite hanging golden Islamic Fanoos lanterns framing the left and right flanks of the header card.
 * - Center swag features a delicate suspended golden jewel drop.
 * - Soft ambient ceiling warm spill and floating micro star sparkles.
 * - Strictly minimal: keeps all lower screen cards, buttons, and navigation 100% clean and uncluttered.
 * - 100% click-through transparent so all underlying buttons remain fully accessible.
 */
@Composable
fun RamadanDecorationGarland(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition(label = "RamadanMinimalLightsTransition")

    // Continuous time phase for organic shimmering bulbs
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(6500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "RamadanLightPhase"
    )

    // Ultra-gentle lantern pendulum sway (-1.2 to +1.2 degrees)
    val lanternSway by infiniteTransition.animateFloat(
        initialValue = -1.2f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(4200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "LanternSway"
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp)
    ) {
        val width = size.width
        val height = size.height

        // ══════════════════════════════════════════════════════════════════
        // 1. SOFT AMBIENT CEILING WARM FESTIVAL SPILL
        // ══════════════════════════════════════════════════════════════════
        val ambientColor = if (isDarkTheme) Color(0x38FFD54F) else Color(0x22E5C07B)
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    ambientColor,
                    ambientColor.copy(alpha = ambientColor.alpha * 0.40f),
                    Color.Transparent
                ),
                startY = 0f,
                endY = height * 0.85f
            ),
            size = Size(width, height * 0.85f)
        )

        // ══════════════════════════════════════════════════════════════════
        // 2. METALLIC GOLDEN SWAG WIRES (3 Graceful Swags across top header)
        // ══════════════════════════════════════════════════════════════════
        val wireBrush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFE5C07B),
                Color(0xFFFFF2B2),
                Color(0xFFD4AF37),
                Color(0xFFA8791E),
                Color(0xFFFFF2B2)
            )
        )

        val leftAnchorX = 18.dp.toPx()
        val rightAnchorX = width - 18.dp.toPx()
        val topAnchorY = 4.dp.toPx()

        val joint1X = width * 0.34f
        val joint1Y = 5.dp.toPx()
        val joint2X = width * 0.66f
        val joint2Y = 5.dp.toPx()

        // Lead-in wires from screen outer edges to lanterns
        val leadLeft = Path().apply {
            moveTo(0f, 2.dp.toPx())
            quadraticTo(leftAnchorX * 0.5f, 3.dp.toPx(), leftAnchorX, topAnchorY)
        }
        val leadRight = Path().apply {
            moveTo(width, 2.dp.toPx())
            quadraticTo(width - (width - rightAnchorX) * 0.5f, 3.dp.toPx(), rightAnchorX, topAnchorY)
        }
        drawPath(leadLeft, brush = wireBrush, style = Stroke(width = 1.2.dp.toPx(), cap = StrokeCap.Round))
        drawPath(leadRight, brush = wireBrush, style = Stroke(width = 1.2.dp.toPx(), cap = StrokeCap.Round))

        // Definition of the 3 primary swags: (startX, startY, midX, sagY, endX, endY)
        data class SwagDef(
            val startX: Float, val startY: Float,
            val midX: Float, val sagY: Float,
            val endX: Float, val endY: Float,
            val tPoints: FloatArray
        )

        val swags = listOf(
            // Left Swag (over logo & app subtitle)
            SwagDef(
                startX = leftAnchorX, startY = topAnchorY,
                midX = (leftAnchorX + joint1X) / 2f, sagY = 17.dp.toPx(),
                endX = joint1X, endY = joint1Y,
                tPoints = floatArrayOf(0.24f, 0.52f, 0.78f)
            ),
            // Center Swag (gracefully dipped over the header gap with a center jewel)
            SwagDef(
                startX = joint1X, startY = joint1Y,
                midX = (joint1X + joint2X) / 2f, sagY = 21.dp.toPx(),
                endX = joint2X, endY = joint2Y,
                tPoints = floatArrayOf(0.18f, 0.38f, 0.50f, 0.62f, 0.82f)
            ),
            // Right Swag (over header buttons)
            SwagDef(
                startX = joint2X, startY = joint2Y,
                midX = (joint2X + rightAnchorX) / 2f, sagY = 17.dp.toPx(),
                endX = rightAnchorX, endY = topAnchorY,
                tPoints = floatArrayOf(0.22f, 0.48f, 0.76f)
            )
        )

        // Draw Swags and collect bulb positions
        val bulbPositions = mutableListOf<Triple<Offset, Int, Float>>() // pos, bulbIdx, dropLen
        var globalBulbIdx = 0

        for (swag in swags) {
            val swagPath = Path().apply {
                moveTo(swag.startX, swag.startY)
                quadraticTo(swag.midX, swag.sagY, swag.endX, swag.endY)
            }

            // Wire drop shadow for 3D realism
            drawPath(
                path = swagPath,
                color = Color(0x40000000),
                style = Stroke(width = 2.4.dp.toPx(), cap = StrokeCap.Round)
            )
            // Metallic golden wire
            drawPath(
                path = swagPath,
                brush = wireBrush,
                style = Stroke(width = 1.35.dp.toPx(), cap = StrokeCap.Round)
            )
            // Subtle top glint highlight
            drawPath(
                path = swagPath,
                color = Color(0x90FFFFFF),
                style = Stroke(width = 0.55.dp.toPx(), cap = StrokeCap.Round)
            )

            // Rosette bead at start anchor
            drawCircle(brush = wireBrush, radius = 2.2.dp.toPx(), center = Offset(swag.startX, swag.startY))
            drawCircle(color = Color(0x99FFFFFF), radius = 0.9.dp.toPx(), center = Offset(swag.startX - 0.4.dp.toPx(), swag.startY - 0.4.dp.toPx()))

            // Calculate bulbs along this swag curve
            for (t in swag.tPoints) {
                val oneMinusT = 1f - t
                val bx = oneMinusT * oneMinusT * swag.startX + 2f * oneMinusT * t * swag.midX + t * t * swag.endX
                val by = oneMinusT * oneMinusT * swag.startY + 2f * oneMinusT * t * swag.sagY + t * t * swag.endY

                val dropLen = when {
                    // Center nadir of center swag gets a graceful drop
                    swag.sagY > 20.dp.toPx() && t in 0.48f..0.52f -> 6.0.dp.toPx()
                    t in 0.45f..0.55f -> 4.5.dp.toPx()
                    else -> 2.5.dp.toPx()
                }

                bulbPositions.add(Triple(Offset(bx, by), globalBulbIdx, dropLen))
                globalBulbIdx++
            }
        }

        // Final rosette bead at right lantern anchor
        drawCircle(brush = wireBrush, radius = 2.2.dp.toPx(), center = Offset(rightAnchorX, topAnchorY))
        drawCircle(color = Color(0x99FFFFFF), radius = 0.9.dp.toPx(), center = Offset(rightAnchorX - 0.4.dp.toPx(), topAnchorY - 0.4.dp.toPx()))

        // ══════════════════════════════════════════════════════════════════
        // 3. FAIRY LIGHT BULBS (Warm 2700K Glow with gentle breathing shimmer)
        // ══════════════════════════════════════════════════════════════════
        for ((pos, idx, dropLen) in bulbPositions) {
            val bulbBottomY = pos.y + dropLen

            // Tiny vertical suspension wire
            drawLine(
                color = Color(0xCCD4AF37),
                start = pos,
                end = Offset(pos.x, bulbBottomY),
                strokeWidth = 0.85.dp.toPx()
            )

            // Brass socket cap
            drawRect(
                color = Color(0xFF996515),
                topLeft = Offset(pos.x - 1.6.dp.toPx(), bulbBottomY - 1.2.dp.toPx()),
                size = Size(3.2.dp.toPx(), 2.2.dp.toPx())
            )

            // Shimmer oscillation
            val phase = time + (idx * 1.15f)
            val shimmer = (sin(phase) * 0.5f + 0.5f) // 0.0 to 1.0

            val bulbCenter = Offset(pos.x, bulbBottomY + 3.4.dp.toPx())

            // Warm incandescent golden color palette
            val (coreColor, glowColor) = when (idx % 3) {
                0 -> Pair(Color(0xFFFFEE58), Color(0xFFFFB300)) // Warm Golden Amber
                1 -> Pair(Color(0xFFFFFDE7), Color(0xFFFFCA28)) // Luminous Champagne
                else -> Pair(Color(0xFFFFE082), Color(0xFFFFA000)) // Deep Honey Gold
            }

            // Layer 1: Radiant Outer Glow Bloom
            val haloRadius = (12.dp.toPx() + shimmer * 5.5.dp.toPx())
            val haloAlpha = (0.35f + shimmer * 0.40f)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        glowColor.copy(alpha = haloAlpha),
                        glowColor.copy(alpha = haloAlpha * 0.35f),
                        Color.Transparent
                    ),
                    center = bulbCenter,
                    radius = haloRadius
                ),
                radius = haloRadius,
                center = bulbCenter
            )

            // Layer 2: Glowing Bulb Body
            val bulbRadius = (3.2.dp.toPx() + shimmer * 0.5.dp.toPx())
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(coreColor, glowColor),
                    center = bulbCenter,
                    radius = bulbRadius
                ),
                radius = bulbRadius,
                center = bulbCenter
            )

            // Layer 3: Hot Filament Sparkle Core
            drawCircle(
                color = Color(0xFFFFFFFF),
                radius = 1.2.dp.toPx(),
                center = Offset(bulbCenter.x - 0.3.dp.toPx(), bulbCenter.y - 0.3.dp.toPx())
            )
        }

        // Center Hanging Jewel on the middle swag nadir
        val centerNadirBulb = bulbPositions.find { it.second == 5 }
        if (centerNadirBulb != null) {
            val jewelTopY = centerNadirBulb.first.y + centerNadirBulb.third + 8.dp.toPx()
            drawCircle(
                color = Color(0xFFFFF2B2),
                radius = 1.8.dp.toPx(),
                center = Offset(centerNadirBulb.first.x, jewelTopY)
            )
            drawCircle(
                color = Color(0xFFFFFFFF),
                radius = 0.8.dp.toPx(),
                center = Offset(centerNadirBulb.first.x - 0.3.dp.toPx(), jewelTopY - 0.3.dp.toPx())
            )
        }

        // ══════════════════════════════════════════════════════════════════
        // 4. FLOATING SUBTLE BOKEH / STAR SPARKLES
        // ══════════════════════════════════════════════════════════════════
        val sparklePoints = listOf(
            Triple(width * 0.16f, 24.dp.toPx(), 0.8f),
            Triple(width * 0.36f, 26.dp.toPx(), 2.1f),
            Triple(width * 0.64f, 26.dp.toPx(), 3.5f),
            Triple(width * 0.84f, 24.dp.toPx(), 4.7f)
        )
        for ((sx, sy, phaseOff) in sparklePoints) {
            val sparkShimmer = (sin(time * 1.5f + phaseOff) * 0.5f + 0.5f)
            if (sparkShimmer > 0.35f) {
                val sparkR = 2.2.dp.toPx() * (sparkShimmer - 0.35f) / 0.65f
                drawCircle(
                    color = Color(0xFFFFD54F).copy(alpha = 0.55f * sparkShimmer),
                    radius = sparkR * 2.5f,
                    center = Offset(sx, sy)
                )
                drawCircle(
                    color = Color(0xFFFFFFFF).copy(alpha = 0.90f * sparkShimmer),
                    radius = sparkR * 0.9f,
                    center = Offset(sx, sy)
                )
            }
        }

        // ══════════════════════════════════════════════════════════════════
        // 5. TWO MAJESTIC HANGING GOLDEN FANOOS LANTERNS (Left & Right Flanks)
        // Suspended on beaded golden chains, framing the header card elegantly!
        // ══════════════════════════════════════════════════════════════════

        // Left Lantern (x = leftAnchorX = 18dp)
        drawFanoosLantern(
            anchor = Offset(leftAnchorX, topAnchorY),
            chainLength = 16.dp.toPx(),
            shimmer = (sin(time * 1.2f) * 0.5f + 0.5f),
            swayAngle = lanternSway,
            isDarkTheme = isDarkTheme
        )

        // Right Lantern (Next to Day/Night mode button) - lowered down to LanguageBar level
        drawFanoosLantern(
            anchor = Offset(rightAnchorX, topAnchorY),
            chainLength = 54.dp.toPx(),
            shimmer = (sin(time * 1.2f + 1.6f) * 0.5f + 0.5f),
            swayAngle = -lanternSway,
            isDarkTheme = isDarkTheme
        )
    }
}

/**
 * Renders an authentic Islamic Fanoos Lantern
 * With:
 * - Golden beaded suspension chain
 * - Moorish pointed dome with finial
 * - Glowing glass chamber with warm internal flame
 * - Golden lattice frame
 * - Scalloped pedestal base
 * - Hanging faceted bottom teardrop jewel
 */
private fun DrawScope.drawFanoosLantern(
    anchor: Offset,
    chainLength: Float,
    shimmer: Float,
    swayAngle: Float,
    isDarkTheme: Boolean
) {
    // Subtle lateral sway at the chain bottom
    val swayPx = swayAngle * 1.8.dp.toPx()
    val topX = anchor.x + swayPx
    val topY = anchor.y + chainLength

    // 1. Delicate Golden Beaded Suspension Chain
    val numBeads = 6
    for (i in 0..numBeads) {
        val t = i / numBeads.toFloat()
        val bx = anchor.x + (topX - anchor.x) * t
        val by = anchor.y + chainLength * t
        drawCircle(
            color = Color(0xFFD4AF37),
            radius = 1.0.dp.toPx(),
            center = Offset(bx, by)
        )
    }

    val lanternW = 20.dp.toPx()
    val halfW = lanternW / 2f
    val bodyH = 24.dp.toPx()

    // 2. Suspension Ring
    drawCircle(
        color = Color(0xFFD4AF37),
        radius = 2.4.dp.toPx(),
        center = Offset(topX, topY),
        style = Stroke(width = 1.2.dp.toPx())
    )

    // 3. Moorish Pointed Dome (Gold Gradient Canopy)
    val domeTopY = topY + 2.5.dp.toPx()
    val domeBaseY = domeTopY + 7.0.dp.toPx()
    val domePath = Path().apply {
        moveTo(topX, domeTopY)
        quadraticTo(topX + halfW * 0.65f, domeTopY + 1.5.dp.toPx(), topX + halfW, domeBaseY)
        lineTo(topX - halfW, domeBaseY)
        quadraticTo(topX - halfW * 0.65f, domeTopY + 1.5.dp.toPx(), topX, domeTopY)
        close()
    }

    val goldGrad = Brush.linearGradient(
        colors = listOf(
            Color(0xFFFFF2B2),
            Color(0xFFE5C07B),
            Color(0xFFD4AF37),
            Color(0xFF8C5D14)
        )
    )
    drawPath(path = domePath, brush = goldGrad, style = Fill)
    drawPath(path = domePath, color = Color(0xFFFFF7D6), style = Stroke(width = 0.8.dp.toPx()))

    // Finial crest on dome tip
    drawCircle(
        color = Color(0xFFFFF2B2),
        radius = 1.2.dp.toPx(),
        center = Offset(topX, domeTopY)
    )

    // 4. Glass Chamber (Tapered hexagonal body)
    val chamberBaseY = domeBaseY + bodyH * 0.65f
    val chamberBottomW = lanternW * 0.68f
    val halfBottomW = chamberBottomW / 2f

    val glassPath = Path().apply {
        moveTo(topX - halfW, domeBaseY)
        lineTo(topX + halfW, domeBaseY)
        lineTo(topX + halfBottomW, chamberBaseY)
        lineTo(topX - halfBottomW, chamberBaseY)
        close()
    }

    // Glass background
    val glassBg = if (isDarkTheme) Color(0xC008241A) else Color(0x75FFF8E1)
    drawPath(path = glassPath, color = glassBg, style = Fill)

    // Radiant flame glow from inside the lantern
    val flameCenter = Offset(topX, domeBaseY + (chamberBaseY - domeBaseY) * 0.5f)
    val flameHaloR = 26.dp.toPx()
    val flameGlowAlpha = 0.38f + shimmer * 0.42f

    // Outer Ambient Glow Bloom
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFFFFB300).copy(alpha = flameGlowAlpha),
                Color(0xFFFFD54F).copy(alpha = flameGlowAlpha * 0.35f),
                Color.Transparent
            ),
            center = flameCenter,
            radius = flameHaloR
        ),
        radius = flameHaloR,
        center = flameCenter
    )

    // Inner Glowing Candle Flame
    val flameW = (3.0.dp.toPx() + shimmer * 0.8.dp.toPx())
    val flameH = (7.5.dp.toPx() + shimmer * 1.5.dp.toPx())
    val flamePath = Path().apply {
        moveTo(flameCenter.x, flameCenter.y - flameH / 2f)
        cubicTo(
            flameCenter.x + flameW, flameCenter.y,
            flameCenter.x + flameW * 0.5f, flameCenter.y + flameH / 2f,
            flameCenter.x, flameCenter.y + flameH / 2f
        )
        cubicTo(
            flameCenter.x - flameW * 0.5f, flameCenter.y + flameH / 2f,
            flameCenter.x - flameW, flameCenter.y,
            flameCenter.x, flameCenter.y - flameH / 2f
        )
        close()
    }
    drawPath(
        path = flamePath,
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFFFFFFFF), Color(0xFFFFD54F), Color(0xFFFF8F00)),
            startY = flameCenter.y - flameH / 2f,
            endY = flameCenter.y + flameH / 2f
        )
    )

    // Golden frame lattice ribs over glass
    drawLine(
        color = Color(0xFFD4AF37),
        start = Offset(topX, domeBaseY),
        end = Offset(topX, chamberBaseY),
        strokeWidth = 1.0.dp.toPx()
    )
    drawLine(
        color = Color(0xFFD4AF37),
        start = Offset(topX - halfW * 0.5f, domeBaseY),
        end = Offset(topX - halfBottomW * 0.5f, chamberBaseY),
        strokeWidth = 0.8.dp.toPx()
    )
    drawLine(
        color = Color(0xFFD4AF37),
        start = Offset(topX + halfW * 0.5f, domeBaseY),
        end = Offset(topX + halfBottomW * 0.5f, chamberBaseY),
        strokeWidth = 0.8.dp.toPx()
    )
    drawPath(path = glassPath, color = Color(0xFFE5C07B), style = Stroke(width = 1.0.dp.toPx()))

    // 5. Base Pedestal
    val pedestalH = 4.0.dp.toPx()
    val pedestalBottomY = chamberBaseY + pedestalH
    val pedestalPath = Path().apply {
        moveTo(topX - halfBottomW * 1.1f, chamberBaseY)
        lineTo(topX + halfBottomW * 1.1f, chamberBaseY)
        lineTo(topX + halfBottomW * 0.7f, pedestalBottomY)
        lineTo(topX - halfBottomW * 0.7f, pedestalBottomY)
        close()
    }
    drawPath(path = pedestalPath, brush = goldGrad, style = Fill)
    drawPath(path = pedestalPath, color = Color(0xFFFFF7D6), style = Stroke(width = 0.7.dp.toPx()))

    // 6. Hanging Bottom Teardrop Jewel / Bead
    val jewelCenter = Offset(topX, pedestalBottomY + 4.5.dp.toPx())
    drawCircle(
        color = Color(0xFFFFF2B2),
        radius = 1.8.dp.toPx(),
        center = jewelCenter
    )
    drawCircle(
        color = Color(0xFFFFFFFF),
        radius = 0.7.dp.toPx(),
        center = Offset(jewelCenter.x - 0.35.dp.toPx(), jewelCenter.y - 0.35.dp.toPx())
    )
}
