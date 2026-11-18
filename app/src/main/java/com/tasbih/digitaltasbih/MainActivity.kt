package com.tasbih.digitaltasbih

import com.tasbih.digitaltasbih.ui.screens.zakat.ZakatCalculatorScreen
import com.tasbih.digitaltasbih.ui.screens.shahada.ShahadaScreen
import com.tasbih.digitaltasbih.ui.screens.salah.SalahPillarScreen
import com.tasbih.digitaltasbih.ui.screens.sawm.SawmScreen
import com.tasbih.digitaltasbih.ui.screens.hajj.HajjScreen
import com.tasbih.digitaltasbih.ui.screens.mosque.FindMosqueScreen

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.tasbih.digitaltasbih.data.AdhkarRepository
import com.tasbih.digitaltasbih.data.DhikrRepository
import com.tasbih.digitaltasbih.data.DuaRepository
import com.tasbih.digitaltasbih.data.PrayerRepository
import com.tasbih.digitaltasbih.data.QuranRepository
import com.tasbih.digitaltasbih.data.QiblaRepository
import com.tasbih.digitaltasbih.ui.navigation.AppScreen
import com.tasbih.digitaltasbih.ui.screens.HomeScreen
import com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreen
import com.tasbih.digitaltasbih.ui.screens.jummah.JummahScreen
import com.tasbih.digitaltasbih.ui.screens.ramadan.RamadanHomeScreen
import com.tasbih.digitaltasbih.ui.screens.ramadan.QuranTrackerScreen
import com.tasbih.digitaltasbih.ui.screens.AdhanAudioScreen
import com.tasbih.digitaltasbih.ui.screens.adhkar.MorningAdhkarScreen
import com.tasbih.digitaltasbih.ui.screens.adhkar.EveningAdhkarScreen
import com.tasbih.digitaltasbih.ui.screens.dhikr.DhikrTrackerScreen
import com.tasbih.digitaltasbih.ui.screens.prayer.PrayerTimesScreen
import com.tasbih.digitaltasbih.ui.screens.salah.SalahTrackerScreen
import com.tasbih.digitaltasbih.ui.screens.quran.ChooseReciterScreen
import com.tasbih.digitaltasbih.ui.screens.quran.QuranHomeScreen
import com.tasbih.digitaltasbih.ui.screens.quran.QuranReadTimeScreen
import com.tasbih.digitaltasbih.ui.screens.quran.QuranReaderScreen
import com.tasbih.digitaltasbih.ui.screens.quran.QuranSettingsScreen
import com.tasbih.digitaltasbih.ui.screens.quran.QuranAudioScreen
import com.tasbih.digitaltasbih.data.QuranSurahAudioEngine
import com.tasbih.digitaltasbih.data.TasbihRepository
import com.tasbih.digitaltasbih.data.HadithRepository
import com.tasbih.digitaltasbih.ui.screens.tasbih.DaroodSharifScreen
import com.tasbih.digitaltasbih.ui.screens.tasbih.TasbihScreen
import com.tasbih.digitaltasbih.ui.screens.tasbih.NamesOfAllahScreen
import com.tasbih.digitaltasbih.data.CalendarRepository
import com.tasbih.digitaltasbih.ui.screens.calendar.IslamicCalendarScreen
import com.tasbih.digitaltasbih.ui.screens.dua.DuaAllCategoriesScreen
import com.tasbih.digitaltasbih.ui.screens.dua.DuaAllFeelingsScreen
import com.tasbih.digitaltasbih.ui.screens.dua.DuaCategoryScreen
import com.tasbih.digitaltasbih.ui.screens.dua.DuaDetailScreen
import com.tasbih.digitaltasbih.ui.screens.dua.DuaHomeScreen
import com.tasbih.digitaltasbih.ui.screens.dua.DuaMyDuaScreen
import com.tasbih.digitaltasbih.ui.screens.dua.DuaMyListsScreen
import com.tasbih.digitaltasbih.ui.screens.qibla.QiblaScreen
import com.tasbih.digitaltasbih.ui.screens.hadith.HadithScreen
import com.tasbih.digitaltasbih.ui.screens.SettingsScreen
import com.tasbih.digitaltasbih.ui.theme.DigitalTasbihTheme
import com.tasbih.digitaltasbih.util.TasbihAudioEngine
import com.tasbih.digitaltasbih.util.MapHelper
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.tasbih.digitaltasbih.ui.components.AppToastHost
import com.tasbih.digitaltasbih.ui.components.AppSplashScreen
import com.tasbih.digitaltasbih.ui.components.OnboardingLanguageModal
import com.tasbih.digitaltasbih.ui.components.PermissionHelper

class MainActivity : ComponentActivity() {
    private val targetScreenState = mutableStateOf<AppScreen?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        QuranRepository.init(applicationContext)
        QuranSurahAudioEngine.init(applicationContext)
        PrayerRepository.init(applicationContext)
        AdhkarRepository.init(applicationContext)
        DhikrRepository.init(applicationContext)
        TasbihRepository.init(applicationContext)
        QiblaRepository.init(applicationContext)
        HadithRepository.init(applicationContext)
        CalendarRepository.init(applicationContext)
        DuaRepository.init(applicationContext)
        TasbihAudioEngine.init(applicationContext)
        com.tasbih.digitaltasbih.data.RamadanManager.init(applicationContext)
        com.tasbih.digitaltasbih.util.AppPreloader.init(applicationContext)
        PrayerRepository.scheduleAllPrayerNotifications(this)

        // 1. Isolated Firebase Ad Tracker initialization
        try {
            val deviceId = com.tasbih.digitaltasbih.data.groups.GroupCampaignRepository(applicationContext).getDeviceId()
            com.tasbih.digitaltasbih.util.FirebaseAdTracker.init(applicationContext, deviceId)
        } catch (e: Exception) {
            android.util.Log.w("MainActivity", "FirebaseAdTracker init warning: ")
        }

        // 2. Google UMP Consent Gathering & Strictly Guarded MobileAds initialization
        try {
            com.tasbih.digitaltasbih.util.ConsentManager.requestConsentAndInitAds(this)
        } catch (e: Exception) {
            android.util.Log.e("MainActivity", "ConsentManager requestConsent error", e)
        }

        // 3. Google Play In-App Updates Initialization & Availability Check
        try {
            val inAppUpdateManager = com.tasbih.digitaltasbih.util.InAppUpdateManager.init(this)
            inAppUpdateManager.checkForUpdates()
        } catch (e: Exception) {
            android.util.Log.w("MainActivity", "InAppUpdateManager init skipped: ${e.message}")
        }

        // Deep Link Check: digitaltasbih://join?code=GC1234 or https://campaign.dhikr.live/join?code=GC1234
        val rawCode = intent?.data?.getQueryParameter("code")
            ?: intent?.data?.lastPathSegment?.takeIf { it != "join" && it.isNotBlank() }
        val deepLinkCode = sanitizeJoinCode(rawCode)
        val openScreen = intent?.getStringExtra("open_screen") ?: intent?.getStringExtra("screen")
        intent?.removeExtra("open_screen")
        intent?.removeExtra("screen")

        val initial = if (deepLinkCode != null) {
            AppScreen.LiveDhikr(autoJoinCode = deepLinkCode)
        } else when (openScreen) {
            "prayer" -> AppScreen.PrayerTimes
            "salah_tracker", "salah" -> AppScreen.SalahTracker
            "morning_adhkar", "adhkar" -> AppScreen.MorningAdhkar
            "evening_adhkar" -> AppScreen.EveningAdhkar
            "dhikr_tracker", "dhikr", "daily" -> AppScreen.DhikrTracker
            "quran_tracker", "khatam", "quran_khatam" -> AppScreen.QuranTracker
            "names", "asma", "99names", "names_of_allah" -> AppScreen.NamesOfAllah
            "tasbih", "tasbeeh", "counter" -> AppScreen.Tasbih()
            "darood", "durood" -> AppScreen.DaroodSharif()
            "hadith" -> AppScreen.Hadith
            "qibla" -> AppScreen.Qibla
            "calendar", "events" -> AppScreen.Calendar
            "dua", "duas" -> {
                DuaRepository.shuffleHomeContent()
                AppScreen.DuaHome
            }
            "quran_audio", "audio" -> AppScreen.QuranAudio
            "azan", "adhan" -> AppScreen.AdhanAudio
            "groups", "live_dhikr", "liveDhikr" -> AppScreen.LiveDhikr()
            "jummah", "jumma", "jummah_mubarak" -> AppScreen.Jummah
            "ramadan", "ramzan", "ramadan_hub" -> AppScreen.Ramadan
                "zakat", "zakat_calculator", "zakatCalculator" -> AppScreen.ZakatCalculator
            "shahada", "shahadah", "kalima", "kalima_shahada" -> AppScreen.Shahada
            "salah_pillar", "salah_guide", "salahPillar", "namaz_guide" -> AppScreen.SalahPillar
            "sawm", "sawm_pillar", "fasting", "roza" -> AppScreen.Sawm
            "hajj", "hajj_pillar", "pilgrimage" -> AppScreen.Hajj
            "zakat", "zakat_calculator", "zakatCalculator" -> AppScreen.ZakatCalculator
            "quran_kahf", "kahf", "surah_kahf" -> AppScreen.QuranReader("surah", 18)
            "quran_reader" -> {
                val sNum = intent?.getIntExtra("surah_num", 1) ?: 1
                AppScreen.QuranReader("surah", sNum)
            }
            "quran_time", "quran_read_time", "quran_stats" -> AppScreen.QuranReadTime
            "quran" -> AppScreen.QuranMain
            "quran_settings" -> AppScreen.QuranSettings
            "settings" -> AppScreen.Settings
            "mosque", "find_mosque" -> AppScreen.FindMosque
            "live", "makkah_live" -> {
                MapHelper.openMakkahLive(this)
                AppScreen.Home
            }
            else -> AppScreen.Home
        }
        targetScreenState.value = initial

        enableEdgeToEdge()
        setContent {
            DigitalTasbihTheme {
                AppRootNavigation(
                    targetScreen = targetScreenState.value,
                    onScreenHandled = { targetScreenState.value = null }
                )
            }
        }
    }

    private fun sanitizeJoinCode(code: String?): String? {
        if (code.isNullOrBlank()) return null
        val clean = code.trim().uppercase()
        return if (clean.length in 3..32 && clean.all { it.isLetterOrDigit() || it == '-' || it == '_' }) {
            clean
        } else {
            null
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        val rawCode = intent.data?.getQueryParameter("code")
            ?: intent.data?.lastPathSegment?.takeIf { it != "join" && it.isNotBlank() }
        val deepLinkCode = sanitizeJoinCode(rawCode)
        if (deepLinkCode != null) {
            targetScreenState.value = AppScreen.LiveDhikr(autoJoinCode = deepLinkCode)
            return
        }

        val openScreen = intent.getStringExtra("open_screen") ?: intent.getStringExtra("screen")
        intent.removeExtra("open_screen")
        intent.removeExtra("screen")
        if (!openScreen.isNullOrBlank()) {
            val target = when (openScreen) {
                "prayer" -> AppScreen.PrayerTimes
                "salah_tracker", "salah" -> AppScreen.SalahTracker
                "morning_adhkar", "adhkar" -> AppScreen.MorningAdhkar
                "evening_adhkar" -> AppScreen.EveningAdhkar
                "dhikr_tracker", "dhikr", "daily" -> AppScreen.DhikrTracker
                "tasbih", "tasbeeh", "counter" -> AppScreen.Tasbih()
                "darood", "durood" -> AppScreen.DaroodSharif()
                "hadith" -> AppScreen.Hadith
                "qibla" -> AppScreen.Qibla
                "calendar", "events" -> AppScreen.Calendar
                "dua", "duas" -> {
                    DuaRepository.shuffleHomeContent()
                    AppScreen.DuaHome
                }
                "quran_audio", "audio" -> AppScreen.QuranAudio
                "azan", "adhan" -> AppScreen.AdhanAudio
                "groups", "live_dhikr", "liveDhikr" -> AppScreen.LiveDhikr()
                "jummah", "jumma", "jummah_mubarak" -> AppScreen.Jummah
                "ramadan", "ramzan", "ramadan_hub" -> AppScreen.Ramadan
                "zakat", "zakat_calculator" -> AppScreen.ZakatCalculator
                "shahada", "shahadah" -> AppScreen.Shahada
                "salah_pillar" -> AppScreen.SalahPillar
                "sawm", "sawm_pillar", "fasting", "roza" -> AppScreen.Sawm
                "hajj", "hajj_pillar", "pilgrimage" -> AppScreen.Hajj
                "quran_reader" -> {
                    val sNum = intent.getIntExtra("surah_num", 1)
                    AppScreen.QuranReader("surah", sNum)
                }
                "quran_kahf", "kahf", "surah_kahf" -> AppScreen.QuranReader("surah", 18)
                "quran_time", "quran_read_time", "quran_stats" -> AppScreen.QuranReadTime
                "quran" -> AppScreen.QuranMain
                "quran_settings" -> AppScreen.QuranSettings
                "mosque", "find_mosque" -> AppScreen.FindMosque
                "live", "makkah_live" -> {
                    MapHelper.openMakkahLive(this)
                    null
                }
                else -> null
            }
            if (target != null) {
                targetScreenState.value = target
            }
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            com.tasbih.digitaltasbih.util.InAppUpdateManager.get()?.onResume()
        } catch (e: Exception) {
            android.util.Log.w("MainActivity", "InAppUpdateManager onResume error: ${e.message}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            com.tasbih.digitaltasbih.util.InAppUpdateManager.get()?.unregisterListener()
        } catch (e: Exception) {
            android.util.Log.w("MainActivity", "InAppUpdateManager unregister error: ${e.message}")
        }
    }
}

@Composable
fun AppRootNavigation(
    targetScreen: AppScreen? = null,
    onScreenHandled: () -> Unit = {}
) {
    val context = LocalContext.current
    var isSplashVisible by remember { mutableStateOf(true) }
    var currentAppLanguage by remember { mutableStateOf(PrayerRepository.getLanguage()) }
    var isOnboardingLangOpen by remember {
        mutableStateOf(!PrayerRepository.isOnboardingLangSelected())
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { _ ->
        PrayerRepository.setFirstPermissionsRequested(true)
    }

    LaunchedEffect(isSplashVisible, isOnboardingLangOpen) {
        if (!isSplashVisible && !isOnboardingLangOpen) {
            // Once splash and onboarding language are finished, ask for location & notification permissions on first arrival at Home
            if (!PrayerRepository.isFirstPermissionsRequested()) {
                val perms = mutableListOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    perms.add(android.Manifest.permission.POST_NOTIFICATIONS)
                }
                permissionLauncher.launch(perms.toTypedArray())
            }
        }
    }

    // Root of backStack is ALWAYS AppScreen.Home
    val backStack = remember {
        mutableStateListOf<AppScreen>().apply {
            add(AppScreen.Home)
            if (targetScreen != null && targetScreen != AppScreen.Home) {
                add(targetScreen)
            }
        }
    }

    // Handle deep links / onNewIntent dynamically
    LaunchedEffect(targetScreen) {
        if (targetScreen != null && targetScreen != AppScreen.Home) {
            if (backStack.lastOrNull() != targetScreen) {
                backStack.add(targetScreen)
            }
            onScreenHandled()
        }
    }

    val currentScreen = backStack.lastOrNull() ?: AppScreen.Home

    fun navigateTo(screen: AppScreen) {
        if (screen == AppScreen.Home) {
            while (backStack.size > 1) {
                backStack.removeAt(backStack.size - 1)
            }
        } else {
            if (backStack.lastOrNull() != screen) {
                backStack.add(screen)
            }
        }
    }

    fun navigateBack() {
        val top = backStack.lastOrNull()
        while (backStack.size > 1 && backStack.lastOrNull() == top) {
            backStack.removeAt(backStack.size - 1)
        }
        if (backStack.isEmpty()) {
            backStack.add(AppScreen.Home)
        }
    }

    // Hardware / system back button handler
    BackHandler(enabled = backStack.size > 1) {
        navigateBack()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (val screen = currentScreen) {
        is AppScreen.Home -> {
            HomeScreen(
                selectedLanguage = currentAppLanguage,
                onLanguageChange = { currentAppLanguage = it },
                onOpenQuran = {
                    navigateTo(AppScreen.QuranMain)
                },
                onOpenPrayerTimes = {
                    navigateTo(AppScreen.PrayerTimes)
                },
                onOpenSalahTracker = {
                    navigateTo(AppScreen.SalahTracker)
                },
                onOpenMorningAdhkar = {
                    navigateTo(AppScreen.MorningAdhkar)
                },
                onOpenEveningAdhkar = {
                    navigateTo(AppScreen.EveningAdhkar)
                },
                onOpenDhikrTracker = {
                    navigateTo(AppScreen.DhikrTracker)
                },
                onOpenTasbih = {
                    navigateTo(AppScreen.Tasbih())
                },
                onOpenTasbihWithName = { asmaItem ->
                    TasbihRepository.setActiveDhikrId("name_${asmaItem.number}")
                    navigateTo(AppScreen.Tasbih(0))
                },
                onOpenAllNames = {
                    navigateTo(AppScreen.NamesOfAllah)
                },
                onOpenDarood = {
                    navigateTo(AppScreen.DaroodSharif())
                },
                onOpenHadith = {
                    navigateTo(AppScreen.Hadith)
                },
                onOpenQibla = {
                    navigateTo(AppScreen.Qibla)
                },
                onOpenCalendar = {
                    navigateTo(AppScreen.Calendar)
                },
                onOpenDua = {
                    DuaRepository.shuffleHomeContent()
                    navigateTo(AppScreen.DuaHome)
                },
                onOpenQuranAudio = {
                    navigateTo(AppScreen.QuranAudio)
                },
                onOpenAdhanAudio = {
                    navigateTo(AppScreen.AdhanAudio)
                },
                onOpenLiveDhikr = {
                    navigateTo(AppScreen.LiveDhikr())
                },
                onOpenJummah = {
                    navigateTo(AppScreen.Jummah)
                },
                onOpenRamadan = {
                    navigateTo(AppScreen.Ramadan)
                },
                onOpenZakat = {
                    navigateTo(AppScreen.ZakatCalculator)
                },
                onOpenShahada = {
                    navigateTo(AppScreen.Shahada)
                },
                onOpenSalahPillar = {
                    navigateTo(AppScreen.SalahPillar)
                },
                onOpenSawm = {
                    navigateTo(AppScreen.Sawm)
                },
                onOpenHajj = {
                    navigateTo(AppScreen.Hajj)
                },
                onOpenFindMosque = {
                    navigateTo(AppScreen.FindMosque)
                },
                onOpenSettings = {
                    navigateTo(AppScreen.Settings)
                }
            )
        }
                is AppScreen.ZakatCalculator -> {
            ZakatCalculatorScreen(
                onBack = { navigateBack() },
                selectedLanguage = currentAppLanguage
            )
        }
        is AppScreen.Shahada -> {
            ShahadaScreen(
                onBack = { navigateBack() },
                selectedLanguage = currentAppLanguage
            )
        }
        is AppScreen.SalahPillar -> {
            SalahPillarScreen(
                onBack = { navigateBack() },
                selectedLanguage = currentAppLanguage
            )
        }
        is AppScreen.Sawm -> {
            SawmScreen(
                onBack = { navigateBack() },
                selectedLanguage = currentAppLanguage
            )
        }
        is AppScreen.Hajj -> {
            HajjScreen(
                onBack = { navigateBack() },
                selectedLanguage = currentAppLanguage
            )
        }
        is AppScreen.FindMosque -> {
            FindMosqueScreen(
                onBack = { navigateBack() }
            )
        }
        is AppScreen.Ramadan -> {
            RamadanHomeScreen(
                onBack = { navigateBack() },
                onOpenQuranTracker = { navigateTo(AppScreen.QuranTracker) }
            )
        }
        is AppScreen.QuranTracker -> {
            QuranTrackerScreen(
                onBack = { navigateBack() },
                onOpenQuranReader = { surahNum ->
                    navigateTo(AppScreen.QuranReader("surah", surahNum))
                },
                onOpenQuranAudio = {
                    navigateTo(AppScreen.QuranAudio)
                },
                onNavigateToHome = { navigateTo(AppScreen.Home) },
                onNavigateToTasbih = { navigateTo(AppScreen.Tasbih()) },
                onNavigateToQuran = { navigateTo(AppScreen.QuranMain) },
                onNavigateToCalendar = { navigateTo(AppScreen.Calendar) },
                onNavigateToDua = {
                    DuaRepository.shuffleHomeContent()
                    navigateTo(AppScreen.DuaHome)
                }
            )
        }
        is AppScreen.LiveDhikr -> {
            LiveDhikrScreen(
                initialAutoJoinCode = screen.autoJoinCode,
                onBack = { navigateBack() }
            )
        }
        is AppScreen.Jummah -> {
            JummahScreen(
                onBack = { navigateBack() },
                onOpenSurahKahf = {
                    navigateTo(AppScreen.QuranReader("surah", 18))
                }
            )
        }
        is AppScreen.QuranMain -> {
            QuranHomeScreen(
                selectedLanguage = QuranRepository.loadSettings().translationLanguage,
                onBack = { navigateBack() },
                onOpenReader = { type, number ->
                    navigateTo(AppScreen.QuranReader(type, number))
                },
                onOpenSettings = {
                    navigateTo(AppScreen.QuranSettings)
                },
                onOpenReadTimeTracker = {
                    navigateTo(AppScreen.QuranReadTime)
                }
            )
        }
        is AppScreen.QuranReadTime -> {
            QuranReadTimeScreen(
                onBack = { navigateBack() },
                onOpenReader = { type, number ->
                    navigateTo(AppScreen.QuranReader(type, number))
                }
            )
        }
        is AppScreen.QuranReader -> {
            QuranReaderScreen(
                type = screen.type,
                number = screen.number,
                onBack = { navigateBack() },
                onOpenSettings = {
                    navigateTo(AppScreen.QuranSettings)
                }
            )
        }
        is AppScreen.QuranSettings -> {
            QuranSettingsScreen(
                onBack = { navigateBack() },
                onOpenChooseReciter = {
                    navigateTo(AppScreen.ChooseReciter)
                }
            )
        }
        is AppScreen.ChooseReciter -> {
            ChooseReciterScreen(
                onBack = { navigateBack() }
            )
        }
        is AppScreen.QuranAudio -> {
            QuranAudioScreen(
                onBack = {
                    QuranSurahAudioEngine.stop()
                    navigateBack()
                },
                onOpenChooseReciter = {
                    navigateTo(AppScreen.ChooseReciter)
                }
            )
        }
        is AppScreen.AdhanAudio -> {
            AdhanAudioScreen(
                onBack = { navigateBack() }
            )
        }
        is AppScreen.PrayerTimes -> {
            PrayerTimesScreen(
                onBack = { navigateBack() }
            )
        }
        is AppScreen.SalahTracker -> {
            SalahTrackerScreen(
                onBack = { navigateBack() },
                onOpenPrayerTimes = { navigateTo(AppScreen.PrayerTimes) }
            )
        }
        is AppScreen.MorningAdhkar -> {
            MorningAdhkarScreen(
                onBack = { navigateBack() }
            )
        }
        is AppScreen.EveningAdhkar -> {
            EveningAdhkarScreen(
                onBack = { navigateBack() }
            )
        }
        is AppScreen.DhikrTracker -> {
            DhikrTrackerScreen(
                onBack = { navigateBack() },
                onNavigateToTasbih = { dhikrId ->
                    TasbihRepository.setActiveDhikrId(dhikrId)
                    navigateTo(AppScreen.Tasbih(initialTab = 0))
                }
            )
        }
        is AppScreen.NamesOfAllah -> {
            NamesOfAllahScreen(
                onBack = { navigateBack() },
                onSelectNameForRecital = { asmaItem ->
                    TasbihRepository.setActiveDhikrId("name_${asmaItem.number}")
                    navigateTo(AppScreen.Tasbih(0))
                },
                onNavigateToHome = { navigateTo(AppScreen.Home) },
                onNavigateToTasbih = { navigateTo(AppScreen.Tasbih()) },
                onNavigateToQuran = { navigateTo(AppScreen.QuranMain) },
                onNavigateToCalendar = { navigateTo(AppScreen.Calendar) },
                onNavigateToDua = {
                    DuaRepository.shuffleHomeContent()
                    navigateTo(AppScreen.DuaHome)
                }
            )
        }
        is AppScreen.Tasbih -> {
            TasbihScreen(
                initialTab = screen.initialTab,
                onBack = { navigateBack() },
                onNavigateToDarood = { daroodId ->
                    navigateTo(AppScreen.DaroodSharif(daroodId))
                },
                onNavigateToHome = {
                    navigateTo(AppScreen.Home)
                },
                onNavigateToQuran = {
                    navigateTo(AppScreen.QuranMain)
                },
                onNavigateToPrayerTimes = {
                    navigateTo(AppScreen.PrayerTimes)
                },
                onNavigateToDua = {
                    DuaRepository.shuffleHomeContent()
                    navigateTo(AppScreen.DuaHome)
                },
                onNavigateToCalendar = {
                    navigateTo(AppScreen.Calendar)
                },
                onOpenSettings = {
                    navigateTo(AppScreen.Settings)
                }
            )
        }
        is AppScreen.DaroodSharif -> {
            DaroodSharifScreen(
                initialDaroodId = screen.initialDaroodId,
                onBack = { navigateBack() }
            )
        }
        is AppScreen.Qibla -> {
            QiblaScreen(
                onBack = { navigateBack() }
            )
        }
        is AppScreen.Hadith -> {
            HadithScreen(
                onNavigateBack = { navigateBack() }
            )
        }
        is AppScreen.Calendar -> {
            IslamicCalendarScreen(
                onBack = { navigateBack() },
                onNavigateToHome = { navigateTo(AppScreen.Home) },
                onNavigateToTasbih = { navigateTo(AppScreen.Tasbih()) },
                onNavigateToQuran = { navigateTo(AppScreen.QuranMain) },
                onNavigateToDua = {
                    DuaRepository.shuffleHomeContent()
                    navigateTo(AppScreen.DuaHome)
                }
            )
        }
        is AppScreen.DuaHome -> {
            DuaHomeScreen(
                onBack = { navigateTo(AppScreen.Home) },
                onOpenCategory = { catId, catName ->
                    navigateTo(AppScreen.DuaCategory(catId, catName))
                },
                onOpenDetail = { duaId, catName ->
                    navigateTo(AppScreen.DuaDetail(duaId, catName))
                },
                onOpenAllFeelings = {
                    navigateTo(AppScreen.DuaAllFeelings)
                },
                onOpenAllCategories = {
                    navigateTo(AppScreen.DuaAllCategories)
                },
                onOpenMyDua = {
                    navigateTo(AppScreen.DuaMyDua)
                },
                onOpenMyLists = {
                    navigateTo(AppScreen.DuaMyLists)
                }
            )
        }
        is AppScreen.DuaCategory -> {
            DuaCategoryScreen(
                categoryId = screen.categoryId,
                categoryName = screen.categoryName,
                icon = screen.icon,
                onBack = { navigateBack() },
                onOpenDetail = { duaId, catName ->
                    navigateTo(AppScreen.DuaDetail(duaId, catName))
                },
                onNavigateHome = { navigateTo(AppScreen.Home) },
                onNavigateMyDua = { navigateTo(AppScreen.DuaMyDua) },
                onNavigateMyLists = { navigateTo(AppScreen.DuaMyLists) }
            )
        }
        is AppScreen.DuaDetail -> {
            DuaDetailScreen(
                duaId = screen.duaId,
                categoryName = screen.categoryName,
                onBack = { navigateBack() },
                onNavigateHome = { navigateTo(AppScreen.Home) }
            )
        }
        is AppScreen.DuaAllFeelings -> {
            DuaAllFeelingsScreen(
                onBack = { navigateBack() },
                onFeelingClick = { feeling ->
                    navigateTo(AppScreen.DuaCategory(feeling.categoryId, feeling.labelEn))
                },
                onNavigateHome = { navigateTo(AppScreen.Home) },
                onNavigateMyDua = { navigateTo(AppScreen.DuaMyDua) },
                onNavigateMyLists = { navigateTo(AppScreen.DuaMyLists) }
            )
        }
        is AppScreen.DuaAllCategories -> {
            DuaAllCategoriesScreen(
                onBack = { navigateBack() },
                onCategoryClick = { cat ->
                    navigateTo(AppScreen.DuaCategory(cat.id, cat.label))
                },
                onNavigateHome = { navigateTo(AppScreen.Home) },
                onNavigateMyDua = { navigateTo(AppScreen.DuaMyDua) },
                onNavigateMyLists = { navigateTo(AppScreen.DuaMyLists) }
            )
        }
        is AppScreen.DuaMyDua -> {
            DuaMyDuaScreen(
                onBack = { navigateBack() },
                onNavigateHome = { navigateTo(AppScreen.Home) },
                onNavigateMyLists = { navigateTo(AppScreen.DuaMyLists) }
            )
        }
        is AppScreen.DuaMyLists -> {
            DuaMyListsScreen(
                onBack = { navigateBack() },
                onOpenDetail = { duaId, catName ->
                    navigateTo(AppScreen.DuaDetail(duaId, catName))
                },
                onNavigateHome = { navigateTo(AppScreen.Home) },
                onNavigateMyDua = { navigateTo(AppScreen.DuaMyDua) }
            )
        }
        is AppScreen.Settings -> {
            SettingsScreen(
                onBack = { navigateBack() }
            )
        }
    }

    // Global Luxury Toast Pill (No Android Robot, Floating Emerald & Gold)
    AppToastHost()

    // Google Play In-App Update Downloaded Banner (Flexible Auto-Update)
    val isUpdateDownloaded = com.tasbih.digitaltasbih.util.InAppUpdateManager.get()?.isUpdateDownloaded?.value ?: false
    if (isUpdateDownloaded && !isSplashVisible) {
        com.tasbih.digitaltasbih.ui.components.InAppUpdateBanner(
            onRestart = {
                com.tasbih.digitaltasbih.util.InAppUpdateManager.get()?.completeUpdate()
            }
        )
    }

    // 1. First-Time User Onboarding Language Selection Modal (Exact match to original app)
    if (isOnboardingLangOpen && !isSplashVisible) {
        OnboardingLanguageModal(
            selectedLanguage = currentAppLanguage,
            onLanguageSelected = { langCode ->
                currentAppLanguage = langCode
                isOnboardingLangOpen = false
            }
        )
    }

    // 2. Exact Original Loading / Splash Screen
    if (isSplashVisible) {
        AppSplashScreen(
            onDismiss = {
                isSplashVisible = false
            }
        )
    }
    }
}