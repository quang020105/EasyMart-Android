package com.example.easymart.data.remote.api

import com.example.easymart.data.remote.dto.ImageSearchResponseDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImageSearchApi {
    @Multipart
    @POST("image")
    suspend fun searchByImage(
        @Part image: MultipartBody.Part
    ): Response<ImageSearchResponseDto>
}
