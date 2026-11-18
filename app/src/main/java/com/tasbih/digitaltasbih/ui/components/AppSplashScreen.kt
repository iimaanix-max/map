package com.tasbih.digitaltasbih.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * AppSplashScreen
 * Exact replica of the original digital tasbih HTML splash screen:
 * - Linear dark luxury gradient: 160deg #0a0c10 -> #111520 -> #0d1520
 * - 📿 Tasbih bead icon with scale (0.5 -> 1.0) and rotation (-20deg -> 0deg) entry
 * - "Digital Tasbih" in rich antique gold #c9a84c (Cinzel/Serif, letter-spacing 2px)
 * - "اَلذِّكْرُ — اللّٰه" in radiant luminous gold #f0d080
 * - 3 pulsating gold dots with staggered timing (sp-dot)
 * - "صَلَّى اللّٰهُ عَلَيْهِ وَسَلَّمَ" blessing in muted slate #6a7090
 */
@Composable
fun AppSplashScreen(
    onDismiss: () -> Unit
) {
    val animScale = remember { Animatable(0.7f) }
    val animRotate = remember { Animatable(-15f) }
    val animAlpha = remember { Animatable(0.4f) }

    LaunchedEffect(Unit) {
        launch {
            animScale.animateTo(1f, tween(durationMillis = 700, easing = EaseOut))
        }
        launch {
            animRotate.animateTo(0f, tween(durationMillis = 700, easing = EaseOut))
        }
        launch {
            animAlpha.animateTo(1f, tween(durationMillis = 500, easing = EaseOut))
        }
        // Total splash display duration matching original app launch timing
        delay(1900L)
        onDismiss()
    }

    val infiniteTransition = rememberInfiniteTransition(label = "splashDots")

    // 3 Dots Pulsing Animation
    val dot1Scale by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot1Scale"
    )
    val dot1Alpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot1Alpha"
    )

    val dot2Scale by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 200, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot2Scale"
    )
    val dot2Alpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 200, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot2Alpha"
    )

    val dot3Scale by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 400, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot3Scale"
    )
    val dot3Alpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 400, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot3Alpha"
    )

    val splashBg = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0A0C10),
            Color(0xFF111520),
            Color(0xFF0D1520)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(splashBg),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Animated Icon: 📿
            Text(
                text = "📿",
                fontSize = 64.sp,
                modifier = Modifier
                    .scale(animScale.value)
                    .rotate(animRotate.value)
                    .alpha(animAlpha.value)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // English Title: Digital Tasbih
            Text(
                text = "Digital Tasbih",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                letterSpacing = 2.sp,
                color = Color(0xFFC9A84C),
                modifier = Modifier.alpha(animAlpha.value)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Arabic Subtitle: اَلذِّكْرُ — اللّٰه
            Text(
                text = "اَلذِّكْرُ — اللّٰه",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = Color(0xFFF0D080),
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(animAlpha.value)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // 3 Staggered Pulsing Gold Dots
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .scale(dot1Scale)
                        .alpha(dot1Alpha)
                        .clip(CircleShape)
                        .background(Color(0xFFC9A84C))
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .scale(dot2Scale)
                        .alpha(dot2Alpha)
                        .clip(CircleShape)
                        .background(Color(0xFFC9A84C))
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .scale(dot3Scale)
                        .alpha(dot3Alpha)
                        .clip(CircleShape)
                        .background(Color(0xFFC9A84C))
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Bottom blessing: صَلَّى اللّٰهُ عَلَيْهِ وَسَلَّمَ
            Text(
                text = "صَلَّى اللّٰهُ عَلَيْهِ وَسَلَّمَ",
                fontSize = 13.sp,
                letterSpacing = 1.sp,
                color = Color(0xFF6A7090),
                modifier = Modifier.alpha(animAlpha.value)
            )
        }
    }
}
