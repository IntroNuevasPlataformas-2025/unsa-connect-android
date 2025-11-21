package com.unsa.unsaconnect.ui.navigation

sealed class Screen(val route: String) {
    object NewsFeed : Screen("news_feed")
    object DetailNew : Screen("detail_screen/{newsId}") {
        fun createRoute(newsId: Int) = "detail_screen/$newsId"
    }
    object Favorites : Screen("favorites_screen")
}
