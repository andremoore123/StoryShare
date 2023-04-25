package com.andre.storyshare.data.remote.repository

import com.andre.storyshare.data.model.AuthResponse
import com.andre.storyshare.data.model.DataStoryUpload
import com.andre.storyshare.data.model.ListStoryData
import com.andre.storyshare.data.model.LoginUser
import com.andre.storyshare.data.model.RegistUser
import retrofit2.Call
import retrofit2.Response


class DicodingApiRepository constructor(
    private val userService: DicodingApiService
) {

    suspend fun register(user: RegistUser): Response<AuthResponse> {
        return userService. registUser(user)
    }
    suspend fun login(user: LoginUser):  Response<AuthResponse>{
        return userService.loginUser(user)
    }

    suspend fun allStories(bearerToken: String): ListStoryData{
        return userService.getAllStory("Bearer $bearerToken")
    }

    suspend fun uploadStory(bearerToken: String, dataStoryUpload: DataStoryUpload): AuthResponse{
        return userService.uploadStory("Bearer $bearerToken",
        dataStoryUpload.description,
        dataStoryUpload.photo,
        dataStoryUpload.lat,
        dataStoryUpload.lon)
    }
}