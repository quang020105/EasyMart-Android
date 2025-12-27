package com.example.easymart.data.remote.api


import com.example.easymart.data.remote.dto.DistrictDto
import com.example.easymart.data.remote.dto.ProvinceDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LocationApi {
    //danh sách tỉnh thành
    @GET("api/v1/p/")
    suspend fun getProvinces(@Query("depth") depth: Int = 1): Response<List<ProvinceDto>>

    @GET("api/v1/p/{provinceCode}")
    suspend fun getProvinceByCode(@Path("provinceCode") provinceCode: Int, @Query("depth") depth: Int = 2): Response<ProvinceDto>

    @GET("api/v1/d/")
    suspend fun getDistricts(@Query("depth") depth: Int = 1)

    @GET("api/v1/d/{districtCode}")
    suspend fun getDistrictByCode(@Path("districtCode") districtCode: Int, @Query("depth") depth: Int = 2): Response<DistrictDto>
}