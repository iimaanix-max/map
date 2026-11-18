package com.tasbih.digitaltasbih.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.ui.theme.GoldAccent

/**
 * Premium Floating / Hover Translation Audio Player Bar.
 * Slides up smoothly at the bottom of the Quran reader screen when any translation text is tapped.
 * Controls: Play / Pause, Stop, Close (✕).
 */
@Composable
fun QuranTranslationFloatingBar(
    visible: Boolean,
    surahNumber: Int,
    ayahNumber: Int,
    langCode: String,
    voiceName: String,
    isPlaying: Boolean,
    isLoading: Boolean,
    progressFraction: Float,
    isNightMode: Boolean = false,
    onPlayPause: () -> Unit,
    onStop: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { it + 60 }) + fadeIn(tween(250)),
        exit = slideOutVertically(targetOffsetY = { it + 60 }) + fadeOut(tween(200)),
        modifier = modifier
    ) {
        val infiniteTransition = rememberInfiniteTransition(label = "speakerPulse")
        val pulseScale by infiniteTransition.animateFloat(
            initialValue = 0.92f,
            targetValue = 1.08f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 650, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse"
        )

        val isEnglish = (langCode == "en")
        val langLabel = when {
            isEnglish -> "English Translation"
            langCode == "hi" -> "हिंदी अनुवाद (Translation)"
            langCode == "ur" -> "اردو ترجمہ (Tarjuma)"
            else -> "Translation Audio"
        }

        val barBg = Brush.horizontalGradient(
            if (isNightMode) {
                listOf(Color(0xFF06140F), Color(0xFF0A221A), Color(0xFF06140F))
            } else {
                listOf(Color(0xFF092920), Color(0xFF0D4A3E), Color(0xFF071F18))
            }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp)
                .shadow(12.dp, RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .background(barBg)
                .border(1.2.dp, Color(0xFFF3D48A).copy(alpha = 0.85f), RoundedCornerShape(24.dp))
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // 1. LEFT ICON: Pulsing Speaker Badge
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .scale(if (isPlaying) pulseScale else 1f)
                            .clip(CircleShape)
                            .background(Color(0xFFF3D48A).copy(alpha = 0.2f))
                            .border(1.dp, Color(0xFFF3D48A).copy(alpha = 0.6f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Speaker",
                            tint = Color(0xFFF3D48A),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // 2. CENTER: Ayah Info & Voice Name
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Ayah $ayahNumber · $langLabel",
                            fontSize = 12.8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "🎙️ $voiceName",
                            fontSize = 10.8.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFF3D48A),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // 3. RIGHT CONTROLS: Play/Pause, Stop, Close (✕)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Play / Pause Button (Gold Circle)
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .shadow(3.dp, CircleShape)
                                .clip(CircleShape)
                                .background(Color(0xFFF3D48A))
                                .clickable(onClick = onPlayPause),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    color = Color(0xFF0D4A3E),
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(18.dp)
                                )
                            } else {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = if (isPlaying) "Pause" else "Play",
                                    tint = Color(0xFF0D4A3E),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        // Stop Button (⏹)
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.12f))
                                .clickable(onClick = onStop),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stop,
                                contentDescription = "Stop",
                                tint = Color(0xFFF1F5F9),
                                modifier = Modifier.size(17.dp)
                            )
                        }

                        // Close Button (✕)
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.12f))
                                .clickable(onClick = onDismiss),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color(0xFFCBD5E1),
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }
                }

                // 4. Subtle Bottom Audio Progress Track
                if (progressFraction > 0f) {
                    Spacer(modifier = Modifier.height(7.dp))
                    LinearProgressIndicator(
                        progress = { progressFraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.5.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = Color(0xFFF3D48A),
                        trackColor = Color.White.copy(alpha = 0.15f)
                    )
                }
            }
        }
    }
}
