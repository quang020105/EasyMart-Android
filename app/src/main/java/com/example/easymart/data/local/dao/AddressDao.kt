package com.example.easymart.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.easymart.data.local.entity.AddressEntity
import com.example.easymart.domain.model.Address
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressDao {
    @Query("SELECT * FROM address ORDER BY isDefault DESC, id DESC")
    fun getALlAddress(): Flow<List<AddressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: AddressEntity): Long

    @Update
    suspend fun updateAddress(address: AddressEntity)

    @Query("SELECT * FROM address WHERE id = :id")
    suspend fun getAddressById(id: Int): AddressEntity?

    @Query("DELETE FROM address WHERE id = :id")
    suspend fun deleteAddress(id: Int)

    @Query("UPDATE address SET isDefault = CASE WHEN id =:id THEN 1 ELSE 0 END")
    suspend fun setDefaultAddress(id: Int)
}