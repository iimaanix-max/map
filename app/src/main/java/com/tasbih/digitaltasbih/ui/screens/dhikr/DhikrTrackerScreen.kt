package com.tasbih.digitaltasbih.ui.screens.dhikr

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.ui.theme.AmiriQuranFont
import com.tasbih.digitaltasbih.ui.theme.AmiriBoldFont
import com.tasbih.digitaltasbih.data.*
import com.tasbih.digitaltasbih.ui.components.*
import com.tasbih.digitaltasbih.util.AdMobInterstitialManager
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * DhikrTrackerScreen - 100% Native Jetpack Compose implementation
 * Faithfully recreating every visual detail and behavior from the original web app:
 * 1. Top Mosque Artwork Header with [←] Back, [DHIKR TRACKER] Title, [☀️] Theme Toggle.
 * 2. Language Selector Bar (English, हिंदी, اردو, العربية, বাংলা, 🌐 +20).
 * 3. 3-Slide Islamic Quotes Carousel (Verily in the remembrance..., Whoever says SubhanAllahil Azeem..., And the remembrance...).
 * 4. "Today's Overview" with Day Stepper [ < ] ... [ > ], Gregorian + Hijri date, 4-stat cards (Total, Recited, Streak, Improvement).
 * 5. "Today's Dhikr" with auto-sync subtitle, Moroccan 8-point gold rosette Dhikr card row, and View all toggle.
 * 6. "Weekly Progress" Card with [Week] [Month] [Year] toggle tabs and dynamic Bar Chart.
 * 7. "Most Frequent" Card with top 3 Dhikrs dynamically sourced from all-time Tasbih counts.
 * 8. "Dhikr Insights" Card with Best Time (prayer window), Total This Week, Daily Average.
 * 9. Motivational Pill Card ("Keep your tongue moist with dhikr...").
 * 10. Expandable "View Dhikr Benefits ▾" Button with authentic Hadith benefits.
 */
@Composable
fun DhikrTrackerScreen(
    onBack: () -> Unit = {},
    onNavigateToTasbih: (String) -> Unit = {}
) {
    val context = LocalContext.current

    // Active language and theme
    var selectedLang by remember { mutableStateOf(PrayerRepository.getLanguage()) }
    var isDarkTheme by remember { mutableStateOf(false) }
    var isMoreLangOpen by remember { mutableStateOf(false) }

    // Day offset for Overview & Today's Dhikr (0 = Today, 1 = Yesterday, 2 = 2 days ago, etc.)
    var dayOffset by remember { mutableStateOf(0) }

    // Quotes carousel active slide index (0, 1, 2)
    var activeQuoteIndex by remember { mutableStateOf(0) }
    var quoteTimerKey by remember { mutableStateOf(0) }

    // Chart period ("week", "month", "year")
    var chartPeriod by remember { mutableStateOf("week") }

    // Selected chart bar tooltip info
    var selectedBarInfo by remember { mutableStateOf<String?>(null) }

    // Benefits expanded accordion state
    var isBenefitsExpanded by remember { mutableStateOf(false) }

    // Toggle for View All dhikrs in Today's Dhikr section
    var showAllDhikrs by remember(dayOffset) { mutableStateOf(false) }

    // Reactive refresh trigger
    var refreshTrigger by remember { mutableStateOf(0) }

    val strings = remember(selectedLang) { DhikrTrackerLocalization.get(selectedLang) }

    // Languages for LanguageBar
    val visibleLanguages = listOf(
        LanguageItem("en", "English"),
        LanguageItem("hi", "हिंदी"),
        LanguageItem("ur", "اردو"),
        LanguageItem("ar", "العربية"),
        LanguageItem("bn", "বাংলা")
    )

    // Date computation for Today's Overview
    val targetDateCal = remember(dayOffset) {
        Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -dayOffset) }
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

    // Auto-advance quotes carousel every 8 seconds (resets timer when user manually changes quote)
    LaunchedEffect(activeQuoteIndex, quoteTimerKey) {
        delay(8000L)
        activeQuoteIndex = (activeQuoteIndex + 1) % 3
    }

    // Colors according to theme
    val bgMain = if (isDarkTheme) Color(0xFF071510) else Color(0xFFFAF7F2)
    val cardBg = if (isDarkTheme) Color(0xFF0A1C15) else Color(0xFFFFFFFF)
    val cardBorder = if (isDarkTheme) Color(0xFFC9A84C).copy(alpha = 0.25f) else Color(0xFFE2E8F0)
    val inkColor = if (isDarkTheme) Color(0xFFF0EAD6) else Color(0xFF14241E)
    val mutedColor = if (isDarkTheme) Color(0xFFA89F8B) else Color(0xFF64748B)
    val emeraldPrimary = Color(0xFF0E5D47)

    // Data metrics from DhikrRepository (100% dynamically linked with TasbihRepository)
    val summary = remember(dayOffset, refreshTrigger, selectedLang) {
        DhikrRepository.getSummaryForDate(targetDateCal, selectedLang)
    }

    val mostFrequentList = remember(refreshTrigger, selectedLang) {
        DhikrRepository.getMostFrequentDhikrs(selectedLang)
    }

    val bestTime = remember(refreshTrigger) {
        DhikrRepository.getBestTime()
    }

    val totalThisWeek = remember(refreshTrigger) {
        DhikrRepository.getTotalThisWeek()
    }

    val dailyAverage = remember(refreshTrigger) {
        DhikrRepository.getDailyAverage()
    }

    val chartData = remember(chartPeriod, refreshTrigger) {
        when (chartPeriod) {
            "month" -> DhikrRepository.getMonthlyChartData()
            "year" -> DhikrRepository.getYearlyChartData()
            else -> DhikrRepository.getWeeklyChartData()
        }
    }

    // Pre-load Interstitial Ad for seamless back exit
    LaunchedEffect(Unit) {
        AdMobInterstitialManager.loadAd(context)
    }

    val activity = context as? Activity
    val handleBackWithAd = {
        if (activity != null) {
            AdMobInterstitialManager.showAd(activity, "dhikr_tracker_back") {
                onBack()
            }
        } else {
            onBack()
        }
    }

    // Back handling
    BackHandler {
        if (isMoreLangOpen) {
            isMoreLangOpen = false
        } else {
            handleBackWithAd()
        }
    }

    Scaffold(
        topBar = {
            DhikrTrackerHeaderTopBar(
                title = strings.title.uppercase(),
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
        val scrollState = rememberScrollState()

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
                // 2. ISLAMIC QUOTE CAROUSEL CARD (Surah Ar-Ra'd / Tirmidhi / Al-Ankabut)
                DhikrQuoteCarouselCard(
                    quoteIndex = activeQuoteIndex,
                    strings = strings,
                    onSelectIndex = { index ->
                        activeQuoteIndex = index
                        quoteTimerKey++
                    }
                )

                // 3. TODAY'S OVERVIEW HEADER WITH DAY STEPPER & DATE
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left: Stepper [ < ] Title [ > ]
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Previous Day [ < ]
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(cardBg)
                                .border(1.dp, emeraldPrimary.copy(alpha = 0.35f), CircleShape)
                                .clickable {
                                    dayOffset++
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "‹",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = emeraldPrimary
                            )
                        }

                        val overviewTitleText = if (isViewingToday) {
                            strings.overview
                        } else if (dayOffset == 1) {
                            strings.yesterdayOverview
                        } else {
                            "$dateDisplayLabel ${strings.overview}"
                        }
                        Text(
                            text = overviewTitleText,
                            fontSize = 16.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = inkColor
                        )

                        // Next Day [ > ] (disabled if viewing today)
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(if (dayOffset > 0) cardBg else Color.Transparent)
                                .border(
                                    1.dp,
                                    if (dayOffset > 0) emeraldPrimary.copy(alpha = 0.35f) else Color.LightGray.copy(alpha = 0.3f),
                                    CircleShape
                                )
                                .clickable(enabled = dayOffset > 0) {
                                    dayOffset--
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "›",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (dayOffset > 0) emeraldPrimary else Color.LightGray
                            )
                        }
                    }

                    // Right: Gregorian / Hijri Date
                    Text(
                        text = "$dateDisplayLabel / $targetHijriStr",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF6B7280)
                    )
                }

                // 4. 4-STAT CARDS OVERVIEW GRID (2x2)
                DhikrStatGrid(
                    summary = summary,
                    strings = strings,
                    isDarkTheme = isDarkTheme,
                    cardBg = cardBg,
                    cardBorder = cardBorder,
                    inkColor = inkColor,
                    mutedColor = mutedColor
                )

                // 5. TODAY'S DHIKR SECTION WITH AUTO-SYNC SUBTITLE & VIEW ALL TOGGLE
                val dhikrSecTitleText = if (isViewingToday) {
                    strings.todayDhikr
                } else if (dayOffset == 1) {
                    strings.yesterdayDhikr
                } else {
                    "$dateDisplayLabel ${strings.todayDhikr}"
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Left Stepper [ < ]
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(cardBg)
                                .border(1.dp, emeraldPrimary.copy(alpha = 0.35f), CircleShape)
                                .clickable {
                                    dayOffset++
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "‹",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = emeraldPrimary
                            )
                        }

                        Text(
                            text = dhikrSecTitleText,
                            fontSize = 16.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = inkColor
                        )

                        // Right Stepper [ > ]
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(if (dayOffset > 0) cardBg else Color.Transparent)
                            .border(
                                1.dp,
                                if (dayOffset > 0) emeraldPrimary.copy(alpha = 0.35f) else Color.LightGray.copy(alpha = 0.3f),
                                CircleShape
                            )
                            .clickable(enabled = dayOffset > 0) {
                                dayOffset--
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "›",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (dayOffset > 0) emeraldPrimary else Color.LightGray
                        )
                    }
                }

                // View All / Show Less button matching original index.html lines 445496
                if (summary.records.size > 4) {
                    Text(
                        text = if (showAllDhikrs) "${strings.showLess} ▴" else "${strings.viewAll} (${summary.records.size}) ›",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = emeraldPrimary,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { showAllDhikrs = !showAllDhikrs }
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }

                // Auto-sync note
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.padding(start = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF38BDF8)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🔄", fontSize = 9.sp)
                    }
                    Text(
                        text = strings.autoSync,
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280),
                        fontWeight = FontWeight.Medium
                    )
                }

                // Dhikr List Cards / Authentic Empty State matching original index.html lines 443984-444008
                if (summary.records.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(cardBg)
                            .border(1.2.dp, cardBorder, RoundedCornerShape(16.dp))
                            .padding(vertical = 26.dp, horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "📿", fontSize = 28.sp)
                            Text(
                                text = strings.noDhikrToday.replace("<br>", "\n").replace(Regex("<[^>]*>"), ""),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = mutedColor,
                                textAlign = TextAlign.Center,
                                lineHeight = 19.sp
                            )
                        }
                    }
                } else {
                    val recordsToDisplay = if (showAllDhikrs) summary.records else summary.records.take(4)
                    recordsToDisplay.forEachIndexed { index, record ->
                        DhikrItemCard(
                            index = index + 1,
                            record = record,
                            strings = strings,
                            cardBg = cardBg,
                            cardBorder = cardBorder,
                            inkColor = inkColor,
                            mutedColor = mutedColor,
                            onItemClick = {
                                onNavigateToTasbih(record.item.id)
                            }
                        )
                    }
                }

                // 6. REAL ADMOB NATIVE AD SLOT (Zero-gap collapse)
                AdMobNativeAdCard(
                    modifier = Modifier.fillMaxWidth()
                )

                // 7. WEEKLY / MONTHLY / YEARLY PROGRESS CARD
                DhikrProgressCard(
                    period = chartPeriod,
                    chartData = chartData,
                    strings = strings,
                    selectedBarInfo = selectedBarInfo,
                    onSelectPeriod = {
                        chartPeriod = it
                        selectedBarInfo = null
                    },
                    onSelectBar = { selectedBarInfo = it },
                    cardBg = cardBg,
                    cardBorder = cardBorder,
                    inkColor = inkColor,
                    mutedColor = mutedColor
                )

                // 8. MOST FREQUENT DHIKR CARD
                MostFrequentDhikrCard(
                    items = mostFrequentList,
                    strings = strings,
                    cardBg = cardBg,
                    cardBorder = cardBorder,
                    inkColor = inkColor,
                    mutedColor = mutedColor,
                    onItemClick = { dhikrId ->
                        onNavigateToTasbih(dhikrId)
                    }
                )

                // 9. DHIKR INSIGHTS CARD (Best Time, Total This Week, Daily Average)
                DhikrInsightsCard(
                    bestTime = bestTime,
                    totalThisWeek = totalThisWeek,
                    dailyAverage = dailyAverage,
                    strings = strings,
                    cardBg = cardBg,
                    cardBorder = cardBorder,
                    inkColor = inkColor,
                    mutedColor = mutedColor
                )

                // 10. MOTIVATIONAL PILL CARD
                MotivationalCard(
                    strings = strings,
                    cardBg = cardBg,
                    cardBorder = cardBorder,
                    inkColor = inkColor,
                    mutedColor = mutedColor
                )

                // 11. EXPANDABLE "VIEW DHIKR BENEFITS" BUTTON & ACCORDION
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(14.dp))
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFF0E5D47),
                                    Color(0xFF0A4434)
                                )
                            )
                        )
                        .border(1.dp, Color(0xFFC9A84C).copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                        .clickable {
                            isBenefitsExpanded = !isBenefitsExpanded
                        }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "📖", fontSize = 16.sp)
                        Text(
                            text = strings.viewBenefits + if (isBenefitsExpanded) " ▴" else " ▾",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF3D48A)
                        )
                    }
                }

                // Expandable Benefits Content
                AnimatedVisibility(
                    visible = isBenefitsExpanded,
                    enter = fadeIn(tween(250)) + expandVertically(tween(250)),
                    exit = fadeOut(tween(200)) + shrinkVertically(tween(200))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(cardBg)
                            .border(1.2.dp, cardBorder, RoundedCornerShape(16.dp))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        DhikrBenefitRow(
                            icon = "❤️",
                            text = strings.benefit1,
                            reference = strings.benefit1Ref,
                            inkColor = inkColor,
                            mutedColor = mutedColor
                        )
                        HorizontalDivider(color = cardBorder)
                        DhikrBenefitRow(
                            icon = "⭐",
                            text = strings.benefit2,
                            reference = strings.benefit2Ref,
                            inkColor = inkColor,
                            mutedColor = mutedColor
                        )
                        HorizontalDivider(color = cardBorder)
                        DhikrBenefitRow(
                            icon = "🌴",
                            text = strings.benefit3,
                            reference = strings.benefit3Ref,
                            inkColor = inkColor,
                            mutedColor = mutedColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // Full 28+ Languages Modal (Triggered by 🌐 +20)
        if (isMoreLangOpen) {
            MoreLanguagesModal(
                selectedCode = selectedLang,
                onSelectLanguage = { code ->
                    selectedLang = code
                    PrayerRepository.saveLanguage(code)
                },
                onDismiss = {
                    isMoreLangOpen = false
                }
            )
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  1. TOPBAR HEADER WITH MOSQUE ARTWORK & ACTION BUTTONS
// ══════════════════════════════════════════════════════════════
@Composable
fun DhikrTrackerHeaderTopBar(
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
        // Mosque silhouette artwork decoration matching original st-hdr-deco
        val painter = rememberAssetPainter("media/6d88c4d6a7d5af94.webp")
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.CenterEnd,
            modifier = Modifier.matchParentSize()
        )

        // Horizontal gradient fade matching original .st-hdr-deco-fade
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFF0C4A3A),
                            Color(0xFF0C4A3A).copy(alpha = 0.55f),
                            Color.Transparent
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
            // Circular Back Button [←]
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
                    text = "←",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Screen Title
            Text(
                text = title,
                fontFamily = FontFamily.Serif,
                fontSize = 16.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )

            // Sun/Theme ☀️ Button
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
//  2. 3-SLIDE ISLAMIC QUOTE CAROUSEL CARD
// ══════════════════════════════════════════════════════════════
@Composable
fun DhikrQuoteCarouselCard(
    quoteIndex: Int,
    strings: DhikrTrackerStrings,
    onSelectIndex: (Int) -> Unit
) {
    val quotes = listOf(
        DhikrQuote(
            arabic = "أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ",
            text = strings.q1Txt,
            reference = strings.q1Ref,
            icon = "🕌"
        ),
        DhikrQuote(
            arabic = "مَنْ قَالَ: سُبْحَانَ اللَّهِ الْعَظِيمِ وَبِحَمْدِهِ، غُرِسَتْ لَهُ نَخْلَةٌ فِي الْجَنَّةِ",
            text = strings.q2Txt,
            reference = strings.q2Ref,
            icon = "🌴"
        ),
        DhikrQuote(
            arabic = "وَلَذِكْرُ اللَّهِ أَكْبَرُ",
            text = strings.q3Txt,
            reference = strings.q3Ref,
            icon = "✨"
        )
    )

    val currentQuote = quotes[quoteIndex.coerceIn(0, 2)]

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFF0E4334),
                        Color(0xFF092E24),
                        Color(0xFF041913)
                    )
                )
            )
            .border(
                width = 1.2.dp,
                color = Color(0xFFC9A84C).copy(alpha = 0.5f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Top Row: Dome Icon Circle on Left + Arabic Script on Right
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Circular Dome Icon Badge
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF145443).copy(alpha = 0.6f))
                        .border(1.dp, Color(0xFFE8C878).copy(alpha = 0.6f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = currentQuote.icon, fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Arabic Script
                Text(
                    text = currentQuote.arabic,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF3D48A),
                    fontFamily = FontFamily.Serif,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // English / Translated Quote Text
            Text(
                text = currentQuote.text,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Serif,
                color = Color.White,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Reference
            Text(
                text = currentQuote.reference,
                fontSize = 11.sp,
                color = Color(0xFFF3D48A),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 3 Indicator Dots with generous 38dp touch targets
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0..2) {
                    val isActive = (i == quoteIndex)
                    Box(
                        modifier = Modifier
                            .size(width = 38.dp, height = 36.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onSelectIndex(i) },
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .height(5.dp)
                                .width(if (isActive) 18.dp else 5.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(
                                    if (isActive) Color(0xFFF3D48A) else Color.White.copy(alpha = 0.35f)
                                )
                        )
                    }
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  ORIGINAL VECTOR ICONS (CLOCK & TRENDING UP)
// ══════════════════════════════════════════════════════════════
@Composable
fun ClockVectorIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF0E5D47)
) {
    Canvas(modifier = modifier) {
        val strokeWidth = 2.dp.toPx()
        val r = (size.minDimension / 2f) - (strokeWidth / 2f)
        val center = Offset(size.width / 2f, size.height / 2f)
        drawCircle(
            color = color,
            radius = r,
            center = center,
            style = Stroke(width = strokeWidth)
        )
        // Hour hand
        drawLine(
            color = color,
            start = center,
            end = Offset(center.x, center.y - r * 0.55f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
        // Minute hand
        drawLine(
            color = color,
            start = center,
            end = Offset(center.x + r * 0.48f, center.y + r * 0.22f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun TrendingUpVectorIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF0E5D47)
) {
    Canvas(modifier = modifier) {
        val strokeW = 2.dp.toPx()
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(w * 0.12f, h * 0.68f)
            lineTo(w * 0.40f, h * 0.40f)
            lineTo(w * 0.58f, h * 0.58f)
            lineTo(w * 0.88f, h * 0.24f)
        }
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
        val arrow = Path().apply {
            moveTo(w * 0.58f, h * 0.24f)
            lineTo(w * 0.88f, h * 0.24f)
            lineTo(w * 0.88f, h * 0.54f)
        }
        drawPath(
            path = arrow,
            color = color,
            style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}

// ══════════════════════════════════════════════════════════════
//  3. 4-STAT CARDS OVERVIEW GRID (2x2) - 100% UNIFORM SIZE
// ══════════════════════════════════════════════════════════════
@Composable
fun DhikrStatGrid(
    summary: DhikrDaySummary,
    strings: DhikrTrackerStrings,
    isDarkTheme: Boolean,
    cardBg: Color,
    cardBorder: Color,
    inkColor: Color,
    mutedColor: Color
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Row 1: Total Dhikr (times) | Dhikrs Recited (types)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Card 1: Total Dhikr
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(138.dp)
                    .shadow(4.dp, RoundedCornerShape(18.dp), ambientColor = Color(0x140E5D47), spotColor = Color(0x140E5D47))
                    .clip(RoundedCornerShape(18.dp))
                    .background(cardBg)
                    .border(1.2.dp, cardBorder, RoundedCornerShape(18.dp))
                    .padding(vertical = 12.dp, horizontal = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Clock Circle
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE8F5EE))
                            .border(1.dp, Color(0xFF0E5D47).copy(alpha = 0.22f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        ClockVectorIcon(modifier = Modifier.size(20.dp), color = Color(0xFF0E5D47))
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Count
                    Text(
                        text = summary.totalCount.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Serif,
                        color = Color(0xFF0E5D47)
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    // Label
                    Text(
                        text = strings.totalDhikrToday,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = mutedColor
                    )

                    // Sublabel (times)
                    Text(
                        text = strings.times,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0E5D47)
                    )
                }
            }

            // Card 2: Dhikrs Recited
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(138.dp)
                    .shadow(4.dp, RoundedCornerShape(18.dp), ambientColor = Color(0x140E5D47), spotColor = Color(0x140E5D47))
                    .clip(RoundedCornerShape(18.dp))
                    .background(cardBg)
                    .border(1.2.dp, cardBorder, RoundedCornerShape(18.dp))
                    .padding(vertical = 12.dp, horizontal = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Checkmark Circle (Solid Dark Teal with White checkmark)
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF12664C), Color(0xFF0C4A3A))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✓",
                            fontSize = 18.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Count
                    Text(
                        text = summary.activeTypesCount.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Serif,
                        color = Color(0xFF0E5D47)
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    // Label
                    Text(
                        text = strings.dhikrsRecited,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = mutedColor
                    )

                    // Sublabel (types)
                    Text(
                        text = strings.types,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0E5D47)
                    )
                }
            }
        }

        // Row 2: Current Streak (days) | Improvement (vs yesterday)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Card 3: Current Streak
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(138.dp)
                    .shadow(4.dp, RoundedCornerShape(18.dp), ambientColor = Color(0x140E5D47), spotColor = Color(0x140E5D47))
                    .clip(RoundedCornerShape(18.dp))
                    .background(cardBg)
                    .border(1.2.dp, cardBorder, RoundedCornerShape(18.dp))
                    .padding(vertical = 12.dp, horizontal = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Flame Circle
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFEDD5))
                            .border(1.dp, Color(0xFFEA580C).copy(alpha = 0.25f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🔥", fontSize = 17.sp)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Count
                    Text(
                        text = summary.streakDays.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Serif,
                        color = Color(0xFFD97706)
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    // Label
                    Text(
                        text = "${strings.streak} (${strings.days})",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = mutedColor
                    )

                    // Sublabel spacer to make Card 3 height match Card 4 exactly
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }

            // Card 4: Improvement
            val deltaStr = if (summary.deltaVsYesterday >= 0) "+${summary.deltaVsYesterday}" else "${summary.deltaVsYesterday}"
            val deltaColor = if (summary.deltaVsYesterday >= 0) Color(0xFF0E5D47) else Color(0xFFD1495B)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(138.dp)
                    .shadow(4.dp, RoundedCornerShape(18.dp), ambientColor = Color(0x140E5D47), spotColor = Color(0x140E5D47))
                    .clip(RoundedCornerShape(18.dp))
                    .background(cardBg)
                    .border(1.2.dp, cardBorder, RoundedCornerShape(18.dp))
                    .padding(vertical = 12.dp, horizontal = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Trending Up Circle
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE8F5EE))
                            .border(1.dp, Color(0xFF0E5D47).copy(alpha = 0.22f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        TrendingUpVectorIcon(
                            modifier = Modifier.size(20.dp),
                            color = deltaColor
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Delta
                    Text(
                        text = deltaStr,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Serif,
                        color = deltaColor
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    // Label
                    Text(
                        text = strings.improve,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = mutedColor
                    )

                    // Sublabel (vs yesterday)
                    Text(
                        text = strings.vsYesterday,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0E5D47)
                    )
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  4. MOROCCAN 8-POINT STAR ROSETTE & DHIKR ITEM CARD
// ══════════════════════════════════════════════════════════════
@Composable
fun MoroccanRosetteBadge(
    number: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(46.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val s = size.minDimension
            val rectSize = Size(s * 0.72f, s * 0.72f)
            val cornerRad = CornerRadius(4.dp.toPx(), 4.dp.toPx())
            val topLeft = Offset((s - rectSize.width) / 2f, (s - rectSize.height) / 2f)

            val fillBrush = Brush.linearGradient(
                listOf(Color(0xFF0E5D47), Color(0xFF062C22))
            )
            val goldColor = Color(0xFFC9A84C)

            // Square 1: Normal
            drawRoundRect(
                brush = fillBrush,
                topLeft = topLeft,
                size = rectSize,
                cornerRadius = cornerRad
            )
            drawRoundRect(
                color = goldColor,
                topLeft = topLeft,
                size = rectSize,
                cornerRadius = cornerRad,
                style = Stroke(width = 1.8.dp.toPx())
            )

            // Square 2: Rotated 45 degrees
            rotate(45f) {
                drawRoundRect(
                    brush = fillBrush,
                    topLeft = topLeft,
                    size = rectSize,
                    cornerRadius = cornerRad
                )
                drawRoundRect(
                    color = goldColor,
                    topLeft = topLeft,
                    size = rectSize,
                    cornerRadius = cornerRad,
                    style = Stroke(width = 1.8.dp.toPx())
                )
            }
        }

        // Rosette Number
        Text(
            text = number,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            color = Color(0xFFF3D48A)
        )
    }
}

@Composable
fun DhikrItemCard(
    index: Int,
    record: DhikrCountRecord,
    strings: DhikrTrackerStrings,
    cardBg: Color,
    cardBorder: Color,
    inkColor: Color,
    mutedColor: Color,
    onItemClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cardBg)
            .border(1.2.dp, cardBorder, RoundedCornerShape(16.dp))
            .clickable { onItemClick() }
            .padding(horizontal = 14.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Moroccan 8-point gold rosette
            val formattedIndex = String.format(Locale.US, "%02d", index)
            MoroccanRosetteBadge(number = formattedIndex)

            Spacer(modifier = Modifier.width(12.dp))

            // Center: Name & Meaning
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = record.item.name,
                    fontSize = 15.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = inkColor
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = record.item.meaning,
                    fontSize = 12.sp,
                    color = mutedColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Arabic Script (AmiriQuranFont Calligraphy)
            Text(
                text = record.item.arabic,
                fontSize = 17.5.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = AmiriQuranFont,
                color = Color(0xFF0E5D47)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Right: Count Badge Pill [Count / times] - Pixel-perfect match to Image 4 & 5
            Box(
                modifier = Modifier
                    .defaultMinSize(minWidth = 60.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFFEAF3EE),
                                Color(0xFFE2EBE5)
                            )
                        )
                    )
                    .border(1.3.dp, Color(0xFF0E5D47).copy(alpha = 0.75f), RoundedCornerShape(14.dp))
                    .padding(horizontal = 10.dp, vertical = 7.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = record.count.toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        color = Color(0xFF0E5D47),
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(
                        text = strings.times,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A5D52),
                        lineHeight = 12.sp
                    )
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  5. PROGRESS CARD (WEEK / MONTH / YEAR) WITH BAR CHART
// ══════════════════════════════════════════════════════════════
@Composable
fun DhikrProgressCard(
    period: String,
    chartData: List<ChartBarData>,
    strings: DhikrTrackerStrings,
    selectedBarInfo: String?,
    onSelectPeriod: (String) -> Unit,
    onSelectBar: (String) -> Unit,
    cardBg: Color,
    cardBorder: Color,
    inkColor: Color,
    mutedColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(cardBg)
            .border(1.2.dp, cardBorder, RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Header Row: Title & Toggle Tabs [Week] [Month] [Year]
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val title = when (period) {
                    "month" -> strings.monthlyProgress
                    "year" -> strings.yearlyProgress
                    else -> strings.weeklyProgress
                }
                Text(
                    text = title,
                    fontSize = 15.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = inkColor
                )

                // 3-Pill Toggle Bar
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFF3F4F6))
                        .padding(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    listOf(
                        "week" to strings.week,
                        "month" to strings.month,
                        "year" to strings.year
                    ).forEach { (pKey, pLabel) ->
                        val isSelected = (period == pKey)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) Color(0xFF0E5D47) else Color.Transparent)
                                .clickable { onSelectPeriod(pKey) }
                                .padding(horizontal = 12.dp, vertical = 5.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = pLabel,
                                fontSize = 11.5.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else Color(0xFF4B5563)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tooltip if bar clicked
            if (selectedBarInfo != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFEAF5EE))
                        .border(1.dp, Color(0xFF0E5D47).copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = selectedBarInfo,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0E5D47)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Dynamic Max Ceiling calculation matching original index.html lines 444752-444808
            val rawMax = chartData.maxOfOrNull { it.count } ?: 0
            val chartMax = when {
                rawMax <= 0 -> 100
                rawMax <= 100 -> 100
                rawMax <= 500 -> (Math.ceil(rawMax / 100.0) * 100).toInt()
                rawMax <= 2000 -> (Math.ceil(rawMax / 250.0) * 250).toInt()
                else -> (Math.ceil(rawMax / 1000.0) * 1000).toInt()
            }
            val yAxisLabels = listOf(
                chartMax.toString(),
                (chartMax * 3 / 4).toString(),
                (chartMax / 2).toString(),
                (chartMax / 4).toString(),
                "0"
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
            ) {
                // Y-Axis Labels
                Column(
                    modifier = Modifier
                        .width(32.dp)
                        .fillMaxHeight()
                        .padding(bottom = 22.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.End
                ) {
                    yAxisLabels.forEach { label ->
                        Text(
                            text = label,
                            fontSize = 9.5.sp,
                            color = mutedColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Chart Columns Container - Clean background without grid lines (matching Image 4 & 5)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    // Bar Columns
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        val barWidth = if (period == "month") 6.dp else if (period == "year") 14.dp else 10.dp

                        chartData.forEach { bar ->
                            val heightFraction = if (chartMax > 0) {
                                (bar.count.toFloat() / chartMax.toFloat()).coerceIn(0f, 1f)
                            } else 0f

                            val animHeight by animateFloatAsState(
                                targetValue = heightFraction,
                                animationSpec = tween(500),
                                label = "barHeight"
                            )

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clickable {
                                        onSelectBar("${bar.fullLabel}: ${bar.count} ${strings.times}")
                                    }
                            ) {
                                // Bar pillar
                                Box(
                                    modifier = Modifier
                                        .width(barWidth)
                                        .fillMaxHeight(0.82f),
                                    contentAlignment = Alignment.BottomCenter
                                ) {
                                    if (bar.isFuture) {
                                        // Future day: soft warm beige/cream rounded stub
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(12.dp)
                                                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                                .background(Color(0xFFEDE9E0))
                                        )
                                    } else if (bar.count > 0) {
                                        // Active Bar: Deep pine green gradient
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .fillMaxHeight(animHeight.coerceAtLeast(0.08f))
                                                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                                .background(
                                                    Brush.verticalGradient(
                                                        listOf(
                                                            Color(0xFF0E5D47),
                                                            Color(0xFF136E54)
                                                        )
                                                    )
                                                )
                                        )
                                    } else {
                                        // 0 count past day: small clean green circular dot (matching Image 4 & 5)
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFF0E5D47))
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                // X-Axis Label
                                Text(
                                    text = bar.label,
                                    fontSize = if (period == "month") 7.5.sp else 10.sp,
                                    color = mutedColor,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  6. MOST FREQUENT DHIKR CARD
// ══════════════════════════════════════════════════════════════
@Composable
fun MostFrequentDhikrCard(
    items: List<FrequentDhikrItem>,
    strings: DhikrTrackerStrings,
    cardBg: Color,
    cardBorder: Color,
    inkColor: Color,
    mutedColor: Color,
    onItemClick: (String) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(cardBg)
            .border(1.2.dp, cardBorder, RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = strings.mostFrequent,
                fontSize = 15.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = inkColor
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (items.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = strings.noFrequentData,
                        fontSize = 12.5.sp,
                        color = mutedColor,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                items.forEachIndexed { idx, item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onItemClick(item.id) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Circular dark teal icon with symbol
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF0E5D47)),
                                contentAlignment = Alignment.Center
                            ) {
                                val iconSymbol = when (item.iconType) {
                                    "sun" -> "☀️"
                                    "moon" -> "🌙"
                                    else -> "✨"
                                }
                                Text(text = iconSymbol, fontSize = 14.sp)
                            }

                            Text(
                                text = item.name,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = inkColor
                            )
                        }

                        // Count
                        Text(
                            text = "${item.count} ${strings.times}",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF0E5D47)
                        )
                    }

                    if (idx < items.size - 1) {
                        HorizontalDivider(color = cardBorder, thickness = 0.8.dp)
                    }
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  7. DHIKR INSIGHTS CARD (Best Time, Total Week, Daily Avg)
// ══════════════════════════════════════════════════════════════
@Composable
fun DhikrInsightsCard(
    bestTime: String,
    totalThisWeek: Int,
    dailyAverage: Int,
    strings: DhikrTrackerStrings,
    cardBg: Color,
    cardBorder: Color,
    inkColor: Color,
    mutedColor: Color
) {
    val localizedBestTime = when (bestTime) {
        "Fajr" -> strings.fajr
        "Dhuhr" -> strings.dhuhr
        "Asr" -> strings.asr
        "Maghrib" -> strings.maghrib
        "Isha" -> strings.isha
        "Night" -> strings.night
        else -> "—"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(cardBg)
            .border(1.2.dp, cardBorder, RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = strings.insights,
                fontSize = 15.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = inkColor
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Row 1: Best Time
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
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFEAF5EE))
                            .border(1.dp, Color(0xFF0E5D47).copy(alpha = 0.25f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "★", fontSize = 16.sp, color = Color(0xFFC9A84C))
                    }

                    Text(
                        text = strings.bestTime,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = inkColor
                    )
                }

                // Best Time Pill Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFE0F2FE))
                        .border(1.dp, Color(0xFF0E5D47).copy(alpha = 0.35f), RoundedCornerShape(14.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = localizedBestTime,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0E5D47)
                    )
                }
            }

            HorizontalDivider(color = cardBorder, thickness = 0.8.dp)

            // Row 2: Total This Week
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
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFEAF5EE))
                            .border(1.dp, Color(0xFF0E5D47).copy(alpha = 0.25f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        ClockVectorIcon(modifier = Modifier.size(18.dp), color = Color(0xFF0E5D47))
                    }

                    Text(
                        text = strings.totalWeek,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = inkColor
                    )
                }

                Text(
                    text = "$totalThisWeek ${strings.times}",
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0E5D47)
                )
            }

            HorizontalDivider(color = cardBorder, thickness = 0.8.dp)

            // Row 3: Daily Average
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
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFEAF5EE))
                            .border(1.dp, Color(0xFF0E5D47).copy(alpha = 0.25f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        TrendingUpVectorIcon(modifier = Modifier.size(18.dp), color = Color(0xFF0E5D47))
                    }

                    Text(
                        text = strings.dailyAvg,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = inkColor
                    )
                }

                Text(
                    text = "$dailyAverage ${strings.times}",
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0E5D47)
                )
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  8. MOTIVATIONAL PILL CARD
// ══════════════════════════════════════════════════════════════
@Composable
fun MotivationalCard(
    strings: DhikrTrackerStrings,
    cardBg: Color,
    cardBorder: Color,
    inkColor: Color,
    mutedColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cardBg)
            .border(1.2.dp, cardBorder, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Soft Pink/Red Heart Circle
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFDE8E8)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "❤️", fontSize = 16.sp)
            }

            Column {
                Text(
                    text = strings.keepTongue,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = inkColor
                )
                Text(
                    text = strings.heartAlive,
                    fontSize = 11.5.sp,
                    color = mutedColor,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  9. DHIKR BENEFIT ROW ACCORDION ITEM
// ══════════════════════════════════════════════════════════════
@Composable
fun DhikrBenefitRow(
    icon: String,
    text: String,
    reference: String,
    inkColor: Color,
    mutedColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(text = icon, fontSize = 18.sp)
        Column {
            Text(
                text = text,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = inkColor,
                lineHeight = 19.sp
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = "📖 $reference",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF0E5D47)
            )
        }
    }
}
