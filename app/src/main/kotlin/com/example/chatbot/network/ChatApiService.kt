package com.example.chatbot.network

import com.example.chatbot.data.ChatRequest
import com.example.chatbot.data.ChatResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatApiService {
    @POST("endpoints/onsite/chat")
    suspend fun sendMessage(
        @Body request: ChatRequest
    ): Response<ChatResponse>
}