package com.mariabuliga.softpaws.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Weight(
    val imperial: String,
    val metric: String
) : java.io.Serializable