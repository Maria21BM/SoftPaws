package com.mariabuliga.softpaws.domain.repository

import android.util.Log
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
        val newListOfCats = getCatsFromAPI(true)
        roomDataSource.clearAll()
        roomDataSource.saveCatsToDB(newListOfCats)
        cacheDataSource.saveCatsToCache(newListOfCats)
        return newListOfCats
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

        if (roomCats.isNotEmpty()) {
            catList = ArrayList(roomCats)
            catList
        } else {
            val apiCats = getCatsFromAPI(true)
            roomDataSource.saveCatsToDB(apiCats)
            apiCats
        }
        return catList
    }

    private suspend fun getCatsFromAPI(initialLoad: Boolean): ArrayList<CatDataItem> {
        val newCats = ArrayList<CatDataItem>()

        withTimeout(30.seconds) {
            try {
                val response = retrofitApiDataSource.getCats(Constants.LIMIT, currentPage)
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.let { cats ->
                        val filteredCats = cats.filter { it.id.isNotEmpty() }
                        newCats.addAll(filteredCats)

                        if (initialLoad) {
                            catList = ArrayList(filteredCats)
                        } else {
                            catList.addAll(filteredCats)
                        }
                    }
                } else if (response.code() == 429) {
                    Log.e(Constants.TAG, "Rate limit exceeded. Try again later.")
                } else {
                    Log.e(Constants.TAG, "Cats API Error: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e(Constants.TAG, e.message.toString())
            }
        }

        return newCats
    }

    override suspend fun loadNextCats(): List<CatDataItem> {
        val newCats = getCatsFromAPI(false)
        if (newCats.isNotEmpty()) {
            currentPage++
            roomDataSource.saveCatsToDB(newCats)
            cacheDataSource.saveCatsToCache(newCats)
        }

        Log.d("Pagination", "Calling API page = $currentPage, returned ${newCats.size} cats")

        return newCats
    }

    override suspend fun getCatsByName(query: String?): ArrayList<CatDataItem> {
        var results: ArrayList<CatDataItem> = ArrayList()
        try {
            val response = retrofitApiDataSource.getCatsByName(query)
            if (response.isSuccessful) {
                response.body()?.let { cats ->
                    val filteredCats = cats.filter { it.id.isNotEmpty() }
                    results.addAll(filteredCats)
                    searchResults.addAll(filteredCats)
                }
            } else {
                Log.e(Constants.TAG, "Search API Error: ${response.code()} ${response.message()}")
            }

        } catch (e: Exception) {
            Log.e(Constants.TAG, e.message.toString())
        }
        return results
    }

}