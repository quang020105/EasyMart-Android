package com.example.easymart.data.remote.api

import com.example.easymart.data.remote.dto.ProductDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {
    @GET("products")
    suspend fun getAllProduct(): Response<List<ProductDto>>
    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<ProductDto>
}