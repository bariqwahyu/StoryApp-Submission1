package com.bariq.bariqstoryapp.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bariq.bariqstoryapp.R
import com.bariq.bariqstoryapp.data.response.ListStoryItem
import com.bariq.bariqstoryapp.databinding.ActivityDetailBinding
import com.bariq.bariqstoryapp.withDateFormat
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.title = resources.getString(R.string.app_nameDetail)

        val story = intent.getParcelableExtra<ListStoryItem>(EXTRA_STORY)
        binding.apply {
            tvNameDetail.text = story?.name
            tvCreated.withDateFormat(story?.createdAt.toString())
            tvDesc.text = story?.description
        }
        Glide.with(this)
            .load(story?.photoUrl)
            .into(binding.imgDetail)

    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}