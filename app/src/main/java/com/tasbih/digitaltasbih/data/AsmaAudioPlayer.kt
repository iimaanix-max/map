package com.tasbih.digitaltasbih.data

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale
import android.util.Log

/**
 * AsmaAudioPlayer - Handles authentic audio pronunciation of the 99 Names of Allah.
 * Features:
 * - Local disk caching (in cacheDir/asma_audio) so subsequent & prefetched plays have 0ms delay.
 * - Background prefetching for today's daily featured name.
 * - Pre-warmed TextToSpeech in Arabic locale as instant fallback.
 */
object AsmaAudioPlayer {
    private const val TAG = "AsmaAudioPlayer"
    private var mediaPlayer: MediaPlayer? = null
    private var tts: TextToSpeech? = null
    private var isTtsReady = false

    var isPlaying by mutableStateOf(false)
    var currentPlayingNum by mutableStateOf<Int?>(null)

    fun initTts(context: Context) {
        if (tts == null) {
            tts = TextToSpeech(context.applicationContext) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    val res = tts?.setLanguage(Locale("ar"))
                    isTtsReady = (res != TextToSpeech.LANG_MISSING_DATA && res != TextToSpeech.LANG_NOT_SUPPORTED)
                }
            }
        }
    }

    private fun getAudioCacheFile(context: Context, itemNumber: Int): File {
        val dir = File(context.cacheDir, "asma_audio").apply { if (!exists()) mkdirs() }
        return File(dir, "asma_$itemNumber.mp3")
    }

    /**
     * Prefetch the audio file in the background so tapping "Play" is instantaneous (0ms delay).
     */
    fun prefetch(context: Context, item: AsmaNameItem) {
        initTts(context)
        val cacheFile = getAudioCacheFile(context, item.number)
        if (cacheFile.exists() && cacheFile.length() > 500) return

        val audioIndex = if (item.number >= 2) item.number - 1 else 1
        val cdnUrl = "https://cdn.jsdelivr.net/gh/soachishti/Asma-ul-Husna@master/audio/$audioIndex.mp3"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL(cdnUrl)
                val conn = url.openConnection() as HttpURLConnection
                conn.connectTimeout = 4000
                conn.readTimeout = 4000
                conn.inputStream.use { input ->
                    cacheFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            } catch (e: Exception) {
                Log.w(TAG, "Prefetch audio failed: ${e.message}")
            }
        }
    }

    fun togglePlay(context: Context, item: AsmaNameItem, onComplete: () -> Unit = {}) {
        if (isPlaying && currentPlayingNum == item.number) {
            stop()
            return
        }
        playName(context, item, onComplete)
    }

    fun playName(context: Context, item: AsmaNameItem, onComplete: () -> Unit = {}) {
        stop()
        initTts(context)

        currentPlayingNum = item.number
        isPlaying = true

        val cacheFile = getAudioCacheFile(context, item.number)
        val audioIndex = if (item.number >= 2) item.number - 1 else 1
        val cdnUrl = "https://cdn.jsdelivr.net/gh/soachishti/Asma-ul-Husna@master/audio/$audioIndex.mp3"

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val player = MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )

                    // If cached locally, play instantly (0ms delay)
                    if (cacheFile.exists() && cacheFile.length() > 500) {
                        setDataSource(cacheFile.absolutePath)
                    } else {
                        // Play stream and simultaneously cache for next time
                        setDataSource(cdnUrl)
                        prefetch(context, item)
                    }

                    setOnPreparedListener { mp ->
                        mp.start()
                    }
                    setOnCompletionListener {
                        AsmaAudioPlayer.isPlaying = false
                        AsmaAudioPlayer.currentPlayingNum = null
                        onComplete()
                    }
                    setOnErrorListener { _, _, _ ->
                        playTtsFallback(item, onComplete)
                        true
                    }
                    prepareAsync()
                }
                mediaPlayer = player
            } catch (e: Exception) {
                Log.w(TAG, "MediaPlayer streaming failed: ${e.message}, falling back to TTS")
                playTtsFallback(item, onComplete)
            }
        }
    }

    private fun playTtsFallback(item: AsmaNameItem, onComplete: () -> Unit) {
        val t = tts
        if (t != null && isTtsReady) {
            t.speak(item.arabic, TextToSpeech.QUEUE_FLUSH, null, "asma_${item.number}")
            CoroutineScope(Dispatchers.Main).launch {
                kotlinx.coroutines.delay(1800)
                AsmaAudioPlayer.isPlaying = false
                AsmaAudioPlayer.currentPlayingNum = null
                onComplete()
            }
        } else {
            AsmaAudioPlayer.isPlaying = false
            AsmaAudioPlayer.currentPlayingNum = null
            onComplete()
        }
    }

    fun stop() {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        } catch (_: Exception) {}
        mediaPlayer = null

        try {
            tts?.stop()
        } catch (_: Exception) {}

        AsmaAudioPlayer.isPlaying = false
        AsmaAudioPlayer.currentPlayingNum = null
    }

    fun shutdown() {
        stop()
        try {
            tts?.shutdown()
        } catch (_: Exception) {}
        tts = null
        isTtsReady = false
    }
}
