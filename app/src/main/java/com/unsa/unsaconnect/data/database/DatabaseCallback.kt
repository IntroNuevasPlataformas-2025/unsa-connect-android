package com.unsa.unsaconnect.data.database

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.unsa.unsaconnect.data.models.Category
import com.unsa.unsaconnect.data.models.New
import com.unsa.unsaconnect.data.models.NewsCategoryCrossRef
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Provider
import com.unsa.unsaconnect.R

class DatabaseCallback(
    // Use Provider to break circular dependency
    private val database: Provider<UnsaConnectDatabase>
) : RoomDatabase.Callback() {

    private val applicationScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        applicationScope.launch {
            populateDatabase()
        }
    }

    private suspend fun populateDatabase() {
        val newsDao = database.get().newsDao()

        // --- Test Data ---
        val categories = listOf(
            Category(1, "Becas"),
            Category(2, "Congresos"),
            Category(3, "General")
        )

        // Helper data class to manage relations
        data class NewsData(val new: New, val categoryIds: List<Int>)

        val newsData = listOf(
            NewsData(
                New(
                    1,
                    "Convocatoria a Becas de Investigación 2025",
                    "La universidad anuncia la apertura de las becas de investigación...",
                    "2025-10-14T09:00:00",
                    "2025-10-14T09:00:00",
                    "2025-10-14T09:00:00",
                    "Oficina Universitaria de Imagen Institucional",
                    "Campus Central",
                    image = R.drawable.image_1
                ),
                listOf(1)
            ),
            NewsData(
                New(
                    2,
                    "Congreso Internacional de Ingeniería 2025",
                    "Se realizará el Congreso Internacional de Ingeniería...",
                    "2025-09-25T10:00:00",
                    "2025-10-14T09:00:00",
                    "2025-10-14T09:00:00",
                    "Oficina Universitaria de Imagen Institucional",
                    "Campus Central",
                    image = R.drawable.image_2
                ),
                listOf(2)
            ),
            NewsData(
                New(
                    3,
                    "Suspensión de Clases por Mantenimiento",
                    "Debido a trabajos de mantenimiento en el campus central...",
                    "2025-10-14T09:00:00Z",
                    "2025-10-14T09:00:00Z",
                    "2025-10-14T09:00:00Z",
                    "Oficina Universitaria de Imagen Institucional",
                    "Campus Central",
                    image = R.drawable.image_3),
                listOf(3)
            ),
            NewsData(
                New(
                    4,
                    "Convocatoria a Becas de Investigación 2025",
                    "La universidad anuncia la apertura de las becas de investigación...",
                    "2025-10-14T09:00:00",
                    "2025-10-14T09:00:00",
                    "2025-10-14T09:00:00",
                    "Oficina Universitaria de Imagen Institucional",
                    "Campus Central",
                    image = R.drawable.image_1),
                listOf(1)
            ),
            NewsData(
                New(
                    5,
                    "Congreso Internacional de Ingeniería 2025",
                    "Se realizará el Congreso Internacional de Ingeniería...",
                    "2025-09-25T10:00:00",
                    "2025-10-14T09:00:00",
                    "2025-10-14T09:00:00",
                    "Oficina Universitaria de Imagen Institucional",
                    "Campus Central",
                    image = R.drawable.image_2
                ),
                listOf(2)
            ),
            NewsData(
                New(
                    6,
                    "Suspensión de Clases por Mantenimiento",
                    "Debido a trabajos de mantenimiento en el campus central...",
                    "2025-10-14T09:00:00Z",
                    "2025-10-14T09:00:00Z",
                    "2025-10-14T09:00:00Z",
                    "Oficina Universitaria de Imagen Institucional",
                    "Campus Central",
                    image = R.drawable.image_3
                ),
                listOf(3)
            )
        )

        // Insert data into the database
        categories.forEach { newsDao.insertCategory(it) }
        newsData.forEach { data ->
            newsDao.insertNews(data.new)
            data.categoryIds.forEach { categoryId ->
                newsDao.insertNewsCategoryCrossRef(NewsCategoryCrossRef(data.new.id, categoryId))
            }
        }
    }
}
