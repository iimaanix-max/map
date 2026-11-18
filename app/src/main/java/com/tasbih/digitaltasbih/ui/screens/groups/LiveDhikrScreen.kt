package com.tasbih.digitaltasbih.ui.screens.groups
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.zIndex

import com.tasbih.digitaltasbih.util.AdConfig

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Delete
import android.content.ContextWrapper

import android.app.Activity
import androidx.compose.ui.window.DialogProperties
import com.tasbih.digitaltasbih.util.AdMobRewardedManager
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.drawscope.Stroke
import com.tasbih.digitaltasbih.data.TasbihRepository
import com.tasbih.digitaltasbih.data.BeadMarbleSkin
import com.tasbih.digitaltasbih.ui.screens.tasbih.CircularCounterDial
import com.tasbih.digitaltasbih.ui.screens.tasbih.RealisticBeadsCanvas
import com.tasbih.digitaltasbih.ui.screens.tasbih.MarbleBallPreview
import com.tasbih.digitaltasbih.ui.screens.tasbih.MarbleGalleryModal

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.widget.Toast
import com.tasbih.digitaltasbih.ui.components.AppToast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.data.CalendarRepository
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import com.tasbih.digitaltasbih.data.HijriCalendarEngine
import com.tasbih.digitaltasbih.data.groups.CampaignItem
import com.tasbih.digitaltasbih.data.groups.DailyWirdItem
import com.tasbih.digitaltasbih.data.groups.GroupCampaignRepository
import com.tasbih.digitaltasbih.data.groups.ParticipantItem
import com.tasbih.digitaltasbih.data.groups.StoredMegaTicket
import com.tasbih.digitaltasbih.data.groups.GroupInviteHelper
import com.tasbih.digitaltasbih.ui.theme.AmiriBoldFont
import com.tasbih.digitaltasbih.ui.theme.GoldAccent
import com.tasbih.digitaltasbih.util.TasbihAudioEngine
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.TextButton
import androidx.compose.ui.graphics.SolidColor
import com.tasbih.digitaltasbih.data.LanguageItem
import com.tasbih.digitaltasbih.data.LocalizationManager
import com.tasbih.digitaltasbih.data.PrayerRepository
import com.tasbih.digitaltasbih.data.groups.GroupLocationsProvider
import com.tasbih.digitaltasbih.ui.components.LanguageBar
import com.tasbih.digitaltasbih.ui.components.MoreLanguagesModal
import com.tasbih.digitaltasbih.ui.components.AdMobNativeAdCard

import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.geometry.Rect
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import com.tasbih.digitaltasbih.data.groups.LiveDhikrTourLocalization
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.tasbih.digitaltasbih.util.NetworkHelper

private fun Context.findActivity(): Activity? {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    return null
}

// ── Ultra-Luxury Islamic Color Palette Matching Original index.html ──
private val DeepEmeraldGradTop = Color(0xFF042116)
private val DeepEmeraldGradMid = Color(0xFF053B2C)
private val DeepEmeraldGradEnd = Color(0xFF07523D)
private val LuxuryGold = Color(0xFFC9A84C)
private val BrightGold = Color(0xFFD4AF37)
private val ShimmerGold = Color(0xFFFCD34D)
private val ParchmentGold = Color(0xFFFFF5B8)
private val DarkCardBg = Color(0xFF0F1A15)
private val DarkCardBorder = Color(0x47C9A84C)
private val TextMutedGreen = Color(0xFFA3C4B8)

@Composable
fun rememberAssetBitmap(path: String): ImageBitmap? {
    val context = LocalContext.current
    return remember(path) {
        try {
            context.assets.open(path).use { stream ->
                BitmapFactory.decodeStream(stream)?.asImageBitmap()
            }
        } catch (_: Exception) {
            null
        }
    }
}

/**
 * 1:1 Exact Native Implementation of the Live Dhikr / Group Campaigns Screen.
 * Faithful to scGroupCampaigns in index.html, with Cloudflare Worker connectivity,
 * local-first offline fallback, smart auto-counter, and Umrah lucky draw.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveDhikrScreen(
    initialAutoJoinCode: String? = null,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()
    val repository = remember { GroupCampaignRepository(context) }

    var isDarkTheme by remember { mutableStateOf(PrayerRepository.isDarkTheme()) }
    var selectedLanguage by remember { mutableStateOf(PrayerRepository.getLanguage()) }
    var isMoreLangOpen by remember { mutableStateOf(false) }

    var selectedTab by remember { mutableStateOf("public") } // "create", "mine", "saved", "history", "special"
    var campaigns by remember { mutableStateOf<List<CampaignItem>>(emptyList()) }
    var specialCampaigns by remember { mutableStateOf<List<CampaignItem>>(emptyList()) }
    var liveCount by remember { mutableIntStateOf(0) }
    var completedCount by remember { mutableIntStateOf(0) }
    var joinedIds by remember { mutableStateOf<Set<String>>(emptySet()) }
    var savedIds by remember { mutableStateOf<Set<String>>(emptySet()) }
    var isLoading by remember { mutableStateOf(true) }

    // Navigation and Modals State
    var activeCampaign by remember { mutableStateOf<CampaignItem?>(null) }
    var showCreateModal by remember { mutableStateOf(false) }
    var showJoinCodeModal by remember { mutableStateOf(false) }
    var showMegaRegisterModal by remember { mutableStateOf(false) }
    var showNoInternetDialog by remember { mutableStateOf(false) }
    var showProfileModal by remember { mutableStateOf(false) }
    var showWirdModal by remember { mutableStateOf(false) }
    var showWeeklyLeaderboardModal by remember { mutableStateOf(false) }
    var showLeaderboardCampaign by remember { mutableStateOf<CampaignItem?>(null) }
    var pendingAdCampaign by remember { mutableStateOf<CampaignItem?>(null) }
    val activity = context as? Activity
    var showFazilatModal by remember { mutableStateOf<String?>(null) }
    var createdShareCampaign by remember { mutableStateOf<CampaignItem?>(null) }
    var shareModalCampaign by remember { mutableStateOf<Pair<CampaignItem, Boolean>?>(null) }
    var campaignToDelete by remember { mutableStateOf<CampaignItem?>(null) }
    var campaignToReport by remember { mutableStateOf<CampaignItem?>(null) }

    // Live Dynamic Date (matching Screenshot 2: "OCT 19, 2026 • 6 JAMADI UL AWWAL 1448 AH")
    val dateCapsuleText = remember {
        val sdf = SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH)
        val greg = sdf.format(Date()).uppercase()
        val hijri = try {
            val cal = java.util.Calendar.getInstance()
            val h = HijriCalendarEngine.getHijriDate(
                cal.get(java.util.Calendar.YEAR),
                cal.get(java.util.Calendar.MONTH) + 1,
                cal.get(java.util.Calendar.DAY_OF_MONTH),
                "en"
            )
            "${h.day} ${h.monthName.uppercase()} ${h.year} AH"
        } catch (_: Exception) {
            "6 JAMADI UL AWWAL 1448 AH"
        }
        "$greg • $hijri"
    }

    // Dynamic Special Campaign: controlled live from Admin panel (ON/OFF)
    val activeSpecialCampaign = remember(specialCampaigns, campaigns) {
        specialCampaigns.firstOrNull { it.isSpecialMega && !it.completed && !repository.isDemoCampaign(it) }
            ?: campaigns.firstOrNull { it.isSpecialMega && !it.completed && !repository.isDemoCampaign(it) }
    }

    // If special campaign was turned OFF or deleted by admin, reset tab from special to public
    LaunchedEffect(activeSpecialCampaign) {
        if (activeSpecialCampaign == null && selectedTab == "special") {
            selectedTab = "public"
        }
    }

    fun reloadData(showLoading: Boolean = true) {
        coroutineScope.launch {
            if (showLoading) isLoading = true
            joinedIds = repository.getJoinedCampaignIds()
            savedIds = repository.getSavedCampaignIds()
            val data = repository.fetchPublicIndex()
            campaigns = data.campaigns
            specialCampaigns = data.specialCampaigns
            liveCount = data.liveCount.coerceAtLeast(campaigns.count { !it.completed })
            completedCount = data.completedCount.coerceAtLeast(campaigns.count { it.completed })
            if (showLoading) isLoading = false
        }
    }

    LaunchedEffect(Unit) {
        reloadData(showLoading = true)
        AdMobRewardedManager.loadAd(context)
        if (!repository.isProfileDone() || repository.getUserName().isBlank()) {
            showProfileModal = true
        }
    }

    // ── Continuous Background Live Count Sync ──
    // Keeps outside group cards 100% in sync with other devices without full screen reloading spinner
    LaunchedEffect(activeCampaign) {
        if (activeCampaign == null) {
            reloadData(showLoading = false)
            while (true) {
                delay(6000L)
                if (activeCampaign == null) {
                    reloadData(showLoading = false)
                }
            }
        }
    }

    // Auto-join deep link handler (when user taps a shared 1-click link)
    var handledAutoJoinCode by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(initialAutoJoinCode) {
        if (!initialAutoJoinCode.isNullOrBlank() && handledAutoJoinCode != initialAutoJoinCode) {
            handledAutoJoinCode = initialAutoJoinCode
            val codeToJoin = initialAutoJoinCode.trim().uppercase()
            val res = repository.joinByCode(codeToJoin)
            res.onSuccess { matched ->
                reloadData()
                activeCampaign = matched
                AppToast.show("Joined group: ${matched.title}!")
            }.onFailure {
                AppToast.show("Invalid join code link")
            }
        }
    }

    BackHandler(enabled = activeCampaign != null) {
        activeCampaign = null
        reloadData()
    }

    // ── Active Dhikr Room ──
    if (activeCampaign != null) {
        val initiallyShowShare = (createdShareCampaign?.campaignId == activeCampaign?.campaignId)
        LiveActiveDhikrRoom(
            campaign = activeCampaign!!,
            repository = repository,
            initiallyShowShareModal = initiallyShowShare,
            onDismissShareModal = { createdShareCampaign = null },
            onBack = {
                activeCampaign = null
                reloadData()
            },
            onOpenLeaderboard = { showLeaderboardCampaign = activeCampaign },
            onOpenFazilat = { showFazilatModal = activeCampaign!!.title },
            isDarkTheme = isDarkTheme,
            onToggleTheme = {
                isDarkTheme = !isDarkTheme
                PrayerRepository.setDarkTheme(isDarkTheme)
            },
            selectedLanguage = selectedLanguage
        )
        return
    }

    val screenBg = if (isDarkTheme) Color(0xFF04150E) else Color(0xFFF8F6F0)

    // ── Main Screen Guide / Spotlight Tour States ──
    var isMainTourOpen by remember { mutableStateOf(false) }
    var mainTourStep by remember { mutableIntStateOf(1) }

    BackHandler(enabled = isMainTourOpen) {
        repository.setMainTourDone(true)
        isMainTourOpen = false
    }
    var rootCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var statsRowCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var tabsRowCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var actionRowCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var firstCardCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }

    val lazyListState = rememberLazyListState()

    // First-time auto-launch for Main Feed Tour
    LaunchedEffect(Unit) {
        if (!repository.isMainTourDone()) {
            delay(600)
            mainTourStep = 1
            isMainTourOpen = true
        }
    }

    // Auto-scroll when active step changes
    LaunchedEffect(mainTourStep, isMainTourOpen) {
        if (isMainTourOpen) {
            when (mainTourStep) {
                1 -> lazyListState.animateScrollToItem(0) // stats
                2 -> lazyListState.animateScrollToItem(1) // tabs
                3 -> lazyListState.animateScrollToItem(2) // big action cards
                4 -> lazyListState.animateScrollToItem(3) // cards
            }
        }
    }

    val mainTargetRect = remember(mainTourStep, isMainTourOpen, rootCoords, statsRowCoords, tabsRowCoords, actionRowCoords, firstCardCoords) {
        val root = rootCoords ?: return@remember null
        val target = when (mainTourStep) {
            1 -> statsRowCoords
            2 -> tabsRowCoords
            3 -> actionRowCoords
            4 -> firstCardCoords
            else -> null
        }
        if (target != null && target.isAttached) {
            root.localBoundingBoxOf(target, clipBounds = false)
        } else null
    }

    // Outer Box for coordinate reference of the spotlight cutout
    Box(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned { rootCoords = it }
    ) {
        // ── Main Group Campaigns Feed ──
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(screenBg)
        ) {
        // ── 1. HEADER (Curved bottom, Sun/Theme button & Profile button) ──
        Surface(
            color = Color.Transparent,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF0D4E38), Color(0xFF063B2A))
                    )
                )
                .statusBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Back Button
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0x33000000))
                        .border(1.dp, Color(0x33D4AF37), CircleShape)
                        .clickable(onClick = onBack),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(19.dp)
                    )
                }

                // Center Title & Date Capsule
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "LIVE DHIKR",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = ShimmerGold,
                        letterSpacing = 2.sp,
                        fontFamily = FontFamily.Serif
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0x33000000))
                            .border(1.dp, Color(0x55E5C368), RoundedCornerShape(20.dp))
                            .padding(horizontal = 10.dp, vertical = 2.5.dp)
                    ) {
                        Text(
                            text = dateCapsuleText,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = ShimmerGold
                        )
                    }
                }

                // Profile, Help Guide, and Day/Night Theme Toggle
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Guide / Help Button (Opens 4-step Main Spotlight Tour)
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (isDarkTheme) Color(0x33000000) else Color.White)
                            .border(1.dp, Color(0x55D4AF37), CircleShape)
                            .clickable {
                                mainTourStep = 1
                                isMainTourOpen = true
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "❓",
                            fontSize = 15.sp
                        )
                    }

                    // Profile Button (white circular button in Day mode)
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (isDarkTheme) Color(0x33000000) else Color.White)
                            .border(1.dp, Color(0x55D4AF37), CircleShape)
                            .clickable { showProfileModal = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Profile",
                            tint = if (isDarkTheme) ShimmerGold else Color(0xFF0D4E38),
                            modifier = Modifier.size(19.dp)
                        )
                    }

                    // Sun / Moon Day-Night Theme Toggle
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (isDarkTheme) Color(0x33000000) else Color(0xFFFFFBEB))
                            .border(1.dp, if (isDarkTheme) Color(0x55D4AF37) else Color(0x88F59E0B), CircleShape)
                            .clickable {
                                isDarkTheme = !isDarkTheme
                                PrayerRepository.setDarkTheme(isDarkTheme)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isDarkTheme) "🌙" else "☀️",
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // ── 1.5 LANGUAGE BAR (From Home Screen - matching Screenshot 2) ──
        val visibleLanguages = remember {
            listOf(
                LanguageItem("en", "English"),
                LanguageItem("hi", "हिंदी"),
                LanguageItem("ur", "اردو"),
                LanguageItem("ar", "العربية"),
                LanguageItem("gu", "ગુજરાતી")
            )
        }

        LanguageBar(
            languages = visibleLanguages,
            selectedCode = selectedLanguage,
            onSelectLanguage = { code ->
                selectedLanguage = code
                PrayerRepository.saveLanguage(code)
            },
            onOpenMoreLanguages = { isMoreLangOpen = true },
            isDarkTheme = isDarkTheme
        )

        if (repository.isAgeDeclared() && !repository.isUser18Plus()) {
            CommunityMinorRestrictedView(
                isDarkTheme = isDarkTheme,
                onUpdateAge = { showProfileModal = true },
                onBack = { onBack() }
            )
        } else {
            LazyColumn(
                state = lazyListState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // ── 2. ROW 2: STATS & FEATURES (Live & Completed on Top, Aaj Ka Wird & Leaderboard Below) ──
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Top Row: Live Groups & Completed
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned { statsRowCoords = it },
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Card 1: Live Groups Card
                        val liveMedallion = rememberAssetBitmap("media/dc9dfebb0a6ecd0b.webp")
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(DeepEmeraldGradMid, DeepEmeraldGradEnd)
                                    )
                                )
                                .border(1.2.dp, Color(0x3D10B981), RoundedCornerShape(16.dp))
                                .clickable { selectedTab = "public" }
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (liveMedallion != null) {
                                    Image(
                                        bitmap = liveMedallion,
                                        contentDescription = null,
                                        modifier = Modifier.size(38.dp)
                                    )
                                } else {
                                    Icon(Icons.Filled.Groups, contentDescription = null, tint = LuxuryGold, modifier = Modifier.size(32.dp))
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "$liveCount Live",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "Join & recite",
                                        fontSize = 10.sp,
                                        color = TextMutedGreen,
                                        maxLines = 1
                                    )
                                }
                                Text(
                                    text = "›",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = LuxuryGold
                                )
                            }
                        }

                        // Card 2: Completed Card
                        val compMedallion = rememberAssetBitmap("media/01852b09211c093f.webp")
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(DeepEmeraldGradMid, DeepEmeraldGradEnd)
                                    )
                                )
                                .border(1.2.dp, Color(0x3D10B981), RoundedCornerShape(16.dp))
                                .clickable { selectedTab = "history" }
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (compMedallion != null) {
                                    Image(
                                        bitmap = compMedallion,
                                        contentDescription = null,
                                        modifier = Modifier.size(38.dp)
                                    )
                                } else {
                                    Icon(Icons.Filled.Check, contentDescription = null, tint = LuxuryGold, modifier = Modifier.size(32.dp))
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "$completedCount Done",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "Your progress",
                                        fontSize = 10.sp,
                                        color = TextMutedGreen,
                                        maxLines = 1
                                    )
                                }
                                Text(
                                    text = "›",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = LuxuryGold
                                )
                            }
                        }
                    }

                    // Bottom Row: Aaj Ka Wird & Leaderboard (Directly below Live and Done)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Card 3: Aaj Ka Wird Card
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(Color(0xFF0F3A2C), Color(0xFF06281D))
                                    )
                                )
                                .border(1.2.dp, Color(0xFFD4AF37), RoundedCornerShape(16.dp))
                                .clickable {
                                    if (!repository.isProfileDone() || repository.getUserName().isBlank()) {
                                        showProfileModal = true
                                    } else {
                                        showWirdModal = true
                                    }
                                }
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(Color(0x33D4AF37)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Filled.MenuBook,
                                        contentDescription = null,
                                        tint = LuxuryGold,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Aaj Ka Wird",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = LuxuryGold,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "Daily guided zikr",
                                        fontSize = 10.sp,
                                        color = TextMutedGreen,
                                        maxLines = 1
                                    )
                                }
                                Text(
                                    text = "›",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = LuxuryGold
                                )
                            }
                        }

                        // Card 4: Dhikr Leaderboard Card
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(Color(0xFF1E3A2F), Color(0xFF0D251D))
                                    )
                                )
                                .border(1.2.dp, Color(0xFFE2C974), RoundedCornerShape(16.dp))
                                .clickable {
                                    if (!repository.isProfileDone() || repository.getUserName().isBlank()) {
                                        showProfileModal = true
                                    } else {
                                        showWeeklyLeaderboardModal = true
                                    }
                                }
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(Color(0x33E2C974)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Filled.Leaderboard,
                                        contentDescription = null,
                                        tint = Color(0xFFFFD700),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Leaderboard",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "Weekly ranks",
                                        fontSize = 10.sp,
                                        color = TextMutedGreen,
                                        maxLines = 1
                                    )
                                }
                                Text(
                                    text = "›",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = LuxuryGold
                                )
                            }
                        }
                    }
                }
            }

            // ── 3. ROW 3: FIVE PILL BUTTONS (Create, My Groups, Saved, History, Special) ──
            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { tabsRowCoords = it },
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        PillButton(
                            title = "Create",
                            iconPath = "media/8a5a1316bff8e274.webp",
                            fallbackIcon = Icons.Filled.Add,
                            isSelected = selectedTab == "create",
                            isDarkTheme = isDarkTheme,
                            onClick = {
                                if (!repository.isProfileDone() || repository.getUserName().isBlank()) {
                                    showProfileModal = true
                                } else {
                                    showCreateModal = true
                                }
                            }
                        )
                    }
                    item {
                        PillButton(
                            title = "My Groups",
                            iconPath = "media/2de406854aaa71f2.webp",
                            fallbackIcon = Icons.Filled.Groups,
                            isSelected = selectedTab == "mine",
                            isDarkTheme = isDarkTheme,
                            onClick = { selectedTab = "mine" }
                        )
                    }
                    item {
                        PillButton(
                            title = "Saved",
                            iconPath = null,
                            fallbackIcon = Icons.Filled.Bookmark,
                            isSelected = selectedTab == "saved",
                            isDarkTheme = isDarkTheme,
                            onClick = { selectedTab = "saved" }
                        )
                    }
                    item {
                        PillButton(
                            title = "History",
                            iconPath = "media/80b45c4747e66808.webp",
                            fallbackIcon = Icons.Filled.History,
                            isSelected = selectedTab == "history",
                            isDarkTheme = isDarkTheme,
                            onClick = { selectedTab = "history" }
                        )
                    }
                    if (activeSpecialCampaign != null) {
                        item {
                            // Gold Shimmer Special Pill (Only displayed when activeSpecialCampaign exists from Admin)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(0xFFB45309), Color(0xFFD97706), Color(0xFFF59E0B))
                                        )
                                    )
                                    .border(1.2.dp, ShimmerGold, RoundedCornerShape(12.dp))
                                    .clickable { selectedTab = "special" }
                                    .padding(horizontal = 14.dp, vertical = 9.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("👑", fontSize = 12.sp)
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        text = "Special",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ── 4. ROW 4: BIG ACTION CARDS (Live Dhikr & Join with Code - Equal Compact Height) ──
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .onGloballyPositioned { actionRowCoords = it },
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val liveBigMed = rememberAssetBitmap("media/fe3d68e31a113bef.webp")
                    val joinBigMed = rememberAssetBitmap("media/836dd01911de0d69.webp")

                    // Big Action: Live Dhikr (with RED Live pulsing light)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(DeepEmeraldGradMid, DeepEmeraldGradEnd)
                                )
                            )
                            .border(1.5.dp, Color(0x3310B981), RoundedCornerShape(18.dp))
                            .clickable { selectedTab = "public" }
                            .padding(horizontal = 10.dp, vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (liveBigMed != null) {
                                Image(bitmap = liveBigMed, contentDescription = null, modifier = Modifier.size(36.dp))
                            } else {
                                Icon(Icons.Filled.Stars, contentDescription = null, tint = ShimmerGold, modifier = Modifier.size(32.dp))
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    PulsingLiveDot(color = Color(0xFFEF4444))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Live Dhikr",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        maxLines = 1
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Join & stay connected",
                                    fontSize = 9.sp,
                                    color = TextMutedGreen,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .border(1.2.dp, LuxuryGold, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = LuxuryGold,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                    }

                    // Big Action: Join with Code (Compact & Equal Height matching Live Dhikr)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(DeepEmeraldGradMid, DeepEmeraldGradEnd)
                                )
                            )
                            .border(1.5.dp, Color(0x3310B981), RoundedCornerShape(18.dp))
                            .clickable {
                                if (!repository.isProfileDone() || repository.getUserName().isBlank()) {
                                    showProfileModal = true
                                } else {
                                    showJoinCodeModal = true
                                }
                            }
                            .padding(horizontal = 10.dp, vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (joinBigMed != null) {
                                Image(bitmap = joinBigMed, contentDescription = null, modifier = Modifier.size(36.dp))
                            } else {
                                Icon(Icons.Filled.VpnKey, contentDescription = null, tint = LuxuryGold, modifier = Modifier.size(32.dp))
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Join with Code",
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Enter to join group",
                                    fontSize = 9.sp,
                                    color = TextMutedGreen,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .border(1.2.dp, LuxuryGold, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = LuxuryGold,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                    }
                }
            }

            // ── 5. ROW 4.5: GRAND SPECIAL MEGA CAMPAIGN POSTER (Dynamic from Admin) ──
            if (activeSpecialCampaign != null && (selectedTab == "public" || selectedTab == "special")) {
                item {
                    GrandUmrahMegaPoster(
                        campaign = activeSpecialCampaign,
                        isDarkTheme = isDarkTheme,
                        onJoinClick = {
                            if (!NetworkHelper.isOnline(context)) {
                                showNoInternetDialog = true
                            } else {
                                val existingTicket = repository.getStoredMegaTicket(activeSpecialCampaign.campaignId)
                                if (existingTicket == null && !repository.isConsentAccepted()) {
                                    showMegaRegisterModal = true
                                } else {
                                    activeCampaign = activeSpecialCampaign
                                }
                            }
                        },
                        onRegisterClick = {
                            if (!NetworkHelper.isOnline(context)) {
                                showNoInternetDialog = true
                            } else {
                                showMegaRegisterModal = true
                            }
                        },
                        onViewParticipants = {
                            if (!NetworkHelper.isOnline(context)) {
                                showNoInternetDialog = true
                            } else {
                                pendingAdCampaign = activeSpecialCampaign
                            }
                        }
                    )
                }
            }

            // ── 6. CAMPAIGN CARDS FEED ──
            val filtered = when (selectedTab) {
                "mine" -> campaigns.filter { (joinedIds.contains(it.campaignId) || it.isCreator || repository.isCampaignCreator(it.campaignId)) && !repository.isDemoCampaign(it) }
                "saved" -> campaigns.filter { savedIds.contains(it.campaignId) && !repository.isDemoCampaign(it) }
                "history" -> campaigns.filter { it.completed && !repository.isDemoCampaign(it) }
                "special" -> {
                    val list = specialCampaigns.ifEmpty { campaigns.filter { it.isSpecialMega } }
                    list.filter { !it.completed && !repository.isDemoCampaign(it) }
                }
                else -> {
                    if (activeSpecialCampaign != null) {
                        campaigns.filter { !it.completed && it.visibility != "private" && it.campaignId != activeSpecialCampaign.campaignId && !repository.isDemoCampaign(it) }
                    } else {
                        campaigns.filter { !it.completed && it.visibility != "private" && !repository.isDemoCampaign(it) }
                    }
                }
            }

            if (isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = ShimmerGold)
                    }
                }
            } else if (filtered.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 50.dp, bottom = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = when (selectedTab) {
                                "mine" -> "You haven't joined any groups yet."
                                "saved" -> "No saved groups found."
                                "history" -> "No completed groups yet."
                                else -> "No public groups right now."
                            },
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Normal,
                            color = if (isDarkTheme) Color(0xFF9CA3AF) else Color(0xFF5A665E)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Be the first to create one! 🤲",
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDarkTheme) Color(0xFFD1D5DB) else Color(0xFF374151)
                        )
                    }
                }
            } else {
                itemsIndexed(filtered, key = { _, camp -> camp.campaignId }) { index, camp ->
                    val isCreatorCamp = repository.isCampaignCreator(camp.campaignId) || camp.isCreator
                    val isJoinedCamp = joinedIds.contains(camp.campaignId) || isCreatorCamp
                    CampaignCard(
                        campaign = camp,
                        isJoined = isJoinedCamp,
                        isSaved = savedIds.contains(camp.campaignId),
                        isDarkTheme = isDarkTheme,
                        isCreator = isCreatorCamp,
                        modifier = if (index == 0) Modifier.onGloballyPositioned { firstCardCoords = it } else Modifier,
                        onEnter = {
                            if (!repository.isProfileDone() || repository.getUserName().isBlank()) {
                                showProfileModal = true
                            } else {
                                activeCampaign = camp
                            }
                        },
                        onToggleSave = {
                            repository.toggleSaveCampaign(camp.campaignId)
                            savedIds = repository.getSavedCampaignIds()
                        },
                        onJoin = {
                            if (!repository.isProfileDone() || repository.getUserName().isBlank()) {
                                showProfileModal = true
                            } else {
                                coroutineScope.launch {
                                    repository.markCampaignJoined(camp.campaignId)
                                    joinedIds = repository.getJoinedCampaignIds()
                                    AppToast.show("Joined ${camp.title}!")
                                    activeCampaign = camp
                                }
                            }
                        },
                        onShare = {
                            shareModalCampaign = Pair(camp, isCreatorCamp)
                        },
                        onViewLeaderboard = {
                            pendingAdCampaign = camp
                        },
                        onDelete = {
                            campaignToDelete = camp
                        },
                        onReport = {
                            campaignToReport = camp
                        }
                    )
                }
            }
        }
        } // end else for 18+ community check
    }

    // ── MODALS ──
    campaignToReport?.let { camp ->
        ReportGroupModal(
            campaign = camp,
            onDismiss = { campaignToReport = null },
            onSubmit = { reason, notes ->
                coroutineScope.launch {
                    val result = repository.reportGroup(
                        campaignId = camp.campaignId,
                        campaignTitle = camp.title,
                        reason = reason,
                        notes = notes
                    )
                    campaignToReport = null
                    result.onSuccess { msg ->
                        AppToast.show(msg)
                    }.onFailure { err ->
                        AppToast.show(err.message ?: "Failed to report group")
                    }
                }
            },
            isDarkTheme = isDarkTheme
        )
    }

    campaignToDelete?.let { camp ->
        DeleteGroupConfirmDialog(
            campaignTitle = camp.title,
            onWatchAdAndDelete = {
                val toDelete = campaignToDelete
                campaignToDelete = null
                if (toDelete != null) {
                    val act = context.findActivity() ?: activity
                    if (act != null) {
                        AdMobRewardedManager.showAd(act, "Delete Group: ${toDelete.title}") {
                            coroutineScope.launch {
                                repository.deleteGroup(toDelete.campaignId)
                                AppToast.show("Group '${toDelete.title}' deleted successfully 🗑️")
                                if (activeCampaign?.campaignId == toDelete.campaignId) {
                                    activeCampaign = null
                                }
                                reloadData()
                            }
                        }
                    } else {
                        coroutineScope.launch {
                            repository.deleteGroup(toDelete.campaignId)
                            AppToast.show("Group '${toDelete.title}' deleted successfully 🗑️")
                            if (activeCampaign?.campaignId == toDelete.campaignId) {
                                activeCampaign = null
                            }
                            reloadData()
                        }
                    }
                }
            },
            onDismiss = { campaignToDelete = null }
        )
    }

    pendingAdCampaign?.let { camp ->
        LeaderboardAdPromptDialog(
            campaignTitle = camp.title,
            onWatchAd = {
                val campToOpen = pendingAdCampaign
                pendingAdCampaign = null
                if (activity != null && campToOpen != null) {
                    AdMobRewardedManager.showAd(activity, "Group Reciters Leaderboard") {
                        showLeaderboardCampaign = campToOpen
                    }
                } else if (campToOpen != null) {
                    showLeaderboardCampaign = campToOpen
                }
            },
            onDismiss = { pendingAdCampaign = null }
        )
    }
    if (isMoreLangOpen) {
        MoreLanguagesModal(
            selectedCode = selectedLanguage,
            onSelectLanguage = { lang ->
                selectedLanguage = lang
                PrayerRepository.saveLanguage(lang)
                isMoreLangOpen = false
            },
            onDismiss = { isMoreLangOpen = false }
        )
    }

    if (showCreateModal) {
        CreateGroupZikrScreen(
            repository = repository,
            selectedLanguage = selectedLanguage,
            onDismiss = { showCreateModal = false },
            onCreate = { title, arabic, target, isPriv, city, isSpecialMega, note ->
                showCreateModal = false
                coroutineScope.launch {
                    val res = repository.createGroup(
                        title = title,
                        arabic = arabic,
                        target = target,
                        isPrivate = isPriv,
                        displayName = repository.getUserName(),
                        city = city,
                        note = note,
                        isSpecialMega = isSpecialMega
                    )
                    res.onSuccess { newCamp ->
                        reloadData()
                        activeCampaign = newCamp
                        createdShareCampaign = newCamp
                        if (isSpecialMega) {
                            val config = com.tasbih.digitaltasbih.data.HomeAnnouncementConfig(
                                isEnabled = true,
                                campaignId = newCamp.campaignId,
                                badgeText = "✨ SPECIAL CAMPAIGN LIVE",
                                arabicText = arabic.ifBlank { "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ" },
                                title = title,
                                prizeTitle = "🏆 GLOBAL ISTIQAMAH CAMPAIGN",
                                message = note.ifBlank { "Join thousands of believers in this special live campaign." },
                                targetText = "$target Goal",
                                buttonText = "Alhamdulillah! Shamil Huein ✓",
                                version = System.currentTimeMillis()
                            )
                            com.tasbih.digitaltasbih.data.PrayerRepository.saveHomeAnnouncementConfig(config)
                        }
                    }
                }
            },
            onOpenAdmin = {
                showMegaRegisterModal = true
            },
            isDarkTheme = isDarkTheme,
            onToggleTheme = {
                isDarkTheme = !isDarkTheme
                PrayerRepository.setDarkTheme(isDarkTheme)
            }
        )
    }

    if (showJoinCodeModal) {
        JoinByCodeDialog(
            onDismiss = { showJoinCodeModal = false },
            onJoin = { code ->
                showJoinCodeModal = false
                coroutineScope.launch {
                    val alreadyJoined = joinedIds.any { id ->
                        campaigns.find { it.campaignId == id }?.joinCode.equals(code.trim(), ignoreCase = true)
                    } || repository.getCreatedCampaignIds().any { id ->
                        campaigns.find { it.campaignId == id }?.joinCode.equals(code.trim(), ignoreCase = true)
                    }

                    val res = repository.joinByCode(code)
                    res.onSuccess { matched ->
                        reloadData()
                        activeCampaign = matched
                        if (alreadyJoined) {
                            AppToast.show("Welcome back! You are already in ${matched.title}")
                        } else {
                            AppToast.show("Joined group: ${matched.title}!")
                        }
                    }.onFailure {
                        AppToast.show("Invalid join code. Please check and try again.")
                    }
                }
            }
        )
    }

    if (showMegaRegisterModal && activeSpecialCampaign != null) {
        UmrahMegaRegisterModal(
            campaign = activeSpecialCampaign,
            repository = repository,
            isDarkTheme = isDarkTheme,
            onDismiss = { showMegaRegisterModal = false },
            onSuccess = { ticket ->
                showMegaRegisterModal = false
                reloadData()
                activeCampaign = activeSpecialCampaign
                AppToast.show("🎉 Mubarak! Registered Ticket #${ticket.ticketNumber}")
            }
        )
    }

    if (showNoInternetDialog) {
        NoInternetConnectionDialog(
            onDismiss = { showNoInternetDialog = false },
            onRetry = {
                if (NetworkHelper.isOnline(context)) {
                    showNoInternetDialog = false
                    reloadData()
                    AppToast.show("Connected to internet!")
                } else {
                    AppToast.show("Still offline. Kindly turn on Wi-Fi or Mobile Data.")
                }
            }
        )
    }

    if (showProfileModal) {
        WelcomeToGroupCampaignsDialog(
            repository = repository,
            onDismiss = {
                showProfileModal = false
                reloadData(showLoading = false)
            }
        )
    }

    if (showWirdModal) {
        AajKaWirdModal(
            repository = repository,
            isDarkTheme = isDarkTheme,
            onDismiss = { showWirdModal = false },
            onReciteWird = { wirdItem ->
                showWirdModal = false
                val wirdCampaign = CampaignItem(
                    campaignId = "wird_" + wirdItem.date.replace("-", "_"),
                    title = wirdItem.title,
                    arabic = wirdItem.arabic,
                    translit = wirdItem.translit,
                    meaning = wirdItem.meaning,
                    target = wirdItem.target,
                    creatorName = "Aaj Ka Wird",
                    note = wirdItem.virtue,
                    visibility = "public"
                )
                activeCampaign = wirdCampaign
            }
        )
    }

    if (showWeeklyLeaderboardModal) {
        WeeklyLeaderboardModal(
            repository = repository,
            isDarkTheme = isDarkTheme,
            onDismiss = { showWeeklyLeaderboardModal = false }
        )
    }

    showLeaderboardCampaign?.let { camp ->
        LeaderboardModalSheet(
            campaign = camp,
            campaignId = camp.campaignId,
            repository = repository,
            onDismiss = { showLeaderboardCampaign = null }
        )
    }

    showFazilatModal?.let { title ->
        FazilatModalSheet(
            title = title,
            onDismiss = { showFazilatModal = null }
        )
    }

    shareModalCampaign?.let { (camp, isCreator) ->
        GroupShareModal(
            campaign = camp,
            isCreator = isCreator,
            isDarkTheme = isDarkTheme,
            selectedLanguage = selectedLanguage,
            onDismiss = { shareModalCampaign = null }
        )
    }

    if (createdShareCampaign != null && activeCampaign == null) {
        GroupShareModal(
            campaign = createdShareCampaign!!,
            isCreator = true,
            isDarkTheme = isDarkTheme,
            selectedLanguage = selectedLanguage,
            onDismiss = { createdShareCampaign = null }
        )
    }

    // ── Main Tour Spotlight Overlay ──
    if (isMainTourOpen && activeCampaign == null && !showCreateModal && !showProfileModal && !showJoinCodeModal) {
        val tourStrings = LiveDhikrTourLocalization.get(selectedLanguage)
        val (title, desc) = when (mainTourStep) {
            1 -> tourStrings.mainMegaTitle to tourStrings.mainMegaDesc
            2 -> tourStrings.mainTabsTitle to tourStrings.mainTabsDesc
            3 -> tourStrings.mainActionTitle to tourStrings.mainActionDesc
            4 -> tourStrings.mainCardsTitle to tourStrings.mainCardsDesc
            else -> "" to ""
        }

        LiveDhikrSpotlightTourModal(
            step = mainTourStep,
            totalSteps = 4,
            targetRect = mainTargetRect,
            stepLabel = tourStrings.step.replace("{curr}", mainTourStep.toString()).replace("{total}", "4"),
            title = title,
            description = desc,
            skipBtnText = tourStrings.skip,
            nextBtnText = tourStrings.next,
            doneBtnText = tourStrings.done,
            onNext = {
                if (mainTourStep < 4) {
                    mainTourStep++
                } else {
                    repository.setMainTourDone(true)
                    isMainTourOpen = false
                }
            },
            onDone = {
                repository.setMainTourDone(true)
                isMainTourOpen = false
            },
            onSkip = {
                repository.setMainTourDone(true)
                isMainTourOpen = false
            }
        )
    }
}
}

// ── Ultra-Luxury Grand Special Mega Campaign Poster (Dynamic from Admin) ──
@Composable
private fun GrandUmrahMegaPoster(
    campaign: CampaignItem,
    isDarkTheme: Boolean = false,
    onJoinClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onViewParticipants: () -> Unit = {}
) {
    val cardBg = if (isDarkTheme) DarkCardBg else Color.White
    val cardBorder = if (isDarkTheme) DarkCardBorder else Color(0xFF053B2C)
    val titleColor = if (isDarkTheme) Color.White else Color(0xFF0F172A)
    val arabicColor = if (isDarkTheme) ShimmerGold else Color(0xFF053B2C)
    val descColor = if (isDarkTheme) ParchmentGold.copy(alpha = 0.9f) else Color(0xFF475569)
    val subRuleColor = if (isDarkTheme) ShimmerGold else Color(0xFF059669)
    val statsTextColor = if (isDarkTheme) TextMutedGreen else Color(0xFF475569)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .border(
                BorderStroke(
                    1.8.dp,
                    if (isDarkTheme) {
                        Brush.linearGradient(listOf(ShimmerGold, LuxuryGold, BrightGold))
                    } else {
                        Brush.linearGradient(listOf(Color(0xFF053B2C), Color(0xFF10B981), Color(0xFFC9A84C)))
                    }
                ),
                RoundedCornerShape(24.dp)
            )
            .clickable { onJoinClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isDarkTheme) 0.dp else 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Top Bar Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val ribbon = campaign.badgeRibbon.ifBlank { "👑 SPECIAL MEGA CAMPAIGN" }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isDarkTheme) Brush.linearGradient(listOf(Color(0xFFB45309), Color(0xFF92400E)))
                                else Brush.horizontalGradient(listOf(Color(0xFF053B2C), Color(0xFF07523D)))
                            )
                            .border(1.dp, Color(0xFFC9A84C), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(ribbon, fontSize = 9.5.sp, fontWeight = FontWeight.ExtraBold, color = if (isDarkTheme) ParchmentGold else Color.White)
                    }
                    val prize = campaign.prizeTitle.trim().ifBlank { "1 Free Umrah Package" }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isDarkTheme) Color(0xFF047857) else Color(0xFFECFDF5))
                            .border(1.dp, if (isDarkTheme) Color(0xFF059669) else Color(0xFFA7F3D0), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (prize.contains("UMRAH", ignoreCase = true) && !prize.startsWith("🕋") && !prize.startsWith("🏆")) "🕋 $prize" else prize,
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isDarkTheme) Color.White else Color(0xFF065F46)
                        )
                    }
                }

                val targetText = if (campaign.target >= 100_000_000L) {
                    "Target: 10 Crore+"
                } else if (campaign.target >= 50_000_000L) {
                    "Target: 5 Crore+"
                } else if (campaign.target >= 10_000_000L) {
                    "Target: 1 Crore+"
                } else if (campaign.target >= 5_000_000L) {
                    "Target: 50 Lakh+"
                } else if (campaign.target >= 1_000_000L) {
                    "Target: ${(campaign.target / 100_000L)} Lakh+"
                } else {
                    "Target: ${NumberFormat.getNumberInstance(Locale.US).format(campaign.target)}"
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isDarkTheme) Color(0x33FCD34D) else Color(0xFFFFFBEB))
                        .border(1.dp, if (isDarkTheme) BrightGold else Color(0xFFFDE68A), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = targetText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) ShimmerGold else Color(0xFFB45309)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Arabic Calligraphy Zone
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (campaign.arabic.isNotBlank()) {
                    Text(
                        text = campaign.arabic,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontFamily = AmiriBoldFont,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = arabicColor,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Text(
                    text = campaign.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = titleColor,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                val rulesDesc = campaign.contestRules.ifBlank {
                    "Join this blessed Muhim, recite ${campaign.title}, and get a chance to win ${campaign.prizeTitle}!"
                }
                Text(
                    text = rulesDesc,
                    fontSize = 11.5.sp,
                    color = descColor,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "✨ Rule: Minimum 100 Recitations/Day to Qualify for Lucky Draw",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = subRuleColor,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Bar Section
            val pct = campaign.progressPercent
            val currentCountFormatted = NumberFormat.getNumberInstance(Locale.US).format(campaign.totalCount)
            val targetFormatted = if (campaign.target >= 10_000_000L) "${campaign.target / 10_000_000L} Crore" else NumberFormat.getNumberInstance(Locale.US).format(campaign.target)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isDarkTheme) Color(0x33000000) else Color(0xFFF8FAFC))
                    .border(1.dp, if (isDarkTheme) Color(0x22FFFFFF) else Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Recited: $currentCountFormatted",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) Color(0xFF34D399) else Color(0xFF053B2C)
                        )
                        Text(
                            text = "Target: $targetFormatted",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) ShimmerGold else Color(0xFFB45309)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { pct },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(999.dp)),
                        color = if (isDarkTheme) ShimmerGold else Color(0xFF10B981),
                        trackColor = if (isDarkTheme) Color(0x22FFFFFF) else Color(0xFFE2E8F0)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Stats row (Reciters, Joined, Free Ticket Pill)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onViewParticipants() }
                        .padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PulsingLiveDot(color = Color(0xFFEF4444))
                    Spacer(modifier = Modifier.width(4.dp))
                    val reciters = (campaign.participantsCount / 160).coerceAtLeast(3)
                    Text("$reciters Reciting", fontSize = 10.sp, color = if (isDarkTheme) Color.White else Color(0xFF0F172A), fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("•", fontSize = 10.sp, color = LuxuryGold)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("👥 ${NumberFormat.getNumberInstance(Locale.US).format(campaign.participantsCount.coerceAtLeast(1))} Joined • View ➔", fontSize = 10.sp, color = statsTextColor)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isDarkTheme) Color(0x3DFCD34D) else Color(0xFFFFFDF5))
                        .border(1.2.dp, if (isDarkTheme) LuxuryGold else Color(0xFFC9A84C), RoundedCornerShape(8.dp))
                        .clickable { onRegisterClick() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("🎟️ Free Draw Entry", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isDarkTheme) ShimmerGold else Color(0xFFB45309))
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Full-Width CTA Button
            Button(
                onClick = onJoinClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDarkTheme) BrightGold else Color(0xFF053B2C)
                ),
                border = if (!isDarkTheme) BorderStroke(1.2.dp, Color(0xFFC9A84C)) else null,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Join Blessed Muhim & Recite Live ➔",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isDarkTheme) Color(0xFF1E1B0E) else Color.White
                )
            }
        }
    }
}

// ── Ultra-Luxury Campaign Card Item ──
@Composable
private fun CampaignCard(
    campaign: CampaignItem,
    isJoined: Boolean,
    isSaved: Boolean,
    isDarkTheme: Boolean = false,
    isCreator: Boolean = false,
    modifier: Modifier = Modifier,
    onEnter: () -> Unit,
    onToggleSave: () -> Unit,
    onJoin: () -> Unit,
    onShare: () -> Unit,
    onViewLeaderboard: () -> Unit = {},
    onDelete: () -> Unit = {},
    onReport: () -> Unit = {}
) {
    val cardBg = if (isDarkTheme) DarkCardBg else Color.White
    val cardBorder = if (isDarkTheme) DarkCardBorder else Color(0xFF064E3B)
    val titleColor = if (isDarkTheme) Color.White else Color(0xFF053B2C)
    val secondaryColor = if (isDarkTheme) TextMutedGreen else Color(0xFF4A6D5F)
    val numberColor = if (isDarkTheme) Color(0xFF34D399) else Color(0xFF053B2C)
    val targetNumberColor = if (isDarkTheme) Color.White else Color(0xFF1A1A1A)

    val formattedDate = remember(campaign.createdAt) {
        val ts = if (campaign.createdAt > 0L) campaign.createdAt else System.currentTimeMillis()
        SimpleDateFormat("dd-MM-yyyy", Locale.US).format(Date(ts))
    }

    val pct = (campaign.progressPercent * 100).toInt()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .border(1.5.dp, cardBorder, RoundedCornerShape(24.dp))
            .clickable { if (isJoined) onEnter() else onJoin() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // ── Top Row: • LIVE Badge & Date ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // • LIVE & Private Badges
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isDarkTheme) Color(0x33EF4444) else Color(0xFFFEE2E2))
                            .border(1.dp, if (isDarkTheme) Color(0x66EF4444) else Color(0xFFFCA5A5), RoundedCornerShape(16.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            PulsingLiveDot(color = Color(0xFFEF4444))
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = if (campaign.completed) "COMPLETED" else "LIVE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFEF4444)
                            )
                        }
                    }

                    if (isCreator && campaign.visibility == "private" && campaign.joinCode.isNotBlank()) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isDarkTheme) Color(0x33C9A84C) else Color(0xFFFEF3C7))
                                .border(1.dp, Color(0xFFF59E0B), RoundedCornerShape(16.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "🔒 Invite: ${campaign.joinCode}",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF92400E)
                            )
                        }
                    } else if (campaign.visibility == "private") {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isDarkTheme) Color(0x2610B981) else Color(0xFFECFDF5))
                                .border(1.dp, Color(0xFFA7F3D0), RoundedCornerShape(16.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "🔒 Private",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF065F46)
                            )
                        }
                    }
                }

                // Date with Calendar icon
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.CalendarToday,
                        contentDescription = null,
                        tint = if (isDarkTheme) ParchmentGold.copy(alpha = 0.7f) else Color(0xFF7A6C4F),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = formattedDate,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isDarkTheme) ParchmentGold.copy(alpha = 0.7f) else Color(0xFF7A6C4F)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Creator & Location ──
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                        tint = if (isDarkTheme) ShimmerGold else Color(0xFF07523D),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = campaign.creatorName.ifBlank { "AAhmadev" },
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = titleColor
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = null,
                        tint = if (isDarkTheme) TextMutedGreen else Color(0xFF07523D),
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = campaign.location.ifBlank { "Global Community" },
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = secondaryColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ── Centered Dhikr Display Box ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isDarkTheme) Color(0x1AFFFFFF) else Color.White)
                    .border(1.5.dp, if (isDarkTheme) Color(0x33C9A84C) else Color(0xFF064E3B), RoundedCornerShape(20.dp))
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (campaign.arabic.isNotBlank()) {
                        Text(
                            text = campaign.arabic,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = AmiriBoldFont,
                                fontSize = 22.sp
                            ),
                            color = if (isDarkTheme) ShimmerGold else Color(0xFF053B2C),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                    Text(
                        text = campaign.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        color = titleColor,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ── Recited vs Target Progress Box ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isDarkTheme) Color(0x0DFFFFFF) else Color(0xFFF8FDF9))
                    .border(1.5.dp, if (isDarkTheme) Color(0x22C9A84C) else Color(0xFF064E3B), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(
                                text = "RECITED",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.8.sp,
                                color = if (isDarkTheme) TextMutedGreen else Color(0xFF527364)
                            )
                            Text(
                                text = NumberFormat.getNumberInstance(Locale.US).format(campaign.totalCount),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                color = numberColor
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "TARGET",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.8.sp,
                                color = if (isDarkTheme) TextMutedGreen else Color(0xFF527364)
                            )
                            Text(
                                text = NumberFormat.getNumberInstance(Locale.US).format(campaign.target),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = targetNumberColor
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Progress Track & Fill
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isDarkTheme) Color(0x1FFFFFFF) else Color(0x1F07523D))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(campaign.progressPercent)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFF053B2C), Color(0xFF07523D))
                                    )
                                )
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Group progress",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) TextMutedGreen else Color(0xFF527364)
                        )
                        Text(
                            text = "$pct%",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = numberColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // ── View Reciters Breakdown Pill ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isDarkTheme) Color(0x1AFFFFFF) else Color(0xFFF1F5F9))
                    .border(1.dp, if (isDarkTheme) Color(0x33C9A84C) else Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                    .clickable { onViewLeaderboard() }
                    .padding(horizontal = 12.dp, vertical = 7.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("👥", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        val membersCount = campaign.participantsCount.coerceAtLeast(1)
                        Text(
                            text = "$membersCount Joined • View Reciters & Leaderboard",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDarkTheme) ShimmerGold else Color(0xFF0F766E)
                        )
                    }
                    Text(
                        text = "➔",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) ShimmerGold else Color(0xFF0F766E)
                    )
                }
            }

            if (campaign.note.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isDarkTheme) Color(0x1110B981) else Color(0xFFF0FDF4))
                        .border(1.dp, if (isDarkTheme) Color(0x3310B981) else Color(0x33064E3B), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "\"${campaign.note.trim()}\"",
                        fontSize = 11.5.sp,
                        fontStyle = FontStyle.Italic,
                        color = secondaryColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ── Action Buttons Row: Join + Share + Save ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Wide Primary Join / Recite Live Button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF053B2C), Color(0xFF07523D), Color(0xFF096349))
                            )
                        )
                        .border(1.dp, Color(0x6610B981), RoundedCornerShape(14.dp))
                        .clickable { if (isJoined) onEnter() else onJoin() },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = if (isJoined) Icons.Filled.PlayArrow else Icons.Outlined.PersonAdd,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isJoined) "Recite Live" else "Join",
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }

                // Share circular button
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(if (isDarkTheme) Color(0x1FFFFFFF) else Color.White)
                        .border(1.5.dp, Color(0xFF07523D), CircleShape)
                        .clickable { onShare() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Send,
                        contentDescription = "Share",
                        tint = if (isDarkTheme) ShimmerGold else Color(0xFF07523D),
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Bookmark circular button
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF053B2C))
                        .border(1.5.dp, BrightGold, CircleShape)
                        .clickable { onToggleSave() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Save",
                        tint = if (isSaved) ShimmerGold else Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Report button (Community Safety)
                val reportBadge = rememberAssetBitmap("media/report_alert_badge.webp")
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(if (isDarkTheme) Color(0x22EF4444) else Color(0xFFFEF2F2))
                        .border(1.5.dp, if (isDarkTheme) Color(0x66EF4444) else Color(0xFFFCA5A5), CircleShape)
                        .clickable { onReport() },
                    contentAlignment = Alignment.Center
                ) {
                    if (reportBadge != null) {
                        Image(
                            bitmap = reportBadge,
                            contentDescription = "Report Group",
                            modifier = Modifier.size(30.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.Flag,
                            contentDescription = "Report Group",
                            tint = Color(0xFFDC2626),
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }

                // Delete button for group creator
                if (isCreator) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(if (isDarkTheme) Color(0x33EF4444) else Color(0xFFFEE2E2))
                            .border(1.5.dp, if (isDarkTheme) Color(0x66EF4444) else Color(0xFFFCA5A5), CircleShape)
                            .clickable { onDelete() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Group",
                            tint = Color(0xFFDC2626),
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun LiveActiveDhikrRoom(
    campaign: CampaignItem,
    repository: GroupCampaignRepository,
    selectedLanguage: String = "en",
    onBack: () -> Unit,
    onOpenLeaderboard: () -> Unit,
    onOpenFazilat: () -> Unit,
    isDarkTheme: Boolean = false,
    onToggleTheme: () -> Unit,
    initiallyShowShareModal: Boolean = false,
    onDismissShareModal: () -> Unit = {}
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()

    val tourStrings = remember(selectedLanguage) { LiveDhikrTourLocalization.get(selectedLanguage) }
    var isRoomTourOpen by remember { mutableStateOf(!repository.isRoomTourDone()) }
    var roomTourStep by remember { mutableIntStateOf(1) }

    val roomScrollState = rememberScrollState()
    var roomRootCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var infoCardCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var dialCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var autoControlCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var leaderboardBarCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var submitBtnCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }

    val currentRoomTourRect: Rect? = remember(
        isRoomTourOpen,
        roomTourStep,
        roomRootCoords,
        infoCardCoords,
        dialCoords,
        autoControlCoords,
        leaderboardBarCoords,
        submitBtnCoords,
        roomScrollState.value
    ) {
        val root = roomRootCoords
        if (root != null && root.isAttached) {
            val target = when (roomTourStep) {
                1 -> infoCardCoords
                2 -> dialCoords
                3 -> autoControlCoords
                4 -> leaderboardBarCoords
                5 -> submitBtnCoords
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

    LaunchedEffect(isRoomTourOpen, roomTourStep) {
        if (isRoomTourOpen) {
            when (roomTourStep) {
                1 -> {
                    delay(200L)
                    try { roomScrollState.animateScrollTo(0) } catch (_: Exception) {}
                }
                2, 3 -> {
                    delay(150L)
                    try { roomScrollState.animateScrollTo(200) } catch (_: Exception) {}
                }
                4 -> {
                    delay(150L)
                    try { roomScrollState.animateScrollTo(420) } catch (_: Exception) {}
                }
                5 -> {
                    delay(150L)
                    try { roomScrollState.animateScrollTo(roomScrollState.maxValue) } catch (_: Exception) {}
                }
            }
        }
    }

    var showShareModal by remember { mutableStateOf(initiallyShowShareModal) }
    var showLeaderboardModal by remember { mutableStateOf(false) }
    var showAdPrompt by remember { mutableStateOf(false) }
    var showFazilatModal by remember { mutableStateOf(false) }
    var showDeleteConfirmModal by remember { mutableStateOf(false) }
    var showNamePromptDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        AdMobRewardedManager.loadAd(context)
    }
    val initialSavedPending = remember { repository.getPendingTaps(campaign.campaignId) }
    var pendingCount by remember { mutableIntStateOf(initialSavedPending) }
    var sessionCount by remember { mutableIntStateOf(0) }
    var liveTotal by remember { mutableLongStateOf(campaign.totalCount) }
    var isSubmitting by remember { mutableStateOf(false) }

    // Smart Auto Counter Engine
    var isAutoRunning by remember { mutableStateOf(false) }
    var autoSpeedMs by remember { mutableLongStateOf(2000L) }
    var showAutoModal by remember { mutableStateOf(false) }

    var isSoundOn by remember { mutableStateOf(true) }
    var isVibrateOn by remember { mutableStateOf(true) }

    // Marble Beads Tasbih State
    var isBeadsViewMode by remember { mutableStateOf(false) }
    var activeSkin by remember { mutableStateOf(TasbihRepository.getActiveBeadSkin()) }
    var showMarbleGalleryModal by remember { mutableStateOf(false) }

    // ── Anti-Cheat Engine (Active for all campaigns; strict mode for Special Mega) ──
    val isSpecialCampaign = campaign.isSpecialMega
    // Max realistic human tap speed: 1 tap per 250ms (4 taps/sec). Auto-counter: 1 per 3000ms for special.
    val minTapIntervalMs = if (isSpecialCampaign) 250L else 150L
    val autoMinSpeedMs   = if (isSpecialCampaign) 3000L else 1500L
    // Max counted per single batch submission
    val maxBatchSize     = 50
    // Max session count per hour (3600 = 1/sec sustained for an hour — very generous)
    val maxPerHour       = if (isSpecialCampaign) 3600 else Int.MAX_VALUE

    var lastTapTimestamp by remember { mutableLongStateOf(0L) }
    val sessionStartMs   = remember { System.currentTimeMillis() }
    var hourlyCount      by remember { mutableIntStateOf(0) }
    var hourBucketStart  by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var cheatWarningShown by remember { mutableStateOf(false) }

    fun isRateLimited(): Boolean {
        if (!isSpecialCampaign) return false
        val now = System.currentTimeMillis()
        // Reset hourly bucket
        if (now - hourBucketStart > 3_600_000L) {
            hourlyCount = 0
            hourBucketStart = now
        }
        return hourlyCount >= maxPerHour
    }

    fun doCountStep(amount: Int = 1) {
        val now = System.currentTimeMillis()

        // ── Rate-limit check (special campaign only) ──
        if (isSpecialCampaign) {
            val elapsed = now - lastTapTimestamp
            if (lastTapTimestamp > 0L && elapsed < minTapIntervalMs) {
                // Tap too fast — silently ignore (no toast to avoid spam)
                return
            }
            if (isRateLimited()) {
                if (!cheatWarningShown) {
                    cheatWarningShown = true
                    AppToast.show("Daily limit reached. JazakAllah! Come back tomorrow. 🤲")
                }
                return
            }
            hourlyCount += amount
        }
        lastTapTimestamp = now

        pendingCount += amount
        repository.savePendingTaps(campaign.campaignId, pendingCount)
        sessionCount += amount
        liveTotal += amount

        val isMilestone = (sessionCount % 33 == 0)
        if (isVibrateOn) {
            if (isMilestone) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            else haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
        if (isSoundOn) {
            TasbihAudioEngine.playSandalwoodClick()
            if (isMilestone) TasbihAudioEngine.playCompletionChime()
        }

        if (isAutoRunning && sessionCount % 100 == 0) {
            isAutoRunning = false
            if (isVibrateOn) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            AppToast.show("Completed 100 recitations. Paused for reflection.")
        }
    }

    // Enforce minimum auto-counter speed for special campaigns
    LaunchedEffect(isAutoRunning, autoSpeedMs) {
        val effectiveSpeed = if (isSpecialCampaign) autoSpeedMs.coerceAtLeast(autoMinSpeedMs) else autoSpeedMs
        while (isAutoRunning) {
            delay(effectiveSpeed)
            doCountStep(1)
        }
    }

    fun submitBatch() {
        if (pendingCount <= 0 || isSubmitting) return
        val currentName = repository.getUserName().trim()
        if (currentName.isBlank() || currentName.equals("Devotee", ignoreCase = true)) {
            showNamePromptDialog = true
            return
        }
        // Clamp batch size to prevent unrealistically large single submissions
        val countToSync = pendingCount.coerceAtMost(maxBatchSize)
        pendingCount = (pendingCount - countToSync).coerceAtLeast(0)
        repository.savePendingTaps(campaign.campaignId, pendingCount)
        isSubmitting = true
        val sessionDurationMs = System.currentTimeMillis() - sessionStartMs
        coroutineScope.launch {
            val res = repository.submitCount(
                campaignId = campaign.campaignId,
                count = countToSync,
                sessionDurationMs = sessionDurationMs,
                isSpecial = isSpecialCampaign
            )
            res.onSuccess { data ->
                if (data.newTotal > 0) {
                    liveTotal = data.newTotal
                    repository.updateCampaignTotal(campaign.campaignId, data.newTotal)
                }
                repository.savePendingTaps(campaign.campaignId, pendingCount)
                repository.syncLeaderboardDhikr(countToSync)
            }.onFailure { ex ->
                val msg = ex.message ?: ""
                val isPermanent = msg.contains("not found", ignoreCase = true) ||
                                  msg.contains("completed", ignoreCase = true) ||
                                  msg.contains("closed", ignoreCase = true) ||
                                  msg.contains("forbidden", ignoreCase = true) ||
                                  msg.contains("not a participant", ignoreCase = true) ||
                                  msg.contains("invalid", ignoreCase = true)
                if (!isPermanent) {
                    // Restore unsubmitted taps back into pendingCount on transient failure (e.g. 429 rate limit or network glitch)
                    pendingCount += countToSync
                    repository.savePendingTaps(campaign.campaignId, pendingCount)
                }
            }
            isSubmitting = false
        }
    }

    BackHandler {
        submitBatch()
        repository.updateCampaignTotal(campaign.campaignId, liveTotal)
        onBack()
    }

    LaunchedEffect(Unit) {
        if (initialSavedPending > 0) {
            submitBatch()
        }
    }

    LaunchedEffect(pendingCount) {
        if (pendingCount >= 30) {
            submitBatch()
        }
    }

    LaunchedEffect(campaign.campaignId) {
        try {
            val initialServerTotal = repository.getCampaignTotal(campaign.campaignId)
            if (initialServerTotal != null && initialServerTotal > liveTotal) {
                liveTotal = initialServerTotal
                repository.updateCampaignTotal(campaign.campaignId, initialServerTotal)
            }
        } catch (_: Exception) {}

        while (true) {
            delay(6000L)
            try {
                val serverTotal = repository.getCampaignTotal(campaign.campaignId)
                if (serverTotal != null && serverTotal > liveTotal) {
                    liveTotal = serverTotal
                    repository.updateCampaignTotal(campaign.campaignId, serverTotal)
                }
            } catch (_: Exception) {}
        }
    }

    val userLang = remember { PrayerRepository.getLanguage() }

    fun shareCampaign() {
        val isCreator = campaign.isCreator || repository.isCampaignCreator(campaign.campaignId)
        GroupInviteHelper.shareCampaign(context, campaign, isCreator, userLang)
    }

    val pageBg = if (isDarkTheme) Color(0xFF041910) else Color(0xFFF4F8F5)
    val cardBg = if (isDarkTheme) DarkCardBg else Color.White
    val cardBorder = if (isDarkTheme) DarkCardBorder else Color(0xFF064E3B)
    val remaining = (campaign.target - liveTotal).coerceAtLeast(0L)
    val pct = if (campaign.target > 0) ((liveTotal.toFloat() / campaign.target) * 100).toInt().coerceIn(0, 100) else 0

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { roomRootCoords = it }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(pageBg)
                .statusBarsPadding()
                .verticalScroll(roomScrollState)
        ) {
            // ── Top Header Bar (Matching Image 4) ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.horizontalGradient(listOf(DeepEmeraldGradMid, DeepEmeraldGradEnd)))
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Back circular button
                    IconButton(onClick = {
                        submitBatch()
                        repository.updateCampaignTotal(campaign.campaignId, liveTotal)
                        onBack()
                    }) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0x2EFFFFFF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Title
                    Text(
                        text = campaign.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 4.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // • LIVE red pill badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFDC2626))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "LIVE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Share Button
                    IconButton(onClick = { shareCampaign() }) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0x2EFFFFFF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Share,
                                contentDescription = "Share",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Sound Toggle Button
                    IconButton(onClick = {
                        isSoundOn = !isSoundOn
                        AppToast.show(if (isSoundOn) "Sound ON" else "Sound OFF")
                    }) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0x2EFFFFFF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(if (isSoundOn) "🔊" else "🔇", fontSize = 16.sp)
                        }
                    }

                    // Help / Guide Button
                    IconButton(onClick = {
                        roomTourStep = 1
                        isRoomTourOpen = true
                    }) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0x2EFFFFFF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("❓", fontSize = 15.sp)
                        }
                    }

                    // Day/Night Toggle Button
                    IconButton(onClick = onToggleTheme) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0x2EFFFFFF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isDarkTheme) Icons.Filled.Nightlight else Icons.Filled.WbSunny,
                                contentDescription = "Toggle Theme",
                                tint = ShimmerGold,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Delete Group Button (Visible only to Group Creator)
                    val isRoomCreator = campaign.isCreator || repository.isCampaignCreator(campaign.campaignId)
                    if (isRoomCreator) {
                        IconButton(onClick = { showDeleteConfirmModal = true }) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x33EF4444)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Group",
                                    tint = Color(0xFFFF6B6B),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Top Info Card (Matching Image 4) ──
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
                    .onGloballyPositioned { infoCardCoords = it }
                    .clip(RoundedCornerShape(22.dp))
                    .border(1.5.dp, cardBorder, RoundedCornerShape(22.dp)),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Arabic Calligraphy
                Text(
                    text = if (campaign.arabic.isNotBlank()) campaign.arabic else "أَسْتَغْفِرُ اللَّهَ",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = AmiriBoldFont,
                        fontSize = 28.sp
                    ),
                    color = if (isDarkTheme) ShimmerGold else Color(0xFF053B2C),
                    textAlign = TextAlign.Center
                )

                // Transliteration / title
                Text(
                    text = campaign.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    color = if (isDarkTheme) Color.White else Color(0xFF1B382D),
                    textAlign = TextAlign.Center
                )

                val isRoomCreator = campaign.isCreator || repository.isCampaignCreator(campaign.campaignId)
                val isPrivate = campaign.visibility == "private"
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isDarkTheme) {
                                if (isPrivate) Color(0x33C9A84C) else Color(0x2610B981)
                            } else {
                                if (isPrivate) Color(0xFFFEF3C7) else Color(0xFFECFDF5)
                            }
                        )
                        .border(
                            1.dp,
                            if (isPrivate) Color(0xFFF59E0B) else Color(0xFFA7F3D0),
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { showShareModal = true }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val pillTitle = if (isRoomCreator) {
                            if (isPrivate) "🔒 Creator Code: ${campaign.joinCode}"
                            else "📢 Share Group"
                        } else {
                            "📢 Invite Friends to Dhikr"
                        }
                        val pillColor = if (isPrivate) Color(0xFF92400E) else Color(0xFF047857)
                        Text(
                            text = pillTitle,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = pillColor
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = pillColor,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Badges Row 1: Remaining & Active Members
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Sand/Beige Pill: ⏳ Remaining
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isDarkTheme) Color(0x33F59E0B) else Color(0xFFFFFBEB))
                            .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "• ⏳ $remaining Remaining",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF92400E)
                        )
                    }

                    // Active Members (Live reciters indicator - Clickable to open Leaderboard)
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isDarkTheme) Color(0x334F46E5) else Color(0xFFEEF2FF))
                            .border(1.dp, Color(0xFFC7D2FE), RoundedCornerShape(12.dp))
                            .clickable { showAdPrompt = true }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PulsingLiveDot(color = Color(0xFF10B981))
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = null,
                            tint = Color(0xFF4F46E5),
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${campaign.participantsCount} Members • View ➔",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4F46E5)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Badges Row 2: Completed count & Links (Leaderboard + Fazilat)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Mint Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isDarkTheme) Color(0x2610B981) else Color(0xFFECFDF5))
                            .border(1.dp, Color(0xFFA7F3D0), RoundedCornerShape(12.dp))
                            .padding(horizontal = 9.dp, vertical = 5.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "✨ $liveTotal of ${campaign.target} completed",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF065F46)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF07523D))
                                    .padding(horizontal = 5.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "$pct%",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    // Leaderboard & Fazilat Links
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "🏆 Leaderboard",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = ShimmerGold,
                            modifier = Modifier.clickable { showAdPrompt = true }
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Fazilat 📖",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isDarkTheme) ShimmerGold else Color(0xFF053B2C),
                            modifier = Modifier.clickable { showFazilatModal = true }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // ── Grand Interactive Dial Area (Matching Image 4) ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .onGloballyPositioned { dialCoords = it },
            contentAlignment = Alignment.Center
        ) {
            // Top Controls Row: Beads icon & Auto Recite button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(10f)
                    .align(Alignment.TopCenter)
                    .onGloballyPositioned { autoControlCoords = it },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Floating Switch Button (⭕ when in beads mode, 📿 when in dial mode)
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(if (isBeadsViewMode) Color(0xFF0E5D47) else (if (isDarkTheme) Color(0xFF16251E) else Color(0xFFF1F8F2)))
                        .border(1.dp, BrightGold, CircleShape)
                        .clickable { isBeadsViewMode = !isBeadsViewMode },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isBeadsViewMode) "⭕" else "📿",
                        fontSize = 16.sp
                    )
                }

                // 2. Auto Reciter Pill Button
                Surface(
                    onClick = { showAutoModal = true },
                    shape = RoundedCornerShape(20.dp),
                    color = if (isAutoRunning) Color(0xFF0E7A52) else (if (isDarkTheme) Color(0xFF1B2F25) else Color(0xFFE8F5E9)),
                    border = BorderStroke(1.dp, if (isAutoRunning) Color(0xFF34D399) else Color(0x66C5A059))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Auto",
                            tint = if (isAutoRunning) Color.White else (if (isDarkTheme) ShimmerGold else Color(0xFF053B2C)),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = if (isAutoRunning) "Auto: ON" else "Auto",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isAutoRunning) Color.White else (if (isDarkTheme) ShimmerGold else Color(0xFF053B2C))
                        )
                    }
                }
            } // Top Controls Row ends

            // Central Counter Area: Circular Dial or Realistic Beads
            if (!isBeadsViewMode) {
                CircularCounterDial(
                    sessionCount = sessionCount,
                    targetCount = campaign.target.toInt(),
                    isNightMode = isDarkTheme,
                    onTap = { doCountStep(1) }
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RealisticBeadsCanvas(
                        skin = activeSkin,
                        sessionCount = sessionCount,
                        targetCount = campaign.target.toInt(),
                        isNightMode = isDarkTheme,
                        onToggleBeadsMode = { isBeadsViewMode = false },
                        onOpenMarbleGallery = { showMarbleGalleryModal = true },
                        onBeadAdvancement = { doCountStep(1) }
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Central Session Count Number in golden serif
                    Text(
                        text = "$sessionCount",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color(0xFFF3D48A) else Color(0xFFA07828),
                        fontFamily = FontFamily.Serif
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Colours & Marbles Pill Button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(if (isDarkTheme) Color(0xFF112D22) else Color(0xFFF8F5EE))
                            .border(1.5.dp, Color(0xFFC9A84C).copy(alpha = 0.5f), RoundedCornerShape(999.dp))
                            .clickable { showMarbleGalleryModal = true }
                            .padding(horizontal = 18.dp, vertical = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            val baseCol = remember(activeSkin.baseColor) {
                                try { Color(android.graphics.Color.parseColor(activeSkin.baseColor)) } catch (_: Exception) { Color(0xFF825F2D) }
                            }
                            val highlightCol = remember(activeSkin.highlightColor) {
                                try { Color(android.graphics.Color.parseColor(activeSkin.highlightColor)) } catch (_: Exception) { Color(0xFFF3D48A) }
                            }
                            val shadowCol = remember(activeSkin.shadowColor) {
                                try { Color(android.graphics.Color.parseColor(activeSkin.shadowColor)) } catch (_: Exception) { Color(0xFF4A3519) }
                            }
                            Canvas(modifier = Modifier.size(20.dp)) {
                                val r = size.minDimension / 2f
                                drawCircle(
                                    brush = Brush.radialGradient(
                                        colors = listOf(highlightCol, baseCol, shadowCol),
                                        center = Offset(r - r * 0.35f, r - r * 0.35f),
                                        radius = r * 1.25f
                                    ),
                                    radius = r,
                                    center = Offset(r, r)
                                )
                            }
                            Text(
                                text = "Colours & Marbles",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isDarkTheme) Color(0xFFF3D48A) else Color(0xFF1E2824)
                            )
                        }
                    }
                }
            }
        } // Dial Box ends

        Spacer(modifier = Modifier.height(18.dp))

        // ── Group Reciters & Leaderboard Entry Bar ──
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clickable {
                    if (activity != null) {
                        showAdPrompt = true
                    } else {
                        showLeaderboardModal = true
                    }
                }
                .onGloballyPositioned { leaderboardBarCoords = it },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDarkTheme) Color(0xFF132B20) else Color(0xFFF0FDF4)
            ),
            border = BorderStroke(1.dp, if (isDarkTheme) Color(0x33C5A059) else Color(0x330E7A52))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("👥", fontSize = 18.sp)
                    Text(
                        text = "Group Reciters & Leaderboard",
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White else Color(0xFF063321)
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = if (isDarkTheme) ShimmerGold else Color(0xFF0E7A52)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ── Submit Your Zikr Button ──
        Button(
            onClick = { submitBatch() },
            enabled = !isSubmitting && pendingCount > 0,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(52.dp)
                .onGloballyPositioned { submitBtnCoords = it },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0E7A52),
                disabledContainerColor = if (isDarkTheme) Color(0xFF1B2F25) else Color(0xFFB0BEC5)
            )
        ) {
            if (isSubmitting) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Submit Your Zikr" + if (pendingCount > 0) " (+$pendingCount)" else "",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Quick Share Platform Row
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDarkTheme) Color(0xFF132B20) else Color(0xFFF8FAF7)
            ),
            border = BorderStroke(1.dp, if (isDarkTheme) Color(0x33C5A059) else Color(0x1A0E7A52))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Invite Friends & Family 📢",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White else Color(0xFF063321)
                    )
                    Text(
                        text = "More ›",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0E7A52),
                        modifier = Modifier.clickable { showShareModal = true }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    ShareTile(label = "WhatsApp", icon = "💬", color = Color(0xFF25D366)) {
                        GroupInviteHelper.sharePlatform(context, "whatsapp", campaign, campaign.isCreator, selectedLanguage)
                    }
                    ShareTile(label = "SMS", icon = "✉️", color = Color(0xFF3B82F6)) {
                        GroupInviteHelper.sharePlatform(context, "sms", campaign, campaign.isCreator, selectedLanguage)
                    }
                    ShareTile(label = "Twitter", icon = "🐦", color = Color(0xFF1DA1F2)) {
                        GroupInviteHelper.sharePlatform(context, "twitter", campaign, campaign.isCreator, selectedLanguage)
                    }
                    ShareTile(label = "Copy Link", icon = "🔗", color = Color(0xFF0E7A52)) {
                        GroupInviteHelper.copyToClipboard(context, GroupInviteHelper.getDeepLinkUrl(campaign.joinCode.ifBlank { campaign.campaignId }), "Invite Link copied!")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // AdMob Native Ad Card Slot
        AdMobNativeAdCard(
            adUnitId = AdConfig.NATIVE_VIDEO_AD_UNIT_ID,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))
    } // Column (scrollable room content) ends

    // ── MODALS & SHEETS FOR ACTIVE DHIKR ROOM ──
    if (showAutoModal) {
        SmartAutoCounterModal(
            isRunning = isAutoRunning,
            currentSpeedMs = autoSpeedMs,
            onToggle = {
                isAutoRunning = !isAutoRunning
                showAutoModal = false
                AppToast.show(if (isAutoRunning) "Auto Reciter Started ⚡" else "Auto Reciter Paused ⏸️")
            },
            onSelectSpeed = { speed ->
                autoSpeedMs = speed
                AppToast.show("Auto speed updated: ${(speed / 1000f)}s")
            },
            onDismiss = { showAutoModal = false },
            isDarkTheme = isDarkTheme
        )
    }

    if (showMarbleGalleryModal) {
        MarbleGalleryModal(
            activeSkinId = activeSkin.id,
            onSelectSkin = { chosenSkin ->
                activeSkin = chosenSkin
                com.tasbih.digitaltasbih.data.TasbihRepository.setActiveBeadSkin(chosenSkin.id)
                isBeadsViewMode = true
                showMarbleGalleryModal = false
            },
            onDismiss = { showMarbleGalleryModal = false }
        )
    }

    if (showShareModal) {
        GroupShareModal(
            campaign = campaign,
            isCreator = campaign.isCreator,
            isDarkTheme = isDarkTheme,
            selectedLanguage = selectedLanguage,
            onDismiss = {
                showShareModal = false
                onDismissShareModal()
            }
        )
    }

    if (showAdPrompt) {
        LeaderboardAdPromptDialog(
            campaignTitle = campaign.title,
            onWatchAd = {
                showAdPrompt = false
                if (activity != null) {
                    AdMobRewardedManager.showAd(activity, "Unlock Leaderboard: ${campaign.title}") {
                        showLeaderboardModal = true
                    }
                } else {
                    showLeaderboardModal = true
                }
            },
            onDismiss = { showAdPrompt = false },
            isDarkTheme = isDarkTheme
        )
    }

    if (showLeaderboardModal) {
        LeaderboardModalSheet(
            campaign = campaign,
            campaignId = campaign.campaignId,
            repository = repository,
            onDismiss = { showLeaderboardModal = false },
            isDarkTheme = isDarkTheme
        )
    }

    if (showFazilatModal) {
        FazilatModalSheet(
            title = campaign.title,
            onDismiss = { showFazilatModal = false },
            isDarkTheme = isDarkTheme
        )
    }

    if (showDeleteConfirmModal) {
        DeleteGroupConfirmDialog(
            campaignTitle = campaign.title,
            onWatchAdAndDelete = {
                showDeleteConfirmModal = false
                if (activity != null) {
                    AdMobRewardedManager.showAd(activity, "Delete Group: ${campaign.title}") {
                        coroutineScope.launch {
                            repository.deleteGroup(campaign.campaignId)
                            AppToast.show("Group '${campaign.title}' deleted successfully 🗑️")
                            onBack()
                        }
                    }
                } else {
                    coroutineScope.launch {
                        repository.deleteGroup(campaign.campaignId)
                        AppToast.show("Group '${campaign.title}' deleted successfully 🗑️")
                        onBack()
                    }
                }
            },
            onDismiss = { showDeleteConfirmModal = false },
            isDarkTheme = isDarkTheme
        )
    }

    // Spotlight Tour for Room
    if (isRoomTourOpen) {
        val (stepTitle, stepDesc) = when (roomTourStep) {
            1 -> tourStrings.roomProgressTitle to tourStrings.roomProgressDesc
            2 -> tourStrings.roomCountTitle to tourStrings.roomCountDesc
            3 -> tourStrings.roomAutoTitle to tourStrings.roomAutoDesc
            4 -> tourStrings.roomLeaderboardTitle to tourStrings.roomLeaderboardDesc
            5 -> tourStrings.roomSubmitTitle to tourStrings.roomSubmitDesc
            else -> "" to ""
        }
        LiveDhikrSpotlightTourModal(
            step = roomTourStep,
            totalSteps = 5,
            targetRect = currentRoomTourRect,
            stepLabel = tourStrings.step.replace("{curr}", "$roomTourStep").replace("{total}", "5"),
            title = stepTitle,
            description = stepDesc,
            skipBtnText = tourStrings.skip,
            nextBtnText = tourStrings.next,
            doneBtnText = tourStrings.done,
            onSkip = {
                isRoomTourOpen = false
                repository.setRoomTourDone(true)
            },
            onNext = {
                if (roomTourStep < 5) roomTourStep++ else {
                    isRoomTourOpen = false
                    repository.setRoomTourDone(true)
                }
            },
            onDone = {
                isRoomTourOpen = false
                repository.setRoomTourDone(true)
            }
        )
    }

    if (showNamePromptDialog) {
        var tempName by remember { mutableStateOf("") }
        var nameError by remember { mutableStateOf<String?>(null) }
        Dialog(
            onDismissRequest = { showNamePromptDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDarkTheme) Color(0xFF13221A) else Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(if (isDarkTheme) Color(0xFF1B3D2F) else Color(0xFFE8F5E9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("👤", fontSize = 26.sp)
                    }
                    Text(
                        text = "Enter Your Display Name",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White else Color(0xFF064E3B)
                    )
                    Text(
                        text = "Please enter your name so your group zikr can be counted on the community leaderboard.",
                        fontSize = 12.5.sp,
                        color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B),
                        textAlign = TextAlign.Center
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isDarkTheme) Color(0xFF16251E) else Color(0xFFF8FAFC))
                            .border(1.2.dp, Color(0xFF047857), RoundedCornerShape(12.dp))
                            .padding(horizontal = 14.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (tempName.isEmpty()) {
                            Text("e.g. Ahmad or Mohd Ali", color = Color(0xFF94A3B8), fontSize = 13.5.sp)
                        }
                        BasicTextField(
                            value = tempName,
                            onValueChange = { tempName = it; nameError = null },
                            singleLine = true,
                            textStyle = TextStyle(
                                color = if (isDarkTheme) Color.White else Color(0xFF1E293B),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    if (nameError != null) {
                        Text(
                            text = nameError!!,
                            color = Color(0xFFDC2626),
                            fontSize = 11.5.sp,
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showNamePromptDialog = false },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f).height(46.dp)
                        ) {
                            Text("Cancel", color = if (isDarkTheme) Color.White else Color(0xFF475569))
                        }
                        Button(
                            onClick = {
                                val clean = tempName.trim()
                                if (clean.isBlank()) {
                                    nameError = "Display name cannot be empty"
                                    return@Button
                                }
                                repository.saveFullProfile(
                                    firstName = clean,
                                    lastName = "",
                                    country = repository.getCountry().ifBlank { "India" },
                                    state = repository.getState(),
                                    city = repository.getCity(),
                                    is18Plus = true
                                )
                                showNamePromptDialog = false
                                AppToast.show("Name saved! Submitting zikr...")
                                submitBatch()
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF064E3B)),
                            modifier = Modifier.weight(1f).height(46.dp)
                        ) {
                            Text("Save & Submit", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
}

// ── 18+ Minor Restriction Banner for Community Features ──
@Composable
fun CommunityMinorRestrictedView(
    isDarkTheme: Boolean,
    onUpdateAge: () -> Unit,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDarkTheme) Color(0xFF14241C) else Color.White
            ),
            border = BorderStroke(1.2.dp, if (isDarkTheme) Color(0xFF2D5A42) else Color(0xFFBBF7D0)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(if (isDarkTheme) Color(0xFF1E382A) else Color(0xFFECFDF5)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🛡️", fontSize = 34.sp)
                }

                Text(
                    text = "Community Features (18+ Only)",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = if (isDarkTheme) Color.White else Color(0xFF064E3B)
                )

                Text(
                    text = "Live Dhikr Groups, Global Campaigns, and Umrah Lucky Draw are restricted to users aged 18 and older in compliance with child safety policies.\n\nAll your offline Islamic utilities (Tasbih Counter, Holy Quran, 206 Duas, Prayer Times, Qibla, Salah Tracker) remain 100% free and open for you without any restrictions.",
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    textAlign = TextAlign.Center,
                    color = if (isDarkTheme) Color(0xFFCBD5E1) else Color(0xFF475569)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Primary Button: Go back to local Islamic features
                Button(
                    onClick = onBack,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF047857)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("Back to Islamic Features", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
                }

                // Secondary Action: Update Age Declaration
                TextButton(
                    onClick = onUpdateAge
                ) {
                    Text(
                        text = "I am 18 or older now (Update Age)",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isDarkTheme) Color(0xFFA7F3D0) else Color(0xFF065F46)
                    )
                }
            }
        }
    }
}
