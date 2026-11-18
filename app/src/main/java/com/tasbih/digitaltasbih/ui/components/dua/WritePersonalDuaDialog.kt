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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tasbih.digitaltasbih.data.UserPersonalDua
import com.tasbih.digitaltasbih.ui.theme.DuaColors
import java.util.UUID

@Composable
fun WritePersonalDuaDialog(
    initialDua: UserPersonalDua? = null,
    onDismiss: () -> Unit,
    onSave: (UserPersonalDua) -> Unit
) {
    var title by remember { mutableStateOf(initialDua?.title ?: "") }
    var prayer by remember { mutableStateOf(initialDua?.prayer ?: "") }
    var arabic by remember { mutableStateOf(initialDua?.arabic ?: "") }
    var transliteration by remember { mutableStateOf(initialDua?.transliteration ?: "") }
    var note by remember { mutableStateOf(initialDua?.note ?: "") }
    var errorMessage by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .shadow(16.dp, RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .border(1.5.dp, Color(0x66C9A84C), RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                // Header: "✍️ Write New Personal Dua" + close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (initialDua == null) "✍️ Write New Personal Dua" else "✏️ Edit Personal Dua",
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

                Spacer(modifier = Modifier.height(14.dp))

                // Title
                FieldLabel(text = "Dua Title / Topic *")
                CustomInputField(
                    value = title,
                    onValueChange = { title = it; errorMessage = "" },
                    placeholder = "e.g., Parents Health, Exam Success",
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Prayer / Meaning
                FieldLabel(text = "Your Prayer / Meaning *")
                CustomInputField(
                    value = prayer,
                    onValueChange = { prayer = it; errorMessage = "" },
                    placeholder = "Write your prayer in your own words (English, Hindi, Urdu, etc.)...",
                    singleLine = false,
                    modifier = Modifier.fillMaxWidth().height(84.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Arabic (Optional)
                FieldLabel(text = "Arabic Text (Optional)")
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    CustomInputField(
                        value = arabic,
                        onValueChange = { arabic = it },
                        placeholder = "رَبِّ زِدْنِي عِلْمًا...",
                        isArabic = true,
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Transliteration (Optional)
                FieldLabel(text = "Transliteration / Roman (Optional)")
                CustomInputField(
                    value = transliteration,
                    onValueChange = { transliteration = it },
                    placeholder = "Rabbi zidni ilma...",
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // When to Recite / Note (Optional)
                FieldLabel(text = "When to Recite / Note (Optional)")
                CustomInputField(
                    value = note,
                    onValueChange = { note = it },
                    placeholder = "e.g., After every prayer",
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                )

                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage,
                        color = Color(0xFFD32F2F),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Bottom buttons: [💾 Save Dua] [Cancel]
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(DuaColors.Emerald)
                            .clickable {
                                if (title.isBlank()) {
                                    errorMessage = "Please enter a Dua title"
                                } else if (prayer.isBlank()) {
                                    errorMessage = "Please enter your prayer/meaning"
                                } else {
                                    val id = initialDua?.id ?: "pdua_${UUID.randomUUID()}"
                                    val saved = UserPersonalDua(
                                        id = id,
                                        title = title.trim(),
                                        prayer = prayer.trim(),
                                        arabic = arabic.trim(),
                                        transliteration = transliteration.trim(),
                                        note = note.trim()
                                    )
                                    onSave(saved)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "💾 Save Dua",
                            color = Color.White,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .height(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0x330E5D47), RoundedCornerShape(12.dp))
                            .clickable { onDismiss() }
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Cancel",
                            color = DuaColors.TextDark,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        color = DuaColors.Emerald,
        fontSize = 12.5.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

@Composable
private fun CustomInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    singleLine: Boolean = true,
    isArabic: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF9F7F2))
            .border(1.dp, Color(0x33C9A84C), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = if (singleLine) Alignment.CenterStart else Alignment.TopStart
    ) {
        if (value.isEmpty()) {
            Text(
                text = placeholder,
                color = DuaColors.TextMuted,
                fontSize = 13.5.sp,
                textAlign = if (isArabic) TextAlign.Right else TextAlign.Left,
                modifier = Modifier.fillMaxWidth()
            )
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = singleLine,
            textStyle = TextStyle(
                color = DuaColors.TextDark,
                fontSize = if (isArabic) 16.sp else 13.5.sp,
                fontFamily = if (isArabic) FontFamily.Serif else FontFamily.Default,
                textAlign = if (isArabic) TextAlign.Right else TextAlign.Left
            ),
            cursorBrush = SolidColor(DuaColors.Emerald),
            modifier = Modifier.fillMaxWidth()
        )
    }
}