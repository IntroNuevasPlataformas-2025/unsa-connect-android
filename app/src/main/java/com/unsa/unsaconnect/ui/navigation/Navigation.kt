package com.unsa.unsaconnect.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.unsa.unsaconnect.ui.components.BottomNavigationBar
import com.unsa.unsaconnect.ui.components.MainTopBar
import com.unsa.unsaconnect.ui.screens.DetailNewScreen
import com.unsa.unsaconnect.ui.screens.FavoritesScreen
import com.unsa.unsaconnect.ui.screens.NewsFeed
import com.unsa.unsaconnect.ui.screens.FullScreenImageScreen
import com.unsa.unsaconnect.ui.screens.SettingsScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    // Observe the current back stack entry
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Determine the title based on the current route
    val currentScreenTitle = when (currentRoute) {
        Screen.Feed.route -> "UNSA Connect"
        Screen.Favorites.route -> "Favoritos"
        Screen.Settings.route -> "Ajustes"
        else -> ""
    }

    Scaffold(
        topBar = {
            if (currentRoute != Screen.DetailNew.route && currentRoute != "full_image/{imageResId}") {
                MainTopBar(title = currentScreenTitle)
            }
        },
        bottomBar = {
            if (currentRoute != Screen.DetailNew.route && currentRoute != "full_image/{imageResId}") {
                BottomNavigationBar(navController = navController) }
            }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Feed.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Screen.Feed.route) {
                NewsFeed(navController = navController)
            }
            composable(route = Screen.Favorites.route) {
                FavoritesScreen()
            }
            composable(route = Screen.Settings.route) {
                SettingsScreen()
            }
            composable(
                route = Screen.DetailNew.route,
                arguments = listOf(navArgument("newsId") { type = NavType.StringType })
            ) {
                DetailNewScreen(navController = navController)
            }
            composable(
                route = "full_image/{imageResId}",
                arguments = listOf(navArgument("imageResId") { type = NavType.IntType })
            ) { backStackEntry ->
                val imageResId = backStackEntry.arguments?.getInt("imageResId") ?: 0
                FullScreenImageScreen(
                    imageResId = imageResId,
                    navController = navController
                )
            }
        }
    }
}
