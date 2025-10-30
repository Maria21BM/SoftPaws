package com.mariabuliga.softpaws.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mariabuliga.softpaws.data.model.CatDataItem
import com.mariabuliga.softpaws.domain.usecases.GetCatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatsViewModel @Inject constructor(
    private val getCatsUseCase: GetCatsUseCase
) : ViewModel() {

    private val _cats = MutableLiveData<List<CatDataItem>>()
    val catsLD: LiveData<List<CatDataItem>> = _cats

    private val _loading = MutableLiveData<Boolean>()
    val loadingLD: LiveData<Boolean> = _loading

    private val currentList = mutableListOf<CatDataItem>()

    fun loadInitialCats() {
        _loading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            val catList = getCatsUseCase.execute().orEmpty()
            currentList.clear()
            currentList.addAll(catList)
            _cats.postValue(currentList)
            _loading.postValue(false)
        }
    }

    fun loadNextPage() {
        _loading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            val nextCats = getCatsUseCase.loadNext()
            if (nextCats.isNotEmpty()) {
                currentList.addAll(nextCats)
                _cats.postValue(currentList.toList())
            }
            _loading.postValue(false)
        }
    }

    fun searchCatByName(query: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val cats = getCatsUseCase.searchCatsByName(query)
            if (!cats.isNullOrEmpty()) {
                _cats.postValue(cats.toList())
            }
        }
    }

}