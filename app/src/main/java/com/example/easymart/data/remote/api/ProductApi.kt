package com.example.easymart.data.remote.api

import com.example.easymart.data.remote.dto.ProductApiDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApi {
    @GET("products")
    suspend fun getAllProduct(): Response<List<ProductApiDto>>
    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<ProductApiDto>
}