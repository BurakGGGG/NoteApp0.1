package com.burakgurgil.burak2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction

import androidx.compose.ui.platform.LocalFocusManager
import kotlinx.coroutines.delay

import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: NoteViewModel

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
                mutableStateOf(
                    ThemeType.valueOf(
                        sharedPreferences.getString("theme", ThemeType.DEFAULT.name) ?: ThemeType.DEFAULT.name
                    )
                )
            }
            var autoDeleteEnabled by remember { mutableStateOf(isAutoDeleteEnabled) }
            var showSettings by remember { mutableStateOf(false) }

            Burak2Theme(themeType = currentTheme) {
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
    val predefinedTags = listOf("Kişisel", "İş", "Önemli", "Fikir")
    val notes by viewModel.notes.collectAsState()
    val deletedNotes by viewModel.deletedNotes.collectAsState()
    val currentColors = themeColors[currentTheme] ?: themeColors[ThemeType.DEFAULT]!!
    val density = LocalDensity.current
    
    // Drag and drop için state
    var draggedNote by remember { mutableStateOf<Note?>(null) }
    var notesOrder by remember { mutableStateOf(notes) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var recentlyStarredNoteId by remember { mutableStateOf<Long?>(null) }
    
    // Notlar değiştiğinde veya filtreler değiştiğinde sıralamayı güncelle
    LaunchedEffect(notes, searchQuery, selectedTagFilter) {
        if (draggedNote == null) {
            val filtered = notes.filter { note ->
                val matchesSearch = if (searchQuery.isBlank()) true else {
                    note.title.contains(searchQuery, ignoreCase = true) || 
                    note.content.contains(searchQuery, ignoreCase = true)
                }
                val matchesTag = if (selectedTagFilter == null) true else {
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

    Scaffold(
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TopAppBar(
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
                        if (!showAddDialog && selectedNoteForEdit == null && selectedNoteForView == null && !showTrash) {
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
                                    onClick = { isSearchActive = true }
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
                                    onClick = { showTrash = true }
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
                                    onClick = onSettingsClick
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
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    )
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
                        items(predefinedTags) { tag ->
                            FilterChip(
                                selected = selectedTagFilter == tag,
                                onClick = { selectedTagFilter = tag },
                                label = { Text(tag) },
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
            if (!showAddDialog && selectedNoteForEdit == null && selectedNoteForView == null && !showTrash) {
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .size(64.dp),
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 6.dp,
                        pressedElevation = 4.dp
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
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = notesOrder,
                            key = { it.id }
                        ) { note ->
                            val isDragging = draggedNote?.id == note.id
                            var dragOffset by remember { mutableStateOf(0f) }
                            
                            NoteItem(
                                note = note,
                                onView = { 
                                    if (draggedNote == null) {
                                        selectedNoteForView = note
                                    }
                                },
                                onEdit = { 
                                    if (draggedNote == null) {
                                        selectedNoteForEdit = note
                                    }
                                },
                                onDelete = { viewModel.moveToTrash(note.id) },
                                onStarred = { 
                                    if (!note.isStarred && starredCount >= 3) {
                                        // Maksimum 3 yıldızlı not limiti
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
                                                
                                                if (kotlin.math.abs(dragOffset) > threshold) {
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

        AnimatedVisibility(
            visible = showAddDialog,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            NoteEditScreen(
                note = null,
                onDismiss = { showAddDialog = false },
                onSave = { title, content, tag ->
                    viewModel.insert(
                        Note(
                            title = title,
                            content = content,
                            tag = tag,
                            createdAt = Date()
                        )
                    )
                    showAddDialog = false
                }
            )
        }

        selectedNoteForEdit?.let { note ->
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                NoteEditScreen(
                    note = note,
                    onDismiss = { selectedNoteForEdit = null },
                    onSave = { title, content, tag ->
                        viewModel.update(
                            note.copy(
                                title = title,
                                content = content,
                                tag = tag
                            )
                        )
                        selectedNoteForEdit = null
                    }
                )
            }
        }

        selectedNoteForView?.let { note ->
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut(),
                modifier = Modifier.fillMaxSize()
            ) {
                ViewNoteScreen(
                    note = note,
                    onDismiss = { selectedNoteForView = null },
                    onEdit = {
                        selectedNoteForEdit = note
                        selectedNoteForView = null
                    }
                )
            }
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NoteItem(
    note: Note,
    onView: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onStarred: () -> Unit,
    currentTheme: ThemeType,
    modifier: Modifier = Modifier,
    isRecentlyStarred: Boolean = false
) {
    var showDeleteConfirmation by remember { mutableStateOf(false) }
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
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isRecentlyStarred && note.isStarred) 8.dp else 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (currentTheme == ThemeType.DEFAULT) Color(0xFFF5F5F5) else MaterialTheme.colorScheme.surface
        ),
        onClick = onView
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
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
                        onClick = onStarred
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
                        onClick = onEdit
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                CustomIcons.Edit,
                                contentDescription = "Düzenle",
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
                                CustomIcons.TrashPlus,
                                contentDescription = "Sil",
                                tint = currentColors.textPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                maxLines = 3
            )
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
    onDismiss: () -> Unit,
    onSave: (String, String, String?) -> Unit
) {
    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }
    var selectedTag by remember { mutableStateOf(note?.tag) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val isEditMode = note != null

    val predefinedTags = listOf("Kişisel", "İş", "Önemli", "Fikir")

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
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
                    .imePadding()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                
                // Başlık alanı - üstte sabit kalır
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Başlık") },
                    modifier = Modifier
                        .fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            keyboardController?.hide()
                        }
                    )
                )
                
                // Etiket Seçici
                
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(predefinedTags) { tag ->
                        val isSelected = selectedTag == tag
                        androidx.compose.material3.FilterChip(
                            selected = isSelected,
                            onClick = { 
                                selectedTag = if (isSelected) null else tag
                            },
                            label = { Text(tag) },
                            colors = androidx.compose.material3.FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // İçerik alanı - kalan alanı doldurur, kendi iç scroll'unu kullanır
                // Yazı yazdıkça cursor her zaman görünür kalır
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("İçerik") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    maxLines = Int.MAX_VALUE,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Butonlar - klavyenin hemen üstünde sabit kalır
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            width = 2.dp
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp
                        )
                    ) {
                        Text(
                            "Geri Çık",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Button(
                        onClick = {
                            onSave(title, content, selectedTag)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 2.dp
                        )
                    ) {
                        Text(
                            "Kaydet",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
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
                    text = note.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = currentColors.textPrimary.copy(alpha = 0.7f),
                    maxLines = 2
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