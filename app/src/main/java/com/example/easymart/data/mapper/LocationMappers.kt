package com.example.easymart.data.mapper

import com.example.easymart.data.remote.dto.DistrictDto
import com.example.easymart.data.remote.dto.ProvinceDto
import com.example.easymart.data.remote.dto.WardDto
import com.example.easymart.domain.model.District
import com.example.easymart.domain.model.Province
import com.example.easymart.domain.model.Ward

fun ProvinceDto.toDomain(): Province {
    val districtsDomain = districts?.map { it.toDomain() }
    return Province(
        name = name,
        code = code,
        divisionType = divisionType,
        codeName = codeName,
        phoneCode = phoneCode,
        districts = districtsDomain ?: emptyList()
    )
}

fun DistrictDto.toDomain(): District {
    val wardsDomain = wards?.map{ it.toDomain()}
    return District(
        name = name,
        code = code,
        divisionType = divisionType,
        codeName = codeName,
        provinceCode = provinceCode,
        wards = wardsDomain ?: emptyList()
    )
}

fun WardDto.toDomain(): Ward {
    return Ward(
        name = name,
        code = code,
        divisionType = divisionType,
        codeName = codeName,
        districtCode = districtCode,
    )
}