package com.tasbih.digitaltasbih.ui.screens.dua

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tasbih.digitaltasbih.data.DuaFeelingItem
import com.tasbih.digitaltasbih.data.DuaRepository
import com.tasbih.digitaltasbih.ui.components.dua.DuaBottomNav
import com.tasbih.digitaltasbih.ui.components.dua.DuaFeelingCard
import com.tasbih.digitaltasbih.ui.components.dua.DuaHeaderBar
import com.tasbih.digitaltasbih.ui.components.dua.DuaLanguageRow
import com.tasbih.digitaltasbih.ui.theme.DuaColors

@Composable
fun DuaAllFeelingsScreen(
    onBack: () -> Unit,
    onFeelingClick: (DuaFeelingItem) -> Unit,
    onNavigateHome: () -> Unit,
    onNavigateMyDua: () -> Unit,
    onNavigateMyLists: () -> Unit,
    modifier: Modifier = Modifier
) {
    val preferences = DuaRepository.preferences
    val selectedLang by preferences.selectedLanguage.collectAsState()
    val feelings = remember { DuaRepository.getAllFeelings() }

    Scaffold(
        topBar = {
            DuaHeaderBar(
                title = "HOW ARE YOU FEELING?",
                onBack = onBack,
                onFavoritesClick = onNavigateMyLists,
                onSettingsClick = {}
            )
        },
        bottomBar = {
            DuaBottomNav(
                activeTab = "",
                onTabSelected = { tabId ->
                    when (tabId) {
                        "home" -> onNavigateHome()
                        "my_dua" -> onNavigateMyDua()
                        "my_lists" -> onNavigateMyLists()
                    }
                }
            )
        },
        containerColor = DuaColors.Bg,
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            DuaLanguageRow(
                selectedLang = selectedLang,
                onLangSelected = { preferences.setLanguage(it) },
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                contentPadding = PaddingValues(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(feelings) { feeling ->
                    DuaFeelingCard(
                        item = feeling,
                        selectedLang = selectedLang,
                        onClick = { onFeelingClick(feeling) }
                    )
                }
            }
        }
    }
}