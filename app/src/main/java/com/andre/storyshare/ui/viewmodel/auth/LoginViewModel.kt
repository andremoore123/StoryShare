package com.andre.storyshare.ui.viewmodel.auth

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andre.storyshare.data.model.LoginResponse
import com.andre.storyshare.data.model.LoginUser
import com.andre.storyshare.data.remote.api.ApiConfig
import com.andre.storyshare.data.remote.repository.DicodingApiRepository
import com.andre.storyshare.datastore.DataStoreManager
import com.andre.storyshare.datastore.DataStoreSingleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.Exception

class LoginViewModel : ViewModel(), CoroutineScope by MainScope() {
    private val repository = DicodingApiRepository(ApiConfig.getInstance().getService())

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isSuccessLogin = MutableLiveData<Boolean>()
    val isSuccessLogin: LiveData<Boolean> = _isSuccessLogin

    private val dataStore = DataStoreSingleton.getDataStore()
    private val dataStoreManager = DataStoreManager(dataStore)

    fun login(user: LoginUser){
        _isError.value = false
        launch {
            try{
                _isLoading.value = true
                val response = repository.login(user)
                _isSuccessLogin.value = response.error
                dataStoreManager.saveLoginResult(response.loginResult)
                _message.value = response.message
            } catch (e: Exception){
                _isError.value = true
                _message.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}