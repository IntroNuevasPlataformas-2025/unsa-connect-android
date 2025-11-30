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
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class NewsFeedViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {
    private val selectedCategoryId = MutableStateFlow<Int?>(null)

    val uiState: StateFlow<NewsFeedUiState> = combine(
        newsRepository.getHighlightedNews(),
        newsRepository.getRecentNews(),
        newsRepository.getCategories(),
        selectedCategoryId
    ) { highlightedNews, recentNews, categories, selectedCategoryId ->
        val filteredHighlighted = if (selectedCategoryId == null) {
            highlightedNews
        } else {
            highlightedNews.filter { new ->
                new.categories.any { it.id == selectedCategoryId }
            }
        }

        val filteredRecent = if (selectedCategoryId == null) {
            recentNews
        } else {
            recentNews.filter { new ->
                new.categories.any { it.id == selectedCategoryId }
            }
        }
        NewsFeedUiState(
            highlightedNews = filteredHighlighted,
            recentNews = filteredRecent,
            categories = categories,
            selectedCategoryId = selectedCategoryId,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NewsFeedUiState(isLoading = true)
    )

    fun selectCategory(categoryId: Int?) {
        selectedCategoryId.value = categoryId
    }
}
