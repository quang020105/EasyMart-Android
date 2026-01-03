package com.example.easymart.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
                LoginRoute(
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
                SignUpRoute(
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
                HomeRoute(
                    onNavigateToProduct = { product ->
                        navController.navigate(Screen.ProductDetail.createRoute(product.id))
                    }
                )
            }
            composable(Screen.Cart.route) { backStackEntry ->
                //dùng chung 1 viewModel cho màn cart và checkout
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
            composable(
                Screen.ProductDetail.route,
                arguments = listOf(navArgument("productId") { type = NavType.IntType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getInt("productId")
                if (productId == null) {
                    //todo
                } else {
                    val viewModel = hiltViewModel<ProductDetailViewModel>(backStackEntry)
                    LaunchedEffect(productId) {
                        viewModel.loadProduct(productId)
                    }
                    ProductDetailRoute(viewModel = viewModel)
                }
            }

            composable(Screen.Checkout.route) { backStackEntry ->
                //dùng chung viewModel
                val parentEntry =
                    remember { navController.getBackStackEntry(Screen.HomeGraph.route) }
                val cartVM = hiltViewModel<CartViewModel>(parentEntry)
                val addressVM = hiltViewModel<AddressViewModel>(parentEntry)
                val paymentVM = hiltViewModel<SelectPaymentViewModel>(parentEntry)
                val checkoutVM = hiltViewModel<CheckoutViewModel>(parentEntry)
                CheckOutRoute(
                    checkoutViewModel = checkoutVM,
                    cartViewModel = cartVM,
                    addressViewModel = addressVM,
                    paymentViewModel = paymentVM,
                    onNavigateToAddress = {
                        navController.navigate(Screen.DeliveryAddress.route)
                    },
                    onNavigateToPayment = {
                        navController.navigate(Screen.PaymentMethod.route)
                    },
                    onNavigateToSuccess = {
                        navController.navigate(
//                            Screen.OrderSuccess.createRoute(
//                                orderId,
//                                totalAmount,
//                                paymentMethod.name
//                            )
                            Screen.OrderSuccess.route
                        )
                    },
                    onNavigateToOnlineProcessing = {
                        navController.navigate(
                            Screen.OnlinePaymentProcessing.route
                        )
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

            composable(
                Screen.OrderSuccess.route
            ) { backStackEntry ->
                val parentEntry =
                    remember { navController.getBackStackEntry(Screen.HomeGraph.route) }
                val viewModel = hiltViewModel<CheckoutViewModel>(parentEntry)
                val uiState = viewModel.uiState.collectAsState()
                val order = uiState.value.order
                OrderSuccessRoute(
                    orderId = order?.id ?: -1,
                    totalAmount = order?.totalAmount ?: 0L,
                    paymentMethod = order?.paymentMethod ?: PaymentMethod.COD,
                    onViewOrderClick = {

                    },
                    onContinueShoppingClick = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.HomeGraph.route) {
                                inclusive = true
                            }
                        }
                    }
                )

            }

            composable(
                route = "payment_failed/{orderId}/{reason}",
                arguments = listOf(
                    navArgument("orderId") { type = NavType.IntType },
                    navArgument("reason") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val orderId = backStackEntry.arguments?.getInt("orderId") ?: -1
                val message = backStackEntry.arguments?.getString("reason")
                if (orderId != -1) {
                    PaymentFailedRoute(
                        message = message ?: "Thanh toán không thành công",
                        onRetry = {},
                        onChangePaymentMethod = {},
                        onBackToCheckOut = {}
                    )
                }
            }

            composable(Screen.OnlinePaymentProcessing.route) {
                OnlinePaymentProcessingRoute(
                    onPaymentFinished = {
                        navController.navigate(
                            Screen.OrderSuccess.route
                        )
                    },
                )
            }


            composable(Screen.DeliveryAddress.route) { backStackEntry ->
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

            composable(Screen.PaymentMethod.route) {
                val parentEntry =
                    remember { navController.getBackStackEntry(Screen.HomeGraph.route) }
                val viewModel = hiltViewModel<SelectPaymentViewModel>(parentEntry)

                SelectPaymentRoute(
                    viewModel = viewModel,
                    onNavigateBack = { navController.navigateUp() }
                )
            }

            composable(Screen.AddAddress.route) { backStackEntry ->
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

            composable(
                route = "${Screen.AddAddress.route}/{addressId}",
                arguments = listOf(navArgument("addressId") {
                    type = NavType.IntType
                    defaultValue = -1
                    nullable = false
                })
            ) { backStackEntry ->
                val addressId = backStackEntry.arguments?.getInt("addressId") ?: -1
                val parentEntry =
                    remember { navController.getBackStackEntry(Screen.HomeGraph.route) }
                val viewModel = hiltViewModel<AddressViewModel>(parentEntry)
                if (addressId > 0) {
                    LaunchedEffect(addressId) {
                        viewModel.loadAddress(addressId)
                    }
                }
                AddAddressRoute(
                    viewModel = viewModel,
                    onNavigateBack = {
                        navController.navigateUp()
                    }
                )
            }


            composable(Screen.Search.route) {
                SearchRoute(
                    onNavigateBack = {
                        navController.navigateUp()
                    },
                    onNavigateToProduct = { product ->
                        navController.navigate(Screen.ProductDetail.createRoute(product.id))
                    }
                )
            }
        }


        //nav profile
        navigation(startDestination = Screen.Profile.route, route = Screen.ProfileGraph.route) {
            composable(Screen.Profile.route) {
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
            composable(Screen.Category.route) {

            }
        }

        //nav search
        navigation(startDestination = Screen.Search.route, route = Screen.SearchGraph.route) {
            composable(Screen.Search.route) {

            }
        }


    }
}
