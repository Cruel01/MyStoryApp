package com.example.storyapp.paging

import android.content.Context
import com.example.storyapp.network.ApiConfig
import com.example.storyapp.utils.UserPref
import com.example.storyapp.viewModel.Repository

object Injection {

    fun provideRepository(context: Context) : Repository {
        val storiesDatabase = Database.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        val userPreference = UserPref(context)
        return Repository(storiesDatabase, apiService, userPreference)
    }
}