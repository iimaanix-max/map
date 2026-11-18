package com.tasbih.digitaltasbih.data

/**
 * Data models for Dhikr Tracker
 */
data class DhikrItem(
    val id: String,
    val name: String,
    val meaning: String,
    val arabic: String,
    val icon: String = "✨",
    val defaultTarget: Int = 100
)

data class DhikrCountRecord(
    val item: DhikrItem,
    val count: Int,
    val target: Int = 100
)

data class DhikrQuote(
    val arabic: String,
    val text: String,
    val reference: String,
    val icon: String = "🕌"
)

data class DhikrBenefit(
    val text: String,
    val reference: String,
    val iconType: String = "heart" // "heart", "star", "palm"
)

data class DhikrDaySummary(
    val totalCount: Int,
    val activeTypesCount: Int,
    val streakDays: Int,
    val deltaVsYesterday: Int,
    val records: List<DhikrCountRecord>
)

data class FrequentDhikrItem(
    val id: String,
    val name: String,
    val count: Int,
    val iconType: String // "sun", "moon", "sparkle"
)

data class ChartBarData(
    val label: String,
    val count: Int,
    val isFuture: Boolean,
    val fullLabel: String
)
