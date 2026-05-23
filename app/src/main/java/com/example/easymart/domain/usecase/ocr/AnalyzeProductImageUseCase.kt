package com.example.easymart.domain.usecase.ocr

import com.example.easymart.domain.model.ocr.ProductOcrResult
import com.example.easymart.domain.repository.OcrRepository
import javax.inject.Inject

class AnalyzeProductImageUseCase @Inject constructor(
    private val repository: OcrRepository
) {
    suspend operator fun invoke(uri: String): ProductOcrResult =
        repository.analyzeProductImage(uri)
}

