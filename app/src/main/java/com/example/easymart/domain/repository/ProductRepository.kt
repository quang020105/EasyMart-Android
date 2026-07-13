package com.example.easymart.domain.repository

import com.example.easymart.domain.model.Product
import com.example.easymart.domain.model.Order
import com.example.easymart.presentation.common.Resource
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getAllProduct(): Flow<Resource<List<Product>>>
    suspend fun addToCart(product: Product, quantity: Int)
    fun getProductById(productId: Int): Flow<Resource<Product>>
    suspend fun refreshProducts(): Resource<Unit>
    suspend fun refreshProductById(productId: Int): Resource<Unit>
    suspend fun upsertProduct(product: Product): Resource<Unit>
    suspend fun syncProducts(): Resource<Unit>
    suspend fun importFakeStoreProductsToFirestore(): Resource<Int>
    suspend fun updateVisibilityLocalOnly(productId: Int, isVisible: Boolean): Resource<Unit>
    suspend fun deductStockForOrder(order: Order): Resource<Unit>
}
