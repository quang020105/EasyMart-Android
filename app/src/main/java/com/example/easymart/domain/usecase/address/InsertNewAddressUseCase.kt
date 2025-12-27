package com.example.easymart.domain.usecase.address

import com.example.easymart.domain.model.Address
import com.example.easymart.domain.repository.AddressRepository
import javax.inject.Inject

class InsertNewAddressUseCase @Inject constructor(
    private val repo: AddressRepository
){
    suspend operator fun invoke(address: Address): Int = repo.insertAddress(address)
}