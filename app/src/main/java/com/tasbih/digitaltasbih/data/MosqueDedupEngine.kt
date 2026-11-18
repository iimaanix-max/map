package com.tasbih.digitaltasbih.data

/**
 * Pure Kotlin Deduplication and Filtering Engine for Mosque Data.
 * Merges live OSM Overpass POIs and offline Overture Places POIs using a 3-tier spatial
 * deduplication algorithm with sectarian conflict checks, and strictly excludes standalone
 * dargahs/madarsas unless an affirmative mosque token is present.
 */
object MosqueDedupEngine {

    private val NON_MOSQUE_EXCLUDE = Regex(
        "\\b(dargah|darga|mazar|astana|aastana|bargah|khangah|khanqah|tomb|shrine|qabristan|graveyard|cemetry|cemetery|madarsa|madrasa|maktab)\\b",
        RegexOption.IGNORE_CASE
    )

    private val MOSQUE_AFFIRMATIVE = Regex(
        "\\b(masjid|mazjid|mosque|jama|jame)\\b",
        RegexOption.IGNORE_CASE
    )

    private val HONORIFIC_SHRINES = Regex(
        "\\b(sarkar|shahvali|wali)\\b",
        RegexOption.IGNORE_CASE
    )

    private val CONFLICT_GROUPS = listOf(
        Pair(setOf("shia"), setOf("sunni")),
        Pair(setOf("badi", "bada", "kalan"), setOf("choti", "chhota", "khurd")),
        Pair(setOf("ahle-hadith", "salafi"), setOf("hanafi", "barelvi", "deobandi"))
    )

    private val GENERIC_WORDS = setOf(
        "masjid", "mazjid", "mosque", "jama", "jame", "sunni", "hanafi"
    )

    /**
     * Determines whether a record is an authentic prayer mosque.
     * Rule: If name has an affirmative token ("Masjid", "Mosque", "Jama"), it is ALWAYS KEPT.
     * Otherwise, standalone dargahs, shrines, tombs, or madarsas are DROPPED.
     */
    fun isAuthenticMosque(name: String, tags: Map<String, String>? = null): Boolean {
        val cleanName = name.trim()
        if (cleanName.isBlank()) return false

        // 1. Affirmative match: prayer facilities with "masjid", "mosque", or "jama" in title
        if (MOSQUE_AFFIRMATIVE.containsMatchIn(cleanName)) {
            return true
        }

        // 2. Exclude non-mosque entities (dargah, mazar, astana, shrine, graveyard, standalone madarsa)
        if (NON_MOSQUE_EXCLUDE.containsMatchIn(cleanName)) {
            return false
        }

        // 3. Exclude honorific shrines/saints without mosque tokens
        if (HONORIFIC_SHRINES.containsMatchIn(cleanName)) {
            return false
        }

        // 4. Exclude OSM tagged tombs or schools without prayer tag
        tags?.let {
            if (it["historic"] == "tomb" || it.containsKey("tomb") || it["amenity"] in listOf("school", "college")) {
                return false
            }
        }

        return true
    }

    /**
     * Splits a name into alphanumeric lowercase tokens.
     */
    fun tokenize(name: String): Set<String> {
        return Regex("\\b\\w+\\b")
            .findAll(name.lowercase())
            .map { it.value }
            .toSet()
    }

    /**
     * Returns tokens excluding generic words (masjid, jama, sunni, etc.).
     */
    fun getCoreTokens(name: String): Set<String> {
        return tokenize(name) - GENERIC_WORDS
    }

    /**
     * Calculates Jaccard similarity index between two token sets: |A ∩ B| / |A ∪ B|.
     */
    fun calculateJaccard(set1: Set<String>, set2: Set<String>): Float {
        if (set1.isEmpty() || set2.isEmpty()) return 0f
        val intersection = (set1 intersect set2).size
        val union = (set1 union set2).size
        return if (union > 0) intersection.toFloat() / union.toFloat() else 0f
    }

    /**
     * Checks if two names have conflicting sectarian or size attributes (e.g., Shia vs Sunni, Badi vs Choti).
     */
    fun hasConflict(name1: String, name2: String): Boolean {
        val t1 = tokenize(name1)
        val t2 = tokenize(name2)
        for ((groupA, groupB) in CONFLICT_GROUPS) {
            val aIn1 = (t1 intersect groupA).isNotEmpty()
            val bIn1 = (t1 intersect groupB).isNotEmpty()
            val aIn2 = (t2 intersect groupA).isNotEmpty()
            val bIn2 = (t2 intersect groupB).isNotEmpty()

            if ((aIn1 && bIn2) || (bIn1 && aIn2)) {
                return true
            }
        }
        return false
    }

    /**
     * Determines if two mosque records refer to the same physical mosque.
     * Uses 3-Tier spatial deduplication:
     * - Tier 1: Micro-proximity (<= 5.0m): Always merge if no conflict.
     * - Tier 2: Close (5.0m < d <= 35.0m): Merge if common core non-generic token or Jaccard >= 0.35.
     * - Tier 3: Medium (35.0m < d <= 100.0m): Merge only if core Jaccard >= 0.65.
     * - Distance > 100.0m: Never merge.
     */
    fun shouldMerge(m1: MosqueItem, m2: MosqueItem): Pair<Boolean, Float> {
        val d = OvertureMosqueLoader.haversineDistanceMeters(m1.latitude, m1.longitude, m2.latitude, m2.longitude)
        if (d > 100.0f) return Pair(false, d)
        if (hasConflict(m1.name, m2.name)) return Pair(false, d)

        // Tier 1: Micro-proximity (<= 5.0m)
        if (d <= 5.0f) return Pair(true, d)

        val core1 = getCoreTokens(m1.name)
        val core2 = getCoreTokens(m2.name)

        // Tier 2: Close (5.0m < d <= 35.0m)
        if (d <= 35.0f) {
            if ((core1 intersect core2).isNotEmpty()) return Pair(true, d)
            val jaccard = calculateJaccard(tokenize(m1.name), tokenize(m2.name))
            if (jaccard >= 0.35f) return Pair(true, d)
            return Pair(false, d)
        }

        // Tier 3: Medium (35.0m < d <= 100.0m)
        val coreJaccard = calculateJaccard(core1, core2)
        if (coreJaccard >= 0.65f) return Pair(true, d)

        return Pair(false, d)
    }

    /**
     * Merges an OSM record and an Overture record into a single consolidated MosqueItem.
     */
    fun mergeMosquePair(
        osm: MosqueItem,
        overture: MosqueItem,
        userLat: Double? = null,
        userLon: Double? = null
    ): MosqueItem {
        val chosenName = if (osm.name.trim().equals("Masjid", ignoreCase = true) && overture.name.isNotBlank()) {
            overture.name
        } else {
            osm.name
        }

        val chosenAddress = when {
            !osm.address.isNullOrBlank() -> osm.address
            !overture.address.isNullOrBlank() -> overture.address
            else -> null
        }

        val dist = if (userLat != null && userLon != null) {
            OvertureMosqueLoader.haversineDistanceMeters(userLat, userLon, osm.latitude, osm.longitude)
        } else {
            osm.distanceMeters
        }

        return MosqueItem(
            id = "merged_${osm.id}_${overture.id}",
            name = chosenName,
            latitude = osm.latitude,
            longitude = osm.longitude,
            address = chosenAddress,
            distanceMeters = dist,
            source = MosqueSource.MERGED,
            osmId = osm.osmId ?: osm.id.toLongOrNull(),
            overtureId = overture.overtureId ?: overture.id,
            isVerifiedMosque = true
        )
    }

    /**
     * Deduplicates and merges raw OSM and Overture lists.
     * Strictly applies authenticity filtering and 3-tier spatial merging.
     */
    fun deduplicateAndMerge(
        osmRaw: List<MosqueItem>,
        overtureRaw: List<MosqueItem>,
        userLat: Double? = null,
        userLon: Double? = null
    ): List<MosqueItem> {
        val osmFiltered = osmRaw.filter { isAuthenticMosque(it.name) }
        val overtureFiltered = overtureRaw.filter { isAuthenticMosque(it.name) }

        val mergedResults = mutableListOf<MosqueItem>()
        val unmatchedOsm = osmFiltered.toMutableList()
        val matchedOvertureIds = mutableSetOf<String>()

        for (osm in osmFiltered) {
            var bestOverture: MosqueItem? = null
            var minDistance = Float.MAX_VALUE

            for (ov in overtureFiltered) {
                if (ov.id in matchedOvertureIds) continue
                val (canMerge, dist) = shouldMerge(osm, ov)
                if (canMerge && dist < minDistance) {
                    bestOverture = ov
                    minDistance = dist
                }
            }

            if (bestOverture != null) {
                val merged = mergeMosquePair(osm, bestOverture, userLat, userLon)
                mergedResults.add(merged)
                unmatchedOsm.remove(osm)
                matchedOvertureIds.add(bestOverture.id)
            }
        }

        // Add remaining unmatched OSM items
        for (osm in unmatchedOsm) {
            val dist = if (userLat != null && userLon != null) {
                OvertureMosqueLoader.haversineDistanceMeters(userLat, userLon, osm.latitude, osm.longitude)
            } else {
                osm.distanceMeters
            }
            mergedResults.add(osm.copy(distanceMeters = dist, source = MosqueSource.OVERPASS_OSM))
        }

        // Add remaining unmatched Overture items
        for (ov in overtureFiltered) {
            if (ov.id !in matchedOvertureIds) {
                val dist = if (userLat != null && userLon != null) {
                    OvertureMosqueLoader.haversineDistanceMeters(userLat, userLon, ov.latitude, ov.longitude)
                } else {
                    ov.distanceMeters
                }
                mergedResults.add(ov.copy(distanceMeters = dist, source = MosqueSource.OVERTURE_OFFLINE))
            }
        }

        mergedResults.sortBy { it.distanceMeters }
        return mergedResults
    }
}
