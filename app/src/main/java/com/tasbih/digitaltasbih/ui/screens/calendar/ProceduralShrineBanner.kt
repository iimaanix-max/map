package com.tasbih.digitaltasbih.ui.screens.calendar

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasbih.digitaltasbih.data.CalendarEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

/**
 * Renders the luxury event banner card.
 * If an admin image URL/Base64 is provided, renders the image.
 * Otherwise, procedurally renders the authentic Islamic Shrine Dome silhouette (Screenshot 4).
 */
@Composable
fun ProceduralShrineBanner(
    event: CalendarEvent,
    locale: String,
    modifier: Modifier = Modifier
) {
    var remoteBitmap by remember(event.image) { mutableStateOf<android.graphics.Bitmap?>(null) }

    LaunchedEffect(event.image) {
        if (event.image.isNotBlank()) {
            withContext(Dispatchers.IO) {
                try {
                    val imgStr = event.image.trim()
                    val bmp = when {
                        imgStr.startsWith("data:image") -> {
                            val base64Data = imgStr.substringAfter("base64,")
                            val decoded = Base64.decode(base64Data, Base64.DEFAULT)
                            BitmapFactory.decodeByteArray(decoded, 0, decoded.size)
                        }
                        imgStr.startsWith("http://") || imgStr.startsWith("https://") -> {
                            val stream = URL(imgStr).openStream()
                            BitmapFactory.decodeStream(stream)
                        }
                        else -> null
                    }
                    remoteBitmap = bmp
                } catch (_: Exception) {
                    remoteBitmap = null
                }
            }
        } else {
            remoteBitmap = null
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9.5f)
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .border(1.dp, Color(0x33C9A84C), RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
    ) {
        val bmp = remoteBitmap
        if (bmp != null) {
            Image(
                bitmap = bmp.asImageBitmap(),
                contentDescription = event.getTitle(locale),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Procedural Shrine Artwork faithful to original calendarShrineArt SVG (Screenshot 4)
            val toneColor = event.getColorParsed()
            val gradientBrush = remember(toneColor) {
                Brush.linearGradient(
                    colors = listOf(
                        toneColor.copy(alpha = 0.95f),
                        Color(0xFF2C2417),
                        Color(0xFF171C2B)
                    )
                )
            }

            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // 1. Background gradient
                drawRect(brush = gradientBrush)

                // 2. Halo circle behind central dome
                val haloCenter = Offset(w * 0.5f, h * 0.40f)
                val haloRadius = h * 0.28f
                drawCircle(
                    color = Color(0x24FFFFFF),
                    radius = haloRadius,
                    center = haloCenter
                )

                // 3. Central dome silhouette
                val domePath = Path().apply {
                    val left = w * 0.35f
                    val right = w * 0.65f
                    val base = h * 0.72f
                    moveTo(left, base)
                    lineTo(right, base)
                    lineTo(right, base - h * 0.20f)
                    // Arch curve
                    cubicTo(
                        right, base - h * 0.35f,
                        w * 0.58f, base - h * 0.45f,
                        w * 0.5f, base - h * 0.50f
                    )
                    cubicTo(
                        w * 0.42f, base - h * 0.45f,
                        left, base - h * 0.35f,
                        left, base - h * 0.20f
                    )
                    close()
                }
                drawPath(domePath, Color(0x2EFFFFFF))

                // 4. Crescent & Top Spire
                val spirePath = Path().apply {
                    val cx = w * 0.5f
                    val top = h * 0.16f
                    val bot = h * 0.28f
                    moveTo(cx, top)
                    cubicTo(cx + w * 0.035f, top + h * 0.04f, cx + w * 0.035f, bot - h * 0.04f, cx, bot)
                    cubicTo(cx - w * 0.035f, bot - h * 0.04f, cx - w * 0.035f, top + h * 0.04f, cx, top)
                    close()
                }
                drawPath(spirePath, Color(0xEEFFFFFF))

                // 5. Left & Right Minarets
                val minaretW = w * 0.045f
                val minaretH = h * 0.30f
                drawRoundRect(
                    color = Color(0x30FFFFFF),
                    topLeft = Offset(w * 0.28f, h * 0.42f),
                    size = Size(minaretW, minaretH),
                    cornerRadius = CornerRadius(6f, 6f)
                )
                drawRoundRect(
                    color = Color(0x30FFFFFF),
                    topLeft = Offset(w * 0.675f, h * 0.42f),
                    size = Size(minaretW, minaretH),
                    cornerRadius = CornerRadius(6f, 6f)
                )

                // 6. Base pedestal banner
                drawRoundRect(
                    color = Color(0x3BFFFFFF),
                    topLeft = Offset(w * 0.24f, h * 0.70f),
                    size = Size(w * 0.52f, h * 0.05f),
                    cornerRadius = CornerRadius(8f, 8f)
                )

                // 7. Dark lower title overlay bar
                drawRect(
                    color = Color(0x660A0E18),
                    topLeft = Offset(0f, h * 0.72f),
                    size = Size(w, h * 0.28f)
                )
            }

            // Foreground Text overlay (matching Screenshot 4)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.weight(1f))

                // Title: e.g. "Makhdoom Ali Mahimi"
                Text(
                    text = event.getName(locale),
                    color = Color(0xFFFFF7DB),
                    fontSize = 15.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Serif,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Location: e.g. "Mahim, Mumbai, Maharashtra, India"
                val loc = event.getLocation(locale)
                if (loc.isNotBlank()) {
                    Text(
                        text = loc,
                        color = Color(0xCCFFFFFF),
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
