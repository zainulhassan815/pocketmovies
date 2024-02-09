package org.dreamerslab.pocketmovies.di

import arrow.retrofit.adapter.either.EitherCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.dreamerslab.pocketmovies.data.api.YtsApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideOkhttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.HEADERS)
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl("https://yts.mx/")
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(EitherCallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideYtsApi(retrofit: Retrofit): YtsApi {
        return retrofit.create(YtsApi::class.java)
    }

}