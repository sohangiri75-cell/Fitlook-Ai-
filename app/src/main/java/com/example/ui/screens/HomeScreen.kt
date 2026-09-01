package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PersonCategory
import com.example.data.model.ProductItem
import com.example.data.model.TryOnResultData
import com.example.data.model.UserAccount
import com.example.ui.components.FitLookTopBar
import com.example.ui.components.SmartImage
import com.example.ui.theme.EditorialCardBg
import com.example.ui.theme.EditorialCardBorder
import com.example.ui.theme.FitLookDarkNavyBg
import com.example.ui.theme.FitLookDarkSurface
import com.example.ui.theme.FitLookGradientPrimary
import com.example.ui.theme.FitLookPink
import com.example.ui.theme.FitLookPinkLight
import com.example.ui.theme.FitLookPurple
import com.example.ui.theme.FitLookTextPrimary
import com.example.ui.theme.FitLookTextSecondary

@Composable
fun HomeScreen(
    currentUser: UserAccount?,
    recentLooks: List<TryOnResultData>,
    featuredProducts: List<ProductItem> = emptyList(),
    onStartTryOn: () -> Unit,
    onViewMyLooks: () -> Unit,
    onViewCatalogue: () -> Unit = {},
    onSelectProduct: (ProductItem) -> Unit = {},
    onViewProfilePrivacy: () -> Unit,
    onSelectQuickCategory: (PersonCategory) -> Unit,
    onSelectOutfitForTryOn: (String) -> Unit = {},
    onViewLookDetail: (TryOnResultData) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FitLookDarkNavyBg)
            .testTag("home_screen")
    ) {
        FitLookTopBar(
            title = "FITLOOK AI",
            subtitle = "Try Clothes on My Photo",
            onPrivacyClick = onViewProfilePrivacy
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 6.dp)
        ) {
            // PREMIUM FITLOOK AI HERO BANNER
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .testTag("hero_banner_card"),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = EditorialCardBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, EditorialCardBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp)
                ) {
                    // "AI Virtual Try-On" Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(FitLookGradientPrimary)
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "✨ REAL AI VIRTUAL TRY-ON",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.5.sp,
                                fontSize = 10.sp
                            ),
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Try Clothes on\nYour Photo",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 28.sp,
                            lineHeight = 34.sp,
                            letterSpacing = (-0.5).sp
                        ),
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Upload your photo, choose or upload an outfit and see realistic AI Try-On look with your face and body kept natural.",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 20.sp
                        ),
                        color = FitLookTextSecondary
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // HERO ACTION BUTTONS: Primary Gradient "✨ Try Clothes on My Photo" & Secondary "👔 Shop Catalogue"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1.3f)
                                .height(52.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(FitLookGradientPrimary)
                                .clickable { onStartTryOn() }
                                .testTag("try_on_my_photo_hero_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "✨ Try On Photo",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp,
                                        fontSize = 14.sp
                                    ),
                                    color = Color.White
                                )
                            }
                        }

                        Button(
                            onClick = onViewCatalogue,
                            modifier = Modifier
                                .weight(1.1f)
                                .height(52.dp)
                                .testTag("browse_catalogue_button"),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = FitLookDarkSurface,
                                contentColor = Color.White
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, EditorialCardBorder)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Checkroom,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = FitLookPinkLight
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "👔 Catalogue",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.4.sp,
                                    fontSize = 13.sp
                                ),
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // POPULAR OUTFIT QUICK TRY-ON
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Checkroom,
                        contentDescription = null,
                        tint = FitLookPink,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "TRY CLOTHES ON MY PHOTO",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            fontSize = 13.sp
                        ),
                        color = FitLookTextPrimary
                    )
                }
                Text(
                    text = "Try Now",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    ),
                    color = FitLookPink,
                    modifier = Modifier
                        .clickable { onStartTryOn() }
                        .padding(4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quick Outfit Chips / Cards
            val popularOutfits = listOf(
                Pair("Pant + Shirt", "👖👔"),
                Pair("T-Shirt + Jeans", "👕👖"),
                Pair("Formal Suit", "🤵"),
                Pair("Jacket", "🧥"),
                Pair("Traditional Clothes", "🥻"),
                Pair("Sherwani", "👑"),
                Pair("Dress", "👗")
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 6.dp)
            ) {
                items(popularOutfits) { (outfit, emoji) ->
                    Card(
                        modifier = Modifier
                            .width(135.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .clickable {
                                onSelectOutfitForTryOn(outfit)
                            }
                            .testTag("outfit_chip_${outfit.replace(" ", "_")}"),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = EditorialCardBg),
                        border = androidx.compose.foundation.BorderStroke(1.dp, EditorialCardBorder)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = emoji, fontSize = 28.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = outfit,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                ),
                                color = FitLookTextPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Try on Photo →",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = FitLookPink
                            )
                        }
                    }
                }
            }

            // FEATURED BOUTIQUE COLLECTION (Live B2B Store products)
            if (featuredProducts.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = null,
                            tint = FitLookPink,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "SHOP CATALOGUE",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                fontSize = 13.sp
                            ),
                            color = FitLookTextPrimary
                        )
                    }
                    Text(
                        text = "Explore All",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        ),
                        color = FitLookPink,
                        modifier = Modifier
                            .clickable { onViewCatalogue() }
                            .padding(4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    items(featuredProducts.take(6)) { product ->
                        Card(
                            modifier = Modifier
                                .width(150.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .clickable { onSelectProduct(product) }
                                .testTag("home_product_card_${product.id}"),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = EditorialCardBg),
                            border = androidx.compose.foundation.BorderStroke(1.dp, EditorialCardBorder)
                        ) {
                            Column {
                                SmartImage(
                                    uriString = product.primaryImageUri,
                                    contentDescription = product.name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(140.dp)
                                )
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = product.name,
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        ),
                                        color = FitLookTextPrimary,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "₹%.0f".format(product.price),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Black,
                                            fontSize = 12.sp
                                        ),
                                        color = FitLookPink
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // CATEGORY SELECTOR SECTION
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SELECT CATEGORY",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        fontSize = 13.sp
                    ),
                    color = FitLookTextPrimary
                )
                Text(
                    text = "View All",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    ),
                    color = FitLookPink,
                    modifier = Modifier
                        .clickable { onStartTryOn() }
                        .padding(4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                PersonCategory.entries.take(4).forEach { category ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(24.dp))
                            .clickable { onSelectQuickCategory(category) }
                            .testTag("home_category_card_${category.id}"),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = EditorialCardBg),
                        border = androidx.compose.foundation.BorderStroke(1.dp, EditorialCardBorder)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp, horizontal = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = category.emoji,
                                fontSize = 28.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = category.title,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = (-0.3).sp,
                                    fontSize = 12.sp
                                ),
                                color = FitLookTextPrimary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // PRIVACY GUARANTEE BANNER
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .clickable { onViewProfilePrivacy() }
                    .testTag("privacy_highlight_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = EditorialCardBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, FitLookPurple.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(FitLookPurple.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = FitLookPink,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "PRIVACY GUARANTEED",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.5.sp,
                                fontSize = 10.sp
                            ),
                            color = FitLookPink
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Photos are automatically deleted according to your selected privacy setting.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            ),
                            color = FitLookTextSecondary
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = FitLookTextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // HOW FITLOOK AI WORKS
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("how_it_works_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = EditorialCardBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, EditorialCardBorder)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = FitLookPink,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "How FitLook AI Works",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
                            color = FitLookTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    HowItWorksStep(stepNum = "1", title = "Upload Person Photo", desc = "Upload from phone Gallery or take with Camera.")
                    Spacer(modifier = Modifier.height(12.dp))
                    HowItWorksStep(stepNum = "2", title = "Select or Upload Outfit", desc = "Choose pant, shirt, dress from catalogue or upload your own garment.")
                    Spacer(modifier = Modifier.height(12.dp))
                    HowItWorksStep(stepNum = "3", title = "Generate AI Look", desc = "AI replaces clothes naturally while keeping face and body posture intact.")
                    Spacer(modifier = Modifier.height(12.dp))
                    HowItWorksStep(stepNum = "4", title = "Download & Share", desc = "Compare Before/After, download HD photo, or share directly on WhatsApp.")
                }
            }

            // RECENT LOOKS SECTION
            if (recentLooks.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "RECENT LOOKS",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            fontSize = 13.sp
                        ),
                        color = FitLookTextPrimary
                    )
                    Text(
                        text = "View All",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        ),
                        color = FitLookPink,
                        modifier = Modifier
                            .clickable { onViewMyLooks() }
                            .padding(4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(recentLooks.take(5)) { look ->
                        Card(
                            modifier = Modifier
                                .width(140.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .clickable { onViewLookDetail(look) },
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = EditorialCardBg),
                            border = androidx.compose.foundation.BorderStroke(1.dp, EditorialCardBorder)
                        ) {
                            Column {
                                SmartImage(
                                    uriString = look.resultImageUri,
                                    contentDescription = look.clothingCategory,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(140.dp)
                                )
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = look.clothingCategory,
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        ),
                                        color = FitLookTextPrimary,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = look.fitStyle.title,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 11.sp
                                        ),
                                        color = FitLookTextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun HowItWorksStep(stepNum: String, title: String, desc: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(CircleShape)
                .background(FitLookPurple.copy(alpha = 0.25f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stepNum,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 11.sp
                ),
                color = FitLookPink
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                ),
                color = FitLookTextPrimary
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                ),
                color = FitLookTextSecondary
            )
        }
    }
}

