package com.tasbih.digitaltasbih.ui.screens

import android.content.Context
import android.media.MediaPlayer
import android.widget.Toast
import androidx.annotation.RawRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import com.tasbih.digitaltasbih.data.PrayerSoundManager
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.tasbih.digitaltasbih.ui.components.AppToast
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.R
import com.tasbih.digitaltasbih.data.LanguageItem
import com.tasbih.digitaltasbih.data.PrayerRepository
import com.tasbih.digitaltasbih.ui.components.LanguageBar
import com.tasbih.digitaltasbih.ui.components.MoreLanguagesModal

// ── Palette Aliases for Adhan UI (Adaptive Day & Night) ──
private val DarkBackground = Color(0xFF04120D)
private val LightBackground = Color(0xFFF6F4EE)
private val DarkSurface = Color(0xFF081C15)
private val LightSurface = Color(0xFFFFFFFF)
private val DarkCard = Color(0xFF0D251C)
private val LightCard = Color(0xFFFFFFFF)
private val DarkBorder = Color(0x33C9A84C)
private val LightBorder = Color(0xFFE2E8F0)
private val DarkTextMuted = Color(0xFF8FA89B)
private val LightTextMuted = Color(0xFF5B6B62)
private val PrimaryEmerald = Color(0xFF0E5D47)
private val MidnightEmerald = Color(0xFF052017)
private val VividEmerald = Color(0xFF10B981)
private val PureGold = Color(0xFFD4AF37)
private val ChampagneGold = Color(0xFFFDE8A8)

/**
 * Data model for the 7 Sacred Adhans, with full multi-language support.
 */
data class SacredAdhanItem(
    val id: String,
    @RawRes val audioRes: Int,
    val titleEn: String,
    val titleHi: String,
    val titleUr: String,
    val titleAr: String,
    val muadhinEn: String,
    val muadhinHi: String,
    val muadhinUr: String,
    val muadhinAr: String,
    val countryNameEn: String,
    val countryNameHi: String,
    val countryNameUr: String,
    val countryNameAr: String,
    val countryFlag: String,
    val styleEn: String,
    val styleHi: String,
    val styleUr: String,
    val styleAr: String,
    val duration: String
) {
    val rawResName: String get() = PrayerSoundManager.mapAdhanToRawName(id)

    fun getTitle(lang: String): String = when (lang) {
        "hi" -> titleHi
        "ur" -> titleUr
        "ar" -> titleAr
        "bn" -> when (id) {
            "adhan_ahmad_al_nafees" -> "শায়খ আহমদ আল-নাফীসের আযান"
            "adhan_hafiz_mustafa_ozcan" -> "হাফেজ মুস্তফা ওজকানের আযান"
            "adhan_sheikh_mansour_al_zahrani" -> "শায়খ মনসুর আল-জাহরানীর আযান"
            "adhan_mishary_dubai_one_tv" -> "মিশারী রাশিদ আল-আফাসীর আযান (দুবাই টিভি)"
            "adhan_karl_jenkins_mass_for_peace" -> "কার্ল জেনকিন্স পিস আযান"
            "adhan_mishary_alafasy_style_2" -> "মিশারী রাশিদ আল-আফাসীর আযান (শৈলী ২)"
            "adhan_fajr_mishary_alafasy" -> "মিশারী রাশিদ আল-আফাসীর ফজরের আযান"
            else -> titleEn
        }
        "tr" -> when (id) {
            "adhan_ahmad_al_nafees" -> "Şeyh Ahmed El-Nefis Ezanı"
            "adhan_hafiz_mustafa_ozcan" -> "Hafız Mustafa Özcan Ezanı"
            "adhan_sheikh_mansour_al_zahrani" -> "Şeyh Mansur El-Zahrani Ezanı"
            "adhan_mishary_dubai_one_tv" -> "Mişari Raşid El-Afasi Ezanı (Dubai TV)"
            "adhan_karl_jenkins_mass_for_peace" -> "Karl Jenkins Barış İçin Ezan"
            "adhan_mishary_alafasy_style_2" -> "Mişari Raşid El-Afasi Ezanı (2. Makam)"
            "adhan_fajr_mishary_alafasy" -> "Mişari Raşid Sabah Ezanı"
            else -> titleEn
        }
        "id", "ms" -> when (id) {
            "adhan_ahmad_al_nafees" -> "Adzan Syekh Ahmad Al-Nafees"
            "adhan_hafiz_mustafa_ozcan" -> "Adzan Hafiz Mustafa Özcan"
            "adhan_sheikh_mansour_al_zahrani" -> "Adzan Syekh Mansour Al-Zahrani"
            "adhan_mishary_dubai_one_tv" -> "Adzan Dubai One TV Mishary Alafasy"
            "adhan_karl_jenkins_mass_for_peace" -> "Adzan Perdamaian Karl Jenkins"
            "adhan_mishary_alafasy_style_2" -> "Adzan Mishary Rashid Alafasy (Lagu 2)"
            "adhan_fajr_mishary_alafasy" -> "Adzan Subuh Mishary Rashid Alafasy"
            else -> titleEn
        }
        "fa" -> when (id) {
            "adhan_ahmad_al_nafees" -> "اذان شیخ احمد النفیس"
            "adhan_hafiz_mustafa_ozcan" -> "اذان حافظ مصطفیٰ اوزجان"
            "adhan_sheikh_mansour_al_zahrani" -> "اذان شیخ منصور الزهرانی"
            "adhan_mishary_dubai_one_tv" -> "اذان دبی تی‌وی با صدای مشاری العفاسی"
            "adhan_karl_jenkins_mass_for_peace" -> "اذان صلح کارل جنکینز"
            "adhan_mishary_alafasy_style_2" -> "اذان مشاری راشد العفاسی (سبک ۲)"
            "adhan_fajr_mishary_alafasy" -> "اذان صبح مشاری راشد العفاسی"
            else -> titleUr
        }
        "ta" -> when (id) {
            "adhan_ahmad_al_nafees" -> "ஷேக் அஹ்மத் அல்-நஃபீஸ் அதான்"
            "adhan_hafiz_mustafa_ozcan" -> "ஹாஃபிழ் முஸ்தஃபா ஓஸ்கான் அதான்"
            "adhan_sheikh_mansour_al_zahrani" -> "ஷேக் மன்சூர் அல்-ஜஹ்ரானி அதான்"
            "adhan_mishary_dubai_one_tv" -> "துபாய் ஒன் டிவி மிஷாரி அலஃபாஸி அதான்"
            "adhan_karl_jenkins_mass_for_peace" -> "கார்ல் ஜென்கின்ஸ் அமைதி அதான்"
            "adhan_mishary_alafasy_style_2" -> "மிஷாரி ரஷித் அலஃபாஸி அதான் (முறை 2)"
            "adhan_fajr_mishary_alafasy" -> "மிஷாரி ரஷித் ஃபஜ்ர் அதான்"
            else -> titleEn
        }
        "te" -> when (id) {
            "adhan_ahmad_al_nafees" -> "షేక్ అహ్మద్ అల్-నఫీస్ అజాన్"
            "adhan_hafiz_mustafa_ozcan" -> "హాఫిజ్ ముస్తఫా ఓజ్కాన్ అజాన్"
            "adhan_sheikh_mansour_al_zahrani" -> "షేక్ మన్సూర్ అల్-జహ్రానీ అజాన్"
            "adhan_mishary_dubai_one_tv" -> "దుబాయ్ వన్ టీవీ మిషారీ అల్-అఫాసీ అజాన్"
            "adhan_karl_jenkins_mass_for_peace" -> "కార్ల్ జెంకిన్స్ శాంతి అజాన్"
            "adhan_mishary_alafasy_style_2" -> "మిషారీ రషీద్ అల్-అఫాసీ అజాన్ (శైలి 2)"
            "adhan_fajr_mishary_alafasy" -> "మిషారీ రషీద్ ఫజర్ అజాన్"
            else -> titleEn
        }
        "ml" -> when (id) {
            "adhan_ahmad_al_nafees" -> "ശൈഖ് അഹ്മദ് അൽ-നഫീസ് ബാങ്ക്"
            "adhan_hafiz_mustafa_ozcan" -> "ഹാഫിള് മുസ്തഫ ഓസ്കാൻ ബാങ്ക്"
            "adhan_sheikh_mansour_al_zahrani" -> "ശൈഖ് മൻസൂർ അൽ-സഹ്റാനി ബാങ്ക്"
            "adhan_mishary_dubai_one_tv" -> "ദുബായ് വൺ ടിവി മിശ്രി അലഫാസി ബാങ്ക്"
            "adhan_karl_jenkins_mass_for_peace" -> "കാൾ ജെൻകിൻസ് സമാധാന ബാങ്ക്"
            "adhan_mishary_alafasy_style_2" -> "മിശ്രി റാഷിദ് അലഫാസി ബാങ്ക് (ശൈലി 2)"
            "adhan_fajr_mishary_alafasy" -> "മിശ്രി റാഷിദ് സുബ്ഹ് ബാങ്ക്"
            else -> titleEn
        }
        "gu" -> when (id) {
            "adhan_ahmad_al_nafees" -> "શેખ અહમદ અલ-નફીસ અઝાન"
            "adhan_hafiz_mustafa_ozcan" -> "હાફિઝ મુસ્તફા ઓઝકન અઝાન"
            "adhan_sheikh_mansour_al_zahrani" -> "શેખ મન્સૂર અલ-ઝહરાની અઝાન"
            "adhan_mishary_dubai_one_tv" -> "દુબઈ વન ટીવી મિશારી અલાફાસી અઝાન"
            "adhan_karl_jenkins_mass_for_peace" -> "કાર્લ જેનકિન્સ શાંતિ અઝાન"
            "adhan_mishary_alafasy_style_2" -> "મિશારી રશીદ અલાફાસી અઝાન (શૈલી ૨)"
            "adhan_fajr_mishary_alafasy" -> "મિશારી રશીદ ફજર અઝાન"
            else -> titleHi
        }
        "mr" -> when (id) {
            "adhan_ahmad_al_nafees" -> "शेख अहमद अल-नफीस अझान"
            "adhan_hafiz_mustafa_ozcan" -> "हाफिज मुस्तफा ओझकन अझान"
            "adhan_sheikh_mansour_al_zahrani" -> "शेख मन्सूर अल-झहराणी अझान"
            "adhan_mishary_dubai_one_tv" -> "दुबई वन टीव्ही मिशारी अलाफसी अझान"
            "adhan_karl_jenkins_mass_for_peace" -> "कार्ल जेनकइन्स शांतता अझान"
            "adhan_mishary_alafasy_style_2" -> "मिशारी रशीद अलाफसी अझान (शैली २)"
            "adhan_fajr_mishary_alafasy" -> "मिशारी रशीद फजर अझान"
            else -> titleHi
        }
        "pa" -> when (id) {
            "adhan_ahmad_al_nafees" -> "ਸ਼ੇਖ਼ ਅਹਿਮਦ ਅਲ-ਨਫ਼ੀਸ ਅਜ਼ਾਨ"
            "adhan_hafiz_mustafa_ozcan" -> "ਹਾਫ਼ਿਜ਼ ਮੁਸਤਫ਼ਾ ਓਜ਼ਕਾਨ ਅਜ਼ਾਨ"
            "adhan_sheikh_mansour_al_zahrani" -> "ਸ਼ੇਖ਼ ਮਨਸੂਰ ਅਲ-ਜ਼ਹਿਰਾਨੀ ਅਜ਼ਾਨ"
            "adhan_mishary_dubai_one_tv" -> "ਦੁਬਈ ਵਨ ਟੀਵੀ ਮਿਸ਼ਾਰੀ ਅਲਾਫਾਸੀ ਅਜ਼ਾਨ"
            "adhan_karl_jenkins_mass_for_peace" -> "ਕਾਰਲ ਜੈਨਕਿਨਸ ਅਮਨ ਅਜ਼ਾਨ"
            "adhan_mishary_alafasy_style_2" -> "ਮਿਸ਼ਾਰੀ ਰਸ਼ੀਦ ਅਲਾਫਾਸੀ ਅਜ਼ਾਨ (ਸ਼ੈਲੀ 2)"
            "adhan_fajr_mishary_alafasy" -> "ਮਿਸ਼ਾਰੀ ਰਸ਼ੀਦ ਫ਼ਜਰ ਅਜ਼ਾਨ"
            else -> titleHi
        }
        "fr" -> when (id) {
            "adhan_ahmad_al_nafees" -> "Adhan de Cheikh Ahmad Al-Nafees"
            "adhan_hafiz_mustafa_ozcan" -> "Adhan de Hafiz Mustafa Özcan"
            "adhan_sheikh_mansour_al_zahrani" -> "Adhan de Cheikh Mansour Al-Zahrani"
            "adhan_mishary_dubai_one_tv" -> "Adhan Dubai TV par Mishary Alafasy"
            "adhan_karl_jenkins_mass_for_peace" -> "Adhan pour la Paix de Karl Jenkins"
            "adhan_mishary_alafasy_style_2" -> "Adhan par Mishary Rashid Alafasy (Style 2)"
            "adhan_fajr_mishary_alafasy" -> "Adhan du Fajr par Mishary Rashid Alafasy"
            else -> titleEn
        }
        "ru" -> when (id) {
            "adhan_ahmad_al_nafees" -> "Азан шейха Ахмада Аль-Нафиса"
            "adhan_hafiz_mustafa_ozcan" -> "Азан хафиза Мустафы Озджана"
            "adhan_sheikh_mansour_al_zahrani" -> "Азан шейха Мансура Аль-Захрани"
            "adhan_mishary_dubai_one_tv" -> "Азан Dubai TV Мишари Рашида"
            "adhan_karl_jenkins_mass_for_peace" -> "Азан Карла Дженкинса за мир"
            "adhan_mishary_alafasy_style_2" -> "Азан Мишари Рашида Аль-Афаси (стиль 2)"
            "adhan_fajr_mishary_alafasy" -> "Утренний азан (Фаджр) Мишари Аль-Афаси"
            else -> titleEn
        }
        else -> titleEn
    }

    fun getMuadhin(lang: String): String = when (lang) {
        "hi" -> muadhinHi
        "ur" -> muadhinUr
        "ar" -> muadhinAr
        "bn" -> when (id) {
            "adhan_ahmad_al_nafees" -> "শায়খ আহমদ আল-নাফীস"
            "adhan_hafiz_mustafa_ozcan" -> "হাফেজ মুস্তফা ওজকান"
            "adhan_sheikh_mansour_al_zahrani" -> "শায়খ মনসুর আল-জাহরানী"
            "adhan_karl_jenkins_mass_for_peace" -> "আন্তর্জাতিক শান্তি কোরাস"
            else -> "মিশারী রাশিদ আল-আফাসী"
        }
        "tr" -> when (id) {
            "adhan_ahmad_al_nafees" -> "Şeyh Ahmed El-Nefis"
            "adhan_hafiz_mustafa_ozcan" -> "Hafız Mustafa Özcan"
            "adhan_sheikh_mansour_al_zahrani" -> "Şeyh Mansur El-Zahrani"
            "adhan_karl_jenkins_mass_for_peace" -> "Uluslararası Barış Korosu"
            else -> "Mişari Raşid El-Afasi"
        }
        "id", "ms" -> when (id) {
            "adhan_ahmad_al_nafees" -> "Syekh Ahmad Al-Nafees"
            "adhan_hafiz_mustafa_ozcan" -> "Hafiz Mustafa Özcan"
            "adhan_sheikh_mansour_al_zahrani" -> "Syekh Mansour Al-Zahrani"
            "adhan_karl_jenkins_mass_for_peace" -> "Paduan Suara Perdamaian"
            else -> "Mishary Rashid Alafasy"
        }
        "fa" -> when (id) {
            "adhan_ahmad_al_nafees" -> "شیخ احمد النفیس"
            "adhan_hafiz_mustafa_ozcan" -> "حافظ مصطفیٰ اوزجان"
            "adhan_sheikh_mansour_al_zahrani" -> "شیخ منصور الزهرانی"
            "adhan_karl_jenkins_mass_for_peace" -> "گروه کر صلح جهانی"
            else -> "مشاری راشد العفاسی"
        }
        "ta" -> when (id) {
            "adhan_ahmad_al_nafees" -> "ஷேக் அஹ்மத் அல்-நஃபீஸ்"
            "adhan_hafiz_mustafa_ozcan" -> "ஹாஃபிழ் முஸ்தஃபா ஓஸ்கான்"
            "adhan_sheikh_mansour_al_zahrani" -> "ஷேக் மன்சூர் அல்-ஜஹ்ரானி"
            "adhan_karl_jenkins_mass_for_peace" -> "அமைதி பாடகர் குழு"
            else -> "மிஷாரி ரஷித் அலஃபாஸி"
        }
        "te" -> when (id) {
            "adhan_ahmad_al_nafees" -> "షేక్ అహ్మద్ అల్-నఫీస్"
            "adhan_hafiz_mustafa_ozcan" -> "హాఫిజ్ ముస్తఫా ఓజ్కాన్"
            "adhan_sheikh_mansour_al_zahrani" -> "షేక్ మన్సూర్ అల్-జహ్రానీ"
            "adhan_karl_jenkins_mass_for_peace" -> "శాంతి బృందం"
            else -> "మిషారీ రషీద్ అల్-అఫాసీ"
        }
        "ml" -> when (id) {
            "adhan_ahmad_al_nafees" -> "ശൈഖ് അഹ്മദ് അൽ-നഫീസ്"
            "adhan_hafiz_mustafa_ozcan" -> "ഹാഫിള് മുസ്തഫ ഓസ്കാൻ"
            "adhan_sheikh_mansour_al_zahrani" -> "ശൈഖ് മൻസൂർ അൽ-സഹ്റാനി"
            "adhan_karl_jenkins_mass_for_peace" -> "സമാധാന ഗായകസംഘം"
            else -> "മിശ്രി റാഷിദ് അലഫാസി"
        }
        "gu" -> when (id) {
            "adhan_ahmad_al_nafees" -> "શેખ અહમદ અલ-નફીસ"
            "adhan_hafiz_mustafa_ozcan" -> "હાફિઝ મુસ્તફા ઓઝકન"
            "adhan_sheikh_mansour_al_zahrani" -> "શેખ મન્સૂર અલ-ઝહરાની"
            "adhan_karl_jenkins_mass_for_peace" -> "શાંતિ ગાયકવૃંદ"
            else -> "મિશારી રશીદ અલાફાસી"
        }
        "mr" -> when (id) {
            "adhan_ahmad_al_nafees" -> "शेख अहमद अल-नफीस"
            "adhan_hafiz_mustafa_ozcan" -> "हाफिज मुस्तफा ओझकन"
            "adhan_sheikh_mansour_al_zahrani" -> "शेख मन्सूर अल-झहराणी"
            "adhan_karl_jenkins_mass_for_peace" -> "शांतता गायकवृंद"
            else -> "मिशारी रशीद अलाफसी"
        }
        "pa" -> when (id) {
            "adhan_ahmad_al_nafees" -> "ਸ਼ੇਖ਼ ਅਹਿਮਦ ਅਲ-ਨਫ਼ੀਸ"
            "adhan_hafiz_mustafa_ozcan" -> "ਹਾਫ਼ਿਜ਼ ਮੁਸਤਫ਼ਾ ਓਜ਼ਕਾਨ"
            "adhan_sheikh_mansour_al_zahrani" -> "ਸ਼ੇਖ਼ ਮਨਸੂਰ ਅਲ-ਜ਼ਹਿਰਾਨੀ"
            "adhan_karl_jenkins_mass_for_peace" -> "ਅਮਨ ਗਾਇਕ ਮੰਡਲੀ"
            else -> "ਮਿਸ਼ਾਰੀ ਰਸ਼ੀਦ ਅਲਾਫਾਸੀ"
        }
        "fr" -> when (id) {
            "adhan_ahmad_al_nafees" -> "Cheikh Ahmad Al-Nafees"
            "adhan_hafiz_mustafa_ozcan" -> "Hafiz Mustafa Özcan"
            "adhan_sheikh_mansour_al_zahrani" -> "Cheikh Mansour Al-Zahrani"
            "adhan_karl_jenkins_mass_for_peace" -> "Chœur International"
            else -> "Mishary Rashid Alafasy"
        }
        "ru" -> when (id) {
            "adhan_ahmad_al_nafees" -> "Шейх Ахмад Аль-Нафис"
            "adhan_hafiz_mustafa_ozcan" -> "Хафиз Мустафа Озджан"
            "adhan_sheikh_mansour_al_zahrani" -> "Шейх Мансур Аль-Захрани"
            "adhan_karl_jenkins_mass_for_peace" -> "Хор мира"
            else -> "Мишари Рашид Аль-Афаси"
        }
        else -> muadhinEn
    }

    fun getCountryName(lang: String): String = when (lang) {
        "hi" -> countryNameHi
        "ur" -> countryNameUr
        "ar" -> countryNameAr
        "bn" -> when (id) {
            "adhan_ahmad_al_nafees" -> "কুয়েত"
            "adhan_hafiz_mustafa_ozcan" -> "তুরস্ক"
            "adhan_sheikh_mansour_al_zahrani" -> "সৌদি আরব"
            "adhan_mishary_dubai_one_tv" -> "দুবাই, ইউএই"
            "adhan_karl_jenkins_mass_for_peace" -> "বিশ্ব শান্তি"
            "adhan_mishary_alafasy_style_2" -> "কুয়েত / মদীনা"
            "adhan_fajr_mishary_alafasy" -> "ফজর বিশেষ (কুয়েত)"
            else -> countryNameEn
        }
        "tr" -> when (id) {
            "adhan_ahmad_al_nafees" -> "Kuveyt"
            "adhan_hafiz_mustafa_ozcan" -> "Türkiye"
            "adhan_sheikh_mansour_al_zahrani" -> "Suudi Arabistan"
            "adhan_mishary_dubai_one_tv" -> "Dubai, BAE"
            "adhan_karl_jenkins_mass_for_peace" -> "Evrensel Barış"
            "adhan_mishary_alafasy_style_2" -> "Kuveyt / Medine"
            "adhan_fajr_mishary_alafasy" -> "Sabah Özel (Kuveyt)"
            else -> countryNameEn
        }
        "id", "ms" -> when (id) {
            "adhan_ahmad_al_nafees" -> "Kuwait"
            "adhan_hafiz_mustafa_ozcan" -> "Turki"
            "adhan_sheikh_mansour_al_zahrani" -> "Arab Saudi"
            "adhan_mishary_dubai_one_tv" -> "Dubai, UEA"
            "adhan_karl_jenkins_mass_for_peace" -> "Kedamaian Global"
            "adhan_mishary_alafasy_style_2" -> "Kuwait / Madinah"
            "adhan_fajr_mishary_alafasy" -> "Fajr Spesial (Kuwait)"
            else -> countryNameEn
        }
        "fa" -> when (id) {
            "adhan_ahmad_al_nafees" -> "کویت"
            "adhan_hafiz_mustafa_ozcan" -> "ترکیه"
            "adhan_sheikh_mansour_al_zahrani" -> "عربستان سعودی"
            "adhan_mishary_dubai_one_tv" -> "دبی، امارات"
            "adhan_karl_jenkins_mass_for_peace" -> "صلح جهانی"
            "adhan_mishary_alafasy_style_2" -> "کویت / مدینه"
            "adhan_fajr_mishary_alafasy" -> "ویژه فجر (کویت)"
            else -> countryNameUr
        }
        "ta" -> when (id) {
            "adhan_ahmad_al_nafees" -> "குவைத்"
            "adhan_hafiz_mustafa_ozcan" -> "துருக்கி"
            "adhan_sheikh_mansour_al_zahrani" -> "சவுதி அரேபியா"
            "adhan_mishary_dubai_one_tv" -> "துபாய், யு.ஏ.இ"
            "adhan_karl_jenkins_mass_for_peace" -> "உலக அமைதி"
            "adhan_mishary_alafasy_style_2" -> "குவைத் / மதீனா"
            "adhan_fajr_mishary_alafasy" -> "ஃபஜ்ர் சிறப்பு (குவைத்)"
            else -> countryNameEn
        }
        "te" -> when (id) {
            "adhan_ahmad_al_nafees" -> "కువైట్"
            "adhan_hafiz_mustafa_ozcan" -> "టర్కీ"
            "adhan_sheikh_mansour_al_zahrani" -> "సౌదీ అరేబియా"
            "adhan_mishary_dubai_one_tv" -> "దుబాయ్, యు.ఎ.ఇ"
            "adhan_karl_jenkins_mass_for_peace" -> "విశ్వ శాంతి"
            "adhan_mishary_alafasy_style_2" -> "కువైట్ / మదీనా"
            "adhan_fajr_mishary_alafasy" -> "ఫజర్ ప్రత్యేకం (కువైట్)"
            else -> countryNameEn
        }
        "ml" -> when (id) {
            "adhan_ahmad_al_nafees" -> "കുവൈത്ത്"
            "adhan_hafiz_mustafa_ozcan" -> "തുർക്കി"
            "adhan_sheikh_mansour_al_zahrani" -> "സൗദി അറേബ്യ"
            "adhan_mishary_dubai_one_tv" -> "ദുബായ്, യു.എ.ഇ"
            "adhan_karl_jenkins_mass_for_peace" -> "ലോക സമാധാനം"
            "adhan_mishary_alafasy_style_2" -> "കുവൈത്ത് / മദീന"
            "adhan_fajr_mishary_alafasy" -> "സുബ്ഹ് സ്പെഷ്യൽ (കുവൈത്ത്)"
            else -> countryNameEn
        }
        "gu" -> when (id) {
            "adhan_ahmad_al_nafees" -> "કુવૈત"
            "adhan_hafiz_mustafa_ozcan" -> "તુર્કી"
            "adhan_sheikh_mansour_al_zahrani" -> "સાઉદી અરેબિયા"
            "adhan_mishary_dubai_one_tv" -> "દુબઈ, યુએઈ"
            "adhan_karl_jenkins_mass_for_peace" -> "વિશ્વ શાંતિ"
            "adhan_mishary_alafasy_style_2" -> "કુવૈત / મદીના"
            "adhan_fajr_mishary_alafasy" -> "ફજર સ્પેશિયલ (કુવૈત)"
            else -> countryNameHi
        }
        "mr" -> when (id) {
            "adhan_ahmad_al_nafees" -> "कुवेत"
            "adhan_hafiz_mustafa_ozcan" -> "तुर्की"
            "adhan_sheikh_mansour_al_zahrani" -> "सौदी अरेबिया"
            "adhan_mishary_dubai_one_tv" -> "दुबई, युएई"
            "adhan_karl_jenkins_mass_for_peace" -> "जागतिक शांतता"
            "adhan_mishary_alafasy_style_2" -> "कुवेत / मदीना"
            "adhan_fajr_mishary_alafasy" -> "फजर विशेष (कुवेत)"
            else -> countryNameHi
        }
        "pa" -> when (id) {
            "adhan_ahmad_al_nafees" -> "ਕੁਵੈਤ"
            "adhan_hafiz_mustafa_ozcan" -> "ਤੁਰਕੀ"
            "adhan_sheikh_mansour_al_zahrani" -> "ਸਊਦੀ ਅਰਬ"
            "adhan_mishary_dubai_one_tv" -> "ਦੁਬਈ, ਯੂ.ਏ.ਈ"
            "adhan_karl_jenkins_mass_for_peace" -> "ਵਿਸ਼ਵ ਅਮਨ"
            "adhan_mishary_alafasy_style_2" -> "ਕੁਵੈਤ / ਮਦੀਨਾ"
            "adhan_fajr_mishary_alafasy" -> "ਫ਼ਜਰ ਸਪੈਸ਼ਲ (ਕੁਵੈਤ)"
            else -> countryNameHi
        }
        "fr" -> when (id) {
            "adhan_ahmad_al_nafees" -> "Koweït"
            "adhan_hafiz_mustafa_ozcan" -> "Turquie"
            "adhan_sheikh_mansour_al_zahrani" -> "Arabie Saoudite"
            "adhan_mishary_dubai_one_tv" -> "Dubaï, ÉAU"
            "adhan_karl_jenkins_mass_for_peace" -> "Harmonie Mondiale"
            "adhan_mishary_alafasy_style_2" -> "Koweït / Médine"
            "adhan_fajr_mishary_alafasy" -> "Spécial Fajr (Koweït)"
            else -> countryNameEn
        }
        "ru" -> when (id) {
            "adhan_ahmad_al_nafees" -> "Кувейт"
            "adhan_hafiz_mustafa_ozcan" -> "Турция"
            "adhan_sheikh_mansour_al_zahrani" -> "Саудовская Аравия"
            "adhan_mishary_dubai_one_tv" -> "Дубай, ОАЭ"
            "adhan_karl_jenkins_mass_for_peace" -> "Всемирный мир"
            "adhan_mishary_alafasy_style_2" -> "Кувейт / Медина"
            "adhan_fajr_mishary_alafasy" -> "Специальный Фаджр (Кувейт)"
            else -> countryNameEn
        }
        else -> countryNameEn
    }

    fun getCountryWithFlag(lang: String): String = "${getCountryName(lang)} $countryFlag"

    fun getStyle(lang: String): String = when (lang) {
        "hi" -> styleHi
        "ur" -> styleUr
        "ar" -> styleAr
        "bn" -> when (id) {
            "adhan_ahmad_al_nafees" -> "হৃদয়স্পর্শী, প্রশান্ত ও সুরেলা আযান"
            "adhan_hafiz_mustafa_ozcan" -> "ঐতিহ্যবাহী অটোমান সাম্রাজ্যিক মসজিদ শৈলী"
            "adhan_sheikh_mansour_al_zahrani" -> "শক্তিশালী, গভীর ও গম্ভীর পবিত্র ধ্বনি"
            "adhan_mishary_dubai_one_tv" -> "বিশ্ববিখ্যাত আইকনিক সুমধুর আযান"
            "adhan_karl_jenkins_mass_for_peace" -> "ঐকতানিক সিম্ফনি ও বিশ্ব শান্তির আহ্বান"
            "adhan_mishary_alafasy_style_2" -> "ঐতিহ্যবাহী হিজায সুর • প্রশান্তিদায়ক"
            "adhan_fajr_mishary_alafasy" -> "\"আস-সালাতু খাইরুম মিনান-নাউম\" সহ"
            else -> styleEn
        }
        "tr" -> when (id) {
            "adhan_ahmad_al_nafees" -> "Huzur Verici, İçten ve Duygusal Melodi"
            "adhan_hafiz_mustafa_ozcan" -> "Klasik Osmanlı Selatin Camii Üslubu"
            "adhan_sheikh_mansour_al_zahrani" -> "Güçlü, Derin ve Yankılanan Kutsal Çağrı"
            "adhan_mishary_dubai_one_tv" -> "Dünyaca Ünlü İkonik Melodik Ezan"
            "adhan_karl_jenkins_mass_for_peace" -> "Senfonik Koro ve Evrensel Barış Çağrısı"
            "adhan_mishary_alafasy_style_2" -> "Geleneksel Hicaz Makamı • Sakinleştirici"
            "adhan_fajr_mishary_alafasy" -> "\"Essalatu Hayrun Minen Nevm\" İlavesiyle"
            else -> styleEn
        }
        "id", "ms" -> when (id) {
            "adhan_ahmad_al_nafees" -> "Melodi Menenangkan, Khusyuk & Menyentuh Hati"
            "adhan_hafiz_mustafa_ozcan" -> "Gaya Khas Masjid Kekaisaran Utsmaniyah"
            "adhan_sheikh_mansour_al_zahrani" -> "Seruan Suci yang Kuat, Dalam & Berwibawa"
            "adhan_mishary_dubai_one_tv" -> "Lantunan Adzan Ikonik Terkenal di Dunia"
            "adhan_karl_jenkins_mass_for_peace" -> "Simfoni Paduan Suara & Seruan Damai Dunia"
            "adhan_mishary_alafasy_style_2" -> "Irama Hijaz Tradisional • Sangat Menyejukkan"
            "adhan_fajr_mishary_alafasy" -> "Disertai \"Ash-Shalatu Khairum Minan-Naum\""
            else -> styleEn
        }
        "fa" -> when (id) {
            "adhan_ahmad_al_nafees" -> "لحن آرام‌بخش، دلنشین و سرشار از احساس"
            "adhan_hafiz_mustafa_ozcan" -> "سبک سنتی مساجد باشکوه عثمانی"
            "adhan_sheikh_mansour_al_zahrani" -> "طنین گیرا، ژرف و پرهیبت اذان"
            "adhan_mishary_dubai_one_tv" -> "اذان نمادین و نغمه ماندگار جهانی"
            "adhan_karl_jenkins_mass_for_peace" -> "سمفونی کرال و دعوت به صلح جهانی"
            "adhan_mishary_alafasy_style_2" -> "نغمه سنتی حجاز • آرامش‌بخش دل‌ها"
            "adhan_fajr_mishary_alafasy" -> "همراه با الصلاة خیر من النوم"
            else -> styleUr
        }
        "ta" -> when (id) {
            "adhan_ahmad_al_nafees" -> "மனதை வருடும், அமைதியான, உணர்வுபூர்வமான அதான்"
            "adhan_hafiz_mustafa_ozcan" -> "ஒட்டோமான் பேரரசு பள்ளிவாசல் பாரம்பரிய பாணி"
            "adhan_sheikh_mansour_al_zahrani" -> "சக்திவாய்ந்த, ஆழமான புனித அழைப்பு"
            "adhan_mishary_dubai_one_tv" -> "உலகப் புகழ் பெற்ற இனிமையான அதான்"
            "adhan_karl_jenkins_mass_for_peace" -> "உலக அமைதிக்கான கூட்டு இசை அழைப்பு"
            "adhan_mishary_alafasy_style_2" -> "பாரம்பரிய ஹிஜாஸ் ராகம் • அமைதியளிக்கும்"
            "adhan_fajr_mishary_alafasy" -> "\"அஸ்ஸலாத்து கைரும் மினன் நவ்ம்\" உடன்"
            else -> styleEn
        }
        "te" -> when (id) {
            "adhan_ahmad_al_nafees" -> "హృదయపూర్వక, ప్రశాంతమైన మరియు భావోద్వేగ అజాన్"
            "adhan_hafiz_mustafa_ozcan" -> "సాంప్రదాయ ఒట్టోమన్ మసీదు శైలి"
            "adhan_sheikh_mansour_al_zahrani" -> "శక్తివంతమైన, గంభీరమైన పవిత్ర పిలుపు"
            "adhan_mishary_dubai_one_tv" -> "ప్రపంచ ప్రసిద్ధ మధురమైన అజాన్"
            "adhan_karl_jenkins_mass_for_peace" -> "విశ్వ శాంతి కోసం సింఫొనీ పిలుపు"
            "adhan_mishary_alafasy_style_2" -> "సాంప్రదాయ హిజాజ్ శైలి • ప్రశాంతత"
            "adhan_fajr_mishary_alafasy" -> "\"అస్సలాతు ఖైరుమ్ మినన్ నౌమ్\" తో"
            else -> styleEn
        }
        "ml" -> when (id) {
            "adhan_ahmad_al_nafees" -> "ഹൃദയസ്പർശിയായ, ശാന്തമായ ശ്രുതിമധുര ബാങ്ക്"
            "adhan_hafiz_mustafa_ozcan" -> "പരമ്പരാഗത ഒട്ടോമൻ പള്ളി ശൈലി"
            "adhan_sheikh_mansour_al_zahrani" -> "ശക്തവും ഗംഭീരവുമായ വിശുദ്ധ വിളി"
            "adhan_mishary_dubai_one_tv" -> "ലോകപ്രസിദ്ധമായ ശ്രുതിമധുര ബാങ്ക്"
            "adhan_karl_jenkins_mass_for_peace" -> "ലോക സമാധാനത്തിനായുള്ള ഐക്യവിളി"
            "adhan_mishary_alafasy_style_2" -> "പരമ്പരാഗത ഹിജാസ് രാഗം • ആശ്വാസകരം"
            "adhan_fajr_mishary_alafasy" -> "\"അസ്സലാത്തു ഖൈറും മിനന്നൗം\" ഉൾപ്പെടെ"
            else -> styleEn
        }
        "gu" -> when (id) {
            "adhan_ahmad_al_nafees" -> "હૃદયસ્પર્શી, શાંત અને મધુર અઝાન"
            "adhan_hafiz_mustafa_ozcan" -> "પરંપરાગત ઓટોમન શાહી મસ્જિદ શૈલી"
            "adhan_sheikh_mansour_al_zahrani" -> "શક્તિશાળી, ઊંડો અને ગંભીર પવિત્ર અવાજ"
            "adhan_mishary_dubai_one_tv" -> "વિશ્વ પ્રસિદ્ધ લોકપ્રિય મધુર અઝાન"
            "adhan_karl_jenkins_mass_for_peace" -> "વિશ્વ શાંતિ માટે સંગીતમય કોલ"
            "adhan_mishary_alafasy_style_2" -> "પરંપરાગત હિઝાઝ લય • શાંતિદાયક"
            "adhan_fajr_mishary_alafasy" -> "\"અસ-સલાતુ ખૈરુમ મિનન-નૌમ\" સાથે"
            else -> styleHi
        }
        "mr" -> when (id) {
            "adhan_ahmad_al_nafees" -> "हृदयस्पर्शी, शांत आणि सुरेल अझान"
            "adhan_hafiz_mustafa_ozcan" -> "पारंपारिक ऑट्टोमन शाही मशीद शैली"
            "adhan_sheikh_mansour_al_zahrani" -> "शक्तिशाली, खोल आणि गंभीर पवित्र पुकार"
            "adhan_mishary_dubai_one_tv" -> "जगप्रसिद्ध लोकप्रिय सुरेल अझान"
            "adhan_karl_jenkins_mass_for_peace" -> "जागतिक शांततेसाठी संगीतमय हाक"
            "adhan_mishary_alafasy_style_2" -> "पारंपारिक हिजाज सूर • शांतता देणारा"
            "adhan_fajr_mishary_alafasy" -> "\"अस-सलातु खैरूम मिनन-नौम\" सह"
            else -> styleHi
        }
        "pa" -> when (id) {
            "adhan_ahmad_al_nafees" -> "ਦਿਲ ਨੂੰ ਛੂਹਣ ਵਾਲੀ, ਸ਼ਾਂਤ ਤੇ ਮਿੱਠੀ ਅਜ਼ਾਨ"
            "adhan_hafiz_mustafa_ozcan" -> "ਰਵਾਇਤੀ ਓਟੋਮਨ ਮਸਜਿਦ ਸ਼ੈਲੀ"
            "adhan_sheikh_mansour_al_zahrani" -> "ਜ਼ੋਰਦਾਰ, ਗੰਭੀਰ ਤੇ ਰੂਹਾਨੀ ਆਵਾਜ਼"
            "adhan_mishary_dubai_one_tv" -> "ਦੁਨੀਆ ਭਰ ਵਿੱਚ ਮਸ਼ਹੂਰ ਦਿਲਕਸ਼ ਅਜ਼ਾਨ"
            "adhan_karl_jenkins_mass_for_peace" -> "ਅਮਨ ਤੇ ਸ਼ਾਂਤੀ ਦਾ ਵਿਸ਼ਵਵਿਆਪੀ ਸੱਦਾ"
            "adhan_mishary_alafasy_style_2" -> "ਰਵਾਇਤੀ ਹਿਜ਼ਾਜ਼ ਲੈਅ • ਸਕੂਨ ਭਰੀ"
            "adhan_fajr_mishary_alafasy" -> "\"ਅਸ-ਸਲਾਤੁ ਖ਼ੈਰੁਮ ਮਿਨਨ-ਨੌਮ\" ਸਮੇਤ"
            else -> styleHi
        }
        "fr" -> when (id) {
            "adhan_ahmad_al_nafees" -> "Mélodie émouvante, sereine et chaleureuse"
            "adhan_hafiz_mustafa_ozcan" -> "Style classique des mosquées impériales ottomanes"
            "adhan_sheikh_mansour_al_zahrani" -> "Appel sacré puissant, profond et résonant"
            "adhan_mishary_dubai_one_tv" -> "Adhan mélodique iconique mondialement célèbre"
            "adhan_karl_jenkins_mass_for_peace" -> "Symphonie chorale et appel à la paix universelle"
            "adhan_mishary_alafasy_style_2" -> "Tonalité Hijaz traditionnelle • Apaisante"
            "adhan_fajr_mishary_alafasy" -> "Avec « As-Salatu Khayrun Minan-Nawm »"
            else -> styleEn
        }
        "ru" -> when (id) {
            "adhan_ahmad_al_nafees" -> "Проникновенный, умиротворяющий и душевный напев"
            "adhan_hafiz_mustafa_ozcan" -> "Классический османский стиль соборных мечетей"
            "adhan_sheikh_mansour_al_zahrani" -> "Мощный, глубокий и величественный призыв"
            "adhan_mishary_dubai_one_tv" -> "Всемирно известный культовый мелодичный азан"
            "adhan_karl_jenkins_mass_for_peace" -> "Хоровая симфония и призыв к всеобщему миру"
            "adhan_mishary_alafasy_style_2" -> "Традиционный напев Хиджаз • Успокаивающий"
            "adhan_fajr_mishary_alafasy" -> "Со словами «Ас-саляту хайрун мина-н-наум»"
            else -> styleEn
        }
        else -> styleEn
    }
}

val SACRED_ADHANS_LIST = listOf(
    SacredAdhanItem(
        id = "adhan_ahmad_al_nafees",
        audioRes = R.raw.adhan_ahmad_al_nafees,
        titleEn = "Adhan by Sheikh Ahmad Al-Nafees",
        titleHi = "अज़ान - शेख अहमद अल-नफीस",
        titleUr = "اذان - شیخ احمد النفیس",
        titleAr = "أذان الشيخ أحمد النفيس",
        muadhinEn = "Sheikh Ahmad Al-Nafees",
        muadhinHi = "शेख अहमद अल-नफीस",
        muadhinUr = "شیخ احمد النفیس",
        muadhinAr = "الشيخ أحمد النفيس",
        countryNameEn = "Kuwait",
        countryNameHi = "कुवैत",
        countryNameUr = "کویت",
        countryNameAr = "الكويت",
        countryFlag = "🇰🇼",
        styleEn = "Heartfelt, Serene & Emotional Melody",
        styleHi = "भावपूर्ण, शांत और मधुर अज़ान",
        styleUr = "دلسوز، پرسکون اور دلنشین لحن",
        styleAr = "نغمة خاشعة، هادئة ومؤثرة",
        duration = "03:34"
    ),
    SacredAdhanItem(
        id = "adhan_hafiz_mustafa_ozcan",
        audioRes = R.raw.adhan_hafiz_mustafa_ozcan,
        titleEn = "Adhan by Hafiz Mustafa Özcan",
        titleHi = "अज़ान - हाफ़िज़ मुस्तफ़ा ओज़कन",
        titleUr = "اذان - حافظ مصطفیٰ اوزجان",
        titleAr = "أذان الحافظ مصطفى أوزجان",
        muadhinEn = "Hafiz Mustafa Özcan",
        muadhinHi = "हाफ़िज़ मुस्तफ़ा ओज़कन",
        muadhinUr = "حافظ مصطفیٰ اوزجان",
        muadhinAr = "الحافظ مصطفى أوزجان",
        countryNameEn = "Turkey",
        countryNameHi = "तुर्की",
        countryNameUr = "ترکی",
        countryNameAr = "تركيا",
        countryFlag = "🇹🇷",
        styleEn = "Classical Ottoman Imperial Mosque Style",
        styleHi = "शास्त्रीय उस्मानी (ऑटोमन) शाही मस्जिद शैली",
        styleUr = "کلاسیکی عثمانی شاہی مسجد کا انداز",
        styleAr = "طراز الجوامع العثمانية الإمبراطورية الكلاسيكية",
        duration = "03:58"
    ),
    SacredAdhanItem(
        id = "adhan_sheikh_mansour_al_zahrani",
        audioRes = R.raw.adhan_mansour_al_zahrani,
        titleEn = "Adhan by Sheikh Mansour Al-Zahrani",
        titleHi = "अज़ान - शेख मंसूर अल-ज़हरानी",
        titleUr = "اذان - شیخ منصور الزهرانی",
        titleAr = "أذان الشيخ منصور الزهراني",
        muadhinEn = "Sheikh Mansour Al-Zahrani",
        muadhinHi = "शेख मंसूर अल-ज़हरानी",
        muadhinUr = "شیخ منصور الزهرانی",
        muadhinAr = "الشيخ منصور الزهراني",
        countryNameEn = "Saudi Arabia",
        countryNameHi = "सऊदी अरब",
        countryNameUr = "سعودی عرب",
        countryNameAr = "المملكة العربية السعودية",
        countryFlag = "🇸🇦",
        styleEn = "Powerful, Deep & Resonant Sacred Call",
        styleHi = "शक्तिशाली, गहरा और गूंजता हुआ पवित्र आह्वान",
        styleUr = "طاقتور، گہرا اور گونج دار مقدس پکار",
        styleAr = "نداء مقدس قوي وعميق ومهيب",
        duration = "03:30"
    ),
    SacredAdhanItem(
        id = "adhan_mishary_dubai_one_tv",
        audioRes = R.raw.adhan_mishary_dubai_tv,
        titleEn = "Adhan from Dubai One TV by Mishary Alafasy",
        titleHi = "अज़ान - दुबई वन टीवी (मिशारी अलाफ़ासी)",
        titleUr = "اذان دبئی ون ٹی وی - مشاری العفاسی",
        titleAr = "أذان تلفزيون دبي ون - مشاري العفاسي",
        muadhinEn = "Mishary Rashid Alafasy",
        muadhinHi = "मिशारी राशिद अलाफ़ासी",
        muadhinUr = "مشاری راشد العفاسی",
        muadhinAr = "مشاري راشد العفاسي",
        countryNameEn = "Dubai, UAE",
        countryNameHi = "दुबई, यूएई",
        countryNameUr = "دبئی، یو اے ای",
        countryNameAr = "دبي، الإمارات",
        countryFlag = "🇦🇪",
        styleEn = "World-Famous Iconic Melodic Adhan",
        styleHi = "विश्व-प्रसिद्ध प्रतिष्ठित और सुरीली अज़ान",
        styleUr = "عالمی شہرت یافتہ پرتاثیر و دلنشین اذان",
        styleAr = "الأذان الرخيم الأيقوني الشهير عالمياً",
        duration = "05:36"
    ),
    SacredAdhanItem(
        id = "adhan_karl_jenkins_mass_for_peace",
        audioRes = R.raw.adhan_karl_jenkins_peace,
        titleEn = "Adhan from Karl Jenkins' Mass for Peace",
        titleHi = "अज़ान - कार्ल जेनकिंस मास फॉर पीस",
        titleUr = "اذان - کارل جینکنز ماس فار پیس",
        titleAr = "أذان كارل جنكينز - قداس من أجل السلام",
        muadhinEn = "Global Harmony Chorus",
        muadhinHi = "ग्लोबल हार्मनी कोरस",
        muadhinUr = "گلوبل ہارمنی کورس",
        muadhinAr = "كورال التناغم والسلام العالمي",
        countryNameEn = "Global Harmony",
        countryNameHi = "विश्व शांति",
        countryNameUr = "عالمی ہم آہنگی",
        countryNameAr = "السلام العالمي",
        countryFlag = "🕊️",
        styleEn = "Choral Symphony & Universal Call to Peace",
        styleHi = "संगीतमय सिम्फनी और शांति का वैश्विक आह्वान",
        styleUr = "سمفونک کورس اور عالمی امن کا نغمہ",
        styleAr = "سيمفونية كورالية ونداء عالمي للسلام",
        duration = "03:07"
    ),
    SacredAdhanItem(
        id = "adhan_mishary_alafasy_style_2",
        audioRes = R.raw.adhan_mishary_alafasy_2,
        titleEn = "Adhan by Mishary Rashid Alafasy (Style 2)",
        titleHi = "अज़ान - मिशारी राशिद अलाफ़ासी (शैली 2)",
        titleUr = "اذان - مشاری راشد العفاسی (انداز دوم)",
        titleAr = "أذان مشاري راشد العفاسي (المقام الثاني)",
        muadhinEn = "Mishary Rashid Alafasy",
        muadhinHi = "मिशारी राशिद अलाफ़ासी",
        muadhinUr = "مشاری راشد العفاسی",
        muadhinAr = "مشاري راشد العفاسي",
        countryNameEn = "Kuwait / Madinah",
        countryNameHi = "कुवैत / मदीना",
        countryNameUr = "کویت / مدینہ منورہ",
        countryNameAr = "الكويت / المدينة المنورة",
        countryFlag = "🇰🇼",
        styleEn = "Traditional Hijaz Tone • Soothing & Uplifting",
        styleHi = "पारंपरिक हिजाज़ लय • सुकून देने वाली और आध्यात्मिक",
        styleUr = "روایتی لحنِ حجاز • روح پرور اور باعثِ سکون",
        styleAr = "مقام الحجاز الأصيل • نغمة روحانية باعثة للسكينة",
        duration = "04:03"
    ),
    SacredAdhanItem(
        id = "adhan_fajr_mishary_alafasy",
        audioRes = R.raw.adhan_mishary_alafasy_3,
        titleEn = "Fajr Adhan by Mishary Rashid Alafasy",
        titleHi = "फ़ज्र अज़ान - मिशारी राशिद अलाफ़ासी",
        titleUr = "فجر اذان - مشاری راشد العفاسی",
        titleAr = "أذان الفجر - مشاري راشد العفاسي",
        muadhinEn = "Mishary Rashid Alafasy",
        muadhinHi = "मिशारी राशिद अलाफ़ासी",
        muadhinUr = "مشاری راشد العفاسی",
        muadhinAr = "مشاري راشد العفاسي",
        countryNameEn = "Fajr Special (Kuwait)",
        countryNameHi = "फ़ज्र स्पेशल (कुवैत)",
        countryNameUr = "خصوصی فجر (کویت)",
        countryNameAr = "فجر خاص (الكويت)",
        countryFlag = "🌅",
        styleEn = "Includes \"As-Salatu Khayrun Minan-Nawm\"",
        styleHi = "\"अस्सलातु खैरूम मिनन्नौम\" के साथ विशेष फ़ज्र अज़ान",
        styleUr = "الصَّلَاةُ خَيْرٌ مِنَ النَّوْمِ کے ساتھ سحر انگیز اذان",
        styleAr = "يتضمن جملة: الصلاة خير من النوم بأداء عذب",
        duration = "04:17"
    )
)

@Composable
fun RosetteEqualizerWave(
    modifier: Modifier = Modifier,
    barColor: Color = ChampagneGold
) {
    val infiniteTransition = rememberInfiniteTransition(label = "WaveTrans")

    val h1 by infiniteTransition.animateFloat(
        initialValue = 3f, targetValue = 12f,
        animationSpec = infiniteRepeatable(tween(500, easing = LinearEasing), RepeatMode.Reverse),
        label = "h1"
    )
    val h2 by infiniteTransition.animateFloat(
        initialValue = 11f, targetValue = 4f,
        animationSpec = infiniteRepeatable(tween(420, easing = LinearEasing), RepeatMode.Reverse),
        label = "h2"
    )
    val h3 by infiniteTransition.animateFloat(
        initialValue = 5f, targetValue = 12f,
        animationSpec = infiniteRepeatable(tween(460, easing = LinearEasing), RepeatMode.Reverse),
        label = "h3"
    )
    val h4 by infiniteTransition.animateFloat(
        initialValue = 10f, targetValue = 3f,
        animationSpec = infiniteRepeatable(tween(530, easing = LinearEasing), RepeatMode.Reverse),
        label = "h4"
    )

    Row(
        modifier = modifier.height(14.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Box(modifier = Modifier.width(2.5.dp).height(h1.dp).background(barColor, RoundedCornerShape(1.dp)))
        Box(modifier = Modifier.width(2.5.dp).height(h2.dp).background(barColor, RoundedCornerShape(1.dp)))
        Box(modifier = Modifier.width(2.5.dp).height(h3.dp).background(barColor, RoundedCornerShape(1.dp)))
        Box(modifier = Modifier.width(2.5.dp).height(h4.dp).background(barColor, RoundedCornerShape(1.dp)))
    }
}

@Composable
fun AdhanAudioScreen(onBack: () -> Unit = {}) {
    val context = LocalContext.current
    var isDarkTheme by remember { mutableStateOf(PrayerRepository.isDarkTheme()) }
    var currentLang by remember { mutableStateOf(PrayerRepository.getLanguage()) }
    var isMoreLangOpen by remember { mutableStateOf(false) }

    val visibleLanguages = remember {
        listOf(
            LanguageItem("en", "English"),
            LanguageItem("hi", "हिंदी"),
            LanguageItem("ur", "اردو"),
            LanguageItem("ar", "العربية"),
            LanguageItem("bn", "বাংলা")
        )
    }

    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var activeAdhanId by remember { mutableStateOf<String?>(null) }
    var isPlaying by remember { mutableStateOf(false) }

    var activeSoundRaw by remember { mutableStateOf(PrayerSoundManager.getSelectedAdhanRaw(context)) }
    var activeSoundMode by remember { mutableStateOf(PrayerSoundManager.getSoundMode(context)) }

    val activeItem = remember(activeAdhanId) {
        SACRED_ADHANS_LIST.firstOrNull { it.id == activeAdhanId }
    }

    // Release player when leaving screen
    DisposableEffect(Unit) {
        onDispose {
            try {
                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = null
            } catch (_: Exception) {}
        }
    }

    fun playOrToggleAdhan(item: SacredAdhanItem) {
        if (activeAdhanId == item.id) {
            mediaPlayer?.let { player ->
                if (player.isPlaying) {
                    player.pause()
                    isPlaying = false
                } else {
                    player.start()
                    isPlaying = true
                }
            }
        } else {
            // Stop and release previous
            try {
                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = null
            } catch (_: Exception) {}

            try {
                val newPlayer = MediaPlayer.create(context, item.audioRes)
                if (newPlayer != null) {
                    mediaPlayer = newPlayer
                    activeAdhanId = item.id
                    isPlaying = true
                    newPlayer.setOnCompletionListener {
                        isPlaying = false
                        activeAdhanId = null
                    }
                    newPlayer.setOnErrorListener { mp, _, _ ->
                        try {
                            mp.stop()
                            mp.release()
                        } catch (_: Exception) {}
                        mediaPlayer = null
                        isPlaying = false
                        activeAdhanId = null
                        true
                    }
                    newPlayer.start()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun stopAdhan() {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        } catch (_: Exception) {}
        activeAdhanId = null
        isPlaying = false
    }

    val pageTitle = when (currentLang) {
        "hi" -> "पवित्र अज़ान"
        "ur" -> "مقدس اذان"
        "ar" -> "الأذان الشريف"
        "bn" -> "পবিত্র আযান"
        "tr" -> "Kutsal Ezan"
        "id", "ms" -> "Azan Suci"
        "fa" -> "اذان مقدس"
        "ta" -> "புனித பாங்கு"
        "te" -> "పవిత్ర అజాన్"
        "ml" -> "വിശുദ്ധ ബാങ്ക്"
        "gu" -> "પવિત્ર અઝાન"
        "mr" -> "पवित्र अझान"
        "pa" -> "ਪਵਿੱਤਰ ਅਜ਼ਾਨ"
        "fr" -> "Adhan Sacré"
        "ru" -> "Священный Азан"
        else -> "Sacred Adhan"
    }

    val subTitle = when (currentLang) {
        "hi" -> "प्रामाणिक और भावपूर्ण अज़ान संग्रह"
        "ur" -> "مستند اور پرتاثیر اذانوں کا مجموعہ"
        "ar" -> "مجموعة الأذان الشريف المختارة"
        "bn" -> "প্রামাণ্য এবং হৃদয়স্পর্শী আযান সংকলন"
        "tr" -> "Özgün ve Duygusal Ezan Koleksiyonu"
        "id", "ms" -> "Koleksi Azan Autentik dan Menyentuh Hati"
        "fa" -> "مجموعه اذان‌های اصیل و دلنشین"
        "ta" -> "அங்கீகரிக்கப்பட்ட மனதைத் தொடும் அதான் தொகுப்பு"
        "te" -> "ప్రామాణిక మరియు హృదయపూర్వక అజాన్ సేకరణ"
        "ml" -> "ആധികാരികവും ഹൃദയസ്പർശിയുമായ ബാങ്ക് ശേഖരം"
        "gu" -> "પ્રામાણિક અને ભાવપૂર્ણ અઝાન સંગ્રહ"
        "mr" -> "प्रामाणिक आणि भावपूर्ण अझान संग्रह"
        "pa" -> "ਪ੍ਰਮਾਣਿਕ ਅਤੇ ਦਿਲਕਸ਼ ਅਜ਼ਾਨ ਸੰਗ੍ਰਹਿ"
        "fr" -> "Enregistrements et récitants de l'Adhan"
        "ru" -> "Коллекция священных азанов и чтецов"
        else -> "Sacred Adhan Recordings & Reciters"
    }

    val screenBg = if (isDarkTheme) DarkBackground else LightBackground
    val headerTitleColor = if (isDarkTheme) PureGold else Color(0xFF14241E)
    val headerSubColor = if (isDarkTheme) DarkTextMuted else LightTextMuted
    val backIconTint = if (isDarkTheme) PureGold else PrimaryEmerald
    val backBtnBg = if (isDarkTheme) DarkCard else Color(0xFFEAF5EE)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(screenBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // ── Top Bar Header (Centered Heading) ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: Emerald Back Button
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(backBtnBg)
                        .border(1.2.dp, if (isDarkTheme) DarkBorder else PrimaryEmerald, RoundedCornerShape(10.dp))
                        .clickable {
                            stopAdhan()
                            onBack()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = backIconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Center: Heading & Subtitle
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = pageTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = headerTitleColor,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subTitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = headerSubColor,
                        fontSize = 11.5.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }

                // Right: Day / Night Theme Toggle Button
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isDarkTheme) Color(0xFF13271F) else Color(0xFFFFFBEB))
                        .border(1.dp, Color(0xFFF59E0B).copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                        .clickable {
                            isDarkTheme = !isDarkTheme
                            PrayerRepository.setDarkTheme(isDarkTheme)
                            AppToast.show(if (isDarkTheme) "Night Theme 🌙" else "Day Theme ☀️")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = if (isDarkTheme) "🌙" else "☀️", fontSize = 16.sp)
                }
            }

            // ── Language Bar (English, हिंदी, اردو, العربية, বাংলা, 🌐 +20) ──
            LanguageBar(
                languages = visibleLanguages,
                selectedCode = currentLang,
                isDarkTheme = isDarkTheme,
                onSelectLanguage = { code ->
                    currentLang = code
                    PrayerRepository.saveLanguage(code)
                },
                onOpenMoreLanguages = {
                    isMoreLangOpen = true
                }
            )

            Spacer(modifier = Modifier.height(4.dp))

            // ── List of 7 Sacred Adhan Cards ──
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 6.dp,
                    bottom = if (activeItem != null) 100.dp else 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(SACRED_ADHANS_LIST) { index, item ->
                    val isCurrent = activeAdhanId == item.id
                    val isThisPlaying = isCurrent && isPlaying
                    val isThisActiveSound = activeSoundMode == PrayerSoundManager.MODE_ADHAN && activeSoundRaw == item.rawResName

                    AdhanCardItem(
                        item = item,
                        index = index + 1,
                        isCurrent = isCurrent,
                        isPlaying = isThisPlaying,
                        isNotificationSoundActive = isThisActiveSound,
                        currentLang = currentLang,
                        isDarkTheme = isDarkTheme,
                        onPlayClick = { playOrToggleAdhan(item) },
                        onSetNotificationSound = {
                            PrayerSoundManager.setSelectedAdhanRaw(context, item.rawResName)
                            activeSoundRaw = item.rawResName
                            activeSoundMode = PrayerSoundManager.MODE_ADHAN
                            AppToast.show(
                                when {
                                    currentLang.startsWith("hi") -> "🕌 ${item.getTitle(currentLang)} को नमाज़ साउंड सेट किया गया!"
                                    currentLang.startsWith("ur") -> "🕌 ${item.getTitle(currentLang)} کو نماز ساؤنڈ سیٹ کر دیا گیا!"
                                    currentLang.startsWith("hinglish") -> "🕌 ${item.getTitle(currentLang)} ko Namaz sound set kar diya gaya!"
                                    else -> "🕌 ${item.getTitle(currentLang)} set as Salah notification sound!"
                                }
                            )
                        }
                    )
                }
            }
        }

        // ── Floating Bottom Mini-Player Bar ──
        AnimatedVisibility(
            visible = activeItem != null,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            activeItem?.let { item ->
                val isMiniActiveSound = activeSoundMode == PrayerSoundManager.MODE_ADHAN && activeSoundRaw == item.rawResName
                AdhanFloatingMiniPlayer(
                    item = item,
                    isPlaying = isPlaying,
                    isNotificationSoundActive = isMiniActiveSound,
                    currentLang = currentLang,
                    isDarkTheme = isDarkTheme,
                    onSetNotificationSound = {
                        PrayerSoundManager.setSelectedAdhanRaw(context, item.rawResName)
                        activeSoundRaw = item.rawResName
                        activeSoundMode = PrayerSoundManager.MODE_ADHAN
                        AppToast.show(
                            when {
                                currentLang.startsWith("hi") -> "🕌 ${item.getTitle(currentLang)} को नमाज़ साउंड सेट किया गया!"
                                currentLang.startsWith("ur") -> "🕌 ${item.getTitle(currentLang)} کو نماز ساؤنڈ سیٹ کر دیا گیا!"
                                else -> "🕌 ${item.getTitle(currentLang)} set as Salah sound!"
                            }
                        )
                    },
                    onTogglePlay = { playOrToggleAdhan(item) },
                    onClose = { stopAdhan() }
                )
            }
        }

        // ── Full 28+ Languages BottomSheet Modal (🌐 +20) ──
        if (isMoreLangOpen) {
            MoreLanguagesModal(
                selectedCode = currentLang,
                onSelectLanguage = { code ->
                    currentLang = code
                    PrayerRepository.saveLanguage(code)
                    isMoreLangOpen = false
                },
                onDismiss = {
                    isMoreLangOpen = false
                }
            )
        }
    }
}

/**
 * Adhan Card matching .qa-adhan-card from index.html with Full Adaptive Day/Night & Multi-Language Support
 */
@Composable
fun AdhanCardItem(
    item: SacredAdhanItem,
    index: Int,
    isCurrent: Boolean,
    isPlaying: Boolean,
    isNotificationSoundActive: Boolean,
    currentLang: String,
    isDarkTheme: Boolean,
    onPlayClick: () -> Unit,
    onSetNotificationSound: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "PulseTransition")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isPlaying) 1.08f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseScale"
    )

    val cardBorder = if (isCurrent) {
        BorderStroke(1.5.dp, PureGold)
    } else {
        BorderStroke(1.dp, if (isDarkTheme) DarkBorder else LightBorder)
    }

    val cardBg = if (isDarkTheme) {
        if (isCurrent) {
            Brush.horizontalGradient(listOf(MidnightEmerald.copy(alpha = 0.5f), DarkCard))
        } else {
            Brush.horizontalGradient(listOf(DarkCard, DarkSurface))
        }
    } else {
        if (isCurrent) {
            Brush.horizontalGradient(listOf(Color(0xFFF0FDF4), Color(0xFFE8F5EE)))
        } else {
            Brush.horizontalGradient(listOf(Color(0xFFFFFFFF), Color(0xFFFAFAFA)))
        }
    }

    val titleColor = if (isDarkTheme) {
        if (isCurrent) PureGold else Color.White
    } else {
        if (isCurrent) PrimaryEmerald else Color(0xFF14241E)
    }

    val styleColor = if (isDarkTheme) DarkTextMuted else LightTextMuted
    val durationColor = if (isDarkTheme) VividEmerald else PrimaryEmerald

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .shadow(if (isCurrent) 6.dp else 2.dp, RoundedCornerShape(20.dp))
            .clickable { onPlayClick() },
        shape = RoundedCornerShape(20.dp),
        border = cardBorder,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .background(cardBg)
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ── Left: Rosette Medallion (1..7) ──
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(
                        if (isDarkTheme) {
                            Brush.linearGradient(listOf(PrimaryEmerald, MidnightEmerald))
                        } else {
                            if (isCurrent) {
                                Brush.linearGradient(listOf(PrimaryEmerald, MidnightEmerald))
                            } else {
                                Brush.linearGradient(listOf(Color(0xFFEAF5EE), Color(0xFFD6EAE0)))
                            }
                        }
                    )
                    .border(
                        1.5.dp,
                        if (isCurrent) PureGold else if (isDarkTheme) PureGold.copy(alpha = 0.5f) else Color(0x660E5D47),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isPlaying) {
                    RosetteEqualizerWave(barColor = if (isDarkTheme || isCurrent) ChampagneGold else PrimaryEmerald)
                } else {
                    Text(
                        text = "$index",
                        color = if (isDarkTheme || isCurrent) ChampagneGold else PrimaryEmerald,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // ── Middle: Info, Country Flag & Style ──
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.getTitle(currentLang),
                    style = MaterialTheme.typography.titleSmall,
                    color = titleColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.5.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Reciter Name & Country with Flag
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Country badge with flag
                    Box(
                        modifier = Modifier
                            .background(
                                if (isDarkTheme) PrimaryEmerald.copy(alpha = 0.35f) else Color(0xFFEAF5EE),
                                RoundedCornerShape(6.dp)
                            )
                            .border(
                                0.8.dp,
                                if (isDarkTheme) VividEmerald.copy(alpha = 0.5f) else Color(0xFF86EFAC),
                                RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = item.getCountryWithFlag(currentLang),
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDarkTheme) ChampagneGold else PrimaryEmerald
                        )
                    }

                    Text(
                        text = "•",
                        color = styleColor,
                        fontSize = 11.sp
                    )

                    // Duration
                    Text(
                        text = item.duration,
                        fontSize = 11.5.sp,
                        color = durationColor,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Melody Style
                Text(
                    text = item.getStyle(currentLang),
                    fontSize = 11.sp,
                    color = styleColor,
                    lineHeight = 15.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            // ── Right: Action Buttons (Set Sound & Play) ──
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val setBtnBrush = if (isNotificationSoundActive) {
                    Brush.horizontalGradient(listOf(PureGold, Color(0xFFC59B27)))
                } else {
                    val bgCol = if (isDarkTheme) Color(0xFF0F3124) else Color(0xFFEAF5EE)
                    Brush.horizontalGradient(listOf(bgCol, bgCol))
                }

                // Set as Notification Sound Button (Baaju me)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(setBtnBrush)
                        .border(
                            1.dp,
                            if (isNotificationSoundActive) ChampagneGold else if (isDarkTheme) PureGold.copy(alpha = 0.5f) else Color(0xFF86EFAC),
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { onSetNotificationSound() }
                        .padding(horizontal = 8.dp, vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (isNotificationSoundActive) Icons.Filled.CheckCircle else Icons.Filled.NotificationsActive,
                            contentDescription = null,
                            tint = if (isNotificationSoundActive) MidnightEmerald else if (isDarkTheme) ChampagneGold else PrimaryEmerald,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = if (isNotificationSoundActive) {
                                when (currentLang) {
                                    "hi" -> "सक्रिय"
                                    "ur" -> "سیٹ ہے"
                                    "ar" -> "مفعل"
                                    "hinglish" -> "Active"
                                    else -> "Active"
                                }
                            } else {
                                when (currentLang) {
                                    "hi" -> "सेट साउंड"
                                    "ur" -> "سیٹ کریں"
                                    "ar" -> "تعيين"
                                    "hinglish" -> "Set Sound"
                                    else -> "Set Sound"
                                }
                            },
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isNotificationSoundActive) MidnightEmerald else if (isDarkTheme) ChampagneGold else PrimaryEmerald
                        )
                    }
                }

                // Circular Play/Pause Action Button
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .scale(if (isPlaying) pulseScale else 1f)
                        .shadow(if (isCurrent) 8.dp else 2.dp, CircleShape)
                        .clip(CircleShape)
                        .background(
                            if (isPlaying) {
                                Brush.linearGradient(listOf(PureGold, Color(0xFF9A7B2C)))
                            } else {
                                Brush.linearGradient(listOf(PrimaryEmerald, MidnightEmerald))
                            }
                        )
                        .border(1.5.dp, PureGold, CircleShape)
                        .clickable { onPlayClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = if (isPlaying) Color.White else ChampagneGold,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

/**
 * Floating Bottom Mini-Player Bar with Full Adaptive Day/Night & Multi-Language Support
 */
@Composable
fun AdhanFloatingMiniPlayer(
    item: SacredAdhanItem,
    isPlaying: Boolean,
    isNotificationSoundActive: Boolean = false,
    currentLang: String,
    isDarkTheme: Boolean,
    onSetNotificationSound: () -> Unit = {},
    onTogglePlay: () -> Unit,
    onClose: () -> Unit
) {
    val miniPlayerBg = if (isDarkTheme) {
        Brush.horizontalGradient(
            listOf(
                MidnightEmerald.copy(alpha = 0.85f),
                DarkCard.copy(alpha = 0.95f)
            )
        )
    } else {
        Brush.horizontalGradient(
            listOf(
                Color(0xFFFFFFFF),
                Color(0xFFF7FBF8)
            )
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, RoundedCornerShape(22.dp)),
        shape = RoundedCornerShape(22.dp),
        color = if (isDarkTheme) DarkSurface else LightSurface,
        border = BorderStroke(1.5.dp, PureGold)
    ) {
        Row(
            modifier = Modifier
                .background(miniPlayerBg)
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(PrimaryEmerald)
                    .border(1.dp, ChampagneGold, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.GraphicEq,
                    contentDescription = null,
                    tint = ChampagneGold,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Adhan Title and Subtitle
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.getTitle(currentLang),
                    color = if (isDarkTheme) Color.White else Color(0xFF14241E),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.5.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${item.getMuadhin(currentLang)} • ${item.getCountryWithFlag(currentLang)}",
                    color = if (isDarkTheme) ChampagneGold else PrimaryEmerald,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Quick Set Sound in Mini Player
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (isNotificationSoundActive) PureGold else if (isDarkTheme) Color(0xFF0F3124) else Color(0xFFEAF5EE)
                    )
                    .border(
                        1.dp,
                        if (isNotificationSoundActive) ChampagneGold else if (isDarkTheme) PureGold.copy(alpha = 0.5f) else Color(0xFF86EFAC),
                        RoundedCornerShape(10.dp)
                    )
                    .clickable { onSetNotificationSound() }
                    .padding(horizontal = 7.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    Icon(
                        imageVector = if (isNotificationSoundActive) Icons.Filled.CheckCircle else Icons.Filled.NotificationsActive,
                        contentDescription = null,
                        tint = if (isNotificationSoundActive) MidnightEmerald else if (isDarkTheme) ChampagneGold else PrimaryEmerald,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = if (isNotificationSoundActive) "Active" else "Set Sound",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isNotificationSoundActive) MidnightEmerald else if (isDarkTheme) ChampagneGold else PrimaryEmerald
                    )
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Play / Pause toggle
            IconButton(
                onClick = onTogglePlay,
                modifier = Modifier
                    .size(38.dp)
                    .background(PureGold, CircleShape)
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    tint = if (isDarkTheme) DarkBackground else Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Stop / Close button
            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .size(34.dp)
                    .background(
                        if (isDarkTheme) Color.White.copy(alpha = 0.1f) else Color(0xFFE2E8F0),
                        CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Stop",
                    tint = if (isDarkTheme) Color.White.copy(alpha = 0.8f) else Color(0xFF475569),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
