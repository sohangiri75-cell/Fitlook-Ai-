package com.example.data.model

sealed interface TryOnStage {
    data object Idle : TryOnStage
    data class Processing(val stageMessage: String, val progress: Float) : TryOnStage
    data class Success(val result: TryOnResultData) : TryOnStage
    data class Error(val message: String, val canRetry: Boolean = true) : TryOnStage
}

data class TryOnResultData(
    val lookId: String,
    val personImageUri: String,
    val clothingImageUri: String,
    val resultImageUri: String,
    val personCategory: PersonCategory,
    val clothingCategory: String,
    val fitStyle: FitStyle,
    val createdAt: Long = System.currentTimeMillis(),
    val expiresAt: Long = System.currentTimeMillis() + (10 * 60 * 1000L), // Default 10 mins
    val isDemoMode: Boolean = true,
    val isSaved: Boolean = false,
    val aiConfidenceNote: String = "High precision neural fit applied with face & identity preservation",
    // B2B Catalogue linkage
    val productId: String? = null,
    val productName: String? = null,
    val productPrice: Double? = null,
    val productSizes: String? = null,
    val shopName: String? = null,
    val shopWhatsapp: String? = null,
    val autoDeleteDuration: AutoDeleteDuration = AutoDeleteDuration.TEN_MINUTES
)
