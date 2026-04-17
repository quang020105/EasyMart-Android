package com.example.easymart.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.easymart.data.local.entity.AddressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressDao {
    @Query("SELECT * FROM address WHERE isDeleted = 0 ORDER BY isDefault DESC, id DESC")
    fun getALlAddress(): Flow<List<AddressEntity>>

    @Query("SELECT * FROM address WHERE userUid = :userUid AND isDeleted = 0 ORDER BY isDefault DESC, id DESC")
    fun getAllAddressByUser(userUid: String): Flow<List<AddressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: AddressEntity): Long

    @Update
    suspend fun updateAddress(address: AddressEntity)

    @Query("SELECT * FROM address WHERE id = :id")
    suspend fun getAddressById(id: Int): AddressEntity?

    @Query("UPDATE address SET isDeleted = 1, isSynced = 0, updatedAt = :updatedAt WHERE id = :id")
    suspend fun softDeleteAddress(id: Int, updatedAt: Long)

    @Query("DELETE FROM address WHERE id = :id")
    suspend fun deleteAddress(id: Int)

    @Query("UPDATE address SET isDefault = CASE WHEN id =:id THEN 1 ELSE 0 END, updatedAt = :updatedAt, isSynced = 0 WHERE (userUid = :userUid OR (:userUid IS NULL AND userUid IS NULL))")
    suspend fun setDefaultAddress(id: Int, userUid: String?, updatedAt: Long)

    @Query("SELECT * FROM address WHERE isDefault = 1 AND isDeleted = 0 LIMIT 1")
    suspend fun getDefaultAddress(): AddressEntity?

    @Query("SELECT * FROM address WHERE userUid = :userUid AND isDefault = 1 AND isDeleted = 0 LIMIT 1")
    suspend fun getDefaultAddressByUser(userUid: String): AddressEntity?

    @Query("SELECT * FROM address WHERE isSynced = 0")
    suspend fun getUnsyncedAddresses(): List<AddressEntity>

    @Query("SELECT * FROM address WHERE userUid = :userUid")
    suspend fun getAllAddressIncludingDeletedByUser(userUid: String): List<AddressEntity>

    @Query("UPDATE address SET isSynced = 1 WHERE id = :id")
    suspend fun markAddressSynced(id: Int)

    @Query("UPDATE address SET isDeleted = 0, isSynced = 1 WHERE id = :id")
    suspend fun restoreAddressSynced(id: Int)
}