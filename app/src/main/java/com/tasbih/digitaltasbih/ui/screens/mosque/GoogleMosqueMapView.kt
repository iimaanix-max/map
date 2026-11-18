package com.tasbih.digitaltasbih.ui.screens.mosque

import android.content.Context
import android.graphics.Color as AndroidColor
import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.*
import com.tasbih.digitaltasbih.data.mosque.NearbyMosque

@Composable
fun GoogleMosqueMapView(
    userLat: Double,
    userLon: Double,
    places: List<NearbyMosque>,
    selectedPlace: NearbyMosque?,
    routePoints: List<Pair<Double, Double>>,
    onPlaceClick: (NearbyMosque) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val mapView = remember {
        MapView(context).apply {
            onCreate(Bundle())
        }
    }

    var googleMapInstance by remember { mutableStateOf<GoogleMap?>(null) }
    var isSatelliteView by remember { mutableStateOf(false) }

    // Manage MapView Lifecycle to avoid leaks
    DisposableEffect(lifecycleOwner, mapView) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> Unit
            }
        }
        val current = lifecycleOwner.lifecycle.currentState
        if (current.isAtLeast(Lifecycle.State.STARTED)) {
            mapView.onStart()
        }
        if (current.isAtLeast(Lifecycle.State.RESUMED)) {
            mapView.onResume()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            try {
                mapView.onPause()
                mapView.onStop()
                mapView.onDestroy()
            } catch (_: Exception) {}
        }
    }

    val markersMap = remember { mutableMapOf<String, Marker>() }

    Box(modifier = modifier) {
        AndroidView(
            factory = {
                mapView.apply {
                    getMapAsync { gMap ->
                        googleMapInstance = gMap
                        gMap.uiSettings.isZoomControlsEnabled = false
                        gMap.uiSettings.isCompassEnabled = true
                        gMap.uiSettings.isMyLocationButtonEnabled = false
                        gMap.uiSettings.isMapToolbarEnabled = false
                        gMap.mapType = if (isSatelliteView) GoogleMap.MAP_TYPE_HYBRID else GoogleMap.MAP_TYPE_NORMAL

                        // Pure Clean Map Style: Completely disable all general POIs (hospitals, schools, shops, cafes)
                        val cleanPoiStyle = """
                        [
                          {
                            "featureType": "poi",
                            "elementType": "all",
                            "stylers": [
                              { "visibility": "off" }
                            ]
                          },
                          {
                            "featureType": "transit",
                            "elementType": "labels.icon",
                            "stylers": [
                              { "visibility": "off" }
                            ]
                          }
                        ]
                        """.trimIndent()
                        try {
                            gMap.setMapStyle(MapStyleOptions(cleanPoiStyle))
                        } catch (_: Exception) {}

                        // Center on User initial location
                        val userLatLng = LatLng(userLat, userLon)
                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f))

                        try {
                            val hasFine = androidx.core.content.ContextCompat.checkSelfPermission(
                                context,
                                android.Manifest.permission.ACCESS_FINE_LOCATION
                            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                            val hasCoarse = androidx.core.content.ContextCompat.checkSelfPermission(
                                context,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION
                            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                            if (hasFine || hasCoarse) {
                                gMap.isMyLocationEnabled = true
                            }
                        } catch (_: SecurityException) {}

                        // Marker Click Listener
                        gMap.setOnMarkerClickListener { marker ->
                            val place = places.find { it.id == marker.tag }
                            if (place != null) {
                                onPlaceClick(place)
                                true
                            } else {
                                false
                            }
                        }

                        // Map POI Click Listener
                        gMap.setOnPoiClickListener { poi ->
                            val dist = com.tasbih.digitaltasbih.data.mosque.MosqueRepository.calculateHaversineDistanceMeters(
                                userLat, userLon, poi.latLng.latitude, poi.latLng.longitude
                            )
                            val walkMin = kotlin.math.max(1, (dist / 1.25 / 60.0).toInt())
                            val driveMin = kotlin.math.max(1, (dist / 8.33 / 60.0).toInt())
                            val tappedPlace = NearbyMosque(
                                id = "poi_${poi.placeId}",
                                name = poi.name,
                                address = "Google Maps Location",
                                lat = poi.latLng.latitude,
                                lon = poi.latLng.longitude,
                                distanceMeters = dist.toInt(),
                                bearingDegrees = com.tasbih.digitaltasbih.data.mosque.MosqueRepository.calculateBearingDegrees(
                                    userLat, userLon, poi.latLng.latitude, poi.latLng.longitude
                                ),
                                estimatedWalkMinutes = walkMin,
                                estimatedDriveMinutes = driveMin,
                                isHalal = false,
                                placeType = "mosque"
                            )
                            onPlaceClick(tappedPlace)
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Update Map Markers & Polylines when data changes
        LaunchedEffect(googleMapInstance, places, selectedPlace, routePoints, isSatelliteView) {
            val map = googleMapInstance ?: return@LaunchedEffect

            map.mapType = if (isSatelliteView) GoogleMap.MAP_TYPE_HYBRID else GoogleMap.MAP_TYPE_NORMAL

            // Clear old markers & polylines
            map.clear()
            markersMap.clear()

            // 1. Add User Custom Location Marker
            val userLatLng = LatLng(userLat, userLon)
            val userIcon = BitmapDescriptorFactory.fromBitmap(CustomMarkerHelper.getUserLocationBitmap(context))
            map.addMarker(
                MarkerOptions()
                    .position(userLatLng)
                    .title("Your Location")
                    .anchor(0.5f, 0.5f)
                    .icon(userIcon)
            )

            // 2. Add Custom Mosque / Halal Food Markers
            places.forEach { place ->
                val pos = LatLng(place.lat, place.lon)
                val isSelected = selectedPlace?.id == place.id
                val distStr = if (place.distanceMeters >= 1000) String.format("%.1f km", place.distanceMeters / 1000.0) else "${place.distanceMeters} m"

                val markerDescriptor = CustomMarkerHelper.getGoogleMapMarkerDescriptor(
                    context = context,
                    name = place.name,
                    distanceStr = distStr,
                    isSelected = isSelected,
                    isHalal = place.isHalal
                )

                val marker = map.addMarker(
                    MarkerOptions()
                        .position(pos)
                        .title(place.name)
                        .snippet("$distStr · ${place.estimatedWalkMinutes} min walk")
                        .anchor(0.5f, 0.95f)
                        .icon(markerDescriptor)
                )
                if (marker != null) {
                    marker.tag = place.id
                    markersMap[place.id] = marker
                }
            }

            // 3. Draw Route Polyline if route is selected
            if (selectedPlace != null && routePoints.isNotEmpty()) {
                val polylinePoints = routePoints.map { LatLng(it.first, it.second) }

                // Outer Glowing Green Line
                map.addPolyline(
                    PolylineOptions()
                        .addAll(polylinePoints)
                        .color(AndroidColor.parseColor("#4410B981"))
                        .width(18f)
                        .geodesic(true)
                )

                // Inner Crisp Emerald Line
                map.addPolyline(
                    PolylineOptions()
                        .addAll(polylinePoints)
                        .color(AndroidColor.parseColor("#10B981"))
                        .width(9f)
                        .geodesic(true)
                )

                // Fit both User and Destination in camera
                try {
                    val boundsBuilder = LatLngBounds.builder()
                    boundsBuilder.include(userLatLng)
                    polylinePoints.forEach { boundsBuilder.include(it) }
                    val bounds = boundsBuilder.build()
                    val padding = 130
                    map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
                } catch (_: Exception) {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(selectedPlace.lat, selectedPlace.lon), 15f))
                }
            } else if (selectedPlace != null) {
                // Animate to destination
                val destLatLng = LatLng(selectedPlace.lat, selectedPlace.lon)
                try {
                    val boundsBuilder = LatLngBounds.builder()
                    boundsBuilder.include(userLatLng)
                    boundsBuilder.include(destLatLng)
                    val bounds = boundsBuilder.build()
                    map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 130))
                } catch (_: Exception) {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(destLatLng, 15f))
                }
            }
        }

        // Floating Map Controls (Satellite Toggle & Recenter on User)
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 115.dp, end = 14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Satellite / Normal Toggle Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFC9A84C).copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                    .clickable { isSatelliteView = !isSatelliteView }
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Layers,
                        contentDescription = "Map Style",
                        tint = Color(0xFF0D4A3E),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = if (isSatelliteView) "Satellite" else "Road",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0D4A3E)
                    )
                }
            }

            // Recenter on User Button
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(1.dp, Color(0xFFC9A84C).copy(alpha = 0.5f), CircleShape)
                    .clickable {
                        googleMapInstance?.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(LatLng(userLat, userLon), 15.5f)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "My Location",
                    tint = Color(0xFF0D4A3E),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
