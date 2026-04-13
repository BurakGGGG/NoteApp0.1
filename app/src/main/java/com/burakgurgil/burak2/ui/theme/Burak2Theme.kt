package com.burakgurgil.burak2.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun Burak2Theme(
    themeType: ThemeType = ThemeType.DEFAULT,
    content: @Composable () -> Unit
) {
    val colors = themeColors[themeType] ?: themeColors[ThemeType.DEFAULT]!!
    val colorScheme = lightColorScheme(
        primary = colors.primary,
        onPrimary = colors.textPrimary,
        secondary = colors.secondary,
        onSecondary = colors.textPrimary,
        background = colors.background,
        onBackground = colors.textPrimary,
        surface = colors.surface,
        onSurface = colors.textPrimary,
        surfaceVariant = colors.surfaceVariant,
        onSurfaceVariant = colors.textSecondary,
        error = colors.error,
        onError = colors.textPrimary
    )

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
} 