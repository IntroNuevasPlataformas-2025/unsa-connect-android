package com.unsa.unsaconnect.ui.states

import com.unsa.unsaconnect.data.models.New

data class FavoritesUiState(
    val favoritesNews: List<New> = emptyList(),
    val isLoading: Boolean = false
)