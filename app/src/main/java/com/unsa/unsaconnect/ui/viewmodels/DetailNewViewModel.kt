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

    private val newsId: String = savedStateHandle.get<String>("newsId")!!

    init {
        getNewsById(newsId.toInt())
    }

    private fun getNewsById(newsId: Int) {
        viewModelScope.launch {
            _uiState.value = DetailNewUiState(isLoading = true)
            try {
                val newsList = newsRepository.getRecentNews().first()
                val news = newsList.find { it.id == newsId }
                    ?: throw Exception("News with ID $newsId not found")
                _uiState.value = DetailNewUiState(isLoading = false, news = news)
            } catch (e: Exception) {
                _uiState.value = DetailNewUiState(isLoading = false, error = e.message)
            }
        }
    }
}
