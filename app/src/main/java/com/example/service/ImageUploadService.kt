package com.example.service

import android.content.Context
import android.net.Uri

data class ImageValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)

class ImageUploadService(private val context: Context) {

    fun validatePersonImage(uriString: String?): ImageValidationResult {
        if (uriString.isNullOrBlank()) {
            return ImageValidationResult(
                isValid = false,
                errorMessage = "Please upload or take a photo to continue."
            )
        }

        // Check if sample or valid resource/uri
        if (uriString.startsWith("drawable/") || uriString.startsWith("android.resource://")) {
            return ImageValidationResult(isValid = true)
        }

        try {
            val uri = Uri.parse(uriString)
            val scheme = uri.scheme
            if (scheme != "content" && scheme != "file" && scheme != "http" && scheme != "https") {
                return ImageValidationResult(
                    isValid = false,
                    errorMessage = "Please upload a clearer full-body photo."
                )
            }
            return ImageValidationResult(isValid = true)
        } catch (e: Exception) {
            return ImageValidationResult(
                isValid = false,
                errorMessage = "Invalid image format. Please select a clear JPG or PNG photo."
            )
        }
    }

    fun validateClothingImage(uriString: String?): ImageValidationResult {
        if (uriString.isNullOrBlank()) {
            return ImageValidationResult(
                isValid = false,
                errorMessage = "Please upload or select a clothing item photo."
            )
        }

        if (uriString.startsWith("drawable/") || uriString.startsWith("android.resource://")) {
            return ImageValidationResult(isValid = true)
        }

        try {
            val uri = Uri.parse(uriString)
            val scheme = uri.scheme
            if (scheme != "content" && scheme != "file" && scheme != "http" && scheme != "https") {
                return ImageValidationResult(
                    isValid = false,
                    errorMessage = "Please upload a clothing image where the item is clearly visible."
                )
            }
            return ImageValidationResult(isValid = true)
        } catch (e: Exception) {
            return ImageValidationResult(
                isValid = false,
                errorMessage = "Please upload a clothing image where the item is clearly visible."
            )
        }
    }
}
