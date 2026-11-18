package com.tasbih.digitaltasbih.data

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object QuranAudioPlayer {
    private var mediaPlayer: MediaPlayer? = null
    private var progressJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main)

    var isPlaying by mutableStateOf(false)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var currentPlayingGlobalNum by mutableStateOf<Int?>(null)
        private set

    var currentAyahProgressFraction by mutableFloatStateOf(0f)
        private set

    var currentAudioPositionMs by mutableIntStateOf(0)
        private set

    var currentAudioDurationMs by mutableIntStateOf(0)
        private set

    private var currentAyahList: List<AyahItem> = emptyList()
    private var currentReciter: String = "ar.alafasy"
    private var onNextAyahCallback: ((Int) -> Unit)? = null
    private var isAutoplayEnabled: Boolean = false

    private fun startProgressTracking(mp: MediaPlayer) {
        progressJob?.cancel()
        progressJob = scope.launch {
            while (isPlaying) {
                try {
                    if (mediaPlayer == mp && mp.isPlaying) {
                        val pos = mp.currentPosition
                        val dur = mp.duration
                        currentAudioPositionMs = pos
                        currentAudioDurationMs = dur
                        if (dur > 0) {
                            currentAyahProgressFraction = (pos.toFloat() / dur.toFloat()).coerceIn(0f, 1f)
                        }
                    }
                } catch (_: Exception) {}
                delay(40)
            }
        }
    }

    var isPlayingTranslationPart by mutableStateOf(false)
        private set

    // ── Translation Audio Floating Player State ──
    var isTranslationBarVisible by mutableStateOf(false)
        private set
    var activeTranslationAyah by mutableStateOf<AyahItem?>(null)
        private set
    var activeTranslationSurahNumber by mutableIntStateOf(1)
        private set
    var activeTranslationAyahNumber by mutableIntStateOf(1)
        private set
    var activeTranslationLang by mutableStateOf("hi")
        private set
    var activeTranslationVoiceName by mutableStateOf("")
        private set

    fun playTranslationOnly(
        ayah: AyahItem,
        language: String
    ) {
        val sNum = ayah.surahNumber
        val aNum = ayah.numberInSurah
        val isSameAyah = (activeTranslationSurahNumber == sNum && activeTranslationAyahNumber == aNum && isTranslationBarVisible)

        if (isSameAyah) {
            if (isPlaying) {
                pause()
                return
            } else if (mediaPlayer != null) {
                resume()
                return
            }
        }

        stop()
        activeTranslationAyah = ayah
        activeTranslationSurahNumber = sNum
        activeTranslationAyahNumber = aNum
        currentPlayingGlobalNum = ayah.globalNumber
        activeTranslationLang = language
        isTranslationBarVisible = true
        isPlayingTranslationPart = true

        val isEnglish = (language == "en")
        activeTranslationVoiceName = if (isEnglish) "Ibrahim Walk (Sahih Int.)" else "शमशाद अली खान (Shamshad Ali Khan)"

        val sPad = String.format(java.util.Locale.US, "%03d", sNum)
        val aPad = String.format(java.util.Locale.US, "%03d", aNum)
        val url = if (isEnglish) {
            "https://everyayah.com/data/English/Sahih_Intnl_Ibrahim_Walk_192kbps/$sPad$aPad.mp3"
        } else {
            // Both Hindi and Urdu use Shamshad Ali Khan
            "https://everyayah.com/data/translations/urdu_shamshad_ali_khan_46kbps/$sPad$aPad.mp3"
        }

        isLoading = true
        currentAyahProgressFraction = 0f

        scope.launch {
            try {
                val player = MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                    setDataSource(url)
                    setOnPreparedListener { mp ->
                        QuranAudioPlayer.isLoading = false
                        QuranAudioPlayer.isPlaying = true
                        QuranAudioPlayer.currentAyahProgressFraction = 0f
                        mp.start()
                        startProgressTracking(mp)
                    }
                    setOnCompletionListener {
                        progressJob?.cancel()
                        QuranAudioPlayer.currentAyahProgressFraction = 1f
                        QuranAudioPlayer.isPlaying = false
                        QuranAudioPlayer.isLoading = false
                        QuranAudioPlayer.isPlayingTranslationPart = false
                    }
                    setOnErrorListener { _, _, _ ->
                        progressJob?.cancel()
                        QuranAudioPlayer.currentAyahProgressFraction = 0f
                        QuranAudioPlayer.isPlaying = false
                        QuranAudioPlayer.isLoading = false
                        QuranAudioPlayer.isPlayingTranslationPart = false
                        true
                    }
                    prepareAsync()
                }
                mediaPlayer = player
            } catch (_: Exception) {
                progressJob?.cancel()
                currentAyahProgressFraction = 0f
                isLoading = false
                isPlaying = false
                isPlayingTranslationPart = false
            }
        }
    }

    fun dismissTranslationBar() {
        stop()
        isTranslationBarVisible = false
        activeTranslationAyah = null
        isPlayingTranslationPart = false
    }

    fun playAyah(
        globalNum: Int,
        reciter: String = "ar.alafasy",
        ayahList: List<AyahItem> = emptyList(),
        autoplayNext: Boolean = false,
        onAdvanceNext: ((Int) -> Unit)? = null
    ) {
        currentAyahList = ayahList
        currentReciter = reciter
        isAutoplayEnabled = autoplayNext
        onNextAyahCallback = onAdvanceNext

        if (currentPlayingGlobalNum == globalNum && isPlaying) {
            pause()
            return
        }

        if (currentPlayingGlobalNum == globalNum && mediaPlayer != null && !isPlaying) {
            resume()
            return
        }

        stop()
        isTranslationBarVisible = false
        activeTranslationAyah = null
        currentPlayingGlobalNum = globalNum
        val s = QuranRepository.loadSettings()
        val playTranslationFirst = (s.audioPlayMode == QuranAudioPlayMode.TRANSLATION_ONLY)
        playSingleAyahStream(globalNum, reciter, playTranslation = playTranslationFirst)
    }

    private fun playSingleAyahStream(
        globalNum: Int,
        reciter: String,
        playTranslation: Boolean
    ) {
        isLoading = true
        currentAyahProgressFraction = 0f
        isPlayingTranslationPart = playTranslation
        val s = QuranRepository.loadSettings()

        val ayahItem = currentAyahList.find { it.globalNumber == globalNum }
        val sNum = ayahItem?.surahNumber ?: 1
        val aNum = ayahItem?.numberInSurah ?: 1

        val url = if (playTranslation) {
            val sPad = String.format(java.util.Locale.US, "%03d", sNum)
            val aPad = String.format(java.util.Locale.US, "%03d", aNum)
            if (s.translationVoiceLang == "en") {
                "https://everyayah.com/data/English/Sahih_Intnl_Ibrahim_Walk_192kbps/$sPad$aPad.mp3"
            } else {
                "https://everyayah.com/data/translations/urdu_shamshad_ali_khan_46kbps/$sPad$aPad.mp3"
            }
        } else {
            "https://cdn.islamic.network/quran/audio/128/$reciter/$globalNum.mp3"
        }

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val player = MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                    setDataSource(url)
                    setOnPreparedListener { mp ->
                        QuranAudioPlayer.isLoading = false
                        QuranAudioPlayer.isPlaying = true
                        QuranAudioPlayer.currentAyahProgressFraction = 0f
                        mp.start()
                        startProgressTracking(mp)
                    }
                    setOnCompletionListener {
                        progressJob?.cancel()
                        currentAyahProgressFraction = 1f
                        if (!playTranslation && s.audioPlayMode == QuranAudioPlayMode.ARABIC_AND_TRANSLATION) {
                            playSingleAyahStream(globalNum, reciter, playTranslation = true)
                        } else {
                            QuranAudioPlayer.isPlaying = false
                            QuranAudioPlayer.isLoading = false
                            isPlayingTranslationPart = false
                            handleAyahCompleted(globalNum)
                        }
                    }
                    setOnErrorListener { _, what, extra ->
                        progressJob?.cancel()
                        currentAyahProgressFraction = 0f
                        QuranAudioPlayer.isPlaying = false
                        QuranAudioPlayer.isLoading = false
                        isPlayingTranslationPart = false
                        true
                    }
                    prepareAsync()
                }
                mediaPlayer = player
            } catch (e: Exception) {
                progressJob?.cancel()
                currentAyahProgressFraction = 0f
                QuranAudioPlayer.isLoading = false
                QuranAudioPlayer.isPlaying = false
                isPlayingTranslationPart = false
            }
        }
    }

    private fun handleAyahCompleted(finishedGlobalNum: Int) {
        if (!isAutoplayEnabled || currentAyahList.isEmpty()) {
            currentPlayingGlobalNum = null
            currentAyahProgressFraction = 0f
            return
        }

        val currentIndex = currentAyahList.indexOfFirst { it.globalNumber == finishedGlobalNum }
        if (currentIndex != -1 && currentIndex + 1 < currentAyahList.size) {
            val nextAyah = currentAyahList[currentIndex + 1]
            onNextAyahCallback?.invoke(nextAyah.globalNumber)
            playAyah(
                globalNum = nextAyah.globalNumber,
                reciter = currentReciter,
                ayahList = currentAyahList,
                autoplayNext = isAutoplayEnabled,
                onAdvanceNext = onNextAyahCallback
            )
        } else {
            currentPlayingGlobalNum = null
            currentAyahProgressFraction = 0f
        }
    }

    fun pause() {
        progressJob?.cancel()
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                isPlaying = false
            }
        }
    }

    fun resume() {
        mediaPlayer?.let {
            it.start()
            isPlaying = true
            startProgressTracking(it)
        }
    }

    fun stop() {
        progressJob?.cancel()
        currentAyahProgressFraction = 0f
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        } catch (_: Exception) {}
        mediaPlayer = null
        isPlaying = false
        isLoading = false
        isPlayingTranslationPart = false
        currentPlayingGlobalNum = null
    }
}
