package com.example.easymart.data.remote.datasource

import com.example.easymart.data.remote.dto.OrderRemoteDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreOrderRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) : OrderRemoteDataSource {

    private fun ordersRef(userId: String) =
        firestore.collection("orders")

    override suspend fun upsertOrder(userId: String, order: OrderRemoteDto): String {
        val documentId = order.remoteId ?: throw IllegalStateException("remoteId must be generated before syncing order")
        ordersRef(userId)
            .document(documentId)
            .set(order.copy(remoteId = documentId), SetOptions.merge())
            .await()
        return documentId
    }
}
