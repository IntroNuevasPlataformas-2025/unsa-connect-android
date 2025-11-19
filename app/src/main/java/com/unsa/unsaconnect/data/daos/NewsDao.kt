package com.unsa.unsaconnect.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.unsa.unsaconnect.data.models.CategoryWithNews
import com.unsa.unsaconnect.data.models.New
import com.unsa.unsaconnect.data.models.NewsCategoryCrossRef
import com.unsa.unsaconnect.data.models.NewsWithCategories
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Transaction
    @Query("SELECT * FROM news")
    fun getAllNewsWithCategories(): Flow<List<NewsWithCategories>>

    @Transaction
    @Query("SELECT * FROM categories WHERE id = :categoryId")
    fun getCategoryWithNews(categoryId: Int): Flow<CategoryWithNews>

    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<com.unsa.unsaconnect.data.models.Category>>

    @Query("SELECT * FROM news WHERE isFavorite = 1")
    fun getFavoriteNews(): Flow<List<New>>

    @Query("UPDATE news SET isFavorite = :isFavorite WHERE id = :newsId")
    suspend fun setFavorite(newsId: Int, isFavorite: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: New)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: com.unsa.unsaconnect.data.models.Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewsCategoryCrossRef(crossRef: NewsCategoryCrossRef)

    @Query("DELETE FROM news WHERE id = :newsId")
    suspend fun deleteNewsById(newsId: Int)
}
