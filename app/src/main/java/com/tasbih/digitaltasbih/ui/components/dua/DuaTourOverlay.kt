package com.tasbih.digitaltasbih.ui.components.dua

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.ui.theme.DuaColors

data class TourStepData(
    val step: Int,
    val emoji: String,
    val title: String,
    val description: String,
    val isBottomTarget: Boolean = false
)

@Composable
fun DuaTourOverlay(
    currentStep: Int, // 1 to 5
    onNext: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
) {
    val steps = listOf(
        TourStepData(
            step = 1,
            emoji = "⚡",
            title = "Quick Access",
            description = "Quickly access daily essential Duas for morning, evening, after prayers, sleeping, and travel with a single tap."
        ),
        TourStepData(
            step = 2,
            emoji = "🤲",
            title = "How are you feeling?",
            description = "Find heartfelt Duas tailored for your current emotions and life situations like anxiety, sadness, gratitude, or seeking patience."
        ),
        TourStepData(
            step = 3,
            emoji = "📚",
            title = "Browse Categories",
            description = "Explore comprehensive Islamic Dua categories including Quranic Duas, Prophets prayers, healing, forgiveness, and daily life."
        ),
        TourStepData(
            step = 4,
            emoji = "📝",
            title = "My Dua",
            description = "Write, save, and keep your own personal heartfelt Duas in your own words in this private space.",
            isBottomTarget = true
        ),
        TourStepData(
            step = 5,
            emoji = "📁",
            title = "My Lists",
            description = "Create custom Dua collections and organize your favorite bookmarked Duas into personalized lists.",
            isBottomTarget = true
        )
    )

    val stepIndex = (currentStep - 1).coerceIn(0, steps.size - 1)
    val stepData = steps[stepIndex]

    // Dark scrim layer
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xB3000000))
            .clickable { /* consume clicks */ },
        contentAlignment = if (stepData.isBottomTarget) Alignment.BottomCenter else Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = if (stepData.isBottomTarget) 90.dp else 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tooltip Card matching Screenshot 1-7
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(18.dp, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .background(DuaColors.TourGradient)
                    .border(1.5.dp, Color(0x99F3D48A), RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Top: Step X of 5 badge on left, Skip on right
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0x22F3D48A))
                                .border(1.dp, Color(0x66F3D48A), RoundedCornerShape(20.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "✨ Step ${stepData.step} of 5",
                                color = Color(0xFFF3D48A),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = "Skip",
                            color = Color(0xFFF3D48A),
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { onSkip() }
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Title
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "${stepData.emoji} ", fontSize = 18.sp)
                        Text(
                            text = stepData.title,
                            color = Color.White,
                            fontSize = 17.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Description
                    Text(
                        text = stepData.description,
                        color = Color(0xE6FFFFFF),
                        fontSize = 13.5.sp,
                        lineHeight = 19.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color(0x26FFFFFF), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(14.dp))

                    // Bottom Row: dots on left, Next / Got it on right
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 5 indicator dots
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            for (i in 1..5) {
                                val isCurrent = i == stepData.step
                                Box(
                                    modifier = Modifier
                                        .size(if (isCurrent) 18.dp else 8.dp, 8.dp)
                                        .clip(CircleShape)
                                        .background(if (isCurrent) Color(0xFFF3D48A) else Color(0x55FFFFFF))
                                )
                            }
                        }

                        // Next button / Got it button
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFFE5C578))
                                .clickable { onNext() }
                                .padding(horizontal = 22.dp, vertical = 9.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (stepData.step == 5) "Got it! ✓" else "Next →",
                                color = Color(0xFF144D3D),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Arrow indicator
            if (stepData.isBottomTarget) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "⬇️",
                    fontSize = 24.sp
                )
            }
        }
    }
}