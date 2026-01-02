package com.example.easymart.data.local.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface WalletDao {
    @Query("SELECT balance from wallets where userId = :userId")
    suspend fun getBalance(userId: Int): Long?
    @Query("UPDATE wallets SET balance = :newBalance WHERE userId = :userId")
    suspend fun updateBalance(userId: Int, newBalance: Long)
}