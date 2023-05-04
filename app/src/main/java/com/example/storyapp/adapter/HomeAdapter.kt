package com.example.storyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.data.ListStory
import com.example.storyapp.data.Story
import com.example.storyapp.databinding.ListStoryBinding

class HomeAdapter: RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private val list = ArrayList<Story>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setListData(stories: ArrayList<Story>) {
        list.clear()
        list.addAll(stories)
        notifyDataSetChanged()
    }


    inner class ViewHolder(private val binding: ListStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Story) {

            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(item)
            }

            binding.judul.text = item.name
            binding.deskripsi.text = item.description
            Glide.with(itemView)
                .load(item.photoUrl)
                .into(binding.picture)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Story)
    }
}