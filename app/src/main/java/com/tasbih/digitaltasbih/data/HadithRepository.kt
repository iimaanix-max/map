package com.tasbih.digitaltasbih.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object HadithRepository {
    private const val PREFS_NAME = "hadith_prefs"
    private const val KEY_BOOKMARKS = "dt_hadith_bookmarks"
    private const val KEY_CACHE_COLLECTIONS = "hadith_collections_v1"
    private const val DEFAULT_BACKEND = "https://hadith-proxy.hadithproxy.workers.dev"

    const val HERO_BG_ASSET = "media/98c1f14ed453c147.webp"

    val SPECIAL_COVERS = listOf(
        "media/ea705f13be3b169e.webp", // 1. Bukhari
        "media/65d78a1c4bbee8b0.webp", // 2. Muslim
        "media/b036a50d52ae79b5.webp", // 3. Nasa'i
        "media/aabb83b5c5fa20c5.webp", // 4. Abu Dawud
        "media/ab4c04b501cf7a25.webp", // 5. Tirmidhi
        "media/eb480f28d85074c0.webp", // 6. Ibn Majah
        "media/507ae5bfece8ca86.webp", // 7. Muwatta Malik
        "media/03f04a03f8828e71.webp", // 8. Musnad Ahmad
        "media/34849f02043d418c.webp", // 9. Sunan ad-Darimi
        "media/ada8c5ad74f6f32f.webp", // 10. Sahih Ibn Khuzayma
        "media/7fba7acfc79f171d.webp", // 11. Sahih Ibn Hibban
        "media/910fa6daa74a9b71.webp", // 12. Mustadrak al-Hakim
        "media/6e0198379b74724f.webp",
        "media/f1227cd2d119d418.webp",
        "media/1b8f54f670bc37ec.webp",
        "media/fd74aff700b15611.webp",
        "media/28dcebf993b6d2ea.webp",
        "media/806369b023203f95.webp",
        "media/c014dca9c705d9dd.webp",
        "media/8e5770cf7c4610fc.webp",
        "media/bd68739369138802.webp",
        "media/d8d830de858224b0.webp",
        "media/902d69b6e5ecde77.webp",
        "media/e52b447de16bd15f.webp",
        "media/bb2e193dbabf469e.webp",
        "media/59d03af07879ff62.webp",
        "media/ae267cf2e7362b66.webp"
    )

    private var appContext: Context? = null
    private var cachedCollections: List<HadithCollectionItem>? = null

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    private fun getPrefs(): SharedPreferences? =
        appContext?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    suspend fun getCollections(): List<HadithCollectionItem> = withContext(Dispatchers.IO) {
        cachedCollections?.let { if (it.isNotEmpty()) return@withContext it }

        // 1. Check local SharedPreferences cache
        val prefs = getPrefs()
        val cachedJson = prefs?.getString(KEY_CACHE_COLLECTIONS, null)
        if (!cachedJson.isNullOrEmpty()) {
            try {
                val list = parseCollectionsJson(cachedJson)
                if (list.isNotEmpty()) {
                    cachedCollections = list
                    return@withContext list
                }
            } catch (_: Exception) {}
        }

        // 2. Check pre-bundled asset hadith_collections.json
        try {
            appContext?.assets?.open("hadith_collections.json")?.use { stream ->
                val assetJson = stream.bufferedReader().use { it.readText() }
                val list = parseCollectionsJson(assetJson)
                if (list.isNotEmpty()) {
                    cachedCollections = list
                }
            }
        } catch (_: Exception) {}

        // 3. Try to fetch fresh online in background/current call
        try {
            val netJson = httpGet("$DEFAULT_BACKEND/v1/collections")
            if (!netJson.isNullOrEmpty()) {
                val obj = JSONObject(netJson)
                val dataArr = obj.optJSONArray("data")?.toString() ?: netJson
                val list = parseCollectionsJson(dataArr)
                if (list.isNotEmpty()) {
                    cachedCollections = list
                    prefs?.edit()?.putString(KEY_CACHE_COLLECTIONS, dataArr)?.apply()
                    return@withContext list
                }
            }
        } catch (_: Exception) {}

        cachedCollections ?: emptyList()
    }

    private fun parseCollectionsJson(jsonStr: String): List<HadithCollectionItem> {
        val arr = try { JSONArray(jsonStr) } catch (_: Exception) { return emptyList() }
        val out = mutableListOf<HadithCollectionItem>()
        for (i in 0 until arr.length()) {
            val obj = arr.optJSONObject(i) ?: continue
            val name = obj.optString("name", "")
            val colArr = obj.optJSONArray("collection")
            var titleEn = name
            var titleAr = ""
            var shortIntro = ""
            if (colArr != null) {
                for (j in 0 until colArr.length()) {
                    val langObj = colArr.optJSONObject(j) ?: continue
                    val lang = langObj.optString("lang", "")
                    if (lang == "en") {
                        titleEn = langObj.optString("title", titleEn)
                        shortIntro = langObj.optString("shortIntro", shortIntro)
                    } else if (lang == "ar") {
                        titleAr = langObj.optString("title", titleAr)
                    }
                }
            }
            val totalHadith = obj.optInt("totalHadith", obj.optInt("totalAvailableHadith", 0))
            val totalBooks = obj.optInt("totalBooks", 0)
            val coverAsset = if (i < SPECIAL_COVERS.size) SPECIAL_COVERS[i] else ""
            val badgeNumber = String.format(java.util.Locale.US, "%02d", i + 1)
            out.add(
                HadithCollectionItem(
                    name = name,
                    titleEn = titleEn,
                    titleAr = titleAr,
                    shortIntro = stripHtml(shortIntro),
                    totalHadith = totalHadith,
                    totalBooks = totalBooks,
                    coverAsset = coverAsset,
                    badgeNumber = badgeNumber,
                    index = i
                )
            )
        }
        return out
    }

    suspend fun getBooks(collectionName: String): List<HadithBookItem> = withContext(Dispatchers.IO) {
        val cacheKey = "h_books_$collectionName"
        val prefs = getPrefs()
        val cached = prefs?.getString(cacheKey, null)
        if (!cached.isNullOrEmpty()) {
            try {
                val list = parseBooksJson(cached)
                if (list.isNotEmpty()) return@withContext list
            } catch (_: Exception) {}
        }

        // Pre-bundled fallback for bukhari
        if (collectionName.equals("bukhari", ignoreCase = true)) {
            try {
                appContext?.assets?.open("hadith_bukhari_books.json")?.use { s ->
                    val txt = s.bufferedReader().use { it.readText() }
                    val list = parseBooksJson(txt)
                    if (list.isNotEmpty()) return@withContext list
                }
            } catch (_: Exception) {}
        }

        // Fetch from network
        try {
            val netJson = httpGet("$DEFAULT_BACKEND/v1/collections/$collectionName/books?limit=100")
            if (!netJson.isNullOrEmpty()) {
                val obj = JSONObject(netJson)
                val dataArr = obj.optJSONArray("data")?.toString() ?: netJson
                val list = parseBooksJson(dataArr)
                if (list.isNotEmpty()) {
                    prefs?.edit()?.putString(cacheKey, dataArr)?.apply()
                    return@withContext list
                }
            }
        } catch (_: Exception) {}

        emptyList()
    }

    private fun parseBooksJson(jsonStr: String): List<HadithBookItem> {
        val arr = try { JSONArray(jsonStr) } catch (_: Exception) { return emptyList() }
        val out = mutableListOf<HadithBookItem>()
        for (i in 0 until arr.length()) {
            val obj = arr.optJSONObject(i) ?: continue
            val bookNumber = obj.optString("bookNumber", "${i + 1}")
            val bArr = obj.optJSONArray("book")
            var nameEn = ""
            var nameAr = ""
            if (bArr != null) {
                for (j in 0 until bArr.length()) {
                    val lObj = bArr.optJSONObject(j) ?: continue
                    if (lObj.optString("lang") == "en") nameEn = lObj.optString("name")
                    if (lObj.optString("lang") == "ar") nameAr = lObj.optString("name")
                }
            }
            if (nameEn.isEmpty()) nameEn = "Book $bookNumber"
            val startNum = obj.optInt("hadithStartNumber", 0)
            val endNum = obj.optInt("hadithEndNumber", 0)
            val count = obj.optInt("numberOfHadith", 0)
            out.add(
                HadithBookItem(
                    bookNumber = bookNumber,
                    nameEn = nameEn,
                    nameAr = nameAr,
                    hadithStartNumber = startNum,
                    hadithEndNumber = endNum,
                    numberOfHadith = count
                )
            )
        }
        return out
    }

    suspend fun getChapters(collectionName: String, bookNumber: String): List<HadithChapterItem> = withContext(Dispatchers.IO) {
        val cacheKey = "h_chap_${collectionName}_$bookNumber"
        val prefs = getPrefs()
        val cached = prefs?.getString(cacheKey, null)
        if (!cached.isNullOrEmpty()) {
            try {
                val list = parseChaptersJson(cached, bookNumber)
                if (list.isNotEmpty()) return@withContext list
            } catch (_: Exception) {}
        }

        if (collectionName.equals("bukhari", ignoreCase = true) && bookNumber == "1") {
            try {
                appContext?.assets?.open("hadith_bukhari_book1_chapters.json")?.use { s ->
                    val txt = s.bufferedReader().use { it.readText() }
                    val list = parseChaptersJson(txt, bookNumber)
                    if (list.isNotEmpty()) return@withContext list
                }
            } catch (_: Exception) {}
        }

        try {
            val netJson = httpGet("$DEFAULT_BACKEND/v1/collections/$collectionName/books/$bookNumber/chapters?limit=100")
            if (!netJson.isNullOrEmpty()) {
                val obj = JSONObject(netJson)
                val dataArr = obj.optJSONArray("data")?.toString() ?: netJson
                val list = parseChaptersJson(dataArr, bookNumber)
                if (list.isNotEmpty()) {
                    prefs?.edit()?.putString(cacheKey, dataArr)?.apply()
                    return@withContext list
                }
            }
        } catch (_: Exception) {}

        emptyList()
    }

    private fun parseChaptersJson(jsonStr: String, defaultBookNum: String): List<HadithChapterItem> {
        val arr = try { JSONArray(jsonStr) } catch (_: Exception) { return emptyList() }
        val out = mutableListOf<HadithChapterItem>()
        for (i in 0 until arr.length()) {
            val obj = arr.optJSONObject(i) ?: continue
            val bookNumber = obj.optString("bookNumber", defaultBookNum)
            val chapterId = obj.optString("chapterId", "${i + 1}.00")
            val cArr = obj.optJSONArray("chapter")
            var titleEn = ""
            var titleAr = ""
            var chapterNumber = ""
            var intro: String? = null
            if (cArr != null) {
                for (j in 0 until cArr.length()) {
                    val lObj = cArr.optJSONObject(j) ?: continue
                    val lang = lObj.optString("lang")
                    if (lang == "en") {
                        titleEn = lObj.optString("chapterTitle")
                        chapterNumber = lObj.optString("chapterNumber")
                        val rawIntro = lObj.optString("intro")
                        if (rawIntro.isNotEmpty() && rawIntro != "null") intro = stripHtml(rawIntro)
                    } else if (lang == "ar") {
                        titleAr = lObj.optString("chapterTitle")
                    }
                }
            }
            if (titleEn.isEmpty()) titleEn = "Chapter $chapterId"
            out.add(
                HadithChapterItem(
                    bookNumber = bookNumber,
                    chapterId = chapterId,
                    chapterNumber = chapterNumber,
                    titleEn = titleEn,
                    titleAr = titleAr,
                    intro = intro
                )
            )
        }
        return out
    }

    suspend fun getHadiths(collectionName: String, bookNumber: String, page: Int = 1): List<HadithDetailItem> = withContext(Dispatchers.IO) {
        val cacheKey = "h_hadiths_${collectionName}_${bookNumber}_p$page"
        val prefs = getPrefs()
        val cached = prefs?.getString(cacheKey, null)
        if (!cached.isNullOrEmpty()) {
            try {
                val list = parseHadithsJson(cached, collectionName, bookNumber)
                if (list.isNotEmpty()) return@withContext list
            } catch (_: Exception) {}
        }

        if (collectionName.equals("bukhari", ignoreCase = true) && bookNumber == "1" && page == 1) {
            try {
                appContext?.assets?.open("hadith_bukhari_book1_hadiths.json")?.use { s ->
                    val txt = s.bufferedReader().use { it.readText() }
                    val list = parseHadithsJson(txt, collectionName, bookNumber)
                    if (list.isNotEmpty()) return@withContext list
                }
            } catch (_: Exception) {}
        }

        try {
            val netJson = httpGet("$DEFAULT_BACKEND/v1/collections/$collectionName/books/$bookNumber/hadiths?limit=20&page=$page")
            if (!netJson.isNullOrEmpty()) {
                val obj = JSONObject(netJson)
                val dataArr = obj.optJSONArray("data")?.toString() ?: netJson
                val list = parseHadithsJson(dataArr, collectionName, bookNumber)
                if (list.isNotEmpty()) {
                    prefs?.edit()?.putString(cacheKey, dataArr)?.apply()
                    return@withContext list
                }
            }
        } catch (_: Exception) {}

        emptyList()
    }

    private fun parseHadithsJson(jsonStr: String, collection: String, defaultBookNum: String): List<HadithDetailItem> {
        val arr = try { JSONArray(jsonStr) } catch (_: Exception) { return emptyList() }
        val out = mutableListOf<HadithDetailItem>()
        for (i in 0 until arr.length()) {
            val obj = arr.optJSONObject(i) ?: continue
            val bookNumber = obj.optString("bookNumber", defaultBookNum)
            val chapterId = obj.optString("chapterId", "")
            val hadithNumber = obj.optString("hadithNumber", "${i + 1}")
            val hList = obj.optJSONArray("hadith")

            var arabicText = ""
            var rawEnglishBody = ""
            var grade = ""
            var gradedBy = ""

            if (hList != null) {
                for (j in 0 until hList.length()) {
                    val entry = hList.optJSONObject(j) ?: continue
                    val lang = entry.optString("lang")
                    if (lang == "ar") {
                        arabicText = stripHtml(entry.optString("body"))
                    } else if (lang == "en") {
                        rawEnglishBody = stripHtml(entry.optString("body"))
                        val gradesArr = entry.optJSONArray("grades")
                        if (gradesArr != null && gradesArr.length() > 0) {
                            val gObj = gradesArr.optJSONObject(0)
                            if (gObj != null) {
                                grade = gObj.optString("grade").trim()
                                gradedBy = gObj.optString("graded_by").trim()
                                if (grade.equals("null", ignoreCase = true)) grade = ""
                                if (gradedBy.equals("null", ignoreCase = true)) gradedBy = ""
                            }
                        }
                    }
                }
            }

            var narrator = ""
            var bodyText = rawEnglishBody
            val narrMatch = Regex("^(Narrated [^:]+:|عَنْ [^:]+:|حدثنا [^:]+:)", RegexOption.IGNORE_CASE).find(rawEnglishBody)
            if (narrMatch != null) {
                narrator = narrMatch.value.trim()
                bodyText = rawEnglishBody.substring(narrMatch.range.last + 1).trim()
            }

            val inBookRef = "Book $bookNumber, Hadith ${i + 1}"
            val refUrl = "https://sunnah.com/$collection:$hadithNumber"

            out.add(
                HadithDetailItem(
                    collection = collection,
                    bookNumber = bookNumber,
                    chapterId = chapterId,
                    hadithNumber = hadithNumber,
                    arabicText = arabicText,
                    narrator = narrator,
                    bodyText = bodyText,
                    grade = grade,
                    gradedBy = gradedBy,
                    refUrl = refUrl,
                    inBookRef = inBookRef
                )
            )
        }
        return out
    }

    // ── Bookmarks ──
    fun getBookmarks(): List<HadithBookmarkItem> {
        val prefs = getPrefs() ?: return emptyList()
        val json = prefs.getString(KEY_BOOKMARKS, null) ?: return emptyList()
        return try {
            val arr = JSONArray(json)
            val list = mutableListOf<HadithBookmarkItem>()
            for (i in 0 until arr.length()) {
                val o = arr.optJSONObject(i) ?: continue
                list.add(
                    HadithBookmarkItem(
                        hadithNum = o.optString("hadithNum"),
                        refLabel = o.optString("refLabel"),
                        refUrl = o.optString("refUrl"),
                        arText = o.optString("arText"),
                        narrator = o.optString("narrator"),
                        bodyText = o.optString("bodyText"),
                        collection = o.optString("collection"),
                        bookNumber = o.optString("bookNumber"),
                        timestamp = o.optLong("timestamp", System.currentTimeMillis())
                    )
                )
            }
            list
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun isBookmarked(collection: String, hadithNum: String): Boolean {
        return getBookmarks().any { it.collection.equals(collection, ignoreCase = true) && it.hadithNum == hadithNum }
    }

    fun toggleBookmark(item: HadithBookmarkItem): Boolean {
        val prefs = getPrefs() ?: return false
        val current = getBookmarks().toMutableList()
        val existingIndex = current.indexOfFirst {
            it.collection.equals(item.collection, ignoreCase = true) && it.hadithNum == item.hadithNum
        }
        val isNowBookmarked: Boolean
        if (existingIndex >= 0) {
            current.removeAt(existingIndex)
            isNowBookmarked = false
        } else {
            current.add(0, item)
            isNowBookmarked = true
        }

        val arr = JSONArray()
        for (b in current) {
            val o = JSONObject()
            o.put("hadithNum", b.hadithNum)
            o.put("refLabel", b.refLabel)
            o.put("refUrl", b.refUrl)
            o.put("arText", b.arText)
            o.put("narrator", b.narrator)
            o.put("bodyText", b.bodyText)
            o.put("collection", b.collection)
            o.put("bookNumber", b.bookNumber)
            o.put("timestamp", b.timestamp)
            arr.put(o)
        }
        prefs.edit().putString(KEY_BOOKMARKS, arr.toString()).apply()
        return isNowBookmarked
    }

    private fun stripHtml(html: String): String {
        return html.replace(Regex("<[^>]*>"), "")
            .replace("&nbsp;", " ")
            .replace("&quot;", "\"")
            .replace("&apos;", "'")
            .replace("&amp;", "&")
            .replace("&#39;", "'")
            .replace(Regex("&[a-zA-Z0-9#]+;"), "")
            .trim()
    }

    private fun httpGet(urlStr: String): String? {
        return try {
            val url = URL(urlStr)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.connectTimeout = 8000
            conn.readTimeout = 8000
            conn.setRequestProperty("User-Agent", "Mozilla/5.0")
            if (conn.responseCode == 200) {
                conn.inputStream.bufferedReader().use { it.readText() }
            } else null
        } catch (_: Exception) {
            null
        }
    }
}
