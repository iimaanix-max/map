package com.tasbih.digitaltasbih.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ConcurrentHashMap

object QuranRepository {
    private const val BASE_URL = "https://quran-proxy.digitaltasbih.workers.dev"
    private const val PREFS_NAME = "quran_prefs_v1"

    // In-memory cache for speed
    private val surahCache = ConcurrentHashMap<String, List<AyahItem>>()
    private val juzCache = ConcurrentHashMap<String, List<AyahItem>>()
    private val mushafCache = ConcurrentHashMap<String, MushafPageData>()
    private val tafsirCache = ConcurrentHashMap<String, String>()
    private val wordsCache = ConcurrentHashMap<String, Map<Int, List<AyahWordItem>>>()

    private var prefs: SharedPreferences? = null
    private var appContext: Context? = null

    fun init(context: Context) {
        if (prefs == null) {
            prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }
        appContext = context.applicationContext
    }

    private fun getHttpConnection(endpoint: String): HttpURLConnection {
        val url = URL(if (endpoint.startsWith("http")) endpoint else "$BASE_URL$endpoint")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.connectTimeout = 12000
        conn.readTimeout = 15000
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Android; Mobile; rv:109.0) Gecko/109.0")
        conn.setRequestProperty("Accept", "application/json")
        return conn
    }

    private fun httpGet(endpoint: String): String {
        val conn = getHttpConnection(endpoint)
        try {
            val code = conn.responseCode
            if (code in 200..299) {
                BufferedReader(InputStreamReader(conn.inputStream, "UTF-8")).use { reader ->
                    return reader.readText()
                }
            } else {
                throw Exception("HTTP $code from $endpoint")
            }
        } finally {
            conn.disconnect()
        }
    }

    // --- FETCH SURAH AYAHS ---
    suspend fun fetchSurahAyahs(surahNum: Int, translationId: String, langCode: String = ""): List<AyahItem> = withContext(Dispatchers.IO) {
        val settings = loadSettings()
        val lang = if (langCode.isNotBlank()) langCode else settings.translationLanguage
        val cacheKey = "surah_${surahNum}_${lang}_$translationId"
        surahCache[cacheKey]?.let { return@withContext it }

        val ctx = appContext
        // Pre-fetch authentic Tanzil translation for selected language (supports all 44 languages)
        var tanzilMap: Map<Int, String> = emptyMap()
        if (ctx != null) {
            try {
                tanzilMap = TanzilOfflineRepository.getSurahTranslation(ctx, surahNum, lang)
            } catch (_: Exception) {}
        }

        // Check local disk cache
        val diskCached = prefs?.getString(cacheKey, null)
        if (diskCached != null) {
            try {
                var parsed = parseAyahsJson(diskCached, surahNum)
                if (parsed.isNotEmpty()) {
                    if (tanzilMap.isNotEmpty()) {
                        parsed = parsed.map { ayah ->
                            val t = tanzilMap[ayah.numberInSurah]
                            if (!t.isNullOrBlank()) ayah.copy(translation = t) else ayah
                        }
                    }
                    surahCache[cacheKey] = parsed
                    return@withContext parsed
                }
            } catch (_: Exception) {}
        }

        try {
            val jsonStr = httpGet("/v1/surah/$surahNum?translation=$translationId")
            var items = parseAyahsJson(jsonStr, surahNum)
            if (items.isNotEmpty()) {
                if (tanzilMap.isNotEmpty()) {
                    items = items.map { ayah ->
                        val t = tanzilMap[ayah.numberInSurah]
                        if (!t.isNullOrBlank()) ayah.copy(translation = t) else ayah
                    }
                }
                surahCache[cacheKey] = items
                prefs?.edit()?.putString(cacheKey, jsonStr)?.apply()
            }
            items
        } catch (e: Exception) {
            // Offline Tanzil Fallback: Provides instant full surah & translation even without network
            if (ctx != null) {
                try {
                    val uthmaniMap = TanzilOfflineRepository.getSurahArabicUthmani(ctx, surahNum)
                    val transMap = if (tanzilMap.isNotEmpty()) tanzilMap else TanzilOfflineRepository.getSurahTranslation(ctx, surahNum, lang)
                    if (uthmaniMap.isNotEmpty()) {
                        var runningGlobal = 0
                        for (sIdx in 1 until surahNum) {
                            runningGlobal += QuranData.surahs.getOrNull(sIdx - 1)?.numberOfAyahs ?: 0
                        }
                        val offlineAyahs = uthmaniMap.map { (ayahNum, arText) ->
                            val gNum = runningGlobal + ayahNum
                            AyahItem(
                                globalNumber = gNum,
                                numberInSurah = ayahNum,
                                surahNumber = surahNum,
                                arabicText = arText,
                                transliteration = "",
                                translation = transMap[ayahNum] ?: "",
                                isFavorite = isAyahFavorite(gNum)
                            )
                        }.sortedBy { it.numberInSurah }
                        surahCache[cacheKey] = offlineAyahs
                        return@withContext offlineAyahs
                    }
                } catch (_: Exception) {}
            }
            emptyList()
        }
    }

    // --- FETCH SURAH WORD-BY-WORD DATA ---
    suspend fun fetchSurahWords(surahNum: Int, langCode: String = "hi"): Map<Int, List<AyahWordItem>> = withContext(Dispatchers.IO) {
        val cacheKey = "surah_words_${surahNum}_$langCode"
        wordsCache[cacheKey]?.let { return@withContext it }

        val diskCached = prefs?.getString(cacheKey, null)
        if (diskCached != null) {
            try {
                val parsed = parseWordsJson(diskCached)
                if (parsed.isNotEmpty()) {
                    wordsCache[cacheKey] = parsed
                    return@withContext parsed
                }
            } catch (_: Exception) {}
        }

        try {
            val url = "https://api.quran.com/api/v4/verses/by_chapter/$surahNum?words=true&language=$langCode&word_fields=audio_url,translation,transliteration,text_uthmani&per_page=300"
            val jsonStr = httpGet(url)
            val parsed = parseWordsJson(jsonStr)
            if (parsed.isNotEmpty()) {
                wordsCache[cacheKey] = parsed
                prefs?.edit()?.putString(cacheKey, jsonStr)?.apply()
            }
            parsed
        } catch (e: Exception) {
            android.util.Log.e("QuranRepository", "Failed to fetch surah words: ${e.message}")
            emptyMap()
        }
    }

    private fun parseWordsJson(jsonStr: String): Map<Int, List<AyahWordItem>> {
        val root = JSONObject(jsonStr)
        val versesArray = root.optJSONArray("verses") ?: return emptyMap()
        val result = mutableMapOf<Int, MutableList<AyahWordItem>>()

        for (vIdx in 0 until versesArray.length()) {
            val vObj = versesArray.getJSONObject(vIdx)
            val verseNumber = vObj.optInt("verse_number", vIdx + 1)
            val wordsArray = vObj.optJSONArray("words") ?: continue
            val wordList = mutableListOf<AyahWordItem>()

            for (wIdx in 0 until wordsArray.length()) {
                val wObj = wordsArray.getJSONObject(wIdx)
                val id = wObj.optInt("id", 0)
                val pos = wObj.optInt("position", wIdx + 1)
                val charType = wObj.optString("char_type_name", "word")
                val text = wObj.optString("text_uthmani").ifBlank { wObj.optString("text", "") }
                val audioUrl = wObj.optString("audio_url", "").takeIf { it.isNotBlank() }
                val transObj = wObj.optJSONObject("translation")
                val translitObj = wObj.optJSONObject("transliteration")

                val translation = transObj?.optString("text", "") ?: ""
                val transliteration = translitObj?.optString("text", "") ?: ""

                wordList.add(
                    AyahWordItem(
                        id = id,
                        position = pos,
                        text = text,
                        audioUrl = audioUrl,
                        translation = translation,
                        transliteration = transliteration,
                        charTypeName = charType
                    )
                )
            }
            result[verseNumber] = wordList
        }
        return result
    }

    fun attachWordsToAyahs(ayahs: List<AyahItem>, wordsMap: Map<Int, List<AyahWordItem>>): List<AyahItem> {
        if (wordsMap.isEmpty()) return ayahs
        return ayahs.map { ayah ->
            val w = wordsMap[ayah.numberInSurah]
            if (!w.isNullOrEmpty()) {
                ayah.copy(words = w)
            } else {
                ayah
            }
        }
    }

    // --- FETCH JUZ AYAHS ---
    suspend fun fetchJuzAyahs(juzNum: Int, translationId: String, langCode: String = ""): List<AyahItem> = withContext(Dispatchers.IO) {
        val settings = loadSettings()
        val lang = if (langCode.isNotBlank()) langCode else settings.translationLanguage
        val cacheKey = "juz_${juzNum}_${lang}_$translationId"
        juzCache[cacheKey]?.let { return@withContext it }

        val ctx = appContext
        val surahTransMaps = mutableMapOf<Int, Map<Int, String>>()

        suspend fun injectTanzilTranslations(list: List<AyahItem>): List<AyahItem> {
            if (ctx == null) return list
            val uniqueSurahs = list.map { it.surahNumber }.distinct()
            for (s in uniqueSurahs) {
                if (!surahTransMaps.containsKey(s)) {
                    try {
                        surahTransMaps[s] = TanzilOfflineRepository.getSurahTranslation(ctx, s, lang)
                    } catch (_: Exception) {}
                }
            }
            return list.map { ayah ->
                val sMap = surahTransMaps[ayah.surahNumber] ?: emptyMap()
                val t = sMap[ayah.numberInSurah]
                if (!t.isNullOrBlank()) ayah.copy(translation = t) else ayah
            }
        }

        val diskCached = prefs?.getString(cacheKey, null)
        if (diskCached != null) {
            try {
                var parsed = parseAyahsJson(diskCached, -1)
                if (parsed.isNotEmpty()) {
                    parsed = injectTanzilTranslations(parsed)
                    juzCache[cacheKey] = parsed
                    return@withContext parsed
                }
            } catch (_: Exception) {}
        }

        try {
            val jsonStr = httpGet("/v1/juz/$juzNum?translation=$translationId")
            var items = parseAyahsJson(jsonStr, -1)
            if (items.isNotEmpty()) {
                items = injectTanzilTranslations(items)
                juzCache[cacheKey] = items
                prefs?.edit()?.putString(cacheKey, jsonStr)?.apply()
            }
            items
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun parseAyahsJson(jsonStr: String, defaultSurahNum: Int): List<AyahItem> {
        val root = JSONObject(jsonStr)
        val arabicObj = root.optJSONObject("arabic") ?: return emptyList()
        val arabicAyahs = arabicObj.optJSONArray("ayahs") ?: JSONArray()

        val translitObj = root.optJSONObject("translit")
        val translitAyahs = translitObj?.optJSONArray("ayahs")

        val transObj = root.optJSONObject("translation")
        val transAyahs = transObj?.optJSONArray("ayahs")

        val result = mutableListOf<AyahItem>()
        for (i in 0 until arabicAyahs.length()) {
            val a = arabicAyahs.getJSONObject(i)
            val globalNum = a.optInt("number", i + 1)
            val numInSurah = a.optInt("numberInSurah", i + 1)
            val sNum = a.optJSONObject("surah")?.optInt("number", defaultSurahNum) ?: defaultSurahNum
            val arText = a.optString("text", "")

            val tlText = if (translitAyahs != null && i < translitAyahs.length()) {
                translitAyahs.getJSONObject(i).optString("text", "")
            } else ""

            val trText = if (transAyahs != null && i < transAyahs.length()) {
                cleanTranslationText(transAyahs.getJSONObject(i).optString("text", ""))
            } else ""

            val isFav = isAyahFavorite(globalNum)
            result.add(
                AyahItem(
                    globalNumber = globalNum,
                    numberInSurah = numInSurah,
                    surahNumber = sNum,
                    arabicText = arText,
                    transliteration = tlText,
                    translation = trText,
                    isFavorite = isFav
                )
            )
        }
        return result
    }

    private fun cleanTranslationText(raw: String): String {
        // Strip out HTML tags like <sup foot_note=... >...</sup>
        return raw.replace(Regex("<sup[^>]*>.*?</sup>"), "")
            .replace(Regex("<[^>]*>"), "")
            .trim()
    }

    // --- FETCH MUSHAF PAGE ---
    suspend fun fetchMushafPage(pageNumber: Int, langCode: String = "hi"): MushafPageData? = withContext(Dispatchers.IO) {
        val cacheKey = "mushaf_page_${pageNumber}_$langCode"
        val cachedData = mushafCache[cacheKey]
        if (cachedData != null) return@withContext cachedData

        val diskCached = prefs?.getString(cacheKey, null)
        if (diskCached != null) {
            try {
                val parsed = parseMushafPageJson(diskCached, pageNumber, langCode)
                if (parsed != null) {
                    mushafCache[cacheKey] = parsed
                    return@withContext parsed
                }
            } catch (_: Exception) {}
        }

        // 1. Primary: Official Quran.com v4 API by page (authentic 15-line layout + word audio + localized meaning)
        try {
            val url = "https://api.quran.com/api/v4/verses/by_page/$pageNumber?words=true&language=$langCode&word_fields=audio_url,translation,transliteration,text_uthmani,line_number,char_type_name&per_page=50"
            val jsonStr = httpGet(url)
            val parsed = parseMushafPageJson(jsonStr, pageNumber, langCode)
            if (parsed != null) {
                mushafCache[cacheKey] = parsed
                prefs?.edit()?.putString(cacheKey, jsonStr)?.apply()
                return@withContext parsed
            }
        } catch (e: Exception) {
            android.util.Log.e("QuranRepository", "Quran.com mushaf page fetch failed: ${e.message}")
        }

        // 2. Secondary: Fallback to proxy
        try {
            val jsonStr = httpGet("/v1/mushaf-page/$pageNumber")
            val parsed = parseMushafPageJson(jsonStr, pageNumber, langCode)
            if (parsed != null) {
                mushafCache[cacheKey] = parsed
                return@withContext parsed
            }
        } catch (e: Exception) {
            android.util.Log.e("QuranRepository", "Fallback mushaf page failed: ${e.message}")
        }

        // 3. Robust Offline Fallback: Check if page exists in disk cache (any language) or assets seed
        val ctx = appContext
        if (ctx != null) {
            try {
                var fallbackJson: String? = null
                val allPrefs = prefs?.all ?: emptyMap()
                for ((k, v) in allPrefs) {
                    if (k.startsWith("mushaf_page_${pageNumber}_") && v is String && v.isNotBlank()) {
                        fallbackJson = v
                        break
                    }
                }
                if (fallbackJson == null && pageNumber == 1) {
                    try {
                        ctx.assets.open("mushaf_page_1_seed.json").use { s ->
                            fallbackJson = s.bufferedReader(Charsets.UTF_8).readText()
                        }
                    } catch (_: Exception) {}
                }

                if (fallbackJson != null) {
                    val parsed = parseMushafPageJson(fallbackJson!!, pageNumber, langCode)
                    if (parsed != null) {
                        mushafCache[cacheKey] = parsed
                        return@withContext parsed
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e("QuranRepository", "Offline fallback page failed: ${e.message}")
            }
        }

        null
    }

    private suspend fun parseMushafPageJson(jsonStr: String, pageNumber: Int, langCode: String = "hi"): MushafPageData? {
        val root = JSONObject(jsonStr)
        val versesArray = root.optJSONArray("verses") ?: JSONArray()
        if (versesArray.length() == 0) return null

        val linesMap = linkedMapOf<Int, MutableList<MushafWordItem>>()
        val translationList = mutableListOf<AyahItem>()

        var firstSurahNum = 1
        var firstJuzNum = 1
        var firstHizbNum = 1
        var firstRukuNum = 1

        val ctx = appContext
        val offlineSurahTranslations = mutableMapOf<Int, Map<Int, String>>()

        for (vIdx in 0 until versesArray.length()) {
            val vObj = versesArray.getJSONObject(vIdx)
            val verseKey = vObj.optString("verse_key", "1:1") // e.g. "1:1"
            val parts = verseKey.split(":")
            val sNum = parts.getOrNull(0)?.toIntOrNull() ?: 1
            val aNum = parts.getOrNull(1)?.toIntOrNull() ?: (vIdx + 1)
            val globalId = vObj.optInt("id", vIdx + 1)

            if (vIdx == 0) {
                firstSurahNum = sNum
                firstJuzNum = vObj.optInt("juz_number", 1)
                firstHizbNum = vObj.optInt("hizb_number", 1)
                firstRukuNum = vObj.optInt("ruku_number", 1)
            }

            if (ctx != null && !offlineSurahTranslations.containsKey(sNum)) {
                try {
                    val map = TanzilOfflineRepository.getSurahTranslation(ctx, sNum, langCode)
                    if (map.isNotEmpty()) {
                        offlineSurahTranslations[sNum] = map
                    }
                } catch (_: Exception) {}
            }

            val wordsArray = vObj.optJSONArray("words") ?: JSONArray()
            val wordsTrans = StringBuilder()

            for (wIdx in 0 until wordsArray.length()) {
                val wObj = wordsArray.getJSONObject(wIdx)
                val lineNum = wObj.optInt("line_number", 1)
                val text = wObj.optString("text_uthmani").ifBlank { wObj.optString("text", "") }
                val charType = wObj.optString("char_type_name", "word")
                val wordId = wObj.optInt("id", 0)
                val audioUrl = wObj.optString("audio_url", "").takeIf { it.isNotBlank() }
                val transText = wObj.optJSONObject("translation")?.optString("text", "") ?: ""
                val translitText = wObj.optJSONObject("transliteration")?.optString("text", "") ?: ""

                if (transText.isNotBlank()) {
                    wordsTrans.append(transText).append(" ")
                }

                val list = linesMap.getOrPut(lineNum) { mutableListOf() }
                list.add(
                    MushafWordItem(
                        id = wordId,
                        text = text,
                        lineNumber = lineNum,
                        charTypeName = charType,
                        verseNumber = aNum,
                        surahNumber = sNum,
                        audioUrl = audioUrl,
                        translation = transText,
                        transliteration = translitText
                    )
                )
            }

            val offlineAyahTrans = offlineSurahTranslations[sNum]?.get(aNum)
            val finalTrans = if (!offlineAyahTrans.isNullOrBlank()) {
                offlineAyahTrans
            } else {
                wordsTrans.toString().trim()
            }

            translationList.add(
                AyahItem(
                    globalNumber = globalId,
                    numberInSurah = aNum,
                    surahNumber = sNum,
                    arabicText = vObj.optString("text_uthmani", ""),
                    translation = finalTrans
                )
            )
        }

        val lineItems = linesMap.keys.sorted().map { lineNum ->
            MushafLineItem(lineNum, linesMap[lineNum] ?: emptyList())
        }

        val surahMeta = QuranData.surahs.find { it.number == firstSurahNum }
        return MushafPageData(
            pageNumber = pageNumber,
            juzNumber = firstJuzNum,
            hizbNumber = firstHizbNum,
            rukuNumber = firstRukuNum,
            surahNumber = firstSurahNum,
            surahArabicName = surahMeta?.arabicName ?: "الفاتحة",
            surahEnglishName = surahMeta?.englishName ?: "Al-Fatihah",
            surahType = surahMeta?.revelationType ?: "Meccan",
            totalAyahs = surahMeta?.numberOfAyahs ?: 7,
            lines = lineItems,
            translations = translationList
        )
    }

    // --- FETCH TAFSIR ---
    suspend fun fetchTafsir(surahNum: Int, ayahNum: Int, tafsirId: String = "926"): String = withContext(Dispatchers.IO) {
        val key = "${surahNum}_${ayahNum}_$tafsirId"
        tafsirCache[key]?.let { return@withContext it }

        try {
            val qs = if (tafsirId.isNotBlank()) "?tafsirId=$tafsirId" else ""
            val jsonStr = httpGet("/v1/tafsir/$surahNum/$ayahNum$qs")
            val root = JSONObject(jsonStr)
            val tafsirObj = root.optJSONObject("tafsir")
            val tafsirsArr = root.optJSONArray("tafsirs")
            val text = tafsirObj?.optString("text", "") 
                ?: (if (tafsirsArr != null && tafsirsArr.length() > 0) tafsirsArr.getJSONObject(0).optString("text", "") else "")
            val clean = cleanTranslationText(text)
            if (clean.isNotBlank()) {
                tafsirCache[key] = clean
                return@withContext clean
            }
        } catch (_: Exception) {}

        "इस आयत की तफ़सीर लोड करने के लिए इंटरनेट कनेक्शन की आवश्यकता है।"
    }

    // --- FAVORITES MANAGEMENT ---
    fun isAyahFavorite(globalNum: Int): Boolean {
        val set = prefs?.getStringSet("fav_ayahs_v1", emptySet()) ?: emptySet()
        return set.contains(globalNum.toString())
    }

    fun toggleAyahFavorite(globalNum: Int): Boolean {
        val set = prefs?.getStringSet("fav_ayahs_v1", emptySet())?.toMutableSet() ?: mutableSetOf()
        val numStr = globalNum.toString()
        val nowFav = if (set.contains(numStr)) {
            set.remove(numStr)
            false
        } else {
            set.add(numStr)
            true
        }
        prefs?.edit()?.putStringSet("fav_ayahs_v1", set)?.apply()
        return nowFav
    }

    fun getFavoriteAyahs(): Set<Int> {
        val set = prefs?.getStringSet("fav_ayahs_v1", emptySet()) ?: emptySet()
        return set.mapNotNull { it.toIntOrNull() }.toSet()
    }

    fun isSurahFavorite(surahNum: Int): Boolean {
        val set = prefs?.getStringSet("fav_surahs_v1", emptySet()) ?: emptySet()
        return set.contains(surahNum.toString())
    }

    fun toggleSurahFavorite(surahNum: Int): Boolean {
        val set = prefs?.getStringSet("fav_surahs_v1", emptySet())?.toMutableSet() ?: mutableSetOf()
        val numStr = surahNum.toString()
        val nowFav = if (set.contains(numStr)) {
            set.remove(numStr)
            false
        } else {
            set.add(numStr)
            true
        }
        prefs?.edit()?.putStringSet("fav_surahs_v1", set)?.apply()
        return nowFav
    }

    // --- LAST READ & RECENTS ---
    fun getLastRead(): QuranRecentItem {
        val type = prefs?.getString("last_read_type", "surah") ?: "surah"
        val num = prefs?.getInt("last_read_num", 1) ?: 1
        val title = prefs?.getString("last_read_title", "अल-फ़ातिहा • الفاتحة") ?: "अल-फ़ातिहा • الفاتحة"
        val date = prefs?.getString("last_read_date", "15/10/2026") ?: "15/10/2026"
        return QuranRecentItem(type, num, title, date)
    }

    fun setLastRead(type: String, num: Int, title: String) {
        val date = "15/10/2026"
        prefs?.edit()
            ?.putString("last_read_type", type)
            ?.putInt("last_read_num", num)
            ?.putString("last_read_title", title)
            ?.putString("last_read_date", date)
            ?.apply()

        // Also add to recents list
        addRecent(type, num, title, date)
    }

    private fun addRecent(type: String, num: Int, title: String, date: String) {
        val key = "quran_recents_json"
        val current = getRecents().toMutableList()
        current.removeAll { it.type == type && it.number == num }
        current.add(0, QuranRecentItem(type, num, title, date))
        val limited = current.take(10)

        val arr = JSONArray()
        for (item in limited) {
            val obj = JSONObject()
            obj.put("type", item.type)
            obj.put("number", item.number)
            obj.put("title", item.title)
            obj.put("date", item.date)
            arr.put(obj)
        }
        prefs?.edit()?.putString(key, arr.toString())?.apply()
    }

    fun getRecents(): List<QuranRecentItem> {
        val raw = prefs?.getString("quran_recents_json", null)
        if (raw != null) {
            try {
                val arr = JSONArray(raw)
                val list = mutableListOf<QuranRecentItem>()
                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    list.add(
                        QuranRecentItem(
                            type = obj.optString("type", "surah"),
                            number = obj.optInt("number", 1),
                            title = obj.optString("title", ""),
                            date = obj.optString("date", "")
                        )
                    )
                }
                return list
            } catch (_: Exception) {}
        }
        // Default sample recents matching user image
        return listOf(
            QuranRecentItem("surah", 1, "अल-फ़ातिहा • الفاتحة", "15/10/2026"),
            QuranRecentItem("surah", 5, "अल-माइदा • المائدة", "16/9/2026"),
            QuranRecentItem("surah", 72, "अल-जिन्न • الجن", "16/9/2026")
        )
    }

    // --- SETTINGS ---
    fun loadSettings(): QuranSettingsState {
        val p = prefs ?: return QuranSettingsState()
        val modeStr = p.getString("setting_mode", QuranReadingMode.LIST.name) ?: QuranReadingMode.LIST.name
        val mode = try { QuranReadingMode.valueOf(modeStr) } catch (_: Exception) { QuranReadingMode.LIST }
        val currentLang = p.getString("setting_lang", "hi") ?: "hi"
        val curReciterId = p.getString("setting_reciter_id", "ar.alafasy") ?: "ar.alafasy"
        val rawReciterName = p.getString("setting_reciter_name", "Alafasy") ?: "Alafasy"
        val reciterItem = QuranData.reciters.find { it.identifier == curReciterId }
        val resolvedReciterName = reciterItem?.getLocalizedName(currentLang) ?: rawReciterName
        val audioModeStr = p.getString("setting_audio_mode", QuranAudioPlayMode.ARABIC_ONLY.name) ?: QuranAudioPlayMode.ARABIC_ONLY.name
        val audioMode = try { QuranAudioPlayMode.valueOf(audioModeStr) } catch (_: Exception) { QuranAudioPlayMode.ARABIC_ONLY }
        val transVoiceLang = p.getString("setting_trans_voice_lang", if (currentLang == "en") "en" else "ur") ?: if (currentLang == "en") "en" else "ur"

        return QuranSettingsState(
            readingMode = mode,
            translationLanguage = currentLang,
            translationId = p.getString("setting_trans_id", "122") ?: "122",
            translatorName = p.getString("setting_trans_name", "Maulana Azizul Haque al-Umari") ?: "Maulana Azizul Haque al-Umari",
            showTranslation = p.getBoolean("setting_show_trans", true),
            showTransliteration = p.getBoolean("setting_show_tl", true),
            reciterId = curReciterId,
            reciterName = resolvedReciterName,
            autoplayNext = p.getBoolean("setting_autoplay", false),
            autoScroll = p.getBoolean("setting_autoscroll", true),
            tafsirId = p.getString("setting_tafsir_id", "926") ?: "926",
            tafsirName = p.getString("setting_tafsir_name", "Arabic Jalalayn Tafseer") ?: "Arabic Jalalayn Tafseer",
            repeatMode = p.getString("setting_repeat", "Repeat Off") ?: "Repeat Off",
            arabicFontSizeSp = p.getFloat("setting_font_size", 24f),
            isNightMode = p.getBoolean("setting_night_mode", false),
            audioPlayMode = audioMode,
            translationVoiceLang = transVoiceLang,
            mushafThemeId = p.getString("setting_mushaf_theme_id", "madani_cream") ?: "madani_cream"
        )
    }

    fun saveSettings(s: QuranSettingsState) {
        prefs?.edit()
            ?.putString("setting_mode", s.readingMode.name)
            ?.putString("setting_lang", s.translationLanguage)
            ?.putString("setting_trans_id", s.translationId)
            ?.putString("setting_trans_name", s.translatorName)
            ?.putBoolean("setting_show_trans", s.showTranslation)
            ?.putBoolean("setting_show_tl", s.showTransliteration)
            ?.putString("setting_reciter_id", s.reciterId)
            ?.putString("setting_reciter_name", s.reciterName)
            ?.putBoolean("setting_autoplay", s.autoplayNext)
            ?.putBoolean("setting_autoscroll", s.autoScroll)
            ?.putString("setting_tafsir_id", s.tafsirId)
            ?.putString("setting_tafsir_name", s.tafsirName)
            ?.putString("setting_repeat", s.repeatMode)
            ?.putFloat("setting_font_size", s.arabicFontSizeSp)
            ?.putBoolean("setting_night_mode", s.isNightMode)
            ?.putString("setting_audio_mode", s.audioPlayMode.name)
            ?.putString("setting_trans_voice_lang", s.translationVoiceLang)
            ?.putString("setting_mushaf_theme_id", s.mushafThemeId)
            ?.apply()
    }

    fun getLanguageInfo(code: String): Triple<String, String, String> {
        return when (code) {
            "en" -> Triple("English (en)", "85", "Abdul Haleem")
            "ur" -> Triple("Urdu (ur)", "234", "Fateh Muhammad Jalandhry")
            "ar" -> Triple("Arabic (ar)", "16", "تفسير الميسر")
            "bn" -> Triple("Bengali (bn)", "161", "Taisirul Quran")
            "ta" -> Triple("Tamil (ta)", "229", "Sheikh Omar Sharif")
            "ml" -> Triple("Malayalam (ml)", "80", "Muhammad Karakunnu")
            "gu" -> Triple("Gujarati (gu)", "225", "Rabila Al-Umry")
            "mr" -> Triple("Marathi (mr)", "226", "Muhammad Shafi Ansari")
            "te" -> Triple("Telugu (te)", "227", "Maulana Abder-Rahim")
            "kn" -> Triple("Kannada (kn)", "771", "Kannada Translation")
            "tr" -> Triple("Turkish (tr)", "124", "Diyanet Isleri")
            "id" -> Triple("Indonesian (id)", "33", "Indonesian Ministry")
            "fr" -> Triple("French (fr)", "31", "Muhammad Hamidullah")
            "ru" -> Triple("Russian (ru)", "45", "Elmir Kuliev")
            "de" -> Triple("German (de)", "27", "Frank Bubenheim and Nadeem")
            "es" -> Triple("Spanish (es)", "83", "Sheikh Isa Garcia")
            "it" -> Triple("Italian (it)", "153", "Hamza Roberto Piccardo")
            "fa" -> Triple("Persian (fa)", "135", "Hussein Ansarian")
            "zh" -> Triple("Chinese (zh)", "109", "Muhammad Ma Jian")
            "sw" -> Triple("Swahili (sw)", "236", "Ali Muhsin Al-Barwani")
            "so" -> Triple("Somali (so)", "237", "Mahmud Muhammad Abduh")
            "ps" -> Triple("Pashto (ps)", "238", "Abdulwali")
            "uz" -> Triple("Uzbek (uz)", "239", "Alauddin Mansour / Sodiq")
            "ku" -> Triple("Kurdish (ku)", "240", "Burhan Muhammad-Amin")
            "bs" -> Triple("Bosnian (bs)", "25", "Besim Korkut")
            "pt" -> Triple("Portuguese (pt)", "103", "Samir El-Hayek")
            "nl" -> Triple("Dutch (nl)", "241", "Salomo Keyzer")
            "no" -> Triple("Norwegian (no)", "242", "Einar Berg")
            "sv" -> Triple("Swedish (sv)", "243", "Knut Bernström")
            "pl" -> Triple("Polish (pl)", "244", "Józef Bielawski")
            "ro" -> Triple("Romanian (ro)", "245", "George Grigore")
            "sq" -> Triple("Albanian (sq)", "246", "Sherif Ahmeti")
            "bg" -> Triple("Bulgarian (bg)", "247", "Tzvetan Theophanov")
            "cs" -> Triple("Czech (cs)", "248", "Prekland / Hrbek")
            "ha" -> Triple("Hausa (ha)", "32", "Abubakar Mahmoud Gumi")
            "ja" -> Triple("Japanese (ja)", "218", "Ryoichi Mita")
            "ko" -> Triple("Korean (ko)", "222", "Korean Muslim Federation")
            "th" -> Triple("Thai (th)", "249", "King Fahd Complex")
            "tg" -> Triple("Tajik (tg)", "250", "AbdolMohammad Ayati")
            "az" -> Triple("Azerbaijani (az)", "251", "Vasim Mammadaliyev")
            "ms" -> Triple("Malay (ms)", "39", "Abdullah Muhammad Basmeih")
            else -> Triple("Hindi (hi)", "122", "Maulana Azizul Haque al-Umari")
        }
    }

    fun getFlagEmoji(code: String): String = when (code) {
        "en" -> "🇬🇧"
        "ur" -> "🇮🇳"
        "ar" -> "🇸🇦"
        "bn" -> "🇧🇩"
        "tr" -> "🇹🇷"
        "id" -> "🇮🇩"
        "fr" -> "🇫🇷"
        "ru" -> "🇷🇺"
        "de" -> "🇩🇪"
        "es" -> "🇪🇸"
        "it" -> "🇮🇹"
        "fa" -> "🇮🇷"
        "zh" -> "🇨🇳"
        "sw" -> "🇹🇿"
        "so" -> "🇸🇴"
        "ps" -> "🇦🇫"
        "uz" -> "🇺🇿"
        "ku" -> "🇮🇶"
        "bs" -> "🇧🇦"
        "pt" -> "🇵🇹"
        "nl" -> "🇳🇱"
        "no" -> "🇳🇴"
        "sv" -> "🇸🇪"
        "pl" -> "🇵🇱"
        "ro" -> "🇷🇴"
        "sq" -> "🇦🇱"
        "bg" -> "🇧🇬"
        "cs" -> "🇨🇿"
        "ha" -> "🇳🇬"
        "ja" -> "🇯🇵"
        "ko" -> "🇰🇷"
        "th" -> "🇹🇭"
        "tg" -> "🇹🇯"
        "az" -> "🇦🇿"
        "ms" -> "🇲🇾"
        "ta", "ml", "gu", "mr", "te", "kn", "hi" -> "🇮🇳"
        else -> "🇮🇳"
    }

    fun switchLanguage(langCode: String): QuranSettingsState {
        val current = loadSettings()
        val info = getLanguageInfo(langCode)
        val reciterItem = QuranData.reciters.find { it.identifier == current.reciterId }
        val localizedReciterName = reciterItem?.getLocalizedName(langCode) ?: current.reciterName
        val updated = current.copy(
            translationLanguage = langCode,
            translationId = info.second,
            translatorName = info.third,
            reciterName = localizedReciterName,
            showTranslation = true,
            showTransliteration = true
        )
        saveSettings(updated)
        return updated
    }

    fun isTourDone(): Boolean {
        return prefs?.getBoolean("dt_quran_tour_completed", false) ?: false
    }

    fun setTourDone(done: Boolean) {
        prefs?.edit()?.putBoolean("dt_quran_tour_completed", done)?.apply()
    }
}
