package com.example.chatbot.data

import com.google.gson.annotations.SerializedName

data class ChatRequest(
    val memory: String = "",
    val prompt: String = "",

    @SerializedName("bot_name")
    val botName: String = "Einstein",

    @SerializedName("user_name")
    val userName: String = "User",

    @SerializedName("chat_history")
    val chatHistory: List<ChatMessage> = emptyList()
)

data class ChatMessage(
    val sender: String,
    val message: String
)
