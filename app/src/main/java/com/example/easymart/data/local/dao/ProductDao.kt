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

    @Query("SELECT * FROM product WHERE id = :id LIMIT 1")
    fun observeById(id: Int): Flow<ProductEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<ProductEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: ProductEntity)

    @Query("DELETE FROM product")
    suspend fun clearAll()

    @Transaction
    suspend fun replaceAll(items: List<ProductEntity>) {
        clearAll()
        upsertAll(items)
    }
}

