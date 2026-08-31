package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LookDao {
    @Query("SELECT * FROM looks ORDER BY createdAt DESC")
    fun getAllLooks(): Flow<List<LookEntity>>

    @Query("SELECT * FROM looks WHERE id = :id LIMIT 1")
    suspend fun getLookById(id: String): LookEntity?

    @Query("SELECT * FROM looks WHERE isSavedByUser = 1 ORDER BY createdAt DESC")
    fun getSavedLooks(): Flow<List<LookEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLook(look: LookEntity)

    @Update
    suspend fun updateLook(look: LookEntity)

    @Query("DELETE FROM looks WHERE id = :id")
    suspend fun deleteLookById(id: String)

    @Query("DELETE FROM looks WHERE isSavedByUser = 0")
    suspend fun deleteAllUnsavedServerLooks()

    @Query("DELETE FROM looks")
    suspend fun deleteAllLooks()

    @Query("DELETE FROM looks WHERE expiresAt < :currentTime AND isSavedByUser = 0")
    suspend fun deleteExpiredUnsavedLooks(currentTime: Long)
}
