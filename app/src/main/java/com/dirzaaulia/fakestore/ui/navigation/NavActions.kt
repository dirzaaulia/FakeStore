package com.dirzaaulia.fakestore.ui.navigation

import androidx.navigation.NavHostController

class NavActions(navController: NavHostController) {
    val navigateToHome: () -> Unit = {
        NavScreen.Home.apply {
            navController.navigate(this.route)
        }
    }

    val navigateToDetail: (Int) -> Unit = { productId: Int ->
        NavScreen.Detail.apply {
            navController.navigate(
                routeWithArgument.replace("{$argument0}", productId.toString())
            )
        }
    }

    val navigateToCart: () -> Unit = {
        NavScreen.Cart.apply {
            navController.navigate(this.route)
        }
    }

    val upPress: () -> Unit = {
        navController.navigateUp()
    }
}