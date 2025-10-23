package com.example.easymart.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.ui.cart.CartScreen
import com.example.easymart.presentation.ui.checkout.CheckoutScreen
import com.example.easymart.presentation.ui.home.HomeScreen
import com.example.easymart.presentation.ui.login.LoginScreen
import com.example.easymart.presentation.ui.productdetail.ProductDetailScreen
import com.example.easymart.presentation.ui.profile.ProfileScreen
import com.example.easymart.presentation.ui.signup.SignUpScreen
import com.example.easymart.presentation.ui.splash.SplashScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onContinue = {
                    navController.navigate(Screen.Home.route)
                },
                onLogin = {
                    navController.navigate(Screen.AuthGraph.route)
                }
            )
        }


        //nav auth
        navigation(startDestination = Screen.Login.route, route = Screen.AuthGraph.route) {
            composable(Screen.Login.route) {
                LoginScreen(
                    onNavigateToSignUp = {
                        navController.navigate(Screen.SignUp.route)
                    },
                    onLoginClick = { _, _, _ ->
                        navController.navigate(Screen.HomeGraph.route) {
                            popUpTo(Screen.AuthGraph.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable(Screen.SignUp.route) {
                SignUpScreen(
                    onSignUpClick = { _, _, _, _ ->
                        navController.navigateUp()
                    },
                    onNavigateToLogin = {
                        navController.navigateUp()
                    }
                )
            }
        }


        //nav home
        navigation(startDestination = Screen.Home.route, route = Screen.HomeGraph.route) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onProductClick = { product ->
                        navController.navigate(Screen.ProductDetail.createRoute(product.id))
                    }
                )
            }
            composable(Screen.Cart.route) {
                CartScreen(
                    onCheckOutCLick = {
                        navController.navigate(Screen.Checkout.route)
                    }
                )
            }
            composable(
                Screen.ProductDetail.route,
                arguments = listOf(navArgument("productId") { type = NavType.StringType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId")
                ProductDetailScreen(
                    product = Product(
                        id = productId?.toInt() ?: 0,
                        name = "Giày sneakers",
                        description = "Đây là đôi giày sneakers thời trang, phù hợp cho cả nam và nữ. Với thiết kế hiện đại và thoải mái, đôi giày này sẽ là lựa chọn hoàn hảo cho các hoạt động hàng ngày cũng như các buổi dạo chơi cuối tuần.",
                        price = 40.55,
                        imageUrl = "",
                        imageRes = com.example.easymart.R.drawable.pic_shoe_1
                    )
                )
            }

            composable(Screen.Checkout.route) {
                CheckoutScreen(
                    onConfirmClick = {
                        //thanh toán xong,xử lý nav
                    }
                )
            }
        }


        //nav profile
        navigation(startDestination = Screen.Profile.route, route = Screen.ProfileGraph.route) {
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onOptionClick = { tag ->
                        when (tag) {
                            "logout" -> {
                                navController.navigate(Screen.AuthGraph.route)
                                {
                                    popUpTo(Screen.HomeGraph.route) {
                                        inclusive = true
                                    }
                                }
                            }
                            "orders" -> {
                                navController.navigate(Screen.Order.route) {
                                }
                            }
                            "address" -> {
                                navController.navigate(Screen.DeliveryAddress.route)
                            }
                            "payment" -> {
                            }
                            "settings" -> {
                            }
                        }
                    }
                )
            }
        }

    }
}
