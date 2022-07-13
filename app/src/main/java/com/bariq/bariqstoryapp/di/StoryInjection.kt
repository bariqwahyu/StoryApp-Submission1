package com.bariq.bariqstoryapp.di

import com.bariq.bariqstoryapp.data.repository.StoryRepository
import com.bariq.bariqstoryapp.retrofit.ApiConfig

object StoryInjection {
    fun provideRepository(): StoryRepository {
        val apiService = ApiConfig.getApiService()
        return StoryRepository.getInstance(apiService)
    }
}