package com.example.easymart.domain.usecase.auth

import com.example.easymart.domain.model.User
import com.example.easymart.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> =
        authRepository.login(email, password)
}