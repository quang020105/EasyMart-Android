package com.example.easymart.presentation.ui.main

import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.easymart.R
import com.example.easymart.presentation.navigation.AppNavGraph
import com.example.easymart.presentation.navigation.Screen
import com.example.easymart.presentation.navigation.getScreenConfig
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.cart.CartViewModel
import com.example.easymart.presentation.ui.home.components.HomeTopbar
import com.example.easymart.presentation.ui.main.bottomnav.BottomNavItem
import com.example.easymart.presentation.ui.main.bottomnav.EasyMartBottomBar
import com.example.easymart.presentation.ui.search.SearchViewModel
import com.example.easymart.presentation.ui.search.components.SearchTopbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(navController: NavHostController, cartCount: Int = 1, startDeepLink: Uri? = null) {
    // theo dõi backstack entry hiện tại
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    //lấy destination hiện tại
    val currentDestination = navBackStackEntry?.destination
    // lấy route hiện tại
    val currentRoute = navBackStackEntry?.destination?.route

    //những màn cần ẩn cả topBar và bottomBar
    val topBottomHiddenRoutes = setOf(
        Screen.Splash.route,
        Screen.Login.route,
        Screen.SignUp.route
    )

    //những màn hiện bottomBar
    val bottomBarRoutes = setOf(
        Screen.Home.route,
        Screen.Search.route,
        Screen.Category.route,
        Screen.Profile.route
    )

    val bottomNavItems = listOf(
        BottomNavItem(
            route = Screen.Home.route,
            label = "Trang chủ",
            icon = R.drawable.ic_home,
            graphRoute = Screen.HomeGraph.route,
        ),
        BottomNavItem(
            route = Screen.Category.route,
            label = "Danh mục",
            icon = R.drawable.ic_category,
            graphRoute = Screen.CategoryGraph.route
        ),
        BottomNavItem(
            route = Screen.Search.route,
            label = "Tìm kiếm",
            icon = R.drawable.ic_search,
            graphRoute = Screen.SearchGraph.route
        ),
        BottomNavItem(
            route = Screen.Profile.route,
            label = "Cá nhân",
            icon = R.drawable.ic_person,
            graphRoute = Screen.ProfileGraph.route
        )
    )


//    val showTopBar = currentRoute !in topBottomHiddenRoutes
//    val showBottomBar = currentRoute != null && currentRoute in bottomBarRoutes

    val config = getScreenConfig(currentRoute)
    val showTopBar = config?.showTopBar == true
    val showBottomBar = config?.showBottomBar == true


    //kiểm tra xem màn hiện tại có nằm trong bất kì graph nào hay không
//    val showBottomBar = currentDestination?.let { dest ->
//        bottomNavItems.any { bnItem ->
//            dest.hierarchy.any {
//                it.route?.startsWith(bnItem.graphRoute) == true || it.route == bnItem.graphRoute
//            }
//        }
//    } ?: false


    val dimens = LocalAppDimens.current

    LaunchedEffect(startDeepLink) {
        val link = startDeepLink ?: return@LaunchedEffect
        val path = link.pathSegments.firstOrNull() ?: return@LaunchedEffect
        val orderCode = link.getQueryParameter("orderCode")?.toLongOrNull()
        val localOrderId = link.getQueryParameter("localOrderId")?.toIntOrNull()
        val status = link.getQueryParameter("status")

        when (path.lowercase()) {
            "return" -> navController.navigate(
                Screen.PayOsReturn.createRoute(orderCode, localOrderId, status)
            )
            "cancel" -> navController.navigate(
                Screen.PayOsCancel.createRoute(orderCode, localOrderId, status)
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (showTopBar) {
                when (currentRoute) {
                    Screen.Home.route -> {
                        val entry = navBackStackEntry
                        if (entry != null) {
                            val viewModel = hiltViewModel<CartViewModel>(entry)
                            val uiState by viewModel.uiState.collectAsState()
                            HomeTopbar(
                                onCartClick = { navController.navigate(Screen.Cart.route) },
                                onSearchClick = { navController.navigate(Screen.Search.route) },
                                cartCount = uiState.items.size
                            )
                        }
                    }

                    Screen.Search.route -> {
                        val entry = navBackStackEntry
                        if (entry != null) {
                            val viewModel = hiltViewModel<SearchViewModel>(entry)
                            val uiState = viewModel.uiState.collectAsState()
                            SearchTopbar(
                                text = uiState.value.searchQuery,
                                onSearchClick = { query -> viewModel.searchProducts(query) },
                                onSearchTextChange = { query -> viewModel.onSearchTextChange(query) },
                                onBackClick = { navController.navigateUp() },
                                autoFocus = true
                            )
                        }
                    }

                    else -> {
                        TopAppBar(
                            title = { Text(config.title ?: "") },
                            navigationIcon = {
                                if (showBottomBar) {
                                    Text(text = "EasyMart")
                                } else {
                                    IconButton(
                                        onClick = {
                                            //nếu back ở màn OrderSuccess thì về Home và xoá hết backstack
                                            if (currentRoute == Screen.OrderSuccess.route) {
                                                navController.navigate(Screen.HomeGraph.route) {
                                                    popUpTo(Screen.HomeGraph.route) {
                                                        inclusive = true
                                                    }
                                                }
                                            } else {
                                                navController.navigateUp()
                                            }
                                        }
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_back),
                                            contentDescription = null
                                        )
                                    }
                                }
                            },
                        )
                    }

                }

            }
        },
        bottomBar = {
            if (showBottomBar) {

                val currentGraphRoute = bottomNavItems.find { item ->
                    currentDestination?.hierarchy?.any {
                        it.route?.startsWith(item.graphRoute) == true || it.route == item.graphRoute
                    } == true
                }?.graphRoute

                EasyMartBottomBar(
                    items = bottomNavItems,
                    currentGraphRoute = currentGraphRoute,
                    onItemClick = { item ->
                        navController.navigate(item.graphRoute) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(Screen.HomeGraph.route) {
                                saveState = true
                            }
                        }
                    }
                )

//                NavigationBar(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(100.dp)
//                        .clip(
//                            RoundedCornerShape(
//                                topStart = dimens.radiusSmall,
//                                topEnd = dimens.radiusSmall
//                            )
//                        )
//                        .border(
//                            width = 1.dp,
//                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
//                            shape = RoundedCornerShape(dimens.radiusSmall)
//                        ),
//                    containerColor = Color.White,
//
//                    ) {
//                    val currentGraphRoute = bottomNavItems.find { item ->
//                        currentDestination?.hierarchy?.any {
//                            it.route?.startsWith(item.graphRoute) == true || it.route == item.graphRoute
//                        } == true
//                    }?.graphRoute
//
//                    bottomNavItems.forEach { item ->
//                        NavigationBarItem(
//                            icon = {
//                                Icon(
//                                    painter = painterResource(id = item.icon),
//                                    contentDescription = item.label,
//                                    modifier = Modifier.size(dimens.iconBottomSize)
//                                )
//                            },
//                            colors = NavigationBarItemDefaults.colors(
//                                selectedIconColor = MaterialTheme.colorScheme.primary,
//                                unselectedIconColor = MaterialTheme.colorScheme.outline,
//                                indicatorColor = Color.White
//                            ),
//                            onClick = {
//                                navController.navigate(item.graphRoute) {
//                                    launchSingleTop = true
//                                    restoreState = true
//                                    popUpTo(Screen.HomeGraph.route) {
//                                        saveState = true
//                                    }
//                                }
//                            },
//                            selected = currentGraphRoute == item.graphRoute
//                        )
//                    }
//                }

            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AppNavGraph(navController = navController, modifier = Modifier.fillMaxSize())
        }
    }
}

//các thể hiện của BottomNavItem
//data class BottomNavItem(
//    val id: String,
//    val label: String,
//    val icon: Int,
//    val graphRoute: String
//)