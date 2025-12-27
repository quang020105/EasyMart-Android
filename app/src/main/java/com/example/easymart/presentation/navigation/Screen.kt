package com.example.easymart.presentation.navigation

sealed class Screen(val route: String){
    data object Splash: Screen("splash")
    //graph auth
    data object AuthGraph: Screen("auth_graph")
    data object Login: Screen("login")
    data object SignUp: Screen("signup")

    //graph home
    data object HomeGraph: Screen("home_graph")
    data object Home: Screen("home")
    data object Cart: Screen("cart")
    data object Checkout: Screen("checkout")
    data object ProductDetail: Screen("product/{productId}"){
        fun createRoute(productId: Int) = "product/$productId"
    }

    //graph category
    data object CategoryGraph: Screen("category_graph")
    data object Category: Screen("category")

    //graph profile
    data object ProfileGraph: Screen("profile_graph")
    data object Profile: Screen("profile")
    data object Order: Screen("order")
    data object ItemOrderStatus: Screen("order_status/{status}"){
        fun createRoute(status: String) = "order_status/$status"
    }
    data object DeliveryAddress: Screen("delivery_address")
    data object PaymentMethod: Screen("payment_method")
    data object AddAddress: Screen("add_address"){
        fun createRoute(addressId: Int) = "add_address/$addressId"
    }

    //graph search
    data object SearchGraph: Screen("search_graph")
    data object Search: Screen("search")
}