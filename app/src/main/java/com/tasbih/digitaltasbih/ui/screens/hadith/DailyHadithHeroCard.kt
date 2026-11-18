package com.tasbih.digitaltasbih.ui.screens.hadith

import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.ui.theme.AmiriQuranFont
import java.util.Locale

data class DailyFeaturedHadithData(
    val title: String,
    val arabic: String,
    val narrator: String,
    val bodyEn: String,
    val bodyHi: String,
    val bodyUr: String,
    val reference: String,
    val grade: String
) {
    fun getBody(lang: String): String = when (lang) {
        "hi" -> bodyHi
        "ur" -> bodyUr
        else -> bodyEn
    }
}

val DEFAULT_DAILY_HADITH = DailyFeaturedHadithData(
    title = "Hadith of the Day • आज की हदीस",
    arabic = "إِنَّمَا الأَعْمَالُ بِالنِّيَّاتِ، وَإِنَّمَا لِكُلِّ امْرِئٍ مَا نَوَى",
    narrator = "Narrated 'Umar bin Al-Khattab (RA):",
    bodyEn = "Actions are according to intentions, and everyone will get what was intended.",
    bodyHi = "अमल का दारोमदार नियतों पर है, और हर इंसान को वही मिलेगा जिसकी उसने नियत की।",
    bodyUr = "اعمال کا دارومدار نیتوں پر ہے اور ہر شخص کے لیے وہی ہے جس کی اس نے نیت کی۔",
    reference = "Sahih al-Bukhari 1, Sahih Muslim 1907",
    grade = "Muttafaqun 'Alayh (Sahih)"
)

@Composable
fun DailyHadithHeroCard(
    style: HadithHeroStyle,
    isDarkTheme: Boolean,
    selectedLang: String,
    isPlaying: Boolean,
    onSpeakClick: () -> Unit,
    onDesignClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val hadith = DEFAULT_DAILY_HADITH

    val cardBgBrush = when (style) {
        HadithHeroStyle.EMERALD_ARCH -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF0F5132),
                Color(0xFF0A3622)
            )
        )
        HadithHeroStyle.CLASSIC_IVORY -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFFFAF7F0),
                Color(0xFFF3ECE0)
            )
        )
        HadithHeroStyle.GOLD_MINIMAL -> Brush.verticalGradient(
            colors = listOf(
                if (isDarkTheme) Color(0xFF13271F) else Color(0xFFFCFAF6),
                if (isDarkTheme) Color(0xFF0C1914) else Color(0xFFF7F3EA)
            )
        )
    }

    val cardBorderColor = when (style) {
        HadithHeroStyle.EMERALD_ARCH -> Color(0xFFC9A84C)
        HadithHeroStyle.CLASSIC_IVORY -> Color(0x66C9A84C)
        HadithHeroStyle.GOLD_MINIMAL -> Color(0xFFE5C158)
    }

    val titleColor = when (style) {
        HadithHeroStyle.EMERALD_ARCH -> Color(0xFFFEF08A)
        HadithHeroStyle.CLASSIC_IVORY -> Color(0xFF0E5D47)
        HadithHeroStyle.GOLD_MINIMAL -> if (isDarkTheme) Color(0xFFFEF08A) else Color(0xFF0E5D47)
    }

    val arabicColor = when (style) {
        HadithHeroStyle.EMERALD_ARCH -> Color(0xFFFFFFFF)
        HadithHeroStyle.CLASSIC_IVORY -> Color(0xFF14532D)
        HadithHeroStyle.GOLD_MINIMAL -> if (isDarkTheme) Color(0xFFE6E2D8) else Color(0xFF14532D)
    }

    val bodyColor = when (style) {
        HadithHeroStyle.EMERALD_ARCH -> Color(0xFFE6E2D8)
        HadithHeroStyle.CLASSIC_IVORY -> Color(0xFF2C2416)
        HadithHeroStyle.GOLD_MINIMAL -> if (isDarkTheme) Color(0xFFD4C8B0) else Color(0xFF2C2416)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(22.dp))
            .clip(RoundedCornerShape(22.dp))
            .background(cardBgBrush)
            .border(1.5.dp, cardBorderColor, RoundedCornerShape(22.dp))
            .padding(18.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row: [Badge & Stars] ... [Theme Switcher] [Audio Recite] [Share]
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (style == HadithHeroStyle.EMERALD_ARCH) Color(0x33000000)
                            else Color(0xFF0E5D47)
                        )
                        .border(1.dp, Color(0xFFC9A84C), RoundedCornerShape(20.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "✨ DAILY HADITH",
                        color = Color.White,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                // Action Buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Design Switcher Button (hadithHeroDesignModal)
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                if (style == HadithHeroStyle.EMERALD_ARCH) Color(0x33FFFFFF)
                                else Color(0xFFF3ECE0)
                            )
                            .border(1.dp, Color(0x44C9A84C), CircleShape)
                            .clickable { onDesignClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = "Design Style",
                            tint = if (style == HadithHeroStyle.EMERALD_ARCH) Color(0xFFE8C878) else Color(0xFF0E5D47),
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Text-To-Speech Audio Recite Button (hadithCardSpeakClicked)
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                if (isPlaying) Color(0xFFC9A84C)
                                else if (style == HadithHeroStyle.EMERALD_ARCH) Color(0x33FFFFFF)
                                else Color(0xFFF3ECE0)
                            )
                            .border(1.dp, Color(0x44C9A84C), CircleShape)
                            .clickable { onSpeakClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isPlaying) "⏸" else "🔊",
                            fontSize = 13.sp
                        )
                    }

                    // Share Button
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                if (style == HadithHeroStyle.EMERALD_ARCH) Color(0x33FFFFFF)
                                else Color(0xFFF3ECE0)
                            )
                            .border(1.dp, Color(0x44C9A84C), CircleShape)
                            .clickable {
                                val shareTxt = "${hadith.arabic}\n\n${hadith.narrator}\n${hadith.getBody(selectedLang)}\n\nReference: ${hadith.reference}\n— Digital Tasbih App"
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, shareTxt)
                                }
                                context.startActivity(Intent.createChooser(intent, "Share Daily Hadith"))
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Send,
                            contentDescription = "Share",
                            tint = if (style == HadithHeroStyle.EMERALD_ARCH) Color(0xFFE8C878) else Color(0xFF0E5D47),
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Arabic Matn (RTL)
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                Text(
                    text = hadith.arabic,
                    color = arabicColor,
                    fontSize = 20.sp,
                    fontFamily = AmiriQuranFont,
                    lineHeight = 32.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "✦ ★ ✦",
                    color = Color(0xFFC9A84C),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Narrator Attribution
            Text(
                text = hadith.narrator,
                color = if (style == HadithHeroStyle.EMERALD_ARCH) Color(0xFFE8C878) else Color(0xFF7A6F65),
                fontSize = 12.5.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Meaning / Translation Text
            Text(
                text = hadith.getBody(selectedLang),
                color = bodyColor,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(
                color = if (style == HadithHeroStyle.EMERALD_ARCH) Color(0x33C9A84C) else Color(0x220E5D47),
                thickness = 1.dp
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Footer: Reference & Authenticity Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📚 ${hadith.reference}",
                    color = if (style == HadithHeroStyle.EMERALD_ARCH) Color(0xFFE8C878) else Color(0xFF0E5D47),
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (style == HadithHeroStyle.EMERALD_ARCH) Color(0x44000000)
                            else Color(0xFFE8F5E9)
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "✓ ${hadith.grade}",
                        color = if (style == HadithHeroStyle.EMERALD_ARCH) Color(0xFF86EFAC) else Color(0xFF1B5E20),
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
