package com.example.easymart.data.remote.api

import com.example.easymart.data.remote.dto.gemini.GeminiGenerateContentRequest
import com.example.easymart.data.remote.dto.gemini.GeminiGenerateContentResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface GeminiApi {
    @POST("v1beta/models/{model}:generateContent")
    suspend fun generateContent(
        @Path("model") model: String,
        @Header("x-goog-api-key") apiKey: String,
        @Body body: GeminiGenerateContentRequest
    ): GeminiGenerateContentResponse
}