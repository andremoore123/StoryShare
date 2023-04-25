package com.andre.storyshare.ui.viewmodel.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andre.storyshare.data.model.AuthResponse
import com.andre.storyshare.data.model.LoginUser
import com.andre.storyshare.data.model.RegistUser
import com.andre.storyshare.data.remote.api.ApiConfig
import com.andre.storyshare.data.remote.repository.DicodingApiRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class RegisterViewModel : ViewModel(), CoroutineScope by MainScope() {
    private val repository = DicodingApiRepository(ApiConfig.getInstance().getService())

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    fun register(user: RegistUser) {
        launch {
            _isLoading.value = true
            val response = repository.register(user)
            if (response.isSuccessful) {
                _message.value = response.body()?.message
                _isSuccess.value = true
            } else {
                val errorBody = response.errorBody()?.string()
                val jsonObject = errorBody?.let { JSONObject(it) }
                if (jsonObject != null) {
                    _message.value = jsonObject.get("message").toString()
                }
            }
            _isLoading.value = false
        }
    }
}