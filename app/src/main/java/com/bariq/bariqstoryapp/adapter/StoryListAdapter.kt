package com.bariq.bariqstoryapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bariq.bariqstoryapp.data.response.ListStoryItem
import com.bariq.bariqstoryapp.databinding.ItemStoryBinding
import com.bariq.bariqstoryapp.ui.detail.DetailActivity
import com.bariq.bariqstoryapp.withDateFormat
import com.bumptech.glide.Glide

class StoryListAdapter(private val listStory: ArrayList<ListStoryItem>) : RecyclerView.Adapter<StoryListAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listStory[position])
    }

    override fun getItemCount(): Int = listStory.size

    class ListViewHolder(private var binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .into(binding.imgStory)
            binding.apply {
                tvRvName.text = story.name
                tvRvCreate.withDateFormat(story.createdAt.toString())
                tvRvDesc.text = story.description
            }

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_STORY, story)
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.imgStory, "image"),
                        Pair(binding.tvRvName, "name"),
                        Pair(binding.tvRvCreate, "create"),
                        Pair(binding.tvRvDesc, "desc")
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }
}