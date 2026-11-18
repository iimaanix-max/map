package com.tasbih.digitaltasbih.data

import androidx.compose.ui.graphics.Color
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Authentic Islamic Calendar Event model.
 * Matches all events in original index.html and admin.html (/calendar_events).
 */
data class CalendarEvent(
    val id: String,
    val m: Int, // Hijri month (1-12)
    val d: Int, // Hijri day (1-30)
    val kind: String = "urs", // "urs", "festival", "death", "shahadat", "birth"
    val namePack: Map<String, String>? = null,
    val defaultName: String = "",
    val titlePack: Map<String, String>? = null,
    val defaultTitle: String? = null,
    val shortPack: Map<String, String>? = null,
    val defaultShort: String? = null,
    val color: String = "#0E5D47",
    val locationPack: Map<String, String>? = null,
    val defaultLocation: String = "",
    val briefPack: Map<String, String>? = null,
    val defaultBrief: String = "",
    val ursCount: String = "",
    val annivPack: Map<String, String>? = null,
    val defaultAnniv: String = "",
    val image: String = "",
    val hy: Int? = null,
    val active: Boolean = true
) {
    fun getTitle(locale: String): String {
        titlePack?.get(locale)?.takeIf { it.isNotBlank() }?.let { return it }
        if (locale in listOf("mr", "gu", "pa", "or", "as")) {
            titlePack?.get("hi")?.takeIf { it.isNotBlank() }?.let { return it }
        }
        if (locale in listOf("fa")) {
            (titlePack?.get("ur") ?: titlePack?.get("ar"))?.takeIf { it.isNotBlank() }?.let { return it }
        }
        val locName = CalendarLocalization.getEventName(id, defaultTitle ?: defaultName, locale)
        if (locName.isNotBlank() && locName != (defaultTitle ?: defaultName)) return locName
        titlePack?.get("en")?.takeIf { it.isNotBlank() }?.let { return it }
        defaultTitle?.takeIf { it.isNotBlank() }?.let { return it }
        return getName(locale)
    }

    fun getName(locale: String): String {
        namePack?.get(locale)?.takeIf { it.isNotBlank() }?.let { return it }
        if (locale in listOf("mr", "gu", "pa", "or", "as")) {
            namePack?.get("hi")?.takeIf { it.isNotBlank() }?.let { return it }
        }
        if (locale in listOf("fa")) {
            (namePack?.get("ur") ?: namePack?.get("ar"))?.takeIf { it.isNotBlank() }?.let { return it }
        }
        val locName = CalendarLocalization.getEventName(id, defaultName, locale)
        if (locName.isNotBlank() && locName != defaultName) return locName
        namePack?.get("en")?.takeIf { it.isNotBlank() }?.let { return it }
        return locName.ifBlank { defaultName.ifBlank { id } }
    }

    fun getShort(locale: String): String {
        shortPack?.get(locale)?.takeIf { it.isNotBlank() }?.let { return it }
        if (locale in listOf("mr", "gu", "pa", "or", "as")) {
            shortPack?.get("hi")?.takeIf { it.isNotBlank() }?.let { return it }
        }
        if (locale in listOf("fa")) {
            (shortPack?.get("ur") ?: shortPack?.get("ar"))?.takeIf { it.isNotBlank() }?.let { return it }
        }
        val locShort = CalendarLocalization.getEventShort(id, defaultShort ?: defaultName, locale)
        if (locShort.isNotBlank()) return locShort
        shortPack?.get("en")?.takeIf { it.isNotBlank() }?.let { return it }
        defaultShort?.takeIf { it.isNotBlank() }?.let { return it }
        val name = getName(locale)
        return if (name.length > 10) name.take(9) + "…" else name
    }

    fun getLocation(locale: String): String {
        locationPack?.get(locale)?.takeIf { it.isNotBlank() }?.let { return it }
        if (locale in listOf("mr", "gu", "pa", "or", "as")) {
            locationPack?.get("hi")?.takeIf { it.isNotBlank() }?.let { return it }
        }
        if (locale in listOf("fa")) {
            (locationPack?.get("ur") ?: locationPack?.get("ar"))?.takeIf { it.isNotBlank() }?.let { return it }
        }
        val loc = CalendarLocalization.getEventLocation(defaultLocation, locale)
        if (loc.isNotBlank()) return loc
        locationPack?.get("en")?.takeIf { it.isNotBlank() }?.let { return it }
        return defaultLocation
    }

    fun getBrief(locale: String): String {
        briefPack?.get(locale)?.takeIf { it.isNotBlank() }?.let { return it }
        if (locale in listOf("mr", "gu", "pa", "or", "as")) {
            briefPack?.get("hi")?.takeIf { it.isNotBlank() }?.let { return it }
        }
        if (locale in listOf("fa")) {
            (briefPack?.get("ur") ?: briefPack?.get("ar"))?.takeIf { it.isNotBlank() }?.let { return it }
        }
        briefPack?.get("en")?.takeIf { it.isNotBlank() }?.let { return it }
        return defaultBrief
    }

    fun getAnniv(locale: String): String {
        annivPack?.get(locale)?.takeIf { it.isNotBlank() }?.let { return it }
        val raw = if (ursCount.isNotBlank()) ursCount else defaultAnniv
        if (raw.isNotBlank()) {
            return CalendarLocalization.formatAnniversary(raw, locale)
        }
        return ""
    }

    fun getKindBadge(locale: String): String {
        return when (kind.lowercase(Locale.ROOT)) {
            "urs" -> when (locale) {
                "hi", "mr" -> "उर्स"
                "gu" -> "ઉર્સ"
                "pa" -> "ਉਰਸ"
                "bn", "as" -> "উরস"
                "or" -> "ଉରସ"
                "ur", "fa" -> "عرس"
                "ar" -> "عرس"
                "ta" -> "உர்ஸ்"
                "te" -> "ఉర్స్"
                "kn" -> "ಉರ್ಸ್"
                "ml" -> "ഉറൂസ്"
                "tr" -> "URUS"
                "id", "ms" -> "HAUL"
                else -> "URS"
            }
            "festival" -> when (locale) {
                "hi", "mr" -> "त्योहार"
                "gu" -> "તહેવાર"
                "pa" -> "ਤਿਉਹਾਰ"
                "bn", "as" -> "উৎসব"
                "or" -> "ଉତ୍ସବ"
                "ur", "fa" -> "عید / تہوار"
                "ar" -> "مناسبة"
                "ta" -> "பண்டிகை"
                "te" -> "పండుగ"
                "kn" -> "ಹಬ್ಬ"
                "ml" -> "ഉത്സവം"
                "tr" -> "BAYRAM"
                "id", "ms" -> "HARI RAYA"
                else -> "FESTIVAL"
            }
            "death" -> when (locale) {
                "hi", "mr" -> "विसाल"
                "gu" -> "વિસાલ"
                "pa" -> "ਵਿਸਾਲ"
                "bn", "as" -> "ওফাত"
                "or" -> "ଓଫାତ"
                "ur", "fa" -> "وصال"
                "ar" -> "وفاة"
                "ta" -> "வபாத்"
                "te" -> "వఫాత్"
                "kn" -> "ವಫಾತ್"
                "ml" -> "വഫാത്ത്"
                else -> "WISAAL"
            }
            "shahadat" -> when (locale) {
                "hi", "mr" -> "शहादत"
                "gu" -> "શહાદત"
                "pa" -> "ਸ਼ਹਾਦਤ"
                "bn", "as" -> "শাহাদাত"
                "or" -> "ଶାହାଦତ"
                "ur", "fa" -> "شہادت"
                "ar" -> "شهادة"
                "ta" -> "ஷஹாதத்"
                "te" -> "షహాదత్"
                "kn" -> "ಶಹಾದತ್"
                "ml" -> "ഷഹാദത്ത്"
                else -> "SHAHADAT"
            }
            "birth" -> when (locale) {
                "hi", "mr" -> "विलादत"
                "gu" -> "વિલાદત"
                "pa" -> "ਵਿਲਾਦਤ"
                "bn", "as" -> "বেলাদত"
                "or" -> "ବିଲାଦତ"
                "ur", "fa" -> "ولادت"
                "ar" -> "مولد"
                "ta" -> "விலாதத்"
                "te" -> "విలాదత్"
                "kn" -> "ವಿಲಾದತ್"
                "ml" -> "വിലാദത്ത്"
                else -> "MILAD"
            }
            else -> kind.uppercase(Locale.ROOT)
        }
    }

    fun getColorParsed(): Color {
        if (color.isBlank()) return Color(0xFF0E5D47)
        return try {
            val hex = color.trim().removePrefix("#")
            when (hex.length) {
                6 -> Color(android.graphics.Color.parseColor("#$hex"))
                8 -> Color(android.graphics.Color.parseColor("#$hex"))
                3 -> {
                    val r = hex[0]; val g = hex[1]; val b = hex[2]
                    Color(android.graphics.Color.parseColor("#$r$r$g$g$b$b"))
                }
                else -> Color(0xFF0E5D47)
            }
        } catch (_: Exception) {
            Color(0xFF0E5D47)
        }
    }
}

/**
 * Represents a date in the Hijri calendar.
 */
data class HijriDate(
    val day: Int,
    val monthNumber: Int,
    val year: Int,
    val monthName: String,
    val isSouthAsiaMoonVerified: Boolean = false
) {
    fun formatDisplay(suffix: String = "AH"): String {
        return "$day $monthName $year $suffix"
    }
}

/**
 * Data model for a single day cell in the 7-column month grid.
 */
data class CalendarDayCell(
    val gregorianYear: Int,
    val gregorianMonth: Int, // 0-based
    val gregorianDay: Int,
    val isCurrentMonth: Boolean,
    val isToday: Boolean,
    val isSelected: Boolean,
    val hijriDate: HijriDate,
    val events: List<CalendarEvent>
) {
    val isoDate: String = String.format(Locale.US, "%04d-%02d-%02d", gregorianYear, gregorianMonth + 1, gregorianDay)
}

/**
 * Month overview item for the 2-column Year View grid.
 */
data class MonthYearOverviewItem(
    val year: Int,
    val monthIndex: Int, // 0-11
    val shortName: String,
    val fullName: String,
    val hijriRange: String,
    val eventCount: Int,
    val isActiveMonth: Boolean,
    val isCurrentMonth: Boolean
)

/**
 * Complete multilingual localization strings for the Islamic Calendar.
 */
data class CalendarScreenStrings(
    val calTitle: String,
    val calSubtitle: String,
    val calToday: String,
    val calSelected: String,
    val calGreg: String,
    val calHijri: String,
    val calYearView: String,
    val calEvent: String,
    val calEventNone: String,
    val calEventMore: String,
    val calMonthEvents: String,
    val calHijriSuffix: String,
    val calSearchPlaceholder: String,
    val calLocation: String,
    val calEventsOnDate: String,
    val calSearchEmpty: String = "No matching Islamic events found."
)

object CalendarLocalization {
    fun get(locale: String): CalendarScreenStrings {
        return when (locale) {
            "hi" -> CalendarScreenStrings(
                calTitle = "इस्लामिक कैलेंडर",
                calSubtitle = "मौजूदा महीना, हिजरी तारीखें, अहम इस्लामी वाक़ियात और सालाना नज़ारा।",
                calToday = "आज",
                calSelected = "चयनित तारीख",
                calGreg = "ईस्वी",
                calHijri = "हिजरी",
                calYearView = "सालाना नज़ारा",
                calEvent = "आयोजन",
                calEventNone = "इस तारीख के लिए कोई बड़ा इस्लामी आयोजन दर्ज नहीं है।",
                calEventMore = "उसका आयोजन देखने के लिए किसी दूसरी हाइलाइट की गई तारीख पर टैप करें।",
                calMonthEvents = "आयोजन",
                calHijriSuffix = "हि.",
                calSearchPlaceholder = "आयोजन खोजें...",
                calLocation = "स्थान",
                calEventsOnDate = "इस तारीख के आयोजन",
                calSearchEmpty = "कोई मिलता-जुलता इस्लामी आयोजन नहीं मिला।"
            )
            "ur" -> CalendarScreenStrings(
                calTitle = "اسلامی کیلنڈر",
                calSubtitle = "موجودہ مہینہ، ہجری تاریخیں، اہم اسلامی واقعات اور سالانہ جائزہ۔",
                calToday = "آج",
                calSelected = "منتخب تاریخ",
                calGreg = "عیسوی",
                calHijri = "ہجری",
                calYearView = "سالانہ جائزہ",
                calEvent = "واقعہ",
                calEventNone = "اس تاریخ پر کوئی بڑا اسلامی واقعہ درج نہیں ہے۔",
                calEventMore = "کسی ہائی لائٹ شدہ تاریخ پر کلک کر کے اس کے واقعات پڑھیں۔",
                calMonthEvents = "واقعات",
                calHijriSuffix = "ھ",
                calSearchPlaceholder = "واقعات تلاش کریں...",
                calLocation = "مقام",
                calEventsOnDate = "اس تاریخ کے واقعات",
                calSearchEmpty = "کوئی مناسب اسلامی واقعہ نہیں ملا۔"
            )
            "ar" -> CalendarScreenStrings(
                calTitle = "التقويم الإسلامي",
                calSubtitle = "الشهر الحالي، التواريخ الهجرية، المناسبات الإسلامية ونظرة عامة على السنة.",
                calToday = "اليوم",
                calSelected = "التاريخ المحدد",
                calGreg = "الميلادي",
                calHijri = "الهجري",
                calYearView = "عرض السنة",
                calEvent = "المناسبة",
                calEventNone = "لا توجد مناسبة إسلامية كبرى مسجلة في هذا التاريخ.",
                calEventMore = "اضغط على تاريخ محدد آخر لقراءة مناسبته.",
                calMonthEvents = "أحداث",
                calHijriSuffix = "هـ",
                calSearchPlaceholder = "ابحث عن الأحداث...",
                calLocation = "الموقع",
                calEventsOnDate = "أحداث هذا اليوم",
                calSearchEmpty = "لم يتم العثور على مناسبات مطابقة."
            )
            "bn" -> CalendarScreenStrings(
                calTitle = "ইসলামিক ক্যালেন্ডার",
                calSubtitle = "চলতি মাস, হিজরি তারিখ, প্রধান ইসলামিক ঘটনাবলি এবং বার্ষিক বিবরণ।",
                calToday = "আজ",
                calSelected = "নির্বাচিত তারিখ",
                calGreg = "ইংরেজি",
                calHijri = "হিজরি",
                calYearView = "বার্ষিক দৃশ্য",
                calEvent = "ইভেন্ট",
                calEventNone = "এই তারিখে কোনো বড় ইসলামিক ইভেন্ট লিপিবদ্ধ নেই।",
                calEventMore = "অন্য কোনো চিহ্নিত তারিখে ট্যাপ করে ইভেন্ট দেখুন।",
                calMonthEvents = "ইভেন্ট",
                calHijriSuffix = "হিজরি",
                calSearchPlaceholder = "ইভেন্ট অনুসন্ধান করুন...",
                calLocation = "স্থান",
                calEventsOnDate = "এই তারিখের ঘটনাবলি",
                calSearchEmpty = "কোনো ইসলামিক ইভেন্ট পাওয়া যায়নি।"
            )
            "gu" -> CalendarScreenStrings(
                calTitle = "ઇસ્લામિક કેલેન્ડર",
                calSubtitle = "ચાલુ મહિનો, હિજરી તારીખો, મહત્વપૂર્ણ ઇસ્લામિક ઘટનાઓ અને વાર્ષિક ઝાંખી.",
                calToday = "આજે",
                calSelected = "પસંદ કરેલ તારીખ",
                calGreg = "ઇસવી",
                calHijri = "હિજરી",
                calYearView = "વાર્ષિક ઝાંખી",
                calEvent = "ઘટના",
                calEventNone = "આ તારીખે કોઈ મોટી ઇસ્લામિક ઘટના નોંધાયેલ નથી.",
                calEventMore = "તેની ઘટના જોવા માટે બીજી હાઇલાઇટ કરેલી તારીખ પર ટેપ કરો.",
                calMonthEvents = "ઘટનાઓ",
                calHijriSuffix = "હિજરી",
                calSearchPlaceholder = "ઘટનાઓ શોધો...",
                calLocation = "સ્થળ",
                calEventsOnDate = "આ તારીખની ઘટનાઓ",
                calSearchEmpty = "કોઈ મેળ ખાતી ઇસ્લામિક ઘટના મળી નથી."
            )
            "mr" -> CalendarScreenStrings(
                calTitle = "इस्लामिक कॅलेंडर",
                calSubtitle = "चालू महिना, हिजरी तारखा, महत्त्वाच्या इस्लामिक घटना आणि वार्षिक आढावा.",
                calToday = "आज",
                calSelected = "निवडलेली तारीख",
                calGreg = "ईस्वी",
                calHijri = "हिजरी",
                calYearView = "वार्षिक दृश्य",
                calEvent = "कार्यक्रम",
                calEventNone = "या तारखेला कोणतीही मोठी इस्लामिक घटना नोंदवलेली नाही.",
                calEventMore = "इतर हायलाइट केलेल्या तारखेवर टॅप करून घटना पहा.",
                calMonthEvents = "कार्यक्रम",
                calHijriSuffix = "हि.",
                calSearchPlaceholder = "कार्यक्रम शोधा...",
                calLocation = "स्थान",
                calEventsOnDate = "या तारखेचे कार्यक्रम",
                calSearchEmpty = "कोणतीही जुळणारी इस्लामिक घटना आढळली नाही."
            )
            "ta" -> CalendarScreenStrings(
                calTitle = "இஸ்லாமிய நாட்காட்டி",
                calSubtitle = "நடப்பு மாதம், ஹிஜ்ரி தேதிகள், முக்கிய நிகழ்வுகள் மற்றும் ஆண்டு பார்வை.",
                calToday = "இன்று",
                calSelected = "தேர்ந்தெடுக்கப்பட்ட தேதி",
                calGreg = "கிரிகோரியன்",
                calHijri = "ஹிஜ்ரி",
                calYearView = "ஆண்டு பார்வை",
                calEvent = "நிகழ்வு",
                calEventNone = "இந்த தேதியில் பெரிய இஸ்லாமிய நிகழ்வுகள் எதுவும் இல்லை.",
                calEventMore = "நிகழ்வைப் பார்க்க மற்றொரு ஹைலைட் செய்யப்பட்ட தேதியைத் தட்டவும்.",
                calMonthEvents = "நிகழ்வுகள்",
                calHijriSuffix = "ஹிஜ்ரி",
                calSearchPlaceholder = "நிகழ்வுகளைத் தேடுங்கள்...",
                calLocation = "இடம்",
                calEventsOnDate = "இந்த தேதியின் நிகழ்வுகள்",
                calSearchEmpty = "பொருந்தும் இஸ்லாமிய நிகழ்வுகள் எதுவும் கிடைக்கவில்லை."
            )
            "te" -> CalendarScreenStrings(
                calTitle = "ఇస్లామిక్ క్యాలెండర్",
                calSubtitle = "ప్రస్తుత నెల, హిజ్రీ తేదీలు, ప్రధాన ఇస్లామిక్ సంఘటనలు మరియు వార్షిక సమీక్ష.",
                calToday = "ఈ రోజు",
                calSelected = "ఎంచుకున్న తేదీ",
                calGreg = "గ్రెగోరియన్",
                calHijri = "హిజ్రీ",
                calYearView = "సంవత్సర వీక్షణ",
                calEvent = "సంఘటన",
                calEventNone = "ఈ తేదీకి ఎటువంటి ప్రధాన ఇస్లామిక్ సంఘటనలు నమోదు కాలేదు.",
                calEventMore = "సంఘటన చూడటానికి మరో హైలైట్ చేసిన తేదీని నొక్కండి.",
                calMonthEvents = "సంఘటనలు",
                calHijriSuffix = "హిజ్రీ",
                calSearchPlaceholder = "సంఘటనలను శోధించండి...",
                calLocation = "స్థలం",
                calEventsOnDate = "ఈ తేదీలోని సంఘటనలు",
                calSearchEmpty = "సరిపోలే ఇస్లామిక్ సంఘటనలు కనుగొనబడలేదు."
            )
            "ml" -> CalendarScreenStrings(
                calTitle = "ഇസ്ലാമിക് കലണ്ടർ",
                calSubtitle = "നിലവിലെ മാസം, ഹിജ്‌രി തീയതികൾ, പ്രധാന ഇസ്ലാമിക സംഭവങ്ങൾ, വാർഷിക കാഴ്ച.",
                calToday = "ഇന്ന്",
                calSelected = "തിരഞ്ഞെടുത്ത തീയതി",
                calGreg = "ഗ്രിഗോറിയൻ",
                calHijri = "ഹിജ്‌രി",
                calYearView = "വർഷ കാഴ്ച",
                calEvent = "ഇവന്റ്",
                calEventNone = "ഈ തീയതിയിൽ പ്രധാന ഇസ്ലാമിക സംഭവങ്ങളൊന്നും രേഖപ്പെടുത്തിയിട്ടില്ല.",
                calEventMore = "ഇവന്റ് കാണാൻ മറ്റൊരു ഹൈലൈറ്റ് ചെയ്ത തീയതിയിൽ ടാപ്പ് ചെയ്യുക.",
                calMonthEvents = "ഇവന്റുകൾ",
                calHijriSuffix = "ഹിജ്‌രി",
                calSearchPlaceholder = "ഇവന്റുകൾ തിരയുക...",
                calLocation = "സ്ഥലം",
                calEventsOnDate = "ഈ തീയതിയിലെ ഇവന്റുകൾ",
                calSearchEmpty = "യോജിച്ച ഇസ്ലാമിക ഇവന്റുകൾ കണ്ടെത്താനായില്ല."
            )
            "kn" -> CalendarScreenStrings(
                calTitle = "ಇಸ್ಲಾಮಿಕ್ ಕ್ಯಾಲೆಂಡರ್",
                calSubtitle = "ಪ್ರಸ್ತುತ ತಿಂಗಳು, ಹಿಜ್ರಿ ದಿನಾಂಕಗಳು, ಪ್ರಮುಖ ಇಸ್ಲಾಮಿಕ್ ಘಟನೆಗಳು ಮತ್ತು ವಾರ್ಷಿಕ ಅವಲೋಕನ.",
                calToday = "ಇಂದು",
                calSelected = "ಆಯ್ದ ದಿನಾಂಕ",
                calGreg = "ಗ್ರೆಗೋರಿಯನ್",
                calHijri = "ಹಿಜ್ರಿ",
                calYearView = "ವರ್ಷದ ನೋಟ",
                calEvent = "ಘಟನೆ",
                calEventNone = "ಈ ದಿನಾಂಕಕ್ಕೆ ಯಾವುದೇ ಪ್ರಮುಖ ಇಸ್ಲಾಮಿಕ್ ಘಟನೆ ಗುರುತಿಸಲಾಗಿಲ್ಲ.",
                calEventMore = "ಘಟನೆ ಓದಲು ಇನ್ನೊಂದು ಹೈಲೈಟ್ ಮಾಡಿದ ದಿನಾಂಕವನ್ನು ಟ್ಯಾಪ್ ಮಾಡಿ.",
                calMonthEvents = "ಘಟನೆಗಳು",
                calHijriSuffix = "ಹಿಜ್ರಿ",
                calSearchPlaceholder = "ಘಟನೆಗಳನ್ನು ಹುಡುಕಿ...",
                calLocation = "ಸ್ಥಳ",
                calEventsOnDate = "ಈ ದಿನಾಂಕದ ಘಟನೆಗಳು",
                calSearchEmpty = "ಹೊಂದಾಣಿಕೆಯ ಇಸ್ಲಾಮಿಕ್ ಘಟನೆಗಳು ಕಂಡುಬಂದಿಲ್ಲ."
            )
            "pa" -> CalendarScreenStrings(
                calTitle = "ਇਸਲਾਮਿਕ ਕੈਲੰਡਰ",
                calSubtitle = "ਮੌਜੂਦਾ ਮਹੀਨਾ, ਹਿਜਰੀ ਤਾਰੀਖਾਂ, ਅਹਿਮ ਇਸਲਾਮੀ ਵਾਕਿਆਤ ਅਤੇ ਸਾਲਾਨਾ ਜਾਇਜ਼ਾ।",
                calToday = "ਅੱਜ",
                calSelected = "ਚੁਣੀ ਹੋਈ ਤਾਰੀਖ",
                calGreg = "ਈਸਵੀ",
                calHijri = "ਹਿਜਰੀ",
                calYearView = "ਸਾਲਾਨਾ ਦ੍ਰਿਸ਼",
                calEvent = "ਵਾਕਿਆ",
                calEventNone = "ਇਸ ਤਾਰੀਖ ਲਈ ਕੋਈ ਵੱਡਾ ਇਸਲਾਮੀ ਵਾਕਿਆ ਦਰਜ ਨਹੀਂ ਹੈ।",
                calEventMore = "ਇਸਦਾ ਵਾਕਿਆ ਦੇਖਣ ਲਈ ਕਿਸੇ ਦੂਜੀ ਹਾਈਲਾਈਟ ਕੀਤੀ ਤਾਰੀਖ 'ਤੇ ਟੈਪ ਕਰੋ।",
                calMonthEvents = "ਵਾਕਿਆਤ",
                calHijriSuffix = "ਹਿਜਰੀ",
                calSearchPlaceholder = "ਵਾਕਿਆਤ ਖੋਜੋ...",
                calLocation = "ਸਥਾਨ",
                calEventsOnDate = "ਇਸ ਤਾਰੀਖ ਦੇ ਵਾਕਿਆਤ",
                calSearchEmpty = "ਕੋਈ ਮੇਲ ਖਾਂਦਾ ਇਸਲਾਮੀ ਵਾਕਿਆ ਨਹੀਂ ਮਿਲਿਆ।"
            )
            "tr" -> CalendarScreenStrings(
                calTitle = "İslami Takvim",
                calSubtitle = "Mevcut ay, Hicri tarihler, önemli İslami olaylar ve yıllık genel bakış.",
                calToday = "Bugün",
                calSelected = "SEÇİLEN TARİH",
                calGreg = "Miladi",
                calHijri = "Hicri",
                calYearView = "Yıllık Görünüm",
                calEvent = "ETKİNLİK",
                calEventNone = "Bu tarih için kayıtlı önemli bir İslami olay yok.",
                calEventMore = "Olayını okumak için vurgulanan başka bir tarihe dokunun.",
                calMonthEvents = "olay",
                calHijriSuffix = "H",
                calSearchPlaceholder = "Olaylarda ara...",
                calLocation = "Konum",
                calEventsOnDate = "BU TARİHTEKİ OLAYLAR",
                calSearchEmpty = "Eşleşen İslami olay bulunamadı."
            )
            "id" -> CalendarScreenStrings(
                calTitle = "Kalender Islam",
                calSubtitle = "Bulan saat ini, tanggal Hijriah, peristiwa penting Islam dan ikhtisar tahunan.",
                calToday = "Hari Ini",
                calSelected = "TANGGAL TERPILIH",
                calGreg = "Masehi",
                calHijri = "Hijriah",
                calYearView = "Tampilan Tahun",
                calEvent = "PERISTIWA",
                calEventNone = "Tidak ada peristiwa besar Islam untuk tanggal ini.",
                calEventMore = "Ketuk tanggal bertanda lain untuk melihat peristiwanya.",
                calMonthEvents = "peristiwa",
                calHijriSuffix = "H",
                calSearchPlaceholder = "Cari peristiwa...",
                calLocation = "Lokasi",
                calEventsOnDate = "PERISTIWA PADA TANGGAL INI",
                calSearchEmpty = "Tidak ada peristiwa Islam yang cocok."
            )
            "ms" -> CalendarScreenStrings(
                calTitle = "Kalendar Islam",
                calSubtitle = "Bulan semasa, tarikh Hijrah, peristiwa penting Islam dan tinjauan tahunan.",
                calToday = "Hari Ini",
                calSelected = "TARIKH DIPILIH",
                calGreg = "Masihi",
                calHijri = "Hijrah",
                calYearView = "Paparan Tahun",
                calEvent = "PERISTIWA",
                calEventNone = "Tiada peristiwa besar Islam dicatatkan untuk tarikh ini.",
                calEventMore = "Ketik tarikh bertanda lain untuk membaca peristiwanya.",
                calMonthEvents = "peristiwa",
                calHijriSuffix = "H",
                calSearchPlaceholder = "Cari peristiwa...",
                calLocation = "Lokasi",
                calEventsOnDate = "PERISTIWA PADA TARIKH INI",
                calSearchEmpty = "Tiada peristiwa Islam yang sepadan ditemui."
            )
            "fa" -> CalendarScreenStrings(
                calTitle = "تقویم اسلامی",
                calSubtitle = "ماه جاری، تاریخ‌های هجری، مناسبت‌های مهم اسلامی و نمای کلی سال.",
                calToday = "امروز",
                calSelected = "تاریخ انتخابی",
                calGreg = "میلادی",
                calHijri = "قمری",
                calYearView = "نمای سالانه",
                calEvent = "مناسبت",
                calEventNone = "هیچ مناسبت مهم اسلامی برای این تاریخ ثبت نشده است.",
                calEventMore = "روی تاریخ مشخص شده دیگری ضربه بزنید تا مناسبت آن را بخوانید.",
                calMonthEvents = "رویداد",
                calHijriSuffix = "ق",
                calSearchPlaceholder = "جستجوی رویدادها...",
                calLocation = "مکان",
                calEventsOnDate = "مناسبت‌های این تاریخ",
                calSearchEmpty = "هیچ رویداد اسلامی منطبقی یافت نشد."
            )
            "fr" -> CalendarScreenStrings(
                calTitle = "Calendrier Islamique",
                calSubtitle = "Mois en cours, dates hégiriennes, événements islamiques majeurs et vue annuelle.",
                calToday = "Aujourd'hui",
                calSelected = "DATE SÉLECTIONNÉE",
                calGreg = "Grégorien",
                calHijri = "Hégirien",
                calYearView = "Aperçu de l'année",
                calEvent = "ÉVÉNEMENT",
                calEventNone = "Aucun événement islamique majeur marqué pour cette date.",
                calEventMore = "Appuyez sur une autre date en surbrillance pour lire son événement.",
                calMonthEvents = "événements",
                calHijriSuffix = "AH",
                calSearchPlaceholder = "Rechercher des événements...",
                calLocation = "Lieu",
                calEventsOnDate = "ÉVÉNEMENTS À CETTE DATE",
                calSearchEmpty = "Aucun événement islamique correspondant trouvé."
            )
            "de" -> CalendarScreenStrings(
                calTitle = "Islamischer Kalender",
                calSubtitle = "Aktueller Monat, Hidschri-Daten, wichtige islamische Ereignisse und Jahresübersicht.",
                calToday = "Heute",
                calSelected = "AUSGEWÄHLTES DATUM",
                calGreg = "Gregorianisch",
                calHijri = "Hidschri",
                calYearView = "Jahresübersicht",
                calEvent = "EREIGNIS",
                calEventNone = "Kein wichtiges islamisches Ereignis für dieses Datum vermerkt.",
                calEventMore = "Tippen Sie auf ein anderes hervorgehobenes Datum, um das Ereignis zu sehen.",
                calMonthEvents = "Ereignisse",
                calHijriSuffix = "n.H.",
                calSearchPlaceholder = "Ereignisse suchen...",
                calLocation = "Ort",
                calEventsOnDate = "EREIGNISSE AN DIESEM DATUM",
                calSearchEmpty = "Keine passenden islamischen Ereignisse gefunden."
            )
            "ru" -> CalendarScreenStrings(
                calTitle = "Исламский Календарь",
                calSubtitle = "Текущий месяц, даты Хиджры, важные исламские события и обзор года.",
                calToday = "Сегодня",
                calSelected = "ВЫБРАННАЯ ДАТА",
                calGreg = "Григорианский",
                calHijri = "Хиджра",
                calYearView = "Обзор года",
                calEvent = "СОБЫТИЕ",
                calEventNone = "На эту дату нет крупных исламских событий.",
                calEventMore = "Нажмите на другую выделенную дату, чтобы прочитать о событии.",
                calMonthEvents = "событий",
                calHijriSuffix = "г.х.",
                calSearchPlaceholder = "Поиск событий...",
                calLocation = "Место",
                calEventsOnDate = "СОБЫТИЯ НА ЭТУ ДАТУ",
                calSearchEmpty = "Подходящих исламских событий не найдено."
            )
            "sw" -> CalendarScreenStrings(
                calTitle = "Kalenda ya Kiislamu",
                calSubtitle = "Mwezi wa sasa, tarehe za Hijria, matukio muhimu ya Kiislamu na muhtasari wa mwaka.",
                calToday = "Leo",
                calSelected = "TAREHE ILIYOCHAGULIWA",
                calGreg = "Miladi",
                calHijri = "Hijria",
                calYearView = "Mtazamo wa Mwaka",
                calEvent = "TUKIO",
                calEventNone = "Hakuna tukio kubwa la Kiislamu lililoandikwa kwa tarehe hii.",
                calEventMore = "Gusa tarehe nyingine iliyoangaziwa kusoma tukio lake.",
                calMonthEvents = "matukio",
                calHijriSuffix = "AH",
                calSearchPlaceholder = "Tafuta matukio...",
                calLocation = "Mahali",
                calEventsOnDate = "MATUKIO YA TAREHE HII",
                calSearchEmpty = "Hakuna matukio ya Kiislamu yaliyopatikana."
            )
            "so" -> CalendarScreenStrings(
                calTitle = "Kalandarka Islaamka",
                calSubtitle = "Bisha hadda, taariikhaha Hijriga, dhacdooyinka muhiimka ah iyo dulmar sannadeed.",
                calToday = "Maanta",
                calSelected = "TAARIIKHDA LA DOORTAY",
                calGreg = "Miilaadiga",
                calHijri = "Hijriga",
                calYearView = "Muuqaalka Sannadka",
                calEvent = "DHACDO",
                calEventNone = "Ma jirto dhacdo weyn oo Islaami ah oo taariikhdan ku qoran.",
                calEventMore = "Taabo taariikh kale oo la muujiyay si aad u akhrido dhacdadeeda.",
                calMonthEvents = "dhacdooyin",
                calHijriSuffix = "H",
                calSearchPlaceholder = "Raadi dhacdooyinka...",
                calLocation = "Goobta",
                calEventsOnDate = "DHACDOOYINKA TAARIIKHDAN",
                calSearchEmpty = "Lama helin dhacdooyin Islaami ah oo u dhigma."
            )
            "ha" -> CalendarScreenStrings(
                calTitle = "Kalandar Musulunci",
                calSubtitle = "Watan yanzu, kwanakin Hijira, manyan al'amuran Musulunci da bayanin shekara.",
                calToday = "Yau",
                calSelected = "RANAR DA AKA ZAƁA",
                calGreg = "Miladiyya",
                calHijri = "Hijira",
                calYearView = "Duba Shekara",
                calEvent = "AL'AMARI",
                calEventNone = "Babu wani babban al'amarin Musulunci da aka rubuta a wannan ranar.",
                calEventMore = "Taɓa wata ranar don ganin al'amarinta.",
                calMonthEvents = "al'amura",
                calHijriSuffix = "AH",
                calSearchPlaceholder = "Nemi al'amura...",
                calLocation = "Wuri",
                calEventsOnDate = "AL'AMURAN WANNAN RANAR",
                calSearchEmpty = "Ba a sami al'amuran Musulunci masu dacewa ba."
            )
            "es" -> CalendarScreenStrings(
                calTitle = "Calendario Islámico",
                calSubtitle = "Mes actual, fechas de la Hégira, eventos islámicos importantes y resumen del año.",
                calToday = "Hoy",
                calSelected = "FECHA SELECCIONADA",
                calGreg = "Gregoriano",
                calHijri = "Hégira",
                calYearView = "Vista del año",
                calEvent = "EVENTO",
                calEventNone = "No hay ningún evento islámico importante registrado para esta fecha.",
                calEventMore = "Toque otra fecha resaltada para ver su evento.",
                calMonthEvents = "eventos",
                calHijriSuffix = "AH",
                calSearchPlaceholder = "Buscar eventos...",
                calLocation = "Ubicación",
                calEventsOnDate = "EVENTOS EN ESTA FECHA",
                calSearchEmpty = "No se encontraron eventos islámicos coincidentes."
            )
            "zh" -> CalendarScreenStrings(
                calTitle = "伊斯兰历",
                calSubtitle = "当前月份、伊斯兰历日期、重要伊斯兰事件和全年概览。",
                calToday = "今天",
                calSelected = "所选日期",
                calGreg = "公历",
                calHijri = "伊斯兰历",
                calYearView = "全年概览",
                calEvent = "事件",
                calEventNone = "此日期没有标记重大的伊斯兰事件。",
                calEventMore = "点击其他突出显示的日期以阅读其事件。",
                calMonthEvents = "个事件",
                calHijriSuffix = "回历",
                calSearchPlaceholder = "搜索事件...",
                calLocation = "地点",
                calEventsOnDate = "此日期的事件",
                calSearchEmpty = "未找到匹配的伊斯兰事件。"
            )
            "or" -> CalendarScreenStrings(
                calTitle = "ଇସଲାମିକ କ୍ୟାଲେଣ୍ଡର",
                calSubtitle = "ଚାଲୁ ମାସ, ହିଜରୀ ତାରିଖ, ପ୍ରମୁଖ ଇସଲାମିକ ଘଟଣା ଏବଂ ବାର୍ଷିକ ସମୀକ୍ଷା।",
                calToday = "ଆଜି",
                calSelected = "ମନୋନୀତ ତାରିଖ",
                calGreg = "ଇସବୀ",
                calHijri = "ହିଜରୀ",
                calYearView = "ବାର୍ଷିକ ଦୃଶ୍ୟ",
                calEvent = "ଘଟଣା",
                calEventNone = "ଏହି ତାରିଖ ପାଇଁ କୌଣସି ପ୍ରମୁଖ ଇସଲାମିକ ଘଟଣା ଲିପିବଦ୍ଧ ନାହିଁ।",
                calEventMore = "ଘଟଣା ଦେଖିବାକୁ ଅନ୍ୟ ଏକ ଚିହ୍ନିତ ତାରିଖକୁ ଟ୍ୟାପ୍ କରନ୍ତୁ।",
                calMonthEvents = "ଘଟଣା",
                calHijriSuffix = "ହିଜରୀ",
                calSearchPlaceholder = "ଘଟଣା ଖୋଜନ୍ତୁ...",
                calLocation = "ସ୍ଥାନ",
                calEventsOnDate = "ଏହି ତାରିଖର ଘଟଣା",
                calSearchEmpty = "କୌଣସି ଇସଲାମିକ ଘଟଣା ମିଳିଲା ନାହିଁ।"
            )
            "as" -> CalendarScreenStrings(
                calTitle = "ইছলামিক কেলেণ্ডাৰ",
                calSubtitle = "চলিত মাহ, হিজৰী তাৰিখ, প্ৰধান ইছলামিক ঘটনাসমূহ আৰু বাৰ্ষিক বিৱৰণ।",
                calToday = "আজি",
                calSelected = "নিৰ্বাচিত তাৰিখ",
                calGreg = "ইংৰাজী",
                calHijri = "হিজৰী",
                calYearView = "বাৰ্ষিক দৃশ্য",
                calEvent = "ঘটনা",
                calEventNone = "এই তাৰিখত কোনো ডাঙৰ ইছলামিক ঘটনা লিপিবদ্ধ নাই।",
                calEventMore = "ঘটনা চাবলৈ আন এটা চিহ্নিত তাৰিখত টেপ কৰক।",
                calMonthEvents = "ঘটনা",
                calHijriSuffix = "হিজৰী",
                calSearchPlaceholder = "ঘটনা সন্ধান কৰক...",
                calLocation = "স্থান",
                calEventsOnDate = "এই তাৰিখৰ ঘটনাসমূহ",
                calSearchEmpty = "কোনো ইছলামিক ঘটনা পোৱা নগল।"
            )
            else -> CalendarScreenStrings(
                calTitle = "ISLAMIC CALENDAR",
                calSubtitle = "Current month, Hijri dates, major Islamic events and year overview.",
                calToday = "Today",
                calSelected = "SELECTED DATE",
                calGreg = "Gregorian",
                calHijri = "Hijri",
                calYearView = "Year View",
                calEvent = "EVENT",
                calEventNone = "No major Islamic event marked for this date.",
                calEventMore = "Tap another highlighted date to read its event.",
                calMonthEvents = "events",
                calHijriSuffix = "AH",
                calSearchPlaceholder = "Search Events...",
                calLocation = "Location",
                calEventsOnDate = "EVENTS ON THIS DATE",
                calSearchEmpty = "No matching Islamic events found."
            )
        }
    }

    fun getJavaLocale(code: String): Locale {
        return when (code) {
            "hi" -> Locale("hi", "IN")
            "ur" -> Locale("ur", "PK")
            "ar" -> Locale("ar", "SA")
            "bn" -> Locale("bn", "BD")
            "gu" -> Locale("gu", "IN")
            "mr" -> Locale("mr", "IN")
            "ta" -> Locale("ta", "IN")
            "te" -> Locale("te", "IN")
            "ml" -> Locale("ml", "IN")
            "kn" -> Locale("kn", "IN")
            "pa" -> Locale("pa", "IN")
            "or" -> Locale("or", "IN")
            "as" -> Locale("as", "IN")
            "tr" -> Locale("tr", "TR")
            "id" -> Locale("id", "ID")
            "ms" -> Locale("ms", "MY")
            "fa" -> Locale("fa", "IR")
            "fr" -> Locale.FRANCE
            "de" -> Locale.GERMANY
            "ru" -> Locale("ru", "RU")
            "sw" -> Locale("sw", "KE")
            "so" -> Locale("so", "SO")
            "ha" -> Locale("ha", "NG")
            "es" -> Locale("es", "ES")
            "zh" -> Locale.SIMPLIFIED_CHINESE
            else -> Locale.US
        }
    }

    fun getGregorianMonthNames(locale: String): List<String> {
        return when (locale) {
            "hi" -> listOf("जनवरी", "फ़रवरी", "मार्च", "अप्रैल", "मई", "जून", "जुलाई", "अगस्त", "सितंबर", "अक्टूबर", "नवंबर", "दिसंबर")
            "ur" -> listOf("جنوری", "فروری", "مارچ", "اپریل", "مئی", "جون", "جولائی", "اگست", "ستمبر", "اکتوبر", "نومبر", "دسمبر")
            "ar" -> listOf("يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو", "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر")
            "bn" -> listOf("জানুয়ারি", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন", "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর")
            "gu" -> listOf("જાન્યુઆરી", "ફેબ્રુઆરી", "માર્ચ", "એપ્રિલ", "મે", "જૂન", "જુલાઈ", "ઑગસ્ટ", "સપ્ટેમ્બર", "ઑક્ટોબર", "નવેમ્બર", "ડિસેમ્બર")
            "mr" -> listOf("जानेवारी", "फेब्रुवारी", "मार्च", "एप्रिल", "मे", "जून", "जुलै", "ऑगस्ट", "सप्टेंबर", "ऑक्टोबर", "नोव्हेंबर", "डिसेंबर")
            "ta" -> listOf("ஜனவரி", "பிப்ரவரி", "மார்ச்", "ஏப்ரல்", "மே", "ஜூன்", "ஜூலை", "ஆகஸ்ட்", "செப்டம்பர்", "அக்டோபர்", "நவம்பர்", "டிசம்பர்")
            "te" -> listOf("జనవరి", "ఫిబ్రవరి", "మార్చి", "ఏప్రిల్", "మే", "జూన్", "జూలై", "ఆగస్టు", "సెప్టెంబర్", "అక్టోబర్", "నవంబర్", "డిసెంబర్")
            "ml" -> listOf("ജനുവരി", "ഫെബ്രുവരി", "മാർച്ച്", "ഏപ്രിൽ", "മേയ്", "ജൂൺ", "ജൂലൈ", "ഓഗസ്റ്റ്", "സെപ്റ്റംബർ", "ഒക്ടോബർ", "നവംബർ", "ഡിസംബർ")
            "kn" -> listOf("ಜನವರಿ", "ಫೆಬ್ರವರಿ", "ಮಾರ್ಚ್", "ಏಪ್ರಿಲ್", "ಮೇ", "ಜೂನ್", "ಜುಲೈ", "ಆಗಸ್ಟ್", "ಸೆಪ್ಟೆಂಬರ್", "ಅಕ್ಟೋಬರ್", "ನವೆಂಬರ್", "ಡಿಸೆಂಬರ್")
            "pa" -> listOf("ਜਨਵਰੀ", "ਫ਼ਰਵਰੀ", "ਮਾਰਚ", "ਅਪ੍ਰੈਲ", "ਮਈ", "ਜੂਨ", "ਜੁਲਾਈ", "ਅਗਸਤ", "ਸਤੰਬਰ", "ਅਕਤੂਬਰ", "ਨਵੰਬਰ", "ਦਸੰਬਰ")
            "or" -> listOf("ଜାନୁଆରୀ", "ଫେବୃଆରୀ", "ମାର୍ଚ୍ଚ", "ଏପ୍ରିଲ", "ମେ", "ଜୁନ", "ଜୁଲାଇ", "ଅଗଷ୍ଟ", "ସେପ୍ଟେମ୍ବର", "ଅକ୍ଟୋବର", "ନଭେମ୍ବର", "ଡିସେମ୍ବର")
            "as" -> listOf("জানুৱাৰী", "ফেব্ৰুৱাৰী", "মাৰ্চ", "এপ্ৰিল", "মে'", "জুন", "জুলাই", "আগষ্ট", "ছেপ্টেম্বৰ", "অক্টোবৰ", "নৱেম্বৰ", "ডিচেম্বৰ")
            "sw" -> listOf("Januari", "Februari", "Machi", "Aprili", "Mei", "Juni", "Julai", "Agosti", "Septemba", "Oktoba", "Novemba", "Desemba")
            "so" -> listOf("Jannaayo", "Febraayo", "Maarso", "Abriil", "May", "Juun", "Luuliyo", "Ogosto", "Sebtembar", "Oktoobar", "Nofembar", "Desembar")
            "ha" -> listOf("Janairu", "Fabrairu", "Marsi", "Afrilu", "Mayu", "Yuni", "Yuli", "Agusta", "Satumba", "Oktoba", "Nuwamba", "Disamba")
            "tr" -> listOf("Ocak", "Şubat", "Mart", "Nisan", "Mayıs", "Haziran", "Temmuz", "Ağustos", "Eylül", "Ekim", "Kasım", "Aralık")
            "id", "ms" -> listOf("Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember")
            "fa" -> listOf("ژانویه", "فوریه", "مارس", "آوریل", "مه", "ژوئن", "ژوئیه", "اوت", "سپتامبر", "اکتبر", "نوامبر", "دسامبر")
            "fr" -> listOf("Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre")
            "de" -> listOf("Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember")
            "ru" -> listOf("Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь")
            "es" -> listOf("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")
            "zh" -> listOf("一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月")
            else -> listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        }
    }

    fun getGregorianShortMonthNames(locale: String): List<String> {
        return when (locale) {
            "hi" -> listOf("जन", "फ़र", "मार्च", "अप्रै", "मई", "जून", "जुला", "अग", "सितं", "अक्टू", "नवं", "दिसं")
            "ur" -> listOf("جن", "فر", "مارچ", "اپریل", "مئی", "جون", "جولائی", "اگست", "ستم", "اکت", "نوم", "دسم")
            "ar" -> listOf("ينا", "فبر", "مار", "أبر", "ماي", "يون", "يول", "أغس", "سبت", "أكت", "نوف", "ديس")
            "bn" -> listOf("জানু", "ফেব্রু", "মার্চ", "এপ্রিল", "মে", "জুন", "জুলাই", "আগস্ট", "সেপ্টে", "অক্টো", "নভে", "ডিসে")
            "gu" -> listOf("જાન્યુ", "ફેબ્રુ", "માર્ચ", "એપ્રિલ", "મે", "જૂન", "જુલાઈ", "ઑગસ્ટ", "સપ્ટે", "ઑક્ટો", "નવે", "ડિસે")
            "mr" -> listOf("जाने", "फेब्रु", "মার্চ", "एप्रि", "मे", "जून", "जुलै", "ऑग", "सप्टें", "ऑक्टो", "नोव्हें", "डिसें")
            "ta" -> listOf("ஜன", "பிப்", "மார்ச்", "ஏப்", "மே", "ஜூன்", "ஜூலை", "ஆக", "செப்", "அக்", "நவ", "டிச")
            "te" -> listOf("జన", "ఫిబ్ర", "మార్చి", "ఏప్రి", "మే", "జూన్", "జూలై", "ఆగ", "సెప్టెం", "అక్టో", "నవం", "డిసెం")
            "ml" -> listOf("ജനു", "ഫെബ്രു", "മാർച്ച്", "ഏപ്രി", "മേയ്", "ജൂൺ", "ജൂലൈ", "ഓഗ", "സെപ്റ്റം", "ഒക്ടോ", "നവം", "ഡിസം")
            "kn" -> listOf("ಜನ", "ಫೆಬ್ರ", "ಮಾರ್ಚ್", "ಏಪ್ರಿ", "ಮೇ", "ಜೂನ್", "ಜುಲೈ", "ಆಗ", "ಸೆಪ್ಟೆಂ", "ಅಕ್ಟೋ", "ನವೆಂ", "ಡಿಸೆಂ")
            "pa" -> listOf("ਜਨ", "ਫ਼ਰ", "ਮਾਰਚ", "ਅਪ੍ਰੈ", "ਮਈ", "ਜੂਨ", "ਜੁਲਾਈ", "ਅਗ", "ਸਤੰ", "ਅਕਤੂ", "ਨਵੰ", "ਦਸੰ")
            "or" -> listOf("ଜାନୁ", "ଫେବ୍ରୁ", "ମାର୍ଚ୍ଚ", "ଏପ୍ରି", "ମେ", "ଜୁନ", "ଜୁଲାଇ", "ଅଗ", "ସେପ୍ଟେ", "ଅକ୍ଟୋ", "ନଭେ", "ଡିସେ")
            "as" -> listOf("জানু", "ফেব্ৰু", "মাৰ্চ", "এপ্ৰি", "মে'", "জুন", "জুলাই", "আগ", "ছেপ্টে", "অক্টো", "নৱে", "ডিচে")
            "sw" -> listOf("Jan", "Feb", "Mac", "Apr", "Mei", "Jun", "Jul", "Ago", "Sep", "Okt", "Nov", "Des")
            "so" -> listOf("Jan", "Feb", "Mar", "Abr", "May", "Jun", "Luu", "Ogo", "Seb", "Okt", "Nof", "Des")
            "ha" -> listOf("Jan", "Fab", "Mar", "Afr", "May", "Yun", "Yul", "Agu", "Sat", "Okt", "Nuw", "Dis")
            "tr" -> listOf("Oca", "Şub", "Mar", "Nis", "May", "Haz", "Tem", "Ağu", "Eyl", "Eki", "Kas", "Ara")
            "id", "ms" -> listOf("Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agt", "Sep", "Okt", "Nov", "Des")
            "fa" -> listOf("ژانویه", "فوریه", "مارس", "آوریل", "مه", "ژوئن", "ژوئیه", "اوت", "سپتامبر", "اکتبر", "نوامبر", "دسامبر")
            "fr" -> listOf("Janv", "Févr", "Mars", "Avr", "Mai", "Juin", "Juil", "Août", "Sept", "Oct", "Nov", "Déc")
            "de" -> listOf("Jan", "Feb", "Mär", "Apr", "Mai", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dez")
            "ru" -> listOf("Янв", "Фев", "Мар", "Апр", "Май", "Июн", "Июл", "Авг", "Сен", "Окт", "Ноя", "Дек")
            "es" -> listOf("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic")
            "zh" -> listOf("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月")
            else -> listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        }
    }

    fun getWeekdayHeaders(locale: String): List<String> {
        return when (locale) {
            "hi" -> listOf("रवि", "सोम", "मंगल", "बुध", "गुरु", "शुक्र", "शनि")
            "ur" -> listOf("اتوار", "پیر", "منگل", "بدھ", "جمعرات", "جمعہ", "ہفتہ")
            "ar" -> listOf("الأحد", "الإثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة", "السبت")
            "bn" -> listOf("রবি", "সোম", "মঙ্গল", "বুধ", "বৃহঃ", "শুক্র", "শনি")
            "gu" -> listOf("રવિ", "સોમ", "મંગળ", "બુધ", "ગુરુ", "શુક્ર", "શનિ")
            "mr" -> listOf("रवि", "सोम", "मंगळ", "बुध", "गुरु", "शुक्र", "शनि")
            "ta" -> listOf("ஞாயிறு", "திங்கள்", "செவ்வாய்", "புதன்", "வியாழன்", "வெள்ளி", "சனி")
            "te" -> listOf("ఆది", "సోమ", "మంగళ", "బుధ", "గురు", "శుక్ర", "శని")
            "ml" -> listOf("ഞായർ", "തിങ്കൾ", "ചൊവ്വ", "ബുധൻ", "വ്യാഴം", "വെള്ളി", "ശനി")
            "kn" -> listOf("ಭಾನು", "ಸೋಮ", "ಮಂಗಳ", "ಬುಧ", "ಗುರು", "ಶುಕ್ರ", "ಶನಿ")
            "pa" -> listOf("ਐਤ", "ਸੋਮ", "ਮੰਗਲ", "ਬੁੱਧ", "ਵੀਰ", "ਸ਼ੁੱਕਰ", "ਸ਼ਨੀ")
            "or" -> listOf("ରବି", "ସୋମ", "ମଙ୍ଗଳ", "ବୁଧ", "ଗୁରୁ", "ଶୁକ୍ର", "ଶନି")
            "as" -> listOf("দেও", "সোম", "মঙ্গল", "বুধ", "বৃহঃ", "শুক্ৰ", "শনি")
            "sw" -> listOf("JUM", "JMT", "JNN", "JTN", "ALH", "IJU", "JMS")
            "so" -> listOf("AXA", "ISN", "TAL", "ARB", "KHA", "JIM", "SAB")
            "ha" -> listOf("LAH", "LIT", "TAL", "LAR", "ALH", "JUM", "ASAB")
            "tr" -> listOf("PAZ", "PZT", "SAL", "ÇAR", "PER", "CUM", "CTS")
            "id" -> listOf("MIN", "SEN", "SEL", "RAB", "KAM", "JUM", "SAB")
            "ms" -> listOf("AHA", "ISN", "SEL", "RAB", "KHA", "JUM", "SAB")
            "fa" -> listOf("یک‌شنبه", "دوشنبه", "سه‌شنبه", "چهارشنبه", "پنج‌شنبه", "جمعه", "شنبه")
            "fr" -> listOf("DIM", "LUN", "MAR", "MER", "JEU", "VEN", "SAM")
            "de" -> listOf("SO", "MO", "DI", "MI", "DO", "FR", "SA")
            "ru" -> listOf("ВС", "ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ")
            "es" -> listOf("DOM", "LUN", "MAR", "MIÉ", "JUE", "VIE", "SÁB")
            "zh" -> listOf("日", "一", "二", "三", "四", "五", "六")
            else -> listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")
        }
    }

    fun getFullDayNames(locale: String): List<String> {
        return when (locale) {
            "hi" -> listOf("रविवार", "सोमवार", "मंगलवार", "बुधवार", "गुरुवार", "शुक्रवार", "शनिवार")
            "ur" -> listOf("اتوار", "پیر", "منگل", "بدھ", "جمعرات", "جمعہ", "ہفتہ")
            "ar" -> listOf("الأحد", "الإثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة", "السبت")
            "bn" -> listOf("রবিবার", "সোমবার", "মঙ্গলবার", "বুধবার", "বৃহস্পতিবার", "শুক্রবার", "শনিবার")
            "gu" -> listOf("રવિવાર", "સોમવાર", "મંગળવાર", "બુધવાર", "ગુરુવાર", "શુક્રવાર", "શનિવાર")
            "mr" -> listOf("रविवार", "सोमवार", "मंगळवार", "बुधवार", "गुरुवार", "शुक्रवार", "शनिवार")
            "ta" -> listOf("ஞாயிற்றுக்கிழமை", "திங்கட்கிழமை", "செவ்வாய்க்கிழமை", "புதன்கிழமை", "வியாழக்கிழமை", "வெள்ளிக்கிழமை", "சனிக்கிழமை")
            "te" -> listOf("ఆదివారం", "సోమవారం", "మంగళవారం", "బుధవారం", "గురువారం", "శుక్రవారం", "శనివారం")
            "ml" -> listOf("ഞായറാഴ്ച", "തിങ്കളാഴ്ച", "ചൊവ്വാഴ്ച", "ബുധനാഴ്ച", "വ്യാഴാഴ്ച", "വെള്ളിയാഴ്ച", "ശനിയാഴ്ച")
            "kn" -> listOf("ಭಾನುವಾರ", "ಸೋಮವಾರ", "ಮಂಗಳವಾರ", "ಬುಧವಾರ", "ಗುರುವಾರ", "ಶುಕ್ರವಾರ", "ಶನಿವಾರ")
            "pa" -> listOf("ਐਤਵਾਰ", "ਸੋਮਵਾਰ", "ਮੰਗਲਵਾਰ", "ਬੁੱਧਵਾਰ", "ਵੀਰਵਾਰ", "ਸ਼ੁੱਕਰਵਾਰ", "ਸ਼ਨੀਵਾਰ")
            "or" -> listOf("ରବିବାର", "ସୋମବାର", "ମଙ୍ଗଳବାର", "ବୁଧବାର", "ଗୁରୁବାର", "ଶୁକ୍ରବାର", "ଶନିବାର")
            "as" -> listOf("দেওবাৰ", "সোমবাৰ", "মঙলবাৰ", "বুধবাৰ", "বৃহস্পতিবাৰ", "শুক্ৰবাৰ", "শনিবাৰ")
            "sw" -> listOf("Jumapili", "Jumatatu", "Jumanne", "Jumatano", "Alhamisi", "Ijumaa", "Jumamosi")
            "so" -> listOf("Axad", "Isniin", "Talaado", "Arbaco", "Khamiis", "Jimce", "Sabti")
            "ha" -> listOf("Lahadi", "Litinin", "Talata", "Laraba", "Alhamis", "Juma'a", "Asabar")
            "tr" -> listOf("Pazar", "Pazartesi", "Salı", "Çarşamba", "Perşembe", "Cuma", "Cumartesi")
            "id" -> listOf("Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu")
            "ms" -> listOf("Ahad", "Isnin", "Selasa", "Rabu", "Khamis", "Jumaat", "Sabtu")
            "fa" -> listOf("یک‌شنبه", "دوشنبه", "سه‌شنبه", "چهارشنبه", "پنج‌شنبه", "جمعه", "شنبه")
            "fr" -> listOf("Dimanche", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi")
            "de" -> listOf("Sonntag", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag")
            "ru" -> listOf("Воскресенье", "Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота")
            "es" -> listOf("Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado")
            "zh" -> listOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
            else -> listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        }
    }

    fun formatSelectedDateHeading(date: Date, locale: String): String {
        val cal = Calendar.getInstance().apply { time = date }
        val dayOfWeekIdx = cal.get(Calendar.DAY_OF_WEEK) // 1 = Sunday, 2 = Monday, ...
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val monthIdx = cal.get(Calendar.MONTH) // 0-based
        val year = cal.get(Calendar.YEAR)

        val fullMonths = getGregorianMonthNames(locale)
        val monthName = fullMonths.getOrElse(monthIdx) { SimpleDateFormat("MMMM", Locale.US).format(date) }

        val fullDays = getFullDayNames(locale)
        val dayName = fullDays.getOrElse(dayOfWeekIdx - 1) { SimpleDateFormat("EEEE", Locale.US).format(date) }

        return when (locale) {
            "hi", "mr", "gu", "pa", "bn", "ta", "te", "ml", "kn", "or", "as" -> "$dayName, $day $monthName $year"
            "ur", "ar", "fa" -> "$dayName، $day $monthName $year"
            "tr", "id", "ms", "fr", "de", "ru", "es", "sw", "so", "ha" -> "$dayName, $day $monthName $year"
            "zh" -> "${year}年$monthName${day}日 $dayName"
            else -> "$dayName, $monthName $day, $year"
        }
    }

    fun formatGregorianDate(date: Date, locale: String): String {
        val cal = Calendar.getInstance().apply { time = date }
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val monthIdx = cal.get(Calendar.MONTH)
        val year = cal.get(Calendar.YEAR)

        val fullMonths = getGregorianMonthNames(locale)
        val monthName = fullMonths.getOrElse(monthIdx) { SimpleDateFormat("MMMM", Locale.US).format(date) }

        return when (locale) {
            "hi", "mr", "gu", "pa", "bn", "ta", "te", "ml", "kn", "or", "as" -> "$day $monthName $year"
            "ur", "ar", "fa" -> "$day $monthName $year"
            "tr", "id", "ms", "fr", "de", "ru", "es", "sw", "so", "ha" -> "$day $monthName $year"
            "zh" -> "${year}年$monthName${day}日"
            else -> "$monthName $day, $year"
        }
    }

    fun formatTodayButtonLabel(date: Date, locale: String): String {
        val cal = Calendar.getInstance().apply { time = date }
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val monthIdx = cal.get(Calendar.MONTH)

        val shortMonths = getGregorianShortMonthNames(locale)
        val monthName = shortMonths.getOrElse(monthIdx) { SimpleDateFormat("MMM", Locale.US).format(date) }

        return when (locale) {
            "hi", "mr", "gu", "pa", "bn", "ta", "te", "ml", "kn", "or", "as" -> "$day $monthName"
            "ur", "ar", "fa" -> "$day $monthName"
            "tr", "id", "ms", "fr", "de", "ru", "es", "sw", "so", "ha" -> "$day $monthName"
            "zh" -> "$monthName${day}日"
            else -> "$monthName $day"
        }
    }

    // ══════════════════════════════════════════════════════════════
    //  EVENT LOCALIZATION (25+ Languages: Hindi, Urdu, Arabic, etc.)
    // ══════════════════════════════════════════════════════════════
    fun formatAnniversary(rawAnniv: String, locale: String): String {
        if (rawAnniv.isBlank()) return ""
        val match = Regex("(\\d+)").find(rawAnniv) ?: return rawAnniv
        val num = match.groupValues[1]

        return when (locale) {
            "hi" -> "${num}वाँ सालाना उर्स"
            "ur" -> "${num}واں سالانہ عرس"
            "ar" -> "العرس السنوي الـ $num"
            "bn" -> "${num}তম বার্ষিক উরস"
            "gu" -> "${num}મો વાર્ષિક ઉર્સ"
            "mr" -> "${num}वे वार्षिक उर्स"
            "ta" -> "${num}-வது ஆண்டு உர்ஸ்"
            "te" -> "${num}వ వార్షిక ఉర్స్"
            "ml" -> "${num}-ാമത് വാർഷിക ഉറൂസ്"
            "kn" -> "${num}ನೇ ವಾರ್ಷಿಕ ಉರ್ಸ್"
            "pa" -> "${num}ਵਾਂ ਸਾਲਾਨা ਉਰਸ"
            "or" -> "${num}ତମ ବାର୍ଷିକ ଉରସ"
            "as" -> "${num}তম বাৰ্ষিক উৰছ"
            "tr" -> "$num. Yıllık Urus"
            "id", "ms" -> "Haul Tahunan ke-$num"
            "fa" -> "${num}مین سالگرد عرس"
            "fr" -> "${num}e Urs Annuel"
            "de" -> "$num. Jahres-Urs"
            "ru" -> "$num-й Ежегодный Урс"
            "es" -> "${num}º Urs Anual"
            "zh" -> "第${num}届年度乌尔斯"
            "sw" -> "Urs ya $num ya Mwaka"
            "so" -> "Urs-ka Sanadlaha ah ee ${num}aad"
            "ha" -> "Bikin Urs na Shekara na $num"
            else -> "${num}th Annual Urs"
        }
    }

    private val EVENT_NAMES = mapOf(
        "urs_mahimi" to mapOf(
            "hi" to "हज़रत मख़दूम अली माहिमी (रह.)",
            "ur" to "حضرت مخدوم علی مہائمی (رح)",
            "ar" to "حضرة مخدوم علي المَهائمي",
            "bn" to "হযরত মাখদুম আলী মাহিমি (রহ.)"
        ),
        "urs_moinuddin" to mapOf(
            "hi" to "हज़रत ख़्वाजा मोईनुद्दीन चिश्ती (रह.)",
            "ur" to "حضرت خواجہ معین الدین چشتی (رح)",
            "ar" to "حضرة خواجة معين الدين الجشتي",
            "bn" to "হযরত খাজা মঈনুদ্দীন চিশতী (রহ.)"
        ),
        "urs_ghaus_pak" to mapOf(
            "hi" to "हज़रत शैख़ अब्दुल क़ादिर जीलानी (ग़ौस-ए-आज़म)",
            "ur" to "حضرت شیخ عبد القادر جیلانی (غوث اعظم)",
            "ar" to "حضرة الشيخ عبد القادر الجيلاني (الغوث الأعظم)",
            "bn" to "হযরত বড়পীর আব্দুল কাদের জিলানী (রহ.)"
        ),
        "urs_haji_ali" to mapOf(
            "hi" to "हज़रत पीर हाजी अली शाह बुख़ारी (रह.)",
            "ur" to "حضرت پیر حاجی علی شاہ بخاری (رح)",
            "ar" to "حضرة بير حاجي علي شاه البخاري",
            "bn" to "হযরত পীর হাজী আলী শাহ বুখারী (রহ.)"
        ),
        "urs_nizamuddin" to mapOf(
            "hi" to "हज़रत निज़ामुद्दीन औलिया (महबूब-ए-इलाही)",
            "ur" to "حضرت نظام الدین اولیاء (محبوب الٰہی)",
            "ar" to "حضرة نظام الدين أولياء",
            "bn" to "হযরত নিজামুদ্দিন আউলিয়া (রহ.)"
        ),
        "urs_sabir_kalyari" to mapOf(
            "hi" to "हज़रत अलाउद्दीन अली अहमद साबिर कलियरी (रह.)",
            "ur" to "حضرت صابر کلیری (رح)",
            "ar" to "حضرة صابر الكلياري",
            "bn" to "হযরত সাবির কালিয়ারী (রহ.)"
        ),
        "urs_ganjshakar" to mapOf(
            "hi" to "हज़रत बाबा फ़रीदुद्दीन गंजशकर (रह.)",
            "ur" to "حضرت بابا فرید الدین گنج شکر (رح)",
            "ar" to "حضرة فريد الدين كنج شكر",
            "bn" to "হযরত বাবা ফরিদুদ্দীন গঞ্জেশকর (রহ.)"
        ),
        "urs_bakhtiyar_kaki" to mapOf(
            "hi" to "हज़रत क़ुतुबुद्दीन बख़्तियार काकी (रह.)",
            "ur" to "حضرت قطب الدین بختیار کاکی (رح)",
            "ar" to "حضرة قطب الدين بختيار كاكي",
            "bn" to "হযরত কুতুবুদ্দীন বখতিয়ার কাকি (রহ.)"
        ),
        "urs_banda_nawaz" to mapOf(
            "hi" to "हज़रत ख़्वाजा बंदा नवाज़ गेसू दराज़ (रह.)",
            "ur" to "حضرت خواجہ بندہ نواز گیسودراز (رح)",
            "ar" to "حضرة خواجة بندة نواز كيسو دراز",
            "bn" to "হযরত খাজা বন্দা নেওয়াজ গেসু দারাজ (রহ.)"
        ),
        "urs_ahmed_raza" to mapOf(
            "hi" to "आला हज़रत इमाम अहमद रज़ा ख़ान फ़ाज़िले बरेलवी",
            "ur" to "اعلیٰ حضرت امام احمد رضا خان بریلوی",
            "ar" to "الإमाम أحمد رضا خان البريلوي",
            "bn" to "আলা হযরত ইমাম আহমদ রেজা খান বেরেলভী (রহ.)"
        ),
        "urs_mustafa_raza" to mapOf(
            "hi" to "मुफ़्ती-ए-आज़म हिंद हज़रत मुस्तफ़ा रज़ा ख़ान (रह.)",
            "ur" to "مفتی اعظم ہند حضرت مصطفیٰ رضا خان",
            "ar" to "مفتي أعظم الهند مصطفى رضا خان",
            "bn" to "মুফতিয়ে আযম হিন্দ মুস্তফা রেজা খান (রহ.)"
        ),
        "urs_tajuddin" to mapOf(
            "hi" to "हज़रत ताजुद्दीन बाबा औलिया (नागपुर)",
            "ur" to "حضرت تاج الدین بابا اولیاء (नागپور)",
            "ar" to "حضرة تاج الدين بابا أولياء",
            "bn" to "হযরত তাজউদ্দীন বাবা আউলিয়া"
        ),
        "urs_waris_ali" to mapOf(
            "hi" to "हज़रत हाजी वारिस अली शाह (देवा शरीफ़)",
            "ur" to "حضرت حاجی وارث علی شاہ (دیوہ شریف)",
            "ar" to "حضرة حاجي وارث علي شاه",
            "bn" to "হযরত হাজী ওয়ারিস আলী শাহ"
        ),
        "urs_mira_datar" to mapOf(
            "hi" to "हज़रत सैयद अली मीरा दातार (उनवा शरीफ़)",
            "ur" to "حضرت سید علی میراں داتار (اوناوا)",
            "ar" to "حضرة سيد علي ميرا داتار",
            "bn" to "হযরত সৈয়দ আলী মীরা দাতার"
        ),
        "urs_ali_akbar_datar" to mapOf(
            "hi" to "हज़रत अली अकबर शाह दातार (उनवा)",
            "ur" to "حضرت علی اکبر شاہ داتار",
            "ar" to "حضرة علي أكبر شاه داتار",
            "bn" to "হযরত আলী আকবর शाह दातार"
        ),
        "urs_ashraf_jahangir" to mapOf(
            "hi" to "हज़रत मख़दूम अशरफ़ जहांगीर सिमनानी (रह.)",
            "ur" to "حضرت مخدوم اشرف جہانگیر سمنانی (رح)",
            "ar" to "حضرة مخدوم أشرف جهانكير السمناني",
            "bn" to "হযরত মাখদুম আশরাফ জাহাঙ্গীর সিমনাঈ (রহ.)"
        ),
        "urs_bahauddin_naqshband" to mapOf(
            "hi" to "हज़रत ख़्वाजा बहाउद्दीन नक़्शबंद बुख़ारी (रह.)",
            "ur" to "حضرت خواجہ بہاؤالدین نقشبند بخاری (رح)",
            "ar" to "حضرة خواجة بهاء الدين نقشبند",
            "bn" to "হযরত খাজা বাহাউদ্দীন নকশবন্দ বুখারী (রহ.)"
        ),
        "urs_bahauddin_zakariya" to mapOf(
            "hi" to "हज़रत शैख़ बहाउद्दीन ज़करिया मुल्तानी (रह.)",
            "ur" to "حضرت بہاؤ الدین زکریا ملتانی (رح)",
            "ar" to "حضرة بهاء الدين زكريا الملتاني",
            "bn" to "হযরত বাহাউদ্দীন যাকারিয়া মুলতানী (রহ.)"
        ),
        "urs_data_ganj_bakhsh" to mapOf(
            "hi" to "हज़रत अली हुजवेरी दाता गंज बख़्श (रह.)",
            "ur" to "حضرت علی ہجویری داتا گنج بخش (رح)",
            "ar" to "حضرة علي الهجويري داتا كنج بخش",
            "bn" to "হযরত আলী হুজভিরী দাতা গঞ্জ বখশ (রহ.)"
        ),
        "urs_lal_shahbaz" to mapOf(
            "hi" to "हज़रत उसमान मरवंदी लाल शहबाज़ क़लंदर (रह.)",
            "ur" to "حضرت لعل شہباز قلندر (رح)",
            "ar" to "حضرة لعل شهباز قلندر",
            "bn" to "হযরত লাল শাহবাজ কলন্দর (রহ.)"
        ),
        "urs_shah_jalal" to mapOf(
            "hi" to "हज़रत शाह जलाल मुजर्रद सिलहटी (रह.)",
            "ur" to "حضرت شاہ جلال مجرد سلہٹی (رح)",
            "ar" to "حضرة شاه جلال المجرد",
            "bn" to "হযরত শাহ জালাল মুজাররাদ (রহ.)"
        ),
        "urs_shahul_hameed" to mapOf(
            "hi" to "हज़रत शाहुल हमीद क़ादिर वली नागौरी (रह.)",
            "ur" to "حضرت شاہ الحمید قادر ولی ناگوری (رح)",
            "ar" to "حضرة شاه الحميد قادر ولي الناغوري",
            "bn" to "হযরত শাহুল হামিদ কাদির ওলী (রহ.)"
        ),
        "urs_shah_alam" to mapOf(
            "hi" to "हज़रत शाह आलम बुख़ारी (अहमदाबाद)",
            "ur" to "حضرت شاہ عالم بخاری (احمد آباد)",
            "ar" to "حضرة شاه عالم البخاري",
            "bn" to "হযরত শাহ আলম বুখারী (রহ.)"
        ),
        "urs_salar_masud" to mapOf(
            "hi" to "हज़रत सैयद सालार मसूद ग़ाज़ी (बहराइच)",
            "ur" to "حضرت سید سالار مسعود غازی (بہڑائچ)",
            "ar" to "حضرة سيد سالار مسعود غازي",
            "bn" to "হযরত সৈয়দ সালার মাসুদ গাজী"
        ),
        "urs_baba_sharfuddin" to mapOf(
            "hi" to "हज़रत बाबा शरफ़ुद्दीन सोहरवर्दी (पहाड़ी शरीफ़)",
            "ur" to "حضرت بابا شرف الدین سہروردی",
            "ar" to "حضرة بابا شرف الدين السهروردي",
            "bn" to "হযরত বাবা শরফুদ্দীন সোহরাওয়ার্দী"
        ),
        "urs_sultan_ibrahim" to mapOf(
            "hi" to "हज़रत सुल्तान सैयद इब्राहीम शहीद बादुशाह (एरवाड़ी)",
            "ur" to "حضرت سلطان سید ابراہیم شہید بادشاہ",
            "ar" to "حضرة سلطان سيد إبراهيم شهيد بادوشاه",
            "bn" to "হযরত সুলতান সৈয়দ ইব্রাহিম শহীদ বাদুশাহ"
        ),
        "urs_suhrawardi" to mapOf(
            "hi" to "हज़रत शहाबुद्दीन उमर सोहरवर्दी (रह.)",
            "ur" to "حضرت شہاب الدین عمر سہروردی (رح)",
            "ar" to "حضرة شهاب الدين السهروردي",
            "bn" to "হযরত শিহাবুদ্দীন সোহরাওয়ার্দী (রহ.)"
        ),
        "urs_maruf_karkhi" to mapOf(
            "hi" to "हज़रत मारूफ़ करख़ी (रह.)",
            "ur" to "حضرت معروف کرخی (رح)",
            "ar" to "حضرة معروف الكرخي",
            "bn" to "হযরত মারুফ কারখী (রহ.)"
        ),
        "urs_hasan_basri" to mapOf(
            "hi" to "हज़रत हसन बसरी (रह.)",
            "ur" to "حضرت حسن بصری (رح)",
            "ar" to "حضرة الحسن البصري",
            "bn" to "হযরত হাসান বসরী (রহ.)"
        ),
        "urs_maneri" to mapOf(
            "hi" to "हज़रत शरफ़ुद्दीन यहया मनेरी (मख़दूम-ए-जहां)",
            "ur" to "حضرت شرف الدین یحییٰ منیری (رح)",
            "ar" to "حضرة شرف الدين يحيى المنيري",
            "bn" to "হযরত শরফুদ্দীন ইয়াহিয়া মানেরী (রহ.)"
        ),
        "urs_bu_ali_qalandar" to mapOf(
            "hi" to "हज़रत बू अली शाह क़लंदर (पानीपत)",
            "ur" to "حضرت بو علی شاہ قلندر (پانی پت)",
            "ar" to "حضرة بو علي شاه قلندر",
            "bn" to "হযরত বু علی شاہ কলন্দর"
        ),
        "urs_shah_waliullah" to mapOf(
            "hi" to "हज़रत शाह वलीउल्लाह देहलवी (रह.)",
            "ur" to "حضرت شاہ ولی اللہ دہلوی (رح)",
            "ar" to "حضرة شاه ولي الله الدهلوي",
            "bn" to "হযরত শাহ ওয়ালীউল্লাহ দেহলভী (রহ.)"
        ),
        "urs_abdul_haq_dehlvi" to mapOf(
            "hi" to "हज़रत शैख़ अब्दुल हक़ मुहद्दिस देहलवी (रह.)",
            "ur" to "حضرت شیخ عبد الحق محدث دہلوی (رح)",
            "ar" to "حضرة عبد الحق الدهلوي",
            "bn" to "হযরত আব্দুল হক মুহাদ্দিস দেহলভী (রহ.)"
        ),
        "urs_shah_mina" to mapOf(
            "hi" to "हज़रत शाह मीना लखनवी (रह.)",
            "ur" to "حضرت شاہ مینا لکھنوی (رح)",
            "ar" to "حضرة شاه مينا شاه اللكنوي",
            "bn" to "হযরত শাহ মীনা লখনবী (রহ.)"
        ),
        "urs_ahmad_sirhindi" to mapOf(
            "hi" to "मुजद्दिद अल्फ़-ए-सानी हज़रत अहमद सरहिंदी (रह.)",
            "ur" to "مجدد الف ثانی حضرت احمد سرہندی (رح)",
            "ar" to "مجدد الألف الثاني أحمد السرهندي",
            "bn" to "মুজাদ্দিদে আলফে সানী আহমদ সারহিন্দী (রহ.)"
        ),
        "urs_masoom_sirhindi" to mapOf(
            "hi" to "हज़रत ख़्वाजा मुहम्मद मासूम सरहिंदी (रह.)",
            "ur" to "حضرت خواجہ محمد معصوم سرہندی (رح)",
            "ar" to "حضرة محمد معصوم السرهندي",
            "bn" to "হযরত মুহাম্মদ মাসুম সারহিন্দী (রহ.)"
        ),
        "urs_mian_mir" to mapOf(
            "hi" to "हज़रत मियां मीर क़ादरी (रह.)",
            "ur" to "حضرت میاں میر قادری (رح)",
            "ar" to "حضرة ميان مير القادري",
            "bn" to "হযরত মিয়াঁ মীর কাদেরী (রহ.)"
        ),
        "urs_mehr_ali" to mapOf(
            "hi" to "पीर मेहर अली शाह (गोलड़ा शरीफ़)",
            "ur" to "پیر مہر علی شاہ (گولڑہ شریف)",
            "ar" to "بير مهر علي شاه الغولروي",
            "bn" to "পীর মেহের আলী শাহ (গোলড়া)"
        ),
        "urs_barkatullah_marehrawi" to mapOf(
            "hi" to "हज़रत शाह बरकतुल्लाह मारहरवी (रह.)",
            "ur" to "حضرت شاہ برکت اللہ مارہروی (رح)",
            "ar" to "حضرة بركة الله المارهروي",
            "bn" to "হযরত শাহ বরকতুল্লাহ মারাহরাভী (রহ.)"
        ),
        "urs_ibrahim_raza" to mapOf(
            "hi" to "हज़रत मुफ़स्सिर-ए-आज़म इब्राहीम रज़ा ख़ान (जिलानी मियाँ)",
            "ur" to "حضرت مفسر اعظم ابراہیم رضا خان (جیلانی میاں)",
            "ar" to "حضرة إبراهيم رضا خان البريلوي",
            "bn" to "মুফাসসিরে আযম ইব্রাহিম রেজা خان (রহ.)"
        ),
        "urs_khairabadi" to mapOf(
            "hi" to "अल्लामा फ़ज़ल-ए-हक़ ख़ैराबादी (रह.)",
            "ur" to "علامہ فضل حق خیر آبادی (رح)",
            "ar" to "العلامة فضل الحق الخير آبادي",
            "bn" to "আল্লামা ফজল হক খাইরাবাদী (রহ.)"
        ),
        "urs_mirza_janan" to mapOf(
            "hi" to "हज़रत मिर्ज़ा मज़हर जान-ए-जानाँ (रह.)",
            "ur" to "حضرت مرزا مظہر جانِ جاناں (رح)",
            "ar" to "حضرة ميرزا مظهر جان جانان",
            "bn" to "হযরত মির্জা মাজহার জান-ই-জানান (রহ.)"
        ),
        "wiladat_fatimah" to mapOf(
            "hi" to "विलादत सय्यदा फ़ातिमा अज़-ज़हरा (रज़ि.)",
            "ur" to "ولادت سیدہ فاطمۃ الزہراء (رض)",
            "ar" to "ولادة السيدة فاطمة الزهراء (رضي الله عنها)",
            "bn" to "বেলাদত সায়্যিদা ফাতিমা আয-যাহরা (রা.)"
        ),
        "wiladat_ali" to mapOf(
            "hi" to "विलादत अमीरुल मोमिनीन हज़रत अली इब्न अबी तालिब (रज़ि.)",
            "ur" to "ولادت امیر المؤمنین حضرت علی ابن ابی طالب (رض)",
            "ar" to "مولد أمير المؤمنين علي بن أبي طالب (رضي الله عنه)",
            "bn" to "বেলাদত আমীরুল মুমিনীন হযরত আলী (রা.)"
        ),
        "wiladat_hasan" to mapOf(
            "hi" to "विलादत इमाम हसन इब्न अली (रज़ि.)",
            "ur" to "ولادت امام حسن ابن علی (رض)",
            "ar" to "مولد الإمام الحسن بن علي (رضي الله عنهما)",
            "bn" to "বেলাদত ইমাম হাসান ইবনে আলী (রা.)"
        ),
        "wiladat_husayn" to mapOf(
            "hi" to "विलादत इमाम हुसैन इब्न अली (रज़ि.)",
            "ur" to "ولادت امام حسین ابن علی (رض)",
            "ar" to "مولد الإمام الحسين بن علي (رضي الله عنهما)",
            "bn" to "বেলাদত ইমাম হুসাইন ইবনে আলী (রা.)"
        ),
        "shahadat_hussain" to mapOf(
            "hi" to "शहादत सय्यदुश शोहदा इमाम हुसैन (रज़ि.)",
            "ur" to "شہادت سید الشہداء امام حسین (رض)",
            "ar" to "استشهاد الإمام الحسين بن علي (سيد الشهداء)",
            "bn" to "শাহাদাত সায়্যিদুশ শুহাদা ইমাম হুসাইন (রা.)"
        ),
        "wisaal_prophet" to mapOf(
            "hi" to "विसाल-ए-अक़दस नबी-ए-करीम हज़रत मुहम्मद मुस्तफ़ा ﷺ",
            "ur" to "وصال اقدس سرور کائنات حضرت محمد مصطفیٰ ﷺ",
            "ar" to "وفاة النبي الأكرم محمد ﷺ",
            "bn" to "ওফাতুন্নবী হযরত মুহাম্মদ মুস্তফা ﷺ"
        ),
        "wisaal_zayn_abidin" to mapOf(
            "hi" to "विसाल इमाम ज़ैनुल आबिदीन (रह.)",
            "ur" to "وصال امام زین العابدین (رض)",
            "ar" to "وفاة الإمام زين العابدين علي بن الحسين",
            "bn" to "ওফাত ইমাম যাইনুল আবেদীন (রহ.)"
        ),
        "wisaal_bilal" to mapOf(
            "hi" to "विसाल हज़रत बिलाल इब्न रबाह (रज़ि.)",
            "ur" to "وصال حضرت بلال ابن رباح (رض)",
            "ar" to "وفاة بلال بن رباح مؤذن الرسول ﷺ",
            "bn" to "ওফাত হযরত বেলাল ইবনে রাবাহ (রা.)"
        ),
        "wisaal_hasan_askari" to mapOf(
            "hi" to "विसाल इमाम हसन अल-अस्करी (रह.)",
            "ur" to "وصال امام حسن العسکری (رض)",
            "ar" to "وفاة الإمام الحسن العسكري",
            "bn" to "ওফাত ইমাম হাসান আল-আসকারী (রহ.)"
        ),
        "wisaal_ali_ridha" to mapOf(
            "hi" to "विसाल इमाम अली अल-रज़ा (रह.)",
            "ur" to "وصال امام علی الرضا (رض)",
            "ar" to "وفاة الإمام علي الرضا",
            "bn" to "ওফাত ইমাম আলী আল-রেজা (রহ.)"
        ),
        "wisaal_nawawi" to mapOf(
            "hi" to "विसाल इमाम यहया इब्न शरफ़ अन-नवावी (रह.)",
            "ur" to "وصال امام نووی (رح)",
            "ar" to "وفاة الإمام يحيى بن شرف النووي",
            "bn" to "ওফাত ইমাম নববী (রহ.)"
        )
    )

    private val EVENT_SHORTS = mapOf(
        "urs_mahimi" to mapOf("hi" to "मख़दूम...", "ur" to "مخدوم...", "ar" to "مخدوم", "bn" to "মাখদুম..."),
        "urs_moinuddin" to mapOf("hi" to "ग़रीब नवाज़", "ur" to "غریب نواز", "ar" to "معين الدين", "bn" to "গরীব নেওয়াজ"),
        "urs_ghaus_pak" to mapOf("hi" to "ग़ौस-ए-आज़म", "ur" to "غوث اعظم", "ar" to "عبد القادر", "bn" to "বড়পীর"),
        "urs_haji_ali" to mapOf("hi" to "हाजी अली", "ur" to "حاجی علی", "ar" to "حاجي علي", "bn" to "হাজী আলী"),
        "urs_nizamuddin" to mapOf("hi" to "निज़ामुद्दीन", "ur" to "نظام الدین", "ar" to "نظام الدين", "bn" to "নিজামুদ্দিন"),
        "urs_sabir_kalyari" to mapOf("hi" to "साबिर पाक", "ur" to "صابر پاک", "ar" to "صابر كلياري", "bn" to "সাবির পাক"),
        "urs_ganjshakar" to mapOf("hi" to "बाबा फ़रीद", "ur" to "بابا فرید", "ar" to "बाबा فريد", "bn" to "বাবা ফরিদ"),
        "urs_bakhtiyar_kaki" to mapOf("hi" to "बख़्तियार काकी", "ur" to "بختیار کاکی", "ar" to "بختيار كاكي", "bn" to "বখতিয়ার কাকি"),
        "urs_banda_nawaz" to mapOf("hi" to "बंदा नवाज़", "ur" to "بندہ نواز", "ar" to "بندة نواز", "bn" to "বন্দা নেওয়াজ"),
        "urs_ahmed_raza" to mapOf("hi" to "आला हज़रत", "ur" to "اعلیٰ حضرت", "ar" to "أحمد رضا", "bn" to "আলা হযরত"),
        "urs_mustafa_raza" to mapOf("hi" to "मुफ़्ती-ए-आज़म", "ur" to "مفتی اعظم", "ar" to "مصطفى رضا", "bn" to "মুফতিয়ে আযম"),
        "urs_tajuddin" to mapOf("hi" to "ताजुद्दीन", "ur" to "تاج الدین", "ar" to "تاج الدين", "bn" to "তাজউদ্দীন"),
        "urs_waris_ali" to mapOf("hi" to "वारिस पाक", "ur" to "وارث پاک", "ar" to "وارث علي", "bn" to "ওয়ারিস पाक"),
        "urs_mira_datar" to mapOf("hi" to "मीरा दातार", "ur" to "میراں داتار", "ar" to "ميرا داتار", "bn" to "মীরা দাতার"),
        "urs_ali_akbar_datar" to mapOf("hi" to "अकबर दातार", "ur" to "اکبر داتار", "ar" to "علي أكبر", "bn" to "আকবর دাতার"),
        "urs_ashraf_jahangir" to mapOf("hi" to "अशरफ़ सिमनानी", "ur" to "اشرف سمنانی", "ar" to "أشرف سمناني", "bn" to "আশরাফ জাহাঙ্গীর"),
        "urs_bahauddin_naqshband" to mapOf("hi" to "नक़्शबंद", "ur" to "نقشبند", "ar" to "بهاء الدين", "bn" to "নকশবন্দ"),
        "urs_bahauddin_zakariya" to mapOf("hi" to "बहाउद्दीन", "ur" to "بہاؤ الدین", "ar" to "بهاء الدين زكريا", "bn" to "বাহাউদ্দীন"),
        "urs_data_ganj_bakhsh" to mapOf("hi" to "दाता गंज बख़्श", "ur" to "داتا دربار", "ar" to "داتا كنج بخش", "bn" to "দাতা গঞ্জ বখশ"),
        "urs_lal_shahbaz" to mapOf("hi" to "लाल शहबाज़", "ur" to "لعل شہباز", "ar" to "لعل شهباز", "bn" to "লাল শাহবাজ"),
        "urs_shah_jalal" to mapOf("hi" to "शाह जलाल", "ur" to "شاہ جلال", "ar" to "شاه جلال", "bn" to "শাহ জালাল"),
        "urs_shahul_hameed" to mapOf("hi" to "नागोर शरीफ़", "ur" to "ناگور شریف", "ar" to "شاه الحميد", "bn" to "শাহুল হামিদ"),
        "urs_shah_alam" to mapOf("hi" to "शाह आलम", "ur" to "شاہ عالم", "ar" to "شاه عالم", "bn" to "শাহ আলম"),
        "urs_salar_masud" to mapOf("hi" to "ग़ाज़ी मियाँ", "ur" to "غازی میاں", "ar" to "سالار مسعود", "bn" to "সালার মাসুদ"),
        "urs_baba_sharfuddin" to mapOf("hi" to "बाबा शरफ़ुद्दीन", "ur" to "بابا شرف الدین", "ar" to "بابا شرف الدين", "bn" to "বাবা শরফুদ্দীন"),
        "urs_sultan_ibrahim" to mapOf("hi" to "सुल्तान इब्राहीम", "ur" to "سلطان ابراہیم", "ar" to "سلطان إبراهيم", "bn" to "সুলতান ইব্রাহিম"),
        "urs_suhrawardi" to mapOf("hi" to "सोहरवर्दी", "ur" to "سہروردی", "ar" to "السهروردي", "bn" to "সোহরাওয়ার্দী"),
        "urs_maruf_karkhi" to mapOf("hi" to "मारूफ़ करख़ी", "ur" to "معروف کرخی", "ar" to "معروف الكرخي", "bn" to "মারুফ কারখী"),
        "urs_hasan_basri" to mapOf("hi" to "हसन बसरी", "ur" to "حسن بصری", "ar" to "الحسن البصري", "bn" to "হাসান বসরী"),
        "urs_maneri" to mapOf("hi" to "यहया मनेरी", "ur" to "یحییٰ منیری", "ar" to "يحيى المنيري", "bn" to "ইয়াহিয়া মানেরী"),
        "urs_bu_ali_qalandar" to mapOf("hi" to "बू अली क़लंदर", "ur" to "بو علی قلندر", "ar" to "بو علي قلندر", "bn" to "বু علی কলন্দর"),
        "urs_shah_waliullah" to mapOf("hi" to "शाह वलीउल्लाह", "ur" to "شاہ ولی اللہ", "ar" to "شاه ولي الله", "bn" to "শাহ ওয়ালীউল্লাহ"),
        "urs_abdul_haq_dehlvi" to mapOf("hi" to "अब्दुल हक़", "ur" to "عبد الحق دہلوی", "ar" to "عبد الحق الدهلوي", "bn" to "আব্দুল হক"),
        "urs_shah_mina" to mapOf("hi" to "शाह मीना", "ur" to "شاہ مینا", "ar" to "شاه مينا", "bn" to "শাহ মীনা"),
        "urs_ahmad_sirhindi" to mapOf("hi" to "मुजद्दिद पाक", "ur" to "مجدد الف ثانی", "ar" to "أحمد السرهندي", "bn" to "মুজাদ্দিদে আলফে সানী"),
        "urs_masoom_sirhindi" to mapOf("hi" to "मासूम सरहिंदी", "ur" to "محمد معصوم", "ar" to "محمد معصوم", "bn" to "মুহাম্মদ মাসুম"),
        "urs_mian_mir" to mapOf("hi" to "मियां मीर", "ur" to "میاں میر", "ar" to "ميان مير", "bn" to "মিয়াঁ মীর"),
        "urs_mehr_ali" to mapOf("hi" to "मेहर अली शाह", "ur" to "مہر علی شاہ", "ar" to "مهر علي شاہ", "bn" to "মেহের আলী শাহ"),
        "urs_barkatullah_marehrawi" to mapOf("hi" to "बरकतुल्लाह", "ur" to "برکت اللہ", "ar" to "بركة الله", "bn" to "বরকতুল্লাহ"),
        "urs_ibrahim_raza" to mapOf("hi" to "जिलानी मियाँ", "ur" to "جیلانی میاں", "ar" to "إبراهيم رضا", "bn" to "জিলানী মিঞা"),
        "urs_khairabadi" to mapOf("hi" to "फ़ज़ले हक़", "ur" to "فضل حق", "ar" to "فضل الحق", "bn" to "ফজল হক"),
        "urs_mirza_janan" to mapOf("hi" to "जान-ए-जानाँ", "ur" to "جانِ جاناں", "ar" to "جان جانان", "bn" to "জান-ই-জানান"),
        "wiladat_fatimah" to mapOf("hi" to "सय्यदा फ़ातिमा", "ur" to "سیدہ فاطمہ", "ar" to "فاطمة الزهراء", "bn" to "সায়্যিদা ফাতিমা"),
        "wiladat_ali" to mapOf("hi" to "मौला अली", "ur" to "مولا علی", "ar" to "علي بن أبي طالب", "bn" to "হযরত আলী"),
        "wiladat_hasan" to mapOf("hi" to "इमाम हसन", "ur" to "امام حسن", "ar" to "الإمام الحسن", "bn" to "ইমাম হাসান"),
        "wiladat_husayn" to mapOf("hi" to "इमाम हुसैन", "ur" to "امام حسین", "ar" to "الإمام الحسين", "bn" to "ইমাম হুসাইন"),
        "shahadat_hussain" to mapOf("hi" to "शहादत इमाम हुसैन", "ur" to "شہادت امام حسین", "ar" to "عاشوراء الحسين", "bn" to "শাহাদাত ইমাম হুসাইন"),
        "wisaal_prophet" to mapOf("hi" to "विसाल-ए-नबी ﷺ", "ur" to "وصال نبی ﷺ", "ar" to "وفاة النبي ﷺ", "bn" to "ওফাতুন্নবী ﷺ"),
        "wisaal_zayn_abidin" to mapOf("hi" to "ज़ैनुल आबिदीन", "ur" to "زین العابدین", "ar" to "زين العابدين", "bn" to "যাইনুল আবেদীন"),
        "wisaal_bilal" to mapOf("hi" to "हज़रत बिलाल", "ur" to "حضرت بلال", "ar" to "بلال بن رباح", "bn" to "হযরত বেলাল"),
        "wisaal_hasan_askari" to mapOf("hi" to "हसन अल-अस्करी", "ur" to "حسن عسکری", "ar" to "الحسن العسكري", "bn" to "হাসান আসকারী"),
        "wisaal_ali_ridha" to mapOf("hi" to "अली अल-रज़ा", "ur" to "علی رضا", "ar" to "علي الرضا", "bn" to "আলী রেজা"),
        "wisaal_nawawi" to mapOf("hi" to "इमाम नवावी", "ur" to "امام نووی", "ar" to "الإمام النووي", "bn" to "ইমাম নববী")
    )

    fun getEventName(id: String, defaultName: String, locale: String): String {
        val entry = EVENT_NAMES[id] ?: return defaultName
        entry[locale]?.let { return it }
        if (locale in listOf("mr", "gu", "pa", "or", "as")) {
            entry["hi"]?.let { return it }
        }
        if (locale in listOf("fa")) {
            (entry["ur"] ?: entry["ar"])?.let { return it }
        }
        return entry["en"] ?: defaultName
    }

    fun getEventShort(id: String, defaultName: String, locale: String): String {
        val entry = EVENT_SHORTS[id] ?: return ""
        entry[locale]?.let { return it }
        if (locale in listOf("mr", "gu", "pa", "or", "as")) {
            entry["hi"]?.let { return it }
        }
        if (locale in listOf("fa")) {
            (entry["ur"] ?: entry["ar"])?.let { return it }
        }
        return entry["en"] ?: ""
    }

    fun getEventLocation(rawLoc: String, locale: String): String {
        if (rawLoc.isBlank()) return ""
        val trimmed = rawLoc.trim()
        val locMap = mapOf(
            "Mahim, Mumbai, Maharashtra, India" to mapOf(
                "hi" to "माहिम, मुंबई, महाराष्ट्र, भारत",
                "ur" to "ماہم، ممبئی، مہاراشٹر، بھارت",
                "ar" to "ماهيم، مومباي، الهند",
                "bn" to "মাহিম, মুম্বাই, মহারাষ্ট্র, ভারত",
                "gu" to "માહિમ, મુંબઈ, મહારાષ્ટ્ર, ભારત",
                "mr" to "माहिम, मुंबई, महाराष्ट्र, भारत"
            ),
            "Ajmer Sharif, Rajasthan, India" to mapOf(
                "hi" to "अजमेर शरीफ़, राजस्थान, भारत",
                "ur" to "اجمیر شریف، راجستھان، بھارت",
                "ar" to "أجمير الشريف، راجستان، الهند",
                "bn" to "আজমির শরীফ, রাজস্থান, ভারত",
                "gu" to "અજમેર શરીફ, રાજસ્થાન, ભારત",
                "mr" to "अजमेर शरीफ, राजस्थान, भारत"
            ),
            "Baghdad, Iraq" to mapOf(
                "hi" to "बग़दाद शरीफ़, इराक़",
                "ur" to "بغداد شریف، عراق",
                "ar" to "بغداد، العراق",
                "bn" to "বাগদাদ, ইরাক",
                "gu" to "બગદાદ શરીફ, ઇરાક",
                "mr" to "बगदाद शरीफ, इराक"
            ),
            "Basra, Iraq" to mapOf(
                "hi" to "बसरा, इराक़",
                "ur" to "بصرہ، عراق",
                "ar" to "البصرة، العراق",
                "bn" to "বসরা, ইরাক"
            ),
            "Makkah al-Mukarramah" to mapOf(
                "hi" to "मक्का अल-मुकर्रमा",
                "ur" to "مکہ مکرمہ",
                "ar" to "مكة المكرمة",
                "bn" to "মক্কা আল-মুকাররমা"
            ),
            "Madinah al-Munawwarah" to mapOf(
                "hi" to "मदीना अल-मुनव्वरा",
                "ur" to "مدینہ منورہ",
                "ar" to "المدينة المنورة",
                "bn" to "মদিনা আল-মুনাওয়ারা"
            ),
            "Madinah, Saudi Arabia" to mapOf(
                "hi" to "मदीना शरीफ़, सऊदी अरब",
                "ur" to "مدینہ شریف، سعودی عرب",
                "ar" to "المدينة المنورة، المملكة العربية السعودية",
                "bn" to "মদিনা শরীফ, সৌদি আরব"
            ),
            "Karbala, Iraq" to mapOf(
                "hi" to "कर्बला मुअल्ला, इराक़",
                "ur" to "کربلا معلی، عراق",
                "ar" to "كربلاء المقدسة، العراق",
                "bn" to "কারবালা, ইরাক"
            ),
            "Bareilly, Uttar Pradesh, India" to mapOf(
                "hi" to "बरेली शरीफ़, उत्तर प्रदेश, भारत",
                "ur" to "بریلی شریف، اتر پردیش، بھارت",
                "ar" to "بريالي الشريف، أتر برديش، الهند",
                "bn" to "বেরেলি শরীফ, উত্তর প্রদেশ, भारत"
            ),
            "Delhi, India" to mapOf(
                "hi" to "दिल्ली, भारत",
                "ur" to "دہلی، بھارت",
                "ar" to "دلهي، الهند",
                "bn" to "দিল্লি, ভারত"
            ),
            "Mehrauli, Delhi, India" to mapOf(
                "hi" to "महरौली, दिल्ली, भारत",
                "ur" to "مہرولی، دہلی، بھارت",
                "ar" to "مهرولي، دلهي، الهند",
                "bn" to "মেহরাউলি, দিল্লি, ভারত"
            ),
            "Nizamuddin, Delhi, India" to mapOf(
                "hi" to "निज़ामुद्दीन औलिया दरगाह, दिल्ली, भारत",
                "ur" to "نظام الدین اولیاء درگاہ، دہلی، بھارت",
                "ar" to "ضريح نظام الدين، دلهي، الهند",
                "bn" to "নিজামুদ্দিন আউলিয়া দরগাহ, দিল্লি, ভারত"
            ),
            "Haji Ali Dargah, Mumbai, Maharashtra, India" to mapOf(
                "hi" to "हाजी अली दरगाह, मुंबई, महाराष्ट्र, भारत",
                "ur" to "حاجی علی درگاہ، ممبئی، مہاراشٹر، بھارت",
                "ar" to "ضريح حاجي علي، مومباي، الهند",
                "bn" to "হাজী আলী দরগাহ, মুম্বাই, মহারাষ্ট্র, ভারত"
            ),
            "Nagore, Tamil Nadu, India" to mapOf(
                "hi" to "नागोर शरीफ़, तमिलनाडु, भारत",
                "ur" to "ناگور شریف، تمل ناڈو، بھارت",
                "ar" to "ناغور الشريف، تاميل نادو، الهند",
                "bn" to "নাগোর শরীফ, তামিলনাড়ু, ভারত"
            ),
            "Gulbarga, Karnataka, India" to mapOf(
                "hi" to "गुलबर्गा शरीफ़, कर्नाटक, भारत",
                "ur" to "گلبرگہ شریف، کرناٹک، بھارت",
                "ar" to "جلبَرجة الشريف، كارناتاكا، الهند",
                "bn" to "গুলবার্গা শরীফ, কর্ণাটক, ভারত"
            ),
            "Kaliyar Sharif, Uttarakhand, India" to mapOf(
                "hi" to "कलियर शरीफ़, उत्तराखंड, भारत",
                "ur" to "کلیئر شریف، اتراکھنڈ، بھارت",
                "ar" to "كلير الشريف، أتراكند، الهند",
                "bn" to "কালিয়ার শরীফ, উত্তরাখণ্ড, ভারত"
            ),
            "Pakpattan, Pakistan" to mapOf(
                "hi" to "पाकपट्टन शरीफ़, पाकिस्तान",
                "ur" to "پاکپتن شریف، پاکستان",
                "ar" to "باكبتن الشريف، باكستان",
                "bn" to "পাকপত্তন শরীফ, পাকিস্তান"
            ),
            "Lahore, Pakistan" to mapOf(
                "hi" to "लाहौर, पाकिस्तान",
                "ur" to "لاہور، پاکستان",
                "ar" to "لاهور، باكستان",
                "bn" to "লাহোর, পাকিস্তান"
            ),
            "Multan, Pakistan" to mapOf(
                "hi" to "मुल्तान शरीफ़, पाकिस्तान",
                "ur" to "ملتان شریف، پاکستان",
                "ar" to "ملتان الشريف، باكستان",
                "bn" to "মুলতান শরীফ, পাকিস্তান"
            ),
            "Golra Sharif, Islamabad, Pakistan" to mapOf(
                "hi" to "गोलड़ा शरीफ़, इस्लामाबाद, पाकिस्तान",
                "ur" to "گولڑہ شریف، اسلام آباد، پاکستان",
                "ar" to "غولرة الشريف، إسلام آباد، باكستان",
                "bn" to "গোলড়া শরীফ, ইসলামাবাদ, পাকিস্তান"
            ),
            "Sehwan Sharif, Sindh, Pakistan" to mapOf(
                "hi" to "सेहवन शरीफ़, सिंध, पाकिस्तान",
                "ur" to "سیہون شریف، سندھ، پاکستان",
                "ar" to "سيهوان الشريف، السند، باكستان",
                "bn" to "সেহওয়ান শরীফ, সিন্ধু, পাকিস্তান"
            ),
            "Sylhet, Bangladesh" to mapOf(
                "hi" to "सिलहट शरीफ़, बांग्लादेश",
                "ur" to "سلہٹ شریف، بنگلہ دیش",
                "ar" to "سلهيت الشريف، بنغلاديش",
                "bn" to "সিলেট শরীফ, বাংলাদেশ"
            ),
            "Unava Sharif, Gujarat, India" to mapOf(
                "hi" to "उनवा शरीफ़, गुजरात, भारत",
                "ur" to "اوناوا شریف، گجرات، بھارت",
                "ar" to "أونافا الشريف، غوجارات، الهند",
                "bn" to "উনাভা শরীফ, গুজরাট, ভারত",
                "gu" to "ઉનાવા શરીફ, ગુજરાત, ભારત"
            ),
            "Unava, Mehsana, Gujarat, India" to mapOf(
                "hi" to "उनवा, मेहसाणा, गुजरात, भारत",
                "ur" to "اوناوا، مہسانہ، گجرات، بھارت",
                "ar" to "أونافا، غوجارات، الهند",
                "bn" to "উনাভা, মেহসানা, গুজরাট, ভারত",
                "gu" to "ઉનાવા, મહેસાણા, ગુજરાત, ભારત"
            ),
            "Dewa Sharif, Barabanki, Uttar Pradesh, India" to mapOf(
                "hi" to "देवा शरीफ़, बाराबंकी, उत्तर प्रदेश, भारत",
                "ur" to "دیوہ شریف، بارہ بنکی، اتر پردیش، بھارت",
                "ar" to "ديوا الشريف، أتر برديش، الهند",
                "bn" to "দেওয়া শরীফ, উত্তর প্রদেশ, ভারত"
            ),
            "Kichaucha Sharif, Uttar Pradesh, India" to mapOf(
                "hi" to "किछौछा शरीफ़, उत्तर प्रदेश, भारत",
                "ur" to "کچھوچھہ شریف، اتر پردیش، بھارت",
                "ar" to "كشوشة الشريف، أتر برديش، الهند",
                "bn" to "কিছৌছা শরীফ, উত্তর প্রদেশ, ভারত"
            ),
            "Tajbagh, Nagpur, Maharashtra, India" to mapOf(
                "hi" to "ताजबाग़ शरीफ़, नागपुर, महाराष्ट्र, भारत",
                "ur" to "تاج باغ شریف، ناگپور، مہاراشٹر، بھارت",
                "ar" to "تاج باغ الشريف، ناغبور، الهند",
                "bn" to "তাজবাগ শরীফ, নাগপুর, ভারত"
            ),
            "Bihar Sharif, Bihar, India" to mapOf(
                "hi" to "बिहार शरीफ़, बिहार, भारत",
                "ur" to "بہار شریف، بہار، بھارت",
                "ar" to "بيهار الشريف، الهند",
                "bn" to "বিহার শরীফ, বিহার, ভারত"
            ),
            "Sirhind, Punjab, India" to mapOf(
                "hi" to "सरहिंद शरीफ़, पंजाब, भारत",
                "ur" to "سرہند شریف، پنجاب، بھارت",
                "ar" to "سرهند الشريف، البنجاب، الهند",
                "bn" to "সারহিন্দ শরীফ, পাঞ্জাব, ভারত"
            ),
            "Bahraich, Uttar Pradesh, India" to mapOf(
                "hi" to "बहराइच शरीफ़, उत्तर प्रदेश, भारत",
                "ur" to "بہڑائچ شریف، اتر پردیش، بھارت",
                "ar" to "بهرائج الشريف، أتر برديش، الهند",
                "bn" to "বাহরাইচ শরীফ, উত্তর প্রদেশ, ভারত"
            ),
            "Panipat, Haryana, India" to mapOf(
                "hi" to "पानीपत शरीफ़, हरियाणा, भारत",
                "ur" to "پانی پت شریف، ہریانہ، بھارت",
                "ar" to "بانيبات الشريف، هاريانا، الهند",
                "bn" to "পানিপথ শরীফ, হরিয়ানা, ভারত"
            ),
            "Ervadi, Tamil Nadu, India" to mapOf(
                "hi" to "एरवाड़ी शरीफ़, तमिलनाडु, भारत",
                "ur" to "ایروادی شریف، تمل ناڈو، بھارت",
                "ar" to "إروادي الشريف، تاميل نادو، الهند",
                "bn" to "এরভাদি শরীফ, তামিলনাড়ু, ভারত"
            ),
            "Pahadi Shareef, Hyderabad, Telangana, India" to mapOf(
                "hi" to "पहाड़ी शरीफ़, हैदराबाद, तेलंगाना, भारत",
                "ur" to "پہاڑی شریف، حیدرآباد، تلنگانہ، بھارت",
                "ar" to "بهادي الشريف، حيدر آباد، الهند",
                "bn" to "পাহাড়ি শরীফ, হায়দ্রাবাদ, ভারত"
            ),
            "Shah Alam Roza, Ahmedabad, Gujarat, India" to mapOf(
                "hi" to "शाह आलम रोज़ा, अहमदाबाद, गुजरात, भारत",
                "ur" to "شاہ عالم روضہ، احمد آباد، گجرات، بھارت",
                "ar" to "روضة شاه عالم، أحمد آباد، الهند",
                "bn" to "শাহ আলম রওজা, আহমেদাবাদ, ভারত",
                "gu" to "શાહ આલમ રોઝા, અમદાવાદ, ગુજરાત, ભારત"
            ),
            "Damascus, Syria" to mapOf(
                "hi" to "दमिश्क, सीरिया",
                "ur" to "دمشق، شام",
                "ar" to "دمشق، سوريا",
                "bn" to "দামেস্ক, সিরিয়া"
            ),
            "Samarra, Iraq" to mapOf(
                "hi" to "सामर्रा, इराक़",
                "ur" to "سامرا، عراق",
                "ar" to "سامراء، العراق",
                "bn" to "সামাররা, ইরাক"
            ),
            "Bukhara, Uzbekistan" to mapOf(
                "hi" to "बुख़ारा, उज़्बेकिस्तान",
                "ur" to "بخارا، ازبکستان",
                "ar" to "بخارى، أوزبكستان",
                "bn" to "বুখারা, উজবেকিস্তান"
            ),
            "Mashhad, Iran" to mapOf(
                "hi" to "मशहद, ईरान",
                "ur" to "مشہد، ایران",
                "ar" to "مشهد، إيران",
                "bn" to "মাশহাদ, ইরান"
            ),
            "Khairabad, Uttar Pradesh, India" to mapOf(
                "hi" to "ख़ैराबाद शरीफ़, उत्तर प्रदेश, भारत",
                "ur" to "خیر آباد شریف، اتر پردیش، بھارت",
                "ar" to "خير آباد، أتر برديش، الهند",
                "bn" to "খাইরাবাদ শরীফ, উত্তর প্রদেশ, ভারত"
            ),
            "Lucknow, Uttar Pradesh, India" to mapOf(
                "hi" to "लखनऊ, उत्तर प्रदेश, भारत",
                "ur" to "لکھنؤ، اتر پردیش، بھارت",
                "ar" to "لكهنؤ، أتر برديش، الهند",
                "bn" to "লখনৌ, উত্তর প্রদেশ, ভারত"
            ),
            "Marehra, Uttar Pradesh, India" to mapOf(
                "hi" to "मारहरा शरीफ़, उत्तर प्रदेश, भारत",
                "ur" to "مارہرہ شریف، اتر پردیش، بھارت",
                "ar" to "مارهره الشريف، أتر برديش، الهند",
                "bn" to "মারাহরা শরীফ, উত্তর প্রদেশ, ভারত"
            ),
            "Devotional observance in South Asia" to mapOf(
                "hi" to "दक्षिण एशिया में अक़ीदत",
                "ur" to "جنوبی ایشیا میں عقیدت",
                "ar" to "احتفال ديني في جنوب آسيا",
                "bn" to "দক্ষিণ এশিয়ায় ভক্তিমূলক অনুষ্ঠান"
            ),
            "Mira Datar devotion observed in South Asia" to mapOf(
                "hi" to "मीरा दातार अक़ीदत, दक्षिण एशिया",
                "ur" to "میراں داتار عقیدت، جنوبی ایشیا",
                "ar" to "احتفال ديني بميرا داتار في جنوب آسيا",
                "bn" to "মীরা দাতার ভক্তিমূলক অনুষ্ঠান"
            )
        )

        val target = locMap[trimmed] ?: return trimmed
        val candidate = target[locale]
            ?: if (locale in listOf("mr", "gu", "pa", "or", "as")) target["hi"]
            else if (locale in listOf("fa")) target["ur"] ?: target["ar"]
            else target["en"]
        return candidate ?: trimmed
    }

}
