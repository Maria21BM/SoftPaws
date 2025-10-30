package com.mariabuliga.softpaws.data.datasource

import com.mariabuliga.softpaws.data.model.CatDataItem

interface RoomDataSource {

    suspend fun getCatsFromDB(): List<CatDataItem>

    suspend fun saveCatsToDB(cats: List<CatDataItem>)

    suspend fun clearAll()

    suspend fun searchCatsByName(query: String): List<CatDataItem>

}