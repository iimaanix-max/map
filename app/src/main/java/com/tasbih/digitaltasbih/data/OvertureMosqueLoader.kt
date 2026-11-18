package com.tasbih.digitaltasbih.data

import android.content.Context
import android.util.Log
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import java.util.zip.GZIPInputStream
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * High-performance offline loader for bundled Overture India Mosques dataset.
 * Streams directly from assets/mosques/overture_mosques_india.json.gz with GZIPInputStream
 * and JsonReader, caching the parsed index in memory for sub-millisecond radius lookups.
 */
object OvertureMosqueLoader {
    private const val TAG = "OvertureMosqueLoader"
    private const val ASSET_PATH = "mosques/overture_mosques_india.json.gz"

    private val lock = Any()
    @Volatile
    private var cachedMosques: List<MosqueItem>? = null

    /**
     * Haversine distance in meters between two WGS-84 coordinates.
     * Pure Kotlin implementation (works identically on Android and JVM unit tests).
     */
    fun haversineDistanceMeters(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val earthRadiusMeters = 6371000.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2.0) * sin(dLat / 2.0) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2.0) * sin(dLon / 2.0)
        val c = 2.0 * atan2(sqrt(a), sqrt(1.0 - a))
        return (earthRadiusMeters * c).toFloat()
    }

    /**
     * Parses the compact JSON stream containing Overture mosques.
     * Schema: array of {"id":"...","n":"...","lat":...,"lon":...,"a":"..."}
     */
    fun parseFromStream(compressedStream: InputStream): List<MosqueItem> {
        val list = ArrayList<MosqueItem>(11000)
        GZIPInputStream(compressedStream).use { gzIn ->
            InputStreamReader(gzIn, Charsets.UTF_8).use { reader ->
                JsonReader(reader).use { json ->
                    if (json.peek() != JsonToken.BEGIN_ARRAY) {
                        return emptyList()
                    }
                    json.beginArray()
                    while (json.hasNext()) {
                        json.beginObject()
                        var id = ""
                        var name = ""
                        var lat = 0.0
                        var lon = 0.0
                        var address: String? = null

                        while (json.hasNext()) {
                            when (json.nextName()) {
                                "id" -> id = json.nextString()
                                "n" -> name = json.nextString()
                                "lat" -> lat = json.nextDouble()
                                "lon" -> lon = json.nextDouble()
                                "a" -> address = json.nextString()
                                else -> json.skipValue()
                            }
                        }
                        json.endObject()

                        if (id.isNotEmpty() && name.isNotEmpty() && lat != 0.0 && lon != 0.0) {
                            list.add(
                                MosqueItem(
                                    id = id,
                                    name = name,
                                    latitude = lat,
                                    longitude = lon,
                                    address = address,
                                    source = MosqueSource.OVERTURE_OFFLINE,
                                    overtureId = id,
                                    isVerifiedMosque = true
                                )
                            )
                        }
                    }
                    json.endArray()
                }
            }
        }
        return list
    }

    /**
     * Loads all India mosques, caching in memory on first call.
     */
    suspend fun getAllMosques(context: Context): List<MosqueItem> = withContext(Dispatchers.IO) {
        cachedMosques?.let { return@withContext it }
        synchronized(lock) {
            cachedMosques?.let { return@synchronized it }
            try {
                val t0 = System.currentTimeMillis()
                context.assets.open(ASSET_PATH).use { stream ->
                    val parsed = parseFromStream(stream)
                    cachedMosques = parsed
                    Log.d(TAG, "Loaded ${parsed.size} Overture India mosques in ${System.currentTimeMillis() - t0}ms")
                    parsed
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load Overture mosques asset: ${e.message}", e)
                emptyList()
            }
        }
    }

    /**
     * Queries mosques within [radiusKm] of [centerLat], [centerLon].
     * Uses bounding box pre-filtering followed by exact Haversine distance computation.
     */
    suspend fun getMosquesNear(
        context: Context,
        centerLat: Double,
        centerLon: Double,
        radiusKm: Double
    ): List<MosqueItem> = withContext(Dispatchers.IO) {
        val all = getAllMosques(context)
        if (all.isEmpty()) return@withContext emptyList()

        // 1 degree latitude ~ 111.0 km
        val latDelta = radiusKm / 111.0
        val cosLat = cos(Math.toRadians(centerLat))
        val lonDelta = if (cosLat > 0.01) radiusKm / (111.0 * cosLat) else radiusKm / 111.0

        val minLat = centerLat - latDelta
        val maxLat = centerLat + latDelta
        val minLon = centerLon - lonDelta
        val maxLon = centerLon + lonDelta

        val radiusMeters = (radiusKm * 1000.0).toFloat()
        val results = ArrayList<MosqueItem>()

        for (m in all) {
            if (m.latitude in minLat..maxLat && m.longitude in minLon..maxLon) {
                val dist = haversineDistanceMeters(centerLat, centerLon, m.latitude, m.longitude)
                if (dist <= radiusMeters) {
                    results.add(m.copy(distanceMeters = dist))
                }
            }
        }

        results.sortBy { it.distanceMeters }
        results
    }
}
