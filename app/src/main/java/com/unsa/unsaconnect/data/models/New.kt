package com.unsa.unsaconnect.data.models

// Data Class para las noticias
// Aún no existe Data Class para las etiquetas/categorías
data class New(
    val id: Int,
    val title: String,
    val categories: List<Category>,
    val content: String,
    val image: Int, // Temporalmente solo se trabaja con imágenes en local
    val publishedAt: String, // Utilizar Date o LocalDateTime en las fechas (especialmente para calcular el "Time Ago")
    val createdAt: String,
    val updatedAt: String,
)