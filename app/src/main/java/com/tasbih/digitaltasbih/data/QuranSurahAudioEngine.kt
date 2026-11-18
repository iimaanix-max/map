package com.tasbih.digitaltasbih.data

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.PowerManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object QuranSurahAudioEngine {
    private var mediaPlayer: MediaPlayer? = null
    private var playToken = 0
    private var appContext: Context? = null
    private var audioManager: AudioManager? = null
    private var audioFocusRequest: AudioFocusRequest? = null
    private val engineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var activeJob: Job? = null

    var isPlaying by mutableStateOf(false)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var currentSurahNum by mutableStateOf<Int?>(null)
        private set

    var currentAyahIndexInSurah by mutableIntStateOf(1)
        private set

    var currentGlobalAyahNum by mutableStateOf<Int?>(null)
        private set

    var isContinuous by mutableStateOf(false)
        private set

    var audioPlayMode by mutableStateOf(QuranAudioPlayMode.ARABIC_ONLY)

    var translationVoiceLang by mutableStateOf("ur")

    var isPlayingTranslationPart by mutableStateOf(false)
        private set

    var currentAyahProgressFraction by mutableFloatStateOf(0f)
        private set

    var currentAudioPositionMs by mutableIntStateOf(0)
        private set

    var currentAudioDurationMs by mutableIntStateOf(0)
        private set

    private var progressJob: Job? = null

    // Precomputed starting and ending global ayah number for each surah 1..114
    private val surahStartAyah = IntArray(115)
    private val surahEndAyah = IntArray(115)

    init {
        var running = 0
        QuranData.surahs.forEach { s ->
            surahStartAyah[s.number] = running + 1
            surahEndAyah[s.number] = running + s.numberOfAyahs
            running += s.numberOfAyahs
        }
    }

    fun init(context: Context) {
        appContext = context.applicationContext
        audioManager = context.applicationContext.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
    }

    private val audioFocusChangeListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
        when (focusChange) {
            AudioManager.AUDIOFOCUS_LOSS -> {
                pause()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT,
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                pause()
            }
            AudioManager.AUDIOFOCUS_GAIN -> {
                resume()
            }
        }
    }

    private fun requestAudioFocus(): Boolean {
        val am = audioManager ?: return true
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val req = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                .setOnAudioFocusChangeListener(audioFocusChangeListener)
                .build()
            audioFocusRequest = req
            am.requestAudioFocus(req) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        } else {
            @Suppress("DEPRECATION")
            am.requestAudioFocus(
                audioFocusChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN
            ) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        }
    }

    private fun abandonAudioFocus() {
        val am = audioManager ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioFocusRequest?.let { am.abandonAudioFocusRequest(it) }
            audioFocusRequest = null
        } else {
            @Suppress("DEPRECATION")
            am.abandonAudioFocus(audioFocusChangeListener)
        }
    }

    fun getStartAyah(surahNum: Int): Int = surahStartAyah.getOrElse(surahNum) { 1 }
    fun getEndAyah(surahNum: Int): Int = surahEndAyah.getOrElse(surahNum) { 7 }

    fun toggleContinuous() {
        isContinuous = !isContinuous
    }

    fun setPlayMode(mode: QuranAudioPlayMode, voiceLang: String? = null) {
        audioPlayMode = mode
        if (voiceLang != null) {
            translationVoiceLang = voiceLang
        }
        val settings = QuranRepository.loadSettings()
        QuranRepository.saveSettings(settings.copy(
            audioPlayMode = mode,
            translationVoiceLang = translationVoiceLang
        ))
    }

    fun playSurah(surahNum: Int, startFromAyahIndexInSurah: Int = 1, reciter: String? = null) {
        val sNum = surahNum.coerceIn(1, 114)
        if (currentSurahNum == sNum && mediaPlayer != null) {
            togglePlayPause()
            return
        }

        stop()
        val s = QuranRepository.loadSettings()
        audioPlayMode = s.audioPlayMode
        translationVoiceLang = s.translationVoiceLang
        currentSurahNum = sNum
        val startAyah = startFromAyahIndexInSurah.coerceAtLeast(1)
        currentAyahIndexInSurah = startAyah
        val startG = getStartAyah(sNum) + (startAyah - 1)
        val activeReciter = reciter ?: s.reciterId
        playAyahInEngine(sNum, startG, startAyah, activeReciter, playTranslation = (audioPlayMode == QuranAudioPlayMode.TRANSLATION_ONLY))
    }

    fun playGlobalAyah(
        surahNum: Int,
        globalNum: Int,
        ayahIndexInSurah: Int,
        reciter: String
    ) {
        val s = QuranRepository.loadSettings()
        audioPlayMode = s.audioPlayMode
        translationVoiceLang = s.translationVoiceLang
        playAyahInEngine(surahNum, globalNum, ayahIndexInSurah, reciter, playTranslation = (audioPlayMode == QuranAudioPlayMode.TRANSLATION_ONLY))
    }

    private fun playAyahInEngine(
        surahNum: Int,
        globalNum: Int,
        ayahIndexInSurah: Int,
        reciter: String,
        playTranslation: Boolean
    ) {
        playToken++
        val thisToken = playToken
        currentGlobalAyahNum = globalNum
        currentAyahIndexInSurah = ayahIndexInSurah
        currentAyahProgressFraction = 0f
        currentAudioPositionMs = 0
        currentAudioDurationMs = 0
        isLoading = true
        isPlayingTranslationPart = playTranslation

        val url = if (playTranslation) {
            val sPad = String.format(java.util.Locale.US, "%03d", surahNum)
            val aPad = String.format(java.util.Locale.US, "%03d", ayahIndexInSurah)
            if (translationVoiceLang == "en") {
                "https://everyayah.com/data/English/Sahih_Intnl_Ibrahim_Walk_192kbps/$sPad$aPad.mp3"
            } else {
                "https://everyayah.com/data/translations/urdu_shamshad_ali_khan_46kbps/$sPad$aPad.mp3"
            }
        } else {
            "https://cdn.islamic.network/quran/audio/128/$reciter/$globalNum.mp3"
        }

        requestAudioFocus()
        activeJob?.cancel()
        activeJob = engineScope.launch {
            try {
                mediaPlayer?.release()
                val player = MediaPlayer()
                appContext?.let { ctx ->
                    try {
                        player.setWakeMode(ctx, PowerManager.PARTIAL_WAKE_LOCK)
                    } catch (_: Exception) {}
                }
                player.setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                player.setDataSource(url)
                player.setOnPreparedListener { mp ->
                    if (thisToken == playToken) {
                        isLoading = false
                        isPlaying = true
                        currentAudioDurationMs = mp.duration
                        mp.start()
                        startProgressTracking()
                    } else {
                        mp.release()
                    }
                }
                player.setOnCompletionListener {
                    if (thisToken == playToken) {
                        progressJob?.cancel()
                        currentAyahProgressFraction = 1f
                        if (!playTranslation && audioPlayMode == QuranAudioPlayMode.ARABIC_AND_TRANSLATION) {
                            // Seamlessly play translation part of this same ayah!
                            playAyahInEngine(surahNum, globalNum, ayahIndexInSurah, reciter, playTranslation = true)
                        } else {
                            handleAyahCompleted(surahNum, globalNum, ayahIndexInSurah, reciter)
                        }
                    }
                }
                player.setOnErrorListener { _, _, _ ->
                    if (thisToken == playToken) {
                        progressJob?.cancel()
                        isLoading = false
                        isPlaying = false
                        if (playTranslation) {
                            handleAyahCompleted(surahNum, globalNum, ayahIndexInSurah, reciter)
                        } else if (reciter != "ar.alafasy") {
                            playAyahInEngine(surahNum, globalNum, ayahIndexInSurah, "ar.alafasy", playTranslation = false)
                        }
                    }
                    true
                }
                player.prepareAsync()
                mediaPlayer = player
            } catch (e: Exception) {
                if (thisToken == playToken) {
                    progressJob?.cancel()
                    isLoading = false
                    isPlaying = false
                }
            }
        }
    }

    private fun startProgressTracking() {
        progressJob?.cancel()
        progressJob = engineScope.launch {
            while (isPlaying) {
                try {
                    val mp = mediaPlayer
                    if (mp != null && mp.isPlaying) {
                        val pos = mp.currentPosition
                        val dur = mp.duration
                        currentAudioPositionMs = pos
                        currentAudioDurationMs = dur
                        if (dur > 0) {
                            currentAyahProgressFraction = (pos.toFloat() / dur.toFloat()).coerceIn(0f, 1f)
                        }
                    }
                } catch (_: Exception) {}
                delay(50)
            }
        }
    }

    private fun handleAyahCompleted(
        surahNum: Int,
        finishedGlobalNum: Int,
        finishedAyahIndex: Int,
        reciter: String
    ) {
        val endG = getEndAyah(surahNum)
        if (finishedGlobalNum < endG) {
            // Advance to next ayah within same surah
            playAyahInEngine(
                surahNum,
                finishedGlobalNum + 1,
                finishedAyahIndex + 1,
                reciter,
                playTranslation = (audioPlayMode == QuranAudioPlayMode.TRANSLATION_ONLY)
            )
        } else {
            // Finished surah!
            if (isContinuous && surahNum < 114) {
                val nextSurah = surahNum + 1
                currentSurahNum = nextSurah
                currentAyahIndexInSurah = 1
                val nextStartG = getStartAyah(nextSurah)
                playAyahInEngine(
                    nextSurah,
                    nextStartG,
                    1,
                    reciter,
                    playTranslation = (audioPlayMode == QuranAudioPlayMode.TRANSLATION_ONLY)
                )
            } else {
                stop()
            }
        }
    }

    fun playNextSurah(reciter: String? = null) {
        val current = currentSurahNum ?: 0
        if (current < 114) {
            playSurah(current + 1, reciter = reciter)
        }
    }

    fun togglePlayPause() {
        val player = mediaPlayer ?: return
        if (isPlaying) {
            progressJob?.cancel()
            player.pause()
            isPlaying = false
        } else {
            requestAudioFocus()
            player.start()
            isPlaying = true
            startProgressTracking()
        }
    }

    fun resume() {
        mediaPlayer?.let {
            if (!it.isPlaying) {
                requestAudioFocus()
                it.start()
                isPlaying = true
                startProgressTracking()
            }
        }
    }

    fun pause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                progressJob?.cancel()
                it.pause()
                isPlaying = false
            }
        }
    }

    fun stop() {
        playToken++
        activeJob?.cancel()
        progressJob?.cancel()
        abandonAudioFocus()
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        } catch (_: Exception) {}
        mediaPlayer = null
        isPlaying = false
        isLoading = false
        isPlayingTranslationPart = false
        currentSurahNum = null
        currentGlobalAyahNum = null
        currentAyahIndexInSurah = 1
        currentAyahProgressFraction = 0f
        currentAudioPositionMs = 0
        currentAudioDurationMs = 0
    }
}
