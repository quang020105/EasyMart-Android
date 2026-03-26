package com.example.easymart.data.remote.api

import retrofit2.http.Body
import retrofit2.http.POST

interface AlgoliaApi {
    @POST("algolia/key")
    suspend fun getAlgoliaKey(@Body request: AlgoliaKeyRequest): AlgoliaKeyResponse
}

data class AlgoliaKeyRequest(
    val index: String = "products",
    val ttlSeconds: Int = 300
)

data class AlgoliaKeyResponse(
    val appId: String,
    val apiKey: String,
    val index: String
)