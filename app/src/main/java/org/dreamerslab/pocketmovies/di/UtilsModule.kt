package org.dreamerslab.pocketmovies.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import org.dreamerslab.pocketmovies.utils.AppCoroutineDispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilsModule {

    @Provides
    @Singleton
    fun provideAppCoroutineDispatchers(): AppCoroutineDispatchers {
        return AppCoroutineDispatchers(
            io = Dispatchers.IO,
            default = Dispatchers.Default,
            main = Dispatchers.Main
        )
    }

}