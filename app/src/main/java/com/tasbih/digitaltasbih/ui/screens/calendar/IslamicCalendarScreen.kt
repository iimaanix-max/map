package com.tasbih.digitaltasbih.ui.screens.calendar

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import com.tasbih.digitaltasbih.ui.components.AppToast
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
import com.tasbih.digitaltasbih.data.CalendarDayCell
import com.tasbih.digitaltasbih.data.CalendarEvent
import com.tasbih.digitaltasbih.data.CalendarLocalization
import com.tasbih.digitaltasbih.data.CalendarRepository
import com.tasbih.digitaltasbih.data.HijriCalendarEngine
import com.tasbih.digitaltasbih.data.LanguageItem
import com.tasbih.digitaltasbih.data.LocalizationManager
import com.tasbih.digitaltasbih.data.MonthYearOverviewItem
import com.tasbih.digitaltasbih.ui.components.BottomNavigationBar
import com.tasbih.digitaltasbih.ui.components.GeneralSpotlightTourModal
import com.tasbih.digitaltasbih.ui.components.LanguageBar
import com.tasbih.digitaltasbih.ui.components.MoreLanguagesModal
import com.tasbih.digitaltasbih.ui.components.rememberAssetPainter
import com.tasbih.digitaltasbih.data.CalendarTourLocalization
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun IslamicCalendarScreen(
    onBack: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToTasbih: () -> Unit = {},
    onNavigateToQuran: () -> Unit = {},
    onNavigateToDua: () -> Unit = {}
) {
    val context = LocalContext.current

    // Initialize repository
    LaunchedEffect(Unit) {
        CalendarRepository.init(context)
    }

    val prefs = remember { context.getSharedPreferences("dt_app_prefs", Context.MODE_PRIVATE) }
    var selectedLocale by remember {
        mutableStateOf(prefs.getString("app_language", "hi") ?: "hi")
    }
    var isDarkTheme by remember { mutableStateOf(false) }
    var isMoreLangOpen by remember { mutableStateOf(false) }

    val strings = remember(selectedLocale) { CalendarLocalization.get(selectedLocale) }

    // Calendar state
    val todayCal = remember { Calendar.getInstance() }
    val todayIso = remember {
        String.format(Locale.US, "%04d-%02d-%02d", todayCal.get(Calendar.YEAR), todayCal.get(Calendar.MONTH) + 1, todayCal.get(Calendar.DAY_OF_MONTH))
    }

    var viewYear by remember { mutableIntStateOf(todayCal.get(Calendar.YEAR)) }
    var viewMonth by remember { mutableIntStateOf(todayCal.get(Calendar.MONTH)) } // 0-based
    var selectedIso by remember { mutableStateOf(todayIso) }

    var searchQuery by remember { mutableStateOf("") }

    var isTourOpen by remember { mutableStateOf(!CalendarRepository.isTourDone()) }
    val scrollState = rememberScrollState()
    var rootCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var eventCardCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }

    val currentTargetRect: Rect? = remember(
        isTourOpen,
        rootCoords,
        eventCardCoords,
        scrollState.value
    ) {
        val root = rootCoords
        if (root != null && root.isAttached) {
            val target = eventCardCoords
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

    LaunchedEffect(isTourOpen) {
        if (isTourOpen) {
            delay(180L)
            try {
                val root = rootCoords
                val eventCard = eventCardCoords
                if (root != null && root.isAttached && eventCard != null && eventCard.isAttached) {
                    val box = root.localBoundingBoxOf(eventCard, clipBounds = false)
                    val targetY = (box.top.toInt() - 200).coerceIn(0, scrollState.maxValue)
                    scrollState.animateScrollTo(targetY)
                } else {
                    scrollState.animateScrollTo(scrollState.maxValue)
                }
            } catch (_: Exception) {}
        }
    }

    val visibleLanguages: List<LanguageItem> = remember(selectedLocale) {
        val base = listOf(
            LanguageItem("en", "English"),
            LanguageItem("hi", "हिंदी"),
            LanguageItem("ur", "اردو"),
            LanguageItem("ar", "العربية"),
            LanguageItem("bn", "বাংলা")
        )
        if (base.none { it.code == selectedLocale }) {
            val found = LocalizationManager.allLanguageGroups.flatMap { it.languages }.firstOrNull { it.code == selectedLocale }
            if (found != null) base + found else base
        } else {
            base
        }
    }

    // Month cells
    val monthCells = remember(viewYear, viewMonth, selectedIso, selectedLocale) {
        CalendarRepository.getMonthGridDays(viewYear, viewMonth, selectedIso, selectedLocale)
    }

    // Selected cell and date
    val selectedCell = remember(monthCells, selectedIso) {
        monthCells.firstOrNull { it.isoDate == selectedIso }
            ?: monthCells.firstOrNull { it.isCurrentMonth && it.gregorianDay == 1 }
            ?: monthCells.firstOrNull()
    }

    // Selected date object
    val selectedDateObj = remember(selectedIso) {
        try {
            val parts = selectedIso.split("-")
            Calendar.getInstance().apply {
                set(Calendar.YEAR, parts[0].toInt())
                set(Calendar.MONTH, parts[1].toInt() - 1)
                set(Calendar.DAY_OF_MONTH, parts[2].toInt())
            }.time
        } catch (_: Exception) {
            Date()
        }
    }

    // 12 Months overview for Year View
    val yearOverview = remember(viewYear, selectedLocale, viewMonth) {
        CalendarRepository.getYearOverview(
            year = viewYear,
            currentYear = todayCal.get(Calendar.YEAR),
            currentMonth = todayCal.get(Calendar.MONTH),
            activeMonth = viewMonth,
            locale = selectedLocale
        )
    }

    // Search results
    val searchResults = remember(searchQuery, selectedLocale) {
        if (searchQuery.isBlank()) emptyList()
        else CalendarRepository.searchEvents(searchQuery, selectedLocale)
    }

    val monthNameFull = remember(viewYear, viewMonth, selectedLocale) {
        val fullMonths = CalendarLocalization.getGregorianMonthNames(selectedLocale)
        val monthStr = fullMonths.getOrElse(viewMonth) {
            val c = Calendar.getInstance().apply {
                set(Calendar.YEAR, viewYear)
                set(Calendar.MONTH, viewMonth)
                set(Calendar.DAY_OF_MONTH, 1)
            }
            SimpleDateFormat("MMMM", CalendarLocalization.getJavaLocale(selectedLocale)).format(c.time)
        }
        "$monthStr $viewYear".uppercase(CalendarLocalization.getJavaLocale(selectedLocale))
    }

    val dualHijriHeader = remember(viewYear, viewMonth, selectedLocale) {
        HijriCalendarEngine.getDualHijriMonthHeader(viewYear, viewMonth, selectedLocale)
    }

    BackHandler {
        if (isTourOpen) {
            CalendarRepository.setTourDone(true)
            isTourOpen = false
        } else if (searchQuery.isNotBlank()) {
            searchQuery = ""
        } else {
            onBack()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDarkTheme) Color(0xFF081610) else Color(0xFFF6F4EE))
            .onGloballyPositioned { rootCoords = it }
    ) {
        Scaffold(
            topBar = {
                Column(
                    modifier = Modifier.background(if (isDarkTheme) Color(0xFF081610) else Color(0xFFF6F4EE))
                ) {
                    // 1. TOP HEADER BANNER (Screenshot 1: Islamic Calendar with Mosque Silhouette & crescent)
                    CalendarTopHeader(
                        title = strings.calTitle,
                        isDarkTheme = isDarkTheme,
                        onBack = onBack,
                        onToggleTheme = {
                            isDarkTheme = !isDarkTheme
                            AppToast.show(if (isDarkTheme) "Night Theme 🌙" else "Day Theme ☀️")
                        },
                        onSupport = {
                            AppToast.show("Thank you for supporting Digital Tasbih ❤️")
                        }
                    )

                    // 2. LANGUAGE SELECTOR BAR
                    LanguageBar(
                        languages = visibleLanguages,
                        selectedCode = selectedLocale,
                        onSelectLanguage = { code ->
                            selectedLocale = code
                            prefs.edit().putString("app_language", code).apply()
                        },
                        onOpenMoreLanguages = {
                            isMoreLangOpen = true
                        }
                    )
                }
            },
            bottomBar = {
                BottomNavigationBar(
                    activeTabId = "calendar",
                    onTabSelected = { tabId ->
                        when (tabId) {
                            "home" -> onNavigateToHome()
                            "tasbih" -> onNavigateToTasbih()
                            "quran" -> onNavigateToQuran()
                            "dua" -> onNavigateToDua()
                            "calendar" -> { /* Already on calendar */ }
                        }
                    }
                )
            },
            containerColor = if (isDarkTheme) Color(0xFF081610) else Color(0xFFF6F4EE)
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ─── 3. CALENDAR MONTH CARD CONTAINER (Exact Screenshot 1) ───
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isDarkTheme) Color(0xFF0D221A) else Color(0xFFFFFFFF))
                    .border(1.5.dp, Color(0x60C9A84C), RoundedCornerShape(20.dp))
                    .padding(horizontal = 8.dp, vertical = 12.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Month Navigation Bar (Prev < , Center Titles , Next >)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Prev month <
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(if (isDarkTheme) Color(0xFF081A13) else Color(0xFFFFFFFF))
                                .border(1.5.dp, Color(0x99C9A84C), CircleShape)
                                .clickable {
                                    val c = Calendar.getInstance().apply {
                                        set(Calendar.YEAR, viewYear)
                                        set(Calendar.MONTH, viewMonth)
                                        add(Calendar.MONTH, -1)
                                    }
                                    viewYear = c.get(Calendar.YEAR)
                                    viewMonth = c.get(Calendar.MONTH)
                                    selectedIso = String.format(Locale.US, "%04d-%02d-01", viewYear, viewMonth + 1)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "‹",
                                color = if (isDarkTheme) Color(0xFFF3D48A) else Color(0xFF0E5D47),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }

                        // Center: Month & Year + Dual Hijri Subtitle
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                        ) {
                            Text(
                                text = monthNameFull,
                                color = if (isDarkTheme) Color(0xFFF0EAD6) else Color(0xFF0E5D47),
                                fontSize = 16.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = FontFamily.Serif,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(3.dp))

                            // Hijri subtitle pill
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(if (isDarkTheme) Color(0x26F3D48A) else Color(0x24C9A84C))
                                    .padding(horizontal = 10.dp, vertical = 2.5.dp)
                            ) {
                                Text(
                                    text = dualHijriHeader,
                                    color = if (isDarkTheme) Color(0xFFF3D48A) else Color(0xFF8A6820),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        // Next month >
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(if (isDarkTheme) Color(0xFF081A13) else Color(0xFFFFFFFF))
                                .border(1.5.dp, Color(0x99C9A84C), CircleShape)
                                .clickable {
                                    val c = Calendar.getInstance().apply {
                                        set(Calendar.YEAR, viewYear)
                                        set(Calendar.MONTH, viewMonth)
                                        add(Calendar.MONTH, 1)
                                    }
                                    viewYear = c.get(Calendar.YEAR)
                                    viewMonth = c.get(Calendar.MONTH)
                                    selectedIso = String.format(Locale.US, "%04d-%02d-01", viewYear, viewMonth + 1)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "›",
                                color = if (isDarkTheme) Color(0xFFF3D48A) else Color(0xFF0E5D47),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Row: Today Button & Search Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 1. Green Capsule "Today: Oct 17" Button
                        val todayLabel = remember(todayCal, selectedLocale) {
                            CalendarLocalization.formatTodayButtonLabel(todayCal.time, selectedLocale)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(999.dp))
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(Color(0xFF0E5D47), Color(0xFF083D32))
                                    )
                                )
                                .border(1.5.dp, Color(0xFFC9A84C), RoundedCornerShape(999.dp))
                                .clickable {
                                    viewYear = todayCal.get(Calendar.YEAR)
                                    viewMonth = todayCal.get(Calendar.MONTH)
                                    selectedIso = todayIso
                                }
                                .padding(horizontal = 13.dp, vertical = 8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Text(text = "🗓️", fontSize = 13.sp)
                                Text(
                                    text = "${strings.calToday}: $todayLabel",
                                    color = Color(0xFFFFFFFF),
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 0.3.sp
                                )
                            }
                        }

                        // 2. Search Events Bar
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .clip(RoundedCornerShape(999.dp))
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(Color(0xFFFFFFFF), Color(0xFFFAF4E6))
                                    )
                                )
                                .border(1.5.dp, Color(0xFFC9A84C), RoundedCornerShape(999.dp))
                                .padding(horizontal = 10.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "🔍",
                                    fontSize = 13.sp,
                                    color = Color(0xFF8C6A28)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                BasicTextField(
                                    value = searchQuery,
                                    onValueChange = { searchQuery = it },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    textStyle = TextStyle(
                                        color = Color(0xFF0E5D47),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    cursorBrush = SolidColor(Color(0xFF0E5D47)),
                                    decorationBox = { innerTextField ->
                                        if (searchQuery.isBlank()) {
                                            Text(
                                                text = strings.calSearchPlaceholder,
                                                color = Color(0x990E5D47),
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Normal
                                            )
                                        }
                                        innerTextField()
                                    }
                                )
                                if (searchQuery.isNotBlank()) {
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clip(CircleShape)
                                            .background(Color(0x26C9A84C))
                                            .clickable { searchQuery = "" },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "✕", color = Color(0xFF8C6A28), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    // Live Search Dropdown
                    if (searchQuery.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 240.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isDarkTheme) Color(0xFF0B1B15) else Color(0xFFFFFFFF))
                                .border(1.5.dp, Color(0x66C9A84C), RoundedCornerShape(14.dp))
                                .padding(6.dp)
                        ) {
                            if (searchResults.isEmpty()) {
                                Box(
                                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = strings.calSearchEmpty,
                                        color = Color(0xFF8C6A28),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            } else {
                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    items(searchResults.take(15)) { ev ->
                                        val evColor = ev.getColorParsed()
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(if (isDarkTheme) Color(0xFF081A13) else Color(0xFFFDFBF7))
                                                .border(1.dp, Color(0x33C9A84C), RoundedCornerShape(10.dp))
                                                .clickable {
                                                    // Jump calendar to this event
                                                    searchQuery = ""
                                                    // Search for closest occurrence in this view year
                                                    for (offsetMonth in 0..11) {
                                                        val targetH = HijriCalendarEngine.getHijriDate(viewYear, offsetMonth, 15, selectedLocale)
                                                        if (targetH.monthNumber == ev.m) {
                                                            viewMonth = offsetMonth
                                                            selectedIso = String.format(Locale.US, "%04d-%02d-%02d", viewYear, offsetMonth + 1, ev.d.coerceIn(1, 28))
                                                            break
                                                        }
                                                    }
                                                }
                                                .padding(horizontal = 10.dp, vertical = 8.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                                                Text(
                                                    text = ev.getTitle(selectedLocale),
                                                    color = if (isDarkTheme) Color(0xFFF0EAD6) else Color(0xFF0E5D47),
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                val loc = ev.getLocation(selectedLocale)
                                                val mName = HijriCalendarEngine.getHijriMonthName(ev.m, selectedLocale)
                                                Text(
                                                    text = "${if (loc.isNotBlank()) "$loc • " else ""}${ev.d} $mName",
                                                    color = Color(0xFF8A6820),
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            }
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(evColor.copy(alpha = 0.15f))
                                                    .border(1.dp, evColor.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                                    .padding(horizontal = 7.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = ev.getKindBadge(selectedLocale),
                                                    color = evColor,
                                                    fontSize = 9.5.sp,
                                                    fontWeight = FontWeight.ExtraBold
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Weekday Headers: SUN, MON, TUE, WED, THU, FRI, SAT (or localized)
                    val weekdayNames = remember(selectedLocale) {
                        CalendarLocalization.getWeekdayHeaders(selectedLocale)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        weekdayNames.forEach { wd ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isDarkTheme) Color(0x26F3D48A) else Color(0x24C9A84C))
                                    .padding(vertical = 5.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = wd,
                                    color = if (isDarkTheme) Color(0xFFF3D48A) else Color(0xFF8A6820),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // 7-Column Month Grid (Exact Screenshot 1)
                    val rows = monthCells.chunked(7)
                    Column(
                        verticalArrangement = Arrangement.spacedBy(3.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        rows.forEach { week ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                week.forEach { dayCell ->
                                    CalendarDayCellView(
                                        cell = dayCell,
                                        isDarkTheme = isDarkTheme,
                                        selectedLocale = selectedLocale,
                                        modifier = Modifier.weight(1f),
                                        onClick = {
                                            selectedIso = dayCell.isoDate
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ─── 4. SELECTED DATE DETAILS CARD (Exact Screenshot 1, 2 & 4) ───
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { eventCardCoords = it }
                    .clip(RoundedCornerShape(18.dp))
                    .background(if (isDarkTheme) Color(0xFF0D221A) else Color(0xFFFFFFFF))
                    .border(1.2.dp, Color(0x60C9A84C), RoundedCornerShape(18.dp))
                    .padding(14.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // "SELECTED DATE" uppercase golden label
                    Text(
                        text = strings.calSelected,
                        color = if (isDarkTheme) Color(0xFFF3D48A) else Color(0xFF8A6820),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Full Date Title: e.g. "Saturday, October 17, 2026" or "सोमवार, 26 अक्टूबर 2026"
                    val fullDateHeading = remember(selectedDateObj, selectedLocale) {
                        CalendarLocalization.formatSelectedDateHeading(selectedDateObj, selectedLocale)
                    }
                    Text(
                        text = fullDateHeading,
                        color = if (isDarkTheme) Color(0xFFF0EAD6) else Color(0xFF0E5D47),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Serif
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Gregorian: October 17, 2026 or 26 अक्टूबर 2026
                    val gregText = remember(selectedDateObj, selectedLocale) {
                        CalendarLocalization.formatGregorianDate(selectedDateObj, selectedLocale)
                    }
                    Text(
                        text = "${strings.calGreg}: $gregText",
                        color = if (isDarkTheme) Color(0xFFC9BDA7) else Color(0xFF2E2417),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    // Hijri: 4 Jamadi ul Awwal 1448 AH
                    val hijriText = selectedCell?.hijriDate?.formatDisplay(strings.calHijriSuffix) ?: ""
                    Text(
                        text = "${strings.calHijri}: $hijriText",
                        color = if (isDarkTheme) Color(0xFFC9BDA7) else Color(0xFF2E2417),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // "EVENTS ON THIS DATE" label
                    Text(
                        text = strings.calEventsOnDate,
                        color = if (isDarkTheme) Color(0xFFF3D48A) else Color(0xFF8A6820),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val events = selectedCell?.events.orEmpty()
                    if (events.isEmpty()) {
                        // Empty State (Screenshot 2)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isDarkTheme) Color(0x14F3D48A) else Color(0x12C9A84C))
                                .border(1.dp, Color(0x40C9A84C), RoundedCornerShape(14.dp))
                                .padding(14.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = strings.calEvent,
                                    color = Color(0xFFC9A84C),
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = strings.calEventNone,
                                    color = if (isDarkTheme) Color(0xFFF3D48A) else Color(0xFF8A6820),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = strings.calEventMore,
                                    color = if (isDarkTheme) Color(0xFFC9BDA7) else Color(0xFF5D4D38),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }
                    } else {
                        // Event List / Card (Screenshot 4: e.g. Hazrat Makhdoom Ali Mahimi RA)
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            events.forEach { ev ->
                                EventDetailsCard(
                                    event = ev,
                                    hijriDay = selectedCell?.hijriDate?.day ?: ev.d,
                                    hijriMonthName = selectedCell?.hijriDate?.monthName?.takeIf { it.isNotBlank() }
                                        ?: HijriCalendarEngine.getHijriMonthName(ev.m, selectedLocale),
                                    locale = selectedLocale,
                                    isDarkTheme = isDarkTheme
                                )
                            }
                        }
                    }
                }
            }

            // ─── 5. YEAR VIEW SECTION (Exact Screenshot 2 & 3) ───
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isDarkTheme) Color(0xFF0D221A) else Color(0xFFFFFFFF))
                    .border(1.2.dp, Color(0x4DC9A84C), RoundedCornerShape(20.dp))
                    .padding(horizontal = 10.dp, vertical = 14.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = strings.calYearView,
                        color = if (isDarkTheme) Color(0xFFF0EAD6) else Color(0xFF14241E),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$viewYear",
                        color = if (isDarkTheme) Color(0xFFF3D48A) else Color(0xFF8A6820),
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // 2-Column Grid of 12 Month Cards (Screenshot 2 & 3)
                    val yearRows = yearOverview.chunked(2)
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        yearRows.forEach { rowPair ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowPair.forEach { monthItem ->
                                    MonthOverviewCard(
                                        item = monthItem,
                                        isDarkTheme = isDarkTheme,
                                        eventsSuffix = strings.calMonthEvents,
                                        modifier = Modifier.weight(1f),
                                        onClick = {
                                            viewMonth = monthItem.monthIndex
                                            selectedIso = String.format(Locale.US, "%04d-%02d-01", viewYear, viewMonth + 1)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }

    if (isMoreLangOpen) {
        MoreLanguagesModal(
            selectedCode = selectedLocale,
            onSelectLanguage = { code ->
                selectedLocale = code
                prefs.edit().putString("app_language", code).apply()
                isMoreLangOpen = false
            },
            onDismiss = { isMoreLangOpen = false }
        )
    }

    if (isTourOpen) {
        val tourTx = remember(selectedLocale) { CalendarTourLocalization.get(selectedLocale) }
        GeneralSpotlightTourModal(
            step = 1,
            totalSteps = 1,
            stepLabel = tourTx.step,
            title = tourTx.title,
            description = tourTx.desc,
            nextBtnText = tourTx.done,
            doneBtnText = tourTx.done,
            skipBtnText = tourTx.skip,
            targetRect = currentTargetRect,
            cornerRadiusDp = 18f,
            padDp = 6f,
            onNext = {
                CalendarRepository.setTourDone(true)
                isTourOpen = false
            },
            onDone = {
                CalendarRepository.setTourDone(true)
                isTourOpen = false
            },
            onSkip = {
                CalendarRepository.setTourDone(true)
                isTourOpen = false
            }
        )
    }
}
}

// ══════════════════════════════════════════════════════════════
//  TOP HEADER BAR WITH MOSQUE CRESCENT BANNER
// ══════════════════════════════════════════════════════════════
@Composable
private fun CalendarTopHeader(
    title: String,
    isDarkTheme: Boolean,
    onBack: () -> Unit,
    onToggleTheme: () -> Unit,
    onSupport: () -> Unit
) {
    val topGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF146450),
            Color(0xFF0C4A3A),
            Color(0xFF062018)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
            .background(topGradient)
            .border(
                width = 1.2.dp,
                color = Color(0x66E8C878),
                shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
            )
            .statusBarsPadding()
            .padding(horizontal = 12.dp, vertical = 9.dp)
    ) {
        // Decorative Mosque background illustration (media/6d88c4d6a7d5af94.webp)
        // Positioned ONLY in the top-right corner with horizontal fade gradient so center title is pure & clean
        val bannerPainter = rememberAssetPainter("media/6d88c4d6a7d5af94.webp")
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .matchParentSize()
                .width(200.dp)
                .clip(RoundedCornerShape(bottomEnd = 20.dp))
        ) {
            Image(
                painter = bannerPainter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alignment = Alignment.CenterEnd,
                modifier = Modifier
                    .matchParentSize()
                    .width(200.dp)
                    .align(Alignment.CenterEnd),
                alpha = 0.40f
            )
            // Left-to-right fade mask: hides the bright clouds/moon behind the title
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .width(200.dp)
                    .align(Alignment.CenterEnd)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF0C4A3A),
                                Color(0x990C4A3A),
                                Color.Transparent
                            )
                        )
                    )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Back button: circular dark emerald button with gold border, dead-centered arrow
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color(0x4D000000))
                    .border(1.2.dp, Color(0x99F3D48A), CircleShape)
                    .clickable(onClick = onBack),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFFF3D48A),
                    modifier = Modifier
                        .size(20.dp)
                        .offset(x = 2.dp)
                )
            }

            // Center Title: Crisp white serif title with subtle gold text glow
            Text(
                text = title,
                color = Color(0xFFFFFFFF),
                fontSize = 18.5.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Serif,
                letterSpacing = 0.5.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 6.dp)
            )

            // Right: Heart & Theme Toggle buttons (rounded square with ivory gradient & gold border)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Heart Support
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFFFAF7F0), Color(0xFFEFE5CD))
                            )
                        )
                        .border(1.dp, Color(0xA6C9A84C), RoundedCornerShape(9.dp))
                        .clickable(onClick = onSupport),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "❤️", fontSize = 14.sp)
                }

                // Sun / Moon Theme toggle
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFFFAF7F0), Color(0xFFEFE5CD))
                            )
                        )
                        .border(1.dp, Color(0xA6C9A84C), RoundedCornerShape(9.dp))
                        .clickable(onClick = onToggleTheme),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = if (isDarkTheme) "🌙" else "☀️", fontSize = 14.sp)
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  7-COLUMN CALENDAR DAY CELL VIEW (Screenshots 1 & 4)
// ══════════════════════════════════════════════════════════════
@Composable
private fun CalendarDayCellView(
    cell: CalendarDayCell,
    isDarkTheme: Boolean,
    selectedLocale: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val hasEvent = cell.events.isNotEmpty()
    val event = cell.events.firstOrNull()
    val isToday = cell.isToday
    val isSelected = cell.isSelected
    val isSelectedNotToday = isSelected && !isToday

    // Event dot pulsing animation
    val infiniteTransition = rememberInfiniteTransition(label = "dotPulse")
    val dotScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dotScale"
    )

    // Cell outer border & background logic
    val borderColor = when {
        isSelected -> Color(0xFFC9A84C) // Selected gets yellow/gold cell border
        hasEvent -> Color(0xFFC9A84C)   // Event gets yellow/gold cell border
        cell.isCurrentMonth -> if (isDarkTheme) Color(0xFF10B981) else Color(0xFF0E5D47) // Normal day gets emerald green
        else -> if (isDarkTheme) Color(0x3310B981) else Color(0x380E5D47) // Out of month
    }

    val borderWidth = when {
        isSelected -> 1.8.dp
        hasEvent -> 1.8.dp
        else -> 1.5.dp
    }

    val cellBackground = when {
        hasEvent && isDarkTheme -> Color(0xFF14110A)
        !cell.isCurrentMonth && isDarkTheme -> Color(0xFF060D0A)
        !cell.isCurrentMonth -> Color(0xFFFAFAF9)
        isDarkTheme -> Color(0xFF0B1612)
        else -> Color(0xFFFDFBF7)
    }

    Box(
        modifier = modifier
            .height(74.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(cellBackground)
            .border(borderWidth, borderColor, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp, horizontal = 1.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        // Event dot (top right)
        if (hasEvent && cell.isCurrentMonth) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 2.dp, end = 2.dp)
                    .size(6.dp)
                    .scale(dotScale)
                    .clip(CircleShape)
                    .background(Color(0xFFC9A84C))
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Core circle badge for Day number & Hijri number
            if (isToday) {
                // 1. TODAY (e.g. Day 17): ALWAYS gets the solid emerald green circle with gold border!
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .then(
                            if (isSelected) {
                                Modifier
                                    .border(2.dp, Color(0xFFF0D080), CircleShape)
                                    .padding(1.5.dp)
                            } else Modifier
                        )
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFF0E5D47), Color(0xFF083D32))
                            )
                        )
                        .border(1.5.dp, Color(0xFFC9A84C), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${cell.gregorianDay}",
                            color = Color(0xFFFFFFFF),
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            lineHeight = 14.sp
                        )
                        Text(
                            text = "${cell.hijriDate.day}",
                            color = Color(0xFFF3D48A),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 9.sp
                        )
                    }
                }
            } else if (isSelectedNotToday) {
                // 2. SELECTED (Not Today, e.g. Day 26):
                // Yellow outer halo + inner emerald green circle + dark Greg number & warm gold Hijri number
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .border(1.8.dp, Color(0xFFF0D080), CircleShape) // Yellow outer ring
                        .padding(1.5.dp)
                        .border(2.dp, if (isDarkTheme) Color(0xFF10B981) else Color(0xFF0E5D47), CircleShape), // Inner emerald green ring
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${cell.gregorianDay}",
                            color = if (isDarkTheme) Color(0xFFF0EAD6) else Color(0xFF1A1408),
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            lineHeight = 14.sp
                        )
                        Text(
                            text = "${cell.hijriDate.day}",
                            color = if (isDarkTheme) Color(0xFFC9BDA7) else Color(0xFF8A6820),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 9.sp
                        )
                    }
                }
            } else {
                // 3. REGULAR / EVENT / OUT OF MONTH (Not selected, not today)
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${cell.gregorianDay}",
                            color = when {
                                !cell.isCurrentMonth -> if (isDarkTheme) Color(0x66F0EAD6) else Color(0x661A1408)
                                else -> if (isDarkTheme) Color(0xFFF0EAD6) else Color(0xFF1A1408)
                            },
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            lineHeight = 14.sp
                        )
                        Text(
                            text = "${cell.hijriDate.day}",
                            color = when {
                                !cell.isCurrentMonth -> if (isDarkTheme) Color(0x55C9BDA7) else Color(0x558A6820)
                                else -> if (isDarkTheme) Color(0xFFC9BDA7) else Color(0xFF8A6820)
                            },
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 9.sp
                        )
                    }
                }
            }

            // Event short label (e.g. "Makhdoo...")
            if (hasEvent && cell.isCurrentMonth && event != null) {
                val shortLabel = event.getShort(selectedLocale)
                Text(
                    text = shortLabel,
                    color = if (isDarkTheme) Color(0xFFF3D48A) else Color(0xFF0E5D47),
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 1.dp, vertical = 1.dp)
                )
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  EVENT DETAILS CARD (Exact Screenshot 4)
// ══════════════════════════════════════════════════════════════
@Composable
private fun EventDetailsCard(
    event: CalendarEvent,
    hijriDay: Int,
    hijriMonthName: String,
    locale: String,
    isDarkTheme: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (isDarkTheme) Color(0xFF081A13) else Color(0xFFFFFFFF))
            .border(1.2.dp, Color(0x4DC9A84C), RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // 1. Procedural or Admin Image Banner
            ProceduralShrineBanner(
                event = event,
                locale = locale
            )

            // 2. Event Body
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                // Top Row: Tag badge (e.g. URS) + Right-aligned Hijri Date
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val evColor = event.getColorParsed()
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(evColor.copy(alpha = 0.12f))
                            .border(1.dp, evColor.copy(alpha = 0.35f), RoundedCornerShape(999.dp))
                            .padding(horizontal = 9.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = event.getKindBadge(locale),
                            color = evColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.8.sp
                        )
                    }

                    // Right Hijri date: e.g. "13 JAMADI UL AWWAL"
                    val formattedMonth = if (locale == "en") hijriMonthName.uppercase(Locale.ROOT) else hijriMonthName
                    Text(
                        text = "$hijriDay $formattedMonth",
                        color = if (isDarkTheme) Color(0xFFF3D48A) else Color(0xFF8A6820),
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Title: e.g. "Hazrat Makhdoom Ali Mahimi (RA)"
                Text(
                    text = event.getTitle(locale),
                    color = if (isDarkTheme) Color(0xFFF0EAD6) else Color(0xFF0E5D47),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Serif,
                    lineHeight = 20.sp
                )

                // Location: e.g. "Location: Mahim, Mumbai, Maharashtra, India" or "स्थान: ..."
                val location = event.getLocation(locale)
                if (location.isNotBlank()) {
                    val locationLabel = CalendarLocalization.get(locale).calLocation
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$locationLabel: $location",
                        color = if (isDarkTheme) Color(0xFFC9BDA7) else Color(0xFF2E2417),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Annual Urs count: e.g. "613th Annual Urs"
                val anniv = event.getAnniv(locale)
                if (anniv.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = anniv,
                        color = Color(0xFFC9A84C),
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Description / Brief (if any)
                val brief = event.getBrief(locale)
                if (brief.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isDarkTheme) Color(0x1AF3D48A) else Color(0x14C9A84C))
                            .border(1.dp, Color(0x33C9A84C), RoundedCornerShape(10.dp))
                            .padding(9.dp)
                    ) {
                        Text(
                            text = brief,
                            color = if (isDarkTheme) Color(0xFFF0EAD6) else Color(0xFF2E2417),
                            fontSize = 12.sp,
                            lineHeight = 17.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  YEAR VIEW MONTH CARD (Exact Screenshot 2 & 3)
// ══════════════════════════════════════════════════════════════
@Composable
private fun MonthOverviewCard(
    item: MonthYearOverviewItem,
    isDarkTheme: Boolean,
    eventsSuffix: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val borderColor = when {
        item.isActiveMonth -> if (isDarkTheme) Color(0xFFF3D48A) else Color(0xFF0E5D47)
        item.isCurrentMonth -> Color(0xFFC9A84C)
        else -> Color(0x33C9A84C)
    }

    val cardBg = when {
        item.isActiveMonth -> if (isDarkTheme) Color(0xFF14241C) else Color(0xFFF7FBF8)
        else -> if (isDarkTheme) Color(0xFF081A13) else Color(0xFFFFFFFF)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(cardBg)
            .border(if (item.isActiveMonth) 1.8.dp else 1.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(10.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Top Row: Month name e.g. "Jan" and gold + icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.shortName,
                    color = if (isDarkTheme) Color(0xFFF0EAD6) else Color(0xFF0E5D47),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                // Gold circular button with + (or ✦)
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(Color(0x24C9A84C)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+",
                        color = Color(0xFF8A6820),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Hijri Range: e.g. "Rajabul Murajjab - Shabanul Muazzam 1447 AH"
            Text(
                text = item.hijriRange,
                color = if (isDarkTheme) Color(0xFFC9BDA7) else Color(0xFF7A6840),
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Bottom Pills Row: "January" and "6 events"
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Full Month Name pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(Color(0x1FC9A84C))
                        .border(1.dp, Color(0x33C9A84C), RoundedCornerShape(999.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = item.fullName,
                        color = Color(0xFF8A6820),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Events count pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(Color(0x1F4FA3FF))
                        .border(1.dp, Color(0x334FA3FF), RoundedCornerShape(999.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${item.eventCount} $eventsSuffix",
                        color = Color(0xFF2B78C5),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
