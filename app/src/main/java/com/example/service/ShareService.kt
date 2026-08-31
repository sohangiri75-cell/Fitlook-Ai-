package com.example.service

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.data.model.ProductItem
import com.example.data.model.ShopProfile
import com.example.data.model.TryOnResultData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URLEncoder

class ShareService(private val context: Context) {

    /**
     * Contact Shop on WhatsApp with pre-filled product interest message.
     * Rule:
     * "Hi, I tried this product on FitLook AI and I'm interested in buying it.
     * Product: [Product Name]
     * Price: ₹[Price]"
     */
    fun contactShopOnWhatsApp(
        whatsappNumber: String,
        productName: String,
        productPrice: Double,
        shopName: String = "FitLook AI Store"
    ) {
        val cleanNumber = whatsappNumber.replace(Regex("[^0-9]"), "")
        val formattedPrice = "₹%.0f".format(productPrice)
        val rawMessage = "Hi, I tried this product on FitLook AI and I'm interested in buying it.\nProduct: $productName\nPrice: $formattedPrice\nShop: $shopName"
        val encodedMessage = URLEncoder.encode(rawMessage, "UTF-8")

        try {
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=$cleanNumber&text=$encodedMessage")
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fallback to standard browser or generic share
            try {
                val webUri = Uri.parse("https://wa.me/$cleanNumber?text=$encodedMessage")
                val webIntent = Intent(Intent.ACTION_VIEW, webUri).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(webIntent)
            } catch (ex: Exception) {
                // Fallback text intent
                val textIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, rawMessage)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(Intent.createChooser(textIntent, "Contact Shop").apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    /**
     * Share Shop Catalogue link
     */
    fun shareShopCatalogue(shop: ShopProfile) {
        val message = "🛍️ Explore our virtual clothing catalogue on FitLook AI!\n" +
                "Try on our real designer collections on your own photo instantly before buying:\n\n" +
                "✨ ${shop.name}\n" +
                "📍 ${shop.location}\n" +
                "🔗 ${shop.shareUrl}\n\n" +
                "📲 WhatsApp: +${shop.whatsappNumber}"

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(Intent.createChooser(intent, "Share Shop Catalogue").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

    /**
     * Share Try-On Look with optional product metadata
     */
    suspend fun shareLook(look: TryOnResultData) = withContext(Dispatchers.IO) {
        try {
            val imageFile = prepareShareableImage(look.resultImageUri)
            val productName = look.productName ?: look.clothingCategory
            val priceStr = look.productPrice?.let { " - ₹%.0f".format(it) } ?: ""
            val shopStr = look.shopName?.let { " from $it" } ?: ""

            val message = "Check out my virtual try-on look for $productName$priceStr$shopStr using FitLook AI! ✨\n\"Try Before You Buy\"\nTry outfits on your photo at https://fitlook.ai"

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/jpeg"
                putExtra(Intent.EXTRA_TEXT, message)
                if (imageFile != null && imageFile.exists()) {
                    val authority = "${context.packageName}.fileprovider"
                    val contentUri = FileProvider.getUriForFile(context, authority, imageFile)
                    putExtra(Intent.EXTRA_STREAM, contentUri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            val chooser = Intent.createChooser(shareIntent, "Share your FitLook").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(chooser)
        } catch (e: Exception) {
            // Fallback to text share
            val textIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Check out my virtual try-on look for ${look.productName ?: look.clothingCategory} on FitLook AI! ✨ \"Try Before You Buy\""
                )
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(Intent.createChooser(textIntent, "Share FitLook").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        }
    }

    private fun prepareShareableImage(uriString: String): File? {
        return try {
            val cacheDir = File(context.cacheDir, "images")
            if (!cacheDir.exists()) cacheDir.mkdirs()
            val shareFile = File(cacheDir, "share_look_${System.currentTimeMillis()}.jpg")

            val bitmap = when {
                uriString == "drawable/img_demo_person_man" ->
                    BitmapFactory.decodeResource(context.resources, com.example.R.drawable.img_demo_person_man)
                uriString == "drawable/img_demo_person_woman" ->
                    BitmapFactory.decodeResource(context.resources, com.example.R.drawable.img_demo_person_woman)
                uriString == "drawable/img_demo_clothing_sherwani" ->
                    BitmapFactory.decodeResource(context.resources, com.example.R.drawable.img_demo_clothing_sherwani)
                uriString == "drawable/img_product_kurta" ->
                    BitmapFactory.decodeResource(context.resources, com.example.R.drawable.img_product_kurta)
                uriString == "drawable/img_product_saree" ->
                    BitmapFactory.decodeResource(context.resources, com.example.R.drawable.img_product_saree)
                uriString == "drawable/img_product_dress" ->
                    BitmapFactory.decodeResource(context.resources, com.example.R.drawable.img_product_dress)
                uriString == "drawable/img_product_jacket" ->
                    BitmapFactory.decodeResource(context.resources, com.example.R.drawable.img_product_jacket)
                uriString == "drawable/img_product_shirt" ->
                    BitmapFactory.decodeResource(context.resources, com.example.R.drawable.img_product_shirt)
                uriString.startsWith("drawable/") -> {
                    val resName = uriString.removePrefix("drawable/")
                    val resId = context.resources.getIdentifier(resName, "drawable", context.packageName)
                    if (resId != 0) BitmapFactory.decodeResource(context.resources, resId) else null
                }
                uriString.startsWith("file://") -> {
                    BitmapFactory.decodeFile(uriString.removePrefix("file://"))
                }
                uriString.startsWith("content://") -> {
                    context.contentResolver.openInputStream(Uri.parse(uriString))?.use {
                        BitmapFactory.decodeStream(it)
                    }
                }
                else -> {
                    val file = File(uriString)
                    if (file.exists()) BitmapFactory.decodeFile(file.absolutePath) else null
                }
            }

            if (bitmap != null) {
                FileOutputStream(shareFile).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 92, out)
                }
                shareFile
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
