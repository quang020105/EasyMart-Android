package com.example.easymart.domain.usecase.address

import com.example.easymart.domain.repository.AddressRepository
import javax.inject.Inject

class GetDefaultAddressByUserUseCase @Inject constructor(
    private val repo: AddressRepository
) {
    suspend operator fun invoke(userUid: String) = repo.getDefaultAddressByUser(userUid)
}

