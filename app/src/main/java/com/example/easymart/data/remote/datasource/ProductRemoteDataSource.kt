package com.example.easymart.data.remote.datasource

import com.example.easymart.domain.model.Product

interface ProductRemoteDataSource {
    suspend fun getAllProducts(): List<Product>
    suspend fun getProductById(productId: Int): Product?
}