package com.tasbih.digitaltasbih.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = EmeraldPrimary,
    onPrimary = Color.White,
    secondary = GoldAccent,
    onSecondary = Color.Black,
    background = BackgroundDay,
    onBackground = TextPrimaryDay,
    surface = CardSurfaceDay,
    onSurface = TextPrimaryDay,
)

private val DarkColorScheme = darkColorScheme(
    primary = EmeraldPrimary,
    onPrimary = Color.White,
    secondary = GoldAccent,
    onSecondary = Color.Black,
    background = BackgroundNight,
    onBackground = TextPrimaryNight,
    surface = CardSurfaceNight,
    onSurface = TextPrimaryNight,
)

@Composable
fun DigitalTasbihTheme(
    darkTheme: Boolean = false, // Day theme default matching user requirement
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
