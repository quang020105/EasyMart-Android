package com.example.easymart.data.repositoryimpl

import android.util.Log
import com.example.easymart.data.mapper.toDomain
import com.example.easymart.data.remote.api.LocationApi
import com.example.easymart.data.remote.dto.ProvinceDto
import com.example.easymart.domain.model.District
import com.example.easymart.domain.model.Province
import com.example.easymart.domain.model.Ward
import com.example.easymart.domain.repository.LocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val api: LocationApi
): LocationRepository{
    //lưu trữ dữ liệu tỉnh thành tạm thời
    private var cachedProvinces: List<ProvinceDto>? = null
    override suspend fun loadProvinces(): List<Province> = withContext(Dispatchers.IO){
        //trả về luôn nếu có dữ liệu
        cachedProvinces?.let { cached ->
            return@withContext cached.map {
                it.toDomain()
            }
        }
        val res = api.getProvinces()
        if(res.isSuccessful){
            val data = res.body()
            cachedProvinces = data
            return@withContext data?.map { province -> province.toDomain() } ?: emptyList()
        } else {
            return@withContext emptyList()
        }
    }

    override suspend fun loadDistricts(provinceCode: Int): List<District> = withContext(Dispatchers.IO){
        //nếu có dữ liệu sẵn thì lấy danh sách district theo mã tỉnh
        cachedProvinces?.find { it.code == provinceCode }?.districts?.let { districts ->
            Log.d("LocationRepositoryImpl", "districts: $districts")
            if(districts.isNotEmpty()){
                return@withContext districts.map { it.toDomain() }
            }
        }
        val res = api.getProvinceByCode(provinceCode = provinceCode,depth = 2)
        if(res.isSuccessful){
            val provinceSelected = res.body()
            Log.d("LocationRepositoryImpl", "loadDistricts: $provinceSelected")
            return@withContext provinceSelected?.toDomain()?.districts ?: emptyList()
        } else {
            Log.d("LocationRepositoryImpl", "Lỗi")
            return@withContext emptyList()
        }

    }

    override suspend fun loadWards(districtCode: Int): List<Ward>  = withContext(Dispatchers.IO){
        val res = api.getDistrictByCode(districtCode = districtCode, depth = 2)
        if(res.isSuccessful){
            val districtSelected = res.body()
            return@withContext districtSelected?.toDomain()?.wards ?: emptyList()
        } else {
            return@withContext emptyList()
        }
    }

}