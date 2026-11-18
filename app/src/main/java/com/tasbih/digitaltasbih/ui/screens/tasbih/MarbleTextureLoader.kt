package com.tasbih.digitaltasbih.ui.screens.tasbih

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tasbih.digitaltasbih.data.BeadMarbleSkin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest
import java.util.concurrent.ConcurrentHashMap

/**
 * High-performance asynchronous loader & memory/disk cache for CDN marble textures.
 * Used across RealisticBeadsCanvas, Colours & Marbles preview, and MarbleGalleryModal.
 */
object MarbleTextureCache {
    val memoryCache = ConcurrentHashMap<String, ImageBitmap>()

    fun findBundledAssetPath(imageUrl: String): String? {
        val match = Regex("""floral_(\d{2})""").find(imageUrl)
        if (match != null) {
            val num = match.groupValues[1]
            val intVal = num.toIntOrNull() ?: 0
            if (intVal in 1..12) {
                return "marbles/floral_$num.jpg"
            }
        }
        if (imageUrl.startsWith("marbles/") || imageUrl.startsWith("assets/marbles/")) {
            return imageUrl.removePrefix("assets/")
        }
        return null
    }

    suspend fun getOrLoadTexture(context: Context, imageUrl: String): ImageBitmap? {
        if (imageUrl.isBlank()) return null
        memoryCache[imageUrl]?.let { return it }

        return withContext(Dispatchers.IO) {
            try {
                // 1. Check local bundled asset first (instant 0ms load)
                val bundledPath = findBundledAssetPath(imageUrl)
                if (bundledPath != null) {
                    try {
                        context.assets.open(bundledPath).use { stream ->
                            val bmp = BitmapFactory.decodeStream(stream)
                            if (bmp != null) {
                                val imageBitmap = bmp.asImageBitmap()
                                memoryCache[imageUrl] = imageBitmap
                                return@withContext imageBitmap
                            }
                        }
                    } catch (_: Exception) {}
                }

                // 2. Disk & Network cache fallback
                val cacheDir = File(context.cacheDir, "marbles_cache")
                if (!cacheDir.exists()) cacheDir.mkdirs()

                val hash = md5(imageUrl)
                val cachedFile = File(cacheDir, "$hash.img")

                var bmp: Bitmap? = null

                if (cachedFile.exists() && cachedFile.length() > 0) {
                    bmp = BitmapFactory.decodeFile(cachedFile.absolutePath)
                }

                if (bmp == null) {
                    val url = URL(imageUrl)
                    val conn = url.openConnection() as HttpURLConnection
                    conn.connectTimeout = 8000
                    conn.readTimeout = 8000
                    conn.setRequestProperty("User-Agent", "Mozilla/5.0")
                    if (conn.responseCode == 200) {
                        val bytes = conn.inputStream.use { it.readBytes() }
                        bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        if (bmp != null) {
                            try {
                                FileOutputStream(cachedFile).use { it.write(bytes) }
                            } catch (_: Exception) {}
                        }
                    }
                }

                if (bmp != null) {
                    val imageBitmap = bmp.asImageBitmap()
                    memoryCache[imageUrl] = imageBitmap
                    imageBitmap
                } else null
            } catch (_: Exception) {
                null
            }
        }
    }

    /**
     * Asynchronously preloads all bundled marble textures into RAM cache
     * so they render with zero latency when opening the gallery or changing designs.
     */
    fun preloadAll(context: Context) {
        try {
            for (i in 1..12) {
                val numStr = String.format(java.util.Locale.US, "%02d", i)
                val assetPath = "marbles/floral_$numStr.jpg"
                try {
                    context.assets.open(assetPath).use { stream ->
                        val bmp = BitmapFactory.decodeStream(stream)
                        if (bmp != null) {
                            val imgBmp = bmp.asImageBitmap()
                            // Store under asset path and floral ID
                            memoryCache[assetPath] = imgBmp
                            memoryCache["floral_$numStr"] = imgBmp
                            // Also map corresponding known CDN URLs
                            for (skin in com.tasbih.digitaltasbih.data.TasbihRepository.BUILTIN_BEAD_MARBLES) {
                                if (skin.id == "floral_$numStr" && skin.image.isNotEmpty()) {
                                    memoryCache[skin.image] = imgBmp
                                }
                            }
                        }
                    }
                } catch (_: Exception) {}
            }
        } catch (_: Exception) {}
    }

    private fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        val bytes = md.digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}

@Composable
fun rememberMarbleTexture(imageUrl: String): ImageBitmap? {
    if (imageUrl.isBlank()) return null
    val context = LocalContext.current
    var texture by remember(imageUrl) { mutableStateOf(MarbleTextureCache.memoryCache[imageUrl]) }

    LaunchedEffect(imageUrl) {
        if (texture == null) {
            val loaded = MarbleTextureCache.getOrLoadTexture(context, imageUrl)
            if (loaded != null) {
                texture = loaded
            }
        }
    }

    return texture
}

/**
 * 60dp or custom size round marble preview matching index.html .mg-ball
 */
@Composable
fun MarbleBallPreview(
    skin: BeadMarbleSkin,
    size: Dp = 60.dp,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier
) {
    val texture = rememberMarbleTexture(skin.image)

    val baseCol = remember(skin.baseColor) {
        try { Color(android.graphics.Color.parseColor(skin.baseColor)) } catch (_: Exception) { Color(0xFF8A5A34) }
    }
    val hlCol = remember(skin.highlightColor) {
        try { Color(android.graphics.Color.parseColor(skin.highlightColor)) } catch (_: Exception) { Color(0xFFECCFA3) }
    }
    val shCol = remember(skin.shadowColor) {
        try { Color(android.graphics.Color.parseColor(skin.shadowColor)) } catch (_: Exception) { Color(0xFF3A2412) }
    }

    Box(
        modifier = modifier
            .size(size)
            .shadow(
                elevation = if (isSelected) 6.dp else 3.dp,
                shape = CircleShape
            )
            .clip(CircleShape)
            .then(
                if (isSelected) {
                    Modifier.border(2.5.dp, Color(0xFFC9A84C), CircleShape)
                } else {
                    Modifier.border(0.6.dp, Color(0x18000000), CircleShape)
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (skin.image.isNotEmpty() && texture != null) {
            // 1. CDN Image Texture
            Image(
                bitmap = texture,
                contentDescription = skin.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // 2. Glossy 3D lighting overlay (matching index.html)
            Canvas(modifier = Modifier.fillMaxSize()) {
                val r = this.size.minDimension / 2f
                drawCircle(
                    brush = Brush.radialGradient(
                        colorStops = arrayOf(
                            0.00f to Color.White.copy(alpha = 0.40f),
                            0.50f to Color.White.copy(alpha = 0.00f),
                            1.00f to Color.Black.copy(alpha = 0.35f)
                        ),
                        center = Offset(r * 0.65f, r * 0.65f),
                        radius = r * 1.15f
                    ),
                    radius = r,
                    center = Offset(r, r)
                )

                // Specular highlight dot
                drawCircle(
                    color = Color.White.copy(alpha = 0.75f),
                    radius = r * 0.20f,
                    center = Offset(r * 0.66f, r * 0.64f)
                )
            }
        } else {
            // 3D Spherical procedural radial gradient (builtin or fallback)
            Canvas(modifier = Modifier.fillMaxSize()) {
                val r = this.size.minDimension / 2f
                drawCircle(
                    brush = Brush.radialGradient(
                        colorStops = arrayOf(
                            0.00f to hlCol,
                            0.45f to baseCol,
                            1.00f to shCol
                        ),
                        center = Offset(r * 0.65f, r * 0.65f),
                        radius = r * 1.15f
                    ),
                    radius = r,
                    center = Offset(r, r)
                )

                if (skin.family == "wood") {
                    for (g in -1..1) {
                        drawOval(
                            color = Color(0x333A2412),
                            topLeft = Offset(r * 0.15f, r + g * r * 0.35f - r * 0.15f),
                            size = androidx.compose.ui.geometry.Size(r * 1.7f, r * 0.35f),
                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.dp.toPx())
                        )
                    }
                }

                // Specular highlight dot
                drawCircle(
                    color = Color.White.copy(alpha = 0.70f),
                    radius = r * 0.22f,
                    center = Offset(r * 0.68f, r * 0.66f)
                )
            }
        }
    }
}
