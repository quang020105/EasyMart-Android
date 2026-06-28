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

enum class CustomerProductPriceFilter(
    val label: String,
    val description: String,
    val minVnd: Double? = null,
    val maxVnd: Double? = null
) {
    ALL("Tất cả giá", "Không giới hạn khoảng giá"),
    UNDER_500K("Dưới 500K", "Sản phẩm tiết kiệm", maxVnd = 500_000.0),
    FROM_500K_TO_1M("500K - 1 triệu", "Phân khúc phổ biến", minVnd = 500_000.0, maxVnd = 1_000_000.0),
    FROM_1M_TO_3M("1 - 3 triệu", "Lựa chọn tầm trung", minVnd = 1_000_000.0, maxVnd = 3_000_000.0),
    OVER_3M("Trên 3 triệu", "Sản phẩm cao cấp", minVnd = 3_000_000.0)
}

enum class CustomerProductRatingFilter(
    val label: String,
    val description: String,
    val minRate: Double
) {
    ALL("Mọi đánh giá", "Hiển thị tất cả sản phẩm", 0.0),
    FOUR_PLUS("Từ 4 sao", "Được khách hàng đánh giá cao", 4.0),
    THREE_PLUS("Từ 3 sao", "Chất lượng ổn định", 3.0),
    TWO_PLUS("Từ 2 sao", "Có đánh giá cơ bản", 2.0)
}
