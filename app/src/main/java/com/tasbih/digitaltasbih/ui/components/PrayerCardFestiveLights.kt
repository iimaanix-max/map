package com.tasbih.digitaltasbih.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sin

/**
 * Natural, Uneven & Hanging Islamic Festive Fairy Light Garland ("Light ki Ladi")
 * Designed based on direct user feedback:
 * - NO rigid "chipka" box borders; instead features loose, organic draped swags.
 * - "Latka & Un-even": Bulbs hang on dropper cords at distinctly varied heights (some short, some long).
 * - "Band-Chalu" (True On/Off Blinking): Bulbs actively turn completely OFF and ON with varied rhythms,
 *   flickering chasers, and sparkling golden glints.
 * - Top-corners feature hanging golden Islamic Fanoos lanterns on swaying chains.
 * - 100% click-through transparent so all underlying buttons remain fully accessible.
 */
@Composable
fun PrayerCardFestiveLights(
    modifier: Modifier = Modifier,
    heroCardHeight: Float = 0f,
    isDarkTheme: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition(label = "FestiveLadiTransition")

    // Continuous time phase for organic asynchronous blinking & twinkling (Slower & peaceful)
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(7500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "FestiveLadiPhase"
    )

    // Gentle pendulum lantern sway (-2 to +2 degrees)
    val lanternSway by infiniteTransition.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(4500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "LanternPendulumSway"
    )

    Canvas(modifier = modifier) {
        val w = size.width
        val h = if (heroCardHeight > 0f) heroCardHeight else size.height

        if (w <= 0 || h <= 0) return@Canvas

        val goldWireColor = Color(0xD9E5C07B)
        val dropperWireColor = Color(0x80D4AF37)

        // ══════════════════════════════════════════════════════════════════
        // 1. TOP HANGING GARLAND (Catenary Swags with Uneven Dropper Bulbs)
        // ══════════════════════════════════════════════════════════════════
        val topSwagCount = 3
        val swagW = w / topSwagCount
        val swagSag = 8.dp.toPx()

        // Draw the top draped golden wire (loose swags across top of card)
        val topWirePath = Path().apply {
            moveTo(0f, 2.dp.toPx())
            for (s in 0 until topSwagCount) {
                val startX = s * swagW
                val endX = (s + 1) * swagW
                val midX = (startX + endX) / 2f
                val controlY = 2.dp.toPx() + swagSag
                quadraticTo(midX, controlY, endX, 2.dp.toPx())
            }
        }
        drawPath(
            path = topWirePath,
            color = goldWireColor,
            style = Stroke(width = 1.1.dp.toPx(), cap = StrokeCap.Round)
        )

        // Top Dropper Bulbs with DISTINCTLY UNEVEN drop lengths ("thoda latka un even")
        // Pair(relativeX, dropDp)
        val topBulbConfigs = listOf(
            Pair(0.06f, 13.dp.toPx()),
            Pair(0.14f, 24.dp.toPx()),  // Long drop
            Pair(0.22f, 7.dp.toPx()),   // Short drop
            Pair(0.31f, 28.dp.toPx()),  // Extra long drop with jewel
            Pair(0.40f, 12.dp.toPx()),
            Pair(0.49f, 22.dp.toPx()),  // Medium long drop
            Pair(0.58f, 9.dp.toPx()),   // Short drop
            Pair(0.67f, 26.dp.toPx()),  // Long drop
            Pair(0.76f, 11.dp.toPx()),
            Pair(0.85f, 29.dp.toPx()),  // Extra long drop
            Pair(0.93f, 14.dp.toPx())
        )

        var bulbId = 0
        topBulbConfigs.forEach { (relX, dropLen) ->
            val bx = w * relX
            // Calculate y on the swag curve at this x
            val sIdx = (bx / swagW).toInt().coerceIn(0, topSwagCount - 1)
            val sLocalX = (bx - sIdx * swagW) / swagW // 0..1
            val swagCurveY = 2.dp.toPx() + (4f * sLocalX * (1f - sLocalX)) * swagSag
            val by = swagCurveY + dropLen

            // Dropper string from wire down to bulb
            drawLine(
                color = dropperWireColor,
                start = Offset(bx, swagCurveY),
                end = Offset(bx, by),
                strokeWidth = 0.8.dp.toPx()
            )

            // Draw Bulb with authentic ON/OFF blinking
            drawBlinkingBulb(
                center = Offset(bx, by),
                index = bulbId++,
                time = time,
                isDarkTheme = isDarkTheme
            )

            // If it's a long drop, add a tiny golden accent jewel
            if (dropLen > 25.dp.toPx()) {
                drawMiniJewelDrop(Offset(bx, by + 5.dp.toPx()), time + bulbId)
            }
        }

        // ══════════════════════════════════════════════════════════════════
        // 2. LEFT SIDE (Loose undulating drape with uneven side droppers)
        // ══════════════════════════════════════════════════════════════════
        val leftStartY = 16.dp.toPx()
        val leftEndY = h - 20.dp.toPx()
        val leftBulbConfigs = listOf(
            Triple(0.22f, 10.dp.toPx(), 4.dp.toPx()),
            Triple(0.45f, 18.dp.toPx(), 9.dp.toPx()),   // Drooping further inward
            Triple(0.68f, 12.dp.toPx(), 3.dp.toPx()),
            Triple(0.88f, 20.dp.toPx(), 8.dp.toPx())
        )

        val leftWirePath = Path().apply {
            moveTo(2.dp.toPx(), leftStartY)
            cubicTo(
                6.dp.toPx(), leftStartY + (leftEndY - leftStartY) * 0.33f,
                3.dp.toPx(), leftStartY + (leftEndY - leftStartY) * 0.66f,
                2.dp.toPx(), leftEndY
            )
        }
        drawPath(
            path = leftWirePath,
            color = goldWireColor.copy(alpha = 0.6f),
            style = Stroke(width = 0.9.dp.toPx(), cap = StrokeCap.Round)
        )

        leftBulbConfigs.forEach { (relY, dropLenY, inwardX) ->
            val baseY = leftStartY + (leftEndY - leftStartY) * relY
            val bx = 2.dp.toPx() + inwardX
            val by = baseY + dropLenY

            drawLine(
                color = dropperWireColor,
                start = Offset(2.dp.toPx(), baseY),
                end = Offset(bx, by),
                strokeWidth = 0.75.dp.toPx()
            )

            drawBlinkingBulb(
                center = Offset(bx, by),
                index = bulbId++,
                time = time,
                isDarkTheme = isDarkTheme
            )
        }

        // ══════════════════════════════════════════════════════════════════
        // 3. RIGHT SIDE (Delicate cascading lights framing the mosque arch)
        // ══════════════════════════════════════════════════════════════════
        val rightStartY = 24.dp.toPx()
        val rightEndY = h - 22.dp.toPx()
        val rightBulbConfigs = listOf(
            Triple(0.20f, 12.dp.toPx(), 3.dp.toPx()),
            Triple(0.44f, 20.dp.toPx(), 8.dp.toPx()),
            Triple(0.68f, 11.dp.toPx(), 2.dp.toPx()),
            Triple(0.88f, 17.dp.toPx(), 7.dp.toPx())
        )

        val rightWirePath = Path().apply {
            moveTo(w - 2.dp.toPx(), rightStartY)
            cubicTo(
                w - 5.dp.toPx(), rightStartY + (rightEndY - rightStartY) * 0.33f,
                w - 3.dp.toPx(), rightStartY + (rightEndY - rightStartY) * 0.66f,
                w - 2.dp.toPx(), rightEndY
            )
        }
        drawPath(
            path = rightWirePath,
            color = goldWireColor.copy(alpha = 0.6f),
            style = Stroke(width = 0.9.dp.toPx(), cap = StrokeCap.Round)
        )

        rightBulbConfigs.forEach { (relY, dropLenY, inwardX) ->
            val baseY = rightStartY + (rightEndY - rightStartY) * relY
            val bx = w - 2.dp.toPx() - inwardX
            val by = baseY + dropLenY

            drawLine(
                color = dropperWireColor,
                start = Offset(w - 2.dp.toPx(), baseY),
                end = Offset(bx, by),
                strokeWidth = 0.75.dp.toPx()
            )

            drawBlinkingBulb(
                center = Offset(bx, by),
                index = bulbId++,
                time = time,
                isDarkTheme = isDarkTheme
            )
        }

        // ══════════════════════════════════════════════════════════════════
        // 4. BOTTOM HANGING GARLAND (Hanging fringe with uneven drops)
        // ══════════════════════════════════════════════════════════════════
        val bottomSwagCount = 3
        val bSwagW = w / bottomSwagCount
        val bSag = 6.dp.toPx()
        val bottomBaseY = h - 2.dp.toPx()

        val bottomWirePath = Path().apply {
            moveTo(8.dp.toPx(), bottomBaseY)
            for (s in 0 until bottomSwagCount) {
                val startX = s * bSwagW + 8.dp.toPx()
                val endX = ((s + 1) * bSwagW - 8.dp.toPx()).coerceAtMost(w - 8.dp.toPx())
                val midX = (startX + endX) / 2f
                val controlY = bottomBaseY + bSag
                quadraticTo(midX, controlY, endX, bottomBaseY)
            }
        }
        drawPath(
            path = bottomWirePath,
            color = goldWireColor,
            style = Stroke(width = 1.dp.toPx(), cap = StrokeCap.Round)
        )

        // Bottom Droppers hanging down over the prayer buttons below ("button ke uppar aane de")
        val bottomBulbConfigs = listOf(
            Pair(0.08f, 18.dp.toPx()),
            Pair(0.20f, 28.dp.toPx()),  // Long drop cascading onto buttons
            Pair(0.35f, 19.dp.toPx()),
            Pair(0.50f, 32.dp.toPx()),  // Center long drop over buttons
            Pair(0.65f, 20.dp.toPx()),
            Pair(0.80f, 29.dp.toPx()),  // Long drop cascading onto buttons
            Pair(0.92f, 17.dp.toPx())
        )

        bottomBulbConfigs.forEach { (relX, dropLen) ->
            val bx = w * relX
            val by = bottomBaseY + dropLen

            drawLine(
                color = dropperWireColor,
                start = Offset(bx, bottomBaseY),
                end = Offset(bx, by),
                strokeWidth = 0.8.dp.toPx()
            )

            drawBlinkingBulb(
                center = Offset(bx, by),
                index = bulbId++,
                time = time,
                isDarkTheme = isDarkTheme
            )
        }

        // ══════════════════════════════════════════════════════════════════
        // 5. TOP CORNER HANGING ISLAMIC LANTERNS (Fanoos with swaying chain)
        // ══════════════════════════════════════════════════════════════════
        drawSwayingLantern(
            mount = Offset(14.dp.toPx(), 1.dp.toPx()),
            chainLen = 14.dp.toPx(),
            lanternH = 18.dp.toPx(),
            swayDeg = lanternSway,
            time = time
        )
        drawSwayingLantern(
            mount = Offset(w - 14.dp.toPx(), 1.dp.toPx()),
            chainLen = 14.dp.toPx(),
            lanternH = 18.dp.toPx(),
            swayDeg = -lanternSway,
            time = time + 1.4f
        )

        // ══════════════════════════════════════════════════════════════════
        // 6. ILLUMINATED MOSQUE ARCH & CRESCENT LIGHTS (Exact Reference Image 2)
        // ══════════════════════════════════════════════════════════════════
        drawIlluminatedMosqueArchAndCrescent(
            cardW = w,
            cardH = h,
            time = time
        )
    }
}

/**
 * Draws a fairy light bulb with authentic "BAND-CHALU" (On/Off) blinking:
 * - Group 0: Sharp ON/OFF blink (turns fully dark, then flashes bright ON)
 * - Group 1: Fast twinkle with golden spark burst
 * - Group 2: Occasional dramatic flare-up and fade-to-black
 * - Group 3: Gentle warm breathing baseline
 */
private fun DrawScope.drawBlinkingBulb(
    center: Offset,
    index: Int,
    time: Float,
    isDarkTheme: Boolean
) {
    val mode = index % 4

    // Calculate light intensity (0f = completely OFF/band, 1f = bright ON/chalu)
    // Slower, smoother wave multipliers for serene ambiance
    val intensity: Float = when (mode) {
        0 -> {
            // SHARP BLINK (Band-Chalu): Turns completely OFF then ON sharply at calm pace!
            val wave = sin(time * 1.6f + index * 1.57f)
            if (wave > 0.1f) 1.0f else 0.05f
        }
        1 -> {
            // FAST TWINKLE (Soft on/off sparkle)
            val wave = sin(time * 2.8f + index * 2.31f)
            if (wave > 0.25f) 1.0f else 0.08f
        }
        2 -> {
            // OCCASIONAL DRAMATIC FLARE: Mostly dark, then flares up bright and fades!
            val flare = max(0f, sin(time * 0.9f + index * 1.83f)).pow(5.5f)
            (0.06f + 0.94f * flare).coerceIn(0f, 1f)
        }
        else -> {
            // WARM BREATHING GLOW (So whole garland is never 100% dark at once)
            val breathe = 0.35f + 0.65f * (0.5f + 0.5f * sin(time * 1.2f + index * 0.91f))
            breathe.coerceIn(0f, 1f)
        }
    }

    val isBulbOn = intensity > 0.25f

    // 1. Socket Mount Cap (Always visible even when light is OFF)
    val socketW = 3.2.dp.toPx()
    val socketH = 2.2.dp.toPx()
    drawRect(
        color = Color(0xFFC5A059),
        topLeft = Offset(center.x - socketW / 2f, center.y - socketH - 1.dp.toPx()),
        size = Size(socketW, socketH)
    )

    if (isBulbOn) {
        // 2. Radiant Outer Bloom Halo (when ON)
        val haloRadiusOuter = 12.dp.toPx() * intensity
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFFD54F).copy(alpha = 0.42f * intensity),
                    Color(0xFFFFB300).copy(alpha = 0.18f * intensity),
                    Color.Transparent
                ),
                center = center,
                radius = haloRadiusOuter
            ),
            radius = haloRadiusOuter,
            center = center
        )

        // 3. Intense Incandescent Inner Glow
        val haloRadiusInner = 5.5.dp.toPx() * intensity
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFFFDE7).copy(alpha = 0.90f * intensity),
                    Color(0xFFFFD54F).copy(alpha = 0.50f * intensity),
                    Color.Transparent
                ),
                center = center,
                radius = haloRadiusInner
            ),
            radius = haloRadiusInner,
            center = center
        )

        // 4. Bright Golden Core Bulb
        val bulbR = 2.8.dp.toPx()
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color.White,
                    Color(0xFFFFF9C4),
                    Color(0xFFFFCA28)
                ),
                center = center,
                radius = bulbR
            ),
            radius = bulbR,
            center = center
        )

        // 5. Specular highlight
        drawCircle(
            color = Color.White.copy(alpha = 0.95f * intensity),
            radius = 0.9.dp.toPx(),
            center = Offset(center.x - 0.7.dp.toPx(), center.y - 0.7.dp.toPx())
        )

        // 6. Cross Star Sparkle Glint (when flared bright ON)
        if (intensity > 0.85f && (index % 2 == 0)) {
            val spR = 3.8.dp.toPx() * intensity
            val spColor = Color(0xFFFFFDE7).copy(alpha = 0.9f)
            drawLine(
                color = spColor,
                start = Offset(center.x - spR, center.y),
                end = Offset(center.x + spR, center.y),
                strokeWidth = 0.9.dp.toPx(),
                cap = StrokeCap.Round
            )
            drawLine(
                color = spColor,
                start = Offset(center.x, center.y - spR),
                end = Offset(center.x, center.y + spR),
                strokeWidth = 0.9.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
    } else {
        // BULB IS OFF (Band): Shows subtle warm glass bead / filament resting state
        val bulbR = 2.4.dp.toPx()
        drawCircle(
            color = Color(0x55B8860B),
            radius = bulbR,
            center = center
        )
        // Tiny dormant glass specular
        drawCircle(
            color = Color(0x66FFF8E1),
            radius = 0.7.dp.toPx(),
            center = Offset(center.x - 0.6.dp.toPx(), center.y - 0.6.dp.toPx())
        )
    }
}

/**
 * Hanging Islamic Fanoos Lantern swaying gently on a suspension chain.
 */
private fun DrawScope.drawSwayingLantern(
    mount: Offset,
    chainLen: Float,
    lanternH: Float,
    swayDeg: Float,
    time: Float
) {
    val swayRad = swayDeg * (PI.toFloat() / 180f)
    val lanternTop = Offset(
        x = mount.x + sin(swayRad) * chainLen,
        y = mount.y + cos(swayRad) * chainLen
    )

    // Suspension chain
    drawLine(
        color = Color(0xFFD4AF37),
        start = mount,
        end = lanternTop,
        strokeWidth = 1.dp.toPx()
    )

    val bodyW = 9.dp.toPx()
    val bodyH = lanternH - 4.dp.toPx()
    val lCenter = Offset(
        x = lanternTop.x + sin(swayRad) * (bodyH / 2f),
        y = lanternTop.y + cos(swayRad) * (bodyH / 2f)
    )

    // Lantern warm pulsing halo
    val pulse = 0.5f + 0.5f * sin(time * 2.2f)
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFFFFD54F).copy(alpha = 0.45f * pulse),
                Color(0xFFFFB300).copy(alpha = 0.15f * pulse),
                Color.Transparent
            ),
            center = lCenter,
            radius = 13.dp.toPx()
        ),
        radius = 13.dp.toPx(),
        center = lCenter
    )

    // Fanoos hexagonal body
    val fanoosPath = Path().apply {
        moveTo(lCenter.x, lanternTop.y)
        lineTo(lCenter.x + bodyW / 2f, lanternTop.y + bodyH * 0.35f)
        lineTo(lCenter.x + bodyW * 0.35f, lanternTop.y + bodyH * 0.85f)
        lineTo(lCenter.x, lanternTop.y + bodyH)
        lineTo(lCenter.x - bodyW * 0.35f, lanternTop.y + bodyH * 0.85f)
        lineTo(lCenter.x - bodyW / 2f, lanternTop.y + bodyH * 0.35f)
        close()
    }

    drawPath(
        path = fanoosPath,
        brush = Brush.radialGradient(
            colors = listOf(Color(0xFFFFF9C4), Color(0xFFFFD54F), Color(0xFFFF8F00)),
            center = lCenter,
            radius = bodyW
        ),
        style = Fill
    )

    drawPath(
        path = fanoosPath,
        color = Color(0xFFE5C07B),
        style = Stroke(width = 1.1.dp.toPx(), cap = StrokeCap.Round)
    )

    drawCircle(
        color = Color.White.copy(alpha = 0.9f * pulse),
        radius = 1.8.dp.toPx(),
        center = lCenter
    )
}

/**
 * Small dangling golden jewel drop on long dropper cords.
 */
private fun DrawScope.drawMiniJewelDrop(
    center: Offset,
    time: Float
) {
    val shimmer = 0.5f + 0.5f * sin(time * 2.5f)
    val r = 2.2.dp.toPx()
    val diamondPath = Path().apply {
        moveTo(center.x, center.y - r)
        lineTo(center.x + r, center.y)
        lineTo(center.x, center.y + r)
        lineTo(center.x - r, center.y)
        close()
    }
    drawPath(
        path = diamondPath,
        color = Color(0xFFFFF8E1).copy(alpha = 0.85f * shimmer),
        style = Fill
    )
}

/**
 * Illuminates the Mosque Arch, Crescent Moon, and Minarets with golden fairy lights
 * Exactly as requested in user's reference aesthetic (Reference Image 2):
 * - Radiant warm golden glow bloom around the crescent moon.
 * - Sparkling 4-point star glint next to crescent.
 * - Golden fairy lights tracing the contour of the Moorish horseshoe arch.
 * - Shimmering beacon lights on minaret crescents and central dome finial.
 */
private fun DrawScope.drawIlluminatedMosqueArchAndCrescent(
    cardW: Float,
    cardH: Float,
    time: Float
) {
    val cardPad = 16.dp.toPx()
    val mSize = 175.dp.toPx()
    val mLeft = cardW - cardPad - mSize
    val mTop = cardH - cardPad - mSize

    // 1. Crescent Moon (Chand) Radiant Golden Glow Bloom
    val moonCenter = Offset(mLeft + mSize * 0.460f, mTop + mSize * 0.391f)
    val moonPulse = 0.5f + 0.5f * sin(time * 1.3f)
    val moonHaloR = 24.dp.toPx()
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFFFFD54F).copy(alpha = 0.45f * moonPulse + 0.22f),
                Color(0xFFFFB300).copy(alpha = 0.18f * moonPulse + 0.08f),
                Color.Transparent
            ),
            center = moonCenter,
            radius = moonHaloR
        ),
        radius = moonHaloR,
        center = moonCenter
    )

    // 2. Star Glint next to Crescent Moon
    val starCenter = Offset(mLeft + mSize * 0.550f, mTop + mSize * 0.395f)
    val starPulse = 0.5f + 0.5f * sin(time * 2.2f + 1.2f)
    val starR = 5.0.dp.toPx() * (0.6f + 0.4f * starPulse)
    // Golden star rays
    drawLine(
        color = Color(0xFFFFFDE7).copy(alpha = 0.90f * starPulse),
        start = Offset(starCenter.x - starR, starCenter.y),
        end = Offset(starCenter.x + starR, starCenter.y),
        strokeWidth = 1.0.dp.toPx(),
        cap = StrokeCap.Round
    )
    drawLine(
        color = Color(0xFFFFFDE7).copy(alpha = 0.90f * starPulse),
        start = Offset(starCenter.x, starCenter.y - starR),
        end = Offset(starCenter.x, starCenter.y + starR),
        strokeWidth = 1.0.dp.toPx(),
        cap = StrokeCap.Round
    )
    drawCircle(
        color = Color.White.copy(alpha = 0.95f),
        radius = 1.2.dp.toPx(),
        center = starCenter
    )

    // 3. Mosque Finial Light Points (Minarets and Main Dome)
    val finialPoints = listOf(
        Offset(mLeft + mSize * 0.308f, mTop + mSize * 0.470f), // Left Minaret tip
        Offset(mLeft + mSize * 0.672f, mTop + mSize * 0.470f), // Right Minaret tip
        Offset(mLeft + mSize * 0.490f, mTop + mSize * 0.522f)  // Dome finial
    )
    finialPoints.forEachIndexed { fIdx, fPos ->
        val fShimmer = 0.5f + 0.5f * sin(time * 2.0f + fIdx * 1.8f)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFFD54F).copy(alpha = 0.65f * fShimmer),
                    Color.Transparent
                ),
                center = fPos,
                radius = 6.dp.toPx()
            ),
            radius = 6.dp.toPx(),
            center = fPos
        )
        drawCircle(
            color = Color(0xFFFFFDE7).copy(alpha = 0.90f * fShimmer),
            radius = 1.4.dp.toPx(),
            center = fPos
        )
    }

    // 4. Exact Arch Contour Bulb Positions Tracing the Golden Moorish Arch
    val archBulbPoints = listOf(
        // Left pillar going up
        Offset(mLeft + mSize * 0.133f, mTop + mSize * 0.830f),
        Offset(mLeft + mSize * 0.133f, mTop + mSize * 0.720f),
        Offset(mLeft + mSize * 0.135f, mTop + mSize * 0.600f),
        Offset(mLeft + mSize * 0.135f, mTop + mSize * 0.480f),
        // Left notch / shoulder step
        Offset(mLeft + mSize * 0.147f, mTop + mSize * 0.400f),
        Offset(mLeft + mSize * 0.183f, mTop + mSize * 0.355f),
        // Left arch curve
        Offset(mLeft + mSize * 0.239f, mTop + mSize * 0.310f),
        Offset(mLeft + mSize * 0.271f, mTop + mSize * 0.255f),
        Offset(mLeft + mSize * 0.341f, mTop + mSize * 0.205f),
        Offset(mLeft + mSize * 0.417f, mTop + mSize * 0.160f),
        Offset(mLeft + mSize * 0.464f, mTop + mSize * 0.120f),
        // Apex (Top center of arch)
        Offset(mLeft + mSize * 0.490f, mTop + mSize * 0.088f),
        // Right arch curve going down
        Offset(mLeft + mSize * 0.517f, mTop + mSize * 0.120f),
        Offset(mLeft + mSize * 0.562f, mTop + mSize * 0.160f),
        Offset(mLeft + mSize * 0.639f, mTop + mSize * 0.205f),
        Offset(mLeft + mSize * 0.708f, mTop + mSize * 0.255f),
        Offset(mLeft + mSize * 0.740f, mTop + mSize * 0.310f),
        // Right notch / shoulder step
        Offset(mLeft + mSize * 0.786f, mTop + mSize * 0.355f),
        Offset(mLeft + mSize * 0.832f, mTop + mSize * 0.400f),
        // Right pillar going down
        Offset(mLeft + mSize * 0.844f, mTop + mSize * 0.480f),
        Offset(mLeft + mSize * 0.844f, mTop + mSize * 0.600f),
        Offset(mLeft + mSize * 0.844f, mTop + mSize * 0.720f),
        Offset(mLeft + mSize * 0.845f, mTop + mSize * 0.830f)
    )

    // Arch outline connecting wire precisely following the bulbs
    val archWirePath = Path().apply {
        moveTo(archBulbPoints.first().x, archBulbPoints.first().y)
        for (i in 1 until archBulbPoints.size) {
            lineTo(archBulbPoints[i].x, archBulbPoints[i].y)
        }
    }
    drawPath(
        path = archWirePath,
        color = Color(0x88D4AF37),
        style = Stroke(width = 0.9.dp.toPx(), cap = StrokeCap.Round)
    )

    archBulbPoints.forEachIndexed { aIdx, aPos ->
        val aWave = sin(time * 1.5f + aIdx * 0.65f)
        val aIntensity = 0.35f + 0.65f * (0.5f + 0.5f * aWave)
        val aRadius = 2.0.dp.toPx()
        val aHaloR = 7.5.dp.toPx() * aIntensity

        // Outer warm golden bloom
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFFD54F).copy(alpha = 0.55f * aIntensity),
                    Color(0xFFFFB300).copy(alpha = 0.22f * aIntensity),
                    Color.Transparent
                ),
                center = aPos,
                radius = aHaloR
            ),
            radius = aHaloR,
            center = aPos
        )

        // Golden bulb bead
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color.White, Color(0xFFFFF9C4), Color(0xFFFFCA28)),
                center = aPos,
                radius = aRadius
            ),
            radius = aRadius,
            center = aPos
        )

        // Star glint on alternate bulbs when bright
        if (aIntensity > 0.82f && aIdx % 3 == 0) {
            val gR = 3.2.dp.toPx()
            drawLine(
                color = Color(0xFFFFFDE7).copy(alpha = 0.85f),
                start = Offset(aPos.x - gR, aPos.y),
                end = Offset(aPos.x + gR, aPos.y),
                strokeWidth = 0.75.dp.toPx(),
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color(0xFFFFFDE7).copy(alpha = 0.85f),
                start = Offset(aPos.x, aPos.y - gR),
                end = Offset(aPos.x, aPos.y + gR),
                strokeWidth = 0.75.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}

