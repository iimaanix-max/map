package com.tasbih.digitaltasbih.ui.components.dua

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.ui.theme.DuaColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DuaFontControlBar(
    fontScale: Float,
    onFontScaleChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 1.dp, shape = RoundedCornerShape(50.dp), spotColor = Color(0x140E5D47))
            .clip(RoundedCornerShape(50.dp))
            .background(Color.White)
            .border(
                width = 1.dp,
                color = Color(0xFF0E5D47).copy(alpha = 0.2f),
                shape = RoundedCornerShape(50.dp)
            )
            .padding(horizontal = 10.dp, vertical = 3.dp),
        contentAlignment = Alignment.Center
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
                        onFontScaleChange((fontScale - 0.1f).coerceAtLeast(0.7f))
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "A-",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0E5D47)
                )
            }

            // Slim Custom Slider
            Slider(
                value = fontScale,
                onValueChange = onFontScaleChange,
                valueRange = 0.7f..1.8f,
                modifier = Modifier
                    .weight(1f)
                    .height(28.dp),
                thumb = {
                    Box(
                        modifier = Modifier
                            .size(15.dp)
                            .shadow(2.dp, CircleShape)
                            .background(Color(0xFF0E5D47), CircleShape)
                            .border(1.5.dp, Color.White, CircleShape)
                    )
                },
                track = { sliderState ->
                    SliderDefaults.Track(
                        sliderState = sliderState,
                        modifier = Modifier.height(3.5.dp),
                        colors = SliderDefaults.colors(
                            activeTrackColor = Color(0xFF0E5D47),
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
                        onFontScaleChange((fontScale + 0.1f).coerceAtMost(1.8f))
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "A+",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0E5D47)
                )
            }

            // Percentage indicator badge - tap to reset
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFEAF5EE))
                    .border(0.8.dp, Color(0xFF0E5D47).copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                    .clickable { onFontScaleChange(1.0f) }
                    .padding(horizontal = 7.dp, vertical = 2.5.dp)
            ) {
                Text(
                    text = "${(fontScale * 100).toInt()}%",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0E5D47)
                )
            }
        }
    }
}