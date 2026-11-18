package com.tasbih.digitaltasbih.data.groups

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import org.json.JSONObject

/**
 * Authoritative Repository for Live Dhikr, Collaborative Group Campaigns & Umrah Lucky Draw.
 * Integrates directly with the Cloudflare Worker serverless backend and provides an
 * offline local-storage fallback engine (gcLocalEngine) for zero network failures.
 *
 * Auth: Firebase Anonymous Auth (idToken) + User-Agent header (Cloudflare bot protection).
 */
class GroupCampaignRepository(private val context: Context) {

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(CampaignItem::class.java, JsonDeserializer { json, _, _ ->
            val obj = json.asJsonObject
            val id = (obj.get("campaignId") ?: obj.get("id"))?.asString ?: ""
            val title = obj.get("title")?.asString ?: ""
            val arabic = obj.get("arabic")?.asString ?: ""
            val translit = obj.get("translit")?.asString ?: ""
            val meaning = obj.get("meaning")?.asString ?: ""
            val target = obj.get("target")?.asLong ?: 1000L
            val totalCount = obj.get("totalCount")?.asLong ?: 0L
            val myContribution = obj.get("myContribution")?.asLong ?: 0L
            val joinCode = obj.get("joinCode")?.asString ?: ""
            val note = obj.get("note")?.asString ?: ""
            val isCreator = obj.get("isCreator")?.asBoolean ?: false
            val visibility = obj.get("visibility")?.asString ?: "public"
            val creatorName = (obj.get("creatorName") ?: obj.get("communityName"))?.asString ?: ""
            val location = obj.get("location")?.asString ?: ""
            val participantsCount = obj.get("participantsCount")?.asInt ?: 1

            val createdAtElem = obj.get("createdAt")
            val createdAtVal = when {
                createdAtElem == null || createdAtElem.isJsonNull -> 0L
                createdAtElem.isJsonPrimitive && createdAtElem.asJsonPrimitive.isNumber -> createdAtElem.asLong
                createdAtElem.isJsonPrimitive && createdAtElem.asJsonPrimitive.isString -> {
                    val s = createdAtElem.asString
                    s.toLongOrNull() ?: run {
                        try {
                            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.US)
                            sdf.parse(s)?.time ?: 0L
                        } catch (_: Exception) { 0L }
                    }
                }
                else -> 0L
            }

            val isSpecialMega = obj.get("isSpecialMega")?.asBoolean ?: false
            val prizeTitle = obj.get("prizeTitle")?.asString ?: ""
            val regEndDate = obj.get("regEndDate")?.asString ?: ""
            val minDailyDhikr = obj.get("minDailyDhikr")?.asInt ?: 100
            val contestRules = obj.get("contestRules")?.asString ?: ""
            val badgeRibbon = obj.get("badgeRibbon")?.asString ?: ""
            val completed = obj.get("completed")?.asBoolean ?: false

            CampaignItem(
                campaignId = id,
                title = title,
                arabic = arabic,
                translit = translit,
                meaning = meaning,
                target = target,
                totalCount = totalCount,
                myContribution = myContribution,
                joinCode = joinCode,
                note = note,
                isCreator = isCreator,
                visibility = visibility,
                creatorName = creatorName,
                location = location,
                participantsCount = participantsCount,
                createdAt = createdAtVal,
                isSpecialMega = isSpecialMega,
                prizeTitle = prizeTitle,
                regEndDate = regEndDate,
                minDailyDhikr = minDailyDhikr,
                contestRules = contestRules,
                badgeRibbon = badgeRibbon,
                completed = completed
            )
        })
        .create()
    private val prefs: SharedPreferences = context.getSharedPreferences("dt_campaign_prefs", Context.MODE_PRIVATE)

    companion object {
        const val BASE_URL = "https://api.imaanix.in/campaigns/"
        const val AUTH_BASE_URL = "https://api.imaanix.in/auth/"
        const val UMRAH_CAMPAIGN_ID = "camp_mega_umrah_2026"

        private const val KEY_DEVICE_ID = "dt_device_id"
        private const val KEY_USER_NAME = "dt_gc_name"
        private const val KEY_USER_CITY = "dt_gc_city"
        private const val KEY_JOINED_IDS = "dt_gc_joined_ids"
        private const val KEY_CREATED_IDS = "dt_gc_created_ids"
        private const val KEY_SAVED_IDS = "dt_gc_saved_ids"
        private const val KEY_CONSENT_ACCEPTED = "dt_gc_consent_accepted"
        private const val KEY_LOCAL_CAMPAIGNS = "dt_gc_local_campaigns_v2"
        private const val KEY_TICKET_PREFIX = "dt_mega_ticket_"
        private const val KEY_DELETED_IDS = "dt_gc_deleted_ids"
        private const val KEY_VERIFIED_CONTACT = "dt_gc_verified_contact"
        private const val KEY_VERIFIED_CONTACT_TYPE = "dt_gc_verified_type"
        private const val KEY_SMS_QUOTA_EXCEEDED = "dt_gc_sms_quota_exceeded"
        private const val KEY_ADMIN_INDIAN_SMS_ENABLED = "dt_gc_admin_indian_sms_enabled"

        // Firebase Auth keys
        private const val FIREBASE_API_KEY = "AIzaSyB3ksliKr_-RBXlwqU12WP1-8cMwIWXmho"
        private const val FIREBASE_SIGNUP_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=$FIREBASE_API_KEY"
        private const val FIREBASE_REFRESH_URL = "https://securetoken.googleapis.com/v1/token?key=$FIREBASE_API_KEY"
        private const val KEY_FB_REFRESH_TOKEN = "dt_fb_refresh"
        private const val KEY_FB_UID = "dt_fb_uid"
        private const val KEY_FB_ID_TOKEN = "dt_fb_id_token"
        private const val KEY_FB_TOKEN_EXPIRES = "dt_fb_token_expires"

        private const val TAG = "GCRepository"
    }

    init {
        GroupLocationsProvider.init(context)
        purgeDemoCampaigns()
    }

    // ══════════════════════════════════════════════════════════════
    // FIREBASE ANONYMOUS AUTH (matches original DT_AUTH exactly)
    // ══════════════════════════════════════════════════════════════

    private var cachedIdToken: String? = null
    private var tokenExpiresAt: Long = 0L

    /**
     * Get a valid Firebase Anonymous Auth ID token.
     * Uses cached token if still valid, refreshes if expired, or creates new anonymous account.
     */
    @Synchronized
    fun getFirebaseToken(forceRefresh: Boolean = false): String? {
        // 1. If user signed in with Google/provider via FirebaseAuth, use their live ID token!
        try {
            val fbUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
            if (fbUser != null && !fbUser.isAnonymous) {
                val task = fbUser.getIdToken(forceRefresh)
                val tokenResult = com.google.android.gms.tasks.Tasks.await(task)
                val liveToken = tokenResult.token
                if (!liveToken.isNullOrBlank()) {
                    cachedIdToken = liveToken
                    return liveToken
                }
            }
        } catch (_: Exception) {}

        // 2. Return cached token if still valid (with 5 min buffer)
        if (!forceRefresh && cachedIdToken != null && System.currentTimeMillis() < tokenExpiresAt) {
            return cachedIdToken
        }

        // Try to refresh with stored refresh token
        val refreshToken = prefs.getString(KEY_FB_REFRESH_TOKEN, null)
        if (refreshToken != null) {
            try {
                val token = refreshFirebaseToken(refreshToken)
                if (token != null) return token
            } catch (_: Exception) {
                // Refresh failed, create new anonymous account
            }
        }

        // Sign up anonymous
        return try {
            signUpAnonymous()
        } catch (e: Exception) {
            Log.w(TAG, "Firebase auth failed: ${e.message}")
            null
        }
    }

    private fun signUpAnonymous(): String? {
        val keys = listOf(
            "AIzaSyB3ksliKr_-RBXlwqU12WP1-8cMwIWXmho",
            "AIzaSyCgVDJ59GRTHkPFIGJH8utWB3mR-0VjgtI"
        )
        for (key in keys) {
            try {
                val url = URL("https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=$key")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.connectTimeout = 8000
                conn.readTimeout = 8000
                conn.doOutput = true
                conn.setRequestProperty("Content-Type", "application/json")

                OutputStreamWriter(conn.outputStream, "UTF-8").use {
                    it.write("""{"returnSecureToken":true}""")
                    it.flush()
                }

                if (conn.responseCode in 200..299) {
                    val response = BufferedReader(InputStreamReader(conn.inputStream, "UTF-8")).use { it.readText() }
                    conn.disconnect()
                    return storeAuthResult(response)
                }
                conn.disconnect()
            } catch (e: Exception) {
                Log.w(TAG, "Anonymous sign-up key error: ${e.message}")
            }
        }
        return null
    }

    private fun refreshFirebaseToken(refreshToken: String): String? {
        val url = URL(FIREBASE_REFRESH_URL)
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.connectTimeout = 8000
        conn.readTimeout = 8000
        conn.doOutput = true
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

        val body = "grant_type=refresh_token&refresh_token=${java.net.URLEncoder.encode(refreshToken, "UTF-8")}"
        OutputStreamWriter(conn.outputStream, "UTF-8").use { it.write(body); it.flush() }

        if (conn.responseCode in 200..299) {
            val response = BufferedReader(InputStreamReader(conn.inputStream, "UTF-8")).use { it.readText() }
            conn.disconnect()
            return storeAuthResult(response)
        }
        val respCode = conn.responseCode
        conn.disconnect()
        // Only clear refresh token if specifically rejected by Firebase (HTTP 400 Invalid/Revoked token).
        // Transient network drops or 5xx server issues must NEVER wipe the user's stored refresh token!
        if (respCode == 400) {
            prefs.edit().remove(KEY_FB_REFRESH_TOKEN).apply()
        }
        return null
    }

    private fun storeAuthResult(jsonResponse: String): String? {
        return try {
            val map = gson.fromJson(jsonResponse, Map::class.java) as Map<*, *>
            val idToken = (map["idToken"] ?: map["id_token"])?.toString() ?: return null
            val uid = (map["localId"] ?: map["user_id"])?.toString()
            val expiresIn = ((map["expiresIn"] ?: map["expires_in"])?.toString()?.toDoubleOrNull()?.toLong()) ?: 3600L
            val refresh = (map["refreshToken"] ?: map["refresh_token"])?.toString()

            cachedIdToken = idToken
            // 5 min buffer before expiry
            tokenExpiresAt = System.currentTimeMillis() + (expiresIn - 300) * 1000

            prefs.edit().apply {
                if (refresh != null) putString(KEY_FB_REFRESH_TOKEN, refresh)
                if (uid != null) putString(KEY_FB_UID, uid)
                putString(KEY_FB_ID_TOKEN, idToken)
                putLong(KEY_FB_TOKEN_EXPIRES, tokenExpiresAt)
                apply()
            }

            Log.d(TAG, "Firebase auth token obtained, expires in ${expiresIn}s")
            idToken
        } catch (e: Exception) {
            Log.w(TAG, "Failed to parse auth response: ${e.message}")
            null
        }
    }

    // ── Profile & Device Management ──

    fun getDeviceId(): String {
        var id = prefs.getString(KEY_DEVICE_ID, null)
        if (id.isNullOrBlank()) {
            id = "dev_" + UUID.randomUUID().toString().replace("-", "").take(16)
            prefs.edit().putString(KEY_DEVICE_ID, id).apply()
        }
        return id
    }

    fun getUserName(): String = prefs.getString(KEY_USER_NAME, "") ?: ""
    fun getUserCity(): String = prefs.getString(KEY_USER_CITY, "") ?: ""

    fun saveUserProfile(name: String, city: String) {
        prefs.edit()
            .putString(KEY_USER_NAME, name.trim())
            .putString(KEY_USER_CITY, city.trim())
            .putBoolean("dt_gc_profile_done", true)
            .apply()
    }

    fun getFirstName(): String = prefs.getString("dt_gc_first_name", "") ?: ""
    fun getLastName(): String = prefs.getString("dt_gc_last_name", "") ?: ""
    fun getCountry(): String = prefs.getString("dt_gc_country", "India") ?: "India"
    fun getState(): String = prefs.getString("dt_gc_state", "") ?: ""
    fun getCity(): String = prefs.getString("dt_gc_city", "") ?: ""
    fun isProfileDone(): Boolean = prefs.getBoolean("dt_gc_profile_done", false) && getUserName().isNotBlank()

    // ── 18+ Age Verification & Restriction for Community Features ──
    fun isUser18Plus(): Boolean = prefs.getBoolean("dt_gc_is_18_plus", false)
    fun isAgeDeclared(): Boolean = prefs.getBoolean("dt_gc_age_declared", false)

    fun setAge18Plus(is18Plus: Boolean) {
        prefs.edit()
            .putBoolean("dt_gc_is_18_plus", is18Plus)
            .putBoolean("dt_gc_age_declared", true)
            .apply()
    }

    fun resetAgeDeclaration() {
        prefs.edit()
            .remove("dt_gc_is_18_plus")
            .remove("dt_gc_age_declared")
            .apply()
    }

    fun isContactVerified(): Boolean {
        return prefs.getString(KEY_VERIFIED_CONTACT, null)?.isNotBlank() == true
    }

    fun getVerifiedContact(): String? {
        return prefs.getString(KEY_VERIFIED_CONTACT, null)
    }

    fun getVerifiedContactType(): String? {
        return prefs.getString(KEY_VERIFIED_CONTACT_TYPE, "phone")
    }

    fun saveVerifiedContact(contact: String, type: String = "phone") {
        prefs.edit()
            .putString(KEY_VERIFIED_CONTACT, contact.trim())
            .putString(KEY_VERIFIED_CONTACT_TYPE, type.trim())
            .apply()
    }

    fun clearVerifiedContact() {
        prefs.edit()
            .remove(KEY_VERIFIED_CONTACT)
            .remove(KEY_VERIFIED_CONTACT_TYPE)
            .apply()
    }

    /**
     * Complete local data erasure for DPDP Act & Google Play user deletion compliance.
     * Clears all cached profile info, phone numbers, campaign identifiers, signs out of Firebase,
     * and resets Firebase Analytics App Instance ID.
     */
    fun clearAllUserData() {
        prefs.edit().clear().apply()
        cachedIdToken = null
        tokenExpiresAt = 0L
        try {
            com.google.firebase.auth.FirebaseAuth.getInstance().signOut()
        } catch (_: Exception) {}
        try {
            com.google.firebase.analytics.FirebaseAnalytics.getInstance(context).resetAnalyticsData()
        } catch (_: Exception) {}
    }

    fun isSmsQuotaExceeded(): Boolean {
        return prefs.getBoolean(KEY_SMS_QUOTA_EXCEEDED, false)
    }

    fun setSmsQuotaExceeded(exceeded: Boolean) {
        prefs.edit().putBoolean(KEY_SMS_QUOTA_EXCEEDED, exceeded).apply()
    }

    fun isIndianSmsGatewayEnabled(): Boolean {
        return prefs.getBoolean(KEY_ADMIN_INDIAN_SMS_ENABLED, true)
    }

    fun setIndianSmsGatewayEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_ADMIN_INDIAN_SMS_ENABLED, enabled).apply()
    }

    /**
     * Determines whether Mobile SMS OTP channel is available based on user's country and quota rules:
     * Condition 1: If SMS quota limit is hit, outside India users get NO mobile OTP (email only).
     * Condition 2: If SMS quota limit is hit, Indian users also get NO mobile OTP if admin has switched off Indian paid SMS gateway.
     */
    fun isMobileOtpAvailable(country: String, countryCode: String = "+91"): Boolean {
        val isIndia = country.equals("India", ignoreCase = true) || countryCode.trim() == "+91"
        val quotaExceeded = isSmsQuotaExceeded()
        if (!quotaExceeded) {
            return true
        }
        if (!isIndia) {
            return false
        }
        return isIndianSmsGatewayEnabled()
    }

    fun saveFullProfile(
        firstName: String,
        lastName: String,
        country: String,
        state: String,
        city: String,
        phone: String = "",
        is18Plus: Boolean = true
    ) {
        val displayName = (firstName.trim() + " " + lastName.trim()).trim().ifBlank { firstName.trim() }
        prefs.edit()
            .putString("dt_gc_first_name", firstName.trim())
            .putString("dt_gc_last_name", lastName.trim())
            .putString("dt_gc_country", country.trim())
            .putString("dt_gc_state", state.trim())
            .putString("dt_gc_city", city.trim())
            .putString("dt_gc_phone", phone.trim())
            .putString(KEY_USER_NAME, displayName)
            .putString(KEY_USER_CITY, city.trim().ifBlank { state.trim().ifBlank { country.trim() } })
            .putString(KEY_CONSENT_ACCEPTED, "1")
            .putBoolean("dt_gc_profile_done", true)
            .putBoolean("dt_gc_is_18_plus", is18Plus)
            .putBoolean("dt_gc_age_declared", true)
            .apply()
    }

    fun getUserPhone(): String = prefs.getString("dt_gc_phone", "") ?: ""


    // ── DPDP Act User Data Consent ──

    fun isConsentAccepted(): Boolean = prefs.getString(KEY_CONSENT_ACCEPTED, "0") == "1"

    fun recordConsentAccepted() {
        prefs.edit().putString(KEY_CONSENT_ACCEPTED, "1").apply()
    }

    // ── Joined & Saved IDs Tracking ──

    fun getJoinedCampaignIds(): Set<String> {
        return prefs.getStringSet(KEY_JOINED_IDS, emptySet()) ?: emptySet()
    }

    fun markCampaignJoined(campaignId: String) {
        val current = getJoinedCampaignIds().toMutableSet()
        current.add(campaignId)
        prefs.edit().putStringSet(KEY_JOINED_IDS, current).apply()
    }

    fun getCreatedCampaignIds(): Set<String> {
        return prefs.getStringSet(KEY_CREATED_IDS, emptySet()) ?: emptySet()
    }

    fun markCampaignCreated(campaignId: String) {
        val current = getCreatedCampaignIds().toMutableSet()
        current.add(campaignId)
        prefs.edit().putStringSet(KEY_CREATED_IDS, current).apply()
        markCampaignJoined(campaignId)
    }

    fun isCampaignCreator(campaignId: String): Boolean {
        return getCreatedCampaignIds().contains(campaignId)
    }

    fun getDeletedCampaignIds(): Set<String> {
        return prefs.getStringSet(KEY_DELETED_IDS, emptySet()) ?: emptySet()
    }

    suspend fun deleteGroup(campaignId: String): Boolean = withContext(Dispatchers.IO) {
        // 1. Permanently track in deleted set
        val deleted = getDeletedCampaignIds().toMutableSet()
        deleted.add(campaignId)
        prefs.edit().putStringSet(KEY_DELETED_IDS, deleted).apply()

        // 2. Remove from created set
        val created = getCreatedCampaignIds().toMutableSet()
        created.remove(campaignId)
        prefs.edit().putStringSet(KEY_CREATED_IDS, created).apply()

        // 3. Remove from joined set
        val joined = getJoinedCampaignIds().toMutableSet()
        joined.remove(campaignId)
        prefs.edit().putStringSet(KEY_JOINED_IDS, joined).apply()

        // 4. Remove from saved set
        val saved = getSavedCampaignIds().toMutableSet()
        saved.remove(campaignId)
        prefs.edit().putStringSet(KEY_SAVED_IDS, saved).apply()

        // 5. Remove from local stored campaigns list
        val localList = getLocalCampaigns().toMutableList()
        localList.removeAll { it.campaignId == campaignId }
        saveLocalCampaigns(localList)

        // 6. Best-effort server notification
        try {
            val body = mapOf(
                "campaignId" to campaignId,
                "deviceId" to getDeviceId()
            )
            executePostRequest("deleteGroup", body)
        } catch (_: Exception) {}

        true
    }

    suspend fun reportGroup(
        campaignId: String,
        campaignTitle: String,
        reason: String,
        notes: String
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val body = mapOf(
                "campaignId" to campaignId,
                "campaignTitle" to campaignTitle,
                "reason" to reason,
                "notes" to notes,
                "deviceId" to getDeviceId()
            )
            val responseText = executePostRequest("reportGroup", body)
            if (!responseText.isNullOrBlank()) {
                val json = JSONObject(responseText)
                if (json.optBoolean("ok", false)) {
                    val msg = json.optString("message", "Thank you. Your report has been submitted for admin review.")
                    Result.success(msg)
                } else {
                    val err = json.optString("error", "Failed to submit report. Please try again.")
                    Result.failure(Exception(err))
                }
            } else {
                Result.failure(Exception("Network error. Please try again later."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getSavedCampaignIds(): Set<String> {
        return prefs.getStringSet(KEY_SAVED_IDS, emptySet()) ?: emptySet()
    }

    fun toggleSaveCampaign(campaignId: String): Boolean {
        val current = getSavedCampaignIds().toMutableSet()
        val isNowSaved = if (current.contains(campaignId)) {
            current.remove(campaignId)
            false
        } else {
            current.add(campaignId)
            true
        }
        prefs.edit().putStringSet(KEY_SAVED_IDS, current).apply()
        return isNowSaved
    }

    // ── Umrah Lucky Draw Stored Ticket ──

    fun getStoredMegaTicket(campaignId: String = UMRAH_CAMPAIGN_ID): StoredMegaTicket? {
        val raw = prefs.getString(KEY_TICKET_PREFIX + campaignId, null) ?: return null
        return try {
            gson.fromJson(raw, StoredMegaTicket::class.java)
        } catch (_: Exception) {
            null
        }
    }

    fun saveMegaTicket(ticket: StoredMegaTicket) {
        val raw = gson.toJson(ticket)
        prefs.edit().putString(KEY_TICKET_PREFIX + ticket.campaignId, raw).apply()
    }

    // ── Network Operations with Offline Fallback ──

    suspend fun fetchPublicIndex(): PublicIndexResponse = withContext(Dispatchers.IO) {
        try {
            val responseText = executePostRequest("getPublicIndex", mapOf("deviceId" to getDeviceId()))
            if (!responseText.isNullOrBlank()) {
                val parsed = gson.fromJson(responseText, PublicIndexResponse::class.java)
                if (parsed.ok) {
                    setSmsQuotaExceeded(parsed.smsQuotaExceeded)
                    setIndianSmsGatewayEnabled(parsed.adminIndianSmsEnabled)
                    val deletedIds = getDeletedCampaignIds()
                    val serverCampaigns = parsed.campaigns.filterNot { isDemoCampaign(it) || deletedIds.contains(it.campaignId) }
                    val special = parsed.specialCampaigns.filterNot { isDemoCampaign(it) || deletedIds.contains(it.campaignId) }
                        .ifEmpty { serverCampaigns.filter { it.isSpecialMega && !it.completed } }

                    // Smart Merge: Preserve all user-created, user-joined, and private groups
                    val localList = getLocalCampaigns().filterNot { isDemoCampaign(it) || deletedIds.contains(it.campaignId) }
                    val joinedIds = getJoinedCampaignIds()
                    val createdIds = getCreatedCampaignIds()

                    val userGroups = localList.filter {
                        !deletedIds.contains(it.campaignId) && (
                            createdIds.contains(it.campaignId) ||
                            joinedIds.contains(it.campaignId) ||
                            it.visibility == "private" ||
                            it.isCreator
                        )
                    }

                    val merged = mutableListOf<CampaignItem>()
                    val serverMap = serverCampaigns.associateBy { it.campaignId }

                    // Add all server campaigns (enriching isCreator if created by this user, preserving highest live count)
                    for (sc in serverCampaigns) {
                        val isMine = createdIds.contains(sc.campaignId)
                        val localMatch = localList.firstOrNull { it.campaignId == sc.campaignId }
                        val bestTotal = maxOf(sc.totalCount, localMatch?.totalCount ?: 0L)
                        val myContrib = maxOf(sc.myContribution, localMatch?.myContribution ?: 0L)
                        merged.add(
                            sc.copy(
                                totalCount = bestTotal,
                                myContribution = myContrib,
                                isCreator = isMine || (localMatch?.isCreator == true),
                                completed = sc.completed || (bestTotal >= sc.target)
                            )
                        )
                    }

                    // Add user groups not in server response (e.g. private groups or offline created)
                    for (ug in userGroups) {
                        if (!serverMap.containsKey(ug.campaignId)) {
                            val isMine = createdIds.contains(ug.campaignId) || ug.isCreator
                            merged.add(ug.copy(isCreator = isMine))
                        }
                    }

                    val finalResponse = parsed.copy(
                        campaigns = merged,
                        specialCampaigns = special,
                        liveCount = merged.count { !it.completed },
                        completedCount = merged.count { it.completed }
                    )
                    saveLocalCampaigns(merged)
                    return@withContext finalResponse
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "fetchPublicIndex failed: ${e.message}")
        }
        return@withContext getLocalFallbackIndex()
    }

    suspend fun submitCount(
        campaignId: String,
        count: Int,
        sessionDurationMs: Long = 0L,
        isSpecial: Boolean = false
    ): Result<SubmitCountResponse> = withContext(Dispatchers.IO) {
        // ── Server-side plausibility guard for Special Campaign ──
        // Max physically possible: 5 taps/sec. If count/duration implies more, clamp it.
        val sanitizedCount = if (isSpecial && sessionDurationMs > 0L) {
            val elapsedSec = (sessionDurationMs / 1000.0).coerceAtLeast(1.0)
            val maxPossible = (elapsedSec * 5).toInt().coerceAtLeast(count.coerceAtMost(200))
            count.coerceAtMost(maxPossible)
        } else {
            count
        }

        try {
            val body = mutableMapOf<String, Any>(
                "campaignId" to campaignId,
                "deviceId" to getDeviceId(),
                "displayName" to getUserName().ifBlank { "Devotee" },
                "amount" to sanitizedCount
            )
            // Include audit metadata for admin anomaly detection
            if (isSpecial) {
                body["isSpecial"] = true
                body["sessionDurationMs"] = sessionDurationMs
                body["impliedTapsPerSec"] = if (sessionDurationMs > 0L) {
                    String.format(java.util.Locale.US, "%.2f", sanitizedCount / (sessionDurationMs / 1000.0))
                } else "n/a"
            }
            val responseText = executePostRequest("submitCampaignCount", body)
            if (!responseText.isNullOrBlank()) {
                // Worker returns {"ok":true,"totalCount":123}
                // Map totalCount to newTotal for our SubmitCountResponse
                val rawMap = gson.fromJson(responseText, Map::class.java) as? Map<*, *>
                if (rawMap != null && rawMap["ok"] == true) {
                    val totalCount = ((rawMap["totalCount"] as? Number)?.toLong())
                        ?: ((rawMap["newTotal"] as? Number)?.toLong())
                        ?: 0L
                    val myTotal = ((rawMap["myTotal"] as? Number)?.toLong()) ?: sanitizedCount.toLong()
                    val completed = rawMap["completed"] == true
                    val resp = SubmitCountResponse(
                        ok = true,
                        newTotal = totalCount,
                        myTotal = myTotal,
                        completed = completed
                    )
                    updateLocalCampaignProgress(campaignId, sanitizedCount)
                    if (totalCount > 0L) {
                        updateCampaignTotal(campaignId, totalCount)
                    }
                    return@withContext Result.success(resp)
                } else if (rawMap != null && rawMap["error"] != null) {
                    val errMsg = rawMap["error"].toString()
                    return@withContext Result.failure(Exception(errMsg))
                }
            }
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
        Result.failure(Exception("Failed to record dhikr taps. Please check your connection."))
    }

    /**
     * Fetch live total count for a campaign from the server.
     * Used for periodic polling in the active dhikr room so all users see real-time updates.
     */
    suspend fun getCampaignTotal(campaignId: String): Long? = withContext(Dispatchers.IO) {
        try {
            val body = mapOf("campaignId" to campaignId)
            val responseText = executePostRequest("getCampaignTotal", body)
            if (!responseText.isNullOrBlank()) {
                val rawMap = gson.fromJson(responseText, Map::class.java) as? Map<*, *>
                if (rawMap != null && rawMap["ok"] == true) {
                    val totalCount = ((rawMap["totalCount"] as? Number)?.toLong()) ?: return@withContext null
                    // Update local storage too
                    val campaigns = getLocalCampaigns().toMutableList()
                    val idx = campaigns.indexOfFirst { it.campaignId == campaignId }
                    if (idx >= 0) {
                        val old = campaigns[idx]
                        if (totalCount > old.totalCount) {
                            campaigns[idx] = old.copy(totalCount = totalCount)
                            saveLocalCampaigns(campaigns)
                        }
                    }
                    return@withContext totalCount
                }
            }
        } catch (_: Exception) {}
        null
    }

    suspend fun createGroup(
        title: String,
        arabic: String,
        target: Long,
        isPrivate: Boolean,
        displayName: String,
        city: String,
        note: String = "",
        isSpecialMega: Boolean = false
    ): Result<CampaignItem> = withContext(Dispatchers.IO) {
        val loc = if (city.isNotBlank()) "$city, India" else "India"

        try {
            val body = mapOf(
                "title" to title,
                "arabic" to arabic,
                "target" to target,
                "isPrivate" to isPrivate,
                "displayName" to displayName,
                "city" to city,
                "note" to note,
                "isSpecialMega" to isSpecialMega,
                "deviceId" to getDeviceId(),
                "firstName" to getFirstName().ifBlank { displayName },
                "lastName" to getLastName(),
                "country" to getCountry(),
                "state" to getState(),
                "visibility" to if (isPrivate) "private" else "public"
            )
            val responseText = executePostRequest("createGroup", body)
            if (!responseText.isNullOrBlank()) {
                val rawMap = gson.fromJson(responseText, Map::class.java) as? Map<*, *>
                if (rawMap != null && rawMap["ok"] == true) {
                    val serverCid = rawMap["campaignId"]?.toString() ?: ""
                    val serverCode = rawMap["joinCode"]?.toString() ?: ""
                    val campObj = (rawMap["campaign"] as? Map<*, *>) ?: emptyMap<String, Any>()

                    val createdCampaign = CampaignItem(
                        campaignId = serverCid,
                        title = campObj["title"]?.toString() ?: title.ifBlank { "Dhikr Campaign" },
                        arabic = campObj["arabic"]?.toString() ?: arabic,
                        translit = campObj["translit"]?.toString() ?: title,
                        target = ((campObj["target"] as? Number)?.toLong()) ?: target.coerceAtLeast(100L),
                        totalCount = 0L,
                        myContribution = 0L,
                        joinCode = serverCode,
                        note = campObj["note"]?.toString() ?: note,
                        isCreator = true,
                        visibility = if (isPrivate) "private" else "public",
                        creatorName = displayName.ifBlank { "Me" },
                        location = loc,
                        participantsCount = 1,
                        createdAt = System.currentTimeMillis(),
                        isSpecialMega = isSpecialMega
                    )
                    markCampaignCreated(serverCid)
                    addLocalCampaign(createdCampaign)
                    return@withContext Result.success(createdCampaign)
                } else if (rawMap != null && rawMap["error"] != null) {
                    return@withContext Result.failure(Exception(rawMap["error"].toString()))
                }
            }
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
        Result.failure(Exception("Failed to create campaign on server."))
    }

    suspend fun joinByCode(code: String): Result<CampaignItem> = withContext(Dispatchers.IO) {
        val cleanCode = code.trim().uppercase()

        // 1. Check if user already joined or created this group locally
        val localList = getLocalCampaigns()
        val joinedIds = getJoinedCampaignIds()
        val createdIds = getCreatedCampaignIds()
        val alreadyMatched = localList.find {
            it.joinCode.equals(cleanCode, ignoreCase = true) || it.campaignId.equals(cleanCode, ignoreCase = true)
        }
        if (alreadyMatched != null && (joinedIds.contains(alreadyMatched.campaignId) || createdIds.contains(alreadyMatched.campaignId))) {
            val isMine = createdIds.contains(alreadyMatched.campaignId) || alreadyMatched.isCreator
            return@withContext Result.success(alreadyMatched.copy(isCreator = isMine))
        }

        // 2. Query Cloudflare server
        try {
            val body = mapOf(
                "joinCode" to cleanCode,
                "code" to cleanCode,
                "deviceId" to getDeviceId(),
                "displayName" to getUserName().ifBlank { "Devotee" }
            )
            val responseText = executePostRequest("joinGroupByCode", body)
            if (!responseText.isNullOrBlank()) {
                val rawMap = gson.fromJson(responseText, Map::class.java) as? Map<*, *>
                if (rawMap != null && (rawMap["ok"] == true || rawMap["campaignId"] != null)) {
                    val campObj = (rawMap["campaign"] as? Map<*, *>) ?: rawMap
                    val serverCid = campObj["campaignId"]?.toString() ?: ""
                    val serverTitle = campObj["title"]?.toString() ?: "Dhikr"
                    val serverArabic = campObj["arabic"]?.toString() ?: ""
                    val serverTarget = ((campObj["target"] as? Number)?.toLong()) ?: 1000L
                    val serverTotal = ((campObj["totalCount"] as? Number)?.toLong()) ?: 0L
                    val serverCreator = campObj["creatorName"]?.toString() ?: "Organizer"
                    val serverLocation = campObj["location"]?.toString() ?: "India"
                    val serverNote = campObj["note"]?.toString() ?: ""
                    val serverVisibility = campObj["visibility"]?.toString() ?: "private"
                    val campaign = CampaignItem(
                        campaignId = serverCid,
                        title = serverTitle,
                        arabic = serverArabic,
                        translit = serverTitle,
                        target = serverTarget,
                        totalCount = serverTotal,
                        joinCode = cleanCode,
                        creatorName = serverCreator,
                        location = serverLocation,
                        note = serverNote,
                        visibility = serverVisibility,
                        isCreator = false
                    )
                    markCampaignJoined(serverCid)
                    addLocalCampaign(campaign)
                    return@withContext Result.success(campaign)
                } else if (rawMap != null && rawMap["error"] != null) {
                    return@withContext Result.failure(Exception(rawMap["error"].toString()))
                }
            }
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }

        // Check local
        val matched = localList.find {
            it.joinCode.equals(cleanCode, ignoreCase = true) || it.campaignId.equals(cleanCode, ignoreCase = true)
        }
        if (matched != null) {
            markCampaignJoined(matched.campaignId)
            return@withContext Result.success(matched)
        }

        // Check cached public campaigns
        try {
            val publicFeed = fetchPublicIndex()
            val publicMatched = (publicFeed.campaigns + publicFeed.specialCampaigns).find {
                it.joinCode.equals(cleanCode, ignoreCase = true) || it.campaignId.equals(cleanCode, ignoreCase = true)
            }
            if (publicMatched != null) {
                markCampaignJoined(publicMatched.campaignId)
                addLocalCampaign(publicMatched)
                return@withContext Result.success(publicMatched)
            }
        } catch (_: Exception) {}

        Result.failure(Exception("Invalid join code. Please check and try again."))
    }

        /**
     * Formats phone input into strict E.164 international format (+919876543210).
     * Prevents HTTP 400 validation rejection from Cloudflare Worker contest endpoint.
     */
    fun formatToE164(phone: String, defaultDialCode: String = "+91"): String {
        val clean = phone.replace(Regex("[\\s()\\-]"), "").trim()
        if (clean.startsWith("+")) return clean
        val dial = if (defaultDialCode.startsWith("+")) defaultDialCode else "+$defaultDialCode"
        val digits = clean.trimStart('0')
        return dial + digits
    }

suspend fun registerMegaContest(
        campaignId: String,
        fullName: String,
        phone: String,
        city: String,
        dialCode: String = "+91"
    ): Result<StoredMegaTicket> = withContext(Dispatchers.IO) {
        val cleanPhone = formatToE164(phone.trim(), dialCode)
        val cleanName = fullName.trim()
        val cleanCity = city.trim()

        try {
            val body = mapOf(
                "campaignId" to campaignId,
                "deviceId" to getDeviceId(),
                "fullName" to cleanName,
                "phone" to cleanPhone,
                "city" to cleanCity,
                "disclaimerAccepted" to true,
                "rulesVersion" to "v1"
            )
            val responseText = executePostRequest("registerMegaContest", body)
            if (!responseText.isNullOrBlank()) {
                val rawMap = gson.fromJson(responseText, Map::class.java) as? Map<*, *>
                if (rawMap != null && rawMap["ok"] == true) {
                    val serverTicket = rawMap["ticketNumber"]?.toString()
                    if (serverTicket.isNullOrBlank()) {
                        return@withContext Result.failure(Exception(rawMap["error"]?.toString() ?: "Server failed to issue contest ticket number"))
                    }
                    val ticket = StoredMegaTicket(
                        ticketNumber = serverTicket,
                        fullName = cleanName,
                        phone = cleanPhone,
                        city = cleanCity,
                        timestamp = System.currentTimeMillis(),
                        campaignId = campaignId
                    )
                    saveMegaTicket(ticket)
                    markCampaignJoined(campaignId)
                    recordConsentAccepted()
                    return@withContext Result.success(ticket)
                } else if (rawMap != null && rawMap["error"] != null) {
                    return@withContext Result.failure(Exception(rawMap["error"].toString()))
                }
            }
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
        Result.failure(Exception("Contest registration failed. Please check your connection."))
    }

    /**
     * Executes HTTP POST against the Auth & OTP Worker.
     */
    private fun executeAuthPostRequest(endpoint: String, body: Any): String? {
        var conn: HttpURLConnection? = null
        return try {
            val cleanEndpoint = endpoint.removePrefix("auth/").removePrefix("/")
            val url = URL(AUTH_BASE_URL + cleanEndpoint)
            conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.connectTimeout = 10000
            conn.readTimeout = 10000
            conn.doOutput = true
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
            conn.setRequestProperty("Accept", "application/json")
            conn.setRequestProperty("User-Agent", "DigitalTasbih/1.0 Android")

            val token = getFirebaseToken()
            if (token != null) {
                conn.setRequestProperty("Authorization", "Bearer $token")
            }

            val jsonString = gson.toJson(body)
            OutputStreamWriter(conn.outputStream, "UTF-8").use { it.write(jsonString); it.flush() }

            val responseCode = conn.responseCode
            val stream = if (responseCode in 200..299) conn.inputStream else conn.errorStream
            if (stream != null) {
                BufferedReader(InputStreamReader(stream, "UTF-8")).use { it.readText() }
            } else {
                Log.w(TAG, "Auth API $endpoint returned HTTP $responseCode")
                null
            }
        } catch (e: Exception) {
            Log.w(TAG, "Auth API $endpoint failed: ${e.message}")
            null
        } finally {
            conn?.disconnect()
        }
    }

    /**
     * Request a 6-digit OTP sent to user's email address via Cloudflare Auth Worker.
     */
    suspend fun sendEmailOtp(email: String): Result<String> = withContext(Dispatchers.IO) {
        val cleanEmail = email.trim().lowercase()
        try {
            val responseText = executeAuthPostRequest("send-email-otp", mapOf("email" to cleanEmail))
            if (!responseText.isNullOrBlank()) {
                val json = JSONObject(responseText)
                val success = json.optBoolean("success", false) || json.optBoolean("ok", false)
                if (success) {
                    Result.success(json.optString("message", "Verification code sent to your email."))
                } else {
                    Result.failure(Exception(json.optString("error", "Failed to send OTP")))
                }
            } else {
                Result.failure(Exception("Unable to contact verification service. Please try again."))
            }
        } catch (e: Exception) {
            Log.e(TAG, "sendEmailOtp error: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * Verify email OTP with Auth Worker, obtaining a Firebase Custom Token and signing in/linking.
     */
    suspend fun verifyEmailOtp(email: String, otp: String): Result<String> = withContext(Dispatchers.IO) {
        val cleanEmail = email.trim().lowercase()
        val cleanOtp = otp.trim()
        try {
            val responseText = executeAuthPostRequest("verify-email-otp", mapOf("email" to cleanEmail, "otp" to cleanOtp))
            if (!responseText.isNullOrBlank()) {
                val json = JSONObject(responseText)
                val success = json.optBoolean("success", false) || json.optBoolean("ok", false)
                val verified = json.optBoolean("verified", false)
                if (success && verified) {
                    val customToken = json.optString("customToken").ifBlank {
                        json.optString("firebaseCustomToken").ifBlank {
                            json.optString("verifiedBadgeToken")
                        }
                    }
                    if (customToken.isNotBlank()) {
                        try {
                            val auth = FirebaseAuth.getInstance()
                            val authResult = com.google.android.gms.tasks.Tasks.await(
                                auth.signInWithCustomToken(customToken)
                            )
                            val fbUser = authResult.user
                            if (fbUser != null) {
                                val tokenRes = com.google.android.gms.tasks.Tasks.await(fbUser.getIdToken(true))
                                val newIdToken = tokenRes.token
                                if (!newIdToken.isNullOrBlank()) {
                                    cachedIdToken = newIdToken
                                    tokenExpiresAt = System.currentTimeMillis() + 3600 * 1000
                                }
                            }
                        } catch (authErr: Exception) {
                            Log.e(TAG, "signInWithCustomToken failed: ${authErr.message}", authErr)
                            return@withContext Result.failure(Exception("Authentication failed: ${authErr.message}"))
                        }
                    }
                    saveVerifiedContact(cleanEmail, "email")
                    Result.success(customToken)
                } else {
                    Result.failure(Exception(json.optString("error", "Invalid or expired verification code.")))
                }
            } else {
                Result.failure(Exception("Unable to verify code with server. Please try again."))
            }
        } catch (e: Exception) {
            Log.e(TAG, "verifyEmailOtp error: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * Link anonymous Firebase user with permanent Google / OAuth credential to preserve UID.
     * Prevents losing ownership of created private campaigns or member docs upon Google Sign-In.
     */
    suspend fun linkAnonymousAccountWithCredential(credential: AuthCredential): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val auth = FirebaseAuth.getInstance()
            val user = auth.currentUser
            if (user != null && user.isAnonymous) {
                try {
                    com.google.android.gms.tasks.Tasks.await(user.linkWithCredential(credential))
                    Log.i(TAG, "Anonymous account successfully linked to credential. UID preserved: ${user.uid}")
                    return@withContext Result.success(true)
                } catch (e: FirebaseAuthUserCollisionException) {
                    Log.w(TAG, "Account collision: credential already in use by another account. Signing in directly.")
                    com.google.android.gms.tasks.Tasks.await(auth.signInWithCredential(credential))
                    return@withContext Result.success(false)
                }
            } else {
                com.google.android.gms.tasks.Tasks.await(auth.signInWithCredential(credential))
                return@withContext Result.success(false)
            }
        } catch (e: Exception) {
            Log.e(TAG, "linkAnonymousAccountWithCredential error: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun fetchParticipants(campaignId: String, fallbackCampaign: CampaignItem? = null): List<ParticipantItem> = withContext(Dispatchers.IO) {
        val result = mutableListOf<ParticipantItem>()
        try {
            val body = mapOf("campaignId" to campaignId)
            val responseText = executePostRequest("getParticipants", body)
            if (!responseText.isNullOrBlank()) {
                val rawMap = gson.fromJson(responseText, Map::class.java) as? Map<*, *>
                if (rawMap != null && rawMap["ok"] == true) {
                    val participantsList = rawMap["participants"]
                    if (participantsList is List<*>) {
                        for (p in participantsList) {
                            if (p is Map<*, *>) {
                                result.add(ParticipantItem(
                                    deviceId = p["deviceId"]?.toString() ?: "",
                                    displayName = (p["name"] ?: p["displayName"])?.toString() ?: "Anonymous",
                                    count = ((p["count"] as? Number)?.toLong()) ?: 0L,
                                    city = p["city"]?.toString() ?: "",
                                    lastActive = ((p["lastActive"] as? Number)?.toLong()) ?: 0L
                                ))
                            }
                        }
                    }
                }
            }
        } catch (_: Exception) {}

        val myDeviceId = getDeviceId()
        val myName = getUserName().ifBlank { "You" }
        val myCity = getUserCity()
        val camp = fallbackCampaign ?: getLocalCampaigns().firstOrNull { it.campaignId == campaignId }
        val myLocalContrib = camp?.myContribution ?: 0L

        if (result.isNotEmpty()) {
            val myIdx = result.indexOfFirst {
                (it.deviceId.isNotBlank() && it.deviceId == myDeviceId) ||
                (it.displayName.equals(myName, ignoreCase = true) && myName != "Devotee" && myName != "You")
            }
            if (myIdx >= 0) {
                val existing = result[myIdx]
                if (myLocalContrib > existing.count) {
                    result[myIdx] = existing.copy(count = myLocalContrib)
                }
            } else if (myLocalContrib > 0L) {
                result.add(ParticipantItem(
                    deviceId = myDeviceId,
                    displayName = myName,
                    city = myCity,
                    count = myLocalContrib,
                    lastActive = System.currentTimeMillis()
                ))
            }
            return@withContext result.sortedByDescending { it.count }
        }

        // Return synthesized participant list if offline or server has no participants yet
        if (camp != null) {
            val isUserCreator = camp.isCreator || isCampaignCreator(campaignId)
            val creatorName = camp.creatorName.ifBlank { "Group Creator" }
            val creatorCity = camp.location.ifBlank { myCity.ifBlank { "Global" } }

            if (isUserCreator) {
                val userCount = if (myLocalContrib > 0L) myLocalContrib else camp.totalCount
                result.add(ParticipantItem(
                    deviceId = myDeviceId,
                    displayName = myName,
                    city = myCity.ifBlank { creatorCity },
                    count = userCount,
                    lastActive = System.currentTimeMillis()
                ))
                val others = (camp.totalCount - userCount).coerceAtLeast(0L)
                if (others > 0L) {
                    result.add(ParticipantItem(
                        displayName = "Group Reciters",
                        city = creatorCity,
                        count = others,
                        lastActive = System.currentTimeMillis() - 60000L
                    ))
                }
            } else {
                val userCount = myLocalContrib
                val creatorCount = (camp.totalCount - userCount).coerceAtLeast(0L)
                if (creatorCount > 0L || camp.totalCount == 0L) {
                    result.add(ParticipantItem(
                        displayName = creatorName,
                        city = creatorCity,
                        count = creatorCount,
                        lastActive = System.currentTimeMillis() - 120000L
                    ))
                }
                if (userCount > 0L) {
                    result.add(ParticipantItem(
                        deviceId = myDeviceId,
                        displayName = myName,
                        city = myCity,
                        count = userCount,
                        lastActive = System.currentTimeMillis()
                    ))
                }
            }
            if (result.isNotEmpty()) {
                return@withContext result.sortedByDescending { it.count }
            }
        }

        emptyList()
    }

    // ── Local Storage Engine Helpers ──

    private fun getLocalFallbackIndex(): PublicIndexResponse {
        val deletedIds = getDeletedCampaignIds()
        val list = getLocalCampaigns().filterNot { isDemoCampaign(it) || deletedIds.contains(it.campaignId) }
        val live = list.filter { !it.completed }
        val completed = list.filter { it.completed }
        val special = list.filter { it.isSpecialMega && !it.completed }
        return PublicIndexResponse(
            ok = true,
            liveCount = live.size,
            completedCount = completed.size,
            campaigns = list,
            specialCampaigns = special
        )
    }

    fun getLocalCampaigns(): List<CampaignItem> {
        val raw = prefs.getString(KEY_LOCAL_CAMPAIGNS, null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<CampaignItem>>() {}.type
            gson.fromJson(raw, type) ?: emptyList()
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun saveLocalCampaigns(list: List<CampaignItem>) {
        val raw = gson.toJson(list)
        prefs.edit().putString(KEY_LOCAL_CAMPAIGNS, raw).apply()
    }

    private fun addLocalCampaign(campaign: CampaignItem) {
        val current = getLocalCampaigns().toMutableList()
        current.removeAll { it.campaignId == campaign.campaignId }
        current.add(0, campaign)
        saveLocalCampaigns(current)
    }

    private fun updateLocalCampaignProgress(campaignId: String, count: Int): Long {
        val current = getLocalCampaigns().toMutableList()
        val idx = current.indexOfFirst { it.campaignId == campaignId }
        if (idx >= 0) {
            val old = current[idx]
            val newTot = old.totalCount + count
            val isComp = newTot >= old.target
            current[idx] = old.copy(
                totalCount = newTot,
                myContribution = old.myContribution + count,
                completed = isComp
            )
            saveLocalCampaigns(current)
            return newTot
        }
        return count.toLong()
    }

    fun updateCampaignTotal(campaignId: String, newTotal: Long) {
        val current = getLocalCampaigns().toMutableList()
        val idx = current.indexOfFirst { it.campaignId == campaignId }
        if (idx >= 0) {
            val old = current[idx]
            if (newTotal > old.totalCount) {
                current[idx] = old.copy(
                    totalCount = newTotal,
                    completed = newTotal >= old.target
                )
                saveLocalCampaigns(current)
            }
        }
    }

    fun getPendingTaps(campaignId: String): Int {
        return prefs.getInt("dt_pending_taps_$campaignId", 0)
    }

    fun savePendingTaps(campaignId: String, count: Int) {
        prefs.edit().putInt("dt_pending_taps_$campaignId", count.coerceAtLeast(0)).apply()
    }

    fun isDemoCampaign(camp: CampaignItem): Boolean =
        isDemoCampaign(camp.campaignId, camp.joinCode, camp.title)

    fun isDemoCampaign(campaignId: String, joinCode: String = "", title: String = ""): Boolean {
        val demoIds = setOf(
            UMRAH_CAMPAIGN_ID,
            "camp_subhanallah_global",
            "camp_astaghfirullah_daily",
            "camp_alhamdulillah_comp"
        )
        val demoCodes = setOf("UMR26", "GC1001", "GC2002", "GC3003")
        return demoIds.contains(campaignId) ||
                demoCodes.contains(joinCode) ||
                title.equals("Durood Shareef", ignoreCase = true) ||
                title.equals("SubhanAllah 100K Group", ignoreCase = true) ||
                title.equals("Daily Astaghfirullah", ignoreCase = true) ||
                title.equals("Alhamdulillah 50K Target", ignoreCase = true)
    }

    private fun purgeDemoCampaigns() {
        val current = getLocalCampaigns()
        val cleaned = current.filterNot { isDemoCampaign(it) }
        if (cleaned.size != current.size) {
            saveLocalCampaigns(cleaned)
        }

        val demoIds = setOf(
            UMRAH_CAMPAIGN_ID,
            "camp_subhanallah_global",
            "camp_astaghfirullah_daily",
            "camp_alhamdulillah_comp"
        )
        val joined = getJoinedCampaignIds().toMutableSet()
        if (joined.removeAll(demoIds)) {
            prefs.edit().putStringSet(KEY_JOINED_IDS, joined).apply()
        }

        val saved = getSavedCampaignIds().toMutableSet()
        if (saved.removeAll(demoIds)) {
            prefs.edit().putStringSet(KEY_SAVED_IDS, saved).apply()
        }

        prefs.edit().remove(KEY_TICKET_PREFIX + UMRAH_CAMPAIGN_ID).apply()
    }

    private fun seedLocalCampaignsIfEmpty() {
        // No-op: Zero demo campaigns
    }

    /**
     * Execute POST request to Worker API with Firebase Auth + User-Agent.
     * Handles 401 retry with fresh token automatically.
     */
    private fun executePostRequest(endpoint: String, body: Any): String? {
        var conn: HttpURLConnection? = null
        return try {
            val url = URL(BASE_URL + endpoint)
            conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.connectTimeout = 8000
            conn.readTimeout = 10000
            conn.doOutput = true
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
            conn.setRequestProperty("Accept", "application/json")
            // CRITICAL: Cloudflare bot protection requires a real User-Agent
            conn.setRequestProperty("User-Agent", "DigitalTasbih/1.0 Android")

            // Add Firebase Anonymous Auth token
            val token = getFirebaseToken()
            if (token != null) {
                conn.setRequestProperty("Authorization", "Bearer $token")
            }

            val jsonString = gson.toJson(body)
            OutputStreamWriter(conn.outputStream, "UTF-8").use { it.write(jsonString); it.flush() }

            val responseCode = conn.responseCode

            // Handle 401: refresh token and retry once
            if (responseCode == 401) {
                conn.disconnect()
                val freshToken = getFirebaseToken(forceRefresh = true)
                if (freshToken != null) {
                    val retryUrl = URL(BASE_URL + endpoint)
                    conn = retryUrl.openConnection() as HttpURLConnection
                    conn.requestMethod = "POST"
                    conn.connectTimeout = 8000
                    conn.readTimeout = 10000
                    conn.doOutput = true
                    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
                    conn.setRequestProperty("Accept", "application/json")
                    conn.setRequestProperty("User-Agent", "DigitalTasbih/1.0 Android")
                    conn.setRequestProperty("Authorization", "Bearer $freshToken")

                    OutputStreamWriter(conn.outputStream, "UTF-8").use { it.write(jsonString); it.flush() }

                    val retryCode = conn.responseCode
                    if (retryCode in 200..299) {
                        return BufferedReader(InputStreamReader(conn.inputStream, "UTF-8")).use { it.readText() }
                    }
                }
                return null
            }

            val stream = if (responseCode in 200..299) conn.inputStream else conn.errorStream
            if (stream != null) {
                BufferedReader(InputStreamReader(stream, "UTF-8")).use { it.readText() }
            } else {
                Log.w(TAG, "API $endpoint returned HTTP $responseCode")
                null
            }
        } catch (e: Exception) {
            Log.w(TAG, "API $endpoint failed: ${e.message}")
            null
        } finally {
            conn?.disconnect()
        }
    }

    // ── Spotlight Tour Walkthrough States (Main feed, Create group, Active room) ──
    fun isMainTourDone(): Boolean = prefs.getBoolean("gc_main_tour_done_v1", false)
    fun setMainTourDone(done: Boolean = true) = prefs.edit().putBoolean("gc_main_tour_done_v1", done).apply()

    fun isCreateTourDone(): Boolean = prefs.getBoolean("gc_create_tour_done_v1", false)
    fun setCreateTourDone(done: Boolean = true) = prefs.edit().putBoolean("gc_create_tour_done_v1", done).apply()

    fun isRoomTourDone(): Boolean = prefs.getBoolean("gc_room_tour_done_v1", false)
    fun setRoomTourDone(done: Boolean = true) = prefs.edit().putBoolean("gc_room_tour_done_v1", done).apply()
}
