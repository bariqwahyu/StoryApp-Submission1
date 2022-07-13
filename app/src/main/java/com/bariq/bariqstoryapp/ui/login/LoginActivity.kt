package com.bariq.bariqstoryapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bariq.bariqstoryapp.R
import com.bariq.bariqstoryapp.data.Result
import com.bariq.bariqstoryapp.databinding.ActivityLoginBinding
import com.bariq.bariqstoryapp.loadAnim
import com.bariq.bariqstoryapp.ui.UserViewModelFactory
import com.bariq.bariqstoryapp.ui.main.MainActivity
import com.bariq.bariqstoryapp.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        val factory: UserViewModelFactory = UserViewModelFactory.getInstance(this)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        loginViewModel.getToken().observe(this) { token ->
            if (token.isNotEmpty()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmailLogin.text.toString()
            val password = binding.etPasswordLogin.text.toString()
            when {
                email.isEmpty() -> {
                    binding.etEmailLogin.error = resources.getString(R.string.input_message, "Email")
                }
                password.isEmpty() -> {
                    binding.etPasswordLogin.error = resources.getString(R.string.input_message, "Password")
                }
                else -> {
                    loginViewModel.login(email, password).observe(this) { result ->
                        if (result != null) {
                            when(result) {
                                is Result.Loading -> {
                                    showLoading(true)
                                }
                                is Result.Success -> {
                                    showLoading(false)
                                    val data = result.data
                                    if (data.error) {
                                        Toast.makeText(this@LoginActivity, data.message, Toast.LENGTH_LONG).show()
                                    }
                                    else {
                                        val token = data.loginResult?.token ?: ""
                                        loginViewModel.setToken(token, true)
                                    }
                                }
                                is Result.Error -> {
                                    showLoading(false)
                                    Toast.makeText(this, resources.getString(R.string.login_error), Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }
        }

        binding.tvRegLogin.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imgLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvLoginPage, View.ALPHA, 1f).setDuration(500)
        val tvEmail = ObjectAnimator.ofFloat(binding.tvEmailLogin, View.ALPHA, 1f).setDuration(500)
        val etEmail = ObjectAnimator.ofFloat(binding.etEmailLogin, View.ALPHA, 1f).setDuration(500)
        val tvPassword = ObjectAnimator.ofFloat(binding.tvPasswordLogin, View.ALPHA, 1f).setDuration(500)
        val etPassword = ObjectAnimator.ofFloat(binding.etPasswordLogin, View.ALPHA, 1f).setDuration(500)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val tvReg = ObjectAnimator.ofFloat(binding.tvTxtReg, View.ALPHA, 1f).setDuration(500)
        val tvRegLog = ObjectAnimator.ofFloat(binding.tvRegLogin, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title, tvEmail, etEmail, tvPassword, etPassword, btnLogin, tvReg, tvRegLog)
            startDelay = 500
        }.start()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            etEmailLogin.isEnabled = !isLoading
            etPasswordLogin.isEnabled = !isLoading
            btnLogin.isEnabled = !isLoading

            if (isLoading) {
                progressBarLayout.loadAnim(true)
            } else {
                progressBarLayout.loadAnim(false)
            }
        }
    }
}