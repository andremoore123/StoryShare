package com.andre.storyshare.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.andre.storyshare.data.model.LoginResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


class DataStoreManager(private val dataStore: DataStore<Preferences>) {
    private lateinit var loginResult: LoginResponse
    companion object {
        val LOGIN_RESULT_KEY = stringPreferencesKey("login_result_key")
    }

    suspend fun saveLoginResult(loginResult: LoginResponse) {
        this@DataStoreManager.loginResult = loginResult
        dataStore.edit { preferences ->
            preferences[LOGIN_RESULT_KEY] = loginResult.token // save the LoginResult object
        }
    }

    suspend fun getToken(): String {
        val data =  dataStore.data.map { preferences ->
            preferences[LOGIN_RESULT_KEY] ?: ""
        }
        return data.first()
    }
}
