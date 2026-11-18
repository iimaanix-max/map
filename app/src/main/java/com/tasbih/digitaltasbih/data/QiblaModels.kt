package com.tasbih.digitaltasbih.data

/**
 * Data models for Qibla Direction feature.
 */
data class CompassSkin(
    val id: String,
    val name: String,
    val tier: String = "free", // "free" | "premium"
    val outerAsset: String = "",
    val innerAsset: String = "",
    val needleAsset: String = "",
    val previewAsset: String = "",
    val isDefault: Boolean = false,
    val backgroundColor: Long = 0xFF1B4D3E
)

data class QiblaCalculation(
    val bearing: Double,
    val distanceKm: Double,
    val heading: Double?,
    val relativeAngle: Double?,
    val finalAngle: Double,
    val isAligned: Boolean,
    val accuracy: Float? = null,
    val accuracyState: String = "pending" // "good", "bad", "pending"
)
