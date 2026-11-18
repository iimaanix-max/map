package com.tasbih.digitaltasbih.util

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability

/**
 * Production-Hardened Google Play In-App Update Manager.
 *
 * Provides:
 * 1. Flexible Background Updates (Non-disruptive): Downloads silently while user counts tasbih/reads Quran.
 * 2. Immediate Force Updates: Triggered automatically for critical versions (high staleness/priority).
 * 3. Reactive 'isUpdateDownloaded' state to render a 1-tap "Restart to Apply" banner.
 * 4. Zero-crash safety guards: Sideloaded APKs or devices without Google Play fail silently.
 */
class InAppUpdateManager private constructor(private val activity: ComponentActivity) {

    private val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(activity)
    private var updateLauncher: ActivityResultLauncher<IntentSenderRequest>? = null

    private val _isUpdateDownloaded = mutableStateOf(false)
    val isUpdateDownloaded: State<Boolean> = _isUpdateDownloaded

    private val installStateListener = InstallStateUpdatedListener { state ->
        when (state.installStatus()) {
            InstallStatus.DOWNLOADED -> {
                Log.i(TAG, "In-app update downloaded successfully and ready for install.")
                _isUpdateDownloaded.value = true
            }
            InstallStatus.DOWNLOADING -> {
                val bytesDownloaded = state.bytesDownloaded()
                val totalBytes = state.totalBytesToDownload()
                if (totalBytes > 0) {
                    val progress = (bytesDownloaded * 100 / totalBytes).toInt()
                    Log.d(TAG, "In-app update downloading: $progress%")
                }
            }
            InstallStatus.FAILED -> {
                Log.w(TAG, "In-app update download failed: ${state.installErrorCode()}")
            }
            InstallStatus.INSTALLED -> {
                _isUpdateDownloaded.value = false
                unregisterListener()
            }
            else -> {}
        }
    }

    init {
        try {
            updateLauncher = activity.registerForActivityResult(
                ActivityResultContracts.StartIntentSenderForResult()
            ) { result ->
                if (result.resultCode != Activity.RESULT_OK) {
                    Log.w(TAG, "Update flow failed or cancelled by user. Result code: ${result.resultCode}")
                }
            }
            appUpdateManager.registerListener(installStateListener)
        } catch (e: Exception) {
            Log.w(TAG, "Failed to register In-App Update launcher/listener: ${e.message}")
        }
    }

    /**
     * Checks Google Play Store for available app updates.
     * Starts flexible download by default, or immediate update if critical.
     */
    fun checkForUpdates() {
        try {
            val appUpdateInfoTask = appUpdateManager.appUpdateInfo
            appUpdateInfoTask.addOnSuccessListener { updateInfo ->
                handleUpdateAvailability(updateInfo)
            }.addOnFailureListener { e ->
                Log.d(TAG, "Play Store update check skipped or unavailable: ${e.message}")
            }
        } catch (e: Exception) {
            Log.w(TAG, "Error checking for in-app updates: ${e.message}")
        }
    }

    private fun handleUpdateAvailability(updateInfo: AppUpdateInfo) {
        val availability = updateInfo.updateAvailability()
        Log.d(TAG, "Update availability: $availability, versionCode: ${updateInfo.availableVersionCode()}")

        if (availability == UpdateAvailability.UPDATE_AVAILABLE) {
            val priority = updateInfo.updatePriority()
            val stalenessDays = updateInfo.clientVersionStalenessDays() ?: 0

            // If critical priority (>= 4) or user is over 14 days out of date -> Immediate update
            val isCritical = priority >= 4 || stalenessDays > 14

            if (isCritical && updateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                startImmediateUpdate(updateInfo)
            } else if (updateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                startFlexibleUpdate(updateInfo)
            } else if (updateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                startImmediateUpdate(updateInfo)
            }
        } else if (updateInfo.installStatus() == InstallStatus.DOWNLOADED) {
            _isUpdateDownloaded.value = true
        }
    }

    private fun startFlexibleUpdate(updateInfo: AppUpdateInfo) {
        try {
            Log.i(TAG, "Starting Flexible (Background) in-app update...")
            val options = AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build()
            val launcher = updateLauncher ?: return
            appUpdateManager.startUpdateFlowForResult(updateInfo, launcher, options)
        } catch (e: Exception) {
            Log.w(TAG, "Failed to launch flexible update flow: ${e.message}")
        }
    }

    private fun startImmediateUpdate(updateInfo: AppUpdateInfo) {
        try {
            Log.i(TAG, "Starting Immediate (Force) in-app update...")
            val options = AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
            val launcher = updateLauncher ?: return
            appUpdateManager.startUpdateFlowForResult(updateInfo, launcher, options)
        } catch (e: Exception) {
            Log.w(TAG, "Failed to launch immediate update flow: ${e.message}")
        }
    }

    /**
     * Resumes in-progress updates on activity resume.
     */
    fun onResume() {
        try {
            appUpdateManager.appUpdateInfo.addOnSuccessListener { updateInfo ->
                if (updateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    _isUpdateDownloaded.value = true
                }
                if (updateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    // Resume immediate update if user left and returned
                    val launcher = updateLauncher ?: return@addOnSuccessListener
                    appUpdateManager.startUpdateFlowForResult(
                        updateInfo,
                        launcher,
                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                    )
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Error in onResume update check: ${e.message}")
        }
    }

    /**
     * Completes the flexible update and restarts the app with the new version.
     */
    fun completeUpdate() {
        try {
            Log.i(TAG, "Completing flexible update and restarting app...")
            appUpdateManager.completeUpdate()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to complete update: ${e.message}", e)
        }
    }

    fun unregisterListener() {
        try {
            appUpdateManager.unregisterListener(installStateListener)
        } catch (e: Exception) {
            Log.w(TAG, "Error unregistering installStateListener: ${e.message}")
        }
    }

    companion object {
        private const val TAG = "InAppUpdateManager"

        @Volatile
        private var instance: InAppUpdateManager? = null

        fun init(activity: ComponentActivity): InAppUpdateManager {
            return instance ?: synchronized(this) {
                instance ?: InAppUpdateManager(activity).also { instance = it }
            }
        }

        fun get(): InAppUpdateManager? = instance
    }
}

