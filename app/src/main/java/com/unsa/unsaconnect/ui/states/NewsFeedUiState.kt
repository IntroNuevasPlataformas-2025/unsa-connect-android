package com.unsa.unsaconnect.ui.states

import com.unsa.unsaconnect.data.models.New

data class NewsFeedUiState(
    val highlightedNews: List<New> = emptyList(),
    val recentNews: List<New> = emptyList(),
    val isLoading: Boolean = false
)
