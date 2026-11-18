package com.tasbih.digitaltasbih.ui.screens.ramadan

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import com.tasbih.digitaltasbih.ui.components.RamadanDecorationGarland
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.data.LanguageItem
import com.tasbih.digitaltasbih.data.PrayerDayData
import com.tasbih.digitaltasbih.data.PrayerRepository
import com.tasbih.digitaltasbih.data.ramadan.RamadanData
import com.tasbih.digitaltasbih.data.ramadan.RamadanDayPlan
import com.tasbih.digitaltasbih.data.ramadan.RamadanLocalization
import com.tasbih.digitaltasbih.data.ramadan.RamadanRepository
import com.tasbih.digitaltasbih.data.ramadan.RamadanStringsData
import com.tasbih.digitaltasbih.ui.components.LanguageBar
import com.tasbih.digitaltasbih.ui.components.MoreLanguagesModal
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.util.Calendar
import kotlin.random.Random

@Composable
fun RamadanHomeScreen(
    onBack: () -> Unit,
    onOpenQuranTracker: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    var isDarkTheme by remember { mutableStateOf(PrayerRepository.isDarkTheme()) }
    var selectedLanguage by remember { mutableStateOf(PrayerRepository.getLanguage()) }
    var isMoreLangOpen by remember { mutableStateOf(false) }

    val strings = remember(selectedLanguage) { RamadanLocalization.get(selectedLanguage) }

    // Modals
    var isDuaModalOpen by remember { mutableStateOf(false) }
    var initialDuaTab by remember { mutableIntStateOf(0) }
    var isZakatModalOpen by remember { mutableStateOf(false) }
    var isQuranModalOpen by remember { mutableStateOf(false) }
    var isFiqhModalOpen by remember { mutableStateOf(false) }
    var isLeaderboardModalOpen by remember { mutableStateOf(false) }

    // Selected Ashra tab (0: All 30, 1: Ashra 1, 2: Ashra 2, 3: Ashra 3)
    var selectedAshraFilter by remember { mutableIntStateOf(0) }

    // Repository trigger for reactive updates
    val trigger = RamadanRepository.updateTrigger.value
    val currentDay = remember(trigger) { RamadanRepository.getCurrentRamadanDay(context) }

    // Today's prayer times for live Sehri & Iftar countdown
    var todayData by remember { mutableStateOf<PrayerDayData?>(null) }
    LaunchedEffect(Unit) {
        todayData = PrayerRepository.fetchTodayPrayerTimes()
    }

    // Live 1-second countdown ticker
    var currentTimeMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (isActive) {
            delay(1000L)
            currentTimeMillis = System.currentTimeMillis()
        }
    }

    // Water tracker state
    var waterGlasses by remember(trigger) {
        mutableIntStateOf(RamadanRepository.getWaterGlassesToday(context))
    }

    // Ashra Dhikr Counter State
    var ashraDhikrCount by remember { mutableIntStateOf(0) }

    // Background colors
    val bgColor = if (isDarkTheme) Color(0xFF06140E) else Color(0xFFF6F4EE)
    val cardBg = if (isDarkTheme) Color(0xFF092017) else Color(0xFFFFFFFF)
    val goldAccent = Color(0xFFD4AF37)
    val emeraldPrimary = Color(0xFF0E5D47)

    val visibleLangs = listOf(
        LanguageItem("en", "English"),
        LanguageItem("hi", "हिंदी"),
        LanguageItem("ur", "اردو"),
        LanguageItem("ar", "العربية"),
        LanguageItem("bn", "বাংলা")
    )

    Scaffold(
        containerColor = bgColor,
        topBar = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(bgColor)
                ) {
                    // Floating Ornate Header Card matching app's standard luxury styling
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .shadow(elevation = 8.dp, shape = RoundedCornerShape(18.dp))
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF146450), Color(0xFF0C4A3A), Color(0xFF062018))
                                )
                            )
                            .border(1.5.dp, Color(0x8CE8C878), RoundedCornerShape(18.dp))
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Back button
                            IconButton(
                                onClick = onBack,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.15f))
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            // Title with Crescent & Hijri subtitle
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = strings.title,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Serif,
                                    letterSpacing = 1.2.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = strings.subtitle,
                                    fontSize = 11.sp,
                                    color = goldAccent,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Day / Night Theme Button
                            IconButton(
                                onClick = {
                                    isDarkTheme = !isDarkTheme
                                    PrayerRepository.setDarkTheme(isDarkTheme)
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.15f))
                            ) {
                                Text(
                                    text = if (isDarkTheme) "🌙" else "☀️",
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }

                    // Language Bar
                    LanguageBar(
                        languages = visibleLangs,
                        selectedCode = selectedLanguage,
                        isDarkTheme = isDarkTheme,
                        onSelectLanguage = { code ->
                            selectedLanguage = code
                            PrayerRepository.saveLanguage(code)
                        },
                        onOpenMoreLanguages = {
                            isMoreLangOpen = true
                        }
                    )
                }

                // RAMADAN FESTIVE HANGING LIGHT GARLAND ("Light ki Ladi")
                RamadanDecorationGarland(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .align(Alignment.TopCenter),
                    isDarkTheme = isDarkTheme
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Subtle Twinkling Golden Stars Canvas in Dark Mode
            if (isDarkTheme) {
                TwinklingStarsBackground()
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // ── 1. HERO SEHRI & IFTAR LIVE COUNTDOWN CARD (PIXEL-PERFECT LUXURY 3D DECK) ──
                RamadanHeroClockCard(
                    todayData = todayData,
                    currentTimeMillis = currentTimeMillis,
                    isDarkTheme = isDarkTheme,
                    language = selectedLanguage,
                    currentDay = currentDay,
                    onOpenDua = { tabIndex ->
                        initialDuaTab = tabIndex
                        isDuaModalOpen = true
                    }
                )

                // ── 2. QUICK FEATURES GRID (Tools Hub) ──
                RamadanQuickFeaturesGrid(
                    strings = strings,
                    isDarkTheme = isDarkTheme,
                    onOpenZakat = { isZakatModalOpen = true },
                    onOpenQuran = { if (onOpenQuranTracker != null) onOpenQuranTracker() else isQuranModalOpen = true },
                    onOpenFiqh = { isFiqhModalOpen = true },
                    onOpenLeaderboard = { isLeaderboardModalOpen = true },
                    onOpenDuas = {
                        initialDuaTab = 2
                        isDuaModalOpen = true
                    }
                )

                // ── 3. TODAY'S IBADAH CHECKLIST (Deeds Tracker) ──
                TodayDeedsTrackerCard(
                    context = context,
                    strings = strings,
                    isDarkTheme = isDarkTheme,
                    trigger = trigger
                )

                // ── 4. CITY DHIKR CHALLENGE BANNER (Rajnandgaon & Area) ──
                CityChallengeHighlightBanner(
                    context = context,
                    strings = strings,
                    isDarkTheme = isDarkTheme,
                    onOpenLeaderboard = { isLeaderboardModalOpen = true }
                )

                // ── 5. WATER INTAKE TRACKER (Iftar to Sehri) ──
                WaterIntakeCard(
                    strings = strings,
                    glasses = waterGlasses,
                    isDarkTheme = isDarkTheme,
                    onIncrement = {
                        val next = (waterGlasses + 1).coerceAtMost(12)
                        RamadanRepository.setWaterGlassesToday(context, next)
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    },
                    onDecrement = {
                        val prev = (waterGlasses - 1).coerceAtLeast(0)
                        RamadanRepository.setWaterGlassesToday(context, prev)
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                )

                // ── 6. SPECIAL ASHRA DHIKR COUNTER ──
                AshraDhikrCounterCard(
                    strings = strings,
                    count = ashraDhikrCount,
                    isDarkTheme = isDarkTheme,
                    onIncrement = {
                        ashraDhikrCount++
                        RamadanRepository.incrementUserRamadanDhikr(context, 1L)
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    },
                    onReset = { ashraDhikrCount = 0 }
                )

                // ── 7. 30-DAY RAMADAN JOURNEY (Roadmap) ──
                DaysJourneySection(
                    strings = strings,
                    language = selectedLanguage,
                    selectedFilter = selectedAshraFilter,
                    onSelectFilter = { selectedAshraFilter = it },
                    isDarkTheme = isDarkTheme
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // ── MODALS ──
    if (isDuaModalOpen) {
        SehriIftarDuaModal(
            onDismiss = { isDuaModalOpen = false },
            isDarkTheme = isDarkTheme,
            initialTab = initialDuaTab,
            language = selectedLanguage
        )
    }

    if (isZakatModalOpen) {
        ZakatCalculatorModal(
            onDismiss = { isZakatModalOpen = false },
            isDarkTheme = isDarkTheme
        )
    }

    if (isQuranModalOpen) {
        QuranTrackerScreen(
            onBack = { isQuranModalOpen = false }
        )
    }

    if (isFiqhModalOpen) {
        RamadanFiqhModal(
            onDismiss = { isFiqhModalOpen = false },
            isDarkTheme = isDarkTheme
        )
    }

    if (isLeaderboardModalOpen) {
        CityLeaderboardModal(
            onDismiss = { isLeaderboardModalOpen = false },
            isDarkTheme = isDarkTheme
        )
    }

    if (isMoreLangOpen) {
        MoreLanguagesModal(
            selectedCode = selectedLanguage,
            onDismiss = { isMoreLangOpen = false },
            onSelectLanguage = { code ->
                selectedLanguage = code
                PrayerRepository.saveLanguage(code)
                isMoreLangOpen = false
            }
        )
    }
}

// ─────────────────────────────────────────────────────────────
// RAMADAN QUICK FEATURES GRID (Tools)
// ─────────────────────────────────────────────────────────────
@Composable
private fun RamadanQuickFeaturesGrid(
    strings: RamadanStringsData,
    isDarkTheme: Boolean,
    onOpenZakat: () -> Unit,
    onOpenQuran: () -> Unit,
    onOpenFiqh: () -> Unit,
    onOpenLeaderboard: () -> Unit,
    onOpenDuas: () -> Unit
) {
    data class ToolCardData(val icon: String, val title: String, val sub: String, val action: () -> Unit)
    val items = listOf(
        ToolCardData("💰", strings.zakatTitle, strings.zakatSubtitle, onOpenZakat),
        ToolCardData("📖", strings.quranTitle, strings.quranSubtitle, onOpenQuran),
        ToolCardData("🏆", strings.dhikrTitle, strings.dhikrSubtitle, onOpenLeaderboard),
        ToolCardData("📚", strings.rulesTitle, strings.rulesSubtitle, onOpenFiqh),
        ToolCardData("🤲", strings.ashra1Tab, strings.ashra1Title, onOpenDuas)
    )

    val goldColor = Color(0xFFD4AF37)
    val cardBg = if (isDarkTheme) {
        Brush.verticalGradient(listOf(Color(0xFF0F3627), Color(0xFF082017)))
    } else {
        Brush.verticalGradient(listOf(Color(0xFFFFFFFF), Color(0xFFFAF7EE)))
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = strings.toolsHubTitle,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (isDarkTheme) goldColor else Color(0xFF0E5D47),
                letterSpacing = 1.sp
            )
            Text(
                text = "✦ Royal Hub",
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold,
                color = goldColor
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items.forEach { item ->
                Box(
                    modifier = Modifier
                        .width(148.dp)
                        .height(116.dp)
                        .shadow(elevation = 3.dp, shape = RoundedCornerShape(18.dp))
                        .clip(RoundedCornerShape(18.dp))
                        .background(cardBg)
                        .border(
                            1.5.dp,
                            Brush.linearGradient(
                                listOf(goldColor, goldColor.copy(alpha = 0.5f), Color(0xFFF3D48A))
                            ),
                            RoundedCornerShape(18.dp)
                        )
                        .clickable { item.action() }
                ) {
                    // Top Sheen Bar (3dp)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF0E5D47), goldColor, Color(0xFF0E5D47))
                                )
                            )
                    )

                    // Corner Sparkle Motif
                    Text(
                        text = "✦",
                        fontSize = 11.sp,
                        color = goldColor.copy(alpha = 0.6f),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 8.dp, end = 10.dp)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Icon in ornate circle badge
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(if (isDarkTheme) Color(0x33D4AF37) else Color(0x180E5D47))
                                .border(1.dp, goldColor.copy(alpha = 0.6f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = item.icon, fontSize = 16.sp)
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text(
                                text = item.title,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isDarkTheme) Color.White else Color(0xFF14241E),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            // Luxury Gold Subtitle Capsule
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isDarkTheme) Color(0xFF081C14) else Color(0xFFF0EAE0))
                                    .border(0.8.dp, goldColor.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = item.sub,
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDarkTheme) Color(0xFFF3D48A) else Color(0xFF7A6A4D),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// TODAY'S IBADAH CHECKLIST (Deeds Tracker)
// ─────────────────────────────────────────────────────────────
@Composable
private fun TodayDeedsTrackerCard(
    context: android.content.Context,
    strings: RamadanStringsData,
    isDarkTheme: Boolean,
    trigger: Long
) {
    val haptic = LocalHapticFeedback.current

    val deedKeys = listOf("roza", "fajr", "dhuhr", "asr", "maghrib", "isha", "taraweeh", "quran", "adhkar", "sadaqah", "dua")
    val deeds = deedKeys.mapIndexed { index, key ->
        Pair(key, strings.deeds.getOrElse(index) { key })
    }

    val completedCount = remember(trigger) {
        RamadanRepository.getTodayCompletedDeedsCount(context)
    }
    val progressPct = (completedCount * 100) / deeds.size

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(if (isDarkTheme) Color(0xFF092017) else Color.White)
            .border(1.2.dp, Color(0x60D4AF37), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "✅ ${strings.trackerTitle}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD4AF37)
                    )
                    Text(
                        text = "$completedCount / ${deeds.size} ${strings.deedsCompleted} ($progressPct%)",
                        fontSize = 11.sp,
                        color = if (isDarkTheme) Color(0xFFD1D5DB) else Color.Gray
                    )
                }

                Text(
                    text = "$progressPct%",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFD4AF37)
                )
            }

            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.White.copy(alpha = 0.1f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = (completedCount.toFloat() / deeds.size).coerceIn(0f, 1f))
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF0E5D47), Color(0xFFD4AF37))
                            )
                        )
                )
            }

            // Checkbox Items
            deeds.forEach { (key, label) ->
                val isChecked = RamadanRepository.isDeedDone(context, key)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isChecked) Color(0xFF0E5D47).copy(alpha = 0.25f) else Color.Transparent)
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            RamadanRepository.setDeedDone(context, key, !isChecked)
                        }
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(if (isChecked) Color(0xFF0E5D47) else Color.Transparent)
                            .border(1.5.dp, if (isChecked) Color(0xFFD4AF37) else Color.Gray, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isChecked) {
                            Icon(Icons.Default.Check, contentDescription = "Done", tint = Color.White, modifier = Modifier.size(14.dp))
                        }
                    }

                    Text(
                        text = label,
                        fontSize = 13.sp,
                        fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Normal,
                        color = if (isChecked) Color(0xFFD4AF37) else if (isDarkTheme) Color(0xFFE5E7EB) else Color(0xFF14241E)
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// CITY CHALLENGE HIGHLIGHT BANNER
// ─────────────────────────────────────────────────────────────
@Composable
private fun CityChallengeHighlightBanner(
    context: android.content.Context,
    strings: RamadanStringsData,
    isDarkTheme: Boolean,
    onOpenLeaderboard: () -> Unit
) {
    val selectedCity = RamadanRepository.getSelectedCity(context)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(Color(0xFF0C3829), Color(0xFF061E16))
                )
            )
            .border(1.2.dp, Color(0xFFD4AF37), RoundedCornerShape(18.dp))
            .clickable { onOpenLeaderboard() }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "🏆 $selectedCity ${strings.dhikrTitle}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD4AF37)
                )
                Text(
                    text = "1,48,250 Community Dhikr Recited!",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Tap to view $selectedCity race & participate",
                    fontSize = 11.sp,
                    color = Color(0xFF9CA3AF)
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFD4AF37))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Join →",
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp,
                    color = Color(0xFF06140E)
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// WATER INTAKE TRACKER (Iftar to Sehri)
// ─────────────────────────────────────────────────────────────
@Composable
private fun WaterIntakeCard(
    strings: RamadanStringsData,
    glasses: Int,
    isDarkTheme: Boolean,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(if (isDarkTheme) Color(0xFF092017) else Color.White)
            .border(1.dp, Color(0x60D4AF37), RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "💧 ${strings.waterTrackerTitle}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF38BDF8)
                    )
                    Text(
                        text = strings.hydrationGoal,
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        onClick = onDecrement,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0E5D47))
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Minus", tint = Color.White)
                    }

                    Text(
                        text = "$glasses / 10",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF38BDF8)
                    )

                    IconButton(
                        onClick = onIncrement,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0E5D47))
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                    }
                }
            }

            // Glass icons row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (i in 1..10) {
                    val isDrunk = i <= glasses
                    Text(
                        text = if (isDrunk) "🥛" else "⚪",
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// ASHRA SPECIAL DHIKR COUNTER CARD
// ─────────────────────────────────────────────────────────────
@Composable
private fun AshraDhikrCounterCard(
    strings: RamadanStringsData,
    count: Int,
    isDarkTheme: Boolean,
    onIncrement: () -> Unit,
    onReset: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0C3325), Color(0xFF051711))
                )
            )
            .border(1.2.dp, Color(0xFFD4AF37), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "📿 ${strings.ashraDhikrTitle}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD4AF37)
            )
            Text(
                text = "رَبِّ اغْفِرْ وَارْحَمْ وَأَنْتَ خَيْرُ الرَّاحِمِينَ",
                fontSize = 18.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Text(
                text = "\"Rabbi-ghfir warham wa Anta Khayrur-Raahimeen\"",
                fontSize = 11.sp,
                color = Color(0xFFD4AF37),
                textAlign = TextAlign.Center
            )

            // Huge Clickable Counter Button
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0xFF0E5D47), Color(0xFF06221A))
                        )
                    )
                    .border(2.dp, Color(0xFFD4AF37), CircleShape)
                    .clickable { onIncrement() },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$count",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text("TAP", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                }
            }

            Text(
                text = strings.reset,
                fontSize = 11.sp,
                color = Color.Gray,
                modifier = Modifier.clickable { onReset() }
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────
// 30-DAY RAMADAN JOURNEY SECTION
// ─────────────────────────────────────────────────────────────
@Composable
private fun DaysJourneySection(
    strings: RamadanStringsData,
    language: String,
    selectedFilter: Int,
    onSelectFilter: (Int) -> Unit,
    isDarkTheme: Boolean
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "📅 ${strings.journeyTitle}",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDarkTheme) Color(0xFFD4AF37) else Color(0xFF0E5D47),
            letterSpacing = 1.sp
        )

        // Ashra Filter Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val tabs = listOf(strings.all30Days, strings.ashra1Tab, strings.ashra2Tab, strings.ashra3Tab)
            tabs.forEachIndexed { index, label ->
                val isSel = selectedFilter == index
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSel) Color(0xFF0E5D47) else if (isDarkTheme) Color(0xFF092017) else Color(0xFFE5E7EB))
                        .border(1.dp, if (isSel) Color(0xFFD4AF37) else Color.Transparent, RoundedCornerShape(10.dp))
                        .clickable { onSelectFilter(index) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = label,
                        fontSize = 12.sp,
                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSel) Color.White else if (isDarkTheme) Color(0xFF9CA3AF) else Color(0xFF4B5563)
                    )
                }
            }
        }

        // Filtered List
        val filteredDays = when (selectedFilter) {
            1 -> RamadanData.days30List.filter { it.ashraNumber == 1 }
            2 -> RamadanData.days30List.filter { it.ashraNumber == 2 }
            3 -> RamadanData.days30List.filter { it.ashraNumber == 3 }
            else -> RamadanData.days30List
        }

        filteredDays.forEach { dayPlan ->
            DayJourneyCard(
                dayPlan = dayPlan,
                strings = strings,
                language = language,
                isDarkTheme = isDarkTheme
            )
        }
    }
}

@Composable
private fun DayJourneyCard(
    dayPlan: RamadanDayPlan,
    strings: RamadanStringsData,
    language: String,
    isDarkTheme: Boolean
) {
    var isExpanded by remember { mutableStateOf(false) }

    val deedText = when (language) {
        "hi" -> dayPlan.sunnahDeedHi
        "ur" -> dayPlan.sunnahDeedUr
        else -> dayPlan.sunnahDeedEn
    }
    val translationText = when (language) {
        "hi" -> dayPlan.translationHi
        "ur" -> dayPlan.translationUr
        else -> dayPlan.translationEn
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(if (isDarkTheme) Color(0xFF092017) else Color.White)
            .border(1.dp, Color(0x40D4AF37), RoundedCornerShape(14.dp))
            .clickable { isExpanded = !isExpanded }
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0E5D47)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${dayPlan.dayNumber}",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }

                    Column {
                        Text(
                            text = "${strings.dayPrefix} ${dayPlan.dayNumber}: ${dayPlan.duaTitle}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (isDarkTheme) Color.White else Color(0xFF14241E)
                        )
                        Text(
                            text = "${strings.sunnahDeedLabel}: $deedText",
                            fontSize = 11.sp,
                            color = Color(0xFFD4AF37)
                        )
                    }
                }

                Text(
                    text = if (isExpanded) "▲" else "▼",
                    color = Color(0xFFD4AF37),
                    fontSize = 12.sp
                )
            }

            if (isExpanded) {
                // Arabic Dua
                Text(
                    text = dayPlan.arabicDua,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) Color(0xFFFAF7EE) else Color(0xFF14241E),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )

                Text(
                    text = dayPlan.transliteration,
                    fontSize = 12.sp,
                    color = Color(0xFFD4AF37),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = translationText,
                    fontSize = 12.sp,
                    color = if (isDarkTheme) Color(0xFFD1D5DB) else Color(0xFF374151),
                    lineHeight = 17.sp
                )

                // Quran Gem box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF0E5D47).copy(alpha = 0.2f))
                        .padding(8.dp)
                ) {
                    Column {
                        Text(
                            text = "📖 ${strings.quranGemLabel} (${dayPlan.quranGemRef}):",
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = Color(0xFF6EE7B7)
                        )
                        Text(
                            text = dayPlan.quranGemAyah,
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Serif,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// TWINKLING STARS BACKGROUND CANVAS
// ─────────────────────────────────────────────────────────────
@Composable
private fun TwinklingStarsBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "twinkle")
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "starAlpha"
    )

    // Pre-calculate pseudo-random stars
    val stars = remember {
        val rand = Random(42)
        List(40) {
            Triple(
                rand.nextFloat(), // xFraction
                rand.nextFloat(), // yFraction
                rand.nextFloat() * 1.5f + 1f // radius
            )
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        stars.forEach { (xFrac, yFrac, radius) ->
            drawCircle(
                color = Color(0xFFD4AF37).copy(alpha = alphaAnim * 0.45f),
                radius = radius.dp.toPx(),
                center = Offset(size.width * xFrac, size.height * yFrac)
            )
        }
    }
}
