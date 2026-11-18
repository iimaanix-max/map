package com.tasbih.digitaltasbih.ui.components.dua

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.ui.theme.DuaColors

data class DuaQuote(
    val arabic: String,
    val translation: String,
    val surah: String
)

@Composable
fun DuaInspirationBanner(
    modifier: Modifier = Modifier
) {
    val quotes = listOf(
        DuaQuote(
            arabic = "ادْعُونِي أَسْتَجِبْ لَكُمْ",
            translation = "\"Call upon Me, I will respond to you.\"",
            surah = "— Surah Ghafir (40:60)"
        ),
        DuaQuote(
            arabic = "وَإِذَا سَأَلَكَ عِبَادِي عَنِّي فَإِنِّي قَرِيبٌ",
            translation = "\"When My servants ask about Me, I am indeed near.\"",
            surah = "— Surah Al-Baqarah (2:186)"
        ),
        DuaQuote(
            arabic = "إِنَّ مَعَ الْعُسْرِ يُسْرًا",
            translation = "\"Indeed, with hardship comes ease.\"",
            surah = "— Surah Ash-Sharh (94:6)"
        )
    )

    var currentIndex by remember { mutableIntStateOf(0) }
    val currentQuote = quotes[currentIndex]

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(DuaColors.HeaderGradient)
            .border(1.5.dp, Color(0x66C9A84C), RoundedCornerShape(20.dp))
            .clickable {
                currentIndex = (currentIndex + 1) % quotes.size
            }
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Top row: icon left, Arabic calligraphy right
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color(0x33FFFFFF))
                        .border(1.dp, Color(0x66F3D48A), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🕌", fontSize = 15.sp)
                }

                Text(
                    text = currentQuote.arabic,
                    color = Color(0xFFF3D48A),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Center: English quote
            Text(
                text = currentQuote.translation,
                color = Color.White,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Surah reference
            Text(
                text = currentQuote.surah,
                color = Color(0xFFF3D48A),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Carousel dots
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                quotes.indices.forEach { index ->
                    val isActive = index == currentIndex
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .size(width = if (isActive) 16.dp else 6.dp, height = 6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(if (isActive) Color(0xFFF3D48A) else Color(0x66FFFFFF))
                    )
                }
            }
        }
    }
}