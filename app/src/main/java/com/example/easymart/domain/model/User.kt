package com.example.easymart.domain.model

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val passwordHash: String,
    val createdAt: String
)
