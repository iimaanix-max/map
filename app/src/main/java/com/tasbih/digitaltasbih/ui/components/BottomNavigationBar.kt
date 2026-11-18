package com.tasbih.digitaltasbih.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.tasbih.digitaltasbih.ui.theme.GoldAccent

data class NavTabItem(
    val id: String,
    val title: String,
    val assetPath: String
)

@Composable
fun BottomNavigationBar(
    activeTabId: String = "home",
    isDarkTheme: Boolean = false,
    onTabSelected: (String) -> Unit
) {
    val tabs = listOf(
        NavTabItem("home", "Home", "media/ec611a54dd82c8cf.webp"),
        NavTabItem("tasbih", "Tasbih", "media/d919e45c30b26c4a.webp"),
        NavTabItem("quran", "Quran", "media/5bdedbbc5411d579.webp"),
        NavTabItem("calendar", "Calendar", "media/a34df81088b13a18.webp"),
        NavTabItem("dua", "Dua", "media/054e2d139b8619a8.webp")
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isDarkTheme) Color(0xFF0A1B14) else Color(0xFFF9F7F2))
            .border(1.dp, if (isDarkTheme) Color(0x44F3D48A) else Color(0x33A07828))
            .navigationBarsPadding()
            .padding(vertical = 6.dp, horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { tab ->
                val isActive = tab.id == activeTabId

                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isActive) Color(0xFF104434) else Color.Transparent)
                        .border(
                            width = if (isActive) 1.5.dp else 0.dp,
                            color = if (isActive) Color(0xFFC9A84C) else Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { onTabSelected(tab.id) },
                    contentAlignment = Alignment.Center
                ) {
                    val iconPainter = rememberAssetPainter(tab.assetPath)
                    Image(
                        painter = iconPainter,
                        contentDescription = tab.title,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}
