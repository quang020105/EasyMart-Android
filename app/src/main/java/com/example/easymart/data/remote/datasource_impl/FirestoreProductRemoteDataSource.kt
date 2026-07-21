package com.example.easymart.data.remote.datasource_impl

import android.util.Log
import androidx.core.net.toUri
import com.example.easymart.data.mapper.toDomain
import com.example.easymart.data.remote.datasource.ProductRemoteDataSource
import com.example.easymart.data.remote.dto.ProductFirestoreDto
import com.example.easymart.domain.model.Product
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreProductRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
): ProductRemoteDataSource {
    private fun productRef() = firestore.collection("products")

    private fun mapDocToProduct(doc: DocumentSnapshot): ProductFirestoreDto {
        return ProductFirestoreDto(
            id = doc.getLong("id")?.toInt() ?: doc.id.toIntOrNull() ?: 0,
            name = doc.getString("name") ?: "",
            description = doc.getString("description"),
            price = (doc.get("price") as? Number)?.toDouble() ?: 0.0,
            priceVnd = (doc.get("priceVnd") as? Number)?.toLong() ?: 0L,
            currency = doc.getString("currency") ?: "VND",
            moneySchemaVersion = (doc.get("moneySchemaVersion") as? Number)?.toInt() ?: 1,
            imageUrl = doc.getString("imageUrl") ?: "",
            imageUrls = (doc.get("imageUrls") as? List<*>)
                ?.mapNotNull { it as? String }
                ?: emptyList(),
            brand = doc.getString("brand") ?: "",
            category = doc.getString("category") ?: "",
            ratingRate = doc.getDouble("ratingRate") ?: 0.0,
            ratingCount = doc.getLong("ratingCount")?.toInt() ?: 0,
            stockQuantity = doc.getLong("stockQuantity")?.toInt() ?: 0,
            soldQuantity = doc.getLong("soldQuantity")?.toInt() ?: 0,
            isVisible = doc.getBoolean("isVisible") ?: true,
            createdAt = doc.getLong("createdAt") ?: 0L,
            updatedAt = doc.getLong("updatedAt") ?: 0L,
            isDeleted = doc.getBoolean("isDeleted") ?: false,
            storagePath = doc.getString("storagePath")
        )
    }

    suspend fun upsertProduct(product: ProductFirestoreDto) {
        productRef()
            .document(product.id.toString())
            .set(product, SetOptions.merge())
            .await()
    }

    suspend fun deductStock(items: Map<Int, Int>) {
        if (items.isEmpty()) return

        val now = System.currentTimeMillis()
        firestore.runTransaction { transaction ->
            val updates = mutableListOf<() -> Unit>()

            items.forEach { (productId, quantity) ->
                require(quantity > 0) { "Invalid quantity for product $productId" }

                val ref = productRef().document(productId.toString())
                val snapshot = transaction.get(ref)
                check(snapshot.exists()) { "Product $productId does not exist" }

                val currentStock = snapshot.getLong("stockQuantity")?.toInt() ?: 0
                val currentSold = snapshot.getLong("soldQuantity")?.toInt() ?: 0
                check(currentStock >= quantity) { "Product $productId does not have enough stock" }

                updates += {
                    transaction.update(
                        ref,
                        mapOf(
                            "stockQuantity" to currentStock - quantity,
                            "soldQuantity" to currentSold + quantity,
                            "updatedAt" to now
                        )
                    )
                }
            }

            updates.forEach { update -> update() }
            null
        }.await()
    }

    suspend fun getProductsOnce(): List<ProductFirestoreDto> {
        val snapshot = productRef().get().await()
        return snapshot.documents.map { mapDocToProduct(it) }
    }

    fun observeProducts(): Flow<List<Product>> = callbackFlow {
        val listener = productRef().addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            trySend(snapshot?.documents.orEmpty().map { document ->
                mapDocToProduct(document).toDomain()
            })
        }

        awaitClose { listener.remove() }
    }

    suspend fun uploadImage(localUri: String, storagePath: String): String {
        val ref = storage.reference.child(storagePath)
        ref.putFile(localUri.toUri()).await()
        Log.d("FirestoreDataSource", "localUri: $localUri , storagePath: $storagePath")
        return ref.downloadUrl.await().toString()
    }

    override suspend fun getAllProducts(): List<Product> {
        val snapshot = productRef().get().await()
        return snapshot.documents.map { mapDocToProduct(it).toDomain() }
    }

    override suspend fun getProductById(productId: Int): Product? {
        val doc = productRef().document(productId.toString()).get().await()
        return if (doc.exists()) {
            mapDocToProduct(doc).toDomain()
        } else {
            null
        }
    }
}
