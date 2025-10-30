package com.mariabuliga.softpaws.data.datasource

import com.mariabuliga.softpaws.data.model.CatDataItem
import retrofit2.Response

interface RetrofitApiDataSource {

    suspend fun getCats(
        limit: Int,
        page: Int
    ): Response<ArrayList<CatDataItem>>

    suspend fun getCatsByName(
        query: String?
    ): Response<ArrayList<CatDataItem>>

}