package com.example.greennote.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.greennote.data.NoteRepository
import com.example.greennote.data.SettingsManager

@Composable
fun AppNavigation(
    noteRepository: NoteRepository,
    settingsManager: SettingsManager,
    hasSeenOnboarding: Boolean
) {
    val navController = rememberNavController()
    val startDestination = if (hasSeenOnboarding) "noteList" else "onboarding"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("onboarding") {
            OnboardingScreen(navController = navController, settingsManager = settingsManager)
        }
        composable("noteList") {
            NoteListScreen(navController = navController, noteRepository = noteRepository)
        }
        composable("noteDetail/{noteId}") { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")
            NoteEditScreen(
                navController = navController,
                noteRepository = noteRepository,
                noteId = noteId
            )
        }
        composable("settings") {
            SettingsScreen(navController = navController, settingsManager = settingsManager)
        }
    }
}
