package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_profiles")
data class ShopEntity(
    @PrimaryKey val shopId: String,
    val name: String,
    val slug: String,
    val logoUri: String,
    val phone: String,
    val whatsappNumber: String,
    val description: String,
    val location: String,
    val plan: String,
    val availableCredits: Int,
    val totalCreditsUsed: Int,
    val customerVisits: Int,
    val createdAt: Long = System.currentTimeMillis()
)
