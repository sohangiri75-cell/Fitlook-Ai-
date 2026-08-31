package com.example.data.model

enum class UserMode(val label: String, val description: String) {
    CUSTOMER("Customer Mode", "Try on real outfits from fashion boutiques on your photo"),
    SHOP_OWNER("Shop Owner Mode", "Upload products, manage catalogue, track AI try-ons & sales");

    val displayName: String
        get() = label
}

enum class ProductCategory(val displayName: String) {
    SHIRT("Shirt"),
    T_SHIRT("T-Shirt"),
    JEANS("Jeans"),
    TROUSERS("Trousers"),
    KURTA("Kurta"),
    SAREE("Saree"),
    DRESS("Dress"),
    JACKET("Jacket"),
    SUIT("Suit"),
    OTHER("Other");

    companion object {
        fun fromString(value: String): ProductCategory {
            return entries.firstOrNull { 
                it.name.equals(value, ignoreCase = true) || 
                it.displayName.equals(value, ignoreCase = true) 
            } ?: OTHER
        }
    }
}

data class ProductItem(
    val id: String,
    val shopId: String = "shop_default_1",
    val name: String,
    val price: Double,
    val category: ProductCategory,
    val availableSizes: List<String>,
    val description: String,
    val primaryImageUri: String,
    val additionalImageUris: List<String> = emptyList(),
    val tryOnCount: Int = 0,
    val viewCount: Int = 0,
    val isAvailable: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

data class ShopProfile(
    val shopId: String = "shop_default_1",
    val name: String = "Royal Heritage Boutique",
    val slug: String = "royal-heritage-boutique",
    val logoUri: String = "drawable/img_shop_logo",
    val phone: String = "+91 98765 43210",
    val whatsappNumber: String = "919876543210",
    val description: String = "Designer ethnic, festive wear & premium contemporary apparel.",
    val location: String = "Connaught Place, New Delhi, India",
    val plan: SubscriptionPlan = SubscriptionPlan.BUSINESS,
    val availableCredits: Int = 284,
    val totalCreditsUsed: Int = 16,
    val customerVisits: Int = 412,
    val createdAt: Long = System.currentTimeMillis()
) {
    val shareUrl: String
        get() = "https://fitlook.ai/shop/$slug"
}

enum class SubscriptionPlan(
    val title: String,
    val priceInr: Int,
    val billingPeriod: String,
    val totalCredits: Int,
    val maxProducts: String,
    val features: List<String>,
    val isPopular: Boolean = false
) {
    STARTER(
        title = "Starter",
        priceInr = 499,
        billingPeriod = "/month",
        totalCredits = 100,
        maxProducts = "50 Products",
        features = listOf(
            "100 AI Virtual Try-Ons",
            "Up to 50 Products Catalogue",
            "Basic Customer Try-On",
            "Direct WhatsApp Inquiries",
            "Auto-Delete Privacy Protection"
        )
    ),
    BUSINESS(
        title = "Business",
        priceInr = 999,
        billingPeriod = "/month",
        totalCredits = 300,
        maxProducts = "200 Products",
        features = listOf(
            "300 AI Virtual Try-Ons",
            "Up to 200 Products Catalogue",
            "WhatsApp Instant Sales Flow",
            "Shareable Digital Catalogue & QR",
            "Real-Time Sales & Try-On Analytics",
            "Priority AI Queue"
        ),
        isPopular = true
    ),
    PRO(
        title = "Pro",
        priceInr = 1999,
        billingPeriod = "/month",
        totalCredits = 1000,
        maxProducts = "Unlimited",
        features = listOf(
            "1,000 AI Virtual Try-Ons",
            "Unlimited Product Catalogue",
            "Advanced Analytics & Customer Trends",
            "Custom Branded QR & Watermark Removal",
            "Dedicated Account Manager",
            "Fastest Ultra-HD AI Generation"
        )
    );

    val monthlyPriceInr: Int
        get() = priceInr

    val tagline: String
        get() = maxProducts
}

enum class AutoDeleteDuration(val minutes: Long, val label: String, val shortLabel: String) {
    FIVE_MINUTES(5, "5 Minutes", "5m"),
    TEN_MINUTES(10, "10 Minutes (Recommended)", "10m"),
    ONE_HOUR(60, "1 Hour", "1h"),
    TWENTY_FOUR_HOURS(1440, "24 Hours", "24h");

    val millis: Long
        get() = minutes * 60 * 1000L

    companion object {
        fun fromMinutes(min: Long): AutoDeleteDuration {
            return entries.firstOrNull { it.minutes == min } ?: TEN_MINUTES
        }
    }
}

data class ShopAnalytics(
    val totalProducts: Int,
    val totalTryOns: Int,
    val totalVisits: Int,
    val mostTriedProduct: ProductItem?,
    val mostViewedProduct: ProductItem?,
    val availableCredits: Int,
    val currentPlan: SubscriptionPlan
)
