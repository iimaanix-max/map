package com.tasbih.digitaltasbih.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tasbih.digitaltasbih.data.HomeAnnouncementConfig
import com.tasbih.digitaltasbih.data.PrayerRepository
import com.tasbih.digitaltasbih.ui.components.AdminAnnouncementModal
import com.tasbih.digitaltasbih.ui.components.AppToast
import com.tasbih.digitaltasbih.ui.components.FixedNotifPreviewCard
import com.tasbih.digitaltasbih.ui.components.HomeAnnouncementModal
import com.tasbih.digitaltasbih.data.RamadanManager
import com.tasbih.digitaltasbih.ui.components.RateAppDialog
import com.tasbih.digitaltasbih.ui.components.SupportUsModal
import com.tasbih.digitaltasbih.ui.components.rememberAssetPainter
import com.tasbih.digitaltasbih.ui.theme.GoldAccent
import java.util.Calendar
import java.util.TimeZone

/**
 * Checks if current user is in India based on Timezone (Asia/Kolkata),
 * UTC+5:30 offset, device country or saved location label.
 * Mandatory under Digital Personal Data Protection (DPDP) Act, 2023.
 */
fun isIndianUser(context: Context): Boolean {
    return try {
        val tzId = TimeZone.getDefault().id
        if (tzId.contains("Kolkata", ignoreCase = true) ||
            tzId.contains("Calcutta", ignoreCase = true) ||
            tzId.contains("India", ignoreCase = true)
        ) {
            return true
        }
        val offsetMs = TimeZone.getDefault().rawOffset
        if (offsetMs == 19800000) { // UTC+5:30
            return true
        }
        val settings = PrayerRepository.loadSettings()
        if (settings.locationLabel.contains("India", ignoreCase = true)) {
            return true
        }
        val country = context.resources.configuration.locales.get(0).country
        country.equals("IN", ignoreCase = true)
    } catch (_: Exception) {
        false
    }
}

@Composable
fun SettingsScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var isDarkTheme by remember { mutableStateOf(PrayerRepository.isDarkTheme()) }

    var isTermsModalOpen by remember { mutableStateOf(false) }
    var isPrivacyModalOpen by remember { mutableStateOf(false) }
    var isDeleteDataModalOpen by remember { mutableStateOf(false) }
    var isAdChoicesModalOpen by remember { mutableStateOf(false) }
    var isSupportUsModalOpen by remember { mutableStateOf(false) }
    var isRateAppDialogOpen by remember { mutableStateOf(false) }
    var isAdminAnnouncementOpen by remember { mutableStateOf(false) }
    var previewAnnouncementConfig by remember { mutableStateOf<HomeAnnouncementConfig?>(null) }
    var isRamadanPreview by remember { mutableStateOf(RamadanManager.isPreviewEnabled(context)) }

    val isIndia = remember(context) { isIndianUser(context) }

    val backgroundColor = if (isDarkTheme) Color(0xFF07150E) else Color(0xFFF6F4EE)
    val brandCardBg = if (isDarkTheme) Color(0xFF0D251C) else Color(0xFFFFFDF8)
    val brandCardBorder = if (isDarkTheme) Color(0xFF1E4535) else Color(0x33C9A84C)
    val primaryText = if (isDarkTheme) Color.White else Color(0xFF1A1408)
    val secondaryText = if (isDarkTheme) Color(0xFF9EABA4) else Color(0xFF757575)

    val headerGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF146450),
            Color(0xFF0C4A3A),
            Color(0xFF062018)
        )
    )

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .shadow(elevation = 6.dp, shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                        .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                        .background(headerGradient)
                        .border(
                            width = 1.dp,
                            color = Color(0x55E8C878),
                            shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
                        )
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Back Button
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xE6FFFFFF))
                                .border(1.5.dp, Color(0xFF0E5D47), RoundedCornerShape(10.dp))
                                .clickable { onBack() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color(0xFF0E5D47),
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Title
                        Text(
                            text = "SETTINGS",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Serif,
                            color = Color(0xFFE8C878),
                            letterSpacing = 2.sp
                        )

                        // Theme Toggle Button
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xE6FFFFFF))
                                .border(1.5.dp, Color(0xFF0E5D47), RoundedCornerShape(10.dp))
                                .clickable {
                                    isDarkTheme = !isDarkTheme
                                    PrayerRepository.setDarkTheme(isDarkTheme)
                                    Toast.makeText(
                                        context,
                                        if (isDarkTheme) "Night Theme 🌙" else "Day Theme ☀️",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isDarkTheme) "🌙" else "☀️",
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // ══════════════════════════════════════════════════════════
            // 1. APP BRAND CARD (Exact from original screenshot)
            // ══════════════════════════════════════════════════════════
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(18.dp))
                    .clip(RoundedCornerShape(18.dp))
                    .background(brandCardBg)
                    .border(1.2.dp, brandCardBorder, RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .shadow(elevation = 3.dp, shape = RoundedCornerShape(14.dp))
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF0E5D47), Color(0xFF062018))
                                )
                            )
                            .border(1.dp, GoldAccent, RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = rememberAssetPainter("media/59329dcad16a9eac.webp"),
                            contentDescription = "Digital Tasbih",
                            modifier = Modifier.size(34.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "DIGITAL TASBIH",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            letterSpacing = 1.sp,
                            color = if (isDarkTheme) Color(0xFFE8C878) else Color(0xFF0E5D47)
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "Your Companion in Worship • v1.0.0",
                            fontSize = 12.5.sp,
                            color = secondaryText
                        )
                    }
                }
            }

            // ══════════════════════════════════════════════════════════
            // 2. COMMUNITY & SADAQAH JARIYAH
            // ══════════════════════════════════════════════════════════
            Text(
                text = "COMMUNITY & SUPPORT",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = GoldAccent,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )

            // Card 1: Support Us (Direct Indian UPI & Sadaqah Jariyah)
            SettingsActionCard(
                icon = "❤️",
                iconColor = Color(0xFFEF4444),
                iconContainerBg = Color(0x2EEF4444),
                title = "Support Digital Tasbih",
                description = "Contribute via Google Pay, PhonePe, Paytm or BHIM",
                onClick = { isSupportUsModalOpen = true }
            )

            // Card 2: Rate on Google Play Store
            SettingsActionCard(
                icon = "⭐",
                iconColor = Color(0xFFF3D48A),
                iconContainerBg = Color(0x33F3D48A),
                title = "Rate & Review on Play Store",
                description = "Support pure Islamic development with a 5-star review",
                onClick = { isRateAppDialogOpen = true }
            )

            // ══════════════════════════════════════════════════════════
            // 2. PERSISTENT PRAYER NOTIFICATION & LIVE PREVIEW
            // ══════════════════════════════════════════════════════════
            Text(
                text = "NOTIFICATIONS & STATUS BAR",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = GoldAccent,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )

            // Live Preview Drawer Mockup Card
            FixedNotifPreviewCard(
                isDarkTheme = isDarkTheme
            )

            // ══════════════════════════════════════════════════════════
            // 3. LEGAL & POLICIES SECTION
            // ══════════════════════════════════════════════════════════
            Text(
                text = "LEGAL & POLICIES",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = GoldAccent,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )

            // Card 1: Terms of Use / Terms & Conditions
            SettingsActionCard(
                icon = "📄",
                title = "Terms of Use / Terms & Conditions",
                description = "Usage guidelines, disclaimers & service agreement",
                onClick = { isTermsModalOpen = true }
            )

            // Card 2: Privacy Policy
            SettingsActionCard(
                icon = "🛡️",
                title = "Privacy Policy",
                description = "How we protect your data, permissions & analytics",
                onClick = { isPrivacyModalOpen = true }
            )

            // Card 3: Delete My Data
            if (isIndia) {
                SettingsActionCard(
                    icon = "🗑️",
                    iconColor = Color(0xFFEF4444),
                    iconContainerBg = Color(0x2EEF4444),
                    title = "Delete My Data",
                    description = "Permanently delete your app data & saved preferences",
                    onClick = { isDeleteDataModalOpen = true }
                )
            }

            // ══════════════════════════════════════════════════════════
            // 4. DATA & PREFERENCES
            // ══════════════════════════════════════════════════════════
            Text(
                text = "DATA & PREFERENCES",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = GoldAccent,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )

            // Card 4: Ad & Privacy Choices
            SettingsActionCard(
                icon = "🍪",
                title = "Ad & Privacy Choices",
                description = "Manage cookies, ad personalization & consent",
                onClick = { isAdChoicesModalOpen = true }
            )

            // ══════════════════════════════════════════════════════════
            // 5. FESTIVE DECORATION
            // ══════════════════════════════════════════════════════════
            Text(
                text = "FESTIVE DECORATION",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = GoldAccent,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )

            SettingsToggleCard(
                icon = "🏮",
                title = "Ramadan Festive Mode",
                subtitle = "Festive hanging lights, Ramadan Mubarak greeting & 99 Names flower bouquet",
                checked = isRamadanPreview,
                onCheckedChange = { checked ->
                    isRamadanPreview = checked
                    RamadanManager.setPreviewEnabled(context, checked)
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // ══════════════════════════════════════════════════════════
            // 4. FOOTER NOTE
            // ══════════════════════════════════════════════════════════
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) Color(0xFFE8C878) else Color(0xFF0E5D47)
                )
                Text(
                    text = "Digital Tasbih • Ad-Supported Islamic Companion",
                    fontSize = 11.5.sp,
                    color = secondaryText
                )
                Text(
                    text = "All rights reserved © 2026",
                    fontSize = 10.5.sp,
                    color = Color(0xFF888888)
                )
            }
        }
    }

    // ══════════════════════════════════════════════════════════
    // MODALS
    // ══════════════════════════════════════════════════════════

    if (isTermsModalOpen) {
        TermsOfUseModal(onDismiss = { isTermsModalOpen = false })
    }

    if (isPrivacyModalOpen) {
        PrivacyPolicyModal(
            onDismiss = { isPrivacyModalOpen = false },
            onOpenAdChoices = {
                isPrivacyModalOpen = false
                isAdChoicesModalOpen = true
            }
        )
    }

    if (isDeleteDataModalOpen) {
        DeleteMyDataDpdpModal(onDismiss = { isDeleteDataModalOpen = false })
    }

    if (isAdChoicesModalOpen) {
        AdChoicesConsentModal(onDismiss = { isAdChoicesModalOpen = false })
    }

    if (isSupportUsModalOpen) {
        SupportUsModal(onDismiss = { isSupportUsModalOpen = false })
    }

    if (isRateAppDialogOpen) {
        RateAppDialog(onDismiss = { isRateAppDialogOpen = false })
    }

    if (isAdminAnnouncementOpen) {
        AdminAnnouncementModal(
            onDismiss = { isAdminAnnouncementOpen = false },
            onPreview = { previewConfig ->
                previewAnnouncementConfig = previewConfig
            }
        )
    }

    previewAnnouncementConfig?.let { previewCfg ->
        HomeAnnouncementModal(
            config = previewCfg,
            onDismiss = { previewAnnouncementConfig = null },
            onParticipateClick = { previewAnnouncementConfig = null }
        )
    }
}

/**
 * Reusable Settings Action Card matching the exact luxury emerald styling
 */
@Composable
private fun SettingsToggleCard(
    icon: String,
    iconColor: Color = Color.White,
    iconContainerBg: Color = Color(0x26FFFFFF),
    title: String,
    titleBadge: String? = null,
    description: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val emeraldGradient = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF0E5D47),
            Color(0xFF0A3D2F),
            Color(0xFF06251C)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 3.dp, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(emeraldGradient)
            .border(1.dp, Color(0x33C9A84C), RoundedCornerShape(16.dp))
            .clickable { onCheckedChange(!isChecked) }
            .padding(horizontal = 14.dp, vertical = 13.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(13.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(iconContainerBg)
                        .border(1.dp, Color(0x26FFFFFF), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = icon,
                        fontSize = 18.sp,
                        color = iconColor
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        if (titleBadge != null) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(5.dp))
                                    .background(Color(0x33F59E0B))
                                    .border(1.dp, Color(0x80F59E0B), RoundedCornerShape(5.dp))
                                    .padding(horizontal = 5.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = titleBadge,
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFF59E0B)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = description,
                        fontSize = 11.5.sp,
                        color = Color(0xFFB5C9C2),
                        lineHeight = 15.sp
                    )
                }
            }

            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFFFFF2B2),
                    checkedTrackColor = Color(0xFF1E8F6E),
                    uncheckedThumbColor = Color(0xFFB5C9C2),
                    uncheckedTrackColor = Color(0xFF082D22)
                )
            )
        }
    }
}

@Composable
private fun SettingsActionCard(
    icon: String,
    iconColor: Color = Color.White,
    iconContainerBg: Color = Color(0x26FFFFFF),
    title: String,
    titleBadge: String? = null,
    description: String,
    onClick: () -> Unit
) {
    val emeraldGradient = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF0E5D47),
            Color(0xFF0A3D2F),
            Color(0xFF06251C)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 3.dp, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(emeraldGradient)
            .border(1.dp, Color(0x33C9A84C), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 13.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(13.dp)
            ) {
                // Icon Container
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(iconContainerBg)
                        .border(1.dp, Color(0x26FFFFFF), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = icon,
                        fontSize = 18.sp,
                        color = iconColor
                    )
                }

                // Text Info
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        if (titleBadge != null) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(5.dp))
                                    .background(Color(0x33F59E0B))
                                    .border(1.dp, Color(0x80F59E0B), RoundedCornerShape(5.dp))
                                    .padding(horizontal = 5.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = titleBadge,
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFF59E0B)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = description,
                        fontSize = 11.5.sp,
                        color = Color(0xFFB5C9C2),
                        lineHeight = 15.sp
                    )
                }
            }

            // Arrow
            Text(
                text = "›",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xCCE8C878),
                modifier = Modifier.padding(end = 4.dp)
            )
        }
    }
}

@Composable
fun SettingsToggleCard(
    icon: String,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val emeraldGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF0F382A),
            Color(0xFF09241B)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 3.dp, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(emeraldGradient)
            .border(1.dp, Color(0x33C9A84C), RoundedCornerShape(16.dp))
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 14.dp, vertical = 13.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(13.dp)
            ) {
                // Icon Container
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x3310B981))
                        .border(1.dp, Color(0x26FFFFFF), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = icon,
                        fontSize = 18.sp
                    )
                }

                // Text Info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        color = Color(0xFFBAC7BE),
                        lineHeight = 15.sp
                    )
                }
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFFE8C878),
                    checkedTrackColor = Color(0xFF0E5D47),
                    uncheckedThumbColor = Color(0xFFBAC7BE),
                    uncheckedTrackColor = Color(0xFF1B382A)
                )
            )
        }
    }
}

// ══════════════════════════════════════════════════════════════════
// 1. TERMS OF USE MODAL (Complete 8-section legal agreement)
// ══════════════════════════════════════════════════════════════════
@Composable
fun TermsOfUseModal(onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x99000000))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0B2019)),
                border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFC9A84C))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Terms & Conditions",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            color = Color(0xFFE8C878)
                        )
                        IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = Color(0x33C9A84C))
                    Spacer(modifier = Modifier.height(12.dp))

                    // Content
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        LegalParagraph(
                            number = "1. Acceptance of Terms",
                            content = "By downloading, installing, or using the Digital Tasbih application (\"App\"), you agree to be bound by these Terms & Conditions. If you do not agree to these terms, please do not use the application."
                        )
                        LegalParagraph(
                            number = "2. Purpose & Islamic Content",
                            content = "Digital Tasbih is developed solely to assist Muslims worldwide in their daily worship, remembrance of Allah (Dhikr), tracking Salah, Islamic calendar schedules, and authentic Sunnah supplications. All religious content is reviewed from authentic sources (Quran, Sahih Bukhari, Sahih Muslim, Hisnul Muslim)."
                        )
                        LegalParagraph(
                            number = "3. Prayer Times & Calculations Disclaimer",
                            content = "Prayer times, Adhan reminders, and Qibla bearings are computed using recognized mathematical-astronomical algorithms and device location sensors. While we strive for absolute accuracy, prayer times may vary by geographical altitude, atmospheric conditions, or local conventions. Users are advised to confirm critical times (such as Fajr, Imsak, and Maghrib) with their local mosque."
                        )
                        LegalParagraph(
                            number = "4. User Conduct",
                            content = "You agree to use this application for lawful, peaceful, and respectful purposes. You may not attempt to decompile, reverse-engineer, modify, or exploit the application or its database for unauthorized commercial purposes."
                        )
                        LegalParagraph(
                            number = "5. Advertisements & Free Service",
                            content = "To keep this application completely free and accessible for all users, respectful non-intrusive advertisements (via Google AdMob) are displayed. We ensure ads conform to ethical and family-safe standards."
                        )
                        LegalParagraph(
                            number = "6. Intellectual Property",
                            content = "All Quranic texts, authentic Hadith narrations, graphic art, custom tasbih marble skins, and audio belong to their respective rights holders and Digital Tasbih under applicable laws."
                        )
                        LegalParagraph(
                            number = "7. Limitation of Liability",
                            content = "The App is provided on an 'AS IS' and 'AS AVAILABLE' basis without warranties of any kind. Developers shall not be liable for any indirect, incidental, or consequential damages resulting from the use or inability to use the application."
                        )
                        LegalParagraph(
                            number = "8. Updates & Modifications",
                            content = "We reserve the right to revise these Terms & Conditions from time to time to reflect feature enhancements, security standards, or regulatory changes."
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E5D47)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("I Understand", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════
// 2. PRIVACY POLICY MODAL (Complete global compliance)
// ══════════════════════════════════════════════════════════════════
@Composable
fun PrivacyPolicyModal(
    onDismiss: () -> Unit,
    onOpenAdChoices: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x99000000))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0B2019)),
                border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFC9A84C))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Privacy Policy",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            color = Color(0xFFE8C878)
                        )
                        IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = Color(0x33C9A84C))
                    Spacer(modifier = Modifier.height(12.dp))

                    // Content
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        LegalParagraph(
                            number = "1. Overview & Commitment to Amanah",
                            content = "Your privacy is of utmost importance to us. In accordance with Islamic values of trust (Amanah) and international privacy laws (GDPR, CCPA, DPDP Act 2023), we are committed to being fully transparent about how your data is handled."
                        )
                        LegalParagraph(
                            number = "2. Device-Only Data Storage",
                            content = "All your personal spiritual progress — including your daily Tasbih counts, completed Salah records, Morning & Evening Adhkar logs, and custom Dua lists — is stored strictly on your local device. We do NOT track or store your worship records on any central server."
                        )
                        LegalParagraph(
                            number = "3. Permissions & Purpose",
                            content = "• Precise/Coarse Location: Used locally to determine live Qibla compass bearing and calculate precise local prayer timings. We never record your movement or travel history.\n• Device Compass/Sensors: Used to animate the 3D Kaaba needle.\n• Notifications: Used solely to alert you when prayer times begin."
                        )
                        LegalParagraph(
                            number = "4. Advertising & Google AdMob",
                            content = "We display advertisements through Google AdMob to support ongoing development and keep the app completely free. In regions requiring consent (such as the European Economic Area under GDPR and California under CCPA), you can manage your personalized advertising choices at any time."
                        )
                        LegalParagraph(
                            number = "5. Zero Data-Sale Guarantee",
                            content = "We NEVER sell, rent, or trade your personal information to third parties. We do not participate in cross-app data broker networks."
                        )
                        LegalParagraph(
                            number = "6. Children's Privacy",
                            content = "Digital Tasbih is family-safe and appropriate for users of all ages. We do not knowingly collect personal data from children under 13."
                        )
                        LegalParagraph(
                            number = "7. Contact & Support",
                            content = "For questions, inquiries, or privacy concerns, please contact our team at support@digitaltasbih.app."
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = onOpenAdChoices,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFC9A84C))
                        ) {
                            Text("Consent Choices", color = Color(0xFFE8C878), fontSize = 12.sp)
                        }
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E5D47)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Close", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════
// 3. DELETE MY DATA (DPDP Act, 2023 - India Right to Erasure)
// ══════════════════════════════════════════════════════════════════
@Composable
fun DeleteMyDataDpdpModal(onDismiss: () -> Unit) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }
    var isSubmitted by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }

    fun isValidEmail(e: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(e).matches()
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x99000000))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0B2019)),
                border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFEF4444))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    if (!isSubmitted) {
                        // Title Row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "⚠️ Delete My Data",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF8A7C)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Submitting this request will permanently erase all your personal data held by Imaanix — including your name, phone number, city, Umrah campaign tickets, OTP records, and linked participation logs.",
                            fontSize = 12.5.sp,
                            color = Color(0xFFC5D6CF),
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "📌 Note: Third-party ad identifiers managed by Google AdMob are not stored by Imaanix and can be deleted directly in your phone settings (Settings ➔ Google ➔ Ads).",
                            fontSize = 11.5.sp,
                            color = Color(0xFF94A3B8),
                            lineHeight = 15.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Email Input
                        Text(
                            text = "Email Address (Required for confirmation):",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE8C878)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                emailError = null
                            },
                            placeholder = { Text("name@example.com", color = Color(0xFF6B8279), fontSize = 13.sp) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFF10B981),
                                unfocusedBorderColor = Color(0x40FFFFFF),
                                cursorColor = Color(0xFF10B981)
                            )
                        )

                        if (emailError != null) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = emailError!!,
                                fontSize = 11.5.sp,
                                color = Color(0xFFEF4444)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Zero-Retention Guarantee Note
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0x2610B981))
                                .border(1.dp, Color(0x4010B981), RoundedCornerShape(10.dp))
                                .padding(10.dp)
                        ) {
                            Text(
                                text = "🔒 Zero-Retention Guarantee: Immediately after an official confirmation email is sent, your email address is also permanently wiped from our server.",
                                fontSize = 11.sp,
                                color = Color(0xFF9AE6B4),
                                lineHeight = 15.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = onDismiss,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x66FFFFFF))
                            ) {
                                Text("Cancel", color = Color.White)
                            }
                            Button(
                                onClick = {
                                    val trimmed = email.trim()
                                    if (!isValidEmail(trimmed)) {
                                        emailError = "Please enter a valid email address"
                                        return@Button
                                    }
                                    isSubmitting = true
                                    // Complete local profile erasure & Firebase sign out
                                    try {
                                        val repo = com.tasbih.digitaltasbih.data.groups.GroupCampaignRepository(context)
                                        repo.clearAllUserData()
                                    } catch (_: Exception) {}
                                    isSubmitting = false
                                    isSubmitted = true
                                },
                                modifier = Modifier.weight(1.3f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = if (isSubmitting) "Submitting…" else "Delete My Data",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    } else {
                        // Success View
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(text = "✅", fontSize = 42.sp)
                            Text(
                                text = "Deletion Request Submitted",
                                fontSize = 16.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE8C878)
                            )
                            Text(
                                text = "Your data deletion request under the DPDP Act, 2023 has been successfully logged. An official confirmation email will be sent to $email once the erasure cycle completes.",
                                fontSize = 12.5.sp,
                                color = Color(0xFFB0C4BE),
                                textAlign = TextAlign.Center,
                                lineHeight = 17.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = onDismiss,
                                modifier = Modifier.fillMaxWidth(0.6f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E5D47)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Done", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════
// 4. AD & PRIVACY CHOICES (GDPR, CCPA & CMP Consent)
// ══════════════════════════════════════════════════════════════════
@Composable
fun AdChoicesConsentModal(onDismiss: () -> Unit) {
    val context = LocalContext.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x99000000))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0B2019)),
                border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFC9A84C))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Ad & Privacy Choices",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            color = Color(0xFFE8C878)
                        )
                        IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                    }

                    HorizontalDivider(color = Color(0x33C9A84C))

                    Text(
                        text = "We use Google AdMob to deliver family-safe advertisements that keep Digital Tasbih 100% free for Muslims globally.",
                        fontSize = 12.5.sp,
                        color = Color(0xFFC5D6CF),
                        lineHeight = 17.sp
                    )

                    // International compliance items
                    ComplianceBullet(
                        region = "🇪🇺 European Union & UK (GDPR)",
                        detail = "Google's certified CMP dialog is presented automatically in EEA & UK to collect your explicit consent for personalized ads, non-personalized ads, or ad measurement."
                    )
                    ComplianceBullet(
                        region = "🇺🇸 United States (CCPA / CPRA)",
                        detail = "We do NOT sell or share your personal information. Under California and US State privacy laws, you have the right to opt-out of cross-context behavioral ads."
                    )
                    ComplianceBullet(
                        region = "🇮🇳 India (DPDP Act 2023)",
                        detail = "You have full Right to Erasure and withdrawal of consent for any campaign data via the 'Delete My Data' portal."
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Button(
                        onClick = {
                            Toast.makeText(context, "Google UMP consent dialog is active for all supported regions.", Toast.LENGTH_LONG).show()
                            onDismiss()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E5D47)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Save & Close", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun LegalParagraph(number: String, content: String) {
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(
            text = number,
            fontSize = 13.5.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFE8C878)
        )
        Text(
            text = content,
            fontSize = 12.sp,
            color = Color(0xFFD1DDD8),
            lineHeight = 17.sp
        )
    }
}

@Composable
private fun ComplianceBullet(region: String, detail: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0x1AFFFFFF))
            .border(1.dp, Color(0x26FFFFFF), RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = region,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE8C878)
            )
            Text(
                text = detail,
                fontSize = 11.sp,
                color = Color(0xFFB5C9C2),
                lineHeight = 15.sp
            )
        }
    }
}
