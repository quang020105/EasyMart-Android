package com.example.easymart.data.repositoryimpl

import com.example.easymart.data.local.dao.CartDao
import com.example.easymart.data.mapper.toDomain
import com.example.easymart.data.mapper.toEntity
import com.example.easymart.domain.model.CartItem
import com.example.easymart.domain.repository.CartRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao
) : CartRepository {
    override fun getAllCartItems(): Flow<List<CartItem>> =
        cartDao.getAllCartItems().map { list -> list.map { item -> item.toDomain() } }

    override suspend fun addCartItem(cartItem: CartItem) {
        withContext(Dispatchers.IO){
            val existing = cartDao.getExistingByProductId(cartItem.product.id)
            if(existing != null){
                val newQuantity = existing.quantity + cartItem.quantity
                cartDao.update(existing.copy(quantity = newQuantity))
            } else {
                cartDao.insertCartItem(cartItem.toEntity())
            }
        }
    }

    override suspend fun clearAllCartItems() {
        withContext(Dispatchers.IO){
            cartDao.clearAllCartItems()
        }
    }

    override suspend fun updateQuantity(cartItem: CartItem, delta: Int) {
        withContext(Dispatchers.IO){
            cartDao.updateQuantityById(cartItem.id, delta)
        }

    }
}