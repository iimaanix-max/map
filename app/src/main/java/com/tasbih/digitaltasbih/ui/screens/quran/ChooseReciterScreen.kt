package com.tasbih.digitaltasbih.ui.screens.quran

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.data.QuranData
import com.tasbih.digitaltasbih.data.QuranRepository
import com.tasbih.digitaltasbih.ui.theme.GoldAccent

@Composable
fun ChooseReciterScreen(
    onBack: () -> Unit
) {
    var settings by remember { mutableStateOf(QuranRepository.loadSettings()) }
    val language = settings.translationLanguage

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(Color(0xFFF9F7F1))
    ) {
        // Top Bar: [ ← Back ], CHOOSE RECITER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .shadow(2.dp, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFFFFDF8))
                    .border(1.2.dp, GoldAccent.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                    .clickable(onClick = onBack)
                    .padding(horizontal = 14.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF0D4A3E),
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = when (language) {
                        "hi" -> "वापस"
                        "ur" -> "واپس"
                        "ar" -> "رجوع"
                        "bn" -> "ফিরে যান"
                        else -> "Back"
                    },
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D4A3E)
                )
            }

            Spacer(modifier = Modifier.weight(0.7f))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = when (language) {
                        "hi" -> "क़ारी का चयन"
                        "ur" -> "قاری کا انتخاب"
                        "ar" -> "اختر القارئ"
                        "bn" -> "ক্বারী নির্বাচন"
                        else -> "CHOOSE RECITER"
                    },
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D4A3E),
                    fontFamily = FontFamily.Serif,
                    letterSpacing = 1.sp
                )
                Box(
                    modifier = Modifier
                        .width(42.dp)
                        .height(2.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(GoldAccent)
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        // Reciters List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(QuranData.reciters, key = { it.identifier }) { reciter ->
                val isSelected = settings.reciterId == reciter.identifier

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(1.5.dp, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFFFFDF8))
                        .border(
                            1.2.dp,
                            if (isSelected) Color(0xFF0D4A3E) else GoldAccent.copy(alpha = 0.45f),
                            RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            val updated = settings.copy(
                                reciterId = reciter.identifier,
                                reciterName = reciter.getLocalizedName(language)
                            )
                            settings = updated
                            QuranRepository.saveSettings(updated)
                        }
                        .padding(horizontal = 14.dp, vertical = 14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // Letter Circle
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF0D4A3E)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = reciter.getLocalizedLetter(language),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = reciter.getLocalizedName(language),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF1F2937)
                                    )
                                    if (reciter.flag.isNotEmpty()) {
                                        Text(
                                            text = reciter.flag,
                                            fontSize = 16.sp
                                        )
                                    }
                                }
                                val locCountry = reciter.getLocalizedCountry(language)
                                if (locCountry.isNotEmpty()) {
                                    Text(
                                        text = locCountry,
                                        fontSize = 11.5.sp,
                                        color = Color(0xFF7A6840)
                                    )
                                }
                            }
                        }

                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = Color(0xFFB45309), // Gold checkmark matching screenshot
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
