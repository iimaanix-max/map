package com.tasbih.digitaltasbih.ui.screens.hajj

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.data.PrayerRepository
import com.tasbih.digitaltasbih.data.hajj.HajjData
import com.tasbih.digitaltasbih.data.hajj.HajjDayStage
import com.tasbih.digitaltasbih.data.hajj.HajjDua
import com.tasbih.digitaltasbih.data.hajj.HajjHadithVirtue
import com.tasbih.digitaltasbih.data.hajj.HajjPillarRequirement
import com.tasbih.digitaltasbih.ui.components.AppToast
import com.tasbih.digitaltasbih.ui.components.rememberAssetPainter
import com.tasbih.digitaltasbih.data.pillars.IslamicReferenceItem
import com.tasbih.digitaltasbih.data.pillars.PillarsReferenceRepository
import com.tasbih.digitaltasbih.ui.components.IslamicReferenceModal
import com.tasbih.digitaltasbih.ui.theme.GoldAccent

import java.util.Locale

@Composable
fun HajjScreen(
    onBack: () -> Unit,
    selectedLanguage: String = PrayerRepository.getLanguage()
) {
    val context = LocalContext.current
    val isDarkTheme = remember { PrayerRepository.isDarkTheme() }

    BackHandler {
        onBack()
    }

    // TTS for Arabic recitation
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var isTtsReady by remember { mutableStateOf(false) }
    var speakingDuaId by remember { mutableStateOf<String?>(null) }

    DisposableEffect(Unit) {
        val ttsInstance = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale("ar")
                isTtsReady = true
            }
        }
        tts = ttsInstance

        onDispose {
            ttsInstance.stop()
            ttsInstance.shutdown()
        }
    }

    fun playArabicAudio(text: String, duaId: String) {
        if (!isTtsReady || tts == null) {
            AppToast.show("Audio initializing...")
            return
        }
        try {
            speakingDuaId = duaId
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "hajj_audio_$duaId")
        } catch (_: Exception) {
            AppToast.show("Audio playback unavailable")
        }
    }

    fun stopAudio() {
        tts?.stop()
        speakingDuaId = null
    }

    fun copyToClipboard(title: String, arabic: String, translation: String) {
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val textToCopy = "$title\n\n$arabic\n\n$translation\n\n(Digital Tasbih App)"
        cm.setPrimaryClip(ClipData.newPlainText("Hajj", textToCopy))
        AppToast.show("Copied to clipboard!")
    }

    fun shareText(title: String, arabic: String, translation: String) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT,
                    "🕋 $title 🕋\n\n$arabic\n\n$translation\n\n📱 Digital Tasbih & Quran App"
                )
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share Hajj Guide"))
        } catch (_: Exception) {
            AppToast.show("Could not share text")
        }
    }

    // Tab state: 0 = तारुफ़ व तल्बिया, 1 = हज के 6 दिन (Day-by-Day), 2 = अरकान व वाजिबात, 3 = मक़बूल हज व फ़ज़ाइल
    var activeReferenceItem by remember { mutableStateOf<IslamicReferenceItem?>(null) }
    var selectedTab by remember { mutableIntStateOf(0) }
    val scrollState = rememberScrollState()

    // Luxury Day & Night Theme Colors
    val bgColor = if (isDarkTheme) Color(0xFF07150E) else Color(0xFFF6F4EE)
    val cardBg = if (isDarkTheme) Color(0xFF0D2218) else Color(0xFFFFFFFF)
    val cardBorder = if (isDarkTheme) Color(0xFF1E4532) else Color(0xFFE2DDD1)
    val primaryText = if (isDarkTheme) Color(0xFFF1EAD8) else Color(0xFF1B3D2F)
    val secondaryText = if (isDarkTheme) Color(0xFFA9BEB1) else Color(0xFF5E6D64)
    val emeraldAccent = Color(0xFF0D4A3E)
    val deepGold = Color(0xFFC99728)

    val heroPainter = rememberAssetPainter("media/hajj_hero.webp")

    val tabs = when (selectedLanguage) {
        "hi", "hinglish" -> listOf("पाँचवाँ अरकान (हज)", "हज के 6 दिन (तरीक़ा)", "अरकान व वाजिबात", "फ़ज़ीलत व अहादीस")
        "ur" -> listOf("پانچواں رکن (حج)", "حج کے ۶ ایام (طریقہ)", "ارکان و واجبات", "فضیلت و احادیث")
        "ar" -> listOf("الركن الخامس (الحج)", "مناسك الحج يوماً بيوم", "الأركان والواجبات", "فضائل وأحاديث")
        else -> listOf("5th Pillar (Hajj)", "6 Days of Hajj", "Pillars & Wajibat", "Virtues & Hadiths")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .statusBarsPadding()
    ) {
        // TOP APP BAR
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
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (isDarkTheme) Color(0xFF133224) else Color(0xFFEAE5D9))
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "←",
                    fontSize = 22.sp,
                    color = primaryText,
                    fontWeight = FontWeight.Bold
                )
            }

            // Screen Header Title
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = when (selectedLanguage) {
                        "hi", "hinglish" -> "हज (Al-Hajj)"
                        "ur" -> "حج بیت اللہ (الحج)"
                        "ar" -> "الْحَجُّ الْمَبْرُور"
                        else -> "Al-Hajj (Pilgrimage)"
                    },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryText
                )
                Text(
                    text = when (selectedLanguage) {
                        "hi", "hinglish" -> "इस्लाम का पाँचवाँ मुक़द्दस रुक्न"
                        "ur" -> "اسلام کا پانچواں مقدس رکن"
                        "ar" -> "الركن الخامس من أركان الإسلام"
                        else -> "The 5th Pillar of Islam"
                    },
                    fontSize = 11.sp,
                    color = deepGold,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Pillar Number Pill
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF0D4A3E), Color(0xFF1B6B58))
                        )
                    )
                    .border(1.dp, deepGold.copy(alpha = 0.6f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "5",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
        }

        // TABS SELECTOR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tabs.forEachIndexed { index, tabTitle ->
                val isSelected = selectedTab == index
                val tabBg = if (isSelected) {
                    Brush.horizontalGradient(listOf(Color(0xFF0D4A3E), Color(0xFF165C4D)))
                } else {
                    Brush.horizontalGradient(
                        if (isDarkTheme) listOf(Color(0xFF132A20), Color(0xFF132A20))
                        else listOf(Color(0xFFEBE6DC), Color(0xFFEBE6DC))
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(tabBg)
                        .border(
                            width = 1.dp,
                            color = if (isSelected) deepGold else cardBorder,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { selectedTab = index }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = tabTitle,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else secondaryText
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // TAB CONTENT
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            when (selectedTab) {
                0 -> HajjOverviewSection(
                    heroPainter = heroPainter,
                    duas = HajjData.DUAS,
                    speakingDuaId = speakingDuaId,
                    onPlay = { text, id -> playArabicAudio(text, id) },
                    onStop = { stopAudio() },
                    onCopy = { title, ar, tr -> copyToClipboard(title, ar, tr) },
                    onShare = { title, ar, tr -> shareText(title, ar, tr) },
                    onReferenceClick = { ref -> activeReferenceItem = PillarsReferenceRepository.findReference(ref, "Hajj") },
                    cardBg = cardBg,
                    cardBorder = cardBorder,
                    primaryText = primaryText,
                    secondaryText = secondaryText,
                    deepGold = deepGold,
                    isDarkTheme = isDarkTheme,
                    selectedLanguage = selectedLanguage
                )
                1 -> HajjDaysSection(
                    days = HajjData.DAYS,
                    onReferenceClick = { ref -> activeReferenceItem = PillarsReferenceRepository.findReference(ref, "Hajj") },
                    cardBg = cardBg,
                    cardBorder = cardBorder,
                    primaryText = primaryText,
                    secondaryText = secondaryText,
                    deepGold = deepGold,
                    isDarkTheme = isDarkTheme,
                    selectedLanguage = selectedLanguage
                )
                2 -> HajjPillarsSection(
                    items = HajjData.PILLARS_AND_WAJIBAT,
                    onReferenceClick = { ref -> activeReferenceItem = PillarsReferenceRepository.findReference(ref, "Hajj") },
                    cardBg = cardBg,
                    cardBorder = cardBorder,
                    primaryText = primaryText,
                    secondaryText = secondaryText,
                    deepGold = deepGold,
                    isDarkTheme = isDarkTheme,
                    selectedLanguage = selectedLanguage
                )
                3 -> HajjVirtuesSection(
                    virtues = HajjData.VIRTUES,
                    onReferenceClick = { ref -> activeReferenceItem = PillarsReferenceRepository.findReference(ref, "Hajj") },
                    cardBg = cardBg,
                    cardBorder = cardBorder,
                    primaryText = primaryText,
                    secondaryText = secondaryText,
                    deepGold = deepGold,
                    isDarkTheme = isDarkTheme,
                    selectedLanguage = selectedLanguage
                )
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }

    if (activeReferenceItem != null) {
        IslamicReferenceModal(
            item = activeReferenceItem!!,
            isDarkTheme = isDarkTheme,
            selectedLanguage = selectedLanguage,
            onClose = { activeReferenceItem = null }
        )
    }
}

@Composable
private fun HajjOverviewSection(
    heroPainter: androidx.compose.ui.graphics.painter.Painter,
    duas: List<HajjDua>,
    speakingDuaId: String?,
    onPlay: (String, String) -> Unit,
    onStop: () -> Unit,
    onCopy: (String, String, String) -> Unit,
    onShare: (String, String, String) -> Unit,
    onReferenceClick: (String) -> Unit,
    cardBg: Color,
    cardBorder: Color,
    primaryText: Color,
    secondaryText: Color,
    deepGold: Color,
    isDarkTheme: Boolean,
    selectedLanguage: String
) {
    // HERO ARTWORK BANNER
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, deepGold.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
            .shadow(4.dp, RoundedCornerShape(18.dp))
    ) {
        Image(
            painter = heroPainter,
            contentDescription = "Hajj Hero",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0xBB051B13),
                            Color(0xFA051B13)
                        )
                    )
                )
        )

        // Banner text overlay
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(deepGold)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "5th PILLAR OF ISLAM",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "الرُّكْنُ الْخَامِسُ: الْحَجُّ",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = when (selectedLanguage) {
                    "hi", "hinglish" -> "बैतुल्लाह शरीफ़ का मुक़द्दस हज"
                    "ur" -> "بیت اللہ شریف کا مقدس حج"
                    "ar" -> "حج بيت الله الحرام"
                    else -> "The Sacred Pilgrimage to Makkah"
                },
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = when (selectedLanguage) {
                    "hi", "hinglish" -> "ज़िंदगी में एक बार साहिब-ए-इस्तिताअत पर फ़र्ज़-ए-ऐन"
                    "ur" -> "زندگی میں ایک بار صاحبِ استطاعت پر فرضِ عین"
                    "ar" -> "وَلِلَّهِ عَلَى النَّاسِ حِجُّ الْبَيْتِ مَنِ اسْتَطَاعَ إِلَيْهِ سَبِيلًا"
                    else -> "Obligatory once in a lifetime for those who can afford the journey"
                },
                fontSize = 11.sp,
                color = Color(0xFFD6C8A8)
            )
        }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // QURANIC INJUNCTION CARD
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cardBg)
            .border(1.dp, deepGold.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🕋", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = when (selectedLanguage) {
                            "hi", "hinglish" -> "हज का फ़रमान-ए-इलाही"
                            "ur" -> "حج کا فرمانِ الٰہی"
                            "ar" -> "الأمر الرباني بالحج"
                            else -> "Divine Injunction of Hajj"
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = deepGold
                    )
                }
                Text(
                    text = when (selectedLanguage) {
                        "hi", "hinglish" -> "सूरह आले-इमरान (3:97)"
                        "ur" -> "سورۃ آلِ عمران (3:97)"
                        "ar" -> "سورة آل عمران (٣:٩٧)"
                        else -> "Surah Ali 'Imran (3:97)"
                    },
                    fontSize = 11.sp,
                    color = secondaryText,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Arabic Ayah
            Text(
                text = "وَلِلَّهِ عَلَى النَّاسِ حِجُّ الْبَيْتِ مَنِ اسْتَطَاعَ إِلَيْهِ سَبِيلًا ۚ وَمَن كَفَرَ فَإِنَّ اللَّهَ غَنِيٌّ عَنِ الْعَالَمِينَ",
                fontSize = 18.sp,
                lineHeight = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Right,
                color = primaryText,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Translation
            Text(
                text = when (selectedLanguage) {
                    "hi", "hinglish" -> "और लोगों पर अल्लाह का हक़ है कि जो उस घर (काबा) तक पहुँचने की इस्तेताअत रखता हो वह उसका हज करे। और जो कोई इन्कार करे तो अल्लाह तमाम जहानों से बेनियाज़ है।"
                    "ur" -> "اور اللہ کے لیے لوگوں پر اس گھر کا حج فرض ہے جو اس تک جانے کی طاقت رکھے، اور جو ناشکری کرے تو اللہ تمام جہانوں سے بے نیاز ہے۔"
                    "ar" -> "فرض الله الحج على المستطيع من عباده مرة في العمر."
                    else -> "And [due] to Allah from the people is a pilgrimage to the House - for whoever is able to find thereto a way. But whoever disbelieves - then indeed, Allah is free from need of the worlds."
                },
                fontSize = 13.sp,
                lineHeight = 19.sp,
                color = primaryText
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Clickable Ayah Reference Chip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isDarkTheme) Color(0xFF072118) else Color(0xFFF1F5F9))
                    .clickable { onReferenceClick("Surah Ali 'Imran 3:97") }
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📖 Surah Ali 'Imran (3:97)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = deepGold
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isDarkTheme) Color(0xFF133E2B) else Color(0xFFDCFCE7))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "🔍 View Ayah ↗",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) GoldAccent else Color(0xFF059669)
                    )
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // TALBIYAH & CORE DUAS (With Audio recitation)
    Text(
        text = when (selectedLanguage) {
            "hi", "hinglish" -> "हज का अज़ीम नारा — तल्बिया व दुआएं"
            "ur" -> "حج کا عظیم نعرہ — تلبیہ و دعائیں"
            "ar" -> "التلبية وأعظم الأدعية في الحج"
            else -> "The Great Talbiyah & Sacred Duas"
        },
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = primaryText
    )

    Spacer(modifier = Modifier.height(10.dp))

    duas.forEach { dua ->
        val isSpeaking = speakingDuaId == dua.id

        val title = when (selectedLanguage) {
            "hi", "hinglish" -> dua.titleHindi
            "ur" -> dua.titleUrdu
            else -> dua.titleEnglish
        }

        val meaning = when (selectedLanguage) {
            "hi", "hinglish" -> dua.meaningHindi
            "ur" -> dua.meaningUrdu
            else -> dua.meaningEnglish
        }

        val contextNote = when (selectedLanguage) {
            "hi", "hinglish" -> dua.contextHindi
            else -> dua.contextEnglish
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(cardBg)
                .border(
                    width = 1.dp,
                    color = if (isSpeaking) deepGold else cardBorder,
                    shape = RoundedCornerShape(16.dp)
                )
                .shadow(2.dp, RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = deepGold
                )

                Spacer(modifier = Modifier.height(4.dp))

                val sourceText = if (selectedLanguage == "hi" || selectedLanguage == "hinglish") dua.hadithSource else if (dua.hadithSourceEnglish.isNotEmpty()) dua.hadithSourceEnglish else dua.hadithSource
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isDarkTheme) Color(0xFF132F23) else Color(0xFFE8E4DA))
                        .clickable { onReferenceClick(sourceText) }
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "📚 $sourceText",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = secondaryText
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isDarkTheme) Color(0xFF1B4D36) else Color(0xFFC7D2FE))
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "🔍 View Hadith ↗",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) GoldAccent else Color(0xFF3730A3)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = dua.arabicText,
                    fontSize = 20.sp,
                    lineHeight = 34.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Right,
                    color = primaryText,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = dua.transliteration,
                    fontSize = 12.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    color = deepGold,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = meaning,
                    fontSize = 13.sp,
                    color = primaryText,
                    lineHeight = 19.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "💡 $contextNote",
                    fontSize = 11.sp,
                    color = secondaryText
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ACTIONS
                val audioBtnText = if (isSpeaking) {
                    when (selectedLanguage) {
                        "hi", "hinglish" -> "⏹ रोकें"
                        "ur" -> "⏹ روکیں"
                        "ar" -> "⏹ إيقاف"
                        else -> "⏹ Stop"
                    }
                } else {
                    when (selectedLanguage) {
                        "hi", "hinglish" -> "🔊 सुनें"
                        "ur" -> "🔊 سنیں"
                        "ar" -> "🔊 استمع"
                        else -> "🔊 Listen"
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isSpeaking) deepGold
                                else (if (isDarkTheme) Color(0xFF163E2D) else Color(0xFFE2DDD1))
                            )
                            .clickable {
                                if (isSpeaking) onStop() else onPlay(dua.arabicText, dua.id)
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = audioBtnText,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSpeaking) Color.Black else primaryText
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Copy Button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isDarkTheme) Color(0xFF163E2D) else Color(0xFFE2DDD1))
                            .clickable { onCopy(title, dua.arabicText, meaning) }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        val copyBtnText = when (selectedLanguage) {
                            "hi", "hinglish" -> "📋 कॉपी"
                            "ur" -> "📋 کاپی"
                            "ar" -> "📋 نسخ"
                            else -> "📋 Copy"
                        }
                        Text(text = copyBtnText, fontSize = 11.sp, color = primaryText)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Share Button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isDarkTheme) Color(0xFF163E2D) else Color(0xFFE2DDD1))
                            .clickable { onShare(title, dua.arabicText, meaning) }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        val shareBtnText = when (selectedLanguage) {
                            "hi", "hinglish" -> "↗ शेयर"
                            "ur" -> "↗ شیئر"
                            "ar" -> "↗ مشاركة"
                            else -> "↗ Share"
                        }
                        Text(text = shareBtnText, fontSize = 11.sp, color = primaryText)
                    }
                }
            }
        }
    }
}

@Composable
private fun HajjDaysSection(
    days: List<HajjDayStage>,
    onReferenceClick: (String) -> Unit,
    cardBg: Color,
    cardBorder: Color,
    primaryText: Color,
    secondaryText: Color,
    deepGold: Color,
    isDarkTheme: Boolean,
    selectedLanguage: String
) {
    Text(
        text = when (selectedLanguage) {
            "hi", "hinglish" -> "हज के 6 मुक़द्दस दिन (8 से 13 ज़िलहिज्जा)"
            "ur" -> "حج کے ۶ مقدس ایام (۸ تا ۱۳ ذوالحجہ)"
            "ar" -> "أيام الحج الستة من الإحرام إلى الوداع"
            else -> "The 6 Sacred Days of Hajj (8th - 13th Dhul-Hijjah)"
        },
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = primaryText
    )

    Spacer(modifier = Modifier.height(10.dp))

    days.forEach { day ->
        val title = when (selectedLanguage) {
            "hi", "hinglish" -> day.titleHindi
            "ur" -> day.titleUrdu
            else -> day.titleEnglish
        }

        val location = when (selectedLanguage) {
            "hi", "hinglish" -> day.locationHindi
            else -> day.locationEnglish
        }

        val summary = when (selectedLanguage) {
            "hi", "hinglish" -> day.summaryHindi
            else -> day.summaryEnglish
        }

        val rituals = when (selectedLanguage) {
            "hi", "hinglish" -> day.keyRitualsHindi
            else -> day.keyRitualsEnglish
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(cardBg)
                .border(1.dp, cardBorder, RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(deepGold)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = day.dateHijri,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }

                    Text(
                        text = "📍 $location",
                        fontSize = 11.sp,
                        color = secondaryText,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryText
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = summary,
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    color = secondaryText
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Key Rituals Bullet Points
                rituals.forEach { ritual ->
                    Row(
                        modifier = Modifier.padding(vertical = 2.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "• ",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = deepGold
                        )
                        Text(
                            text = ritual,
                            fontSize = 12.sp,
                            lineHeight = 17.sp,
                            color = primaryText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                val dayRef = if (selectedLanguage == "hi" || selectedLanguage == "hinglish") day.quranHadithReference else if (day.quranHadithReferenceEnglish.isNotEmpty()) day.quranHadithReferenceEnglish else day.quranHadithReference
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isDarkTheme) Color(0xFF072118) else Color(0xFFF1F5F9))
                        .clickable { onReferenceClick(dayRef) }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📖 $dayRef",
                        fontSize = 10.5.sp,
                        color = deepGold,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isDarkTheme) Color(0xFF133E2B) else Color(0xFFDCFCE7))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "🔍 View Reference ↗",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) GoldAccent else Color(0xFF059669)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HajjPillarsSection(
    items: List<HajjPillarRequirement>,
    onReferenceClick: (String) -> Unit,
    cardBg: Color,
    cardBorder: Color,
    primaryText: Color,
    secondaryText: Color,
    deepGold: Color,
    isDarkTheme: Boolean,
    selectedLanguage: String
) {
    Text(
        text = when (selectedLanguage) {
            "hi", "hinglish" -> "हज के 4 अरकान (फ़राइज़) और 7 वाजिबात"
            "ur" -> "حج کے ۴ ارکان (فرائض) اور ۷ واجبات"
            "ar" -> "أركان الحج الأربعة وواجباته السبعة"
            else -> "4 Pillars (Arkaan) & 7 Mandatory Acts (Wajibat)"
        },
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = primaryText
    )

    Spacer(modifier = Modifier.height(4.dp))

    Text(
        text = when (selectedLanguage) {
            "hi", "hinglish" -> "अरकान के छूटने से हज बातिल हो जाता है (क़ुरबानी से भी पूरा नहीं होता), जबकि वाजिब छूटने पर दम (क़ुरबानी) लाज़िम आता है।"
            "ur" -> "رکن کے چھوٹنے سے حج باطل ہو جاتا ہے جبکہ واجب کے ترک پر دم (قربانی) لازم آتی ہے۔"
            "ar" -> "الركن لا يصح الحج بدونه ولا يجبر بدم، والواجب يجبر بذبح شاة."
            else -> "Missing a Pillar invalidates Hajj; missing an Obligation (Wajib) incurs a compensatory sacrifice (Damm)."
        },
        fontSize = 11.sp,
        color = secondaryText,
        lineHeight = 16.sp
    )

    Spacer(modifier = Modifier.height(10.dp))

    items.forEach { item ->
        val badgeColor = if (item.isRukn) Color(0xFFD32F2F) else Color(0xFF0D4A3E)
        val badgeText = if (item.isRukn) {
            when (selectedLanguage) {
                "hi", "hinglish" -> "⭐ रुक्न (फ़र्ज़)"
                "ur" -> "⭐ رکن (فرض)"
                "ar" -> "⭐ ركن (فرض)"
                else -> "⭐ Core Pillar (Fard)"
            }
        } else {
            when (selectedLanguage) {
                "hi", "hinglish" -> "📌 वाजिब"
                "ur" -> "📌 واجب"
                "ar" -> "📌 واجب"
                else -> "📌 Mandatory (Wajib)"
            }
        }

        val title = when (selectedLanguage) {
            "hi", "hinglish" -> item.titleHindi
            "ur" -> item.titleUrdu
            else -> item.titleEnglish
        }

        val desc = when (selectedLanguage) {
            "hi", "hinglish" -> item.descriptionHindi
            else -> item.descriptionEnglish
        }

        val consequence = when (selectedLanguage) {
            "hi", "hinglish" -> item.consequenceHindi
            else -> item.consequenceEnglish
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(cardBg)
                .border(1.dp, cardBorder, RoundedCornerShape(14.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(badgeColor.copy(alpha = 0.15f))
                            .border(1.dp, badgeColor.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = badgeText,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = badgeColor
                        )
                    }

                    Text(
                        text = item.arabicTitle,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryText
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = deepGold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = desc,
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    color = primaryText
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "⚠️ $consequence",
                    fontSize = 11.sp,
                    color = secondaryText
                )

                Spacer(modifier = Modifier.height(4.dp))

                val itemRef = if (selectedLanguage == "hi" || selectedLanguage == "hinglish") item.reference else if (item.referenceEnglish.isNotEmpty()) item.referenceEnglish else item.reference
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isDarkTheme) Color(0xFF072118) else Color(0xFFF1F5F9))
                        .clickable { onReferenceClick(itemRef) }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📚 $itemRef",
                        fontSize = 10.5.sp,
                        color = deepGold,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isDarkTheme) Color(0xFF133E2B) else Color(0xFFDCFCE7))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "🔍 View Hadith ↗",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) GoldAccent else Color(0xFF059669)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HajjVirtuesSection(
    virtues: List<HajjHadithVirtue>,
    onReferenceClick: (String) -> Unit,
    cardBg: Color,
    cardBorder: Color,
    primaryText: Color,
    secondaryText: Color,
    deepGold: Color,
    isDarkTheme: Boolean,
    selectedLanguage: String
) {
    Text(
        text = when (selectedLanguage) {
            "hi", "hinglish" -> "हज-ए-मबरूर की अज़ीम फ़ज़ीलत (सहीह अहादीस)"
            "ur" -> "حجِ مبرور کی عظیم فضیلت (صحیح احادیث)"
            "ar" -> "فضائل الحج المبرور في السنة النبوية"
            else -> "Virtues of Accepted Hajj (Sahih Hadith)"
        },
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = primaryText
    )

    Spacer(modifier = Modifier.height(10.dp))

    virtues.forEach { v ->
        val topic = when (selectedLanguage) {
            "hi", "hinglish" -> v.topicHindi
            else -> v.topicEnglish
        }

        val translation = when (selectedLanguage) {
            "hi", "hinglish" -> v.translationHindi
            "ur" -> v.translationUrdu
            else -> v.translationEnglish
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(cardBg)
                .border(1.dp, deepGold.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                .padding(14.dp)
        ) {
            Column {
                Text(
                    text = topic,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = deepGold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = v.arabicText,
                    fontSize = 16.sp,
                    lineHeight = 26.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Right,
                    color = primaryText,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = translation,
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    color = primaryText
                )

                Spacer(modifier = Modifier.height(6.dp))

                val vSource = if (selectedLanguage == "hi" || selectedLanguage == "hinglish") v.source else if (v.sourceEnglish.isNotEmpty()) v.sourceEnglish else v.source
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isDarkTheme) Color(0xFF072118) else Color(0xFFF1F5F9))
                        .clickable { onReferenceClick(vSource) }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📚 $vSource",
                        fontSize = 10.5.sp,
                        color = secondaryText,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isDarkTheme) Color(0xFF133E2B) else Color(0xFFDCFCE7))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "🔍 View Hadith ↗",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) GoldAccent else Color(0xFF059669)
                        )
                    }
                }
            }
        }
    }
}

