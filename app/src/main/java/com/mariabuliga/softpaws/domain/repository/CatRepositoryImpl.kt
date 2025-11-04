package com.mariabuliga.softpaws.domain.repository

import android.util.Log
import com.mariabuliga.softpaws.data.api.ApiResult
import com.mariabuliga.softpaws.data.datasource.CacheDataSource
import com.mariabuliga.softpaws.data.datasource.RetrofitApiDataSource
import com.mariabuliga.softpaws.data.datasource.RoomDataSource
import com.mariabuliga.softpaws.data.model.CatDataItem
import com.mariabuliga.softpaws.utils.Constants
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class CatRepositoryImpl @Inject constructor(
    private val retrofitApiDataSource: RetrofitApiDataSource,
    private val roomDataSource: RoomDataSource,
    private val cacheDataSource: CacheDataSource
) : CatRepository {

    private var currentPage = 0

    var catList: ArrayList<CatDataItem> = ArrayList()
    var searchResults: ArrayList<CatDataItem> = ArrayList()

    //Get cats from Cache Step 1
    override suspend fun getCats(): List<CatDataItem>? {
        return getCatsFromCache()
    }

    override suspend fun saveCats(): ArrayList<CatDataItem>? {
        currentPage = 0
        val result = getCatsFromAPI(true)
        return when (result) {
            is ApiResult.Success -> {
                val newListOfCats = result.data
                roomDataSource.clearAll()
                roomDataSource.saveCatsToDB(newListOfCats)
                cacheDataSource.saveCatsToCache(newListOfCats)
                newListOfCats
            }

            is ApiResult.Error -> {
                Log.e(Constants.TAG, "Api Error ${result.code} ${result.message}")
                null
            }

            is ApiResult.Exception -> {
                Log.e(Constants.TAG, "Exception ${result.message}")
                null
            }

            is ApiResult.Unauthorized -> {
                Log.e(Constants.TAG, "Unauthorized.")
                null
            }
        }
    }

    //Get cats from Cache Step 2
    private suspend fun getCatsFromCache(): List<CatDataItem>? {
        var cachedCats: List<CatDataItem> = ArrayList()
        try {
            cachedCats = cacheDataSource.getCatsFromCache()
        } catch (e: Exception) {
            Log.e(Constants.TAG, e.message.toString())
        }

        if (cachedCats.isNotEmpty()) {
            catList = ArrayList(cachedCats)
            return catList
        } else {
            val roomCats = getCatsFromROOM()
            cacheDataSource.saveCatsToCache(roomCats)
            roomCats
        }

        return catList
    }

    //Get cats from Cache Step 3
    suspend fun getCatsFromROOM(): List<CatDataItem> {
        var roomCats: List<CatDataItem> = ArrayList()
        try {
            roomCats = roomDataSource.getCatsFromDB()
        } catch (e: Exception) {
            Log.e(Constants.TAG, e.message.toString())
        }

        return if (roomCats.isNotEmpty()) {
            catList = ArrayList(roomCats)
            catList
        } else {
            val apiCats = getCatsFromAPI(true)
            when (apiCats) {
                is ApiResult.Success -> {
                    val cats = apiCats.data
                    roomDataSource.saveCatsToDB(cats)
                    catList = ArrayList(cats)
                    catList
                }

                is ApiResult.Error -> {
                    Log.e(Constants.TAG, "Api Error ${apiCats.code} ${apiCats.message}")
                    emptyList()
                }

                is ApiResult.Exception -> {
                    Log.e(Constants.TAG, "Exception ${apiCats.message}")
                    emptyList()
                }

                is ApiResult.Unauthorized -> {
                    Log.e(Constants.TAG, "Unauthorized")
                    emptyList()
                }
            }
        }
    }

    private suspend fun getCatsFromAPI(initialLoad: Boolean): ApiResult<ArrayList<CatDataItem>> {
        val newCats = ArrayList<CatDataItem>()
        val response = retrofitApiDataSource.getCats(Constants.LIMIT, currentPage)

        return withTimeout(30.seconds) {
            try {
                when (response) {
                    is ApiResult.Success -> {
                        val filteredCats = response.data.filter { it.id.isNotEmpty() }
                        newCats.addAll(filteredCats)
                        if (initialLoad) {
                            catList = ArrayList(filteredCats)
                        } else {
                            catList.addAll(filteredCats)
                        }
                        currentPage++
                        ApiResult.Success(ArrayList(filteredCats))
                    }

                    is ApiResult.Error<*> -> {
                        Log.e(Constants.TAG, "Cats Api Error ${response.code} ${response.message}")
                        ApiResult.Error(response.code, response.message, response.parsedError)
                    }

                    is ApiResult.Exception<*> -> {
                        Log.e(Constants.TAG, "Exception ${response.message}")
                        ApiResult.Exception(response.message)
                    }

                    is ApiResult.Unauthorized<*> -> {
                        Log.e(Constants.TAG, "Session expired.")
                        ApiResult.Unauthorized()
                    }
                }
            } catch (e: Exception) {
                Log.e(Constants.TAG, e.message.toString())
                ApiResult.Exception(e.message)
            }
        }
    }

    override suspend fun loadNextCats(): List<CatDataItem> {
        val newCats = getCatsFromAPI(false)
        return when (newCats) {
            is ApiResult.Success -> {
                val newCats = newCats.data
                if (newCats.isNotEmpty()) {
                    roomDataSource.saveCatsToDB(newCats)
                    cacheDataSource.saveCatsToCache(newCats)
                    Log.d(
                        "Pagination",
                        "Calling API page = $currentPage, returned ${newCats.size} cats"
                    )
                }
                newCats
            }

            else -> emptyList()
        }
    }

    override suspend fun getCatsByName(query: String?): ArrayList<CatDataItem> {
        var results: ArrayList<CatDataItem> = ArrayList()
        try {
            val response = retrofitApiDataSource.getCatsByName(query)
            return when (response) {
                is ApiResult.Success -> {
                    val filteredCats = response.data.filter { it.id.isNotEmpty() }
                    results.addAll(filteredCats)
                    searchResults.addAll(filteredCats)
                    results
                }

                is ApiResult.Error<*> -> {
                    Log.e(Constants.TAG, "Search Api Error ${response.code} ${response.message}")
                    results
                }

                is ApiResult.Exception<*> -> {
                    Log.e(Constants.TAG, "Search Api Exception ${response.message}")
                    results
                }

                is ApiResult.Unauthorized<*> -> {
                    Log.e(Constants.TAG, "Session expired.")
                    results
                }
            }
        } catch (e: Exception) {
            Log.e(Constants.TAG, e.message.toString())
        }
        return results
    }

}