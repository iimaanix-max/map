package com.tasbih.digitaltasbih.data.mosque

data class NearbyMosque(
    val id: String,
    val name: String,
    val address: String,
    val lat: Double,
    val lon: Double,
    val distanceMeters: Int,
    val bearingDegrees: Float,
    val estimatedWalkMinutes: Int,
    val estimatedDriveMinutes: Int,
    val isHalal: Boolean = false,
    val placeType: String = "mosque"
)

data class RouteStep(
    val instruction: String,
    val distanceMeters: Double,
    val durationSeconds: Double,
    val maneuverType: String = ""
)

data class MosqueRouteInfo(
    val distanceMeters: Double,
    val durationSeconds: Double,
    val polylinePoints: List<Pair<Double, Double>>,
    val steps: List<RouteStep>,
    val isDriving: Boolean = false
)
