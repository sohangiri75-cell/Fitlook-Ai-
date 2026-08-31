package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R

@Composable
fun SmartImage(
    uriString: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    if (uriString == null) {
        Box(
            modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Image,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        }
        return
    }

    when {
        uriString == "drawable/img_demo_person_man" -> {
            Image(
                painter = painterResource(id = R.drawable.img_demo_person_man),
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale
            )
        }
        uriString == "drawable/img_demo_person_woman" -> {
            Image(
                painter = painterResource(id = R.drawable.img_demo_person_woman),
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale
            )
        }
        uriString == "drawable/img_demo_clothing_sherwani" -> {
            Image(
                painter = painterResource(id = R.drawable.img_demo_clothing_sherwani),
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale
            )
        }
        uriString == "drawable/img_hero_fashion" -> {
            Image(
                painter = painterResource(id = R.drawable.img_hero_fashion),
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale
            )
        }
        uriString == "drawable/img_app_icon" -> {
            Image(
                painter = painterResource(id = R.drawable.img_app_icon),
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale
            )
        }
        uriString.startsWith("drawable/") -> {
            val context = LocalContext.current
            val resName = uriString.removePrefix("drawable/")
            val resId = context.resources.getIdentifier(resName, "drawable", context.packageName)
            if (resId != 0) {
                Image(
                    painter = painterResource(id = resId),
                    contentDescription = contentDescription,
                    modifier = modifier,
                    contentScale = contentScale
                )
            } else {
                Box(
                    modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        else -> {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(uriString)
                    .crossfade(true)
                    .build(),
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale
            )
        }
    }
}
