package com.example.chatbot.repository

import com.example.chatbot.data.ChatRequest
import com.example.chatbot.network.AuthInterceptor
import com.example.chatbot.network.ChatApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChatRepository {
    val BASE_URL = "http://guanaco-submitter.guanaco-backend.k2.chaiverse.com"
    val BEARER_TOKEN = "CR_14d43f2bf78b4b0590c2a8b87f354746"

    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(
            AuthInterceptor {
                BEARER_TOKEN
            }
        )
        .build()

    private val chatApiService: ChatApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ChatApiService::class.java)

    suspend fun sendMessage(request: ChatRequest): Result<String> {
        return try {
            val response = chatApiService.sendMessage(request)
            if (response.isSuccessful) {
                val chatResponse = response.body()
                Result.success(chatResponse?.response ?: "")
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}