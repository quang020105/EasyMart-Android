package com.example.easymart.domain.usecase.location

import com.example.easymart.domain.repository.LocationRepository
import javax.inject.Inject

class GetWardsUseCase @Inject constructor(
    private val repo: LocationRepository
) {
    suspend operator fun invoke(districtCode: Int) = repo.loadWards(districtCode = districtCode)
}