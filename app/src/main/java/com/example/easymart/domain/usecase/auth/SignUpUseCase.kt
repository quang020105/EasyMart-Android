package com.example.easymart.domain.usecase.auth

import com.example.easymart.domain.model.User
import com.example.easymart.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        name: String
    ): Result<User>  = authRepository.register(email, password, name)
}