package com.example.greennote.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsManager(context: Context) {

    private val appContext = context.applicationContext

    companion object {
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val HAS_SEEN_ONBOARDING = booleanPreferencesKey("has_seen_onboarding")
    }

    val isDarkMode: Flow<Boolean> = appContext.dataStore.data
        .map { preferences ->
            preferences[IS_DARK_MODE] ?: false
        }

    suspend fun setDarkMode(isDarkMode: Boolean) {
        appContext.dataStore.edit { preferences ->
            preferences[IS_DARK_MODE] = isDarkMode
        }
    }

    val hasSeenOnboarding: Flow<Boolean> = appContext.dataStore.data
        .map { preferences ->
            preferences[HAS_SEEN_ONBOARDING] ?: false
        }

    suspend fun setOnboardingSeen() {
        appContext.dataStore.edit { preferences ->
            preferences[HAS_SEEN_ONBOARDING] = true
        }
    }
}
