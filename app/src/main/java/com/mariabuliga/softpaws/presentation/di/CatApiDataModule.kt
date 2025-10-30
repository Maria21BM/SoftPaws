package com.mariabuliga.softpaws.presentation.di

import com.mariabuliga.softpaws.BuildConfig
import com.mariabuliga.softpaws.data.api.CatService
import com.mariabuliga.softpaws.data.datasource.RetrofitApiDataSource
import com.mariabuliga.softpaws.data.datasourceimpl.RetrofitApiDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CatApiDataModule() {

    @Provides
    @Singleton
    fun provideApiKey(): String {
        return BuildConfig.API_KEY
    }

    @Singleton
    @Provides
    fun provideCatApiDataSource(catService: CatService, apiKey: String) : RetrofitApiDataSource {
        return RetrofitApiDataSourceImpl(catService, apiKey)
    }

}