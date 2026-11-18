package com.tasbih.digitaltasbih.ui.components.dua

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tasbih.digitaltasbih.data.LanguageItem
import com.tasbih.digitaltasbih.data.LocalizationManager
import com.tasbih.digitaltasbih.data.PrayerRepository
import com.tasbih.digitaltasbih.ui.components.LanguageBar
import com.tasbih.digitaltasbih.ui.components.MoreLanguagesModal

data class DuaLangItem(val code: String, val label: String)

@Composable
fun DuaLanguageRow(
    selectedLang: String,
    onLangSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = PrayerRepository.isDarkTheme()
) {
    var isMoreLangOpen by remember { mutableStateOf(false) }

    // Visible languages on the top bar dynamically includes user chosen language in 5th slot (Exact Home Screen layout)
    val visibleLanguages = remember(selectedLang) {
        val coreCodes = listOf("en", "hi", "ur", "ar")
        val allFromGroups = LocalizationManager.allLanguageGroups.flatMap { it.languages }
        val list = mutableListOf<LanguageItem>()
        for (code in coreCodes) {
            allFromGroups.find { it.code == code }?.let { list.add(it) }
        }
        val fifthCode = if (!coreCodes.contains(selectedLang)) {
            selectedLang
        } else {
            PrayerRepository.getExtraLangSlot()
        }
        allFromGroups.find { it.code == fifthCode }?.let {
            if (!list.any { item -> item.code == it.code }) {
                list.add(it)
            }
        }
        if (list.size < 5) {
            allFromGroups.find { it.code == "bn" }?.let {
                if (!list.any { item -> item.code == it.code }) list.add(it)
            }
        }
        list
    }

    Box(modifier = modifier.fillMaxWidth()) {
        LanguageBar(
            languages = visibleLanguages,
            selectedCode = selectedLang,
            isDarkTheme = isDarkTheme,
            contentPadding = PaddingValues(horizontal = 2.dp),
            onSelectLanguage = { code ->
                onLangSelected(code)
                PrayerRepository.saveLanguage(code)
                if (!listOf("en", "hi", "ur", "ar").contains(code)) {
                    PrayerRepository.setExtraLangSlot(code)
                }
            },
            onOpenMoreLanguages = {
                isMoreLangOpen = true
            }
        )

        // Full 28+ Languages BottomSheet Modal (Triggered by 🌐 +20, Exact Home Screen Modal)
        if (isMoreLangOpen) {
            MoreLanguagesModal(
                selectedCode = selectedLang,
                onSelectLanguage = { code ->
                    onLangSelected(code)
                    PrayerRepository.saveLanguage(code)
                    if (!listOf("en", "hi", "ur", "ar").contains(code)) {
                        PrayerRepository.setExtraLangSlot(code)
                    }
                    isMoreLangOpen = false
                },
                onDismiss = {
                    isMoreLangOpen = false
                }
            )
        }
    }
}