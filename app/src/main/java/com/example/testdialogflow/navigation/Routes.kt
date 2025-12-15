package com.example.testdialogflow.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Type-safe navigation routes using Kotlin Serialization
 */
sealed interface Route : NavKey{
    
    @Serializable
    data object Chat : Route
    
    @Serializable
    data object Settings : Route
}
