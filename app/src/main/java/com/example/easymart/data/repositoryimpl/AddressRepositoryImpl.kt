package com.example.easymart.data.repositoryimpl

import com.example.easymart.data.local.dao.AddressDao
import com.example.easymart.data.mapper.toDomain
import com.example.easymart.data.mapper.toEntity
import com.example.easymart.domain.model.Address
import com.example.easymart.domain.repository.AddressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(
    private val addressDao: AddressDao
) : AddressRepository {
    override fun getAllAddresses(): Flow<List<Address>> {
        return addressDao.getALlAddress()
            .map { addressEntities -> addressEntities.map { it.toDomain() } }
    }

    override suspend fun insertAddress(address: Address): Int {
        return addressDao.insertAddress(address.toEntity()).toInt()
    }

    override suspend fun updateAddress(address: Address) {
        addressDao.updateAddress(address.toEntity())
    }

    override suspend fun setDefaultAddress(addressId: Int) {
        addressDao.setDefaultAddress(addressId)
    }

    override suspend fun deleteAddress(addressId: Int) {
        addressDao.deleteAddress(addressId)
    }

    override suspend fun getAddressById(addressId: Int): Address? {
        return addressDao.getAddressById(addressId)?.let {
            return it.toDomain()
        }
    }

    override suspend fun getDefaultAddress(): Address? {
        return addressDao.getDefaultAddress()?.toDomain()
    }
}