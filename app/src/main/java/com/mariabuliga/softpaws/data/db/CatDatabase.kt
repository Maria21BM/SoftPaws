package com.mariabuliga.softpaws.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mariabuliga.softpaws.data.model.CatDataItem

@Database(entities = [CatDataItem::class], version = 1, exportSchema = false)
@TypeConverters(ImageTypeConverter::class, WeightConverter::class)
abstract class CatDatabase : RoomDatabase() {

    abstract fun catDao(): CatDao

}