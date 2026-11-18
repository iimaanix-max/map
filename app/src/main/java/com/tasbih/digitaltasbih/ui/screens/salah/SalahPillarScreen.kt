package com.tasbih.digitaltasbih.ui.screens.salah

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
import com.tasbih.digitaltasbih.data.salah.PrayerRakatsInfo
import com.tasbih.digitaltasbih.data.salah.SalahData
import com.tasbih.digitaltasbih.data.salah.SalahHadithVirtue
import com.tasbih.digitaltasbih.data.salah.SalahPillarCondition
import com.tasbih.digitaltasbih.data.salah.SalahStep
import com.tasbih.digitaltasbih.ui.components.AppToast
import com.tasbih.digitaltasbih.ui.components.rememberAssetPainter
import com.tasbih.digitaltasbih.data.pillars.IslamicReferenceItem
import com.tasbih.digitaltasbih.data.pillars.PillarsReferenceRepository
import com.tasbih.digitaltasbih.ui.components.IslamicReferenceModal

import com.tasbih.digitaltasbih.ui.theme.GoldAccent
import java.util.Locale

@Composable
fun SalahPillarScreen(
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
    var speakingStepId by remember { mutableIntStateOf(-1) }

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

    fun playArabicAudio(text: String, stepId: Int = -1) {
        if (!isTtsReady || tts == null) {
            AppToast.show("Audio initializing...")
            return
        }
        try {
            speakingStepId = stepId
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "salah_audio")
        } catch (_: Exception) {
            AppToast.show("Audio playback unavailable")
        }
    }

    fun stopAudio() {
        tts?.stop()
        speakingStepId = -1
    }

    fun copyToClipboard(title: String, arabic: String, translation: String) {
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val textToCopy = "$title\n\n$arabic\n\n$translation\n\n(Digital Tasbih App)"
        cm.setPrimaryClip(ClipData.newPlainText("Salah", textToCopy))
        AppToast.show("Copied to clipboard!")
    }

    fun shareText(title: String, arabic: String, translation: String) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT,
                    "🕌 $title 🕌\n\n$arabic\n\n$translation\n\n📱 Digital Tasbih & Quran App"
                )
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share Salah Guide"))
        } catch (_: Exception) {
            AppToast.show("Could not share text")
        }
    }

    // Tab state: 0 = तारुफ़ (Overview), 1 = मुकम्मल तरीक़ा (Step-by-step), 2 = 5 वक़्त की रकअतें (Rak'ahs), 3 = शराइत व अरकान (Conditions/Pillars), 4 = सहीह अहादीस (Virtues)
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

    val heroPainter = rememberAssetPainter("media/salah_hero.webp")

    val tabs = when (selectedLanguage) {
        "hi", "hinglish" -> listOf("दूसरा अरकान (Salah)", "नमाज़ का तरीक़ा", "5 वक़्त की रकअतें", "शराइत व अरकान", "हदीस व फ़ज़ीलत")
        "ur" -> listOf("دوسرا رکن (صلوٰۃ)", "نماز کا طریقہ", "۵ وقت کی رکعتیں", "شرائط و ارکان", "حدیث و فضیلت")
        "ar" -> listOf("الركن الثاني (الصلاة)", "صفة الصلاة", "ركعات الصلوات الخمس", "الشروط والأركان", "فضائل وأحاديث")
        "bn" -> listOf("২য় স্তম্ভ (নামাজ)", "নামাজের নিয়ম", "৫ ওয়াক্তের রাকাত", "শর্ত ও রুকন", "হাদিস ও ফজিলত")
        else -> listOf("2nd Pillar (Salah)", "Step-by-Step Guide", "5 Daily Rak'ahs", "Conditions & Pillars", "Hadith & Virtues")
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
                        "hi", "hinglish" -> "सलाह (As-Salah)"
                        "ur" -> "نماز (الصلوٰۃ)"
                        "ar" -> "الصَّلَاةُ"
                        "bn" -> "সালাত (নামাজ)"
                        else -> "As-Salah (Prayer)"
                    },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryText
                )
                Text(
                    text = when (selectedLanguage) {
                        "hi", "hinglish" -> "इस्लाम का दूसरा अरकान — الرُّكْنُ الثَّانِي"
                        "ur" -> "اسلام کا دوسرا رکن — الرُّكْنُ الثَّانِي"
                        "ar" -> "الرُّكْنُ الثَّانِي مِنَ الإِسْلَامِ"
                        "bn" -> "ইসলামের দ্বিতীয় স্তম্ভ — الركن الثاني"
                        else -> "The 2nd Pillar of Islam — الركن الثاني"
                    },
                    fontSize = 11.sp,
                    color = deepGold,
                    fontWeight = FontWeight.Medium
                )
            }

            // Share Header Action
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (isDarkTheme) Color(0xFF133224) else Color(0xFFEAE5D9))
                    .clickable {
                        shareText(
                            "As-Salah (The 2nd Pillar of Islam)",
                            "الصَّلَاةُ عِمَادُ الدِّينِ",
                            "Salah is the pillar of religion; establishing it brings success in Dunya and Akhirah."
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "📤", fontSize = 18.sp)
            }
        }

        // TABS NAVIGATION (Scrollable Row)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tabs.forEachIndexed { index, title ->
                val isSelected = selectedTab == index
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (isSelected) {
                                Brush.horizontalGradient(listOf(Color(0xFF0D4A3E), Color(0xFF176856)))
                            } else {
                                if (isDarkTheme) Brush.horizontalGradient(listOf(Color(0xFF112B1F), Color(0xFF112B1F)))
                                else Brush.horizontalGradient(listOf(Color(0xFFEFEBE1), Color(0xFFEFEBE1)))
                            }
                        )
                        .border(
                            width = 1.dp,
                            color = if (isSelected) GoldAccent else cardBorder,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable {
                            selectedTab = index
                            stopAudio()
                        }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = title,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else secondaryText
                    )
                }
            }
        }

        // SCROLLABLE TAB CONTENT
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // ══════════════════════════════════════════════════════════
            // TAB 0: OVERVIEW & THE 2ND PILLAR
            // ══════════════════════════════════════════════════════════
            if (selectedTab == 0) {
                // 3D Luxury Hero Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF082D25), Color(0xFF0D4A3E), Color(0xFF051D18))
                            )
                        )
                        .border(1.5.dp, GoldAccent, RoundedCornerShape(20.dp))
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // 3D Artwork
                        Image(
                            painter = heroPainter,
                            contentDescription = "Salah 3D Art",
                            modifier = Modifier
                                .size(175.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Fit
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "الرُّكْنُ الثَّانِي مِنَ الإِسْلَامِ",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldAccent
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = when (selectedLanguage) {
                                "hi", "hinglish" -> "सलाह — मोमिन का मेराज और दीन का सुतून"
                                "ur" -> "نماز — مومن کی معراج اور دین کا ستون"
                                "ar" -> "الصَّلَاةُ — مِعْرَاجُ الْمُؤْمِنِ وَعِمَادُ الدِّينِ"
                                "bn" -> "সালাত — মুমিনের মিরাজ ও দ্বীনের স্তম্ভ"
                                else -> "As-Salah — The Ascension of the Believer & Pillar of Religion"
                            },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Central Sacred Hadith Calligraphy Banner
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0x2E000000))
                                .border(1.dp, GoldAccent.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                                .padding(14.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "الصَّلَاةُ عِمَادُ الدِّينِ",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldAccent,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 34.sp
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "As-Salatu 'Imadud-Deen",
                                    fontSize = 13.sp,
                                    color = Color(0xFFD4E3DB),
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Medium
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = when (selectedLanguage) {
                                        "hi", "hinglish" -> "“नमाज़ दीन का सुतून (स्तंभ) है; जिसने इसे क़ायम किया उसने दीन को क़ायम किया।” (बैहक़ी 2550)"
                                        "ur" -> "”نماز دین کا ستون ہے؛ جس نے اسے قائم کیا اس نے دین کو قائم کیا، اور جس نے اسے گرایا اس نے دین کو گرا دیا۔“ (بیہقی ۲۵۵۰) "
                                        "ar" -> "«الصَّلَاةُ عِمَادُ الدِّينِ، فَمَنْ أَقَامَهَا فَقَدْ أَقَامَ الدِّينَ»"
                                        "bn" -> "“সালাত হলো দ্বীনের স্তম্ভ; যে তা প্রতিষ্ঠিত করল সে দ্বীনকে প্রতিষ্ঠিত করল।” (বায়হাকী ২৫৫০)"
                                        else -> "“Salah is the pillar of religion; whoever establishes it has established religion.” (Al-Bayhaqi 2550)"
                                    },
                                    fontSize = 13.sp,
                                    color = Color(0xFFE8F0EC),
                                    textAlign = TextAlign.Center,
                                    lineHeight = 19.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Calligraphy Reference Chip (Clickable with Authentic Hadith Modal)
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF08261B))
                                .border(0.8.dp, GoldAccent.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                .clickable {
                                    activeReferenceItem = PillarsReferenceRepository.findReference("Shu'ab al-Iman, Al-Bayhaqi 2550", "Salah as Pillar of Islam")
                                }
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "📜 Shu'ab al-Iman, Al-Bayhaqi 2550",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = GoldAccent
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFF133E2B))
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "🔍 View Hadith ↗",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldAccent
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Action Buttons: Listen, Copy, Share
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Listen Audio
                            val isPlaying = speakingStepId == 999
                            val listenLabel = when (selectedLanguage) {
                                "hi", "hinglish" -> if (isPlaying) "⏹ रोकें" else "🔊 सुनें"
                                "ur" -> if (isPlaying) "⏹ روکیں" else "🔊 سنیں"
                                "ar" -> if (isPlaying) "⏹ إيقاف" else "🔊 استمع"
                                else -> if (isPlaying) "⏹ Stop" else "🔊 Listen"
                            }
                            val copyLabel = when (selectedLanguage) {
                                "hi", "hinglish" -> "📋 कॉपी"
                                "ur" -> "📋 کاپی"
                                "ar" -> "📋 نسخ"
                                else -> "📋 Copy"
                            }
                            val shareLabel = when (selectedLanguage) {
                                "hi", "hinglish" -> "📲 शेयर"
                                "ur" -> "📲 شیئر"
                                "ar" -> "📲 مشاركة"
                                else -> "📲 Share"
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isPlaying) Color(0xFFC99728) else Color(0xFF135346))
                                    .clickable {
                                        if (isPlaying) stopAudio()
                                        else playArabicAudio("الصَّلَاةُ عِمَادُ الدِّينِ، فَمَنْ أَقَامَهَا فَقَدْ أَقَامَ الدِّينَ", 999)
                                    }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = listenLabel,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            // Copy
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF135346))
                                    .clickable {
                                        copyToClipboard(
                                            "Salah - The 2nd Pillar of Islam",
                                            "الصَّلَاةُ عِمَادُ الدِّينِ",
                                            "Salah is the pillar of religion (Baihaqi 2550)"
                                        )
                                    }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = copyLabel, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }

                            // Share
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF135346))
                                    .clickable {
                                        shareText(
                                            "Salah - The 2nd Pillar of Islam",
                                            "الصَّلَاةُ عِمَادُ الدِّينِ",
                                            "Salah is the pillar of religion; establishing it brings peace and eternal salvation."
                                        )
                                    }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = shareLabel, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }

                // Quranic Foundation 1: Surah Al-Baqarah 2:43
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(cardBg)
                        .border(1.dp, cardBorder, RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF135346)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "📖", fontSize = 14.sp)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = when (selectedLanguage) {
                                    "hi" -> "क़ुरान पाक का सीधा हुक्म (सूरह अल-बक़रह 2:43)"
                                    "ur" -> "قرآن مجید کا صریح حکم (سورۃ البقرۃ ۲:۴۳)"
                                    "ar" -> "الأمر القرآني بإقامة الصلاة (سورة البقرة: ٤٣)"
                                    else -> "Direct Quranic Command (Surah Al-Baqarah 2:43)"
                                },
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryText
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "وَأَقِيمُوا الصَّلَاةَ وَآتُوا الزَّكَاةَ وَارْكَعُوا مَعَ الرَّاكِعِينَ",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryText,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth(),
                            lineHeight = 28.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = when (selectedLanguage) {
                                "hi" -> "“और नमाज़ क़ायम करो, और ज़कात अदा करो, और रुकू करने वालों के साथ रुकू करो।”"
                                "ur" -> "”اور نماز قائم کرو اور زکوٰۃ ادا کرو اور رکوع کرنے والوں کے ساتھ رکوع کرو۔“"
                                else -> "“And establish prayer and give zakah and bow with those who bow [in worship and obedience].”"
                            },
                            fontSize = 13.sp,
                            color = secondaryText,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isDarkTheme) Color(0xFF072118) else Color(0xFFF1F5F9))
                                .clickable {
                                    activeReferenceItem = PillarsReferenceRepository.findReference("Surah Al-Baqarah 2:43", "Quran 2:43")
                                }
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "📖 Surah Al-Baqarah (2:43)",
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

                // Quranic Foundation 2: Surah Al-Ankabut 29:45 (Prohibits Immorality)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(cardBg)
                        .border(1.dp, cardBorder, RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFC99728)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🛡️", fontSize = 14.sp)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = when (selectedLanguage) {
                                    "hi" -> "बुराई और बेहयाई से हिफ़ाज़त (सूरह अनकबूत 29:45)"
                                    "ur" -> "بے حیائی اور برائی سے حفاظت (سورۃ العنکبوت ۲۹:۴۵)"
                                    "ar" -> "النهي عن الفحشاء والمنكر (سورة العنكبوت: ٤٥)"
                                    else -> "Protection from Immorality (Surah Al-Ankabut 29:45)"
                                },
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryText
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "إِنَّ الصَّلَاةَ تَنْهَىٰ عَنِ الْفَحْشَاءِ وَالْمُنْكَرِ ۗ وَلَذِكْرُ اللهِ أَكْبَرُ",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryText,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth(),
                            lineHeight = 28.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = when (selectedLanguage) {
                                "hi" -> "“बेशक नमाज़ बेहयाई और नाफ़रमानी के कामों से रोकती है, और यक़ीनन अल्लाह की याद सबसे बड़ी चीज़ है।”"
                                "ur" -> "”بے شک نماز بے حیائی اور برائی سے روکتی ہے، اور یقیناً اللہ کا ذکر سب سے بڑا ہے۔“"
                                else -> "“Indeed, prayer prohibits immorality and wrongdoing, and the remembrance of Allah is greater.”"
                            },
                            fontSize = 13.sp,
                            color = secondaryText,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isDarkTheme) Color(0xFF072118) else Color(0xFFF1F5F9))
                                .clickable {
                                    activeReferenceItem = PillarsReferenceRepository.findReference("Surah Al-Ankabut 29:45", "Quran 29:45")
                                }
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "📖 Surah Al-Ankabut (29:45)",
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

                // Mi'raj Story Card (The Gift of 5 Daily Prayers)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(cardBg)
                        .border(1.dp, cardBorder, RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🌙", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = when (selectedLanguage) {
                                    "hi" -> "मेराज का तोहफ़ा (50 नमाज़ों से 5 नमाज़ें)"
                                    "ur" -> "معراج کا تحفہ (۵۰ نمازوں سے ۵ نمازیں)"
                                    "ar" -> "هدية ليلة الإسراء والمعراج"
                                    else -> "The Gift of Mi'raj (50 to 5 Prayers)"
                                },
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryText
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = when (selectedLanguage) {
                                "hi" -> "शब-ए-मेराज में अल्लाह रब्बुल इज़्ज़त ने हज़रत मुहम्मद ﷺ को सीधे आसमानों पर बुलाकर उम्मत के लिए नमाज़ का तोहफ़ा अता फ़रमाया। शुरू में 50 नमाज़ें फ़र्ज़ थीं, फिर हज़रत मूसा (अलै.) के मशवरे पर हुज़ूर ﷺ ने दरख़्वास्त की और अल्लाह ने कम करते-करते 5 नमाज़ें मुक़र्रर कीं और फ़रमाया: 'पढ़ने में 5 हैं, लेकिन सवाब पूरे 50 नमाज़ों का मिलेगा।' (सहीह बुख़ारी 349, मुस्लिम 162)"
                                "ur" -> "شبِ معراج میں اللہ تعالیٰ نے حضرت محمد ﷺ کو بلا واسطہ بلا کر امت کے لیے نماز کا عظیم تحفہ عطا فرمایا۔ ابتدا میں ۵۰ نمازیں تھیں، پھر تخفیف ہو کر ۵ رہ گئیں اور اللہ نے فرمایا: 'پڑھنے میں ۵ ہیں لیکن اجر میں ۵۰ کا ثواب ملے گا۔' (بخاری ۳۴۹)"
                                else -> "During Isra & Mi'raj, Allah gifted the 5 daily prayers directly to Prophet Muhammad ﷺ. Originally 50 prayers, Allah mercifully reduced them to 5 while promising the reward of all 50. (Sahih Bukhari 349, Muslim 162)"
                            },
                            fontSize = 12.sp,
                            color = secondaryText,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isDarkTheme) Color(0xFF072118) else Color(0xFFF1F5F9))
                                .clickable {
                                    activeReferenceItem = PillarsReferenceRepository.findReference("Sahih al-Bukhari 349", "The Gift of 5 Daily Prayers")
                                }
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "📜 Sahih al-Bukhari 349, Muslim 162",
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

            // ══════════════════════════════════════════════════════════
            // TAB 1: STEP-BY-STEP PRAYER GUIDE (12 STEPS)
            // ══════════════════════════════════════════════════════════
            if (selectedTab == 1) {
                Text(
                    text = when (selectedLanguage) {
                        "hi" -> "तकबीर-ए-तहरीमा से सलाम तक मुकम्मल तरीक़ा (12 मराहिल)"
                        "ur" -> "تکبیر تحریمہ سے سلام تک مکمل طریقہ (۱۲ مراحل)"
                        "ar" -> "صفة صلاة النبي ﷺ خطوة بخطوة (١٢ مرحلة)"
                        else -> "Complete Step-by-Step Prayer Guide (12 Stages)"
                    },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryText
                )

                SalahData.STEPS.forEach { step ->
                    val isPlaying = speakingStepId == step.id

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(cardBg)
                            .border(1.dp, if (isPlaying) GoldAccent else cardBorder, RoundedCornerShape(16.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            // Step Header with Audio Play Button
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(emeraldAccent),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${step.id}",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = when (selectedLanguage) {
                                            "ur" -> step.stepNameUrdu
                                            "hi", "hinglish" -> step.stepNameHindi
                                            else -> step.stepNameEnglish
                                        },
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = primaryText
                                    )
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    // Audio Play Button
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isPlaying) Color(0xFFC99728) else Color(0xFF135346))
                                            .clickable {
                                                if (isPlaying) stopAudio()
                                                else playArabicAudio(step.arabicText, step.id)
                                            }
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        val stepAudioLabel = when (selectedLanguage) {
                                            "hi", "hinglish" -> if (isPlaying) "⏹ रोकें" else "🔊 सुनें"
                                            "ur" -> if (isPlaying) "⏹ روکیں" else "🔊 سنیں"
                                            "ar" -> if (isPlaying) "⏹ إيقاف" else "🔊 استمع"
                                            else -> if (isPlaying) "⏹ Stop" else "🔊 Listen"
                                        }
                                        Text(
                                            text = stepAudioLabel,
                                            fontSize = 11.sp,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    // Copy
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isDarkTheme) Color(0xFF133224) else Color(0xFFECE7DB))
                                            .clickable {
                                                copyToClipboard(
                                                    step.stepNameEnglish,
                                                    step.arabicText,
                                                    when (selectedLanguage) {
                                                        "hi", "hinglish" -> step.meaningHindi
                                                        "ur" -> step.meaningUrdu
                                                        else -> step.meaningEnglish
                                                    }
                                                )
                                            }
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(text = "📋", fontSize = 11.sp)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Arabic Box
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isDarkTheme) Color(0xFF071B12) else Color(0xFFF3EFE6))
                                    .border(1.dp, Color(0xFFC99728).copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = step.arabicText,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDarkTheme) Color(0xFFF9F4E8) else Color(0xFF0F3A2B),
                                    textAlign = TextAlign.Right,
                                    modifier = Modifier.fillMaxWidth(),
                                    lineHeight = 28.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Transliteration
                            Text(
                                text = step.transliteration,
                                fontSize = 12.sp,
                                color = deepGold,
                                fontWeight = FontWeight.Medium,
                                lineHeight = 16.sp
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // Meaning
                            Text(
                                text = when (selectedLanguage) {
                                    "ur" -> step.meaningUrdu
                                    "hi", "hinglish" -> step.meaningHindi
                                    else -> step.meaningEnglish
                                },
                                fontSize = 13.sp,
                                color = primaryText,
                                lineHeight = 18.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Physical Posture Instruction Box
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isDarkTheme) Color(0xFF0F261C) else Color(0xFFF6F2E9))
                                    .padding(10.dp)
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = "📌", fontSize = 12.sp)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = when (selectedLanguage) {
                                                "hi" -> "तरीक़ा व हिदायत (How to perform):"
                                                "ur" -> "طریقہ اور ہدایت:"
                                                else -> "How to perform:"
                                            },
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = deepGold
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = when (selectedLanguage) {
                                            "hi", "hinglish" -> step.instructionHindi
                                            else -> step.instructionEnglish
                                        },
                                        fontSize = 12.sp,
                                        color = secondaryText,
                                        lineHeight = 17.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Hadith Proof Tag (Clickable with Authentic Hadith Modal)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isDarkTheme) Color(0xFF072118) else Color(0xFFF1F5F9))
                                    .clickable {
                                        activeReferenceItem = PillarsReferenceRepository.findReference(step.hadithProof, step.stepNameEnglish)
                                    }
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "📖 " + step.hadithProof,
                                    fontSize = 11.sp,
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

            // ══════════════════════════════════════════════════════════
            // TAB 2: 5 DAILY PRAYERS RAK'AHS TABLE
            // ══════════════════════════════════════════════════════════
            if (selectedTab == 2) {
                Text(
                    text = when (selectedLanguage) {
                        "hi" -> "पाँचों वक़्त की नमाज़ों और जुमा की मुकम्मल रकअतें"
                        "ur" -> "پانچوں وقت کی نمازوں اور جمعہ کی مکمل رکعتیں"
                        "ar" -> "جدول ركعات الصلوات الخمس والجمعة"
                        else -> "5 Daily Prayers & Jummah Rak'ahs Table"
                    },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryText
                )

                SalahData.RAKATS.forEach { prayer ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(cardBg)
                            .border(1.dp, cardBorder, RoundedCornerShape(16.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            // Header: Name & Total Rak'ahs Badge
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = when (selectedLanguage) {
                                            "hi", "hinglish" -> prayer.nameHindi
                                            "ur" -> prayer.nameUrdu
                                            else -> prayer.nameEnglish
                                        },
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = primaryText
                                    )
                                    Text(
                                        text = prayer.arabicName,
                                        fontSize = 13.sp,
                                        color = deepGold,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                // Total Rak'ah Badge
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Brush.horizontalGradient(listOf(Color(0xFF0D4A3E), Color(0xFF196753))))
                                        .border(1.dp, GoldAccent, RoundedCornerShape(12.dp))
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    val totalRakatsLabel = when (selectedLanguage) {
                                        "hi", "hinglish" -> "कुल ${prayer.totalRakats} रकअतें"
                                        "ur" -> "کل ${prayer.totalRakats} رکعتیں"
                                        "ar" -> "المجموع ${prayer.totalRakats} ركعات"
                                        "bn" -> "মোট ${prayer.totalRakats} রাকাত"
                                        else -> "Total ${prayer.totalRakats} Rak'ahs"
                                    }
                                    Text(
                                        text = totalRakatsLabel,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Rak'ah Chips Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                val sunnahW = when (selectedLanguage) {
                                    "hi", "hinglish" -> "सुन्नत"
                                    "ur" -> "سنت"
                                    "ar" -> "سنة"
                                    "bn" -> "সুন্নাত"
                                    else -> "Sunnah"
                                }
                                val fardW = when (selectedLanguage) {
                                    "hi", "hinglish" -> "फ़र्ज़"
                                    "ur" -> "فرض"
                                    "ar" -> "فرض"
                                    "bn" -> "ফরজ"
                                    else -> "Fard"
                                }
                                val naflW = when (selectedLanguage) {
                                    "hi", "hinglish" -> "नफ़्ल"
                                    "ur" -> "نفل"
                                    "ar" -> "نفل"
                                    "bn" -> "নফল"
                                    else -> "Nafl"
                                }
                                val witrW = when (selectedLanguage) {
                                    "hi", "hinglish" -> "वित्र"
                                    "ur" -> "وتر"
                                    "ar" -> "وتر"
                                    "bn" -> "বিতর"
                                    else -> "Witr"
                                }
                                if (prayer.sunnahBefore > 0) {
                                    RakatsChip("${prayer.sunnahBefore} $sunnahW", Color(0xFF135346))
                                }
                                if (prayer.fard > 0) {
                                    RakatsChip("${prayer.fard} $fardW", Color(0xFFC99728))
                                }
                                if (prayer.sunnahAfter > 0) {
                                    RakatsChip("${prayer.sunnahAfter} $sunnahW", Color(0xFF135346))
                                }
                                if (prayer.nafl > 0) {
                                    RakatsChip("${prayer.nafl} $naflW", Color(0xFF2E6B80))
                                }
                                if (prayer.witr > 0) {
                                    RakatsChip("${prayer.witr} $witrW", Color(0xFF7A3E8C))
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Time Window
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "⏰", fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = when (selectedLanguage) {
                                        "hi", "hinglish" -> prayer.timeWindowHindi
                                        else -> prayer.timeWindowEnglish
                                    },
                                    fontSize = 12.sp,
                                    color = secondaryText,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Details / Hadith Virtue
                            Text(
                                text = when (selectedLanguage) {
                                    "hi", "hinglish" -> prayer.detailsHindi
                                    else -> prayer.detailsEnglish
                                },
                                fontSize = 12.sp,
                                color = primaryText,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
            }

            // ══════════════════════════════════════════════════════════
            // TAB 3: 9 CONDITIONS & 14 PILLARS
            // ══════════════════════════════════════════════════════════
            if (selectedTab == 3) {
                Text(
                    text = when (selectedLanguage) {
                        "hi" -> "नमाज़ की 9 शर्तें (शराइत) और 14 लाज़िमी अरकान"
                        "ur" -> "نماز کی ۹ شرائط اور ۱۴ لازمی ارکان"
                        "ar" -> "شروط الصلاة التسعة وأركانها الأربعة عشر"
                        else -> "9 Prerequisites (Shuroot) & 14 Essential Pillars (Arkaan)"
                    },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryText
                )

                SalahData.CONDITIONS_AND_PILLARS.forEach { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
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
                                Text(
                                    text = when (selectedLanguage) {
                                        "hi", "hinglish" -> item.titleHindi
                                        "ur" -> item.titleUrdu
                                        else -> item.titleEnglish
                                    },
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = primaryText,
                                    modifier = Modifier.weight(1f)
                                )

                                val typeLabel = when (selectedLanguage) {
                                    "hi", "hinglish" -> if (item.type.contains("SHART")) "शर्त (Pre-condition)" else "रुक्न (Pillar)"
                                    "ur" -> if (item.type.contains("SHART")) "شرط" else "رکن"
                                    "ar" -> if (item.type.contains("SHART")) "شرط" else "ركن"
                                    else -> if (item.type.contains("SHART")) "Condition (Shart)" else "Pillar (Rukn)"
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(
                                            if (item.type.contains("SHART")) Color(0xFF135346)
                                            else Color(0xFFC99728)
                                        )
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = typeLabel,
                                        fontSize = 10.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = item.arabicTitle,
                                fontSize = 14.sp,
                                color = deepGold,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = when (selectedLanguage) {
                                    "hi", "hinglish" -> item.explanationHindi
                                    else -> item.explanationEnglish
                                },
                                fontSize = 12.sp,
                                color = secondaryText,
                                lineHeight = 17.sp
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // Proof Reference (Clickable with Authentic Hadith/Ayah Modal)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isDarkTheme) Color(0xFF072118) else Color(0xFFF1F5F9))
                                    .clickable {
                                        activeReferenceItem = PillarsReferenceRepository.findReference(item.proofReference, item.titleEnglish)
                                    }
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "📜 " + item.proofReference,
                                    fontSize = 11.sp,
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

            // ══════════════════════════════════════════════════════════
            // TAB 4: SAHIH HADITH VIRTUES
            // ══════════════════════════════════════════════════════════
            if (selectedTab == 4) {
                Text(
                    text = when (selectedLanguage) {
                        "hi" -> "सहीह अहादीस में नमाज़ की अज़ीम फ़ज़ीलतें"
                        "ur" -> "صحیح احادیث میں نماز کی عظیم فضیلتیں"
                        "ar" -> "فضائل الصلاة في الأحاديث الصحيحة"
                        else -> "Authentic Hadith Virtues of Salah"
                    },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryText
                )

                SalahData.VIRTUES.forEachIndexed { index, virtue ->
                    val isPlaying = speakingStepId == 100 + index

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(cardBg)
                            .border(1.dp, if (isPlaying) GoldAccent else cardBorder, RoundedCornerShape(16.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            // Topic Header
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = when (selectedLanguage) {
                                        "hi", "hinglish" -> virtue.topicHindi
                                        else -> virtue.topicEnglish
                                    },
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = primaryText,
                                    modifier = Modifier.weight(1f)
                                )

                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    // Audio Play
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isPlaying) Color(0xFFC99728) else Color(0xFF135346))
                                            .clickable {
                                                if (isPlaying) stopAudio()
                                                else playArabicAudio(virtue.arabicText, 100 + index)
                                            }
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        val vAudioLabel = when (selectedLanguage) {
                                            "hi", "hinglish" -> if (isPlaying) "⏹ रोकें" else "🔊 सुनें"
                                            "ur" -> if (isPlaying) "⏹ روکیں" else "🔊 سنیں"
                                            "ar" -> if (isPlaying) "⏹ إيقاف" else "🔊 استمع"
                                            else -> if (isPlaying) "⏹ Stop" else "🔊 Listen"
                                        }
                                        Text(
                                            text = vAudioLabel,
                                            fontSize = 11.sp,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    // Copy
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isDarkTheme) Color(0xFF133224) else Color(0xFFECE7DB))
                                            .clickable {
                                                copyToClipboard(
                                                    virtue.topicEnglish,
                                                    virtue.arabicText,
                                                    when (selectedLanguage) {
                                                        "hi", "hinglish" -> virtue.translationHindi
                                                        "ur" -> virtue.translationUrdu
                                                        else -> virtue.translationEnglish
                                                    }
                                                )
                                            }
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(text = "📋", fontSize = 11.sp)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Arabic Box
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isDarkTheme) Color(0xFF071B12) else Color(0xFFF3EFE6))
                                    .border(1.dp, Color(0xFFC99728).copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = virtue.arabicText,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDarkTheme) Color(0xFFF9F4E8) else Color(0xFF0F3A2B),
                                    textAlign = TextAlign.Right,
                                    modifier = Modifier.fillMaxWidth(),
                                    lineHeight = 26.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Translation
                            Text(
                                text = when (selectedLanguage) {
                                    "ur" -> virtue.translationUrdu
                                    "hi", "hinglish" -> virtue.translationHindi
                                    else -> virtue.translationEnglish
                                },
                                fontSize = 13.sp,
                                color = primaryText,
                                lineHeight = 19.sp
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // Source Reference Tag (Clickable)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isDarkTheme) Color(0xFF072118) else Color(0xFFF1F5F9))
                                    .clickable {
                                        activeReferenceItem = PillarsReferenceRepository.findReference(virtue.source, virtue.topicEnglish)
                                    }
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "📚 " + virtue.source,
                                    fontSize = 11.sp,
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

            Spacer(modifier = Modifier.height(20.dp))
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
private fun RakatsChip(label: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color)
            .padding(horizontal = 6.dp, vertical = 3.dp)
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}
