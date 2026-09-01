package com.example.ui.screens

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AutoDelete
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.model.CategoryCatalog
import com.example.data.model.FitStyle
import com.example.data.model.PersonCategory
import com.example.ui.components.FitLookTopBar
import com.example.ui.components.SmartImage
import com.example.ui.theme.EditorialBlue
import com.example.ui.theme.EditorialBlueContainer
import com.example.ui.theme.EditorialBlueContainerBorder
import com.example.ui.theme.EditorialBorderSubtle
import com.example.ui.theme.EditorialCardBg
import com.example.ui.theme.EditorialCardBorder
import com.example.ui.theme.EditorialNavy
import com.example.ui.theme.EditorialSecondaryText
import com.example.ui.theme.EditorialTextDark

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TryOnWizardScreen(
    currentStep: Int,
    selectedPersonCategory: PersonCategory,
    selectedClothingCategory: String,
    personImageUri: String?,
    clothingImageUri: String?,
    outfitDescription: String,
    selectedFitStyle: FitStyle,
    onSelectPersonCategory: (PersonCategory) -> Unit,
    onSelectClothingCategory: (String) -> Unit,
    onSetPersonImage: (String?) -> Unit,
    onSetClothingImage: (String?) -> Unit,
    onSetOutfitDescription: (String) -> Unit,
    onSelectFitStyle: (FitStyle) -> Unit,
    onSaveCameraBitmap: (Bitmap) -> String,
    onNavigateStep: (Int) -> Unit,
    onExecuteTryOn: () -> Unit,
    onBackClick: () -> Unit
) {
    // Real Camera and Gallery Launchers
    val personGalleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) onSetPersonImage(uri.toString())
    }

    val clothingGalleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) onSetClothingImage(uri.toString())
    }

    val personCameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            val savedUri = onSaveCameraBitmap(bitmap)
            onSetPersonImage(savedUri)
        }
    }

    val clothingCameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            val savedUri = onSaveCameraBitmap(bitmap)
            onSetClothingImage(savedUri)
        }
    }

    val totalSteps = 4
    val displayStep = currentStep.coerceIn(1, totalSteps)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("tryon_wizard_screen")
    ) {
        FitLookTopBar(
            title = "Try Clothes on My Photo",
            subtitle = "Step $displayStep of $totalSteps",
            showBackButton = true,
            onBackClick = {
                if (displayStep > 1) onNavigateStep(displayStep - 1) else onBackClick()
            }
        )

        // Step Progress Indicator Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (step in 1..totalSteps) {
                val isActive = step <= displayStep
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(if (isActive) EditorialBlue else EditorialCardBorder)
                )
            }
        }

        // Wizard Step Contents
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp)
        ) {
            AnimatedContent(
                targetState = displayStep,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "step_content"
            ) { step ->
                when (step) {
                    1 -> Step1PersonPhotoAndCategory(
                        selectedCategory = selectedPersonCategory,
                        imageUri = personImageUri,
                        onSelectCategory = onSelectPersonCategory,
                        onTakePhoto = { personCameraLauncher.launch(null) },
                        onPickGallery = { personGalleryLauncher.launch("image/*") },
                        onRemovePhoto = { onSetPersonImage(null) },
                        onUseSample = {
                            onSetPersonImage(
                                if (selectedPersonCategory == PersonCategory.MAN || selectedPersonCategory == PersonCategory.BOY)
                                    "drawable/img_demo_person_man"
                                else
                                    "drawable/img_demo_person_woman"
                            )
                        }
                    )
                    2 -> Step2SelectClothingCategory(
                        personCategory = selectedPersonCategory,
                        selectedClothing = selectedClothingCategory,
                        onSelect = {
                            onSelectClothingCategory(it)
                            onNavigateStep(3)
                        }
                    )
                    3 -> Step3OutfitInput(
                        clothingCategory = selectedClothingCategory,
                        imageUri = clothingImageUri,
                        outfitDescription = outfitDescription,
                        onTakePhoto = { clothingCameraLauncher.launch(null) },
                        onPickGallery = { clothingGalleryLauncher.launch("image/*") },
                        onRemovePhoto = { onSetClothingImage(null) },
                        onUseSample = { onSetClothingImage("drawable/img_demo_clothing_sherwani") },
                        onUpdateDescription = onSetOutfitDescription
                    )
                    else -> Step4FitAndReview(
                        selectedPersonCategory = selectedPersonCategory,
                        selectedClothingCategory = selectedClothingCategory,
                        personImageUri = personImageUri,
                        clothingImageUri = clothingImageUri,
                        outfitDescription = outfitDescription,
                        selectedFit = selectedFitStyle,
                        onSelectFit = onSelectFitStyle,
                        onExecuteTryOn = onExecuteTryOn
                    )
                }
            }
        }

        // Bottom Wizard Action Bar
        WizardBottomNavButtons(
            currentStep = displayStep,
            totalSteps = totalSteps,
            canProceed = if (displayStep == 1) !personImageUri.isNullOrBlank() else true,
            onBack = {
                if (displayStep > 1) onNavigateStep(displayStep - 1) else onBackClick()
            },
            onNext = {
                if (displayStep < totalSteps) onNavigateStep(displayStep + 1) else onExecuteTryOn()
            }
        )
    }
}

// STEP 1: PERSON PHOTO UPLOAD / CAMERA & CATEGORY
@Composable
private fun Step1PersonPhotoAndCategory(
    selectedCategory: PersonCategory,
    imageUri: String?,
    onSelectCategory: (PersonCategory) -> Unit,
    onTakePhoto: () -> Unit,
    onPickGallery: () -> Unit,
    onRemovePhoto: () -> Unit,
    onUseSample: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "1. Upload or Take Photo",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
            color = EditorialNavy
        )
        Text(
            text = "Take a full-body photo or pick from gallery.",
            style = MaterialTheme.typography.bodyMedium,
            color = EditorialSecondaryText
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Who are you styling (Quick selector)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PersonCategory.entries.forEach { cat ->
                val isSelected = cat == selectedCategory
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onSelectCategory(cat) }
                        .testTag("wizard_category_${cat.id}"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) EditorialBlueContainer else EditorialCardBg
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        if (isSelected) 1.5.dp else 1.dp,
                        if (isSelected) EditorialBlue else EditorialCardBorder
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = cat.emoji, fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = cat.title,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (isSelected) EditorialNavy else EditorialTextDark
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Image Preview & Selection Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = EditorialCardBg),
            border = androidx.compose.foundation.BorderStroke(
                if (imageUri != null) 2.dp else 1.dp,
                if (imageUri != null) EditorialBlue else EditorialCardBorder
            )
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (imageUri != null) {
                    SmartImage(
                        uriString = imageUri,
                        contentDescription = "Selected Person Photo",
                        modifier = Modifier.fillMaxSize()
                    )

                    // Selected Photo Badge
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(10.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xCC001D36))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Photo Selected ✓",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                    }

                    // Remove/Replace action button
                    IconButton(
                        onClick = onRemovePhoto,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(10.dp)
                            .clip(CircleShape)
                            .background(Color(0xCC001D36))
                            .testTag("btn_remove_person_photo")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove Photo",
                            tint = Color.White
                        )
                    }
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = null,
                            tint = EditorialSecondaryText,
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No photo selected yet",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = EditorialSecondaryText
                        )
                        Text(
                            text = "Tap Camera or Gallery below",
                            style = MaterialTheme.typography.labelSmall,
                            color = EditorialSecondaryText.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Actions: Camera, Gallery, Sample
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onTakePhoto,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("btn_take_person_photo"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EditorialNavy,
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color.White)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Camera", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
            }

            Button(
                onClick = onPickGallery,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("btn_gallery_person_photo"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EditorialCardBg,
                    contentColor = EditorialTextDark
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, EditorialCardBorder)
            ) {
                Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(18.dp), tint = EditorialTextDark)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Gallery", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onUseSample,
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .testTag("btn_sample_person_photo"),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, EditorialBlue.copy(alpha = 0.5f))
        ) {
            Text("✨ Use High-Quality Sample Model Photo", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = EditorialBlue)
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 24H Privacy Guarantee Banner
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(EditorialBlueContainer.copy(alpha = 0.5f))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = null,
                tint = EditorialBlue,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Privacy Protected: Photos are auto-deleted from server within 24h.",
                style = MaterialTheme.typography.labelSmall,
                color = EditorialNavy
            )
        }
    }
}

// STEP 2: SELECT CLOTHING CATEGORY
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Step2SelectClothingCategory(
    personCategory: PersonCategory,
    selectedClothing: String,
    onSelect: (String) -> Unit
) {
    val items = CategoryCatalog.standardClothingCategories

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "2. Select Clothing Category",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
            color = EditorialNavy
        )
        Text(
            text = "Choose what type of outfit you want to try on.",
            style = MaterialTheme.typography.bodyMedium,
            color = EditorialSecondaryText
        )

        Spacer(modifier = Modifier.height(16.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items.forEach { clothing ->
                val isSelected = clothing.equals(selectedClothing, ignoreCase = true)
                Card(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onSelect(clothing) }
                        .testTag("clothing_chip_${clothing.replace(" ", "_")}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) EditorialBlue else EditorialCardBg
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) EditorialBlue else EditorialCardBorder
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Checkroom,
                            contentDescription = null,
                            tint = if (isSelected) Color.White else EditorialSecondaryText,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = clothing,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 14.sp
                            ),
                            color = if (isSelected) Color.White else EditorialTextDark
                        )
                    }
                }
            }
        }
    }
}

// STEP 3: OUTFIT SPECIFICATION (PHOTO UPLOAD OR TEXT DESCRIPTION)
@Composable
private fun Step3OutfitInput(
    clothingCategory: String,
    imageUri: String?,
    outfitDescription: String,
    onTakePhoto: () -> Unit,
    onPickGallery: () -> Unit,
    onRemovePhoto: () -> Unit,
    onUseSample: () -> Unit,
    onUpdateDescription: (String) -> Unit
) {
    var inputModeTab by remember { mutableIntStateOf(if (!imageUri.isNullOrBlank()) 0 else if (outfitDescription.isNotBlank()) 1 else 0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "3. Choose Outfit Details",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
            color = EditorialNavy
        )
        Text(
            text = "Category: $clothingCategory • Upload reference photo OR write description",
            style = MaterialTheme.typography.bodyMedium,
            color = EditorialBlue
        )

        Spacer(modifier = Modifier.height(14.dp))

        // TAB SWITCHER: PHOTO VS TEXT
        TabRow(
            selectedTabIndex = inputModeTab,
            containerColor = EditorialCardBg,
            contentColor = EditorialBlue,
            indicator = { tabPositions ->
                SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[inputModeTab]),
                    color = EditorialBlue,
                    height = 3.dp
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, EditorialCardBorder, RoundedCornerShape(14.dp))
        ) {
            Tab(
                selected = inputModeTab == 0,
                onClick = { inputModeTab = 0 },
                text = {
                    Text(
                        "📷 Reference Photo",
                        fontWeight = if (inputModeTab == 0) FontWeight.Bold else FontWeight.Normal,
                        color = if (inputModeTab == 0) EditorialBlue else EditorialSecondaryText
                    )
                }
            )
            Tab(
                selected = inputModeTab == 1,
                onClick = { inputModeTab = 1 },
                text = {
                    Text(
                        "✍️ Text Description",
                        fontWeight = if (inputModeTab == 1) FontWeight.Bold else FontWeight.Normal,
                        color = if (inputModeTab == 1) EditorialBlue else EditorialSecondaryText
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (inputModeTab == 0) {
            // Reference Image Preview
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = EditorialCardBg),
                border = androidx.compose.foundation.BorderStroke(
                    if (imageUri != null) 2.dp else 1.dp,
                    if (imageUri != null) EditorialBlue else EditorialCardBorder
                )
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    if (imageUri != null) {
                        SmartImage(
                            uriString = imageUri,
                            contentDescription = "Selected Garment Photo",
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(10.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xCC001D36))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Garment Ready ✓",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White
                            )
                        }

                        IconButton(
                            onClick = onRemovePhoto,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(10.dp)
                                .clip(CircleShape)
                                .background(Color(0xCC001D36))
                                .testTag("btn_remove_clothing_photo")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove Clothing Photo",
                                tint = Color.White
                            )
                        }
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Checkroom,
                                contentDescription = null,
                                tint = EditorialSecondaryText,
                                modifier = Modifier.size(44.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Upload reference image of $clothingCategory",
                                style = MaterialTheme.typography.bodyMedium,
                                color = EditorialSecondaryText
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Actions: Camera, Gallery, Sample
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onTakePhoto,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("btn_take_clothing_photo"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EditorialNavy,
                        contentColor = Color.White
                    )
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color.White)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Camera", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                }

                Button(
                    onClick = onPickGallery,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("btn_gallery_clothing_photo"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EditorialCardBg,
                        contentColor = EditorialTextDark
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EditorialCardBorder)
                ) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(18.dp), tint = EditorialTextDark)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Gallery", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onUseSample,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("btn_sample_clothing_photo"),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, EditorialBlue.copy(alpha = 0.5f))
            ) {
                Text("✨ Use Designer Sample Garment", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = EditorialBlue)
            }
        } else {
            // Text Prompt input field
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = EditorialCardBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, EditorialCardBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Describe your desired $clothingCategory:",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = EditorialNavy
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = outfitDescription,
                        onValueChange = onUpdateDescription,
                        placeholder = {
                            Text("e.g. Classic navy blue blazer with golden brass buttons and tailored lapels...")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .testTag("outfit_description_input"),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EditorialBlue,
                            unfocusedBorderColor = EditorialCardBorder,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        maxLines = 4
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Quick style suggestions (Tap to apply):",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = EditorialSecondaryText
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    val promptSuggestions = when (clothingCategory) {
                        "Pant + Shirt" -> listOf("Navy blue crisp formal shirt with slim-fit beige chinos", "White linen button-down shirt with charcoal pleated trousers", "Sky blue Oxford shirt with dark navy dress pants")
                        "T-Shirt + Jeans" -> listOf("Classic white crew neck cotton t-shirt with slim indigo denim jeans", "Black graphic minimalist tee with faded grey denim jeans", "Olive green vintage wash t-shirt with raw blue jeans")
                        "Formal Suit", "Suit" -> listOf("Midnight blue formal 3-piece tuxedo with satin lapels", "Charcoal grey tailored Italian wool blazer suit", "Classic black two-piece notch lapel business suit")
                        "Jacket" -> listOf("Black distressed leather biker jacket with silver zippers", "Olive green classic bomber jacket with ribbed collar", "Camel brown suede trucker jacket")
                        "Traditional Clothes", "Traditional Wear" -> listOf("Royal silk embroidered sherwani with zardosi work", "Festive maroon kurta pajama with gold nehru jacket", "Traditional banarasi silk ensemble")
                        "Kurta Pajama", "Kurta" -> listOf("Royal blue silk kurta with white churidar pajama", "Embroidered festive yellow linen kurta set", "Emerald green festive pathani kurta suit")
                        "Dress" -> listOf("Elegant crimson evening gown with flowing silk drape", "Pastel floral summer midi dress with tie waist", "Cocktail off-shoulder navy party dress")
                        "Saree" -> listOf("Royal maroon Banarasi silk saree with golden zari borders", "Pastel pink Kanjeevaram silk wedding saree", "Emerald green georgette designer festive saree")
                        else -> listOf("Royal blue tailored garment with modern fit", "Contemporary streetwear casual outfit", "Designer festive embroidered piece")
                    }

                    promptSuggestions.forEach { suggestion ->
                        Text(
                            text = "• $suggestion",
                            style = MaterialTheme.typography.bodySmall,
                            color = EditorialBlue,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onUpdateDescription(suggestion) }
                                .padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

// STEP 4: FIT SELECTION & REVIEW & "GENERATE NEW LOOK"
@Composable
private fun Step4FitAndReview(
    selectedPersonCategory: PersonCategory,
    selectedClothingCategory: String,
    personImageUri: String?,
    clothingImageUri: String?,
    outfitDescription: String,
    selectedFit: FitStyle,
    onSelectFit: (FitStyle) -> Unit,
    onExecuteTryOn: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "4. Select Fit & Generate",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
            color = EditorialNavy
        )
        Text(
            text = "Select how the garment should fit your body.",
            style = MaterialTheme.typography.bodyMedium,
            color = EditorialSecondaryText
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Fit Style Options
        FitStyle.entries.forEach { fit ->
            val isSelected = fit == selectedFit
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .clickable { onSelectFit(fit) }
                    .testTag("fit_style_card_${fit.id}"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) EditorialBlueContainer else EditorialCardBg
                ),
                border = androidx.compose.foundation.BorderStroke(
                    if (isSelected) 1.5.dp else 1.dp,
                    if (isSelected) EditorialBlue else EditorialCardBorder
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = fit.iconEmoji,
                        fontSize = 28.sp,
                        modifier = Modifier.padding(end = 14.dp)
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = fit.title,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = EditorialNavy
                        )
                        Text(
                            text = fit.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = EditorialSecondaryText
                        )
                    }

                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Selected",
                            tint = EditorialBlue,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Summary Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = EditorialCardBg),
            border = androidx.compose.foundation.BorderStroke(1.dp, EditorialCardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Summary:",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = EditorialNavy
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "• Person: ${selectedPersonCategory.title} (Face & Identity Preserved)",
                    style = MaterialTheme.typography.bodySmall,
                    color = EditorialTextDark
                )
                Text(
                    text = "• Category: $selectedClothingCategory",
                    style = MaterialTheme.typography.bodySmall,
                    color = EditorialTextDark
                )
                if (outfitDescription.isNotBlank()) {
                    Text(
                        text = "• Prompt: \"$outfitDescription\"",
                        style = MaterialTheme.typography.bodySmall,
                        color = EditorialBlue
                    )
                }
                Text(
                    text = "• Fit Style: ${selectedFit.title}",
                    style = MaterialTheme.typography.bodySmall,
                    color = EditorialTextDark
                )
                Text(
                    text = "• Privacy: Auto-delete in 24 hours 🔒",
                    style = MaterialTheme.typography.bodySmall,
                    color = EditorialSecondaryText
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // PROMINENT "TRY ON MY PHOTO" / "GENERATE NEW LOOK" HERO BUTTON
        Button(
            onClick = onExecuteTryOn,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("try_on_my_photo_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = EditorialBlue,
                contentColor = Color.White
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                modifier = Modifier.size(22.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "TRY ON MY PHOTO",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun WizardBottomNavButtons(
    currentStep: Int,
    totalSteps: Int,
    canProceed: Boolean,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, EditorialBorderSubtle),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (currentStep > 1) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("wizard_prev_button"),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EditorialCardBorder)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(18.dp), tint = EditorialNavy)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Previous", color = EditorialNavy, fontWeight = FontWeight.SemiBold)
                }
            }

            Button(
                onClick = onNext,
                enabled = canProceed,
                modifier = Modifier
                    .weight(if (currentStep > 1) 1.6f else 1f)
                    .height(50.dp)
                    .testTag(if (currentStep == totalSteps) "wizard_try_on_my_photo_action" else "wizard_next_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EditorialBlue,
                    contentColor = Color.White
                )
            ) {
                if (currentStep == totalSteps) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("TRY ON MY PHOTO", fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                } else {
                    Text("Continue", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color.White)
                }
            }
        }
    }
}
