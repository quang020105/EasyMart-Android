package com.example.easymart.data.remote.datasource_impl

import com.example.easymart.data.remote.api.ImageSearchApi
import com.example.easymart.data.remote.datasource.ImageSearchRemoteDataSource
import com.example.easymart.data.remote.dto.ImageSearchRequestDto
import com.example.easymart.data.remote.dto.ImageSearchResponseDto
import javax.inject.Inject

class RetrofitImageSearchRemoteDataSource @Inject constructor(
    private val api: ImageSearchApi
) : ImageSearchRemoteDataSource {
    override suspend fun searchByImage(request: ImageSearchRequestDto): ImageSearchResponseDto {
        val response = api.searchByImage(request.image)

        if (response.isSuccessful) {
            val body = response.body()
                ?: throw IllegalStateException("Empty image search response")

            if (!body.success) {
                throw IllegalStateException(body.message.ifBlank { "Image search failed" })
            }

            return body
        }

        throw IllegalStateException(
            response.errorBody()?.string()?.takeIf { it.isNotBlank() }
                ?: "Failed to search by image: ${response.code()} ${response.message()}"
        )
    }
}
