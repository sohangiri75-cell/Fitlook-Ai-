package com.example.service

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import com.example.BuildConfig
import com.example.data.model.FitStyle
import com.example.data.model.PersonCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.concurrent.TimeUnit

class GeminiApiService(private val context: Context) {

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(90, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val apiKey: String
        get() = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

    fun isApiKeyConfigured(): Boolean {
        val key = apiKey
        return key.isNotBlank() && key != "MY_GEMINI_API_KEY" && key != "YOUR_GEMINI_API_KEY"
    }

    suspend fun generateVirtualTryOn(
        personImageUri: String,
        clothingImageUri: String?,
        outfitDescription: String?,
        personCategory: PersonCategory,
        clothingCategory: String,
        fitStyle: FitStyle,
        onProgress: (stage: String, progress: Float) -> Unit
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            onProgress("Analyzing Customer Photo & Garment...", 0.20f)

            val personBitmap = loadBitmap(personImageUri)
                ?: return@withContext Result.failure(Exception("Could not load uploaded person photo."))

            val clothingBitmap = if (!clothingImageUri.isNullOrBlank()) {
                loadBitmap(clothingImageUri)
            } else null

            val currentKey = apiKey
            if (!isApiKeyConfigured()) {
                Log.w("GeminiApiService", "Gemini API key is not configured or is default placeholder.")
                // Return failure with guidance so caller can trigger smart on-device neural composite
                return@withContext Result.failure(
                    Exception("Gemini API key is not configured. Please set your key in AI Studio Secrets.")
                )
            }

            onProgress("Connecting to Gemini AI Image Generation...", 0.40f)

            // Convert person bitmap to base64 JPEG
            val personBase64 = bitmapToBase64(personBitmap, 1024)
            val clothingBase64 = clothingBitmap?.let { bitmapToBase64(it, 1024) }

            // Build try-on prompt
            val promptBuilder = StringBuilder()
            promptBuilder.append("TASK: Virtual Try-On and Clothing Replacement.\n")
            promptBuilder.append("You are an expert AI fashion stylist and image editor. Replace ONLY the clothing worn by the person in the first image.\n")
            promptBuilder.append("TARGET CATEGORY: $clothingCategory\n")
            if (!outfitDescription.isNullOrBlank()) {
                promptBuilder.append("OUTFIT DESCRIPTION: $outfitDescription\n")
            }
            promptBuilder.append("FIT STYLE: ${fitStyle.title} (${fitStyle.description})\n")
            promptBuilder.append("TARGET PERSON: ${personCategory.title}\n")
            promptBuilder.append("CRITICAL INSTRUCTIONS:\n")
            promptBuilder.append("1. Keep the exact same person's face, facial expression, hair, and identity.\n")
            promptBuilder.append("2. Preserve the body pose, posture, and natural body proportions.\n")
            promptBuilder.append("3. Change ONLY the clothing/outfit according to the specified category and description.\n")
            if (clothingBase64 != null) {
                promptBuilder.append("4. Use the provided clothing reference image for the garment color, pattern, texture, and style.\n")
            }
            promptBuilder.append("5. Maintain the original lighting, realistic shadows, fabric drape, and background.\n")
            promptBuilder.append("6. Return a high-resolution, photorealistic, complete image.")

            val requestJson = JSONObject().apply {
                val contentsArray = JSONArray()
                val contentObj = JSONObject()
                val partsArray = JSONArray()

                // Text part
                val textPart = JSONObject().apply {
                    put("text", promptBuilder.toString())
                }
                partsArray.put(textPart)

                // Person image part
                val personPart = JSONObject().apply {
                    val inlineData = JSONObject().apply {
                        put("mimeType", "image/jpeg")
                        put("data", personBase64)
                    }
                    put("inlineData", inlineData)
                }
                partsArray.put(personPart)

                // Optional Clothing image part
                if (clothingBase64 != null) {
                    val clothingPart = JSONObject().apply {
                        val inlineData = JSONObject().apply {
                            put("mimeType", "image/jpeg")
                            put("data", clothingBase64)
                        }
                        put("inlineData", inlineData)
                    }
                    partsArray.put(clothingPart)
                }

                contentObj.put("parts", partsArray)
                contentsArray.put(contentObj)
                put("contents", contentsArray)

                // Generation config specifying IMAGE modality
                val generationConfig = JSONObject().apply {
                    val modalities = JSONArray().apply {
                        put("TEXT")
                        put("IMAGE")
                    }
                    put("responseModalities", modalities)
                }
                put("generationConfig", generationConfig)
            }

            onProgress("AI Generating New Look with Preserved Identity...", 0.65f)

            // Try image generation model
            val endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-image:generateContent?key=$currentKey"
            val requestBody = requestJson.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            val request = Request.Builder()
                .url(endpoint)
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseBodyString = response.body?.string()

            if (!response.isSuccessful || responseBodyString == null) {
                Log.e("GeminiApiService", "Gemini API failed with code ${response.code}: $responseBodyString")
                return@withContext Result.failure(
                    Exception("AI Generation server responded with code ${response.code}: ${response.message}")
                )
            }

            onProgress("Rendering High-Resolution Try-On...", 0.90f)

            val jsonResponse = JSONObject(responseBodyString)
            val candidates = jsonResponse.optJSONArray("candidates")
            if (candidates == null || candidates.length() == 0) {
                return@withContext Result.failure(Exception("No image candidates generated by AI."))
            }

            val firstCandidate = candidates.getJSONObject(0)
            val content = firstCandidate.optJSONObject("content")
            val parts = content?.optJSONArray("parts")

            var generatedImageBase64: String? = null

            if (parts != null) {
                for (i in 0 until parts.length()) {
                    val part = parts.getJSONObject(i)
                    val inlineData = part.optJSONObject("inlineData")
                    if (inlineData != null) {
                        val data = inlineData.optString("data")
                        if (data.isNotBlank()) {
                            generatedImageBase64 = data
                            break
                        }
                    }
                }
            }

            if (generatedImageBase64 != null) {
                val imageBytes = Base64.decode(generatedImageBase64, Base64.DEFAULT)
                val generatedBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                if (generatedBitmap != null) {
                    val dir = File(context.filesDir, "looks")
                    if (!dir.exists()) dir.mkdirs()
                    val resultFile = File(dir, "gemini_look_${System.currentTimeMillis()}.png")
                    FileOutputStream(resultFile).use { out ->
                        generatedBitmap.compress(Bitmap.CompressFormat.PNG, 95, out)
                    }
                    return@withContext Result.success(Uri.fromFile(resultFile).toString())
                }
            }

            // If only text was returned
            Result.failure(Exception("AI did not return an image part. Please try again."))
        } catch (e: Exception) {
            Log.e("GeminiApiService", "Virtual try-on error", e)
            Result.failure(e)
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap, maxDimension: Int = 1024): String {
        var scaled = bitmap
        val width = bitmap.width
        val height = bitmap.height
        if (width > maxDimension || height > maxDimension) {
            val ratio = width.toFloat() / height.toFloat()
            val newWidth: Int
            val newHeight: Int
            if (width > height) {
                newWidth = maxDimension
                newHeight = (maxDimension / ratio).toInt()
            } else {
                newHeight = maxDimension
                newWidth = (maxDimension * ratio).toInt()
            }
            scaled = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
        }

        val outputStream = ByteArrayOutputStream()
        scaled.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }

    fun loadBitmap(uriString: String): Bitmap? {
        return try {
            when {
                uriString == "drawable/img_demo_person_man" -> {
                    BitmapFactory.decodeResource(context.resources, com.example.R.drawable.img_demo_person_man)
                }
                uriString == "drawable/img_demo_person_woman" -> {
                    BitmapFactory.decodeResource(context.resources, com.example.R.drawable.img_demo_person_woman)
                }
                uriString == "drawable/img_demo_clothing_sherwani" -> {
                    BitmapFactory.decodeResource(context.resources, com.example.R.drawable.img_demo_clothing_sherwani)
                }
                uriString == "drawable/img_product_kurta" -> {
                    BitmapFactory.decodeResource(context.resources, com.example.R.drawable.img_product_kurta)
                }
                uriString == "drawable/img_product_saree" -> {
                    BitmapFactory.decodeResource(context.resources, com.example.R.drawable.img_product_saree)
                }
                uriString == "drawable/img_product_dress" -> {
                    BitmapFactory.decodeResource(context.resources, com.example.R.drawable.img_product_dress)
                }
                uriString == "drawable/img_product_jacket" -> {
                    BitmapFactory.decodeResource(context.resources, com.example.R.drawable.img_product_jacket)
                }
                uriString == "drawable/img_product_shirt" -> {
                    BitmapFactory.decodeResource(context.resources, com.example.R.drawable.img_product_shirt)
                }
                uriString == "drawable/img_shop_logo" -> {
                    BitmapFactory.decodeResource(context.resources, com.example.R.drawable.img_shop_logo)
                }
                uriString.startsWith("drawable/") -> {
                    val resName = uriString.removePrefix("drawable/")
                    val resId = context.resources.getIdentifier(resName, "drawable", context.packageName)
                    if (resId != 0) BitmapFactory.decodeResource(context.resources, resId) else null
                }
                uriString.startsWith("file://") || uriString.startsWith("/") -> {
                    val path = uriString.removePrefix("file://")
                    BitmapFactory.decodeFile(path)
                }
                uriString.startsWith("content://") -> {
                    val uri = Uri.parse(uriString)
                    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                    inputStream?.use { BitmapFactory.decodeStream(it) }
                }
                else -> null
            }
        } catch (e: Exception) {
            Log.e("GeminiApiService", "Failed to load bitmap from: $uriString", e)
            null
        }
    }
}
