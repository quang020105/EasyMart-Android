package com.example.easymart.presentation.ui.category.components

enum class CustomerProductSort(val label: String) {
    FEATURED("Nổi bật"),
    NAME_ASC("Tên A-Z"),
    PRICE_ASC("Giá tăng dần"),
    PRICE_DESC("Giá giảm dần"),
    SOLD_DESC("Bán chạy")
}

enum class CustomerProductStockFilter(val label: String) {
    ALL("Tất cả"),
    IN_STOCK("Còn hàng"),
    OUT_OF_STOCK("Hết hàng")
}
