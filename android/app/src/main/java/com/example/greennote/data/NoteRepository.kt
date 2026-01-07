package com.example.greennote.data

import kotlinx.coroutines.flow.Flow
import java.util.UUID
import java.util.Date

class NoteRepository(private val noteDao: NoteDao) {

    val notes: Flow<List<Note>> = noteDao.getAllNotes()

    suspend fun getNoteById(id: String): Note? {
        return noteDao.getNoteById(id)
    }

    suspend fun addNote(title: String, content: String, color: Long) {
        val newNote = Note(
            id = UUID.randomUUID().toString(),
            title = title,
            content = content,
            createdAt = Date().time,
            color = color
        )
        noteDao.insertNote(newNote)
    }

    suspend fun updateNote(id: String, title: String, content: String, color: Long) {
        // First, get the existing note to preserve its creation date
        val existingNote = getNoteById(id)
        if (existingNote != null) {
            val updatedNote = existingNote.copy(
                title = title,
                content = content,
                color = color
            )
            noteDao.updateNote(updatedNote)
        }
    }

    suspend fun deleteNote(id: String) {
        val noteToDelete = getNoteById(id)
        if (noteToDelete != null) {
            noteDao.deleteNote(noteToDelete)
        }
    }
}
