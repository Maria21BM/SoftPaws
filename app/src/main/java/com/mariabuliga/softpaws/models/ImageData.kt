package com.mariabuliga.softpaws.models

data class ImageData(
    val id: String,
    val url: String,
    val width: Int,
    val height: Int,
    val mime_type: String,
    val petData: List<PetData>,
    val categories: List<Any>
)
