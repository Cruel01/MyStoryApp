package com.example.storyapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.*
import com.example.storyapp.utils.UserPref
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UniversalVM(private val repository: IRepository): ViewModel() {

    val storyList = repository.storyList
    val uploadStory = repository.uploadStory
    val detailStory = repository.detailStory
    val nextGo = repository.nextGo
    val listStory = repository.listStory

    fun register(email: String, username: String, password: String) {
        repository.register(email, username, password)
    }

    fun login(email: String, password: String) {
        repository.login(email, password)
    }

    fun getStory(id: String) {
        repository.getStory(id)
    }

    fun getAllStory() : LiveData<PagingData<Story>> = repository.getAllStory().cachedIn(viewModelScope)

    fun postStory(description: RequestBody, file: MultipartBody.Part) {
        repository.postStory(description, file)
    }

    fun loadStories() {
        repository.loadStories()
    }

}

