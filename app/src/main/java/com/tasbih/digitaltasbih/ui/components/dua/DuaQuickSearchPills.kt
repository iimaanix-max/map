package com.tasbih.digitaltasbih.ui.components.dua

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.tasbih.digitaltasbih.ui.theme.DuaColors

data class DuaSearchPillItem(
    val id: String,
    val icon: String,
    val labelEn: String,
    val labelHi: String,
    val labelUr: String,
    val searchQuery: String
) {
    fun getLabel(lang: String): String = when (lang) {
        "hi" -> labelHi
        "ur" -> labelUr
        else -> labelEn
    }
}

val PRESET_SEARCH_PILLS = listOf(
    DuaSearchPillItem("rabbana", "🤲", "Rabbana", "रब्बना", "ربنا", "rabbana"),
    DuaSearchPillItem("protection", "🛡️", "Protection", "हिफ़ाज़त", "حفاظت", "protection"),
    DuaSearchPillItem("forgiveness", "🕊️", "Forgiveness", "मग़फ़िरत", "مغفرت", "forgiveness"),
    DuaSearchPillItem("parents", "👨‍👩‍👦", "Parents", "वालिदैन", "والدین", "parents"),
    DuaSearchPillItem("morning_evening", "🌅", "Morning & Eve", "सुबह-शाम", "صبح و شام", "morning"),
    DuaSearchPillItem("shifa", "🌿", "Shifa / Health", "शिफ़ा व सेहत", "شفاء", "shifa"),
    DuaSearchPillItem("rizq", "💰", "Rizq & Debt", "रिज़्क़ व क़र्ज़", "رزق", "rizq"),
    DuaSearchPillItem("anxiety", "🧘", "Peace of Mind", "फ़िक्र व सुकून", "تسکین", "anxiety"),
    DuaSearchPillItem("travel", "✈️", "Travel", "सफ़र", "سفر", "travel")
)

@Composable
fun DuaQuickSearchPills(
    selectedPillId: String?,
    selectedLang: String,
    onPillSelected: (DuaSearchPillItem?) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PRESET_SEARCH_PILLS.forEach { pill ->
            val isSelected = pill.id == selectedPillId

            val bgBrush = if (isSelected) {
                Brush.horizontalGradient(
                    colors = listOf(
                        DuaColors.Emerald,
                        DuaColors.EmeraldDark
                    )
                )
            } else {
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFF9F8F5),
                        Color(0xFFF3EFE6)
                    )
                )
            }

            val borderColor = if (isSelected) DuaColors.Gold else Color(0x33C9A84C)
            val textColor = if (isSelected) Color.White else DuaColors.TextDark

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(bgBrush)
                    .border(1.dp, borderColor, RoundedCornerShape(20.dp))
                    .clickable {
                        if (isSelected) {
                            onPillSelected(null)
                        } else {
                            onPillSelected(pill)
                        }
                    }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(text = pill.icon, fontSize = 12.sp)
                    Text(
                        text = pill.getLabel(selectedLang),
                        color = textColor,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                    if (isSelected) {
                        Text(
                            text = "✕",
                            color = DuaColors.GoldLight,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
