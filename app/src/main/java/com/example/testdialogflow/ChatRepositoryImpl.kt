package com.example.testdialogflow

// Domain Model
data class ChatMessage(
    val text: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

// Repository (Interface)
interface ChatRepository {
    suspend fun sendMessage(text: String, sessionId: String): Result<String>
}

// Repository (Implementation)
class ChatRepositoryImpl(
    private val service: DialogflowService,
    private val authManager: DialogflowAuthManager,
    private val projectId: String = "testagenta-lmwm"
) : ChatRepository {

    override suspend fun sendMessage(text: String, sessionId: String): Result<String> {
        return try {
            val accessToken = authManager.getAccessToken()
            val request = DetectIntentRequest(QueryInput(TextInput(text)))
            val response = service.detectIntent(
                projectId, 
                sessionId, 
                "Bearer $accessToken", 
                request
            )
            Result.success(response.queryResult.fulfillmentText)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}