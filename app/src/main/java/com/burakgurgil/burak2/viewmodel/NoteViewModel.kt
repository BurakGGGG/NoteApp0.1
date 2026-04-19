package com.burakgurgil.burak2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.burakgurgil.burak2.data.Note
import com.burakgurgil.burak2.data.NoteDatabase
import com.burakgurgil.burak2.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NoteRepository
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    private val _deletedNotes = MutableStateFlow<List<Note>>(emptyList())
    val deletedNotes: StateFlow<List<Note>> = _deletedNotes.asStateFlow()

    init {
        val noteDao = NoteDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(noteDao)
        
        // Tek bir coroutine scope içinde her iki Flow'u da topla
        viewModelScope.launch {
            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                repository.allNotes.collect { noteList ->
                    _notes.value = noteList
                }
            }
        }
        
        viewModelScope.launch {
            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                repository.deletedNotes.collect { noteList ->
                    _deletedNotes.value = noteList
                }
            }
        }
    }

    fun insert(note: Note, onInserted: ((Long) -> Unit)? = null) = viewModelScope.launch {
        val id = repository.insert(note)
        onInserted?.invoke(id)
    }

    fun update(note: Note) = viewModelScope.launch {
        repository.update(note)
    }

    fun delete(note: Note) = viewModelScope.launch {
        repository.delete(note)
    }

    fun moveToTrash(noteId: Long) = viewModelScope.launch {
        repository.moveToTrash(noteId)
    }

    fun deleteOldNotesFromTrash(thresholdTimestamp: Long) = viewModelScope.launch {
        repository.deleteOldNotesFromTrash(thresholdTimestamp)
    }

    fun restoreFromTrash(noteId: Long) = viewModelScope.launch {
        repository.restoreFromTrash(noteId)
    }

    fun emptyTrash() = viewModelScope.launch {
        repository.emptyTrash()
    }

    fun toggleStarred(noteId: Long, isStarred: Boolean) = viewModelScope.launch {
        repository.toggleStarred(noteId, isStarred)
    }

    fun toggleLocked(noteId: Long, isLocked: Boolean) = viewModelScope.launch {
        repository.toggleLocked(noteId, isLocked)
    }
} 