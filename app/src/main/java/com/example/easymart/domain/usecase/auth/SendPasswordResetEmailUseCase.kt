package com.example.easymart.domain.usecase.auth

import com.example.easymart.domain.repository.AuthRepository
import javax.inject.Inject

class SendPasswordResetEmailUseCase @Inject constructor(
    private val repo: AuthRepository
){
    suspend operator fun invoke(email: String) = repo.sendPasswordResetEmail(email)
}