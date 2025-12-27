package com.example.easymart.domain.model

data class Province(
    val name: String,
    val code: Int,
    val divisionType: String? = null,
    val codeName: String? = null,
    val phoneCode: Int = 0,
    val districts: List<District> = emptyList()
)

data class District(
    val name: String,
    val code: Int,
    val divisionType: String? = null,
    val codeName: String? = null,
    val provinceCode: Int = 0,
    val wards: List<Ward> = emptyList()
)

data class Ward(
    val name: String,
    val code: Int,
    val divisionType: String? = null,
    val codeName: String? = null,
    val districtCode: Int = 0,
)
