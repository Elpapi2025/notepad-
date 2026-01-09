package com.example.greennote.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsManager(context: Context) {

    private val appContext = context.applicationContext

    companion object {
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val HAS_SEEN_ONBOARDING = booleanPreferencesKey("has_seen_onboarding")
        val LANGUAGE_SETTING = stringPreferencesKey("language_setting")
        val FONT_SIZE_SETTING = intPreferencesKey("font_size_setting")
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

    val languageSetting: Flow<String> = appContext.dataStore.data
        .map { preferences ->
            preferences[LANGUAGE_SETTING] ?: "en" // Default to English
        }

    suspend fun setLanguageSetting(languageCode: String) {
        appContext.dataStore.edit { preferences ->
            preferences[LANGUAGE_SETTING] = languageCode
        }
    }

    val fontSizeSetting: Flow<Int> = appContext.dataStore.data
        .map { preferences ->
            preferences[FONT_SIZE_SETTING] ?: 16 // Default font size
        }

    suspend fun setFontSizeSetting(fontSize: Int) {
        appContext.dataStore.edit { preferences ->
            preferences[FONT_SIZE_SETTING] = fontSize
        }
    }
}

