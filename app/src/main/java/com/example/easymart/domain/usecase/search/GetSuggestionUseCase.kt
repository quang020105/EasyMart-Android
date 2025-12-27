package com.example.easymart.domain.usecase.search

import com.example.easymart.domain.repository.SearchRepository
import javax.inject.Inject

class GetSuggestionUseCase @Inject constructor(
    private val repo: SearchRepository
) {
    suspend operator fun invoke(query: String) = repo.getSuggestions(query)
}