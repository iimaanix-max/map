package com.tasbih.digitaltasbih.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.R
import com.tasbih.digitaltasbih.data.HomeCardsLocalization
import com.tasbih.digitaltasbih.data.IslamicEventData

/**
 * Ultra-Luxury Islamic Upcoming Events Card
 * Directly uses the user-provided pristine 3D template graphic (bg_upcoming_events_template.jpg)
 * and dynamically overlays titles, Hijri dates, and countdown badges with pixel-perfect accuracy.
 */
@Composable
fun UpcomingEventsCard(
    events: List<IslamicEventData>,
    selectedLanguage: String = "en",
    onViewAll: () -> Unit,
    onEventClick: (String) -> Unit
) {
    val strings = HomeCardsLocalization.get(selectedLanguage)
    val event1 = events.getOrNull(0)
    val event2 = events.getOrNull(1)

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(22.dp), spotColor = Color(0x66000000))
            .clip(RoundedCornerShape(22.dp))
    ) {
        val totalW = maxWidth
        // Exact aspect ratio of template image: 1024 x 704
        val totalH = totalW * (704f / 1024f)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(totalH)
        ) {
            // 1. Pristine 1:1 Ultra-Luxury 3D Template Graphic
            Image(
                painter = painterResource(id = R.drawable.bg_upcoming_events_template),
                contentDescription = "Upcoming Events",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.matchParentSize()
            )

            // ── 2. HEADER OVERLAYS ──
            // Header Title: "Upcoming" (White) + "Events" (Gold #E5C37A)
            val headerAnnotated = buildAnnotatedString {
                if (selectedLanguage == "en") {
                    withStyle(SpanStyle(color = Color.White)) {
                        append("Upcoming ")
                    }
                    withStyle(SpanStyle(color = Color(0xFFE5C37A))) {
                        append("Events")
                    }
                } else {
                    val fullTitle = if (strings.upcomingEventsWord.isNotBlank()) {
                        "${strings.upcomingTitle} ${strings.upcomingEventsWord}"
                    } else {
                        strings.upcomingTitle
                    }
                    withStyle(SpanStyle(color = Color.White)) {
                        append(fullTitle)
                    }
                }
            }

            Text(
                text = headerAnnotated,
                fontSize = (totalW.value * 0.051f).sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                modifier = Modifier
                    .offset(x = totalW * 0.242f, y = totalH * 0.088f)
                    .width(totalW * 0.49f)
            )

            // Header Subtitle: "STAY CONNECTED • DON'T MISS OUT"
            Text(
                text = strings.upcomingSubtitle,
                fontSize = (totalW.value * 0.022f).sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFCEBF9E),
                letterSpacing = 1.0.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                modifier = Modifier
                    .offset(x = totalW * 0.238f, y = totalH * 0.198f)
                    .width(totalW * 0.495f)
            )

            // "View all" text inside the right pill (centered before the pre-rendered arrow)
            Box(
                modifier = Modifier
                    .offset(x = totalW * 0.774f, y = totalH * 0.145f)
                    .width(totalW * 0.122f)
                    .height(totalH * 0.082f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = strings.viewAll.replace("→", "").replace("←", "").trim(),
                    fontSize = (totalW.value * 0.028f).sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )
            }

            // Transparent Clickable area for "View all"
            Box(
                modifier = Modifier
                    .offset(x = totalW * 0.750f, y = totalH * 0.130f)
                    .width(totalW * 0.220f)
                    .height(totalH * 0.110f)
                    .clip(RoundedCornerShape(50))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onViewAll
                    )
            )

            // ── 3. EVENT 1 OVERLAYS (Top Card) ──
            if (event1 != null) {
                // Clickable area for Event 1
                Box(
                    modifier = Modifier
                        .offset(x = totalW * 0.04f, y = totalH * 0.29f)
                        .width(totalW * 0.92f)
                        .height(totalH * 0.32f)
                        .clip(RoundedCornerShape(18.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onEventClick(event1.id) }
                        )
                )

                // Event 1 Title (Prominent, bold, elegant typography, shifted down gracefully)
                val title1FontSize = when {
                    event1.title.length > 32 -> (totalW.value * 0.032f).sp
                    event1.title.length > 22 -> (totalW.value * 0.035f).sp
                    else -> (totalW.value * 0.038f).sp
                }

                Text(
                    text = event1.title,
                    fontSize = title1FontSize,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0A261A),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                    modifier = Modifier
                        .offset(x = totalW * 0.252f, y = totalH * 0.338f)
                        .width(totalW * 0.515f)
                )

                // Event 1 Hijri Date (Clear, bold, perfectly aligned next to location pin, shifted down)
                Box(
                    modifier = Modifier
                        .offset(x = totalW * 0.302f, y = totalH * 0.435f)
                        .width(totalW * 0.460f)
                        .height(totalH * 0.048f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = event1.hijriDate,
                        fontSize = (totalW.value * 0.031f).sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF164835),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                    )
                }

                // Event 1 Countdown Plaque: Upper Compartment with clean luxury backing for maximum clarity
                Box(
                    modifier = Modifier
                        .offset(x = totalW * 0.770f, y = totalH * 0.298f)
                        .width(totalW * 0.175f)
                        .height(totalH * 0.164f)
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                        .background(Color(0xFF014425)),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.foundation.layout.Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
                    ) {
                        Text(
                            text = "${event1.daysRemaining}",
                            fontSize = (totalW.value * 0.056f).sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                        )
                        Text(
                            text = strings.days.uppercase(),
                            fontSize = (totalW.value * 0.021f).sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFDCC79B),
                            letterSpacing = 0.8.sp,
                            textAlign = TextAlign.Center,
                            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                        )
                    }
                }

                // Event 1 Countdown Plaque: Gregorian Date (Lower compartment cleanly centered)
                Box(
                    modifier = Modifier
                        .offset(x = totalW * 0.770f, y = totalH * 0.490f)
                        .width(totalW * 0.175f)
                        .height(totalH * 0.075f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = event1.gregorianDate,
                        fontSize = (totalW.value * 0.026f).sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFAF7EE),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                    )
                }
            }

            // ── 4. EVENT 2 OVERLAYS (Bottom Card) ──
            if (event2 != null) {
                // Clickable area for Event 2
                Box(
                    modifier = Modifier
                        .offset(x = totalW * 0.04f, y = totalH * 0.63f)
                        .width(totalW * 0.92f)
                        .height(totalH * 0.32f)
                        .clip(RoundedCornerShape(18.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onEventClick(event2.id) }
                        )
                )

                // Event 2 Title (Prominent, bold, elegant typography, shifted down gracefully)
                val title2FontSize = when {
                    event2.title.length > 32 -> (totalW.value * 0.032f).sp
                    event2.title.length > 22 -> (totalW.value * 0.035f).sp
                    else -> (totalW.value * 0.038f).sp
                }

                Text(
                    text = event2.title,
                    fontSize = title2FontSize,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0A261A),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                    modifier = Modifier
                        .offset(x = totalW * 0.252f, y = totalH * 0.678f)
                        .width(totalW * 0.515f)
                )

                // Event 2 Hijri Date (Clear, bold, perfectly aligned next to location pin, shifted down)
                Box(
                    modifier = Modifier
                        .offset(x = totalW * 0.302f, y = totalH * 0.772f)
                        .width(totalW * 0.460f)
                        .height(totalH * 0.048f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = event2.hijriDate,
                        fontSize = (totalW.value * 0.031f).sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF164835),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                    )
                }

                // Event 2 Countdown Plaque: Upper Compartment with clean luxury backing
                Box(
                    modifier = Modifier
                        .offset(x = totalW * 0.770f, y = totalH * 0.639f)
                        .width(totalW * 0.175f)
                        .height(totalH * 0.164f)
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                        .background(Color(0xFF014425)),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.foundation.layout.Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
                    ) {
                        Text(
                            text = "${event2.daysRemaining}",
                            fontSize = (totalW.value * 0.056f).sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                        )
                        Text(
                            text = strings.days.uppercase(),
                            fontSize = (totalW.value * 0.021f).sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFDCC79B),
                            letterSpacing = 0.8.sp,
                            textAlign = TextAlign.Center,
                            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                        )
                    }
                }

                // Event 2 Countdown Plaque: Gregorian Date (Lower compartment cleanly centered)
                Box(
                    modifier = Modifier
                        .offset(x = totalW * 0.770f, y = totalH * 0.830f)
                        .width(totalW * 0.175f)
                        .height(totalH * 0.075f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = event2.gregorianDate,
                        fontSize = (totalW.value * 0.026f).sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFAF7EE),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                    )
                }
            }
        }
    }
}
