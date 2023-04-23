package com.andre.storyshare.data.remote.repository

import androidx.lifecycle.LiveData
import com.andre.storyshare.data.model.AuthResponse
import com.andre.storyshare.data.model.ListStoryData
import com.andre.storyshare.data.model.LoginUser
import com.andre.storyshare.data.model.RegistUser
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface DicodingApiService {
    @POST("register")
    suspend fun registUser(@Body user: RegistUser): AuthResponse

    @POST("login")
    suspend fun loginUser(@Body user: LoginUser): AuthResponse

    @GET("stories")
    suspend fun getAllStory(@Header("Authorization") bearerToken: String): ListStoryData
}