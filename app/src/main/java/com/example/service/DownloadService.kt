package com.example.service

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class DownloadService(private val context: Context) {

    suspend fun saveImageToGallery(uriString: String, lookName: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val bitmap = loadBitmap(uriString) ?: return@withContext Result.failure(Exception("Image not found"))

            val fileName = "FitLook_${lookName}_${System.currentTimeMillis()}.jpg"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/FitLookAI")
                }

                val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    ?: return@withContext Result.failure(Exception("Failed to create MediaStore entry"))

                context.contentResolver.openOutputStream(uri)?.use { stream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream)
                }
                Result.success("Saved to Pictures/FitLookAI in your gallery")
            } else {
                val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "FitLookAI")
                if (!dir.exists()) dir.mkdirs()
                val file = File(dir, fileName)
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 95, out)
                }
                Result.success("Saved to Gallery: ${file.name}")
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun loadBitmap(uriString: String): Bitmap? {
        return try {
            when {
                uriString == "drawable/img_demo_person_man" ->
                    BitmapFactory.decodeResource(context.resources, com.example.R.drawable.img_demo_person_man)
                uriString == "drawable/img_demo_person_woman" ->
                    BitmapFactory.decodeResource(context.resources, com.example.R.drawable.img_demo_person_woman)
                uriString == "drawable/img_demo_clothing_sherwani" ->
                    BitmapFactory.decodeResource(context.resources, com.example.R.drawable.img_demo_clothing_sherwani)
                uriString.startsWith("drawable/") -> {
                    val resName = uriString.removePrefix("drawable/")
                    val resId = context.resources.getIdentifier(resName, "drawable", context.packageName)
                    if (resId != 0) BitmapFactory.decodeResource(context.resources, resId) else null
                }
                uriString.startsWith("file://") -> {
                    val path = uriString.removePrefix("file://")
                    BitmapFactory.decodeFile(path)
                }
                uriString.startsWith("content://") -> {
                    context.contentResolver.openInputStream(Uri.parse(uriString))?.use {
                        BitmapFactory.decodeStream(it)
                    }
                }
                else -> BitmapFactory.decodeFile(uriString)
            }
        } catch (e: Exception) {
            null
        }
    }
}
