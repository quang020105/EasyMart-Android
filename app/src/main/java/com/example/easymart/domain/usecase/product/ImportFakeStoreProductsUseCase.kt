package com.example.easymart.domain.usecase.product

import com.example.easymart.domain.repository.ProductRepository
import com.example.easymart.presentation.common.Resource
import javax.inject.Inject

class ImportFakeStoreProductsUseCase @Inject constructor(
    private val repo: ProductRepository
) {
    suspend operator fun invoke(): Resource<Int> =
        repo.importFakeStoreProductsToFirestore()
}
