package com.andre.storyshare.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.andre.storyshare.R
import com.andre.storyshare.databinding.ActivityHomeBinding
import com.andre.storyshare.ui.viewmodel.ViewModelFactory
import com.andre.storyshare.ui.viewmodel.home.HomeViewModel

private lateinit var binding: ActivityHomeBinding
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setBottomNav()
    }
    private fun setBottomNav() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.homeFragmentContainerView) as NavHostFragment
        val navHostMainController = navHostFragment.navController
        binding.homeBottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    navHostMainController.navigate(R.id.homeFragment)
                    true
                }
                R.id.postFragment -> {
                    navHostMainController.navigate(R.id.postFragment)
                    true
                }
                R.id.profileFragment -> {
                    navHostMainController.navigate(R.id.profileFragment)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
}