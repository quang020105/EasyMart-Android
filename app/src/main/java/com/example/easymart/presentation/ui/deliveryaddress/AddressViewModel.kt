package com.example.easymart.presentation.ui.deliveryaddress

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymart.di.IoDispatcher
import com.example.easymart.domain.model.Address
import com.example.easymart.domain.model.District
import com.example.easymart.domain.model.Province
import com.example.easymart.domain.model.Ward
import com.example.easymart.domain.usecase.address.DeleteAddressUseCase
import com.example.easymart.domain.usecase.address.GetAddressByIdUseCase
import com.example.easymart.domain.usecase.address.GetAllAddressUseCase
import com.example.easymart.domain.usecase.address.GetDefaultAddressUseCase
import com.example.easymart.domain.usecase.address.InsertNewAddressUseCase
import com.example.easymart.domain.usecase.address.SetDefaultAddressUseCase
import com.example.easymart.domain.usecase.address.UpdateAddressUseCase
import com.example.easymart.domain.usecase.location.GetDistrictsUseCase
import com.example.easymart.domain.usecase.location.GetProvincesUseCase
import com.example.easymart.domain.usecase.location.GetWardsUseCase
import com.example.easymart.domain.usecase.address.GetAllAddressByUserUseCase
import com.example.easymart.domain.usecase.address.GetDefaultAddressByUserUseCase
import com.example.easymart.domain.usecase.address.SyncAddressesUseCase
import com.example.easymart.domain.usecase.auth.GetCurrentUserUseCase
import com.example.easymart.domain.usecase.auth.ObserveCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val getALlAddressUS: GetAllAddressUseCase,
    private val getAllAddressByUserUS: GetAllAddressByUserUseCase,
    private val insertAddressUS: InsertNewAddressUseCase,
    private val updateAddressUS: UpdateAddressUseCase,
    private val deleteAddressUS: DeleteAddressUseCase,
    private val getAddressByIdUS: GetAddressByIdUseCase,
    private val setDefaultAddressUS: SetDefaultAddressUseCase,
    private val getDefaultAddressUseCase: GetDefaultAddressUseCase,
    private val getDefaultAddressByUserUS: GetDefaultAddressByUserUseCase,
    private val getProvincesUS: GetProvincesUseCase,
    private val getDistrictsUS: GetDistrictsUseCase,
    private val getWardsUS: GetWardsUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    private val syncAddressesUseCase: SyncAddressesUseCase,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val currentUserUid: StateFlow<String?> = observeCurrentUserUseCase()
        .map { it?.id }
        .stateIn(viewModelScope, SharingStarted.Lazily, getCurrentUserUseCase()?.id)

    @OptIn(ExperimentalCoroutinesApi::class)
    val addresses: StateFlow<List<Address>> = currentUserUid
        .flatMapLatest { uid ->
            if (uid == null) getALlAddressUS() else getAllAddressByUserUS(uid)
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _selectedAddressId = MutableStateFlow<Int?>(null)

    // kết hợp giữa danh sách địa chỉ và id địa chỉ được chọn để lấy ra địa chỉ được chọn
    // , nếu không có thì lấy địa chỉ mặc định
    val selectedAddress = combine(addresses, _selectedAddressId) { list, selectedId ->
        list.firstOrNull { it.id == selectedId }
            ?: list.firstOrNull { it.isDefault }
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)


    private val _uiState = MutableStateFlow(AddAddressUIState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<AddressUiEvent>(
        1
    )
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        loadDefaultAddress()
        viewModelScope.launch {
            currentUserUid
                .collect { uid ->
                    if (uid != null) {
                        syncAddressesUseCase(uid)
                    }
                }
        }
    }

    //set địa chỉ mặc định khi vào màn thanh toán
    private fun loadDefaultAddress() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val defaultAddress = withContext(ioDispatcher) {
                    val uid = getCurrentUserUseCase()?.id
                    if (uid != null) getDefaultAddressByUserUS(uid) else getDefaultAddressUseCase()
                }
                _selectedAddressId.value = defaultAddress?.id
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun clearUiEvent() {
        _uiEvent.resetReplayCache()
    }

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


//    init {
//        loadProvinces()
//    }

    fun loadProvinces() {
        viewModelScope.launch {
            val provinces = withContext(ioDispatcher) {
                getProvincesUS()
            }
            Log.d("AddressViewModel", "provincesUC: $provinces")
            _uiState.value = _uiState.value.copy(provinces = provinces)
            Log.d("AddressViewModel", "provinces: ${uiState.value.provinces}")
        }
    }

    fun onProvinceSelected(province: Province?) {
        viewModelScope.launch {
            val districts = withContext(ioDispatcher) {
                getDistrictsUS(province?.code ?: 0)
            }
            _uiState.value = _uiState.value.copy(
                districts = districts,
                selectedProvince = province,
                selectedDistrict = null,
                selectedWard = null
            )
            //_uiState.update { it.copy(districts = districts, selectedProvince = province) }
            Log.d("AddressViewModel", "districts: $districts")
        }
    }

    fun onDistrictSelected(district: District?) {
        viewModelScope.launch {
            val wards = withContext(ioDispatcher) {
                getWardsUS(district?.code ?: 0)
            }
            _uiState.value =
                _uiState.value.copy(wards = wards, selectedDistrict = district, selectedWard = null)
        }
    }

    fun onWardsSelected(ward: Ward?) {
        _uiState.value = _uiState.value.copy(selectedWard = ward)
    }

    fun selectAddress(address: Address) {
        _selectedAddressId.value = address.id
    }

    fun addNewAddress(address: Address) {
        viewModelScope.launch {
            val uid = getCurrentUserUseCase()?.id
            insertAddressUS(address.copy(userUid = uid))
            uid?.let { syncAddressesUseCase(it) }
        }
    }

    fun deleteAddress(addressId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                deleteAddressUS(addressId)
                val uid = getCurrentUserUseCase()?.id
                if (_selectedAddressId.value == addressId) {
                    _selectedAddressId.value = null
                }
                uid?.let { syncAddressesUseCase(it) }
                _uiEvent.emit(AddressUiEvent.ShowMessage("Đã xóa địa chỉ"))
            } catch (e: Exception) {
                Log.e("AddressViewModel", "Error deleting address", e)
                _uiEvent.emit(AddressUiEvent.ShowMessage("Lỗi khi xóa địa chỉ: ${e.message}"))
            } finally {
                _uiState.update {
                    it.copy(isLoading = false)
                }
            }
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
            //tạo chuỗi địa chỉ chi tiết
            val addressString = listOf(
                current.detailAddress.trim(),
                current.selectedWard?.name,
                current.selectedDistrict?.name,
                current.selectedProvince?.name
            ).filter { it != null }.joinToString(", ")

            //thêm vào database
            val address = Address(
                id = current.editId ?: 0,
                userUid = getCurrentUserUseCase()?.id,
                name = current.fullName,
                phone = current.phone,
                detailAddress = current.detailAddress,
                addressString = addressString,
                isDefault = current.isDefault,
                provinceCode = current.selectedProvince?.code ?: 0,
                districtCode = current.selectedDistrict?.code ?: 0,
                wardCode = current.selectedWard?.code ?: 0
            )

            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }
                try {
                    val uid = getCurrentUserUseCase()?.id
                    if (current.isEdit) {
                        updateAddressUS(address)
                        if (current.isDefault) {
                            setDefaultAddressUS(address.id)
                        }
                        _uiEvent.emit(AddressUiEvent.ShowMessage("Cập nhật thành công"))
                    } else {
                        val newId = insertAddressUS(address)
                        if (current.isDefault) {
                            setDefaultAddressUS(newId)
                        }
                        _uiEvent.emit(AddressUiEvent.ShowMessage("Đã thêm 1 địa chỉ"))
                    }
                    uid?.let { syncAddressesUseCase(it) }
                } catch (e: Exception) {
                    Log.e("AddressViewModel", "Error saving address", e)
                    _uiEvent.emit(AddressUiEvent.ShowMessage("Lỗi khi lưu địa chỉ: ${e.message}"))
                } finally {
                    _uiState.update { it.copy(isLoading = false) }
                }
                _uiState.update { it.copy(success = true) }
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
