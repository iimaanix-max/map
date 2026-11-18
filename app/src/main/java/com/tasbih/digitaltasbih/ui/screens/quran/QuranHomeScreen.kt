package com.tasbih.digitaltasbih.ui.screens.quran

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import com.tasbih.digitaltasbih.data.JuzMeta
import com.tasbih.digitaltasbih.data.QuranData
import com.tasbih.digitaltasbih.data.QuranRecentItem
import com.tasbih.digitaltasbih.data.QuranRepository
import com.tasbih.digitaltasbih.data.QuranReadTimeRepository
import com.tasbih.digitaltasbih.ui.components.QuranReadTimeBannerCard
import com.tasbih.digitaltasbih.data.QuranTourLocalization
import com.tasbih.digitaltasbih.data.SurahMeta
import com.tasbih.digitaltasbih.data.toArabicDigits
import com.tasbih.digitaltasbih.ui.components.GeneralSpotlightTourModal
import com.tasbih.digitaltasbih.ui.components.rememberAssetPainter
import com.tasbih.digitaltasbih.ui.theme.AmiriBoldFont
import com.tasbih.digitaltasbih.ui.theme.AmiriQuranFont
import com.tasbih.digitaltasbih.ui.theme.GoldAccent
import kotlinx.coroutines.delay

enum class QuranHomeTab {
    BY_SURAH,
    BY_JUZ,
    FAVORITES
}

@Composable
fun QuranHomeScreen(
    selectedLanguage: String = "hi",
    onBack: () -> Unit,
    onOpenReader: (type: String, number: Int) -> Unit,
    onOpenSettings: () -> Unit,
    onOpenReadTimeTracker: () -> Unit = {}
) {
    val context = LocalContext.current
    val quranTimeTrigger = QuranReadTimeRepository.updateTrigger.value
    LaunchedEffect(Unit) {
        QuranRepository.init(context)
        QuranReadTimeRepository.init(context)
    }
    val todayReadMinutes = remember(context, quranTimeTrigger) {
        QuranReadTimeRepository.getTodayMinutes(context)
    }
    val dailyGoalMinutes = remember(context, quranTimeTrigger) {
        QuranReadTimeRepository.getDailyGoalMinutes(context)
    }

    var activeTab by remember { mutableStateOf(QuranHomeTab.BY_SURAH) }
    var searchQuery by remember { mutableStateOf("") }
    var isLangModalOpen by remember { mutableStateOf(false) }

    var isTourOpen by remember { mutableStateOf(!QuranRepository.isTourDone()) }
    var tourStep by remember { mutableIntStateOf(1) } // 1 to 4

    val lazyListState = rememberLazyListState()
    var rootCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var langBarCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var tabsCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var gearCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var searchCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var firstSurahCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }

    val currentTargetRect: Rect? = remember(
        isTourOpen,
        tourStep,
        rootCoords,
        langBarCoords,
        tabsCoords,
        gearCoords,
        searchCoords,
        firstSurahCoords,
        lazyListState.firstVisibleItemIndex,
        lazyListState.firstVisibleItemScrollOffset
    ) {
        val root = rootCoords
        if (root != null && root.isAttached) {
            val target = when (tourStep) {
                1 -> langBarCoords
                2 -> tabsCoords
                3 -> gearCoords
                4 -> firstSurahCoords ?: searchCoords
                else -> null
            }
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

    LaunchedEffect(isTourOpen, tourStep) {
        if (isTourOpen) {
            when (tourStep) {
                1, 2, 3 -> {
                    delay(180L)
                    try {
                        lazyListState.animateScrollToItem(index = 0, scrollOffset = 0)
                    } catch (_: Exception) {}
                }
                4 -> {
                    delay(180L)
                    try {
                        lazyListState.animateScrollToItem(index = 5, scrollOffset = -80)
                    } catch (_: Exception) {}
                }
            }
        }
    }

    BackHandler {
        if (isTourOpen) {
            QuranRepository.setTourDone(true)
            isTourOpen = false
        } else if (searchQuery.isNotBlank()) {
            searchQuery = ""
        } else {
            onBack()
        }
    }

    var currentLanguage by remember(selectedLanguage) {
        mutableStateOf(QuranRepository.loadSettings().translationLanguage)
    }

    // Keep currentLanguage synchronized with settings and navigation
    LaunchedEffect(selectedLanguage) {
        currentLanguage = QuranRepository.loadSettings().translationLanguage
    }

    val lastRead = remember(currentLanguage) { QuranRepository.getLastRead() }
    val recents = remember(currentLanguage) { QuranRepository.getRecents() }

    // Filter surahs by search
    val filteredSurahs = remember(searchQuery, currentLanguage) {
        if (searchQuery.isBlank()) {
            QuranData.surahs
        } else {
            val q = searchQuery.trim().lowercase()
            QuranData.surahs.filter { s ->
                s.number.toString().contains(q) ||
                s.getLocalizedName(currentLanguage).lowercase().contains(q) ||
                s.getLocalizedMeaning(currentLanguage).lowercase().contains(q) ||
                s.englishName.lowercase().contains(q) ||
                s.hindiName.lowercase().contains(q) ||
                s.arabicName.contains(q)
            }
        }
    }

    // Filter juz by search
    val filteredJuz = remember(searchQuery, currentLanguage) {
        if (searchQuery.isBlank()) {
            QuranData.juzList
        } else {
            val q = searchQuery.trim().lowercase()
            QuranData.juzList.filter { j ->
                j.juzNumber.toString().contains(q) ||
                j.arabicName.contains(q) ||
                j.ranges.any { it.surahHindiName.lowercase().contains(q) }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F7F1))
            .onGloballyPositioned { rootCoords = it }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // 1. TOP BAR (Localized: ← Back, The Holy Quran / Al-Quran, ☀️, Flag, ⚙️)
            QuranTopBar(
                langCode = currentLanguage,
                onBack = onBack,
                onOpenLangModal = { isLangModalOpen = true },
                onOpenSettings = onOpenSettings,
                gearModifier = Modifier.onGloballyPositioned { gearCoords = it }
            )

            // 2. LANGUAGE BAR (English, हिंदी, اردو, العربية, বাংলা, 🌐 +20)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { langBarCoords = it }
            ) {
                QuranLanguageBar(
                    selectedCode = currentLanguage,
                    onSelect = { code ->
                        val updated = QuranRepository.switchLanguage(code)
                        currentLanguage = updated.translationLanguage
                    },
                    onOpenMore = { isLangModalOpen = true }
                )
            }

            // 3. TRANSLATION LANGUAGE MODAL
            if (isLangModalOpen) {
                QuranLanguageModal(
                    currentLanguage = currentLanguage,
                    onSelectLanguage = { code ->
                        val updated = QuranRepository.switchLanguage(code)
                        currentLanguage = updated.translationLanguage
                        isLangModalOpen = false
                    },
                    onDismiss = { isLangModalOpen = false }
                )
            }

            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp),
                contentPadding = PaddingValues(top = 10.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // 3. 3 SEGMENTED TABS
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned { tabsCoords = it }
                    ) {
                        QuranSegmentedTabs(
                            activeTab = activeTab,
                            langCode = currentLanguage,
                            onSelectTab = { activeTab = it }
                        )
                    }
                }

                // 4. SEARCH BAR
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned { searchCoords = it }
                    ) {
                        QuranSearchBar(
                            query = searchQuery,
                            langCode = currentLanguage,
                            onQueryChange = { searchQuery = it }
                        )
                    }
                }

                // 5. DAILY AYAH CARD
                item {
                    DailyAyahCard(
                        langCode = currentLanguage,
                        onClick = { onOpenReader("surah", 9) }
                    )
                }

                // 5.5 QURAN TIME PROGRESS BANNER (Image 1)
                item {
                    QuranReadTimeBannerCard(
                        todayMinutes = todayReadMinutes,
                        goalMinutes = dailyGoalMinutes,
                        onClick = onOpenReadTimeTracker
                    )
                }

                // 6. CONTINUE READING HERO CARD
                item {
                    ContinueReadingCard(
                        lastRead = lastRead,
                        langCode = currentLanguage,
                        onClick = { onOpenReader(lastRead.type, lastRead.number) }
                    )
                }

                // 7. RECENTS HORIZONTAL CAROUSEL
                item {
                    RecentsCarousel(
                        recents = recents,
                        onItemClick = { r -> onOpenReader(r.type, r.number) }
                    )
                }

                // 8. TAB SPECIFIC ITEMS
                when (activeTab) {
                    QuranHomeTab.BY_SURAH -> {
                        items(filteredSurahs, key = { it.number }) { surah ->
                            val isFirst = filteredSurahs.firstOrNull()?.number == surah.number
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .then(if (isFirst) Modifier.onGloballyPositioned { firstSurahCoords = it } else Modifier)
                            ) {
                                SurahCardItem(
                                    surah = surah,
                                    langCode = currentLanguage,
                                    onClick = { onOpenReader("surah", surah.number) }
                                )
                            }
                        }
                    }
                    QuranHomeTab.BY_JUZ -> {
                        items(filteredJuz, key = { it.juzNumber }) { juz ->
                            JuzCardItem(
                                juz = juz,
                                langCode = currentLanguage,
                                onClick = { onOpenReader("juz", juz.juzNumber) }
                            )
                        }
                    }
                    QuranHomeTab.FAVORITES -> {
                        item {
                            FavoritesTabContent(
                                langCode = currentLanguage,
                                onOpenReader = onOpenReader
                            )
                        }
                    }
                }
            }
        }

        // 4-Step Spotlight Tour Modal
        if (isTourOpen) {
            val tourTx = remember(currentLanguage) { QuranTourLocalization.get(currentLanguage) }
            val stepInfo = tourTx.steps.getOrNull(tourStep - 1)
            val stepLabel = tourTx.stepOf.replace("{s}", "$tourStep").replace("{total}", "4")
            GeneralSpotlightTourModal(
                step = tourStep,
                totalSteps = 4,
                stepLabel = stepLabel,
                title = stepInfo?.title ?: "",
                description = stepInfo?.desc ?: "",
                nextBtnText = tourTx.next,
                doneBtnText = tourTx.finish,
                skipBtnText = tourTx.skip,
                targetRect = currentTargetRect,
                cornerRadiusDp = if (tourStep == 3) 50f else 16f,
                padDp = if (tourStep == 3) 4f else 6f,
                onNext = {
                    if (tourStep < 4) {
                        tourStep++
                    } else {
                        QuranRepository.setTourDone(true)
                        isTourOpen = false
                    }
                },
                onDone = {
                    QuranRepository.setTourDone(true)
                    isTourOpen = false
                },
                onSkip = {
                    QuranRepository.setTourDone(true)
                    isTourOpen = false
                }
            )
        }
    }
}

// ══════════════════════════════════════════════════════════════
// 1. TOP BAR
// ══════════════════════════════════════════════════════════════
@Composable
private fun QuranTopBar(
    langCode: String,
    onBack: () -> Unit,
    onOpenLangModal: () -> Unit,
    onOpenSettings: () -> Unit,
    gearModifier: Modifier = Modifier
) {
    val backLabel = when (langCode) {
        "en" -> "Back"
        "ur" -> "واپس"
        "ar" -> "رجوع"
        else -> "वापस"
    }
    val titleLabel = when (langCode) {
        "en" -> "The Holy Quran"
        "ur" -> "قرآن مجید"
        "ar" -> "القرآن الكريم"
        else -> "कुरान शरीफ़"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // [ ← Back ] Pill Button
        Row(
            modifier = Modifier
                .shadow(2.dp, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFFFFFDF8))
                .border(1.2.dp, GoldAccent.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                .clickable(onClick = onBack)
                .padding(horizontal = 14.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color(0xFF0D4A3E),
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = backLabel,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D4A3E)
            )
        }

        // Title: The Holy Quran / Al-Quran + subtle curved gold underline
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = titleLabel,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF0D4A3E),
                fontFamily = FontFamily.Serif
            )
            // Gold curved underline
            Box(
                modifier = Modifier
                    .width(36.dp)
                    .height(2.5.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color.Transparent, GoldAccent, Color.Transparent)
                        )
                    )
            )
        }

        // Right Action Buttons: ☀️, Flag (clickable), ⚙️
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Sun toggle
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .shadow(1.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Color(0xFFFFFDF8))
                    .border(1.dp, GoldAccent.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "☀️", fontSize = 16.sp)
            }

            // Flag (opens language modal)
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .shadow(1.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Color(0xFFFFFDF8))
                    .border(1.dp, GoldAccent.copy(alpha = 0.4f), CircleShape)
                    .clickable(onClick = onOpenLangModal),
                contentAlignment = Alignment.Center
            ) {
                Text(text = QuranRepository.getFlagEmoji(langCode), fontSize = 17.sp)
            }

            // Settings ⚙️
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .shadow(1.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Color(0xFFFFFDF8))
                    .border(1.dp, GoldAccent.copy(alpha = 0.4f), CircleShape)
                    .then(gearModifier)
                    .clickable(onClick = onOpenSettings),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "⚙️", fontSize = 16.sp)
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
// 2. LANGUAGE BAR
// ══════════════════════════════════════════════════════════════
@Composable
private fun QuranLanguageBar(
    selectedCode: String,
    onSelect: (String) -> Unit,
    onOpenMore: () -> Unit
) {
    val defaultLanguages = listOf(
        Pair("en", "English"),
        Pair("hi", "हिंदी"),
        Pair("ur", "اردو"),
        Pair("ar", "العربية"),
        Pair("bn", "বাংলা"),
        Pair("ta", "தமிழ்"),
        Pair("ml", "മലയാളം"),
        Pair("gu", "ગુજરાતી")
    )
    val activeLangItem = AvailableQuranLanguages.find { it.code == selectedCode }
    val languages = if (defaultLanguages.none { it.first == selectedCode } && activeLangItem != null) {
        defaultLanguages + Pair(activeLangItem.code, activeLangItem.nativeName)
    } else {
        defaultLanguages
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 14.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        languages.forEach { (code, name) ->
            val isSelected = selectedCode == code
            Box(
                modifier = Modifier
                    .shadow(if (isSelected) 3.dp else 1.dp, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isSelected) Color(0xFF0D4A3E) else Color(0xFFFFFDF8))
                    .border(
                        1.dp,
                        if (isSelected) Color(0xFF0D4A3E) else GoldAccent.copy(alpha = 0.35f),
                        RoundedCornerShape(16.dp)
                    )
                    .clickable { onSelect(code) }
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = name,
                    fontSize = 12.5.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) Color.White else Color(0xFF1F2937)
                )
            }
        }

        // 🌐 +20
        Box(
            modifier = Modifier
                .shadow(1.dp, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFE8F4F8))
                .border(1.dp, Color(0xFF0284C7).copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                .clickable(onClick = onOpenMore)
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = "🌐 +More",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0284C7)
            )
        }
    }
}

// ══════════════════════════════════════════════════════════════
// 3. SEGMENTED TABS
// ══════════════════════════════════════════════════════════════
@Composable
private fun QuranSegmentedTabs(
    activeTab: QuranHomeTab,
    langCode: String,
    onSelectTab: (QuranHomeTab) -> Unit
) {
    val tab1 = when (langCode) {
        "en" -> "By Surah"
        "ur" -> "بلحاظ سورہ"
        "ar" -> "بالسور"
        else -> "सूरह के अनुसार"
    }
    val tab2 = when (langCode) {
        "en" -> "By Juz"
        "ur" -> "بلحاظ پارہ"
        "ar" -> "بالأجزاء"
        else -> "पारा के अनुसार"
    }
    val tab3 = when (langCode) {
        "en" -> "⭐ Favorites"
        "ur" -> "⭐ پسندیدہ"
        "ar" -> "⭐ المفضلة"
        else -> "⭐ पसंदीदा"
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Tab 1: By Surah
        TabButton(
            title = tab1,
            isSelected = activeTab == QuranHomeTab.BY_SURAH,
            modifier = Modifier.weight(1.15f),
            onClick = { onSelectTab(QuranHomeTab.BY_SURAH) }
        )

        // Tab 2: By Juz
        TabButton(
            title = tab2,
            isSelected = activeTab == QuranHomeTab.BY_JUZ,
            modifier = Modifier.weight(1.05f),
            onClick = { onSelectTab(QuranHomeTab.BY_JUZ) }
        )

        // Tab 3: Favorites
        TabButton(
            title = tab3,
            isSelected = activeTab == QuranHomeTab.FAVORITES,
            modifier = Modifier.weight(0.95f),
            onClick = { onSelectTab(QuranHomeTab.FAVORITES) }
        )
    }
}

@Composable
private fun TabButton(
    title: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(44.dp)
            .shadow(if (isSelected) 4.dp else 1.dp, RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
            .background(if (isSelected) Color(0xFF0D4A3E) else Color(0xFFFFFDF8))
            .border(
                1.2.dp,
                if (isSelected) Color(0xFF0D4A3E) else GoldAccent.copy(alpha = 0.45f),
                RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
            color = if (isSelected) Color.White else Color(0xFF1F2937),
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

// ══════════════════════════════════════════════════════════════
// 4. SEARCH BAR
// ══════════════════════════════════════════════════════════════
@Composable
private fun QuranSearchBar(
    query: String,
    langCode: String,
    onQueryChange: (String) -> Unit
) {
    val placeholder = when (langCode) {
        "en" -> "Search Surah name or number..."
        "ur" -> "سورہ کا نام یا نمبر تلاش کریں..."
        "ar" -> "ابحث عن اسم السورة أو رقمها..."
        else -> "सूरह का नाम या नंबर खोजें..."
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .shadow(2.dp, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .border(1.2.dp, GoldAccent.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = Color(0xFF9CA3AF),
            modifier = Modifier.size(20.dp)
        )
        Box(modifier = Modifier.fillMaxWidth()) {
            if (query.isEmpty()) {
                Text(
                    text = placeholder,
                    color = Color(0xFF9CA3AF),
                    fontSize = 14.sp
                )
            }
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    color = Color(0xFF1F2937),
                    fontWeight = FontWeight.Medium
                ),
                cursorBrush = SolidColor(Color(0xFF0D4A3E)),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// ══════════════════════════════════════════════════════════════
// 5. DAILY AYAH CARD
// ══════════════════════════════════════════════════════════════
@Composable
private fun DailyAyahCard(
    langCode: String,
    onClick: () -> Unit
) {
    val dailyTitle = when (langCode) {
        "en" -> "✨ Daily Ayah • At-Tawbah — التوبة"
        "ur" -> "✨ آج کی آیت • التوبہ — التوبة"
        "ar" -> "✨ آية اليوم • التوبة"
        else -> "✨ आज की आयत • अत-तौबह — التوبة"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFFFDF8))
            .border(1.2.dp, GoldAccent.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = dailyTitle,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D4A3E)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "— 9:18",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF4B5563),
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// ══════════════════════════════════════════════════════════════
// 6. CONTINUE READING HERO CARD
// ══════════════════════════════════════════════════════════════
@Composable
private fun ContinueReadingCard(
    lastRead: QuranRecentItem,
    langCode: String,
    onClick: () -> Unit
) {
    val continueLabel = when (langCode) {
        "en" -> "Continue Reading"
        "ur" -> "پڑھنا جاری رکھیں"
        "ar" -> "متابعة القراءة"
        else -> "पढ़ना जारी रखें"
    }
    val emeraldGradient = Brush.horizontalGradient(
        listOf(Color(0xFF0A3C31), Color(0xFF0F5A4B))
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .shadow(4.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(emeraldGradient)
            .border(1.2.dp, GoldAccent.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Book Icon Circle
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0x26FFFFFF))
                    .border(1.dp, GoldAccent.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "📖", fontSize = 20.sp)
            }

            Column {
                Text(
                    text = continueLabel,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFD4E7DF)
                )
                Text(
                    text = lastRead.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Text(
            text = "›",
            fontSize = 24.sp,
            fontWeight = FontWeight.Light,
            color = Color(0xFFD4E7DF)
        )
    }
}

// ══════════════════════════════════════════════════════════════
// 7. RECENTS CAROUSEL
// ══════════════════════════════════════════════════════════════
@Composable
private fun RecentsCarousel(
    recents: List<QuranRecentItem>,
    onItemClick: (QuranRecentItem) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        recents.forEach { r ->
            Box(
                modifier = Modifier
                    .width(130.dp)
                    .shadow(1.5.dp, RoundedCornerShape(14.dp))
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFFFFDF8))
                    .border(1.dp, GoldAccent.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
                    .clickable { onItemClick(r) }
                    .padding(horizontal = 10.dp, vertical = 10.dp)
            ) {
                Column {
                    Text(
                        text = r.title,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = r.date,
                        fontSize = 10.5.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
// 8. SURAH CARD ITEM (Surahs 1 to 114)
// ══════════════════════════════════════════════════════════════
@Composable
private fun SurahCardItem(
    surah: SurahMeta,
    langCode: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFFFDF8))
            .border(1.2.dp, GoldAccent.copy(alpha = 0.45f), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Circle with Number + Mid Details
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Circle Badge with Number
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF0D4A3E))
                        .border(1.2.dp, GoldAccent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = surah.number.toString(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }

                // Middle Text: Localized Surah Name — Meaning
                Column {
                    Text(
                        text = "${surah.getLocalizedName(langCode)} — ${surah.getLocalizedMeaning(langCode)}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${surah.getLocalizedType(langCode)} • ${surah.getLocalizedAyahsLabel(langCode)}",
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }

            // Right: Arabic Calligraphy with Amiri Bold
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Watermark rosette icon
                val watermark = rememberAssetPainter("media/84e5172e17268b1f.webp")
                Image(
                    painter = watermark,
                    contentDescription = null,
                    modifier = Modifier.size(26.dp),
                    alpha = 0.45f
                )

                Text(
                    text = surah.arabicName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D4A3E),
                    fontFamily = AmiriBoldFont
                )
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
// 9. JUZ CARD ITEM (Juz 1 to 30)
// ══════════════════════════════════════════════════════════════
@Composable
private fun JuzCardItem(
    juz: JuzMeta,
    langCode: String,
    onClick: () -> Unit
) {
    val juzTitle = when (langCode) {
        "en" -> "Juz ${juz.juzNumber}"
        "ur" -> "پارہ ${juz.juzNumber}"
        "ar" -> "الجزء ${toArabicDigits(juz.juzNumber)}"
        else -> "पारा ${juz.juzNumber}"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFFFDF8))
            .border(1.2.dp, GoldAccent.copy(alpha = 0.45f), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Circle with Number + Mid Details
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Circle Badge
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF0D4A3E))
                        .border(1.2.dp, GoldAccent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = juz.juzNumber.toString(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }

                // Middle Text: Juz title + Surahs covered
                Column {
                    Text(
                        text = juzTitle,
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    juz.ranges.forEach { r ->
                        val surahName = QuranData.surahs.find { it.number == r.surahNumber }?.getLocalizedName(langCode) ?: r.surahHindiName
                        Text(
                            text = "✦ $surahName (${r.ayahRange})",
                            fontSize = 11.5.sp,
                            color = Color(0xFF4B5563)
                        )
                    }
                }
            }

            // Right: Arabic Juz title & opening text
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${toArabicDigits(juz.juzNumber)} جزء",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D4A3E),
                    fontFamily = AmiriBoldFont
                )
                Text(
                    text = juz.arabicName,
                    fontSize = 14.sp,
                    color = Color(0xFFB45309), // Bronze/Gold accent
                    fontFamily = AmiriQuranFont
                )
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
// 10. FAVORITES TAB CONTENT
// ══════════════════════════════════════════════════════════════
@Composable
private fun FavoritesTabContent(
    langCode: String,
    onOpenReader: (type: String, number: Int) -> Unit
) {
    val favAyahs = remember { QuranRepository.getFavoriteAyahs() }

    val emptyTitle = when (langCode) {
        "en" -> "No favorite ayahs yet"
        "ur" -> "کوئی پسندیدہ آیت نہیں ہے"
        "ar" -> "لا توجد آيات مفضلة"
        else -> "कोई पसंदीदा आयत नहीं है"
    }
    val emptySubtitle = when (langCode) {
        "en" -> "Tap the ⭐ icon on any Ayah to save it here"
        "ur" -> "کسی بھی آیت پر ⭐ دبا کر یہاں محفوظ کریں"
        "ar" -> "اضغط على ⭐ في أي آية لحفظها هنا"
        else -> "किसी भी आयत पर ⭐ दबाकर उसे यहाँ सेव करें"
    }
    val readLabel = when (langCode) {
        "en" -> "Read 📖"
        "ur" -> "پڑھیں 📖"
        "ar" -> "اقرأ 📖"
        else -> "पढ़ें 📖"
    }

    if (favAyahs.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 40.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "⭐", fontSize = 36.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = emptyTitle,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF6B7280)
                )
                Text(
                    text = emptySubtitle,
                    fontSize = 12.sp,
                    color = Color(0xFF9CA3AF)
                )
            }
        }
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            favAyahs.forEach { globalNum ->
                val favAyahLabel = when (langCode) {
                    "en" -> "Favorite Ayah #$globalNum"
                    "ur" -> "پسندیدہ آیت #$globalNum"
                    "ar" -> "آية مفضلة رقم $globalNum"
                    else -> "पसंदीदा आयत #$globalNum"
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(1.5.dp, RoundedCornerShape(14.dp))
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFFFFDF8))
                        .border(1.dp, GoldAccent.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                        .clickable { onOpenReader("surah", 1) }
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = favAyahLabel,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0D4A3E)
                        )
                        Text(
                            text = readLabel,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB45309)
                        )
                    }
                }
            }
        }
    }
}
