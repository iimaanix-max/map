package com.tasbih.digitaltasbih.ui.components

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import com.tasbih.digitaltasbih.data.PrayerRepository

object PermissionHelper {

    fun hasLocationPermission(context: Context): Boolean {
        val fine = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }

    fun hasNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    /**
     * Checks if location denial dialog should be automatically shown.
     * Respects user choice: if user denied before, waits at least 3 days before re-prompting.
     */
    fun shouldPromptLocation(context: Context): Boolean {
        if (hasLocationPermission(context)) return false
        val lastDenied = PrayerRepository.getLocationDeniedLastTime()
        if (lastDenied == 0L) return true
        val threeDaysMs = 3 * 24 * 60 * 60 * 1000L
        return (System.currentTimeMillis() - lastDenied) > threeDaysMs
    }

    fun openAppSettings(context: Context) {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (_: Exception) {
            try {
                val fallbackIntent = Intent(Settings.ACTION_SETTINGS).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(fallbackIntent)
            } catch (_: Exception) {}
        }
    }

    fun openNotificationSettings(context: Context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
                return
            }
        } catch (_: Exception) {}
        openAppSettings(context)
    }
}

/**
 * Professional, Multi-Lingual Location Permission Required Dialog
 * Matches user's chosen language with strict rationale:
 * Local prayer times and Qibla direction CANNOT work accurately without device location.
 */
@Composable
fun LocationPermissionDialog(
    selectedLang: String = PrayerRepository.getLanguage(),
    featureName: String = "Prayer Times & Qibla",
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    val (titleText, bodyText, btnSettingsText, btnDismissText) = when (selectedLang.lowercase()) {
        "hi" -> Quad(
            "स्थान (Location) अनुमति आवश्यक है 📍",
            "सटीक नमाज़ के समय और क़िबला दिशा के लिए आपके शहर की लोकेशन की सख्त ज़रूरत है। बिना लोकेशन के सही अज़ान और नमाज़ का वक्त तय नहीं हो सकता। कृपया सेटिंग्स में जाकर लोकेशन अनुमति दें।",
            "फ़ोन सेटिंग्स खोलें ⚙️",
            "बाद में (डिफ़ॉल्ट रखें)"
        )
        "ur" -> Quad(
            "مقام (Location) کی اجازت درکار ہے 📍",
            "آپ کے شہر کے مطابق نماز کے اوقات اور قبلہ رخ معلوم کرنے کے لیے لوکیشن کی اشد ضرورت ہے۔ لوکیشن کے بغیر درست اوقات ممکن نہیں۔ براہ کرم سیٹنگز میں لوکیشن آن کریں۔",
            "فون سیٹنگز کھولیں ⚙️",
            "بعد میں"
        )
        "ar" -> Quad(
            "مطلوب إذن الموقع 📍",
            "يحتاج التطبيق إلى إذن الموقع لحساب أوقات الصلاة واتجاه القبلة بدقة لمدينتك. لا يمكن حساب الأوقات بدقة بدون الموقع. يرجى تفعيل إذن الموقع من الإعدادات.",
            "فتح إعدادات الهاتف ⚙️",
            "لاحقاً"
        )
        "bn" -> Quad(
            "লোকেশন অনুমতি প্রয়োজন 📍",
            "আপনার শহরের সঠিক নামাজের সময় এবং কিবলার দিক নির্ধারণের জন্য লোকেশন অনুমতি আবশ্যক। লোকেশন ছাড়া সঠিক সময় নির্ধারণ সম্ভব নয়। অনুগ্রহ করে সেটিংসে গিয়ে লোকেশন অন করুন।",
            "ফোন সেটিংস খুলুন ⚙️",
            "পরে"
        )
        "gu" -> Quad(
            "લોકેશન પરવાનગી જરૂરી છે 📍",
            "તમારા શહેરના ચોક્કસ નમાઝના સમય અને કિબલા દિશા માટે લોકેશન પરવાનગી ખૂબ જરૂરી છે. લોકેશન વગર સચોટ સમય શક્ય નથી. કૃપા કરીને સેટિંગ્સમાં લોકેશન ચાલુ કરો.",
            "સેટિંગ્સ ખોલો ⚙️",
            "પછીથી"
        )
        "ta" -> Quad(
            "இருப்பிட அனுமதி தேவை 📍",
            "துல்லியமான தொழுகை நேரங்கள் மற்றும் கிப்லா திசையைக் கணக்கிட உங்கள் இருப்பிடம் அவசியமாகும். அமைப்புகளுக்குச் சென்று அனுமதியை இயக்கவும்.",
            "அமைப்புகளைத் திறக்கவும் ⚙️",
            "பின்னர்"
        )
        "te" -> Quad(
            "లొకేషన్ అనుమతి అవసరం 📍",
            "ఖచ్చితమైన ప్రార్థన సమయాలు మరియు ఖిబ్లా దిశను తెలుసుకోవడానికి మీ లొకేషన్ తప్పనిసరి. దయచేసి సెట్టింగ్స్‌లో అనుమతిని ప్రారంభించండి.",
            "సెట్టింగ్‌లను తెరవండి ⚙️",
            "తర్వాత"
        )
        "hinglish" -> Quad(
            "Location Permission Zaroori Hai 📍",
            "Sahi Namaz ke time aur Qibla direction ke liye aapki location ki sakht zaroorat hai. Bina location ke accurate times calculate nahi ho sakte. Please Settings me jaakar Location allow karein.",
            "Phone Settings Kholein ⚙️",
            "Baad Me"
        )
        else -> Quad(
            "Location Access Required 📍",
            "Digital Tasbih strictly requires your device location to calculate exact prayer times and Qibla direction for your city. Without location, accurate timings cannot be calculated. Please grant location access in App Settings.",
            "Open Phone Settings ⚙️",
            "Dismiss (Use Default)"
        )
    }

    Dialog(onDismissRequest = {
        PrayerRepository.setLocationDeniedLastTime()
        onDismiss()
    }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .shadow(elevation = 20.dp, shape = RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0C241B)),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFC9A84C))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Location Icon Emerald-Gold Glow Badge
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF146450))
                        .border(1.5.dp, Color(0xFFC9A84C), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFFF3D48A),
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Title
                Text(
                    text = titleText,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFF0EAD6),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Body Message
                Text(
                    text = bodyText,
                    fontSize = 12.5.sp,
                    lineHeight = 18.sp,
                    color = Color(0xFFD1D5DB),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Open App Settings Button
                Button(
                    onClick = {
                        PermissionHelper.openAppSettings(context)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFC9A84C),
                        contentColor = Color(0xFF071510)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFF071510)
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(
                        text = btnSettingsText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Dismiss Button
                OutlinedButton(
                    onClick = {
                        PrayerRepository.setLocationDeniedLastTime()
                        onDismiss()
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFA89F8B)
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFC9A84C).copy(alpha = 0.35f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    Text(
                        text = btnDismissText,
                        fontSize = 12.sp,
                        color = Color(0xFFA89F8B)
                    )
                }
            }
        }
    }
}

/**
 * Professional Notification Permission Required Dialog
 */
@Composable
fun NotificationPermissionDialog(
    selectedLang: String = PrayerRepository.getLanguage(),
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    val (titleText, bodyText, btnSettingsText, btnDismissText) = when (selectedLang.lowercase()) {
        "hi" -> Quad(
            "सूचना (Notification) अनुमति आवश्यक है 🔔",
            "अज़ान, नमाज़ अलर्ट और रोज़ाना तस्बीह रिमाइंडर पाने के लिए नोटिफिकेशन अनुमति ज़रूरी है। इसके बिना नमाज़ के वक़्त फ़ोन आपको सूचित नहीं कर पाएगा।",
            "नोटिफिकेशन चालू करें ⚙️",
            "बाद में"
        )
        "ur" -> Quad(
            "اطلاعات (Notification) کی اجازت درکار ہے 🔔",
            "اذان الرٹس، نماز کے اوقات اور روزانہ ذکر ریمائنڈر حاصل کرنے کے لیے نوٹیفکیشن آن کریں۔",
            "نوٹیفکیشن سیٹنگز کھولیں ⚙️",
            "بعد میں"
        )
        "ar" -> Quad(
            "مطلوب إذن الإشعارات 🔔",
            "لتلقي تنبيهات الأذان، وأوقات الصلاة، وتذكيرات الذكر اليومية، يرجى تفعيل إذن الإشعارات.",
            "تفعيل الإشعارات ⚙️",
            "لاحقاً"
        )
        "bn" -> Quad(
            "নোটিফিকেশন অনুমতি প্রয়োজন 🔔",
            "আযান, নামাজের সময় এবং প্রতিদিনের তাসবিহ রিমাইন্ডার পেতে অনুগ্রহ করে নোটিফিকেশন চালু করুন।",
            "নোটিফিকেশন সেটিংস খুলুন ⚙️",
            "পরে"
        )
        "hinglish" -> Quad(
            "Notification Permission Zaroori Hai 🔔",
            "Azan alerts, Salah reminders aur daily Tasbih notifications paane ke liye permission allow karein.",
            "Notification On Karein ⚙️",
            "Baad Me"
        )
        else -> Quad(
            "Notification Access Needed 🔔",
            "To receive timely Azan alerts, Salah reminders, and daily Dhikr notifications, please allow notification access in App Settings.",
            "Enable Notifications ⚙️",
            "Later"
        )
    }

    Dialog(onDismissRequest = {
        PrayerRepository.setNotificationDeniedLastTime()
        onDismiss()
    }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .shadow(elevation = 20.dp, shape = RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0C241B)),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFC9A84C))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF146450))
                        .border(1.5.dp, Color(0xFFC9A84C), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = null,
                        tint = Color(0xFFF3D48A),
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = titleText,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFF0EAD6),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = bodyText,
                    fontSize = 12.5.sp,
                    lineHeight = 18.sp,
                    color = Color(0xFFD1D5DB),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        PermissionHelper.openNotificationSettings(context)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFC9A84C),
                        contentColor = Color(0xFF071510)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFF071510)
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(
                        text = btnSettingsText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = {
                        PrayerRepository.setNotificationDeniedLastTime()
                        onDismiss()
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFA89F8B)
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFC9A84C).copy(alpha = 0.35f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    Text(
                        text = btnDismissText,
                        fontSize = 12.sp,
                        color = Color(0xFFA89F8B)
                    )
                }
            }
        }
    }
}

private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
