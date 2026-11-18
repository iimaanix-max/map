package com.tasbih.digitaltasbih.ui.screens.adhkar

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.tasbih.digitaltasbih.ui.components.AppToast
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.tasbih.digitaltasbih.R
import com.tasbih.digitaltasbih.data.*
import com.tasbih.digitaltasbih.ui.components.*
import com.tasbih.digitaltasbih.util.AdMobInterstitialManager
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun MorningAdhkarScreen(
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current

    // Active language and theme
    var selectedLang by remember { mutableStateOf(PrayerRepository.getLanguage()) }
    var isDarkTheme by remember { mutableStateOf(false) }
    var isMoreLangOpen by remember { mutableStateOf(false) }

    // Spotlight Tour state (auto opens on first launch)
    var isTourOpen by remember { mutableStateOf(!AdhkarRepository.isTourDone()) }
    var tourStep by remember { mutableStateOf(1) } // 1 or 2

    // Active reading item (null = list view, non-null = reading/counting view)
    var activeItem by remember { mutableStateOf<AdhkarItem?>(null) }

    val scrollState = rememberScrollState()
    var rootCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var firstCardCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var progressCardCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }

    val currentTargetRect: Rect? = remember(
        isTourOpen,
        tourStep,
        rootCoords,
        firstCardCoords,
        progressCardCoords,
        scrollState.value
    ) {
        val root = rootCoords
        if (root != null && root.isAttached) {
            val target = if (tourStep == 1) firstCardCoords else progressCardCoords
            if (target != null && target.isAttached) {
                try {
                    val box = root.localBoundingBoxOf(target, clipBounds = false)
                    val scrolledBox = box.translate(0f, -scrollState.value.toFloat())
                    if (scrolledBox.width > 0f && scrolledBox.height > 0f) scrolledBox else null
                } catch (_: Exception) {
                    null
                }
            } else null
        } else null
    }

    LaunchedEffect(isTourOpen, tourStep) {
        if (isTourOpen && activeItem == null) {
            if (tourStep == 1) {
                delay(180L)
                try {
                    scrollState.animateScrollTo(0)
                } catch (_: Exception) {}
            } else if (tourStep == 2) {
                delay(180L)
                try {
                    val root = rootCoords
                    val progress = progressCardCoords
                    if (root != null && root.isAttached && progress != null && progress.isAttached) {
                        val box = root.localBoundingBoxOf(progress, clipBounds = false)
                        val targetY = (box.top.toInt() - 250).coerceIn(0, scrollState.maxValue)
                        scrollState.animateScrollTo(targetY)
                    } else {
                        scrollState.animateScrollTo(scrollState.maxValue)
                    }
                } catch (_: Exception) {}
            }
        }
    }

    // Active Hadith reference modal item
    var activeHadithItem by remember { mutableStateOf<AdhkarItem?>(null) }

    // Protection Shield detail modal
    var isShieldModalOpen by remember { mutableStateOf(false) }

    // 100% Celebration Modal
    var isCelebrationOpen by remember { mutableStateOf(false) }
    var hasCelebratedThisSession by remember { mutableStateOf(false) }

    // Reset confirmation dialog
    var isResetConfirmOpen by remember { mutableStateOf(false) }

    // Reactive refresh trigger
    var refreshTrigger by remember { mutableStateOf(0) }

    // Weekly progress timeframe tab (week, month, year)
    var progressTab by remember { mutableStateOf("week") }

    // Font scale
    var fontScale by remember { mutableStateOf(AdhkarRepository.getFontScale()) }

    val allItems = remember(refreshTrigger) { AdhkarRepository.getMorningAdhkarList() }
    val completedCount = remember(refreshTrigger) { AdhkarRepository.getCompletedCount() }
    val streak = remember(refreshTrigger) { AdhkarRepository.getStreak() }
    val donePct = if (allItems.isNotEmpty()) ((completedCount * 100) / allItems.size) else 0

    // Localized UI strings for current active language (All 20+ languages supported)
    val strings = remember(selectedLang) { AdhkarLocalization.getStrings(selectedLang) }

    // Auto trigger 100% celebration when all 18 are done!
    LaunchedEffect(completedCount, allItems.size) {
        if (allItems.isNotEmpty() && completedCount == allItems.size && !hasCelebratedThisSession) {
            hasCelebratedThisSession = true
            isCelebrationOpen = true
        }
    }

    // Protection Shield stage (0 to 8)
    val shieldStage = remember(completedCount, allItems.size) {
        if (completedCount == 0) 0
        else if (completedCount >= allItems.size) 8
        else Math.min(7, Math.max(1, Math.round((completedCount.toFloat() / allItems.size.toFloat()) * 8f)))
    }

    val visibleLanguages = listOf(
        LanguageItem("en", "English"),
        LanguageItem("hi", "हिंदी"),
        LanguageItem("ur", "اردو"),
        LanguageItem("ar", "العربية"),
        LanguageItem("bn", "বাংলা")
    )

    // Pre-load Interstitial Ad for back navigation
    LaunchedEffect(Unit) {
        AdMobInterstitialManager.loadAd(context)
    }

    val activity = context as? Activity
    val handleBackWithAd = {
        if (activity != null) {
            AdMobInterstitialManager.showAd(activity, "adhkar_back") {
                onBack()
            }
        } else {
            onBack()
        }
    }

    // Vibrator for counting
    fun vibrateTap() {
        try {
            val v = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            if (v != null && v.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(35, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    v.vibrate(35)
                }
            }
        } catch (_: Exception) {}
    }

    // Back press handling
    BackHandler {
        if (isTourOpen) {
            AdhkarRepository.setTourDone(true)
            isTourOpen = false
        } else if (activeHadithItem != null) {
            activeHadithItem = null
        } else if (isShieldModalOpen) {
            isShieldModalOpen = false
        } else if (activeItem != null) {
            activeItem = null
        } else if (isMoreLangOpen) {
            isMoreLangOpen = false
        } else {
            handleBackWithAd()
        }
    }

    val bgMain = if (isDarkTheme) Color(0xFF0F1713) else Color(0xFFF7F5EE)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgMain)
            .onGloballyPositioned { rootCoords = it }
    ) {
        if (activeItem != null) {
            // ═══════════════════════════════════════════════════════════
            // INTERACTIVE RECITATION & COUNTER VIEW (Exact Screenshots 6 & 7)
            // ═══════════════════════════════════════════════════════════
            AdhkarReaderView(
                item = activeItem!!,
                totalCount = allItems.size,
                completedCount = completedCount,
                fontScale = fontScale,
                isDarkTheme = isDarkTheme,
                selectedLang = selectedLang,
                strings = strings,
                visibleLanguages = visibleLanguages,
                refreshTrigger = refreshTrigger,
                onBack = { activeItem = null },
                onSelectLanguage = { code ->
                    selectedLang = code
                    PrayerRepository.saveLanguage(code)
                },
                onOpenMoreLanguages = { isMoreLangOpen = true },
                onFontScaleChange = { newScale ->
                    fontScale = newScale
                    AdhkarRepository.setFontScale(newScale)
                },
                onViewHadith = { activeHadithItem = it },
                onToggleComplete = { itm ->
                    val isDone = AdhkarRepository.isItemDone(itm.id)
                    AdhkarRepository.setItemDone(itm.id, !isDone)
                    if (!isDone) {
                        AdhkarRepository.setItemCount(itm.id, itm.targetCount)
                    }
                    vibrateTap()
                    refreshTrigger++
                },
                onIncrementCount = { itm ->
                    val cur = AdhkarRepository.getItemCount(itm.id)
                    if (cur < itm.targetCount) {
                        val next = cur + 1
                        AdhkarRepository.setItemCount(itm.id, next)
                        if (next >= itm.targetCount) {
                            AdhkarRepository.setItemDone(itm.id, true)
                            AppToast.show("${strings.done} ✓")
                        }
                        vibrateTap()
                        refreshTrigger++
                    }
                },
                onUndoCount = { itm ->
                    val cur = AdhkarRepository.getItemCount(itm.id)
                    if (cur > 0) {
                        val next = cur - 1
                        AdhkarRepository.setItemCount(itm.id, next)
                        if (next < itm.targetCount) {
                            AdhkarRepository.setItemDone(itm.id, false)
                        }
                        vibrateTap()
                        refreshTrigger++
                    }
                },
                onResetCount = { itm ->
                    AdhkarRepository.setItemCount(itm.id, 0)
                    AdhkarRepository.setItemDone(itm.id, false)
                    vibrateTap()
                    refreshTrigger++
                    AppToast.show(strings.reset)
                },
                onNext = { itm ->
                    val nextIdx = (itm.order % allItems.size)
                    activeItem = allItems[nextIdx]
                }
            )
        } else {
            // ═══════════════════════════════════════════════════════════
            // MORNING ADHKAR MAIN LIST VIEW (Exact Screenshots 3, 4, 5)
            // ═══════════════════════════════════════════════════════════
            Scaffold(
                topBar = {
                    AdhkarHeaderTopBar(
                        completedCount = completedCount,
                        totalCount = allItems.size,
                        isDarkTheme = isDarkTheme,
                        title = strings.title,
                        showProgress = false,
                        onBack = { handleBackWithAd() },
                        onToggleTheme = {
                            isDarkTheme = !isDarkTheme
                            AppToast.show(if (isDarkTheme) "Night Theme 🌙" else "Day Theme ☀️")
                        }
                    )
                },
                containerColor = bgMain
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(scrollState)
                ) {
                    // 1. LANGUAGE BAR
                    LanguageBar(
                        languages = visibleLanguages,
                        selectedCode = selectedLang,
                        onSelectLanguage = { code ->
                            selectedLang = code
                            PrayerRepository.saveLanguage(code)
                        },
                        onOpenMoreLanguages = { isMoreLangOpen = true }
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // 2. TOP 3 METRIC CARDS (Completed, Protection Shield, Daily Streak)
                        AdhkarTopMetricsRow(
                            completedCount = completedCount,
                            totalCount = allItems.size,
                            donePct = donePct,
                            shieldStage = shieldStage,
                            streak = streak,
                            isDarkTheme = isDarkTheme,
                            strings = strings,
                            onShieldClick = { isShieldModalOpen = true }
                        )

                        // 3. 18 ADHKAR CARDS (01 to 18)
                        allItems.forEachIndexed { idx, item ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .then(if (idx == 0) Modifier.onGloballyPositioned { firstCardCoords = it } else Modifier)
                            ) {
                                AdhkarCardItem(
                                    item = item,
                                    isDone = AdhkarRepository.isItemDone(item.id),
                                    isDarkTheme = isDarkTheme,
                                    selectedLang = selectedLang,
                                    onClick = { activeItem = item },
                                    onViewHadith = { activeHadithItem = it },
                                    onToggleDone = {
                                        val isDone = AdhkarRepository.isItemDone(item.id)
                                        AdhkarRepository.setItemDone(item.id, !isDone)
                                        if (!isDone) {
                                            AdhkarRepository.setItemCount(item.id, item.targetCount)
                                        }
                                        vibrateTap()
                                        refreshTrigger++
                                    }
                                )
                            }
                        }

                        // 4. REAL ADMOB NATIVE AD SLOT (Zero-gap collapse)
                        if (!isTourOpen) {
                            AdMobNativeAdCard(modifier = Modifier.fillMaxWidth())
                        }

                        // 5. WEEKLY PROGRESS CARD (Screenshot 5)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { progressCardCoords = it }
                        ) {
                            AdhkarWeeklyProgressCard(
                                progressTab = progressTab,
                                isDarkTheme = isDarkTheme,
                                strings = strings,
                                onTabSelected = { progressTab = it }
                            )
                        }

                        // 6. RECENT DAYS HISTORY CARD (Screenshot 5)
                        AdhkarRecentHistoryCard(
                            streak = streak,
                            isDarkTheme = isDarkTheme,
                            strings = strings,
                            historyList = remember(refreshTrigger) { AdhkarRepository.getRecentDaysHistory() }
                        )

                        // 7. RESET ALL ADHKAR BUTTON (Screenshot 5)
                        Button(
                            onClick = { isResetConfirmOpen = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .shadow(4.dp, RoundedCornerShape(14.dp)),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0C4A3A)
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(text = "🔄", fontSize = 16.sp)
                                Text(
                                    text = strings.resetAll,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(28.dp))
                    }
                }
            }
        }

        // ═══════════════════════════════════════════════════════════
        // 2-STEP SPOTLIGHT TOUR OVERLAY
        // ═══════════════════════════════════════════════════════════
        if (isTourOpen && activeItem == null) {
            val tourTx = remember(selectedLang) { AdhkarTourLocalization.getMorning(selectedLang) }
            val stepLabel = tourTx.step.replace("{curr}", "$tourStep").replace("{total}", "2")
            GeneralSpotlightTourModal(
                step = tourStep,
                totalSteps = 2,
                stepLabel = stepLabel,
                title = if (tourStep == 1) tourTx.reciteTitle else tourTx.progressTitle,
                description = if (tourStep == 1) tourTx.reciteDesc else tourTx.progressDesc,
                nextBtnText = tourTx.next,
                doneBtnText = tourTx.done,
                skipBtnText = tourTx.skip,
                targetRect = currentTargetRect,
                cornerRadiusDp = if (tourStep == 1) 18f else 22f,
                padDp = 6f,
                onNext = {
                    if (tourStep == 1) {
                        tourStep = 2
                    } else {
                        AdhkarRepository.setTourDone(true)
                        isTourOpen = false
                    }
                },
                onDone = {
                    AdhkarRepository.setTourDone(true)
                    isTourOpen = false
                },
                onSkip = {
                    AdhkarRepository.setTourDone(true)
                    isTourOpen = false
                }
            )
        }

        // ═══════════════════════════════════════════════════════════
        // AUTHENTIC HADITH REFERENCE MODAL (Popup dialog)
        // ═══════════════════════════════════════════════════════════
        if (activeHadithItem != null) {
            AdhkarHadithModal(
                item = activeHadithItem!!,
                isDarkTheme = isDarkTheme,
                selectedLang = selectedLang,
                strings = strings,
                onClose = { activeHadithItem = null }
            )
        }

        // ═══════════════════════════════════════════════════════════
        // PROTECTION SHIELD DETAIL MODAL
        // ═══════════════════════════════════════════════════════════
        if (isShieldModalOpen) {
            ProtectionShieldModal(
                stage = shieldStage,
                completedCount = completedCount,
                totalCount = allItems.size,
                isDarkTheme = isDarkTheme,
                strings = strings,
                onClose = { isShieldModalOpen = false }
            )
        }

        // ═══════════════════════════════════════════════════════════
        // RESET ALL CONFIRMATION DIALOG
        // ═══════════════════════════════════════════════════════════
        if (isResetConfirmOpen) {
            AlertDialog(
                onDismissRequest = { isResetConfirmOpen = false },
                title = {
                    Text(
                        text = strings.resetConfirmTitle,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = Color(0xFF1F2937)
                    )
                },
                text = {
                    Text(
                        text = strings.resetConfirmDesc,
                        fontSize = 14.sp,
                        color = Color(0xFF4B5563)
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            AdhkarRepository.resetAll()
                            isResetConfirmOpen = false
                            refreshTrigger++
                            AppToast.show("${strings.resetAll} ✓")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                    ) {
                        Text(text = strings.reset, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { isResetConfirmOpen = false }) {
                        Text(text = strings.cancel, color = Color(0xFF4B5563))
                    }
                },
                shape = RoundedCornerShape(18.dp),
                containerColor = Color.White
            )
        }

        // 100% PROTECTION SHIELD CELEBRATION MODAL
        if (isCelebrationOpen) {
            AdhkarCelebrationModal(
                streak = streak,
                strings = strings,
                onClose = { isCelebrationOpen = false },
                onShare = {
                    val shareText = "🛡️ 100% Divine Protection Shield Attained!\n\nAlhamdulillah, I have completed all 18 Morning Adhkar for today on Digital Tasbih.\n\n🔥 Daily Streak: $streak Days\n\"Whoever recites them in the morning will be protected from all evil until evening.\"\n\nDownload Digital Tasbih App:"
                    val sendIntent = android.content.Intent().apply {
                        action = android.content.Intent.ACTION_SEND
                        putExtra(android.content.Intent.EXTRA_TEXT, shareText)
                        type = "text/plain"
                    }
                    context.startActivity(android.content.Intent.createChooser(sendIntent, "Share Morning Protection"))
                }
            )
        }

        // MORE LANGUAGES MODAL
        if (isMoreLangOpen) {
            MoreLanguagesModal(
                selectedCode = selectedLang,
                onSelectLanguage = { code ->
                    selectedLang = code
                    PrayerRepository.saveLanguage(code)
                    isMoreLangOpen = false
                },
                onDismiss = { isMoreLangOpen = false }
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════
// TOP HEADER TOP BAR (Exact Original Mosque Scenery Artwork)
// ═══════════════════════════════════════════════════════════
@Composable
fun AdhkarHeaderTopBar(
    completedCount: Int,
    totalCount: Int,
    isDarkTheme: Boolean,
    title: String = "MORNING ADHKAR",
    showProgress: Boolean = false,
    onBack: () -> Unit,
    onToggleTheme: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            .background(Color(0xFF0C4A3A))
            .border(
                width = 1.5.dp,
                brush = Brush.verticalGradient(
                    listOf(Color.Transparent, Color(0xFFF3D48A).copy(alpha = 0.65f))
                ),
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
    ) {
        // Exact Original Artwork: media/491b72628f03e198.webp (Sunrise Mosque & Desert Sky)
        val mosquePainter = rememberAssetPainter("media/491b72628f03e198.webp")
        Image(
            painter = mosquePainter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        // Subtle gradient overlay matching original app
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Black.copy(alpha = 0.02f),
                            Color.Black.copy(alpha = 0.32f)
                        )
                    )
                )
        )

        // Top bar controls & typography with status bar safe padding
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(start = 14.dp, end = 14.dp, top = 8.dp, bottom = if (showProgress) 10.dp else 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Button background & border depending on Day / Night mode
                val btnBg = if (isDarkTheme) Color(0xFF0E5D47).copy(alpha = 0.5f) else Color.White.copy(alpha = 0.92f)
                val btnBorder = if (isDarkTheme) Color(0xFFF3D48A).copy(alpha = 0.65f) else Color(0xFF0E5D47)
                val btnIconColor = if (isDarkTheme) Color(0xFFF3D48A) else Color(0xFF0E5D47)

                // Back Button (Exact original: 34dp white rounded square, 1.5px emerald border)
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .shadow(4.dp, RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp))
                        .background(btnBg)
                        .border(1.5.dp, btnBorder, RoundedCornerShape(10.dp))
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "←",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = btnIconColor
                    )
                }

                // Center Title: MORNING ADHKAR (Classical Serif font with text shadow)
                Text(
                    text = title,
                    fontSize = 16.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Serif,
                    letterSpacing = 0.8.sp,
                    color = if (isDarkTheme) Color(0xFFF3D48A) else Color(0xFF042E22),
                    textAlign = TextAlign.Center
                )

                // Right action buttons: Heart ❤️ & Sun ☀️ (34dp, 10dp rounded corners)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Support Us / Favorite button
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .shadow(4.dp, RoundedCornerShape(10.dp))
                            .clip(RoundedCornerShape(10.dp))
                            .background(btnBg)
                            .border(1.5.dp, btnBorder, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "❤️", fontSize = 15.sp)
                    }

                    // Theme toggle button
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .shadow(4.dp, RoundedCornerShape(10.dp))
                            .clip(RoundedCornerShape(10.dp))
                            .background(btnBg)
                            .border(1.5.dp, btnBorder, RoundedCornerShape(10.dp))
                            .clickable { onToggleTheme() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = if (isDarkTheme) "🌙" else "☀️", fontSize = 15.sp)
                    }
                }
            }

            // Bottom row: progress indicator "0 / 18" in champagne gold (#F3D48A) - Only in Counter/Reader view
            if (showProgress) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$completedCount / $totalCount",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF3D48A),
                    modifier = Modifier.padding(start = 2.dp)
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════
// TOP 3 METRICS ROW (Completed, Protection Shield, Daily Streak)
// ═══════════════════════════════════════════════════════════
@Composable
fun AdhkarTopMetricsRow(
    completedCount: Int,
    totalCount: Int,
    donePct: Int,
    shieldStage: Int,
    streak: Int,
    isDarkTheme: Boolean,
    strings: AdhkarStrings,
    onShieldClick: () -> Unit
) {
    val cardBg = if (isDarkTheme) Color(0xFF1A2621) else Color.White
    val cardBorder = if (isDarkTheme) Color(0xFF2E4037) else Color(0xFFE5DECC)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Card 1: Completed
        Card(
            modifier = Modifier
                .weight(1f)
                .height(116.dp)
                .shadow(2.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(cardBorder, cardBorder)))
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Ring with 0/18
                Box(modifier = Modifier.size(46.dp), contentAlignment = Alignment.Center) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val strokeW = 4.dp.toPx()
                        // Track
                        drawCircle(
                            color = Color(0xFFE5DECC),
                            style = Stroke(width = strokeW)
                        )
                        // Progress
                        val sweep = (donePct.toFloat() / 100f) * 360f
                        drawArc(
                            color = Color(0xFF0E5D47),
                            startAngle = -90f,
                            sweepAngle = sweep,
                            useCenter = false,
                            style = Stroke(width = strokeW, cap = StrokeCap.Round)
                        )
                    }
                    Text(
                        text = "$completedCount/$totalCount",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0E5D47)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = strings.completed,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "$donePct% ${strings.done}",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF0E5D47),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Card 2: Protection Shield (Working Stage 0 to 8)
        Card(
            modifier = Modifier
                .weight(1f)
                .height(116.dp)
                .shadow(2.dp, RoundedCornerShape(16.dp))
                .clickable { onShieldClick() },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(cardBorder, cardBorder)))
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Exact Shield Image from res/drawable/adk_shield_stage_X
                val shieldResId = when (shieldStage) {
                    0 -> R.drawable.adk_shield_stage_0
                    1 -> R.drawable.adk_shield_stage_1
                    2 -> R.drawable.adk_shield_stage_2
                    3 -> R.drawable.adk_shield_stage_3
                    4 -> R.drawable.adk_shield_stage_4
                    5 -> R.drawable.adk_shield_stage_5
                    6 -> R.drawable.adk_shield_stage_6
                    7 -> R.drawable.adk_shield_stage_7
                    else -> R.drawable.adk_shield_stage_8
                }

                Box(
                    modifier = Modifier.height(46.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = shieldResId),
                        contentDescription = "Protection Shield",
                        modifier = Modifier
                            .height(44.dp)
                            .width(54.dp),
                        contentScale = ContentScale.Fit
                    )

                    // Small badge lock or check
                    if (shieldStage == 0) {
                        Text(
                            text = "🔒",
                            fontSize = 10.sp,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .offset(x = 2.dp, y = 2.dp)
                        )
                    } else if (shieldStage == 8) {
                        Text(
                            text = "🛡️",
                            fontSize = 10.sp,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .offset(x = 2.dp, y = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = strings.protection,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = if (shieldStage == 8) strings.active else if (shieldStage > 0) strings.forging else strings.pending,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (shieldStage == 8) Color(0xFF0E5D47) else Color(0xFFD97706),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Card 3: Daily Streak
        Card(
            modifier = Modifier
                .weight(1f)
                .height(116.dp)
                .shadow(2.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(cardBorder, cardBorder)))
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "🔥", fontSize = 28.sp)

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "$streak ${strings.days}",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = strings.streak,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFB45309),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════
// ADHKAR CARD ITEM (Cards 01 to 18)
// ═══════════════════════════════════════════════════════════
@Composable
fun AdhkarCardItem(
    item: AdhkarItem,
    isDone: Boolean,
    isDarkTheme: Boolean,
    selectedLang: String,
    onClick: () -> Unit,
    onViewHadith: (AdhkarItem) -> Unit = {},
    onToggleDone: () -> Unit
) {
    val cardBg = if (isDarkTheme) Color(0xFF1A2621) else Color.White
    val cardBorder = if (isDarkTheme) Color(0xFF2E4037) else Color(0xFFEAE3D3)
    val title = item.getTitle(selectedLang)
    val curCount = AdhkarRepository.getItemCount(item.id)

    Card(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.5.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(cardBorder, cardBorder)))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Ornate Circular Medallion Rosette with Number
            AdhkarRosetteBadge(order = item.order)

            Spacer(modifier = Modifier.width(14.dp))

            // Center: Title & Reference
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFB45309), // Warm golden brown matching screenshot
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(3.dp))

                // Clickable Authentic Hadith Reference row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.clickable { onViewHadith(item) }
                ) {
                    Text(text = "📖", fontSize = 11.sp)
                    Text(
                        text = item.getReference(selectedLang),
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF0E5D47),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Right: Count Pill (Showing real-time progress e.g. 1/3 or 3x ✓)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (isDone) Color(0xFFE8F5EE)
                        else if (curCount > 0) Color(0xFFFEF3C7)
                        else Color(0xFFF3EFE6)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isDone) "${item.targetCount}x ✓" else if (curCount > 0) "$curCount/${item.targetCount}" else "${item.targetCount}x",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDone) Color(0xFF0E5D47) else if (curCount > 0) Color(0xFFB45309) else Color(0xFF4B5563)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Circular Checkmark Box
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(if (isDone) Color(0xFF0E5D47) else Color.Transparent)
                    .border(
                        width = 1.5.dp,
                        color = if (isDone) Color(0xFF0E5D47) else Color(0xFFD4AF37),
                        shape = CircleShape
                    )
                    .clickable { onToggleDone() },
                contentAlignment = Alignment.Center
            ) {
                if (isDone) {
                    Text(
                        text = "✓",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════
// ORNATE CIRCULAR MEDALLION ROSETTE BADGE
// ═══════════════════════════════════════════════════════════
@Composable
fun AdhkarRosetteBadge(order: Int) {
    Box(
        modifier = Modifier.size(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val r = size.minDimension / 2f
            val c = Offset(size.width / 2f, size.height / 2f)

            // Outer dashed gold rosette ring
            drawCircle(
                color = Color(0xFFC9A84C),
                radius = r - 1.5.dp.toPx(),
                center = c,
                style = Stroke(
                    width = 1.6.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 3f), 0f)
                )
            )

            // Inner dark emerald ring
            drawCircle(
                color = Color(0xFF0E5D47),
                radius = r - 4.dp.toPx(),
                center = c,
                style = Stroke(width = 1.8.dp.toPx())
            )
        }

        val numStr = String.format(Locale.US, "%02d", order)
        Text(
            text = numStr,
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF0E5D47),
            fontFamily = FontFamily.Serif
        )
    }
}

// ═══════════════════════════════════════════════════════════
// WEEKLY PROGRESS CARD (Screenshot 5)
// ═══════════════════════════════════════════════════════════
@Composable
fun AdhkarWeeklyProgressCard(
    progressTab: String,
    isDarkTheme: Boolean,
    strings: AdhkarStrings,
    onTabSelected: (String) -> Unit
) {
    val cardBg = if (isDarkTheme) Color(0xFF1A2621) else Color.White
    val cardBorder = if (isDarkTheme) Color(0xFF2E4037) else Color(0xFFEAE3D3)

    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val pcts = remember { AdhkarRepository.getWeeklyProgress() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(cardBorder, cardBorder)))
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            // Header with Period Tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = strings.weeklyProgress,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Tabs pill [ Week ] [ Month ] [ Year ]
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xFFF3EFE6))
                        .padding(3.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    listOf(strings.week to "week", strings.month to "month", strings.year to "year").forEach { (label, key) ->
                        val isSel = (progressTab == key)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(if (isSel) Color(0xFF0C4A3A) else Color.Transparent)
                                .clickable { onTabSelected(key) }
                                .padding(horizontal = 10.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontSize = 11.5.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSel) Color.White else Color(0xFF6B7280)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Chart area: Y-axis (100, 75, 50, 25, 0) + 7 vertical bars
            Row(
                modifier = Modifier.fillMaxWidth().height(140.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                // Y-axis labels
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.End
                ) {
                    listOf("100", "75", "50", "25", "0").forEach { yVal ->
                        Text(
                            text = yVal,
                            fontSize = 10.sp,
                            color = Color(0xFF9CA3AF)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                // 7 Days Bars
                Row(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.Bottom
                ) {
                    days.forEachIndexed { idx, day ->
                        val pct = pcts.getOrElse(idx) { 0f }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            // Track + Fill
                            Box(
                                modifier = Modifier
                                    .width(14.dp)
                                    .height(115.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFF3EFE6)),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                val fillHeight = (pct / 100f) * 115f
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(fillHeight.coerceAtLeast(3f).dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF0E5D47))
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = day,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF6B7280)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════
// RECENT DAYS HISTORY CARD (Screenshot 5)
// ═══════════════════════════════════════════════════════════
@Composable
fun AdhkarRecentHistoryCard(
    streak: Int,
    isDarkTheme: Boolean,
    strings: AdhkarStrings,
    historyList: List<AdhkarDayStats>
) {
    val cardBg = if (isDarkTheme) Color(0xFF1A2621) else Color.White
    val cardBorder = if (isDarkTheme) Color(0xFF2E4037) else Color(0xFFEAE3D3)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(cardBorder, cardBorder)))
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = "📋", fontSize = 14.sp)
                    Text(
                        text = strings.recentHistory,
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "🔥", fontSize = 12.sp)
                    Text(
                        text = "$streak ${strings.days} ${strings.streak}",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFB45309),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Date rows
            historyList.forEachIndexed { idx, stat ->
                val displayLabel = when (stat.label) {
                    "Today" -> strings.today
                    "Yesterday" -> strings.yesterday
                    else -> stat.label
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 7.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "📅", fontSize = 13.sp)
                        Text(
                            text = displayLabel,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF1F2937)
                        )
                    }

                    // Moon pill 0/18
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color(0xFFF3EFE6))
                            .padding(horizontal = 10.dp, vertical = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = "🌘", fontSize = 10.sp)
                            Text(
                                text = "${stat.doneCount}/${stat.totalCount}",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (stat.doneCount >= stat.totalCount) Color(0xFF0E5D47) else Color(0xFF6B7280)
                            )
                        }
                    }
                }

                if (idx < historyList.size - 1) {
                    Divider(color = Color(0xFFF3EFE6), thickness = 1.dp)
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════
// INTERACTIVE ADHKAR READER VIEW (Exact Screenshots 6 & 7)
// ═══════════════════════════════════════════════════════════
@Composable
fun AdhkarReaderView(
    item: AdhkarItem,
    totalCount: Int,
    completedCount: Int,
    fontScale: Float,
    isDarkTheme: Boolean,
    selectedLang: String,
    strings: AdhkarStrings,
    visibleLanguages: List<LanguageItem>,
    refreshTrigger: Int = 0,
    onBack: () -> Unit,
    onSelectLanguage: (String) -> Unit,
    onOpenMoreLanguages: () -> Unit,
    onFontScaleChange: (Float) -> Unit,
    onViewHadith: (AdhkarItem) -> Unit,
    onToggleComplete: (AdhkarItem) -> Unit,
    onIncrementCount: (AdhkarItem) -> Unit,
    onUndoCount: (AdhkarItem) -> Unit,
    onResetCount: (AdhkarItem) -> Unit,
    onNext: (AdhkarItem) -> Unit
) {
    val scrollState = rememberScrollState()
    val isDone = remember(refreshTrigger, item.id) { AdhkarRepository.isItemDone(item.id) }
    val curCount = remember(refreshTrigger, item.id) { AdhkarRepository.getItemCount(item.id) }
    val bgMain = if (isDarkTheme) Color(0xFF0F1713) else Color(0xFFF7F5EE)

    Scaffold(
        topBar = {
            AdhkarHeaderTopBar(
                completedCount = completedCount,
                totalCount = totalCount,
                isDarkTheme = isDarkTheme,
                title = strings.title,
                showProgress = true,
                onBack = onBack,
                onToggleTheme = {}
            )
        },
        containerColor = bgMain
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            // 1. Language Bar
            LanguageBar(
                languages = visibleLanguages,
                selectedCode = selectedLang,
                onSelectLanguage = onSelectLanguage,
                onOpenMoreLanguages = onOpenMoreLanguages
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Real AdMob Native Ad Card (Zero-gap collapse) matching adkCounterNativeAdWrap
                AdMobNativeAdCard(modifier = Modifier.fillMaxWidth())

                // 2. Sleek, Minimalist Font Zoom Control Pill
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 1.dp, shape = RoundedCornerShape(50.dp), spotColor = Color(0x140E5D47))
                        .clip(RoundedCornerShape(50.dp))
                        .background(if (isDarkTheme) Color(0xFF13271F) else Color.White)
                        .border(
                            width = 1.dp,
                            color = if (isDarkTheme) Color(0xFF1F4335) else Color(0xFF0E5D47).copy(alpha = 0.2f),
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
                                .background(if (isDarkTheme) Color(0xFF1C382C) else Color(0xFFF1F7F4))
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
                        @OptIn(ExperimentalMaterial3Api::class)
                        Slider(
                            value = fontScale,
                            onValueChange = onFontScaleChange,
                            valueRange = 0.8f..1.8f,
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
                                .background(if (isDarkTheme) Color(0xFF1C382C) else Color(0xFFF1F7F4))
                                .clickable {
                                    onFontScaleChange((fontScale + 0.1f).coerceAtMost(1.8f))
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

                        // Percentage indicator badge - tap to reset
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isDarkTheme) Color(0xFF1C382C) else Color(0xFFEAF5EE))
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

                // 3. Recitation Card (Arabic, Transliteration, Translation, Reference, Benefit)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(Color(0xFFEAE3D3), Color(0xFFEAE3D3))))
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Arabic Text
                        Text(
                            text = item.arabicText,
                            fontSize = (22f * fontScale).sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = (36f * fontScale).sp,
                            textAlign = TextAlign.Center,
                            color = Color(0xFF1F2937),
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Transliteration (Golden brown)
                        val trans = item.getTransliteration(selectedLang)
                        if (trans.isNotEmpty()) {
                            Text(
                                text = trans,
                                fontSize = (13.5f * fontScale).sp,
                                fontWeight = FontWeight.SemiBold,
                                lineHeight = (20f * fontScale).sp,
                                color = Color(0xFFB45309),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        // Translation
                        val meaning = item.getTranslation(selectedLang)
                        if (meaning.isNotEmpty()) {
                            Text(
                                text = meaning,
                                fontSize = (13.5f * fontScale).sp,
                                fontWeight = FontWeight.Normal,
                                lineHeight = (20f * fontScale).sp,
                                color = Color(0xFF374151),
                                textAlign = TextAlign.Justify,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        // Reference & View Hadith button
                        val refText = item.getReference(selectedLang)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color(0xFFF3EFE6))
                                .clickable { onViewHadith(item) }
                                .padding(horizontal = 12.dp, vertical = 7.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f, fill = false),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(text = "📖", fontSize = 12.sp)
                                Text(
                                    text = refText,
                                    fontSize = 11.5.sp,
                                    color = Color(0xFF1F2937),
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(Color(0xFFFEF3C7))
                                    .padding(horizontal = 9.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Text(text = "🔍", fontSize = 11.sp)
                                Text(
                                    text = strings.viewHadith,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF92400E)
                                )
                            }
                        }

                        // Yellow Benefit Callout Box
                        val benefitText = item.getBenefit(selectedLang)
                        if (benefitText.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFFEFCE8))
                                    .border(1.dp, Color(0xFFFEF08A), RoundedCornerShape(12.dp))
                                    .padding(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text(text = "💡", fontSize = 14.sp)
                                    Text(
                                        text = benefitText,
                                        fontSize = 12.5.sp,
                                        color = Color(0xFF713F12),
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }
                }

                val undoText = remember(selectedLang) { TasbihLocalization.get(selectedLang).undo }

                // 4. Interactive Master Counter Section (100% Tasbih Parity: Dial/Beads, Press Physics, Ripples, Gold Pulse, Chime & Controls)
                AdhkarCounterSection(
                    curCount = curCount,
                    targetCount = item.targetCount,
                    isDone = isDone,
                    isNightMode = isDarkTheme,
                    tapToCountLabel = strings.tapToCount,
                    undoLabel = undoText,
                    resetLabel = strings.reset,
                    markCompleteLabel = strings.markComplete,
                    completedLabel = strings.completed,
                    nextLabel = strings.nextAdhkar,
                    onIncrement = { onIncrementCount(item) },
                    onUndo = { onUndoCount(item) },
                    onReset = { onResetCount(item) },
                    onToggleComplete = { onToggleComplete(item) },
                    onNext = { onNext(item) }
                )

                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════
// 2-STEP SPOTLIGHT TOUR OVERLAY (Exact Screenshots 1 & 2)
// ═══════════════════════════════════════════════════════════
@Composable
fun AdhkarSpotlightTourOverlay(
    step: Int,
    strings: AdhkarStrings,
    onNext: () -> Unit,
    onSkip: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.72f))
            .clickable { /* Block clicks outside */ }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .align(Alignment.Center)
        ) {
            // Cutout/Highlighted mockup card representation
            if (step == 1) {
                // Highlighted Card 01 (Ayat al-Kursi) with gold glow
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(12.dp, RoundedCornerShape(18.dp))
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color.White)
                        .border(3.dp, Color(0xFFE5B25D), RoundedCornerShape(18.dp))
                        .padding(horizontal = 14.dp, vertical = 14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AdhkarRosetteBadge(order = 1)
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Ayat al-Kursi",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB45309)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "📖", fontSize = 11.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Qur'an 2:255 (Al-Hakim 1/562)",
                                    fontSize = 11.5.sp,
                                    color = Color(0xFF6B7280)
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFF3EFE6))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(text = "1x", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4B5563))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .border(1.5.dp, Color(0xFFD4AF37), CircleShape)
                        )
                    }
                }

                // Arrow pointing UP
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "▲", fontSize = 20.sp, color = Color(0xFFE5B25D))
                }
            }

            // Dark Emerald Spotlight Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(16.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF072C22)),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(Color(0xFF0F5A46), Color(0xFF0F5A46))))
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(18.dp)) {
                    // Header: Step Badge & Skip
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color(0xFF0F4E3C))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "✨ ${strings.tourStep} $step ${strings.ofWord} 2",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE5B25D)
                            )
                        }

                        Text(
                            text = strings.skip,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE5B25D),
                            modifier = Modifier.clickable { onSkip() }
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Title
                    Text(
                        text = if (step == 1) strings.tour1Title else strings.tour2Title,
                        fontSize = 16.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Description
                    Text(
                        text = if (step == 1) strings.tour1Desc else strings.tour2Desc,
                        fontSize = 13.5.sp,
                        color = Color(0xFFD1FAE5),
                        lineHeight = 19.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Bottom Row: Pagination dots & Next/Got it button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Pagination
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(if (step == 1) 22.dp else 8.dp)
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (step == 1) Color(0xFFE5B25D) else Color(0xFF0F5A46))
                            )
                            Box(
                                modifier = Modifier
                                    .width(if (step == 2) 22.dp else 8.dp)
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (step == 2) Color(0xFFE5B25D) else Color(0xFF0F5A46))
                            )
                        }

                        // Button (Next ➔ or Got it! ✓)
                        Button(
                            onClick = { onNext() },
                            shape = RoundedCornerShape(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE5B25D))
                        ) {
                            Text(
                                text = if (step == 1) strings.next else strings.doneBtn,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF072C22)
                            )
                        }
                    }
                }
            }

            if (step == 2) {
                // Arrow pointing DOWN
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "▼", fontSize = 20.sp, color = Color(0xFFE5B25D))
                }

                // Highlighted Mockup Weekly Progress Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(12.dp, RoundedCornerShape(18.dp))
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color.White)
                        .border(3.dp, Color(0xFFE5B25D), RoundedCornerShape(18.dp))
                        .padding(14.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Weekly Progress", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(Color(0xFF0C4A3A))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(text = "Week", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth().height(40.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF0E5D47)))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = it, fontSize = 9.sp, color = Color(0xFF6B7280))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════
// AUTHENTIC HADITH REFERENCE MODAL
// ═══════════════════════════════════════════════════════════
@Composable
fun AdhkarHadithModal(
    item: AdhkarItem,
    isDarkTheme: Boolean,
    selectedLang: String,
    strings: AdhkarStrings,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val hadithBook = item.hadithBook.ifEmpty { item.reference }
    val hadithGrade = item.hadithGrade.ifEmpty { "Sahih / Hasan" }
    val hadithNarrator = item.hadithNarrator
    val hadithArabic = item.hadithArabic
    val hadithTrans = item.getHadithTranslation(selectedLang).ifEmpty { item.hadithTranslationEn }
    val hadithUrl = item.hadithUrl.ifEmpty { "https://sunnah.com" }

    val transLabel = strings.transLabel
    val viewSourceText = strings.viewSource

    Dialog(onDismissRequest = onClose) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
                .shadow(20.dp, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(2.dp, Color(0xFF0E5D47))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // 1. Royal Dark Emerald Header with Golden Seam (Matching Screenshot)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF0E5D47), Color(0xFF063B2C))
                            )
                        )
                        .border(
                            width = 1.5.dp,
                            color = Color(0xFFF6D365),
                            shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp)
                        )
                        .padding(horizontal = 18.dp, vertical = 14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f, fill = false),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "📜", fontSize = 20.sp)
                            Text(
                                text = strings.hadithModalTitle,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Serif,
                                letterSpacing = 0.5.sp,
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        // Circular Close Button
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.18f))
                                .clickable { onClose() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "✕",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                // 2. Modal Body Content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Metadata Pill (Book & Grade)
                    if (hadithBook.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF4FAF7))
                                .border(1.dp, Color(0xFFD1E7DD), RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 9.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f, fill = false),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(text = "🧾", fontSize = 13.sp)
                                Text(
                                    text = hadithBook,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0E5D47),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            if (hadithGrade.isNotEmpty()) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFFDCFCE7))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = "✓ $hadithGrade",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFF059669)
                                    )
                                }
                            }
                        }
                    }

                    // Narrated by line
                    if (hadithNarrator.isNotEmpty()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = strings.narratedBy,
                                fontSize = 12.sp,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                color = Color(0xFF6B7280)
                            )
                            Text(
                                text = hadithNarrator,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF374151)
                            )
                        }
                    }

                    // Arabic Matn Box
                    if (hadithArabic.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFFAF6EE))
                                .border(1.2.dp, Color(0xFFE5D5B5), RoundedCornerShape(14.dp))
                                .padding(14.dp)
                        ) {
                            Text(
                                text = hadithArabic,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 32.sp,
                                textAlign = TextAlign.End,
                                color = Color(0xFF1B382B),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    // Translation Box with 4px Emerald Accent Border
                    if (hadithTrans.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF9FAFB))
                                .drawBehind {
                                    drawRect(
                                        color = Color(0xFF0E5D47),
                                        size = androidx.compose.ui.geometry.Size(4.dp.toPx(), size.height)
                                    )
                                }
                                .padding(start = 14.dp, end = 12.dp, top = 10.dp, bottom = 12.dp)
                        ) {
                            Text(
                                text = transLabel,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0E5D47),
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = hadithTrans,
                                fontSize = 13.sp,
                                lineHeight = 19.sp,
                                color = Color(0xFF1F2937)
                            )
                        }
                    }

                    // View on Sunnah.com / Official Source Button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(3.dp, RoundedCornerShape(12.dp))
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF0E5D47), Color(0xFF10B981))
                                )
                            )
                            .clickable {
                                try {
                                    val intent = android.content.Intent(
                                        android.content.Intent.ACTION_VIEW,
                                        android.net.Uri.parse(hadithUrl)
                                    )
                                    context.startActivity(intent)
                                } catch (_: Exception) {}
                            }
                            .padding(vertical = 12.dp, horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(text = "🌐", fontSize = 14.sp)
                            Text(
                                text = viewSourceText,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(text = "↗", fontSize = 14.sp, color = Color.White)
                        }
                    }

                    // AdMob Native Ad Card (Zero-gap collapse)
                    AdMobNativeAdCard(modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════
// PROTECTION SHIELD DETAIL MODAL
// ═══════════════════════════════════════════════════════════
@Composable
fun ProtectionShieldModal(
    stage: Int,
    completedCount: Int,
    totalCount: Int,
    isDarkTheme: Boolean,
    strings: AdhkarStrings,
    onClose: () -> Unit
) {
    val shieldResId = when (stage) {
        0 -> R.drawable.adk_shield_stage_0
        1 -> R.drawable.adk_shield_stage_1
        2 -> R.drawable.adk_shield_stage_2
        3 -> R.drawable.adk_shield_stage_3
        4 -> R.drawable.adk_shield_stage_4
        5 -> R.drawable.adk_shield_stage_5
        6 -> R.drawable.adk_shield_stage_6
        7 -> R.drawable.adk_shield_stage_7
        else -> R.drawable.adk_shield_stage_8
    }

    Dialog(onDismissRequest = onClose) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(16.dp, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Large Shield Image
                Image(
                    painter = painterResource(id = shieldResId),
                    contentDescription = "Shield Stage $stage",
                    modifier = Modifier
                        .height(110.dp)
                        .width(130.dp),
                    contentScale = ContentScale.Fit
                )

                Text(
                    text = if (stage == 8) "100% Divine Protection Shield Active! 🛡️" else "Protection Shield: Stage $stage / 8",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (stage == 8) Color(0xFF0C4A3A) else Color(0xFFB45309),
                    textAlign = TextAlign.Center
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFEFCE8))
                        .border(1.dp, Color(0xFFFEF08A), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = if (stage == 8)
                            "Alhamdulillah! You have completed all 18 Morning Adhkar. By Allah's permission, this divine fortress protects you from jinn, evil eye, misfortune, and anxiety until evening."
                        else
                            "You have recited $completedCount of $totalCount morning adhkar. Complete all 18 to forge your full spiritual shield of divine protection!",
                        fontSize = 13.sp,
                        color = Color(0xFF713F12),
                        lineHeight = 19.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Button(
                    onClick = onClose,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0C4A3A))
                ) {
                    Text(text = strings.backToAdhkar, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════
// 100% PROTECTION SHIELD CELEBRATION MODAL
// ═══════════════════════════════════════════════════════════
@Composable
fun AdhkarCelebrationModal(
    streak: Int,
    strings: AdhkarStrings,
    onClose: () -> Unit,
    onShare: () -> Unit
) {
    Dialog(onDismissRequest = onClose) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(20.dp, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.linearGradient(listOf(Color(0xFFE5B25D), Color(0xFFC9A84C)))
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Top close
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    IconButton(onClick = onClose, modifier = Modifier.size(32.dp)) {
                        Text(text = "✕", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B7280))
                    }
                }

                // 100% Protection Shield with radiant gold halo
                Box(contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .size(130.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE5B25D).copy(alpha = 0.18f))
                    )
                    Image(
                        painter = painterResource(id = R.drawable.adk_shield_stage_8),
                        contentDescription = "100% Protection Shield",
                        modifier = Modifier.size(110.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                // Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xFF0C4A3A))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = strings.celebrationBadge,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFE5B25D)
                    )
                }

                Text(
                    text = strings.celebrationTitle,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )

                Text(
                    text = strings.celebrationDesc,
                    fontSize = 13.5.sp,
                    color = Color(0xFF4B5563),
                    textAlign = TextAlign.Center,
                    lineHeight = 19.sp
                )

                // Hadith quote card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFF7F5EE))
                        .border(1.dp, Color(0xFFEAE3D3), RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "❝", fontSize = 24.sp, color = Color(0xFFC9A84C))
                        Text(
                            text = strings.celebrationQuote,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF0C4A3A),
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = strings.celebrationQuoteRef,
                            fontSize = 11.sp,
                            color = Color(0xFFB45309),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Action Buttons
                Button(
                    onClick = onShare,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE5B25D))
                ) {
                    Text(
                        text = "📤 ${strings.shareCard}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.5.sp,
                        color = Color(0xFF072C22)
                    )
                }

                OutlinedButton(
                    onClick = onClose,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = Brush.linearGradient(listOf(Color(0xFF0C4A3A), Color(0xFF0C4A3A)))
                    )
                ) {
                    Text(text = "✨ ${strings.doneBtn}", fontWeight = FontWeight.Bold, color = Color(0xFF0C4A3A))
                }
            }
        }
    }
}
