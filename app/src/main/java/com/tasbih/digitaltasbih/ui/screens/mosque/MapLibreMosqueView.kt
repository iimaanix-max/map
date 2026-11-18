package com.tasbih.digitaltasbih.ui.screens.mosque

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.tasbih.digitaltasbih.data.mosque.NearbyMosque
import org.maplibre.android.MapLibre
import org.maplibre.android.annotations.IconFactory
import org.maplibre.android.annotations.Marker
import org.maplibre.android.annotations.MarkerOptions
import org.maplibre.android.annotations.Polyline
import org.maplibre.android.annotations.PolylineOptions
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style

/**
 * Native MapLibre View using OpenFreeMap Liberty vector tiles style (₹0 cost, open source).
 * Renders authentic mosque markers (green), halal food markers (red), user location blue dot,
 * and navigation polyline preview.
 */
@Composable
fun MapLibreMosqueView(
    userLat: Double,
    userLon: Double,
    places: List<NearbyMosque>,
    selectedPlace: NearbyMosque?,
    routePoints: List<Pair<Double, Double>> = emptyList(),
    onPlaceClick: (NearbyMosque) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Initialize MapLibre engine once
    remember {
        MapLibre.getInstance(context)
    }

    val mapView = remember {
        MapView(context).apply {
            onCreate(Bundle())
        }
    }

    var mapLibreMap by remember { mutableStateOf<MapLibreMap?>(null) }
    var currentStyle by remember { mutableStateOf<Style?>(null) }
    var userLocationMarker by remember { mutableStateOf<Marker?>(null) }
    val markerPlaceMap = remember { mutableMapOf<Marker, NearbyMosque>() }
    var currentPolyline by remember { mutableStateOf<Polyline?>(null) }

    // User Location Blue Dot Icon
    val userBlueDotIcon = remember(context) {
        val density = context.resources.displayMetrics.density
        val size = (24 * density).toInt()
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Outer glow
        val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.parseColor("#442196F3")
            style = Paint.Style.FILL
        }
        canvas.drawCircle(size / 2f, size / 2f, 11f * density, glowPaint)

        // White border
        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.WHITE
            style = Paint.Style.FILL
        }
        canvas.drawCircle(size / 2f, size / 2f, 8f * density, borderPaint)

        // Blue core
        val corePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.parseColor("#1976D2")
            style = Paint.Style.FILL
        }
        canvas.drawCircle(size / 2f, size / 2f, 6f * density, corePaint)

        @Suppress("DEPRECATION")
        IconFactory.getInstance(context).fromBitmap(bitmap)
    }

    // Lifecycle handling for MapView
    DisposableEffect(lifecycleOwner, mapView) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Box(modifier = modifier) {
        AndroidView(
            factory = {
                mapView.apply {
                    getMapAsync { map ->
                        mapLibreMap = map
                        // OpenFreeMap Liberty Vector Style
                        map.setStyle(
                            Style.Builder().fromUri("https://tiles.openfreemap.org/styles/liberty")
                        ) { style ->
                            currentStyle = style
                            map.uiSettings.isLogoEnabled = false
                            map.uiSettings.isAttributionEnabled = false
                            map.uiSettings.isRotateGesturesEnabled = true
                            map.uiSettings.isTiltGesturesEnabled = false

                            map.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(userLat, userLon),
                                    14.0
                                )
                            )
                        }

                        @Suppress("DEPRECATION")
                        map.setOnMarkerClickListener { clickedMarker ->
                            val item = markerPlaceMap[clickedMarker]
                            if (item != null) {
                                onPlaceClick(item)
                                true
                            } else {
                                false
                            }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Floating Recenter FAB
        FloatingActionButton(
            onClick = {
                mapLibreMap?.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(LatLng(userLat, userLon), 14.5),
                    600
                )
            },
            shape = CircleShape,
            containerColor = Color.White,
            contentColor = Color(0xFF0D4A3E),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = if (selectedPlace != null) 240.dp else 24.dp)
                .size(46.dp)
        ) {
            Icon(
                imageVector = Icons.Default.MyLocation,
                contentDescription = "My Location",
                modifier = Modifier.size(22.dp)
            )
        }
    }

    // Update user location blue dot and camera position
    LaunchedEffect(mapLibreMap, currentStyle, userLat, userLon) {
        val map = mapLibreMap ?: return@LaunchedEffect
        if (currentStyle == null) return@LaunchedEffect

        @Suppress("DEPRECATION")
        userLocationMarker?.let { map.removeMarker(it) }

        @Suppress("DEPRECATION")
        val marker = map.addMarker(
            MarkerOptions()
                .position(LatLng(userLat, userLon))
                .title("My Location")
                .icon(userBlueDotIcon)
        )
        userLocationMarker = marker

        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(LatLng(userLat, userLon), 13.5),
            700
        )
    }

    // Update place markers
    LaunchedEffect(mapLibreMap, currentStyle, places, selectedPlace) {
        val map = mapLibreMap ?: return@LaunchedEffect
        if (currentStyle == null) return@LaunchedEffect

        @Suppress("DEPRECATION")
        for (m in markerPlaceMap.keys) {
            map.removeMarker(m)
        }
        markerPlaceMap.clear()

        @Suppress("DEPRECATION")
        val iconFactory = IconFactory.getInstance(context)

        for (place in places) {
            val isSelected = selectedPlace?.id == place.id
            val distStr = if (place.distanceMeters > 0) {
                if (place.distanceMeters >= 1000) {
                    "%.1f km".format(place.distanceMeters / 1000f)
                } else {
                    "${place.distanceMeters} m"
                }
            } else ""

            val markerBitmap = CustomMarkerHelper.getMosqueMarkerBitmap(
                context = context,
                name = place.name,
                distanceStr = distStr,
                isSelected = isSelected,
                isHalal = place.isHalal
            )
            @Suppress("DEPRECATION")
            val icon = iconFactory.fromBitmap(markerBitmap)

            @Suppress("DEPRECATION")
            val marker = map.addMarker(
                MarkerOptions()
                    .position(LatLng(place.lat, place.lon))
                    .title(place.name)
                    .icon(icon)
            )
            markerPlaceMap[marker] = place
        }
    }

    // Update route polyline
    LaunchedEffect(mapLibreMap, currentStyle, routePoints) {
        val map = mapLibreMap ?: return@LaunchedEffect
        if (currentStyle == null) return@LaunchedEffect

        @Suppress("DEPRECATION")
        currentPolyline?.let {
            map.removePolyline(it)
            currentPolyline = null
        }

        if (routePoints.isNotEmpty()) {
            val latLngs = routePoints.map { LatLng(it.first, it.second) }
            @Suppress("DEPRECATION")
            val polyline = map.addPolyline(
                PolylineOptions()
                    .addAll(latLngs)
                    .color(android.graphics.Color.parseColor("#0D4A3E"))
                    .width(5f)
            )
            currentPolyline = polyline
        }
    }
}
