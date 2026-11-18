package com.tasbih.digitaltasbih.data

import android.content.Context
import android.util.Log
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URLEncoder
import java.util.Locale
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.floor

/**
 * Repository orchestrating live OpenStreetMap Overpass queries and offline Overture dataset.
 * Implements privacy-safe coordinate rounding, Overpass mirror failover, in-memory caching,
 * and 3-tier runtime deduplication.
 */
object NearMeRepository {
    private const val TAG = "NearMeRepository"
    private const val CACHE_TTL_MS = 2 * 60 * 60 * 1000L // 2 hours

    // Trusted privacy-conscious Overpass API mirrors
    private val OVERPASS_MIRRORS = listOf(
        "https://overpass-api.de/api/interpreter",
        "https://lz4.overpass-api.de/api/interpreter",
        "https://overpass.kumi.systems/api/interpreter",
        "https://overpass.private.coffee/api/interpreter"
    )

    private data class CacheEntry<T>(
        val data: List<T>,
        val timestamp: Long
    )

    private val mosqueCache = mutableMapOf<String, CacheEntry<MosqueItem>>()
    private val halalCache = mutableMapOf<String, CacheEntry<HalalFoodItem>>()

    /**
     * Rounds bounding box coordinates to 3 decimal places (~110 meters) to protect user location privacy.
     * Overpass convention: minLat, minLon, maxLat, maxLon
     */
    fun calculatePrivacySafeBbox(centerLat: Double, centerLon: Double, radiusKm: Double): String {
        val latDelta = radiusKm / 111.0
        val cosLat = cos(Math.toRadians(centerLat))
        val lonDelta = if (cosLat > 0.01) radiusKm / (111.0 * cosLat) else radiusKm / 111.0

        val minLat = floor((centerLat - latDelta) * 1000.0) / 1000.0
        val minLon = floor((centerLon - lonDelta) * 1000.0) / 1000.0
        val maxLat = ceil((centerLat + latDelta) * 1000.0) / 1000.0
        val maxLon = ceil((centerLon + lonDelta) * 1000.0) / 1000.0

        return String.format(Locale.US, "%.3f,%.3f,%.3f,%.3f", minLat, minLon, maxLat, maxLon)
    }

    private fun getCacheKey(centerLat: Double, centerLon: Double, radiusKm: Double): String {
        val rLat = String.format(Locale.US, "%.2f", centerLat)
        val rLon = String.format(Locale.US, "%.2f", centerLon)
        return "${rLat}_${rLon}_${radiusKm}"
    }

    /**
     * Fetches mosques by combining live OSM Overpass with offline Overture Places.
     * If Overpass fails or is unreachable, transparently falls back to offline Overture.
     */
    suspend fun getMosques(
        context: Context,
        centerLat: Double,
        centerLon: Double,
        radiusKm: Double,
        forceRefresh: Boolean = false
    ): List<MosqueItem> = withContext(Dispatchers.IO) {
        val cacheKey = getCacheKey(centerLat, centerLon, radiusKm)
        if (!forceRefresh) {
            mosqueCache[cacheKey]?.let { entry ->
                if (System.currentTimeMillis() - entry.timestamp < CACHE_TTL_MS) {
                    Log.d(TAG, "Returning ${entry.data.size} mosques from in-memory cache")
                    return@withContext entry.data
                }
            }
        }

        // 1. Load offline Overture mosques (guaranteed fallback)
        val overtureMosques = OvertureMosqueLoader.getMosquesNear(context, centerLat, centerLon, radiusKm)

        // 2. Fetch live OSM mosques from Overpass mirrors
        val osmMosques = fetchLiveOsmMosques(centerLat, centerLon, radiusKm)

        // 3. Deduplicate and merge at runtime
        val mergedList = if (osmMosques.isNotEmpty()) {
            MosqueDedupEngine.deduplicateAndMerge(osmMosques, overtureMosques, centerLat, centerLon)
        } else {
            Log.d(TAG, "Live OSM unavailable or empty; using ${overtureMosques.size} offline Overture mosques")
            overtureMosques
        }

        mosqueCache[cacheKey] = CacheEntry(mergedList, System.currentTimeMillis())
        mergedList
    }

    /**
     * Executes live Overpass query across mirror fallback chain with privacy-safe bbox.
     */
    private suspend fun fetchLiveOsmMosques(
        centerLat: Double,
        centerLon: Double,
        radiusKm: Double
    ): List<MosqueItem> {
        val bbox = calculatePrivacySafeBbox(centerLat, centerLon, radiusKm)
        val query = """
            [out:json][timeout:15];
            (
              node["amenity"="place_of_worship"]["religion"="muslim"]($bbox);
              way["amenity"="place_of_worship"]["religion"="muslim"]($bbox);
              relation["amenity"="place_of_worship"]["religion"="muslim"]($bbox);
            );
            out center tags;
        """.trimIndent()

        val postData = "data=" + URLEncoder.encode(query, "UTF-8")

        for (mirror in OVERPASS_MIRRORS) {
            try {
                Log.d(TAG, "Querying Overpass mirror: $mirror")
                val response = NearMeHttpClient.post(mirror, postData)
                if (response.isSuccessful && response.body.isNotBlank()) {
                    val items = parseOsmMosques(response.body, centerLat, centerLon, radiusKm)
                    Log.d(TAG, "Mirror $mirror returned ${items.size} authentic mosques")
                    return items
                } else {
                    Log.w(TAG, "Mirror $mirror returned HTTP ${response.statusCode}")
                }
            } catch (e: Exception) {
                Log.w(TAG, "Error connecting to $mirror: ${e.message}")
            }
        }
        return emptyList()
    }

    private fun parseOsmMosques(
        jsonString: String,
        centerLat: Double,
        centerLon: Double,
        radiusKm: Double
    ): List<MosqueItem> {
        val results = mutableListOf<MosqueItem>()
        val radiusMeters = (radiusKm * 1000.0).toFloat()

        try {
            val root = JsonParser.parseString(jsonString).asJsonObject
            val elements = root.getAsJsonArray("elements") ?: return emptyList()

            for (i in 0 until elements.size()) {
                val el = elements[i].asJsonObject
                val id = el.get("id")?.asLong ?: continue

                var lat = 0.0
                var lon = 0.0

                if (el.has("lat") && el.has("lon")) {
                    lat = el.get("lat").asDouble
                    lon = el.get("lon").asDouble
                } else if (el.has("center")) {
                    val center = el.getAsJsonObject("center")
                    lat = center.get("lat").asDouble
                    lon = center.get("lon").asDouble
                }

                if (lat == 0.0 || lon == 0.0) continue

                val tagsMap = mutableMapOf<String, String>()
                if (el.has("tags")) {
                    val tags = el.getAsJsonObject("tags")
                    for (entry in tags.entrySet()) {
                        tagsMap[entry.key] = entry.value.asString
                    }
                }

                val name = tagsMap["name"]
                    ?: tagsMap["name:en"]
                    ?: tagsMap["name:ur"]
                    ?: tagsMap["name:hi"]
                    ?: "Masjid"

                // Strict filter: exclude standalone dargahs/madarsas
                if (!MosqueDedupEngine.isAuthenticMosque(name, tagsMap)) {
                    continue
                }

                val dist = OvertureMosqueLoader.haversineDistanceMeters(centerLat, centerLon, lat, lon)
                if (dist > radiusMeters) continue

                val addressParts = listOfNotNull(
                    tagsMap["addr:street"],
                    tagsMap["addr:suburb"],
                    tagsMap["addr:city"]
                ).filter { it.isNotBlank() }
                val address = if (addressParts.isNotEmpty()) addressParts.joinToString(", ") else null

                results.add(
                    MosqueItem(
                        id = "osm_$id",
                        name = name,
                        latitude = lat,
                        longitude = lon,
                        address = address,
                        distanceMeters = dist,
                        source = MosqueSource.OVERPASS_OSM,
                        osmId = id,
                        isVerifiedMosque = true
                    )
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse OSM mosques JSON: ${e.message}", e)
        }

        results.sortBy { it.distanceMeters }
        return results
    }

    /**
     * Fetches reported halal food outlets using strict OSM tag [diet:halal=yes|only].
     * Zero name heuristics are applied.
     */
    suspend fun getHalalFood(
        centerLat: Double,
        centerLon: Double,
        radiusKm: Double,
        forceRefresh: Boolean = false
    ): List<HalalFoodItem> = withContext(Dispatchers.IO) {
        val cacheKey = getCacheKey(centerLat, centerLon, radiusKm)
        if (!forceRefresh) {
            halalCache[cacheKey]?.let { entry ->
                if (System.currentTimeMillis() - entry.timestamp < CACHE_TTL_MS) {
                    return@withContext entry.data
                }
            }
        }

        val bbox = calculatePrivacySafeBbox(centerLat, centerLon, radiusKm)
        val query = """
            [out:json][timeout:15];
            (
              node["diet:halal"~"^(yes|only)$"]($bbox);
              way["diet:halal"~"^(yes|only)$"]($bbox);
            );
            out center tags;
        """.trimIndent()

        val postData = "data=" + URLEncoder.encode(query, "UTF-8")
        var items = emptyList<HalalFoodItem>()

        for (mirror in OVERPASS_MIRRORS) {
            try {
                val response = NearMeHttpClient.post(mirror, postData)
                if (response.isSuccessful && response.body.isNotBlank()) {
                    items = parseHalalFood(response.body, centerLat, centerLon, radiusKm)
                    break
                }
            } catch (e: Exception) {
                Log.w(TAG, "Halal query error on $mirror: ${e.message}")
            }
        }

        halalCache[cacheKey] = CacheEntry(items, System.currentTimeMillis())
        items
    }

    private fun parseHalalFood(
        jsonString: String,
        centerLat: Double,
        centerLon: Double,
        radiusKm: Double
    ): List<HalalFoodItem> {
        val results = mutableListOf<HalalFoodItem>()
        val radiusMeters = (radiusKm * 1000.0).toFloat()

        try {
            val root = JsonParser.parseString(jsonString).asJsonObject
            val elements = root.getAsJsonArray("elements") ?: return emptyList()

            for (i in 0 until elements.size()) {
                val el = elements[i].asJsonObject
                val id = el.get("id")?.asLong ?: continue

                var lat = 0.0
                var lon = 0.0

                if (el.has("lat") && el.has("lon")) {
                    lat = el.get("lat").asDouble
                    lon = el.get("lon").asDouble
                } else if (el.has("center")) {
                    val center = el.getAsJsonObject("center")
                    lat = center.get("lat").asDouble
                    lon = center.get("lon").asDouble
                }

                if (lat == 0.0 || lon == 0.0) continue

                val tagsMap = mutableMapOf<String, String>()
                if (el.has("tags")) {
                    val tags = el.getAsJsonObject("tags")
                    for (entry in tags.entrySet()) {
                        tagsMap[entry.key] = entry.value.asString
                    }
                }

                val dietHalal = tagsMap["diet:halal"]?.lowercase() ?: continue
                if (dietHalal != "yes" && dietHalal != "only") continue

                val dist = OvertureMosqueLoader.haversineDistanceMeters(centerLat, centerLon, lat, lon)
                if (dist > radiusMeters) continue

                val name = tagsMap["name"] ?: tagsMap["name:en"] ?: "Halal Outlet"
                val cuisine = tagsMap["cuisine"]

                val addressParts = listOfNotNull(
                    tagsMap["addr:street"],
                    tagsMap["addr:suburb"],
                    tagsMap["addr:city"]
                ).filter { it.isNotBlank() }
                val address = if (addressParts.isNotEmpty()) addressParts.joinToString(", ") else null

                results.add(
                    HalalFoodItem(
                        id = "halal_$id",
                        name = name,
                        latitude = lat,
                        longitude = lon,
                        address = address,
                        distanceMeters = dist,
                        cuisine = cuisine,
                        dietHalalTag = dietHalal,
                        verificationLabel = "Reported halal (not certified)"
                    )
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse Halal Food JSON: ${e.message}", e)
        }

        results.sortBy { it.distanceMeters }
        return results
    }
}
