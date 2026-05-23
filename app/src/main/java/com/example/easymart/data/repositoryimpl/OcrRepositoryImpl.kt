package com.example.easymart.data.repositoryimpl

import com.example.easymart.data.local.ocr.MlKitOcrDataSource
import com.example.easymart.data.remote.ocr.GeminiOcrDataSource
import com.example.easymart.domain.model.ocr.ProductOcrResult
import com.example.easymart.domain.repository.OcrRepository
import javax.inject.Inject
import androidx.core.net.toUri

class OcrRepositoryImpl @Inject constructor(
    private val ocrDataSource: MlKitOcrDataSource,
    private val geminiOcrDataSource: GeminiOcrDataSource
) : OcrRepository {
    override suspend fun analyzeProductImage(uri: String): ProductOcrResult {
        val text = ocrDataSource.extractText(uri.toUri())
        val suggestion = geminiOcrDataSource.normalizeProductText(text)
        return ProductOcrResult(
            rawText = text,
            suggestedTitle = suggestion.title,
            suggestedCategory = suggestion.category,
            suggestedDescription = suggestion.description,
            suggestionConfidence = suggestion.confidence
        )
    }
}
