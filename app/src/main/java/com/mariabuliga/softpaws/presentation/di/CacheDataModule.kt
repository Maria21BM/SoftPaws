package com.mariabuliga.softpaws.presentation.di

import com.mariabuliga.softpaws.data.datasource.CacheDataSource
import com.mariabuliga.softpaws.data.datasourceimpl.CacheDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CacheDataModule {

    @Singleton
    @Provides
    fun provideCatCachedDataSource(): CacheDataSource{
        return CacheDataSourceImpl()
    }

}