package com.andre.storyshare.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.andre.storyshare.data.model.DataStory
import com.andre.storyshare.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val story = intent.getParcelableExtra<DataStory>("story")
        if (story != null){
            binding.detailName.text = story.name
            binding.detailDate.text = story.createdAt
            binding.detailDesc.text = story.description
            Glide.with(this).load(story.photoUrl).into(binding.detailPicture)
        }
        binding.detailTopAppBar.setNavigationOnClickListener {
            onBackPressed()
            finish()
        }
    }
}