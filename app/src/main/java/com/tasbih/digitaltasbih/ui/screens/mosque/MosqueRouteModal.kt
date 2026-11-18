package com.tasbih.digitaltasbih.ui.screens.mosque

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.data.mosque.MosqueRouteInfo
import com.tasbih.digitaltasbih.data.mosque.NearbyMosque
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MosqueRouteModal(
    place: NearbyMosque,
    userLat: Double,
    userLon: Double,
    routeInfo: MosqueRouteInfo?,
    isLoadingRoute: Boolean,
    isDrivingMode: Boolean,
    onToggleRouteMode: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFFF9F7F2),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .padding(bottom = 28.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    // Icon Circle
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(
                                if (place.isHalal) Color(0xFFDC2626) else Color(0xFF0D4A3E),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (place.isHalal) "🍽️" else "🕌",
                            fontSize = 18.sp
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = place.name,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0D4A3E),
                            maxLines = 1
                        )
                        Text(
                            text = place.address,
                            fontSize = 12.sp,
                            color = Color(0xFF64748B),
                            maxLines = 1
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color.White, CircleShape)
                        .border(1.dp, Color(0xFFE2E8F0), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color(0xFF0D4A3E),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Stats Banner
            val distMeters = routeInfo?.distanceMeters?.toInt() ?: place.distanceMeters
            val distText = if (distMeters >= 1000) {
                String.format("%.1f km", distMeters / 1000.0)
            } else {
                "$distMeters m"
            }

            val estTimeMin = if (routeInfo != null) {
                max(1, (routeInfo.durationSeconds / 60.0).toInt())
            } else {
                if (isDrivingMode) place.estimatedDriveMinutes else place.estimatedWalkMinutes
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0xFFC9A84C).copy(alpha = 0.35f), RoundedCornerShape(16.dp))
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatItem(label = "TOTAL DISTANCE", value = distText, icon = "📏")
                Divider(modifier = Modifier.height(30.dp).width(1.dp), color = Color(0xFFE2E8F0))
                StatItem(
                    label = if (isDrivingMode) "DRIVE TIME" else "WALK TIME",
                    value = "~$estTimeMin mins",
                    icon = if (isDrivingMode) "🚗" else "🚶"
                )
                Divider(modifier = Modifier.height(30.dp).width(1.dp), color = Color(0xFFE2E8F0))
                StatItem(label = "STATUS", value = if (place.isHalal) "Halal" else "Open", icon = "✅")
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Route Type Selector: Shortest (Walking) vs Fastest (Driving)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEFECE2), RoundedCornerShape(12.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Shortest Route (Walk)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (!isDrivingMode) Color(0xFF0D4A3E) else Color.Transparent)
                        .clickable { onToggleRouteMode(false) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = "🚶", fontSize = 13.sp)
                        Text(
                            text = "Shortest Route (Walk)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (!isDrivingMode) Color.White else Color(0xFF5C5240)
                        )
                    }
                }

                // Fastest Route (Drive)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isDrivingMode) Color(0xFF0D4A3E) else Color.Transparent)
                        .clickable { onToggleRouteMode(true) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = "🚗", fontSize = 13.sp)
                        Text(
                            text = "Fastest Route (Drive)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDrivingMode) Color.White else Color(0xFF5C5240)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Turn-by-Turn Steps or Loading Indicator
            if (isLoadingRoute) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(Color.White, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Color(0xFF0D4A3E), modifier = Modifier.size(28.dp), strokeWidth = 2.5.dp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Calculating ${if (isDrivingMode) "fastest driving" else "shortest walking"} route...", fontSize = 12.sp, color = Color(0xFF5C5240))
                    }
                }
            } else if (routeInfo != null && routeInfo.steps.isNotEmpty()) {
                Text(
                    text = "ROAD DIRECTIONS (${routeInfo.steps.size} STEPS)",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8C6B1F),
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 160.dp)
                ) {
                    itemsIndexed(routeInfo.steps) { index, step ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .background(Color(0xFF0D4A3E), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${index + 1}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFF3D48A)
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = step.instruction,
                                fontSize = 13.sp,
                                color = Color(0xFF1F2937),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons: Live Turn-by-Turn GPS Navigation & Share
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Primary: Start GPS Navigation in Google Maps
                Button(
                    onClick = {
                        val navMode = if (isDrivingMode) "d" else "w"
                        val navUri = Uri.parse("google.navigation:q=${place.lat},${place.lon}&mode=$navMode")
                        val navIntent = Intent(Intent.ACTION_VIEW, navUri).apply {
                            setPackage("com.google.android.apps.maps")
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        try {
                            context.startActivity(navIntent)
                        } catch (_: Exception) {
                            val geoUri = Uri.parse("geo:${place.lat},${place.lon}?q=${place.lat},${place.lon}(${Uri.encode(place.name)})")
                            context.startActivity(Intent(Intent.ACTION_VIEW, geoUri).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK })
                        }
                    },
                    modifier = Modifier.weight(1f).height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D4A3E)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(imageVector = Icons.Default.Navigation, contentDescription = null, tint = Color(0xFFF3D48A), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Start Live GPS", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                // Share Button
                OutlinedButton(
                    onClick = {
                        val shareText = "Location: ${place.name}\n${place.address}\nhttps://maps.google.com/?q=${place.lat},${place.lon}"
                        val sendIntent = Intent(Intent.ACTION_SEND).apply {
                            putExtra(Intent.EXTRA_TEXT, shareText)
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, "Share Location"))
                    },
                    modifier = Modifier.height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF0D4A3E))
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String, icon: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = icon, fontSize = 13.sp)
            Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D4A3E))
        }
        Text(text = label, fontSize = 9.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF64748B))
    }
}
