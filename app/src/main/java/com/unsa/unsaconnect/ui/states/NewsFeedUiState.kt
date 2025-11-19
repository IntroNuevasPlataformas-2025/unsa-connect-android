package com.unsa.unsaconnect.ui.states

import com.unsa.unsaconnect.data.models.New
import com.unsa.unsaconnect.data.models.NewsWithCategories

data class NewsFeedUiState(
    val highlightedNews: List<NewsWithCategories> = emptyList(),
    val recentNews: List<NewsWithCategories> = emptyList(),
    val isLoading: Boolean = false
)
