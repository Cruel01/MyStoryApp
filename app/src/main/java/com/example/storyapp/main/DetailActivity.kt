package com.example.storyapp.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.storyapp.databinding.ActivityDetailBinding
import com.example.storyapp.utils.UserPref
import com.example.storyapp.viewModel.UniversalFactory
import com.example.storyapp.viewModel.UniversalVM

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var userPref: UserPref
    private lateinit var viewmodel: UniversalVM

    companion object {
        const val EXTRA_ID ="EXTRA_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra(EXTRA_ID)

        userPref = UserPref(this)
        viewmodel = ViewModelProvider(this, UniversalFactory(this))[UniversalVM::class.java]

        if (id != null) {
            viewmodel.getStory(id)
        }

        viewmodel.detailStory.observe(this) {
            binding.apply {
                name.text = it.name
                desc.text = it.description
                Glide.with(this@DetailActivity)
                    .load(it.photoUrl)
                    .into(binding.postPic)
            }
        }




    }
}