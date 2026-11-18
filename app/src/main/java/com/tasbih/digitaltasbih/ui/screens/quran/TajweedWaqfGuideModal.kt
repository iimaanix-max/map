package com.tasbih.digitaltasbih.ui.screens.quran

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.util.TajweedRule
import com.tasbih.digitaltasbih.util.TajweedWaqfHelper
import com.tasbih.digitaltasbih.util.WaqfSign
import com.tasbih.digitaltasbih.util.getAction
import com.tasbih.digitaltasbih.util.getDescription
import com.tasbih.digitaltasbih.util.getName
import com.tasbih.digitaltasbih.util.getSummary
import com.tasbih.digitaltasbih.util.getTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TajweedWaqfGuideModal(
    langCode: String = "hi",
    isNightMode: Boolean = false,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedTab by remember { mutableIntStateOf(0) }

    val bgCard = if (isNightMode) Color(0xFF10221A) else Color(0xFFFCFAF5)
    val textPrimary = if (isNightMode) Color(0xFFF1F5F3) else Color(0xFF093B22)
    val textSecondary = if (isNightMode) Color(0xFF94A3B8) else Color(0xFF5D6B62)

    val titleText = when (langCode) {
        "en" -> "Waqf & Tajweed Guide"
        "ur" -> "وقف اور تجوید گائیڈ"
        "ar" -> "دليل الوقف والتجويد"
        else -> "वक़्फ़ व तजवीद गाइड (Waqf & Tajweed)"
    }
    val subtitleText = when (langCode) {
        "en" -> "Learn where to pause, stop, continue and Tajweed pronunciation"
        "ur" -> "قرآن میں کہاں ٹھہریں، کہاں جاری رکھیں اور صحیح ادائیگی سیکھیں"
        "ar" -> "تعلم علامات الوقف وأحكام التجويد الصحيحة"
        else -> "क़ुरआन में कहाँ रुकें, कहाँ जारी रखें व सही उच्चारण सीखें"
    }

    val tab1Title = when (langCode) {
        "en" -> "🛑 Pause Signs (Waqf)"
        "ur" -> "🛑 علاماتِ وقف"
        "ar" -> "🛑 علامات الوقف"
        else -> "🛑 वक़्फ़ चिह्न (Pause Signs)"
    }
    val tab2Title = when (langCode) {
        "en" -> "🎨 Tajweed Colors"
        "ur" -> "🎨 تجوید کے اصول"
        "ar" -> "🎨 أحكام التجويد"
        else -> "🎨 तजवीद रंग (Color Rules)"
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
                .fillMaxHeight(0.88f)
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
                        fontSize = 17.sp,
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

            // Two-tab Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isNightMode) Color(0xFF091712) else Color(0xFFF0EBE1))
                    .padding(4.dp)
            ) {
                TabButton(
                    title = tab1Title,
                    isSelected = selectedTab == 0,
                    isNightMode = isNightMode,
                    modifier = Modifier.weight(1f),
                    onClick = { selectedTab = 0 }
                )
                TabButton(
                    title = tab2Title,
                    isSelected = selectedTab == 1,
                    isNightMode = isNightMode,
                    modifier = Modifier.weight(1f),
                    onClick = { selectedTab = 1 }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tab Content
            if (selectedTab == 0) {
                WaqfSignsList(langCode = langCode, isNightMode = isNightMode)
            } else {
                TajweedRulesList(langCode = langCode, isNightMode = isNightMode)
            }
        }
    }
}

@Composable
private fun TabButton(
    title: String,
    isSelected: Boolean,
    isNightMode: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .background(
                if (isSelected) {
                    if (isNightMode) Color(0xFF134E35) else Color(0xFF0A4428)
                } else Color.Transparent
            )
            .padding(vertical = 9.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 12.5.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else (if (isNightMode) Color(0xFF94A3B8) else Color(0xFF4B5563))
        )
    }
}

@Composable
private fun WaqfSignsList(langCode: String, isNightMode: Boolean) {
    val signs = TajweedWaqfHelper.allSigns
    val itemBg = if (isNightMode) Color(0xFF152A20) else Color.White
    val textDark = if (isNightMode) Color(0xFFF1F5F3) else Color(0xFF1F2923)
    val textDesc = if (isNightMode) Color(0xFFCBD5E1) else Color(0xFF4B5563)

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        items(signs) { s ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(itemBg)
                    .border(1.2.dp, s.color.copy(alpha = 0.35f), RoundedCornerShape(18.dp))
                    .padding(14.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Large Arabic Symbol Badge
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(s.color.copy(alpha = 0.12f))
                        .border(1.5.dp, s.color, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = s.symbol,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = s.color,
                        fontFamily = FontFamily.Serif
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                // Details Column
                Column(modifier = Modifier.weight(1f)) {
                    val subTitle = when (langCode) {
                        "en" -> s.nameArabic
                        "ur" -> "${s.nameArabic} • ${s.nameEnglish}"
                        "ar" -> s.nameEnglish
                        else -> "${s.nameArabic} • ${s.nameEnglish}"
                    }

                    Text(
                        text = s.getName(langCode),
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = textDark
                    )
                    Text(
                        text = subTitle,
                        fontSize = 11.5.sp,
                        color = s.color,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    // Action Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(s.color.copy(alpha = 0.12f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = s.getAction(langCode),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = s.color
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = s.getDescription(langCode),
                        fontSize = 12.sp,
                        color = textDesc,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun TajweedRulesList(langCode: String, isNightMode: Boolean) {
    val rules = TajweedWaqfHelper.allRules
    val itemBg = if (isNightMode) Color(0xFF152A20) else Color.White
    val textDark = if (isNightMode) Color(0xFFF1F5F3) else Color(0xFF1F2923)
    val textDesc = if (isNightMode) Color(0xFFCBD5E1) else Color(0xFF4B5563)
    val exampleBoxBg = if (isNightMode) Color(0xFF0E1F17) else Color(0xFFFAF7F0)

    val exampleLabel = when (langCode) {
        "en" -> "Example:"
        "ur" -> "مثال:"
        "ar" -> "مثال:"
        else -> "उदाहरण:"
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        items(rules) { r ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(itemBg)
                    .border(1.2.dp, r.color.copy(alpha = 0.35f), RoundedCornerShape(18.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(r.color)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = r.getTitle(langCode),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = textDark
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = r.getSummary(langCode),
                    fontSize = 12.5.sp,
                    color = textDesc,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Example Box
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(exampleBoxBg)
                        .border(1.dp, r.color.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = exampleLabel,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = textDesc
                    )
                    Text(
                        text = r.exampleArabic,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = r.color,
                        fontFamily = FontFamily.Serif,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}
