package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// FitLook AI Premium Dark Theme Palette
val FitLookDarkNavyBg = Color(0xFF0B0F19)
val FitLookDarkSurface = Color(0xFF111726)
val FitLookDarkCard = Color(0xFF161E2E)
val FitLookDarkCardBorder = Color(0xFF263248)
val FitLookTextPrimary = Color(0xFFF1F5F9)
val FitLookTextSecondary = Color(0xFF94A3B8)
val FitLookTextMuted = Color(0xFF64748B)

// Purple to Pink Brand Gradients (FitLook AI Identity)
val FitLookPurple = Color(0xFF8B5CF6)
val FitLookPink = Color(0xFFEC4899)
val FitLookVioletDark = Color(0xFF6D28D9)
val FitLookPinkLight = Color(0xFFF472B6)

val FitLookGradientPrimary = Brush.horizontalGradient(
    listOf(FitLookPurple, FitLookPink)
)
val FitLookGradientAccent = Brush.linearGradient(
    listOf(Color(0xFF7C3AED), Color(0xFFDB2777), Color(0xFFF43F5E))
)
val FitLookGradientCard = Brush.verticalGradient(
    listOf(Color(0xFF1E293B), Color(0xFF0F172A))
)

// Legacy / Compatibility Palette (Dark Navy Fashion Identity)
val EditorialBg = FitLookDarkNavyBg
val EditorialTextDark = FitLookTextPrimary
val EditorialNavy = Color(0xFF0B0F19)
val EditorialSubtext = FitLookTextSecondary
val EditorialSecondaryText = FitLookTextSecondary

val EditorialBlue = Color(0xFF8B5CF6)
val EditorialBlueLight = Color(0xFFA78BFA)
val EditorialBlueDark = Color(0xFF6D28D9)
val EditorialBlueContainer = Color(0xFF241C38)
val EditorialBlueContainerBorder = Color(0xFF4C3870)

val EditorialCardBg = FitLookDarkCard
val EditorialCardBorder = FitLookDarkCardBorder
val EditorialBorderSubtle = Color(0xFF334155)
val EditorialActiveCard = Color(0xFF1E293B)

val EditorialDarkBanner = Color(0xFF0F172A)
val EditorialDarkBannerSub = FitLookTextSecondary
val EditorialDarkBannerIconBg = Color(0xFF1E293B)

val EditorialSuccess = Color(0xFF10B981)
val EditorialWarning = Color(0xFFF59E0B)
val EditorialError = Color(0xFFEF4444)

// Dark Theme Variants
val EditorialDarkBg = FitLookDarkNavyBg
val EditorialDarkSurface = FitLookDarkSurface
val EditorialDarkCard = FitLookDarkCard
val EditorialDarkCardBorder = FitLookDarkCardBorder
val EditorialDarkPrimary = FitLookPurple
val EditorialDarkPrimaryContainer = EditorialBlueContainer
val EditorialDarkTextPrimary = FitLookTextPrimary
val EditorialDarkTextSecondary = FitLookTextSecondary

// Legacy aliases for backward compatibility
val FashionGoldPrimary = EditorialBlue
val FashionGoldLight = EditorialBlueLight
val FashionGoldDark = Color(0xFF6D28D9)
val FashionIndigoAccent = EditorialBlue
val FashionIndigoLight = EditorialBlueLight
val FashionRoseAccent = FitLookPink
val FashionTealAccent = Color(0xFF06B6D4)
val FashionSuccess = EditorialSuccess
val FashionWarning = EditorialWarning
val FashionError = EditorialError
val FashionDarkBg = FitLookDarkNavyBg
val FashionDarkSurface = FitLookDarkSurface
val FashionDarkCard = FitLookDarkCard
val FashionDarkCardBorder = FitLookDarkCardBorder
val FashionTextPrimary = FitLookTextPrimary
val FashionTextSecondary = FitLookTextSecondary
val FashionTextMuted = FitLookTextMuted
val FashionLightBg = FitLookDarkNavyBg
val FashionLightSurface = FitLookDarkSurface
val FashionLightCard = FitLookDarkCard
val FashionLightCardBorder = FitLookDarkCardBorder
val FashionLightTextPrimary = FitLookTextPrimary
val FashionLightTextSecondary = FitLookTextSecondary


