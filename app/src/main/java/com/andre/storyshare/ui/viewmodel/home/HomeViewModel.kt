package com.andre.storyshare.ui.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andre.storyshare.data.model.DataStory
import com.andre.storyshare.data.remote.api.ApiConfig
import com.andre.storyshare.data.remote.repository.DicodingApiRepository
import com.andre.storyshare.datastore.DataStoreManager
import com.andre.storyshare.datastore.DataStoreSingleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel(),  CoroutineScope by MainScope()  {
    private val repository = DicodingApiRepository(ApiConfig.getInstance().getService())

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _storyList = MutableLiveData<List<DataStory>>()
    val storyList: LiveData<List<DataStory>> = _storyList

    private val dataStore = DataStoreSingleton.getDataStore()
    private val dataStoreManager = DataStoreManager(dataStore)

    fun getStories(){
        _isError.value = false
        launch {
            _isLoading.value = true
            try {
                _storyList.value = repository.allStories(dataStoreManager.getToken()).listStory
            } catch (e: Exception){
                _isError.value = true
                _message.value = e.message.toString()
            } finally {
                _isLoading.value = false
            }
        }
    }
}