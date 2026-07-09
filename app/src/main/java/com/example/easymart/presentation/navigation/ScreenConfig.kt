package com.example.easymart.presentation.navigation

data class ScreenConfig(
    val title: String? = null,
    val showTopBar: Boolean = false,
    val showBottomBar: Boolean = false
)

private val screenConfigs: List<Pair<String, ScreenConfig>> = listOf(
    "splash" to ScreenConfig(showTopBar = false, showBottomBar = false),
    "login" to ScreenConfig(title = "Đăng nhập", showTopBar = false),
    "signup" to ScreenConfig(title = "Đăng ký", showTopBar = false),
    "forgot_password" to ScreenConfig(title = "Quên mật khẩu", showTopBar = false),

    // home graph
    "home" to ScreenConfig(showTopBar = true, showBottomBar = true),
    "search" to ScreenConfig(showTopBar = true, showBottomBar = true),
    "image_search" to ScreenConfig(title = "Tìm kiếm bằng ảnh", showTopBar = true, showBottomBar = false),
    "category" to ScreenConfig(showTopBar = true, showBottomBar = true),
    "profile" to ScreenConfig(showTopBar = true, showBottomBar = true),

    //
    "cart" to ScreenConfig(title = "Giỏ hàng", showTopBar = true),
    "checkout" to ScreenConfig(title = "Thanh toán", showTopBar = true),
    "delivery_address" to ScreenConfig(title = "Địa chỉ giao hàng", showTopBar = true),
    "add_address" to ScreenConfig(title = "Thêm địa chỉ", showTopBar = true),
    "payment_method" to ScreenConfig(title = "Phương thức thanh toán", showTopBar = true),
    "order" to ScreenConfig(title = "Đơn hàng của tôi", showTopBar = true),

    // admin
    "admin_dashboard" to ScreenConfig(title = "Admin Dashboard", showTopBar = true, showBottomBar = false),
    "admin_products" to ScreenConfig(title = "Quản lý sản phẩm", showTopBar = true, showBottomBar = false),
    "admin_orders/detail" to ScreenConfig(title = "Chi tiết đơn hàng", showTopBar = true, showBottomBar = false),
    "admin_orders" to ScreenConfig(title = "Quản lý đơn hàng", showTopBar = true, showBottomBar = false),
    "admin_categories" to ScreenConfig(title = "Quản lý danh mục", showTopBar = true, showBottomBar = false),

    "order_success" to ScreenConfig(title = "Đặt hàng thành công", showTopBar = true),
    "payment_failed" to ScreenConfig(title = "Thanh toán thất bại", showTopBar = true),
    "online_payment_processing" to ScreenConfig(showTopBar = false, showBottomBar = false),
    "payos_return" to ScreenConfig(showTopBar = false, showBottomBar = false),
    "payos_cancel" to ScreenConfig(showTopBar = false, showBottomBar = false),

    // product detail:
    "product" to ScreenConfig(showTopBar = true, showBottomBar = false)
)

fun getScreenConfig(route: String?): ScreenConfig? {
    if(route == null) return null
    // trả về cấu hình tương ứng với route (hỗ trợ cả các route có tham số)
    return screenConfigs.firstOrNull { (prefix,_ ) ->
        route.startsWith(prefix)
    }?.second
}
