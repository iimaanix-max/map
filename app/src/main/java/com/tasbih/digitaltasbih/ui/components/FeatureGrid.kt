package com.tasbih.digitaltasbih.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import androidx.compose.ui.platform.LocalContext
import com.tasbih.digitaltasbih.data.AdhkarRepository
import com.tasbih.digitaltasbih.data.AppStrings
import com.tasbih.digitaltasbih.data.jummah.JummahRepository
import com.tasbih.digitaltasbih.ui.theme.GoldAccent

data class CompactTileData(
    val id: String,
    val title: String,
    val assetPath: String,
    val hasRedDot: Boolean = false,
    val isLiveBadge: Boolean = false,
    val hasLiveGlow: Boolean = false
)

@Composable
fun FeatureGrid(
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

    // Exact sliding buttons matching index.html with Jummah tile appearing before tracker
    val allTiles = remember(strings, isJummahVisible, isJummahUnread, isMorningAdhkarUnread, isEveningAdhkarUnread) {
        buildList {
            add(CompactTileData("quran", strings.quran, "media/5fbfa4f9cb23d6b2.webp"))
            add(CompactTileData("prayer", strings.prayerTimes, "media/bd512d13aad9c8fd.webp", hasRedDot = true))
            add(CompactTileData("qibla", strings.qibla, "media/a623ce3fad98a23d.webp"))
            add(CompactTileData("tasbih", strings.tasbih, "media/59329dcad16a9eac.webp"))
            add(CompactTileData(
                id = "groups",
                title = strings.liveDhikr,
                assetPath = "media/fe3d68e31a113bef.webp",
                isLiveBadge = true,
                hasLiveGlow = true
            ))
            if (isJummahVisible) {
                add(CompactTileData(
                    id = "jummah",
                    title = strings.jummahMubarak,
                    assetPath = "media/tile_jummah_mubarak.webp",
                    hasRedDot = isJummahUnread
                ))
            }
            add(CompactTileData("tracker", strings.salahTracker, "media/cd777c476662f26f.webp"))
            add(CompactTileData("calendar", strings.islamicCalendar, "media/24b814cf08d6fe3c.webp"))
            add(CompactTileData("azan", strings.adhan, "media/aea9d1273d13188c.webp"))
            add(CompactTileData("dua", strings.duaTile, "media/20eccdc2c990c9c2.webp"))
            add(CompactTileData("names99", strings.names99, "media/bc58ef05da0da4cb.webp"))
            add(CompactTileData("hadith", strings.hadith, "media/ba13638e21fd68a9.webp"))
            add(CompactTileData("audio", strings.quranAudio, "media/9588f2cd2da27fd8.webp"))
            add(CompactTileData("mosque", strings.findMosque, "media/2ac68dc80764ef75.webp"))
            add(CompactTileData("live", strings.liveKaaba, "media/ebd990316e7041a6.webp"))
            add(CompactTileData("zakat", strings.zakat, "media/tile_zakat.webp"))
            add(CompactTileData("shahada", strings.shahada, "media/tile_shahada.webp"))
            add(CompactTileData("salah_pillar", strings.salah, "media/tile_salah.webp"))
            add(CompactTileData("sawm", strings.sawm, "media/tile_sawm.webp"))
            add(CompactTileData("hajj", strings.hajj, "media/tile_hajj.webp"))
            add(CompactTileData("daily", strings.dhikrTracker, "media/454ff52ffff9a617.webp"))
            add(CompactTileData("morning_adhkar", strings.morningAdhkar, "media/4b0b3782cfc16b4b.webp", hasRedDot = isMorningAdhkarUnread))
            add(CompactTileData("evening_adhkar", strings.eveningAdhkar, "media/2ba4960e82735607.webp", hasRedDot = isEveningAdhkarUnread))
        }
    }

    // Infinite breathing/pulsing animation transitions
    val infiniteTransition = rememberInfiniteTransition(label = "featureAnimations")

    // 1. Red Notification Dot Pulse (0.82f -> 1.28f scale + alpha glow)
    val notifDotScale by infiniteTransition.animateFloat(
        initialValue = 0.82f,
        targetValue = 1.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 850, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "notifDotScale"
    )

    // 2. Live Dhikr Glowing Halo Roshni (0.30f -> 0.95f alpha)
    val liveGlowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.30f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "liveGlowAlpha"
    )

    // 3. Live Dhikr Button Breathing Scale (1.0f -> 1.035f)
    val liveButtonScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.035f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "liveButtonScale"
    )

    // 4. Live Badge Pulse (0.92f -> 1.12f)
    val liveBadgeScale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 750, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "liveBadgeScale"
    )

    // Horizontal sliding row with exactly 8.dp gaps and smooth scrolling
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 2.dp, vertical = 4.dp)
    ) {
        items(allTiles, key = { it.id }) { tile ->
            HorizontalTileItem(
                tile = tile,
                notifDotScale = notifDotScale,
                liveGlowAlpha = liveGlowAlpha,
                liveButtonScale = liveButtonScale,
                liveBadgeScale = liveBadgeScale,
                onClick = { onTileClick(tile.id) }
            )
        }
    }
}

@Composable
fun HorizontalTileItem(
    tile: CompactTileData,
    notifDotScale: Float,
    liveGlowAlpha: Float,
    liveButtonScale: Float,
    liveBadgeScale: Float,
    onClick: () -> Unit
) {
    val emeraldTileGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF134A38),
            Color(0xFF0A2B20)
        )
    )

    // Outer wrapper box (height 86dp) to accommodate the floating LIVE badge above the button and glowing aura
    Box(
        modifier = Modifier
            .width(62.dp)
            .height(86.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        // PEECHE SE ROSHNI (Radiant Red Glowing Light Halo behind Live Dhikr button)
        if (tile.hasLiveGlow) {
            Box(
                modifier = Modifier
                    .width(62.dp)
                    .height(78.dp)
                    .graphicsLayer {
                        scaleX = liveButtonScale * 1.16f
                        scaleY = liveButtonScale * 1.16f
                        alpha = liveGlowAlpha
                    }
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFEF4444),
                                Color(0xB3DC2626),
                                Color(0x33EF4444),
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(18.dp)
                    )
            )
        }

        // Main Button Surface
        Box(
            modifier = Modifier
                .width(62.dp)
                .height(78.dp)
                .graphicsLayer {
                    if (tile.hasLiveGlow) {
                        scaleX = liveButtonScale
                        scaleY = liveButtonScale
                    }
                }
                .shadow(
                    elevation = if (tile.hasLiveGlow) 8.dp else 3.dp,
                    shape = RoundedCornerShape(14.dp),
                    spotColor = if (tile.hasLiveGlow) Color(0xFFEF4444) else Color(0x33000000)
                )
                .clip(RoundedCornerShape(14.dp))
                .background(emeraldTileGradient)
                .border(
                    width = 1.2.dp,
                    color = if (tile.hasLiveGlow) Color(0xFFEF4444) else GoldAccent,
                    shape = RoundedCornerShape(14.dp)
                )
                .clickable(onClick = onClick)
                .padding(horizontal = 2.dp, vertical = 5.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(3.dp))

                // Icon (100% visible, centered and slightly larger!)
                val iconPainter = rememberAssetPainter(tile.assetPath)
                Image(
                    painter = iconPainter,
                    contentDescription = tile.title,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(45.dp)
                )

                Spacer(modifier = Modifier.height(3.dp))

                // Localized Title
                Text(
                    text = tile.title,
                    fontSize = 8.3.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Top-Right BLINKING RED DOT (chota-bada breathing animation)
            if (tile.hasRedDot) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 2.dp, end = 2.dp)
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

        // BUTTON KE UPAR FLOATING "● LIVE" BADGE (Ab logo bilkul nahi chhupega!)
        if (tile.isLiveBadge) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = 2.dp)
                    .graphicsLayer {
                        scaleX = liveBadgeScale
                        scaleY = liveBadgeScale
                    }
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(99.dp), spotColor = Color(0xFFEF4444))
                    .clip(RoundedCornerShape(99.dp))
                    .background(Color(0xFFDC2626))
                    .border(0.8.dp, Color.White.copy(alpha = 0.95f), RoundedCornerShape(99.dp))
                    .padding(horizontal = 5.dp, vertical = 1.dp)
            ) {
                Text(
                    text = "● LIVE",
                    fontSize = 6.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
        }
    }
}
