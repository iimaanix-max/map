package com.tasbih.digitaltasbih.ui.screens.quran

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * 12 Authentic Quran Themes inspired by real physical Mushafs,
 * including the Madani Medina Mushaf, Indo-Pak Color Tajweed Quran with side stripes,
 * and high-contrast night reading editions.
 */
data class MushafTheme(
    val id: String,
    val nameEnglish: String,
    val nameHindi: String,
    val nameArabic: String,
    val pageColor: Color,
    val outerBorderColor: Color,
    val innerBorderColor: Color,
    val borderAccentColor: Color,
    val arabicTextColor: Color,
    val activeWordColor: Color,
    val ayahMarkerColor: Color,
    val surahHeaderBrush: Brush,
    val surahHeaderTextColor: Color,
    val surahHeaderBorderColor: Color,
    val bismillahColor: Color,
    val marginStripeColors: List<Color> = emptyList(),
    val hasSideStripes: Boolean = false,
    val isDark: Boolean = false
) {
    companion object {
        val allThemes = listOf(
            // 1. Classic Madani Cream (Original Medina Mushaf)
            MushafTheme(
                id = "madani_cream",
                nameEnglish = "Classic Madani Cream",
                nameHindi = "क्लासिक मदीना मुसहफ़",
                nameArabic = "المصحف المدني الأصلي",
                pageColor = Color(0xFFFAF6EC),
                outerBorderColor = Color(0xFF0E3D60),
                innerBorderColor = Color(0xFFD4AF37),
                borderAccentColor = Color(0xFFC59B27),
                arabicTextColor = Color(0xFF141A16),
                activeWordColor = Color(0xFFD97706),
                ayahMarkerColor = Color(0xFFB45309),
                surahHeaderBrush = Brush.linearGradient(listOf(Color(0xFFE8D39E), Color(0xFFCBA558))),
                surahHeaderTextColor = Color(0xFF1E2621),
                surahHeaderBorderColor = Color(0xFF8A6D29),
                bismillahColor = Color(0xFF1C2B22)
            ),

            // 2. Indo-Pak Color Tajweed (Exact match to South Asian physical Quran with pastel margin stripes)
            MushafTheme(
                id = "indopak_tajweed",
                nameEnglish = "Indo-Pak Color Tajweed",
                nameHindi = "इण्डो-पाक रंगीन तजवीद",
                nameArabic = "مصحف التجويد الملون",
                pageColor = Color(0xFFFFFBF2),
                hasSideStripes = true,
                marginStripeColors = listOf(
                    Color(0xFFFFD1BA), // Warm peach stripe
                    Color(0xFFC6EBD5), // Mint pastel green stripe
                    Color(0xFFFFCCD5), // Soft pastel pink stripe
                    Color(0xFFFDE68A)  // Delicate gold stripe
                ),
                outerBorderColor = Color(0xFF8B5A2B),
                innerBorderColor = Color(0xFF0A6847),
                borderAccentColor = Color(0xFFD97706),
                arabicTextColor = Color(0xFF0F172A),
                activeWordColor = Color(0xFFD97706),
                ayahMarkerColor = Color(0xFF0284C7),
                surahHeaderBrush = Brush.linearGradient(listOf(Color(0xFFFFF3CD), Color(0xFFFFE082), Color(0xFFFFCC80))),
                surahHeaderTextColor = Color(0xFF78350F),
                surahHeaderBorderColor = Color(0xFFB45309),
                bismillahColor = Color(0xFF0A4428)
            ),

            // 3. Royal Emerald & Gold
            MushafTheme(
                id = "royal_emerald",
                nameEnglish = "Royal Emerald & Gold",
                nameHindi = "शाही ज़मर्रुद व सोना",
                nameArabic = "الزمردي الملكي المذهب",
                pageColor = Color(0xFFF9FAF7),
                outerBorderColor = Color(0xFF06331C),
                innerBorderColor = Color(0xFFE5B842),
                borderAccentColor = Color(0xFF136A3E),
                arabicTextColor = Color(0xFF092817),
                activeWordColor = Color(0xFFF59E0B),
                ayahMarkerColor = Color(0xFF0E5434),
                surahHeaderBrush = Brush.linearGradient(listOf(Color(0xFF06331C), Color(0xFF0F5834))),
                surahHeaderTextColor = Color(0xFFFFF7D6),
                surahHeaderBorderColor = Color(0xFFD4AF37),
                bismillahColor = Color(0xFF06331C)
            ),

            // 4. Vintage Sepia Papyrus (Antique manuscript)
            MushafTheme(
                id = "vintage_sepia",
                nameEnglish = "Vintage Sepia Papyrus",
                nameHindi = "क़दीम मख़तूतह (एंटीक)",
                nameArabic = "المخطوطة الأثرية العتيقة",
                pageColor = Color(0xFFF3E7CF),
                outerBorderColor = Color(0xFF6B4423),
                innerBorderColor = Color(0xFFA67C52),
                borderAccentColor = Color(0xFF8C5D35),
                arabicTextColor = Color(0xFF3B2514),
                activeWordColor = Color(0xFFB45309),
                ayahMarkerColor = Color(0xFF8C532B),
                surahHeaderBrush = Brush.linearGradient(listOf(Color(0xFFE4D0AB), Color(0xFFCDB082))),
                surahHeaderTextColor = Color(0xFF331D0E),
                surahHeaderBorderColor = Color(0xFF7A4E2B),
                bismillahColor = Color(0xFF452A15)
            ),

            // 5. Rainbow Tajweed Pastel (5 pastel luxury stripes)
            MushafTheme(
                id = "rainbow_pastel",
                nameEnglish = "Rainbow Tajweed Pastel",
                nameHindi = "इंद्रधनुष तजवीद संस्करण",
                nameArabic = "طبعة قوس قزح التجويدية",
                pageColor = Color(0xFFFFFEFA),
                hasSideStripes = true,
                marginStripeColors = listOf(
                    Color(0xFFFECDD3), // Rose pastel
                    Color(0xFFFED7AA), // Peach pastel
                    Color(0xFFFEF08A), // Soft gold
                    Color(0xFFBBF7D0), // Pale green
                    Color(0xFFBAE6FD)  // Sky blue
                ),
                outerBorderColor = Color(0xFF9333EA),
                innerBorderColor = Color(0xFFF59E0B),
                borderAccentColor = Color(0xFF0D9488),
                arabicTextColor = Color(0xFF1E293B),
                activeWordColor = Color(0xFFEA580C),
                ayahMarkerColor = Color(0xFF7C3AED),
                surahHeaderBrush = Brush.linearGradient(listOf(Color(0xFFFCE7F3), Color(0xFFEDE9FE), Color(0xFFE0F2FE))),
                surahHeaderTextColor = Color(0xFF4C1D95),
                surahHeaderBorderColor = Color(0xFFA855F7),
                bismillahColor = Color(0xFF0F766E)
            ),

            // 6. Ottoman Royal Blue & Gold
            MushafTheme(
                id = "ottoman_blue",
                nameEnglish = "Ottoman Royal Blue & Gold",
                nameHindi = "उस्मानी सुल्तानी (लाजवर्त)",
                nameArabic = "العثماني الكحلي المذهب",
                pageColor = Color(0xFFFAF5E8),
                outerBorderColor = Color(0xFF0F2E52),
                innerBorderColor = Color(0xFFC59B27),
                borderAccentColor = Color(0xFF1E4C7C),
                arabicTextColor = Color(0xFF0F172A),
                activeWordColor = Color(0xFFD97706),
                ayahMarkerColor = Color(0xFF0F2E52),
                surahHeaderBrush = Brush.linearGradient(listOf(Color(0xFF0F2E52), Color(0xFF1A4778))),
                surahHeaderTextColor = Color(0xFFFDE68A),
                surahHeaderBorderColor = Color(0xFFE5B842),
                bismillahColor = Color(0xFF0F2E52)
            ),

            // 7. Midnight Obsidian & Gold (Dark Night Mode)
            MushafTheme(
                id = "midnight_dark",
                nameEnglish = "Midnight Obsidian & Gold",
                nameHindi = "शब-ए-नूर (डार्क नाइट)",
                nameArabic = "الليل الملكي المذهب",
                pageColor = Color(0xFF111714),
                outerBorderColor = Color(0xFF26332A),
                innerBorderColor = Color(0xFFC59B27),
                borderAccentColor = Color(0xFF1E4630),
                arabicTextColor = Color(0xFFF1F5F3),
                activeWordColor = Color(0xFFFBBF24),
                ayahMarkerColor = Color(0xFFD4AF37),
                surahHeaderBrush = Brush.linearGradient(listOf(Color(0xFF1A261F), Color(0xFF253A2E))),
                surahHeaderTextColor = Color(0xFFFDE68A),
                surahHeaderBorderColor = Color(0xFF997A24),
                bismillahColor = Color(0xFFE2E8F0),
                isDark = true
            ),

            // 8. Warm Amber Bedtime Mode (Zero Blue Light)
            MushafTheme(
                id = "warm_amber",
                nameEnglish = "Warm Amber Night",
                nameHindi = "गर्म अंबर रात (आँखों के लिए शांत)",
                nameArabic = "العنبر الدافئ للقراءة الليلية",
                pageColor = Color(0xFF1C1814),
                outerBorderColor = Color(0xFF382E24),
                innerBorderColor = Color(0xFF996A3D),
                borderAccentColor = Color(0xFF5C4733),
                arabicTextColor = Color(0xFFF3E5D0),
                activeWordColor = Color(0xFFF59E0B),
                ayahMarkerColor = Color(0xFFD97706),
                surahHeaderBrush = Brush.linearGradient(listOf(Color(0xFF2E241B), Color(0xFF453424))),
                surahHeaderTextColor = Color(0xFFFEE685),
                surahHeaderBorderColor = Color(0xFF8C5E33),
                bismillahColor = Color(0xFFECD7BA),
                isDark = true
            ),

            // 9. Rose Floral Luxury (Gul-e-Firdaus)
            MushafTheme(
                id = "rose_floral",
                nameEnglish = "Rose Floral Luxury",
                nameHindi = "गुल-ए-फिरदौस (गुलाबी सोना)",
                nameArabic = "الزهري الفاخر المذهب",
                pageColor = Color(0xFFFFF7F8),
                hasSideStripes = true,
                marginStripeColors = listOf(
                    Color(0xFFFFCCD5),
                    Color(0xFFFFB3C1),
                    Color(0xFFFDE2E4)
                ),
                outerBorderColor = Color(0xFF701A2B),
                innerBorderColor = Color(0xFFD4AF37),
                borderAccentColor = Color(0xFFA8324E),
                arabicTextColor = Color(0xFF2D151B),
                activeWordColor = Color(0xFFE11D48),
                ayahMarkerColor = Color(0xFF9F1239),
                surahHeaderBrush = Brush.linearGradient(listOf(Color(0xFF701A2B), Color(0xFF881337))),
                surahHeaderTextColor = Color(0xFFFFF1F2),
                surahHeaderBorderColor = Color(0xFFE5B842),
                bismillahColor = Color(0xFF4C0519)
            ),

            // 10. Pure Minimalist White
            MushafTheme(
                id = "pure_white",
                nameEnglish = "Pure Minimalist White",
                nameHindi = "सफ़ेद सादगी (क्लीन)",
                nameArabic = "الأبيض الناصع العصري",
                pageColor = Color(0xFFFFFFFF),
                outerBorderColor = Color(0xFFD1D5DB),
                innerBorderColor = Color(0xFF9CA3AF),
                borderAccentColor = Color(0xFFE5E7EB),
                arabicTextColor = Color(0xFF111827),
                activeWordColor = Color(0xFFD97706),
                ayahMarkerColor = Color(0xFF4B5563),
                surahHeaderBrush = Brush.linearGradient(listOf(Color(0xFFF3F4F6), Color(0xFFE5E7EB))),
                surahHeaderTextColor = Color(0xFF111827),
                surahHeaderBorderColor = Color(0xFF9CA3AF),
                bismillahColor = Color(0xFF111827)
            ),

            // 11. Muted Olive
            MushafTheme(
                id = "muted_olive",
                nameEnglish = "Muted Olive",
                nameHindi = "ज़ैतूनी सादगी",
                nameArabic = "الزيتوني الهادئ",
                pageColor = Color(0xFFF8FAF5),
                outerBorderColor = Color(0xFF364835),
                innerBorderColor = Color(0xFF94A587),
                borderAccentColor = Color(0xFF5B6E52),
                arabicTextColor = Color(0xFF1C2B1B),
                activeWordColor = Color(0xFFD97706),
                ayahMarkerColor = Color(0xFF5B6E52),
                surahHeaderBrush = Brush.linearGradient(listOf(Color(0xFF364835), Color(0xFF4A5E44))),
                surahHeaderTextColor = Color(0xFFF1F5E9),
                surahHeaderBorderColor = Color(0xFF94A587),
                bismillahColor = Color(0xFF364835)
            ),

            // 12. Desert Sand
            MushafTheme(
                id = "desert_sand",
                nameEnglish = "Desert Sand",
                nameHindi = "रेगिस्तानी रेत (सहराई)",
                nameArabic = "رمال الصحراء الذهبية",
                pageColor = Color(0xFFFAF3E6),
                outerBorderColor = Color(0xFF8C6D47),
                innerBorderColor = Color(0xFFD4AF37),
                borderAccentColor = Color(0xFFB08C59),
                arabicTextColor = Color(0xFF261D13),
                activeWordColor = Color(0xFFD97706),
                ayahMarkerColor = Color(0xFF8C6D47),
                surahHeaderBrush = Brush.linearGradient(listOf(Color(0xFFF0DFC2), Color(0xFFDECAAA))),
                surahHeaderTextColor = Color(0xFF3B2A18),
                surahHeaderBorderColor = Color(0xFFB08C59),
                bismillahColor = Color(0xFF4A341E)
            )
        )

        fun getById(id: String?): MushafTheme {
            return allThemes.find { it.id == id } ?: allThemes.first()
        }
    }
}

