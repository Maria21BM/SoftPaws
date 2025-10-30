package com.mariabuliga.softpaws.presentation.di

import com.mariabuliga.softpaws.data.datasource.CacheDataSource
import com.mariabuliga.softpaws.data.datasource.RoomDataSource
import com.mariabuliga.softpaws.data.datasource.RetrofitApiDataSource
import com.mariabuliga.softpaws.domain.repository.CatRepository
import com.mariabuliga.softpaws.domain.repository.CatRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideCatRepository(
        retrofitApiDataSource: RetrofitApiDataSource,
        roomDataSource: RoomDataSource,
        cacheDataSource: CacheDataSource
    ): CatRepository {
        return CatRepositoryImpl(retrofitApiDataSource, roomDataSource, cacheDataSource)
    }

}