package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.data.model.UserMode
import com.example.ui.components.FitLookBottomBar
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MyLooksScreen
import com.example.ui.screens.PrivacyConsentDialog
import com.example.ui.screens.ProcessingScreen
import com.example.ui.screens.ProductCatalogueScreen
import com.example.ui.screens.ProductDetailScreen
import com.example.ui.screens.ProfilePrivacyScreen
import com.example.ui.screens.ShopDashboardScreen
import com.example.ui.screens.ShopInventoryScreen
import com.example.ui.screens.ShopPricingScreen
import com.example.ui.screens.ShopProfileEditScreen
import com.example.ui.screens.ShopQrCodeScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.TryOnResultScreen
import com.example.ui.screens.TryOnWizardScreen
import com.example.ui.screens.WelcomeScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.FitLookScreen
import com.example.ui.viewmodel.FitLookViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {

    private val viewModel: FitLookViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                FitLookApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun FitLookApp(viewModel: FitLookViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val userMode by viewModel.userMode.collectAsState()
    val shopProfile by viewModel.shopProfile.collectAsState()
    val shopProducts by viewModel.shopProducts.collectAsState()
    val filteredProducts by viewModel.filteredProducts.collectAsState()
    val selectedProductCategory by viewModel.selectedProductCategory.collectAsState()
    val productSearchQuery by viewModel.productSearchQuery.collectAsState()
    val shopAnalytics by viewModel.shopAnalytics.collectAsState()
    val autoDeleteDuration by viewModel.autoDeleteDuration.collectAsState()
    val isProcessingPayment by viewModel.isProcessingPayment.collectAsState()
    val allLooks by viewModel.allLooks.collectAsState()
    val selectedPersonCategory by viewModel.selectedPersonCategory.collectAsState()
    val selectedClothingCategory by viewModel.selectedClothingCategory.collectAsState()
    val personImageUri by viewModel.personImageUri.collectAsState()
    val clothingImageUri by viewModel.clothingImageUri.collectAsState()
    val outfitDescription by viewModel.outfitDescription.collectAsState()
    val selectedFitStyle by viewModel.selectedFitStyle.collectAsState()
    val tryOnStage by viewModel.tryOnStage.collectAsState()
    val showPrivacyDialog by viewModel.showPrivacyConsentDialog.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.snackBarMessage.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    val showBottomNav = when (currentScreen) {
        is FitLookScreen.Splash, is FitLookScreen.Welcome, is FitLookScreen.Processing, is FitLookScreen.TryOnWizard -> false
        else -> true
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            if (showBottomNav) {
                FitLookBottomBar(
                    currentScreen = currentScreen,
                    userMode = userMode,
                    onNavigateHome = { viewModel.navigateTo(FitLookScreen.Home) },
                    onNavigateCatalogue = { viewModel.navigateTo(FitLookScreen.ProductCatalogue) },
                    onNavigateTryOn = { viewModel.startTryOnWizard() },
                    onNavigateMyLooks = { viewModel.navigateTo(FitLookScreen.MyLooks) },
                    onNavigateDashboard = { viewModel.navigateTo(FitLookScreen.ShopDashboard) },
                    onNavigateInventory = { viewModel.navigateTo(FitLookScreen.ShopInventory) },
                    onNavigateQr = { viewModel.navigateTo(FitLookScreen.ShopQrCode) },
                    onNavigatePricing = { viewModel.navigateTo(FitLookScreen.ShopPricing) },
                    onNavigateProfile = { viewModel.navigateTo(FitLookScreen.ProfilePrivacy) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screen_transition"
            ) { screen ->
                when (screen) {
                    is FitLookScreen.Splash -> {
                        SplashScreen(
                            onSplashFinished = {
                                if (currentUser != null) {
                                    if (userMode == UserMode.SHOP_OWNER) {
                                        viewModel.navigateTo(FitLookScreen.ShopDashboard)
                                    } else {
                                        viewModel.navigateTo(FitLookScreen.Home)
                                    }
                                } else {
                                    viewModel.navigateTo(FitLookScreen.Welcome)
                                }
                            }
                        )
                    }

                    is FitLookScreen.Welcome -> {
                        WelcomeScreen(
                            currentMode = userMode,
                            onSelectMode = { mode -> viewModel.setUserMode(mode) },
                            onContinueGoogle = { viewModel.loginWithGoogle() },
                            onContinuePhone = { phone -> viewModel.loginWithPhone(phone) },
                            onContinueGuest = { viewModel.continueAsGuest() }
                        )
                    }

                    // CUSTOMER SCREENS
                    is FitLookScreen.Home -> {
                        HomeScreen(
                            currentUser = currentUser,
                            recentLooks = allLooks,
                            featuredProducts = shopProducts,
                            onStartTryOn = { viewModel.startTryClothesOnMyPhoto() },
                            onViewMyLooks = { viewModel.navigateTo(FitLookScreen.MyLooks) },
                            onViewCatalogue = { viewModel.navigateTo(FitLookScreen.ProductCatalogue) },
                            onSelectProduct = { prod -> viewModel.viewProductDetail(prod) },
                            onViewProfilePrivacy = { viewModel.navigateTo(FitLookScreen.ProfilePrivacy) },
                            onSelectQuickCategory = { cat ->
                                viewModel.selectPersonCategory(cat)
                                viewModel.startTryClothesOnMyPhoto()
                            },
                            onSelectOutfitForTryOn = { outfit ->
                                viewModel.startTryClothesOnMyPhoto(outfit)
                            },
                            onViewLookDetail = { look ->
                                viewModel.navigateTo(FitLookScreen.Result(look))
                            }
                        )
                    }

                    is FitLookScreen.ProductCatalogue -> {
                        ProductCatalogueScreen(
                            shopProfile = shopProfile,
                            products = filteredProducts,
                            selectedCategory = selectedProductCategory,
                            searchQuery = productSearchQuery,
                            onCategorySelected = { cat -> viewModel.filterProductCategory(cat) },
                            onSearchQueryChanged = { q -> viewModel.setProductSearchQuery(q) },
                            onProductClick = { prod -> viewModel.viewProductDetail(prod) },
                            onTryOnProduct = { prod -> viewModel.startProductTryOn(prod) },
                            onContactShop = { prod -> viewModel.contactShopForProduct(prod) }
                        )
                    }

                    is FitLookScreen.ProductDetail -> {
                        ProductDetailScreen(
                            product = screen.product,
                            shopProfile = shopProfile,
                            onBack = { viewModel.navigateTo(FitLookScreen.ProductCatalogue) },
                            onTryOn = { viewModel.startProductTryOn(screen.product) },
                            onContactWhatsApp = { viewModel.contactShopForProduct(screen.product) }
                        )
                    }

                    is FitLookScreen.TryOnWizard -> {
                        TryOnWizardScreen(
                            currentStep = screen.step,
                            selectedPersonCategory = selectedPersonCategory,
                            selectedClothingCategory = selectedClothingCategory,
                            personImageUri = personImageUri,
                            clothingImageUri = clothingImageUri,
                            outfitDescription = outfitDescription,
                            selectedFitStyle = selectedFitStyle,
                            onSelectPersonCategory = { cat -> viewModel.selectPersonCategory(cat) },
                            onSelectClothingCategory = { clothing -> viewModel.selectClothingCategory(clothing) },
                            onSetPersonImage = { uri -> viewModel.setPersonImage(uri) },
                            onSetClothingImage = { uri -> viewModel.setClothingImage(uri) },
                            onSetOutfitDescription = { desc -> viewModel.setOutfitDescription(desc) },
                            onSelectFitStyle = { fit -> viewModel.selectFitStyle(fit) },
                            onSaveCameraBitmap = { bitmap -> viewModel.saveCapturedBitmap(bitmap) },
                            onNavigateStep = { step -> viewModel.navigateTo(FitLookScreen.TryOnWizard(step)) },
                            onExecuteTryOn = { viewModel.executeVirtualTryOn() },
                            onBackClick = { viewModel.navigateTo(FitLookScreen.Home) }
                        )
                    }

                    is FitLookScreen.Processing -> {
                        ProcessingScreen(
                            stage = tryOnStage,
                            onRetry = { viewModel.executeVirtualTryOn() },
                            onReturnToPrevious = { viewModel.navigateTo(FitLookScreen.TryOnWizard(5)) }
                        )
                    }

                    is FitLookScreen.Result -> {
                        TryOnResultScreen(
                            resultData = screen.resultData,
                            onSaveToggle = { look -> viewModel.toggleSaveLook(look) },
                            onDownload = { look -> viewModel.downloadActiveLook(look) },
                            onShare = { look -> viewModel.shareActiveLook(look) },
                            onContactShopOnWhatsApp = { look ->
                                val matchingProduct = shopProducts.firstOrNull { it.id == look.productId }
                                if (matchingProduct != null) {
                                    viewModel.contactShopViaWhatsApp(matchingProduct)
                                } else {
                                    viewModel.contactShopViaWhatsAppGeneric(look)
                                }
                            },
                            onTryAnotherOutfit = { viewModel.resetWizardForNewOutfit() },
                            onDeleteNow = { look -> viewModel.deleteLookNow(look) },
                            onBackClick = { viewModel.navigateTo(FitLookScreen.Home) }
                        )
                    }

                    is FitLookScreen.MyLooks -> {
                        MyLooksScreen(
                            looks = allLooks,
                            autoDeleteService = viewModel.autoDeleteService,
                            onViewLook = { look -> viewModel.navigateTo(FitLookScreen.Result(look)) },
                            onDownloadLook = { look -> viewModel.downloadActiveLook(look) },
                            onShareLook = { look -> viewModel.shareActiveLook(look) },
                            onDeleteLook = { look -> viewModel.deleteLookNow(look) },
                            onDeleteAllServerData = { viewModel.deleteAllServerDataNow() },
                            onStartTryOn = { viewModel.startTryOnWizard() }
                        )
                    }

                    // SHOP OWNER SCREENS
                    is FitLookScreen.ShopDashboard -> {
                        ShopDashboardScreen(
                            shopProfile = shopProfile,
                            analytics = shopAnalytics,
                            allProducts = shopProducts,
                            onManageInventory = { viewModel.navigateTo(FitLookScreen.ShopInventory) },
                            onViewPricing = { viewModel.navigateTo(FitLookScreen.ShopPricing) },
                            onViewQrCode = { viewModel.navigateTo(FitLookScreen.ShopQrCode) },
                            onEditProfile = { viewModel.navigateTo(FitLookScreen.ShopProfileEdit) },
                            onShareCatalogue = { viewModel.shareShopCatalogue() },
                            onSwitchToCustomerMode = { viewModel.setUserMode(UserMode.CUSTOMER) }
                        )
                    }

                    is FitLookScreen.ShopInventory -> {
                        ShopInventoryScreen(
                            products = shopProducts,
                            onBack = { viewModel.navigateTo(FitLookScreen.ShopDashboard) },
                            onAddProduct = { name, price, cat, sizes, desc, img ->
                                viewModel.addProduct(name, price, cat, sizes, desc, img)
                            },
                            onUpdateProduct = { prod ->
                                viewModel.updateProduct(prod)
                            },
                            onDeleteProduct = { prodId ->
                                viewModel.deleteProduct(prodId)
                            }
                        )
                    }

                    is FitLookScreen.ShopQrCode -> {
                        ShopQrCodeScreen(
                            shopProfile = shopProfile,
                            onBack = { viewModel.navigateTo(FitLookScreen.ShopDashboard) },
                            onShare = { viewModel.shareShopQr(shopProfile) }
                        )
                    }

                    is FitLookScreen.ShopPricing -> {
                        ShopPricingScreen(
                            currentShop = shopProfile,
                            isProcessingPayment = isProcessingPayment,
                            onBack = { viewModel.navigateTo(FitLookScreen.ShopDashboard) },
                            onSelectPlan = { plan -> viewModel.purchaseSubscriptionPlan(plan) }
                        )
                    }

                    is FitLookScreen.ShopProfileEdit -> {
                        ShopProfileEditScreen(
                            currentShop = shopProfile,
                            onBack = { viewModel.navigateTo(FitLookScreen.ShopDashboard) },
                            onSaveProfile = { name, phone, whatsapp, location, desc, logo ->
                                viewModel.updateShopProfile(name, phone, whatsapp, location, desc, logo)
                            }
                        )
                    }

                    // COMMON SCREENS
                    is FitLookScreen.ProfilePrivacy -> {
                        ProfilePrivacyScreen(
                            currentUser = currentUser,
                            currentMode = userMode,
                            selectedAutoDeleteDuration = autoDeleteDuration,
                            privacyService = viewModel.privacyService,
                            onSelectMode = { mode -> viewModel.setUserMode(mode) },
                            onSelectAutoDeleteDuration = { dur -> viewModel.setAutoDeleteDuration(dur) },
                            onDeleteAllServerData = { viewModel.deleteAllServerDataNow() },
                            onLogout = {
                                viewModel.authService.logout()
                                viewModel.navigateTo(FitLookScreen.Welcome)
                            }
                        )
                    }
                }
            }

            if (showPrivacyDialog) {
                PrivacyConsentDialog(
                    onAccept = { viewModel.acceptPrivacyConsent() },
                    onDismiss = { viewModel.dismissPrivacyDialog() }
                )
            }
        }
    }
}
