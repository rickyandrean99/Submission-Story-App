package com.rickyandrean.a2320j2802_submissionintermediate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rickyandrean.a2320j2802_submissionintermediate.databinding.ItemRowStoryBinding
import com.rickyandrean.a2320j2802_submissionintermediate.model.StoryResponse

class StoryAdapter(private val stories: ArrayList<StoryResponse>): RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {
    inner class StoryViewHolder(var binding: ItemRowStoryBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        // holder.binding
    }

    override fun getItemCount(): Int = stories.size
}