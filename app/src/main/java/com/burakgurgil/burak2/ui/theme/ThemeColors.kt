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
    ThemeType.DEFAULT to ThemeColors(
        primary = Color(0xFF5E5CE6), // Modern indigo/purple
        secondary = Color(0xFF34C759), // Vibrant green
        textPrimary = Color(0xFF1C1C1E),
        textSecondary = Color(0xFF8E8E93),
        background = Color(0xFFF2F2F7), // iOS-like soft background
        surface = Color(0xFFFFFFFF),
        surfaceVariant = Color(0xFFE5E5EA),
        error = Color(0xFFFF3B30),
        addButton = Color(0xFF5E5CE6),
        editButton = Color(0xFF007AFF),
        deleteButton = Color(0xFFFF3B30),
        settingsButton = Color(0xFFE5E5EA)
    ),
    ThemeType.PASTEL to ThemeColors(
        primary = Color(0xFF9EA1D4),
        secondary = Color(0xFFA8D1D1),
        textPrimary = Color(0xFF4A4A4A),
        textSecondary = Color(0xFF9B9B9B),
        background = Color(0xFFFDFBF7), // Warm off-white
        surface = Color(0xFFFFFFFF),
        surfaceVariant = Color(0xFFF4F0EC),
        error = Color(0xFFFF9B9B),
        addButton = Color(0xFF9EA1D4),
        editButton = Color(0xFFA8D1D1),
        deleteButton = Color(0xFFFF9B9B),
        settingsButton = Color(0xFFF4F0EC)
    ),
    ThemeType.DARK to ThemeColors(
        primary = Color(0xFFBF5AF2),
        secondary = Color(0xFF32D74B),
        textPrimary = Color(0xFFFFFFFF),
        textSecondary = Color(0xFFAEAEB2),
        background = Color(0xFF000000), // Pure black for OLED
        surface = Color(0xFF1C1C1E), // Elevated surface
        surfaceVariant = Color(0xFF2C2C2E),
        error = Color(0xFFFF453A),
        addButton = Color(0xFFBF5AF2),
        editButton = Color(0xFF0A84FF),
        deleteButton = Color(0xFFFF453A),
        settingsButton = Color(0xFF2C2C2E)
    ),
    ThemeType.SPRING to ThemeColors(
        primary = Color(0xFF7CB342),
        secondary = Color(0xFF81C784),
        textPrimary = Color(0xFF1B5E20),
        textSecondary = Color(0xFF4CAF50),
        background = Color(0xFFF1F8E9),
        surface = Color(0xFFFFFFFF),
        surfaceVariant = Color(0xFFDCEDC8),
        error = Color(0xFFE57373),
        addButton = Color(0xFF7CB342),
        editButton = Color(0xFF81C784),
        deleteButton = Color(0xFFE57373),
        settingsButton = Color(0xFFDCEDC8)
    ),
    ThemeType.SUMMER to ThemeColors(
        primary = Color(0xFFFFB300),
        secondary = Color(0xFFFFD54F),
        textPrimary = Color(0xFFE65100),
        textSecondary = Color(0xFFFF8F00),
        background = Color(0xFFFFF8E1),
        surface = Color(0xFFFFFFFF),
        surfaceVariant = Color(0xFFFFECB3),
        error = Color(0xFFE57373),
        addButton = Color(0xFFFFB300),
        editButton = Color(0xFFFFD54F),
        deleteButton = Color(0xFFE57373),
        settingsButton = Color(0xFFFFECB3)
    ),
    ThemeType.AUTUMN to ThemeColors(
        primary = Color(0xFFF4511E),
        secondary = Color(0xFFFF8A65),
        textPrimary = Color(0xFFBF360C),
        textSecondary = Color(0xFFE64A19),
        background = Color(0xFFFBE9E7),
        surface = Color(0xFFFFFFFF),
        surfaceVariant = Color(0xFFFFCCBC),
        error = Color(0xFFE57373),
        addButton = Color(0xFFF4511E),
        editButton = Color(0xFFFF8A65),
        deleteButton = Color(0xFFE57373),
        settingsButton = Color(0xFFFFCCBC)
    ),
    ThemeType.WINTER to ThemeColors(
        primary = Color(0xFF039BE5),
        secondary = Color(0xFF81D4FA),
        textPrimary = Color(0xFF01579B),
        textSecondary = Color(0xFF0288D1),
        background = Color(0xFFE1F5FE),
        surface = Color(0xFFFFFFFF),
        surfaceVariant = Color(0xFFB3E5FC),
        error = Color(0xFFE57373),
        addButton = Color(0xFF039BE5),
        editButton = Color(0xFF81D4FA),
        deleteButton = Color(0xFFE57373),
        settingsButton = Color(0xFFB3E5FC)
    )
) 