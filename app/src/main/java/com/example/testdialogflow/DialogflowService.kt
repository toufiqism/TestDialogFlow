package com.example.testdialogflow

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

// 1. Request DTOs
data class DetectIntentRequest(
    val queryInput: QueryInput
)

data class QueryInput(
    val text: TextInput
)

data class TextInput(
    val text: String,
    val languageCode: String = "en-US"
)

// 2. Response DTOs
data class DetectIntentResponse(
    val queryResult: QueryResult
)

data class QueryResult(
    val fulfillmentText: String
)



interface DialogflowService {
    @POST("v2/projects/{projectId}/agent/sessions/{sessionId}:detectIntent")
    suspend fun detectIntent(
        @Path("projectId") projectId: String,
        @Path("sessionId") sessionId: String,
        @Header("Authorization") token: String, // Bearer <Access Token>
        @Body request: DetectIntentRequest
    ): DetectIntentResponse
}