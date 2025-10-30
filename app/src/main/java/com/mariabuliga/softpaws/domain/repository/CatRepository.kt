package com.mariabuliga.softpaws.domain.repository

import com.mariabuliga.softpaws.data.model.CatDataItem

interface CatRepository {

    suspend fun getCats(): List<CatDataItem>?

    suspend fun loadNextCats(): List<CatDataItem>

    suspend fun saveCats(): List<CatDataItem>?

    suspend fun getCatsByName(query: String?) : List<CatDataItem>?

}