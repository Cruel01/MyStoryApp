package com.example.storyapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.utils.UserPref

class UniversalFactory(private val userPreference: UserPref) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UniversalVM::class.java)) {
            return UniversalVM(userPreference) as T
        }
        throw IllegalArgumentException("Unknown Class")
    }
}