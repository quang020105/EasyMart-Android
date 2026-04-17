package com.example.easymart.data.remote.datasource

import com.example.easymart.data.remote.dto.AddressRemoteDto
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreAddressRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) : AddressRemoteDataSource {

    private fun addressRef(userUid: String) =
        firestore.collection("usersEM").document(userUid).collection("addresses")

    private fun mapDocToAddress(doc: DocumentSnapshot): AddressRemoteDto {
        val id = doc.getLong("id")?.toInt() ?: doc.id.toIntOrNull() ?: 0
        val defaultRaw = doc.get("isDefault") ?: doc.get("default") ?: doc.get("is_default")
        val isDefault = when (defaultRaw) {
            is Boolean -> defaultRaw
            is Number -> defaultRaw.toInt() == 1
            is String -> defaultRaw.equals("true", ignoreCase = true) || defaultRaw == "1"
            else -> false
        }
        return AddressRemoteDto(
            id = id,
            userUid = doc.getString("userUid"),
            name = doc.getString("name") ?: "",
            phone = doc.getString("phone") ?: "",
            detailAddress = doc.getString("detailAddress") ?: "",
            addressString = doc.getString("addressString") ?: "",
            isDefault = isDefault,
            provinceCode = doc.getLong("provinceCode")?.toInt() ?: 0,
            districtCode = doc.getLong("districtCode")?.toInt() ?: 0,
            wardCode = doc.getLong("wardCode")?.toInt() ?: 0,
            isDeleted = doc.getBoolean("isDeleted") ?: false,
            updatedAt = doc.getLong("updatedAt") ?: 0L,
            createdAt = doc.getLong("createdAt") ?: 0L
        )
    }

    override suspend fun upsertAddress(userUid: String, address: AddressRemoteDto) {
        addressRef(userUid)
            .document(address.id.toString())
            .set(address, SetOptions.merge())
            .await()
    }

    override suspend fun deleteAddress(userUid: String, addressId: Int) {
        addressRef(userUid)
            .document(addressId.toString())
            .delete()
            .await()
    }

    override suspend fun getAddressesOnce(userUid: String): List<AddressRemoteDto> {
        val snapshot = addressRef(userUid).get().await()
        return snapshot.documents.map { mapDocToAddress(it) }
    }

    override suspend fun replaceAllAddresses(userUid: String, addresses: List<AddressRemoteDto>) {
        val batch = firestore.batch()
        val ref = addressRef(userUid)
        val current = ref.get().await()
        current.documents.forEach { batch.delete(it.reference) }
        addresses.forEach { address ->
            val doc = ref.document(address.id.toString())
            batch.set(doc, address)
        }
        batch.commit().await()
    }
}
