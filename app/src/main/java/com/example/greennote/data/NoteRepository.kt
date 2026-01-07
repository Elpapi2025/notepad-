package com.example.greennote.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID
import java.util.Date

class NoteRepository {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    init {
        // Pre-populate with some sample data
        _notes.value = listOf(
            Note(
                id = UUID.randomUUID().toString(),
                title = "Welcome to GreenNote!",
                content = "This is a native Android version of the GreenNote app, built with Kotlin and Jetpack Compose.",
                createdAt = Date().time
            ),
            Note(
                id = UUID.randomUUID().toString(),
                title = "My Second Note",
                content = "You can create, edit, and delete notes.",
                createdAt = Date().time - 1000 * 60 * 5 // 5 minutes ago
            )
        )
    }

    fun getNoteById(id: String): Note? {
        return _notes.value.find { it.id == id }
    }

    fun addNote(title: String, content: String) {
        val newNote = Note(
            id = UUID.randomUUID().toString(),
            title = title,
            content = content
        )
        _notes.update { currentNotes ->
            listOf(newNote) + currentNotes
        }
    }

    fun updateNote(id: String, title: String, content: String) {
        _notes.update { currentNotes ->
            currentNotes.map { note ->
                if (note.id == id) {
                    note.copy(title = title, content = content)
                } else {
                    note
                }
            }
        }
    }

    fun deleteNote(id: String) {
        _notes.update { currentNotes ->
            currentNotes.filterNot { it.id == id }
        }
    }
}
