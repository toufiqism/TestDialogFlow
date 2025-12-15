package com.example.testdialogflow.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.outlined.Contrast
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.testdialogflow.ThemeMode

@Composable
fun ThemeSelector(
    currentThemeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }

    IconButton(
        onClick = { showMenu = true },
        modifier = modifier
    ) {
        Icon(
            imageVector = currentThemeMode.icon,
            contentDescription = "Theme",
            tint = MaterialTheme.colorScheme.onSurface
        )
    }

    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = { showMenu = false }
    ) {
        ThemeMode.entries.forEach { mode ->
            DropdownMenuItem(
                text = { Text(mode.label) },
                onClick = {
                    onThemeModeChange(mode)
                    showMenu = false
                },
                leadingIcon = {
                    Icon(mode.icon, contentDescription = null)
                }
            )
        }
    }
}

val ThemeMode.icon: ImageVector
    get() = when (this) {
        ThemeMode.SYSTEM -> Icons.Outlined.Contrast
        ThemeMode.LIGHT -> Icons.Filled.LightMode
        ThemeMode.DARK -> Icons.Filled.DarkMode
    }

val ThemeMode.label: String
    get() = when (this) {
        ThemeMode.SYSTEM -> "System default"
        ThemeMode.LIGHT -> "Light"
        ThemeMode.DARK -> "Dark"
    }
