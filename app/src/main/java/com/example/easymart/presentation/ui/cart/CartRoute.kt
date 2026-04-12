package com.example.easymart.presentation.ui.cart

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
    val uiState by viewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest { event ->
            when(event) {
                is CartEvent.ShowMessage -> snackBarHostState.showSnackbar(event.message)
            }
        }
    }

    //Log.d("HomeRoute", "ShowMessage: ${cartItems.value}")
    Log.d("HomeRoute", "CartViewModel instance hash=${viewModel.hashCode()}")
    CartScreen(
        cartItems = uiState.items,
        selectedItems = uiState.selectedItems,
        pendingRemoveItem = uiState.pendingRemoveItem,
        onConfirmRemove = { viewModel.confirmRemovePendingItem() },
        onCancelRemove = { viewModel.cancelRemovePendingItem() },
        onCheckOutClick = {
            // Lưu danh sách sản phẩm đã chọn trước khi điều hướng sang checkout
            viewModel.saveSelectedItemsForCheckout()
            onCheckOutClick()
        },
        onCartItemClick = onCartItemClick,
        subtotal = uiState.subtotal,
        shipping = uiState.shipping,
        total = uiState.total,
        onPlusClick = { cartItem -> viewModel.updateQuantity(cartItem,1)},
        onMinusClick = { cartItem -> viewModel.updateQuantity(cartItem, -1)},
        onCheckedChange = { cartItem, checked -> viewModel.onCheckChanged(cartItem.id, checked)},
        onChangeCheckedAll = { checked -> viewModel.onCheckedChangeAll(checked)},
        allChecked = uiState.checkedAll
    )
}