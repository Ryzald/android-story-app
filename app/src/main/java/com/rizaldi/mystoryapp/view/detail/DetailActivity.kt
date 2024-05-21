package com.rizaldi.mystoryapp.view.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.rizaldi.mystoryapp.data.response.StoryItem
import com.rizaldi.mystoryapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {


    companion object {
        const val EXTRA_NAME = "extra_nama"
        const val EXTRA_URL = "extra_url"
        const val EXTRA_DESC = "extra_desc"
    }

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyName = intent.getStringExtra(EXTRA_NAME)
        val storyUrl = intent.getStringExtra(EXTRA_URL)
        val storyDesc = intent.getStringExtra(EXTRA_DESC)

        binding.apply {
            tvTitleDetail.text = storyName
            tvDescDetail.text = storyDesc
        }
        Glide.with(this)
            .load(storyUrl)
            .into(binding.ivDetail)

    }

}
