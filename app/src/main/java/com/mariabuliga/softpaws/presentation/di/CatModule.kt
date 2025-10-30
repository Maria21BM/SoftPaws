package com.mariabuliga.softpaws.presentation.di

import com.mariabuliga.softpaws.domain.usecases.GetCatsUseCase
import com.mariabuliga.softpaws.presentation.CatViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class CatModule {

    @Provides
    fun provideCatViewModelFactory(
        getCatsUseCase: GetCatsUseCase
    ): CatViewModelFactory {
        return CatViewModelFactory(getCatsUseCase)
    }

}