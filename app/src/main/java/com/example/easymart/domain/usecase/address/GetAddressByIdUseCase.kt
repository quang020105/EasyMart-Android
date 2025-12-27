package com.example.easymart.domain.usecase.address

import com.example.easymart.domain.repository.AddressRepository
import javax.inject.Inject

class GetAddressByIdUseCase @Inject constructor(
    private val repo: AddressRepository
){
    suspend operator fun invoke(id: Int) = repo.getAddressById(id)
}