package com.tasbih.digitaltasbih.ui.screens.tasbih

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.tasbih.digitaltasbih.data.AsmaNameItem
import com.tasbih.digitaltasbih.data.PrayerRepository
import com.tasbih.digitaltasbih.data.TasbihLocalization
import com.tasbih.digitaltasbih.data.TasbihStrings
import com.tasbih.digitaltasbih.ui.components.BottomNavigationBar

/**
 * Dedicated full screen for the 99 Names of Allah.
 * Opened directly from the Home screen "99 Names" button (next to Hadith).
 * Displays full, unobstructed 99 Names list with floating flower petals, benefits card,
 * and bottom navigation. Tapping any name directly opens it for Dhikr recital in Tasbih counter.
 */
@Composable
fun NamesOfAllahScreen(
    onBack: () -> Unit,
    onSelectNameForRecital: (AsmaNameItem) -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToTasbih: () -> Unit,
    onNavigateToQuran: () -> Unit,
    onNavigateToCalendar: () -> Unit,
    onNavigateToDua: () -> Unit
) {
    BackHandler { onBack() }

    val selectedLanguage = remember { PrayerRepository.getLanguage() }
    val isNightMode = remember { PrayerRepository.isDarkTheme() }
    val strings: TasbihStrings = remember(selectedLanguage) {
        TasbihLocalization.get(selectedLanguage)
    }

    val bgColor = if (isNightMode) Color(0xFF0F1B16) else Color(0xFFF9F7F2)

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        bottomBar = {
            BottomNavigationBar(
                activeTabId = "tasbih",
                isDarkTheme = isNightMode,
                onTabSelected = { tabId ->
                    when (tabId) {
                        "home" -> onNavigateToHome()
                        "tasbih" -> onNavigateToTasbih()
                        "quran" -> onNavigateToQuran()
                        "calendar" -> onNavigateToCalendar()
                        "dua" -> onNavigateToDua()
                    }
                }
            )
        },
        containerColor = bgColor
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(bgColor)
        ) {
            TasbihNamesOfAllahView(
                isNightMode = isNightMode,
                strings = strings,
                selectedLanguage = selectedLanguage,
                activeTab = 0,
                onBack = onBack,
                onSelectNameForRecital = onSelectNameForRecital
            )
        }
    }
}
