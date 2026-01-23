package com.example.easymart.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.easymart.data.local.entity.CartEntity
import com.example.easymart.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    //cart

    @Query("SELECT * FROM carts WHERE userId = :userId LIMIT 1")
    suspend fun getUserCart(userId: String): CartEntity?

    @Query("SELECT * FROM carts WHERE userId IS NULL LIMIT 1")
    suspend fun getGuestCart(): CartEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(cart: CartEntity)

    @Delete
    suspend fun deleteCart(cart: CartEntity)


    //cart items

    @Query("SELECT * FROM cart_items WHERE cartId = :cartId ORDER BY addAt DESC")
    fun getAllCartItems(cartId: String): Flow<List<CartItemEntity>>

    //dùng để lấy danh sách sản phẩm trong giỏ hàng một lần (xử lý logic)
    @Query("SELECT * FROM cart_items WHERE cartId = :cartId")
    suspend fun getAllCartItemsOnce(cartId: String): List<CartItemEntity>

    @Query("SELECT * FROM cart_items WHERE cartId = :cartId AND productId = :productId LIMIT 1")
    suspend fun getExistingByProductId(cartId: String, productId: Int): CartItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItemEntity)

    @Update
    suspend fun updateCartItem(cartItem: CartItemEntity)

    @Delete
    suspend fun deleteCartItem(cartItem: CartItemEntity)

    @Query(
        """
        UPDATE cart_items 
        SET quantity = quantity + :delta 
        WHERE id = :cartItemId AND quantity + :delta > 0
        """
    )
    suspend fun updateQuantityById(cartItemId: Int, delta: Int)

    @Query("DELETE FROM cart_items WHERE cartId = :cartId")
    suspend fun clearAllCartItems(cartId: String)

//    @Query("UPDATE cart_items SET isChecked =:isChecked WHERE id =:cartItemId")
//    suspend fun checkedChangeById(cartItemId: Int, isChecked: Boolean)
}