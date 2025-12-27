package com.example.easymart.presentation.ui.deliveryaddress

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.easymart.data.local.entity.AddressEntity
import com.example.easymart.domain.model.Address
import com.example.easymart.domain.model.District
import com.example.easymart.domain.model.Province
import com.example.easymart.domain.model.Ward
import com.example.easymart.domain.usecase.address.DeleteAddressUseCase
import com.example.easymart.domain.usecase.address.GetAddressByIdUseCase
import com.example.easymart.domain.usecase.address.GetAllAddressUseCase
import com.example.easymart.domain.usecase.address.InsertNewAddressUseCase
import com.example.easymart.domain.usecase.address.SetDefaultAddressUseCase
import com.example.easymart.domain.usecase.address.UpdateAddressUseCase
import com.example.easymart.domain.usecase.location.GetDistrictsUseCase
import com.example.easymart.domain.usecase.location.GetProvincesUseCase
import com.example.easymart.domain.usecase.location.GetWardsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val getALlAddressUS: GetAllAddressUseCase,
    private val insertAddressUS: InsertNewAddressUseCase,
    private val updateAddressUS: UpdateAddressUseCase,
    private val deleteAddressUS: DeleteAddressUseCase,
    private val getAddressByIdUS: GetAddressByIdUseCase,
    private val setDefaultAddressUS: SetDefaultAddressUseCase,
    private val getProvincesUS: GetProvincesUseCase,
    private val getDistrictsUS: GetDistrictsUseCase,
    private val getWardsUS: GetWardsUseCase
) : ViewModel() {
    val addresses: StateFlow<List<Address>> = getALlAddressUS()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _selectedAddress = MutableStateFlow<Address?>(null)
    val selectedAddress = _selectedAddress.asStateFlow()

    private val _uiState = MutableStateFlow(AddAddressUIState())
    val uiState = _uiState.asStateFlow()

    val isFormValid: StateFlow<Boolean> = _uiState
        .map { state ->
            val noFieldEmpty = state.fullName.isNotBlank()
                    && state.phone.isNotBlank()
                    && state.detailAddress.isNotBlank()
            val phoneOk = Regex("^(0[1-9][0-9]{8}|\\+84[1-9][0-9]{8})$").matches(state.phone)
            val locationOk =
                state.selectedProvince != null && state.selectedDistrict != null && state.selectedWard != null
            val noVmErrors =
                state.fullNameError == null && state.phoneError == null && state.detailAddressError == null && state.errorMessage == null
            noFieldEmpty && phoneOk && locationOk && noVmErrors
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, false)


    init {
        loadProvinces()
    }

    private fun loadProvinces() {
        viewModelScope.launch(Dispatchers.IO) {
            val provinces = getProvincesUS()
            _uiState.value = _uiState.value.copy(provinces = provinces)
        }
        Log.d("AddressViewModel", "districts: ${uiState.value.districts}")
    }

    fun onProvinceSelected(province: Province?) {
        viewModelScope.launch {
            val districts = withContext(Dispatchers.IO) {
                getDistrictsUS(province?.code ?: 0)
            }
            _uiState.value = _uiState.value.copy(districts = districts, selectedProvince = province)
            //_uiState.update { it.copy(districts = districts, selectedProvince = province) }
            Log.d("AddressViewModel", "districts: $districts")
        }
    }

    fun onDistrictSelected(district: District?) {
        viewModelScope.launch(Dispatchers.IO) {
            val wards = getWardsUS(district?.code ?: 0)
            _uiState.value = _uiState.value.copy(wards = wards, selectedDistrict = district)
        }
    }

    fun onWardsSelected(ward: Ward?) {
        _uiState.value = _uiState.value.copy(selectedWard = ward)
    }

    fun selectAddress(address: Address) {
        _selectedAddress.value = address
    }

    fun addNewAddress(address: Address) {
        viewModelScope.launch {
            insertAddressUS(address)
        }
    }

    fun onFullNameChange(fullName: String) =
        _uiState.update { it.copy(fullName = fullName, errorMessage = null) }

    fun onPhoneNumberChange(phoneNumber: String) =
        _uiState.update { it.copy(phone = phoneNumber, errorMessage = null) }

    fun onDetailAddressChange(address: String) =
        _uiState.update { it.copy(detailAddress = address, errorMessage = null) }

    fun onToggleDefault(isDefault: Boolean) =
        _uiState.update { it.copy(isDefault = isDefault) }

    private fun validateFullName(fullName: String) {
        if (fullName.isBlank())
            _uiState.update { it.copy(fullNameError = "Tên người nhận là bắt buộc") }
        else
            _uiState.update { it.copy(fullNameError = null) }
    }

    private fun validatePhoneNumber(phoneNumber: String) {
        val phoneRegex = Regex("^(0[1-9][0-9]{8}|\\+84[1-9][0-9]{8})$")
        if (!phoneRegex.matches(phoneNumber))
            _uiState.update { it.copy(phoneError = "Số điện thoại không hợp lệ") }
        else
            _uiState.update { it.copy(phoneError = null) }
    }

    private fun validateDetailAddress(detailAddress: String) {
        if (detailAddress.isBlank())
            _uiState.update { it.copy(detailAddressError = "Địa chỉ chi tiết là bắt buộc") }
        else
            _uiState.update { it.copy(detailAddressError = null) }
    }

    private fun validateLocation(state: AddAddressUIState) {
        if (state.selectedProvince == null || state.selectedDistrict == null || state.selectedWard == null)
            _uiState.update { it.copy(errorMessage = "Vui lòng chọn địa chỉ") }
        else
            _uiState.update { it.copy(errorMessage = null) }
    }

    //load dữ liệu cho form edit
    fun loadAddress(id: Int) {
        viewModelScope.launch {
            val editAddress = getAddressByIdUS(id)
            editAddress?.let { editAddress ->
                val provinces = _uiState.value.provinces.ifEmpty { getProvincesUS() }
                val prov = provinces.find { it.code == editAddress.provinceCode }
                val districts = prov?.let { getDistrictsUS(prov.code) }
                    ?: getDistrictsUS(editAddress.provinceCode)
                val dist = districts.find { it.code == editAddress.districtCode }
                val wards =
                    dist?.let { getWardsUS(dist.code) } ?: getWardsUS(editAddress.districtCode)
                val ward = wards.find { it.code == editAddress.wardCode }
                Log.d("AddressViewModel", "editAddress: $editAddress")
                Log.d("AddressViewModel", "provinces: $provinces")
                Log.d("AddressViewModel", "districts: $districts")
                Log.d("AddressViewModel", "wards: $wards")
                Log.d("AddressViewModel", "prov: $prov, dist: $dist, ward: $ward")

                _uiState.value = _uiState.value.copy(
                    editId = id,
                    isEdit = true,
                    fullName = editAddress.name,
                    phone = editAddress.phone,
                    detailAddress = editAddress.detailAddress,
                    provinces = provinces,
                    districts = districts,
                    wards = wards,
                    selectedProvince = prov,
                    selectedDistrict = dist,
                    selectedWard = ward,
                    isDefault = editAddress.isDefault,
                    fullNameError = null,
                    phoneError = null,
                    detailAddressError = null,
                    errorMessage = null
                )
            }
        }
    }


    fun onSave() {
        var current = _uiState.value
        validateFullName(current.fullName)
        validatePhoneNumber(current.phone)
        validateDetailAddress(current.detailAddress)
        validateLocation(current)
        current = _uiState.value
        Log.d(
            "AddressViewModel",
            "fullNameErr" + current.fullNameError + "phoneErr" + current.phoneError + "detailErr" + current.detailAddressError + "locationErr" + current.errorMessage
        )
        //kiểm tra xem có lỗi không ,nếu không lỗi thì làm bước tiếp theo
        val hasError = listOf(
            current.fullNameError,
            current.phoneError,
            current.detailAddressError,
            current.errorMessage
        )
            .any { it != null }
        Log.d("AddressViewModel", "hasError: $hasError")
        if (!hasError) {
            //thêm vào database
            val address = Address(
                id = current.editId ?: 0,
                name = current.fullName,
                phone = current.phone,
                detailAddress = current.detailAddress,
                districtCity = "",
                isDefault = current.isDefault,
                provinceCode = current.selectedProvince?.code ?: 0,
                districtCode = current.selectedDistrict?.code ?: 0,
                wardCode = current.selectedWard?.code ?: 0
            )
            viewModelScope.launch {
                if (current.isEdit) {
                    updateAddressUS(address)
                    if(current.isDefault){
                        setDefaultAddressUS(address.id)
                    }
                } else {
                    val newId = insertAddressUS(address)
                    if(current.isDefault){
                        setDefaultAddressUS(newId)
                    }
                }
                _uiState.update { it.copy(success = true) }
                Log.d("AddressViewModel", "uiState: ${_uiState.value}")
            }
        }
    }

    //xóa trạng thái thêm thành công sau khi add
    fun clearSuccess() {
        _uiState.update { it.copy(success = false) }
    }

    fun resetForm() {
        _uiState.update { AddAddressUIState() }
    }
}