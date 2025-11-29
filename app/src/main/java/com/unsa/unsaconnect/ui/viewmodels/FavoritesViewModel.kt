package com.unsa.unsaconnect.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unsa.unsaconnect.data.models.New
import com.unsa.unsaconnect.data.models.NewsWithCategories
import com.unsa.unsaconnect.domain.repositories.NewsRepository
import com.unsa.unsaconnect.ui.states.FavoritesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    // 1. Obtenemos el Flow de favoritos y lo convertimos a StateFlow para la UI
    val uiState: StateFlow<FavoritesUiState> = newsRepository.getFavorites()
        .map { newsList ->
            // ESTE es el punto donde la lista YA está actualizada
            Log.d("FavoritesFlow", "Lista de favoritos actualizada: ${newsList.size} items")
            newsList.forEach { news ->
                Log.d("FavoritesFlow", " - Item: ${news.title}")
            }
            FavoritesUiState(favoritesNews = newsList, isLoading = false)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FavoritesUiState(isLoading = true)
        )


    fun addFavorite(newsId: Int) {
        viewModelScope.launch {
            // Llama al repositorio para actualizar el estado a 'true'
            newsRepository.setFavorite(newsId, isFavorite = true)
        }
    }

    fun removeFavorite(newsId: Int) {
        viewModelScope.launch {
            // El DAO ya tiene una función setFavorite(newsId, isFavorite)
            // Para quitar de favoritos, pasamos 'isFavorite = false'
            newsRepository.setFavorite(newsId, isFavorite = false)
        }
    }

    fun mapToNewsWithCategories(news: New): NewsWithCategories {
        // Si no hay categorías, retorna una lista vacía
        return NewsWithCategories(news = news, categories = emptyList())
    }
}