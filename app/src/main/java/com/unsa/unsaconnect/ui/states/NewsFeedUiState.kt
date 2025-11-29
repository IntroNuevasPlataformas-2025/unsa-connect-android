package com.unsa.unsaconnect.ui.states

import com.unsa.unsaconnect.data.models.NewsWithCategories
import com.unsa.unsaconnect.data.models.Category

data class NewsFeedUiState(
    val highlightedNews: List<NewsWithCategories> = emptyList(),
    val recentNews: List<NewsWithCategories> = emptyList(),
    val categories: List<Category> = emptyList(),
    val selectedCategoryId: Int? = null,
    val isLoading: Boolean = false
)
