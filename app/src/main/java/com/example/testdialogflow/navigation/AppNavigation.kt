package com.example.testdialogflow.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.testdialogflow.ChatRepositoryImpl
import com.example.testdialogflow.DialogflowAuthManager
import com.example.testdialogflow.NetworkModule
import com.example.testdialogflow.ThemeMode
import com.example.testdialogflow.chat.ChatScreenWithScaffold
import com.example.testdialogflow.chat.ChatViewModel
import com.example.testdialogflow.settings.SettingsScreen

@Composable
fun AppNavigation(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit
) {
    val backStack = rememberNavBackStack(Route.Chat)
    val context = LocalContext.current

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            entry<Route.Chat> {
                val authManager = remember { DialogflowAuthManager(context) }
                val repository = remember {
                    ChatRepositoryImpl(NetworkModule.dialogflowService, authManager)
                }
                val viewModel: ChatViewModel = viewModel { ChatViewModel(repository) }

                ChatScreenWithScaffold(
                    viewModel = viewModel,
                    onNavigateToSettings = { backStack.add(Route.Settings) }
                )
            }

            entry<Route.Settings> {
                SettingsScreen(
                    currentThemeMode = themeMode,
                    onThemeModeChange = onThemeModeChange,
                    onNavigateBack = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}
