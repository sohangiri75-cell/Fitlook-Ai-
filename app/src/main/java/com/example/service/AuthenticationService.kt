package com.example.service

import android.content.Context
import com.example.data.model.AuthType
import com.example.data.model.UserAccount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class AuthenticationService(private val context: Context) {
    private val prefs = context.getSharedPreferences("fitlook_auth_prefs", Context.MODE_PRIVATE)

    private val _currentUser = MutableStateFlow<UserAccount?>(loadSavedUser())
    val currentUser: StateFlow<UserAccount?> = _currentUser.asStateFlow()

    private fun loadSavedUser(): UserAccount? {
        val userId = prefs.getString("user_id", null) ?: return null
        val name = prefs.getString("user_name", "Guest User") ?: "Guest User"
        val email = prefs.getString("user_email", "") ?: ""
        val typeStr = prefs.getString("auth_type", AuthType.GUEST.name) ?: AuthType.GUEST.name
        val consent = prefs.getBoolean("privacy_consent", false)
        val authType = try { AuthType.valueOf(typeStr) } catch (e: Exception) { AuthType.GUEST }
        return UserAccount(
            userId = userId,
            displayName = name,
            emailOrPhone = email,
            authType = authType,
            privacyConsentGiven = consent
        )
    }

    fun continueAsGuest(): UserAccount {
        val user = UserAccount(
            userId = "guest_" + UUID.randomUUID().toString().take(8),
            displayName = "Fashion Explorer (Guest)",
            emailOrPhone = "guest@fitlook.ai",
            authType = AuthType.GUEST,
            privacyConsentGiven = prefs.getBoolean("privacy_consent", false)
        )
        saveUser(user)
        return user
    }

    fun loginWithGoogle(accountName: String = "User", email: String = "user@gmail.com"): UserAccount {
        val user = UserAccount(
            userId = "google_" + UUID.randomUUID().toString().take(8),
            displayName = accountName,
            emailOrPhone = email,
            authType = AuthType.GOOGLE,
            privacyConsentGiven = prefs.getBoolean("privacy_consent", false)
        )
        saveUser(user)
        return user
    }

    fun loginWithPhone(phoneNumber: String): UserAccount {
        val user = UserAccount(
            userId = "phone_" + UUID.randomUUID().toString().take(8),
            displayName = "User " + phoneNumber.takeLast(4),
            emailOrPhone = phoneNumber,
            authType = AuthType.MOBILE_OTP,
            privacyConsentGiven = prefs.getBoolean("privacy_consent", false)
        )
        saveUser(user)
        return user
    }

    fun setPrivacyConsent(granted: Boolean) {
        prefs.edit().putBoolean("privacy_consent", granted).apply()
        _currentUser.value = _currentUser.value?.copy(privacyConsentGiven = granted)
    }

    fun logout() {
        prefs.edit().clear().apply()
        _currentUser.value = null
    }

    private fun saveUser(user: UserAccount) {
        prefs.edit()
            .putString("user_id", user.userId)
            .putString("user_name", user.displayName)
            .putString("user_email", user.emailOrPhone)
            .putString("auth_type", user.authType.name)
            .putBoolean("privacy_consent", user.privacyConsentGiven)
            .apply()
        _currentUser.value = user
    }
}
