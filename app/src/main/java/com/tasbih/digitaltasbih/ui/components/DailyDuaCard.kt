package com.tasbih.digitaltasbih.ui.components

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.R
import com.tasbih.digitaltasbih.data.DailyDuaData
import com.tasbih.digitaltasbih.data.HomeCardsLocalization

/**
 * Ultra-Luxury Islamic Daily Dua Widget for Home Screen.
 * Renders the user-provided raw 3D un-flattened graphic template (bg_daily_dua_template.png)
 * and dynamically overlays all typography with mathematical pixel perfection:
 * 1. Header: "Dua" title, "ASK • BELIEVE • RECEIVE" subtitle, and "View all" action button.
 * 2. Parchment Content:
 *    - Transliteration (italic serif, centered, seated below top rosette divider)
 *    - Traditional Arabic Calligraphy with full tashkeel (large, centered)
 *    - Localized Translation (serif, centered, seated below center rosette divider)
 *    - Authentic Hadith Reference ribbon (clickable with authentic reference modal)
 * 3. Bottom Action Bar:
 *    - "Daily Supplication" capsule
 *    - Share button (triggers native Android share chooser)
 *    - Favorite toggle button (with animated ruby heart indicator)
 */
@Composable
fun DailyDuaCard(
    dua: DailyDuaData,
    selectedLanguage: String = "en",
    onViewAll: () -> Unit,
    onRefClick: () -> Unit
) {
    val context = LocalContext.current
    val strings = HomeCardsLocalization.get(selectedLanguage)
    var isFavorite by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(26.dp), spotColor = Color(0x66000000))
            .clip(RoundedCornerShape(26.dp))
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val totalW = maxWidth
            // Aspect ratio matches the exact 522 x 1024 px raw graphic template
            val totalH = totalW / (522f / 1024f)

            // ── 0. RAW 3D GRAPHIC TEMPLATE BASE ──
            Image(
                painter = painterResource(id = R.drawable.bg_daily_dua_template),
                contentDescription = "Daily Dua",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(totalH)
            )

            // ── 1. HEADER TITLE & SUBTITLE ──
            Text(
                text = strings.duaTitle,
                fontSize = (totalW.value * 0.056f).sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFAF7EE),
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                modifier = Modifier.offset(x = totalW * 0.295f, y = totalH * 0.038f)
            )

            // Subtitle sits cleanly to the right of the vertical divider at 0.270f and above horizontal line at 0.124f
            Text(
                text = strings.duaSubtitle,
                fontSize = (totalW.value * 0.024f).sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFCBB179),
                letterSpacing = 1.2.sp,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                modifier = Modifier.offset(x = totalW * 0.300f, y = totalH * 0.082f)
            )

            // ── 2. VIEW ALL BUTTON (Centered inside pre-rendered emerald pill before arrow) ──
            Box(
                modifier = Modifier
                    .offset(x = totalW * 0.744f, y = totalH * 0.1015f)
                    .width(totalW * 0.146f)
                    .height(totalH * 0.0352f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = strings.viewAll.replace("→", "").replace("←", "").trim(),
                    fontSize = (totalW.value * 0.026f).sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFAF7EE),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )
            }

            // Clickable area for View All
            Box(
                modifier = Modifier
                    .offset(x = totalW * 0.720f, y = totalH * 0.090f)
                    .width(totalW * 0.250f)
                    .height(totalH * 0.060f)
                    .clip(RoundedCornerShape(50))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onViewAll
                    )
            )

            // ── 3. TRANSLITERATION (Enlarged font, seated below top rosette divider) ──
            Text(
                text = dua.transliteration,
                fontSize = (totalW.value * 0.034f).sp,
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1E3529),
                textAlign = TextAlign.Center,
                lineHeight = (totalW.value * 0.048f).sp,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .offset(x = totalW * 0.075f, y = totalH * 0.236f)
                    .width(totalW * 0.850f)
            )

            // ── 4. ARABIC CALLIGRAPHY (Significantly enlarged, traditional Arabic with Harakat) ──
            Text(
                text = dua.arabic,
                fontSize = (totalW.value * 0.076f).sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0C261B),
                textAlign = TextAlign.Center,
                lineHeight = (totalW.value * 0.122f).sp,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .offset(x = totalW * 0.065f, y = totalH * 0.342f)
                    .width(totalW * 0.870f)
            )

            // ── 5. LOCALIZED TRANSLATION (Enlarged font, seated below middle rosette divider) ──
            Text(
                text = dua.translation,
                fontSize = (totalW.value * 0.037f).sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1D3126),
                textAlign = TextAlign.Center,
                lineHeight = (totalW.value * 0.052f).sp,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .offset(x = totalW * 0.075f, y = totalH * 0.676f)
                    .width(totalW * 0.850f)
            )

            // ── 6. AUTHENTIC HADITH REFERENCE RIBBON (Seated inside ribbon after vertical bar) ──
            val displayRef = remember(dua) {
                buildString {
                    if (dua.hadithBook.isNotBlank()) {
                        append(dua.hadithBook)
                        if (dua.hadithGrade.isNotBlank()) {
                            val grade = dua.hadithGrade.replace("✓", "").trim()
                            val shortGrade = if (grade.startsWith("Sahih", ignoreCase = true)) "Sahih" else grade
                            append(" • $shortGrade")
                        }
                    } else {
                        val clean = dua.reference.substringBefore("(").ifBlank { dua.reference.substringBefore("|") }.trim()
                        append(clean)
                    }
                }
            }

            Box(
                modifier = Modifier
                    .offset(x = totalW * 0.295f, y = totalH * 0.836f)
                    .width(totalW * 0.570f)
                    .height(totalH * 0.043f),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = displayRef,
                    fontSize = (totalW.value * 0.034f).sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFAF7EE),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )
            }

            // Clickable area for Hadith Reference ribbon
            Box(
                modifier = Modifier
                    .offset(x = totalW * 0.080f, y = totalH * 0.830f)
                    .width(totalW * 0.840f)
                    .height(totalH * 0.055f)
                    .clip(RoundedCornerShape(50))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onRefClick
                    )
            )

            // ── 7. BOTTOM ACTION BAR ──
            // Left: "Daily Supplication" label inside pre-rendered green pill (placed cleanly to the right of star icon)
            val suppFont = if (strings.dailySupplication.length > 20) {
                (totalW.value * 0.024f).sp
            } else {
                (totalW.value * 0.027f).sp
            }
            Box(
                modifier = Modifier
                    .offset(x = totalW * 0.146f, y = totalH * 0.934f)
                    .width(totalW * 0.280f)
                    .height(totalH * 0.041f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = strings.dailySupplication,
                    fontSize = suppFont,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFAF7EE),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )
            }

            // Clickable area for Daily Supplication pill
            Box(
                modifier = Modifier
                    .offset(x = totalW * 0.050f, y = totalH * 0.925f)
                    .width(totalW * 0.400f)
                    .height(totalH * 0.060f)
                    .clip(RoundedCornerShape(50))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onViewAll
                    )
            )

            // Middle-Right: Share Button (Circular, overlays pre-rendered share button)
            Box(
                modifier = Modifier
                    .offset(
                        x = totalW * 0.755f - totalW * 0.055f,
                        y = totalH * 0.953f - totalW * 0.055f
                    )
                    .size(totalW * 0.110f)
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            com.tasbih.digitaltasbih.util.DuaShareHelper.shareDailyDua(context, dua, selectedLanguage)
                        }
                    )
            )

            // Right: Favorite Button (Circular, overlays pre-rendered favorite button)
            // The template already has a pristine gold outline heart (♡) pre-rendered at (0.8927f * W, 0.9531f * H).
            // When !isFavorite, nothing is drawn so the native gold outline heart shows cleanly with ZERO obstruction.
            // When isFavorite == true, a matching dark emerald background circle masks the gold outline,
            // and a filled ruby red heart is centered on top so NO background color can ever peek out.
            val heartCenterW = totalW * 0.8927f
            val heartCenterH = totalH * 0.9531f
            val heartOverlaySize = totalW * 0.076f

            if (isFavorite) {
                // Subtle dark emerald circular backdrop to cleanly mask the underlying ivory outline
                Box(
                    modifier = Modifier
                        .offset(
                            x = heartCenterW - totalW * 0.045f,
                            y = heartCenterH - totalW * 0.045f
                        )
                        .size(totalW * 0.090f)
                        .clip(CircleShape)
                        .background(Color(0xFF033320))
                )
                // Pure filled ruby red heart on top
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorite Active",
                    tint = Color(0xFFE53935),
                    modifier = Modifier
                        .offset(
                            x = heartCenterW - heartOverlaySize / 2,
                            y = heartCenterH - heartOverlaySize / 2
                        )
                        .size(heartOverlaySize)
                )
            }

            // Clickable transparent area over the entire circular button
            Box(
                modifier = Modifier
                    .offset(
                        x = heartCenterW - totalW * 0.060f,
                        y = heartCenterH - totalW * 0.060f
                    )
                    .size(totalW * 0.120f)
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            isFavorite = !isFavorite
                            AppToast.show(if (isFavorite) "Added to Favorites ❤️" else "Removed from Favorites")
                        }
                    )
            )
        }
    }
}
