package com.unsa.unsaconnect.ui.states

import com.unsa.unsaconnect.data.models.NewsWithCategories

data class DetailNewUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val news: NewsWithCategories? = null
)
