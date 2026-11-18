package com.tasbih.digitaltasbih.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.tasbih.digitaltasbih.data.pillars.IslamicReferenceItem

@Composable
fun IslamicReferenceModal(
    item: IslamicReferenceItem,
    isDarkTheme: Boolean = false,
    selectedLanguage: String = "en",
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val isQuran = item.webUrl.contains("quran.com") || item.grade.contains("Quran")

    // Localized Strings for Modal
    val modalTitle = when (selectedLanguage) {
        "hi", "hinglish" -> "प्रमाणित संदर्भ (AUTHENTIC REFERENCE)"
        "ur" -> "مستند حوالہ (صحیح حدیث و قرآن)"
        "ar" -> "المراجع والسنّة الصحيحة"
        else -> "AUTHENTIC REFERENCE"
    }

    val narratedByLabel = when (selectedLanguage) {
        "hi", "hinglish" -> "रावी (वर्णनकर्ता): "
        "ur" -> "راوی: "
        "ar" -> "عن: "
        else -> "Narrated by: "
    }

    val transLabel = when (selectedLanguage) {
        "hi", "hinglish" -> "अनुवाद (TRANSLATION):"
        "ur" -> "ترجمہ (TRANSLATION):"
        "ar" -> "البيان والشرح:"
        else -> "TRANSLATION:"
    }

    val viewSourceText = when (selectedLanguage) {
        "hi", "hinglish" -> if (isQuran) "Quran.com पर मूल आयत व तफ़सीर देखें" else "Sunnah.com पर मूल हदीस देखें"
        "ur" -> if (isQuran) "Quran.com پر اصل آیت اور تفسیر دیکھیں" else "Sunnah.com پر اصل حدیث دیکھیں"
        "ar" -> if (isQuran) "عرض الآية الكريمة وتفسيرها على Quran.com" else "عرض الحديث الشريف على Sunnah.com"
        else -> if (isQuran) "View on Quran.com (Original Ayah & Tafsir)" else "View on Sunnah.com (Original Hadith Source)"
    }

    val translationText = item.getTranslation(selectedLanguage)

    Dialog(onDismissRequest = onClose) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
                .shadow(20.dp, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDarkTheme) Color(0xFF0D2218) else Color.White
            ),
            border = BorderStroke(2.dp, Color(0xFF0E5D47))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // 1. Royal Dark Emerald Header with Golden Seam (Matching Morning Adhkar)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF0E5D47), Color(0xFF063B2C))
                            )
                        )
                        .border(
                            width = 1.5.dp,
                            color = Color(0xFFF6D365),
                            shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 13.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f, fill = false),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "📜", fontSize = 20.sp)
                            Text(
                                text = modalTitle,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Serif,
                                letterSpacing = 0.5.sp,
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        // Circular Close Button
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f))
                                .clickable { onClose() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "✕",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                // 2. Modal Body Content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Metadata Pill (Book & Grade)
                    if (item.bookOrSurah.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isDarkTheme) Color(0xFF132F23) else Color(0xFFF4FAF7))
                                .border(
                                    1.dp,
                                    if (isDarkTheme) Color(0xFF1E4532) else Color(0xFFD1E7DD),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 9.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f, fill = false),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(text = if (isQuran) "📖" else "🧾", fontSize = 13.sp)
                                Text(
                                    text = item.bookOrSurah,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDarkTheme) Color(0xFF68D391) else Color(0xFF0E5D47),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            if (item.grade.isNotEmpty()) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFFDCFCE7))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = "✓ ${item.grade}",
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFF059669)
                                    )
                                }
                            }
                        }
                    }

                    // Narrator Line
                    if (item.narrator.isNotEmpty()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = narratedByLabel,
                                fontSize = 11.5.sp,
                                fontStyle = FontStyle.Italic,
                                color = if (isDarkTheme) Color(0xFFA0AEC0) else Color(0xFF6B7280)
                            )
                            Text(
                                text = item.narrator,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color(0xFFE2E8F0) else Color(0xFF374151)
                            )
                        }
                    }

                    // Arabic Matn Box
                    if (item.arabicText.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isDarkTheme) Color(0xFF071911) else Color(0xFFFAF6EE))
                                .border(
                                    1.2.dp,
                                    if (isDarkTheme) Color(0xFF1E4532) else Color(0xFFE5D5B5),
                                    RoundedCornerShape(14.dp)
                                )
                                .padding(14.dp)
                        ) {
                            Text(
                                text = item.arabicText,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 30.sp,
                                textAlign = TextAlign.End,
                                color = if (isDarkTheme) Color(0xFFF1EAD8) else Color(0xFF1B382B),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    // Translation Box with 4px Emerald Accent Border
                    if (translationText.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isDarkTheme) Color(0xFF132F23) else Color(0xFFF9FAFB))
                                .drawBehind {
                                    drawRect(
                                        color = Color(0xFF0E5D47),
                                        size = Size(4.dp.toPx(), size.height)
                                    )
                                }
                                .padding(start = 14.dp, end = 12.dp, top = 10.dp, bottom = 12.dp)
                        ) {
                            Text(
                                text = transLabel,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color(0xFF68D391) else Color(0xFF0E5D47),
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = translationText,
                                fontSize = 12.5.sp,
                                lineHeight = 18.sp,
                                color = if (isDarkTheme) Color(0xFFE2E8F0) else Color(0xFF1F2937)
                            )
                        }
                    }

                    // View on Sunnah.com / Quran.com Official Source Button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(3.dp, RoundedCornerShape(12.dp))
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF0E5D47), Color(0xFF10B981))
                                )
                            )
                            .clickable {
                                try {
                                    val intent = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(item.webUrl)
                                    )
                                    context.startActivity(intent)
                                } catch (_: Exception) {}
                            }
                            .padding(vertical = 12.dp, horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(text = "🌐", fontSize = 14.sp)
                            Text(
                                text = viewSourceText,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(text = "↗", fontSize = 14.sp, color = Color.White)
                        }
                    }

                    // AdMob Native Ad Card (Zero-gap collapse)
                    AdMobNativeAdCard(modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

