package com.tasbih.digitaltasbih.data

data class HomeCardsStrings(
    val upcomingTitle: String,
    val upcomingEventsWord: String,
    val upcomingSubtitle: String,
    val viewAll: String,
    val days: String,
    val duaTitle: String,
    val duaSubtitle: String,
    val dailySupplication: String,
    val authenticHadithRef: String,
    val narratedBy: String,
    val translationLabel: String,
    val viewOnSunnah: String
)

object HomeCardsLocalization {

    fun get(locale: String): HomeCardsStrings {
        return when (locale) {
            "hi" -> HomeCardsStrings(
                upcomingTitle = "आगामी",
                upcomingEventsWord = "आयोजन",
                upcomingSubtitle = "जुड़े रहें • कोई मौका न छूटे",
                viewAll = "सभी देखें →",
                days = "दिन",
                duaTitle = "दुआ",
                duaSubtitle = "माँगें • यक़ीन रखें • पाएँ",
                dailySupplication = "दैनिक दुआ",
                authenticHadithRef = "प्रमाणिक हदीस संदर्भ",
                narratedBy = "रावी:",
                translationLabel = "अनुवाद:",
                viewOnSunnah = "Sunnah.com पर आधिकारिक स्रोत देखें ↗"
            )
            "ur" -> HomeCardsStrings(
                upcomingTitle = "آئندہ",
                upcomingEventsWord = "واقعات",
                upcomingSubtitle = "جڑے رہیں • کوئی لمحہ نہ چھوڑیں",
                viewAll = "سب دیکھیں →",
                days = "دن",
                duaTitle = "دعا",
                duaSubtitle = "مانگیں • یقین رکھیں • پائیں",
                dailySupplication = "روزانہ کی دعا",
                authenticHadithRef = "مستند حدیث کا حوالہ",
                narratedBy = "راوی:",
                translationLabel = "ترجمہ:",
                viewOnSunnah = "Sunnah.com پر آفیشل ماخذ دیکھیں ↗"
            )
            "ar" -> HomeCardsStrings(
                upcomingTitle = "المناسبات",
                upcomingEventsWord = "القادمة",
                upcomingSubtitle = "ابق على اتصال • لا تفوت المناسبة",
                viewAll = "عرض الكل ←",
                days = "أيام",
                duaTitle = "دعاء",
                duaSubtitle = "ادعُ • آمِن • استجب",
                dailySupplication = "دعاء اليوم",
                authenticHadithRef = "مرجع الحديث الصحيح",
                narratedBy = "رواه:",
                translationLabel = "الترجمة:",
                viewOnSunnah = "عرض المصدر الرسمي على Sunnah.com ↗"
            )
            "bn" -> HomeCardsStrings(
                upcomingTitle = "আসন্ন",
                upcomingEventsWord = "অনুষ্ঠান",
                upcomingSubtitle = "সংযুক্ত থাকুন • কোনো সুযোগ মিস করবেন না",
                viewAll = "সব দেখুন →",
                days = "দিন",
                duaTitle = "দোয়া",
                duaSubtitle = "চান • বিশ্বাস রাখুন • পান",
                dailySupplication = "দৈনিক দোয়া",
                authenticHadithRef = "সহীহ হাদিসের রেফারেন্স",
                narratedBy = "বর্ণনাকারী:",
                translationLabel = "অনুবাদ:",
                viewOnSunnah = "Sunnah.com-এ অফিসিয়াল উৎস দেখুন ↗"
            )
            "tr" -> HomeCardsStrings(
                upcomingTitle = "Yaklaşan",
                upcomingEventsWord = "Etkinlikler",
                upcomingSubtitle = "BAĞLANTIDA KALIN • KAÇIRMAYIN",
                viewAll = "Tümünü Gör →",
                days = "Gün",
                duaTitle = "Dua",
                duaSubtitle = "İSTE • İNAN • AL",
                dailySupplication = "Günlük Dua",
                authenticHadithRef = "SAHİH HADİS KAYNAĞI",
                narratedBy = "Ravi:",
                translationLabel = "TERCÜME:",
                viewOnSunnah = "Sunnah.com'da Resmi Kaynağı Gör ↗"
            )
            "id" -> HomeCardsStrings(
                upcomingTitle = "Acara",
                upcomingEventsWord = "Mendatang",
                upcomingSubtitle = "TETAP TERHUBUNG • JANGAN LEWATKAN",
                viewAll = "Lihat Semua →",
                days = "Hari",
                duaTitle = "Doa",
                duaSubtitle = "MINTA • YAKIN • TERIMA",
                dailySupplication = "Doa Harian",
                authenticHadithRef = "REFERENSI HADIS SHAHIH",
                narratedBy = "Diriwayatkan oleh:",
                translationLabel = "TERJEMAHAN:",
                viewOnSunnah = "Lihat Sumber Resmi di Sunnah.com ↗"
            )
            // Marathi, Gujarati, Punjabi, Odia, Assamese fallbacks
            "mr", "gu", "pa", "or", "as" -> HomeCardsStrings(
                upcomingTitle = "आगामी",
                upcomingEventsWord = "आयोजन",
                upcomingSubtitle = "जुड़े रहें • कोई मौका न छूटे",
                viewAll = "सभी देखें →",
                days = "दिन",
                duaTitle = "दुआ",
                duaSubtitle = "माँगें • यक़ीन रखें • पाएँ",
                dailySupplication = "दैनिक दुआ",
                authenticHadithRef = "प्रमाणिक हदीस संदर्भ",
                narratedBy = "रावी:",
                translationLabel = "अनुवाद:",
                viewOnSunnah = "Sunnah.com पर आधिकारिक स्रोत देखें ↗"
            )
            // Persian, Pashto, Kurdish fallbacks
            "fa", "ps", "ku" -> HomeCardsStrings(
                upcomingTitle = "آئندہ",
                upcomingEventsWord = "واقعات",
                upcomingSubtitle = "جڑے رہیں • کوئی لمحہ نہ چھوڑیں",
                viewAll = "سب دیکھیں →",
                days = "دن",
                duaTitle = "دعا",
                duaSubtitle = "مانگیں • یقین رکھیں • پائیں",
                dailySupplication = "روزانہ کی دعا",
                authenticHadithRef = "مستند حدیث کا حوالہ",
                narratedBy = "راوی:",
                translationLabel = "ترجمہ:",
                viewOnSunnah = "Sunnah.com پر آفیشل ماخذ دیکھیں ↗"
            )
            // Default English
            else -> HomeCardsStrings(
                upcomingTitle = "Upcoming",
                upcomingEventsWord = "Events",
                upcomingSubtitle = "STAY CONNECTED • DON'T MISS OUT",
                viewAll = "View all →",
                days = "Days",
                duaTitle = "Dua",
                duaSubtitle = "ASK • BELIEVE • RECEIVE",
                dailySupplication = "Daily Supplication",
                authenticHadithRef = "AUTHENTIC HADITH REFERENCE",
                narratedBy = "Narrated by:",
                translationLabel = "TRANSLATION:",
                viewOnSunnah = "View on Sunnah.com / Official Source ↗"
            )
        }
    }
}
