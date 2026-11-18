package com.tasbih.digitaltasbih.data

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Dedicated lightweight audio player for Word-by-Word Quran pronunciation.
 * Streams genuine Qari pronunciation clips directly from the official Quran.com CDN.
 */
object QuranWordAudioPlayer {

    private const val TAG = "QuranWordAudioPlayer"
    private const val CDN_BASE = "https://audio.qurancdn.com/"

    private var mediaPlayer: MediaPlayer? = null

    var isPlaying by mutableStateOf(false)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var playingWordId by mutableIntStateOf(-1)
        private set

    fun playWord(wordId: Int, audioPath: String?) {
        if (audioPath.isNullOrBlank()) {
            Log.w(TAG, "No audio URL for wordId: $wordId")
            return
        }

        val fullUrl = if (audioPath.startsWith("http")) audioPath else "$CDN_BASE$audioPath"
        Log.d(TAG, "Playing word audio: $fullUrl (wordId=$wordId)")

        stop()
        playingWordId = wordId
        isLoading = true

        try {
            val player = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(fullUrl)
                setOnPreparedListener { mp ->
                    QuranWordAudioPlayer.isLoading = false
                    QuranWordAudioPlayer.isPlaying = true
                    mp.start()
                }
                setOnCompletionListener {
                    QuranWordAudioPlayer.isPlaying = false
                    QuranWordAudioPlayer.isLoading = false
                    QuranWordAudioPlayer.playingWordId = -1
                    stop()
                }
                setOnErrorListener { _, what, extra ->
                    Log.e(TAG, "MediaPlayer error: what=$what extra=$extra for url: $fullUrl")
                    QuranWordAudioPlayer.isPlaying = false
                    QuranWordAudioPlayer.isLoading = false
                    QuranWordAudioPlayer.playingWordId = -1
                    stop()
                    true
                }
                prepareAsync()
            }
            mediaPlayer = player
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize word MediaPlayer: ${e.message}")
            QuranWordAudioPlayer.isLoading = false
            QuranWordAudioPlayer.isPlaying = false
            QuranWordAudioPlayer.playingWordId = -1
        }
    }

    fun stop() {
        try {
            mediaPlayer?.let { mp ->
                if (mp.isPlaying) {
                    mp.stop()
                }
                mp.reset()
                mp.release()
            }
        } catch (_: Exception) {}
        mediaPlayer = null
        isPlaying = false
        isLoading = false
        playingWordId = -1
    }
}
