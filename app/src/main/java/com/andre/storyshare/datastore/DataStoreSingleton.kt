package com.andre.storyshare.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

object DataStoreSingleton {
    private lateinit var dataStore: DataStore<Preferences>
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    fun init(context: Context) {
        dataStore = context.dataStore
    }

    fun getDataStore(): DataStore<Preferences> {
        return dataStore
    }
}