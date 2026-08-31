package com.example.data.model

enum class FitStyle(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String
) {
    SLIM_FIT("slim", "Slim Fit", "Close-fitting contour tailoring", "📐"),
    REGULAR_FIT("regular", "Regular Fit", "Classic comfortable drape", "👔"),
    LOOSE_FIT("loose", "Loose Fit", "Relaxed oversized silhouette", "✨")
}
