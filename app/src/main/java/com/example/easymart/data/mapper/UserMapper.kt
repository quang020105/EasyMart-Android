package com.example.easymart.data.mapper

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