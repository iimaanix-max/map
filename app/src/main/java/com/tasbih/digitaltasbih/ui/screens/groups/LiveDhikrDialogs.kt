package com.tasbih.digitaltasbih.ui.screens.groups

import com.tasbih.digitaltasbih.ui.components.rememberAssetPainter
import com.tasbih.digitaltasbih.R
import androidx.compose.ui.res.painterResource
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import kotlinx.coroutines.delay


import android.app.Activity
import android.content.Context
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tasbih.digitaltasbih.data.PrayerRepository
import com.tasbih.digitaltasbih.data.groups.*
import com.tasbih.digitaltasbih.ui.components.AppToast
import com.tasbih.digitaltasbih.util.AdMobRewardedManager
import kotlinx.coroutines.launch

// ── DhikrCatalogItem Data Class ──
data class DhikrCatalogItem(
    val id: String,
    val arabic: String,
    val translit: String,
    val meaning: String,
    val category: String,
    val badge: String = "Dhikr"
)

// ── 20-Item Official Dhikr Catalog ──
val FULL_DHIKR_CATALOG: List<DhikrCatalogItem> = listOf(
    DhikrCatalogItem("subhan", "سُبْحَانَ اللّٰہ", "SubhanAllah", "Glory be to Allah", "tasbih", "Tasbih"),
    DhikrCatalogItem("alhamd", "اَلْحَمْدُ لِلّٰہ", "Alhamdulillah", "All praise is for Allah", "tasbih", "Tahmeed"),
    DhikrCatalogItem("akbar", "اَللّٰہُ اَکْبَر", "Allahu Akbar", "Allah is the Greatest", "tasbih", "Takbeer"),
    DhikrCatalogItem("tahlil", "لَا اِلٰہَ اِلَّا اللّٰہ", "La ilaha illallah", "None has the right to be worshipped but Allah", "tasbih", "Tahleel"),
    DhikrCatalogItem("istighfar", "اَسْتَغْفِرُ اللّٰہ", "Astaghfirullah", "I seek forgiveness of Allah", "istighfar", "Istighfar"),
    DhikrCatalogItem("istighfar_long", "اَسْتَغْفِرُ اللَّهَ رَبِّي مِنْ كُلِّ ذَنْبٍ وَّاتُوْبُ اِلَيْهِ", "Astaghfirullah Rabbi min Kulli Zambin", "I ask forgiveness of Allah for every sin and turn to Him in repentance", "istighfar", "Istighfar"),
    DhikrCatalogItem("sayyidul_istighfar", "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَهَ إِلَّا أَنْتَ خَلَقْتَنِي وَأَنَا عَبْدُكَ", "Sayyidul Istighfar", "O Allah, You are my Lord, none has the right to be worshipped but You", "istighfar", "Master Istighfar"),
    DhikrCatalogItem("durood_short", "اَللّٰھُمَّ صَلِّ عَلٰی مُحَمَّدٍ وَّعَلٰٓی اٰلِ مُحَمَّدٍ", "Durood Shareef (Salawat)", "O Allah, send blessings upon Muhammad and upon the family of Muhammad", "durood", "Salawat"),
    DhikrCatalogItem("durood_ibrahimi", "اللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ وَعَلَى آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلَى إِبْرَاهِيمَ", "Durood-e-Ibrahimi", "O Allah, bestow Your favor upon Muhammad and the family of Muhammad", "durood", "Salawat"),
    DhikrCatalogItem("durood_tunajjina", "اللَّهُمَّ صَلِّ عَلَى سَيِّدِنَا مُحَمَّدٍ صَلاَةً تُنَجِّينَا بِهَا مِنْ جَمِيعِ الأَهْوَالِ وَالآفَاتِ", "Durood-e-Tunajjina", "Prayer of Deliverance upon the Prophet", "durood", "Salawat"),
    DhikrCatalogItem("hauqala", "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللّٰهِ", "La hawla wa la quwwata illa billah", "There is no power nor strength except with Allah", "tasbih", "Hauqala"),
    DhikrCatalogItem("subhan_azeem", "سُبْحَانَ اللّٰهِ وَبِحَمْدِهِ سُبْحَانَ اللّٰهِ الْعَظِيْمِ", "SubhanAllahi wa bihamdihi SubhanAllahil Azeem", "Glory be to Allah and all praise; glory be to Allah the Supreme", "tasbih", "Tasbih"),
    DhikrCatalogItem("ayatul_kursi", "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ", "Ayat-ul-Kursi", "Allah! There is no deity except Him, the Ever-Living, the Sustainer of all existence", "tasbih", "Quranic Ayah"),
    DhikrCatalogItem("dua_yunus", "لَّا إِلَٰهَ إِلَّا أَنتَ سُبْحَانَكَ إِنِّي كُنتُ مِنَ الظَّالِمِينَ", "Dua-e-Yunus (Ayat-e-Kareema)", "There is no deity except You; exalted are You. Indeed, I have been of the wrongdoers", "istighfar", "Supplication"),
    DhikrCatalogItem("rabbana_atina", "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الْآخِرَةِ حَسَنَةً", "Rabbana Atina fid-Dunya", "Our Lord, give us in this world that which is good and in the Hereafter good", "tasbih", "Quranic Dua"),
    DhikrCatalogItem("asma_1", "اَللهُ", "Allah", "The Greatest Name, The One and Only", "asma", "Name #1"),
    DhikrCatalogItem("asma_2", "اَلرَّحْمٰن", "Ar-Rahman", "The Most Gracious", "asma", "Name #2"),
    DhikrCatalogItem("asma_3", "اَلرَّحِيْم", "Ar-Raheem", "The Most Merciful", "asma", "Name #3"),
    DhikrCatalogItem("asma_4", "اَلْمَلِك", "Al-Malik", "The Absolute Sovereign", "asma", "Name #4"),
    DhikrCatalogItem("asma_5", "اَلْقُدُّوْس", "Al-Quddus", "The Most Holy, Free from all imperfections", "asma", "Name #5"),
    DhikrCatalogItem("asma_6", "اَلسَّلَام", "As-Salam", "The Source of Peace and Safety", "asma", "Name #6"),
    DhikrCatalogItem("asma_7", "اَلْمُؤْمِن", "Al-Mu'min", "The Guardian of Faith, Granter of Security", "asma", "Name #7"),
    DhikrCatalogItem("asma_8", "اَلْمُهَيْمِن", "Al-Muhaymin", "The Protector, The Ever-Watching", "asma", "Name #8"),
    DhikrCatalogItem("asma_9", "اَلْعَزِيْز", "Al-Aziz", "The All-Mighty, The Invincible", "asma", "Name #9"),
    DhikrCatalogItem("asma_10", "اَلْجَبَّار", "Al-Jabbar", "The Compeller, The Restorer", "asma", "Name #10"),
    DhikrCatalogItem("asma_11", "اَلْمُتَكَبِّر", "Al-Mutakabbir", "The Supreme, The Majestic", "asma", "Name #11"),
    DhikrCatalogItem("asma_12", "اَلْخَالِق", "Al-Khaliq", "The Creator, The Maker", "asma", "Name #12"),
    DhikrCatalogItem("asma_13", "اَلْبَارِئ", "Al-Bari'", "The Evolver, The Maker of Order", "asma", "Name #13"),
    DhikrCatalogItem("asma_14", "اَلْمُصَوِّر", "Al-Musawwir", "The Fashioner, The Shaper of Beauty", "asma", "Name #14"),
    DhikrCatalogItem("asma_15", "اَلْغَفَّار", "Al-Ghaffar", "The Constant Forgiver", "asma", "Name #15"),
    DhikrCatalogItem("asma_16", "اَلْقَهَّار", "Al-Qahhar", "The Subduer, The Dominant", "asma", "Name #16"),
    DhikrCatalogItem("asma_17", "اَلْوَهَّاب", "Al-Wahhab", "The Bestower, The Giver of Gifts", "asma", "Name #17"),
    DhikrCatalogItem("asma_18", "اَلرَّزَّاق", "Ar-Razzaq", "The Provider, The Sustainer", "asma", "Name #18"),
    DhikrCatalogItem("asma_19", "اَلْفَتَّاح", "Al-Fattah", "The Opener, The Reliever", "asma", "Name #19"),
    DhikrCatalogItem("asma_20", "اَلْعَلِيْم", "Al-Aleem", "The All-Knowing, The Omniscient", "asma", "Name #20")
)

// ── PillButton Composable ──
@Composable
fun PillButton(
    title: String,
    iconPath: String? = null,
    fallbackIcon: ImageVector? = null,
    isSelected: Boolean = false,
    isDarkTheme: Boolean = false,
    onClick: () -> Unit
) {
    val painter = if (iconPath != null) rememberAssetPainter(iconPath) else null
    val bgColor = if (isSelected) Color(0xFF0E7A52) else if (isDarkTheme) Color(0xFF072418) else Color(0xFFF1F8F2)
    val borderColor = if (isSelected) Color(0xFF0E7A52) else if (isDarkTheme) Color(0x40C5A059) else Color(0x339E9E9E)
    val textColor = if (isSelected) Color.White else if (isDarkTheme) Color(0xFFD6C8A6) else Color(0xFF133E2B)

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        if (painter != null) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        } else if (fallbackIcon != null) {
            Icon(
                imageVector = fallbackIcon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(15.dp)
            )
        }
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = textColor
        )
    }
}

// ── PulsingLiveDot Composable ──
@Composable
fun PulsingLiveDot(
    color: Color = Color(0xFFEF4444),
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition("pulse")
    val alpha by transition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    Box(
        modifier = modifier
            .size(7.dp)
            .background(color.copy(alpha = alpha), CircleShape)
    )
}

// ── DeleteGroupConfirmDialog ──
@Composable
fun DeleteGroupConfirmDialog(
    campaignTitle: String,
    onWatchAdAndDelete: () -> Unit,
    onDismiss: () -> Unit,
    isDarkTheme: Boolean = false
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDarkTheme) Color(0xFF16251E) else Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color(0xFFFFEBEE), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🗑️", fontSize = 26.sp)
                }

                Text(
                    text = "Delete Dhikr Group?",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) Color.White else Color(0xFF1E293B)
                )

                Text(
                    text = "Group: \"$campaignTitle\"",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFDC2626)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (isDarkTheme) Color(0xFF0F1A15) else Color(0xFFF8FAFC),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("⚠️ All joined reciters will be disconnected", fontSize = 12.sp, color = if (isDarkTheme) Color(0xFFCBD5E1) else Color(0xFF64748B))
                    Text("🚫 Group join code will become inactive", fontSize = 12.sp, color = if (isDarkTheme) Color(0xFFCBD5E1) else Color(0xFF64748B))
                    Text("🎬 Short sponsored ad required before deletion", fontSize = 12.sp, color = if (isDarkTheme) Color(0xFFCBD5E1) else Color(0xFF64748B))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (isDarkTheme) Color(0xFFE2E8F0) else Color(0xFF475569)
                        )
                    ) {
                        Text("Cancel", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onWatchAdAndDelete,
                        modifier = Modifier.weight(1.3f),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFDC2626)
                        )
                    ) {
                        Text("🎬 Watch Ad & Delete", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// ── LeaderboardAdPromptDialog ──
@Composable
fun LeaderboardAdPromptDialog(
    campaignTitle: String,
    onWatchAd: () -> Unit,
    onDismiss: () -> Unit,
    isDarkTheme: Boolean = false
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDarkTheme) Color(0xFF16251E) else Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color(0xFFFEF3C7), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("👥", fontSize = 26.sp)
                }

                Text(
                    text = "Unlock Group Leaderboard",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) Color.White else Color(0xFF1E293B)
                )

                Text(
                    text = "See all reciters & contributions for \"$campaignTitle\"",
                    fontSize = 13.5.sp,
                    textAlign = TextAlign.Center,
                    color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)
                )

                Text(
                    text = "🎬 Watch a short sponsored video to view the live rankings & top contributors.",
                    fontSize = 12.5.sp,
                    textAlign = TextAlign.Center,
                    color = if (isDarkTheme) Color(0xFFE2E8F0) else Color(0xFF334155)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Not Now", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onWatchAd,
                        modifier = Modifier.weight(1.3f),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E7A52))
                    ) {
                        Text("Watch Ad ➔", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

// ── NoInternetConnectionDialog ──
@Composable
fun NoInternetConnectionDialog(
    onDismiss: () -> Unit,
    onRetry: () -> Unit,
    isDarkTheme: Boolean = false
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDarkTheme) Color(0xFF16251E) else Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text("📡 ⚠️", fontSize = 36.sp)

                Text(
                    text = "No Internet Connection",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFEF4444)
                )

                Text(
                    text = "No internet connection detected. Please verify your connection to continue live reciting.",
                    fontSize = 13.5.sp,
                    color = if (isDarkTheme) Color(0xFFCBD5E1) else Color(0xFF374151),
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Close", fontWeight = FontWeight.SemiBold)
                    }

                    Button(
                        onClick = onRetry,
                        modifier = Modifier.weight(1.2f),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E7A52))
                    ) {
                        Text("Retry Connection", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

// ── SmartAutoCounterModal ──
@Composable
fun SmartAutoCounterModal(
    isRunning: Boolean,
    currentSpeedMs: Long,
    onToggle: () -> Unit,
    onSelectSpeed: (Long) -> Unit,
    onDismiss: () -> Unit,
    isDarkTheme: Boolean = false
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDarkTheme) Color(0xFF16251E) else Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("⚡ Smart Auto Reciter", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = if (isDarkTheme) Color.White else Color(0xFF063321))

                Text(
                    text = "Automated recitation pulse interval",
                    fontSize = 13.sp,
                    color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)
                )

                val speedOptions = listOf(1000L to "1.0s", 1500L to "1.5s", 2000L to "2.0s", 2500L to "2.5s", 3000L to "3.0s")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    speedOptions.forEach { (speedMs, label) ->
                        val isSelected = currentSpeedMs == speedMs
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) Color(0xFF0E7A52) else (if (isDarkTheme) Color(0xFF0E1A14) else Color(0xFFF1F5F9)))
                                .border(1.dp, if (isSelected) Color(0xFF0E7A52) else Color(0x33000000), RoundedCornerShape(12.dp))
                                .clickable { onSelectSpeed(speedMs) }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else (if (isDarkTheme) Color(0xFFE2E8F0) else Color(0xFF334155))
                            )
                        }
                    }
                }

                Button(
                    onClick = onToggle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isRunning) Color(0xFFDC2626) else Color(0xFF0E7A52)
                    )
                ) {
                    Text(
                        text = if (isRunning) "⏹ Stop Auto Reciting" else "▶ Start Auto Reciting",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Close", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

// ── JoinByCodeDialog ──
@Composable
fun JoinByCodeDialog(
    onDismiss: () -> Unit,
    onJoin: (String) -> Unit,
    isDarkTheme: Boolean = false
) {
    var code by remember { mutableStateOf("") }
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDarkTheme) Color(0xFF16251E) else Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("🔑 Join with Code", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = if (isDarkTheme) Color.White else Color(0xFF063321))

                Text(
                    text = "Enter the group code shared by your organizer (e.g. GC-8F2KL91X).",
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)
                )

                OutlinedTextField(
                    value = code,
                    onValueChange = { if (it.length <= 12) code = it.uppercase() },
                    placeholder = { Text("e.g. GC-8F2KL91X", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Cancel", fontWeight = FontWeight.SemiBold)
                    }

                    Button(
                        onClick = { if (code.isNotBlank()) onJoin(code.trim()) },
                        enabled = code.trim().length >= 4,
                        modifier = Modifier.weight(1.3f),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E7A52))
                    ) {
                        Text("Join Group ➔", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

// ── DhikrPickerSheet ──
@Composable
fun DhikrPickerSheet(
    onDismiss: () -> Unit,
    onSelect: (DhikrCatalogItem) -> Unit,
    isDarkTheme: Boolean = false
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCat by remember { mutableStateOf("all") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = if (isDarkTheme) Color(0xFF0C1913) else Color(0xFFF8FAF7)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // TopBar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Choose Zikr Campaign",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White else Color(0xFF063321)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = if (isDarkTheme) Color.White else Color.Black)
                    }
                }

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by name or Arabic...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Categories Row
                val categories = listOf("all" to "All", "tasbih" to "Tasbih", "istighfar" to "Istighfar", "durood" to "Durood", "asma" to "99 Names")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { (catId, label) ->
                        val isSelected = selectedCat == catId
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(18.dp))
                                .background(if (isSelected) Color(0xFF0E7A52) else (if (isDarkTheme) Color(0xFF16251E) else Color.White))
                                .border(1.dp, if (isSelected) Color(0xFF0E7A52) else Color(0x33000000), RoundedCornerShape(18.dp))
                                .clickable { selectedCat = catId }
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else (if (isDarkTheme) Color(0xFFE2E8F0) else Color(0xFF334155))
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // List
                val filtered = remember(searchQuery, selectedCat) {
                    FULL_DHIKR_CATALOG.filter { item ->
                        val matchCat = selectedCat == "all" || item.category == selectedCat
                        val matchSearch = searchQuery.isBlank() ||
                                item.translit.contains(searchQuery, ignoreCase = true) ||
                                item.arabic.contains(searchQuery, ignoreCase = true) ||
                                item.meaning.contains(searchQuery, ignoreCase = true)
                        matchCat && matchSearch
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filtered) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelect(item)
                                    onDismiss()
                                },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isDarkTheme) Color(0xFF16251E) else Color.White
                            ),
                            border = BorderStroke(1.dp, if (isDarkTheme) Color(0x22C5A059) else Color(0x1A0E7A52))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = item.translit,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDarkTheme) Color.White else Color(0xFF063321)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFFE8F5E9))
                                            .padding(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text(item.badge, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0E7A52))
                                    }
                                }

                                Text(
                                    text = item.arabic,
                                    fontSize = 18.sp,
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFFD97706)
                                )

                                Text(
                                    text = item.meaning,
                                    fontSize = 12.sp,
                                    color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ── LocationSelectionDialog ──
@Composable
fun LocationSelectionDialog(
    title: String,
    items: List<String>,
    selectedItem: String = "",
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit,
    isDarkTheme: Boolean = false
) {
    var query by remember { mutableStateOf("") }
    val filtered = remember(query, items) {
        if (query.isBlank()) items else items.filter { it.contains(query, ignoreCase = true) }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDarkTheme) Color(0xFF16251E) else Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = if (isDarkTheme) Color.White else Color(0xFF063321))
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = if (isDarkTheme) Color.White else Color.Black)
                    }
                }

                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("Search location...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(filtered) { item ->
                        val isSelected = item.equals(selectedItem, ignoreCase = true)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) Color(0xFFE8F5E9) else Color.Transparent)
                                .clickable {
                                    onSelect(item)
                                    onDismiss()
                                }
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item,
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color(0xFF0E7A52) else (if (isDarkTheme) Color(0xFFE2E8F0) else Color(0xFF1E293B))
                            )
                            if (isSelected) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF0E7A52), modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}


// ── Country Dial Codes Data & Selection Dialog ──
data class CountryDialCode(
    val country: String,
    val flag: String,
    val dialCode: String,
    val sampleDigits: String = "10 digits"
)

val WORLD_DIAL_CODES: List<CountryDialCode> = listOf(
    CountryDialCode("India", "🇮🇳", "+91", "10 digits"),
    CountryDialCode("Pakistan", "🇵🇰", "+92", "10-11 digits"),
    CountryDialCode("Saudi Arabia", "🇸🇦", "+966", "9 digits"),
    CountryDialCode("United Arab Emirates", "🇦🇪", "+971", "9 digits"),
    CountryDialCode("Bangladesh", "🇧🇩", "+880", "10 digits"),
    CountryDialCode("United Kingdom", "🇬🇧", "+44", "10 digits"),
    CountryDialCode("United States", "🇺🇸", "+1", "10 digits"),
    CountryDialCode("Canada", "🇨🇦", "+1", "10 digits"),
    CountryDialCode("Turkey", "🇹🇷", "+90", "10 digits"),
    CountryDialCode("Indonesia", "🇮🇩", "+62", "9-12 digits"),
    CountryDialCode("Malaysia", "🇲🇾", "+60", "9-10 digits"),
    CountryDialCode("Qatar", "🇶🇦", "+974", "8 digits"),
    CountryDialCode("Kuwait", "🇰🇼", "+965", "8 digits"),
    CountryDialCode("Oman", "🇴🇲", "+968", "8 digits"),
    CountryDialCode("Bahrain", "🇧🇭", "+973", "8 digits"),
    CountryDialCode("Egypt", "🇪🇬", "+20", "10 digits"),
    CountryDialCode("Jordan", "🇯🇴", "+962", "9 digits"),
    CountryDialCode("Iraq", "🇮🇶", "+964", "10 digits"),
    CountryDialCode("Lebanon", "🇱🇧", "+961", "8 digits"),
    CountryDialCode("Syria", "🇸🇾", "+963", "9 digits"),
    CountryDialCode("Yemen", "🇾🇪", "+967", "9 digits"),
    CountryDialCode("Palestine", "🇵🇸", "+970", "9 digits"),
    CountryDialCode("Algeria", "🇩🇿", "+213", "9 digits"),
    CountryDialCode("Morocco", "🇲🇦", "+212", "9 digits"),
    CountryDialCode("Tunisia", "🇹🇳", "+216", "8 digits"),
    CountryDialCode("Libya", "🇱🇾", "+218", "9 digits"),
    CountryDialCode("Sudan", "🇸🇩", "+249", "9 digits"),
    CountryDialCode("Somalia", "🇸🇴", "+252", "8-9 digits"),
    CountryDialCode("Afghanistan", "🇦🇫", "+93", "9 digits"),
    CountryDialCode("Iran", "🇮🇷", "+98", "10 digits"),
    CountryDialCode("Nigeria", "🇳🇬", "+234", "10 digits"),
    CountryDialCode("South Africa", "🇿🇦", "+27", "9 digits"),
    CountryDialCode("Kenya", "🇰🇪", "+254", "9 digits"),
    CountryDialCode("Singapore", "🇸🇬", "+65", "8 digits"),
    CountryDialCode("Australia", "🇦🇺", "+61", "9 digits"),
    CountryDialCode("New Zealand", "🇳🇿", "+64", "9-10 digits"),
    CountryDialCode("Germany", "🇩🇪", "+49", "10-11 digits"),
    CountryDialCode("France", "🇫🇷", "+33", "9 digits"),
    CountryDialCode("Italy", "🇮🇹", "+39", "10 digits"),
    CountryDialCode("Spain", "🇪🇸", "+34", "9 digits"),
    CountryDialCode("Netherlands", "🇳🇱", "+31", "9 digits"),
    CountryDialCode("Belgium", "🇧🇪", "+32", "9 digits"),
    CountryDialCode("Switzerland", "🇨🇭", "+41", "9 digits"),
    CountryDialCode("Sweden", "🇸🇪", "+46", "9 digits"),
    CountryDialCode("Norway", "🇳🇴", "+47", "8 digits"),
    CountryDialCode("Denmark", "🇩🇰", "+45", "8 digits"),
    CountryDialCode("Finland", "🇫🇮", "+358", "9-10 digits"),
    CountryDialCode("Russia", "🇷🇺", "+7", "10 digits"),
    CountryDialCode("Sri Lanka", "🇱🇰", "+94", "9 digits"),
    CountryDialCode("Nepal", "🇳🇵", "+977", "10 digits"),
    CountryDialCode("Maldives", "🇲🇻", "+960", "7 digits"),
    CountryDialCode("Japan", "🇯🇵", "+81", "10 digits"),
    CountryDialCode("South Korea", "🇰🇷", "+82", "10 digits"),
    CountryDialCode("China", "🇨🇳", "+86", "11 digits"),
    CountryDialCode("Brazil", "🇧🇷", "+55", "11 digits"),
    CountryDialCode("Mexico", "🇲🇽", "+52", "10 digits"),
    CountryDialCode("Argentina", "🇦🇷", "+54", "10 digits"),
    CountryDialCode("Philippines", "🇵🇭", "+63", "10 digits"),
    CountryDialCode("Thailand", "🇹🇭", "+66", "9 digits")
)

@Composable
fun CountryDialCodeSelectionDialog(
    selectedCode: CountryDialCode,
    onSelect: (CountryDialCode) -> Unit,
    onDismiss: () -> Unit,
    isDarkTheme: Boolean = false
) {
    var query by remember { mutableStateOf("") }
    val filtered = remember(query) {
        if (query.isBlank()) WORLD_DIAL_CODES
        else WORLD_DIAL_CODES.filter {
            it.country.contains(query, ignoreCase = true) ||
            it.dialCode.contains(query, ignoreCase = true)
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDarkTheme) Color(0xFF16251E) else Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Select Country Code",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White else Color(0xFF063321)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Close",
                            tint = if (isDarkTheme) Color.White else Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("Search country or code (e.g. +91, Saudi)...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(filtered) { item ->
                        val isSelected = item.country == selectedCode.country
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) Color(0xFFE8F5E9) else Color.Transparent)
                                .clickable {
                                    onSelect(item)
                                    onDismiss()
                                }
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(item.flag, fontSize = 20.sp)
                                Text(
                                    text = item.country,
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color(0xFF0E7A52) else (if (isDarkTheme) Color(0xFFE2E8F0) else Color(0xFF1E293B))
                                )
                            }
                            Text(
                                text = item.dialCode,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color(0xFF0E7A52) else Color(0xFF64748B)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── LeaderboardModalSheet ──
@Composable
fun LeaderboardModalSheet(
    campaign: CampaignItem? = null,
    campaignId: String = campaign?.campaignId ?: "",
    repository: GroupCampaignRepository,
    onDismiss: () -> Unit,
    isDarkTheme: Boolean = false
) {
    var participants by remember { mutableStateOf<List<ParticipantItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(campaignId) {
        if (campaignId.isNotBlank()) {
            try {
                val list = repository.fetchParticipants(campaignId, campaign)
                participants = list
            } catch (_: Exception) {}
            isLoading = false
        } else {
            isLoading = false
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = if (isDarkTheme) Color(0xFF0C1913) else Color(0xFFF8FAF7)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("👥", fontSize = 22.sp)
                        Column {
                            Text(
                                text = "Group Reciters & Leaderboard",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color.White else Color(0xFF063321)
                            )
                            campaign?.let {
                                Text(
                                    text = it.title,
                                    fontSize = 12.sp,
                                    color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)
                                )
                            }
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = if (isDarkTheme) Color.White else Color.Black)
                    }
                }

                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF0E7A52))
                    }
                } else if (participants.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("🤲", fontSize = 40.sp)
                            Text(
                                text = "No reciters recorded yet.",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)
                            )
                            Text(
                                text = "Be the first to recite and submit your zikr!",
                                fontSize = 13.sp,
                                color = if (isDarkTheme) Color(0xFF64748B) else Color(0xFF94A3B8)
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(participants.sortedByDescending { it.count }) { p ->
                            val index = participants.sortedByDescending { it.count }.indexOf(p)
                            val isMe = p.deviceId == repository.getDeviceId()
                            val medal = when (index) {
                                0 -> "🥇"
                                1 -> "🥈"
                                2 -> "🥉"
                                else -> "#${index + 1}"
                            }

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isMe) (if (isDarkTheme) Color(0xFF123B2A) else Color(0xFFE8F5E9))
                                    else (if (isDarkTheme) Color(0xFF16251E) else Color.White)
                                ),
                                border = if (isMe) BorderStroke(1.2.dp, Color(0xFF0E7A52)) else null
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Text(medal, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                        Column {
                                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                                Text(
                                                    text = if (p.displayName.isNotBlank()) p.displayName else "Anonymous Reciter",
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isDarkTheme) Color.White else Color(0xFF1E293B)
                                                )
                                                if (isMe) {
                                                    Box(
                                                        modifier = Modifier
                                                            .clip(RoundedCornerShape(6.dp))
                                                            .background(Color(0xFF0E7A52))
                                                            .padding(horizontal = 6.dp, vertical = 1.dp)
                                                    ) {
                                                        Text("You", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                                    }
                                                }
                                            }
                                            if (p.city.isNotBlank()) {
                                                Text("📍 ${p.city}", fontSize = 11.sp, color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B))
                                            }
                                        }
                                    }

                                    Text(
                                        text = "${p.count}",
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFD97706),
                                        fontFamily = FontFamily.Serif
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ── FazilatModalSheet ──
@Composable
fun FazilatModalSheet(
    title: String,
    onDismiss: () -> Unit,
    isDarkTheme: Boolean = false
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDarkTheme) Color(0xFF16251E) else Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text("📖 Fazilat & Virtues", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = if (isDarkTheme) Color.White else Color(0xFF063321))

                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFD97706)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (isDarkTheme) Color(0xFF0F1A15) else Color(0xFFF8FAFC),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Reciting Dhikr in congregation brings abundant peace (Sakinah), mercy of Allah, and angels surround the gathering. Every utterance of praise elevates the reciter's rank and erases sins.",
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        color = if (isDarkTheme) Color(0xFFCBD5E1) else Color(0xFF334155)
                    )
                    Text(
                        text = "The Prophet ﷺ said: 'No people sit together remembering Allah without the angels surrounding them and mercy covering them.' (Sahih Muslim)",
                        fontSize = 12.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)
                    )
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E7A52))
                ) {
                    Text("Understood", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

// ── GroupShareModal ──
@Composable
fun GroupShareModal(
    campaign: CampaignItem,
    isCreator: Boolean,
    isDarkTheme: Boolean = false,
    selectedLanguage: String = "en",
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val isPrivate = campaign.visibility.equals("private", ignoreCase = true)
    val joinCode = campaign.joinCode.ifBlank { campaign.campaignId }
    val inviteUrl = GroupInviteHelper.getDeepLinkUrl(joinCode)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDarkTheme) Color(0xFF16251E) else Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Share Dhikr Campaign 🚀", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = if (isDarkTheme) Color.White else Color(0xFF063321))
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = if (isDarkTheme) Color.White else Color.Black)
                    }
                }

                Text(
                    text = "\"${campaign.title}\"",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD97706)
                )

                if (isPrivate) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = if (isDarkTheme) Color(0xFF0F1A15) else Color(0xFFF1F8F5))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("Group Invite Code", fontSize = 11.sp, color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B))
                            Text(
                                text = joinCode,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 3.sp,
                                color = Color(0xFF0E7A52)
                            )
                        }
                    }
                }

                // Share Buttons Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ShareTile(label = "WhatsApp", icon = "💬", color = Color(0xFF25D366)) {
                        GroupInviteHelper.sharePlatform(context, "whatsapp", campaign, isCreator, selectedLanguage)
                    }
                    ShareTile(label = "SMS", icon = "✉️", color = Color(0xFF3B82F6)) {
                        GroupInviteHelper.sharePlatform(context, "sms", campaign, isCreator, selectedLanguage)
                    }
                    ShareTile(label = "Twitter", icon = "🐦", color = Color(0xFF1DA1F2)) {
                        GroupInviteHelper.sharePlatform(context, "twitter", campaign, isCreator, selectedLanguage)
                    }
                    ShareTile(label = "Facebook", icon = "📘", color = Color(0xFF1877F2)) {
                        GroupInviteHelper.sharePlatform(context, "facebook", campaign, isCreator, selectedLanguage)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            GroupInviteHelper.copyToClipboard(context, joinCode, "Join Code copied!")
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = if (isDarkTheme) Color(0xFF1E382B) else Color(0xFFE8F5E9))
                    ) {
                        Text("Copy Code", fontWeight = FontWeight.Bold, color = Color(0xFF0E7A52), fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            GroupInviteHelper.copyToClipboard(context, inviteUrl, "Invite Link copied!")
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E7A52))
                    ) {
                        Text("Copy Link", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun ShareTile(
    label: String,
    icon: String,
    color: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .background(color.copy(alpha = 0.15f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(icon, fontSize = 20.sp)
        }
        Text(label, fontSize = 11.sp, fontWeight = FontWeight.Medium)
    }
}

// ── Authentic Apple, Google Logos & 6-Box OTP Input ──
@Composable
fun AppleLogo(
    modifier: Modifier = Modifier.size(18.dp),
    tint: Color = Color.Unspecified,
    isDark: Boolean = false
) {
    val isNight = isDark || tint == Color.White
    Image(
        painter = painterResource(id = if (isNight) R.drawable.ic_apple_white else R.drawable.ic_apple_black),
        contentDescription = "Apple",
        modifier = modifier
    )
}

@Composable
fun GoogleLogo(modifier: Modifier = Modifier.size(19.dp)) {
    Image(
        painter = painterResource(id = R.drawable.ic_google_logo),
        contentDescription = "Google",
        modifier = modifier
    )
}


@Composable
fun OtpSixBoxField(
    otpValue: String,
    onOtpChange: (String) -> Unit,
    isDarkTheme: Boolean = false
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 0 until 6) {
                val digit = if (i < otpValue.length) otpValue[i].toString() else ""
                val isFocused = i == otpValue.length.coerceAtMost(5)
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isDarkTheme) Color(0xFF16251E) else Color.White)
                        .border(
                            width = if (isFocused) 2.dp else 1.dp,
                            color = if (isFocused) Color(0xFF047857) else Color(0xFFCBD5E1),
                            shape = RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = digit,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White else Color(0xFF064E3B)
                    )
                }
            }
        }

        BasicTextField(
            value = otpValue,
            onValueChange = {
                if (it.length <= 6 && it.all { c -> c.isDigit() }) {
                    onOtpChange(it)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            textStyle = TextStyle(color = Color.Transparent),
            modifier = Modifier.fillMaxSize()
        )
    }
}


// ── UmrahMegaRegisterModal ──
@Composable
fun UmrahMegaRegisterModal(
    campaign: CampaignItem,
    repository: GroupCampaignRepository,
    isDarkTheme: Boolean = false,
    onDismiss: () -> Unit,
    onSuccess: (StoredMegaTicket) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var fullName by remember { mutableStateOf(repository.getUserName()) }
    var phone by remember { mutableStateOf(repository.getVerifiedContact() ?: "") }
    var city by remember { mutableStateOf(repository.getUserCity()) }
    var agreed by remember { mutableStateOf(false) }
    var isRegistering by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    val isVerified = repository.isContactVerified()
    val verifiedContact = repository.getVerifiedContact()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDarkTheme) Color(0xFF13221A) else Color.White
            ),
            modifier = Modifier
                .fillMaxWidth(0.93f)
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(13.dp)
            ) {
                // Top Close button & Header
                Box(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🕋 ✨", fontSize = 28.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Free Umrah Lucky Draw Registration",
                            fontSize = 17.5.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = if (isDarkTheme) Color.White else Color(0xFF064E3B)
                        )
                        Text(
                            text = "Special Mega Campaign Giveaway (18+ Only)",
                            fontSize = 12.sp,
                            color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(if (isDarkTheme) Color(0xFF1E3528) else Color(0xFFE8F5E9))
                            .clickable { onDismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Close",
                            tint = if (isDarkTheme) Color(0xFFA7F3D0) else Color(0xFF064E3B),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // Full Name
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Full Name *",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isDarkTheme) Color(0xFFCBD5E1) else Color(0xFF1E293B)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isDarkTheme) Color(0xFF16251E) else Color.White)
                            .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(12.dp))
                            .padding(horizontal = 14.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (fullName.isEmpty()) {
                            Text("e.g. Mohammad Bilal", color = Color(0xFF94A3B8), fontSize = 13.5.sp)
                        }
                        BasicTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            singleLine = true,
                            textStyle = TextStyle(
                                color = if (isDarkTheme) Color.White else Color(0xFF1E293B),
                                fontSize = 14.sp
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Contact Section: If already verified, display verified card without asking to enter again!
                if (isVerified && !verifiedContact.isNullOrBlank()) {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDarkTheme) Color(0xFF1A2E24) else Color(0xFFF0FDF4)
                        ),
                        border = BorderStroke(1.dp, if (isDarkTheme) Color(0xFF2D5A42) else Color(0xFFA7F3D0)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF059669)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(15.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "Verified Contact",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (isDarkTheme) Color(0xFFA7F3D0) else Color(0xFF065F46)
                                        )
                                        Text(
                                            text = verifiedContact,
                                            fontSize = 13.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isDarkTheme) Color.White else Color(0xFF064E3B)
                                        )
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFD1FAE5))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = "✓ Verified",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF065F46)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "🔒 Safe & Private: Used exclusively to contact you if you win.",
                                fontSize = 10.5.sp,
                                color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)
                            )
                        }
                    }
                } else {
                    // Contact input for users not yet verified
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "WhatsApp / Mobile Number / Email *",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDarkTheme) Color(0xFFCBD5E1) else Color(0xFF1E293B)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isDarkTheme) Color(0xFF16251E) else Color.White)
                                .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(12.dp))
                                .padding(horizontal = 14.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (phone.isEmpty()) {
                                Text("e.g. +91 98765 43210 or email", color = Color(0xFF94A3B8), fontSize = 13.5.sp)
                            }
                            BasicTextField(
                                value = phone,
                                onValueChange = { phone = it },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                textStyle = TextStyle(
                                    color = if (isDarkTheme) Color.White else Color(0xFF1E293B),
                                    fontSize = 14.sp
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "🔒 Safe & Private: Used exclusively to contact you if you win.",
                            fontSize = 10.5.sp,
                            color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)
                        )
                    }
                }

                // City / Location
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "City / Location *",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isDarkTheme) Color(0xFFCBD5E1) else Color(0xFF1E293B)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isDarkTheme) Color(0xFF16251E) else Color.White)
                            .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(12.dp))
                            .padding(horizontal = 14.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (city.isEmpty()) {
                            Text("e.g. Mumbai, Delhi, Lucknow", color = Color(0xFF94A3B8), fontSize = 13.5.sp)
                        }
                        BasicTextField(
                            value = city,
                            onValueChange = { city = it },
                            singleLine = true,
                            textStyle = TextStyle(
                                color = if (isDarkTheme) Color.White else Color(0xFF1E293B),
                                fontSize = 14.sp
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Terms & Conditions Card
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDarkTheme) Color(0xFF232215) else Color(0xFFFFFBEB)
                    ),
                    border = BorderStroke(1.dp, if (isDarkTheme) Color(0xFF524419) else Color(0xFFFED7AA)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "📜 TERMS & RULES (18+ ONLY)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color(0xFFFDE68A) else Color(0xFF92400E)
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFD1FAE5))
                                    .padding(horizontal = 7.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "🔞 18+ Only Event",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF065F46)
                                )
                            }
                        }
                        Text(
                            text = "• Eligibility: Participants must be 18 years of age or older at the time of entry.\n• Daily min 100 recital required. 1 Lucky Winner selected via transparent random draw.",
                            fontSize = 11.5.sp,
                            color = if (isDarkTheme) Color(0xFFCBD5E1) else Color(0xFF475569),
                            lineHeight = 15.sp
                        )
                    }
                }

                // Agreement Checkbox
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { agreed = !agreed }
                ) {
                    Checkbox(
                        checked = agreed,
                        onCheckedChange = { agreed = it },
                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFF047857))
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "I confirm that I am 18 years of age or older, and agree to the Terms & Conditions and Lucky Draw Rules.",
                        fontSize = 11.5.sp,
                        color = if (isDarkTheme) Color(0xFFCBD5E1) else Color(0xFF1E293B),
                        lineHeight = 15.sp
                    )
                }

                errorMsg?.let {
                    Text(it, fontSize = 12.sp, color = Color(0xFFDC2626))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            val contactToUse = if (isVerified && !verifiedContact.isNullOrBlank()) verifiedContact else phone
                            if (fullName.isBlank()) {
                                errorMsg = "Please enter your name"
                                return@Button
                            }
                            if (contactToUse.isBlank()) {
                                errorMsg = "Please enter your contact details"
                                return@Button
                            }
                            if (!agreed) {
                                errorMsg = "Please confirm you are 18+ and accept the participation agreement"
                                return@Button
                            }
                            isRegistering = true
                            errorMsg = null
                            coroutineScope.launch {
                                val res = repository.registerMegaContest(
                                    campaignId = campaign.campaignId,
                                    fullName = fullName,
                                    phone = contactToUse,
                                    city = city
                                )
                                isRegistering = false
                                res.onSuccess { regRes ->
                                    val ticket = StoredMegaTicket(
                                        ticketNumber = regRes.ticketNumber,
                                        fullName = fullName,
                                        phone = contactToUse,
                                        city = city,
                                        timestamp = System.currentTimeMillis(),
                                        campaignId = campaign.campaignId
                                    )
                                    onSuccess(ticket)
                                }.onFailure { err ->
                                    errorMsg = err.message ?: "Registration failed. Please try again."
                                }
                            }
                        },
                        enabled = !isRegistering,
                        modifier = Modifier
                            .weight(1.3f)
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF047857))
                    ) {
                        if (isRegistering) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        } else {
                            Text("Generate Lucky Ticket 🎟️", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                        }
                    }

                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(0.7f)
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Cancel", fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

// ── WelcomeToGroupCampaignsDialog ──
@Composable
fun WelcomeToGroupCampaignsDialog(
    repository: GroupCampaignRepository,
    onDismiss: () -> Unit,
    isDarkTheme: Boolean = false
) {
    val context = LocalContext.current
    var displayName by remember {
        mutableStateOf(
            run {
                val stored = repository.getUserName()
                if (stored.isBlank() || stored.contains("@")) {
                    val fn = repository.getFirstName()
                    if (fn.isNotBlank() && !fn.contains("@")) fn else ""
                } else stored
            }
        )
    }
    var country by remember { mutableStateOf(repository.getCountry().ifBlank { "India" }) }
    var state by remember { mutableStateOf(repository.getState()) }
    var city by remember { mutableStateOf(repository.getCity().ifBlank { repository.getUserCity() }) }

    var showCountryPicker by remember { mutableStateOf(false) }
    var showStatePicker by remember { mutableStateOf(false) }
    var showCityPicker by remember { mutableStateOf(false) }

    var consentChecked by remember { mutableStateOf(repository.isConsentAccepted()) }
    var is18PlusSelected by remember {
        mutableStateOf<Boolean?>(if (repository.isAgeDeclared()) repository.isUser18Plus() else true)
    }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDarkTheme) Color(0xFF13221A) else Color.White
            ),
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight()
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // ── TOP HEADER (Dark Green Banner with 👥 Group Icon) ──
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = if (isDarkTheme) {
                                    listOf(Color(0xFF063327), Color(0xFF03221A))
                                } else {
                                    listOf(Color(0xFF064E3B), Color(0xFF047857))
                                }
                            ),
                            shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp, bottomStart = 24.dp, bottomEnd = 24.dp)
                        )
                        .padding(vertical = 18.dp, horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("👥", fontSize = 32.sp)
                        Text(
                            text = "Welcome to Group Campaigns",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Enter your display name to join and recite",
                            fontSize = 11.5.sp,
                            color = Color(0xFFD1FAE5)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0x33000000))
                            .clickable { onDismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // ── BODY SCROLLABLE ──
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 14.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // ── 18+ Age Declaration Card ──
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDarkTheme) Color(0xFF1B2E24) else Color(0xFFF0FDF4)
                        ),
                        border = BorderStroke(1.dp, if (isDarkTheme) Color(0xFF2D5A42) else Color(0xFFBBF7D0)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("🔞", fontSize = 18.sp)
                                Column {
                                    Text(
                                        text = "Age Declaration (18+ Community)",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDarkTheme) Color.White else Color(0xFF064E3B)
                                    )
                                    Text(
                                        text = "Community group features require you to be 18 or older.",
                                        fontSize = 11.sp,
                                        color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // Option 1: 18 or Older
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            if (is18PlusSelected == true) Color(0xFF047857)
                                            else if (isDarkTheme) Color(0xFF13221A)
                                            else Color.White
                                        )
                                        .border(
                                            1.dp,
                                            if (is18PlusSelected == true) Color(0xFF047857) else Color(0xFFCBD5E1),
                                            RoundedCornerShape(10.dp)
                                        )
                                        .clickable {
                                            is18PlusSelected = true
                                            errorMessage = null
                                        }
                                        .padding(vertical = 8.dp, horizontal = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (is18PlusSelected == true) "✓ 18 or Older" else "18 or Older",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.5.sp,
                                        color = if (is18PlusSelected == true) Color.White else (if (isDarkTheme) Color.White else Color(0xFF1E293B))
                                    )
                                }

                                // Option 2: Under 18
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            if (is18PlusSelected == false) Color(0xFFB91C1C)
                                            else if (isDarkTheme) Color(0xFF13221A)
                                            else Color.White
                                        )
                                        .border(
                                            1.dp,
                                            if (is18PlusSelected == false) Color(0xFFB91C1C) else Color(0xFFCBD5E1),
                                            RoundedCornerShape(10.dp)
                                        )
                                        .clickable {
                                            is18PlusSelected = false
                                            errorMessage = null
                                        }
                                        .padding(vertical = 8.dp, horizontal = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (is18PlusSelected == false) "✓ Under 18" else "Under 18",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.5.sp,
                                        color = if (is18PlusSelected == false) Color.White else (if (isDarkTheme) Color.White else Color(0xFF1E293B))
                                    )
                                }
                            }
                        }
                    }

                    if (is18PlusSelected != false) {
                        // Display Name Field
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Display Name (आपका नाम) *",
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isDarkTheme) Color(0xFFFDE68A) else Color(0xFF854D0E)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isDarkTheme) Color(0xFF16251E) else Color.White)
                                    .border(1.2.dp, Color(0xFF047857), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 14.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (displayName.isEmpty()) {
                                    Text("e.g. Ahmad or Mohd Ali", color = Color(0xFF94A3B8), fontSize = 13.5.sp)
                                }
                                BasicTextField(
                                    value = displayName,
                                    onValueChange = { displayName = it; errorMessage = null },
                                    singleLine = true,
                                    textStyle = TextStyle(
                                        color = if (isDarkTheme) Color.White else Color(0xFF1E293B),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        // Location Section
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Location (City / Country)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isDarkTheme) Color(0xFFFDE68A) else Color(0xFF854D0E)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            // Country Picker
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isDarkTheme) Color(0xFF16251E) else Color.White)
                                    .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(12.dp))
                                    .clickable { showCountryPicker = true }
                                    .padding(horizontal = 12.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text("🌍", fontSize = 15.sp)
                                        Text(
                                            text = country.ifBlank { "Select Country" },
                                            fontSize = 13.sp,
                                            color = if (country.isNotBlank()) (if (isDarkTheme) Color.White else Color(0xFF1E293B)) else Color(0xFF94A3B8)
                                        )
                                    }
                                    Icon(
                                        Icons.Default.KeyboardArrowDown,
                                        contentDescription = null,
                                        tint = Color(0xFF64748B)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // State & City Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(46.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isDarkTheme) Color(0xFF16251E) else Color.White)
                                        .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(12.dp))
                                        .clickable { showStatePicker = true }
                                        .padding(horizontal = 10.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = state.ifBlank { "State" },
                                            fontSize = 12.5.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            color = if (state.isNotBlank()) (if (isDarkTheme) Color.White else Color(0xFF1E293B)) else Color(0xFF94A3B8)
                                        )
                                        Icon(
                                            Icons.Default.KeyboardArrowDown,
                                            contentDescription = null,
                                            tint = Color(0xFF64748B),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(46.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isDarkTheme) Color(0xFF16251E) else Color.White)
                                        .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(12.dp))
                                        .clickable { showCityPicker = true }
                                        .padding(horizontal = 10.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = city.ifBlank { "City" },
                                            fontSize = 12.5.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            color = if (city.isNotBlank()) (if (isDarkTheme) Color.White else Color(0xFF1E293B)) else Color(0xFF94A3B8)
                                        )
                                        Icon(
                                            Icons.Default.KeyboardArrowDown,
                                            contentDescription = null,
                                            tint = Color(0xFF64748B),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // Public Profile Consent Card
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isDarkTheme) Color(0xFF182B21) else Color(0xFFF0FDF4)
                            ),
                            border = BorderStroke(1.dp, if (isDarkTheme) Color(0xFF2D5A42) else Color(0xFFBBF7D0)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { consentChecked = !consentChecked }
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Checkbox(
                                    checked = consentChecked,
                                    onCheckedChange = { consentChecked = it },
                                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF047857))
                                )
                                Column {
                                    Text(
                                        text = "Public Profile Consent",
                                        fontSize = 12.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDarkTheme) Color.White else Color(0xFF064E3B)
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "I understand and agree that my display name and location will be displayed on group leaderboards.",
                                        fontSize = 11.sp,
                                        color = if (isDarkTheme) Color(0xFFCBD5E1) else Color(0xFF475569),
                                        lineHeight = 15.sp
                                    )
                                }
                            }
                        }
                    } // end if (is18PlusSelected != false)

                    errorMessage?.let {
                        Text(
                            text = it,
                            fontSize = 12.sp,
                            color = Color(0xFFDC2626),
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }

                    // ── BOTTOM CONTINUE BUTTON ──
                    if (is18PlusSelected == false) {
                        Button(
                            onClick = {
                                repository.setAge18Plus(false)
                                AppToast.show("Local features open! Community features restricted to 18+.")
                                onDismiss()
                            },
                            shape = RoundedCornerShape(25.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(
                                text = "Continue with Free Local Features",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    } else {
                        Button(
                            onClick = {
                                if (is18PlusSelected == null) {
                                    errorMessage = "Please declare your age above (18+ / Under 18)"
                                    return@Button
                                }
                                if (displayName.isBlank()) {
                                    errorMessage = "Please enter your Display Name"
                                    return@Button
                                }
                                if (!consentChecked) {
                                    errorMessage = "Please accept Public Profile Consent to continue"
                                    return@Button
                                }

                                repository.saveFullProfile(
                                    firstName = displayName.trim(),
                                    lastName = "",
                                    country = country.ifBlank { "India" },
                                    state = state.trim(),
                                    city = city.trim(),
                                    phone = "",
                                    is18Plus = true
                                )
                                repository.recordConsentAccepted()
                                repository.setAge18Plus(true)
                                AppToast.show("Profile saved! Welcome to Group Campaigns.")
                                onDismiss()
                            },
                            shape = RoundedCornerShape(25.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF064E3B)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Continue",
                                    fontSize = 15.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "➔",
                                    fontSize = 15.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Dropdown pickers
    if (showCountryPicker) {
        LocationSelectionDialog(
            title = "Select Country",
            items = GroupLocationsProvider.getCountries(),
            selectedItem = country,
            onSelect = {
                country = it
                state = ""
                city = ""
            },
            onDismiss = { showCountryPicker = false },
            isDarkTheme = isDarkTheme
        )
    }

    if (showStatePicker) {
        val statesList = remember(country) { GroupLocationsProvider.getStates(country) }
        LocationSelectionDialog(
            title = "Select State / Province",
            items = if (statesList.isEmpty()) listOf("Delhi", "Maharashtra", "Uttar Pradesh", "Karnataka", "Telangana", "Gujarat", "Tamil Nadu", "West Bengal", "Bihar", "Punjab", "Kerala", "Rajasthan") else statesList,
            selectedItem = state,
            onSelect = {
                state = it
                city = ""
            },
            onDismiss = { showStatePicker = false },
            isDarkTheme = isDarkTheme
        )
    }

    if (showCityPicker) {
        val citiesList = remember(country, state) { GroupLocationsProvider.getCities(country, state) }
        LocationSelectionDialog(
            title = "Select City",
            items = if (citiesList.isEmpty()) listOf("Mumbai", "Delhi", "Bengaluru", "Hyderabad", "Lucknow", "Kolkata", "Ahmedabad", "Chennai", "Pune", "Jaipur", "Surat", "Kanpur", "Nagpur", "Patna", "Indore", "Bhopal", "Srinagar") else citiesList,
            selectedItem = city,
            onSelect = { city = it },
            onDismiss = { showCityPicker = false },
            isDarkTheme = isDarkTheme
        )
    }
}


// ── ReportGroupModal ──
@Composable
fun ReportGroupModal(
    campaign: CampaignItem,
    onDismiss: () -> Unit,
    onSubmit: (reason: String, notes: String) -> Unit,
    isDarkTheme: Boolean = false
) {
    val reportReasons = listOf(
        "Abusive / Vulgar Language (गाली-गलौज / अशोभनीय)",
        "Misleading / Not Real Dhikr (गलत ज़िक्र या इबादत)",
        "Spam / Commercial Advertising (विज्ञापनों का प्रचार)",
        "Hate Speech or Inappropriate Content",
        "Other Reason (अन्य कारण)"
    )

    var selectedReason by remember { mutableStateOf(reportReasons[0]) }
    var notesText by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }

    val bgCard = if (isDarkTheme) Color(0xFF16251E) else Color.White
    val titleColor = if (isDarkTheme) Color.White else Color(0xFF064E3B)
    val textColor = if (isDarkTheme) Color(0xFFE2E8F0) else Color(0xFF1E293B)
    val subtitleColor = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)
    val reportLogo = rememberAssetBitmap("media/report_alert_badge.webp")

    Dialog(
        onDismissRequest = { if (!isSubmitting) onDismiss() },
        properties = DialogProperties(dismissOnBackPress = !isSubmitting, dismissOnClickOutside = !isSubmitting)
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = bgCard),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (reportLogo != null) {
                            Image(
                                bitmap = reportLogo,
                                contentDescription = "Report",
                                modifier = Modifier.size(34.dp)
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFEE2E2)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🚩", fontSize = 16.sp)
                            }
                        }
                        Column {
                            Text(
                                text = "Report Group",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = titleColor
                            )
                            Text(
                                text = "Help keep Dhikr sacred & safe",
                                fontSize = 11.5.sp,
                                color = subtitleColor
                            )
                        }
                    }

                    IconButton(
                        onClick = { if (!isSubmitting) onDismiss() },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = subtitleColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Group preview badge
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isDarkTheme) Color(0x1AFFFFFF) else Color(0xFFF8FAFC))
                        .border(1.dp, if (isDarkTheme) Color(0x33FFFFFF) else Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Column {
                        Text(
                            text = "Reporting: ${campaign.title}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) Color(0xFF34D399) else Color(0xFF047857),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (campaign.creatorName.isNotBlank()) {
                            Text(
                                text = "Created by: ${campaign.creatorName}",
                                fontSize = 11.sp,
                                color = subtitleColor
                            )
                        }
                    }
                }

                Text(
                    text = "Why are you reporting this group?",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textColor
                )

                // Radio Options List
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    reportReasons.forEach { reason ->
                        val isSelected = selectedReason == reason
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .clickable(enabled = !isSubmitting) { selectedReason = reason }
                                .padding(vertical = 4.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { if (!isSubmitting) selectedReason = reason },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFFDC2626),
                                    unselectedColor = subtitleColor
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = reason,
                                fontSize = 12.5.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) (if (isDarkTheme) Color.White else Color(0xFF991B1B)) else textColor
                            )
                        }
                    }
                }

                // Notes / Explanation Field
                Text(
                    text = "Additional Details (Optional):",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = subtitleColor
                )

                OutlinedTextField(
                    value = notesText,
                    onValueChange = { if (it.length <= 300) notesText = it },
                    placeholder = {
                        Text(
                            text = "Describe what is inappropriate or abusive...",
                            fontSize = 12.sp,
                            color = subtitleColor.copy(alpha = 0.7f)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    textStyle = TextStyle(fontSize = 12.5.sp, color = textColor),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFDC2626),
                        unfocusedBorderColor = if (isDarkTheme) Color(0x33FFFFFF) else Color(0xFFCBD5E1)
                    ),
                    maxLines = 3
                )

                // Action Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        enabled = !isSubmitting,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Cancel", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Button(
                        onClick = {
                            isSubmitting = true
                            onSubmit(selectedReason, notesText.trim())
                        },
                        enabled = !isSubmitting,
                        modifier = Modifier.weight(1.3f),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                if (reportLogo != null) {
                                    Image(
                                        bitmap = reportLogo,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                } else {
                                    Text("🚩", fontSize = 13.sp)
                                }
                                Text(
                                    text = "Submit Report",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ── AajKaWirdModal ──
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AajKaWirdModal(
    repository: GroupCampaignRepository,
    isDarkTheme: Boolean = false,
    onDismiss: () -> Unit,
    onReciteWird: (DailyWirdItem) -> Unit
) {
    var wirdResponse by remember { mutableStateOf<WeeklyWirdResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val todayStr = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    }

    LaunchedEffect(Unit) {
        isLoading = true
        val res = repository.fetchWeeklyWird()
        res.onSuccess { data ->
            wirdResponse = data
            isLoading = false
        }.onFailure { ex ->
            errorMessage = ex.message ?: "Failed to load wird plan"
            isLoading = false
        }
    }

    val days = wirdResponse?.plan?.days ?: emptyList()
    val activeDay = days.find { it.date == todayStr } ?: days.firstOrNull()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = if (isDarkTheme) Color(0xFF0F1E17) else Color(0xFFFBFDFB),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .width(42.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(if (isDarkTheme) Color(0xFF2D5A42) else Color(0xFFCBD5E1))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .padding(bottom = 28.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(Color(0xFF064E3B), Color(0xFF047857)))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📖", fontSize = 19.sp)
                    }
                    Column {
                        Text(
                            text = "आज का विर्द (Daily Guided Wird)",
                            fontSize = 16.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) Color.White else Color(0xFF064E3B)
                        )
                        Text(
                            text = wirdResponse?.plan?.title ?: "Weekly Guided Dhikr Schedule",
                            fontSize = 11.5.sp,
                            color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close",
                        tint = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF047857))
                }
            } else if (activeDay != null) {
                // ── HERO CARD: TODAY'S WIRD ──
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDarkTheme) Color(0xFF132A1F) else Color(0xFFF0FDF4)
                    ),
                    border = BorderStroke(1.5.dp, Color(0xFF10B981)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF064E3B))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (activeDay.date == todayStr) "🌟 TODAY'S WIRD" else "📅 ${activeDay.day}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFDE68A)
                                )
                            }
                            Text(
                                text = "🎯 Target: ${activeDay.target}×",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color(0xFF6EE7B7) else Color(0xFF047857)
                            )
                        }

                        if (activeDay.arabic.isNotBlank()) {
                            Text(
                                text = activeDay.arabic,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color(0xFFFDE68A) else Color(0xFF064E3B),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            )
                        }

                        Text(
                            text = activeDay.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) Color.White else Color(0xFF1E293B)
                        )

                        if (activeDay.meaning.isNotBlank()) {
                            Text(
                                text = "\"${activeDay.meaning}\"",
                                fontSize = 12.sp,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF475569)
                            )
                        }

                        if (activeDay.virtue.isNotBlank()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isDarkTheme) Color(0xFF0B2117) else Color(0xFFDCFCE7))
                                    .padding(horizontal = 10.dp, vertical = 8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.Top,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text("✨", fontSize = 12.sp)
                                    Text(
                                        text = activeDay.virtue,
                                        fontSize = 11.5.sp,
                                        color = if (isDarkTheme) Color(0xFFA7F3D0) else Color(0xFF065F46),
                                        lineHeight = 15.sp
                                    )
                                }
                            }
                        }

                        Button(
                            onClick = { onReciteWird(activeDay) },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF064E3B)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text("▶ Recite Today's Wird", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("›", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFDE68A))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── SECTION 2: THIS WEEK'S SCHEDULE & PAST DAYS ──
                Text(
                    text = "This Week's Schedule (All Days)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) Color(0xFFFDE68A) else Color(0xFF064E3B)
                )
                Text(
                    text = "New user or missed a day? Recite any day's wird anytime!",
                    fontSize = 11.sp,
                    color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    days.forEach { dayItem ->
                        val isToday = dayItem.date == todayStr
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isToday) {
                                    if (isDarkTheme) Color(0xFF1B3D2F) else Color(0xFFECFDF5)
                                } else {
                                    if (isDarkTheme) Color(0xFF16251E) else Color(0xFFF8FAFC)
                                }
                            ),
                            border = BorderStroke(
                                1.dp,
                                if (isToday) Color(0xFF10B981) else (if (isDarkTheme) Color(0xFF233B2E) else Color(0xFFE2E8F0))
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = dayItem.day,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isToday) Color(0xFF047857) else (if (isDarkTheme) Color.White else Color(0xFF1E293B))
                                        )
                                        if (isToday) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(4.dp))
                                                    .background(Color(0xFF047857))
                                                    .padding(horizontal = 6.dp, vertical = 1.dp)
                                            ) {
                                                Text("Today", fontSize = 9.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = dayItem.title,
                                        fontSize = 11.5.sp,
                                        color = if (isDarkTheme) Color(0xFFCBD5E1) else Color(0xFF475569)
                                    )
                                    Text(
                                        text = "Target: ${dayItem.target}×",
                                        fontSize = 10.5.sp,
                                        color = Color(0xFF047857),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                OutlinedButton(
                                    onClick = { onReciteWird(dayItem) },
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.height(34.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                    border = BorderStroke(1.dp, Color(0xFF047857))
                                ) {
                                    Text(
                                        text = "Recite",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDarkTheme) Color(0xFF6EE7B7) else Color(0xFF047857)
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                Text(
                    text = errorMessage ?: "No active wird plan found for this week.",
                    fontSize = 13.sp,
                    color = Color(0xFFDC2626),
                    modifier = Modifier.padding(vertical = 20.dp)
                )
            }
        }
    }
}

// ── WeeklyLeaderboardModal ──
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyLeaderboardModal(
    repository: GroupCampaignRepository,
    isDarkTheme: Boolean = false,
    onDismiss: () -> Unit
) {
    var selectedScope by remember { mutableStateOf("city") }
    var rankings by remember { mutableStateOf<List<LeaderboardUserItem>>(emptyList()) }
    var myRank by remember { mutableStateOf<LeaderboardMyRankResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val userCity = remember { repository.getUserCity().ifBlank { "My City" } }
    val userCountry = remember { repository.getCountry().ifBlank { "India" } }
    val userName = remember { repository.getUserName().ifBlank { "You" } }

    LaunchedEffect(selectedScope) {
        isLoading = true
        val rRes = repository.fetchLeaderboardRankings(scope = selectedScope)
        rRes.onSuccess { data -> rankings = data.rankings }
        val mRes = repository.fetchLeaderboardMyRank(scope = selectedScope)
        mRes.onSuccess { data -> myRank = data }
        isLoading = false
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = if (isDarkTheme) Color(0xFF0B1912) else Color(0xFFF9FCFA),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .width(42.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(if (isDarkTheme) Color(0xFF2D5A42) else Color(0xFFCBD5E1))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .padding(bottom = 24.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(Color(0xFF854D0E), Color(0xFFD97706)))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🏆", fontSize = 20.sp)
                    }
                    Column {
                        Text(
                            text = "Dhikr Leaderboard (साप्ताहिक हल्का)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) Color.White else Color(0xFF064E3B)
                        )
                        Text(
                            text = "Weekly Halqa • Resets on Jummah (Fri 23:59 UTC)",
                            fontSize = 11.sp,
                            color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close",
                        tint = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Scope Selector Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isDarkTheme) Color(0xFF16251E) else Color(0xFFE2E8F0))
                    .padding(3.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf(
                    "city" to userCity,
                    "country" to userCountry,
                    "global" to "Global"
                ).forEach { (scopeKey, label) ->
                    val isSelected = selectedScope == scopeKey
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (isSelected) Color(0xFF064E3B) else Color.Transparent
                            )
                            .clickable {
                                selectedScope = scopeKey
                            }
                            .padding(vertical = 7.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                            color = if (isSelected) Color.White else (if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF475569)),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // User's Current Rank Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDarkTheme) Color(0xFF142B20) else Color(0xFFECFDF5)
                ),
                border = BorderStroke(1.dp, Color(0xFF10B981)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF047857)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("👤", fontSize = 16.sp)
                        }
                        Column {
                            Text(
                                text = userName,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color.White else Color(0xFF064E3B)
                            )
                            Text(
                                text = "Your Rank: ${if ((myRank?.rank ?: 0) > 0) "#${myRank?.rank}" else "Unranked"}",
                                fontSize = 11.5.sp,
                                color = if (isDarkTheme) Color(0xFF6EE7B7) else Color(0xFF047857),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${myRank?.myCount ?: 0}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) Color(0xFFFDE68A) else Color(0xFF854D0E)
                        )
                        Text(
                            text = "this week",
                            fontSize = 10.sp,
                            color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Leaderboard List
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF047857))
                }
            } else if (rankings.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🤲", fontSize = 28.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Be the first reciter in this $selectedScope!",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 380.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(rankings) { userItem ->
                        val medal = when (userItem.rank) {
                            1 -> "🥇"
                            2 -> "🥈"
                            3 -> "🥉"
                            else -> null
                        }
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isDarkTheme) Color(0xFF16251E) else Color.White
                            ),
                            border = BorderStroke(
                                1.dp,
                                if (userItem.rank <= 3) Color(0xFFF59E0B) else (if (isDarkTheme) Color(0xFF233B2E) else Color(0xFFE2E8F0))
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 9.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier.size(28.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (medal != null) {
                                            Text(medal, fontSize = 20.sp)
                                        } else {
                                            Text(
                                                text = "#${userItem.rank}",
                                                fontSize = 12.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)
                                            )
                                        }
                                    }

                                    Column {
                                        Text(
                                            text = userItem.displayName,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isDarkTheme) Color.White else Color(0xFF1E293B),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = if (userItem.city.isNotBlank()) userItem.city.capitalize(Locale.ROOT) else userItem.country.uppercase(),
                                            fontSize = 10.5.sp,
                                            color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isDarkTheme) Color(0xFF1B3D2F) else Color(0xFFDCFCE7))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "${userItem.totalCount}",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDarkTheme) Color(0xFF6EE7B7) else Color(0xFF047857)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
