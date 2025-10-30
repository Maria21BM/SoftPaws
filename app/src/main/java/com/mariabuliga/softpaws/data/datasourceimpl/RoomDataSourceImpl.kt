package com.mariabuliga.softpaws.data.datasourceimpl

import com.mariabuliga.softpaws.data.datasource.RoomDataSource
import com.mariabuliga.softpaws.data.db.CatDao
import com.mariabuliga.softpaws.data.model.CatDataItem

class RoomDataSourceImpl(
    private val catDao: CatDao
) : RoomDataSource {

    override suspend fun getCatsFromDB(): List<CatDataItem> {
        return catDao.getCats()
    }

    override suspend fun saveCatsToDB(cats: List<CatDataItem>) {
        catDao.saveCats(cats)
    }

    override suspend fun clearAll() {
        catDao.deleteAllCats()
    }

    override suspend fun searchCatsByName(query: String): List<CatDataItem> {
        return catDao.searchCatsByName(query)
    }
}