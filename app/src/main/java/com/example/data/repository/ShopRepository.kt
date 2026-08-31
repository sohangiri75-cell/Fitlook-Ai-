package com.example.data.repository

import com.example.data.local.ProductDao
import com.example.data.local.ProductEntity
import com.example.data.local.ShopDao
import com.example.data.local.ShopEntity
import com.example.data.model.ProductCategory
import com.example.data.model.ProductItem
import com.example.data.model.ShopAnalytics
import com.example.data.model.ShopProfile
import com.example.data.model.SubscriptionPlan
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

interface ShopRepository {
    fun getShopProfile(): Flow<ShopProfile>
    suspend fun updateShopProfile(profile: ShopProfile)
    fun getAllProducts(): Flow<List<ProductItem>>
    suspend fun getProductById(id: String): ProductItem?
    fun getProductsByCategory(category: ProductCategory): Flow<List<ProductItem>>
    suspend fun addProduct(product: ProductItem)
    suspend fun updateProduct(product: ProductItem)
    suspend fun deleteProduct(id: String)
    suspend fun recordProductTryOn(productId: String)
    suspend fun recordProductView(productId: String)
    suspend fun deductCredit(): Boolean
    suspend fun addCredits(credits: Int)
    suspend fun updateSubscriptionPlan(plan: SubscriptionPlan)
    suspend fun seedInitialDataIfNeeded()
    suspend fun getAnalytics(): ShopAnalytics
}

class ShopRepositoryImpl(
    private val shopDao: ShopDao,
    private val productDao: ProductDao
) : ShopRepository {

    override fun getShopProfile(): Flow<ShopProfile> {
        return shopDao.getShopFlow().map { entity ->
            entity?.toModel() ?: ShopProfile()
        }
    }

    override suspend fun updateShopProfile(profile: ShopProfile) {
        shopDao.insertOrUpdateShop(profile.toEntity())
    }

    override fun getAllProducts(): Flow<List<ProductItem>> {
        return productDao.getAllProducts().map { entities ->
            entities.map { it.toModel() }
        }
    }

    override suspend fun getProductById(id: String): ProductItem? {
        return productDao.getProductById(id)?.toModel()
    }

    override fun getProductsByCategory(category: ProductCategory): Flow<List<ProductItem>> {
        return productDao.getProductsByCategory(category.name).map { entities ->
            entities.map { it.toModel() }
        }
    }

    override suspend fun addProduct(product: ProductItem) {
        productDao.insertProduct(product.toEntity())
    }

    override suspend fun updateProduct(product: ProductItem) {
        productDao.updateProduct(product.toEntity())
    }

    override suspend fun deleteProduct(id: String) {
        productDao.deleteProductById(id)
    }

    override suspend fun recordProductTryOn(productId: String) {
        productDao.incrementTryOnCount(productId)
    }

    override suspend fun recordProductView(productId: String) {
        productDao.incrementViewCount(productId)
        shopDao.incrementCustomerVisits()
    }

    override suspend fun deductCredit(): Boolean {
        val current = shopDao.getShop() ?: return false
        if (current.availableCredits > 0) {
            shopDao.deductCredit()
            return true
        }
        return false
    }

    override suspend fun addCredits(credits: Int) {
        shopDao.addCredits(credits)
    }

    override suspend fun updateSubscriptionPlan(plan: SubscriptionPlan) {
        shopDao.updatePlan(plan.name, plan.totalCredits)
    }

    override suspend fun getAnalytics(): ShopAnalytics {
        val shop = shopDao.getShop()?.toModel() ?: ShopProfile()
        val allProducts = productDao.getAllProducts().map { list -> list.map { it.toModel() } }
        val productList = allProducts.firstOrNull() ?: emptyList()
        val mostTried = productDao.getMostTriedProduct()?.toModel()
        val mostViewed = productDao.getMostViewedProduct()?.toModel()

        val totalTryOns = productList.sumOf { it.tryOnCount }

        return ShopAnalytics(
            totalProducts = productList.size,
            totalTryOns = totalTryOns,
            totalVisits = shop.customerVisits,
            mostTriedProduct = mostTried,
            mostViewedProduct = mostViewed,
            availableCredits = shop.availableCredits,
            currentPlan = shop.plan
        )
    }

    override suspend fun seedInitialDataIfNeeded() {
        val existingShop = shopDao.getShop()
        if (existingShop == null) {
            val defaultShop = ShopProfile()
            shopDao.insertOrUpdateShop(defaultShop.toEntity())
        }

        val existingProducts = productDao.getAllProducts()
        val currentList = existingProducts.firstOrNull()
        if (currentList.isNullOrEmpty()) {
            val initialProducts = listOf(
                ProductItem(
                    id = "prod_kurta_01",
                    name = "Royal Silk Embroidered Kurta",
                    price = 2499.0,
                    category = ProductCategory.KURTA,
                    availableSizes = listOf("M", "L", "XL", "XXL"),
                    description = "Handcrafted festive royal blue kurta with delicate zari embroidery on collar and placket. Breathable luxury silk blend.",
                    primaryImageUri = "drawable/img_product_kurta",
                    tryOnCount = 42,
                    viewCount = 138
                ),
                ProductItem(
                    id = "prod_saree_01",
                    name = "Banarasi Crimson Silk Saree",
                    price = 4999.0,
                    category = ProductCategory.SAREE,
                    availableSizes = listOf("Free Size"),
                    description = "Heritage crimson red pure Banarasi silk saree with ornate golden floral zari weave and rich pallu finish.",
                    primaryImageUri = "drawable/img_product_saree",
                    tryOnCount = 58,
                    viewCount = 189
                ),
                ProductItem(
                    id = "prod_shirt_01",
                    name = "Classic Crisp Oxford Formal Shirt",
                    price = 1299.0,
                    category = ProductCategory.SHIRT,
                    availableSizes = listOf("S", "M", "L", "XL"),
                    description = "100% Egyptian Giza cotton tailored business shirt. Wrinkle-resistant finish with structured spread collar.",
                    primaryImageUri = "drawable/img_product_shirt",
                    tryOnCount = 31,
                    viewCount = 94
                ),
                ProductItem(
                    id = "prod_jacket_01",
                    name = "Midnight Navy Tailored Blazer",
                    price = 5499.0,
                    category = ProductCategory.JACKET,
                    availableSizes = listOf("38", "40", "42", "44"),
                    description = "Italian cut slim-fit wool blend blazer jacket. Perfect for business meetings, evening galas, and formal receptions.",
                    primaryImageUri = "drawable/img_product_jacket",
                    tryOnCount = 27,
                    viewCount = 112
                ),
                ProductItem(
                    id = "prod_dress_01",
                    name = "Emerald Floral Cocktail Dress",
                    price = 3299.0,
                    category = ProductCategory.DRESS,
                    availableSizes = listOf("XS", "S", "M", "L"),
                    description = "Contemporary jewel-toned emerald green midi dress with subtle botanical embossing and elegant flutter sleeves.",
                    primaryImageUri = "drawable/img_product_dress",
                    tryOnCount = 49,
                    viewCount = 156
                ),
                ProductItem(
                    id = "prod_sherwani_01",
                    name = "Imperial Gold Royal Sherwani",
                    price = 8999.0,
                    category = ProductCategory.SUIT,
                    availableSizes = listOf("M", "L", "XL"),
                    description = "Grand bridal couture ivory and gold brocade sherwani with antique metal buttons and handcrafted embroidery.",
                    primaryImageUri = "drawable/img_demo_clothing_sherwani",
                    tryOnCount = 63,
                    viewCount = 205
                )
            )
            productDao.insertProducts(initialProducts.map { it.toEntity() })
        }
    }
}

private fun ShopEntity.toModel(): ShopProfile {
    val planEnum = try {
        SubscriptionPlan.valueOf(plan)
    } catch (e: Exception) {
        SubscriptionPlan.BUSINESS
    }
    return ShopProfile(
        shopId = shopId,
        name = name,
        slug = slug,
        logoUri = logoUri,
        phone = phone,
        whatsappNumber = whatsappNumber,
        description = description,
        location = location,
        plan = planEnum,
        availableCredits = availableCredits,
        totalCreditsUsed = totalCreditsUsed,
        customerVisits = customerVisits,
        createdAt = createdAt
    )
}

private fun ShopProfile.toEntity(): ShopEntity {
    return ShopEntity(
        shopId = shopId,
        name = name,
        slug = slug,
        logoUri = logoUri,
        phone = phone,
        whatsappNumber = whatsappNumber,
        description = description,
        location = location,
        plan = plan.name,
        availableCredits = availableCredits,
        totalCreditsUsed = totalCreditsUsed,
        customerVisits = customerVisits,
        createdAt = createdAt
    )
}

private fun ProductEntity.toModel(): ProductItem {
    val cat = ProductCategory.fromString(category)
    val sizes = if (sizesCsv.isNotBlank()) sizesCsv.split(",") else listOf("Free Size")
    val additionalImages = if (additionalImagesCsv.isNotBlank()) additionalImagesCsv.split(",") else emptyList()
    return ProductItem(
        id = id,
        shopId = shopId,
        name = name,
        price = price,
        category = cat,
        availableSizes = sizes,
        description = description,
        primaryImageUri = primaryImageUri,
        additionalImageUris = additionalImages,
        tryOnCount = tryOnCount,
        viewCount = viewCount,
        isAvailable = isAvailable,
        createdAt = createdAt
    )
}

private fun ProductItem.toEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        shopId = shopId,
        name = name,
        price = price,
        category = category.name,
        sizesCsv = availableSizes.joinToString(","),
        description = description,
        primaryImageUri = primaryImageUri,
        additionalImagesCsv = additionalImageUris.joinToString(","),
        tryOnCount = tryOnCount,
        viewCount = viewCount,
        isAvailable = isAvailable,
        createdAt = createdAt
    )
}
