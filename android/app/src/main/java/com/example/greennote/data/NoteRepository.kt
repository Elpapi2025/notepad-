package com.example.greennote.data

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID
import java.util.Date
import java.text.SimpleDateFormat
import java.util.*
import tech.turso.libsql.SQLiteConnection

class NoteRepository(private val context: Context) {

    private suspend fun getConnection(): SQLiteConnection {
        return TursoClient.getConnection(context)
    }

    // This Flow will emit a list of Notes whenever the data changes (requires polling or a change listener if Turso supports it)
    // For simplicity, this will just fetch all notes on subscription. Real-time updates would require more advanced Turso features.
    val notes: Flow<List<Note>> = flow {
        while (true) {
            emit(getAllNotes())
            kotlinx.coroutines.delay(5000) // Poll every 5 seconds for changes
        }
    }

    private suspend fun getAllNotes(): List<Note> {
        val connection = getConnection()
        val resultSet = connection.prepareStatement("SELECT id, title, content, createdAt, color FROM Note").execute()
        val notesList = mutableListOf<Note>()
        resultSet.rows.forEach { row ->
            notesList.add(
                Note(
                    id = row.getString(0) ?: UUID.randomUUID().toString(),
                    title = row.getString(1) ?: "",
                    content = row.getString(2) ?: "",
                    createdAt = row.getString(3) ?: "",
                    color = row.getString(4)
                )
            )
        }
        return notesList
    }

    suspend fun getNoteById(id: String): Note? {
        val connection = getConnection()
        val resultSet = connection.prepareStatement("SELECT id, title, content, createdAt, color FROM Note WHERE id = ?").execute(id)
        return resultSet.rows.firstOrNull()?.let { row ->
            Note(
                id = row.getString(0) ?: UUID.randomUUID().toString(),
                title = row.getString(1) ?: "",
                content = row.getString(2) ?: "",
                createdAt = row.getString(3) ?: "",
                color = row.getString(4)
            )
        }
    }

    suspend fun addNote(title: String, content: String, color: String?) {
        val newNote = Note(
            id = UUID.randomUUID().toString(),
            title = title,
            content = content,
            createdAt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(Date()),
            color = color
        )
        val connection = getConnection()
        connection.prepareStatement(
            "INSERT INTO Note (id, title, content, createdAt, color) VALUES (?, ?, ?, ?, ?)"
        ).execute(newNote.id, newNote.title, newNote.content, newNote.createdAt, newNote.color)
    }

    suspend fun updateNote(id: String, title: String, content: String, color: String?) {
        val connection = getConnection()
        connection.prepareStatement(
            "UPDATE Note SET title = ?, content = ?, color = ? WHERE id = ?"
        ).execute(title, content, color, id)
    }

    suspend fun deleteNote(id: String) {
        val connection = getConnection()
        connection.prepareStatement("DELETE FROM Note WHERE id = ?").execute(id)
    }
}
