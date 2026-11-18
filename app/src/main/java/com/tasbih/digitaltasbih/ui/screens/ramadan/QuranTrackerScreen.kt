package com.tasbih.digitaltasbih.ui.screens.ramadan

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.tasbih.digitaltasbih.data.PrayerRepository
import com.tasbih.digitaltasbih.data.QuranData
import com.tasbih.digitaltasbih.data.ramadan.QuranBookmarkItem
import com.tasbih.digitaltasbih.data.ramadan.QuranReflectionNote
import com.tasbih.digitaltasbih.data.ramadan.QuranTrackerRepository
import com.tasbih.digitaltasbih.ui.components.AppToast
import com.tasbih.digitaltasbih.ui.components.BottomNavigationBar
import com.tasbih.digitaltasbih.ui.theme.AmiriQuranFont
import java.util.UUID

@Composable
fun QuranTrackerScreen(
    onBack: () -> Unit,
    onOpenQuranReader: (Int) -> Unit = {},
    onOpenQuranAudio: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToTasbih: () -> Unit = {},
    onNavigateToQuran: () -> Unit = {},
    onNavigateToCalendar: () -> Unit = {},
    onNavigateToDua: () -> Unit = {}
) {
    BackHandler { onBack() }
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    val isDarkTheme = remember { PrayerRepository.isDarkTheme() }
    val trigger = QuranTrackerRepository.updateTrigger.value

    // Persistent State
    val pagesRead = remember(trigger) { QuranTrackerRepository.getPagesRead(context) }
    val currentJuz = remember(trigger) { QuranTrackerRepository.getCurrentJuz(context) }
    val completedJuzCount = remember(trigger) { QuranTrackerRepository.getCompletedJuzCount(context) }
    val readingMins = remember(trigger) { QuranTrackerRepository.getReadingMinutes(context) }
    val streakDays = remember(trigger) { QuranTrackerRepository.getStreakDays(context) }
    val targetDays = remember(trigger) { QuranTrackerRepository.getTargetDays(context) }
    val pagesPerSalah = remember(trigger) { QuranTrackerRepository.getPagesPerSalah(context) }
    val savedNotes = remember(trigger) { QuranTrackerRepository.getNotes(context) }
    val savedBookmarks = remember(trigger) { QuranTrackerRepository.getBookmarks(context) }

    // Today's 5 Salah Checklist State
    val isFajrRead = remember(trigger) { QuranTrackerRepository.isSalahRead(context, "fajr") }
    val isDhuhrRead = remember(trigger) { QuranTrackerRepository.isSalahRead(context, "dhuhr") }
    val isAsrRead = remember(trigger) { QuranTrackerRepository.isSalahRead(context, "asr") }
    val isMaghribRead = remember(trigger) { QuranTrackerRepository.isSalahRead(context, "maghrib") }
    val isIshaRead = remember(trigger) { QuranTrackerRepository.isSalahRead(context, "isha") }

    val todayPrayersCompleted = listOf(isFajrRead, isDhuhrRead, isAsrRead, isMaghribRead, isIshaRead).count { it }
    val todayPagesDone = todayPrayersCompleted * pagesPerSalah
    val todayGoalPages = 5 * pagesPerSalah

    // Main Navigation Tabs (0: 30 Juz Tracker, 1: 114 Surahs, 2: Custom Goal, 3: Reflections & Bookmarks)
    var selectedTab by remember { mutableIntStateOf(0) }

    // Filters & Search
    var juzFilter by remember { mutableIntStateOf(0) } // 0: All, 1: Completed, 2: Remaining
    var surahFilter by remember { mutableIntStateOf(0) } // 0: All, 1: Completed, 2: Remaining
    var surahSearchQuery by remember { mutableStateOf("") }

    // Modals
    var isNoteDialogOpen by remember { mutableStateOf(false) }
    var isPlannerDialogOpen by remember { mutableStateOf(false) }
    var isDuasDialogOpen by remember { mutableStateOf(false) }

    // Theme Palettes
    val emeraldDark = if (isDarkTheme) Color(0xFF071B12) else Color(0xFFF9F7F2)
    val cardBg = if (isDarkTheme) {
        Brush.verticalGradient(listOf(Color(0xFF0F2C21), Color(0xFF0A1F17)))
    } else {
        Brush.verticalGradient(listOf(Color(0xFFFFFFFF), Color(0xFFFAF7EE)))
    }
    val goldColor = Color(0xFFD4AF37)
    val emeraldAccent = Color(0xFF0E5D47)

    val progressPct = ((pagesRead.toFloat() / 604f) * 100f).coerceIn(0f, 100f)
    val animatedProgress by animateFloatAsState(
        targetValue = progressPct / 100f,
        animationSpec = tween(durationMillis = 600),
        label = "quranProgress"
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(if (isDarkTheme) Color(0x33D4AF37) else Color(0x150E5D47))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = if (isDarkTheme) Color.White else emeraldAccent,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "📖 Quran Tracker",
                        fontSize = 17.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isDarkTheme) Color(0xFFF3D48A) else emeraldAccent,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = "Read • Track • Complete with Salah Routine",
                        fontSize = 10.sp,
                        color = if (isDarkTheme) Color(0xFFC0A868) else Color(0xFF7A6A4D),
                        fontWeight = FontWeight.Medium
                    )
                }

                // Goal Pill / Customize button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isDarkTheme) Color(0xFF09251B) else Color(0xFFE8F2EC))
                        .border(1.dp, goldColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .clickable { isPlannerDialogOpen = true }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "🎯 ${targetDays}d Plan",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) goldColor else emeraldAccent
                    )
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(
                activeTabId = "quran",
                isDarkTheme = isDarkTheme,
                onTabSelected = { tabId ->
                    when (tabId) {
                        "home" -> onNavigateToHome()
                        "tasbih" -> onNavigateToTasbih()
                        "quran" -> onNavigateToQuran()
                        "calendar" -> onNavigateToCalendar()
                        "dua" -> onNavigateToDua()
                    }
                }
            )
        },
        containerColor = emeraldDark
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(top = 4.dp, bottom = 28.dp)
        ) {
            // ══════════════════════════════════════════════════════════
            // 1. HERO JOURNEY CARD: Progress Arc + 4 Stats + Hadith
            // ══════════════════════════════════════════════════════════
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 4.dp, shape = RoundedCornerShape(20.dp))
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (isDarkTheme) {
                                Brush.verticalGradient(listOf(Color(0xFF0E382A), Color(0xFF071F17)))
                            } else {
                                Brush.verticalGradient(listOf(Color(0xFF0E5D47), Color(0xFF084131)))
                            }
                        )
                        .border(1.5.dp, goldColor.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Your Quran Journey",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    fontFamily = FontFamily.Serif
                                )
                                Text(
                                    text = "Step by step towards full Khatam",
                                    fontSize = 11.5.sp,
                                    color = Color(0xFFD4AF37)
                                )
                            }

                            // Circular Progress Arc
                            Box(
                                modifier = Modifier.size(76.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                androidx.compose.foundation.Canvas(modifier = Modifier.size(76.dp)) {
                                    drawArc(
                                        color = Color(0x33FFFFFF),
                                        startAngle = -90f,
                                        sweepAngle = 360f,
                                        useCenter = false,
                                        style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                                    )
                                    drawArc(
                                        brush = Brush.sweepGradient(
                                            listOf(Color(0xFFD4AF37), Color(0xFFF3D48A), Color(0xFFD4AF37))
                                        ),
                                        startAngle = -90f,
                                        sweepAngle = animatedProgress * 360f,
                                        useCenter = false,
                                        style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                                    )
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "${progressPct.toInt()}%",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFFF3D48A)
                                    )
                                    Text(
                                        text = "Complete",
                                        fontSize = 8.sp,
                                        color = Color(0xFFE0E0E0)
                                    )
                                }
                            }
                        }

                        // 4 Metrics Grid
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            val hours = readingMins / 60
                            val mins = readingMins % 60
                            listOf(
                                Triple("📖 Juz Done", "$completedJuzCount / 30", Color(0xFFF3D48A)),
                                Triple("📄 Pages", "$pagesRead / 604", Color.White),
                                Triple("⏱️ Time", "${hours}h ${mins}m", Color.White),
                                Triple("🔥 Streak", "$streakDays Days", Color(0xFFFF9800))
                            ).forEach { (lbl, value, valColor) ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0x22FFFFFF))
                                        .border(1.dp, Color(0x33D4AF37), RoundedCornerShape(10.dp))
                                        .padding(vertical = 7.dp, horizontal = 2.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(lbl, fontSize = 9.sp, color = Color(0xFFD4AF37))
                                        Text(
                                            text = value,
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = valColor,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }

                        // Bukhari Hadith Plaque
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x25000000))
                                .border(1.dp, goldColor.copy(alpha = 0.45f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 7.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "خَيْرُكُمْ مَنْ تَعَلَّمَ الْقُرْآنَ وَعَلَّمَهُ",
                                    fontFamily = AmiriQuranFont,
                                    fontSize = 14.5.sp,
                                    color = Color(0xFFF3D48A),
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "\"The best of you are those who learn the Quran and teach it.\" (Bukhari)",
                                    fontSize = 9.5.sp,
                                    color = Color(0xFFE2E8F0),
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // ══════════════════════════════════════════════════════════
            // 2. TODAY'S 5 SALAH READING CHECKLIST (User requested interactive checklist!)
            // ══════════════════════════════════════════════════════════
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 3.dp, shape = RoundedCornerShape(20.dp))
                        .clip(RoundedCornerShape(20.dp))
                        .background(cardBg)
                        .border(1.5.dp, goldColor.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "📅 TODAY'S PRAYER READING ROUTINE",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isDarkTheme) Color(0xFFF3D48A) else emeraldAccent,
                                    letterSpacing = 0.8.sp
                                )
                                Text(
                                    text = "Read $pagesPerSalah pages after each Salah ($todayPagesDone / $todayGoalPages pgs done)",
                                    fontSize = 10.5.sp,
                                    color = if (isDarkTheme) Color(0xFFC0A868) else Color(0xFF7A6A4D)
                                )
                            }

                            // Customize button
                            TextButton(
                                onClick = { isPlannerDialogOpen = true },
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text("⚙️ Edit", fontSize = 11.sp, color = goldColor, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Progress Bar for Today
                        LinearProgressIndicator(
                            progress = { (todayPrayersCompleted.toFloat() / 5f).coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = goldColor,
                            trackColor = if (isDarkTheme) Color(0xFF132A20) else Color(0xFFE0DDD5)
                        )

                        // 5 Prayer Checkboxes Row
                        val prayers = listOf(
                            Triple("fajr", "🌅 After Fajr", isFajrRead),
                            Triple("dhuhr", "☀️ After Dhuhr", isDhuhrRead),
                            Triple("asr", "⛅ After Asr", isAsrRead),
                            Triple("maghrib", "🌇 After Maghrib", isMaghribRead),
                            Triple("isha", "🌙 After Isha", isIshaRead)
                        )

                        prayers.forEach { (key, label, isRead) ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (isRead) {
                                            if (isDarkTheme) Color(0xFF143B2A) else Color(0xFFE8F5E9)
                                        } else {
                                            if (isDarkTheme) Color(0xFF091F17) else Color(0xFFF6F3EB)
                                        }
                                    )
                                    .border(
                                        1.dp,
                                        if (isRead) goldColor else Color(0x30D4AF37),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        val newState = QuranTrackerRepository.toggleSalahRead(context, key, pagesPerSalah)
                                        val statusText = if (newState) "✓ Marked completed (+${pagesPerSalah} pages)!" else "Marked unread"
                                        AppToast.show("$label: $statusText")
                                    }
                                    .padding(horizontal = 12.dp, vertical = 9.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        // Tick mark circle
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(if (isRead) emeraldAccent else Color.Transparent)
                                                .border(
                                                    1.5.dp,
                                                    if (isRead) goldColor else Color.Gray,
                                                    CircleShape
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (isRead) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Done",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = label,
                                            fontSize = 12.5.sp,
                                            fontWeight = if (isRead) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isRead) (if (isDarkTheme) Color.White else Color(0xFF0E5D47)) else (if (isDarkTheme) Color(0xFFD1D5DB) else Color(0xFF374151))
                                        )
                                    }

                                    // Badge
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isRead) goldColor else Color(0x22888888))
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Text(
                                            text = if (isRead) "✓ Completed" else "+$pagesPerSalah pgs",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isRead) Color(0xFF072118) else (if (isDarkTheme) Color(0xFFA0B0A7) else Color(0xFF666666))
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ══════════════════════════════════════════════════════════
            // 3. MAIN NAVIGATION TABS (30 Juz | 114 Surahs | Custom Goal | Notes & Duas)
            // ══════════════════════════════════════════════════════════
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isDarkTheme) Color(0xFF081C15) else Color(0xFFEAE5D8))
                        .padding(3.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val tabs = listOf(
                        "📖 30 Juz",
                        "🕌 Surahs",
                        "🎯 My Goal",
                        "📝 Notes & Duas"
                    )

                    tabs.forEachIndexed { idx, tabTitle ->
                        val isSelected = selectedTab == idx
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) emeraldAccent else Color.Transparent)
                                .clickable { selectedTab = idx }
                                .padding(vertical = 9.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tabTitle,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                color = if (isSelected) Color.White else (if (isDarkTheme) Color(0xFFB0C0B7) else Color(0xFF555555)),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            // ══════════════════════════════════════════════════════════
            // TAB 0: 30 JUZ CHECKLIST
            // ══════════════════════════════════════════════════════════
            if (selectedTab == 0) {
                item {
                    // Filter Chips: All | Completed | Remaining
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            Pair("All (30)", 0),
                            Pair("Completed ($completedJuzCount)", 1),
                            Pair("Remaining (${30 - completedJuzCount})", 2)
                        ).forEach { (lbl, fIdx) ->
                            val isFSelected = juzFilter == fIdx
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isFSelected) goldColor else (if (isDarkTheme) Color(0xFF10281F) else Color(0xFFF3EEE3)))
                                    .border(1.dp, if (isFSelected) goldColor else Color(0x30D4AF37), RoundedCornerShape(10.dp))
                                    .clickable { juzFilter = fIdx }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = lbl,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isFSelected) Color(0xFF0E382A) else (if (isDarkTheme) Color.White else Color(0xFF14241E))
                                )
                            }
                        }
                    }
                }

                // List of 30 Juz
                val filteredJuz = (1..30).filter { jNum ->
                    val isDone = QuranTrackerRepository.isJuzCompleted(context, jNum)
                    when (juzFilter) {
                        1 -> isDone
                        2 -> !isDone
                        else -> true
                    }
                }

                items(filteredJuz) { jNum ->
                    val isCompleted = QuranTrackerRepository.isJuzCompleted(context, jNum)
                    val startPage = ((jNum - 1) * 20) + 1
                    val endPage = (jNum * 20).coerceAtMost(604)
                    val juzMeta = QuranData.juzList.getOrNull(jNum - 1)
                    val firstSurahNum = juzMeta?.ranges?.firstOrNull()?.surahNumber ?: 1
                    val surahMeta = QuranData.surahs.getOrNull(firstSurahNum - 1)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(cardBg)
                            .border(
                                1.dp,
                                if (isCompleted) goldColor else Color(0x28D4AF37),
                                RoundedCornerShape(14.dp)
                            )
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Left: Checkbox + Juz Info
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                // Checkbox to Mark Completed
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(if (isCompleted) emeraldAccent else Color.Transparent)
                                        .border(
                                            1.5.dp,
                                            if (isCompleted) goldColor else Color.Gray,
                                            CircleShape
                                        )
                                        .clickable {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            val done = QuranTrackerRepository.toggleJuzCompleted(context, jNum)
                                            AppToast.show(if (done) "🎉 MashaAllah! Juz $jNum marked completed!" else "Juz $jNum marked uncompleted")
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isCompleted) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Done",
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Juz $jNum",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isDarkTheme) Color.White else Color(0xFF14241E)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "• Pages $startPage–$endPage",
                                            fontSize = 11.sp,
                                            color = Color.Gray
                                        )
                                    }
                                    Text(
                                        text = "${surahMeta?.englishName ?: ""} (${juzMeta?.ranges?.firstOrNull()?.ayahRange ?: ""})",
                                        fontSize = 11.sp,
                                        color = if (isDarkTheme) Color(0xFFA0B0A7) else Color(0xFF6B7280)
                                    )
                                }
                            }

                            // Right: Status Chip + Read Button
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isCompleted) Color(0xFF104434) else Color(0x18888888))
                                        .border(
                                            0.8.dp,
                                            if (isCompleted) goldColor else Color.Transparent,
                                            RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = if (isCompleted) "Completed ✓" else "Pending",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isCompleted) Color(0xFFF3D48A) else Color.Gray
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                IconButton(
                                    onClick = { onOpenQuranReader(firstSurahNum) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = "Read",
                                        tint = goldColor,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ══════════════════════════════════════════════════════════
            // TAB 1: 114 SURAHS CHECKLIST WITH SEARCH & READ
            // ══════════════════════════════════════════════════════════
            if (selectedTab == 1) {
                item {
                    // Search Bar
                    OutlinedTextField(
                        value = surahSearchQuery,
                        onValueChange = { surahSearchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Search Surah by name or number...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = goldColor) },
                        trailingIcon = {
                            if (surahSearchQuery.isNotBlank()) {
                                IconButton(onClick = { surahSearchQuery = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear")
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp)
                    )
                }

                // Filter Chips
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            Pair("All (114)", 0),
                            Pair("Completed", 1),
                            Pair("Remaining", 2)
                        ).forEach { (lbl, fIdx) ->
                            val isFSelected = surahFilter == fIdx
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isFSelected) goldColor else (if (isDarkTheme) Color(0xFF10281F) else Color(0xFFF3EEE3)))
                                    .border(1.dp, if (isFSelected) goldColor else Color(0x30D4AF37), RoundedCornerShape(10.dp))
                                    .clickable { surahFilter = fIdx }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = lbl,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isFSelected) Color(0xFF0E382A) else (if (isDarkTheme) Color.White else Color(0xFF14241E))
                                )
                            }
                        }
                    }
                }

                val allSurahs = QuranData.surahs
                val filteredSurahs = allSurahs.filter { surah ->
                    val isDone = QuranTrackerRepository.isSurahCompleted(context, surah.number)
                    val matchesQuery = surahSearchQuery.isBlank() ||
                            surah.englishName.contains(surahSearchQuery, ignoreCase = true) ||
                            surah.hindiName.contains(surahSearchQuery, ignoreCase = true) ||
                            surah.number.toString() == surahSearchQuery.trim()

                    val matchesFilter = when (surahFilter) {
                        1 -> isDone
                        2 -> !isDone
                        else -> true
                    }
                    matchesQuery && matchesFilter
                }

                items(filteredSurahs) { surah ->
                    val isSurahDone = QuranTrackerRepository.isSurahCompleted(context, surah.number)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(cardBg)
                            .border(
                                1.dp,
                                if (isSurahDone) goldColor else Color(0x25D4AF37),
                                RoundedCornerShape(14.dp)
                            )
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Left: Checkbox + Surah Meta
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(if (isSurahDone) emeraldAccent else Color.Transparent)
                                        .border(
                                            1.5.dp,
                                            if (isSurahDone) goldColor else Color.Gray,
                                            CircleShape
                                        )
                                        .clickable {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            val done = QuranTrackerRepository.toggleSurahCompleted(context, surah.number)
                                            AppToast.show(if (done) "✓ ${surah.englishName} marked read!" else "${surah.englishName} marked unread")
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isSurahDone) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Done",
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "${surah.number}. ${surah.englishName}",
                                            fontSize = 13.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isDarkTheme) Color.White else Color(0xFF14241E)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "(${surah.numberOfAyahs} Ayahs)",
                                            fontSize = 10.5.sp,
                                            color = Color.Gray
                                        )
                                    }
                                    Text(
                                        text = "${surah.revelationType} • ${surah.englishMeaning}",
                                        fontSize = 10.5.sp,
                                        color = if (isDarkTheme) Color(0xFFA0B0A7) else Color(0xFF6B7280)
                                    )
                                }
                            }

                            // Right: Arabic Name + Read Button
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = surah.arabicName,
                                    fontFamily = AmiriQuranFont,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = goldColor
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                IconButton(
                                    onClick = { onOpenQuranReader(surah.number) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = "Read",
                                        tint = goldColor,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ══════════════════════════════════════════════════════════
            // TAB 2: CUSTOM GOAL & KHATAM PLANNER
            // ══════════════════════════════════════════════════════════
            if (selectedTab == 2) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(cardBg)
                            .border(1.5.dp, goldColor.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
                            .padding(16.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            Text(
                                text = "🎯 Customize Your Quran Khatam Target",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isDarkTheme) Color(0xFFF3D48A) else emeraldAccent
                            )
                            Text(
                                text = "Choose a reading plan that fits your Ramadan schedule:",
                                fontSize = 11.5.sp,
                                color = if (isDarkTheme) Color(0xFFD1D5DB) else Color(0xFF4B5563)
                            )

                            // 3 Preset Cards
                            val plans = listOf(
                                Triple(30, "🌟 30 Days (Ramadan Standard)", "1 Juz (20 pages/day) • 4 pages after each of the 5 Salahs"),
                                Triple(15, "⚡ 15 Days (2 Khatams Challenge)", "2 Juz (40 pages/day) • 8 pages after each of the 5 Salahs"),
                                Triple(60, "🕊️ 60 Days (Gentle Habit)", "0.5 Juz (10 pages/day) • 2 pages after each of the 5 Salahs")
                            )

                            plans.forEach { (days, title, subtitle) ->
                                val isPlanSelected = targetDays == days
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (isPlanSelected) {
                                                Brush.verticalGradient(listOf(Color(0xFF0E5D47), Color(0xFF07382A)))
                                            } else {
                                                Brush.verticalGradient(
                                                    if (isDarkTheme) listOf(Color(0xFF0A1F17), Color(0xFF071812))
                                                    else listOf(Color(0xFFF6F3EB), Color(0xFFEDE9E0))
                                                )
                                            }
                                        )
                                        .border(
                                            if (isPlanSelected) 1.5.dp else 1.dp,
                                            if (isPlanSelected) goldColor else Color(0x30D4AF37),
                                            RoundedCornerShape(12.dp)
                                        )
                                        .clickable {
                                            QuranTrackerRepository.setTargetDays(context, days)
                                            AppToast.show("✓ Switched to $days Days Khatam Plan!")
                                        }
                                        .padding(12.dp)
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                        Text(
                                            text = title,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isPlanSelected) Color.White else (if (isDarkTheme) Color.White else Color(0xFF14241E))
                                        )
                                        Text(
                                            text = subtitle,
                                            fontSize = 10.5.sp,
                                            color = if (isPlanSelected) Color(0xFFF3D48A) else Color(0xFF7A6A4D)
                                        )
                                    }
                                }
                            }

                            // Calculator Summary
                            val remainingPages = (604 - pagesRead).coerceAtLeast(0)
                            val dailyPgs = when (targetDays) {
                                15 -> 40
                                60 -> 10
                                else -> 20
                            }
                            val daysToFinish = if (dailyPgs > 0) (remainingPages / dailyPgs) else 30

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isDarkTheme) Color(0xFF143024) else Color(0xFFEAF5EE))
                                    .border(1.dp, goldColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                    .padding(12.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        text = "📊 Your Pace Summary:",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = goldColor
                                    )
                                    Text(
                                        text = "• Pages Left: $remainingPages of 604",
                                        fontSize = 11.5.sp
                                    )
                                    Text(
                                        text = "• At $dailyPgs pages/day ($pagesPerSalah pgs/Salah), you will finish in approx $daysToFinish days!",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ══════════════════════════════════════════════════════════
            // TAB 3: REFLECTIONS & DUAS & BOOKMARKS
            // ══════════════════════════════════════════════════════════
            if (selectedTab == 3) {
                // Section 1: Reflections Notes
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(cardBg)
                            .border(1.dp, goldColor.copy(alpha = 0.45f), RoundedCornerShape(18.dp))
                            .padding(14.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "✍️ My Quran Reflections (${savedNotes.size})",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDarkTheme) Color.White else Color(0xFF14241E)
                                )
                                Button(
                                    onClick = { isNoteDialogOpen = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = emeraldAccent),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("+ Add Note", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            if (savedNotes.isNotEmpty()) {
                                savedNotes.forEach { note ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(if (isDarkTheme) Color(0xFF091F17) else Color(0xFFF7F3E9))
                                            .border(1.dp, Color(0x25D4AF37), RoundedCornerShape(10.dp))
                                            .padding(10.dp)
                                    ) {
                                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = note.title,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isDarkTheme) Color(0xFFF3D48A) else emeraldAccent
                                                )
                                                IconButton(
                                                    onClick = { QuranTrackerRepository.deleteNote(context, note.id) },
                                                    modifier = Modifier.size(24.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Delete,
                                                        contentDescription = "Delete",
                                                        tint = Color.Gray,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                }
                                            }
                                            Text(
                                                text = note.text,
                                                fontSize = 11.sp,
                                                color = if (isDarkTheme) Color(0xFFD1D5DB) else Color(0xFF4B5563)
                                            )
                                            Text(
                                                text = "${note.date} • ${note.pageOrRuku}",
                                                fontSize = 9.sp,
                                                color = Color.Gray
                                            )
                                        }
                                    }
                                }
                            } else {
                                Text(
                                    text = "No notes added yet. Tap '+ Add Note' to capture your reflections on verses.",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }

                // Section 2: Bookmarks
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(cardBg)
                            .border(1.dp, goldColor.copy(alpha = 0.45f), RoundedCornerShape(18.dp))
                            .padding(14.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "🔖 Saved Bookmarks (${savedBookmarks.size})",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDarkTheme) Color.White else Color(0xFF14241E)
                                )
                                Button(
                                    onClick = {
                                        val bm = QuranBookmarkItem(
                                            id = UUID.randomUUID().toString(),
                                            label = "Page $pagesRead (Juz $currentJuz)",
                                            juz = currentJuz,
                                            page = pagesRead,
                                            ruku = (pagesRead * 558) / 604,
                                            date = QuranTrackerRepository.formatDateNow()
                                        )
                                        QuranTrackerRepository.addBookmark(context, bm)
                                        AppToast.show("🔖 Bookmark saved for Page $pagesRead!")
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = emeraldAccent),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("+ Bookmark Current", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            savedBookmarks.forEach { bm ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isDarkTheme) Color(0xFF091F17) else Color(0xFFF7F3E9))
                                        .border(1.dp, Color(0x25D4AF37), RoundedCornerShape(10.dp))
                                        .padding(10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = bm.label,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isDarkTheme) Color(0xFFF3D48A) else emeraldAccent
                                            )
                                            Text(
                                                text = "Page ${bm.page} • Juz ${bm.juz} • ${bm.date}",
                                                fontSize = 9.5.sp,
                                                color = Color.Gray
                                            )
                                        }
                                        Row {
                                            IconButton(
                                                onClick = {
                                                    QuranTrackerRepository.setPagesRead(context, bm.page)
                                                    AppToast.show("✓ Jumped to Page ${bm.page}")
                                                },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Go", tint = goldColor)
                                            }
                                            IconButton(
                                                onClick = { QuranTrackerRepository.removeBookmark(context, bm.id) },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(Icons.Default.Delete, contentDescription = "Remove", tint = Color.Gray)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Section 3: Authentic Quran Duas
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(cardBg)
                            .border(1.dp, goldColor.copy(alpha = 0.45f), RoundedCornerShape(18.dp))
                            .padding(14.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = "🤲 Authentic Quran Duas",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color.White else Color(0xFF14241E)
                            )

                            // Dua 1: Before Tilawat
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isDarkTheme) Color(0xFF091F17) else Color(0xFFF7F3E9))
                                    .padding(10.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                    Text("Dua Before Tilawat:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = goldColor)
                                    Text(
                                        text = "اللَّهُمَّ افْتَحْ عَلَيَّ حِكْمَتَكَ وَانْشُرْ عَلَيَّ رَحْمَتَكَ يَا ذَا الْجَلَالِ وَالْإِكْرَامِ",
                                        fontFamily = AmiriQuranFont,
                                        fontSize = 14.sp,
                                        color = if (isDarkTheme) Color(0xFFF3D48A) else emeraldAccent
                                    )
                                    Text(
                                        text = "O Allah, open for me the doors of Your wisdom and bestow Your mercy upon me, O Possessor of Majesty and Honor.",
                                        fontSize = 10.sp,
                                        color = if (isDarkTheme) Color(0xFFD1D5DB) else Color(0xFF4B5563)
                                    )
                                }
                            }

                            // Dua 2: After Khatm Quran
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isDarkTheme) Color(0xFF091F17) else Color(0xFFF7F3E9))
                                    .padding(10.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                    Text("Dua Khatm-ul-Quran (Upon Completion):", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = goldColor)
                                    Text(
                                        text = "اللَّهُمَّ ارْحَمْنِي بِالْقُرْآنِ وَاجْعَلْهُ لِي إِمَامًا وَنُورًا وَهُدًى وَرَحْمَةً",
                                        fontFamily = AmiriQuranFont,
                                        fontSize = 14.sp,
                                        color = if (isDarkTheme) Color(0xFFF3D48A) else emeraldAccent
                                    )
                                    Text(
                                        text = "O Allah, have mercy on me through the Quran, and make it for me a guide, light, guidance and mercy.",
                                        fontSize = 10.sp,
                                        color = if (isDarkTheme) Color(0xFFD1D5DB) else Color(0xFF4B5563)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // ══════════════════════════════════════════════════════════════════
    // DIALOGS
    // ══════════════════════════════════════════════════════════════════

    // 1. ADD REFLECTION NOTE DIALOG
    if (isNoteDialogOpen) {
        var noteTitle by remember { mutableStateOf("Juz $currentJuz • Page $pagesRead") }
        var noteText by remember { mutableStateOf("") }

        Dialog(onDismissRequest = { isNoteDialogOpen = false }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isDarkTheme) Color(0xFF0F261E) else Color.White)
                    .border(1.5.dp, goldColor, RoundedCornerShape(20.dp))
                    .padding(18.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "📝 Add Quran Reflection",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) goldColor else emeraldAccent
                    )
                    OutlinedTextField(
                        value = noteTitle,
                        onValueChange = { noteTitle = it },
                        label = { Text("Title / Verse") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = noteText,
                        onValueChange = { noteText = it },
                        label = { Text("Your reflection or lesson...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp),
                        maxLines = 5
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { isNoteDialogOpen = false }) {
                            Text("Cancel", color = Color.Gray)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (noteText.isNotBlank()) {
                                    val newNote = QuranReflectionNote(
                                        id = UUID.randomUUID().toString(),
                                        title = noteTitle.ifBlank { "Quran Reflection" },
                                        text = noteText,
                                        date = QuranTrackerRepository.formatDateNow(),
                                        pageOrRuku = "Page $pagesRead"
                                    )
                                    QuranTrackerRepository.addNote(context, newNote)
                                    AppToast.show("✓ Reflection saved!")
                                    isNoteDialogOpen = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = emeraldAccent)
                        ) {
                            Text("Save Note", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // 2. KHATAM PLANNER DIALOG
    if (isPlannerDialogOpen) {
        Dialog(onDismissRequest = { isPlannerDialogOpen = false }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isDarkTheme) Color(0xFF0F261E) else Color.White)
                    .border(1.5.dp, goldColor, RoundedCornerShape(20.dp))
                    .padding(18.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "🎯 Quran Khatam Target Planner",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) goldColor else emeraldAccent
                    )
                    Text(
                        text = "Choose how many days you want to complete the Quran in:",
                        fontSize = 11.5.sp,
                        color = if (isDarkTheme) Color(0xFFD1D5DB) else Color(0xFF4B5563)
                    )

                    listOf(
                        Triple(30, "30 Days (Ramadan)", "1 Juz (20 pages/day) • 4 pgs / Salah"),
                        Triple(15, "15 Days (2 Khatams)", "2 Juz (40 pages/day) • 8 pgs / Salah"),
                        Triple(60, "60 Days (Relaxed)", "0.5 Juz (10 pages/day) • 2 pgs / Salah")
                    ).forEach { (days, label, detail) ->
                        val isSelected = targetDays == days
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) emeraldAccent else (if (isDarkTheme) Color(0xFF091C15) else Color(0xFFF3EEE3)))
                                .border(1.dp, if (isSelected) goldColor else Color(0x30D4AF37), RoundedCornerShape(12.dp))
                                .clickable {
                                    QuranTrackerRepository.setTargetDays(context, days)
                                    AppToast.show("✓ Target set to $days Days ($detail)")
                                }
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = label,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else (if (isDarkTheme) Color.White else Color(0xFF14241E))
                                )
                                Text(
                                    text = detail,
                                    fontSize = 10.5.sp,
                                    color = if (isSelected) Color(0xFFF3D48A) else Color(0xFF7A6A4D)
                                )
                            }
                        }
                    }

                    Button(
                        onClick = { isPlannerDialogOpen = false },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = emeraldAccent)
                    ) {
                        Text("Save & Apply", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
