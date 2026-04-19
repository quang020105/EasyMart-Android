package com.example.easymart.data.local.datasource

import com.example.easymart.data.local.dao.ProductDao
import com.example.easymart.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductLocalDataSource @Inject constructor(
    private val productDao: ProductDao
) {
    fun observeProducts(): Flow<List<ProductEntity>> = productDao.observeAll()

    fun observeProductById(productId: Int): Flow<ProductEntity?> = productDao.observeById(productId)

    suspend fun upsertAll(items: List<ProductEntity>) = productDao.upsertAll(items)

    suspend fun upsert(item: ProductEntity) = productDao.upsert(item)
}

