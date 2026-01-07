package com.example.greennote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.greennote.data.AppDatabase
import com.example.greennote.data.NoteRepository
import com.example.greennote.data.SettingsManager
import com.example.greennote.ui.screens.AppNavigation
import com.example.greennote.ui.theme.GreenNoteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(this)
        val noteRepository = NoteRepository(database.noteDao())
        val settingsManager = SettingsManager(this)

        setContent {
            val isDarkMode by settingsManager.isDarkMode.collectAsState(initial = false)
            GreenNoteTheme(darkTheme = isDarkMode) {
                AppNavigation(noteRepository = noteRepository, settingsManager = settingsManager)
            }
        }
    }
}
