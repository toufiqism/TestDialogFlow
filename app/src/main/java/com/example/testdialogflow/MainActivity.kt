package com.example.testdialogflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import com.example.testdialogflow.navigation.AppNavigation
import com.example.testdialogflow.ui.theme.TestDialogFlowTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()

        setContent {
            val context = LocalContext.current
            val themePreferences = remember { ThemePreferences(context) }
            val themeMode by themePreferences.themeMode.collectAsState(initial = ThemeMode.SYSTEM)
            val scope = rememberCoroutineScope()

            val isDarkTheme = when (themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }

            TestDialogFlowTheme(darkTheme = isDarkTheme) {
                AppNavigation(
                    themeMode = themeMode,
                    onThemeModeChange = { mode ->
                        scope.launch { themePreferences.setThemeMode(mode) }
                    }
                )
            }
        }
    }
}
