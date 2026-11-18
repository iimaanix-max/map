package com.tasbih.digitaltasbih.ui.components.dua

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.data.DuaFeelingItem
import com.tasbih.digitaltasbih.ui.components.rememberAssetPainter
import com.tasbih.digitaltasbih.ui.theme.DuaColors

@Composable
fun DuaFeelingsSection(
    feelings: List<DuaFeelingItem>,
    selectedLang: String,
    onFeelingClick: (DuaFeelingItem) -> Unit,
    onViewAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "How are you feeling?",
                color = DuaColors.Emerald,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "View all ›",
                color = DuaColors.TextMuted,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { onViewAllClick() }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // First 6 feelings in 2 rows of 3 columns
        val displayItems = feelings.take(6)
        val chunked = displayItems.chunked(3)

        chunked.forEach { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                rowItems.forEach { feeling ->
                    DuaFeelingCard(
                        item = feeling,
                        selectedLang = selectedLang,
                        onClick = { onFeelingClick(feeling) },
                        modifier = Modifier.weight(1f)
                    )
                }
                // Filler if last row has less than 3 items
                if (rowItems.size < 3) {
                    repeat(3 - rowItems.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun DuaFeelingCard(
    item: DuaFeelingItem,
    selectedLang: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 4.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val painter = rememberAssetPainter(item.assetPath)
        Image(
            painter = painter,
            contentDescription = item.getLocalizedLabel(selectedLang),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(76.dp)
                .clip(RoundedCornerShape(14.dp))
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = item.getLocalizedLabel(selectedLang),
            color = DuaColors.TextDark,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 1
        )

        if (item.subLabel.isNotBlank()) {
            Text(
                text = item.subLabel,
                color = DuaColors.TextMuted,
                fontSize = 10.5.sp,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}