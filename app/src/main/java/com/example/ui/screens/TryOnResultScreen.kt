package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AutoDelete
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AutoDeleteDuration
import com.example.data.model.TryOnResultData
import com.example.ui.components.FitLookTopBar
import com.example.ui.components.SmartImage
import com.example.ui.theme.EditorialBlue
import com.example.ui.theme.EditorialBlueContainer
import com.example.ui.theme.EditorialBlueContainerBorder
import com.example.ui.theme.EditorialCardBg
import com.example.ui.theme.EditorialCardBorder
import com.example.ui.theme.EditorialNavy
import com.example.ui.theme.EditorialSecondaryText
import com.example.ui.theme.EditorialSubtext
import com.example.ui.theme.EditorialTextDark

@Composable
fun TryOnResultScreen(
    resultData: TryOnResultData,
    onSaveToggle: (TryOnResultData) -> Unit,
    onDownload: (TryOnResultData) -> Unit,
    onShare: (TryOnResultData) -> Unit,
    onContactShopOnWhatsApp: (TryOnResultData) -> Unit,
    onTryAnotherOutfit: () -> Unit,
    onDeleteNow: (TryOnResultData) -> Unit,
    onBackClick: () -> Unit
) {
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = AI Result, 1 = Original Person, 2 = Clothing

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("result_screen")
    ) {
        FitLookTopBar(
            title = "Try-On Result",
            subtitle = resultData.productName ?: "${resultData.clothingCategory} • ${resultData.fitStyle.title}",
            showBackButton = true,
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 6.dp)
        ) {
            // DEMO MODE NOTICE
            if (resultData.isDemoMode) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .testTag("demo_mode_banner"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = EditorialBlueContainer),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EditorialBlueContainerBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Demo Mode",
                            tint = EditorialBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "“AI Try-On service is in high-fidelity preview mode.”",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                ),
                                color = EditorialNavy
                            )
                            Text(
                                text = "Neural garment warping and pose fitting rendered seamlessly.",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                color = EditorialSecondaryText
                            )
                        }
                    }
                }
            }

            // AUTO-DELETE PRIVACY BADGE
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = EditorialCardBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, EditorialCardBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoDelete,
                            contentDescription = null,
                            tint = EditorialBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Server auto-delete in ${resultData.autoDeleteDuration.label}",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            ),
                            color = EditorialTextDark
                        )
                    }

                    Text(
                        text = "🔒 Auto Purge",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        color = EditorialBlue
                    )
                }
            }

            // VIEW TOGGLE TABS (Result / Original / Garment)
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = EditorialCardBg,
                contentColor = EditorialBlue,
                indicator = { tabPositions ->
                    SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = EditorialBlue,
                        height = 3.dp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, EditorialCardBorder, RoundedCornerShape(16.dp))
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            "AI Result",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 0) EditorialBlue else EditorialSecondaryText
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            "Original",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 1) EditorialBlue else EditorialSecondaryText
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = {
                        Text(
                            "Garment",
                            fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 2) EditorialBlue else EditorialSecondaryText
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // MAIN FOCUS IMAGE CARD
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp)
                    .testTag("result_main_image_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = EditorialCardBg),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, EditorialBlue)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    val displayUri = when (selectedTab) {
                        0 -> resultData.resultImageUri
                        1 -> resultData.personImageUri
                        else -> resultData.clothingImageUri
                    }

                    SmartImage(
                        uriString = displayUri,
                        contentDescription = "Try-On Output",
                        modifier = Modifier.fillMaxSize()
                    )

                    // Tag Badge on Top Left
                    Box(
                        modifier = Modifier
                            .padding(14.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xCC001D36))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = when (selectedTab) {
                                0 -> "✨ Virtual Try-On Result"
                                1 -> "👤 Uploaded Person Photo"
                                else -> "👗 Selected Clothing"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }

                    // Save Favorite Icon on Top Right
                    IconButton(
                        onClick = { onSaveToggle(resultData) },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(14.dp)
                            .clip(CircleShape)
                            .background(Color(0xCC001D36))
                            .testTag("btn_save_look_heart")
                    ) {
                        Icon(
                            imageVector = if (resultData.isSaved) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Save Look",
                            tint = if (resultData.isSaved) Color(0xFFBA1A1A) else Color.White
                        )
                    }
                }
            }

            // SHOP PRODUCT DETAIL CARD (If linked to a boutique product)
            if (resultData.productName != null || resultData.shopName != null) {
                Spacer(modifier = Modifier.height(14.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = EditorialCardBg),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EditorialCardBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(EditorialBlueContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingBag,
                                contentDescription = null,
                                tint = EditorialBlue,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = resultData.productName ?: resultData.clothingCategory,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = EditorialNavy,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            resultData.productPrice?.let { price ->
                                Text(
                                    text = "₹%.0f • ${resultData.shopName ?: "Boutique Store"}".format(price),
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                    color = EditorialBlue
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // WHATSAPP BUY / INQUIRE CTA BUTTON (B2B Sales direct link)
            Button(
                onClick = { onContactShopOnWhatsApp(resultData) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("whatsapp_order_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF25D366),
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = "WhatsApp",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Interested? Inquire on WhatsApp",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.4.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ACTION BUTTONS (Save, Download, Share)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Save Look
                Button(
                    onClick = { onSaveToggle(resultData) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("btn_save_look"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (resultData.isSaved) EditorialNavy else EditorialCardBg,
                        contentColor = if (resultData.isSaved) Color.White else EditorialTextDark
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EditorialCardBorder)
                ) {
                    Icon(
                        imageVector = if (resultData.isSaved) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = if (resultData.isSaved) Color(0xFFFFB4AB) else EditorialTextDark
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (resultData.isSaved) "Saved" else "Save", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                }

                // Download
                Button(
                    onClick = { onDownload(resultData) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("btn_download_look"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EditorialCardBg,
                        contentColor = EditorialTextDark
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EditorialCardBorder)
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp), tint = EditorialTextDark)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Download", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                }

                // Share
                Button(
                    onClick = { onShare(resultData) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("btn_share_look"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EditorialCardBg,
                        contentColor = EditorialTextDark
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EditorialCardBorder)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp), tint = EditorialTextDark)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Share", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // PRIMARY NEXT ACTION: 🔄 TRY ANOTHER OUTFIT
            Button(
                onClick = onTryAnotherOutfit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("btn_try_another_outfit"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EditorialBlue,
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Try Another Outfit", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // PRIVACY ACTION: 🗑️ DELETE NOW
            OutlinedButton(
                onClick = { showDeleteConfirmDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_delete_now"),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBA1A1A).copy(alpha = 0.4f))
            ) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFBA1A1A), modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Delete Now (Remove Photos & Result)", color = Color(0xFFBA1A1A), style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold))
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }

    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            containerColor = Color.White,
            title = {
                Text(
                    "Delete Photos & Result Now?",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = EditorialNavy)
                )
            },
            text = {
                Text(
                    text = "This will permanently delete your uploaded person photo, clothing photo, and AI result from server storage immediately.\n\nDownloaded gallery files on your phone will not be affected.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = EditorialSecondaryText
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirmDialog = false
                        onDeleteNow(resultData)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBA1A1A))
                ) {
                    Text("Delete Permanently", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Cancel", color = EditorialSecondaryText)
                }
            }
        )
    }
}
