package com.example.storyapp.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.paging.Injection
import com.example.storyapp.utils.UserPref

class UniversalFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UniversalVM::class.java)) {
            return UniversalVM(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}