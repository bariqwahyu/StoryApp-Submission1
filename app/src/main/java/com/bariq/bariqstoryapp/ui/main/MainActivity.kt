package com.bariq.bariqstoryapp.ui.main

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bariq.bariqstoryapp.R
import com.bariq.bariqstoryapp.adapter.StoryListAdapter
import com.bariq.bariqstoryapp.data.Result
import com.bariq.bariqstoryapp.data.response.ListStoryItem
import com.bariq.bariqstoryapp.databinding.ActivityMainBinding
import com.bariq.bariqstoryapp.ui.StoryViewModelFactory
import com.bariq.bariqstoryapp.ui.login.LoginActivity
import com.bariq.bariqstoryapp.ui.story.AddStoryActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
    }

    private fun setupViewModel() {
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvStory.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvStory.layoutManager = LinearLayoutManager(this)
        }

        val factory: StoryViewModelFactory = StoryViewModelFactory.getInstance(this)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        mainViewModel.isLogin().observe(this){
            if (!it){
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        mainViewModel.getToken().observe(this) { token ->
            if (token.isNotEmpty()) {
                mainViewModel.getStories(token).observe(this) { result ->
                    if (result != null) {
                        when(result) {
                            is Result.Loading -> {
                                showLoading(true)
                            }
                            is Result.Success -> {
                                showLoading(false)
                                val data = result.data.listStory
                                val storyListAdapter = StoryListAdapter(data as ArrayList<ListStoryItem>)
                                binding.rvStory.adapter = storyListAdapter
                            }
                            is Result.Error -> {
                                showLoading(false)
                                Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_story -> {
                startActivity(Intent(this, AddStoryActivity::class.java))
                true
            }
            R.id.settings -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            R.id.logout -> {
                mainViewModel.logout()
                true
            }
            else -> true
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}