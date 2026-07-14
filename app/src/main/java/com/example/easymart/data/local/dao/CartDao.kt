package com.example.easymart.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
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
    //@Query("SELECT * FROM cart_items WHERE cartId = :cartId AND isDeleted = 0 ORDER BY addAt DESC")
    @Query("SELECT * FROM cart_items WHERE cartId = :cartId AND isDeleted = 0")
    fun getAllCartItems(cartId: String): Flow<List<CartItemEntity>>

    //dùng để lấy danh sách sản phẩm trong giỏ hàng một lần (xử lý logic)
    @Query("SELECT * FROM cart_items WHERE cartId = :cartId AND isDeleted = 0")
    suspend fun getAllCartItemsOnce(cartId: String): List<CartItemEntity>

    @Query("SELECT * FROM cart_items WHERE cartId = :cartId AND productId = :productId LIMIT 1")
    suspend fun getExistingByProductId(cartId: String, productId: Int): CartItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItemEntity)

    @Query(
        """
        UPDATE cart_items
        SET name = :name,
            price = :price,
            imageUrl = :imageUrl,
            quantity = :quantity,
            isSynced = :isSynced,
            isDeleted = :isDeleted,
            updatedAt = :updatedAt,
            addAt = :addAt
        WHERE cartId = :cartId AND productId = :productId
        """
    )
    suspend fun updateCartItemByProduct(
        cartId: String,
        productId: Int,
        name: String,
        price: Double,
        imageUrl: String?,
        quantity: Int,
        isSynced: Boolean,
        isDeleted: Boolean,
        updatedAt: Long,
        addAt: Long
    ): Int

    @Transaction
    suspend fun upsertCartItemByProduct(cartItem: CartItemEntity) {
        val updatedRows = updateCartItemByProduct(
            cartId = cartItem.cartId,
            productId = cartItem.productId,
            name = cartItem.name,
            price = cartItem.price,
            imageUrl = cartItem.imageUrl,
            quantity = cartItem.quantity,
            isSynced = cartItem.isSynced,
            isDeleted = cartItem.isDeleted,
            updatedAt = cartItem.updatedAt,
            addAt = cartItem.addAt
        )
        if (updatedRows == 0) {
            insertCartItem(cartItem.copy(id = 0))
        }
    }

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

    @Query("SELECT cartId FROM cart_items WHERE id = :cartItemId LIMIT 1")
    suspend fun getCartIdByCartItemId(cartItemId: Int): String?

    @Query("SELECT * FROM carts WHERE id = :cartId LIMIT 1")
    suspend fun getCartById(cartId: String): CartEntity?

    @Query("UPDATE cart_items SET isDeleted = 1, isSynced = 0, updatedAt = :updatedAt WHERE id = :id")
    suspend fun markCartItemDeleted(id: Int, updatedAt: Long)

    @Query("SELECT * FROM cart_items WHERE cartId = :cartId")
    suspend fun getAllCartItemsIncludingDeleted(cartId: String): List<CartItemEntity>

    @Query("""
    SELECT * FROM cart_items
    WHERE cartId = :cartId AND isSynced = 0
    ORDER BY updatedAt ASC
""")
    suspend fun getUnsyncedCartItems(cartId: String): List<CartItemEntity>

    @Query("UPDATE cart_items SET isSynced = 1 WHERE id = :id")
    suspend fun markCartItemSynced(id: Int)

//    @Query("UPDATE cart_items SET isChecked =:isChecked WHERE id =:cartItemId")
//    suspend fun checkedChangeById(cartItemId: Int, isChecked: Boolean)
}
