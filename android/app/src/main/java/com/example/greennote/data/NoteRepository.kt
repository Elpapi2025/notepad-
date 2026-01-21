package com.example.greennote.data

import android.content.Context
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import java.util.Date
import java.text.SimpleDateFormat
import java.util.*
import com.example.greennote.data.AppDatabase
import com.example.greennote.data.NoteDao

class NoteRepository(private val context: Context) {

    private val noteDao: NoteDao by lazy {
        AppDatabase.getDatabase(context).noteDao()
    }

    val notes: Flow<List<Note>> = noteDao.getAllNotes()

    suspend fun getNoteById(id: String): Note? {
        return noteDao.getNoteById(id)
    }

    suspend fun addNote(title: String, content: String, color: String?) {
        val newNote = Note(
            id = UUID.randomUUID().toString(),
            title = title,
            content = content,
            createdAt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(Date()),
            color = color
        )
        noteDao.insertNote(newNote)
    }

    suspend fun updateNote(id: String, title: String, content: String, color: String?) {
        val existingNote = noteDao.getNoteById(id)
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
        val existingNote = noteDao.getNoteById(id)
        if (existingNote != null) {
            noteDao.deleteNote(existingNote)
        }
    }
}
