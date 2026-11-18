package com.tasbih.digitaltasbih.ui.screens.sawm

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.data.PrayerRepository
import com.tasbih.digitaltasbih.data.sawm.SawmData
import com.tasbih.digitaltasbih.data.sawm.SawmDua
import com.tasbih.digitaltasbih.data.sawm.SawmHadithVirtue
import com.tasbih.digitaltasbih.data.sawm.SawmRule
import com.tasbih.digitaltasbih.data.sawm.VoluntaryFast
import com.tasbih.digitaltasbih.ui.components.AppToast
import com.tasbih.digitaltasbih.ui.components.rememberAssetPainter
import com.tasbih.digitaltasbih.ui.theme.GoldAccent
import com.tasbih.digitaltasbih.data.pillars.IslamicReferenceItem
import com.tasbih.digitaltasbih.data.pillars.PillarsReferenceRepository
import com.tasbih.digitaltasbih.ui.components.IslamicReferenceModal
import java.util.Locale

@Composable
fun SawmScreen(
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
    var activeReferenceItem by remember { mutableStateOf<IslamicReferenceItem?>(null) }

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
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "sawm_audio_$duaId")
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
        cm.setPrimaryClip(ClipData.newPlainText("Sawm", textToCopy))
        AppToast.show("Copied to clipboard!")
    }

    fun shareText(title: String, arabic: String, translation: String) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT,
                    "🌙 $title 🌙\n\n$arabic\n\n$translation\n\n📱 Digital Tasbih & Quran App"
                )
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share Sawm Guide"))
        } catch (_: Exception) {
            AppToast.show("Could not share text")
        }
    }

    // Tab state: 0 = तारुफ़ (Overview), 1 = सहरी व इफ़्तार (Duas), 2 = अहकाम व मसाइल (Rules), 3 = नफ़्ल रोज़े व फ़ज़ाइल (Virtues)
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

    val heroPainter = rememberAssetPainter("media/sawm_hero.webp")

    val tabs = when (selectedLanguage) {
        "hi", "hinglish" -> listOf("चौथा अरकान (स़ौम)", "सहरी व इफ़्तार दुआएं", "अहकाम व मसाइल", "नफ़्ल रोज़े व फ़ज़ाइल")
        "ur" -> listOf("چوتھا رکن (صوم)", "سحری و افطار دعائیں", "احکام و مسائل", "نفلی روزے و فضائل")
        "ar" -> listOf("الركن الرابع (الصيام)", "أدعية السحور والإفطار", "أحكام ومسائل", "صيام التطوع وفضائله")
        else -> listOf("4th Pillar (Sawm)", "Suhoor & Iftar Duas", "Rules & Exemptions", "Voluntary Fasts & Virtues")
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
                        "hi", "hinglish" -> "स़ौम (Sawm — रोज़ा)"
                        "ur" -> "صوم (روزہ)"
                        "ar" -> "الصِّيَامُ"
                        else -> "Sawm (Fasting)"
                    },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryText
                )
                Text(
                    text = when (selectedLanguage) {
                        "hi", "hinglish" -> "इस्लाम का चौथा मुक़द्दस रुक्न"
                        "ur" -> "اسلام کا چوتھا مقدس رکن"
                        "ar" -> "الركن الرابع من أركان الإسلام"
                        else -> "The 4th Pillar of Islam"
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
                    text = "4",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
        }

        // TABS SELECTOR (Pill Chips)
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
                0 -> SawmOverviewSection(
                    heroPainter = heroPainter,
                    cardBg = cardBg,
                    cardBorder = cardBorder,
                    primaryText = primaryText,
                    secondaryText = secondaryText,
                    deepGold = deepGold,
                    isDarkTheme = isDarkTheme,
                    selectedLanguage = selectedLanguage,
                    onReferenceClick = { ref -> activeReferenceItem = PillarsReferenceRepository.findReference(ref) }
                )
                1 -> SawmDuasSection(
                    duas = SawmData.DUAS,
                    speakingDuaId = speakingDuaId,
                    onPlay = { text, id -> playArabicAudio(text, id) },
                    onStop = { stopAudio() },
                    onCopy = { title, ar, tr -> copyToClipboard(title, ar, tr) },
                    onShare = { title, ar, tr -> shareText(title, ar, tr) },
                    cardBg = cardBg,
                    cardBorder = cardBorder,
                    primaryText = primaryText,
                    secondaryText = secondaryText,
                    deepGold = deepGold,
                    isDarkTheme = isDarkTheme,
                    selectedLanguage = selectedLanguage,
                    onReferenceClick = { ref -> activeReferenceItem = PillarsReferenceRepository.findReference(ref) }
                )
                2 -> SawmRulesSection(
                    rules = SawmData.RULES,
                    cardBg = cardBg,
                    cardBorder = cardBorder,
                    primaryText = primaryText,
                    secondaryText = secondaryText,
                    deepGold = deepGold,
                    isDarkTheme = isDarkTheme,
                    selectedLanguage = selectedLanguage,
                    onReferenceClick = { ref -> activeReferenceItem = PillarsReferenceRepository.findReference(ref) }
                )
                3 -> SawmVoluntarySection(
                    voluntaryFasts = SawmData.VOLUNTARY_FASTS,
                    virtues = SawmData.VIRTUES,
                    cardBg = cardBg,
                    cardBorder = cardBorder,
                    primaryText = primaryText,
                    secondaryText = secondaryText,
                    deepGold = deepGold,
                    isDarkTheme = isDarkTheme,
                    selectedLanguage = selectedLanguage,
                    onReferenceClick = { ref -> activeReferenceItem = PillarsReferenceRepository.findReference(ref) }
                )
            }

            Spacer(modifier = Modifier.height(28.dp))
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
}

@Composable
private fun SawmOverviewSection(
    heroPainter: androidx.compose.ui.graphics.painter.Painter,
    cardBg: Color,
    cardBorder: Color,
    primaryText: Color,
    secondaryText: Color,
    deepGold: Color,
    isDarkTheme: Boolean,
    selectedLanguage: String,
    onReferenceClick: (String) -> Unit = {}
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
            contentDescription = "Sawm Hero",
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
                        text = "4th PILLAR OF ISLAM",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "الرُّكْنُ الرَّابِعُ: الصِّيَامُ",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = when (selectedLanguage) {
                    "hi", "hinglish" -> "माह-ए-रमज़ान के मुक़द्दस रोज़े"
                    "ur" -> "ماہِ رمضان المبارک کے روزے"
                    "ar" -> "صوم شهر رمضان المبارك"
                    else -> "The Obligatory Fast of Ramadan"
                },
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = when (selectedLanguage) {
                    "hi", "hinglish" -> "तक़्वा, गुनाहों से हिफ़ाज़त और अल्लाह की रज़ा का अज़ीम ज़रिया"
                    "ur" -> "تقویٰ، گناہوں سے ڈھال اور قربِ الٰہی کا عظیم ذریعہ"
                    "ar" -> "لَعَلَّكُمْ تَتَّقُونَ — جُنَّةٌ مِنَ النَّارِ"
                    else -> "Attaining Taqwa, shield from sins, and Divine closeness"
                },
                fontSize = 11.sp,
                color = Color(0xFFD6C8A8)
            )
        }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // CORE QURANIC COMMAND CARD (Clickable to view original)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cardBg)
            .border(1.dp, deepGold.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .clickable { onReferenceClick("Surah Al-Baqarah (2:183)") }
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "📖", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = when (selectedLanguage) {
                            "hi", "hinglish" -> "रोज़े की फ़र्ज़ियत (क़ुरआन मजीद)"
                            "ur" -> "روزے کی فرضیت (قرآن مجید)"
                            "ar" -> "فرضية الصيام في القرآن الكريم"
                            else -> "Obligation of Fasting (Quran)"
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = deepGold
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = when (selectedLanguage) {
                            "hi", "hinglish" -> "सूरह अल-बक़रह (2:183)"
                            "ur" -> "سورۃ البقرۃ (2:183)"
                            "ar" -> "سورة البقرة (٢:١٨٣)"
                            else -> "Surah Al-Baqarah (2:183)"
                        },
                        fontSize = 11.sp,
                        color = secondaryText,
                        fontWeight = FontWeight.Medium
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(deepGold.copy(alpha = 0.15f))
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Text(text = "🔍 Read ↗", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = deepGold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Arabic Ayah
            Text(
                text = "يَا أَيُّهَا الَّذِينَ آمَنُوا كُتِبَ عَلَيْكُمُ الصِّيَامُ كَمَا كُتِبَ عَلَى الَّذِينَ مِن قَبْلِكُمْ لَعَلَّكُمْ تَتَّقُونَ",
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
                    "hi", "hinglish" -> "ऐ ईमान वालो! तुम पर रोज़े फ़र्ज़ किए गए जिस तरह तुमसे पहले के लोगों पर फ़र्ज़ किए गए थे, ताकि तुम्हारे अंदर तक़्वा (परहेज़गारी) पैदा हो।"
                    "ur" -> "اے ایمان والو! تم پر روزے فرض کیے گئے جیسے تم سے اگلوں پر فرض ہوئے تھے تاکہ تم پرہیزگار بن جاؤ۔"
                    "ar" -> "كتب الله الصيام على أمة محمد ﷺ كما كتبه على الأمم السابقة لتحقيق التقوى."
                    else -> "O you who have believed, decreed upon you is fasting as it was decreed upon those before you that you may become righteous (attain Taqwa)."
                },
                fontSize = 13.sp,
                lineHeight = 19.sp,
                color = primaryText
            )
        }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // 4 PILLARS OF SAWM EXPLANATION
    Text(
        text = when (selectedLanguage) {
            "hi", "hinglish" -> "रोज़े के अरकान व हक़ीक़त"
            "ur" -> "روزے کے بنیادی ارکان"
            "ar" -> "أركان الصيام الأساسية"
            else -> "Essential Pillars of Fasting"
        },
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = primaryText
    )

    Spacer(modifier = Modifier.height(8.dp))

    val pillarsInfo = when (selectedLanguage) {
        "hi", "hinglish" -> listOf(
            Pair("1. नीयत (الإِمْسَاكُ بِنِيَّة)", "सुबह सादिक़ से पहले दिल से ख़ालिस अल्लाह की रज़ा के लिए रोज़े का इरादा करना।"),
            Pair("2. मुफ़्तिरात से रुकना (الإِمْسَاك)", "फ़ज्र (सुबह सादिक़) से लेकर सूरज डूबने (मग़रिब) तक खाने, पीने और नफ़्सानी ख़्वाहिशात से मुकम्मल बाज़ रहना।"),
            Pair("3. वक़्त (الزَّمَانُ الْمَخْصُوص)", "फ़ज्र के तुलूअ से लेकर मग़रिब के ग़ुरूब तक का मुक़र्ररा वक़्त।"),
            Pair("4. गुनाहों से परहेज़ (صَوْمُ الْجَوَارِح)", "ज़बान, आँख, कान और दिल को ग़ीबत, झूठ, गाली-गलौज और हराम कामों से बचाना (बुख़ारी 1903)।")
        )
        "ur" -> listOf(
            Pair("1. نیت (الإمساك بنية)", "صبح صادق سے پہلے خالص اللہ کی رضا کے لیے دل سے روزے کا پختہ ارادہ کرنا۔"),
            Pair("2. امساک (کھانے پینے سے رکنا)", "فجر سے لے کر غروبِ آفتاب (مغرب) تک کھانے پینے اور شہوت سے مکمل باز رہنا۔"),
            Pair("3. مخصوص وقت (الزمان المخصوص)", "صبح صادق کے طلوع سے لے کر غروبِ آفتاب تک کا معین وقت۔"),
            Pair("4. گناہوں سے پرہیز (صوم الجوارح)", "زبان، آنکھ، کان اور تمام اعضاء کو جھوٹ، غیبت اور حرام کاموں سے بچانا (بخاری 1903)۔")
        )
        "ar" -> listOf(
            Pair("1. النية (الإِمْسَاكُ بِنِيَّة)", "عزم القلب على الصيام تقرباً إلى الله تعالى قبل طلوع الفجر."),
            Pair("2. الإمساك عن المفطرات", "الامتناع عن الأكل والشرب والجماع وسائر المفطرات من طلوع الفجر إلى غروب الشمس."),
            Pair("3. الزمان المخصوص", "الوقت المحدد من طلوع الفجر الصادق إلى غروب كامل قرص الشمس."),
            Pair("4. صوم الجوارح والأخلاق", "كف اللسان والسمع والبصر عن اللغو والرفث وقول الزور (صحيح البخاري 1903).")
        )
        else -> listOf(
            Pair("1. Intention (An-Niyyah)", "Forming the sincere intention in the heart for Allah before the break of dawn (Fajr)."),
            Pair("2. Abstinence (Al-Imsak)", "Abstaining completely from eating, drinking, and intimate relations from dawn until sunset."),
            Pair("3. The Designated Time (Az-Zaman)", "Observing fasting strictly from true dawn (Fajr) until complete sunset (Maghrib)."),
            Pair("4. Restraint of Limbs & Speech (Sawm al-Jawarih)", "Guarding the tongue, ears, and eyes against lying, backbiting, and evil deeds (Bukhari 1903).")
        )
    }

    pillarsInfo.forEach { (title, desc) ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(cardBg)
                .border(1.dp, cardBorder, RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Column {
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
            }
        }
    }
}

@Composable
private fun SawmDuasSection(
    duas: List<SawmDua>,
    speakingDuaId: String?,
    onPlay: (String, String) -> Unit,
    onStop: () -> Unit,
    onCopy: (String, String, String) -> Unit,
    onShare: (String, String, String) -> Unit,
    cardBg: Color,
    cardBorder: Color,
    primaryText: Color,
    secondaryText: Color,
    deepGold: Color,
    isDarkTheme: Boolean,
    selectedLanguage: String,
    onReferenceClick: (String) -> Unit = {}
) {
    Text(
        text = when (selectedLanguage) {
            "hi", "hinglish" -> "सहरी व इफ़्तार की मस्नून दुआएं (बा-हवाला)"
            "ur" -> "سحری و افطار کی مسنون دعائیں (باحوالہ)"
            "ar" -> "أدعية السحور والإفطار المأثورة"
            else -> "Sunnah Supplications for Suhoor & Iftar"
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

        val timing = when (selectedLanguage) {
            "hi", "hinglish" -> dua.timingHindi
            else -> dua.timingEnglish
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
                // Header: Title + Hadith Source Tag
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = deepGold,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Source Badge (Clickable with Hadith Modal)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isDarkTheme) Color(0xFF132F23) else Color(0xFFE8E4DA))
                        .clickable { onReferenceClick(dua.hadithSourceEnglish.ifEmpty { dua.hadithSource }) }
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    val sourceText = if (selectedLanguage == "hi" || selectedLanguage == "hinglish") dua.hadithSource else if (dua.hadithSourceEnglish.isNotEmpty()) dua.hadithSourceEnglish else dua.hadithSource
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "📚 $sourceText",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = secondaryText
                        )
                        Text(text = "🔍 Read ↗", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = deepGold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // ARABIC TEXT (Centered & Large Tashkeel)
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

                // Transliteration
                Text(
                    text = dua.transliteration,
                    fontSize = 12.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    color = deepGold,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Translation
                Text(
                    text = meaning,
                    fontSize = 13.sp,
                    color = primaryText,
                    lineHeight = 19.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Timing note
                Text(
                    text = "⏰ $timing",
                    fontSize = 11.sp,
                    color = secondaryText
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ACTION BUTTONS (Listen Arabic, Copy, Share)
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
                    // Audio Listen Button
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = audioBtnText,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSpeaking) Color.Black else primaryText
                            )
                        }
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
private fun SawmRulesSection(
    rules: List<SawmRule>,
    cardBg: Color,
    cardBorder: Color,
    primaryText: Color,
    secondaryText: Color,
    deepGold: Color,
    isDarkTheme: Boolean,
    selectedLanguage: String,
    onReferenceClick: (String) -> Unit = {}
) {
    Text(
        text = when (selectedLanguage) {
            "hi", "hinglish" -> "रोज़े के ज़रूरी अहकाम व शरई मसाइल"
            "ur" -> "روزے کے ضروری احکام و مسائل"
            "ar" -> "أحكام الصيام ومفطراته والرخص"
            else -> "Essential Rules, Invalidation & Exemptions"
        },
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = primaryText
    )

    Spacer(modifier = Modifier.height(10.dp))

    rules.forEach { rule ->
        val categoryColor = when (rule.category) {
            "BREAKS" -> Color(0xFFD32F2F) // Red for what breaks fast
            "DOES_NOT_BREAK" -> Color(0xFF2E7D32) // Green for what does NOT break
            else -> Color(0xFFE65100) // Amber for exemptions
        }

        val categoryBadge = when (rule.category) {
            "BREAKS" -> when (selectedLanguage) {
                "hi", "hinglish" -> "❌ रोज़ा टूट जाता है"
                "ur" -> "❌ روزہ ٹوٹ جاتا ہے"
                "ar" -> "❌ مفسدات الصيام"
                else -> "❌ Invalidates Fast"
            }
            "DOES_NOT_BREAK" -> when (selectedLanguage) {
                "hi", "hinglish" -> "✅ रोज़ा नहीं टूटता (जायज़)"
                "ur" -> "✅ روزہ نہیں ٹوٹتا (جائز)"
                "ar" -> "✅ لا يفسد الصيام (مباح)"
                else -> "✅ Does Not Break Fast"
            }
            else -> when (selectedLanguage) {
                "hi", "hinglish" -> "🏷️ शरई रुख़सत (छूट)"
                "ur" -> "🏷️ شرعی رخصت (چھوٹ)"
                "ar" -> "🏷️ الرخص الشرعية"
                else -> "🏷️ Shariah Exemption"
            }
        }

        val title = when (selectedLanguage) {
            "hi", "hinglish" -> rule.titleHindi
            "ur" -> rule.titleUrdu
            else -> rule.titleEnglish
        }

        val detail = when (selectedLanguage) {
            "hi", "hinglish" -> rule.detailHindi
            else -> rule.detailEnglish
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
                            .background(categoryColor.copy(alpha = 0.15f))
                            .border(1.dp, categoryColor.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = categoryBadge,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = categoryColor
                        )
                    }

                    val ruleProofText = if (selectedLanguage == "hi" || selectedLanguage == "hinglish") rule.proof else if (rule.proofEnglish.isNotEmpty()) rule.proofEnglish else rule.proof
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isDarkTheme) Color(0xFF132F23) else Color(0xFFE8E4DA))
                            .clickable { onReferenceClick(rule.proofEnglish.ifEmpty { rule.proof }) }
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text(
                                text = "📖 $ruleProofText",
                                fontSize = 10.sp,
                                color = secondaryText,
                                fontWeight = FontWeight.Medium
                            )
                            Text(text = "↗", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = deepGold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryText
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = detail,
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    color = secondaryText
                )
            }
        }
    }
}

@Composable
private fun SawmVoluntarySection(
    voluntaryFasts: List<VoluntaryFast>,
    virtues: List<SawmHadithVirtue>,
    cardBg: Color,
    cardBorder: Color,
    primaryText: Color,
    secondaryText: Color,
    deepGold: Color,
    isDarkTheme: Boolean,
    selectedLanguage: String,
    onReferenceClick: (String) -> Unit = {}
) {
    // 1. VOLUNTARY FASTS SECTION
    Text(
        text = when (selectedLanguage) {
            "hi", "hinglish" -> "मसनून व नफ़्ली रोज़े (अज़ीम सवाब)"
            "ur" -> "مسنون و نفلی روزے (عظیم ثواب)"
            "ar" -> "صيام التطوع والسنن المؤكدة"
            else -> "Voluntary Fasts (Sunnah & Nafl)"
        },
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = primaryText
    )

    Spacer(modifier = Modifier.height(10.dp))

    voluntaryFasts.forEach { fast ->
        val title = when (selectedLanguage) {
            "hi", "hinglish" -> fast.titleHindi
            "ur" -> fast.titleUrdu
            else -> fast.titleEnglish
        }

        val timing = when (selectedLanguage) {
            "hi", "hinglish" -> fast.timingHindi
            else -> fast.timingEnglish
        }

        val reward = when (selectedLanguage) {
            "hi", "hinglish" -> fast.rewardHindi
            else -> fast.rewardEnglish
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
                    Text(
                        text = title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = deepGold
                    )
                    Text(
                        text = fast.arabicTitle,
                        fontSize = 12.sp,
                        color = primaryText,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "📅 $timing",
                    fontSize = 11.sp,
                    color = secondaryText
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "✨ $reward",
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    fontWeight = FontWeight.Medium,
                    color = primaryText
                )

                Spacer(modifier = Modifier.height(4.dp))

                val fastSource = if (selectedLanguage == "hi" || selectedLanguage == "hinglish") fast.hadithSource else if (fast.hadithSourceEnglish.isNotEmpty()) fast.hadithSourceEnglish else fast.hadithSource
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isDarkTheme) Color(0xFF132F23) else Color(0xFFE8E4DA))
                        .clickable { onReferenceClick(fast.hadithSourceEnglish.ifEmpty { fast.hadithSource }) }
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "📖 $fastSource",
                            fontSize = 10.sp,
                            color = deepGold,
                            fontWeight = FontWeight.Medium
                        )
                        Text(text = "🔍 Read ↗", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = deepGold)
                    }
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // 2. AUTHENTIC HADITHS SECTION
    Text(
        text = when (selectedLanguage) {
            "hi", "hinglish" -> "रोज़े की अज़ीम फ़ज़ीलत (सहीह अहादीस)"
            "ur" -> "روزے کی عظیم فضیلت (صحیح احادیث)"
            "ar" -> "فضائل الصيام في الأحاديث الصحيحة"
            else -> "Virtues of Fasting (Sahih Hadith)"
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
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isDarkTheme) Color(0xFF132F23) else Color(0xFFE8E4DA))
                        .clickable { onReferenceClick(v.sourceEnglish.ifEmpty { v.source }) }
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "📚 $vSource",
                            fontSize = 10.sp,
                            color = secondaryText,
                            fontWeight = FontWeight.Medium
                        )
                        Text(text = "🔍 Read ↗", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = deepGold)
                    }
                }
            }
        }
    }
}

