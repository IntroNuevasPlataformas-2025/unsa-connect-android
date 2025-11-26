package com.unsa.unsaconnect.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String? = null,
    val icon: ImageVector? = null
) {
    object Feed : Screen("feed", "Feed", Icons.Default.Home)
    object Favorites : Screen("favorites", "Favorites", Icons.Default.Bookmarks)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)

    object DetailNew : Screen("detail_screen/{newsId}") {
        fun createRoute(newsId: Int) = "detail_screen/$newsId"
    }
    object FullImage : Screen("full_image/{imageResId}")
}
