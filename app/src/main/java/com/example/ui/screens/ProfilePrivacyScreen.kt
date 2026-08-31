package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AuthType
import com.example.data.model.AutoDeleteDuration
import com.example.data.model.UserAccount
import com.example.data.model.UserMode
import com.example.service.PrivacyService
import com.example.ui.components.FitLookTopBar
import com.example.ui.theme.EditorialBlue
import com.example.ui.theme.EditorialBlueContainer
import com.example.ui.theme.EditorialBlueContainerBorder
import com.example.ui.theme.EditorialBorderSubtle
import com.example.ui.theme.EditorialCardBg
import com.example.ui.theme.EditorialCardBorder
import com.example.ui.theme.EditorialDarkBanner
import com.example.ui.theme.EditorialDarkBannerIconBg
import com.example.ui.theme.EditorialNavy
import com.example.ui.theme.EditorialSecondaryText
import com.example.ui.theme.EditorialSubtext
import com.example.ui.theme.EditorialTextDark

@Composable
fun ProfilePrivacyScreen(
    currentUser: UserAccount?,
    currentMode: UserMode,
    selectedAutoDeleteDuration: AutoDeleteDuration,
    privacyService: PrivacyService,
    onSelectMode: (UserMode) -> Unit,
    onSelectAutoDeleteDuration: (AutoDeleteDuration) -> Unit,
    onDeleteAllServerData: () -> Unit,
    onLogout: () -> Unit
) {
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var showPrivacyPolicyDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("profile_privacy_screen")
    ) {
        FitLookTopBar(
            title = "Profile & Privacy",
            subtitle = "Security & Account Management"
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // USER ACCOUNT CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = EditorialCardBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, EditorialCardBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(EditorialBlueContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (currentMode == UserMode.CUSTOMER) Icons.Default.Person else Icons.Default.Storefront,
                            contentDescription = null,
                            tint = EditorialBlue,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = currentUser?.displayName ?: if (currentMode == UserMode.CUSTOMER) "Customer Explorer" else "Boutique Store Owner",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            ),
                            color = EditorialNavy
                        )
                        Text(
                            text = when (currentUser?.authType) {
                                AuthType.GOOGLE -> "Signed in with Google"
                                AuthType.MOBILE_OTP -> "Mobile Verified"
                                else -> "Active in ${currentMode.displayName} Mode"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = EditorialBlue
                        )
                    }
                }
            }

            // APP MODE SWITCHER CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, EditorialCardBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = null,
                                tint = EditorialBlue,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "App Experience Mode",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = EditorialNavy
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(EditorialBlueContainer)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = currentMode.displayName,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = EditorialBlue
                            )
                        }
                    }

                    Text(
                        text = if (currentMode == UserMode.CUSTOMER)
                            "You are browsing in Customer Mode (Virtual try-on on your photos, boutique shopping)."
                        else
                            "You are operating in Shop Owner Mode (Inventory management, sales analytics, QR codes).",
                        style = MaterialTheme.typography.bodySmall,
                        color = EditorialSecondaryText
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { onSelectMode(UserMode.CUSTOMER) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (currentMode == UserMode.CUSTOMER) EditorialBlue else EditorialCardBg,
                                contentColor = if (currentMode == UserMode.CUSTOMER) Color.White else EditorialNavy
                            )
                        ) {
                            Text("Customer Mode", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        Button(
                            onClick = { onSelectMode(UserMode.SHOP_OWNER) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (currentMode == UserMode.SHOP_OWNER) EditorialBlue else EditorialCardBg,
                                contentColor = if (currentMode == UserMode.SHOP_OWNER) Color.White else EditorialNavy
                            )
                        ) {
                            Text("Shop Owner", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }

            // AUTO-DELETE DURATION CONFIGURATION
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, EditorialCardBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoDelete,
                            contentDescription = null,
                            tint = EditorialBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Auto-Delete Photos Duration",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = EditorialNavy
                        )
                    }

                    Text(
                        text = "Choose how quickly your uploaded photos and generated AI looks are permanently purged from the server.",
                        style = MaterialTheme.typography.bodySmall,
                        color = EditorialSecondaryText
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AutoDeleteDuration.entries.forEach { duration ->
                            val isSelected = selectedAutoDeleteDuration == duration
                            FilterChip(
                                selected = isSelected,
                                onClick = { onSelectAutoDeleteDuration(duration) },
                                label = { Text(duration.label, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = EditorialBlue,
                                    selectedLabelColor = Color.White,
                                    containerColor = EditorialCardBg,
                                    labelColor = EditorialNavy
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = isSelected,
                                    borderColor = if (isSelected) EditorialBlue else EditorialBorderSubtle
                                )
                            )
                        }
                    }
                }
            }

            // PRIVACY & SECURITY BANNER
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = EditorialDarkBanner),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF223555))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(EditorialDarkBannerIconBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Privacy & Encryption Shield",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "“For your privacy, uploaded photos and AI looks are automatically erased from our server after your selected duration.”",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp, lineHeight = 17.sp),
                        color = Color(0xFFD4E3FF)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = { showPrivacyPolicyDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4A658E))
                    ) {
                        Text("View Complete Privacy Information", color = Color.White, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold))
                    }
                }
            }

            // IMMEDIATE SERVER PHOTO DELETION (DELETE NOW)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = EditorialCardBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBA1A1A).copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DeleteForever,
                            contentDescription = null,
                            tint = Color(0xFFBA1A1A),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Instant Data Erasure",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFFBA1A1A)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Permanently purge all uploaded person photos, clothing images, and AI virtual try-on results right now.",
                        style = MaterialTheme.typography.bodySmall,
                        color = EditorialSecondaryText
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { showDeleteConfirmDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("btn_profile_delete_all"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBA1A1A))
                    ) {
                        Text("Delete All Server Data Now", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // ABOUT & TAGLINES
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = EditorialCardBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, EditorialCardBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "FITLOOK AI",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, letterSpacing = 1.sp),
                        color = EditorialNavy
                    )
                    Text(
                        text = "“Try Before You Buy”",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = EditorialBlue
                    )
                    Text(
                        text = "“Kapda kharidne se pehle, khud par dekho.”",
                        style = MaterialTheme.typography.labelMedium,
                        color = EditorialSecondaryText
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "FitLook AI B2B Virtual Try-On & Sales Suite",
                        style = MaterialTheme.typography.labelSmall,
                        color = EditorialSecondaryText
                    )
                }
            }

            // LOGOUT / SWITCH USER
            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_logout"),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, EditorialBorderSubtle)
            ) {
                Icon(Icons.Default.Logout, contentDescription = null, tint = EditorialNavy, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Switch User / Log Out", color = EditorialNavy, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            containerColor = Color.White,
            title = {
                Text(
                    "Permanently Delete All Data?",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = EditorialNavy)
                )
            },
            text = {
                Text(
                    text = "This will immediately erase all uploaded photos, clothing garments, and AI virtual try-on results from server storage.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = EditorialSecondaryText
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirmDialog = false
                        onDeleteAllServerData()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBA1A1A))
                ) {
                    Text("Delete Everything Now", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Cancel", color = EditorialSecondaryText)
                }
            }
        )
    }

    if (showPrivacyPolicyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyPolicyDialog = false },
            containerColor = Color.White,
            title = {
                Text(
                    "Privacy First Commitment",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = EditorialNavy)
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "At FitLook AI, your photos are your personal property.",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = EditorialNavy
                    )
                    Text(
                        text = "1. Zero Biometric Reselling: Your face, body measurements, and photos are never sold, indexed, or shared with third parties.\n2. Configurable Auto-Delete: Every file is automatically destroyed when your chosen timer expires.\n3. Complete User Control: You can purge all data on-demand at any time.",
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                        color = EditorialSecondaryText
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showPrivacyPolicyDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = EditorialBlue)
                ) {
                    Text("Understood", color = Color.White)
                }
            }
        )
    }
}
