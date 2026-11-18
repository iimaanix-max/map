package com.tasbih.digitaltasbih.ui.screens.dua

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.data.DuaRepository
import com.tasbih.digitaltasbih.ui.components.dua.DuaBottomNav
import com.tasbih.digitaltasbih.ui.components.dua.DuaCompactCard
import com.tasbih.digitaltasbih.ui.components.dua.DuaHeaderBar
import com.tasbih.digitaltasbih.ui.components.dua.DuaLanguageRow
import com.tasbih.digitaltasbih.ui.components.dua.SaveToCollectionSheet
import com.tasbih.digitaltasbih.ui.theme.DuaColors

@Composable
fun DuaCategoryScreen(
    categoryId: String,
    categoryName: String,
    icon: String = "",
    onBack: () -> Unit,
    onOpenDetail: (duaId: String, categoryName: String) -> Unit,
    onNavigateHome: () -> Unit,
    onNavigateMyDua: () -> Unit,
    onNavigateMyLists: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val preferences = DuaRepository.preferences

    val selectedLang by preferences.selectedLanguage.collectAsState()
    val bookmarkedIds by preferences.bookmarkedDuaIds.collectAsState()
    val collections by preferences.customCollections.collectAsState()

    var selectedDuaForCollection by remember { mutableStateOf<String?>(null) }

    val categoryDuas = remember(categoryId) {
        DuaRepository.getDuasByCategory(categoryId)
    }

    val displayTitle = if (categoryName.isNotBlank()) {
        "${if (icon.isNotBlank()) "$icon " else ""}${categoryName.uppercase()}"
    } else {
        val cat = DuaRepository.getCategoryById(categoryId)
        "${cat?.icon ?: "🤲"} ${cat?.label?.uppercase() ?: categoryId.uppercase()}"
    }

    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                DuaHeaderBar(
                    title = displayTitle,
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
            containerColor = DuaColors.Bg
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    DuaLanguageRow(
                        selectedLang = selectedLang,
                        onLangSelected = { preferences.setLanguage(it) }
                    )
                }

                if (categoryDuas.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No Duas found in this category",
                                color = DuaColors.TextMuted,
                                fontSize = 14.5.sp
                            )
                        }
                    }
                } else {
                    itemsIndexed(categoryDuas) { index, dua ->
                        DuaCompactCard(
                            dua = dua,
                            index = index,
                            selectedLang = selectedLang,
                            isBookmarked = bookmarkedIds.contains(dua.id),
                            onCardClick = { onOpenDetail(dua.id, categoryName) },
                            onShareClick = {
                                com.tasbih.digitaltasbih.util.DuaShareHelper.shareDua(context, dua, selectedLang)
                            },
                            onBookmarkClick = {
                                selectedDuaForCollection = dua.id
                                preferences.toggleBookmark(dua.id)
                            }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }

        if (selectedDuaForCollection != null) {
            SaveToCollectionSheet(
                duaId = selectedDuaForCollection!!,
                collections = collections,
                onToggleCollection = { colId ->
                    preferences.toggleDuaInCollection(colId, selectedDuaForCollection!!)
                },
                onCreateCollection = { name ->
                    val newCol = preferences.createCollection(name)
                    preferences.toggleDuaInCollection(newCol.id, selectedDuaForCollection!!)
                },
                onDismiss = { selectedDuaForCollection = null }
            )
        }
    }
}