package com.tasbih.digitaltasbih.ui.screens.groups

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Rect
import com.tasbih.digitaltasbih.ui.components.GeneralSpotlightTourModal

/**
 * Luxury Spotlight Tour Modal for Live Dhikr Screen.
 * Delegates to the ultra-smooth master GeneralSpotlightTourModal engine.
 */
@Composable
fun LiveDhikrSpotlightTourModal(
    step: Int,
    totalSteps: Int,
    targetRect: Rect?,
    stepLabel: String,
    title: String,
    description: String,
    skipBtnText: String,
    nextBtnText: String,
    doneBtnText: String,
    onNext: () -> Unit,
    onDone: () -> Unit,
    onSkip: () -> Unit
) {
    GeneralSpotlightTourModal(
        step = step,
        totalSteps = totalSteps,
        stepLabel = stepLabel,
        title = title,
        description = description,
        nextBtnText = nextBtnText,
        doneBtnText = doneBtnText,
        skipBtnText = skipBtnText,
        targetRect = targetRect,
        cornerRadiusDp = 18f,
        padDp = 6f,
        onNext = onNext,
        onDone = onDone,
        onSkip = onSkip
    )
}
