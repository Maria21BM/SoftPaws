package com.mariabuliga.softpaws.data.datasourceimpl

import com.mariabuliga.softpaws.data.datasource.CacheDataSource
import com.mariabuliga.softpaws.data.model.CatDataItem

class CacheDataSourceImpl : CacheDataSource {

    private var catList = ArrayList<CatDataItem>()


    override suspend fun getCatsFromCache(): List<CatDataItem> {
        return catList
    }

    override suspend fun saveCatsToCache(cats: List<CatDataItem>) {
        val newUniqueCats = cats.filter { newCat ->
            catList.none { cachedCat -> cachedCat.id == newCat.id }
        }
        catList.addAll(newUniqueCats)
    }

}