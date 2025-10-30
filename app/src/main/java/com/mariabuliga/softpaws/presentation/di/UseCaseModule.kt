package com.mariabuliga.softpaws.presentation.di

import com.mariabuliga.softpaws.domain.repository.CatRepository
import com.mariabuliga.softpaws.domain.usecases.GetCatsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Singleton
    @Provides
    fun provideGetCatUseCase(catRepository: CatRepository): GetCatsUseCase {
        return GetCatsUseCase(catRepository)
    }

}