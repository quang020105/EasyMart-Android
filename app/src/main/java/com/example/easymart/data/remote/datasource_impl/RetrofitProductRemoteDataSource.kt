package com.example.easymart.data.remote.datasource_impl

import com.example.easymart.data.mapper.toDomain
import com.example.easymart.data.remote.api.ProductApi
import com.example.easymart.data.remote.datasource.ProductRemoteDataSource
import com.example.easymart.domain.model.Product
import javax.inject.Inject

class RetrofitProductRemoteDataSource @Inject constructor(
    private val api: ProductApi
): ProductRemoteDataSource {
    override suspend fun getAllProducts(): List<Product> {
        val response = api.getAllProduct()
        if (response.isSuccessful){
            return response.body().orEmpty().map { it.toDomain() }
        } else {
            throw Exception("Failed to fetch products: ${response.code()} ${response.message()}")
        }
    }

    override suspend fun getProductById(productId: Int): Product? {
        val response = api.getProductById(productId)
        if (response.isSuccessful) {
            return response.body()?.toDomain()
        } else {
            throw Exception("Failed to fetch product: ${response.code()} ${response.message()}")
        }
    }
}