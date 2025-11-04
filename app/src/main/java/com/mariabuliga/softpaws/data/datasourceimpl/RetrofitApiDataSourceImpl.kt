package com.mariabuliga.softpaws.data.datasourceimpl

import com.mariabuliga.softpaws.data.api.ApiResult
import com.mariabuliga.softpaws.data.api.CatService
import com.mariabuliga.softpaws.data.api.handleApi
import com.mariabuliga.softpaws.data.datasource.RetrofitApiDataSource
import com.mariabuliga.softpaws.data.model.CatDataItem
import javax.inject.Inject

class RetrofitApiDataSourceImpl @Inject constructor(
    private val catService: CatService,
    private val apiKey: String
) : RetrofitApiDataSource {

    override suspend fun getCats(limit: Int, page: Int): ApiResult<ArrayList<CatDataItem>> {
        return handleApi { catService.getCats(apiKey, limit, page) }
    }

    override suspend fun getCatsByName(query: String?): ApiResult<ArrayList<CatDataItem>> {
        return handleApi { catService.searchCats(apiKey, query) }
    }

}