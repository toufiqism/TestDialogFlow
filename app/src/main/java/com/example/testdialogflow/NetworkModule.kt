package com.example.testdialogflow

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {
    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // See JSON logs in Logcat
        })
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://dialogflow.googleapis.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val dialogflowService: DialogflowService = retrofit.create(DialogflowService::class.java)
}