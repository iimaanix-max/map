package com.tasbih.digitaltasbih.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tasbih.digitaltasbih.data.PrayerRepository

/**
 * Luxury Rate App on Play Store Dialog.
 * Matches original HTML showRatePrompt() with 5 interactive stars,
 * market:// deep link, web fallback, and graceful persistence.
 */
@Composable
fun RateAppDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var selectedRating by remember { mutableIntStateOf(5) }

    fun openPlayStore() {
        val packageName = context.packageName
        PrayerRepository.setAppRated(true)
        val marketUri = Uri.parse("market://details?id=$packageName")
        val marketIntent = Intent(Intent.ACTION_VIEW, marketUri).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        try {
            context.startActivity(marketIntent)
        } catch (_: Exception) {
            val webUri = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            val webIntent = Intent(Intent.ACTION_VIEW, webUri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            try {
                context.startActivity(webIntent)
            } catch (e: Exception) {
                Toast.makeText(context, "Could not open Play Store: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
        onDismiss()
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.78f))
                .clickable { onDismiss() }
                .padding(horizontal = 22.dp, vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable(enabled = false) {}
                    .shadow(32.dp, RoundedCornerShape(26.dp), spotColor = Color(0xFFF3D48A))
                    .clip(RoundedCornerShape(26.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF102820),
                                Color(0xFF0C1D17),
                                Color(0xFF05100C)
                            )
                        )
                    )
                    .border(1.5.dp, Color(0xFFD4AF37), RoundedCornerShape(26.dp))
                    .padding(22.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Close icon top-right
                    Box(
                        modifier = Modifier
                            .align(Alignment.End)
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(Color(0x22FFFFFF))
                            .clickable { onDismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    // Top Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x33F3D48A))
                            .border(1.dp, Color(0x66F3D48A), RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "⭐ PLAY STORE REVIEW",
                            color = Color(0xFFF3D48A),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }

                    // Title
                    Text(
                        text = "Rate Digital Tasbih",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Serif,
                        color = Color(0xFFF3D48A),
                        textAlign = TextAlign.Center
                    )

                    // Body
                    Text(
                        text = "Enjoying Digital Tasbih? Support us with a 5-star rating on the Google Play Store. Your honest review helps millions of Muslims find an ad-free, pure Dhikr app.",
                        fontSize = 13.sp,
                        color = Color(0xFFCBD5E1),
                        textAlign = TextAlign.Center,
                        lineHeight = 19.sp
                    )

                    // 5 Interactive Glowing Stars
                    Row(
                        modifier = Modifier.padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (i in 1..5) {
                            val isSelected = i <= selectedRating
                            val starScale by animateFloatAsState(
                                targetValue = if (isSelected) 1.15f else 1f,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                ),
                                label = "starScale$i"
                            )

                            Icon(
                                imageVector = if (isSelected) Icons.Filled.Star else Icons.Outlined.Star,
                                contentDescription = "$i Stars",
                                tint = if (isSelected) Color(0xFFF6D365) else Color(0x55F3D48A),
                                modifier = Modifier
                                    .size(34.dp)
                                    .scale(starScale)
                                    .clickable { selectedRating = i }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    // Primary Button: Rate Now
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(8.dp, RoundedCornerShape(18.dp), spotColor = Color(0xFFF3D48A))
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFFF3D48A), Color(0xFFD8A846))
                                )
                            )
                            .clickable { openPlayStore() }
                            .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "⭐", fontSize = 16.sp)
                            Text(
                                text = "Rate 5 Stars on Play Store",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF0C1D17)
                            )
                        }
                    }

                    // Secondary Row: Later & Already Rated
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Later / बाद में",
                            color = Color(0xFF94A3B8),
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable {
                                    // Remind in 7 days
                                    PrayerRepository.setRatePromptReminderTime(System.currentTimeMillis() + 7L * 24 * 3600 * 1000)
                                    onDismiss()
                                }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )

                        Text(
                            text = "Already Rated ✓",
                            color = Color(0xFF94A3B8),
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable {
                                    PrayerRepository.setAppRated(true)
                                    onDismiss()
                                }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
