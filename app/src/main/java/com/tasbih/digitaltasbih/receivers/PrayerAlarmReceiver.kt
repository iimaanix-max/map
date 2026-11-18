package com.tasbih.digitaltasbih.receivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.tasbih.digitaltasbih.MainActivity
import com.tasbih.digitaltasbih.R
import com.tasbih.digitaltasbih.data.PrayerRepository
import com.tasbih.digitaltasbih.data.PrayerSoundManager
import kotlin.math.cos
import kotlin.math.sin

class PrayerAlarmReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_PRAYER_ALARM = "com.tasbih.digitaltasbih.ACTION_PRAYER_ALARM"
        const val ACTION_MARK_PRAYED = "com.tasbih.digitaltasbih.ACTION_MARK_PRAYED"
        const val CHANNEL_ID = "digital_tasbih_prayer_channel"
        const val CHANNEL_NAME = "Salah & Athan Alerts"
        private const val TAG = "PrayerAlarmReceiver"
    }

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent == null) return

        PrayerRepository.init(context)

        // Handle Quick Action: Mark as Prayed directly from notification button
        if (intent.action == ACTION_MARK_PRAYED) {
            val prayerName = intent.getStringExtra("prayer_name") ?: ""
            val dateKey = intent.getStringExtra("date_key") ?: ""
            val notifId = intent.getIntExtra("notif_id", -1)
            if (prayerName.isNotEmpty() && dateKey.isNotEmpty()) {
                PrayerRepository.setPrayerDone(prayerName, dateKey, true)
                Log.d(TAG, "Marked $prayerName as prayed for $dateKey from notification action.")
            }
            if (notifId != -1) {
                val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
                nm?.cancel(notifId)
            }
            return
        }

        // If device rebooted, reschedule all exact alarms
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            PrayerRepository.scheduleAllPrayerNotifications(context)
            return
        }

        if (intent.action == ACTION_PRAYER_ALARM) {
            val id = intent.getIntExtra("id", (System.currentTimeMillis() % 100000).toInt())
            var title = intent.getStringExtra("title")
            var body = intent.getStringExtra("body")
            val isWarning = intent.getBooleanExtra("is_warning", false)
            var prayerName = intent.getStringExtra("prayer_name") ?: ""
            val dateKey = intent.getStringExtra("date_key") ?: ""

            if (prayerName.isEmpty()) {
                prayerName = extractPrayerName(title, body)
            }

            if (title.isNullOrEmpty() || body.isNullOrEmpty()) {
                val curLang = PrayerRepository.getLanguage()
                if (isWarning) {
                    val (wTitle, wBody) = PrayerRepository.getEndingWarningQuote(prayerName, curLang)
                    if (title.isNullOrEmpty()) title = wTitle
                    if (body.isNullOrEmpty()) body = wBody
                } else {
                    val (qTitle, qBody) = PrayerRepository.getSpiritualQuote(prayerName, curLang, java.util.Calendar.getInstance())
                    if (title.isNullOrEmpty()) title = qTitle
                    if (body.isNullOrEmpty()) body = qBody
                }
            }

            // Smart Skip: If 20-min warning and user already marked prayer as done, skip! (ignore for test)
            if (isWarning && prayerName.isNotEmpty() && dateKey.isNotEmpty() && dateKey != "test") {
                if (PrayerRepository.isPrayerDone(prayerName, dateKey)) {
                    Log.d(TAG, "Skipping 20-min warning for $prayerName because already prayed.")
                    return
                }
            }

            showNotification(context, id, title, body, isWarning, prayerName, dateKey)

            // Reschedule upcoming alarms to keep the chain alive
            PrayerRepository.scheduleAllPrayerNotifications(context)
        }
    }

    private fun showNotification(
        context: Context,
        id: Int,
        title: String,
        body: String,
        isWarning: Boolean,
        prayerName: String,
        dateKey: String = ""
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager ?: return

        // Sound Selection with dynamic user choice (Adhan, Ting, Chime, Silent)
        val soundUri: Uri? = try {
            PrayerSoundManager.getSoundUri(context, isWarning)
        } catch (e: Exception) {
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }
        val targetChannelId = PrayerSoundManager.getChannelId(context, isWarning)

        // Create Channel on Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Clean up obsolete v1 channels so device uses fresh channel with immediate zero-delay sound
            try {
                notificationManager.deleteNotificationChannel("dt_salah_channel_takbeer_nafees_v1")
                notificationManager.deleteNotificationChannel("dt_salah_channel_hijaz_chime_v1")
                notificationManager.deleteNotificationChannel("dt_salah_channel_ting")
                notificationManager.deleteNotificationChannel("digital_tasbih_prayer_channel")
                notificationManager.deleteNotificationChannel("digital_tasbih_prayer_warning_channel")
            } catch (_: Exception) {}

            val audioAttributes = android.media.AudioAttributes.Builder()
                .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(android.media.AudioAttributes.USAGE_NOTIFICATION)
                .build()

            val channel = NotificationChannel(
                targetChannelId,
                if (isWarning) "Salah Warning Alerts" else CHANNEL_NAME,
                if (soundUri == null) NotificationManager.IMPORTANCE_DEFAULT else NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Daily spiritual alerts for Fajr, Dhuhr, Asr, Maghrib, Isha, and Tahajjud prayers."
                enableLights(true)
                lightColor = Color.parseColor("#10B981")
                enableVibration(true)
                vibrationPattern = if (isWarning) {
                    longArrayOf(0, 300, 150, 300, 150, 300)
                } else {
                    longArrayOf(0, 500, 200, 500)
                }
                if (soundUri != null) {
                    setSound(soundUri, audioAttributes)
                } else {
                    setSound(null, null)
                }
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Tap opens MainActivity directly into Prayer Times
        val openIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("open_screen", "prayer")
        }

        val pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT or
                (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
        val pendingIntent = PendingIntent.getActivity(context, id, openIntent, pendingFlags)

        // Color & Subtitle Theme matching original app
        val isMakruh = isMakruhPrayer(prayerName)
        val accentColor: String
        val summary: String

        if (isWarning || isMakruh) {
            accentColor = "#DC2626" // Bold Red for ending warning or Makrooh
            summary = if (isMakruh) "🚫 मकरूह वक़्त • Makrooh Time Alert" else "⚠️ वक़्त ख़त्म होने वाला है • 20 Mins Remaining"
        } else if (prayerName.equals("tahajjud", ignoreCase = true)) {
            accentColor = "#6D28D9" // Midnight Violet
            summary = "🌙 तहज्जुद • शब-ए-बेदारी • Night Prayer"
        } else {
            accentColor = "#111827" // Luxury Rich Obsidian Black
            summary = "🕌 नमाज़ का वक़्त • Salah Alert"
        }

        val largeIcon = createPrayerNotificationLargeIcon(context, prayerName, isWarning)

        val builder = NotificationCompat.Builder(context, targetChannelId)
            .setSmallIcon(R.drawable.ic_stat_prayer)
            .setColor(Color.parseColor(accentColor))
            .setColorized(true)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .setBigContentTitle(title)
                    .bigText(body)
                    .setSummaryText(summary)
            )
            .setAutoCancel(true)
            .setVibrate(if (isWarning) longArrayOf(0, 300, 150, 300, 150, 300) else longArrayOf(0, 500, 200, 500))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setContentIntent(pendingIntent)

        if (largeIcon != null) {
            builder.setLargeIcon(largeIcon)
        }

        if (soundUri != null) {
            builder.setSound(soundUri)
        } else {
            builder.setSilent(true)
        }

        try {
            notificationManager.notify(id, builder.build())
        } catch (e: Exception) {
            Log.w(TAG, "Failed to post notification: ${e.message}")
        }
    }

    private fun isMakruhPrayer(prayerName: String?): Boolean {
        if (prayerName == null) return false
        val p = prayerName.lowercase()
        return p.contains("zawal") || p.contains("tulu") || p.contains("ghurub") || p.contains("makruh") || p.contains("मकरूह")
    }

    private fun extractPrayerName(title: String?, body: String?): String {
        val combined = ((title ?: "") + " " + (body ?: "")).lowercase()
        if (combined.contains("fajr") || combined.contains("फ़ज्र") || combined.contains("فجر")) return "Fajr"
        if (combined.contains("dhuhr") || combined.contains("ज़ुहर") || combined.contains("ظهر")) return "Dhuhr"
        if (combined.contains("asr") || combined.contains("अस्र") || combined.contains("عصر")) return "Asr"
        if (combined.contains("maghrib") || combined.contains("मग़रिब") || combined.contains("مغرب")) return "Maghrib"
        if (combined.contains("isha") || combined.contains("इशा") || combined.contains("عشاء")) return "Isha"
        if (combined.contains("tahajjud") || combined.contains("तहज्जुद") || combined.contains("تهجد")) return "Tahajjud"
        if (combined.contains("zawal") || combined.contains("ज़वाल")) return "Zawal"
        if (combined.contains("tulu") || combined.contains("तुलू")) return "Tulu"
        if (combined.contains("ghurub") || combined.contains("ग़ुरूब")) return "Ghurub"
        return "Salah"
    }

    // ── 🎨 Luxury Canvas Medallion Generator for Notification Drawer (Exact Copy from Original) ──
    private fun createPrayerNotificationLargeIcon(
        context: Context,
        prayerName: String,
        isWarning: Boolean
    ): Bitmap? {
        return try {
            val size = 192
            val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            val cx = size / 2f
            val cy = size / 2f
            val r = size / 2f - 4f

            // Background Circle with subtle luxury gradient
            val topColor: Int
            val botColor: Int
            if (isWarning) {
                topColor = Color.parseColor("#991B1B") // Deep Crimson
                botColor = Color.parseColor("#450A0A") // Dark Maroon
            } else if (prayerName.equals("tahajjud", ignoreCase = true)) {
                topColor = Color.parseColor("#2E1065") // Midnight Indigo
                botColor = Color.parseColor("#0B0F19") // Deep Obsidian
            } else {
                // Luxury Midnight Black / Deep Emerald
                topColor = Color.parseColor("#062A1E") // Deep Midnight Emerald
                botColor = Color.parseColor("#0B0F19") // Rich Obsidian
            }
            bgPaint.shader = LinearGradient(0f, 0f, 0f, size.toFloat(), topColor, botColor, Shader.TileMode.CLAMP)
            canvas.drawCircle(cx, cy, r, bgPaint)

            // 24K Gold Outer Ring
            val ringPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.STROKE
                strokeWidth = 5f
                color = Color.parseColor("#F59E0B") // 24K Gold
            }
            canvas.drawCircle(cx, cy, r, ringPaint)

            // Inner subtle gold guideline ring
            ringPaint.strokeWidth = 1.5f
            ringPaint.color = Color.parseColor("#B45309")
            canvas.drawCircle(cx, cy, r - 6f, ringPaint)

            // Center Islamic artwork in 24K Gold
            val goldPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.parseColor("#FDE68A") // Bright Gold fill
                style = Paint.Style.FILL
            }

            if (prayerName.equals("tahajjud", ignoreCase = true)) {
                // Crescent Moon & Stars for Tahajjud
                val moonPath = Path().apply {
                    addCircle(cx - 8f, cy, 38f, Path.Direction.CW)
                }
                val cutPath = Path().apply {
                    addCircle(cx + 6f, cy - 8f, 34f, Path.Direction.CW)
                }
                moonPath.op(cutPath, Path.Op.DIFFERENCE)
                canvas.drawPath(moonPath, goldPaint)

                // Small 4-point stars
                drawStar(canvas, goldPaint, cx + 32f, cy - 22f, 10f, 4)
                drawStar(canvas, goldPaint, cx + 24f, cy + 24f, 7f, 4)
            } else if (isWarning) {
                // Alert Dome with Warning Shield
                drawMosqueSilhouette(canvas, goldPaint, cx, cy + 16f, size * 0.65f)

                // Warning badge at top
                val alertPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = Color.parseColor("#EF4444")
                }
                canvas.drawCircle(cx, cy - 30f, 20f, alertPaint)

                val alertRing = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    style = Paint.Style.STROKE
                    strokeWidth = 3f
                    color = Color.parseColor("#F59E0B")
                }
                canvas.drawCircle(cx, cy - 30f, 20f, alertRing)

                val exPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = Color.WHITE
                    textSize = 24f
                    typeface = Typeface.DEFAULT_BOLD
                    textAlign = Paint.Align.CENTER
                }
                canvas.drawText("!", cx, cy - 22f, exPaint)
            } else {
                // Classic Ornate Mosque Dome Silhouette
                drawMosqueSilhouette(canvas, goldPaint, cx, cy + 6f, size * 0.72f)
            }

            bitmap
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create custom large icon", e)
            null
        }
    }

    private fun drawMosqueSilhouette(canvas: Canvas, paint: Paint, cx: Float, baseCy: Float, width: Float) {
        val path = Path()
        val halfW = width / 2f
        val baseH = baseCy + 36f
        val minaretW = width * 0.12f

        // Left Minaret
        path.moveTo(cx - halfW, baseH)
        path.lineTo(cx - halfW, baseCy - 16f)
        path.lineTo(cx - halfW + minaretW / 2f, baseCy - 32f) // Spire
        path.lineTo(cx - halfW + minaretW, baseCy - 16f)
        path.lineTo(cx - halfW + minaretW, baseH)

        // Right Minaret
        path.moveTo(cx + halfW, baseH)
        path.lineTo(cx + halfW, baseCy - 16f)
        path.lineTo(cx + halfW - minaretW / 2f, baseCy - 32f) // Spire
        path.lineTo(cx + halfW - minaretW, baseCy - 16f)
        path.lineTo(cx + halfW - minaretW, baseH)

        // Center Dome
        val domeRadius = width * 0.28f
        val domeBounds = RectF(cx - domeRadius, baseCy - domeRadius - 10f, cx + domeRadius, baseCy + domeRadius - 10f)
        path.arcTo(domeBounds, 180f, 180f, false)

        // Base rectangle connecting minarets and dome
        path.lineTo(cx + halfW, baseH)
        path.lineTo(cx - halfW, baseH)
        path.close()

        canvas.drawPath(path, paint)

        // Crescent moon on top of central dome
        val crescentPaint = Paint(paint).apply {
            color = Color.parseColor("#F59E0B")
        }
        val crescent = Path().apply {
            addCircle(cx, baseCy - domeRadius - 22f, 9f, Path.Direction.CW)
        }
        val inner = Path().apply {
            addCircle(cx + 3f, baseCy - domeRadius - 24f, 7.5f, Path.Direction.CW)
        }
        crescent.op(inner, Path.Op.DIFFERENCE)
        canvas.drawPath(crescent, crescentPaint)

        // Central Arch Door (contrasting cutout)
        val doorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#0B0F19")
        }
        val doorRect = RectF(cx - 9f, baseH - 22f, cx + 9f, baseH)
        val door = Path().apply {
            addRoundRect(doorRect, floatArrayOf(8f, 8f, 8f, 8f, 0f, 0f, 0f, 0f), Path.Direction.CW)
        }
        canvas.drawPath(door, doorPaint)
    }

    private fun drawStar(canvas: Canvas, paint: Paint, x: Float, y: Float, outerR: Float, points: Int) {
        val path = Path()
        val innerR = outerR * 0.4f
        for (i in 0 until points * 2) {
            val r = if (i % 2 == 0) outerR else innerR
            val angle = i * Math.PI / points - Math.PI / 2
            val px = (x + r * cos(angle)).toFloat()
            val py = (y + r * sin(angle)).toFloat()
            if (i == 0) path.moveTo(px, py)
            else path.lineTo(px, py)
        }
        path.close()
        canvas.drawPath(path, paint)
    }
}
