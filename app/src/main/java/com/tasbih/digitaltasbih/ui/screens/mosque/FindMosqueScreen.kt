package com.tasbih.digitaltasbih.ui.screens.mosque

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.tasbih.digitaltasbih.data.HalalFoodItem
import com.tasbih.digitaltasbih.data.MosqueItem
import com.tasbih.digitaltasbih.data.NearMeRepository
import com.tasbih.digitaltasbih.data.PrayerRepository
import com.tasbih.digitaltasbih.data.mosque.MosqueRepository
import com.tasbih.digitaltasbih.data.mosque.MosqueRouteInfo
import com.tasbih.digitaltasbih.data.mosque.NearbyMosque
import com.tasbih.digitaltasbih.util.MapHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindMosqueScreen(
    onBack: () -> Unit
) {
    BackHandler {
        onBack()
    }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val prayerSettings = remember {
        try {
            PrayerRepository.loadSettings()
        } catch (_: Exception) {
            null
        }
    }

    var userLat by remember { mutableStateOf(prayerSettings?.latitude ?: 21.0970) }
    var userLon by remember { mutableStateOf(prayerSettings?.longitude ?: 81.0374) }
    var hasRealGps by remember { mutableStateOf(false) }
    var isPermissionDenied by remember { mutableStateOf(false) }

    var selectedRadiusKm by remember { mutableStateOf(5.0) }
    var isHalalMode by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var useGoogleMapsSdk by remember { mutableStateOf(true) }

    // Places and Route state
    var placesList by remember { mutableStateOf<List<NearbyMosque>>(emptyList()) }
    var isLoadingPlaces by remember { mutableStateOf(false) }
    var selectedPlace by remember { mutableStateOf<NearbyMosque?>(null) }
    var routeInfo by remember { mutableStateOf<MosqueRouteInfo?>(null) }
    var isLoadingRoute by remember { mutableStateOf(false) }
    var isDrivingMode by remember { mutableStateOf(false) } // false = Shortest (Walk), true = Fastest (Drive)

    val cityName = remember(prayerSettings) {
        prayerSettings?.locationLabel?.split(",")?.firstOrNull()?.trim()
            ?.takeIf { it.isNotBlank() && it != "Current Location" } ?: "Your Area"
    }

    // Function to calculate route from user's current position to selected place
    fun calculateRoute(dest: NearbyMosque, driving: Boolean) {
        selectedPlace = dest
        isDrivingMode = driving
        isLoadingRoute = true
        coroutineScope.launch {
            val route = MosqueRepository.getRoute(
                startLat = userLat,
                startLon = userLon,
                destLat = dest.lat,
                destLon = dest.lon,
                isDriving = driving
            )
            routeInfo = route
            isLoadingRoute = false
        }
    }

    // Function to refresh places using unified NearMeRepository (OSM Live + Overture Offline + Dedup)
    fun loadPlaces() {
        isLoadingPlaces = true
        coroutineScope.launch {
            try {
                val places = if (isHalalMode) {
                    val halal = NearMeRepository.getHalalFood(userLat, userLon, selectedRadiusKm)
                    halal.map { it.toNearbyMosque() }
                } else {
                    val mosques = NearMeRepository.getMosques(context, userLat, userLon, selectedRadiusKm)
                    mosques.map { it.toNearbyMosque() }
                }
                placesList = places
            } catch (_: Exception) {
            } finally {
                isLoadingPlaces = false
            }
        }
    }

    // Location Permission Launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (fineGranted || coarseGranted) {
            isPermissionDenied = false
            startContinuousGpsTracking(context) { loc ->
                userLat = loc.latitude
                userLon = loc.longitude
                hasRealGps = true

                // Auto recalculate route if navigation is active and user moved
                selectedPlace?.let { sel ->
                    if (routeInfo != null) {
                        calculateRoute(sel, isDrivingMode)
                    }
                }
            }
            loadPlaces()
        } else {
            isPermissionDenied = true
        }
    }

    // Initial check on mount
    LaunchedEffect(Unit) {
        val fineGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarseGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fineGranted || coarseGranted) {
            startContinuousGpsTracking(context) { loc ->
                userLat = loc.latitude
                userLon = loc.longitude
                hasRealGps = true

                selectedPlace?.let { sel ->
                    if (routeInfo != null) {
                        calculateRoute(sel, isDrivingMode)
                    }
                }
            }
            loadPlaces()
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // Trigger places reload when radius, mode, or coordinates change
    LaunchedEffect(selectedRadiusKm, isHalalMode, userLat, userLon) {
        loadPlaces()
    }

    Scaffold(
        topBar = {
            Surface(
                color = Color(0xFFFAF8F4),
                shadowElevation = 4.dp,
                border = BorderStroke(1.dp, Color(0xFFE8E2D2)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Solid Luxury Back Button
                        Surface(
                            onClick = onBack,
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0xFFF3EFE6),
                            border = BorderStroke(1.2.dp, Color(0xFFC9A84C).copy(alpha = 0.5f)),
                            shadowElevation = 2.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color(0xFF0D4A3E),
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "Back",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0D4A3E)
                                )
                            }
                        }

                        // Centered Title with Gold Accent Bar
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (isHalalMode) "NEARBY HALAL FOOD" else "NEARBY MOSQUE",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0D4A3E),
                                fontFamily = FontFamily.Serif,
                                letterSpacing = 0.8.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Box(
                                modifier = Modifier
                                    .width(44.dp)
                                    .height(2.5.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(Color(0xFFC9A84C))
                            )
                        }

                        // Balance Spacer to keep title perfectly centered
                        Spacer(modifier = Modifier.width(76.dp))
                    }
                }
            }
        },
        containerColor = Color(0xFFFAF8F4)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // ── 1. MAP ENGINE (Google Maps SDK or 100% Free OSMDroid Fallback) ──
            val filteredPlaces = remember(placesList, searchQuery) {
                if (searchQuery.isBlank()) {
                    placesList
                } else {
                    val q = searchQuery.trim().lowercase()
                    placesList.filter { it.name.lowercase().contains(q) || it.address.lowercase().contains(q) }
                }
            }

            // ── 1. MAP ENGINE: MapLibre Native with OpenFreeMap Liberty style ──
            MapLibreMosqueView(
                userLat = userLat,
                userLon = userLon,
                places = filteredPlaces,
                selectedPlace = selectedPlace,
                routePoints = routeInfo?.polylinePoints ?: emptyList(),
                onPlaceClick = { place ->
                    calculateRoute(place, isDrivingMode)
                },
                modifier = Modifier.fillMaxSize()
            )

            // ── 2. FLOATING TOP BAR & CONTROLS ──
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Clean Map Search Bar (Search mosque or halal food)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(14.dp))
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0xFFC9A84C).copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                        .padding(horizontal = 14.dp, vertical = 9.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color(0xFF8C6B1F),
                            modifier = Modifier.size(18.dp)
                        )

                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            textStyle = TextStyle(
                                fontSize = 13.5.sp,
                                color = Color(0xFF1F2937),
                                fontWeight = FontWeight.Medium
                            ),
                            cursorBrush = SolidColor(Color(0xFF0D4A3E)),
                            decorationBox = { innerTextField ->
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        text = if (isHalalMode) "Search halal food or area..." else "Search mosque by name or area...",
                                        fontSize = 13.sp,
                                        color = Color(0xFF94A3B8)
                                    )
                                }
                                innerTextField()
                            }
                        )

                        if (searchQuery.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear",
                                tint = Color(0xFF64748B),
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable { searchQuery = "" }
                            )
                        }

                        if (isLoadingPlaces) {
                            CircularProgressIndicator(
                                color = Color(0xFF0D4A3E),
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                // Controls Row: Distance Filter Pills (2km, 5km, 10km, 15km) & Halal Food Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Distance Radius Pills
                    Row(
                        modifier = Modifier
                            .weight(1f, fill = false)
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(2.0, 5.0, 10.0, 15.0).forEach { radius ->
                            val isSelected = selectedRadiusKm == radius
                            Surface(
                                onClick = { selectedRadiusKm = radius },
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) Color(0xFF0D4A3E) else Color.White,
                                shadowElevation = 3.dp,
                                border = BorderStroke(1.dp, if (isSelected) Color(0xFF0D4A3E) else Color(0xFFC9A84C).copy(alpha = 0.4f))
                            ) {
                                Box(
                                    modifier = Modifier.padding(horizontal = 11.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "${radius.toInt()} km",
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                        color = if (isSelected) Color.White else Color(0xFF1F2937)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Halal Food Button (Floating right below search bar)
                    Surface(
                        onClick = {
                            isHalalMode = !isHalalMode
                            selectedPlace = null
                            routeInfo = null
                        },
                        shape = RoundedCornerShape(20.dp),
                        color = if (isHalalMode) Color(0xFFDC2626) else Color.White,
                        shadowElevation = 4.dp,
                        border = BorderStroke(1.2.dp, if (isHalalMode) Color(0xFFFCA5A5) else Color(0xFF0D4A3E))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text(
                                text = if (isHalalMode) "🕌 Mosques" else "🍽️ Halal Food",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isHalalMode) Color.White else Color(0xFF0D4A3E)
                            )
                        }
                    }
                }

                // Permission Warning Banner if GPS Denied
                AnimatedVisibility(visible = isPermissionDenied) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFEF2F2),
                        border = BorderStroke(1.dp, Color(0xFFF87171)),
                        shadowElevation = 2.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "⚠️ Location permission denied. Using estimated location.",
                                fontSize = 11.sp,
                                color = Color(0xFF991B1B)
                            )
                            Text(
                                text = "Allow",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFDC2626),
                                modifier = Modifier.clickable {
                                    permissionLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }

            // ── 3. HALAL FOOD EMPTY STATE WITH OSM REPORT BUTTON ──
            if (isHalalMode && filteredPlaces.isEmpty() && !isLoadingPlaces) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    shadowElevation = 6.dp,
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("🍽️", fontSize = 28.sp)
                        Text(
                            text = "No Reported Halal Outlets",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937)
                        )
                        Text(
                            text = "No restaurants with confirmed 'diet:halal' tag found within ${selectedRadiusKm.toInt()} km in OpenStreetMap.",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Text(
                            text = "Reported halal (not certified) · Zero AI guessing",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF8C6B1F)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Button(
                            onClick = {
                                val osmAddUrl = "https://www.openstreetmap.org/note/new#map=16/$userLat/$userLon"
                                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(osmAddUrl))
                                context.startActivity(intent)
                            },
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D4A3E))
                        ) {
                            Text(
                                text = "Add / Report on OpenStreetMap",
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // ── 4. SMART NAVIGATION ROUTE MODAL (Bottom Sheet) ──
            selectedPlace?.let { place ->
                MosqueRouteModal(
                    place = place,
                    userLat = userLat,
                    userLon = userLon,
                    routeInfo = routeInfo,
                    isLoadingRoute = isLoadingRoute,
                    isDrivingMode = isDrivingMode,
                    onToggleRouteMode = { driving ->
                        calculateRoute(place, driving)
                    },
                    onDismiss = {
                        selectedPlace = null
                        routeInfo = null
                    }
                )
            }
        }
    }
}

/**
 * Continuous GPS Location Tracking.
 */
@SuppressLint("MissingPermission")
private fun startContinuousGpsTracking(context: Context, onLocation: (Location) -> Unit) {
    try {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager ?: return
        val gpsLoc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val netLoc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        val best = gpsLoc ?: netLoc
        if (best != null) {
            onLocation(best)
        }

        val listener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                // Filter out default Android Studio emulator mock location in California (37.42, -122.08)
                val isDefaultEmulator = Math.abs(location.latitude - 37.42) < 0.2 && Math.abs(location.longitude - (-122.08)) < 0.2
                if (!isDefaultEmulator) {
                    onLocation(location)
                }
            }
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        lm.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            4000L,
            15f,
            listener,
            Looper.getMainLooper()
        )
        lm.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            6000L,
            25f,
            listener,
            Looper.getMainLooper()
        )
    } catch (_: Exception) {}
}

private fun MosqueItem.toNearbyMosque(): NearbyMosque {
    val dist = distanceMeters.toInt()
    val walkMins = kotlin.math.max(1, (dist / 83.0).toInt())
    val driveMins = kotlin.math.max(1, (dist / 500.0).toInt())
    return NearbyMosque(
        id = id,
        name = name,
        address = address ?: "Nearby Mosque",
        lat = latitude,
        lon = longitude,
        distanceMeters = dist,
        bearingDegrees = 0f,
        estimatedWalkMinutes = walkMins,
        estimatedDriveMinutes = driveMins,
        isHalal = false,
        placeType = "mosque"
    )
}

private fun HalalFoodItem.toNearbyMosque(): NearbyMosque {
    val dist = distanceMeters.toInt()
    val walkMins = kotlin.math.max(1, (dist / 83.0).toInt())
    val driveMins = kotlin.math.max(1, (dist / 500.0).toInt())
    return NearbyMosque(
        id = id,
        name = name,
        address = address ?: cuisine ?: "Reported halal (not certified)",
        lat = latitude,
        lon = longitude,
        distanceMeters = dist,
        bearingDegrees = 0f,
        estimatedWalkMinutes = walkMins,
        estimatedDriveMinutes = driveMins,
        isHalal = true,
        placeType = "halal"
    )
}
