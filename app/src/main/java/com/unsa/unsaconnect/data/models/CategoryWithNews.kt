package com.unsa.unsaconnect.data.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class CategoryWithNews(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = NewsCategoryCrossRef::class,
            parentColumn = "categoryId",
            entityColumn = "newsId"
        )
    )
    val news: List<New>
)
