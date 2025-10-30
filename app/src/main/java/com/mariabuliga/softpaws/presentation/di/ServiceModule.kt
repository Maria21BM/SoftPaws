package com.mariabuliga.softpaws.presentation.di

import android.content.Context
import com.mariabuliga.softpaws.data.api.CatService
import com.mariabuliga.softpaws.data.api.RetrofitInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Singleton
    @Provides
    fun providesApiService(@ApplicationContext context: Context): CatService {
        return RetrofitInstance.getApi(context)
    }

}