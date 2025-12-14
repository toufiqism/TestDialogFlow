package com.example.testdialogflow

import android.content.Context
import com.google.auth.oauth2.GoogleCredentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.InputStream

class DialogflowAuthManager(private val context: Context) {
    
    private var credentials: GoogleCredentials? = null
    private val mutex = Mutex()
    
    private val DIALOGFLOW_SCOPE = "https://www.googleapis.com/auth/dialogflow"
    
    suspend fun getAccessToken(): String = withContext(Dispatchers.IO) {
        mutex.withLock {
            if (credentials == null) {
                val inputStream: InputStream = context.resources.openRawResource(R.raw.creds)
                credentials = GoogleCredentials.fromStream(inputStream)
                    .createScoped(listOf(DIALOGFLOW_SCOPE))
            }
            
            // Refresh if expired or about to expire
            credentials!!.refreshIfExpired()
            credentials!!.accessToken.tokenValue
        }
    }
}
