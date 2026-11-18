package com.tasbih.digitaltasbih.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tasbih.digitaltasbih.data.HomeAnnouncementConfig
import com.tasbih.digitaltasbih.data.PrayerRepository

/**
 * Dynamic Multilingual Home Special Announcement Modal.
 * Respects user's chosen language (Hindi, English, Urdu, Arabic, Bengali, etc.),
 * with default fallback to Hindi ("hi"), and displays admin-broadcasted content.
 */
@Composable
fun HomeAnnouncementModal(
    config: HomeAnnouncementConfig = remember { PrayerRepository.getHomeAnnouncementConfig() },
    selectedLanguage: String = PrayerRepository.getLanguage(),
    onDismiss: () -> Unit,
    onParticipateClick: () -> Unit = onDismiss
) {
    val l = selectedLanguage.lowercase().split("-")[0]

    val defaultBadge = when (l) {
        "hi" -> "✨ विशेष आयोजन लाइव"
        "ur" -> "✨ خصوصی مہم لائیو"
        "ar" -> "✨ فعالية خاصة مباشرة"
        "bn" -> "✨ বিশেষ ইভেন্ট লাইভ"
        else -> "✨ SPECIAL EVENT LIVE"
    }

    val highlightTag = when (l) {
        "hi" -> "🏆 मुख्य घोषणा / विशेष आयोजन"
        "ur" -> "🏆 اہم اعلانیہ / خصوصی جھلکیاں"
        "ar" -> "🏆 تفاصيل الإعلان الهام"
        "bn" -> "🏆 বিশেষ ঘোষণা হাইলাইট"
        else -> "🏆 ANNOUNCEMENT HIGHLIGHT"
    }

    val statusLabel = when (l) {
        "hi" -> "स्थिति (Status)"
        "ur" -> "حیثیت (Status)"
        "ar" -> "الحالة"
        "bn" -> "স্ট্যাটাস"
        else -> "Status"
    }

    val liveNowText = when (l) {
        "hi" -> "लाइव जारी ✨"
        "ur" -> "لائیو جاری ✨"
        "ar" -> "مباشر الآن ✨"
        "bn" -> "লাইভ চলছে ✨"
        else -> "Live Now ✨"
    }

    val goalLabel = when (l) {
        "hi" -> "लक्ष्य / टारगेट"
        "ur" -> "ہدف / ٹارگٹ"
        "ar" -> "الهدف المطلوب"
        "bn" -> "লক্ষ্যমাত্রা"
        else -> "Goal / Target"
    }

    val defaultButtonText = when (l) {
        "hi" -> "अल्हम्दुलिल्लाह! शामिल हों ✓"
        "ur" -> "الحمد للہ! شامل ہوں ✓"
        "ar" -> "الحمد لله! انضم الآن ✓"
        "bn" -> "আলহামদুলিল্লাহ! যুক্ত হোন ✓"
        else -> "Alhamdulillah! Join Now ✓"
    }

    val defaultTitle = when (l) {
        "hi" -> "विशेष लाइव घोषणा 📿"
        "ur" -> "خصوصی لائیو اعلانیہ 📿"
        "ar" -> "إعلان مبارك مباشر 📿"
        "bn" -> "বিশেষ লাইভ ঘোষণা 📿"
        else -> "Special Live Announcement 📿"
    }

    val displayBadge = config.badgeText.ifBlank { defaultBadge }
    val displayTitle = config.title.ifBlank { defaultTitle }
    val displayButton = config.buttonText.ifBlank { defaultButtonText }

    Dialog(
        onDismissRequest = {
            PrayerRepository.markAnnouncementSeen(config.version)
            onDismiss()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.82f))
                .clickable {
                    PrayerRepository.markAnnouncementSeen(config.version)
                    onDismiss()
                }
                .padding(horizontal = 20.dp, vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable(enabled = false) {}
                    .shadow(36.dp, RoundedCornerShape(26.dp), spotColor = Color(0xFFF3D48A))
                    .clip(RoundedCornerShape(26.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF14382A),
                                Color(0xFF0C241B),
                                Color(0xFF04120D)
                            )
                        )
                    )
                    .border(1.5.dp, Color(0xFFF3D48A), RoundedCornerShape(26.dp))
                    .padding(22.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Top Bar: Badge + Close Icon
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
                                text = displayBadge,
                                color = Color(0xFFF3D48A),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0x33FFFFFF))
                                .clickable {
                                    PrayerRepository.markAnnouncementSeen(config.version)
                                    onDismiss()
                                },
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

                    // Arabic Calligraphy Header (if provided or default Bismillah)
                    val arabicHeader = config.arabicText.ifBlank { "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ" }
                    Text(
                        text = arabicHeader,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF3D48A),
                        textAlign = TextAlign.Center
                    )

                    // Event Title
                    Text(
                        text = displayTitle,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Serif,
                        color = Color(0xFFF3D48A),
                        textAlign = TextAlign.Center
                    )

                    // Prize / Fazilat Banner Box (if provided)
                    if (config.prizeTitle.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0x44F3D48A), Color(0x22F3D48A))
                                    )
                                )
                                .border(1.dp, Color(0x88F3D48A), RoundedCornerShape(16.dp))
                                .padding(14.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = highlightTag,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFF3D48A),
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = config.prizeTitle,
                                    fontSize = 14.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    // Detailed Message
                    if (config.message.isNotBlank()) {
                        Text(
                            text = config.message,
                            fontSize = 13.sp,
                            color = Color(0xFFD1D5DB),
                            textAlign = TextAlign.Center,
                            lineHeight = 19.sp
                        )
                    }

                    // Stats / Target Box (if provided)
                    if (config.targetText.isNotBlank()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0x1AF3D48A))
                                .border(1.dp, Color(0x33F3D48A), RoundedCornerShape(14.dp))
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = statusLabel,
                                    fontSize = 11.sp,
                                    color = Color(0xFF94A3B8)
                                )
                                Text(
                                    text = liveNowText,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFF3D48A)
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = goalLabel,
                                    fontSize = 11.sp,
                                    color = Color(0xFF94A3B8)
                                )
                                Text(
                                    text = config.targetText,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFF3D48A)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Primary Action Button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(10.dp, RoundedCornerShape(18.dp), spotColor = Color(0xFFF3D48A))
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFFF3D48A), Color(0xFFD8A846))
                                )
                            )
                            .clickable {
                                PrayerRepository.markAnnouncementSeen(config.version)
                                onParticipateClick()
                            }
                            .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = displayButton,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF0A2219)
                        )
                    }
                }
            }
        }
    }
}
