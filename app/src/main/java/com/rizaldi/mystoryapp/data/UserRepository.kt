package com.rizaldi.mystoryapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.Gson
import com.rizaldi.mystoryapp.ViewModelFactory
import com.rizaldi.mystoryapp.data.preferences.UserModel
import com.rizaldi.mystoryapp.data.preferences.UserPreference
import com.rizaldi.mystoryapp.data.response.ResponseLogin
import com.rizaldi.mystoryapp.data.response.ResponseRegister
import com.rizaldi.mystoryapp.data.response.ResponseStory
import com.rizaldi.mystoryapp.data.response.StoryItem
import com.rizaldi.mystoryapp.data.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {
    private val resultStory = MutableLiveData<Result<List<StoryItem>>>()
    private val resultApi = MutableLiveData<Result<String>>()


    fun getStoriesPaging(): LiveData<PagingData<StoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
    }

    suspend fun login(email: String, password: String): LiveData<Result<String>> {
        try {
            val successResponse = apiService.login(email, password)
            Log.d(TAG, "login: $successResponse")
            val user = UserModel(email, successResponse.loginResult.token, true)
            Log.d(TAG + "cek token", successResponse.loginResult.token)
            userPreference.saveSession(user)
            resultApi.value = Result.Success(successResponse.message)
            // menghapus token yang kosong ketika login agar ketika pemanggilan selanjutnya memanggil instance lagi agar mendapatkan token yang baru
            instance = null
            ViewModelFactory.removeInstance()

        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ResponseLogin::class.java)
            resultApi.value = Result.Error(errorResponse.message)
            Log.d(TAG, "error: ${errorResponse.message}")
        } catch (e: SocketTimeoutException) {
            Log.d(TAG, "timeout: ${e.message}")
            resultApi.value = Result.Error("Timeout")
        }
        return resultApi
    }

    suspend fun signUp(name: String, email: String, password: String): LiveData<Result<String>> {
        try {
            val response = apiService.register(name, email, password)
            resultApi.value = Result.Success(response.message)
            Log.d(TAG, "pendaftaran berhasil")
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val responseError = Gson().fromJson(errorBody, ResponseRegister::class.java)
            resultApi.value = Result.Error(responseError.message)
            Log.e(TAG, "error signup $responseError")
        }
        return resultApi
    }


    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    fun getStories(): LiveData<Result<List<StoryItem>>> {
        resultStory.value = Result.Loading
        val client = apiService.getStories(1)
        client.enqueue(object : Callback<ResponseStory> {
            override fun onResponse(call: Call<ResponseStory>, response: Response<ResponseStory>) {
                if (response.isSuccessful) {
                    if (response.body()?.error != true) {
                        response.body()?.let {
                            resultStory.value = Result.Success(it.listStory)
                            Log.d(TAG, "getStories Successful: ${it.message}")
                        }
                    } else {
                        resultStory.value =
                            Result.Error("getStories Error: ${response.body()?.message}")
                    }
                } else {
                    resultStory.value =
                        Result.Error("getStories Failed: ${response.body()?.message}")
                }

            }

            override fun onFailure(call: Call<ResponseStory>, t: Throwable) {
                resultStory.value = Result.Error("getStories onFailure: ${t.message}")
            }

        })
        return resultStory
    }


    suspend fun logout() {
        userPreference.logout()
        instance = null
        ViewModelFactory.removeInstance()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        private const val TAG = "userRepository"
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}