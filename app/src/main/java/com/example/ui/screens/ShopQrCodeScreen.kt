package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ShopProfile
import com.example.ui.components.SmartImage
import com.example.ui.theme.EditorialBlue
import com.example.ui.theme.EditorialBlueContainer
import com.example.ui.theme.EditorialBorderSubtle
import com.example.ui.theme.EditorialCardBorder
import com.example.ui.theme.EditorialNavy
import com.example.ui.theme.EditorialSecondaryText
import com.example.ui.theme.EditorialSubtext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopQrCodeScreen(
    shopProfile: ShopProfile,
    onBack: () -> Unit,
    onShare: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    var isCopied by remember { mutableStateOf(false) }
    val storeUrl = "https://fitlook.ai/store/${shopProfile.slug}"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Store QR & Catalogue",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = EditorialNavy
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = EditorialNavy
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Printable Store Standee Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, EditorialCardBorder, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Shop Logo & Name
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .border(1.dp, EditorialBorderSubtle, RoundedCornerShape(14.dp))
                    ) {
                        SmartImage(
                            uriString = shopProfile.logoUri,
                            contentDescription = shopProfile.name,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = shopProfile.name,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                        color = EditorialNavy,
                        textAlign = TextAlign.Center
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = EditorialSubtext,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = shopProfile.location,
                            style = MaterialTheme.typography.bodySmall,
                            color = EditorialSecondaryText
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // QR Code Canvas Graphic
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFF9F9FC))
                            .border(1.dp, EditorialBorderSubtle, RoundedCornerShape(16.dp))
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        QrMatrixGraphic(
                            modifier = Modifier.fillMaxSize(),
                            tint = EditorialNavy
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tagline callout
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(EditorialBlueContainer)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = EditorialBlue,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "SCAN TO TRY ON WITH AI",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                ),
                                color = EditorialBlue
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Powered by FitLook AI",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = EditorialSubtext
                    )
                }
            }

            // Store Link Copy Box
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .border(1.dp, EditorialBorderSubtle, RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "DIRECT STORE LINK",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        ),
                        color = EditorialSubtext
                    )
                    Text(
                        text = storeUrl,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = EditorialBlue,
                        maxLines = 1
                    )
                }

                IconButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(storeUrl))
                        isCopied = true
                    },
                    modifier = Modifier.testTag("copy_store_url_button")
                ) {
                    Icon(
                        imageVector = if (isCopied) Icons.Default.Check else Icons.Default.ContentCopy,
                        contentDescription = "Copy Link",
                        tint = if (isCopied) Color(0xFF2E7D32) else EditorialNavy
                    )
                }
            }

            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onShare,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EditorialBlue)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Share Store Link", fontWeight = FontWeight.Bold)
                }
            }

            // Standee placement tips
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, EditorialCardBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "💡 How to Use This in Your Store",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = EditorialNavy
                    )
                    Text(
                        text = "1. Print and place this QR code at your billing counter and fitting rooms.\n2. Customers scan to view your entire collection and try clothes on their photo.\n3. They buy right away in store or order later directly on WhatsApp.",
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 19.sp),
                        color = EditorialSecondaryText
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun QrMatrixGraphic(
    modifier: Modifier = Modifier,
    tint: Color
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val matrixSize = 21
        val cellSize = w / matrixSize

        // Draw Corner Finder Patterns
        fun drawFinder(top: Float, left: Float) {
            // Outer square
            drawRect(
                color = tint,
                topLeft = Offset(left * cellSize, top * cellSize),
                size = Size(7 * cellSize, 7 * cellSize)
            )
            // Inner white
            drawRect(
                color = Color(0xFFF9F9FC),
                topLeft = Offset((left + 1) * cellSize, (top + 1) * cellSize),
                size = Size(5 * cellSize, 5 * cellSize)
            )
            // Core
            drawRect(
                color = tint,
                topLeft = Offset((left + 2) * cellSize, (top + 2) * cellSize),
                size = Size(3 * cellSize, 3 * cellSize)
            )
        }

        drawFinder(0f, 0f)
        drawFinder(0f, (matrixSize - 7).toFloat())
        drawFinder((matrixSize - 7).toFloat(), 0f)

        // Seeded decorative QR pixel matrix
        val pattern = listOf(
            0,1,0,1,1,0,1, 1,0,1,0,0,1,1, 0,1,1,0,1,0,1,
            1,0,1,0,0,1,0, 0,1,1,0,1,0,0, 1,0,0,1,0,1,0,
            0,1,1,1,0,0,1, 1,0,0,1,0,1,1, 0,1,0,1,1,0,1,
            1,1,0,0,1,0,1, 0,1,0,1,1,0,0, 1,0,1,0,0,1,1
        )

        for (r in 0 until matrixSize) {
            for (c in 0 until matrixSize) {
                // Skip finder pattern zones
                val inTopLeft = r < 8 && c < 8
                val inTopRight = r < 8 && c >= matrixSize - 8
                val inBottomLeft = r >= matrixSize - 8 && c < 8

                if (!inTopLeft && !inTopRight && !inBottomLeft) {
                    val idx = (r * 11 + c * 7) % pattern.size
                    if (pattern[idx] == 1) {
                        drawRect(
                            color = tint,
                            topLeft = Offset(c * cellSize, r * cellSize),
                            size = Size(cellSize * 0.9f, cellSize * 0.9f)
                        )
                    }
                }
            }
        }
    }
}
