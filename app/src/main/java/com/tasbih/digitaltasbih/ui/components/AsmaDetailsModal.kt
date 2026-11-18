package com.tasbih.digitaltasbih.ui.components

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.compose.ui.zIndex
import com.tasbih.digitaltasbih.data.AsmaNameItem
import com.tasbih.digitaltasbih.ui.theme.AmiriQuranFont

/**
 * Pixel-Perfect Asma-ul-Husna Details Modal faithfully matching Screenshots 2 & 3.
 * Features:
 * 1. Centered Arabic calligraphy in authentic AmiriQuranFont, bold transliteration & localized meaning.
 * 2. 📜 AUTHENTIC HADITH REFERENCE card (Bukhari 2736 & Muslim 2677).
 * 3. 📖 QURANIC COMMAND card with Quranic calligraphy & English/Hindi/Urdu translation (Surah Al-A'raf 7:180).
 * 4. 🤲 Spiritual Virtues & Benefits with 3 key dimensions (Divine Closeness, Acceptance, Barakah).
 * 5. Interactive Action Buttons: "📿 Count in Tasbih" and "📋 Share Details".
 */
@Composable
fun AsmaDetailsModal(
    asma: AsmaNameItem,
    selectedLanguage: String = "en",
    onDismiss: () -> Unit,
    onCountInTasbih: (AsmaNameItem) -> Unit
) {
    val context = LocalContext.current
    val lang = selectedLanguage.lowercase()

    // ── Localized Content Dictionary ──
    val hadithHeader = when (lang) {
        "hi" -> "📜 प्रामाणिक हदीस का संदर्भ"
        "ur" -> "📜 صحیح حدیث مبارکہ کا حوالہ"
        "ar" -> "📜 الحديث النبوي الشريف"
        "bn" -> "📜 সহীহ হাদিসের দলিল"
        else -> "📜 AUTHENTIC HADITH REFERENCE"
    }

    val hadithQuote = when (lang) {
        "hi" -> "\"अल्लाह तआला के निन्यानवे (99) नाम हैं, यानी एक कम सौ; जिसने इन्हें याद रखा, समझा और इन पर अमल किया, वह जन्नत में दाख़िल होगा।\""
        "ur" -> "\"اللہ تعالیٰ کے ننانوے (99) نام ہیں یعنی ایک کم سو؛ جس نے انہیں یاد رکھا، سمجھا اور ان کا حق ادا کیا وہ جنت میں داخل ہو گا۔\""
        "ar" -> "\"إِنَّ لِلَّهِ تِسْعَةً وَتِسْعِينَ اسْمًا مِائَةً إِلَّا وَاحِدًا، مَنْ أَحْصَاهَا دَخَلَ الْجَنَّةَ.\""
        "bn" -> "\"নিশ্চয়ই আল্লাহর নিরানব্বইটি (৯৯) নাম রয়েছে, অর্থাৎ এক কম একশত; যে ব্যক্তি এগুলো মুখস্থ করবে, অনুধাবন করবে ও আমল করবে, সে জান্নাতে প্রবেশ করবে।\""
        else -> "\"Allah has ninety-nine names, one hundred less one; whoever memorizes, understands, and acts upon them will enter Paradise.\""
    }

    val hadithCite = when (lang) {
        "hi" -> "— सहीह अल-बुख़ारी 2736 व सहीह मुस्लिम 2677"
        "ur" -> "— صحیح البخاری 2736 و صحیح مسلم 2677"
        "ar" -> "— صحيح البخاري: 2736 وصحيح مسلم: 2677"
        "bn" -> "— সহীহ বুখারী ২৭৩৬ ও সহীহ মুসলিম ২৬৭৭"
        else -> "— Sahih al-Bukhari 2736 & Sahih Muslim 2677"
    }

    val quranHeader = when (lang) {
        "hi" -> "📖 क़ुरआनी हुक्म"
        "ur" -> "📖 قرآنی حکم"
        "ar" -> "📖 الأمر القرآني العظيم"
        "bn" -> "📖 কুরআনের মহিমান্বিত নির্দেশ"
        else -> "📖 QURANIC COMMAND"
    }

    val quranVerseText = when (lang) {
        "hi" -> "\"और अच्छे-अच्छे नाम अल्लाह ही के हैं, पस उन्हीं नामों से उसको पुकारो।\" — (सूरह अल-अ'राफ़ 7:180)"
        "ur" -> "\"اور اللہ ہی کے لیے بہترین نام ہیں، پس اس کو انہی ناموں سے پکارو۔\" — (سورۃ الاعراف 7:180)"
        "ar" -> "وَلِلَّهِ الْأَسْمَاءُ الْحُسْنَىٰ فَادْعُوهُ بِهَا — (سورة الأعراف: 180)"
        "bn" -> "\"আর আল্লাহর জন্যই রয়েছে সুন্দর সুন্দর নাম, কাজেই তোমরা তাঁকে সেসব নামেই ডাক।\" — (সূরা আল-আ'রাফ ৭:১৮০)"
        else -> "\"And to Allah belong the best names, so invoke Him by them.\" — (Surah Al-A'raf 7:180)"
    }

    val virtuesTitle = when (lang) {
        "hi" -> "🤲 ${asma.transliteration} पढ़ने की फ़ज़ीलत व आध्यात्मिक बरकतें"
        "ur" -> "🤲 ${asma.transliteration} ورد کرنے کی فضیلت اور روحانی برکات"
        "ar" -> "🤲 فضل الذكر والبركات الروحية لاسم ${asma.arabic}"
        "bn" -> "🤲 ${asma.transliteration} পাঠ করার ফজিলত ও আত্মিক বরকত"
        else -> "🤲 Spiritual Virtues & Benefits of Calling upon ${asma.transliteration}"
    }

    val benefitsList: List<Pair<String, String>> = when (lang) {
        "hi" -> listOf(
            Pair("रब की क़ुर्बत:", "अल्लाह तआला को उसकी ख़ास सिफ़त से पुकारना दिल को बेपनाह सुकून, इत्मीनान और यक़ीन अता करता है।"),
            Pair("दुआओं की क़बूलियत:", "नबी करीम (ﷺ) ने रंज-ओ-ग़म और तंगदस्ती से नजात के लिए अल्लाह के अस्मा-उल-हुस्ना के वसीले से दुआ मांगने की ताकीद फ़रमाई है।"),
            Pair("ज़िक्र की बरकत:", "रोज़ाना तस्बीह में इस मुबारक़ नाम का विर्द करने से दिल की घबराहट दूर होती है और ईमान ताज़ा होता है।")
        )
        "ur" -> listOf(
            Pair("قربِ الٰہی:", "اللہ تعالیٰ کو اس کی خاص صفت سے پکارنا دل کو بے مثال سکون، اطمینان اور روحانی نور بخشتا ہے۔"),
            Pair("دعاؤں کی قبولیت:", "نبی کریم (ﷺ) نے رنج و غم اور مشکلات سے نجات کے لیے اللہ کے خوبصورت ناموں سے دعا مانگنے کی ترغیب دی ہے۔"),
            Pair("ذکر کی برکت:", "روزانہ تسبیح میں اس مبارک نام کا ورد کرنے سے دنیاوی پریشانیاں دور اور ایمان مضبوط ہوتا ہے۔")
        )
        "ar" -> listOf(
            Pair("القرب من الله:", "دعاء الله بأسمائه الحسنى يملأ القلب طمأنينة وسكينة ويقينًا خالصًا."),
            Pair("استجابة الدعاء:", "حث النبي ﷺ على التوسل بأسماء الله الحسنى لتفريج الكروب وقضاء الحوائج."),
            Pair("بركة الذكر:", "الإكثار من ذكر هذا الاسم المبارك في التسبيح اليومي ينقي الروح ويزيد الإيمان.")
        )
        "bn" -> listOf(
            Pair("আল্লাহর নৈকট্য:", "মহান আল্লাহর সুন্দর নামে তাঁকে ডাকা অন্তরে গভীর শান্তি, প্রশান্তি ও দৃঢ় ঈমান দান করে।"),
            Pair("দুয়া কবুলিয়াত:", "রাসুলুল্লাহ (ﷺ) পেরেশানি ও সংকট মুক্তির জন্য আল্লাহর সুন্দরতম নামের ওসিলায় দুয়া করতে উৎসাহিত করেছেন।"),
            Pair("যিকিরের বরকত:", "দৈনিক তসবিহে এই বরকতময় নামের যিকির দুনিয়াবি দুঃখ-কষ্ট দূর করে এবং অন্তরের নূর বৃদ্ধি করে।")
        )
        else -> listOf(
            Pair("Divine Closeness:", "Calling upon Allah by His specific attribute brings deep peace, spiritual serenity, and certainty to the heart."),
            Pair("Acceptance of Supplication:", "The Prophet (ﷺ) encouraged supplicating to Allah by His Greatest and Most Beautiful Names for immediate relief from anxiety and hardship."),
            Pair("Barakah in Remembrance:", "Reciting this Name repeatedly in daily Tasbih cleanses the soul from worldly grief and increases faith.")
        )
    }

    val btnCountLabel = when (lang) {
        "hi" -> "📿 तस्बीह में पढ़ें"
        "ur" -> "📿 تسبیح میں پڑھیں"
        "ar" -> "📿 ذكر في التسبيح"
        "bn" -> "📿 তসবিহে পড়ুন"
        else -> "📿 Count in Tasbih"
    }

    val btnShareLabel = when (lang) {
        "hi" -> "📋 विवरण शेयर करें"
        "ur" -> "📋 تفصیل شیئر کریں"
        "ar" -> "📋 مشاركة الفضائل"
        "bn" -> "📋 বিস্তারিত শেয়ার করুন"
        else -> "📋 Share Details"
    }

    val meaningText = asma.meanings[lang]
        ?: asma.meanings["en"]
        ?: asma.meanings["hi"]
        ?: asma.meanings.values.firstOrNull()
        ?: asma.transliteration

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, dismissOnClickOutside = true)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.55f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss
                ),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
                    .heightIn(max = 700.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {} // Consume click inside card so clicking content doesn't dismiss
                    )
                    .shadow(elevation = 24.dp, shape = RoundedCornerShape(26.dp)),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFCFBF7)),
                border = BorderStroke(1.2.dp, Color(0xFFC9A84C).copy(alpha = 0.5f))
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 20.dp, vertical = 18.dp)
                    ) {
                        // 1. TOP HEADER SECTION (Arabic Calligraphy, Transliteration, Meaning)
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = asma.arabic,
                                fontFamily = AmiriQuranFont,
                                fontSize = 42.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0E5D47),
                                textAlign = TextAlign.Center,
                                lineHeight = 54.sp
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = asma.transliteration,
                                fontFamily = FontFamily.Serif,
                                fontSize = 23.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF1A2E26),
                                textAlign = TextAlign.Center,
                                letterSpacing = 0.4.sp
                            )

                            Spacer(modifier = Modifier.height(3.dp))

                            Text(
                                text = meaningText,
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF5D4D38),
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // 2. AUTHENTIC HADITH REFERENCE CARD
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFFBF8F2))
                                .border(1.dp, Color(0x66C9A84C), RoundedCornerShape(14.dp))
                                .padding(14.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = hadithHeader,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFB88E28),
                                    letterSpacing = 0.6.sp
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = hadithQuote,
                                    fontSize = 13.5.sp,
                                    fontStyle = FontStyle.Italic,
                                    color = Color(0xFF2D3748),
                                    lineHeight = 20.sp
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = hadithCite,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF0E5D47)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // 3. QURANIC COMMAND CARD
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFFDFBF7))
                                .border(1.dp, Color(0x66C9A84C), RoundedCornerShape(14.dp))
                                .padding(14.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = quranHeader,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFB88E28),
                                    letterSpacing = 0.6.sp
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "وَلِلَّهِ الْأَسْمَاءُ الْحُسْنَىٰ فَادْعُوهُ بِهَا",
                                    fontFamily = AmiriQuranFont,
                                    fontSize = 21.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0E5D47),
                                    textAlign = TextAlign.End,
                                    lineHeight = 32.sp,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = quranVerseText,
                                    fontSize = 13.sp,
                                    color = Color(0xFF4A5568),
                                    lineHeight = 19.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // 4. SPIRITUAL VIRTUES & BENEFITS
                        Text(
                            text = virtuesTitle,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF1A2E26)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        benefitsList.forEach { (label, desc) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = "• ",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0E5D47)
                                )
                                Text(
                                    text = buildAnnotatedString {
                                        withStyle(
                                            SpanStyle(
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF1A2E26)
                                            )
                                        ) {
                                            append("$label ")
                                        }
                                        withStyle(
                                            SpanStyle(
                                                color = Color(0xFF4A5568)
                                            )
                                        ) {
                                            append(desc)
                                        }
                                    },
                                    fontSize = 13.sp,
                                    lineHeight = 19.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // 5. INTERACTIVE ACTION BUTTONS (Count in Tasbih & Share Details)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Left Button: Count in Tasbih
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(Color(0xFF064E3B), Color(0xFF033A2A))
                                        )
                                    )
                                    .clickable {
                                        onDismiss()
                                        onCountInTasbih(asma)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = btnCountLabel,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFDE8A8)
                                )
                            }

                            // Right Button: Share Details
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFEDF7F2))
                                    .border(1.2.dp, Color(0xFF064E3B), RoundedCornerShape(12.dp))
                                    .clickable {
                                        val shareText = """
                                            ✨ Asma-ul-Husna (Name of Allah) ✨
                                            
                                            ${asma.arabic}
                                            ${asma.transliteration} — $meaningText
                                            
                                            📖 "And to Allah belong the best names, so invoke Him by them." (Surah Al-A'raf 7:180)
                                            📜 Sahih al-Bukhari 2736 & Sahih Muslim 2677
                                            
                                            📲 Digital Tasbih App
                                        """.trimIndent()
                                        val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                            type = "text/plain"
                                            putExtra(Intent.EXTRA_TEXT, shareText)
                                        }
                                        context.startActivity(Intent.createChooser(sendIntent, "Share Asma-ul-Husna"))
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = btnShareLabel,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF064E3B)
                                )
                            }
                        }
                    }

                    // Close 'X' Button on top-right (Rendered AFTER Column with high zIndex so clicks are never intercepted)
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(10.dp)
                            .zIndex(100f)
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0x220E5D47))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onDismiss
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color(0xFF0E5D47),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
