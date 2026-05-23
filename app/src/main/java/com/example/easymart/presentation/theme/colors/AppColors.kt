package com.example.easymart.presentation.theme.colors

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class AppColors(
    // Brand
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val primaryGradientStart: Color,
    val primaryGradientEnd: Color,
    val focusRing: Color,

    // Secondary / Tertiary
    val secondary: Color,
    val onSecondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,
    val tertiary: Color,
    val onTertiary: Color,

    // Semantic / Status
    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val success: Color,
    val onSuccess: Color,
    val successContainer: Color,
    val warning: Color,
    val onWarning: Color,
    val warningContainer: Color,
    val info: Color,
    val onInfo: Color,
    val infoContainer: Color,

    // Background / Surface
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val surfaceVariant: Color,
    val surfaceBright: Color,
    val surfaceDim: Color,
    val surfaceContainerLowest: Color,
    val surfaceContainerLow: Color,
    val surfaceContainer: Color,
    val surfaceContainerHigh: Color,
    val surfaceContainerHighest: Color,
    val scrim: Color,

    // Text / Icon
    val textPrimary: Color,
    val textSecondary: Color,
    val onSurfaceVariant: Color,
    val iconMuted: Color,
    val onSelected: Color,

    // Border / Divider
    val outline: Color,
    val outlineVariant: Color,
    val divider: Color,

    // Interaction / State
    val pressed: Color,
    val selected: Color,
    val disabledContainer: Color,
    val disabledContent: Color,

    // AI / Special feature
    val aiAccent: Color,
    val aiAccentLight: Color,
    val aiAccentSoft: Color
)

val LocalAppColors = staticCompositionLocalOf { LightAppColors }