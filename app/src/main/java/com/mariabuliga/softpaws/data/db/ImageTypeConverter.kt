package com.mariabuliga.softpaws.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mariabuliga.softpaws.data.model.Image

class ImageTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromImage(image: Image?): String? {
        return if (image == null) null else gson.toJson(image)
    }

    @TypeConverter
    fun toImage(json: String?): Image? {
        if (json == null) return null
        val type = object : TypeToken<Image>() {}.type
        return gson.fromJson(json, type)
    }
}