package com.example.easymart.presentation.ui.deliveryaddress

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import com.example.easymart.domain.model.District
import com.example.easymart.domain.model.Province
import com.example.easymart.domain.model.Ward
import kotlinx.coroutines.launch

@Composable
fun AddAddressRoute(
    viewModel: AddressViewModel,
    onNavigateBack: () -> Unit = {}
){
    val uiState = viewModel.uiState.collectAsState()
    val isFormValid = viewModel.isFormValid.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    viewModel.loadProvinces()
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { innerPadding ->
        val padding = innerPadding
        AddAddressScreen(
            state = uiState.value,
            isFormValid = isFormValid.value,
            events = object : AddAddressUiEvents {
                override fun onFullNameChange(v: String) {
                    viewModel.onFullNameChange(v)
                }

                override fun onPhoneChange(v: String) {
                    viewModel.onPhoneNumberChange(v)
                }

                override fun onDetailAddressChange(v: String) {
                    viewModel.onDetailAddressChange(v)
                }

                override fun onProvinceSelected(p: Province?) {
                    viewModel.onProvinceSelected(p)
                }

                override fun onDistrictSelected(d: District?) {
                    viewModel.onDistrictSelected(d)
                }

                override fun onWardSelected(w: Ward?) {
                    viewModel.onWardsSelected(w)
                }

                override fun onDefaultToggle(v: Boolean) {
                    viewModel.onToggleDefault(v)
                }

                override fun onSave() {
                    viewModel.onSave()
                }

                override fun onCancel() {
                    TODO("Not yet implemented")
                }

            }
        )

        //thông báo
        if(uiState.value.success){
            LaunchedEffect(uiState.value.success) {
                //chạy coroutine con tránh block main thread
//                launch {
//                    snackBarHostState.showSnackbar("Thêm địa chỉ thành công")
//                }
                viewModel.clearSuccess()
                viewModel.resetForm()
                //quay lại nếu thêm thành công
                onNavigateBack()
            }
        }
    }
}