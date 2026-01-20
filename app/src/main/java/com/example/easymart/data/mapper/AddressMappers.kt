package com.example.easymart.data.mapper

import com.example.easymart.data.local.entity.AddressEmbedded
import com.example.easymart.data.local.entity.AddressEntity
import com.example.easymart.domain.model.Address

fun AddressEntity.toDomain(): Address {
    return Address(
        id = id,
        name = name,
        phone = phone,
        detailAddress = detailAddress,
        addressString = addressString,
        isDefault = isDefault,
        provinceCode = provinceCode,
        districtCode = districtCode,
        wardCode = wardCode
    )
}

fun Address.toEntity(): AddressEntity {
    return AddressEntity(
        id = id,
        name = name,
        tagName = tagName,
        phone = phone,
        detailAddress = detailAddress,
        addressString = addressString,
        isDefault = isDefault,
        provinceCode = provinceCode,
        districtCode = districtCode,
        wardCode = wardCode
    )
}


fun Address.toEmbedded(): AddressEmbedded {
    return AddressEmbedded(
        name = name,
        phone = phone,
        addressString = addressString,
    )
}

fun AddressEmbedded.toDomain(): Address {
    return Address(
        name = name,
        phone = phone,
        addressString = addressString,
    )
}