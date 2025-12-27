package com.example.easymart.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.easymart.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items ORDER BY addAt DESC")
    fun getAllCartItems(): Flow<List<CartItemEntity>>

    @Query("SELECT * FROM cart_items WHERE productId = :productId LIMIT 1")
    fun getExistingByProductId(productId: Int): CartItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItemEntity)

    @Update
    suspend fun update(cartItem: CartItemEntity)

    @Delete
    suspend fun delete(cartItem: CartItemEntity)

    @Query("UPDATE cart_items SET quantity = quantity + :delta WHERE id = :cartItemId AND quantity + :delta > 0")
    suspend fun updateQuantityById(cartItemId: Int, delta: Int)

    @Query("DELETE FROM cart_items")
    suspend fun clearAllCartItems()

//    @Query("UPDATE cart_items SET isChecked =:isChecked WHERE id =:cartItemId")
//    suspend fun checkedChangeById(cartItemId: Int, isChecked: Boolean)
}