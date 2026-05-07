package com.example.easymart.presentation.ui.mock

import com.example.easymart.R
import com.example.easymart.domain.model.CartItem
import com.example.easymart.domain.model.Product
import com.example.easymart.domain.model.ProductRating


val mockProducts = List(10){ idx ->
    Product(
        id = idx,
        name = "Product $idx",
        description = "This is the description for product $idx.",
        price = (100000..3000000).random().toDouble(),
        imageUrl = "https://via.placeholder.com/150",
        imageRes = R.drawable.pic_shoe_1,
        category = listOf("Điện thoại", "Laptop", "Phụ kiện").random(),
        rating = ProductRating(rate = 4.2, count = 120 + idx)
    )
}

val mockSimpleProduct = Product(
    id = 1,
    name = "Product 1",
    description = "This is the description for product 1.",
    price = 100000.0,
    imageUrl = "",
    imageRes = R.drawable.pic_shoe_1,
    category = "Phụ kiện",
    rating = ProductRating(rate = 3.9, count = 120)
)
