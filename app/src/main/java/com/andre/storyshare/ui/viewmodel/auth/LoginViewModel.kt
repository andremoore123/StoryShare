package com.andre.storyshare.ui.viewmodel.auth

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andre.storyshare.data.model.AuthResponse
import com.andre.storyshare.data.model.LoginResponse
import com.andre.storyshare.data.model.LoginUser
import com.andre.storyshare.data.remote.api.ApiConfig
import com.andre.storyshare.data.remote.repository.DicodingApiRepository
import com.andre.storyshare.datastore.DataStoreManager
import com.andre.storyshare.datastore.DataStoreSingleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import kotlin.Exception

class LoginViewModel : ViewModel(), CoroutineScope by MainScope() {
    private val repository = DicodingApiRepository(ApiConfig.getInstance().getService())

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isSuccessLogin = MutableLiveData<Boolean>()
    val isSuccessLogin: LiveData<Boolean> = _isSuccessLogin

    private val dataStore = DataStoreSingleton.getDataStore()
    private val dataStoreManager = DataStoreManager(dataStore)


    fun login(user: LoginUser){
        _isSuccessLogin.value = false
        launch {
            _isLoading.value = true
            val response = repository.login(user)
            if (response.isSuccessful){
                response.body()?.let { dataStoreManager.saveLoginResult(it.loginResult) }
                _isSuccessLogin.value = true
            } else {
                val errorBody = response.errorBody()?.string()
                val jsonObject = errorBody?.let { JSONObject(it) }
                if (jsonObject != null) {
                    _message.value =  jsonObject.get("message").toString()
                }
            }
            _isLoading.value = false
        }
    }
}