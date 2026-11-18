package com.tasbih.digitaltasbih.ui.screens.quran

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.ui.theme.GoldAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MushafThemePickerModal(
    currentThemeId: String,
    langCode: String = "hi",
    isNightMode: Boolean = false,
    onSelectTheme: (MushafTheme) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val themes = MushafTheme.allThemes

    val bgCard = if (isNightMode) Color(0xFF10221A) else Color(0xFFFCFAF5)
    val textPrimary = if (isNightMode) Color(0xFFF1F5F3) else Color(0xFF093B22)
    val textSecondary = if (isNightMode) Color(0xFF94A3B8) else Color(0xFF5D6B62)

    val titleText = when (langCode) {
        "en" -> "Mushaf Themes"
        "ur" -> "مصحف کے تھیمز"
        "ar" -> "ثيمات المصحف"
        else -> "मुसहफ़ थीम (Mushaf Themes)"
    }
    val subtitleText = when (langCode) {
        "en" -> "12 Authentic physical Mushaf styles & colored Tajweed pages"
        "ur" -> "12 مستند قرآنی نسخے اور رنگین تجویدی صفحات"
        "ar" -> "12 نمطاً أصيلاً للمصاحف وصفحات التجويد الملونة"
        else -> "12 प्रामाणिक मुसहफ़ शैलियाँ एवं रंगीन तजवीद पृष्ठ"
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = bgCard,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .width(44.dp)
                    .height(4.5.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0xFFD4AF37).copy(alpha = 0.5f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(horizontal = 16.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = titleText,
                        fontSize = 17.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                    Text(
                        text = subtitleText,
                        fontSize = 12.sp,
                        color = textSecondary
                    )
                }
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = textSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(themes) { t ->
                    val isSelected = (t.id == currentThemeId)

                    val cardBg = if (isNightMode) Color(0xFF152A20) else t.pageColor
                    val cardBorder = if (isSelected) {
                        GoldAccent
                    } else {
                        if (isNightMode) Color(0xFF1E382B) else t.innerBorderColor.copy(alpha = 0.45f)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(cardBg)
                            .border(if (isSelected) 2.dp else 1.2.dp, cardBorder, RoundedCornerShape(16.dp))
                            .clickable {
                                onSelectTheme(t)
                                onDismiss()
                            }
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Swatch Preview Box
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(t.pageColor)
                                .border(1.5.dp, t.outerBorderColor, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(t.innerBorderColor),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "ق",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = t.arabicTextColor,
                                    fontFamily = FontFamily.Serif
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        // Titles
                        Column(modifier = Modifier.weight(1f)) {
                            val primaryName = when (langCode) {
                                "en" -> t.nameEnglish
                                "ur" -> t.nameArabic
                                "ar" -> t.nameArabic
                                else -> t.nameHindi
                            }
                            val secondaryName = when (langCode) {
                                "en" -> "${t.nameArabic} • ${t.nameHindi}"
                                "ur" -> "${t.nameEnglish} • ${t.nameArabic}"
                                "ar" -> t.nameEnglish
                                else -> "${t.nameEnglish} • ${t.nameArabic}"
                            }

                            Text(
                                text = primaryName,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (t.isDark) Color.White else (if (isNightMode) Color(0xFFF1F5F3) else Color(0xFF1F2923))
                            )
                            Text(
                                text = secondaryName,
                                fontSize = 11.5.sp,
                                color = if (t.isDark) Color(0xFFCBD5E1) else textSecondary
                            )
                        }

                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Selected",
                                tint = GoldAccent,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
