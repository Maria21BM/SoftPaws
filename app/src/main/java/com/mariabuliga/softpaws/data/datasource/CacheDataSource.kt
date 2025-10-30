package com.mariabuliga.softpaws.data.datasource

import com.mariabuliga.softpaws.data.model.CatDataItem

interface CacheDataSource {

    suspend fun getCatsFromCache(): List<CatDataItem>

    suspend fun saveCatsToCache(cats: List<CatDataItem>)

}