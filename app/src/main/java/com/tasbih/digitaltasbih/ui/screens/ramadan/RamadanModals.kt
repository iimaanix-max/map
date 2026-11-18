package com.tasbih.digitaltasbih.ui.screens.ramadan

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.tasbih.digitaltasbih.data.ramadan.RamadanData
import com.tasbih.digitaltasbih.data.ramadan.RamadanDua
import com.tasbih.digitaltasbih.data.ramadan.RamadanRepository

// ─────────────────────────────────────────────────────────────
// 1. SEHRI & IFTAR DUA MODAL
// ─────────────────────────────────────────────────────────────
@Composable
fun SehriIftarDuaModal(
    onDismiss: () -> Unit,
    isDarkTheme: Boolean = true,
    initialTab: Int = 0, // 0: Sehri, 1: Iftar, 2: 3 Ashras
    language: String = "en"
) {
    var selectedTab by remember { mutableIntStateOf(initialTab) }
    val haptic = LocalHapticFeedback.current

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 16.dp, shape = RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .background(if (isDarkTheme) Color(0xFF092017) else Color(0xFFFFFFFF))
                .border(1.5.dp, Color(0xFFD4AF37), RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (language == "hi") "🌙 रमज़ान की दुआएं" else if (language == "ur") "🌙 رمضان کی دعائیں" else "🌙 RAMADAN DUAS",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD4AF37),
                        letterSpacing = 1.sp
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.1f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = if (isDarkTheme) Color.White else Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Tab Switcher
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isDarkTheme) Color(0xFF06140E) else Color(0xFFF1EFEA))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val tabs = if (language == "hi") {
                        listOf("सहरी की दुआ", "इफ़्तार", "3 अशरा")
                    } else if (language == "ur") {
                        listOf("سحری کی دعا", "افطار", "3 عشرہ")
                    } else {
                        listOf("Suhoor (Sehri)", "Iftar", "3 Ashras")
                    }
                    tabs.forEachIndexed { index, label ->
                        val isSelected = selectedTab == index
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isSelected) Color(0xFF0E5D47) else Color.Transparent
                                )
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    selectedTab = index
                                }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else if (isDarkTheme) Color(0xFF9CA3AF) else Color(0xFF6B7280)
                            )
                        }
                    }
                }

                when (selectedTab) {
                    0 -> {
                        // Sehri Dua
                        DuaDisplayCard(
                            dua = RamadanData.sehriDua,
                            isDarkTheme = isDarkTheme,
                            language = language
                        )
                    }
                    1 -> {
                        // Iftar Duas (Both Sunnah and Popular)
                        DuaDisplayCard(
                            dua = RamadanData.iftarSunnahDua,
                            isDarkTheme = isDarkTheme,
                            language = language
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        DuaDisplayCard(
                            dua = RamadanData.iftarPopularDua,
                            isDarkTheme = isDarkTheme,
                            language = language
                        )
                    }
                    2 -> {
                        // 3 Ashras
                        DuaDisplayCard(dua = RamadanData.ashra1Dua, isDarkTheme = isDarkTheme, language = language)
                        DuaDisplayCard(dua = RamadanData.ashra2Dua, isDarkTheme = isDarkTheme, language = language)
                        DuaDisplayCard(dua = RamadanData.ashra3Dua, isDarkTheme = isDarkTheme, language = language)
                    }
                }
            }
        }
    }
}

@Composable
private fun DuaDisplayCard(
    dua: RamadanDua,
    isDarkTheme: Boolean,
    language: String = "en"
) {
    val translationText = when (language) {
        "hi" -> dua.translationHi
        "ur" -> dua.translationUr
        else -> "${dua.translationHi}\n\n\"${dua.translationEn}\""
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (isDarkTheme) Color(0xFF0D2C20) else Color(0xFFFBF9F4))
            .border(1.dp, Color(0x40D4AF37), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = dua.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD4AF37),
                textAlign = TextAlign.Center
            )

            // Arabic Calligraphy
            Text(
                text = dua.arabic,
                fontSize = 22.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = if (isDarkTheme) Color(0xFFFAF7EE) else Color(0xFF14241E),
                textAlign = TextAlign.Center,
                lineHeight = 36.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            // Transliteration
            Text(
                text = dua.transliteration,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.Serif,
                color = Color(0xFFD4AF37),
                textAlign = TextAlign.Center
            )

            // Translation (Localized)
            Text(
                text = translationText,
                fontSize = 12.sp,
                color = if (isDarkTheme) Color(0xFFD1D5DB) else Color(0xFF4B5563),
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )

            Text(
                text = "Ref: ${dua.reference}",
                fontSize = 10.sp,
                color = if (isDarkTheme) Color(0xFF6EE7B7) else Color(0xFF065F46),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────
// 2. ZAKAT & FITRANA CALCULATOR MODAL
// ─────────────────────────────────────────────────────────────
@Composable
fun ZakatCalculatorModal(
    onDismiss: () -> Unit,
    isDarkTheme: Boolean = true
) {
    val context = LocalContext.current
    var activeSubTab by remember { mutableIntStateOf(0) } // 0: Zakat, 1: Fitrana

    // Zakat inputs
    var cashStr by remember { mutableStateOf(RamadanRepository.getSavedCashAmount(context).takeIf { it > 0 }?.toString() ?: "") }
    var goldGramsStr by remember { mutableStateOf(RamadanRepository.getSavedGoldGrams(context).takeIf { it > 0 }?.toString() ?: "") }
    var silverGramsStr by remember { mutableStateOf(RamadanRepository.getSavedSilverGrams(context).takeIf { it > 0 }?.toString() ?: "") }
    var debtsStr by remember { mutableStateOf(RamadanRepository.getSavedDebts(context).takeIf { it > 0 }?.toString() ?: "") }

    // Rates in INR (Standard benchmarks)
    val goldRatePerGram = 7400.0
    val silverRatePerGram = 92.0
    val silverNisabGrams = 612.36
    val silverNisabThreshold = silverNisabGrams * silverRatePerGram // ~56,337 INR

    val cashVal = cashStr.toDoubleOrNull() ?: 0.0
    val goldVal = (goldGramsStr.toDoubleOrNull() ?: 0.0) * goldRatePerGram
    val silverVal = (silverGramsStr.toDoubleOrNull() ?: 0.0) * silverRatePerGram
    val debtsVal = debtsStr.toDoubleOrNull() ?: 0.0

    val netZakatable = (cashVal + goldVal + silverVal - debtsVal).coerceAtLeast(0.0)
    val isEligible = netZakatable >= silverNisabThreshold
    val zakatPayable = if (isEligible) netZakatable * 0.025 else 0.0

    // Fitrana inputs
    var familyCount by remember { mutableIntStateOf(RamadanRepository.getFitranaFamilyCount(context)) }
    var fitranaRate by remember { mutableDoubleStateOf(RamadanRepository.getFitranaRatePerPerson(context)) }
    val totalFitrana = familyCount * fitranaRate

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 620.dp)
                .shadow(16.dp, RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .background(if (isDarkTheme) Color(0xFF092017) else Color.White)
                .border(1.5.dp, Color(0xFFD4AF37), RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "💰 ZAKAT & FITRANA",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD4AF37)
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.1f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = if (isDarkTheme) Color.White else Color.Black
                        )
                    }
                }

                // Sub-tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isDarkTheme) Color(0xFF06140E) else Color(0xFFF1EFEA))
                        .padding(4.dp)
                ) {
                    val tabs = listOf("Zakat (2.5%)", "Sadaqatul Fitr (Fitrana)")
                    tabs.forEachIndexed { idx, title ->
                        val sel = activeSubTab == idx
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (sel) Color(0xFF0E5D47) else Color.Transparent)
                                .clickable { activeSubTab = idx }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (sel) FontWeight.Bold else FontWeight.Medium,
                                color = if (sel) Color.White else if (isDarkTheme) Color(0xFF9CA3AF) else Color(0xFF6B7280)
                            )
                        }
                    }
                }

                if (activeSubTab == 0) {
                    // ZAKAT CALCULATOR
                    OutlinedTextField(
                        value = cashStr,
                        onValueChange = {
                            cashStr = it
                            RamadanRepository.setSavedCashAmount(context, it.toDoubleOrNull() ?: 0.0)
                        },
                        label = { Text("Cash in Hand & Bank (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFD4AF37),
                            unfocusedBorderColor = Color(0x60D4AF37)
                        )
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = goldGramsStr,
                            onValueChange = {
                                goldGramsStr = it
                                RamadanRepository.setSavedGoldGrams(context, it.toDoubleOrNull() ?: 0.0)
                            },
                            label = { Text("Gold (Grams)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFD4AF37),
                                unfocusedBorderColor = Color(0x60D4AF37)
                            )
                        )
                        OutlinedTextField(
                            value = silverGramsStr,
                            onValueChange = {
                                silverGramsStr = it
                                RamadanRepository.setSavedSilverGrams(context, it.toDoubleOrNull() ?: 0.0)
                            },
                            label = { Text("Silver (Grams)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFD4AF37),
                                unfocusedBorderColor = Color(0x60D4AF37)
                            )
                        )
                    }

                    OutlinedTextField(
                        value = debtsStr,
                        onValueChange = {
                            debtsStr = it
                            RamadanRepository.setSavedDebts(context, it.toDoubleOrNull() ?: 0.0)
                        },
                        label = { Text("Minus Immediate Debts / Liabilities (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFD4AF37),
                            unfocusedBorderColor = Color(0x60D4AF37)
                        )
                    )

                    // Result Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFF0F4D3A), Color(0xFF09281E))
                                )
                            )
                            .border(1.dp, Color(0xFFD4AF37), RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "TOTAL ZAKAT PAYABLE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFD4AF37),
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "₹ %,.2f".format(zakatPayable),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                            Text(
                                text = if (isEligible) "Net Wealth ₹%,.0f exceeds Nisab threshold (₹%,.0f)".format(netZakatable, silverNisabThreshold)
                                else "Net wealth is below Nisab threshold (₹%,.0f). Zakat is not obligatory.".format(silverNisabThreshold),
                                fontSize = 11.sp,
                                color = if (isEligible) Color(0xFF6EE7B7) else Color(0xFFF87171),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    // FITRANA CALCULATOR
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isDarkTheme) Color(0xFF0D2C20) else Color(0xFFF6F4EE))
                            .padding(14.dp)
                    ) {
                        Text(
                            text = "Sadaqatul Fitr (Fitrana) is obligatory on every Muslim (man, woman, child) and must be paid before Eid Salah so the poor can celebrate.",
                            fontSize = 12.sp,
                            color = if (isDarkTheme) Color(0xFFD1D5DB) else Color(0xFF4B5563),
                            lineHeight = 17.sp
                        )
                    }

                    // Family members stepper
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Family Members", fontWeight = FontWeight.Bold, color = if (isDarkTheme) Color.White else Color.Black)
                            Text("Including children & dependants", fontSize = 11.sp, color = Color.Gray)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            IconButton(
                                onClick = {
                                    if (familyCount > 1) {
                                        familyCount--
                                        RamadanRepository.setFitranaFamilyCount(context, familyCount)
                                    }
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF0E5D47))
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Minus", tint = Color.White)
                            }
                            Text(
                                text = "$familyCount",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFD4AF37)
                            )
                            IconButton(
                                onClick = {
                                    familyCount++
                                    RamadanRepository.setFitranaFamilyCount(context, familyCount)
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF0E5D47))
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                            }
                        }
                    }

                    // Rate selector
                    Text(
                        text = "Standard Rate: ₹100 per person (approx 2.04 kg wheat/flour equivalent)",
                        fontSize = 12.sp,
                        color = Color(0xFFD4AF37)
                    )

                    // Total Fitrana Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFF0F4D3A), Color(0xFF09281E))
                                )
                            )
                            .border(1.dp, Color(0xFFD4AF37), RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "TOTAL FITRANA PAYABLE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFD4AF37),
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "₹ %,.0f".format(totalFitrana),
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                            Text(
                                text = "$familyCount members × ₹%,.0f = ₹%,.0f".format(fitranaRate, totalFitrana),
                                fontSize = 12.sp,
                                color = Color(0xFF6EE7B7)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// 3. QURAN KHATAM PLANNER MODAL
// ─────────────────────────────────────────────────────────────
@Composable
fun QuranKhatamModal(
    onDismiss: () -> Unit,
    isDarkTheme: Boolean = true
) {
    val context = LocalContext.current
    var completedJuz by remember { mutableIntStateOf(RamadanRepository.getCompletedJuzCount(context)) }
    var targetKhatams by remember { mutableIntStateOf(RamadanRepository.getQuranTargetKhatams(context)) }

    val pagesPerSalah = if (targetKhatams == 1) 4 else 8

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(16.dp, RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .background(if (isDarkTheme) Color(0xFF092017) else Color.White)
                .border(1.5.dp, Color(0xFFD4AF37), RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📖 QURAN KHATAM PLANNER",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD4AF37)
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.1f))
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = if (isDarkTheme) Color.White else Color.Black)
                    }
                }

                // Strategy Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF0E5D47).copy(alpha = 0.2f))
                        .border(1.dp, Color(0xFFD4AF37).copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "✨ The Sunnah Formula:",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD4AF37),
                            fontSize = 13.sp
                        )
                        Text(
                            text = "The Quran has 600 pages (30 Juz). By reciting just $pagesPerSalah pages after each of the 5 daily prayers, you finish 1 full Juz every day and complete $targetKhatams full Quran Khatam by Eid!",
                            fontSize = 12.sp,
                            color = if (isDarkTheme) Color(0xFFD1D5DB) else Color(0xFF374151),
                            lineHeight = 17.sp
                        )
                    }
                }

                // Juz Completed Stepper
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Juz (Paras) Completed",
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) Color.White else Color.Black
                        )
                        Text(
                            text = "$completedJuz of 30 Juz (${(completedJuz * 100) / 30}%)",
                            fontSize = 12.sp,
                            color = Color(0xFFD4AF37)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(
                            onClick = {
                                if (completedJuz > 0) {
                                    completedJuz--
                                    RamadanRepository.setCompletedJuzCount(context, completedJuz)
                                }
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF0E5D47))
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Minus", tint = Color.White)
                        }

                        Text(
                            text = "$completedJuz",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFD4AF37)
                        )

                        IconButton(
                            onClick = {
                                if (completedJuz < 30) {
                                    completedJuz++
                                    RamadanRepository.setCompletedJuzCount(context, completedJuz)
                                }
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF0E5D47))
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                        }
                    }
                }

                // Progress Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color.White.copy(alpha = 0.1f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction = (completedJuz / 30f).coerceIn(0f, 1f))
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF0E5D47), Color(0xFFD4AF37))
                                )
                            )
                    )
                }

                // Target Selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(1, 2).forEach { kTarget ->
                        val isSelected = targetKhatams == kTarget
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) Color(0xFF0E5D47) else Color.Transparent)
                                .border(1.dp, Color(0xFFD4AF37), RoundedCornerShape(12.dp))
                                .clickable {
                                    targetKhatams = kTarget
                                    RamadanRepository.setQuranTargetKhatams(context, kTarget)
                                }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$kTarget Khatam Target",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else Color(0xFFD4AF37)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// 4. RAMADAN FIQH & RULES MODAL (Doubts & Masail FAQ)
// ─────────────────────────────────────────────────────────────
@Composable
fun RamadanFiqhModal(
    onDismiss: () -> Unit,
    isDarkTheme: Boolean = true
) {
    var expandedItemIndex by remember { mutableIntStateOf(-1) }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 620.dp)
                .shadow(16.dp, RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .background(if (isDarkTheme) Color(0xFF092017) else Color.White)
                .border(1.5.dp, Color(0xFFD4AF37), RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📚 RAMADAN RULES & FIQH",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD4AF37)
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.1f))
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = if (isDarkTheme) Color.White else Color.Black)
                    }
                }

                Text(
                    text = "Authentic rulings on common questions (What breaks fast & what is permissible):",
                    fontSize = 12.sp,
                    color = if (isDarkTheme) Color(0xFF9CA3AF) else Color(0xFF6B7280)
                )

                RamadanData.fiqhQuestions.forEachIndexed { idx, item ->
                    val isExpanded = expandedItemIndex == idx
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isDarkTheme) Color(0xFF0D2C20) else Color(0xFFF9F8F5))
                            .border(1.dp, if (isExpanded) Color(0xFFD4AF37) else Color(0x33D4AF37), RoundedCornerShape(14.dp))
                            .clickable {
                                expandedItemIndex = if (isExpanded) -1 else idx
                            }
                            .padding(14.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item.questionHi,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDarkTheme) Color.White else Color(0xFF14241E),
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Expand",
                                    tint = Color(0xFFD4AF37)
                                )
                            }

                            // Ruling pill
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF0E5D47).copy(alpha = 0.3f))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = item.rulingType,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF6EE7B7)
                                )
                            }

                            if (isExpanded) {
                                Text(
                                    text = item.answerHi,
                                    fontSize = 12.sp,
                                    color = if (isDarkTheme) Color(0xFFD1D5DB) else Color(0xFF374151),
                                    lineHeight = 18.sp
                                )
                                Text(
                                    text = "English: ${item.answerEn}",
                                    fontSize = 11.sp,
                                    color = Color.Gray,
                                    lineHeight = 16.sp
                                )
                                Text(
                                    text = "Reference: ${item.reference}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFFD4AF37)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// 5. CITY / AREA LEADERBOARD MODAL (Fastabiqul Khairat)
// ─────────────────────────────────────────────────────────────
@Composable
fun CityLeaderboardModal(
    onDismiss: () -> Unit,
    isDarkTheme: Boolean = true
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    var selectedCity by remember { mutableStateOf(RamadanRepository.getSelectedCity(context)) }
    var userDhikrTotal by remember { mutableStateOf(RamadanRepository.getUserRamadanDhikrTotal(context)) }

    val leaderboard = remember(selectedCity) {
        RamadanData.getCityLeaderboard(selectedCity)
    }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 640.dp)
                .shadow(16.dp, RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .background(if (isDarkTheme) Color(0xFF092017) else Color.White)
                .border(1.5.dp, Color(0xFFD4AF37), RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "🏆 AREA DHIKR RACE",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD4AF37)
                        )
                        Text(
                            text = "Fastabiqul Khairat • Race in Good Deeds",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.1f))
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = if (isDarkTheme) Color.White else Color.Black)
                    }
                }

                // City Selector horizontal scroll
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RamadanData.featuredCities.forEach { city ->
                        val isSel = city == selectedCity
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSel) Color(0xFF0E5D47) else Color(0xFF06140E))
                                .border(1.dp, if (isSel) Color(0xFFD4AF37) else Color(0x33D4AF37), RoundedCornerShape(10.dp))
                                .clickable {
                                    selectedCity = city
                                    RamadanRepository.setSelectedCity(context, city)
                                }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = city,
                                fontSize = 12.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSel) Color.White else Color(0xFFD1D5DB)
                            )
                        }
                    }
                }

                // City Highlight Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF0F4D3A), Color(0xFF082218))
                            )
                        )
                        .border(1.dp, Color(0xFFD4AF37), RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "$selectedCity Total Recitations",
                                fontSize = 11.sp,
                                color = Color(0xFFD4AF37),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "1,48,250 Dhikr ✨",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }

                        // Button to add dhikr
                        Button(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                RamadanRepository.incrementUserRamadanDhikr(context, 10L)
                                userDhikrTotal += 10L
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFD4AF37)
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("+10 Dhikr", color = Color(0xFF06140E), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }

                // Leaderboard List
                Text(
                    text = "Top Contributors in $selectedCity:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) Color.White else Color.Black
                )

                leaderboard.forEach { item ->
                    val medal = when (item.rank) {
                        1 -> "🥇"
                        2 -> "🥈"
                        3 -> "🥉"
                        else -> "#${item.rank}"
                    }
                    val isYou = item.isCurrentUser

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isYou) Color(0xFF0E5D47).copy(alpha = 0.4f)
                                else if (isDarkTheme) Color(0xFF0D2C20)
                                else Color(0xFFF9F8F5)
                            )
                            .border(
                                width = if (isYou) 1.5.dp else 0.5.dp,
                                color = if (isYou) Color(0xFFD4AF37) else Color(0x30D4AF37),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = medal,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Column {
                                Text(
                                    text = if (isYou) "${item.userName} (You)" else item.userName,
                                    fontSize = 13.sp,
                                    fontWeight = if (isYou) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isYou) Color(0xFFD4AF37) else if (isDarkTheme) Color.White else Color.Black
                                )
                                Text(
                                    text = item.cityName,
                                    fontSize = 10.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        Text(
                            text = "%,d".format(if (isYou) userDhikrTotal else item.dhikrCount),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD4AF37)
                        )
                    }
                }
            }
        }
    }
}
