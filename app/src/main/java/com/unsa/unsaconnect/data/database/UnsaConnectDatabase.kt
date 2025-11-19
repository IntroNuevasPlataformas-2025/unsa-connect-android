package com.unsa.unsaconnect.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.unsa.unsaconnect.data.daos.NewsDao
import com.unsa.unsaconnect.data.models.Category
import com.unsa.unsaconnect.data.models.New
import com.unsa.unsaconnect.data.models.NewsCategoryCrossRef

@Database(
    entities = [
        New::class,
        Category::class,
        NewsCategoryCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
abstract class UnsaConnectDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao

    companion object {
        @Volatile
        private var INSTANCE: UnsaConnectDatabase? = null

        fun getDatabase(context: Context): UnsaConnectDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UnsaConnectDatabase::class.java,
                    "unsaconnect_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
