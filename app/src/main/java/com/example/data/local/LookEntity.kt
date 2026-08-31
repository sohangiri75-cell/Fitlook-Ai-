package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "looks")
data class LookEntity(
    @PrimaryKey val id: String,
    val personImageUri: String,
    val clothingImageUri: String,
    val resultImageUri: String,
    val personCategory: String,
    val clothingCategory: String,
    val fitStyle: String,
    val createdAt: Long,
    val expiresAt: Long,
    val isDemoMode: Boolean = true,
    val isSavedByUser: Boolean = false,
    val productId: String? = null,
    val productName: String? = null,
    val productPrice: Double? = null,
    val productSizes: String? = null,
    val shopName: String? = null,
    val shopWhatsapp: String? = null
)
