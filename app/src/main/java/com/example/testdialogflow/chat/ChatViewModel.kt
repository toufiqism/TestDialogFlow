package com.example.testdialogflow.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testdialogflow.ChatMessage
import com.example.testdialogflow.ChatRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class ChatViewModel(
    private val repository: ChatRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ChatContract.State.initial())
    val state: StateFlow<ChatContract.State> = _state.asStateFlow()

    private val _effect = Channel<ChatContract.Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    private val sessionId = UUID.randomUUID().toString()
    private var lastUserMessage: String? = null

    fun onIntent(intent: ChatContract.Intent) {
        when (intent) {
            is ChatContract.Intent.SendMessage -> sendMessage(intent.text)
            is ChatContract.Intent.ClearChat -> clearChat()
            is ChatContract.Intent.RetryLastMessage -> retryLastMessage()
        }
    }

    private fun sendMessage(text: String) {
        if (text.isBlank()) return
        lastUserMessage = text

        val userMessage = ChatMessage(text = text, isFromUser = true)
        
        _state.update { currentState ->
            currentState.copy(
                messages = currentState.messages + userMessage,
                isLoading = true,
                error = null
            )
        }
        
        sendEffect(ChatContract.Effect.ClearInput)
        sendEffect(ChatContract.Effect.ScrollToBottom)

        viewModelScope.launch {
            val result = repository.sendMessage(text, sessionId)
            
            result.fold(
                onSuccess = { responseText ->
                    val botMessage = ChatMessage(text = responseText, isFromUser = false)
                    _state.update { currentState ->
                        currentState.copy(
                            messages = currentState.messages + botMessage,
                            isLoading = false,
                            error = null
                        )
                    }
                    sendEffect(ChatContract.Effect.ScrollToBottom)
                },
                onFailure = { exception ->
                    val errorMessage = exception.message ?: "Unknown error occurred"
                    _state.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            error = errorMessage
                        )
                    }
                    sendEffect(ChatContract.Effect.ShowError(errorMessage))
                }
            )
        }
    }

    private fun clearChat() {
        _state.update { ChatContract.State.initial() }
        lastUserMessage = null
    }

    private fun retryLastMessage() {
        lastUserMessage?.let { sendMessage(it) }
    }

    private fun sendEffect(effect: ChatContract.Effect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
