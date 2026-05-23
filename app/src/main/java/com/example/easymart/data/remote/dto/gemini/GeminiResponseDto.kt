package com.example.easymart.data.remote.dto.gemini

data class GeminiGenerateContentResponse(
    val candidates: List<GeminiCandidate> = emptyList()
)

data class GeminiCandidate(
    val content: GeminiResponseContent? = null
)

data class GeminiResponseContent(
    val parts: List<GeminiResponsePart> = emptyList()
)

data class GeminiResponsePart(
    val text: String? = null
)