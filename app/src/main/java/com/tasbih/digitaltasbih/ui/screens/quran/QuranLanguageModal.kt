package com.tasbih.digitaltasbih.ui.screens.quran

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.tasbih.digitaltasbih.data.TanzilOfflineRepository
import com.tasbih.digitaltasbih.ui.theme.GoldAccent

data class LangItem(
    val code: String,
    val name: String,
    val nativeName: String,
    val flag: String
)

val AvailableQuranLanguages = listOf(
    LangItem("en", "English", "English (Saheeh)", "🇬🇧"),
    LangItem("hi", "Hindi", "हिंदी (फ़ारूक़ / नदवी)", "🇮🇳"),
    LangItem("ur", "Urdu", "اردو (جالندھری)", "🇮🇳"),
    LangItem("ar", "Arabic", "العربية (تفسير)", "🇸🇦"),
    LangItem("bn", "Bengali", "বাংলা (মুহিউদ্দীন)", "🇧🇩"),
    LangItem("ta", "Tamil", "தமிழ்", "🇮🇳"),
    LangItem("ml", "Malayalam", "മലയാളം", "🇮🇳"),
    LangItem("gu", "Gujarati", "ગુજરાતી", "🇮🇳"),
    LangItem("mr", "Marathi", "मराठी", "🇮🇳"),
    LangItem("te", "Telugu", "తెలుగు", "🇮🇳"),
    LangItem("kn", "Kannada", "ಕನ್ನಡ", "🇮🇳"),
    LangItem("tr", "Turkish", "Türkçe (Diyanet)", "🇹🇷"),
    LangItem("id", "Indonesian", "Bahasa Indonesia", "🇮🇩"),
    LangItem("fr", "French", "Français (Hamidullah)", "🇫🇷"),
    LangItem("ru", "Russian", "Русский (Кулиев)", "🇷🇺"),
    LangItem("de", "German", "Deutsch (Bubenheim)", "🇩🇪"),
    LangItem("es", "Spanish", "Español (Garcia)", "🇪🇸"),
    LangItem("it", "Italian", "Italiano (Piccardo)", "🇮🇹"),
    LangItem("fa", "Persian", "فارسی (انصاریان)", "🇮🇷"),
    LangItem("zh", "Chinese", "中文 (马坚)", "🇨🇳"),
    LangItem("sw", "Swahili", "Kiswahili (Barwani)", "🇹🇿"),
    LangItem("so", "Somali", "Soomaali (Abduh)", "🇸🇴"),
    LangItem("ps", "Pashto", "پښتو (عبدالولی)", "🇦🇫"),
    LangItem("uz", "Uzbek", "Oʻzbekcha (Sodiq)", "🇺🇿"),
    LangItem("ku", "Kurdish", "Kurdî (Asan)", "🇮🇶"),
    LangItem("bs", "Bosnian", "Bosanski (Korkut)", "🇧🇦"),
    LangItem("pt", "Portuguese", "Português (El-Hayek)", "🇵🇹"),
    LangItem("nl", "Dutch", "Nederlands (Keyzer)", "🇳🇱"),
    LangItem("no", "Norwegian", "Norsk (Berg)", "🇳🇴"),
    LangItem("sv", "Swedish", "Svenska (Bernström)", "🇸🇪"),
    LangItem("pl", "Polish", "Polski (Bielawski)", "🇵🇱"),
    LangItem("ro", "Romanian", "Română (Grigore)", "🇷🇴"),
    LangItem("sq", "Albanian", "Shqip (Ahmeti)", "🇦🇱"),
    LangItem("bg", "Bulgarian", "Български (Теофанов)", "🇧🇬"),
    LangItem("cs", "Czech", "Čeština (Hrbek)", "🇨🇿"),
    LangItem("ha", "Hausa", "Hausa (Gumi)", "🇳🇬"),
    LangItem("ja", "Japanese", "日本語", "🇯🇵"),
    LangItem("ko", "Korean", "한국어", "🇰🇷"),
    LangItem("th", "Thai", "ไทย", "🇹🇭"),
    LangItem("tg", "Tajik", "Тоҷикӣ (Оятӣ)", "🇹🇯"),
    LangItem("az", "Azerbaijani", "Azərbaycan (Məmmədəliyev)", "🇦🇿"),
    LangItem("ms", "Malay", "Bahasa Melayu (Basmeih)", "🇲🇾")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranLanguageModal(
    currentLanguage: String,
    onSelectLanguage: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFFF9F7F1),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .padding(bottom = 28.dp)
        ) {
            // Header: Title & Close
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "TRANSLATION LANGUAGE",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0D4A3E),
                        fontFamily = FontFamily.Serif,
                        letterSpacing = 0.5.sp
                    )
                    Box(
                        modifier = Modifier
                            .padding(top = 3.dp)
                            .width(55.dp)
                            .height(2.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(GoldAccent)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEDE8DD))
                        .clickable(onClick = onDismiss),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color(0xFF0D4A3E),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 440.dp)
            ) {
                items(AvailableQuranLanguages) { lang ->
                    val isSelected = lang.code == currentLanguage
                    val isOffline = remember(lang.code) {
                        TanzilOfflineRepository.isLanguageAvailableOffline(context, lang.code)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSelected) Color(0xFF0D4A3E).copy(alpha = 0.08f) else Color(0xFFFFFDF8))
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) Color(0xFF0D4A3E) else GoldAccent.copy(alpha = 0.35f),
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable {
                                onSelectLanguage(lang.code)
                                onDismiss()
                            }
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(text = lang.flag, fontSize = 24.sp)
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = lang.name,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0D4A3E)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    if (isOffline) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(Color(0xFFE8F5E9))
                                                .padding(horizontal = 5.dp, vertical = 1.dp)
                                        ) {
                                            Text(
                                                text = "⚡ Offline",
                                                fontSize = 9.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF2E7D32)
                                            )
                                        }
                                    } else if (lang.code in listOf("gu", "mr", "te", "kn")) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(Color(0xFFE0F2FE))
                                                .padding(horizontal = 5.dp, vertical = 1.dp)
                                        ) {
                                            Text(
                                                text = "🌐 Online",
                                                fontSize = 9.5.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color(0xFF0369A1)
                                            )
                                        }
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(Color(0xFFFEF3C7))
                                                .padding(horizontal = 5.dp, vertical = 1.dp)
                                        ) {
                                            Text(
                                                text = "☁️ 1-Tap Offline",
                                                fontSize = 9.5.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color(0xFFB45309)
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = lang.nativeName,
                                    fontSize = 12.sp,
                                    color = Color(0xFF6B7280)
                                )
                            }
                        }

                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF0D4A3E)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Color.White,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
