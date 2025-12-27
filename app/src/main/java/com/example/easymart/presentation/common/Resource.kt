package com.example.easymart.presentation.common

sealed class Resource<out T> {
    data object Loading: Resource<Nothing>()
    data class Success<T>(val data: T): Resource<T>()
    data class Error<Nothing>(val message: String? = null): Resource<Nothing>()
}