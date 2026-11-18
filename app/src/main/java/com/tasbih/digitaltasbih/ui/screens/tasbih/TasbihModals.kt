package com.tasbih.digitaltasbih.ui.screens.tasbih

import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tasbih.digitaltasbih.ui.components.rememberAssetPainter


import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import com.tasbih.digitaltasbih.util.AdMobRewardedManager

import androidx.compose.animation.core.*
import androidx.compose.foundation.interaction.MutableInteractionSource

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.tasbih.digitaltasbih.ui.components.AppToast
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.layout.onGloballyPositioned


import com.tasbih.digitaltasbih.data.BeadMarbleSkin
import com.tasbih.digitaltasbih.data.TasbihDhikrItem
import com.tasbih.digitaltasbih.data.TasbihRepository

/**
 * 1. SET TARGET MODAL (Exact Screenshot 000325)
 * Presets: 7, 11, 33, 66, 99, 100, 200, 500
 * Custom target input field + [ Done ✓ ]
 */
@Composable
fun SetTargetModal(
    currentTarget: Int,
    onSelectTarget: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedVal by remember { mutableIntStateOf(currentTarget) }
    var customText by remember { mutableStateOf("") }
    val presets = listOf(7, 11, 33, 66, 99, 100, 200, 500)

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFFAF7F2))
                .border(1.5.dp, Color(0xFFC9A84C).copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Top handle
                Box(
                    modifier = Modifier
                        .width(42.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFFC9A84C).copy(alpha = 0.4f))
                )

                // Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = "🎯", fontSize = 16.sp)
                    Text(
                        text = "SET TARGET",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF0E5D47),
                        letterSpacing = 0.8.sp
                    )
                }

                // 8 Preset Buttons Grid (4x2)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(presets) { preset ->
                        val isSelected = (selectedVal == preset)
                        Box(
                            modifier = Modifier
                                .height(46.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) Color(0xFFFEF9EE) else Color.White)
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) Color(0xFF0E5D47) else Color(0xFFE2E8F0),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    selectedVal = preset
                                    customText = ""
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = preset.toString(),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color(0xFF0E5D47) else Color(0xFF8A6414)
                            )
                        }
                    }
                }

                // Custom Target Input + [+ Set] Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = customText,
                        onValueChange = { customText = it.filter { char -> char.isDigit() } },
                        placeholder = {
                            Text(
                                text = "Custom Target (e.g. 40, 1000)",
                                fontSize = 12.sp,
                                color = Color(0xFF94A3B8)
                            )
                        },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF0E5D47),
                            unfocusedBorderColor = Color(0xFF0E5D47).copy(alpha = 0.4f),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )

                    Button(
                        onClick = {
                            val parsed = customText.toIntOrNull()
                            if (parsed != null && parsed > 0) {
                                selectedVal = parsed
                            }
                        },
                        modifier = Modifier.height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E5D47))
                    ) {
                        Text(text = "+ Set", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // [ Done ✓ ] Button
                Button(
                    onClick = {
                        val parsed = customText.toIntOrNull()
                        val finalTarget = if (parsed != null && parsed > 0) parsed else selectedVal
                        onSelectTarget(finalTarget)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E5D47))
                ) {
                    Text(
                        text = "Done ✓",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

/**
 * 2. ADD CUSTOM DHIKR MODAL (Exact Screenshot 000219)
 */
@Composable
fun AddCustomDhikrModal(
    onAddDhikr: (name: String, arabic: String) -> Unit,
    onDismiss: () -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    val customDhikrs = remember {
        TasbihRepository.getAllDhikrs().filter { it.isCustom }
    }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFFAF7F2))
                .border(1.5.dp, Color(0xFFC9A84C).copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Top handle
                Box(
                    modifier = Modifier
                        .width(42.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFFC9A84C).copy(alpha = 0.4f))
                        .align(Alignment.CenterHorizontally)
                )

                // Header with Close [✕]
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = "✏️", fontSize = 16.sp)
                        Text(
                            text = "Add Custom Dhikr",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0E5D47)
                        )
                    }

                    Text(
                        text = "✕",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B),
                        modifier = Modifier.clickable { onDismiss() }
                    )
                }

                Text(
                    text = "Enter your Dhikr or Dua:",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF334155)
                )

                // Input + [+ Add]
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = {
                            Text(
                                text = "e.g. SubhanAllah / سُبْحَانَ اللَّهِ",
                                fontSize = 12.sp,
                                color = Color(0xFF94A3B8)
                            )
                        },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF0E5D47),
                            unfocusedBorderColor = Color(0xFF0E5D47).copy(alpha = 0.4f),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )

                    Button(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                onAddDhikr(inputText.trim(), inputText.trim())
                                inputText = ""
                                onDismiss()
                            }
                        },
                        modifier = Modifier.height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E5D47))
                    ) {
                        Text(text = "+ Add", fontSize = 13.5.sp, fontWeight = FontWeight.Bold)
                    }
                }

                HorizontalDivider(color = Color(0xFFE2E8F0))

                // Custom Dhikrs List or Empty State
                Text(
                    text = "📿 My Custom Dhikrs (${customDhikrs.size})",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )

                if (customDhikrs.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "✨", fontSize = 28.sp)
                        Text(
                            text = "No custom dhikrs yet. Type above to add one!",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 200.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(customDhikrs) { d ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White)
                                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                                    .padding(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = d.getName("en"),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0E5D47)
                                    )
                                    Text(
                                        text = d.arabic,
                                        fontSize = 15.sp,
                                        fontFamily = FontFamily.Serif,
                                        color = Color(0xFF0E5D47)
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

/**
 * 3. SELECT DHIKR BOTTOMSHEET MODAL (Exact Screenshot 000226)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectDhikrModal(
    activeDhikrId: String,
    onSelectDhikr: (TasbihDhikrItem) -> Unit,
    onOpenAddCustom: () -> Unit,
    onDismiss: () -> Unit
) {
    val allDhikrs = remember { TasbihRepository.getAllDhikrs() }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFFFAF7F2),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Header: Select Dhikr + [✎] Edit
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Select Dhikr",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF14241E)
                )

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(8.dp))
                        .clickable {
                            onDismiss()
                            onOpenAddCustom()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "✏️", fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Scrollable List of Dhikrs
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 480.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(allDhikrs) { item ->
                    val isSelected = (item.id == activeDhikrId)
                    val count = TasbihRepository.getAllTimeCount(item.id)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .border(
                                width = if (isSelected) 1.8.dp else 1.dp,
                                color = if (isSelected) Color(0xFF0E5D47) else Color(0xFFE2E8F0),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable {
                                onSelectDhikr(item)
                                onDismiss()
                            }
                            .padding(horizontal = 14.dp, vertical = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Circular icon badge
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF0E5D47)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = item.icon, fontSize = 15.sp)
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            // Name
                            Text(
                                text = item.getName("en"),
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF14241E),
                                modifier = Modifier.weight(1f)
                            )

                            // Arabic
                            Text(
                                text = item.arabic,
                                fontSize = 15.5.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif,
                                color = Color(0xFF0E5D47)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            // Count Pill Badge
                            if (count > 0) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFF0E5D47))
                                        .padding(horizontal = 8.dp, vertical = 3.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = count.toString(),
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFF3D48A)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                            }

                            // Active Checkmark
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF0E5D47)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "✓", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

/**
 * 4. COLOURS & MARBLES GALLERY MODAL (Exact match for Image 2)
 * Large full-width view with 5 columns, circular close button, Free/Premium pills, and lock badges.
 */
private fun Context.findActivity(): Activity? {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    return null
}

@Composable
fun MarbleGalleryModal(
    activeSkinId: String,
    onSelectSkin: (BeadMarbleSkin) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val allSkins = remember { TasbihRepository.getAllBeadSkins() }
    var skinToUnlock by remember { mutableStateOf<BeadMarbleSkin?>(null) }

    LaunchedEffect(Unit) {
        AdMobRewardedManager.loadAd(context)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .fillMaxHeight(0.92f)
                .clip(RoundedCornerShape(26.dp))
                .background(Color(0xFFFAF6EF))
                .border(1.2.dp, Color(0xFFDECFA6), RoundedCornerShape(26.dp))
                .padding(horizontal = 14.dp, vertical = 14.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Top Bar: [ 🎨 Colours & Marbles ] ──────── [ ✕ ]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "🎨", fontSize = 18.sp)
                        Text(
                            text = "Colours & Marbles",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E1B18)
                        )
                    }

                    // Circular Close Button
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .shadow(2.dp, CircleShape)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, Color(0xFFE2D8C3), CircleShape)
                            .clickable { onDismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✕",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 5-Column Grid of Marbles (Exact Image 2)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(top = 4.dp, bottom = 16.dp)
                ) {
                    items(allSkins, key = { it.id }) { skin ->
                        val isSelected = (skin.id == activeSkinId)
                        val isUnlocked = TasbihRepository.isMarbleUnlocked(skin.id)

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    if (isUnlocked) {
                                        TasbihRepository.setActiveBeadSkin(skin.id)
                                        onSelectSkin(skin)
                                        onDismiss()
                                    } else {
                                        skinToUnlock = skin
                                    }
                                },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // 1. 3D Marble Sphere Preview
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.size(54.dp)
                            ) {
                                MarbleBallPreview(
                                    skin = skin,
                                    size = 50.dp,
                                    isSelected = isSelected
                                )

                                // Bronze Lock Badge on bottom-right of marble sphere for locked premium items
                                if (skin.tier == "premium" && !isUnlocked) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .offset(x = 2.dp, y = 2.dp)
                                            .size(19.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFB47C35))
                                            .border(1.2.dp, Color.White, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "🔒", fontSize = 9.sp)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(5.dp))

                            // 2. Free / Premium Badge Pill (Exact Image 2)
                            if (skin.tier == "premium" && !isUnlocked) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(999.dp))
                                        .background(Color(0xFFFDECC8))
                                        .padding(horizontal = 7.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "Premium",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF966010)
                                    )
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(999.dp))
                                        .background(Color(0xFFD4F4E2))
                                        .padding(horizontal = 7.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "Free",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF168B58)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(3.dp))

                            // 3. Name below the badge
                            Text(
                                text = skin.name,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF4A453E),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }

    // Rewarded Ad Confirmation Dialog for Locked Premium Marbles
    skinToUnlock?.let { skin ->
        AlertDialog(
            onDismissRequest = { skinToUnlock = null },
            shape = RoundedCornerShape(24.dp),
            containerColor = Color(0xFFFAF6EF),
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("✨", fontSize = 20.sp)
                    Text(
                        text = "Unlock ${skin.name}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E1B18)
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(4.dp))
                    MarbleBallPreview(
                        skin = skin,
                        size = 64.dp,
                        isSelected = true
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFFEF3C7))
                            .border(1.dp, Color(0xFFF59E0B), RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "💎 24-HOUR PREMIUM PASS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF92400E)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Watch a short sponsored video to unlock '${skin.name}' for 24 hours.\n\nThank you for supporting Digital Tasbih!",
                        fontSize = 13.sp,
                        color = Color(0xFF475569),
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val s = skinToUnlock
                        skinToUnlock = null
                        val activity = context.findActivity()
                        if (s != null && activity != null) {
                            AdMobRewardedManager.showAd(activity, "${s.name} Marble") {
                                TasbihRepository.unlockMarble24Hours(s.id)
                                TasbihRepository.setActiveBeadSkin(s.id)
                                onSelectSkin(s)
                                AppToast.show("${s.name} Unlocked for 24 Hours! 🌟")
                                onDismiss()
                            }
                        } else if (s != null) {
                            TasbihRepository.unlockMarble24Hours(s.id)
                            TasbihRepository.setActiveBeadSkin(s.id)
                            onSelectSkin(s)
                            AppToast.show("${s.name} Unlocked for 24 Hours! 🌟")
                            onDismiss()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0F5132),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Text(
                        text = "🎬 Watch Ad to Unlock",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { skinToUnlock = null },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF64748B))
                ) {
                    Text(
                        text = "Cancel",
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp
                    )
                }
            }
        )
    }
}

/**
 * 5. SPOTLIGHT TOUR WALKTHROUGH (Exact match for user screenshots media_1792304400570.png & media_1792304562729.png)
 * Step 1: Highlights [ Target: 33 ] [ बदलें ✎ ] row with rounded rect cutout. Downward arrow badge [↓] directly above it. Guide card above.
 * Step 2: Highlights the large Circular Counter Dial with circular cutout. Downward arrow badge [↓] directly above it. Guide card above.
 * The underlying screen components do NOT move or shift.
 */
@Composable
fun TasbihSpotlightTour(
    step: Int,
    targetBounds: Rect?,
    totalSteps: Int = 2,
    lang: String = "hi",
    onNext: () -> Unit,
    onDone: () -> Unit = onNext,
    onSkip: () -> Unit
) {
    val l = lang.lowercase().split("-")[0]
    val density = LocalDensity.current

    // Localized Strings matching user screenshots
    val stepBadge = if (totalSteps == 1) {
        when (l) {
            "hi" -> "क्विक टिप"
            "ur" -> "کوئیک گائیڈ"
            "ar" -> "إرشاد سريع"
            "bn" -> "কুইক গাইড"
            else -> "Quick Tip"
        }
    } else {
        when (l) {
            "hi" -> "कदम $step / $totalSteps"
            "ur" -> "مرحلہ $step / $totalSteps"
            "ar" -> "الخطوة $step / $totalSteps"
            "bn" -> "ধাপ $step / $totalSteps"
            else -> "Step $step / $totalSteps"
        }
    }

    val skipText = when (l) {
        "hi" -> "छोड़ें"
        "ur" -> "چھوڑیں"
        "ar" -> "تخطي"
        "bn" -> "এড়িয়ে যান"
        else -> "Skip"
    }

    val title = if (totalSteps == 1 || step == 2) {
        when (l) {
            "hi" -> "📿 तस्बीह काउंटर (Tap to Count)"
            "ur" -> "📿 تسبیح کاؤنٹر (Tap to Count)"
            "ar" -> "📿 عداد التسبيح (Tap to Count)"
            "bn" -> "📿 তসবিহ কাউন্টার (Tap to Count)"
            else -> "📿 Tasbih Counter (Tap to Count)"
        }
    } else {
        when (l) {
            "hi" -> "🎯 तस्बीह का लक्ष्य (Target)"
            "ur" -> "🎯 تسبیح کا ہدف (Target)"
            "ar" -> "🎯 هدف التسبيح (Target)"
            "bn" -> "🎯 তসবিহর লক্ষ্য (Target)"
            else -> "🎯 Tasbih Target"
        }
    }

    val description = if (totalSteps == 1 || step == 2) {
        when (l) {
            "hi" -> "यहाँ डायल पर कहीं भी टैप करके गिनें, Volume key से भी गिनती हो सकती है।"
            "ur" -> "یہاں ڈائل پر کہیں بھی ٹیپ کر کے گنیں، والیوم کی سے بھی گنتی ہو سکتی ہے۔"
            "ar" -> "اضغط هنا في أي مكان للعد، ويمكنك استخدام زر الصوت للعد أيضاً."
            "bn" -> "এখানে যেকোনো জায়গায় ট্যাপ করে গণনা করুন, ভলিউম কী দিয়েও গণনা করা যাবে।"
            else -> "Tap here to count, or use your phone's Volume Keys."
        }
    } else {
        when (l) {
            "hi" -> "यहाँ से आप अपनी तस्बीह का लक्ष्य (जैसे 33, 100 या मनचाहा नंबर) सेट कर सकते हैं ताकि गिनती आसान रहे।"
            "ur" -> "یہاں سے آپ اپنی تسبیح کا ہدف (جیسے 33، 100 یا اپنی پسند کا نمبر) مقرر کر سکتے ہیں تاکہ شمار آسان رہے۔"
            "ar" -> "يمكنك هنا تحديد هدف التسبيح (مثل 33 أو 100 أو رقم مخصص) لتسهيل العد ومتابعته."
            "bn" -> "এখান থেকে আপনি আপনার তসবিহর লক্ষ্য (যেমন ৩৩, ১০০ বা পছন্দের সংখ্যা) নির্ধারণ করতে পারেন যাতে গণনা সহজ হয়।"
            else -> "Set your tasbih target (like 33, 100 or custom) here so you can easily track your count."
        }
    }

    val nextBtnText = when (l) {
        "hi" -> "आगे बढ़ें ➔"
        "ur" -> "آگے بڑھیں ➔"
        "ar" -> "التالي ➔"
        "bn" -> "পরবর্তী ➔"
        else -> "Next ➔"
    }

    val doneBtnText = when (l) {
        "hi" -> "समझ गया! ✓"
        "ur" -> "سمجھ گیا! ✓"
        "ar" -> "فهمت! ✓"
        "bn" -> "বুঝেছি! ✓"
        else -> "Got it! ✓"
    }

    val cornerRadius = if (totalSteps == 1) 180f else if (step == 1) 14f else 180f
    val pad = if (totalSteps == 1) 8f else if (step == 1) 6f else 8f

    com.tasbih.digitaltasbih.ui.components.GeneralSpotlightTourModal(
        step = step,
        totalSteps = totalSteps,
        stepLabel = stepBadge,
        title = title,
        description = description,
        nextBtnText = nextBtnText,
        doneBtnText = doneBtnText,
        skipBtnText = skipText,
        targetRect = targetBounds,
        cornerRadiusDp = cornerRadius,
        padDp = pad,
        onNext = onNext,
        onDone = onDone,
        onSkip = onSkip
    )
}


