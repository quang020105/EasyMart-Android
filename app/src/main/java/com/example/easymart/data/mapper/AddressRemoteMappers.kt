package com.example.easymart.data.mapper

import com.example.easymart.data.local.entity.AddressEntity
import com.example.easymart.data.remote.dto.AddressRemoteDto

fun AddressEntity.toRemoteDto(): AddressRemoteDto {
    return AddressRemoteDto(
        id = id,
        userUid = userUid,
        name = name,
        phone = phone,
        detailAddress = detailAddress,
        addressString = addressString,
        isDefault = isDefault,
        provinceCode = provinceCode,
        districtCode = districtCode,
        wardCode = wardCode,
        isDeleted = isDeleted,
        updatedAt = updatedAt,
        createdAt = createdAt
    )
}

fun AddressRemoteDto.toEntity(): AddressEntity {
    return AddressEntity(
        id = id,
        userUid = userUid,
        name = name,
        phone = phone,
        detailAddress = detailAddress,
        addressString = addressString,
        isDefault = isDefault,
        provinceCode = provinceCode,
        districtCode = districtCode,
        wardCode = wardCode,
        isDeleted = isDeleted,
        isSynced = true,
        updatedAt = updatedAt,
        createdAt = createdAt
    )
}

