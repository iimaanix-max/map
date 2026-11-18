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
import com.tasbih.digitaltasbih.data.DhikrRepository
import com.tasbih.digitaltasbih.data.PrayerRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * DhikrReminderReceiver
 * Fires at 9:00 PM daily if user has NOT done any dhikr today.
 * Shows a heartfelt Quran 13:28 message in the user's selected language.
 * Automatically skips if user already recited any dhikr today.
 */
class DhikrReminderReceiver : BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID = "dt_dhikr_reminder_channel"
        const val CHANNEL_NAME = "Dhikr & Zikr Reminder"
        const val ACTION_DHIKR_REMINDER = "com.tasbih.digitaltasbih.ACTION_DHIKR_REMINDER"
        const val NOTIF_ID = 9001
        private const val TAG = "DhikrReminderReceiver"
    }

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent == null) return

        PrayerRepository.init(context)
        DhikrRepository.init(context)

        // Reschedule for next day 9:00 PM to maintain recurring schedule
        PrayerRepository.scheduleDhikrDailyReminder(context)

        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d(TAG, "Device rebooted: Rescheduled daily 9 PM dhikr reminder.")
            return
        }

        if (intent.action == ACTION_DHIKR_REMINDER) {
            val todayCal = Calendar.getInstance()
            val totalToday = try {
                DhikrRepository.getDayTotal(todayCal)
            } catch (e: Exception) {
                0
            }

            // Smart Skip 1: If user already recited dhikr today, don't disturb them
            if (totalToday > 0) {
                Log.d(TAG, "Skipping dhikr reminder: User already recited $totalToday dhikr today.")
                return
            }

            // Smart Skip 2: If notification was already sent today, don't duplicate
            val todayKey = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
            val prefs = context.getSharedPreferences("dt_dhikr_prefs", Context.MODE_PRIVATE)
            if (prefs.getBoolean("dhikr_notif_sent_$todayKey", false)) {
                Log.d(TAG, "Skipping dhikr reminder: Notification already sent today ($todayKey).")
                return
            }
            prefs.edit().putBoolean("dhikr_notif_sent_$todayKey", true).apply()

            showNotification(context)
        }
    }

    private fun showNotification(context: Context) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager ?: return

        // Create Channel on Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Daily evening reminder to remember Allah through Dhikr and Tasbih."
                enableLights(true)
                lightColor = Color.WHITE
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 400)
            }
            nm.createNotificationChannel(channel)
        }

        // Tap opens MainActivity directly into Tasbih screen
        val tapIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("open_screen", "tasbih")
        }
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or
                (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
        val pendingIntent = PendingIntent.getActivity(context, NOTIF_ID, tapIntent, flags)

        val lang = PrayerRepository.getLanguage().lowercase().split("-")[0]
        val (title, body) = when (lang) {
            "hi" -> Pair(
                "📿 आज आपने कोई ज़िक्र नहीं किया",
                "\"बेशक, अल्लाह के ज़िक्र से ही दिलों को सुकून मिलता है।\" (क़ुरआन 13:28)\nसुब्हानअल्लाह के कुछ पल आपके पूरे दिन को सुकून अता कर सकते हैं।"
            )
            "ur" -> Pair(
                "📿 آج آپ نے کوئی ذکر نہیں کیا",
                "\"بے شک، اللہ کے ذکر سے ہی دلوں کو سکون ملتا ہے۔\" (قرآن 13:28)\nسبحان اللہ کے چند لمحے آپ کے پورے دن کو پرسکون بنا سکتے ہیں۔"
            )
            "hinglish", "roman" -> Pair(
                "📿 Aaj Aapne Koi Dhikr Nahi Kiya",
                "\"Beshak, Allah ke zikr se hi dilon ko sukoon milta hai.\" (Quran 13:28)\nSubhanAllah ke chand lamhe aapke poore din ko pur-sukoon bana sakte hain."
            )
            "ar" -> Pair(
                "📿 لم تذكر الله اليوم",
                "\"أَلاَ بِذِكْرِ اللّهِ تَطْمَئِنُّ الْقُلُوبُ\" (الرعد: 28)\nلحظات قليلة في تسبيح الله تجلب الطمأنينة لقلبك طوال اليوم."
            )
            "bn" -> Pair(
                "📿 আজ আপনি কোনো যিকির করেননি",
                "\"জেনে রেখো, আল্লাহর স্মরণেই অন্তরসমূহ প্রশান্ত হয়।\" (কুরআন ১৩:২৮)\nকয়েক মুহূর্ত সুবহানাল্লাহ পাঠ আপনার সারাদিনে প্রশান্তি বয়ে আনতে পারে।"
            )
            "gu" -> Pair(
                "📿 આજે તમે કોઈ ઝિક્ર નથી કર્યું",
                "\"ખરેખર, અલ્લાહના સ્મરણ (ઝિક્ર) થી જ હૃદયને શાંતિ મળે છે.\" (કુરાન ૧૩:૨૮)\nસુભાનઅલ્લાહની થોડી પળો તમારા આખા દિવસને શાંતિમય બનાવી શકે છે."
            )
            "mr" -> Pair(
                "📿 आज तुम्ही कोणतेही स्मरण (झिक्र) केले नाही",
                "\"नक्कीच, अल्लाहच्या स्मरणानेच मनाला खरी शांती मिळते.\" (कुराण १३:२८)\nसुभानअल्लाहचे काही क्षण तुमच्या संपूर्ण दिवसाला शांतता देऊ शकतात."
            )
            "ta" -> Pair(
                "📿 இன்று நீங்கள் எந்த திக்ரும் செய்யவில்லை",
                "\"நிச்சயமாக, அல்லாஹ்வின் நினைவால் மட்டுமே உள்ளங்கள் அமைதி பெறுகின்றன.\" (குர்ஆன் 13:28)\nசுப்ஹானல்லாஹ் என ஓதும் சில நொடிகள் உங்கள் முழு நாளுக்கும் நிம்மதியை தரும்."
            )
            "pa" -> Pair(
                "📿 ਅੱਜ ਤੁਸੀਂ ਕੋਈ ਜ਼ਿਕਰ ਨਹੀਂ ਕੀਤਾ",
                "\"ਬੇਸ਼ੱਕ, ਅੱਲ੍ਹਾ ਦੇ ਜ਼ਿਕਰ ਨਾਲ ਹੀ ਦਿਲਾਂ ਨੂੰ ਸਕੂਨ ਮਿਲਦਾ ਹੈ।\" (ਕੁਰਆਨ 13:28)\nਸੁਬਹਾਨਅੱਲ੍ਹਾ ਦੇ ਕੁਝ ਪਲ ਤੁਹਾਡੇ ਪੂਰੇ ਦਿਨ ਨੂੰ ਪੁਰਸਕੂਨ ਬਣਾ ਸਕਦੇ ਹਨ।"
            )
            "tr" -> Pair(
                "📿 Bugün Hiç Zikir Çekmediniz",
                "\"Bilesiniz ki, kalpler ancak Allah'ı anmakla huzur bulur.\" (Râd, 13:28)\nSübhanallah diyerek geçireceğiniz birkaç an gününüze huzur katabilir."
            )
            "id", "ms" -> Pair(
                "📿 Anda Belum Berzikir Hari Ini",
                "\"Ingatlah, hanya dengan mengingat Allah hati menjadi tenteram.\" (Ar-Ra'd: 28)\nBeberapa saat membaca Subhanallah dapat membawa kedamaian sepanjang hari."
            )
            else -> Pair(
                "📿 You Haven't Done Any Dhikr Today",
                "\"Verily, in the remembrance of Allah do hearts find rest.\" (Quran 13:28)\nA few moments of SubhanAllah can bring peace to your whole day."
            )
        }

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_prayer)
            .setColor(Color.parseColor("#1A1A1A"))
            .setColorized(true)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .setBigContentTitle(title)
                    .bigText(body)
                    .setSummaryText("📿 Daily Dhikr Reminder")
            )
            .setAutoCancel(true)
            .setSound(soundUri)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        nm.notify(NOTIF_ID, builder.build())
    }
}
