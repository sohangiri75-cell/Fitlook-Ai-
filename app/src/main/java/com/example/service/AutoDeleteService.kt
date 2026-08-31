package com.example.service

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.AutoDeleteDuration
import com.example.data.repository.LookRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class AutoDeleteService(
    private val context: Context,
    private val lookRepository: LookRepository
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("fitlook_privacy_prefs", Context.MODE_PRIVATE)

    private val _selectedDuration = MutableStateFlow(
        AutoDeleteDuration.fromMinutes(prefs.getLong("auto_delete_minutes", AutoDeleteDuration.TEN_MINUTES.minutes))
    )
    val selectedDuration: StateFlow<AutoDeleteDuration> = _selectedDuration.asStateFlow()

    fun setAutoDeleteDuration(duration: AutoDeleteDuration) {
        _selectedDuration.value = duration
        prefs.edit().putLong("auto_delete_minutes", duration.minutes).apply()
    }

    /**
     * Formats remaining time before server auto-delete.
     */
    fun formatRemainingTime(expiresAt: Long): String {
        val remainingMillis = expiresAt - System.currentTimeMillis()
        if (remainingMillis <= 0) return "Expired & Auto-deleted"

        val hours = remainingMillis / (1000 * 60 * 60)
        val minutes = (remainingMillis % (1000 * 60 * 60)) / (1000 * 60)
        val seconds = (remainingMillis % (1000 * 60)) / 1000
        
        return when {
            hours > 0 -> "${hours}h ${minutes}m left"
            minutes > 0 -> "${minutes}m ${seconds}s left"
            else -> "${seconds}s left"
        }
    }

    /**
     * Permanent Delete Now action.
     * Deletes uploaded person photo, clothing photo, generated result, and local server cache records.
     * Note: Downloaded gallery files on user's storage are retained.
     */
    suspend fun deleteNow(lookId: String) {
        lookRepository.deleteLook(lookId)
    }

    /**
     * Deletes all unsaved uploaded photos and results immediately (Purge All Server Stored Data).
     */
    suspend fun deleteAllServerData() {
        lookRepository.deleteAllServerLooks()
        // Clean cached images
        try {
            val cacheDir = File(context.cacheDir, "images")
            if (cacheDir.exists()) cacheDir.deleteRecursively()
        } catch (e: Exception) {
            // Ignore
        }
    }

    /**
     * Background periodic cleanup of expired items.
     */
    fun triggerPeriodicCleanup(scope: CoroutineScope) {
        scope.launch(Dispatchers.IO) {
            lookRepository.cleanupExpiredLooks()
        }
    }
}
