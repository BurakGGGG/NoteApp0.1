package com.burakgurgil.burak2.repository

import com.burakgurgil.burak2.data.Note
import com.burakgurgil.burak2.data.NoteDao
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteDao) {
    val allNotes: Flow<List<Note>> = noteDao.getAllNotes()
    val deletedNotes: Flow<List<Note>> = noteDao.getDeletedNotes()

    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    suspend fun update(note: Note) {
        noteDao.update(note)
    }

    suspend fun delete(note: Note) {
        noteDao.delete(note)
    }

    suspend fun moveToTrash(noteId: Long) {
        noteDao.moveToTrash(noteId, System.currentTimeMillis())
    }

    suspend fun deleteOldNotesFromTrash(thresholdTimestamp: Long) {
        noteDao.deleteOldNotesFromTrash(thresholdTimestamp)
    }

    suspend fun restoreFromTrash(noteId: Long) {
        noteDao.restoreFromTrash(noteId)
    }

    suspend fun emptyTrash() {
        noteDao.emptyTrash()
    }

    suspend fun toggleStarred(noteId: Long, isStarred: Boolean) {
        noteDao.toggleStarred(noteId, isStarred)
    }

    suspend fun toggleLocked(noteId: Long, isLocked: Boolean) {
        noteDao.toggleLocked(noteId, isLocked)
    }
} 