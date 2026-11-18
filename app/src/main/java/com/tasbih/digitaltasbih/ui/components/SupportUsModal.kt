package com.tasbih.digitaltasbih.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

private const val SUPPORT_UPI_ID = "digitaltasbih@upi"
private const val SUPPORT_UPI_NAME = "Digital Tasbih"

/**
 * Luxury Indian UPI Support Us Modal.
 * Matches original HTML openSupportUs() with direct Intent chooser
 * for GPay, PhonePe, Paytm, BHIM and Clipboard fallback.
 */
@Composable
fun SupportUsModal(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var selectedAmount by remember { mutableStateOf("100") }
    val amounts = listOf("50", "100", "250", "500")

    fun launchUpiPayment(amt: String) {
        val upiUri = Uri.Builder()
            .scheme("upi")
            .authority("pay")
            .appendQueryParameter("pa", SUPPORT_UPI_ID)
            .appendQueryParameter("pn", SUPPORT_UPI_NAME)
            .appendQueryParameter("cu", "INR")
            .appendQueryParameter("tn", "Support Digital Tasbih App")
            .let { builder ->
                if (amt.isNotBlank() && amt != "0") builder.appendQueryParameter("am", amt) else builder
            }
            .build()

        val intent = Intent(Intent.ACTION_VIEW, upiUri)
        try {
            val chooser = Intent.createChooser(intent, "Choose UPI App (GPay / PhonePe / Paytm)")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)
        } catch (_: Exception) {
            // Fallback: Copy UPI ID to clipboard
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText("UPI ID", SUPPORT_UPI_ID))
            Toast.makeText(
                context,
                "💛 UPI ID copy ho gayi: $SUPPORT_UPI_ID — apni UPI app me paste karke support karein",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.76f))
                .clickable { onDismiss() }
                .padding(horizontal = 20.dp, vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable(enabled = false) {} // Prevent click-through
                    .shadow(28.dp, RoundedCornerShape(26.dp), spotColor = Color(0xFFF3D48A))
                    .clip(RoundedCornerShape(26.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF102E24),
                                Color(0xFF0A2018),
                                Color(0xFF04120D)
                            )
                        )
                    )
                    .border(1.5.dp, Color(0xFFF3D48A), RoundedCornerShape(26.dp))
                    .padding(22.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Top Bar: Heart badge + Close
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x33F3D48A))
                                .border(1.dp, Color(0x66F3D48A), RoundedCornerShape(12.dp))
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = "❤️ SADAQAH JARIYAH",
                                color = Color(0xFFF3D48A),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0x33FFFFFF))
                                .clickable { onDismiss() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // Icon and Title
                    Text(
                        text = "🤲",
                        fontSize = 38.sp
                    )

                    Text(
                        text = "Support Digital Tasbih",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Serif,
                        color = Color(0xFFF3D48A),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Digital Tasbih is built with pure devotion as an authentic, ad-free Islamic tool. Your small contribution helps keep servers running and features free for Muslims worldwide.",
                        fontSize = 13.sp,
                        color = Color(0xFFD1D5DB),
                        textAlign = TextAlign.Center,
                        lineHeight = 19.sp
                    )

                    // Hadith Quote Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x1AF3D48A))
                            .border(1.dp, Color(0x44F3D48A), RoundedCornerShape(14.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "\"مَا نَقَصَتْ صَدَقَةٌ مِنْ مَالٍ\"\n\"Charity does not decrease wealth.\"\n— Sahih Muslim 2588",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFF3D48A),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Amount Selection Chips
                    Text(
                        text = "Select Contribution (₹ INR):",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8),
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        amounts.forEach { amt ->
                            val isSelected = selectedAmount == amt
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (isSelected) Color(0xFFF3D48A) else Color(0x22F3D48A)
                                    )
                                    .border(
                                        1.dp,
                                        if (isSelected) Color(0xFFF3D48A) else Color(0x55F3D48A),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable { selectedAmount = amt }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "₹$amt",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isSelected) Color(0xFF0E2E22) else Color(0xFFF3D48A)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Primary Action Button (Direct UPI Intent)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(8.dp, RoundedCornerShape(18.dp), spotColor = Color(0xFFF3D48A))
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFFF3D48A), Color(0xFFD8A846))
                                )
                            )
                            .clickable { launchUpiPayment(selectedAmount) }
                            .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "⚡", fontSize = 16.sp)
                            Text(
                                text = "Pay ₹$selectedAmount via UPI",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF0A2219)
                            )
                        }
                    }

                    // Copy UPI ID button
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboard.setPrimaryClip(ClipData.newPlainText("UPI ID", SUPPORT_UPI_ID))
                                Toast.makeText(
                                    context,
                                    "✓ UPI ID copied: $SUPPORT_UPI_ID",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📋 Copy UPI ID: $SUPPORT_UPI_ID",
                            fontSize = 12.sp,
                            color = Color(0xFF94A3B8),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
