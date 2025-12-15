package com.example.testdialogflow.chat

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.testdialogflow.ui.components.ChatBubble
import com.example.testdialogflow.ui.components.MessageInput
import kotlinx.coroutines.flow.Flow

@Composable
fun ChatScreen(
    state: ChatContract.State,
    effectFlow: Flow<ChatContract.Effect>,
    onIntent: (ChatContract.Intent) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    var inputText by remember { mutableStateOf("") }
    val imeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0

    // Handle side effects
    LaunchedEffect(Unit) {
        effectFlow.collect { effect ->
            when (effect) {
                is ChatContract.Effect.ShowError -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                is ChatContract.Effect.ScrollToBottom -> {
                    if (state.messages.isNotEmpty()) {
                        listState.animateScrollToItem(state.messages.size - 1)
                    }
                }
                is ChatContract.Effect.ClearInput -> {
                    inputText = ""
                }
            }
        }
    }

    // Auto-scroll when keyboard opens
    LaunchedEffect(imeVisible) {
        if (imeVisible && state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .navigationBarsPadding()
            .animateContentSize()
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }
            items(state.messages, key = { it.timestamp }) { message ->
                ChatBubble(message = message)
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 3.dp
        ) {
            MessageInput(
                text = inputText,
                onTextChange = { inputText = it },
                onSend = { onIntent(ChatContract.Intent.SendMessage(inputText)) },
                isLoading = state.isLoading,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }
    }
}
