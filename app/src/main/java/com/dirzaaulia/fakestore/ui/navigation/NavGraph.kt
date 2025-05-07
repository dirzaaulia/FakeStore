package com.dirzaaulia.fakestore.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dirzaaulia.fakestore.ui.cart.Cart
import com.dirzaaulia.fakestore.ui.checkout.Checkout
import com.dirzaaulia.fakestore.ui.detail.Detail
import com.dirzaaulia.fakestore.ui.home.Home
import com.dirzaaulia.fakestore.ui.home.HomeViewModel
import com.dirzaaulia.fakestore.ui.home.Login

@Composable
fun NavGraph(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val actions = remember(navController) { NavActions(navController) }

    NavHost(
        navController = navController,
        startDestination = NavScreen.Login.route
    ) {
        composable(NavScreen.Login.route) {
           Login(
               viewModel = homeViewModel,
               navigateToHome = actions.navigateToHome
           )
        }
        composable(NavScreen.Home.route) {
            Home(
                viewModel = homeViewModel,
                navigateToDetail = actions.navigateToDetail,
                navigateToCart = actions.navigateToCart
            )
        }
        composable(
            route = NavScreen.Detail.routeWithArgument,
            arguments = listOf(
                navArgument(NavScreen.Detail.argument0) {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            backStackEntry.arguments.let { bundle ->
                bundle?.let { argument ->
                    Detail(
                        productId = argument.getInt(NavScreen.Detail.argument0)
                    )
                }
            }
        }
        composable(NavScreen.Cart.route) {
            Cart(
                navigateUp = actions.upPress,
                navigateToCheckout = actions.navigateToCheckout
            )
        }
        composable(NavScreen.Checkout.route) {
            Checkout(
                navigateUp = actions.upPress
            )
        }
    }
}