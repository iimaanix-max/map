package com.tasbih.digitaltasbih.util

import android.app.Activity
import android.content.Context
import android.telephony.TelephonyManager
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.android.gms.ads.MobileAds
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Google AdMob & UMP Consent Manager.
 *
 * Compliance & Delivery Strategy:
 * 1. INDIA & NON-EU USERS:
 *    Under GDPR and Google EU User Consent Policy, consent is ONLY legally mandated
 *    for users in the European Economic Area (EEA), UK, and Switzerland.
 *    For users in India and all non-EEA countries, consent is NOT required.
 *    Ads initialize and request IMMEDIATELY on app launch with zero blocking.
 *
 * 2. EEA / UK USERS:
 *    Strictly gathers user consent via official Google UMP SDK prior to ad requests.
 *    Zero ad requests fire until consent is granted.
 */
object ConsentManager {
    private const val TAG = "ConsentManager"
    private val isMobileAdsInitialized = AtomicBoolean(false)
    private val consentListeners = CopyOnWriteArrayList<() -> Unit>()

    /**
     * ISO-3166-1 alpha-2 country codes for European Economic Area (EEA), UK, and Switzerland.
     */
    private val EEA_COUNTRY_CODES = setOf(
        "AT", "BE", "BG", "HR", "CY", "CZ", "DK", "EE", "FI", "FR",
        "DE", "GR", "HU", "IS", "IE", "IT", "LV", "LI", "LT", "LU",
        "MT", "NL", "NO", "PL", "PT", "RO", "SK", "SI", "ES", "SE",
        "CH", "GB", "UK"
    )

    /**
     * Observable Compose state. Initialized based on geography:
     * - `true` by default for India & non-EEA users (instant ads).
     * - `false` initially for EEA/UK users until UMP consent is obtained.
     */
    var canRequestAdsState by mutableStateOf(true)
        private set

    /**
     * Determines whether the user is located in the EEA, UK, or Switzerland.
     * Checks UMP requirement status first, followed by device SIM, network, locale, and timezone.
     */
    fun isUserInEEAOrUK(context: Context): Boolean {
        // 1. Check UMP SDK requirement if already resolved
        try {
            val consentInfo = UserMessagingPlatform.getConsentInformation(context)
            if (consentInfo.privacyOptionsRequirementStatus == ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED) {
                return true
            }
            if (consentInfo.privacyOptionsRequirementStatus == ConsentInformation.PrivacyOptionsRequirementStatus.NOT_REQUIRED) {
                return false
            }
        } catch (_: Exception) {}

        // 2. Check Telephony SIM and Network Country (Hardware carrier info)
        try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
            val simCountry = tm?.simCountryIso?.uppercase()
            if (!simCountry.isNullOrBlank() && simCountry.length == 2) {
                return EEA_COUNTRY_CODES.contains(simCountry)
            }
            val networkCountry = tm?.networkCountryIso?.uppercase()
            if (!networkCountry.isNullOrBlank() && networkCountry.length == 2) {
                return EEA_COUNTRY_CODES.contains(networkCountry)
            }
        } catch (_: Exception) {}

        // 3. Check Default Device Locale Country
        try {
            val localeCountry = Locale.getDefault().country?.uppercase()
            if (!localeCountry.isNullOrBlank() && localeCountry.length == 2) {
                return EEA_COUNTRY_CODES.contains(localeCountry)
            }
        } catch (_: Exception) {}

        // 4. Check Timezone ID
        try {
            val tzId = TimeZone.getDefault().id
            if (tzId.startsWith("Europe/")) {
                return true
            }
        } catch (_: Exception) {}

        // Non-EU / India by default
        return false
    }

    /**
     * Main entry point called from [MainActivity.onCreate].
     *
     * - In India / non-EU: Initializes MobileAds immediately and invokes callbacks without blocking.
     * - In EU / UK: Enforces Google UMP consent flow before initializing MobileAds.
     */
    fun requestConsentAndInitAds(
        activity: Activity,
        onConsentComplete: () -> Unit = {}
    ) {
        val inEEA = isUserInEEAOrUK(activity)

        if (!inEEA) {
            // India and Non-EU countries: Consent is NOT required.
            Log.d(TAG, "User outside EEA/UK (India/Non-EU): Consent not required. Initializing MobileAds immediately.")
            canRequestAdsState = true
            initializeMobileAds(activity)
            notifyConsentListeners()
            onConsentComplete()
            return
        }

        // User is in EEA/UK: Enforce Google UMP consent
        Log.d(TAG, "User in EEA/UK: Initiating Google UMP consent flow.")
        val consentInformation = UserMessagingPlatform.getConsentInformation(activity)

        // Check if consent was already granted in a previous session
        if (consentInformation.canRequestAds()) {
            canRequestAdsState = true
            initializeMobileAds(activity)
            notifyConsentListeners()
        } else {
            canRequestAdsState = false
        }

        val params = ConsentRequestParameters.Builder()
            .setTagForUnderAgeOfConsent(false)
            .build()

        consentInformation.requestConsentInfoUpdate(
            activity,
            params,
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { formError ->
                    if (formError != null) {
                        Log.w(TAG, "Consent form dismiss/error: [${formError.errorCode}] ${formError.message}")
                    }

                    if (consentInformation.canRequestAds()) {
                        canRequestAdsState = true
                        initializeMobileAds(activity)
                        notifyConsentListeners()
                    }
                    onConsentComplete()
                }
            },
            { requestError ->
                Log.w(TAG, "Consent info update failed: [${requestError.errorCode}] ${requestError.message}")
                if (consentInformation.canRequestAds()) {
                    canRequestAdsState = true
                    initializeMobileAds(activity)
                    notifyConsentListeners()
                }
                onConsentComplete()
            }
        )
    }

    /**
     * Initializes Google Mobile Ads SDK once per app lifecycle.
     */
    fun initializeMobileAds(context: Context) {
        if (!canRequestAds(context)) {
            Log.w(TAG, "Refusing MobileAds.initialize(): Awaiting EU user consent.")
            return
        }

        if (isMobileAdsInitialized.compareAndSet(false, true)) {
            try {
                val configBuilder = com.google.android.gms.ads.RequestConfiguration.Builder()
                    .setMaxAdContentRating(com.google.android.gms.ads.RequestConfiguration.MAX_AD_CONTENT_RATING_G)

                // DPDP Act 2023 & COPPA: If user has declared age under 18, disable targeted advertising & tracking
                try {
                    val repo = com.tasbih.digitaltasbih.data.groups.GroupCampaignRepository(context)
                    if (repo.isAgeDeclared() && !repo.isUser18Plus()) {
                        configBuilder.setTagForUnderAgeOfConsent(com.google.android.gms.ads.RequestConfiguration.TAG_FOR_UNDER_AGE_OF_CONSENT_TRUE)
                        configBuilder.setTagForChildDirectedTreatment(com.google.android.gms.ads.RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE)
                        Log.i(TAG, "AdMob configured for Child / Under-Age-of-Consent protection (DPDP compliant).")
                    }
                } catch (_: Exception) {}

                if (AdConfig.USE_TEST_ADS) {
                    configBuilder.setTestDeviceIds(listOf(com.google.android.gms.ads.AdRequest.DEVICE_ID_EMULATOR))
                }
                MobileAds.setRequestConfiguration(configBuilder.build())

                MobileAds.initialize(context.applicationContext) { initializationStatus ->
                    Log.d(TAG, "MobileAds initialized successfully: ${initializationStatus.adapterStatusMap.keys}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "MobileAds initialization encountered an error", e)
            }
        }
    }

    /**
     * Checks if ads can currently be requested.
     *
     * - Returns `true` immediately for India and non-EU users.
     * - Returns `true` for EU/UK users only if UMP consent is granted.
     */
    fun canRequestAds(context: Context): Boolean {
        // India & Non-EU: Always allowed
        if (!isUserInEEAOrUK(context)) {
            if (!canRequestAdsState) {
                canRequestAdsState = true
            }
            return true
        }

        // EEA / UK: Strictly check UMP
        return try {
            val consentInfo = UserMessagingPlatform.getConsentInformation(context)
            val isNotRequired = consentInfo.privacyOptionsRequirementStatus == ConsentInformation.PrivacyOptionsRequirementStatus.NOT_REQUIRED
            val canRequest = consentInfo.canRequestAds() || isNotRequired
            if (canRequest && !canRequestAdsState) {
                canRequestAdsState = true
            }
            canRequest
        } catch (_: Exception) {
            false
        }
    }

    /**
     * Registers a listener to be notified as soon as ads can be requested.
     */
    fun addConsentListener(listener: () -> Unit) {
        if (!consentListeners.contains(listener)) {
            consentListeners.add(listener)
        }
        if (canRequestAdsState) {
            listener()
        }
    }

    fun removeConsentListener(listener: () -> Unit) {
        consentListeners.remove(listener)
    }

    private fun notifyConsentListeners() {
        for (listener in consentListeners) {
            try {
                listener()
            } catch (e: Exception) {
                Log.w(TAG, "Error notifying consent listener", e)
            }
        }
    }

    /**
     * Checks if the user is in a region requiring privacy options (e.g., GDPR).
     */
    fun isPrivacyOptionsRequired(context: Context): Boolean {
        return try {
            val consentInfo = UserMessagingPlatform.getConsentInformation(context)
            consentInfo.privacyOptionsRequirementStatus == ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED
        } catch (_: Exception) {
            isUserInEEAOrUK(context)
        }
    }

    /**
     * Shows the Google UMP Privacy Options Form so EU users can change or revoke consent.
     */
    fun showPrivacyOptionsForm(activity: Activity, onDismiss: () -> Unit = {}) {
        UserMessagingPlatform.showPrivacyOptionsForm(activity) { formError ->
            if (formError != null) {
                Log.w(TAG, "Privacy options form error: [${formError.errorCode}] ${formError.message}")
            }
            onDismiss()
        }
    }
}
