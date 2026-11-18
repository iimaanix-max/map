package com.tasbih.digitaltasbih.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DuaPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("digital_tasbih_dua_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _selectedLanguage = MutableStateFlow(
        prefs.getString("selected_lang", null) ?: PrayerRepository.getLanguage()
    )
    val selectedLanguage: StateFlow<String> = _selectedLanguage

    private val _fontScale = MutableStateFlow(prefs.getFloat("font_scale", 1.0f))
    val fontScale: StateFlow<Float> = _fontScale

    private val _isHomeTourDone = MutableStateFlow(prefs.getBoolean("home_tour_done", false))
    val isHomeTourDone: StateFlow<Boolean> = _isHomeTourDone

    private val _isSaveTourDone = MutableStateFlow(prefs.getBoolean("save_tour_done", false))
    val isSaveTourDone: StateFlow<Boolean> = _isSaveTourDone

    private val _bookmarkedDuaIds = MutableStateFlow(loadBookmarks())
    val bookmarkedDuaIds: StateFlow<Set<String>> = _bookmarkedDuaIds

    private val _personalDuas = MutableStateFlow(loadPersonalDuasInternal())
    val personalDuas: StateFlow<List<UserPersonalDua>> = _personalDuas

    private val _customCollections = MutableStateFlow(loadCollectionsInternal())
    val customCollections: StateFlow<List<DuaCustomCollection>> = _customCollections

    private val _recentDuaIds = MutableStateFlow(loadRecentDuas())
    val recentDuaIds: StateFlow<List<String>> = _recentDuaIds

    fun setLanguage(lang: String) {
        prefs.edit().putString("selected_lang", lang).apply()
        _selectedLanguage.value = lang
        PrayerRepository.saveLanguage(lang)
    }

    fun setFontScale(scale: Float) {
        val clamped = scale.coerceIn(0.7f, 1.8f)
        prefs.edit().putFloat("font_scale", clamped).apply()
        _fontScale.value = clamped
    }

    fun markHomeTourDone() {
        prefs.edit().putBoolean("home_tour_done", true).apply()
        _isHomeTourDone.value = true
    }

    fun resetHomeTour() {
        prefs.edit().putBoolean("home_tour_done", false).apply()
        _isHomeTourDone.value = false
    }

    fun markSaveTourDone() {
        prefs.edit().putBoolean("save_tour_done", true).apply()
        _isSaveTourDone.value = true
    }

    private fun loadBookmarks(): Set<String> {
        return prefs.getStringSet("bookmarked_dua_ids", emptySet()) ?: emptySet()
    }

    fun toggleBookmark(duaId: String): Boolean {
        val current = loadBookmarks().toMutableSet()
        val newState = if (current.contains(duaId)) {
            current.remove(duaId)
            false
        } else {
            current.add(duaId)
            true
        }
        prefs.edit().putStringSet("bookmarked_dua_ids", current).apply()
        _bookmarkedDuaIds.value = current
        return newState
    }

    fun isBookmarked(duaId: String): Boolean {
        return _bookmarkedDuaIds.value.contains(duaId)
    }

    private fun loadPersonalDuasInternal(): List<UserPersonalDua> {
        val json = prefs.getString("personal_duas_json", null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<UserPersonalDua>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun savePersonalDua(dua: UserPersonalDua) {
        val current = _personalDuas.value.toMutableList()
        val index = current.indexOfFirst { it.id == dua.id }
        if (index >= 0) {
            current[index] = dua
        } else {
            current.add(0, dua)
        }
        val json = gson.toJson(current)
        prefs.edit().putString("personal_duas_json", json).apply()
        _personalDuas.value = current
    }

    fun deletePersonalDua(id: String) {
        val current = _personalDuas.value.filter { it.id != id }
        val json = gson.toJson(current)
        prefs.edit().putString("personal_duas_json", json).apply()
        _personalDuas.value = current
    }

    private fun loadCollectionsInternal(): List<DuaCustomCollection> {
        val json = prefs.getString("custom_collections_json", null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<DuaCustomCollection>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun createCollection(name: String): DuaCustomCollection {
        val current = _customCollections.value.toMutableList()
        val newCol = DuaCustomCollection(
            id = "col_${System.currentTimeMillis()}",
            name = name,
            duaIds = emptyList()
        )
        current.add(newCol)
        val json = gson.toJson(current)
        prefs.edit().putString("custom_collections_json", json).apply()
        _customCollections.value = current
        return newCol
    }

    fun toggleDuaInCollection(collectionId: String, duaId: String): Boolean {
        val current = _customCollections.value.toMutableList()
        val index = current.indexOfFirst { it.id == collectionId }
        if (index < 0) return false

        val col = current[index]
        val ids = col.duaIds.toMutableList()
        val isAdded = if (ids.contains(duaId)) {
            ids.remove(duaId)
            false
        } else {
            ids.add(duaId)
            true
        }
        current[index] = col.copy(duaIds = ids)
        val json = gson.toJson(current)
        prefs.edit().putString("custom_collections_json", json).apply()
        _customCollections.value = current
        return isAdded
    }

    fun deleteCollection(collectionId: String) {
        val current = _customCollections.value.filter { it.id != collectionId }
        val json = gson.toJson(current)
        prefs.edit().putString("custom_collections_json", json).apply()
        _customCollections.value = current
    }

    private fun loadRecentDuas(): List<String> {
        val json = prefs.getString("recent_dua_ids_json", null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<String>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun addRecentDua(duaId: String) {
        if (duaId.isBlank()) return
        val current = _recentDuaIds.value.toMutableList()
        current.remove(duaId)
        current.add(0, duaId)
        val trimmed = if (current.size > 30) current.take(30) else current
        val json = gson.toJson(trimmed)
        prefs.edit().putString("recent_dua_ids_json", json).apply()
        _recentDuaIds.value = trimmed
    }

    fun clearRecentDuas() {
        prefs.edit().remove("recent_dua_ids_json").apply()
        _recentDuaIds.value = emptyList()
    }
}