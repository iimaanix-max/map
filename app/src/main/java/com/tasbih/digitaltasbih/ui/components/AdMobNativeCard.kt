package com.tasbih.digitaltasbih.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.ui.theme.EmeraldPrimary
import com.tasbih.digitaltasbih.ui.theme.GoldAccent
import com.tasbih.digitaltasbih.ui.theme.GoldBorder
import com.tasbih.digitaltasbih.ui.theme.TextSecondaryDay

@Composable
fun AdMobNativeCard(
    isAdLoaded: Boolean = true
) {
    if (com.tasbih.digitaltasbih.util.AdConfig.DISABLE_ALL_ADS_FOR_SCREENSHOTS) return
    if (!isAdLoaded) return

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.2.dp, GoldBorder, RoundedCornerShape(16.dp))
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(GoldAccent)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "SPONSORED",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Google AdMob Native Card",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = EmeraldPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Hardware-accelerated native ad container",
                fontSize = 11.sp,
                color = TextSecondaryDay
            )
        }
    }
}
