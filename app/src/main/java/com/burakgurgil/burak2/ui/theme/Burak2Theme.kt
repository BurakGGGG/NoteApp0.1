package com.burakgurgil.burak2.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun Burak2Theme(
    themeType: ThemeType = ThemeType.DEFAULT,
    useDynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val isDark = themeType == ThemeType.DARK
    
    val colorScheme = when {
        useDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (isDark) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        else -> {
            val colors = themeColors[themeType] ?: themeColors[ThemeType.DEFAULT]!!
            lightColorScheme(
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
        }
    }

    val colors = themeColors[themeType] ?: themeColors[ThemeType.DEFAULT]!!
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDark
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = getTypographyForTheme(themeType),
        content = content
    )
} 