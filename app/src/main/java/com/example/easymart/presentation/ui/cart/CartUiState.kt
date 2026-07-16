package com.example.easymart.presentation.ui.cart

import com.example.easymart.domain.model.CartItem
import com.example.easymart.domain.shipping.ShippingFeePolicy

data class CartUiState(
    val isLoading: Boolean = false,
    val items: List<CartItem> = emptyList(),
    val checkedAll: Boolean = false,
    val pendingRemoveItem: CartItem? = null,
    val pendingRemoveSelected: Boolean = false
) {
    val selectedItems
        get() = items.filter { it.isChecked }

    val subtotal
        get() = selectedItems.sumOf { it.totalPriceVnd }

    val shipping
        get() = ShippingFeePolicy.calculate(
            subtotalVnd = subtotal,
            totalItemQuantity = selectedItems.sumOf { it.quantity }
        )

    val total
        get() = subtotal + shipping
    val isEmpty get() = !isLoading && items.isEmpty()
}
