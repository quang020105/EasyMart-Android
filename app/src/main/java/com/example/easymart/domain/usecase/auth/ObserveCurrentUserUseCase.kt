package com.example.easymart.domain.usecase.auth

import com.example.easymart.domain.model.User
import com.example.easymart.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<User?> =
        authRepository.observeCurrentUser()
}
