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
    primary = EditorialDarkPrimary,
    onPrimary = EditorialNavy,
    primaryContainer = EditorialDarkPrimaryContainer,
    onPrimaryContainer = Color.White,
    secondary = EditorialDarkPrimary,
    onSecondary = EditorialNavy,
    secondaryContainer = EditorialDarkCard,
    onSecondaryContainer = EditorialDarkTextPrimary,
    tertiary = EditorialBlueLight,
    onTertiary = Color.White,
    background = EditorialDarkBg,
    onBackground = EditorialDarkTextPrimary,
    surface = EditorialDarkSurface,
    onSurface = EditorialDarkTextPrimary,
    surfaceVariant = EditorialDarkCard,
    onSurfaceVariant = EditorialDarkTextSecondary,
    outline = EditorialDarkCardBorder,
    error = EditorialError,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = EditorialBlue,
    onPrimary = Color.White,
    primaryContainer = EditorialBlueContainer,
    onPrimaryContainer = EditorialNavy,
    secondary = EditorialNavy,
    onSecondary = Color.White,
    secondaryContainer = EditorialCardBg,
    onSecondaryContainer = EditorialTextDark,
    tertiary = EditorialSubtext,
    onTertiary = Color.White,
    background = EditorialBg,
    onBackground = EditorialTextDark,
    surface = Color.White,
    onSurface = EditorialTextDark,
    surfaceVariant = EditorialCardBg,
    onSurfaceVariant = EditorialSecondaryText,
    outline = EditorialCardBorder,
    error = EditorialError,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use custom luxury theme by default for fashion vibe
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
