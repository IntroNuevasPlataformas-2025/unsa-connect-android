package com.unsa.unsaconnect.di

import com.unsa.unsaconnect.data.repositories.FakeNewsRepository
import com.unsa.unsaconnect.domain.repositories.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindNewsRepository(
        fakeNewsRepository: FakeNewsRepository
    ): NewsRepository
}
