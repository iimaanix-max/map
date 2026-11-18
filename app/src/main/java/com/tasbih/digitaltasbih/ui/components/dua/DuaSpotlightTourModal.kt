package com.tasbih.digitaltasbih.ui.components.dua

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Rect
import com.tasbih.digitaltasbih.data.DuaTourLocalization
import com.tasbih.digitaltasbih.ui.components.GeneralSpotlightTourModal

/**
 * Luxury Spotlight Tour for Dua Screen.
 * Delegates to the ultra-smooth master GeneralSpotlightTourModal engine.
 */
@Composable
fun DuaSpotlightTourModal(
    step: Int, // 1 to 5
    totalSteps: Int = 5,
    lang: String = "hi",
    targetRect: Rect? = null,
    onNext: () -> Unit,
    onDone: () -> Unit,
    onSkip: () -> Unit
) {
    val strings = remember(lang) { DuaTourLocalization.get(lang) }

    val l = lang.lowercase().split("-")[0]
    val stepLabel = if (totalSteps == 1) {
        when (l) {
            "hi" -> "क्विक टिप"
            "ur" -> "کوئیک گائیڈ"
            "ar" -> "إرشاد سريع"
            "bn" -> "কুইক গাইড"
            else -> "Quick Tip"
        }
    } else {
        strings.step
            .replace("{curr}", step.toString())
            .replace("{total}", totalSteps.toString())
    }

    val title = if (totalSteps == 1) {
        when (l) {
            "hi" -> "📂 पसंदीदा दुआएं (My Lists & Bookmarks)"
            "ur" -> "📂 پسندیدہ دعائیں (My Lists)"
            "ar" -> "📂 أدعيتي وقوائمي المفضلة"
            "bn" -> "📂 প্রিয় দোয়া ও তালিকা"
            else -> "📂 My Lists & Bookmarks"
        }
    } else {
        when (step) {
            1 -> strings.quickTitle
            2 -> strings.feelTitle
            3 -> strings.catsTitle
            4 -> strings.myduaTitle
            5 -> strings.mylistsTitle
            else -> ""
        }
    }

    val description = if (totalSteps == 1) {
        when (l) {
            "hi" -> "यहाँ से अपनी पसंदीदा दुआ सेव या बुकमार्क करें ताकि कभी भी आसानी से पढ़ सकें।"
            "ur" -> "یہاں سے اپنی پسندیدہ دعا محفوظ (Save) یا بُک مارک کریں تاکہ کبھی بھی باآسانی پڑھ سکیں۔"
            "ar" -> "احفظ أدعيتك المفضلة هنا للوصول إليها وقراءتها في أي وقت بكل سهولة."
            "bn" -> "এখান থেকে আপনার প্রিয় দোয়া সেভ বা বুকমার্ক করুন যাতে যেকোনো সময় সহজে পড়তে পারেন।"
            else -> "Save or bookmark your favorite Duas here for quick access anytime."
        }
    } else {
        when (step) {
            1 -> strings.quickDesc
            2 -> strings.feelDesc
            3 -> strings.catsDesc
            4 -> strings.myduaDesc
            5 -> strings.mylistsDesc
            else -> ""
        }
    }

    val cornerRadius = if (totalSteps == 1) 14f else if (step in 4..5) 14f else 18f
    val pad = if (totalSteps == 1) 6f else if (step in 4..5) 8f else 6f

    GeneralSpotlightTourModal(
        step = step,
        totalSteps = totalSteps,
        stepLabel = stepLabel,
        title = title,
        description = description,
        nextBtnText = strings.next,
        doneBtnText = strings.done,
        skipBtnText = strings.skip,
        targetRect = targetRect,
        cornerRadiusDp = cornerRadius,
        padDp = pad,
        onNext = onNext,
        onDone = onDone,
        onSkip = onSkip
    )
}
