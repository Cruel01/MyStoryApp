package com.example.storyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.storyapp.databinding.LoadingBinding

class LoadingAdapter(private val retry: () -> Unit) : LoadStateAdapter<LoadingAdapter.LoadingStateViewHolder>() {
    class LoadingStateViewHolder(private val binding: LoadingBinding, retry: () -> Unit) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.loop.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            binding.loadingg.isVisible = loadState is LoadState.Loading
            binding.loop.isVisible = loadState is LoadState.Error
        }
    }

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingStateViewHolder {
        val binding = LoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadingStateViewHolder(binding, retry)
    }
}