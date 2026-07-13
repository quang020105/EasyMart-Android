package com.example.easymart.data.remote.datasource_impl

import com.example.easymart.data.remote.datasource.OrderRemoteDataSource
import com.example.easymart.data.remote.dto.OrderRemoteDto
import com.example.easymart.data.remote.dto.OrderRemoteItemDto
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.collections.get

class FirestoreOrderRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) : OrderRemoteDataSource {

    private fun ordersRef() =
        firestore.collection("orders")

    override suspend fun upsertOrder(userId: String, order: OrderRemoteDto): String {
        val documentId = order.remoteId ?: throw IllegalStateException("remoteId must be generated before syncing order")
        ordersRef()
            .document(documentId)
            .set(order.copy(remoteId = documentId), SetOptions.merge())
            .await()
        return documentId
    }

    override suspend fun getOrdersOnce(userId: String): List<OrderRemoteDto> {
        val snapshot = ordersRef()
            .whereEqualTo("userId", userId)
            .get()
            .await()
        return snapshot.documents.map { it.toOrderRemoteDto() }
    }

    override fun observeOrders(userId: String): Flow<List<OrderRemoteDto>> = callbackFlow {
        val listener = ordersRef()
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.documents.orEmpty().map { it.toOrderRemoteDto() })
            }

        awaitClose { listener.remove() }
    }

    override fun observeAllOrders(): Flow<List<OrderRemoteDto>> = callbackFlow {
        val listener = ordersRef()
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.documents.orEmpty().map { it.toOrderRemoteDto() })
            }

        awaitClose { listener.remove() }
    }

    override suspend fun getOrderByRemoteId(remoteId: String): OrderRemoteDto? {
        val doc = ordersRef().document(remoteId).get().await()
        return if (doc.exists()) doc.toOrderRemoteDto() else null
    }

    override suspend fun updateOrderStatus(
        remoteId: String,
        orderStatus: String,
        paymentStatus: String?,
        updatedAt: Long
    ) {
        val updates = mutableMapOf<String, Any>(
            "orderStatus" to orderStatus,
            "updatedAt" to updatedAt
        )
        if (paymentStatus != null) {
            updates["paymentStatus"] = paymentStatus
        }
        ordersRef().document(remoteId).update(updates).await()
    }

    private fun DocumentSnapshot.toOrderRemoteDto(): OrderRemoteDto {
        val dto = toObject(OrderRemoteDto::class.java)
        if (dto != null) {
            return dto.copy(remoteId = dto.remoteId ?: id)
        }

        return OrderRemoteDto(
            localId = getLong("localId")?.toInt() ?: 0,
            remoteId = getString("remoteId") ?: id,
            userId = getString("userId").orEmpty(),
            orderNumber = getString("orderNumber").orEmpty(),
            items = (get("items") as? List<*>)
                ?.mapNotNull { item -> item as? Map<*, *> }
                ?.map { item ->
                    OrderRemoteItemDto(
                        productId = (item["productId"] as? Number)?.toInt() ?: 0,
                        productName = item["productName"] as? String ?: "",
                        productImage = item["productImage"] as? String ?: "",
                        price = (item["price"] as? Number)?.toDouble() ?: 0.0,
                        quantity = (item["quantity"] as? Number)?.toInt() ?: 0
                    )
                }
                ?: emptyList(),
            totalAmount = getLong("totalAmount") ?: 0L,
            orderStatus = getString("orderStatus").orEmpty(),
            paymentStatus = getString("paymentStatus").orEmpty(),
            paymentMethod = getString("paymentMethod").orEmpty(),
            shippingName = getString("shippingName").orEmpty(),
            shippingPhone = getString("shippingPhone").orEmpty(),
            shippingAddressString = getString("shippingAddressString").orEmpty(),
            createdAt = getLong("createdAt") ?: 0L,
            updatedAt = getLong("updatedAt") ?: 0L,
            stockDeducted = getBoolean("stockDeducted") ?: false
        )
    }
}
