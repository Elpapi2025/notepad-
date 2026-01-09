package com.example.greennote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import com.example.greennote.data.NoteRepository
import com.example.greennote.data.SettingsManager
import com.example.greennote.ui.screens.AppNavigation
import com.example.greennote.ui.theme.GreenNoteTheme
import com.example.greennote.ui.screens.setLocale // Import the setLocale function
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val noteRepository = NoteRepository(applicationContext)
        val settingsManager = SettingsManager(this)

        setContent {
            val isDarkMode by settingsManager.isDarkMode.collectAsState(initial = false)
            val hasSeenOnboarding by settingsManager.hasSeenOnboarding.collectAsState(initial = null)
            val languageSetting by settingsManager.languageSetting.collectAsState(initial = "en") // Observe language setting

            val context = LocalContext.current

            LaunchedEffect(languageSetting) {
                setLocale(context, languageSetting)
            }

            GreenNoteTheme(darkTheme = isDarkMode) {
                if (hasSeenOnboarding != null) { // Wait until the preference is loaded
                    AppNavigation(
                        noteRepository = noteRepository,
                        settingsManager = settingsManager,
                        hasSeenOnboarding = hasSeenOnboarding!!
                    )
                }
            }
        }
    }
}
