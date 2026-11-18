package com.tasbih.digitaltasbih.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.data.LanguageItem
import com.tasbih.digitaltasbih.ui.theme.EmeraldPrimary

@Composable
fun LanguageBar(
    languages: List<LanguageItem>,
    selectedCode: String,
    onSelectLanguage: (String) -> Unit,
    onOpenMoreLanguages: () -> Unit,
    isDarkTheme: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp)
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(vertical = 4.dp),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(languages) { lang ->
            val isSelected = lang.code == selectedCode

            val itemBg = if (isSelected) {
                Color(0xFF0F5132)
            } else if (isDarkTheme) {
                Color(0xFF132820)
            } else {
                Color.White
            }

            val itemBorder = if (isSelected) {
                Color(0xFF0F5132)
            } else if (isDarkTheme) {
                Color(0xFF1E3D32)
            } else {
                Color(0x33A07828)
            }

            val textColor = if (isSelected) {
                Color.White
            } else if (isDarkTheme) {
                Color(0xFFE2E8F0)
            } else {
                Color(0xFF14241E)
            }

            Box(
                modifier = Modifier
                    .height(29.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(itemBg)
                    .border(
                        width = 1.2.dp,
                        color = itemBorder,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable { onSelectLanguage(lang.code) }
                    .padding(horizontal = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = lang.label,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                    color = textColor
                )
            }
        }

        // More Languages Pill: 🌐 +20
        item {
            val moreBg = if (isDarkTheme) Color(0xFF082F49) else Color(0xFFF0F9FF)
            val moreBorder = if (isDarkTheme) Color(0xFF0369A1) else Color(0xFF38BDF8)
            val moreText = if (isDarkTheme) Color(0xFF7DD3FC) else Color(0xFF0284C7)

            Box(
                modifier = Modifier
                    .height(29.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(moreBg)
                    .border(
                        width = 1.2.dp,
                        color = moreBorder,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable(onClick = onOpenMoreLanguages)
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🌐", fontSize = 11.5.sp)
                    Text(
                        text = " +20",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = moreText
                    )
                }
            }
        }
    }
}
