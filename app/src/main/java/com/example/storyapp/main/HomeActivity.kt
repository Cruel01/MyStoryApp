package com.example.storyapp.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.adapter.HomeAdapter
import com.example.storyapp.data.Story
import com.example.storyapp.databinding.ActivityHomeBinding
import com.example.storyapp.utils.UserPref
import com.example.storyapp.viewModel.UniversalFactory
import com.example.storyapp.viewModel.UniversalVM

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var userPref: UserPref
    private lateinit var adapter: HomeAdapter
    private lateinit var viewmodel: UniversalVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPref = UserPref(this)
        viewmodel = ViewModelProvider(this, UniversalFactory(userPref))[UniversalVM::class.java]
        viewmodel.getAllStory()



        adapter = HomeAdapter()
        adapter.notifyDataSetChanged()
        adapter.setOnItemClickCallback(object: HomeAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Story) {
                val intent = Intent(this@HomeActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_ID, data.id)
                startActivity(intent)
            }
        })

        val layoutManager = LinearLayoutManager(this)
        binding.apply {
            rvContent.layoutManager = layoutManager
            rvContent.setHasFixedSize(true)
            rvContent.adapter = adapter
        }

        viewmodel.listStory.observe(this) {
            if (it != null) {
                adapter.setListData(it)
            }
        }

        binding.add.setOnClickListener {
            next()
        }

        binding.exit.setOnClickListener{
            nextMain()
        }
    }

    fun next() {
        val intent = Intent(this@HomeActivity, CameraActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun nextMain() {
        userPref.deleteToken()
        val intent = Intent(this@HomeActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}