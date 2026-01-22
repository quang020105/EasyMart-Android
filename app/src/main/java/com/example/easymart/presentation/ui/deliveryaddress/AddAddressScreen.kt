package com.example.easymart.presentation.ui.deliveryaddress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.easymart.R
import com.example.easymart.domain.model.District
import com.example.easymart.domain.model.Province
import com.example.easymart.domain.model.Ward
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.common.components.RoundedActionButton
import com.example.easymart.presentation.ui.deliveryaddress.components.SearchableDropDown

@Composable
fun AddAddressScreen(
    state: AddAddressUIState = AddAddressUIState(),
    isFormValid: Boolean = false,
    events: AddAddressUiEvents
) {
    val dimens = LocalAppDimens.current


    // local UI state: đánh dấu đã "touched" (rời focus) để chỉ show lỗi sau khi user rời ô
    var fullNameTouched by remember { mutableStateOf(false) }
    var phoneTouched by remember { mutableStateOf(false) }
    var detailTouched by remember { mutableStateOf(false) }
    var locationTouched by remember { mutableStateOf(false) }

    var fullNameHadFocus by remember { mutableStateOf(false) }
    var phoneHadFocus by remember { mutableStateOf(false) }
    var detailHadFocus by remember { mutableStateOf(false) }

    // local error messages (UI-level) — sẽ hiển thị nếu field bị touched;
    // ngoài ra state còn có các lỗi do ViewModel set khi onSave() được gọi (state.fullNameError, ...)
    var localFullNameError by remember { mutableStateOf<String?>(null) }
    var localPhoneError by remember { mutableStateOf<String?>(null) }
    var localDetailError by remember { mutableStateOf<String?>(null) }
    var localLocationError by remember { mutableStateOf<String?>(null) }

    // cùng một regex dùng trong ViewModel (raw string để tránh lỗi escape)
    val phoneRegex = Regex("""^(0[1-9][0-9]{8}|\+84[1-9][0-9]{8})$""")

    fun validateFullName(value: String): String? =
        if (value.isBlank()) "Họ tên không được để trống !" else null

    fun validatePhone(value: String): String? =
        if (value.isBlank()) "Số điện thoại là bắt buộc"
        else if (!phoneRegex.matches(value)) "Số điện thoại không hợp lệ"
        else null

    fun validateDetail(value: String): String? =
        if (value.isBlank()) "Địa chỉ chi tiết là bắt buộc" else null

    fun validateLocation(prov: Province?, dist: District?, ward: Ward?): String? =
        if (prov == null || dist == null || ward == null) "Vui lòng chọn địa chỉ" else null


    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimens.spaceXl),
            verticalArrangement = Arrangement.spacedBy(dimens.spaceMd)
        ) {

            Column {
                // Tên người nhận
                OutlinedTextField(
                    value = state.fullName,
                    onValueChange = {
                        events.onFullNameChange(it)
                        // cập nhật lỗi local khi đã touched
                        if (fullNameTouched) localFullNameError = validateFullName(it)
                    },
                    label = { Text("Tên người nhận") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = dimens.space2xl)
                        .onFocusChanged { focusState ->
                            //nếu đã được focus thì đánh dấu đã từng focus
                            if (focusState.isFocused) {
                                fullNameHadFocus = true
                            } else {
                                if (fullNameHadFocus) {
                                    fullNameTouched = true
                                    localFullNameError = validateFullName(state.fullName)
                                }
                            }
                        },
                    singleLine = true
                )
                (state.fullNameError ?: localFullNameError)?.let { msg ->
                    Text(
                        modifier = Modifier.padding(start = dimens.spaceXs, top = dimens.spaceXs),
                        text = msg,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            // Số điện thoại
            Column {
                OutlinedTextField(
                    value = state.phone,
                    onValueChange = {
                        events.onPhoneChange(it)
                        if (phoneTouched) localPhoneError = validatePhone(it)
                    },
                    label = { Text("Số điện thoại") },
                    modifier = Modifier.fillMaxWidth()
                        .onFocusChanged{ focusState ->
                            if(focusState.isFocused){
                                phoneHadFocus = true
                            } else {
                                if(phoneHadFocus){
                                    phoneTouched = true
                                    localPhoneError = validatePhone(state.phone)
                                }
                            }
                        },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true
                )
                (state.phoneError ?: localPhoneError)?.let{ msg ->
                    Text(
                        modifier = Modifier.padding(start = dimens.spaceXs, top = dimens.spaceXs),
                        text = msg,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            // Địa chỉ chi tiết

            Column {
                OutlinedTextField(
                    value = state.detailAddress,
                    onValueChange = {
                        events.onDetailAddressChange(it)
                                    if(detailTouched) localDetailError = validateDetail(it)
                    },
                    label = { Text("Địa chỉ chi tiết") },
                    modifier = Modifier.fillMaxWidth()
                        .onFocusChanged{ focusState ->
                            if(focusState.isFocused){
                                detailHadFocus = true
                            } else {
                                if(detailHadFocus){
                                    detailTouched = true
                                    localDetailError = validateDetail(state.detailAddress)
                                }
                            }
                        },
                    singleLine = false,
                    maxLines = 3
                )
                (state.detailAddressError ?: localDetailError)?.let{ msg ->
                    Text(
                        modifier = Modifier.padding(start = dimens.spaceXs, top = dimens.spaceXs),
                        text = msg,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }



            Column(modifier = Modifier.fillMaxWidth()) {
                SearchableDropDown(
                    label = "Tỉnh/Thành phố *",
                    options = state.provinces,
                    selected = state.selectedProvince,
                    onSelect = { events.onProvinceSelected(it) },
                    itemLabel = { it.name }
                )

                Spacer(modifier = Modifier.height(8.dp))

                SearchableDropDown(
                    label = "Quận/Huyện *",
                    options = state.districts,
                    selected = state.selectedDistrict,
                    onSelect = { events.onDistrictSelected(it) },
                    itemLabel = { it.name },
                    enabled = state.districts.isNotEmpty()
                )


                Spacer(modifier = Modifier.height(8.dp))


                SearchableDropDown(
                    label = "Phường/Xã *",
                    options = state.wards,
                    selected = state.selectedWard,
                    onSelect = { events.onWardSelected(it) },
                    itemLabel = { it.name },
                    enabled = state.wards.isNotEmpty()
                )
            }


            // Checkbox mặc định
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = state.isDefault,
                    onCheckedChange = { events.onDefaultToggle(it) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary
                    )
                )
                Text("Đặt làm địa chỉ mặc định")
            }

            Spacer(modifier = Modifier.height(dimens.spaceLg))

            state.errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }

            RoundedActionButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.action_save_address),
                onClick = { events.onSave() },
                verticalPadding = dimens.spaceLg,
                enabled = isFormValid
            )
        }
    }
}

@Preview
@Composable
fun AddAddressScreenPreview() {
    EasyMartTheme {
        AddAddressScreen(
            events = object : AddAddressUiEvents {
                override fun onFullNameChange(v: String) {
                    TODO("Not yet implemented")
                }

                override fun onPhoneChange(v: String) {
                    TODO("Not yet implemented")
                }

                override fun onDetailAddressChange(v: String) {
                    TODO("Not yet implemented")
                }

                override fun onProvinceSelected(p: Province?) {
                    TODO("Not yet implemented")
                }

                override fun onDistrictSelected(d: District?) {
                    TODO("Not yet implemented")
                }

                override fun onWardSelected(w: Ward?) {
                    TODO("Not yet implemented")
                }

                override fun onDefaultToggle(v: Boolean) {
                    TODO("Not yet implemented")
                }

                override fun onSave() {
                    TODO("Not yet implemented")
                }

                override fun onCancel() {
                    TODO("Not yet implemented")
                }
            }
        )
    }
}
