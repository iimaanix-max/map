package com.tasbih.digitaltasbih.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.tasbih.digitaltasbih.ui.components.AppToast
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import com.tasbih.digitaltasbih.data.AsmaAudioPlayer
import com.tasbih.digitaltasbih.data.AsmaNameItem
import com.tasbih.digitaltasbih.data.LanguageItem
import com.tasbih.digitaltasbih.data.LocalizationManager
import com.tasbih.digitaltasbih.data.PrayerActiveMeta
import com.tasbih.digitaltasbih.data.PrayerDayData
import com.tasbih.digitaltasbih.data.PrayerRepository
import com.tasbih.digitaltasbih.data.SampleHomeData
import com.tasbih.digitaltasbih.data.TasbihRepository
import com.tasbih.digitaltasbih.ui.components.AdMobNativeAdCard
import com.tasbih.digitaltasbih.ui.components.AsmaDetailsModal
import com.tasbih.digitaltasbih.ui.components.BottomNavigationBar
import com.tasbih.digitaltasbih.util.AdConfig
import com.tasbih.digitaltasbih.ui.components.DailyDuaCard
import com.tasbih.digitaltasbih.data.CalendarRepository
import com.tasbih.digitaltasbih.data.DuaRepository
import com.tasbih.digitaltasbih.ui.components.AuthenticHadithReferenceModal
import com.tasbih.digitaltasbih.data.HomeCardsLocalization
import com.tasbih.digitaltasbih.ui.components.FeatureGrid
import com.tasbih.digitaltasbih.ui.components.FeaturedAllahNameCard
import com.tasbih.digitaltasbih.ui.components.LanguageBar
import com.tasbih.digitaltasbih.ui.components.LargeFeatureGrid
import com.tasbih.digitaltasbih.ui.components.HomeAnnouncementModal
import com.tasbih.digitaltasbih.ui.components.MoreLanguagesModal
import com.tasbih.digitaltasbih.ui.components.MasjidJamatReviewModal
import com.tasbih.digitaltasbih.ui.components.PrayerHeroCard
import com.tasbih.digitaltasbih.ui.components.PrayerTimesStrip
import com.tasbih.digitaltasbih.ui.components.TopHeader
import com.tasbih.digitaltasbih.ui.components.UpcomingEventsCard
import com.tasbih.digitaltasbih.util.MapHelper
import com.tasbih.digitaltasbih.data.RamadanManager
import com.tasbih.digitaltasbih.ui.components.RamadanDecorationGarland
import com.tasbih.digitaltasbih.ui.components.PrayerCardFestiveLights

@Composable
fun HomeScreen(
    onOpenQuran: () -> Unit = {},
    onOpenPrayerTimes: () -> Unit = {},
    onOpenSalahTracker: () -> Unit = {},
    onOpenMorningAdhkar: () -> Unit = {},
    onOpenEveningAdhkar: () -> Unit = {},
    onOpenDhikrTracker: () -> Unit = {},
    onOpenTasbih: () -> Unit = {},
    onOpenTasbihWithName: (AsmaNameItem) -> Unit = {},
    onOpenAllNames: () -> Unit = {},
    onOpenDarood: () -> Unit = {},
    onOpenQibla: () -> Unit = {},
    onOpenHadith: () -> Unit = {},
    onOpenCalendar: () -> Unit = {},
    onOpenDua: () -> Unit = {},
    onOpenQuranAudio: () -> Unit = {},
    onOpenAdhanAudio: () -> Unit = {},
    onOpenLiveDhikr: () -> Unit = {},
    onOpenJummah: () -> Unit = {},
    onOpenRamadan: () -> Unit = {},
    onOpenZakat: () -> Unit = {},
    onOpenShahada: () -> Unit = {},
    onOpenSalahPillar: () -> Unit = {},
    onOpenSawm: () -> Unit = {},
    onOpenHajj: () -> Unit = {},
    onOpenFindMosque: () -> Unit = {},
    onOpenSettings: () -> Unit = {},
    selectedLanguage: String = PrayerRepository.getLanguage(),
    onLanguageChange: (String) -> Unit = {}
) {
    val context = LocalContext.current
    var isDarkTheme by remember { mutableStateOf(PrayerRepository.isDarkTheme()) }
    var currentLanguage by remember(selectedLanguage) { mutableStateOf(selectedLanguage) }

    LaunchedEffect(selectedLanguage) {
        currentLanguage = selectedLanguage
    }
    var activeTab by remember { mutableStateOf("home") }
    var isMoreLangOpen by remember { mutableStateOf(false) }
    var isAsmaDetailsOpen by remember { mutableStateOf(false) }
    var isHadithRefModalOpen by remember { mutableStateOf(false) }
    var isAnnouncementOpen by remember {
        mutableStateOf(!PrayerRepository.isHomeAnnouncementSeen())
    }
    val isRamadanActive by RamadanManager.isRamadanActiveState
    LaunchedEffect(Unit) {
        RamadanManager.init(context)
    }

    val upcomingEvents = remember(currentLanguage) {
        CalendarRepository.getUpcomingEvents(currentLanguage, 2)
    }

    val dailyDua = remember(currentLanguage) {
        DuaRepository.getDailyFeaturedDua(currentLanguage)
    }
    var isJamatReviewOpen by remember {
        mutableStateOf(PrayerRepository.shouldShowJamat30DayReview())
    }

    val dailyAsmaItem = remember { TasbihRepository.getDailyFeaturedAsmaItem() }
    val dailyAllahName = remember(currentLanguage, dailyAsmaItem) {
        TasbihRepository.getDailyFeaturedAllahName(currentLanguage)
    }
    val isAudioPlaying = AsmaAudioPlayer.isPlaying && AsmaAudioPlayer.currentPlayingNum == dailyAsmaItem.number

    val strings = LocalizationManager.getString(currentLanguage)
    val prayerSettings = remember { PrayerRepository.loadSettings() }
    var todayPrayerData by remember { mutableStateOf<PrayerDayData?>(null) }
    var prayerActiveMeta by remember { mutableStateOf<PrayerActiveMeta?>(null) }

    LaunchedEffect(currentLanguage, prayerSettings) {
        val data = PrayerRepository.fetchTodayPrayerTimes()
        todayPrayerData = data
        prayerActiveMeta = PrayerRepository.getActiveMeta(data, currentLanguage)
    }

    // Live 1-second ticker to update countdown & transition prayers automatically
    LaunchedEffect(todayPrayerData, currentLanguage) {
        val data = todayPrayerData ?: return@LaunchedEffect
        while (isActive) {
            prayerActiveMeta = PrayerRepository.getActiveMeta(data, currentLanguage)
            delay(1000L)
        }
    }

    LaunchedEffect(dailyAsmaItem) {
        AsmaAudioPlayer.prefetch(context, dailyAsmaItem)
    }

    // Visible languages on the top bar dynamically includes user chosen language in 5th slot
    val visibleLanguages = remember(currentLanguage) {
        val coreCodes = listOf("en", "hi", "ur", "ar")
        val allFromGroups = LocalizationManager.allLanguageGroups.flatMap { it.languages }
        val list = mutableListOf<LanguageItem>()
        for (code in coreCodes) {
            allFromGroups.find { it.code == code }?.let { list.add(it) }
        }
        val fifthCode = if (!coreCodes.contains(currentLanguage)) {
            currentLanguage
        } else {
            PrayerRepository.getExtraLangSlot()
        }
        allFromGroups.find { it.code == fifthCode }?.let {
            if (!list.any { item -> item.code == it.code }) {
                list.add(it)
            }
        }
        if (list.size < 5) {
            allFromGroups.find { it.code == "bn" }?.let {
                if (!list.any { item -> item.code == it.code }) list.add(it)
            }
        }
        list
    }

    Scaffold(
        containerColor = if (isDarkTheme) Color(0xFF07150E) else Color(0xFFF6F4EE),
        topBar = {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.background(if (isDarkTheme) Color(0xFF07150E) else Color(0xFFF6F4EE))
                ) {
                    // 1. ORNATE FLOATING DARK EMERALD HEADER (Exact Image 2)
                    TopHeader(
                        strings = strings,
                        isDarkTheme = isDarkTheme,
                        onToggleTheme = {
                            isDarkTheme = !isDarkTheme
                            PrayerRepository.setDarkTheme(isDarkTheme)
                            AppToast.show(if (isDarkTheme) "Night Theme 🌙" else "Day Theme ☀️")
                        },
                        onOpenSettings = onOpenSettings,
                        onShare = {
                            AppToast.show("Sharing Digital Tasbih...")
                        },
                        onSupport = {
                            AppToast.show("Thank you for your love & support ❤️")
                        }
                    )

                    // 2. LANGUAGE BAR (English, हिंदी, اردو, العربية, বাংলা, 🌐 +20)
                    LanguageBar(
                        languages = visibleLanguages,
                        selectedCode = currentLanguage,
                        isDarkTheme = isDarkTheme,
                        onSelectLanguage = { code ->
                            currentLanguage = code
                            PrayerRepository.saveLanguage(code)
                            if (!listOf("en", "hi", "ur", "ar").contains(code)) {
                                PrayerRepository.setExtraLangSlot(code)
                            }
                            onLanguageChange(code)
                        },
                        onOpenMoreLanguages = {
                            isMoreLangOpen = true
                        }
                    )
                }

                // 3. RAMADAN FESTIVE HANGING LIGHT GARLAND ("Light ki Ladi")
                if (isRamadanActive) {
                    RamadanDecorationGarland(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .align(Alignment.TopCenter),
                        isDarkTheme = isDarkTheme
                    )
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(
                activeTabId = activeTab,
                isDarkTheme = isDarkTheme,
                onTabSelected = { tabId ->
                    if (tabId == "quran") {
                        onOpenQuran()
                    } else if (tabId == "tasbih") {
                        onOpenTasbih()
                    } else if (tabId == "calendar") {
                        onOpenCalendar()
                    } else if (tabId == "dua") {
                        onOpenDua()
                    } else {
                        activeTab = tabId
                    }
                }
            )
        }
    ) { paddingValues ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isDarkTheme) Color(0xFF07150E) else Color(0xFFF6F4EE))
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // 3. PRAYER HERO CARD & STRIP WRAPPED IN BOX FOR FESTIVE LIGHTS OVERLAY
            Box(modifier = Modifier.fillMaxWidth()) {
                var heroCardHeightPx by remember { mutableFloatStateOf(0f) }

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PrayerHeroCard(
                        modifier = Modifier.onGloballyPositioned { coords ->
                            heroCardHeightPx = coords.size.height.toFloat()
                        },
                        strings = strings,
                        currentPrayer = prayerActiveMeta?.currentPrayer,
                        nextPrayer = prayerActiveMeta?.nextPrayer,
                        countdownText = prayerActiveMeta?.countdownText,
                        todayPrayerData = todayPrayerData,
                        isRamadanActive = isRamadanActive,
                        onClick = onOpenPrayerTimes
                    )

                    // 4. HORIZONTAL PRAYER TIMES STRIP (Active Capsule + All Prayers Card)
                    PrayerTimesStrip(
                        strings = strings,
                        todayPrayerData = todayPrayerData,
                        currentPrayerId = prayerActiveMeta?.activeHighlightPrayerId ?: prayerActiveMeta?.currentPrayer?.id ?: prayerActiveMeta?.nextPrayer?.id,
                        isDarkTheme = isDarkTheme,
                        isRamadanActive = isRamadanActive,
                        onOpenPrayerScreen = onOpenPrayerTimes
                    )
                }

                // Festive lights rendered AFTER PrayerTimesStrip so bottom lights drape OVER the buttons!
                if (isRamadanActive && heroCardHeightPx > 0f) {
                    PrayerCardFestiveLights(
                        modifier = Modifier.matchParentSize(),
                        heroCardHeight = heroCardHeightPx,
                        isDarkTheme = isDarkTheme
                    )
                }
            }

            // 4.5 EXCLUSIVE RAMADAN HUB VIP BANNER (Clickable directly to open Ramadan Screen)
            if (isRamadanActive) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 6.dp, shape = RoundedCornerShape(18.dp))
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF0F4D3A), Color(0xFF08281E), Color(0xFF041710))
                            )
                        )
                        .border(1.2.dp, Color(0xFFD4AF37), RoundedCornerShape(18.dp))
                        .clickable { onOpenRamadan() }
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF0E5D47))
                                    .border(1.dp, Color(0xFFD4AF37), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🌙", fontSize = 18.sp)
                            }
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "RAMADAN KAREEM HUB",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White,
                                        letterSpacing = 0.8.sp
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Color(0xFFD4AF37))
                                            .padding(horizontal = 5.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "NEW",
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color(0xFF06140E)
                                        )
                                    }
                                }
                                Text(
                                    text = "Live Sehri/Iftar Clock • 30 Days Duas • Zakat",
                                    fontSize = 11.sp,
                                    color = Color(0xFFD4AF37)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White.copy(alpha = 0.15f))
                                .border(0.5.dp, Color(0x60D4AF37), RoundedCornerShape(10.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Open →",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // 5. COMPACT FEATURE TILES GRID (Row 1: 5 items | Row 2: 4 items)
            FeatureGrid(
                strings = strings,
                onTileClick = { tileId ->
                    if (tileId == "ramadan" || tileId == "ramzan" || tileId == "ramadan_hub") {
                        onOpenRamadan()
                    } else if (tileId == "jummah" || tileId == "jummahMubarak") {
                        onOpenJummah()
                    } else if (tileId == "quran") {
                        onOpenQuran()
                    } else if (tileId == "prayer" || tileId == "prayerTimes") {
                        onOpenPrayerTimes()
                    } else if (tileId == "tracker" || tileId == "salahTracker") {
                        onOpenSalahTracker()
                    } else if (tileId == "morning_adhkar" || tileId == "adhkar") {
                        onOpenMorningAdhkar()
                    } else if (tileId == "evening_adhkar") {
                        onOpenEveningAdhkar()
                    } else if (tileId == "daily" || tileId == "dhikrTracker" || tileId == "tracker_dhikr") {
                        onOpenDhikrTracker()
                    } else if (tileId == "tasbih" || tileId == "tasbeeh" || tileId == "counter") {
                        onOpenTasbih()
                    } else if (tileId == "names99" || tileId == "names") {
                        onOpenAllNames()
                    } else if (tileId == "darood" || tileId == "durood") {
                        onOpenDarood()
                    } else if (tileId == "hadith") {
                        onOpenHadith()
                    } else if (tileId == "qibla" || tileId == "qiblah") {
                        onOpenQibla()
                    } else if (tileId == "calendar" || tileId == "islamicCalendar" || tileId == "events") {
                        onOpenCalendar()
                    } else if (tileId == "audio" || tileId == "quran_audio" || tileId == "quranAudio") {
                        onOpenQuranAudio()
                    } else if (tileId == "azan" || tileId == "adhan") {
                        onOpenAdhanAudio()
                    } else if (tileId == "dua" || tileId == "duas") {
                        onOpenDua()
                    } else if (tileId == "mosque" || tileId == "find_mosque" || tileId == "nearby_mosque") {
                        onOpenFindMosque()
                    } else if (tileId == "live" || tileId == "liveKaaba" || tileId == "makkah_live") {
                        MapHelper.openMakkahLive(context)
                    } else if (tileId == "groups" || tileId == "liveDhikr" || tileId == "live_dhikr") {
                        onOpenLiveDhikr()
                    } else if (tileId == "zakat" || tileId == "zakat_calculator" || tileId == "zakatCalculator") {
                        onOpenZakat()
                    } else if (tileId == "shahada" || tileId == "shahadah" || tileId == "kalima_shahada") {
                        onOpenShahada()
                    } else if (tileId == "salah_pillar" || tileId == "salah_guide" || tileId == "salahPillar") {
                        onOpenSalahPillar()
                    } else if (tileId == "sawm" || tileId == "fasting" || tileId == "sawm_pillar") {
                        onOpenSawm()
                    } else if (tileId == "hajj" || tileId == "pilgrimage" || tileId == "hajj_pillar") {
                        onOpenHajj()
                    } else {
                        AppToast.show("Opened: $tileId")
                    }
                }
            )

            // 5.5 GOOGLE ADMOB NATIVE VIDEO AD CARD (Original homeNativeAdSlot)
            AdMobNativeAdCard(
                adUnitId = AdConfig.NATIVE_VIDEO_AD_UNIT_ID,
                modifier = Modifier.fillMaxWidth(),
                isDarkTheme = isDarkTheme
            )

            // 6. 16 LARGE FEATURE TILES GRID (Exact 4x4 Grid matching Image 2)
            LargeFeatureGrid(
                strings = strings,
                onTileClick = { tileId ->
                    if (tileId == "jummah" || tileId == "jummahMubarak") {
                        onOpenJummah()
                    } else if (tileId == "quran") {
                        onOpenQuran()
                    } else if (tileId == "prayer" || tileId == "prayerTimes") {
                        onOpenPrayerTimes()
                    } else if (tileId == "tracker" || tileId == "salahTracker") {
                        onOpenSalahTracker()
                    } else if (tileId == "morning_adhkar" || tileId == "adhkar") {
                        onOpenMorningAdhkar()
                    } else if (tileId == "evening_adhkar") {
                        onOpenEveningAdhkar()
                    } else if (tileId == "daily" || tileId == "dhikrTracker" || tileId == "tracker_dhikr") {
                        onOpenDhikrTracker()
                    } else if (tileId == "tasbih" || tileId == "tasbeeh" || tileId == "counter") {
                        onOpenTasbih()
                    } else if (tileId == "names99" || tileId == "names") {
                        onOpenAllNames()
                    } else if (tileId == "darood" || tileId == "durood") {
                        onOpenDarood()
                    } else if (tileId == "hadith") {
                        onOpenHadith()
                    } else if (tileId == "qibla" || tileId == "qiblah") {
                        onOpenQibla()
                    } else if (tileId == "calendar" || tileId == "islamicCalendar" || tileId == "events") {
                        onOpenCalendar()
                    } else if (tileId == "audio" || tileId == "quran_audio" || tileId == "quranAudio") {
                        onOpenQuranAudio()
                    } else if (tileId == "azan" || tileId == "adhan") {
                        onOpenAdhanAudio()
                    } else if (tileId == "dua" || tileId == "duas") {
                        onOpenDua()
                    } else if (tileId == "mosque" || tileId == "find_mosque" || tileId == "nearby_mosque") {
                        onOpenFindMosque()
                    } else if (tileId == "live" || tileId == "liveKaaba" || tileId == "makkah_live") {
                        MapHelper.openMakkahLive(context)
                    } else if (tileId == "groups" || tileId == "liveDhikr" || tileId == "live_dhikr") {
                        onOpenLiveDhikr()
                    } else if (tileId == "zakat" || tileId == "zakat_calculator" || tileId == "zakatCalculator") {
                        onOpenZakat()
                    } else if (tileId == "shahada" || tileId == "shahadah" || tileId == "kalima_shahada") {
                        onOpenShahada()
                    } else if (tileId == "salah_pillar" || tileId == "salah_guide" || tileId == "salahPillar") {
                        onOpenSalahPillar()
                    } else if (tileId == "sawm" || tileId == "fasting" || tileId == "sawm_pillar") {
                        onOpenSawm()
                    } else if (tileId == "hajj" || tileId == "pilgrimage" || tileId == "hajj_pillar") {
                        onOpenHajj()
                    } else {
                        AppToast.show("Opened: $tileId")
                    }
                }
            )

            // 7. FEATURED NAME OF ALLAH LUXURY CARD (Daily Rotating Name with Audio & Details)
            // Generous spacing to give breathing room for top rose bouquet ("guldasta") ONLY when Ramadan is active
            if (isRamadanActive) {
                Spacer(modifier = Modifier.height(112.dp))
            }

            FeaturedAllahNameCard(
                item = dailyAllahName,
                isPlaying = isAudioPlaying,
                isDarkTheme = isDarkTheme,
                isRamadanActive = isRamadanActive,
                onViewAll = onOpenAllNames,
                onPlayAudio = {
                    AsmaAudioPlayer.togglePlay(context, dailyAsmaItem)
                },
                onOpenDetails = {
                    isAsmaDetailsOpen = true
                }
            )

            // 8. UPCOMING EVENTS LUXURY WIDGET (Image 1 - Dynamic from Calendar)
            UpcomingEventsCard(
                events = upcomingEvents,
                selectedLanguage = selectedLanguage,
                onViewAll = onOpenCalendar,
                onEventClick = { _ ->
                    onOpenCalendar()
                }
            )

            // 9. DAILY DUA LUXURY WIDGET (Image 2 - Dynamic daily rotation with Sunnah reference)
            DailyDuaCard(
                dua = dailyDua,
                selectedLanguage = selectedLanguage,
                onViewAll = onOpenDua,
                onRefClick = {
                    isHadithRefModalOpen = true
                }
            )

            Spacer(modifier = Modifier.height(18.dp))
        }

        // Full 28+ Languages BottomSheet Modal (Triggered by 🌐 +20)
        if (isMoreLangOpen) {
            MoreLanguagesModal(
                selectedCode = currentLanguage,
                onSelectLanguage = { code ->
                    currentLanguage = code
                    PrayerRepository.saveLanguage(code)
                    if (!listOf("en", "hi", "ur", "ar").contains(code)) {
                        PrayerRepository.setExtraLangSlot(code)
                    }
                    onLanguageChange(code)
                },
                onDismiss = {
                    isMoreLangOpen = false
                }
            )
        }

        // Authentic Hadith Reference Modal (Image 3)
        if (isHadithRefModalOpen) {
            AuthenticHadithReferenceModal(
                dua = dailyDua,
                selectedLanguage = currentLanguage,
                onDismiss = { isHadithRefModalOpen = false }
            )
        }

        // 99 Names of Allah Authentic Details Modal (Screenshots 2 & 3)
        if (isAsmaDetailsOpen) {
            AsmaDetailsModal(
                asma = dailyAsmaItem,
                selectedLanguage = currentLanguage,
                onDismiss = { isAsmaDetailsOpen = false },
                onCountInTasbih = { asmaItem ->
                    isAsmaDetailsOpen = false
                    onOpenTasbihWithName(asmaItem)
                }
            )
        }

        // Masjid Jamat Timings 30-Day Monthly Review Modal
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
                    isJamatReviewOpen = false
                    PrayerRepository.keepJamatCurrentFor30Days()
                    AppToast.show("Jamat timings kept for next 30 days ✨")
                },
                onCancelSnooze = {
                    isJamatReviewOpen = false
                    PrayerRepository.snoozeJamatReviewFor15Days()
                    AppToast.show("Reminder postponed for 15 days")
                }
            )
        }

        // Home Special Live Announcement Modal (Exact from index.html)
        if (isAnnouncementOpen) {
            HomeAnnouncementModal(
                selectedLanguage = currentLanguage,
                onDismiss = { isAnnouncementOpen = false },
                onParticipateClick = {
                    isAnnouncementOpen = false
                    onOpenLiveDhikr()
                }
            )
        }
    }
}
