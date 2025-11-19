package com.unsa.unsaconnect.data.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

// Data Class para las noticias
// Aún no existe Data Class para las etiquetas/categorías
@Entity(tableName = "news")
data class New(
    @PrimaryKey
    val id: Int,
    val title: String,
    @Ignore
    val categories: List<Category>,
    val content: String,
    @Ignore
    val image: Int, // Temporalmente solo se trabaja con imágenes en local
    val publishedAt: String, // Utilizar Date o LocalDateTime en las fechas (especialmente para calcular el "Time Ago")
    val createdAt: String,
    val updatedAt: String,
    val author: String,
    val source: String,
    val isFavorite: Boolean = false
)