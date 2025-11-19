package com.unsa.unsaconnect.data.repositories

import com.unsa.unsaconnect.data.daos.NewsDao
import com.unsa.unsaconnect.data.models.Category
import com.unsa.unsaconnect.data.models.New
import com.unsa.unsaconnect.domain.repositories.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val newsDao: NewsDao
) : NewsRepository {

    override fun getHighlightedNews(): Flow<List<New>> {
        // For now, highlighted news are the 3 most recent
        return getRecentNews().map { it.take(3) }
    }

    override fun getRecentNews(): Flow<List<New>> {
        return newsDao.getAllNewsWithCategories().map { list ->
            list.map { it.news }
        }
    }

    override fun getCategories(): Flow<List<Category>> {
        return newsDao.getAllCategories()
    }

    override fun getNewsByCategory(categoryId: Int): Flow<List<New>> {
        return newsDao.getCategoryWithNews(categoryId).map { categoryWithNews ->
            categoryWithNews.news
        }
    }

    override fun getFavorites(): Flow<List<New>> {
        // This query already returns `New` objects, but they won't have the categories populated
        // This is a limitation for now, but acceptable for this migration step
        return newsDao.getFavoriteNews()
    }

    override suspend fun setFavorite(newsId: Int, isFavorite: Boolean) {
        newsDao.setFavorite(newsId, isFavorite)
    }
}
