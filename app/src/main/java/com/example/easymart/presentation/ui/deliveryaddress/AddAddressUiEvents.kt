package com.example.easymart.presentation.ui.deliveryaddress

import com.example.easymart.domain.model.District
import com.example.easymart.domain.model.Province
import com.example.easymart.domain.model.Ward

interface AddAddressUiEvents {
    fun onFullNameChange(v: String)
    fun onPhoneChange(v: String)
    fun onDetailAddressChange(v: String)
    fun onProvinceSelected(p: Province?)
    fun onDistrictSelected(d: District?)
    fun onWardSelected(w: Ward?)
    fun onDefaultToggle(v: Boolean)
    fun onSave()
    fun onCancel()
}