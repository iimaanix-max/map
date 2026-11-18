package com.tasbih.digitaltasbih.ui.screens.prayer

import android.content.Context
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tasbih.digitaltasbih.R
import com.tasbih.digitaltasbih.data.PrayerSoundManager
import com.tasbih.digitaltasbih.ui.components.AppToast

@Composable
fun PrayerNotificationSoundModal(
    selectedLang: String,
    isDarkTheme: Boolean = false,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var soundMode by remember { mutableStateOf(PrayerSoundManager.getSoundMode(context)) }
    var selectedAdhanRaw by remember { mutableStateOf(PrayerSoundManager.getSelectedAdhanRaw(context)) }

    // Audio Preview State
    var activePreviewId by remember { mutableStateOf<String?>(null) } // "ting", "chime", or adhan rawName
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var ringtonePlayer by remember { mutableStateOf<Ringtone?>(null) }

    fun stopAllPreview() {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        } catch (_: Exception) {}
        try {
            ringtonePlayer?.stop()
            ringtonePlayer = null
        } catch (_: Exception) {}
        activePreviewId = null
    }

    // Stop audio on dismiss
    DisposableEffect(Unit) {
        onDispose {
            stopAllPreview()
        }
    }

    fun playPreview(id: String, audioRes: Int? = null, ringtoneType: Int? = null) {
        if (activePreviewId == id) {
            stopAllPreview()
            return
        }

        stopAllPreview()
        activePreviewId = id

        if (audioRes != null) {
            try {
                val mp = MediaPlayer.create(context, audioRes)
                if (mp != null) {
                    mediaPlayer = mp
                    mp.setOnCompletionListener {
                        stopAllPreview()
                    }
                    mp.start()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                stopAllPreview()
            }
        } else if (ringtoneType != null) {
            try {
                val uri = RingtoneManager.getDefaultUri(ringtoneType)
                val rt = RingtoneManager.getRingtone(context, uri)
                if (rt != null) {
                    ringtonePlayer = rt
                    rt.play()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                stopAllPreview()
            }
        }
    }

    val primaryEmerald = Color(0xFF0F5132)
    val pureGold = Color(0xFFD4AF37)
    val champagneGold = Color(0xFFFDE8A8)
    val darkCardBg = Color(0xFF091F16)
    val lightCardBg = Color(0xFFFFFFFF)

    Dialog(
        onDismissRequest = {
            stopAllPreview()
            onDismiss()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 660.dp)
                    .shadow(24.dp, RoundedCornerShape(26.dp)),
                shape = RoundedCornerShape(26.dp),
                color = if (isDarkTheme) Color(0xFF06150F) else Color(0xFFFAF9F5),
                border = BorderStroke(1.5.dp, if (isDarkTheme) pureGold.copy(alpha = 0.5f) else Color(0xFF0F5132))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    // ── Header ──
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(primaryEmerald, Color(0xFF052017))
                                        )
                                    )
                                    .border(1.2.dp, pureGold, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🔔", fontSize = 20.sp)
                            }

                            Column {
                                Text(
                                    text = when {
                                        selectedLang.startsWith("hi") -> "नमाज़ नोटिफिकेशन साउंड"
                                        selectedLang.startsWith("ur") -> "نماز نوٹیفکیشن ساؤنڈ"
                                        selectedLang.startsWith("ar") -> "صوت تنبيه الصلاة"
                                        selectedLang.startsWith("hinglish") -> "Namaz Notification Sound"
                                        else -> "Salah Notification Sound"
                                    },
                                    fontSize = 16.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDarkTheme) Color.White else Color(0xFF0F5132)
                                )
                                Text(
                                    text = when {
                                        selectedLang.startsWith("hi") -> "नमाज़ के वक़्त बजने वाला साउंड चुनें"
                                        selectedLang.startsWith("ur") -> "نماز کے وقت کی آواز منتخب کریں"
                                        selectedLang.startsWith("ar") -> "اختر نغمة التنبيه عند حلول الأذان"
                                        selectedLang.startsWith("hinglish") -> "Namaz ke waqt ka sound choose karein"
                                        else -> "Choose sound for Azan & Salah alerts"
                                    },
                                    fontSize = 11.5.sp,
                                    color = if (isDarkTheme) Color(0xFF8FA89B) else Color(0xFF556960)
                                )
                            }
                        }

                        IconButton(
                            onClick = {
                                stopAllPreview()
                                onDismiss()
                            },
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(if (isDarkTheme) Color(0x33FFFFFF) else Color(0xFFE2E8F0))
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Close",
                                tint = if (isDarkTheme) Color.White else Color(0xFF334155),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // ── Scrollable Sound Options ──
                    Column(
                        modifier = Modifier
                            .weight(1f, fill = false)
                            .verticalScroll(scrollState),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // ── Option 1: Full Sacred Adhan (Recommended / Default) ──
                        val isAdhanSelected = soundMode == PrayerSoundManager.MODE_ADHAN
                        SoundCategoryCard(
                            title = when {
                                selectedLang.startsWith("hi") -> "पवित्र अज़ान (पूरी 3-मिनट अज़ान)"
                                selectedLang.startsWith("ur") -> "مقدس اذان (مکمل 3 منٹ اذان)"
                                selectedLang.startsWith("ar") -> "الأذان الشريف (كامل 3 دقائق)"
                                selectedLang.startsWith("hinglish") -> "Full Sacred Adhan (~3 min)"
                                else -> "Full Sacred Adhan (~3 min)"
                            },
                            subtitle = when {
                                selectedLang.startsWith("hi") -> "मशहूर मुअज़्ज़िनों की पूरी अज़ान चुनें • डिफ़ॉल्ट"
                                selectedLang.startsWith("ur") -> "معروف مؤذنین کی مکمل اذان منتخب کریں • ڈیفالٹ"
                                selectedLang.startsWith("ar") -> "أصوات عذبة من مشاهير المؤذنين • الافتراضي"
                                selectedLang.startsWith("hinglish") -> "Choose full melodious Adhan from world-renowned Muadhins • Default"
                                else -> "Choose full melodious Adhan from world-renowned Muadhins • Default"
                            },
                            icon = "📢",
                            badgeText = when {
                                selectedLang.startsWith("hi") -> "अनुशंसित / डिफ़ॉल्ट"
                                selectedLang.startsWith("ur") -> "تجویز کردہ / ڈیفالٹ"
                                else -> "Recommended / Default"
                            },
                            isSelected = isAdhanSelected,
                            isDarkTheme = isDarkTheme,
                            onClick = {
                                soundMode = PrayerSoundManager.MODE_ADHAN
                                PrayerSoundManager.setSoundMode(context, PrayerSoundManager.MODE_ADHAN)
                                AppToast.show(
                                    when {
                                        selectedLang.startsWith("hi") -> "पूरी अज़ान सेट की गई 📢"
                                        selectedLang.startsWith("ur") -> "مکمل اذان سیٹ کر دی گئی 📢"
                                        else -> "Full Adhan set as notification sound 📢"
                                    }
                                )
                            }
                        )

                        // ── Expanded Adhan Reciters List ──
                        AnimatedVisibility(visible = isAdhanSelected) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 10.dp, top = 4.dp, bottom = 4.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                PrayerSoundManager.SACRED_ADHANS.forEach { adhan ->
                                    val isThisAdhanActive = selectedAdhanRaw == adhan.rawName
                                    val isThisPlaying = activePreviewId == adhan.rawName

                                    AdhanSubChoiceItem(
                                        adhan = adhan,
                                        selectedLang = selectedLang,
                                        isActive = isThisAdhanActive,
                                        isPlaying = isThisPlaying,
                                        isDarkTheme = isDarkTheme,
                                        onSelect = {
                                            selectedAdhanRaw = adhan.rawName
                                            PrayerSoundManager.setSelectedAdhanRaw(context, adhan.rawName)
                                            AppToast.show("🕌 ${adhan.getTitle(selectedLang)}")
                                        },
                                        onTogglePlay = {
                                            playPreview(adhan.rawName, audioRes = adhan.audioRes)
                                        }
                                    )
                                }
                            }
                        }

                        // ── Option 2: Short Sacred Takbeer (8s Quick Option) ──
                        val isTakbeerSelected = soundMode == PrayerSoundManager.MODE_TAKBEER
                        val isTakbeerPlaying = activePreviewId == "takbeer"
                        SoundCategoryCard(
                            title = when {
                                selectedLang.startsWith("hi") -> "पवित्र तकबीर (शेख अहमद अल-नफीस)"
                                selectedLang.startsWith("ur") -> "مقدس تکبیر (شیخ احمد النفیس)"
                                selectedLang.startsWith("ar") -> "التكبير الشريف (الشيخ أحمد النفيس)"
                                selectedLang.startsWith("hinglish") -> "Short Sacred Takbeer (8s)"
                                else -> "Short Sacred Takbeer (8s)"
                            },
                            subtitle = when {
                                selectedLang.startsWith("hi") -> "अल्लाहु अकबर, अल्लाहु अकबर • 8 सेकंड की रूह-परवर आवाज़"
                                selectedLang.startsWith("ur") -> "اللہ اکبر، اللہ اکبر • 8 سیکنڈ کی مسحور کن تکبیر"
                                selectedLang.startsWith("ar") -> "الله أكبر، الله أكبر • تكبير شريف ومدوي لمدة 8 ثوان"
                                selectedLang.startsWith("hinglish") -> "Allahu Akbar, Allahu Akbar • 8-sec soul-stirring alert"
                                else -> "Allahu Akbar, Allahu Akbar • 8-sec soul-stirring alert"
                            },
                            icon = "🕌",
                            badgeText = when {
                                selectedLang.startsWith("hi") -> "8s शॉर्ट"
                                selectedLang.startsWith("ur") -> "8s شارٹ"
                                else -> "8s Short"
                            },
                            isSelected = isTakbeerSelected,
                            isPlaying = isTakbeerPlaying,
                            hasPreview = true,
                            isDarkTheme = isDarkTheme,
                            onClick = {
                                soundMode = PrayerSoundManager.MODE_TAKBEER
                                PrayerSoundManager.setSoundMode(context, PrayerSoundManager.MODE_TAKBEER)
                                AppToast.show(
                                    when {
                                        selectedLang.startsWith("hi") -> "पवित्र तकबीर सेट की गई 🕌"
                                        selectedLang.startsWith("ur") -> "مقدس تکبیر سیٹ کر دی گئی 🕌"
                                        else -> "Short Sacred Takbeer set as sound 🕌"
                                    }
                                )
                            },
                            onPreviewToggle = {
                                playPreview("takbeer", audioRes = R.raw.prayer_takbeer_nafees)
                            }
                        )

                        // ── Option 3: Sacred Hijaz Chime ──
                        val isChimeSelected = soundMode == PrayerSoundManager.MODE_CHIME
                        val isChimePlaying = activePreviewId == "chime"
                        SoundCategoryCard(
                            title = when {
                                selectedLang.startsWith("hi") -> "हिजाज़ चाइम (पवित्र मधुर धुन)"
                                selectedLang.startsWith("ur") -> "حجاز چائم (مدھر اور پرسکون دھن)"
                                selectedLang.startsWith("ar") -> "نغمة الحجاز الهادئة"
                                selectedLang.startsWith("hinglish") -> "Sacred Hijaz Chime (3s)"
                                else -> "Sacred Hijaz Chime (3s)"
                            },
                            subtitle = when {
                                selectedLang.startsWith("hi") -> "सुकून देने वाली सौम्य इस्लामिक धुन (मकाम हिजाज़)"
                                selectedLang.startsWith("ur") -> "روح پرور اور دھیمی اسلامی دھن"
                                selectedLang.startsWith("ar") -> "نغمة روحانية هادئة على مقام الحجاز"
                                selectedLang.startsWith("hinglish") -> "Serene gentle Islamic chime (Maqam Hijaz)"
                                else -> "Serene gentle Islamic chime (Maqam Hijaz)"
                            },
                            icon = "✨",
                            badgeText = "2.8s",
                            isSelected = isChimeSelected,
                            isPlaying = isChimePlaying,
                            hasPreview = true,
                            isDarkTheme = isDarkTheme,
                            onClick = {
                                soundMode = PrayerSoundManager.MODE_CHIME
                                PrayerSoundManager.setSoundMode(context, PrayerSoundManager.MODE_CHIME)
                                AppToast.show(
                                    when {
                                        selectedLang.startsWith("hi") -> "हिजाज़ चाइम सेट किया गया ✨"
                                        selectedLang.startsWith("ur") -> "حجاز چائم سیٹ کر دیا گیا ✨"
                                        else -> "Sacred Hijaz Chime set ✨"
                                    }
                                )
                            },
                            onPreviewToggle = {
                                playPreview("chime", audioRes = R.raw.prayer_chime_hijaz)
                            }
                        )

                        // ── Option 4: Normal Ting / Beep ──
                        val isTingSelected = soundMode == PrayerSoundManager.MODE_TING
                        val isTingPlaying = activePreviewId == "ting"
                        SoundCategoryCard(
                            title = when {
                                selectedLang.startsWith("hi") -> "नॉर्मल टिंग (सिस्टम टोन)"
                                selectedLang.startsWith("ur") -> "نارمل ٹنگ (سسٹم ٹون)"
                                selectedLang.startsWith("ar") -> "نغمة إشعار قياسية (رنة قصيرة)"
                                selectedLang.startsWith("hinglish") -> "Normal Ting (System Tone)"
                                else -> "Normal Ting (System Tone)"
                            },
                            subtitle = when {
                                selectedLang.startsWith("hi") -> "फ़ोन का साधारण डिफ़ॉल्ट नोटिफिकेशन साउंड"
                                selectedLang.startsWith("ur") -> "موبائل کا معیاری نوٹیفکیشن ساؤنڈ"
                                selectedLang.startsWith("ar") -> "صوت تنبيه خفيف وسريع للهاتف"
                                selectedLang.startsWith("hinglish") -> "Standard short system notification sound"
                                else -> "Standard short system notification tone"
                            },
                            icon = "🔔",
                            isSelected = isTingSelected,
                            isPlaying = isTingPlaying,
                            hasPreview = true,
                            isDarkTheme = isDarkTheme,
                            onClick = {
                                soundMode = PrayerSoundManager.MODE_TING
                                PrayerSoundManager.setSoundMode(context, PrayerSoundManager.MODE_TING)
                                AppToast.show(
                                    when {
                                        selectedLang.startsWith("hi") -> "नॉर्मल टिंग सेट किया गया 🔔"
                                        selectedLang.startsWith("ur") -> "نارمل ٹنگ سیٹ کر دیا گیا 🔔"
                                        else -> "Normal Ting tone set 🔔"
                                    }
                                )
                            },
                            onPreviewToggle = {
                                playPreview("ting", ringtoneType = RingtoneManager.TYPE_NOTIFICATION)
                            }
                        )

                        // ── Option 5: Silent / Vibrate Only ──
                        val isSilentSelected = soundMode == PrayerSoundManager.MODE_SILENT
                        SoundCategoryCard(
                            title = when {
                                selectedLang.startsWith("hi") -> "केवल वाइब्रेशन (म्यूट)"
                                selectedLang.startsWith("ur") -> "صرف وائبریشن (خاموش)"
                                selectedLang.startsWith("ar") -> "اهتزاز فقط (صامت)"
                                selectedLang.startsWith("hinglish") -> "Silent (Vibrate Only)"
                                else -> "Silent (Vibrate Only)"
                            },
                            subtitle = when {
                                selectedLang.startsWith("hi") -> "बिना आवाज़ केवल कंपन होगा"
                                selectedLang.startsWith("ur") -> "بغیر کسی آواز کے صرف وائبریشن ہوگی"
                                selectedLang.startsWith("ar") -> "تنبيه بالاهتزاز بدون إصدار صوت"
                                selectedLang.startsWith("hinglish") -> "Bina kisi sound ke sirf vibration aayega"
                                else -> "Vibrate only without any audio tone"
                            },
                            icon = "🔕",
                            isSelected = isSilentSelected,
                            isDarkTheme = isDarkTheme,
                            onClick = {
                                soundMode = PrayerSoundManager.MODE_SILENT
                                PrayerSoundManager.setSoundMode(context, PrayerSoundManager.MODE_SILENT)
                                stopAllPreview()
                                AppToast.show(
                                    when {
                                        selectedLang.startsWith("hi") -> "वाइब्रेशन मोड सेट 🔕"
                                        selectedLang.startsWith("ur") -> "وائبریشن موڈ سیٹ 🔕"
                                        else -> "Vibrate only mode set 🔕"
                                    }
                                )
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // ── Active Selection Confirmation Banner ──
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (isDarkTheme) Color(0xFF0C241B) else Color(0xFFEAF5EE)
                            )
                            .border(
                                1.dp,
                                if (isDarkTheme) pureGold.copy(alpha = 0.5f) else Color(0xFF86EFAC),
                                RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = if (isDarkTheme) pureGold else primaryEmerald,
                                modifier = Modifier.size(18.dp)
                            )
                            Column {
                                Text(
                                    text = when {
                                        selectedLang.startsWith("hi") -> "वर्तमान सक्रिय साउंड:"
                                        selectedLang.startsWith("ur") -> "موجودہ فعال ساؤنڈ:"
                                        else -> "Active Notification Sound:"
                                    },
                                    fontSize = 11.sp,
                                    color = if (isDarkTheme) Color(0xFF8FA89B) else Color(0xFF556960)
                                )
                                Text(
                                    text = PrayerSoundManager.getSoundDisplayName(context, selectedLang),
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDarkTheme) champagneGold else primaryEmerald,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SoundCategoryCard(
    title: String,
    subtitle: String,
    icon: String,
    isSelected: Boolean,
    badgeText: String? = null,
    isPlaying: Boolean = false,
    hasPreview: Boolean = false,
    isDarkTheme: Boolean = false,
    onClick: () -> Unit,
    onPreviewToggle: (() -> Unit)? = null
) {
    val pureGold = Color(0xFFD4AF37)
    val primaryEmerald = Color(0xFF0F5132)

    val cardBorder = if (isSelected) {
        BorderStroke(1.5.dp, pureGold)
    } else {
        BorderStroke(1.dp, if (isDarkTheme) Color(0x33FDE8A8) else Color(0xFFE2E8F0))
    }

    val cardBg = if (isDarkTheme) {
        if (isSelected) Color(0xFF0F2E22) else Color(0xFF0A1C15)
    } else {
        if (isSelected) Color(0xFFF0FDF4) else Color(0xFFFFFFFF)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        border = cardBorder,
        color = cardBg
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val iconBgBrush = if (isSelected) {
                Brush.linearGradient(listOf(primaryEmerald, Color(0xFF052017)))
            } else {
                val c = if (isDarkTheme) Color(0xFF132B21) else Color(0xFFEAF5EE)
                Brush.linearGradient(listOf(c, c))
            }

            // Icon
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(iconBgBrush)
                    .border(1.dp, if (isSelected) pureGold else Color.Transparent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = icon, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Title, Badge & Subtitle
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = title,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) {
                            if (isDarkTheme) pureGold else primaryEmerald
                        } else {
                            if (isDarkTheme) Color.White else Color(0xFF1E293B)
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (badgeText != null) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(pureGold.copy(alpha = if (isDarkTheme) 0.25f else 0.18f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = badgeText,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) pureGold else Color(0xFF92400E)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = if (isDarkTheme) Color(0xFF8FA89B) else Color(0xFF64748B),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Preview Play button (if applicable)
            if (hasPreview && onPreviewToggle != null) {
                IconButton(
                    onClick = onPreviewToggle,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(if (isPlaying) pureGold else if (isDarkTheme) Color(0xFF183B2C) else Color(0xFFE2E8F0))
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = "Preview",
                        tint = if (isPlaying) Color.Black else if (isDarkTheme) Color.White else Color(0xFF0F5132),
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
            }

            // Radio Button
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = pureGold,
                    unselectedColor = if (isDarkTheme) Color(0xFF556960) else Color(0xFF94A3B8)
                ),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun AdhanSubChoiceItem(
    adhan: com.tasbih.digitaltasbih.data.AdhanChoice,
    selectedLang: String,
    isActive: Boolean,
    isPlaying: Boolean,
    isDarkTheme: Boolean,
    onSelect: () -> Unit,
    onTogglePlay: () -> Unit
) {
    val pureGold = Color(0xFFD4AF37)
    val primaryEmerald = Color(0xFF0F5132)

    val itemBorder = if (isActive) {
        BorderStroke(1.2.dp, pureGold)
    } else {
        BorderStroke(0.8.dp, if (isDarkTheme) Color(0x338FA89B) else Color(0xFFE2E8F0))
    }

    val itemBg = if (isActive) {
        if (isDarkTheme) Color(0xFF123527) else Color(0xFFF0FDF4)
    } else {
        if (isDarkTheme) Color(0xFF081812) else Color(0xFFF8FAF9)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onSelect() },
        shape = RoundedCornerShape(12.dp),
        border = itemBorder,
        color = itemBg
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Flag
            Text(text = adhan.countryFlag, fontSize = 16.sp)

            Spacer(modifier = Modifier.width(8.dp))

            // Title & Reciter
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = adhan.getTitle(selectedLang),
                    fontSize = 12.sp,
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                    color = if (isActive) {
                        if (isDarkTheme) pureGold else primaryEmerald
                    } else {
                        if (isDarkTheme) Color.White else Color(0xFF1E293B)
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = adhan.getReciter(selectedLang),
                    fontSize = 10.5.sp,
                    color = if (isDarkTheme) Color(0xFF8FA89B) else Color(0xFF64748B),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Preview Play button
            IconButton(
                onClick = onTogglePlay,
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(if (isPlaying) pureGold else if (isDarkTheme) Color(0xFF1B3F30) else Color(0xFFE2E8F0))
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = "Preview",
                    tint = if (isPlaying) Color.Black else if (isDarkTheme) Color.White else Color(0xFF0F5132),
                    modifier = Modifier.size(15.dp)
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Selected checkmark / radio
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(if (isActive) pureGold else Color.Transparent)
                    .border(
                        1.2.dp,
                        if (isActive) pureGold else if (isDarkTheme) Color(0xFF556960) else Color(0xFF94A3B8),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isActive) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
        }
    }
}
