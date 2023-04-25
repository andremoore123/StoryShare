package com.andre.storyshare.data.remote.api

import com.andre.storyshare.data.remote.repository.DicodingApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://story-api.dicoding.dev/v1/"

class ApiConfig private constructor() {
    fun getService(): DicodingApiService {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(DicodingApiService::class.java)
    }

    companion object {
        @Volatile
        private var INSTANCE: ApiConfig? = null

        fun getInstance(): ApiConfig =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ApiConfig().also { INSTANCE = it }
            }
    }
}