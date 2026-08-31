package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val shopId: String,
    val name: String,
    val price: Double,
    val category: String,
    val sizesCsv: String,
    val description: String,
    val primaryImageUri: String,
    val additionalImagesCsv: String = "",
    val tryOnCount: Int = 0,
    val viewCount: Int = 0,
    val isAvailable: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
