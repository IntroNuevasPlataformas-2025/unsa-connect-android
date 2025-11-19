package com.unsa.unsaconnect.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.unsa.unsaconnect.data.models.New
import com.unsa.unsaconnect.data.models.NewsCategoryCrossRef
import com.unsa.unsaconnect.data.models.NewsWithCategories
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Transaction
    @Query("SELECT * FROM news")
    fun getAllNewsWithCategories(): Flow<List<NewsWithCategories>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: New)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: com.unsa.unsaconnect.data.models.Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewsCategoryCrossRef(crossRef: NewsCategoryCrossRef)

    @Query("DELETE FROM news WHERE id = :newsId")
    suspend fun deleteNewsById(newsId: Int)
}
