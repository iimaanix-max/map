package com.tasbih.digitaltasbih.data

import android.content.Context
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

object QiblaRepository {
    const val KAABA_LAT = 21.4224779
    const val KAABA_LON = 39.8251832

    private const val PREF_NAME = "qibla_prefs"
    private const val KEY_SKIN_ID = "selected_compass_skin_id"
    private const val KEY_SKIN_UNLOCK_PREFIX = "skin_unlock_exp_"

    private var appContext: Context? = null
    private var cachedLeafletCss: String? = null
    private var cachedLeafletJs: String? = null

    fun init(context: Context) {
        appContext = context.applicationContext
        warmupLeafletAssets(context)
    }

    fun warmupLeafletAssets(context: Context? = appContext) {
        val ctx = context ?: appContext ?: return
        if (cachedLeafletCss == null) {
            try {
                cachedLeafletCss = ctx.assets.open("leaflet/leaflet.css").bufferedReader().use { it.readText() }
            } catch (_: Exception) {}
        }
        if (cachedLeafletJs == null) {
            try {
                val rawJs = ctx.assets.open("leaflet/leaflet.js").bufferedReader().use { it.readText() }
                cachedLeafletJs = rawJs.replace("</script>", "<\\/script>")
            } catch (_: Exception) {}
        }
    }

    fun getLeafletCss(context: Context? = appContext): String {
        warmupLeafletAssets(context)
        return cachedLeafletCss ?: ""
    }

    fun getLeafletJs(context: Context? = appContext): String {
        warmupLeafletAssets(context)
        return cachedLeafletJs ?: ""
    }

    val DEFAULT_SKINS = listOf(
        CompassSkin(
            id = "emerald_gold",
            name = "Imperial Emerald",
            tier = "free",
            outerAsset = "media/compass_outer_emerald_gold.webp",
            innerAsset = "media/compass_inner_emerald_gold.webp",
            needleAsset = "media/compass_needle_emerald_gold.webp",
            previewAsset = "media/compass_preview_emerald_gold.webp",
            isDefault = true,
            backgroundColor = 0xFF0B3B24
        ),
        CompassSkin(
            id = "royal_amethyst",
            name = "Royal Amethyst",
            tier = "premium",
            outerAsset = "media/compass_outer_royal_amethyst.webp",
            innerAsset = "media/compass_inner_royal_amethyst.webp",
            needleAsset = "media/compass_needle_royal_amethyst.webp",
            previewAsset = "media/compass_preview_royal_amethyst.webp",
            isDefault = false,
            backgroundColor = 0xFF3B1E54
        ),
        CompassSkin(
            id = "turquoise_pearl",
            name = "Celestial Turquoise",
            tier = "premium",
            outerAsset = "media/compass_outer_turquoise_pearl.webp",
            innerAsset = "media/compass_inner_turquoise_pearl.webp",
            needleAsset = "media/compass_needle_turquoise_pearl.webp",
            previewAsset = "media/compass_preview_turquoise_pearl.webp",
            isDefault = false,
            backgroundColor = 0xFF0F4C5C
        ),
        CompassSkin(
            id = "imperial_ruby",
            name = "Imperial Ruby",
            tier = "premium",
            outerAsset = "media/compass_outer_imperial_ruby.webp",
            innerAsset = "media/compass_inner_imperial_ruby.webp",
            needleAsset = "media/compass_needle_imperial_ruby.webp",
            previewAsset = "media/compass_preview_imperial_ruby.webp",
            isDefault = false,
            backgroundColor = 0xFF581845
        ),
        CompassSkin(
            id = "sapphire_lanterns",
            name = "Royal Sapphire",
            tier = "premium",
            outerAsset = "media/compass_outer_sapphire_lanterns.webp",
            innerAsset = "media/compass_inner_sapphire_lanterns.webp",
            needleAsset = "media/compass_needle_sapphire_lanterns.webp",
            previewAsset = "media/compass_preview_sapphire_lanterns.webp",
            isDefault = false,
            backgroundColor = 0xFF0D1B2A
        ),
        CompassSkin(
            id = "turquoise_lotus",
            name = "Turquoise Lotus",
            tier = "premium",
            outerAsset = "media/compass_outer_turquoise_lotus.webp",
            innerAsset = "media/compass_inner_turquoise_lotus.webp",
            needleAsset = "media/compass_needle_turquoise_lotus.webp",
            previewAsset = "media/compass_preview_turquoise_lotus.webp",
            isDefault = false,
            backgroundColor = 0xFF144246
        ),
        CompassSkin(
            id = "crimson_sultan",
            name = "Crimson Sultan",
            tier = "premium",
            outerAsset = "media/compass_outer_crimson_sultan.webp",
            innerAsset = "media/compass_inner_crimson_sultan.webp",
            needleAsset = "media/compass_needle_crimson_sultan.webp",
            previewAsset = "media/compass_preview_crimson_sultan.webp",
            isDefault = false,
            backgroundColor = 0xFF3E1210
        ),
        CompassSkin(
            id = "midnight_star",
            name = "Midnight Star",
            tier = "premium",
            outerAsset = "media/compass_outer_midnight_star.webp",
            innerAsset = "media/compass_inner_midnight_star.webp",
            needleAsset = "media/compass_needle_midnight_star.webp",
            previewAsset = "media/compass_preview_midnight_star.webp",
            isDefault = false,
            backgroundColor = 0xFF0D213F
        ),
        CompassSkin(
            id = "pearl_aquamarine",
            name = "Pearl Aquamarine",
            tier = "premium",
            outerAsset = "media/compass_outer_pearl_aquamarine.webp",
            innerAsset = "media/compass_inner_pearl_aquamarine.webp",
            needleAsset = "media/compass_needle_pearl_aquamarine.webp",
            previewAsset = "media/compass_preview_pearl_aquamarine.webp",
            isDefault = false,
            backgroundColor = 0xFF1E3A3A
        ),
        CompassSkin(
            id = "ottoman_emerald",
            name = "Ottoman Jade",
            tier = "premium",
            outerAsset = "media/compass_outer_ottoman_emerald.webp",
            innerAsset = "media/compass_inner_ottoman_emerald.webp",
            needleAsset = "media/compass_needle_ottoman_emerald.webp",
            previewAsset = "media/compass_preview_ottoman_emerald.webp",
            isDefault = false,
            backgroundColor = 0xFF0F3B2E
        ),
        CompassSkin(
            id = "silver_sapphire",
            name = "Silver Sapphire",
            tier = "premium",
            outerAsset = "media/compass_outer_silver_sapphire.webp",
            innerAsset = "media/compass_inner_silver_sapphire.webp",
            needleAsset = "media/compass_needle_silver_sapphire.webp",
            previewAsset = "media/compass_preview_silver_sapphire.webp",
            isDefault = false,
            backgroundColor = 0xFF16253B
        ),
        CompassSkin(
            id = "celestial_navy",
            name = "Celestial Navy",
            tier = "premium",
            outerAsset = "media/compass_outer_celestial_navy.webp",
            innerAsset = "media/compass_inner_celestial_navy.webp",
            needleAsset = "media/compass_needle_celestial_navy.webp",
            previewAsset = "media/compass_preview_celestial_navy.webp",
            isDefault = false,
            backgroundColor = 0xFF122238
        ),
        CompassSkin(
            id = "golden_astrolabe",
            name = "Golden Astrolabe",
            tier = "premium",
            outerAsset = "media/compass_outer_golden_astrolabe.webp",
            innerAsset = "media/compass_inner_golden_astrolabe.webp",
            needleAsset = "media/compass_needle_golden_astrolabe.webp",
            previewAsset = "media/compass_preview_golden_astrolabe.webp",
            isDefault = false,
            backgroundColor = 0xFF3A2E1A
        ),
        CompassSkin(
            id = "rose_mauve",
            name = "Rose Mauve",
            tier = "premium",
            outerAsset = "media/compass_outer_rose_mauve.webp",
            innerAsset = "media/compass_inner_rose_mauve.webp",
            needleAsset = "media/compass_needle_rose_mauve.webp",
            previewAsset = "media/compass_preview_rose_mauve.webp",
            isDefault = false,
            backgroundColor = 0xFF3D212B
        ),
        CompassSkin(
            id = "bronze_sultan",
            name = "Bronze Sultan",
            tier = "premium",
            outerAsset = "media/compass_outer_bronze_sultan.webp",
            innerAsset = "media/compass_inner_bronze_sultan.webp",
            needleAsset = "media/compass_needle_bronze_sultan.webp",
            previewAsset = "media/compass_preview_bronze_sultan.webp",
            isDefault = false,
            backgroundColor = 0xFF421B13
        ),
        CompassSkin(
            id = "platinum_silver",
            name = "Platinum Silver",
            tier = "premium",
            outerAsset = "media/compass_outer_platinum_silver.webp",
            innerAsset = "media/compass_inner_platinum_silver.webp",
            needleAsset = "media/compass_needle_platinum_silver.webp",
            previewAsset = "media/compass_preview_platinum_silver.webp",
            isDefault = false,
            backgroundColor = 0xFF212832
        ),
        CompassSkin(
            id = "classic_green",
            name = "Classic Green",
            tier = "premium",
            outerAsset = "",
            innerAsset = "",
            needleAsset = "",
            previewAsset = "",
            isDefault = false,
            backgroundColor = 0xFF0DBF7B
        )
    )

    fun getSkins(): List<CompassSkin> = DEFAULT_SKINS

    /**
     * Checks if a compass skin is unlocked (either free tier or currently within 24h rewarded window).
     */
    fun isSkinUnlocked(context: Context, skin: CompassSkin): Boolean {
        if (skin.tier.equals("free", ignoreCase = true)) return true
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val expireTime = prefs.getLong(KEY_SKIN_UNLOCK_PREFIX + skin.id, 0L)
        return System.currentTimeMillis() < expireTime
    }

    /**
     * Unlocks a premium compass skin for 24 hours from current timestamp.
     */
    fun unlockSkinFor24Hours(context: Context, skinId: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val expireTime = System.currentTimeMillis() + (24 * 60 * 60 * 1000L)
        prefs.edit().putLong(KEY_SKIN_UNLOCK_PREFIX + skinId, expireTime).apply()
    }

    /**
     * Returns remaining hours until unlock expires, or 0 if expired/not unlocked.
     */
    fun getSkinRemainingHours(context: Context, skinId: String): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val expireTime = prefs.getLong(KEY_SKIN_UNLOCK_PREFIX + skinId, 0L)
        val diff = expireTime - System.currentTimeMillis()
        if (diff <= 0) return 0
        return ((diff / (1000L * 60 * 60)) + 1).toInt().coerceAtMost(24)
    }

    fun getSelectedSkin(context: Context): CompassSkin {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val id = prefs.getString(KEY_SKIN_ID, "emerald_gold") ?: "emerald_gold"
        return DEFAULT_SKINS.find { it.id == id } ?: DEFAULT_SKINS.first()
    }

    fun setSelectedSkin(context: Context, id: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_SKIN_ID, id).apply()
    }

    fun normalize(deg: Double): Double = ((deg % 360.0) + 360.0) % 360.0

    fun angleDiff(target: Double, current: Double): Double = ((target - current + 540.0) % 360.0) - 180.0

    fun lerpAngle(current: Double, target: Double, factor: Double): Double {
        val diff = angleDiff(target, current)
        return normalize(current + diff * factor)
    }

    fun computeBearing(lat: Double, lon: Double): Double {
        val phi1 = Math.toRadians(lat)
        val phi2 = Math.toRadians(KAABA_LAT)
        val dLon = Math.toRadians(KAABA_LON - lon)
        val y = sin(dLon) * cos(phi2)
        val x = cos(phi1) * sin(phi2) - sin(phi1) * cos(phi2) * cos(dLon)
        return normalize(Math.toDegrees(atan2(y, x)))
    }

    fun computeDistanceKm(lat: Double, lon: Double): Double {
        val r = 6371.0
        val dLat = Math.toRadians(KAABA_LAT - lat)
        val dLon = Math.toRadians(KAABA_LON - lon)
        val a = sin(dLat / 2.0).pow(2) + cos(Math.toRadians(lat)) * cos(Math.toRadians(KAABA_LAT)) * sin(dLon / 2.0).pow(2)
        return 2.0 * r * atan2(sqrt(a), sqrt(1.0 - a))
    }

    fun generateLeafletMapHtml(userLat: Double, userLon: Double, userLocationName: String, context: Context? = appContext): String {
        val safeUserNameJson = org.json.JSONObject.quote(userLocationName)
        val localCss = getLeafletCss(context)
        val localJs = getLeafletJs(context)

        val cssBlock = if (localCss.isNotBlank()) {
            "<style>\n$localCss\n</style>"
        } else {
            """<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/leaflet@1.9.4/dist/leaflet.css" />"""
        }

        val jsBlock = if (localJs.isNotBlank()) {
            "<script>\n$localJs\n</script>"
        } else {
            """<script src="https://cdn.jsdelivr.net/npm/leaflet@1.9.4/dist/leaflet.js"></script>"""
        }

        return """
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset="utf-8" />
              <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
              $cssBlock
              <style>
                html, body, #qiblaMap {
                  margin: 0;
                  padding: 0;
                  width: 100%;
                  height: 100%;
                  background: #FAF8F5;
                  overflow: hidden;
                }
                .qq-map-tooltip {
                  background: #1B4D3E !important;
                  color: #FFF7D6 !important;
                  font-weight: 700 !important;
                  font-size: 11px !important;
                  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif !important;
                  border: 1.2px solid #C9A84C !important;
                  border-radius: 12px !important;
                  padding: 3px 10px !important;
                  box-shadow: 0 4px 10px rgba(0,0,0,0.3) !important;
                }
                .leaflet-tooltip-top:before { border-top-color: #1B4D3E !important; }
                .leaflet-control-attribution {
                  font-size: 9px !important;
                  background: rgba(255,255,255,0.75) !important;
                  padding: 2px 6px !important;
                  border-radius: 4px;
                }
                @keyframes qDash {
                  from { stroke-dashoffset: 44; }
                  to { stroke-dashoffset: 0; }
                }
                .q-dashed-route {
                  animation: qDash 1.8s linear infinite;
                }
              </style>
            </head>
            <body>
              <div id="qiblaMap"></div>
              $jsBlock
              <script>
                try {
                  const map = L.map('qiblaMap', {
                    zoomControl: false,
                    attributionControl: false,
                    scrollWheelZoom: false,
                    dragging: true,
                    tap: true
                  });

                  const tile = L.tileLayer('https://server.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/{z}/{y}/{x}', {
                    maxZoom: 18,
                    attribution: '&copy; Esri &copy; OpenStreetMap'
                  });
                  tile.on('tileerror', function(e) {
                    if (e && e.tile && e.coords) {
                      e.tile.src = 'https://tile.openstreetmap.org/' + e.coords.z + '/' + e.coords.x + '/' + e.coords.y + '.png';
                    }
                  });
                  tile.addTo(map);

                  const kaabaIcon = L.divIcon({
                    className: 'qq-kaaba-icon',
                    html: '<span style="font-size:24px;line-height:1;display:block;filter:drop-shadow(0 2px 4px rgba(0,0,0,0.45));">🕋</span>',
                    iconSize: [28, 28],
                    iconAnchor: [14, 14]
                  });

                  const userIcon = L.divIcon({
                    className: 'qq-user-icon',
                    html: '<span style="font-size:24px;line-height:1;display:block;filter:drop-shadow(0 2px 4px rgba(0,0,0,0.45));">📍</span>',
                    iconSize: [28, 28],
                    iconAnchor: [14, 24]
                  });

                  L.marker([$KAABA_LAT, $KAABA_LON], { icon: kaabaIcon })
                    .addTo(map)
                    .bindTooltip('Makkah', { permanent: true, direction: 'top', offset: [0, -10], className: 'qq-map-tooltip' });

                  let userMarker = L.marker([$userLat, $userLon], { icon: userIcon })
                    .addTo(map)
                    .bindTooltip($safeUserNameJson, { permanent: true, direction: 'top', offset: [0, -18], className: 'qq-map-tooltip' });

                  function getGreatCirclePoints(lat1, lon1, lat2, lon2, numPoints) {
                    const rLat1 = lat1 * Math.PI / 180;
                    const rLon1 = lon1 * Math.PI / 180;
                    const rLat2 = lat2 * Math.PI / 180;
                    const rLon2 = lon2 * Math.PI / 180;
                    const d = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin((rLat1 - rLat2)/2), 2) + Math.cos(rLat1) * Math.cos(rLat2) * Math.pow(Math.sin((rLon1 - rLon2)/2), 2)));
                    const pts = [];
                    for (let i = 0; i <= numPoints; i++) {
                      const f = i / numPoints;
                      const A = Math.sin((1 - f) * d) / Math.sin(d);
                      const B = Math.sin(f * d) / Math.sin(d);
                      const x = A * Math.cos(rLat1) * Math.cos(rLon1) + B * Math.cos(rLat2) * Math.cos(rLon2);
                      const y = A * Math.cos(rLat1) * Math.sin(rLon1) + B * Math.cos(rLat2) * Math.sin(rLon2);
                      const z = A * Math.sin(rLat1) + B * Math.sin(rLat2);
                      const lat = Math.atan2(z, Math.sqrt(x*x + y*y)) * 180 / Math.PI;
                      const lon = Math.atan2(y, x) * 180 / Math.PI;
                      pts.push([lat, lon]);
                    }
                    return pts;
                  }

                  let points = getGreatCirclePoints($userLat, $userLon, $KAABA_LAT, $KAABA_LON, 48);
                  let route = L.polyline(points, {
                    color: '#2dd4bf',
                    weight: 4,
                    opacity: 0.95,
                    dashArray: '12 10',
                    className: 'q-dashed-route'
                  }).addTo(map);

                  const bounds = L.latLngBounds([[$userLat, $userLon], [$KAABA_LAT, $KAABA_LON]]);
                  map.fitBounds(bounds.pad(0.38));
                  setTimeout(function() { try { map.invalidateSize(); } catch(_) {} }, 150);

                  window.updateUserLocation = function(newLat, newLon, newLabel) {
                    try {
                      if (userMarker) {
                        userMarker.setLatLng([newLat, newLon]);
                        userMarker.setTooltipContent(newLabel);
                      }
                      const newPts = getGreatCirclePoints(newLat, newLon, $KAABA_LAT, $KAABA_LON, 48);
                      if (route) {
                        route.setLatLngs(newPts);
                      }
                      const b = L.latLngBounds([[newLat, newLon], [$KAABA_LAT, $KAABA_LON]]);
                      map.fitBounds(b.pad(0.38));
                    } catch(err) {
                      console.error(err);
                    }
                  };
                } catch(e) {
                  console.error(e);
                }
              </script>
            </body>
            </html>
        """.trimIndent()
    }
}
