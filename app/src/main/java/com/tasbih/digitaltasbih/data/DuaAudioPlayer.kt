package com.tasbih.digitaltasbih.data

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.json.JSONObject
import java.util.Locale

/**
 * DuaAudioPlayer - 100% Offline, Zero-Latency Authentic Human Voice Audio Player for Duas.
 * Features:
 * - Direct Asset-descriptor playback: 0ms delay, instantaneous response.
 * - Clean cut authentic recitations (Sheikh Hamad Al-Durayhim & Sheikh Mishary Rashid Alafasy).
 * - Automatic and reliable UI reset to Play button upon audio completion.
 * - High quality Arabic TextToSpeech fallback for any unmapped phrases.
 */
object DuaAudioPlayer {
    private const val TAG = "DuaAudioPlayer"

    private var mediaPlayer: MediaPlayer? = null
    private var currentAfd: AssetFileDescriptor? = null
    private var tts: TextToSpeech? = null
    private var isTtsReady = false

    var isPlaying by mutableStateOf(false)
    var isLoading by mutableStateOf(false)
    var currentPlayingDuaId by mutableStateOf<String?>(null)
    var currentReciter by mutableStateOf<String?>("")

    private var audioMap: Map<String, DuaAudioMeta>? = null

    data class DuaAudioMeta(
        val file: String,
        val source: String,
        val reciter: String
    )

    private fun loadAudioMap(context: Context): Map<String, DuaAudioMeta> {
        if (audioMap != null) return audioMap!!
        return try {
            val jsonString = context.assets.open("dua_audio_map.json").bufferedReader().use { it.readText() }
            val json = JSONObject(jsonString)
            val map = mutableMapOf<String, DuaAudioMeta>()
            val keys = json.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                val obj = json.getJSONObject(key)
                map[key] = DuaAudioMeta(
                    file = obj.optString("file", ""),
                    source = obj.optString("source", ""),
                    reciter = obj.optString("reciter", "Sheikh Hamad Al-Durayhim")
                )
            }
            audioMap = map
            map
        } catch (e: Exception) {
            Log.e(TAG, "Error loading dua_audio_map.json", e)
            emptyMap()
        }
    }

    fun getAudioMeta(context: Context, duaId: String): DuaAudioMeta? {
        val map = loadAudioMap(context)
        return map[duaId]
    }

    fun isDuaPlaying(duaId: String): Boolean {
        return isPlaying && currentPlayingDuaId == duaId
    }

    fun isDuaLoading(duaId: String): Boolean {
        return isLoading && currentPlayingDuaId == duaId
    }

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

    fun togglePlay(context: Context, dua: DuaEntry, onComplete: () -> Unit = {}) {
        if (isDuaPlaying(dua.id)) {
            stop()
            return
        }
        playDua(context, dua, onComplete)
    }

    fun playDua(context: Context, dua: DuaEntry, onComplete: () -> Unit = {}) {
        stop()
        initTts(context)

        val meta = getAudioMeta(context, dua.id)
        val file = meta?.file.orEmpty()
        val reciter = meta?.reciter ?: "Sheikh Hamad Al-Durayhim"

        Log.d(TAG, "playDua requested: duaId='${dua.id}', file='$file', reciter='$reciter'")

        currentPlayingDuaId = dua.id
        currentReciter = reciter

        if (file.isNotBlank()) {
            try {
                val afd = context.assets.openFd("dua_audio/$file")
                currentAfd = afd

                val player = MediaPlayer()
                player.setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                player.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                player.prepare()
                player.setOnCompletionListener {
                    Log.d(TAG, "Playback completed for ${dua.id}")
                    // 1. Immediately reset Compose state so UI flips to Play button instantly
                    this@DuaAudioPlayer.isPlaying = false
                    this@DuaAudioPlayer.isLoading = false
                    this@DuaAudioPlayer.currentPlayingDuaId = null
                    this@DuaAudioPlayer.currentReciter = ""

                    // 2. Safely release native MediaPlayer on next Looper frame (prevents native lock deadlock)
                    Handler(Looper.getMainLooper()).post {
                        try {
                            mediaPlayer?.release()
                        } catch (_: Exception) {}
                        mediaPlayer = null
                        try {
                            currentAfd?.close()
                        } catch (_: Exception) {}
                        currentAfd = null
                        onComplete()
                    }
                }
                player.setOnErrorListener { _, what, extra ->
                    Log.e(TAG, "MediaPlayer error: what=$what, extra=$extra")
                    stop()
                    true
                }
                player.start()
                mediaPlayer = player
                this@DuaAudioPlayer.isPlaying = true
                Log.d(TAG, "Asset audio started with 0ms latency for ${dua.id}")
                return
            } catch (e: Exception) {
                Log.w(TAG, "Asset openFd failed for $file: ${e.message}, falling back to TTS")
                try {
                    currentAfd?.close()
                } catch (_: Exception) {}
                currentAfd = null
            }
        }

        // Fallback to TTS recitation if asset not found
        Log.d(TAG, "Playing via TTS for ${dua.id}")
        playTts(dua.ar, onComplete)
    }

    private fun playTts(arabicText: String, onComplete: () -> Unit) {
        if (tts != null && isTtsReady && arabicText.isNotBlank()) {
            isPlaying = true
            tts?.speak(arabicText, TextToSpeech.QUEUE_FLUSH, null, "dua_tts_${System.currentTimeMillis()}")
            val estDurationMs = (arabicText.length * 90L).coerceIn(2000L, 15000L)
            Handler(Looper.getMainLooper()).postDelayed({
                if (isPlaying) {
                    stop()
                    onComplete()
                }
            }, estDurationMs)
        } else {
            stop()
            onComplete()
        }
    }

    fun stop() {
        // Immediately reset state so UI updates without any delay
        isPlaying = false
        isLoading = false
        currentPlayingDuaId = null
        currentReciter = ""

        val playerToRelease = mediaPlayer
        mediaPlayer = null
        val afdToClose = currentAfd
        currentAfd = null

        try {
            tts?.stop()
        } catch (_: Exception) {}

        // Release native resources on next main looper frame to avoid deadlock
        Handler(Looper.getMainLooper()).post {
            try {
                playerToRelease?.let { p ->
                    try {
                        if (p.isPlaying) p.stop()
                    } catch (_: Exception) {}
                    try {
                        p.reset()
                    } catch (_: Exception) {}
                    try {
                        p.release()
                    } catch (_: Exception) {}
                }
            } catch (_: Exception) {}
            try {
                afdToClose?.close()
            } catch (_: Exception) {}
            Log.d(TAG, "Native MediaPlayer released successfully")
        }
    }
}
