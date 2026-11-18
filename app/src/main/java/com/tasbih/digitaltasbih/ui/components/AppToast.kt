package com.tasbih.digitaltasbih.ui.components

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * Global Luxury App Toast Notification State
 */
data class AppToastData(
    val message: String,
    val id: Long = System.currentTimeMillis()
)

object AppToast {
    var currentToast by mutableStateOf<AppToastData?>(null)
        private set

    fun show(message: String) {
        currentToast = AppToastData(message = message, id = System.currentTimeMillis())
    }

    fun dismiss() {
        currentToast = null
    }
}

/**
 * Extension for Context to easily replace standard Toast.makeText across the app
 */
fun Context.showAppToast(message: String) {
    AppToast.show(message)
}

/**
 * Global Host composable for the luxury in-app Toast Pill.
 * Placed in AppRootNavigation to float seamlessly on top of ANY screen.
 */
@Composable
fun AppToastHost(
    modifier: Modifier = Modifier
) {
    val toastData = AppToast.currentToast
    val haptic = LocalHapticFeedback.current

    LaunchedEffect(toastData?.id) {
        if (toastData != null) {
            try {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            } catch (_: Exception) {}
            delay(2800L)
            AppToast.dismiss()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 78.dp, start = 20.dp, end = 20.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        AnimatedVisibility(
            visible = toastData != null,
            enter = slideInVertically(
                initialOffsetY = { 80 },
                animationSpec = spring(dampingRatio = 0.72f, stiffness = Spring.StiffnessMediumLow)
            ) + fadeIn(animationSpec = tween(220)) + scaleIn(initialScale = 0.88f, animationSpec = tween(220)),
            exit = slideOutVertically(
                targetOffsetY = { 60 },
                animationSpec = tween(180)
            ) + fadeOut(animationSpec = tween(180)) + scaleOut(targetScale = 0.90f, animationSpec = tween(180))
        ) {
            if (toastData != null) {
                LuxuryToastPill(
                    message = toastData.message,
                    onDismiss = { AppToast.dismiss() }
                )
            }
        }
    }
}

/**
 * Ultra-premium Emerald & Royal Gold Floating Pill (No Android Robot!)
 */
@Composable
private fun LuxuryToastPill(
    message: String,
    onDismiss: () -> Unit
) {
    // Separate leading emoji if present for a clean badge layout
    val firstChar = message.trim().take(2)
    val hasLeadingEmoji = message.isNotEmpty() && (
        message.startsWith("🎉") || message.startsWith("✓") ||
        message.startsWith("💾") || message.startsWith("❤️") ||
        message.startsWith("🌙") || message.startsWith("☀️") ||
        message.startsWith("🌟") || message.startsWith("🔔") ||
        message.startsWith("🕌") || message.startsWith("✨")
    )

    val iconText = if (hasLeadingEmoji) firstChar else "✦"
    val cleanText = if (hasLeadingEmoji) message.trim().drop(2).trim() else message

    Surface(
        modifier = Modifier
            .widthIn(min = 200.dp, max = 340.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(32.dp),
                spotColor = Color(0x99000000),
                ambientColor = Color(0x40C9A84C)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onDismiss() },
        shape = RoundedCornerShape(32.dp),
        color = Color.Transparent,
        border = BorderStroke(
            width = 1.2.dp,
            brush = Brush.horizontalGradient(
                listOf(
                    Color(0xFFE8CF7A),
                    Color(0xFFC9A84C),
                    Color(0xFF9E7C30)
                )
            )
        )
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            Color(0xFF09241B),
                            Color(0xFF134233),
                            Color(0xFF0C2B20)
                        )
                    )
                )
                .padding(horizontal = 16.dp, vertical = 11.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Royal Gold Coin Badge with Emoji/Islamic Icon (No Android Robot!)
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(
                            brush = Brush.radialGradient(
                                listOf(
                                    Color(0xFF1E5442),
                                    Color(0xFF0A261D)
                                )
                            ),
                            shape = CircleShape
                        )
                        .border(1.dp, Color(0xFFC9A84C).copy(alpha = 0.85f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = iconText,
                        fontSize = if (hasLeadingEmoji) 14.sp else 12.sp,
                        color = Color(0xFFFFDF78),
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Premium crisp typography
                Text(
                    text = cleanText,
                    color = Color(0xFFFFFBF2),
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
