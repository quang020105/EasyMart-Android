package com.example.easymart.domain.usecase.product

import com.example.easymart.domain.repository.ProductRepository
import com.example.easymart.presentation.common.Resource
import javax.inject.Inject

class SyncProductsUseCase @Inject constructor(
    private val repo: ProductRepository
) {
    suspend fun sync(): Resource<Unit> = repo.syncProducts()
}

