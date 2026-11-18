package com.tasbih.digitaltasbih.ui.screens.qibla

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.Surface
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.tasbih.digitaltasbih.ui.components.AppToast
import com.tasbih.digitaltasbih.ui.components.LocationPermissionDialog
import com.tasbih.digitaltasbih.ui.components.PermissionHelper
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.tasbih.digitaltasbih.data.*
import com.tasbih.digitaltasbih.ui.components.AdMobNativeAdCard
import com.tasbih.digitaltasbih.ui.components.LanguageBar
import com.tasbih.digitaltasbih.ui.components.MoreLanguagesModal
import com.tasbih.digitaltasbih.ui.components.TopHeaderButton
import com.tasbih.digitaltasbih.ui.components.rememberAssetPainter
import com.tasbih.digitaltasbih.util.AdMobInterstitialManager
import com.tasbih.digitaltasbih.util.AdMobRewardedManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun QiblaScreen(
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()

    // Active Language & Theme
    var selectedLang by remember { mutableStateOf(PrayerRepository.getLanguage()) }
    var isDarkTheme by remember { mutableStateOf(false) }
    var isMoreLangOpen by remember { mutableStateOf(false) }

    // Compass Skin
    var selectedSkin by remember { mutableStateOf(QiblaRepository.getSelectedSkin(context)) }
    var isCompassModalOpen by remember { mutableStateOf(false) }
    var skinToUnlock by remember { mutableStateOf<CompassSkin?>(null) }
    var skinUnlockTrigger by remember { mutableIntStateOf(0) }

    // Calibration Prompt Toast (starts visible when calibration is needed)
    var isCalibrationPromptVisible by remember { mutableStateOf(true) }

    // Location & Calculation State
    val prayerSettings = remember { PrayerRepository.loadSettings() }
    val initialLat = if (prayerSettings.locationLabel.contains("Mountain View") || prayerSettings.latitude in 37.0..38.0) 21.0970 else prayerSettings.latitude
    val initialLon = if (prayerSettings.locationLabel.contains("Mountain View") || prayerSettings.longitude in -123.0..-121.0) 81.0374 else prayerSettings.longitude
    val initialLabel = if (prayerSettings.locationLabel.contains("Mountain View") || prayerSettings.locationLabel.contains("United States")) "Rajnandgaon, India" else prayerSettings.locationLabel

    var userLat by remember { mutableStateOf(initialLat) }
    var userLon by remember { mutableStateOf(initialLon) }
    var userLocationLabel by remember { mutableStateOf(initialLabel) }
    var isLiveLocation by remember { mutableStateOf(false) }
    var isGpsUpdating by remember { mutableStateOf(false) }
    var mapWebViewRef by remember { mutableStateOf<WebView?>(null) }
    var isLocationPermissionDialogOpen by remember { mutableStateOf(false) }

    var gpsBannerText by remember { mutableStateOf<String?>(null) }
    var gpsBannerType by remember { mutableStateOf("loading") } // "loading", "success", "error"
    var gpsBannerJob by remember { mutableStateOf<kotlinx.coroutines.Job?>(null) }

    fun showGpsBanner(type: String, text: String, autoDismissMs: Long = 3200L) {
        gpsBannerJob?.cancel()
        gpsBannerType = type
        gpsBannerText = text
        if (autoDismissMs > 0) {
            gpsBannerJob = coroutineScope.launch {
                kotlinx.coroutines.delay(autoDismissMs)
                gpsBannerText = null
            }
        }
    }

    val gpsInfiniteTransition = rememberInfiniteTransition(label = "gpsSpinTransition")
    val gpsSpinAngle by gpsInfiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "gpsSpinAngle"
    )

    LaunchedEffect(Unit) {
        if (prayerSettings.locationLabel.contains("Mountain View") || prayerSettings.locationLabel.contains("United States")) {
            PrayerRepository.saveSettings(
                prayerSettings.copy(
                    latitude = 21.0970,
                    longitude = 81.0374,
                    locationLabel = "Rajnandgaon, India"
                )
            )
        }
    }

    // Live GPS Location Updater
    fun applyNewLocation(loc: Location) {
        val lat = loc.latitude
        val lon = loc.longitude
        // If emulator reports default Mountain View (37.422, -122.084) but user is in India, keep Rajnandgaon
        val isDefaultEmulatorMock = (lat in 37.41..37.43 && lon in -122.09..-122.07)
        if (isDefaultEmulatorMock && (userLocationLabel.contains("Rajnandgaon") || userLocationLabel.contains("India"))) {
            isGpsUpdating = false
            showGpsBanner("success", "Location: $userLocationLabel 📍", autoDismissMs = 3000L)
            return
        }

        userLat = lat
        userLon = lon
        isLiveLocation = true

        coroutineScope.launch(Dispatchers.IO) {
            val latDir = if (lat >= 0) "N" else "S"
            val lonDir = if (lon >= 0) "E" else "W"
            var label = String.format(Locale.US, "%.2f° %s, %.2f° %s", abs(lat), latDir, abs(lon), lonDir)
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(lat, lon, 1)
                if (!addresses.isNullOrEmpty()) {
                    val addr = addresses[0]
                    val city = addr.locality ?: addr.subAdminArea ?: addr.adminArea ?: ""
                    val country = addr.countryName ?: ""
                    if (city.isNotEmpty() && country.isNotEmpty()) {
                        label = "$city, $country"
                    } else if (city.isNotEmpty()) {
                        label = city
                    } else if (country.isNotEmpty()) {
                        label = country
                    }
                }
            } catch (_: Exception) {}

            withContext(Dispatchers.Main) {
                userLocationLabel = label
                isGpsUpdating = false
                showGpsBanner("success", "Location: $label 📍", autoDismissMs = 3500L)
                PrayerRepository.saveSettings(
                    PrayerRepository.loadSettings().copy(
                        latitude = lat,
                        longitude = lon,
                        locationLabel = label
                    )
                )
                val safeLabel = org.json.JSONObject.quote(label)
                mapWebViewRef?.evaluateJavascript(
                    "if(window.updateUserLocation){window.updateUserLocation($lat, $lon, $safeLabel);}", null
                )
            }
        }
    }

    lateinit var requestLocationUpdateRef: () -> Unit

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (fineGranted || coarseGranted) {
            requestLocationUpdateRef()
        } else {
            isGpsUpdating = false
            isLocationPermissionDialogOpen = true
            showGpsBanner("error", "Location permission needed for GPS", autoDismissMs = 3200L)
        }
    }

    val requestLocationUpdate: () -> Unit = {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        val fineAllowed = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarseAllowed = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!fineAllowed && !coarseAllowed) {
            locationPermissionLauncher.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            )
        } else if (locationManager != null) {
            isGpsUpdating = true
            showGpsBanner("loading", "Fetching live GPS location... 🛰️", autoDismissMs = 5000L)

            coroutineScope.launch {
                kotlinx.coroutines.delay(4500L)
                if (isGpsUpdating) {
                    isGpsUpdating = false
                    if (gpsBannerType == "loading") {
                        gpsBannerText = null
                    }
                }
            }

            try {
                val providers = listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER, LocationManager.PASSIVE_PROVIDER)
                var bestLoc: Location? = null
                for (provider in providers) {
                    try {
                        val loc = locationManager.getLastKnownLocation(provider)
                        if (loc != null && (bestLoc == null || loc.accuracy < bestLoc.accuracy)) {
                            bestLoc = loc
                        }
                    } catch (_: Exception) {}
                }
                if (bestLoc != null) {
                    applyNewLocation(bestLoc)
                }

                val singleListener = object : LocationListener {
                    override fun onLocationChanged(loc: Location) {
                        applyNewLocation(loc)
                        try { locationManager.removeUpdates(this) } catch (_: Exception) {}
                    }
                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                    override fun onProviderEnabled(provider: String) {}
                    override fun onProviderDisabled(provider: String) {}
                }

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 5f, singleListener)
                } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000L, 5f, singleListener)
                }
            } catch (_: Exception) {
                isGpsUpdating = false
            }
        }
    }
    requestLocationUpdateRef = requestLocationUpdate

    LaunchedEffect(Unit) {
        val fineAllowed = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarseAllowed = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fineAllowed || coarseAllowed) {
            requestLocationUpdate()
        } else {
            locationPermissionLauncher.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            )
        }
    }

    val qiblaBearing = remember(userLat, userLon) {
        QiblaRepository.computeBearing(userLat, userLon)
    }
    val distanceKm = remember(userLat, userLon) {
        QiblaRepository.computeDistanceKm(userLat, userLon)
    }

    // Hardware Compass Heading State
    var rawHeading by remember { mutableStateOf(0.0) }
    var currentHeading by remember { mutableStateOf(0.0) }
    var hasCompassSensor by remember { mutableStateOf(true) }
    var isPhoneFlat by remember { mutableStateOf(true) }
    var accuracyState by remember { mutableStateOf("bad") } // "good", "bad"



    // Relative angle to Qibla: finalAngle = bearing - heading
    val finalAngle = remember(qiblaBearing, currentHeading) {
        QiblaRepository.angleDiff(qiblaBearing, currentHeading)
    }
    val isAligned = remember(finalAngle, hasCompassSensor) {
        hasCompassSensor && abs(finalAngle) <= 3.0
    }

    // Haptic feedback tick on exact alignment
    var wasAligned by remember { mutableStateOf(false) }
    LaunchedEffect(isAligned) {
        if (isAligned && !wasAligned) {
            try {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            } catch (_: Exception) {}
        }
        wasAligned = isAligned
    }

    // Sensor Manager Registration (Primary Rotation Vector -> Geomagnetic -> Magnetometer+Accel fallback)
    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        val rotSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        val geomagSensor = if (rotSensor == null) sensorManager?.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR) else null
        val magSensor = if (rotSensor == null && geomagSensor == null) sensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) else null
        val accelSensor = if (rotSensor == null && geomagSensor == null) sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) else null
        val oriSensor = if (rotSensor == null && geomagSensor == null && (magSensor == null || accelSensor == null)) sensorManager?.getDefaultSensor(Sensor.TYPE_ORIENTATION) else null

        val hasAnySensor = rotSensor != null || geomagSensor != null || (magSensor != null && accelSensor != null) || oriSensor != null
        if (sensorManager == null || !hasAnySensor) {
            hasCompassSensor = false
        }

        val listener = object : SensorEventListener {
            private val rotMatrix = FloatArray(9)
            private val remappedMatrix = FloatArray(9)
            private val orientation = FloatArray(3)
            private val magnetValues = FloatArray(3)
            private val accelValues = FloatArray(3)
            private var hasMagnet = false
            private var hasAccel = false
            private var consecutiveTiltCount = 0
            private var consecutiveFlatCount = 0
            private var lastAzDeg = -1.0
            private var lastPitchDeg = -1.0
            private var lastRollDeg = -1.0
            private var figure8MotionAcc = 0.0
            private var smoothedHeading = 0.0
            private var firstReading = true
            private var lastPushMs = 0L
            private var lastPushedHeading = -999.0

            private fun extractOrientation(sourceMatrix: FloatArray) {
                val displayRotation = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                    try {
                        context.display?.rotation ?: Surface.ROTATION_0
                    } catch (_: Exception) {
                        (context as? Activity)?.windowManager?.defaultDisplay?.rotation ?: Surface.ROTATION_0
                    }
                } else {
                    (context as? Activity)?.windowManager?.defaultDisplay?.rotation ?: Surface.ROTATION_0
                }

                val (axisX, axisY) = when (displayRotation) {
                    Surface.ROTATION_90 -> Pair(SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X)
                    Surface.ROTATION_180 -> Pair(SensorManager.AXIS_MINUS_X, SensorManager.AXIS_MINUS_Y)
                    Surface.ROTATION_270 -> Pair(SensorManager.AXIS_MINUS_Y, SensorManager.AXIS_X)
                    else -> Pair(SensorManager.AXIS_X, SensorManager.AXIS_Y)
                }

                val ok = SensorManager.remapCoordinateSystem(sourceMatrix, axisX, axisY, remappedMatrix)
                val matToUse = if (ok) remappedMatrix else sourceMatrix
                SensorManager.getOrientation(matToUse, orientation)
            }

            override fun onSensorChanged(event: SensorEvent?) {
                if (event == null) return
                var azDeg = -1.0
                var pitchDeg = 0.0
                var rollDeg = 0.0
                var hasValidReading = false

                if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR || event.sensor.type == Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR) {
                    SensorManager.getRotationMatrixFromVector(rotMatrix, event.values)
                    extractOrientation(rotMatrix)
                    val p = orientation[1].toDouble()
                    val r = orientation[2].toDouble()
                    val azRad = orientation[0].toDouble()
                    pitchDeg = if (p.isNaN()) 0.0 else Math.toDegrees(p)
                    rollDeg = if (r.isNaN()) 0.0 else Math.toDegrees(r)
                    azDeg = (Math.toDegrees(azRad) + 360.0) % 360.0
                    hasValidReading = true
                } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                    System.arraycopy(event.values, 0, magnetValues, 0, 3)
                    hasMagnet = true
                } else if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    System.arraycopy(event.values, 0, accelValues, 0, 3)
                    hasAccel = true
                } else if (event.sensor.type == Sensor.TYPE_ORIENTATION) {
                    azDeg = (event.values[0].toDouble() + 360.0) % 360.0
                    pitchDeg = event.values[1].toDouble()
                    rollDeg = event.values[2].toDouble()
                    hasValidReading = true
                }

                if (!hasValidReading && hasMagnet && hasAccel &&
                    (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD || event.sensor.type == Sensor.TYPE_ACCELEROMETER)) {
                    val ok = SensorManager.getRotationMatrix(rotMatrix, null, accelValues, magnetValues)
                    if (ok) {
                        extractOrientation(rotMatrix)
                        val p = orientation[1].toDouble()
                        val r = orientation[2].toDouble()
                        val azRad = orientation[0].toDouble()
                        pitchDeg = if (p.isNaN()) 0.0 else Math.toDegrees(p)
                        rollDeg = if (r.isNaN()) 0.0 else Math.toDegrees(r)
                        azDeg = (Math.toDegrees(azRad) + 360.0) % 360.0
                        hasValidReading = true
                    }
                }

                if (hasValidReading && azDeg >= 0.0) {
                    if (firstReading) {
                        smoothedHeading = azDeg
                        firstReading = false
                    } else {
                        var d = azDeg - smoothedHeading
                        if (d > 180.0) d -= 360.0
                        if (d < -180.0) d += 360.0
                        val absD = kotlin.math.abs(d)
                        // Adaptive responsive smoothing: instant tracking (0.48) when turning phone, silky fine filtering (0.24) when holding steady
                        val factor = if (absD > 12.0) 0.48 else if (absD > 4.0) 0.36 else 0.24
                        smoothedHeading = (smoothedHeading + factor * d + 360.0) % 360.0
                    }

                    val now = android.os.SystemClock.uptimeMillis()
                    if (now - lastPushMs >= 16L) { // 60fps smooth refresh rate
                        var delta = kotlin.math.abs(smoothedHeading - lastPushedHeading)
                        if (delta > 180.0) delta = 360.0 - delta
                        if (delta >= 0.15 || lastPushedHeading < 0) {
                            lastPushMs = now
                            lastPushedHeading = smoothedHeading
                            currentHeading = smoothedHeading
                            rawHeading = azDeg
                            hasCompassSensor = true
                        }
                    }

                    // Figure-8 Motion Accumulator: auto-dismiss calibration prompt when user moves phone
                    if (isCalibrationPromptVisible) {
                        if (lastAzDeg >= 0) {
                            val dAz = kotlin.math.abs(QiblaRepository.angleDiff(azDeg, lastAzDeg))
                            val dP = kotlin.math.abs(pitchDeg - lastPitchDeg)
                            val dR = kotlin.math.abs(rollDeg - lastRollDeg)
                            val delta = dAz + dP + dR
                            if (delta in 1.2..45.0) {
                                figure8MotionAcc += delta
                                if (figure8MotionAcc >= 260.0) {
                                    isCalibrationPromptVisible = false
                                    accuracyState = "good"
                                    figure8MotionAcc = 0.0
                                }
                            }
                        }
                        lastAzDeg = azDeg
                        lastPitchDeg = pitchDeg
                        lastRollDeg = rollDeg
                    } else {
                        figure8MotionAcc = 0.0
                        lastAzDeg = -1.0
                    }

                    val maxTilt = kotlin.math.max(kotlin.math.abs(pitchDeg), kotlin.math.abs(rollDeg))
                    if (maxTilt > 60.0) {
                        consecutiveTiltCount++
                        consecutiveFlatCount = 0
                        if (consecutiveTiltCount >= 20 && isPhoneFlat) {
                            isPhoneFlat = false
                        }
                    } else if (maxTilt < 45.0) {
                        consecutiveFlatCount++
                        consecutiveTiltCount = 0
                        if (consecutiveFlatCount >= 10 && !isPhoneFlat) {
                            isPhoneFlat = true
                        }
                    } else {
                        consecutiveTiltCount = 0
                        consecutiveFlatCount = 0
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                accuracyState = when (accuracy) {
                    SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> "good"
                    SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> "good"
                    SensorManager.SENSOR_STATUS_ACCURACY_LOW -> "bad"
                    SensorManager.SENSOR_STATUS_UNRELIABLE -> "bad"
                    else -> "good"
                }
                if (accuracyState == "good") {
                    isCalibrationPromptVisible = false
                }
            }
        }

        // Register best available sensor with fast 60Hz SENSOR_DELAY_GAME:
        if (rotSensor != null) {
            sensorManager?.registerListener(listener, rotSensor, SensorManager.SENSOR_DELAY_GAME)
        } else if (geomagSensor != null) {
            sensorManager?.registerListener(listener, geomagSensor, SensorManager.SENSOR_DELAY_GAME)
        } else if (magSensor != null && accelSensor != null) {
            sensorManager?.registerListener(listener, magSensor, SensorManager.SENSOR_DELAY_GAME)
            sensorManager?.registerListener(listener, accelSensor, SensorManager.SENSOR_DELAY_GAME)
        } else if (oriSensor != null) {
            sensorManager?.registerListener(listener, oriSensor, SensorManager.SENSOR_DELAY_GAME)
        }

        onDispose {
            sensorManager?.unregisterListener(listener)
        }
    }

    // Auto-dismiss calibration prompt after 6 seconds so it never traps the user or blocks compass
    LaunchedEffect(isCalibrationPromptVisible) {
        if (isCalibrationPromptVisible) {
            kotlinx.coroutines.delay(6000L)
            isCalibrationPromptVisible = false
        }
    }

    // Pre-load Interstitial Ad for back navigation and Rewarded Ad for compass skins
    LaunchedEffect(Unit) {
        AdMobInterstitialManager.loadAd(context)
        AdMobRewardedManager.loadAd(context)
    }

    val activity = context as? Activity
    val handleBack = {
        if (activity != null) {
            AdMobInterstitialManager.showAd(activity, "qibla_back") {
                onBack()
            }
        } else {
            onBack()
        }
    }

    BackHandler {
        if (isCompassModalOpen) {
            isCompassModalOpen = false
        } else if (isMoreLangOpen) {
            isMoreLangOpen = false
        } else {
            handleBack()
        }
    }

    val strings = remember(selectedLang) { QiblaLocalization.getStrings(selectedLang) }

    val visibleLanguages = listOf(
        LanguageItem("en", "English"),
        LanguageItem("hi", "हिंदी"),
        LanguageItem("ur", "اردو"),
        LanguageItem("ar", "العربية"),
        LanguageItem("bn", "বাংলা")
    )

    // Palette & Theme styling
    val bgMain = if (isDarkTheme) Color(0xFF071510) else Color(0xFFFAF7F2)
    val cardBg = if (isDarkTheme) Color(0xFF0C241B) else Color(0xFFFFFFFF)
    val cardBorder = if (isDarkTheme) Color(0xFFC9A84C).copy(alpha = 0.28f) else Color(0xFFE2E8F0)
    val inkColor = if (isDarkTheme) Color(0xFFF0EAD6) else Color(0xFF1A1408)
    val mutedColor = if (isDarkTheme) Color(0xFFA89F8B) else Color(0xFF64748B)

    // Leaflet HTML String with local cached assets
    val leafletMapHtml = remember(userLat, userLon, userLocationLabel) {
        QiblaRepository.generateLeafletMapHtml(userLat, userLon, userLocationLabel, context)
    }



    Scaffold(
        topBar = {
            QiblaHeaderTopBar(
                title = strings.title,
                locationLabel = userLocationLabel,
                isDarkTheme = isDarkTheme,
                onBack = { handleBack() },
                onRefreshLocation = { requestLocationUpdate() },
                onToggleTheme = {
                    isDarkTheme = !isDarkTheme
                    AppToast.show(if (isDarkTheme) "Night Theme 🌙" else "Day Theme ☀️")
                },
                onFavorite = {
                    AppToast.show("Added to Favorites ❤️")
                },
                onSettings = {
                    AppToast.show("Qibla Settings")
                }
            )
        },
        containerColor = bgMain
    ) { paddingValues ->
        val scrollState = rememberScrollState()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                // 1. HORIZONTAL LANGUAGE PILLS BAR
                LanguageBar(
                    languages = visibleLanguages,
                    selectedCode = selectedLang,
                    onSelectLanguage = { code ->
                        selectedLang = code
                        PrayerRepository.saveLanguage(code)
                    },
                    onOpenMoreLanguages = {
                        isMoreLangOpen = true
                    }
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // ══════════════════════════════════════════════════════
                    // 2. CARD 1: INTERACTIVE LEAFLET MAP & 4 METRIC TILES
                    // ══════════════════════════════════════════════════════
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(18.dp)),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        border = androidx.compose.foundation.BorderStroke(1.dp, cardBorder)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Leaflet Map Box (Height: 140dp)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp)
                                    .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                                    .background(Color(0xFFE2E8F0))
                            ) {
                                AndroidView(
                                    factory = { ctx ->
                                        WebView(ctx).apply {
                                            layoutParams = ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT
                                            )
                                            settings.javaScriptEnabled = true
                                            settings.domStorageEnabled = true
                                            settings.loadWithOverviewMode = true
                                            settings.useWideViewPort = true
                                            settings.cacheMode = WebSettings.LOAD_DEFAULT
                                            settings.allowFileAccess = false
                                            settings.allowContentAccess = false
                                            webViewClient = WebViewClient()
                                            setBackgroundColor(android.graphics.Color.TRANSPARENT)
                                            loadDataWithBaseURL(
                                                "https://server.arcgisonline.com",
                                                leafletMapHtml,
                                                "text/html",
                                                "UTF-8",
                                                null
                                            )
                                            mapWebViewRef = this
                                        }
                                    },
                                    update = { webView ->
                                        mapWebViewRef = webView
                                        val safeLabel = org.json.JSONObject.quote(userLocationLabel)
                                        webView.evaluateJavascript(
                                            "if(window.updateUserLocation){window.updateUserLocation($userLat, $userLon, $safeLabel);}", null
                                        )
                                    },
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            // Location Header Row with Quick GPS Refresh
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 12.dp, end = 12.dp, top = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(text = "📍", fontSize = 12.sp)
                                    Text(
                                        text = userLocationLabel,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = inkColor,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .shadow(elevation = 2.dp, shape = RoundedCornerShape(50.dp))
                                        .clip(RoundedCornerShape(50.dp))
                                        .background(
                                            Brush.horizontalGradient(
                                                if (isGpsUpdating) {
                                                    listOf(Color(0xFF047857), Color(0xFF0F5A44))
                                                } else {
                                                    listOf(Color(0xFF0F3B2E), Color(0xFF14533C))
                                                }
                                            )
                                        )
                                        .border(
                                            width = 1.2.dp,
                                            color = if (isGpsUpdating) Color(0xFF6EE7B7) else Color(0xFFD4AF37),
                                            shape = RoundedCornerShape(50.dp)
                                        )
                                        .clickable {
                                            if (!isGpsUpdating) {
                                                requestLocationUpdate()
                                            }
                                        }
                                        .padding(horizontal = 10.dp, vertical = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = "🔄",
                                            fontSize = 11.sp,
                                            modifier = Modifier.rotate(if (isGpsUpdating) gpsSpinAngle else 0f)
                                        )
                                        Text(
                                            text = if (isGpsUpdating) "LOCATING..." else "GPS",
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = if (isGpsUpdating) Color(0xFFE6FFFA) else Color(0xFFF7E2A8),
                                            letterSpacing = 0.5.sp
                                        )
                                    }
                                }
                            }

                            // Status Chips Row below map (Matching Image 1)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Accuracy Chip (Image 1: beige/taupe outline, muted brown label, teal value)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50.dp))
                                        .background(Color(0xFFFAF8F5))
                                        .border(1.dp, Color(0xFFE2D7C5), RoundedCornerShape(50.dp))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                                    ) {
                                        Text(
                                            text = "${strings.accuracy}:",
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFF786B58)
                                        )
                                        Text(
                                            text = "—",
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0D9488)
                                        )
                                    }
                                }

                                // Location Source Chip (Image 1: SAVED LOCATION in bold teal #0E7490 with mint border/bg)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50.dp))
                                        .background(Color(0xFFE6F5F2))
                                        .border(
                                            1.dp,
                                            Color(0xFFB5E2D9),
                                            RoundedCornerShape(50.dp)
                                        )
                                        .clickable { requestLocationUpdate() }
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = if (isLiveLocation) strings.liveLocation else strings.savedLocation,
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0E7490)
                                    )
                                }

                                // Calibration Status Chip (Image 1: NEEDS CALIBRATION in bold sky-blue #0284C7 with warm border)
                                val isCalibrated = (accuracyState == "good")
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50.dp))
                                        .background(Color(0xFFFCF9F2))
                                        .border(
                                            1.dp,
                                            Color(0xFFE5DACB),
                                            RoundedCornerShape(50.dp)
                                        )
                                        .clickable {
                                            isCalibrationPromptVisible = !isCalibrationPromptVisible
                                            if (isCalibrationPromptVisible) {
                                                accuracyState = "bad"
                                            }
                                        }
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = if (isCalibrated) strings.calibrated else strings.needsCalibration,
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isCalibrated) Color(0xFF059669) else Color(0xFF0284C7)
                                    )
                                }
                            }

                            // 4 Metric Tiles Grid (Matching Image 1: QIBLA #047857, FACING #0284C7, FINAL ANGLE #D97706, DISTANCE #0284C7)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // QIBLA Tile
                                MetricTileItem(
                                    modifier = Modifier.weight(1f),
                                    label = strings.qibla,
                                    value = "${qiblaBearing.roundToInt()}°",
                                    valueColor = Color(0xFF047857)
                                )

                                // FACING Tile (Image 1: sky blue #0284C7)
                                MetricTileItem(
                                    modifier = Modifier.weight(1f),
                                    label = strings.facing,
                                    value = if (hasCompassSensor && currentHeading > 0) "${currentHeading.roundToInt()}°" else "—",
                                    valueColor = Color(0xFF0284C7)
                                )

                                // Turn / Alignment Metric Tile: Unambiguous so users clearly see deviation turn direction
                                val isAlignedTile = hasCompassSensor && abs(finalAngle) <= 3.0
                                val turnValue = when {
                                    !hasCompassSensor -> "—"
                                    isAlignedTile -> "0° 🕋"
                                    finalAngle > 0.5 -> "↱ ${abs(finalAngle).roundToInt()}°"
                                    finalAngle < -0.5 -> "↰ ${abs(finalAngle).roundToInt()}°"
                                    else -> "0°"
                                }
                                val turnLabel = if (isAlignedTile) {
                                    if (selectedLang == "hi") "संरेखित" else if (selectedLang == "ur") "متوازن" else "ALIGNED"
                                } else {
                                    if (selectedLang == "hi") "दिशा घुमाएं" else if (selectedLang == "ur") "رخ موڑیں" else "TURN"
                                }
                                val turnColor = if (isAlignedTile) Color(0xFF059669) else Color(0xFFD97706)

                                MetricTileItem(
                                    modifier = Modifier.weight(1.2f),
                                    label = turnLabel,
                                    value = turnValue,
                                    valueColor = turnColor
                                )

                                // DISTANCE Tile (Image 1: vibrant cyan/blue #0284C7)
                                MetricTileItem(
                                    modifier = Modifier.weight(1.2f),
                                    label = strings.distance,
                                    value = "${distanceKm.roundToInt()} km",
                                    valueColor = Color(0xFF0284C7)
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }

                    // ══════════════════════════════════════════════════════
                    // 3. CARD 2: REAL-TIME COMPASS DIAL & FLAT PHONE HINT
                    // ══════════════════════════════════════════════════════
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(18.dp)),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        border = androidx.compose.foundation.BorderStroke(1.dp, cardBorder)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Real-Time Compass Dial
                                Box(
                                    modifier = Modifier
                                        .size(285.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    // Radiant Glowing Green Halo when Aligned - isolated to avoid recompositions
                                    if (isAligned) {
                                        GlowingAlignmentHalo()
                                    }

                                    // Top Alignment Guideline Notch
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopCenter)
                                            .size(width = 4.dp, height = 12.dp)
                                            .background(
                                                if (isAligned) Color(0xFF10B981) else Color(0xFFC9A84C),
                                                RoundedCornerShape(2.dp)
                                            )
                                    )

                                    if (selectedSkin.id == "classic_green") {
                                        // Classic Vector Green Dial
                                        ClassicGreenCompassCanvas(
                                            heading = currentHeading,
                                            finalAngle = finalAngle,
                                            isAligned = isAligned
                                        )
                                    } else {
                                        // 1. Static Outer Frame (Does not rotate)
                                        if (selectedSkin.outerAsset.isNotEmpty()) {
                                            Image(
                                                painter = rememberAssetPainter(selectedSkin.outerAsset),
                                                contentDescription = "Compass Outer Frame",
                                                modifier = Modifier.fillMaxSize(),
                                                contentScale = ContentScale.Fit
                                            )
                                        }

                                        // 2. Rotating Inner Dial (Rotates by -heading, 0-gap locked)
                                        if (selectedSkin.innerAsset.isNotEmpty()) {
                                            Image(
                                                painter = rememberAssetPainter(selectedSkin.innerAsset),
                                                contentDescription = "Compass Inner Dial",
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .rotate(-currentHeading.toFloat()),
                                                contentScale = ContentScale.Fit
                                            )
                                        }

                                        // 3. Rotating Needle (Points to Kaaba, pivot centered, scaled ~2cm larger)
                                        if (selectedSkin.needleAsset.isNotEmpty()) {
                                            Image(
                                                painter = rememberAssetPainter(selectedSkin.needleAsset),
                                                contentDescription = "Compass Needle",
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .scale(1.48f)
                                                    .rotate(finalAngle.toFloat()),
                                                contentScale = ContentScale.Fit
                                            )
                                        }
                                    }

                                    // Center pivot jewel pin (only if default needle without baked jewel)
                                    if (selectedSkin.needleAsset.isEmpty()) {
                                        Box(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    if (isAligned) Color(0xFF10B981) else Color(0xFFE8C878)
                                                )
                                                .border(1.5.dp, Color(0xFF1B4D3E), CircleShape)
                                        )
                                    }
                                }

                                // Flat Phone Warning (Shown ONLY when phone is held nearly upright / not flat)
                                AnimatedVisibility(
                                    visible = !isPhoneFlat,
                                    enter = fadeIn(tween(350)) + expandVertically(tween(350)),
                                    exit = fadeOut(tween(350)) + shrinkVertically(tween(350))
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(top = 16.dp)
                                    ) {
                                        // Hand holding flat phone illustration
                                        Image(
                                            painter = rememberAssetPainter("media/ba7e0050108eb929.webp"),
                                            contentDescription = "Place your phone flat",
                                            modifier = Modifier
                                                .width(180.dp)
                                                .height(64.dp),
                                            contentScale = ContentScale.Fit
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        // Main Instruction Heading
                                        Text(
                                            text = strings.placeFlatInstruction,
                                            fontSize = 14.5.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            fontFamily = FontFamily.Serif,
                                            color = Color(0xFF9E641E),
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(horizontal = 16.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Subtitle
                                Text(
                                    text = strings.trueBearingKaaba,
                                    fontSize = 12.sp,
                                    color = Color(0xFF8E8880),
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                // Real AdMob Native Ad with Zero-Gap Collapse
                                AdMobNativeAdCard(
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            // Floating Palette Button in Top-Right Corner (Matches Original index.html & Screenshots)
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(top = 2.dp, end = 2.dp)
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF7E8B8B).copy(alpha = 0.88f))
                                    .border(1.2.dp, Color(0xFFE8C878).copy(alpha = 0.7f), CircleShape)
                                    .clickable { isCompassModalOpen = true },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "🎨",
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }

                    // ══════════════════════════════════════════════════════
                    // 4. BOTTOM SECTION: ROUTE PROGRESS TRACK & DEBUG FOOTER
                    // ══════════════════════════════════════════════════════
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Green Route Bar (qq-route)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF0C382B))
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            // Connecting Dashed Mint Track Line
                            Canvas(modifier = Modifier.fillMaxWidth().height(2.dp)) {
                                drawLine(
                                    color = Color(0xFF2DD4BF),
                                    start = Offset(24.dp.toPx(), size.height / 2),
                                    end = Offset(size.width - 24.dp.toPx(), size.height / 2),
                                    strokeWidth = 2.dp.toPx(),
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f), 0f)
                                )
                            }

                            // Kaaba icon on left
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF144D3E))
                                    .border(1.dp, Color(0xFFC9A84C), CircleShape)
                                    .align(Alignment.CenterStart),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🕋", fontSize = 14.sp)
                            }

                            // User Pin on right
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF059669))
                                    .border(1.dp, Color(0xFF34D399), CircleShape)
                                    .align(Alignment.CenterEnd),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "📍", fontSize = 14.sp)
                            }
                        }

                        // Route Labels Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = strings.makkah,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = inkColor
                            )
                            Text(
                                text = strings.yourLocation,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = inkColor
                            )
                        }

                        // Great-circle explanation note
                        Text(
                            text = strings.shortestRoute,
                            fontSize = 11.sp,
                            color = mutedColor,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Debug Footer Row (Image 2)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${strings.debug}   ${strings.qibla}: ${qiblaBearing.roundToInt()}°   ${strings.facing}: ${if (hasCompassSensor && currentHeading > 0) "${currentHeading.roundToInt()}°" else "—"}   ${strings.finalAngle}: ${abs(finalAngle).roundToInt()}°   ${strings.accuracy}: —",
                                fontSize = 10.sp,
                                color = Color(0xFF8E7C57),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(80.dp))
                }
            }

            // ══════════════════════════════════════════════════════
            // 5. FLOATING FIGURE-8 CALIBRATION PROMPT TOAST (Matching Image 3)
            // ══════════════════════════════════════════════════════
            AnimatedVisibility(
                visible = isCalibrationPromptVisible,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 14.dp, vertical = 14.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 12.dp, shape = RoundedCornerShape(18.dp)),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF134E39)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1D5A43))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Left: White rounded card with figure-8 illustration (Image 3)
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFFBF9F5))
                                .padding(3.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = rememberAssetPainter("media/f9700fb42a26ada5.jpg"),
                                contentDescription = "Figure 8 calibration",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(10.dp)),
                                contentScale = ContentScale.Fit
                            )
                        }

                        // Right: Title + Crimson Badge, Subtitle, and Gold Done Button (Image 3)
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Text(
                                        text = "🧲",
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = strings.calibrationRequired,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFF6D06F)
                                    )
                                }

                                // Crimson Outline Badge (Image 3: Needs Calibration)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0x22DC2626))
                                        .border(1.dp, Color(0xFFDC2626), RoundedCornerShape(6.dp))
                                        .padding(horizontal = 7.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = strings.needsCalibration,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFF87171)
                                    )
                                }
                            }

                            // Subtitle (Image 3: Move your phone in a figure-8 to calibrate the compass.)
                            Text(
                                text = strings.figure8Instruction,
                                fontSize = 11.5.sp,
                                color = Color(0xFFF1F5F9),
                                lineHeight = 15.sp
                            )

                            // Done button (Image 3: Gold gradient pill button with Done ✓)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            colors = listOf(
                                                Color(0xFFF5D78E),
                                                Color(0xFFD8B04E)
                                            )
                                        )
                                    )
                                    .clickable {
                                        isCalibrationPromptVisible = false
                                        accuracyState = "good"
                                    }
                                    .padding(horizontal = 18.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = strings.done,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF18181B)
                                )
                            }
                        }
                    }
                }
            }

            // ══════════════════════════════════════════════════════
            // 6. COMPASS DESIGNS BOTTOM SHEET MODAL (Image 3)
            // ══════════════════════════════════════════════════════
            if (isCompassModalOpen) {
                CompassDesignsModal(
                    skins = QiblaRepository.getSkins(),
                    selectedSkinId = selectedSkin.id,
                    strings = strings,
                    unlockTrigger = skinUnlockTrigger,
                    onSelectSkin = { skin ->
                        if (QiblaRepository.isSkinUnlocked(context, skin)) {
                            selectedSkin = skin
                            QiblaRepository.setSelectedSkin(context, skin.id)
                            isCompassModalOpen = false
                            showGpsBanner("success", "${skin.name} applied ✨", 3000L)
                        } else {
                            skinToUnlock = skin
                        }
                    },
                    onDismiss = { isCompassModalOpen = false }
                )
            }

            // Unlock Confirmation Dialog for Premium Compass Skins
            if (skinToUnlock != null) {
                val skin = skinToUnlock!!
                AlertDialog(
                    onDismissRequest = { skinToUnlock = null },
                    containerColor = Color(0xFFFAF7EE),
                    shape = RoundedCornerShape(24.dp),
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "👑", fontSize = 20.sp)
                            Text(
                                text = "Unlock Premium Compass",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif,
                                color = Color(0xFF1B4D3E)
                            )
                        }
                    },
                    text = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Square Preview Thumbnail Box
                            Box(
                                modifier = Modifier
                                    .size(84.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        if (skin.backgroundColor != 0L) Color(skin.backgroundColor).copy(alpha = 0.15f)
                                        else Color(0xFFF1F5F9)
                                    )
                                    .border(1.5.dp, Color(0xFFC9A84C), RoundedCornerShape(16.dp))
                                    .padding(4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                if (skin.previewAsset.isNotEmpty()) {
                                    Image(
                                        painter = rememberAssetPainter(skin.previewAsset),
                                        contentDescription = skin.name,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Fit
                                    )
                                } else {
                                    Text(text = "🧭", fontSize = 32.sp)
                                }
                            }

                            Text(
                                text = skin.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A1408)
                            )

                            Text(
                                text = "Watch a short video ad to unlock this premium design free for 24 hours!",
                                fontSize = 13.sp,
                                color = Color(0xFF475569),
                                textAlign = TextAlign.Center
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                val s = skinToUnlock
                                skinToUnlock = null
                                if (s != null && activity != null) {
                                    AdMobRewardedManager.showAd(activity, s.name) {
                                        QiblaRepository.unlockSkinFor24Hours(context, s.id)
                                        selectedSkin = s
                                        QiblaRepository.setSelectedSkin(context, s.id)
                                        skinUnlockTrigger++
                                        isCompassModalOpen = false
                                        showGpsBanner("success", "${s.name} Unlocked for 24h! 🎉", 3500L)
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0F4735),
                                contentColor = Color(0xFFF7E2A8)
                            ),
                            shape = RoundedCornerShape(50.dp)
                        ) {
                            Text(
                                text = "🎬 Watch Ad to Unlock",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.5.sp
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { skinToUnlock = null }
                        ) {
                            Text(
                                text = "Cancel",
                                color = Color(0xFF64748B),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                )
            }

            // ══════════════════════════════════════════════════════
            // 7. MORE LANGUAGES MODAL (+20)
            // ══════════════════════════════════════════════════════
            if (isMoreLangOpen) {
                MoreLanguagesModal(
                    selectedCode = selectedLang,
                    onSelectLanguage = { code ->
                        selectedLang = code
                        PrayerRepository.saveLanguage(code)
                        isMoreLangOpen = false
                    },
                    onDismiss = { isMoreLangOpen = false }
                )
            }

            // Location Permission Required Rationale Dialog
            if (isLocationPermissionDialogOpen) {
                LocationPermissionDialog(
                    selectedLang = selectedLang,
                    featureName = "Qibla Direction",
                    onDismiss = { isLocationPermissionDialogOpen = false }
                )
            }

            // ══════════════════════════════════════════════════════
            // 8. LUXURY WHITE & EMERALD GREEN GPS FLOATING TOAST BANNER
            // ══════════════════════════════════════════════════════
            AnimatedVisibility(
                visible = gpsBannerText != null,
                enter = slideInVertically(initialOffsetY = { 80 }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { 80 }) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp, start = 16.dp, end = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .shadow(elevation = 12.dp, shape = RoundedCornerShape(50.dp), spotColor = Color(0x55047857))
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color.White)
                        .border(
                            width = 1.6.dp,
                            color = Color(0xFF047857),
                            shape = RoundedCornerShape(50.dp)
                        )
                        .clickable { gpsBannerText = null }
                        .padding(horizontal = 16.dp, vertical = 9.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Emerald Green Icon Badge
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE8F5E9)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (gpsBannerType == "loading") {
                                Text(
                                    text = "🛰️",
                                    fontSize = 13.sp,
                                    modifier = Modifier.rotate(gpsSpinAngle)
                                )
                            } else if (gpsBannerType == "success") {
                                Box(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF047857)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "✓",
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            } else {
                                Text(
                                    text = "📍",
                                    fontSize = 13.sp
                                )
                            }
                        }

                        // Text content: Crisp Deep Emerald Green
                        Text(
                            text = gpsBannerText ?: "",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF064E3B)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Metric Tile Item used in Card 1 (QIBLA, FACING, FINAL ANGLE, DISTANCE).
 */
@Composable
private fun MetricTileItem(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    valueColor: Color
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFFAF8F5))
            .border(1.4.dp, Color(0xFF047857), RoundedCornerShape(12.dp))
            .padding(vertical = 8.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = label.uppercase(),
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF7A6E63),
                letterSpacing = 0.5.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = value,
                fontSize = 14.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = valueColor,
                maxLines = 1
            )
        }
    }
}

/**
 * Ornate Top Header Bar for Qibla Screen matching user screenshot 1.
 */
@Composable
private fun QiblaHeaderTopBar(
    title: String,
    locationLabel: String,
    isDarkTheme: Boolean,
    onBack: () -> Unit,
    onRefreshLocation: () -> Unit,
    onToggleTheme: () -> Unit,
    onFavorite: () -> Unit,
    onSettings: () -> Unit
) {
    val headerGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF146450),
            Color(0xFF0C4A3A),
            Color(0xFF062018)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 6.dp, shape = RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .background(headerGradient)
                .border(
                    width = 1.2.dp,
                    color = Color(0xFFC9A84C).copy(alpha = 0.55f),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Back Button: ‹ in clean white button matching Home style
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0x33A07828), RoundedCornerShape(8.dp))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "‹",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0C4A3A)
                    )
                }

                // Center: Title + Live Current Location Pill
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = title.uppercase(),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Serif,
                        color = Color(0xFFFFFFFF),
                        letterSpacing = 0.6.sp,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Current Location Pill
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color.Black.copy(alpha = 0.28f))
                            .border(0.8.dp, Color(0xFFE8C878).copy(alpha = 0.55f), RoundedCornerShape(50.dp))
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { onRefreshLocation() }
                            .padding(horizontal = 9.dp, vertical = 2.5.dp)
                    ) {
                        Text(
                            text = "📍",
                            fontSize = 10.sp
                        )
                        Text(
                            text = locationLabel.ifEmpty { "Locating..." },
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFF0D080),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Right Actions: [ ❤️ ] [ ☀️/🌙 ] [ ⚙️ ] - Matching Home Header TopHeaderButtons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TopHeaderButton(
                        icon = "❤️",
                        onClick = onFavorite
                    )
                    TopHeaderButton(
                        icon = if (isDarkTheme) "🌙" else "☀️",
                        onClick = onToggleTheme
                    )
                    TopHeaderButton(
                        icon = "⚙️",
                        onClick = onSettings
                    )
                }
            }
        }
    }
}

/**
 * Isolated glowing halo animation to avoid 60fps main screen recompositions.
 */
@Composable
private fun GlowingAlignmentHalo() {
    val infiniteTransition = rememberInfiniteTransition(label = "halo")
    val haloScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "haloScale"
    )
    val haloAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.75f,
        animationSpec = infiniteRepeatable(
            animation = tween(900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "haloAlpha"
    )
    Box(
        modifier = Modifier
            .size(285.dp)
            .scale(haloScale)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF10B981).copy(alpha = haloAlpha),
                        Color(0xFF059669).copy(alpha = haloAlpha * 0.45f),
                        Color.Transparent
                    )
                )
            )
    )
}

/**
 * Classic Green Canvas Compass implementation.
 */
@Composable
private fun ClassicGreenCompassCanvas(
    heading: Double,
    finalAngle: Double,
    isAligned: Boolean
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2 * 0.92f

        // Outer green ring
        drawCircle(
            color = Color(0xFF0DBF7B),
            radius = radius,
            center = center
        )
        // Inner darker green
        drawCircle(
            color = Color(0xFF0A9F67),
            radius = radius * 0.82f,
            center = center
        )
        // Cream disc
        drawCircle(
            color = Color(0xFFFAF8F5),
            radius = radius * 0.70f,
            center = center
        )

        // Degree ticks (every 30 degrees)
        for (deg in 0 until 360 step 30) {
            val rad = Math.toRadians((deg - heading - 90))
            val startR = radius * 0.74f
            val endR = radius * 0.81f
            val start = Offset(center.x + (startR * kotlin.math.cos(rad)).toFloat(), center.y + (startR * kotlin.math.sin(rad)).toFloat())
            val end = Offset(center.x + (endR * kotlin.math.cos(rad)).toFloat(), center.y + (endR * kotlin.math.sin(rad)).toFloat())
            drawLine(
                color = Color.White,
                start = start,
                end = end,
                strokeWidth = 2.dp.toPx()
            )
        }

        // Pointer Needle pointing to Qibla (Scaled ~2cm larger)
        val needleRad = Math.toRadians((finalAngle - 90))
        val needleLen = radius * 0.94f
        val needleEnd = Offset(center.x + (needleLen * kotlin.math.cos(needleRad)).toFloat(), center.y + (needleLen * kotlin.math.sin(needleRad)).toFloat())
        drawLine(
            color = if (isAligned) Color(0xFF10B981) else Color(0xFFE11D48),
            start = center,
            end = needleEnd,
            strokeWidth = 5.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

/**
 * Mathematically and optically dead-centered lock badge for locked compass skins.
 */
@Composable
private fun CenteredLockBadge(
    modifier: Modifier = Modifier,
    badgeSize: androidx.compose.ui.unit.Dp = 20.dp
) {
    Box(
        modifier = modifier
            .size(badgeSize)
            .clip(CircleShape)
            .background(Color(0xD90A0F1D))
            .border(0.75.dp, Color.White.copy(alpha = 0.25f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(11.5.dp)) {
            val w = size.width
            val h = size.height
            val cx = w / 2f
            val cy = h / 2f

            // Total vertical height = 0.88f * h, centered exactly on cy with 0.00px offset
            // Total horizontal width = 0.86f * w, centered exactly on cx with 0.00px offset
            val offsetY = -h * 0.08f
            val strokeW = w * 0.16f
            val shackleRadius = w * 0.20f
            val arcTop = cy + offsetY - h * 0.44f + strokeW / 2f

            val bodyTop = cy + offsetY - h * 0.04f
            val bodyBottom = cy + offsetY + h * 0.44f
            val bodyH = bodyBottom - bodyTop
            val bodyW = w * 0.86f
            val bodyL = cx - bodyW / 2f

            // 1. Shackle (Silver metallic loop at top)
            val shackleL = cx - shackleRadius
            val shackleR = cx + shackleRadius
            val shacklePath = Path().apply {
                moveTo(shackleL, bodyTop + h * 0.08f)
                lineTo(shackleL, arcTop + shackleRadius)
                arcTo(
                    rect = Rect(
                        left = shackleL,
                        top = arcTop,
                        right = shackleR,
                        bottom = arcTop + shackleRadius * 2f
                    ),
                    startAngleDegrees = 180f,
                    sweepAngleDegrees = 180f,
                    forceMoveTo = false
                )
                lineTo(shackleR, bodyTop + h * 0.08f)
            }
            drawPath(
                path = shacklePath,
                color = Color(0xFFE2E8F0),
                style = Stroke(width = strokeW, cap = StrokeCap.Round)
            )

            // 2. Padlock Body (Warm luxury gold rounded rectangle)
            drawRoundRect(
                color = Color(0xFFF1D06C),
                topLeft = Offset(bodyL, bodyTop),
                size = Size(bodyW, bodyH),
                cornerRadius = CornerRadius(w * 0.14f, w * 0.14f)
            )

            // 3. Keyhole (Deep obsidian circle + slit)
            val keyholeY = (bodyTop + bodyBottom) / 2f - h * 0.02f
            drawCircle(
                color = Color(0xFF0F172A),
                radius = w * 0.075f,
                center = Offset(cx, keyholeY)
            )
            drawLine(
                color = Color(0xFF0F172A),
                start = Offset(cx, keyholeY),
                end = Offset(cx, keyholeY + h * 0.13f),
                strokeWidth = w * 0.08f,
                cap = StrokeCap.Round
            )
        }
    }
}

/**
 * Compass Designs Modal (Matching Image 3).
 */
@Composable
private fun CompassDesignsModal(
    skins: List<CompassSkin>,
    selectedSkinId: String,
    strings: QiblaStrings,
    unlockTrigger: Int = 0,
    onSelectSkin: (CompassSkin) -> Unit,
    onDismiss: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.55f))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onDismiss() },
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(Color(0xFFFAF7EE))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { /* consume clicks */ }
                .padding(top = 16.dp, bottom = 24.dp)
        ) {
            // Modal Top Bar: 🧭 Compass Designs + Close (✕)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "🧭", fontSize = 20.sp)
                    Text(
                        text = strings.compassDesigns,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        color = Color(0xFF1A1408)
                    )
                }

                // Close button (✕)
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEDE9DF))
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✕",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF555555)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Multi-row Grid of Compass Skins (3 columns utilizing full vertical space)
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(skins) { skin ->
                    val isSelected = (skin.id == selectedSkinId)
                    val isUnlocked = remember(skin.id, unlockTrigger) { QiblaRepository.isSkinUnlocked(context, skin) }
                    val remainingHours = remember(skin.id, unlockTrigger) { QiblaRepository.getSkinRemainingHours(context, skin.id) }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (isSelected) Color(0xFFC9A84C).copy(alpha = 0.08f)
                                else Color.Transparent
                            )
                            .clickable { onSelectSkin(skin) }
                            .padding(vertical = 4.dp, horizontal = 2.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Square Preview Thumbnail Box
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(
                                    if (skin.backgroundColor != 0L) Color(skin.backgroundColor).copy(alpha = 0.12f)
                                    else Color(0xFFF1F5F9)
                                )
                                .border(
                                    width = if (isSelected) 2.5.dp else 1.2.dp,
                                    color = if (isSelected) Color(0xFFC9A84C) else if (isUnlocked) Color(0xFF047857) else Color(0xFFE2E8F0),
                                    shape = RoundedCornerShape(18.dp)
                                )
                                .padding(5.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (skin.previewAsset.isNotEmpty()) {
                                Image(
                                    painter = rememberAssetPainter(skin.previewAsset),
                                    contentDescription = skin.name,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Fit
                                )
                            } else if (skin.id == "classic_green") {
                                Box(
                                    modifier = Modifier
                                        .size(62.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF0DBF7B)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "N",
                                        color = Color.White,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            // Active Check Badge, Lock Badge, or 24h Lightning Badge
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFC9A84C)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "✓",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            } else if (!isUnlocked) {
                                CenteredLockBadge(
                                    modifier = Modifier.align(Alignment.TopEnd)
                                )
                            } else if (skin.tier.equals("premium", ignoreCase = true)) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF047857))
                                        .border(0.75.dp, Color.White.copy(alpha = 0.35f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "⚡",
                                        fontSize = 10.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        // Status Pill Badge (Active, Free, ⏳ 24h Left, or 👑 PREMIUM)
                        val pillBg = when {
                            isSelected -> Color(0xFFC9A84C)
                            skin.tier.equals("free", ignoreCase = true) -> Color(0xFFDCFCE7)
                            isUnlocked -> Color(0xFFE0F2FE)
                            else -> Color(0xFFFEF3C7)
                        }
                        val pillTextColor = when {
                            isSelected -> Color.White
                            skin.tier.equals("free", ignoreCase = true) -> Color(0xFF16A34A)
                            isUnlocked -> Color(0xFF0284C7)
                            else -> Color(0xFFD97706)
                        }
                        val pillText = when {
                            isSelected -> "Active"
                            skin.tier.equals("free", ignoreCase = true) -> strings.free
                            isUnlocked -> "⏳ ${remainingHours}h"
                            else -> "👑 PREMIUM"
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(pillBg)
                                .padding(horizontal = 7.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = pillText,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = pillTextColor,
                                letterSpacing = 0.3.sp
                            )
                        }

                        // Skin Name
                        Text(
                            text = skin.name,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color(0xFF1B4D3E) else Color(0xFF334155),
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = 14.sp
                        )
                    }
                }
            }
        }
    }
}
