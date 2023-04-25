package com.andre.storyshare

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.andre.storyshare.databinding.ActivityMainBinding
import com.andre.storyshare.datastore.DataStoreManager
import com.andre.storyshare.datastore.DataStoreSingleton
import com.andre.storyshare.ui.auth.AuthenticationActivity
import com.andre.storyshare.ui.main.HomeActivity
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
//        binding.mainStartButton.setOnClickListener {
//            val intent = Intent(this, AuthenticationActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
        setupView()
        setupAction()
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.mainStartButton.setOnClickListener {
            val intent = Intent(this@MainActivity, AuthenticationActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.mainLogo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val appName = ObjectAnimator.ofFloat(binding.mainAppName, View.ALPHA, 1f).setDuration(500)
        val landingText = ObjectAnimator.ofFloat(binding.mainLandingText, View.ALPHA, 1f).setDuration(500)
        val startButton = ObjectAnimator.ofFloat(binding.mainStartButton, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(startButton, appName, landingText)
        }

        AnimatorSet().apply {
            playSequentially(appName, landingText, together)
            start()
        }
    }
}