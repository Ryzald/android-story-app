package com.rizaldi.mystoryapp.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rizaldi.mystoryapp.data.Result
import com.rizaldi.mystoryapp.data.UserRepository
import com.rizaldi.mystoryapp.data.preferences.UserModel
import kotlinx.coroutines.launch

class SignupViewModel(private val repository: UserRepository): ViewModel() {
    private val resultApi = MutableLiveData<Result<String>>()

    fun signup(name : String, email: String, password: String) : LiveData<Result<String>>{
        resultApi.value = Result.Loading
        viewModelScope.launch {
            resultApi.value = repository.signUp(name,email,password).value
        }

        return resultApi
    }

    fun getSession(): LiveData<UserModel> = repository.getSession().asLiveData()

}