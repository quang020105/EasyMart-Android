package com.example.easymart.domain.repository

import com.example.easymart.domain.model.Product

interface SearchRepository {
    suspend fun searchProducts(query: String): List<Product>
    suspend fun getSuggestions(query: String): List<String>
}