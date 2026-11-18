package com.tasbih.digitaltasbih.receivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.tasbih.digitaltasbih.MainActivity
import com.tasbih.digitaltasbih.R
import com.tasbih.digitaltasbih.data.PrayerRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * JummahReminderReceiver
 * Fires automatically every Friday:
 * 1. Morning at 11:00 AM (Slot 1: Preparation, Sunnahs, and Surah Al-Kahf)
 * 2. Afternoon at 3:00 PM (Slot 2: Sa'at al-Istijabah - Hour of Accepted Dua & Surah Kahf Completion)
 * 
 * Features:
 * - Rotates through authentic messages randomly across weeks so every Jummah feels fresh.
 * - Both slots on the same Jummah draw from distinct morning vs afternoon message pools.
 * - Full multi-language support (Hindi, Urdu, English, Arabic, Bengali, etc.).
 * - One-tap direct launch into Jummah checklist or Surah Al-Kahf.
 */
class JummahReminderReceiver : BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID = "dt_jummah_reminder_channel"
        const val CHANNEL_NAME = "Jummah Mubarak & Sunnahs"
        const val ACTION_JUMMAH_REMINDER_MORNING = "com.tasbih.digitaltasbih.ACTION_JUMMAH_REMINDER_MORNING"
        const val ACTION_JUMMAH_REMINDER_AFTERNOON = "com.tasbih.digitaltasbih.ACTION_JUMMAH_REMINDER_AFTERNOON"
        const val REQ_CODE_MORNING = 11001
        const val REQ_CODE_AFTERNOON = 15001
        const val NOTIF_ID_MORNING = 11001
        const val NOTIF_ID_AFTERNOON = 15001
        private const val TAG = "JummahReminderReceiver"
        private const val PREFS_NAME = "dt_jummah_notif_prefs"
    }

    data class JummahNotificationItem(
        val title: String,
        val body: String,
        val summary: String,
        val actionKahfLabel: String,
        val actionSunnahLabel: String
    )

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent == null) return

        PrayerRepository.init(context)

        // Reschedule for next Friday to maintain recurring schedule
        PrayerRepository.scheduleJummahReminders(context)

        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d(TAG, "Device rebooted: Rescheduled Jummah Friday reminders.")
            return
        }

        val isMorning = intent.action == ACTION_JUMMAH_REMINDER_MORNING
        val isAfternoon = intent.action == ACTION_JUMMAH_REMINDER_AFTERNOON

        if (!isMorning && !isAfternoon) return

        val todayKey = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val sentKey = if (isMorning) "sent_morning_$todayKey" else "sent_afternoon_$todayKey"

        // Prevent duplicate notification on the same Friday slot
        if (prefs.getBoolean(sentKey, false)) {
            Log.d(TAG, "Skipping: Jummah notification already sent today ($sentKey).")
            return
        }
        prefs.edit().putBoolean(sentKey, true).apply()

        // Randomize selection while rotating from last week
        val lastIdxKey = if (isMorning) "last_morning_idx" else "last_afternoon_idx"
        val lastIdx = prefs.getInt(lastIdxKey, -1)
        val available = (0..2).filter { it != lastIdx }
        val chosenIdx = if (available.isNotEmpty()) available.random() else (0..2).random()
        prefs.edit().putInt(lastIdxKey, chosenIdx).apply()

        val lang = PrayerRepository.getLanguage().lowercase().split("-")[0]
        val item = if (isMorning) {
            getMorningContent(chosenIdx, lang)
        } else {
            getAfternoonContent(chosenIdx, lang)
        }

        val notifId = if (isMorning) NOTIF_ID_MORNING else NOTIF_ID_AFTERNOON
        showNotification(context, notifId, item)
    }

    private fun showNotification(context: Context, notifId: Int, item: JummahNotificationItem) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager ?: return

        // 1. Create Channel on Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val audioAttributes = android.media.AudioAttributes.Builder()
                .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(android.media.AudioAttributes.USAGE_NOTIFICATION)
                .build()

            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Blessed Friday spiritual reminders for Surah Al-Kahf, Salawat, and Jummah Sunnahs."
                enableLights(true)
                lightColor = Color.parseColor("#10B981") // Islamic Emerald
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 400, 200, 400)
                setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), audioAttributes)
            }
            nm.createNotificationChannel(channel)
        }

        val flags = PendingIntent.FLAG_UPDATE_CURRENT or
                (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)

        // Main Tap -> Opens Jummah Checklist Screen
        val mainTapIntent = Intent(context, MainActivity::class.java).apply {
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("open_screen", "jummah")
        }
        val mainPendingIntent = PendingIntent.getActivity(context, notifId, mainTapIntent, flags)

        // Action Button 1 -> Direct Open Surah Al-Kahf in Quran Reader
        val kahfIntent = Intent(context, MainActivity::class.java).apply {
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("open_screen", "quran_kahf")
        }
        val kahfPendingIntent = PendingIntent.getActivity(context, notifId + 100, kahfIntent, flags)

        // Action Button 2 -> Open Jummah Sunnahs Checklist
        val sunnahIntent = Intent(context, MainActivity::class.java).apply {
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("open_screen", "jummah")
        }
        val sunnahPendingIntent = PendingIntent.getActivity(context, notifId + 200, sunnahIntent, flags)

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_prayer)
            .setColor(Color.parseColor("#06281E")) // Royal Emerald & Obsidian
            .setColorized(true)
            .setContentTitle(item.title)
            .setContentText(item.body)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .setBigContentTitle(item.title)
                    .bigText(item.body)
                    .setSummaryText(item.summary)
            )
            .setAutoCancel(true)
            .setSound(soundUri)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setContentIntent(mainPendingIntent)
            .addAction(
                R.drawable.ic_stat_prayer,
                item.actionKahfLabel,
                kahfPendingIntent
            )
            .addAction(
                R.drawable.ic_stat_check,
                item.actionSunnahLabel,
                sunnahPendingIntent
            )

        nm.notify(notifId, builder.build())
        Log.d(TAG, "Posted Jummah notification ID=$notifId title=${item.title}")
    }

    // ══════════════════════════════════════════════════════════════
    //  SLOT 1: MORNING 11:00 AM MESSAGE POOL (3 Diverse Variations)
    // ══════════════════════════════════════════════════════════════
    private fun getMorningContent(index: Int, lang: String): JummahNotificationItem {
        return when (lang) {
            "hi" -> when (index) {
                0 -> JummahNotificationItem(
                    title = "🕌 जुम्मा मुबारक • सूरह कहफ़ का नूर ✨",
                    body = "नबी ﷺ ने फरमाया: \"जो शख्स जुम्मा के दिन सूरह कहफ़ पढ़ता है, उसके लिए दो जुमों के दरमियान नूर चमकता रहता है।\" (सहीह अल-जामी 6470)",
                    summary = "📖 सूरह कहफ़ की तिलावत व दुरूद शरीफ़",
                    actionKahfLabel = "📖 सूरह कहफ़",
                    actionSunnahLabel = "📿 जुम्मा सुन्नतें"
                )
                1 -> JummahNotificationItem(
                    title = "🌸 मुबारक जुम्मा • सुन्नतें और दुरूद-ए-पाक 📿",
                    body = "आज जुम्मा की नमाज़ से पहले ग़ुस्ल करें, साफ़ कपड़े पहनें, ख़ुशबू लगाएं और मस्जिद जल्दी पहुंचें। सूरह कहफ़ पढ़ना न भूलें!",
                    summary = "🕌 जुम्मा की तैयारी • सुन्नतों पर अमल करें",
                    actionKahfLabel = "📖 सूरह कहफ़",
                    actionSunnahLabel = "📿 चेकलिस्ट देखें"
                )
                else -> JummahNotificationItem(
                    title = "🌙 सैय्यिदुल अय्याम • जुम्मा की अज़मत 🌟",
                    body = "\"सूरज निकलने वाले दिनों में सबसे बेहतरीन दिन जुम्मा का दिन है।\" (सहीह मुस्लिम 854)। आइए सूरह कहफ़ की तिलावत से दिन संवारें।",
                    summary = "🤲 मग़फ़िरत और बरकत का मुबारक दिन",
                    actionKahfLabel = "📖 सूरह कहफ़",
                    actionSunnahLabel = "🕌 जुम्मा आमाल"
                )
            }
            "ur" -> when (index) {
                0 -> JummahNotificationItem(
                    title = "🕌 جمعہ مبارک • سورۃ الکہف کا نور ✨",
                    body = "نبی ﷺ نے فرمایا: \"جو شخص جمعہ کے دن سورۃ الکہف پڑھتا ہے، اس کے لیے دو جمعوں کے درمیان نور روشن رہتا ہے۔\" (صحیح الجامع 6470)",
                    summary = "📖 سورۃ الکہف کی تلاوت اور درود شریف",
                    actionKahfLabel = "📖 سورۃ الکہف",
                    actionSunnahLabel = "📿 جمعہ کی سنتیں"
                )
                1 -> JummahNotificationItem(
                    title = "🌸 مبارک جمعہ • سنتیں اور درودِ پاک 📿",
                    body = "آج نمازِ جمعہ سے پہلے غسل کریں، صاف لباس پہنیں، خوشبو لگائیں اور مسجد جلدی تشریف لے جائیں۔ سورۃ الکہف پڑھنا نہ بھولیں!",
                    summary = "🕌 تیاریِ جمعہ • مسنون اعمال زندہ کریں",
                    actionKahfLabel = "📖 سورۃ الکہف",
                    actionSunnahLabel = "📿 چیک لسٹ دیکھیں"
                )
                else -> JummahNotificationItem(
                    title = "🌙 سید الایام • عظمتِ جمعۃ المبارک 🌟",
                    body = "\"تمام دنوں میں سب سے بہترین دن جس پر سورج طلوع ہوتا ہے، وہ جمعہ کا دن ہے۔\" (صحیح مسلم 854)۔ سورۃ الکہف کی تلاوت سے دن سنواریں۔",
                    summary = "🤲 بخشش، مغفرت اور برکتوں کا دن",
                    actionKahfLabel = "📖 سورۃ الکہف",
                    actionSunnahLabel = "🕌 اعمالِ جمعہ"
                )
            }
            "hinglish", "roman" -> when (index) {
                0 -> JummahNotificationItem(
                    title = "🕌 Jummah Mubarak • Surah Kahf Ka Noor ✨",
                    body = "Nabi ﷺ ne farmaya: \"Jo shakhs Jummah ke din Surah Kahf padhta hai, uske liye do Jummoun ke darmiyan noor chamakta rehta hai.\" (Sahih al-Jami' 6470)",
                    summary = "📖 Surah Kahf ki tilawat aur kasrat se Durood bhejein",
                    actionKahfLabel = "📖 Surah Kahf",
                    actionSunnahLabel = "📿 Jummah Sunnatein"
                )
                1 -> JummahNotificationItem(
                    title = "🌸 Mubarak Jummah • Sunnatein Aur Durood-e-Paak 📿",
                    body = "Aaj Jummah ki namaz se pehle Ghusl karein, saaf kapde pehnein, khushboo lagayein aur masjid jaldi pahuchein. Surah Kahf padhna na bhoolein!",
                    summary = "🕌 Jummah ki tayyari • Sunnaton par amal karein",
                    actionKahfLabel = "📖 Surah Kahf",
                    actionSunnahLabel = "📿 Checklist Dekhein"
                )
                else -> JummahNotificationItem(
                    title = "🌙 Sayyidul Ayyam • Jummah Ki Azmat 🌟",
                    body = "\"Suraj nikalne wale dinon me sabse behtareen din Jummah ka din hai.\" (Sahih Muslim 854). Aaiye Surah Kahf ki tilawat se din sanwarein.",
                    summary = "🤲 Maghfirat aur barkat ka mubarak din",
                    actionKahfLabel = "📖 Surah Kahf",
                    actionSunnahLabel = "🕌 Jummah Aamaal"
                )
            }
            "gu" -> when (index) {
                0 -> JummahNotificationItem(
                    title = "🕌 જુમ્મા મુબારક • સુરાહ અલ-કહફનું નૂર ✨",
                    body = "નબી ﷺ એ ફરમાવ્યું: \"જે વ્યક્તિ જુમ્માના દિવસે સુરાહ કહફ પઢે છે, તેના માટે બે જુમ્મા વચ્ચે નૂર ચમકતું રહે છે.\" (સહીહ અલ-જામી ૬૪૭૦)",
                    summary = "📖 સુરાહ કહફનું પઠન અને વધુમાં વધુ દુરુદ પઢો",
                    actionKahfLabel = "📖 સુરાહ કહફ",
                    actionSunnahLabel = "📿 જુમ્માની સુન્નતો"
                )
                1 -> JummahNotificationItem(
                    title = "🌸 મુબારક જુમ્મા • સુન્નતો અને દુરુદ-એ-પાક 📿",
                    body = "આજે જુમ્માની નમાઝ પહેલાં ગુસ્લ કરો, સ્વચ્છ કપડાં પહેરો, અત્તર લગાવો અને મસ્જિદે વહેલા પહોંચો. સુરાહ કહફ પઢવાનું ભૂલશો નહીં!",
                    summary = "🕌 જુમ્માની તૈયારી • સુન્નતો પર અમલ કરો",
                    actionKahfLabel = "📖 સુરાહ કહફ",
                    actionSunnahLabel = "📿 ચેકલિસ્ટ જુઓ"
                )
                else -> JummahNotificationItem(
                    title = "🌙 સૈયિદુલ અય્યામ • જુમ્માની મહાનતા 🌟",
                    body = "\"સૂર્ય ઊગવાના દિવસોમાં સૌથી શ્રેષ્ઠ દિવસ જુમ્માનો દિવસ છે.\" (સહીહ મુસ્લિમ ૮૫૪). સુરાહ કહફના પઠનથી દિવસને રોશન કરો.",
                    summary = "🤲 માફી અને બરકતનો મુબારક દિવસ",
                    actionKahfLabel = "📖 સુરાહ કહફ",
                    actionSunnahLabel = "🕌 જુમ્માના અમલો"
                )
            }
            "mr" -> when (index) {
                0 -> JummahNotificationItem(
                    title = "🕌 जुम्मा मुबारक • सूरह कहफचे नूर ✨",
                    body = "प्रेषित ﷺ म्हणाले: \"जो व्यक्ती जुम्माच्या दिवशी सूरह कहफ पठण करतो, त्याच्यासाठी दोन जुम्मांदरम्यान नूर चमकत राहतो.\" (सहीह अल-जामी ६४७०)",
                    summary = "📖 सूरह कहफचे पठण आणि भरपूर दुरूद पाठवा",
                    actionKahfLabel = "📖 सूरह कहफ",
                    actionSunnahLabel = "📿 जुम्माच्या सुन्नती"
                )
                1 -> JummahNotificationItem(
                    title = "🌸 मुबारक जुम्मा • सुन्नती आणि दुरूद-ए-पाक 📿",
                    body = "आज जुम्माच्या नमाजापूर्वी गुस्ल करा, स्वच्छ कपडे परिधान करा, अत्तर लावा आणि मशिदीत लवकर पोहोचा. सूरह कहफ वाचायला विसरू नका!",
                    summary = "🕌 जुम्माची तयारी • सुन्नतींचे पालन करा",
                    actionKahfLabel = "📖 सूरह कहफ",
                    actionSunnahLabel = "📿 चेकलिस्ट पहा"
                )
                else -> JummahNotificationItem(
                    title = "🌙 सय्यिदुल अय्याम • जुम्माचे महत्त्व 🌟",
                    body = "\"सूर्य उगवणाऱ्या सर्व दिवसांमध्ये सर्वात उत्तम दिवस जुम्माचा आहे.\" (सहीह मुस्लिम ८५४). सूरह कहफच्या पठणाने आपला दिवस उजळवा.",
                    summary = "🤲 क्षमा आणि आशीर्वादांचा पवित्र दिवस",
                    actionKahfLabel = "📖 सूरह कहफ",
                    actionSunnahLabel = "🕌 जुम्माची कृत्ये"
                )
            }
            "ta" -> when (index) {
                0 -> JummahNotificationItem(
                    title = "🕌 ஜும்ஆ முபாரக் • ஸூரா அல்-கஹ்ஃபின் ஒளி ✨",
                    body = "நபி ﷺ கூறினார்கள்: \"எவர் ஜும்ஆ நாளில் ஸூரா அல்-கஹ்ஃபை ஓதுகிறாரோ, அவருக்கு இரு ஜும்ஆக்களுக்கு இடைப்பட்ட காலம் வரை பேரொளி பிரகாசிக்கும்.\" (ஸஹீஹுல் ஜாமி' 6470)",
                    summary = "📖 ஸூரா கஹ்ஃப் ஓதுங்கள் மற்றும் ஸலவாத் கூறுங்கள்",
                    actionKahfLabel = "📖 ஸூரா கஹ்ஃப்",
                    actionSunnahLabel = "📿 ஜும்ஆ சுன்னத்துகள்"
                )
                1 -> JummahNotificationItem(
                    title = "🌸 பாக்கியமிக்க ஜும்ஆ • சுன்னத்துகள் & ஸலவாத் 📿",
                    body = "ஜும்ஆ தொழுகைக்கு முன் குளித்து, சுத்தமான ஆடைகளை அணிந்து, நறுமணம் பூசி பள்ளிவாசலுக்கு முன்கூட்டியே செல்லுங்கள். ஸூரா கஹ்ஃப் ஓத மறவாதீர்கள்!",
                    summary = "🕌 ஜும்ஆ தயாரிப்பு • சுன்னத்துகளை கடைபிடியுங்கள்",
                    actionKahfLabel = "📖 ஸூரா கஹ்ஃப்",
                    actionSunnahLabel = "📿 சரிபார்ப்பு பட்டியல்"
                )
                else -> JummahNotificationItem(
                    title = "🌙 சையிதுல் அய்யாம் • ஜும்ஆவின் சிறப்பு 🌟",
                    body = "\"சூரியன் உதிக்கும் நாட்களில் மிகவும் சிறந்த நாள் ஜும்ஆ நாளாகும்.\" (ஸஹீஹ் முஸ்லிம் 854). ஸூரா கஹ்ஃப் ஓதி உங்கள் நாளை பாக்கியமாக்குங்கள்.",
                    summary = "🤲 பாவமன்னிப்பு மற்றும் அருட்கொடைகளின் நாள்",
                    actionKahfLabel = "📖 ஸூரா கஹ்ஃப்",
                    actionSunnahLabel = "🕌 ஜும்ஆ அமல்கள்"
                )
            }
            "ar" -> when (index) {
                0 -> JummahNotificationItem(
                    title = "🕌 جمعة مباركة • نور سورة الكهف ✨",
                    body = "قال رسول الله ﷺ: «من قرأ سورةَ الكهفِ في يومِ الجمعةِ أضاء له من النورِ ما بين الجمعتين» (صحيح الجامع 6470)",
                    summary = "📖 نور ما بين الجمعتين والصلاة على النبي ﷺ",
                    actionKahfLabel = "📖 سورة الكهف",
                    actionSunnahLabel = "📿 سنن الجمعة"
                )
                1 -> JummahNotificationItem(
                    title = "🌸 سنن يوم الجمعة المبارك 📿",
                    body = "اغتسل، وتطيب، والبس أحسن الثياب، وبكر إلى المسجد، وأكثر من الصلاة على الحبيب المصطفى ﷺ ولا تنس قراءة سورة الكهف.",
                    summary = "🕌 إحياء سنن الجمعة والمبادرة للخير",
                    actionKahfLabel = "📖 سورة الكهف",
                    actionSunnahLabel = "📿 قائمة السنن"
                )
                else -> JummahNotificationItem(
                    title = "🌙 خير يوم طلعت عليه الشمس 🌟",
                    body = "«خير يوم طلعت عليه الشمس يوم الجمعة» (صحيح مسلم). اغتنم نفحات هذا اليوم المبارك بالذكر وتلاوة سورة الكهف.",
                    summary = "🤲 يوم المغفرة واستجابة الدعوات",
                    actionKahfLabel = "📖 سورة الكهف",
                    actionSunnahLabel = "🕌 فضل الجمعة"
                )
            }
            "bn" -> when (index) {
                0 -> JummahNotificationItem(
                    title = "🕌 জুম্মা মুবারক • সূরা আল-কাহাফের নূর ✨",
                    body = "রাসূলুল্লাহ ﷺ বলেছেন: \"যে ব্যক্তি জুমার দিনে সূরা কাহাফ পাঠ করবে, তার জন্য দুই জুমার মধ্যবর্তী সময় নূর দ্বারা আলোকিত থাকবে।\" (সহীহ আল-জামে ৬৪৭০)",
                    summary = "📖 সূরা কাহাফ তিলাওয়াত ও বেশি বেশি দুরুদ",
                    actionKahfLabel = "📖 সূরা কাহাফ",
                    actionSunnahLabel = "📿 জুমার সুন্নত"
                )
                1 -> JummahNotificationItem(
                    title = "🌸 বরকতময় জুমা • সুন্নত ও দুরুদ শরীফ 📿",
                    body = "আজ জুমার পূর্বে গোসল করুন, পরিষ্কার পোশাক পরিধান করুন, সুগন্ধি লাগান এবং আগেভাগে মসজিদে যান। সূরা কাহাফ পড়তে ভুলবেন না!",
                    summary = "🕌 জুমার প্রস্তুতি • সুন্নতসমূহ পালন করুন",
                    actionKahfLabel = "📖 সূরা কাহাফ",
                    actionSunnahLabel = "📿 চেকলিস্ট"
                )
                else -> JummahNotificationItem(
                    title = "🌙 সাইয়্যিদুল আইয়াম • জুমার মর্যাদা 🌟",
                    body = "\"সূর্য উদিত হওয়ার দিনগুলোর মধ্যে সবচেয়ে উত্তম দিন হলো জুমার দিন।\" (সহীহ মুসলিম ৮৫৪)। সূরা কাহাফ তিলাওয়াত করুন।",
                    summary = "🤲 ক্ষমা ও রহমত লাভের বরকতময় দিন",
                    actionKahfLabel = "📖 সূরা কাহাফ",
                    actionSunnahLabel = "🕌 জুমার আমল"
                )
            }
            "tr" -> when (index) {
                0 -> JummahNotificationItem(
                    title = "🕌 Hayırlı Cumalar • Kehf Suresi'nin Nuru ✨",
                    body = "Peygamberimiz ﷺ buyurdu: «Cuma günü Kehf suresini okuyan kimse için iki Cuma arası nurla aydınlanır.» (Sahihü'l-Cami 6470)",
                    summary = "📖 Kehf Suresi'ni okuyun ve bolca Salavat getirin",
                    actionKahfLabel = "📖 Kehf Suresi",
                    actionSunnahLabel = "📿 Cuma Sünnetleri"
                )
                1 -> JummahNotificationItem(
                    title = "🌸 Mübarek Cuma • Sünnetler ve Salavat-ı Şerife 📿",
                    body = "Cuma namazından önce boy abdesti alın, temiz elbiseler giyin, güzel koku sürünün ve camiye erken gidin. Kehf suresini okumayı unutmayın!",
                    summary = "🕌 Cuma hazırlığı • Sünnetleri ihya edin",
                    actionKahfLabel = "📖 Kehf Suresi",
                    actionSunnahLabel = "📿 Kontrol Listesi"
                )
                else -> JummahNotificationItem(
                    title = "🌙 Seyyidü'l-Eyyam • Cuma Gününün Fazileti 🌟",
                    body = "«Üzerine güneşin doğduğu en hayırlı gün Cuma günüdür.» (Müslim 854). Gününüzü Kehf Suresi okuyarak nurlandırın.",
                    summary = "🤲 Mağfiret ve duaların kabul olduğu gün",
                    actionKahfLabel = "📖 Kehf Suresi",
                    actionSunnahLabel = "🕌 Cuma Amelleri"
                )
            }
            "id", "ms" -> when (index) {
                0 -> JummahNotificationItem(
                    title = "🕌 Jum'at Mubarak • Cahaya Surah Al-Kahfi ✨",
                    body = "Nabi ﷺ bersabda: \"Barangsiapa membaca Surah Al-Kahfi pada hari Jum'at, maka akan memancar cahaya baginya di antara dua Jum'at.\" (Shahih al-Jami' 6470)",
                    summary = "📖 Baca Surah Al-Kahfi & perbanyak Salawat",
                    actionKahfLabel = "📖 Surah Al-Kahfi",
                    actionSunnahLabel = "📿 Sunnah Jum'at"
                )
                1 -> JummahNotificationItem(
                    title = "🌸 Jum'at Berkah • Sunnah & Salawat 📿",
                    body = "Sebelum shalat Jum'at, mandilah, kenakan pakaian bersih, gunakan wewangian, dan bersegeralah ke masjid. Jangan lupa membaca Surah Al-Kahfi!",
                    summary = "🕌 Persiapan Jum'at • Hidupkan amalan sunnah",
                    actionKahfLabel = "📖 Surah Al-Kahfi",
                    actionSunnahLabel = "📿 Checklist Sunnah"
                )
                else -> JummahNotificationItem(
                    title = "🌙 Sayyidul Ayyam • Keutamaan Hari Jum'at 🌟",
                    body = "\"Sebaik-baik hari yang matahari terbit padanya adalah hari Jum'at.\" (HR. Muslim 854). Terangi harimu dengan tilawah Surah Al-Kahfi.",
                    summary = "🤲 Hari pengampunan dan keberkahan",
                    actionKahfLabel = "📖 Surah Al-Kahfi",
                    actionSunnahLabel = "🕌 Amalan Jum'at"
                )
            }
            else -> when (index) {
                0 -> JummahNotificationItem(
                    title = "🕌 Jummah Mubarak • The Light of Surah Al-Kahf ✨",
                    body = "The Prophet ﷺ said: \"Whoever reads Surah Al-Kahf on Friday, a light will shine for him between the two Fridays.\" (Sahih al-Jami' 6470)",
                    summary = "📖 Recite Surah Al-Kahf & send Salawat",
                    actionKahfLabel = "📖 Read Al-Kahf",
                    actionSunnahLabel = "📿 Friday Sunnahs"
                )
                1 -> JummahNotificationItem(
                    title = "🌸 Blessed Friday • Sunnahs & Salawat 📿",
                    body = "Perform Ghusl, wear clean clothes, apply fragrance, and head early to the mosque. Don't forget to recite Surah Al-Kahf!",
                    summary = "🕌 Friday Sunnahs • Revive the blessed traditions",
                    actionKahfLabel = "📖 Read Al-Kahf",
                    actionSunnahLabel = "📿 View Checklist"
                )
                else -> JummahNotificationItem(
                    title = "🌙 Master of Days • The Virtues of Friday 🌟",
                    body = "\"The best day on which the sun rises is Friday.\" (Sahih Muslim 854). Purify your heart and illuminate your day with Surah Al-Kahf.",
                    summary = "🤲 A day of forgiveness and divine blessings",
                    actionKahfLabel = "📖 Read Al-Kahf",
                    actionSunnahLabel = "🕌 Friday Deeds"
                )
            }
        }
    }

    // ══════════════════════════════════════════════════════════════
    //  SLOT 2: AFTERNOON 3:00 PM MESSAGE POOL (3 Diverse Variations)
    // ══════════════════════════════════════════════════════════════
    private fun getAfternoonContent(index: Int, lang: String): JummahNotificationItem {
        return when (lang) {
            "hi" -> when (index) {
                0 -> JummahNotificationItem(
                    title = "🤲 जुम्मा की दुआ का ख़ास वक़्त • साअतुल इस्तिजाबा ⏳",
                    body = "रसूलुल्लाह ﷺ ने फरमाया: \"जुम्मा के दिन एक ऐसी घड़ी है जिसमें बंदा जो भी भलाई मांगता है, अल्लाह अता फरमाता है।\" (बुख़ारी 935)। मग़रिब से पहले ख़ास दुआएं मांगें!",
                    summary = "✨ इस मुबारक वक़्त में दुआएं रद्द नहीं होतीं",
                    actionKahfLabel = "📖 सूरह कहफ़ पढ़ें",
                    actionSunnahLabel = "🤲 दुआ व तौबा"
                )
                1 -> JummahNotificationItem(
                    title = "📖 क्या आपने सूरह कहफ़ पढ़ ली? 🌅",
                    body = "सूरज डूबने (मग़रिब) से पहले सूरह अल-कहफ़ ज़रूर मुकम्मल कर लें ताकि पूरे हफ़्ते आपके लिए नूर चमकता रहे। साथ ही नबी ﷺ पर कसरत से दुरूद भेजें।",
                    summary = "🕊️ सूरह कहफ़ • मग़रिब से पहले मुकम्मल करें",
                    actionKahfLabel = "📖 अभी कहफ़ पढ़ें",
                    actionSunnahLabel = "📿 दुरूद शरीफ़"
                )
                else -> JummahNotificationItem(
                    title = "📿 जुम्मा की शाम • दुरूद शरीफ़ और इस्तिग़फ़ार 🕌",
                    body = "\"जुम्मा के दिन मुझ पर कसरत से दुरूद भेजो, क्योंकि तुम्हारा दुरूद मुझ पर पेश किया जाता है।\" (अबू दाऊद)। इस मुबारक दिन के आख़िरी पलों में तौबा व इस्तिग़फ़ार करें।",
                    summary = "💚 अल्लाहुम्मा सल्लि अला मुहम्मद",
                    actionKahfLabel = "📖 सूरह कहफ़",
                    actionSunnahLabel = "📿 दुरूद काउंटर"
                )
            }
            "ur" -> when (index) {
                0 -> JummahNotificationItem(
                    title = "🤲 قبولیتِ دعا کی مبارک گھڑی • ساعتِ استجابت ⏳",
                    body = "رسول اللہ ﷺ نے فرمایا: \"جمعہ کے دن ایک ایسی گھڑی ہے جس میں بندہ جو خیر مانگے، اللہ تعالیٰ عطا فرماتا ہے۔\" (صحیح بخاری 935)۔ مغرب سے پہلے دعا میں لگ جائیں!",
                    summary = "✨ اس خاص ساعت میں دعائیں رد نہیں ہوتیں",
                    actionKahfLabel = "📖 سورۃ الکہف پڑھیں",
                    actionSunnahLabel = "🤲 دعائیں اور توبہ"
                )
                1 -> JummahNotificationItem(
                    title = "📖 کیا آپ نے سورۃ الکہف پڑھ لی؟ 🌅",
                    body = "سورج غروب (مغرب) ہونے سے پہلے سورۃ الکہف ضرور مکمل کر لیں تاکہ پورا ہفتہ آپ کے لیے نور قائم رہے۔ درود شریف کا ورد جاری رکھیں۔",
                    summary = "🕊️ سورۃ الکہف • مغرب سے پہلے تلاوت مکمل کریں",
                    actionKahfLabel = "📖 ابھی کہف پڑھیں",
                    actionSunnahLabel = "📿 درود شریف"
                )
                else -> JummahNotificationItem(
                    title = "📿 جمعہ کی شام • درود شریف اور استغفار 🕌",
                    body = "\"جمعہ کے دن مجھ پر کثرت سے درود بھیجا کرو، کیونکہ تمہارا درود مجھ پر پیش ہوتا ہے۔\" (ابوداؤد)۔ اس با برکت دن کے آخری لمحات میں توبہ و استغفار کریں۔",
                    summary = "💚 اللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ",
                    actionKahfLabel = "📖 سورۃ الکہف",
                    actionSunnahLabel = "📿 درود کاؤنٹر"
                )
            }
            "hinglish", "roman" -> when (index) {
                0 -> JummahNotificationItem(
                    title = "🤲 Jummah Ki Dua Ka Khas Waqt • Sa'at al-Istijabah ⏳",
                    body = "Rasoolullah ﷺ ne farmaya: \"Jummah ke din ek aisi ghadi hai jisme banda jo bhi bhalai mangta hai, Allah ata farmata hai.\" (Bukhari 935). Maghrib se pehle khas duayein mangein!",
                    summary = "✨ Is mubarak waqt me duayein radd nahi hoti",
                    actionKahfLabel = "📖 Surah Kahf Padhein",
                    actionSunnahLabel = "🤲 Dua aur Tauba"
                )
                1 -> JummahNotificationItem(
                    title = "📖 Kya Aapne Surah Kahf Padh Li? 🌅",
                    body = "Suraj doobne (Maghrib) se pehle Surah Al-Kahf zaroor mukammal kar lein taaki poore hafte aapke liye noor chamakta rahe. Saath hi Nabi ﷺ par kasrat se Durood bhejein.",
                    summary = "🕊️ Surah Kahf • Maghrib se pehle mukammal karein",
                    actionKahfLabel = "📖 Abhi Kahf Padhein",
                    actionSunnahLabel = "📿 Durood Sharif"
                )
                else -> JummahNotificationItem(
                    title = "📿 Jummah Ki Shaam • Durood Sharif Aur Istighfar 🕌",
                    body = "\"Jummah ke din mujh par kasrat se Durood bhejo, kyunki tumhara Durood mujh par pesh kiya jata hai.\" (Abu Dawud). Is mubarak din ke aakhiri palon me tauba o istighfar karein.",
                    summary = "💚 Allahumma Salli 'ala Muhammad",
                    actionKahfLabel = "📖 Surah Kahf",
                    actionSunnahLabel = "📿 Durood Counter"
                )
            }
            "gu" -> when (index) {
                0 -> JummahNotificationItem(
                    title = "🤲 જુમ્માની દુઆનો ખાસ સમય • સાઅતુલ ઇસ્તિજાબા ⏳",
                    body = "રસૂલુલ્લાહ ﷺ એ ફરમાવ્યું: \"જુમ્માના દિવસે એક એવી ઘડી છે જેમાં બંદો જે પણ ભલાઈ માંગે, અલ્લાહ અતા ફરમાવે છે.\" (બુખારી ૯૩૫). મગરીબ પહેલાં ખાસ દુઆઓ માંગો!",
                    summary = "✨ આ મુબારક ઘડીમાં દુઆઓ રદ્દ નથી થતી",
                    actionKahfLabel = "📖 સુરાહ કહફ પઢો",
                    actionSunnahLabel = "🤲 દુઆ અને તૌબા"
                )
                1 -> JummahNotificationItem(
                    title = "📖 શું તમે સુરાહ કહફ પઢી લીધી? 🌅",
                    body = "સૂરજ ડૂબવા (મગરીબ) પહેલાં સુરાહ અલ-કહફ જરૂર પૂર્ણ કરી લો જેથી આખું અઠવાડિયું તમારા માટે નૂર ચમકતું રહે. સાથે જ નબી ﷺ પર વધુમાં વધુ દુરુદ મોકલો.",
                    summary = "🕊️ સુરાહ કહફ • મગરીબ પહેલાં પૂર્ણ કરો",
                    actionKahfLabel = "📖 હમણાં કહફ પઢો",
                    actionSunnahLabel = "📿 દુરુદ શરીફ"
                )
                else -> JummahNotificationItem(
                    title = "📿 જુમ્માની સાંજ • દુરુદ શરીફ અને ઇસ્તિગફાર 🕌",
                    body = "\"જુમ્માના દિવસે મારા પર કસરતથી દુરુદ મોકલો, કારણ કે તમારું દુરુદ મારી સમક્ષ રજૂ કરવામાં આવે છે.\" (અબૂ દાઉદ). પવિત્ર દિવસની આખરી પળોમાં ઇસ્તિગફાર કરો.",
                    summary = "💚 અલ્લાહુમ્મા સલ્લિ અલા મુહમ્મદ",
                    actionKahfLabel = "📖 સુરાહ કહફ",
                    actionSunnahLabel = "📿 દુરુદ કાઉન્ટર"
                )
            }
            "mr" -> when (index) {
                0 -> JummahNotificationItem(
                    title = "🤲 जुम्माच्या दुवांची खास वेळ • साअतुल इस्तिजाबा ⏳",
                    body = "रसूलुल्लाह ﷺ म्हणाले: \"जुम्माच्या दिवशी एक अशी वेळ असते ज्यामध्ये बंदा जे काही भले मागतो, अल्लाह ते प्रदान करतो.\" (बुखारी ९३५). मगरीबपूर्वी दुवा मागा!",
                    summary = "✨ या शुभ वेळेत मागितलेली दुवा कधीही नाकारली जात नाही",
                    actionKahfLabel = "📖 सूरह कहफ वाचा",
                    actionSunnahLabel = "🤲 दुवा व तौबा"
                )
                1 -> JummahNotificationItem(
                    title = "📖 तुम्ही सूरह कहफ वाचली का? 🌅",
                    body = "सूर्य मावळण्यापूर्वी (मगरीबपूर्वी) सूरह अल-कहफ अवश्य पूर्ण करा जेणेकरून संपूर्ण आठवडा तुमच्यासाठी नूर राहील. प्रेषितांवर ﷺ भरपूर दुरूद पाठवा.",
                    summary = "🕊️ सूरह कहफ • मगरीबपूर्वी पूर्ण करा",
                    actionKahfLabel = "📖 आताच कहफ वाचा",
                    actionSunnahLabel = "📿 दुरूद शरीफ"
                )
                else -> JummahNotificationItem(
                    title = "📿 जुम्माची संध्याकाळ • दुरूद शरीफ आणि इस्तिगफार 🕌",
                    body = "\"जुम्माच्या दिवशी माझ्यावर भरपूर दुरूद पाठवा, কারণ तुमचे दुरूद माझ्यासमोर सादर केले जाते.\" (अबू दाऊद). या पवित्र दिवसाच्या शेवटच्या क्षणांमध्ये क्षमा मागा.",
                    summary = "💚 अल्लाहुम्मा सल्ली अला मुहम्मद",
                    actionKahfLabel = "📖 सूरह कहफ",
                    actionSunnahLabel = "📿 दुरूद काउंटर"
                )
            }
            "ta" -> when (index) {
                0 -> JummahNotificationItem(
                    title = "🤲 துஆ ஏற்றுக்கொள்ளப்படும் பொன்னான நேரம் ⏳",
                    body = "நபி ﷺ கூறினார்கள்: \"ஜும்ஆ நாளில் ஒரு நேரம் உண்டு; ஒரு முஸ்லிம் அநேரத்தில் அல்லாஹ்விடம் எதைக் கேட்டாலும் அதை அவனுக்கு அவன் கொடுக்காமல் இருப்பதில்லை.\" (புகாரி 935).",
                    summary = "✨ மஃக்ரிபுக்கு முன் துஆக்கள் நிராகரிக்கப்படுவதில்லை",
                    actionKahfLabel = "📖 ஸூரா கஹ்ஃப்",
                    actionSunnahLabel = "🤲 துஆ & தவ்பா"
                )
                1 -> JummahNotificationItem(
                    title = "📖 இன்று ஸூரா அல்-கஹ்ஃப் ஓதிவிட்டீர்களா? 🌅",
                    body = "சூரியன் மறைவதற்கு (மஃக்ரிபுக்கு) முன்பாகவே ஸூரா கஹ்ஃபை முழுமையாக ஓதிவிடுங்கள். அண்ணல் நபி ﷺ மீது அதிகமான ஸலவாத் கூறுங்கள்.",
                    summary = "🕊️ மஃக்ரிபுக்கு முன் கஹ்ஃபை முடியுங்கள்",
                    actionKahfLabel = "📖 இப்போது ஓதுங்கள்",
                    actionSunnahLabel = "📿 ஸலவாத் ஓதுங்கள்"
                )
                else -> JummahNotificationItem(
                    title = "📿 ஜும்ஆ மாலை வேளை • ஸலவாத் & இஸ்திஃபார் 🕌",
                    body = "\"ஜும்ஆ நாளில் என் மீது அதிகமான ஸலவாத் கூறுங்கள்; ஏனெனில் உங்கள் ஸலவாத் என்னிடம் எடுத்துக் காட்டப்படுகிறது.\" (அபூ தாவூத்).",
                    summary = "💚 அல்லாஹும்ம ஸல்லி அலா முஹம்மத்",
                    actionKahfLabel = "📖 ஸூரா கஹ்ஃப்",
                    actionSunnahLabel = "📿 ஸலவாத் கவுண்டர்"
                )
            }
            "ar" -> when (index) {
                0 -> JummahNotificationItem(
                    title = "🤲 ساعة الاستجابة • لا ترد فيها دعوة ⏳",
                    body = "قال ﷺ: «إن في الجمعة لساعة لا يوافقها عبد مسلم يسأل الله خيراً إلا أعطاه إياه» (البخاري). ألح في الدعاء قبل الغروب.",
                    summary = "✨ ساعة مباركة قبل مغرب الجمعة",
                    actionKahfLabel = "📖 سورة الكهف",
                    actionSunnahLabel = "🤲 دعاء واستغفار"
                )
                1 -> JummahNotificationItem(
                    title = "📖 هل قرأت سورة الكهف اليوم؟ 🌅",
                    body = "بادر بإتمام قراءة سورة الكهف قبل غروب شمس الجمعة لتنال نورها المبارك، وأكثر من الصلاة والتسليم على النبي ﷺ.",
                    summary = "🕊️ بادر قبل غروب الشمس",
                    actionKahfLabel = "📖 اقرأ الكهف الآن",
                    actionSunnahLabel = "📿 الصلاة على النبي"
                )
                else -> JummahNotificationItem(
                    title = "📿 أصيل الجمعة • الصلاة على الحبيب والاستغفار 🕌",
                    body = "«أكثروا من الصلاة عليّ يوم الجمعة فإن صلاتكم معروضة عليّ» (أبو داود). اختم يومك المبارك بالتوبة وطلب الرضا.",
                    summary = "💚 اللهم صلِّ وسلم على نبينا محمد",
                    actionKahfLabel = "📖 سورة الكهف",
                    actionSunnahLabel = "📿 عداد الصلوات"
                )
            }
            "bn" -> when (index) {
                0 -> JummahNotificationItem(
                    title = "🤲 দুআ কবুলের বিশেষ মুহূর্ত • সা'আতুল ইস্তিজাবাহ ⏳",
                    body = "রাসূলুল্লাহ ﷺ বলেছেন: \"জুমার দিনে এমন একটি মুহূর্ত রয়েছে, যে সময়ে বান্দা যা কিছু কল্যাণ চায়, আল্লাহ তাকে তা দান করেন।\" (বুখারী ৯৩৫)। মাগরিবের পূর্বে দুআ করুন!",
                    summary = "✨ এই বরকতময় মুহূর্তে দুআ ফিরিয়ে দেওয়া হয় না",
                    actionKahfLabel = "📖 সূরা কাহাফ পড়ুন",
                    actionSunnahLabel = "🤲 দুআ ও তাওবাহ"
                )
                1 -> JummahNotificationItem(
                    title = "📖 আপনি কি আজ সূরা কাহাফ পড়েছেন? 🌅",
                    body = "মাগরিবের (সূর্যাস্তের) আগেই সূরা কাহাফ পাঠ সম্পন্ন করুন, যেন পুরো সপ্তাহ আপনার জন্য নূর উদ্ভাসিত থাকে। সাথে দুরুদ বেশি বেশি পাঠ করুন।",
                    summary = "🕊️ সূর্যাস্তের আগেই সূরা কাহাফ সম্পন্ন করুন",
                    actionKahfLabel = "📖 এখনই কাহাফ পড়ুন",
                    actionSunnahLabel = "📿 দুরুদ শরীফ"
                )
                else -> JummahNotificationItem(
                    title = "📿 জুমার বিকাল • দুরুদ শরীফ ও ইস্তিগফার 🕌",
                    body = "\"জুমার দিনে আমার ওপর বেশি বেশি দুরুদ পাঠ করো, কেননা তা আমার কাছে পেশ করা হয়।\" (আবু দাউদ)। এই বরকতময় দিনের শেষ প্রহরে ক্ষমা প্রার্থনা করুন।",
                    summary = "💚 আল্লাহুম্মা সাল্লি আলা মুহাম্মাদ",
                    actionKahfLabel = "📖 সূরা কাহাফ",
                    actionSunnahLabel = "📿 দুরুদ কাউন্টার"
                )
            }
            "tr" -> when (index) {
                0 -> JummahNotificationItem(
                    title = "🤲 Cuma Günü Duaların Kabul Saati • İcabet Vakti ⏳",
                    body = "Resûlullah ﷺ buyurdu: «Cuma gününde öyle bir vakit vardır ki, Müslüman bir kul o vakitte Allah'tan bir hayır dilerse Allah ona mutlaka verir.» (Buhari 935). Akşamdan önce dua edin!",
                    summary = "✨ Bu mübarek saatte yapılan dualar geri çevrilmez",
                    actionKahfLabel = "📖 Kehf Suresi",
                    actionSunnahLabel = "🤲 Dua ve İstiğfar"
                )
                1 -> JummahNotificationItem(
                    title = "📖 Kehf Suresini Okudunuz mu? 🌅",
                    body = "Güneş batmadan (akşam namazından önce) Kehf Suresi'ni tamamlayarak tüm haftanızı aydınlatın. Efendimiz'e ﷺ bolca salavat gönderin.",
                    summary = "🕊️ Akşamdan önce Kehf Suresi'ni tamamlayın",
                    actionKahfLabel = "📖 Hemen Kehf Oku",
                    actionSunnahLabel = "📿 Salavat Getir"
                )
                else -> JummahNotificationItem(
                    title = "📿 Cuma Akşamı • Salavat ve İstiğfar 🕌",
                    body = "«Cuma günü bana çokça salavat getirin, çünkü salavatınız bana arz olunur.» (Ebu Davud). Bu mübarek günün son anlarında tövbe ve istiğfar edin.",
                    summary = "💚 Allahümme Salli Alâ Muhammed",
                    actionKahfLabel = "📖 Kehf Suresi",
                    actionSunnahLabel = "📿 Salavat Sayacı"
                )
            }
            "id", "ms" -> when (index) {
                0 -> JummahNotificationItem(
                    title = "🤲 Waktu Mustajab Doa Hari Jum'at ⏳",
                    body = "Rasulullah ﷺ bersabda: \"Pada hari Jum'at ada satu waktu, tidaklah seorang muslim berdoa memohon kebaikan kepada Allah bertepatan dengan waktu itu melainkan Allah mengabulkannya.\" (HR. Bukhari 935).",
                    summary = "✨ Waktu mustajab sebelum terbenam matahari",
                    actionKahfLabel = "📖 Surah Al-Kahfi",
                    actionSunnahLabel = "🤲 Doa & Istighfar"
                )
                1 -> JummahNotificationItem(
                    title = "📖 Sudahkah Anda Membaca Surah Al-Kahfi? 🌅",
                    body = "Selesaikan bacaan Surah Al-Kahfi sebelum Maghrib agar cahaya berkah menerangi Anda sepekan penuh. Perbanyak shalawat atas Nabi ﷺ.",
                    summary = "🕊️ Selesaikan Al-Kahfi sebelum Maghrib",
                    actionKahfLabel = "📖 Baca Al-Kahfi Sekarang",
                    actionSunnahLabel = "📿 Shalawat Nabi"
                )
                else -> JummahNotificationItem(
                    title = "📿 Sore Jum'at • Shalawat & Istighfar 🕌",
                    body = "\"Perbanyaklah bershalawat kepadaku pada hari Jum'at, karena shalawat kalian disampaikan kepadaku.\" (HR. Abu Dawud). Raih berkah di penghujung hari mulia ini.",
                    summary = "💚 Allahumma Sholli 'ala Muhammad",
                    actionKahfLabel = "📖 Surah Al-Kahfi",
                    actionSunnahLabel = "📿 Counter Shalawat"
                )
            }
            else -> when (index) {
                0 -> JummahNotificationItem(
                    title = "🤲 The Hour of Acceptance • Sa'at al-Istijabah ⏳",
                    body = "The Messenger ﷺ said: \"On Friday there is an hour when no Muslim asks Allah for good except that He gives it to him.\" (Bukhari 935). Make heartfelt du'as before Maghrib!",
                    summary = "✨ Supplications are never rejected during this hour",
                    actionKahfLabel = "📖 Read Al-Kahf",
                    actionSunnahLabel = "🤲 Du'a & Prayers"
                )
                1 -> JummahNotificationItem(
                    title = "📖 Have you recited Surah Al-Kahf today? 🌅",
                    body = "Complete Surah Al-Kahf before sunset (Maghrib) to keep your light shining until next Friday. Keep sending blessings upon our Prophet ﷺ!",
                    summary = "🕊️ Surah Al-Kahf • Complete before sunset",
                    actionKahfLabel = "📖 Read Kahf Now",
                    actionSunnahLabel = "📿 Send Salawat"
                )
                else -> JummahNotificationItem(
                    title = "📿 Friday Evening • Salawat & Istighfar 🕌",
                    body = "\"Send abundant blessings upon me on Friday, for your blessings are presented to me.\" (Abu Dawud). Seek forgiveness in the final moments of this blessed day.",
                    summary = "💚 Allahumma Salli 'ala Muhammad",
                    actionKahfLabel = "📖 Read Al-Kahf",
                    actionSunnahLabel = "📿 Salawat Counter"
                )
            }
        }
    }
}
