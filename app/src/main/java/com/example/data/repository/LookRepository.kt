package com.example.data.repository

import com.example.data.local.LookDao
import com.example.data.local.LookEntity
import com.example.data.model.FitStyle
import com.example.data.model.PersonCategory
import com.example.data.model.TryOnResultData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface LookRepository {
    fun getAllLooks(): Flow<List<TryOnResultData>>
    fun getSavedLooks(): Flow<List<TryOnResultData>>
    suspend fun getLookById(id: String): TryOnResultData?
    suspend fun insertLook(result: TryOnResultData)
    suspend fun setLookSaved(id: String, saved: Boolean)
    suspend fun deleteLook(id: String)
    suspend fun deleteAllServerLooks()
    suspend fun cleanupExpiredLooks()
}

class LookRepositoryImpl(private val lookDao: LookDao) : LookRepository {

    override fun getAllLooks(): Flow<List<TryOnResultData>> {
        return lookDao.getAllLooks().map { entities ->
            entities.map { it.toModel() }
        }
    }

    override fun getSavedLooks(): Flow<List<TryOnResultData>> {
        return lookDao.getSavedLooks().map { entities ->
            entities.map { it.toModel() }
        }
    }

    override suspend fun getLookById(id: String): TryOnResultData? {
        return lookDao.getLookById(id)?.toModel()
    }

    override suspend fun insertLook(result: TryOnResultData) {
        lookDao.insertLook(result.toEntity())
    }

    override suspend fun setLookSaved(id: String, saved: Boolean) {
        val existing = lookDao.getLookById(id) ?: return
        lookDao.updateLook(existing.copy(isSavedByUser = saved))
    }

    override suspend fun deleteLook(id: String) {
        lookDao.deleteLookById(id)
    }

    override suspend fun deleteAllServerLooks() {
        lookDao.deleteAllUnsavedServerLooks()
    }

    override suspend fun cleanupExpiredLooks() {
        lookDao.deleteExpiredUnsavedLooks(System.currentTimeMillis())
    }
}

private fun LookEntity.toModel(): TryOnResultData {
    val personCat = PersonCategory.entries.find { it.id == personCategory } ?: PersonCategory.MAN
    val fit = FitStyle.entries.find { it.id == fitStyle } ?: FitStyle.REGULAR_FIT
    return TryOnResultData(
        lookId = id,
        personImageUri = personImageUri,
        clothingImageUri = clothingImageUri,
        resultImageUri = resultImageUri,
        personCategory = personCat,
        clothingCategory = clothingCategory,
        fitStyle = fit,
        createdAt = createdAt,
        expiresAt = expiresAt,
        isDemoMode = isDemoMode,
        isSaved = isSavedByUser,
        productId = productId,
        productName = productName,
        productPrice = productPrice,
        productSizes = productSizes,
        shopName = shopName,
        shopWhatsapp = shopWhatsapp
    )
}

private fun TryOnResultData.toEntity(): LookEntity {
    return LookEntity(
        id = lookId,
        personImageUri = personImageUri,
        clothingImageUri = clothingImageUri,
        resultImageUri = resultImageUri,
        personCategory = personCategory.id,
        clothingCategory = clothingCategory,
        fitStyle = fitStyle.id,
        createdAt = createdAt,
        expiresAt = expiresAt,
        isDemoMode = isDemoMode,
        isSavedByUser = isSaved,
        productId = productId,
        productName = productName,
        productPrice = productPrice,
        productSizes = productSizes,
        shopName = shopName,
        shopWhatsapp = shopWhatsapp
    )
}
