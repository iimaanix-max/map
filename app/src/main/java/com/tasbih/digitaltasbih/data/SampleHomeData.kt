package com.tasbih.digitaltasbih.data

object SampleHomeData {
    val languages = listOf(
        LanguageItem("en", "English"),
        LanguageItem("hi", "हिन्दी"),
        LanguageItem("ur", "اردو"),
        LanguageItem("ar", "العربية"),
        LanguageItem("bn", "বাংলা"),
        LanguageItem("tr", "Türkçe"),
        LanguageItem("id", "Bahasa")
    )

    val prayerTimes = listOf(
        HomePrayerItemSample("fajr", "Fajr", "05:08 AM", isPast = true, isActive = false),
        HomePrayerItemSample("sunrise", "Sunrise", "06:22 AM", isPast = true, isActive = false),
        HomePrayerItemSample("dhuhr", "Dhuhr", "12:45 PM", isPast = false, isActive = true),
        HomePrayerItemSample("asr", "Asr", "04:15 PM", isPast = false, isActive = false),
        HomePrayerItemSample("maghrib", "Maghrib", "06:05 PM", isPast = false, isActive = false),
        HomePrayerItemSample("isha", "Isha", "07:25 PM", isPast = false, isActive = false)
    )

    val featureTiles = listOf(
        FeatureTile("quran", "Noble Quran", "media/5fbfa4f9cb23d6b2.webp", isFeatured = true),
        FeatureTile("prayer", "Prayer Times", "media/bd512d13aad9c8fd.webp", isFeatured = true),
        FeatureTile("qibla", "Qibla Direction", "media/a623ce3fad98a23d.webp", isFeatured = true),
        FeatureTile("tasbih", "Dhikr Counter", "media/59329dcad16a9eac.webp", isFeatured = true),
        FeatureTile("groups", "Live Dhikr", "media/d919e45c30b26c4a.webp", isFeatured = true, isPulse = true),
        FeatureTile("tracker", "Salah Tracker", "media/cd777c476662f26f.webp", isFeatured = true),
        FeatureTile("calendar", "Islamic Calendar", "media/24b814cf08d6fe3c.webp", isFeatured = true),
        FeatureTile("azan", "Adhan Audio", "media/aea9d1273d13188c.webp", isFeatured = true),
        FeatureTile("dua", "Daily Duas", "media/20eccdc2c990c9c2.webp", isFeatured = true),
        FeatureTile("names99", "99 Names", "media/bc58ef05da0da4cb.webp", isFeatured = true),
        FeatureTile("hadith", "Hadith Sharif", "media/ba13638e21fd68a9.webp", isFeatured = true),
        FeatureTile("audio", "Quran Audio", "media/9588f2cd2da27fd8.webp", isFeatured = true),
        FeatureTile("morning_adhkar", "Morning Adhkar", "media/4b0b3782cfc16b4b.webp"),
        FeatureTile("evening_adhkar", "Evening Adhkar", "media/2ba4960e82735607.webp"),
        FeatureTile("daily", "Dhikr Tracker", "media/454ff52ffff9a617.webp"),
        FeatureTile("mosque", "Nearby Mosque", "media/2ac68dc80764ef75.webp"),
        FeatureTile("live", "Makkah Live", "media/ebd990316e7041a6.webp")
    )

    val featuredAllahName = AllahNameData(
        number = 31,
        arabic = "الْعَظِيمُ",
        transliteration = "Al-Azim",
        meaning = "The Most Great"
    )

    val upcomingEvents = listOf(
        IslamicEventData(
            id = "event_1",
            title = "Lailat al Miraj",
            hijriDate = "27 Rajab, 1448",
            gregorianDate = "05 Jan 2027",
            daysRemaining = 112
        ),
        IslamicEventData(
            id = "event_2",
            title = "Ramadan Mubarak",
            hijriDate = "1 Ramadan, 1448",
            gregorianDate = "08 Feb 2027",
            daysRemaining = 142
        )
    )

    val dailyDua = DailyDuaData(
        arabic = "اللَّهُمَّ إِنِّي أَسْأَلُكَ الْعَفْوَ وَالْعَافِيَةَ فِي الدُّنْيَا وَالآخِرَةِ",
        transliteration = "Allahumma inni as'aluka al-'afwa wal-'afiyata fid-dunya wal-akhirah",
        translation = "O Allah, I ask You for forgiveness and well-being in this life and the Next.",
        reference = "Sunan Ibn Majah 3871 • Sahih"
    )
}
