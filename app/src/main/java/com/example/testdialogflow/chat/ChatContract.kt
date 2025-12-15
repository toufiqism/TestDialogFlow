package com.example.testdialogflow.chat

import com.example.testdialogflow.ChatMessage

/**
 * MVI Contract for Chat feature
 * Defines the Intent (user actions), State (UI state), and Effect (one-time events)
 */
object ChatContract {
    
    // User Intents - actions the user can perform
    sealed class Intent {
        data class SendMessage(val text: String) : Intent()
        data object ClearChat : Intent()
        data object RetryLastMessage : Intent()
    }
    
    // UI State - represents the current state of the screen
    data class State(
        val messages: List<ChatMessage> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val inputText: String = ""
    ) {
        companion object {
            fun initial() = State()
        }
    }
    
    // Side Effects - one-time events (navigation, toasts, etc.)
    sealed class Effect {
        data class ShowError(val message: String) : Effect()
        data object ScrollToBottom : Effect()
        data object ClearInput : Effect()
    }
}
