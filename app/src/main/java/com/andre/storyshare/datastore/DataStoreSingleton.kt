package com.andre.storyshare.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

object DataStoreSingleton {
    private lateinit var dataStore: DataStore<Preferences>
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var context: Context

    fun init(context: Context) {
        this.context = context
        dataStore = context.dataStore
    }

    fun getDataStore(): DataStore<Preferences> {
        init(context)
        return dataStore
    }
}