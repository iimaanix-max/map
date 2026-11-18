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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.data.DuaAudioPlayer
import com.tasbih.digitaltasbih.data.DuaEntry
import com.tasbih.digitaltasbih.ui.theme.DuaColors

@Composable
fun DuaCompactCard(
    dua: DuaEntry,
    index: Int,
    selectedLang: String,
    isBookmarked: Boolean,
    onCardClick: () -> Unit,
    onShareClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onRefClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isPlaying = DuaAudioPlayer.isDuaPlaying(dua.id)
    val isLoading = DuaAudioPlayer.isDuaLoading(dua.id)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .border(1.dp, Color(0x1F0E5D47), RoundedCornerShape(18.dp))
            .clickable { onCardClick() }
            .padding(14.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row: [01 badge] [Category pill] [Ref button] ... [Share btn] [Bookmark btn]
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Gold circle number badge
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, DuaColors.Gold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = String.format(java.util.Locale.US, "%02d", index + 1),
                            color = DuaColors.Gold,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Green category pill
                    val catTag = dua.cat.replaceFirstChar { it.uppercase() }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(DuaColors.Emerald)
                            .padding(horizontal = 10.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = catTag,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Authentic Hadith Reference Badge Button
                    if (onRefClick != null) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFFF3ECE0))
                                .border(1.dp, Color(0x88C9A84C), RoundedCornerShape(20.dp))
                                .clickable { onRefClick() }
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "📜 Ref",
                                color = DuaColors.TextDark,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Play Audio, Share & Bookmark buttons
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DuaPlayAudioButton(
                        isPlaying = isPlaying,
                        isLoading = isLoading,
                        onClick = { DuaAudioPlayer.togglePlay(context, dua) },
                        size = 34.dp
                    )

                    DuaShareButton(
                        onClick = onShareClick,
                        size = 34.dp
                    )

                    DuaBookmarkButton(
                        isBookmarked = isBookmarked,
                        onClick = onBookmarkClick,
                        size = 34.dp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title
            Text(
                text = dua.getLocalizedTitle(selectedLang),
                color = DuaColors.TextDark,
                fontSize = 15.5.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 20.sp
            )

            // Arabic snippet (RTL)
            if (dua.ar.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Text(
                        text = dua.ar,
                        color = DuaColors.ArabicGreen,
                        fontSize = 16.5.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Meaning snippet
            val meaning = dua.getLocalizedMeaning(selectedLang)
            if (meaning.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = meaning,
                    color = DuaColors.TextMuted,
                    fontSize = 13.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Color(0x1F0E5D47), thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))

            // Footer: Read Full Dua >
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val readFullText = when (selectedLang) {
                        "hi" -> "🤲  पूरी दुआ पढ़ें"
                        "ur" -> "🤲  مکمل دعا پڑھیں"
                        "bn" -> "🤲  সম্পূর্ণ দোয়া পড়ুন"
                        else -> "🤲  Read Full Dua"
                    }
                    Text(
                        text = readFullText,
                        color = DuaColors.Gold,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = DuaColors.Gold,
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }
}
