package com.rizaldi.mystoryapp.view.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.rizaldi.mystoryapp.data.preferences.UserModel
import com.rizaldi.mystoryapp.data.retrofit.ApiConfig
import com.rizaldi.mystoryapp.ViewModelFactory
import com.rizaldi.mystoryapp.data.Result
import com.rizaldi.mystoryapp.databinding.ActivityLoginBinding
import com.rizaldi.mystoryapp.util.TextMessage
import com.rizaldi.mystoryapp.view.detail.DetailActivity
import com.rizaldi.mystoryapp.view.main.MainActivity
import com.rizaldi.mystoryapp.view.signup.SignupActivity
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
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
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            viewModel.login(email, password).observe(this@LoginActivity) {
                when (it) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.loginButton.visibility = View.INVISIBLE
                    }

                    is Result.Success -> {
                        Log.d("loginSucces", it.data)
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    is Result.Error -> {
                        Log.d("loginerror", it.error)
                        Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.loginButton.visibility = View.VISIBLE
                    }
                }
            }

        }
    }
}


