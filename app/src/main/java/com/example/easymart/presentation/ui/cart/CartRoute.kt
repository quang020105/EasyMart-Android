package com.example.easymart.presentation.ui.cart

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.easymart.domain.model.Product
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CartRoute(
    viewModel: CartViewModel,
    onCheckOutClick: () -> Unit,
    onCartItemClick: (Product) -> Unit = {},
){
    val cartItems = viewModel.cartItems.collectAsState()
    val selectedItems = viewModel.selectedItems.collectAsState(initial = emptyList())
    val subTotal = viewModel.subtotal.collectAsState()
    val shipping = viewModel.shipping.collectAsState()
    val total = viewModel.total.collectAsState()
    val checkedAll = viewModel.checkedAll.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest { event ->
            when(event) {
                is CartEvent.ShowMessage -> snackBarHostState.showSnackbar(event.message)
            }
        }
    }

    Log.d("HomeRoute", "ShowMessage: ${cartItems.value}")
    Log.d("HomeRoute", "CartViewModel instance hash=${viewModel.hashCode()}")
    CartScreen(
        cartItems = cartItems.value,
        selectedItems = selectedItems.value,
        onCheckOutClick = onCheckOutClick,
        onCartItemClick = onCartItemClick,
        subtotal = subTotal.value,
        shipping = shipping.value,
        total = total.value,
        onPlusClick = { cartItem -> viewModel.updateQuantity(cartItem,1)},
        onMinusClick = { cartItem -> viewModel.updateQuantity(cartItem, -1)},
        onCheckedChange = { cartItem, checked -> viewModel.onCheckChanged(cartItem.id, checked)},
        onChangeCheckedAll = { checked -> viewModel.onCheckedChangeAll(checked)},
        allChecked = checkedAll.value
    )
}