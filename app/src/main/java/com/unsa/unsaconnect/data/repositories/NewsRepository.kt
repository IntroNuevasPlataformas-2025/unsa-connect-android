package com.unsa.unsaconnect.data.repositories

import com.unsa.unsaconnect.R
import com.unsa.unsaconnect.data.models.New

//object NewsRepository { ... } (algo así encontré por ahí para singleton)

fun getHighlightedNews(): List<New> {
    val highlightedNewsList = listOf(
        New(
            id = 1,
            title = "Convocatoria a Becas de Investigación 2025",
            categories = listOf(
                "Becas"
            ),
            content = "La universidad anuncia la apertura de las becas de investigación para el año académico 2025. Se invita a los estudiantes de pregrado y posgrado a postular con sus proyectos.",
            image = R.drawable.image_1,
            publishedAt = "2025-10-14T09:00:00",
            createdAt = "2025-10-14T09:00:00",
            updatedAt = "2025-10-14T09:00:00"
        ),
        New(
            id = 2,
            title = "Congreso Internacional de Ingeniería 2025",
            categories = listOf(
                "Congresos"
            ),
            content = "Se realizará el Congreso Internacional de Ingeniería en la ciudad de Arequipa del 15 al 17 de noviembre. Se contará con ponentes nacionales e internacionales.",
            image = R.drawable.image_2,
            publishedAt = "2025-09-25T10:00:00",
            createdAt = "2025-10-14T09:00:00",
            updatedAt = "2025-10-14T09:00:00"
        ),
        New(
            id = 3,
            title = "Suspensión de Clases por Mantenimiento",
            categories = listOf(
                "General"
            ),
            content = "Debido a trabajos de mantenimiento en el campus central, las clases del 20 de octubre se suspenden para todas las facultades.",
            image = R.drawable.image_3,
            publishedAt = "2025-10-14T09:00:00Z",
            createdAt = "2025-10-14T09:00:00Z",
            updatedAt = "2025-10-14T09:00:00Z"
        ),
    )

    return highlightedNewsList
}