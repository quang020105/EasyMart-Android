package com.example.easymart.data.repositoryimpl

import android.util.Log
import com.example.easymart.data.local.datasource.ProductLocalDataSource
import com.example.easymart.data.mapper.toDomain
import com.example.easymart.data.mapper.toEntity
import com.example.easymart.data.mapper.toRemoteDto
import com.example.easymart.data.mapper.toEntity as remoteToEntity
import com.example.easymart.data.remote.datasource_impl.RetrofitProductRemoteDataSource
import com.example.easymart.domain.model.Product
import com.example.easymart.domain.repository.ProductRepository
import com.example.easymart.presentation.common.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.example.easymart.data.local.entity.ProductEntity
import com.example.easymart.data.remote.datasource_impl.FirestoreProductRemoteDataSource

class ProductRepositoryImpl @Inject constructor(
    private val localDS: ProductLocalDataSource,
    private val remoteDS: RetrofitProductRemoteDataSource,
    private val firestoreDS: FirestoreProductRemoteDataSource
): ProductRepository {
    override fun getAllProduct(): Flow<Resource<List<Product>>> {
        val source: Flow<List<ProductEntity>> = localDS.observeActiveProducts()
        val mapped: Flow<Resource<List<Product>>> = source.map { items ->
            Resource.Success(items.map { it.toDomain() })
        }
        return mapped.catch { e ->
            emit(Resource.Error("Lỗi " + e.message))
        }
    }

    override suspend fun addToCart(
        product: Product,
        quantity: Int
    ) {
        //todo
    }

    override fun getProductById(productId: Int): Flow<Resource<Product>> {
        val source: Flow<ProductEntity?> = localDS.observeProductById(productId)
        val mapped: Flow<Resource<Product>> = source.map { entity ->
            if (entity != null) {
                Resource.Success(entity.toDomain())
            } else {
                Resource.Error("Không tìm thấy sản phẩm")
            }
        }
        return mapped.catch { e ->
            emit(Resource.Error("Lỗi " + e.message))
        }
    }

    override suspend fun refreshProducts(): Resource<Unit> {
        return try {
            val products = remoteDS.getAllProducts()
            localDS.upsertAll(products.map { it.toEntity() })
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Lỗi " + e.message)
        }
    }

    override suspend fun refreshProductById(productId: Int): Resource<Unit> {
        return try {
            val product = remoteDS.getProductById(productId)
            if(product != null){
                localDS.upsert(product.toEntity())
                Resource.Success(Unit)
            } else {
                Resource.Error("Không tìm thấy sản phẩm")
            }
        } catch (e: Exception) {
            Resource.Error("Lỗi " + e.message)
        }
    }

    override suspend fun upsertProduct(product: Product): Resource<Unit> {
        return try {
            val now = System.currentTimeMillis()
            val entity = product.toEntity().copy(
                updatedAt = now,
                isDeleted = false,
                isSynced = false
            )
            Log.d("ProductRepositoryImpl", "upsertProduct: $entity")
            localDS.upsert(entity)
            syncProduct(entity)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Lỗi " + e.message)
        }
    }

    override suspend fun syncProducts(): Resource<Unit> {
        return try {
            // 1) Push local unsynced
            val unsynced = localDS.getUnsynced()
            unsynced.forEach { local ->
                syncProduct(local)
            }

            // 2) Pull remote and resolve conflict by updatedAt
            val remote = firestoreDS.getProductsOnce()
            val localAll = localDS.getAllOnce()
            val localById = localAll.associateBy { it.id }
            val remoteById = remote.associateBy { it.id }

            remote.forEach { remoteItem ->
                val local = localById[remoteItem.id]
                if (local == null) {
                    localDS.upsert(remoteItem.remoteToEntity())
                } else {
                    if (!local.isSynced && local.updatedAt > remoteItem.updatedAt) {
                        return@forEach
                    }
                    localDS.upsert(remoteItem.remoteToEntity())
                }
            }

            // 3) Xoa local neu remote khong con va local da synced
            localAll.forEach { local ->
                if (remoteById[local.id] == null && local.isSynced && local.storagePath != null) {
                    localDS.upsert(local.copy(isDeleted = true, updatedAt = System.currentTimeMillis()))
                }
            }

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Lỗi " + e.message)
        }
    }

    override suspend fun updateVisibilityLocalOnly(productId: Int, isVisible: Boolean): Resource<Unit> {
        return try {
            localDS.updateVisibility(productId, isVisible, System.currentTimeMillis())
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Lỗi " + e.message)
        }
    }

    private suspend fun syncProduct(local: ProductEntity) {
        val storagePath = local.storagePath ?: "products/${local.id}/main.jpg"
        val imageUrl = if (!local.localImageUri.isNullOrBlank()) {
            firestoreDS.uploadImage(local.localImageUri, storagePath)
        } else {
            local.imageUrl
        }
        val now = System.currentTimeMillis()
        val updated = local.copy(
            imageUrl = imageUrl,
            storagePath = storagePath,
            updatedAt = now
        )
        firestoreDS.upsertProduct(updated.toRemoteDto())
        localDS.markSynced(local.id, now, imageUrl, storagePath)
    }
}