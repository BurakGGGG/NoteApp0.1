package com.burakgurgil.burak2.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val createdAt: Date,
    val isDeleted: Boolean = false,
    val isStarred: Boolean = false,
    val tag: String? = null,
    val isLocked: Boolean = false,
    val isArchived: Boolean = false,
    val deletedAt: Date? = null,
    val reminderTime: Long? = null
) 