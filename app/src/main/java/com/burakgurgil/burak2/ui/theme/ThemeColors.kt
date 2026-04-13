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
        primary = Color(0xFF6200EE),
        secondary = Color(0xFF03DAC6),
        textPrimary = Color(0xFF000000),
        textSecondary = Color(0xFF666666),
        background = Color(0xFFFFFFFF),
        surface = Color(0xFFFFFFFF),
        surfaceVariant = Color(0xFFF5F5F5),
        error = Color(0xFFB00020),
        addButton = Color(0xFF6200EE),
        editButton = Color(0xFF03DAC6),
        deleteButton = Color(0xFFB00020),
        settingsButton = Color(0xFFF5F5F5)
    ),
    ThemeType.PASTEL to ThemeColors(
        primary = Color(0xFFB5EAD7),
        secondary = Color(0xFFC7CEEA),
        textPrimary = Color(0xFF2C3E50),
        textSecondary = Color(0xFF7F8C8D),
        background = Color(0xFFF8F9FA),
        surface = Color(0xFFFFFFFF),
        surfaceVariant = Color(0xFFF1F3F5),
        error = Color(0xFFFFB3B3),
        addButton = Color(0xFFB5EAD7),
        editButton = Color(0xFFC7CEEA),
        deleteButton = Color(0xFFFFB3B3),
        settingsButton = Color(0xFFF1F3F5)
    ),
    ThemeType.DARK to ThemeColors(
        primary = Color(0xFFBB86FC),
        secondary = Color(0xFF03DAC6),
        textPrimary = Color(0xFFFFFFFF),
        textSecondary = Color(0xFFB3B3B3),
        background = Color(0xFF121212),
        surface = Color(0xFF1E1E1E),
        surfaceVariant = Color(0xFF2D2D2D),
        error = Color(0xFFCF6679),
        addButton = Color(0xFFBB86FC),
        editButton = Color(0xFF00BFFF),
        deleteButton = Color(0xFFFF1744),
        settingsButton = Color(0xFF2D2D2D)
    ),
    ThemeType.SPRING to ThemeColors(
        primary = Color(0xFFA5D6A7),
        secondary = Color(0xFFC8E6C9),
        textPrimary = Color(0xFF1B5E20),
        textSecondary = Color(0xFF2E7D32),
        background = Color(0xFFE8F5E9),
        surface = Color(0xFFFFFFFF),
        surfaceVariant = Color(0xFFF1F3F5),
        error = Color(0xFFFFCDD2),
        addButton = Color(0xFFA5D6A7),
        editButton = Color(0xFFC8E6C9),
        deleteButton = Color(0xFFFFCDD2),
        settingsButton = Color(0xFFE8F5E9)
    ),
    ThemeType.SUMMER to ThemeColors(
        primary = Color(0xFFFFE082),
        secondary = Color(0xFFFFF59D),
        textPrimary = Color(0xFFF57F17),
        textSecondary = Color(0xFFFB8C00),
        background = Color(0xFFFFFDE7),
        surface = Color(0xFFFFFFFF),
        surfaceVariant = Color(0xFFF1F3F5),
        error = Color(0xFFFFCDD2),
        addButton = Color(0xFFFFE082),
        editButton = Color(0xFFFFF59D),
        deleteButton = Color(0xFFFFCDD2),
        settingsButton = Color(0xFFFFFDE7)
    ),
    ThemeType.AUTUMN to ThemeColors(
        primary = Color(0xFFFFB74D),
        secondary = Color(0xFFFFCC80),
        textPrimary = Color(0xFFE65100),
        textSecondary = Color(0xFFEF6C00),
        background = Color(0xFFFFF3E0),
        surface = Color(0xFFFFFFFF),
        surfaceVariant = Color(0xFFF1F3F5),
        error = Color(0xFFFFCDD2),
        addButton = Color(0xFFFFB74D),
        editButton = Color(0xFFFFCC80),
        deleteButton = Color(0xFFFFCDD2),
        settingsButton = Color(0xFFFFF3E0)
    ),
    ThemeType.WINTER to ThemeColors(
        primary = Color(0xFFB3E5FC),
        secondary = Color(0xFFE1F5FE),
        textPrimary = Color(0xFF01579B),
        textSecondary = Color(0xFF0277BD),
        background = Color(0xFFE1F5FE),
        surface = Color(0xFFFFFFFF),
        surfaceVariant = Color(0xFFF1F3F5),
        error = Color(0xFFFFCDD2),
        addButton = Color(0xFFB3E5FC),
        editButton = Color(0xFFE1F5FE),
        deleteButton = Color(0xFFFFCDD2),
        settingsButton = Color(0xFFE1F5FE)
    )
) 