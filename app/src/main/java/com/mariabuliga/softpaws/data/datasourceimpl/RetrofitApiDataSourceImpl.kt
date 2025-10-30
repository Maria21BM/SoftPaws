package com.mariabuliga.softpaws.data.datasourceimpl

import com.mariabuliga.softpaws.data.api.CatService
import com.mariabuliga.softpaws.data.datasource.RetrofitApiDataSource
import com.mariabuliga.softpaws.data.model.CatDataItem
import retrofit2.Response
import javax.inject.Inject

class RetrofitApiDataSourceImpl @Inject constructor(
    private val catService: CatService,
    private val apiKey: String
) : RetrofitApiDataSource {

    override suspend fun getCats(limit: Int, page: Int): Response<ArrayList<CatDataItem>> {
        return catService.getCats(apiKey, limit, page)
    }

    override suspend fun getCatsByName(query: String?): Response<ArrayList<CatDataItem>> {
        return catService.searchCats(query)
    }

}