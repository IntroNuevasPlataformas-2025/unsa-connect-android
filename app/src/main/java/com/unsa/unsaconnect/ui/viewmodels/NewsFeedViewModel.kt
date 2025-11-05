package com.unsa.unsaconnect.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unsa.unsaconnect.domain.repositories.NewsRepository
import com.unsa.unsaconnect.ui.states.NewsFeedUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsFeedViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewsFeedUiState())
    val uiState: StateFlow<NewsFeedUiState> = _uiState

    init {
        loadNews()
    }

    private fun loadNews() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val highlightedNews = newsRepository.getHighlightedNews()
            val recentNews = newsRepository.getRecentNews()
            _uiState.value = _uiState.value.copy(
                highlightedNews = highlightedNews,
                recentNews = recentNews,
                isLoading = false
            )
        }
    }
}
