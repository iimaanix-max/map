package com.tasbih.digitaltasbih.util

import androidx.compose.ui.graphics.Color

/**
 * Authentic Quran Waqf (Stop / Pause) Signs and Tajweed Color definitions.
 * Follows the standard King Fahad Quran Complex & Tanzil.net Tajweed rules.
 */
data class WaqfSign(
    val symbol: String,
    val nameArabic: String,
    val nameHindi: String,
    val nameEnglish: String,
    val nameUrdu: String,
    val actionHindi: String,
    val actionEnglish: String = "",
    val actionUrdu: String = "",
    val descriptionHindi: String,
    val descriptionEnglish: String = "",
    val descriptionUrdu: String = "",
    val color: Color
)

fun WaqfSign.getName(lang: String): String = when (lang) {
    "en" -> nameEnglish
    "ur" -> nameUrdu
    "ar" -> nameArabic
    else -> nameHindi
}

fun WaqfSign.getAction(lang: String): String = when (lang) {
    "en" -> actionEnglish.ifBlank { nameEnglish }
    "ur" -> actionUrdu.ifBlank { nameUrdu }
    "ar" -> nameArabic
    else -> actionHindi
}

fun WaqfSign.getDescription(lang: String): String = when (lang) {
    "en" -> descriptionEnglish.ifBlank { descriptionHindi }
    "ur" -> descriptionUrdu.ifBlank { descriptionHindi }
    "ar" -> descriptionEnglish.ifBlank { descriptionHindi }
    else -> descriptionHindi
}

data class TajweedRule(
    val titleHindi: String,
    val titleArabic: String,
    val titleEnglish: String,
    val titleUrdu: String,
    val ruleSummary: String,
    val ruleSummaryEnglish: String = "",
    val ruleSummaryUrdu: String = "",
    val exampleArabic: String,
    val color: Color
)

fun TajweedRule.getTitle(lang: String): String = when (lang) {
    "en" -> titleEnglish
    "ur" -> titleUrdu
    "ar" -> titleArabic
    else -> titleHindi
}

fun TajweedRule.getSummary(lang: String): String = when (lang) {
    "en" -> ruleSummaryEnglish.ifBlank { ruleSummary }
    "ur" -> ruleSummaryUrdu.ifBlank { ruleSummary }
    "ar" -> ruleSummaryEnglish.ifBlank { ruleSummary }
    else -> ruleSummary
}

object TajweedWaqfHelper {

    val allSigns = listOf(
        WaqfSign(
            symbol = "مـ",
            nameArabic = "الوقف اللازم",
            nameHindi = "वक़्फ़ लाज़िम (अनिवार्य ठहराव)",
            nameEnglish = "Waqf Lazim (Compulsory Stop)",
            nameUrdu = "وقف لازم (ٹھہرنا لازمی)",
            actionHindi = "यहाँ अवश्य रुकें (Must Stop)",
            actionEnglish = "Must Stop (Compulsory)",
            actionUrdu = "ٹھہرنا لازمی ہے",
            descriptionHindi = "यहाँ रुकना अनिवार्य है। यदि मिलाकर पढ़ेंगे तो आयत का अर्थ पूरी तरह बदल सकता है।",
            descriptionEnglish = "A compulsory stop. Continuing without pause may significantly alter the theological meaning of the verse.",
            descriptionUrdu = "یہاں رکنا ضروری ہے۔ اگر ملا کر پڑھیں گے تو آیت کا مفہوم بدل سکتا ہے۔",
            color = Color(0xFFE11D48) // Rose Red
        ),
        WaqfSign(
            symbol = "ط",
            nameArabic = "الوقف المطلق",
            nameHindi = "वक़्फ़ मुतलक़ (पूर्ण ठहराव)",
            nameEnglish = "Waqf Mutlaq (Absolute Stop)",
            nameUrdu = "وقف مطلق (مکمل ٹھہراؤ)",
            actionHindi = "ठहरना बहुत बेहतर है (Strongly Recommended Stop)",
            actionEnglish = "Strongly Recommended Stop",
            actionUrdu = "ٹھہرنا بہت بہتر ہے",
            descriptionHindi = "यहाँ बात पूरी हो चुकी है। ठहर कर सांस लें और फिर अगली बात शुरू करें।",
            descriptionEnglish = "The sentence or thought is complete. Take a breath here before starting the next statement.",
            descriptionUrdu = "یہاں بات مکمل ہو چکی ہے۔ رک کر سانس لیں اور اگلا کلام شروع کریں۔",
            color = Color(0xFF2563EB) // Royal Blue
        ),
        WaqfSign(
            symbol = "ج",
            nameArabic = "الوقف الجائز",
            nameHindi = "वक़्फ़ जायज़ (अनुमत ठहराव)",
            nameEnglish = "Waqf Ja'iz (Permissible Stop)",
            nameUrdu = "وقف جائز (ٹھہرنا و ملانا جائز)",
            actionHindi = "रुकना व मिलाना दोनों जायज़ (Stop or Continue)",
            actionEnglish = "Stop or Continue (Permissible)",
            actionUrdu = "رکنا یا ملانا دونوں جائز",
            descriptionHindi = "यहाँ रुकना और मिलाकर पढ़ना दोनों सही है, परन्तु रुकना थोड़ा अधिक उत्तम है।",
            descriptionEnglish = "Permissible to pause or continue, though taking a pause is slightly preferable.",
            descriptionUrdu = "یہاں رکنا اور ملا کر پڑھنا دونوں جائز ہیں، مگر رکنا تھوڑا زیادہ بہتر ہے۔",
            color = Color(0xFF16A34A) // Emerald Green
        ),
        WaqfSign(
            symbol = "قلے",
            nameArabic = "الوقف أولى",
            nameHindi = "अल-वक़्फ़ औला (ठहरना उत्तम)",
            nameEnglish = "Al-Waqf Awla (Stopping is Better)",
            nameUrdu = "الوقف اولیٰ (ٹھہرنا بہتر)",
            actionHindi = "ठहरना अधिक बेहतर (Stopping is Preferred)",
            actionEnglish = "Stopping is Preferred",
            actionUrdu = "ٹھہرنا افضل ہے",
            descriptionHindi = "यहाँ रुकना मिलाकर पढ़ने की तुलना में अधिक श्रेष्ठ और मुस्तहब है।",
            descriptionEnglish = "Stopping here is preferred and recommended over connecting to the next word.",
            descriptionUrdu = "یہاں ملا کر پڑھنے کے مقابلے میں ٹھہرنا زیادہ بہتر اور مستحب ہے۔",
            color = Color(0xFF9333EA) // Purple
        ),
        WaqfSign(
            symbol = "صلے",
            nameArabic = "الوصل أولى",
            nameHindi = "अल-वस्ल औला (मिलाना उत्तम)",
            nameEnglish = "Al-Wasl Awla (Continue is Better)",
            nameUrdu = "الوصل اولیٰ (ملانا بہتر)",
            actionHindi = "मिलाकर पढ़ना बेहतर (Continuation is Preferred)",
            actionEnglish = "Continuation is Preferred",
            actionUrdu = "ملا کر پڑھنا بہتر ہے",
            descriptionHindi = "यहाँ न रुककर आगे मिलाकर पढ़ना अधिक उत्तम है, यद्यपि रुकना भी मना नहीं है।",
            descriptionEnglish = "Continuing without pause is preferred, although pausing is also permissible.",
            descriptionUrdu = "یہاں آگے ملا کر پڑھنا افضل ہے، البتہ رکنا بھی منع نہیں ہے۔",
            color = Color(0xFF0284C7) // Sky Blue
        ),
        WaqfSign(
            symbol = "لا",
            nameArabic = "لا تقف",
            nameHindi = "ला वक़्फ़ (मत रुकिए)",
            nameEnglish = "La Waqf (Do Not Stop)",
            nameUrdu = "لا وقف (نہ ٹھہریں)",
            actionHindi = "यहाँ बिल्कुल न रुकें (Do Not Stop)",
            actionEnglish = "Do Not Stop",
            actionUrdu = "ہرگز نہ ٹھہریں",
            descriptionHindi = "यहाँ नहीं रुकना चाहिए, क्योंकि बात अधूरी रह जाएगी और अर्थ स्पष्ट नहीं होगा।",
            descriptionEnglish = "Do not stop here, because the grammatical thought is incomplete and meaning will be unclear.",
            descriptionUrdu = "یہاں نہیں رکنا چاہیے، کیونکہ کلام ادھورا رہ جائے گا اور بات واضح نہیں ہوگی۔",
            color = Color(0xFFEA580C) // Orange
        ),
        WaqfSign(
            symbol = "س",
            nameArabic = "السكتة",
            nameHindi = "सक्ता (अल्प दम-साध ठहराव)",
            nameEnglish = "Saktah (Breathless Pause)",
            nameUrdu = "سکتہ (بغیر سانس توڑے توقف)",
            actionHindi = "बिना सांस तोड़े हल्की खामोशी (Short Pause without Breath)",
            actionEnglish = "Breathless Pause",
            actionUrdu = "بغیر سانس توڑے توقف",
            descriptionHindi = "एक-दो सेकंड के लिए आवाज़ रोकें लेकिन सांस न तोड़ें, फिर तुरंत आगे पढ़ें।",
            descriptionEnglish = "Pause your voice briefly for 1 to 2 counts without taking a new breath, then proceed immediately.",
            descriptionUrdu = "آواز کو بغیر سانس توڑے ایک دو سیکنڈ روکیں پھر فوراً آگے پڑھیں۔",
            color = Color(0xFF4F46E5) // Indigo
        ),
        WaqfSign(
            symbol = "∴ ∴",
            nameArabic = "المعانقة",
            nameHindi = "मुआनक़ा (दो में से एक पर रुकें)",
            nameEnglish = "Mu'anaqah (Stop at Either One)",
            nameUrdu = "معانقہ (کسی ایک پر ٹھہریں)",
            actionHindi = "किसी एक स्थान पर ठहरें (Stop at One, Not Both)",
            actionEnglish = "Stop at One, Never Both",
            actionUrdu = "کسی ایک پر ٹھہریں",
            descriptionHindi = "आयत में तीन बिंदियों के दो जोड़े होते हैं। दोनों में से किसी एक पर रुकें, दोनों पर नहीं।",
            descriptionEnglish = "Appears in pairs of three dots. You may pause at either one, but never at both locations.",
            descriptionUrdu = "آیت میں تین نقطوں کے دو جوڑے ہوتے ہیں۔ دونوں میں سے کسی ایک پر رکیں، دونوں پر نہیں۔",
            color = Color(0xFFB45309) // Amber Gold
        )
    )

    val allRules = listOf(
        TajweedRule(
            titleHindi = "मद (Madd - खींच कर पढ़ना)",
            titleArabic = "المدود (لازم / متصل / منفصل)",
            titleEnglish = "Madd (Prolonged Lengthening)",
            titleUrdu = "مد (کھینچ کر لمبا کرنا)",
            ruleSummary = "जब किसी हर्फ़ पर मद (~ / ٓ) का निशान हो तो आवाज़ को 4 से 6 हरकत तक खींचकर पढ़ें।",
            ruleSummaryEnglish = "When a letter has a Madd mark (~ or ٓ), prolong the vowel sound for 4 to 6 vowel counts (Harakat).",
            ruleSummaryUrdu = "جب کسی حرف پر مد (~ / ٓ) کا نشان ہو تو آواز کو 4 سے 6 حرکات تک کھینچ کر پڑھیں۔",
            exampleArabic = "الضَّالِّينَ • جَاءَ • السَّمَاءِ",
            color = Color(0xFFE11D48) // Vivid Red
        ),
        TajweedRule(
            titleHindi = "ग़ुन्ना (Ghunnah - नाक में आवाज़)",
            titleArabic = "الغنة المشددة (نّ / مّ)",
            titleEnglish = "Ghunnah (2-Count Nasalization)",
            titleUrdu = "غنہ (ناک میں آواز ٹھہرانا)",
            ruleSummary = "नून मुशद्दद (نّ) या मीम मुशद्दद (मّ) आने पर आवाज़ को 2 हरकत तक नाक के बांसुरे में ठहराकर गुनगुनाया जाता है।",
            ruleSummaryEnglish = "When Noon or Meem carries a Shaddah (نّ / مّ), hold the humming sound in the nasal cavity for 2 vowel counts.",
            ruleSummaryUrdu = "نون مشدد (نّ) یا میم مشدد (مّ) پر آواز کو 2 حرکات تک ناک کے بانسے میں ٹھہرا کر غنہ کریں۔",
            exampleArabic = "إِنَّ • عَمَّ • النَّاسِ",
            color = Color(0xFFEA580C) // Warm Orange
        ),
        TajweedRule(
            titleHindi = "क़लक़ला (Qalqalah - गूंजती आवाज़)",
            titleArabic = "القلقلة (قطب جد)",
            titleEnglish = "Qalqalah (Echoing / Bouncing Sound)",
            titleUrdu = "قلقلہ (گونجتی ہوئی آواز)",
            ruleSummary = "क़ुतब जद (ق ، ط ، ब ، ج ، د) साकिन (ْ / ۡ) होने पर आवाज़ को हिलाकर और टकराकर गूंज के साथ अदा किया जाता है।",
            ruleSummaryEnglish = "When letters of Qutb Jad (ق ، ط ، ب ، ج ، د) have Sukun (ْ / ۡ), produce a distinct bouncing echo resonance.",
            ruleSummaryUrdu = "قطب جد (ق ، ط ، ب ، ج ، د) ساکن ہونے پر آواز کو ہلا کر گونج کے ساتھ ادا کیا جاتا ہے۔",
            exampleArabic = "أَحَدْ • الفَلَقْ • الحَبْلِ • مَطْلَعِ",
            color = Color(0xFF0284C7) // Cyan Blue
        ),
        TajweedRule(
            titleHindi = "इख़्फ़ा व इक़्लाब (Ikhfa / Iqlab - छुपाना)",
            titleArabic = "الإخفاء الحقيقي والإقلاب",
            titleEnglish = "Ikhfa & Iqlab (Hidden Nasal Tone)",
            titleUrdu = "اخفاء و اقلاب (چھپانا اور بدلنا)",
            ruleSummary = "नून साकिन या तनवीन के बाद इख़्फ़ा के हर्फ़ आएं तो आवाज़ को नाक में छुपाकर पढ़ें। बा (ب) आने पर मीम (ۢ) में बदलें।",
            ruleSummaryEnglish = "Conceal Noon Sakin or Tanween with a subtle nasal hum before Ikhfa letters; convert into Meem (ۢ) before Baa (ب).",
            ruleSummaryUrdu = "نون ساکن یا تنوین کے بعد حروفِ اخفاء پر آواز کو ناک میں چھپائیں، با (ب) آنے پر میم (ۢ) سے بدلیں۔",
            exampleArabic = "مِنْ قَبْلِ • كُنْتُمْ • أَنْبِئْهُمْ",
            color = Color(0xFF16A34A) // Emerald Green
        ),
        TajweedRule(
            titleHindi = "मौन हर्फ़ (Silent / Unpronounced Letters)",
            titleArabic = "الحروف غير المنطوقة (إدغام بغير غنة / همزة وصل)",
            titleEnglish = "Silent Letters (Unpronounced)",
            titleUrdu = "حروفِ غیر ملفوظہ (ساکت حروف)",
            ruleSummary = "जो हर्फ़ केवल लिखे जाते हैं परन्तु तिलावत के समय अदा नहीं किए जाते (जैसे अलिफ़ वस्ल या मुकम्मल इदग़ाम)।",
            ruleSummaryEnglish = "Letters written in Uthmani script that are bypassed and not pronounced during fluent recitation (e.g., Hamzatul Wasl).",
            ruleSummaryUrdu = "وہ حروف جو خطِ عثمانی میں لکھے جاتے ہیں لیکن تلاوت میں پڑھے نہیں جاتے (جیسے ہمزۃ الوصل)۔",
            exampleArabic = "وَالشَّمْسِ • مِّن رَّبِّهِمْ",
            color = Color(0xFF94A3B8) // Muted Slate Grey
        )
    )

    /**
     * Inspects an Arabic token and determines its authentic Tajweed or Waqf highlight color.
     * If no rule matches, returns [defaultColor].
     */
    fun getTajweedOrWaqfColor(text: String, defaultColor: Color): Color {
        if (text.isBlank()) return defaultColor

        // 1. Check Unicode Quranic Waqf symbols
        if (text.contains("ۘ") || text.contains("مـ")) return Color(0xFFE11D48) // Waqf Lazim (Red)
        if (text.contains("ۚ") || text.contains("ج")) return Color(0xFF16A34A)  // Waqf Ja'iz (Green)
        if (text.contains("ۗ") || text.contains("قلے")) return Color(0xFF9333EA) // Waqf Awla (Purple)
        if (text.contains("ۖ") || text.contains("صلے")) return Color(0xFF0284C7) // Wasl Awla (Blue)
        if (text.contains("ۙ") || text.contains("لا")) return Color(0xFFEA580C)  // La Waqf (Orange)
        if (text.contains("ۛ") || text.contains("∴")) return Color(0xFFB45309)   // Mu'anaqah (Amber)
        if (text.contains("ط")) return Color(0xFF2563EB)                         // Waqf Mutlaq (Blue)

        // 2. Madd (4 to 6 Harakat Lengthening)
        if (text.contains("~") || text.contains("\u0653") || text.contains("\u06E4") ||
            text.contains("ۤ") || text.contains("ٓ")
        ) {
            return Color(0xFFE11D48) // Vivid Red
        }

        // 3. Ghunnah (2 Harakat Nasalization on Noon/Meem with Shaddah)
        if (text.contains("نّ") || text.contains("مّ") ||
            text.contains("نَّ") || text.contains("مَّ") ||
            text.contains("نِّ") || text.contains("مِّ") ||
            text.contains("نُّ") || text.contains("مُّ")
        ) {
            return Color(0xFFEA580C) // Warm Orange
        }

        // 4. Qalqalah (Qaf, Taa, Baa, Jeem, Daal with Sukun)
        if (text.contains("قْ") || text.contains("طْ") || text.contains("بْ") ||
            text.contains("جْ") || text.contains("دْ") ||
            text.contains("قۡ") || text.contains("طۡ") || text.contains("بۡ") ||
            text.contains("جۡ") || text.contains("دۡ")
        ) {
            return Color(0xFF0284C7) // Cyan Blue
        }

        // 5. Ikhfa / Iqlab (Meem substitution ۢ)
        if (text.contains("ۢ") || text.contains("\u06E2")) {
            return Color(0xFF16A34A) // Emerald Green
        }

        return defaultColor
    }
}
