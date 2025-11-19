package com.unsa.unsaconnect.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unsa.unsaconnect.domain.repositories.NewsRepository
import com.unsa.unsaconnect.ui.states.DetailNewUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailNewViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailNewUiState())
    val uiState: StateFlow<DetailNewUiState> = _uiState.asStateFlow()

    private val newsId: Int = savedStateHandle.get<String>("newsId")?.toIntOrNull() ?: -1

    init {
        if(newsId != -1){
            getNewsById(newsId)
        }
    }

    private fun getNewsById(newsId: Int) {
        viewModelScope.launch {
            _uiState.value = DetailNewUiState(isLoading = true)
            try {
                val newsList = newsRepository.getRecentNews().first()

                val foundNews = newsList.find { it.news.id == newsId }

                _uiState.value = DetailNewUiState(isLoading = false, news = foundNews)
            } catch (e: Exception) {
                _uiState.value = DetailNewUiState(isLoading = false, error = e.message)
            }
        }
    }
}
