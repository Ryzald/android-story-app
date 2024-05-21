package com.rizaldi.mystoryapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rizaldi.mystoryapp.data.Result
import com.rizaldi.mystoryapp.data.UserRepository
import com.rizaldi.mystoryapp.data.preferences.UserModel
import com.rizaldi.mystoryapp.data.preferences.UserPreference
import com.rizaldi.mystoryapp.data.response.StoryItem
import com.rizaldi.mystoryapp.data.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    private lateinit var storyList: LiveData<Result<List<StoryItem>>>

    fun getStories(): LiveData<Result<List<StoryItem>>> {
        viewModelScope.launch {
            storyList = repository.getStories()

        }
        return storyList
    }


    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun refrestStory() = viewModelScope.launch {
        repository.getStories()
    }

    val story : LiveData<PagingData<StoryItem>> = repository.getStoriesPaging().cachedIn(viewModelScope)


}