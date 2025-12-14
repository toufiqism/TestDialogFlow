package com.example.testdialogflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testdialogflow.ui.theme.TestDialogFlowTheme

class MainActivity : ComponentActivity() {

    private val viewModel: ChatViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val authManager = DialogflowAuthManager(this@MainActivity)
                val repository = ChatRepositoryImpl(
                    NetworkModule.dialogflowService,
                    authManager
                )
                @Suppress("UNCHECKED_CAST")
                return ChatViewModel(repository) as T
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestDialogFlowTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ChatScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatScreen(viewModel: ChatViewModel, modifier: Modifier = Modifier) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    // Auto-scroll to bottom when new messages arrive
    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size - 1)
        }
    }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        // Chat History
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.messages) { message ->
                ChatBubble(message)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Input Area
        MessageInput(onSend = { viewModel.sendMessage(it) }, isLoading = uiState.isLoading)
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val alignment = if (message.isFromUser) Alignment.CenterEnd else Alignment.CenterStart
    val color = if (message.isFromUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = alignment) {
        Text(
            text = message.text,
            modifier = Modifier
                .background(color, shape = RoundedCornerShape(12.dp))
                .padding(12.dp),
            color = if (message.isFromUser) Color.White else Color.Black
        )
    }
}

@Composable
fun MessageInput(onSend: (String) -> Unit, isLoading: Boolean) {
    var text by remember { mutableStateOf("") }

    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.weight(1f),
            placeholder = { Text("Type a message...") }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = {
                onSend(text)
                text = ""
            },
            enabled = !isLoading && text.isNotBlank()
        ) {
            Text("Send")
        }
    }
}