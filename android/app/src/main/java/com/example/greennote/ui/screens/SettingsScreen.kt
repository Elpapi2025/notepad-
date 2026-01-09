package com.example.greennote.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.greennote.data.SettingsManager
import kotlinx.coroutines.launch
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Slider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.greennote.R
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import java.util.Locale
import androidx.compose.foundation.clickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    settingsManager: SettingsManager
) {
    val scope = rememberCoroutineScope()
    val isDarkMode by settingsManager.isDarkMode.collectAsState(initial = false)
    val languageSetting by settingsManager.languageSetting.collectAsState(initial = "en")
    val fontSizeSetting by settingsManager.fontSizeSetting.collectAsState(initial = 16)

    val context = LocalContext.current

    var showLanguageMenu by remember { mutableStateOf(false) }
    var showFontSizeMenu by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.settings_title)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back_button_desc)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).padding(16.dp)
        ) {
            // Dark Mode Setting
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(id = R.string.dark_mode_setting), style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.width(16.dp))
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { newValue ->
                        scope.launch {
                            settingsManager.setDarkMode(newValue)
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            // Language Setting
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(id = R.string.language_setting), style = MaterialTheme.typography.bodyLarge)
                DropdownMenu(
                    expanded = showLanguageMenu,
                    onDismissRequest = { showLanguageMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(id = R.string.language_english)) },
                        onClick = {
                            scope.launch {
                                settingsManager.setLanguageSetting("en")
                                setLocale(context, "en")
                                showLanguageMenu = false
                            }
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(id = R.string.language_spanish)) },
                        onClick = {
                            scope.launch {
                                settingsManager.setLanguageSetting("es")
                                setLocale(context, "es")
                                showLanguageMenu = false
                            }
                        }
                    )
                }
                Text(
                    text = if (languageSetting == "es") stringResource(id = R.string.language_spanish) else stringResource(id = R.string.language_english),
                    modifier = Modifier.clickable { showLanguageMenu = true }
                )
            }
            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            // Font Size Setting
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(id = R.string.font_size_setting), style = MaterialTheme.typography.bodyLarge)
                Slider(
                    value = fontSizeSetting.toFloat(),
                    onValueChange = { newValue ->
                        scope.launch {
                            settingsManager.setFontSizeSetting(newValue.toInt())
                        }
                    },
                    valueRange = 12f..24f,
                    steps = 12,
                    modifier = Modifier.width(150.dp)
                )
                Text(text = fontSizeSetting.toString())
            }
        }
    }
}

// Helper function to change locale
fun setLocale(context: Context, languageCode: String) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)
    val resources: Resources = context.resources
    val config: Configuration = resources.configuration
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        config.setLocale(locale)
    } else {
        config.locale = locale
    }
    resources.updateConfiguration(config, resources.displayMetrics)
}
