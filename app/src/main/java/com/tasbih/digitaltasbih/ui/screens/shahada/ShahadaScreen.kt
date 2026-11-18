package com.tasbih.digitaltasbih.ui.screens.shahada

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
import androidx.activity.compose.BackHandler
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
import com.tasbih.digitaltasbih.data.shahada.HadithVirtue
import com.tasbih.digitaltasbih.data.shahada.KalimaModel
import com.tasbih.digitaltasbih.data.shahada.ShahadaCondition
import com.tasbih.digitaltasbih.data.shahada.ShahadaData
import com.tasbih.digitaltasbih.ui.components.AppToast
import com.tasbih.digitaltasbih.ui.components.rememberAssetPainter
import com.tasbih.digitaltasbih.ui.theme.GoldAccent
import com.tasbih.digitaltasbih.data.pillars.IslamicReferenceItem
import com.tasbih.digitaltasbih.data.pillars.PillarsReferenceRepository
import com.tasbih.digitaltasbih.ui.components.IslamicReferenceModal
import java.util.Locale

@Composable
fun ShahadaScreen(
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
    var speakingKalimaId by remember { mutableIntStateOf(-1) }
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

    fun playArabicAudio(text: String, kalimaId: Int = -1) {
        if (!isTtsReady || tts == null) {
            AppToast.show("Audio initializing...")
            return
        }
        try {
            speakingKalimaId = kalimaId
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "shahada_audio")
        } catch (_: Exception) {
            AppToast.show("Audio playback unavailable")
        }
    }

    fun stopAudio() {
        tts?.stop()
        speakingKalimaId = -1
    }

    fun copyToClipboard(title: String, arabic: String, translation: String) {
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val textToCopy = "$title\n\n$arabic\n\n$translation\n\n(Digital Tasbih App)"
        cm.setPrimaryClip(ClipData.newPlainText("Kalima", textToCopy))
        AppToast.show("Copied to clipboard!")
    }

    fun shareText(title: String, arabic: String, translation: String) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT,
                    "✨ $title ✨\n\n$arabic\n\n$translation\n\n🕋 Digital Tasbih & Quran App"
                )
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share Shahada"))
        } catch (_: Exception) {
            AppToast.show("Could not share text")
        }
    }

    // Tab state: 0 = Overview & Tawheed, 1 = 6 Kalimas, 2 = 7 Conditions, 3 = Hadith & Virtues
    var selectedTab by remember { mutableIntStateOf(0) }
    val scrollState = rememberScrollState()

    // Day Mode Color Scheme (Dark Emerald Green + Warm White) vs Night Mode
    val screenBg = if (isDarkTheme) Color(0xFF07150E) else Color(0xFFF6F4EE)

    val localizedTitle = when (selectedLanguage) {
        "hi", "hinglish" -> "शहादा (Ash-Shahadah)"
        "ur" -> "کلمہ شہادت (پہلا رکن)"
        "ar" -> "الشهادتان (الركن الأول)"
        "bn" -> "শাহাদাহ (প্রথম স্তম্ভ)"
        else -> "Ash-Shahadah (Pillar 1)"
    }

    val shareHeaderTranslation = when (selectedLanguage) {
        "hi", "hinglish" -> "मैं गवाही देता हूँ कि अल्लाह के सिवा कोई सच्चा माबूद नहीं, और मैं गवाही देता हूँ कि हज़रत मुहम्मद ﷺ अल्लाह के रसूल हैं।"
        "ur" -> "میں گواہی دیتا ہوں کہ اللہ کے سوا کوئی معبود نہیں اور محمد ﷺ اللہ کے رسول ہیں۔"
        "ar" -> "أشهد أن لا إله إلا الله وأشهد أن محمداً رسول الله"
        else -> "I bear witness that there is no deity worthy of worship except Allah, and I bear witness that Muhammad is the Messenger of Allah."
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(screenBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // 1. TOP HEADER (Dark Emerald Green + Gold Border)
            ShahadaTopBar(
                title = localizedTitle,
                isDarkTheme = isDarkTheme,
                onBack = onBack,
                onShareAll = {
                    shareText(
                        localizedTitle,
                        "أَشْهَدُ أَنْ لَا إِلٰهَ إِلَّا اللهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا رَسُولُ اللهِ",
                        shareHeaderTranslation
                    )
                }
            )

            // 2. HORIZONTAL TAB SELECTOR
            ShahadaTabRow(
                selectedTab = selectedTab,
                onSelectTab = { selectedTab = it },
                isDarkTheme = isDarkTheme,
                language = selectedLanguage
            )

            // 3. SCROLLABLE TAB CONTENT
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                when (selectedTab) {
                    0 -> OverviewAndTawheedTab(
                        isDarkTheme = isDarkTheme,
                        language = selectedLanguage,
                        onPlayAudio = { playArabicAudio(it, 0) },
                        onShare = ::shareText,
                        onCopy = ::copyToClipboard,
                        onReferenceClick = { ref ->
                            activeReferenceItem = PillarsReferenceRepository.findReference(ref, "Shahadah")
                        }
                    )
                    1 -> SixKalimasTab(
                        isDarkTheme = isDarkTheme,
                        language = selectedLanguage,
                        speakingKalimaId = speakingKalimaId,
                        onPlayAudio = { txt, id -> playArabicAudio(txt, id) },
                        onStopAudio = ::stopAudio,
                        onShare = ::shareText,
                        onCopy = ::copyToClipboard,
                        onReferenceClick = { ref ->
                            activeReferenceItem = PillarsReferenceRepository.findReference(ref, "Shahadah")
                        }
                    )
                    2 -> SevenConditionsTab(
                        isDarkTheme = isDarkTheme,
                        language = selectedLanguage,
                        onReferenceClick = { ref ->
                            activeReferenceItem = PillarsReferenceRepository.findReference(ref, "Shahadah")
                        }
                    )
                    3 -> HadithVirtuesTab(
                        isDarkTheme = isDarkTheme,
                        language = selectedLanguage,
                        onReferenceClick = { ref ->
                            activeReferenceItem = PillarsReferenceRepository.findReference(ref, "Shahadah")
                        }
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))
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
}

/**
 * Top App Bar with back button, luxury emerald styling and share icon
 */
@Composable
private fun ShahadaTopBar(
    title: String,
    isDarkTheme: Boolean,
    onBack: () -> Unit,
    onShareAll: () -> Unit
) {
    val emeraldGradient = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF0A2E22),
            Color(0xFF0D4A3E),
            Color(0xFF0A2E22)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(emeraldGradient)
            .border(1.2.dp, GoldAccent.copy(alpha = 0.85f), RoundedCornerShape(16.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back Button
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF072118))
                    .border(1.dp, GoldAccent.copy(alpha = 0.5f), CircleShape)
                    .clickable(onClick = onBack),
                contentAlignment = Alignment.Center
            ) {
                Text("←", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }

            // Title
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "الإسلام — الركن الأول (Pillar 1)",
                    fontSize = 11.sp,
                    color = GoldAccent,
                    fontWeight = FontWeight.Medium
                )
            }

            // Share Header Icon
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF072118))
                    .border(1.dp, GoldAccent.copy(alpha = 0.5f), CircleShape)
                    .clickable(onClick = onShareAll),
                contentAlignment = Alignment.Center
            ) {
                Text("📤", fontSize = 16.sp)
            }
        }
    }
}

/**
 * 4 Horizontal Tabs: Overview, 6 Kalimas, 7 Conditions, Hadith
 */
@Composable
private fun ShahadaTabRow(
    selectedTab: Int,
    onSelectTab: (Int) -> Unit,
    isDarkTheme: Boolean,
    language: String
) {
    val tabs = remember(language) {
        when (language) {
            "hi", "hinglish" -> listOf(
                "पहला अरकान (Tawheed)",
                "6 कलिमे (6 Kalimas)",
                "7 शर्तें (Conditions)",
                "हदीस व फ़ज़ीलत"
            )
            "ur" -> listOf(
                "پہلا رکن (توحید)",
                "چھ کلمے (6 Kalimas)",
                "۷ شرائط (Conditions)",
                "احادیث و فضائل"
            )
            "ar" -> listOf(
                "الركن الأول (التوحيد)",
                "الكلمات الست",
                "شروط الشهادة",
                "فضائل وأحاديث"
            )
            "bn" -> listOf(
                "১ম স্তম্ভ (তাওহীদ)",
                "৬টি কালিমা",
                "৭টি শর্ত",
                "হাদিস ও ফজিলত"
            )
            else -> listOf(
                "1st Pillar (Tawheed)",
                "6 Kalimas",
                "7 Conditions",
                "Hadith & Virtues"
            )
        }
    }

    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 14.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tabs.forEachIndexed { index, title ->
            val isSelected = selectedTab == index
            val tabBg = if (isSelected) {
                Color(0xFF0D4A3E)
            } else {
                if (isDarkTheme) Color(0xFF133628) else Color(0xFFE8EDE9)
            }
            val textColor = if (isSelected) Color.White else if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF475569)
            val borderCol = if (isSelected) GoldAccent else Color.Transparent

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(tabBg)
                    .border(1.dp, borderCol, RoundedCornerShape(12.dp))
                    .clickable { onSelectTab(index) }
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = textColor
                )
            }
        }
    }
}

/**
 * TAB 1: Hero Card with 3D artwork, Arabic Calligraphy, Two Testimonies, and Quranic references
 */
@Composable
private fun OverviewAndTawheedTab(
    isDarkTheme: Boolean,
    language: String,
    onPlayAudio: (String) -> Unit,
    onShare: (String, String, String) -> Unit,
    onCopy: (String, String, String) -> Unit,
    onReferenceClick: (String) -> Unit = {}
) {
    val heroSubtitle = when (language) {
        "hi", "hinglish" -> "शहादा — इस्लाम का पहला और सबसे अज़ीम अरकान"
        "ur" -> "شہادت — اسلام کی بنیاد اور سب سے عظیم رکن"
        "ar" -> "الشهادة — الركن الأول وأعظم أركان الإسلام"
        "bn" -> "শাহাদাহ — ইসলামের প্রথম ও সর্বপ্রধান স্তম্ভ"
        else -> "Shahadah — The Foundation and Greatest Pillar of Islam"
    }

    val coreTranslation = when (language) {
        "hi", "hinglish" -> "“मैं गवाही देता हूँ कि अल्लाह के सिवा कोई सच्चा माबूद (उपास्य) नहीं, वह अकेला है उसका कोई साझीदार नहीं, और मैं गवाही देता हूँ कि हज़रत मुहम्मद ﷺ उसके सच्चे बंदे और रसूल हैं।”"
        "ur" -> "”میں گواہی دیتا ہوں کہ اللہ کے سوا کوئی معبود نہیں، وہ یکتا ہے، اس کا کوئی شریک نہیں، اور میں گواہی دیتا ہوں کہ محمد ﷺ اس کے بندے اور رسول ہیں۔“"
        "ar" -> "«أشهد أن لا إله إلا الله وحده لا شريك له وأشهد أن محمداً عبده ورسوله»"
        "bn" -> "“আমি সাক্ষ্য দিচ্ছি যে আল্লাহ ছাড়া কোনো সত্য উপাস্য নেই, তিনি একক, তাঁর কোনো অংশীদার নেই; এবং আমি সাক্ষ্য দিচ্ছি যে হযরত মুহাম্মদ ﷺ তাঁর বান্দা ও রাসূল।”"
        else -> "“I bear witness that there is no deity worthy of worship except Allah, alone without partner, and I bear witness that Muhammad ﷺ is His servant and Messenger.”"
    }

    val listenLabel = when (language) {
        "hi", "hinglish" -> "🔊 सुनें (Listen)"
        "ur" -> "🔊 سنیں"
        "ar" -> "🔊 استمع"
        else -> "🔊 Listen"
    }
    val copyLabel = when (language) {
        "hi", "hinglish" -> "📋 कॉपी"
        "ur" -> "📋 کاپی"
        "ar" -> "📋 نسخ"
        else -> "📋 Copy"
    }
    val shareLabel = when (language) {
        "hi", "hinglish" -> "📲 शेयर"
        "ur" -> "📲 شیئر"
        "ar" -> "📲 مشاركة"
        else -> "📲 Share"
    }

    val testimoniesHeader = when (language) {
        "hi", "hinglish" -> "दो मुक़द्दस गवाहियों की तफ़सील (The 2 Testimonies)"
        "ur" -> "دو مقدس شہادتوں کی تفصیل (The 2 Testimonies)"
        "ar" -> "تفصيل الشهادتين (The 2 Testimonies)"
        "bn" -> "দুটি পবিত্র সাক্ষ্যের বিবরণ (The 2 Testimonies)"
        else -> "Details of the Two Sacred Testimonies"
    }

    val t1Title = when (language) {
        "hi", "hinglish" -> "गवाही 1: तौहीद (अल्लाह का एक होना)"
        "ur" -> "پہلی گواہی: توحید (اللہ کی وحدانیت)"
        "ar" -> "الشهادة الأولى: التوحيد (لا إله إلا الله)"
        "bn" -> "সাক্ষ্য ১: তাওহীদ (আল্লাহর একত্ববাদ)"
        else -> "Testimony 1: Tawheed (The Oneness of Allah)"
    }
    val t1Desc = when (language) {
        "hi", "hinglish" -> "• 'ला इलाहा' (नफ़ी): दुनिया के हर झूठे माबूद, बुत, नफ़्स की ख्वाहिश और इंसान द्वारा गढ़े गए पूज्यों का पूर्ण इनकार।\n• 'इल्लल्लाह' (इसबात): सिर्फ और सिर्फ एक सच्चे ख़ालिक़ व मालिक 'अल्लाह' की वहदानियत और इबादत का इक़रार।"
        "ur" -> "• 'لا الہ' (نفی): دنیا کے ہر باطل معبود، بت، نفسانی خواہشات اور خود ساختہ معبودوں کا قطعی انکار۔\n• 'الا اللہ' (اثبات): صرف اور صرف ایک سچے خالق و مالک 'اللہ' کی وحدانیت اور عبادت کا اقرار۔"
        "ar" -> "• «لا إله» (نفي): نفي الألوهية والعبادة عن كل ما سوى الله من الأصنام والأهواء والآلهة الباطلة.\n• «إلا الله» (إثبات): إثبات العبادة الحقة لله وحده لا شريك له في ملكه وخلقه."
        "bn" -> "• 'লা ইলাহা' (নফি): দুনিয়ার সকল মিথ্যা উপাস্য, মূর্তি ও নফসের দাসত্বের পূর্ণ অস্বীকৃতি।\n• 'ইল্লাল্লাহ' (ইসবাত): একমাত্র সত্য স্রষ্টা মহান আল্লাহর একত্ববাদ ও ইবাদতের আন্তরিক স্বীকার।"
        else -> "• 'La Ilaha' (Negation): Complete rejection of all false deities, idols, egoistic desires, and human-fabricated objects of worship.\n• 'Ill-Allah' (Affirmation): Sincere affirmation of the absolute Oneness, Lordship, and worship of Allah alone."
    }
    val t1Ref = when (language) {
        "hi", "hinglish" -> "सूरह मुहम्मद (47:19) — “तो जान लो कि अल्लाह के सिवा कोई सच्चा माबूद नहीं”"
        "ur" -> "سورۃ محمد (۴۷:۱۹) — ”جان لیں کہ اللہ کے سوا کوئی سچا معبود نہیں“"
        "ar" -> "سورة محمد (٤٧:١٩) — «فَاعْلَمْ أَنَّهُ لَا إِلَٰهَ إِلَّا اللَّهُ»"
        "bn" -> "সূরা মুহাম্মদ (৪৭:১৯) — “অতএব জেনে রাখুন, আল্লাহ ছাড়া কোনো সত্য উপাস্য নেই”"
        else -> "Surah Muhammad (47:19) — “So know that there is no deity worthy of worship except Allah”"
    }

    val t2Title = when (language) {
        "hi", "hinglish" -> "गवाही 2: रिसालत (हज़रत मुहम्मद ﷺ का रसूल होना)"
        "ur" -> "دوسری گواہی: رسالت (حضرت محمد ﷺ کی رسالت)"
        "ar" -> "الشهادة الثانية: الرسالة (محمد رسول الله)"
        "bn" -> "সাক্ষ্য ২: রিসালাত (হযরত মুহাম্মদ ﷺ আল্লাহর রাসূল)"
        else -> "Testimony 2: Risalat (Prophethood of Muhammad ﷺ)"
    }
    val t2Desc = when (language) {
        "hi", "hinglish" -> "• नबी करीम हज़रत मुहम्मद मुस्तफ़ा ﷺ को अल्लाह का सच्चा बंदा और आख़िरी नबी (ख़ातम-उन-नबी) दिल से स्वीकार करना।\n• उनकी लाई हुई शरीअत और सुन्नत को जीवन का एकमात्र आदर्श मानकर उनकी पैरवी करना।"
        "ur" -> "• نبی کریم حضرت محمد مصطفیٰ ﷺ کو اللہ کا سچا بندہ اور آخری نبی (خاتم النبیین) دل و جان سے تسلیم کرنا۔\n• آپ ﷺ کی لائی ہوئی شریعت اور سنت کو زندگی کا کامل اور حتمی نمونہ مان کر اس کی پیروی کرنا۔"
        "ar" -> "• الإقرار الجازم بأن محمداً ﷺ عبد الله ورسوله وخاتم الأنبياء والمرسلين.\n• طاعته فيما أمر، وتصديقه فيما أخبر، واجتناب ما نهى عنه وزجر."
        "bn" -> "• প্রিয় নবী হযরত মুহাম্মদ ﷺ-কে আল্লাহর সত্য বান্দা ও সর্বশেষ নবী (খাতামুন্নাবিয়্যীন) হিসেবে মনে-প্রাণে স্বীকার করা।\n• তাঁর আনীত শরীয়ত ও সুন্নাহকে জীবনের একমাত্র আদর্শ হিসেবে অনুসরণ করা।"
        else -> "• Wholeheartedly acknowledging Prophet Muhammad ﷺ as the noble servant and Final Messenger of Allah (Khatam an-Nabiyyin).\n• Following his divine Shariah and Sunnah as the supreme life exemplar and guide."
    }
    val t2Ref = when (language) {
        "hi", "hinglish" -> "सूरह अल-फ़तह (48:29) — “मुहम्मद अल्लाह के रसूल हैं”\nसूरह अल-अहज़ाब (33:40) — “और वे तमाम नबियों में आख़िरी (ख़ातम-उन-नबी) हैं”"
        "ur" -> "سورۃ الفتح (۴۸:۲۹) — ”محمد اللہ کے رسول ہیں“\nسورۃ الاحزاب (۳۳:۴۰) — ”اور وہ تمام نبیوں کے خاتم ہیں“"
        "ar" -> "سورة الفتح (٤٨:٢٩) — «مُّحَمَّدٌ رَّسُولُ اللَّهِ»\nسورة الأحزاب (٣٣:٤٠) — «وَلَٰكِن رَّسُولَ اللَّهِ وَخَاتَمَ النَّبِيِّينَ»"
        "bn" -> "সূরা আল-ফাতহ (৪৮:২৯) — “মুহাম্মদ আল্লাহর রাসূল”\nসূরা আল-আহযাব (৩৩:৪০) — “এবং তিনি সর্বশেষ নবী”"
        else -> "Surah Al-Fath (48:29) — “Muhammad is the Messenger of Allah”\nSurah Al-Ahzab (33:40) — “And he is the Seal of the Prophets”"
    }

    // 1. HERO CARD
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(if (isDarkTheme) 0.dp else 4.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF092E22),
                        Color(0xFF0D4A3E),
                        Color(0xFF072118)
                    )
                )
            )
            .border(1.2.dp, GoldAccent, RoundedCornerShape(18.dp))
            .padding(18.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val heroPainter = rememberAssetPainter("media/shahada_hero.webp")
            Image(
                painter = heroPainter,
                contentDescription = "Shahada 3D",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(130.dp)
                    .shadow(12.dp, CircleShape, spotColor = GoldAccent)
                    .clip(RoundedCornerShape(16.dp))
            )

            Text(
                text = "الرُّكْنُ الأَوَّلُ مِنَ الإِسْلَامِ",
                fontSize = 13.sp,
                color = GoldAccent,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = heroSubtitle,
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            // Primary Sacred Arabic Calligraphy
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF061D15))
                    .border(1.dp, GoldAccent.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                    .padding(14.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "أَشْهَدُ أَنْ لَا إِلٰهَ إِلَّا اللهُ\nوَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFE082),
                        textAlign = TextAlign.Center,
                        lineHeight = 32.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Ash-hadu an laa ilaaha ill-Allahu wahdahu laa shareeka lahu wa ash-hadu anna Muhammadan 'abduhu wa Rasooluh",
                        fontSize = 11.sp,
                        color = Color(0xFFE2E8F0),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Translation in selected language
            Text(
                text = coreTranslation,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.95f),
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )

            // Audio & Action Buttons
            Row(
                modifier = Modifier.padding(top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Audio Button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF135A43))
                        .border(1.dp, GoldAccent, RoundedCornerShape(12.dp))
                        .clickable {
                            onPlayAudio("أَشْهَدُ أَنْ لَا إِلٰهَ إِلَّا اللهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ")
                        }
                        .padding(horizontal = 14.dp, vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(listenLabel, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                // Copy
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF135A43))
                        .border(1.dp, GoldAccent.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .clickable {
                            onCopy(
                                "Shahada Testimony",
                                "أَشْهَدُ أَنْ لَا إِلٰهَ إِلَّا اللهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ",
                                coreTranslation
                            )
                        }
                        .padding(horizontal = 14.dp, vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(copyLabel, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                // Share
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF135A43))
                        .border(1.dp, GoldAccent.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .clickable {
                            onShare(
                                "Shahada Testimony",
                                "أَشْهَدُ أَنْ لَا إِلٰهَ إِلَّا اللهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَरَسُولُهُ",
                                coreTranslation
                            )
                        }
                        .padding(horizontal = 14.dp, vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(shareLabel, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }

    // 2. THE TWO TESTIMONIES
    SectionTitle(title = testimoniesHeader, isDarkTheme = isDarkTheme)

    // Testimony 1: Tawheed
    TestimonyCard(
        badgeNumber = "1",
        title = t1Title,
        arabicKey = "لَا إِلٰهَ إِلَّا اللهُ",
        desc = t1Desc,
        quranAyat = "فَاعْلَمْ أَنَّهُ لَا إِلَٰهَ إِلَّا اللَّهُ وَاسْتَغْفِرْ لِذَنْبِكَ",
        quranRef = t1Ref,
        isDarkTheme = isDarkTheme,
        onReferenceClick = onReferenceClick
    )

    // Testimony 2: Risalat
    TestimonyCard(
        badgeNumber = "2",
        title = t2Title,
        arabicKey = "مُحَمَّدٌ رَسُولُ اللهِ",
        desc = t2Desc,
        quranAyat = "مُحَمَّدٌ رَسُولُ اللَّهِ ۚ وَالَّذِينَ مَعَهُ أَشِدَّاءُ عَلَى الْكُفَّارِ رُحَمَاءُ بَيْنَهُمْ",
        quranRef = t2Ref,
        isDarkTheme = isDarkTheme,
        onReferenceClick = onReferenceClick
    )
}

/**
 * Single Testimony card component with Arabic Ayat and translation
 */
@Composable
private fun TestimonyCard(
    badgeNumber: String,
    title: String,
    arabicKey: String,
    desc: String,
    quranAyat: String,
    quranRef: String,
    isDarkTheme: Boolean,
    onReferenceClick: (String) -> Unit = {}
) {
    val cardBg = if (isDarkTheme) Color(0xFF0D2D20) else Color(0xFFFFFFFF)
    val titleColor = if (isDarkTheme) Color.White else Color(0xFF0D382B)
    val bodyColor = if (isDarkTheme) Color(0xFFE2E8F0) else Color(0xFF334155)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(if (isDarkTheme) 0.dp else 3.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(cardBg)
            .border(
                1.2.dp,
                if (isDarkTheme) GoldAccent.copy(alpha = 0.4f) else Color(0xFF0D4A3E),
                RoundedCornerShape(16.dp)
            )
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // Header Row: Badge + Title + Arabic Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0D4A3E))
                            .border(1.dp, GoldAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(badgeNumber, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Text(
                        text = title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = titleColor
                    )
                }
            }

            // Arabic Main Phrase
            Text(
                text = arabicKey,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDarkTheme) Color(0xFFFFD54F) else Color(0xFF0D4A3E),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            // Meaning / Explanation Points
            Text(
                text = desc,
                fontSize = 12.sp,
                color = bodyColor,
                lineHeight = 18.sp
            )

            // Quran Ayat Box (Clickable with Authentic Hadith/Ayah modal)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isDarkTheme) Color(0xFF072118) else Color(0xFFF1F5F9))
                    .border(0.8.dp, GoldAccent.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                    .clickable { onReferenceClick(quranRef) }
                    .padding(10.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "📖 $quranAyat",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color(0xFFFFE082) else Color(0xFF0F766E),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = quranRef,
                            fontSize = 11.sp,
                            color = if (isDarkTheme) Color(0xFFCBD5E1) else Color(0xFF475569),
                            modifier = Modifier.weight(1f)
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
        }
    }
}

/**
 * TAB 2: The 6 Kalimas of Islam with authentic Hadith references, Arabic audio, and translation
 */
@Composable
private fun SixKalimasTab(
    isDarkTheme: Boolean,
    language: String,
    speakingKalimaId: Int,
    onPlayAudio: (String, Int) -> Unit,
    onStopAudio: () -> Unit,
    onShare: (String, String, String) -> Unit,
    onCopy: (String, String, String) -> Unit,
    onReferenceClick: (String) -> Unit = {}
) {
    val sectionHeader = when (language) {
        "hi", "hinglish" -> "इस्लाम के 6 मुक़द्दस कलिमे (The 6 Kalimas)"
        "ur" -> "اسلام کے ۶ مقدس کلمے (The 6 Kalimas)"
        "ar" -> "الكلمات الست في الإسلام"
        "bn" -> "ইসলামের পবিত্র ৬টি কালিমা (The 6 Kalimas)"
        else -> "The 6 Sacred Kalimas of Islam"
    }

    SectionTitle(title = sectionHeader, isDarkTheme = isDarkTheme)

    ShahadaData.KALIMAS.forEach { kalima ->
        val kalimaTitle = when (language) {
            "hi", "hinglish" -> kalima.titleHindi
            "ur" -> kalima.titleUrdu
            else -> kalima.titleEnglish
        }
        val kalimaTranslation = when (language) {
            "hi", "hinglish" -> kalima.translationHindi
            "ur" -> kalima.translationUrdu
            else -> kalima.translationEnglish
        }

        KalimaCard(
            kalima = kalima,
            isDarkTheme = isDarkTheme,
            language = language,
            isPlaying = speakingKalimaId == kalima.id,
            onPlay = { onPlayAudio(kalima.arabicText, kalima.id) },
            onStop = onStopAudio,
            onShare = { onShare(kalimaTitle, kalima.arabicText, kalimaTranslation) },
            onCopy = { onCopy(kalimaTitle, kalima.arabicText, kalimaTranslation) },
            onReferenceClick = onReferenceClick
        )
    }
}

/**
 * Single Kalima Card with full rich details, authentic Hadith reference, and actions
 */
@Composable
private fun KalimaCard(
    kalima: KalimaModel,
    isDarkTheme: Boolean,
    language: String,
    isPlaying: Boolean,
    onPlay: () -> Unit,
    onStop: () -> Unit,
    onShare: () -> Unit,
    onCopy: () -> Unit,
    onReferenceClick: (String) -> Unit = {}
) {
    val cardBg = if (isDarkTheme) Color(0xFF0D2D20) else Color(0xFFFFFFFF)
    val titleColor = if (isDarkTheme) Color.White else Color(0xFF0D382B)
    val bodyColor = if (isDarkTheme) Color(0xFFE2E8F0) else Color(0xFF334155)

    val localizedCardTitle = when (language) {
        "hi", "hinglish" -> kalima.titleHindi
        "ur" -> kalima.titleUrdu
        else -> kalima.titleEnglish
    }

    val localizedSubtitle = when (language) {
        "hi", "hinglish" -> kalima.subtitle
        else -> kalima.subtitleEnglish
    }

    val localizedTranslation = when (language) {
        "hi", "hinglish" -> "तर्जुमा: ${kalima.translationHindi}"
        "ur" -> "ترجمہ: ${kalima.translationUrdu}"
        "bn" -> "অনুবাদ: ${kalima.translationEnglish}"
        else -> "Translation: ${kalima.translationEnglish}"
    }

    val localizedVirtue = when (language) {
        "hi", "hinglish" -> "💡 फ़ज़ीलत: ${kalima.virtueHindi}"
        "ur" -> "💡 فضیلت: ${kalima.virtueUrdu}"
        else -> "💡 Virtue: ${kalima.virtueEnglish}"
    }

    val listenLabel = when (language) {
        "hi", "hinglish" -> if (isPlaying) "⏹ रोकें" else "🔊 सुनें"
        "ur" -> if (isPlaying) "⏹ روکیں" else "🔊 سنیں"
        else -> if (isPlaying) "⏹ Stop" else "🔊 Listen"
    }

    val copyLabel = when (language) {
        "hi", "hinglish" -> "📋 कॉपी"
        "ur" -> "📋 کاپی"
        else -> "📋 Copy"
    }

    val shareLabel = when (language) {
        "hi", "hinglish" -> "📲 शेयर"
        "ur" -> "📲 شیئر"
        else -> "📲 Share"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(if (isDarkTheme) 0.dp else 3.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(cardBg)
            .border(
                1.2.dp,
                if (isDarkTheme) GoldAccent.copy(alpha = 0.4f) else Color(0xFF0D4A3E),
                RoundedCornerShape(16.dp)
            )
            .animateContentSize()
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // Header Row: Number + Title + Arabic Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0D4A3E))
                            .border(1.dp, GoldAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("${kalima.id}", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    Column {
                        Text(
                            text = localizedCardTitle,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = titleColor
                        )
                        Text(
                            text = localizedSubtitle,
                            fontSize = 10.sp,
                            color = if (isDarkTheme) GoldAccent else Color(0xFFB45309)
                        )
                    }
                }

                Text(
                    text = kalima.arabicTitle,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) GoldAccent else Color(0xFF0D4A3E)
                )
            }

            // Arabic Text Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isDarkTheme) Color(0xFF081F16) else Color(0xFFF8FAFC))
                    .border(1.dp, if (isDarkTheme) Color(0x30D4AF37) else Color(0xFFCBD5E1), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = kalima.arabicText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) Color(0xFFFFD54F) else Color(0xFF0D4A3E),
                    textAlign = TextAlign.Center,
                    lineHeight = 30.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Transliteration
            Text(
                text = "🔤 ${kalima.transliteration}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B),
                lineHeight = 16.sp
            )

            // Translation
            Text(
                text = localizedTranslation,
                fontSize = 12.sp,
                color = bodyColor,
                lineHeight = 18.sp
            )

            // Authentic Hadith Reference Badge (Clickable)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isDarkTheme) Color(0xFF072118) else Color(0xFFEEF2F6))
                    .border(0.8.dp, GoldAccent.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                    .clickable { onReferenceClick(kalima.hadithReference) }
                    .padding(horizontal = 9.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📚 Reference: ${kalima.hadithReference}",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isDarkTheme) GoldAccent else Color(0xFF0F766E),
                    lineHeight = 15.sp,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isDarkTheme) Color(0xFF133E2B) else Color(0xFFFEF3C7))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "🔍 View Hadith ↗",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) GoldAccent else Color(0xFF92400E)
                    )
                }
            }

            // Virtue Highlight Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isDarkTheme) Color(0xFF1B3828) else Color(0xFFFEF3C7))
                    .border(0.8.dp, GoldAccent.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = localizedVirtue,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isDarkTheme) GoldAccent else Color(0xFF92400E),
                    lineHeight = 15.sp
                )
            }

            // Quick Actions: Play / Stop, Copy, Share
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Audio Play / Stop
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isPlaying) Color(0xFFDC2626) else Color(0xFF0D4A3E))
                        .clickable {
                            if (isPlaying) onStop() else onPlay()
                        }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = listenLabel,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Copy
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isDarkTheme) Color(0xFF163E2E) else Color(0xFFE2E8F0))
                        .clickable(onClick = onCopy)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(copyLabel, fontSize = 11.sp, color = titleColor, fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Share
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isDarkTheme) Color(0xFF163E2E) else Color(0xFFE2E8F0))
                        .clickable(onClick = onShare)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(shareLabel, fontSize = 11.sp, color = titleColor, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

/**
 * TAB 3: 7 Necessary Conditions of Shahadah
 */
@Composable
private fun SevenConditionsTab(
    isDarkTheme: Boolean,
    language: String,
    onReferenceClick: (String) -> Unit = {}
) {
    val sectionHeader = when (language) {
        "hi", "hinglish" -> "शहादा की 7 शर्तें (7 Conditions of Shahadah)"
        "ur" -> "شہادت کی ۷ شرائط (7 Conditions of Shahadah)"
        "ar" -> "شروط لا إله إلا الله السبعة"
        "bn" -> "শাহাদাহ্-র ৭টি অপরিহার্য শর্ত (Conditions)"
        else -> "The 7 Necessary Conditions of Shahadah"
    }

    val introText = when (language) {
        "hi", "hinglish" -> "उलमा-ए-किराम फ़रमाते हैं: 'ला इलाहा इल्लल्लाह' जन्नत की चाबी है, और हर चाबी के दंदाने (teeth) होते हैं। यदि दंदाने सही होंगे तभी दरवाज़ा खुलेगा। ये 7 शर्तें इस कलिमे के वही दंदाने हैं:"
        "ur" -> "علماء کرام فرماتے ہیں: 'لا الہ الا اللہ' جنت کی چابی ہے، اور ہر چابی کے دندانے ہوتے ہیں۔ یہ سات شرائط اس چابی کے وہی دندانے ہیں جن کے بغیر جنت کا دروازہ نہیں کھلے گا:"
        "ar" -> "قال السلف الصالح: «لا إله إلا الله مفتاح الجنة، ولكل مفتاح أسنان، فإن جئت بمفتاح له أسنان فُتح لك، وإلا لم يُفتح لك». وهذه الشروط هي أسنان المفتاح:"
        "bn" -> "বিদ্বানগণ বলেন: 'লা ইলাহা ইল্লাল্লাহ' হলো জান্নাতের চাবি, আর প্রতিটি চাবিরই দাঁত থাকে। এই ৭টি শর্ত হলো সেই চাবির দাঁত, যা ছাড়া জান্নাতের দরজা খুলবে না:"
        else -> "Classical Islamic scholars explain: 'La ilaha ill-Allah' is the key to Paradise, and every key must have teeth. Only with the proper teeth will the door unlock. These 7 conditions are the teeth of that sacred key:"
    }

    SectionTitle(title = sectionHeader, isDarkTheme = isDarkTheme)

    // Introduction Card
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isDarkTheme) Color(0xFF0F3A2B) else Color(0xFFEFF6FF))
            .border(
                1.dp,
                if (isDarkTheme) GoldAccent.copy(alpha = 0.5f) else Color(0xFFBFDBFE),
                RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
    ) {
        Text(
            text = introText,
            fontSize = 12.sp,
            color = if (isDarkTheme) Color(0xFFE2E8F0) else Color(0xFF334155),
            lineHeight = 18.sp
        )
    }

    ShahadaData.CONDITIONS.forEach { condition ->
        ConditionCard(
            condition = condition,
            language = language,
            isDarkTheme = isDarkTheme,
            onReferenceClick = onReferenceClick
        )
    }
}

@Composable
private fun ConditionCard(
    condition: ShahadaCondition,
    language: String,
    isDarkTheme: Boolean,
    onReferenceClick: (String) -> Unit = {}
) {
    val cardBg = if (isDarkTheme) Color(0xFF0D2D20) else Color(0xFFFFFFFF)
    val titleColor = if (isDarkTheme) Color.White else Color(0xFF0D382B)
    val bodyColor = if (isDarkTheme) Color(0xFFE2E8F0) else Color(0xFF334155)

    val localizedTitle = when (language) {
        "hi", "hinglish" -> condition.titleHindi
        "ur" -> condition.titleUrdu
        else -> condition.titleEnglish
    }

    val localizedDesc = when (language) {
        "hi", "hinglish" -> condition.descHindi
        "ur" -> condition.descUrdu
        else -> condition.descEnglish
    }

    val localizedRef = when (language) {
        "hi", "hinglish" -> condition.quranOrHadithRefHindi
        "ur" -> condition.quranOrHadithRefUrdu
        else -> condition.quranOrHadithRefEnglish
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(if (isDarkTheme) 0.dp else 3.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(cardBg)
            .border(
                1.2.dp,
                if (isDarkTheme) GoldAccent.copy(alpha = 0.4f) else Color(0xFF0D4A3E),
                RoundedCornerShape(16.dp)
            )
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0D4A3E))
                            .border(1.dp, GoldAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("${condition.number}", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Text(
                        text = localizedTitle,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = titleColor
                    )
                }

                Text(
                    text = condition.arabicName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) GoldAccent else Color(0xFF0D4A3E)
                )
            }

            Text(
                text = localizedDesc,
                fontSize = 12.sp,
                color = bodyColor,
                lineHeight = 17.sp
            )

            // Quran / Hadith Reference (Clickable with Modal & Sunnah.com / Quran.com redirect)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isDarkTheme) Color(0xFF072118) else Color(0xFFF1F5F9))
                    .clickable { onReferenceClick(localizedRef) }
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f, fill = false),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "📜", fontSize = 11.sp)
                    Text(
                        text = localizedRef,
                        fontSize = 10.5.sp,
                        color = if (isDarkTheme) GoldAccent else Color(0xFF0F766E),
                        lineHeight = 14.sp
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isDarkTheme) Color(0xFF133E2B) else Color(0xFFDCFCE7))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "🔍 Read ↗",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) GoldAccent else Color(0xFF059669)
                    )
                }
            }
        }
    }
}

/**
 * TAB 4: Authentic Hadith on Virtues of Shahada & Tawheed
 */
@Composable
private fun HadithVirtuesTab(
    isDarkTheme: Boolean,
    language: String,
    onReferenceClick: (String) -> Unit = {}
) {
    val sectionHeader = when (language) {
        "hi", "hinglish" -> "सहीह हदीस संदर्भ व फ़ज़ीलत (Sahih Hadith References)"
        "ur" -> "کلمہ شہادت کی فضیلت پر صحیح احادیث"
        "ar" -> "أحاديث صحيحة في فضل الشهادة والتوحيد"
        "bn" -> "শাহাদাহ্-র ফজিলত সম্পর্কিত সহীহ হাদিস"
        else -> "Authentic Hadith on the Virtues of Shahadah"
    }

    SectionTitle(title = sectionHeader, isDarkTheme = isDarkTheme)

    ShahadaData.VIRTUES.forEach { virtue ->
        VirtueCard(
            virtue = virtue,
            language = language,
            isDarkTheme = isDarkTheme,
            onReferenceClick = onReferenceClick
        )
    }
}

@Composable
private fun VirtueCard(
    virtue: HadithVirtue,
    language: String,
    isDarkTheme: Boolean,
    onReferenceClick: (String) -> Unit = {}
) {
    val cardBg = if (isDarkTheme) Color(0xFF0D2D20) else Color(0xFFFFFFFF)
    val bodyColor = if (isDarkTheme) Color(0xFFE2E8F0) else Color(0xFF334155)

    val localizedSource = when (language) {
        "hi", "hinglish" -> virtue.sourceHindi
        "ur" -> virtue.sourceUrdu
        else -> virtue.sourceEnglish
    }

    val localizedTranslation = when (language) {
        "hi", "hinglish" -> "तर्जुमा: “${virtue.translationHindi}”"
        "ur" -> "ترجمہ: ”${virtue.translationUrdu}“"
        else -> "Translation: “${virtue.translationEnglish}”"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(if (isDarkTheme) 0.dp else 3.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(cardBg)
            .border(
                1.2.dp,
                if (isDarkTheme) GoldAccent.copy(alpha = 0.4f) else Color(0xFF0D4A3E),
                RoundedCornerShape(16.dp)
            )
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // Source Tag (Clickable)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isDarkTheme) Color(0xFF133628) else Color(0xFFE0E7FF))
                    .clickable { onReferenceClick(localizedSource) }
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📚 $localizedSource",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) GoldAccent else Color(0xFF3730A3),
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isDarkTheme) Color(0xFF1B4D36) else Color(0xFFC7D2FE))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "🔍 View Hadith ↗",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) GoldAccent else Color(0xFF3730A3)
                    )
                }
            }

            // Arabic Hadith
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isDarkTheme) Color(0xFF081F16) else Color(0xFFF8FAFC))
                    .border(1.dp, if (isDarkTheme) Color(0x30D4AF37) else Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
                    .padding(10.dp)
            ) {
                Text(
                    text = virtue.arabicQuote,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) Color(0xFFFFD54F) else Color(0xFF0D4A3E),
                    textAlign = TextAlign.Center,
                    lineHeight = 26.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Translation
            Text(
                text = localizedTranslation,
                fontSize = 12.sp,
                color = bodyColor,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String, isDarkTheme: Boolean) {
    Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = if (isDarkTheme) Color.White else Color(0xFF0D382B),
        modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
    )
}
