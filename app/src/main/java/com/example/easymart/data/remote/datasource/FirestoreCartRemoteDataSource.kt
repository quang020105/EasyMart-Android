package com.example.easymart.data.remote.datasource

import com.example.easymart.data.remote.dto.CartItemRemoteDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreCartRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) : CartRemoteDataSource {

    private fun cartItemsRef(userId: String) =
        firestore.collection("usersEM").document(userId).collection("cartItems")

    override fun observeCartItems(userId: String): Flow<List<CartItemRemoteDto>> = callbackFlow {
        val registration: ListenerRegistration = cartItemsRef(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val items = snapshot?.toObjects(CartItemRemoteDto::class.java) ?: emptyList()
                trySend(items).isSuccess
            }
        awaitClose { registration.remove() }
    }

    //đẩy sản phẩm lên firestore, nếu đã có thì merge để cập nhật số lượng
    override suspend fun upsertCartItem(userId: String, item: CartItemRemoteDto) {
        cartItemsRef(userId)
            .document(item.productId.toString())
            .set(item, SetOptions.merge())
            .await()
    }

    override suspend fun deleteCartItem(userId: String, productId: Int) {
        cartItemsRef(userId)
            .document(productId.toString())
            .delete()
            .await()
    }

    //lấy danh sách sản phẩm
    override suspend fun getCartItemsOnce(userId: String): List<CartItemRemoteDto> {
        val snapshot = cartItemsRef(userId).get().await()
        return snapshot.toObjects(CartItemRemoteDto::class.java)
    }

    // thay thế toàn bộ sản phẩm trong giỏ hàng bằng một danh sách mới
    override suspend fun replaceAllCartItems(userId: String, items: List<CartItemRemoteDto>) {
        val batch = firestore.batch()
        val ref = cartItemsRef(userId)
        // Xóa tất cả doc hiện có
        val current = ref.get().await()
        current.documents.forEach { batch.delete(it.reference) }
        // Ghi mới
        items.forEach { item ->
            val doc = ref.document(item.productId.toString())
            batch.set(doc, item)
        }
        batch.commit().await()
    }

    //xóa toàn b sản phẩm trong giỏ hàng
    override suspend fun clearCart(userId: String) {
        val batch = firestore.batch()
        val ref = cartItemsRef(userId)
        val current = ref.get().await()
        current.documents.forEach { batch.delete(it.reference) }
        batch.commit().await()
    }
}

