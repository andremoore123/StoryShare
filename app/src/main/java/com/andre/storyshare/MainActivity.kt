package com.andre.storyshare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.andre.storyshare.databinding.ActivityMainBinding
import com.andre.storyshare.datastore.DataStoreManager
import com.andre.storyshare.datastore.DataStoreSingleton
import com.andre.storyshare.ui.auth.AuthenticationActivity
import com.andre.storyshare.ui.home.HomeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        DataStoreSingleton.init(applicationContext)
        val dataStore = DataStoreSingleton.getDataStore()
        val dataStoreManager = DataStoreManager(dataStore)

        runBlocking {
            if (dataStoreManager.getToken().isNotEmpty()){
                val intent = Intent(this@MainActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        binding.mainStartButton.setOnClickListener {
            val intent = Intent(this, AuthenticationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}