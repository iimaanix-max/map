package com.tasbih.digitaltasbih.ui.screens.mosque

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import kotlin.math.max

/**
 * Creates custom high-definition vector/canvas markers for Mosques & Halal Food places.
 * - Green Mosque Icon + Gold border + Mosque Name badge
 * - Red Halal Food Icon + Gold border + Restaurant Name badge
 * - Fully compatible with both Google Maps SDK (BitmapDescriptor) and OSMDroid (Drawable)
 */
object CustomMarkerHelper {

    private val cache = mutableMapOf<String, Bitmap>()

    fun getMosqueMarkerBitmap(
        context: Context,
        name: String,
        distanceStr: String,
        isSelected: Boolean = false,
        isHalal: Boolean = false
    ): Bitmap {
        val cleanName = if (name.length > 20) name.take(18) + "…" else name
        val cacheKey = "${cleanName}_${distanceStr}_${isSelected}_${isHalal}"
        cache[cacheKey]?.let { return it }

        val density = context.resources.displayMetrics.density

        // Paint setup
        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = if (isSelected) Color.parseColor("#0D4A3E") else Color.parseColor("#1F2937")
            textSize = 11.5f * density
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val subtextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#8C6B1F")
            textSize = 9.5f * density
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }

        val nameWidth = textPaint.measureText(cleanName)
        val distWidth = if (distanceStr.isNotBlank()) subtextPaint.measureText(" · $distanceStr") else 0f
        val badgeContentWidth = nameWidth + distWidth

        val pinRadius = 18f * density
        val badgeHeight = 22f * density
        val badgePaddingHorizontal = 8f * density
        val badgeWidth = max(pinRadius * 2, badgeContentWidth + badgePaddingHorizontal * 2)

        val pointerHeight = 8f * density
        val totalWidth = (badgeWidth + 12f * density).toInt()
        val totalHeight = (pinRadius * 2 + pointerHeight + badgeHeight + 10f * density).toInt()

        val bitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val centerX = totalWidth / 2f
        val pinCenterY = pinRadius + 4f * density

        // 1. Draw Drop Shadow for Pin
        val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#25000000")
            maskFilter = BlurMaskFilter(4f * density, BlurMaskFilter.Blur.NORMAL)
        }
        canvas.drawCircle(centerX, pinCenterY + 2f * density, pinRadius + 1f * density, shadowPaint)

        // 2. Draw Pin Pointer (Triangle at bottom of circle)
        val pointerPath = Path().apply {
            moveTo(centerX - 7f * density, pinCenterY + pinRadius - 2f * density)
            lineTo(centerX + 7f * density, pinCenterY + pinRadius - 2f * density)
            lineTo(centerX, pinCenterY + pinRadius + pointerHeight)
            close()
        }

        val pinColor = when {
            isSelected -> Color.parseColor("#C9A84C") // Radiant Gold
            isHalal -> Color.parseColor("#DC2626")    // Halal Crimson
            else -> Color.parseColor("#0D4A3E")       // Islamic Deep Emerald
        }

        val pinPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = pinColor
            style = Paint.Style.FILL
        }
        canvas.drawPath(pointerPath, pinPaint)
        canvas.drawCircle(centerX, pinCenterY, pinRadius, pinPaint)

        // Pin Gold Border Stroke
        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = if (isSelected) Color.WHITE else Color.parseColor("#F3D48A")
            style = Paint.Style.STROKE
            strokeWidth = 2.2f * density
        }
        canvas.drawCircle(centerX, pinCenterY, pinRadius, borderPaint)

        // 3. Draw Icon (Emoji 🕌 for Mosque, 🍽️ for Halal)
        val iconPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 17f * density
            textAlign = Paint.Align.CENTER
        }
        val iconText = if (isHalal) "🍽️" else "🕌"
        val iconY = pinCenterY - ((iconPaint.descent() + iconPaint.ascent()) / 2)
        canvas.drawText(iconText, centerX, iconY, iconPaint)

        // 4. Draw Label Badge below the pin
        val badgeTop = pinCenterY + pinRadius + pointerHeight + 2f * density
        val badgeBottom = badgeTop + badgeHeight
        val badgeLeft = centerX - (badgeWidth / 2f)
        val badgeRight = centerX + (badgeWidth / 2f)
        val badgeRect = RectF(badgeLeft, badgeTop, badgeRight, badgeBottom)

        // Badge Shadow
        canvas.drawRoundRect(badgeRect, 7f * density, 7f * density, shadowPaint)

        // Badge Background (White or Warm Cream)
        val badgeBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = if (isSelected) Color.parseColor("#FFFDF5") else Color.WHITE
            style = Paint.Style.FILL
        }
        canvas.drawRoundRect(badgeRect, 7f * density, 7f * density, badgeBgPaint)

        // Badge Border
        val badgeBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = if (isSelected) Color.parseColor("#C9A84C") else Color.parseColor("#D4AF37").let {
                Color.argb(120, Color.red(it), Color.green(it), Color.blue(it))
            }
            style = Paint.Style.STROKE
            strokeWidth = 1.2f * density
        }
        canvas.drawRoundRect(badgeRect, 7f * density, 7f * density, badgeBorderPaint)

        // 5. Draw Label Text inside Badge
        val textY = badgeTop + (badgeHeight / 2f) - ((textPaint.descent() + textPaint.ascent()) / 2)
        var currentX = badgeLeft + badgePaddingHorizontal
        canvas.drawText(cleanName, currentX, textY, textPaint)
        currentX += nameWidth

        if (distanceStr.isNotBlank()) {
            canvas.drawText(" · $distanceStr", currentX, textY, subtextPaint)
        }

        cache[cacheKey] = bitmap
        return bitmap
    }

    fun getGoogleMapMarkerDescriptor(
        context: Context,
        name: String,
        distanceStr: String,
        isSelected: Boolean = false,
        isHalal: Boolean = false
    ): BitmapDescriptor {
        val bmp = getMosqueMarkerBitmap(context, name, distanceStr, isSelected, isHalal)
        return BitmapDescriptorFactory.fromBitmap(bmp)
    }

    fun getOsmMarkerDrawable(
        context: Context,
        name: String,
        distanceStr: String,
        isSelected: Boolean = false,
        isHalal: Boolean = false
    ): Drawable {
        val bmp = getMosqueMarkerBitmap(context, name, distanceStr, isSelected, isHalal)
        return BitmapDrawable(context.resources, bmp)
    }

    fun getUserLocationBitmap(context: Context): Bitmap {
        val density = context.resources.displayMetrics.density
        val size = (32 * density).toInt()
        val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        val center = size / 2f

        // Pulsing / Outer Aura
        val auraPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#333B82F6")
        }
        canvas.drawCircle(center, center, center, auraPaint)

        // White border
        val whitePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
        }
        canvas.drawCircle(center, center, 10f * density, whitePaint)

        // Blue Core
        val bluePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#2563EB")
        }
        canvas.drawCircle(center, center, 8f * density, bluePaint)

        return bmp
    }
}
