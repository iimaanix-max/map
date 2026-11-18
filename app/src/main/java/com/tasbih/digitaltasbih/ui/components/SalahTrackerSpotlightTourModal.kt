package com.tasbih.digitaltasbih.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.data.SalahTrackerLocalization

@Composable
fun SalahTrackerSpotlightTourModal(
    step: Int, // 1: Today's Prayers, 2: Monthly Progress Chart
    totalSteps: Int = 2,
    lang: String = "hi",
    onNext: () -> Unit,
    onDone: () -> Unit,
    onSkip: () -> Unit
) {
    val strings = SalahTrackerLocalization.get(lang)
    val stepLabel = strings.tourStep.replace("{curr}", "$step").replace("{total}", "$totalSteps")

    val title = if (step == 1) strings.tourTodayTitle else strings.tourChartTitle
    val desc = if (step == 1) strings.tourTodayDesc else strings.tourChartDesc

    // Breathing pulse for the gold highlight border
    val infiniteTransition = rememberInfiniteTransition(label = "salahTourPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "salahTourPulseScale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.65f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "salahTourPulseAlpha"
    )

    AnimatedVisibility(
        visible = true,
        enter = fadeIn(tween(250)),
        exit = fadeOut(tween(200))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.72f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { /* Consume taps outside */ },
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Gold glowing spotlight frame card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 20.dp,
                            shape = RoundedCornerShape(20.dp),
                            ambientColor = Color(0xFFC9A84C),
                            spotColor = Color(0xFFC9A84C).copy(alpha = pulseAlpha)
                        )
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFF0C4A3A),
                                    Color(0xFF072C22),
                                    Color(0xFF031812)
                                )
                            )
                        )
                        .border(
                            width = 1.6.dp,
                            color = Color(0xFFF3D48A).copy(alpha = pulseAlpha),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(18.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Top bar: Step pill badge on left, Skip on right
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(Color(0xFFF3D48A).copy(alpha = 0.16f))
                                    .border(1.dp, Color(0xFFF3D48A).copy(alpha = 0.5f), RoundedCornerShape(50.dp))
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "✨ $stepLabel",
                                    color = Color(0xFFF3D48A),
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Text(
                                text = strings.tourSkip,
                                color = Color(0xFFF3D48A),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .clickable { onSkip() }
                                    .padding(4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Tour Title (Left aligned with emoji icon)
                        Text(
                            text = if (step == 1) "☀️ $title" else "📈 $title",
                            color = Color.White,
                            fontSize = 16.5.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Tour Description (Left aligned)
                        Text(
                            text = desc,
                            color = Color(0xFFF5EEDB),
                            fontSize = 13.sp,
                            lineHeight = 19.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Hairline divider
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color(0xFFC9A84C).copy(alpha = 0.25f))
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Bottom Row: Pagination dots on left, Glowing Gold Pill on right
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Dots indicator: pill for active, circle for inactive
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                for (i in 1..totalSteps) {
                                    val isActive = (i == step)
                                    Box(
                                        modifier = Modifier
                                            .size(if (isActive) 22.dp else 6.dp, 6.dp)
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(
                                                if (isActive) Color(0xFFF3D48A) else Color.White.copy(alpha = 0.35f)
                                            )
                                    )
                                }
                            }

                            // Glowing Amber Gold Pill button
                            Box(
                                modifier = Modifier
                                    .shadow(8.dp, RoundedCornerShape(50.dp), ambientColor = Color(0xFFDFAB3E), spotColor = Color(0xFFDFAB3E))
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(
                                                Color(0xFFF3CF7A),
                                                Color(0xFFDFAB3E)
                                            )
                                        )
                                    )
                                    .clickable {
                                        if (step < totalSteps) onNext() else onDone()
                                    }
                                    .padding(horizontal = 22.dp, vertical = 9.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (step < totalSteps) "${strings.tourNext} ➔" else "${strings.tourDone} ✓",
                                    color = Color(0xFF1C1408),
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                }

                // Directional gold arrow pointer
                if (step == 1) {
                    Text(
                        text = "▲",
                        color = Color(0xFFF3D48A),
                        fontSize = 20.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                } else {
                    Text(
                        text = "▼",
                        color = Color(0xFFF3D48A),
                        fontSize = 20.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}
