package com.tasbih.digitaltasbih.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.data.QuranAudioPlayMode
import com.tasbih.digitaltasbih.data.QuranRepository
import com.tasbih.digitaltasbih.ui.theme.GoldAccent

data class QuranAudioModeStrings(
    val sectionMode: String,
    val arabicOnlyTitle: String,
    val arabicOnlyDesc: String,
    val tarjumaOnlyTitle: String,
    val tarjumaOnlyDesc: String,
    val arabicAndTarjumaTitle: String,
    val arabicAndTarjumaDesc: String,
    val sectionVoice: String,
    val urduVoiceTitle: String,
    val urduVoiceDesc: String,
    val englishVoiceTitle: String,
    val englishVoiceDesc: String,
    val pillArabic: String,
    val pillTarjuma: String,
    val pillArabicAndTarjuma: String
)

fun getQuranAudioModeStrings(lang: String): QuranAudioModeStrings {
    return when (lang) {
        "hi" -> QuranAudioModeStrings(
            sectionMode = "ऑडियो प्लेबैक मोड",
            arabicOnlyTitle = "🎙️ सिर्फ अरबी (Arabic Only)",
            arabicOnlyDesc = "क़ारी की मूल तिलावत (डिफ़ॉल्ट)",
            tarjumaOnlyTitle = "🗣️ सिर्फ तर्जुमा (Tarjuma Only)",
            tarjumaOnlyDesc = "इंसानी आवाज में केवल अनुवाद",
            arabicAndTarjumaTitle = "🔄 अरबी + तर्जुमा (Arabic + Tarjuma)",
            arabicAndTarjumaDesc = "पहले अरबी आयत, फिर उसका तर्जुमा",
            sectionVoice = "तर्जुमा की इंसानी आवाज",
            urduVoiceTitle = "उर्दू (Urdu)",
            urduVoiceDesc = "शमशाद अली खान (स्टूडियो रिकॉर्डिंग)",
            englishVoiceTitle = "अंग्रेज़ी (English)",
            englishVoiceDesc = "इब्राहिम वॉक (सहीह इंटरनेशनल)",
            pillArabic = "🎙️ अरबी",
            pillTarjuma = "🗣️ तर्जुमा",
            pillArabicAndTarjuma = "🔄 अरबी+तर्जुमा"
        )
        "ur" -> QuranAudioModeStrings(
            sectionMode = "پلے بیک آڈیو موڈ",
            arabicOnlyTitle = "🎙️ صرف عربی (Arabic Only)",
            arabicOnlyDesc = "قاری کی اصل تلاوت (ڈیفالٹ)",
            tarjumaOnlyTitle = "🗣️ صرف ترجمہ (Tarjuma Only)",
            tarjumaOnlyDesc = "صرف انسانی آواز میں ترجمہ",
            arabicAndTarjumaTitle = "🔄 عربی + ترجمہ (Arabic + Tarjuma)",
            arabicAndTarjumaDesc = "پہلے عربی آیت، پھر اس کا ترجمہ",
            sectionVoice = "ترجمہ کی انسانی آواز",
            urduVoiceTitle = "اردو (Urdu)",
            urduVoiceDesc = "شمشاد علی خان (سٹوڈیو انسانی آواز)",
            englishVoiceTitle = "انگریزی (English)",
            englishVoiceDesc = "ابراہیم واک (صحیح انٹرنیشنل)",
            pillArabic = "🎙️ عربی",
            pillTarjuma = "🗣️ ترجمہ",
            pillArabicAndTarjuma = "🔄 عربی+ترجمہ"
        )
        "ar" -> QuranAudioModeStrings(
            sectionMode = "نمط تشغيل الصوت",
            arabicOnlyTitle = "🎙️ التلاوة العربية فقط",
            arabicOnlyDesc = "تلاوة القارئ الأصلية (الافتراضي)",
            tarjumaOnlyTitle = "🗣️ الترجمة الصوتية فقط",
            tarjumaOnlyDesc = "صوت بشري للترجمة فقط",
            arabicAndTarjumaTitle = "🔄 عربية + ترجمة",
            arabicAndTarjumaDesc = "الآية بالعربية ثم ترجمتها الصوتية",
            sectionVoice = "صوت الترجمة البشري",
            urduVoiceTitle = "أردو (Urdu)",
            urduVoiceDesc = "شمشاد علي خان (تسجيل استوديو)",
            englishVoiceTitle = "إنجليزي (English)",
            englishVoiceDesc = "إبراهيم والك (صحيح انترناشونال)",
            pillArabic = "🎙️ عربية",
            pillTarjuma = "🗣️ ترجمة",
            pillArabicAndTarjuma = "🔄 عربية+ترجمة"
        )
        "bn" -> QuranAudioModeStrings(
            sectionMode = "অডিও প্লেব্যাক মোড",
            arabicOnlyTitle = "🎙️ শুধু আরবি (Arabic Only)",
            arabicOnlyDesc = "ক্বারীর মূল তিলাওয়াত (ডিফল্ট)",
            tarjumaOnlyTitle = "🗣️ শুধু তরজমা (Tarjuma Only)",
            tarjumaOnlyDesc = "মানুষের কণ্ঠে শুধু অনুবাদ",
            arabicAndTarjumaTitle = "🔄 আরবি + তরজমা",
            arabicAndTarjumaDesc = "প্রথমে আরবি আয়াত, এরপর তরজমা",
            sectionVoice = "তরজমার মানুষের কণ্ঠ",
            urduVoiceTitle = "উর্দু (Urdu)",
            urduVoiceDesc = "শামশাদ আলী খান (স্টুডিও ভয়েস)",
            englishVoiceTitle = "ইংরেজি (English)",
            englishVoiceDesc = "ইব্রাহিম ওয়াক (সহিহ ইন্টারন্যাশনাল)",
            pillArabic = "🎙️ আরবি",
            pillTarjuma = "🗣️ তরজমা",
            pillArabicAndTarjuma = "🔄 আরবি+তরজমা"
        )
        "ta" -> QuranAudioModeStrings(
            sectionMode = "ஆடியோ பிளேபேக் பயன்முறை",
            arabicOnlyTitle = "🎙️ அரபு மட்டும் (Arabic Only)",
            arabicOnlyDesc = "அசல் காரி ஓதுதல் (இயல்புநிலை)",
            tarjumaOnlyTitle = "🗣️ மொழிபெயர்ப்பு மட்டும்",
            tarjumaOnlyDesc = "மனித குரலில் மொழிபெயர்ப்பு மட்டும்",
            arabicAndTarjumaTitle = "🔄 அரபு + மொழிபெயர்ப்பு",
            arabicAndTarjumaDesc = "முதலில் அரபு வசனம், பின் மொழிபெயர்ப்பு",
            sectionVoice = "மொழிபெயர்ப்பு மனித குரல்",
            urduVoiceTitle = "உருது (Urdu)",
            urduVoiceDesc = "ஷம்ஷாத் அலி கான் (ஸ்டுடியோ குரல்)",
            englishVoiceTitle = "ஆங்கிலம் (English)",
            englishVoiceDesc = "இப்ராஹிம் வாக் (ஸஹீஹ் இன்டர்நேஷனல்)",
            pillArabic = "🎙️ அரபு",
            pillTarjuma = "🗣️ மொழிபெயர்ப்பு",
            pillArabicAndTarjuma = "🔄 அரபு+மொழிபெயர்ப்பு"
        )
        "ml" -> QuranAudioModeStrings(
            sectionMode = "ഓഡിയോ പ്ലേബാക്ക് മോഡ്",
            arabicOnlyTitle = "🎙️ അറബിക് മാത്രം (Arabic Only)",
            arabicOnlyDesc = "ഖാരിഇന്റെ യഥാർത്ഥ പാരായണം (ഡീഫോൾട്ട്)",
            tarjumaOnlyTitle = "🗣️ തർജ്ജമ മാത്രം (Tarjuma Only)",
            tarjumaOnlyDesc = "മനുഷ്യ ശബ്ദത്തിലുള്ള തർജ്ജമ മാത്രം",
            arabicAndTarjumaTitle = "🔄 അറബിക് + തർജ്ജമ",
            arabicAndTarjumaDesc = "ആദ്യം അറബിക് ആയത്ത്, ശേഷം തർജ്ജമ",
            sectionVoice = "തർജ്ജമ മനുഷ്യ ശബ്ദം",
            urduVoiceTitle = "ഉർദു (Urdu)",
            urduVoiceDesc = "ശംഷാദ് അലി ഖാൻ (സ്റ്റുഡിയോ റെക്കോർഡിംഗ്)",
            englishVoiceTitle = "ഇംഗ്ലീഷ് (English)",
            englishVoiceDesc = "ഇബ്രാഹിം വാക്ക് (സഹീഹ് ഇന്റർനാഷണൽ)",
            pillArabic = "🎙️ അറബി",
            pillTarjuma = "🗣️ തർജ്ജമ",
            pillArabicAndTarjuma = "🔄 അറബി+തർജ്ജമ"
        )
        "gu" -> QuranAudioModeStrings(
            sectionMode = "ઑડિયો પ્લેબેક મોડ",
            arabicOnlyTitle = "🎙️ ફક્ત અરબી (Arabic Only)",
            arabicOnlyDesc = "કારીનું મૂળ તિલાવત (ડિફૉલ્ટ)",
            tarjumaOnlyTitle = "🗣️ ફક્ત તરજુમા (Tarjuma Only)",
            tarjumaOnlyDesc = "માનવ અવાજમાં માત્ર તરજુમા",
            arabicAndTarjumaTitle = "🔄 અરબી + તરજુમા",
            arabicAndTarjumaDesc = "પહેલાં અરબી આયત, પછી તરજુમા",
            sectionVoice = "તરજુમા માનવ અવાજ",
            urduVoiceTitle = "ઉર્દૂ (Urdu)",
            urduVoiceDesc = "શમશાદ અલી ખાન (સ્ટુડિયો રેકોર્ડિંગ)",
            englishVoiceTitle = "અંગ્રેજી (English)",
            englishVoiceDesc = "ઇબ્રાહિમ વૉક (સહીહ ઇન્ટરનેશનલ)",
            pillArabic = "🎙️ અરબી",
            pillTarjuma = "🗣️ તરજુમા",
            pillArabicAndTarjuma = "🔄 અરબી+તરજુમા"
        )
        "mr" -> QuranAudioModeStrings(
            sectionMode = "ऑडिओ प्लेबॅक मोड",
            arabicOnlyTitle = "🎙️ फक्त अरबी (Arabic Only)",
            arabicOnlyDesc = "कारीचे मूळ पठण (डिफॉल्ट)",
            tarjumaOnlyTitle = "🗣️ फक्त भाषांतर (Tarjuma Only)",
            tarjumaOnlyDesc = "मानवी आवाजात फक्त भाषांतर",
            arabicAndTarjumaTitle = "🔄 अरबी + भाषांतर",
            arabicAndTarjumaDesc = "आधी अरबी आयत, नंतर भाषांतर",
            sectionVoice = "भाषांतराचा मानवी आवाज",
            urduVoiceTitle = "उर्दू (Urdu)",
            urduVoiceDesc = "शमशाद अली खान (स्टुडिओ व्हॉईस)",
            englishVoiceTitle = "इंग्रजी (English)",
            englishVoiceDesc = "इब्राहिम वॉक (सहीह इंटरनॅशनल)",
            pillArabic = "🎙️ अरबी",
            pillTarjuma = "🗣️ भाषांतर",
            pillArabicAndTarjuma = "🔄 अरबी+भाषांतर"
        )
        "te" -> QuranAudioModeStrings(
            sectionMode = "ఆడియో ప్లేబ్యాక్ మోడ్",
            arabicOnlyTitle = "🎙️ అరబిక్ మాత్రమే (Arabic Only)",
            arabicOnlyDesc = "ఖారీ అసలు పఠనం (డిఫాల్ట్)",
            tarjumaOnlyTitle = "🗣️ అనువాదం మాత్రమే",
            tarjumaOnlyDesc = "మానవ గొంతులో అనువాదం మాత్రమే",
            arabicAndTarjumaTitle = "🔄 అరబిక్ + అనువాదం",
            arabicAndTarjumaDesc = "ముందుగా అరబిక్ ఆయత్, తర్వాత అనువాదం",
            sectionVoice = "అనువాద మానవ గొంతు",
            urduVoiceTitle = "ఉర్దూ (Urdu)",
            urduVoiceDesc = "షంషాద్ అలీ ఖాన్ (స్టూడియో వాయిస్)",
            englishVoiceTitle = "ఇంగ్లీష్ (English)",
            englishVoiceDesc = "ఇబ్రహీం వాక్ (సహీహ్ ఇంటర్నేషనల్)",
            pillArabic = "🎙️ అరబిక్",
            pillTarjuma = "🗣️ అనువాదం",
            pillArabicAndTarjuma = "🔄 అరబిక్+అనువాదం"
        )
        "kn" -> QuranAudioModeStrings(
            sectionMode = "ಆಡಿಯೋ ಪ್ಲೇಬ್ಯಾಕ್ ಮೋಡ್",
            arabicOnlyTitle = "🎙️ ಅರೇಬಿಕ್ ಮಾತ್ರ (Arabic Only)",
            arabicOnlyDesc = "ಮೂಲ ಖಾರಿ ಪಠಣ (ಡೀಫಾಲ್ಟ್)",
            tarjumaOnlyTitle = "🗣️ ಅನುವಾದ ಮಾತ್ರ (Tarjuma Only)",
            tarjumaOnlyDesc = "ಮಾನವ ಧ್ವನಿಯಲ್ಲಿ ಅನುವಾದ ಮಾತ್ರ",
            arabicAndTarjumaTitle = "🔄 ಅರೇಬಿಕ್ + ಅನುವಾದ",
            arabicAndTarjumaDesc = "ಮೊದಲು ಅರೇಬಿಕ್ ಆಯತ್, ನಂತರ ಅನುವಾದ",
            sectionVoice = "ಅನುವಾದ ಮಾನವ ಧ್ವನಿ",
            urduVoiceTitle = "ಉರ್ದು (Urdu)",
            urduVoiceDesc = "ಶಂಶಾದ್ ಅಲಿ ಖಾನ್ (ಸ್ಟುಡಿಯೋ ಧ್ವನಿ)",
            englishVoiceTitle = "ಇಂಗ್ಲಿಷ್ (English)",
            englishVoiceDesc = "ಇಬ್ರಾಹಿಂ ವಾಕ್ (ಸಹೀಹ್ ಇಂಟರ್ನ್ಯಾಷನಲ್)",
            pillArabic = "🎙️ ಅರೇಬಿಕ್",
            pillTarjuma = "🗣️ ಅನುವಾದ",
            pillArabicAndTarjuma = "🔄 ಅರೇಬಿಕ್+ಅನುವಾದ"
        )
        "id" -> QuranAudioModeStrings(
            sectionMode = "MODE PEMUTARAN AUDIO",
            arabicOnlyTitle = "🎙️ Hanya Bahasa Arab",
            arabicOnlyDesc = "Tilawah Qari Asli (Default)",
            tarjumaOnlyTitle = "🗣️ Hanya Terjemahan",
            tarjumaOnlyDesc = "Suara Manusia Terjemahan Saja",
            arabicAndTarjumaTitle = "🔄 Arab + Terjemahan",
            arabicAndTarjumaDesc = "Ayat Arab, lalu Terjemahannya",
            sectionVoice = "SUARA MANUSIA TERJEMAHAN",
            urduVoiceTitle = "Urdu (اردو)",
            urduVoiceDesc = "Shamshad Ali Khan (Suara Studio)",
            englishVoiceTitle = "Inggris (English)",
            englishVoiceDesc = "Ibrahim Walk (Sahih Internasional)",
            pillArabic = "🎙️ Arab",
            pillTarjuma = "🗣️ Terjemahan",
            pillArabicAndTarjuma = "🔄 Arab+Terjemahan"
        )
        "tr" -> QuranAudioModeStrings(
            sectionMode = "SES ÇALMA MODU",
            arabicOnlyTitle = "🎙️ Yalnızca Arapça",
            arabicOnlyDesc = "Orijinal Kâri Tilaveti (Varsayılan)",
            tarjumaOnlyTitle = "🗣️ Yalnızca Meal / Tercüme",
            tarjumaOnlyDesc = "Yalnızca İnsan Sesiyle Tercüme",
            arabicAndTarjumaTitle = "🔄 Arapça + Tercüme",
            arabicAndTarjumaDesc = "Önce Arapça ayet, ardından meali",
            sectionVoice = "TERCÜME İNSAN SESİ",
            urduVoiceTitle = "Urduca (اردو)",
            urduVoiceDesc = "Shamshad Ali Khan (Stüdyo Kaydı)",
            englishVoiceTitle = "İngilizce (English)",
            englishVoiceDesc = "Ibrahim Walk (Sahih International)",
            pillArabic = "🎙️ Arapça",
            pillTarjuma = "🗣️ Meal",
            pillArabicAndTarjuma = "🔄 Arapça+Meal"
        )
        "fr" -> QuranAudioModeStrings(
            sectionMode = "MODE AUDIO",
            arabicOnlyTitle = "🎙️ Arabe uniquement",
            arabicOnlyDesc = "Récitation originale du Qari (Défaut)",
            tarjumaOnlyTitle = "🗣️ Traduction uniquement",
            tarjumaOnlyDesc = "Voix humaine de traduction",
            arabicAndTarjumaTitle = "🔄 Arabe + Traduction",
            arabicAndTarjumaDesc = "Verset arabe puis sa traduction",
            sectionVoice = "VOIX HUMAINE DE TRADUCTION",
            urduVoiceTitle = "Ourdou (Urdu)",
            urduVoiceDesc = "Shamshad Ali Khan (Voix studio)",
            englishVoiceTitle = "Anglais (English)",
            englishVoiceDesc = "Ibrahim Walk (Sahih Int.)",
            pillArabic = "🎙️ Arabe",
            pillTarjuma = "🗣️ Traduction",
            pillArabicAndTarjuma = "🔄 Arabe+Trad."
        )
        "es" -> QuranAudioModeStrings(
            sectionMode = "MODO DE AUDIO",
            arabicOnlyTitle = "🎙️ Solo Árabe",
            arabicOnlyDesc = "Recitación original del Qari (Predeterminado)",
            tarjumaOnlyTitle = "🗣️ Solo Traducción",
            tarjumaOnlyDesc = "Solo voz humana de traducción",
            arabicAndTarjumaTitle = "🔄 Árabe + Traducción",
            arabicAndTarjumaDesc = "Verso en árabe y luego su traducción",
            sectionVoice = "VOZ HUMANA DE TRADUCCIÓN",
            urduVoiceTitle = "Urdu (اردو)",
            urduVoiceDesc = "Shamshad Ali Khan (Voz de estudio)",
            englishVoiceTitle = "Inglés (English)",
            englishVoiceDesc = "Ibrahim Walk (Sahih Int.)",
            pillArabic = "🎙️ Árabe",
            pillTarjuma = "🗣️ Traducción",
            pillArabicAndTarjuma = "🔄 Árabe+Trad."
        )
        "ru" -> QuranAudioModeStrings(
            sectionMode = "РЕЖИМ АУДИО",
            arabicOnlyTitle = "🎙️ Только арабский",
            arabicOnlyDesc = "Чтение чтеца (По умолчанию)",
            tarjumaOnlyTitle = "🗣️ Только перевод",
            tarjumaOnlyDesc = "Живой голос перевода",
            arabicAndTarjumaTitle = "🔄 Арабский + Перевод",
            arabicAndTarjumaDesc = "Аят на арабском, затем перевод",
            sectionVoice = "ГОЛОС ПЕРЕВОДА",
            urduVoiceTitle = "Урду (Urdu)",
            urduVoiceDesc = "Шамшад Али Хан (Студийный голос)",
            englishVoiceTitle = "Английский (English)",
            englishVoiceDesc = "Ибрагим Волк (Сахих Инт.)",
            pillArabic = "🎙️ Арабский",
            pillTarjuma = "🗣️ Перевод",
            pillArabicAndTarjuma = "🔄 Араб.+Перевод"
        )
        "de" -> QuranAudioModeStrings(
            sectionMode = "AUDIO-MODUS",
            arabicOnlyTitle = "🎙️ Nur Arabisch",
            arabicOnlyDesc = "Original Qari Rezitation (Standard)",
            tarjumaOnlyTitle = "🗣️ Nur Übersetzung",
            tarjumaOnlyDesc = "Menschliche Stimme Übersetzung",
            arabicAndTarjumaTitle = "🔄 Arabisch + Übersetzung",
            arabicAndTarjumaDesc = "Arabischer Vers, dann Übersetzung",
            sectionVoice = "ÜBERSETZUNGS-STIMME",
            urduVoiceTitle = "Urdu (اردو)",
            urduVoiceDesc = "Shamshad Ali Khan (Studio-Aufnahme)",
            englishVoiceTitle = "Englisch (English)",
            englishVoiceDesc = "Ibrahim Walk (Sahih Int.)",
            pillArabic = "🎙️ Arabisch",
            pillTarjuma = "🗣️ Übersetz.",
            pillArabicAndTarjuma = "🔄 Arab.+Übersetz."
        )
        "it" -> QuranAudioModeStrings(
            sectionMode = "MODALITÀ AUDIO",
            arabicOnlyTitle = "🎙️ Solo Arabo",
            arabicOnlyDesc = "Recitazione originale del Qari (Predefinito)",
            tarjumaOnlyTitle = "🗣️ Solo Traduzione",
            tarjumaOnlyDesc = "Solo voce umana di traduzione",
            arabicAndTarjumaTitle = "🔄 Arabo + Traduzione",
            arabicAndTarjumaDesc = "Versetto arabo e poi la traduzione",
            sectionVoice = "VOCE UMANA TRADUZIONE",
            urduVoiceTitle = "Urdu (اردو)",
            urduVoiceDesc = "Shamshad Ali Khan (Voce studio)",
            englishVoiceTitle = "Inglese (English)",
            englishVoiceDesc = "Ibrahim Walk (Sahih Int.)",
            pillArabic = "🎙️ Arabo",
            pillTarjuma = "🗣️ Traduzione",
            pillArabicAndTarjuma = "🔄 Arabo+Trad."
        )
        else -> QuranAudioModeStrings(
            sectionMode = "PLAYBACK AUDIO MODE",
            arabicOnlyTitle = "🎙️ Arabic Only (عربية)",
            arabicOnlyDesc = "Original Qari Recitation (Default)",
            tarjumaOnlyTitle = "🗣️ Tarjuma Only (ترجمة)",
            tarjumaOnlyDesc = "Human Voice Translation Only",
            arabicAndTarjumaTitle = "🔄 Arabic + Tarjuma (عربية + ترجمة)",
            arabicAndTarjumaDesc = "Arabic Ayah, then its Human Tarjuma",
            sectionVoice = "TARJUMA HUMAN VOICE",
            urduVoiceTitle = "Urdu (اردو)",
            urduVoiceDesc = "Shamshad Ali Khan (Studio Human Voice)",
            englishVoiceTitle = "English",
            englishVoiceDesc = "Ibrahim Walk (Saheeh Int. Human Voice)",
            pillArabic = "🎙️ Arabic",
            pillTarjuma = "🗣️ Tarjuma",
            pillArabicAndTarjuma = "🔄 Arabic+Tarjuma"
        )
    }
}

/**
 * Luxury Dark Emerald Green & White/Gold Mode Selector
 * Fully localized according to user's chosen language.
 */
@Composable
fun QuranAudioModeButton(
    audioPlayMode: QuranAudioPlayMode,
    translationVoiceLang: String,
    onModeChange: (QuranAudioPlayMode, String) -> Unit,
    modifier: Modifier = Modifier,
    language: String? = null,
    isNightMode: Boolean = false
) {
    val activeLang = language ?: QuranRepository.loadSettings().translationLanguage
    val strings = remember(activeLang) { getQuranAudioModeStrings(activeLang) }

    var isMenuOpen by remember { mutableStateOf(false) }

    val voiceCode = if (translationVoiceLang == "en") "EN" else "UR"
    val displayText = when (audioPlayMode) {
        QuranAudioPlayMode.ARABIC_ONLY -> strings.pillArabic
        QuranAudioPlayMode.TRANSLATION_ONLY -> "${strings.pillTarjuma} ($voiceCode)"
        QuranAudioPlayMode.ARABIC_AND_TRANSLATION -> strings.pillArabicAndTarjuma
    }

    val buttonBg = if (isNightMode) Color(0xFF042016) else Color(0xFF0D4A3E)
    val buttonBorder = GoldAccent.copy(alpha = 0.65f)

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(14.dp))
                .clip(RoundedCornerShape(14.dp))
                .background(buttonBg)
                .border(width = 1.2.dp, color = buttonBorder, shape = RoundedCornerShape(14.dp))
                .clickable { isMenuOpen = true }
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = displayText,
                color = Color.White,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.2.sp
            )
            Text(
                text = "▾",
                color = GoldAccent,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        DropdownMenu(
            expanded = isMenuOpen,
            onDismissRequest = { isMenuOpen = false },
            modifier = Modifier
                .background(Color(0xFFFFFDF8))
                .border(1.dp, GoldAccent.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
        ) {
            // Section 1: Playback Mode
            Text(
                text = strings.sectionMode.uppercase(),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFB45309),
                fontFamily = FontFamily.Serif,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
            )

            // 1. Arabic Only
            DropdownMenuItem(
                text = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = strings.arabicOnlyTitle,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0D4A3E)
                            )
                            Text(
                                text = strings.arabicOnlyDesc,
                                fontSize = 10.5.sp,
                                color = Color(0xFF6B7280)
                            )
                        }
                        if (audioPlayMode == QuranAudioPlayMode.ARABIC_ONLY) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF0D4A3E)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                        }
                    }
                },
                onClick = {
                    onModeChange(QuranAudioPlayMode.ARABIC_ONLY, translationVoiceLang)
                    isMenuOpen = false
                }
            )

            // 2. Tarjuma Only
            DropdownMenuItem(
                text = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = strings.tarjumaOnlyTitle,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0D4A3E)
                            )
                            Text(
                                text = strings.tarjumaOnlyDesc,
                                fontSize = 10.5.sp,
                                color = Color(0xFF6B7280)
                            )
                        }
                        if (audioPlayMode == QuranAudioPlayMode.TRANSLATION_ONLY) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF0D4A3E)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                        }
                    }
                },
                onClick = {
                    onModeChange(QuranAudioPlayMode.TRANSLATION_ONLY, translationVoiceLang)
                    isMenuOpen = false
                }
            )

            // 3. Arabic + Tarjuma
            DropdownMenuItem(
                text = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = strings.arabicAndTarjumaTitle,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0D4A3E)
                            )
                            Text(
                                text = strings.arabicAndTarjumaDesc,
                                fontSize = 10.5.sp,
                                color = Color(0xFF6B7280)
                            )
                        }
                        if (audioPlayMode == QuranAudioPlayMode.ARABIC_AND_TRANSLATION) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF0D4A3E)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                        }
                    }
                },
                onClick = {
                    onModeChange(QuranAudioPlayMode.ARABIC_AND_TRANSLATION, translationVoiceLang)
                    isMenuOpen = false
                }
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp),
                color = GoldAccent.copy(alpha = 0.3f)
            )

            // Section 2: Human Voice Language
            Text(
                text = strings.sectionVoice.uppercase(),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFB45309),
                fontFamily = FontFamily.Serif,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
            )

            // Voice 1: Urdu
            DropdownMenuItem(
                text = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = strings.urduVoiceTitle,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0D4A3E)
                            )
                            Text(
                                text = strings.urduVoiceDesc,
                                fontSize = 10.5.sp,
                                color = Color(0xFF6B7280)
                            )
                        }
                        if (translationVoiceLang == "ur") {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF0D4A3E)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                        }
                    }
                },
                onClick = {
                    onModeChange(audioPlayMode, "ur")
                    isMenuOpen = false
                }
            )

            // Voice 2: English
            DropdownMenuItem(
                text = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = strings.englishVoiceTitle,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0D4A3E)
                            )
                            Text(
                                text = strings.englishVoiceDesc,
                                fontSize = 10.5.sp,
                                color = Color(0xFF6B7280)
                            )
                        }
                        if (translationVoiceLang == "en") {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF0D4A3E)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                        }
                    }
                },
                onClick = {
                    onModeChange(audioPlayMode, "en")
                    isMenuOpen = false
                }
            )
        }
    }
}
