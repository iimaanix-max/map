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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.data.DuaCustomCollection
import com.tasbih.digitaltasbih.ui.theme.DuaColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveToCollectionSheet(
    duaId: String,
    collections: List<DuaCustomCollection>,
    selectedLang: String = "en",
    onToggleCollection: (collectionId: String) -> Unit,
    onCreateCollection: (name: String) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var newListName by remember { mutableStateOf("") }

    val sheetTitle = when (selectedLang) {
        "hi" -> "लिस्ट में सेव करें"
        "ur" -> "لسٹ میں محفوظ کریں"
        else -> "Save to Collection"
    }
    val noColTitle = when (selectedLang) {
        "hi" -> "अभी कोई लिस्ट नहीं है"
        "ur" -> "ابھی کوئی لسٹ نہیں ہے"
        else -> "No collections yet"
    }
    val noColSubtitle = when (selectedLang) {
        "hi" -> "अपनी पहली लिस्ट बनाने के लिए नीचे नाम लिखें!"
        "ur" -> "اپنی پہلی لسٹ بنانے کے لیے نیچے نام درج کریں!"
        else -> "Type a name below to create your first collection!"
    }
    val placeholderText = when (selectedLang) {
        "hi" -> "नई लिस्ट का नाम..."
        "ur" -> "نئی لسٹ کا نام..."
        else -> "New list name..."
    }
    val createBtnText = when (selectedLang) {
        "hi" -> "+ बनाएं"
        "ur" -> "+ بنائیں"
        else -> "+ Create"
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .navigationBarsPadding()
        ) {
            // Header: 🔖 Save to Collection + ✕
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    DuaBookmarkIcon(size = 32.dp, iconSize = 15.dp)

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = sheetTitle,
                        color = DuaColors.TextDark,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "✕",
                    color = DuaColors.TextMuted,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onDismiss() }
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            if (collections.isEmpty()) {
                // Empty state matching Screenshot 22
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "📁", fontSize = 36.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = noColTitle,
                        color = DuaColors.TextDark,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = noColSubtitle,
                        color = DuaColors.TextMuted,
                        fontSize = 12.5.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                ) {
                    items(collections) { col ->
                        val isContained = col.duaIds.contains(duaId)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onToggleCollection(col.id) }
                                .padding(vertical = 10.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "📁", fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = col.name,
                                    color = DuaColors.TextDark,
                                    fontSize = 14.5.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isContained) DuaColors.Emerald else Color.Transparent)
                                    .border(
                                        1.5.dp,
                                        if (isContained) DuaColors.Emerald else Color(0x660E5D47),
                                        RoundedCornerShape(6.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isContained) {
                                    Text(text = "✓", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Create New List Input Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF9F7F2))
                        .border(1.dp, Color(0x33C9A84C), RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (newListName.isEmpty()) {
                        Text(
                            text = placeholderText,
                            color = DuaColors.TextMuted,
                            fontSize = 13.5.sp
                        )
                    }

                    BasicTextField(
                        value = newListName,
                        onValueChange = { newListName = it },
                        singleLine = true,
                        textStyle = TextStyle(
                            color = DuaColors.TextDark,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        cursorBrush = SolidColor(DuaColors.Emerald),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Box(
                    modifier = Modifier
                        .height(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(DuaColors.Emerald)
                        .clickable {
                            if (newListName.isNotBlank()) {
                                onCreateCollection(newListName.trim())
                                newListName = ""
                            }
                        }
                        .padding(horizontal = 18.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = createBtnText,
                        color = Color.White,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}