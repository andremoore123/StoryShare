package com.andre.storyshare.ui.viewmodel.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andre.storyshare.data.model.LoginResponse
import com.andre.storyshare.datastore.DataStoreManager
import com.andre.storyshare.datastore.DataStoreSingleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel(), CoroutineScope by MainScope() {
    private val dataStore = DataStoreSingleton.getDataStore()
    private val dataStoreManager = DataStoreManager(dataStore)

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    fun getUserName(){
        launch{
            _userName.value = dataStoreManager.getName()
        }
    }

    fun logOut(){
        launch {
            dataStoreManager.saveLoginResult(LoginResponse(
                "",
                "",
                ""
            ))
            _userName.value = ""
        }
    }
}