package com.mariabuliga.softpaws.presentation.di

import com.mariabuliga.softpaws.data.datasource.RoomDataSource
import com.mariabuliga.softpaws.data.datasourceimpl.RoomDataSourceImpl
import com.mariabuliga.softpaws.data.db.CatDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDataModule {

    @Singleton
    @Provides
    fun provideCatLocalDataSource(catDao: CatDao): RoomDataSource {
        return RoomDataSourceImpl(catDao)
    }

}