package com.andre.storyshare.data.remote.repository

import androidx.lifecycle.LiveData
import com.andre.storyshare.data.model.AuthResponse
import com.andre.storyshare.data.model.DataStoryUpload
import com.andre.storyshare.data.model.ListStoryData
import com.andre.storyshare.data.model.LoginUser
import com.andre.storyshare.data.model.RegistUser
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface DicodingApiService {
    @POST("register")
    suspend fun registUser(@Body user: RegistUser): Response<AuthResponse>

    @POST("login")
    suspend fun loginUser(@Body user: LoginUser): Response<AuthResponse>

    @GET("stories")
    suspend fun getAllStory(@Header("Authorization") bearerToken: String): ListStoryData

    @POST("stories")
    @Multipart
    suspend fun uploadStory(
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?
    ): AuthResponse
}