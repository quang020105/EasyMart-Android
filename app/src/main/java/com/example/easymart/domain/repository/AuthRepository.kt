package com.example.easymart.domain.repository

import com.example.easymart.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun register(email: String, password: String, name: String): Result<User>
    suspend fun login(email: String, password: String): Result<User>
    fun getCurrentUser(): User?
    fun observeCurrentUser(): Flow<User?>
    suspend fun logout(): Result<Unit>
}