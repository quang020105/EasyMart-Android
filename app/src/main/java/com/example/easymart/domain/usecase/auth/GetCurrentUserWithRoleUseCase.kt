package com.example.easymart.domain.usecase.auth

import com.example.easymart.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserWithRoleUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() = authRepository.getCurrentUserWithRole()
}

