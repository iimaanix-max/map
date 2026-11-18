package com.tasbih.digitaltasbih.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Ultra-Smooth Master Spotlight Tour Modal with physics-driven spring morphing,
 * graceful entry/exit dissolve transitions, and liquid layout adaptation.
 */
@Composable
fun GeneralSpotlightTourModal(
    step: Int,
    totalSteps: Int,
    stepLabel: String,
    title: String,
    description: String,
    nextBtnText: String,
    doneBtnText: String,
    skipBtnText: String,
    targetRect: Rect?,
    cornerRadiusDp: Float = 16f,
    padDp: Float = 6f,
    onNext: () -> Unit,
    onDone: () -> Unit,
    onSkip: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    var isExiting by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        isVisible = true
    }

    val modalAlpha by animateFloatAsState(
        targetValue = if (isVisible && !isExiting) 1f else 0f,
        animationSpec = tween(durationMillis = 260, easing = FastOutSlowInEasing),
        label = "modalAlpha"
    )

    fun handleExit(action: () -> Unit) {
        if (!isExiting) {
            isExiting = true
            coroutineScope.launch {
                delay(240L)
                action()
            }
        }
    }

    // Gold pulse animation for spotlight ring
    val infiniteTransition = rememberInfiniteTransition(label = "spotlightPulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.55f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    // Bouncing animation for pointer arrow
    val bounceOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(650, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounceOffset"
    )

    // Cache last valid rect so during scrolling / step transitions the spotlight doesn't collapse to 0
    var lastValidRect by remember { mutableStateOf<Rect?>(null) }
    if (targetRect != null && targetRect.width > 0f && targetRect.height > 0f) {
        lastValidRect = targetRect
    }
    val effectiveRect = targetRect ?: lastValidRect

    var initialRectCaptured by remember { mutableStateOf(false) }

    val animLeft = remember { Animatable(0f) }
    val animTop = remember { Animatable(0f) }
    val animRight = remember { Animatable(0f) }
    val animBottom = remember { Animatable(0f) }
    val animCornerRadiusDp = remember { Animatable(cornerRadiusDp) }

    val morphSpec = spring<Float>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMediumLow
    )

    LaunchedEffect(effectiveRect, cornerRadiusDp) {
        if (effectiveRect != null && effectiveRect.width > 0f && effectiveRect.height > 0f) {
            if (!initialRectCaptured) {
                // Immediate snap on first detection - completely eliminates (0,0) flying glitch
                animLeft.snapTo(effectiveRect.left)
                animTop.snapTo(effectiveRect.top)
                animRight.snapTo(effectiveRect.right)
                animBottom.snapTo(effectiveRect.bottom)
                animCornerRadiusDp.snapTo(cornerRadiusDp)
                initialRectCaptured = true
            } else {
                // Fluid spring morph between targets
                launch { animLeft.animateTo(effectiveRect.left, morphSpec) }
                launch { animTop.animateTo(effectiveRect.top, morphSpec) }
                launch { animRight.animateTo(effectiveRect.right, morphSpec) }
                launch { animBottom.animateTo(effectiveRect.bottom, morphSpec) }
                launch { animCornerRadiusDp.animateTo(cornerRadiusDp, morphSpec) }
            }
        }
    }

    val density = LocalDensity.current
    val padPx = with(density) { padDp.dp.toPx() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(modalAlpha)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { /* Consume background taps */ }
    ) {
        // 1. Full-screen Spotlight Cutout Canvas
        val cornerRadiusPx = with(density) { animCornerRadiusDp.value.dp.toPx() }
        Canvas(modifier = Modifier.fillMaxSize()) {
            val fullPath = Path().apply {
                addRect(Rect(0f, 0f, size.width, size.height))
            }

            if (initialRectCaptured && animRight.value > animLeft.value && animBottom.value > animTop.value) {
                val spotlightRect = Rect(
                    left = (animLeft.value - padPx).coerceAtLeast(0f),
                    top = (animTop.value - padPx).coerceAtLeast(0f),
                    right = (animRight.value + padPx).coerceAtMost(size.width),
                    bottom = (animBottom.value + padPx).coerceAtMost(size.height)
                )

                val cutoutPath = Path().apply {
                    addRoundRect(RoundRect(spotlightRect, CornerRadius(cornerRadiusPx, cornerRadiusPx)))
                }

                // Dark background with cutout hole showing REAL element underneath
                val darkenedPath = Path.combine(PathOperation.Difference, fullPath, cutoutPath)
                drawPath(darkenedPath, color = Color(0xD9030E0B))

                // Pulsing glowing golden border around the real element
                drawRoundRect(
                    color = Color(0xFFF3D48A).copy(alpha = pulseAlpha),
                    topLeft = Offset(spotlightRect.left, spotlightRect.top),
                    size = Size(spotlightRect.width, spotlightRect.height),
                    cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
                    style = Stroke(width = 2.5f * density.density)
                )
            } else {
                drawPath(fullPath, color = Color(0xD9030E0B))
            }
        }

        // 2. Guide Tooltip Card + Fluid Animated Positioning
        val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp
        val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp

        val targetTopDp = with(density) { animTop.value.toDp() }
        val targetBottomDp = with(density) { animBottom.value.toDp() }
        val targetCenterXDp = with(density) { ((animLeft.value + animRight.value) / 2f).toDp() }

        // Position: if target is in bottom half (> 45% screen height), place card above it
        val isAbove = initialRectCaptured && targetTopDp > (screenHeightDp * 0.45f)

        val targetCardTop = if (isAbove) {
            (targetTopDp - 265.dp).coerceIn(20.dp, (screenHeightDp - 280.dp).coerceAtLeast(20.dp))
        } else {
            (targetBottomDp + 12.dp).coerceIn(20.dp, (screenHeightDp - 280.dp).coerceAtLeast(20.dp))
        }

        val animCardTop by animateDpAsState(
            targetValue = targetCardTop,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMediumLow
            ),
            label = "animCardTop"
        )

        val targetArrowX = (targetCenterXDp - (screenWidthDp / 2f)).coerceIn(-120.dp, 120.dp)
        val animArrowX by animateDpAsState(
            targetValue = targetArrowX,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMediumLow
            ),
            label = "animArrowX"
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .offset(y = animCardTop),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // When card is BELOW the target, arrow appears at top of card pointing UP
                AnimatedVisibility(
                    visible = !isAbove && initialRectCaptured,
                    enter = fadeIn(tween(180)) + expandVertically(tween(180)),
                    exit = fadeOut(tween(180)) + shrinkVertically(tween(180))
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .offset(x = animArrowX, y = (-bounceOffset).dp)
                                .size(28.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFF3D48A))
                                .border(1.dp, Color(0xFFCA8A04), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "⬆",
                                color = Color(0xFF14532D),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                // The Persistent Tooltip Card
                GeneralTourCardContent(
                    stepLabel = stepLabel,
                    skipBtnText = skipBtnText,
                    title = title,
                    description = description,
                    pulseAlpha = pulseAlpha,
                    step = step,
                    totalSteps = totalSteps,
                    doneBtnText = doneBtnText,
                    nextBtnText = nextBtnText,
                    onSkip = { handleExit(onSkip) },
                    onAction = {
                        if (step >= totalSteps) {
                            handleExit(onDone)
                        } else {
                            onNext()
                        }
                    }
                )

                // When card is ABOVE the target, arrow appears at bottom of card pointing DOWN
                AnimatedVisibility(
                    visible = isAbove && initialRectCaptured,
                    enter = fadeIn(tween(180)) + expandVertically(tween(180)),
                    exit = fadeOut(tween(180)) + shrinkVertically(tween(180))
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .offset(x = animArrowX, y = bounceOffset.dp)
                                .size(28.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFF3D48A))
                                .border(1.dp, Color(0xFFCA8A04), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "⬇",
                                color = Color(0xFF14532D),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GeneralTourCardContent(
    stepLabel: String,
    skipBtnText: String,
    title: String,
    description: String,
    pulseAlpha: Float,
    step: Int,
    totalSteps: Int,
    doneBtnText: String,
    nextBtnText: String,
    onSkip: () -> Unit,
    onAction: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 280,
                    easing = FastOutSlowInEasing
                )
            )
            .shadow(
                elevation = 24.dp,
                shape = RoundedCornerShape(22.dp),
                spotColor = Color(0xFFF3D48A),
                ambientColor = Color.Black
            )
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0E4435),
                        Color(0xFF082D23),
                        Color(0xFF051E17)
                    )
                )
            )
            .border(
                width = 1.5.dp,
                color = Color(0xFFF3D48A).copy(alpha = pulseAlpha),
                shape = RoundedCornerShape(22.dp)
            )
            .padding(20.dp)
    ) {
        // Top Row: Step Badge + Skip
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x33F3D48A))
                    .border(1.dp, Color(0x66F3D48A), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "✨ $stepLabel",
                    color = Color(0xFFF3D48A),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = skipBtnText,
                color = Color(0xFF94A3B8),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .clickable(onClick = onSkip)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Title & Description with silky Crossfade
        Crossfade(
            targetState = step,
            animationSpec = tween(250, easing = FastOutSlowInEasing),
            label = "textCrossfade"
        ) { _ ->
            Column {
                Text(
                    text = title,
                    color = Color(0xFFF3D48A),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = description,
                    color = Color(0xFFE2E8F0),
                    fontSize = 13.5.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Bottom Row: Pagination dots + Tactile Primary Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (totalSteps > 1) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(totalSteps) { idx ->
                        val isCurr = idx + 1 == step
                        val dotWidth by animateDpAsState(
                            targetValue = if (isCurr) 18.dp else 6.dp,
                            animationSpec = tween(280, easing = FastOutSlowInEasing),
                            label = "dotWidth"
                        )
                        Box(
                            modifier = Modifier
                                .height(6.dp)
                                .width(dotWidth)
                                .clip(CircleShape)
                                .background(if (isCurr) Color(0xFFF3D48A) else Color(0x44F3D48A))
                        )
                    }
                }
            } else {
                Spacer(modifier = Modifier.width(4.dp))
            }

            var isPressed by remember { mutableStateOf(false) }
            val btnScale by animateFloatAsState(
                targetValue = if (isPressed) 0.94f else 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "btnScale"
            )
            val coroutineScope = rememberCoroutineScope()

            Box(
                modifier = Modifier
                    .scale(btnScale)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color(0xFFF3D48A), Color(0xFFD8A846))
                        )
                    )
                    .clickable {
                        if (!isPressed) {
                            isPressed = true
                            coroutineScope.launch {
                                delay(90L)
                                isPressed = false
                                onAction()
                            }
                        }
                    }
                    .padding(horizontal = 22.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (step >= totalSteps) doneBtnText else nextBtnText,
                    color = Color(0xFF0C382B),
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
