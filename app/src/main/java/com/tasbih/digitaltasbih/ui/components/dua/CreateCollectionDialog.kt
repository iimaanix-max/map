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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.tasbih.digitaltasbih.ui.theme.DuaColors

@Composable
fun CreateCollectionDialog(
    selectedLang: String = "en",
    onDismiss: () -> Unit,
    onCreate: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    val dialogTitle = when (selectedLang) {
        "hi" -> "📁 नई लिस्ट बनाएं"
        "ur" -> "📁 نئی لسٹ بنائیں"
        else -> "📁 Create New Collection"
    }
    val labelText = when (selectedLang) {
        "hi" -> "लिस्ट का नाम दर्ज करें:"
        "ur" -> "لسٹ کا نام درج کریں:"
        else -> "Enter Collection Name:"
    }
    val placeholderText = when (selectedLang) {
        "hi" -> "उदा. तहज्जुद, रोज़ाना की दुआ, सफ़र"
        "ur" -> "مثلاً تہجد، روزانہ کی دعائیں، سفر"
        else -> "e.g., Tahajjud, Daily Peace, Travel"
    }
    val createText = when (selectedLang) {
        "hi" -> "+ लिस्ट बनाएं"
        "ur" -> "+ لسٹ بنائیں"
        else -> "+ Create Collection"
    }
    val cancelText = when (selectedLang) {
        "hi" -> "रद्द करें"
        "ur" -> "منسوخ"
        else -> "Cancel"
    }
    val emptyErrorText = when (selectedLang) {
        "hi" -> "लिस्ट का नाम खाली नहीं हो सकता"
        "ur" -> "لسٹ کا نام خالی نہیں ہو سکتا"
        else -> "Collection name cannot be empty"
    }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(16.dp, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .border(1.5.dp, Color(0x66C9A84C), RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = dialogTitle,
                        color = DuaColors.TextDark,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "✕",
                        color = DuaColors.TextMuted,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onDismiss() }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = labelText,
                    color = DuaColors.Emerald,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF9F7F2))
                        .border(1.dp, Color(0x33C9A84C), RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (name.isEmpty()) {
                        Text(
                            text = placeholderText,
                            color = DuaColors.TextMuted,
                            fontSize = 13.5.sp
                        )
                    }

                    BasicTextField(
                        value = name,
                        onValueChange = { name = it; error = "" },
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

                if (error.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = error,
                        color = Color(0xFFD32F2F),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF0EFEA))
                            .border(1.dp, Color(0x22000000), RoundedCornerShape(12.dp))
                            .clickable {
                                if (name.isBlank()) {
                                    error = emptyErrorText
                                } else {
                                    onCreate(name.trim())
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = createText,
                            color = DuaColors.TextDark,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .height(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF9F7F2))
                            .border(1.dp, Color(0x33C9A84C), RoundedCornerShape(12.dp))
                            .clickable { onDismiss() }
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = cancelText,
                            color = DuaColors.TextDark,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}