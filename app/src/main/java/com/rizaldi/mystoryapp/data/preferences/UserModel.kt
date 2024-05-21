package com.rizaldi.mystoryapp.data.preferences

data class UserModel(
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)