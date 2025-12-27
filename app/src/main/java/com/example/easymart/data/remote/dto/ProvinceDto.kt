package com.example.easymart.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProvinceDto(
    val name: String,
    val code: Int,
    @SerializedName("division_type") val divisionType: String? = null,
    @SerializedName("code_name") val codeName: String? = null,
    @SerializedName("phone_code") val phoneCode: Int = 0,
    val districts: List<DistrictDto>? = emptyList()
)

data class DistrictDto(
    val name: String,
    val code: Int,
    @SerializedName("division_type") val divisionType: String? = null,
    @SerializedName("code_name") val codeName: String? = null,
    @SerializedName("province_code") val provinceCode: Int,
    val wards: List<WardDto>? = emptyList()
)

data class WardDto(
    val name: String,
    val code: Int,
    @SerializedName("division_type") val divisionType: String? = null,
    @SerializedName("code_name") val codeName: String? = null,
    @SerializedName("district_code") val districtCode: Int,
)


