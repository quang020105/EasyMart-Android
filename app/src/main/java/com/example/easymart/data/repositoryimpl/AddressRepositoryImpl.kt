package com.example.easymart.data.repositoryimpl

import com.example.easymart.data.local.dao.AddressDao
import com.example.easymart.data.mapper.toDomain
import com.example.easymart.data.mapper.toEntity
import com.example.easymart.data.mapper.toRemoteDto
import com.example.easymart.data.remote.datasource.AddressRemoteDataSource
import com.example.easymart.domain.model.Address
import com.example.easymart.domain.repository.AddressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(
    private val addressDao: AddressDao,
    private val remoteDS: AddressRemoteDataSource
) : AddressRepository {
    override fun getAllAddresses(): Flow<List<Address>> {
        return addressDao.getALlAddress()
            .map { addressEntities -> addressEntities.map { it.toDomain() } }
    }

    override fun getAllAddressesByUser(userUid: String): Flow<List<Address>> {
        return addressDao.getAllAddressByUser(userUid)
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun insertAddress(address: Address): Int {
        val now = System.currentTimeMillis()
        val entity = address.toEntity().copy(
            isDeleted = false,
            isSynced = false,
            createdAt = now,
            updatedAt = now
        )
        return addressDao.insertAddress(entity).toInt()
    }

    override suspend fun updateAddress(address: Address) {
        val now = System.currentTimeMillis()
        addressDao.updateAddress(address.toEntity().copy(
            isSynced = false,
            updatedAt = now
        ))
    }

    override suspend fun setDefaultAddress(addressId: Int) {
        val address = addressDao.getAddressById(addressId) ?: return
        val uid = address.userUid
        addressDao.setDefaultAddress(addressId, uid, System.currentTimeMillis())
    }

    override suspend fun deleteAddress(addressId: Int) {
        addressDao.softDeleteAddress(addressId, System.currentTimeMillis())
    }

    override suspend fun getAddressById(addressId: Int): Address? {
        return addressDao.getAddressById(addressId)?.toDomain()
    }

    override suspend fun getDefaultAddress(): Address? {
        return addressDao.getDefaultAddress()?.toDomain()
    }

    override suspend fun getDefaultAddressByUser(userUid: String): Address? {
        return addressDao.getDefaultAddressByUser(userUid)?.toDomain()
    }

    override suspend fun syncAddresses(userUid: String) {
        // 1) Đẩy local chưa sync lên remote
        val unSynced = addressDao.getUnsyncedAddresses().filter { it.userUid == userUid }
        unSynced.forEach { local ->
            runCatching {
                if (local.isDeleted) {
                    remoteDS.deleteAddress(userUid, local.id)
                    addressDao.deleteAddress(local.id)
                } else {
                    remoteDS.upsertAddress(userUid, local.toRemoteDto())
                    addressDao.markAddressSynced(local.id)
                }
            }
        }

        // 2) Kéo remote về local + xử lý conflict theo updatedAt
        val remote = remoteDS.getAddressesOnce(userUid)
        val localAll = addressDao.getAllAddressIncludingDeletedByUser(userUid)
        val localById = localAll.associateBy { it.id }
        val remoteById = remote.associateBy { it.id }

        remote.forEach { remoteItem ->
            val local = localById[remoteItem.id]
            if (local == null) {
                addressDao.insertAddress(remoteItem.toEntity().copy(userUid = userUid, isSynced = true))
            } else {
                if (!local.isSynced && local.updatedAt > remoteItem.updatedAt) {
                    // local mới hơn, ưu tiên local
                    return@forEach
                }
                // remote mới hơn hoặc local đã synced
                addressDao.insertAddress(remoteItem.toEntity().copy(userUid = userUid, isSynced = true))
            }
        }

        // 3) Xóa local nếu remote không còn và local đã synced
        localAll.forEach { local ->
            val remoteItem = remoteById[local.id]
            if (remoteItem == null && local.isSynced) {
                addressDao.deleteAddress(local.id)
            }
        }
    }
}