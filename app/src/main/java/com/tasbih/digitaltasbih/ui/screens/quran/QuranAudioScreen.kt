package com.tasbih.digitaltasbih.ui.screens.quran

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.data.QuranData
import com.tasbih.digitaltasbih.data.QuranRepository
import com.tasbih.digitaltasbih.data.QuranAudioPlayMode
import com.tasbih.digitaltasbih.ui.components.QuranAudioModeButton
import com.tasbih.digitaltasbih.data.QuranSurahAudioEngine
import com.tasbih.digitaltasbih.data.SurahMeta
import com.tasbih.digitaltasbih.ui.theme.AmiriBoldFont
import com.tasbih.digitaltasbih.ui.theme.GoldAccent

@Composable
fun QuranAudioScreen(
    onBack: () -> Unit,
    onOpenChooseReciter: () -> Unit
) {
    var settings by remember { mutableStateOf(QuranRepository.loadSettings()) }
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    androidx.compose.runtime.DisposableEffect(lifecycleOwner) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME) {
                settings = QuranRepository.loadSettings()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val activeReciterName = settings.reciterName
    val language = settings.translationLanguage

    var searchQuery by remember { mutableStateOf("") }

    val filteredSurahs = remember(searchQuery, language) {
        if (searchQuery.isBlank()) {
            QuranData.surahs
        } else {
            val q = searchQuery.trim().lowercase()
            QuranData.surahs.filter { s ->
                s.number.toString().contains(q) ||
                s.getLocalizedName(language).lowercase().contains(q) ||
                s.englishName.lowercase().contains(q) ||
                s.hindiName.lowercase().contains(q) ||
                s.arabicName.contains(q)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F5EE))
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // ── 1. TOP HEADER BAR (.qa-hdr) ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back Button (← वापस / Back)
                Row(
                    modifier = Modifier
                        .shadow(2.dp, RoundedCornerShape(20.dp))
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFFFFDF8))
                        .border(1.2.dp, GoldAccent.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                        .clickable(onClick = onBack)
                        .padding(horizontal = 14.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF0E5D47),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = if (language == "hi") "वापस" else "Back",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0E5D47)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Title (.qa-title)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (language == "hi") "क़ुरान ऑडियो" else "QURAN AUDIO",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0E5D47),
                        fontFamily = FontFamily.Serif,
                        letterSpacing = 1.sp
                    )
                    Box(
                        modifier = Modifier
                            .width(44.dp)
                            .height(2.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(GoldAccent)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(36.dp)) // balance layout
            }

            // ── 2. RECITER PICKER BAR (.quran-reciter-picker) ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 4.dp)
                    .shadow(2.dp, RoundedCornerShape(14.dp))
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .border(1.2.dp, GoldAccent.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                    .clickable(onClick = onOpenChooseReciter)
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF0E5D47), Color(0xFF083D32))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🎙️", fontSize = 16.sp)
                }

                val activeReciterItem = QuranData.reciters.find { it.identifier == settings.reciterId }
                val activeReciterDisplayName = activeReciterItem?.getLocalizedName(language) ?: activeReciterName
                val activeReciterFlag = activeReciterItem?.flag ?: ""

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = activeReciterDisplayName,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1408),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (activeReciterFlag.isNotEmpty()) {
                            Text(
                                text = activeReciterFlag,
                                fontSize = 14.sp
                            )
                        }
                    }
                    Text(
                        text = when (language) {
                            "hi" -> "क़ारी बदलें"
                            "ur" -> "قاری تبدیل کریں"
                            "ar" -> "تغيير القارئ"
                            "bn" -> "ক্বারী পরিবর্তন করুন"
                            else -> "Tap to change reciter"
                        },
                        fontSize = 10.5.sp,
                        color = Color(0xFF7A6840)
                    )
                }

                Text(
                    text = "›",
                    fontSize = 18.sp,
                    color = Color(0xFF7A6840),
                    fontWeight = FontWeight.Bold
                )
            }

            // ── AUDIO PLAYBACK MODE BAR ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = when (language) {
                        "hi" -> "ऑडियो मोड (Audio Mode)"
                        "ur" -> "آڈیو موڈ / آواز"
                        "ar" -> "نمط الصوت والترجمة"
                        else -> "Audio Mode / Tarjuma Voice"
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF0D4A3E)
                )

                QuranAudioModeButton(
                    audioPlayMode = settings.audioPlayMode,
                    translationVoiceLang = settings.translationVoiceLang,
                    language = language,
                    onModeChange = { newMode, newVoiceLang ->
                        val updated = settings.copy(audioPlayMode = newMode, translationVoiceLang = newVoiceLang)
                        settings = updated
                        QuranRepository.saveSettings(updated)
                        QuranSurahAudioEngine.setPlayMode(newMode, newVoiceLang)
                    }
                )
            }

            // ── 3. SEARCH BOX ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .border(1.dp, GoldAccent.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 9.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color(0xFF7A6840),
                        modifier = Modifier.size(18.dp)
                    )
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        textStyle = TextStyle(
                            fontSize = 13.5.sp,
                            color = Color(0xFF1A1408),
                            fontWeight = FontWeight.Medium
                        ),
                        cursorBrush = SolidColor(Color(0xFF0E5D47)),
                        decorationBox = { innerTextField ->
                            if (searchQuery.isEmpty()) {
                                Text(
                                    text = if (language == "hi") "सूरह खोजें (नाम या नंबर)..." else "Search surah by name or number...",
                                    fontSize = 13.sp,
                                    color = Color(0xFF7A6840).copy(alpha = 0.65f)
                                )
                            }
                            innerTextField()
                        }
                    )
                }
            }

            // ── 4. SURAHS LIST (.qa-list) ──
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp),
                contentPadding = PaddingValues(top = 6.dp, bottom = 150.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredSurahs, key = { it.number }) { surah ->
                    val isCurrentSurah = QuranSurahAudioEngine.currentSurahNum == surah.number
                    val isPlaying = isCurrentSurah && QuranSurahAudioEngine.isPlaying
                    val isLoading = isCurrentSurah && QuranSurahAudioEngine.isLoading

                    SurahAudioRowItem(
                        surah = surah,
                        language = language,
                        isCurrentSurah = isCurrentSurah,
                        isPlaying = isPlaying,
                        isLoading = isLoading,
                        onClick = {
                            QuranSurahAudioEngine.playSurah(surah.number)
                        }
                    )
                }
            }
        }

        // ── 5. FLOATING MINI-PLAYER BAR (.qa-player, #qaPlayerBar) ──
        val currentSurahNum = QuranSurahAudioEngine.currentSurahNum
        AnimatedVisibility(
            visible = currentSurahNum != null,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(start = 14.dp, end = 14.dp, bottom = 28.dp, top = 8.dp)
        ) {
            if (currentSurahNum != null) {
                val currentSurah = QuranData.surahs.getOrNull(currentSurahNum - 1)
                val surahName = currentSurah?.getLocalizedName(language) ?: "Surah $currentSurahNum"
                val ayahIndex = QuranSurahAudioEngine.currentAyahIndexInSurah
                val totalAyahs = currentSurah?.numberOfAyahs ?: 7

                FloatingSurahPlayerBar(
                    surahName = surahName,
                    currentAyah = ayahIndex,
                    totalAyahs = totalAyahs,
                    language = language,
                    isPlaying = QuranSurahAudioEngine.isPlaying,
                    isLoading = QuranSurahAudioEngine.isLoading,
                    isContinuous = QuranSurahAudioEngine.isContinuous,
                    audioPlayMode = settings.audioPlayMode,
                    translationVoiceLang = settings.translationVoiceLang,
                    onModeChange = { newMode, newVoiceLang ->
                        val updated = settings.copy(audioPlayMode = newMode, translationVoiceLang = newVoiceLang)
                        settings = updated
                        QuranRepository.saveSettings(updated)
                        QuranSurahAudioEngine.setPlayMode(newMode, newVoiceLang)
                    },
                    onToggleContinuous = { QuranSurahAudioEngine.toggleContinuous() },
                    onNextSurah = { QuranSurahAudioEngine.playNextSurah() },
                    onTogglePlayPause = { QuranSurahAudioEngine.togglePlayPause() },
                    onStop = { QuranSurahAudioEngine.stop() }
                )
            }
        }
    }
}

@Composable
private fun SurahAudioRowItem(
    surah: SurahMeta,
    language: String,
    isCurrentSurah: Boolean,
    isPlaying: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    val emeraldGradient = Brush.linearGradient(
        listOf(Color(0xFF0E5D47), Color(0xFF083D32))
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (isCurrentSurah) 6.dp else 2.dp,
                shape = RoundedCornerShape(18.dp)
            )
            .clip(RoundedCornerShape(18.dp))
            .background(if (isCurrentSurah) Color(0xFFFBF8F0) else Color.White)
            .border(
                width = if (isCurrentSurah) 1.5.dp else 1.dp,
                color = if (isCurrentSurah) GoldAccent else GoldAccent.copy(alpha = 0.35f),
                shape = RoundedCornerShape(18.dp)
            )
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Left Gold Line Accent (.qa-row border-left: 4px solid var(--q-gold))
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(38.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(if (isCurrentSurah) GoldAccent else GoldAccent.copy(alpha = 0.5f))
        )

        // Surah Number Badge (.qa-num: 38x38 circle)
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(emeraldGradient)
                .border(1.5.dp, GoldAccent, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${surah.number}",
                fontSize = 13.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFF3D48A)
            )
        }

        // Surah Information (.qa-info)
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = surah.getLocalizedName(language),
                fontSize = 14.5.sp,
                fontWeight = FontWeight.Bold,
                color = if (isCurrentSurah) Color(0xFF0E5D47) else Color(0xFF1A1408),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "${surah.arabicName} · ${surah.numberOfAyahs} ${if (language == "hi") "आयतें" else "Ayahs"}",
                fontSize = 12.sp,
                color = Color(0xFF7A6840),
                fontFamily = AmiriBoldFont,
                maxLines = 1
            )
        }

        // Play/Pause Icon Button (.qa-play-ico)
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(emeraldGradient)
                .border(1.2.dp, GoldAccent, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = if (isPlaying) "⏸" else "▶",
                    fontSize = 13.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun FloatingSurahPlayerBar(
    surahName: String,
    currentAyah: Int,
    totalAyahs: Int,
    language: String,
    isPlaying: Boolean,
    isLoading: Boolean,
    isContinuous: Boolean,
    audioPlayMode: QuranAudioPlayMode,
    translationVoiceLang: String,
    onModeChange: (QuranAudioPlayMode, String) -> Unit,
    onToggleContinuous: () -> Unit,
    onNextSurah: () -> Unit,
    onTogglePlayPause: () -> Unit,
    onStop: () -> Unit
) {
    val emeraldGradient = Brush.linearGradient(
        listOf(Color(0xFF0E5D47), Color(0xFF083D32))
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(18.dp), spotColor = Color(0xFF083D32))
            .clip(RoundedCornerShape(18.dp))
            .background(emeraldGradient)
            .border(1.5.dp, GoldAccent, RoundedCornerShape(18.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Player Info (.qa-player-info)
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = surahName,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = if (language == "hi") "चल रहा है · आयत $currentAyah / $totalAyahs" else "Playing · Ayah $currentAyah of $totalAyahs",
                fontSize = 10.sp,
                color = Color(0xFFF3D48A),
                fontWeight = FontWeight.Medium
            )
        }

        // Audio Mode Pill beside controls
        QuranAudioModeButton(
            audioPlayMode = audioPlayMode,
            translationVoiceLang = translationVoiceLang,
            language = language,
            onModeChange = onModeChange
        )

        // 1. Continuous Play Toggle (.qaContinuousBtn)
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(if (isContinuous) GoldAccent else Color.White.copy(alpha = 0.16f))
                .clickable(onClick = onToggleContinuous),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🔁",
                fontSize = 13.sp
            )
        }

        // 2. Next Surah Button (.qaNextBtn)
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.16f))
                .clickable(onClick = onNextSurah),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "⏭",
                fontSize = 13.sp,
                color = Color.White
            )
        }

        // 3. Play / Pause Button (.qaPlayerBtn)
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.22f))
                .border(1.dp, GoldAccent, CircleShape)
                .clickable(onClick = onTogglePlayPause),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(14.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = if (isPlaying) "⏸" else "▶",
                    fontSize = 13.5.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // 4. Close / Stop Button (.qa-player-stop)
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.10f))
                .clickable(onClick = onStop),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "✕",
                fontSize = 13.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
