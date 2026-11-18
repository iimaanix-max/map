package com.tasbih.digitaltasbih.ui.screens.hadith

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.tasbih.digitaltasbih.ui.components.AppToast
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.tasbih.digitaltasbih.data.HadithBookItem
import com.tasbih.digitaltasbih.data.HadithBookmarkItem
import com.tasbih.digitaltasbih.data.HadithChapterItem
import com.tasbih.digitaltasbih.data.HadithCollectionItem
import com.tasbih.digitaltasbih.data.HadithDetailItem
import com.tasbih.digitaltasbih.data.HadithLocalization
import com.tasbih.digitaltasbih.data.HadithRepository
import com.tasbih.digitaltasbih.ui.components.rememberAssetPainter
import com.tasbih.digitaltasbih.ui.theme.AmiriQuranFont
import kotlinx.coroutines.launch
import java.util.Locale

private enum class HadithScreenMode {
    COLLECTIONS,
    BOOKS,
    CHAPTERS,
    READER,
    SETTINGS
}

@Composable
fun HadithScreen(
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val prefs = remember { context.getSharedPreferences("hadith_prefs", Context.MODE_PRIVATE) }

    var selectedLang by remember { mutableStateOf(prefs.getString("hadith_lang", "hi") ?: "hi") }
    val strings = remember(selectedLang) { HadithLocalization.get(selectedLang) }

    var isDarkTheme by remember { mutableStateOf(prefs.getBoolean("hadith_dark", false)) }
    var fontScale by remember { mutableFloatStateOf(prefs.getFloat("hadith_font_scale", 1.0f)) }

    val toggleTheme = {
        val newVal = !isDarkTheme
        isDarkTheme = newVal
        prefs.edit().putBoolean("hadith_dark", newVal).apply()
    }
    val updateFontScale: (Float) -> Unit = { scale ->
        fontScale = scale
        prefs.edit().putFloat("hadith_font_scale", scale).apply()
    }
    val updateLanguage: (String) -> Unit = { lang ->
        selectedLang = lang
        prefs.edit().putString("hadith_lang", lang).apply()
    }

    var mode by remember { mutableStateOf(HadithScreenMode.COLLECTIONS) }
    var previousMode by remember { mutableStateOf(HadithScreenMode.COLLECTIONS) }
    var activeCollection by remember { mutableStateOf<HadithCollectionItem?>(null) }
    var activeBook by remember { mutableStateOf<HadithBookItem?>(null) }
    var activeChapter by remember { mutableStateOf<HadithChapterItem?>(null) }

    // TTS
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var playingHadithNum by remember { mutableStateOf<String?>(null) }

    DisposableEffect(Unit) {
        var t: TextToSpeech? = null
        t = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                t?.language = Locale.ENGLISH
            }
        }
        tts = t
        onDispose {
            t.stop()
            t.shutdown()
        }
    }

    // Back handling
    BackHandler {
        when (mode) {
            HadithScreenMode.SETTINGS -> {
                mode = previousMode
            }
            HadithScreenMode.READER -> {
                tts?.stop()
                playingHadithNum = null
                mode = if (activeChapter != null) HadithScreenMode.CHAPTERS else HadithScreenMode.BOOKS
            }
            HadithScreenMode.CHAPTERS -> {
                mode = HadithScreenMode.BOOKS
            }
            HadithScreenMode.BOOKS -> {
                mode = HadithScreenMode.COLLECTIONS
            }
            HadithScreenMode.COLLECTIONS -> {
                onNavigateBack()
            }
        }
    }

    when (mode) {
        HadithScreenMode.COLLECTIONS -> {
            HadithCollectionsView(
                strings = strings,
                isDarkTheme = isDarkTheme,
                selectedLang = selectedLang,
                tts = tts,
                onBack = onNavigateBack,
                onToggleTheme = toggleTheme,
                onOpenSettings = {
                    previousMode = mode
                    mode = HadithScreenMode.SETTINGS
                },
                onSelectCollection = { col ->
                    activeCollection = col
                    mode = HadithScreenMode.BOOKS
                }
            )
        }
        HadithScreenMode.BOOKS -> {
            val col = activeCollection
            if (col != null) {
                HadithBooksView(
                    collection = col,
                    strings = strings,
                    isDarkTheme = isDarkTheme,
                    onBack = { mode = HadithScreenMode.COLLECTIONS },
                    onToggleTheme = toggleTheme,
                    onOpenSettings = {
                        previousMode = mode
                        mode = HadithScreenMode.SETTINGS
                    },
                    onSelectBook = { book ->
                        activeBook = book
                        mode = HadithScreenMode.CHAPTERS
                    },
                    onJumpToHadith = { num ->
                        activeBook = HadithBookItem(bookNumber = "1", nameEn = col.titleEn)
                        activeChapter = null
                        mode = HadithScreenMode.READER
                    }
                )
            } else {
                mode = HadithScreenMode.COLLECTIONS
            }
        }
        HadithScreenMode.CHAPTERS -> {
            val col = activeCollection
            val book = activeBook
            if (col != null && book != null) {
                HadithChaptersView(
                    collection = col,
                    book = book,
                    strings = strings,
                    isDarkTheme = isDarkTheme,
                    onBack = { mode = HadithScreenMode.BOOKS },
                    onToggleTheme = toggleTheme,
                    onOpenSettings = {
                        previousMode = mode
                        mode = HadithScreenMode.SETTINGS
                    },
                    onSelectChapter = { chap ->
                        activeChapter = chap
                        mode = HadithScreenMode.READER
                    }
                )
            } else {
                mode = HadithScreenMode.COLLECTIONS
            }
        }
        HadithScreenMode.READER -> {
            val col = activeCollection
            val book = activeBook
            if (col != null && book != null) {
                HadithReaderView(
                    collection = col,
                    book = book,
                    chapter = activeChapter,
                    strings = strings,
                    isDarkTheme = isDarkTheme,
                    fontScale = fontScale,
                    onBack = {
                        tts?.stop()
                        playingHadithNum = null
                        mode = if (activeChapter != null) HadithScreenMode.CHAPTERS else HadithScreenMode.BOOKS
                    },
                    onToggleTheme = toggleTheme,
                    onOpenSettings = {
                        previousMode = mode
                        mode = HadithScreenMode.SETTINGS
                    },
                    onSpeak = { text, num ->
                        if (playingHadithNum == num) {
                            tts?.stop()
                            playingHadithNum = null
                        } else {
                            tts?.stop()
                            val hasArabic = text.any { it in '\u0600'..'\u06FF' }
                            if (hasArabic) {
                                val res = tts?.setLanguage(Locale("ar"))
                                if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
                                    tts?.language = Locale.ENGLISH
                                }
                            } else {
                                tts?.language = if (selectedLang.startsWith("hi")) Locale("hi") else Locale.ENGLISH
                            }
                            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "hd_$num")
                            playingHadithNum = num
                        }
                    },
                    playingHadithNum = playingHadithNum
                )
            } else {
                mode = HadithScreenMode.COLLECTIONS
            }
        }
        HadithScreenMode.SETTINGS -> {
            HadithSettingsView(
                strings = strings,
                isDarkTheme = isDarkTheme,
                fontScale = fontScale,
                selectedLang = selectedLang,
                onFontScaleChange = updateFontScale,
                onSelectLang = updateLanguage,
                onBack = { mode = previousMode }
            )
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// 1. COLLECTIONS VIEW (Exact Screenshot 1 & 2)
// ══════════════════════════════════════════════════════════════════════════════
@Composable
private fun HadithCollectionsView(
    strings: com.tasbih.digitaltasbih.data.HadithStrings,
    isDarkTheme: Boolean,
    selectedLang: String,
    tts: TextToSpeech? = null,
    onBack: () -> Unit,
    onToggleTheme: () -> Unit,
    onOpenSettings: () -> Unit,
    onSelectCollection: (HadithCollectionItem) -> Unit
) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("hadith_prefs", Context.MODE_PRIVATE) }
    var heroStyle by remember {
        mutableStateOf(HadithHeroStyle.fromId(prefs.getString("hadith_hero_style", "emerald_arch") ?: "emerald_arch"))
    }
    var isDesignModalOpen by remember { mutableStateOf(false) }
    var isHeroPlaying by remember { mutableStateOf(false) }

    var searchQuery by remember { mutableStateOf("") }
    var activeTab by remember { mutableStateOf("collections") }
    var collections by remember { mutableStateOf<List<HadithCollectionItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var infoModalItem by remember { mutableStateOf<HadithCollectionItem?>(null) }
    var bookmarks by remember { mutableStateOf<List<HadithBookmarkItem>>(emptyList()) }

    LaunchedEffect(Unit) {
        collections = HadithRepository.getCollections()
        bookmarks = HadithRepository.getBookmarks()
        isLoading = false
    }

    val filteredCollections = remember(collections, searchQuery) {
        if (searchQuery.isBlank()) collections
        else collections.filter {
            it.titleEn.contains(searchQuery, ignoreCase = true) ||
            it.titleAr.contains(searchQuery, ignoreCase = true) ||
            it.name.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (isDarkTheme) Color(0xFF122019) else Color(0xFFFBF8F2))
            ) {
                HadithTopBar(
                    title = strings.title,
                    backLabel = strings.back,
                    isDarkTheme = isDarkTheme,
                    onBack = onBack,
                    onToggleTheme = onToggleTheme,
                    onOpenSettings = onOpenSettings
                )

                Spacer(Modifier.height(10.dp))

                // Search Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(if (isDarkTheme) Color(0xFF182E24) else Color.White)
                        .border(1.dp, if (isDarkTheme) Color(0xFF28483B) else Color(0xFFE5E0D5), RoundedCornerShape(50.dp))
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🔍", fontSize = 14.sp, color = if (isDarkTheme) Color(0xFF9E948A) else Color(0xFF8C827A))
                        Spacer(Modifier.width(10.dp))
                        if (searchQuery.isEmpty()) {
                            Text(strings.searchCollections, color = Color(0xFF9E948A), fontSize = 13.5.sp)
                        }
                    }
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth().padding(start = 28.dp),
                        singleLine = true,
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 13.5.sp,
                            color = if (isDarkTheme) Color.White else Color(0xFF1C1917)
                        )
                    )
                }

                Spacer(Modifier.height(10.dp))

                // Segmented Tabs: Collections vs Bookmarks
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Collections Tab
                    val isCol = activeTab == "collections"
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .background(
                                if (isCol) Color(0xFF064E3B)
                                else if (isDarkTheme) Color(0xFF182E24)
                                else Color.Transparent
                            )
                            .border(
                                1.dp,
                                if (isCol) Color(0xFF064E3B)
                                else if (isDarkTheme) Color(0xFF28483B)
                                else Color(0xFFDCD5C5),
                                RoundedCornerShape(50.dp)
                            )
                            .clickable { activeTab = "collections" },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = strings.collectionsTab,
                            color = if (isCol) Color.White else if (isDarkTheme) Color(0xFFD4C8B0) else Color(0xFF5D4D38),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    // Bookmarks Tab
                    val isBm = activeTab == "bookmarks"
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .background(
                                if (isBm) Color(0xFF064E3B)
                                else if (isDarkTheme) Color(0xFF182E24)
                                else Color.Transparent
                            )
                            .border(
                                1.dp,
                                if (isBm) Color(0xFF064E3B)
                                else if (isDarkTheme) Color(0xFF28483B)
                                else Color(0xFFDCD5C5),
                                RoundedCornerShape(50.dp)
                            )
                            .clickable {
                                bookmarks = HadithRepository.getBookmarks()
                                activeTab = "bookmarks"
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = strings.bookmarksTab,
                            color = if (isBm) Color.White else if (isDarkTheme) Color(0xFFD4C8B0) else Color(0xFF5D4D38),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isDarkTheme) Color(0xFF122019) else Color(0xFFFBF8F2))
                .padding(padding)
        ) {
            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF0E5D47))
                }
            } else if (activeTab == "bookmarks") {
                if (bookmarks.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🔖", fontSize = 42.sp)
                            Spacer(Modifier.height(8.dp))
                            Text(
                                strings.noBookmarks,
                                color = if (isDarkTheme) Color(0xFFB5A99B) else Color(0xFF7A6F65),
                                fontSize = 14.sp
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(bookmarks) { bm ->
                            HadithBookmarkCard(bm = bm)
                        }
                    }
                }
            } else {
                // 2-Column Luxury 3D Bookshelf Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 8.dp, bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Daily Featured Hadith Hero Card with Design Switcher (hadithHeroDesignModal)
                    if (activeTab == "collections" && searchQuery.isBlank()) {
                        item(span = { GridItemSpan(2) }) {
                            DailyHadithHeroCard(
                                style = heroStyle,
                                isDarkTheme = isDarkTheme,
                                selectedLang = selectedLang,
                                isPlaying = isHeroPlaying,
                                onSpeakClick = {
                                    if (isHeroPlaying) {
                                        tts?.stop()
                                        isHeroPlaying = false
                                    } else {
                                        tts?.stop()
                                        val textToSpeak = "${DEFAULT_DAILY_HADITH.arabic}. ${DEFAULT_DAILY_HADITH.narrator} ${DEFAULT_DAILY_HADITH.getBody(selectedLang)}"
                                        val hasArabic = DEFAULT_DAILY_HADITH.arabic.any { it in '\u0600'..'\u06FF' }
                                        if (hasArabic) {
                                            tts?.setLanguage(Locale("ar"))
                                        }
                                        tts?.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, "hero_daily_hadith")
                                        isHeroPlaying = true
                                    }
                                },
                                onDesignClick = { isDesignModalOpen = true },
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                    }

                    items(filteredCollections) { col ->
                        HadithBookCoverCard(
                            col = col,
                            isDarkTheme = isDarkTheme,
                            onClick = { onSelectCollection(col) },
                            onInfoClick = { infoModalItem = col }
                        )
                    }
                }
            }

            // Hero Card Design Switcher Modal (hadithHeroDesignModal)
            if (isDesignModalOpen) {
                HadithHeroDesignModal(
                    currentStyle = heroStyle,
                    isDarkTheme = isDarkTheme,
                    onSelectStyle = { newStyle ->
                        heroStyle = newStyle
                        prefs.edit().putString("hadith_hero_style", newStyle.id).apply()
                    },
                    onDismiss = { isDesignModalOpen = false }
                )
            }

            // Info Modal
            infoModalItem?.let { col ->
                HadithInfoModal(col = col, onDismiss = { infoModalItem = null })
            }
        }
    }
}

// ── 3D Book Cover Card ──
@Composable
private fun HadithBookCoverCard(
    col: HadithCollectionItem,
    isDarkTheme: Boolean,
    onClick: () -> Unit,
    onInfoClick: () -> Unit
) {
    val painter = rememberAssetPainter(col.coverAsset)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.72f)
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        shadowElevation = 8.dp,
        color = Color(0xFF1B3B2B)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            androidx.compose.foundation.Image(
                painter = painter,
                contentDescription = col.titleEn,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Info button at top right
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color(0x55000000))
                    .clickable { onInfoClick() },
                contentAlignment = Alignment.Center
            ) {
                Text("ⓘ", color = Color(0xCCFFFFFF), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            // Middle Typography Overlays with strict boundary checking
            HadithCoverContent(
                col = col,
                isDarkTheme = isDarkTheme,
                isFloating = false
            )
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// 2. BOOKS / SECTION VIEW (Exact Screenshot 3)
// ══════════════════════════════════════════════════════════════════════════════
@Composable
private fun HadithBooksView(
    collection: HadithCollectionItem,
    strings: com.tasbih.digitaltasbih.data.HadithStrings,
    isDarkTheme: Boolean,
    onBack: () -> Unit,
    onToggleTheme: () -> Unit,
    onOpenSettings: () -> Unit,
    onSelectBook: (HadithBookItem) -> Unit,
    onJumpToHadith: (String) -> Unit
) {
    val context = LocalContext.current
    var books by remember { mutableStateOf<List<HadithBookItem>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    var jumpNumber by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var showBioModal by remember { mutableStateOf(false) }

    LaunchedEffect(collection.name) {
        books = HadithRepository.getBooks(collection.name)
        isLoading = false
    }

    val filteredBooks = remember(books, searchQuery) {
        if (searchQuery.isBlank()) books
        else books.filter {
            it.nameEn.contains(searchQuery, ignoreCase = true) ||
            it.nameAr.contains(searchQuery, ignoreCase = true) ||
            it.bookNumber == searchQuery
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDarkTheme) Color(0xFF122019) else Color(0xFFFBF8F2))
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Hero Teakwood Carved Mashrabiya Banner with 3D Floating Book Cover (Exact Image 2)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(290.dp)
                ) {
                    androidx.compose.foundation.Image(
                        painter = rememberAssetPainter(HadithRepository.HERO_BG_ASSET),
                        contentDescription = "Mashrabiya",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Brush.verticalGradient(listOf(Color(0x33000000), Color(0x99000000))))
                    )

                    // Floating 3D Book Cover positioned safely below status bar & camera notch
                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 20.dp)
                            .width(148.dp)
                            .aspectRatio(0.72f)
                            .clip(RoundedCornerShape(14.dp)),
                        shape = RoundedCornerShape(14.dp),
                        shadowElevation = 20.dp,
                        color = Color(0xFF1B3B2B)
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            androidx.compose.foundation.Image(
                                painter = rememberAssetPainter(collection.coverAsset),
                                contentDescription = collection.titleEn,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            HadithCoverContent(
                                col = collection,
                                isDarkTheme = isDarkTheme,
                                isFloating = true
                            )
                        }
                    }

                    // Floating Emerald Back Button Top Left (Exact Image 2)
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .statusBarsPadding()
                            .padding(start = 16.dp, top = 8.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF00897B))
                            .border(1.dp, Color(0x66FFFFFF), CircleShape)
                            .clickable { onBack() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Collection Details Box (Exact Screenshot 3)
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = collection.titleEn,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = if (isDarkTheme) Color.White else Color(0xFF1C1917)
                        )
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(if (isDarkTheme) Color(0xFF182E24) else Color(0xFFF0EBE1))
                                .clickable { },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🔍", fontSize = 14.sp)
                        }
                    }

                    Spacer(Modifier.height(4.dp))

                    // Stats Pill
                    Text(
                        text = "${collection.totalHadith} Hadith",
                        color = if (isDarkTheme) Color(0xFF10B981) else Color(0xFF0E5D47),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )

                    Spacer(Modifier.height(6.dp))

                    // Short Intro + Read More
                    if (collection.shortIntro.isNotEmpty()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = collection.shortIntro.take(130) + "... ",
                                color = if (isDarkTheme) Color(0xFFD4C8B0) else Color(0xFF5D4D38),
                                fontSize = 12.5.sp,
                                lineHeight = 16.sp,
                                modifier = Modifier.weight(1f, fill = false)
                            )
                            Text(
                                text = strings.readMore,
                                color = if (isDarkTheme) Color(0xFF10B981) else Color(0xFF0E5D47),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.5.sp,
                                modifier = Modifier.clickable { showBioModal = true }
                            )
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    // Search input
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .background(if (isDarkTheme) Color(0xFF182E24) else Color.White)
                            .border(1.dp, if (isDarkTheme) Color(0xFF28483B) else Color(0xFFE5E0D5), RoundedCornerShape(50.dp))
                            .padding(horizontal = 14.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🔍", fontSize = 13.sp, color = if (isDarkTheme) Color(0xFF9E948A) else Color(0xFF8C827A))
                            Spacer(Modifier.width(8.dp))
                            if (searchQuery.isEmpty()) {
                                Text(strings.searchBooks, color = Color(0xFF9E948A), fontSize = 13.sp)
                            }
                        }
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.fillMaxWidth().padding(start = 24.dp),
                            singleLine = true,
                            textStyle = androidx.compose.ui.text.TextStyle(
                                fontSize = 13.sp,
                                color = if (isDarkTheme) Color.White else Color(0xFF1C1917)
                            )
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    // Jump to Hadith Number row (Exact Screenshot 3)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp)
                                .clip(RoundedCornerShape(50.dp))
                                .background(if (isDarkTheme) Color(0xFF182E24) else Color.White)
                                .border(1.dp, if (isDarkTheme) Color(0xFF28483B) else Color(0xFFE5E0D5), RoundedCornerShape(50.dp))
                                .padding(horizontal = 14.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("#", fontSize = 13.sp, color = if (isDarkTheme) Color(0xFF9E948A) else Color(0xFF8C827A), fontWeight = FontWeight.Bold)
                                Spacer(Modifier.width(8.dp))
                                if (jumpNumber.isEmpty()) {
                                    Text(strings.jumpPlaceholder, color = Color(0xFF9E948A), fontSize = 13.sp)
                                }
                            }
                            BasicTextField(
                                value = jumpNumber,
                                onValueChange = { jumpNumber = it },
                                modifier = Modifier.fillMaxWidth().padding(start = 22.dp),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Go),
                                keyboardActions = KeyboardActions(onGo = {
                                    if (jumpNumber.isNotBlank()) onJumpToHadith(jumpNumber.trim())
                                }),
                                textStyle = androidx.compose.ui.text.TextStyle(
                                    fontSize = 13.sp,
                                    color = if (isDarkTheme) Color.White else Color(0xFF1C1917)
                                )
                            )
                        }

                        // Dark Emerald Capsule Button
                        Box(
                            modifier = Modifier
                                .height(42.dp)
                                .clip(RoundedCornerShape(50.dp))
                                .background(Brush.linearGradient(listOf(Color(0xFF064E3B), Color(0xFF033527))))
                                .border(1.5.dp, Color(0x99C9A84C), RoundedCornerShape(50.dp))
                                .clickable {
                                    if (jumpNumber.isNotBlank()) onJumpToHadith(jumpNumber.trim())
                                }
                                .padding(horizontal = 20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = strings.jumpButton,
                                color = Color(0xFFFDE8A8),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.5.sp
                            )
                        }
                    }
                }
            }

            // Books / Section Cards List
            if (isLoading) {
                item {
                    Box(Modifier.fillMaxWidth().height(160.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF0E5D47))
                    }
                }
            } else {
                items(filteredBooks) { book ->
                    HadithSectionRowCard(
                        book = book,
                        isDarkTheme = isDarkTheme,
                        onClick = { onSelectBook(book) }
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }
        }

        // Bio Modal
        if (showBioModal) {
            HadithInfoModal(col = collection, onDismiss = { showBioModal = false })
        }
    }
}

// ── Section Card (Revelation, Belief, Knowledge, etc.) ──
@Composable
private fun HadithSectionRowCard(
    book: HadithBookItem,
    isDarkTheme: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isDarkTheme) Color(0xFF1F4335) else Color(0xFFEBE3D3)),
        color = if (isDarkTheme) Color(0xFF13271F) else Color.White,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Emerald Number Pill
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF064E3B))
                    .border(1.dp, Color(0xFFC9A84C), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = book.bookNumber,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(Modifier.width(14.dp))

            // Title
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = book.nameEn,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = if (isDarkTheme) Color.White else Color(0xFF1C1917)
                )
                if (book.numberOfHadith > 0) {
                    Text(
                        text = "${book.numberOfHadith} Hadith",
                        fontSize = 11.5.sp,
                        color = if (isDarkTheme) Color(0xFF9E948A) else Color(0xFF7A6F65)
                    )
                }
            }

            // Right arrow
            Text("›", fontSize = 20.sp, color = Color(0xFFC9A84C), fontWeight = FontWeight.Bold)
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// 3. CHAPTERS VIEW (Exact Screenshot 4)
// ══════════════════════════════════════════════════════════════════════════════
@Composable
private fun HadithChaptersView(
    collection: HadithCollectionItem,
    book: HadithBookItem,
    strings: com.tasbih.digitaltasbih.data.HadithStrings,
    isDarkTheme: Boolean,
    onBack: () -> Unit,
    onToggleTheme: () -> Unit,
    onOpenSettings: () -> Unit,
    onSelectChapter: (HadithChapterItem) -> Unit
) {
    var chapters by remember { mutableStateOf<List<HadithChapterItem>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(collection.name, book.bookNumber) {
        chapters = HadithRepository.getChapters(collection.name, book.bookNumber)
        isLoading = false
    }

    val filtered = remember(chapters, searchQuery) {
        if (searchQuery.isBlank()) chapters
        else chapters.filter {
            it.titleEn.contains(searchQuery, ignoreCase = true) ||
            it.chapterId.contains(searchQuery)
        }
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (isDarkTheme) Color(0xFF0C1914) else Color(0xFFFBF8F2))
            ) {
                HadithTopBar(
                    title = book.nameEn.uppercase(),
                    backLabel = strings.back,
                    isDarkTheme = isDarkTheme,
                    onBack = onBack,
                    onToggleTheme = onToggleTheme,
                    onOpenSettings = onOpenSettings
                )

                // Search Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                        .height(44.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(if (isDarkTheme) Color(0xFF182E24) else Color.White)
                        .border(1.dp, if (isDarkTheme) Color(0xFF28483B) else Color(0xFFE5E0D5), RoundedCornerShape(50.dp))
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🔍", fontSize = 14.sp, color = if (isDarkTheme) Color(0xFF9E948A) else Color(0xFF8C827A))
                        Spacer(Modifier.width(10.dp))
                        if (searchQuery.isEmpty()) {
                            Text(strings.searchChapters, color = Color(0xFF9E948A), fontSize = 13.5.sp)
                        }
                    }
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth().padding(start = 28.dp),
                        singleLine = true,
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 13.5.sp,
                            color = if (isDarkTheme) Color.White else Color(0xFF1C1917)
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isDarkTheme) Color(0xFF0C1914) else Color(0xFFFBF8F2))
                .padding(padding)
        ) {
            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF0E5D47))
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filtered) { chap ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { onSelectChapter(chap) },
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isDarkTheme) Color(0xFF1F4335) else Color(0xFFEBE3D3)),
                            color = if (isDarkTheme) Color(0xFF13271F) else Color.White,
                            shadowElevation = 2.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFF064E3B))
                                        .border(1.dp, Color(0xFFC9A84C), RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = chap.chapterNumber.ifEmpty { chap.chapterId.take(2) },
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }

                                Spacer(Modifier.width(14.dp))

                                Text(
                                    text = chap.titleEn,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.5.sp,
                                    color = if (isDarkTheme) Color.White else Color(0xFF1C1917),
                                    modifier = Modifier.weight(1f),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text("›", fontSize = 20.sp, color = Color(0xFFC9A84C), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// 4. HADITH READER VIEW (Exact Screenshot 5)
// ══════════════════════════════════════════════════════════════════════════════
@Composable
private fun HadithReaderView(
    collection: HadithCollectionItem,
    book: HadithBookItem,
    chapter: HadithChapterItem?,
    strings: com.tasbih.digitaltasbih.data.HadithStrings,
    isDarkTheme: Boolean,
    fontScale: Float,
    onBack: () -> Unit,
    onToggleTheme: () -> Unit,
    onOpenSettings: () -> Unit,
    onSpeak: (String, String) -> Unit,
    playingHadithNum: String?
) {
    val context = LocalContext.current
    var hadiths by remember { mutableStateOf<List<HadithDetailItem>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var currentPage by remember { mutableIntStateOf(1) }
    var hasMore by remember { mutableStateOf(true) }

    LaunchedEffect(collection.name, book.bookNumber, currentPage) {
        val list = HadithRepository.getHadiths(collection.name, book.bookNumber, currentPage)
        if (currentPage == 1) {
            hadiths = list
        } else {
            hadiths = hadiths + list
        }
        if (list.size < 20) hasMore = false
        isLoading = false
    }

    val filtered = remember(hadiths, chapter, searchQuery) {
        var base = hadiths
        if (chapter != null) {
            val cid = chapter.chapterId
            val filteredByChap = base.filter { it.chapterId == cid }
            if (filteredByChap.isNotEmpty()) base = filteredByChap
        }
        if (searchQuery.isNotBlank()) {
            base = base.filter {
                it.bodyText.contains(searchQuery, ignoreCase = true) ||
                it.arabicText.contains(searchQuery) ||
                it.hadithNumber == searchQuery
            }
        }
        base
    }

    val headerTitle = chapter?.titleEn?.take(22) ?: book.nameEn.uppercase()

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (isDarkTheme) Color(0xFF0C1914) else Color(0xFFFBF8F2))
            ) {
                HadithTopBar(
                    title = headerTitle.uppercase(),
                    backLabel = strings.back,
                    isDarkTheme = isDarkTheme,
                    onBack = onBack,
                    onToggleTheme = onToggleTheme,
                    onOpenSettings = onOpenSettings
                )

                // Search Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                        .height(44.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(if (isDarkTheme) Color(0xFF182E24) else Color.White)
                        .border(1.dp, if (isDarkTheme) Color(0xFF28483B) else Color(0xFFE5E0D5), RoundedCornerShape(50.dp))
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🔍", fontSize = 14.sp, color = if (isDarkTheme) Color(0xFF9E948A) else Color(0xFF8C827A))
                        Spacer(Modifier.width(10.dp))
                        if (searchQuery.isEmpty()) {
                            Text(strings.searchLoadedHadiths, color = Color(0xFF9E948A), fontSize = 13.5.sp)
                        }
                    }
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth().padding(start = 28.dp),
                        singleLine = true,
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 13.5.sp,
                            color = if (isDarkTheme) Color.White else Color(0xFF1C1917)
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isDarkTheme) Color(0xFF0C1914) else Color(0xFFFBF8F2))
                .padding(padding)
        ) {
            if (isLoading && hadiths.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF0E5D47))
                }
            } else if (filtered.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(strings.noHadiths, color = if (isDarkTheme) Color(0xFF9E948A) else Color(0xFF7A6F65), fontSize = 14.sp)
                }
            } else {
                val listState = rememberLazyListState()
                var isJumpDialogOpen by remember { mutableStateOf(false) }

                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                    items(filtered) { hd ->
                        HadithParchmentCard(
                            hd = hd,
                            collectionTitle = collection.titleEn,
                            isPlaying = playingHadithNum == hd.hadithNumber,
                            isDarkTheme = isDarkTheme,
                            fontScale = fontScale,
                            onPlayPause = {
                                val speechText = if (hd.arabicText.isNotBlank()) {
                                    "${hd.arabicText}. ${hd.narrator} ${hd.bodyText}"
                                } else {
                                    "${hd.narrator} ${hd.bodyText}"
                                }
                                onSpeak(speechText, hd.hadithNumber)
                            },
                            onBookmark = {
                                val item = HadithBookmarkItem(
                                    hadithNum = hd.hadithNumber,
                                    refLabel = "${collection.titleEn} ${hd.hadithNumber}",
                                    refUrl = hd.refUrl,
                                    arText = hd.arabicText,
                                    narrator = hd.narrator,
                                    bodyText = hd.bodyText,
                                    collection = hd.collection,
                                    bookNumber = hd.bookNumber
                                )
                                val added = HadithRepository.toggleBookmark(item)
                                AppToast.show(if (added) "Hadith bookmarked 💾" else "Bookmark removed")
                            },
                            onShare = {
                                val shareTxt = "${collection.titleEn} ${hd.hadithNumber}\n\n${hd.arabicText}\n\n${hd.narrator}\n${hd.bodyText}\n\n${hd.refUrl}"
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, shareTxt)
                                }
                                context.startActivity(Intent.createChooser(intent, "Share Hadith"))
                            },
                            onOpenSunnah = {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(hd.refUrl))
                                    context.startActivity(intent)
                                } catch (_: Exception) {}
                            }
                        )
                    }

                    // Load More Button
                    if (hasMore) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50.dp))
                                        .background(Color(0xFF064E3B))
                                        .border(1.dp, Color(0xFFC9A84C), RoundedCornerShape(50.dp))
                                        .clickable { currentPage++ }
                                        .padding(horizontal = 24.dp, vertical = 10.dp)
                                ) {
                                    Text(
                                        text = strings.loadMore,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // Floating Direct Hadith Quick-Jump Button (hadithFloatingBarJumpToHadith)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 20.dp, end = 16.dp)
                        .shadow(8.dp, RoundedCornerShape(50.dp))
                        .clip(RoundedCornerShape(50.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF0E5D47), Color(0xFF063A2C))
                            )
                        )
                        .border(1.5.dp, Color(0xFFE8C878), RoundedCornerShape(50.dp))
                        .clickable { isJumpDialogOpen = true }
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("🚀", fontSize = 13.sp)
                        Text(
                            text = "Hadith # Jump",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            if (isJumpDialogOpen) {
                val coroutineScope = rememberCoroutineScope()
                HadithQuickJumpDialog(
                    totalLoaded = filtered.size,
                    isDarkTheme = isDarkTheme,
                    onJump = { num ->
                        val idx = filtered.indexOfFirst {
                            it.hadithNumber == num || it.hadithNumber == num.trimStart('0')
                        }
                        if (idx >= 0) {
                            coroutineScope.launch {
                                listState.animateScrollToItem(idx)
                            }
                            AppToast.show("Jumped to Hadith #$num 🚀")
                        } else {
                            searchQuery = num
                            AppToast.show("Filtered to Hadith #$num")
                        }
                    },
                    onDismiss = { isJumpDialogOpen = false }
                )
            }
        }
    }
}
}

// ── Hadith Parchment Card (Exact Screenshot 5) ──
@Composable
private fun HadithParchmentCard(
    hd: HadithDetailItem,
    collectionTitle: String,
    isPlaying: Boolean,
    isDarkTheme: Boolean,
    fontScale: Float,
    onPlayPause: () -> Unit,
    onBookmark: () -> Unit,
    onShare: () -> Unit,
    onOpenSunnah: () -> Unit
) {
    val isBookmarked = remember(hd.hadithNumber) {
        HadithRepository.isBookmarked(hd.collection, hd.hadithNumber)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isDarkTheme) Color(0xFF1F4335) else Color(0x66C9A84C)),
        color = if (isDarkTheme) Color(0xFF13271F) else Color(0xFFFDFBF7),
        shadowElevation = 3.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Card Top Action Bar: Number Ring on left, Action Buttons on right
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Circular Ring with Hadith Number
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(if (isDarkTheme) Color(0xFF182E24) else Color(0xFFEBF4EF))
                        .border(1.5.dp, Color(0xFF0E5D47), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = hd.hadithNumber,
                        color = if (isDarkTheme) Color(0xFF34D399) else Color(0xFF0E5D47),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                // Action Buttons: Play/Pause Audio, Bookmark, Share
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Play Audio
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(if (isPlaying) Color(0xFF0E5D47) else if (isDarkTheme) Color(0xFF1C3A2E) else Color(0xFFF0EBE1))
                            .clickable { onPlayPause() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isPlaying) "⏸" else "▶",
                            fontSize = 12.sp,
                            color = if (isPlaying) Color.White else if (isDarkTheme) Color(0xFF34D399) else Color(0xFF0E5D47)
                        )
                    }

                    // Bookmark
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(if (isDarkTheme) Color(0xFF1C3A2E) else Color(0xFFF0EBE1))
                            .clickable { onBookmark() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(if (isBookmarked) "🔖" else "🏷️", fontSize = 13.sp)
                    }

                    // Share
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(if (isDarkTheme) Color(0xFF1C3A2E) else Color(0xFFF0EBE1))
                            .clickable { onShare() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("↗", fontSize = 14.sp, color = if (isDarkTheme) Color(0xFF34D399) else Color(0xFF0E5D47), fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Triple Gold Diamond Stars Separator
            Spacer(Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("✦ ★ ✦", color = Color(0xFFC9A84C), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(10.dp))

            // Large Arabic Hadith Matn (AmiriQuranFont scaled with fontScale)
            if (hd.arabicText.isNotEmpty()) {
                Text(
                    text = hd.arabicText,
                    fontFamily = AmiriQuranFont,
                    fontSize = (20 * fontScale).sp,
                    lineHeight = (34 * fontScale).sp,
                    color = if (isDarkTheme) Color(0xFFFBF8F2) else Color(0xFF1C1917),
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("✦ ★ ✦", color = Color(0xFFC9A84C), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(10.dp))
            }

            // Italic Narrator Attribution
            if (hd.narrator.isNotEmpty()) {
                Text(
                    text = hd.narrator,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = (13.5 * fontScale).sp,
                    color = if (isDarkTheme) Color(0xFFC4B5A5) else Color(0xFF5D4D38),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("✦ ★ ✦", color = Color(0xFFC9A84C), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(10.dp))
            }

            // Translation Text (scaled with fontScale)
            Text(
                text = hd.bodyText,
                fontSize = (14 * fontScale).sp,
                lineHeight = (22 * fontScale).sp,
                color = if (isDarkTheme) Color(0xFFE6E2D8) else Color(0xFF1C1917),
                modifier = Modifier.fillMaxWidth()
            )

            // Authenticity Grade Badge
            if (hd.grade.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isDarkTheme) Color(0xFF1B3B2B) else Color(0xFFE8F5E9))
                        .border(1.dp, if (isDarkTheme) Color(0xFF2E6349) else Color(0xFFA5D6A7), RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "✓ ${hd.grade}${if (hd.gradedBy.isNotBlank() && !hd.gradedBy.equals("null", ignoreCase = true)) " — ${hd.gradedBy}" else ""}",
                        color = if (isDarkTheme) Color(0xFF86EFAC) else Color(0xFF1B5E20),
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            // Bottom Reference Box with Sunnah.com Link (Exact Screenshot 5)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isDarkTheme) Color(0xFF182E24) else Color(0xFFF9F6EE))
                    .border(1.dp, if (isDarkTheme) Color(0xFF28483B) else Color(0xFFE8DFC7), RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "$collectionTitle ${hd.hadithNumber}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (isDarkTheme) Color.White else Color(0xFF1C1917)
                        )
                        if (hd.inBookRef.isNotEmpty()) {
                            Text(
                                text = "In-book reference : ${hd.inBookRef}",
                                fontSize = 11.sp,
                                color = if (isDarkTheme) Color(0xFF9E948A) else Color(0xFF7A6F65)
                            )
                        }
                    }

                    // Sunnah.com ↗ CTA
                    Text(
                        text = "Sunnah.com ↗",
                        color = Color(0xFFC9A84C),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        modifier = Modifier.clickable { onOpenSunnah() }
                    )
                }
            }
        }
    }
}

// ── Bookmark Card ──
@Composable
private fun HadithBookmarkCard(bm: HadithBookmarkItem) {
    val context = LocalContext.current
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEBE3D3)),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(bm.refLabel, fontWeight = FontWeight.Bold, fontSize = 14.5.sp, color = Color(0xFF0E5D47))
                Text(
                    text = "Sunnah.com ↗",
                    color = Color(0xFFC9A84C),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable {
                        try {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(bm.refUrl)))
                        } catch (_: Exception) {}
                    }
                )
            }
            if (bm.arText.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = bm.arText,
                    fontFamily = AmiriQuranFont,
                    fontSize = 16.sp,
                    color = Color(0xFF1C1917),
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.height(6.dp))
            Text(
                text = bm.bodyText,
                fontSize = 12.5.sp,
                color = Color(0xFF5D4D38),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ── Collection Info / Bio Modal ──
@Composable
private fun HadithInfoModal(
    col: HadithCollectionItem,
    isDarkTheme: Boolean = false,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            color = if (isDarkTheme) Color(0xFF13271F) else Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, if (isDarkTheme) Color(0xFF1F4335) else Color(0xFFEBE3D3)),
            shadowElevation = 16.dp
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = col.titleEn,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = if (isDarkTheme) Color(0xFFFEF08A) else Color(0xFF0E5D47)
                    )
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(if (isDarkTheme) Color(0xFF1F4335) else Color(0xFFF0EBE1))
                            .clickable { onDismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("✕", fontSize = 13.sp, color = if (isDarkTheme) Color(0xFFFEF08A) else Color(0xFF5D4D38))
                    }
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text = col.shortIntro.ifEmpty { "No biography available." },
                    fontSize = 13.5.sp,
                    lineHeight = 20.sp,
                    color = if (isDarkTheme) Color(0xFFE6E2D8) else Color(0xFF3D3222)
                )

                Spacer(Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xFF0E5D47))
                        .clickable { onDismiss() }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Close", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// 5. UNIFIED LUXURY TOP BAR (Theme Toggle + Settings Gear)
// ══════════════════════════════════════════════════════════════════════════════
@Composable
private fun HadithTopBar(
    title: String,
    backLabel: String,
    isDarkTheme: Boolean,
    onBack: () -> Unit,
    onToggleTheme: () -> Unit,
    onOpenSettings: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isDarkTheme) Color(0xFF0C1914) else Color(0xFFFBF8F2))
            .statusBarsPadding()
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Back Pill [ ← वापस ]
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(if (isDarkTheme) Color(0xFF182E24) else Color.White)
                    .border(1.dp, if (isDarkTheme) Color(0xFF28483B) else Color(0xFFE2D9C8), RoundedCornerShape(50.dp))
                    .clickable { onBack() }
                    .padding(horizontal = 14.dp, vertical = 7.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "←",
                        color = if (isDarkTheme) Color(0xFF34D399) else Color(0xFF0E5D47),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        backLabel,
                        color = if (isDarkTheme) Color(0xFF34D399) else Color(0xFF0E5D47),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            // Title in Golden/Emerald Serif Caps
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = title,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = if (isDarkTheme) Color(0xFFFEF08A) else Color(0xFF0E5D47),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
                Box(
                    modifier = Modifier
                        .width(36.dp)
                        .height(2.dp)
                        .background(Brush.horizontalGradient(listOf(Color.Transparent, Color(0xFFC9A84C), Color.Transparent)))
                )
            }

            // Right icons (Theme Toggle + Settings Gear)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Theme Toggle circle
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(if (isDarkTheme) Color(0xFF182E24) else Color.White)
                        .border(1.dp, if (isDarkTheme) Color(0xFF28483B) else Color(0xFFE2D9C8), CircleShape)
                        .clickable { onToggleTheme() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(if (isDarkTheme) "🌙" else "☀️", fontSize = 15.sp)
                }

                // Settings Gear circle
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(if (isDarkTheme) Color(0xFF182E24) else Color.White)
                        .border(1.dp, if (isDarkTheme) Color(0xFF28483B) else Color(0xFFE2D9C8), CircleShape)
                        .clickable { onOpenSettings() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("⚙️", fontSize = 15.sp)
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// 6. 3D BOOK COVER TYPOGRAPHY CONTENT (Exact Image 1 & 2)
// ══════════════════════════════════════════════════════════════════════════════
private fun formatCoverTitle(title: String): String {
    val clean = title.trim().uppercase()
    return when {
        clean.contains("AL-BUKHARI") || clean.contains("BUKHARI") -> "SAHIH AL-\nBUKHARI"
        clean.contains("MUSLIM") -> "SAHIH\nMUSLIM"
        clean.contains("AN-NASA'I") || clean.contains("AN-NASAI") || clean.contains("NASA'I") || clean.contains("NASAI") -> "SUNAN AN-\nNASA'I"
        clean.contains("ABI DAWUD") || clean.contains("ABU DAWUD") || clean.contains("DAWUD") -> "SUNAN ABI\nDAWUD"
        clean.contains("TIRMIDHI") -> "JAMI` AT-\nTIRMIDHI"
        clean.contains("IBN MAJAH") || clean.contains("MAJAH") -> "SUNAN IBN\nMAJAH"
        clean.contains("MUWATTA") || clean.contains("MALIK") -> "MUWATTA\nMALIK"
        clean.contains("MUSNAD AHMAD") || clean.contains("AHMAD") -> "MUSNAD\nAHMAD"
        clean.contains("DARIMI") -> "SUNAN AD-\nDARIMI"
        clean.contains("RIYAD") -> "RIYADUS\nSALIHEEN"
        clean.contains("MISHKAT") -> "MISHKAT AL-\nMASABIH"
        clean.contains("BULUGH") -> "BULUGH AL-\nMARAM"
        clean.contains("NAWAWI") -> "40 HADITH\nNAWAWI"
        clean.contains("QUDSI") -> "40 HADITH\nQUDSI"
        clean.contains("SHAMA'IL") || clean.contains("SHAMAIL") -> "SHAMA'IL\nMUHAMMADIYAH"
        clean.contains("HISN") -> "HISN AL-\nMUSLIM"
        clean.contains("ADAB") -> "AL-ADAB\nAL-MUFRAD"
        else -> {
            val words = clean.split(" ").filter { it.isNotBlank() }
            if (words.size == 2) {
                "${words[0]}\n${words[1]}"
            } else if (words.size > 2) {
                val mid = (words.size + 1) / 2
                words.take(mid).joinToString(" ") + "\n" + words.drop(mid).joinToString(" ")
            } else {
                clean
            }
        }
    }
}

@Composable
private fun HadithCoverContent(
    col: HadithCollectionItem,
    isDarkTheme: Boolean,
    isFloating: Boolean
) {
    val idx = remember(col.coverAsset) {
        val i = HadithRepository.SPECIAL_COVERS.indexOf(col.coverAsset)
        if (i >= 0) i else 0
    }
    // Matches original index.html rule: idx 0, 4, 8, 16, 20, 24 are light cream covers
    val isLightCover = (idx == 0 || idx == 4 || idx == 8 || idx == 16 || idx == 20 || idx == 24)

    val titleEnColor = if (isLightCover) Color(0xFF1E150A) else Color.White
    val titleArColor = if (isLightCover) Color(0xFF1A1208) else Color(0xFFFDE047)
    val countColor = if (isLightCover) Color(0xFF2D2115) else Color(0xFFFDE047)
    val countBg = if (isLightCover) Color(0x33D97706) else Color(0x77000000)
    val countBorder = if (isLightCover) Color(0x99B8860B) else Color(0x88FACC15)

    val formattedTitle = remember(col.titleEn) { formatCoverTitle(col.titleEn) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = if (isFloating) 40.dp else 46.dp,
                bottom = if (isFloating) 36.dp else 42.dp,
                start = 10.dp,
                end = 10.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // English Title (Uppercase, 2 lines, Bold Sans-Serif strictly inside arch)
            Text(
                text = formattedTitle,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.ExtraBold,
                fontSize = if (isFloating) 12.5.sp else 13.sp,
                letterSpacing = 0.4.sp,
                color = titleEnColor,
                textAlign = TextAlign.Center,
                lineHeight = if (isFloating) 14.5.sp else 15.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(2.dp))

            // Arabic Title (Prominent Calligraphy strictly inside arch)
            if (col.titleAr.isNotEmpty()) {
                Text(
                    text = col.titleAr,
                    fontFamily = AmiriQuranFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = if (isFloating) 18.sp else 19.sp,
                    color = titleArColor,
                    textAlign = TextAlign.Center,
                    lineHeight = if (isFloating) 21.sp else 22.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(3.dp))
            }

            // Hadith Count Pill (Strictly inside arch above rosette)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(countBg)
                    .border(1.dp, countBorder, RoundedCornerShape(50.dp))
                    .padding(
                        horizontal = if (isFloating) 7.dp else 9.dp,
                        vertical = if (isFloating) 1.5.dp else 2.dp
                    ),
                contentAlignment = Alignment.Center
            ) {
                val formattedCount = try {
                    java.text.NumberFormat.getInstance().format(col.totalHadith.toString().replace(",", "").toLong())
                } catch (_: Exception) {
                    col.totalHadith.toString()
                }
                Text(
                    text = "$formattedCount Hadith",
                    color = countColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = if (isFloating) 10.sp else 10.5.sp,
                    letterSpacing = 0.3.sp,
                    maxLines = 1
                )
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// 7. HADITH SETTINGS VIEW (Exact Screenshot 4)
// ══════════════════════════════════════════════════════════════════════════════
private data class HadithLangOption(
    val code: String,
    val badge: String,
    val name: String
)

@Composable
private fun HadithSettingsView(
    strings: com.tasbih.digitaltasbih.data.HadithStrings,
    isDarkTheme: Boolean,
    fontScale: Float,
    selectedLang: String,
    onFontScaleChange: (Float) -> Unit,
    onSelectLang: (String) -> Unit,
    onBack: () -> Unit
) {
    val languages = listOf(
        HadithLangOption("en", "EN", "English"),
        HadithLangOption("ar", "AR", "العربية (Arabic)"),
        HadithLangOption("ur", "UR", "اردو (Urdu)"),
        HadithLangOption("bn", "BN", "বাংলা (Bangla)"),
        HadithLangOption("hi", "HI", "हिंदी (Urdu से लिप्यंतरण)")
    )

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (isDarkTheme) Color(0xFF0C1914) else Color(0xFFFBF8F2))
                    .statusBarsPadding()
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Back Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(if (isDarkTheme) Color(0xFF182E24) else Color.White)
                            .border(1.dp, if (isDarkTheme) Color(0xFF28483B) else Color(0xFFE2D9C8), RoundedCornerShape(50.dp))
                            .clickable { onBack() }
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "←",
                                color = if (isDarkTheme) Color(0xFF34D399) else Color(0xFF0E5D47),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                strings.back,
                                color = if (isDarkTheme) Color(0xFF34D399) else Color(0xFF0E5D47),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }

                    Spacer(Modifier.width(16.dp))

                    Text(
                        text = strings.settingsTitle,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = if (isDarkTheme) Color(0xFFFEF08A) else Color(0xFF0E5D47)
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isDarkTheme) Color(0xFF0C1914) else Color(0xFFFBF8F2))
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // SECTION 1: Font Size
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = if (isDarkTheme) Color(0xFF13271F) else Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isDarkTheme) Color(0xFF1F4335) else Color(0xFFEBE3D3)),
                shadowElevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "🔤 ${strings.fontSize}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.5.sp,
                        color = if (isDarkTheme) Color.White else Color(0xFF1C1917)
                    )

                    Spacer(Modifier.height(10.dp))

                    // Sleek Minimal Pill Slider
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(elevation = 1.dp, shape = RoundedCornerShape(50.dp), spotColor = Color(0x140E5D47))
                            .clip(RoundedCornerShape(50.dp))
                            .background(if (isDarkTheme) Color(0xFF1C382C) else Color(0xFFFBF9F4))
                            .border(
                                width = 1.dp,
                                color = if (isDarkTheme) Color(0xFF285240) else Color(0xFF0E5D47).copy(alpha = 0.2f),
                                shape = RoundedCornerShape(50.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 3.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Smaller font (A-)
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(if (isDarkTheme) Color(0xFF244738) else Color(0xFFF1F7F4))
                                    .clickable {
                                        onFontScaleChange((fontScale - 0.1f).coerceAtLeast(0.8f))
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "A-",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDarkTheme) Color(0xFF34D399) else Color(0xFF0E5D47)
                                )
                            }

                            // Slim Custom Slider
                            @OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
                            Slider(
                                value = fontScale,
                                onValueChange = onFontScaleChange,
                                valueRange = 0.8f..1.6f,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(28.dp),
                                thumb = {
                                    Box(
                                        modifier = Modifier
                                            .size(15.dp)
                                            .shadow(2.dp, CircleShape)
                                            .background(Color(0xFF0E5D47), CircleShape)
                                            .border(1.5.dp, Color.White, CircleShape)
                                    )
                                },
                                track = { sliderState ->
                                    SliderDefaults.Track(
                                        sliderState = sliderState,
                                        modifier = Modifier.height(3.5.dp),
                                        colors = SliderDefaults.colors(
                                            activeTrackColor = Color(0xFF0E5D47),
                                            inactiveTrackColor = if (isDarkTheme) Color(0xFF1F4335) else Color(0xFFE2EDE7)
                                        ),
                                        drawStopIndicator = null
                                    )
                                }
                            )

                            // Larger font (A+)
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(if (isDarkTheme) Color(0xFF244738) else Color(0xFFF1F7F4))
                                    .clickable {
                                        onFontScaleChange((fontScale + 0.1f).coerceAtMost(1.6f))
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "A+",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDarkTheme) Color(0xFF34D399) else Color(0xFF0E5D47)
                                )
                            }

                            // Percentage indicator badge - tap to reset to 100%
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isDarkTheme) Color(0xFF244738) else Color(0xFFEAF5EE))
                                    .border(
                                        0.8.dp,
                                        if (isDarkTheme) Color(0xFF34D399).copy(alpha = 0.3f) else Color(0xFF0E5D47).copy(alpha = 0.25f),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable { onFontScaleChange(1.0f) }
                                    .padding(horizontal = 7.dp, vertical = 2.5.dp)
                            ) {
                                Text(
                                    text = "${(fontScale * 100).toInt()}%",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isDarkTheme) Color(0xFF34D399) else Color(0xFF0E5D47)
                                )
                            }
                        }
                    }
                }
            }

            // SECTION 2: Language Selector
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "🌐 ${strings.language}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = if (isDarkTheme) Color(0xFFFEF08A) else Color(0xFF1C1917),
                    modifier = Modifier.padding(start = 4.dp)
                )

                languages.forEach { lang ->
                    val isSelected = lang.code == selectedLang
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onSelectLang(lang.code) },
                        shape = RoundedCornerShape(16.dp),
                        color = if (isDarkTheme) {
                            if (isSelected) Color(0xFF183B2B) else Color(0xFF13271F)
                        } else {
                            if (isSelected) Color(0xFFEBF4EF) else Color.White
                        },
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) Color(0xFFC9A84C) else if (isDarkTheme) Color(0xFF1F4335) else Color(0xFFEBE3D3)
                        ),
                        shadowElevation = if (isSelected) 3.dp else 1.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 13.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Circular 2-letter badge
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF0E5D47)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = lang.badge,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }

                            Spacer(Modifier.width(14.dp))

                            Text(
                                text = lang.name,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                fontSize = 14.5.sp,
                                color = if (isDarkTheme) Color.White else Color(0xFF1C1917),
                                modifier = Modifier.weight(1f)
                            )

                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFC9A84C)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("✓", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun NumberFormat(num: Int): String {
    return java.text.NumberFormat.getIntegerInstance(Locale.US).format(num)
}
