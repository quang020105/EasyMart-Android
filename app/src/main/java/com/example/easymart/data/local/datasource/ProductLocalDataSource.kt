package com.example.easymart.data.local.datasource

import com.example.easymart.data.local.dao.ProductDao
import com.example.easymart.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductLocalDataSource @Inject constructor(
    private val productDao: ProductDao
) {
    fun observeProducts(): Flow<List<ProductEntity>> = productDao.observeAll()

    fun observeActiveProducts(): Flow<List<ProductEntity>> = productDao.observeActive()

    fun observeProductById(productId: Int): Flow<ProductEntity?> = productDao.observeById(productId)

    suspend fun upsertAll(items: List<ProductEntity>) = productDao.upsertAll(items)

    suspend fun upsert(item: ProductEntity) = productDao.upsert(item)

    suspend fun getUnsynced(): List<ProductEntity> = productDao.getUnsynced()

    suspend fun getAllOnce(): List<ProductEntity> = productDao.getAllOnce()

    suspend fun markSynced(
        id: Int,
        updatedAt: Long,
        imageUrl: String,
        imageUrlsJson: String,
        storagePath: String?
    ) = productDao.markSynced(id, updatedAt, imageUrl, imageUrlsJson, storagePath)

    suspend fun updateVisibility(
        id: Int,
        isVisible: Boolean,
        updatedAt: Long
    ) = productDao.updateVisibility(id, isVisible, updatedAt)
}
