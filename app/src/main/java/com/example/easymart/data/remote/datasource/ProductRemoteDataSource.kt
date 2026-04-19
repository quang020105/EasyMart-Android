package com.example.easymart.data.remote.datasource

import com.example.easymart.data.remote.api.ProductApi
import com.example.easymart.data.remote.dto.ProductDto
import retrofit2.Response
import javax.inject.Inject

class ProductRemoteDataSource @Inject constructor(
    private val api: ProductApi
) {
    suspend fun getAllProducts(): Response<List<ProductDto>> = api.getAllProduct()

    suspend fun getProductById(id: Int): Response<ProductDto> = api.getProductById(id)
}

