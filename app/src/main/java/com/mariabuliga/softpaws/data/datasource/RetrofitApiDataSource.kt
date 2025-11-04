package com.mariabuliga.softpaws.data.datasource

import com.mariabuliga.softpaws.data.api.ApiResult
import com.mariabuliga.softpaws.data.model.CatDataItem

interface RetrofitApiDataSource {

    suspend fun getCats(
        limit: Int,
        page: Int
    ): ApiResult<ArrayList<CatDataItem>>

    suspend fun getCatsByName(
        query: String?
    ): ApiResult<ArrayList<CatDataItem>>

}