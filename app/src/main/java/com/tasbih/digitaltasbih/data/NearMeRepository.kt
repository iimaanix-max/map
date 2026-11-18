package com.tasbih.digitaltasbih.data

import android.content.Context
import android.util.Log
import com.google.gson.JsonParser
import com.tasbih.digitaltasbih.data.mosque.MosqueRouteInfo
import com.tasbih.digitaltasbih.data.mosque.RouteStep
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.net.URLEncoder
import java.util.Locale
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.max

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

    private var lastNominatimRequestTime = 0L

    /**
     * Searches cities in India using OpenStreetMap Nominatim.
     * Enforces 1 req/sec rate limit per Nominatim policy with a 500ms debounce.
     */
    suspend fun searchCities(query: String): List<CitySearchResult> = withContext(Dispatchers.IO) {
        val clean = query.trim()
        if (clean.length < 2) return@withContext emptyList()

        val now = System.currentTimeMillis()
        val elapsed = now - lastNominatimRequestTime
        if (elapsed < 1000L) {
            delay(1000L - elapsed)
        }
        lastNominatimRequestTime = System.currentTimeMillis()

        val encoded = URLEncoder.encode(clean, "UTF-8")
        val url = "https://nominatim.openstreetmap.org/search?format=json&q=$encoded&countrycodes=in&limit=5&addressdetails=1"

        try {
            val response = NearMeHttpClient.get(url)
            if (response.isSuccessful && response.body.isNotBlank()) {
                val jsonArray = JsonParser.parseString(response.body).asJsonArray
                val results = mutableListOf<CitySearchResult>()
                for (i in 0 until jsonArray.size()) {
                    val obj = jsonArray[i].asJsonObject
                    val lat = obj.get("lat")?.asDouble ?: continue
                    val lon = obj.get("lon")?.asDouble ?: continue
                    val displayName = obj.get("display_name")?.asString ?: clean
                    val name = obj.get("name")?.asString
                        ?: displayName.split(",").firstOrNull()?.trim()
                        ?: clean

                    results.add(
                        CitySearchResult(
                            name = name,
                            displayName = displayName,
                            latitude = lat,
                            longitude = lon
                        )
                    )
                }
                results
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Nominatim search error: ${e.message}")
            emptyList()
        }
    }

    /**
     * Option A Navigation: Fetches route preview polyline and distance/ETA from OSRM.
     * When walking, duration is computed at realistic walking speed (1.38 m/s ~ 5 km/h)
     * and labeled as approximate.
     */
    suspend fun getRoute(
        startLat: Double,
        startLon: Double,
        destLat: Double,
        destLon: Double,
        isDriving: Boolean = false
    ): MosqueRouteInfo? = withContext(Dispatchers.IO) {
        val urlStr = "https://router.project-osrm.org/route/v1/driving/$startLon,$startLat;$destLon,$destLat?overview=full&geometries=polyline&steps=true"
        try {
            val response = NearMeHttpClient.get(urlStr)
            if (response.isSuccessful && response.body.isNotBlank()) {
                val json = JsonParser.parseString(response.body).asJsonObject
                if (json.get("code")?.asString == "Ok") {
                    val routes = json.getAsJsonArray("routes")
                    if (routes != null && routes.size() > 0) {
                        val route = routes.get(0).asJsonObject
                        val distance = route.get("distance")?.asDouble ?: 0.0
                        val rawDuration = route.get("duration")?.asDouble ?: 0.0

                        // For walking: estimate realistic walking speed (approx 5 km/h = 1.38 m/s)
                        val duration = if (isDriving) {
                            rawDuration
                        } else {
                            max(60.0, distance / 1.38)
                        }

                        val encodedPolyline = route.get("geometry")?.asString ?: ""
                        val polylinePoints = decodePolyline(encodedPolyline)

                        val stepsList = mutableListOf<RouteStep>()
                        val legs = route.getAsJsonArray("legs")
                        if (legs != null && legs.size() > 0) {
                            val leg = legs.get(0).asJsonObject
                            val stepsArray = leg.getAsJsonArray("steps")
                            if (stepsArray != null) {
                                for (j in 0 until stepsArray.size()) {
                                    val stepObj = stepsArray.get(j).asJsonObject
                                    val name = stepObj.get("name")?.asString ?: ""
                                    val maneuver = stepObj.getAsJsonObject("maneuver")
                                    val mType = maneuver?.get("type")?.asString ?: ""
                                    val mMod = maneuver?.get("modifier")?.asString ?: ""
                                    val sDist = stepObj.get("distance")?.asDouble ?: 0.0
                                    val sDur = stepObj.get("duration")?.asDouble ?: 0.0

                                    val instruction = buildInstruction(mType, mMod, name, sDist)
                                    stepsList.add(RouteStep(instruction, sDist, sDur, mType))
                                }
                            }
                        }

                        return@withContext MosqueRouteInfo(
                            distanceMeters = distance,
                            durationSeconds = duration,
                            polylinePoints = polylinePoints,
                            steps = stepsList,
                            isDriving = isDriving
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "OSRM route fetch failed: ${e.message}")
        }
        null
    }

    private fun buildInstruction(type: String, modifier: String, name: String, distance: Double): String {
        val road = if (name.isNotBlank()) "onto $name" else ""
        val distText = if (distance >= 1000) String.format(Locale.US, "%.1f km", distance / 1000.0) else "${distance.toInt()} m"

        return when (type) {
            "depart" -> "Head out $road ($distText)"
            "arrive" -> "Arrive at destination"
            "turn" -> "Turn ${modifier.replace("_", " ")} $road ($distText)"
            "continue" -> "Continue straight $road ($distText)"
            "new name" -> "Continue $road ($distText)"
            "fork" -> "Keep ${modifier.replace("_", " ")} $road ($distText)"
            "roundabout" -> "Take roundabout $road ($distText)"
            else -> "${type.replaceFirstChar { it.uppercase() }} $modifier $road ($distText)".trim()
        }
    }

    private fun decodePolyline(encoded: String): List<Pair<Double, Double>> {
        val poly = ArrayList<Pair<Double, Double>>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or ((b and 0x1f) shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if ((result and 1) != 0) (result shr 1).inv() else (result shr 1)
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or ((b and 0x1f) shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if ((result and 1) != 0) (result shr 1).inv() else (result shr 1)
            lng += dlng

            val pLat = lat.toDouble() / 1E5
            val pLng = lng.toDouble() / 1E5
            poly.add(Pair(pLat, pLng))
        }
        return poly
    }
}
