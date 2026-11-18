package com.tasbih.digitaltasbih.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.tasbih.digitaltasbih.data.AllahNameData
import com.tasbih.digitaltasbih.ui.theme.AmiriQuranFont

/**
 * Pixel-Perfect Featured Name of Allah Luxury Card for Home Screen.
 * Exactly matches master design and user mockup:
 * 1. Top bar: Moroccan 8-point star badge with "99", title, and "View all →" pill.
 * 2. Middle Banner: Exact intricate geometric star background pattern (media/950b7d9c22577e8d.webp)
 *    with radiant golden calligraphy in AmiriQuranFont.
 * 3. Seam: 3.5dp gold gradient bar with overlapping Islamic arch crest medallion (media/1080e5e18bb8e70f.webp).
 * 4. Body Frame: Radial ivory canvas with flush top-left lantern (media/ba1fb7fc2276343b.webp),
 *    bottom-right mandala rosette (media/a3972f9bd8bc5f8d.webp), 8-point jewel ornament rows,
 *    transliteration, underline bar, meaning, and two pill action buttons (Play & Details).
 */
@Composable
fun FeaturedAllahNameCard(
    item: AllahNameData,
    isPlaying: Boolean = false,
    isDarkTheme: Boolean = false,
    isRamadanActive: Boolean = false,
    onViewAll: () -> Unit,
    onPlayAudio: () -> Unit,
    onOpenDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 3.5dp Gold Seam Gradient
    val goldSeamGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFFEAA7),
            Color(0xFFD4AF37),
            Color(0xFFA68122),
            Color(0xFF59420D)
        )
    )

    // Body radial ivory gradient
    val bodyRadialGradient = Brush.radialGradient(
        colors = listOf(
            Color(0xFFFFFFFF),
            Color(0xFFFAF8F2),
            Color(0xFFF4ECE0)
        )
    )

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(if (isDarkTheme) Color(0xFF071711) else Color.White)
                .border(1.5.dp, Color(0x80C9A84C), RoundedCornerShape(20.dp))
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
            // -------------------------------------------------------------
            // 1. TOP HEADER BAR (Luxurious Dark Emerald/Black in Night Mode)
            // -------------------------------------------------------------
            val headerBg = if (isDarkTheme) {
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0E2A1E),
                        Color(0xFF071711)
                    )
                )
            } else {
                Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        Color(0xFFFAF9F5)
                    )
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(brush = headerBg)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Title Box with Moroccan 8-point Star
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(onClick = onViewAll)
                ) {
                    MoroccanStarBadge(number = "99", isDarkTheme = isDarkTheme)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "99 Names of Allah",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Serif,
                        color = if (isDarkTheme) Color(0xFFFAF7EE) else Color(0xFF064E3B)
                    )
                }

                // "View all →" Pill Button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isDarkTheme) Color(0xFF143B2C) else Color(0xFFEDF7F2))
                        .border(
                            1.dp,
                            if (isDarkTheme) Color(0x80C9A84C) else Color(0x4D064E3B),
                            RoundedCornerShape(20.dp)
                        )
                        .clickable(onClick = onViewAll)
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "View all →",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color(0xFFFDE8A8) else Color(0xFF064E3B)
                    )
                }
            }

            // Subtle gold divider between header and calligraphy banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        if (isDarkTheme) {
                            Brush.horizontalGradient(
                                listOf(
                                    Color(0x1AC9A84C),
                                    Color(0x99C9A84C),
                                    Color(0x1AC9A84C)
                                )
                            )
                        } else {
                            Brush.horizontalGradient(
                                listOf(
                                    Color(0x00064E3B),
                                    Color(0x26064E3B),
                                    Color(0x00064E3B)
                                )
                            )
                        }
                    )
            )

            // -------------------------------------------------------------
            // 2. MIDDLE ARABIC CALLIGRAPHY BANNER
            // Features the exact fine/intricate dark green star pattern
            // -------------------------------------------------------------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(146.dp)
            ) {
                // Fine Islamic geometric watermark pattern
                Image(
                    painter = rememberAssetPainter("media/950b7d9c22577e8d.webp"),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Arabic Calligraphy in radiant gold with shadow
                Text(
                    text = item.arabic,
                    fontSize = 44.sp,
                    fontFamily = AmiriQuranFont,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFE494),
                    textAlign = TextAlign.Center,
                    lineHeight = 56.sp,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color(0xCC000000),
                            offset = Offset(0f, 4f),
                            blurRadius = 14f
                        )
                    ),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 16.dp)
                )
            }

            // -------------------------------------------------------------
            // 3. GOLD SEAM BAR + OVERLAPPING ARCH CREST MEDALLION
            // -------------------------------------------------------------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.5.dp)
                    .background(goldSeamGradient)
                    .zIndex(10f),
                contentAlignment = Alignment.Center
            ) {
                // Overlapping Arch Crest Medallion (protrudes up & down across seam)
                Image(
                    painter = rememberAssetPainter("media/1080e5e18bb8e70f.webp"),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.width(84.dp)
                )
            }

            // -------------------------------------------------------------
            // 4. BOTTOM BODY FRAME (Radial Ivory Canvas + Flourishes)
            // -------------------------------------------------------------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(bodyRadialGradient)
            ) {
                // Top-Left Corner Hanging Lantern (flush to top-left under seam)
                Image(
                    painter = rememberAssetPainter("media/ba1fb7fc2276343b.webp"),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .width(115.dp)
                )

                // Bottom-Right Corner Mandala Rosette (flush to bottom-right)
                Image(
                    painter = rememberAssetPainter("media/a3972f9bd8bc5f8d.webp"),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .width(135.dp)
                )

                // Centered Dynamic Content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 22.dp, bottom = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Top Ornamental Divider Row
                    StarJewelDivider()

                    Spacer(modifier = Modifier.height(4.dp))

                    // Transliteration (e.g., Al-Azim)
                    Text(
                        text = item.transliteration,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Serif,
                        color = Color(0xFF064E3B),
                        letterSpacing = 0.5.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    // Accent Pill Bar
                    Box(
                        modifier = Modifier
                            .width(36.dp)
                            .height(3.5.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color(0xFF064E3B))
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Meaning (e.g., The Most Great)
                    Text(
                        text = item.meaning,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF4A4336),
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Bottom Ornamental Divider Row
                    StarJewelDivider()

                    Spacer(modifier = Modifier.height(14.dp))

                    // Action Buttons Row (Play & Details)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Play Button: Soft mint background, deep emerald border, play/pause icon
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .clip(RoundedCornerShape(50))
                                .background(if (isPlaying) Color(0xFFD4EEDF) else Color(0xFFEDF7F2))
                                .border(1.8.dp, Color(0xFF064E3B), RoundedCornerShape(50))
                                .clickable(onClick = onPlayAudio),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                if (isPlaying) {
                                    Canvas(modifier = Modifier.size(15.dp)) {
                                        drawRoundRect(
                                            color = Color(0xFF064E3B),
                                            topLeft = Offset(size.width * 0.18f, size.height * 0.15f),
                                            size = Size(size.width * 0.22f, size.height * 0.7f),
                                            cornerRadius = CornerRadius(2.dp.toPx())
                                        )
                                        drawRoundRect(
                                            color = Color(0xFF064E3B),
                                            topLeft = Offset(size.width * 0.60f, size.height * 0.15f),
                                            size = Size(size.width * 0.22f, size.height * 0.7f),
                                            cornerRadius = CornerRadius(2.dp.toPx())
                                        )
                                    }
                                } else {
                                    Canvas(modifier = Modifier.size(15.dp)) {
                                        val path = Path().apply {
                                            moveTo(size.width * 0.22f, size.height * 0.15f)
                                            lineTo(size.width * 0.86f, size.height * 0.5f)
                                            lineTo(size.width * 0.22f, size.height * 0.85f)
                                            close()
                                        }
                                        drawPath(path, color = Color(0xFF064E3B))
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isPlaying) "Pause" else "Play",
                                    fontSize = 15.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF064E3B)
                                )
                            }
                        }

                        // Details Button: Deep emerald background, luxury gold border, info icon
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .clip(RoundedCornerShape(50))
                                .background(Color(0xFF083E28))
                                .border(1.8.dp, Color(0xFFC9A84C), RoundedCornerShape(50))
                                .clickable(onClick = onOpenDetails),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Canvas(modifier = Modifier.size(17.dp)) {
                                    val center = Offset(size.width / 2f, size.height / 2f)
                                    val radius = size.minDimension / 2f - 1.5f
                                    drawCircle(
                                        color = Color.White,
                                        radius = radius,
                                        center = center,
                                        style = Stroke(width = 1.8.dp.toPx())
                                    )
                                    drawCircle(
                                        color = Color.White,
                                        radius = 1.2.dp.toPx(),
                                        center = Offset(center.x, size.height * 0.33f)
                                    )
                                    drawLine(
                                        color = Color.White,
                                        start = Offset(center.x, size.height * 0.48f),
                                        end = Offset(center.x, size.height * 0.72f),
                                        strokeWidth = 1.8.dp.toPx(),
                                        cap = StrokeCap.Round
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Details",
                                    fontSize = 15.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Festive Floral Bouquet, Hanging Lanterns & Twinkling Fairy Lights (Active only when Ramadan is ON)
    if (isRamadanActive) {
        AllahNameFestiveDecorations(
            modifier = Modifier.matchParentSize(),
            isDarkTheme = isDarkTheme
        )
    }
}
}

/**
 * Moroccan 8-point Star Badge containing the number "99".
 * Recreates the exact overlapping emerald squares and gold borders from master design.
 */
@Composable
fun MoroccanStarBadge(
    number: String = "99",
    isDarkTheme: Boolean = false,
    modifier: Modifier = Modifier
) {
    val starFillColor = if (isDarkTheme) Color(0xFF0F3B2C) else Color(0xFF064E3B)
    val starBorderColor = if (isDarkTheme) Color(0xFFFFD770) else Color(0xFFC9A84C)

    Box(
        modifier = modifier.size(30.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val rectSize = Size(size.width * 0.68f, size.height * 0.68f)
            val cornerRadius = CornerRadius(3.5.dp.toPx(), 3.5.dp.toPx())

            // Rect 1 (0 deg)
            drawRoundRect(
                color = starFillColor,
                topLeft = Offset(center.x - rectSize.width / 2f, center.y - rectSize.height / 2f),
                size = rectSize,
                cornerRadius = cornerRadius
            )
            drawRoundRect(
                color = starBorderColor,
                topLeft = Offset(center.x - rectSize.width / 2f, center.y - rectSize.height / 2f),
                size = rectSize,
                cornerRadius = cornerRadius,
                style = Stroke(width = 1.5.dp.toPx())
            )

            // Rect 2 (45 deg)
            rotate(45f, pivot = center) {
                drawRoundRect(
                    color = starFillColor,
                    topLeft = Offset(center.x - rectSize.width / 2f, center.y - rectSize.height / 2f),
                    size = rectSize,
                    cornerRadius = cornerRadius
                )
                drawRoundRect(
                    color = Color(0xFFFDE8A8),
                    topLeft = Offset(center.x - rectSize.width / 2f, center.y - rectSize.height / 2f),
                    size = rectSize,
                    cornerRadius = cornerRadius,
                    style = Stroke(width = 1.2.dp.toPx())
                )
            }

            // Inner circle
            drawCircle(
                color = Color(0x2EC9A84C),
                radius = size.minDimension * 0.28f,
                center = center
            )
            drawCircle(
                color = Color(0xFFFFE494),
                radius = size.minDimension * 0.28f,
                center = center,
                style = Stroke(width = 0.8.dp.toPx())
            )
        }

        Text(
            text = number,
            fontSize = 11.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Serif,
            color = Color(0xFFFDE8A8),
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Ornamental Divider with twin horizontal gradient hairlines and centered 8-point gold/green star jewel.
 */
@Composable
fun StarJewelDivider(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(0.72f)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Left gradient hairline
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0x66C9A84C),
                            Color(0xFFC9A84C)
                        )
                    )
                )
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Center 8-point Star Jewel
        Canvas(modifier = Modifier.size(18.dp)) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val rectSize = Size(size.width * 0.6f, size.height * 0.6f)
            val cornerRadius = CornerRadius(1.5.dp.toPx(), 1.5.dp.toPx())

            // Rect 1 (0 deg)
            drawRoundRect(
                color = Color(0xFF064E3B),
                topLeft = Offset(center.x - rectSize.width / 2f, center.y - rectSize.height / 2f),
                size = rectSize,
                cornerRadius = cornerRadius
            )
            drawRoundRect(
                color = Color(0xFFC9A84C),
                topLeft = Offset(center.x - rectSize.width / 2f, center.y - rectSize.height / 2f),
                size = rectSize,
                cornerRadius = cornerRadius,
                style = Stroke(width = 1.4.dp.toPx())
            )

            // Rect 2 (45 deg)
            rotate(45f, pivot = center) {
                drawRoundRect(
                    color = Color(0xFF064E3B),
                    topLeft = Offset(center.x - rectSize.width / 2f, center.y - rectSize.height / 2f),
                    size = rectSize,
                    cornerRadius = cornerRadius
                )
                drawRoundRect(
                    color = Color(0xFFC9A84C),
                    topLeft = Offset(center.x - rectSize.width / 2f, center.y - rectSize.height / 2f),
                    size = rectSize,
                    cornerRadius = cornerRadius,
                    style = Stroke(width = 1.4.dp.toPx())
                )
            }

            // Center gold dot
            drawCircle(
                color = Color(0xFFFDE8A8),
                radius = 2.2.dp.toPx(),
                center = center
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Right gradient hairline
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFC9A84C),
                            Color(0x66C9A84C),
                            Color.Transparent
                        )
                    )
                )
        )
    }
}
