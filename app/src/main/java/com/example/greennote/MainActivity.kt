package com.example.greennote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.greennote.ui.theme.GreenNoteTheme
import com.example.greennote.data.NoteRepository
import com.example.greennote.ui.screens.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val noteRepository = NoteRepository()

        setContent {
            GreenNoteTheme {
                AppNavigation(noteRepository = noteRepository)
            }
        }
    }
}
