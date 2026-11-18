package com.tasbih.digitaltasbih.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.data.PrayerRepository
import java.util.Calendar

/**
 * Live Interactive Notification Preview Mockup Card.
 * Exact match to original HTML fixedNotifLivePreview with live next prayer countdown,
 * drawer styling, and toggle switch.
 */
@Composable
fun FixedNotifPreviewCard(
    isDarkTheme: Boolean = true,
    onConfigureClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var isEnabled by remember { mutableStateOf(PrayerRepository.isFixedCountdownNotifEnabled()) }

    val nextPrayerInfo: Pair<String, String> = remember {
        try {
            val settings = PrayerRepository.loadSettings()
            val dayData = PrayerRepository.computeOfflineDay(Calendar.getInstance().time, settings)
            val lang = PrayerRepository.getLanguage()
            val meta = PrayerRepository.getActiveMeta(dayData, lang)
            val pName = meta.nextPrayer?.getLocalizedName(lang) ?: "Next Prayer"
            val pCountdown = meta.countdownText.ifBlank { "soon" }
            Pair(pName, pCountdown)
        } catch (_: Exception) {
            Pair("Asr", "1h 24m")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 3.dp, shape = RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(if (isDarkTheme) Color(0xFF0D251C) else Color(0xFFFFFDF8))
            .border(1.2.dp, if (isDarkTheme) Color(0xFF1E4535) else Color(0x33C9A84C), RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header with Switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🔔", fontSize = 20.sp)
                    Column {
                        Text(
                            text = "Persistent Prayer Notification",
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) Color(0xFFE8C878) else Color(0xFF0E5D47)
                        )
                        Text(
                            text = "Next prayer countdown in status bar",
                            fontSize = 12.sp,
                            color = if (isDarkTheme) Color(0xFF9EABA4) else Color(0xFF757575)
                        )
                    }
                }

                Switch(
                    checked = isEnabled,
                    onCheckedChange = { checked ->
                        isEnabled = checked
                        PrayerRepository.setFixedCountdownNotifEnabled(checked)
                        Toast.makeText(
                            context,
                            if (checked) "Persistent Notification Enabled ✓" else "Persistent Notification Disabled",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFF0A2219),
                        checkedTrackColor = Color(0xFFF3D48A),
                        uncheckedThumbColor = Color(0xFF94A3B8),
                        uncheckedTrackColor = Color(0x3394A3B8)
                    )
                )
            }

            // Live Preview Box (mimicking Android Status Bar Drawer notification card)
            AnimatedVisibility(
                visible = isEnabled,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "LIVE NOTIFICATION SHADE PREVIEW:",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF3D48A),
                        letterSpacing = 1.sp
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(6.dp, RoundedCornerShape(14.dp))
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFF061A13), Color(0xFF03100B))
                                )
                            )
                            .border(1.dp, Color(0x44F3D48A), RoundedCornerShape(14.dp))
                            .clickable {
                                Toast.makeText(
                                    context,
                                    "✨ Live Preview: Tapping notification opens Digital Tasbih Prayer Times",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .padding(14.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            // Top Row: App name + Ongoing badge + Settings tag
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "🕌", fontSize = 12.sp)
                                    Text(
                                        text = "Islamic Prayer Times",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF94A3B8)
                                    )
                                    Text(
                                        text = "• Ongoing",
                                        fontSize = 11.sp,
                                        color = Color(0xFF64748B)
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0x22F3D48A))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "⚙️ Active",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFF3D48A)
                                    )
                                }
                            }

                            // Notification Title: Next prayer & Countdown
                            Text(
                                text = "🕌 ${nextPrayerInfo.first} • Next in ${nextPrayerInfo.second}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFF3D48A)
                            )

                            // Notification Body
                            Text(
                                text = "Tap to open app & view offline timetable",
                                fontSize = 12.sp,
                                color = Color(0xFFCBD5E1)
                            )
                        }
                    }
                }
            }
        }
    }
}
