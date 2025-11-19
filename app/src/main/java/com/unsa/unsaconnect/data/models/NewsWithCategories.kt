package com.unsa.unsaconnect.data.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class NewsWithCategories(
    @Embedded val news: New,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = NewsCategoryCrossRef::class,
            parentColumn = "newsId",
            entityColumn = "categoryId"
        )
    )
    val categories: List<Category>
)
