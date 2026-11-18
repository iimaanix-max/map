package com.tasbih.digitaltasbih.ui.screens.salah

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import com.tasbih.digitaltasbih.util.AdMobInterstitialManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.data.*
import com.tasbih.digitaltasbih.ui.components.*
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun SalahTrackerScreen(
    onBack: () -> Unit = {},
    onOpenPrayerTimes: () -> Unit = {}
) {
    val context = LocalContext.current

    // Active language and theme
    var selectedLang by remember { mutableStateOf(PrayerRepository.getLanguage()) }
    var isDarkTheme by remember { mutableStateOf(false) }
    var isMoreLangOpen by remember { mutableStateOf(false) }

    // Spotlight Tour state (auto opens on first launch)
    var isTourOpen by remember { mutableStateOf(!PrayerRepository.isSalahTourDone()) }
    var tourStep by remember { mutableStateOf(1) }

    // Jamat Times 30-Day Review Prompt Modal (jamatReviewPromptModal)
    var isJamatReviewOpen by remember {
        mutableStateOf(PrayerRepository.shouldShowJamat30DayReview())
    }

    val scrollState = rememberScrollState()
    var rootCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var prayerRowCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var monthlyChartCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }

    val currentTargetRect: Rect? = remember(
        isTourOpen,
        tourStep,
        rootCoords,
        prayerRowCoords,
        monthlyChartCoords,
        scrollState.value
    ) {
        val root = rootCoords
        if (root != null && root.isAttached) {
            val target = if (tourStep == 1) prayerRowCoords else monthlyChartCoords
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

    LaunchedEffect(isTourOpen, tourStep, prayerRowCoords, monthlyChartCoords) {
        if (isTourOpen) {
            delay(150L) // Wait for layout to measure
            try {
                val root = rootCoords ?: return@LaunchedEffect
                if (tourStep == 1) {
                    val prayerRow = prayerRowCoords ?: return@LaunchedEffect
                    if (root.isAttached && prayerRow.isAttached) {
                        val box = root.localBoundingBoxOf(prayerRow, clipBounds = false)
                        val absoluteY = scrollState.value + box.top
                        // Center 5 Prayer Cards with space above for the guide tooltip card
                        val targetY = (absoluteY.toInt() - 320).coerceIn(0, scrollState.maxValue)
                        scrollState.animateScrollTo(
                            value = targetY,
                            animationSpec = tween(durationMillis = 750, easing = FastOutSlowInEasing)
                        )
                    }
                } else if (tourStep == 2) {
                    val chart = monthlyChartCoords ?: return@LaunchedEffect
                    if (root.isAttached && chart.isAttached) {
                        val box = root.localBoundingBoxOf(chart, clipBounds = false)
                        val absoluteY = scrollState.value + box.top
                        val targetY = (absoluteY.toInt() - 260).coerceIn(0, scrollState.maxValue)
                        scrollState.animateScrollTo(
                            value = targetY,
                            animationSpec = tween(durationMillis = 750, easing = FastOutSlowInEasing)
                        )
                    }
                }
            } catch (_: Exception) {}
        }
    }

    // Period selector modal (Today, Week, Month, Year, All)
    var selectedPeriod by remember { mutableStateOf("week") }
    var isPeriodModalOpen by remember { mutableStateOf(false) }

    // Day offset for Today's Prayers checklist (0 = Today, 1 = Yesterday, etc.)
    var dayOffset by remember { mutableStateOf(0) }

    // Month offset for monthly bar chart (0 = Current month, 1 = Previous month, etc.)
    var monthOffset by remember { mutableStateOf(0) }

    // Quotes carousel active slide index (0, 1, 2)
    var activeQuoteIndex by remember { mutableStateOf(0) }

    // History expanded state (4 days vs 14 days)
    var isHistExpanded by remember { mutableStateOf(false) }

    // Detailed insights breakdown toggle
    var isBreakdownOpen by remember { mutableStateOf(false) }

    // Reactive refresh trigger when prayer done is toggled
    var refreshTrigger by remember { mutableStateOf(0) }

    // Today's base prayer timings
    var todayData by remember { mutableStateOf<PrayerDayData?>(null) }

    val strings = remember(selectedLang) { SalahTrackerLocalization.get(selectedLang) }

    // Languages for LanguageBar
    val visibleLanguages = listOf(
        LanguageItem("en", "English"),
        LanguageItem("hi", "हिंदी"),
        LanguageItem("ur", "اردو"),
        LanguageItem("ar", "العربية"),
        LanguageItem("bn", "বাংলা")
    )

    // Date computation for Today's Prayers checklist
    val targetDateCal = remember(dayOffset) {
        Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -dayOffset) }
    }
    val targetDateStr = remember(targetDateCal) {
        String.format(
            Locale.US,
            "%02d-%02d-%04d",
            targetDateCal.get(Calendar.DAY_OF_MONTH),
            targetDateCal.get(Calendar.MONTH) + 1,
            targetDateCal.get(Calendar.YEAR)
        )
    }

    val isViewingToday = (dayOffset == 0)

    val dateDisplayLabel = remember(targetDateCal, selectedLang) {
        val locale = if (selectedLang.startsWith("hi")) Locale("hi", "IN") else Locale.ENGLISH
        SimpleDateFormat("d MMM yyyy", locale).format(targetDateCal.time)
    }

    // Approx Hijri text
    val targetHijriStr = remember(targetDateCal) {
        val (hDay, hMonth, hYear) = PrayerRepository.approximateHijri(targetDateCal)
        "$hDay $hMonth $hYear AH"
    }

    // Load today's prayer timings & settings
    val prayerSettings = remember { PrayerRepository.loadSettings() }
    val targetDayData = remember(targetDateCal, prayerSettings, refreshTrigger) {
        PrayerRepository.computeOfflineDay(targetDateCal.time, prayerSettings)
    }

    // Auto-advance quotes carousel every 8 seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(8000L)
            activeQuoteIndex = (activeQuoteIndex + 1) % 3
        }
    }

    // Colors according to theme
    val bgMain = if (isDarkTheme) Color(0xFF071510) else Color(0xFFFAF7F2)
    val cardBg = if (isDarkTheme) Color(0xFF0A1C15) else Color(0xFFFFFFFF)
    val cardBorder = if (isDarkTheme) Color(0xFFC9A84C).copy(alpha = 0.25f) else Color(0xFFE2E8F0)
    val inkColor = if (isDarkTheme) Color(0xFFF0EAD6) else Color(0xFF1A1408)
    val mutedColor = if (isDarkTheme) Color(0xFFA89F8B) else Color(0xFF64748B)

    // Period overview stats
    val periodStats = remember(selectedPeriod, refreshTrigger) {
        PrayerRepository.salahPeriodStats(selectedPeriod)
    }

    val currentStreak = remember(refreshTrigger) {
        PrayerRepository.salahStreak()
    }

    // Month chart computation
    val chartMonthCal = remember(monthOffset) {
        Calendar.getInstance().apply {
            add(Calendar.MONTH, -monthOffset)
            set(Calendar.DAY_OF_MONTH, 1)
        }
    }

    val chartMonthLabel = remember(chartMonthCal, selectedLang) {
        val locale = if (selectedLang.startsWith("hi")) Locale("hi", "IN") else Locale.ENGLISH
        SimpleDateFormat("MMMM yyyy", locale).format(chartMonthCal.time)
    }

    val daysInChartMonth = chartMonthCal.getActualMaximum(Calendar.DAY_OF_MONTH)
    val isCurrentChartMonth = (monthOffset == 0)
    val nowCal = Calendar.getInstance()
    val daysSoFarInChartMonth = if (isCurrentChartMonth) nowCal.get(Calendar.DAY_OF_MONTH) else daysInChartMonth

    // Calculate daily bars for the month
    val monthDailyCounts = remember(monthOffset, refreshTrigger) {
        val list = mutableListOf<Pair<Int, Boolean>>() // count (0..5), isFuture
        val tempCal = chartMonthCal.clone() as Calendar
        val today = Calendar.getInstance()
        val currentYear = today.get(Calendar.YEAR)
        val currentDayOfYear = today.get(Calendar.DAY_OF_YEAR)

        for (d in 1..daysInChartMonth) {
            tempCal.set(Calendar.DAY_OF_MONTH, d)
            val isFuture = if (tempCal.get(Calendar.YEAR) > currentYear) true
            else if (tempCal.get(Calendar.YEAR) == currentYear) tempCal.get(Calendar.DAY_OF_YEAR) > currentDayOfYear
            else false

            val count = if (isFuture) 0 else PrayerRepository.salahDayCount(tempCal)
            list.add(Pair(count, isFuture))
        }
        list
    }

    val monthDoneTotal = remember(monthDailyCounts, daysSoFarInChartMonth) {
        monthDailyCounts.take(daysSoFarInChartMonth).sumOf { it.first }
    }
    val monthPossibleTotal = daysSoFarInChartMonth * 5
    val monthPct = if (monthPossibleTotal > 0) ((monthDoneTotal * 100) / monthPossibleTotal) else 0

    // Per-prayer breakdown for selected month
    val perPrayerStats = remember(monthOffset, refreshTrigger, daysSoFarInChartMonth) {
        val prayers = listOf(
            Triple("Fajr", "🌅", strings.fajr),
            Triple("Dhuhr", "☀️", strings.dhuhr),
            Triple("Asr", "🌤", strings.asr),
            Triple("Maghrib", "🌇", strings.maghrib),
            Triple("Isha", "🌙", strings.isha)
        )
        val tempCal = chartMonthCal.clone() as Calendar
        prayers.map { (pId, icon, name) ->
            var doneCount = 0
            for (d in 1..daysSoFarInChartMonth) {
                tempCal.set(Calendar.DAY_OF_MONTH, d)
                val dStr = String.format(Locale.US, "%02d-%02d-%04d", d, tempCal.get(Calendar.MONTH) + 1, tempCal.get(Calendar.YEAR))
                if (PrayerRepository.isPrayerDone(pId, dStr)) {
                    doneCount++
                }
            }
            val pct = if (daysSoFarInChartMonth > 0) ((doneCount * 100) / daysSoFarInChartMonth) else 0
            PerPrayerItemData(pId, icon, name, doneCount, daysSoFarInChartMonth, pct)
        }
    }

    val sortedByPct = remember(perPrayerStats) {
        perPrayerStats.sortedByDescending { it.pct }
    }
    val bestPrayer = sortedByPct.firstOrNull() ?: PerPrayerItemData("Fajr", "🌅", strings.fajr, 0, 1, 0)
    val worstPrayer = sortedByPct.lastOrNull() ?: PerPrayerItemData("Isha", "🌙", strings.isha, 0, 1, 0)

    // Pre-load Interstitial Ad for seamless back exit
    LaunchedEffect(Unit) {
        AdMobInterstitialManager.loadAd(context)
    }

    val activity = context as? Activity
    val handleBackWithAd = {
        if (activity != null) {
            AdMobInterstitialManager.showAd(activity, "salah_back") {
                onBack()
            }
        } else {
            onBack()
        }
    }

    // Back handling
    BackHandler {
        if (isTourOpen) {
            PrayerRepository.setSalahTourDone(true)
            isTourOpen = false
        } else if (isPeriodModalOpen) {
            isPeriodModalOpen = false
        } else if (isMoreLangOpen) {
            isMoreLangOpen = false
        } else {
            handleBackWithAd()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgMain)
            .onGloballyPositioned { rootCoords = it }
    ) {
        Scaffold(
            topBar = {
                SalahTrackerHeaderTopBar(
                    title = strings.title,
                    isDarkTheme = isDarkTheme,
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
                // 1. LANGUAGE BAR (English, हिंदी, اردو, العربية, বাংলা, 🌐 +20)
                LanguageBar(
                    languages = visibleLanguages,
                    selectedCode = selectedLang,
                    onSelectLanguage = { code ->
                        selectedLang = code
                        PrayerRepository.saveLanguage(code)
                    },
                    onOpenMoreLanguages = {
                        isMoreLangOpen = true
                    }
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // 2. ISLAMIC QUOTES CAROUSEL CARD (3 dynamic slides)
                    SalahQuoteCard(
                        quoteIndex = activeQuoteIndex,
                        strings = strings,
                        onSelectIndex = { activeQuoteIndex = it }
                    )

                    // 3. REAL ADMOB NATIVE AD SLOT 1 (Zero-gap collapse)
                    if (!isTourOpen) {
                        AdMobNativeAdCard(
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                // 4. OVERVIEW SECTION HEADER & TIMEFRAME SELECTOR
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = strings.overview,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = inkColor
                    )

                    // Royal Period Selector Pill Button (st-period-btn)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(cardBg)
                            .border(1.2.dp, Color(0xFF0E5D47).copy(alpha = 0.28f), RoundedCornerShape(50.dp))
                            .clickable { isPeriodModalOpen = true }
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            val periodLabel = when (selectedPeriod) {
                                "today" -> strings.todayPrayers
                                "month" -> strings.thisMonth
                                "year" -> strings.thisYear
                                "all" -> strings.allTime
                                else -> strings.thisWeek
                            }
                            Text(
                                text = periodLabel,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color(0xFFF3D48A) else Color(0xFF0E5D47)
                            )
                            Text(
                                text = "▼",
                                fontSize = 8.sp,
                                color = if (isDarkTheme) Color(0xFFF3D48A) else Color(0xFF0E5D47)
                            )
                        }
                    }
                }

                // 5. 4 STAT CARDS OVERVIEW GRID (Score, Performed, Streak, Improvement)
                SalahStatGrid(
                    stats = periodStats,
                    streak = currentStreak,
                    strings = strings,
                    isDarkTheme = isDarkTheme,
                    cardBg = cardBg,
                    cardBorder = cardBorder,
                    inkColor = inkColor,
                    mutedColor = mutedColor
                )

                // 6. TODAY'S PRAYERS CHECKLIST WITH DATE STEPPER
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Previous Day Circular Button
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(cardBg)
                                .border(1.2.dp, Color(0xFFCBD5E1), CircleShape)
                                .clickable { dayOffset++ },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "‹",
                                fontSize = 19.sp,
                                color = Color(0xFF146450),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Date Title (Stacked or 2 lines)
                        Column(
                            verticalArrangement = Arrangement.Center
                        ) {
                            if (isViewingToday) {
                                Text(
                                    text = "Today's",
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = inkColor,
                                    lineHeight = 15.sp
                                )
                                Text(
                                    text = "Prayers",
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = inkColor,
                                    lineHeight = 15.sp
                                )
                            } else {
                                val dayMonthStr = SimpleDateFormat("d MMM", Locale.ENGLISH).format(targetDateCal.time)
                                val yearStr = SimpleDateFormat("yyyy", Locale.ENGLISH).format(targetDateCal.time)
                                Text(
                                    text = dayMonthStr,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = inkColor,
                                    lineHeight = 15.sp
                                )
                                Text(
                                    text = yearStr,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = inkColor,
                                    lineHeight = 15.sp
                                )
                            }
                        }

                        // Next Day Circular Button (Disabled if viewing today)
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(if (isViewingToday) cardBg.copy(alpha = 0.45f) else cardBg)
                                .border(1.2.dp, if (isViewingToday) Color(0xFFE2E8F0) else Color(0xFFCBD5E1), CircleShape)
                                .clickable(enabled = !isViewingToday) { if (dayOffset > 0) dayOffset-- },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "›",
                                fontSize = 19.sp,
                                color = if (isViewingToday) Color(0xFFCBD5E1) else Color(0xFF146450),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Gregorian / Hijri date meta
                    Text(
                        text = "$dateDisplayLabel / $targetHijriStr",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF64748B),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // 5 Prayer Cards Row
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { prayerRowCoords = it }
                ) {
                    SalahPrayerCardsRow(
                        dateStr = targetDateStr,
                        isViewingToday = isViewingToday,
                        dayData = targetDayData,
                        strings = strings,
                        isDarkTheme = isDarkTheme,
                        onTogglePrayer = { pId ->
                            val newlyDone = PrayerRepository.togglePrayerDone(pId, targetDateStr)
                            if (newlyDone) {
                                AppToast.show("✨ Masha ALLAH")
                            }
                            refreshTrigger++
                        }
                    )
                }

                // Salah Quote Carousel Dots (salahQuoteGoto)
                SalahQuoteCarousel(
                    selectedLang = selectedLang,
                    isDarkTheme = isDarkTheme
                )

                // 7. MONTHLY PROGRESS BAR CHART PANEL
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { monthlyChartCoords = it }
                ) {
                    SalahMonthlyChartPanel(
                        monthLabel = chartMonthLabel,
                        daysInMonth = daysInChartMonth,
                        dailyCounts = monthDailyCounts,
                        monthDone = monthDoneTotal,
                        monthPossible = monthPossibleTotal,
                        monthPct = monthPct,
                        isCurrentMonth = isCurrentChartMonth,
                        strings = strings,
                        isDarkTheme = isDarkTheme,
                        cardBg = cardBg,
                        cardBorder = cardBorder,
                        inkColor = inkColor,
                        mutedColor = mutedColor,
                        onPrevMonth = { monthOffset++ },
                        onNextMonth = { if (monthOffset > 0) monthOffset-- }
                    )
                }

                // 8. CONSISTENCY NOOR RING PANEL
                SalahConsistencyPanel(
                    monthLabel = chartMonthLabel,
                    pct = monthPct,
                    strings = strings,
                    isDarkTheme = isDarkTheme,
                    cardBg = cardBg,
                    cardBorder = cardBorder,
                    inkColor = inkColor
                )

                // 9. REAL ADMOB NATIVE AD SLOT 2 (Insights Ad)
                if (!isTourOpen) {
                    AdMobNativeAdCard(
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // 10. INSIGHTS & DETAILED BREAKDOWN PANEL
                SalahInsightsPanel(
                    monthLabel = chartMonthLabel,
                    best = bestPrayer,
                    worst = worstPrayer,
                    perPrayer = perPrayerStats,
                    isOpen = isBreakdownOpen,
                    strings = strings,
                    isDarkTheme = isDarkTheme,
                    cardBg = cardBg,
                    cardBorder = cardBorder,
                    inkColor = inkColor,
                    onToggleBreakdown = { isBreakdownOpen = !isBreakdownOpen }
                )

                // 11. RECENT HISTORY LIST
                SalahHistoryPanel(
                    isExpanded = isHistExpanded,
                    strings = strings,
                    isDarkTheme = isDarkTheme,
                    cardBg = cardBg,
                    cardBorder = cardBorder,
                    inkColor = inkColor,
                    mutedColor = mutedColor,
                    onToggleExpand = { isHistExpanded = !isHistExpanded }
                )

                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }

    // More Languages Modal
        if (isMoreLangOpen) {
            MoreLanguagesModal(
                selectedCode = selectedLang,
                onSelectLanguage = { code ->
                    selectedLang = code
                    PrayerRepository.saveLanguage(code)
                },
                onDismiss = { isMoreLangOpen = false }
            )
        }

        // Timeframe Selector Modal (Today, Week, Month, Year, All)
        if (isPeriodModalOpen) {
            SalahPeriodModal(
                activeKey = selectedPeriod,
                strings = strings,
                isDarkTheme = isDarkTheme,
                onSelect = {
                    selectedPeriod = it
                    isPeriodModalOpen = false
                },
                onDismiss = { isPeriodModalOpen = false }
            )
        }

        // Masjid Jamat Review Prompt Modal (jamatReviewPromptModal)
        if (isJamatReviewOpen) {
            MasjidJamatReviewModal(
                onDismiss = {
                    isJamatReviewOpen = false
                    PrayerRepository.snoozeJamatReviewFor15Days()
                },
                onChangeJamat = {
                    isJamatReviewOpen = false
                    PrayerRepository.markJamatUpdatedNow()
                    onOpenPrayerTimes()
                },
                onKeepCurrent = {
                    PrayerRepository.keepJamatCurrentFor30Days()
                    isJamatReviewOpen = false
                    AppToast.show("Jamat times confirmed ✓")
                },
                onCancelSnooze = {
                    PrayerRepository.snoozeJamatReviewFor15Days()
                    isJamatReviewOpen = false
                }
            )
        }

        // Minimal Spotlight Tour Guide Modal (Single-step on 5 daily prayers)
        if (isTourOpen) {
            val tourTx = remember(selectedLang) { SalahTourLocalization.get(selectedLang) }
            val stepLabel = if (selectedLang.startsWith("hi")) "क्विक टिप" else if (selectedLang.startsWith("ur")) "کوئیک گائیڈ" else "Quick Tip"
            GeneralSpotlightTourModal(
                step = 1,
                totalSteps = 1,
                stepLabel = stepLabel,
                title = tourTx.todayTitle,
                description = tourTx.todayDesc,
                nextBtnText = tourTx.next,
                doneBtnText = tourTx.done,
                skipBtnText = tourTx.skip,
                targetRect = currentTargetRect,
                cornerRadiusDp = 20f,
                padDp = 6f,
                onNext = {
                    PrayerRepository.setSalahTourDone(true)
                    isTourOpen = false
                },
                onDone = {
                    PrayerRepository.setSalahTourDone(true)
                    isTourOpen = false
                },
                onSkip = {
                    PrayerRepository.setSalahTourDone(true)
                    isTourOpen = false
                }
            )
        }
    }
}

data class PerPrayerItemData(
    val id: String,
    val icon: String,
    val name: String,
    val count: Int,
    val total: Int,
    val pct: Int
)

// ══════════════════════════════════════════════════════════════
//  1. TOPBAR HEADER WITH MOSQUE ARTWORK & ACTION BUTTONS
// ══════════════════════════════════════════════════════════════
@Composable
fun SalahTrackerHeaderTopBar(
    title: String,
    isDarkTheme: Boolean,
    onBack: () -> Unit,
    onToggleTheme: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFF146450),
                        Color(0xFF0C4A3A),
                        Color(0xFF062018)
                    )
                )
            )
            .border(
                width = 1.5.dp,
                brush = Brush.verticalGradient(
                    listOf(
                        Color.Transparent,
                        Color(0xFFE8C878).copy(alpha = 0.55f)
                    )
                ),
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
    ) {
        // Mosque silhouette artwork decoration (st-hdr-deco)
        val painter = rememberAssetPainter("media/6d88c4d6a7d5af94.webp")
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
                .align(Alignment.BottomCenter)
        )

        // Gradient fade to blend mosque artwork smoothly into background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            Color(0xFF062018).copy(alpha = 0.85f)
                        )
                    )
                )
        )

        // Topbar controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Circular Back Button (st-back)
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.22f))
                    .border(1.dp, Color.White.copy(alpha = 0.35f), CircleShape)
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "‹",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Screen Title
            Text(
                text = "SALAH TRACKER",
                fontFamily = FontFamily.Serif,
                fontSize = 16.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )

            // Sun/Theme ☀️ Button (st-theme-btn)
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFFEF3C7))
                    .border(1.dp, Color(0xFFF59E0B).copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                    .clickable { onToggleTheme() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "☀️",
                    fontSize = 15.sp
                )
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  2. ISLAMIC QUOTE CAROUSEL CARD (st-quote-card)
// ══════════════════════════════════════════════════════════════
@Composable
fun SalahQuoteCard(
    quoteIndex: Int,
    strings: SalahTrackerStrings,
    onSelectIndex: (Int) -> Unit
) {
    val quoteTxt = when (quoteIndex) {
        0 -> strings.q1Txt
        1 -> strings.q2Txt
        else -> strings.q3Txt
    }
    val quoteRef = when (quoteIndex) {
        0 -> strings.q1Ref
        1 -> strings.q2Ref
        else -> strings.q3Ref
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFF0B3B2D),
                        Color(0xFF072C21)
                    )
                )
            )
            .border(1.2.dp, Color(0xFFC9A84C).copy(alpha = 0.35f), RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Mosque circular badge (st-quote-ico)
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF0E5D47).copy(alpha = 0.7f))
                        .border(1.5.dp, Color(0xFFC9A84C), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🕌",
                        fontSize = 20.sp
                    )
                }

                // Quote Text & Reference
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "\"$quoteTxt\"",
                        fontFamily = FontFamily.Serif,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = quoteRef,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFD4AF37)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3 Dots Indicator (st-quote-dots)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0..2) {
                    val isActive = (i == quoteIndex)
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .size(if (isActive) 24.dp else 5.dp, 5.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(
                                if (isActive) Color(0xFFC9A84C) else Color.White.copy(alpha = 0.28f)
                            )
                            .clickable { onSelectIndex(i) }
                    )
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  3. 4-CARD STATISTICS GRID (st-stat-grid)
// ══════════════════════════════════════════════════════════════
@Composable
fun SalahStatGrid(
    stats: PrayerRepository.SalahPeriodStats,
    streak: Int,
    strings: SalahTrackerStrings,
    isDarkTheme: Boolean,
    cardBg: Color,
    cardBorder: Color,
    inkColor: Color,
    mutedColor: Color
) {
    val levelText = if (stats.pct >= 75) strings.lvlGreat else if (stats.pct >= 50) strings.lvlGood else strings.lvlLow

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Card 1: Score with Mini Noor Ring
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(cardBg)
                    .border(1.5.dp, cardBorder, RoundedCornerShape(16.dp))
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Mini Ring (52dp)
                    Box(
                        modifier = Modifier.size(52.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            // Track
                            drawCircle(
                                color = Color(0xFFC9A84C).copy(alpha = 0.18f),
                                style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                            )
                            // Fill
                            val sweep = (stats.pct / 100f) * 360f
                            drawArc(
                                brush = Brush.linearGradient(
                                    listOf(Color(0xFF0E5D47), Color(0xFF1F9D63))
                                ),
                                startAngle = -90f,
                                sweepAngle = sweep,
                                useCenter = false,
                                style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                            )
                        }
                        Text(
                            text = "${stats.pct}%",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = inkColor
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = strings.score,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = mutedColor
                    )
                    Text(
                        text = levelText,
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F9D63)
                    )
                }
            }

            // Card 2: Performed prayers count
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(cardBg)
                    .border(1.5.dp, cardBorder, RoundedCornerShape(16.dp))
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Green circular checkmark badge (stIconCheck)
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF146450), Color(0xFF0C4A3A))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✓",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${stats.done}/${stats.total}",
                        fontFamily = FontFamily.Serif,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = inkColor
                    )
                    Text(
                        text = strings.performed,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = mutedColor
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Card 3: Streak flame
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(cardBg)
                    .border(1.5.dp, cardBorder, RoundedCornerShape(16.dp))
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Flame icon badge (stIconFlame)
                    Text(
                        text = "🔥",
                        fontSize = 28.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$streak",
                        fontFamily = FontFamily.Serif,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = inkColor
                    )
                    Text(
                        text = "${strings.streak} (${strings.days})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = mutedColor
                    )
                }
            }

            // Card 4: Improvement Delta
            val isPositive = (stats.delta >= 0)
            val deltaColor = if (isPositive) Color(0xFF146450) else Color(0xFFD1495B)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(cardBg)
                    .border(1.5.dp, cardBorder, RoundedCornerShape(16.dp))
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(deltaColor.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isPositive) "↗" else "↘",
                            color = deltaColor,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${if (isPositive) "+" else ""}${stats.delta}%",
                        fontFamily = FontFamily.Serif,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = deltaColor
                    )
                    Text(
                        text = strings.improve,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = mutedColor
                    )
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  4. 5 PRAYER CARDS CHECKLIST ROW (st-prayer-card)
// ══════════════════════════════════════════════════════════════
@Composable
fun SalahPrayerCardsRow(
    dateStr: String,
    isViewingToday: Boolean,
    dayData: PrayerDayData?,
    strings: SalahTrackerStrings,
    isDarkTheme: Boolean,
    onTogglePrayer: (String) -> Unit
) {
    val prayers = listOf(
        Triple("Fajr", "🌅", strings.fajr),
        Triple("Dhuhr", "☀️", strings.dhuhr),
        Triple("Asr", "🌤", strings.asr),
        Triple("Maghrib", "🌇", strings.maghrib),
        Triple("Isha", "🌙", strings.isha)
    )

    val now = Calendar.getInstance()
    val nowMins = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        prayers.forEach { (pId, icon, name) ->
            val isDone = PrayerRepository.isPrayerDone(pId, dateStr)
            val timeRaw = dayData?.timings?.get(pId) ?: "05:00"
            val displayTime = format12hUpper(timeRaw)

            // Calculate if prayer is past
            val pMins = parseTimeMins(timeRaw)
            val isPast = if (!isViewingToday) true else (nowMins >= pMins)

            val statusLabel = if (isDone) strings.prayed else if (isPast) strings.missed else strings.upcoming

            val cardBg = if (isDone) {
                if (isDarkTheme) Color(0xFF146450).copy(alpha = 0.25f) else Color(0xFFF0FDF4)
            } else if (isPast) {
                if (isDarkTheme) Color(0xFFEF4444).copy(alpha = 0.14f) else Color(0xFFFFF5F5)
            } else {
                if (isDarkTheme) Color(0xFF1E293B) else Color.White
            }

            val borderColor = if (isDone) {
                Color(0xFF0E5D47)
            } else if (isPast) {
                Color(0xFFFECACA)
            } else {
                Color(0xFFE2E8F0)
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(cardBg)
                    .border(1.5.dp, borderColor, RoundedCornerShape(14.dp))
                    .clickable { onTogglePrayer(pId) }
                    .padding(vertical = 10.dp, horizontal = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Circular Prayer Icon Badge (st-prayer-ico)
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF0E5D47), Color(0xFF083327))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = icon,
                            fontSize = 17.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    // Prayer Name
                    Text(
                        text = name,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color(0xFFF0EAD6) else Color(0xFF0F172A),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Waqt Time
                    Text(
                        text = displayTime,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isDarkTheme) Color(0xFFA89F8B) else Color(0xFF64748B)
                    )

                    Spacer(modifier = Modifier.height(7.dp))

                    // Circular Tick Checkbox (st-circle-btn)
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(
                                if (isDone) Color(0xFF0E5D47)
                                else Color.White
                            )
                            .border(
                                width = if (isDone) 0.dp else 2.dp,
                                color = if (isDone) Color.Transparent else if (isPast) Color(0xFFEF4444) else Color(0xFFCBD5E1),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isDone) {
                            Text(
                                text = "✓",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Status Pill Label (st-status-label)
                    Text(
                        text = statusLabel,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isDone) Color(0xFF0E5D47) else if (isPast) Color(0xFFEF4444) else Color(0xFF64748B)
                    )
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  5. MONTHLY PROGRESS BAR CHART PANEL (st-panel)
// ══════════════════════════════════════════════════════════════
@Composable
fun SalahMonthlyChartPanel(
    monthLabel: String,
    daysInMonth: Int,
    dailyCounts: List<Pair<Int, Boolean>>,
    monthDone: Int,
    monthPossible: Int,
    monthPct: Int,
    isCurrentMonth: Boolean,
    strings: SalahTrackerStrings,
    isDarkTheme: Boolean,
    cardBg: Color,
    cardBorder: Color,
    inkColor: Color,
    mutedColor: Color,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cardBg)
            .border(1.6.dp, cardBorder, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column {
            // Header with month stepper and percentage badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Prev Month
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(cardBg)
                            .border(1.2.dp, cardBorder, RoundedCornerShape(8.dp))
                            .clickable { onPrevMonth() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "‹", fontSize = 16.sp, color = Color(0xFF146450), fontWeight = FontWeight.Bold)
                    }

                    Text(
                        text = monthLabel,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = inkColor
                    )

                    // Next Month
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isCurrentMonth) cardBg.copy(alpha = 0.4f) else cardBg)
                            .border(1.2.dp, cardBorder.copy(alpha = if (isCurrentMonth) 0.3f else 1f), RoundedCornerShape(8.dp))
                            .clickable(enabled = !isCurrentMonth) { onNextMonth() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "›", fontSize = 16.sp, color = if (isCurrentMonth) Color(0xFFCBD5E1) else Color(0xFF146450), fontWeight = FontWeight.Bold)
                    }
                }

                // Month % Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF146450), Color(0xFF0C4A3A))
                            )
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "$monthPct%",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Chart area: Y-Axis + Bars
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(112.dp)
            ) {
                // Y-Axis labels
                Column(
                    modifier = Modifier
                        .width(30.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.End
                ) {
                    listOf("100%", "75%", "50%", "25%", "0%").forEach {
                        Text(text = it, fontSize = 8.5.sp, color = mutedColor)
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Daily Bars (1..daysInMonth)
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    dailyCounts.forEach { (count, isFuture) ->
                        val heightFraction = if (isFuture) 0.12f else if (count == 0) 0.035f else (count * 20 / 100f)
                        val animatedHeight by animateFloatAsState(
                            targetValue = heightFraction,
                            animationSpec = tween(400)
                        )

                        val barModifier = if (isFuture) {
                            Modifier.background(if (isDarkTheme) Color(0xFF1E293B) else Color(0xFFEDE8DC))
                        } else {
                            Modifier.background(Color(0xFF0E5D47))
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(animatedHeight)
                                .clip(RoundedCornerShape(3.dp))
                                .then(barModifier)
                        )
                    }
                }
            }

            // X-Axis ticks: 1, 5, 10, 15, 20, 25, daysInMonth
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 36.dp, top = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf(1, 5, 10, 15, 20, 25, daysInMonth).forEach {
                    Text(text = "$it", fontSize = 8.5.sp, color = mutedColor)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Summary Strip (Performed & Score)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        brush = Brush.horizontalGradient(listOf(Color(0xFF146450).copy(alpha = 0.2f), Color.Transparent)),
                        shape = RoundedCornerShape(0.dp)
                    )
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${strings.performed}: $monthDone/$monthPossible",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = inkColor
                )
                Text(
                    text = "${strings.score}: $monthPct%",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF146450)
                )
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  6. CONSISTENCY NOOR RING PANEL (st-panel)
// ══════════════════════════════════════════════════════════════
@Composable
fun SalahConsistencyPanel(
    monthLabel: String,
    pct: Int,
    strings: SalahTrackerStrings,
    isDarkTheme: Boolean,
    cardBg: Color,
    cardBorder: Color,
    inkColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cardBg)
            .border(1.6.dp, cardBorder, RoundedCornerShape(16.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${strings.consistency} ($monthLabel)",
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = inkColor,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Big Noor Ring (96dp)
            Box(
                modifier = Modifier.size(96.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = Color(0xFFC9A84C).copy(alpha = 0.18f),
                        style = Stroke(width = 9.dp.toPx(), cap = StrokeCap.Round)
                    )
                    val sweep = (pct / 100f) * 360f
                    drawArc(
                        brush = Brush.linearGradient(
                            listOf(Color(0xFF0E5D47), Color(0xFF1F9D63))
                        ),
                        startAngle = -90f,
                        sweepAngle = sweep,
                        useCenter = false,
                        style = Stroke(width = 9.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                Text(
                    text = "$pct%",
                    fontFamily = FontFamily.Serif,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = inkColor
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = strings.keepItUp,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = inkColor
            )
            Text(
                text = strings.doingGreat,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF146450)
            )
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  7. INSIGHTS & DETAILED BREAKDOWN PANEL (st-panel)
// ══════════════════════════════════════════════════════════════
@Composable
fun SalahInsightsPanel(
    monthLabel: String,
    best: PerPrayerItemData,
    worst: PerPrayerItemData,
    perPrayer: List<PerPrayerItemData>,
    isOpen: Boolean,
    strings: SalahTrackerStrings,
    isDarkTheme: Boolean,
    cardBg: Color,
    cardBorder: Color,
    inkColor: Color,
    onToggleBreakdown: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cardBg)
            .border(1.6.dp, cardBorder, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column {
            Text(
                text = "${strings.insights} ($monthLabel)",
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = inkColor
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Row 1: Best prayer (Star icon)
            SalahInsightRow(
                icon = "⭐",
                iconBg = Color(0xFFE6F4EA),
                iconBorder = Color(0xFF0E5D47).copy(alpha = 0.4f),
                label = strings.best,
                value = "${best.name} (${best.pct}%)",
                valColor = Color(0xFF146450),
                isDarkTheme = isDarkTheme
            )

            HorizontalDivider(color = if (isDarkTheme) Color(0xFF1E293B) else Color(0xFFF1F5F9), thickness = 1.dp)

            // Row 2: Most missed prayer (Clock icon)
            SalahInsightRow(
                icon = "🕒",
                iconBg = Color(0xFFFCE8E6),
                iconBorder = Color(0xFFEF4444).copy(alpha = 0.4f),
                label = strings.mostMissed,
                value = "${worst.name} (${100 - worst.pct}%)",
                valColor = Color(0xFFEF4444),
                isDarkTheme = isDarkTheme
            )

            HorizontalDivider(color = if (isDarkTheme) Color(0xFF1E293B) else Color(0xFFF1F5F9), thickness = 1.dp)

            // Row 3: Improvement area (Chart icon)
            SalahInsightRow(
                icon = "📈",
                iconBg = Color(0xFFE6F4EA),
                iconBorder = Color(0xFF0E5D47).copy(alpha = 0.4f),
                label = strings.improveArea,
                value = worst.name,
                valColor = Color(0xFF146450),
                isDarkTheme = isDarkTheme
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Detailed insights toggle button (st-detail-btn)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0E5D47))
                    .clickable { onToggleBreakdown() }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = "📊", fontSize = 14.sp)
                    Text(
                        text = "${strings.viewInsights} ${if (isOpen) "▲" else ""}",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Expandable detailed breakdown list
            AnimatedVisibility(visible = isOpen) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    perPrayer.forEach { p ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "${p.icon} ${p.name} (${p.count}/${p.total})",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = inkColor,
                                modifier = Modifier.width(110.dp)
                            )

                            // Track & Fill bar
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(5.dp))
                                    .background(Color(0xFFC9A84C).copy(alpha = 0.15f))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(p.pct / 100f)
                                        .clip(RoundedCornerShape(5.dp))
                                        .background(
                                            Brush.horizontalGradient(
                                                listOf(Color(0xFF0E5D47), Color(0xFF10B981))
                                            )
                                        )
                                )
                            }

                            Text(
                                text = "${p.pct}%",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF146450),
                                modifier = Modifier.width(36.dp),
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SalahInsightRow(
    icon: String,
    iconBg: Color,
    iconBorder: Color,
    label: String,
    value: String,
    valColor: Color,
    isDarkTheme: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(iconBg)
                    .border(1.dp, iconBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = icon, fontSize = 15.sp)
            }

            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDarkTheme) Color(0xFFF0E6CC) else Color(0xFF1A1408)
            )
        }

        Text(
            text = value,
            fontSize = 13.5.sp,
            fontWeight = FontWeight.ExtraBold,
            color = valColor
        )
    }
}

// ══════════════════════════════════════════════════════════════
//  8. RECENT HISTORY LIST PANEL
// ══════════════════════════════════════════════════════════════
@Composable
fun SalahHistoryPanel(
    isExpanded: Boolean,
    strings: SalahTrackerStrings,
    isDarkTheme: Boolean,
    cardBg: Color,
    cardBorder: Color,
    inkColor: Color,
    mutedColor: Color,
    onToggleExpand: () -> Unit
) {
    val countDays = if (isExpanded) 14 else 4
    val historyItems = remember(countDays) {
        val list = mutableListOf<Triple<String, Int, Int>>() // dateLabel, count, pct
        val cal = Calendar.getInstance()
        for (i in 0 until countDays) {
            val d = (cal.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, -i) }
            val dStr = String.format(Locale.US, "%02d-%02d-%04d", d.get(Calendar.DAY_OF_MONTH), d.get(Calendar.MONTH) + 1, d.get(Calendar.YEAR))
            val dLabel = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(d.time)
            val done = PrayerRepository.salahDayCount(dStr)
            val pct = (done * 100) / 5
            list.add(Triple(dLabel, done, pct))
        }
        list
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cardBg)
            .border(1.6.dp, cardBorder, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = strings.recent,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = inkColor
                )

                Text(
                    text = if (isExpanded) strings.showLess else strings.viewAll,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFC9A84C),
                    modifier = Modifier.clickable { onToggleExpand() }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            historyItems.forEach { (dLabel, count, pct) ->
                val badgeBg = if (pct >= 80) Color(0xFF1F9D63).copy(alpha = 0.14f)
                else if (pct >= 50) Color(0xFFD97706).copy(alpha = 0.14f)
                else Color(0xFFD1495B).copy(alpha = 0.14f)

                val badgeColor = if (pct >= 80) Color(0xFF146450)
                else if (pct >= 50) Color(0xFFD97706)
                else Color(0xFFD1495B)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "📅", fontSize = 14.sp)
                        Text(
                            text = dLabel,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = inkColor
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "$count/5 ${strings.prayersWord}",
                            fontSize = 11.5.sp,
                            color = mutedColor,
                            fontWeight = FontWeight.SemiBold
                        )

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(badgeBg)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "$pct%",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = badgeColor
                            )
                        }
                    }
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  9. TIMEFRAME SELECTION MODAL (st-period-modal-card)
// ══════════════════════════════════════════════════════════════
@Composable
fun SalahPeriodModal(
    activeKey: String,
    strings: SalahTrackerStrings,
    isDarkTheme: Boolean,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val options = listOf(
        OptionPeriodItem("today", "📅", strings.todayPrayers, Color(0xFFEDE9FE)),
        OptionPeriodItem("week", "🗓️", strings.thisWeek, Color(0xFF0E5D47)),
        OptionPeriodItem("month", "🌙", strings.thisMonth, Color(0xFFFEF3C7)),
        OptionPeriodItem("year", "🕌", strings.thisYear, Color(0xFFF3E8FF)),
        OptionPeriodItem("all", "🌟", strings.allTime, Color(0xFFFEF9C3))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.65f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(if (isDarkTheme) Color(0xFF0D1A14) else Color(0xFFFAF7F2))
                .border(1.5.dp, Color(0xFFC9A84C).copy(alpha = 0.4f), RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .padding(18.dp)
                .clickable(enabled = false) { /* Prevent dismissing on card click */ }
        ) {
            // Drag handle
            Box(
                modifier = Modifier
                    .size(44.dp, 4.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFCBD5E1))
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "⏳", fontSize = 16.sp)
                    Text(
                        text = "OVERVIEW",
                        fontFamily = FontFamily.Serif,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF0E5D47),
                        letterSpacing = 1.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF0E5D47).copy(alpha = 0.08f))
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "✕", fontSize = 13.sp, color = Color(0xFF0E5D47), fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Options list
            options.forEach { opt ->
                val isActive = (opt.key == activeKey)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            if (isActive) Color(0xFFEAF5F0)
                            else if (isDarkTheme) Color(0xFF13241C) else Color.White
                        )
                        .border(
                            width = 1.5.dp,
                            color = if (isActive) Color(0xFF0E5D47) else Color(0xFFE2E8F0),
                            shape = RoundedCornerShape(14.dp)
                        )
                        .clickable { onSelect(opt.key) }
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isActive) Color(0xFF0E5D47) else opt.iconBg),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = opt.icon, fontSize = 16.sp)
                            }

                            Text(
                                text = opt.label,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isActive) Color(0xFF0E5D47) else if (isDarkTheme) Color(0xFFF0EAD6) else Color(0xFF14352B)
                            )
                        }

                        // Radio check indicator
                        if (isActive) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF0E5D47)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "✓", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .border(1.5.dp, Color(0xFFCBD5E1), CircleShape)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

data class OptionPeriodItem(
    val key: String,
    val icon: String,
    val label: String,
    val iconBg: Color
)

// ══════════════════════════════════════════════════════════════
//  HELPER FUNCTIONS (Time conversion)
// ══════════════════════════════════════════════════════════════
private fun parseTimeMins(raw: String): Int {
    return try {
        val parts = raw.split(":")
        val h = parts[0].trim().toInt()
        val m = parts[1].trim().take(2).toInt()
        h * 60 + m
    } catch (_: Exception) {
        0
    }
}

private fun format12hUpper(raw: String): String {
    return try {
        val parts = raw.split(":")
        var h = parts[0].trim().toInt()
        val m = parts[1].trim().take(2).toInt()
        val ampm = if (h >= 12) "PM" else "AM"
        if (h > 12) h -= 12
        if (h == 0) h = 12
        String.format(Locale.US, "%02d:%02d %s", h, m, ampm)
    } catch (_: Exception) {
        raw
    }
}
