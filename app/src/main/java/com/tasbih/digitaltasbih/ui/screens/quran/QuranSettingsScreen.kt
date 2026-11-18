package com.tasbih.digitaltasbih.ui.screens.quran

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.LaunchedEffect
import com.tasbih.digitaltasbih.data.QuranData
import com.tasbih.digitaltasbih.data.QuranReadingMode
import com.tasbih.digitaltasbih.data.QuranRepository
import com.tasbih.digitaltasbih.data.QuranSettingsState
import com.tasbih.digitaltasbih.data.QuranAudioPlayMode
import com.tasbih.digitaltasbih.data.QuranSurahAudioEngine
import com.tasbih.digitaltasbih.ui.components.QuranAudioModeButton
import com.tasbih.digitaltasbih.ui.theme.GoldAccent

@Composable
fun QuranSettingsScreen(
    onBack: () -> Unit,
    onOpenChooseReciter: () -> Unit
) {
    var settings by remember { mutableStateOf(QuranRepository.loadSettings()) }
    LaunchedEffect(Unit) {
        settings = QuranRepository.loadSettings()
    }
    val lang = settings.translationLanguage

    var isLangDropdownOpen by remember { mutableStateOf(false) }
    var isTranslatorDropdownOpen by remember { mutableStateOf(false) }
    var isTafsirDropdownOpen by remember { mutableStateOf(false) }
    var isRepeatDropdownOpen by remember { mutableStateOf(false) }

    fun updateSettings(newSettings: QuranSettingsState) {
        settings = newSettings
        QuranRepository.saveSettings(newSettings)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F7F1))
            .statusBarsPadding()
    ) {
        // Top Bar: [ ← Back ], QURAN SETTINGS
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                    tint = Color(0xFF0D4A3E),
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = when (lang) {
                        "hi" -> "वापस"
                        "ur" -> "واپس"
                        "ar" -> "رجوع"
                        "bn" -> "ফিরে যান"
                        else -> "Back"
                    },
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D4A3E)
                )
            }

            Spacer(modifier = Modifier.weight(0.7f))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = when (lang) {
                        "hi" -> "क़ुरान सेटिंग्स"
                        "ur" -> "قرآن سیٹنگز"
                        "ar" -> "إعدادات القرآن"
                        "bn" -> "কোরআন সেটিংস"
                        else -> "QURAN SETTINGS"
                    },
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D4A3E),
                    fontFamily = FontFamily.Serif,
                    letterSpacing = 1.sp
                )
                Box(
                    modifier = Modifier
                        .width(42.dp)
                        .height(2.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(GoldAccent)
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ══════════════════════════════════════════════════════════════
            // 1. READING MODE
            // ══════════════════════════════════════════════════════════════
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when (lang) {
                        "hi" -> "पढ़ने का तरीका"
                        "ur" -> "پڑھنے کا انداز"
                        "ar" -> "طريقة القراءة"
                        "bn" -> "পড়ার ধরন"
                        else -> "Reading Mode"
                    },
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFB45309) // Bronze gold label
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF0D4A3E).copy(alpha = 0.1f))
                        .border(1.dp, Color(0xFF0D4A3E).copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    val badgeText = when (settings.readingMode) {
                        QuranReadingMode.MUSHAF -> "Real 15-Line Mushaf"
                        QuranReadingMode.LIST -> "List Cards"
                        QuranReadingMode.TRANSLATION -> "Flowing Translation"
                    }
                    Text(
                        text = badgeText,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0D4A3E)
                    )
                }
            }

            // 3 Mode Preview Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // List Mode
                ModeCard(
                    title = "List",
                    sub = "Ayat by Ayat Cards",
                    isSelected = settings.readingMode == QuranReadingMode.LIST,
                    previewType = 1,
                    modifier = Modifier.weight(1f),
                    onClick = { updateSettings(settings.copy(readingMode = QuranReadingMode.LIST)) }
                )

                // Translation Mode
                ModeCard(
                    title = "Translation",
                    sub = "Flowing Text Only",
                    isSelected = settings.readingMode == QuranReadingMode.TRANSLATION,
                    previewType = 2,
                    modifier = Modifier.weight(1f),
                    onClick = { updateSettings(settings.copy(readingMode = QuranReadingMode.TRANSLATION)) }
                )

                // Real Mushaf Page Mode
                ModeCard(
                    title = "Real Mushaf Page",
                    sub = "15-Line Book Page",
                    isSelected = settings.readingMode == QuranReadingMode.MUSHAF,
                    previewType = 3,
                    modifier = Modifier.weight(1f),
                    onClick = { updateSettings(settings.copy(readingMode = QuranReadingMode.MUSHAF)) }
                )
            }

            // ══════════════════════════════════════════════════════════════
            // 2. TRANSLATION LANGUAGE
            // ══════════════════════════════════════════════════════════════
            SettingSection(
                title = when (lang) {
                    "hi" -> "अनुवाद भाषा"
                    "ur" -> "ترجمہ کی زبان"
                    "ar" -> "لغة الترجمة"
                    "bn" -> "অনুবাদ ভাষা"
                    else -> "Translation Language"
                }
            ) {
                Box {
                    DropdownCard(
                        text = AvailableQuranLanguages.find { it.code == settings.translationLanguage }?.let { "${it.flag}  ${it.name} (${it.nativeName})" }
                            ?: settings.translationLanguage.uppercase(),
                        onClick = { isLangDropdownOpen = true }
                    )
                    DropdownMenu(
                        expanded = isLangDropdownOpen,
                        onDismissRequest = { isLangDropdownOpen = false }
                    ) {
                        AvailableQuranLanguages.forEach { langItem ->
                            DropdownMenuItem(
                                text = { Text("${langItem.flag}  ${langItem.name} — ${langItem.nativeName}") },
                                onClick = {
                                    val updated = QuranRepository.switchLanguage(langItem.code)
                                    settings = updated
                                    isLangDropdownOpen = false
                                }
                            )
                        }
                    }
                }
            }

            // ══════════════════════════════════════════════════════════════
            // 3. TRANSLATOR
            // ══════════════════════════════════════════════════════════════
            SettingSection(
                title = when (lang) {
                    "hi" -> "अनुवादक"
                    "ur" -> "مترجم"
                    "ar" -> "المترجم"
                    "bn" -> "অনুবাদক"
                    else -> "Translator"
                }
            ) {
                Box {
                    DropdownCard(
                        text = settings.translatorName,
                        onClick = { isTranslatorDropdownOpen = true }
                    )
                    DropdownMenu(
                        expanded = isTranslatorDropdownOpen,
                        onDismissRequest = { isTranslatorDropdownOpen = false }
                    ) {
                        val translators = when (settings.translationLanguage) {
                            "hi" -> listOf(
                                Pair("122", "Maulana Azizul Haque al-Umari"),
                                Pair("hi.hindi", "Suhel Farooq Khan & Saifur Rahman")
                            )
                            "ur" -> listOf(
                                Pair("234", "Fatah Muhammad Jalandhari"),
                                Pair("54", "Maulana Muhammad Junagarhi"),
                                Pair("97", "Sayyid Abul Ala Maududi (Tafheem)"),
                                Pair("831", "Abul Ala Maududi (Roman Urdu)")
                            )
                            "bn" -> listOf(
                                Pair("161", "Taisirul Quran"),
                                Pair("163", "Sheikh Mujibur Rahman"),
                                Pair("213", "Dr. Abu Bakr Muhammad Zakaria")
                            )
                            "en" -> listOf(
                                Pair("85", "Abdul Haleem"),
                                Pair("20", "Saheeh International"),
                                Pair("84", "Mufti Taqi Usmani"),
                                Pair("22", "Abdullah Yusuf Ali")
                            )
                            else -> {
                                val info = QuranRepository.getLanguageInfo(settings.translationLanguage)
                                listOf(Pair(info.second, info.third))
                            }
                        }

                        translators.forEach { (id, name) ->
                            DropdownMenuItem(
                                text = { Text(name) },
                                onClick = {
                                    updateSettings(settings.copy(translationId = id, translatorName = name))
                                    isTranslatorDropdownOpen = false
                                }
                            )
                        }
                    }
                }
            }

            // ══════════════════════════════════════════════════════════════
            // 4. SHOW TRANSLATION TOGGLE
            // ══════════════════════════════════════════════════════════════
            ToggleRow(
                title = when (lang) {
                    "hi" -> "अनुवाद दिखाएं"
                    "ur" -> "ترجمہ دکھائیں"
                    "ar" -> "إظهار الترجمة"
                    "bn" -> "অনুবাদ দেখান"
                    else -> "Show Translation"
                },
                checked = settings.showTranslation,
                onCheckedChange = { updateSettings(settings.copy(showTranslation = it)) }
            )

            // ══════════════════════════════════════════════════════════════
            // 5. RECITER
            // ══════════════════════════════════════════════════════════════
            val activeReciter = QuranData.reciters.find { it.identifier == settings.reciterId }
            val reciterNameDisp = activeReciter?.getLocalizedName(lang) ?: settings.reciterName
            val reciterFlagDisp = activeReciter?.flag ?: ""
            val reciterCountryDisp = activeReciter?.getLocalizedCountry(lang) ?: ""
            val reciterLetterDisp = activeReciter?.getLocalizedLetter(lang) ?: "🎙️"

            SettingSection(
                title = when (lang) {
                    "hi" -> "क़ारी (ऑडियो पाठक)"
                    "ur" -> "قاری (تلاوت کرنے والا)"
                    "ar" -> "القارئ"
                    "bn" -> "ক্বারী"
                    else -> "Reciter"
                }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(1.5.dp, RoundedCornerShape(14.dp))
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFFFFDF8))
                        .border(1.2.dp, GoldAccent.copy(alpha = 0.45f), RoundedCornerShape(14.dp))
                        .clickable(onClick = onOpenChooseReciter)
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Letter Badge in dark emerald circle (matching Choose Reciter)
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF0D4A3E)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = reciterLetterDisp,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = reciterNameDisp,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1F2937)
                                )
                                if (reciterFlagDisp.isNotEmpty()) {
                                    Text(
                                        text = reciterFlagDisp,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                            if (reciterCountryDisp.isNotEmpty()) {
                                Text(
                                    text = reciterCountryDisp,
                                    fontSize = 11.5.sp,
                                    color = Color(0xFF7A6840)
                                )
                            }
                        }
                    }

                    Text(
                        text = "›",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Light,
                        color = Color.Gray
                    )
                }
            }

            // ══════════════════════════════════════════════════════════════
            // 5b. AUDIO PLAYBACK MODE & TARJUMA VOICE
            // ══════════════════════════════════════════════════════════════
            val modeStrings = remember(lang) { com.tasbih.digitaltasbih.ui.components.getQuranAudioModeStrings(lang) }

            SettingSection(
                title = when (lang) {
                    "hi" -> "ऑडियो प्लेबैक मोड और तर्जुमा की आवाज"
                    "ur" -> "آڈیو موڈ اور ترجمہ کی آواز"
                    "ar" -> "نمط التلاوة وصوت الترجمة"
                    else -> "Audio Playback Mode & Translation Voice"
                }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(1.5.dp, RoundedCornerShape(14.dp))
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFFFFDF8))
                        .border(1.2.dp, GoldAccent.copy(alpha = 0.45f), RoundedCornerShape(14.dp))
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = when (settings.audioPlayMode) {
                                QuranAudioPlayMode.ARABIC_ONLY -> modeStrings.arabicOnlyTitle
                                QuranAudioPlayMode.TRANSLATION_ONLY -> modeStrings.tarjumaOnlyTitle
                                QuranAudioPlayMode.ARABIC_AND_TRANSLATION -> modeStrings.arabicAndTarjumaTitle
                            },
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937)
                        )
                        Text(
                            text = if (settings.translationVoiceLang == "en") modeStrings.englishVoiceDesc else modeStrings.urduVoiceDesc,
                            fontSize = 11.5.sp,
                            color = Color(0xFF7A6840)
                        )
                    }

                    QuranAudioModeButton(
                        audioPlayMode = settings.audioPlayMode,
                        translationVoiceLang = settings.translationVoiceLang,
                        language = lang,
                        onModeChange = { newMode, newVoiceLang ->
                            val updated = settings.copy(audioPlayMode = newMode, translationVoiceLang = newVoiceLang)
                            updateSettings(updated)
                            QuranSurahAudioEngine.setPlayMode(newMode, newVoiceLang)
                        }
                    )
                }
            }

            // ══════════════════════════════════════════════════════════════
            // 6. TRANSLITERATION TOGGLE
            // ══════════════════════════════════════════════════════════════
            ToggleRow(
                title = when (lang) {
                    "hi" -> "लिप्यांतरण (रोमन)"
                    "ur" -> "رومن تلفظ"
                    "ar" -> "النقحرة"
                    "bn" -> "উচ্চারণ"
                    else -> "Transliteration"
                },
                checked = settings.showTransliteration,
                onCheckedChange = { updateSettings(settings.copy(showTransliteration = it)) }
            )

            // ══════════════════════════════════════════════════════════════
            // 7. AUTOPLAY NEXT AYAH TOGGLE
            // ══════════════════════════════════════════════════════════════
            ToggleRow(
                title = when (lang) {
                    "hi" -> "अगली आयत अपने आप चलाएं"
                    "ur" -> "اگلی آیت خودکار چلائیں"
                    "ar" -> "تشغيل الآية التالية تلقائياً"
                    "bn" -> "পরবর্তী আয়াত অটো-প্লে"
                    else -> "Autoplay next Ayah"
                },
                checked = settings.autoplayNext,
                onCheckedChange = { updateSettings(settings.copy(autoplayNext = it)) }
            )

            // ══════════════════════════════════════════════════════════════
            // 8. AUTO-SCROLL WITH AUDIO TOGGLE
            // ══════════════════════════════════════════════════════════════
            ToggleRow(
                title = when (lang) {
                    "hi" -> "ऑडियो के साथ ऑटो-स्क्रॉल"
                    "ur" -> "آڈیو کے ساتھ خودکار اسکرول"
                    "ar" -> "التمرير التلقائي مع الصوت"
                    "bn" -> "অডিওর সাথে অটো-স্ক্রোল"
                    else -> "Auto-scroll with Audio"
                },
                checked = settings.autoScroll,
                onCheckedChange = { updateSettings(settings.copy(autoScroll = it)) }
            )

            // ══════════════════════════════════════════════════════════════
            // 8.1. NIGHT MODE TOGGLE
            // ══════════════════════════════════════════════════════════════
            ToggleRow(
                title = when (lang) {
                    "hi" -> "नाईट मोड (डार्क थीम) 🌙"
                    "ur" -> "نائٹ موڈ (ڈارک تھیم) 🌙"
                    "ar" -> "الوضع الليلي 🌙"
                    "bn" -> "নাইট মোড (ডার্ক থিম) 🌙"
                    else -> "Night Mode (Dark Theme) 🌙"
                },
                checked = settings.isNightMode,
                onCheckedChange = { updateSettings(settings.copy(isNightMode = it)) }
            )

            // ══════════════════════════════════════════════════════════════
            SettingSection(
                title = when (lang) {
                    "hi" -> "तफ़सीर (व्याख्या)"
                    "ur" -> "تفسیر"
                    "ar" -> "التفسير"
                    "bn" -> "তাফসীর"
                    else -> "Tafsir"
                }
            ) {
                Box {
                    DropdownCard(
                        text = settings.tafsirName,
                        onClick = { isTafsirDropdownOpen = true }
                    )
                    DropdownMenu(
                        expanded = isTafsirDropdownOpen,
                        onDismissRequest = { isTafsirDropdownOpen = false }
                    ) {
                        listOf(
                            Pair("926", "Arabic Jalalayn Tafseer"),
                            Pair("169", "Ibn Kathir (Abridged)"),
                            Pair("925", "Arabic Tanweer Tafseer"),
                            Pair("381", "Tafsir Fathul Majid")
                        ).forEach { (id, name) ->
                            DropdownMenuItem(
                                text = { Text(name) },
                                onClick = {
                                    updateSettings(settings.copy(tafsirId = id, tafsirName = name))
                                    isTafsirDropdownOpen = false
                                }
                            )
                        }
                    }
                }
            }

            // ══════════════════════════════════════════════════════════════
            // 10. REPEAT
            // ══════════════════════════════════════════════════════════════
            SettingSection(
                title = when (lang) {
                    "hi" -> "दोहराव"
                    "ur" -> "دہرائیں"
                    "ar" -> "التكرار"
                    "bn" -> "পুনরাবৃত্তি"
                    else -> "Repeat"
                }
            ) {
                Box {
                    DropdownCard(
                        text = settings.repeatMode,
                        onClick = { isRepeatDropdownOpen = true }
                    )
                    DropdownMenu(
                        expanded = isRepeatDropdownOpen,
                        onDismissRequest = { isRepeatDropdownOpen = false }
                    ) {
                        listOf("Repeat Off", "Repeat Ayah 1x", "Repeat Ayah 3x", "Continuous Play").forEach { mode ->
                            DropdownMenuItem(
                                text = { Text(mode) },
                                onClick = {
                                    updateSettings(settings.copy(repeatMode = mode))
                                    isRepeatDropdownOpen = false
                                }
                            )
                        }
                    }
                }
            }

            // ══════════════════════════════════════════════════════════════
            // 11. ARABIC FONT SIZE / ZOOM SLIDER
            // ══════════════════════════════════════════════════════════════
            SettingSection(
                title = when (lang) {
                    "hi" -> "अरबी फ़ॉन्ट साइज़"
                    "ur" -> "عربی فونٹ سائز"
                    "ar" -> "حجم الخط العربي"
                    "bn" -> "আরবি ফন্ট সাইজ"
                    else -> "Arabic Font Size / Zoom"
                }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 1.dp, shape = RoundedCornerShape(50.dp), spotColor = Color(0x140D4A3E))
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color.White)
                        .border(
                            width = 1.dp,
                            color = Color(0xFF0D4A3E).copy(alpha = 0.2f),
                            shape = RoundedCornerShape(50.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Smaller font (A-)
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF1F7F4))
                                .clickable {
                                    val newSize = (settings.arabicFontSizeSp - 2f).coerceAtLeast(18f)
                                    updateSettings(settings.copy(arabicFontSizeSp = newSize))
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "A-",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0D4A3E)
                            )
                        }

                        // Slim Custom Slider
                        @OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
                        Slider(
                            value = settings.arabicFontSizeSp,
                            onValueChange = { updateSettings(settings.copy(arabicFontSizeSp = it)) },
                            valueRange = 18f..36f,
                            modifier = Modifier
                                .weight(1f)
                                .height(28.dp),
                            thumb = {
                                Box(
                                    modifier = Modifier
                                        .size(15.dp)
                                        .shadow(2.dp, CircleShape)
                                        .background(Color(0xFF0D4A3E), CircleShape)
                                        .border(1.5.dp, Color.White, CircleShape)
                                )
                            },
                            track = { sliderState ->
                                SliderDefaults.Track(
                                    sliderState = sliderState,
                                    modifier = Modifier.height(3.5.dp),
                                    colors = SliderDefaults.colors(
                                        activeTrackColor = Color(0xFF0D4A3E),
                                        inactiveTrackColor = Color(0xFFE2EDE7)
                                    ),
                                    drawStopIndicator = null
                                )
                            }
                        )

                        // Larger font (A+)
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF1F7F4))
                                .clickable {
                                    val newSize = (settings.arabicFontSizeSp + 2f).coerceAtMost(36f)
                                    updateSettings(settings.copy(arabicFontSizeSp = newSize))
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "A+",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0D4A3E)
                            )
                        }

                        // Size indicator badge - tap to reset to default 28sp
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFEAF5EE))
                                .border(0.8.dp, Color(0xFF0D4A3E).copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                                .clickable { updateSettings(settings.copy(arabicFontSizeSp = 28f)) }
                                .padding(horizontal = 7.dp, vertical = 2.5.dp)
                        ) {
                            Text(
                                text = "${settings.arabicFontSizeSp.toInt()}sp",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF0D4A3E)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ModeCard(
    title: String,
    sub: String,
    isSelected: Boolean,
    previewType: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .shadow(if (isSelected) 3.dp else 1.dp, RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFFFFDF8))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) Color(0xFF0D4A3E) else GoldAccent.copy(alpha = 0.45f),
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick)
            .padding(7.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Miniature preview box (Matching exact screenshot illustration)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(76.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(8.dp))
                    .padding(4.dp)
            ) {
                when (previewType) {
                    1 -> {
                        // 1. List Mode Miniature Preview (Green Ribbon on left with badge, Arabic text, action icons)
                        Row(modifier = Modifier.fillMaxSize()) {
                            // Left dark emerald ribbon
                            Box(
                                modifier = Modifier
                                    .width(13.dp)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp))
                                    .background(Color(0xFF0E4B3D)),
                                contentAlignment = Alignment.TopCenter
                            ) {
                                Box(
                                    modifier = Modifier
                                        .padding(top = 4.dp)
                                        .size(9.dp)
                                        .clip(CircleShape)
                                        .background(GoldAccent),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "1",
                                        fontSize = 6.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(4.dp))

                            Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
                                // Top right mini action rosettes
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "⭐ 📋 ▶ 📖",
                                        fontSize = 5.sp,
                                        color = GoldAccent
                                    )
                                }

                                Spacer(modifier = Modifier.height(2.dp))

                                // Arabic line
                                Text(
                                    text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                                    fontSize = 7.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0D4A3E),
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.fillMaxWidth(),
                                    maxLines = 1
                                )

                                Spacer(modifier = Modifier.height(3.dp))
                                HorizontalDivider(color = GoldAccent.copy(alpha = 0.3f), thickness = 0.5.dp)
                                Spacer(modifier = Modifier.height(2.dp))

                                // Transliteration line
                                Text(
                                    text = "bismi llāhi r-raḥmā...",
                                    fontSize = 5.5.sp,
                                    fontStyle = FontStyle.Italic,
                                    color = Color(0xFF6B7280),
                                    maxLines = 1
                                )
                            }
                        }
                    }
                    2 -> {
                        // 2. Flowing Translation Miniature Preview
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 2.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "① In the name of God...",
                                fontSize = 6.5.sp,
                                color = Color(0xFF1F2937),
                                fontWeight = FontWeight.Medium,
                                maxLines = 1
                            )
                            Text(
                                text = "② Praise belongs to God...",
                                fontSize = 6.5.sp,
                                color = Color(0xFF1F2937),
                                fontWeight = FontWeight.Medium,
                                maxLines = 1
                            )
                            Text(
                                text = "③ The Lord of Mercy...",
                                fontSize = 6.5.sp,
                                color = Color(0xFF1F2937),
                                fontWeight = FontWeight.Medium,
                                maxLines = 1
                            )
                        }
                    }
                    3 -> {
                        // 3. Real 15-Line Mushaf Miniature Preview (Green Header + Ruled Table)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            // Green Surah Header
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(13.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(Color(0xFF0E4B3D))
                                    .border(0.5.dp, GoldAccent, RoundedCornerShape(3.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "سُورَةُ الفَاتِحَة",
                                    fontSize = 6.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldAccent
                                )
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            // Ruled lines with 2 columns
                            val lines = listOf(
                                "الرَّحْمَـٰنِ الرَّحِيمِ" to "بِسْمِ اللَّهِ",
                                "رَبِّ الْعَالَمِينَ" to "الْحَمْدُ لِلَّهِ",
                                "الرَّحْمَـٰنِ" to "مَالِكِ يَوْمِ الدِّينِ"
                            )
                            lines.forEach { (c1, c2) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 1.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = c1, fontSize = 6.sp, color = Color(0xFF0D4A3E), maxLines = 1)
                                    Text(text = c2, fontSize = 6.sp, color = Color(0xFF0D4A3E), maxLines = 1)
                                }
                                HorizontalDivider(color = GoldAccent.copy(alpha = 0.35f), thickness = 0.5.dp)
                            }
                        }
                    }
                }

                // Checkmark badge if selected (Top-Right corner of the card preview)
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0D4A3E)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = Color.White,
                            modifier = Modifier.size(10.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937),
                maxLines = 1
            )
            Text(
                text = sub,
                fontSize = 9.sp,
                color = Color.Gray,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun SettingSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937)
        )
        content()
    }
}

@Composable
private fun DropdownCard(
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.5.dp, RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFFFFDF8))
            .border(1.2.dp, GoldAccent.copy(alpha = 0.45f), RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1F2937)
        )
        Text(
            text = "∨",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

@Composable
private fun ToggleRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF0D4A3E),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFE5E7EB)
            )
        )
    }
}
