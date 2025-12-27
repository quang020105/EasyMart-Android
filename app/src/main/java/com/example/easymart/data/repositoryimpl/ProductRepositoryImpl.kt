package com.example.easymart.data.repositoryimpl

import android.util.Log
import com.example.easymart.data.remote.api.ProductApi
import com.example.easymart.data.mapper.toDomain
import com.example.easymart.domain.model.Product
import com.example.easymart.domain.repository.ProductRepository
import com.example.easymart.presentation.common.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApi
): ProductRepository{
    override fun getAllProduct(): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading)
        try {
            val res = api.getAllProduct()
            if(res.isSuccessful){
                val data = res.body()
                data?.let {
                    val products = it.map { p -> p.toDomain() }
                    emit(Resource.Success(products))
                }
            } else{
                emit(Resource.Error("Gặp vấn đề trong quá trình tải sản phẩm"))
            }
        } catch (e: Exception){
            emit(Resource.Error("Lỗi " + e.message))
        }
    }

    override suspend fun addToCart(
        product: Product,
        quantity: Int
    ) {
        //todo
    }

    override fun getProductById(productId: Int): Flow<Resource<Product>> = flow {
        emit(Resource.Loading)
        try{
            val res = api.getProductById(productId)
            if(res.isSuccessful){
                val data = res.body()
                Log.d("Detail", "uiState: $data")
                data?.let {
                    val product = it.toDomain()
                    emit(Resource.Success(product))
                }
            } else {
                Log.d("Detail", "Không thành công")
                emit(Resource.Error("Có lỗi xảy ra"))
            }
        } catch (e: Exception){
            emit(Resource.Error("Lỗi " + e.message))
            Log.d("Detail", "Lỗi: " + e.message)
        }
        Log.d("Detail", "ID$productId")
    }
}