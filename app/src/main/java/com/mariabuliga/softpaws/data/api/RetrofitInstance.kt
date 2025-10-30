package com.mariabuliga.softpaws.data.api

import android.content.Context
import com.mariabuliga.softpaws.BuildConfig
import com.mariabuliga.softpaws.utils.ErrorHandlingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    const val BASE_URL = BuildConfig.BASE_URL

    fun getApi(context: Context): CatService {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(ErrorHandlingInterceptor(context))
            .build()


        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CatService::class.java)
    }

}