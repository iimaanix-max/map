package com.tasbih.digitaltasbih.ui.components.dua

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.ui.theme.DuaColors

data class DuaBottomNavTab(val id: String, val icon: ImageVector, val label: String)

@Composable
fun DuaBottomNav(
    activeTab: String,
    onTabSelected: (String) -> Unit,
    onTabPositioned: ((tabId: String, LayoutCoordinates) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val tabs = listOf(
        DuaBottomNavTab("home", Icons.Outlined.Home, "Home"),
        DuaBottomNavTab("my_dua", Icons.Outlined.Edit, "My Dua"),
        DuaBottomNavTab("my_lists", Icons.Outlined.BookmarkBorder, "My Lists")
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(12.dp)
            .background(Color(0xFFFFFFFF))
            .border(1.dp, Color(0x33C9A84C))
            .navigationBarsPadding()
            .padding(vertical = 6.dp, horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { tab ->
                val isActive = tab.id == activeTab

                Column(
                    modifier = Modifier
                        .onGloballyPositioned { coords ->
                            onTabPositioned?.invoke(tab.id, coords)
                        }
                        .clickable { onTabSelected(tab.id) }
                        .padding(horizontal = 20.dp, vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.label,
                        tint = if (isActive) DuaColors.Emerald else DuaColors.TextMuted,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = tab.label,
                        color = if (isActive) DuaColors.Emerald else DuaColors.TextMuted,
                        fontSize = 12.sp,
                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }
    }
}
