package com.example.easymart.domain.usecase.location

import com.example.easymart.domain.repository.LocationRepository
import javax.inject.Inject

class GetProvincesUseCase @Inject constructor(
    private val repo: LocationRepository
) {
    suspend operator fun invoke() = repo.loadProvinces()
}