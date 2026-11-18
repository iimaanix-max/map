package com.tasbih.digitaltasbih.ui.components

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tasbih.digitaltasbih.data.LanguageItem
import com.tasbih.digitaltasbih.data.LocalizationManager
import com.tasbih.digitaltasbih.data.PrayerRepository

/**
 * OnboardingLanguageModal
 * Exact replica of the original first-time user language selection modal:
 * - 🌐 Emerald & Gold header badge with gradient & glow
 * - "Choose Your Language"
 * - Subtitle: "अपनी भाषा चुनें • اپنی زبان منتخب کریں"
 * - "Select your preferred language for prayer times, duas, and daily reminders."
 * - Scrollable 2-column grid of all 24+ languages
 * - Saves selected language, marks onboarding done, and updates home bar
 */
@Composable
fun OnboardingLanguageModal(
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit
) {
    // Collect all languages from groups, with core languages on top
    val allLanguages = remember {
        val uniqueLangs = mutableListOf<LanguageItem>()
        // Top core languages first
        val topCoreCodes = listOf("en", "hi", "ur", "ar", "bn", "gu", "ta", "te", "mr", "pa", "ml", "kn", "hinglish")
        val allFromGroups = LocalizationManager.allLanguageGroups.flatMap { it.languages }

        for (code in topCoreCodes) {
            allFromGroups.find { it.code == code }?.let {
                if (!uniqueLangs.any { u -> u.code == it.code }) uniqueLangs.add(it)
            }
        }
        for (item in allFromGroups) {
            if (!uniqueLangs.any { u -> u.code == item.code }) {
                uniqueLangs.add(item)
            }
        }
        uniqueLangs
    }

    Dialog(
        onDismissRequest = { /* Force explicit user choice on first launch */ },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .shadow(elevation = 20.dp, shape = RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFF3D48A).copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 22.dp, bottom = 18.dp, start = 18.dp, end = 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. Globe Icon Emerald-Gold Badge
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF146450), Color(0xFF0C4A3A))
                            )
                        )
                        .border(1.5.dp, Color(0xFFF3D48A), RoundedCornerShape(18.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🌐", fontSize = 28.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Title: Choose Your Language
                Text(
                    text = "Choose Your Language",
                    fontSize = 17.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0F172A),
                    letterSpacing = 0.2.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Subtitle: अपनी भाषा चुनें • اپنی زبان منتخب کریں
                Text(
                    text = "अपनी भाषा चुनें • اپنی زبان منتخب کریں",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF146450),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Explanation Description
                Text(
                    text = "Select your preferred language for prayer times, duas, and daily reminders.",
                    fontSize = 11.5.sp,
                    lineHeight = 16.sp,
                    color = Color(0xFF64748B),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                // 2. Scrollable 2-Column Language Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 340.dp)
                ) {
                    items(allLanguages) { langItem ->
                        val isSelected = (selectedLanguage == langItem.code)

                        val cardBg = if (isSelected) Color(0xFF146450).copy(alpha = 0.08f) else Color.White
                        val cardBorder = if (isSelected) Color(0xFF146450) else Color(0xFFE2E8F0)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(cardBg)
                                .border(1.2.dp, cardBorder, RoundedCornerShape(12.dp))
                                .clickable {
                                    PrayerRepository.saveLanguage(langItem.code)
                                    PrayerRepository.setOnboardingLangSelected(true)
                                    val core = listOf("en", "hi", "ur", "ar")
                                    if (!core.contains(langItem.code)) {
                                        PrayerRepository.setExtraLangSlot(langItem.code)
                                    }
                                    AppToast.show("🌐 Language set to ${langItem.label}")
                                    onLanguageSelected(langItem.code)
                                }
                                .padding(horizontal = 12.dp, vertical = 11.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = langItem.label,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
                                color = if (isSelected) Color(0xFF146450) else Color(0xFF0F172A),
                                modifier = Modifier.weight(1f, fill = false)
                            )
                            Text(
                                text = langItem.code.uppercase(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF94A3B8)
                            )
                        }
                    }
                }
            }
        }
    }
}
