package com.tasbih.digitaltasbih.data

data class DuaEntry(
    val id: String,
    val cat: String,
    val subcat: String = "",
    val allCats: List<String> = emptyList(),
    val ar: String = "",
    val lo: Map<String, String> = emptyMap(),
    val me: Map<String, String> = emptyMap(),
    val rf: Map<String, String> = emptyMap(),
    val title: Map<String, String> = emptyMap(),
    val tm: Map<String, String> = emptyMap(),
    val how: Map<String, String> = emptyMap(),
    val order: Int = 99
) {
    fun getLocalizedTitle(lang: String): String {
        val t = title[lang] ?: title["en"]
        if (!t.isNullOrBlank()) return t
        if (subcat.isNotBlank()) {
            return subcat.replace('_', ' ')
                .split(' ')
                .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
        }
        return cat.replaceFirstChar { it.uppercase() }
    }

    fun getLocalizedTransliteration(lang: String): String {
        return lo[lang] ?: lo["en"] ?: lo.values.firstOrNull() ?: ""
    }

    fun getLocalizedMeaning(lang: String): String {
        return me[lang] ?: me["en"] ?: me.values.firstOrNull() ?: ""
    }

    fun getLocalizedReference(lang: String): String {
        val r = rf[lang] ?: rf["en"] ?: rf.values.firstOrNull() ?: ""
        return if (r.startsWith("1 ")) r.substring(2) else r
    }

    fun getLocalizedWhen(lang: String): String {
        return tm[lang] ?: tm["en"] ?: tm.values.firstOrNull() ?: ""
    }

    fun getLocalizedHow(lang: String): String {
        return how[lang] ?: how["en"] ?: how.values.firstOrNull() ?: ""
    }
}

data class DuaCategoryItem(
    val id: String,
    val icon: String,
    val label: String,
    val count: Int = 0,
    val assetPath: String = ""
)

data class DuaFeelingItem(
    val id: String,
    val icon: String,
    val labelEn: String,
    val labelHi: String,
    val subLabel: String = "",
    val categoryId: String,
    val assetPath: String = "",
    val hiTriggers: List<String> = emptyList(),
    val enTriggers: List<String> = emptyList()
) {
    fun getLocalizedLabel(lang: String): String {
        return if (lang == "hi") labelHi else labelEn
    }
}

data class UserPersonalDua(
    val id: String,
    val title: String,
    val prayer: String,
    val arabic: String = "",
    val transliteration: String = "",
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

data class DuaCustomCollection(
    val id: String,
    val name: String,
    val duaIds: List<String> = emptyList()
)

data class QuickAccessDuaItem(
    val id: String,
    val title: String,
    val categoryId: String,
    val assetPath: String
)