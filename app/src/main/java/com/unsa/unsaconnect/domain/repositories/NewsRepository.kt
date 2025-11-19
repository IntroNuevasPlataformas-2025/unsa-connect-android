package com.unsa.unsaconnect.domain.repositories

import com.unsa.unsaconnect.data.models.Category
import com.unsa.unsaconnect.data.models.New
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getHighlightedNews(): Flow<List<New>>
    fun getRecentNews(): Flow<List<New>>
    fun getCategories(): Flow<List<Category>>
    fun getNewsByCategory(categoryId: Int): Flow<List<New>>
    fun getFavorites(): Flow<List<New>>
    suspend fun setFavorite(newsId: Int, isFavorite: Boolean)
}
