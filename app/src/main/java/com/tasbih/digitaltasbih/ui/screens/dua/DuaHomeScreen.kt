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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.rememberLazyListState
import kotlinx.coroutines.delay

import com.tasbih.digitaltasbih.data.DuaCategoryItem
import com.tasbih.digitaltasbih.data.DuaEntry
import com.tasbih.digitaltasbih.data.DuaFeelingItem
import com.tasbih.digitaltasbih.data.DuaRepository
import com.tasbih.digitaltasbih.ui.components.dua.DuaBottomNav
import com.tasbih.digitaltasbih.ui.components.dua.DuaCategoriesSection
import com.tasbih.digitaltasbih.ui.components.dua.DuaCompactCard
import com.tasbih.digitaltasbih.ui.components.dua.DuaFeelingsSection
import com.tasbih.digitaltasbih.ui.components.dua.DuaHadithRefModal
import com.tasbih.digitaltasbih.ui.components.dua.DuaHeaderBar
import com.tasbih.digitaltasbih.ui.components.dua.DuaInspirationBanner
import com.tasbih.digitaltasbih.ui.components.dua.DuaLanguageRow
import com.tasbih.digitaltasbih.ui.components.dua.DuaQuickAccessRow
import com.tasbih.digitaltasbih.ui.components.dua.DuaQuickSearchPills
import com.tasbih.digitaltasbih.ui.components.dua.DuaSearchBar
import com.tasbih.digitaltasbih.ui.components.dua.DuaSpotlightTourModal
import com.tasbih.digitaltasbih.ui.components.dua.SaveToCollectionSheet
import com.tasbih.digitaltasbih.ui.theme.DuaColors

@Composable
fun DuaHomeScreen(
    onBack: () -> Unit,
    onOpenCategory: (categoryId: String, categoryName: String) -> Unit,
    onOpenDetail: (duaId: String, categoryName: String) -> Unit,
    onOpenAllFeelings: () -> Unit,
    onOpenAllCategories: () -> Unit,
    onOpenMyDua: () -> Unit,
    onOpenMyLists: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val preferences = DuaRepository.preferences

    val selectedLang by preferences.selectedLanguage.collectAsState()
    val isHomeTourDone by preferences.isHomeTourDone.collectAsState()
    val bookmarkedIds by preferences.bookmarkedDuaIds.collectAsState()
    val collections by preferences.customCollections.collectAsState()
    val recentDuaIds by preferences.recentDuaIds.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedPillId by remember { mutableStateOf<String?>(null) }
    var tourStep by remember { mutableIntStateOf(1) }
    var selectedDuaForCollection by remember { mutableStateOf<String?>(null) }
    var selectedRefDua by remember { mutableStateOf<DuaEntry?>(null) }

    // Active View Filter: "all" (Standard), "popular" (Popular Duas View), "recent" (Recently Read Duas View)
    var activeHomeView by remember { mutableStateOf("all") }

    val lazyListState = rememberLazyListState()
    var rootCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var quickAccessCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var feelingsCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var categoriesCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var myDuaCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var myListsCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }

    val currentTargetRect: Rect? = remember(
        isHomeTourDone,
        tourStep,
        rootCoords,
        quickAccessCoords,
        feelingsCoords,
        categoriesCoords,
        myDuaCoords,
        myListsCoords,
        lazyListState.firstVisibleItemIndex,
        lazyListState.firstVisibleItemScrollOffset
    ) {
        val root = rootCoords
        if (root != null && root.isAttached) {
            val target = myListsCoords
            if (target != null && target.isAttached) {
                try {
                    val box = root.localBoundingBoxOf(target, clipBounds = false)
                    if (box.width > 0f && box.height > 0f) box else null
                } catch (_: Exception) {
                    null
                }
            } else null
        } else null
    }

    LaunchedEffect(!isHomeTourDone, tourStep) {
        if (!isHomeTourDone) {
            when (tourStep) {
                1 -> {
                    delay(200L)
                    try {
                        lazyListState.animateScrollToItem(index = 0, scrollOffset = 0)
                    } catch (_: Exception) {}
                }
                2 -> {
                    delay(150L)
                    try {
                        lazyListState.animateScrollToItem(index = 4, scrollOffset = -80)
                    } catch (_: Exception) {}
                }
                3 -> {
                    delay(150L)
                    try {
                        lazyListState.animateScrollToItem(index = 5, scrollOffset = -80)
                    } catch (_: Exception) {}
                }
            }
        }
    }

    val quickAccessItems = remember { DuaRepository.getQuickAccessItems() }
    val feelings by DuaRepository.homeFeelings.collectAsState()
    val categories by DuaRepository.homeCategories.collectAsState()
    val popularDuas = remember { DuaRepository.getPopularDuas() }
    val recentDuas = remember(recentDuaIds) { DuaRepository.getRecentDuas() }

    LaunchedEffect(Unit) {
        if (feelings.isEmpty()) {
            DuaRepository.shuffleHomeContent()
        }
    }

    val searchResults = remember(searchQuery, selectedLang) {
        if (searchQuery.isNotBlank()) {
            DuaRepository.searchDuas(searchQuery, selectedLang)
        } else {
            emptyList()
        }
    }

    BackHandler {
        if (!isHomeTourDone) {
            preferences.markHomeTourDone()
        } else if (searchQuery.isNotBlank()) {
            searchQuery = ""
        } else {
            onBack()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned { rootCoords = it }
    ) {
        Scaffold(
            topBar = {
                DuaHeaderBar(
                    title = "DUA",
                    onBack = onBack,
                    onFavoritesClick = onOpenMyLists,
                    onSettingsClick = {
                        tourStep = 1
                        preferences.resetHomeTour()
                    }
                )
            },
            bottomBar = {
                DuaBottomNav(
                    activeTab = "home",
                    onTabSelected = { tabId ->
                        when (tabId) {
                            "home" -> {
                                searchQuery = ""
                                selectedPillId = null
                                activeHomeView = "all"
                                DuaRepository.shuffleHomeContent()
                            }
                            "my_dua" -> onOpenMyDua()
                            "my_lists" -> onOpenMyLists()
                        }
                    },
                    onTabPositioned = { tabId, coords ->
                        if (tabId == "my_dua") {
                            myDuaCoords = coords
                        } else if (tabId == "my_lists") {
                            myListsCoords = coords
                        }
                    }
                )
            },
            containerColor = DuaColors.Bg
        ) { paddingValues ->
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // 1. Language Row
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    DuaLanguageRow(
                        selectedLang = selectedLang,
                        onLangSelected = { preferences.setLanguage(it) }
                    )
                }

                // 2. Search Bar
                item {
                    DuaSearchBar(
                        query = searchQuery,
                        onQueryChange = {
                            searchQuery = it
                            if (it.isBlank()) {
                                selectedPillId = null
                            }
                        }
                    )
                }

                // 3. Quick Search Pills (quickSearchPills)
                item {
                    DuaQuickSearchPills(
                        selectedPillId = selectedPillId,
                        selectedLang = selectedLang,
                        onPillSelected = { pill ->
                            selectedPillId = pill?.id
                            searchQuery = pill?.searchQuery ?: ""
                        }
                    )
                }

                // 4. View Switcher Tabs (All | Popular 🔥 | Recently Read 🕒)
                if (searchQuery.isBlank()) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFEDE9DF))
                                .padding(3.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            listOf(
                                Triple("all", "🌟 All", "🌟 सभी"),
                                Triple("popular", "🔥 Popular", "🔥 मशहूर दुआएं"),
                                Triple("recent", "🕒 Recents (${recentDuas.size})", "🕒 हालिया (${recentDuas.size})")
                            ).forEach { (viewKey, labelEn, labelHi) ->
                                val isActive = activeHomeView == viewKey
                                val label = if (selectedLang == "hi") labelHi else labelEn

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(11.dp))
                                        .background(
                                            if (isActive) DuaColors.Emerald else Color.Transparent
                                        )
                                        .clickable { activeHomeView = viewKey }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        color = if (isActive) Color.White else DuaColors.TextDark,
                                        fontSize = 12.sp,
                                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }

                if (searchQuery.isNotBlank()) {
                    // Search Results
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val searchHeader = when (selectedLang) {
                                "hi" -> "खोज परिणाम (${searchResults.size})"
                                "ur" -> "تلاش کے نتائج (${searchResults.size})"
                                else -> "Search Results (${searchResults.size})"
                            }
                            val clearText = when (selectedLang) {
                                "hi" -> "हटाएं"
                                "ur" -> "صاف کریں"
                                else -> "Clear"
                            }
                            Text(
                                text = searchHeader,
                                color = DuaColors.Emerald,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )

                            Text(
                                text = clearText,
                                color = DuaColors.Gold,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable {
                                    searchQuery = ""
                                    selectedPillId = null
                                }
                            )
                        }
                    }

                    if (searchResults.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No Duas found for \"$searchQuery\"",
                                    color = DuaColors.TextMuted,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    } else {
                        itemsIndexed(searchResults) { index, dua ->
                            DuaCompactCard(
                                dua = dua,
                                index = index,
                                selectedLang = selectedLang,
                                isBookmarked = bookmarkedIds.contains(dua.id),
                                onCardClick = { onOpenDetail(dua.id, dua.cat) },
                                onRefClick = { selectedRefDua = dua },
                                onShareClick = {
                                    com.tasbih.digitaltasbih.util.DuaShareHelper.shareDua(context, dua, selectedLang)
                                },
                                onBookmarkClick = {
                                    selectedDuaForCollection = dua.id
                                    preferences.toggleBookmark(dua.id)
                                }
                            )
                        }
                    }
                } else if (activeHomeView == "popular") {
                    // DEDICATED POPULAR VIEW (openDuaPopularView)
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = when (selectedLang) {
                                        "hi" -> "🔥 सर्वाधिक पढ़ी जाने वाली दुआएं"
                                        "ur" -> "🔥 سب سے زیادہ پڑھی جانے والی دعائیں"
                                        else -> "🔥 Most Popular Duas"
                                    },
                                    color = DuaColors.Emerald,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Globally most recited authentic supplications",
                                    color = DuaColors.TextMuted,
                                    fontSize = 12.sp
                                )
                            }

                            Text(
                                text = "✕ Close",
                                color = DuaColors.Gold,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable { activeHomeView = "all" }
                            )
                        }
                    }

                    itemsIndexed(popularDuas) { index, dua ->
                        DuaCompactCard(
                            dua = dua,
                            index = index,
                            selectedLang = selectedLang,
                            isBookmarked = bookmarkedIds.contains(dua.id),
                            onCardClick = { onOpenDetail(dua.id, dua.cat) },
                            onRefClick = { selectedRefDua = dua },
                            onShareClick = {
                                com.tasbih.digitaltasbih.util.DuaShareHelper.shareDua(context, dua, selectedLang)
                            },
                            onBookmarkClick = {
                                selectedDuaForCollection = dua.id
                                preferences.toggleBookmark(dua.id)
                            }
                        )
                    }
                } else if (activeHomeView == "recent") {
                    // DEDICATED RECENT DUAS HISTORY VIEW (openDuaRecentsView)
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = when (selectedLang) {
                                        "hi" -> "🕒 हाल ही में पढ़ी गई दुआएं"
                                        "ur" -> "🕒 حال ہی میں پڑھی گئی دعائیں"
                                        else -> "🕒 Recently Read Duas"
                                    },
                                    color = DuaColors.Emerald,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${recentDuas.size} Duas in your reading history",
                                    color = DuaColors.TextMuted,
                                    fontSize = 12.sp
                                )
                            }

                            if (recentDuas.isNotEmpty()) {
                                Text(
                                    text = "Clear All",
                                    color = Color(0xFFC62828),
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.clickable {
                                        preferences.clearRecentDuas()
                                    }
                                )
                            }
                        }
                    }

                    if (recentDuas.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "📖", fontSize = 36.sp)
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = when (selectedLang) {
                                            "hi" -> "अभी कोई हालिया दुआ नहीं है"
                                            "ur" -> "ابھی کوئی حالیہ دعا نہیں ہے"
                                            else -> "No recently read Duas yet"
                                        },
                                        color = DuaColors.TextDark,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Any Dua you open and read will appear here.",
                                        color = DuaColors.TextMuted,
                                        fontSize = 12.5.sp
                                    )
                                }
                            }
                        }
                    } else {
                        itemsIndexed(recentDuas) { index, dua ->
                            DuaCompactCard(
                                dua = dua,
                                index = index,
                                selectedLang = selectedLang,
                                isBookmarked = bookmarkedIds.contains(dua.id),
                                onCardClick = { onOpenDetail(dua.id, dua.cat) },
                                onRefClick = { selectedRefDua = dua },
                                onShareClick = {
                                    com.tasbih.digitaltasbih.util.DuaShareHelper.shareDua(context, dua, selectedLang)
                                },
                                onBookmarkClick = {
                                    selectedDuaForCollection = dua.id
                                    preferences.toggleBookmark(dua.id)
                                }
                            )
                        }
                    }
                } else {
                    // STANDARD HOME VIEW

                    // Quick Access Row
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { quickAccessCoords = it }
                        ) {
                            DuaQuickAccessRow(
                                items = quickAccessItems,
                                onItemClick = { item ->
                                    onOpenCategory(item.categoryId, item.title)
                                },
                                onViewAllClick = onOpenAllCategories
                            )
                        }
                    }

                    // Daily Inspiration Card
                    item {
                        DuaInspirationBanner()
                    }

                    // Recently Read Quick Carousel (if history exists)
                    if (recentDuas.isNotEmpty()) {
                        item {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = "🕒 ", fontSize = 14.sp)
                                        Text(
                                            text = when (selectedLang) {
                                                "hi" -> "हालिया पढ़ी गई"
                                                "ur" -> "حالیہ دعائیں"
                                                else -> "Recently Read"
                                            },
                                            color = DuaColors.Emerald,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Text(
                                        text = "View all ›",
                                        color = DuaColors.Gold,
                                        fontSize = 12.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.clickable { activeHomeView = "recent" }
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    items(recentDuas.take(8)) { dua ->
                                        Box(
                                            modifier = Modifier
                                                .width(200.dp)
                                                .clip(RoundedCornerShape(14.dp))
                                                .background(Color.White)
                                                .border(1.dp, Color(0x33C9A84C), RoundedCornerShape(14.dp))
                                                .clickable { onOpenDetail(dua.id, dua.cat) }
                                                .padding(12.dp)
                                        ) {
                                            Column {
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(10.dp))
                                                        .background(Color(0xFFE8F5E9))
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text(
                                                        text = dua.cat.replaceFirstChar { it.uppercase() },
                                                        color = DuaColors.Emerald,
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }

                                                Spacer(modifier = Modifier.height(6.dp))

                                                Text(
                                                    text = dua.getLocalizedTitle(selectedLang),
                                                    color = DuaColors.TextDark,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    maxLines = 2,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // How are you feeling?
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { feelingsCoords = it }
                        ) {
                            DuaFeelingsSection(
                                feelings = feelings,
                                selectedLang = selectedLang,
                                onFeelingClick = { feel ->
                                    onOpenCategory(feel.categoryId, feel.getLocalizedLabel(selectedLang))
                                },
                                onViewAllClick = onOpenAllFeelings
                            )
                        }
                    }

                    // Browse Categories
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { categoriesCoords = it }
                        ) {
                            DuaCategoriesSection(
                                categories = categories,
                                onCategoryClick = { cat ->
                                    onOpenCategory(cat.id, cat.label)
                                },
                                onViewAllClick = onOpenAllCategories
                            )
                        }
                    }

                    // Popular Duas Header
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Popular Duas",
                                color = DuaColors.Emerald,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "View all ›",
                                color = DuaColors.Gold,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable { activeHomeView = "popular" }
                            )
                        }
                    }

                    // Popular Duas Preview List
                    itemsIndexed(popularDuas.take(6)) { index, dua ->
                        DuaCompactCard(
                            dua = dua,
                            index = index,
                            selectedLang = selectedLang,
                            isBookmarked = bookmarkedIds.contains(dua.id),
                            onCardClick = { onOpenDetail(dua.id, dua.cat) },
                            onRefClick = { selectedRefDua = dua },
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

        // Hadith Reference Modal (duaHadithRef)
        if (selectedRefDua != null) {
            DuaHadithRefModal(
                dua = selectedRefDua!!,
                selectedLang = selectedLang,
                onDismiss = { selectedRefDua = null }
            )
        }

        // Save To Collection Bottom Sheet
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

        // Minimal Spotlight Tour Modal (Single-step on My Lists / Bookmarks)
        if (!isHomeTourDone) {
            DuaSpotlightTourModal(
                step = 1,
                totalSteps = 1,
                lang = selectedLang,
                targetRect = currentTargetRect,
                onNext = {
                    preferences.markHomeTourDone()
                },
                onDone = {
                    preferences.markHomeTourDone()
                },
                onSkip = {
                    preferences.markHomeTourDone()
                }
            )
        }
    }
}