package com.example.chatbot.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenProvider: () -> String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val request = original.newBuilder()
            .addHeader(
                "Authorization",
                "Bearer ${tokenProvider()}"
            )
            .build()

        return chain.proceed(request)
    }
}