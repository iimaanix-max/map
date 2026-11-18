package com.tasbih.digitaltasbih.util

import android.content.Context
import android.content.Intent
import com.tasbih.digitaltasbih.data.DailyDuaData
import com.tasbih.digitaltasbih.data.DuaEntry

/**
 * Universal, Highly-Localized Share Engine for Duas.
 * Formats every shared Dua with:
 * 1. Salam (السلام عليكم ورحمة الله وبركاته)
 * 2. Purpose / Title (User Language)
 * 3. Arabic Dua
 * 4. Transliteration (Pronunciation in User Language)
 * 5. Translation (Meaning in User Language)
 * 6. How to Recite / Padhne Ka Tarika (if present)
 * 7. Virtues & Benefits / Fayde (if present)
 * 8. Hadith / Quran Reference (if present)
 * 9. Localized Call-to-Action + Google Play Store Link
 */
object DuaShareHelper {

    private const val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=com.tasbih.digitaltasbih"
    private const val SALAM_ARABIC = "السلام عليكم ورحمة الله وبركاته"

    data class ShareTemplate(
        val purposeLabel: String,
        val transliterationLabel: String,
        val translationLabel: String,
        val howToReciteLabel: String,
        val benefitsLabel: String,
        val referenceLabel: String,
        val ctaText: String,
        val shareChooserTitle: String
    )

    fun getTemplate(lang: String): ShareTemplate {
        val l = lang.lowercase().trim()
        return when {
            l.startsWith("hi") && !l.startsWith("hinglish") -> ShareTemplate(
                purposeLabel = "🤲 दुआ:",
                transliterationLabel = "🔤 उच्चारण (Transliteration):",
                translationLabel = "📖 तर्जुमा (अर्थ):",
                howToReciteLabel = "✨ पढ़ने का तरीका:",
                benefitsLabel = "🌟 फ़ज़ीलत व फ़ायदे:",
                referenceLabel = "📚 सनद (Reference):",
                ctaText = "ऐसी ही और बेहतरीन दुआएं पढ़ने और सुनने के लिए डाउनलोड करें डिजिटल तस्बीह ऐप 📲",
                shareChooserTitle = "दुआ शेयर करें"
            )
            l.startsWith("ur") -> ShareTemplate(
                purposeLabel = "🤲 دعا برائے:",
                transliterationLabel = "🔤 تلفظ (Transliteration):",
                translationLabel = "📖 ترجمہ:",
                howToReciteLabel = "✨ پڑھنے کا طریقہ:",
                benefitsLabel = "🌟 فضیلت اور فوائد:",
                referenceLabel = "📚 حوالہ (Reference):",
                ctaText = "ایسی ہی اور بہترین دعائیں پڑھنے اور سننے کے لیے ڈیجیٹل تسبیح ایپ ڈاؤن لوڈ کریں 📲",
                shareChooserTitle = "دعا شیئر کریں"
            )
            l.startsWith("hinglish") -> ShareTemplate(
                purposeLabel = "🤲 Dua:",
                transliterationLabel = "🔤 Pronunciation (Transliteration):",
                translationLabel = "📖 Tarjuma (Translation):",
                howToReciteLabel = "✨ Padhne Ka Tarika:",
                benefitsLabel = "🌟 Fazilat & Fayde:",
                referenceLabel = "📚 Reference:",
                ctaText = "Aise hi aur behtareen duayein padhne aur sunne ke liye download karein Digital Tasbih App 📲",
                shareChooserTitle = "Share Dua via WhatsApp"
            )
            l.startsWith("ar") -> ShareTemplate(
                purposeLabel = "🤲 الدعاء:",
                transliterationLabel = "🔤 اللفظ الصوتي:",
                translationLabel = "📖 المعنى والتفسير:",
                howToReciteLabel = "✨ طريقة القراءة:",
                benefitsLabel = "🌟 الفضل والمنافع:",
                referenceLabel = "📚 المصدر:",
                ctaText = "لقراءة والاستماع للمزيد من الأدعية المباركة، حمّل تطبيق التسبيح الرقمي 📲",
                shareChooserTitle = "مشاركة الدعاء"
            )
            l.startsWith("mr") -> ShareTemplate(
                purposeLabel = "🤲 दुआ:",
                transliterationLabel = "🔤 उच्चारण:",
                translationLabel = "📖 अर्थ (भाषांतर):",
                howToReciteLabel = "✨ वाचण्याची पद्धत:",
                benefitsLabel = "🌟 फायदे व महत्त्व:",
                referenceLabel = "📚 संदर्भ:",
                ctaText = "अशाच आणखी उत्तम दुआ वाचण्यासाठी आणि ऐकण्यासाठी डिजिटल तस्बीह ॲप डाउनलोड करा 📲",
                shareChooserTitle = "दुआ शेअर करा"
            )
            l.startsWith("gu") -> ShareTemplate(
                purposeLabel = "🤲 દુઆ:",
                transliterationLabel = "🔤 ઉચ્ચારણ:",
                translationLabel = "📖 તરજુમા (અર્થ):",
                howToReciteLabel = "✨ વાંચવાની રીત:",
                benefitsLabel = "🌟 ફઝીલત અને ફાયદા:",
                referenceLabel = "📚 સંદર્ભ:",
                ctaText = "આવી જ વધુ ઉત્તમ દુઆઓ વાંચવા અને સાંભળવા માટે ડિજિટલ તસ્બીહ એપ ડાઉનલોડ કરો 📲",
                shareChooserTitle = "દુઆ શેર કરો"
            )
            l.startsWith("bn") -> ShareTemplate(
                purposeLabel = "🤲 দোয়া:",
                transliterationLabel = "🔤 উচ্চারণ:",
                translationLabel = "📖 অনুবাদ:",
                howToReciteLabel = "✨ পড়ার নিয়ম:",
                benefitsLabel = "🌟 ফজিলত ও উপকারিতা:",
                referenceLabel = "📚 তথ্যসূত্র:",
                ctaText = "এমন আরও বরকতময় দোয়া পড়তে ও শুনতে ডিজিটাল তাসবিহ অ্যাপ ডাউনলোড করুন 📲",
                shareChooserTitle = "দোয়া শেয়ার করুন"
            )
            l.startsWith("as") -> ShareTemplate(
                purposeLabel = "🤲 দোয়া:",
                transliterationLabel = "🔤 উচ্চাৰণ:",
                translationLabel = "📖 অনুবাদ:",
                howToReciteLabel = "✨ পঢ়াৰ নিয়ম:",
                benefitsLabel = "🌟 ফজিলত আৰু উপকাৰিতা:",
                referenceLabel = "📚 তথ্যসূত্ৰ:",
                ctaText = "এনেকুৱা অধিক বৰকতময় দুআ পঢ়িবলৈ আৰু শুনিবলৈ ডিজিটেল তাচবিহ এপ ডাউনল’ড কৰক 📲",
                shareChooserTitle = "দোয়া শ্বেয়াৰ কৰক"
            )
            l.startsWith("ta") -> ShareTemplate(
                purposeLabel = "🤲 துஆ:",
                transliterationLabel = "🔤 உச்சரிப்பு:",
                translationLabel = "📖 பொருள் (மொழிபெயர்ப்பு):",
                howToReciteLabel = "✨ ஓதும் முறை:",
                benefitsLabel = "🌟 நன்மைகள் & சிறப்புகள்:",
                referenceLabel = "📚 ஆதாரம்:",
                ctaText = "இன்னும் பல துஆக்களை படிக்கவும் கேட்கவும் டிஜிட்டல் தஸ்பீஹ் செயலியை பதிவிறக்கவும் 📲",
                shareChooserTitle = "துஆவைப் பகிரவும்"
            )
            l.startsWith("te") -> ShareTemplate(
                purposeLabel = "🤲 దువా:",
                transliterationLabel = "🔤 ఉచ్చారణ:",
                translationLabel = "📖 అర్థం (అనువాదం):",
                howToReciteLabel = "✨ చదివే విధానం:",
                benefitsLabel = "🌟 ఫలితాలు & ప్రయోజనాలు:",
                referenceLabel = "📚 మూలం:",
                ctaText = "ఇలాంటి మరిన్ని ఉత్తమ దువాలను చదవడానికి మరియు వినడానికి డిజిటల్ తస్బీహ్ యాప్‌ను డౌన్‌లోడ్ చేసుకోండి 📲",
                shareChooserTitle = "దువాను షేర్ చేయండి"
            )
            l.startsWith("kn") -> ShareTemplate(
                purposeLabel = "🤲 ದುಆ:",
                transliterationLabel = "🔤 ಉಚ್ಚಾರಣೆ:",
                translationLabel = "📖 ಅರ್ಥ (ಅನುವಾದ):",
                howToReciteLabel = "✨ ಓದುವ ವಿಧಾನ:",
                benefitsLabel = "🌟 ಫಲಗಳು ಮತ್ತು ಪ್ರಯೋಜನಗಳು:",
                referenceLabel = "📚 ಆಧಾರ:",
                ctaText = "ಇಂತಹ ಇನ್ನಷ್ಟು ಉತ್ತಮ ದುಆಗಳನ್ನು ಓದಲು ಮತ್ತು ಕೇಳಲು ಡಿಜಿಟಲ್ ತಸ್ಬೀಹ್ ಆ್ಯಪ್ ಡೌನ್‌ಲೋಡ್ ಮಾಡಿ 📲",
                shareChooserTitle = "ದುಆ ಹಂಚಿಕೊಳ್ಳಿ"
            )
            l.startsWith("ml") -> ShareTemplate(
                purposeLabel = "🤲 പ്രാർത്ഥന:",
                transliterationLabel = "🔤 ഉച്ചാരണം:",
                translationLabel = "📖 അർത്ഥം:",
                howToReciteLabel = "✨ വായിക്കേണ്ട രീതി:",
                benefitsLabel = "🌟 ഫലങ്ങളും മഹത്വവും:",
                referenceLabel = "📚 അവലംബം:",
                ctaText = "ഇതുപോലുള്ള കൂടുതൽ പ്രാർത്ഥനകൾ വായിക്കാനും കേൾക്കാനും ഡിജിറ്റൽ തസ്ബീഹ് ആപ്പ് ഡൗൺലോഡ് ചെയ്യുക 📲",
                shareChooserTitle = "പ്രാർത്ഥന ഷെയർ ചെയ്യുക"
            )
            l.startsWith("pa") -> ShareTemplate(
                purposeLabel = "🤲 ਦੁਆ:",
                transliterationLabel = "🔤 ਉਚਾਰਨ:",
                translationLabel = "📖 ਤਰਜਮਾ (ਅਰਥ):",
                howToReciteLabel = "✨ ਪੜ੍ਹਨ ਦਾ ਤਰੀਕਾ:",
                benefitsLabel = "🌟 ਫ਼ਜ਼ੀਲਤ ਤੇ ਫ਼ਾਇਦੇ:",
                referenceLabel = "📚 ਹਵਾਲਾ:",
                ctaText = "ਇਹੋ ਜਿਹੀਆਂ ਹੋਰ ਬਿਹਤਰੀਨ ਦੁਆਵਾਂ ਪੜ੍ਹਨ ਤੇ ਸੁਣਨ ਲਈ ਡਿਜੀਟਲ ਤਸਬੀਹ ਐਪ ਡਾਊਨਲੋਡ ਕਰੋ 📲",
                shareChooserTitle = "ਦੁਆ ਸਾਂਝੀ ਕਰੋ"
            )
            l.startsWith("ru") -> ShareTemplate(
                purposeLabel = "🤲 Дуа:",
                transliterationLabel = "🔤 Транслитерация:",
                translationLabel = "📖 Перевод (смысл):",
                howToReciteLabel = "✨ Как читать:",
                benefitsLabel = "🌟 Достоинства и польза:",
                referenceLabel = "📚 Источник:",
                ctaText = "Чтобы читать и слушать еще больше благословенных дуа, скачайте приложение Digital Tasbih 📲",
                shareChooserTitle = "Поделиться дуа"
            )
            l.startsWith("tr") -> ShareTemplate(
                purposeLabel = "🤲 Dua:",
                transliterationLabel = "🔤 Okunuşu:",
                translationLabel = "📖 Anlamı (Meal):",
                howToReciteLabel = "✨ Okuma Usulü:",
                benefitsLabel = "🌟 Fazileti ve Faydaları:",
                referenceLabel = "📚 Kaynak:",
                ctaText = "Daha fazla mübarek dua okumak ve dinlemek için Dijital Tesbih uygulamasını indirin 📲",
                shareChooserTitle = "Duayı Paylaş"
            )
            l.startsWith("id") || l.startsWith("ms") -> ShareTemplate(
                purposeLabel = "🤲 Doa:",
                transliterationLabel = "🔤 Transliterasi:",
                translationLabel = "📖 Arti (Terjemahan):",
                howToReciteLabel = "✨ Cara Membaca:",
                benefitsLabel = "🌟 Fadhilah & Manfaat:",
                referenceLabel = "📚 Sumber / Referensi:",
                ctaText = "Untuk membaca dan mendengarkan lebih banyak doa pilihan, unduh aplikasi Digital Tasbih 📲",
                shareChooserTitle = "Bagikan Doa"
            )
            l.startsWith("fa") -> ShareTemplate(
                purposeLabel = "🤲 دعا:",
                transliterationLabel = "🔤 تلفظ صوتی:",
                translationLabel = "📖 ترجمه و معنی:",
                howToReciteLabel = "✨ روش خواندن:",
                benefitsLabel = "🌟 فضیلت و برکات:",
                referenceLabel = "📚 منبع:",
                ctaText = "برای خواندن و شنیدن دعاهای پربرکت بیشتر، اپلیکیشن تسبیح دیجیتال را دانلود کنید 📲",
                shareChooserTitle = "اشتراک گذاری دعا"
            )
            l.startsWith("fr") -> ShareTemplate(
                purposeLabel = "🤲 Invocation:",
                transliterationLabel = "🔤 Translittération:",
                translationLabel = "📖 Traduction (Sens):",
                howToReciteLabel = "✨ Façon de réciter:",
                benefitsLabel = "🌟 Bienfaits & Mérites:",
                referenceLabel = "📚 Référence:",
                ctaText = "Pour lire et écouter d'autres invocations bénies, téléchargez l'application Digital Tasbih 📲",
                shareChooserTitle = "Partager l'invocation"
            )
            l.startsWith("de") -> ShareTemplate(
                purposeLabel = "🤲 Bittgebet:",
                transliterationLabel = "🔤 Transkription:",
                translationLabel = "📖 Bedeutung (Übersetzung):",
                howToReciteLabel = "✨ Art der Rezitation:",
                benefitsLabel = "🌟 Vorzüge und Nutzen:",
                referenceLabel = "📚 Quelle / Referenz:",
                ctaText = "Um weitere gesegnete Bittgebete zu lesen und anzuhören, laden Sie die Digital Tasbih App herunter 📲",
                shareChooserTitle = "Bittgebet teilen"
            )
            l.startsWith("es") -> ShareTemplate(
                purposeLabel = "🤲 Súplica:",
                transliterationLabel = "🔤 Transliteración:",
                translationLabel = "📖 Traducción (Significado):",
                howToReciteLabel = "✨ Modo de recitación:",
                benefitsLabel = "🌟 Virtudes y Beneficios:",
                referenceLabel = "📚 Referencia:",
                ctaText = "Para leer y escuchar más súplicas benditas, descargue la aplicación Digital Tasbih 📲",
                shareChooserTitle = "Compartir súplica"
            )
            else -> ShareTemplate(
                purposeLabel = "🤲 Dua:",
                transliterationLabel = "🔤 Pronunciation (Transliteration):",
                translationLabel = "📖 Meaning (Translation):",
                howToReciteLabel = "✨ How to Recite:",
                benefitsLabel = "🌟 Virtues & Benefits:",
                referenceLabel = "📚 Reference:",
                ctaText = "To read and listen to more beautiful and authentic Duas, download the Digital Tasbih App 📲",
                shareChooserTitle = "Share Dua"
            )
        }
    }

    /**
     * Builds the complete formatted, localized WhatsApp/SMS share text for a DuaEntry.
     */
    fun buildShareText(dua: DuaEntry, lang: String): String {
        val t = getTemplate(lang)
        val sb = StringBuilder()

        // 1. Salam
        sb.append(SALAM_ARABIC)
        sb.append("\n\n")

        // 2. Kis cheez ke liye dua hai (Title / Purpose)
        val title = dua.getLocalizedTitle(lang).trim()
        if (title.isNotBlank()) {
            sb.append("${t.purposeLabel} $title")
            sb.append("\n\n")
        }

        // 3. Dua (Arabic Text)
        val ar = dua.ar.trim()
        if (ar.isNotBlank()) {
            sb.append(ar)
            sb.append("\n\n")
        }

        // 4. Transliteration (Pronunciation)
        val transliteration = dua.getLocalizedTransliteration(lang).trim()
        if (transliteration.isNotBlank()) {
            sb.append(t.transliterationLabel)
            sb.append("\n")
            sb.append(transliteration)
            sb.append("\n\n")
        }

        // 5. Translation (Meaning)
        val meaning = dua.getLocalizedMeaning(lang).trim()
        if (meaning.isNotBlank()) {
            sb.append(t.translationLabel)
            sb.append("\n")
            sb.append(meaning)
            sb.append("\n\n")
        }

        // 6. Padhne ka Tarika (How to recite - if present)
        val how = dua.getLocalizedHow(lang).trim()
        if (how.isNotBlank()) {
            sb.append(t.howToReciteLabel)
            sb.append("\n")
            sb.append(how)
            sb.append("\n\n")
        }

        // 7. Fayde / Virtues / Timing (if present)
        val benefits = dua.getLocalizedWhen(lang).trim()
        if (benefits.isNotBlank()) {
            sb.append(t.benefitsLabel)
            sb.append("\n")
            sb.append(benefits)
            sb.append("\n\n")
        }

        // 8. Hadith / Reference (if present)
        val ref = dua.getLocalizedReference(lang).trim()
        if (ref.isNotBlank()) {
            sb.append("${t.referenceLabel} $ref")
            sb.append("\n\n")
        }

        // 9. Call-to-action & Play Store Link
        sb.append("━━━━━━━━━━━━━━━━━━━━\n")
        sb.append(t.ctaText)
        sb.append("\n👉 ")
        sb.append(PLAY_STORE_URL)

        return sb.toString().trim()
    }

    /**
     * Builds the complete formatted, localized share text for a DailyDuaData item.
     */
    fun buildShareText(dailyDua: DailyDuaData, lang: String): String {
        val t = getTemplate(lang)
        val sb = StringBuilder()

        // 1. Salam
        sb.append(SALAM_ARABIC)
        sb.append("\n\n")

        // 2. Arabic Dua
        val ar = dailyDua.arabic.trim()
        if (ar.isNotBlank()) {
            sb.append(ar)
            sb.append("\n\n")
        }

        // 3. Transliteration
        val transliteration = dailyDua.transliteration.trim()
        if (transliteration.isNotBlank()) {
            sb.append(t.transliterationLabel)
            sb.append("\n")
            sb.append(transliteration)
            sb.append("\n\n")
        }

        // 4. Translation
        val translation = dailyDua.translation.trim()
        if (translation.isNotBlank()) {
            sb.append(t.translationLabel)
            sb.append("\n")
            sb.append(translation)
            sb.append("\n\n")
        }

        // 5. Reference
        val ref = dailyDua.reference.trim()
        if (ref.isNotBlank()) {
            sb.append("${t.referenceLabel} $ref")
            sb.append("\n\n")
        }

        // 6. Call-to-action & Play Store Link
        sb.append("━━━━━━━━━━━━━━━━━━━━\n")
        sb.append(t.ctaText)
        sb.append("\n👉 ")
        sb.append(PLAY_STORE_URL)

        return sb.toString().trim()
    }

    /**
     * Universal Launch Share Chooser (Direct to WhatsApp or any chosen app).
     */
    fun shareDua(context: Context, dua: DuaEntry, lang: String) {
        val shareText = buildShareText(dua, lang)
        val t = getTemplate(lang)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        context.startActivity(Intent.createChooser(intent, t.shareChooserTitle))
    }

    /**
     * Universal Launch Share Chooser for DailyDuaData.
     */
    fun shareDailyDua(context: Context, dailyDua: DailyDuaData, lang: String) {
        val shareText = buildShareText(dailyDua, lang)
        val t = getTemplate(lang)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        context.startActivity(Intent.createChooser(intent, t.shareChooserTitle))
    }
}

