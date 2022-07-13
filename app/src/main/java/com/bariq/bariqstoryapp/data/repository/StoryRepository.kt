package com.bariq.bariqstoryapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bariq.bariqstoryapp.data.Result
import com.bariq.bariqstoryapp.data.response.AddStoryResponse
import com.bariq.bariqstoryapp.data.response.GetStoriesResponse
import com.bariq.bariqstoryapp.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(private val apiService: ApiService) {

    fun getStories(token: String) : LiveData<Result<GetStoriesResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.getStories("Bearer $token")
            emit(Result.Success(result))
        } catch (e: Exception) {
            Log.d("StoryRepository", "getStories: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun addStories(token: String, photo: MultipartBody.Part, desc: RequestBody) : LiveData<Result<AddStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.addStory("Bearer $token", photo, desc)
            emit(Result.Success(result))
        } catch (e: Exception) {
            Log.d("StoryRepository", "addStories: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(apiService: ApiService): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService)
            }.also { instance = it }
    }
}