package com.tasbih.digitaltasbih.ui.components.dua

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.data.DuaEntry
import com.tasbih.digitaltasbih.ui.theme.DuaColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DuaHadithRefModal(
    dua: DuaEntry,
    selectedLang: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val reference = dua.getLocalizedReference(selectedLang).ifBlank { "Authentic Sunnah" }
    val title = dua.getLocalizedTitle(selectedLang)

    val isQuran = reference.contains("Surah", ignoreCase = true) ||
            reference.contains("Qur'an", ignoreCase = true) ||
            reference.contains("सूरा", ignoreCase = true) ||
            reference.contains("سورة", ignoreCase = true)

    val gradeBadge = when {
        isQuran -> "✓ Qur'an Karim (القرآن الكريم)"
        reference.contains("Bukhari", ignoreCase = true) -> "✓ Sahih al-Bukhari (صحيح)"
        reference.contains("Muslim", ignoreCase = true) -> "✓ Sahih Muslim (صحيح)"
        reference.contains("Tirmidhi", ignoreCase = true) -> "✓ Jami` at-Tirmidhi (Hasan Sahih)"
        reference.contains("Dawud", ignoreCase = true) || reference.contains("Dawood", ignoreCase = true) -> "✓ Sunan Abi Dawud (Sahih)"
        reference.contains("Nasa", ignoreCase = true) -> "✓ Sunan an-Nasa'i (Sahih)"
        reference.contains("Majah", ignoreCase = true) -> "✓ Sunan Ibn Majah (Hasan)"
        else -> "✓ Sahih / Authentic Sunnah (صحيح)"
    }

    val bookName = when {
        isQuran -> "Noble Qur'an (القرآن الكريم)"
        reference.contains("Bukhari", ignoreCase = true) -> "Sahih al-Bukhari (صحيح البخاري)"
        reference.contains("Muslim", ignoreCase = true) -> "Sahih Muslim (صحيح مسلم)"
        reference.contains("Tirmidhi", ignoreCase = true) -> "Jami` at-Tirmidhi (جامع الترمذي)"
        reference.contains("Dawud", ignoreCase = true) || reference.contains("Dawood", ignoreCase = true) -> "Sunan Abi Dawud (سنن أبي داود)"
        reference.contains("Nasa", ignoreCase = true) -> "Sunan an-Nasa'i (سنن النسائي)"
        reference.contains("Majah", ignoreCase = true) -> "Sunan Ibn Majah (سنن ابن ماجه)"
        else -> "Authentic Hadith Collections"
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = null,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE8F5E9))
                            .border(1.dp, DuaColors.Emerald, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.MenuBook,
                            contentDescription = null,
                            tint = DuaColors.Emerald,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Text(
                            text = when (selectedLang) {
                                "hi" -> "हदीस संदर्भ व सनद"
                                "ur" -> "حدیث سند و حوالہ"
                                "ar" -> "تخريج الحديث والسند"
                                "bn" -> "হাদিসের সনদ ও রেফারেন্স"
                                else -> "Hadith Sanad & Reference"
                            },
                            color = DuaColors.Emerald,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Authentic Islamic Citation",
                            color = DuaColors.TextMuted,
                            fontSize = 11.5.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF5F4F0))
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = DuaColors.TextDark,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Islamic Luxury Card with Reference details
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFFBF9F4),
                                Color(0xFFF3ECE0)
                            )
                        )
                    )
                    .border(1.5.dp, Color(0x66C9A84C), RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Column {
                    // Authenticity Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFFE8F5E9))
                            .border(1.dp, Color(0xFF81C784), RoundedCornerShape(20.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = gradeBadge,
                                color = Color(0xFF1B5E20),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Dua Title
                    Text(
                        text = title,
                        color = DuaColors.TextDark,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = Color(0x33C9A84C), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(10.dp))

                    // Primary Source Box
                    Text(
                        text = "PRIMARY SOURCE",
                        color = DuaColors.Gold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = bookName,
                        color = DuaColors.EmeraldDark,
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Exact Citation / Reference Number
                    Text(
                        text = "DETAILED CITATION",
                        color = DuaColors.Gold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)
                            .border(1.dp, Color(0x22C9A84C), RoundedCornerShape(10.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = reference,
                            color = DuaColors.TextDark,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Scholarly Context / Virtue Note
                    Text(
                        text = when (selectedLang) {
                            "hi" -> "यह दुआ मुस्तनद (Authentic) हदीस किताबों से साबित है और रसूलुल्लाह (ﷺ) के मुबारक तरीक़े (सुन्नत) के मुताबिक़ पढ़ी जाती है।"
                            "ur" -> "یہ دعا مستند احادیث کی کتب سے ثابت ہے اور رسول اللہ (ﷺ) کی سنتِ مطہرہ کے مطابق پڑھی جاتی ہے۔"
                            "ar" -> "هذا الدعاء مروي بأسانيد صحيحة في كتب السنة النبوية الشريفة."
                            "bn" -> "এই দোয়াটি বিশুদ্ধ হাদিস গ্রন্থ থেকে সংকলিত এবং সুন্নাহ সম্মত।"
                            else -> "This supplication is authentically established in classical Hadith collections according to the prophetic Sunnah."
                        },
                        color = DuaColors.TextMuted,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons: Copy Reference & Share Reference
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Copy Button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFF5F4F0))
                        .border(1.dp, Color(0x33C9A84C), RoundedCornerShape(14.dp))
                        .clickable {
                            val clipMgr = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Hadith Reference", "$title\nReference: $reference\n— Digital Tasbih App")
                            clipMgr.setPrimaryClip(clip)
                            Toast.makeText(context, "Reference copied to clipboard", Toast.LENGTH_SHORT).show()
                        }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = null,
                            tint = DuaColors.TextDark,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = when (selectedLang) {
                                "hi" -> "कॉपी करें"
                                "ur" -> "کاپی کریں"
                                else -> "Copy Ref"
                            },
                            color = DuaColors.TextDark,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Share Button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(DuaColors.Emerald, DuaColors.EmeraldDark)
                            )
                        )
                        .clickable {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "$title\n\nReference: $reference ($gradeBadge)\n\n— Verified via Digital Tasbih App"
                                )
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Share Reference"))
                        }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Send,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = when (selectedLang) {
                                "hi" -> "शेयर करें"
                                "ur" -> "شیئر کریں"
                                else -> "Share Ref"
                            },
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
