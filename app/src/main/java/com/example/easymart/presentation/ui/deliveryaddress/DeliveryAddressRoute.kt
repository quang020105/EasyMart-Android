package com.example.easymart.presentation.ui.deliveryaddress

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.example.easymart.domain.model.Address

@Composable
fun DeliveryAddressRoute(
    viewModel: AddressViewModel,
    onAddressAddClick: () -> Unit,
    onAddressEditClick: (addressId: Int) -> Unit,
    onAddressDeleteClick: (addressId: Int) -> Unit,
    onAddressClick: (Address) -> Unit
) {

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val address = viewModel.addresses.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AddressUiEvent.ShowMessage -> {
                    snackBarHostState.showSnackbar(event.message)
                    viewModel.clearUiEvent()
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { padding ->
        val p = padding
        DeliveryAddressScreen(
            addresses = address.value,
            onAddressAddClick = onAddressAddClick,
            onAddressEditClick = onAddressEditClick,
            onAddressDeleteClick = onAddressDeleteClick,
            onAddressClick = onAddressClick
        )
    }
}
