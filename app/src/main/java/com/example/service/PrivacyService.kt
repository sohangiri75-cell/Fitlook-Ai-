package com.example.service

import android.content.Context
import android.content.SharedPreferences

class PrivacyService(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("fitlook_privacy_prefs", Context.MODE_PRIVATE)

    fun hasAcceptedConsent(): Boolean {
        return prefs.getBoolean("has_accepted_consent", false)
    }

    fun acceptConsent() {
        prefs.edit().putBoolean("has_accepted_consent", true).apply()
    }

    fun revokeConsent() {
        prefs.edit().putBoolean("has_accepted_consent", false).apply()
    }

    fun getPrivacyPolicyText(): String {
        return """
            FitLook AI Privacy Commitment:
            
            1. Dedicated Virtual Try-On Use:
            Your photos (full-body and clothing images) are solely utilized to generate your personalized virtual try-on previews.
            
            2. 24-Hour Automatic Server Deletion:
            By default, all uploaded personal photos, clothing images, and AI-generated try-on looks are automatically and permanently purged from server storage after 24 hours.
            
            3. Instant 'Delete Now' Control:
            You can delete any try-on result and uploaded photos immediately using the 'Delete Now' button at any time.
            
            4. User Ownership:
            Photos you explicitly choose to download are saved directly onto your device gallery. You own and control your downloaded media.
        """.trimIndent()
    }
}
