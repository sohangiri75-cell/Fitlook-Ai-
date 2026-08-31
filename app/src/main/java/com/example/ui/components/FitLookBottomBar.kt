package com.example.ui.components

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.outlined.Collections
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserMode
import com.example.ui.theme.EditorialBlue
import com.example.ui.theme.EditorialBlueContainer
import com.example.ui.theme.EditorialCardBorder
import com.example.ui.theme.EditorialSecondaryText
import com.example.ui.viewmodel.FitLookScreen

@Composable
fun FitLookBottomBar(
    currentScreen: FitLookScreen,
    userMode: UserMode,
    onNavigateHome: () -> Unit,
    onNavigateCatalogue: () -> Unit,
    onNavigateTryOn: () -> Unit,
    onNavigateMyLooks: () -> Unit,
    onNavigateDashboard: () -> Unit,
    onNavigateInventory: () -> Unit,
    onNavigateQr: () -> Unit,
    onNavigatePricing: () -> Unit,
    onNavigateProfile: () -> Unit
) {
    NavigationBar(
        modifier = Modifier
            .navigationBarsPadding()
            .drawBehind {
                drawLine(
                    color = EditorialCardBorder,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx()
                )
            }
            .testTag("fitlook_bottom_bar"),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        val itemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = EditorialBlue,
            selectedTextColor = EditorialBlue,
            indicatorColor = EditorialBlueContainer.copy(alpha = 0.6f),
            unselectedIconColor = EditorialSecondaryText.copy(alpha = 0.7f),
            unselectedTextColor = EditorialSecondaryText.copy(alpha = 0.7f)
        )

        if (userMode == UserMode.CUSTOMER) {
            val isHome = currentScreen is FitLookScreen.Home
            val isCatalogue = currentScreen is FitLookScreen.ProductCatalogue || currentScreen is FitLookScreen.ProductDetail
            val isTryOn = currentScreen is FitLookScreen.TryOnWizard || currentScreen is FitLookScreen.Processing || currentScreen is FitLookScreen.Result
            val isMyLooks = currentScreen is FitLookScreen.MyLooks
            val isProfile = currentScreen is FitLookScreen.ProfilePrivacy

            NavigationBarItem(
                selected = isHome,
                onClick = onNavigateHome,
                icon = { Icon(if (isHome) Icons.Filled.Home else Icons.Outlined.Home, "Home") },
                label = { Text("HOME", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp)) },
                colors = itemColors,
                modifier = Modifier.testTag("nav_item_home")
            )

            NavigationBarItem(
                selected = isCatalogue,
                onClick = onNavigateCatalogue,
                icon = { Icon(if (isCatalogue) Icons.Filled.ShoppingBag else Icons.Outlined.ShoppingBag, "Store") },
                label = { Text("STORE", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp)) },
                colors = itemColors,
                modifier = Modifier.testTag("nav_item_store")
            )

            NavigationBarItem(
                selected = isTryOn,
                onClick = onNavigateTryOn,
                icon = { Icon(Icons.Filled.AutoAwesome, "Try On") },
                label = { Text("TRY ON", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp)) },
                colors = itemColors,
                modifier = Modifier.testTag("nav_item_try_on")
            )

            NavigationBarItem(
                selected = isMyLooks,
                onClick = onNavigateMyLooks,
                icon = { Icon(if (isMyLooks) Icons.Filled.Collections else Icons.Outlined.Collections, "Looks") },
                label = { Text("LOOKS", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp)) },
                colors = itemColors,
                modifier = Modifier.testTag("nav_item_my_looks")
            )

            NavigationBarItem(
                selected = isProfile,
                onClick = onNavigateProfile,
                icon = { Icon(if (isProfile) Icons.Filled.Person else Icons.Outlined.Person, "Profile") },
                label = { Text("PROFILE", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp)) },
                colors = itemColors,
                modifier = Modifier.testTag("nav_item_profile")
            )
        } else {
            // SHOP OWNER NAVIGATION
            val isDashboard = currentScreen is FitLookScreen.ShopDashboard
            val isInventory = currentScreen is FitLookScreen.ShopInventory
            val isQr = currentScreen is FitLookScreen.ShopQrCode
            val isPricing = currentScreen is FitLookScreen.ShopPricing
            val isProfile = currentScreen is FitLookScreen.ProfilePrivacy || currentScreen is FitLookScreen.ShopProfileEdit

            NavigationBarItem(
                selected = isDashboard,
                onClick = onNavigateDashboard,
                icon = { Icon(if (isDashboard) Icons.Filled.Dashboard else Icons.Outlined.Dashboard, "Dashboard") },
                label = { Text("DASHBOARD", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 9.sp)) },
                colors = itemColors,
                modifier = Modifier.testTag("nav_item_shop_dashboard")
            )

            NavigationBarItem(
                selected = isInventory,
                onClick = onNavigateInventory,
                icon = { Icon(if (isInventory) Icons.Filled.Inventory2 else Icons.Outlined.Inventory2, "Inventory") },
                label = { Text("PRODUCTS", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 9.sp)) },
                colors = itemColors,
                modifier = Modifier.testTag("nav_item_shop_inventory")
            )

            NavigationBarItem(
                selected = isQr,
                onClick = onNavigateQr,
                icon = { Icon(if (isQr) Icons.Filled.QrCode else Icons.Outlined.QrCode, "Store QR") },
                label = { Text("QR CODE", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 9.sp)) },
                colors = itemColors,
                modifier = Modifier.testTag("nav_item_shop_qr")
            )

            NavigationBarItem(
                selected = isPricing,
                onClick = onNavigatePricing,
                icon = { Icon(if (isPricing) Icons.Filled.CreditCard else Icons.Outlined.CreditCard, "Plans") },
                label = { Text("PLANS", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 9.sp)) },
                colors = itemColors,
                modifier = Modifier.testTag("nav_item_shop_pricing")
            )

            NavigationBarItem(
                selected = isProfile,
                onClick = onNavigateProfile,
                icon = { Icon(if (isProfile) Icons.Filled.Storefront else Icons.Filled.Storefront, "Shop Profile") },
                label = { Text("STORE", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 9.sp)) },
                colors = itemColors,
                modifier = Modifier.testTag("nav_item_shop_profile")
            )
        }
    }
}
