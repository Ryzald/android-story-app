package com.rizaldi.mystoryapp.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizaldi.mystoryapp.data.Result
import com.rizaldi.mystoryapp.data.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val resultApi = MutableLiveData<Result<String>>()

    fun login(email: String, password: String): LiveData<Result<String>> {
        resultApi.value = Result.Loading
        viewModelScope.launch {
            resultApi.value = repository.login(email, password).value
        }
        return resultApi
    }

}