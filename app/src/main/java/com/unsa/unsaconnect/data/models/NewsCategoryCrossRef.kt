package com.unsa.unsaconnect.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "news_category_cross_ref",
    primaryKeys = ["newsId", "categoryId"],
    foreignKeys = [
        ForeignKey(
            entity = New::class,
            parentColumns = ["id"],
            childColumns = ["newsId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["newsId"]),
        Index(value = ["categoryId"])
    ]
)
data class NewsCategoryCrossRef(
    val newsId: Int,
    val categoryId: Int
)
