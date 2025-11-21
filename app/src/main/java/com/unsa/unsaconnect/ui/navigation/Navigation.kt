package com.unsa.unsaconnect.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.unsa.unsaconnect.ui.screens.DetailNewScreen
import com.unsa.unsaconnect.ui.screens.NewsFeed

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.NewsFeed.route) {
        composable(route = Screen.NewsFeed.route) {
            NewsFeed(navController = navController)
        }
        composable(
            route = Screen.DetailNew.route,
            arguments = listOf(navArgument("newsId") { type = NavType.StringType })
        ) {
            DetailNewScreen(navigateUp = { navController.navigateUp() })
        }
        composable(
            route = "full_image/{imageResId}",
            arguments = listOf(navArgument("imageResId") { type = NavType.IntType })
        ) { 
            backStackEntry ->
            val imageResId = backStackEntry.arguments?.getInt("imageResId") ?: 0  // Default 0 si falla
            FullScreenImageScreen(imageResId = imageResId, navController = navController)
        }
    }
}
