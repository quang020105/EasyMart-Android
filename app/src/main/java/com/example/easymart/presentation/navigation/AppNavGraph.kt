package com.example.easymart.presentation.navigation
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.easymart.domain.model.Role
import com.example.easymart.presentation.auth.AuthState
import com.example.easymart.presentation.auth.AuthViewModel
import com.example.easymart.presentation.common.AppEventBus
import com.example.easymart.presentation.common.ui.LoginRequiredBottomSheet
import com.example.easymart.presentation.common.ui.UiEvent
import com.example.easymart.presentation.ui.admin.orders.detail.AdminOrderDetailRoute
import com.example.easymart.presentation.ui.admin.orders.management.AdminOrdersRoute
import com.example.easymart.presentation.ui.admin.dashboard.AdminDashboardRoute
import com.example.easymart.presentation.ui.admin.AdminPlaceholderScreen
import com.example.easymart.presentation.ui.cart.CartRoute
import com.example.easymart.presentation.ui.cart.CartViewModel
import com.example.easymart.presentation.ui.category.CategoryRoute
import com.example.easymart.presentation.ui.checkout.CheckOutRoute
import com.example.easymart.presentation.ui.checkout.CheckoutViewModel
import com.example.easymart.presentation.ui.deliveryaddress.AddAddressRoute
import com.example.easymart.presentation.ui.deliveryaddress.AddressViewModel
import com.example.easymart.presentation.ui.deliveryaddress.DeliveryAddressRoute
import com.example.easymart.presentation.ui.home.HomeRoute
import com.example.easymart.presentation.ui.image_search.ImageSearchRoute
import com.example.easymart.presentation.ui.auth.login.LoginRoute
import com.example.easymart.presentation.ui.auth.login.LoginViewModel
import com.example.easymart.presentation.ui.order.OrderRoute
import com.example.easymart.presentation.ui.order.OrderViewModel
import com.example.easymart.presentation.ui.orderdetail.OrderDetailRoute
import com.example.easymart.presentation.ui.orderdetail.OrderDetailViewModel
import com.example.easymart.presentation.ui.payment.SelectPaymentRoute
import com.example.easymart.presentation.ui.productdetail.ProductDetailRoute
import com.example.easymart.presentation.ui.productdetail.ProductDetailViewModel
import com.example.easymart.presentation.ui.profile.ProfileRoute
import com.example.easymart.presentation.ui.profile.ProfileViewModel
import com.example.easymart.presentation.ui.resultorder.OnlinePaymentProcessingRoute
import com.example.easymart.presentation.ui.resultorder.OrderSuccessRoute
import com.example.easymart.presentation.ui.resultorder.PaymentFailedRoute
import com.example.easymart.presentation.ui.search.SearchRoute
import com.example.easymart.presentation.ui.auth.signup.SignUpRoute
import com.example.easymart.presentation.ui.auth.signup.SignUpViewModel
import com.example.easymart.presentation.ui.splash.SplashScreen
import com.example.easymart.presentation.ui.auth.forgot_password.ForgotPasswordRoute
import com.example.easymart.presentation.ui.auth.forgot_password.ForgotPasswordViewModel
import com.example.easymart.presentation.ui.admin.products.add_edit.AdminAddEditProductRoute
import com.example.easymart.presentation.ui.admin.products.management.AdminProductsRoute
import com.example.easymart.presentation.ui.admin.products.detail.AdminProductDetailRoute

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    ///quản lí trạng thái đăng nhập
    val authViewModel = hiltViewModel<AuthViewModel>()
    val authState = authViewModel.authState.collectAsState()
    val context = LocalContext.current

    val isAdmin = (authState.value as? AuthState.LoggedIn)?.role == Role.ADMIN

    //quản lý bottom sheet
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showLoginSheet by remember { mutableStateOf(false) }
    var pendingRoute by remember { mutableStateOf<String?>(null) }

    // xử lí hiển thị bottom sheet đăng nhập khi có sự kiện yêu cầu đăng nhập
    LaunchedEffect(Unit) {
        AppEventBus.events.collect { event ->
            when (event) {
                is UiEvent.RequireLogin -> {
                    pendingRoute = event.targetRoute
                    showLoginSheet = true
                }

                is UiEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

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
        navigation(
            startDestination = "${Screen.Login.route}?next={next}",
            route = Screen.AuthGraph.route
        ) {

            composableWithAnim(
                route = "${Screen.Login.route}?next={next}",
                anim = NavAnim.FADE,
                arguments = listOf(navArgument("next") {
                    type = NavType.StringType
                    defaultValue = ""
                })
            ) { backStackEntry ->
                val next = backStackEntry.arguments?.getString("next") ?: ""
                val loginViewModel = hiltViewModel<LoginViewModel>()
                LoginRoute(
                    viewModel = loginViewModel,
                    onNavigateToSignUp = {
                        navController.navigate("${Screen.SignUp.route}?next=${Uri.encode(next)}")
                    },
                    onNavigateToForgotPassword = {
                        navController.navigate(
                            "${Screen.ForgotPassword.route}?next=${
                                Uri.encode(
                                    next
                                )
                            }"
                        )
                    },
                    onLoginClick = { email, phone, password ->
                        loginViewModel.login(email, password)
                    },
                    onLoginSuccess = { user ->
                        authViewModel.onUserLoggedIn(user)
                        if (next.isNotEmpty()) {
                            //nếu có next thì điều hướng đến next
                            navController.navigate(Uri.decode(next)) {
                                popUpTo(Screen.AuthGraph.route) { inclusive = true }
                            }
                        } else {
                            //ngược lại điều hướng về home
                            navController.navigate(Screen.HomeGraph.route) {
                                popUpTo(Screen.AuthGraph.route) { inclusive = true }
                            }
                        }
                    }

                )
            }


            composableWithAnim(
                route = "${Screen.SignUp.route}?next={next}",
                anim = NavAnim.FADE,
                arguments = listOf(navArgument("next") {
                    type = NavType.StringType
                    defaultValue = ""
                })
            ) { backStackEntry ->
                val next = backStackEntry.arguments?.getString("next") ?: ""
                val signUpViewModel = hiltViewModel<SignUpViewModel>()
                SignUpRoute(
                    viewModel = signUpViewModel,
                    onSignUpClick = {
                        //Log.d("SignUpRoute", "Đã đăng kí: $email, $phone, $password, $name")
                        signUpViewModel.signUp()
                    },
                    onNavigateToLogin = {
                        navController.navigate(
                            "${Screen.Login.route}?next=${
                                Uri.encode(
                                    next
                                )
                            }"
                        )
                    },
                    onSignUpSuccess = { user ->
                        authViewModel.onUserLoggedIn(user)
                        if (next.isNotEmpty()) {
                            //nếu có next thì điều hướng đến next
                            navController.navigate(Uri.decode(next)) {
                                popUpTo(Screen.AuthGraph.route) { inclusive = true }
                            }
                        } else {
                            //ngược lại điều hướng về home
                            navController.navigate(Screen.HomeGraph.route) {
                                popUpTo(Screen.AuthGraph.route) { inclusive = true }
                            }
                        }
                    }
                )
            }

            composableWithAnim(
                route = "${Screen.ForgotPassword.route}?next={next}",
                anim = NavAnim.FADE,
                arguments = listOf(navArgument("next") {
                    type = NavType.StringType
                    defaultValue = ""
                })
            ) { backStackEntry ->
                val next = backStackEntry.arguments?.getString("next") ?: ""
                val forgotViewModel = hiltViewModel<ForgotPasswordViewModel>()
                ForgotPasswordRoute(
                    viewModel = forgotViewModel,
                    onNavigateToLogin = {
                        navController.navigate("${Screen.Login.route}?next=${Uri.encode(next)}")
                    }
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
                        navController.requireLoginThenNavigate(
                            authState.value,
                            Screen.Checkout.route
                        )
                    },
                    onCartItemClick = { product ->
                        navController.navigate(Screen.ProductDetail.createRoute(product.id))
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

                ProductDetailRoute(
                    viewModel = viewModel,
                    onNavigateToProduct = { product ->
                        navController.navigate(Screen.ProductDetail.createRoute(product.id))
                    },
                    onNavigateToQuickCheckout = { product, quantity ->
                        navController.requireLoginThenNavigate(
                            authState.value,
                            Screen.QuickCheckout.createRoute(product.id, quantity)
                        )
                    }
                )
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
                        navController.requireLoginThenNavigate(
                            authState.value,
                            Screen.DeliveryAddress.route
                        )
                    },
                    onNavigateToPayment = {
                        navController.requireLoginThenNavigate(
                            authState.value,
                            Screen.SelectPaymentMethod.route
                        )
                    },
                    onNavigateToSuccess = {
                        navController.navigate(Screen.OrderSuccess.route) {
                            popUpTo(Screen.HomeGraph.route)
                            launchSingleTop = true
                        }
                    },
                    onNavigateToOnlineProcessing = { orderCode, localOrderId ->
                        navController.navigate(
                            Screen.OnlinePaymentProcessing.createRoute(orderCode, localOrderId)
                        )
                    },
                    onNavigateToPaymentFailed = { orderId, reason ->
                        navController.navigate(
                            Screen.PaymentFailed.createRoute(orderId, reason)
                        )
                    }
                )
            }

            composableWithAnim(
                route = Screen.QuickCheckout.route,
                anim = NavAnim.HORIZONTAL,
                arguments = listOf(
                    navArgument("productId") { type = NavType.IntType; defaultValue = -1 },
                    navArgument("quantity") { type = NavType.IntType; defaultValue = 1 }
                )
            ) { backStackEntry ->
                val parentEntry =
                    remember { navController.getBackStackEntry(Screen.HomeGraph.route) }

                val productId = backStackEntry.arguments?.getInt("productId") ?: -1
                val quantity = backStackEntry.arguments?.getInt("quantity") ?: 1

                CheckOutRoute(
                    checkoutViewModel = hiltViewModel(parentEntry),
                    cartViewModel = hiltViewModel(parentEntry),
                    addressViewModel = hiltViewModel(parentEntry),
                    paymentViewModel = hiltViewModel(parentEntry),
                    quickOrderProductId = productId.takeIf { it > 0 },
                    quickOrderQuantity = quantity.takeIf { it > 0 } ?: 1,
                    onNavigateToAddress = {
                        navController.requireLoginThenNavigate(
                            authState.value,
                            Screen.DeliveryAddress.route
                        )
                    },
                    onNavigateToPayment = {
                        navController.requireLoginThenNavigate(
                            authState.value,
                            Screen.SelectPaymentMethod.route
                        )
                    },
                    onNavigateToSuccess = {
                        navController.navigate(Screen.OrderSuccess.route) {
                            popUpTo(Screen.HomeGraph.route)
                            launchSingleTop = true
                        }
                    },
                    onNavigateToOnlineProcessing = { orderCode, localOrderId ->
                        navController.navigate(
                            Screen.OnlinePaymentProcessing.createRoute(orderCode, localOrderId)
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


            composableWithAnim(
                route = Screen.OrderSuccess.route,
                anim = NavAnim.FADE
            ) {
                val parentEntry =
                    remember { navController.getBackStackEntry(Screen.HomeGraph.route) }
                val viewModel = hiltViewModel<CheckoutViewModel>(parentEntry)

                OrderSuccessRoute(
                    checkoutViewModel = viewModel,
                    onViewOrderClick = { orderId ->
                        navController.navigate(Screen.OrderDetail.createRoute(orderId)) {
                            launchSingleTop = true
                        }
                    },
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
                val message = backStackEntry.arguments?.getString(stringResource(R.string.ui_text_003))

                PaymentFailedRoute(
                    message = message ?: stringResource(R.string.ui_text_004),
                    onRetry = {
                        navController.navigate(Screen.Checkout.route) {
                            popUpTo(Screen.HomeGraph.route)
                            launchSingleTop = true
                        }
                    },
                    onChangePaymentMethod = {
                        navController.navigate(Screen.SelectPaymentMethod.route) {
                            popUpTo(Screen.HomeGraph.route)
                            launchSingleTop = true
                        }
                    },
                    onBackToCheckOut = {
                        navController.navigate(Screen.Checkout.route) {
                            popUpTo(Screen.HomeGraph.route)
                            launchSingleTop = true
                        }
                    }
                )
            }

            composableWithAnim(
                route = Screen.OnlinePaymentProcessing.route,
                anim = NavAnim.FADE,
                arguments = listOf(
                    navArgument("orderCode") { type = NavType.StringType; defaultValue = "" },
                    navArgument("localOrderId") { type = NavType.StringType; defaultValue = "" }
                )
            ) { backStackEntry ->
                val orderCode = backStackEntry.arguments?.getString("orderCode")?.toLongOrNull()
                val localOrderId =
                    backStackEntry.arguments?.getString("localOrderId")?.toIntOrNull()

                //Log.d("AppNavGraph", "Navigating to OnlinePaymentProcessingRoute with orderCode=$orderCode, localOrderId=$localOrderId")
                OnlinePaymentProcessingRoute(
                    orderCode = orderCode,
                    localOrderId = localOrderId,
                    onSuccess = {
                        navController.navigate(Screen.OrderSuccess.route) {
                            popUpTo(Screen.HomeGraph.route)
                            launchSingleTop = true
                        }
                        Log.d("AppNavGraph", "Navigated to OrderSuccessRoute")
                    },
                    onFailed = { reason ->
                        navController.navigate(
                            Screen.PaymentFailed.createRoute(localOrderId ?: -1, reason)
                        )
                    },
                    onPendingTimeout = {
                        // nếu vẫn pending thì cứ ở màn này (user có thể chờ hoặc back)
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
                        navController.requireLoginThenNavigate(
                            authState.value,
                            Screen.AddAddress.route
                        )
                    },
                    onAddressEditClick = { addressId ->
                        navController.requireLoginThenNavigate(
                            authState.value,
                            Screen.AddAddress.createRoute(addressId)
                        )
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
                    checkoutViewModel = hiltViewModel(parentEntry),
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

        }


        //nav profile
        navigation(startDestination = Screen.Profile.route, route = Screen.ProfileGraph.route) {
            composableWithAnim(
                Screen.Profile.route,
                anim = NavAnim.NONE
            ) {
                val viewModel = hiltViewModel<ProfileViewModel>()
                ProfileRoute(
                    viewModel = viewModel,
                    onNavigate = { tag ->
                        when (tag) {
                            "login" -> {
                                navController.navigate(Screen.AuthGraph.route)
                                {
                                    popUpTo(0) { //clear toàn bộ backstack
                                        inclusive = true
                                    }
                                }
                            }

                            "orders" -> {
                                navController.requireLoginThenNavigate(
                                    authState.value,
                                    Screen.Order.route
                                )
                            }

                            "address" -> {
                                Log.d("AppNavGraph", "Address clicked")
                                navController.navigate(Screen.DeliveryAddress.route)
                            }

                            "payment" -> {
                            }

                            "admin" -> {
                                Log.d(
                                    "AppNavGraph",
                                    "User role: ${(authState.value as? AuthState.LoggedIn)?.role}"
                                )
                                navController.requireAdminThenNavigate(
                                    authState.value,
                                    Screen.AdminGraph.route
                                )
                            }

                            "settings" -> {
                            }
                        }
                    },
                    onLogoutSuccess = {
                        navController.navigate(Screen.AuthGraph.route)
                        {
                            popUpTo(0) { //clear toàn bộ backstack
                                inclusive = true
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
                CategoryRoute(
                    onNavigateToProduct = { product ->
                        navController.navigate(Screen.ProductDetail.createRoute(product.id))
                    }
                )
            }
        }

        //nav search
        navigation(startDestination = Screen.Search.route, route = Screen.SearchGraph.route) {
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

            composableWithAnim(
                route = Screen.ImageSearch.route,
                anim = NavAnim.HORIZONTAL
            ) {
                ImageSearchRoute(
                    onProductClick = { product ->
                        navController.navigate(Screen.ProductDetail.createRoute(product.id))
                    }
                )
            }

        }


        // admin graph
        navigation(
            startDestination = Screen.AdminDashboard.route,
            route = Screen.AdminGraph.route
        ) {
            composableWithAnim(
                route = Screen.AdminDashboard.route,
                anim = NavAnim.HORIZONTAL
            ) {
                AdminDashboardRoute(
                    onNavigateOrders = { navController.navigate(Screen.AdminOrders.route) },
                    onNavigateProducts = { navController.navigate(Screen.AdminProducts.route) },
                    onNavigateCategories = { navController.navigate(Screen.AdminCategories.route) }
                )
            }

            composableWithAnim(
                route = Screen.AdminOrders.route,
                anim = NavAnim.HORIZONTAL
            ) {
                AdminOrdersRoute(
                    onViewOrderDetail = { remoteId ->
                        navController.navigate(Screen.AdminOrderDetail.createRoute(remoteId))
                    }
                )
            }

            composableWithAnim(
                route = Screen.AdminOrderDetail.route,
                anim = NavAnim.HORIZONTAL,
                arguments = listOf(navArgument("remoteId") { type = NavType.StringType })
            ) { backStackEntry ->
                val remoteId =
                    backStackEntry.arguments?.getString("remoteId") ?: return@composableWithAnim
                AdminOrderDetailRoute(remoteId = remoteId)
            }

            composableWithAnim(
                route = Screen.AdminProducts.route,
                anim = NavAnim.HORIZONTAL
            ) {
                AdminProductsRoute(
                    onNavigateBack = { navController.navigateUp() },
                    onAddProduct = { navController.navigate(Screen.AdminAddProduct.route) },
                    onEditProduct = { productId ->
                        navController.navigate(Screen.AdminEditProduct.createRoute(productId))
                    },
                    onViewProductDetail = { productId ->
                        navController.navigate(Screen.AdminProductDetail.createRoute(productId))
                    }
                )
            }

            composableWithAnim(
                route = Screen.AdminProductDetail.route,
                anim = NavAnim.HORIZONTAL,
                arguments = listOf(navArgument("productId") { type = NavType.IntType })
            ) { backStackEntry ->
                val productId =
                    backStackEntry.arguments?.getInt("productId") ?: return@composableWithAnim
                AdminProductDetailRoute(
                    productId = productId,
                    onEditProduct = { id ->
                        navController.navigate(Screen.AdminEditProduct.createRoute(id))
                    },
                    onNavigateBack = { navController.navigateUp() }
                )
            }

            composableWithAnim(
                route = Screen.AdminAddProduct.route,
                anim = NavAnim.HORIZONTAL
            ) {
                AdminAddEditProductRoute(
                    onNavigateBack = { navController.navigateUp() }
                )
            }

            composableWithAnim(
                route = Screen.AdminEditProduct.route,
                anim = NavAnim.HORIZONTAL,
                arguments = listOf(navArgument("productId") { type = NavType.IntType })
            ) { backStackEntry ->
                val productId =
                    backStackEntry.arguments?.getInt("productId") ?: return@composableWithAnim
                AdminAddEditProductRoute(
                    productId = productId,
                    onNavigateBack = { navController.navigateUp() }
                )
            }

            composableWithAnim(
                route = Screen.AdminCategories.route,
                anim = NavAnim.HORIZONTAL
            ) {
                AdminPlaceholderScreen("Quản lý danh mục")
            }
        }

        //màn đơn hàng của tôi
        composableWithAnim(
            route = Screen.Order.route,
            anim = NavAnim.HORIZONTAL
        ) {
            val viewModel = hiltViewModel<OrderViewModel>()
            OrderRoute(
                viewModel,
                onNavigateToOrderDetail = { orderId ->
                    navController.requireLoginThenNavigate(
                        authState.value,
                        Screen.OrderDetail.createRoute(orderId)
                    )
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
        ) {
            val viewModel = hiltViewModel<OrderDetailViewModel>()
            val orderId = it.arguments?.getInt("orderId") ?: return@composableWithAnim
            LaunchedEffect(orderId) {
                viewModel.loadOrderDetail(orderId)
            }
            OrderDetailRoute(viewModel)
        }

        composableWithAnim(
            route = Screen.PayOsReturn.route,
            anim = NavAnim.FADE,
            arguments = listOf(
                navArgument("orderCode") { type = NavType.StringType; defaultValue = "" },
                navArgument("localOrderId") { type = NavType.StringType; defaultValue = "" },
                navArgument("status") { type = NavType.StringType; defaultValue = "" }
            )
        ) { backStackEntry ->
            val orderCode = backStackEntry.arguments?.getString("orderCode")?.toLongOrNull()
            val localOrderId = backStackEntry.arguments?.getString("localOrderId")?.toIntOrNull()

            LaunchedEffect(orderCode, localOrderId) {
                Log.d(
                    "AppNavGraph",
                    "Received PayOs return: orderCode=$orderCode, localOrderId=$localOrderId"
                )
                navController.navigate(
                    Screen.OnlinePaymentProcessing.createRoute(orderCode, localOrderId)
                ) {
                    popUpTo(Screen.HomeGraph.route)
                }
            }
        }

        composableWithAnim(
            route = Screen.PayOsCancel.route,
            anim = NavAnim.FADE,
            arguments = listOf(
                navArgument("orderCode") { type = NavType.StringType; defaultValue = "" },
                navArgument("localOrderId") { type = NavType.StringType; defaultValue = "" },
                navArgument("status") { type = NavType.StringType; defaultValue = "" }
            )
        ) { backStackEntry ->
            val parentEntry = remember { navController.getBackStackEntry(Screen.HomeGraph.route) }
            val viewModel = hiltViewModel<CheckoutViewModel>(parentEntry)
            val orderCode = backStackEntry.arguments?.getString("orderCode")?.toLongOrNull()
            val localOrderId = backStackEntry.arguments?.getString("localOrderId")?.toIntOrNull()

            LaunchedEffect(orderCode, localOrderId) {
                if (localOrderId != null) {
                    viewModel.markPayOsCancelled(localOrderId)
                }

                navController.navigate(
                    Screen.PaymentFailed.createRoute(localOrderId ?: -1, "Bạn đã hủy thanh toán")
                ) {
                    popUpTo(Screen.HomeGraph.route)
                }
            }
        }

    }


    // bottom sheet yêu cầu đăng nhập
    if (showLoginSheet && pendingRoute != null) {
        ModalBottomSheet(
            onDismissRequest = { showLoginSheet = false },
            sheetState = sheetState
        ) {
            LoginRequiredBottomSheet(
                onLoginClick = {
                    val encoded = Uri.encode(pendingRoute)
                    navController.navigate("${Screen.Login.route}?next=$encoded")
                    showLoginSheet = false
                },
                onDismiss = {
                    showLoginSheet = false
                }
            )
        }
    }
}



































