package com.example.easymart.data.repositoryimpl

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.example.easymart.data.mapper.toDomain
import com.example.easymart.data.remote.datasource.ImageSearchRemoteDataSource
import com.example.easymart.data.remote.dto.ImageSearchRequestDto
import com.example.easymart.domain.model.image_search.ImageSearchResult
import com.example.easymart.domain.repository.ImageSearchRepository
import com.example.easymart.presentation.common.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class ImageSearchRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val remoteDataSource: ImageSearchRemoteDataSource
) : ImageSearchRepository {
    override suspend fun searchByImage(uri: Uri): Resource<ImageSearchResult> = withContext(Dispatchers.IO) {
        runCatching {
            val request = ImageSearchRequestDto(image = uri.toImagePart())
            remoteDataSource.searchByImage(request).toDomain()
        }.fold(
            onSuccess = { Resource.Success(it) },
            onFailure = { error -> Resource.Error(error.toImageSearchMessage()) }
        )
    }

    private fun Throwable.toImageSearchMessage(): String {
        return when (this) {
            is SocketTimeoutException ->
                "Tìm kiếm bằng hình ảnh mất nhiều thời gian hơn dự kiến. Vui lòng thử lại hoặc chọn ảnh rõ hơn."
            is IOException ->
                "Không thể kết nối đến máy chủ Image Search. Vui lòng kiểm tra mạng hoặc backend rồi thử lại."
            is IllegalArgumentException ->
                message ?: "Ảnh không hợp lệ. Vui lòng chọn ảnh khác."
            else ->
                "Lỗi tìm kiếm bằng hình ảnh: ${message ?: "Không xác định"}"
        }
    }

    private fun Uri.toImagePart(): MultipartBody.Part {
        val resolver = context.contentResolver
        val mimeType = resolver.getType(this)
            ?.takeIf { it.startsWith("image/") }
            ?: DEFAULT_IMAGE_MIME_TYPE
        val bytes = resolver.openInputStream(this)?.use { it.readBytes() }
            ?: throw IllegalArgumentException("Cannot open image uri")

        require(bytes.isNotEmpty()) { "Image uri contains empty data" }

        val requestBody = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(
            FORM_FIELD_IMAGE,
            resolveFileName() ?: DEFAULT_FILE_NAME,
            requestBody
        )
    }

    private fun Uri.resolveFileName(): String? {
        val resolver = context.contentResolver
        return resolver.query(this, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
            ?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex >= 0 && cursor.moveToFirst()) {
                    cursor.getString(nameIndex)
                } else {
                    null
                }
            }
    }

    private companion object {
        const val FORM_FIELD_IMAGE = "image"
        const val DEFAULT_FILE_NAME = "image_search_upload.jpg"
        const val DEFAULT_IMAGE_MIME_TYPE = "image/jpeg"
    }
}
