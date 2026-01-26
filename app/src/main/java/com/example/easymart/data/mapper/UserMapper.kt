package com.example.easymart.data.mapper

import com.example.easymart.data.remote.dto.FirebaseUserDto
import com.example.easymart.domain.model.User
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toDomain(): User {
    return User(
        id = uid,
        name = displayName ?: "",
        email = email ?: "",
        createdAt = 0L
    )
}

fun FirebaseUserDto.toDomain(): User {
    return User(
        id = uid,
        name = name,
        email = email,
        phone = phone,
        avatarUrl = avatarUrl,
        createdAt = createdAt
    )
}