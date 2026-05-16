package com.example.easymart.domain.usecase.product

import com.example.easymart.domain.repository.ProductRepository
import com.example.easymart.presentation.common.Resource
import javax.inject.Inject

class UpdateProductVisibilityLocalOnlyUseCase @Inject constructor(
    private val repo: ProductRepository
) {
    suspend fun update(productId: Int, isVisible: Boolean): Resource<Unit> {
        return repo.updateVisibilityLocalOnly(productId, isVisible)
    }
}

