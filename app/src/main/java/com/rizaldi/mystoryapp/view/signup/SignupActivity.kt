package com.rizaldi.mystoryapp.view.signup

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.rizaldi.mystoryapp.ViewModelFactory
import com.rizaldi.mystoryapp.data.Result
import com.rizaldi.mystoryapp.data.preferences.UserModel
import com.rizaldi.mystoryapp.data.retrofit.ApiConfig
import com.rizaldi.mystoryapp.databinding.ActivitySignupBinding
import com.rizaldi.mystoryapp.util.TextMessage
import com.rizaldi.mystoryapp.view.login.LoginActivity
import com.rizaldi.mystoryapp.view.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val viewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
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
        binding.signupButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val name = binding.nameEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            if (email.isNotEmpty() && name.isNotEmpty() && password.isNotEmpty()) {

                showLoading(true)

                viewModel.signup(name, email, password).observe(this@SignupActivity) {
                    when (it) {
                        is Result.Loading -> {
                            showLoading(true)
                        }

                        is Result.Error -> {
                            Toast.makeText(this@SignupActivity, it.error, Toast.LENGTH_SHORT).show()
                            showLoading(false)
                        }

                        is Result.Success -> {
                            Toast.makeText(this@SignupActivity, it.data, Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                            startActivity(intent)

                        }
                    }
                }
            }
        }
    }

    private fun showLoading(loading: Boolean) {
        if (loading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.signupButton.visibility = View.INVISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
            binding.signupButton.visibility = View.VISIBLE
        }
    }
}