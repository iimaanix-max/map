package com.tasbih.digitaltasbih.ui.screens.dua

import android.content.Intent
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.data.DuaCustomCollection
import com.tasbih.digitaltasbih.data.DuaRepository
import com.tasbih.digitaltasbih.ui.components.dua.CreateCollectionDialog
import com.tasbih.digitaltasbih.ui.components.dua.DuaBottomNav
import com.tasbih.digitaltasbih.ui.components.dua.DuaCompactCard
import com.tasbih.digitaltasbih.ui.components.dua.DuaHeaderBar
import com.tasbih.digitaltasbih.ui.components.dua.DuaLanguageRow
import com.tasbih.digitaltasbih.ui.components.dua.SaveToCollectionSheet
import com.tasbih.digitaltasbih.ui.theme.DuaColors

@Composable
fun DuaMyListsScreen(
    onBack: () -> Unit,
    onOpenDetail: (duaId: String, categoryName: String) -> Unit,
    onNavigateHome: () -> Unit,
    onNavigateMyDua: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val preferences = DuaRepository.preferences

    val selectedLang by preferences.selectedLanguage.collectAsState()
    val bookmarkedIds by preferences.bookmarkedDuaIds.collectAsState()
    val collections by preferences.customCollections.collectAsState()

    var showCreateDialog by remember { mutableStateOf(false) }
    var selectedCollectionForView by remember { mutableStateOf<DuaCustomCollection?>(null) }
    var selectedDuaForCollection by remember { mutableStateOf<String?>(null) }

    val allDuas = remember { DuaRepository.getAllDuas() }
    val bookmarkedDuas = remember(bookmarkedIds) {
        allDuas.filter { bookmarkedIds.contains(it.id) }
    }

    Scaffold(
        topBar = {
            val listTitle = if (selectedCollectionForView != null) {
                "📁 ${selectedCollectionForView!!.name.uppercase()}"
            } else {
                when (selectedLang) {
                    "hi" -> "📑 मेरी दुआ लिस्ट"
                    "ur" -> "📑 میری دعا لسٹ"
                    else -> "📑 MY DUA COLLECTIONS"
                }
            }
            DuaHeaderBar(
                title = listTitle,
                onBack = {
                    if (selectedCollectionForView != null) {
                        selectedCollectionForView = null
                    } else {
                        onBack()
                    }
                },
                onFavoritesClick = null,
                onSettingsClick = {}
            )
        },
        bottomBar = {
            DuaBottomNav(
                activeTab = "my_lists",
                onTabSelected = { tabId ->
                    when (tabId) {
                        "home" -> onNavigateHome()
                        "my_dua" -> onNavigateMyDua()
                        "my_lists" -> { selectedCollectionForView = null }
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

            if (selectedCollectionForView != null) {
                // View Duas inside a selected collection
                val col = selectedCollectionForView!!
                val collectionDuas = allDuas.filter { col.duaIds.contains(it.id) }

                if (collectionDuas.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        val emptyColText = when (selectedLang) {
                            "hi" -> "इस लिस्ट में अभी कोई दुआ नहीं है।\nदुआओं को सेव करके यहां जोड़ें!"
                            "ur" -> "اس لسٹ میں ابھی کوئی دعا نہیں ہے۔\nدعاؤں کو محفوظ کر کے یہاں شامل کریں!"
                            else -> "No Duas in this collection yet.\nBookmark Duas to add them here!"
                        }
                        Text(
                            text = emptyColText,
                            color = DuaColors.TextMuted,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(collectionDuas) { index, dua ->
                            DuaCompactCard(
                                dua = dua,
                                index = index,
                                selectedLang = selectedLang,
                                isBookmarked = bookmarkedIds.contains(dua.id),
                                onCardClick = { onOpenDetail(dua.id, col.name) },
                                onShareClick = {
                                    com.tasbih.digitaltasbih.util.DuaShareHelper.shareDua(context, dua, selectedLang)
                                },
                                onBookmarkClick = {
                                    selectedDuaForCollection = dua.id
                                }
                            )
                        }
                    }
                }
            } else {
                // "+ Create New List" Button matching Screenshot 20
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .shadow(4.dp, RoundedCornerShape(14.dp))
                        .clip(RoundedCornerShape(14.dp))
                        .background(DuaColors.Emerald)
                        .clickable { showCreateDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    val createBtnText = when (selectedLang) {
                        "hi" -> "+ नई लिस्ट बनाएं"
                        "ur" -> "+ نئی لسٹ بنائیں"
                        else -> "+ Create New List"
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "📁 ", fontSize = 16.sp)
                        Text(
                            text = createBtnText,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (collections.isEmpty() && bookmarkedDuas.isEmpty()) {
                    // Empty state matching Screenshot 20
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        val noColTitle = when (selectedLang) {
                            "hi" -> "कोई लिस्ट मौजूद नहीं है"
                            "ur" -> "کوئی لسٹ موجود نہیں ہے"
                            else -> "No Collections Yet"
                        }
                        val noColSub = when (selectedLang) {
                            "hi" -> "अक्सर पढ़ी जाने वाली दुआओं के लिए अपनी पसंदीदा लिस्ट बनाएं।"
                            "ur" -> "اکثر پڑھی جانے والی دعاؤں کے لیے اپنی پسندیدہ لسٹ بنائیں۔"
                            else -> "Create named collections for your frequently recited duas."
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        ) {
                            Text(text = "📁", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = noColTitle,
                                color = DuaColors.TextDark,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = noColSub,
                                color = DuaColors.TextMuted,
                                fontSize = 13.5.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Custom Collections
                        if (collections.isNotEmpty()) {
                            item {
                                val myListsHeader = when (selectedLang) {
                                    "hi" -> "मेरी लिस्ट्स (${collections.size})"
                                    "ur" -> "میری لسٹس (${collections.size})"
                                    else -> "My Lists (${collections.size})"
                                }
                                Text(
                                    text = myListsHeader,
                                    color = DuaColors.Emerald,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            items(collections) { col ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .shadow(3.dp, RoundedCornerShape(16.dp))
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color.White)
                                        .border(1.dp, Color(0x26C9A84C), RoundedCornerShape(16.dp))
                                        .clickable { selectedCollectionForView = col }
                                        .padding(14.dp)
                                    ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(text = "📁", fontSize = 22.sp)
                                            Spacer(modifier = Modifier.padding(horizontal = 6.dp))
                                            Column {
                                                Text(
                                                    text = col.name,
                                                    color = DuaColors.TextDark,
                                                    fontSize = 15.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                val duasCountText = when (selectedLang) {
                                                    "hi" -> "${col.duaIds.size} दुआएं"
                                                    "ur" -> "${col.duaIds.size} دعائیں"
                                                    else -> "${col.duaIds.size} duas"
                                                }
                                                Text(
                                                    text = duasCountText,
                                                    color = DuaColors.Gold,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                        }

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(28.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(0xFFFFEBEE))
                                                    .clickable { preferences.deleteCollection(col.id) },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(text = "🗑️", fontSize = 11.sp)
                                            }
                                            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                                            Text(text = "›", color = DuaColors.TextMuted, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }

                        // Bookmarked Duas Section
                        if (bookmarkedDuas.isNotEmpty()) {
                            item {
                                val bookmarkedHeader = when (selectedLang) {
                                    "hi" -> "सेव की गई दुआएं (${bookmarkedDuas.size})"
                                    "ur" -> "محفوظ شدہ دعائیں (${bookmarkedDuas.size})"
                                    else -> "Bookmarked Duas (${bookmarkedDuas.size})"
                                }
                                Text(
                                    text = bookmarkedHeader,
                                    color = DuaColors.Emerald,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(top = 10.dp)
                                )
                            }

                            itemsIndexed(bookmarkedDuas) { index, dua ->
                                DuaCompactCard(
                                    dua = dua,
                                    index = index,
                                    selectedLang = selectedLang,
                                    isBookmarked = true,
                                    onCardClick = { onOpenDetail(dua.id, "Favorites") },
                                    onShareClick = {
                                        com.tasbih.digitaltasbih.util.DuaShareHelper.shareDua(context, dua, selectedLang)
                                    },
                                    onBookmarkClick = {
                                        preferences.toggleBookmark(dua.id)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showCreateDialog) {
        CreateCollectionDialog(
            selectedLang = selectedLang,
            onDismiss = { showCreateDialog = false },
            onCreate = { name ->
                preferences.createCollection(name)
                showCreateDialog = false
            }
        )
    }

    if (selectedDuaForCollection != null) {
        SaveToCollectionSheet(
            duaId = selectedDuaForCollection!!,
            collections = collections,
            selectedLang = selectedLang,
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