package com.tasbih.digitaltasbih.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.tasbih.digitaltasbih.data.DhikrRepository
import com.tasbih.digitaltasbih.data.PrayerRepository

/**
 * BootCompletedReceiver
 * Dedicated broadcast receiver for android.intent.action.BOOT_COMPLETED.
 * Protected by android.permission.RECEIVE_BOOT_COMPLETED so only the Android OS can trigger it.
 * This allows all action-specific receivers (PrayerAlarmReceiver, DhikrReminderReceiver,
 * JummahReminderReceiver) to remain securely unexported (android:exported="false").
 */
class BootCompletedReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "BootCompletedReceiver"
    }

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent == null || intent.action != Intent.ACTION_BOOT_COMPLETED) return

        try {
            Log.d(TAG, "Device reboot completed. Rescheduling all background alarms and reminders...")
            PrayerRepository.init(context)
            DhikrRepository.init(context)

            // Reschedule prayer notifications & warnings
            PrayerRepository.scheduleAllPrayerNotifications(context)

            // Reschedule daily 9 PM dhikr reminder
            PrayerRepository.scheduleDhikrDailyReminder(context)

            // Reschedule Friday Jummah reminders
            PrayerRepository.scheduleJummahReminders(context)
        } catch (e: Exception) {
            Log.e(TAG, "Error rescheduling reminders on boot: ${e.message}", e)
        }
    }
}
