package com.bariq.bariqstoryapp.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bariq.bariqstoryapp.data.repository.StoryRepository
import com.bariq.bariqstoryapp.data.repository.UserRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel(private val userRepository: UserRepository, private val storyRepository: StoryRepository) : ViewModel() {
    fun getToken() : LiveData<String> {
        return userRepository.getToken().asLiveData()
    }

    fun addStory(token: String, photo: MultipartBody.Part, desc: RequestBody) = storyRepository.addStories(token, photo, desc)
}