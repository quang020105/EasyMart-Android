package com.example.easymart.domain.usecase.address

import com.example.easymart.domain.repository.AddressRepository
import javax.inject.Inject

class DeleteAddressUseCase @Inject constructor(
    private val repo: AddressRepository
){
    suspend operator fun invoke(id: Int) = repo.deleteAddress(id)
}