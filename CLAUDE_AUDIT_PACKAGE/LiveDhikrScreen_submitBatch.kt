// Location: app/src/main/java/com/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreen.kt
// Feature: Tap Preservation on Transient Failure (Only restores on 429 or network glitch; rejects permanent 400/403/404)
// Batch Size: Clamped to 50 taps to strictly respect D1 atomic speed cap

    val maxBatchSize = 50

    fun submitBatch() {
        if (pendingCount <= 0 || isSubmitting) return
        // Clamp batch size to prevent unrealistically large single submissions
        val countToSync = pendingCount.coerceAtMost(maxBatchSize)
        pendingCount = (pendingCount - countToSync).coerceAtLeast(0)
        isSubmitting = true
        val sessionDurationMs = System.currentTimeMillis() - sessionStartMs
        coroutineScope.launch {
            val res = repository.submitCount(
                campaignId = campaign.campaignId,
                count = countToSync,
                sessionDurationMs = sessionDurationMs,
                isSpecial = isSpecialCampaign
            )
            res.onSuccess { data ->
                if (data.newTotal > 0) liveTotal = data.newTotal
            }.onFailure { ex ->
                val msg = ex.message ?: ""
                val isPermanent = msg.contains("not found", ignoreCase = true) ||
                                  msg.contains("completed", ignoreCase = true) ||
                                  msg.contains("closed", ignoreCase = true) ||
                                  msg.contains("forbidden", ignoreCase = true) ||
                                  msg.contains("not a participant", ignoreCase = true) ||
                                  msg.contains("invalid", ignoreCase = true)
                if (!isPermanent) {
                    // Restore unsubmitted taps back into pendingCount on transient failure (e.g. 429 rate limit or network glitch)
                    pendingCount += countToSync
                }
            }
            isSubmitting = false
        }
    }
