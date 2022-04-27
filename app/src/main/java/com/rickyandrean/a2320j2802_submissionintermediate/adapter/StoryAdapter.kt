package com.rickyandrean.a2320j2802_submissionintermediate.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.rickyandrean.a2320j2802_submissionintermediate.databinding.ItemRowStoryBinding
import com.rickyandrean.a2320j2802_submissionintermediate.model.ListStoryItem
import com.rickyandrean.a2320j2802_submissionintermediate.ui.detail.DetailActivity

class StoryAdapter : PagingDataAdapter<ListStoryItem, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {
    inner class StoryViewHolder(private val binding: ItemRowStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ListStoryItem) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(data.photoUrl)
                    .into(ivStory)
                tvName.text = data.name
                tvDescription.text = data.description
            }

            with(itemView) {
                setOnClickListener {
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            context as Activity,
                            Pair(binding.ivStory, "photo"),
                            Pair(binding.tvName, "name"),
                            Pair(binding.tvDescription, "description"),
                        )

                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.STORY, data)
                    context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding =
            ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}