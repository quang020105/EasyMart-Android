package com.example.easymart.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.easymart.data.local.dao.AddressDao
import com.example.easymart.data.local.dao.CartDao
import com.example.easymart.data.local.entity.AddressEntity
import com.example.easymart.data.local.entity.CartItemEntity

@Database(entities = [CartItemEntity::class, AddressEntity::class], version = 6, exportSchema = false)
abstract class EasyMartDatabase: RoomDatabase() {
    abstract fun getCartDao(): CartDao
    abstract fun getALlAddressDao(): AddressDao
}