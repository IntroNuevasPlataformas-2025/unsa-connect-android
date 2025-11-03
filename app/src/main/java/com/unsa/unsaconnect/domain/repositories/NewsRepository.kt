package com.unsa.unsaconnect.domain.repositories

import com.unsa.unsaconnect.data.models.Category
import com.unsa.unsaconnect.data.models.New

interface NewsRepository {
    fun getHighlightedNews(): List<New>
    fun getRecentNews(): List<New>
    fun getCategories(): List<Category>
}
