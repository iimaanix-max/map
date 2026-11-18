package com.tasbih.digitaltasbih.data

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import androidx.annotation.RawRes
import com.tasbih.digitaltasbih.R

data class AdhanChoice(
    val rawName: String,
    val titleEn: String,
    val titleHi: String,
    val titleUr: String,
    val reciterEn: String,
    val reciterHi: String,
    val countryFlag: String,
    @RawRes val audioRes: Int
) {
    fun getTitle(lang: String): String = when {
        lang.startsWith("hi") -> titleHi
        lang.startsWith("ur") -> titleUr
        else -> titleEn
    }

    fun getReciter(lang: String): String = when {
        lang.startsWith("hi") -> reciterHi
        else -> reciterEn
    }
}

object PrayerSoundManager {
    const val PREFS_NAME = "dt_prayer_prefs"
    const val KEY_SOUND_MODE = "prayer_sound_mode"
    const val KEY_SELECTED_ADHAN = "selected_adhan_raw"

    const val MODE_TAKBEER = "takbeer"
    const val MODE_ADHAN = "adhan"
    const val MODE_CHIME = "chime"
    const val MODE_TING = "ting"
    const val MODE_SILENT = "silent"

    val SACRED_ADHANS = listOf(
        AdhanChoice(
            rawName = "adhan_ahmad_al_nafees",
            titleEn = "Adhan by Sheikh Ahmad Al-Nafees",
            titleHi = "अज़ान - शेख अहमद अल-नफीस",
            titleUr = "اذان - شیخ احمد النفیس",
            reciterEn = "Sheikh Ahmad Al-Nafees",
            reciterHi = "शेख अहमद अल-नफीस",
            countryFlag = "🇰🇼",
            audioRes = R.raw.adhan_ahmad_al_nafees
        ),
        AdhanChoice(
            rawName = "adhan_hafiz_mustafa_ozcan",
            titleEn = "Adhan by Hafiz Mustafa Özcan",
            titleHi = "अज़ान - हाफ़िज़ मुस्तफ़ा ओज़कन",
            titleUr = "اذان - حافظ مصطفیٰ اوزجان",
            reciterEn = "Hafiz Mustafa Özcan",
            reciterHi = "हाफ़िज़ मुस्तफ़ा ओज़कन",
            countryFlag = "🇹🇷",
            audioRes = R.raw.adhan_hafiz_mustafa_ozcan
        ),
        AdhanChoice(
            rawName = "adhan_mansour_al_zahrani",
            titleEn = "Adhan by Sheikh Mansour Al-Zahrani",
            titleHi = "अज़ान - शेख मंसूर अल-ज़हरानी",
            titleUr = "اذان - شیخ منصور الزهرانی",
            reciterEn = "Sheikh Mansour Al-Zahrani",
            reciterHi = "शेख मंसूर अल-ज़हरानी",
            countryFlag = "🇸🇦",
            audioRes = R.raw.adhan_mansour_al_zahrani
        ),
        AdhanChoice(
            rawName = "adhan_mishary_dubai_tv",
            titleEn = "Adhan from Dubai TV (Mishary Alafasy)",
            titleHi = "अज़ान - दुबई वन टीवी (मिशारी अलाफ़ासी)",
            titleUr = "اذان دبئی ون ٹی وی - مشاری العفاسی",
            reciterEn = "Mishary Rashid Alafasy",
            reciterHi = "मिशारी राशिद अलाफ़ासी",
            countryFlag = "🇦🇪",
            audioRes = R.raw.adhan_mishary_dubai_tv
        ),
        AdhanChoice(
            rawName = "adhan_karl_jenkins_peace",
            titleEn = "Adhan from Karl Jenkins' Mass for Peace",
            titleHi = "अज़ान - कार्ल जेनकिंस मास फॉर पीस",
            titleUr = "اذان - कार्ल جینکنز ماس فار پیس",
            reciterEn = "Global Harmony Chorus",
            reciterHi = "ग्लोबल हार्मनी कोरस",
            countryFlag = "🕊️",
            audioRes = R.raw.adhan_karl_jenkins_peace
        ),
        AdhanChoice(
            rawName = "adhan_mishary_alafasy_2",
            titleEn = "Adhan by Mishary Alafasy (Style 2)",
            titleHi = "अज़ान - मिशारी राशिद अलाफ़ासी (शैली 2)",
            titleUr = "اذان - مشاری راشد العفاسی (انداز دوم)",
            reciterEn = "Mishary Rashid Alafasy",
            reciterHi = "मिशारी राशिद अलाफ़ासी",
            countryFlag = "🇰🇼",
            audioRes = R.raw.adhan_mishary_alafasy_2
        ),
        AdhanChoice(
            rawName = "adhan_mishary_alafasy_3",
            titleEn = "Fajr Adhan by Mishary Rashid Alafasy",
            titleHi = "फ़ज्र अज़ान - मिशारी राशिद अलाफ़ासी",
            titleUr = "فجر اذان - مشاری راشد العفاسی",
            reciterEn = "Mishary Rashid Alafasy",
            reciterHi = "मिशारी राशिद अलाफ़ासी",
            countryFlag = "🌅",
            audioRes = R.raw.adhan_mishary_alafasy_3
        )
    )

    fun mapAdhanToRawName(idOrRaw: String): String = when (idOrRaw) {
        "adhan_sheikh_mansour_al_zahrani", "adhan_mansour_al_zahrani" -> "adhan_mansour_al_zahrani"
        "adhan_mishary_dubai_one_tv", "adhan_mishary_dubai_tv" -> "adhan_mishary_dubai_tv"
        "adhan_karl_jenkins_mass_for_peace", "adhan_karl_jenkins_peace" -> "adhan_karl_jenkins_peace"
        "adhan_mishary_alafasy_style_2", "adhan_mishary_alafasy_2" -> "adhan_mishary_alafasy_2"
        "adhan_fajr_mishary_alafasy", "adhan_mishary_alafasy_3" -> "adhan_mishary_alafasy_3"
        "adhan_hafiz_mustafa_ozcan" -> "adhan_hafiz_mustafa_ozcan"
        "adhan_ahmad_al_nafees" -> "adhan_ahmad_al_nafees"
        else -> {
            val matched = SACRED_ADHANS.firstOrNull { it.rawName == idOrRaw }
            matched?.rawName ?: "adhan_ahmad_al_nafees"
        }
    }

    fun getSoundMode(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_SOUND_MODE, MODE_ADHAN) ?: MODE_ADHAN
    }

    fun setSoundMode(context: Context, mode: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_SOUND_MODE, mode).apply()
    }

    fun getSelectedAdhanRaw(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val raw = prefs.getString(KEY_SELECTED_ADHAN, "adhan_ahmad_al_nafees") ?: "adhan_ahmad_al_nafees"
        return mapAdhanToRawName(raw)
    }

    fun setSelectedAdhanRaw(context: Context, rawOrId: String) {
        val mappedRaw = mapAdhanToRawName(rawOrId)
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_SOUND_MODE, MODE_ADHAN)
            .putString(KEY_SELECTED_ADHAN, mappedRaw)
            .apply()
    }

    fun getSoundUri(context: Context, isWarning: Boolean = false): Uri? {
        if (isWarning) {
            return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }
        val mode = getSoundMode(context)
        return when (mode) {
            MODE_SILENT -> null
            MODE_TAKBEER -> {
                val resId = R.raw.prayer_takbeer_nafees
                Uri.parse("android.resource://${context.packageName}/$resId")
            }
            MODE_CHIME -> {
                val resId = R.raw.prayer_chime_hijaz
                Uri.parse("android.resource://${context.packageName}/$resId")
            }
            MODE_TING -> RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            MODE_ADHAN -> {
                val rawName = getSelectedAdhanRaw(context)
                val directResId = SACRED_ADHANS.firstOrNull { it.rawName == rawName }?.audioRes
                val soundResId = directResId ?: context.resources.getIdentifier(rawName, "raw", context.packageName)
                if (soundResId != 0) {
                    Uri.parse("android.resource://${context.packageName}/$soundResId")
                } else {
                    Uri.parse("android.resource://${context.packageName}/${R.raw.adhan_ahmad_al_nafees}")
                }
            }
            else -> {
                val rawName = getSelectedAdhanRaw(context)
                val directResId = SACRED_ADHANS.firstOrNull { it.rawName == rawName }?.audioRes
                val soundResId = directResId ?: context.resources.getIdentifier(rawName, "raw", context.packageName)
                if (soundResId != 0) {
                    Uri.parse("android.resource://${context.packageName}/$soundResId")
                } else {
                    Uri.parse("android.resource://${context.packageName}/${R.raw.adhan_ahmad_al_nafees}")
                }
            }
        }
    }

    fun getChannelId(context: Context, isWarning: Boolean = false): String {
        if (isWarning) return "digital_tasbih_prayer_warning_channel_v2"
        val mode = getSoundMode(context)
        return when (mode) {
            MODE_SILENT -> "dt_salah_channel_silent_v2"
            MODE_TAKBEER -> "dt_salah_channel_takbeer_nafees_v2"
            MODE_CHIME -> "dt_salah_channel_hijaz_chime_v2"
            MODE_TING -> "dt_salah_channel_ting_v2"
            else -> {
                val raw = getSelectedAdhanRaw(context)
                "dt_salah_channel_adhan_${raw}_v2"
            }
        }
    }

    fun getSoundDisplayName(context: Context, lang: String): String {
        val mode = getSoundMode(context)
        return when (mode) {
            MODE_TAKBEER -> when {
                lang.startsWith("hi") -> "पवित्र तकबीर (शेख अहमद अल-नफीस • 8s)"
                lang.startsWith("ur") -> "مقدس تکبیر (شیخ احمد النفیس • 8 سیکنڈ)"
                lang.startsWith("ar") -> "التكبير الشريف (الشيخ أحمد النفيس • 8 ث)"
                lang.startsWith("hinglish") -> "Short Sacred Takbeer (8s) 🕌"
                else -> "Short Sacred Takbeer (8s) 🕌"
            }
            MODE_ADHAN -> {
                val raw = getSelectedAdhanRaw(context)
                val adhan = SACRED_ADHANS.firstOrNull { it.rawName == raw } ?: SACRED_ADHANS.first()
                "🕌 ${adhan.getTitle(lang)}"
            }
            MODE_CHIME -> when {
                lang.startsWith("hi") -> "हिजाज़ चाइम (पवित्र मधुर धुन)"
                lang.startsWith("ur") -> "حجاز چائم (مدھر اور پرسکون دھن)"
                lang.startsWith("ar") -> "نغمة الحجاز الهادئة"
                lang.startsWith("hinglish") -> "Sacred Hijaz Chime (Gentle Melody)"
                else -> "Sacred Hijaz Chime (Gentle Melody)"
            }
            MODE_TING -> when {
                lang.startsWith("hi") -> "नॉर्मल टिंग (डिफ़ॉल्ट टोन)"
                lang.startsWith("ur") -> "نارمل ٹنگ (ڈیفالٹ टोन)"
                lang.startsWith("hinglish") -> "Normal Ting (Default Tone)"
                else -> "Normal Ting (Default Tone)"
            }
            MODE_SILENT -> when {
                lang.startsWith("hi") -> "केवल वाइब्रेशन (म्यूट)"
                lang.startsWith("ur") -> "صرف वائبریشن (خاموش)"
                lang.startsWith("hinglish") -> "Vibration Only (Silent)"
                else -> "Vibration Only (Silent)"
            }
            else -> {
                val raw = getSelectedAdhanRaw(context)
                val adhan = SACRED_ADHANS.firstOrNull { it.rawName == raw } ?: SACRED_ADHANS.first()
                "🕌 ${adhan.getTitle(lang)}"
            }
        }
    }
}
