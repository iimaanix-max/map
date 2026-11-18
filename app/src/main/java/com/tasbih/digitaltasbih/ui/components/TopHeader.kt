package com.tasbih.digitaltasbih.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.data.AppStrings

@Composable
fun TopHeader(
    strings: AppStrings,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onOpenSettings: () -> Unit,
    onShare: () -> Unit,
    onSupport: () -> Unit
) {
    val headerGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF146450),
            Color(0xFF0C4A3A),
            Color(0xFF062018)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 6.dp, shape = RoundedCornerShape(18.dp))
                .clip(RoundedCornerShape(18.dp))
                .background(headerGradient)
                .border(1.5.dp, Color(0x8CE8C878), RoundedCornerShape(18.dp))
                .padding(horizontal = 12.dp, vertical = 9.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: Logo Badge + Title + Localized Subtitle
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val logoBadgeGradient = Brush.linearGradient(
                        colors = listOf(Color(0xFF1E8F6E), Color(0xFF083D32))
                    )

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(logoBadgeGradient)
                            .border(1.5.dp, Color(0xFFE8C878), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        val tasbihPainter = rememberAssetPainter("media/59329dcad16a9eac.webp")
                        Image(
                            painter = tasbihPainter,
                            contentDescription = "Logo",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(9.dp))

                    Column {
                        Text(
                            text = "DIGITAL TASBIH",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Serif,
                            color = Color.White,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = strings.appSubtitle,
                            fontSize = 10.sp,
                            color = Color(0xFFE8C878),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Right: 4 Small Square Buttons (Exact Image 2)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TopHeaderButton(icon = "⚙️", onClick = onOpenSettings, isDarkTheme = isDarkTheme)
                    TopHeaderButton(icon = "❤️", onClick = onSupport, isDarkTheme = isDarkTheme)
                    TopHeaderButton(icon = "🔗", onClick = onShare, isDarkTheme = isDarkTheme)
                    TopHeaderButton(
                        icon = if (isDarkTheme) "🌙" else "☀️",
                        onClick = onToggleTheme,
                        isDarkTheme = isDarkTheme
                    )
                }
            }
        }
    }
}

@Composable
fun TopHeaderButton(
    icon: String,
    onClick: () -> Unit,
    isActive: Boolean = false,
    isDarkTheme: Boolean = false
) {
    val bg = if (isActive) Color(0xFFF3D48A)
             else if (isDarkTheme) Color(0xFF1E382B)
             else Color.White

    val borderCol = if (isActive) Color(0xFFD4AF37)
                    else if (isDarkTheme) Color(0x44F3D48A)
                    else Color(0x33A07828)

    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(bg)
            .border(1.dp, borderCol, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text = icon, fontSize = 12.sp)
    }
}
