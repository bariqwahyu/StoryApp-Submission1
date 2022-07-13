package com.bariq.bariqstoryapp.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bariq.bariqstoryapp.R
import com.bariq.bariqstoryapp.data.Result
import com.bariq.bariqstoryapp.databinding.ActivityRegisterBinding
import com.bariq.bariqstoryapp.loadAnim
import com.bariq.bariqstoryapp.ui.UserViewModelFactory
import com.bariq.bariqstoryapp.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
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
        registerViewModel = ViewModelProvider(this, factory)[RegisterViewModel::class.java]
    }

    private fun setupAction() {
        binding.btnRegister.setOnClickListener {
            val name = binding.etNameReg.text.toString()
            val email = binding.etEmailReg.text.toString()
            val password = binding.etPasswordReg.text.toString()
            when {
                name.isEmpty() -> {
                    binding.etNameReg.error = resources.getString(R.string.input_message, "Name")
                }
                email.isEmpty() -> {
                    binding.etEmailReg.error = resources.getString(R.string.input_message, "Email")
                }
                password.isEmpty() -> {
                    binding.etPasswordReg.error = resources.getString(R.string.input_message, "Password")
                }
                else -> {
                    registerViewModel.register(name, email, password).observe(this) { result ->
                        if (result != null) {
                            when(result) {
                                is Result.Loading -> {
                                    showLoading(true)
                                }
                                is Result.Success -> {
                                    showLoading(false)
                                    val data = result.data
                                    if (data.error) {
                                        Toast.makeText(this@RegisterActivity, data.message, Toast.LENGTH_LONG).show()
                                    }
                                    else {
                                        AlertDialog.Builder(this).apply {
                                            setTitle(resources.getString(R.string.reg_title))
                                            setMessage(resources.getString(R.string.reg_message))
                                            setPositiveButton(resources.getString(R.string.reg_action)) { _, _ ->
                                                finish()
                                            }
                                            create()
                                            show()
                                        }
                                    }
                                }
                                is Result.Error -> {
                                    showLoading(false)
                                    Toast.makeText(this, resources.getString(R.string.register_error), Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }
        }

        binding.tvLoginReg.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imgRegister, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvRegPage, View.ALPHA, 1f).setDuration(500)
        val tvName = ObjectAnimator.ofFloat(binding.tvNameReg, View.ALPHA, 1f).setDuration(500)
        val etName = ObjectAnimator.ofFloat(binding.etNameReg, View.ALPHA, 1f).setDuration(500)
        val tvEmail = ObjectAnimator.ofFloat(binding.tvEmailReg, View.ALPHA, 1f).setDuration(500)
        val etEmail = ObjectAnimator.ofFloat(binding.etEmailReg, View.ALPHA, 1f).setDuration(500)
        val tvPassword = ObjectAnimator.ofFloat(binding.tvPasswordReg, View.ALPHA, 1f).setDuration(500)
        val etPassword = ObjectAnimator.ofFloat(binding.etPasswordReg, View.ALPHA, 1f).setDuration(500)
        val btnRegister = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)
        val tvLogin = ObjectAnimator.ofFloat(binding.tvTxtLogin, View.ALPHA, 1f).setDuration(500)
        val tvLoginReg = ObjectAnimator.ofFloat(binding.tvLoginReg, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title, tvName, etName, tvEmail, etEmail, tvPassword, etPassword, btnRegister, tvLogin, tvLoginReg)
            startDelay = 500
        }.start()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            etNameReg.isEnabled = !isLoading
            etEmailReg.isEnabled = !isLoading
            etPasswordReg.isEnabled = !isLoading
            btnRegister.isEnabled = !isLoading

            if (isLoading) {
                progressBarLayout.loadAnim(true)
            } else {
                progressBarLayout.loadAnim(false)
            }
        }
    }
}