package com.rizaldi.mystoryapp.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rizaldi.mystoryapp.R
import com.rizaldi.mystoryapp.view.welcome.WelcomeActivity
import com.rizaldi.mystoryapp.ViewModelFactory
import com.rizaldi.mystoryapp.adapter.ListStoryAdapter
import com.rizaldi.mystoryapp.adapter.LoadingStateAdapter
import com.rizaldi.mystoryapp.databinding.ActivityMainBinding
import com.rizaldi.mystoryapp.view.addStory.AddStoryActivity
import com.rizaldi.mystoryapp.view.map.MapsActivity

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupMenuClickListener()
        setupView()

        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)

        }

        val adapter = ListStoryAdapter()
        binding.rvStory.adapter =
            adapter.withLoadStateFooter(footer = LoadingStateAdapter { adapter.retry() })
        binding.rvStory.layoutManager = LinearLayoutManager(this)

        viewModel.story.observe(this) {
            adapter.submitData(lifecycle, it)
            binding.progressBar.visibility = View.GONE


        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.refrestStory()
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


    private fun setupMenuClickListener() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu1 -> {
                    viewModel.logout()
                    val intentDestination = Intent(this, WelcomeActivity::class.java)
                    startActivity(intentDestination)
                    false
                }

                R.id.menuMaps -> {
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                    false
                }

                else -> false
            }
        }
    }


}