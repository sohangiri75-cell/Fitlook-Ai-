package com.example.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.AutoDeleteDuration
import com.example.data.model.FitStyle
import com.example.data.model.PersonCategory
import com.example.data.model.ProductCategory
import com.example.data.model.ProductItem
import com.example.data.model.ShopAnalytics
import com.example.data.model.ShopProfile
import com.example.data.model.SubscriptionPlan
import com.example.data.model.TryOnResultData
import com.example.data.model.TryOnStage
import com.example.data.model.UserAccount
import com.example.data.model.UserMode
import com.example.data.repository.LookRepository
import com.example.data.repository.LookRepositoryImpl
import com.example.data.repository.ShopRepository
import com.example.data.repository.ShopRepositoryImpl
import com.example.service.AuthenticationService
import com.example.service.AutoDeleteService
import com.example.service.DownloadService
import com.example.service.ImageUploadService
import com.example.service.PrivacyService
import com.example.service.ShareService
import com.example.service.VirtualTryOnService
import com.example.service.VirtualTryOnServiceImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

sealed interface FitLookScreen {
    data object Splash : FitLookScreen
    data object Welcome : FitLookScreen
    // Customer Screens
    data object Home : FitLookScreen
    data object ProductCatalogue : FitLookScreen
    data class ProductDetail(val product: ProductItem) : FitLookScreen
    data class TryOnWizard(val step: Int = 1) : FitLookScreen
    data object Processing : FitLookScreen
    data class Result(val resultData: TryOnResultData) : FitLookScreen
    data object MyLooks : FitLookScreen
    data object ProfilePrivacy : FitLookScreen
    // Shop Owner Screens
    data object ShopDashboard : FitLookScreen
    data object ShopInventory : FitLookScreen
    data object ShopPricing : FitLookScreen
    data object ShopQrCode : FitLookScreen
    data object ShopProfileEdit : FitLookScreen
}

class FitLookViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    val lookRepository: LookRepository = LookRepositoryImpl(database.lookDao())
    val shopRepository: ShopRepository = ShopRepositoryImpl(database.shopDao(), database.productDao())

    val authService = AuthenticationService(application)
    val imageUploadService = ImageUploadService(application)
    val virtualTryOnService: VirtualTryOnService = VirtualTryOnServiceImpl(application)
    val autoDeleteService = AutoDeleteService(application, lookRepository)
    val privacyService = PrivacyService(application)
    val downloadService = DownloadService(application)
    val shareService = ShareService(application)

    // User Mode (Customer vs Shop Owner)
    private val _userMode = MutableStateFlow(UserMode.CUSTOMER)
    val userMode: StateFlow<UserMode> = _userMode.asStateFlow()

    // Current Screen Navigation
    private val _currentScreen = MutableStateFlow<FitLookScreen>(FitLookScreen.Welcome)
    val currentScreen: StateFlow<FitLookScreen> = _currentScreen.asStateFlow()

    // Shop Profile & Catalogue Data
    val shopProfile: StateFlow<ShopProfile> = shopRepository.getShopProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ShopProfile())

    val allProducts: StateFlow<List<ProductItem>> = shopRepository.getAllProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val shopProducts: StateFlow<List<ProductItem>> = allProducts
    val autoDeleteDuration = autoDeleteService.selectedDuration

    // Catalogue category filter & search query
    private val _selectedProductCategory = MutableStateFlow<ProductCategory?>(null)
    val selectedProductCategory: StateFlow<ProductCategory?> = _selectedProductCategory.asStateFlow()

    private val _productSearchQuery = MutableStateFlow("")
    val productSearchQuery: StateFlow<String> = _productSearchQuery.asStateFlow()

    val filteredProducts: StateFlow<List<ProductItem>> = combine(
        allProducts,
        _selectedProductCategory,
        _productSearchQuery
    ) { products, category, query ->
        products.filter { product ->
            val matchCat = category == null || product.category == category
            val matchQuery = query.isBlank() || product.name.contains(query, ignoreCase = true) || product.category.displayName.contains(query, ignoreCase = true)
            matchCat && matchQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Selected product for try-on or detail view
    private val _selectedProductForTryOn = MutableStateFlow<ProductItem?>(null)
    val selectedProductForTryOn: StateFlow<ProductItem?> = _selectedProductForTryOn.asStateFlow()

    // Try-on wizard form inputs
    private val _selectedPersonCategory = MutableStateFlow(PersonCategory.MAN)
    val selectedPersonCategory: StateFlow<PersonCategory> = _selectedPersonCategory.asStateFlow()

    private val _selectedClothingCategory = MutableStateFlow("Sherwani")
    val selectedClothingCategory: StateFlow<String> = _selectedClothingCategory.asStateFlow()

    private val _personImageUri = MutableStateFlow<String?>("drawable/img_demo_person_man")
    val personImageUri: StateFlow<String?> = _personImageUri.asStateFlow()

    private val _clothingImageUri = MutableStateFlow<String?>("drawable/img_demo_clothing_sherwani")
    val clothingImageUri: StateFlow<String?> = _clothingImageUri.asStateFlow()

    private val _outfitDescription = MutableStateFlow<String>("")
    val outfitDescription: StateFlow<String> = _outfitDescription.asStateFlow()

    private val _selectedFitStyle = MutableStateFlow(FitStyle.REGULAR_FIT)
    val selectedFitStyle: StateFlow<FitStyle> = _selectedFitStyle.asStateFlow()

    // Try-on Processing and Result State
    private val _tryOnStage = MutableStateFlow<TryOnStage>(TryOnStage.Idle)
    val tryOnStage: StateFlow<TryOnStage> = _tryOnStage.asStateFlow()

    private val _activeResult = MutableStateFlow<TryOnResultData?>(null)
    val activeResult: StateFlow<TryOnResultData?> = _activeResult.asStateFlow()

    // Shop Owner Analytics State
    private val _shopAnalytics = MutableStateFlow<ShopAnalytics?>(null)
    val shopAnalytics: StateFlow<ShopAnalytics?> = _shopAnalytics.asStateFlow()

    // Payment Processing State for Shop Subscriptions
    private val _isProcessingPayment = MutableStateFlow(false)
    val isProcessingPayment: StateFlow<Boolean> = _isProcessingPayment.asStateFlow()

    // UI Feedback Message
    private val _snackBarMessage = MutableSharedFlow<String>()
    val snackBarMessage: SharedFlow<String> = _snackBarMessage.asSharedFlow()

    // Privacy Consent Dialog
    private val _showPrivacyConsentDialog = MutableStateFlow(false)
    val showPrivacyConsentDialog: StateFlow<Boolean> = _showPrivacyConsentDialog.asStateFlow()

    // Looks List
    val allLooks: StateFlow<List<TryOnResultData>> = lookRepository.getAllLooks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currentUser: StateFlow<UserAccount?> = authService.currentUser

    init {
        // Run periodic auto-deletion check
        autoDeleteService.triggerPeriodicCleanup(viewModelScope)
        // Seed initial shop boutique data & products
        viewModelScope.launch {
            shopRepository.seedInitialDataIfNeeded()
            refreshShopAnalytics()
        }
    }

    fun setUserMode(mode: UserMode) {
        _userMode.value = mode
        if (mode == UserMode.SHOP_OWNER) {
            refreshShopAnalytics()
            navigateTo(FitLookScreen.ShopDashboard)
        } else {
            navigateTo(FitLookScreen.Home)
        }
    }

    fun navigateTo(screen: FitLookScreen) {
        _currentScreen.value = screen
    }

    fun continueAsGuest() {
        authService.continueAsGuest()
        if (_userMode.value == UserMode.SHOP_OWNER) {
            navigateTo(FitLookScreen.ShopDashboard)
        } else {
            navigateTo(FitLookScreen.Home)
        }
    }

    fun loginWithGoogle() {
        authService.loginWithGoogle("Fashion Boutique Owner", "store.fitlook@gmail.com")
        if (_userMode.value == UserMode.SHOP_OWNER) {
            navigateTo(FitLookScreen.ShopDashboard)
        } else {
            navigateTo(FitLookScreen.Home)
        }
    }

    fun loginWithPhone(phone: String) {
        if (phone.length >= 10) {
            authService.loginWithPhone(phone)
            if (_userMode.value == UserMode.SHOP_OWNER) {
                navigateTo(FitLookScreen.ShopDashboard)
            } else {
                navigateTo(FitLookScreen.Home)
            }
        } else {
            showToast("Please enter a valid 10-digit mobile number")
        }
    }

    // Customer Catalogue Actions
    fun filterProductCategory(category: ProductCategory?) {
        _selectedProductCategory.value = category
    }

    fun setProductSearchQuery(query: String) {
        _productSearchQuery.value = query
    }

    fun viewProductDetail(product: ProductItem) {
        viewModelScope.launch {
            shopRepository.recordProductView(product.id)
        }
        navigateTo(FitLookScreen.ProductDetail(product))
    }

    fun startTryClothesOnMyPhoto(outfit: String? = null) {
        _selectedProductForTryOn.value = null
        if (!outfit.isNullOrBlank()) {
            _selectedClothingCategory.value = outfit
            // Auto set smart sample reference garment or description if matching
            when (outfit) {
                "Pant + Shirt", "Shirt" -> {
                    _clothingImageUri.value = "drawable/img_product_shirt"
                    if (_outfitDescription.value.isBlank()) _outfitDescription.value = "Classic tailored shirt with formal trousers"
                }
                "T-Shirt + Jeans" -> {
                    _clothingImageUri.value = "drawable/img_product_shirt"
                    if (_outfitDescription.value.isBlank()) _outfitDescription.value = "Casual cotton t-shirt with slim denim jeans"
                }
                "Formal Suit" -> {
                    _clothingImageUri.value = "drawable/img_product_jacket"
                    if (_outfitDescription.value.isBlank()) _outfitDescription.value = "Midnight blue 3-piece formal suit with tailored blazer"
                }
                "Jacket" -> {
                    _clothingImageUri.value = "drawable/img_product_jacket"
                    if (_outfitDescription.value.isBlank()) _outfitDescription.value = "Stylish leather bomber jacket"
                }
                "Traditional Clothes", "Kurta Pajama" -> {
                    _clothingImageUri.value = "drawable/img_demo_clothing_sherwani"
                    if (_outfitDescription.value.isBlank()) _outfitDescription.value = "Royal designer embroidered sherwani"
                }
                "Saree" -> {
                    _clothingImageUri.value = "drawable/img_product_saree"
                    if (_outfitDescription.value.isBlank()) _outfitDescription.value = "Banarasi silk festive designer saree"
                }
                "Dress" -> {
                    _clothingImageUri.value = "drawable/img_product_dress"
                    if (_outfitDescription.value.isBlank()) _outfitDescription.value = "Elegant evening dress"
                }
            }
        }
        if (!privacyService.hasAcceptedConsent()) {
            _showPrivacyConsentDialog.value = true
        } else {
            navigateTo(FitLookScreen.TryOnWizard(1))
        }
    }

    fun startProductTryOn(product: ProductItem) {
        _selectedProductForTryOn.value = product
        _clothingImageUri.value = product.primaryImageUri
        _selectedClothingCategory.value = product.category.displayName

        if (!privacyService.hasAcceptedConsent()) {
            _showPrivacyConsentDialog.value = true
        } else {
            navigateTo(FitLookScreen.TryOnWizard(1))
        }
    }

    fun selectPersonCategory(category: PersonCategory) {
        _selectedPersonCategory.value = category
        if (_personImageUri.value?.startsWith("drawable/img_demo_person") == true) {
            _personImageUri.value = when (category) {
                PersonCategory.MAN, PersonCategory.BOY -> "drawable/img_demo_person_man"
                else -> "drawable/img_demo_person_woman"
            }
        }
        if (_selectedProductForTryOn.value == null) {
            val categories = com.example.data.model.CategoryCatalog.getClothingCategoriesFor(category)
            _selectedClothingCategory.value = categories.firstOrNull() ?: "Shirt"
        }
    }

    fun selectClothingCategory(clothing: String) {
        _selectedClothingCategory.value = clothing
    }

    fun setPersonImage(uri: String?) {
        _personImageUri.value = uri
    }

    fun setClothingImage(uri: String?) {
        _clothingImageUri.value = uri
        _selectedProductForTryOn.value = null
    }

    fun setOutfitDescription(description: String) {
        _outfitDescription.value = description
    }

    fun saveCapturedBitmap(bitmap: Bitmap): String {
        return try {
            val cacheDir = getApplication<Application>().cacheDir
            val cameraFile = File(cacheDir, "camera_photo_${System.currentTimeMillis()}.jpg")
            FileOutputStream(cameraFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }
            Uri.fromFile(cameraFile).toString()
        } catch (e: Exception) {
            "drawable/img_demo_person_man"
        }
    }

    fun selectFitStyle(style: FitStyle) {
        _selectedFitStyle.value = style
    }

    fun startTryOnWizard() {
        if (!privacyService.hasAcceptedConsent()) {
            _showPrivacyConsentDialog.value = true
        } else {
            navigateTo(FitLookScreen.TryOnWizard(1))
        }
    }

    fun acceptPrivacyConsent() {
        privacyService.acceptConsent()
        authService.setPrivacyConsent(true)
        _showPrivacyConsentDialog.value = false
        navigateTo(FitLookScreen.TryOnWizard(1))
    }

    fun dismissPrivacyDialog() {
        _showPrivacyConsentDialog.value = false
    }

    fun setAutoDeleteDuration(duration: AutoDeleteDuration) {
        autoDeleteService.setAutoDeleteDuration(duration)
        showToast("Auto-delete set to ${duration.label} 🔒")
    }

    fun executeVirtualTryOn() {
        val personUri = _personImageUri.value
        val clothingUri = _clothingImageUri.value
        val outfitDesc = _outfitDescription.value.trim()
        val currentShop = shopProfile.value

        // Check shop available AI credits
        if (currentShop.availableCredits <= 0) {
            showToast("AI Try-On credits exhausted. Please upgrade your shop plan or top up credits.")
            return
        }

        // Validate Person Image
        val personValidation = imageUploadService.validatePersonImage(personUri)
        if (!personValidation.isValid) {
            showToast(personValidation.errorMessage ?: "Please upload a clearer full-body photo.")
            return
        }

        // Validate Clothing: Either clothing image or text description is required
        if (clothingUri.isNullOrBlank() && outfitDesc.isBlank()) {
            showToast("Please upload a clothing image or write an outfit description.")
            return
        }

        navigateTo(FitLookScreen.Processing)
        _tryOnStage.value = TryOnStage.Processing("Analyzing Customer Photo & Garment...", 0.20f)

        val product = _selectedProductForTryOn.value
        val autoDeleteDur = autoDeleteService.selectedDuration.value

        viewModelScope.launch {
            if (product != null) {
                shopRepository.recordProductTryOn(product.id)
            }

            val result = virtualTryOnService.generateTryOn(
                personImageUri = personUri!!,
                clothingImageUri = clothingUri,
                outfitDescription = outfitDesc.ifBlank { null },
                personCategory = _selectedPersonCategory.value,
                clothingCategory = _selectedClothingCategory.value,
                fitStyle = _selectedFitStyle.value,
                productItem = product,
                shopProfile = currentShop,
                autoDeleteDuration = autoDeleteDur
            ) { stageName, progress ->
                _tryOnStage.value = TryOnStage.Processing(stageName, progress)
            }

            result.onSuccess { data ->
                // Deduct 1 credit strictly on successful AI generation
                shopRepository.deductCredit()
                _activeResult.value = data
                _tryOnStage.value = TryOnStage.Success(data)
                lookRepository.insertLook(data)
                refreshShopAnalytics()
                navigateTo(FitLookScreen.Result(data))
            }.onFailure { error ->
                // Do NOT deduct credit on failure
                _tryOnStage.value = TryOnStage.Error(
                    message = error.localizedMessage ?: "Virtual try-on processing failed. Your credits remain safe."
                )
            }
        }
    }


    fun toggleSaveLook(look: TryOnResultData) {
        viewModelScope.launch {
            val newSavedState = !look.isSaved
            lookRepository.setLookSaved(look.lookId, newSavedState)
            _activeResult.value = _activeResult.value?.copy(isSaved = newSavedState)
            showToast(if (newSavedState) "Saved to My Looks ❤️" else "Removed from Saved Looks")
        }
    }

    fun downloadActiveLook(look: TryOnResultData) {
        viewModelScope.launch {
            val result = downloadService.saveImageToGallery(look.resultImageUri, look.productName ?: look.clothingCategory)
            result.onSuccess { msg ->
                showToast(msg)
            }.onFailure { err ->
                showToast("Download failed: ${err.localizedMessage}")
            }
        }
    }

    fun shareActiveLook(look: TryOnResultData) {
        viewModelScope.launch {
            shareService.shareLook(look)
        }
    }

    fun contactShopOnWhatsApp(look: TryOnResultData) {
        val targetShop = shopProfile.value
        val number = look.shopWhatsapp ?: targetShop.whatsappNumber
        val productName = look.productName ?: look.clothingCategory
        val price = look.productPrice ?: 2499.0
        val shopName = look.shopName ?: targetShop.name

        shareService.contactShopOnWhatsApp(
            whatsappNumber = number,
            productName = productName,
            productPrice = price,
            shopName = shopName
        )
    }

    fun contactShopForProduct(product: ProductItem) {
        val targetShop = shopProfile.value
        shareService.contactShopOnWhatsApp(
            whatsappNumber = targetShop.whatsappNumber,
            productName = product.name,
            productPrice = product.price,
            shopName = targetShop.name
        )
    }

    fun shareShopCatalogue() {
        shareService.shareShopCatalogue(shopProfile.value)
    }

    fun shareShopQr(shop: ShopProfile) {
        shareService.shareShopCatalogue(shop)
    }

    fun contactShopViaWhatsApp(product: ProductItem) {
        contactShopForProduct(product)
    }

    fun contactShopViaWhatsAppGeneric(look: TryOnResultData) {
        contactShopOnWhatsApp(look)
    }

    fun startTryOnWithProduct(product: ProductItem) {
        startProductTryOn(product)
    }

    fun deleteLookNow(look: TryOnResultData) {
        viewModelScope.launch {
            autoDeleteService.deleteNow(look.lookId)
            showToast("Look and uploaded photos permanently deleted 🗑️")
            navigateTo(FitLookScreen.Home)
        }
    }

    fun deleteAllServerDataNow() {
        viewModelScope.launch {
            autoDeleteService.deleteAllServerData()
            showToast("All unsaved photos & AI looks permanently purged from server 🗑️")
        }
    }

    fun resetWizardForNewOutfit() {
        _selectedProductForTryOn.value = null
        _clothingImageUri.value = "drawable/img_demo_clothing_sherwani"
        _tryOnStage.value = TryOnStage.Idle
        navigateTo(FitLookScreen.TryOnWizard(1))
    }

    // Shop Owner Functions
    fun updateShopProfile(
        name: String,
        phone: String,
        whatsapp: String,
        location: String,
        description: String,
        logoUri: String? = null
    ) {
        viewModelScope.launch {
            val current = shopProfile.value
            val cleanSlug = name.trim().lowercase().replace(Regex("[^a-z0-9]+"), "-")
            val updated = current.copy(
                name = name,
                slug = if (cleanSlug.isNotBlank()) cleanSlug else current.slug,
                phone = phone,
                whatsappNumber = whatsapp.replace(Regex("[^0-9]"), ""),
                location = location,
                description = description,
                logoUri = logoUri ?: current.logoUri
            )
            shopRepository.updateShopProfile(updated)
            showToast("Shop Profile updated successfully ✨")
            refreshShopAnalytics()
            navigateTo(FitLookScreen.ShopDashboard)
        }
    }

    fun addProduct(
        name: String,
        price: Double,
        category: ProductCategory,
        sizes: List<String>,
        description: String,
        imageUri: String
    ) {
        viewModelScope.launch {
            val newProduct = ProductItem(
                id = "prod_" + UUID.randomUUID().toString().take(8),
                name = name,
                price = price,
                category = category,
                availableSizes = if (sizes.isNotEmpty()) sizes else listOf("Free Size"),
                description = description,
                primaryImageUri = imageUri
            )
            shopRepository.addProduct(newProduct)
            showToast("Product added to catalogue 🎉")
            refreshShopAnalytics()
            navigateTo(FitLookScreen.ShopInventory)
        }
    }

    fun updateProduct(product: ProductItem) {
        viewModelScope.launch {
            shopRepository.updateProduct(product)
            showToast("Product updated successfully ✏️")
            refreshShopAnalytics()
        }
    }

    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            shopRepository.deleteProduct(productId)
            showToast("Product deleted from catalogue 🗑️")
            refreshShopAnalytics()
        }
    }

    fun purchaseSubscriptionPlan(plan: SubscriptionPlan) {
        viewModelScope.launch {
            _isProcessingPayment.value = true
            // Simulate standard Indian payment gateway verification (Razorpay/Cashfree/UPI)
            delay(1800)
            shopRepository.updateSubscriptionPlan(plan)
            _isProcessingPayment.value = false
            showToast("Successfully upgraded to ${plan.title} Plan! ${plan.totalCredits} AI Credits added 🚀")
            refreshShopAnalytics()
            navigateTo(FitLookScreen.ShopDashboard)
        }
    }

    fun refreshShopAnalytics() {
        viewModelScope.launch {
            _shopAnalytics.value = shopRepository.getAnalytics()
        }
    }

    private fun showToast(message: String) {
        viewModelScope.launch {
            _snackBarMessage.emit(message)
        }
    }
}
