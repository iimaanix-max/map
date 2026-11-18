package com.tasbih.digitaltasbih.data

enum class MosqueSource {
    OVERPASS_OSM,
    OVERTURE_OFFLINE,
    MERGED
}

data class MosqueItem(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val address: String? = null,
    val distanceMeters: Float = 0f,
    val source: MosqueSource = MosqueSource.OVERTURE_OFFLINE,
    val osmId: Long? = null,
    val overtureId: String? = null,
    val isVerifiedMosque: Boolean = true
)

data class HalalFoodItem(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val address: String? = null,
    val distanceMeters: Float = 0f,
    val cuisine: String? = null,
    val dietHalalTag: String = "yes",
    val verificationLabel: String = "Reported halal (not certified)"
)

data class CitySearchResult(
    val name: String,
    val displayName: String,
    val latitude: Double,
    val longitude: Double
)
