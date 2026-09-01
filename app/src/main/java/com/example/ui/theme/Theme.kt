package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = FitLookPurple,
    onPrimary = Color.White,
    primaryContainer = EditorialBlueContainer,
    onPrimaryContainer = FitLookTextPrimary,
    secondary = FitLookPink,
    onSecondary = Color.White,
    secondaryContainer = FitLookDarkCard,
    onSecondaryContainer = FitLookTextPrimary,
    tertiary = FitLookPinkLight,
    onTertiary = Color.White,
    background = FitLookDarkNavyBg,
    onBackground = FitLookTextPrimary,
    surface = FitLookDarkSurface,
    onSurface = FitLookTextPrimary,
    surfaceVariant = FitLookDarkCard,
    onSurfaceVariant = FitLookTextSecondary,
    outline = FitLookDarkCardBorder,
    error = EditorialError,
    onError = Color.White
)

private val LightColorScheme = DarkColorScheme // Enforce luxury dark theme throughout the app

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to FitLook AI Dark Premium UI
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

