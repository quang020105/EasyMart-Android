package com.example.easymart.data.repositoryimpl

import com.example.easymart.data.local.datasource.ProductLocalDataSource
import com.example.easymart.data.mapper.toDomain
import com.example.easymart.data.mapper.toEntity
import com.example.easymart.data.remote.datasource.ProductRemoteDataSource
import com.example.easymart.domain.model.Product
import com.example.easymart.domain.repository.ProductRepository
import com.example.easymart.presentation.common.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.example.easymart.data.local.entity.ProductEntity

class ProductRepositoryImpl @Inject constructor(
    private val localDS: ProductLocalDataSource,
    private val remoteDS: ProductRemoteDataSource
): ProductRepository {
    override fun getAllProduct(): Flow<Resource<List<Product>>> {
        val source: Flow<List<ProductEntity>> = localDS.observeProducts()
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
            val res = remoteDS.getAllProducts()
            if (res.isSuccessful) {
                val data = res.body().orEmpty()
                localDS.upsertAll(data.map { it.toEntity() })
                Resource.Success(Unit)
            } else {
                Resource.Error("Gặp vấn đề trong quá trình tải sản phẩm")
            }
        } catch (e: Exception) {
            Resource.Error("Lỗi " + e.message)
        }
    }

    override suspend fun refreshProductById(productId: Int): Resource<Unit> {
        return try {
            val res = remoteDS.getProductById(productId)
            if (res.isSuccessful) {
                val data = res.body()
                if (data != null) {
                    localDS.upsert(data.toEntity())
                    Resource.Success(Unit)
                } else {
                    Resource.Error("Không có dữ liệu sản phẩm")
                }
            } else {
                Resource.Error("Có lỗi xảy ra")
            }
        } catch (e: Exception) {
            Resource.Error("Lỗi " + e.message)
        }
    }
}