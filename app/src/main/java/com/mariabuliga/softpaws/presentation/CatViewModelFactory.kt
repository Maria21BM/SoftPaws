package com.mariabuliga.softpaws.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mariabuliga.softpaws.domain.usecases.GetCatsUseCase

class CatViewModelFactory(
    private val getCatsUseCase: GetCatsUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CatsViewModel(getCatsUseCase) as T
    }

}