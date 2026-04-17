package com.example.easymart.data.remote.datasource

import com.example.easymart.data.remote.dto.AddressRemoteDto

interface AddressRemoteDataSource {
    suspend fun upsertAddress(userUid: String, address: AddressRemoteDto)
    suspend fun deleteAddress(userUid: String, addressId: Int)
    suspend fun getAddressesOnce(userUid: String): List<AddressRemoteDto>
    suspend fun replaceAllAddresses(userUid: String, addresses: List<AddressRemoteDto>)
}

