package com.example.easymart.domain.usecase.product

import com.example.easymart.domain.model.Product
import com.example.easymart.domain.repository.ProductRepository
import com.example.easymart.presentation.common.Resource
import javax.inject.Inject

class UpsertProductUseCase @Inject constructor(
    private val repo: ProductRepository
){
    suspend fun upsertProduct(product: Product): Resource<Unit> = repo.upsertProduct(product)
}