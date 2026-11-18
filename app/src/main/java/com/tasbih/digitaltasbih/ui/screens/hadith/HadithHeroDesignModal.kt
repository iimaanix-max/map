package com.tasbih.digitaltasbih.ui.screens.hadith

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

enum class HadithHeroStyle(
    val id: String,
    val titleEn: String,
    val titleHi: String,
    val descEn: String,
    val descHi: String,
    val previewBg: Color,
    val previewBorder: Color,
    val previewAccent: Color
) {
    EMERALD_ARCH(
        id = "emerald_arch",
        titleEn = "Emerald Arch",
        titleHi = "एमराल्ड आर्क (शाही हरा)",
        descEn = "Deep Islamic emerald with royal gold trim",
        descHi = "गहरे हरे रंग का शाही इस्लामिक डिज़ाइन",
        previewBg = Color(0xFF0E5D47),
        previewBorder = Color(0xFFC9A84C),
        previewAccent = Color(0xFFE8C878)
    ),
    CLASSIC_IVORY(
        id = "classic_ivory",
        titleEn = "Classic Ivory",
        titleHi = "क्लासिक आइवरी (पार्चमेंट)",
        descEn = "Warm authentic parchment with vintage serif borders",
        descHi = "हल्का वॉर्म पार्चमेंट विंटेज डिज़ाइन",
        previewBg = Color(0xFFFDFBF7),
        previewBorder = Color(0xFFC9A84C),
        previewAccent = Color(0xFF5D4D38)
    ),
    GOLD_MINIMAL(
        id = "gold_minimal",
        titleEn = "Gold Minimal",
        titleHi = "गोल्ड मिनिमल (मॉडर्न)",
        descEn = "Sleek minimalist slate with thin golden hairline",
        descHi = "अल्ट्रा क्लीन मॉडर्न मिनिमल डिज़ाइन",
        previewBg = Color(0xFF13271F),
        previewBorder = Color(0xFFE5C158),
        previewAccent = Color(0xFFFEF08A)
    );

    companion object {
        fun fromId(id: String): HadithHeroStyle {
            return entries.firstOrNull { it.id == id } ?: EMERALD_ARCH
        }
    }
}

@Composable
fun HadithHeroDesignModal(
    currentStyle: HadithHeroStyle,
    isDarkTheme: Boolean,
    onSelectStyle: (HadithHeroStyle) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(26.dp)),
            shape = RoundedCornerShape(26.dp),
            color = if (isDarkTheme) Color(0xFF122019) else Color.White,
            border = androidx.compose.foundation.BorderStroke(
                1.5.dp,
                if (isDarkTheme) Color(0xFF1F4335) else Color(0xFFE5DECE)
            ),
            shadowElevation = 16.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE8F5E9))
                                .border(1.dp, Color(0xFF0E5D47), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Palette,
                                contentDescription = null,
                                tint = Color(0xFF0E5D47),
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "Daily Hadith Card Theme",
                                color = if (isDarkTheme) Color.White else Color(0xFF0E5D47),
                                fontSize = 16.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Choose your favorite visual style",
                                color = Color(0xFF9E948A),
                                fontSize = 11.5.sp
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(if (isDarkTheme) Color(0xFF1A3326) else Color(0xFFF3ECE0))
                            .clickable { onDismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = if (isDarkTheme) Color.White else Color(0xFF5D4D38),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Options List
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    HadithHeroStyle.entries.forEach { style ->
                        val isSelected = style == currentStyle

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    if (isSelected) {
                                        if (isDarkTheme) Color(0xFF1A382B) else Color(0xFFF7F4EC)
                                    } else {
                                        if (isDarkTheme) Color(0xFF0C1914) else Color(0xFFFAF9F6)
                                    }
                                )
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) Color(0xFFC9A84C) else Color(0x33C9A84C),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable {
                                    onSelectStyle(style)
                                    onDismiss()
                                }
                                .padding(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    // Visual color badge preview
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(style.previewBg)
                                            .border(1.5.dp, style.previewBorder, RoundedCornerShape(10.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("📖", fontSize = 16.sp)
                                    }

                                    Column {
                                        Text(
                                            text = style.titleHi,
                                            color = if (isDarkTheme) Color.White else Color(0xFF1C1917),
                                            fontSize = 14.5.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = style.descEn,
                                            color = Color(0xFF8C827A),
                                            fontSize = 11.5.sp,
                                            lineHeight = 15.sp
                                        )
                                    }
                                }

                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF0E5D47)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = Color(0xFFE8C878),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(50.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF0E5D47), Color(0xFF083C2D))
                            )
                        )
                        .clickable { onDismiss() }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Done / ठीक है",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.5.sp
                    )
                }
            }
        }
    }
}
