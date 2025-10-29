package com.mariabuliga.softpaws.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mariabuliga.softpaws.data.model.Weight

class WeightConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromWeight(weight: Weight?): String? {
        return if (weight == null) null else gson.toJson(weight)
    }

    @TypeConverter
    fun toWeight(json: String?): Weight? {
        if (json == null) return null
        val type = object : TypeToken<Weight>() {}.type
        return gson.fromJson(json, type)
    }

}