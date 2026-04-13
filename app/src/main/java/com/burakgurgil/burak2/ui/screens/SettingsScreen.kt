package com.burakgurgil.burak2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.burakgurgil.burak2.ui.theme.ThemeType
import com.burakgurgil.burak2.ui.theme.themeColors
import com.burakgurgil.burak2.ui.theme.ThemeColors
import com.burakgurgil.burak2.ui.icons.CustomIcons

enum class SettingsPage {
    MAIN, THEMES
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    currentTheme: ThemeType,
    onThemeChange: (ThemeType) -> Unit,
    isAutoDeleteEnabled: Boolean,
    onAutoDeleteChange: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    val currentColors = themeColors[currentTheme] ?: themeColors[ThemeType.DEFAULT]!!
    var currentPage by remember { mutableStateOf(SettingsPage.MAIN) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        if (currentPage == SettingsPage.MAIN) "Ayarlar" else "Temalar",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = currentColors.textPrimary
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (currentPage == SettingsPage.MAIN) {
                                onDismiss()
                            } else {
                                currentPage = SettingsPage.MAIN
                            }
                        },
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Geri",
                            tint = currentColors.textPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = currentColors.textPrimary
                )
            )
        }
    ) { paddingValues ->
        if (currentPage == SettingsPage.MAIN) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text(
                    "Görünüm",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = currentColors.textPrimary
                )
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { currentPage = SettingsPage.THEMES },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = currentColors.background
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(currentColors.primary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = CustomIcons.Settings,
                                contentDescription = "Temalar",
                                tint = currentColors.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Temalar",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = currentColors.textPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Uygulama renklerini özelleştirin",
                                style = MaterialTheme.typography.bodyMedium,
                                color = currentColors.textSecondary.copy(alpha = 0.8f)
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "İleri",
                            tint = currentColors.textSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    "Veri Yönetimi",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = currentColors.textPrimary
                )
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = currentColors.background
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.error.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Otomatik Temizleme",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Otomatik Temizle",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = currentColors.textPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "30 güden eski çöpleri siler",
                                style = MaterialTheme.typography.bodyMedium,
                                color = currentColors.textSecondary.copy(alpha = 0.8f)
                            )
                        }
                        Switch(
                            checked = isAutoDeleteEnabled,
                            onCheckedChange = onAutoDeleteChange,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                                checkedTrackColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text(
                    "Tema Seçimi",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = currentColors.textPrimary
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                ThemeOption(
                    title = "Açık",
                    description = "Modern ve ferah bir görünüm",
                    icon = CustomIcons.WhiteTheme,
                    isSelected = currentTheme == ThemeType.DEFAULT,
                    onClick = { onThemeChange(ThemeType.DEFAULT) },
                    colors = currentColors,
                    themeColors = themeColors[ThemeType.DEFAULT]!!
                )
                
                ThemeOption(
                    title = "Koyu",
                    description = "Göz yorgunluğunu azaltan koyu mod",
                    icon = CustomIcons.BlackTheme,
                    isSelected = currentTheme == ThemeType.DARK,
                    onClick = { onThemeChange(ThemeType.DARK) },
                    colors = currentColors,
                    themeColors = themeColors[ThemeType.DARK]!!
                )
                
                ThemeOption(
                    title = "İlkbahar",
                    description = "Taze ve canlı yeşil tonları",
                    icon = CustomIcons.SpringTheme,
                    isSelected = currentTheme == ThemeType.SPRING,
                    onClick = { onThemeChange(ThemeType.SPRING) },
                    colors = currentColors,
                    themeColors = themeColors[ThemeType.SPRING]!!
                )
                
                ThemeOption(
                    title = "Yaz",
                    description = "Parlak ve enerjik sarı tonları",
                    icon = CustomIcons.SummerTheme,
                    isSelected = currentTheme == ThemeType.SUMMER,
                    onClick = { onThemeChange(ThemeType.SUMMER) },
                    colors = currentColors,
                    themeColors = themeColors[ThemeType.SUMMER]!!
                )
                
                ThemeOption(
                    title = "Sonbahar",
                    description = "Sıcak ve huzur veren turuncu tonları",
                    icon = CustomIcons.AutumnTheme,
                    isSelected = currentTheme == ThemeType.AUTUMN,
                    onClick = { onThemeChange(ThemeType.AUTUMN) },
                    colors = currentColors,
                    themeColors = themeColors[ThemeType.AUTUMN]!!
                )
                
                ThemeOption(
                    title = "Kış",
                    description = "Sakin ve ferah mavi tonları",
                    icon = CustomIcons.WinterTheme,
                    isSelected = currentTheme == ThemeType.WINTER,
                    onClick = { onThemeChange(ThemeType.WINTER) },
                    colors = currentColors,
                    themeColors = themeColors[ThemeType.WINTER]!!
                )
            }
        }
    }
}

@Composable
fun ThemeOption(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    colors: ThemeColors,
    themeColors: ThemeColors
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = themeColors.background
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 1.dp
        ),
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(
                width = 2.dp,
                color = themeColors.primary.copy(alpha = 0.6f)
            )
        } else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        themeColors.primary.copy(alpha = if (isSelected) 0.25f else 0.15f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = themeColors.primary,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(18.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = themeColors.textPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = themeColors.textSecondary.copy(alpha = 0.8f)
                )
            }
            if (isSelected) {
                Spacer(modifier = Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(themeColors.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Seçili",
                        tint = themeColors.background,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
} 