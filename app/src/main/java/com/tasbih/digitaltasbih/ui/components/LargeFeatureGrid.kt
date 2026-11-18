package com.tasbih.digitaltasbih.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.data.AppStrings
import com.tasbih.digitaltasbih.ui.theme.GoldAccent

import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import androidx.compose.ui.platform.LocalContext
import com.tasbih.digitaltasbih.data.AdhkarRepository
import com.tasbih.digitaltasbih.data.jummah.JummahRepository

data class LargeTileData(
    val id: String,
    val title: String,
    val assetPath: String,
    val hasRedDot: Boolean = false
)

/**
 * 4x4 Grid of Large Feature Buttons
 * When Jummah is active: Row 1 has Jummah Mubarak, Salah Tracker, Morning Adhkar, Evening Adhkar
 */
@Composable
fun LargeFeatureGrid(
    strings: AppStrings,
    onTileClick: (String) -> Unit
) {
    val context = LocalContext.current
    val jummahTrigger = JummahRepository.updateTrigger.value
    val adhkarTrigger = AdhkarRepository.updateTrigger.value

    // Auto-refresh visibility every 30 seconds for real-time Maghrib & prayer transitions
    LaunchedEffect(Unit) {
        while (isActive) {
            delay(30_000L)
            JummahRepository.updateTrigger.value = System.currentTimeMillis()
            AdhkarRepository.updateTrigger.value = System.currentTimeMillis()
        }
    }

    val isJummahVisible = remember(context, jummahTrigger) {
        JummahRepository.isJummahTileVisible(context)
    }
    val isJummahUnread = remember(context, jummahTrigger) {
        JummahRepository.isJummahSunnahUnread(context)
    }
    val isMorningAdhkarUnread = remember(context, adhkarTrigger) {
        AdhkarRepository.isMorningAdhkarUnfinishedNow(context)
    }
    val isEveningAdhkarUnread = remember(context, adhkarTrigger) {
        AdhkarRepository.isEveningAdhkarUnfinishedNow(context)
    }

    val row1 = remember(strings, isJummahVisible, isJummahUnread, isMorningAdhkarUnread, isEveningAdhkarUnread) {
        if (isJummahVisible) {
            listOf(
                LargeTileData("jummah", strings.jummahMubarak, "media/tile_jummah_mubarak.webp", hasRedDot = isJummahUnread),
                LargeTileData("tracker", strings.salahTracker, "media/cd777c476662f26f.webp"),
                LargeTileData("morning_adhkar", strings.morningAdhkar, "media/4b0b3782cfc16b4b.webp", hasRedDot = isMorningAdhkarUnread),
                LargeTileData("evening_adhkar", strings.eveningAdhkar, "media/2ba4960e82735607.webp", hasRedDot = isEveningAdhkarUnread)
            )
        } else {
            listOf(
                LargeTileData("tracker", strings.salahTracker, "media/cd777c476662f26f.webp"),
                LargeTileData("morning_adhkar", strings.morningAdhkar, "media/4b0b3782cfc16b4b.webp", hasRedDot = isMorningAdhkarUnread),
                LargeTileData("evening_adhkar", strings.eveningAdhkar, "media/2ba4960e82735607.webp", hasRedDot = isEveningAdhkarUnread),
                LargeTileData("daily", strings.dhikrTracker, "media/454ff52ffff9a617.webp")
            )
        }
    }

    val row2 = listOf(
        LargeTileData("quran", strings.quran, "media/5fbfa4f9cb23d6b2.webp"),
        LargeTileData("prayer", strings.prayerTimes, "media/bd512d13aad9c8fd.webp", hasRedDot = true),
        LargeTileData("qibla", strings.qibla, "media/a623ce3fad98a23d.webp"),
        LargeTileData("tasbih", strings.tasbih, "media/59329dcad16a9eac.webp")
    )

    val row3 = listOf(
        LargeTileData("names99", strings.names99, "media/bc58ef05da0da4cb.webp"),
        LargeTileData("hadith", strings.hadith, "media/ba13638e21fd68a9.webp"),
        LargeTileData("audio", strings.quranAudio, "media/9588f2cd2da27fd8.webp"),
        LargeTileData("mosque", strings.findMosque, "media/2ac68dc80764ef75.webp")
    )

    var isMoreExpanded by remember { mutableStateOf(false) }

    val row4 = listOf(
        LargeTileData("calendar", strings.islamicCalendar, "media/24b814cf08d6fe3c.webp"),
        LargeTileData("azan", strings.adhan, "media/aea9d1273d13188c.webp"),
        LargeTileData("dua", strings.duaTile, "media/20eccdc2c990c9c2.webp"),
        LargeTileData("more", if (isMoreExpanded) "${strings.more} ▴" else "${strings.more} ▾", "media/tile_more.webp")
    )

    val infiniteTransition = rememberInfiniteTransition(label = "largeTileNotifPulse")
    val notifDotScale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 850, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "largeTileNotifDotScale"
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Row 1
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            row1.forEach { tile ->
                LargeTileItem(tile = tile, notifDotScale = notifDotScale, onClick = { onTileClick(tile.id) })
            }
        }

        // Row 2
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            row2.forEach { tile ->
                LargeTileItem(tile = tile, notifDotScale = notifDotScale, onClick = { onTileClick(tile.id) })
            }
        }

        // Row 3
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            row3.forEach { tile ->
                LargeTileItem(tile = tile, notifDotScale = notifDotScale, onClick = { onTileClick(tile.id) })
            }
        }

        // Row 4 (Islamic Calendar, Adhan, Dua, More)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            row4.forEach { tile ->
                LargeTileItem(
                    tile = tile,
                    notifDotScale = notifDotScale,
                    onClick = {
                        if (tile.id == "more") {
                            isMoreExpanded = !isMoreExpanded
                        } else {
                            onTileClick(tile.id)
                        }
                    }
                )
            }
        }

        // Expanded Rows for More Section (Pillars of Islam, Live Kaaba, Zakat Calculator)
        AnimatedVisibility(
            visible = isMoreExpanded,
            enter = expandVertically(animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)),
            exit = shrinkVertically(animationSpec = tween(250)) + fadeOut(animationSpec = tween(250))
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Row 5: Live Kaaba, Zakat, Shahada, Salah
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // 1. Live Kaaba
                    LargeTileItem(
                        tile = LargeTileData("live", strings.liveKaaba, "media/ebd990316e7041a6.webp"),
                        notifDotScale = notifDotScale,
                        onClick = { onTileClick("live") }
                    )
                    // 2. Zakat Calculator
                    LargeTileItem(
                        tile = LargeTileData("zakat", strings.zakat, "media/tile_zakat.webp"),
                        notifDotScale = notifDotScale,
                        onClick = { onTileClick("zakat") }
                    )
                    // 3. Shahada (The 1st Pillar of Islam)
                    LargeTileItem(
                        tile = LargeTileData("shahada", strings.shahada, "media/tile_shahada.webp"),
                        notifDotScale = notifDotScale,
                        onClick = { onTileClick("shahada") }
                    )
                    // 4. Salah (The 2nd Pillar of Islam)
                    LargeTileItem(
                        tile = LargeTileData("salah_pillar", strings.salah, "media/tile_salah.webp"),
                        notifDotScale = notifDotScale,
                        onClick = { onTileClick("salah_pillar") }
                    )
                }

                // Row 6: Sawm (Fasting), Hajj, + Spacers
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // 1. Sawm / Fasting (The 4th Pillar of Islam)
                    LargeTileItem(
                        tile = LargeTileData("sawm", strings.sawm, "media/tile_sawm.webp"),
                        notifDotScale = notifDotScale,
                        onClick = { onTileClick("sawm") }
                    )
                    // 2. Hajj (The 5th Pillar of Islam)
                    LargeTileItem(
                        tile = LargeTileData("hajj", strings.hajj, "media/tile_hajj.webp"),
                        notifDotScale = notifDotScale,
                        onClick = { onTileClick("hajj") }
                    )
                    // 3. Empty slot spacer for 4-column symmetry
                    Spacer(modifier = Modifier.weight(1f))
                    // 4. Empty slot spacer for 4-column symmetry
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun RowScope.LargeTileItem(
    tile: LargeTileData,
    notifDotScale: Float,
    onClick: () -> Unit
) {
    val emeraldTileGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF134A38),
            Color(0xFF0A2B20)
        )
    )

    Box(
        modifier = Modifier
            .weight(1f)
            .height(110.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(emeraldTileGradient)
            .border(1.3.dp, GoldAccent, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Large 3D Icon
            val iconPainter = rememberAssetPainter(tile.assetPath)
            Image(
                painter = iconPainter,
                contentDescription = tile.title,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(62.dp)
            )

            Spacer(modifier = Modifier.height(3.dp))

            // Localized Title
            Text(
                text = tile.title,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Top-Right BLINKING RED DOT (on Namaz Time and Morning Adhkar)
        if (tile.hasRedDot) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(3.dp)
                    .graphicsLayer {
                        scaleX = notifDotScale
                        scaleY = notifDotScale
                    }
                    .size(10.dp)
                    .shadow(elevation = 6.dp, shape = CircleShape, spotColor = Color(0xFFEF4444))
                    .clip(CircleShape)
                    .background(Color(0xFFEF4444))
                    .border(1.5.dp, Color.White, CircleShape)
            )
        }
    }
}
