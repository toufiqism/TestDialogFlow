package com.example.testdialogflow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false
)

class ChatViewModel(private val repository: ChatRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    // Generate a session ID for this app run
    private val sessionId = UUID.randomUUID().toString()

    fun sendMessage(userText: String) {
        if (userText.isBlank()) return

        // 1. Optimistic Update: Show user message immediately
        val userMessage = ChatMessage(text = userText, isFromUser = true)
        _uiState.update { it.copy(messages = it.messages + userMessage, isLoading = true) }

        viewModelScope.launch {
            // 2. Network Call
            val result = repository.sendMessage(userText, sessionId)

            // 3. Handle Response
            val botMessage = if (result.isSuccess) {
                ChatMessage(text = result.getOrDefault("..."), isFromUser = false)
            } else {
                ChatMessage(text = "Error: ${result.exceptionOrNull()?.message}", isFromUser = false)
            }

            _uiState.update {
                it.copy(messages = it.messages + botMessage, isLoading = false)
            }
        }
    }
}