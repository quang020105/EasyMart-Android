package com.example.easymart.domain.model.ocr

// dùng để lưu kết quả OCR sau khi scan sản phẩm
data class ProductOcrResult(
    val rawText: String,
    val suggestedTitle: String? = null,
    val suggestedCategory: String? = null,
    val suggestedDescription: String? = null,
    val suggestionConfidence: Float? = null
)
