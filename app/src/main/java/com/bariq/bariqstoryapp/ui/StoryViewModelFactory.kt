package com.bariq.bariqstoryapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bariq.bariqstoryapp.data.repository.StoryRepository
import com.bariq.bariqstoryapp.data.repository.UserRepository
import com.bariq.bariqstoryapp.di.StoryInjection
import com.bariq.bariqstoryapp.di.UserInjection
import com.bariq.bariqstoryapp.ui.main.MainViewModel
import com.bariq.bariqstoryapp.ui.story.StoryViewModel

class StoryViewModelFactory private constructor(private val userRepository: UserRepository, private val storyRepository: StoryRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userRepository, storyRepository) as T
            }
            modelClass.isAssignableFrom(StoryViewModel::class.java) -> {
                StoryViewModel(userRepository, storyRepository) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }
        }
    }

    companion object {
        @Volatile
        private var instance: StoryViewModelFactory? = null
        fun getInstance(context: Context): StoryViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: StoryViewModelFactory(UserInjection.providePreferences(context), StoryInjection.provideRepository())
            }.also { instance = it }
    }
}