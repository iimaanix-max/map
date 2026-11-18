/*
 * ════════════════════════════════════════════════════════════════════════════════════
 *  ⛔ CRITICAL ARCHITECTURAL LOCK / CODE FREEZE — DO NOT TOUCH OR MODIFY ⛔
 * ════════════════════════════════════════════════════════════════════════════════════
 *  STRICT INSTRUCTION FROM USER (PERMANENTLY LOCKED ON 2026-11-01):
 *
 *  THE TASBIH SCREEN AND ALL COUNTER LOGIC ARE 100% PERFECT AND FULLY FINALIZED.
 *  ABSOLUTELY NO AI ASSISTANT, DEVELOPER, OR AUTOMATED SCRIPT MAY MODIFY, REFACTOR,
 *  REORGANIZE, CLEAN UP, RE-STYLE, OR TOUCH ANY PART OF THIS FILE OR ANY RELATED
 *  TASBIH LOGIC UNLESS THE USER EXPLICITLY AND SPECIFICALLY GIVES A DIRECT COMMAND
 *  IN CHAT ASKING TO EDIT TASBIH.
 *
 *  STRICTLY FROZEN ELEMENTS:
 *   1. Realistic Beads Counter (RealisticBeadsCanvas):
 *      - Bézier curve geometry (P0, P1, P2), 11 logical slots, 7 visible slots.
 *      - Strictly 3 beads on the left (Slots 0, 1, 2) and 4 beads on the right (Slots 7, 8, 9, 10).
 *      - Natural glide animation curve (naturalTasbihSlide) over 440ms.
 *      - Right-to-left slide transition across center thread gap.
 *      - Subtle soft radial glow on active bead (NO hard ring outline, NO flat solid yellow disc).
 *      - ZERO black touch ripple / box highlight on tap.
 *   2. Digital Dial Counter (CircleCounter):
 *      - Smooth ring stroke progress, center tap-to-count interaction.
 *   3. Counting & Persistence:
 *      - Reset button ONLY resets the active session counter to 0.
 *      - Dhikr card lifetime badges, 'Today Total', and 'All Time' stats MUST NEVER be reset.
 *   4. Floating switch button (⭕ / beads toggle), Undo, Reset, Save controls.
 *   5. Dhikr selection, targets, marble themes/skins, sound and haptic effects.
 *
 *  DO NOT TOUCH OR EDIT ANYTHING IN THIS FILE WITHOUT EXPLICIT USER COMMAND.
 * ════════════════════════════════════════════════════════════════════════════════════
 */

package com.tasbih.digitaltasbih.ui.screens.tasbih



import android.content.Intent



import android.net.Uri



import android.widget.Toast



import androidx.compose.animation.AnimatedVisibility



import androidx.compose.animation.core.*



import androidx.compose.ui.graphics.graphicsLayer



import androidx.compose.animation.fadeIn



import androidx.compose.animation.fadeOut



import androidx.compose.foundation.Canvas



import androidx.compose.foundation.background



import androidx.compose.foundation.border



import androidx.compose.foundation.clickable



import androidx.compose.foundation.gestures.awaitEachGesture



import androidx.compose.foundation.gestures.awaitFirstDown



import androidx.compose.foundation.gestures.detectDragGestures



import androidx.compose.foundation.gestures.detectTapGestures



import androidx.compose.foundation.gestures.waitForUpOrCancellation



import androidx.compose.foundation.interaction.MutableInteractionSource



import androidx.compose.foundation.layout.*



import androidx.compose.foundation.lazy.LazyColumn



import androidx.compose.foundation.lazy.LazyRow



import androidx.compose.foundation.lazy.grid.GridCells



import androidx.compose.foundation.lazy.grid.LazyVerticalGrid



import androidx.compose.foundation.lazy.grid.items



import androidx.compose.foundation.lazy.items



import androidx.compose.foundation.shape.CircleShape



import androidx.compose.foundation.shape.RoundedCornerShape



import androidx.compose.material3.*



import androidx.compose.runtime.*



import androidx.compose.ui.Alignment



import androidx.compose.ui.Modifier



import androidx.compose.ui.draw.clip



import androidx.compose.ui.draw.drawBehind



import androidx.compose.ui.draw.scale



import androidx.compose.ui.draw.shadow



import androidx.compose.ui.geometry.Rect



import androidx.compose.ui.layout.boundsInRoot



import androidx.compose.ui.layout.onGloballyPositioned



import androidx.compose.ui.geometry.Offset



import androidx.compose.ui.geometry.Size



import androidx.compose.ui.graphics.Path



import androidx.compose.ui.graphics.Brush



import androidx.compose.ui.graphics.Color



import androidx.compose.ui.graphics.PathEffect



import androidx.compose.ui.graphics.StrokeCap



import androidx.compose.ui.graphics.drawscope.Stroke



import androidx.compose.ui.hapticfeedback.HapticFeedbackType



import androidx.compose.ui.input.pointer.pointerInput



import androidx.compose.ui.platform.LocalContext



import androidx.compose.ui.platform.LocalHapticFeedback



import androidx.compose.ui.text.font.FontFamily



import androidx.compose.ui.text.font.FontWeight



import androidx.compose.ui.text.style.TextAlign



import androidx.compose.ui.text.style.TextOverflow



import androidx.compose.ui.unit.dp



import androidx.compose.ui.unit.sp



import androidx.compose.ui.window.Dialog



import androidx.compose.ui.window.DialogProperties



import androidx.compose.foundation.rememberScrollState



import androidx.compose.foundation.verticalScroll



import androidx.compose.foundation.BorderStroke



import androidx.compose.foundation.Image



import androidx.compose.foundation.lazy.itemsIndexed



import androidx.compose.ui.layout.ContentScale



import com.tasbih.digitaltasbih.ui.components.CompactFloatingNativeVideoAd



import com.tasbih.digitaltasbih.ui.components.AdMobBannerAd



import com.tasbih.digitaltasbih.util.AdConfig



import com.tasbih.digitaltasbih.ui.components.AppToast



import androidx.compose.ui.zIndex



import com.tasbih.digitaltasbih.ui.components.BottomNavigationBar



import com.tasbih.digitaltasbih.ui.components.LanguageBar



import com.tasbih.digitaltasbih.ui.components.MoreLanguagesModal



import com.tasbih.digitaltasbih.ui.components.rememberAssetPainter



import com.tasbih.digitaltasbih.data.*



import com.tasbih.digitaltasbih.ui.theme.AmiriQuranFont



import com.tasbih.digitaltasbih.ui.theme.AmiriBoldFont



import androidx.compose.ui.text.font.FontStyle



import androidx.compose.ui.text.buildAnnotatedString



import androidx.compose.ui.text.withStyle



import androidx.compose.ui.text.SpanStyle



import androidx.compose.material.icons.Icons



import androidx.compose.material.icons.filled.Delete



import androidx.compose.material.icons.filled.Edit



import androidx.compose.material.icons.filled.Menu



import androidx.compose.material.icons.filled.Share



import androidx.compose.ui.graphics.vector.ImageVector



import com.tasbih.digitaltasbih.util.TasbihAudioEngine



import kotlinx.coroutines.launch



/**



* TasbihScreen - The Master Jetpack Compose implementation of Digital Tasbih.



* Faithfully recreating every visual detail and behavior from the original web app



* and the 23 reference screenshots in whatspp images.



*/



@Composable



fun TasbihScreen(



initialTab: Int = 0,



onBack: () -> Unit = {},



onNavigateToDarood: (String?) -> Unit = {},



onNavigateToHome: () -> Unit = {},



onNavigateToQuran: () -> Unit = {},



onNavigateToPrayerTimes: () -> Unit = {},



onNavigateToDua: () -> Unit = {},



onNavigateToCalendar: () -> Unit = {},



onOpenSettings: () -> Unit = {}



) {



val context = LocalContext.current



val haptic = LocalHapticFeedback.current



val coroutineScope = rememberCoroutineScope()



// ── Repository Initialization ──



LaunchedEffect(Unit) {



TasbihRepository.init(context)



DhikrRepository.init(context)



}



// ── State Variables ──



var selectedLanguage by remember { mutableStateOf("en") }



val strings: TasbihStrings = remember(selectedLanguage) { TasbihLocalization.get(selectedLanguage) }



var activeTab by remember(initialTab) { mutableIntStateOf(initialTab) } // 0: Tasbih, 1: History, 2: Guide, 3: 99 Names, 4: Streak



var isNightMode by remember { mutableStateOf(false) }



var isSoundEnabled by remember { mutableStateOf(true) }



var isVibrationEnabled by remember { mutableStateOf(true) }



var isBeadsViewMode by remember { mutableStateOf(false) }



var isTouchModeEnabled by remember { mutableStateOf(false) }



var isMoreLangOpen by remember { mutableStateOf(false) }



val languages = remember {



listOf(



LanguageItem("en", "English"),



LanguageItem("hi", "हिंदी"),



LanguageItem("ur", "اردو"),



LanguageItem("ar", "العربية"),



LanguageItem("bn", "বাংলা")



)



}



// Dhikr items list (merges Core + Custom + Firebase Admin extra dhikrs)



var allDhikrs by remember { mutableStateOf(TasbihRepository.getAllDhikrs()) }



var activeDhikrId by remember { mutableStateOf(TasbihRepository.getActiveDhikrId()) }



val activeDhikr: TasbihDhikrItem = remember(activeDhikrId, allDhikrs) {



allDhikrs.find { it.id == activeDhikrId } ?: allDhikrs.firstOrNull() ?: TasbihRepository.DEFAULT_DHIKR_LIST.first()



}



// Counts & Target



var sessionCount by remember(activeDhikrId) { mutableIntStateOf(TasbihRepository.getSessionCount(activeDhikrId)) }



var todayCount by remember(activeDhikrId) { mutableIntStateOf(TasbihRepository.getTodayCount(activeDhikrId)) }



var allTimeCount by remember(activeDhikrId) { mutableIntStateOf(TasbihRepository.getAllTimeCount(activeDhikrId)) }



var targetCount by remember(activeDhikrId) { mutableIntStateOf(TasbihRepository.getTarget(activeDhikrId, activeDhikr.defaultTarget)) }



var grandTotal by remember { mutableIntStateOf(TasbihRepository.getGrandTotal()) }



// Active Bead Marble Skin



var activeSkin by remember { mutableStateOf(TasbihRepository.getActiveBeadSkin()) }



// Modals visibility



var showSetTargetModal by remember { mutableStateOf(false) }



var showAddCustomModal by remember { mutableStateOf(false) }



var showSelectDhikrModal by remember { mutableStateOf(false) }



var showMarbleGalleryModal by remember { mutableStateOf(false) }



var showSpotlightTour by remember { mutableStateOf(!TasbihRepository.isTasbihTourDone() && initialTab == 0) }



var targetRowBounds by remember { mutableStateOf<Rect?>(null) }



var dialBounds by remember { mutableStateOf<Rect?>(null) }



var selectedHadithGuide by remember { mutableStateOf<DhikrGuideDetail?>(null) }



var showResetConfirmDialog by remember { mutableStateOf(false) }



// Dial Tap Scale Animation



val dialScale = remember { Animatable(1f) }



// ── Counting Action Handler (Zero-lag, 0ms latency) ──



fun performCountTap() {



if (isVibrationEnabled) {



haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)



}



if (isSoundEnabled) {



val nextVal = sessionCount + 1



if (targetCount > 0 && nextVal % targetCount == 0) {



TasbihAudioEngine.playCompletionChime()



} else {



TasbihAudioEngine.playSandalwoodClick()



}



}



TasbihRepository.recordCount(activeDhikrId, 1)



sessionCount = TasbihRepository.getSessionCount(activeDhikrId)



todayCount = TasbihRepository.getTodayCount(activeDhikrId)



allTimeCount = TasbihRepository.getAllTimeCount(activeDhikrId)



grandTotal = TasbihRepository.getGrandTotal()



}



fun performUndo() {



if (sessionCount > 0) {



if (isVibrationEnabled) {



haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)



}



TasbihRepository.undoCount(activeDhikrId)



sessionCount = TasbihRepository.getSessionCount(activeDhikrId)



todayCount = TasbihRepository.getTodayCount(activeDhikrId)



allTimeCount = TasbihRepository.getAllTimeCount(activeDhikrId)



grandTotal = TasbihRepository.getGrandTotal()



}



}



fun performReset() {



TasbihRepository.resetSession(activeDhikrId)



sessionCount = 0



showResetConfirmDialog = false



AppToast.show("Counter reset to 0")



}



fun performSave() {



if (sessionCount > 0) {



TasbihRepository.saveSessionToHistory(activeDhikr, sessionCount)



sessionCount = 0



grandTotal = TasbihRepository.getGrandTotal()



Toast.makeText(context, "Saved to Dhikr History!", Toast.LENGTH_SHORT).show()



} else {



Toast.makeText(context, "Nothing to save yet (count is 0)", Toast.LENGTH_SHORT).show()



}



}



// Theme Colors



val bgColor = if (isNightMode) Color(0xFF0F1A15) else Color(0xFFF7F5F0)



val cardBgColor = if (isNightMode) Color(0xFF16251E) else Color(0xFFFAF8F5)



val textPrimary = if (isNightMode) Color(0xFFEDEDED) else Color(0xFF1C2826)



val textSecondary = if (isNightMode) Color(0xFF9EABA2) else Color(0xFF6B7280)



val goldColor = Color(0xFFC9A84C)



val emeraldColor = Color(0xFF104434)



Box(



modifier = Modifier



.fillMaxSize()



.background(bgColor)



) {



Scaffold(



topBar = {



Column(



modifier = Modifier.background(bgColor)



) {



// 1. TOP FLOATING HEADER CARD (Exact Image 2 & 3, positioned identically to HomeScreen)



Column(



modifier = Modifier



.fillMaxWidth()



.statusBarsPadding()



.padding(horizontal = 12.dp, vertical = 6.dp)



) {



Box(



modifier = Modifier



.fillMaxWidth()



.shadow(elevation = 6.dp, shape = RoundedCornerShape(18.dp))



.clip(RoundedCornerShape(18.dp))



.background(



Brush.linearGradient(



colors = if (isNightMode) listOf(Color(0xFF0B1410), Color(0xFF14241E))



else listOf(Color(0xFF146450), Color(0xFF0C4A3A), Color(0xFF062018))



)



)



.border(1.5.dp, Color(0x8CE8C878), RoundedCornerShape(18.dp))



.padding(horizontal = 12.dp, vertical = 9.dp)



) {



Row(



modifier = Modifier.fillMaxWidth(),



horizontalArrangement = Arrangement.SpaceBetween,



verticalAlignment = Alignment.CenterVertically



) {



// Left: Logo Badge + Title + Subtitle



Row(



verticalAlignment = Alignment.CenterVertically



) {



Box(



modifier = Modifier



.size(36.dp)



.clip(RoundedCornerShape(10.dp))



.background(



Brush.linearGradient(



colors = listOf(Color(0xFF1E8F6E), Color(0xFF083D32))



)



)



.border(1.5.dp, Color(0xFFE8C878), RoundedCornerShape(10.dp)),



contentAlignment = Alignment.Center



) {



val tasbihPainter = rememberAssetPainter("media/59329dcad16a9eac.webp")



Image(



painter = tasbihPainter,



contentDescription = "Logo",



contentScale = ContentScale.Fit,



modifier = Modifier.size(24.dp)



)



}



Spacer(modifier = Modifier.width(9.dp))



Column {



Text(



text = "DIGITAL TASBIH",



fontSize = 13.sp,



fontWeight = FontWeight.ExtraBold,



fontFamily = FontFamily.Serif,



color = Color.White,



letterSpacing = 0.8.sp



)



Text(



text = strings.appSubtitle,



fontSize = 10.sp,



color = Color(0xFFE8C878),



fontWeight = FontWeight.SemiBold



)



}



}



// Right: Header Action Buttons (Dynamic: Counter controls on Tab 0; Settings/Love/Share/Theme on other tabs matching Image 1 & 2)



Row(



horizontalArrangement = Arrangement.spacedBy(6.dp),



verticalAlignment = Alignment.CenterVertically



) {



if (activeTab == 0) {



// 1. Touch Mode Toggle Button (👆)



HeaderIconButton(



icon = "👆",



isActive = isTouchModeEnabled,



onClick = {



isTouchModeEnabled = !isTouchModeEnabled



Toast.makeText(



context,



if (isTouchModeEnabled) "Full-Screen Touch Mode Activated" else "Touch Mode Exited",



Toast.LENGTH_SHORT



).show()



}



)



// 2. Theme Toggle Button (☀️ / 🌙)



HeaderIconButton(



icon = if (isNightMode) "🌙" else "☀️",



isActive = isNightMode,



onClick = { isNightMode = !isNightMode }



)



// 3. Vibration / Haptic Toggle Button (📳)



HeaderIconButton(



icon = if (isVibrationEnabled) "📳" else "🔕",



isActive = isVibrationEnabled,



onClick = {



isVibrationEnabled = !isVibrationEnabled



Toast.makeText(



context,



if (isVibrationEnabled) "Vibration ON" else "Vibration OFF",



Toast.LENGTH_SHORT



).show()



}



)



// 4. Sound Toggle Button (🔊 / 🔇)



HeaderIconButton(



icon = if (isSoundEnabled) "🔊" else "🔇",



isActive = isSoundEnabled,



onClick = {



isSoundEnabled = !isSoundEnabled



Toast.makeText(



context,



if (isSoundEnabled) "Sound ON" else "Sound OFF",



Toast.LENGTH_SHORT



).show()



}



)



} else {



// Exact Image 1 & 2 Buttons: [ ⚙️ ] [ ❤️ ] [ 🔗 ] [ ☀️ / 🌙 ]



HeaderIconButton(



icon = "⚙️",



isActive = false,



onClick = {



Toast.makeText(context, "Opening Settings...", Toast.LENGTH_SHORT).show()



}



)



HeaderIconButton(



icon = "❤️",



isActive = false,



onClick = {



Toast.makeText(context, "Thank you for your love & support ❤️", Toast.LENGTH_SHORT).show()



}



)



HeaderIconButton(



imageVector = Icons.Default.Share,



isActive = false,



onClick = {



val shareIntent = Intent(Intent.ACTION_SEND).apply {



type = "text/plain"



putExtra(Intent.EXTRA_SUBJECT, "Digital Tasbih")



putExtra(Intent.EXTRA_TEXT, "Digital Tasbih - Daily Dhikr & Quran App: https://play.google.com/store/apps/details?id=com.tasbih.digitaltasbih")



}



context.startActivity(Intent.createChooser(shareIntent, "Share Digital Tasbih"))



}



)



HeaderIconButton(



icon = if (isNightMode) "🌙" else "☀️",



isActive = isNightMode,



onClick = { isNightMode = !isNightMode }



)



}



}



}



}



}



// 2. LANGUAGE BAR (Exact Image 2 & 3)



LanguageBar(



languages = languages,



selectedCode = selectedLanguage,



onSelectLanguage = { selectedLanguage = it },



onOpenMoreLanguages = { isMoreLangOpen = true }



)



}



},



bottomBar = {



BottomNavigationBar(



activeTabId = "tasbih",



onTabSelected = { tabId ->



when (tabId) {



"home" -> onNavigateToHome()



"tasbih" -> { /* Already on Tasbih */ }



"quran" -> onNavigateToQuran()



"calendar" -> onNavigateToCalendar()



"dua" -> onNavigateToDua()



}



}



)



},



containerColor = bgColor



) { paddingValues ->



Box(



modifier = Modifier



.fillMaxSize()



.padding(paddingValues)



.background(bgColor)



) {



Column(



modifier = Modifier



.fillMaxSize()



) {



// ══════════════════════════════════════════════════════════



// 3. SECONDARY TABS ROW (Exact Image 2 & 3)



// ══════════════════════════════════════════════════════════



val tabTitles = listOf(



"📿 " + strings.tasbih,



"📊 " + strings.history,



"📖 " + strings.guide,



"✨ " + strings.names99,



"🔥 " + strings.currentStreak



)



Box(



modifier = Modifier



.fillMaxWidth()



.padding(horizontal = 12.dp, vertical = 4.dp)



.clip(RoundedCornerShape(16.dp))



.background(if (isNightMode) Color(0xFF16251E) else Color(0xFFFBF9F5))



.border(1.dp, Color(0x22A07828), RoundedCornerShape(16.dp))



.padding(4.dp)



) {



LazyRow(



modifier = Modifier.fillMaxWidth(),



horizontalArrangement = Arrangement.spacedBy(6.dp),



verticalAlignment = Alignment.CenterVertically



) {



itemsIndexed(tabTitles) { idx, title ->



val isSelected = activeTab == idx



Box(



modifier = Modifier



.clip(RoundedCornerShape(12.dp))



.background(if (isSelected) Color(0xFF104434) else Color.Transparent)



.clickable {



if (activeTab == 0 && idx == 1 && sessionCount > 0) {



TasbihRepository.saveSessionToHistory(activeDhikr, sessionCount)



sessionCount = 0



grandTotal = TasbihRepository.getGrandTotal()



}



activeTab = idx



}



.padding(horizontal = 12.dp, vertical = 6.dp),



contentAlignment = Alignment.Center



) {



Text(



text = title,



fontSize = 12.sp,



fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,



color = if (isSelected) Color.White else (if (isNightMode) Color(0xFFB0C0B7) else Color(0xFF6B7280))



)



}



}



}



}



// ══════════════════════════════════════════════════════════



// 4. MAIN CONTENT AREA BASED ON ACTIVE TAB



// ══════════════════════════════════════════════════════════



Box(



modifier = Modifier



.fillMaxSize()



.weight(1f)



) {



when (activeTab) {



0 -> TasbihCounterView(



isNightMode = isNightMode,



strings = strings,



selectedLanguage = selectedLanguage,



allDhikrs = allDhikrs,



activeDhikr = activeDhikr,



activeSkin = activeSkin,



isBeadsViewMode = isBeadsViewMode,



sessionCount = sessionCount,



todayCount = todayCount,



allTimeCount = allTimeCount,



targetCount = targetCount,



dialScale = dialScale.value,



onCountTap = { performCountTap() },



onUndoTap = { performUndo() },



onResetTap = { showResetConfirmDialog = true },



onSaveTap = { performSave() },



onSelectDhikr = { item ->



if (sessionCount > 0 && item.id != activeDhikrId) {



TasbihRepository.saveSessionToHistory(activeDhikr, sessionCount)



}



activeDhikrId = item.id



TasbihRepository.setActiveDhikrId(item.id)



sessionCount = TasbihRepository.getSessionCount(item.id)



todayCount = TasbihRepository.getTodayCount(item.id)



allTimeCount = TasbihRepository.getAllTimeCount(item.id)



targetCount = TasbihRepository.getTarget(item.id, item.defaultTarget)



grandTotal = TasbihRepository.getGrandTotal()



},



onNavigateToDarood = { onNavigateToDarood(null) },



onOpenSetTarget = { showSetTargetModal = true },



onOpenAddCustom = { showAddCustomModal = true },



onOpenSelectDhikr = { showSelectDhikrModal = true },



onOpenMarbleGallery = { showMarbleGalleryModal = true },



onToggleBeadsMode = {



isBeadsViewMode = !isBeadsViewMode



},



onTargetRowBounds = { targetRowBounds = it },



onDialBounds = { dialBounds = it }



)



1 -> TasbihHistoryView(



activeTab = activeTab,



isNightMode = isNightMode,



selectedLanguage = selectedLanguage,



strings = strings,



grandTotal = grandTotal,



allDhikrs = allDhikrs,



onContinueDhikr = { histItem ->



if (histItem.dhikrId.startsWith("darood_")) {



onNavigateToDarood(histItem.dhikrId)



} else {



activeDhikrId = histItem.dhikrId



TasbihRepository.setActiveDhikrId(histItem.dhikrId)



sessionCount = TasbihRepository.getSessionCount(histItem.dhikrId)



todayCount = TasbihRepository.getTodayCount(histItem.dhikrId)



allTimeCount = TasbihRepository.getAllTimeCount(histItem.dhikrId)



targetCount = TasbihRepository.getTarget(histItem.dhikrId, 33)



activeTab = 0



}



val toastName = allDhikrs.find { it.id == histItem.dhikrId }?.getName(selectedLanguage) ?: histItem.dhikrName



Toast.makeText(



context,



"✓ $toastName",



Toast.LENGTH_SHORT



).show()



}



)



2 -> TasbihGuideView(



isNightMode = isNightMode,



strings = strings,



selectedLanguage = selectedLanguage,



onOpenHadithRef = { guide ->



selectedHadithGuide = guide



},



onUseDhikrInCounter = { guide ->



val targetInt = when {



guide.targetStr.contains("33+33+34") -> 100



guide.targetStr.contains("100") -> 100



guide.targetStr.contains("40") -> 40



guide.targetStr.contains("70") -> 70



guide.targetStr.contains("10") -> 10



guide.targetStr.contains("7") -> 7



guide.targetStr.contains("1") -> 1



guide.targetStr.contains("∞") -> 100



else -> Regex("\\d+").find(guide.targetStr)?.value?.toIntOrNull() ?: 100



}



val match = allDhikrs.find { it.arabic.trim() == guide.arabic.trim() || it.id == guide.id }



if (match != null) {



activeDhikrId = match.id



TasbihRepository.setActiveDhikrId(match.id)



} else {



val guideTitle = guide.getTitle(selectedLanguage).ifBlank { guide.getTitle("en") }.ifBlank { "Guide Dhikr" }



val customItem = TasbihRepository.addCustomDhikr(



name = guideTitle,



arabic = guide.arabic,



target = targetInt



)



allDhikrs = TasbihRepository.getAllDhikrs()



activeDhikrId = customItem.id



TasbihRepository.setActiveDhikrId(customItem.id)



}



sessionCount = 0



targetCount = targetInt



TasbihRepository.setTarget(activeDhikrId, targetInt)



activeTab = 0



val label = guide.getTitle(selectedLanguage).ifBlank { guide.getTitle("en") }



Toast.makeText(context, "✓ $label ($targetInt)", Toast.LENGTH_SHORT).show()



}



)



3 -> TasbihNamesOfAllahView(



isNightMode = isNightMode,



strings = strings,



selectedLanguage = selectedLanguage,



activeTab = activeTab,



onSelectNameForRecital = { asmaItem ->



val nameId = "name_${asmaItem.number}"



if (sessionCount > 0 && activeDhikrId != nameId) {



TasbihRepository.saveSessionToHistory(activeDhikr, sessionCount)



}



TasbihRepository.setActiveDhikrId(nameId)



activeDhikrId = nameId



allDhikrs = TasbihRepository.getAllDhikrs()



sessionCount = TasbihRepository.getSessionCount(nameId)



todayCount = TasbihRepository.getTodayCount(nameId)



allTimeCount = TasbihRepository.getAllTimeCount(nameId)



targetCount = TasbihRepository.getTarget(nameId, 99)



grandTotal = TasbihRepository.getGrandTotal()



activeTab = 0



Toast.makeText(



context,



"✓ ${asmaItem.transliteration} (${asmaItem.arabic})",



Toast.LENGTH_SHORT



).show()



}



)



4 -> TasbihStreaksView(



isNightMode = isNightMode,



strings = strings



)



}



}



}



}



// ══════════════════════════════════════════════════════════════



// ROOT-LEVEL MODALS & SPOTLIGHT TOUR



// ══════════════════════════════════════════════════════════════



if (showSetTargetModal) {



SetTargetModal(



currentTarget = targetCount,



onSelectTarget = { newTgt ->



targetCount = newTgt



TasbihRepository.setTarget(activeDhikrId, newTgt)



showSetTargetModal = false



},



onDismiss = { showSetTargetModal = false }



)



}



if (showAddCustomModal) {



AddCustomDhikrModal(



onAddDhikr = { name, arabic ->



val newDhikr = TasbihRepository.addCustomDhikr(name, arabic, 33)



allDhikrs = TasbihRepository.getAllDhikrs()



activeDhikrId = newDhikr.id



TasbihRepository.setActiveDhikrId(newDhikr.id)



sessionCount = 0



todayCount = 0



allTimeCount = 0



targetCount = newDhikr.defaultTarget



showAddCustomModal = false



},



onDismiss = { showAddCustomModal = false }



)



}



if (showSelectDhikrModal) {



SelectDhikrModal(



activeDhikrId = activeDhikrId,



onSelectDhikr = { selected ->



activeDhikrId = selected.id



TasbihRepository.setActiveDhikrId(selected.id)



sessionCount = TasbihRepository.getSessionCount(selected.id)



todayCount = TasbihRepository.getTodayCount(selected.id)



allTimeCount = TasbihRepository.getAllTimeCount(selected.id)



targetCount = TasbihRepository.getTarget(selected.id, selected.defaultTarget)



showSelectDhikrModal = false



},



onOpenAddCustom = {



showSelectDhikrModal = false



showAddCustomModal = true



},



onDismiss = { showSelectDhikrModal = false }



)



}



if (showMarbleGalleryModal) {



MarbleGalleryModal(



activeSkinId = activeSkin.id,



onSelectSkin = { chosenSkin ->



activeSkin = chosenSkin



TasbihRepository.setActiveBeadSkin(chosenSkin.id)



isBeadsViewMode = true



showMarbleGalleryModal = false



},



onDismiss = { showMarbleGalleryModal = false }



)



}



var tourStep by remember { mutableIntStateOf(1) }



if (showSpotlightTour && activeTab == 0) {



val targetBounds = if (tourStep == 1) targetRowBounds else dialBounds



TasbihSpotlightTour(



step = tourStep,



targetBounds = targetBounds,



totalSteps = 2,



lang = selectedLanguage,



onNext = {



if (tourStep == 1) {



tourStep = 2



} else {



TasbihRepository.setTasbihTourDone(true)



showSpotlightTour = false



}



},



onDone = {



TasbihRepository.setTasbihTourDone(true)



showSpotlightTour = false



},



onSkip = {



TasbihRepository.setTasbihTourDone(true)



showSpotlightTour = false



}



)



}



// Hadith Detail Reference Modal



selectedHadithGuide?.let { guide ->



AuthenticHadithReferenceModal(



guide = guide,



selectedLanguage = selectedLanguage,



strings = strings,



onDismiss = { selectedHadithGuide = null }



)



}



// Reset Confirm Dialog



if (showResetConfirmDialog) {



AlertDialog(



onDismissRequest = { showResetConfirmDialog = false },



title = {



Text(text = "Reset Session?", fontWeight = FontWeight.Bold, color = textPrimary)



},



text = {



Text(



text = "Are you sure you want to reset current session count ($sessionCount) to 0? Today's total will remain safe.",



color = textSecondary



)



},



confirmButton = {



TextButton(onClick = { performReset() }) {



Text(text = "Reset", color = Color(0xFFC81D3F), fontWeight = FontWeight.Bold)



}



},



dismissButton = {



TextButton(onClick = { showResetConfirmDialog = false }) {



Text(text = "Cancel", color = textSecondary)



}



},



containerColor = cardBgColor



)



}



// Full 25+ Languages BottomSheet Modal (Triggered by 🌐 +20)



if (isMoreLangOpen) {



MoreLanguagesModal(



selectedCode = selectedLanguage,



onSelectLanguage = { code ->



selectedLanguage = code



},



onDismiss = {



isMoreLangOpen = false



}



)



}



}



// 🎬 COMPACT FLOATING NATIVE VIDEO AD (Positioned at Top Header level inside Root Box) 🎬



if (activeTab == 0 && !isTouchModeEnabled) {



CompactFloatingNativeVideoAd(



modifier = Modifier



.align(Alignment.TopStart)



.statusBarsPadding()



.padding(start = 10.dp, top = 8.dp),



isDarkTheme = isNightMode



)



}



// ══════════════════════════════════════════════════════════════════



// TRUE FULL-SCREEN TOUCH OVERLAY (Edge-to-Edge across whole window)



// ══════════════════════════════════════════════════════════════════



if (isTouchModeEnabled && activeTab == 0) {



Box(



modifier = Modifier



.fillMaxSize()



.zIndex(999f)



.background(if (isNightMode) Color(0xFF07140E) else Color(0xFF0B1C14))



.pointerInput(Unit) {



awaitEachGesture {



val down = awaitFirstDown(requireUnconsumed = false)



performCountTap()



waitForUpOrCancellation()



}



}



) {



// Exit Touch Mode Floating Capsule



Box(



modifier = Modifier



.align(Alignment.TopCenter)



.statusBarsPadding()



.padding(top = 16.dp)



.clip(RoundedCornerShape(20.dp))



.background(Color(0xFF0F4736))



.border(1.5.dp, Color(0xFFC9A84C), RoundedCornerShape(20.dp))



.clickable {



isTouchModeEnabled = false



AppToast.show("Touch Mode Exited")



}



.padding(horizontal = 20.dp, vertical = 10.dp)



) {



Row(



verticalAlignment = Alignment.CenterVertically,



horizontalArrangement = Arrangement.spacedBy(8.dp)



) {



Text(text = "👆", fontSize = 15.sp)



Text(



text = "Touch anywhere to count • Tap here to exit",



color = Color(0xFFFFF7D6),



fontSize = 13.sp,



fontWeight = FontWeight.Bold



)



}



}



// Giant Counter Display in Center of Full-Screen Touch



Column(



modifier = Modifier.align(Alignment.Center),



horizontalAlignment = Alignment.CenterHorizontally



) {



Text(



text = activeDhikr.arabic,



color = Color(0xFFF3E5AB),



fontSize = 40.sp,



fontWeight = FontWeight.Bold,



fontFamily = AmiriQuranFont,



textAlign = TextAlign.Center,



modifier = Modifier.padding(horizontal = 24.dp)



)



Spacer(modifier = Modifier.height(14.dp))



Text(



text = "$sessionCount",



color = Color.White,



fontSize = 96.sp,



fontWeight = FontWeight.ExtraBold,



fontFamily = FontFamily.Serif



)



Spacer(modifier = Modifier.height(6.dp))



Text(



text = "Target: $targetCount",



color = Color(0xFFC9A84C),



fontSize = 17.sp,



fontWeight = FontWeight.SemiBold



)



}



}



}



}



}



// ══════════════════════════════════════════════════════════════════════



// 5. TAB 1: TASBIH MAIN COUNTER VIEW



// ══════════════════════════════════════════════════════════════════════



@Composable



private fun TasbihCounterView(



isNightMode: Boolean,



strings: TasbihStrings,



selectedLanguage: String,



allDhikrs: List<TasbihDhikrItem>,



activeDhikr: TasbihDhikrItem,



activeSkin: BeadMarbleSkin,



isBeadsViewMode: Boolean,



sessionCount: Int,



todayCount: Int,



allTimeCount: Int,



targetCount: Int,



dialScale: Float,



onCountTap: () -> Unit,



onUndoTap: () -> Unit,



onResetTap: () -> Unit,



onSaveTap: () -> Unit,



onSelectDhikr: (TasbihDhikrItem) -> Unit,



onNavigateToDarood: () -> Unit,



onOpenSetTarget: () -> Unit,



onOpenAddCustom: () -> Unit,



onOpenSelectDhikr: () -> Unit,



onOpenMarbleGallery: () -> Unit,



onToggleBeadsMode: () -> Unit,



onTargetRowBounds: (Rect) -> Unit = {},



onDialBounds: (Rect) -> Unit = {}



) {



val goldColor = Color(0xFFC9A84C)



val emeraldColor = Color(0xFF104434)



val cardBg = if (isNightMode) Color(0xFF16251E) else Color.White



val textPrimary = if (isNightMode) Color(0xFFEDEDED) else Color(0xFF1C2826)



val textSecondary = if (isNightMode) Color(0xFF9EABA2) else Color(0xFF6B7280)



LazyColumn(



modifier = Modifier



.fillMaxSize()



.padding(horizontal = 14.dp),



verticalArrangement = Arrangement.spacedBy(6.dp),



contentPadding = PaddingValues(top = 4.dp, bottom = 12.dp)



) {



// ── Sub-header: SELECT DHIKR + [ Darood Sharif › ] [ ✎ ] [ ☰ ] ──



item {



Row(



modifier = Modifier



.fillMaxWidth()



.padding(vertical = 2.dp),



verticalAlignment = Alignment.CenterVertically,



horizontalArrangement = Arrangement.SpaceBetween



) {



Text(



text = strings.selectDhikr.uppercase(),



color = Color(0xFF8C7A5B),



fontSize = 11.5.sp,



fontWeight = FontWeight.ExtraBold,



letterSpacing = 0.8.sp



)



Row(



verticalAlignment = Alignment.CenterVertically,



horizontalArrangement = Arrangement.spacedBy(8.dp)



) {



// Darood Sharif quick jump button (Fully rounded pill, dark emerald, white text)



Box(



modifier = Modifier



.clip(RoundedCornerShape(50))



.background(Color(0xFF0F4736))



.clickable { onNavigateToDarood() }



.padding(horizontal = 14.dp, vertical = 7.dp)



) {



Text(



text = "${strings.daroodSharif} ›",



color = Color.White,



fontSize = 12.5.sp,



fontWeight = FontWeight.Bold



)



}



// Add Custom Dhikr Pencil [ ✎ ] (White squircle, green vector icon)



Box(



modifier = Modifier



.size(34.dp)



.shadow(elevation = 1.5.dp, shape = RoundedCornerShape(10.dp))



.clip(RoundedCornerShape(10.dp))



.background(Color.White)



.border(1.2.dp, Color(0xFFE2DCD0), RoundedCornerShape(10.dp))



.clickable { onOpenAddCustom() },



contentAlignment = Alignment.Center



) {



Icon(



imageVector = Icons.Default.Edit,



contentDescription = "Custom Dhikr",



tint = Color(0xFF0E5D47),



modifier = Modifier.size(17.dp)



)



}



// Menu List All Dhikrs [ ☰ ] (White squircle, green vector icon)



Box(



modifier = Modifier



.size(34.dp)



.shadow(elevation = 1.5.dp, shape = RoundedCornerShape(10.dp))



.clip(RoundedCornerShape(10.dp))



.background(Color.White)



.border(1.2.dp, Color(0xFFE2DCD0), RoundedCornerShape(10.dp))



.clickable { onOpenSelectDhikr() },



contentAlignment = Alignment.Center



) {



Icon(



imageVector = Icons.Default.Menu,



contentDescription = "Select Dhikr",



tint = Color(0xFF0E5D47),



modifier = Modifier.size(18.dp)



)



}



}



}



}



// ── Horizontal Sliding Dhikr Cards Carousel (Exact Reference Match with Badges) ──



item {



LazyRow(



modifier = Modifier.fillMaxWidth(),



horizontalArrangement = Arrangement.spacedBy(10.dp),



contentPadding = PaddingValues(top = 6.dp, bottom = 4.dp, start = 2.dp, end = 12.dp)



) {



items(allDhikrs) { item ->



val isSelected = item.id == activeDhikr.id



val liveCount = if (isSelected) {



allTimeCount



} else {



TasbihRepository.getAllTimeCount(item.id)



}



val dhikrName = item.getName(selectedLanguage)



// Outer Box to anchor the floating top-right badge without clipping



Box(



modifier = Modifier



.padding(top = 5.dp, end = 6.dp)



) {



// Main Chip Card (Exact CSS Match: .dhikr-chip.selected background gradient #0E5D47 -> #083D32, border #C9A84C)



Box(



modifier = Modifier



.wrapContentWidth()



.defaultMinSize(minWidth = 114.dp)



.height(68.dp)



.shadow(elevation = if (isSelected) 4.dp else 1.dp, shape = RoundedCornerShape(16.dp))



.clip(RoundedCornerShape(16.dp))



.background(



if (isSelected) {



Brush.linearGradient(listOf(Color(0xFF0E5D47), Color(0xFF083D32)))



} else {



Brush.linearGradient(



if (isNightMode) listOf(Color(0xFF16251E), Color(0xFF16251E))



else listOf(Color.White, Color.White)



)



}



)



.border(



width = if (isSelected) 1.5.dp else 1.2.dp,



color = if (isSelected) Color(0xFFC9A84C) else (if (isNightMode) Color(0xFF2D4539) else Color(0xFFE5DAC6)),



shape = RoundedCornerShape(16.dp)



)



.clickable { onSelectDhikr(item) }



.padding(horizontal = 14.dp, vertical = 6.dp),



contentAlignment = Alignment.Center



) {



Column(



horizontalAlignment = Alignment.CenterHorizontally,



verticalArrangement = Arrangement.Center



) {



Text(



text = item.arabic,



color = if (isSelected) Color(0xFFF3D48A) else (if (isNightMode) Color(0xFF68D391) else Color(0xFF0E5D47)),



fontSize = if (item.arabic.length > 25) 15.sp else 17.5.sp,



fontWeight = FontWeight.Bold,



fontFamily = AmiriBoldFont,



softWrap = false,



maxLines = 1,



textAlign = TextAlign.Center



)



Spacer(modifier = Modifier.height(3.dp))



Text(



text = dhikrName,



color = if (isSelected) Color.White else (if (isNightMode) Color(0xFFCBD5E1) else Color(0xFF1A1408)),



fontSize = if (dhikrName.length > 22) 10.sp else 11.5.sp,



fontWeight = FontWeight.Bold,



softWrap = false,



maxLines = 1,



textAlign = TextAlign.Center



)



}



}



// Floating Top-Right Count Badge (Exact CSS .chip-badge: top -8px, right -8px, bg #083D32, text #F3D48A, border #C9A84C)



Box(



modifier = Modifier



.align(Alignment.TopEnd)



.offset(x = 6.dp, y = (-6).dp)



.shadow(elevation = 3.dp, shape = RoundedCornerShape(50))



.clip(RoundedCornerShape(50))



.background(if (isSelected) Color(0xFF083D32) else Color(0xFF0E4435))



.border(1.5.dp, Color(0xFFC9A84C), RoundedCornerShape(50))



.padding(horizontal = 7.dp, vertical = 2.dp),



contentAlignment = Alignment.Center



) {



Text(



text = "$liveCount",



color = Color(0xFFF3D48A),



fontSize = 10.sp,



fontWeight = FontWeight.Black,



lineHeight = 11.sp



)



}



}



}



}



}



// ── Active Dhikr Focus Card (Pure White with Amiri Bold Typography) ──



item {



Box(



modifier = Modifier



.fillMaxWidth()



.shadow(elevation = 2.dp, shape = RoundedCornerShape(20.dp))



.clip(RoundedCornerShape(20.dp))



.background(if (isNightMode) Color(0xFF16251E) else Color.White)



.border(1.2.dp, Color(0xFFE5DAC6), RoundedCornerShape(20.dp))



.padding(vertical = 11.dp, horizontal = 14.dp)



) {



Column(



modifier = Modifier.fillMaxWidth(),



horizontalAlignment = Alignment.CenterHorizontally,



verticalArrangement = Arrangement.Center



) {



Text(



text = activeDhikr.arabic,



color = if (isNightMode) Color(0xFFE8F5E9) else Color(0xFF0E5D47),



fontSize = if (activeDhikr.arabic.length > 30) 24.sp else 28.sp,



fontWeight = FontWeight.Bold,



fontFamily = AmiriBoldFont,



textAlign = TextAlign.Center



)



Spacer(modifier = Modifier.height(4.dp))



Text(



text = activeDhikr.getName(selectedLanguage),



color = if (isNightMode) Color.White else Color(0xFF1F2937),



fontSize = 16.sp,



fontWeight = FontWeight.Bold,



textAlign = TextAlign.Center



)



val rawMeaning = activeDhikr.getMeaning(selectedLanguage)



val cleanMeaning = rawMeaning.trim()



.removeSurrounding("\"")



.removeSurrounding("“", "”")



.removeSurrounding("'", "'")



if (cleanMeaning.isNotBlank()) {



Spacer(modifier = Modifier.height(2.dp))



Text(



text = cleanMeaning,



color = Color(0xFF8C7A5B),



fontSize = 12.sp,



fontStyle = FontStyle.Italic,



fontWeight = FontWeight.Normal,



textAlign = TextAlign.Center



)



}



Spacer(modifier = Modifier.height(7.dp))



Row(



modifier = Modifier



.onGloballyPositioned { coords ->



onTargetRowBounds(coords.boundsInRoot())



},



verticalAlignment = Alignment.CenterVertically,



horizontalArrangement = Arrangement.spacedBy(10.dp)



) {



// Target pill (Soft mint tint background, green text)



Box(



modifier = Modifier



.clip(RoundedCornerShape(14.dp))



.background(Color(0xFFF0F5F2))



.border(1.2.dp, Color(0xFFD8E6DE), RoundedCornerShape(14.dp))



.padding(horizontal = 16.dp, vertical = 6.dp)



) {



Text(



text = "Target: $targetCount",



color = Color(0xFF0E5D47),



fontSize = 12.5.sp,



fontWeight = FontWeight.SemiBold



)



}



// Change pill (Dark green background matching original user screenshot)



Box(



modifier = Modifier



.clip(RoundedCornerShape(14.dp))



.background(Color(0xFF0E5D47))



.border(1.2.dp, Color(0xFF0E5D47), RoundedCornerShape(14.dp))



.clickable { onOpenSetTarget() }



.padding(horizontal = 16.dp, vertical = 6.dp)



) {



Text(



text = if (selectedLanguage.lowercase().startsWith("hi")) "बदलें ✎" else "Change ✎",



color = Color.White,



fontSize = 12.5.sp,



fontWeight = FontWeight.SemiBold



)



}



}



}



}



}



if (!isBeadsViewMode) {

            // ── Dial Mode: Left Floating Bead Button + Central Dial Counter ──

            item {

                Box(

                    modifier = Modifier

                        .fillMaxWidth()

                        .padding(vertical = 2.dp),

                    contentAlignment = Alignment.Center

                ) {

                    CircularCounterDial(

                        sessionCount = sessionCount,

                        targetCount = targetCount,

                        scale = 1f,

                        isNightMode = isNightMode,

                        onDialBounds = onDialBounds,

                        onTap = onCountTap

                    )



                    // Floating Tasbih Bead Button on Left at 10 o'clock position

                    Box(

                        modifier = Modifier

                            .align(Alignment.TopStart)

                            .zIndex(10f)

                            .padding(start = 12.dp, top = 14.dp)

                            .size(38.dp)

                            .shadow(3.dp, CircleShape)

                            .clip(CircleShape)

                            .background(Color.White)

                            .border(1.2.dp, Color(0x33C9A84C), CircleShape)

                            .clickable { onToggleBeadsMode() },

                        contentAlignment = Alignment.Center

                    ) {

                        val tasbihIcon = rememberAssetPainter("media/d919e45c30b26c4a.webp")

                        Image(

                            painter = tasbihIcon,

                            contentDescription = "Switch to Beads View",

                            contentScale = ContentScale.Fit,

                            modifier = Modifier.size(22.dp)

                        )

                    }

                }

            }



            // Stats Row: Today's Total & All Time (Dial Mode)

            item {

                Row(

                    modifier = Modifier

                        .fillMaxWidth()

                        .padding(vertical = 2.dp),

                    horizontalArrangement = Arrangement.SpaceEvenly,

                    verticalAlignment = Alignment.CenterVertically

                ) {

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        Text(

                            text = "$todayCount",

                            fontSize = 22.sp,

                            fontWeight = FontWeight.Bold,

                            color = if (isNightMode) Color(0xFFE8F5E9) else Color(0xFF104434),

                            fontFamily = FontFamily.Serif

                        )

                        Text(

                            text = strings.todayTotal,

                            fontSize = 11.sp,

                            fontWeight = FontWeight.Medium,

                            color = Color(0xFF6B7280)

                        )

                    }



                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        Text(

                            text = "$allTimeCount",

                            fontSize = 22.sp,

                            fontWeight = FontWeight.Bold,

                            color = if (isNightMode) Color(0xFFE8F5E9) else Color(0xFF104434),

                            fontFamily = FontFamily.Serif

                        )

                        Text(

                            text = strings.allTime,

                            fontSize = 11.sp,

                            fontWeight = FontWeight.Medium,

                            color = Color(0xFF6B7280)

                        )

                    }

                }

            }



            // Action Controls: [ ↩ Undo ] [ ↺ Reset ] [ Save ] (Dial Mode)

            item {

                Row(

                    modifier = Modifier

                        .fillMaxWidth()

                        .padding(top = 2.dp),

                    horizontalArrangement = Arrangement.spacedBy(10.dp)

                ) {

                    // 1. Undo

                    Box(

                        modifier = Modifier

                            .weight(1f)

                            .height(40.dp)

                            .clip(RoundedCornerShape(14.dp))

                            .background(Color.White)

                            .border(1.dp, Color(0x44A07828), RoundedCornerShape(14.dp))

                            .clickable { onUndoTap() },

                        contentAlignment = Alignment.Center

                    ) {

                        Text(

                            text = "↩ ${strings.undo}",

                            fontWeight = FontWeight.Bold,

                            fontSize = 12.5.sp,

                            color = Color(0xFF1C2826)

                        )

                    }



                    // 2. Reset

                    Box(

                        modifier = Modifier

                            .weight(1f)

                            .height(40.dp)

                            .clip(RoundedCornerShape(14.dp))

                            .background(Color.White)

                            .border(1.dp, Color(0x66C81D3F), RoundedCornerShape(14.dp))

                            .clickable { onResetTap() },

                        contentAlignment = Alignment.Center

                    ) {

                        Text(

                            text = "↺ ${strings.reset}",

                            fontWeight = FontWeight.Bold,

                            fontSize = 12.5.sp,

                            color = Color(0xFFC81D3F)

                        )

                    }



                    // 3. Save

                    Box(

                        modifier = Modifier

                            .weight(1f)

                            .height(40.dp)

                            .clip(RoundedCornerShape(14.dp))

                            .background(Color.White)

                            .border(1.dp, Color(0x66104434), RoundedCornerShape(14.dp))

                            .clickable { onSaveTap() },

                        contentAlignment = Alignment.Center

                    ) {

                        Text(

                            text = strings.save,

                            fontWeight = FontWeight.Bold,

                            fontSize = 12.5.sp,

                            color = Color(0xFF104434)

                        )

                    }

                }

            }

        } else {

            // ── Beads Mode: EXACT Image 3 Architecture ──

            item {

                Box(

                    modifier = Modifier

                        .fillMaxWidth()

                        .padding(vertical = 2.dp),

                    contentAlignment = Alignment.Center

                ) {

                    // Authentic Bézier Beads Canvas (transparent background, NO box/border)

                    RealisticBeadsCanvas(

                        skin = activeSkin,

                        sessionCount = sessionCount,

                        targetCount = targetCount,

                        isNightMode = isNightMode,

                        onToggleBeadsMode = onToggleBeadsMode,

                        onOpenMarbleGallery = onOpenMarbleGallery,

                        onBeadAdvancement = onCountTap

                    )



                    // Floating Dial Switch Button on Left at 10 o'clock position (Exact Image 3 Green circle with ⭕)

                    Box(

                        modifier = Modifier

                            .align(Alignment.TopStart)

                            .zIndex(10f)

                            .padding(start = 12.dp, top = 8.dp)

                            .size(38.dp)

                            .shadow(3.dp, CircleShape)

                            .clip(CircleShape)

                            .background(Color(0xFF0E5D47))

                            .border(1.2.dp, Color(0xFFC9A84C), CircleShape)

                            .clickable { onToggleBeadsMode() },

                        contentAlignment = Alignment.Center

                    ) {

                        Text(

                            text = "⭕",

                            fontSize = 16.sp

                        )

                    }

                }

            }



            // Central Session Count Number directly below bead arc (Exact Image 3)

            item {

                Box(

                    modifier = Modifier

                        .fillMaxWidth()

                        .padding(top = 2.dp),

                    contentAlignment = Alignment.Center

                ) {

                    Text(

                        text = "$sessionCount",

                        fontSize = 24.sp,

                        fontWeight = FontWeight.Bold,

                        color = if (isNightMode) Color(0xFFF3D48A) else Color(0xFFA07828),

                        fontFamily = FontFamily.Serif

                    )

                }

            }



            // ⚪ Colours & Marbles Pill Button (Exact Image 3)

            item {

                Box(

                    modifier = Modifier

                        .fillMaxWidth()

                        .padding(top = 8.dp, bottom = 6.dp),

                    contentAlignment = Alignment.Center

                ) {

                    val baseCol = remember(activeSkin.baseColor) {

                        try { Color(android.graphics.Color.parseColor(activeSkin.baseColor)) } catch (_: Exception) { Color(0xFF825F2D) }

                    }

                    val highlightCol = remember(activeSkin.highlightColor) {

                        try { Color(android.graphics.Color.parseColor(activeSkin.highlightColor)) } catch (_: Exception) { Color(0xFFF3D48A) }

                    }

                    val shadowCol = remember(activeSkin.shadowColor) {

                        try { Color(android.graphics.Color.parseColor(activeSkin.shadowColor)) } catch (_: Exception) { Color(0xFF4A3519) }

                    }



                    Box(

                        modifier = Modifier

                            .clip(RoundedCornerShape(999.dp))

                            .background(if (isNightMode) Color(0xFF112D22) else Color(0xFFF8F5EE))

                            .border(1.5.dp, Color(0xFFC9A84C).copy(alpha = 0.5f), RoundedCornerShape(999.dp))

                            .clickable { onOpenMarbleGallery() }

                            .padding(horizontal = 18.dp, vertical = 8.dp)

                    ) {

                        Row(

                            verticalAlignment = Alignment.CenterVertically,

                            horizontalArrangement = Arrangement.spacedBy(10.dp)

                        ) {

                            // Circular 3D bead preview

                            Box(

                                modifier = Modifier

                                    .size(24.dp)

                                    .clip(CircleShape)

                                    .background(

                                        Brush.radialGradient(

                                            colors = listOf(highlightCol, baseCol, shadowCol),

                                            center = Offset(10f, 10f)

                                        )

                                    )

                                    .border(1.dp, Color(0x66C9A84C), CircleShape)

                            )

                            Text(

                                text = "Colours & Marbles",

                                fontSize = 13.5.sp,

                                fontWeight = FontWeight.SemiBold,

                                color = if (isNightMode) Color(0xFFEDEDED) else Color(0xFF1C2826)

                            )

                        }

                    }

                }

            }



            // Action Controls: [ ↩ Undo ] [ ↺ Reset ] [ Save ] (Right below Colours & Marbles, Exact Image 3)

            item {

                Row(

                    modifier = Modifier

                        .fillMaxWidth()

                        .padding(top = 2.dp),

                    horizontalArrangement = Arrangement.spacedBy(10.dp)

                ) {

                    // 1. Undo

                    Box(

                        modifier = Modifier

                            .weight(1f)

                            .height(40.dp)

                            .clip(RoundedCornerShape(14.dp))

                            .background(Color.White)

                            .border(1.dp, Color(0x44A07828), RoundedCornerShape(14.dp))

                            .clickable { onUndoTap() },

                        contentAlignment = Alignment.Center

                    ) {

                        Text(

                            text = "↩ ${strings.undo}",

                            fontWeight = FontWeight.Bold,

                            fontSize = 12.5.sp,

                            color = Color(0xFF1C2826)

                        )

                    }



                    // 2. Reset

                    Box(

                        modifier = Modifier

                            .weight(1f)

                            .height(40.dp)

                            .clip(RoundedCornerShape(14.dp))

                            .background(Color.White)

                            .border(1.dp, Color(0x66C81D3F), RoundedCornerShape(14.dp))

                            .clickable { onResetTap() },

                        contentAlignment = Alignment.Center

                    ) {

                        Text(

                            text = "↺ ${strings.reset}",

                            fontWeight = FontWeight.Bold,

                            fontSize = 12.5.sp,

                            color = Color(0xFFC81D3F)

                        )

                    }



                    // 3. Save

                    Box(

                        modifier = Modifier

                            .weight(1f)

                            .height(40.dp)

                            .clip(RoundedCornerShape(14.dp))

                            .background(Color.White)

                            .border(1.dp, Color(0x66104434), RoundedCornerShape(14.dp))

                            .clickable { onSaveTap() },

                        contentAlignment = Alignment.Center

                    ) {

                        Text(

                            text = strings.save,

                            fontWeight = FontWeight.Bold,

                            fontSize = 12.5.sp,

                            color = Color(0xFF104434)

                        )

                    }

                }

            }



            // Stats Row: Today's Total & All Time (Below Action Controls in Beads Mode)

            item {

                Row(

                    modifier = Modifier

                        .fillMaxWidth()

                        .padding(top = 10.dp, bottom = 4.dp),

                    horizontalArrangement = Arrangement.SpaceEvenly,

                    verticalAlignment = Alignment.CenterVertically

                ) {

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        Text(

                            text = "$todayCount",

                            fontSize = 22.sp,

                            fontWeight = FontWeight.Bold,

                            color = if (isNightMode) Color(0xFFE8F5E9) else Color(0xFF104434),

                            fontFamily = FontFamily.Serif

                        )

                        Text(

                            text = strings.todayTotal,

                            fontSize = 11.sp,

                            fontWeight = FontWeight.Medium,

                            color = Color(0xFF6B7280)

                        )

                    }



                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        Text(

                            text = "$allTimeCount",

                            fontSize = 22.sp,

                            fontWeight = FontWeight.Bold,

                            color = if (isNightMode) Color(0xFFE8F5E9) else Color(0xFF104434),

                            fontFamily = FontFamily.Serif

                        )

                        Text(

                            text = strings.allTime,

                            fontSize = 11.sp,

                            fontWeight = FontWeight.Medium,

                            color = Color(0xFF6B7280)

                        )

                    }

                }

            }

        }

    }

}



// ══════════════════════════════════════════════════════════════════════



@Composable



fun CircularCounterDial(



sessionCount: Int,



targetCount: Int,



scale: Float = 1f,



isNightMode: Boolean,



onDialBounds: (Rect) -> Unit = {},



onTap: () -> Unit



) {



val currentOnTap by rememberUpdatedState(onTap)



val progress = if (targetCount > 0) (sessionCount % targetCount) / targetCount.toFloat() else 0f



val animatedProgress by animateFloatAsState(



targetValue = progress,



animationSpec = spring(stiffness = Spring.StiffnessMediumLow),



label = "progress"



)



var isPressed by remember { mutableStateOf(false) }



val pressScale by animateFloatAsState(



targetValue = if (isPressed) 0.92f else scale,



animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMedium),



label = "pressScale"



)



val glowAlpha by animateFloatAsState(



targetValue = if (isPressed) 0.35f else 0f,



animationSpec = androidx.compose.animation.core.tween(durationMillis = 150),



label = "glowAlpha"



)



val dialSize = 195.dp



val goldColor = Color(0xFFC9A84C)



val emeraldColor = Color(0xFF0E5D47)



Box(



modifier = Modifier



.size(dialSize)



.scale(pressScale)



.onGloballyPositioned { coords ->



onDialBounds(coords.boundsInRoot())



}



.pointerInput(currentOnTap) {



awaitEachGesture {



val down = awaitFirstDown(requireUnconsumed = false)



currentOnTap()



isPressed = true



waitForUpOrCancellation()



isPressed = false



}



},



contentAlignment = Alignment.Center



) {



// Outer animated glow aura on tap/press



if (glowAlpha > 0f) {



Box(



modifier = Modifier



.size(dialSize + 14.dp)



.clip(CircleShape)



.background(emeraldColor.copy(alpha = glowAlpha))



)



}



Canvas(modifier = Modifier.fillMaxSize()) {



val strokeWidth = 9.dp.toPx()



val diameter = size.minDimension - strokeWidth - 2.dp.toPx()



val topLeft = Offset((size.width - diameter) / 2, (size.height - diameter) / 2)



val arcSize = Size(diameter, diameter)



// Outer Base Ring with gradient from deep emerald to golden olive (Exact Image Reference)



drawArc(



brush = Brush.verticalGradient(



colors = listOf(



Color(0xFF0F4736), // Deep Authentic Emerald



Color(0xFF1B6B54), // Mid emerald



Color(0xFF8FA355)  // Golden Olive



)



),



startAngle = -90f,



sweepAngle = 360f,



useCenter = false,



topLeft = topLeft,



size = arcSize,



style = Stroke(width = strokeWidth)



)



// Progress Accent Arc



if (animatedProgress > 0f) {



drawArc(



brush = Brush.verticalGradient(



colors = listOf(



Color(0xFF0E5D47),



Color(0xFF28A745),



Color(0xFFC9A84C)



)



),



startAngle = -90f,



sweepAngle = animatedProgress * 360f,



useCenter = false,



topLeft = topLeft,



size = arcSize,



style = Stroke(width = strokeWidth + 1.dp.toPx(), cap = StrokeCap.Round)



)



}



}



// Inner Dial Background - Pure White (Exact Reference Image)



Box(



modifier = Modifier



.size(dialSize - 18.dp)



.shadow(5.dp, CircleShape, spotColor = Color(0x260E5D47))



.clip(CircleShape)



.background(if (isNightMode) Color(0xFF16251E) else Color.White),



contentAlignment = Alignment.Center



) {



Column(



horizontalAlignment = Alignment.CenterHorizontally,



verticalArrangement = Arrangement.Center



) {



Text(



text = "$sessionCount",



color = if (isNightMode) Color.White else Color(0xFF0E5D47),



fontSize = if (sessionCount >= 1000) 38.sp else 46.sp,



fontWeight = FontWeight.Bold,



fontFamily = FontFamily.Serif,



lineHeight = 48.sp



)



Spacer(modifier = Modifier.height(2.dp))



Text(



text = "Tap to Count",



color = Color(0xFF8C7A5B),



fontSize = 11.5.sp,



fontWeight = FontWeight.Bold,



letterSpacing = 0.5.sp



)



}



}



}



}



// ══════════════════════════════════════════════════════════════════════



// REALISTIC 3D BEADS CANVAS (Vertical Strand with Marble Skins)



// ══════════════════════════════════════════════════════════════════════



// AUTHENTIC MARBLE BEADS BÉZIER VIEW (index.html:126530-128550)



// 11 slots (3 arrived + 4 gap slots + 4 waiting) along quadratic Bézier



// easeOutBack settling physics & 3D spherical shader with active gold aura



// ══════════════════════════════════════════════════════════════════════



@Composable
fun RealisticBeadsCanvas(
    skin: BeadMarbleSkin,
    sessionCount: Int,
    targetCount: Int,
    modifier: Modifier = Modifier,
    isNightMode: Boolean = false,
    onToggleBeadsMode: () -> Unit = {},
    onOpenMarbleGallery: () -> Unit = {},
    onBeadAdvancement: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()
    val progress = remember { Animatable(0f) }
    var animJob by remember { mutableStateOf<kotlinx.coroutines.Job?>(null) }
    var dragAccumulator by remember { mutableFloatStateOf(0f) }

    fun naturalTasbihSlide(t: Float): Float {
        if (t <= 0.85f) {
            val p = t / 0.85f
            return 0.95f * (p * p * (3f - 2f * p))
        } else {
            val p = (t - 0.85f) / 0.15f
            val overshoot = (1f - p) * kotlin.math.sin(p * Math.PI.toFloat())
            return 0.95f + 0.05f * (1f + 0.25f * overshoot)
        }
    }

    fun triggerAdvance() {
        onBeadAdvancement()
        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        animJob?.cancel()
        animJob = coroutineScope.launch {
            progress.snapTo(0f)
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 440, easing = LinearEasing)
            )
            progress.snapTo(0f)
        }
    }

    val baseCol = remember(skin.baseColor) {
        try { Color(android.graphics.Color.parseColor(skin.baseColor)) } catch (_: Exception) { Color(0xFF825F2D) }
    }
    val highlightCol = remember(skin.highlightColor) {
        try { Color(android.graphics.Color.parseColor(skin.highlightColor)) } catch (_: Exception) { Color(0xFFF3D48A) }
    }
    val shadowCol = remember(skin.shadowColor) {
        try { Color(android.graphics.Color.parseColor(skin.shadowColor)) } catch (_: Exception) { Color(0xFF4A3519) }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(185.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        dragAccumulator += (dragAmount.x + dragAmount.y)
                        if (kotlin.math.abs(dragAccumulator) > 30f) {
                            triggerAdvance()
                            dragAccumulator = 0f
                        }
                    },
                    onDragEnd = { dragAccumulator = 0f }
                )
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                triggerAdvance()
            },
        contentAlignment = Alignment.Center
    ) {
        // Authentic Bézier 11-slot Beads Canvas on transparent background (Exact Image 3)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Quadratic Bézier curve points (P0 start bottom-left, P1 control, P2 end top-right)
            val p0 = Offset(w * 0.20f, h * 0.84f)
            val p1 = Offset(w * 0.52f, -h * 0.18f)
            val p2 = Offset(w * 0.92f, h * 0.22f)

            fun bezierPoint(t: Float): Offset {
                val mt = 1f - t
                val x = mt * mt * p0.x + 2f * mt * t * p1.x + t * t * p2.x
                val y = mt * mt * p0.y + 2f * mt * t * p1.y + t * t * p2.y
                return Offset(x, y)
            }

            // 1. Double-Pass Silk Cord (Shadow line + Golden/wood silk line)
            val threadShadowPath = Path().apply {
                val startPt = bezierPoint(-0.08f)
                moveTo(startPt.x, startPt.y + 1f)
                val csteps = 48
                for (i in -4..csteps + 4) {
                    val pt = bezierPoint(i / csteps.toFloat())
                    lineTo(pt.x, pt.y + 1f)
                }
            }
            drawPath(
                path = threadShadowPath,
                color = Color.Black.copy(alpha = 0.16f),
                style = Stroke(width = 2.2.dp.toPx(), cap = StrokeCap.Round)
            )

            val threadPath = Path().apply {
                val startPt = bezierPoint(-0.08f)
                moveTo(startPt.x, startPt.y)
                val csteps = 48
                for (i in -4..csteps + 4) {
                    val pt = bezierPoint(i / csteps.toFloat())
                    lineTo(pt.x, pt.y)
                }
            }
            drawPath(
                path = threadPath,
                color = if (isNightMode) Color(0xFFC9A84C).copy(alpha = 0.75f) else Color(0xFF825F2D).copy(alpha = 0.75f),
                style = Stroke(width = 1.6.dp.toPx(), cap = StrokeCap.Round)
            )

            // Arc length parameterization
            val steps = 200
            val table = ArrayList<Pair<Float, Float>>(steps + 1)
            table.add(Pair(0f, 0f))
            var prevPt = p0
            var totalLen = 0f
            for (i in 1..steps) {
                val t = i / steps.toFloat()
                val pt = bezierPoint(t)
                val dist = kotlin.math.hypot(pt.x - prevPt.x, pt.y - prevPt.y)
                totalLen += dist
                table.add(Pair(t, totalLen))
                prevPt = pt
            }

            fun tForLen(target: Float): Float {
                if (target <= 0f) return 0f
                if (target >= totalLen) return 1f
                for (i in 1 until table.size) {
                    if (table[i].second >= target) {
                        val a = table[i - 1]
                        val b = table[i]
                        val frac = (target - a.second) / ((b.second - a.second).coerceAtLeast(0.001f))
                        return a.first + (b.first - a.first) * frac
                    }
                }
                return 1f
            }

            // Exact cluster spacing matching original index.html:
            // Left cluster: slots 0, 1, 2 (strictly 3 arrived beads)
            // Right cluster: slots 7, 8, 9, 10 (strictly 4 upcoming beads)
            val spacing = 29.dp.toPx()
            val startL = totalLen * 0.04f
            val l0 = startL
            val l1 = startL + spacing
            val l2 = startL + 2f * spacing

            val endR = totalLen * 0.96f
            val l10 = endR
            val l9 = endR - spacing
            val l8 = endR - 2f * spacing
            val l7 = endR - 3f * spacing

            val midGap = l7 - l2
            val l3 = l2 + midGap * 0.20f
            val l4 = l2 + midGap * 0.40f
            val l5 = l2 + midGap * 0.65f
            val l6 = l2 + midGap * 0.85f

            val lens = floatArrayOf(l0, l1, l2, l3, l4, l5, l6, l7, l8, l9, l10)
            val slotT = FloatArray(11) { i -> tForLen(lens[i]) }

            val showSlots = intArrayOf(0, 1, 2, 7, 8, 9, 10)
            val baseBeadRadius = 14.dp.toPx()

            fun drawSingleBead(x: Float, y: Float, isActive: Boolean, alpha: Float) {
                if (alpha <= 0.01f) return
                val r = baseBeadRadius

                // Soft subtle glow for active bead (natural fade matching original web, no hard ring)
                if (isActive) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFC9A84C).copy(alpha = 0.28f * alpha),
                                Color(0xFFC9A84C).copy(alpha = 0.08f * alpha),
                                Color.Transparent
                            ),
                            center = Offset(x, y),
                            radius = r * 1.55f
                        ),
                        radius = r * 1.55f,
                        center = Offset(x, y)
                    )
                }

                // Drop shadow
                drawCircle(
                    color = Color.Black.copy(alpha = (if (isNightMode) 0.45f else 0.22f) * alpha),
                    radius = r * 1.05f,
                    center = Offset(x + r * 0.12f, y + r * 0.25f)
                )

                // 3D Spherical body
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            highlightCol.copy(alpha = alpha),
                            baseCol.copy(alpha = alpha),
                            shadowCol.copy(alpha = alpha)
                        ),
                        center = Offset(x - r * 0.35f, y - r * 0.35f),
                        radius = r * 1.25f
                    ),
                    radius = r,
                    center = Offset(x, y)
                )

                // Specular highlight dot (crisp white)
                drawCircle(
                    color = Color.White.copy(alpha = 0.65f * alpha),
                    radius = r * 0.22f,
                    center = Offset(x - r * 0.32f, y - r * 0.34f)
                )
            }

            fun drawSlot(slotIdx: Int, alpha: Float, isActive: Boolean) {
                val t = slotT[slotIdx]
                val pt = bezierPoint(t)
                drawSingleBead(pt.x, pt.y, isActive, alpha)
            }

            fun drawAtT(t: Float, alpha: Float, isActive: Boolean) {
                val pt = bezierPoint(t)
                drawSingleBead(pt.x, pt.y, isActive, alpha)
            }

            val pVal = progress.value.coerceIn(0f, 1f)

            if (pVal <= 0.001f || pVal >= 0.999f) {
                // Settled state: strictly 3 beads on left (slot 2 active nearest to gap) + 4 on right
                showSlots.forEachIndexed { k, slotIdx ->
                    val isActive = (k == 2)
                    drawSlot(slotIdx, 1f, isActive)
                }
            } else {
                // In-flight animation: smooth visible natural physical slide across the thread
                val ease = naturalTasbihSlide(pVal)

                // 6 beads slide forward:
                // k = 1: slot 1 -> slot 0 (slides down)
                // k = 2: slot 2 -> slot 1 (slides down)
                // k = 3: slot 7 -> slot 2 (slides across the thread from right to left! Glowing active bead!)
                // k = 4: slot 8 -> slot 7
                // k = 5: slot 9 -> slot 8
                // k = 6: slot 10 -> slot 9
                for (k in 1..6) {
                    val fromSlot = showSlots[k]
                    val toSlot = if (k == 3) showSlots[2] else (fromSlot - 1)
                    val fromT = slotT[fromSlot]
                    val toT = slotT[toSlot]
                    val t = fromT + (toT - fromT) * ease
                    val isActive = (k == 3) // Glowing as it visibly slides across the thread into slot 2!
                    drawAtT(t, 1f, isActive)
                }

                // Oldest arrived bead on left: slides downwards past slot 0 and fades out
                val fromT0 = slotT[showSlots[0]]
                val beyondT = fromT0 - (slotT[showSlots[1]] - fromT0)
                val t0 = fromT0 + (beyondT - fromT0) * ease
                drawAtT(t0, (1f - ease).coerceIn(0f, 1f), false)

                // New incoming bead arrives on right tail (slot 10) and fades in
                drawSlot(showSlots[6], ease.coerceIn(0f, 1f), false)
            }
        }
    }
}


// 6. TAB 2: MY DHIKR HISTORY VIEW (Exact Original App & Screenshots)



// ══════════════════════════════════════════════════════════════════════



@Composable



private fun TasbihHistoryView(



activeTab: Int = 1,



isNightMode: Boolean,



selectedLanguage: String,



strings: TasbihStrings,



grandTotal: Int,



allDhikrs: List<TasbihDhikrItem>,



onContinueDhikr: (DhikrHistoryItem) -> Unit



) {



val context = LocalContext.current



var historyList by remember(activeTab) { mutableStateOf(TasbihRepository.getHistoryList()) }



var showClearDialog by remember { mutableStateOf(false) }



// Colors matching original .h-card & .tot-card



val cardBg = if (isNightMode) Color(0xFF0D271E) else Color.White



val cardBorder = if (isNightMode) Color(0xFFC9A84C).copy(alpha = 0.4f) else Color(0xFFC9A84C).copy(alpha = 0.35f)



val textPrimary = if (isNightMode) Color(0xFFEDEDED) else Color(0xFF1A1408)



val textMuted = if (isNightMode) Color(0xFFA89870) else Color(0xFF7A6840)



val goldColor = Color(0xFFC9A84C)



val emeraldColor = if (isNightMode) Color(0xFF6EE7B7) else Color(0xFF0E5D47)



val coinBg = if (isNightMode) Color(0xFF162E24) else Color(0xFFF8F5EE)



// Clear confirmation dialog



if (showClearDialog) {



AlertDialog(



onDismissRequest = { showClearDialog = false },



title = {



Text(



text = strings.clearAll,



fontWeight = FontWeight.Bold,



color = if (isNightMode) Color.White else Color(0xFF1C2826)



)



},



text = {



Text(



text = if (selectedLanguage == "hi") "क्या आप अपना सारा ज़िक्र इतिहास मिटाना चाहते हैं?"



else if (selectedLanguage == "ur") "کیا آپ اپنا تمام ذکر تاریخچہ صاف کرنا چاہتے ہیں؟"



else if (selectedLanguage == "ar") "هل تريد مسح سجل الأذكار بالكامل؟"



else if (selectedLanguage == "bn") "আপনি কি আপনার সমস্ত জিকির ইতিহাস মুছে ফেলতে চান?"



else "Do you want to clear your entire Dhikr history?",



color = if (isNightMode) Color(0xFFCBD5E1) else Color(0xFF4B5563)



)



},



confirmButton = {



TextButton(



onClick = {



TasbihRepository.clearHistory()



historyList = emptyList()



showClearDialog = false



Toast.makeText(



context,



if (selectedLanguage == "hi") "इतिहास मिटा दिया गया" else "History cleared",



Toast.LENGTH_SHORT



).show()



}



) {



Text(



text = strings.clearAll,



color = Color(0xFFC0504F),



fontWeight = FontWeight.Bold



)



}



},



dismissButton = {



TextButton(onClick = { showClearDialog = false }) {



Text(



text = if (selectedLanguage == "hi") "रद्द करें" else "Cancel",



color = if (isNightMode) Color(0xFF94A3B8) else Color(0xFF6B7280)



)



}



},



containerColor = if (isNightMode) Color(0xFF13221B) else Color(0xFFFAF8F5)



)



}



// Grouping by dhikrId, summing cumulative count, preserving latest date, sorting descending by count



val groupedItems = remember(historyList) {



val map = linkedMapOf<String, DhikrHistoryItem>()



for (item in historyList) {



val existing = map[item.dhikrId]



if (existing == null) {



map[item.dhikrId] = item



} else {



map[item.dhikrId] = existing.copy(



count = existing.count + item.count,



dateDisplay = item.dateDisplay,



timestamp = maxOf(existing.timestamp, item.timestamp)



)



}



}



map.values.sortedByDescending { it.count }



}



val displayGrandTotal = remember(groupedItems, grandTotal) {



val sum = groupedItems.sumOf { it.count }



if (sum > 0) sum else grandTotal



}



LazyColumn(



modifier = Modifier



.fillMaxSize()



.padding(horizontal = 14.dp),



verticalArrangement = Arrangement.spacedBy(12.dp),



contentPadding = PaddingValues(top = 4.dp, bottom = 100.dp)



) {



// ── 1. HEADER ROW (मेरा ज़िक्र इतिहास + सब मिटाएँ) ──



item {



Row(



modifier = Modifier



.fillMaxWidth()



.padding(top = 10.dp, bottom = 4.dp),



verticalAlignment = Alignment.CenterVertically,



horizontalArrangement = Arrangement.SpaceBetween



) {



// Title with Gold Underline Bar (.hist-title)



Column {



Text(



text = strings.myDhikrHistory,



color = if (isNightMode) Color(0xFFE2E8F0) else Color(0xFF0E5D47),



fontSize = 17.sp,



fontWeight = FontWeight.ExtraBold,



fontFamily = FontFamily.Serif,



letterSpacing = 0.5.sp



)



Spacer(modifier = Modifier.height(5.dp))



Box(



modifier = Modifier



.width(46.dp)



.height(2.5.dp)



.clip(RoundedCornerShape(2.dp))



.background(



Brush.horizontalGradient(



listOf(Color(0xFFC9A84C), Color(0xFFF3D48A))



)



)



)



}



// Clear All Pill Button (.clr-all)



if (historyList.isNotEmpty()) {



Box(



modifier = Modifier



.clip(RoundedCornerShape(50.dp))



.background(if (isNightMode) Color(0xFF1F2420) else Color(0xFFF8F5EE))



.border(1.5.dp, Color(0xFFC9A84C).copy(alpha = 0.4f), RoundedCornerShape(50.dp))



.clickable { showClearDialog = true }



.padding(horizontal = 14.dp, vertical = 7.dp),



contentAlignment = Alignment.Center



) {



Row(



verticalAlignment = Alignment.CenterVertically,



horizontalArrangement = Arrangement.spacedBy(5.dp)



) {



Text(



text = "🗑",



fontSize = 13.sp



)



Text(



text = strings.clearAll,



color = Color(0xFFC0504F),



fontSize = 12.sp,



fontWeight = FontWeight.Bold



)



}



}



}



}



}



// ── 2. HERO TOTAL CARD (.tot-card) ──



item {



Box(



modifier = Modifier



.fillMaxWidth()



.clip(RoundedCornerShape(22.dp))



.background(



Brush.linearGradient(



colors = if (isNightMode) listOf(Color(0xFF134233), Color(0xFF07221A))



else listOf(Color(0xFF0E5D47), Color(0xFF083D32))



)



)



.border(2.dp, Color(0xFFC9A84C), RoundedCornerShape(22.dp))



.padding(horizontal = 22.dp, vertical = 20.dp)



) {



Row(



modifier = Modifier.fillMaxWidth(),



verticalAlignment = Alignment.CenterVertically,



horizontalArrangement = Arrangement.SpaceBetween



) {



Column {



Text(



text = strings.totalDhikr,



color = Color(0xFFF3D48A),



fontSize = 13.sp,



fontWeight = FontWeight.Bold,



letterSpacing = 0.3.sp



)



Spacer(modifier = Modifier.height(4.dp))



Text(



text = "$displayGrandTotal",



color = Color.White,



fontSize = 36.sp,



fontWeight = FontWeight.ExtraBold,



fontFamily = FontFamily.Serif



)



}



Text(



text = "🏅",



fontSize = 34.sp



)



}



}



}



// ── 3. HISTORY CARDS LIST (.h-card) ──



if (groupedItems.isEmpty()) {



item {



Box(



modifier = Modifier



.fillMaxWidth()



.padding(top = 50.dp, bottom = 40.dp),



contentAlignment = Alignment.Center



) {



Column(



horizontalAlignment = Alignment.CenterHorizontally,



verticalArrangement = Arrangement.spacedBy(8.dp)



) {



Text(text = "📿", fontSize = 48.sp)



Text(



text = if (selectedLanguage == "hi") "अभी तक कोई ज़िक्र इतिहास नहीं है"



else if (selectedLanguage == "ur") "ابھی تک کوئی ذکر نہیں"



else if (selectedLanguage == "ar") "لا يوجد ذكر بعد"



else if (selectedLanguage == "bn") "এখনও কোনো জিকির ইতিহাস নেই"



else "No Dhikr History Yet",



color = textPrimary,



fontWeight = FontWeight.Bold,



fontSize = 15.sp,



textAlign = TextAlign.Center



)



Text(



text = if (selectedLanguage == "hi") "यहाँ देखने के लिए एक तस्बीह सत्र शुरू करें।"



else if (selectedLanguage == "ur") "اپنا پہلا ذکر شروع کریں"



else if (selectedLanguage == "ar") "ابدأ بأول ذكر لك"



else if (selectedLanguage == "bn") "এখানে দেখতে একটি তাসবিহ সেশন শুরু করুন।"



else "Start a Tasbih session to see it here.",



color = textMuted,



fontSize = 12.5.sp,



textAlign = TextAlign.Center



)



}



}



}



} else {



items(groupedItems) { item ->



// Dynamically resolve localized title, Arabic, and icon



val dhikrObj = allDhikrs.find { it.id == item.dhikrId }



val asmaObj = if (dhikrObj == null && (item.dhikrId.startsWith("name_") || item.dhikrId == "allah" || item.dhikrId == "rahman")) {



TasbihRepository.getAsmaUlHusna().find { asma -> 



asma.transliteration.equals(item.dhikrId, ignoreCase = true) || 



"name_${asma.number}" == item.dhikrId ||



asma.transliteration.replace("-", "").equals(item.dhikrId.replace("-", ""), ignoreCase = true)



}



} else null



val displayTitle = dhikrObj?.getName(selectedLanguage)?.takeIf { it.isNotBlank() } 



?: asmaObj?.transliteration 



?: item.dhikrName



val displayArabic = dhikrObj?.arabic?.takeIf { it.isNotBlank() } 



?: asmaObj?.arabic 



?: item.arabic



val displayIcon = dhikrObj?.icon?.takeIf { it.isNotBlank() } ?: item.icon



Box(



modifier = Modifier



.fillMaxWidth()



.shadow(



elevation = 2.dp,



shape = RoundedCornerShape(20.dp),



ambientColor = Color(0x140E5D47),



spotColor = Color(0x140E5D47)



)



.clip(RoundedCornerShape(20.dp))



.background(cardBg)



.border(1.5.dp, cardBorder, RoundedCornerShape(20.dp))



.clickable { onContinueDhikr(item) }



.padding(horizontal = 16.dp, vertical = 14.dp)



) {



Row(



modifier = Modifier.fillMaxWidth(),



verticalAlignment = Alignment.CenterVertically



) {



// Left Coin (.h-ico)



Box(



modifier = Modifier



.size(44.dp)



.clip(CircleShape)



.background(coinBg)



.border(1.5.dp, Color(0xFFC9A84C).copy(alpha = 0.5f), CircleShape),



contentAlignment = Alignment.Center



) {



Text(



text = displayIcon,



fontSize = 20.sp



)



}



Spacer(modifier = Modifier.width(12.dp))



// Middle Info (.h-info)



Column(



modifier = Modifier



.weight(1f)



.padding(end = 6.dp)



) {



Text(



text = displayTitle,



color = textPrimary,



fontSize = 15.sp,



fontWeight = FontWeight.ExtraBold,



maxLines = 1,



overflow = TextOverflow.Ellipsis



)



Spacer(modifier = Modifier.height(4.dp))



Text(



text = item.dateDisplay,



color = textMuted,



fontSize = 11.5.sp,



maxLines = 1,



overflow = TextOverflow.Ellipsis



)



}



// Right Column (.h-ar, .h-cnt, .h-cnt-lbl, continue button)



Column(



horizontalAlignment = Alignment.End,



verticalArrangement = Arrangement.spacedBy(2.dp)



) {



// Arabic (.h-ar)



Text(



text = displayArabic,



color = emeraldColor,



fontSize = 19.sp,



fontWeight = FontWeight.Bold,



fontFamily = AmiriBoldFont,



textAlign = TextAlign.End,



maxLines = 1,



overflow = TextOverflow.Ellipsis



)



// Count (.h-cnt)



Text(



text = "${item.count}",



color = if (isNightMode) Color(0xFFF3D48A) else Color(0xFFC59828),



fontSize = 28.sp,



fontWeight = FontWeight.ExtraBold,



fontFamily = FontFamily.Serif,



textAlign = TextAlign.End



)



// Count Label (.h-cnt-lbl: "बार" / "times")



Text(



text = strings.times,



color = textMuted,



fontSize = 11.5.sp,



fontWeight = FontWeight.SemiBold,



textAlign = TextAlign.End



)



Spacer(modifier = Modifier.height(4.dp))



// Action Button: [ ▶ जारी रखें ]



Box(



modifier = Modifier



.shadow(elevation = 1.dp, shape = RoundedCornerShape(50.dp))



.clip(RoundedCornerShape(50.dp))



.background(



Brush.linearGradient(



listOf(Color(0xFF0E5D47), Color(0xFF083D32))



)



)



.border(1.dp, Color(0xFFC9A84C), RoundedCornerShape(50.dp))



.clickable { onContinueDhikr(item) }



.padding(horizontal = 13.dp, vertical = 5.dp),



contentAlignment = Alignment.Center



) {



Text(



text = "▶ " + strings.continueBtn,



color = Color.White,



fontSize = 10.5.sp,



fontWeight = FontWeight.Bold



)



}



}



}



}



}



}



}



}



// ══════════════════════════════════════════════════════════════════════



// 7. TAB 3: ISLAMIC DHIKR GUIDE (Exact Screenshots 1, 2, 3, 4)



// ══════════════════════════════════════════════════════════════════════



@Composable



private fun TasbihGuideView(



isNightMode: Boolean,



strings: TasbihStrings,



selectedLanguage: String,



onOpenHadithRef: (DhikrGuideDetail) -> Unit,



onUseDhikrInCounter: (DhikrGuideDetail) -> Unit



) {



val goldColor = Color(0xFFC9A84C)



val guideItems = remember(selectedLanguage) {



TasbihRepository.getGuideList(selectedLanguage)



}



val isRtl = selectedLanguage == "ur" || selectedLanguage == "ar"



LazyColumn(



modifier = Modifier



.fillMaxSize()



.padding(horizontal = 14.dp),



verticalArrangement = Arrangement.spacedBy(16.dp),



contentPadding = PaddingValues(top = 10.dp, bottom = 32.dp)



) {



// Golden Serif Section Title Header (Exact Screenshot)



item {



Box(



modifier = Modifier



.fillMaxWidth()



.padding(top = 4.dp, bottom = 2.dp),



contentAlignment = Alignment.Center



) {



Text(



text = strings.guideTitle.uppercase(),



color = goldColor,



fontSize = 15.sp,



fontWeight = FontWeight.ExtraBold,



fontFamily = FontFamily.Serif,



letterSpacing = 2.sp,



textAlign = TextAlign.Center



)



}



}



items(guideItems, key = { it.id }) { guide ->



// Card Colors exactly matching Image 2



val cardBgColor = if (isNightMode) Color(0xFF13271F) else Color(0xFFFFFFFF)



val cardBorderColor = if (isNightMode) goldColor.copy(alpha = 0.35f) else Color(0xFFDFC594)



val arabicColor = if (isNightMode) Color(0xFF8AE2B8) else Color(0xFF0C4A38)



val titleColor = if (isNightMode) Color(0xFF4ADE80) else Color(0xFF0E5D47)



val meaningColor = if (isNightMode) Color(0xFFA7B9AC) else Color(0xFF5D4D38)



// Top-right Count Pill



val pillBg = if (isNightMode) Color(0xFF22362C) else Color(0xFFFFF7E8)



val pillBorder = if (isNightMode) goldColor.copy(alpha = 0.6f) else Color(0xFFDFC48F)



val pillTextColor = if (isNightMode) Color(0xFFF0D590) else Color(0xFF8C6416)



// Block 1: How & When to Recite Box



val howBg = if (isNightMode) Color(0xFF162D24) else Color(0xFFEEF5F1)



val howBorder = if (isNightMode) Color(0xFF2E5343) else Color(0xFFD2E4DA)



val howHeaderColor = if (isNightMode) Color(0xFF4ADE80) else Color(0xFF0E5D47)



val howTextColor = if (isNightMode) Color(0xFFD1E0D7) else Color(0xFF2C3A33)



// Block 2: Interactive Hadith Reference Pill



val refBg = if (isNightMode) Color(0xFF183327) else Color(0xFFEEF7F4)



val refBorder = if (isNightMode) Color(0xFF0E5D47).copy(alpha = 0.35f) else Color(0xFFC5E5DD)



val refTextColor = if (isNightMode) Color(0xFFE0E0E0) else Color(0xFF0E5D47)



val refBtnBg = if (isNightMode) Color(0xFF0E5D47).copy(alpha = 0.4f) else Color(0xFFCDEAE0)



val refBtnBorder = if (isNightMode) Color(0xFF0E5D47).copy(alpha = 0.25f) else Color(0xFF9DCFBF)



val refBtnTextColor = if (isNightMode) Color(0xFF80CBC4) else Color(0xFF0A523E)



// Block 3: Virtues & Benefits Box



val benBg = if (isNightMode) Color(0xFF1A2B23) else Color(0xFFFAF7F0)



val benBorder = if (isNightMode) goldColor.copy(alpha = 0.4f) else Color(0xFFEFE8DA)



val benTextColor = if (isNightMode) Color(0xFFD6D1C4) else Color(0xFF383024)



// Block 4: CTA Action Button



val ctaBg = Color(0xFF0E4D3A)



val ctaBorder = goldColor



val ctaTextColor = Color.White



Box(



modifier = Modifier



.fillMaxWidth()



.shadow(



elevation = if (isNightMode) 2.dp else 4.dp,



shape = RoundedCornerShape(26.dp),



ambientColor = Color(0x100E5D47),



spotColor = Color(0x18C9A84C)



)



.clip(RoundedCornerShape(26.dp))



.background(cardBgColor)



.border(1.2.dp, cardBorderColor, RoundedCornerShape(26.dp))



.drawBehind {



// Left 4.5dp vertical accent gradient strip (clipped by clip(RoundedCornerShape(26.dp)))



drawRect(



brush = Brush.verticalGradient(



listOf(



Color(0xFF0E5D47),



Color(0xFFC9A84C),



Color(0xFF083D32)



)



),



topLeft = Offset.Zero,



size = Size(4.5.dp.toPx(), size.height)



)



}



) {



// Main Card Content Column



Column(



modifier = Modifier



.fillMaxWidth()



.padding(start = 22.dp, end = 18.dp, top = 20.dp, bottom = 22.dp),



horizontalAlignment = Alignment.CenterHorizontally,



verticalArrangement = Arrangement.spacedBy(14.dp)



) {



// Top Area: Centered Arabic Calligraphy with Absolute Top-Right Count Badge



Box(



modifier = Modifier.fillMaxWidth()



) {



// Count Badge (Pill at top right)



Box(



modifier = Modifier



.align(Alignment.TopEnd)



.clip(RoundedCornerShape(999.dp))



.background(pillBg)



.border(



1.dp,



pillBorder,



RoundedCornerShape(999.dp)



)



.padding(horizontal = 14.dp, vertical = 5.dp)



) {



Text(



text = guide.targetStr,



color = pillTextColor,



fontSize = 12.sp,



fontWeight = FontWeight.ExtraBold



)



}



// Arabic Calligraphy centered (padded horizontally so it doesn't collide with the pill)



Text(



text = guide.arabic,



color = arabicColor,



fontSize = 27.sp,



fontWeight = FontWeight.Bold,



fontFamily = AmiriQuranFont,



lineHeight = 44.sp,



textAlign = TextAlign.Center,



modifier = Modifier



.fillMaxWidth()



.padding(top = 16.dp, start = 8.dp, end = 8.dp)



)



}



// Localized Title / Transliteration (.sg-lo)



val title = guide.getTitle(selectedLanguage)



if (title.isNotBlank()) {



Text(



text = title,



color = titleColor,



fontSize = 16.5.sp,



fontWeight = FontWeight.ExtraBold,



textAlign = TextAlign.Center,



lineHeight = 22.sp,



modifier = Modifier.fillMaxWidth()



)



}



// Localized Meaning / Translation (.sg-me)



val meaning = guide.getMeaning(selectedLanguage)



if (meaning.isNotBlank()) {



Text(



text = meaning,



color = meaningColor,



fontSize = 13.5.sp,



fontStyle = FontStyle.Italic,



textAlign = TextAlign.Center,



lineHeight = 20.sp,



modifier = Modifier.fillMaxWidth()



)



}



// 1. "How & When to Recite" Box (.sg-how)



val howText = guide.getHowTo(selectedLanguage)



if (howText.isNotBlank()) {



Box(



modifier = Modifier



.fillMaxWidth()



.clip(RoundedCornerShape(14.dp))



.background(howBg)



.border(



1.2.dp,



howBorder,



RoundedCornerShape(14.dp)



)



.padding(horizontal = 16.dp, vertical = 13.dp)



) {



Column(



modifier = Modifier.fillMaxWidth(),



verticalArrangement = Arrangement.spacedBy(4.dp)



) {



Text(



text = strings.howToReciteLabel,



fontSize = 12.5.sp,



fontWeight = FontWeight.ExtraBold,



color = howHeaderColor,



textAlign = if (isRtl) TextAlign.Right else TextAlign.Left,



modifier = Modifier.fillMaxWidth()



)



Text(



text = howText,



fontSize = 13.sp,



color = howTextColor,



lineHeight = 19.sp,



textAlign = if (isRtl) TextAlign.Right else TextAlign.Left,



modifier = Modifier.fillMaxWidth()



)



}



}



}



// 2. Interactive Hadith Reference Pill (.sg-ref)



val hadithRefText = guide.hadithRef.ifBlank { guide.hadithBook }.ifBlank { "Authentic Hadith" }



Box(



modifier = Modifier



.fillMaxWidth()



.clip(RoundedCornerShape(999.dp))



.background(refBg)



.border(



1.2.dp,



refBorder,



RoundedCornerShape(999.dp)



)



.clickable { onOpenHadithRef(guide) }



.padding(start = 14.dp, end = 8.dp, top = 6.dp, bottom = 6.dp)



) {



Row(



modifier = Modifier.fillMaxWidth(),



verticalAlignment = Alignment.CenterVertically,



horizontalArrangement = Arrangement.SpaceBetween



) {



Row(



verticalAlignment = Alignment.CenterVertically,



horizontalArrangement = Arrangement.spacedBy(6.dp),



modifier = Modifier.weight(1f, fill = false)



) {



Text(text = "📖 📜", fontSize = 12.sp)



Text(



text = hadithRefText,



fontSize = 12.5.sp,



fontWeight = FontWeight.Bold,



color = refTextColor,



maxLines = 1,



overflow = TextOverflow.Ellipsis



)



}



// Read Hadith ↗ Pill Button



Box(



modifier = Modifier



.clip(RoundedCornerShape(8.dp))



.background(refBtnBg)



.border(



1.dp,



refBtnBorder,



RoundedCornerShape(8.dp)



)



.padding(horizontal = 12.dp, vertical = 6.dp)



) {



Text(



text = strings.readHadith,



fontSize = 11.5.sp,



fontWeight = FontWeight.ExtraBold,



color = refBtnTextColor



)



}



}



}



// 3. Virtues and Benefits Card (.sg-ben)



val virtuesText = guide.getVirtues(selectedLanguage)



if (virtuesText.isNotBlank()) {



Box(



modifier = Modifier



.fillMaxWidth()



.clip(RoundedCornerShape(14.dp))



.background(benBg)



.border(



1.2.dp,



benBorder,



RoundedCornerShape(14.dp)



)



.drawBehind {



// Gold 4dp left border accent



drawRect(



color = Color(0xFFC9A84C),



topLeft = Offset.Zero,



size = Size(4.dp.toPx(), size.height)



)



}



.padding(start = 16.dp, end = 14.dp, top = 12.dp, bottom = 12.dp)



) {



Text(



text = buildAnnotatedString {



withStyle(



SpanStyle(



fontWeight = FontWeight.Bold,



color = if (isNightMode) Color(0xFFE0C070) else Color(0xFF946815)



)



) {



append("✨ ${strings.virtuesBenefitsLabel}: ")



}



withStyle(



SpanStyle(



fontWeight = FontWeight.Normal,



color = benTextColor



)



) {



append(virtuesText)



}



},



fontSize = 13.sp,



lineHeight = 19.sp,



textAlign = if (isRtl) TextAlign.Right else TextAlign.Left,



modifier = Modifier.fillMaxWidth()



)



}



}



// 4. Action Button (.sg-use: e.g. "यह ज़िक्र करें → →" / "Use This → →" / "← ← یہ ذکر کریں")



Box(



modifier = Modifier



.align(if (isRtl) Alignment.End else Alignment.Start)



.shadow(elevation = 4.dp, shape = RoundedCornerShape(999.dp))



.clip(RoundedCornerShape(999.dp))



.background(ctaBg)



.border(1.5.dp, ctaBorder, RoundedCornerShape(999.dp))



.clickable { onUseDhikrInCounter(guide) }



.padding(horizontal = 26.dp, vertical = 12.dp)



) {



Text(



text = strings.useThis,



color = ctaTextColor,



fontSize = 14.5.sp,



fontWeight = FontWeight.ExtraBold



)



}



}



}



}



}



}



// ══════════════════════════════════════════════════════════════════════



// 8. TAB 4: 99 NAMES OF ALLAH (Exact Screenshots 1 & 2 Parity)



// ══════════════════════════════════════════════════════════════════════



private data class PetalParticle(



val emoji: String,



val xFraction: Float,



val initialYFraction: Float,



val sizeSp: Float,



val swayDurationMs: Int,



val swayAmplitudeDp: Float,



val baseAlpha: Float



)



// 12 Authentic flowers & leaves matching original app (index.html:382431)



// Staggered vertically so they are distributed across the entire screen immediately on open & scroll



private val PETAL_PRESETS = listOf(



PetalParticle("🌸", 0.06f, 0.04f, 15f, 3200, 18f, 0.70f),



PetalParticle("🍃", 0.14f, 0.20f, 14f, 3800, 22f, 0.60f),



PetalParticle("🌺", 0.22f, 0.36f, 17f, 3400, 16f, 0.75f),



PetalParticle("🌼", 0.30f, 0.52f, 15f, 2900, 20f, 0.65f),



PetalParticle("🌷", 0.38f, 0.68f, 16f, 3600, 18f, 0.70f),



PetalParticle("🌿", 0.46f, 0.84f, 14f, 4000, 24f, 0.55f),



PetalParticle("💮", 0.54f, 0.12f, 17f, 3100, 17f, 0.72f),



PetalParticle("🌸", 0.62f, 0.28f, 15f, 3300, 19f, 0.68f),



PetalParticle("🍂", 0.70f, 0.44f, 14f, 3900, 21f, 0.60f),



PetalParticle("🌺", 0.78f, 0.60f, 16f, 3500, 16f, 0.75f),



PetalParticle("🍃", 0.86f, 0.76f, 14f, 3700, 23f, 0.58f),



PetalParticle("🌼", 0.94f, 0.92f, 15f, 3000, 18f, 0.65f)



)



@Composable



private fun FallingPetalsOverlay(modifier: Modifier = Modifier) {



BoxWithConstraints(modifier = modifier.fillMaxSize()) {



val widthPx = constraints.maxWidth.toFloat()



val heightPx = constraints.maxHeight.toFloat()



if (widthPx > 50f && heightPx > 50f) {



val masterTransition = rememberInfiniteTransition(label = "petalsMasterFall")



val fallProgress by masterTransition.animateFloat(



initialValue = 0f,



targetValue = 1f,



animationSpec = infiniteRepeatable(



animation = tween(11000, easing = LinearEasing),



repeatMode = RepeatMode.Restart



),



label = "masterFallProgress"



)



PETAL_PRESETS.forEachIndexed { idx, particle ->



val swayTransition = rememberInfiniteTransition(label = "petalSway_${idx}")



val swayProgress by swayTransition.animateFloat(



initialValue = -1f,



targetValue = 1f,



animationSpec = infiniteRepeatable(



animation = tween(particle.swayDurationMs, easing = FastOutSlowInEasing),



repeatMode = RepeatMode.Reverse



),



label = "sway"



)



val currentYFraction = (fallProgress + particle.initialYFraction) % 1f



val totalDistance = heightPx + 100f



val currentAlpha = when {



currentYFraction < 0.08f -> (currentYFraction / 0.08f) * particle.baseAlpha



currentYFraction > 0.88f -> ((1f - currentYFraction) / 0.12f) * particle.baseAlpha



else -> particle.baseAlpha



}



Text(



text = particle.emoji,



fontSize = particle.sizeSp.sp,



modifier = Modifier.graphicsLayer {



translationY = (currentYFraction * totalDistance) - 50f



val swayPx = particle.swayAmplitudeDp * density



translationX = (particle.xFraction * widthPx) + (swayProgress * swayPx)



rotationZ = swayProgress * 25f



alpha = currentAlpha



}



)



}



}



}



}



@Composable



private fun NamesLanternIcon(isNightMode: Boolean) {



if (isNightMode) {



val glowTransition = rememberInfiniteTransition(label = "lanternGlow")



val glowPulse by glowTransition.animateFloat(



initialValue = 0.70f,



targetValue = 1.0f,



animationSpec = infiniteRepeatable(



animation = tween(1200, easing = FastOutSlowInEasing),



repeatMode = RepeatMode.Reverse



),



label = "pulse"



)



Box(



modifier = Modifier.size(26.dp),



contentAlignment = Alignment.Center



) {



// Glowing golden-amber flame aura (#FFD700 and #FF8C00)



Canvas(modifier = Modifier.size(34.dp)) {



// Outer warm amber flame glow (#FF8C00)



drawCircle(



brush = Brush.radialGradient(



colors = listOf(



Color(0xFFFFD700).copy(alpha = 0.60f * glowPulse),



Color(0xFFFF8C00).copy(alpha = 0.38f * glowPulse),



Color.Transparent



)



),



radius = size.minDimension / 1.4f,



center = center



)



// Inner bright golden flame core (#FFF59D)



drawCircle(



brush = Brush.radialGradient(



colors = listOf(



Color(0xFFFFF9C4).copy(alpha = 0.85f * glowPulse),



Color(0xFFFFC107).copy(alpha = 0.50f * glowPulse),



Color.Transparent



)



),



radius = size.minDimension / 2.6f,



center = center



)



}



Text(



text = "🏮",



fontSize = 17.sp,



modifier = Modifier.graphicsLayer {



scaleX = 1f + (glowPulse - 0.70f) * 0.12f



scaleY = 1f + (glowPulse - 0.70f) * 0.12f



}



)



}



} else {



Box(



modifier = Modifier.size(24.dp),



contentAlignment = Alignment.Center



) {



Text(



text = "🏮",



fontSize = 16.sp



)



}



}



}



@Composable



fun TasbihNamesOfAllahView(



isNightMode: Boolean,



strings: TasbihStrings,



selectedLanguage: String,



activeTab: Int = 3,



onBack: () -> Unit = {},



onSelectNameForRecital: (AsmaNameItem) -> Unit



) {



val asmaList = remember { TasbihRepository.getAsmaUlHusna() }



val benefitInfo = remember(selectedLanguage) {



NAMES_BENEFIT[selectedLanguage] ?: NAMES_BENEFIT["hi"] ?: NAMES_BENEFIT["en"]!!



}



val goldColor = Color(0xFFC9A84C)



val emeraldDark = if (isNightMode) Color(0xFF38D9A9) else Color(0xFF0E5D47)



val arabicColor = if (isNightMode) Color(0xFFF7E7CE) else Color(0xFF083D32)



val textMeaningColor = if (isNightMode) Color(0xFFDCD5C4) else Color(0xFF5A4830)



val cardBg = if (isNightMode) {



Brush.verticalGradient(listOf(Color(0xFF16251E), Color(0xFF0F1B16)))



} else {



Brush.verticalGradient(listOf(Color(0xFFFFFFFF), Color(0xFFFAF6EE)))



}



val benCardBg = if (isNightMode) {



Brush.verticalGradient(listOf(Color(0xFF13221C), Color(0xFF0E1A15)))



} else {



Brush.verticalGradient(listOf(Color(0xFFFFFFFF), Color(0xFFFAF7F0)))



}



Box(modifier = Modifier.fillMaxSize()) {



LazyColumn(



modifier = Modifier



.fillMaxSize()



.padding(horizontal = 14.dp),



verticalArrangement = Arrangement.spacedBy(12.dp),



contentPadding = PaddingValues(top = 10.dp, bottom = 28.dp)



) {



// 1. TOP HEADER (Exact 🏮 99 NAMES OF ALLAH 🏮)



item {



Column(



modifier = Modifier



.fillMaxWidth()



.padding(top = 4.dp, bottom = 8.dp),



horizontalAlignment = Alignment.CenterHorizontally



) {



Row(



verticalAlignment = Alignment.CenterVertically,



horizontalArrangement = Arrangement.Center



) {



NamesLanternIcon(isNightMode = isNightMode)



Spacer(modifier = Modifier.width(6.dp))



Text(



text = "99 NAMES OF ALLAH",



fontFamily = FontFamily.Serif,



fontSize = 16.5.sp,



fontWeight = FontWeight.ExtraBold,



color = emeraldDark,



letterSpacing = 0.8.sp



)



Spacer(modifier = Modifier.width(6.dp))



NamesLanternIcon(isNightMode = isNightMode)



}



Spacer(modifier = Modifier.height(4.dp))



// Gold Hairline Divider Under Header



Box(



modifier = Modifier



.width(44.dp)



.height(2.dp)



.background(



Brush.horizontalGradient(



listOf(Color.Transparent, goldColor, Color.Transparent)



)



)



)



Spacer(modifier = Modifier.height(4.dp))



// Arabic Calligraphy: أَسْمَاءُ اللَّهِ الْحُسْنَى



Text(



text = "أَسْمَاءُ اللَّهِ الْحُسْنَى",



fontFamily = AmiriQuranFont,



fontSize = 24.sp,



fontWeight = FontWeight.Bold,



color = if (isNightMode) Color(0xFFE8DCB8) else Color(0xFF4B3621),



textAlign = TextAlign.Center



)



}



}



// 2. BENEFITS CARD (Exact Screenshot 1: 📿 Benefits of Reciting the 99 Names)



item {



Box(



modifier = Modifier



.fillMaxWidth()



.shadow(elevation = 3.dp, shape = RoundedCornerShape(20.dp))



.clip(RoundedCornerShape(20.dp))



.background(benCardBg)



.border(1.5.dp, goldColor.copy(alpha = 0.38f), RoundedCornerShape(20.dp))



) {



// Top Accent Bar (3.5dp)



Box(



modifier = Modifier



.fillMaxWidth()



.height(3.5.dp)



.background(



Brush.horizontalGradient(



listOf(Color(0xFF0E5D47), goldColor, Color(0xFF0E5D47))



)



)



)



Column(



modifier = Modifier



.fillMaxWidth()



.padding(horizontal = 16.dp, vertical = 14.dp)



) {



// Title



Text(



text = benefitInfo.title,



fontSize = 14.sp,



fontWeight = FontWeight.ExtraBold,



color = emeraldDark



)



Spacer(modifier = Modifier.height(8.dp))



// 4 Diamond Bullets (✦)



benefitInfo.items.forEach { bulletText ->



Row(



modifier = Modifier



.fillMaxWidth()



.padding(vertical = 3.dp),



verticalAlignment = Alignment.Top



) {



Text(



text = "✦ ",



fontSize = 12.sp,



fontWeight = FontWeight.Bold,



color = if (isNightMode) Color(0xFFC9A84C) else Color(0xFF4B3621)



)



Text(



text = bulletText,



fontSize = 12.5.sp,



lineHeight = 18.sp,



color = if (isNightMode) Color(0xFFDCD5C4) else Color(0xFF4B3621)



)



}



}



Spacer(modifier = Modifier.height(8.dp))



// Hadith Reference Pill Badge



Box(



modifier = Modifier



.clip(RoundedCornerShape(8.dp))



.background(Color(0xFF0E5D47).copy(alpha = if (isNightMode) 0.25f else 0.08f))



.border(1.dp, Color(0xFF0E5D47).copy(alpha = 0.22f), RoundedCornerShape(8.dp))



.padding(horizontal = 12.dp, vertical = 5.dp)



) {



Text(



text = "📖 ${benefitInfo.reference}",



fontSize = 11.5.sp,



fontWeight = FontWeight.Bold,



color = emeraldDark



)



}



}



}



}



// 3. 2-COLUMN GRID CARDS (Exact Screenshot 1 & 2)



items(asmaList.chunked(2)) { pair ->



Row(



modifier = Modifier.fillMaxWidth(),



horizontalArrangement = Arrangement.spacedBy(10.dp)



) {



pair.forEach { asmaItem ->



val nameCnt = TasbihRepository.getAllTimeCount("name_${asmaItem.number}")



Box(



modifier = Modifier



.weight(1f)



.shadow(elevation = 2.dp, shape = RoundedCornerShape(20.dp))



.clip(RoundedCornerShape(20.dp))



.background(cardBg)



.border(1.5.dp, goldColor.copy(alpha = 0.45f), RoundedCornerShape(20.dp))



.clickable { onSelectNameForRecital(asmaItem) }



) {



// Top Accent Sheen Bar (3.5dp)



Box(



modifier = Modifier



.fillMaxWidth()



.height(3.5.dp)



.background(



Brush.horizontalGradient(



listOf(



Color.Transparent,



goldColor.copy(alpha = 0.7f),



Color(0xFF0E5D47),



goldColor.copy(alpha = 0.7f),



Color.Transparent



)



)



)



)



// Card Content



Column(



modifier = Modifier



.fillMaxWidth()



.padding(horizontal = 10.dp, vertical = 12.dp),



horizontalAlignment = Alignment.CenterHorizontally



) {



// Top Row: Left Number Circle + Right Count Pill (if count > 0)



Row(



modifier = Modifier.fillMaxWidth(),



verticalAlignment = Alignment.CenterVertically,



horizontalArrangement = Arrangement.SpaceBetween



) {



// Number Circle ( 1 )



Box(



modifier = Modifier



.size(26.dp)



.clip(CircleShape)



.background(



Brush.linearGradient(



listOf(goldColor.copy(alpha = 0.2f), Color(0xFF0E5D47).copy(alpha = 0.1f))



)



)



.border(1.2.dp, goldColor.copy(alpha = 0.55f), CircleShape),



contentAlignment = Alignment.Center



) {



Text(



text = "${asmaItem.number}",



fontFamily = FontFamily.Serif,



fontSize = 11.5.sp,



fontWeight = FontWeight.ExtraBold,



color = if (isNightMode) goldColor else Color(0xFF0E5D47)



)



}



// Count Badge Pill (Visible only if count > 0!)



if (nameCnt > 0) {



Box(



modifier = Modifier



.defaultMinSize(minWidth = 22.dp, minHeight = 22.dp)



.clip(RoundedCornerShape(20.dp))



.background(



Brush.linearGradient(



listOf(Color(0xFF0E5D47), Color(0xFF083D32))



)



)



.border(1.dp, goldColor.copy(alpha = 0.4f), RoundedCornerShape(20.dp))



.padding(horizontal = 6.dp, vertical = 2.dp),



contentAlignment = Alignment.Center



) {



Text(



text = if (nameCnt >= 1000) "${nameCnt / 1000}k" else "$nameCnt",



color = Color.White,



fontSize = 10.sp,



fontWeight = FontWeight.ExtraBold



)



}



} else {



Spacer(modifier = Modifier.size(1.dp))



}



}



Spacer(modifier = Modifier.height(2.dp))



// Arabic Calligraphy (AmiriQuranFont, 26sp)



Text(



text = asmaItem.arabic,



fontFamily = AmiriQuranFont,



fontSize = 26.sp,



fontWeight = FontWeight.Bold,



color = arabicColor,



textAlign = TextAlign.Center,



modifier = Modifier.fillMaxWidth()



)



Spacer(modifier = Modifier.height(2.dp))



// Transliteration (Serif, 14sp, Deep Emerald)



Text(



text = asmaItem.transliteration,



fontFamily = FontFamily.Serif,



fontSize = 14.sp,



fontWeight = FontWeight.Bold,



color = emeraldDark,



textAlign = TextAlign.Center,



modifier = Modifier.fillMaxWidth()



)



Spacer(modifier = Modifier.height(3.dp))



// Meaning (Warm Brown, 11.5sp)



Text(



text = asmaItem.getMeaning(selectedLanguage),



fontSize = 11.5.sp,



fontWeight = FontWeight.Medium,



color = textMeaningColor,



textAlign = TextAlign.Center,



maxLines = 2,



overflow = TextOverflow.Ellipsis,



modifier = Modifier.fillMaxWidth()



)



}



}



}



if (pair.size == 1) {



Spacer(modifier = Modifier.weight(1f))



}



}



}



}



// 4. CONTINUOUS FALLING PETALS OVERLAY (Floats gracefully over the entire screen continuously)



FallingPetalsOverlay()



}



}



// ══════════════════════════════════════════════════════════════════════



// 9. TAB 5: CURRENT STREAKS & ACHIEVEMENTS (Exact Screenshot 12 & 13)



// ══════════════════════════════════════════════════════════════════════



@Composable



private fun TasbihStreaksView(



isNightMode: Boolean,



strings: TasbihStrings



) {



val cardBg = if (isNightMode) Color(0xFF16251E) else Color(0xFFFAF8F5)



val textPrimary = if (isNightMode) Color(0xFFEDEDED) else Color(0xFF1C2826)



val textSecondary = if (isNightMode) Color(0xFF9EABA2) else Color(0xFF6B7280)



val goldColor = Color(0xFFC9A84C)



val emeraldColor = Color(0xFF1B4D3E)



val streakDays: Int = remember { TasbihRepository.getStreakDays() }



val longestStreak: Int = remember { TasbihRepository.getLongestStreak() }



val achievements = remember {



listOf(



MedallionAchievement("m1", "First Step", "Complete your first tasbih session", "🌱", 1, 1, "habits", true),



MedallionAchievement("m2", "Tasbih Century", "Recite 100 times in a single day", "✨", 100, 100, "volume", true),



MedallionAchievement("m3", "Fajr Remembrance", "Recite dhikr between dawn and sunrise", "🌅", 1, 1, "habits", true),



MedallionAchievement("m4", "Evening Peace", "Recite dhikr after Asr/Maghrib prayer", "🌇", 1, 1, "habits", true),



MedallionAchievement("m5", "Midnight Noor", "Recite dhikr in the final third of night", "🌌", 1, 1, "habits", true),



MedallionAchievement("m6", "Consistent Believer", "Complete dhikr 3 days in a row", "📿", streakDays, 3, "streaks", streakDays >= 3),



MedallionAchievement("m7", "7-Day Champion", "Maintain daily dhikr for a full week", "⭐", streakDays, 7, "streaks", streakDays >= 7),



MedallionAchievement("m8", "Devoted Believer", "Maintain 30-day continuous dhikr streak", "👑", streakDays, 30, "streaks", streakDays >= 30),



MedallionAchievement("m9", "Millennial Devotion", "Reach 1,000 total all-time dhikrs", "💎", 294, 1000, "volume", false),



MedallionAchievement("m10", "Salawat Devotee", "Recite 500 Salawat upon the Prophet ﷺ", "🌹", 140, 500, "volume", false),



MedallionAchievement("m11", "99 Names Seeker", "Recite all 99 Names of Allah at least once", "📖", 14, 99, "habits", false),



MedallionAchievement("m12", "Shield Bearer", "Protect your streak for 5 consecutive days", "🛡️", 2, 5, "streaks", false)



)



}



LazyColumn(



modifier = Modifier



.fillMaxSize()



.padding(horizontal = 14.dp),



verticalArrangement = Arrangement.spacedBy(12.dp),



contentPadding = PaddingValues(top = 10.dp, bottom = 24.dp)



) {



// Streak Card



item {



Box(



modifier = Modifier



.fillMaxWidth()



.clip(RoundedCornerShape(22.dp))



.background(



Brush.radialGradient(



colors = if (isNightMode) listOf(Color(0xFF1D3B2F), Color(0xFF10211A))



else listOf(Color(0xFF1A493B), Color(0xFF113228))



)



)



.border(1.5.dp, goldColor.copy(alpha = 0.7f), RoundedCornerShape(22.dp))



.padding(18.dp)



) {



Column(



modifier = Modifier.fillMaxWidth(),



horizontalAlignment = Alignment.CenterHorizontally,



verticalArrangement = Arrangement.spacedBy(12.dp)



) {



// Top Badges: Shield + Record



Row(



modifier = Modifier.fillMaxWidth(),



horizontalArrangement = Arrangement.SpaceBetween



) {



Box(



modifier = Modifier



.clip(RoundedCornerShape(10.dp))



.background(Color.Black.copy(alpha = 0.3f))



.border(1.dp, goldColor.copy(alpha = 0.6f), RoundedCornerShape(10.dp))



.padding(horizontal = 10.dp, vertical = 4.dp)



) {



Text(text = "🛡️ 1 Shield", color = Color(0xFFFFF7D6), fontSize = 11.sp, fontWeight = FontWeight.Bold)



}



Box(



modifier = Modifier



.clip(RoundedCornerShape(10.dp))



.background(Color.Black.copy(alpha = 0.3f))



.border(1.dp, goldColor.copy(alpha = 0.6f), RoundedCornerShape(10.dp))



.padding(horizontal = 10.dp, vertical = 4.dp)



) {



Text(text = "🏆 Record: ${longestStreak}d", color = goldColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)



}



}



// Main Big Counter



Column(horizontalAlignment = Alignment.CenterHorizontally) {



Text(



text = "$streakDays Days",



color = Color(0xFFFFF7D6),



fontSize = 44.sp,



fontWeight = FontWeight.ExtraBold,



fontFamily = FontFamily.Serif



)



Text(



text = "CURRENT STREAK",



color = goldColor,



fontSize = 11.sp,



fontWeight = FontWeight.ExtraBold,



letterSpacing = 1.2.sp



)



}



// Week Circles (M T W T F S S)



Row(



modifier = Modifier.fillMaxWidth(),



horizontalArrangement = Arrangement.SpaceEvenly



) {



listOf("M", "T", "W", "T", "F", "S", "S").forEachIndexed { idx, day ->



val isDone = idx < streakDays



Box(



modifier = Modifier



.size(32.dp)



.clip(CircleShape)



.background(if (isDone) goldColor else Color.White.copy(alpha = 0.15f))



.border(1.dp, if (isDone) Color.White else goldColor.copy(alpha = 0.4f), CircleShape),



contentAlignment = Alignment.Center



) {



Text(



text = day,



color = if (isDone) emeraldColor else Color.White,



fontSize = 12.sp,



fontWeight = FontWeight.Bold



)



}



}



}



Text(



text = "“Consistency in remembrance brings peace to the heart.”",



color = Color(0xFFE2EFE7),



fontSize = 11.sp,



textAlign = TextAlign.Center



)



}



}



}



// Medallions & Achievements Header



item {



val unlockedCount = achievements.count { it.isUnlocked }



Column {



Text(



text = "MEDALLIONS & ACHIEVEMENTS",



color = goldColor,



fontSize = 12.sp,



fontWeight = FontWeight.ExtraBold,



letterSpacing = 1.sp



)



Text(



text = "$unlockedCount / ${achievements.size} Unlocked",



color = textSecondary,



fontSize = 12.sp



)



}



}



// Medallions List



items(achievements) { ach ->



Box(



modifier = Modifier



.fillMaxWidth()



.clip(RoundedCornerShape(16.dp))



.background(cardBg)



.border(



1.dp,



if (ach.isUnlocked) goldColor else goldColor.copy(alpha = 0.25f),



RoundedCornerShape(16.dp)



)



.padding(14.dp)



) {



Row(



modifier = Modifier.fillMaxWidth(),



verticalAlignment = Alignment.CenterVertically,



horizontalArrangement = Arrangement.spacedBy(12.dp)



) {



// Badge Icon



Box(



modifier = Modifier



.size(44.dp)



.clip(CircleShape)



.background(if (ach.isUnlocked) goldColor.copy(alpha = 0.2f) else Color.Gray.copy(alpha = 0.15f))



.border(1.dp, if (ach.isUnlocked) goldColor else Color.Gray.copy(alpha = 0.3f), CircleShape),



contentAlignment = Alignment.Center



) {



Text(text = ach.icon, fontSize = 22.sp)



}



Column(



modifier = Modifier.weight(1f),



verticalArrangement = Arrangement.spacedBy(2.dp)



) {



Text(



text = ach.title,



color = textPrimary,



fontSize = 14.sp,



fontWeight = FontWeight.Bold



)



Text(



text = ach.description,



color = textSecondary,



fontSize = 11.sp



)



// Progress bar if locked



if (!ach.isUnlocked) {



Spacer(modifier = Modifier.height(4.dp))



LinearProgressIndicator(



progress = { (ach.currentProgress.toFloat() / ach.maxProgress).coerceIn(0f, 1f) },



modifier = Modifier



.fillMaxWidth()



.height(5.dp)



.clip(RoundedCornerShape(2.5.dp)),



color = goldColor,



trackColor = goldColor.copy(alpha = 0.2f),



)



Text(



text = "${ach.currentProgress} / ${ach.maxProgress}",



color = goldColor,



fontSize = 9.sp,



fontWeight = FontWeight.SemiBold



)



}



}



// Status



if (ach.isUnlocked) {



Text(text = "✓", color = goldColor, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)



} else {



Text(text = "🔒", fontSize = 14.sp)



}



}



}



}



}



}



// ══════════════════════════════════════════════════════════════════════



// 10. AUTHENTIC HADITH REFERENCE MODAL (Exact Screenshot 8)



// ══════════════════════════════════════════════════════════════════════



@Composable



fun AuthenticHadithReferenceModal(



guide: DhikrGuideDetail,



selectedLanguage: String = "hi",



strings: TasbihStrings,



onDismiss: () -> Unit



) {



val context = LocalContext.current



val isRtl = selectedLanguage == "ur" || selectedLanguage == "ar"



val hadithBook = guide.hadithBook.ifBlank { guide.hadithRef }.ifBlank { "Authentic Hadith" }



val hadithGrade = guide.hadithGrade.ifBlank { "Sahih" }



val hadithNarrator = guide.narrator.ifBlank { guide.hadithNarrator }



val arMatn = guide.hadithArabic.ifBlank { guide.arabic }



val hadithTrans = guide.getHadithTranslation(selectedLanguage)



.ifBlank { guide.getMeaning(selectedLanguage) }



.ifBlank { guide.getVirtues(selectedLanguage) }



val hadithUrl = guide.hadithUrl.ifBlank { "https://sunnah.com/search?q=" + Uri.encode(guide.arabic) }



Dialog(



onDismissRequest = onDismiss,



properties = DialogProperties(usePlatformDefaultWidth = false)



) {



Card(



modifier = Modifier



.fillMaxWidth()



.padding(horizontal = 20.dp)



.heightIn(max = 680.dp)



.shadow(elevation = 20.dp, shape = RoundedCornerShape(24.dp)),



shape = RoundedCornerShape(24.dp),



colors = CardDefaults.cardColors(containerColor = Color.White),



border = BorderStroke(1.5.dp, Color(0xFF0E5D47))



) {



Column(



modifier = Modifier



.fillMaxWidth()



.verticalScroll(rememberScrollState())



) {



// 1. Royal Dark Emerald Header with Golden Seam



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



.padding(horizontal = 18.dp, vertical = 14.dp)



) {



Row(



modifier = Modifier.fillMaxWidth(),



horizontalArrangement = Arrangement.SpaceBetween,



verticalAlignment = Alignment.CenterVertically



) {



Row(



modifier = Modifier.weight(1f, fill = false),



verticalAlignment = Alignment.CenterVertically,



horizontalArrangement = Arrangement.spacedBy(8.dp)



) {



Text(text = "📜", fontSize = 18.sp)



Text(



text = strings.hadithRefModalTitle.uppercase(),



fontSize = 13.5.sp,



fontWeight = FontWeight.Black,



fontFamily = FontFamily.Serif,



letterSpacing = 0.6.sp,



color = Color.White,



maxLines = 1,



overflow = TextOverflow.Ellipsis



)



}



// Circular Close Button



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



fontSize = 14.sp,



fontWeight = FontWeight.Bold,



color = Color.White



)



}



}



}



// 2. Modal Body Content



Column(



modifier = Modifier



.fillMaxWidth()



.padding(horizontal = 18.dp, vertical = 18.dp),



verticalArrangement = Arrangement.spacedBy(14.dp)



) {



// Metadata Pill (Book & Grade)



Row(



modifier = Modifier



.fillMaxWidth()



.clip(RoundedCornerShape(12.dp))



.background(Color(0xFFF4FAF7))



.border(1.dp, Color(0xFFD1E7DD), RoundedCornerShape(12.dp))



.padding(horizontal = 12.dp, vertical = 9.dp),



horizontalArrangement = Arrangement.SpaceBetween,



verticalAlignment = Alignment.CenterVertically



) {



Row(



modifier = Modifier.weight(1f, fill = false),



verticalAlignment = Alignment.CenterVertically,



horizontalArrangement = Arrangement.spacedBy(6.dp)



) {



Text(text = "🧾", fontSize = 13.sp)



Text(



text = hadithBook,



fontSize = 12.sp,



fontWeight = FontWeight.Bold,



color = Color(0xFF0E5D47),



maxLines = 2,



overflow = TextOverflow.Ellipsis



)



}



Spacer(modifier = Modifier.width(8.dp))



Box(



modifier = Modifier



.clip(RoundedCornerShape(6.dp))



.background(Color(0xFFDCFCE7))



.padding(horizontal = 8.dp, vertical = 3.dp)



) {



Text(



text = "✓ $hadithGrade",



fontSize = 11.sp,



fontWeight = FontWeight.ExtraBold,



color = Color(0xFF059669),



maxLines = 1



)



}



}



// Narrated by line (RTL-safe with buildAnnotatedString)



if (hadithNarrator.isNotBlank()) {



Text(



text = buildAnnotatedString {



withStyle(SpanStyle(fontStyle = FontStyle.Italic, color = Color(0xFF6B7280))) {



append("${strings.narratorLabel}: ")



}



withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFF374151))) {



append(hadithNarrator)



}



},



fontSize = 12.sp,



textAlign = if (isRtl) TextAlign.Right else TextAlign.Left,



modifier = Modifier.fillMaxWidth()



)



}



// Arabic Matn Box



if (arMatn.isNotBlank()) {



Box(



modifier = Modifier



.fillMaxWidth()



.clip(RoundedCornerShape(14.dp))



.background(Color(0xFFFAF6EE))



.border(1.2.dp, Color(0xFFE5D5B5), RoundedCornerShape(14.dp))



.padding(14.dp)



) {



Text(



text = arMatn,



fontSize = 18.sp,



fontWeight = FontWeight.Bold,



fontFamily = AmiriQuranFont,



lineHeight = 32.sp,



textAlign = TextAlign.End,



color = Color(0xFF1B382B),



modifier = Modifier.fillMaxWidth()



)



}



}



// Transliteration Box (if available)



val translit = guide.getTranslit(selectedLanguage)



if (translit.isNotBlank()) {



Column(



modifier = Modifier



.fillMaxWidth()



.clip(RoundedCornerShape(12.dp))



.background(Color(0xFFFFFBEB))



.border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(12.dp))



.padding(start = 14.dp, end = 12.dp, top = 10.dp, bottom = 12.dp)



) {



Text(



text = strings.transliterationTitle.ifBlank { "Transliteration:" },



fontSize = 11.sp,



fontWeight = FontWeight.Bold,



color = Color(0xFF92400E),



letterSpacing = 0.5.sp



)



Spacer(modifier = Modifier.height(4.dp))



Text(



text = "« $translit »",



fontSize = 12.5.sp,



fontStyle = FontStyle.Italic,



lineHeight = 18.sp,



color = Color(0xFF78350F),



textAlign = if (isRtl) TextAlign.Right else TextAlign.Left,



modifier = Modifier.fillMaxWidth()



)



}



}



// Translation Box with 4px Emerald Accent Border



if (hadithTrans.isNotBlank()) {



Column(



modifier = Modifier



.fillMaxWidth()



.clip(RoundedCornerShape(12.dp))



.background(Color(0xFFF9FAFB))



.drawBehind {



drawRect(



color = Color(0xFF0E5D47),



size = Size(4.dp.toPx(), size.height)



)



}



.padding(start = 14.dp, end = 12.dp, top = 10.dp, bottom = 12.dp)



) {



Text(



text = strings.translationTitle.ifBlank { "Translation / Meaning:" },



fontSize = 11.sp,



fontWeight = FontWeight.Bold,



color = Color(0xFF0E5D47),



letterSpacing = 0.5.sp



)



Spacer(modifier = Modifier.height(4.dp))



Text(



text = hadithTrans,



fontSize = 13.sp,



lineHeight = 19.sp,



color = Color(0xFF1F2937),



textAlign = if (isRtl) TextAlign.Right else TextAlign.Left,



modifier = Modifier.fillMaxWidth()



)



}



}



// View on Sunnah.com / Official Source Button



Box(



modifier = Modifier



.fillMaxWidth()



.shadow(3.dp, RoundedCornerShape(12.dp))



.clip(RoundedCornerShape(12.dp))



.background(



Brush.linearGradient(



listOf(Color(0xFF0E5D47), Color(0xFF10B981))



)



)



.clickable {



try {



val intent = Intent(Intent.ACTION_VIEW, Uri.parse(hadithUrl))



context.startActivity(intent)



} catch (_: Exception) {



Toast.makeText(context, "Cannot open browser", Toast.LENGTH_SHORT).show()



}



}



.padding(vertical = 12.dp, horizontal = 16.dp),



contentAlignment = Alignment.Center



) {



Row(



verticalAlignment = Alignment.CenterVertically,



horizontalArrangement = Arrangement.spacedBy(6.dp)



) {



Text(text = "🌐", fontSize = 14.sp)



Text(



text = strings.viewOnSunnah,



fontSize = 13.5.sp,



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



// ══════════════════════════════════════════════════════════════════════



// HELPER COMPOSABLE: Header Icon Button



// ══════════════════════════════════════════════════════════════════════



@Composable



private fun HeaderIconButton(



icon: String? = null,



imageVector: ImageVector? = null,



isActive: Boolean = false,



onClick: () -> Unit



) {



Box(



modifier = Modifier



.size(34.dp)



.shadow(elevation = 2.dp, shape = RoundedCornerShape(9.dp))



.clip(RoundedCornerShape(9.dp))



.background(Color.White)



.border(1.dp, Color(0x33A07828), RoundedCornerShape(9.dp))



.clickable { onClick() },



contentAlignment = Alignment.Center



) {



if (imageVector != null) {



Icon(



imageVector = imageVector,



contentDescription = null,



tint = Color(0xFF1C2826),



modifier = Modifier.size(15.dp)



)



} else if (icon != null) {



Text(text = icon, fontSize = 14.sp)



}



}



}



