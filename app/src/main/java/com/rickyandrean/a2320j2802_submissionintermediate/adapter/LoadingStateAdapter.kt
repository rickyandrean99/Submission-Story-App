package com.rickyandrean.a2320j2802_submissionintermediate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rickyandrean.a2320j2802_submissionintermediate.databinding.PagingLoadingBinding

class LoadingStateAdapter(private val refresh: () -> Unit) :
    LoadStateAdapter<LoadingStateAdapter.LoadingStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingStateViewHolder {
        val binding =
            PagingLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadingStateViewHolder(binding, refresh)
    }

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class LoadingStateViewHolder(private val binding: PagingLoadingBinding, refresh: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.btnRefresh.setOnClickListener { refresh.invoke() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.msgError.text = loadState.error.localizedMessage
            }

            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.btnRefresh.isVisible = loadState is LoadState.Error
            binding.msgError.isVisible = loadState is LoadState.Error
        }
    }
}