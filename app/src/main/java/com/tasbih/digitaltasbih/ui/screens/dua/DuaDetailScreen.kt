package com.tasbih.digitaltasbih.ui.screens.dua

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.DisposableEffect
import com.tasbih.digitaltasbih.data.DuaAudioPlayer
import com.tasbih.digitaltasbih.ui.components.dua.DuaAudioRecitationBar
import com.tasbih.digitaltasbih.ui.components.dua.DuaHadithRefModal
import com.tasbih.digitaltasbih.ui.components.dua.DuaPlayAudioButton
import com.tasbih.digitaltasbih.ui.components.dua.DuaSavePillButton
import com.tasbih.digitaltasbih.ui.components.dua.DuaShareButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.data.DuaRepository
import com.tasbih.digitaltasbih.ui.components.dua.DuaFontControlBar
import com.tasbih.digitaltasbih.ui.components.dua.DuaHeaderBar
import com.tasbih.digitaltasbih.ui.components.dua.DuaLanguageRow
import com.tasbih.digitaltasbih.ui.components.dua.SaveToCollectionSheet
import com.tasbih.digitaltasbih.ui.theme.DuaColors

@Composable
fun DuaDetailScreen(
    duaId: String,
    categoryName: String = "",
    onBack: () -> Unit,
    onNavigateHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val preferences = DuaRepository.preferences

    LaunchedEffect(duaId) {
        preferences.addRecentDua(duaId)
    }

    DisposableEffect(duaId) {
        onDispose {
            DuaAudioPlayer.stop()
        }
    }

    val selectedLang by preferences.selectedLanguage.collectAsState()
    val fontScale by preferences.fontScale.collectAsState()
    val bookmarkedIds by preferences.bookmarkedDuaIds.collectAsState()
    val collections by preferences.customCollections.collectAsState()

    var showCollectionSheet by remember { mutableStateOf(false) }
    var showRefModal by remember { mutableStateOf(false) }

    val dua = remember(duaId) { DuaRepository.getDuaById(duaId) }
    val isBookmarked = bookmarkedIds.contains(duaId)

    val scrollState = rememberScrollState()

    val headerTitle = if (categoryName.isNotBlank()) {
        categoryName.uppercase()
    } else {
        dua?.cat?.uppercase() ?: "DUA DETAIL"
    }

    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                DuaHeaderBar(
                    title = headerTitle,
                    onBack = onBack,
                    onFavoritesClick = { showCollectionSheet = true },
                    onSettingsClick = {}
                )
            },
            containerColor = DuaColors.Bg
        ) { paddingValues ->
            if (dua == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Dua not found", color = DuaColors.TextMuted)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(scrollState)
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    // 1. Language Row
                    DuaLanguageRow(
                        selectedLang = selectedLang,
                        onLangSelected = { preferences.setLanguage(it) }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // 2. Font Zoom Control Bar
                    DuaFontControlBar(
                        fontScale = fontScale,
                        onFontScaleChange = { preferences.setFontScale(it) }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 3. Main Dua Card matching Screenshot 11 & 12
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(6.dp, RoundedCornerShape(20.dp))
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White)
                            .border(1.dp, Color(0x260E5D47), RoundedCornerShape(20.dp))
                            .padding(18.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            // Header: 🤲 Title on left, Category pill on right
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text(text = "🤲 ", fontSize = 16.sp)
                                    Text(
                                        text = dua.getLocalizedTitle(selectedLang),
                                        color = DuaColors.TextDark,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        lineHeight = 22.sp
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(DuaColors.Emerald)
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = dua.cat.replaceFirstChar { it.uppercase() },
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            HorizontalDivider(color = Color(0x33C9A84C), thickness = 1.dp)
                            Spacer(modifier = Modifier.height(14.dp))

                            // Arabic Box (Cream rounded box with Amiri font & tashkeel)
                            if (dua.ar.isNotBlank()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFAF8F5))
                                        .border(1.dp, Color(0x22C9A84C), RoundedCornerShape(16.dp))
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                                        Text(
                                            text = dua.ar,
                                            color = DuaColors.ArabicGreen,
                                            fontSize = (23 * fontScale).sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Serif,
                                            textAlign = TextAlign.Center,
                                            lineHeight = (36 * fontScale).sp,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))

                                // Dedicated Audio Recitation Bar
                                DuaAudioRecitationBar(
                                    isPlaying = DuaAudioPlayer.isDuaPlaying(dua.id),
                                    isLoading = DuaAudioPlayer.isDuaLoading(dua.id),
                                    reciterName = DuaAudioPlayer.getAudioMeta(context, dua.id)?.reciter ?: "Sheikh Hamad Al-Durayhim",
                                    onTogglePlay = { DuaAudioPlayer.togglePlay(context, dua) },
                                    selectedLang = selectedLang
                                )

                                Spacer(modifier = Modifier.height(16.dp))
                            }

                            // READING Label & Transliteration
                            val transliteration = dua.getLocalizedTransliteration(selectedLang)
                            if (transliteration.isNotBlank()) {
                                val readingLabel = when (selectedLang) {
                                    "hi" -> "तल्फ़्फ़ुज़ (उच्चारण)"
                                    "ur" -> "تلفظ (Reading)"
                                    "bn" -> "উচ্চারণ (Reading)"
                                    else -> "READING"
                                }
                                Text(
                                    text = readingLabel,
                                    color = DuaColors.Gold,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.2.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = transliteration,
                                    color = DuaColors.TextDark,
                                    fontSize = (14.5 * fontScale).sp,
                                    fontWeight = FontWeight.Medium,
                                    lineHeight = (20 * fontScale).sp
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                            }

                            // MEANING Label & Translation
                            val meaning = dua.getLocalizedMeaning(selectedLang)
                            if (meaning.isNotBlank()) {
                                val meaningLabel = when (selectedLang) {
                                    "hi" -> "तर्जुमा (अर्थ)"
                                    "ur" -> "ترجمہ (Meaning)"
                                    "bn" -> "অনুবাদ (Meaning)"
                                    else -> "MEANING"
                                }
                                Text(
                                    text = meaningLabel,
                                    color = DuaColors.Gold,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.2.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = meaning,
                                    color = DuaColors.TextDark,
                                    fontSize = (14.5 * fontScale).sp,
                                    fontWeight = FontWeight.Normal,
                                    lineHeight = (21 * fontScale).sp
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                            }

                            // "When to recite" note box (Gold left border)
                            val whenNote = dua.getLocalizedWhen(selectedLang)
                            val defaultWhen = when (selectedLang) {
                                "hi" -> "सुबह व शाम के अज़कार में, और दुआ मांगने के मुबारक वक़्त में पढ़ें।"
                                "ur" -> "صبح و شام کے اذکار میں اور دعا مانگتے وقت پڑھیں۔"
                                "bn" -> "সকাল ও সন্ধ্যার দোয়ার সময় পড়ুন।"
                                else -> "Upon stopping, lodging, or resting at any place during daily movement."
                            }
                            val displayWhen = if (whenNote.isNotBlank()) whenNote else defaultWhen
                            val whenLabel = when (selectedLang) {
                                "hi" -> "कब पढ़ें (वक़्त)"
                                "ur" -> "پڑھنے کا وقت"
                                "bn" -> "কখন পড়বেন"
                                else -> "When to recite"
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
                                    .background(DuaColors.WhenNoteBg)
                                    .border(
                                        width = 1.dp,
                                        color = Color(0x33C9A84C),
                                        shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
                                    )
                                    .padding(start = 12.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFFFCFBF8))
                                        .padding(12.dp)
                                ) {
                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(text = "⏰ ", fontSize = 14.sp)
                                            Text(
                                                text = whenLabel,
                                                color = DuaColors.Gold,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = displayWhen,
                                            color = DuaColors.TextDark,
                                            fontSize = 13.sp,
                                            lineHeight = 18.sp
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // "How to recite & Benefits" note box (Green left border)
                            val howNote = dua.getLocalizedHow(selectedLang)
                            val defaultHow = when (selectedLang) {
                                "hi" -> "तादाद: १ बार या ३ बार। यक़ीन और आजिज़ी के साथ पढ़ें।"
                                "ur" -> "تعداد: ۱ یا ۳ مرتبہ۔ خلوصِ دل اور یقین کے ساتھ پڑھیں۔"
                                "bn" -> "আন্তরিকতা ও ভক্তির সাথে পড়ুন।"
                                else -> "Count: 1 time. Recite with pure intention and presence of heart."
                            }
                            val displayHow = if (howNote.isNotBlank()) howNote else defaultHow
                            val howLabel = when (selectedLang) {
                                "hi" -> "फ़ज़ीलत व तरीक़ा"
                                "ur" -> "فضیلت اور طریقہ"
                                "bn" -> "ফজিলত ও নিয়ম"
                                else -> "How to recite & Benefits"
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
                                    .background(DuaColors.HowNoteBg)
                                    .border(
                                        width = 1.dp,
                                        color = Color(0x330E5D47),
                                        shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
                                    )
                                    .padding(start = 12.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFFFCFBF8))
                                        .padding(12.dp)
                                ) {
                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(text = "📜 ", fontSize = 14.sp)
                                            Text(
                                                text = howLabel,
                                                color = DuaColors.Emerald,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = displayHow,
                                            color = DuaColors.TextDark,
                                            fontSize = 13.sp,
                                            lineHeight = 18.sp
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            // Bottom row: Reference pill left, Share & Save buttons right
                            val reference = dua.getLocalizedReference(selectedLang)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Reference pill (Clickable to open Hadith Sanad Modal)
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(Color(0xFFF3ECE0))
                                        .border(1.dp, Color(0x66C9A84C), RoundedCornerShape(20.dp))
                                        .clickable { showRefModal = true }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = if (reference.isNotBlank()) "📜 $reference" else "📜 Authentic Sunnah",
                                            color = DuaColors.TextDark,
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            maxLines = 1,
                                            modifier = Modifier.weight(1f, fill = false)
                                        )
                                        Text(
                                            text = "›",
                                            color = DuaColors.Gold,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                // Play Audio Button
                                DuaPlayAudioButton(
                                    isPlaying = DuaAudioPlayer.isDuaPlaying(dua.id),
                                    isLoading = DuaAudioPlayer.isDuaLoading(dua.id),
                                    onClick = { DuaAudioPlayer.togglePlay(context, dua) },
                                    size = 36.dp,
                                    iconSize = 18.dp
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                // Share button
                                DuaShareButton(
                                    onClick = {
                                        com.tasbih.digitaltasbih.util.DuaShareHelper.shareDua(context, dua, selectedLang)
                                    },
                                    size = 36.dp,
                                    iconSize = 17.dp
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                // Save Pill Button
                                DuaSavePillButton(
                                    isBookmarked = isBookmarked,
                                    onClick = {
                                        preferences.toggleBookmark(duaId)
                                        showCollectionSheet = true
                                    },
                                    selectedLang = selectedLang
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }

        if (showRefModal && dua != null) {
            DuaHadithRefModal(
                dua = dua,
                selectedLang = selectedLang,
                onDismiss = { showRefModal = false }
            )
        }

        if (showCollectionSheet) {
            SaveToCollectionSheet(
                duaId = duaId,
                collections = collections,
                onToggleCollection = { colId ->
                    preferences.toggleDuaInCollection(colId, duaId)
                },
                onCreateCollection = { name ->
                    val newCol = preferences.createCollection(name)
                    preferences.toggleDuaInCollection(newCol.id, duaId)
                },
                onDismiss = { showCollectionSheet = false }
            )
        }
    }
}