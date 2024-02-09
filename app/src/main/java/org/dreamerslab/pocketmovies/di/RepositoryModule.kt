package org.dreamerslab.pocketmovies.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.dreamerslab.pocketmovies.data.api.YtsApi
import org.dreamerslab.pocketmovies.data.repository.YtsMoviesRepository
import org.dreamerslab.pocketmovies.utils.AppCoroutineDispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideYtsMoviesRepository(
        ytsApi: YtsApi,
        dispatchers: AppCoroutineDispatchers
    ): YtsMoviesRepository = YtsMoviesRepository(
        ytsApi = ytsApi,
        dispatchers = dispatchers,
    )
}