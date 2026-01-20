package com.example.easymart.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.presentation.ui.cart.CartRoute
import com.example.easymart.presentation.ui.cart.CartViewModel
import com.example.easymart.presentation.ui.checkout.CheckOutRoute
import com.example.easymart.presentation.ui.checkout.CheckoutViewModel
import com.example.easymart.presentation.ui.deliveryaddress.AddAddressRoute
import com.example.easymart.presentation.ui.deliveryaddress.AddressViewModel
import com.example.easymart.presentation.ui.deliveryaddress.DeliveryAddressRoute
import com.example.easymart.presentation.ui.home.HomeRoute
import com.example.easymart.presentation.ui.login.LoginRoute
import com.example.easymart.presentation.ui.order.OrderRoute
import com.example.easymart.presentation.ui.order.OrderViewModel
import com.example.easymart.presentation.ui.orderdetail.OrderDetailRoute
import com.example.easymart.presentation.ui.orderdetail.OrderDetailViewModel
import com.example.easymart.presentation.ui.payment.SelectPaymentRoute
import com.example.easymart.presentation.ui.payment.SelectPaymentViewModel
import com.example.easymart.presentation.ui.productdetail.ProductDetailRoute
import com.example.easymart.presentation.ui.productdetail.ProductDetailViewModel
import com.example.easymart.presentation.ui.profile.ProfileRoute
import com.example.easymart.presentation.ui.resultorder.OnlinePaymentProcessingRoute
import com.example.easymart.presentation.ui.resultorder.OrderSuccessRoute
import com.example.easymart.presentation.ui.resultorder.PaymentFailedRoute
import com.example.easymart.presentation.ui.search.SearchRoute
import com.example.easymart.presentation.ui.signup.SignUpRoute
import com.example.easymart.presentation.ui.splash.SplashScreen

@SuppressLint("UnrememberedGetBackStackEntry")
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
        composableWithAnim(
            route = Screen.Splash.route,
            anim = NavAnim.FADE
        ) {
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

            composableWithAnim(
                route = Screen.Login.route,
                anim = NavAnim.FADE
            ) {
                LoginRoute(
                    onNavigateToSignUp = {
                        navController.navigate(Screen.SignUp.route)
                    },
                    onLoginClick = { _, _, _ ->
                        navController.navigate(Screen.HomeGraph.route) {
                            popUpTo(Screen.AuthGraph.route) { inclusive = true }
                        }
                    }
                )
            }


            composableWithAnim(
                route = Screen.SignUp.route,
                anim = NavAnim.FADE
            ) {
                SignUpRoute(
                    onSignUpClick = { _, _, _, _ -> navController.navigateUp() },
                    onNavigateToLogin = { navController.navigateUp() }
                )
            }

        }


        //nav home
        navigation(startDestination = Screen.Home.route, route = Screen.HomeGraph.route) {
            composableWithAnim(
                route = Screen.Home.route,
                anim = NavAnim.NONE
            ) {
                HomeRoute(
                    onNavigateToProduct = { product ->
                        navController.navigate(Screen.ProductDetail.createRoute(product.id))
                    }
                )
            }

            composableWithAnim(
                route = Screen.Cart.route,
                anim = NavAnim.HORIZONTAL
            ) { backStackEntry ->
                val parentEntry =
                    remember { navController.getBackStackEntry(Screen.HomeGraph.route) }
                val viewModel = hiltViewModel<CartViewModel>(parentEntry)

                CartRoute(
                    viewModel = viewModel,
                    onCheckOutClick = {
                        navController.navigate(Screen.Checkout.route)
                    }
                )
            }


            composableWithAnim(
                route = Screen.ProductDetail.route,
                anim = NavAnim.HORIZONTAL,
                arguments = listOf(navArgument("productId") { type = NavType.IntType })
            ) { backStackEntry ->

                val productId =
                    backStackEntry.arguments?.getInt("productId") ?: return@composableWithAnim
                val viewModel = hiltViewModel<ProductDetailViewModel>(backStackEntry)

                LaunchedEffect(productId) {
                    viewModel.loadProduct(productId)
                }

                ProductDetailRoute(viewModel = viewModel)
            }


            composableWithAnim(
                route = Screen.Checkout.route,
                anim = NavAnim.HORIZONTAL
            ) {
                val parentEntry =
                    remember { navController.getBackStackEntry(Screen.HomeGraph.route) }

                CheckOutRoute(
                    checkoutViewModel = hiltViewModel(parentEntry),
                    cartViewModel = hiltViewModel(parentEntry),
                    addressViewModel = hiltViewModel(parentEntry),
                    paymentViewModel = hiltViewModel(parentEntry),
                    onNavigateToAddress = {
                        navController.navigate(Screen.DeliveryAddress.route)
                    },
                    onNavigateToPayment = {
                        navController.navigate(Screen.SelectPaymentMethod.route)
                    },
                    onNavigateToSuccess = {
                        navController.navigate(Screen.OrderSuccess.route)
                    },
                    onNavigateToOnlineProcessing = {
                        navController.navigate(Screen.OnlinePaymentProcessing.route)
                    },
                    onNavigateToPaymentFailed = { orderId, reason ->
                        navController.navigate(
                            Screen.PaymentFailed.createRoute(orderId, reason)
                        )
                    }
                )
            }


//            composable(
//                "${Screen.OrderSuccess.route}/{orderId}/{totalAmount}/{paymentMethod}",
//                arguments = listOf(
//                    navArgument("orderId") { type = NavType.IntType },
//                    navArgument("totalAmount") { type = NavType.LongType },
//                    navArgument("paymentMethod") { type = NavType.StringType })
//            ) { backStackEntry ->
//                val orderId = backStackEntry.arguments?.getInt("orderId") ?: -1
//                val totalAmount = backStackEntry.arguments?.getLong("totalAmount") ?: 0L
//                val paymentMethodStr = PaymentMethod.valueOf(
//                    backStackEntry.arguments?.getString("paymentMethod") ?: PaymentMethod.COD.name
//                )
//                OrderSuccessRoute(
//                    orderId = orderId,
//                    totalAmount = totalAmount,
//                    paymentMethod = paymentMethodStr,
//                    onViewOrderClick = {
//
//                    },
//                    onContinueShoppingClick = {
//                        navController.navigate(Screen.Home.route) {
//                            popUpTo(Screen.HomeGraph.route) {
//                                inclusive = true
//                            }
//                        }
//                    }
//                )


            composableWithAnim(
                route = Screen.OrderSuccess.route,
                anim = NavAnim.FADE
            ) {
                val parentEntry =
                    remember { navController.getBackStackEntry(Screen.HomeGraph.route) }
                val viewModel = hiltViewModel<CheckoutViewModel>(parentEntry)

                OrderSuccessRoute(
                    checkoutViewModel = viewModel,
                    onViewOrderClick = {},
                    onContinueShoppingClick = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.HomeGraph.route) { inclusive = true }
                        }
                    }
                )
            }


            composableWithAnim(
                route = Screen.PaymentFailed.route,
                anim = NavAnim.FADE,
                arguments = listOf(
                    navArgument("orderId") { type = NavType.IntType },
                    navArgument("reason") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val message = backStackEntry.arguments?.getString("reason")

                PaymentFailedRoute(
                    message = message ?: "Thanh toán không thành công",
                    onRetry = {},
                    onChangePaymentMethod = {},
                    onBackToCheckOut = {}
                )
            }

            composableWithAnim(
                route = Screen.OnlinePaymentProcessing.route,
                anim = NavAnim.FADE
            ) {
                OnlinePaymentProcessingRoute(
                    onPaymentFinished = {
                        navController.navigate(Screen.OrderSuccess.route)
                    }
                )
            }


            composableWithAnim(
                route = Screen.DeliveryAddress.route,
                anim = NavAnim.MODAL
            ) {
                val parentEntry =
                    remember { navController.getBackStackEntry(Screen.HomeGraph.route) }
                val viewModel = hiltViewModel<AddressViewModel>(parentEntry)

                DeliveryAddressRoute(
                    viewModel,
                    onAddressAddClick = {
                        navController.navigate(Screen.AddAddress.route)
                    },
                    onAddressEditClick = { addressId ->
                        navController.navigate(Screen.AddAddress.createRoute(addressId))
                    },
                    onAddressDeleteClick = { addressId ->
                        viewModel.deleteAddress(addressId)
                    },
                    onAddressClick = { address ->
                        viewModel.selectAddress(address)
                        navController.navigateUp()
                    }
                )
            }


            composableWithAnim(
                route = Screen.SelectPaymentMethod.route,
                anim = NavAnim.MODAL
            ) {
                val parentEntry =
                    remember { navController.getBackStackEntry(Screen.HomeGraph.route) }
                //val viewModel = hiltViewModel<SelectPaymentViewModel>(parentEntry)

                SelectPaymentRoute(
                    selectPaymentViewModel = hiltViewModel(parentEntry),
                    cartViewModel = hiltViewModel(parentEntry),
                    onNavigateBack = { navController.navigateUp() }
                )
            }


            composableWithAnim(
                route = Screen.AddAddress.route,
                anim = NavAnim.MODAL
            ) {
                val parentEntry =
                    remember { navController.getBackStackEntry(Screen.HomeGraph.route) }
                val viewModel = hiltViewModel<AddressViewModel>(parentEntry)

                LaunchedEffect(Unit) {
                    viewModel.resetForm()
                }

                AddAddressRoute(
                    viewModel = viewModel,
                    onNavigateBack = { navController.navigateUp() }
                )
            }


            composableWithAnim(
                route = "${Screen.AddAddress.route}/{addressId}",
                anim = NavAnim.MODAL,
                arguments = listOf(
                    navArgument("addressId") {
                        type = NavType.IntType
                        defaultValue = -1
                    }
                )
            ) { backStackEntry ->

                val addressId = backStackEntry.arguments?.getInt("addressId") ?: -1
                val parentEntry =
                    remember { navController.getBackStackEntry(Screen.HomeGraph.route) }
                val viewModel = hiltViewModel<AddressViewModel>(parentEntry)

                LaunchedEffect(addressId) {
                    if (addressId > 0) viewModel.loadAddress(addressId)
                }

                AddAddressRoute(
                    viewModel = viewModel,
                    onNavigateBack = { navController.navigateUp() }
                )
            }


            composableWithAnim(
                route = Screen.Search.route,
                anim = NavAnim.HORIZONTAL
            ) {
                SearchRoute(
                    onNavigateBack = { navController.navigateUp() },
                    onNavigateToProduct = { product ->
                        navController.navigate(Screen.ProductDetail.createRoute(product.id))
                    }
                )
            }
        }


        //nav profile
        navigation(startDestination = Screen.Profile.route, route = Screen.ProfileGraph.route) {
            composableWithAnim(
                Screen.Profile.route,
                anim = NavAnim.NONE
            ) {
                ProfileRoute(
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


        //nav category
        navigation(startDestination = Screen.Category.route, route = Screen.CategoryGraph.route) {
            composableWithAnim(
                route = Screen.Category.route,
                anim = NavAnim.NONE
            ) {
                // CategoryRoute()
            }
        }

        //nav search
        navigation(startDestination = Screen.Search.route, route = Screen.SearchGraph.route) {
            composableWithAnim(
                route = Screen.Search.route,
                anim = NavAnim.HORIZONTAL
            ) {
                // SearchRoute()
            }

        }


        //màn đơn hàng của tôi
        composableWithAnim(
            route = Screen.Order.route,
            anim = NavAnim.HORIZONTAL
        ){
            val viewModel = hiltViewModel<OrderViewModel>()
            OrderRoute(
                viewModel,
                onNavigateToOrderDetail = { orderId ->
                    navController.navigate(Screen.OrderDetail.createRoute(orderId))
                },
                onNavigateToTrack = {},
                onNavigateToBuyAgain = {}
            )
        }

        //chi tiết đơn hàng
        composableWithAnim(
            route = Screen.OrderDetail.route,
            anim = NavAnim.HORIZONTAL,
            arguments = listOf(
                navArgument("orderId") { type = NavType.IntType }
            )
        ){
            val viewModel = hiltViewModel<OrderDetailViewModel>()
            val orderId = it.arguments?.getInt("orderId") ?: return@composableWithAnim
            LaunchedEffect(orderId) {
                viewModel.loadOrderDetail(orderId)
            }
            OrderDetailRoute(viewModel)
        }

    }
}



