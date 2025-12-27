package com.example.easymart.domain.usecase.location

import com.example.easymart.domain.repository.LocationRepository
import javax.inject.Inject

class GetDistrictsUseCase @Inject constructor(
    private val repo: LocationRepository
) {
    suspend operator fun invoke(provinceCode: Int) = repo.loadDistricts(provinceCode = provinceCode)
}