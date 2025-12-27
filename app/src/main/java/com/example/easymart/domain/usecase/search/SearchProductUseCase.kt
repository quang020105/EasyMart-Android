package com.example.easymart.domain.usecase.search

import com.example.easymart.domain.model.Product
import com.example.easymart.domain.repository.SearchRepository
import javax.inject.Inject

class SearchProductUseCase @Inject constructor(
    private val repo: SearchRepository
) {
    suspend operator fun invoke(query: String) = repo.searchProducts(query)
}