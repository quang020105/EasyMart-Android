package com.example.easymart.presentation.ui.deliveryaddress

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

@Composable
fun DeliveryAddressRoute(
    viewModel: AddressViewModel,
    onAddressAddClick: () -> Unit,
    onAddressEditClick: (addressId: Int) -> Unit
){
    val address = viewModel.addresses.collectAsState()
    DeliveryAddressScreen(
        address = address.value,
        onAddressAddClick = onAddressAddClick,
        onAddressEditClick = onAddressEditClick
    )
}
