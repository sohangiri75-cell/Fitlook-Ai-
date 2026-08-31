package com.example.data.model

data class UserAccount(
    val userId: String,
    val displayName: String,
    val emailOrPhone: String,
    val authType: AuthType,
    val privacyConsentGiven: Boolean = false
)

enum class AuthType {
    GUEST,
    GOOGLE,
    MOBILE_OTP
}
