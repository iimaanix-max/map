package com.tasbih.digitaltasbih.ui.screens.mosque

import android.content.Context
import android.graphics.Color as AndroidColor
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
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
import com.tasbih.digitaltasbih.data.mosque.NearbyMosque
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.MapTileIndex
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

/**
 * 100% Free Google Maps Raster Tiles Engine via OSMDroid.
 * Zero API keys, zero billing, loads official Google Maps road and satellite tiles natively.
 */
@Composable
fun GoogleOsmMapView(
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

    var isSatelliteView by remember { mutableStateOf(false) }

    // CartoDB Voyager Tile Source (Ultra-clean modern vector styling, zero commercial POIs/shops)
    val cartoVoyagerSource = remember {
        object : OnlineTileSourceBase(
            "CartoVoyager",
            0, 20, 256, ".png",
            arrayOf(
                "https://a.basemaps.cartocdn.com/rastertiles/voyager/",
                "https://b.basemaps.cartocdn.com/rastertiles/voyager/",
                "https://c.basemaps.cartocdn.com/rastertiles/voyager/",
                "https://d.basemaps.cartocdn.com/rastertiles/voyager/"
            )
        ) {
            override fun getTileURLString(pMapTileIndex: Long): String {
                val x = MapTileIndex.getX(pMapTileIndex)
                val y = MapTileIndex.getY(pMapTileIndex)
                val z = MapTileIndex.getZoom(pMapTileIndex)
                return "$baseUrl$z/$x/$y.png"
            }
        }
    }

    val googleMapsSatelliteSource = remember {
        object : OnlineTileSourceBase(
            "GoogleMapsSatellite",
            0, 20, 256, ".png",
            arrayOf(
                "https://mt0.google.com/vt/lyrs=y",
                "https://mt1.google.com/vt/lyrs=y",
                "https://mt2.google.com/vt/lyrs=y",
                "https://mt3.google.com/vt/lyrs=y"
            )
        ) {
            override fun getTileURLString(pMapTileIndex: Long): String {
                val x = MapTileIndex.getX(pMapTileIndex)
                val y = MapTileIndex.getY(pMapTileIndex)
                val z = MapTileIndex.getZoom(pMapTileIndex)
                return "$baseUrl&x=$x&y=$y&z=$z"
            }
        }
    }

    val mapView = remember {
        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid_dt", Context.MODE_PRIVATE))
        Configuration.getInstance().userAgentValue = context.packageName

        MapView(context).apply {
            setTileSource(if (isSatelliteView) googleMapsSatelliteSource else org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            isTilesScaledToDpi = true

            controller.setZoom(15.5)
            controller.setCenter(GeoPoint(userLat, userLon))
        }
    }

    // Lifecycle Observer
    DisposableEffect(lifecycleOwner, mapView) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_DESTROY -> mapView.onDetach()
                else -> Unit
            }
        }
        if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            mapView.onResume()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            try {
                mapView.onPause()
                mapView.onDetach()
            } catch (_: Exception) {}
        }
    }

    var currentPolylineGlow by remember { mutableStateOf<Polyline?>(null) }
    var currentPolylineCore by remember { mutableStateOf<Polyline?>(null) }
    val placeMarkers = remember { mutableListOf<Marker>() }

    // Update Tile Source on toggle
    LaunchedEffect(isSatelliteView) {
        val newSource = if (isSatelliteView) googleMapsSatelliteSource else org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK
        mapView.setTileSource(newSource)
        try {
            mapView.tileProvider.clearTileCache()
        } catch (_: Exception) {}
        mapView.invalidate()
    }

    // Update Markers & Polyline
    LaunchedEffect(places, selectedPlace, routePoints) {
        // 1. Remove previous markers & polylines
        placeMarkers.forEach { mapView.overlays.remove(it) }
        placeMarkers.clear()

        currentPolylineGlow?.let { mapView.overlays.remove(it) }
        currentPolylineCore?.let { mapView.overlays.remove(it) }
        currentPolylineGlow = null
        currentPolylineCore = null

        // Add Map Tap Listener to select place
        val mapEventsOverlay = MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                return false
            }

            override fun longPressHelper(p: GeoPoint): Boolean = false
        })
        mapView.overlays.add(mapEventsOverlay)

        // 2. Add User Location Marker (Blue Dot)
        val userBmp = CustomMarkerHelper.getUserLocationBitmap(context)
        val userMarker = Marker(mapView).apply {
            position = GeoPoint(userLat, userLon)
            title = "Your Location"
            icon = BitmapDrawable(context.resources, userBmp)
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        }
        placeMarkers.add(userMarker)
        mapView.overlays.add(userMarker)

        // 3. Add Custom Mosque / Halal Food Markers
        places.forEach { place ->
            val isSelected = selectedPlace?.id == place.id
            val distStr = if (place.distanceMeters >= 1000) String.format("%.1f km", place.distanceMeters / 1000.0) else "${place.distanceMeters} m"
            val drawable = CustomMarkerHelper.getOsmMarkerDrawable(
                context = context,
                name = place.name,
                distanceStr = distStr,
                isSelected = isSelected,
                isHalal = place.isHalal
            )

            val marker = Marker(mapView).apply {
                position = GeoPoint(place.lat, place.lon)
                title = place.name
                snippet = "$distStr · ${place.estimatedWalkMinutes} min walk"
                icon = drawable
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                setOnMarkerClickListener { _, _ ->
                    onPlaceClick(place)
                    true
                }
            }
            placeMarkers.add(marker)
            mapView.overlays.add(marker)
        }

        // 4. Draw OSRM Polyline Road Path
        if (selectedPlace != null && routePoints.isNotEmpty()) {
            val geoPoints = routePoints.map { GeoPoint(it.first, it.second) }

            // Outer Glowing Stroke
            val glowLine = Polyline(mapView).apply {
                setPoints(geoPoints)
                outlinePaint.color = AndroidColor.parseColor("#4410B981")
                outlinePaint.strokeWidth = 20f
                outlinePaint.strokeCap = Paint.Cap.ROUND
                outlinePaint.strokeJoin = Paint.Join.ROUND
            }

            // Inner Crisp Emerald Line
            val coreLine = Polyline(mapView).apply {
                setPoints(geoPoints)
                outlinePaint.color = AndroidColor.parseColor("#10B981")
                outlinePaint.strokeWidth = 10f
                outlinePaint.strokeCap = Paint.Cap.ROUND
                outlinePaint.strokeJoin = Paint.Join.ROUND
            }

            mapView.overlays.add(glowLine)
            mapView.overlays.add(coreLine)
            currentPolylineGlow = glowLine
            currentPolylineCore = coreLine

            // Animate to destination
            mapView.controller.animateTo(GeoPoint(selectedPlace.lat, selectedPlace.lon), 16.0, 1000L)
        }

        mapView.invalidate()
    }

    Box(modifier = modifier) {
        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize()
        )

        // Floating Map Controls (Satellite Toggle & Recenter on User)
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 100.dp, end = 14.dp),
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
                        mapView.controller.animateTo(GeoPoint(userLat, userLon), 16.0, 800L)
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
