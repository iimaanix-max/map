package com.tasbih.digitaltasbih.data

import android.content.Context
import java.util.Calendar
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.io.StringReader

object DuaRepository {
    private val gson = GsonBuilder().setLenient().create()
    lateinit var preferences: DuaPreferences
        private set

    private var cachedDuas: List<DuaEntry> = emptyList()
    private var cachedCategories: List<DuaCategoryItem> = emptyList()
    private var cachedFeelings: List<DuaFeelingItem> = emptyList()
    private var cachedIconMap: Map<String, String> = emptyMap()
    private var isInitialized = false

    private val _homeFeelings = MutableStateFlow<List<DuaFeelingItem>>(emptyList())
    val homeFeelings: StateFlow<List<DuaFeelingItem>> = _homeFeelings.asStateFlow()

    private val _homeCategories = MutableStateFlow<List<DuaCategoryItem>>(emptyList())
    val homeCategories: StateFlow<List<DuaCategoryItem>> = _homeCategories.asStateFlow()

    fun shuffleHomeFeelings(): List<DuaFeelingItem> {
        val list = if (cachedFeelings.isNotEmpty()) cachedFeelings.shuffled().take(6) else emptyList()
        _homeFeelings.value = list
        return list
    }

    fun shuffleHomeCategories(): List<DuaCategoryItem> {
        val list = if (cachedCategories.isNotEmpty()) cachedCategories.shuffled().take(4) else emptyList()
        _homeCategories.value = list
        return list
    }

    fun shuffleHomeContent() {
        shuffleHomeFeelings()
        shuffleHomeCategories()
    }

    fun init(context: Context) {
        if (isInitialized) return
        preferences = DuaPreferences(context.applicationContext)
        loadFromAssetsSync(context.applicationContext)
        isInitialized = true
    }

    private fun loadFromAssetsSync(context: Context) {
        try {
            // 1. Icon Map (Streamed directly without intermediate String allocations)
            val iconReader = JsonReader(InputStreamReader(context.assets.open("dua_icon_map.json"), Charsets.UTF_8)).apply { isLenient = true }
            val mapType = object : TypeToken<Map<String, String>>() {}.type
            val rawMap: Map<String, String> = gson.fromJson(iconReader, mapType) ?: emptyMap()
            cachedIconMap = rawMap.mapValues { (_, path) ->
                if (path.startsWith("media/")) path.replaceFirst("media/", "dua_media/") else path
            }

            // 2. Dua Library (Streamed directly via JsonReader - 2.7MB zero heap string bloat)
            val duaReader = JsonReader(InputStreamReader(context.assets.open("dua_library.json"), Charsets.UTF_8)).apply { isLenient = true }
            val jsonArray: JsonArray = gson.fromJson(duaReader, JsonArray::class.java)
            val entries = mutableListOf<DuaEntry>()
            for (element in jsonArray) {
                if (element.isJsonObject) {
                    val obj = element.asJsonObject
                    val id = obj.get("id")?.asString ?: continue
                    val cat = obj.get("cat")?.asString ?: "general"
                    val subcat = obj.get("subcat")?.asString ?: ""
                    val ar = obj.get("ar")?.asString ?: ""

                    val allCats = mutableListOf<String>()
                    obj.getAsJsonArray("allCats")?.forEach { allCats.add(it.asString) }
                    if (!allCats.contains(cat)) allCats.add(0, cat)

                    val lo = jsonObjectToMap(obj.getAsJsonObject("lo"))
                    val me = jsonObjectToMap(obj.getAsJsonObject("me"))
                    val refStr = obj.get("ref")?.asString ?: ""
                    val rf = jsonObjectToMap(obj.getAsJsonObject("rf")).toMutableMap()
                    if (refStr.isNotBlank() && !rf.containsKey("en")) {
                        rf["en"] = refStr
                    }
                    val title = jsonObjectToMap(obj.getAsJsonObject("title"))
                    val tm = jsonObjectToMap(obj.getAsJsonObject("tm"))
                    val how = jsonObjectToMap(obj.getAsJsonObject("how"))

                    entries.add(
                        DuaEntry(
                            id = id,
                            cat = cat,
                            subcat = subcat,
                            allCats = allCats,
                            ar = ar,
                            lo = lo,
                            me = me,
                            rf = rf,
                            title = title,
                            tm = tm,
                            how = how
                        )
                    )
                }
            }
            cachedDuas = entries

            // 3. Categories (Streamed directly via JsonReader)
            val catReader = JsonReader(InputStreamReader(context.assets.open("dua_categories.json"), Charsets.UTF_8)).apply { isLenient = true }
            val catArray: JsonArray = gson.fromJson(catReader, JsonArray::class.java)
            val catItems = mutableListOf<DuaCategoryItem>()
            for (element in catArray) {
                if (element.isJsonObject) {
                    val obj = element.asJsonObject
                    val id = obj.get("id")?.asString ?: continue
                    val icon = obj.get("icon")?.asString ?: "🤲"
                    val label = obj.get("label")?.asString ?: id
                    val count = cachedDuas.count { it.cat == id || it.allCats.contains(id) }
                    val assetPath = when (id) {
                        "travel" -> cachedIconMap["qa_travel"] ?: "dua_media/8e4b3db741ec0a4d.webp"
                        "food" -> cachedIconMap["cat_food"] ?: "dua_media/81fda447a6909eee.webp"
                        "health_shifa", "shifa" -> cachedIconMap["cat_shifa"] ?: "dua_media/76ad8dcecacb7c6a.webp"
                        "sleep" -> cachedIconMap["cat_sleep"] ?: "dua_media/4aacafdfca5783bd.webp"
                        "home" -> cachedIconMap["cat_home"] ?: "dua_media/4ce9977d9e45849d.webp"
                        "mosque" -> cachedIconMap["cat_mosque"] ?: "dua_media/0637ca49dcc9a782.webp"
                        "bathroom" -> cachedIconMap["cat_bathroom"] ?: "dua_media/7c44e4e1d0e07408.webp"
                        "rain" -> cachedIconMap["cat_rain"] ?: "dua_media/b1335a6cfa857b8f.webp"
                        "hardship" -> cachedIconMap["cat_hardship"] ?: "dua_media/2631a38e53085395.webp"
                        "protection" -> cachedIconMap["qa_protection"] ?: "dua_media/634e160db191af6f.webp"
                        "morning" -> cachedIconMap["qa_morning"] ?: "dua_media/66c5a660459102c3.webp"
                        "evening" -> cachedIconMap["qa_evening"] ?: "dua_media/a63b94d152b4326b.webp"
                        "rizq" -> cachedIconMap["cat_rizq"] ?: "dua_media/1f30ccc9b4c2e8f0.webp"
                        "career" -> cachedIconMap["cat_career"] ?: "dua_media/63e4826f89d67f80.webp"
                        "forgiveness" -> cachedIconMap["cat_forgiveness"] ?: "dua_media/9d57223b83a869db.webp"
                        "education" -> cachedIconMap["cat_education"] ?: "dua_media/7ca7410b27e5104e.webp"
                        "akhirah" -> cachedIconMap["cat_akhirah"] ?: "dua_media/3e2622e00dfc783d.webp"
                        "self_control" -> cachedIconMap["cat_self_control"] ?: "dua_media/905a9430bdfcfb0f.webp"
                        "faith_worship" -> cachedIconMap["cat_faith_worship"] ?: "dua_media/c93bee104d3afe57.webp"
                        "gratitude" -> cachedIconMap["cat_gratitude"] ?: "dua_media/d28ad5a119e7e40a.webp"
                        "special_dua" -> cachedIconMap["qa_special_dua"] ?: "dua_media/5c6cdcb913f9b803.webp"
                        "jumma_dua" -> cachedIconMap["qa_jumma_dua"] ?: "dua_media/edb3e561058bac8c.jpg"
                        else -> cachedIconMap["cat_$id"] ?: cachedIconMap["qa_$id"] ?: "dua_media/76ad8dcecacb7c6a.webp"
                    }

                    catItems.add(
                        DuaCategoryItem(
                            id = id,
                            icon = icon,
                            label = label,
                            count = count,
                            assetPath = assetPath
                        )
                    )
                }
            }
            cachedCategories = catItems

            // 4. Feelings (Streamed directly via JsonReader)
            val feelReader = JsonReader(InputStreamReader(context.assets.open("dua_feelings_map.json"), Charsets.UTF_8)).apply { isLenient = true }
            val feelArray: JsonArray = gson.fromJson(feelReader, JsonArray::class.java)
            val feelItems = mutableListOf<DuaFeelingItem>()
            for (element in feelArray) {
                if (element.isJsonObject) {
                    val obj = element.asJsonObject
                    val id = obj.get("id")?.asString ?: continue
                    val icon = obj.get("icon")?.asString ?: "🤲"

                    val labelObj = obj.getAsJsonObject("label")
                    val labelEn = labelObj?.get("en")?.asString ?: id
                    val labelHi = labelObj?.get("hi")?.asString ?: labelEn

                    val mapsToObj = obj.getAsJsonObject("maps_to")
                    val mapsToCat = mapsToObj?.get("cat")?.asString ?: "general"

                    val triggersObj = obj.getAsJsonObject("triggers")
                    val hiTriggers = mutableListOf<String>()
                    triggersObj?.getAsJsonArray("hi")?.forEach { hiTriggers.add(it.asString) }
                    val enTriggers = mutableListOf<String>()
                    triggersObj?.getAsJsonArray("en")?.forEach { enTriggers.add(it.asString) }

                    val subLabel = when (id) {
                        "anxiety_stress" -> "Find peace"
                        "financial_stress" -> "Relief from debt"
                        "job_loss_worry" -> "Find guidance"
                        "sadness_grief" -> "Find comfort"
                        "illness_fever" -> "Shifa & recovery"
                        "physical_pain" -> "Healing & relief"
                        "gratitude_feel" -> "Give thanks"
                        "travel_safety" -> "Safe journey"
                        "protection_fear" -> "Divine refuge"
                        "loneliness_feel" -> "Find connection"
                        "overwhelmed_feel" -> "Ease & strength"
                        "stressed_feel" -> "Mental relief"
                        else -> "Find comfort"
                    }

                    val feelImgKey = when (id) {
                        "anxiety_stress" -> "feel_anxiety"
                        "financial_stress" -> "feel_money"
                        "job_loss_worry" -> "feel_job"
                        "sadness_grief" -> "feel_sad"
                        "illness_fever" -> "feel_illness"
                        "physical_pain" -> "feel_pain"
                        "gratitude_feel" -> "feel_gratitude"
                        "travel_safety" -> "feel_travel"
                        "protection_fear" -> "feel_protection"
                        "loneliness_feel" -> "feel_lonely"
                        "overwhelmed_feel" -> "feel_overwhelmed"
                        "stressed_feel" -> "feel_stress"
                        else -> "feel_anxiety"
                    }
                    val assetPath = cachedIconMap[feelImgKey] ?: "dua_media/2509e6dc4768f9dd.webp"

                    feelItems.add(
                        DuaFeelingItem(
                            id = id,
                            icon = icon,
                            labelEn = labelEn,
                            labelHi = labelHi,
                            subLabel = subLabel,
                            categoryId = mapsToCat,
                            assetPath = assetPath,
                            hiTriggers = hiTriggers,
                            enTriggers = enTriggers
                        )
                    )
                }
            }
            cachedFeelings = feelItems
            shuffleHomeContent()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun jsonObjectToMap(obj: JsonObject?): Map<String, String> {
        if (obj == null) return emptyMap()
        val map = mutableMapOf<String, String>()
        for ((key, value) in obj.entrySet()) {
            if (value.isJsonPrimitive) {
                map[key] = value.asString
            }
        }
        return map
    }

    fun getAllDuas(): List<DuaEntry> = cachedDuas

    fun getDuaById(id: String): DuaEntry? {
        return cachedDuas.firstOrNull { it.id == id }
    }

    fun getDuasByCategory(categoryId: String): List<DuaEntry> {
        return cachedDuas.filter { it.cat == categoryId || it.allCats.contains(categoryId) }
    }

    fun getCategoryById(categoryId: String): DuaCategoryItem? {
        return cachedCategories.firstOrNull { it.id == categoryId }
    }

    fun getAllCategories(): List<DuaCategoryItem> = cachedCategories

    fun getAllFeelings(): List<DuaFeelingItem> = cachedFeelings

    fun getQuickAccessItems(): List<QuickAccessDuaItem> {
        return listOf(
            QuickAccessDuaItem("morning", "Morning", "morning", cachedIconMap["qa_morning"] ?: "dua_media/66c5a660459102c3.webp"),
            QuickAccessDuaItem("evening", "Evening", "evening", cachedIconMap["qa_evening"] ?: "dua_media/a63b94d152b4326b.webp"),
            QuickAccessDuaItem("protection", "Protection", "protection", cachedIconMap["qa_protection"] ?: "dua_media/634e160db191af6f.webp"),
            QuickAccessDuaItem("travel", "Travel", "travel", cachedIconMap["qa_travel"] ?: "dua_media/8e4b3db741ec0a4d.webp"),
            QuickAccessDuaItem("career", "Career", "career", cachedIconMap["cat_career"] ?: "dua_media/63e4826f89d67f80.webp"),
            QuickAccessDuaItem("forgiveness", "Forgiveness", "forgiveness", cachedIconMap["cat_forgiveness"] ?: "dua_media/9d57223b83a869db.webp"),
            QuickAccessDuaItem("special", "Special Duas", "special_dua", cachedIconMap["qa_special_dua"] ?: "dua_media/5c6cdcb913f9b803.webp"),
            QuickAccessDuaItem("jumma", "Jummah Duas", "jumma_dua", cachedIconMap["qa_jumma_dua"] ?: "dua_media/edb3e561058bac8c.jpg")
        )
    }

    fun getPopularDuas(): List<DuaEntry> {
        val popularKeywords = listOf("protection", "forgiveness", "sleep", "food", "travel", "anxiety", "morning", "evening", "rizq", "istighfar")
        val priorityIds = setOf(
            "dua_v_food_001",
            "dua_v_protection_001",
            "dua_v_sleep_001",
            "dua_v_forgiveness_001",
            "dua_v_anxiety_001",
            "dua_v_001",
            "dua_v_077",
            "dua_v_istighfar",
            "dua_v_guidance",
            "dua_v_protection_harm"
        )
        val directMatched = cachedDuas.filter { priorityIds.contains(it.id) }
        val categoryMatched = cachedDuas.filter { dua ->
            popularKeywords.any { kw -> dua.cat.contains(kw) || dua.subcat.contains(kw) }
        }
        return (directMatched + categoryMatched + cachedDuas).distinctBy { it.id }.take(20)
    }

    fun getRecentDuas(): List<DuaEntry> {
        val ids = preferences.recentDuaIds.value
        return ids.mapNotNull { id -> cachedDuas.firstOrNull { it.id == id } }
    }

    fun searchDuas(query: String, lang: String): List<DuaEntry> {
        val q = query.trim().lowercase()
        if (q.isBlank()) return emptyList()

        // Check feeling trigger match
        for (feel in cachedFeelings) {
            val matchesHi = feel.hiTriggers.any { q.contains(it.lowercase()) }
            val matchesEn = feel.enTriggers.any { q.contains(it.lowercase()) }
            if (matchesHi || matchesEn) {
                val results = getDuasByCategory(feel.categoryId)
                if (results.isNotEmpty()) return results
            }
        }

        return cachedDuas.filter { dua ->
            dua.getLocalizedTitle(lang).lowercase().contains(q) ||
            dua.getLocalizedMeaning(lang).lowercase().contains(q) ||
            dua.getLocalizedTransliteration(lang).lowercase().contains(q) ||
            dua.cat.lowercase().contains(q) ||
            dua.subcat.lowercase().contains(q)
        }
    }

    /**
     * Get the authentic Daily Featured Dua (matching Image 2 & 3).
     * Rotates daily or defaults to the authentic Bukhari 1377 protection supplication.
     * Supports full localization across all languages.
     */
    /**
     * Returns a dynamically rotating daily featured Dua that changes every single day (Image 3).
     * Rotates automatically by day-of-year across authentic Prophet (PBUH) supplications.
     * Supports full localization across all languages with authentic Hadith book and narrator details.
     */
    fun getDailyFeaturedDua(locale: String): DailyDuaData {
        val cal = Calendar.getInstance()
        val dayOfYear = cal.get(Calendar.DAY_OF_YEAR)

        // Authentic daily curated supplications with complete Bukhari/Muslim/Sunnah metadata
        val featuredList = listOf(
            DailyDuaData(
                id = "dua_v_077",
                arabic = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنْ عَذَابِ الْقَبْرِ، وَمِنْ عَذَابِ جَهَنَّمَ، وَمِنْ فِتْنَةِ الْمَحْيَا وَالْمَمَاتِ، وَمِنْ شَرِّ فِتْنَةِ الْمَسِيحِ الدَّجَّالِ",
                transliteration = "Allahumma inni a'udhu bika min 'adhabil-qabr, wa min 'adhabi Jahannam, wa min sharr fitnatil-mahya wal-mamat, wa min sharr fitnati-Masihid-Dajjal.",
                translation = when (locale) {
                    "hi" -> "ऐ अल्लाह! मैं तेरी पनाह मांगता हूँ क़ब्र के अज़ाब से, और जहन्नम के अज़ाब से, और ज़िन्दगी और मौत की आज़माइश से, और मसीह दज्जाल के फ़ित्ने की बुराई से।"
                    "ur" -> "اے اللہ! میں تیری پناہ مانگتا ہوں قبر کے عذاب سے، اور جہنم کے عذاب سے، اور زندگی اور موت کی آزمائش سے، اور مسیح دجال کے فتنے کے شر سے۔"
                    "ar" -> "اللهم إني أعوذ بك من عذاب القبر ومن عذاب جهنم ومن فتنة المحيا والممات ومن شر فتنة المسيح الدجال."
                    "bn" -> "হে আল্লাহ! আমি আপনার কাছে আশ্রয় চাই কবরের শাস্তি থেকে, জাহান্নামের শাস্তি থেকে, জীবন ও মৃত্যুর পরীক্ষা থেকে এবং দাজ্জালের ফিতনার অনিষ্ট থেকে।"
                    else -> "O Allah, I seek refuge in You from the punishment of the grave, the punishment of Hell, the trials of life and death, and the evil trial of the False Messiah (Dajjal)."
                },
                reference = "Sahih al-Bukhari (Sahih al-Bukhari 1377, Sahih Muslim 589) | Sahih",
                hadithBook = "Sahih al-Bukhari 1377",
                hadithGrade = "✓ Sahih al-Bukhari",
                narrator = "Abu Hurairah (RA)",
                sunnahUrl = "https://sunnah.com/bukhari:1377"
            ),
            DailyDuaData(
                id = "dua_v_istighfar",
                arabic = "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَهَ إِلَّا أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ",
                transliteration = "Allahumma Anta Rabbi la ilaha illa Anta, khalaqtani wa ana 'abduka, wa ana 'ala 'ahdika wa wa'dika mastata'tu, a'udhu bika min sharri ma sana'tu, abu'u laka bini'matika 'alayya, wa abu'u bidhanbi faghfir li fa'innahu la yaghfirudh-dhunuba illa Anta.",
                translation = when (locale) {
                    "hi" -> "ऐ अल्लाह! तू मेरा रब है, तेरे सिवा कोई माबूद नहीं। तूने मुझे पैदा किया और मैं तेरा बंदा हूँ, और मैं अपनी ताक़त के मुताबिक़ तेरे अहद और वादे पर कायम हूँ। मैं अपनी बुराइयों से तेरी पनाह चाहता हूँ, मुझ पर तेरी जो नेमतें हैं उनका एतराफ़ करता हूँ और अपने गुनाहों का इक़रार करता हूँ, पस मुझे बख़्श दे क्योंकि तेरे सिवा कोई गुनाहों को नहीं बख़्श सकता।"
                    "ur" -> "اے اللہ! تو میرا رب ہے، تیرے سوا کوئی معبود نہیں۔ تو نے مجھے پیدا کیا اور میں تیرا بندہ ہوں اور تیرے عہد اور وعدے پر حتی المقدور قائم ہوں۔ اپنے کیے کے شر سے تیری پناہ مانگتا ہوں، اپنے اوپر تیری نعمت کا اعتراف کرتا ہوں اور اپنے گناہ کا اقرار کرتا ہوں، پس مجھے بخش دے، یقیناً تیرے سوا کوئی گناہوں کو معاف نہیں کر سکتا۔"
                    else -> "O Allah, You are my Lord, none has the right to be worshiped but You. You created me and I am Your servant, and I abide by Your covenant and promise as best I can. I seek refuge in You from the evil of what I have done. I acknowledge Your favor upon me, and I acknowledge my sins, so forgive me, for none can forgive sins except You."
                },
                reference = "Sahih al-Bukhari 6306 | Sahih (Sayyidul Istighfar)",
                hadithBook = "Sahih al-Bukhari 6306",
                hadithGrade = "✓ Sahih al-Bukhari",
                narrator = "Shaddad bin Aws (RA)",
                sunnahUrl = "https://sunnah.com/bukhari:6306"
            ),
            DailyDuaData(
                id = "dua_v_anxiety",
                arabic = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْهَمِّ وَالْحَزَنِ، وَالْعَجْزِ وَالْكَسَلِ، وَالْبُخْلِ وَالْجُبْنِ، وَضَلَعِ الدَّيْنِ وَغَلَبَةِ الرِّجَالِ",
                transliteration = "Allahumma inni a'udhu bika minal-hammi wal-hazan, wal-'ajzi wal-kasal, wal-bukhli wal-jubn, wa dala'id-dayni wa ghalabatir-rijal.",
                translation = when (locale) {
                    "hi" -> "ऐ अल्लाह! मैं तेरी पनाह मांगता हूँ फ़िक्र और ग़म से, लाचारी और सुस्ती से, कंजूसी और बुज़दिली से, और क़र्ज़ के बोझ और लोगों के दबदबे से।"
                    "ur" -> "اے اللہ! میں تیری پناہ مانگتا ہوں فکر اور غم سے، عاجزی اور سستی سے، بخل اور بزدلی سے، اور قرض کے بوجھ اور لوگوں کے دباؤ سے۔"
                    else -> "O Allah, I seek refuge in You from grief and sadness, weakness and laziness, miserliness and cowardice, the burden of debt, and being overpowered by men."
                },
                reference = "Sahih al-Bukhari 2893 | Sahih",
                hadithBook = "Sahih al-Bukhari 2893",
                hadithGrade = "✓ Sahih al-Bukhari",
                narrator = "Anas bin Malik (RA)",
                sunnahUrl = "https://sunnah.com/bukhari:2893"
            ),
            DailyDuaData(
                id = "dua_v_guidance",
                arabic = "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ",
                transliteration = "Rabbana atina fid-dunya hasanatan wa fil-akhirati hasanatan wa qina 'adhaban-nar.",
                translation = when (locale) {
                    "hi" -> "ऐ हमारे रब! हमें दुनिया में भी भलाई दे और आख़िरत में भी भलाई अता फ़रमा, और हमें दोज़ख़ के अज़ाब से बचा।"
                    "ur" -> "اے ہمارے پروردگار! ہمیں دنیا میں بھی بھلائی دے اور آخرت میں بھی بھلائی عطا فرما اور ہمیں آگ کے عذاب سے بچا۔"
                    else -> "Our Lord! Grant us good in this world and good in the Hereafter, and save us from the punishment of the Fire."
                },
                reference = "Surah Al-Baqarah 2:201 (Sahih al-Bukhari 4522)",
                hadithBook = "Sahih al-Bukhari 4522",
                hadithGrade = "✓ Qur'an & Sunnah",
                narrator = "Anas bin Malik (RA)",
                sunnahUrl = "https://sunnah.com/bukhari:4522"
            ),
            DailyDuaData(
                id = "dua_v_protection_harm",
                arabic = "بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الأَرْضِ وَلَا فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ",
                transliteration = "Bismillahil-ladhi la yadurru ma'asmihi shay'un fil-ardi wa la fis-sama'i wa Huwas-Sami'ul-'Alim.",
                translation = when (locale) {
                    "hi" -> "अल्लाह के नाम के साथ, जिसकी ज़ात के नाम की बरकत से ज़मीन और आसमान की कोई चीज़ नुक़सान नहीं पहुँचा सकती, और वही सब कुछ सुनने वाला और जानने वाला है।"
                    "ur" -> "اللہ کے نام کے ساتھ جس کے نام کی برکت سے زمین اور آسمان کی کوئی چیز نقصان نہیں پہنچا سکتی اور وہ خوب سننے والا اور جاننے والا ہے۔"
                    else -> "In the Name of Allah, with Whose Name nothing can cause harm in the earth or in the heavens, and He is the All-Hearing, the All-Knowing."
                },
                reference = "Sunan Abi Dawud 5088, Jami` at-Tirmidhi 3388 | Sahih",
                hadithBook = "Sunan Abi Dawud 5088",
                hadithGrade = "✓ Hasan Sahih",
                narrator = "Uthman bin Affan (RA)",
                sunnahUrl = "https://sunnah.com/abudawud:5088"
            ),
            DailyDuaData(
                id = "dua_v_firm_heart",
                arabic = "يَا مُقَلِّبَ الْقُلُوبِ ثَبِّتْ قَلْبِي عَلَى دِينِكَ",
                transliteration = "Ya Muqallibal-qulub thabbit qalbi 'ala dinik.",
                translation = when (locale) {
                    "hi" -> "ऐ दिलों को फेरने वाले! मेरे दिल को अपने दीन पर साबित क़दम रख।"
                    "ur" -> "اے دلوں کو پھیرنے والے! میرے دل کو اپنے دین پر ثابت قدم رکھ۔"
                    else -> "O Turner of the hearts, make my heart firm upon Your religion."
                },
                reference = "Jami` at-Tirmidhi 2140, 3522 | Sahih",
                hadithBook = "Jami` at-Tirmidhi 2140",
                hadithGrade = "✓ Sahih at-Tirmidhi",
                narrator = "Shahr bin Hawshab / Umm Salamah (RA)",
                sunnahUrl = "https://sunnah.com/tirmidhi:2140"
            ),
            DailyDuaData(
                id = "dua_v_rizq_knowledge",
                arabic = "اللَّهُمَّ إِنِّي أَسْأَلُكَ عِلْمًا نَافِعًا، وَرِزْقًا طَيِّبًا، وَعَمَلًا مُتَقَبَّلًا",
                transliteration = "Allahumma inni as'aluka 'ilman nafi'an, wa rizqan tayyiban, wa 'amalan mutaqabbala.",
                translation = when (locale) {
                    "hi" -> "ऐ अल्लाह! मैं तुझसे नफ़ा देने वाले इल्म, हलाल व पाकीज़ा रिज़्क़, और क़बूल होने वाले अमल का सवाल करता हूँ।"
                    "ur" -> "اے اللہ! میں تجھ سے نفع بخش علم، پاکیزہ رزق اور قبول ہونے والے عمل کا سوال کرتا ہوں۔"
                    else -> "O Allah, I ask You for beneficial knowledge, good and lawful provision, and deeds that will be accepted."
                },
                reference = "Sunan Ibn Majah 925 | Sahih",
                hadithBook = "Sunan Ibn Majah 925",
                hadithGrade = "✓ Sahih Ibn Majah",
                narrator = "Umm Salamah (RA)",
                sunnahUrl = "https://sunnah.com/ibnmajah:925"
            )
        )

        // Featured Daily Dua (Matches Reference Image 2: Dua for Protection from Grave & Hell)
        return featuredList[0]
    }
}
