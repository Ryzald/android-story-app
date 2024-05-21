package com.rizaldi.mystoryapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rizaldi.mystoryapp.data.response.StoryItem
import com.rizaldi.mystoryapp.databinding.StoryItemBinding
import com.rizaldi.mystoryapp.view.detail.DetailActivity

class ListStoryAdapter : PagingDataAdapter<StoryItem, ListStoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(private val binding: StoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: StoryItem) {
            binding.apply {
                titleTextView.text = story.name
                descriptionTextView.text = story.description
                Glide.with(itemView)
                    .load(story.photoUrl)
                    .into(imageView)
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_NAME, story.name)
                    intent.putExtra(DetailActivity.EXTRA_DESC, story.description)
                    intent.putExtra(DetailActivity.EXTRA_URL, story.photoUrl)
                    itemView.context.startActivity(intent)
                }

            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryItem>() {
            override fun areItemsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}