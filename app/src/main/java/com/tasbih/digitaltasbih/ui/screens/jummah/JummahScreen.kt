package com.tasbih.digitaltasbih.ui.screens.jummah

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.data.LanguageItem
import com.tasbih.digitaltasbih.data.PrayerRepository
import com.tasbih.digitaltasbih.data.jummah.JummahData
import com.tasbih.digitaltasbih.data.jummah.JummahRepository
import com.tasbih.digitaltasbih.data.jummah.JummahSunnahItem
import com.tasbih.digitaltasbih.ui.components.LanguageBar
import com.tasbih.digitaltasbih.ui.components.MoreLanguagesModal
import com.tasbih.digitaltasbih.util.TasbihAudioEngine
import kotlinx.coroutines.delay

@Composable
fun JummahScreen(
    onBack: () -> Unit,
    onOpenSurahKahf: () -> Unit = {}
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    // Initialize audio engine
    LaunchedEffect(Unit) {
        TasbihAudioEngine.init(context)
    }

    var selectedLanguage by remember { mutableStateOf(PrayerRepository.getLanguage()) }
    var isDarkTheme by remember { mutableStateOf(PrayerRepository.isDarkTheme()) }
    var isMoreLangOpen by remember { mutableStateOf(false) }

    // Observe repository updates
    val trigger = JummahRepository.updateTrigger.value
    val progress = remember(trigger) {
        JummahRepository.getProgress(context)
    }

    val totalItems = JummahData.items.size
    val completedCount = remember(progress) {
        JummahData.items.count { progress[it.id] == true }
    }
    val progressPct = remember(completedCount, totalItems) {
        if (totalItems > 0) (completedCount * 100) / totalItems else 0
    }

    val animatedPct by animateFloatAsState(
        targetValue = progressPct.toFloat(),
        animationSpec = tween(durationMillis = 400),
        label = "progressPct"
    )

    // Active hadith popup modal
    var activeHadithModalId by remember { mutableStateOf<String?>(null) }

    val uiPack = remember(selectedLanguage) {
        JummahData.getUiPack(selectedLanguage)
    }

    val visibleLangs = listOf(
        LanguageItem("en", "English"),
        LanguageItem("hi", "हिंदी"),
        LanguageItem("ur", "اردو"),
        LanguageItem("ar", "العربية"),
        LanguageItem("bn", "বাংলা")
    )

    val bgColor = if (isDarkTheme) Color(0xFF07150E) else Color(0xFFF6F4EE)

    Scaffold(
        containerColor = bgColor,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(bgColor)
            ) {
                // Floating Ornate Header Card (matching HomeScreen style)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .shadow(elevation = 6.dp, shape = RoundedCornerShape(18.dp))
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF146450), Color(0xFF0C4A3A), Color(0xFF062018))
                            )
                        )
                        .border(1.5.dp, Color(0x8CE8C878), RoundedCornerShape(18.dp))
                        .padding(horizontal = 12.dp, vertical = 9.dp)
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

                        // Title: JUMMAH MUBARAK
                        Text(
                            text = uiPack.title.uppercase(),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Serif,
                            letterSpacing = 1.2.sp,
                            color = Color.White
                        )

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

                // Standard LanguageBar on page background (Separate from header, exactly like HomeScreen!)
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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            // ── HERO CARD ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(24.dp))
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF053325), Color(0xFF0A4F3B), Color(0xFF0E654C))
                        )
                    )
                    .border(
                        1.8.dp,
                        Color(0xFFF6D365).copy(alpha = 0.5f),
                        RoundedCornerShape(24.dp)
                    )
                    .padding(20.dp)
            ) {
                Column {
                    // Badge: BLESSED FRIDAY
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(Color.Black.copy(alpha = 0.35f))
                            .border(
                                1.dp,
                                Color(0xFFF6D365).copy(alpha = 0.6f),
                                RoundedCornerShape(999.dp)
                            )
                            .padding(horizontal = 14.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "✨ ${uiPack.badge}",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFFDE047),
                            letterSpacing = 0.8.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Title: JUMMAH MUBARAK
                    Text(
                        text = uiPack.title,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Serif,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Hadith Hero Quote Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color.Black.copy(alpha = 0.28f),
                                RoundedCornerShape(14.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = Color.White.copy(alpha = 0.08f),
                                shape = RoundedCornerShape(14.dp)
                            )
                            .padding(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(38.dp)
                                    .background(Color(0xFFFDE047), RoundedCornerShape(2.dp))
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = uiPack.hadithHero,
                                fontSize = 13.sp,
                                lineHeight = 19.sp,
                                fontStyle = FontStyle.Italic,
                                color = Color(0xFFF0FDF4)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Progress Section Wrap
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color.Black.copy(alpha = 0.3f),
                                RoundedCornerShape(16.dp)
                            )
                            .border(
                                1.2.dp,
                                Color(0xFFF6D365).copy(alpha = 0.35f),
                                RoundedCornerShape(16.dp)
                            )
                            .padding(14.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = uiPack.progressLabel,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "$completedCount / $totalItems ($progressPct%)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFFFDE047)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Smooth animated Progress Bar
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(999.dp))
                                    .background(Color.White.copy(alpha = 0.2f))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(animatedPct / 100f)
                                        .height(10.dp)
                                        .clip(RoundedCornerShape(999.dp))
                                        .background(
                                            Brush.horizontalGradient(
                                                listOf(Color(0xFFFDE047), Color(0xFF10B981))
                                            )
                                        )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── SECTION TITLE: SUNNAHS COMPLETED ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "✨", fontSize = 16.sp)
                Text(
                    text = uiPack.progressLabel.uppercase(),
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp,
                    color = if (isDarkTheme) Color(0xFFF6D365) else Color(0xFF064E3B)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── 12 SUNNAH CHECKLIST CARDS ──
            JummahData.items.forEach { item ->
                val isDone = progress[item.id] == true
                JummahSunnahCard(
                    item = item,
                    isDone = isDone,
                    lang = selectedLanguage,
                    uiPack = uiPack,
                    isDarkTheme = isDarkTheme,
                    onToggle = {
                        JummahRepository.toggleItem(context, item.id)
                        TasbihAudioEngine.playSandalwoodClick()
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    },
                    onOpenHadithModal = {
                        activeHadithModalId = item.id
                    },
                    onOpenSurahKahf = onOpenSurahKahf
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // ── COMPLETION CELEBRATION BOX ──
            AnimatedVisibility(
                visible = completedCount == totalItems,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFF10B981).copy(alpha = 0.15f),
                                    Color(0xFFF6D365).copy(alpha = 0.2f)
                                )
                            )
                        )
                        .border(
                            2.dp,
                            Color(0xFFF6D365),
                            RoundedCornerShape(18.dp)
                        )
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = "🌟", fontSize = 36.sp)
                        Text(
                            text = uiPack.mubarakTitle,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isDarkTheme) Color(0xFF6EE7B7) else Color(0xFF0E5D47),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = uiPack.mubarakDesc,
                            fontSize = 12.5.sp,
                            color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF475569),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = {
                                JummahRepository.resetProgress(context)
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = if (isDarkTheme) Color(0xFFFDE047) else Color(0xFF0E5D47)
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                Color(0xFFF6D365)
                            )
                        ) {
                            Text(
                                text = uiPack.resetBtn,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    // Modal dialogs
    if (activeHadithModalId != null) {
        val hadithDetail = JummahData.hadithDetails[activeHadithModalId]
        if (hadithDetail != null) {
            JummahHadithModal(
                hadith = hadithDetail,
                lang = selectedLanguage,
                isDarkTheme = isDarkTheme,
                onDismiss = { activeHadithModalId = null }
            )
        }
    }

    if (isMoreLangOpen) {
        MoreLanguagesModal(
            selectedCode = selectedLanguage,
            onSelectLanguage = { code ->
                selectedLanguage = code
                isMoreLangOpen = false
            },
            onDismiss = { isMoreLangOpen = false }
        )
    }
}

@Composable
fun JummahSunnahCard(
    item: JummahSunnahItem,
    isDone: Boolean,
    lang: String,
    uiPack: com.tasbih.digitaltasbih.data.jummah.JummahUiPack,
    isDarkTheme: Boolean,
    onToggle: () -> Unit,
    onOpenHadithModal: () -> Unit,
    onOpenSurahKahf: () -> Unit
) {
    val title = JummahData.getItemTitle(item, lang)
    val desc = JummahData.getItemDesc(item, lang)
    val hadithRef = JummahData.getItemHadithRef(item, lang)

    val cardBg = if (isDone) {
        if (isDarkTheme) Color(0xFF0F261C) else Color(0xFFF2FAF6)
    } else {
        if (isDarkTheme) Color(0xFF121E18) else Color.White
    }

    val borderColor = if (isDone) {
        Color(0xFF10B981)
    } else {
        if (isDarkTheme) Color(0xFF1E382B) else Color(0xFF0E5D47).copy(alpha = 0.16f)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (isDone) 5.dp else 2.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(cardBg)
            .border(
                width = if (isDone) 1.5.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onToggle() }
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            // Icon Square Box
            val iconBgModifier = if (isDone) {
                Modifier.background(Brush.linearGradient(listOf(Color(0xFF0E5D47), Color(0xFF064E3B))))
            } else {
                Modifier.background(if (isDarkTheme) Color(0xFF182B22) else Color(0xFFE6F5ED))
            }
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .then(iconBgModifier)
                    .border(
                        1.dp,
                        if (isDone) Color(0xFF10B981) else Color(0xFFC4EBD7).copy(alpha = 0.5f),
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.icon,
                    fontSize = 22.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Content Column
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Number badge & Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                if (isDarkTheme) Color(0xFF10B981).copy(alpha = 0.2f) else Color(0xFFE6F5ED),
                                RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = item.sequence,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isDarkTheme) Color(0xFF10B981) else Color(0xFF0E5D47)
                        )
                    }

                    Text(
                        text = title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isDone) {
                            if (isDarkTheme) Color(0xFF34D399) else Color(0xFF0E5D47)
                        } else {
                            if (isDarkTheme) Color(0xFFE6F7EF) else Color(0xFF064E3B)
                        }
                    )
                }

                if (desc.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = desc,
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF52616B)
                    )
                }

                Spacer(modifier = Modifier.height(7.dp))

                // Hadith Reference Pill (Clickable -> Opens Authentic Hadith Modal)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (isDarkTheme) Color(0xFF2E2209) else Color(0xFFFEF3C7)
                        )
                        .border(
                            1.dp,
                            if (isDarkTheme) Color(0xFFF59E0B).copy(alpha = 0.4f) else Color(0xFFFCD34D),
                            RoundedCornerShape(6.dp)
                        )
                        .clickable { onOpenHadithModal() }
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = "📜", fontSize = 11.sp)
                        Text(
                            text = hadithRef,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) Color(0xFFFDE68A) else Color(0xFF92400E)
                        )
                        Text(text = "🔍", fontSize = 10.sp)
                    }
                }

                // ── SPECIAL ACTION: #9 Surah Al-Kahf ──
                if (item.id == "surah_kahf") {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF0E5D47), Color(0xFF064E3B))
                                )
                            )
                            .border(1.dp, Color(0xFFF6D365), RoundedCornerShape(10.dp))
                            .clickable { onOpenSurahKahf() }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "${uiPack.readKahfBtn} →",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Checkbox on right
            val checkboxBgModifier = if (isDone) {
                Modifier.background(Brush.linearGradient(listOf(Color(0xFF0E5D47), Color(0xFF064E3B))))
            } else {
                Modifier.background(if (isDarkTheme) Color(0xFF0B1410) else Color.White)
            }
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .then(checkboxBgModifier)
                    .border(
                        width = 2.dp,
                        color = if (isDone) Color(0xFF10B981) else Color(0xFFCBD5E1),
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isDone) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Done",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
