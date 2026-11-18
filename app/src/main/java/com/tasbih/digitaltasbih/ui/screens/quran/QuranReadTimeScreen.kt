package com.tasbih.digitaltasbih.ui.screens.quran

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.aspectRatio
import com.tasbih.digitaltasbih.data.MonthlyTrackerData
import com.tasbih.digitaltasbih.data.MonthlyWeekProgress
import com.tasbih.digitaltasbih.data.MonthlyDayCell
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.data.DayReadingRecord
import com.tasbih.digitaltasbih.data.HeatmapDay
import com.tasbih.digitaltasbih.data.HeatmapIntensity
import com.tasbih.digitaltasbih.data.MonthBreakdown
import com.tasbih.digitaltasbih.data.QuranReadStats
import com.tasbih.digitaltasbih.data.QuranReadTimeRepository
import com.tasbih.digitaltasbih.data.WeeklyDayProgress
import com.tasbih.digitaltasbih.ui.components.AppToast
import java.util.Calendar

/**
 * Screen 2 & 3: Quran Read Time Tracker & Analytics
 * Styled in Luxury Emerald Green and Crisp White (Day Mode):
 * - Streak, Best streak, Total time, Days metrics.
 * - Weekly progress bar chart (last 8 days) with minutes and day labels.
 * - Reading Activity Heatmap:
 *   High (20m+) -> Rich Green
 *   Moderate (10-19m) -> Medium Green
 *   Light (5-9m) -> Light Green
 *   Minimal (1-4m) -> Yellow
 *   Missed (0m) -> Red
 * - Days collapsible monthly breakdown with individual reading records.
 * - Log manual reading & set daily goal dialogs.
 */
@Composable
fun QuranReadTimeScreen(
    onBack: () -> Unit,
    onOpenReader: (type: String, number: Int) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current
    val trigger = QuranReadTimeRepository.updateTrigger.value

    LaunchedEffect(Unit) {
        QuranReadTimeRepository.init(context)
    }

    val currentYear = remember { Calendar.getInstance().get(Calendar.YEAR) }
    var selectedYear by remember { mutableIntStateOf(currentYear) }
    var isYearMenuOpen by remember { mutableStateOf(false) }

    val stats = remember(context, trigger) {
        QuranReadTimeRepository.getStats(context)
    }
    val weeklyProgress = remember(context, trigger) {
        QuranReadTimeRepository.getWeeklyProgress(context)
    }
    val monthlyTrackerData = remember(context, trigger) {
        QuranReadTimeRepository.getMonthlyTrackerData(context)
    }
    var progressViewMode by remember { mutableStateOf("Weekly") }
    val heatmapWeeks = remember(context, trigger, selectedYear) {
        QuranReadTimeRepository.getReadingActivityHeatmap(context, selectedYear)
    }
    val monthlyBreakdown = remember(context, trigger) {
        QuranReadTimeRepository.getMonthlyBreakdown(context)
    }

    // Modal state for manual logging
    var isLogDialogVisible by remember { mutableStateOf(false) }
    var targetDateForLog by remember { mutableStateOf<String?>(null) }
    var isGoalDialogVisible by remember { mutableStateOf(false) }

    // Selected day preview from heatmap
    var selectedHeatmapDay by remember { mutableStateOf<HeatmapDay?>(null) }

    // Expanded state for month accordions (current month expanded by default)
    val expandedMonths = remember { mutableStateMapOf<String, Boolean>() }
    LaunchedEffect(monthlyBreakdown) {
        if (monthlyBreakdown.isNotEmpty() && expandedMonths.isEmpty()) {
            expandedMonths[monthlyBreakdown.first().monthKey] = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F7F1))
            .statusBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // ── Top Navigation Bar ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back Button
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, Color(0xFFE2E8F0), CircleShape)
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF064E3B),
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Screen Title
                Text(
                    text = "Quran Read time",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF064E3B),
                    letterSpacing = 0.3.sp
                )

                // Year Dropdown Pill
                Box {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .border(1.dp, Color(0xFF10B981), RoundedCornerShape(12.dp))
                            .clickable { isYearMenuOpen = true }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "$selectedYear",
                            color = Color(0xFF064E3B),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Select Year",
                            tint = Color(0xFF059669),
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = isYearMenuOpen,
                        onDismissRequest = { isYearMenuOpen = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        listOf(currentYear, currentYear - 1, currentYear + 1).forEach { yr ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "$yr",
                                        color = if (yr == selectedYear) Color(0xFF059669) else Color(0xFF1E293B),
                                        fontWeight = if (yr == selectedYear) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                onClick = {
                                    selectedYear = yr
                                    isYearMenuOpen = false
                                }
                            )
                        }
                    }
                }
            }

            // ── Scrollable Body ──
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. TOP SUMMARY METRICS CARD (Image 2)
                item {
                    QuranReadTimeSummaryCard(
                        stats = stats,
                        onEditGoal = { isGoalDialogVisible = true }
                    )
                }

                // 2. WEEKLY / MONTHLY PROGRESS TRACKER CARD
                item {
                    QuranProgressTrackerCard(
                        viewMode = progressViewMode,
                        onViewModeChange = { progressViewMode = it },
                        weeklyProgress = weeklyProgress,
                        monthlyTrackerData = monthlyTrackerData,
                        onDayClick = { dateStr ->
                            targetDateForLog = dateStr
                            isLogDialogVisible = true
                        }
                    )
                }

                // 3. READING ACTIVITY HEATMAP CARD (Image 2 - Core Dot Grid)
                item {
                    ReadingActivityHeatmapCard(
                        selectedYear = selectedYear,
                        weeks = heatmapWeeks,
                        onDaySelected = { day ->
                            selectedHeatmapDay = day
                        }
                    )
                }

                // 4. DAYS SECTION (Image 3 - Monthly Accordion)
                item {
                    Text(
                        text = "Days",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF064E3B),
                        modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
                    )
                }

                if (monthlyBreakdown.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("📖", fontSize = 28.sp)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "No Quran reading logged yet",
                                    color = Color(0xFF64748B),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Start reading in the Quran reader or tap Log below",
                                    color = Color(0xFF94A3B8),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                } else {
                    items(monthlyBreakdown, key = { it.monthKey }) { month ->
                        val isExpanded = expandedMonths[month.monthKey] ?: false
                        MonthlyBreakdownCard(
                            month = month,
                            isExpanded = isExpanded,
                            goalMinutes = stats.goalMinutes,
                            onToggleExpand = {
                                expandedMonths[month.monthKey] = !isExpanded
                            },
                            onLogDay = { dateStr ->
                                targetDateForLog = dateStr
                                isLogDialogVisible = true
                            }
                        )
                    }
                }

            }

            // ── Persistent Luxury Bottom Action Bar ──
            Surface(
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .navigationBarsPadding(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            targetDateForLog = QuranReadTimeRepository.getTodayDateStr()
                            isLogDialogVisible = true
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFECFDF5)
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFF10B981))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color(0xFF059669),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Log Read Time",
                            color = Color(0xFF065F46),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = {
                            onOpenReader("surah", 1)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF059669)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.MenuBook,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Open Quran",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    // ── Log Reading Dialog ──
    if (isLogDialogVisible) {
        val activeDate = targetDateForLog ?: QuranReadTimeRepository.getTodayDateStr()
        val currentMins = QuranReadTimeRepository.getMinutesForDate(context, activeDate)
        LogReadingMinutesDialog(
            dateStr = activeDate,
            initialMinutes = currentMins,
            onDismiss = { isLogDialogVisible = false },
            onSave = { mins ->
                QuranReadTimeRepository.setReadingMinutes(context, mins, activeDate)
                AppToast.show("Logged $mins mins of Quran reading ✨")
                isLogDialogVisible = false
            }
        )
    }

    // ── Edit Daily Goal Dialog ──
    if (isGoalDialogVisible) {
        EditDailyGoalDialog(
            currentGoal = stats.goalMinutes,
            onDismiss = { isGoalDialogVisible = false },
            onSave = { newGoal ->
                QuranReadTimeRepository.setDailyGoalMinutes(context, newGoal)
                AppToast.show("Daily target set to $newGoal mins 🎯")
                isGoalDialogVisible = false
            }
        )
    }

    // ── Selected Heatmap Day Details Modal ──
    selectedHeatmapDay?.let { day ->
        AlertDialog(
            onDismissRequest = { selectedHeatmapDay = null },
            containerColor = Color.White,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(day.intensity.color)
                            .border(1.dp, day.intensity.borderColor, RoundedCornerShape(3.dp))
                    )
                    Text(
                        text = "${day.dayOfWeekName()} ${day.dayOfMonth} ${day.monthName}",
                        color = Color(0xFF064E3B),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = if (day.isFuture) "Upcoming Day" else "Reading Time: ${day.minutes} Minutes",
                        color = Color(0xFF1E293B),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Status: ${
                            when {
                                day.isFuture -> "Upcoming"
                                day.isToday && day.minutes == 0 -> "Today (In Progress)"
                                day.intensity == HeatmapIntensity.NOT_STARTED -> "Before App Install"
                                day.intensity == HeatmapIntensity.MISSED -> "Missed (0m)"
                                else -> day.intensity.label
                            }
                        }",
                        color = when {
                            day.intensity == HeatmapIntensity.MISSED -> Color(0xFFEF4444)
                            day.intensity == HeatmapIntensity.NOT_STARTED -> Color(0xFF64748B)
                            else -> Color(0xFF059669)
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (day.isToday) {
                        Text(
                            text = "✦ Today's Goal: ${stats.goalMinutes} mins (${if (day.minutes >= stats.goalMinutes) "Completed ✓" else "${stats.goalMinutes - day.minutes}m remaining"})",
                            color = Color(0xFF047857),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            },
            confirmButton = {
                if (!day.isFuture) {
                    TextButton(
                        onClick = {
                            selectedHeatmapDay = null
                            targetDateForLog = day.dateStr
                            isLogDialogVisible = true
                        }
                    ) {
                        Text("Log Time", color = Color(0xFF059669), fontWeight = FontWeight.Bold)
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedHeatmapDay = null }) {
                    Text("Close", color = Color(0xFF64748B))
                }
            }
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 1. TOP SUMMARY METRICS CARD (Image 2)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun QuranReadTimeSummaryCard(
    stats: QuranReadStats,
    onEditGoal: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(22.dp), ambientColor = Color(0x33059669))
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF065F46),
                        Color(0xFF047857),
                        Color(0xFF02513B)
                    )
                )
            )
            .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(22.dp))
            .padding(18.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header: Book Icon + Title + Goal Edit
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.MenuBook,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.95f),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Quran Read time",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.2.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(alpha = 0.2f))
                        .clickable { onEditGoal() }
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "Goal: ${stats.goalMinutes}m ⚙",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Row 1: Current Streak & Best Streak
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Current Streak
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = Color(0xFFF97316),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Current Streak",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "${stats.currentStreak}",
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "d",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }

                // Vertical Divider
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(55.dp)
                        .background(Color.White.copy(alpha = 0.25f))
                )

                // Best Streak
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = null,
                            tint = Color(0xFFFBBF24),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Best Streak",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "${stats.bestStreak}",
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "d",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.2f), thickness = 0.8.dp)
            Spacer(modifier = Modifier.height(14.dp))

            // Row 2: Read Time & Days
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Total Read Time
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = Color(0xFFFCD34D),
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "Read time",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "${stats.totalReadMinutes}",
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Mins",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }

                // Vertical Divider
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(55.dp)
                        .background(Color.White.copy(alpha = 0.25f))
                )

                // Days
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = Color(0xFF86EFAC),
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "Days",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${stats.totalActiveDays}",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 2. WEEKLY & MONTHLY PROGRESS TRACKER CARD (Day Mode White & Emerald)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun QuranProgressTrackerCard(
    viewMode: String,
    onViewModeChange: (String) -> Unit,
    weeklyProgress: List<WeeklyDayProgress>,
    monthlyTrackerData: MonthlyTrackerData,
    onDayClick: (String) -> Unit
) {
    var isDropdownOpen by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(20.dp), ambientColor = Color(0x10000000))
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
            .padding(18.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row: Left (Icon + Title), Right (Dropdown Selector Pill)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFECFDF5)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(if (viewMode == "Weekly") "📊" else "📅", fontSize = 15.sp)
                    }
                    Text(
                        text = if (viewMode == "Weekly") "Weekly Progress" else "Monthly Tracker",
                        color = Color(0xFF064E3B),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Dropdown Menu Button on the right
                Box {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF1F5F9))
                            .border(1.dp, Color(0xFF10B981).copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                            .clickable { isDropdownOpen = true }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = if (viewMode == "Weekly") "Weekly" else "Monthly",
                            color = Color(0xFF065F46),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Switch tracker view",
                            tint = Color(0xFF059669),
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = isDropdownOpen,
                        onDismissRequest = { isDropdownOpen = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        DropdownMenuItem(
                            text = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Weekly Progress",
                                        color = if (viewMode == "Weekly") Color(0xFF059669) else Color(0xFF1E293B),
                                        fontWeight = if (viewMode == "Weekly") FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 13.sp
                                    )
                                    if (viewMode == "Weekly") {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("✓", color = Color(0xFF059669), fontWeight = FontWeight.Bold)
                                    }
                                }
                            },
                            onClick = {
                                onViewModeChange("Weekly")
                                isDropdownOpen = false
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Monthly Tracker",
                                        color = if (viewMode == "Monthly") Color(0xFF059669) else Color(0xFF1E293B),
                                        fontWeight = if (viewMode == "Monthly") FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 13.sp
                                    )
                                    if (viewMode == "Monthly") {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("✓", color = Color(0xFF059669), fontWeight = FontWeight.Bold)
                                    }
                                }
                            },
                            onClick = {
                                onViewModeChange("Monthly")
                                isDropdownOpen = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (viewMode == "Weekly") {
                // ── WEEKLY VIEW: 8 Vertical Bars Row ──
                val maxMins = maxOf(30, weeklyProgress.maxOfOrNull { it.minutes } ?: 30)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    weeklyProgress.forEach { day ->
                        val barFraction = (day.minutes.toFloat() / maxMins.toFloat()).coerceIn(0f, 1f)

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onDayClick(day.dateStr) }
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(22.dp)
                                    .height(110.dp)
                                    .clip(RoundedCornerShape(11.dp))
                                    .background(Color(0xFFF1F5F2)),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                if (day.minutes > 0) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight(barFraction.coerceAtLeast(0.08f))
                                            .clip(RoundedCornerShape(11.dp))
                                            .background(
                                                Brush.verticalGradient(
                                                    listOf(Color(0xFF10B981), Color(0xFF059669))
                                                )
                                            )
                                    )
                                }

                                Text(
                                    text = "${day.minutes}",
                                    color = if (day.minutes > 0) Color(0xFF065F46) else Color(0xFF94A3B8),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = day.dayLabel,
                                color = if (day.isToday) Color(0xFF059669) else Color(0xFF64748B),
                                fontSize = 11.sp,
                                fontWeight = if (day.isToday) FontWeight.Bold else FontWeight.Normal,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                // ── MONTHLY TRACKER VIEW ──
                MonthlyTrackerContentView(
                    data = monthlyTrackerData,
                    onDayClick = onDayClick
                )
            }
        }
    }
}

@Composable
private fun MonthlyTrackerContentView(
    data: MonthlyTrackerData,
    onDayClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Month Header & Stat Pills
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = data.monthTitle,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF064E3B)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                // Total Minutes Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFECFDF5))
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "⏱ ${data.totalMinutes}m",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF065F46)
                    )
                }
                // Active Days Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF1F5F9))
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "📅 ${data.activeDays}/${data.totalDaysInMonth}d",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF334155)
                    )
                }
            }
        }

        // 2. Week-by-Week Bars for the Month (W1 to W5)
        val maxWeekMins = maxOf(60, data.weeks.maxOfOrNull { it.totalMinutes } ?: 60)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF8FAFC))
                .padding(12.dp)
        ) {
            Text(
                text = "Weekly Breakdown",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF334155),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Bottom
            ) {
                data.weeks.forEach { week ->
                    val fraction = (week.totalMinutes.toFloat() / maxWeekMins.toFloat()).coerceIn(0f, 1f)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(26.dp)
                                .height(72.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (week.isCurrentWeek) Color(0xFFE2E8F0) else Color(0xFFEDF2F7))
                                .then(
                                    if (week.isCurrentWeek) Modifier.border(1.2.dp, Color(0xFF10B981), RoundedCornerShape(8.dp))
                                    else Modifier
                                ),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            if (week.totalMinutes > 0) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(fraction.coerceAtLeast(0.12f))
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(Color(0xFF10B981), Color(0xFF059669))
                                            )
                                        )
                                )
                            }
                            Text(
                                text = "${week.totalMinutes}m",
                                color = if (week.totalMinutes > 0) Color.White else Color(0xFF94A3B8),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = week.weekLabel,
                            fontSize = 11.sp,
                            fontWeight = if (week.isCurrentWeek) FontWeight.Bold else FontWeight.Medium,
                            color = if (week.isCurrentWeek) Color(0xFF059669) else Color(0xFF475569)
                        )

                        Text(
                            text = week.dateRangeLabel.split(" ").firstOrNull() ?: "",
                            fontSize = 9.5.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }
            }
        }

        // 3. Calendar Days Grid (Days 1 to 31)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFFAFAF9))
                .border(0.8.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Calendar View",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF334155)
                )
                Text(
                    text = "Tap a day to log",
                    fontSize = 10.5.sp,
                    color = Color(0xFF059669),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            val dayHeaders = listOf("S", "M", "T", "W", "T", "F", "S")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                dayHeaders.forEach { h ->
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = h,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            val cells = mutableListOf<MonthlyDayCell?>()
            repeat(data.startDayOfWeekOffset) {
                cells.add(null)
            }
            cells.addAll(data.days)
            val remainder = cells.size % 7
            if (remainder != 0) {
                repeat(7 - remainder) { cells.add(null) }
            }

            cells.chunked(7).forEach { weekRow ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    weekRow.forEach { dayCell ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (dayCell != null) {
                                val isRead = dayCell.minutes > 0
                                val isMissed = !dayCell.isToday && !dayCell.isFuture && !dayCell.isBeforeInstall && dayCell.minutes == 0

                                val bgColor = when {
                                    isRead -> Color(0xFF059669)
                                    isMissed -> Color(0xFFFEE2E2)
                                    dayCell.isToday -> Color(0xFFECFDF5)
                                    else -> Color(0xFFF1F5F9)
                                }

                                val textColor = when {
                                    isRead -> Color.White
                                    isMissed -> Color(0xFFDC2626)
                                    dayCell.isToday -> Color(0xFF064E3B)
                                    else -> Color(0xFF94A3B8)
                                }

                                val borderMod = when {
                                    dayCell.isToday -> Modifier.border(1.5.dp, Color(0xFF059669), RoundedCornerShape(7.dp))
                                    isMissed -> Modifier.border(0.6.dp, Color(0xFFF87171), RoundedCornerShape(7.dp))
                                    else -> Modifier
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(7.dp))
                                        .background(bgColor)
                                        .then(borderMod)
                                        .clickable { onDayClick(dayCell.dateStr) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "${dayCell.dayNumber}",
                                            color = textColor,
                                            fontSize = 11.sp,
                                            fontWeight = if (isRead || dayCell.isToday) FontWeight.Bold else FontWeight.Normal
                                        )
                                        if (isRead) {
                                            Text(
                                                text = "${dayCell.minutes}m",
                                                color = Color.White.copy(alpha = 0.9f),
                                                fontSize = 7.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                lineHeight = 8.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 4. Monthly Progress Bar Footer
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Month Target: ${data.totalMinutes} / ${data.monthTargetMinutes} Mins",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF064E3B)
                )
                Text(
                    text = "${data.completionPercentage}%",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF059669)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFE2E8F0))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(data.completionPercentage / 100f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF10B981), Color(0xFF059669))
                            )
                        )
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 3. READING ACTIVITY HEATMAP CARD (Image 2 - Day Mode White & Emerald)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun ReadingActivityHeatmapCard(
    selectedYear: Int,
    weeks: List<List<HeatmapDay>>,
    onDaySelected: (HeatmapDay) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(20.dp), ambientColor = Color(0x10000000))
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
            .padding(18.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row: Title & Year tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFECFDF5)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📅", fontSize = 15.sp)
                    }
                    Text(
                        text = "Reading Activity",
                        color = Color(0xFF064E3B),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFECFDF5))
                        .border(0.8.dp, Color(0xFF10B981), RoundedCornerShape(10.dp))
                        .padding(horizontal = 9.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "$selectedYear",
                        color = Color(0xFF065F46),
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Month Labels Header (Aug, Sep, Oct)
            val monthLabels = remember(weeks) {
                val labels = mutableListOf<Pair<Int, String>>()
                var lastMonth = ""
                weeks.forEachIndexed { colIdx, week ->
                    val firstDay = week.firstOrNull()
                    if (firstDay != null && firstDay.monthName != lastMonth) {
                        labels.add(Pair(colIdx, firstDay.monthName))
                        lastMonth = firstDay.monthName
                    }
                }
                labels
            }

            val dayRowNames = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

            // Horizontal Scrollable Grid for smooth display across all screen widths
            val scrollState = rememberScrollState()
            LaunchedEffect(Unit) {
                scrollState.scrollTo(scrollState.maxValue)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
            ) {
                // Day Labels Column on the left
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.padding(top = 22.dp, end = 8.dp)
                ) {
                    dayRowNames.forEach { dayName ->
                        Box(
                            modifier = Modifier.height(16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = dayName,
                                color = Color(0xFF64748B),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                }

                // Columns of Weeks
                Column {
                    // Month labels row
                    Row(
                        modifier = Modifier.padding(bottom = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        weeks.forEachIndexed { colIdx, week ->
                            val match = monthLabels.find { it.first == colIdx }
                            Box(
                                modifier = Modifier.width(16.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (match != null) {
                                    Text(
                                        text = match.second,
                                        color = Color(0xFF334155),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }

                    // 7 Rows of day dots
                    for (row in 0 until 7) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            modifier = Modifier.padding(bottom = 5.dp)
                        ) {
                            weeks.forEach { week ->
                                val day = week.getOrNull(row)
                                if (day != null) {
                                    HeatmapDotCell(day = day, onClick = { onDaySelected(day) })
                                } else {
                                    Spacer(modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Heatmap Legend (User's Exact Specification)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("Less", color = Color(0xFF64748B), fontSize = 11.sp)

                    // Missed (Red)
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color(0xFFEF4444))
                    )
                    // Minimal (Yellow)
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color(0xFFFACC15))
                    )
                    // Light Green
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color(0xFF86EFAC))
                    )
                    // Moderate Green
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color(0xFF22C55E))
                    )
                    // Rich Green
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color(0xFF15803D))
                    )

                    Text("More", color = Color(0xFF64748B), fontSize = 11.sp)
                }

                // Today Indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .border(1.5.dp, Color(0xFF059669), RoundedCornerShape(3.dp))
                    )
                    Text("Today", color = Color(0xFF064E3B), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun HeatmapDotCell(
    day: HeatmapDay,
    onClick: () -> Unit
) {
    val isToday = day.isToday
    val borderModifier = if (isToday) {
        Modifier.border(2.dp, Color(0xFF064E3B), RoundedCornerShape(4.dp))
    } else if (day.intensity != HeatmapIntensity.FUTURE && day.intensity != HeatmapIntensity.NOT_STARTED) {
        Modifier.border(0.6.dp, day.intensity.borderColor.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
    } else {
        Modifier.border(0.5.dp, Color(0xFFCBD5E1), RoundedCornerShape(4.dp))
    }

    Box(
        modifier = Modifier
            .size(16.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(day.intensity.color)
            .then(borderModifier)
            .clickable { onClick() }
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// 4. DAYS COLLAPSIBLE MONTHLY BREAKDOWN (Image 3 - Day Mode White & Emerald)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun MonthlyBreakdownCard(
    month: MonthBreakdown,
    isExpanded: Boolean,
    goalMinutes: Int,
    onToggleExpand: () -> Unit,
    onLogDay: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(18.dp), ambientColor = Color(0x10000000))
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(18.dp))
            .padding(vertical = 14.dp, horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row: Month Name, Active Days, Total Mins, Chevron
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleExpand() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Vertical accent green line
                    Box(
                        modifier = Modifier
                            .width(3.5.dp)
                            .height(38.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color(0xFF059669))
                    )

                    Column {
                        Text(
                            text = month.monthTitle,
                            color = Color(0xFF064E3B),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${month.activeDays} Days read",
                            color = Color(0xFF64748B),
                            fontSize = 12.sp
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Minutes capsule
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFD1FAE5))
                            .border(0.8.dp, Color(0xFF10B981), RoundedCornerShape(12.dp))
                            .padding(horizontal = 9.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = Color(0xFF059669),
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "${month.totalMinutes} Mins",
                                color = Color(0xFF065F46),
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand Month",
                        tint = Color(0xFF064E3B),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Expanded Daily Records List
            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 0.8.dp)

                    month.days.forEach { day ->
                        DayRecordRow(
                            record = day,
                            goalMinutes = goalMinutes,
                            onEdit = { onLogDay(day.dateStr) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DayRecordRow(
    record: DayReadingRecord,
    goalMinutes: Int,
    onEdit: () -> Unit
) {
    val progress = (record.minutes.toFloat() / goalMinutes.toFloat()).coerceIn(0f, 1f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFF8FAFC))
            .border(0.8.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
            .clickable { onEdit() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Date & Today tag
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.width(115.dp)
        ) {
            Text(
                text = "${record.dayOfWeekName}, ${record.dayOfMonth} ${record.monthName}",
                color = Color(0xFF1E293B),
                fontSize = 12.5.sp,
                fontWeight = if (record.isToday) FontWeight.Bold else FontWeight.Medium
            )
            if (record.isToday) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFD1FAE5))
                        .padding(horizontal = 5.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = "Today",
                        color = Color(0xFF065F46),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Progress bar towards daily goal
        Box(
            modifier = Modifier
                .weight(1f)
                .height(5.dp)
                .padding(horizontal = 8.dp)
                .clip(RoundedCornerShape(2.5.dp))
                .background(Color(0xFFE2E8F0))
        ) {
            if (record.minutes > 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress.coerceAtLeast(0.06f))
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(2.5.dp))
                        .background(Color(0xFF059669))
                )
            }
        }

        // Minutes text & edit button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "${record.minutes} Minutes",
                color = if (record.minutes > 0) Color(0xFF065F46) else Color(0xFFEF4444),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                tint = Color(0xFF059669),
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// DIALOGS: MANUAL LOG & DAILY GOAL (Day Mode White & Emerald)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun LogReadingMinutesDialog(
    dateStr: String,
    initialMinutes: Int,
    onDismiss: () -> Unit,
    onSave: (Int) -> Unit
) {
    var minutesText by remember { mutableStateOf(if (initialMinutes > 0) "$initialMinutes" else "20") }
    val quickAddOptions = listOf(5, 10, 15, 20, 30, 45)

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = {
            Text(
                text = "Log Quran Read Time",
                color = Color(0xFF064E3B),
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Date: $dateStr",
                    color = Color(0xFF059669),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "Quick Presets:",
                    color = Color(0xFF64748B),
                    fontSize = 12.sp
                )

                // Quick Preset Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    quickAddOptions.take(3).forEach { m ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFECFDF5))
                                .border(1.dp, Color(0xFF10B981), RoundedCornerShape(8.dp))
                                .clickable { minutesText = "$m" }
                                .padding(vertical = 7.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("+$m m", color = Color(0xFF065F46), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    quickAddOptions.drop(3).forEach { m ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFECFDF5))
                                .border(1.dp, Color(0xFF10B981), RoundedCornerShape(8.dp))
                                .clickable { minutesText = "$m" }
                                .padding(vertical = 7.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("+$m m", color = Color(0xFF065F46), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = minutesText,
                    onValueChange = { minutesText = it.filter { ch -> ch.isDigit() }.take(4) },
                    label = { Text("Reading Minutes", color = Color(0xFF059669)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            val mins = minutesText.toIntOrNull() ?: 0
                            onSave(mins)
                        }
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF064E3B),
                        unfocusedTextColor = Color(0xFF1E293B),
                        focusedBorderColor = Color(0xFF059669),
                        unfocusedBorderColor = Color(0xFFCBD5E1),
                        focusedLabelColor = Color(0xFF059669),
                        cursorColor = Color(0xFF059669)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val mins = minutesText.toIntOrNull() ?: 0
                    onSave(mins)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669))
            ) {
                Text("Save", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color(0xFF64748B))
            }
        }
    )
}

@Composable
private fun EditDailyGoalDialog(
    currentGoal: Int,
    onDismiss: () -> Unit,
    onSave: (Int) -> Unit
) {
    var goalText by remember { mutableStateOf("$currentGoal") }
    val presets = listOf(15, 20, 30, 45, 60)

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = {
            Text(
                text = "Set Daily Quran Goal",
                color = Color(0xFF064E3B),
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Select target minutes to read each day:",
                    color = Color(0xFF64748B),
                    fontSize = 13.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    presets.forEach { p ->
                        val isSelected = goalText == "$p"
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) Color(0xFF059669) else Color(0xFFECFDF5))
                                .border(1.dp, Color(0xFF059669), RoundedCornerShape(8.dp))
                                .clickable { goalText = "$p" }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${p}m",
                                color = if (isSelected) Color.White else Color(0xFF065F46),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = goalText,
                    onValueChange = { goalText = it.filter { ch -> ch.isDigit() }.take(3) },
                    label = { Text("Daily Minutes", color = Color(0xFF059669)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            val g = goalText.toIntOrNull() ?: 20
                            onSave(g)
                        }
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF064E3B),
                        unfocusedTextColor = Color(0xFF1E293B),
                        focusedBorderColor = Color(0xFF059669),
                        unfocusedBorderColor = Color(0xFFCBD5E1),
                        focusedLabelColor = Color(0xFF059669),
                        cursorColor = Color(0xFF059669)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val g = goalText.toIntOrNull() ?: 20
                    onSave(g)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669))
            ) {
                Text("Set Goal", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color(0xFF64748B))
            }
        }
    )
}

private fun HeatmapDay.dayOfWeekName(): String {
    return when (dayOfWeek) {
        Calendar.SUNDAY -> "Sunday"
        Calendar.MONDAY -> "Monday"
        Calendar.TUESDAY -> "Tuesday"
        Calendar.WEDNESDAY -> "Wednesday"
        Calendar.THURSDAY -> "Thursday"
        Calendar.FRIDAY -> "Friday"
        Calendar.SATURDAY -> "Saturday"
        else -> ""
    }
}
