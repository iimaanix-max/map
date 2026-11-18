package com.tasbih.digitaltasbih.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.util.concurrent.ConcurrentHashMap

/**
 * High-performance Offline Quran and Translation Repository powered by official Tanzil.net datasets.
 * Provides instant sub-millisecond retrieval of all 6,236 Ayahs and translations without internet.
 */
object TanzilOfflineRepository {

    // In-memory cache for loaded Surahs: key = "${langCode}_${surahNum}"
    private val translationMemoryCache = ConcurrentHashMap<String, Map<Int, String>>()
    private val uthmaniArabicCache = ConcurrentHashMap<Int, Map<Int, String>>()

    const val CDN_BASE_URL = "https://cdn.jsdelivr.net/gh/abdulquadirqureshi3-star/quran-translations@main/"

    fun getFileNameForLang(code: String): String {
        return when (code) {
            "am" -> "am.sadiq.txt"
            "ar" -> "ar.jalalayn.txt"
            "bn" -> "bn.bengali.txt"
            "tr" -> "tr.diyanet.txt"
            "ru" -> "ru.kuliev.txt"
            "fr" -> "fr.hamidullah.txt"
            "de" -> "de.bubenheim.txt"
            "es" -> "es.garcia.txt"
            "id" -> "id.indonesian.txt"
            "ta" -> "ta.tamil.txt"
            "ml" -> "ml.abdulhameed.txt"
            "it" -> "it.piccardo.txt"
            "fa" -> "fa.ansarian.txt"
            "zh" -> "zh.jian.txt"
            "sw" -> "sw.barwani.txt"
            "so" -> "so.abduh.txt"
            "sq" -> "sq.ahmeti.txt"
            "uz" -> "uz.sodik.txt"
            "ps" -> "ps.abdulwali.txt"
            "ku" -> "ku.asan.txt"
            "pt" -> "pt.elhayek.txt"
            "nl" -> "nl.keyzer.txt"
            "no" -> "no.berg.txt"
            "sv" -> "sv.bernstrom.txt"
            "pl" -> "pl.bielawskiego.txt"
            "ro" -> "ro.grigore.txt"
            "bs" -> "bs.korkut.txt"
            "bg" -> "bg.theophanov.txt"
            "cs" -> "cs.hrbek.txt"
            "ha" -> "ha.gumi.txt"
            "ja" -> "ja.japanese.txt"
            "ko" -> "ko.korean.txt"
            "th" -> "th.thai.txt"
            "tg" -> "tg.ayati.txt"
            "az" -> "az.mammadaliyev.txt"
            "dv" -> "dv.divehi.txt"
            "ber" -> "ber.mensur.txt"
            "sd" -> "sd.amroti.txt"
            "ug" -> "ug.saleh.txt"
            "tt" -> "tt.nugman.txt"
            "ms" -> "ms.basmeih.txt"
            "ur" -> "ur.jalandhry.txt"
            "hi" -> "hi.hindi.txt"
            "en" -> "en.sahih.txt"
            else -> "$code.txt"
        }
    }

    suspend fun downloadTranslation(context: Context, langCode: String): Boolean = withContext(Dispatchers.IO) {
        val destFile = File(getTranslationsDir(context), "$langCode.txt")
        if (destFile.exists() && destFile.length() > 50000) return@withContext true

        val fileName = getFileNameForLang(langCode)
        val urlStr = "$CDN_BASE_URL$fileName"
        val tempFile = File(getTranslationsDir(context), "$langCode.txt.tmp")
        try {
            val url = java.net.URL(urlStr)
            val conn = url.openConnection() as java.net.HttpURLConnection
            conn.connectTimeout = 10000
            conn.readTimeout = 20000
            conn.setRequestProperty("User-Agent", "Mozilla/5.0")
            if (conn.responseCode == 200) {
                conn.inputStream.use { input ->
                    tempFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                if (tempFile.length() > 50000) {
                    if (destFile.exists()) destFile.delete()
                    val success = tempFile.renameTo(destFile)
                    if (success) {
                        translationMemoryCache.clear() // Invalidate cache so newly downloaded text is read
                        return@withContext true
                    }
                } else {
                    tempFile.delete()
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("TanzilOfflineRepo", "Download failed for $langCode: ${e.message}")
            if (tempFile.exists()) tempFile.delete()
        }
        false
    }

    /**
     * Checks if a language translation is bundled in assets or downloaded in internal storage.
     */
    fun isLanguageAvailableOffline(context: Context, langCode: String): Boolean {
        if (langCode == "hi" || langCode == "ur" || langCode == "en") return true
        val internalFile = File(getTranslationsDir(context), "$langCode.txt")
        return internalFile.exists() && internalFile.length() > 50000
    }

    fun getTranslationsDir(context: Context): File {
        val dir = File(context.filesDir, "quran_translations")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    /**
     * Retrieves all verses for a Surah in the specified language (ayahNumber -> translationText).
     */
    suspend fun getSurahTranslation(
        context: Context,
        surahNum: Int,
        langCode: String
    ): Map<Int, String> = withContext(Dispatchers.IO) {
        val cacheKey = "${langCode}_$surahNum"
        translationMemoryCache[cacheKey]?.let { return@withContext it }

        var inputStream: InputStream? = try {
            when (langCode) {
                "hi" -> context.assets.open("tanzil/hi.hindi.txt")
                "ur" -> context.assets.open("tanzil/ur.jalandhry.txt")
                "en" -> context.assets.open("tanzil/en.sahih.txt")
                else -> {
                    val downloaded = File(getTranslationsDir(context), "$langCode.txt")
                    if (downloaded.exists() && downloaded.length() > 1000) {
                        downloaded.inputStream()
                    } else {
                        // Attempt on-demand download if network available
                        downloadTranslation(context, langCode)
                        val fresh = File(getTranslationsDir(context), "$langCode.txt")
                        if (fresh.exists() && fresh.length() > 1000) fresh.inputStream() else null
                    }
                }
            }
        } catch (_: Exception) {
            null
        }

        // If still null, try fallback to English bundled translation
        if (inputStream == null && langCode != "en") {
            try {
                inputStream = context.assets.open("tanzil/en.sahih.txt")
            } catch (_: Exception) {}
        }

        if (inputStream == null) return@withContext emptyMap()

        val surahMap = mutableMapOf<Int, String>()
        try {
            BufferedReader(InputStreamReader(inputStream, "UTF-8")).use { reader ->
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    val l = line?.trim() ?: continue
                    if (l.isEmpty() || l.startsWith("#") || !l.contains("|")) continue

                    val parts = l.split("|", limit = 3)
                    if (parts.size >= 3) {
                        val sNum = parts[0].toIntOrNull() ?: continue
                        if (sNum == surahNum) {
                            val aNum = parts[1].toIntOrNull() ?: continue
                            surahMap[aNum] = parts[2].trim()
                        } else if (sNum > surahNum && surahMap.isNotEmpty()) {
                            // Sorted order in Tanzil files allows early break
                            break
                        }
                    }
                }
            }
        } catch (e: Exception) {
            android.util.Log.w("TanzilOfflineRepo", "Error reading translation: ${e.message}")
        }

        if (surahMap.isNotEmpty()) {
            translationMemoryCache[cacheKey] = surahMap
        }
        surahMap
    }

    /**
     * Retrieves authentic Tanzil Uthmani Arabic script for a Surah (ayahNumber -> arabicText).
     */
    suspend fun getSurahArabicUthmani(
        context: Context,
        surahNum: Int
    ): Map<Int, String> = withContext(Dispatchers.IO) {
        uthmaniArabicCache[surahNum]?.let { return@withContext it }

        val surahMap = mutableMapOf<Int, String>()
        try {
            context.assets.open("tanzil/quran-uthmani.txt").use { stream ->
                BufferedReader(InputStreamReader(stream, "UTF-8")).use { reader ->
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        val l = line?.trim() ?: continue
                        if (l.isEmpty() || l.startsWith("#") || !l.contains("|")) continue

                        val parts = l.split("|", limit = 3)
                        if (parts.size >= 3) {
                            val sNum = parts[0].toIntOrNull() ?: continue
                            if (sNum == surahNum) {
                                val aNum = parts[1].toIntOrNull() ?: continue
                                surahMap[aNum] = parts[2].trim()
                            } else if (sNum > surahNum && surahMap.isNotEmpty()) {
                                break
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            android.util.Log.w("TanzilOfflineRepo", "Error reading uthmani text: ${e.message}")
        }

        if (surahMap.isNotEmpty()) {
            uthmaniArabicCache[surahNum] = surahMap
        }
        surahMap
    }
}

