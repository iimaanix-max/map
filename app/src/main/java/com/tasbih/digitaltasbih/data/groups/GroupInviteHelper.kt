package com.tasbih.digitaltasbih.data.groups

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.tasbih.digitaltasbih.data.PrayerRepository
import com.tasbih.digitaltasbih.ui.components.AppToast
import java.text.NumberFormat
import java.util.Locale

data class InviteTranslations(
    val salam: String,
    val creatorInvitePrivate: String,
    val creatorInviteNormal: String,
    val memberInvitePrivate: String,
    val memberInviteNormal: String,
    val zikrPrefix: String,
    val targetTemplate: String,
    val organizedByPrefix: String,
    val defaultCreatorName: String,
    val purposePrefix: String,
    val oneClickTitle: String,
    val oneClickSubtitle: String,
    val manualPrivateTitle: String,
    val privateCodeLabel: String,
    val codeStepsTitle: String,
    val codeStep1: String,
    val codeStep2: String,
    val codeStep3Template: String,
    val normalStepsTitle: String,
    val normalStep1: String,
    val normalStep2: String,
    val normalStep3Template: String,
    val rewardText: String
)

object GroupInviteHelper {

    const val DEEP_LINK_BASE = "https://join.imaanix.in"

    fun getDeepLinkUrl(joinCodeOrId: String): String {
        val clean = joinCodeOrId.trim()
        return "$DEEP_LINK_BASE/?code=$clean"
    }

    private val translations: Map<String, InviteTranslations> = mapOf(
        // ── English ──
        "en" to InviteTranslations(
            salam = "Assalamu Alaikum wa Rahmatullahi wa Barakatuh 🌿",
            creatorInvitePrivate = "You are cordially invited to join our Private Live Dhikr Group on Digital Tasbih:",
            creatorInviteNormal = "You are cordially invited to join our Global Live Dhikr Group on Digital Tasbih:",
            memberInvitePrivate = "Come join our Private Live Dhikr Group on Digital Tasbih!",
            memberInviteNormal = "Come join our Global Live Dhikr Group on Digital Tasbih!",
            zikrPrefix = "📿 Zikr:",
            targetTemplate = "🎯 Group Target: %s recitations",
            organizedByPrefix = "👤 Organized By:",
            defaultCreatorName = "A Devotee",
            purposePrefix = "📝 Purpose:",
            oneClickTitle = "✨ JOIN IN 1-CLICK (No Code Needed):",
            oneClickSubtitle = "(Tap the link above to open Digital Tasbih & start reciting immediately!)",
            manualPrivateTitle = "🔢 OR JOIN MANUALLY WITH CODE:",
            privateCodeLabel = "🔑 Private Invite Code:",
            codeStepsTitle = "📲 3 Easy Steps to Join with Code:",
            codeStep1 = "1️⃣ Open the Digital Tasbih App",
            codeStep2 = "2️⃣ Go to \"Live Dhikr\"",
            codeStep3Template = "3️⃣ Tap \"🔑 Join with Code\" & enter: %s",
            normalStepsTitle = "📲 OR FIND IN APP:",
            normalStep1 = "1️⃣ Open the Digital Tasbih App",
            normalStep2 = "2️⃣ Go to \"Live Dhikr\"",
            normalStep3Template = "3️⃣ Select \"%s\" to start reciting!",
            rewardText = "🤲 \"Whoever guides someone to goodness will have a reward like one who did it.\" (Sahih Muslim)\nLet's recite together and earn continuous rewards (Sadqah Jariyah)! 🤲"
        ),

        // ── Hindi ──
        "hi" to InviteTranslations(
            salam = "अस्सलामू अलैकुम व रहमतुल्लाहि व बरकातुहू 🌿",
            creatorInvitePrivate = "आपको डिजिटल तस्बीह पर हमारे प्राइवेट लाइव ज़िक्र ग्रुप में शामिल होने के लिए सादर आमंत्रित किया जाता है:",
            creatorInviteNormal = "आपको डिजिटल तस्बीह पर हमारे ग्लोबल लाइव ज़िक्र ग्रुप में शामिल होने के लिए सादर आमंत्रित किया जाता है:",
            memberInvitePrivate = "डिजिटल तस्बीह पर हमारे प्राइवेट लाइव ज़िक्र ग्रुप से जुड़ें!",
            memberInviteNormal = "डिजिटल तस्बीह पर हमारे ग्लोबल लाइव ज़िक्र ग्रुप से जुड़ें!",
            zikrPrefix = "📿 ज़िक्र:",
            targetTemplate = "🎯 ग्रुप लक्ष्य: %s बार पाठ",
            organizedByPrefix = "👤 आयोजक:",
            defaultCreatorName = "एक मोमिन",
            purposePrefix = "📝 उद्देश्य / नीयत:",
            oneClickTitle = "✨ 1-क्लिक में तुरंत जुड़ें (कोड की ज़रूरत नहीं):",
            oneClickSubtitle = "(ऊपर दिए गए लिंक पर टैप करें और ऐप खुलते ही तुरंत ज़िक्र शुरू करें!)",
            manualPrivateTitle = "🔢 या कोड डालकर मैन्युअली जुड़ें:",
            privateCodeLabel = "🔑 प्राइवेट इनवाइट कोड:",
            codeStepsTitle = "📲 कोड से जुड़ने के 3 आसान तरीके:",
            codeStep1 = "1️⃣ डिजिटल तस्बीह ऐप खोलें",
            codeStep2 = "2️⃣ \"लाइव ज़िक्र\" (Live Dhikr) पर जाएं",
            codeStep3Template = "3️⃣ \"🔑 Join with Code\" पर टैप करें और कोड डालें: %s",
            normalStepsTitle = "📲 या ऐप में सीधे देखें:",
            normalStep1 = "1️⃣ डिजिटल तस्बीह ऐप खोलें",
            normalStep2 = "2️⃣ \"लाइव ज़िक्र\" (Live Dhikr) पर जाएं",
            normalStep3Template = "3️⃣ \"%s\" पर टैप करके तुरंत पढ़ना शुरू करें!",
            rewardText = "🤲 \"जो किसी को भलाई का रास्ता दिखाता है, उसे भी उतना ही सवाब मिलता है जितना करने वाले को।\" (सहीह मुस्लिम)\nआइए मिलकर ज़िक्र करें और सदक़ा-ए-जारिया कमाएं! 🤲"
        ),

        // ── Urdu ──
        "ur" to InviteTranslations(
            salam = "السلام علیکم ورحمۃ اللہ وبرکاتہ 🌿",
            creatorInvitePrivate = "آپ کو ڈیجیٹل تسبیح پر ہمارے پرائیویٹ لائیو ذکر گروپ میں شرکت کی پرخلوص دعوت ہے:",
            creatorInviteNormal = "آپ کو ڈیجیٹل تسبیح پر ہمارے گلوبل لائیو ذکر گروپ میں شرکت کی پرخلوص دعوت ہے:",
            memberInvitePrivate = "ڈیجیٹل تسبیح پر ہمارے پرائیویٹ لائیو ذکر گروپ میں شامل ہوں!",
            memberInviteNormal = "ڈیجیٹل تسبیح پر ہمارے گلوبل لائیو ذکر گروپ میں شامل ہوں!",
            zikrPrefix = "📿 ذکر پاک:",
            targetTemplate = "🎯 گروپ ہدف: %s مرتبہ تسبیح",
            organizedByPrefix = "👤 منتظم:",
            defaultCreatorName = "ایک امتی",
            purposePrefix = "📝 مقصد / نیت:",
            oneClickTitle = "✨ صرف 1 کلک میں شامل ہوں (کوڈ کی ضرورت نہیں):",
            oneClickSubtitle = "(اوپر دیے گئے لنک پر ٹیپ کریں اور ایپ کھلتے ہی فورا ذکر شروع کریں!)",
            manualPrivateTitle = "🔢 یا کوڈ کے ذریعے دستی طور پر شامل ہوں:",
            privateCodeLabel = "🔑 پرائیویٹ انوائٹ کوڈ:",
            codeStepsTitle = "📲 کوڈ کے ساتھ شامل ہونے کے 3 آسان طریقے:",
            codeStep1 = "1️⃣ ڈیجیٹل تسبیح ایپ کھولیں",
            codeStep2 = "2️⃣ \"Live Dhikr\" پر جائیں",
            codeStep3Template = "3️⃣ \"🔑 Join with Code\" پر ٹیپ کریں اور کوڈ درج کریں: %s",
            normalStepsTitle = "📲 یا ایپ میں براہ راست دیکھیں:",
            normalStep1 = "1️⃣ ڈیجیٹل تسبیح ایپ کھولیں",
            normalStep2 = "2️⃣ \"Live Dhikr\" پر جائیں",
            normalStep3Template = "3️⃣ \"%s\" منتخب کریں اور ذکر شروع کریں!",
            rewardText = "🤲 \"جس نے کسی نیکی کی رہنمائی کی، اسے اتنا ہی اجر ملے گا جتنا اس نیکی کو کرنے والے کو۔\" (صحیح مسلم)\nآئیں مل کر ذکر کریں اور صدقہ جاریہ حاصل کریں! 🤲"
        ),

        // ── Hinglish ──
        "hinglish" to InviteTranslations(
            salam = "Assalamu Alaikum wa Rahmatullahi wa Barakatuh 🌿",
            creatorInvitePrivate = "Aapko Digital Tasbih par hamare Private Live Dhikr Group me shamil hone ki dawat di jaati hai:",
            creatorInviteNormal = "Aapko Digital Tasbih par hamare Global Live Dhikr Group me shamil hone ki dawat di jaati hai:",
            memberInvitePrivate = "Digital Tasbih par hamare Private Live Dhikr Group se judein!",
            memberInviteNormal = "Digital Tasbih par hamare Global Live Dhikr Group se judein!",
            zikrPrefix = "📿 Zikr:",
            targetTemplate = "🎯 Group Target: %s recitations",
            organizedByPrefix = "👤 Organized By:",
            defaultCreatorName = "A Devotee",
            purposePrefix = "📝 Purpose / Niyyah:",
            oneClickTitle = "✨ 1-CLICK ME TURANT JUDEIN (Code ki zaroorat nahi):",
            oneClickSubtitle = "(Upar diye link par tap karein aur app khulte hi direct zikr shuru karein!)",
            manualPrivateTitle = "🔢 YA CODE DAAL KAR JUDEIN:",
            privateCodeLabel = "🔑 Private Invite Code:",
            codeStepsTitle = "📲 Code se judne ke 3 aasan steps:",
            codeStep1 = "1️⃣ Digital Tasbih App kholein",
            codeStep2 = "2️⃣ \"Live Dhikr\" section me jayein",
            codeStep3Template = "3️⃣ \"🔑 Join with Code\" dabayein aur code dalein: %s",
            normalStepsTitle = "📲 YA DIRECT APP ME DEKHEIN:",
            normalStep1 = "1️⃣ Digital Tasbih App kholein",
            normalStep2 = "2️⃣ \"Live Dhikr\" section me jayein",
            normalStep3Template = "3️⃣ \"%s\" par tap karein aur padhna shuru karein!",
            rewardText = "🤲 \"Jo kisi ko bhalai ka rasta dikhata hai, use bhi utna hi sawab milta hai jitna karne wale ko.\" (Sahih Muslim)\nAaiye milkar zikr karein aur Sadqah Jariyah kamayein! 🤲"
        ),

        // ── Gujarati ──
        "gu" to InviteTranslations(
            salam = "અસ્સલામુ અલૈકુમ વ રહમતુલ્લાહિ વ બરકાતુહુ 🌿",
            creatorInvitePrivate = "તમને ડિજિટલ તસ્બીહ પર અમારા પ્રાઈવેટ લાઈવ ઝિક્ર ગ્રુપમાં જોડાવા માટે હાર્દિક આમંત્રણ છે:",
            creatorInviteNormal = "તમને ડિજિટલ તસ્બીહ પર અમારા ગ્લોબલ લાઈવ ઝિક્ર ગ્રુપમાં જોડાવા માટે હાર્દિક આમંત્રણ છે:",
            memberInvitePrivate = "ડિજિટલ તસ્બીહ પર અમારા પ્રાઈવેટ લાઈવ ઝિક્ર ગ્રુપ સાથે જોડાઓ!",
            memberInviteNormal = "ડિજિટલ તસ્બીહ પર અમારા ગ્લોબલ લાઈવ ઝિક્ર ગ્રુપ સાથે જોડાઓ!",
            zikrPrefix = "📿 ઝિક્ર:",
            targetTemplate = "🎯 ગ્રુપ લક્ષ્ય: %s વખત પઠન",
            organizedByPrefix = "👤 આયોજક:",
            defaultCreatorName = "એક મોમિન",
            purposePrefix = "📝 હેતુ / નિયત:",
            oneClickTitle = "✨ ફક્ત 1-ક્લિકમાં તરત જોડાઓ (કોડની જરૂર નથી):",
            oneClickSubtitle = "(ઉપરની લિંક પર ટેપ કરો અને ડિજિટલ તસ્બીહ એપમાં તરત પઠન શરૂ કરો!)",
            manualPrivateTitle = "🔢 અથવા કોડ દાખલ કરીને જોડાઓ:",
            privateCodeLabel = "🔑 પ્રાઈવેટ ઇનવાઇટ કોડ:",
            codeStepsTitle = "📲 કોડ સાથે જોડાવાના 3 સરળ પગલાં:",
            codeStep1 = "1️⃣ ડિજિટલ તસ્બીહ એપ ખોલો",
            codeStep2 = "2️⃣ \"Live Dhikr\" માં જાઓ",
            codeStep3Template = "3️⃣ \"🔑 Join with Code\" પર ટેપ કરી કોડ નાખો: %s",
            normalStepsTitle = "📲 અથવા એપમાં સીધા જુઓ:",
            normalStep1 = "1️⃣ ડિજિટલ તસ્બીહ એપ ખોલો",
            normalStep2 = "2️⃣ \"Live Dhikr\" માં જાઓ",
            normalStep3Template = "3️⃣ \"%s\" પર ટેપ કરો અને પઠન શરૂ કરો!",
            rewardText = "🤲 \"જે કોઈ ભલાઈનો રસ્તો બતાવે છે, તેને પણ તેટલો જ સવાબ મળે છે જેટલો કરનારને મળે છે.\" (સહીહ મુસ્લિમ)\nચાલો સાથે મળીને ઝિક્ર કરીએ અને સદકા-એ-જારિયાહ મેળવીએ! 🤲"
        ),

        // ── Bengali ──
        "bn" to InviteTranslations(
            salam = "আসসালামু আলাইকুম ওয়া রাহমাতুল্লাহি ওয়া বারাকাতুহু 🌿",
            creatorInvitePrivate = "আপনাকে ডিজিটাল তাসবীহ-তে আমাদের প্রাইভেট লাইভ জিকির গ্রুপে যোগদানের আন্তরিক আমন্ত্রণ জানানো হচ্ছে:",
            creatorInviteNormal = "আপনাকে ডিজিটাল তাসবীহ-তে আমাদের গ্লোবাল লাইভ জিকির গ্রুপে যোগদানের আন্তরিক আমন্ত্রণ জানানো হচ্ছে:",
            memberInvitePrivate = "ডিজিটাল তাসবীহ-তে আমাদের প্রাইভেট লাইভ জিকির গ্রুপে যোগ দিন!",
            memberInviteNormal = "ডিজিটাল তাসবীহ-তে আমাদের গ্লোবাল লাইভ জিকির গ্রুপে যোগ দিন!",
            zikrPrefix = "📿 জিকির:",
            targetTemplate = "🎯 গ্রুপ লক্ষ্য: %s বার পাঠ",
            organizedByPrefix = "👤 আয়োজক:",
            defaultCreatorName = "এক মুমিন",
            purposePrefix = "📝 উদ্দেশ্য / নিয়ত:",
            oneClickTitle = "✨ মাত্র ১-ক্লিকে যোগ দিন (কোনো কোডের প্রয়োজন নেই):",
            oneClickSubtitle = "(উপরের লিঙ্কে ট্যাপ করুন এবং ডিজিটাল তাসবীহ অ্যাপে সরাসরি পাঠ শুরু করুন!)",
            manualPrivateTitle = "🔢 অথবা কোড দিয়ে যোগ দিন:",
            privateCodeLabel = "🔑 প্রাইভেট ইনভাইট কোড:",
            codeStepsTitle = "📲 কোড দিয়ে যোগ দেওয়ার ৩টি সহজ ধাপ:",
            codeStep1 = "1️⃣ ডিজিটাল তাসবীহ অ্যাপ খুলুন",
            codeStep2 = "2️⃣ \"Live Dhikr\"-এ যান",
            codeStep3Template = "3️⃣ \"🔑 Join with Code\"-এ ট্যাপ করে কোডটি লিখুন: %s",
            normalStepsTitle = "📲 অথবা অ্যাপে সরাসরি খুঁজুন:",
            normalStep1 = "1️⃣ ডিজিটাল তাসবীহ অ্যাপ খুলুন",
            normalStep2 = "2️⃣ \"Live Dhikr\"-এ যান",
            normalStep3Template = "3️⃣ \"%s\" নির্বাচন করে পাঠ শুরু করুন!",
            rewardText = "🤲 \"যে ব্যক্তি কোনো ভালো কাজের পথ দেখায়, সে ওই কাজ সম্পাদনকারীর সমান সওয়াব পাবে।\" (সহীহ মুসলিম)\nআসুন একসাথে জিকির করি এবং সদকায়ে জারিয়াহ অর্জন করি! 🤲"
        ),

        // ── Tamil ──
        "ta" to InviteTranslations(
            salam = "அஸ்ஸலாமு அலைக்கும் வரஹ்மத்துல்லாஹி வபரகாத்துஹு 🌿",
            creatorInvitePrivate = "டிஜிட்டல் தஸ்பீஹில் எங்கள் பிரைவேட் லைவ் திக்ர் குழுவில் இணைய அன்புடன் அழைக்கிறோம்:",
            creatorInviteNormal = "டிஜிட்டல் தஸ்பீஹில் எங்கள் உலகளாவிய லைவ் திக்ர் குழுவில் இணைய அன்புடன் அழைக்கிறோம்:",
            memberInvitePrivate = "டிஜிட்டல் தஸ்பீஹில் எங்கள் பிரைவேட் லைவ் திக்ர் குழுவில் இணையுங்கள்!",
            memberInviteNormal = "டிஜிட்டல் தஸ்பீஹில் எங்கள் உலகளாவிய லைவ் திக்ர் குழுவில் இணையுங்கள்!",
            zikrPrefix = "📿 திக்ர்:",
            targetTemplate = "🎯 குழு இலக்கு: %s முறைகள்",
            organizedByPrefix = "👤 ஒருங்கிணைப்பாளர்:",
            defaultCreatorName = "ஒரு பக்தர்",
            purposePrefix = "📝 நோக்கம் / நிய்யத்:",
            oneClickTitle = "✨ ஒரே கிளிக்கில் உடனடியாக இணையுங்கள் (குறியீடு தேவையில்லை):",
            oneClickSubtitle = "(மேலே உள்ள இணைப்பைத் தட்டி செயலியைத் திறந்து உடனே திக்ர் ஓதத் தொடங்குங்கள்!)",
            manualPrivateTitle = "🔢 அல்லது குறியீட்டைப் பயன்படுத்தி இணையுங்கள்:",
            privateCodeLabel = "🔑 பிரைவேட் அழைப்புக் குறியீடு:",
            codeStepsTitle = "📲 குறியீட்டுடன் சேர 3 எளிய வழிகள்:",
            codeStep1 = "1️⃣ டிஜிட்டல் தஸ்பீஹ் செயலியைத் திறக்கவும்",
            codeStep2 = "2️⃣ \"Live Dhikr\" பகுதிக்குச் செல்லவும்",
            codeStep3Template = "3️⃣ \"🔑 Join with Code\" அழுத்தி குறியீட்டை உள்ளிடவும்: %s",
            normalStepsTitle = "📲 அல்லது செயலியில் நேரடியாகப் பாருங்கள்:",
            normalStep1 = "1️⃣ டிஜிட்டல் தஸ்பீஹ் செயலியைத் திறக்கவும்",
            normalStep2 = "2️⃣ \"Live Dhikr\" பகுதிக்குச் செல்லவும்",
            normalStep3Template = "3️⃣ \"%s\" என்பதைத் தேர்ந்தெடுத்து ஓதத் தொடங்குங்கள்!",
            rewardText = "🤲 \"ஒரு நன்மையை நோக்கி வழிகாட்டுபவருக்கு, அதைச் செய்தவரின் நன்மையைப் போன்றதே கிடைக்கும்.\" (ஸஹீஹ் முஸ்லிம்)\nஒன்றாக திக்ர் செய்து சதகாயே ஜாரியாவின் நன்மைகளைப் பெறுவோம்! 🤲"
        ),

        // ── Telugu ──
        "te" to InviteTranslations(
            salam = "అస్సలాము అలైకుమ్ వరహ్మతుల్లాహి వబరకాతుహు 🌿",
            creatorInvitePrivate = "డిజిటల్ తస్బీహ్‌లో మా ప్రైవేట్ లైవ్ జిక్ర్ గ్రూప్‌లో చేరడానికి మీకు సాదర ఆహ్వానం:",
            creatorInviteNormal = "డిజిటల్ తస్బీహ్‌లో మా గ్లోబల్ లైవ్ జిక్ర్ గ్రూప్‌లో చేరడానికి మీకు సాదర ఆహ్వానం:",
            memberInvitePrivate = "డిజిటల్ తస్బీహ్‌లో మా ప్రైవేట్ లైవ్ జిక్ర్ గ్రూప్‌లో చేరండి!",
            memberInviteNormal = "డిజిటల్ తస్బీహ్‌లో మా గ్లోబల్ లైవ్ జిక్ర్ గ్రూప్‌లో చేరండి!",
            zikrPrefix = "📿 జిక్ర్:",
            targetTemplate = "🎯 గ్రూప్ లక్ష్యం: %s సార్లు",
            organizedByPrefix = "👤 నిర్వహణ:",
            defaultCreatorName = "ఒక ముస్లిం",
            purposePrefix = "📝 ఉద్దేశం / నియ్యత్:",
            oneClickTitle = "✨ 1-క్లిక్‌తో వెంటనే చేరండి (కోడ్ అవసరం లేదు):",
            oneClickSubtitle = "(పై లింక్‌ను నొక్కండి మరియు యాప్‌లో వెంటనే పారాయణం ప్రారంభించండి!)",
            manualPrivateTitle = "🔢 లేదా కోడ్‌తో నేరుగా చేరండి:",
            privateCodeLabel = "🔑 ప్రైవేట్ ఆహ్వాన కోడ్:",
            codeStepsTitle = "📲 కోడ్‌తో చేరడానికి 3 సులభమైన దశలు:",
            codeStep1 = "1️⃣ డిజిటల్ తస్బీహ్ యాప్‌ని తెరవండి",
            codeStep2 = "2️⃣ \"Live Dhikr\" విభాగానికి వెళ్ళండి",
            codeStep3Template = "3️⃣ \"🔑 Join with Code\" నొక్కి కోడ్‌ను నమోదు చేయండి: %s",
            normalStepsTitle = "📲 లేదా యాప్‌లో నేరుగా చూడండి:",
            normalStep1 = "1️⃣ డిజిటల్ తస్బీహ్ యాప్‌ని తెరవండి",
            normalStep2 = "2️⃣ \"Live Dhikr\" విభాగానికి వెళ్ళండి",
            normalStep3Template = "3️⃣ \"%s\" ని ఎంచుకుని పారాయణం ప్రారంభించండి!",
            rewardText = "🤲 \"ఎవరైతే మంచి పనికి మార్గదర్శనం చేస్తారో, వారికి కూడా ఆ పని చేసినంతటి పుణ్యం దక్కుతుంది.\" (సహీహ్ ముస్లిం)\nకలిసి జిక్ర్ చేసి సదఖాయె జారియా పొందుదాం! 🤲"
        ),

        // ── Malayalam ──
        "ml" to InviteTranslations(
            salam = "അസ്സലാമു അലൈക്കും വറഹ്മത്തുല്ലാഹി വബറകാതുഹു 🌿",
            creatorInvitePrivate = "ഡിജിറ്റൽ തസ്ബീഹിലെ ഞങ്ങളുടെ പ്രൈവറ്റ് ലൈവ് ദിക്ർ ഗ്രൂപ്പിലേക്ക് നിങ്ങളെ സ്നേഹപൂർവ്വം ക്ഷണിക്കുന്നു:",
            creatorInviteNormal = "ഡിജിറ്റൽ തസ്ബീഹിലെ ഞങ്ങളുടെ ഗ്ലോബൽ ലൈവ് ദിക്ർ ഗ്രൂപ്പിലേക്ക് നിങ്ങളെ സ്നേഹപൂർവ്വം ക്ഷണിക്കുന്നു:",
            memberInvitePrivate = "ഡിജിറ്റൽ തസ്ബീഹിലെ ഞങ്ങളുടെ പ്രൈവറ്റ് ലൈവ് ദിക്ർ ഗ്രൂപ്പിൽ ചേരൂ!",
            memberInviteNormal = "ഡിജിറ്റൽ തസ്ബീഹിലെ ഞങ്ങളുടെ ഗ്ലോബൽ ലൈവ് ദിക്ർ ഗ്രൂപ്പിൽ ചേരൂ!",
            zikrPrefix = "📿 ദിക്ർ:",
            targetTemplate = "🎯 ഗ്രൂപ്പ് ലക്ഷ്യം: %s തവണ ചൊല്ലൽ",
            organizedByPrefix = "👤 സംഘാടകൻ:",
            defaultCreatorName = "ഒരു വിശ്വാസി",
            purposePrefix = "📝 ഉദ്ദേശ്യം / നിയ്യത്ത്:",
            oneClickTitle = "✨ 1-ക്ലിക്കിൽ ഉടൻ ചേരൂ (കോഡ് ആവശ്യമില്ല):",
            oneClickSubtitle = "(മുകളിലുള്ള ലിങ്കിൽ തൊട്ട് ആപ്പ് തുറന്ന് ഉടൻ ദിക്ർ ചൊല്ലി തുടങ്ങൂ!)",
            manualPrivateTitle = "🔢 അല്ലെങ്കിൽ കോഡ് ഉപയോഗിച്ച് ചേരൂ:",
            privateCodeLabel = "🔑 പ്രൈവറ്റ് ക്ഷണം കോഡ്:",
            codeStepsTitle = "📲 കോഡ് ഉപയോഗിച്ച് ചേരാൻ 3 എളുപ്പവഴികൾ:",
            codeStep1 = "1️⃣ ഡിജിറ്റൽ തസ്ബീഹ് ആപ്പ് തുറക്കുക",
            codeStep2 = "2️⃣ \"Live Dhikr\" ലേക്ക് പോവുക",
            codeStep3Template = "3️⃣ \"🔑 Join with Code\" ക്ലിക്ക് ചെയ്ത് കോഡ് നൽകുക: %s",
            normalStepsTitle = "📲 അല്ലെങ്കിൽ ആപ്പിൽ നേരിട്ട് കണ്ടെത്തൂ:",
            normalStep1 = "1️⃣ ഡിജിറ്റൽ തസ്ബീഹ് ആപ്പ് തുറക്കുക",
            normalStep2 = "2️⃣ \"Live Dhikr\" ലേക്ക് പോവുക",
            normalStep3Template = "3️⃣ \"%s\" തിരഞ്ഞെടുത്ത് ദിക്ർ ചൊല്ലി തുടങ്ങൂ!",
            rewardText = "🤲 \"നന്മയിലേക്ക് വഴി കാണിക്കുന്നവന് അത് ചെയ്യുന്നവന്റെ പ്രതിഫലം ലഭിക്കും.\" (സ്വഹീഹ് മുസ്ലിം)\nനമുക്ക് ഒരുമിച്ച് ദിക്ർ ചൊല്ലി സദഖ ജാരിയ നേടാം! 🤲"
        ),

        // ── Kannada ──
        "kn" to InviteTranslations(
            salam = "ಅಸ್ಸಲಾಮು ಅಲೈಕುಮ್ ವ ರಹ್ಮತುಲ್ಲಾಹಿ ವ ಬರಕಾತುಹು 🌿",
            creatorInvitePrivate = "ಡಿಜಿಟಲ್ ತಸ್ಬೀಹ್‌ನಲ್ಲಿ ನಮ್ಮ ಪ್ರೈವೇಟ್ ಲೈವ್ ಧಿಕ್ರ್ ಗ್ರೂಪ್‌ಗೆ ಸೇರಲು ನಿಮಗೆ ಆತ್ಮೀಯ ಸ್ವಾಗತ:",
            creatorInviteNormal = "ಡಿಜಿಟಲ್ ತಸ್ಬೀಹ್‌ನಲ್ಲಿ ನಮ್ಮ ಗ್ಲೋಬಲ್ ಲೈವ್ ಧಿಕ್ರ್ ಗ್ರೂಪ್‌ಗೆ ಸೇರಲು ನಿಮಗೆ ಆತ್ಮೀಯ ಸ್ವಾಗತ:",
            memberInvitePrivate = "ಡಿಜಿಟಲ್ ತಸ್ಬೀಹ್‌ನಲ್ಲಿ ನಮ್ಮ ಪ್ರೈವೇಟ್ ಲೈವ್ ಧಿಕ್ರ್ ಗ್ರೂಪ್‌ಗೆ ಸೇರಿಕೊಳ್ಳಿ!",
            memberInviteNormal = "ಡಿಜಿಟಲ್ ತಸ್ಬೀಹ್‌ನಲ್ಲಿ ನಮ್ಮ ಗ್ಲೋಬಲ್ ಲೈವ್ ಧಿಕ್ರ್ ಗ್ರೂಪ್‌ಗೆ ಸೇರಿಕೊಳ್ಳಿ!",
            zikrPrefix = "📿 ಧಿಕ್ರ್:",
            targetTemplate = "🎯 ಗುಂಪಿನ ಗುರಿ: %s ಬಾರಿ",
            organizedByPrefix = "👤 ಆಯೋಜಕರು:",
            defaultCreatorName = "ಒಬ್ಬ ಮುಅಮಿನ್",
            purposePrefix = "📝 ಉದ್ದೇಶ / ನಿಯ್ಯತ್:",
            oneClickTitle = "✨ ಕೇವಲ 1-ಕ್ಲಿಕ್‌ನಲ್ಲಿ ಸೇರಿ (ಕೋಡ್ ಅಗತ್ಯವಿಲ್ಲ):",
            oneClickSubtitle = "(ಮೇಲಿನ ಲಿಂಕ್ ಕ್ಲಿಕ್ ಮಾಡಿ ಆ್ಯಪ್ ತೆರೆದು ತಕ್ಷಣ ಧಿಕ್ರ್ ಆರಂಭಿಸಿ!)",
            manualPrivateTitle = "🔢 ಅಥವಾ ಕೋಡ್ ನಮೂದಿಸಿ ಸೇರಿಕೊಳ್ಳಿ:",
            privateCodeLabel = "🔑 ಪ್ರೈವೇಟ್ ಆಹ್ವಾನ ಕೋಡ್:",
            codeStepsTitle = "📲 ಕೋಡ್‌ನೊಂದಿಗೆ ಸೇರಲು 3 ಸುಲಭ ಹಂತಗಳು:",
            codeStep1 = "1️⃣ ಡಿಜಿಟಲ್ ತಸ್ಬೀಹ್ ಆ್ಯಪ್ ತೆರೆಯಿರಿ",
            codeStep2 = "2️⃣ \"Live Dhikr\" ಗೆ ಹೋಗಿ",
            codeStep3Template = "3️⃣ \"🔑 Join with Code\" ಮೇಲೆ ಕ್ಲಿಕ್ ಮಾಡಿ ಕೋಡ್ ಹಾಕಿ: %s",
            normalStepsTitle = "📲 ಅಥವಾ ಆ್ಯಪ್‌ನಲ್ಲಿ ನೇರವಾಗಿ ಹುಡುಕಿ:",
            normalStep1 = "1️⃣ ಡಿಜಿಟಲ್ ತಸ್ಬೀಹ್ ಆ್ಯಪ್ ತೆರೆಯಿರಿ",
            normalStep2 = "2️⃣ \"Live Dhikr\" ಗೆ ಹೋಗಿ",
            normalStep3Template = "3️⃣ \"%s\" ಅನ್ನು ಆಯ್ಕೆ ಮಾಡಿ ಪಠಣ ಆರಂಭಿಸಿ!",
            rewardText = "🤲 \"ಯಾರು ಒಳ್ಳೆಯ ಕಾರ್ಯಕ್ಕೆ ಮಾರ್ಗದರ್ಶನ ನೀಡುತ್ತಾರೋ, ಅವರಿಗೆ ಅದನ್ನು ಮಾಡಿದಷ್ಟೇ ಪುಣ್ಯ ಸಿಗುತ್ತದೆ.\" (ಸಹೀಹ್ ಮುಸ್ಲಿಂ)\nಒಟ್ಟಾಗಿ ಧಿಕ್ರ್ ಮಾಡಿ ಸದ್ಖಾ ಜಾರಿಯಾ ಪಡೆಯೋಣ! 🤲"
        ),

        // ── Punjabi ──
        "pa" to InviteTranslations(
            salam = "ਅਸਲਾਮੁ ਅਲੈਕੁਮ ਵ ਰਹਿਮਤੁੱਲਾਹੀ ਵ ਬਰਕਾਤੁਹੂ 🌿",
            creatorInvitePrivate = "ਤੁਹਾਨੂੰ ਡਿਜੀਟਲ ਤਸਬੀਹ 'ਤੇ ਸਾਡੇ ਪ੍ਰਾਈਵੇਟ ਲਾਈਵ ਜ਼ਿਕਰ ਗਰੁੱਪ ਵਿੱਚ ਸ਼ਾਮਲ ਹੋਣ ਦਾ ਸੱਦਾ ਦਿੱਤਾ ਜਾਂਦਾ ਹੈ:",
            creatorInviteNormal = "ਤੁਹਾਨੂੰ ਡਿਜੀਟਲ ਤਸਬੀਹ 'ਤੇ ਸਾਡੇ ਗਲੋਬਲ ਲਾਈਵ ਜ਼ਿਕਰ ਗਰੁੱਪ ਵਿੱਚ ਸ਼ਾਮਲ ਹੋਣ ਦਾ ਸੱਦਾ ਦਿੱਤਾ ਜਾਂਦਾ ਹੈ:",
            memberInvitePrivate = "ਡਿਜੀਟਲ ਤਸਬੀਹ 'ਤੇ ਸਾਡੇ ਪ੍ਰਾਈਵੇਟ ਲਾਈਵ ਜ਼ਿਕਰ ਗਰੁੱਪ ਨਾਲ ਜੁੜੋ!",
            memberInviteNormal = "ਡਿਜੀਟਲ ਤਸਬੀਹ 'ਤੇ ਸਾਡੇ ਗਲੋਬਲ ਲਾਈਵ ਜ਼ਿਕਰ ਗਰੁੱਪ ਨਾਲ ਜੁੜੋ!",
            zikrPrefix = "📿 ਜ਼ਿਕਰ:",
            targetTemplate = "🎯 ਗਰੁੱਪ ਟੀਚਾ: %s ਵਾਰ ਪਾਠ",
            organizedByPrefix = "👤 ਪ੍ਰਬੰਧਕ:",
            defaultCreatorName = "ਇੱਕ ਮੋਮਿਨ",
            purposePrefix = "📝 ਮਕਸਦ / ਨੀਯਤ:",
            oneClickTitle = "✨ 1-ਕਲਿੱਕ ਵਿੱਚ ਤੁਰੰਤ ਜੁੜੋ (ਕੋਡ ਦੀ ਲੋੜ ਨਹੀਂ):",
            oneClickSubtitle = "(ਉੱਪਰ ਦਿੱਤੇ ਲਿੰਕ 'ਤੇ ਟੈਪ ਕਰੋ ਅਤੇ ਐਪ ਖੁੱਲ੍ਹਦੇ ਹੀ ਤੁਰੰਤ ਤਸਬੀਹ ਸ਼ੁਰੂ ਕਰੋ!)",
            manualPrivateTitle = "🔢 ਜਾਂ ਕੋਡ ਰਾਹੀਂ ਸ਼ਾਮਲ ਹੋਵੋ:",
            privateCodeLabel = "🔑 ਪ੍ਰਾਈਵੇਟ ਇਨਵਾਈਟ ਕੋਡ:",
            codeStepsTitle = "📲 ਕੋਡ ਨਾਲ ਜੁੜਨ ਦੇ 3 ਆਸਾਨ ਤਰੀਕੇ:",
            codeStep1 = "1️⃣ ਡਿਜੀਟਲ ਤਸਬੀਹ ਐਪ ਖੋਲ੍ਹੋ",
            codeStep2 = "2️⃣ \"Live Dhikr\" 'ਤੇ ਜਾਓ",
            codeStep3Template = "3️⃣ \"🔑 Join with Code\" 'ਤੇ ਟੈਪ ਕਰੋ ਅਤੇ ਕੋਡ ਦਰਜ ਕਰੋ: %s",
            normalStepsTitle = "📲 ਜਾਂ ਐਪ ਵਿੱਚ ਸਿੱਧਾ ਦੇਖੋ:",
            normalStep1 = "1️⃣ ਡਿਜੀਟਲ ਤਸਬੀਹ ਐਪ ਖੋਲ੍ਹੋ",
            normalStep2 = "2️⃣ \"Live Dhikr\" 'ਤੇ ਜਾਓ",
            normalStep3Template = "3️⃣ \"%s\" ਚੁਣੋ ਅਤੇ ਪਾਠ ਸ਼ੁਰੂ ਕਰੋ!",
            rewardText = "🤲 \"ਜੋ ਕਿਸੇ ਨੂੰ ਭਲਾਈ ਦਾ ਰਾਹ ਦਿਖਾਉਂਦਾ ਹੈ, ਉਸਨੂੰ ਵੀ ਓਨਾ ਹੀ ਸਵਾਬ ਮਿਲਦਾ ਹੈ ਜਿੰਨਾ ਕਰਨ ਵਾਲੇ ਨੂੰ।\" (ਸਹੀਹ ਮੁਸਲਿਮ)\nਆਓ ਰਲ ਕੇ ਜ਼ਿਕਰ ਕਰੀਏ ਅਤੇ ਸਦਕਾ-ਏ-ਜਾਰੀਆ ਕਮਾਈਏ! 🤲"
        ),

        // ── Marathi ──
        "mr" to InviteTranslations(
            salam = "अस्सलामु अलैकुम व रहमतुल्लाही व बरकातुहू 🌿",
            creatorInvitePrivate = "तुम्हाला डिजिटल तस्बीहवरील आमच्या खाजगी (Private) लाईव्ह झिक्र ग्रुपमध्ये सहभागी होण्यासाठी आदरपूर्वक आमंत्रित केले आहे:",
            creatorInviteNormal = "तुम्हाला डिजिटल तस्बीहवरील आमच्या ग्लोबल लाईव्ह झिक्र ग्रुपमध्ये सहभागी होण्यासाठी आदरपूर्वक आमंत्रित केले आहे:",
            memberInvitePrivate = "डिजिटल तस्बीहवरील आमच्या खाजगी लाईव्ह झिक्र ग्रुपमध्ये सामील व्हा!",
            memberInviteNormal = "डिजिटल तस्बीहवरील आमच्या ग्लोबल लाईव्ह झिक्र ग्रुपमध्ये सामील व्हा!",
            zikrPrefix = "📿 झिक्र:",
            targetTemplate = "🎯 ग्रुप उद्दिष्ट: %s वेळा",
            organizedByPrefix = "👤 आयोजक:",
            defaultCreatorName = "एक मोमीन",
            purposePrefix = "📝 हेतू / निय्यत:",
            oneClickTitle = "✨ एका क्लिकमध्ये लगेच सामील व्हा (कोणत्याही कोडची गरज नाही):",
            oneClickSubtitle = "(वरील लिंकवर टॅप करा आणि डिजिटल तस्बीह ॲपमध्ये लगेच झिक्र सुरू करा!)",
            manualPrivateTitle = "🔢 किंवा कोड टाकून मॅन्युअली सामील व्हा:",
            privateCodeLabel = "🔑 खाजगी इनव्हाઇટ कोड:",
            codeStepsTitle = "📲 कोडद्वारे सामील होण्यासाठी ३ सोप्या पायऱ्या:",
            codeStep1 = "1️⃣ डिजिटल तस्बीह ॲप उघडा",
            codeStep2 = "2️⃣ \"Live Dhikr\" विभागात जा",
            codeStep3Template = "3️⃣ \"🔑 Join with Code\" वर टॅप करा आणि कोड टाका: %s",
            normalStepsTitle = "📲 किंवा ॲपमध्ये थेट शोधा:",
            normalStep1 = "1️⃣ डिजिटल तस्बीह ॲप उघडा",
            normalStep2 = "2️⃣ \"Live Dhikr\" विभागात जा",
            normalStep3Template = "3️⃣ \"%s\" निवडा आणि लगेच झिक्र सुरू करा!",
            rewardText = "🤲 \"जो कोणाला चांगल्या मार्गावर चालण्याची प्रेरणा देतो, त्यालाही ते काम करणाऱ्याइतकेच पुण्य (सवाब) मिळते.\" (सहीह मुस्लिम)\nचला एकत्र येऊन झिक्र करूया आणि सदका-ए-जारिया मिळवूया! 🤲"
        ),

        // ── Arabic ──
        "ar" to InviteTranslations(
            salam = "السلام عليكم ورحمة الله وبركاته 🌿",
            creatorInvitePrivate = "يسعدنا ويشرفنا دعوتكم للانضمام إلى مجموعة الذكر المباشر الخاصة على تطبيق التسبيح الإلكتروني:",
            creatorInviteNormal = "يسعدنا ويشرفنا دعوتكم للانضمام إلى مجموعة الذكر المباشر العامة على تطبيق التسبيح الإلكتروني:",
            memberInvitePrivate = "تعال وانضم إلى مجموعة الذكر المباشر الخاصة على تطبيق التسبيح الإلكتروني!",
            memberInviteNormal = "تعال وانضم إلى مجموعة الذكر المباشر العامة على تطبيق التسبيح الإلكتروني!",
            zikrPrefix = "📿 الذكر المبارك:",
            targetTemplate = "🎯 الهدف الجماعي: %s مرة",
            organizedByPrefix = "👤 منظَّم من قِبل:",
            defaultCreatorName = "فاعل خير",
            purposePrefix = "📝 النية / الهدف:",
            oneClickTitle = "✨ انضم بنقرة واحدة (بدون الحاجة لرمز):",
            oneClickSubtitle = "(اضغط على الرابط أعلاه لفتح تطبيق التسبيح والبدء في الذكر فوراً!)",
            manualPrivateTitle = "🔢 أو انضم يدويًا برمز الدعوة:",
            privateCodeLabel = "🔑 رمز الدعوة الخاص:",
            codeStepsTitle = "📲 3 خطوات سهلة للانضمام برمز الدعوة:",
            codeStep1 = "1️⃣ افتح تطبيق التسبيح الإلكتروني (Digital Tasbih)",
            codeStep2 = "2️⃣ انتقل إلى قسم \"Live Dhikr\"",
            codeStep3Template = "3️⃣ اضغط على \"🔑 Join with Code\" وأدخل الرمز: %s",
            normalStepsTitle = "📲 أو افتح المجموعة مباشرة في التطبيق:",
            normalStep1 = "1️⃣ افتح تطبيق التسبيح الإلكتروني (Digital Tasbih)",
            normalStep2 = "2️⃣ انتقل إلى قسم \"Live Dhikr\"",
            normalStep3Template = "3️⃣ اختر \"%s\" وابدأ في التسبيح فوراً!",
            rewardText = "🤲 \"مَنْ دَلَّ عَلَى خَيْرٍ فَلَهُ مِثْلُ أَجْرِ فَاعِلِهِ\" (صحيح مسلم)\nفلنذكر الله معًا وننال أجر الصدقة الجارية! 🤲"
        ),

        // ── Indonesian ──
        "id" to InviteTranslations(
            salam = "Assalamu Alaikum wa Rahmatullahi wa Barakatuh 🌿",
            creatorInvitePrivate = "Anda dengan hormat diundang untuk bergabung dengan Grup Dzikir Bersama (Privat) di Digital Tasbih:",
            creatorInviteNormal = "Anda dengan hormat diundang untuk bergabung dengan Grup Dzikir Bersama Global di Digital Tasbih:",
            memberInvitePrivate = "Ayo bergabung dengan Grup Dzikir Bersama (Privat) di Digital Tasbih!",
            memberInviteNormal = "Ayo bergabung dengan Grup Dzikir Bersama Global di Digital Tasbih!",
            zikrPrefix = "📿 Dzikir:",
            targetTemplate = "🎯 Target Grup: %s bacaan",
            organizedByPrefix = "👤 Dibuat oleh:",
            defaultCreatorName = "Hamba Allah",
            purposePrefix = "📝 Niat / Tujuan:",
            oneClickTitle = "✨ GABUNG DALAM 1-KLIK (Tanpa Perlu Kode):",
            oneClickSubtitle = "(Ketuk tautan di atas untuk membuka aplikasi & langsung mulai berdzikir!)",
            manualPrivateTitle = "🔢 ATAU GABUNG MANUAL DENGAN KODE:",
            privateCodeLabel = "🔑 Kode Undangan Privat:",
            codeStepsTitle = "📲 3 Langkah Mudah Bergabung dengan Kode:",
            codeStep1 = "1️⃣ Buka Aplikasi Digital Tasbih",
            codeStep2 = "2️⃣ Buka menu \"Live Dhikr\"",
            codeStep3Template = "3️⃣ Ketuk \"🔑 Join with Code\" & masukkan kode: %s",
            normalStepsTitle = "📲 ATAU TEMUKAN LANGSUNG DI APLIKASI:",
            normalStep1 = "1️⃣ Buka Aplikasi Digital Tasbih",
            normalStep2 = "2️⃣ Buka menu \"Live Dhikr\"",
            normalStep3Template = "3️⃣ Pilih \"%s\" untuk mulai membaca!",
            rewardText = "🤲 \"Barangsiapa menunjukkan kepada kebaikan, maka baginya pahala seperti orang yang melakukannya.\" (HR. Muslim)\nMari berdzikir bersama dan raih pahala jariyah! 🤲"
        ),

        // ── Malay ──
        "ms" to InviteTranslations(
            salam = "Assalamu Alaikum wa Rahmatullahi wa Barakatuh 🌿",
            creatorInvitePrivate = "Anda dijemput untuk menyertai Kumpulan Zikir Bersama (Privat) di Digital Tasbih:",
            creatorInviteNormal = "Anda dijemput untuk menyertai Kumpulan Zikir Bersama Global di Digital Tasbih:",
            memberInvitePrivate = "Mari sertai Kumpulan Zikir Bersama (Privat) di Digital Tasbih!",
            memberInviteNormal = "Mari sertai Kumpulan Zikir Bersama Global di Digital Tasbih!",
            zikrPrefix = "📿 Zikir:",
            targetTemplate = "🎯 Sasaran Kumpulan: %s bacaan",
            organizedByPrefix = "👤 Dianjurkan oleh:",
            defaultCreatorName = "Hamba Allah",
            purposePrefix = "📝 Niat / Tujuan:",
            oneClickTitle = "✨ SERTAI DALAM 1-KLIK (Tanpa Perlu Kod):",
            oneClickSubtitle = "(Ketik pautan di atas untuk membuka aplikasi & terus berzikir!)",
            manualPrivateTitle = "🔢 ATAU SERTAI SECARA MANUAL DENGAN KOD:",
            privateCodeLabel = "🔑 Kod Jemputan Privat:",
            codeStepsTitle = "📲 3 Langkah Mudah Menyertai dengan Kod:",
            codeStep1 = "1️⃣ Buka Aplikasi Digital Tasbih",
            codeStep2 = "2️⃣ Pergi ke bahagian \"Live Dhikr\"",
            codeStep3Template = "3️⃣ Ketik \"🔑 Join with Code\" & masukkan kod: %s",
            normalStepsTitle = "📲 ATAU CARI TERUS DI APLIKASI:",
            normalStep1 = "1️⃣ Buka Aplikasi Digital Tasbih",
            normalStep2 = "2️⃣ Pergi ke bahagian \"Live Dhikr\"",
            normalStep3Template = "3️⃣ Pilih \"%s\" untuk mula berzikir!",
            rewardText = "🤲 \"Sesiapa yang menunjukkan kepada kebaikan, dia mendapat pahala seperti orang yang melakukannya.\" (Sahih Muslim)\nMari berzikir bersama dan raih pahala berterusan! 🤲"
        ),

        // ── Turkish ──
        "tr" to InviteTranslations(
            salam = "Selâmün Aleyküm ve Rahmetullâhi ve Berekâtüh 🌿",
            creatorInvitePrivate = "Digital Tasbih uygulamasında Özel Canlı Zikir Grubumuza katılmaya davetlisiniz:",
            creatorInviteNormal = "Digital Tasbih uygulamasında Genel Canlı Zikir Grubumuza katılmaya davetlisiniz:",
            memberInvitePrivate = "Digital Tasbih uygulamasında Özel Canlı Zikir Grubumuza sen de katıl!",
            memberInviteNormal = "Digital Tasbih uygulamasında Genel Canlı Zikir Grubumuza sen de katıl!",
            zikrPrefix = "📿 Zikir:",
            targetTemplate = "🎯 Grup Hedefi: %s adet",
            organizedByPrefix = "👤 Düzenleyen:",
            defaultCreatorName = "Bir Mümin",
            purposePrefix = "📝 Niyet / Amaç:",
            oneClickTitle = "✨ 1 TIKLA HEMEN KATILIN (Kod Gerekmez):",
            oneClickSubtitle = "(Yukarıdaki bağlantıya tıklayarak uygulamayı açıp hemen zikre başlayın!)",
            manualPrivateTitle = "🔢 VEYA KODLA MANUEL KATILIN:",
            privateCodeLabel = "🔑 Özel Davet Kodu:",
            codeStepsTitle = "📲 Kodla Katılmak İçin 3 Kolay Adım:",
            codeStep1 = "1️⃣ Digital Tasbih Uygulamasını Açın",
            codeStep2 = "2️⃣ \"Live Dhikr\" Bölümüne Gidin",
            codeStep3Template = "3️⃣ \"🔑 Join with Code\" Seçeneğine Dokunup Kodu Girin: %s",
            normalStepsTitle = "📲 VEYA UYGULAMADAN BULUN:",
            normalStep1 = "1️⃣ Digital Tasbih Uygulamasını Açın",
            normalStep2 = "2️⃣ \"Live Dhikr\" Bölümüne Gidin",
            normalStep3Template = "3️⃣ \"%s\" Başlığına Dokunup Zikre Başlayın!",
            rewardText = "🤲 \"Hayra vesile olan, o hayrı yapan gibidir.\" (Sahih Müslim)\nBirlikte zikredelim ve Sadaka-i Cariye kazanalım! 🤲"
        ),

        // ── Persian / Farsi ──
        "fa" to InviteTranslations(
            salam = "السلام علیکم ورحمة الله وبرکاته 🌿",
            creatorInvitePrivate = "با کمال احترام از شما دعوت می‌شود به گروه ذکر زنده اختصاصی ما در دیجیتال تسبیح بپیوندید:",
            creatorInviteNormal = "با کمال احترام از شما دعوت می‌شود به گروه ذکر زنده عمومی ما در دیجیتال تسبیح بپیوندید:",
            memberInvitePrivate = "به گروه ذکر زنده اختصاصی ما در دیجیتال تسبیح بپیوندید!",
            memberInviteNormal = "به گروه ذکر زنده عمومی ما در دیجیتال تسبیح بپیوندید!",
            zikrPrefix = "📿 ذکر شریف:",
            targetTemplate = "🎯 هدف گروه: %s بار ذکر",
            organizedByPrefix = "👤 برگزارکننده:",
            defaultCreatorName = "یک مؤمن",
            purposePrefix = "📝 هدف / نیت:",
            oneClickTitle = "✨ عضویت فقط با ۱ کلیک (بدون نیاز به کد):",
            oneClickSubtitle = "(روی لینک بالا کلیک کنید تا اپلیکیشن باز شده و فوراً ذکر را آغاز کنید!)",
            manualPrivateTitle = "🔢 یا عضویت دستی با کد:",
            privateCodeLabel = "🔑 کد دعوت اختصاصی:",
            codeStepsTitle = "📲 ۳ مرحله ساده برای عضویت با کد:",
            codeStep1 = "1️⃣ اپلیکیشن دیجیتال تسبیح را باز کنید",
            codeStep2 = "2️⃣ به بخش \"Live Dhikr\" بروید",
            codeStep3Template = "3️⃣ روی \"🔑 Join with Code\" ضربه بزنید و کد را وارد کنید: %s",
            normalStepsTitle = "📲 یا مستقیماً در اپلیکیشن بیابید:",
            normalStep1 = "1️⃣ اپلیکیشن دیجیتال تسبیح را باز کنید",
            normalStep2 = "2️⃣ به بخش \"Live Dhikr\" بروید",
            normalStep3Template = "3️⃣ \"%s\" را انتخاب کرده و ذکر گفتن را آغاز کنید!",
            rewardText = "🤲 «هر کس به کار نیکی راهنمایی کند، پاداشی همانند انجام‌دهنده آن خواهد داشت.» (صحیح مسلم)\nبیایید با هم ذکر بگوییم و ثواب صدقه جاریه کسب کنیم! 🤲"
        ),

        // ── French ──
        "fr" to InviteTranslations(
            salam = "Assalamu Alaikum wa Rahmatullahi wa Barakatuh 🌿",
            creatorInvitePrivate = "Vous êtes cordialement invité(e) à rejoindre notre groupe privé de Dhikr en direct sur Digital Tasbih :",
            creatorInviteNormal = "Vous êtes cordialement invité(e) à rejoindre notre groupe public de Dhikr en direct sur Digital Tasbih :",
            memberInvitePrivate = "Rejoignez notre groupe privé de Dhikr en direct sur Digital Tasbih !",
            memberInviteNormal = "Rejoignez notre groupe public de Dhikr en direct sur Digital Tasbih !",
            zikrPrefix = "📿 Dhikr :",
            targetTemplate = "🎯 Objectif du groupe : %s récitations",
            organizedByPrefix = "👤 Organisé par :",
            defaultCreatorName = "Un Croyant",
            purposePrefix = "📝 Intention / Motif :",
            oneClickTitle = "✨ REJOINDRE EN 1 CLIC (Aucun code requis) :",
            oneClickSubtitle = "(Appuyez sur le lien ci-dessus pour ouvrir l'application et commencer !)",
            manualPrivateTitle = "🔢 OU REJOIGNEZ MANUELLEMENT AVEC LE CODE :",
            privateCodeLabel = "🔑 Code d'invitation privé :",
            codeStepsTitle = "📲 3 étapes faciles pour rejoindre avec le code :",
            codeStep1 = "1️⃣ Ouvrez l'application Digital Tasbih",
            codeStep2 = "2️⃣ Allez dans la section « Live Dhikr »",
            codeStep3Template = "3️⃣ Appuyez sur « 🔑 Join with Code » et entrez le code : %s",
            normalStepsTitle = "📲 OU RETROUVEZ DANS L'APPLICATION :",
            normalStep1 = "1️⃣ Ouvrez l'application Digital Tasbih",
            normalStep2 = "2️⃣ Allez dans la section « Live Dhikr »",
            normalStep3Template = "3️⃣ Sélectionnez « %s » pour commencer à réciter !",
            rewardText = "🤲 « Celui qui guide vers un bien a la même récompense que celui qui l'a fait. » (Sahih Muslim)\nRécitons ensemble et gagnons une aumône continue (Sadaqah Jariyah) ! 🤲"
        ),

        // ── German ──
        "de" to InviteTranslations(
            salam = "Assalamu Alaikum wa Rahmatullahi wa Barakatuh 🌿",
            creatorInvitePrivate = "Du bist herzlich eingeladen, unserer privaten Live-Dhikr-Gruppe auf Digital Tasbih beizutreten:",
            creatorInviteNormal = "Du bist herzlich eingeladen, unserer öffentlichen Live-Dhikr-Gruppe auf Digital Tasbih beizutreten:",
            memberInvitePrivate = "Komm und schließe dich unserer privaten Live-Dhikr-Gruppe auf Digital Tasbih an!",
            memberInviteNormal = "Komm und schließe dich unserer öffentlichen Live-Dhikr-Gruppe auf Digital Tasbih an!",
            zikrPrefix = "📿 Dhikr:",
            targetTemplate = "🎯 Gruppenziel: %s Rezitationen",
            organizedByPrefix = "👤 Erstellt von:",
            defaultCreatorName = "Ein Gläubiger",
            purposePrefix = "📝 Absicht / Anlass:",
            oneClickTitle = "✨ MIT 1 KLICK BEITRETEN (Kein Code erforderlich):",
            oneClickSubtitle = "(Tippe auf den Link oben, um die App zu öffnen und sofort zu rezitieren!)",
            manualPrivateTitle = "🔢 ODER MANUELL MIT CODE BEITRETEN:",
            privateCodeLabel = "🔑 Privater Einladungscode:",
            codeStepsTitle = "📲 3 einfache Schritte zum Beitreten mit Code:",
            codeStep1 = "1️⃣ Öffne die Digital Tasbih App",
            codeStep2 = "2️⃣ Gehe zu \"Live Dhikr\"",
            codeStep3Template = "3️⃣ Tippe auf \"🔑 Join with Code\" & gib den Code ein: %s",
            normalStepsTitle = "📲 ODER IN DER APP FINDEN:",
            normalStep1 = "1️⃣ Öffne die Digital Tasbih App",
            normalStep2 = "2️⃣ Gehe zu \"Live Dhikr\"",
            normalStep3Template = "3️⃣ Wähle \"%s\" aus, um mit dem Rezitieren zu beginnen!",
            rewardText = "🤲 \"Wer zu etwas Gutem anleitet, erhält den gleichen Lohn wie der, der es tut.\" (Sahih Muslim)\nLasst uns gemeinsam gedenken und fortlaufenden Lohn erwerben! 🤲"
        ),

        // ── Spanish ──
        "es" to InviteTranslations(
            salam = "Assalamu Alaikum wa Rahmatullahi wa Barakatuh 🌿",
            creatorInvitePrivate = "Estás cordialmente invitado(a) a unirte a nuestro grupo privado de Dhikr en vivo en Digital Tasbih:",
            creatorInviteNormal = "Estás cordialmente invitado(a) a unirte a nuestro grupo público de Dhikr en vivo en Digital Tasbih:",
            memberInvitePrivate = "¡Únete a nuestro grupo privado de Dhikr en vivo en Digital Tasbih!",
            memberInviteNormal = "¡Únete a nuestro grupo público de Dhikr en vivo en Digital Tasbih!",
            zikrPrefix = "📿 Dhikr:",
            targetTemplate = "🎯 Objetivo del grupo: %s recitaciones",
            organizedByPrefix = "👤 Organizado por:",
            defaultCreatorName = "Un Creyente",
            purposePrefix = "📝 Propósito / Intención:",
            oneClickTitle = "✨ ÚNETE EN 1 CLIC (Sin código necesario):",
            oneClickSubtitle = "(¡Toca el enlace de arriba para abrir la app y comenzar de inmediato!)",
            manualPrivateTitle = "🔢 O ÚNETE MANUALMENTE CON CÓDIGO:",
            privateCodeLabel = "🔑 Código de invitación privado:",
            codeStepsTitle = "📲 3 sencillos pasos para unirse con código:",
            codeStep1 = "1️⃣ Abre la app Digital Tasbih",
            codeStep2 = "2️⃣ Ve a la sección \"Live Dhikr\"",
            codeStep3Template = "3️⃣ Toca \"🔑 Join with Code\" e ingresa el código: %s",
            normalStepsTitle = "📲 O ENCUÉNTRALO EN LA APP:",
            normalStep1 = "1️⃣ Abre la app Digital Tasbih",
            normalStep2 = "2️⃣ Ve a la sección \"Live Dhikr\"",
            normalStep3Template = "3️⃣ ¡Selecciona \"%s\" para comenzar a recitar!",
            rewardText = "🤲 \"Quien guía hacia el bien tendrá una recompensa similar a quien lo hace.\" (Sahih Muslim)\n¡Recitemos juntos y obtengamos recompensas continuas (Sadaqah Jariyah)! 🤲"
        ),

        // ── Russian ──
        "ru" to InviteTranslations(
            salam = "Ас-саляму алейкум ва рахматуллахи ва баракатух 🌿",
            creatorInvitePrivate = "Сердечно приглашаем вас присоединиться к нашей закрытой группе живого зикра в Digital Tasbih:",
            creatorInviteNormal = "Сердечно приглашаем вас присоединиться к нашей общей группе живого зикра в Digital Tasbih:",
            memberInvitePrivate = "Присоединяйтесь к нашей закрытой группе живого зикра в Digital Tasbih!",
            memberInviteNormal = "Присоединяйтесь к нашей общей группе живого зикра в Digital Tasbih!",
            zikrPrefix = "📿 Зикр:",
            targetTemplate = "🎯 Цель группы: %s раз",
            organizedByPrefix = "👤 Организатор:",
            defaultCreatorName = "Верующий",
            purposePrefix = "📝 Намерение / Цель:",
            oneClickTitle = "✨ ВХОД В 1 КЛИК (Код не требуется):",
            oneClickSubtitle = "(Нажмите на ссылку выше, чтобы открыть приложение и сразу начать зикр!)",
            manualPrivateTitle = "🔢 ИЛИ ВОЙДИТЕ ВРУЧНУЮ С КОДОМ:",
            privateCodeLabel = "🔑 Приватный код приглашения:",
            codeStepsTitle = "📲 3 простых шага для входа по коду:",
            codeStep1 = "1️⃣ Откройте приложение Digital Tasbih",
            codeStep2 = "2️⃣ Перейдите в раздел \"Live Dhikr\"",
            codeStep3Template = "3️⃣ Нажмите \"🔑 Join with Code\" и введите код: %s",
            normalStepsTitle = "📲 ИЛИ НАЙДИТЕ В ПРИЛОЖЕНИИ:",
            normalStep1 = "1️⃣ Откройте приложение Digital Tasbih",
            normalStep2 = "2️⃣ Перейдите в раздел \"Live Dhikr\"",
            normalStep3Template = "3️⃣ Выберите \"%s\" и начните читать зикр!",
            rewardText = "🤲 «Указавшему на благое полагается такая же награда, как и совершившему его.» (Сахих Муслим)\nДавайте читать зикр вместе и обретать непрерывную награду (Садака Джария)! 🤲"
        )
    )

    fun getTranslations(lang: String): InviteTranslations {
        val clean = lang.trim().lowercase()
        return translations[clean] ?: translations["en"]!!
    }

    /**
     * Blessed invite message for the Group Creator.
     * Multilingual: Uses the user's chosen language.
     * Normal groups contain NO code at all; Private groups include the private code and manual steps.
     */
    fun buildCreatorInviteMessage(
        campaign: CampaignItem,
        lang: String = PrayerRepository.getLanguage()
    ): String {
        val t = getTranslations(lang)
        val isPrivate = campaign.visibility == "private"
        val codeOrId = if (isPrivate) campaign.joinCode.ifBlank { campaign.campaignId } else campaign.campaignId
        val link = getDeepLinkUrl(codeOrId)

        val inviteHeader = if (isPrivate) t.creatorInvitePrivate else t.creatorInviteNormal
        val arabicText = if (campaign.arabic.isNotBlank()) "\n${campaign.arabic.trim()}\n" else ""
        val hasNote = campaign.note.isNotBlank() &&
                !campaign.note.trim().equals("null", ignoreCase = true) &&
                !campaign.note.trim().equals("None", ignoreCase = true)
        val noteSection = if (hasNote) "\n${t.purposePrefix} ${campaign.note.trim()}" else ""
        val creatorName = campaign.creatorName.ifBlank { t.defaultCreatorName }
        val targetFormatted = NumberFormat.getNumberInstance(Locale.US).format(campaign.target)
        val targetLine = String.format(t.targetTemplate, targetFormatted)

        val manualSection = if (isPrivate && campaign.joinCode.isNotBlank()) {
            val step3 = String.format(t.codeStep3Template, campaign.joinCode)
            """
━━━━━━━━━━━━━━━━━━━━
${t.manualPrivateTitle}
${t.privateCodeLabel} ${campaign.joinCode}

${t.codeStepsTitle}
${t.codeStep1}
${t.codeStep2}
$step3
""".trimIndent()
        } else {
            val step3 = String.format(t.normalStep3Template, campaign.title)
            """
━━━━━━━━━━━━━━━━━━━━
${t.normalStepsTitle}
${t.normalStep1}
${t.normalStep2}
$step3
""".trimIndent()
        }

        return """
${t.salam}

$inviteHeader

${t.zikrPrefix} ${campaign.title}$arabicText
$targetLine
${t.organizedByPrefix} $creatorName$noteSection

━━━━━━━━━━━━━━━━━━━━
${t.oneClickTitle}
👉 $link
${t.oneClickSubtitle}

$manualSection

${t.rewardText}
""".trimIndent()
    }

    /**
     * Blessed invite message for Joined Members.
     * Multilingual: Uses the user's chosen language.
     * Normal groups contain NO code at all; Private groups include link and manual steps.
     */
    fun buildMemberInviteMessage(
        campaign: CampaignItem,
        lang: String = PrayerRepository.getLanguage()
    ): String {
        val t = getTranslations(lang)
        val isPrivate = campaign.visibility == "private"
        val codeOrId = if (isPrivate) campaign.joinCode.ifBlank { campaign.campaignId } else campaign.campaignId
        val link = getDeepLinkUrl(codeOrId)

        val inviteHeader = if (isPrivate) t.memberInvitePrivate else t.memberInviteNormal
        val arabicText = if (campaign.arabic.isNotBlank()) "\n${campaign.arabic.trim()}\n" else ""
        val hasNote = campaign.note.isNotBlank() &&
                !campaign.note.trim().equals("null", ignoreCase = true) &&
                !campaign.note.trim().equals("None", ignoreCase = true)
        val noteSection = if (hasNote) "\n${t.purposePrefix} ${campaign.note.trim()}" else ""
        val creatorName = campaign.creatorName.ifBlank { t.defaultCreatorName }
        val targetFormatted = NumberFormat.getNumberInstance(Locale.US).format(campaign.target)
        val targetLine = String.format(t.targetTemplate, targetFormatted)

        val manualSection = if (isPrivate && campaign.joinCode.isNotBlank()) {
            val step3 = String.format(t.codeStep3Template, campaign.joinCode)
            """
━━━━━━━━━━━━━━━━━━━━
${t.manualPrivateTitle}
${t.privateCodeLabel} ${campaign.joinCode}

${t.codeStepsTitle}
${t.codeStep1}
${t.codeStep2}
$step3
""".trimIndent()
        } else {
            val step3 = String.format(t.normalStep3Template, campaign.title)
            """
━━━━━━━━━━━━━━━━━━━━
${t.normalStepsTitle}
${t.normalStep1}
${t.normalStep2}
$step3
""".trimIndent()
        }

        return """
${t.salam}

$inviteHeader

${t.zikrPrefix} ${campaign.title}$arabicText
$targetLine
${t.organizedByPrefix} $creatorName$noteSection

━━━━━━━━━━━━━━━━━━━━
${t.oneClickTitle}
👉 $link
${t.oneClickSubtitle}

$manualSection

${t.rewardText}
""".trimIndent()
    }

    // ── Platform-Specific Share Launchers ──

    fun shareToWhatsApp(context: Context, text: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            setPackage("com.whatsapp")
        }
        try {
            context.startActivity(intent)
        } catch (_: Exception) {
            shareGeneral(context, "Share Dhikr Group via WhatsApp", text)
        }
    }

    fun shareToSms(context: Context, text: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("sms:")
            putExtra("sms_body", text)
        }
        try {
            context.startActivity(intent)
        } catch (_: Exception) {
            shareGeneral(context, "Share Dhikr Group via SMS", text)
        }
    }

    fun shareToTwitter(context: Context, text: String) {
        val tweetUrl = "https://twitter.com/intent/tweet?text=" + Uri.encode(text)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl))
        try {
            context.startActivity(intent)
        } catch (_: Exception) {
            shareGeneral(context, "Share Dhikr Group on X", text)
        }
    }

    fun shareToFacebook(context: Context, text: String, url: String) {
        val fbUrl = "https://www.facebook.com/sharer/sharer.php?u=" + Uri.encode(url) + "&quote=" + Uri.encode(text)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(fbUrl))
        try {
            context.startActivity(intent)
        } catch (_: Exception) {
            shareGeneral(context, "Share Dhikr Group on Facebook", text)
        }
    }

    fun shareToInstagram(context: Context, text: String) {
        copyToClipboard(context, text, "Invite copied! Open Instagram to share.")
        val intent = context.packageManager.getLaunchIntentForPackage("com.instagram.android")
        if (intent != null) {
            try {
                context.startActivity(intent)
                return
            } catch (_: Exception) {}
        }
        shareGeneral(context, "Share Dhikr Group on Instagram", text)
    }

    fun shareCampaign(
        context: Context,
        campaign: CampaignItem,
        isCreator: Boolean,
        lang: String = PrayerRepository.getLanguage()
    ) {
        val text = if (isCreator) buildCreatorInviteMessage(campaign, lang) else buildMemberInviteMessage(campaign, lang)
        shareGeneral(context, "Join Live Dhikr: ${campaign.title}", text)
    }

    fun sharePlatform(
        context: Context,
        platform: String,
        campaign: CampaignItem,
        isCreator: Boolean,
        lang: String = PrayerRepository.getLanguage()
    ) {
        val text = if (isCreator) buildCreatorInviteMessage(campaign, lang) else buildMemberInviteMessage(campaign, lang)
        val isPrivate = campaign.visibility == "private"
        val codeOrId = if (isPrivate) campaign.joinCode.ifBlank { campaign.campaignId } else campaign.campaignId
        when (platform.lowercase(Locale.ROOT)) {
            "whatsapp" -> shareToWhatsApp(context, text)
            "sms" -> shareToSms(context, text)
            "twitter", "x" -> shareToTwitter(context, text)
            "facebook", "fb" -> shareToFacebook(context, text, getDeepLinkUrl(codeOrId))
            "instagram", "insta" -> shareToInstagram(context, text)
            else -> shareGeneral(context, "Live Dhikr: ${campaign.title}", text)
        }
    }

    fun shareGeneral(context: Context, title: String, text: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TEXT, text)
        }
        context.startActivity(Intent.createChooser(intent, title))
    }

    fun copyToClipboard(context: Context, text: String, toastMsg: String = "Copied to clipboard!") {
        val clip = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clip.setPrimaryClip(ClipData.newPlainText("Dhikr Invite", text))
        AppToast.show(toastMsg)
    }
}
