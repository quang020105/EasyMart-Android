package com.example.easymart.domain.repository

import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.common.Resource
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getAllProduct(): Flow<Resource<List<Product>>>
    suspend fun addToCart(product: Product, quantity: Int)
    fun getProductById(productId: Int): Flow<Resource<Product>>
    suspend fun refreshProducts(): Resource<Unit>
    suspend fun refreshProductById(productId: Int): Resource<Unit>
}