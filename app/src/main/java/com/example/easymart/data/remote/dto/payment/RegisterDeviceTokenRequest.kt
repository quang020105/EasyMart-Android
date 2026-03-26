package com.example.easymart.data.remote.dto.payment

data class RegisterDeviceTokenRequest(
    val userId: String,
    val fcmToken: String
)