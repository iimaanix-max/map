package com.tasbih.digitaltasbih.data.groups

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object GroupLocationsProvider {
    private var isLoaded = false
    private var popularCountries: List<String> = listOf("India", "Pakistan", "Saudi Arabia", "United Arab Emirates", "Bangladesh", "United Kingdom", "United States", "Canada")
    private var allCountries: List<String> = emptyList()
    private var statesCitiesMap: Map<String, Map<String, List<String>>> = emptyMap()

    fun init(context: Context) {
        if (isLoaded) return
        try {
            val jsonString = context.assets.open("group_locations.json").bufferedReader().use { it.readText() }
            val gson = Gson()
            val mapType = object : TypeToken<Map<String, Any>>() {}.type
            val root: Map<String, Any> = gson.fromJson(jsonString, mapType)

            @Suppress("UNCHECKED_CAST")
            popularCountries = (root["popular"] as? List<String>) ?: popularCountries
            @Suppress("UNCHECKED_CAST")
            allCountries = (root["countries"] as? List<String>) ?: popularCountries

            val scRaw = root["statesCities"]
            val scJson = gson.toJson(scRaw)
            val scType = object : TypeToken<Map<String, Map<String, List<String>>>>() {}.type
            statesCitiesMap = gson.fromJson(scJson, scType) ?: emptyMap()
            isLoaded = true
        } catch (_: Exception) {
            if (allCountries.isEmpty()) {
                allCountries = popularCountries
            }
        }
    }

    fun getCountries(): List<String> {
        val set = linkedSetOf<String>()
        set.addAll(popularCountries)
        set.addAll(allCountries)
        return set.toList()
    }

    fun getStates(country: String): List<String> {
        return statesCitiesMap[country]?.keys?.sorted() ?: emptyList()
    }

    fun getCities(country: String, state: String): List<String> {
        val countryMap = statesCitiesMap[country] ?: return emptyList()
        return countryMap[state]?.sorted() ?: emptyList()
    }
}
