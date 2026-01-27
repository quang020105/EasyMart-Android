package com.example.easymart.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.easymart.data.local.dao.AddressDao
import com.example.easymart.data.local.dao.CartDao
import com.example.easymart.data.local.dao.OrderDao
import com.example.easymart.data.local.dao.PaymentDao
import com.example.easymart.data.local.dao.WalletDao
import com.example.easymart.data.local.entity.AddressEntity
import com.example.easymart.data.local.entity.CartEntity
import com.example.easymart.data.local.entity.CartItemEntity
import com.example.easymart.data.local.entity.OrderEntity
import com.example.easymart.data.local.entity.OrderItemEntity
import com.example.easymart.data.local.entity.PaymentEntity
import com.example.easymart.data.local.entity.WalletEntity

@Database(
    entities = [CartEntity::class, CartItemEntity::class, AddressEntity::class, WalletEntity::class, OrderEntity::class, OrderItemEntity::class, PaymentEntity::class],
    version = 15,
    exportSchema = false
)
abstract class EasyMartDatabase : RoomDatabase() {
    abstract fun getCartDao(): CartDao
    abstract fun getALlAddressDao(): AddressDao
    abstract fun getWalletDao(): WalletDao
    abstract fun getOrderDao(): OrderDao
    abstract fun getPaymentDao(): PaymentDao
}