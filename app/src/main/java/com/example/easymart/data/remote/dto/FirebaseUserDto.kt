package com.example.easymart.data.remote.dto

class FirebaseUserDto(
    var uid: String = "",
    var name: String = "",
    var email: String = "",
    var phone: String? = null,
    var avatarUrl: String? = null,
    var role: String = "customer",
    var createdAt: Long = 0L
)