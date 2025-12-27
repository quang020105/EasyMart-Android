package com.example.easymart.domain.repository

import com.example.easymart.domain.model.District
import com.example.easymart.domain.model.Province
import com.example.easymart.domain.model.Ward

interface LocationRepository {
    suspend fun loadProvinces(): List<Province>
    suspend fun loadDistricts(provinceCode: Int): List<District>
    suspend fun loadWards(districtCode: Int): List<Ward>
}