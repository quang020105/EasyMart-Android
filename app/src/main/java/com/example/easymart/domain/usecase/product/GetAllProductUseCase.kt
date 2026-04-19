package com.example.easymart.domain.usecase.product

import com.example.easymart.domain.model.Product
import com.example.easymart.domain.repository.ProductRepository
import com.example.easymart.presentation.common.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllProductUseCase @Inject constructor(
    private val repo: ProductRepository
) {
    //operator fun invoke : tạo đối tượng sẽ gọi trực tiếp hàm này
    operator fun invoke(): Flow<Resource<List<Product>>> = repo.getAllProduct()

    suspend fun refresh(): Resource<Unit> = repo.refreshProducts()
}