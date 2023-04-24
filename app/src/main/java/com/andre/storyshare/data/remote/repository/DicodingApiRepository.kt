package com.andre.storyshare.data.remote.repository

import com.andre.storyshare.data.model.AuthResponse
import com.andre.storyshare.data.model.LoginUser
import com.andre.storyshare.data.model.RegistUser

class DicodingApiRepository constructor(
    private val userService: DicodingApiService
) {
    suspend fun register(user: RegistUser): AuthResponse{
        return userService.registUser(user)
    }
    suspend fun login(user: LoginUser): AuthResponse{
        return userService.loginUser(user)
    }
}