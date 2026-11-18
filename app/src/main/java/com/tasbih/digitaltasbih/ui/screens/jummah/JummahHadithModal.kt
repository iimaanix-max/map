package com.tasbih.digitaltasbih.ui.screens.jummah

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tasbih.digitaltasbih.data.jummah.JummahHadithDetail

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun JummahHadithModal(
    hadith: JummahHadithDetail,
    lang: String,
    isDarkTheme: Boolean = false,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val translationText = when (lang) {
        "hi" -> hadith.hi.ifBlank { hadith.en }
        "ur" -> hadith.ur.ifBlank { hadith.en }
        else -> hadith.en
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 20.dp, shape = RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                color = if (isDarkTheme) Color(0xFF101C16) else Color.White,
                border = androidx.compose.foundation.BorderStroke(
                    2.dp,
                    if (isDarkTheme) Color(0xFF10B981) else Color(0xFF0E5D47)
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // 1. Header (Emerald Gradient + Golden accent border bottom)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF0E5D47), Color(0xFF063B2C))
                                )
                            )
                            .border(
                                width = 1.5.dp,
                                color = Color(0xFFF6D365),
                                shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp)
                            )
                            .padding(horizontal = 18.dp, vertical = 15.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "📜",
                                    fontSize = 20.sp
                                )
                                Text(
                                    text = "AUTHENTIC HADITH",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 0.8.sp,
                                    color = Color.White,
                                    fontFamily = FontFamily.Serif
                                )
                            }

                            // Round close button
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.18f))
                                    .clickable { onDismiss() },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "✕",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }

                    // 2. Scrollable Body
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(scrollState)
                            .padding(18.dp)
                    ) {
                        // Badges Row (Reference & Grade)
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Reference Tag
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (isDarkTheme) Color(0xFF3B270C) else Color(0xFFFEF3C7),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .border(
                                        1.dp,
                                        Color(0xFFF59E0B),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "📖 ${hadith.ref}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDarkTheme) Color(0xFFFDE68A) else Color(0xFF92400E)
                                )
                            }

                            // Sahih Grade Tag
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (isDarkTheme) Color(0xFF063E2D) else Color(0xFFD1FAE5),
                                        RoundedCornerShape(6.dp)
                                    )
                                    .border(
                                        1.dp,
                                        Color(0xFF10B981),
                                        RoundedCornerShape(6.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "✅ ${hadith.grade}",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDarkTheme) Color(0xFF6EE7B7) else Color(0xFF065F46)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Arabic Text Box (RTL, Emerald Calligraphy)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    if (isDarkTheme) Color(0xFF09291D) else Color(0xFFF0FDF4),
                                    RoundedCornerShape(14.dp)
                                )
                                .border(
                                    1.dp,
                                    Color(0xFF10B981).copy(alpha = 0.4f),
                                    RoundedCornerShape(14.dp)
                                )
                                .padding(16.dp)
                        ) {
                            Text(
                                text = hadith.ar,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End,
                                fontSize = 19.sp,
                                lineHeight = 32.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = FontFamily.Serif,
                                color = if (isDarkTheme) Color(0xFF6EE7B7) else Color(0xFF064E3B)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Localized Translation Box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    if (isDarkTheme) Color(0xFF16231D) else Color(0xFFF8FAFC),
                                    RoundedCornerShape(14.dp)
                                )
                                .border(
                                    1.dp,
                                    if (isDarkTheme) Color(0xFF2E453A) else Color(0xFFE2E8F0),
                                    RoundedCornerShape(14.dp)
                                )
                                .padding(14.dp)
                        ) {
                            Column {
                                Text(
                                    text = "TRANSLATION (${lang.uppercase()}):",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B),
                                    letterSpacing = 0.5.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = translationText,
                                    fontSize = 13.5.sp,
                                    lineHeight = 20.sp,
                                    color = if (isDarkTheme) Color(0xFFE2E8F0) else Color(0xFF1E293B)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Direct Source Links
                        if (hadith.links.isNotEmpty()) {
                            Text(
                                text = "AUTHENTIC REFERENCES & SOURCE LINKS:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B),
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            hadith.links.forEach { link ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            Brush.linearGradient(
                                                listOf(Color(0xFF0E5D47), Color(0xFF063B2C))
                                            )
                                        )
                                        .border(
                                            1.2.dp,
                                            Color(0xFFF6D365),
                                            RoundedCornerShape(12.dp)
                                        )
                                        .clickable {
                                            try {
                                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link.url))
                                                context.startActivity(intent)
                                            } catch (_: Exception) {}
                                        }
                                        .padding(horizontal = 14.dp, vertical = 11.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text(text = "🌐", fontSize = 13.sp)
                                            Text(
                                                text = link.label,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                        Text(
                                            text = "↗",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFF6D365)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Close Button
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isDarkTheme) Color(0xFF1E2D24) else Color(0xFFF1F5F9),
                                contentColor = if (isDarkTheme) Color(0xFFE2E8F0) else Color(0xFF334155)
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isDarkTheme) Color(0xFF334A3E) else Color(0xFFCBD5E1)
                            )
                        ) {
                            Text(
                                text = "✕ Close",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
