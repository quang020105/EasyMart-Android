package com.example.easymart.presentation.ui.cart

import com.example.easymart.domain.model.CartItem

data class CartUiState(
    val items: List<CartItem> = emptyList(),
    val checkedAll: Boolean = false,
    val pendingRemoveItem: CartItem? = null
) {
    val selectedItems = items.filter { it.isChecked }

    val subtotal = selectedItems.sumOf { it.totalPrice }

    val shipping = selectedItems.sumOf { it.quantity * (15000.0 / 26333) }

    val total = subtotal + shipping
}