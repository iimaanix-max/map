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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tasbih.digitaltasbih.data.HomeAnnouncementConfig
import com.tasbih.digitaltasbih.data.PrayerRepository

/**
 * Admin Panel for Managing Home Screen Special Announcements.
 * Allows Admin to turn announcements ON/OFF, write custom title, message,
 * arabic calligraphy, targets, and broadcast to all users.
 */
@Composable
fun AdminAnnouncementModal(
    onDismiss: () -> Unit,
    onPreview: (HomeAnnouncementConfig) -> Unit = {}
) {
    val context = LocalContext.current
    val currentConfig = remember { PrayerRepository.getHomeAnnouncementConfig() }

    var isEnabled by remember { mutableStateOf(currentConfig.isEnabled) }
    var badgeText by remember { mutableStateOf(currentConfig.badgeText.ifBlank { "✨ SPECIAL EVENT LIVE" }) }
    var arabicText by remember { mutableStateOf(currentConfig.arabicText.ifBlank { "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ" }) }
    var title by remember { mutableStateOf(currentConfig.title.ifBlank { "Special Live Dhikr Muhim" }) }
    var prizeTitle by remember { mutableStateOf(currentConfig.prizeTitle.ifBlank { "🏆 GLOBAL ISTIQAMAH CAMPAIGN" }) }
    var message by remember { mutableStateOf(currentConfig.message.ifBlank { "Join thousands of believers in collective remembrance & prayers." }) }
    var targetText by remember { mutableStateOf(currentConfig.targetText.ifBlank { "1 Crore Target" }) }
    var buttonText by remember { mutableStateOf(currentConfig.buttonText.ifBlank { "Alhamdulillah! Shamil Huein ✓" }) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f))
                .clickable { onDismiss() }
                .padding(horizontal = 16.dp, vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable(enabled = false) {}
                    .shadow(32.dp, RoundedCornerShape(24.dp), spotColor = Color(0xFFF3D48A))
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF0F2E22), Color(0xFF091F17), Color(0xFF04100C))
                        )
                    )
                    .border(1.5.dp, Color(0xFFF3D48A), RoundedCornerShape(24.dp))
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Header Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "ANNOUNCEMENT MANAGER",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF3D48A),
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Admin Broadcast Control",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0x33FFFFFF))
                                .clickable { onDismiss() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // Master ON / OFF Switch Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isEnabled) Color(0x3310B981) else Color(0x22EF4444))
                            .border(1.2.dp, if (isEnabled) Color(0xFF10B981) else Color(0xFFEF4444), RoundedCornerShape(16.dp))
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isEnabled) "🟢 Broadcast is ACTIVE" else "🔴 Broadcast is OFF",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isEnabled) Color(0xFF34D399) else Color(0xFFF87171)
                                )
                                Text(
                                    text = if (isEnabled) "Home Screen popup will show to users" else "Announcement is hidden (no popup on Home)",
                                    fontSize = 11.5.sp,
                                    color = Color(0xFFCBD5E1)
                                )
                            }

                            Switch(
                                checked = isEnabled,
                                onCheckedChange = { isEnabled = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color(0xFF062018),
                                    checkedTrackColor = Color(0xFF34D399),
                                    uncheckedThumbColor = Color.White,
                                    uncheckedTrackColor = Color(0x44EF4444)
                                )
                            )
                        }
                    }

                    // Content Configuration Section
                    AnimatedVisibility(
                        visible = isEnabled,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            // Event Title
                            Text(text = "Announcement Title:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF3D48A))
                            OutlinedTextField(
                                value = title,
                                onValueChange = { title = it },
                                placeholder = { Text("e.g. Ramadan Mubarak / 10 Crore Durood Muhim") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )

                            // Arabic Calligraphy / Header
                            Text(text = "Arabic Calligraphy Header:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF3D48A))
                            OutlinedTextField(
                                value = arabicText,
                                onValueChange = { arabicText = it },
                                placeholder = { Text("بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )

                            // Badge Text
                            Text(text = "Top Ribbon Badge:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF3D48A))
                            OutlinedTextField(
                                value = badgeText,
                                onValueChange = { badgeText = it },
                                placeholder = { Text("✨ SPECIAL EVENT LIVE") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )

                            // Prize / Tagline Banner
                            Text(text = "Prize / Highlight Box:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF3D48A))
                            OutlinedTextField(
                                value = prizeTitle,
                                onValueChange = { prizeTitle = it },
                                placeholder = { Text("🏆 Global Live Istiqamah Campaign") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )

                            // Detailed Message
                            Text(text = "Message Body:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF3D48A))
                            OutlinedTextField(
                                value = message,
                                onValueChange = { message = it },
                                placeholder = { Text("Write message for the community...") },
                                minLines = 2,
                                maxLines = 4,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )

                            // Target Text
                            Text(text = "Target / Goal Text:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF3D48A))
                            OutlinedTextField(
                                value = targetText,
                                onValueChange = { targetText = it },
                                placeholder = { Text("e.g. 10 Cr Target / Khatam Live") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )

                            // Action Button Text
                            Text(text = "Button Text:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF3D48A))
                            OutlinedTextField(
                                value = buttonText,
                                onValueChange = { buttonText = it },
                                placeholder = { Text("Alhamdulillah! Shamil Huein ✓") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Buttons Row: Save & Broadcast + Preview
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Preview Button
                        OutlinedButton(
                            onClick = {
                                val cfg = HomeAnnouncementConfig(
                                    isEnabled = isEnabled,
                                    badgeText = badgeText,
                                    arabicText = arabicText,
                                    title = title,
                                    prizeTitle = prizeTitle,
                                    message = message,
                                    targetText = targetText,
                                    buttonText = buttonText,
                                    version = System.currentTimeMillis()
                                )
                                onPreview(cfg)
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF3D48A)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFF3D48A))
                        ) {
                            Text("👁️ Preview", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }

                        // Save & Broadcast Button
                        Button(
                            onClick = {
                                val newVersion = System.currentTimeMillis()
                                val cfg = HomeAnnouncementConfig(
                                    isEnabled = isEnabled,
                                    badgeText = badgeText.trim(),
                                    arabicText = arabicText.trim(),
                                    title = title.trim(),
                                    prizeTitle = prizeTitle.trim(),
                                    message = message.trim(),
                                    targetText = targetText.trim(),
                                    buttonText = buttonText.trim(),
                                    version = newVersion
                                )
                                PrayerRepository.saveHomeAnnouncementConfig(cfg)
                                Toast.makeText(
                                    context,
                                    if (isEnabled) "✓ Announcement saved & broadcasted to Home Screen!" else "✓ Announcement turned OFF successfully",
                                    Toast.LENGTH_LONG
                                ).show()
                                onDismiss()
                            },
                            modifier = Modifier.weight(1.4f),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isEnabled) Color(0xFFF3D48A) else Color(0xFFEF4444),
                                contentColor = Color(0xFF091F17)
                            )
                        ) {
                            Text(
                                text = if (isEnabled) "✓ Save & Broadcast" else "✓ Save (Keep OFF)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }
        }
    }
}
