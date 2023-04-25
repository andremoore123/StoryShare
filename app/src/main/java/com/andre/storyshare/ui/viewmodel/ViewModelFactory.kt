package com.andre.storyshare.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.andre.storyshare.ui.viewmodel.auth.LoginViewModel
import com.andre.storyshare.ui.viewmodel.auth.RegisterViewModel
import com.andre.storyshare.ui.viewmodel.home.HomeViewModel
import com.andre.storyshare.ui.viewmodel.post.PostViewModel
import com.andre.storyshare.ui.viewmodel.profile.ProfileViewModel

class ViewModelFactory private constructor(): ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory()
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Tambahkan ViewModel lain jika perlu disini...
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel() as T
        }
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel() as T
        }
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel() as T
        }
        if (modelClass.isAssignableFrom(PostViewModel::class.java)) {
            return PostViewModel() as T
        }
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel!")
    }
}