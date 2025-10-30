package com.mariabuliga.softpaws.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mariabuliga.softpaws.data.model.CatDataItem
import com.mariabuliga.softpaws.domain.usecases.GetCatsUseCase
import com.mariabuliga.softpaws.utils.NetworkUtils
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

    private val _emptyList = MutableLiveData<Boolean>()
    val emptyListLD: LiveData<Boolean> = _emptyList

    private val _loading = MutableLiveData<Boolean>()
    val loadingLD: LiveData<Boolean> = _loading

    private val _errorLiveData = MutableLiveData<Pair<Boolean, String>>()
    val errorLiveData: LiveData<Pair<Boolean, String>> = _errorLiveData

    private val currentList = mutableListOf<CatDataItem>()

    fun checkNetworkConnectivity(context: Context) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            _errorLiveData.postValue(Pair(true, "No Internet Connection."))
        }
    }

    fun loadInitialCats(context: Context) {
        checkNetworkConnectivity(context)
        _loading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            val catList = getCatsUseCase.execute().orEmpty()
            currentList.clear()
            currentList.addAll(catList)
            _cats.postValue(currentList)
            _loading.postValue(false)
        }
    }

    fun loadNextPage(context: Context) {
        checkNetworkConnectivity(context)
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

    fun searchCatByName(context: Context, query: String?) {
        checkNetworkConnectivity(context)
        viewModelScope.launch(Dispatchers.IO) {
            val cats = getCatsUseCase.searchCatsByName(query)
            if (!cats.isNullOrEmpty()) {
                _cats.postValue(cats.toList())
            } else {
                _emptyList.postValue(true)
            }
        }
    }

    fun refreshCats(context: Context){
        loadInitialCats(context)
    }

}