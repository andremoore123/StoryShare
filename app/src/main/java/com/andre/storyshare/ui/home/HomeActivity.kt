package com.andre.storyshare.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.andre.storyshare.R
import com.andre.storyshare.databinding.ActivityHomeBinding

private lateinit var binding: ActivityHomeBinding
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}