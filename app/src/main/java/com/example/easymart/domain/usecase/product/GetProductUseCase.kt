package com.example.easymart.domain.usecase.product

import com.example.easymart.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductUseCase @Inject constructor(
    private val productRepo: ProductRepository
) {
    operator fun invoke(productId: Int) = productRepo.getProductById(productId)
}