package com.tasbih.digitaltasbih.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object DuaColors {
    val Bg = Color(0xFFF6F4EE)
    val HeaderStart = Color(0xFF146450)
    val HeaderMid = Color(0xFF0C4A3A)
    val HeaderEnd = Color(0xFF062018)
    val HeaderGradient = Brush.linearGradient(listOf(HeaderStart, HeaderMid, HeaderEnd))

    val Emerald = Color(0xFF0E5D47)
    val EmeraldDark = Color(0xFF062018)
    val Gold = Color(0xFFC9A84C)
    val GoldLight = Color(0xFFF3D48A)

    val CardBg = Color(0xFFFFFFFF)
    val CardBorder = Color(0x220E5D47)
    val CardBorderGold = Color(0x55C9A84C)

    val TextDark = Color(0xFF1A1408)
    val TextMuted = Color(0xFF6B7280)
    val ArabicGreen = Color(0xFF042E22)

    val WhenNoteBg = Color(0x14C9A84C)
    val WhenNoteBorder = Color(0xFFC9A84C)

    val HowNoteBg = Color(0x120E5D47)
    val HowNoteBorder = Color(0xFF0E5D47)

    val TourGradient = Brush.linearGradient(
        listOf(
            Color(0xFF0E4435),
            Color(0xFF082D23),
            Color(0xFF051E17)
        )
    )
}