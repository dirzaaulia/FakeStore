package com.dirzaaulia.fakestore.ui.navigation

sealed class NavScreen(val route: String) {
    object Login : NavScreen("Login")

    object Home : NavScreen("Home")

    object Detail : NavScreen("Detail") {
        const val routeWithArgument: String = "Detail/{productId}"
        const val argument0: String = "productId"
    }

    object Cart: NavScreen("Cart")
}