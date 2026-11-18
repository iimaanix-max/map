package com.tasbih.digitaltasbih.ui.screens.salah

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.ui.theme.AmiriQuranFont
import kotlinx.coroutines.delay

data class SalahQuoteItem(
    val arabic: String,
    val translationEn: String,
    val translationHi: String,
    val translationUr: String,
    val source: String,
    val grade: String
) {
    fun getTranslation(lang: String): String = when {
        lang.startsWith("hi") -> translationHi
        lang.startsWith("ur") -> translationUr
        else -> translationEn
    }
}

val SALAH_QUOTES = listOf(
    SalahQuoteItem(
        arabic = "إِنَّ الصَّلَاةَ كَانَتْ عَلَى الْمُؤْمِنِينَ كِتَابًا مَّوْقُوتًا",
        translationEn = "Indeed, prayer has been decreed upon the believers a decree of specified times.",
        translationHi = "बेशक नमाज़ मोमिनों पर मुक़र्ररा वक़्तों में फ़र्ज़ की गई है।",
        translationUr = "یقیناً نماز مومنوں پر مقررہ اوقات میں فرض کی گئی ہے۔",
        source = "Surah An-Nisa 4:103",
        grade = "Noble Qur'an"
    ),
    SalahQuoteItem(
        arabic = "رَأْسُ الأَمْرِ الإِسْلاَمُ وَعَمُودُهُ الصَّلاَةُ",
        translationEn = "The head of the matter is Islam, and its main pillar is prayer.",
        translationHi = "दीन का सर इस्लाम है और उसका सबसे बड़ा सुतून (स्तंभ) नमाज़ है।",
        translationUr = "دین کی اصل اسلام ہے اور اس کا ستون نماز ہے۔",
        source = "Jami` at-Tirmidhi 2616",
        grade = "✓ Sahih"
    ),
    SalahQuoteItem(
        arabic = "أَحَبُّ الأَعْمَالِ إِلَى اللَّهِ الصَّلاَةُ عَلَى وَقْتِهَا",
        translationEn = "The deed most beloved to Allah is offering prayer at its proper time.",
        translationHi = "अल्लाह को सबसे ज़्यादा महबूब अमल नमाज़ को उसके वक़्त पर पढ़ना है।",
        translationUr = "اللہ کے نزدیک سب سے پسندیدہ عمل اپنے وقت پر نماز پڑھنا ہے۔",
        source = "Sahih al-Bukhari 527, Muslim 85",
        grade = "✓ Muttafaqun 'Alayh"
    )
)

@Composable
fun SalahQuoteCarousel(
    selectedLang: String,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier
) {
    var activeIndex by remember { mutableIntStateOf(0) }

    // Auto-slide every 6 seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(6000L)
            activeIndex = (activeIndex + 1) % SALAH_QUOTES.size
        }
    }

    val currentQuote = SALAH_QUOTES[activeIndex]

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(20.dp), spotColor = Color(0x66000000))
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF063327), // Deep Dark Emerald Green
                        Color(0xFF032219)  // Rich Midnight Emerald
                    )
                )
            )
            .border(
                1.2.dp,
                Color(0xFFC9A84C).copy(alpha = 0.45f),
                RoundedCornerShape(20.dp)
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Pill: 🕌 VIRTUE OF SALAH & Grade Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF0C4A3A))
                        .border(1.dp, Color(0xFF10B981).copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 9.dp, vertical = 3.5.dp)
                ) {
                    Text(
                        text = "🕌 VIRTUE OF SALAH",
                        color = Color(0xFFFDE047),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0x2210B981))
                        .border(1.dp, Color(0x4434D399), RoundedCornerShape(20.dp))
                        .padding(horizontal = 9.dp, vertical = 2.5.dp)
                ) {
                    Text(
                        text = currentQuote.grade,
                        color = Color(0xFF34D399),
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Animated Quote Content
            AnimatedContent(
                targetState = activeIndex,
                transitionSpec = {
                    (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                        slideOutHorizontally { width -> -width } + fadeOut()
                    )
                },
                label = "SalahQuoteTransition"
            ) { targetIndex ->
                val quote = SALAH_QUOTES[targetIndex]
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Arabic Matn (RTL) - GOLDEN
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                        Text(
                            text = quote.arabic,
                            color = Color(0xFFFCD34D), // Golden Arabic
                            fontSize = 18.5.sp,
                            fontFamily = AmiriQuranFont,
                            lineHeight = 30.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Translation - PURE WHITE
                    Text(
                        text = "“${quote.getTranslation(selectedLang)}”",
                        color = Color.White, // White translation
                        fontSize = 13.5.sp,
                        lineHeight = 19.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Source - WARM GOLD
                    Text(
                        text = "— ${quote.source}",
                        color = Color(0xFFFDE68A),
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Pagination Dots (salahQuoteGoto)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SALAH_QUOTES.indices.forEach { index ->
                    val isSelected = index == activeIndex
                    Box(
                        modifier = Modifier
                            .size(if (isSelected) 20.dp else 6.dp, 6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(
                                if (isSelected) Color(0xFFF59E0B)
                                else Color.White.copy(alpha = 0.35f)
                            )
                            .clickable { activeIndex = index }
                    )
                }
            }
        }
    }
}
