package com.example.ui.screens

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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CategoryCatalog
import com.example.data.model.FitStyle
import com.example.data.model.PersonCategory
import com.example.ui.components.FitLookTopBar
import com.example.ui.components.SmartImage
import com.example.ui.theme.EditorialActiveCard
import com.example.ui.theme.EditorialBlue
import com.example.ui.theme.EditorialBlueContainer
import com.example.ui.theme.EditorialBlueContainerBorder
import com.example.ui.theme.EditorialBorderSubtle
import com.example.ui.theme.EditorialCardBg
import com.example.ui.theme.EditorialCardBorder
import com.example.ui.theme.EditorialDarkBanner
import com.example.ui.theme.EditorialDarkBannerIconBg
import com.example.ui.theme.EditorialDarkBannerSub
import com.example.ui.theme.EditorialNavy
import com.example.ui.theme.EditorialSecondaryText
import com.example.ui.theme.EditorialSubtext
import com.example.ui.theme.EditorialTextDark

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TryOnWizardScreen(
    currentStep: Int,
    selectedPersonCategory: PersonCategory,
    selectedClothingCategory: String,
    personImageUri: String?,
    clothingImageUri: String?,
    selectedFitStyle: FitStyle,
    onSelectPersonCategory: (PersonCategory) -> Unit,
    onSelectClothingCategory: (String) -> Unit,
    onSetPersonImage: (String?) -> Unit,
    onSetClothingImage: (String?) -> Unit,
    onSelectFitStyle: (FitStyle) -> Unit,
    onNavigateStep: (Int) -> Unit,
    onExecuteTryOn: () -> Unit,
    onBackClick: () -> Unit
) {
    // Gallery & Camera Launchers
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
    ) { bitmap ->
        if (bitmap != null) {
            onSetPersonImage(if (selectedPersonCategory == PersonCategory.MAN || selectedPersonCategory == PersonCategory.BOY) "drawable/img_demo_person_man" else "drawable/img_demo_person_woman")
        }
    }

    val clothingCameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            onSetClothingImage("drawable/img_demo_clothing_sherwani")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("tryon_wizard_screen")
    ) {
        FitLookTopBar(
            title = "Virtual Try-On",
            subtitle = "Step $currentStep of 5",
            showBackButton = true,
            onBackClick = {
                if (currentStep > 1) onNavigateStep(currentStep - 1) else onBackClick()
            }
        )

        // Step Progress Indicator Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (step in 1..5) {
                val isActive = step <= currentStep
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(
                            if (isActive) EditorialBlue else EditorialCardBorder
                        )
                )
            }
        }

        // Wizard Step Contents
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            AnimatedContent(
                targetState = currentStep,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "step_content"
            ) { step ->
                when (step) {
                    1 -> Step1SelectPersonCategory(
                        selectedCategory = selectedPersonCategory,
                        onSelect = {
                            onSelectPersonCategory(it)
                            onNavigateStep(2)
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
                    3 -> Step3UploadPersonPhoto(
                        personCategory = selectedPersonCategory,
                        imageUri = personImageUri,
                        onTakePhoto = { personCameraLauncher.launch(null) },
                        onPickGallery = { personGalleryLauncher.launch("image/*") },
                        onUseSample = {
                            onSetPersonImage(
                                if (selectedPersonCategory == PersonCategory.MAN || selectedPersonCategory == PersonCategory.BOY)
                                    "drawable/img_demo_person_man"
                                else
                                    "drawable/img_demo_person_woman"
                            )
                        }
                    )
                    4 -> Step4UploadClothingPhoto(
                        clothingCategory = selectedClothingCategory,
                        imageUri = clothingImageUri,
                        onTakePhoto = { clothingCameraLauncher.launch(null) },
                        onPickGallery = { clothingGalleryLauncher.launch("image/*") },
                        onUseSample = { onSetClothingImage("drawable/img_demo_clothing_sherwani") }
                    )
                    5 -> Step5SelectFitStyle(
                        selectedFit = selectedFitStyle,
                        onSelectFit = onSelectFitStyle
                    )
                }
            }
        }

        // Bottom Wizard Action Bar
        WizardBottomNavButtons(
            currentStep = currentStep,
            onBack = {
                if (currentStep > 1) onNavigateStep(currentStep - 1) else onBackClick()
            },
            onNext = {
                if (currentStep < 5) onNavigateStep(currentStep + 1) else onExecuteTryOn()
            }
        )
    }
}

// STEP 1: SELECT PERSON CATEGORY
@Composable
private fun Step1SelectPersonCategory(
    selectedCategory: PersonCategory,
    onSelect: (PersonCategory) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Who are you styling?",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            ),
            color = EditorialNavy
        )
        Text(
            text = "Select a person category to tailor garments & AI fit.",
            style = MaterialTheme.typography.bodyMedium,
            color = EditorialSecondaryText
        )

        Spacer(modifier = Modifier.height(18.dp))

        PersonCategory.entries.forEach { category ->
            val isSelected = category == selectedCategory
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onSelect(category) }
                    .testTag("person_category_card_${category.id}"),
                shape = RoundedCornerShape(20.dp),
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
                        text = category.emoji,
                        fontSize = 34.sp,
                        modifier = Modifier.padding(end = 16.dp)
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = category.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
                            color = EditorialNavy
                        )
                        Text(
                            text = category.subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = EditorialSecondaryText
                        )
                    }

                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Selected",
                            tint = EditorialBlue,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
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
    val items = CategoryCatalog.getClothingCategoriesFor(personCategory)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Select Clothing Category",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            ),
            color = EditorialNavy
        )
        Text(
            text = "Showing categories tailored for ${personCategory.title}",
            style = MaterialTheme.typography.bodyMedium,
            color = EditorialBlue
        )

        Spacer(modifier = Modifier.height(18.dp))

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
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Checkroom,
                            contentDescription = null,
                            tint = if (isSelected) Color.White else EditorialSecondaryText,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = clothing,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            ),
                            color = if (isSelected) Color.White else EditorialTextDark
                        )
                    }
                }
            }
        }
    }
}

// STEP 3: UPLOAD PERSON PHOTO
@Composable
private fun Step3UploadPersonPhoto(
    personCategory: PersonCategory,
    imageUri: String?,
    onTakePhoto: () -> Unit,
    onPickGallery: () -> Unit,
    onUseSample: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Upload Person Photo",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            ),
            color = EditorialNavy
        )
        Text(
            text = "Upload or take a clear full-body photo to try clothes on.",
            style = MaterialTheme.typography.bodyMedium,
            color = EditorialSecondaryText
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Photo Guidelines Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = EditorialCardBg),
            border = androidx.compose.foundation.BorderStroke(1.dp, EditorialCardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Photo Guidelines for Best AI Result:",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    ),
                    color = EditorialNavy
                )
                Spacer(modifier = Modifier.height(8.dp))
                InstructionItem(icon = Icons.Default.Visibility, text = "“Upload a clear full-body photo.”")
                InstructionItem(icon = Icons.Default.Straighten, text = "“Stand straight with arms slightly parted.”")
                InstructionItem(icon = Icons.Default.CheckCircle, text = "“Keep your full body clearly visible.”")
                InstructionItem(icon = Icons.Default.LightMode, text = "“Use a clear and well-lit image.”")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Image Preview & Selection
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp),
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
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
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
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = null,
                            tint = EditorialSecondaryText,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No photo selected yet",
                            style = MaterialTheme.typography.bodyMedium,
                            color = EditorialSecondaryText
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

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
                    containerColor = EditorialCardBg,
                    contentColor = EditorialTextDark
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, EditorialCardBorder)
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp), tint = EditorialTextDark)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Camera", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold))
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
                Text("Gallery", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold))
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = onUseSample,
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .testTag("btn_sample_person_photo"),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, EditorialBlue.copy(alpha = 0.5f))
        ) {
            Text("✨ Use High-Quality Sample Model Photo", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = EditorialBlue)
        }
    }
}

// STEP 4: UPLOAD CLOTHING PHOTO
@Composable
private fun Step4UploadClothingPhoto(
    clothingCategory: String,
    imageUri: String?,
    onTakePhoto: () -> Unit,
    onPickGallery: () -> Unit,
    onUseSample: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Upload Clothing Photo",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            ),
            color = EditorialNavy
        )
        Text(
            text = "Target Item: $clothingCategory",
            style = MaterialTheme.typography.bodyMedium,
            color = EditorialBlue
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Guidelines
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = EditorialCardBg),
            border = androidx.compose.foundation.BorderStroke(1.dp, EditorialCardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Garment Photo Instructions:",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    ),
                    color = EditorialNavy
                )
                Spacer(modifier = Modifier.height(6.dp))
                InstructionItem(icon = Icons.Default.Visibility, text = "“Upload a clear photo of the clothing item.”")
                InstructionItem(icon = Icons.Default.CheckCircle, text = "The clothing image should ideally show one clothing item clearly.")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Image Preview & Selection
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp),
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
                        contentDescription = "Selected Clothing Photo",
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
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
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Checkroom,
                            contentDescription = null,
                            tint = EditorialSecondaryText,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Upload a photo of the clothing",
                            style = MaterialTheme.typography.bodyMedium,
                            color = EditorialSecondaryText
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Actions
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
                    containerColor = EditorialCardBg,
                    contentColor = EditorialTextDark
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, EditorialCardBorder)
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp), tint = EditorialTextDark)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Camera", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold))
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
                Text("Gallery", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold))
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = onUseSample,
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .testTag("btn_sample_clothing_photo"),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, EditorialBlue.copy(alpha = 0.5f))
        ) {
            Text("✨ Use Designer Garment Sample", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = EditorialBlue)
        }
    }
}

// STEP 5: SELECT FIT STYLE
@Composable
private fun Step5SelectFitStyle(
    selectedFit: FitStyle,
    onSelectFit: (FitStyle) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Select Fit Style",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            ),
            color = EditorialNavy
        )
        Text(
            text = "Choose how the garment should drape and contour.",
            style = MaterialTheme.typography.bodyMedium,
            color = EditorialSecondaryText
        )

        Spacer(modifier = Modifier.height(20.dp))

        FitStyle.entries.forEach { fit ->
            val isSelected = fit == selectedFit
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onSelectFit(fit) }
                    .testTag("fit_style_card_${fit.id}"),
                shape = RoundedCornerShape(20.dp),
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
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = fit.iconEmoji,
                        fontSize = 32.sp,
                        modifier = Modifier.padding(end = 16.dp)
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = fit.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
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
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WizardBottomNavButtons(
    currentStep: Int,
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
                        .height(52.dp)
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
                modifier = Modifier
                    .weight(if (currentStep > 1) 1.5f else 1f)
                    .height(52.dp)
                    .testTag(if (currentStep == 5) "wizard_try_on_with_ai_button" else "wizard_next_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EditorialBlue,
                    contentColor = Color.White
                )
            ) {
                if (currentStep == 5) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("TRY ON WITH AI", fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                } else {
                    Text("Continue", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color.White)
                }
            }
        }
    }
}

@Composable
private fun InstructionItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = EditorialBlue,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = EditorialTextDark
        )
    }
}

