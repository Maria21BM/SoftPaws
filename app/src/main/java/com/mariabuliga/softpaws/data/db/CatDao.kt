package com.mariabuliga.softpaws.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mariabuliga.softpaws.data.model.CatDataItem

@Dao
interface CatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCats(cats: List<CatDataItem>)

    @Query("SELECT * FROM cats")
    suspend fun getCats(): List<CatDataItem>

    @Query("DELETE FROM cats")
    suspend fun deleteAllCats()

    @Query("SELECT * FROM cats WHERE name LIKE :query")
    suspend fun searchCatsByName(query: String): List<CatDataItem>

}