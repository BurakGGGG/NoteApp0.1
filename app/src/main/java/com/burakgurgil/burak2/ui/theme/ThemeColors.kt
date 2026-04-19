package com.burakgurgil.burak2.ui.theme

import androidx.compose.ui.graphics.Color

data class ThemeColors(
    val primary: Color,
    val secondary: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val error: Color,
    val addButton: Color,
    val editButton: Color,
    val deleteButton: Color,
    val settingsButton: Color
)

val themeColors = mapOf(
    ThemeType.PAPER to ThemeColors(
        primary = Color(0xFF2C2C2E),      // Mürekkep siyahı
        secondary = Color(0xFF8B7355),    // Toprak tonu
        textPrimary = Color(0xFF1C1C1E),
        textSecondary = Color(0xFF6E6E73),
        background = Color(0xFFFBF8F3),   // Kirli beyaz (kağıt)
        surface = Color(0xFFFDFBF7),      // Hafif krem
        surfaceVariant = Color(0xFFF0ECE5),
        error = Color(0xFFB00020),
        addButton = Color(0xFF2C2C2E),
        editButton = Color(0xFF8B7355),
        deleteButton = Color(0xFFB00020),
        settingsButton = Color(0xFFF0ECE5)
    ),
    ThemeType.MIDNIGHT to ThemeColors(
        primary = Color(0xFF6B9FFF),      // Yumuşak gece mavisi
        secondary = Color(0xFF9BB5FF),
        textPrimary = Color(0xFFE8EAED),
        textSecondary = Color(0xFF9AA0A6),
        background = Color(0xFF0A1628),   // Derin gece
        surface = Color(0xFF14243A),
        surfaceVariant = Color(0xFF1E2F47),
        error = Color(0xFFFF6B6B),
        addButton = Color(0xFF6B9FFF),
        editButton = Color(0xFF9BB5FF),
        deleteButton = Color(0xFFFF6B6B),
        settingsButton = Color(0xFF1E2F47)
    ),
    ThemeType.TERMINAL to ThemeColors(
        primary = Color(0xFF00FF41),      // Neon yeşil (Matrix)
        secondary = Color(0xFF39FF14),
        textPrimary = Color(0xFF00FF41),
        textSecondary = Color(0xFF009933),
        background = Color(0xFF000000),
        surface = Color(0xFF0D0D0D),
        surfaceVariant = Color(0xFF1A1A1A),
        error = Color(0xFFFF3B30),
        addButton = Color(0xFF00FF41),
        editButton = Color(0xFF39FF14),
        deleteButton = Color(0xFFFF3B30),
        settingsButton = Color(0xFF1A1A1A)
    ),
    ThemeType.FOCUS to ThemeColors(
        primary = Color(0xFF424242),      // Minimalist gri
        secondary = Color(0xFF757575),
        textPrimary = Color(0xFF212121),
        textSecondary = Color(0xFF757575),
        background = Color(0xFFF5F5F5),
        surface = Color(0xFFFFFFFF),
        surfaceVariant = Color(0xFFEEEEEE),
        error = Color(0xFFD32F2F),
        addButton = Color(0xFF424242),
        editButton = Color(0xFF757575),
        deleteButton = Color(0xFFD32F2F),
        settingsButton = Color(0xFFEEEEEE)
    ),
    ThemeType.SUNSET to ThemeColors(
        primary = Color(0xFFFF5722),      // Sıcak turuncu-pembe
        secondary = Color(0xFFFF9800),
        textPrimary = Color(0xFF3E2723),
        textSecondary = Color(0xFF5D4037),
        background = Color(0xFFFFF3E0),
        surface = Color(0xFFFFFFFF),
        surfaceVariant = Color(0xFFFFE0B2),
        error = Color(0xFFE91E63),
        addButton = Color(0xFFFF5722),
        editButton = Color(0xFFFF9800),
        deleteButton = Color(0xFFE91E63),
        settingsButton = Color(0xFFFFE0B2)
    ),
    ThemeType.FOREST to ThemeColors(
        primary = Color(0xFF2E7D32),      // Koyu yeşil + toprak
        secondary = Color(0xFF558B2F),
        textPrimary = Color(0xFF1B5E20),
        textSecondary = Color(0xFF33691E),
        background = Color(0xFFF1F8E9),
        surface = Color(0xFFFFFFFF),
        surfaceVariant = Color(0xFFDCEDC8),
        error = Color(0xFFC62828),
        addButton = Color(0xFF2E7D32),
        editButton = Color(0xFF558B2F),
        deleteButton = Color(0xFFC62828),
        settingsButton = Color(0xFFDCEDC8)
    ),
    ThemeType.DEFAULT to ThemeColors(
        primary = Color(0xFF5E5CE6),
        secondary = Color(0xFF34C759),
        textPrimary = Color(0xFF1C1C1E),
        textSecondary = Color(0xFF8E8E93),
        background = Color(0xFFF2F2F7),
        surface = Color(0xFFFFFFFF),
        surfaceVariant = Color(0xFFE5E5EA),
        error = Color(0xFFFF3B30),
        addButton = Color(0xFF5E5CE6),
        editButton = Color(0xFF007AFF),
        deleteButton = Color(0xFFFF3B30),
        settingsButton = Color(0xFFE5E5EA)
    ),
    ThemeType.DARK to ThemeColors(
        primary = Color(0xFFBF5AF2),
        secondary = Color(0xFF32D74B),
        textPrimary = Color(0xFFFFFFFF),
        textSecondary = Color(0xFFAEAEB2),
        background = Color(0xFF000000),
        surface = Color(0xFF1C1C1E),
        surfaceVariant = Color(0xFF2C2C2E),
        error = Color(0xFFFF453A),
        addButton = Color(0xFFBF5AF2),
        editButton = Color(0xFF0A84FF),
        deleteButton = Color(0xFFFF453A),
        settingsButton = Color(0xFF2C2C2E)
    )
)
 