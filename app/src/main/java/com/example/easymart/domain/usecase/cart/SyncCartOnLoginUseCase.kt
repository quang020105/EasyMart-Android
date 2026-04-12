package com.example.easymart.domain.usecase.cart

import com.example.easymart.domain.usecase.auth.ObserveCurrentUserUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// usecase theo dõi user hiện tại để sync cart khi đăng nhập thành công
class SyncCartOnLoginUseCase @Inject constructor(
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase
) {
    operator fun invoke(): Flow<String> {
        return observeCurrentUserUseCase()
            .map { it?.id }
            .filterNotNull()
            .distinctUntilChanged()
    }
}

