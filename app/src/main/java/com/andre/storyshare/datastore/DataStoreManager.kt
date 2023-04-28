package com.andre.storyshare.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.andre.storyshare.data.model.LoginResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


class DataStoreManager(private val dataStore: DataStore<Preferences>) {
    companion object {
        val LOGIN_RESULT_KEY = stringPreferencesKey("login_result_key")
        val NAME_USER = stringPreferencesKey("login_result_name")
    }

    suspend fun saveLoginResult(loginResult: LoginResponse) {
        dataStore.edit { preferences ->
            preferences[LOGIN_RESULT_KEY] = loginResult.token // save the LoginResult object
            preferences[NAME_USER] = loginResult.name // save the LoginResult object
        }
    }

    suspend fun getToken(): String {
        val data =  dataStore.data.map { preferences ->
            preferences[LOGIN_RESULT_KEY] ?: ""
        }
        return data.first()
    }

    suspend fun getName(): String {
        val data =  dataStore.data.map { preferences ->
            preferences[NAME_USER] ?: ""
        }
        return data.first()
    }
}
