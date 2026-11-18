package com.tasbih.digitaltasbih.ui.screens.groups

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.data.PrayerRepository
import com.tasbih.digitaltasbih.data.groups.GroupCampaignRepository
import com.tasbih.digitaltasbih.data.groups.LiveDhikrTourLocalization

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupZikrScreen(
    repository: GroupCampaignRepository,
    selectedLanguage: String = "en",
    onDismiss: () -> Unit,
    onCreate: (title: String, arabic: String, target: Long, isPrivate: Boolean, city: String, isSpecialMega: Boolean, note: String) -> Unit,
    onOpenAdmin: () -> Unit = {},
    isDarkTheme: Boolean = false,
    onToggleTheme: () -> Unit = {}
) {
    val tourStrings = remember(selectedLanguage) { LiveDhikrTourLocalization.get(selectedLanguage) }
    var isCreateTourOpen by remember { mutableStateOf(!repository.isCreateTourDone()) }
    var createTourStep by remember { mutableIntStateOf(1) }
    val scrollState = rememberScrollState()

    var createRootCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var chooseZikrCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var targetCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var privacyCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var createBtnCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }

    var title by remember { mutableStateOf("SubhanAllah") }
    var arabic by remember { mutableStateOf("سُبْحَانَ اللّٰہ") }
    var translit by remember { mutableStateOf("SubhanAllah") }
    var meaning by remember { mutableStateOf("Glory be to Allah") }
    var targetStr by remember { mutableStateOf("1000") }
    var note by remember { mutableStateOf("") }
    var isPrivate by remember { mutableStateOf(false) }
    var showPicker by remember { mutableStateOf(false) }
    var selectedLang by remember { mutableStateOf(selectedLanguage) }

    val currentTourRect = remember(
        createRootCoords, chooseZikrCoords, targetCoords, privacyCoords, createBtnCoords,
        createTourStep, scrollState.value
    ) {
        val root = createRootCoords
        if (root != null && root.isAttached) {
            val target = when (createTourStep) {
                1 -> chooseZikrCoords
                2 -> targetCoords
                3 -> privacyCoords
                4 -> createBtnCoords
                else -> null
            }
            if (target != null && target.isAttached) {
                val box = target.boundsInRoot()
                val rootBox = root.boundsInRoot()
                Rect(
                    left = box.left - rootBox.left,
                    top = box.top - rootBox.top,
                    right = box.right - rootBox.left,
                    bottom = box.bottom - rootBox.top
                )
            } else null
        } else null
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDarkTheme) Color(0xFF0C1913) else Color(0xFFF8FAF7))
            .onGloballyPositioned { createRootCoords = it }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // TopBar Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (isDarkTheme) Color(0xFF16251E) else Color.White)
                        .border(1.dp, if (isDarkTheme) Color(0x33C5A059) else Color(0x22000000), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = if (isDarkTheme) Color.White else Color(0xFF063321)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Create Group Zikr",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White else Color(0xFF063321)
                    )
                    Text(
                        text = "Choose a Zikr and set a shared target",
                        fontSize = 11.5.sp,
                        color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)
                    )
                }

                IconButton(
                    onClick = onToggleTheme,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (isDarkTheme) Color(0xFF16251E) else Color.White)
                        .border(1.dp, if (isDarkTheme) Color(0x33C5A059) else Color(0x22000000), CircleShape)
                ) {
                    Text(if (isDarkTheme) "☀️" else "🌙", fontSize = 16.sp)
                }
            }

            // Language Row
            val languages = listOf(
                "en" to "English",
                "hi" to "हिंदी",
                "ur" to "اردو",
                "ar" to "العربية",
                "bn" to "বাংলা",
                "gu" to "ગુજરાતી"
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(languages) { (code, label) ->
                    val isSel = selectedLang == code
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSel) Color(0xFF0E7A52) else (if (isDarkTheme) Color(0xFF16251E) else Color.White))
                            .border(1.dp, if (isSel) Color(0xFF0E7A52) else Color(0x22000000), RoundedCornerShape(16.dp))
                            .clickable {
                                selectedLang = code
                                PrayerRepository.saveLanguage(code)
                            }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSel) Color.White else (if (isDarkTheme) Color(0xFFE2E8F0) else Color(0xFF334155))
                        )
                    }
                }
            }

            // CARD 1: Choose Zikr
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { chooseZikrCoords = it },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDarkTheme) Color(0xFF16251E) else Color.White
                ),
                border = BorderStroke(1.dp, if (isDarkTheme) Color(0x33C5A059) else Color(0x1A0E7A52))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("📿", fontSize = 18.sp)
                        Column {
                            Text(
                                text = "Choose Zikr",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color.White else Color(0xFF063321)
                            )
                            Text(
                                text = "Select language, category and Zikr, or write custom",
                                fontSize = 11.5.sp,
                                color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)
                            )
                        }
                    }

                    // Choose Zikr Button
                    Card(
                        onClick = { showPicker = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDarkTheme) Color(0xFF0F1D17) else Color(0xFFF1F8F5)
                        ),
                        border = BorderStroke(1.dp, Color(0xFF0E7A52).copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Select Dhikr",
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0E7A52)
                                )
                                Text(
                                    text = "Tap to choose from 100+ Dhikrs & 99 Names ›",
                                    fontSize = 11.sp,
                                    color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowRight,
                                contentDescription = null,
                                tint = Color(0xFF0E7A52)
                            )
                        }
                    }

                    // Dhikr Name TextField
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Dhikr Name (Or Write Custom)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true
                    )

                    // Display Preview Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isDarkTheme) Color(0xFF0B1611) else Color(0xFFF8FBF8))
                            .border(1.dp, if (isDarkTheme) Color(0x22C5A059) else Color(0x1A0E7A52), RoundedCornerShape(14.dp))
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            if (arabic.isNotBlank()) {
                                Text(
                                    text = arabic,
                                    fontSize = 22.sp,
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFD97706),
                                    textAlign = TextAlign.Center
                                )
                            }
                            Text(
                                text = translit.ifBlank { title },
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isDarkTheme) Color.White else Color(0xFF063321),
                                textAlign = TextAlign.Center
                            )
                            if (meaning.isNotBlank()) {
                                Text(
                                    text = meaning,
                                    fontSize = 12.sp,
                                    color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            // CARD 2: Group Details
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { targetCoords = it },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDarkTheme) Color(0xFF16251E) else Color.White
                ),
                border = BorderStroke(1.dp, if (isDarkTheme) Color(0x33C5A059) else Color(0x1A0E7A52))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("🎯", fontSize = 18.sp)
                        Column {
                            Text(
                                text = "Group Details",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color.White else Color(0xFF063321)
                            )
                            Text(
                                text = "Set the target and group information",
                                fontSize = 11.5.sp,
                                color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)
                            )
                        }
                    }

                    // Target Count
                    Text(
                        text = "Target Count",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isDarkTheme) Color(0xFFE2E8F0) else Color(0xFF1E293B)
                    )

                    // Preset Chips
                    val presets = listOf(500L to "+500", 1000L to "1,000", 5000L to "5,000", 10000L to "10,000", 50000L to "50,000")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        presets.forEach { (targetVal, label) ->
                            val isSel = targetStr == targetVal.toString()
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSel) Color(0xFF0E7A52) else (if (isDarkTheme) Color(0xFF0F1D17) else Color(0xFFF1F5F9)))
                                    .border(1.dp, if (isSel) Color(0xFF0E7A52) else Color(0x22000000), RoundedCornerShape(10.dp))
                                    .clickable {
                                        if (label == "+500") {
                                            val cur = targetStr.toLongOrNull() ?: 0L
                                            targetStr = (cur + 500L).toString()
                                        } else {
                                            targetStr = targetVal.toString()
                                        }
                                    }
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 11.5.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSel) Color.White else (if (isDarkTheme) Color(0xFFE2E8F0) else Color(0xFF334155))
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = targetStr,
                        onValueChange = { targetStr = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Custom Target Number") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        label = { Text("Note / Intention (Optional)") },
                        placeholder = { Text("e.g. For peace, health, or family...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        maxLines = 3
                    )
                }
            }

            // CARD 3: Visibility
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { privacyCoords = it },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDarkTheme) Color(0xFF16251E) else Color.White
                ),
                border = BorderStroke(1.dp, if (isDarkTheme) Color(0x33C5A059) else Color(0x1A0E7A52))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Visibility",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White else Color(0xFF063321)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Public Group
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (!isPrivate) Color(0xFF0E7A52) else (if (isDarkTheme) Color(0xFF0F1D17) else Color(0xFFF1F5F9)))
                                .border(1.dp, if (!isPrivate) Color(0xFF0E7A52) else Color(0x22000000), RoundedCornerShape(14.dp))
                                .clickable { isPrivate = false }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "🌐 Public Group",
                                fontSize = 13.sp,
                                fontWeight = if (!isPrivate) FontWeight.Bold else FontWeight.Medium,
                                color = if (!isPrivate) Color.White else (if (isDarkTheme) Color(0xFFE2E8F0) else Color(0xFF334155))
                            )
                        }

                        // Private Group
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isPrivate) Color(0xFF0E7A52) else (if (isDarkTheme) Color(0xFF0F1D17) else Color(0xFFF1F5F9)))
                                .border(1.dp, if (isPrivate) Color(0xFF0E7A52) else Color(0x22000000), RoundedCornerShape(14.dp))
                                .clickable { isPrivate = true }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "🔒 Private (Code Only)",
                                fontSize = 13.sp,
                                fontWeight = if (isPrivate) FontWeight.Bold else FontWeight.Medium,
                                color = if (isPrivate) Color.White else (if (isDarkTheme) Color(0xFFE2E8F0) else Color(0xFF334155))
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Create Button
            Button(
                onClick = {
                    val finalTarget = targetStr.toLongOrNull() ?: 1000L
                    val finalCity = repository.getUserCity().ifBlank { "Makkah" }
                    onCreate(
                        title.ifBlank { "Group Dhikr" },
                        arabic,
                        finalTarget.coerceAtLeast(100L),
                        isPrivate,
                        finalCity,
                        false, // isSpecialMega
                        note
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .onGloballyPositioned { createBtnCoords = it },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E7A52))
            ) {
                Text(
                    text = "Create Group Zikr ➔",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Dhikr Picker Sheet Modal
        if (showPicker) {
            DhikrPickerSheet(
                onDismiss = { showPicker = false },
                onSelect = { item ->
                    title = item.translit
                    arabic = item.arabic
                    translit = item.translit
                    meaning = item.meaning
                },
                isDarkTheme = isDarkTheme
            )
        }

        // Spotlight Tour
        if (isCreateTourOpen) {
            val (stepTitle, stepDesc) = when (createTourStep) {
                1 -> tourStrings.createChooseTitle to tourStrings.createChooseDesc
                2 -> tourStrings.createTargetTitle to tourStrings.createTargetDesc
                3 -> tourStrings.createPrivacyTitle to tourStrings.createPrivacyDesc
                4 -> tourStrings.createSubmitTitle to tourStrings.createSubmitDesc
                else -> "" to ""
            }
            LiveDhikrSpotlightTourModal(
                step = createTourStep,
                totalSteps = 4,
                targetRect = currentTourRect,
                stepLabel = tourStrings.step.replace("{curr}", "$createTourStep").replace("{total}", "4"),
                title = stepTitle,
                description = stepDesc,
                skipBtnText = tourStrings.skip,
                nextBtnText = tourStrings.next,
                doneBtnText = tourStrings.done,
                onSkip = {
                    isCreateTourOpen = false
                    repository.setCreateTourDone(true)
                },
                onNext = {
                    if (createTourStep < 4) createTourStep++ else {
                        isCreateTourOpen = false
                        repository.setCreateTourDone(true)
                    }
                },
                onDone = {
                    isCreateTourOpen = false
                    repository.setCreateTourDone(true)
                }
            )
        }
    }
}
