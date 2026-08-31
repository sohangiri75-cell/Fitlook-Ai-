package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopDao {
    @Query("SELECT * FROM shop_profiles WHERE shopId = :shopId LIMIT 1")
    fun getShopFlow(shopId: String = "shop_default_1"): Flow<ShopEntity?>

    @Query("SELECT * FROM shop_profiles WHERE shopId = :shopId LIMIT 1")
    suspend fun getShop(shopId: String = "shop_default_1"): ShopEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateShop(shop: ShopEntity)

    @Query("UPDATE shop_profiles SET availableCredits = availableCredits - 1, totalCreditsUsed = totalCreditsUsed + 1 WHERE shopId = :shopId AND availableCredits > 0")
    suspend fun deductCredit(shopId: String = "shop_default_1")

    @Query("UPDATE shop_profiles SET availableCredits = availableCredits + :credits WHERE shopId = :shopId")
    suspend fun addCredits(credits: Int, shopId: String = "shop_default_1")

    @Query("UPDATE shop_profiles SET plan = :plan, availableCredits = :credits WHERE shopId = :shopId")
    suspend fun updatePlan(plan: String, credits: Int, shopId: String = "shop_default_1")

    @Query("UPDATE shop_profiles SET customerVisits = customerVisits + 1 WHERE shopId = :shopId")
    suspend fun incrementCustomerVisits(shopId: String = "shop_default_1")
}
