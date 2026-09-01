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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.TryOnResultData
import com.example.ui.components.FitLookTopBar
import com.example.ui.components.SmartImage
import com.example.ui.theme.EditorialCardBg
import com.example.ui.theme.EditorialCardBorder
import com.example.ui.theme.EditorialError
import com.example.ui.theme.FitLookDarkNavyBg
import com.example.ui.theme.FitLookDarkSurface
import com.example.ui.theme.FitLookGradientPrimary
import com.example.ui.theme.FitLookPink
import com.example.ui.theme.FitLookPinkLight
import com.example.ui.theme.FitLookPurple
import com.example.ui.theme.FitLookTextPrimary
import com.example.ui.theme.FitLookTextSecondary

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
    var viewMode by remember { mutableIntStateOf(0) } // 0 = Side-by-Side Before/After, 1 = Full Result, 2 = Original, 3 = Clothing
    var showFullscreenZoom by remember { mutableStateOf(false) }
    var zoomTargetUri by remember { mutableStateOf(resultData.resultImageUri) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FitLookDarkNavyBg)
            .testTag("result_screen")
    ) {
        FitLookTopBar(
            title = "AI Fit Result",
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
            // AUTO-DELETE PRIVACY BADGE
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
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
                            tint = FitLookPurple,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Auto-deletes in ${resultData.autoDeleteDuration.label}",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            ),
                            color = FitLookTextSecondary
                        )
                    }

                    Text(
                        text = "🔒 Private & Secure",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        color = FitLookPink
                    )
                }
            }

            // VIEW TOGGLE TABS (Side-by-Side vs Single Views)
            TabRow(
                selectedTabIndex = viewMode,
                containerColor = EditorialCardBg,
                contentColor = FitLookPink,
                indicator = { tabPositions ->
                    SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[viewMode]),
                        color = FitLookPink,
                        height = 3.dp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, EditorialCardBorder, RoundedCornerShape(16.dp))
            ) {
                Tab(
                    selected = viewMode == 0,
                    onClick = { viewMode = 0 },
                    text = {
                        Text(
                            "⚖️ Side-by-Side",
                            fontWeight = if (viewMode == 0) FontWeight.Bold else FontWeight.Normal,
                            color = if (viewMode == 0) FitLookPink else FitLookTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                )
                Tab(
                    selected = viewMode == 1,
                    onClick = { viewMode = 1 },
                    text = {
                        Text(
                            "✨ Result",
                            fontWeight = if (viewMode == 1) FontWeight.Bold else FontWeight.Normal,
                            color = if (viewMode == 1) FitLookPink else FitLookTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                )
                Tab(
                    selected = viewMode == 2,
                    onClick = { viewMode = 2 },
                    text = {
                        Text(
                            "👤 Original",
                            fontWeight = if (viewMode == 2) FontWeight.Bold else FontWeight.Normal,
                            color = if (viewMode == 2) FitLookPink else FitLookTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                )
                Tab(
                    selected = viewMode == 3,
                    onClick = { viewMode = 3 },
                    text = {
                        Text(
                            "👗 Outfit",
                            fontWeight = if (viewMode == 3) FontWeight.Bold else FontWeight.Normal,
                            color = if (viewMode == 3) FitLookPink else FitLookTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // MAIN VISUAL CONTAINER: SIDE-BY-SIDE OR FULL VIEW
            if (viewMode == 0) {
                // BEFORE / AFTER SIDE-BY-SIDE
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(340.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // LEFT: ORIGINAL PHOTO
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .clickable {
                                zoomTargetUri = resultData.personImageUri
                                showFullscreenZoom = true
                            }
                            .testTag("result_original_photo_card"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = EditorialCardBg),
                        border = androidx.compose.foundation.BorderStroke(1.dp, EditorialCardBorder)
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            SmartImage(
                                uriString = resultData.personImageUri,
                                contentDescription = "Original Photo",
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(8.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xCC000000))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Original Photo",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
                                    color = Color.White
                                )
                            }
                        }
                    }

                    // RIGHT: AI FIT RESULT
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .clickable {
                                zoomTargetUri = resultData.resultImageUri
                                showFullscreenZoom = true
                            }
                            .testTag("result_ai_fit_card"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = EditorialCardBg),
                        border = androidx.compose.foundation.BorderStroke(2.dp, FitLookPurple)
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            SmartImage(
                                uriString = resultData.resultImageUri,
                                contentDescription = "AI Fit Result",
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(8.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(FitLookPurple)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "✨ AI Fit Result",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
                                    color = Color.White
                                )
                            }
                            // Tap to Zoom indicator
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(8.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xCC000000))
                                    .padding(6.dp)
                            ) {
                                Icon(Icons.Default.Fullscreen, contentDescription = "Zoom", tint = Color.White, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            } else {
                // SINGLE FULL VIEW
                val singleUri = when (viewMode) {
                    1 -> resultData.resultImageUri
                    2 -> resultData.personImageUri
                    else -> resultData.clothingImageUri
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(360.dp)
                        .clickable {
                            zoomTargetUri = singleUri
                            showFullscreenZoom = true
                        }
                        .testTag("result_main_image_card"),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = EditorialCardBg),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, FitLookPurple)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        SmartImage(
                            uriString = singleUri,
                            contentDescription = "Try-On Output",
                            modifier = Modifier.fillMaxSize()
                        )

                        Box(
                            modifier = Modifier
                                .padding(12.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xCC000000))
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = when (viewMode) {
                                    1 -> "✨ AI Fit Result"
                                    2 -> "👤 Original Person Photo"
                                    else -> "👗 Selected Clothing"
                                },
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }

                        // Zoom Icon on bottom right
                        IconButton(
                            onClick = {
                                zoomTargetUri = singleUri
                                showFullscreenZoom = true
                            },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(10.dp)
                                .clip(CircleShape)
                                .background(Color(0xCC000000))
                        ) {
                            Icon(Icons.Default.Fullscreen, contentDescription = "Full Screen", tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // PRIMARY ACTION BUTTON: 🔄 TRY ANOTHER OUTFIT (Preserves same person photo)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(FitLookGradientPrimary)
                    .clickable { onTryAnotherOutfit() }
                    .testTag("btn_try_another_outfit"),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "🔄 Try Another Outfit",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        ),
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // SECONDARY ACTIONS: ⬇️ Download HD & 📤 Share on WhatsApp
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Download HD
                Button(
                    onClick = { onDownload(resultData) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("btn_download_look"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EditorialCardBg,
                        contentColor = FitLookTextPrimary
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EditorialCardBorder)
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp), tint = FitLookPinkLight)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("⬇️ Download HD", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                }

                // Share on WhatsApp
                Button(
                    onClick = { onContactShopOnWhatsApp(resultData) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
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
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "📤 Share WhatsApp",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // PRIVACY ACTION: 🗑️ DELETE RESULT
            OutlinedButton(
                onClick = { showDeleteConfirmDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .testTag("btn_delete_now"),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, EditorialError.copy(alpha = 0.5f))
            ) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = EditorialError, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("🗑️ Delete Result", color = EditorialError, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold))
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }

    // FULLSCREEN IMAGE ZOOM DIALOG
    if (showFullscreenZoom) {
        Dialog(
            onDismissRequest = { showFullscreenZoom = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.95f))
            ) {
                SmartImage(
                    uriString = zoomTargetUri,
                    contentDescription = "Full Screen Preview",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )

                IconButton(
                    onClick = { showFullscreenZoom = false },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(24.dp)
                        .clip(CircleShape)
                        .background(Color(0x88000000))
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                }
            }
        }
    }

    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            containerColor = FitLookDarkSurface,
            title = {
                Text(
                    "Delete Photos & Result?",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = Color.White)
                )
            },
            text = {
                Text(
                    text = "This will immediately delete your uploaded person photo and generated AI result from server storage.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = FitLookTextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirmDialog = false
                        onDeleteNow(resultData)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EditorialError)
                ) {
                    Text("Delete Permanently", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Cancel", color = FitLookTextSecondary)
                }
            }
        )
    }
}

