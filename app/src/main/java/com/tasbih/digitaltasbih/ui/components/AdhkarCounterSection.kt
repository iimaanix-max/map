package com.tasbih.digitaltasbih.ui.components

import androidx.compose.ui.zIndex
import com.tasbih.digitaltasbih.ui.screens.tasbih.RealisticBeadsCanvas
import com.tasbih.digitaltasbih.ui.screens.tasbih.MarbleGalleryModal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.data.BeadMarbleSkin
import com.tasbih.digitaltasbih.data.TasbihRepository
import com.tasbih.digitaltasbih.util.TasbihAudioEngine
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * Ripple animation item for the circular counter dial.
 */
data class AdhkarDialRipple(
    val id: Long,
    val px: Float,
    val py: Float,
    val startTime: Long
)

/**
 * AdhkarCounterSection - Master Counter Component for Morning & Evening Adhkar.
 * Built to be 100% identical in physics, audio, ripples, gold pulse, and controls to Digital Tasbih.
 */
@Composable
fun AdhkarCounterSection(
    curCount: Int,
    targetCount: Int,
    isDone: Boolean,
    isNightMode: Boolean = false,
    tapToCountLabel: String = "Tap to Count",
    undoLabel: String = "Undo",
    resetLabel: String = "Reset",
    markCompleteLabel: String = "Mark Complete",
    completedLabel: String = "Completed",
    nextLabel: String = "Next Adhkar",
    onIncrement: () -> Unit,
    onUndo: () -> Unit,
    onReset: () -> Unit,
    onToggleComplete: () -> Unit,
    onNext: () -> Unit
) {
    var isBeadsViewMode by remember { mutableStateOf(false) }
    var activeSkin by remember { mutableStateOf(TasbihRepository.getActiveBeadSkin()) }
    var showMarbleGalleryModal by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Central Counter Area: Circular Dial or Realistic Beads (100% Tasbih Parity)
        if (!isBeadsViewMode) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AdhkarCircularCounterDial(
                    sessionCount = curCount,
                    targetCount = targetCount,
                    isNightMode = isNightMode,
                    titleText = tapToCountLabel,
                    onTap = onIncrement
                )

                // Floating Beads Switch Button on Top-Left (Exact Tasbih)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .zIndex(10f)
                        .padding(start = 12.dp, top = 8.dp)
                        .size(38.dp)
                        .shadow(3.dp, CircleShape)
                        .clip(CircleShape)
                        .background(if (isNightMode) Color(0xFF1B2F25) else Color.White)
                        .border(1.2.dp, Color(0x44C9A84C), CircleShape)
                        .clickable { isBeadsViewMode = true },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "📿",
                        fontSize = 18.sp
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    RealisticBeadsCanvas(
                        skin = activeSkin,
                        sessionCount = curCount,
                        targetCount = targetCount,
                        isNightMode = isNightMode,
                        onToggleBeadsMode = { isBeadsViewMode = false },
                        onOpenMarbleGallery = { showMarbleGalleryModal = true },
                        onBeadAdvancement = onIncrement
                    )

                    // Floating Dial Switch Button on Top-Left (⭕ Exact Tasbih)
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .zIndex(10f)
                            .padding(start = 12.dp, top = 8.dp)
                            .size(38.dp)
                            .shadow(3.dp, CircleShape)
                            .clip(CircleShape)
                            .background(Color(0xFF0E5D47))
                            .border(1.2.dp, Color(0xFFC9A84C), CircleShape)
                            .clickable { isBeadsViewMode = false },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "⭕",
                            fontSize = 16.sp
                        )
                    }
                }

                // Central Session Count Number in Serif Gold directly below bead arc
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$curCount",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isNightMode) Color(0xFFF3D48A) else Color(0xFFA07828),
                        fontFamily = FontFamily.Serif
                    )
                }

                // Colours & Marbles Pill Button (Exact Tasbih)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val baseCol = remember(activeSkin.baseColor) {
                        try { Color(android.graphics.Color.parseColor(activeSkin.baseColor)) } catch (_: Exception) { Color(0xFF825F2D) }
                    }
                    val highlightCol = remember(activeSkin.highlightColor) {
                        try { Color(android.graphics.Color.parseColor(activeSkin.highlightColor)) } catch (_: Exception) { Color(0xFFF3D48A) }
                    }
                    val shadowCol = remember(activeSkin.shadowColor) {
                        try { Color(android.graphics.Color.parseColor(activeSkin.shadowColor)) } catch (_: Exception) { Color(0xFF4A3519) }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(if (isNightMode) Color(0xFF112D22) else Color(0xFFF8F5EE))
                            .border(1.5.dp, Color(0xFFC9A84C).copy(alpha = 0.5f), RoundedCornerShape(999.dp))
                            .clickable { showMarbleGalleryModal = true }
                            .padding(horizontal = 18.dp, vertical = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            colors = listOf(highlightCol, baseCol, shadowCol),
                                            center = Offset(10f, 10f)
                                        )
                                    )
                                    .border(1.dp, Color(0x66C9A84C), CircleShape)
                            )
                            Text(
                                text = "Colours & Marbles",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isNightMode) Color(0xFFEDEDED) else Color(0xFF1C2826)
                            )
                        }
                    }
                }
            }
        }

        // ── Action Controls Row: [ ⇄ Undo ] [ ↺ Reset ] [ ✓ Complete ] (Matching Tasbih Image 1) ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // 1. Undo Button (White/Night bg, Emerald #0E5D47 border & text, 16dp rounded)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .shadow(1.dp, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isNightMode) Color(0xFF1B2B23) else Color.White)
                    .border(
                        1.2.dp,
                        if (isNightMode) Color(0x6634D399) else Color(0x66C9A84C),
                        RoundedCornerShape(16.dp)
                    )
                    .clickable { onUndo() },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "⇄",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isNightMode) Color(0xFF34D399) else Color(0xFF0E5D47)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = undoLabel,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.5.sp,
                        color = if (isNightMode) Color(0xFF34D399) else Color(0xFF0E5D47)
                    )
                }
            }

            // 2. Reset Button (White/Night bg, Red #C81D3F border & text, 16dp rounded)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .shadow(1.dp, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isNightMode) Color(0xFF1B2B23) else Color.White)
                    .border(
                        1.2.dp,
                        Color(0x66C81D3F),
                        RoundedCornerShape(16.dp)
                    )
                    .clickable { onReset() },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "↺",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFC81D3F)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = resetLabel,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.5.sp,
                        color = Color(0xFFC81D3F)
                    )
                }
            }

            // 3. Mark Complete / Save Button (Matching Tasbih Save action)
            Box(
                modifier = Modifier
                    .weight(1.1f)
                    .height(44.dp)
                    .shadow(1.dp, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (isDone) {
                            if (isNightMode) Color(0xFF0E5D47) else Color(0xFF0C4A3A)
                        } else {
                            if (isNightMode) Color(0xFF16251E) else Color.White
                        }
                    )
                    .border(
                        1.2.dp,
                        if (isDone) Color(0xFFC9A84C) else Color(0x66104434),
                        RoundedCornerShape(16.dp)
                    )
                    .clickable { onToggleComplete() },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "✓",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDone) Color(0xFFF3D48A) else (if (isNightMode) Color(0xFFE8F5E9) else Color(0xFF104434))
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = if (isDone) "$completedLabel ✓" else markCompleteLabel,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (isDone) Color.White else (if (isNightMode) Color(0xFFE8F5E9) else Color(0xFF104434))
                    )
                }
            }
        }

        // 4. Next Adhkar Full-Width Button
        OutlinedButton(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(14.dp),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = Brush.linearGradient(
                    listOf(
                        if (isNightMode) Color(0xFF34D399) else Color(0xFF0C4A3A),
                        if (isNightMode) Color(0xFF10B981) else Color(0xFF0C4A3A)
                    )
                )
            )
        ) {
            val cleanNext = nextLabel.replace("➔", "").replace("->", "").trim()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = cleanNext,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isNightMode) Color(0xFF34D399) else Color(0xFF0C4A3A)
                )
                Text(
                    text = "➔",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isNightMode) Color(0xFF34D399) else Color(0xFF0C4A3A)
                )
            }
        }
    }

    if (showMarbleGalleryModal) {
        MarbleGalleryModal(
            activeSkinId = activeSkin.id,
            onSelectSkin = { chosenSkin ->
                activeSkin = chosenSkin
                TasbihRepository.setActiveBeadSkin(chosenSkin.id)
                isBeadsViewMode = true
                showMarbleGalleryModal = false
            },
            onDismiss = { showMarbleGalleryModal = false }
        )
    }
}

/**
 * Authentic Tasbih Circular Counter Dial.
 * Features 0.92f finger press scale, number bounce 1.18x, expanding ripple physics,
 * target reached gold pulse ring, Sandalwood click audio & completion chime!
 */
@Composable
fun AdhkarCircularCounterDial(
    sessionCount: Int,
    targetCount: Int,
    dialSize: Dp = 196.dp,
    isNightMode: Boolean = false,
    titleText: String = "Tap to Count",
    onTap: () -> Unit
) {
    val currentOnTap by rememberUpdatedState(onTap)
    val coroutineScope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current

    // Progress Arc animation (smooth 350ms easing)
    val targetProgress = if (targetCount > 0) {
        (sessionCount.toFloat() / targetCount.toFloat()).coerceIn(0f, 1f)
    } else 0f

    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
        label = "adhkarProgress"
    )

    // Button press scale (0.92f spring compression)
    var isPressed by remember { mutableStateOf(false) }
    val innerPressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessMedium),
        label = "adhkarPressScale"
    )

    // Number Bounce animation (1.18x at 40%, 180ms total)
    val numberScale = remember { Animatable(1f) }

    // Dynamic Expanding Ripples
    var ripples by remember { mutableStateOf(listOf<AdhkarDialRipple>()) }
    var frameTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(ripples.isNotEmpty()) {
        while (ripples.isNotEmpty()) {
            withFrameMillis { _ ->
                val now = System.currentTimeMillis()
                frameTime = now
                ripples = ripples.filter { now - it.startTime < 650L }
            }
        }
    }

    // Target Reached Golden Pulse Ring (450ms expanding aura)
    val goldPulseAnim = remember { Animatable(0f) }
    LaunchedEffect(sessionCount) {
        if (sessionCount > 0 && targetCount > 0 && sessionCount == targetCount) {
            goldPulseAnim.snapTo(0f)
            goldPulseAnim.animateTo(1f, animationSpec = tween(durationMillis = 450, easing = FastOutSlowInEasing))
            goldPulseAnim.snapTo(0f)
        }
    }

    fun triggerTapEffects() {
        // 1. Play authentic procedural audio
        if (sessionCount + 1 >= targetCount && targetCount > 0) {
            TasbihAudioEngine.playCompletionChime()
        } else {
            TasbihAudioEngine.playSandalwoodClick()
        }

        // 2. Haptic vibration
        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)

        // 3. Count callback
        currentOnTap()

        // 4. Number bounce
        coroutineScope.launch {
            numberScale.snapTo(1f)
            numberScale.animateTo(1.18f, animationSpec = tween(72, easing = FastOutSlowInEasing))
            numberScale.animateTo(1.0f, animationSpec = tween(108, easing = FastOutSlowInEasing))
        }

        // 5. Spawn realistic expanding ripple inside button
        val rx = Random.nextFloat() * 0.68f + 0.16f
        val ry = Random.nextFloat() * 0.68f + 0.16f
        val newRipple = AdhkarDialRipple(
            id = System.nanoTime(),
            px = rx,
            py = ry,
            startTime = System.currentTimeMillis()
        )
        ripples = ripples + newRipple
    }

    Box(
        modifier = Modifier
            .size(dialSize)
            .pointerInput(currentOnTap) {
                awaitEachGesture {
                    awaitFirstDown(requireUnconsumed = false)
                    isPressed = true
                    triggerTapEffects()
                    waitForUpOrCancellation()
                    isPressed = false
                }
            },
        contentAlignment = Alignment.Center
    ) {
        // 1. Target Complete Golden Aura Pulse
        if (goldPulseAnim.value > 0f) {
            val p = goldPulseAnim.value
            Canvas(modifier = Modifier.fillMaxSize()) {
                val centerOffset = Offset(size.width / 2f, size.height / 2f)
                val baseRadius = (size.minDimension - 16.dp.toPx()) / 2f
                val pulseSpread = 18.dp.toPx() * (p / 0.7f).coerceAtMost(1f)
                val alpha = (0.4f * (1f - p)).coerceIn(0f, 0.4f)
                drawCircle(
                    color = Color(0xFFC9A84C).copy(alpha = alpha),
                    radius = baseRadius + pulseSpread,
                    center = centerOffset,
                    style = Stroke(width = 3.dp.toPx())
                )
            }
        }

        // 2. Progress Ring Canvas (Track + Diagonal Gradient Arc)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 8.dp.toPx()
            val diameter = size.minDimension - strokeWidth
            val topLeft = Offset((size.width - diameter) / 2, (size.height - diameter) / 2)
            val arcSize = Size(diameter, diameter)

            // Background soft track
            drawArc(
                color = if (isNightMode) Color(0xFF18382B) else Color(0xFFC2D6CA),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth)
            )

            // Progress Arc with diagonal gradient
            if (animatedProgress > 0f) {
                drawArc(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFFC9A84C), Color(0xFF0E5D47)),
                        start = Offset(0f, 0f),
                        end = Offset(size.width, size.height)
                    ),
                    startAngle = -90f,
                    sweepAngle = animatedProgress * 360f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
        }

        // 3. Central Counting Button with Shadows, Gold Border & Dynamic Ripples
        Box(
            modifier = Modifier
                .size(dialSize - 16.dp)
                .scale(innerPressScale)
                .shadow(
                    elevation = 4.dp,
                    shape = CircleShape,
                    spotColor = if (isNightMode) Color(0x40000000) else Color(0x1F0E5D47),
                    ambientColor = Color(0x140E5D47)
                )
                .clip(CircleShape)
                .border(
                    width = if (isNightMode) 2.2.dp else 1.8.dp,
                    color = Color(0xFFC9A84C),
                    shape = CircleShape
                )
                .drawBehind {
                    // Button Face background
                    drawCircle(color = if (isNightMode) Color(0xFF16251E) else Color.White)

                    // Expanding green/emerald ripples
                    val baseS = 55.dp.toPx()
                    val maxScale = 4f
                    val duration = 550f

                    ripples.forEach { ripple ->
                        val elapsed = (frameTime - ripple.startTime).coerceIn(0L, 550L)
                        val rawP = elapsed / duration
                        val p = FastOutSlowInEasing.transform(rawP)
                        val currentRadius = (baseS / 2f) * (p * maxScale)
                        val alpha = (0.22f * (1f - p)).coerceIn(0f, 0.22f)

                        if (currentRadius > 0.5f && alpha > 0.005f) {
                            drawCircle(
                                color = Color(0xFF0E5D47).copy(alpha = alpha),
                                radius = currentRadius,
                                center = Offset(ripple.px * size.width, ripple.py * size.height)
                            )
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Bouncing Count Number
                Text(
                    text = "$sessionCount",
                    color = if (isNightMode) Color(0xFFFFF0C2) else Color(0xFF0E5D47),
                    fontSize = if (sessionCount >= 1000) 36.sp else 44.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    lineHeight = 46.sp,
                    modifier = Modifier.scale(numberScale.value)
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Target Pill / Instruction
                Text(
                    text = "/ $targetCount",
                    color = if (isNightMode) Color(0xFFD4AF37) else Color(0xFF8C7A5B),
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}
