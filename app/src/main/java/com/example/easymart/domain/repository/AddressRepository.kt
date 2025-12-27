package com.example.easymart.domain.repository

import com.example.easymart.domain.model.Address
import kotlinx.coroutines.flow.Flow

interface AddressRepository {
    fun getAllAddresses(): Flow<List<Address>>
    suspend fun insertAddress(address: Address): Int
    suspend fun updateAddress(address: Address)
    suspend fun setDefaultAddress(addressId: Int)
    suspend fun deleteAddress(addressId: Int)
    suspend fun getAddressById(addressId: Int): Address?
}