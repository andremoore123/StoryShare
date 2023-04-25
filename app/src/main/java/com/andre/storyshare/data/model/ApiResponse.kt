package com.andre.storyshare.data.model

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

data class AuthResponse(
    @field:SerializedName("error")
    val error: Boolean,
    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("loginResult")
    val loginResult: LoginResponse
)

data class LoginResponse(
    @field:SerializedName("userId")
    val uuid: String,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("token")
    val token: String
)

data class ListStoryData(
    @field:SerializedName("listStory")
    val listStory: List<DataStory>
)

data class DataStory(
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("description")
    val description: String,
    @field:SerializedName("photoUrl")
    val photoUrl: String,
    @field:SerializedName("createdAt")
    val createdAt: String,
    @field:SerializedName("lat")
    val lat: Float,
    @field:SerializedName("lon")
    val lon: Float,
)
data class RegistUser(
    val name: String,
    val email: String,
    val password: String
)

data class LoginUser(
    val email: String,
    val password: String
)

data class DataStoryUpload(
    @Part("description") val description: RequestBody,
    @Part val photo: MultipartBody.Part,
    @Part("lat") val lat: RequestBody?,
    @Part("lon") val lon: RequestBody?
)
