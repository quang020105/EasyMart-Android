package com.example.easymart.presentation.ui.cart

import com.example.easymart.domain.model.CartItem

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
        get() = selectedItems.sumOf { it.quantity * 15_000L }

    val total
        get() = subtotal + shipping
    val isEmpty get() = !isLoading && items.isEmpty()
}
