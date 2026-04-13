package com.burakgurgil.burak2.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.burakgurgil.burak2.data.Note
import com.burakgurgil.burak2.ui.icons.CustomIcons
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.delay
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ViewNoteScreen(
    note: Note,
    onDismiss: () -> Unit,
    onEdit: () -> Unit
) {
    // rememberSaveable kullanarak durumu kaydet
    var searchText by rememberSaveable { mutableStateOf("") }
    var currentMatchIndex by rememberSaveable { mutableStateOf(-1) }
    val scrollState = rememberScrollState()
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    val density = LocalDensity.current
    val richTextState = rememberRichTextState()
    LaunchedEffect(note.content) {
        if (richTextState.toHtml() != note.content) {
            richTextState.setHtml(note.content)
        }
    }
    
    // Eşleşmeleri güvenli bir şekilde hesapla ve önbelleğe al
    val plainTextContent = richTextState.annotatedString.text
    val matches = remember(searchText, plainTextContent) {
        if (searchText.isBlank()) emptyList()
        else {
            val contentLower = plainTextContent.lowercase(Locale("tr"))
            val searchTextLower = searchText.lowercase(Locale("tr"))
            var index = 0
            val result = mutableListOf<Int>()
            while (index < contentLower.length) {
                val foundIndex = contentLower.indexOf(searchTextLower, index)
                if (foundIndex == -1) break
                result.add(foundIndex)
                index = foundIndex + searchText.length
            }
            result
        }
    }

    // Seçili eşleşmeye otomatik scroll et
    LaunchedEffect(currentMatchIndex, textLayoutResult) {
        if (matches.isNotEmpty() && currentMatchIndex in matches.indices && textLayoutResult != null) {
            delay(100) // UI güncellemesi için kısa bir gecikme
            val matchStartIndex = matches[currentMatchIndex]
            
            textLayoutResult?.let { layoutResult ->
                try {
                    // Eşleşmenin hangi satırda olduğunu bul
                    val lineIndex = layoutResult.getLineForOffset(matchStartIndex)
                    // O satırın y pozisyonunu al
                    val matchY = layoutResult.getLineTop(lineIndex)
                    
                    // Scroll pozisyonunu hesapla (ekranın üst kısmından biraz aşağıya getirmek için)
                    val scrollY = with(density) {
                        (matchY - 100.dp.toPx()).toInt().coerceAtLeast(0)
                    }
                    
                    scrollState.animateScrollTo(scrollY)
                } catch (e: Exception) {
                    // Hata durumunda sessizce devam et
                }
            }
        }
    }

    // Tarih formatını önbelleğe al
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }
    val formattedDate = remember(note.createdAt) { dateFormat.format(note.createdAt) }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                        ) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Geri",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    actions = {
                        Button(
                            onClick = onEdit,
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .height(44.dp),
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
                            Icon(
                                CustomIcons.Edit,
                                contentDescription = "Düzenle",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Düzenle",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Ara",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        TextField(
                            value = searchText,
                            onValueChange = { 
                                searchText = it
                                currentMatchIndex = -1
                            },
                            placeholder = { 
                                Text(
                                    "Notta ara...",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )
                        if (searchText.isNotBlank()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "${matches.size} eşleşme",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                if (currentMatchIndex >= 0) {
                                    Text(
                                        text = "${currentMatchIndex + 1}/${matches.size}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                IconButton(
                                    onClick = { 
                                        if (matches.isNotEmpty()) {
                                            currentMatchIndex = (currentMatchIndex - 1 + matches.size) % matches.size
                                        }
                                    },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        Icons.Default.KeyboardArrowUp,
                                        contentDescription = "Önceki",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                IconButton(
                                    onClick = { 
                                        if (matches.isNotEmpty()) {
                                            currentMatchIndex = (currentMatchIndex + 1) % matches.size
                                        }
                                    },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Sonraki",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                IconButton(
                                    onClick = { 
                                        searchText = ""
                                        currentMatchIndex = -1
                                    },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Temizle",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
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
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Geri",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Geri Dön",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            if (searchText.isBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            if (searchText.isBlank()) {
                RichText(
                    state = richTextState,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            } else {
                val baseAnnotatedString = richTextState.annotatedString
                val contentText = baseAnnotatedString.text
                val searchTextLower = searchText.lowercase(Locale("tr"))
                val contentLower = contentText.lowercase(Locale("tr"))
                
                var lastIndex = 0
                val highlightedText = buildAnnotatedString {
                    append(baseAnnotatedString)
                    
                    while (lastIndex < contentText.length) {
                        val startIndex = contentLower.indexOf(searchTextLower, lastIndex)
                        if (startIndex == -1) {
                            break
                        }
                        
                        if (matches.indexOf(startIndex) == currentMatchIndex) {
                            addStyle(
                                style = SpanStyle(
                                    background = MaterialTheme.colorScheme.primary,
                                    color = MaterialTheme.colorScheme.onPrimary
                                ),
                                start = startIndex,
                                end = startIndex + searchText.length
                            )
                        } else {
                            addStyle(
                                style = SpanStyle(
                                    background = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                ),
                                start = startIndex,
                                end = startIndex + searchText.length
                            )
                        }
                        
                        lastIndex = startIndex + searchText.length
                    }
                }
                
                Text(
                    text = highlightedText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    onTextLayout = { textLayoutResult = it }
                )
            }
        }
    }
}