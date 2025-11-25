package com.unsa.unsaconnect.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.unsa.unsaconnect.ui.screens.DetailNewScreen
import com.unsa.unsaconnect.ui.screens.FullScreenImageScreen
import com.unsa.unsaconnect.ui.screens.NewsFeed

/**
 * @brief Configuración del NavHost y rutas de navegación de la app.
 * Incluye la ruta para ver una imagen en pantalla completa.
 */
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
            DetailNewScreen(navController = navController)   
        }
        composable(
            route = "full_image/{imageResId}",  // Si usas Screen.kt, cambia a Screen.FullImage.route
            arguments = listOf(navArgument("imageResId") { type = NavType.IntType })
        ) { backStackEntry ->
            // @brief Navega a la pantalla de imagen completa pasando el ID de recurso de imagen.
            // TODO: Permitir paso de enlaces externos en futuras versiones.
            val imageResId = backStackEntry.arguments?.getInt("imageResId") ?: 0
            FullScreenImageScreen(
                imageResId = imageResId,
                navController = navController
            )
        }
    }
}
