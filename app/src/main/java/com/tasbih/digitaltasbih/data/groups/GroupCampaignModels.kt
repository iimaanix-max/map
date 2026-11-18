package com.tasbih.digitaltasbih.data.groups

import com.google.gson.annotations.SerializedName

/**
 * Data models for Live Dhikr, Collaborative Group Campaigns & Umrah Lucky Draw.
 * Compatible with Cloudflare Worker API & Local Offline Engine.
 */
data class CampaignItem(
    @SerializedName("campaignId") val campaignId: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("arabic") val arabic: String = "",
    @SerializedName("translit") val translit: String = "",
    @SerializedName("meaning") val meaning: String = "",
    @SerializedName("target") val target: Long = 1000L,
    @SerializedName("totalCount") val totalCount: Long = 0L,
    @SerializedName("myContribution") val myContribution: Long = 0L,
    @SerializedName("joinCode") val joinCode: String = "",
    @SerializedName("note") val note: String = "",
    @SerializedName("isCreator") val isCreator: Boolean = false,
    @SerializedName("visibility") val visibility: String = "public",
    @SerializedName("creatorName") val creatorName: String = "",
    @SerializedName("location") val location: String = "",
    @SerializedName("participantsCount") val participantsCount: Int = 1,
    @SerializedName("createdAt") val createdAt: Long = 0L,
    @SerializedName("isSpecialMega") val isSpecialMega: Boolean = false,
    @SerializedName("prizeTitle") val prizeTitle: String = "",
    @SerializedName("regEndDate") val regEndDate: String = "",
    @SerializedName("minDailyDhikr") val minDailyDhikr: Int = 100,
    @SerializedName("contestRules") val contestRules: String = "",
    @SerializedName("badgeRibbon") val badgeRibbon: String = "",
    @SerializedName("completed") val completed: Boolean = false
) {
    val id: String get() = campaignId
    val displayDhikr: String get() = if (arabic.isNotBlank()) arabic else title
    val progressPercent: Float get() = if (target > 0) (totalCount.toFloat() / target).coerceIn(0f, 1f) else 0f
    val remaining: Long get() = (target - totalCount).coerceAtLeast(0L)
}

data class ParticipantItem(
    @SerializedName("deviceId") val deviceId: String = "",
    @SerializedName("displayName") val displayName: String = "",
    @SerializedName("city") val city: String = "",
    @SerializedName("count") val count: Long = 0L,
    @SerializedName("lastActive") val lastActive: Long = 0L
)

data class PublicIndexResponse(
    @SerializedName("ok") val ok: Boolean = true,
    @SerializedName("liveCount") val liveCount: Int = 0,
    @SerializedName("completedCount") val completedCount: Int = 0,
    @SerializedName("campaigns") val campaigns: List<CampaignItem> = emptyList(),
    @SerializedName("specialCampaigns") val specialCampaigns: List<CampaignItem> = emptyList(),
    @SerializedName("smsQuotaExceeded") val smsQuotaExceeded: Boolean = false,
    @SerializedName("adminIndianSmsEnabled") val adminIndianSmsEnabled: Boolean = true
)

data class SubmitCountResponse(
    @SerializedName("ok") val ok: Boolean = true,
    @SerializedName("newTotal") val newTotal: Long = 0L,
    @SerializedName("myTotal") val myTotal: Long = 0L,
    @SerializedName("completed") val completed: Boolean = false
)

data class CreateGroupResponse(
    @SerializedName("ok") val ok: Boolean = true,
    @SerializedName("campaignId") val campaignId: String = "",
    @SerializedName("joinCode") val joinCode: String = "",
    @SerializedName("campaign") val campaign: CampaignItem? = null
)

data class ParticipantsResponse(
    @SerializedName("ok") val ok: Boolean = true,
    @SerializedName("participants") val participants: List<ParticipantItem> = emptyList()
)

data class MegaContestRegistrationResponse(
    @SerializedName("ok") val ok: Boolean = true,
    @SerializedName("ticketNumber") val ticketNumber: String = "",
    @SerializedName("message") val message: String = ""
)

data class StoredMegaTicket(
    val ticketNumber: String = "",
    val fullName: String = "",
    val phone: String = "",
    val city: String = "",
    val timestamp: Long = 0L,
    val campaignId: String = ""
)

data class DailyWirdItem(
    @SerializedName("date") val date: String = "",
    @SerializedName("day") val day: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("arabic") val arabic: String = "",
    @SerializedName("translit") val translit: String = "",
    @SerializedName("meaning") val meaning: String = "",
    @SerializedName("target") val target: Long = 100L,
    @SerializedName("virtue") val virtue: String = ""
)

data class WeeklyWirdPlan(
    @SerializedName("week_index") val weekIndex: Int = 0,
    @SerializedName("title") val title: String = "",
    @SerializedName("theme") val theme: String = "",
    @SerializedName("days") val days: List<DailyWirdItem> = emptyList()
)

data class WeeklyWirdResponse(
    @SerializedName("ok") val ok: Boolean = true,
    @SerializedName("currentWeek") val currentWeek: Int = 0,
    @SerializedName("requestedWeek") val requestedWeek: Int = 0,
    @SerializedName("weekEndsAt") val weekEndsAt: Long = 0L,
    @SerializedName("plan") val plan: WeeklyWirdPlan? = null,
    @SerializedName("recentPlans") val recentPlans: List<WeeklyWirdPlan> = emptyList()
)

data class LeaderboardUserItem(
    @SerializedName("rank") val rank: Int = 0,
    @SerializedName("displayName") val displayName: String = "",
    @SerializedName("avatarId") val avatarId: Int = 1,
    @SerializedName("city") val city: String = "",
    @SerializedName("state") val state: String = "",
    @SerializedName("country") val country: String = "",
    @SerializedName("totalCount") val totalCount: Long = 0L
)

data class LeaderboardRankingsResponse(
    @SerializedName("ok") val ok: Boolean = true,
    @SerializedName("weekIndex") val weekIndex: Int = 0,
    @SerializedName("weekEndsAt") val weekEndsAt: Long = 0L,
    @SerializedName("scope") val scope: String = "global",
    @SerializedName("rankings") val rankings: List<LeaderboardUserItem> = emptyList()
)

data class LeaderboardMyRankResponse(
    @SerializedName("ok") val ok: Boolean = true,
    @SerializedName("weekIndex") val weekIndex: Int = 0,
    @SerializedName("weekEndsAt") val weekEndsAt: Long = 0L,
    @SerializedName("myCount") val myCount: Long = 0L,
    @SerializedName("rank") val rank: Int = 0,
    @SerializedName("scope") val scope: String = "global"
)
