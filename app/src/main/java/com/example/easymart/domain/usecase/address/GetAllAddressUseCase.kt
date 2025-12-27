package com.example.easymart.domain.usecase.address

import com.example.easymart.domain.repository.AddressRepository
import javax.inject.Inject

class GetAllAddressUseCase @Inject constructor(
    private val repo: AddressRepository
){
    operator fun invoke() = repo.getAllAddresses()
}