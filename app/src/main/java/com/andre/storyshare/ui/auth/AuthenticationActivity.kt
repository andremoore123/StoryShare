package com.andre.storyshare.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.andre.storyshare.databinding.ActivityAuthenticationBinding

private lateinit var binding: ActivityAuthenticationBinding

class AuthenticationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}