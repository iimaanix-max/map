package com.tasbih.digitaltasbih.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Rect
import com.tasbih.digitaltasbih.data.PrayerTimesTourLocalization

/**
 * Luxury Spotlight Tour for Prayer Times Screen.
 * Delegates to the ultra-smooth master GeneralSpotlightTourModal engine.
 */
@Composable
fun PrayerSpotlightTourModal(
    step: Int, // 1 to 5
    totalSteps: Int = 5,
    lang: String = "hi",
    targetRect: Rect? = null,
    onNext: () -> Unit,
    onDone: () -> Unit,
    onSkip: () -> Unit
) {
    val tourStrings = remember(lang) { PrayerTimesTourLocalization.get(lang) }

    val (title, description) = when (step) {
        1 -> tourStrings.heroTitle to tourStrings.heroDesc
        2 -> tourStrings.timingsTitle to tourStrings.timingsDesc
        3 -> tourStrings.jamatTitle to tourStrings.jamatDesc
        4 -> tourStrings.trackerTitle to tourStrings.trackerDesc
        5 -> tourStrings.settingsTitle to tourStrings.settingsDesc
        else -> tourStrings.heroTitle to tourStrings.heroDesc
    }

    val stepLabel = tourStrings.step.replace("{curr}", step.toString()).replace("{total}", totalSteps.toString())
    val cornerRadius = when (step) {
        3 -> 12f
        5 -> 22f
        else -> 18f
    }

    GeneralSpotlightTourModal(
        step = step,
        totalSteps = totalSteps,
        stepLabel = stepLabel,
        title = title,
        description = description,
        nextBtnText = tourStrings.next,
        doneBtnText = tourStrings.done,
        skipBtnText = tourStrings.skip,
        targetRect = targetRect,
        cornerRadiusDp = cornerRadius,
        padDp = 6f,
        onNext = onNext,
        onDone = onDone,
        onSkip = onSkip
    )
}
