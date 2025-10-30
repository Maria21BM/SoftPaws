package com.mariabuliga.softpaws.data.api

import com.mariabuliga.softpaws.data.model.CatDataItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CatService {

    @GET("v1/breeds")
    // https://api.thecatapi.com/v1/breeds?limit=10&page=0
    suspend fun getCats(
        @Header("x-api-key") apiKey: String,
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ): Response<ArrayList<CatDataItem>>


    @GET("v1/breeds/search")
    suspend fun searchCats(
        @Query("q") query: String?
    ): Response<ArrayList<CatDataItem>>


}