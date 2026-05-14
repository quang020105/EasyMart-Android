package com.example.easymart.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.easymart.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM product")
    fun observeAll(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM product WHERE isDeleted = 0")
    fun observeActive(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM product WHERE id = :id LIMIT 1")
    fun observeById(id: Int): Flow<ProductEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<ProductEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: ProductEntity)

    @Query("SELECT * FROM product WHERE isSynced = 0")
    suspend fun getUnsynced(): List<ProductEntity>

    @Query("SELECT * FROM product")
    suspend fun getAllOnce(): List<ProductEntity>

    @Query("UPDATE product SET isSynced = 1, updatedAt = :updatedAt, imageUrl = :imageUrl, storagePath = :storagePath, localImageUri = NULL WHERE id = :id")
    suspend fun markSynced(
        id: Int,
        updatedAt: Long,
        imageUrl: String,
        storagePath: String?
    )

    @Query("DELETE FROM product")
    suspend fun clearAll()

    @Transaction
    suspend fun replaceAll(items: List<ProductEntity>) {
        clearAll()
        upsertAll(items)
    }
}
