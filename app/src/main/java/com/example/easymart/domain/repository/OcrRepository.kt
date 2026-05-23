package com.example.easymart.domain.repository

import com.example.easymart.domain.model.ocr.ProductOcrResult

interface OcrRepository {
    suspend fun analyzeProductImage(uri: String): ProductOcrResult
}

