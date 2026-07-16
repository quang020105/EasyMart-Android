package com.example.easymart.presentation.ui.mock

import com.example.easymart.domain.model.CartItem

val mockCartItems = List(5){ idx ->
    CartItem(
        id = idx,
        product = mockSimpleProduct,
        quantity = (1..10).random(),
        unitPriceVnd = mockSimpleProduct.priceVnd
    )
}

val mockSimpleCartItems = CartItem(
    id = 0,
    product = mockSimpleProduct,
    quantity = (1..10).random(),
    unitPriceVnd = mockSimpleProduct.priceVnd
)
