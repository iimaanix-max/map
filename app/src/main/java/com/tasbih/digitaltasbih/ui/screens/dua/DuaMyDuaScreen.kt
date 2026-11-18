package com.tasbih.digitaltasbih.ui.screens.dua

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.data.DuaRepository
import com.tasbih.digitaltasbih.data.UserPersonalDua
import com.tasbih.digitaltasbih.ui.components.dua.DuaBottomNav
import com.tasbih.digitaltasbih.ui.components.dua.DuaHeaderBar
import com.tasbih.digitaltasbih.ui.components.dua.DuaLanguageRow
import com.tasbih.digitaltasbih.ui.components.dua.WritePersonalDuaDialog
import com.tasbih.digitaltasbih.ui.theme.DuaColors

@Composable
fun DuaMyDuaScreen(
    onBack: () -> Unit,
    onNavigateHome: () -> Unit,
    onNavigateMyLists: () -> Unit,
    modifier: Modifier = Modifier
) {
    val preferences = DuaRepository.preferences
    val selectedLang by preferences.selectedLanguage.collectAsState()
    val personalDuas by preferences.personalDuas.collectAsState()

    var showWriteDialog by remember { mutableStateOf(false) }
    var editingDua by remember { mutableStateOf<UserPersonalDua?>(null) }

    Scaffold(
        topBar = {
            DuaHeaderBar(
                title = "✍️ MY PERSONAL DUAS",
                onBack = onBack,
                onFavoritesClick = onNavigateMyLists,
                onSettingsClick = {}
            )
        },
        bottomBar = {
            DuaBottomNav(
                activeTab = "my_dua",
                onTabSelected = { tabId ->
                    when (tabId) {
                        "home" -> onNavigateHome()
                        "my_dua" -> { /* already here */ }
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
                .padding(horizontal = 14.dp)
        ) {
            DuaLanguageRow(
                selectedLang = selectedLang,
                onLangSelected = { preferences.setLanguage(it) },
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // "+ Write New Dua" Button matching Screenshot 18
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .shadow(4.dp, RoundedCornerShape(14.dp))
                    .clip(RoundedCornerShape(14.dp))
                    .background(DuaColors.Emerald)
                    .clickable {
                        editingDua = null
                        showWriteDialog = true
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "✍️ ", fontSize = 16.sp)
                    Text(
                        text = "+ Write New Dua",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (personalDuas.isEmpty()) {
                // Empty state matching Screenshot 18
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    ) {
                        Text(text = "✍️", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "No Personal Duas Yet",
                            color = DuaColors.TextDark,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Write your personal heartfelt prayers in your own words, saved permanently on your device.",
                            color = DuaColors.TextMuted,
                            fontSize = 13.5.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 19.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(personalDuas) { dua ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(3.dp, RoundedCornerShape(16.dp))
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                                .border(1.dp, Color(0x220E5D47), RoundedCornerShape(16.dp))
                                .padding(14.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = dua.title,
                                        color = DuaColors.TextDark,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.weight(1f)
                                    )

                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Box(
                                            modifier = Modifier
                                                .size(30.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFF9F7F2))
                                                .clickable {
                                                    editingDua = dua
                                                    showWriteDialog = true
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(text = "✏️", fontSize = 12.sp)
                                        }

                                        Box(
                                            modifier = Modifier
                                                .size(30.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFFFEBEE))
                                                .clickable {
                                                    preferences.deletePersonalDua(dua.id)
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(text = "🗑️", fontSize = 12.sp)
                                        }
                                    }
                                }

                                if (dua.arabic.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = dua.arabic,
                                        color = DuaColors.ArabicGreen,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Serif,
                                        textAlign = TextAlign.Right,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = dua.prayer,
                                    color = DuaColors.TextDark,
                                    fontSize = 13.5.sp,
                                    lineHeight = 19.sp
                                )

                                if (dua.transliteration.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Pronunciation: ${dua.transliteration}",
                                        color = DuaColors.TextMuted,
                                        fontSize = 12.sp
                                    )
                                }

                                if (dua.note.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Note: ${dua.note}",
                                        color = DuaColors.Gold,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showWriteDialog) {
        WritePersonalDuaDialog(
            initialDua = editingDua,
            onDismiss = {
                showWriteDialog = false
                editingDua = null
            },
            onSave = { savedDua ->
                preferences.savePersonalDua(savedDua)
                showWriteDialog = false
                editingDua = null
            }
        )
    }
}