package com.tasbih.digitaltasbih.ui.screens.quran

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.foundation.layout.navigationBarsPadding
import com.tasbih.digitaltasbih.ui.components.QuranTranslationFloatingBar
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.tasbih.digitaltasbih.ui.components.AppToast
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.data.AyahItem
import com.tasbih.digitaltasbih.data.AyahWordItem
import com.tasbih.digitaltasbih.data.MushafPageData
import com.tasbih.digitaltasbih.data.MushafWordItem
import com.tasbih.digitaltasbih.data.QuranAudioPlayer
import com.tasbih.digitaltasbih.data.QuranSurahAudioEngine
import com.tasbih.digitaltasbih.data.QuranWordAudioPlayer
import com.tasbih.digitaltasbih.data.QuranData
import com.tasbih.digitaltasbih.data.QuranReadingMode
import com.tasbih.digitaltasbih.data.QuranRepository
import com.tasbih.digitaltasbih.data.QuranReadTimeRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import com.tasbih.digitaltasbih.data.QuranSettingsState
import com.tasbih.digitaltasbih.data.toArabicDigits
import com.tasbih.digitaltasbih.ui.components.rememberAssetPainter
import com.tasbih.digitaltasbih.ui.components.QuranAudioModeButton
import com.tasbih.digitaltasbih.ui.theme.AmiriBoldFont
import com.tasbih.digitaltasbih.ui.theme.AmiriQuranFont
import com.tasbih.digitaltasbih.ui.theme.GoldAccent
import com.tasbih.digitaltasbih.util.TajweedWaqfHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun QuranReaderScreen(
    type: String, // "surah" or "juz"
    number: Int,
    onBack: () -> Unit,
    onOpenSettings: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Real-time active reading session time tracker (logs every 10 seconds)
    LaunchedEffect(Unit) {
        while (isActive) {
            delay(10_000L)
            QuranReadTimeRepository.logSessionSeconds(context, 10)
        }
    }

    var currentNumber by remember { mutableIntStateOf(number) }
    var currentType by remember { mutableStateOf(type) }

    var settings by remember { mutableStateOf(QuranRepository.loadSettings()) }
    var ayahs by remember { mutableStateOf<List<AyahItem>>(emptyList()) }
    var mushafPage by remember { mutableStateOf<MushafPageData?>(null) }
    var currentMushafPageNum by remember { mutableIntStateOf(1) }
    var isLoading by remember { mutableStateOf(true) }

    var isZoomBarVisible by remember { mutableStateOf(false) }
    var isLangModalOpen by remember { mutableStateOf(false) }
    var isTajweedGuideOpen by remember { mutableStateOf(false) }
    var isThemePickerOpen by remember { mutableStateOf(false) }
    var selectedWordInfo by remember { mutableStateOf<Pair<AyahWordItem, Int>?>(null) }

    // Map of open tafsirs by globalNumber -> tafsir text
    val openTafsirs = remember { mutableStateListOf<Int>() }
    val tafsirTexts = remember { androidx.compose.runtime.mutableStateMapOf<Int, String>() }

    // Stop audio when leaving screen
    DisposableEffect(Unit) {
        onDispose {
            QuranAudioPlayer.dismissTranslationBar()
            QuranAudioPlayer.stop()
            QuranWordAudioPlayer.stop()
            QuranSurahAudioEngine.stop()
        }
    }

    // Pre-cache adjacent Mushaf pages in memory for 0ms latency transitions
    LaunchedEffect(currentMushafPageNum, settings.translationLanguage) {
        coroutineScope.launch(Dispatchers.IO) {
            if (currentMushafPageNum < 604) {
                QuranRepository.fetchMushafPage(currentMushafPageNum + 1, settings.translationLanguage)
            }
            if (currentMushafPageNum > 1) {
                QuranRepository.fetchMushafPage(currentMushafPageNum - 1, settings.translationLanguage)
            }
        }
    }

    // Auto-advance Mushaf page when full Surah recitation progresses past the current page
    LaunchedEffect(
        QuranSurahAudioEngine.currentSurahNum,
        QuranSurahAudioEngine.currentAyahIndexInSurah,
        QuranSurahAudioEngine.isPlaying
    ) {
        if (!QuranSurahAudioEngine.isPlaying && !QuranSurahAudioEngine.isLoading) return@LaunchedEffect
        val playingSurah = QuranSurahAudioEngine.currentSurahNum ?: return@LaunchedEffect
        val playingAyah = QuranSurahAudioEngine.currentAyahIndexInSurah
        val curPage = mushafPage ?: return@LaunchedEffect

        // Check verses belonging to the playing Surah on the current page
        val versesOnCurPage = curPage.lines
            .flatMap { it.words }
            .filter { it.surahNumber == playingSurah && it.verseNumber > 0 }
            .map { it.verseNumber }

        if (versesOnCurPage.isNotEmpty()) {
            val maxVerse = versesOnCurPage.maxOrNull() ?: 0
            if (playingAyah > maxVerse && currentMushafPageNum < 604) {
                // Next page of the same Surah!
                val nextPage = currentMushafPageNum + 1
                currentMushafPageNum = nextPage
                val nextData = QuranRepository.fetchMushafPage(nextPage, settings.translationLanguage)
                if (nextData != null) {
                    mushafPage = nextData
                }
            }
        } else if (playingSurah > curPage.surahNumber) {
            // Next Surah started! (e.g. Surah 1 finished and Surah 2 began)
            val targetPage = QuranData.getSurahStartPage(playingSurah)
            if (targetPage != currentMushafPageNum) {
                currentMushafPageNum = targetPage
                val pData = QuranRepository.fetchMushafPage(targetPage, settings.translationLanguage)
                if (pData != null) {
                    mushafPage = pData
                }
            }
        }
    }

    // Load data whenever number, type, or language changes
    LaunchedEffect(currentType, currentNumber, settings.readingMode, settings.translationId, settings.translationLanguage) {
        isLoading = true
        if (settings.readingMode == QuranReadingMode.MUSHAF) {
            // In Mushaf mode, load page
            val startPage = if (currentType == "surah") {
                QuranData.getSurahStartPage(currentNumber)
            } else {
                (currentNumber - 1) * 20 + 1
            }
            currentMushafPageNum = startPage
            val page = QuranRepository.fetchMushafPage(startPage, settings.translationLanguage)
            mushafPage = page
            isLoading = false
        } else {
            // List or Flowing Translation mode
            val list = if (currentType == "surah") {
                QuranRepository.fetchSurahAyahs(currentNumber, settings.translationId, settings.translationLanguage)
            } else {
                QuranRepository.fetchJuzAyahs(currentNumber, settings.translationId, settings.translationLanguage)
            }
            ayahs = list
            isLoading = false

            // Asynchronously attach word-by-word data for Surah in selected language
            if (currentType == "surah") {
                coroutineScope.launch {
                    val wordsMap = QuranRepository.fetchSurahWords(currentNumber, settings.translationLanguage)
                    if (wordsMap.isNotEmpty()) {
                        ayahs = QuranRepository.attachWordsToAyahs(ayahs, wordsMap)
                    }
                }
            }

            // Update Last Read
            val title = if (currentType == "surah") {
                val s = QuranData.surahs.find { it.number == currentNumber }
                "${s?.getLocalizedName(settings.translationLanguage) ?: "Surah"} • ${s?.arabicName ?: ""}"
            } else {
                if (settings.translationLanguage == "en") "Juz $currentNumber" else "पारा $currentNumber"
            }
            QuranRepository.setLastRead(currentType, currentNumber, title)
        }
    }

    val surahMeta = remember(currentNumber, currentType, settings.translationLanguage) {
        if (currentType == "surah") QuranData.surahs.find { it.number == currentNumber } else null
    }

    val titleLocalized = surahMeta?.let { "${it.getLocalizedName(settings.translationLanguage)} •" }
        ?: (if (settings.translationLanguage == "en") "JUZ $currentNumber •" else "पारा $currentNumber •")
    val titleArabic = surahMeta?.arabicName ?: "${currentNumber} جزء"

    val isNightMode = settings.isNightMode
    val screenBg = if (isNightMode) Color(0xFF0A1410) else Color(0xFFF9F7F1)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(screenBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
        // 1. TOP BAR (Exact Screenshot: ← Back, AL-FATIHAH • \n الفاتحة, ☀️/🌙, 🇬🇧/🇮🇳, AA, ⚙️)
        ReaderTopBar(
            titleLocalized = titleLocalized,
            titleArabic = titleArabic,
            langCode = settings.translationLanguage,
            isNightMode = isNightMode,
            onBack = onBack,
            onToggleNightMode = {
                val updated = settings.copy(isNightMode = !settings.isNightMode)
                settings = updated
                QuranRepository.saveSettings(updated)
            },
            onToggleZoom = { isZoomBarVisible = !isZoomBarVisible },
            onOpenLangModal = { isLangModalOpen = true },
            onOpenSettings = onOpenSettings,
            onOpenTajweedGuide = { isTajweedGuideOpen = true }
        )

        // 2. QUICK FONT ZOOM SLIDER BAR (Appears directly below Top Bar when AA tapped)
        AnimatedVisibility(visible = isZoomBarVisible) {
            QuranZoomSliderBar(
                fontSizeSp = settings.arabicFontSizeSp,
                isNightMode = isNightMode,
                onFontSizeChange = { newSize ->
                    settings = settings.copy(arabicFontSizeSp = newSize)
                    QuranRepository.saveSettings(settings)
                }
            )
        }

        // 3. LANGUAGE MODAL (Translation Language picker)
        if (isLangModalOpen) {
            QuranLanguageModal(
                currentLanguage = settings.translationLanguage,
                onSelectLanguage = { code ->
                    val updated = QuranRepository.switchLanguage(code)
                    settings = updated
                    isLangModalOpen = false
                },
                onDismiss = { isLangModalOpen = false }
            )
        }

        // 3.5 TAJWEED & WAQF GUIDE MODAL
        if (isTajweedGuideOpen) {
            TajweedWaqfGuideModal(
                langCode = settings.translationLanguage,
                isNightMode = isNightMode,
                onDismiss = { isTajweedGuideOpen = false }
            )
        }

        // 3.6 MUSHAF THEME PICKER MODAL
        if (isThemePickerOpen) {
            MushafThemePickerModal(
                currentThemeId = settings.mushafThemeId,
                langCode = settings.translationLanguage,
                isNightMode = isNightMode,
                onSelectTheme = { selectedTheme ->
                    val updated = settings.copy(mushafThemeId = selectedTheme.id)
                    settings = updated
                    QuranRepository.saveSettings(updated)
                },
                onDismiss = { isThemePickerOpen = false }
            )
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = if (isNightMode) Color(0xFF6EE7B7) else Color(0xFF0D4A3E))
                    Spacer(modifier = Modifier.height(12.dp))
                    val loadingText = when (settings.translationLanguage) {
                        "en" -> "Loading Quran Ayahs..."
                        "ur" -> "آیات لوڈ ہو رہی ہیں..."
                        "ar" -> "جارٍ تحميل الآيات..."
                        else -> "कुरान की आयतें लोड हो रही हैं..."
                    }
                    Text(
                        text = loadingText,
                        color = if (isNightMode) Color(0xFFE2E8F0) else Color(0xFF0D4A3E),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            if (settings.readingMode == QuranReadingMode.MUSHAF) {
                // ══════════════════════════════════════════════════════════════
                // REAL MUSHAF 15-LINE PAGE VIEW
                // ══════════════════════════════════════════════════════════════
                mushafPage?.let { page ->
                    MushafPageContent(
                        pageData = page,
                        pageNum = currentMushafPageNum,
                        settings = settings,
                        onUpdateSettings = { settings = it },
                        fontSizeSp = settings.arabicFontSizeSp,
                        isNightMode = isNightMode,
                        onOpenThemePicker = { isThemePickerOpen = true },
                        onPrevPage = {
                            if (currentMushafPageNum > 1) {
                                currentMushafPageNum--
                                coroutineScope.launch {
                                    isLoading = true
                                    mushafPage = QuranRepository.fetchMushafPage(currentMushafPageNum, settings.translationLanguage)
                                    isLoading = false
                                }
                            }
                        },
                        onNextPage = {
                            if (currentMushafPageNum < 604) {
                                currentMushafPageNum++
                                coroutineScope.launch {
                                    isLoading = true
                                    mushafPage = QuranRepository.fetchMushafPage(currentMushafPageNum, settings.translationLanguage)
                                    isLoading = false
                                }
                            }
                        }
                    )
                } ?: run {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        val errorPageText = when (settings.translationLanguage) {
                            "en" -> "Could not load page"
                            "ur" -> "صفحہ لوڈ نہیں ہو سکا"
                            "ar" -> "تعذر تحميل الصفحة"
                            else -> "पृष्ठ लोड नहीं हो सका"
                        }
                        Text(text = errorPageText, color = if (isNightMode) Color(0xFF94A3B8) else Color.Gray)
                    }
                }
            } else {
                // ══════════════════════════════════════════════════════════════
                // LIST VIEW (Ayat by Ayat Ornate Ribbon Cards)
                // ══════════════════════════════════════════════════════════════
                val listState = rememberLazyListState()

                // Auto-scroll list when Surah audio advances
                LaunchedEffect(QuranSurahAudioEngine.currentGlobalAyahNum, QuranSurahAudioEngine.isPlaying) {
                    if (QuranSurahAudioEngine.isPlaying && settings.autoScroll) {
                        val currGlobal = QuranSurahAudioEngine.currentGlobalAyahNum
                        if (currGlobal != null) {
                            val targetIdx = ayahs.indexOfFirst { it.globalNumber == currGlobal }
                            if (targetIdx != -1) {
                                listState.animateScrollToItem(targetIdx)
                            }
                        }
                    }
                }

                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    contentPadding = PaddingValues(
                        top = 8.dp,
                        bottom = if (QuranAudioPlayer.isTranslationBarVisible) 110.dp else 40.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Top Surah Header Banner with Play/Pause button in List View
                    if (currentType == "surah") {
                        item {
                            val sMeta = QuranData.surahs.getOrNull(currentNumber - 1)
                            val surahArabic = sMeta?.arabicName ?: ""
                            val surahEnglish = sMeta?.englishName ?: ""
                            val totalAyahs = sMeta?.numberOfAyahs ?: ayahs.size
                            val surahType = sMeta?.revelationType ?: "Meccan"

                            val isCurrentSurahActive = (QuranSurahAudioEngine.currentSurahNum == currentNumber)
                            val isCurrentSurahPlaying = isCurrentSurahActive && QuranSurahAudioEngine.isPlaying
                            val isCurrentSurahLoading = isCurrentSurahActive && QuranSurahAudioEngine.isLoading

                            val bannerBrush = if (isNightMode) {
                                Brush.linearGradient(listOf(Color(0xFF041C13), Color(0xFF0A3323), Color(0xFF041C13)))
                            } else {
                                Brush.linearGradient(listOf(Color(0xFF062B1E), Color(0xFF0C4331), Color(0xFF062B1E)))
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 2.dp, vertical = 4.dp)
                                    .shadow(2.dp, RoundedCornerShape(12.dp))
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(bannerBrush)
                                    .border(1.4.dp, GoldAccent, RoundedCornerShape(12.dp))
                                    .padding(vertical = 10.dp, horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(
                                        text = "سُورَةُ $surahArabic",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontFamily = AmiriBoldFont
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "$surahEnglish • $totalAyahs Ayahs • $surahType",
                                        fontSize = 11.5.sp,
                                        color = Color(0xFFD4AF37)
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    QuranAudioModeButton(
                                        audioPlayMode = settings.audioPlayMode,
                                        translationVoiceLang = settings.translationVoiceLang,
                                        language = settings.translationLanguage,
                                        onModeChange = { newMode, newVoiceLang ->
                                            val updated = settings.copy(audioPlayMode = newMode, translationVoiceLang = newVoiceLang)
                                            settings = updated
                                            QuranRepository.saveSettings(updated)
                                            QuranSurahAudioEngine.setPlayMode(newMode, newVoiceLang)
                                        },
                                        isNightMode = isNightMode
                                    )

                                    val playBtnBrush = if (isCurrentSurahPlaying) {
                                        Brush.linearGradient(listOf(Color(0xFFD4AF37), Color(0xFFB45309)))
                                    } else {
                                        if (isNightMode) {
                                            Brush.linearGradient(listOf(Color(0xFF1B4E3E), Color(0xFF0B2B20)))
                                        } else {
                                            Brush.linearGradient(listOf(Color(0xFF146450), Color(0xFF093B2E)))
                                        }
                                    }

                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .shadow(3.dp, CircleShape)
                                            .clip(CircleShape)
                                            .background(playBtnBrush)
                                            .border(1.4.dp, GoldAccent, CircleShape)
                                            .clickable {
                                                if (isCurrentSurahActive) {
                                                    QuranSurahAudioEngine.togglePlayPause()
                                                } else {
                                                    QuranAudioPlayer.stop()
                                                    QuranWordAudioPlayer.stop()
                                                    selectedWordInfo = null
                                                    QuranSurahAudioEngine.playSurah(currentNumber)
                                                }
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (isCurrentSurahLoading) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(16.dp),
                                                strokeWidth = 2.dp,
                                                color = Color.White
                                            )
                                        } else {
                                            Icon(
                                                imageVector = if (isCurrentSurahPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                                contentDescription = if (isCurrentSurahPlaying) "Pause Surah" else "Play Surah",
                                                tint = if (isCurrentSurahPlaying) Color.White else Color(0xFFFDE68A),
                                                modifier = Modifier.size(22.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Bismillah Header (except Surah 9 At-Tawbah and Surah 1)
                    if (currentType == "surah" && currentNumber != 1 && currentNumber != 9) {
                        item {
                            BismillahBanner(isNightMode = isNightMode)
                        }
                    }

                    itemsIndexed(ayahs, key = { _, a -> a.globalNumber }) { idx, ayah ->
                        val isAyahPlayingPlayer = QuranAudioPlayer.currentPlayingGlobalNum == ayah.globalNumber && QuranAudioPlayer.isPlaying
                        val isAyahPlayingSurah = QuranSurahAudioEngine.currentGlobalAyahNum == ayah.globalNumber && QuranSurahAudioEngine.isPlaying
                        val isPlaying = isAyahPlayingPlayer || isAyahPlayingSurah

                        val isAyahLoadingPlayer = QuranAudioPlayer.currentPlayingGlobalNum == ayah.globalNumber && QuranAudioPlayer.isLoading
                        val isAyahLoadingSurah = QuranSurahAudioEngine.currentGlobalAyahNum == ayah.globalNumber && QuranSurahAudioEngine.isLoading
                        val isAudioLoading = isAyahLoadingPlayer || isAyahLoadingSurah

                        val audioProgress = when {
                            isAyahPlayingPlayer -> QuranAudioPlayer.currentAyahProgressFraction
                            isAyahPlayingSurah -> QuranSurahAudioEngine.currentAyahProgressFraction
                            else -> 0f
                        }

                        val isFav = QuranRepository.isAyahFavorite(ayah.globalNumber)
                        val isTafsirOpen = openTafsirs.contains(ayah.globalNumber)
                        val tafsirText = tafsirTexts[ayah.globalNumber]

                        val isCurrentAyahWord = (selectedWordInfo?.second == ayah.globalNumber)
                        val selectedWord = if (isCurrentAyahWord) selectedWordInfo?.first else null

                        AyahCardItem(
                            ayah = ayah,
                            fontSizeSp = settings.arabicFontSizeSp,
                            isNightMode = isNightMode,
                            showTransliteration = settings.showTransliteration,
                            showTranslation = settings.showTranslation,
                            isPlaying = isPlaying,
                            audioProgressFraction = audioProgress,
                            isAudioLoading = isAudioLoading,
                            isFavorite = isFav,
                            isTafsirOpen = isTafsirOpen,
                            tafsirText = tafsirText,
                            selectedWord = selectedWord,
                            onToggleFavorite = {
                                QuranRepository.toggleAyahFavorite(ayah.globalNumber)
                                ayahs = ayahs.toMutableList() // trigger recomposition
                            },
                            onCopy = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText(
                                    "Quran Ayah",
                                    "${ayah.arabicText}\n\n${ayah.transliteration}\n\n${ayah.translation}\n(Surah $currentNumber:${ayah.numberInSurah})"
                                )
                                clipboard.setPrimaryClip(clip)
                                val toastCopiedText = when (settings.translationLanguage) {
                                    "en" -> "Ayah copied! 📋"
                                    "ur" -> "آیت کاپی ہو گئی! 📋"
                                    "ar" -> "تم نسخ الآية! 📋"
                                    else -> "आयत कॉपी हो गई! 📋"
                                }
                                AppToast.show(toastCopiedText)
                            },
                            onPlayAudio = {
                                if (isAyahPlayingSurah) {
                                    QuranSurahAudioEngine.togglePlayPause()
                                } else {
                                    if (QuranSurahAudioEngine.isPlaying) {
                                        QuranSurahAudioEngine.stop()
                                    }
                                    QuranWordAudioPlayer.stop()
                                    selectedWordInfo = null
                                    QuranAudioPlayer.playAyah(
                                        globalNum = ayah.globalNumber,
                                        reciter = settings.reciterId,
                                        ayahList = ayahs,
                                        autoplayNext = settings.autoplayNext,
                                        onAdvanceNext = { nextGlobal ->
                                            if (settings.autoScroll) {
                                                val nextIndex = ayahs.indexOfFirst { it.globalNumber == nextGlobal }
                                                if (nextIndex != -1) {
                                                    coroutineScope.launch {
                                                        listState.animateScrollToItem(nextIndex)
                                                    }
                                                }
                                            }
                                        }
                                    )
                                }
                            },
                            onToggleTafsir = {
                                if (isTafsirOpen) {
                                    openTafsirs.remove(ayah.globalNumber)
                                } else {
                                    openTafsirs.add(ayah.globalNumber)
                                    if (tafsirText == null) {
                                        coroutineScope.launch {
                                            val fetched = QuranRepository.fetchTafsir(currentNumber, ayah.numberInSurah, settings.tafsirId)
                                            tafsirTexts[ayah.globalNumber] = fetched
                                        }
                                    }
                                }
                            },
                            onTranslationClick = {
                                if (QuranSurahAudioEngine.isPlaying) {
                                    QuranSurahAudioEngine.stop()
                                }
                                QuranWordAudioPlayer.stop()
                                selectedWordInfo = null
                                QuranAudioPlayer.playTranslationOnly(ayah, settings.translationLanguage)
                            },
                            onWordClick = { word ->
                                selectedWordInfo = word to ayah.globalNumber
                                QuranWordAudioPlayer.playWord(word.id, word.audioUrl)
                            },
                            onDismissWordInfo = {
                                selectedWordInfo = null
                                QuranWordAudioPlayer.stop()
                            },
                            onReplayWordAudio = { word ->
                                QuranWordAudioPlayer.playWord(word.id, word.audioUrl)
                            },
                            langCode = settings.translationLanguage
                        )
                    }

                    // Bottom Navigation Bar: [ ← Previous ] [ Next → ]
                    item {
                        BottomSurahNav(
                            currentNum = currentNumber,
                            maxNum = if (currentType == "surah") 114 else 30,
                            langCode = settings.translationLanguage,
                            isNightMode = isNightMode,
                            onPrev = {
                                if (currentNumber > 1) currentNumber--
                            },
                            onNext = {
                                val max = if (currentType == "surah") 114 else 30
                                if (currentNumber < max) currentNumber++
                            }
                        )
                    }
                }
            }
        }
    }

    // 4. FLOATING TRANSLATION AUDIO PLAYER BAR (Hovering smoothly at bottom)
    QuranTranslationFloatingBar(
        visible = QuranAudioPlayer.isTranslationBarVisible,
        surahNumber = QuranAudioPlayer.activeTranslationSurahNumber,
        ayahNumber = QuranAudioPlayer.activeTranslationAyahNumber,
        langCode = QuranAudioPlayer.activeTranslationLang,
        voiceName = QuranAudioPlayer.activeTranslationVoiceName,
        isPlaying = QuranAudioPlayer.isPlaying,
        isLoading = QuranAudioPlayer.isLoading,
        progressFraction = QuranAudioPlayer.currentAyahProgressFraction,
        isNightMode = isNightMode,
        onPlayPause = {
            if (QuranAudioPlayer.isPlaying) {
                QuranAudioPlayer.pause()
            } else {
                QuranAudioPlayer.resume()
            }
        },
        onStop = {
            QuranAudioPlayer.stop()
        },
        onDismiss = {
            QuranAudioPlayer.dismissTranslationBar()
        },
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .navigationBarsPadding()
    )
}
}

// ══════════════════════════════════════════════════════════════
// 1. READER TOP BAR
// ══════════════════════════════════════════════════════════════
@Composable
private fun ReaderTopBar(
    titleLocalized: String,
    titleArabic: String,
    langCode: String,
    isNightMode: Boolean = false,
    onBack: () -> Unit,
    onToggleNightMode: () -> Unit,
    onToggleZoom: () -> Unit,
    onOpenLangModal: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenTajweedGuide: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // [ ← Back ]
        val backLabel = when (langCode) {
            "en" -> "Back"
            "ur" -> "واپس"
            "ar" -> "رجوع"
            else -> "वापस"
        }
        val backBtnBg = if (isNightMode) Color(0xFF13221B) else Color(0xFFFFFDF8)
        val backBtnBorder = if (isNightMode) GoldAccent.copy(alpha = 0.4f) else GoldAccent.copy(alpha = 0.5f)
        val titleColor = if (isNightMode) Color(0xFFE8F5E9) else Color(0xFF0D4A3E)
        val titleSubColor = if (isNightMode) Color(0xFFD4AF37) else Color(0xFF0D4A3E)

        Row(
            modifier = Modifier
                .shadow(2.dp, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .background(backBtnBg)
                .border(1.2.dp, backBtnBorder, RoundedCornerShape(20.dp))
                .clickable(onClick = onBack)
                .padding(horizontal = 14.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = titleColor,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = backLabel,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor
            )
        }

        // Center Title: AL-FATIHAH • \n الفاتحة
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = titleLocalized,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                color = titleSubColor,
                fontFamily = FontFamily.Serif,
                letterSpacing = 0.5.sp
            )
            Text(
                text = titleArabic,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor,
                fontFamily = AmiriBoldFont
            )
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(2.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(GoldAccent)
            )
        }

        // Right Actions: ☀️ / 🌙, Language Flag, AA, ⚙️
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ActionCircleButton(
                text = if (isNightMode) "🌙" else "☀️",
                isNightMode = isNightMode,
                isActive = isNightMode,
                onClick = onToggleNightMode
            )
            ActionCircleButton(text = QuranRepository.getFlagEmoji(langCode), isNightMode = isNightMode, onClick = onOpenLangModal)
            ActionCircleButton(text = "🎨", isNightMode = isNightMode, onClick = onOpenTajweedGuide)
            ActionCircleButton(text = "AA", isNightMode = isNightMode, onClick = onToggleZoom)
            ActionCircleButton(text = "⚙️", isNightMode = isNightMode, onClick = onOpenSettings)
        }
    }
}

@Composable
private fun ActionCircleButton(
    text: String,
    isNightMode: Boolean = false,
    isActive: Boolean = false,
    onClick: () -> Unit
) {
    val bg = if (isNightMode) {
        if (isActive) Color(0xFF1B3B2B) else Color(0xFF13221B)
    } else {
        if (isActive) Color(0xFFFEF3C7) else Color(0xFFFFFDF8)
    }
    val borderCol = if (isActive) GoldAccent else (if (isNightMode) GoldAccent.copy(alpha = 0.4f) else GoldAccent.copy(alpha = 0.45f))
    val textCol = if (isNightMode) Color(0xFFE2E8F0) else Color(0xFF0D4A3E)

    Box(
        modifier = Modifier
            .size(34.dp)
            .shadow(1.dp, CircleShape)
            .clip(CircleShape)
            .background(bg)
            .border(1.dp, borderCol, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = if (text == "AA") 12.sp else 15.sp,
            fontWeight = FontWeight.Bold,
            color = textCol
        )
    }
}

// ══════════════════════════════════════════════════════════════
// 2. QUICK FONT ZOOM SLIDER BAR
// ══════════════════════════════════════════════════════════════
@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
private fun QuranZoomSliderBar(
    fontSizeSp: Float,
    isNightMode: Boolean = false,
    onFontSizeChange: (Float) -> Unit
) {
    val outerBg = if (isNightMode) Color(0xFF0A1410) else Color(0xFFF9F7F1)
    val barBg = if (isNightMode) Color(0xFF13221B) else Color.White
    val barBorder = if (isNightMode) GoldAccent.copy(alpha = 0.35f) else Color(0xFF0D4A3E).copy(alpha = 0.2f)
    val btnBg = if (isNightMode) Color(0xFF1B352B) else Color(0xFFF1F7F4)
    val btnText = if (isNightMode) Color(0xFFE2E8F0) else Color(0xFF0D4A3E)
    val badgeBg = if (isNightMode) Color(0xFF1B352B) else Color(0xFFEAF5EE)
    val badgeText = if (isNightMode) Color(0xFFFDE68A) else Color(0xFF0D4A3E)
    val activeTrack = if (isNightMode) GoldAccent else Color(0xFF0D4A3E)
    val inactiveTrack = if (isNightMode) Color(0xFF243B31) else Color(0xFFE2EDE7)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(outerBg)
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 1.dp, shape = RoundedCornerShape(50.dp), spotColor = Color(0x140D4A3E))
                .clip(RoundedCornerShape(50.dp))
                .background(barBg)
                .border(
                    width = 1.dp,
                    color = barBorder,
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
                        .background(btnBg)
                        .clickable {
                            onFontSizeChange((fontSizeSp - 2f).coerceAtLeast(16f))
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "A-",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = btnText
                    )
                }

                // Slim Custom Slider
                Slider(
                    value = fontSizeSp,
                    onValueChange = onFontSizeChange,
                    valueRange = 16f..46f,
                    modifier = Modifier
                        .weight(1f)
                        .height(28.dp),
                    thumb = {
                        Box(
                            modifier = Modifier
                                .size(15.dp)
                                .shadow(2.dp, CircleShape)
                                .background(activeTrack, CircleShape)
                                .border(1.5.dp, Color.White, CircleShape)
                        )
                    },
                    track = { sliderState ->
                        SliderDefaults.Track(
                            sliderState = sliderState,
                            modifier = Modifier.height(3.5.dp),
                            colors = SliderDefaults.colors(
                                activeTrackColor = activeTrack,
                                inactiveTrackColor = inactiveTrack
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
                        .background(btnBg)
                        .clickable {
                            onFontSizeChange((fontSizeSp + 2f).coerceAtMost(46f))
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "A+",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = btnText
                    )
                }

                // Size indicator badge - tap to reset to default 28sp
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(badgeBg)
                        .border(0.8.dp, GoldAccent.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                        .clickable { onFontSizeChange(28f) }
                        .padding(horizontal = 7.dp, vertical = 2.5.dp)
                ) {
                    Text(
                        text = "${fontSizeSp.toInt()}sp",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = badgeText
                    )
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
// 2. BISMILLAH BANNER
// ══════════════════════════════════════════════════════════════
@Composable
private fun BismillahBanner(isNightMode: Boolean = false) {
    val bannerBg = if (isNightMode) Color(0xFF122019) else Color(0xFFFFFDF8)
    val bannerBorder = if (isNightMode) GoldAccent.copy(alpha = 0.35f) else GoldAccent.copy(alpha = 0.5f)
    val textColor = if (isNightMode) Color(0xFFE2E8F0) else Color(0xFF0D4A3E)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
            .background(bannerBg)
            .border(1.2.dp, bannerBorder, RoundedCornerShape(14.dp))
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            fontFamily = AmiriQuranFont
        )
    }
}

// ══════════════════════════════════════════════════════════════
// 3. AYAH CARD ITEM (User's Custom Template with Ribbon & 4 Rosettes)
// ══════════════════════════════════════════════════════════════
@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
private fun AyahCardItem(
    ayah: AyahItem,
    fontSizeSp: Float,
    isNightMode: Boolean = false,
    showTransliteration: Boolean,
    showTranslation: Boolean,
    isPlaying: Boolean,
    audioProgressFraction: Float = 0f,
    isAudioLoading: Boolean = false,
    isFavorite: Boolean,
    isTafsirOpen: Boolean,
    tafsirText: String?,
    selectedWord: AyahWordItem? = null,
    onToggleFavorite: () -> Unit,
    onCopy: () -> Unit,
    onPlayAudio: () -> Unit,
    onToggleTafsir: () -> Unit,
    onTranslationClick: () -> Unit = {},
    onWordClick: (AyahWordItem) -> Unit = {},
    onDismissWordInfo: () -> Unit = {},
    onReplayWordAudio: (AyahWordItem) -> Unit = {},
    langCode: String = "hi"
) {
    // Pulse animation when audio is playing
    val infiniteTransition = rememberInfiniteTransition(label = "audioPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val cardBg = if (isNightMode) Color(0xFF122019) else Color(0xFFFFFDF8)
    val cardBorder = if (isPlaying) {
        if (isNightMode) Color(0xFF6EE7B7) else Color(0xFF0D4A3E)
    } else {
        if (isNightMode) GoldAccent.copy(alpha = 0.35f) else GoldAccent.copy(alpha = 0.45f)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .shadow(3.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(cardBg)
            .border(
                1.4.dp,
                cardBorder,
                RoundedCornerShape(18.dp)
            )
    ) {
        // --- 1. LEFT 3-SLICE ORNATE EMERALD RIBBON ---
        Box(
            modifier = Modifier
                .width(68.dp)
                .fillMaxHeight()
                .align(Alignment.CenterStart)
        ) {
            Column(modifier = Modifier.fillMaxHeight()) {
                // Top Slice: Top-Left Bracket + Gold Medallion
                val topPainter = rememberAssetPainter("media/a05c7273eb3837c0.webp")
                Image(
                    painter = topPainter,
                    contentDescription = null,
                    modifier = Modifier
                        .width(68.dp)
                        .height(80.dp),
                    contentScale = ContentScale.FillBounds
                )

                // Middle Slice: Vertical Damask Strip
                val midPainter = rememberAssetPainter("media/caf7509e6057cc99.webp")
                Image(
                    painter = midPainter,
                    contentDescription = null,
                    modifier = Modifier
                        .width(68.dp)
                        .weight(1f, fill = true),
                    contentScale = ContentScale.FillBounds
                )

                // Bottom Slice: Bottom Bracket
                val botPainter = rememberAssetPainter("media/cb299215ef09d6c1.webp")
                Image(
                    painter = botPainter,
                    contentDescription = null,
                    modifier = Modifier
                        .width(68.dp)
                        .height(28.dp),
                    contentScale = ContentScale.FillBounds
                )
            }

            // Circular Ayah Number Badge inside the Medallion (Dead-centered)
            Box(
                modifier = Modifier
                    .padding(start = 24.dp, top = 32.dp)
                    .size(36.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = ayah.numberInSurah.toString(),
                    fontSize = if (ayah.numberInSurah >= 100) 11.5.sp else 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    fontFamily = FontFamily.Serif,
                    textAlign = TextAlign.Center
                )
            }
        }

        // --- 2. AYAH CONTENT (Padded right of the ribbon) ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 84.dp, end = 16.dp, top = 14.dp, bottom = 14.dp)
        ) {
            // Top-Right 4 Action Circle Rosette Buttons (Crisp unclipped original coins)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    // 1. Star / Favorite
                    val starPainter = rememberAssetPainter("media/e4a23e11a60fe751.webp")
                    Image(
                        painter = starPainter,
                        contentDescription = "Favorite",
                        modifier = Modifier
                            .size(36.dp)
                            .clickable(onClick = onToggleFavorite)
                            .graphicsLayer {
                                if (isFavorite) {
                                    scaleX = 1.1f
                                    scaleY = 1.1f
                                }
                            }
                    )

                    // 2. Copy
                    val copyPainter = rememberAssetPainter("media/2ffde76d634199e9.webp")
                    Image(
                        painter = copyPainter,
                        contentDescription = "Copy",
                        modifier = Modifier
                            .size(36.dp)
                            .clickable(onClick = onCopy)
                    )

                    // 3. Play / Pause
                    val playPainter = rememberAssetPainter(
                        if (isPlaying) "media/f8d4547f63a228ba.webp" else "media/7d177db1ac11e898.webp"
                    )
                    Image(
                        painter = playPainter,
                        contentDescription = "Play/Pause",
                        modifier = Modifier
                            .size(36.dp)
                            .clickable(onClick = onPlayAudio)
                            .graphicsLayer {
                                if (isPlaying) {
                                    scaleX = pulseScale
                                    scaleY = pulseScale
                                }
                            }
                    )

                    // 4. Tafsir (Open Book)
                    val tafsirPainter = rememberAssetPainter("media/f13817383f167293.webp")
                    Image(
                        painter = tafsirPainter,
                        contentDescription = "Tafsir",
                        modifier = Modifier
                            .size(36.dp)
                            .clickable(onClick = onToggleTafsir)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Right-Aligned Arabic Text (Word-by-word interactive mode when words available)
            if (ayah.words.isNotEmpty()) {
                val cleanWords = remember(ayah.words) {
                    ayah.words.filter { it.charTypeName != "end" }
                }
                val weights = remember(cleanWords) {
                    cleanWords.map { it.text.length.coerceAtLeast(3) }
                }
                val totalWeight = remember(weights) {
                    weights.sum().toFloat().coerceAtLeast(1f)
                }

                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        ayah.words.forEach { word ->
                            if (word.charTypeName == "end") {
                                val isEndMedallionActive = isPlaying && (audioProgressFraction >= 0.90f)

                                Box(
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = " ۝${word.text} ",
                                        fontSize = (fontSizeSp * 0.85f).sp,
                                        fontWeight = if (isEndMedallionActive) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isEndMedallionActive) Color(0xFFCA9A2A) else (if (isNightMode) Color(0xFFD4AF37) else Color(0xFFB45309)),
                                        fontFamily = AmiriQuranFont,
                                        style = TextStyle(
                                            shadow = if (isEndMedallionActive) Shadow(
                                                color = Color(0xFFFFE082).copy(alpha = 0.95f),
                                                blurRadius = 16f,
                                                offset = Offset.Zero
                                            ) else null
                                        )
                                    )
                                }
                            } else {
                                val isWordActiveInVoice = if (isPlaying) {
                                    val wIdx = cleanWords.indexOfFirst { it.id == word.id }
                                    if (wIdx != -1 && cleanWords.isNotEmpty()) {
                                        val startFrac = weights.take(wIdx).sum() / totalWeight
                                        val endFrac = weights.take(wIdx + 1).sum() / totalWeight
                                        if (isAudioLoading) wIdx == 0 else (audioProgressFraction in startFrac..endFrac)
                                    } else false
                                } else false

                                val isWordSelected = (selectedWord?.id == word.id)
                                val isWordAudioPlayerPlaying = (QuranWordAudioPlayer.playingWordId == word.id && QuranWordAudioPlayer.isPlaying)
                                val isWordHighlighted = isWordActiveInVoice || isWordAudioPlayerPlaying

                                Box(
                                    modifier = Modifier
                                        .clickable {
                                            onWordClick(word)
                                        }
                                        .then(
                                            if (isWordSelected) {
                                                Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(if (isNightMode) Color(0xFF1E3A2F) else Color(0xFFE8F5E9))
                                                    .border(
                                                        1.2.dp,
                                                        if (isNightMode) Color(0xFF6EE7B7) else Color(0xFF146450),
                                                        RoundedCornerShape(6.dp)
                                                    )
                                            } else Modifier
                                        )
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = word.text,
                                        fontSize = fontSizeSp.sp,
                                        fontWeight = if (isWordHighlighted) FontWeight.Bold else FontWeight.Normal,
                                        color = when {
                                            isWordHighlighted -> Color(0xFFCA9A2A) // Shiny Light Golden (Zero orange!)
                                            isWordSelected -> if (isNightMode) Color(0xFFFDE68A) else Color(0xFF0D4A3E)
                                            else -> {
                                                val defaultCol = if (isNightMode) Color(0xFFF1F5F9) else Color(0xFF0C2B20)
                                                TajweedWaqfHelper.getTajweedOrWaqfColor(word.text, defaultCol)
                                            }
                                        },
                                        lineHeight = (fontSizeSp * 1.75f).sp,
                                        fontFamily = AmiriQuranFont,
                                        style = TextStyle(
                                            shadow = if (isWordHighlighted) Shadow(
                                                color = Color(0xFFFFE082).copy(alpha = 0.95f), // Radiant light golden halo glow!
                                                blurRadius = 16f,
                                                offset = Offset.Zero
                                            ) else null
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                // INLINE WORD DETAIL STRIP (Appears directly under the Arabic words on tap)
                AnimatedVisibility(
                    visible = selectedWord != null,
                    enter = expandVertically(animationSpec = tween(220)) + fadeIn(animationSpec = tween(220)),
                    exit = shrinkVertically(animationSpec = tween(180)) + fadeOut(animationSpec = tween(180))
                ) {
                    selectedWord?.let { activeWord ->
                        InlineWordDetailBanner(
                            word = activeWord,
                            isNightMode = isNightMode,
                            isPlaying = (QuranWordAudioPlayer.playingWordId == activeWord.id && QuranWordAudioPlayer.isPlaying),
                            isLoading = (QuranWordAudioPlayer.playingWordId == activeWord.id && QuranWordAudioPlayer.isLoading),
                            onReplay = { onReplayWordAudio(activeWord) },
                            onDismiss = onDismissWordInfo
                        )
                    }
                }
            } else {
                // Fallback: When ayah.words is empty, split arabicText into words for voice-synced light golden glow!
                val fallbackWords = remember(ayah.arabicText) {
                    ayah.arabicText.split("\\s+".toRegex()).filter { it.isNotBlank() }
                }
                if (fallbackWords.isNotEmpty()) {
                    val weights = remember(fallbackWords) {
                        fallbackWords.map { it.length.coerceAtLeast(3) }
                    }
                    val totalWeight = remember(weights) {
                        weights.sum().toFloat().coerceAtLeast(1f)
                    }

                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            fallbackWords.forEachIndexed { wIdx, wText ->
                                val isWordActiveInVoice = if (isPlaying) {
                                    val startFrac = weights.take(wIdx).sum() / totalWeight
                                    val endFrac = weights.take(wIdx + 1).sum() / totalWeight
                                    if (isAudioLoading) wIdx == 0 else (audioProgressFraction in startFrac..endFrac)
                                } else false

                                Box(
                                    modifier = Modifier.padding(horizontal = 3.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = wText,
                                        fontSize = fontSizeSp.sp,
                                        fontWeight = if (isWordActiveInVoice) FontWeight.Bold else FontWeight.Normal,
                                        color = when {
                                            isWordActiveInVoice -> Color(0xFFCA9A2A) // Shiny Light Golden
                                            else -> {
                                                val defaultCol = if (isNightMode) Color(0xFFF1F5F9) else Color(0xFF0C2B20)
                                                TajweedWaqfHelper.getTajweedOrWaqfColor(wText, defaultCol)
                                            }
                                        },
                                        lineHeight = (fontSizeSp * 1.75f).sp,
                                        fontFamily = AmiriQuranFont,
                                        style = TextStyle(
                                            shadow = if (isWordActiveInVoice) Shadow(
                                                color = Color(0xFFFFE082).copy(alpha = 0.95f),
                                                blurRadius = 16f,
                                                offset = Offset.Zero
                                            ) else null
                                        )
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Text(
                        text = ayah.arabicText,
                        fontSize = fontSizeSp.sp,
                        fontWeight = FontWeight.Normal,
                        color = if (isNightMode) Color(0xFFF1F5F9) else Color(0xFF0C2B20),
                        lineHeight = (fontSizeSp * 2.15f).sp,
                        textAlign = TextAlign.End,
                        fontFamily = AmiriQuranFont,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Horizontal Divider Seam
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = GoldAccent.copy(alpha = if (isNightMode) 0.2f else 0.25f), thickness = 1.dp)
            Spacer(modifier = Modifier.height(10.dp))

            // Transliteration
            if (showTransliteration && ayah.transliteration.isNotBlank()) {
                Text(
                    text = ayah.transliteration,
                    fontSize = 13.5.sp,
                    fontStyle = FontStyle.Italic,
                    color = if (isNightMode) Color(0xFF94A3B8) else Color(0xFF4B5563),
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            // Translation (Tap to play authentic translation audio!)
            if (showTranslation && ayah.translation.isNotBlank()) {
                val isThisTranslationPlaying = QuranAudioPlayer.isTranslationBarVisible &&
                    QuranAudioPlayer.activeTranslationAyahNumber == ayah.numberInSurah &&
                    QuranAudioPlayer.activeTranslationSurahNumber == ayah.surahNumber

                Surface(
                    onClick = onTranslationClick,
                    shape = RoundedCornerShape(10.dp),
                    color = if (isThisTranslationPlaying) {
                        if (isNightMode) Color(0xFF1E3A2F) else Color(0xFFE8F5E9)
                    } else Color.Transparent,
                    border = if (isThisTranslationPlaying) {
                        BorderStroke(1.2.dp, if (isNightMode) Color(0xFF6EE7B7) else Color(0xFF10B981))
                    } else null
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = if (isThisTranslationPlaying) 10.dp else 0.dp,
                                vertical = if (isThisTranslationPlaying) 6.dp else 2.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isThisTranslationPlaying) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Playing Translation",
                                tint = if (isNightMode) Color(0xFF6EE7B7) else Color(0xFF0D4A3E),
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(end = 6.dp)
                            )
                        }
                        Text(
                            text = ayah.translation,
                            fontSize = 14.5.sp,
                            color = if (isThisTranslationPlaying) {
                                if (isNightMode) Color(0xFF6EE7B7) else Color(0xFF0D4A3E)
                            } else {
                                if (isNightMode) Color(0xFFCBD5E1) else Color(0xFF1F2937)
                            },
                            lineHeight = 22.sp,
                            fontWeight = if (isThisTranslationPlaying) FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                }
            }

            // Expandable Tafsir Box
            AnimatedVisibility(visible = isTafsirOpen) {
                val tafsirBg = if (isNightMode) Color(0xFF0B1F17) else Color(0xFF0E5D47).copy(alpha = 0.05f)
                val tafsirBorder = if (isNightMode) Color(0xFF134233) else Color(0xFF0E5D47).copy(alpha = 0.2f)
                val tafsirTitle = when (langCode) {
                    "en" -> "TAFSIR"
                    "ur" -> "تفسیر"
                    "ar" -> "التفسير"
                    else -> "तफ़सीर (TAFSIR)"
                }
                val tafsirLoading = when (langCode) {
                    "en" -> "Loading Tafsir..."
                    "ur" -> "تفسیر لوڈ ہو رہی ہے..."
                    "ar" -> "جارٍ تحميل التفسير..."
                    else -> "तफ़सीर लोड हो रही है..."
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(tafsirBg)
                        .border(1.dp, tafsirBorder, RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isNightMode) Color(0xFF133629) else Color(0xFF0E5D47).copy(alpha = 0.12f))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = tafsirTitle,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isNightMode) Color(0xFF6EE7B7) else Color(0xFF0E5D47)
                                )
                            }
                            Text(
                                text = "✕",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isNightMode) Color(0xFF94A3B8) else Color.Gray,
                                modifier = Modifier.clickable(onClick = onToggleTafsir)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = tafsirText ?: tafsirLoading,
                            fontSize = 13.5.sp,
                            color = if (isNightMode) Color(0xFFCBD5E1) else Color(0xFF1F2937),
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }

        // --- 3. CORNER FLORAL WATERMARK ---
        val watermarkPainter = rememberAssetPainter("media/84e5172e17268b1f.webp")
        Image(
            painter = watermarkPainter,
            contentDescription = null,
            modifier = Modifier
                .size(54.dp)
                .align(Alignment.BottomEnd)
                .padding(2.dp),
            alpha = if (isNightMode) 0.18f else 0.35f
        )
    }
}

// ══════════════════════════════════════════════════════════════
// 3.1. INLINE WORD DETAIL BANNER (Fast, interactive, non-blocking)
// ══════════════════════════════════════════════════════════════
@Composable
private fun InlineWordDetailBanner(
    word: AyahWordItem,
    isNightMode: Boolean = false,
    isPlaying: Boolean,
    isLoading: Boolean,
    onReplay: () -> Unit,
    onDismiss: () -> Unit
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        val infiniteTransition = rememberInfiniteTransition(label = "pulse")
        val pulseScale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.15f,
            animationSpec = infiniteRepeatable(
                animation = tween(500, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulseScale"
        )

        val bannerBg = if (isNightMode) Color(0xFF162720) else Color(0xFFF9FCFA)
        val bannerBorder = if (isNightMode) Color(0xFF104434) else Color(0xFF146450).copy(alpha = 0.35f)
        val playBtnBg = if (isPlaying) Color(0xFF146450) else (if (isNightMode) Color(0xFF223C30) else Color(0xFFE8F5E9))
        val playBtnIconTint = if (isPlaying) Color.White else (if (isNightMode) Color(0xFF6EE7B7) else Color(0xFF146450))
        val translitColor = if (isNightMode) Color(0xFF6EE7B7) else Color(0xFF0D4A3E)
        val transColor = if (isNightMode) Color(0xFFCBD5E1) else Color(0xFF1E293B)
        val dismissBg = if (isNightMode) Color(0xFF223C30) else Color(0xFFF1F5F9)
        val dismissTint = if (isNightMode) Color(0xFF94A3B8) else Color(0xFF64748B)

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 4.dp),
            shape = RoundedCornerShape(12.dp),
            color = bannerBg,
            border = BorderStroke(1.2.dp, bannerBorder),
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Play / Replay Audio Button
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(playBtnBg)
                        .clickable(onClick = onReplay),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = Color(0xFF146450)
                        )
                    } else {
                        Icon(
                            imageVector = if (isPlaying) Icons.Filled.GraphicEq else Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Pronounce Word",
                            tint = playBtnIconTint,
                            modifier = Modifier
                                .size(18.dp)
                                .graphicsLayer {
                                    if (isPlaying) {
                                        scaleX = pulseScale
                                        scaleY = pulseScale
                                    }
                                }
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                // 2. Pronunciation & Lafzi Tarjuma (Word Meaning)
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (word.transliteration.isNotBlank()) {
                            Text(
                                text = word.transliteration,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = translitColor,
                                letterSpacing = 0.2.sp
                            )
                        }

                        Text(
                            text = "•",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF94A3B8)
                        )

                        Text(
                            text = word.text,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFDE68A),
                            fontFamily = AmiriQuranFont
                        )
                    }

                    if (word.translation.isNotBlank()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = word.translation,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = transColor,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                // 3. Dismiss (X) button
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(dismissBg)
                        .clickable(onClick = onDismiss),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = dismissTint,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
// 4. MUSHAF PAGE CONTENT (15-Line Ruled Real Mushaf)
// ══════════════════════════════════════════════════════════════
@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
private fun MushafPageContent(
    pageData: MushafPageData,
    pageNum: Int,
    settings: QuranSettingsState,
    onUpdateSettings: (QuranSettingsState) -> Unit,
    fontSizeSp: Float,
    isNightMode: Boolean = false,
    onOpenThemePicker: () -> Unit = {},
    onPrevPage: () -> Unit,
    onNextPage: () -> Unit
) {
    var selectedWord by remember(pageData.pageNumber) { mutableStateOf<MushafWordItem?>(null) }

    DisposableEffect(pageData.pageNumber) {
        onDispose {
            QuranWordAudioPlayer.stop()
        }
    }

    val activeMushafTheme = MushafTheme.getById(settings.mushafThemeId)
    val mushafCardBg = if (isNightMode) Color(0xFF111714) else activeMushafTheme.pageColor
    val mushafCardBorder = if (isNightMode) GoldAccent.copy(alpha = 0.45f) else activeMushafTheme.innerBorderColor

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            contentPadding = PaddingValues(
                top = 4.dp,
                bottom = if (selectedWord != null || QuranAudioPlayer.isTranslationBarVisible) 110.dp else 36.dp
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Ruled Mushaf Page Card with Integrated Gold Frame & Header
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(mushafCardBg)
                        .border(1.8.dp, mushafCardBorder, RoundedCornerShape(16.dp))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        // Left Decorative Margin Stripes (Indo-Pak / Rainbow Quran style)
                        if (activeMushafTheme.hasSideStripes && activeMushafTheme.marginStripeColors.isNotEmpty() && !isNightMode) {
                            Row(modifier = Modifier.padding(start = 3.dp, top = 6.dp, bottom = 6.dp)) {
                                activeMushafTheme.marginStripeColors.forEach { stripeColor ->
                                    Box(
                                        modifier = Modifier
                                            .width(3.dp)
                                            .height(580.dp)
                                            .background(stripeColor)
                                    )
                                }
                            }
                        }

                        // Center content column
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp, vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // 1. Integrated Authentic Mushaf Header Strip (Juz, Ruku Badge, Theme Selector, Surah Name)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Left: Juz info
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "الجزء ${toArabicDigits(pageData.juzNumber)}",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isNightMode) Color(0xFFE8F5E9) else Color(0xFF0D4A3E),
                                        fontFamily = AmiriQuranFont
                                    )
                                    Text(
                                        text = "(Juz ${pageData.juzNumber})",
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isNightMode) Color(0xFFD4AF37) else Color(0xFF8B6F20)
                                    )
                                }

                                // Center: Unified Ruku Badge + Theme Selector Pill
                                val rukuLabel = when (settings.translationLanguage) {
                                    "en" -> "RUKU ${pageData.rukuNumber}"
                                    "ur" -> "رکوع ${pageData.rukuNumber}"
                                    "ar" -> "ركوع ${pageData.rukuNumber}"
                                    else -> "रुकू ${pageData.rukuNumber}"
                                }
                                val themeBtnText = when (settings.translationLanguage) {
                                    "en" -> "Theme"
                                    "ur" -> "تھیم"
                                    "ar" -> "المظهر"
                                    else -> "थीम"
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .shadow(1.dp, RoundedCornerShape(12.dp))
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(if (isNightMode) Color(0xFF0B241C) else Color(0xFF0E4D3E))
                                            .border(1.dp, GoldAccent, RoundedCornerShape(12.dp))
                                            .padding(horizontal = 8.dp, vertical = 3.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = rukuLabel,
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color(0xFFFFE082),
                                            letterSpacing = 0.3.sp
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .shadow(1.dp, RoundedCornerShape(12.dp))
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(if (isNightMode) Color(0xFF143024) else Color(0xFF0E4D3E))
                                            .border(1.dp, GoldAccent, RoundedCornerShape(12.dp))
                                            .clickable { onOpenThemePicker() }
                                            .padding(horizontal = 7.dp, vertical = 3.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                                        ) {
                                            Text(text = "🎨", fontSize = 10.sp)
                                            Text(
                                                text = themeBtnText,
                                                fontSize = 10.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFFFE082)
                                            )
                                        }
                                    }
                                }

                            // Right: Surah Name & Header Play/Pause indicator
                            val isSurahAudioActive = (QuranSurahAudioEngine.currentSurahNum == pageData.surahNumber) &&
                                (QuranSurahAudioEngine.isPlaying || QuranSurahAudioEngine.isLoading)

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        if (QuranSurahAudioEngine.currentSurahNum == pageData.surahNumber && QuranSurahAudioEngine.isPlaying) {
                                            QuranSurahAudioEngine.togglePlayPause()
                                        } else if (QuranSurahAudioEngine.currentSurahNum == pageData.surahNumber && !QuranSurahAudioEngine.isPlaying && QuranSurahAudioEngine.currentGlobalAyahNum != null) {
                                            QuranSurahAudioEngine.resume()
                                        } else {
                                            QuranWordAudioPlayer.stop()
                                            val firstAyahOnPage = pageData.translations.firstOrNull()?.numberInSurah
                                                ?: pageData.lines.firstOrNull()?.words?.firstOrNull()?.verseNumber ?: 1
                                            QuranSurahAudioEngine.playSurah(pageData.surahNumber, startFromAyahIndexInSurah = firstAyahOnPage)
                                        }
                                    }
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                val headerAudioBg = if (isSurahAudioActive) Color(0xFF0E4D3E) else (if (isNightMode) Color(0xFF1B352B) else Color(0xFF0E4D3E).copy(alpha = 0.12f))
                                val headerAudioBorder = if (isSurahAudioActive) GoldAccent else (if (isNightMode) GoldAccent.copy(alpha = 0.35f) else Color(0xFF0E4D3E).copy(alpha = 0.35f))
                                val headerAudioIconTint = if (isSurahAudioActive) Color(0xFFFDE68A) else (if (isNightMode) Color(0xFF6EE7B7) else Color(0xFF0E4D3E))

                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(headerAudioBg)
                                        .border(1.dp, headerAudioBorder, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (QuranSurahAudioEngine.isLoading && isSurahAudioActive) {
                                        CircularProgressIndicator(modifier = Modifier.size(12.dp), strokeWidth = 1.5.dp, color = Color(0xFFD97706))
                                    } else {
                                        Icon(
                                            imageVector = if (QuranSurahAudioEngine.isPlaying && QuranSurahAudioEngine.currentSurahNum == pageData.surahNumber) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                            contentDescription = "Play/Pause Recitation",
                                            tint = headerAudioIconTint,
                                            modifier = Modifier.size(13.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = pageData.surahEnglishName,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isNightMode) Color(0xFFD4AF37) else Color(0xFF8B6F20)
                                )
                                Text(
                                    text = pageData.surahArabicName,
                                    fontSize = 14.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isNightMode) Color(0xFFE8F5E9) else Color(0xFF0D4A3E),
                                    fontFamily = AmiriBoldFont
                                )
                            }
                        }

                        // Ornate Divider beneath header
                        HorizontalDivider(
                            color = GoldAccent.copy(alpha = if (isNightMode) 0.35f else 0.45f),
                            thickness = 1.dp,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                        )

                        // Extract words and group by Surah in encounter order
                        val surahSections = remember(pageData) {
                            val sections = mutableListOf<Pair<Int, List<MushafWordItem>>>()
                            var currentS = -1
                            var currentList = mutableListOf<MushafWordItem>()
                            for (w in pageData.lines.flatMap { it.words }) {
                                if (w.surahNumber != currentS) {
                                    if (currentList.isNotEmpty()) {
                                        sections.add(Pair(currentS, currentList))
                                        currentList = mutableListOf()
                                    }
                                    currentS = w.surahNumber
                                }
                                currentList.add(w)
                            }
                            if (currentList.isNotEmpty()) {
                                sections.add(Pair(currentS, currentList))
                            }
                            sections
                        }

                        surahSections.forEach { (surahNum, wordsInSurah) ->
                            val sMeta = QuranData.surahs.getOrNull(surahNum - 1)
                            val surahArabic = sMeta?.arabicName ?: pageData.surahArabicName
                            val surahEnglish = sMeta?.englishName ?: pageData.surahEnglishName
                            val totalAyahs = sMeta?.numberOfAyahs ?: pageData.totalAyahs
                            val surahType = sMeta?.revelationType ?: pageData.surahType

                            val isSurahStarting = wordsInSurah.any { it.verseNumber == 1 && it.charTypeName != "end" } ||
                                pageNum == QuranData.getSurahStartPage(surahNum)

                            // 2. Surah Green Banner (ONLY when this Surah starts!)
                            if (isSurahStarting) {
                                val isCurrentSurahActive = (QuranSurahAudioEngine.currentSurahNum == surahNum)
                                val isCurrentSurahPlaying = isCurrentSurahActive && QuranSurahAudioEngine.isPlaying
                                val isCurrentSurahLoading = isCurrentSurahActive && QuranSurahAudioEngine.isLoading

                                val bannerBrush = if (isNightMode) {
                                    Brush.linearGradient(listOf(Color(0xFF041C13), Color(0xFF0A3323), Color(0xFF041C13)))
                                } else {
                                    activeMushafTheme.surahHeaderBrush
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 4.dp, vertical = 5.dp)
                                        .shadow(2.dp, RoundedCornerShape(10.dp))
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(bannerBrush)
                                        .border(1.4.dp, if (isNightMode) GoldAccent else activeMushafTheme.surahHeaderBorderColor, RoundedCornerShape(10.dp))
                                        .padding(vertical = 8.dp, horizontal = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        horizontalAlignment = Alignment.Start
                                    ) {
                                        Text(
                                            text = "سُورَةُ $surahArabic",
                                            fontSize = (fontSizeSp * 0.95f).coerceIn(18f, 26f).sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isNightMode) Color.White else activeMushafTheme.surahHeaderTextColor,
                                            fontFamily = AmiriBoldFont
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "$surahEnglish • $totalAyahs Ayahs • $surahType",
                                            fontSize = 11.sp,
                                            color = Color(0xFFD4AF37)
                                        )
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        QuranAudioModeButton(
                                            audioPlayMode = settings.audioPlayMode,
                                            translationVoiceLang = settings.translationVoiceLang,
                                            language = settings.translationLanguage,
                                            onModeChange = { newMode, newVoiceLang ->
                                                val updated = settings.copy(audioPlayMode = newMode, translationVoiceLang = newVoiceLang)
                                                onUpdateSettings(updated)
                                                QuranRepository.saveSettings(updated)
                                                QuranSurahAudioEngine.setPlayMode(newMode, newVoiceLang)
                                            },
                                            isNightMode = isNightMode
                                        )

                                        val playBtnBrush = if (isCurrentSurahPlaying) {
                                            Brush.linearGradient(listOf(Color(0xFFD4AF37), Color(0xFFB45309)))
                                        } else {
                                            if (isNightMode) {
                                                Brush.linearGradient(listOf(Color(0xFF1B4E3E), Color(0xFF0B2B20)))
                                            } else {
                                                Brush.linearGradient(listOf(Color(0xFF146450), Color(0xFF093B2E)))
                                            }
                                        }

                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .shadow(3.dp, CircleShape)
                                                .clip(CircleShape)
                                                .background(playBtnBrush)
                                                .border(1.4.dp, GoldAccent, CircleShape)
                                                .clickable {
                                                    if (isCurrentSurahActive) {
                                                        QuranSurahAudioEngine.togglePlayPause()
                                                    } else {
                                                        QuranWordAudioPlayer.stop()
                                                        selectedWord = null
                                                        QuranSurahAudioEngine.playSurah(surahNum)
                                                    }
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (isCurrentSurahLoading) {
                                                CircularProgressIndicator(
                                                    modifier = Modifier.size(16.dp),
                                                    strokeWidth = 2.dp,
                                                    color = Color.White
                                                )
                                            } else {
                                                Icon(
                                                    imageVector = if (isCurrentSurahPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                                    contentDescription = if (isCurrentSurahPlaying) "Pause Surah" else "Play Surah",
                                                    tint = if (isCurrentSurahPlaying) Color.White else Color(0xFFFDE68A),
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }
                                    }
                                }

                                // Header Bismillah (ONLY for new Surahs, excluding Surah 1 and Surah 9)
                                if (surahNum != 1 && surahNum != 9) {
                                    Text(
                                        text = "بِسْمِ ٱللَّهِ ٱلرَّحْمَـٰنِ ٱلرَّحِيمِ",
                                        fontSize = (fontSizeSp * 1.05f).sp,
                                        fontWeight = FontWeight.Normal,
                                        color = if (isNightMode) Color(0xFFE2E8F0) else activeMushafTheme.bismillahColor,
                                        fontFamily = AmiriQuranFont,
                                        modifier = Modifier.padding(vertical = 6.dp)
                                    )
                                }
                            }

                            // 3. Flowing, perfectly arranged Mushaf text in authentic Right-to-Left (RTL) order
                            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                                FlowRow(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 4.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalArrangement = Arrangement.spacedBy((fontSizeSp * 0.28f).dp.coerceIn(6.dp, 12.dp))
                                ) {
                                    wordsInSurah.forEach { word ->
                                        val isSurahAyahActive = (QuranSurahAudioEngine.isPlaying || QuranSurahAudioEngine.isLoading) &&
                                            QuranSurahAudioEngine.currentSurahNum == word.surahNumber &&
                                            QuranSurahAudioEngine.currentAyahIndexInSurah == word.verseNumber

                                        if (word.charTypeName == "end") {
                                            // Verse end medallion with Arabic numeral inside
                                            val isEndMedallionActive = isSurahAyahActive &&
                                                (QuranSurahAudioEngine.currentAyahProgressFraction >= 0.90f)

                                            Box(
                                                modifier = Modifier.padding(horizontal = 2.dp, vertical = 1.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = " ۝${toArabicDigits(word.verseNumber)} ",
                                                    fontSize = (fontSizeSp * 0.85f).sp,
                                                    color = if (isEndMedallionActive) Color(0xFFCA9A2A) else (if (isNightMode) Color(0xFFD4AF37) else Color(0xFF8B6F20)),
                                                    fontFamily = AmiriQuranFont,
                                                    fontWeight = if (isEndMedallionActive) FontWeight.Bold else FontWeight.Normal,
                                                    style = TextStyle(
                                                        shadow = if (isEndMedallionActive) Shadow(
                                                            color = Color(0xFFFFE082).copy(alpha = 0.95f),
                                                            blurRadius = 16f,
                                                            offset = Offset.Zero
                                                        ) else null
                                                    )
                                                )
                                            }
                                        } else {
                                            // Word-by-word active voice synchronization
                                            val isWordActiveInVoice = if (isSurahAyahActive) {
                                                val ayahWords = remember(wordsInSurah, word.surahNumber, word.verseNumber) {
                                                    wordsInSurah.filter { it.surahNumber == word.surahNumber && it.verseNumber == word.verseNumber && it.charTypeName != "end" }
                                                }
                                                val wIdx = ayahWords.indexOfFirst { it.id == word.id }
                                                if (wIdx != -1 && ayahWords.isNotEmpty()) {
                                                    val weights = ayahWords.map { it.text.length.coerceAtLeast(3) }
                                                    val totalWeight = weights.sum().toFloat().coerceAtLeast(1f)
                                                    val startFrac = weights.take(wIdx).sum() / totalWeight
                                                    val endFrac = weights.take(wIdx + 1).sum() / totalWeight
                                                    val progress = QuranSurahAudioEngine.currentAyahProgressFraction
                                                    if (QuranSurahAudioEngine.isLoading) wIdx == 0 else (progress in startFrac..endFrac)
                                                } else false
                                            } else false

                                            val isWordSelected = (selectedWord?.id == word.id)
                                            val isWordPlaying = (QuranWordAudioPlayer.playingWordId == word.id && QuranWordAudioPlayer.isPlaying)
                                            val isWordHighlighted = isWordActiveInVoice || isWordPlaying

                                            Box(
                                                modifier = Modifier
                                                    .clickable {
                                                        selectedWord = word
                                                        if (QuranSurahAudioEngine.isPlaying) {
                                                            QuranSurahAudioEngine.pause()
                                                        }
                                                        QuranWordAudioPlayer.playWord(word.id, word.audioUrl)
                                                    }
                                                    .then(
                                                        if (isWordSelected) {
                                                            Modifier
                                                                .clip(RoundedCornerShape(4.dp))
                                                                .background(if (isNightMode) Color(0xFF1E3A2F) else Color(0xFFE8F5E9).copy(alpha = 0.85f))
                                                        } else Modifier
                                                    )
                                                    .padding(horizontal = 2.5.dp, vertical = 1.dp)
                                            ) {
                                                Text(
                                                    text = word.text,
                                                    fontSize = fontSizeSp.sp,
                                                    lineHeight = (fontSizeSp * 1.6f).sp,
                                                    color = when {
                                                        isWordHighlighted -> Color(0xFFCA9A2A) // Shiny Light Golden (Zero orange!)
                                                        isWordSelected -> if (isNightMode) Color(0xFFFDE68A) else Color(0xFF0D4A3E)
                                                        else -> {
                                                            val defaultCol = if (isNightMode) Color(0xFFF1F5F9) else activeMushafTheme.arabicTextColor
                                                            TajweedWaqfHelper.getTajweedOrWaqfColor(word.text, defaultCol)
                                                        }
                                                    },
                                                    fontWeight = if (isWordHighlighted) FontWeight.Bold else FontWeight.Normal,
                                                    fontFamily = AmiriQuranFont,
                                                    style = TextStyle(
                                                        shadow = if (isWordHighlighted) Shadow(
                                                            color = Color(0xFFFFE082).copy(alpha = 0.95f), // Radiant shiny golden glow
                                                            blurRadius = 16f,
                                                            offset = Offset.Zero
                                                        ) else null
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Right Decorative Margin Stripes (Indo-Pak / Rainbow Tajweed style)
                    if (activeMushafTheme.hasSideStripes && activeMushafTheme.marginStripeColors.isNotEmpty() && !isNightMode) {
                        Row(modifier = Modifier.padding(end = 3.dp, top = 6.dp, bottom = 6.dp)) {
                            activeMushafTheme.marginStripeColors.reversed().forEach { stripeColor ->
                                Box(
                                    modifier = Modifier
                                        .width(3.dp)
                                        .height(580.dp)
                                        .background(stripeColor)
                                )
                            }
                        }
                    }
                }
            }
        }

            // Bottom Page Navigation Bar: [ ← Previous Page ] [ Page X / 604 ] [ Next Page → ]
            item {
                val prevPageLabel = when (settings.translationLanguage) {
                    "hi" -> "← पिछला पृष्ठ"
                    "ur" -> "← پچھلا صفحہ"
                    "ar" -> "← الصفحة السابقة"
                    else -> "← Previous Page"
                }
                val pageLabel = when (settings.translationLanguage) {
                    "hi" -> "पृष्ठ $pageNum / 604"
                    "ur" -> "صفحہ $pageNum / 604"
                    "ar" -> "الصفحة $pageNum / 604"
                    else -> "Page $pageNum / 604"
                }
                val nextPageLabel = when (settings.translationLanguage) {
                    "hi" -> "अगला पृष्ठ →"
                    "ur" -> "اگلا صفحہ →"
                    "ar" -> "الصفحة التالية →"
                    else -> "Next Page →"
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Prev
                    val prevBg = if (pageNum > 1) {
                        if (isNightMode) Color(0xFF16251E) else Color(0xFFFFFDF8)
                    } else {
                        if (isNightMode) Color(0xFF111A16) else Color(0xFFE5E7EB)
                    }
                    val prevText = if (pageNum > 1) {
                        if (isNightMode) Color(0xFFE2E8F0) else Color(0xFF0D4A3E)
                    } else Color.Gray

                    Box(
                        modifier = Modifier
                            .shadow(1.dp, RoundedCornerShape(20.dp))
                            .clip(RoundedCornerShape(20.dp))
                            .background(prevBg)
                            .border(1.dp, GoldAccent.copy(alpha = if (isNightMode) 0.35f else 0.4f), RoundedCornerShape(20.dp))
                            .clickable(enabled = pageNum > 1, onClick = onPrevPage)
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = prevPageLabel,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = prevText
                        )
                    }

                    // Page X / 604
                    val pageBadgeBg = if (isNightMode) Color(0xFF16251E) else GoldAccent.copy(alpha = 0.12f)
                    val pageBadgeText = if (isNightMode) Color(0xFFF3D48A) else Color(0xFF0D4A3E)

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(pageBadgeBg)
                            .border(1.dp, GoldAccent.copy(alpha = if (isNightMode) 0.35f else 0.4f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = pageLabel,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = pageBadgeText
                        )
                    }

                    // Next
                    val nextBg = if (pageNum < 604) {
                        if (isNightMode) Color(0xFF104434) else Color(0xFF0D4A3E)
                    } else {
                        if (isNightMode) Color(0xFF111A16) else Color(0xFFE5E7EB)
                    }
                    val nextText = if (pageNum < 604) Color.White else Color.Gray

                    Box(
                        modifier = Modifier
                            .shadow(2.dp, RoundedCornerShape(20.dp))
                            .clip(RoundedCornerShape(20.dp))
                            .background(nextBg)
                            .border(1.dp, GoldAccent, RoundedCornerShape(20.dp))
                            .clickable(enabled = pageNum < 604, onClick = onNextPage)
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = nextPageLabel,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = nextText
                        )
                    }
                }
            }

            // Flowing Translation below Mushaf page
            if (pageData.translations.isNotEmpty()) {
                item {
                    val transCardBg = if (isNightMode) Color(0xFF122019) else Color(0xFFFFFDF8)
                    val transCardBorder = if (isNightMode) GoldAccent.copy(alpha = 0.3f) else GoldAccent.copy(alpha = 0.35f)
                    val transTextColor = if (isNightMode) Color(0xFFE2E8F0) else Color(0xFF1F2937)
                    val transHeaderLabel = when (settings.translationLanguage) {
                        "en" -> "Translation"
                        "ur" -> "ترجمہ"
                        "ar" -> "الترجمة"
                        else -> "अनुवाद (Translation)"
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(2.dp, RoundedCornerShape(14.dp))
                            .clip(RoundedCornerShape(14.dp))
                            .background(transCardBg)
                            .border(1.dp, transCardBorder, RoundedCornerShape(14.dp))
                            .padding(14.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = transHeaderLabel,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFD4AF37)
                            )
                            pageData.translations.forEach { t ->
                                val isThisPlaying = QuranAudioPlayer.isTranslationBarVisible &&
                                    QuranAudioPlayer.activeTranslationAyahNumber == t.numberInSurah &&
                                    QuranAudioPlayer.activeTranslationSurahNumber == t.surahNumber

                                Surface(
                                    onClick = {
                                        if (QuranSurahAudioEngine.isPlaying) {
                                            QuranSurahAudioEngine.stop()
                                        }
                                        QuranWordAudioPlayer.stop()
                                        QuranAudioPlayer.playTranslationOnly(t, settings.translationLanguage)
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isThisPlaying) {
                                        if (isNightMode) Color(0xFF1E3A2F) else Color(0xFFE8F5E9)
                                    } else Color.Transparent,
                                    border = if (isThisPlaying) {
                                        BorderStroke(1.dp, if (isNightMode) Color(0xFF6EE7B7) else Color(0xFF10B981))
                                    } else null
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                horizontal = if (isThisPlaying) 8.dp else 0.dp,
                                                vertical = if (isThisPlaying) 4.dp else 2.dp
                                            ),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (isThisPlaying) {
                                            Icon(
                                                imageVector = Icons.Default.VolumeUp,
                                                contentDescription = null,
                                                tint = if (isNightMode) Color(0xFF6EE7B7) else Color(0xFF0D4A3E),
                                                modifier = Modifier.size(14.dp).padding(end = 6.dp)
                                            )
                                        }
                                        Text(
                                            text = "(${t.numberInSurah}) ${t.translation}",
                                            fontSize = 13.5.sp,
                                            color = if (isThisPlaying) {
                                                if (isNightMode) Color(0xFF6EE7B7) else Color(0xFF0D4A3E)
                                            } else transTextColor,
                                            lineHeight = 20.sp,
                                            fontWeight = if (isThisPlaying) FontWeight.SemiBold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Floating Word Detail Banner anchored at the bottom of the screen
        AnimatedVisibility(
            visible = selectedWord != null,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(animationSpec = tween(200)),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(animationSpec = tween(180))
        ) {
            selectedWord?.let { w ->
                InlineWordDetailBanner(
                    word = AyahWordItem(
                        id = w.id,
                        position = 0,
                        text = w.text,
                        audioUrl = w.audioUrl,
                        translation = w.translation,
                        transliteration = w.transliteration
                    ),
                    isNightMode = isNightMode,
                    isPlaying = (QuranWordAudioPlayer.playingWordId == w.id && QuranWordAudioPlayer.isPlaying),
                    isLoading = (QuranWordAudioPlayer.playingWordId == w.id && QuranWordAudioPlayer.isLoading),
                    onReplay = { QuranWordAudioPlayer.playWord(w.id, w.audioUrl) },
                    onDismiss = {
                        selectedWord = null
                        QuranWordAudioPlayer.stop()
                    }
                )
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
// 5. BOTTOM SURAH NAVIGATION BAR
// ══════════════════════════════════════════════════════════════
@Composable
private fun BottomSurahNav(
    currentNum: Int,
    maxNum: Int,
    langCode: String,
    isNightMode: Boolean = false,
    onPrev: () -> Unit,
    onNext: () -> Unit
) {
    val prevLabel = when (langCode) {
        "en" -> "← Previous"
        "ur" -> "← پچھلا"
        "ar" -> "← السابق"
        else -> "← पिछला"
    }
    val nextLabel = when (langCode) {
        "en" -> "Next →"
        "ur" -> "اگلا →"
        "ar" -> "التالي →"
        else -> "अगला →"
    }

    val btnBg = if (isNightMode) Color(0xFF13221B) else Color(0xFF093324)
    val disabledBg = if (isNightMode) Color(0xFF111A16) else Color(0xFFE5E7EB)
    val centerBg = if (isNightMode) Color(0xFF13221B) else GoldAccent.copy(alpha = 0.12f)
    val centerText = if (isNightMode) Color(0xFFF3D48A) else Color(0xFF0D4A3E)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Prev Surah
        Box(
            modifier = Modifier
                .shadow(2.dp, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .background(if (currentNum > 1) btnBg else disabledBg)
                .border(1.2.dp, GoldAccent, RoundedCornerShape(20.dp))
                .clickable(enabled = currentNum > 1, onClick = onPrev)
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Text(
                text = prevLabel,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (currentNum > 1) Color(0xFFF3D48A) else Color.Gray
            )
        }

        // Center Indicator
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(centerBg)
                .border(1.dp, GoldAccent.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 7.dp)
        ) {
            Text(
                text = "$currentNum / $maxNum",
                fontSize = 12.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = centerText
            )
        }

        // Next Surah
        Box(
            modifier = Modifier
                .shadow(2.dp, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .background(if (currentNum < maxNum) btnBg else disabledBg)
                .border(1.2.dp, GoldAccent, RoundedCornerShape(20.dp))
                .clickable(enabled = currentNum < maxNum, onClick = onNext)
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Text(
                text = nextLabel,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (currentNum < maxNum) Color(0xFFF3D48A) else Color.Gray
            )
        }
    }
}
