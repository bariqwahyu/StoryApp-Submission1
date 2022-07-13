package com.bariq.bariqstoryapp.retrofit

import com.bariq.bariqstoryapp.data.response.AddStoryResponse
import com.bariq.bariqstoryapp.data.response.GetStoriesResponse
import com.bariq.bariqstoryapp.data.response.LoginResponse
import com.bariq.bariqstoryapp.data.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getStories(@Header("Authorization") token: String): GetStoriesResponse

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Header("Authorization") Authorization: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): AddStoryResponse
}