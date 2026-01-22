package com.example.easymart.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String? = null,
    val avatarUrl: String? = null,
    val createdAt: Long
)
