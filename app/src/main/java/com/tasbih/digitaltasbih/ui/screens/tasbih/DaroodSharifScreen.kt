package com.tasbih.digitaltasbih.ui.screens.tasbih

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.platform.LocalContext
import com.tasbih.digitaltasbih.ui.components.AppToast
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.data.*
import com.tasbih.digitaltasbih.ui.components.LanguageBar
import com.tasbih.digitaltasbih.ui.components.MoreLanguagesModal
import com.tasbih.digitaltasbih.util.AdMobInterstitialManager
import com.tasbih.digitaltasbih.util.TasbihAudioEngine

/**
 * DaroodSharifScreen - Dedicated Darood Sharif collection and recitation counter.
 * Matches user screenshots 000148, 000154, 000205, 000211.
 * Features:
 * 1. Full collection of authentic Daroods with Hadith references.
 * 2. Dedicated Recite & Count mode with resizable font scale (A- to A+ slider / 100% zoom).
 * 3. Transliteration, expandable translation accordion, and Hadith virtues.
 * 4. Dedicated Counter Dial with #3 Sandalwood sound, target setter, Save Session, Reset, Previous/Next Darood navigation.
 */
@Composable
fun DaroodSharifScreen(
    onBack: () -> Unit = {},
    initialDaroodId: String? = null
) {
    val context = LocalContext.current
    var selectedLang by remember { mutableStateOf(PrayerRepository.getLanguage()) }
    var isDarkTheme by remember { mutableStateOf(false) }
    var isMoreLangOpen by remember { mutableStateOf(false) }

    // If activeDaroodIndex is not null, we are in Recite & Count mode
    var activeDaroodIndex by remember(initialDaroodId) {
        val initialIdx = if (!initialDaroodId.isNullOrEmpty()) {
            TasbihRepository.DAROOD_LIST.indexOfFirst {
                it.id == initialDaroodId ||
                it.id.replace("d_", "darood_") == initialDaroodId ||
                it.id == initialDaroodId.replace("darood_", "d_")
            }.takeIf { it >= 0 }
        } else null
        mutableStateOf(initialIdx)
    }

    LaunchedEffect(initialDaroodId) {
        if (!initialDaroodId.isNullOrEmpty()) {
            val idx = TasbihRepository.DAROOD_LIST.indexOfFirst {
                it.id == initialDaroodId ||
                it.id.replace("d_", "darood_") == initialDaroodId ||
                it.id == initialDaroodId.replace("darood_", "d_")
            }
            if (idx >= 0) {
                activeDaroodIndex = idx
            }
        }
    }

    // Font zoom scale for Arabic text (1.0f = 100%, range: 0.8f to 1.6f)
    var fontScale by remember { mutableFloatStateOf(1.0f) }

    // Expand translation accordion in counting mode
    var isTranslationExpanded by remember { mutableStateOf(false) }

    // Counting state for active Darood
    val activeDarood = activeDaroodIndex?.let { TasbihRepository.DAROOD_LIST.getOrNull(it) }
    var count by remember(activeDarood) {
        mutableIntStateOf(activeDarood?.let { TasbihRepository.getSessionCount(it.id) } ?: 0)
    }
    var lifetimeCount by remember(activeDarood) {
        mutableIntStateOf(activeDarood?.let { TasbihRepository.getAllTimeCount(it.id) } ?: 0)
    }
    var target by remember(activeDarood) {
        mutableIntStateOf(activeDarood?.let { TasbihRepository.getTarget(it.id, it.defaultTarget) } ?: 33)
    }
    var isTargetModalOpen by remember { mutableStateOf(false) }
    var selectedHadithGuide by remember { mutableStateOf<DhikrGuideDetail?>(null) }
    val strings = remember(selectedLang) { TasbihLocalization.get(selectedLang) }

    // Haptic feedback
    fun vibrateTap() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vm?.defaultVibrator?.vibrate(VibrationEffect.createOneShot(35, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                val v = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                @Suppress("DEPRECATION")
                v?.vibrate(35)
            }
        } catch (_: Exception) {}
    }

    // Auto-save session helper
    fun saveSession() {
        if (activeDarood != null && count > 0) {
            val dummyItem = TasbihDhikrItem(
                id = activeDarood.id,
                arabic = activeDarood.arabic,
                localizedNames = activeDarood.title,
                localizedMeanings = activeDarood.translation,
                icon = "🕌",
                defaultTarget = target
            )
            TasbihRepository.saveSessionToHistory(dummyItem, count)
        }
    }

    // Single-tap counter handler matching Personal Tasbih
    fun performCountTap() {
        if (activeDarood == null) return
        val nextCount = count + 1
        count = nextCount
        lifetimeCount += 1
        TasbihRepository.recordCount(activeDarood.id, 1)
        TasbihAudioEngine.playSandalwoodClick()
        vibrateTap()

        if (nextCount == target) {
            TasbihAudioEngine.playCompletionChime()
            AppToast.show("MashaAllah! Target Completed 🎉")
        }
    }

    // Ensure session is saved if user leaves or switches Darood
    DisposableEffect(activeDarood) {
        onDispose {
            saveSession()
        }
    }

    // Languages
    val visibleLanguages = listOf(
        LanguageItem("en", "English"),
        LanguageItem("hi", "हिंदी"),
        LanguageItem("ur", "اردو"),
        LanguageItem("ar", "العربية"),
        LanguageItem("bn", "বাংলা")
    )

    // Colors
    val bgMain = if (isDarkTheme) Color(0xFF071510) else Color(0xFFFAF7F2)
    val cardBg = if (isDarkTheme) Color(0xFF0A1C15) else Color(0xFFFFFFFF)
    val cardBorder = if (isDarkTheme) Color(0xFFC9A84C).copy(alpha = 0.25f) else Color(0xFFE2E8F0)
    val inkColor = if (isDarkTheme) Color(0xFFF0EAD6) else Color(0xFF14241E)
    val mutedColor = if (isDarkTheme) Color(0xFFA89F8B) else Color(0xFF64748B)

    // Back Exit Ad
    val activity = context as? Activity
    val handleBackWithAd = {
        if (activeDaroodIndex != null) {
            saveSession()
            // Return to Darood list first
            activeDaroodIndex = null
        } else {
            if (activity != null) {
                AdMobInterstitialManager.showAd(activity, "darood_back") {
                    onBack()
                }
            } else {
                onBack()
            }
        }
    }

    BackHandler {
        if (selectedHadithGuide != null) {
            selectedHadithGuide = null
        } else if (isMoreLangOpen) {
            isMoreLangOpen = false
        } else if (isTargetModalOpen) {
            isTargetModalOpen = false
        } else {
            handleBackWithAd()
        }
    }

    Scaffold(
        topBar = {
            val topTitle = if (activeDarood != null) {
                activeDarood.getTitle(selectedLang).uppercase()
            } else {
                "DAROOD SHARIF"
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(10.dp, RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF146450), Color(0xFF0C4A3A), Color(0xFF062018))
                        )
                    )
                    .border(
                        1.5.dp,
                        Brush.verticalGradient(listOf(Color.Transparent, Color(0xFFE8C878).copy(alpha = 0.55f))),
                        RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Back button [←]
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.22f))
                            .border(1.dp, Color.White.copy(alpha = 0.35f), CircleShape)
                            .clickable { handleBackWithAd() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "←", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }

                    // Title
                    Text(
                        text = topTitle,
                        fontFamily = FontFamily.Serif,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        letterSpacing = 0.8.sp,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                    )

                    // Right button: [All Daroods] if in counting mode, or [☀️] Theme button
                    if (activeDaroodIndex != null) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White)
                                .border(1.dp, Color(0x33A07828), RoundedCornerShape(8.dp))
                                .clickable { activeDaroodIndex = null }
                                .padding(horizontal = 8.dp, vertical = 5.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(text = "📋", fontSize = 11.sp)
                                Text(
                                    text = "All Daroods",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0E5D47)
                                )
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFFEF3C7))
                                .border(1.dp, Color(0xFFF59E0B).copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                                .clickable {
                                    isDarkTheme = !isDarkTheme
                                    AppToast.show(if (isDarkTheme) "Night Theme 🌙" else "Day Theme ☀️")
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = if (isDarkTheme) "🌙" else "☀️", fontSize = 15.sp)
                        }
                    }
                }
            }
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
            // Language Bar
            LanguageBar(
                languages = visibleLanguages,
                selectedCode = selectedLang,
                onSelectLanguage = { code ->
                    selectedLang = code
                    PrayerRepository.saveLanguage(code)
                },
                onOpenMoreLanguages = { isMoreLangOpen = true }
            )

            if (activeDarood != null) {
                // ══════════════════════════════════════════════════════════════
                //  RECITE & COUNT MODE (Screenshots 000205 & 000211)
                // ══════════════════════════════════════════════════════════════
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Sleek, Minimalist Font Zoom Control Pill
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(elevation = 1.dp, shape = RoundedCornerShape(50.dp), spotColor = Color(0x140E5D47))
                            .clip(RoundedCornerShape(50.dp))
                            .background(if (isDarkTheme) Color(0xFF13271F) else Color.White)
                            .border(
                                width = 1.dp,
                                color = if (isDarkTheme) Color(0xFF2E4D3E) else Color(0xFF0E5D47).copy(alpha = 0.2f),
                                shape = RoundedCornerShape(50.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 3.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // A- (Decrease button)
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(if (isDarkTheme) Color(0xFF1B382D) else Color(0xFFF1F7F4))
                                    .clickable { fontScale = (fontScale - 0.1f).coerceAtLeast(0.8f) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "A-",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDarkTheme) Color(0xFF86EFAC) else Color(0xFF0E5D47)
                                )
                            }

                            // Slim Custom Slider
                            @OptIn(ExperimentalMaterial3Api::class)
                            Slider(
                                value = fontScale,
                                onValueChange = { fontScale = it },
                                valueRange = 0.8f..1.5f,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(28.dp),
                                thumb = {
                                    Box(
                                        modifier = Modifier
                                            .size(15.dp)
                                            .shadow(2.dp, CircleShape)
                                            .background(Color(0xFF0E5D47), CircleShape)
                                            .border(1.5.dp, Color.White, CircleShape)
                                    )
                                },
                                track = { sliderState ->
                                    SliderDefaults.Track(
                                        sliderState = sliderState,
                                        modifier = Modifier.height(3.5.dp),
                                        colors = SliderDefaults.colors(
                                            activeTrackColor = Color(0xFF0E5D47),
                                            inactiveTrackColor = if (isDarkTheme) Color(0xFF1E3A2F) else Color(0xFFE2ECE6)
                                        ),
                                        drawStopIndicator = null
                                    )
                                }
                            )

                            // A+ (Increase button)
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(if (isDarkTheme) Color(0xFF1B382D) else Color(0xFFF1F7F4))
                                    .clickable { fontScale = (fontScale + 0.1f).coerceAtMost(1.5f) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "A+",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDarkTheme) Color(0xFF86EFAC) else Color(0xFF0E5D47)
                                )
                            }

                            // Percentage Badge (e.g. 100%) - tap resets to 100%
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isDarkTheme) Color(0xFF1B382D) else Color(0xFFEAF5EE))
                                    .border(
                                        0.8.dp,
                                        if (isDarkTheme) Color(0xFF2E4D3E) else Color(0xFF0E5D47).copy(alpha = 0.25f),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable { fontScale = 1.0f }
                                    .padding(horizontal = 7.dp, vertical = 2.5.dp)
                            ) {
                                Text(
                                    text = "${(fontScale * 100).toInt()}%",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isDarkTheme) Color(0xFF86EFAC) else Color(0xFF0E5D47)
                                )
                            }
                        }
                    }

                    // Main Reading Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(cardBg)
                            .border(1.2.dp, cardBorder, RoundedCornerShape(20.dp))
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Hadith Ref Header Pill (Interactive)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isDarkTheme) Color(0xFF13271F) else Color(0xFFEAF5EE))
                                    .border(1.dp, Color(0xFF0E5D47).copy(alpha = 0.35f), RoundedCornerShape(10.dp))
                                    .clickable {
                                        selectedHadithGuide = activeDarood.toGuideDetail(selectedLang)
                                    }
                                    .padding(horizontal = 12.dp, vertical = 7.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        modifier = Modifier.weight(1f, fill = false)
                                    ) {
                                        Text(text = "📖 📜", fontSize = 12.sp)
                                        Text(
                                            text = activeDarood.hadithRef,
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isDarkTheme) Color(0xFF86EFAC) else Color(0xFF0E5D47),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    // Read Hadith Pill Button
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(if (isDarkTheme) Color(0xFF0E5D47) else Color(0xFFDCFCE7))
                                            .border(1.dp, if (isDarkTheme) Color(0xFF22C55E) else Color(0xFF86EFAC), RoundedCornerShape(6.dp))
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Text(
                                            text = strings.readHadith,
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = if (isDarkTheme) Color(0xFFDCFCE7) else Color(0xFF0E5D47)
                                        )
                                    }
                                }
                            }

                            // Arabic Calligraphy Frame
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color(0xFFFAF7F2))
                                    .border(1.dp, Color(0xFF0E5D47).copy(alpha = 0.2f), RoundedCornerShape(14.dp))
                                    .padding(14.dp)
                            ) {
                                Text(
                                    text = activeDarood.arabic,
                                    fontSize = (22 * fontScale).sp,
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0E5D47),
                                    textAlign = TextAlign.Center,
                                    lineHeight = (36 * fontScale).sp,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            // Transliteration
                            val translit = activeDarood.getTranslit(selectedLang)
                            if (translit.isNotBlank()) {
                                Text(
                                    text = "« $translit »",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFFB45309),
                                    fontFamily = FontFamily.Serif,
                                    lineHeight = 19.sp
                                )
                            }

                            // Expandable Meaning / Translation Accordion
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFFEF3C7))
                                    .border(1.dp, Color(0xFFF59E0B).copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                    .clickable { isTranslationExpanded = !isTranslationExpanded }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(text = "📜", fontSize = 12.sp)
                                    Text(
                                        text = "Show Meaning / Translation " + if (isTranslationExpanded) "▴" else "▾",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF92400E)
                                    )
                                }
                            }

                            AnimatedVisibility(
                                visible = isTranslationExpanded,
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically()
                            ) {
                                Text(
                                    text = activeDarood.getTranslation(selectedLang),
                                    fontSize = 12.5.sp,
                                    color = inkColor,
                                    lineHeight = 18.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }

                            // Virtue & Reference Card
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFF0FDF4))
                                    .border(1.dp, Color(0xFF10B981).copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                                    .padding(10.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text(text = "💡", fontSize = 13.sp)
                                    Column {
                                        Text(
                                            text = "Virtue & Reference:",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF065F46)
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = activeDarood.getBenefit(selectedLang),
                                            fontSize = 11.5.sp,
                                            color = Color(0xFF047857),
                                            lineHeight = 16.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // ══════════════════════════════════════════════════════════════
                    //  DEDICATED COUNTER DIAL FOR DAROOD WITH LIFETIME BADGE
                    // ══════════════════════════════════════════════════════════════
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 10.dp)
                    ) {
                        // Top-Right Lifetime Recitations Badge (Never resets!)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 2.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Box(
                                modifier = Modifier
                                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(12.dp), spotColor = Color(0x1A0E5D47))
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isDarkTheme) Color(0xFF13271F) else Color.White)
                                    .border(
                                        width = 1.2.dp,
                                        color = if (isDarkTheme) Color(0xFF2E4D3E) else Color(0xFFC9A84C).copy(alpha = 0.55f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 14.dp, vertical = 5.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "LIFETIME",
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 1.sp,
                                        color = if (isDarkTheme) Color(0xFFE2C475) else Color(0xFFB88E2D)
                                    )
                                    Spacer(modifier = Modifier.height(1.dp))
                                    Text(
                                        text = "$lifetimeCount",
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontFamily = FontFamily.Serif,
                                        color = if (isDarkTheme) Color(0xFF86EFAC) else Color(0xFF0E5D47)
                                    )
                                }
                            }
                        }

                        // Circular Counter Dial (Identical to Tasbih screen)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularCounterDial(
                                sessionCount = count,
                                targetCount = target,
                                isNightMode = isDarkTheme,
                                onTap = { performCountTap() }
                            )
                        }
                    }

                    // Target Pill Row: [ 🎯 Target: 33x ] [ Change ▾ ]
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isDarkTheme) cardBg else Color.White)
                                .border(1.2.dp, if (isDarkTheme) Color(0xFF2E4D3E) else Color(0xFF0E5D47), RoundedCornerShape(20.dp))
                                .clickable { isTargetModalOpen = true }
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(text = "🎯", fontSize = 12.sp)
                                Text(
                                    text = "Target: ${target}×",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDarkTheme) Color(0xFF86EFAC) else Color(0xFF0E5D47)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isDarkTheme) cardBg else Color.White)
                                .border(1.2.dp, if (isDarkTheme) Color(0xFF2E4D3E) else Color(0xFF0E5D47), RoundedCornerShape(20.dp))
                                .clickable { isTargetModalOpen = true }
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Change ▾",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color(0xFF86EFAC) else Color(0xFF0E5D47)
                            )
                        }
                    }

                    // Action Buttons Row: [ 💾 Save Session ] [ ↺ Reset ]
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                if (count > 0 && activeDarood != null) {
                                    saveSession()
                                    count = 0
                                    AppToast.show("Session Saved to History! 💾")
                                } else {
                                    AppToast.show("Count is 0")
                                }
                            },
                            modifier = Modifier
                                .weight(1.2f)
                                .height(46.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0E5D47)
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(text = "💾", fontSize = 14.sp)
                                Text(text = "Save Session", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        OutlinedButton(
                            onClick = {
                                count = 0
                                if (activeDarood != null) {
                                    TasbihRepository.resetSession(activeDarood.id)
                                }
                                AppToast.show("Reset to 0")
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = Brush.linearGradient(listOf(Color(0xFFEA580C), Color(0xFFC2410C)))
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(text = "↺", fontSize = 14.sp, color = Color(0xFFEA580C))
                                Text(text = "Reset", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEA580C))
                            }
                        }
                    }

                    // Stepper: [ ◀ Previous Darood ] [ Next Darood ▶ ]
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                saveSession()
                                val currentIdx = activeDaroodIndex ?: 0
                                val prevIdx = if (currentIdx > 0) currentIdx - 1 else TasbihRepository.DAROOD_LIST.size - 1
                                activeDaroodIndex = prevIdx
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(text = "◀ Previous Darood", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0E5D47))
                        }

                        OutlinedButton(
                            onClick = {
                                saveSession()
                                val currentIdx = activeDaroodIndex ?: 0
                                val nextIdx = (currentIdx + 1) % TasbihRepository.DAROOD_LIST.size
                                activeDaroodIndex = nextIdx
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(text = "Next Darood ▶", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0E5D47))
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            } else {
                // ══════════════════════════════════════════════════════════════
                //  DAROOD LIST MODE (Screenshot 000148 & 000154)
                // ══════════════════════════════════════════════════════════════
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    TasbihRepository.DAROOD_LIST.forEachIndexed { index, darood ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .background(cardBg)
                                .border(1.2.dp, cardBorder, RoundedCornerShape(18.dp))
                                .padding(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // Title Row + Target Pill
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(text = if (index == 0) "🕌" else "💚", fontSize = 16.sp)
                                        Text(
                                            text = darood.getTitle(selectedLang),
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = inkColor
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(if (isDarkTheme) Color(0xFF0F261E) else Color.White)
                                            .border(1.2.dp, if (isDarkTheme) Color(0xFF34D399) else Color(0xFF0E5D47), RoundedCornerShape(12.dp))
                                            .padding(horizontal = 10.dp, vertical = 3.dp)
                                    ) {
                                        Text(
                                            text = "${darood.defaultTarget}×",
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = if (isDarkTheme) Color(0xFF34D399) else Color(0xFF0E5D47)
                                        )
                                    }
                                }

                                // Interactive Hadith Ref Pill in List Mode
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isDarkTheme) Color(0xFF13271F) else Color(0xFFEAF5EE))
                                        .border(1.dp, Color(0xFF0E5D47).copy(alpha = 0.35f), RoundedCornerShape(10.dp))
                                        .clickable {
                                            selectedHadithGuide = darood.toGuideDetail(selectedLang)
                                        }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                                            modifier = Modifier.weight(1f, fill = false)
                                        ) {
                                            Text(text = "📖 📜", fontSize = 11.5.sp)
                                            Text(
                                                text = darood.hadithRef,
                                                fontSize = 11.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isDarkTheme) Color(0xFF86EFAC) else Color(0xFF0E5D47),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }

                                        // Read Hadith Pill Button
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(if (isDarkTheme) Color(0xFF0E5D47) else Color(0xFFDCFCE7))
                                                .border(1.dp, if (isDarkTheme) Color(0xFF22C55E) else Color(0xFF86EFAC), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 8.dp, vertical = 3.dp)
                                        ) {
                                            Text(
                                                text = strings.readHadith,
                                                fontSize = 10.5.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = if (isDarkTheme) Color(0xFFDCFCE7) else Color(0xFF0E5D47)
                                            )
                                        }
                                    }
                                }

                                // Arabic Calligraphy Box
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFFAF7F2))
                                        .border(1.dp, Color(0xFF0E5D47).copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = darood.arabic,
                                        fontSize = 18.sp,
                                        fontFamily = FontFamily.Serif,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0E5D47),
                                        textAlign = TextAlign.Center,
                                        lineHeight = 30.sp,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }

                                // Transliteration
                                val translit = darood.getTranslit(selectedLang)
                                if (translit.isNotBlank()) {
                                    Text(
                                        text = "« $translit »",
                                        fontSize = 12.sp,
                                        color = Color(0xFFB45309),
                                        fontFamily = FontFamily.Serif,
                                        lineHeight = 17.sp
                                    )
                                }

                                // [ 📿 Recite & Count › ] Button
                                Button(
                                    onClick = {
                                        activeDaroodIndex = index
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(44.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF0E5D47)
                                    )
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(text = "📿", fontSize = 14.sp)
                                        Text(
                                            text = "Recite & Count ›",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }

        // Target Modal
        if (isTargetModalOpen && activeDarood != null) {
            SetTargetModal(
                currentTarget = target,
                onSelectTarget = { newTgt ->
                    target = newTgt
                    activeDarood.let { TasbihRepository.setTarget(it.id, newTgt) }
                    isTargetModalOpen = false
                },
                onDismiss = { isTargetModalOpen = false }
            )
        }

        // Full 28+ Languages Modal
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

        // Authentic Hadith Reference Modal (Exact Islamic Dhikr Guide Spec)
        selectedHadithGuide?.let { guide ->
            AuthenticHadithReferenceModal(
                guide = guide,
                selectedLanguage = selectedLang,
                strings = strings,
                onDismiss = { selectedHadithGuide = null }
            )
        }
    }
}
