package com.rizaldi.mystoryapp.view.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.rizaldi.mystoryapp.data.UserRepository
import com.rizaldi.mystoryapp.data.preferences.UserModel

class WelcomeViewModel(private val repository: UserRepository) : ViewModel() {

    fun getSession(): LiveData<UserModel> = repository.getSession().asLiveData()
}