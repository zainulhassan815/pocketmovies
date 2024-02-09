package org.dreamerslab.pocketmovies.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.dreamerslab.pocketmovies.utils.Logger
import org.dreamerslab.pocketmovies.utils.TimberLogger
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface LoggingModule {

    @Singleton
    @Binds
    fun bindLogger(logger: TimberLogger): Logger

}