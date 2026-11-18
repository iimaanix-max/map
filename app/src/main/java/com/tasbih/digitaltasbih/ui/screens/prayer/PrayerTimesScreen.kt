package com.tasbih.digitaltasbih.ui.screens.prayer

import com.tasbih.digitaltasbih.util.AdConfig

import com.tasbih.digitaltasbih.data.LanguageItem
import com.tasbih.digitaltasbih.ui.components.LanguageBar
import com.tasbih.digitaltasbih.ui.components.MoreLanguagesModal
import com.tasbih.digitaltasbih.ui.components.TopHeaderButton

import android.app.Activity
import android.content.Context
import com.tasbih.digitaltasbih.ui.components.PermissionHelper
import com.tasbih.digitaltasbih.ui.components.LocationPermissionDialog
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tasbih.digitaltasbih.ui.components.AppToast
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.tasbih.digitaltasbih.R
import com.tasbih.digitaltasbih.util.AdMobInterstitialManager
import com.tasbih.digitaltasbih.data.*
import com.tasbih.digitaltasbih.ui.components.PrayerSpotlightTourModal
import com.tasbih.digitaltasbih.ui.components.AdMobNativeAdCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale
import java.text.SimpleDateFormat

@Composable
fun PrayerTimesScreen(
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var selectedLang by remember { mutableStateOf(PrayerRepository.getLanguage()) }
    var isMoreLangOpen by remember { mutableStateOf(false) }
    var isDarkTheme by remember { mutableStateOf(PrayerRepository.isDarkTheme()) }
    var isSettingsOpen by remember { mutableStateOf(false) }
    var isSoundPickerOpen by remember { mutableStateOf(false) }
    var isTourOpen by remember { mutableStateOf(false) }
    var tourStep by remember { mutableIntStateOf(1) }
    var isLocationPermissionDialogOpen by remember {
        mutableStateOf(PermissionHelper.shouldPromptLocation(context))
    }
    val lazyListState = rememberLazyListState()
    var rootCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var heroCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var timingsCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var jamatCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var trackerCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var timetableCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }

    // First-time auto-launch for Prayer Times Guide (triggers automatically on 1st open)
    LaunchedEffect(isLocationPermissionDialogOpen) {
        if (!isLocationPermissionDialogOpen && !PrayerRepository.isPrayerTourDone()) {
            delay(400L)
            if (!isLocationPermissionDialogOpen && !isTourOpen && !isSettingsOpen) {
                tourStep = 1
                isTourOpen = true
            }
        }
    }

    var settings by remember { mutableStateOf(PrayerRepository.loadSettings()) }
    var todayData by remember {
        val initialDay = PrayerRepository.computeOfflineDay(Calendar.getInstance().time, settings)
        mutableStateOf<PrayerDayData?>(initialDay)
    }
    var meta by remember {
        val initialDay = todayData
        mutableStateOf<PrayerActiveMeta?>(
            if (initialDay != null) PrayerRepository.getActiveMeta(initialDay, selectedLang) else null
        )
    }

    val currentTargetRect: Rect? = remember(
        isTourOpen,
        tourStep,
        rootCoords,
        heroCoords,
        timingsCoords,
        jamatCoords,
        trackerCoords,
        timetableCoords,
        lazyListState.firstVisibleItemIndex,
        lazyListState.firstVisibleItemScrollOffset
    ) {
        val root = rootCoords
        if (root != null && root.isAttached) {
            val target = when (tourStep) {
                1 -> heroCoords
                2 -> timingsCoords
                3 -> jamatCoords
                4 -> trackerCoords
                5 -> timetableCoords
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

    LaunchedEffect(isTourOpen, tourStep, todayData != null) {
        if (isTourOpen && todayData != null) {
            delay(180L)
            when (tourStep) {
                1 -> {
                    try {
                        lazyListState.animateScrollToItem(index = 0, scrollOffset = 0)
                    } catch (_: Exception) {}
                }
                2 -> {
                    try {
                        lazyListState.animateScrollToItem(index = 1, scrollOffset = 0)
                    } catch (_: Exception) {}
                }
                3 -> {
                    val targetPrayerId = meta?.currentPrayer?.id ?: meta?.nextPrayer?.id ?: "Dhuhr"
                    val pList = listOf("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha")
                    val pIdx = pList.indexOf(targetPrayerId).coerceAtLeast(0)
                    try {
                        lazyListState.animateScrollToItem(index = 1 + pIdx, scrollOffset = 0)
                    } catch (_: Exception) {}
                }
                4 -> {
                    val trackerIdx = if (isTourOpen) 6 else 7
                    try {
                        lazyListState.animateScrollToItem(index = trackerIdx, scrollOffset = 0)
                    } catch (_: Exception) {}
                }
                5 -> {
                    val timetableIdx = if (isTourOpen) 7 else 8
                    try {
                        lazyListState.animateScrollToItem(index = timetableIdx, scrollOffset = 0)
                    } catch (_: Exception) {}
                }
            }
        }
    }
    var calendarMonth by remember { mutableStateOf(Calendar.getInstance()) }
    var monthDays by remember { mutableStateOf<List<PrayerDayData>>(emptyList()) }
    var isLoadingMonth by remember { mutableStateOf(false) }
    var isWatchAdPromptOpen by remember { mutableStateOf(false) }
    var isMonthlyTimetableViewOpen by remember { mutableStateOf(false) }

    var refreshTrigger by remember { mutableIntStateOf(0) }
    var jamatTargetId by remember { mutableStateOf<String?>(null) }
    var isCityDialogOpen by remember { mutableStateOf(false) }
    var cityInputText by remember { mutableStateOf("") }

    val strings = remember(selectedLang) { PrayerLocalization.get(selectedLang) }

    fun loadMonthData(cal: Calendar) {
        coroutineScope.launch {
            isLoadingMonth = true
            val m = cal.get(Calendar.MONTH) + 1
            val y = cal.get(Calendar.YEAR)
            val days = PrayerRepository.fetchMonthPrayerTimes(
                year = y,
                month = m
            )
            monthDays = days
            isLoadingMonth = false
        }
    }

    LaunchedEffect(settings, refreshTrigger) {
        coroutineScope.launch {
            val td = PrayerRepository.fetchTodayPrayerTimes()
            todayData = td
            meta = PrayerRepository.getActiveMeta(td, selectedLang)
        }
    }

    var currentNowMins by remember {
        val cal = Calendar.getInstance()
        mutableIntStateOf(cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE))
    }

    // Live 1-second ticking countdown timer for upcoming prayer
    LaunchedEffect(todayData, selectedLang) {
        while (true) {
            delay(1000L)
            val cal = Calendar.getInstance()
            currentNowMins = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
            todayData?.let {
                meta = PrayerRepository.getActiveMeta(it, selectedLang)
            }
        }
    }

    var isUnmarkedAlertActive by remember { mutableStateOf(false) }
    var hasTriggeredUnmarkedScroll by remember { mutableStateOf(false) }

    val dueUnmarkedList = remember(todayData, refreshTrigger, currentNowMins) {
        val td = todayData
        if (td != null) {
            val list = listOf("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha")
            list.filter { pId ->
                val isDone = PrayerRepository.isPrayerDone(pId, td.dateStr)
                if (isDone) false
                else {
                    val rawTime = td.timings[pId] ?: return@filter false
                    val pMins = parseMinsToTotal(rawTime)
                    currentNowMins >= pMins
                }
            }
        } else emptyList()
    }

    // Auto-scroll & red pulse prompt for unmarked due prayers (when tour is not active)
    LaunchedEffect(isTourOpen, todayData != null) {
        if (!isTourOpen && todayData != null && !hasTriggeredUnmarkedScroll) {
            if (dueUnmarkedList.isNotEmpty()) {
                hasTriggeredUnmarkedScroll = true
                delay(350L) // Wait for layout to settle
                try {
                    val trackerIdx = if (isTourOpen) 6 else 7
                    lazyListState.animateScrollToItem(index = trackerIdx, scrollOffset = 0)
                } catch (_: Exception) {}

                // Trigger 6-second red pulse alert
                isUnmarkedAlertActive = true
                delay(6000L)
                isUnmarkedAlertActive = false
            }
        }
    }

    // Immediately stop alert if all due prayers become marked before 6 seconds expire
    LaunchedEffect(dueUnmarkedList.isEmpty()) {
        if (dueUnmarkedList.isEmpty()) {
            isUnmarkedAlertActive = false
        }
    }

    LaunchedEffect(Unit) {
        AdMobInterstitialManager.loadAd(context)
    }

    val activity = context as? Activity

    fun handlePrayerBackWithAd() {
        if (activity != null) {
            AdMobInterstitialManager.showAd(activity, "prayer_back") {
                onBack()
            }
        } else {
            onBack()
        }
    }

    BackHandler {
        if (isTourOpen) {
            PrayerRepository.setPrayerTourDone(true)
            isTourOpen = false
        } else if (isMonthlyTimetableViewOpen) {
            isMonthlyTimetableViewOpen = false
        } else if (isSettingsOpen) {
            isSettingsOpen = false
        } else {
            handlePrayerBackWithAd()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { rootCoords = it }
    ) {
        Scaffold(
            topBar = {
                if (!isMonthlyTimetableViewOpen) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (isDarkTheme) Color(0xFF07150E) else Color(0xFFF6F4EE))
                    ) {
                        PrayerHeaderTopBar(
                            isSettingsView = isSettingsOpen,
                            strings = strings,
                            selectedLang = selectedLang,
                            locationLabel = settings.locationLabel,
                            isDarkTheme = isDarkTheme,
                            isSoundPickerOpen = isSoundPickerOpen,
                            onBack = {
                                if (isSettingsOpen) isSettingsOpen = false else handlePrayerBackWithAd()
                            },
                            onOpenSoundPicker = {
                                isSoundPickerOpen = true
                            },
                            onHelpClick = {
                                tourStep = 1
                                isTourOpen = true
                            },
                            onToggleSettings = {
                                isSettingsOpen = !isSettingsOpen
                            },
                            onToggleTheme = {
                                isDarkTheme = !isDarkTheme
                                PrayerRepository.setDarkTheme(isDarkTheme)
                                AppToast.show(if (isDarkTheme) "Night Theme 🌙" else "Day Theme ☀️")
                            },
                            onLocationClick = {
                                if (!PermissionHelper.hasLocationPermission(context)) {
                                    isLocationPermissionDialogOpen = true
                                } else {
                                    AppToast.show("📍 " + settings.locationLabel)
                                }
                            }
                        )

                        if (!isSettingsOpen) {
                            LanguageBar(
                                languages = listOf(
                                    LanguageItem("en", "English"),
                                    LanguageItem("hi", "हिंदी"),
                                    LanguageItem("ur", "اردو"),
                                    LanguageItem("ar", "العربية"),
                                    LanguageItem("bn", "বাংলা")
                                ),
                                selectedCode = selectedLang,
                                isDarkTheme = isDarkTheme,
                                onSelectLanguage = { code: String ->
                                    selectedLang = code
                                    PrayerRepository.saveLanguage(code)
                                    refreshTrigger++
                                },
                                onOpenMoreLanguages = {
                                    isMoreLangOpen = true
                                }
                            )
                        }
                    }
                }
            },
            containerColor = if (isDarkTheme) Color(0xFF07150E) else Color(0xFFF6F4EE)
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(if (isMonthlyTimetableViewOpen) PaddingValues(0.dp) else paddingValues)
            ) {
                if (isMonthlyTimetableViewOpen) {
                    MonthlyTimetableFullScreenView(
                        calendar = calendarMonth,
                        days = monthDays,
                        isLoading = isLoadingMonth,
                        todayData = todayData,
                        locationLabel = settings.locationLabel,
                        selectedLang = selectedLang,
                        strings = strings,
                        onPrevMonth = {
                            calendarMonth.add(Calendar.MONTH, -1)
                            loadMonthData(calendarMonth)
                        },
                        onNextMonth = {
                            calendarMonth.add(Calendar.MONTH, 1)
                            loadMonthData(calendarMonth)
                        },
                        onOpenSettings = {
                            isMonthlyTimetableViewOpen = false
                            isSettingsOpen = true
                        },
                        onBack = {
                            isMonthlyTimetableViewOpen = false
                        }
                    )
                } else if (isSettingsOpen) {
                    PrayerTimeSettingsView(
                        settings = settings,
                        strings = strings,
                        isDarkTheme = isDarkTheme,
                        onUpdateSettings = { ns ->
                            settings = ns
                            PrayerRepository.saveSettings(ns)
                            PrayerRepository.scheduleAllPrayerNotifications(context)
                            refreshTrigger++
                        },
                        onChangeCity = {
                            cityInputText = settings.locationLabel
                            isCityDialogOpen = true
                        },
                        onOpenPhoneSettings = {
                            try {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts("package", context.packageName, null)
                                }
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                AppToast.show("Cannot open settings")
                            }
                        }
                    )
                } else {
                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // 1. Next Prayer Hero Card (Exact 3D Arched Mosque mockup with dynamic timings)
                        item {
                            meta?.let { activeMeta ->
                                val targetPrayer = activeMeta.nextPrayer ?: activeMeta.currentPrayer
                                val prayerId = targetPrayer?.id ?: "Asr"
                                val soundOn = when (prayerId) {
                                    "Fajr" -> settings.soundFajr
                                    "Dhuhr" -> settings.soundDhuhr
                                    "Asr" -> settings.soundAsr
                                    "Maghrib" -> settings.soundMaghrib
                                    else -> settings.soundIsha
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .onGloballyPositioned { heroCoords = it }
                                ) {
                                    PrayerHeroNextCard(
                                        meta = activeMeta,
                                        strings = strings,
                                        selectedLang = selectedLang,
                                        isSoundOn = soundOn,
                                        onToggleSound = {
                                            val ns = when (prayerId) {
                                                "Fajr" -> settings.copy(soundFajr = !settings.soundFajr)
                                                "Dhuhr" -> settings.copy(soundDhuhr = !settings.soundDhuhr)
                                                "Asr" -> settings.copy(soundAsr = !settings.soundAsr)
                                                "Maghrib" -> settings.copy(soundMaghrib = !settings.soundMaghrib)
                                                else -> settings.copy(soundIsha = !settings.soundIsha)
                                            }
                                            settings = ns
                                            PrayerRepository.saveSettings(ns)
                                            PrayerRepository.scheduleAllPrayerNotifications(context)
                                        }
                                    )
                                }
                            }
                        }

                        // 2. Five Detailed Prayer Cards (All prayer timings)
                        todayData?.let { day ->
                            val prayerList = listOf(
                                Triple("Fajr", "🌅", settings.soundFajr),
                                Triple("Dhuhr", "☀️", settings.soundDhuhr),
                                Triple("Asr", "🌤", settings.soundAsr),
                                Triple("Maghrib", "🌅", settings.soundMaghrib),
                                Triple("Isha", "🌙", settings.soundIsha)
                            )

                            val currentPrayerId = meta?.currentPrayer?.id
                            items(prayerList) { (pId, icon, soundOn) ->
                                val rawTime = day.timings[pId] ?: "00:00"
                                val jamatTime = if (PrayerRepository.isJamatCustomized(pId)) {
                                    PrayerRepository.getJamatTime(pId)
                                } else "00:00"
                                val isDone = PrayerRepository.isPrayerDone(pId, day.dateStr)
                                val isCurrent = (currentPrayerId == pId)

                                Box(
                                    modifier = if (pId == "Fajr") Modifier.onGloballyPositioned { timingsCoords = it } else Modifier
                                ) {
                                    PrayerDetailedCard(
                                        prayerId = pId,
                                        icon = icon,
                                        time = rawTime,
                                    jamatTime = jamatTime,
                                    isSoundOn = soundOn,
                                    isDone = isDone,
                                    isCurrent = isCurrent,
                                    nowMins = currentNowMins,
                                    strings = strings,
                                    selectedLang = selectedLang,
                                    day = day,
                                    isDarkTheme = isDarkTheme,
                                    onToggleSound = {
                                        val ns = when (pId) {
                                            "Fajr" -> settings.copy(soundFajr = !settings.soundFajr)
                                            "Dhuhr" -> settings.copy(soundDhuhr = !settings.soundDhuhr)
                                            "Asr" -> settings.copy(soundAsr = !settings.soundAsr)
                                            "Maghrib" -> settings.copy(soundMaghrib = !settings.soundMaghrib)
                                            else -> settings.copy(soundIsha = !settings.soundIsha)
                                        }
                                        settings = ns
                                        PrayerRepository.saveSettings(ns)
                                        PrayerRepository.scheduleAllPrayerNotifications(context)
                                    },
                                    onOpenJamatPicker = {
                                        jamatTargetId = pId
                                    },
                                    onToggleDone = {
                                        PrayerRepository.togglePrayerDone(pId, day.dateStr)
                                        refreshTrigger++
                                    },
                                    onJamatPositioned = { coords ->
                                        val targetPrayerId = meta?.currentPrayer?.id ?: meta?.nextPrayer?.id ?: "Dhuhr"
                                        if (pId == targetPrayerId || jamatCoords == null) {
                                            jamatCoords = coords
                                        }
                                    }
                                )
                            }
                        }
                    }

                        // 3. Native Video Ad Slot (Only shown when tour is NOT active)
                        if (!isTourOpen) {
                            item {
                                NativeVideoAdCardSlot(isDarkTheme = isDarkTheme)
                            }
                        }

                        // 4. Today's Prayers Tracker Card (Missed / Upcoming tick circles)
                        item {
                            todayData?.let { day ->
                                TodaysPrayersTrackerCard(
                                    modifier = Modifier.onGloballyPositioned { coords ->
                                        trackerCoords = coords
                                    },
                                    defaultDay = day,
                                    strings = strings,
                                    selectedLang = selectedLang,
                                    refreshTrigger = refreshTrigger,
                                    isDarkTheme = isDarkTheme,
                                    isAlertActive = isUnmarkedAlertActive,
                                    dueUnmarkedIds = dueUnmarkedList.toSet(),
                                    onTogglePrayerDone = { pId, dateStr ->
                                        PrayerRepository.togglePrayerDone(pId, dateStr)
                                        refreshTrigger++
                                    }
                                )
                            }
                        }

                        // 5. Monthly Timetable Banner Button (Below Today's Prayers)
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .onGloballyPositioned { timetableCoords = it }
                            ) {
                                MonthlyTimetableButton(
                                    label = strings.monthlyTimetable,
                                    isDarkTheme = isDarkTheme,
                                    onClick = {
                                        isWatchAdPromptOpen = true
                                    }
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            }
        }

        // Jamat Time Modal
        jamatTargetId?.let { pId ->
            todayData?.let { day ->
                val rawTime = day.timings[pId] ?: "05:00"
                SetJamatTimeModal(
                    prayerId = pId,
                    waqtRawTime = rawTime,
                    dateMappedStr = day.dateStr,
                    strings = strings,
                    selectedLang = selectedLang,
                    onDismiss = { jamatTargetId = null },
                    onSave = { newTime ->
                        PrayerRepository.setJamatTime(pId, newTime)
                        refreshTrigger++
                        jamatTargetId = null
                    },
                    onReset = {
                        PrayerRepository.clearJamatTime(pId)
                        refreshTrigger++
                        jamatTargetId = null
                    }
                )
            }
        }

        // Watch Ad to See Monthly Timetable Prompt Modal
        if (isWatchAdPromptOpen) {
            WatchAdMonthlyTimetablePromptModal(
                strings = strings,
                selectedLang = selectedLang,
                onWatchAd = {
                    isWatchAdPromptOpen = false
                    loadMonthData(calendarMonth)
                    if (activity != null) {
                        AdMobInterstitialManager.showAd(activity, "monthly_timetable") {
                            isMonthlyTimetableViewOpen = true
                        }
                    } else {
                        isMonthlyTimetableViewOpen = true
                    }
                },
                onDismiss = {
                    isWatchAdPromptOpen = false
                }
            )
        }

        // Location Permission Required Rationale Dialog (with App Settings intent)
        if (isLocationPermissionDialogOpen) {
            LocationPermissionDialog(
                selectedLang = selectedLang,
                featureName = "Prayer Times",
                onDismiss = {
                    isLocationPermissionDialogOpen = false
                    if (!PrayerRepository.isPrayerTourDone()) {
                        coroutineScope.launch {
                            delay(400L)
                            tourStep = 1
                            isTourOpen = true
                        }
                    }
                }
            )
        }

        // City Search Dialog
        if (isCityDialogOpen) {
            AlertDialog(
                onDismissRequest = { isCityDialogOpen = false },
                title = { Text(text = strings.searchChangeCity, fontWeight = FontWeight.Bold) },
                text = {
                    OutlinedTextField(
                        value = cityInputText,
                        onValueChange = { cityInputText = it },
                        placeholder = { Text(text = "Rajnandgaon, India") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (cityInputText.isNotBlank()) {
                                val ns = settings.copy(
                                    autoLocation = false,
                                    locationLabel = cityInputText.trim()
                                )
                                settings = ns
                                PrayerRepository.saveSettings(ns)
                                refreshTrigger++
                            }
                            isCityDialogOpen = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F5132))
                    ) {
                        Text(text = "OK", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { isCityDialogOpen = false }) {
                        Text(text = if (selectedLang.startsWith("hi")) "रद्द करें" else if (selectedLang.startsWith("ur")) "منسوخ" else "Cancel", color = Color(0xFF64748B))
                    }
                }
            )
        }

        // Spotlight Tour Modal (5 Steps with 25 Languages)
        if (isTourOpen && !isLocationPermissionDialogOpen && !isCityDialogOpen && !isSettingsOpen) {
            PrayerSpotlightTourModal(
                step = tourStep,
                totalSteps = 5,
                lang = selectedLang,
                targetRect = currentTargetRect,
                onNext = {
                    if (tourStep < 5) {
                        tourStep++
                    } else {
                        isTourOpen = false
                        PrayerRepository.setPrayerTourDone(true)
                    }
                },
                onDone = {
                    isTourOpen = false
                    PrayerRepository.setPrayerTourDone(true)
                },
                onSkip = {
                    isTourOpen = false
                    PrayerRepository.setPrayerTourDone(true)
                }
            )
        }

        // Notification Sound Selection Modal
        if (isSoundPickerOpen) {
            PrayerNotificationSoundModal(
                selectedLang = selectedLang,
                isDarkTheme = isDarkTheme,
                onDismiss = { isSoundPickerOpen = false }
            )
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════════
//  TOP BAR
// ══════════════════════════════════════════════════════════════════════════════════
@Composable
fun PrayerHeaderTopBar(
    isSettingsView: Boolean,
    strings: PrayerScreenStrings,
    selectedLang: String,
    locationLabel: String,
    isDarkTheme: Boolean = false,
    isSoundPickerOpen: Boolean = false,
    onBack: () -> Unit,
    onOpenSoundPicker: () -> Unit = {},
    onHelpClick: () -> Unit = {},
    onToggleSettings: () -> Unit,
    onToggleTheme: () -> Unit,
    onLocationClick: () -> Unit = {}
) {
    val topGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0F5132),
            Color(0xFF07301E)
        )
    )

    val calendar = remember { Calendar.getInstance() }
    val isHi = selectedLang.startsWith("hi")
    val isUr = selectedLang.startsWith("ur")

    val dayOfWeekName = when (calendar.get(Calendar.DAY_OF_WEEK)) {
        Calendar.SUNDAY -> if (isHi) "रवि" else if (isUr) "اتوار" else "Sun"
        Calendar.MONDAY -> if (isHi) "सोम" else if (isUr) "پیر" else "Mon"
        Calendar.TUESDAY -> if (isHi) "मंगल" else if (isUr) "منگل" else "Tue"
        Calendar.WEDNESDAY -> if (isHi) "बुध" else if (isUr) "بدھ" else "Wed"
        Calendar.THURSDAY -> if (isHi) "गुरु" else if (isUr) "جمعرات" else "Thu"
        Calendar.FRIDAY -> if (isHi) "शुक्र" else if (isUr) "جمعہ" else "Fri"
        else -> if (isHi) "शनि" else if (isUr) "ہفتہ" else "Sat"
    }

    val monthName = when (calendar.get(Calendar.MONTH)) {
        0 -> if (isHi) "जन॰" else "Jan"
        1 -> if (isHi) "फ़र॰" else "Feb"
        2 -> if (isHi) "मार्च" else "Mar"
        3 -> if (isHi) "अप्रैल" else "Apr"
        4 -> if (isHi) "मई" else "May"
        5 -> if (isHi) "जून" else "Jun"
        6 -> if (isHi) "जुल॰" else "Jul"
        7 -> if (isHi) "अग॰" else "Aug"
        8 -> if (isHi) "सित॰" else "Sep"
        9 -> if (isHi) "अक्टू॰" else "Oct"
        10 -> if (isHi) "नव॰" else "Nov"
        else -> if (isHi) "दिस॰" else "Dec"
    }

    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
    val year = calendar.get(Calendar.YEAR)

    val todayGDate = if (isHi) {
        "आज, $dayOfWeekName, $dayOfMonth $monthName $year"
    } else if (isUr) {
        "آج، $dayOfWeekName، $dayOfMonth $monthName $year"
    } else {
        "Today, $dayOfWeekName, $monthName $dayOfMonth, $year"
    }

    val hijriTriple = remember(calendar, selectedLang) {
        PrayerRepository.approximateHijri(calendar, selectedLang)
    }
    val todayHDate = "${hijriTriple.first} ${hijriTriple.second} ${hijriTriple.third}"

    val displayLoc = if (isHi) {
        locationLabel.replace("India", "भारत").replace("Rajnandgaon", "राजनांदगांव")
    } else locationLabel

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(topGradient)
            .statusBarsPadding()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back Button (Centered Arrow)
            Box(
                modifier = Modifier
                    .requiredSize(42.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(Color(0x3D000000))
                    .border(1.2.dp, Color(0x66F3D48A), CircleShape)
                    .clickable(onClick = onBack),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFFF3D48A),
                    modifier = Modifier.size(20.dp)
                )
            }

            // Title & Location
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    text = if (isSettingsView) strings.prayerTimeSettings else todayGDate,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = todayHDate,
                    fontSize = 11.sp,
                    color = Color(0xFFE2E8F0)
                )
                Text(
                    text = "📍 $displayLoc",
                    fontSize = 10.5.sp,
                    color = Color(0xFFF3D48A),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.clickable { onLocationClick() }
                )
            }

            // Action Buttons: Help Guide, Sound Picker, Theme Toggle, Settings Toggle
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Guide / Help Button (Opens 5-step Spotlight Tour)
                TopHeaderButton(
                    icon = "❓",
                    onClick = onHelpClick,
                    isDarkTheme = isDarkTheme
                )

                // Sound Notification Picker Toggle
                TopHeaderButton(
                    icon = "🔔",
                    onClick = onOpenSoundPicker,
                    isActive = isSoundPickerOpen,
                    isDarkTheme = isDarkTheme
                )

                // Theme Toggle
                TopHeaderButton(
                    icon = if (isDarkTheme) "🌙" else "☀️",
                    onClick = onToggleTheme,
                    isDarkTheme = isDarkTheme
                )

                // Settings Toggle
                TopHeaderButton(
                    icon = "⚙️",
                    onClick = onToggleSettings,
                    isActive = isSettingsView,
                    isDarkTheme = isDarkTheme
                )
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════════
//  NEXT PRAYER HERO CARD (Exact User Screenshot: 3D Arched Mosque, Quran on Rehal & Lanterns)
// ══════════════════════════════════════════════════════════════════════════════════
@Composable
fun PrayerHeroNextCard(
    meta: PrayerActiveMeta,
    strings: PrayerScreenStrings,
    selectedLang: String,
    isSoundOn: Boolean,
    onToggleSound: () -> Unit
) {
    val targetPrayer = meta.nextPrayer ?: meta.currentPrayer
    val targetName = targetPrayer?.getLocalizedName(selectedLang) ?: strings.asr
    val timeDisplay = targetPrayer?.let { format12hLower(it.time) } ?: "4:03 pm"

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1024f / 442f)
            .clip(RoundedCornerShape(22.dp))
    ) {
        val cardWidth = maxWidth
        val cardHeight = maxHeight

        // 1. Pristine Full-Res Background Artwork (3D Golden Arch, Green Domes, Crescent, Open Quran, Lanterns)
        Image(
            painter = painterResource(id = R.drawable.bg_next_prayer_card),
            contentDescription = "Next Prayer Banner",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        // 2. Dynamic Prayer Name & Circular Glowing Speaker Icon Row
        Row(
            modifier = Modifier
                .padding(start = cardWidth * 0.092f, top = cardHeight * 0.245f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = targetName,
                fontSize = (cardHeight.value * 0.185f).sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color(0x88000000),
                        offset = Offset(0f, 2f),
                        blurRadius = 4f
                    )
                )
            )

            Spacer(modifier = Modifier.width(cardWidth * 0.028f))

            // Circular Glowing Speaker Button (Exact Golden 3D Artwork matching user screenshot)
            Image(
                painter = painterResource(
                    id = if (isSoundOn) R.drawable.ic_gold_speaker_on else R.drawable.ic_gold_speaker_off
                ),
                contentDescription = "Toggle Adhan",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(cardHeight * 0.24f)
                    .clip(CircleShape)
                    .clickable(onClick = onToggleSound)
            )
        }

        // 3. Dynamic Prayer Time Text (Large 3D Metallic Gold Gradient with Subtle Drop Shadow)
        val goldBrush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFFFFF9DB),
                Color(0xFFFFDF78),
                Color(0xFFE5B036),
                Color(0xFFB57D18)
            )
        )

        Text(
            text = timeDisplay,
            style = TextStyle(
                brush = goldBrush,
                fontSize = (cardHeight.value * 0.23f).sp,
                fontWeight = FontWeight.ExtraBold,
                shadow = Shadow(
                    color = Color(0x99000000),
                    offset = Offset(0f, 3f),
                    blurRadius = 6f
                )
            ),
            modifier = Modifier
                .padding(start = cardWidth * 0.092f, top = cardHeight * 0.445f)
        )

        // 4. Dynamic Live Ticking Countdown Pill (Adapts dynamically to any text length / language)
        Surface(
            shape = RoundedCornerShape(percent = 50),
            color = Color(0x77021E13), // Deep translucent emerald glass
            border = BorderStroke(
                width = 1.2.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFE5B036),
                        Color(0xFFFFDF78),
                        Color(0xFFE5B036)
                    )
                )
            ),
            modifier = Modifier
                .padding(start = cardWidth * 0.08f, top = cardHeight * 0.735f)
                .wrapContentWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Golden Clock Icon
                Box(
                    modifier = Modifier
                        .size((cardHeight.value * 0.125f).dp)
                        .clip(CircleShape)
                        .border(1.2.dp, Color(0xFFFFDF78), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size((cardHeight.value * 0.075f).dp)) {
                        val center = Offset(size.width / 2, size.height / 2)
                        drawLine(
                            color = Color(0xFFFFDF78),
                            start = center,
                            end = Offset(center.x, center.y - size.height * 0.44f),
                            strokeWidth = 2.2f
                        )
                        drawLine(
                            color = Color(0xFFFFDF78),
                            start = center,
                            end = Offset(center.x + size.width * 0.36f, center.y + size.height * 0.12f),
                            strokeWidth = 2.2f
                        )
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "${strings.next}: ",
                    fontSize = (cardHeight.value * 0.078f).sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6DE1A2)
                )
                Text(
                    text = meta.countdownText,
                    fontSize = (cardHeight.value * 0.078f).sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFFFEEA9)
                )
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════════
//  DETAILED PRAYER CARD WITH SUB-CARD (Exact match with original screenshot)
// ══════════════════════════════════════════════════════════════════════════════════
@Composable
fun PrayerDetailedCard(
    prayerId: String,
    icon: String,
    time: String,
    jamatTime: String,
    isSoundOn: Boolean,
    isDone: Boolean,
    isCurrent: Boolean,
    nowMins: Int,
    strings: PrayerScreenStrings,
    selectedLang: String,
    day: PrayerDayData,
    isDarkTheme: Boolean = false,
    onToggleSound: () -> Unit,
    onOpenJamatPicker: () -> Unit,
    onToggleDone: () -> Unit,
    onJamatPositioned: ((LayoutCoordinates) -> Unit)? = null
) {
    val localizedPrayerName = when (prayerId) {
        "Fajr" -> strings.fajr
        "Dhuhr" -> strings.dhuhr
        "Asr" -> strings.asr
        "Maghrib" -> strings.maghrib
        else -> strings.isha
    }
    val isUnset = jamatTime.contains("00:00") || jamatTime.equals("Unset", ignoreCase = true)
    val displayJamat = if (isUnset) "00:00" else jamatTime

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = if (isDarkTheme) Color(0xFF132820) else Color.White),
        border = if (isCurrent) BorderStroke(1.5.dp, Color(0xFFF3D48A)) else if (isDarkTheme) BorderStroke(1.dp, Color(0xFF1E3D32)) else BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 13.dp)) {
            // Header Row: [Icon] [Name] [Speaker] [Time] ... [Jamat 00:00 ✏️] [Circle]
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: Icon + Name + Sound Icon + Time
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = icon, fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = localizedPrayerName,
                        fontSize = 16.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color(0xFFF1F5F9) else Color(0xFF0F172A),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    SpeakerSoundButton(isSoundOn = isSoundOn, onToggle = onToggleSound)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = format12hLower(time),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color(0xFFE2E8F0) else Color(0xFF0F172A)
                    )
                }

                // Right: Jamat Button + Prayed Done Button
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Jamat clickable text + pencil
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .onGloballyPositioned { coords ->
                                onJamatPositioned?.invoke(coords)
                            }
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onOpenJamatPicker
                            )
                            .padding(vertical = 4.dp, horizontal = 2.dp)
                    ) {
                        Text(
                            text = "${strings.jamat} ",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) Color(0xFFF3D48A) else Color(0xFF0F5132)
                        )
                        Text(
                            text = displayJamat,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isUnset) Color(0xFFEF4444) else if (isDarkTheme) Color(0xFFF3D48A) else Color(0xFF0F5132)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Jamat",
                            tint = if (isUnset) Color(0xFFEF4444) else Color(0xFF0F5132),
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Done checkbox button (Circular ring matching screenshot)
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(if (isDone) Color(0xFF0F5132) else Color.Transparent)
                            .border(1.8.dp, if (isDone) (if (isDarkTheme) Color(0xFFF3D48A) else Color(0xFF0F5132)) else (if (isDarkTheme) Color(0xFF475569) else Color(0xFF94A3B8)), CircleShape)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onToggleDone
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isDone) {
                            Text(text = "✓", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Sub-phases inside prayer card
            when (prayerId) {
                "Fajr" -> {
                    val sunriseRaw = day.timings["Sunrise"] ?: "06:02"
                    val sunriseDisp = format12hLower(sunriseRaw)
                    val tuluEnd = addMins(sunriseRaw, 20)
                    val tuluEndDisp = format12hLower(tuluEnd)
                    val sunriseMins = parseMinsToTotal(sunriseRaw)
                    val tuluEndMins = parseMinsToTotal(tuluEnd)
                    val isTuluActive = nowMins in sunriseMins..tuluEndMins

                    Spacer(modifier = Modifier.height(10.dp))
                    PrayerSubCardContainer(isProhibitedActive = isTuluActive, isDarkTheme = isDarkTheme) {
                        // 1. Tulu al-Shams / Tulu-e-Aftab (Prohibited window)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🌄 " + strings.tuluShams,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isTuluActive) Color(0xFFDC2626) else Color(0xFF1E293B)
                            )
                            if (isTuluActive) {
                                ActiveStatusPill(
                                    label = if (selectedLang.startsWith("hi")) "मकरूह जारी" else if (selectedLang.startsWith("ur")) "مکروہ جاری" else "Makruh Active",
                                    isRed = true
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${strings.start} $sunriseDisp · ${strings.end} $tuluEndDisp",
                            fontSize = 11.sp,
                            color = if (isTuluActive) Color(0xFFDC2626) else Color(0xFF64748B)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // 2. Sunrise Time
                        Text(
                            text = "☀️ " + strings.sunriseTime,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${strings.start} $sunriseDisp",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
                "Dhuhr" -> {
                    val dhuhrRaw = day.timings["Dhuhr"] ?: "11:51"
                    val zawalStart = addMins(dhuhrRaw, -10)
                    val zawalStartDisp = format12hLower(zawalStart)
                    val dhuhrDisp = format12hLower(dhuhrRaw)
                    val zawalStartMins = parseMinsToTotal(zawalStart)
                    val dhuhrMins = parseMinsToTotal(dhuhrRaw)
                    val isZawalActive = nowMins in zawalStartMins..dhuhrMins

                    Spacer(modifier = Modifier.height(10.dp))
                    PrayerSubCardContainer(isProhibitedActive = isZawalActive, isDarkTheme = isDarkTheme) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "☀️ " + strings.zawal,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isZawalActive) Color(0xFFDC2626) else Color(0xFF1E293B)
                            )
                            if (isZawalActive) {
                                ActiveStatusPill(
                                    label = if (selectedLang.startsWith("hi")) "मकरूह जारी" else if (selectedLang.startsWith("ur")) "مکروہ جاری" else "Makruh Active",
                                    isRed = true
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${strings.start} $zawalStartDisp · ${strings.end} $dhuhrDisp",
                            fontSize = 11.sp,
                            color = if (isZawalActive) Color(0xFFDC2626) else Color(0xFF64748B)
                        )
                    }
                }
                "Asr" -> {
                    // In exact match with user reference screenshot: Asr has no subcard!
                }
                "Maghrib" -> {
                    val maghribRaw = day.timings["Maghrib"] ?: "17:39"
                    val ghurubStart = addMins(maghribRaw, -15)
                    val ghurubStartDisp = format12hLower(ghurubStart)
                    val maghribDisp = format12hLower(maghribRaw)
                    val ghurubStartMins = parseMinsToTotal(ghurubStart)
                    val maghribMins = parseMinsToTotal(maghribRaw)
                    val isGhurubActive = nowMins in ghurubStartMins..maghribMins

                    Spacer(modifier = Modifier.height(10.dp))
                    PrayerSubCardContainer(isProhibitedActive = isGhurubActive, isDarkTheme = isDarkTheme) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🌆 " + strings.ghurubShams,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isGhurubActive) Color(0xFFDC2626) else Color(0xFF1E293B)
                            )
                            if (isGhurubActive) {
                                ActiveStatusPill(
                                    label = if (selectedLang.startsWith("hi")) "मकरूह जारी" else if (selectedLang.startsWith("ur")) "مکروہ جاری" else "Makruh Active",
                                    isRed = true
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${strings.start} $ghurubStartDisp · ${strings.end} $maghribDisp",
                            fontSize = 11.sp,
                            color = if (isGhurubActive) Color(0xFFDC2626) else Color(0xFF64748B)
                        )
                    }
                }
                "Isha" -> {
                    val fajrRaw = day.timings["Fajr"] ?: "04:48"
                    val fajrDisp = format12hLower(fajrRaw)
                    val fajrMins = parseMinsToTotal(fajrRaw)
                    val tahajjudStartMins = 90 // 1:30 am
                    val isTahajjudActive = nowMins in tahajjudStartMins..fajrMins

                    Spacer(modifier = Modifier.height(10.dp))
                    PrayerSubCardContainer(isTahajjudActive = isTahajjudActive, isDarkTheme = isDarkTheme) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🌙 " + strings.tahajjud,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isTahajjudActive) Color(0xFF0F5132) else Color(0xFF1E293B)
                            )
                            if (isTahajjudActive) {
                                ActiveStatusPill(
                                    label = if (selectedLang.startsWith("hi")) "तहज्जुद जारी" else if (selectedLang.startsWith("ur")) "تہجد جاری" else "Tahajjud Active",
                                    isRed = false
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${strings.start} 1:30 am · ${strings.end} $fajrDisp",
                            fontSize = 11.sp,
                            color = if (isTahajjudActive) Color(0xFF0F5132) else Color(0xFF64748B)
                        )
                    }
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════════
//  SUB-CARD CONTAINER (Normal Ivory/Cream vs Red Prohibited Pulse vs Green Tahajjud Pulse)
// ══════════════════════════════════════════════════════════════════════════════════
@Composable
fun PrayerSubCardContainer(
    isProhibitedActive: Boolean = false,
    isTahajjudActive: Boolean = false,
    isDarkTheme: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    if (isProhibitedActive) {
        // "jalta hai red red"
        val infiniteTransition = rememberInfiniteTransition("prohibitedPulse")
        val pulseAlpha by infiniteTransition.animateFloat(
            initialValue = 0.45f,
            targetValue = 1.0f,
            animationSpec = infiniteRepeatable(
                animation = tween(700, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "prohibitedAlpha"
        )
        val pulseBorder by infiniteTransition.animateFloat(
            initialValue = 1.4f,
            targetValue = 2.4f,
            animationSpec = infiniteRepeatable(
                animation = tween(700, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "prohibitedBorder"
        )

        val glowColor = Color(0xFFEF4444).copy(alpha = pulseAlpha)
        val borderColor = Color(0xFFDC2626).copy(alpha = pulseAlpha)
        val bgColor = if (isDarkTheme) Color(0xFF2A1010) else Color(0xFFFEF2F2)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(12.dp),
                    spotColor = glowColor,
                    ambientColor = glowColor
                )
                .clip(RoundedCornerShape(12.dp))
                .background(bgColor)
                .border(pulseBorder.dp, borderColor, RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 10.dp),
            content = content
        )
    } else if (isTahajjudActive) {
        // "tahajuud wala jab hota hai toh green"
        val infiniteTransition = rememberInfiniteTransition("tahajjudPulse")
        val pulseAlpha by infiniteTransition.animateFloat(
            initialValue = 0.45f,
            targetValue = 1.0f,
            animationSpec = infiniteRepeatable(
                animation = tween(700, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "tahajjudAlpha"
        )
        val pulseBorder by infiniteTransition.animateFloat(
            initialValue = 1.4f,
            targetValue = 2.4f,
            animationSpec = infiniteRepeatable(
                animation = tween(700, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "tahajjudBorder"
        )

        val glowColor = Color(0xFF10B981).copy(alpha = pulseAlpha)
        val borderColor = Color(0xFF0F5132).copy(alpha = pulseAlpha)
        val bgColor = if (isDarkTheme) Color(0xFF0D281D) else Color(0xFFF0FDF4)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(12.dp),
                    spotColor = glowColor,
                    ambientColor = glowColor
                )
                .clip(RoundedCornerShape(12.dp))
                .background(bgColor)
                .border(pulseBorder.dp, borderColor, RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 10.dp),
            content = content
        )
    } else {
        // Normal State: Exact Ivory/Cream card matching user reference screenshot
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(if (isDarkTheme) Color(0xFF10221A) else Color(0xFFFAF7F2))
                .border(1.dp, if (isDarkTheme) Color(0xFF1E3D30) else Color(0xFFEBE6DC), RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 10.dp),
            content = content
        )
    }
}

@Composable
fun ActiveStatusPill(
    label: String,
    isRed: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition("pillPulse")
    val dotAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dotAlpha"
    )

    val tintColor = if (isRed) Color(0xFFDC2626) else Color(0xFF0F5132)
    val dotColor = if (isRed) Color(0xFFEF4444).copy(alpha = dotAlpha) else Color(0xFF10B981).copy(alpha = dotAlpha)
    val bgPill = if (isRed) Color(0xFFFEE2E2) else Color(0xFFDCFCE7)
    val borderPill = if (isRed) Color(0xFFFCA5A5) else Color(0xFF86EFAC)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgPill)
            .border(0.8.dp, borderPill, RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 2.5.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(dotColor)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = tintColor
            )
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════════
//  NATIVE VIDEO AD CARD SLOT
// ══════════════════════════════════════════════════════════════════════════════════
@Composable
fun NativeVideoAdCardSlot(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = false
) {
    AdMobNativeAdCard(
        adUnitId = AdConfig.NATIVE_VIDEO_AD_UNIT_ID, // Google standard test native video ad unit
        modifier = modifier.fillMaxWidth(),
        isDarkTheme = isDarkTheme
    )
}
@Composable
fun MonthlyTimetableButton(
    label: String,
    isDarkTheme: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = if (isDarkTheme) Color(0xFF132820) else Color.White),
        border = BorderStroke(1.dp, if (isDarkTheme) Color(0xFF1E3D32) else Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "📅", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = label,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isDarkTheme) Color(0xFFF1F5F9) else Color(0xFF0F172A)
                )
            }
            Text(text = "›", fontSize = 20.sp, color = if (isDarkTheme) Color(0xFFF3D48A) else Color(0xFF94A3B8), fontWeight = FontWeight.Bold)
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════════
//  PRAYER TIME SETTINGS VIEW
// ══════════════════════════════════════════════════════════════════════════════════
@Composable
fun PrayerTimeSettingsView(
    settings: PrayerSettingsState,
    strings: PrayerScreenStrings,
    isDarkTheme: Boolean = false,
    onUpdateSettings: (PrayerSettingsState) -> Unit,
    onChangeCity: () -> Unit,
    onOpenPhoneSettings: () -> Unit
) {
    val scroll = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Location Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = if (isDarkTheme) Color(0xFF132820) else Color.White),
            border = BorderStroke(1.dp, if (isDarkTheme) Color(0xFF1E3D32) else Color(0xFFE2E8F0))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "📍 " + strings.locationTitle,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0F5132),
                    letterSpacing = 0.6.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = settings.locationLabel,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = onChangeCity,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F5132)),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(text = "🔍 " + strings.searchChangeCity, fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Calculation Method Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "📐 " + strings.prayerCalculationTitle,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0F5132),
                    letterSpacing = 0.6.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                AvailableCalculationMethods.forEach { method ->
                    val isSelected = method.id == settings.methodId
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) Color(0xFFF0FDF4) else Color.Transparent)
                            .border(1.dp, if (isSelected) Color(0xFF0F5132) else Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
                            .clickable { onUpdateSettings(settings.copy(methodId = method.id)) }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { onUpdateSettings(settings.copy(methodId = method.id)) },
                            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF0F5132))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = method.name, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                            Text(text = method.params, fontSize = 11.5.sp, color = Color(0xFF64748B))
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }

        // Juristic School (Asr Calculation) Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "⚖️ " + strings.asrCalculationTitle,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0F5132),
                    letterSpacing = 0.6.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                listOf(
                    Pair(2, Pair(strings.hanafiTitle, strings.hanafiDesc)),
                    Pair(1, Pair(strings.shafiTitle, strings.shafiDesc))
                ).forEach { (schoolId, labels) ->
                    val isSelected = schoolId == settings.school
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) Color(0xFFF0FDF4) else Color.Transparent)
                            .border(1.dp, if (isSelected) Color(0xFF0F5132) else Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
                            .clickable { onUpdateSettings(settings.copy(school = schoolId)) }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { onUpdateSettings(settings.copy(school = schoolId)) },
                            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF0F5132))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = labels.first, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                            Text(text = labels.second, fontSize = 11.5.sp, color = Color(0xFF64748B))
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }

        // Mosque Adjustment (Tune) Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "⏱️ " + strings.localMosqueAdjustmentTitle,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0F5132),
                    letterSpacing = 0.6.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MosqueTuneBox(label = strings.fajr, value = settings.tune.fajr, modifier = Modifier.weight(1f)) { delta ->
                        onUpdateSettings(settings.copy(tune = settings.tune.copy(fajr = settings.tune.fajr + delta)))
                    }
                    MosqueTuneBox(label = strings.dhuhr, value = settings.tune.dhuhr, modifier = Modifier.weight(1f)) { delta ->
                        onUpdateSettings(settings.copy(tune = settings.tune.copy(dhuhr = settings.tune.dhuhr + delta)))
                    }
                    MosqueTuneBox(label = strings.asr, value = settings.tune.asr, modifier = Modifier.weight(1f)) { delta ->
                        onUpdateSettings(settings.copy(tune = settings.tune.copy(asr = settings.tune.asr + delta)))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MosqueTuneBox(label = strings.maghrib, value = settings.tune.maghrib, modifier = Modifier.weight(1f)) { delta ->
                        onUpdateSettings(settings.copy(tune = settings.tune.copy(maghrib = settings.tune.maghrib + delta)))
                    }
                    MosqueTuneBox(label = strings.isha, value = settings.tune.isha, modifier = Modifier.weight(1f)) { delta ->
                        onUpdateSettings(settings.copy(tune = settings.tune.copy(isha = settings.tune.isha + delta)))
                    }
                    MosqueTuneBox(label = strings.sunriseTime, value = settings.tune.sunrise, modifier = Modifier.weight(1f)) { delta ->
                        onUpdateSettings(settings.copy(tune = settings.tune.copy(sunrise = settings.tune.sunrise + delta)))
                    }
                }
            }
        }

        // Notifications Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "🔔 " + if (strings.prayerTimeSettings.contains("सेटिंग")) "नमाज़ नोटिफिकेशन" else if (strings.prayerTimeSettings.contains("سیٹنگ")) "نماز نوٹیفیکیشن" else "Prayer Notifications",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0F5132),
                    letterSpacing = 0.6.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = strings.fixedNotifTitle, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                        Text(text = strings.fixedNotifDesc, fontSize = 11.sp, color = Color(0xFF64748B))
                    }
                    Switch(
                        checked = settings.fixedNotificationEnabled,
                        onCheckedChange = { onUpdateSettings(settings.copy(fixedNotificationEnabled = it)) },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFF0F5132))
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = onOpenPhoneSettings,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "⚙️ " + strings.openPhoneSettings, fontSize = 12.sp, color = Color(0xFF334155), fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

// ══════════════════════════════════════════════════════════════════════════════════
//  MOSQUE TUNE BOX
// ══════════════════════════════════════════════════════════════════════════════════
@Composable
fun MosqueTuneBox(
    label: String,
    value: Int,
    modifier: Modifier = Modifier,
    onDelta: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF475569)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .requiredSize(26.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(Color(0xFFE2E8F0))
                    .clickable { onDelta(-1) },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "–", color = Color(0xFF475569), fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "${if (value > 0) "+$value" else "$value"} m",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (value != 0) Color(0xFF0F5132) else Color(0xFF1E293B)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Box(
                modifier = Modifier
                    .requiredSize(26.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(Color(0xFFE2E8F0))
                    .clickable { onDelta(1) },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "+", color = Color(0xFF475569), fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════════
//  HELPER FUNCTIONS
// ══════════════════════════════════════════════════════════════════════════════════
fun getGregorianMonthNameLocalized(month: Int, lang: String): String {
    val isHi = lang.startsWith("hi")
    val isUr = lang.startsWith("ur")
    val hiNames = listOf("जनवरी", "फ़रवरी", "मार्च", "अप्रैल", "मई", "जून", "जुलाई", "अगस्त", "सितंबर", "अक्टूबर", "नवंबर", "दिसंबर")
    val urNames = listOf("جنوری", "فروری", "مارچ", "اپریل", "مئی", "جون", "جولائی", "اگست", "ستمبر", "اکتوبر", "نومبر", "دسمبر")
    val enNames = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
    val idx = month.coerceIn(0, 11)
    return if (isHi) hiNames[idx] else if (isUr) urNames[idx] else enNames[idx]
}

fun getGregorianMonthShortLocalized(month: Int, lang: String): String {
    val isHi = lang.startsWith("hi")
    val isUr = lang.startsWith("ur")
    val hiShort = listOf("जन॰", "फ़र॰", "मार्च", "अप्रै॰", "मई", "जून", "जुला॰", "अग॰", "सित॰", "ऑक्टू॰", "नव॰", "दिस॰")
    val urShort = listOf("جنوری", "فروری", "مارچ", "اپریل", "مئی", "جون", "جولائی", "اگست", "ستمبر", "اکتوبر", "نومبر", "دسمبر")
    val enShort = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    val idx = month.coerceIn(0, 11)
    return if (isHi) hiShort[idx] else if (isUr) urShort[idx] else enShort[idx]
}

fun getHijriMonthNameLocalized(hijriName: String, lang: String): String {
    val isHi = lang.startsWith("hi")
    val isUr = lang.startsWith("ur")
    if (!isHi && !isUr) return hijriName
    val lower = hijriName.lowercase()
    if (lower.contains("muharram")) return if (isHi) "मुहर्रम" else "محرم"
    if (lower.contains("safar")) return if (isHi) "सफ़र" else "صفر"
    if (lower.contains("rabi") && (lower.contains("awwal") || lower.contains("1"))) return if (isHi) "रबीउल अव्वल" else "ربیع الاول"
    if (lower.contains("rabi")) return if (isHi) "रबीउल आखिर" else "ربیع الثانی"
    if ((lower.contains("jumada") && (lower.contains("awwal") || lower.contains("ula") || lower.contains("1"))) || (lower.contains("jamadi") && lower.contains("awwal"))) return if (isHi) "जमादिउल अव्वल" else "جمادی الاول"
    if (lower.contains("jumada") || lower.contains("jamadi")) return if (isHi) "जमादिउस सानी" else "جمادی الثانی"
    if (lower.contains("rajab")) return if (isHi) "रजब" else "رجب"
    if (lower.contains("sha")) return if (isHi) "शाबान" else "شعبان"
    if (lower.contains("ramadan") || lower.contains("ramzan")) return if (isHi) "रमज़ान" else "رمضان"
    if (lower.contains("shawwal")) return if (isHi) "शव्वाल" else "شوال"
    if (lower.contains("qi")) return if (isHi) "ज़ुल क़ादा" else "ذوالقعدہ"
    if (lower.contains("hij")) return if (isHi) "ज़ुल हिज्जा" else "ذوالحجہ"
    return hijriName
}

fun format12hLower(time24: String): String {
    val parts = time24.split(":")
    if (parts.size < 2) return time24
    val h = parts[0].trim().toIntOrNull() ?: 0
    val m = parts[1].trim().toIntOrNull() ?: 0
    val ampm = if (h >= 12) "pm" else "am"
    val h12 = if (h % 12 == 0) 12 else h % 12
    return String.format(Locale.US, "%d:%02d %s", h12, m, ampm)
}

fun format12hUpper(time24: String): String = format12hLower(time24)

fun addMins(time24: String, minutes: Int): String {
    val parts = time24.split(":")
    if (parts.size < 2) return time24
    val h = parts[0].trim().toIntOrNull() ?: 0
    val m = parts[1].trim().toIntOrNull() ?: 0
    var total = (h * 60) + m + minutes
    while (total < 0) total += 1440
    val total2 = total % 1440
    return String.format(Locale.US, "%02d:%02d", total2 / 60, total2 % 60)
}

fun parseMinsToTotal(time24: String): Int {
    val clean = time24.trim().split(" ")[0]
    val parts = clean.split(":")
    if (parts.size < 2) return 0
    val h = parts[0].trim().toIntOrNull() ?: 0
    val m = parts[1].trim().toIntOrNull() ?: 0
    return (h * 60) + m
}


@Composable
fun TodaysPrayersTrackerCard(
    modifier: Modifier = Modifier,
    defaultDay: PrayerDayData,
    strings: PrayerScreenStrings,
    selectedLang: String,
    refreshTrigger: Int = 0,
    isDarkTheme: Boolean = false,
    isAlertActive: Boolean = false,
    dueUnmarkedIds: Set<String> = emptySet(),
    onTogglePrayerDone: (String, String) -> Unit
) {
    var dayOffset by remember { mutableStateOf(0) }

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

    val targetHijriStr = remember(targetDateCal) {
        val (hDay, hMonth, hYear) = PrayerRepository.approximateHijri(targetDateCal)
        "$hDay $hMonth $hYear AH"
    }

    val currentDayData = remember(targetDateCal, refreshTrigger) {
        if (isViewingToday) defaultDay
        else PrayerRepository.computeOfflineDay(targetDateCal.time, PrayerRepository.loadSettings())
    }

    val prayers = listOf(
        Triple("Fajr", "🌅", currentDayData.timings["Fajr"] ?: "04:46"),
        Triple("Dhuhr", "☀️", currentDayData.timings["Dhuhr"] ?: "11:51"),
        Triple("Asr", "🌤", currentDayData.timings["Asr"] ?: "16:04"),
        Triple("Maghrib", "🌇", currentDayData.timings["Maghrib"] ?: "17:42"),
        Triple("Isha", "🌙", currentDayData.timings["Isha"] ?: "18:56")
    )

    val now = Calendar.getInstance()
    val nowMins = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE)

    val infiniteTransition = rememberInfiniteTransition("unmarkedRedAlert")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.45f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(550, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.045f,
        animationSpec = infiniteRepeatable(
            animation = tween(550, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val isShowingAlertOnContainer = isAlertActive && isViewingToday && dueUnmarkedIds.isNotEmpty()

    val containerModifier = if (isShowingAlertOnContainer) {
        modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(18.dp),
                spotColor = Color(0xFFEF4444).copy(alpha = 0.5f * pulseAlpha),
                ambientColor = Color(0xFFEF4444).copy(alpha = 0.35f * pulseAlpha)
            )
            .clip(RoundedCornerShape(18.dp))
            .background(if (isDarkTheme) Color(0xFF1B1212) else Color(0xFFFFFBFB))
            .drawWithContent {
                drawContent()
                val strokeWidth = 2.2.dp.toPx()
                val cornerRadiusPx = 18.dp.toPx()
                val dashEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 10f), 0f)
                val path = Path().apply {
                    addRoundRect(
                        RoundRect(
                            left = strokeWidth / 2f,
                            top = strokeWidth / 2f,
                            right = size.width - strokeWidth / 2f,
                            bottom = size.height - strokeWidth / 2f,
                            cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
                        )
                    )
                }
                drawPath(
                    path = path,
                    color = Color(0xFFEF4444).copy(alpha = pulseAlpha),
                    style = Stroke(width = strokeWidth, pathEffect = dashEffect)
                )
            }
            .padding(horizontal = 12.dp, vertical = 10.dp)
    } else {
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(if (isDarkTheme) Color(0xFF132820) else Color.White)
            .border(1.dp, if (isDarkTheme) Color(0xFF1E3D32) else Color(0xFFE2E8F0), RoundedCornerShape(18.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp)
    }

    Column(
        modifier = containerModifier
    ) {
        // Header with [<] Today's Prayers [>] on the left and Date on the right (Exact Image 4)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Group: [<] Today's Prayers [>]
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Previous Day Button (<)
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(if (isDarkTheme) Color(0xFF1E382B) else Color.White)
                        .border(1.2.dp, if (isDarkTheme) Color(0xFFF3D48A) else Color(0xFF146450), CircleShape)
                        .clickable { dayOffset++ },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "‹",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color(0xFFF3D48A) else Color(0xFF146450),
                        modifier = Modifier.offset(y = (-1).dp)
                    )
                }

                Text(
                    text = if (isViewingToday) strings.todaysPrayers else dateDisplayLabel,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) Color(0xFFF1F5F9) else Color(0xFF0F172A)
                )

                // Next Day Button (>)
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(if (isDarkTheme) Color(0xFF1E382B) else Color.White)
                        .border(
                            1.2.dp,
                            if (isViewingToday) (if (isDarkTheme) Color(0xFF334155) else Color(0xFFCBD5E1)) else (if (isDarkTheme) Color(0xFFF3D48A) else Color(0xFF146450)),
                            CircleShape
                        )
                        .clickable(enabled = !isViewingToday) { if (dayOffset > 0) dayOffset-- },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "›",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isViewingToday) (if (isDarkTheme) Color(0xFF475569) else Color(0xFFCBD5E1)) else (if (isDarkTheme) Color(0xFFF3D48A) else Color(0xFF146450)),
                        modifier = Modifier.offset(y = (-1).dp)
                    )
                }
            }

            // Right: Date String
            Text(
                text = "$dateDisplayLabel / $targetHijriStr",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF475569)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 5 Vertical Cards Side-by-Side (Compact & Proportional, matching Image 4)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            prayers.forEach { (pId, icon, rawTime) ->
                val isDone = remember(refreshTrigger, pId, targetDateStr) { PrayerRepository.isPrayerDone(pId, targetDateStr) }
                val pName = when (pId) {
                    "Fajr" -> strings.fajr
                    "Dhuhr" -> strings.dhuhr
                    "Asr" -> strings.asr
                    "Maghrib" -> strings.maghrib
                    else -> strings.isha
                }

                val pMins = parseMinsToTotal(rawTime)
                val isPast = if (!isViewingToday) true else (nowMins >= pMins)
                val isDueUnmarkedAlert = isAlertActive && isViewingToday && (pId in dueUnmarkedIds) && isPast && !isDone

                // Normal resting colors vs. Alert pulse colors
                val cardBg = if (isDone) {
                    if (isDarkTheme) Color(0xFF0D281D) else Color(0xFFF0FDF4)
                } else if (isDueUnmarkedAlert) {
                    if (isDarkTheme) Color(0xFF2E1212) else Color(0xFFFEE2E2)
                } else if (isPast) {
                    if (isDarkTheme) Color(0xFF221414) else Color(0xFFFFF5F5)
                } else {
                    if (isDarkTheme) Color(0xFF14241C) else Color(0xFFFFFDF7)
                }

                val borderColor = if (isDone) {
                    Color(0xFF0E5D47)
                } else if (isDueUnmarkedAlert) {
                    Color(0xFFEF4444).copy(alpha = pulseAlpha)
                } else if (isPast) {
                    if (isDarkTheme) Color(0xFF7F1D1D) else Color(0xFFFECACA)
                } else {
                    if (isDarkTheme) Color(0xFFB45309) else Color(0xFFFDE68A)
                }

                val cardBorderWidth = if (isDueUnmarkedAlert) 2.dp else 1.dp
                val cardScale = if (isDueUnmarkedAlert) pulseScale else 1f

                val cardItemModifier = if (isDueUnmarkedAlert) {
                    Modifier
                        .weight(1f)
                        .scale(cardScale)
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(12.dp),
                            spotColor = Color(0xFFEF4444).copy(alpha = 0.5f * pulseAlpha),
                            ambientColor = Color(0xFFEF4444).copy(alpha = 0.3f * pulseAlpha)
                        )
                        .clip(RoundedCornerShape(12.dp))
                        .background(cardBg)
                        .border(cardBorderWidth, borderColor, RoundedCornerShape(12.dp))
                        .clickable { onTogglePrayerDone(pId, targetDateStr) }
                        .padding(vertical = 6.dp, horizontal = 2.dp)
                } else {
                    Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(cardBg)
                        .border(cardBorderWidth, borderColor, RoundedCornerShape(12.dp))
                        .clickable { onTogglePrayerDone(pId, targetDateStr) }
                        .padding(vertical = 6.dp, horizontal = 2.dp)
                }

                Box(
                    modifier = cardItemModifier,
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        // Circular dark green badge - strictly 1:1 circular!
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF0F5132)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = icon, fontSize = 13.sp)
                        }

                        Text(
                            text = pName,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) Color(0xFFF1F5F9) else Color(0xFF0F172A),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = format12hUpper(rawTime),
                            fontSize = 8.5.sp,
                            color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF475569),
                            maxLines = 1
                        )

                        Spacer(modifier = Modifier.height(1.dp))

                        // Status circle - strictly 1:1 circular!
                        if (isDone) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF0E5D47)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "✓",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                            Text(text = strings.prayed, fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0E5D47), maxLines = 1)
                        } else if (isDueUnmarkedAlert) {
                            // Special glowing/pulsing missed circle during alert (Matching original CSS prv-unmarked-red-blink)
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEF4444).copy(alpha = 0.22f))
                                    .border(2.2.dp, Color(0xFFEF4444).copy(alpha = pulseAlpha), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFEF4444).copy(alpha = pulseAlpha))
                                )
                            }
                            Text(
                                text = strings.missed,
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFEF4444),
                                maxLines = 1
                            )
                        } else if (isPast) {
                            // Normal resting missed circle (completely untouched, preserving existing styling!)
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .border(1.8.dp, Color(0xFFEF4444), CircleShape)
                            )
                            Text(text = strings.missed, fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEF4444), maxLines = 1)
                        } else {
                            // Upcoming prayer: Amber/Orange outline & text matching Image 4!
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .border(1.8.dp, Color(0xFFD97706), CircleShape)
                            )
                            Text(text = strings.upcoming, fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD97706), maxLines = 1)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SpeakerSoundButton(
    isSoundOn: Boolean,
    onToggle: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onToggle
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(17.dp, 15.dp)) {
            val strokeColor = if (isSoundOn) Color(0xFF0F5132) else Color(0xFF94A3B8)
            val w = size.width
            val h = size.height
            val strokeW = 1.35.dp.toPx()

            // Speaker body rectangle + cone polygon
            val bodyLeft = 0.5f
            val bodyRight = w * 0.28f
            val bodyTop = h * 0.26f
            val bodyBottom = h * 0.74f

            val conePath = Path().apply {
                moveTo(bodyLeft, bodyTop)
                lineTo(bodyRight, bodyTop)
                lineTo(w * 0.58f, 0.5f)
                lineTo(w * 0.58f, h - 0.5f)
                lineTo(bodyRight, bodyBottom)
                lineTo(bodyLeft, bodyBottom)
                close()
            }
            drawPath(
                path = conePath,
                color = strokeColor,
                style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            if (isSoundOn) {
                // Sound Wave 1 (inner arc)
                drawArc(
                    color = strokeColor,
                    startAngle = -42f,
                    sweepAngle = 84f,
                    useCenter = false,
                    topLeft = Offset(w * 0.42f, h * 0.22f),
                    size = Size(w * 0.30f, h * 0.56f),
                    style = Stroke(width = strokeW, cap = StrokeCap.Round)
                )
                // Sound Wave 2 (outer arc)
                drawArc(
                    color = strokeColor,
                    startAngle = -45f,
                    sweepAngle = 90f,
                    useCenter = false,
                    topLeft = Offset(w * 0.46f, h * 0.08f),
                    size = Size(w * 0.48f, h * 0.84f),
                    style = Stroke(width = strokeW, cap = StrokeCap.Round)
                )
            } else {
                // Muted state: Clean diagonal red slash
                drawLine(
                    color = Color(0xFFDC2626),
                    start = Offset(w * 0.05f, h * 0.92f),
                    end = Offset(w * 0.95f, h * 0.08f),
                    strokeWidth = 1.6.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
    }
}

@Composable
fun SetJamatTimeModal(
    prayerId: String,
    waqtRawTime: String,
    dateMappedStr: String,
    strings: PrayerScreenStrings,
    selectedLang: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
    onReset: () -> Unit
) {
    val context = LocalContext.current
    val localizedPrayerName = when (prayerId) {
        "Fajr" -> strings.fajr
        "Dhuhr" -> strings.dhuhr
        "Asr" -> strings.asr
        "Maghrib" -> strings.maghrib
        else -> strings.isha
    }

    val waqtParts = waqtRawTime.split(":")
    val baseH = waqtParts.getOrNull(0)?.trim()?.toIntOrNull() ?: 5
    val baseM = waqtParts.getOrNull(1)?.trim()?.toIntOrNull() ?: 0

    val waqtDisplay = format12hLower(waqtRawTime)

    val existingJamat = if (PrayerRepository.isJamatCustomized(prayerId)) PrayerRepository.getJamatTime(prayerId) else null

    var selectedHour by remember {
        mutableStateOf(
            if (existingJamat != null && existingJamat.contains(":")) {
                val p = existingJamat.split(":")
                p[0].trim().toIntOrNull() ?: ((baseH * 60 + baseM + 15) / 60 % 24)
            } else {
                (baseH * 60 + baseM + 15) / 60 % 24
            }
        )
    }

    var selectedMinute by remember {
        mutableStateOf(
            if (existingJamat != null && existingJamat.contains(":")) {
                val p = existingJamat.split(":")
                p[1].trim().split(" ")[0].toIntOrNull() ?: ((baseH * 60 + baseM + 15) % 60)
            } else {
                (baseH * 60 + baseM + 15) % 60
            }
        )
    }

    var activePreset by remember {
        mutableStateOf<Int?>(if (existingJamat == null) 15 else null)
    }

    var showDropdown by remember { mutableStateOf(false) }

    val h12 = if (selectedHour % 12 == 0) 12 else selectedHour % 12
    val isPm = selectedHour >= 12

    var hourInputText by remember(selectedHour) {
        mutableStateOf(String.format(Locale.US, "%02d", h12))
    }
    var minuteInputText by remember(selectedMinute) {
        mutableStateOf(String.format(Locale.US, "%02d", selectedMinute))
    }

    val hourListState = rememberLazyListState()
    val minuteListState = rememberLazyListState()

    LaunchedEffect(showDropdown) {
        if (showDropdown) {
            val targetH = maxOf(0, h12 - 2)
            val targetM = maxOf(0, selectedMinute - 2)
            try {
                hourListState.scrollToItem(targetH)
                minuteListState.scrollToItem(targetM)
            } catch (_: Exception) {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x99000000))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onDismiss
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {}
                .padding(20.dp)
        ) {
            // Header: 🕌 Set Jamat Time • फ़ज्र          ✕
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🕌 Set Jamat Time • \${localizedPrayerName}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onDismiss),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✕",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF64748B)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mint Info Box: Waqt 4:48 am & 18 अक्टू॰ mapped
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFF0FDF4))
                    .border(1.dp, Color(0xFFD1FAE5), RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Waqt \${waqtDisplay} & \${dateMappedStr} mapped",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF92400E)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Waqt Start Time: ",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF92400E)
                        )
                        Text(
                            text = waqtDisplay,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF047857)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Label: Jamat Time (Masjid Time):
            Text(
                text = "Jamat Time (Masjid Time):",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Box Container for the time input and anchored dropdown popup
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Interactive Time Input Box
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White)
                        .border(
                            width = if (showDropdown) 2.dp else 1.5.dp,
                            color = if (showDropdown) Color(0xFF1E88E5) else Color(0xFFE2D6C0),
                            shape = RoundedCornerShape(14.dp)
                        )
                        .clickable { showDropdown = !showDropdown }
                        .padding(horizontal = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left editable numbers + colon + AM/PM
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Hour Field
                        BasicTextField(
                            value = hourInputText,
                            onValueChange = { newVal ->
                                val clean = newVal.filter { it.isDigit() }.take(2)
                                hourInputText = clean
                                val num = clean.toIntOrNull()
                                if (num != null && num in 1..12) {
                                    val new24H = if (isPm) {
                                        if (num == 12) 12 else num + 12
                                    } else {
                                        if (num == 12) 0 else num
                                    }
                                    selectedHour = new24H
                                    activePreset = null
                                }
                            },
                            textStyle = TextStyle(
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF0F172A),
                                textAlign = TextAlign.Center
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier
                                .width(34.dp)
                                .clickable { /* allow keyboard focus */ }
                        )

                        Text(
                            text = " : ",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFF0F172A)
                        )

                        // Minute Field
                        BasicTextField(
                            value = minuteInputText,
                            onValueChange = { newVal ->
                                val clean = newVal.filter { it.isDigit() }.take(2)
                                minuteInputText = clean
                                val num = clean.toIntOrNull()
                                if (num != null && num in 0..59) {
                                    selectedMinute = num
                                    activePreset = null
                                }
                            },
                            textStyle = TextStyle(
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF0F172A),
                                textAlign = TextAlign.Center
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier
                                .width(34.dp)
                                .clickable { /* allow keyboard focus */ }
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        // AM/PM Interactive Pill
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFEBF5FF))
                                .border(1.dp, Color(0xFF90CAF9), RoundedCornerShape(8.dp))
                                .clickable {
                                    selectedHour = (selectedHour + 12) % 24
                                    activePreset = null
                                }
                                .padding(horizontal = 10.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isPm) "PM" else "AM",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF1E88E5)
                            )
                        }
                    }

                    // Right Clock Icon / Dropdown Indicator
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (showDropdown) Color(0xFFEBF5FF) else Color(0xFFF8FAFC))
                            .clickable { showDropdown = !showDropdown },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🕒",
                            fontSize = 18.sp
                        )
                    }
                }

                // 3-Column Dropdown Wheel Overlay (Exact Match to Screenshot with Blue Selection Pills)
                if (showDropdown) {
                    val density = LocalDensity.current
                    val yOffsetPx = with(density) { 58.dp.roundToPx() }

                    Popup(
                        alignment = Alignment.TopStart,
                        offset = IntOffset(x = 0, y = yOffsetPx),
                        onDismissRequest = { showDropdown = false },
                        properties = PopupProperties(
                            focusable = false,
                            dismissOnBackPress = true,
                            dismissOnClickOutside = true
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .width(200.dp)
                                .height(235.dp)
                                .shadow(elevation = 16.dp, shape = RoundedCornerShape(14.dp))
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color.White)
                                .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(14.dp))
                                .padding(vertical = 6.dp, horizontal = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // 1. Hours Column (1..12)
                                LazyColumn(
                                    state = hourListState,
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    items((1..12).toList()) { h ->
                                        val isSelected = (h == h12)
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(34.dp)
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(if (isSelected) Color(0xFF1E88E5) else Color.Transparent)
                                                .clickable {
                                                    val new24H = if (isPm) {
                                                        if (h == 12) 12 else h + 12
                                                    } else {
                                                        if (h == 12) 0 else h
                                                    }
                                                    selectedHour = new24H
                                                    activePreset = null
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = String.format(Locale.US, "%02d", h),
                                                fontSize = 15.sp,
                                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
                                                color = if (isSelected) Color.White else Color(0xFF0F172A)
                                            )
                                        }
                                    }
                                }

                                // Subtle Vertical Separator
                                Box(
                                    modifier = Modifier
                                        .width(1.dp)
                                        .fillMaxHeight()
                                        .background(Color(0xFFE2E8F0))
                                )

                                // 2. Minutes Column (0..59)
                                LazyColumn(
                                    state = minuteListState,
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    items((0..59).toList()) { m ->
                                        val isSelected = (m == selectedMinute)
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(34.dp)
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(if (isSelected) Color(0xFF1E88E5) else Color.Transparent)
                                                .clickable {
                                                    selectedMinute = m
                                                    activePreset = null
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = String.format(Locale.US, "%02d", m),
                                                fontSize = 15.sp,
                                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
                                                color = if (isSelected) Color.White else Color(0xFF0F172A)
                                            )
                                        }
                                    }
                                }

                                // Subtle Vertical Separator
                                Box(
                                    modifier = Modifier
                                        .width(1.dp)
                                        .fillMaxHeight()
                                        .background(Color(0xFFE2E8F0))
                                )

                                // 3. AM/PM Column
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .padding(horizontal = 2.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    // AM Pill
                                    val isAmSelected = !isPm
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(34.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(if (isAmSelected) Color(0xFF1E88E5) else Color.Transparent)
                                            .clickable {
                                                if (selectedHour >= 12) selectedHour -= 12
                                                activePreset = null
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "AM",
                                            fontSize = 14.sp,
                                            fontWeight = if (isAmSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
                                            color = if (isAmSelected) Color.White else Color(0xFF0F172A)
                                        )
                                    }

                                    // PM Pill
                                    val isPmSelected = isPm
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(34.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(if (isPmSelected) Color(0xFF1E88E5) else Color.Transparent)
                                            .clickable {
                                                if (selectedHour < 12) selectedHour += 12
                                                activePreset = null
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "PM",
                                            fontSize = 14.sp,
                                            fontWeight = if (isPmSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
                                            color = if (isPmSelected) Color.White else Color(0xFF0F172A)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Quick Presets (+ minutes from start):
            Text(
                text = "Quick Presets (+ minutes from start):",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF475569)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(10, 15, 30, 45).forEach { mins ->
                    val isSelected = (activePreset == mins)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) Color(0xFFECFDF5) else Color.White)
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) Color(0xFF0F5132) else Color(0xFFE2D6C0),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                activePreset = mins
                                val totalMins = baseH * 60 + baseM + mins
                                selectedHour = (totalMins / 60) % 24
                                selectedMinute = totalMins % 60
                            }
                            .padding(vertical = 9.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+\${mins}m",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color(0xFF0F5132) else Color(0xFF0F172A)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // 💾 Save Jamat Time
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0F5132))
                    .clickable {
                        val savedStr = String.format(Locale.US, "%02d:%02d", selectedHour, selectedMinute)
                        onSave(savedStr)
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "💾", fontSize = 16.sp)
                    Text(
                        text = "Save Jamat Time",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF3D48A)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // ↺ Reset to Default
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFE2D6C0), RoundedCornerShape(16.dp))
                    .clickable {
                        onReset()
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = "↺", fontSize = 16.sp, color = Color(0xFF475569), fontWeight = FontWeight.Bold)
                    Text(
                        text = "Reset to Default",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF475569)
                    )
                }
            }
        }
    }
}

@Composable
fun WatchAdMonthlyTimetablePromptModal(
    strings: PrayerScreenStrings,
    selectedLang: String,
    onWatchAd: () -> Unit,
    onDismiss: () -> Unit
) {
    val isHi = selectedLang.startsWith("hi")
    val isUr = selectedLang.startsWith("ur")

    val title = if (isHi) "माहवारी टाइम टेबल" else if (isUr) "ماہانہ ٹائم ٹیبل" else "Monthly Timetable"
    val msg = if (isHi) {
        "पूरे महीने का मुकम्मल नमाज़ टाइम टेबल देखने के लिए एक छोटा सा ऐड देखें।"
    } else if (isUr) {
        "پورے مہینے کا مکمل نماز ٹائم ٹیبل دیکھنے کے لیے ایک چھوٹا اشتہار دیکھیں۔"
    } else {
        "Watch a short ad to view the full monthly prayer timetable."
    }
    val watchAdBtn = if (isHi) "📺 ऐड देखें" else if (isUr) "📺 اشتہار دیکھیں" else "📺 Watch Ad"
    val cancelBtn = if (isHi) "रद्द करें" else if (isUr) "منسوخ" else "Cancel"

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(22.dp),
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE8F5E9))
                        .border(1.5.dp, Color(0xFF0F5132), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "📅", fontSize = 26.sp)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = title,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            Text(
                text = msg,
                fontSize = 14.sp,
                color = Color(0xFF475569),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)
            )
        },
        confirmButton = {
            Button(
                onClick = onWatchAd,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F5132)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
            ) {
                Text(
                    text = watchAdBtn,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF3D48A)
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = cancelBtn,
                    fontSize = 14.sp,
                    color = Color(0xFF64748B),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    )
}

// ══════════════════════════════════════════════════════════════
//  FULL SCREEN MONTHLY TIMETABLE VIEW (Exact media_1792304819630.png)
// ══════════════════════════════════════════════════════════════
@Composable
fun MonthlyTimetableFullScreenView(
    calendar: Calendar,
    days: List<PrayerDayData>,
    isLoading: Boolean,
    todayData: PrayerDayData?,
    locationLabel: String,
    selectedLang: String,
    strings: PrayerScreenStrings,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onOpenSettings: () -> Unit,
    onBack: () -> Unit
) {
    val isHi = selectedLang.startsWith("hi")
    val isUr = selectedLang.startsWith("ur")

    val topGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0F5132),
            Color(0xFF07301E)
        )
    )

    val currentMonthIdx = calendar.get(Calendar.MONTH)
    val currentYear = calendar.get(Calendar.YEAR)
    val monthTitle = "\${getGregorianMonthNameLocalized(currentMonthIdx, selectedLang)} $currentYear"
    val todayDateStr = todayData?.dateStr ?: ""

    // Hijri date for header
    val headerHijriStr = todayData?.let {
        "\${it.hijriDay} \${getHijriMonthNameLocalized(it.hijriMonthName, selectedLang)} \${it.hijriYear}"
    } ?: if (isHi) "5 जमादिउल अव्वल 1448" else if (isUr) "5 جمادی الاول 1448" else "5 Jumada al-Awwal 1448"

    val titleHeader = if (isHi) "इस्लामिक कैलेंडर" else if (isUr) "اسلامی کیلنڈر" else "Islamic Calendar"
    val settingsBtnText = if (isHi) "⚙️ नमाज़ समय सेटिंग्स" else if (isUr) "⚙️ اوقات نماز ترتیبات" else "⚙️ Prayer Settings"
    val prevBtnText = if (isHi) "पिछला" else if (isUr) "پچھلا" else "Prev"
    val nextBtnText = if (isHi) "अगला" else if (isUr) "اگلا" else "Next"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F4EE))
    ) {
        // ── TOP EMERALD HEADER CARD (Exact media_1792304819630.png) ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(6.dp, RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .background(topGradient)
                .statusBarsPadding()
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Left: Back button & Titles
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        // Circular back button with centered arrow
                        Box(
                            modifier = Modifier
                                .requiredSize(42.dp)
                                .aspectRatio(1f)
                                .clip(CircleShape)
                                .background(Color(0x3D000000))
                                .border(1.2.dp, Color(0x66F3D48A), CircleShape)
                                .clickable(onClick = onBack),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color(0xFFF3D48A),
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            // Title
                            Text(
                                text = titleHeader,
                                fontSize = 17.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            // Hijri subtitle
                            Text(
                                text = headerHijriStr,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFF3D48A)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            // Location row
                            Text(
                                text = "📍 $locationLabel  |  🧭 GPS 0 m",
                                fontSize = 11.5.sp,
                                color = Color(0xFFB8D6C9),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Right: Actions (Heart, Sun) and Settings Pill
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFFF1F2))
                                    .border(0.8.dp, Color(0xFFFFCDD2), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "❤️", fontSize = 15.sp)
                            }
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFEF3C7))
                                    .border(0.8.dp, Color(0xFFFDE68A), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "☀️", fontSize = 15.sp)
                            }
                        }

                        // Settings Pill Button
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0x2E000000))
                                .border(1.dp, Color(0x66F3D48A), RoundedCornerShape(20.dp))
                                .clickable(onClick = onOpenSettings)
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = settingsBtnText,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFF3D48A)
                            )
                        }
                    }
                }
            }
        }

        // ── MONTH SELECTOR / NAVIGATION BAR ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Prev month button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF0F5132))
                    .border(1.dp, Color(0x66F3D48A), RoundedCornerShape(20.dp))
                    .clickable(onClick = onPrevMonth)
                    .padding(horizontal = 14.dp, vertical = 7.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "◀", fontSize = 11.sp, color = Color(0xFFF3D48A))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = prevBtnText,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Month & Year title
            Text(
                text = monthTitle,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F5132)
            )

            // Next month button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF0F5132))
                    .border(1.dp, Color(0x66F3D48A), RoundedCornerShape(20.dp))
                    .clickable(onClick = onNextMonth)
                    .padding(horizontal = 14.dp, vertical = 7.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = nextBtnText,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "▶", fontSize = 11.sp, color = Color(0xFFF3D48A))
                }
            }
        }

        // ── MONTHLY TIMETABLE CARDS LIST ──
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = Color(0xFF0F5132))
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = if (isHi) "नमाज़ समय लोड हो रहा है..." else "Loading prayer times...",
                        fontSize = 13.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }
        } else if (days.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isHi) "कोई डेटा उपलब्ध नहीं है" else "No timetable available",
                    color = Color(0xFF64748B),
                    fontSize = 14.sp
                )
            }
        } else {
            val prayerNames = listOf(
                strings.fajr,
                strings.dhuhr,
                strings.asr,
                strings.maghrib,
                strings.isha
            )
            val prayerKeys = listOf("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha")

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 2.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                items(days) { day ->
                    val isToday = day.dateStr == todayDateStr
                    val shortMonth = getGregorianMonthShortLocalized(day.monthNumber - 1, selectedLang)

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(
                            width = if (isToday) 1.6.dp else 1.dp,
                            color = if (isToday) Color(0xFF0F5132) else Color(0xFFE2E8F0)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 11.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Left Section: Date & Hijri Date
                            Column(modifier = Modifier.width(96.dp)) {
                                Text(
                                    text = "\${day.dayOfMonth} $shortMonth",
                                    fontSize = 16.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F5132)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "\${day.hijriDay} \${day.hijriMonthName}",
                                    fontSize = 11.5.sp,
                                    color = Color(0xFF64748B),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Spacer(modifier = Modifier.width(6.dp))

                            // Right Section: 5 Prayer Time Columns
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                prayerKeys.forEachIndexed { idx, key ->
                                    val name = prayerNames[idx]
                                    val rawTime = day.timings[key] ?: "--:--"
                                    val time24 = format24h(rawTime)

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFFF8FAFC))
                                            .padding(vertical = 6.dp, horizontal = 2.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = name,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = Color(0xFF64748B),
                                                maxLines = 1
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = time24,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A),
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
        }
    }
}

fun format24h(timeStr: String): String {
    val clean = timeStr.trim().split(" ")[0]
    val parts = clean.split(":")
    if (parts.size < 2) return timeStr
    val h = parts[0].trim().toIntOrNull() ?: return timeStr
    val m = parts[1].trim().toIntOrNull() ?: 0
    return String.format(Locale.US, "%02d:%02d", h, m)
}