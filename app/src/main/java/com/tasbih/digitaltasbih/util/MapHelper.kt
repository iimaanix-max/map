package com.tasbih.digitaltasbih.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.tasbih.digitaltasbih.ui.components.AppToast
import com.tasbih.digitaltasbih.data.PrayerRepository

object MapHelper {

    /**
     * Opens the device's Google Maps application (or default map / web browser)
     * and automatically searches for nearby mosques ("mosque near me").
     *
     * Matches the exact behavior from original index.html:
     * - Uses lat/lon from prayer settings if available
     * - Dispatches geo intent to Google Maps app first
     * - Gracefully falls back to web Google Maps if Maps app is absent
     */
    fun openNearbyMosques(context: Context) {
        try {
            val settings = try {
                PrayerRepository.loadSettings()
            } catch (_: Exception) {
                null
            }
            val lat = settings?.latitude ?: 0.0
            val lon = settings?.longitude ?: 0.0

            // Try explicit geo intent targeted to Google Maps application
            val geoUri = if (lat != 0.0 && lon != 0.0) {
                Uri.parse("geo:$lat,$lon?q=mosque")
            } else {
                Uri.parse("geo:0,0?q=mosque+near+me")
            }

            val mapIntent = Intent(Intent.ACTION_VIEW, geoUri).apply {
                setPackage("com.google.android.apps.maps")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }

            try {
                context.startActivity(mapIntent)
                return
            } catch (_: Exception) {
                // If package launch fails, try generic geo intent without package
            }

            val genericMapIntent = Intent(Intent.ACTION_VIEW, geoUri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            try {
                context.startActivity(genericMapIntent)
                return
            } catch (_: Exception) {
                // No geo handler found, fallback to web
            }

            // Fallback to Google Maps web search
            val webUrl = if (lat != 0.0 && lon != 0.0) {
                "https://www.google.com/maps/search/mosque/@$lat,$lon,14z"
            } else {
                "https://www.google.com/maps/search/mosque+near+me"
            }
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(webUrl)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(webIntent)

        } catch (e: Exception) {
            AppToast.show("Could not open Maps: ${e.localizedMessage}")
        }
    }

    /**
     * Opens Google Maps searching for Halal Food near the user's location.
     */
    fun openHalalFood(context: Context) {
        try {
            val settings = try {
                PrayerRepository.loadSettings()
            } catch (_: Exception) {
                null
            }
            val lat = settings?.latitude ?: 0.0
            val lon = settings?.longitude ?: 0.0

            val geoUri = if (lat != 0.0 && lon != 0.0) {
                Uri.parse("geo:$lat,$lon?q=halal+food")
            } else {
                Uri.parse("geo:0,0?q=halal+food+near+me")
            }

            val mapIntent = Intent(Intent.ACTION_VIEW, geoUri).apply {
                setPackage("com.google.android.apps.maps")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }

            try {
                context.startActivity(mapIntent)
                return
            } catch (_: Exception) {
                // package launch failed
            }

            val genericMapIntent = Intent(Intent.ACTION_VIEW, geoUri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(genericMapIntent)
        } catch (e: Exception) {
            val webUri = Uri.parse("https://www.google.com/maps/search/halal+food")
            val webIntent = Intent(Intent.ACTION_VIEW, webUri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(webIntent)
        }
    }

    /**
     * Opens the device's native YouTube application (or web browser)
     * and automatically searches for "makkah live kaaba" live streams.
     *
     * Matches homeOpenLiveKaaba from original index.html:
     * - Dispatches action view intent targeted to com.google.android.youtube
     * - Searches "makkah live kaaba"
     * - Falls back to browser if YouTube app is absent
     */
    fun openMakkahLive(context: Context) {
        try {
            val liveUrl = "https://www.youtube.com/results?search_query=makkah+live+kaaba"

            // 1. Try launching native YouTube application directly
            val ytIntent = Intent(Intent.ACTION_VIEW, Uri.parse(liveUrl)).apply {
                setPackage("com.google.android.youtube")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            try {
                context.startActivity(ytIntent)
                return
            } catch (_: Exception) {
                // YouTube app not installed or direct launch failed, proceed to fallback
            }

            // 2. Fallback to generic browser / default handler
            val genericIntent = Intent(Intent.ACTION_VIEW, Uri.parse(liveUrl)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(genericIntent)
        } catch (e: Exception) {
            AppToast.show("Could not open Makkah Live: ${e.localizedMessage}")
        }
    }
}
