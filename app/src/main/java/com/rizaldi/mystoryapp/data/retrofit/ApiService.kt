package com.rizaldi.mystoryapp.data.retrofit

import com.rizaldi.mystoryapp.data.response.ResponseDetail
import com.rizaldi.mystoryapp.data.response.ResponseFileUpload
import com.rizaldi.mystoryapp.data.response.ResponseRegister
import com.rizaldi.mystoryapp.data.response.ResponseLogin
import com.rizaldi.mystoryapp.data.response.ResponseStory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("register")
    @FormUrlEncoded
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): ResponseRegister


    @POST("login")
    @FormUrlEncoded
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): ResponseLogin

    @GET("stories")
    fun getStories(
        @Query("location") location: Int = 1,
    ): Call<ResponseStory>

    @GET("stories")
    suspend fun getStoriesPaging(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ) : ResponseStory

    @GET("stories/{id}")
    suspend fun getDetailStories(
        @Path("id") id: String
    ): Response<ResponseDetail>


    @Multipart
    @POST("stories")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): ResponseFileUpload
}