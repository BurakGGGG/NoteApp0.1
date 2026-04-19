package com.burakgurgil.burak2

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.lifecycle.ViewModelProvider
import com.burakgurgil.burak2.data.Note
import com.burakgurgil.burak2.ui.screens.SettingsScreen
import com.burakgurgil.burak2.ui.screens.ViewNoteScreen
import com.burakgurgil.burak2.ui.theme.Burak2Theme
import com.burakgurgil.burak2.ui.theme.ThemeType
import com.burakgurgil.burak2.ui.theme.themeColors
import com.burakgurgil.burak2.ui.icons.CustomIcons
import com.burakgurgil.burak2.viewmodel.NoteViewModel
import com.burakgurgil.burak2.viewmodel.NoteViewModelFactory
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.layout.imePadding
import androidx.activity.compose.BackHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction

import kotlinx.coroutines.delay

import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

import android.os.Build
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle

import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import androidx.core.text.HtmlCompat
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import com.burakgurgil.burak2.receiver.ReminderReceiver
import com.google.gson.Gson
import java.io.OutputStreamWriter
import kotlin.math.abs

class MainActivity : FragmentActivity() {
    private lateinit var viewModel: NoteViewModel

    private val exportNotesLauncher = registerForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { uri ->
        if (uri != null) {
            try {
                val notesToExport = viewModel.notes.value
                val json = Gson().toJson(notesToExport)
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    OutputStreamWriter(outputStream).use { writer ->
                        writer.write(json)
                    }
                }
                Toast.makeText(this, "Notlar başarıyla dışa aktarıldı", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "Dışa aktarma başarısız: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, NoteViewModelFactory(applicationContext))[NoteViewModel::class.java]
        
        val sharedPreferences = getSharedPreferences("THEME_PREF", MODE_PRIVATE)
        
        // Oto çöp temizliği
        val isAutoDeleteEnabled = sharedPreferences.getBoolean("auto_delete", true)
        if (isAutoDeleteEnabled) {
            // 30 gün = 30L * 24L * 60L * 60L * 1000L = 2592000000L
            val thirtyDaysAgo = System.currentTimeMillis() - 2592000000L
            viewModel.deleteOldNotesFromTrash(thirtyDaysAgo)
        }

        setContent {
            var currentTheme by remember {
                val savedTheme = sharedPreferences.getString("theme", ThemeType.DEFAULT.name) ?: ThemeType.DEFAULT.name
                val theme = try {
                    ThemeType.valueOf(savedTheme)
                } catch (e: Exception) {
                    // Migration logic
                    when (savedTheme) {
                        "PASTEL" -> ThemeType.PAPER
                        "SPRING" -> ThemeType.FOREST
                        "SUMMER", "AUTUMN" -> ThemeType.SUNSET
                        "WINTER" -> ThemeType.MIDNIGHT
                        else -> ThemeType.DEFAULT
                    }
                }
                mutableStateOf(theme)
            }
            var autoDeleteEnabled by remember { mutableStateOf(isAutoDeleteEnabled) }
            var useDynamicColor by remember { 
                mutableStateOf(sharedPreferences.getBoolean("dynamic_color", false))
            }
            var showSettings by remember { mutableStateOf(false) }

            Burak2Theme(
                themeType = currentTheme,
                useDynamicColor = useDynamicColor
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (showSettings) {
                        BackHandler {
                            showSettings = false
                        }
                        
                        SettingsScreen(
                            currentTheme = currentTheme,
                            onThemeChange = {
                                currentTheme = it
                                sharedPreferences.edit().putString("theme", it.name).apply()
                            },
                            isAutoDeleteEnabled = autoDeleteEnabled,
                            onAutoDeleteChange = {
                                autoDeleteEnabled = it
                                sharedPreferences.edit().putBoolean("auto_delete", it).apply()
                            },
                            useDynamicColor = useDynamicColor,
                            onDynamicColorChange = {
                                useDynamicColor = it
                                sharedPreferences.edit().putBoolean("dynamic_color", it).apply()
                            },
                            onExportNotes = {
                                exportNotesLauncher.launch("notlar_yedek.json")
                            },
                            onDismiss = { showSettings = false }
                        )
                    } else {
                        NoteApp(
                            viewModel = viewModel,
                            onSettingsClick = { showSettings = true },
                            currentTheme = currentTheme
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NoteApp(
    viewModel: NoteViewModel,
    onSettingsClick: () -> Unit,
    currentTheme: ThemeType
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedNoteForEdit by remember { mutableStateOf<Note?>(null) }
    var selectedNoteForView by remember { mutableStateOf<Note?>(null) }
    var showTrash by remember { mutableStateOf(false) }
    var showDeleteAllConfirmation by remember { mutableStateOf(false) }
    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedTagFilter by remember { mutableStateOf<String?>(null) }
    
    val context = LocalContext.current
    val sharedPrefs = context.getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE)
    var customTags by remember { 
        mutableStateOf(sharedPrefs.getStringSet("tags", setOf("Kişisel", "İş", "Önemli", "Fikir"))?.toList() ?: listOf("Kişisel", "İş", "Önemli", "Fikir"))
    }
    var isGridView by remember { mutableStateOf(sharedPrefs.getBoolean("is_grid_view", false)) }
    var showAddTagDialog by remember { mutableStateOf(false) }
    var newTagText by remember { mutableStateOf("") }
    val notes by viewModel.notes.collectAsState()
    val deletedNotes by viewModel.deletedNotes.collectAsState()
    val archivedNotes by viewModel.archivedNotes.collectAsState()
    val haptic = LocalHapticFeedback.current
    val currentColors = themeColors[currentTheme] ?: themeColors[ThemeType.DEFAULT]!!
    val density = LocalDensity.current
    val fragmentActivity = context as FragmentActivity
    
    // Kilitli not için biyometrik doğrulama
    val authenticateAndPerform: (Note, String) -> Unit = { noteToAuth, action ->
        if (noteToAuth.isLocked) {
            val executor = ContextCompat.getMainExecutor(context)
            val biometricPrompt = BiometricPrompt(
                fragmentActivity,
                executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        when (action) {
                            "view" -> selectedNoteForView = noteToAuth
                            "edit" -> selectedNoteForEdit = noteToAuth
                            "unlock" -> viewModel.toggleLocked(noteToAuth.id, false)
                        }
                    }
                }
            )
            
            val promptInfoBuilder = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Kilitli Not")
                .setSubtitle("Notu görüntülemek için doğrulayın")
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                promptInfoBuilder.setAllowedAuthenticators(
                    BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
                )
            } else {
                @Suppress("DEPRECATION")
                promptInfoBuilder.setDeviceCredentialAllowed(true)
            }
            
            biometricPrompt.authenticate(promptInfoBuilder.build())
        } else {
            when (action) {
                "view" -> selectedNoteForView = noteToAuth
                "edit" -> selectedNoteForEdit = noteToAuth
            }
        }
    }
    
    // Drag and drop için state
    var draggedNote by remember { mutableStateOf<Note?>(null) }
    var notesOrder by remember { mutableStateOf(notes) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var recentlyStarredNoteId by remember { mutableStateOf<Long?>(null) }
    
    // Notlar değiştiğinde veya filtreler değiştiğinde sıralamayı güncelle
    LaunchedEffect(notes, archivedNotes, searchQuery, selectedTagFilter) {
        if (draggedNote == null) {
            val baseList = if (selectedTagFilter == "ARCHIVED") archivedNotes else notes
            val filtered = baseList.filter { note ->
                val matchesSearch = if (searchQuery.isBlank()) true else {
                    note.title.contains(searchQuery, ignoreCase = true) || 
                    note.content.contains(searchQuery, ignoreCase = true)
                }
                val matchesTag = if (selectedTagFilter == null || selectedTagFilter == "ARCHIVED") true else {
                    note.tag == selectedTagFilter
                }
                matchesSearch && matchesTag
            }
            notesOrder = filtered
        }
    }
    
    // Yıldızlı not sayısını kontrol et
    val starredCount = notes.count { it.isStarred }
    
    // Yıldızlanan not animasyonu bittikten sonra flag'i sıfırla
    LaunchedEffect(recentlyStarredNoteId) {
        if (recentlyStarredNoteId != null) {
            delay(1500) // Animasyon süresi + biraz ekstra
            recentlyStarredNoteId = null
        }
    }

    // Geri tuşu yönetimi
    BackHandler(enabled = isSearchActive || showTrash || showAddDialog || selectedNoteForEdit != null || selectedNoteForView != null) {
        when {
            isSearchActive -> {
                isSearchActive = false
                searchQuery = ""
            }
            showTrash -> showTrash = false
            showAddDialog -> showAddDialog = false
            selectedNoteForEdit != null -> selectedNoteForEdit = null
            selectedNoteForView != null -> selectedNoteForView = null
        }
    }

    // Not görüntüleme ekranı - tamamen ayrı bir ekran olarak göster (topBar ile çakışmasın)
    if (selectedNoteForView != null) {
        val note = selectedNoteForView!!
        ViewNoteScreen(
            note = note,
            onDismiss = { selectedNoteForView = null },
            onEdit = {
                selectedNoteForEdit = note
                selectedNoteForView = null
            }
        )
        return
    }

    // Not düzenleme ekranı - tamamen ayrı bir ekran olarak göster
    if (selectedNoteForEdit != null) {
        val note = selectedNoteForEdit!!
        NoteEditScreen(
            note = note,
            customTags = customTags,
            onDismiss = { selectedNoteForEdit = null },
            onSave = { title, content, tag, rTime ->
                val updatedNote = note.copy(
                    title = title,
                    content = content,
                    tag = tag,
                    reminderTime = rTime
                )
                viewModel.update(updatedNote)
                if (rTime != null) {
                    ReminderReceiver.scheduleReminder(context, note.id, title, "Hatırlatıcı", rTime)
                } else {
                    ReminderReceiver.cancelReminder(context, note.id)
                }
                selectedNoteForEdit = null
            }
        )
        return
    }

    // Yeni not ekleme ekranı - tamamen ayrı bir ekran olarak göster
    if (showAddDialog) {
        NoteEditScreen(
            note = null,
            customTags = customTags,
            onDismiss = { showAddDialog = false },
            onSave = { title, content, tag, rTime ->
                val newNote = Note(
                    title = title,
                    content = content,
                    tag = tag,
                    reminderTime = rTime,
                    createdAt = Date()
                )
                viewModel.insert(newNote) { newId ->
                    if (rTime != null) {
                        ReminderReceiver.scheduleReminder(context, newId, title, "Hatırlatıcı", rTime)
                    }
                }
                showAddDialog = false
            }
        )
        return
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent
                    ),
                    title = { 
                        if (isSearchActive) {
                            TextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                placeholder = { 
                                    Text(
                                        "Notlarda ara...",
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                    )
                                },
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Text(
                                "notepad",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    },
                    actions = {
                        if (!showTrash) {
                            if (!isSearchActive) {
                                Card(
                                    modifier = Modifier
                                        .padding(end = 8.dp)
                                        .size(48.dp),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = currentColors.settingsButton
                                    ),
                                    elevation = CardDefaults.cardElevation(
                                        defaultElevation = 2.dp,
                                        pressedElevation = 1.dp
                                    ),
                                    onClick = { 
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        isSearchActive = true 
                                    }
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.Search,
                                            contentDescription = "Ara",
                                            tint = currentColors.textPrimary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            } else {
                                IconButton(onClick = { 
                                    isSearchActive = false
                                    searchQuery = "" 
                                }) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Aramayı Kapat",
                                        tint = currentColors.textPrimary
                                    )
                                }
                            }

                            if (!isSearchActive) {
                                Card(
                                    modifier = Modifier
                                        .padding(end = 8.dp)
                                        .size(48.dp),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = currentColors.settingsButton
                                    ),
                                    elevation = CardDefaults.cardElevation(
                                        defaultElevation = 2.dp,
                                        pressedElevation = 1.dp
                                    ),
                                    onClick = { 
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        showTrash = true 
                                    }
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            CustomIcons.TrashBlank,
                                            contentDescription = "Çöp Kutusu",
                                            tint = currentColors.deleteButton,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                                Card(
                                    modifier = Modifier
                                        .padding(end = 8.dp)
                                        .size(48.dp),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = currentColors.settingsButton
                                    ),
                                    elevation = CardDefaults.cardElevation(
                                        defaultElevation = 2.dp,
                                        pressedElevation = 1.dp
                                    ),
                                    onClick = { 
                                        isGridView = !isGridView 
                                        sharedPrefs.edit().putBoolean("is_grid_view", isGridView).apply()
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    }
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            if (isGridView) Icons.Default.List else Icons.Default.Menu,
                                            contentDescription = "Görünümü Değiştir",
                                            tint = currentColors.textPrimary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                                Card(
                                    modifier = Modifier
                                        .padding(end = 8.dp)
                                        .size(48.dp),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = currentColors.settingsButton
                                    ),
                                    elevation = CardDefaults.cardElevation(
                                        defaultElevation = 2.dp,
                                        pressedElevation = 1.dp
                                    ),
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        onSettingsClick()
                                    }
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            CustomIcons.Settings,
                                            contentDescription = "Ayarlar",
                                            tint = currentColors.textPrimary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                )

                // Etiket filtreleri
                if (!showTrash) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            FilterChip(
                                selected = selectedTagFilter == null,
                                onClick = { selectedTagFilter = null },
                                label = { Text("Tümü") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                        }
                        item {
                            FilterChip(
                                selected = selectedTagFilter == "ARCHIVED",
                                onClick = { selectedTagFilter = "ARCHIVED" },
                                label = { Text("Arşiv") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            )
                        }
                        item {
                            FilterChip(
                                selected = false,
                                onClick = { showAddTagDialog = true },
                                label = { Text("+ Yeni") },
                                colors = FilterChipDefaults.filterChipColors()
                            )
                        }
                        items(customTags) { tag ->
                            val isDefault = setOf("Kişisel", "İş", "Önemli", "Fikir").contains(tag)
                            FilterChip(
                                selected = selectedTagFilter == tag,
                                onClick = { selectedTagFilter = tag },
                                label = { Text(tag) },
                                trailingIcon = if (!isDefault) {
                                    {
                                        Icon(
                                            Icons.Default.Close,
                                            contentDescription = "Sil",
                                            modifier = Modifier.size(16.dp).clickable {
                                                val updatedTags = customTags - tag
                                                customTags = updatedTags
                                                sharedPrefs.edit().putStringSet("tags", updatedTags.toSet()).apply()
                                                if (selectedTagFilter == tag) selectedTagFilter = null
                                            }
                                        )
                                    }
                                } else null,
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                        }
                    }
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            if (!showTrash) {
                FloatingActionButton(
                    onClick = { 
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        showAddDialog = true 
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .size(64.dp),
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 4.dp,
                        hoveredElevation = 10.dp
                    )
                ) {
                    Icon(
                        CustomIcons.NewNote,
                        contentDescription = "Not Ekle",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    ) { paddingValues ->
        if (showTrash) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        modifier = Modifier.size(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = currentColors.settingsButton
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp,
                            pressedElevation = 1.dp
                        ),
                        onClick = { showTrash = false }
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Geri",
                                tint = currentColors.textPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        "Çöp Kutusu",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = currentColors.textPrimary
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    if (deletedNotes.isNotEmpty()) {
                        Button(
                            onClick = { showDeleteAllConfirmation = true },
                            modifier = Modifier
                                .height(44.dp)
                                .padding(horizontal = 4.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = currentColors.deleteButton,
                                contentColor = currentColors.textPrimary
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 3.dp,
                                pressedElevation = 2.dp
                            )
                        ) {
                            Icon(
                                CustomIcons.TrashXmark,
                                contentDescription = "Hepsini Sil",
                                tint = currentColors.textPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Hepsini Sil",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = currentColors.textPrimary
                            )
                        }
                    }
                }

                if (deletedNotes.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Çöp Kutusu Boş",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = currentColors.textPrimary.copy(alpha = 0.7f)
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(deletedNotes) { note ->
                            DeletedNoteItem(
                                note = note,
                                onRestore = { viewModel.restoreFromTrash(note.id) },
                                onDelete = { viewModel.delete(Note(id = note.id, title = "", content = "", createdAt = Date())) },
                                currentTheme = currentTheme
                            )
                        }
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                if (notes.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(32.dp)
                        ) {
                            Icon(
                                CustomIcons.NewNote,
                                contentDescription = null,
                                modifier = Modifier.size(80.dp).alpha(0.2f),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                "Henüz not eklenmemiş",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Yeni not eklemek için sağ üstteki + butonuna tıklayın",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    if (isGridView) {
                        LazyVerticalStaggeredGrid(
                            columns = StaggeredGridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalItemSpacing = 12.dp
                        ) {
                            items(
                                items = notesOrder,
                                key = { it.id }
                            ) { note ->
                                val dismissState = rememberSwipeToDismissBoxState(
                                    confirmValueChange = { value ->
                                        when (value) {
                                            SwipeToDismissBoxValue.EndToStart -> {
                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                viewModel.moveToTrash(note.id)
                                                true
                                            }
                                            SwipeToDismissBoxValue.StartToEnd -> {
                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                viewModel.toggleArchived(note.id, true)
                                                true
                                            }
                                            else -> false
                                        }
                                    }
                                )

                                SwipeToDismissBox(
                                    state = dismissState,
                                    backgroundContent = { SwipeBackground(dismissState) },
                                    modifier = Modifier.animateItemPlacement()
                                ) {
                                    val isDragging = draggedNote?.id == note.id
                                var dragOffset by remember { mutableStateOf(0f) }
                                
                                NoteItem(
                                    note = note,
                                    onView = { 
                                        if (draggedNote == null) {
                                            authenticateAndPerform(note, "view")
                                        }
                                    },
                                    onEdit = { 
                                        if (draggedNote == null) {
                                            authenticateAndPerform(note, "edit")
                                        }
                                    },
                                    onDelete = { viewModel.moveToTrash(note.id) },
                                    onStarred = { 
                                        if (!note.isStarred && starredCount >= 3) {
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Maksimum 3 yıldızlı not olabilir", duration = SnackbarDuration.Short)
                                            }
                                        } else {
                                            recentlyStarredNoteId = note.id
                                            viewModel.toggleStarred(note.id, !note.isStarred)
                                        }
                                    },
                                    onLockToggle = {
                                        if (note.isLocked) {
                                            authenticateAndPerform(note, "unlock")
                                        } else {
                                            viewModel.toggleLocked(note.id, true)
                                        }
                                    },
                                    currentTheme = currentTheme,
                                    isRecentlyStarred = recentlyStarredNoteId == note.id,
                                    modifier = Modifier
                                    )
                                }
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = notesOrder,
                                key = { it.id }
                            ) { note ->
                                val dismissState = rememberSwipeToDismissBoxState(
                                    confirmValueChange = { value ->
                                        when (value) {
                                            SwipeToDismissBoxValue.EndToStart -> {
                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                viewModel.moveToTrash(note.id)
                                                true
                                            }
                                            SwipeToDismissBoxValue.StartToEnd -> {
                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                viewModel.toggleArchived(note.id, !note.isArchived)
                                                true
                                            }
                                            else -> false
                                        }
                                    }
                                )

                                SwipeToDismissBox(
                                    state = dismissState,
                                    backgroundContent = { SwipeBackground(dismissState) },
                                    modifier = Modifier.animateItemPlacement()
                                ) {
                                    val isDragging = draggedNote?.id == note.id
                                    var dragOffset by remember { mutableStateOf(0f) }
                                    
                                    NoteItem(
                                        note = note,
                                        onView = { 
                                            if (draggedNote == null) {
                                                authenticateAndPerform(note, "view")
                                            }
                                        },
                                        onEdit = { 
                                            if (draggedNote == null) {
                                                authenticateAndPerform(note, "edit")
                                            }
                                        },
                                        onDelete = { viewModel.moveToTrash(note.id) },
                                        onStarred = { 
                                            if (!note.isStarred && starredCount >= 3) {
                                                coroutineScope.launch {
                                                    snackbarHostState.showSnackbar(
                                                        message = "Maksimum 3 yıldızlı not olabilir",
                                                        duration = SnackbarDuration.Short
                                                    )
                                                }
                                            } else {
                                                recentlyStarredNoteId = note.id
                                                viewModel.toggleStarred(note.id, !note.isStarred)
                                            }
                                        },
                                        onLockToggle = {
                                            if (note.isLocked) {
                                                authenticateAndPerform(note, "unlock")
                                            } else {
                                                viewModel.toggleLocked(note.id, true)
                                            }
                                        },
                                        currentTheme = currentTheme,
                                        isRecentlyStarred = recentlyStarredNoteId == note.id,
                                        modifier = Modifier
                                            .graphicsLayer {
                                                translationY = if (isDragging) dragOffset else 0f
                                                alpha = if (isDragging) 0.85f else 1f
                                                scaleX = if (isDragging) 1.03f else 1f
                                                scaleY = if (isDragging) 1.03f else 1f
                                                shadowElevation = if (isDragging) 8f else 0f
                                            }
                                            .pointerInput(note.id) {
                                                detectDragGesturesAfterLongPress(
                                                    onDragStart = {
                                                        draggedNote = note
                                                        dragOffset = 0f
                                                    },
                                            onDrag = { change, dragAmount ->
                                                change.consume()
                                                dragOffset += dragAmount.y
                                                
                                                // Sürüklenen notun pozisyonunu güncelle
                                                val currentIndex = notesOrder.indexOf(note)
                                                val threshold = with(density) { 60.dp.toPx() } // Minimum hareket mesafesi
                                                
                                                if (abs(dragOffset) > threshold) {
                                                    val newIndex = if (dragOffset > 0) {
                                                        // Aşağı sürükleniyor
                                                        (currentIndex + 1).coerceAtMost(notesOrder.size - 1)
                                                    } else {
                                                        // Yukarı sürükleniyor
                                                        (currentIndex - 1).coerceAtLeast(0)
                                                    }
                                                    
                                                    if (newIndex != currentIndex) {
                                                        val newOrder = notesOrder.toMutableList()
                                                        newOrder.removeAt(currentIndex)
                                                        newOrder.add(newIndex, note)
                                                        notesOrder = newOrder
                                                        dragOffset = 0f // Pozisyon değişti, offset'i sıfırla
                                                    }
                                                }
                                            },
                                            onDragEnd = {
                                                draggedNote = null
                                                dragOffset = 0f
                                            },
                                            onDragCancel = {
                                                draggedNote = null
                                                dragOffset = 0f
                                            }
                                        )
                                    }
                            )
                        }
                    }
                    }
                }
            }
        }

        if (showAddTagDialog) {
            AlertDialog(
                onDismissRequest = { showAddTagDialog = false },
                title = { Text("Yeni Etiket Ekle") },
                text = {
                    TextField(
                        value = newTagText,
                        onValueChange = { newTagText = it },
                        singleLine = true,
                        placeholder = { Text("Etiket Adı") }
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (newTagText.isNotBlank() && !customTags.contains(newTagText.trim())) {
                            val updatedTags = customTags + newTagText.trim()
                            customTags = updatedTags
                            sharedPrefs.edit().putStringSet("tags", updatedTags.toSet()).apply()
                        }
                        newTagText = ""
                        showAddTagDialog = false
                    }) {
                        Text("Ekle")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { 
                        newTagText = ""
                        showAddTagDialog = false 
                    }) {
                        Text("İptal")
                    }
                }
            )
        }

        if (showDeleteAllConfirmation) {
            AlertDialog(
                onDismissRequest = { showDeleteAllConfirmation = false },
                title = {
                    Text(
                        "Tüm Notları Sil",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                text = {
                    Text(
                        "Çöp kutusundaki tüm notlar kalıcı olarak silinecek. Bu işlem geri alınamaz.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            deletedNotes.forEach { note ->
                                viewModel.delete(Note(id = note.id, title = "", content = "", createdAt = Date()))
                            }
                            showDeleteAllConfirmation = false
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = currentColors.deleteButton,
                            contentColor = currentColors.textPrimary
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 3.dp,
                            pressedElevation = 2.dp
                        )
                    ) {
                        Text(
                            "Sil",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = { showDeleteAllConfirmation = false },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            width = 2.dp
                        )
                    ) {
                        Text(
                            "İptal",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            )
        }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SwipeBackground(dismissState: SwipeToDismissBoxState) {
    val direction = dismissState.dismissDirection ?: return
    val color = when (direction) {
        SwipeToDismissBoxValue.StartToEnd -> Color(0xFF4CAF50) // Yeşil (Arşiv)
        SwipeToDismissBoxValue.EndToStart -> Color(0xFFF44336) // Kırmızı (Sil)
        else -> Color.Transparent
    }
    
    val alignment = when (direction) {
        SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
        SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
        else -> Alignment.Center
    }
    
    val icon = when (direction) {
        SwipeToDismissBoxValue.StartToEnd -> Icons.Default.Done
        SwipeToDismissBoxValue.EndToStart -> Icons.Default.Delete
        else -> Icons.Default.Delete
    }
    
    val scale by animateFloatAsState(
        if (dismissState.targetValue == SwipeToDismissBoxValue.Settled) 0.75f else 1f
    )

    Box(
        Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(24.dp))
            .background(color)
            .padding(horizontal = 20.dp),
        contentAlignment = alignment
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.graphicsLayer(scaleX = scale, scaleY = scale),
            tint = Color.White
        )
    }
}

@Composable
fun NoteItem(
    note: Note,
    onView: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onStarred: () -> Unit,
    onLockToggle: () -> Unit,
    currentTheme: ThemeType,
    isRecentlyStarred: Boolean = false,
    modifier: Modifier
) {
    val haptic = LocalHapticFeedback.current
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    val currentColors = themeColors[currentTheme] ?: themeColors[ThemeType.DEFAULT]!!
    
    // Yıldızlanan not için scale pulse animasyonu
    val starScale by animateFloatAsState(
        targetValue = if (isRecentlyStarred && note.isStarred) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "starAnimation"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = starScale
                scaleY = starScale
            }
            .animateContentSize(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isRecentlyStarred && note.isStarred) 12.dp else 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = currentColors.surface
        ),
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            onView()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Card(
                        modifier = Modifier.size(32.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 0.dp
                        ),
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onStarred()
                        }
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (note.isStarred) CustomIcons.StarFilled else CustomIcons.Star,
                                contentDescription = if (note.isStarred) "Yıldızı Kaldır" else "Yıldızla",
                                tint = if (note.isStarred) Color(0xFFFFD700) else currentColors.textPrimary.copy(alpha = 0.6f),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Text(
                        text = note.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (note.isLocked) {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = "Kilitli",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    if (note.reminderTime != null) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Hatırlatıcı Var",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                
                // 3 nokta menüsü
                Box {
                    Card(
                        modifier = Modifier.size(36.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = currentColors.settingsButton
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp,
                            pressedElevation = 1.dp
                        ),
                        onClick = { 
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            showMenu = true 
                        }
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = "Menü",
                                tint = currentColors.textPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { 
                                Text(
                                    "Düzenle",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            },
                            onClick = { 
                                showMenu = false
                                onEdit()
                            },
                            leadingIcon = {
                                Icon(
                                    CustomIcons.Edit,
                                    contentDescription = null,
                                    tint = currentColors.editButton,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        )
                        DropdownMenuItem(
                            text = { 
                                Text(
                                    "Sil",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            },
                            onClick = { 
                                showMenu = false
                                showDeleteConfirmation = true
                            },
                            leadingIcon = {
                                Icon(
                                    CustomIcons.TrashPlus,
                                    contentDescription = null,
                                    tint = currentColors.deleteButton,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        )
                        DropdownMenuItem(
                            text = { 
                                Text(
                                    if (note.isLocked) "Kilidi Aç" else "Kilitle",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            },
                            onClick = { 
                                showMenu = false
                                onLockToggle()
                            },
                            leadingIcon = {
                                Icon(
                                    if (note.isLocked) Icons.Default.Lock else Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            
            // İçerik - kilitli notlarda gizle
            if (note.isLocked) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Bu not kilitli",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontStyle = FontStyle.Italic
                        ),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            } else {
                Text(
                    text = HtmlCompat.fromHtml(note.content, HtmlCompat.FROM_HTML_MODE_COMPACT).toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                        .format(note.createdAt),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )

                if (note.tag != null) {
                    Box(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = note.tag,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = {
                Text(
                    "Notu Sil",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            text = {
                Text(
                    "Bu notu silmek istediğinizden emin misiniz? Not çöp kutusuna taşınacak.",
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete()
                        showDeleteConfirmation = false
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = currentColors.deleteButton,
                        contentColor = currentColors.textPrimary
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 3.dp,
                        pressedElevation = 2.dp
                    )
                ) {
                    Text(
                        "Sil",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showDeleteConfirmation = false },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = 2.dp
                    )
                ) {
                    Text(
                        "İptal",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NoteEditScreen(
    note: Note?,
    customTags: List<String>,
    onDismiss: () -> Unit,
    onSave: (String, String, String?, Long?) -> Unit
) {
    var title by remember { mutableStateOf(note?.title ?: "") }
    val richTextState = rememberRichTextState()
    var selectedTag by remember { mutableStateOf(note?.tag) }
    var reminderTime by remember { mutableStateOf(note?.reminderTime) }
    var isFormatBarOpen by remember { mutableStateOf(false) }
    var isTagMenuExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)
            reminderTime = calendar.timeInMillis
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            timePickerDialog.show()
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    LaunchedEffect(note) {
        if (note != null && richTextState.toHtml() != note.content) {
            richTextState.setHtml(note.content)
        }
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val isEditMode = note != null

    // removed predefinedTags

    // Geri tuşu yönetimi
    BackHandler {
        onDismiss()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isEditMode) "Not Düzenle" else "Not Ekle",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Geri",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { datePickerDialog.show() }) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Hatırlatıcı",
                            tint = if (reminderTime != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    TextButton(onClick = { onSave(title, richTextState.toHtml(), selectedTag, reminderTime) }) {
                        Text("Kaydet", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.primary)
                    }
                    Box {
                        IconButton(onClick = { isTagMenuExpanded = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Etiket Seç")
                        }
                        DropdownMenu(
                            expanded = isTagMenuExpanded,
                            onDismissRequest = { isTagMenuExpanded = false },
                            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                        ) {
                            customTags.forEach { tag ->
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(tag)
                                            if (tag == selectedTag) {
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                                            }
                                        }
                                    },
                                    onClick = {
                                        selectedTag = if (selectedTag == tag) null else tag
                                        isTagMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
                    .background(MaterialTheme.colorScheme.background)
                    .imePadding()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (selectedTag != null) {
                        Text(
                            text = "🏷️ $selectedTag",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 0.dp, start = 16.dp, end = 8.dp)
                        )
                    }
                    if (reminderTime != null) {
                        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                        Text(
                            text = "⏰ ${formatter.format(Date(reminderTime!!))}",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 0.dp, start = if (selectedTag == null) 16.dp else 0.dp)
                        )
                    }
                }

                // Başlık alanı - Sınırları tamamen kaldırılmış Defter Görünümü
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Başlık...", style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f), fontWeight = FontWeight.Bold)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false,
                    textStyle = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { keyboardController?.hide() })
                )
                

                
                // İçerik alanı - Tam ekran defter hissi, sıfır kenarlık
                RichTextEditor(
                    state = richTextState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(Color.Transparent),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                )
                
                // Animasyonlu Biçimlendirme Araç Çubuğu (Expandable)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 2.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!isFormatBarOpen) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            onClick = { isFormatBarOpen = true },
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("Aa", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onPrimaryContainer)
                            }
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(22.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.animateContentSize()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                            ) {
                                IconButton(onClick = { richTextState.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold)) }) {
                                    Text("B", fontWeight = FontWeight.Bold, color = if(richTextState.currentSpanStyle.fontWeight == FontWeight.Bold) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                IconButton(onClick = { richTextState.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic)) }) {
                                    Text("I", fontStyle = FontStyle.Italic, color = if(richTextState.currentSpanStyle.fontStyle == FontStyle.Italic) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                IconButton(onClick = { richTextState.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline)) }) {
                                    Text("U", textDecoration = TextDecoration.Underline, color = if(richTextState.currentSpanStyle.textDecoration == TextDecoration.Underline) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                IconButton(onClick = { richTextState.toggleUnorderedList() }) {
                                    Text("•", fontWeight = FontWeight.Bold, color = if(richTextState.isUnorderedList) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Box(modifier = Modifier.width(1.dp).height(24.dp).background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)))
                                IconButton(onClick = { isFormatBarOpen = false }) {
                                    Icon(Icons.Default.Close, contentDescription = "Kapat", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun DeletedNoteItem(
    note: Note,
    onRestore: () -> Unit,
    onDelete: () -> Unit,
    currentTheme: ThemeType
) {
    val currentColors = themeColors[currentTheme] ?: themeColors[ThemeType.DEFAULT]!!
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (currentTheme == ThemeType.DEFAULT) Color(0xFFF5F5F5) else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = currentColors.textPrimary
                )
                Text(
                    text = HtmlCompat.fromHtml(note.content, HtmlCompat.FROM_HTML_MODE_COMPACT).toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = currentColors.textPrimary.copy(alpha = 0.7f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                        .format(note.createdAt),
                    style = MaterialTheme.typography.bodySmall,
                    color = currentColors.textPrimary.copy(alpha = 0.5f)
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.padding(start = 12.dp)
            ) {
                Card(
                    modifier = Modifier.size(36.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = currentColors.editButton
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp,
                        pressedElevation = 1.dp
                    ),
                    onClick = onRestore
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            CustomIcons.TrashUndo,
                            contentDescription = "Geri Yükle",
                            tint = currentColors.textPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Card(
                    modifier = Modifier.size(36.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = currentColors.deleteButton
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp,
                        pressedElevation = 1.dp
                    ),
                    onClick = { showDeleteConfirmation = true }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            CustomIcons.TrashXmark,
                            contentDescription = "Kalıcı Sil",
                            tint = currentColors.textPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = {
                Text(
                    "Notu Kalıcı Olarak Sil",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            text = {
                Text(
                    "Bu not kalıcı olarak silinecek. Bu işlem geri alınamaz.",
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete()
                        showDeleteConfirmation = false
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = currentColors.deleteButton,
                        contentColor = currentColors.textPrimary
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 3.dp,
                        pressedElevation = 2.dp
                    )
                ) {
                    Text(
                        "Sil",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showDeleteConfirmation = false },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = 2.dp
                    )
                ) {
                    Text(
                        "İptal",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        )
    }
}