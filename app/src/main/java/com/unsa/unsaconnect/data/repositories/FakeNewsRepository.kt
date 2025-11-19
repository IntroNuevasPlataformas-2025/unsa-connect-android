package com.unsa.unsaconnect.data.repositories

import javax.inject.Inject

import com.unsa.unsaconnect.R
import com.unsa.unsaconnect.data.models.Category
import com.unsa.unsaconnect.data.models.New
import com.unsa.unsaconnect.domain.repositories.NewsRepository

class FakeNewsRepository @Inject constructor() : NewsRepository {

    private val sampleCategories = listOf(
        Category(1, "Becas"),
        Category(2, "Congresos"),
        Category(3, "General")
    )

    private val sampleNews = listOf(
        New(
            id = 1,
            title = "Convocatoria a Becas de Investigación 2025",
            categories = listOf(sampleCategories[0]),
            content = "La universidad anuncia la apertura de las becas de investigación para el año académico 2025. Se invita a los estudiantes de pregrado y posgrado a postular con sus proyectos.",
            image = R.drawable.image_1,
            publishedAt = "2025-10-14T09:00:00",
            createdAt = "2025-10-14T09:00:00",
            updatedAt = "2025-10-14T09:00:00",
            author = "Oficina Universitaria de Imagen Institucional",
            source = "Campus Central"
        ),
        New(
            id = 2,
            title = "Congreso Internacional de Ingeniería 2025",
            categories = listOf(sampleCategories[1]),
            content = "Se realizará el Congreso Internacional de Ingeniería en la ciudad de Arequipa del 15 al 17 de noviembre. Se contará con ponentes nacionales e internacionales.",
            image = R.drawable.image_2,
            publishedAt = "2025-09-25T10:00:00",
            createdAt = "2025-10-14T09:00:00",
            updatedAt = "2025-10-14T09:00:00",
            author = "Oficina Universitaria de Imagen Institucional",
            source = "Campus Central"
        ),
        New(
            id = 3,
            title = "Suspensión de Clases por Mantenimiento",
            categories = listOf(sampleCategories[2]),
            content = "Debido a trabajos de mantenimiento en el campus central, las clases del 20 de octubre se suspenden para todas las facultades.",
            image = R.drawable.image_3,
            publishedAt = "2025-10-14T09:00:00Z",
            createdAt = "2025-10-14T09:00:00Z",
            updatedAt = "2025-10-14T09:00:00Z",
            author = "Oficina Universitaria de Imagen Institucional",
            source = "Campus Central"
        ),
        New(
            id = 4,
            title = "Convocatoria a Becas de Investigación 2025",
            categories = listOf(sampleCategories[0]),
            content = "La universidad anuncia la apertura de las becas de investigación para el año académico 2025. Se invita a los estudiantes de pregrado y posgrado a postular con sus proyectos.",
            image = R.drawable.image_1,
            publishedAt = "2025-10-14T09:00:00",
            createdAt = "2025-10-14T09:00:00",
            updatedAt = "2025-10-14T09:00:00",
            author = "Oficina Universitaria de Imagen Institucional",
            source = "Campus Central"
        ),
        New(
            id = 5,
            title = "Congreso Internacional de Ingeniería 2025",
            categories = listOf(sampleCategories[1]),
            content = "Se realizará el Congreso Internacional de Ingeniería en la ciudad de Arequipa del 15 al 17 de noviembre. Se contará con ponentes nacionales e internacionales.",
            image = R.drawable.image_2,
            publishedAt = "2025-09-25T10:00:00",
            createdAt = "2025-10-14T09:00:00",
            updatedAt = "2025-10-14T09:00:00",
            author = "Oficina Universitaria de Imagen Institucional",
            source = "Campus Central"
        ),
        New(
            id = 6,
            title = "Suspensión de Clases por Mantenimiento",
            categories = listOf(sampleCategories[2]),
            content = "Debido a trabajos de mantenimiento en el campus central, las clases del 20 de octubre se suspenden para todas las facultades.",
            image = R.drawable.image_3,
            publishedAt = "2025-10-14T09:00:00Z",
            createdAt = "2025-10-14T09:00:00Z",
            updatedAt = "2025-10-14T09:00:00Z",
            author = "Oficina Universitaria de Imagen Institucional",
            source = "Campus Central"
        )
    )

    override fun getHighlightedNews(): Flow<List<New>> {
        return flowOf(sampleNews.take(3))
    }

    override fun getRecentNews(): Flow<List<New>> {
        return flowOf(sampleNews)
    }

    override fun getCategories(): Flow<List<Category>> {
        return flowOf(sampleCategories)
    }

    override fun getNewsByCategory(categoryId: Int): Flow<List<New>> {
        return flowOf(emptyList())
    }

    override fun getFavorites(): Flow<List<New>> {
        return flowOf(emptyList())
    }

    override suspend fun setFavorite(newsId: Int, isFavorite: Boolean) {
        // No-op
    }
}
