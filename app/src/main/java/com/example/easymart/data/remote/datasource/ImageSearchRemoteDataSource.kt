package com.example.easymart.data.remote.datasource

import com.example.easymart.data.remote.dto.ImageSearchRequestDto
import com.example.easymart.data.remote.dto.ImageSearchResponseDto

interface ImageSearchRemoteDataSource {
    suspend fun searchByImage(request: ImageSearchRequestDto): ImageSearchResponseDto
}
