package com.unsa.unsaconnect.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unsa.unsaconnect.domain.repositories.NewsRepository
import com.unsa.unsaconnect.ui.states.NewsFeedUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import com.unsa.unsaconnect.data.models.NewsWithCategories

@HiltViewModel
class NewsFeedViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    val uiState: StateFlow<NewsFeedUiState> = combine(
        newsRepository.getHighlightedNews(),
        newsRepository.getRecentNews()
    ) { highlightedNews, recentNews ->
        NewsFeedUiState(
            highlightedNews = highlightedNews,
            recentNews = recentNews,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NewsFeedUiState(isLoading = true)
    )
}
