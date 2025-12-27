package com.example.easymart.presentation.ui.deliveryaddress

import com.example.easymart.domain.model.District
import com.example.easymart.domain.model.Province
import com.example.easymart.domain.model.Ward

data class AddAddressUIState(
    val editId: Int? = null,
    val isEdit: Boolean = false,
    val fullName: String = "",
    val phone: String = "",
    val detailAddress: String = "",
    val street: String = "",
    val provinces: List<Province> = emptyList(),
    val districts: List<District> = emptyList(),
    val wards: List<Ward> = emptyList(),
    val selectedProvince: Province? = null,
    val selectedDistrict: District? = null,
    val selectedWard: Ward? = null,
    val isDefault: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val fullNameError: String? = null,
    val phoneError: String? = null,
    val detailAddressError: String? = null,
    val success: Boolean = false
)
