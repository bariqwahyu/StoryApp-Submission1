package com.bariq.bariqstoryapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bariq.bariqstoryapp.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository): ViewModel() {
    fun setToken(token: String, isLogin: Boolean) {
        viewModelScope.launch {
            userRepository.setToken(token, isLogin)
        }
    }

    fun getToken(): LiveData<String> {
        return userRepository.getToken().asLiveData()
    }

    fun login(email: String, password: String) = userRepository.login(email, password)
}