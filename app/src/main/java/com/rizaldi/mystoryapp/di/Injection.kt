package com.rizaldi.mystoryapp.di

import android.content.Context
import com.rizaldi.mystoryapp.data.UserRepository
import com.rizaldi.mystoryapp.data.preferences.UserPreference
import com.rizaldi.mystoryapp.data.preferences.dataStore
import com.rizaldi.mystoryapp.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val session = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(session.token)
        return UserRepository.getInstance(apiService,pref)
    }
}