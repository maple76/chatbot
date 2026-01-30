package com.example.chatbot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot.data.ChatMessage
import com.example.chatbot.repository.ChatRepository
import com.example.chatbot.data.ChatRequest
import com.example.chatbot.data.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val repository = ChatRepository()

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun sendMessage(text: String) {
        val userMessage = Message(text, isUser = true)
        _messages.value += userMessage
        _isLoading.value = true
        _errorMessage.value = null // Clear previous errors

        viewModelScope.launch {
            val chatHistory = _messages.value.filter { it != userMessage }.map {
                ChatMessage(
                    sender = if (it.isUser) "User" else "Bot",
                    message = it.text
                )
            }

            val request = ChatRequest(
                prompt = text, 
                chatHistory = chatHistory
            )

            val result = repository.sendMessage(request)
            result.onSuccess {
                _messages.value += Message(it, isUser = false)
            }.onFailure {
                _errorMessage.value = "Failed to get response: ${it.message}"
            }.also {
                _isLoading.value = false
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}
