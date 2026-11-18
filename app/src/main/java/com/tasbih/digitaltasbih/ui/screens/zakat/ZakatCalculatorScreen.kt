package com.tasbih.digitaltasbih.ui.screens.zakat

import android.content.Intent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.data.PrayerRepository
import com.tasbih.digitaltasbih.data.zakat.NisabStandard
import com.tasbih.digitaltasbih.data.zakat.ZakatCalculationData
import com.tasbih.digitaltasbih.data.zakat.ZakatLocalization
import com.tasbih.digitaltasbih.data.zakat.ZakatRepository
import com.tasbih.digitaltasbih.data.zakat.ZakatStrings
import com.tasbih.digitaltasbih.ui.theme.GoldAccent
import com.tasbih.digitaltasbih.data.pillars.IslamicReferenceItem
import com.tasbih.digitaltasbih.data.pillars.PillarsReferenceRepository
import com.tasbih.digitaltasbih.ui.components.IslamicReferenceModal

import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZakatCalculatorScreen(
    onBack: () -> Unit,
    selectedLanguage: String = PrayerRepository.getLanguage()
) {
    val context = LocalContext.current
    val isDarkTheme = remember { PrayerRepository.isDarkTheme() }
    val strings = remember(selectedLanguage) { ZakatLocalization.getStrings(selectedLanguage) }

    var calcData by remember { mutableStateOf(ZakatRepository.loadData(context)) }

    // Auto-save whenever values change
    LaunchedEffect(calcData) {
        ZakatRepository.saveData(context, calcData)
    }

    // Modal state for results bottom sheet (Image 4)
    var showResultsSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    // Accordion expansion states for assets
    var activeReferenceItem by remember { mutableStateOf<IslamicReferenceItem?>(null) }
    var isNisabExpanded by remember { mutableStateOf(false) }
    var isCashExpanded by remember { mutableStateOf(false) }
    var isGoldExpanded by remember { mutableStateOf(false) }
    var isSilverExpanded by remember { mutableStateOf(false) }
    var isBusinessExpanded by remember { mutableStateOf(false) }
    var isMoneyOwedExpanded by remember { mutableStateOf(false) }
    var isInvestmentsExpanded by remember { mutableStateOf(false) }
    var isDebtsExpanded by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    // Day Mode Color Scheme (Dark Emerald + White) vs Night Mode
    val screenBgColor = if (isDarkTheme) Color(0xFF07150E) else Color(0xFFF6F4EE)
    val cardBgColor = if (isDarkTheme) Color(0xFF0D2D20) else Color(0xFFFFFFFF)
    val cardBorderColor = if (isDarkTheme) Color(0x40D4AF37) else Color(0x350D4A3E)
    val primaryTextColor = if (isDarkTheme) Color.White else Color(0xFF0D382B)
    val secondaryTextColor = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)

    fun formatMoney(amount: Double): String {
        return try {
            val format = NumberFormat.getNumberInstance(Locale("en", "IN"))
            format.maximumFractionDigits = 0
            "${calcData.currencySymbol} ${format.format(amount)}"
        } catch (_: Exception) {
            "${calcData.currencySymbol} ${amount.toLong()}"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(screenBgColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // 1. TOP APP BAR (Dark Emerald Green + White/Gold)
            TopZakatHeader(
                title = strings.title,
                isDarkTheme = isDarkTheme,
                onBack = onBack,
                onReset = {
                    calcData = ZakatCalculationData()
                    ZakatRepository.clearAll(context)
                },
                resetLabel = strings.resetAll
            )

            // 2. SCROLLABLE CONTENT BODY
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // HERO CARD (Dark Emerald Green + Gold Calligraphy)
                ZakatHeroCard(strings = strings)

                // HAWL & DEFINITION INFO BOX
                HawlInfoBox(
                    text = strings.hawlInfo,
                    isDarkTheme = isDarkTheme
                )

                // AUTHENTIC QURAN AYAT & HADITH CARD (Surah Al-Baqarah 2:43)
                AuthenticZakatReferenceCard(
                    strings = strings,
                    onReferenceClick = { ref -> activeReferenceItem = PillarsReferenceRepository.findReference(ref, "Zakat Command") }
                )

                // NISAB THRESHOLD CARD (Silver 595g / Gold 85g)
                NisabThresholdCard(
                    strings = strings,
                    calcData = calcData,
                    isDarkTheme = isDarkTheme,
                    isExpanded = isNisabExpanded,
                    onToggleExpand = { isNisabExpanded = !isNisabExpanded },
                    onUpdateData = { calcData = it },
                    onReferenceClick = { ref -> activeReferenceItem = PillarsReferenceRepository.findReference(ref, "Nisab Benchmark") },
                    formatMoney = ::formatMoney
                )

                // SECTION: YOUR ASSETS (इन्सानी जायदाद / संपत्तियां)
                SectionHeader(
                    title = strings.assetsSection,
                    icon = "💰",
                    totalFormatted = formatMoney(calcData.totalAssets),
                    isDarkTheme = isDarkTheme
                )

                // 1. Cash & Bank Savings
                AssetCategoryCard(
                    title = strings.cashTitle,
                    subtitle = strings.cashDesc,
                    amount = calcData.totalCashAndBank,
                    isExpanded = isCashExpanded,
                    isDarkTheme = isDarkTheme,
                    onToggleExpand = { isCashExpanded = !isCashExpanded },
                    formatMoney = ::formatMoney
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        ZakatInputField(
                            label = strings.cashOnHand,
                            value = if (calcData.cashInHand > 0) calcData.cashInHand.toString() else "",
                            currencySymbol = calcData.currencySymbol,
                            isDarkTheme = isDarkTheme,
                            onValueChange = { str ->
                                val v = str.toDoubleOrNull() ?: 0.0
                                calcData = calcData.copy(cashInHand = v)
                            }
                        )
                        ZakatInputField(
                            label = strings.bankAccount,
                            value = if (calcData.bankSavings > 0) calcData.bankSavings.toString() else "",
                            currencySymbol = calcData.currencySymbol,
                            isDarkTheme = isDarkTheme,
                            onValueChange = { str ->
                                val v = str.toDoubleOrNull() ?: 0.0
                                calcData = calcData.copy(bankSavings = v)
                            }
                        )
                    }
                }

                // 2. Gold Value (Enter in Grams or Direct Total)
                AssetCategoryCard(
                    title = strings.goldTitle,
                    subtitle = strings.goldDesc,
                    amount = calcData.effectiveGoldValue,
                    isExpanded = isGoldExpanded,
                    isDarkTheme = isDarkTheme,
                    onToggleExpand = { isGoldExpanded = !isGoldExpanded },
                    formatMoney = ::formatMoney
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "💡 Benchmark: ${formatMoney(calcData.goldRatePerGram)}/gram",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDarkTheme) GoldAccent else Color(0xFFB45309)
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.weight(1f)) {
                                ZakatInputField(
                                    label = strings.gramsLabel,
                                    value = if (calcData.goldGrams > 0) calcData.goldGrams.toString() else "",
                                    currencySymbol = "g",
                                    isDarkTheme = isDarkTheme,
                                    onValueChange = { str ->
                                        val v = str.toDoubleOrNull() ?: 0.0
                                        calcData = calcData.copy(goldGrams = v, goldDirectValue = 0.0)
                                    }
                                )
                            }
                            Box(modifier = Modifier.weight(1.3f)) {
                                ZakatInputField(
                                    label = strings.directValueLabel,
                                    value = if (calcData.goldDirectValue > 0) calcData.goldDirectValue.toString() else "",
                                    currencySymbol = calcData.currencySymbol,
                                    isDarkTheme = isDarkTheme,
                                    onValueChange = { str ->
                                        val v = str.toDoubleOrNull() ?: 0.0
                                        calcData = calcData.copy(goldDirectValue = v)
                                    }
                                )
                            }
                        }
                    }
                }

                // 3. Silver Value (Enter in Grams or Direct Total)
                AssetCategoryCard(
                    title = strings.silverTitle,
                    subtitle = strings.silverDesc,
                    amount = calcData.effectiveSilverValue,
                    isExpanded = isSilverExpanded,
                    isDarkTheme = isDarkTheme,
                    onToggleExpand = { isSilverExpanded = !isSilverExpanded },
                    formatMoney = ::formatMoney
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "💡 Benchmark: ${formatMoney(calcData.silverRatePerGram)}/gram",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDarkTheme) GoldAccent else Color(0xFFB45309)
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.weight(1f)) {
                                ZakatInputField(
                                    label = strings.gramsLabel,
                                    value = if (calcData.silverGrams > 0) calcData.silverGrams.toString() else "",
                                    currencySymbol = "g",
                                    isDarkTheme = isDarkTheme,
                                    onValueChange = { str ->
                                        val v = str.toDoubleOrNull() ?: 0.0
                                        calcData = calcData.copy(silverGrams = v, silverDirectValue = 0.0)
                                    }
                                )
                            }
                            Box(modifier = Modifier.weight(1.3f)) {
                                ZakatInputField(
                                    label = strings.directValueLabel,
                                    value = if (calcData.silverDirectValue > 0) calcData.silverDirectValue.toString() else "",
                                    currencySymbol = calcData.currencySymbol,
                                    isDarkTheme = isDarkTheme,
                                    onValueChange = { str ->
                                        val v = str.toDoubleOrNull() ?: 0.0
                                        calcData = calcData.copy(silverDirectValue = v)
                                    }
                                )
                            }
                        }
                    }
                }

                // 4. Business Inventory & Goods
                AssetCategoryCard(
                    title = strings.businessTitle,
                    subtitle = strings.businessDesc,
                    amount = calcData.businessInventory,
                    isExpanded = isBusinessExpanded,
                    isDarkTheme = isDarkTheme,
                    onToggleExpand = { isBusinessExpanded = !isBusinessExpanded },
                    formatMoney = ::formatMoney
                ) {
                    ZakatInputField(
                        label = strings.directValueLabel,
                        value = if (calcData.businessInventory > 0) calcData.businessInventory.toString() else "",
                        currencySymbol = calcData.currencySymbol,
                        isDarkTheme = isDarkTheme,
                        onValueChange = { str ->
                            val v = str.toDoubleOrNull() ?: 0.0
                            calcData = calcData.copy(businessInventory = v)
                        }
                    )
                }

                // 5. Money Owed to You
                AssetCategoryCard(
                    title = strings.moneyOwedTitle,
                    subtitle = strings.moneyOwedDesc,
                    amount = calcData.moneyOwedToYou,
                    isExpanded = isMoneyOwedExpanded,
                    isDarkTheme = isDarkTheme,
                    onToggleExpand = { isMoneyOwedExpanded = !isMoneyOwedExpanded },
                    formatMoney = ::formatMoney
                ) {
                    ZakatInputField(
                        label = strings.directValueLabel,
                        value = if (calcData.moneyOwedToYou > 0) calcData.moneyOwedToYou.toString() else "",
                        currencySymbol = calcData.currencySymbol,
                        isDarkTheme = isDarkTheme,
                        onValueChange = { str ->
                            val v = str.toDoubleOrNull() ?: 0.0
                            calcData = calcData.copy(moneyOwedToYou = v)
                        }
                    )
                }

                // 6. Stocks & Investments
                AssetCategoryCard(
                    title = strings.investmentsTitle,
                    subtitle = strings.investmentsDesc,
                    amount = calcData.investmentsAndStocks,
                    isExpanded = isInvestmentsExpanded,
                    isDarkTheme = isDarkTheme,
                    onToggleExpand = { isInvestmentsExpanded = !isInvestmentsExpanded },
                    formatMoney = ::formatMoney
                ) {
                    ZakatInputField(
                        label = strings.directValueLabel,
                        value = if (calcData.investmentsAndStocks > 0) calcData.investmentsAndStocks.toString() else "",
                        currencySymbol = calcData.currencySymbol,
                        isDarkTheme = isDarkTheme,
                        onValueChange = { str ->
                            val v = str.toDoubleOrNull() ?: 0.0
                            calcData = calcData.copy(investmentsAndStocks = v)
                        }
                    )
                }

                // SECTION: YOUR LIABILITIES (कर्ज़ और देनदारियां)
                SectionHeader(
                    title = strings.liabilitiesSection,
                    icon = "📑",
                    totalFormatted = "- ${formatMoney(calcData.totalLiabilities)}",
                    isDarkTheme = isDarkTheme,
                    isLiability = true
                )

                // Debts Due Now
                AssetCategoryCard(
                    title = strings.debtsTitle,
                    subtitle = strings.debtsDesc,
                    amount = calcData.debtsDueNow,
                    isExpanded = isDebtsExpanded,
                    isDarkTheme = isDarkTheme,
                    onToggleExpand = { isDebtsExpanded = !isDebtsExpanded },
                    formatMoney = ::formatMoney,
                    isLiability = true
                ) {
                    ZakatInputField(
                        label = strings.directValueLabel,
                        value = if (calcData.debtsDueNow > 0) calcData.debtsDueNow.toString() else "",
                        currencySymbol = calcData.currencySymbol,
                        isDarkTheme = isDarkTheme,
                        onValueChange = { str ->
                            val v = str.toDoubleOrNull() ?: 0.0
                            calcData = calcData.copy(debtsDueNow = v)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(84.dp))
            }

            // 3. STICKY BOTTOM ACTION BAR: "Calculate Zakat"
            BottomCalculateBar(
                strings = strings,
                isDarkTheme = isDarkTheme,
                onCalculate = {
                    showResultsSheet = true
                }
            )
        }

        // 4. RESULTS BOTTOM SHEET (Exact Image 4)
        if (showResultsSheet) {
            ModalBottomSheet(
                onDismissRequest = { showResultsSheet = false },
                sheetState = sheetState,
                containerColor = Color(0xFF0D4A3E),
                contentColor = Color.White,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                ZakatResultsSheetContent(
                    strings = strings,
                    calcData = calcData,
                    formatMoney = ::formatMoney,
                    onReferenceClick = { ref -> activeReferenceItem = PillarsReferenceRepository.findReference(ref, "Zakat Hadith") },
                    onRecalculate = {
                        coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                            showResultsSheet = false
                        }
                    },
                    onShare = {
                        val shareText = buildString {
                            appendLine("📊 ${strings.title} Summary")
                            appendLine("──────────────────")
                            appendLine("🪙 Status: ${if (calcData.isObligatory) strings.obligatoryTitle else strings.notObligatoryTitle}")
                            appendLine("💰 ${strings.netWealthLabel}: ${formatMoney(calcData.netZakatableWealth)}")
                            appendLine("⚖️ Nisab: ${formatMoney(calcData.nisabThreshold)}")
                            appendLine("📈 ${strings.zakatRateLabel}: ${calcData.zakatRatePercent}%")
                            if (calcData.isObligatory) {
                                appendLine("✨ ${strings.totalZakatPayable}: ${formatMoney(calcData.zakatPayable)}")
                            }
                            appendLine("──────────────────")
                            appendLine("Calculated via Digital Tasbih App")
                        }
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, shareText)
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, strings.shareSummary))
                    }
                )
            }
        }

        if (activeReferenceItem != null) {
            IslamicReferenceModal(
                item = activeReferenceItem!!,
                isDarkTheme = isDarkTheme,
                selectedLanguage = selectedLanguage,
                onClose = { activeReferenceItem = null }
            )
        }
    }
}

@Composable
private fun TopZakatHeader(
    title: String,
    isDarkTheme: Boolean,
    onBack: () -> Unit,
    onReset: () -> Unit,
    resetLabel: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(Color(0xFF0D4A3E), Color(0xFF09362C), Color(0xFF05241C))
                )
            )
            .border(1.2.dp, GoldAccent, RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
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
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f))
                        .border(1.dp, GoldAccent.copy(alpha = 0.6f), CircleShape)
                        .clickable(onClick = onBack),
                    contentAlignment = Alignment.Center
                ) {
                    Text("←", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Text(
                    text = title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.12f))
                    .border(1.dp, GoldAccent.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                    .clickable(onClick = onReset)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = resetLabel,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldAccent
                )
            }
        }
    }
}

@Composable
private fun ZakatHeroCard(strings: ZakatStrings) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0D4A3E), Color(0xFF08332A), Color(0xFF041C16))
                )
            )
            .border(1.2.dp, GoldAccent, RoundedCornerShape(18.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text("⭐", fontSize = 12.sp)
                Text("🪙", fontSize = 22.sp)
                Text("⭐", fontSize = 12.sp)
            }

            Text(
                text = strings.heroArabic,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = GoldAccent,
                fontFamily = FontFamily.Serif
            )

            Text(
                text = strings.heroBadge,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            HorizontalDivider(
                modifier = Modifier.width(160.dp),
                color = GoldAccent.copy(alpha = 0.4f),
                thickness = 1.dp
            )

            Text(
                text = strings.heroDesc,
                fontSize = 11.5.sp,
                color = Color(0xFFD1E7DD),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun HawlInfoBox(text: String, isDarkTheme: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(if (isDarkTheme) 0.dp else 2.dp, RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (isDarkTheme) Color(0xFF0F3225) else Color(0xFFFFFFFF)
            )
            .border(
                1.dp,
                if (isDarkTheme) GoldAccent.copy(alpha = 0.35f) else Color(0x350D4A3E),
                RoundedCornerShape(14.dp)
            )
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(if (isDarkTheme) Color(0xFF1B5943) else Color(0xFF0D4A3E)),
                contentAlignment = Alignment.Center
            ) {
                Text("ℹ️", fontSize = 13.sp)
            }
            Text(
                text = text,
                fontSize = 12.sp,
                color = if (isDarkTheme) Color(0xFFE2E8F0) else Color(0xFF0D382B),
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun AuthenticZakatReferenceCard(
    strings: ZakatStrings,
    onReferenceClick: (String) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0D4A3E), Color(0xFF083329))
                )
            )
            .border(1.2.dp, GoldAccent.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { onReferenceClick(strings.quranAyatRef) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("📖", fontSize = 14.sp)
                    Text(
                        text = strings.quranAyatRef,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent
                    )
                }
                Text(
                    text = "🔍 Read ↗",
                    fontSize = 11.sp,
                    color = GoldAccent,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = strings.quranAyatArabic,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = FontFamily.Serif,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Right
            )

            Text(
                text = strings.quranAyatTranslation,
                fontSize = 11.5.sp,
                color = Color(0xFFE2E8F0),
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun NisabThresholdCard(
    strings: ZakatStrings,
    calcData: ZakatCalculationData,
    isDarkTheme: Boolean,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onUpdateData: (ZakatCalculationData) -> Unit,
    onReferenceClick: (String) -> Unit = {},
    formatMoney: (Double) -> String
) {
    val cardBg = if (isDarkTheme) Color(0xFF103627) else Color(0xFFFFFFFF)
    val titleColor = if (isDarkTheme) Color.White else Color(0xFF0D382B)
    val subtitleColor = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(if (isDarkTheme) 0.dp else 3.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(cardBg)
            .border(
                1.2.dp,
                if (isDarkTheme) GoldAccent.copy(alpha = 0.5f) else Color(0xFF0D4A3E),
                RoundedCornerShape(16.dp)
            )
            .animateContentSize()
            .clickable(onClick = onToggleExpand)
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(if (isDarkTheme) Color(0xFF1D5A43) else Color(0xFFE8F5E9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("⚖️", fontSize = 16.sp)
                    }
                    Column {
                        Text(
                            text = strings.nisabTitle,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = titleColor
                        )
                        Text(
                            text = if (calcData.nisabStandard == NisabStandard.SILVER)
                                strings.silverBenchmark else strings.goldBenchmark,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDarkTheme) GoldAccent else Color(0xFFB45309)
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${calcData.zakatRatePercent}%",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isDarkTheme) GoldAccent else Color(0xFF0D4A3E)
                    )
                    Text(
                        text = formatMoney(calcData.nisabThreshold),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = titleColor
                    )
                }
            }

            Text(
                text = strings.nisabSubtitle,
                fontSize = 11.sp,
                color = subtitleColor
            )

            // Hadith proof for Nisab
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isDarkTheme) Color(0xFF072118) else Color(0xFFE8F5E9))
                    .border(0.8.dp, GoldAccent.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                    .clickable { onReferenceClick("Sunan Abi Dawud 1573") }
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "?? Hadith: Sunan Abi Dawud 1573 (20 Dinars / 200 Dirhams)",
                        fontSize = 10.sp,
                        color = if (isDarkTheme) Color(0xFFD4E3DB) else Color(0xFF0D4A3E),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "?? Read ?",
                        fontSize = 10.sp,
                        color = GoldAccent,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // EXPANDABLE NISAB SETTINGS
            if (isExpanded) {
                HorizontalDivider(
                    color = if (isDarkTheme) Color(0x30D4AF37) else Color(0x300D4A3E),
                    thickness = 1.dp
                )

                // Benchmark selector: Silver (Recommended) vs Gold
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (calcData.nisabStandard == NisabStandard.SILVER) Color(0xFF0D4A3E) else Color(0xFFF1F5F9)
                            )
                            .border(
                                width = 1.dp,
                                color = if (calcData.nisabStandard == NisabStandard.SILVER) GoldAccent else Color(0xFFCBD5E1),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable {
                                onUpdateData(calcData.copy(nisabStandard = NisabStandard.SILVER, customNisabValue = null))
                            }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🪙 595g Silver",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (calcData.nisabStandard == NisabStandard.SILVER) Color.White else Color(0xFF0F172A)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (calcData.nisabStandard == NisabStandard.GOLD) Color(0xFF0D4A3E) else Color(0xFFF1F5F9)
                            )
                            .border(
                                width = 1.dp,
                                color = if (calcData.nisabStandard == NisabStandard.GOLD) GoldAccent else Color(0xFFCBD5E1),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable {
                                onUpdateData(calcData.copy(nisabStandard = NisabStandard.GOLD, customNisabValue = null))
                            }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🥇 85g Gold",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (calcData.nisabStandard == NisabStandard.GOLD) Color.White else Color(0xFF0F172A)
                        )
                    }
                }

                // Custom Nisab Field
                ZakatInputField(
                    label = strings.customValue,
                    value = calcData.customNisabValue?.toString() ?: "",
                    currencySymbol = calcData.currencySymbol,
                    isDarkTheme = isDarkTheme,
                    onValueChange = { str ->
                        val v = str.toDoubleOrNull()
                        onUpdateData(calcData.copy(customNisabValue = v))
                    }
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    icon: String,
    totalFormatted: String,
    isDarkTheme: Boolean,
    isLiability: Boolean = false
) {
    val textColor = if (isDarkTheme) Color.White else Color(0xFF0D382B)
    val totalColor = if (isLiability) Color(0xFFDC2626) else if (isDarkTheme) GoldAccent else Color(0xFF0D4A3E)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(icon, fontSize = 16.sp)
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
        Text(
            text = totalFormatted,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = totalColor
        )
    }
}

@Composable
private fun AssetCategoryCard(
    title: String,
    subtitle: String,
    amount: Double,
    isExpanded: Boolean,
    isDarkTheme: Boolean,
    onToggleExpand: () -> Unit,
    formatMoney: (Double) -> String,
    isLiability: Boolean = false,
    content: @Composable () -> Unit
) {
    val cardBg = if (isLiability) {
        if (isDarkTheme) Color(0xFF261214) else Color(0xFFFFF1F2)
    } else {
        if (isDarkTheme) Color(0xFF0D2D20) else Color(0xFFFFFFFF)
    }

    val cardBorder = if (isLiability) {
        Color(0x80F87171)
    } else if (amount > 0) {
        if (isDarkTheme) GoldAccent.copy(alpha = 0.8f) else Color(0xFF0D4A3E)
    } else {
        if (isDarkTheme) Color(0x30D4AF37) else Color(0x350D4A3E)
    }

    val titleColor = if (isLiability) {
        Color(0xFF991B1B)
    } else {
        if (isDarkTheme) Color.White else Color(0xFF0D382B)
    }

    val subtitleColor = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(if (isDarkTheme) 0.dp else 2.dp, RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
            .background(cardBg)
            .border(
                width = if (amount > 0) 1.4.dp else 1.dp,
                color = cardBorder,
                shape = RoundedCornerShape(14.dp)
            )
            .animateContentSize()
            .clickable(onClick = onToggleExpand)
            .padding(12.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = titleColor
                    )
                    Text(
                        text = subtitle,
                        fontSize = 10.5.sp,
                        color = subtitleColor,
                        maxLines = if (isExpanded) 3 else 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (amount > 0) {
                        Text(
                            text = if (isLiability) "- ${formatMoney(amount)}" else formatMoney(amount),
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isLiability) Color(0xFFDC2626) else if (isDarkTheme) GoldAccent else Color(0xFF0D4A3E)
                        )
                    }
                    Text(
                        text = if (isExpanded) "▲" else "▼",
                        fontSize = 10.sp,
                        color = subtitleColor
                    )
                }
            }

            if (isExpanded) {
                HorizontalDivider(
                    color = if (isLiability) Color(0x40F87171) else if (isDarkTheme) Color(0x30D4AF37) else Color(0x250D4A3E),
                    thickness = 0.8.dp
                )
                content()
            }
        }
    }
}

@Composable
private fun ZakatInputField(
    label: String,
    value: String,
    currencySymbol: String,
    isDarkTheme: Boolean,
    onValueChange: (String) -> Unit
) {
    val fieldBg = if (isDarkTheme) Color(0xFF092017) else Color(0xFFF8FAFC)
    val fieldText = if (isDarkTheme) Color.White else Color(0xFF0F172A)
    val prefixColor = if (isDarkTheme) GoldAccent else Color(0xFF0D4A3E)
    val focusedBorder = if (isDarkTheme) GoldAccent else Color(0xFF0D4A3E)
    val unfocusedBorder = if (isDarkTheme) Color(0x50D4AF37) else Color(0xFFCBD5E1)

    OutlinedTextField(
        value = value,
        onValueChange = { input ->
            val clean = input.filter { it.isDigit() || it == '.' }
            onValueChange(clean)
        },
        label = { Text(label, fontSize = 11.sp, color = if (isDarkTheme) Color(0xFFA0AEC0) else Color(0xFF64748B)) },
        leadingIcon = {
            Text(currencySymbol, color = prefixColor, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = fieldText,
            unfocusedTextColor = fieldText,
            focusedBorderColor = focusedBorder,
            unfocusedBorderColor = unfocusedBorder,
            focusedContainerColor = fieldBg,
            unfocusedContainerColor = fieldBg
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun BottomCalculateBar(
    strings: ZakatStrings,
    isDarkTheme: Boolean,
    onCalculate: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isDarkTheme) Color(0xFF06140D) else Color(0xFFFFFFFF))
            .border(
                1.dp,
                if (isDarkTheme) Color(0x30D4AF37) else Color(0xFFE2E8F0),
                RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp)
            )
            .navigationBarsPadding()
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .shadow(elevation = 6.dp, shape = RoundedCornerShape(14.dp))
                .clip(RoundedCornerShape(14.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF0D4A3E), Color(0xFF08362C), Color(0xFF05241C))
                    )
                )
                .border(1.2.dp, GoldAccent, RoundedCornerShape(14.dp))
                .clickable(onClick = onCalculate),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("🧮", fontSize = 18.sp)
                Text(
                    text = strings.calculateButton,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun ZakatResultsSheetContent(
    strings: ZakatStrings,
    calcData: ZakatCalculationData,
    formatMoney: (Double) -> String,
    onRecalculate: () -> Unit,
    onShare: () -> Unit,
    onReferenceClick: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 14.dp)
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Drag handle bar
        Box(
            modifier = Modifier
                .width(42.dp)
                .height(4.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.3f))
        )

        // Sheet Header: Icon, Title, Close
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("🪙", fontSize = 20.sp)
                Text(
                    text = strings.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.15f))
                    .clickable(onClick = onRecalculate),
                contentAlignment = Alignment.Center
            ) {
                Text("✕", fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        // Status Card: Obligatory vs Not Obligatory (Exact Image 4)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    if (calcData.isObligatory) Color(0xFF093327) else Color(0xFF1E2E28)
                )
                .border(
                    width = 1.2.dp,
                    color = if (calcData.isObligatory) GoldAccent else Color(0x40D4AF37),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(14.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(if (calcData.isObligatory) "✅" else "ℹ️", fontSize = 16.sp)
                    Text(
                        text = if (calcData.isObligatory) strings.obligatoryTitle else strings.notObligatoryTitle,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (calcData.isObligatory) GoldAccent else Color(0xFF94A3B8)
                    )
                }

                Text(
                    text = if (calcData.isObligatory) strings.obligatorySubtitle else strings.notObligatorySubtitle,
                    fontSize = 11.5.sp,
                    color = Color(0xFFCBD5E1),
                    textAlign = TextAlign.Center
                )

                if (calcData.isObligatory) {
                    HorizontalDivider(
                        color = Color(0x30D4AF37),
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text(
                        text = strings.totalZakatPayable,
                        fontSize = 11.sp,
                        color = Color(0xFFD1E7DD)
                    )
                    Text(
                        text = formatMoney(calcData.zakatPayable),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = GoldAccent
                    )
                }
            }
        }

        // Two Summary Cards (Exact Image 4: Net Zakatable Wealth | Zakat Rate)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF093327))
                    .border(1.dp, Color(0x40D4AF37), RoundedCornerShape(14.dp))
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = strings.netWealthLabel,
                        fontSize = 10.sp,
                        color = Color(0xFF94A3B8)
                    )
                    Text(
                        text = formatMoney(calcData.netZakatableWealth),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF093327))
                    .border(1.dp, Color(0x40D4AF37), RoundedCornerShape(14.dp))
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = strings.zakatRateLabel,
                        fontSize = 10.sp,
                        color = Color(0xFF94A3B8)
                    )
                    Text(
                        text = "${calcData.zakatRatePercent}%",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent
                    )
                }
            }
        }

        // Authentic Hadith Card (Clickable with Modal)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF06221A))
                .border(0.8.dp, Color(0x30D4AF37), RoundedCornerShape(14.dp))
                .clickable { onReferenceClick(strings.hadithRefSource) }
                .padding(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = strings.hadithRefTitle,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent
                    )
                    Text(
                        text = "?? Read ?",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent
                    )
                }
                Text(
                    text = strings.hadithRefText,
                    fontSize = 11.5.sp,
                    color = Color(0xFFE2E8F0),
                    lineHeight = 16.sp
                )
                Text(
                    text = strings.hadithRefSource,
                    fontSize = 10.sp,
                    color = Color(0xFF94A3B8)
                )
            }
        }

        // Action Buttons: Recalculate & Share Summary
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF093327))
                    .border(1.dp, GoldAccent.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .clickable(onClick = onRecalculate),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("🔄", fontSize = 14.sp)
                    Text(
                        text = strings.recalculateButton,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFFB48A25), Color(0xFF8A6614))
                        )
                    )
                    .clickable(onClick = onShare),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("📤", fontSize = 14.sp)
                    Text(
                        text = strings.shareSummary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}