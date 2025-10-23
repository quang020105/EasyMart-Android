package com.example.easymart.presentation.ui.mock

import com.example.easymart.R
import com.example.easymart.domain.model.Product

val mockProducts = List(10){ idx ->
    Product(
        id = idx,
        name = "Product $idx",
        description = "This is the description for product $idx.",
        price = (100000..3000000).random().toDouble(),
        imageUrl = "https://via.placeholder.com/150",
        imageRes = R.drawable.pic_shoe_1
    )
}