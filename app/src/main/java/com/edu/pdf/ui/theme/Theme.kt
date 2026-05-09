@file:Suppress("DEPRECATION")

package com.edu.pdf.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = BrandPrimary,
    error = SolidError,           // 🌟 GOD MODE: Default faded red ko hata kar Solid Red laga diya
    errorContainer = SolidError.copy(alpha = 0.1f), // Delete dialogs ke background ke liye
    tertiary = FolderColor,       // 🌟 FOLDERS: App ke saare folders ab ye color lenge
    background = LightBackground,
    surface = LightSurface,
    surfaceVariant = LightSurfaceVariant,
    onPrimary = Color.White,
    onError = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black
)

private val DarkColorScheme = darkColorScheme(
    primary = BrandPrimary,
    error = SolidError,           // 🌟 GOD MODE: Dark mode mein bhi ekdum SOLID Red aayega!
    errorContainer = SolidError.copy(alpha = 0.2f),
    tertiary = FolderColor,       // 🌟 FOLDERS: Yahan bhi folders same rahenge
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    onPrimary = Color.White,
    onError = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun PdfTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // 🌟 GOD MODE: Dynamic colors completely disabled to enforce strict Brand Identity
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            // 🌟 2026 PRO FIX: Status bar ko 100% invisible (transparent) kar diya.
            // Ab app seedha notch/camera cutout tak smoothly pahailegi bina kisi 'Patti' ke.
            window.statusBarColor = android.graphics.Color.TRANSPARENT

            // Edge-to-Edge transparent Navigation bar
            window.navigationBarColor = android.graphics.Color.TRANSPARENT
            window.isNavigationBarContrastEnforced = false

            // Adjust icon colors (black icons in light mode, white icons in dark mode)
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    // 🌟 THE ELITE FIX: Clean Native Material 3 Theme (Smooth without messy custom ripples)
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}