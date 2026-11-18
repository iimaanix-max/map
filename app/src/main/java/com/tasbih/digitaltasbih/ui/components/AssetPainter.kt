package com.tasbih.digitaltasbih.ui.components

import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import com.tasbih.digitaltasbih.ui.theme.GoldBorderLight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap

private val assetCache = ConcurrentHashMap<String, ImageBitmap>()

/**
 * Synchronously or in background decodes an asset into assetCache if not already present.
 */
fun preloadAsset(context: android.content.Context, assetPath: String) {
    if (assetPath.isEmpty() || assetCache.containsKey(assetPath)) return
    try {
        context.assets.open(assetPath).use { stream ->
            BitmapFactory.decodeStream(stream)?.asImageBitmap()?.let {
                assetCache[assetPath] = it
            }
        }
    } catch (_: Exception) {}
}

@Composable
fun rememberAssetPainter(assetPath: String): Painter {
    if (assetPath.isEmpty()) {
        return ColorPainter(androidx.compose.ui.graphics.Color.Transparent)
    }

    val cached = assetCache[assetPath]
    if (cached != null) {
        return BitmapPainter(cached)
    }

    val context = LocalContext.current
    var bitmap by remember(assetPath) { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(assetPath) {
        val img = withContext(Dispatchers.IO) {
            try {
                context.assets.open(assetPath).use { stream ->
                    BitmapFactory.decodeStream(stream)?.asImageBitmap()
                }
            } catch (_: Exception) {
                null
            }
        }
        if (img != null) {
            assetCache[assetPath] = img
            bitmap = img
        }
    }

    val bm = bitmap
    return if (bm != null) BitmapPainter(bm) else ColorPainter(androidx.compose.ui.graphics.Color.Transparent)
}
