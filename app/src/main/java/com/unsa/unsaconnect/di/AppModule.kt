package com.unsa.unsaconnect.di

import android.content.Context
import androidx.room.Room
import com.unsa.unsaconnect.data.daos.NewsDao
import com.unsa.unsaconnect.data.database.DatabaseCallback
import com.unsa.unsaconnect.data.database.UnsaConnectDatabase
import com.unsa.unsaconnect.data.local.SettingsManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton

import com.unsa.unsaconnect.data.repositories.NewsRepositoryImpl
import com.unsa.unsaconnect.domain.repositories.NewsRepository
import com.unsa.unsaconnect.ui.screens.SettingsScreen
import dagger.Binds

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindNewsRepository(
        newsRepositoryImpl: NewsRepositoryImpl
    ): NewsRepository

    companion object {
        @Provides
        @Singleton
        fun provideDatabaseCallback(
            database: Provider<UnsaConnectDatabase>
        ): DatabaseCallback {
            return DatabaseCallback(database)
        }

        @Provides
        @Singleton
        fun provideAppDatabase(
            @ApplicationContext context: Context,
            callback: DatabaseCallback
        ): UnsaConnectDatabase {
            return Room.databaseBuilder(
                context,
                UnsaConnectDatabase::class.java,
                "unsaconnect_database"
            ).fallbackToDestructiveMigration().addCallback(callback).build()
        }

        @Provides
        fun provideNewsDao(database: UnsaConnectDatabase): NewsDao {
            return database.newsDao()
        }

        @Provides
        @Singleton
        fun provideSettingsManager(
            @ApplicationContext context: Context
        ) : SettingsManager {
            return SettingsManager(context)
        }
    }
}
