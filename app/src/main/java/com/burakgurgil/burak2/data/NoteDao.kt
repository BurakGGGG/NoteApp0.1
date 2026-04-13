package com.burakgurgil.burak2.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes WHERE isDeleted = 0 ORDER BY isStarred DESC, createdAt DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE isDeleted = 1 ORDER BY createdAt DESC")
    fun getDeletedNotes(): Flow<List<Note>>

    @Insert
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("UPDATE notes SET isDeleted = 1, deletedAt = :timestamp WHERE id = :noteId")
    suspend fun moveToTrash(noteId: Long, timestamp: Long)

    @Query("UPDATE notes SET isDeleted = 0, deletedAt = NULL WHERE id = :noteId")
    suspend fun restoreFromTrash(noteId: Long)

    @Query("DELETE FROM notes WHERE isDeleted = 1 AND deletedAt < :thresholdTimestamp AND deletedAt IS NOT NULL")
    suspend fun deleteOldNotesFromTrash(thresholdTimestamp: Long)

    @Query("DELETE FROM notes WHERE isDeleted = 1")
    suspend fun emptyTrash()

    @Query("UPDATE notes SET isStarred = :isStarred WHERE id = :noteId")
    suspend fun toggleStarred(noteId: Long, isStarred: Boolean)

    @Query("SELECT COUNT(*) FROM notes")
    suspend fun getNoteCount(): Int
} 