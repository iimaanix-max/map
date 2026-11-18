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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.R
import com.tasbih.digitaltasbih.ui.theme.DuaColors

/**
 * Exact Share Button for Dua cards:
 * White circular background, emerald green border, and green paper airplane icon.
 */
@Composable
fun DuaShareButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 34.dp,
    iconSize: Dp = 16.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Color.White)
            .border(1.2.dp, Color(0xFF0E5D47), CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_paper_plane),
            contentDescription = "Share Dua",
            tint = Color(0xFF0E5D47),
            modifier = Modifier.size(iconSize)
        )
    }
}

/**
 * Exact Bookmark / Save Button for Dua cards:
 * Deep emerald gradient circle, radiant gold border, and crisp white filled bookmark icon.
 */
@Composable
fun DuaBookmarkButton(
    isBookmarked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 34.dp,
    iconSize: Dp = 16.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .shadow(
                elevation = 3.dp,
                shape = CircleShape,
                spotColor = Color(0x66042E22),
                ambientColor = Color(0x33000000)
            )
            .clip(CircleShape)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F6E55),
                        Color(0xFF074D3B),
                        Color(0xFF03261C)
                    )
                )
            )
            .border(
                1.3.dp,
                if (isBookmarked) Color(0xFFF59E0B) else Color(0xFFD4AF37),
                CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Bookmark,
            contentDescription = "Bookmark Dua",
            tint = Color.White,
            modifier = Modifier.size(iconSize)
        )
    }
}

/**
 * Reusable Dua Bookmark Icon badge (used in modals and pill buttons):
 * Green gradient circle + gold border + white bookmark ribbon.
 */
@Composable
fun DuaBookmarkIcon(
    modifier: Modifier = Modifier,
    size: Dp = 30.dp,
    iconSize: Dp = 14.dp,
    borderWidth: Dp = 1.2.dp,
    borderColor: Color = Color(0xFFD4AF37)
) {
    Box(
        modifier = modifier
            .size(size)
            .shadow(
                elevation = 2.dp,
                shape = CircleShape,
                spotColor = Color(0x55042E22),
                ambientColor = Color(0x22000000)
            )
            .clip(CircleShape)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F6E55),
                        Color(0xFF074D3B),
                        Color(0xFF03261C)
                    )
                )
            )
            .border(borderWidth, borderColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Bookmark,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(iconSize)
        )
    }
}

/**
 * Exact Save Pill Button for the Dua Detail Screen:
 * Houses the iconic green+gold bookmark icon badge on the left and bold "Save" / "Saved" label on the right.
 */
@Composable
fun DuaSavePillButton(
    isBookmarked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectedLang: String = "en"
) {
    val saveText = if (isBookmarked) {
        when (selectedLang) {
            "hi" -> "सेव्ड"
            "ur" -> "محفوظ"
            "bn" -> "সংরক্ষিত"
            else -> "Saved"
        }
    } else {
        when (selectedLang) {
            "hi" -> "सेव करें"
            "ur" -> "محفوظ کریں"
            "bn" -> "সংরক্ষণ"
            else -> "Save"
        }
    }

    Box(
        modifier = modifier
            .height(36.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFFAF7F0))
            .border(1.dp, Color(0xFFE2D6B5), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(start = 3.dp, end = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            DuaBookmarkIcon(
                size = 28.dp,
                iconSize = 13.dp,
                borderColor = if (isBookmarked) Color(0xFFF59E0B) else Color(0xFFD4AF37)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = saveText,
                color = DuaColors.Emerald,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Exact Play Audio Button for Dua cards:
 * Emerald green circular button with Play / Pause / Loading states.
 */
@Composable
fun DuaPlayAudioButton(
    isPlaying: Boolean,
    isLoading: Boolean,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    size: Dp = 34.dp,
    iconSize: Dp = 18.dp
) {
    val clickModifier = if (onClick != null) {
        Modifier.clickable { onClick() }
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .size(size)
            .shadow(
                elevation = if (isPlaying) 4.dp else 1.5.dp,
                shape = CircleShape,
                spotColor = Color(0x660E5D47),
                ambientColor = Color(0x33000000)
            )
            .clip(CircleShape)
            .background(
                if (isPlaying) {
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0F6E55),
                            Color(0xFF074D3B)
                        )
                    )
                } else {
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White,
                            Color(0xFFF9F7F2)
                        )
                    )
                }
            )
            .border(
                1.3.dp,
                if (isPlaying) Color(0xFFD4AF37) else Color(0xFF0E5D47),
                CircleShape
            )
            .then(clickModifier),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(iconSize),
                strokeWidth = 2.dp,
                color = if (isPlaying) Color.White else Color(0xFF0E5D47)
            )
        } else if (isPlaying) {
            Icon(
                imageVector = Icons.Filled.Pause,
                contentDescription = "Pause Dua",
                tint = Color.White,
                modifier = Modifier.size(iconSize)
            )
        } else {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "Play Dua Audio",
                tint = Color(0xFF0E5D47),
                modifier = Modifier.size(iconSize)
            )
        }
    }
}

/**
 * Dedicated Audio Recitation Bar for DuaDetailScreen:
 * Shows the reciter name, animated audio state, and Play/Pause control.
 */
@Composable
fun DuaAudioRecitationBar(
    isPlaying: Boolean,
    isLoading: Boolean,
    reciterName: String,
    onTogglePlay: () -> Unit,
    modifier: Modifier = Modifier,
    selectedLang: String = "en"
) {
    val barTitle = if (isPlaying) {
        when (selectedLang) {
            "hi" -> "तिलावत जारी है..."
            "ur" -> "تلاوت جاری ہے..."
            "bn" -> "তেলাওয়াত চলছে..."
            else -> "Reciting..."
        }
    } else {
        when (selectedLang) {
            "hi" -> "दुआ सुनें (ऑडियो)"
            "ur" -> "دعا سنیں (آڈیو)"
            "bn" -> "দোয়া শুনুন (অডিও)"
            else -> "Listen to Audio"
        }
    }

    val qariLabel = when (selectedLang) {
        "hi" -> "क़ारी:"
        "ur" -> "قاری:"
        "bn" -> "ক্বারী:"
        else -> "Qari:"
    }

    val badgeLabel = if (isPlaying) {
        when (selectedLang) {
            "hi" -> "🔊 चल रहा है"
            "ur" -> "🔊 جاری ہے"
            "bn" -> "🔊 চলছে"
            else -> "🔊 PLAYING"
        }
    } else {
        when (selectedLang) {
            "hi" -> "▶ ऑडियो"
            "ur" -> "▶ آڈیو"
            "bn" -> "▶ অডিও"
            else -> "▶ AUDIO"
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFF6F8F6))
            .border(1.dp, Color(0x2E0E5D47), RoundedCornerShape(14.dp))
            .clickable { onTogglePlay() }
            .padding(horizontal = 12.dp, vertical = 9.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DuaPlayAudioButton(
                    isPlaying = isPlaying,
                    isLoading = isLoading,
                    onClick = null,
                    size = 32.dp,
                    iconSize = 17.dp
                )

                Column {
                    Text(
                        text = barTitle,
                        color = if (isPlaying) DuaColors.Emerald else DuaColors.TextDark,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (reciterName.isNotBlank()) "$qariLabel $reciterName" else "Authentic Arabic Voice",
                        color = DuaColors.TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isPlaying) Color(0xFFE8F5E9) else Color(0xFFEDE9DF))
                    .padding(horizontal = 9.dp, vertical = 3.dp)
            ) {
                Text(
                    text = badgeLabel,
                    color = if (isPlaying) Color(0xFF2E7D32) else DuaColors.TextMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}
