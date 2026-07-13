package com.example.easymart.data.repositoryimpl

import android.util.Log
import com.example.easymart.data.local.datasource.ProductLocalDataSource
import com.example.easymart.data.local.entity.ProductEntity
import com.example.easymart.data.mapper.decodeList
import com.example.easymart.data.mapper.encodeList
import com.example.easymart.data.mapper.toDomain
import com.example.easymart.data.mapper.toEntity
import com.example.easymart.data.mapper.toRemoteDto
import com.example.easymart.data.mapper.toEntity as remoteToEntity
import com.example.easymart.data.remote.datasource_impl.FirestoreProductRemoteDataSource
import com.example.easymart.data.remote.datasource_impl.RetrofitProductRemoteDataSource
import com.example.easymart.domain.model.Product
import com.example.easymart.domain.repository.ProductRepository
import com.example.easymart.presentation.common.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val localDS: ProductLocalDataSource,
    private val remoteDS: RetrofitProductRemoteDataSource,
    private val firestoreDS: FirestoreProductRemoteDataSource
) : ProductRepository {
    private companion object {
        const val FAKESTORE_DEFAULT_STOCK = 100
        const val FAKESTORE_BRAND = ""
    }

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

    // thay thế tất cả các sản phẩm trong local bằng dữ liệu từ retrofit và firestore
    override suspend fun refreshProducts(): Resource<Unit> {
        val apiResult = runCatching {
            remoteDS.getAllProducts().map { it.toEntity() }
        }
        val firestoreResult = runCatching {
            firestoreDS.getProductsOnce().map { it.remoteToEntity() }
        }

        val products = buildList {
            apiResult.getOrNull()?.let(::addAll)
            firestoreResult.getOrNull()?.let(::addAll)
        }
            .associateBy { it.id }
            .values
            .toList()

        if (products.isNotEmpty()) {
            localDS.upsertAll(products)
            return Resource.Success(Unit)
        }

        return Resource.Error("Không tìm thấy sản phẩm")
    }

    // thay thế sản phẩm trong local bằng dữ liệu mới nhất từ retrofit hoặc firestore
    override suspend fun refreshProductById(productId: Int): Resource<Unit> {
        val firestoreResult = runCatching { firestoreDS.getProductById(productId) }
        firestoreResult.getOrNull()?.let { product ->
            localDS.upsert(product.toEntity())
            return Resource.Success(Unit)
        }

        val apiResult = runCatching { remoteDS.getProductById(productId) }
        apiResult.getOrNull()?.let { product ->
            localDS.upsert(product.toEntity())
            return Resource.Success(Unit)
        }

        return Resource.Error("Không tìm thấy sản phẩm")
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
            Log.e("ProductRepositoryImpl", "upsertProduct error", e)
            Resource.Error("Lỗi " + e.message)
        }
    }

    override suspend fun importFakeStoreProductsToFirestore(): Resource<Int> {
        return try {
            val now = System.currentTimeMillis()
            val remoteExistingIds = firestoreDS.getProductsOnce()
                .map { it.id }
                .toSet()

            val importedEntities = remoteDS.getAllProducts()
                .filter { product -> product.id !in remoteExistingIds }
                .map { product ->
                    product.copy(
                        brand = FAKESTORE_BRAND,
                        stockQuantity = FAKESTORE_DEFAULT_STOCK,
                        isVisible = true,
                        createdAt = now,
                        updatedAt = now,
                        isDeleted = false,
                        isSynced = false,
                        storagePath = "products/${product.id}",
                        localImageUri = null,
                        localImageUris = emptyList()
                    ).toEntity().copy(
                        isSynced = false,
                        localImageUri = null,
                        localImageUrisJson = "[]"
                    )
                }

            importedEntities.forEach { entity ->
                localDS.upsert(entity)
                syncProduct(entity)
            }

            Resource.Success(importedEntities.size)
        } catch (e: Exception) {
            Resource.Error("Lá»—i " + e.message)
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
                    Log.d("ProductRepositoryImpl", "syncProducts: Thêm mới từ remote ${remoteItem.id}")
                    localDS.upsert(remoteItem.remoteToEntity())
                } else {
                    if (!local.isSynced && local.updatedAt >= remoteItem.updatedAt) {
                        return@forEach
                    }
                    localDS.upsert(remoteItem.remoteToEntity())
                    Log.d("ProductRepositoryImpl", "syncProducts: Cập nhật từ remote ${remoteItem}")
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
        val now = System.currentTimeMillis()
        val basePath = local.storagePath ?: "products/${local.id}"

        val localSecondaryUris = decodeList(local.localImageUrisJson)
        val existingRemoteSecondary = decodeList(local.imageUrlsJson)
            .filter { it.isNotBlank() }
            .filter { it.startsWith("http", ignoreCase = true) }

        val existingRemoteMain = local.imageUrl
            .takeIf { it.isNotBlank() && it.startsWith("http", ignoreCase = true) }

        val uploadedMainUrl = if (!local.localImageUri.isNullOrBlank()) {
            val storagePath = "${basePath}/main.jpg"
            firestoreDS.uploadImage(local.localImageUri, storagePath)
        } else {
            null
        }

        val uploadedSecondaryUrls = if (localSecondaryUris.isNotEmpty()) {
            localSecondaryUris.mapIndexed { index, uri ->
                val storagePath = "${basePath}/secondary_$index.jpg"
                firestoreDS.uploadImage(uri, storagePath)
            }
        } else {
            emptyList()
        }

        val mergedSecondaryUrls = (existingRemoteSecondary + uploadedSecondaryUrls)
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .distinct()

        val mainImageUrl = uploadedMainUrl ?: existingRemoteMain.orEmpty()

        val updated = local.copy(
            imageUrl = mainImageUrl,
            imageUrlsJson = encodeList(mergedSecondaryUrls),
            storagePath = basePath,
            updatedAt = now
        )

        firestoreDS.upsertProduct(updated.toRemoteDto())
        localDS.markSynced(local.id, now, mainImageUrl, encodeList(mergedSecondaryUrls), basePath)
    }
}
