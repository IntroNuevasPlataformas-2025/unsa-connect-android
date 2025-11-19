package com.unsa.unsaconnect.domain.repositories

import com.unsa.unsaconnect.data.models.Category
import com.unsa.unsaconnect.data.models.New
import com.unsa.unsaconnect.data.models.NewsWithCategories
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getHighlightedNews(): Flow<List<NewsWithCategories>>
    fun getRecentNews(): Flow<List<NewsWithCategories>>
    fun getCategories(): Flow<List<Category>>
    fun getNewsByCategory(categoryId: Int): Flow<List<New>>
    fun getFavorites(): Flow<List<New>>
    suspend fun setFavorite(newsId: Int, isFavorite: Boolean)
}
