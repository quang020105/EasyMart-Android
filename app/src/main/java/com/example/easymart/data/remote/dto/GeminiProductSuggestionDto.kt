package com.example.easymart.data.remote.dto

data class GeminiProductSuggestion(
    val title: String? = null,
    val category: String? = null,
    val description: String? = null,
    val confidence: Float? = null
) {
    fun sanitize(): GeminiProductSuggestion = copy(
        title = title.normalizeField(),
        category = category.normalizeField(),
        description = description.normalizeField(),
        confidence = confidence?.coerceIn(0f, 1f)
    )
}

private fun String?.normalizeField(): String? {
    val value = this?.replace("\\n", " ")?.replace("\\t", " ")?.trim().orEmpty()
    return value.takeIf { it.isNotBlank() }
}
