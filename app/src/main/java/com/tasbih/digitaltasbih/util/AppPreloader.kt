package com.tasbih.digitaltasbih.util

import android.content.Context
import com.tasbih.digitaltasbih.data.QiblaRepository
import com.tasbih.digitaltasbih.ui.components.preloadAsset
import com.tasbih.digitaltasbih.ui.screens.tasbih.MarbleTextureCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * AppPreloader - Asynchronously warms up heavy UI resources on app launch
 * in a low-priority background thread (Dispatchers.IO) so screens open instantly:
 * 1. Preloads all 12 Floral Marble textures into RAM (0ms "Colours & Marbles" dialog & bead switches).
 * 2. Inlines and warms up Leaflet CSS & JS local assets (0ms Qibla Map initialization).
 * 3. Prewarms active & popular Compass skin webp assets (zero-flicker dial rotation).
 * 4. Prewarms top feature grid webp icons.
 */
object AppPreloader {
    private var isInitialized = false
    private val scope = CoroutineScope(Dispatchers.IO)

    fun init(context: Context) {
        if (isInitialized) return
        isInitialized = true
        val appContext = context.applicationContext

        scope.launch {
            try {
                // 1. Warm up Qibla Leaflet local assets
                QiblaRepository.init(appContext)

                // 2. Preload all floral marble textures into RAM
                MarbleTextureCache.preloadAll(appContext)

                // 3. Asynchronously fetch any new Firebase designs / dhikrs in background
                com.tasbih.digitaltasbih.data.TasbihRepository.syncFirebaseContent()

                // 3. Prewarm selected compass skin assets
                val selectedSkin = QiblaRepository.getSelectedSkin(appContext)
                preloadAsset(appContext, selectedSkin.outerAsset)
                preloadAsset(appContext, selectedSkin.innerAsset)
                preloadAsset(appContext, selectedSkin.needleAsset)
                preloadAsset(appContext, selectedSkin.previewAsset)

                // Prewarm top 4 default skins
                for (skin in QiblaRepository.DEFAULT_SKINS.take(4)) {
                    preloadAsset(appContext, skin.outerAsset)
                    preloadAsset(appContext, skin.innerAsset)
                    preloadAsset(appContext, skin.needleAsset)
                    preloadAsset(appContext, skin.previewAsset)
                }

                // 4. Prewarm main feature icons
                val featureIcons = listOf(
                    "media/tile_jummah_mubarak.webp",
                    "media/cd777c476662f26f.webp",
                    "media/4b0b3782cfc16b4b.webp",
                    "media/2ba4960e82735607.webp",
                    "media/5fbfa4f9cb23d6b2.webp",
                    "media/bd512d13aad9c8fd.webp",
                    "media/a623ce3fad98a23d.webp",
                    "media/59329dcad16a9eac.webp"
                )
                for (icon in featureIcons) {
                    preloadAsset(appContext, icon)
                }
            } catch (_: Exception) {}
        }
    }
}
