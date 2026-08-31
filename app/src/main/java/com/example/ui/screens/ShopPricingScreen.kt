package com.example.ui.screens

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ShopProfile
import com.example.data.model.SubscriptionPlan
import com.example.ui.theme.EditorialBlue
import com.example.ui.theme.EditorialBlueContainer
import com.example.ui.theme.EditorialBorderSubtle
import com.example.ui.theme.EditorialCardBg
import com.example.ui.theme.EditorialCardBorder
import com.example.ui.theme.EditorialNavy
import com.example.ui.theme.EditorialSecondaryText
import com.example.ui.theme.EditorialSubtext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopPricingScreen(
    currentShop: ShopProfile,
    isProcessingPayment: Boolean,
    onBack: () -> Unit,
    onSelectPlan: (SubscriptionPlan) -> Unit
) {
    var selectedPlanToPurchase by remember { mutableStateOf<SubscriptionPlan?>(null) }
    var selectedPaymentMethod by remember { mutableStateOf("UPI (GPay / PhonePe / Paytm)") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Shop Plans & AI Credits",
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Intro Header
            Text(
                text = "Scale Your Clothing Sales with AI",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black),
                color = EditorialNavy
            )
            Text(
                text = "Give your boutique shoppers a high-precision virtual dressing room. Upgrade anytime with rollover credits.",
                style = MaterialTheme.typography.bodyMedium,
                color = EditorialSecondaryText
            )

            // Current Plan Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = EditorialBlueContainer)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "CURRENT ACTIVE PLAN",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = EditorialBlue
                        )
                        Text(
                            text = "${currentShop.plan.title} Plan",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = EditorialNavy
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(EditorialBlue)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${currentShop.availableCredits} Credits Left",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }
                }
            }

            // Plan Cards
            SubscriptionPlan.entries.forEach { plan ->
                val isCurrentPlan = currentShop.plan == plan
                PlanCard(
                    plan = plan,
                    isCurrentPlan = isCurrentPlan,
                    onSelect = { selectedPlanToPurchase = plan }
                )
            }

            // Credits Transparency Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, EditorialCardBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = EditorialBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Transparent Credit System",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = EditorialNavy
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        CreditRuleRow(text = "1 Credit = 1 Successful Virtual Try-On.")
                        CreditRuleRow(text = "Failed or interrupted generations are NEVER charged.")
                        CreditRuleRow(text = "Unused credits rollover automatically with each monthly billing.")
                        CreditRuleRow(text = "Cancel or change plans anytime from this screen.")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Payment Checkout Modal Dialog
    selectedPlanToPurchase?.let { plan ->
        AlertDialog(
            onDismissRequest = { if (!isProcessingPayment) selectedPlanToPurchase = null },
            title = {
                Text(
                    text = "Upgrade to ${plan.title} Plan",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = EditorialNavy
                )
            },
            text = {
                if (isProcessingPayment) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator(color = EditorialBlue)
                        Text(
                            text = "Securing transaction with payment gateway...",
                            style = MaterialTheme.typography.bodySmall,
                            color = EditorialSecondaryText
                        )
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Monthly Subscription",
                                style = MaterialTheme.typography.bodyMedium,
                                color = EditorialSecondaryText
                            )
                            Text(
                                text = "₹%.0f / mo".format(plan.monthlyPriceInr),
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                                color = EditorialBlue
                            )
                        }

                        Text(
                            text = "Includes ${plan.totalCredits} AI virtual try-on credits per month.",
                            style = MaterialTheme.typography.bodySmall,
                            color = EditorialNavy
                        )

                        Text(
                            text = "SELECT PAYMENT METHOD",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = EditorialSubtext
                        )

                        val paymentMethods = listOf(
                            "UPI (GPay / PhonePe / Paytm)",
                            "Debit / Credit Card (Visa, RuPay)",
                            "Net Banking (All Indian Banks)"
                        )

                        paymentMethods.forEach { method ->
                            val isSelected = selectedPaymentMethod == method
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { selectedPaymentMethod = method }
                                    .background(if (isSelected) EditorialBlueContainer else EditorialCardBg)
                                    .border(1.dp, if (isSelected) EditorialBlue else EditorialBorderSubtle, RoundedCornerShape(10.dp))
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = if (method.startsWith("UPI")) Icons.Default.Payments else Icons.Default.CreditCard,
                                        contentDescription = null,
                                        tint = EditorialNavy,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = method,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        ),
                                        color = EditorialNavy
                                    )
                                }
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Selected",
                                        tint = EditorialBlue,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                if (!isProcessingPayment) {
                    Button(
                        onClick = {
                            onSelectPlan(plan)
                            selectedPlanToPurchase = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EditorialBlue),
                        modifier = Modifier.testTag("confirm_plan_payment_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Pay ₹%.0f Securely".format(plan.monthlyPriceInr))
                    }
                }
            },
            dismissButton = {
                if (!isProcessingPayment) {
                    TextButton(onClick = { selectedPlanToPurchase = null }) {
                        Text("Cancel", color = EditorialSecondaryText)
                    }
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
private fun PlanCard(
    plan: SubscriptionPlan,
    isCurrentPlan: Boolean,
    onSelect: () -> Unit
) {
    val isPopular = plan == SubscriptionPlan.BUSINESS

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isPopular) 2.dp else 1.dp,
                color = if (isPopular) EditorialBlue else EditorialCardBorder,
                shape = RoundedCornerShape(18.dp)
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPopular) Color.White else EditorialCardBg
        )
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    if (isPopular) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(EditorialBlue)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "MOST POPULAR FOR SHOPS",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp
                                ),
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    Text(
                        text = "${plan.title} Plan",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = EditorialNavy
                    )
                    Text(
                        text = plan.tagline,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = EditorialSecondaryText
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "₹%.0f".format(plan.monthlyPriceInr),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 22.sp
                        ),
                        color = EditorialNavy
                    )
                    Text(
                        text = "/ month",
                        style = MaterialTheme.typography.labelSmall,
                        color = EditorialSubtext
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Plan Features List
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                plan.features.forEach { feat ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(EditorialBlueContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = EditorialBlue,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                        Text(
                            text = feat,
                            style = MaterialTheme.typography.bodySmall,
                            color = EditorialNavy
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (isCurrentPlan) {
                OutlinedButton(
                    onClick = {},
                    enabled = false,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Currently Active Plan")
                }
            } else {
                Button(
                    onClick = onSelect,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isPopular) EditorialBlue else EditorialNavy,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Select ${plan.title} Plan",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun CreditRuleRow(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(EditorialBlue)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
            color = EditorialSecondaryText
        )
    }
}
